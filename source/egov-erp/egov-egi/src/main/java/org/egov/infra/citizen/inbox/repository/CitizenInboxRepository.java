package org.egov.infra.citizen.inbox.repository;

import java.util.List;

import org.egov.infra.citizen.inbox.entity.CitizenInbox;
import org.egov.infra.citizen.inbox.entity.enums.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenInboxRepository extends JpaRepository<CitizenInbox, Long> {

	@Query("select count(*) from CitizenInbox ci where ci.assignedToCitizen.id=:citizenUserId order by createdDate desc")
	Integer findUnreadMessagesCount(@Param("citizenUserId")Long citizenUserId);

	@Query(" from CitizenInbox ci where ci.assignedToCitizen.id=:citizenUserId order by createdDate desc")
	List<CitizenInbox> findAllInboxMessage(@Param("citizenUserId")Long citizenUserId);

	@Query(" from CitizenInbox ci where ci.messageType=:messageType and ci.assignedToCitizen.id=:citizenUserId order by createdDate desc")
	List<CitizenInbox> findAllInboxMessageByType(@Param("messageType")MessageType messageType, @Param("citizenUserId")Long citizenUserId);
}
