package project.emergencyApplication.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.emergencyApplication.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndProvider(String email, String provider);
}
