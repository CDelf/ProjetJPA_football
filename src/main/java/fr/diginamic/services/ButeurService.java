package fr.diginamic.services;

import fr.diginamic.dao.ButeurDaoImpl;
import fr.diginamic.model.Buteur;
import fr.diginamic.model.Equipe;
import fr.diginamic.utils.CheckUtils;
import fr.diginamic.utils.ErreurCollector;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Service métier pour la gestion des buteurs.
 */
public class ButeurService {

    private ButeurDaoImpl buteurDao;
    private final ErreurCollector erreurCollector;

    /**
     * Initialise le service avec un EntityManager
     * @param em EntityManager à utiliser
     */
    public ButeurService(EntityManager em, ErreurCollector collector) {
        this.buteurDao = new ButeurDaoImpl(em);
        this.erreurCollector = collector;
    }

    /**
     * Récupère un buteur par son nom et son équipe pour l'import
     * @param nom nom du buteur
     * @param equipe équipe du buteur
     * @return l'instance correspondante
     */
    public Buteur getByNomAndEquipe(String nom, Equipe equipe){
        List<Buteur> buteurs = buteurDao.findByNomAndEquipe(nom, equipe.getId());
        return buteurs.size() == 1 ? buteurs.get(0) : null;
    }

    /**
     * Insère un buteur s'il n'existe pas déjà en base.
     * En cas de doublon, une erreur est loguée.
     * @param nom désigne le nom du buteur
     * @param equipe désigne l'équipe du buteur
     * @param ligne indique à quelle ligne du csv l'erreur éventuelle est déclenchée
     * @param fichier indique quel fichier est à l'origine de l'erreur éventuelle
     */
    public void enregistrerButeurSiNouveau(
            String nom, Equipe equipe, String ligne, String fichier
    ) {
        try {
            if(CheckUtils.isValidString(nom) && CheckUtils.isNotNull(equipe)) {
                List<Buteur> resultats = buteurDao.findByNomAndEquipe(nom, equipe.getId());
                if(resultats.isEmpty()){
                    Buteur buteur = new Buteur(nom, equipe);
                    buteurDao.insert(buteur);
                } else if(resultats.size() > 1 ) {
                    erreurCollector.log(fichier, ligne,
                            "Doublon : plusieurs buteurs trouvés pour ce nom et équipe", "Buteur");
                }
            } else {
                erreurCollector.log(fichier, ligne,
                        "Erreur dans la recherche: nom vide/invalide ou équipe null", "Buteur");
            }
        } catch (Exception e) {
            erreurCollector.log(fichier, ligne, e.getMessage(), "Buteur");
        }
    }

    /**
     * Setter
     * @param buteurDao buteurDao, utilisé pour les tests
     */
    public void setButeurDao(ButeurDaoImpl buteurDao) {
        this.buteurDao = buteurDao;
    }
}
