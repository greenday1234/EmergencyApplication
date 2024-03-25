package project.emergencyApplication.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.platform.Platform;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

//    Optional<Member> findByEmailAndPlatform(String email, Platform platform);
//
//    @Query("select m.id from Member m where m.platform = :platform and m.platformId = :platformId")
//    Optional<Long> findByPlatformAndPlatformId(Platform platform, String platformId);
}
