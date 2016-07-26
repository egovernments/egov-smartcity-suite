package org.egov.assets.repository;


import org.egov.assets.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository 
public interface AssetRepository extends JpaRepository<Asset,java.lang.Long> {

	Asset findByCode(String code);
	Asset findByName(String name);

}