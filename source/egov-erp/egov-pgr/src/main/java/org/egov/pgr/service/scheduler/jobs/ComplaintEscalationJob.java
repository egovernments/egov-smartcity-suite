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

import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infra.events.entity.Event;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.utils.constants.CommonConstants;
import org.egov.pims.commons.Position;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @author Vaibhav.K
 *
 */
@DisallowConcurrentExecution
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
    
    @Override
    public void executeJob() {
        final AppConfigValues appConfigValue = this.appConfigValuesDAO.getConfigValuesByModuleAndKey(CommonConstants.MODULE_NAME, "SENDEMAILFORESCALATION").get(0);
        final Boolean isEmailNotificationSet = "YES".equalsIgnoreCase(appConfigValue.getValue());
        final List<Complaint> escalationComplaints = this.complaintService.getComplaintsEligibleForEscalation();
        for (final Complaint complaints : escalationComplaints) {
            final Position superiorPosition = this.eisCommonService.getSuperiorPositionByObjectTypeAndPositionFrom(null,complaints.getAssignee().getId());
            final User superiorUser = this.eisCommonService.getUserForPositionId(superiorPosition.getId(), new Date());
        }   
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
