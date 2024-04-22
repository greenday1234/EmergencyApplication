package project.emergencyApplication.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.domain.member.entity.Member;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDto {

    private String name;
    private String profileUrl;
    private String email;
    private boolean hasWatch;
    private List<ConnectionMemberDto> connectionMemberDtoList;

    public MemberInfoResponseDto createMemberInfoResponseDto(Member member) {
        return MemberInfoResponseDto.builder()
                .name(member.getName())
                .profileUrl(member.getProfileUrl())
                .email(member.getEmail())
                .hasWatch(member.isHasWatch())
                .build();
    }

    public void addConnectionMemberDto(ConnectionMemberDto connectionMemberDto) {
        connectionMemberDtoList.add(connectionMemberDto);
    }

}
