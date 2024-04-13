package project.emergencyApplication.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.emergencyApplication.fcm.entity.ReceiveMessage;

public interface ReceiveMessageRepository extends JpaRepository<ReceiveMessage, Long> {
}
