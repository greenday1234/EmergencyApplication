package project.emergencyApplication.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.domain.member.entity.Location;
import project.emergencyApplication.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDto {

    private String name;
    private String profileUrl;
    private String email;
    private Boolean hasWatch;
    private Location location;
    private List<ConnectionMemberDto> connectionMemberDtoList;

    public MemberInfoResponseDto createMemberInfoResponseDto(Member member) {
        return MemberInfoResponseDto.builder()
                .name(member.getName())
                .profileUrl(member.getProfileUrl())
                .email(member.getEmail())
                .hasWatch(member.getHasWatch())
                .location(member.getLocation())
                .connectionMemberDtoList(new ArrayList<>()) // Builder 패턴에서 List 필드의 Default 값은 null
                .build();
    }

    public void addConnectionMemberDto(ConnectionMemberDto connectionMemberDto) {
        connectionMemberDtoList.add(connectionMemberDto);
    }

}
