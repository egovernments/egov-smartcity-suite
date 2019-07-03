package org.egov.ptis.domain.repository.writeOff;


import org.egov.ptis.domain.entity.property.WriteOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriteOffRepository extends JpaRepository<WriteOff, Long>{

}
