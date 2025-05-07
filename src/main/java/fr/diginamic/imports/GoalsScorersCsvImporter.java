package fr.diginamic.imports;

import fr.diginamic.model.Buteur;
import fr.diginamic.model.Equipe;
import fr.diginamic.model.Match;
import fr.diginamic.services.ButService;
import fr.diginamic.services.ButeurService;
import fr.diginamic.services.EquipeService;
import fr.diginamic.services.MatchService;
import fr.diginamic.utils.CheckUtils;
import fr.diginamic.utils.CsvImporter;
import fr.diginamic.utils.DateUtils;
import fr.diginamic.utils.ErreurCollector;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

/**
 * Classe organisant l'import du fichier goalsscorers.csv stocké dans resources
 * concerne les entités Equipe, Match, Buteur et But
 */
public class GoalsScorersCsvImporter {

    private final EquipeService equipeService;
    private final MatchService matchService;
    private final ButeurService buteurService;
    private final ButService butService;
    private final ErreurCollector collector;

    /**
     * Initialise les services des entités concernées par l'import.
     * @param em EntityManager partagé
     */
    public GoalsScorersCsvImporter(EntityManager em, ErreurCollector collector) {
        this.equipeService = new EquipeService(em, collector);
        this.matchService = new MatchService(em, collector);
        this.buteurService = new ButeurService(em, collector);
        this.butService = new ButService(em, collector);
        this.collector = collector;
    }

    /**
     * Import des données par colonnes : correspondance avec attributs,
     * vérification si doublon et import
     * @param fichier correspond au fichier d'import
     */
    public void importer(String fichier) {
        CsvImporter.lireFichier(fichier, ligne -> {
            try{
                String[] tokens = ligne.split(",");

                if (tokens.length < 8) {
                    throw new IllegalArgumentException("Ligne incomplète : " + ligne);
                }

                String dateStr = tokens[0].trim();
                String homeTeam = tokens[1].trim();
                String awayTeam = tokens[2].trim();
                String team = tokens[3].trim();
                String scorer = tokens[4].trim();
                int minute = Integer.parseInt(tokens[5]);
                boolean ownGoal = Boolean.parseBoolean(tokens[6]);
                boolean penalty = Boolean.parseBoolean(tokens[7]);

                // Format date correspondant au csv
                LocalDate date = LocalDate.parse(dateStr, DateUtils.CSV_DATE_FORMATTER);

                // Récupérer équipes et match (import préalable de results.csv)
                Equipe equipeHote = equipeService.getByNom(homeTeam);
                Equipe equipeInvitee = equipeService.getByNom(awayTeam);
                Match match = matchService.getByDateAndEquipes(date, equipeHote, equipeInvitee);

                Equipe equipeButeur = equipeService.getByNom(team);

                //Si match et equipeButeur existants, enregistrer nouveau buteur
                if(CheckUtils.isNotNull(match) && CheckUtils.isNotNull(equipeButeur)) {
                    buteurService.enregistrerButeurSiNouveau(scorer, equipeButeur, ligne, fichier);
                }

                // Récupérer le buteur
                Buteur buteur = buteurService.getByNomAndEquipe(scorer, equipeButeur);

                // Si match et buteur existants, enregistrer nouveau but
                if(CheckUtils.isNotNull(match) && CheckUtils.isNotNull(buteur)){
                    butService.enregistrerButSiNouveau(minute, ownGoal, penalty, match, buteur, ligne, fichier);
                }
            } catch (Exception e) {
                System.err.println("Erreur de parsing ligne: " + ligne);
                e.printStackTrace();
                collector.log("goalsscorers.csv", ligne, e.getMessage(), "GoalScorer");
            }
        });
    }
}
