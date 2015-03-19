/**
 * 
 */
package org.egov.pgr.service;

import org.egov.pgr.entity.Escalation;
import org.egov.pgr.repository.EscalationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vaibhav.K
 *
 */
@Service
@Transactional(readOnly = true)
public class EscalationService {
    
    @Autowired
    public EscalationService(final EscalationRepository escalationRepository) {
       
        this.escalationRepository = escalationRepository;
    }

    private EscalationRepository escalationRepository;
    
    @Transactional
    public void create(final Escalation escalation) {
        escalationRepository.save(escalation);
    }
    
    @Transactional
    public void update(final Escalation escalation) {
        escalationRepository.save(escalation);
    }
    
    @Transactional
    public void delete(final Escalation escalation) {
        escalationRepository.delete(escalation);
    }
    
    public Integer getHrsToResolve(Integer designationId,Long complaintTypeId) {
        return escalationRepository.findByDesignationAndComplaintType(designationId, complaintTypeId).getNoOfHrs();
    }
    
}
