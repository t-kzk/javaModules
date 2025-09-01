package org.kzk.repository.imp;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.kzk.jpa.EmfProvider;
import org.kzk.jpa.JpaService;
import org.kzk.model.Label;
import org.kzk.repository.LabelRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class HibernateLabelRepositoryImpl extends JpaService implements LabelRepository {
    public HibernateLabelRepositoryImpl() {
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

    @Override
    public Set<Label> findLabelsByIds(Set<Integer> labelIds) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Label> criteria = cb.createQuery(Label.class);
        Root<Label> root = criteria.from(Label.class);
        criteria.select(root)
                .where(
                       root.get("id").in(labelIds)
                );
        return em.createQuery(criteria).getResultStream().collect(Collectors.toSet());
    }
}
