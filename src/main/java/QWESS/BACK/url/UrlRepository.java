package QWESS.BACK.url;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UrlRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Url url) {
        em.persist(url);
    }

    public void delete(Url url) {
        em.remove(url);
    }

    public Optional<Boolean> findByUrl(String url) {
        try {
            TypedQuery<Boolean> query = em.createQuery("select u.tf from Url u where u.url =: url", Boolean.class);
            query.setParameter("url", url);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
