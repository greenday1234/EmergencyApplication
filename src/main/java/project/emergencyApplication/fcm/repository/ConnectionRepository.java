package project.emergencyApplication.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.emergencyApplication.fcm.entity.Connection;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
}
