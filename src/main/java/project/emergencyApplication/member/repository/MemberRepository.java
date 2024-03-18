package project.emergencyApplication.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.emergencyApplication.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
