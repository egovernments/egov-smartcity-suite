/**
 *
 */
package org.egov.pgr.service;

import java.util.Date;
import java.util.List;

import org.egov.commons.ObjectType;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.commons.service.ObjectTypeService;
import org.egov.infra.utils.EmailUtils;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.notification.HTTPSMS;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.repository.EscalationRepository;
import org.egov.pgr.utils.constants.CommonConstants;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EscalationService {

    private final EscalationRepository escalationRepository;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private AppConfigValuesDAO appConfigValuesDAO;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private ObjectTypeService objectTypeService;

    @Autowired
    private ComplaintRepository complaintRepository;
    
    @Autowired
    private GenericHibernateDaoFactory genericHibernateDaoFactory;
    

    @Autowired
    public EscalationService(final EscalationRepository escalationRepository) {

        this.escalationRepository = escalationRepository;
    }

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

    public Integer getHrsToResolve(final Integer designationId, final Long complaintTypeId) {
        return escalationRepository.findByDesignationAndComplaintType(designationId, complaintTypeId).getNoOfHrs();
    }

    @Transactional
    public void escalateComplaint() {
        final AppConfigValues appConfigValue = appConfigValuesDAO.getConfigValuesByModuleAndKey(
                CommonConstants.MODULE_NAME, "SENDEMAILFORESCALATION").get(0);
        final Boolean isEmailNotificationSet = "YES".equalsIgnoreCase(appConfigValue.getValue());
        final ObjectType objectType = objectTypeService.getObjectTypeByName(CommonConstants.EG_OBJECT_TYPE_COMPLAINT);
        final List<Complaint> escalationComplaints = complaintService.getComplaintsEligibleForEscalation();

        for (final Complaint complaint : escalationComplaints) {
            final Position superiorPosition = eisCommonService
                    .getSuperiorPositionByObjectAndObjectSubTypeAndPositionFrom(objectType.getId(), complaint
                            .getComplaintType().getName(), complaint.getAssignee().getId());
            final User superiorUser = eisCommonService.getUserForPosition(superiorPosition.getId(), new Date());
            complaint.setEscalationDate(getExpiryDate(complaint));
            complaint.transition().withOwner(superiorPosition);
            complaintRepository.save(complaint);
            if (isEmailNotificationSet) {
                StringBuffer emailBody = new StringBuffer()
						.append("Dear ").append(superiorUser.getName()).append(",\n \n     The complaint Number (")
						.append(complaint.getCRN()).append("with complaint type :")
						.append(complaint.getComplaintType().getName()).append(" ,location details :")
						.append(complaint.getLocation().getName())
						.append(", complaint description :").append(complaint.getDetails())
						.append("and status :").append(complaint.getStatus())
						.append(") has been escalated to ")
						.append(superiorUser.getName()).append(" on ")
						.append(complaint.getEscalationDate());
				StringBuffer emailSubject = new StringBuffer()
						.append("Complaint Number -")
						.append(complaint.getCRN()).append(" (")
						.append(complaint.getStatus()).append(")");
				StringBuffer smsBody = new StringBuffer()
				.append("Dear ").append(superiorUser.getName()).append(", The complaint Number (")
				.append(complaint.getCRN())
				.append(") has been escalated to ")
				.append(superiorUser.getName()).append(" on ")
				.append(complaint.getEscalationDate());
				emailUtils.sendMail(superiorUser.getEmailId(),emailBody.toString(),emailSubject.toString());
                HTTPSMS.sendSMS(smsBody.toString(), "91" +superiorUser.getMobileNumber());
            }
        }
    }

    private DateTime getExpiryDate(final Complaint complaint) {

        DateTime expiryDate = complaint.getEscalationDate();
        final DesignationMaster designation = eisCommonService.getEmployeeDesignation(complaint.getAssignee().getId());
        final Integer noOfhrs = getHrsToResolve(designation.getDesignationId(), complaint.getComplaintType().getId());
        expiryDate = expiryDate.plusHours(noOfhrs);
        return expiryDate;
    }
}
