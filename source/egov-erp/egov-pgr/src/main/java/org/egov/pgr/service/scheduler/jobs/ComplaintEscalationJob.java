/**
 * 
 */
package org.egov.pgr.service.scheduler.jobs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.egov.commons.ObjectType;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.commons.service.ObjectTypeService;
import org.egov.infra.events.entity.Event;
import org.egov.infra.events.service.EventService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.EscalationService;
import org.egov.pgr.utils.constants.CommonConstants;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.joda.time.DateTime;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vaibhav.K
 *
 */
@Transactional
public class ComplaintEscalationJob extends AbstractQuartzJob {
    
    private static final long serialVersionUID = -5428952585539260293L;
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    @Autowired
    private AppConfigValuesDAO appConfigValuesDAO;
    
    @Autowired
    private ComplaintService complaintService;
    
    @Autowired
    private EisCommonService eisCommonService;
    
    @Autowired
    private ObjectTypeService objectTypeService;
    
    @Autowired
    private EscalationService escalationService;
    
    @Autowired
    private ComplaintRepository complaintRepository;
    
    @Override
    public void executeJob() {
        final AppConfigValues appConfigValue = this.appConfigValuesDAO.getConfigValuesByModuleAndKey(CommonConstants.MODULE_NAME, "SENDEMAILFORESCALATION").get(0);
        final Boolean isEmailNotificationSet = "YES".equalsIgnoreCase(appConfigValue.getValue());
        final ObjectType objectType = this.objectTypeService.getObjectTypeByName(CommonConstants.EG_OBJECT_TYPE_COMPLAINT);
        final List<Complaint> escalationComplaints = this.complaintService.getComplaintsEligibleForEscalation();
        
        for (final Complaint complaint : escalationComplaints) {
            final Position superiorPosition = this.eisCommonService.getSuperiorPositionByObjectAndObjectSubTypeAndPositionFrom(objectType.getId(),complaint.getComplaintType().getName(),complaint.getAssignee().getId());
            final User superiorUser = this.eisCommonService.getUserForPosition(superiorPosition.getId(), new Date());
            complaint.setEscalationDate(getExpiryDate(complaint));
            complaint.transition().withOwner(superiorPosition);
            complaintRepository.save(complaint);
            if (isEmailNotificationSet) {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("to_address", superiorUser.getEmailId());
                params.put("subject", "Complaint Escaltion");
                params.put("to_name", superiorUser.getName());
                final Event event = new Event(CommonConstants.MODULE_NAME, "Complaint Escalation", params);
                EventService.registerEvent(event);
                sendEmail();
            }
            System.out.println("Time is "+new Date());
      }
        
    }
    
    private void sendEmail() {
        if (EventService.isEventRegistered()) {
                final Event event = EventService.getRegisteredEvent();
                raiseEvent(event);
                EventService.removeRegisteredEvent();
        }
}
    
    private DateTime getExpiryDate(final Complaint complaint) {

        DateTime expiryDate = complaint.getEscalationDate();
        DesignationMaster designation = this.eisCommonService.getEmployeeDesignation(complaint.getAssignee().getId());
        Integer noOfhrs = this.escalationService.getHrsToResolve(designation.getDesignationId(), complaint.getComplaintType().getId());
        expiryDate = expiryDate.plusHours(noOfhrs);
        return expiryDate;
    }
    
    private void raiseEvent(final Event event) {
        try {
                final Map<String, String> params = new HashMap<String, String>();
                event.addParam("DOMAIN_NAME", EGOVThreadLocals.getDomainName());
                event.addParam("JNDI_NAME", EGOVThreadLocals.getJndiName());
                event.addParam("FACTORY_NAME", EGOVThreadLocals.getHibFactName());
                event.addParams(params);
                event.setDateRaised(new Date());

                final MessageCreator message = new MessageCreator() {
                        @Override
                        public Message createMessage(final Session session) throws JMSException {
                                final ObjectMessage objectMessage = session.createObjectMessage();
                                objectMessage.setObject(event);
                                return objectMessage;
                        }
                };
                this.jmsTemplate.send(message);

        } catch (final BeansException e) {
                throw new EGOVRuntimeException("Exception in raising Event:::::", e);
        } catch (final JmsException e) {
                throw new EGOVRuntimeException("Exception in raising Event:::::", e);
        }
}

}
