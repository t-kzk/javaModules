package org.kzk.repository.imp;

import org.kzk.jpa.EmfProvider;
import org.kzk.jpa.JpaService;
import org.kzk.model.Post;
import org.kzk.repository.PostRepository;

import java.util.List;
import java.util.Optional;

public class PostRepositoryHibernateImpl extends JpaService implements PostRepository {
    public PostRepositoryHibernateImpl() {
        super(EmfProvider.INSTANCE.getEmf().createEntityManager());
    }

    @Override
    public List<Post> findAllByWriterId(Integer writerId) {
        return em.createQuery("from Post where writer=:writerId", Post.class)
                .setParameter("writerId", writerId).getResultList();
    }

    @Override
    public Post save(Post entity) {
        return merge(entity);
    }

    @Override
    public Post update(Post entity) {
        return merge(entity);
    }

    @Override
    public boolean delete(Post entity) {
        remove(entity);
        return true;
    }

    @Override
    public Optional<Post> findById(Integer integer) {
       /* Post post = em.createQuery("from Post where id = :id", Post.class)
                .setParameter("id", integer).getSingleResult();*/
        Post post = em.find(Post.class, integer);
        return Optional.of(post);
    }

    @Override
    public List<Post> findAll() {
        return em.createQuery("from Post", Post.class).getResultList();
    }
}
