/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pgr.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.egov.commons.ObjectType;
import org.egov.commons.service.ObjectTypeService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.EmailUtils;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.notification.HTTPSMS;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.repository.EscalationRepository;
import org.egov.pgr.utils.constants.PGRConstants;
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
    private HTTPSMS httpSMS;

    @Autowired
    public EscalationService(final EscalationRepository escalationRepository) {

        this.escalationRepository = escalationRepository;
    }

    @Autowired
    private SecurityUtils securityUtils;
    
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
         Escalation escalation = escalationRepository.findByDesignationAndComplaintType(designationId, complaintTypeId);
         if(escalation != null)
        	 return escalation.getNoOfHrs();
         else
        	 return 0;
    }

    @Transactional
    public void escalateComplaint() {
        final AppConfigValues appConfigValue = appConfigValuesDAO.getConfigValuesByModuleAndKey(
                PGRConstants.MODULE_NAME, "SENDEMAILFORESCALATION").get(0);
        final Boolean isEmailNotificationSet = "YES".equalsIgnoreCase(appConfigValue.getValue());
        final ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);
        final List<Complaint> escalationComplaints = complaintService.getComplaintsEligibleForEscalation();

        for (final Complaint complaint : escalationComplaints) {
            final Position superiorPosition = eisCommonService
                    .getSuperiorPositionByObjectAndObjectSubTypeAndPositionFrom(objectType.getId(), complaint
                            .getComplaintType().getName(), complaint.getAssignee().getId());
            final User superiorUser = eisCommonService.getUserForPosition(superiorPosition.getId(), new Date());
            complaint.setEscalationDate(getExpiryDate(complaint));
            complaint.setAssignee(superiorPosition);
            complaint.transition().withOwner(superiorPosition).withComments("Complaint is escalated")
			.withDateInfo(complaint.getEscalationDate().toDate()).withStateValue(complaint.getStatus().getName())
			.withSenderName(securityUtils.getCurrentUser().getName());
            complaintRepository.save(complaint);
            if (isEmailNotificationSet) {
                final String formattedEscalationDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(complaint
                        .getEscalationDate().toDate());
                final StringBuffer emailBody = new StringBuffer().append("Dear ").append(superiorUser.getName())
                        .append(",\n \n     The complaint Number (").append(complaint.getCRN())
                        .append(") is escalated.\n").append("\n Complaint Details - \n \n Complaint type - ")
                        .append(complaint.getComplaintType().getName()).append(" \n Location details - ")
                        .append(complaint.getLocation().getName()).append("\n Complaint description - ")
                        .append(complaint.getDetails()).append("\n Complaint status -")
                        .append(complaint.getStatus().getName()).append("\n Complaint escalated to - ")
                        .append(superiorUser.getName()).append("\n Escalation Time - ").append(formattedEscalationDate);
                final StringBuffer emailSubject = new StringBuffer().append("Escalated Complaint Number -")
                        .append(complaint.getCRN()).append(" (").append(complaint.getStatus().getName()).append(")");
                final StringBuffer smsBody = new StringBuffer().append("Dear ").append(superiorUser.getName())
                        .append(", The complaint Number (").append(complaint.getCRN())
                        .append(") has been escalated to ").append(superiorUser.getName()).append(" on ")
                        .append(formattedEscalationDate);
                if (superiorUser != null && superiorUser.getEmailId() != null)
                    emailUtils.sendMail(superiorUser.getEmailId(), emailBody.toString(), emailSubject.toString());
                if (superiorUser != null && superiorUser.getMobileNumber() != null)
                	httpSMS.sendSMS(smsBody.toString(), "91" + superiorUser.getMobileNumber());
            }
        }
    }

    protected DateTime getExpiryDate(final Complaint complaint) {

        DateTime expiryDate = complaint.getEscalationDate();
        final DesignationMaster designation = eisCommonService.getEmployeeDesignation(complaint.getAssignee().getId());
        final Integer noOfhrs = getHrsToResolve(designation.getDesignationId(), complaint.getComplaintType().getId());
        expiryDate = expiryDate.plusHours(noOfhrs);
        return expiryDate;
    }
}
