package org.egov.ptis.domain.repository.writeOff;

import java.util.List;

import org.egov.ptis.domain.entity.property.WriteOffReasons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriteOffReasonRepository extends JpaRepository<WriteOffReasons, Long> {

    public WriteOffReasons findByCode(String code);

    List<WriteOffReasons> findByTypeOrderByIdAsc(String type);
}
