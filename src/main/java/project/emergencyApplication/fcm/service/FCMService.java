package project.emergencyApplication.fcm.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.entity.ConnectionMember;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.MemberRepository;
import project.emergencyApplication.fcm.dto.FCMConnectionNotificationRequestDto;
import project.emergencyApplication.fcm.dto.FCMNotificationRequestDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;

    public String multipleSendNotificationByToken(FCMNotificationRequestDto requestDto) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        if (findMember.getDeviceToken() != null) {
            MulticastMessage message = getMessages(requestDto, findMember);

            try {
                BatchResponse response = firebaseMessaging.sendMulticast(message);
                notificationValid(findMember, response);
                return "알림을 성공적으로 전송했습니다.";
            } catch (FirebaseMessagingException e) {
                log.error(e.getMessage());
                return "알림 보내기를 실패했습니다.";
            }
        } else {
            return "서버에 저장된 해당 유저의 DeviceToken 이 존재하지 않습니다.";
        }
    }

/*
    public String sendConnectionNotification(FCMConnectionNotificationRequestDto requestDto) {

    }
*/

    /**
     * 알림 메시지 생성 메소드
     */
    private MulticastMessage getMessages(FCMNotificationRequestDto requestDto, Member findMember) {
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
     * 알림 전송 검증
     */
    private void notificationValid(Member findMember, BatchResponse response) {
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    failedTokens.add(String.valueOf(findMember.getConnectionMembers().get(i)));
                }
            }
            if (!failedTokens.isEmpty()) {
                log.info("알림 전송에 실패한 기기: " + failedTokens);
            }
        }
    }
}
