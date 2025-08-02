package org.kzk.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {

    T save(T entity);

    T update(T entity);

    boolean delete(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();
}
