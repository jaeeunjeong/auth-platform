package com.jejeong.authplatform.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class EntityDate {

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createAt;

  @LastModifiedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime modifiedAt;

}
