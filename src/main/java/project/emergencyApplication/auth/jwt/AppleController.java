package project.emergencyApplication.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AppleController {

    private final AppleOauthService appleOauthService;

    @PostMapping("api/apple/user")
    public String getAppleUser() {

        return "ok";
    }
}
