package org.kzk.repository.imp;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.kzk.env.jpa.EmfProvider;
import org.kzk.env.jpa.JpaService;
import org.kzk.model.Writer;
import org.kzk.repository.WriterRepository;

import java.util.List;
import java.util.Optional;

public class WriterRepositoryHibernateImpl extends JpaService implements WriterRepository {
    public WriterRepositoryHibernateImpl() {
        super(EmfProvider.INSTANCE.getEmf().createEntityManager());
    }

    public void createWriter(Writer writer) {
        super.persist(writer);
    }

    public void deleteWriter(int id) {
        tx(em -> em.createQuery("delete from Writer u where u.id = :id")
                .setParameter("id", id).executeUpdate());
    }

    @Override
    public Writer save(Writer entity) {
        persist(entity);
        return entity;
    }

    @Override
    public Writer update(Writer entity) {
       return merge(entity);
    }

    @Override
    public boolean delete(Writer entity) {
        remove(entity);
        return true;
    }

    @Override
    public Optional<Writer> findById(Integer integer) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Writer> query = cb.createQuery(Writer.class);
        Root<Writer> root = query.from(Writer.class);
        query.select(root);
        query.where(
                cb.equal(root.get("id"), integer)
        );
        Writer result = em.createQuery(query).getSingleResult();

        return Optional.of(result);
    }

    @Override
    public List<Writer> findAll() {
        return em.createQuery("from Writer", Writer.class).getResultList();
    }
}
