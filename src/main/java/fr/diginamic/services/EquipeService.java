package fr.diginamic.services;

import fr.diginamic.dao.EquipeDaoImpl;
import fr.diginamic.model.Equipe;
import fr.diginamic.utils.CheckUtils;
import fr.diginamic.utils.ErreurCollector;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Classe de service pour gérer l'insertion conditionnée d'une équipe
 */
public class EquipeService {

    private final EquipeDaoImpl equipeDao;
    private final ErreurCollector erreurCollector;

    /**
     * Initialise le service avec un EntityManager
     * @param em EntityManager à utiliser
     */
    public EquipeService(EntityManager em, ErreurCollector collector) {
        this.equipeDao = new EquipeDaoImpl(em);
        this.erreurCollector = collector;
    }

    /**
     * Récupère une équipe par son nom lors de l'import
     * @param nom désigne le nom de l'équipe à rechercher
     * @return l'instance correspondante 
     */
    public Equipe getByNom(String nom) {
        List<Equipe> equipes = equipeDao.findByName(nom);
        return equipes.size() == 1 ? equipes.get(0) : null;
    }

    /**
     * Vérifie si une équipe de ce nom existe en base :
     * Si aucune trouvée, elle est créée et insérée.
     * Si plusieurs trouvées, une erreur est loguée.
     * Sinon, rien à faire (déjà en base).
     *
     * @param nom nom de l'équipe à traiter
     * @param ligne ligne du fichier CSV
     * @param fichier nom du fichier source
     */
    public void enregistrerEquipeSiNouvelle(String nom, String ligne, String fichier) {
        try {
            if(CheckUtils.isValidString(nom)) {
                List<Equipe> resultats = equipeDao.findByName(nom);

                if (resultats.isEmpty()) {
                    equipeDao.insert(new Equipe(nom));
                } else if (resultats.size() > 1) {
                    erreurCollector.log(
                            fichier,
                            ligne,
                            "Doublon : plusieurs équipes portent le nom '" + nom + "'",
                            "Equipe");
                }
            } else {
                erreurCollector.log(fichier, ligne,
                        "Le nom de l'équipe est vide ou invalide.", "Equipe"
                );
            }
        } catch (Exception e) {
            erreurCollector.log(fichier, ligne, e.getMessage(), "Equipe");
        }
    }
}
