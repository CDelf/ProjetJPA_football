package fr.diginamic.dao;

import fr.diginamic.model.Equipe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Implémentation de l'entité Equipe via l'interface GenericDao
 */
public class EquipeDaoImpl implements GenericDao<Equipe> {

    private EntityManager em;

    /**
     * Constructeur de la classe, initialisée avec un entity manager
     * @param em instance d'EntityManager
     */
    public EquipeDaoImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Méthode pour insérer une équipe et la persister en base de données
     * @param equipe désigne l'équipe à insérer
     */
    @Override
    public void insert(Equipe equipe) {
        em.persist(equipe);
    }

    /**
     * Méthode permettant de récupérer une équipe en la recherchant avec son id
     * @param id désigne l'id unique de l'équipe
     * @return l'instance possédant l'id recherché
     */
    @Override
    public Equipe findById(int id) {
        return em.find(Equipe.class, id);
    }

    /**
     * Méthode retournant une liste d'équipes selon plusieurs attributs
     * @param nom désigne le nom de l'équipe
     * @param nbMatchsHote désigne le nombre de matchs joués à domicile
     * @param nbMatchsInv désigne le nombre de matchs joués à l'extérieur
     * @return la liste des matchs ayant ces critères en commun
     */
    public List<Equipe> findByNameAndNbMatchs(String nom, int nbMatchsHote, int nbMatchsInv) {
        return em.createQuery(
                "SELECT e FROM Equipe e WHERE e.nom = :nom AND SIZE(e.matchsHote) = :nbMatchsHote AND SIZE(e.matchsInvitee) = :nbMatchsInv", Equipe.class)
                .setParameter("nom", nom)
                .setParameter("nbMatchsHote",nbMatchsHote)
                .setParameter("nbMatchsInv", nbMatchsInv)
                .getResultList();
    }

    /**
     * Méthode retournant la liste de toutes les équipes en base de données
     * @return liste de toutes les équipes en base de données
     */
    @Override
    public List<Equipe> findAll() {
        return em.createQuery("SELECT e FROM Equipe e", Equipe.class).getResultList();
    }

    /**
     * Méthode permettant de supprimer une équipe via son id unique
     * @param id id utilisé pour identifier l'instance de manière unique
     */
    @Override
    public void delete(int id) {
        Equipe e = em.find(Equipe.class, id);
        if (e != null) {
            em.remove(e);
        }
    }
}
