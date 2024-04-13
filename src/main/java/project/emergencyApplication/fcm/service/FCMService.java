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
import project.emergencyApplication.fcm.entity.ReceiveMessage;
import project.emergencyApplication.fcm.entity.SendMessage;
import project.emergencyApplication.fcm.repository.ConnectionRepository;
import project.emergencyApplication.fcm.repository.ReceiveMessageRepository;
import project.emergencyApplication.fcm.repository.SendMessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;
    private final ReceiveMessageRepository receiveMessageRepository;
    private final SendMessageRepository sendMessageRepository;
    private final ConnectionRepository connectionRepository;

    public String multipleSendNotificationByToken(FCMNotificationRequestDto requestDto) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        if (deviceTokenValid(findMember)) {
            MulticastMessage messages = createMessages(requestDto, findMember);

            try {
                firebaseMessaging.sendMulticast(messages);

                SendMessage sendMessage = requestDto.createSendNotificationMessage();
                sendMessageRepository.save(sendMessage);

                saveReceiveNotificationMessages(requestDto, findMember);
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

                SendMessage sendMessage = requestDto.createSendConnMessage();
                sendMessageRepository.save(sendMessage);

                ReceiveMessage receiveMessage = requestDto.createReceiveMessage(findConnMember);
                receiveMessageRepository.save(receiveMessage);

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

    }

    /**
     * 계정 연동 메시지
     */
    private Message createMessage(FCMConnectionNotificationRequestDto requestDto, Member findConnMember) {
        Message message = Message.builder()
                .setToken(findConnMember.getDeviceToken())
                .setNotification(Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getBody())
                        .build())
                .build();
        return message;
    }

    /**
     * 다중 알림 메시지
     */
    private MulticastMessage createMessages(FCMNotificationRequestDto requestDto, Member findMember) {
        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(findMember.getConnectionMemberDeviceTokens())
                .setNotification(Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getBody())
                        .build())
                .build();
        return message;
    }

    /**
     * 연동된 계정들의 모든 DeviceToken 값이 존재하는지 검증
     */
    private boolean deviceTokenValid(Member findMember) {
        List<String> deviceTokens = findMember.getConnectionMemberDeviceTokens();
        for (String deviceToken : deviceTokens) {
            if (deviceToken.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void saveReceiveNotificationMessages(FCMNotificationRequestDto requestDto, Member findMember) {
        List<Long> connectionMembersId = findMember.getConnectionMembersId();
        for (Long connMemberId : connectionMembersId) {
            ReceiveMessage receiveMessage = requestDto.createReceiveNotificationMessage(connMemberId);
            receiveMessageRepository.save(receiveMessage);
        }
    }
}
