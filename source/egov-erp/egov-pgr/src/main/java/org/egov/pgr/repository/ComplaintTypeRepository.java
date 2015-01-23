package org.egov.pgr.repository;

import org.egov.pgr.entity.ComplaintType;

import java.util.List;

//@Repository
public interface ComplaintTypeRepository {

    List<ComplaintType> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    List<ComplaintType> findByDepartmentId(Long id);
}
