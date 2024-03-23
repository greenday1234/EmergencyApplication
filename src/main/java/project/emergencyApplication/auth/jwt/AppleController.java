package project.emergencyApplication.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Writer;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AppleController {

    private final AppleOauthService appleOauthService;

    @PostMapping("api/apple/user")
    public String getAppleUser(@RequestBody HashMap<String, Object> appleToken) {

        AppleUser appleUser = appleOauthService.createAppleUser(String.valueOf(appleToken.get("id_token")));

        return "ok";
    }
}
