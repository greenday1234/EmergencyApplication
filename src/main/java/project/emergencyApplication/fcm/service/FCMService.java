package project.emergencyApplication.fcm.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.entity.ConnectionMember;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.ConnectionMemberRepository;
import project.emergencyApplication.domain.member.repository.MemberRepository;
import project.emergencyApplication.fcm.dto.FCMConnectionNotificationRequestDto;
import project.emergencyApplication.fcm.dto.FCMNotificationRequestDto;
import project.emergencyApplication.fcm.entity.Connection;
import project.emergencyApplication.fcm.entity.Messages;
import project.emergencyApplication.fcm.repository.ConnectionRepository;
import project.emergencyApplication.fcm.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final ConnectionRepository connectionRepository;
    private final ConnectionMemberRepository connectionMemberRepository;

    /**
     * 위험 알림 메시지
     */
    public String multipleSendNotificationByToken(FCMNotificationRequestDto requestDto) {
        Member findMember = findThisMember();

        if (deviceTokenValid(findMember)) {
            MulticastMessage messages = createMessages(requestDto, findMember);

            try {
                firebaseMessaging.sendMulticast(messages);

                saveNotificationMessages(requestDto, findMember);
                return "알림을 성공적으로 전송했습니다.";
            } catch (FirebaseMessagingException e) {
                log.error(e.getMessage());
                return "알림 보내기를 실패했습니다.";
            }
        } else {
            return "서버에 저장된 유저의 DeviceToken 이 존재하지 않습니다.";
        }
    }

    /**
     * 계정 연동 메시지
     */
    public String connectionNotification(FCMConnectionNotificationRequestDto requestDto) {
        Member receiveMember = findConnMemberByEmail(requestDto.getConnectionEmail());
        Member sendMember = findThisMember();

        // 처음 요청할 때의 send, receive 와 응답할 떄의 send, receive 가 달라 connection 엔티티를 못찾음
        // 해당 문제 수정해야 함!!!!
        Optional<Connection> conn = findConnection(sendMember, receiveMember);

        if (receiveMember.getDeviceToken() != null) {
            Message message = createMessage(requestDto, receiveMember);

            try {
                firebaseMessaging.send(message);

                Messages connMessage = requestDto.createConnMessage(receiveMember.getMemberId());
                messageRepository.save(connMessage);

                // 계정 연동 요청 DB 가 있는지 확인하고, 없으면 DB 생성, 있으면 업데이트
                if (conn.isEmpty()) {
                    Connection sendConn = requestDto.createSendConn(receiveMember);
                    connectionRepository.save(sendConn);
                } else {
                    updateConn(requestDto, conn.get());
                    createConnection(receiveMember, sendMember, conn.get());
                }

                return "알림을 성공적으로 전송했습니다.";
            } catch (FirebaseMessagingException e) {
                log.error(e.getMessage());
                return "알림 보내기를 실패했습니다.";
            }
        } else {
            return "전송하고자 하는 유저의 DeviceToken 이 존재하지 않습니다.";
        }
    }

    private void createConnection(Member sendMember, Member receiveMember, Connection findConn) {
        if (findConn.getSendBool() && findConn.getReceiveBool()) {  // 요청 수락
            createConnectionMember(sendMember, receiveMember);
        }
        connectionRepository.delete(findConn);
    }

    private void createConnectionMember(Member sendMember, Member receiveMember) {

        ConnectionMember sendConnectionMember = new ConnectionMember()
                .createConnectionMember(sendMember, receiveMember);
        connectionMemberRepository.save(sendConnectionMember);

        ConnectionMember receiveConnectionMember = new ConnectionMember()
                .createConnectionMember(receiveMember, sendMember);
        connectionMemberRepository.save(receiveConnectionMember);
    }

    private void updateConn(FCMConnectionNotificationRequestDto requestDto, Connection findConn) {
        if (!requestDto.getState()) {
            findConn.updateReceiveBool(requestDto.getState());
        }
    }

    /**
     * 계정 연동 메시지
     */
    private Message createMessage(FCMConnectionNotificationRequestDto requestDto, Member findConnMember) {
        return Message.builder()
                .setToken(findConnMember.getDeviceToken())
                .setNotification(Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getBody())
                        .build())
                .build();
    }

    /**
     * 다중 알림 메시지
     */
    private MulticastMessage createMessages(FCMNotificationRequestDto requestDto, Member findMember) {
        return MulticastMessage.builder()
                .addAllTokens(getConnectionMemberDeviceTokens(findMember))
                .setNotification(Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getBody())
                        .build())
                .build();
    }

    private List<String> getConnectionMemberDeviceTokens(Member findMember) {
        List<Member> connectionMembers = getConnectionMembers(findMember);
        List<String> tokens = new ArrayList<>();
        for (Member connectionMember : connectionMembers) {
            tokens.add(connectionMember.getDeviceToken());
        }
        return tokens;
    }

    private List<Member> getConnectionMembers(Member findMember) {
        List<Member> members = new ArrayList<>();
        List<ConnectionMember> connectionMembers = connectionMemberRepository.findAllByMember(findMember);
        for (ConnectionMember connectionMember : connectionMembers) {
            members.add(connectionMember.getConnectionMember());
        }
        return members;
    }

    /**
     * 연동된 계정들의 모든 DeviceToken 값이 존재하는지 검증
     */
    private boolean deviceTokenValid(Member findMember) {
        List<String> deviceTokens = getConnectionMemberDeviceTokens(findMember);
        for (String deviceToken : deviceTokens) {
            if (deviceToken.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void saveNotificationMessages(FCMNotificationRequestDto requestDto, Member findMember) {
        List<Member> connectionMembers = getConnectionMembers(findMember);
        for (Member connectionMember : connectionMembers) {
            Messages message = requestDto.createNotificationMessage(connectionMember.getMemberId());
            messageRepository.save(message);
        }
    }

    public Member findThisMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
    }

    public Member findConnMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 유저가 존재하지 않습니다."));
    }

    private Optional<Connection> findConnection(Member sendMember, Member receiveMember) {
        Optional<Connection> conn = connectionRepository
                .findBySendConnectionIdAndReceiveConnectionId(sendMember.getMemberId(), receiveMember.getMemberId());
        return conn;
    }
}