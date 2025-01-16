package gr.aueb.cf.dance_school.repository;

import gr.aueb.cf.dance_school.core.enums.RoleType;
import gr.aueb.cf.dance_school.models.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach,Long>, JpaSpecificationExecutor<Coach> {
    Optional<Coach> findByRole (RoleType role);
    Optional<Coach> findByUsername(String username);
    Optional<Coach> findByUuid(String uuid);
    Optional<Coach> findById(Long id);


}
