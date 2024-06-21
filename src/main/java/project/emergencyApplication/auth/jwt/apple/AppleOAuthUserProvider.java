package project.emergencyApplication.auth.jwt.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.emergencyApplication.auth.dto.AppleLoginRequest;
import project.emergencyApplication.auth.dto.OAuthPlatformMemberResponse;
import project.emergencyApplication.auth.exception.InvalidAccessTokenException;

import java.security.PublicKey;
import java.util.Map;

/**
 * Apple 계정 유저 정보 반환
 */
@Component
@RequiredArgsConstructor
public class AppleOAuthUserProvider {

    private final AppleTokenParser appleJwtParser;
    private final AppleClient appleClient;
    private final ApplePublicKeyGenerator publicKeyGenerator;
    private final AppleClaimsValidator appleClaimsValidator;

    public OAuthPlatformMemberResponse getApplePlatformMember(AppleLoginRequest request) {
        // 1. id_token 의 헤더 정보 파싱
        Map<String, String> headers = appleJwtParser.parseHeaders(request.getIdToken());

        // 2. ApplePublicKey 들을 불러오기
        ApplePublicKeys applePublicKeys = appleClient.getApplePublicKeys();

        // 3. 불러온 key 들과 파싱한 헤더 정보를 바탕으로 알맞은 publicKey 생성
        PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers, applePublicKeys);

        // 4. 생성한 publicKey 를 사용해 id_token 의 claim 추출
        Claims claims = appleJwtParser.parsePublicKeyAndGetClaims(request.getIdToken(), publicKey);

        // 5. claim 검증
        validateClaims(claims);

        return new OAuthPlatformMemberResponse(claims.getSubject(), request.getName(), claims.get("email", String.class), request.getDeviceToken());
    }

    private void validateClaims(Claims claims) {
        if (!appleClaimsValidator.isValid(claims)) {
            throw new InvalidAccessTokenException("Apple OAuth Claims 값이 올바르지 않습니다.");
        }
    }
}
