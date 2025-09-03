package org.kzk.repository.imp;

import jakarta.persistence.criteria.*;
import org.kzk.jpa.EmfProvider;
import org.kzk.jpa.JpaService;
import org.kzk.model.Post;
import org.kzk.model.Writer;
import org.kzk.repository.WriterRepository;

import java.util.List;
import java.util.Optional;

public class HibernateWriterRepositoryImpl extends JpaService implements WriterRepository {
    public HibernateWriterRepositoryImpl() {
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
        CriteriaQuery<Writer> criteria = cb.createQuery(Writer.class);
        Root<Writer> rootWriterTable = criteria.from(Writer.class);
        Fetch<Writer, Post> postsFetch = rootWriterTable.fetch("posts", JoinType.LEFT);
        postsFetch.fetch("labels", JoinType.LEFT);
        criteria.select(rootWriterTable);
        criteria.where(
                cb.equal(rootWriterTable.get("id"), integer)
        );
        Writer result = em.createQuery(criteria).getSingleResult();

        return Optional.of(result);
    }

    @Override
    public List<Writer> findAll() {
        return em.createQuery("""
                        select w from Writer w left join fetch w.posts p left join fetch p.labels l
                        """, Writer.class)
                .getResultList();
    }
}
