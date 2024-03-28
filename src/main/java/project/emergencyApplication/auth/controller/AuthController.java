package project.emergencyApplication.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.emergencyApplication.auth.dto.AppleLoginRequest;
import project.emergencyApplication.auth.dto.OAuthTokenResponse;
import project.emergencyApplication.auth.jwt.JwtFilter;
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
        OAuthTokenResponse response = authService.appleOAuthLogin(request);
        HttpHeaders httpHeaders = new HttpHeaders();

        //response Header 에 accessToken 넣기
        httpHeaders.add(JwtFilter.HEADER_AUTHORIZATION, JwtFilter.TOKEN_PREFIX + response.getAccessToken());

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/hello")
    public String helloApi() {
        return "ok";
    }
}