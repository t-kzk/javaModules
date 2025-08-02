package org.kzk.repository;

public interface CrudRepository<T, ID> extends ReadRepository<T, ID> {
    ID save(T entity);

/*    void update(T entity);

    void delete(T entity);*/
}
