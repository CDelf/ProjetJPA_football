package fr.diginamic.imports;

import fr.diginamic.model.Equipe;
import fr.diginamic.model.Match;
import fr.diginamic.services.EquipeService;
import fr.diginamic.services.MatchService;
import fr.diginamic.services.ScoreService;
import fr.diginamic.utils.CheckUtils;
import fr.diginamic.utils.CsvImporter;
import fr.diginamic.utils.DateUtils;
import fr.diginamic.utils.ErreurCollector;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

/**
 * Classe organisant l'import du fichier result.csv stocké dans resources
 * concerne les entités Equipe, Match et Score
 */
public class ResultCsvImporter {

    private final EquipeService equipeService;
    private final MatchService matchService;
    private final ScoreService scoreService;
    private final ErreurCollector collector;

    /**
     * Initialise les services des entités concernées par l'import.
     * @param em EntityManager partagé
     */
    public ResultCsvImporter(EntityManager em, ErreurCollector collector) {
        this.equipeService = new EquipeService(em, collector);
        this.matchService = new MatchService(em, collector);
        this.scoreService = new ScoreService(em, collector);
        this.collector = collector;
    }

    /**
     * Import des données par colonnes : correspondance avec attributs,
     * vérification si doublon et import
     * @param fichier correspond au fichier d'import
     */
    public void importer(String fichier) {
        CsvImporter.lireFichier(fichier, ligne -> {
            try {
                String[] tokens = ligne.split(",");

                if (tokens.length < 8) {
                    throw new IllegalArgumentException("Ligne incomplète : " + ligne);
                }

                String dateStr = tokens[0].trim();
                String homeTeam = tokens[1].trim();
                String awayTeam = tokens[2].trim();
                int homeScore = Integer.parseInt(tokens[3].trim());
                int awayScore = Integer.parseInt(tokens[4].trim());
                String tournament = tokens[5].trim();
                String city = tokens[6].trim();
                String country = tokens[7].trim();
                boolean neutral = Boolean.parseBoolean(tokens[8].trim());
                // Format date correspondant au csv
                LocalDate date = LocalDate.parse(dateStr, DateUtils.CSV_DATE_FORMATTER);

                // 1. Insérer les équipes si nouvelles
                equipeService.enregistrerEquipeSiNouvelle(homeTeam, ligne, fichier);
                equipeService.enregistrerEquipeSiNouvelle(awayTeam, ligne, fichier);

                // 2. Récupérer les équipes
                Equipe equipeHote = equipeService.getByNom(homeTeam);
                Equipe equipeInvitee = equipeService.getByNom(awayTeam);

                if (CheckUtils.isNotNull(equipeHote) && CheckUtils.isNotNull(equipeInvitee)) {
                    // 3. Insérer le match si nouveau
                    matchService.enregistrerMatchSiNouveau(date, city, country, neutral, tournament,
                            equipeHote, equipeInvitee, ligne, fichier);

                    // 4. Récupérer le match (il vient d'être inséré ou existait déjà)
                    Match match = matchService.getByDateAndEquipes(date, equipeHote, equipeInvitee);

                    if (CheckUtils.isNotNull(match)) {
                        // 5. Insérer le score si nouveau
                        scoreService.enregistrerScoreSiNouveau(match, homeScore, awayScore, ligne, fichier);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur de parsing ligne: " + ligne);
                e.printStackTrace();
                collector.log("results.csv", ligne, e.getMessage(), "Results");
            }
        });
    }
}
