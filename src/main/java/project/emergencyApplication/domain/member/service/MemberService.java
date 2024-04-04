package project.emergencyApplication.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.domain.member.dto.MemberInfoResponseDto;
import project.emergencyApplication.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 내 정보 조회 (SettingView)
     */
    public MemberInfoResponseDto memberInfo(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberInfoResponseDto::createMemberInfoResponseDto)
                        .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }
}
