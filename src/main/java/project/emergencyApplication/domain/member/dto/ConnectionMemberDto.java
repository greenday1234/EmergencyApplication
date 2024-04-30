package project.emergencyApplication.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.domain.member.entity.Location;
import project.emergencyApplication.domain.member.entity.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectionMemberDto {

    private String name;
    private String email;
    private String profileUrl;
    private Location location;
    private Boolean emgState;

    public ConnectionMemberDto createConnectionMemberDto(Member member) {
        return ConnectionMemberDto.builder()
                .name(member.getName())
                .profileUrl(member.getProfileUrl())
                .email(member.getEmail())
                .location(member.getLocation())
                .emgState(member.getEmgState())
                .build();
    }

}
