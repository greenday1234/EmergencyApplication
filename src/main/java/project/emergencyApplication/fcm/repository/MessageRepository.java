package project.emergencyApplication.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.emergencyApplication.fcm.entity.Messages;

public interface MessageRepository extends JpaRepository<Messages, Long> {
}
