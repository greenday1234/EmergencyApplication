package project.emergencyApplication.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import project.emergencyApplication.auth.dto.TokenDto;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import static project.emergencyApplication.auth.jwt.utils.SecurityMessage.*;

/**
 * 유저 정보로 JWT 토큰을 만들거나, 토큰을 바탕으로 유저 정보를 갖고 옴
 * JWT 에 관련된 암호화, 복호화, 검증 로직은 모두 이곳에서 이루어짐
 */

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;


    private final Key key;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secret,
                            @Value("${security.jwt.token.access-key-expire-length}")
                            long accessTokenExpireTime,
                            @Value("${security.jwt.token.access-key-expire-length}")
                            long refreshTokenExpireTime) {
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpireTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime;

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        //권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        //AccessToken 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "1"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth" : "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp" : 1234(예시)
                .signWith(key, SignatureAlgorithm.HS256)    // payload "alg" : "HS256"
                .compact();

        //RefreshToken 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {   //accessToken 에만 유저 정보가 담겨있기 때문에 accessToken 을 받음
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities); //SecurityContext 를 사용하기 위한 절차
    }

    private Claims parseClaims(String accessToken) {
        try {
          return Jwts.parser().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
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
