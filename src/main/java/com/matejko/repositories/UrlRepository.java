package com.matejko.repositories;

import com.matejko.model.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mikołaj Matejko on 20.09.2017 as part of item-statistics
 */
public interface UrlRepository extends JpaRepository<Url, Long>, UrlRepositoryCustom {
}
