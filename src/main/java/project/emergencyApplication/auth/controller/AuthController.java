package project.emergencyApplication.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.emergencyApplication.auth.dto.AppleLoginRequest;
import project.emergencyApplication.auth.dto.OAuthTokenResponse;
import project.emergencyApplication.auth.service.AuthService;

import javax.validation.Valid;

@Tag(name = "Login", description = "인증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "애플 OAuth 로그인")
    @PostMapping("/apple")
    public ResponseEntity<OAuthTokenResponse> loginApple(@RequestBody @Valid AppleLoginRequest request) {
        System.out.println("request>>>> "+ request);
        OAuthTokenResponse response = authService.appleOAuthLogin(request);
        return ResponseEntity.ok(response);
    }
}