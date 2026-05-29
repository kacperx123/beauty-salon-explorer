package beauty_salon_explorer.salonExplorer.salon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SalonRepository extends JpaRepository<Salon, Long>, JpaSpecificationExecutor<Salon> {

    Optional<Salon> findByExternalId(String externalId);
}