package project.emergencyApplication.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.emergencyApplication.auth.dto.AppleLoginRequest;
import project.emergencyApplication.auth.dto.TokenDto;
import project.emergencyApplication.auth.service.AuthService;

import javax.validation.Valid;

@Tag(name = "Login", description = "인증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    // SecurityConfig 에서 해당 URL 요청은 허용했기 때문에 토큰 검증 로직은 타지 않는다.
    @Operation(summary = "애플 OAuth 로그인")
    @PostMapping("/apple")
    public ResponseEntity<TokenDto> loginApple(@RequestBody @Valid AppleLoginRequest request) {
        return ResponseEntity.ok(authService.appleOAuthLogin(request));
    }

    @GetMapping("/hello")
    public String helloApi() {
        return "ok";
    }
}