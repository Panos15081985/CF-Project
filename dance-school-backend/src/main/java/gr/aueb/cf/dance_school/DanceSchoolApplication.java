package gr.aueb.cf.dance_school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DanceSchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(DanceSchoolApplication.class, args);
	}

}
