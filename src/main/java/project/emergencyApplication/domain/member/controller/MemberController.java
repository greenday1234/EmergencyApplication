package project.emergencyApplication.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.dto.MemberInfoResponseDto;
import project.emergencyApplication.domain.member.service.MemberService;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 정보 조회")
    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponseDto> memberInfo() {
        // SecurityUtill 에는 로그인한 유저의 ID 를 조회하는 메소드가 있음
        return ResponseEntity.ok(memberService.memberInfo(SecurityUtil.getCurrentMemberId()));
    }
}
