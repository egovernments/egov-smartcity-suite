package org.egov.ptis.domain.repository.writeOff;

import java.util.List;

import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.WriteOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WriteOffRepository extends JpaRepository<WriteOff, Long>, JpaSpecificationExecutor<WriteOff> {

    @Query("select wo from WriteOff wo where wo.basicProperty.upicNo=:upicNo order by wo.id desc ")
    List<WriteOff> findAllSpecialNoticesGeneratedForUpicNo(@Param("upicNo") String name);

    @Query("select DT from DocumentType DT where DT.name=:name")
    public DocumentType findDocumentTypeByName(@Param("name") String name);
    
    @Query("select wo from WriteOff wo where wo.applicationNumber=:appNo")
    WriteOff getWriteOffByApplicationNo(@Param("appNo") String name);
}
