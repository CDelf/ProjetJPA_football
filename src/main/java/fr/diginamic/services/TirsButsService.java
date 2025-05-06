package fr.diginamic.services;

import fr.diginamic.dao.ErreurImportDaoImpl;
import fr.diginamic.dao.TirsButsDaoImpl;
import fr.diginamic.model.Equipe;
import fr.diginamic.model.Match;
import fr.diginamic.model.TirsButs;
import fr.diginamic.utils.CheckUtils;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Service métier pour la gestion des tirs aux buts.
 */
public class TirsButsService {

    private final TirsButsDaoImpl tirsDao;
    private final ErreurImportDaoImpl erreurDao;

    /**
     * Initialise le service avec un EntityManager
     * @param em EntityManager à utiliser
     */
    public TirsButsService(EntityManager em) {
        this.tirsDao = new TirsButsDaoImpl(em);
        this.erreurDao = new ErreurImportDaoImpl(em);
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
            if(CheckUtils.isNotNull(match) && CheckUtils.isNotNull(equipeCommence) && CheckUtils.isNotNull(vainqueur)) {
                List<TirsButs> resultats = tirsDao.findByMatchAndEquipes(
                        match.getId(), equipeCommence.getId(), vainqueur.getId()
                );
                if(resultats.isEmpty()) {
                    TirsButs tirsButs = new TirsButs(match, equipeCommence, vainqueur);
                    tirsDao.insert(tirsButs);
                } else if(resultats.size() > 1) {
                    erreurDao.logErreur(fichier, ligne,
                            "Doublon : plusieurs tirs aux buts trouvés pour ce match et équipes", "TirsButs");
                }
            } else {
                erreurDao.logErreur(fichier, ligne,
                        "Erreur dans la recherche: match ou équipe null","TirsButs");
            }
        } catch (Exception e) {
            erreurDao.logErreur(fichier, ligne, e.getMessage(), "TirsButs");
        }
    }
}
