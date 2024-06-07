package project.emergencyApplication.domain.connection.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.emergencyApplication.domain.connection.dto.ConnectionResponseDto;
import project.emergencyApplication.domain.connection.entity.Connection;
import project.emergencyApplication.domain.connection.repository.ConnectionRepository;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final MemberRepository memberRepository;

    public List<ConnectionResponseDto> connectionInfo(Long memberId) {
        List<Connection> connectionList = connectionRepository.findByReceiveConnectionId(memberId);

        if (connectionList.isEmpty()) {
            return null;
        }

        List<ConnectionResponseDto> connectionRequestDtoList = new ArrayList<>();

        for (Connection connection : connectionList) {
            Long sendMemberId = connection.getSendConnectionId();
            Member findMember = findMember(sendMemberId);

            connectionRequestDtoList.add(
                    new ConnectionResponseDto().builder()
                    .email(findMember.getEmail())
                    .name(findMember.getName())
                    .url(findMember.getProfileUrl())
                    .build());
        }

        return connectionRequestDtoList;
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));
    }
}
