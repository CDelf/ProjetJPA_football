package fr.diginamic.services;

import fr.diginamic.dao.TirsButsDaoImpl;
import fr.diginamic.model.Equipe;
import fr.diginamic.model.Match;
import fr.diginamic.model.TirsButs;
import fr.diginamic.utils.CheckUtils;
import fr.diginamic.utils.ErreurCollector;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Service métier pour la gestion des tirs aux buts.
 */
public class TirsButsService {

    private final TirsButsDaoImpl tirsDao;
    private final ErreurCollector erreurCollector;

    /**
     * Initialise le service avec un EntityManager
     * @param em EntityManager à utiliser
     */
    public TirsButsService(EntityManager em, ErreurCollector collector) {
        this.tirsDao = new TirsButsDaoImpl(em);
        this.erreurCollector = collector;
    }

    /**
     * Insère une séance de tirs aux buts s'il n'existe pas déjà en base.
     * En cas de doublon, une erreur est loguée
     * @param match désigne le match concerné par le tir aux buts
     * @param equipeCommence désigne l'équipe qui commence à tirer
     * @param vainqueur désigne l'équipe qui remporte le tir aux buts
     * @param ligne indique à quelle ligne du csv l'erreur éventuelle est déclenchée
     * @param fichier indique quel fichier est à l'origine de l'erreur éventuelle
     */
    public void enregistrerTirsButsSiNouveau(
            Match match, Equipe equipeCommence, Equipe vainqueur, String ligne, String fichier
    ) {
        try {
            if(CheckUtils.isNotNull(match) && CheckUtils.isNotNull(vainqueur)) {
                List<TirsButs> resultats = tirsDao.findByMatchAndVainqueur(
                        match.getId(), vainqueur.getId()
                );
                if(resultats.isEmpty()) {
                    TirsButs tirsButs = new TirsButs(match, equipeCommence, vainqueur);
                    tirsDao.insert(tirsButs);
                } else if(resultats.size() > 1) {
                    erreurCollector.log(fichier, ligne,
                            "Doublon : plusieurs tirs aux buts trouvés pour ce match et équipes", "TirsButs");
                }
            } else {
                erreurCollector.log(fichier, ligne,
                        "Erreur dans la recherche: match ou équipe null","TirsButs");
            }
        } catch (Exception e) {
            erreurCollector.log(fichier, ligne, e.getMessage(), "TirsButs");
        }
    }
}
