package project.emergencyApplication.auth.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.security.PublicKey;
import java.util.Map;

/**
 * Header를 Decode해 일치하는 JWK를 찾을 alg와 kid 값을 얻는다.
 */
@RequiredArgsConstructor
@Component
public class AppleTokenParser {

    private static final String IDENTITY_TOKEN_VALUE_DELIMITER = "\\.";
    private static final int HEADER_INDEX = 0;

    private final ObjectMapper objectMapper;

    public Map<String, String> parseHeader(final String appleToken) {
        try {
            final String encodedHeader = appleToken.split(IDENTITY_TOKEN_VALUE_DELIMITER)[HEADER_INDEX];
            final String decodedHeader = new String(Base64Utils.decodeFromUrlSafeString(encodedHeader));
            return objectMapper.readValue(decodedHeader, Map.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException("appleToken 값이 jwt 형식인지, 값이 정상적인지 확인해주세요.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("디코드된 헤더를 Map 형태로 분류할 수 없습니다. 헤더를 확인해주세요.");
        }
    }

    /**
     * id_token Claim 추출
     */
    public Claims extractClaims(final String appleToken, final PublicKey publicKey) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(appleToken)
                    .getPayload();
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("지원되지 않는 jwt 타입");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("비어있는 jwt");
        } catch (JwtException e) {
            throw new JwtException("jwt 검증 or 분석 오류");
        }
    }
}
