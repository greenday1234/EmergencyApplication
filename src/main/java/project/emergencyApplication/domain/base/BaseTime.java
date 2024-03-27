package project.emergencyApplication.domain.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class BaseTime {

    @CreationTimestamp
    @Column(name = "created_time")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "modified_time")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime modifiedTime;
}
