package fr.diginamic.services;

import fr.diginamic.dao.MatchDaoImpl;
import fr.diginamic.model.Equipe;
import fr.diginamic.model.Match;
import fr.diginamic.utils.CheckUtils;
import fr.diginamic.utils.ErreurCollector;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

/**
 * Service métier pour la gestion des matchs de football.
 */
public class MatchService {

    private MatchDaoImpl matchDao;
    private final ErreurCollector erreurCollector;

    /**
     * Initialise le service avec les DAO nécessaires.
     * @param em EntityManager partagé
     */
    public MatchService(EntityManager em, ErreurCollector collector) {
        this.matchDao = new MatchDaoImpl(em);
        this.erreurCollector = collector;
    }

    /**
     * Récupère un match par sa date et ses équipes pour l'import
     * @param date date du match
     * @param hote équipe qui reçoit le match
     * @param invitee équipe qui rejoint le match
     * @return l'instance de Match correspondante
     */
    public Match getByDateAndEquipes(LocalDate date, Equipe hote, Equipe invitee) {
        List<Match> matchs = matchDao.findByDateAndEquipes(date, hote.getId(), invitee.getId());
        return matchs.size() == 1 ? matchs.get(0) : null;
    }

    /**
     * Insère un match s'il n'existe pas déjà en base.
     * En cas de doublon, une erreur est loguée.
     * @param date date du match
     * @param ville ville où se joue le match
     * @param pays pays où se joue le match
     * @param lieuNeutre vrai si le lieu est neutre
     * @param tournoi nom du tournoi
     * @param equipeHote équipe hôte
     * @param equipeInvitee équipe invitée
     * @param ligne indique à quelle ligne du csv l'erreur éventuelle est déclenchée
     * @param fichier indique quel fichier est à l'origine de l'erreur éventuelle
     */
    public void enregistrerMatchSiNouveau(LocalDate date, String ville, String pays,
                                          boolean lieuNeutre, String tournoi,
                                          Equipe equipeHote, Equipe equipeInvitee,
                                          String ligne, String fichier) {
        try {
            if (CheckUtils.isValidDate(date) && CheckUtils.isNotNull(equipeHote) && CheckUtils.isNotNull(equipeInvitee)) {

                List<Match> resultats = matchDao.findByDateAndEquipes(date, equipeHote.getId(), equipeInvitee.getId());

                if (resultats.isEmpty()) {
                    Match match = new Match(date, ville, pays, lieuNeutre, tournoi, equipeHote, equipeInvitee);
                    matchDao.insert(match);
                } else if (resultats.size() > 1) {
                    erreurCollector.log(fichier, ligne,
                            "Doublon : plusieurs matchs trouvés pour cette date et ces équipes", "Match");
                }
            } else {
                erreurCollector.log(fichier, ligne,
                        "Erreur dans les paramètres : date invalide ou équipe hôte/invitée absente", "Match");
            }
        } catch (Exception e) {
            erreurCollector.log(fichier, ligne, e.getMessage(), "Match");
        }
    }

    /**
     * Setter
     * @param matchDao matchDao, utilisé pour les tests
     */
    public void setMatchDao(MatchDaoImpl matchDao) {
        this.matchDao = matchDao;
    }
}
