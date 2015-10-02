package org.egov.tl.domain.repository;


import org.egov.tl.domain.entity.LicenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface LicenseCategoryRepository extends JpaRepository<LicenseCategory,Long> {

LicenseCategory findByName(String name);

}