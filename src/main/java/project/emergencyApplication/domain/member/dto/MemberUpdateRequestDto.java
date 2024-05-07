package project.emergencyApplication.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequestDto {

    private String name;
    private String email;
    private boolean hasWatch;
}
