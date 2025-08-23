package org.kzk.repository.imp;

import org.kzk.jpa.EmfProvider;
import org.kzk.jpa.JpaService;
import org.kzk.model.Label;
import org.kzk.repository.LabelRepository;

import java.util.List;
import java.util.Optional;

public class LabelRepositoryHibernateImpl extends JpaService implements LabelRepository {
    public LabelRepositoryHibernateImpl() {
        super(EmfProvider.INSTANCE.getEmf().createEntityManager());
    }

    @Override
    public Label save(Label entity) {
        persist(entity);
        return entity;
    }

    @Override
    public Label update(Label entity) {
        return merge(entity);
    }

    @Override
    public boolean delete(Label entity) {
        remove(entity);
        return false;
    }

    @Override
    public Optional<Label> findById(Integer integer) {
        Label label = em.createQuery("from Label where id = :id", Label.class)
                .setParameter("id", integer).getSingleResult();
        return Optional.of(label);
    }

    @Override
    public List<Label> findAll() {
        return em.createQuery("from Label", Label.class).getResultList();
    }
}
