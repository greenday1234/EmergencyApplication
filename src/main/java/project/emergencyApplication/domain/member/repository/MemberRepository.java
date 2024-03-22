package project.emergencyApplication.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.emergencyApplication.domain.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
