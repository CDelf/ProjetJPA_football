package fr.diginamic.dao;

import fr.diginamic.model.But;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Implémentation de l'entité But via l'interface GenericDao
 */
public class ButDaoImpl implements GenericDao<But> {

    private EntityManager em;

    /**
     * Constructeur de la classe, initialisée avec un entity manager
     * @param em instance d'EntityManager
     */
    public ButDaoImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Méthode pour insérer un but et le persister en base de données
     * @param but désigne le but à insérer
     */
    @Override
    public void insert(But but) {
        em.persist(but);
    }

    /**
     * Méthode permettant de récupérer un but en le recherchant avec son id
     * @param id désigne l'id unique du but
     * @return l'instance possédant l'id recherché
     */
    @Override
    public But findById(int id) {
        return em.find(But.class, id);
    }

    /**
     * Méthode permettant de récupérer la liste des buts selon plusieurs attributs
     * @param matchId désigne l'id du match durant lequel a eu lieu le but
     * @param buteurId désigne l'id du buteur ayant marqué le but
     * @param minute désigne la minute du match à laquelle le but a été marqué
     * @return la liste des buts ayant l'ensemble de ces critères
     */
    public List<But> findByMatchButeurAndMinute(int matchId, int buteurId, int minute){
        return em.createQuery(
                "SELECT b FROM But b WHERE b.match.id = :matchId AND b.buteur.id = :buteurId AND b.minute = :min", But.class)
                .setParameter("matchId", matchId)
                .setParameter("buteurId", buteurId)
                .setParameter("min", minute)
                .getResultList();
    }

    /**
     * Méthode retournant la liste de tous les buts en base de données
     * @return liste de tous les buts en base de données
     */
    @Override
    public List<But> findAll() {
        return em.createQuery("SELECT b FROM But b", But.class).getResultList();
    }

    /**
     * Méthode permettant de supprimer un but via son id unique
     * @param id id utilisé pour identifier l'instance de manière unique
     */
    @Override
    public void delete(int id) {
        But b = em.find(But.class, id);
        if(b != null) {
            em.remove(b);
        }
    }
}
