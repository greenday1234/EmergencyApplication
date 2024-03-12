package project.emergencyApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.emergencyApplication.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
