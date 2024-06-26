package project.emergencyApplication.domain.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.MemberRepository;
import project.emergencyApplication.domain.message.dto.MessageRequestDto;
import project.emergencyApplication.domain.message.dto.MessageResponseDto;
import project.emergencyApplication.domain.message.entity.Message;
import project.emergencyApplication.domain.message.repository.MessageRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    /**
     * 받은 메시지 조회
     */
    @Transactional(readOnly = true)
    public List<MessageResponseDto> messageInfo(MessageRequestDto requestDto, Long memberId) {
        Member findSendMember = memberRepository.findByEmail(requestDto.getSenderEmail())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));

        List<Message> messages = messageRepository.findByReceiveMemberIdAndSendMemberId(
                memberId, findSendMember.getMemberId());

        if (messages.isEmpty()) {
            return null;
        }

        /** NOTE
         * messageDto 를 생성한 뒤 message 와 date 넣고 list 반환
         */

        for (Message message : messages) {
            message.getMessage();
        }



        return null;
    }
}
