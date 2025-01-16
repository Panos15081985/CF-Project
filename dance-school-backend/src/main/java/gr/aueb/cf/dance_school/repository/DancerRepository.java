package gr.aueb.cf.dance_school.repository;

import gr.aueb.cf.dance_school.models.Dancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface DancerRepository extends JpaRepository<Dancer,Long>, JpaSpecificationExecutor<Dancer> {
    boolean existsByFirstnameAndLastnameAndDateOfBirth(String firstname, String lastname, LocalDate dateOfBirth);
    Optional<Dancer> findByUuid (String uuid);

}
