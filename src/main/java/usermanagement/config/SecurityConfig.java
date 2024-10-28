package usermanagement.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import usermanagement.exception.JwtAuthenticationEntryPoint;
import usermanagement.filter.JwtRequestFilter;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String[] AUTHORIZED_ENDPOINTS = {
//            "/api/users/**",
//            "/auth/token",
//            "/auth/create",
            "/api/auth/login"
    };

    private final String[] ADMIN_ENDPOINTS = {
            "/admin/hello"
    };

    private final AuthenticationProvider authenticationProvider;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
//                    request.requestMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN");
                    request.requestMatchers(AUTHORIZED_ENDPOINTS).permitAll();
                    request.anyRequest().permitAll();
                })
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint));
//                .formLogin(Customizer.withDefaults());
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));
        return http.build();
    }

}