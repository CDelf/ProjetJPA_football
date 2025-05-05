package fr.diginamic.dao;

import fr.diginamic.model.Equipe;
import fr.diginamic.model.Match;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

/**
 * Implémentation de l'entité Match via l'interface GenericDao
 */
public class MatchDaoImpl implements GenericDao<Match> {

    private EntityManager em;

    /**
     * Constructeur de la classe, initialisée avec un entity manager
     * @param em instance d'EntityManager
     */
    public MatchDaoImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Méthode pour insérer un match et le persister en base de données
     * @param match désigne l'équipe à insérer
     */
    @Override
    public void insert(Match match) {
        em.persist(match);
    }

    /**
     * Méthode permettant de récupérer un match en le recherchant avec son id
     * @param id désigne l'id unique du match
     * @return l'instance possédant l'id recherché
     */
    @Override
    public Match findById(int id) {
        return em.find(Match.class, id);
    }

    /**
     * Méthode permettant de récupérer la liste des matchs selon plusieurs attributs
     * @param date désigne la date à laquelle se déroule le match
     * @param hoteId désigne l'id de l'équipe hôte
     * @param inviteeId désigne l'id de l'équipe invitée
     * @return la liste des matchs ayant ces mêmes attributs
     */
    public List<Match> findByDateAndEquipes(LocalDate date, int hoteId, int inviteeId){
        return em.createQuery(
                "SELECT m FROM Match m WHERE m.date=:date AND m.equipeHote.id = :hoteId AND m.equipeInvitee.id = :inviteeId", Match.class)
                .setParameter("date", date)
                .setParameter("hoteId", hoteId)
                .setParameter("inviteeId", inviteeId)
                .getResultList();
    }

    /**
     * Méthode retournant la liste de tous les matchs en base de données
     * @return liste de tous les matchs en base de données
     */
    @Override
    public List<Match> findAll() {
        return em.createQuery("SELECT m FROM Match m", Match.class).getResultList();
    }

    /**
     * Méthode permettant de supprimer un match via son id unique
     * @param id id utilisé pour identifier l'instance de manière unique
     */
    @Override
    public void delete(int id) {
        Match m = em.find(Match.class, id);
        if (m != null) {
            em.remove(m);
        }
    }
}
