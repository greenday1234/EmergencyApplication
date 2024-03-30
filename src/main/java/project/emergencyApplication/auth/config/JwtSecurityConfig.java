package project.emergencyApplication.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.emergencyApplication.auth.jwt.JwtFilter;
import project.emergencyApplication.auth.jwt.JwtTokenProvider;

/**
 * 직접 만든 JwtTokenProvider 과 JwtFilter 를 SecurityConfig 에 적용할 때 사용
 */
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider tokenProvider;

    // JwtTokenProvider 를 주입받아 JwtFiler 를 통해 Security 로직에 필터 등록
    @Override
    public void configure(HttpSecurity http) {

        http.addFilterBefore(
                new JwtFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter.class
        );
    }
}
