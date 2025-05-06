package fr.diginamic.utils;

import java.time.LocalDate;

public class CheckUtils {

    /**
     * Vérifie qu'une chaîne n'est ni null ni vide (après trim).
     * @param value La chaîne à vérifier
     * @return true si la chaîne est valide, false sinon
     */
    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Vérifie qu'un entier est strictement positif.
     * @param value désigne l'entier à vérifier
     * @return true si positif, sinon false
     */
    public static boolean isNotNegative(int value) {
        return value >= 0;
    }

    /**
     * Vérifie qu'un objet n'est pas null.
     * @param obj désigne l'objet à tester
     * @return true s'il n'est pas null, sinon false
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * Vérifie la validité d'une date : non nulle et dans le passé
     * @param date désigne la date à vérifier
     * @return true si valide, sinon false
     */
    public static boolean isValidDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

}
