package project.emergencyApplication.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

    @Id
    @Column(name = "rt_key")
    private Long key; //memberId

    @Column(name = "rt_value")
    private String value;   //Refresh Token

    @Builder
    public RefreshToken(Long key, String value) {
        this.key = key;
        this.value = value;
    }

    public void updateValue(String token) {
        this.value = token;
    }
}
