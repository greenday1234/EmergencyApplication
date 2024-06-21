package project.emergencyApplication.auth.jwt.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * JWK 리스트 받아오기
 */
@FeignClient(name = "apple-public-key", url = "https://appleid.apple.com/auth")
public interface AppleClient {

    @GetMapping("/keys")
    ApplePublicKeys getApplePublicKeys();
}
