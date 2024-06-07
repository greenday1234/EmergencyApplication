package project.emergencyApplication.domain.connection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ConnectionResponseDto {

    private String email;
    private String name;
    private String url;
}
