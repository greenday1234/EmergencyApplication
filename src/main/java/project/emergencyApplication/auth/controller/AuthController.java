package project.emergencyApplication.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.emergencyApplication.auth.dto.AppleLoginRequest;
import project.emergencyApplication.auth.dto.TokenDto;
import project.emergencyApplication.auth.dto.TokenRequestDto;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.auth.service.AuthService;

import javax.validation.Valid;

@Tag(name = "OAuth", description = "인증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "애플 OAuth 로그인")
    @PostMapping("/apple/login")
    public ResponseEntity<TokenDto> loginApple(@RequestBody @Valid AppleLoginRequest request) {
        return ResponseEntity.ok(authService.appleOAuthLogin(request));
    }

    /** NOTE
     * 강제 로그아웃 시 어떻게 할 것인지 정해야 함!!
     * 시스템 종료
     */
    @Operation(summary = "애플 OAuth 로그아웃")
    @PostMapping("/apple/logout")
    public ResponseEntity<String> logoutApple() {
        return ResponseEntity.ok(authService.appleOAuthLogout(SecurityUtil.getCurrentMemberId()));
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

}