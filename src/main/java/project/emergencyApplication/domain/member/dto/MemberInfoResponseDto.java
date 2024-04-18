package project.emergencyApplication.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.domain.member.entity.ConnectionMember;
import project.emergencyApplication.domain.member.entity.Member;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDto {

    private String name;
    private byte image;
    private String email;
    private boolean hasWatch;
    private List<ConnectionMember> connectionMemberList;

    public static MemberInfoResponseDto createMemberInfoResponseDto(Member member) {
        return MemberInfoResponseDto.builder()
                .name(member.getName())
                .image(member.getImage())
                .email(member.getEmail())
                .hasWatch(member.isHasWatch())
                .connectionMemberList(member.getConnectionMembers())
                .build();
    }
}
