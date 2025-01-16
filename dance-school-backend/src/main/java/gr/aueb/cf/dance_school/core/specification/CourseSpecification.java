package gr.aueb.cf.dance_school.core.specification;

import gr.aueb.cf.dance_school.models.Coach;
import gr.aueb.cf.dance_school.models.Course;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecification {

    public CourseSpecification(){}

    public static Specification<Course> nameLike(String value) {
        return ((root, query, criteriaBuilder) -> {

            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("name"), "%" + value + "%");
        });
    }

    public static Specification<Course> dayLike(String value){
        return ((root, query, criteriaBuilder) -> {

            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("day"), "%" + value + "%");
        });
    }

    /**
     * Δημιουργεί μια προδιαγραφή που φιλτράρει μαθήματα με βάση το ID του προπονητή.
     * Επιστρέφει όλα τα μαθήματα αν το `coachId` είναι `null`.
     */
    public static Specification<Course> byCoachId(Long coachId) {
        return (root, query, criteriaBuilder) -> {

            if (coachId == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Course, Coach> coachJoin = root.join("coach");

            return criteriaBuilder.equal(root.get("coach").get("id"), coachId);
        };
    }
}
