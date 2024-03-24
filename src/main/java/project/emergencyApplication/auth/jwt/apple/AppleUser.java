package project.emergencyApplication.auth.jwt.apple;

import lombok.Getter;

@Getter
public class AppleUser {

    private String name;
    private String email;
    private String sub;

    public AppleUser(String name, String email, String sub) {
        this.name = name;
        this.email = email;
        this.sub = sub;
    }

}
