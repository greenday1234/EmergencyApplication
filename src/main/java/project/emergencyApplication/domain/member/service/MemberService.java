package project.emergencyApplication.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.auth.jwt.JwtTokenProvider;
import project.emergencyApplication.domain.member.dto.MemberInfoResponseDto;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberInfoResponseDto memberInfo(String bearerToken) {

        // Filter 에서 검증됐으므로 바로 accessToken 만 가져오기
        String accessToken = bearerToken.substring(7);

        // accessToken 에서 데이터 추출을 하기 위해 authentication 변환
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // ID 값으로 회원 조회
        Member findMember = memberRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        // 조회한 회원 정보를 기반으로 Dto 생성
        MemberInfoResponseDto memberInfoResponseDto = MemberInfoResponseDto.builder()
                .name(findMember.getName())
                .image(findMember.getImage())
                .email(findMember.getEmail())
                .connectionMemberList(findMember.getConnectionMembers())
                .build();

        return memberInfoResponseDto;
    }
}
