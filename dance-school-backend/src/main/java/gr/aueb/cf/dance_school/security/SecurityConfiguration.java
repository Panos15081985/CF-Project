package gr.aueb.cf.dance_school.security;


import gr.aueb.cf.dance_school.authentication.JwtAuthenticationFilter;
import gr.aueb.cf.dance_school.core.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
/**
 * Ρυθμίζει την ασφάλεια της εφαρμογής με τη χρήση JWT και τη διαχείριση των ρόλων χρηστών.
 * Εφαρμόζει τις απαιτούμενες ρυθμίσεις για:
 * - Επιλογή των endpoints που επιτρέπονται για μη εξουσιοδοτημένους χρήστες.
 * - Διαχείριση εξαιρέσεων για μη εξουσιοδοτημένα αιτήματα και πρόσβαση σε περιορισμένα endpoints.
 * - Ρυθμίσεις CORS για διασταυρούμενη πρόσβαση.
 * - Ορισμό του φίλτρου JWT για τον έλεγχο της αυθεντικοποίησης.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
           .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
           .csrf(AbstractHttpConfigurer::disable)
           .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(myAuthenticationEntryPoint()))
           .exceptionHandling(exceptions ->exceptions.accessDeniedHandler(myCustomAccessDeniedHandler()))
           .authorizeHttpRequests(req -> req
               .requestMatchers(
                   "/v3/api-docs/**",
                   "/swagger-ui/**",
                   "/swagger-ui.html",
                   "/swagger-resources/**",
                   "/webjars/**"
               ).permitAll()
               .requestMatchers(
                       "/course/getAllCourses",
                       "/api/authenticate"
               ).permitAll()
               .requestMatchers(
                       "/course/update",
                       "/course/save",
                       "/course/delete/{id}"
               ).hasAnyAuthority(RoleType.Admin.name())
               .requestMatchers("/course/paginated").hasAnyAuthority(
                       RoleType.Admin.name(),
                       RoleType.Zumba_Coach.name(),
                       RoleType.HipHop_Coach.name(),
                       RoleType.Latin_Coach.name()
               )
               .requestMatchers(
                       "/coach/update",
                       "/coach/update/credentials",
                       "/coach/getAllCoaches",
                       "/coach/filtered"
               ).hasAnyAuthority(
                       RoleType.Admin.name(),
                       RoleType.Zumba_Coach.name(),
                       RoleType.HipHop_Coach.name(),
                       RoleType.Latin_Coach.name()
               )
               .requestMatchers(
                       "/coach/save",
                       "/coach/delete/{uuid}"
               ).hasAnyAuthority(RoleType.Admin.name())
               .requestMatchers(
                       "/dancer/update",
                       "/dancer/filtered"
               ).hasAnyAuthority(
                       RoleType.Admin.name(),
                       RoleType.Zumba_Coach.name(),
                       RoleType.HipHop_Coach.name(),
                       RoleType.Latin_Coach.name()
               )
               .requestMatchers(
                       "/dancer/save",
                       "/dancer/delete{uuid}"
               ).hasAnyAuthority(RoleType.Admin.name())
               .requestMatchers(
                       "/api/authenticationChangeUsernamePassword"
               ).hasAnyAuthority(
                   RoleType.Admin.name(),
                   RoleType.Zumba_Coach.name(),
                   RoleType.HipHop_Coach.name(),
                   RoleType.Latin_Coach.name()
                   )
           )
           .sessionManagement((session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
           .authenticationProvider(authenticationProvider())
           .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("https://coding-factory.apps.gov",
               "http://localhost:4200"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint myAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler myCustomAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
