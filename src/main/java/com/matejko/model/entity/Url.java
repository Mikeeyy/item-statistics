package com.matejko.model.entity;

import com.matejko.model.common.Status;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Mikołaj Matejko on 07.09.2017 as part of item-statistics
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = "statistics")
@ToString(callSuper = true, exclude = "statistics")
public class Url extends BaseEntity {
  @Lob
  private String url;

  private Boolean active;

  private String description;

  @Enumerated(EnumType.STRING)
  private Status status;

  @OneToMany(mappedBy = "url", cascade = CascadeType.ALL, orphanRemoval = true)
  @Getter(AccessLevel.NONE)
  private Set<Statistics> statistics;

  /**
   * setter
   *
   * @return set of statistics
   */
  public Set<Statistics> getStatistics() {
    if (this.statistics == null) {
      this.statistics = new HashSet<>();
    }

    return statistics;
  }
}
