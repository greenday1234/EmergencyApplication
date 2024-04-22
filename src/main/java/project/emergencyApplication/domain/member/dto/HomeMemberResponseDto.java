package project.emergencyApplication.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.domain.member.entity.Location;
import project.emergencyApplication.domain.member.entity.Member;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeMemberResponseDto {

    private String name;
    private String profileUrl;
    private String email;
    private Location location;
    private List<HomeConnectionMemberResponseDto> connectionMembers;

    public HomeMemberResponseDto createHomeMemberResponseDto(Member member) {
        return HomeMemberResponseDto.builder()
                .name(member.getName())
                .profileUrl(member.getProfileUrl())
                .email(member.getEmail())
                .location(member.getLocation())
                .build();
    }

    public void addHomeConnectionMemberDto(HomeConnectionMemberResponseDto homeConnectionMemberResponseDto) {
        connectionMembers.add(homeConnectionMemberResponseDto);
    }
}
