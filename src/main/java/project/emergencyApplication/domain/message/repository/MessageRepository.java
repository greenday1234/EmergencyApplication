package project.emergencyApplication.domain.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.emergencyApplication.domain.message.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByReceiveMemberIdAndSendMemberId(Long receiveMemberId, Long sendMemberId);
}
