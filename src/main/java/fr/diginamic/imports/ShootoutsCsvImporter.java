package fr.diginamic.imports;

import fr.diginamic.model.Equipe;
import fr.diginamic.model.Match;
import fr.diginamic.services.EquipeService;
import fr.diginamic.services.MatchService;
import fr.diginamic.services.TirsButsService;
import fr.diginamic.utils.CheckUtils;
import fr.diginamic.utils.CsvImporter;
import fr.diginamic.utils.DateUtils;
import fr.diginamic.utils.ErreurCollector;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

/**
 * Classe organisant l'import du fichier shootouts.csv stocké dans resources
 * concerne les entités Equipe, Match et TirsButs
 */
public class ShootoutsCsvImporter {

    private final EquipeService equipeService;
    private final MatchService matchService;
    private final TirsButsService tirsButsService;
    private final ErreurCollector collector;

    /**
     * Initialise les services des entités concernées par l'import.
     * @param em EntityManager partagé
     */
    public ShootoutsCsvImporter(EntityManager em, ErreurCollector collector) {
        this.equipeService = new EquipeService(em, collector);
        this.matchService = new MatchService(em, collector);
        this.tirsButsService = new TirsButsService(em, collector);
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

                if (tokens.length < 4) {
                    throw new IllegalArgumentException("Ligne incomplète : " + ligne);
                }

                String dateStr = tokens[0].trim();
                String homeTeam = tokens[1].trim();
                String awayTeam = tokens[2].trim();
                String winner = tokens[3].trim();
                String firstShooter = tokens.length > 4 ? tokens[4].trim() : "";

                // Format date correspondant au csv
                LocalDate date = LocalDate.parse(dateStr, DateUtils.CSV_DATE_FORMATTER);

                // Récupérer matchs et équipes (import préalable de results.csv)
                Equipe equipeHote = equipeService.getByNom(homeTeam);
                Equipe equipeInvitee = equipeService.getByNom(awayTeam);
                Match match = matchService.getByDateAndEquipes(date, equipeHote, equipeInvitee);

                // identifier équipe qui commence (peut être null dans csv) et vainqueur
                Equipe equipeCommence = CheckUtils.isValidString(firstShooter) ? equipeService.getByNom(firstShooter) : null;
                Equipe vainqueur = equipeService.getByNom(winner);

                // Si match et vainqueur existants, y associer les tirs aux buts
                if (match == null) System.out.println("Match introuvable pour ligne : " + ligne);
                if (vainqueur == null) System.out.println("Vainqueur introuvable : " + winner);
                if(CheckUtils.isNotNull(match) && CheckUtils.isNotNull(vainqueur)){
                   tirsButsService.enregistrerTirsButsSiNouveau(match, equipeCommence, vainqueur, ligne, fichier);
               }
            } catch (Exception e){
                System.err.println("Erreur de parsing ligne: " + ligne);
                e.printStackTrace();
                collector.log("shootouts.csv", ligne, e.getMessage(), "Shootouts");
            }
        });
    }
}
