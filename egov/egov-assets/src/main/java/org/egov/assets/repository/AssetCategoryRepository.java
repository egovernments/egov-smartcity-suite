package org.egov.assets.repository;


import org.egov.assets.model.AssetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository 
public interface AssetCategoryRepository extends JpaRepository<AssetCategory,java.lang.Long> {

	AssetCategory findByName(String name);

	AssetCategory findByCode(String code);
	 

}