package travel.persistance.hibernate;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import travel.model.Agency;
import travel.persistance.interfaces.IAgencyRepository;

import java.util.Optional;

public class AgencyDBRepoHibernate implements IAgencyRepository {
    @Override
    public Optional<Agency> findByEmail(String email) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createQuery("FROM Agency WHERE email=:emailA", Agency.class)
                    .setParameter("emailA", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Agency> findOne(Long aLong) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createQuery("FROM Agency WHERE id=:idA", Agency.class)
                    .setParameter("idA", aLong)
                    .getSingleResult());
        }
    }

    @Override
    public Iterable<Agency> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Agency ", Agency.class).getResultList();
        }
    }

    @Override
    public Optional<Agency> save(Agency entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(entity));
        return Optional.of(entity);
    }

    @Override
    public Optional<Agency> delete(Long id) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Agency agency = session.createQuery("FROM Agency WHERE id=?1", Agency.class)
                    .setParameter(1, id).uniqueResult();
            System.out.println("Found the Agency (delete repo hibernate): " + agency);
            if (agency != null) {
                session.remove(agency);
                session.flush();
            }
        });
        return Optional.empty();
    }

    @Override
    public Optional<Agency> update(Agency entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            if (session.find(Agency.class, entity.getId()) != null) {
                System.out.println("Found the Agency (update repo hibernate): " + entity);
                session.merge(entity);
                session.flush();
            }
        });
        return Optional.of(entity);
    }
}
