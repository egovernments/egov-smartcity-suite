package org.egov.infra.config.process.repository;

import java.util.List;

import org.egov.infra.config.process.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findByName(String name);

    List<Group> findByNameContains(String name);
}
