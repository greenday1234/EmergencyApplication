package project.emergencyApplication.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.emergencyApplication.fcm.entity.Connection;

import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    @Query("select c.connectionId from Connection c where c.sendConnectionId = :sendConnectionId and c.receiveConnectionId = :receiveConnectionId")
    Optional<Connection> findBySendConnectionIdAndReceiveConnectionId(Long sendConnectionId, Long receiveConnectionId);
}
