package project.emergencyApplication.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.platform.Platform;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m.memberId from Member m where m.platform = :platform and m.email = :email")
    Optional<Long> findByPlatformAndEmail(Platform platform, String email);
}
