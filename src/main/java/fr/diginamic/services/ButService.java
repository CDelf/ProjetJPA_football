package fr.diginamic.services;

import fr.diginamic.dao.ButDaoImpl;
import fr.diginamic.dao.ErreurImportDaoImpl;
import fr.diginamic.model.But;
import fr.diginamic.model.Buteur;
import fr.diginamic.model.Match;
import fr.diginamic.utils.CheckUtils;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Classe de service pour gérer l'insertion conditionnée d'un But
 */
public class ButService {

    private final ButDaoImpl butDao;
    private final ErreurImportDaoImpl erreurDao;

    /**
     * Initialise le service avec un EntityManager
     * @param em EntityManager à utiliser
     */
    public ButService(EntityManager em) {
        this.butDao = new ButDaoImpl(em);
        this.erreurDao = new ErreurImportDaoImpl(em);
    }

    /**
     * Insère un match s'il n'existe pas déjà en base.
     * En cas de doublon, une erreur est loguée.
     * @param minute minute à laquelle le but est marqué
     * @param csc indique si le but est marqué contre son camp
     * @param penalty indique si le but est marqué sur penalty
     * @param match indique à quel match le but est rattaché
     * @param buteur indique le buteur ayant marqué
     * @param ligne indique à quelle ligne du csv l'erreur éventuelle est déclenchée
     * @param fichier indique quel fichier est à l'origine de l'erreur éventuelle
     */
    public void enregistrerButSiNouveau(
            int minute, boolean csc, boolean penalty, Match match, Buteur buteur,
            String ligne, String fichier
    ) {
        try {
            if (CheckUtils.isNotNull(match) && CheckUtils.isNotNull(buteur) && CheckUtils.isNotNegative(minute)) {
                List<But> resultats = butDao.findByMatchButeurAndMinute(
                        match.getId(), buteur.getId(), minute
                );

                if (resultats.isEmpty()) {
                    But but = new But(minute, csc, penalty, match, buteur);
                    butDao.insert(but);
                } else if (resultats.size() > 1) {
                    erreurDao.logErreur(fichier, ligne,
                            "Doublon : plusieurs buts trouvés pour ce minutage, match et buteur", "But");
                }
            } else {
                erreurDao.logErreur(fichier, ligne,
                        "Paramètres invalides : match ou buteur null, ou minute négatif", "But");
            }
        } catch (Exception e) {
            erreurDao.logErreur(fichier, ligne, e.getMessage(), "But");
        }
    }
}
