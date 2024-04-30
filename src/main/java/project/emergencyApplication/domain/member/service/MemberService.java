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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ConnectionMemberRepository connectionMemberRepository;

    /**
     * 내 정보 조회 (SettingView)
     */
    public MemberInfoResponseDto memberInfo(Long memberId) {
        Member findMember = findMember(memberId);
        return getMemberInfoResponseDto(findMember);
    }

    /**
     * 내 정보 수정 (SettingView)
     */
    public MemberInfoResponseDto updateMemberInfo(MemberUpdateRequestDto memberUpdateRequestDto) {
        Member findMember = findMember(SecurityUtil.getCurrentMemberId());
        findMember.updateMember(memberUpdateRequestDto);
        return getMemberInfoResponseDto(findMember);
    }

    /**
     * 내 정보 조회 (HomeView)
     */
    public MemberInfoResponseDto homeInfo(Long memberId) {
        Member findMember = findMember(memberId);
        return getMemberInfoResponseDto(findMember);
    }

    /**
     * GPS Update
     */
    public void updateGps(GpsUpdateRequestDto requestDto) {
        Member findMember = findMember(SecurityUtil.getCurrentMemberId());

        findMember.updateLocation(requestDto.getLatitude(), requestDto.getLongitude());
    }

    /**
     * 위험 상태 종료
     */
    public void emgTermination() {
        Member findMember = findMember(SecurityUtil.getCurrentMemberId());

        findMember.updateEmgState(false);
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
                .profileUrl("1-2-3-4-5-6")
                .location(new Location(36.9791, 126.9222))
                .emgState(false)
                .build();
        responseDto.addConnectionMemberDto(connectionMemberDto);

        ConnectionMemberDto connectionMemberDto1 = ConnectionMemberDto.builder()
                .name("찬희")
                .email("11@naver.com")
                .profileUrl("1-1-1-1-1-")
                .location(new Location(36.7745, 126.9338))
                .emgState(true)
                .build();
        responseDto.addConnectionMemberDto(connectionMemberDto1);
        return responseDto;
    }

    public Member findMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 회원입니다!"));
    }
}
