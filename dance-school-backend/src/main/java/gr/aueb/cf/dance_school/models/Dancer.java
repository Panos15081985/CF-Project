package gr.aueb.cf.dance_school.models;

import gr.aueb.cf.dance_school.core.enums.GenderType;
import gr.aueb.cf.dance_school.core.exceptions.ConflictException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "dancers")
public class Dancer extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(unique = true)
    private String uuid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_details_id")
    private ContactDetails contactDetails;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @Column(nullable = false, name= "is_under18")
    private Boolean isUnder18;

    private LocalDate contractEnd;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToMany
    @JoinTable(
            name = "dancers_courses",
            joinColumns = @JoinColumn(name = "dancer_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();


    @ManyToOne(optional = true, cascade = CascadeType.ALL)//Ο χορευτής (Dancer) μπορεί να μην έχει κηδεμόνα (Guardian).
    @JoinColumn(name = "guardian_id", referencedColumnName = "id")
    private Guardian guardian;

    @PrePersist
    public void validateDancer() throws ConflictException {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }

        if (dateOfBirth != null) {
            LocalDate now = LocalDate.now();
            int age = Period.between(dateOfBirth, now).getYears();
            this.isUnder18 = age < 18;

            if (this.isUnder18) {
                if (guardian == null) {
                    throw new ConflictException("Dancer","Guardian is required for dancers under 18");
                }
            }
        }
    }
}
