package org.kzk.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class JpaService {

    protected final EntityManager em;


    protected JpaService(EntityManager em) {
        this.em = em;
    }

    protected <T> void persist(T entity) {
        tx(em -> em.persist(entity));
    }
    protected <T> void remove(T entity) {
        tx(em -> em.remove(entity));
    }
    protected <T> T merge(T entity) {
       return txWithResult(em -> em.merge(entity));
    }

    protected  <T> T txWithResult(Function<EntityManager, T> function) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            T result = function.apply(em);
            transaction.commit();

            return result;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    protected void tx(Consumer<EntityManager> consumer) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            consumer.accept(em);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
