package project.emergencyApplication.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.dto.ConnectionMemberDto;
import project.emergencyApplication.domain.member.dto.GpsUpdateRequestDto;
import project.emergencyApplication.domain.member.dto.MemberInfoResponseDto;
import project.emergencyApplication.domain.member.dto.MemberUpdateRequestDto;
import project.emergencyApplication.domain.member.entity.ConnectionMember;
import project.emergencyApplication.domain.member.entity.Location;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.ConnectionMemberRepository;
import project.emergencyApplication.domain.member.repository.MemberRepository;
import project.emergencyApplication.texts.ExceptionTexts;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ConnectionMemberRepository connectionMemberRepository;

    /**
     * 내 정보 조회 (SettingView)
     */
    public MemberInfoResponseDto memberInfo(Long memberId) {
        Member member = findMember(memberId);
        return getMemberInfoResponseDto(member);
    }

    /**
     * 내 정보 수정 (SettingView)
     */
    @Transactional
    public MemberInfoResponseDto updateMemberInfo(MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        member.updateMember(memberUpdateRequestDto);
        return getMemberInfoResponseDto(member);
    }

    /**
     * 내 정보 조회 (HomeView)
     */
    public MemberInfoResponseDto homeInfo(Long memberId) {
        Member member = findMember(memberId);
        return getMemberInfoResponseDto(member);
    }

    /**
     * GPS Update
     */
    @Transactional
    public String updateGps(GpsUpdateRequestDto requestDto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        member.updateLocation(requestDto.getLatitude(), requestDto.getLongitude());

        return "GPS Update 완료";
    }

    /**
     * 위험 상태 종료
     */
    @Transactional
    public String emgTermination() {
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        member.updateEmgState(false);

        return "위험 상태 종료";
    }

    private MemberInfoResponseDto getMemberInfoResponseDto(Member findMember) {
        List<ConnectionMember> connectionMembers = connectionMemberRepository.findAllByMember(findMember);
        MemberInfoResponseDto responseDto = new MemberInfoResponseDto().createMemberInfoResponseDto(findMember);
        for (ConnectionMember connectionMember : connectionMembers) {
            responseDto.addConnectionMemberDto(new ConnectionMemberDto()
                    .createConnectionMemberDto(connectionMember.getConnectionMember()));
        }

        ConnectionMemberDto connectionMemberDto = ConnectionMemberDto.builder()
                .name("안용")
                .email("1234@naver.com")
                .profileUrl("https://firebasestorage.googleapis.com/v0/b/lmessenger-d0f09.appspot.com/o/41E08E5B-37E8-4335-B405-E44AA4944C4A_1_105_c.jpeg?alt=media&token=94a79810-7f80-4564-bcba-6919453b8ad4")
                .location(new Location(36.9791, 126.9222))
                .emgState(false)
                .build();
        responseDto.addConnectionMemberDto(connectionMemberDto);

        ConnectionMemberDto connectionMemberDto1 = ConnectionMemberDto.builder()
                .name("찬희")
                .email("11@naver.com")
                .profileUrl("https://firebasestorage.googleapis.com/v0/b/lmessenger-d0f09.appspot.com/o/Users%2FJTBTBtT24HckerXzUUhIStw72U52%2F055F4CC8-B854-4C7F-ADD1-4DCㄱ8BB80DBE9?alt=media&token=281e822e-24cc-4b3b-858f-3275c16b5230")
                .location(new Location(36.7745, 126.9338))
                .emgState(true)
                .build();
        responseDto.addConnectionMemberDto(connectionMemberDto1);

        return responseDto;
    }

    public Member findMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()-> new RuntimeException(ExceptionTexts.NOT_EXIST.getText()));
    }
}
