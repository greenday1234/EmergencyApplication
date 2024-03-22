package project.emergencyApplication.domain.member.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String phone;
    private boolean watch;
    private String connEmail;
}
