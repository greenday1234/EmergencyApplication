package project.emergencyApplication.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.emergencyApplication.domain.member.entity.ConnectionMember;
import project.emergencyApplication.domain.member.entity.Member;

import java.util.List;

public interface ConnectionMemberRepository extends JpaRepository<ConnectionMember, Long> {

    List<ConnectionMember> findAllByMember(Member member);
}
