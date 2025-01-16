package gr.aueb.cf.dance_school.core.specification;

import gr.aueb.cf.dance_school.models.Coach;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;


public class CoachSpecification {

    public CoachSpecification(){}

    public static Specification<Coach> coachLike(String value){
        return ((root, query, criteriaBuilder) -> {

            if(value == null || value.trim().isEmpty()){
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            return criteriaBuilder.like(root.get("lastname"), "%" + value + "%");
        });
    }

    public static Specification<Coach> findByUuid(String uuid){
        return ((root, query, criteriaBuilder) -> {

            if(uuid == null || uuid.trim().isEmpty()){
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("uuid"),uuid);

        });
    }
}
