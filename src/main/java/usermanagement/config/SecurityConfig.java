package usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import usermanagement.exception.CustomAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] WHITELIST = {
            "/api/users/create"
    };


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint entryPoint) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(entryPoint))
                .authorizeHttpRequests((authorize) -> {
//                    authorize.requestMatchers("/api/auth/signingoogle","/api/auth/profile").authenticated();
//                    authorize.requestMatchers("/", "/error", "/webjars/**","/api/auth/**", "/oauth2/**").permitAll();
//                    authorize.requestMatchers("/api/admin/**","/api/schedule/**","/api/account/**").hasAuthority("ADMIN");
//                    authorize.requestMatchers("/api/user/**").hasAnyAuthority("EMPLOYEE","ADMIN");
                    authorize
                            .requestMatchers(WHITELIST).authenticated()
                            .anyRequest().permitAll();
                });
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}