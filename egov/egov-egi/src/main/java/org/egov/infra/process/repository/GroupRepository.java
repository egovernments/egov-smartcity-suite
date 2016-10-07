package org.egov.infra.process.repository;

import org.egov.infra.process.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findByName(String name);

    List<Group> findByNameContains(String name);
}
