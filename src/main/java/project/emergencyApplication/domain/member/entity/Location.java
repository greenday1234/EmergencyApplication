package project.emergencyApplication.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
public class Location {

    private Double latitude;    // 위도
    private Double longitude;   // 경도

    public void setLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
