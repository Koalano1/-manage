package usermanagement.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import usermanagement.exception.CustomAuthenticationEntryPoint;
import usermanagement.filter.JwtRequestFilter;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String[] AUTHRIZE_ENPOINT = {
            "/api/users/**",
            "/auth/token",
            "/auth/create"
    };

    private final AuthenticationProvider authenticationProvider;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint entryPoint) throws Exception {
        http
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
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
                            .requestMatchers(AUTHRIZE_ENPOINT).authenticated()
                            .anyRequest().permitAll();
                });

        return http.build();
    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

}