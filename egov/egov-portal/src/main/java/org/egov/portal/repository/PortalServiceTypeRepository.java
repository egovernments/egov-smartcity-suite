package org.egov.portal.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.Module;
import org.egov.portal.entity.PortalServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PortalServiceTypeRepository
        extends JpaRepository<PortalServiceType, Long>, JpaSpecificationExecutor<PortalServiceType> {

    PortalServiceType findById(Long id);

    @Query("select distinct pst.module from PortalServiceType pst")
    List<Module> findAllModules();

    @Query("select distinct pst.name from PortalServiceType pst where pst.module.id=:moduleId order by pst.name asc")
    List<String> findAllServiceTypes(@Param("moduleId") Long moduleId);

}
