package project.emergencyApplication.auth.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static project.emergencyApplication.auth.jwt.utils.SecurityMessage.*;

@Component
@Slf4j
public class JwtTokenProvider {

    private Key key;
    private final String secret;
    private final Long validityAccessTokenInMilliseconds;


    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secret,
                            @Value("${security.jwt.token.access-key-expire-length}")
                            long validityAccessTokenInMilliseconds) {
        this.secret = secret;
        this.validityAccessTokenInMilliseconds = validityAccessTokenInMilliseconds;
    }

    public String createAccessToken(Authentication authentication) {
        List<String> authorities =  authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String authoritiesString = String.join(",", authorities);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityAccessTokenInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authoritiesString)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(),
                "", authorities), token, authorities);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error(INVALID_JWT);
        } catch (ExpiredJwtException e) {
            log.error(EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            log.error(UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            log.error(WRONG_JWT);
        }
        return false;
    }
}
