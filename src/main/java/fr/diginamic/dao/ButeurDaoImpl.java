package fr.diginamic.dao;

import fr.diginamic.model.Buteur;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Implémentation de l'entité Buteur via l'interface GenericDao
 */
public class ButeurDaoImpl implements GenericDao<Buteur> {

    private EntityManager em;

    /**
     * Constructeur de la classe, initialisée avec un entity manager
     * @param em instance d'EntityManager
     */
    public ButeurDaoImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Méthode pour insérer un buteur et le persister en base de données
     * @param buteur désigne le but à insérer
     */
    @Override
    public void insert(Buteur buteur) {
        em.persist(buteur);
    }

    /**
     * Méthode permettant de récupérer un buteur en le recherchant avec son id
     * @param id désigne l'id unique du buteur
     * @return l'instance possédant l'id recherché
     */
    @Override
    public Buteur findById(int id) {
        return em.find(Buteur.class, id);
    }

    /**
     * Méthode permettant de récupérer la liste des buteurs selon plusieurs attributs
     * @param nom désigne le nom du buteur
     * @param equipeId désigne l'id de son équipe
     * @return la liste des buteurs respectant ces critères
     */
    public List<Buteur> findByNomEquipeAndNbButs(String nom, int equipeId) {
        return em.createQuery("SELECT b FROM Buteur b WHERE b.nom = :nom AND b.equipe.id = :equipeId", Buteur.class)
                .setParameter("nom", nom)
                .setParameter("equipeId", equipeId)
                .getResultList();
    }

    /**
     * Méthode retournant la liste de tous les buteurs en base de données
     * @return liste de tous les buteurs en base de données
     */
    @Override
    public List<Buteur> findAll() {
        return em.createQuery("SELECT b FROM Buteur b", Buteur.class).getResultList();
    }

    /**
     * Méthode permettant de supprimer un buteur via son id unique
     * @param id id utilisé pour identifier l'instance de manière unique
     */
    @Override
    public void delete(int id) {
        Buteur b = em.find(Buteur.class, id);
        if(b != null) {
            em.remove(b);
        }
    }
}
