package fr.diginamic.imports;

import fr.diginamic.model.Equipe;
import fr.diginamic.model.Match;
import fr.diginamic.services.EquipeService;
import fr.diginamic.services.MatchService;
import fr.diginamic.services.TirsButsService;
import fr.diginamic.utils.CheckUtils;
import fr.diginamic.utils.CsvImporter;
import fr.diginamic.utils.DateUtils;
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

    /**
     * Initialise les services des entités concernées par l'import.
     * @param em EntityManager partagé
     */
    public ShootoutsCsvImporter(EntityManager em) {
        this.equipeService = new EquipeService(em);
        this.matchService = new MatchService(em);
        this.tirsButsService = new TirsButsService(em);
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

                if (tokens.length < 5) {
                    throw new IllegalArgumentException("Ligne incomplète : " + ligne);
                }

                String dateStr = tokens[0].trim();
                String homeTeam = tokens[1].trim();
                String awayTeam = tokens[2].trim();
                String winner = tokens[3].trim();
                String firstShooter = tokens[4].trim();

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
               if(CheckUtils.isNotNull(match) && CheckUtils.isNotNull(vainqueur)){
                   tirsButsService.enregistrerTirsButsSiNouveau(match, equipeCommence, vainqueur, ligne, fichier);
               }
            } catch (Exception e){
                System.err.println("Erreur de parsing ligne: " + ligne);
                e.printStackTrace();
            }
        });
    }
}
