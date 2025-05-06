package fr.diginamic.dao;

import fr.diginamic.model.ErreurImport;
import jakarta.persistence.EntityManager;

/**
 * Implémentation de l'entité permettant de représenter les erreurs d'import
 */
public class ErreurImportDaoImpl {

    private EntityManager em;

    /**
     * Constructeur de la classe, initialisée avec un entity manager
     * @param em instance d'EntityManager
     */
    public ErreurImportDaoImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Enregistre une erreur en base
     * @param fichier désigne le fichier concerné par l'erreur d'import
     * @param ligne désigne la ligne concernée par l'erreur
     * @param message désigne le message d'erreur
     * @param traitement désigne le traitement à l'origine de l'erreur pour identifier l'entité concernée
     */
    public void logErreur(String fichier, String ligne, String message, String traitement) {
        em.persist(new ErreurImport(fichier, ligne, message, traitement));
    }
}
