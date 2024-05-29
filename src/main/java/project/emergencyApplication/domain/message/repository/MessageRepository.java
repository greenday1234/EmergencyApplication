package project.emergencyApplication.domain.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.emergencyApplication.domain.message.entity.Messages;

public interface MessageRepository extends JpaRepository<Messages, Long> {
}
