package org.egov.tl.domain.repository;


import org.egov.tl.domain.entity.LicenseSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface LicenseSubCategoryRepository extends JpaRepository<LicenseSubCategory,Long> {

LicenseSubCategory findByName(String name);

}