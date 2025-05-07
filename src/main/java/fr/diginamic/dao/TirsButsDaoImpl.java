package fr.diginamic.dao;

import fr.diginamic.model.TirsButs;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Implémentation de l'entité TirsButs via l'interface GenericDao
 */
public class TirsButsDaoImpl implements GenericDao<TirsButs> {

    private EntityManager em;

    /**
     * Constructeur de la classe, initialisée avec un entity manager
     * @param em instance d'EntityManager
     */
    public TirsButsDaoImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * Méthode pour insérer une séance de tirs aux buts et la persister en base de données
     * @param tirsButs désigne la séance de tirs aux buts
     */
    @Override
    public void insert(TirsButs tirsButs) {
        em.persist(tirsButs);
    }

    /**
     * Méthode permettant de récupérer une séance de tirs aux buts en la recherchant avec son id
     * @param id désigne l'id unique de la séance de tirs aux buts
     * @return l'instance possédant l'id recherché
     */
    @Override
    public TirsButs findById(int id) {
        return em.find(TirsButs.class, id);
    }

    /**
     * Méthode permettant de récupérer la liste des tirs aux buts selon plusieurs attributs
     * @param matchId désigne l'id du match à l'origine des tirs aux buts
     * @param vainqueurId désigne l'id du vainqueur
     * @return la liste des tirs aux buts répondant à ces critères
     */
    public List<TirsButs> findByMatchAndVainqueur(int matchId, int vainqueurId) {
        return em.createQuery(
                "SELECT t FROM TirsButs t WHERE t.match.id = :matchId AND t.vainqueur.id = :vainqueurId", TirsButs.class)
                .setParameter("matchId", matchId)
                .setParameter("vainqueurId", vainqueurId)
                .getResultList();
    }

    /**
     * Méthode retournant la liste de tous les tirs aux buts en base de données
     * @return liste de tous les tirs aux buts en base de données
     */
    @Override
    public List<TirsButs> findAll() {
        return em.createQuery("SELECT t FROM TirsButs t", TirsButs.class).getResultList();
    }

    /**
     * Méthode permettant de supprimer une séance de tirs aux buts via son id unique
     * @param id id utilisé pour identifier l'instance de manière unique
     */
    @Override
    public void delete(int id) {
        TirsButs t = em.find(TirsButs.class, id);
        if(t != null) {
            em.remove(t);
        }
    }
}
