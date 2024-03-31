package project.emergencyApplication.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.entity.Platform;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m.memberId from Member m where m.platform = :platform and m.email = :email")
    Optional<Long> findByPlatformAndEmail(Platform platform, String email);
    Optional<Member> findByEmail(String email);

    // apple로만 로그인하기 때문에 중복 가입 방지 검증은 따로 진행하지 않음
}
