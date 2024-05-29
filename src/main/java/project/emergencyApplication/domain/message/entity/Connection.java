package project.emergencyApplication.domain.message.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "connection")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "connection_id")
    private Long connectionId;

    @Column(name = "send_connection_id")
    private Long sendConnectionId;

    @Column(name = "receive_connection_id")
    private Long receiveConnectionId;

    @Column(name = "send_bool")
    private Boolean sendBool;

    @Column(name = "receive_bool")
    private Boolean receiveBool;

    public void updateReceiveBool(Boolean receiveBool) {
        this.receiveBool = receiveBool;
    }
}
