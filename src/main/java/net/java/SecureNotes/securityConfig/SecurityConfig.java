package net.java.SecureNotes.securityConfig;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.security.config.Customizer.withDefaults;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import net.java.SecureNotes.Repository.RoleRepository;
import net.java.SecureNotes.Repository.UserRepository;
import net.java.SecureNotes.entity.AppRole;
import net.java.SecureNotes.entity.Role;
import net.java.SecureNotes.entity.User;
import net.java.SecureNotes.securityConfig.jwtutils.AuthTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class SecurityConfig {
	@Autowired
	private UserDetailsService userdetailservice;
	@Autowired
	private AuthTokenFilter authtokenfilter;
	
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    	//http.csrf(csrf->csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
    	  http.csrf(AbstractHttpConfigurer::disable)
    	  .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          .sessionManagement(session ->
          session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests((requests)
                -> requests
                .requestMatchers("/api/csrf-token").permitAll()
                .requestMatchers("/api/notes/**").hasRole("ADMIN")
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated());
        
        http.authenticationProvider(authenticationProvider())
        .addFilterBefore(
                authtokenfilter,
                UsernamePasswordAuthenticationFilter.class
            );
    //    .addFilterBefore(,UsernamePasswordAuthenticationFilter.class);
       // http.formLogin(withDefaults());
        //http.httpBasic(withDefaults());
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
            List.of("http://localhost:3000")
        );

        configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
            List.of("Authorization", "Content-Type", "Accept")
        );

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    
    @Bean
    public PasswordEncoder passwordencoder() {
    	return new BCryptPasswordEncoder();
    }   
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider provider=new  DaoAuthenticationProvider();
    	provider.setUserDetailsService(userdetailservice);
    	provider.setPasswordEncoder(passwordencoder());
    	return provider;
    }
    
    

//    @Bean
//    public CommandLineRunner initData(RoleRepository roleRepository,
//                                      UserRepository userRepository,PasswordEncoder passwordencoder) {
//        return args -> {
//            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
//                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));
//
//            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
//                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));
//
//            if (!userRepository.existsByUserName("user1")) {
//                User user1 = new User("user1", "user1@example.com", passwordencoder.encode("password1"));
//                user1.setAccountNonLocked(false);
//                user1.setAccountNonExpired(true);
//                user1.setCredentialsNonExpired(true);
//                user1.setEnabled(true);
//                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
//                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
//                user1.setTwoFactorEnabled(false);
//                user1.setSignUpMethod("email");
//                user1.setRole(userRole);
//                userRepository.save(user1);
//            }
//
//            if (!userRepository.existsByUserName("admin")) {
//                User admin = new User("admin", "admin@example.com", passwordencoder.encode("adminPass"));
//                admin.setAccountNonLocked(true);
//                admin.setAccountNonExpired(true);
//                admin.setCredentialsNonExpired(true);
//                admin.setEnabled(true);
//                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
//                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
//                admin.setTwoFactorEnabled(false);
//                admin.setSignUpMethod("email");
//                admin.setRole(adminRole);
//                userRepository.save(admin);
//            }
        //};
    //}
}
