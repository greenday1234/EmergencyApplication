package project.emergencyApplication.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.auth.entity.RefreshToken;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.auth.repository.RefreshTokenRepository;
import project.emergencyApplication.domain.connection.entity.Connection;
import project.emergencyApplication.domain.connection.repository.ConnectionRepository;
import project.emergencyApplication.domain.member.dto.ConnectionMemberDto;
import project.emergencyApplication.domain.member.dto.GpsUpdateRequestDto;
import project.emergencyApplication.domain.member.dto.MemberInfoResponseDto;
import project.emergencyApplication.domain.member.dto.MemberUpdateRequestDto;
import project.emergencyApplication.domain.member.entity.ConnectionMember;
import project.emergencyApplication.domain.member.entity.Location;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.ConnectionMemberRepository;
import project.emergencyApplication.domain.member.repository.MemberRepository;
import project.emergencyApplication.domain.message.entity.Message;
import project.emergencyApplication.domain.message.repository.MessageRepository;
import project.emergencyApplication.texts.ExceptionTexts;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ConnectionMemberRepository connectionMemberRepository;
    private final ConnectionRepository connectionRepository;
    private final MessageRepository messageRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 내 정보 조회 (SettingView)
     */
    @Transactional(readOnly = true)
    public MemberInfoResponseDto memberInfo(Long memberId) {
        Member member = findMember(memberId);
        return getMemberInfoResponseDto(member);
    }

    /**
     * 내 정보 수정 (SettingView)
     */
    @Transactional
    public MemberInfoResponseDto updateMemberInfo(MemberUpdateRequestDto memberUpdateRequestDto, Long memberId) {
        Member member = findMember(memberId);
        member.updateMember(memberUpdateRequestDto);
        return getMemberInfoResponseDto(member);
    }

    /**
     * 내 정보 조회 (HomeView)
     */
    @Transactional(readOnly = true)
    public MemberInfoResponseDto homeInfo(Long memberId) {
        Member member = findMember(memberId);
        return getMemberInfoResponseDto(member);
    }

    /**
     * GPS Update
     */
    @Transactional
    public String updateGps(GpsUpdateRequestDto requestDto, Long memberId) {
        Member member = findMember(memberId);

        member.updateLocation(requestDto.getLatitude(), requestDto.getLongitude());

        return "GPS Update 완료";
    }

    /**
     * 위험 상태 종료
     */
    @Transactional
    public String emgTermination(Long memberId) {
        Member member = findMember(memberId);

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

        /**
         * 더미 데이터
         */
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

    /**
     * 실무에서는 회원 탈퇴 시 delete 를 거의 사용하지 않음
     * (삭제된 데이터를 확인해야 하는 경우가 있을 수 있음)
     */
    @Transactional
    public String memberRemote(Long memberId) {
        Member member = findMember(memberId);

        /** NOTE
         * 추후 CASECADE 사용해 연쇄삭제 코드로 변경 요망
         */

        // 연동 계정 요청 처리
        deleteConnection(memberId);
        // 메시지 처리
        deleteMessage(memberId);
        // refreshToken 삭제
        deleteRefreshToken(memberId);

        memberRepository.delete(member);

        return "회원 탈퇴 성공";
    }

    private void deleteRefreshToken(Long memberId) {
        RefreshToken refreshToken = refreshTokenRepository.findByKey(memberId)
                .orElseThrow(() -> new RuntimeException("해당 유저의 refreshToken 이 없습니다."));
        refreshTokenRepository.delete(refreshToken);
    }

    private void deleteMessage(Long memberId) {
        List<Message> sendMessages = messageRepository.findBySendMemberId(memberId);
        for (Message sendMessage : sendMessages) {
            messageRepository.delete(sendMessage);
        }
    }

    private void deleteConnection(Long memberId) {
        List<Connection> receiveConnections = connectionRepository.findByReceiveConnectionId(memberId);
        List<Connection> sendConnections = connectionRepository.findBySendConnectionId(memberId);

        for (Connection sendConnection : sendConnections) {
            connectionRepository.delete(sendConnection);
        }

        for (Connection receiveConnection : receiveConnections) {
            connectionRepository.delete(receiveConnection);
        }
    }

    private Member findMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()-> new RuntimeException(ExceptionTexts.NOT_EXIST.getText()));
    }
}
