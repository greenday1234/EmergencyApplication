package project.emergencyApplication.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.dto.MemberInfoResponseDto;
import project.emergencyApplication.domain.member.dto.MemberUpdateRequestDto;
import project.emergencyApplication.domain.member.service.MemberService;

@Tag(name = "Member", description = "회원 정보")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 정보 조회")
    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponseDto> memberInfo() {
        // SecurityUtil 에는 로그인한 유저의 ID 를 조회하는 메소드가 있음
        return ResponseEntity.ok(memberService.memberInfo(SecurityUtil.getCurrentMemberId()));
    }

    @Operation(summary = "회원 정보 수정")
    @PostMapping("/update")
    public ResponseEntity<MemberInfoResponseDto> updateMemberInfo(@RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        return ResponseEntity.ok(memberService.updateMemberInfo(memberUpdateRequestDto));
    }

}
