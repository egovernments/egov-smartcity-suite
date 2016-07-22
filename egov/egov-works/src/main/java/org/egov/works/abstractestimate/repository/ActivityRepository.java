package org.egov.works.abstractestimate.repository;


import org.egov.works.abstractestimate.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository 
public interface ActivityRepository extends JpaRepository<Activity,java.lang.Long> {

}