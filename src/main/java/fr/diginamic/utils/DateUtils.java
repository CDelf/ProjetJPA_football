package fr.diginamic.utils;

import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaire pour la gestion des formats de date dans les fichiers CSV.
 */
public final class DateUtils {

    /** Format de date utilisé dans les fichiers d'import CSV */
    public static final DateTimeFormatter CSV_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** Constructeur privé pour empêcher l’instanciation */
    private DateUtils() {
        throw new AssertionError("Classe utilitaire, ne doit pas être instanciée.");
    }
}
