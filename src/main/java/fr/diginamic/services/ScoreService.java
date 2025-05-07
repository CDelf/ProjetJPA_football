package fr.diginamic.services;

import fr.diginamic.dao.ScoreDaoImpl;
import fr.diginamic.model.Match;
import fr.diginamic.model.Score;
import fr.diginamic.utils.CheckUtils;
import fr.diginamic.utils.ErreurCollector;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Service métier pour la gestion des scores.
 */
public class ScoreService {

    final private ScoreDaoImpl scoreDao;
    final private ErreurCollector erreurCollector;

    /**
     * Initialise le service avec un EntityManager
     * @param em EntityManager à utiliser
     */
    public ScoreService(EntityManager em, ErreurCollector collector) {
        this.scoreDao = new ScoreDaoImpl(em);
        this.erreurCollector = collector;
    }

    /**
     * Insère un score s'il n'existe pas déjà en base.
     * En cas de doublon, une erreur est loguée.
     * @param match désigne le match à l'origine du score
     * @param scoreHote désigne le score final de l'équipe hôte
     * @param scoreInvite désigne le score final de l'équipe invitée
     * @param ligne indique à quelle ligne du csv l'erreur éventuelle est déclenchée
     * @param fichier indique quel fichier est à l'origine de l'erreur éventuelle
     */
    public void enregistrerScoreSiNouveau(
            Match match, int scoreHote, int scoreInvite, String ligne, String fichier
    ){
        try {
            if(CheckUtils.isNotNull(match) && CheckUtils.isNotNegative(scoreHote) && CheckUtils.isNotNegative(scoreInvite)) {
                List<Score> resultats = scoreDao.findByMatchAndScores(match.getId(), scoreHote, scoreInvite);
                if(resultats.isEmpty()) {
                    Score score = new Score(match, scoreHote, scoreInvite);
                    scoreDao.insert(score);
                } else if(resultats.size() > 1) {
                    erreurCollector.log(fichier, ligne,
                            "Doublon : plusieurs scores trouvés pour ce match et ces résultats", "Score");
                }
            } else {
                erreurCollector.log(fichier, ligne,
                        "Erreur dans la recherche: match null ou scores invalides", "Score");
            }
        } catch (Exception e) {
            erreurCollector.log(fichier, ligne, e.getMessage(), "Score");
        }
    }
}
