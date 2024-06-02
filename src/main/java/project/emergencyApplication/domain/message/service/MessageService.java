package project.emergencyApplication.domain.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
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

    @Transactional(readOnly = true)
    public MessageResponseDto messageInfo(MessageRequestDto requestDto) {
        Member findSendMember = memberRepository.findByEmail(requestDto.getSenderEmail())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));

        List<Message> messages = messageRepository.findByReceiveMemberIdAndSendMemberId(
                SecurityUtil.getCurrentMemberId(), findSendMember.getMemberId());

        /**
         * 예외 처리 다시 짜야함!!
         */
        if (messages.isEmpty()) {
            throw new RuntimeException("해당 유저에게서 온 메시지가 없습니다.");
        }





        return new MessageResponseDto("hi");
    }
}
