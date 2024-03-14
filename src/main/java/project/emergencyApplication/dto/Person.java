package project.emergencyApplication.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

// API 모델 및 연산 주석 예시
@ApiModel(description="Class representing a person tracked by the application.")
public class Person {

    @ApiModelProperty(notes="Unique identifier of the Person. No two persons can have the same id.", example="1", required=true, position=0)
    private Long id;

    @ApiModelProperty(notes="Name of the Person", example="Jinwoong Kim", required=true, position=1)
    private String name;

    // Getters and Setters
}
