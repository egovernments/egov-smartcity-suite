package org.egov.eventnotification.repository;

import org.egov.eventnotification.entity.NotificationDrafts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftRepository  extends JpaRepository<NotificationDrafts, Long> {

}
