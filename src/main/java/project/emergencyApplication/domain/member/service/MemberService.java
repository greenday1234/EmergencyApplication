package project.emergencyApplication.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.dto.ConnectionMemberDto;
import project.emergencyApplication.domain.member.dto.MemberInfoResponseDto;
import project.emergencyApplication.domain.member.dto.MemberUpdateRequestDto;
import project.emergencyApplication.domain.member.entity.ConnectionMember;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.ConnectionMemberRepository;
import project.emergencyApplication.domain.member.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final ConnectionMemberRepository connectionMemberRepository;

    /**
     * 내 정보 조회 (SettingView)
     */
    public MemberInfoResponseDto memberInfo(Long memberId) {
        Member findMember = findMember(memberId);
        return getMemberInfoResponseDto(findMember);
    }

    /**
     * 내 정보 수정 (SettingView)
     */
    public MemberInfoResponseDto updateMemberInfo(MemberUpdateRequestDto memberUpdateRequestDto) {
        Member findMember = findMember(SecurityUtil.getCurrentMemberId());
        findMember.updateMember(memberUpdateRequestDto);
        return getMemberInfoResponseDto(findMember);
    }

    private MemberInfoResponseDto getMemberInfoResponseDto(Member findMember) {
        List<ConnectionMember> connectionMembers = connectionMemberRepository.findAllByMember(findMember);
        MemberInfoResponseDto responseDto = new MemberInfoResponseDto().createMemberInfoResponseDto(findMember);
        for (ConnectionMember connectionMember : connectionMembers) {
            responseDto.addConnectionMemberDto(new ConnectionMemberDto().
                    createConnectionMemberDto(connectionMember.getConnectionMember()));
        }
        return responseDto;
    }

    public Member findMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 회원입니다!"));
    }
}
