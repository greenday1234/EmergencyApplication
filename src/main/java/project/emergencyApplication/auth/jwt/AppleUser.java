package project.emergencyApplication.auth.jwt;

import lombok.Getter;

@Getter
public class AppleUser {

    private String name;
    private String email;

    public AppleUser(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
