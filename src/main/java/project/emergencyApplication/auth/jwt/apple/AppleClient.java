package project.emergencyApplication.auth.jwt.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import project.emergencyApplication.auth.config.FeignConfig;

/**
 * JWK 리스트 받아오기
 */
@FeignClient(name = "apple-public-key-client", url = "https://appleid.apple.com", configuration = FeignConfig.class)
public interface AppleClient {

    @GetMapping("/auth/keys")
    ApplePublicKeys getApplePublicKeys();
}
