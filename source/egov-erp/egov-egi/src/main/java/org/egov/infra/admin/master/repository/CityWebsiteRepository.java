package org.egov.infra.admin.master.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.CityWebsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CityWebsiteRepository extends JpaRepository<CityWebsite, Long> {
    CityWebsite findByCityName(String cityName);
    List<CityWebsite> findByCityNameContainingIgnoreCase(String cityName);
    
    @Query("select c from CityWebsite c where cityBaseURL = :url")
    CityWebsite findByURL(@Param("url") String url);
}
