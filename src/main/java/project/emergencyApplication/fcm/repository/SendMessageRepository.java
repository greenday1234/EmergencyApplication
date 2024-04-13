package project.emergencyApplication.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.emergencyApplication.fcm.entity.SendMessage;

public interface SendMessageRepository extends JpaRepository<SendMessage, Long> {
}
