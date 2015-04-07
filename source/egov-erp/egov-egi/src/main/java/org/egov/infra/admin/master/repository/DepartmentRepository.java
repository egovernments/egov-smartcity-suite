package org.egov.infra.admin.master.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author subhash
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department findByName(String name);

    List<Department> findByNameContainingIgnoreCase(String name);
    
    Department findByCode(String code);
}
