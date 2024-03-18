package project.emergencyApplication.swagger.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder()
                .group("spring")
                .packagesToScan("project.emergencyApplication.member.controller")
                //.pathsToMatch("/api/member/**") swagger 문서 그룹 나누기
                .build();
    }
}
