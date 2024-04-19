package project.emergencyApplication.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.domain.member.entity.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectionMemberDto {

    private String name;
    private String email;
    private byte image;

    public ConnectionMemberDto createConnectionMemberDto(Member member) {
        return ConnectionMemberDto.builder()
                .name(member.getName())
                .image(member.getImage())
                .email(member.getEmail())
                .build();
    }

}
