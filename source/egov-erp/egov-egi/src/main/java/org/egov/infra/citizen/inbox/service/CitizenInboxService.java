package org.egov.infra.citizen.inbox.service;

import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.citizen.inbox.entity.CitizenInbox;
import org.egov.infra.citizen.inbox.entity.enums.MessageType;
import org.egov.infra.citizen.inbox.repository.CitizenInboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for the CitizenInbox
 * 
 * @author rishi
 *
 */
@Service
@Transactional(readOnly = true)
public class CitizenInboxService {

	private CitizenInboxRepository citizenInboxRepository;

	@Autowired
	public CitizenInboxService(CitizenInboxRepository citizenInboxRepository) {
		this.citizenInboxRepository = citizenInboxRepository;
	}

	@Transactional
	public void pushMessage(CitizenInbox citizenInbox) {
		citizenInboxRepository.save(citizenInbox);
	}

	public Integer findUnreadMessagesCount(User citizenUser) {
		return citizenInboxRepository.findUnreadMessagesCount(citizenUser.getId());
	}

	public List<CitizenInbox> findAllInboxMessage(User citizenUser) {
		return citizenInboxRepository.findAllInboxMessage(citizenUser.getId());
	}

	public List<CitizenInbox> findAllUserMessages(User citizenUser) {
		return citizenInboxRepository.findAllInboxMessageByType(MessageType.USER_MESSAGE, citizenUser.getId());
	}

	public List<CitizenInbox> findAllSystemMessages(User citizenUser) {
		return citizenInboxRepository.findAllInboxMessageByType(MessageType.SYSTEM_MESSAGE, citizenUser.getId());
	}

	@Transactional
	public void updateMessage(CitizenInbox citizenInbox) {
		citizenInboxRepository.save(citizenInbox);
	}

	public CitizenInbox getInboxMessageById(Long id) {
		return citizenInboxRepository.findOne(id);
	}

}
