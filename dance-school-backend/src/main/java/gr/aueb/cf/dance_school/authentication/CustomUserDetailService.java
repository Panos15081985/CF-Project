package gr.aueb.cf.dance_school.authentication;

import gr.aueb.cf.dance_school.repository.CoachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final CoachRepository coachRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return coachRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("user not found with username: " + username));
    }
}
