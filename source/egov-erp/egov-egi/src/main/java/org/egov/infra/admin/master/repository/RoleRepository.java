package org.egov.infra.admin.master.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    public List<Role> findByNameContainingIgnoreCase(String name);

}
