package org.egov.tl.domain.repository;


import org.egov.tl.domain.entity.LicenseAppType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface LicenseAppTypeRepository extends JpaRepository<LicenseAppType,Long> {

LicenseAppType findByName(String name);

}