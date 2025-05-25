package travel.persistance.hibernate;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import travel.model.Agency;
import travel.model.Flight;
import travel.persistance.interfaces.IFlightRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class FlightDBRepoHibernate implements IFlightRepository {

    @Override
    public Optional<Flight> findOne(Long aLong) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createQuery("FROM Flight WHERE id=:idF", Flight.class)
                    .setParameter("idF", aLong)
                    .getSingleResult());
        }catch(NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Iterable<Flight> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Flight ", Flight.class).getResultList();
        }catch(NoResultException e) {
            return new ArrayList<Flight>();
        }
    }

    @Override
    public Optional<Flight> save(Flight entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(entity));
        return Optional.of(entity);
    }

    @Override
    public Optional<Flight> delete(Long id) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Flight flight = session.createQuery("FROM Flight WHERE id=?1", Flight.class)
                    .setParameter(1, id).uniqueResult();
            System.out.println("Found the Flight (delete repo hibernate): " + flight);
            if (flight != null) {
                session.remove(flight);
                session.flush();
            }
        });
        return Optional.empty();
    }

    @Override
    public Optional<Flight> update(Flight entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            if (session.find(Flight.class, entity.getId()) != null) {
                System.out.println("Found the Flight (update repo hibernate): " + entity);
                session.merge(entity);
                session.flush();
            }
        });
        return Optional.of(entity);
    }

    @Override
    public Iterable<Flight> findAllFlightsDestinationDateAboveZero(String destination, LocalDate date) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            return session.createQuery("FROM Flight WHERE destination = :destinationF AND timeOfDeparture >= :start AND timeOfDeparture < :end", Flight.class)
                    .setParameter("destinationF", destination)
                    .setParameter("start", startOfDay)
                    .setParameter("end", endOfDay)
                    .getResultList();
        }catch(NoResultException e) {
            return new ArrayList<Flight>();
        }
    }
}
