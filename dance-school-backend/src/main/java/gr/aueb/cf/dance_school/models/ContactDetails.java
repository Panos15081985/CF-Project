package gr.aueb.cf.dance_school.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "contact_details")
public class ContactDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;
    private String road;
    private String road_number;
    private String postalCode;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;
}
