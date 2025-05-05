package fr.diginamic.dao;

import java.util.List;

/**
 * Interface DAO à implémenter dans toutes les implémentations d'entités
 * @param <T> Entité à implémenter
 */
public interface GenericDao<T> {

    void insert(T entity);

    T findById(int id);

    List<T> findAll();

    void delete(int id);
}
