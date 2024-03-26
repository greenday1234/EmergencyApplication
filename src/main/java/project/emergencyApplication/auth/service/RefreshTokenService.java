package project.emergencyApplication.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.emergencyApplication.auth.dto.Token;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.MemberRepository;

import java.util.Optional;

@Service
public class RefreshTokenService {

    private final Long validityRefreshTokenInMilliseconds;
    private final MemberRepository memberRepository;

    public RefreshTokenService(@Value("${security.jwt.token.refresh-key-expire-length}")
                               long validityRefreshTokenInMilliseconds,
                               MemberRepository memberRepository) {
        this.validityRefreshTokenInMilliseconds = validityRefreshTokenInMilliseconds;
        this.memberRepository = memberRepository;
    }

    public void saveTokenInfo(Long memberId, String refreshToken, String accessToken) {
        Token token = Token.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .expiration(validityRefreshTokenInMilliseconds) // 리프레시 토큰 유효기간
                .build();

        Optional<Member> findMember = memberRepository.findById(memberId);
        findMember.get().tokenUpdate(token);
    }
}
