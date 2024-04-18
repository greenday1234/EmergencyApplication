package project.emergencyApplication.fcm.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.entity.ConnectionMember;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.MemberRepository;
import project.emergencyApplication.fcm.dto.FCMConnectionNotificationRequestDto;
import project.emergencyApplication.fcm.dto.FCMConnectionReceiveRequestDto;
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

    public String multipleSendNotificationByToken(FCMNotificationRequestDto requestDto) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

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

    public String sendConnectionNotification(FCMConnectionNotificationRequestDto requestDto) {
        Member findConnMember = memberRepository.findByEmail(requestDto.getConnectionEmail())
                .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 유저가 존재하지 않습니다."));

        if (findConnMember.getDeviceToken() != null) {
            Message message = createMessage(requestDto, findConnMember);

            try {
                firebaseMessaging.send(message);

                Messages connMessage = requestDto.createConnMessage(findConnMember.getMemberId());
                messageRepository.save(connMessage);

                Connection sendConn = requestDto.createSendConn(findConnMember);    // 상대방이 수락하기 전까진 계정 연동이 되지 않음
                connectionRepository.save(sendConn);
                return "알림을 성공적으로 전송했습니다.";
            } catch (FirebaseMessagingException e) {
                log.error(e.getMessage());
                return "알림 보내기를 실패했습니다.";
            }
        } else {
            return "전송하고자 하는 유저의 DeviceToken 이 존재하지 않습니다.";
        }
    }

    public String receiveConnectionNotification(FCMConnectionReceiveRequestDto requestDto) {
        Member sendMember = memberRepository.findByEmail(requestDto.getSendMemberEmail())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));

        Member receiveMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));

        Connection findConn = connectionRepository.findBySendConnectionIdAndReceiveConnectionId(sendMember.getMemberId(), receiveMember.getMemberId())
                .orElseThrow(() -> new RuntimeException("해당 요청이 없습니다."));

        if (sendMember.getDeviceToken() != null) {
            Message receiveMessage = createReceiveMessage(requestDto, sendMember);

            try {
                firebaseMessaging.send(receiveMessage);

                Messages connReceiveMessage = requestDto.createConnReceiveMessage(receiveMember.getMemberId());
                messageRepository.save(connReceiveMessage);

                updateConn(requestDto, findConn);

                createConnection(sendMember, receiveMember, findConn);

                return "알림 보내기에 성공했습니다.";
            }catch (FirebaseMessagingException e) {
                log.error(e.getMessage());
                return "알림 보내기를 실패했습니다.";
            }
        } else {
            return "전송하고자 하는 유저의 DeviceToken 이 존재하지 않습니다.";
        }
    }

    private void createConnection(Member sendMember, Member receiveMember, Connection findConn) {
        if (findConn.getSendBool() && findConn.getReceiveBool()) {  // 요청 수락

            ConnectionMember sendConnectionMember = ConnectionMember.createConnectionMember(sendMember);
            ConnectionMember receiveConnectionMember = ConnectionMember.createConnectionMember(receiveMember);

            sendMember.addConnectionMember(receiveConnectionMember);
            receiveMember.addConnectionMember(sendConnectionMember);
        }

            connectionRepository.delete(findConn);
    }

    private void updateConn(FCMConnectionReceiveRequestDto requestDto, Connection findConn) {
        if (!requestDto.getState()) {
            findConn.updateReceiveBool(requestDto.getState());
        }
    }

    /**
     * 계정 연동 수락 및 거절 메시지
     */
    private Message createReceiveMessage(FCMConnectionReceiveRequestDto requestDto, Member sendMember) {
        return Message.builder()
                .setToken(sendMember.getDeviceToken())
                .setNotification(Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getBody())
                        .build())
                .build();
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
        List<String> tokens = new ArrayList<>();
        for (ConnectionMember connectionMember : findMember.getConnectionMembers()) {
            Optional<Member> findConnMember = memberRepository.findById(connectionMember.getConnectionId());
            tokens.add(findConnMember.get().getDeviceToken());
        }
        return tokens;
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
        List<ConnectionMember> connectionMemberIds = findMember.getConnectionMembers();
        for (ConnectionMember connMember : connectionMemberIds) {
            Messages message = requestDto.createNotificationMessage(connMember.getConnectionId());
            messageRepository.save(message);
        }
    }
}
