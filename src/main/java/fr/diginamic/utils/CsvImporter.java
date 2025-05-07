package fr.diginamic.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * Classe utilitaire pour importer et traiter des fichiers CSV placés dans le dossier resources.
 */
public class CsvImporter {

    /**
     * Parcourt un fichier CSV ligne par ligne et applique une action à chaque ligne.
     * @param resourceName nom du fichier dans resources
     * @param lineConsumer action à exécuter pour chaque ligne lue
     */
    public static void lireFichier(String resourceName, Consumer<String> lineConsumer) {
        InputStream is = CsvImporter.class.getClassLoader().getResourceAsStream(resourceName);

        if (is == null) {
            System.err.println("Fichier introuvable dans les ressources : " + resourceName);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String ligne;
            boolean firstLine = true;

            while ((ligne = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                lineConsumer.accept(ligne);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture du fichier " + resourceName + " : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
