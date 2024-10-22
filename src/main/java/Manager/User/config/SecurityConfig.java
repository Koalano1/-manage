package Manager.User.config;

import Manager.User.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;

import java.util.Date;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityConstants securityConstants;

    public SecurityConfig(SecurityConstants securityConstants) {
        this.securityConstants = securityConstants;
    }

    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            Authentication auth) {
        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + securityConstants.getExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, securityConstants.getSecretKey())
                .compact();
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }

}