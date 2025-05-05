package fr.diginamic.dao;

import fr.diginamic.model.Score;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Implémentation de l'entité Score via l'interface GenericDao
 */
public class ScoreDaoImpl implements GenericDao<Score> {

    private EntityManager em;

    /**
     * Constructeur de la classe, initialisée avec un entity manager
     * @param em instance d'EntityManager
     */
    public ScoreDaoImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Méthode pour insérer un score et le persister en base de données
     * @param score désigne l'équipe à insérer
     */
    @Override
    public void insert(Score score) {
        em.persist(score);
    }

    /**
     * Méthode permettant de récupérer un score en le recherchant avec son id
     * @param id désigne l'id unique du score
     * @return l'instance possédant l'id recherché
     */
    @Override
    public Score findById(int id) {
        return em.find(Score.class, id);
    }

    /**
     * Méthode permettant de récupérer la liste des scores selon plusieurs attributs
     * @param scoreHote désigne le score de l'équipe hôte
     * @param scoreInvite désigne le score de l'équipe invitée
     * @param matchId désigne le match source du score
     * @return la liste des scores respectant ces critères
     */
    public List<Score> findByMatchAndScores(int matchId, int scoreHote, int scoreInvite) {
        return em.createQuery(
                "SELECT s FROM Score s WHERE s.match.id = :matchId AND s.scoreHote = :scoreHote AND s.scoreInvite = :scoreInvite", Score.class)
                .setParameter("matchId", matchId)
                .setParameter("scoreHote", scoreHote)
                .setParameter("scoreInvite", scoreInvite)
                .getResultList();
    }

    /**
     * Méthode retournant la liste de tous les scores en base de données
     * @return liste de tous les scores en base de données
     */
    @Override
    public List<Score> findAll() {
        return em.createQuery("SELECT s FROM Score s", Score.class).getResultList();
    }

    /**
     * Méthode permettant de supprimer un score via son id unique
     * @param id id utilisé pour identifier l'instance de manière unique
     */
    @Override
    public void delete(int id) {
        Score s = em.find(Score.class, id);
        if(s != null) {
            em.remove(s);
        }
    }
}
