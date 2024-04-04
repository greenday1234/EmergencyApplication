package project.emergencyApplication.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.dto.MemberInfoResponseDto;
import project.emergencyApplication.domain.member.dto.MemberUpdateRequestDto;
import project.emergencyApplication.domain.member.entity.Member;
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

    /**
     * 내 정보 수정 (SettingView)
     */
    public MemberInfoResponseDto updateMemberInfo(MemberUpdateRequestDto memberUpdateRequestDto) {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(findMember -> {
                    // Transactional 이 걸려있으므로 save, update 를 해줄 필요 X
                    findMember.updateMember(memberUpdateRequestDto);

                    return MemberInfoResponseDto.createMemberInfoResponseDto(findMember);
                })
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }
}
