package project.emergencyApplication.member.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String phone;
    private boolean watch;
    private String connEmail;
}
