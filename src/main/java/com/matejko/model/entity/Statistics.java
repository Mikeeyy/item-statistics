package com.matejko.model.entity;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by Mikołaj Matejko on 07.09.2017 as part of item-statistics
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = "url")
@ToString(callSuper = true, exclude = "url")
public class Statistics extends BaseEntity {
  @Column(nullable = false)
  private LocalDate executionDate;

  @Column(nullable = false)
  private Long quantity;

  private Double averagePrice;
  private Double medianPrice;
  private Double lowestPrice;
  private Double highestPrice;

  @ManyToOne(cascade = CascadeType.ALL)
  private Url url;
}
