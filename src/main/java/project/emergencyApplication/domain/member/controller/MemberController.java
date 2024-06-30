package project.emergencyApplication.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.dto.GpsUpdateRequestDto;
import project.emergencyApplication.domain.member.dto.MemberInfoResponseDto;
import project.emergencyApplication.domain.member.dto.MemberUpdateRequestDto;
import project.emergencyApplication.domain.member.service.MemberService;

@Tag(name = "Member", description = "회원 정보")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 정보 조회 (Setting View)")
    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponseDto> memberInfo() {
        // SecurityUtil 에는 로그인한 유저의 ID 를 조회하는 메소드가 있음
        return ResponseEntity.ok(memberService.memberInfo(SecurityUtil.getCurrentMemberId()));
    }

    @Operation(summary = "회원 정보 수정 (Setting View)")
    @PostMapping("/update")
    public ResponseEntity<MemberInfoResponseDto> updateMemberInfo(@RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        return ResponseEntity.ok(memberService.updateMemberInfo(memberUpdateRequestDto, SecurityUtil.getCurrentMemberId()));
    }

    @Operation(summary = "홈 화면 (Home View)")
    @GetMapping("/home")
    public ResponseEntity<MemberInfoResponseDto> homeInfo() {
        return ResponseEntity.ok(memberService.homeInfo(SecurityUtil.getCurrentMemberId()));
    }

    @Operation(summary = "GPS API")
    @PostMapping("/gps")
    public ResponseEntity<String> updateGps(@RequestBody GpsUpdateRequestDto requestDto) {
        return ResponseEntity.ok(memberService.updateGps(requestDto, SecurityUtil.getCurrentMemberId()));
    }

    /** NOTE
     * 위험 상태 변경을 한 곳으로 묶는게 좋은지 고민
     */
    @Operation(summary = "위험 상태 종료")
    @PostMapping("/emg/termination")
    public ResponseEntity<String> emgTermination() {
        return ResponseEntity.ok(memberService.emgTermination(SecurityUtil.getCurrentMemberId()));
    }

    @Operation(summary = "회원 탈퇴")
    @PostMapping("/remote")
    public ResponseEntity<String> memberRemote() {
        return ResponseEntity.ok(memberService.memberRemote(SecurityUtil.getCurrentMemberId()));
    }

}
