package org.egov.infra.admin.master.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig,Long>{
	
	@Query("select app from AppConfig app where app.keyName = :keyName and app.module.name = :name")
    public AppConfig findBykeyNameAndModuleName(@Param("keyName") String keyName, @Param("name") String name);

	
	AppConfig findBykeyName(String keyName);
	
	   @Query("select b from Module b where  b.enabled=true AND "
	   		+ "(b.parentModule IS NULL OR (b.parentModule IN (select c.id from Module c where c.parentModule IS NULL ))) "
	   		+ "AND  UPPER(b.name) like UPPER(:name) order by b.id")
	  List<Module> findByNameContainingIgnoreCase(@Param("name") String name);
	   
	   @Query("select b from Module b where  b.enabled=true AND "
		   		+ "(b.parentModule IS NULL OR (b.parentModule IN (select c.id from Module c where c.parentModule IS NULL ))) "
		   		+ " order by b.id")
		  List<Module> findAllModules();
	   
	   @Query("select b from Module b where  b.enabled=true AND b.id=(:id)")
	   Module findByModuleById(@Param("id")Long id);
}



