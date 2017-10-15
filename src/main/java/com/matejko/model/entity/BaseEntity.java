package com.matejko.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;

/**
 * Created by Mikołaj Matejko on 19.08.2017 as part of offer-lookup
 */
@MappedSuperclass
@Data
public abstract class BaseEntity {
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  @Column(nullable = false, unique = true)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime creationDate;

  @Column
  private LocalDateTime modificationDate;

  public void setModified() {
    this.modificationDate = LocalDateTime.now();
  }
}
