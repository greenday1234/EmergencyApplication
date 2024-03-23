package project.emergencyApplication.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class AppleController {

    private final AppleOauthService appleOauthService;

    @PostMapping("/api/apple/user")
    public ResponseEntity<Object> getAppleUser(@RequestBody HashMap<String, Object> appleToken) {

        AppleUser appleUser = appleOauthService.createAppleUser(String.valueOf(appleToken.get("id_token")));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appleUser);
    }
}
