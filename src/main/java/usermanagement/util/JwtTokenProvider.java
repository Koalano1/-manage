package usermanagement.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;


@Configuration
public class JwtTokenProvider {

    private final AuthenticationProvider authenticationProvider;
    //private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private String jwtSecret = "5681d1168e6df8cc24e1900bc4a2fee31e0442c4a1909693ebc3b16c5f5b3314a5e39f012c8ef22e9cd4ee1b0e2470d925e286f446b22d0cdbe1d2f9b8d6bb89";
    private int jwtExpirationMs = 86400000;


//    @Value("${app.jwt-secret}")
//    private String jwtSecret;

//    @Value("${app.app-jwt-expiration-milliseconds}")
//    private long jwtExpirationDate;
//
//
//    @Value("${app.app-jwt-renewal-milliseconds}")
//    private long jwtRenewalDate;

    public JwtTokenProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public String generateJwtToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid JWT token");
        }
    }
    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }
}
