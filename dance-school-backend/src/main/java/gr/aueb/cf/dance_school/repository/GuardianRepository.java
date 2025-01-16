package gr.aueb.cf.dance_school.repository;

import gr.aueb.cf.dance_school.models.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface GuardianRepository extends JpaRepository<Guardian,Long>, JpaSpecificationExecutor<Guardian> {
    Optional<Guardian> findByFirstnameAndLastname(String firstname, String lastname);
}
