package gr.aueb.cf.dance_school.repository;

import gr.aueb.cf.dance_school.models.Coach;
import gr.aueb.cf.dance_school.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long>, JpaSpecificationExecutor<Course> {
    Optional<Course> findCourseById(Long id);
    List<Course> findCoursesByCoach(Coach coach);
}
