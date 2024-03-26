package project.emergencyApplication.domain.platform;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.emergencyApplication.auth.exception.InvalidPlatformException;

import java.util.Arrays;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum Platform {

    APPLE("apple");

    private String email;

    public static Platform form(String email) {
        return Arrays.stream(values())
                .filter(it -> Objects.equals(it.email, email))
                .findFirst()
                .orElseThrow(InvalidPlatformException::new);
    }
}
