package project.emergencyApplication.notification.service;

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
import project.emergencyApplication.texts.ExceptionTexts;
import project.emergencyApplication.texts.MessageTexts;
import project.emergencyApplication.notification.dto.ConnectionNotificationRequestDto;
import project.emergencyApplication.notification.dto.NotificationRequestDto;
import project.emergencyApplication.notification.dto.SendReceiveMember;
import project.emergencyApplication.domain.connection.entity.Connection;
import project.emergencyApplication.domain.message.entity.Message;
import project.emergencyApplication.domain.connection.repository.ConnectionRepository;
import project.emergencyApplication.domain.message.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final ConnectionRepository connectionRepository;
    private final ConnectionMemberRepository connectionMemberRepository;

    /**
     * 위험 알림 메시지
     */
    public String multipleSendNotificationByToken(NotificationRequestDto requestDto) {
        Member findMember = findThisMember();

        if (deviceTokenValid(findMember)) {
            MulticastMessage messages = createMessages(requestDto, findMember);

            try {
                firebaseMessaging.sendMulticast(messages);

                findMember.updateEmgState(true);    // 위험 상태 변환
                saveNotificationMessages(requestDto, findMember);
                return MessageTexts.SUCCESS.getText();
            } catch (FirebaseMessagingException e) {
                log.error(e.getMessage());
                return MessageTexts.FAIL.getText();
            }
        } else {
            return MessageTexts.EMPTY_DEVICETOKEN.getText();
        }
    }

    /**
     * 계정 연동 메시지
     */
    public String connectionNotification(ConnectionNotificationRequestDto requestDto) {

        /**
         * 코드 수정하긴 해야함 validateFirstRequest
         */
        SendReceiveMember sendReceiveMember = validateFirstRequest(requestDto); //최초 요청인지, 요청 응답인지 검증 후 send, receive 설정

        Member receiveMember = sendReceiveMember.getReceiveMember();
        Member sendMember = sendReceiveMember.getSendMember();

        Optional<Connection> conn = findConnection(sendMember, receiveMember);

        if (receiveMember.getDeviceToken() != null) {
            com.google.firebase.messaging.Message message = createMessage(requestDto, receiveMember);

            try {
                firebaseMessaging.send(message);

                // 계정 연동 요청 DB 가 있는지 확인하고, 없으면 DB 생성, 있으면 업데이트
                checkConnection(requestDto, receiveMember, sendMember, conn);

                return MessageTexts.SUCCESS.getText();
            } catch (FirebaseMessagingException e) {
                log.error(e.getMessage());
                return MessageTexts.FAIL.getText();
            }
        } else {
            return MessageTexts.EMPTY_DEVICETOKEN.getText();
        }
    }

    public void checkConnection(ConnectionNotificationRequestDto requestDto,
                                Member receiveMember, Member sendMember, Optional<Connection> conn) {
        if (conn.isEmpty()) {
            Connection sendConn = requestDto.createSendConn(receiveMember);
            connectionRepository.save(sendConn);
        } else {
            updateConn(requestDto, conn.get());
            createConnection(receiveMember, sendMember, conn.get());
        }
    }

    /**
     * 최초 요청인지, 요청 응답인지 검증 후 send, receive 반환
     */
    private SendReceiveMember validateFirstRequest(ConnectionNotificationRequestDto requestDto) {
        if (requestDto.getFirstRequest()) {
            return SendReceiveMember.builder()
                    .receiveMember(findConnMemberByEmail(requestDto.getConnectionEmail()))
                    .sendMember(findThisMember())
                    .build();
        } else {
            return SendReceiveMember.builder()
                    .receiveMember(findThisMember())
                    .sendMember(findConnMemberByEmail(requestDto.getConnectionEmail()))
                    .build();
        }
    }

    private void createConnection(Member sendMember, Member receiveMember, Connection findConn) {
        if (findConn.getSendBool() && findConn.getReceiveBool()) {  // 요청 수락
            connectionMemberMapping(sendMember, receiveMember);
        }
        connectionRepository.delete(findConn);
    }

    /**
     * 계정 연동
     */
    private void connectionMemberMapping(Member sendMember, Member receiveMember) {

        ConnectionMember sendConnectionMember = new ConnectionMember()
                .createConnectionMember(sendMember, receiveMember);
        connectionMemberRepository.save(sendConnectionMember);

        ConnectionMember receiveConnectionMember = new ConnectionMember()
                .createConnectionMember(receiveMember, sendMember);
        connectionMemberRepository.save(receiveConnectionMember);
    }

    private void updateConn(ConnectionNotificationRequestDto requestDto, Connection findConn) {
        if (!requestDto.getState()) {
            findConn.updateReceiveBool(requestDto.getState());
        }
    }

    /**
     * 계정 연동 메시지
     */
    private com.google.firebase.messaging.Message createMessage(ConnectionNotificationRequestDto requestDto, Member findConnMember) {
        return com.google.firebase.messaging.Message.builder()
                .setToken(findConnMember.getDeviceToken())
                .setNotification(Notification.builder()
                        .build())
                .build();
    }

    /**
     * 다중 알림 메시지
     */
    private MulticastMessage createMessages(NotificationRequestDto requestDto, Member findMember) {
        return MulticastMessage.builder()
                .addAllTokens(getConnectionMemberDeviceTokens(findMember))
                .setNotification(Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getBody())
                        .build())
                .build();
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

    /**
     * Message 저장
     */
    private void saveNotificationMessages(NotificationRequestDto requestDto, Member findMember) {
        List<Member> connectionMembers = getConnectionMembers(findMember);
        for (Member connectionMember : connectionMembers) {
            Message message = requestDto.createNotificationMessage(connectionMember.getMemberId());
            messageRepository.save(message);
        }
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

    public Member findThisMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException(ExceptionTexts.NOT_EXIST.getText()));
    }

    public Member findConnMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(ExceptionTexts.NOT_EXIST_EMAIL.getText()));
    }

    private Optional<Connection> findConnection(Member sendMember, Member receiveMember) {
        return connectionRepository
                .findBySendConnectionIdAndReceiveConnectionId(sendMember.getMemberId(), receiveMember.getMemberId());
    }
}