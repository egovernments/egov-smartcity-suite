package org.egov.pushbox.application.repository;

import org.egov.pushbox.application.entity.ScheduleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleLogRepository extends JpaRepository<ScheduleLog, Long> {

}
