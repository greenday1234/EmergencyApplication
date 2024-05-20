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
import project.emergencyApplication.message.ExceptionTexts;

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
        log.info("멤버 조회 전@@@@@@@@");
        Member findMember = findMember(memberId);
        log.info("멤버 조회 후 @@@@@@@@");
        return getMemberInfoResponseDto(findMember);
    }

    /**
     * GPS Update
     */
    public String updateGps(GpsUpdateRequestDto requestDto) {
        Member findMember = findMember(SecurityUtil.getCurrentMemberId());

        findMember.updateLocation(requestDto.getLatitude(), requestDto.getLongitude());

        return "GPS Update 완료";
    }

    /**
     * 위험 상태 종료
     */
    public String emgTermination() {
        Member findMember = findMember(SecurityUtil.getCurrentMemberId());

        findMember.updateEmgState(false);

        return "위험 상태 종료";
    }

    private MemberInfoResponseDto getMemberInfoResponseDto(Member findMember) {
        log.info("여기까진 돼요@@@@@@@@@@@@ 멤버 생성");
        List<ConnectionMember> connectionMembers = connectionMemberRepository.findAllByMember(findMember);
        MemberInfoResponseDto responseDto = new MemberInfoResponseDto().createMemberInfoResponseDto(findMember);
        for (ConnectionMember connectionMember : connectionMembers) {
            responseDto.addConnectionMemberDto(new ConnectionMemberDto()
                    .createConnectionMemberDto(connectionMember.getConnectionMember()));
        }
        log.info("여기서 터지나????? 커넥션 멤버");

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
                .orElseThrow(()-> new RuntimeException(ExceptionTexts.NOT_EXIST.getText()));
    }
}
