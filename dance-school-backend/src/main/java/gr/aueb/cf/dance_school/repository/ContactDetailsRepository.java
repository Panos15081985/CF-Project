package gr.aueb.cf.dance_school.repository;

import gr.aueb.cf.dance_school.models.ContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ContactDetailsRepository extends JpaRepository<ContactDetails,Long>, JpaSpecificationExecutor<ContactDetails> {
}
