package org.egov.eventnotification.repository;

import org.egov.eventnotification.entity.NotificationDrafts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DraftRepository extends JpaRepository<NotificationDrafts, Long> {

}
