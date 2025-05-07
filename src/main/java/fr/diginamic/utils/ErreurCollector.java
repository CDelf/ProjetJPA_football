package fr.diginamic.utils;

import fr.diginamic.model.ErreurImport;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilitaire centralisant les erreurs rencontrées pendant l'import.
 * À utiliser à la place du DAO pour éviter les conflits de transaction.
 */
public final class ErreurCollector {

    private final List<ErreurImport> erreurs = new ArrayList<>();

    /**
     * Ajoute une erreur complète
     * @param fichier nom du fichier en erreur
     * @param ligne ligne source
     * @param message message explicatif
     * @param traitement nom de l'étape ou de l'entité concernée
     */
    public void log(String fichier, String ligne, String message, String traitement) {
        erreurs.add(new ErreurImport(fichier, ligne, message, traitement));
    }

    /**
     * Retourne toutes les erreurs accumulées
     * @return liste d'erreurs
     */
    public List<ErreurImport> getErreurs() {
        return erreurs;
    }

    /**
     * Nombre total d'erreurs collectées
     * @return int
     */
    public int count() {
        return erreurs.size();
    }

    /**
     * Affiche les erreurs sur la console (optionnel)
     */
    public void printAll() {
        erreurs.forEach(System.out::println);
    }
}
