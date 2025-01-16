package gr.aueb.cf.dance_school.core.specification;

import gr.aueb.cf.dance_school.models.Coach;
import gr.aueb.cf.dance_school.models.Course;
import gr.aueb.cf.dance_school.models.Dancer;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DancerSpecification {

    public DancerSpecification(){}

    public static Specification<Dancer> lastnameLike(String value){
        return ((root, query, criteriaBuilder) -> {

            if(value == null || value.trim().isEmpty()){
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("lastname"), "%" + value + "%");
        });
    }

    public static Specification<Dancer> byDateOfBirth(LocalDate dateOfBirth){
        return ((root, query, criteriaBuilder) -> {

            if(dateOfBirth == null){
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            return criteriaBuilder.equal(root.get("dateOfBirth"),dateOfBirth);
        });
    }

    public static Specification<Dancer> byIsUnder18(Boolean isUnder18){
        return ((root, query, criteriaBuilder) -> {

            if(isUnder18 == null){
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            return criteriaBuilder.equal(root.get("isUnder18"),isUnder18);
        });
    }

    /**
     * Δημιουργεί μια προδιαγραφή για το συμβόλαιο του χορευτή, με βάση το τέλος του συμβολαίου.
     * Επιστρέφει όλους τους χορευτές αν και οι δύο παράμετροι είναι `null`.
     **/
    public static Specification<Dancer> byPeriodContractEnd(LocalDate contractEndFrom, LocalDate contractEndUntil) {
        return (root, query, criteriaBuilder) -> {
            // Αν και οι δύο παράμετροι είναι null, επιστρέφουμε όλα τα αποτελέσματα
            if (contractEndFrom == null && contractEndUntil == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            // Αν το contractEndFrom είναι null, φέρνουμε όσα έχουν contract_end <= contractEndUntil
            if (contractEndFrom == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("contractEnd"), contractEndUntil);
            }

            // Αν το contractEndUntil είναι null, φέρνουμε όσα έχουν contract_end >= contractEndFrom
            if (contractEndUntil == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("contractEnd"), contractEndFrom);
            }

            // Αν και οι δύο παράμετροι έχουν τιμές, εφαρμόζουμε το εύρος
            return criteriaBuilder.between(root.get("contractEnd"), contractEndFrom, contractEndUntil);
        };
    }

    public static Specification<Dancer> byCourseId(Long courseId) {
        return (root, query, criteriaBuilder) -> {
            if (courseId == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            // Join με την οντότητα Course
            Join<Dancer, Course> courseJoin = root.join("courses");

            // Επιστρέφει τους χορευτές που ανήκουν στο μάθημα με courseId
            return criteriaBuilder.equal(courseJoin.get("id"), courseId);
        };
    }

    /**
     * Δημιουργεί μια προδιαγραφή που φιλτράρει χορευτές με βάση το UUID του προπονητή.
     * Επιστρέφει όλους τους χορευτές αν το `coachUuid` είναι `null` ή κενό.
     **/
    public static Specification<Dancer> byCoachUuid(String coachUuid) {
        return (root, query, criteriaBuilder) -> {
            // Αν το coachUuid είναι null ή κενό, επέστρεψε όλους τους χορευτές
            if (coachUuid == null || coachUuid.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            // Δημιουργία Join μεταξύ Dancer, Course και Coach
            Join<Dancer, Course> courseJoin = root.join("courses");
            Join<Course, Coach> coachJoin = courseJoin.join("coach");

            // Επιστροφή Predicate για φιλτράρισμα βάσει coachUuid
            return criteriaBuilder.equal(coachJoin.get("uuid"), coachUuid);
        };
    }
}
