package fr.diginamic;

import fr.diginamic.imports.GoalsScorersCsvImporter;
import fr.diginamic.imports.ResultCsvImporter;
import fr.diginamic.imports.ShootoutsCsvImporter;
import fr.diginamic.utils.ErreurCollector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    /**
     * Classe principale qui gère l'import des fichiers csv issus de resources dans l'ordre
     * @param args contenu de la classe main
     */
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-football");
        EntityManager em = emf.createEntityManager();
        ErreurCollector collector = new ErreurCollector();

        long startTime = System.currentTimeMillis();

        try {
            em.getTransaction().begin();

            System.out.println("Import : results.csv");
            new ResultCsvImporter(em, collector).importer("results.csv");

            System.out.println("Import : goalscorers.csv");
            new GoalsScorersCsvImporter(em, collector).importer("goalscorers.csv");

            System.out.println("Import : shootouts.csv");
            new ShootoutsCsvImporter(em, collector).importer("shootouts.csv");

            collector.getErreurs().forEach(em::persist);
            em.getTransaction().commit();

            System.out.println("Import terminé avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur pendant l'import : rollback effectué.");
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Durée totale : " + (endTime - startTime) + " ms");
        System.out.println("Nombre d’erreurs collectées : " + collector.getErreurs().size());
    }
}
