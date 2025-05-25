package travel.persistance.interfaces;

import travel.persistance.IRepository;
import travel.model.Agency;

import java.util.Optional;

public interface IAgencyRepository extends IRepository<Long, Agency> {
    Optional<Agency> findByEmail(String email);
}
