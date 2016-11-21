/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.egov.commons.ObjectType;
import org.egov.commons.service.ObjectTypeService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.PositionHierarchy;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionHierarchyService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pgr.config.properties.PgrApplicationProperties;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.repository.EscalationRepository;
import org.egov.pgr.service.es.ComplaintIndexService;
import org.egov.pgr.utils.constants.PGRConstants;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EscalationService {
    private static final Logger LOG = LoggerFactory.getLogger(EscalationService.class);
    private final EscalationRepository escalationRepository;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ObjectTypeService objectTypeService;

    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private MessagingService messagingService;

    @Autowired
    private PgrApplicationProperties pgrApplicationProperties;

    @Autowired
    private PositionHierarchyService positionHierarchyService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PositionMasterService positionMasterService;
    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ComplaintIndexService complaintIndexService;

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

    public List<Escalation> findAllBycomplaintTypeId(final Long complaintTypeId) {
        return escalationRepository.findEscalationByComplaintTypeId(complaintTypeId);
    }

    @Transactional
    public void escalateComplaint() {
        try {
            final ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);
            if (objectType == null) {
                LOG.error("Escalation can't be done, Object Type {} not found", PGRConstants.EG_OBJECT_TYPE_COMPLAINT);
                return;
            }
            final List<Complaint> escalationComplaints = complaintService.getComplaintsEligibleForEscalation();
            for (final Complaint complaint : escalationComplaints) {
                final PositionHierarchy positionHierarchy = positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(
                        complaint.getAssignee().getId(),
                        objectType.getId(), complaint.getComplaintType().getCode());

                Position superiorPosition = null;
                User superiorUser = null;
                if (positionHierarchy != null) {
                    superiorPosition = positionHierarchy.getToPosition();
                    final List<Assignment> superiorAssignments = assignmentService
                            .getAssignmentsForPosition(superiorPosition.getId(), new Date());
                    superiorUser = !superiorAssignments.isEmpty() ? superiorAssignments.get(0).getEmployee() : null;
                } else {
                    final Set<User> users = userService.getUsersByRoleName(PGRConstants.GRO_ROLE_NAME);
                    if (!users.isEmpty())
                        superiorUser = users.iterator().next();
                    if (superiorUser != null) {
                        List<Position> positionList = positionMasterService.getPositionsForEmployee(superiorUser.getId(), new Date());
                        if (!positionList.isEmpty())
                            superiorPosition = positionList.iterator().next();
                        else {
                            LOG.error("Could not escalate, no position defined for Grievance Officer role");
                            continue;
                        }
                    } else {
                        LOG.error("Could not escalate, no user defined for Grievance Officer role");
                        continue;
                    }
                }
                // && condition is to avoid escalation if a user does not have superior position.
                if (superiorPosition != null && superiorUser != null && !superiorPosition.equals(complaint.getAssignee()))
                    updateOnEscalation(complaint, superiorPosition, superiorUser);
            }
        } catch (final Exception e) {
            // Ignoring and logging exception since exception will cause multi city scheduler to fail for all remaining cities.
            LOG.error("An error occurred, escalation can't be completed ", e);
            return;
        }
    }

    public void updateOnEscalation(final Complaint complaint, final Position superiorPosition,
                                   final User superiorUser) {
        final Position previousOwner = complaint.getAssignee();
        final List<Assignment> prevUserAssignments = assignmentService
                .getAssignmentsForPosition(previousOwner.getId(), new Date());
        final User previoususer = !prevUserAssignments.isEmpty() ? prevUserAssignments.get(0).getEmployee() : null;
        complaint.setEscalationDate(getExpiryDate(complaint));
        complaint.setAssignee(superiorPosition);
        complaint.transition().withOwner(superiorPosition).withComments("Complaint is escalated")
                .withDateInfo(new Date())
                .withStateValue(complaint.getStatus().getName())
                .withSenderName(securityUtils.getCurrentUser().getName());
        complaintRepository.save(complaint);
        final AppConfigValues appConfigValue = appConfigValuesService
                .getConfigValuesByModuleAndKey(PGRConstants.MODULE_NAME, "SENDEMAILFORESCALATION").get(0);
        if ("YES".equalsIgnoreCase(appConfigValue.getValue())) {
            final String formattedEscalationDate = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(complaint.getEscalationDate());
            final StringBuffer emailBody = new StringBuffer().append("Dear ").append(superiorUser.getName())
                    .append(",\n \n     The complaint Number (").append(complaint.getCrn())
                    .append(") is escalated.\n").append("\n Complaint Details - \n \n Complaint type - ")
                    .append(complaint.getComplaintType().getName()).append(" \n Location details - ")
                    .append(complaint.getLocation().getName()).append("\n Complaint description - ")
                    .append(complaint.getDetails()).append("\n Complaint status -")
                    .append(complaint.getStatus().getName()).append("\n Complaint escalated to - ")
                    .append(superiorUser.getName()).append("\n Escalation Time - ")
                    .append(formattedEscalationDate);
            final StringBuffer emailSubject = new StringBuffer().append("Escalated Complaint Number -")
                    .append(complaint.getCrn()).append(" (").append(complaint.getStatus().getName()).append(")");
            final StringBuffer smsBody = new StringBuffer().append("Dear ").append(superiorUser.getName())
                    .append(", ").append(complaint.getCrn() + " by ")
                    .append(complaint.getComplainant().getName() != null ? complaint.getComplainant().getName()
                            : "Anonymous User")
                    .append(", " + complaint.getComplainant().getMobile())
                    .append(" for " + complaint.getComplaintType().getName() + " from ")
                    .append(complaint.getLocation().getName()).append(" handled by ")
                    .append(previoususer != null ? previoususer.getName()
                            : previousOwner.getName() + " has been escalated to you. ");
            messagingService.sendEmail(superiorUser.getEmailId(), emailSubject.toString(), emailBody.toString());
            messagingService.sendSMS(superiorUser.getMobileNumber(), smsBody.toString());
        }
        // update complaint index values
        complaintIndexService.updateComplaintEscalationIndexValues(complaint);
    }

    protected Date getExpiryDate(final Complaint complaint) {
        Date expiryDate = complaint.getEscalationDate();
        final Designation designation = complaint.getAssignee().getDeptDesig().getDesignation();
        final Integer noOfhrs = getHrsToResolve(designation.getId(), complaint.getComplaintType().getId());

        expiryDate = DateUtils.addHours(expiryDate, noOfhrs);
        return expiryDate;
    }

    public Integer getHrsToResolve(final Long designationId, final Long complaintTypeId) {
        final Escalation escalation = escalationRepository.findByDesignationAndComplaintType(designationId,
                complaintTypeId);
        if (escalation != null)
            return escalation.getNoOfHrs();
        else
            return pgrApplicationProperties.defaultResolutionTime();
    }

    @Transactional
    public void deleteAllInBatch(final List<Escalation> entities) {
        escalationRepository.deleteInBatch(entities);

    }

    public Page<Escalation> getPageOfEscalations(final Integer pageNumber, final Integer pageSize,
                                                 final Long complaintTypeId, final Long designationId) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "id");
        if (complaintTypeId != 0 && designationId != 0)
            return escalationRepository.findEscalationBycomplaintTypeAndDesignation(complaintTypeId, designationId,
                    pageable);
        else if (complaintTypeId != 0)
            return escalationRepository.findEscalationBycomplaintType(complaintTypeId, pageable);
        else if (designationId != 0)
            return escalationRepository.findEscalationByDesignation(designationId, pageable);
        else
            return escalationRepository.findEscalationByAll(pageable);

    }

    public List<PositionHierarchy> getEscalationObjByComplaintTypeFromPosition(final List<ComplaintType> complaintTypes,
                                                                               final Position fromPosition) {
        final List<String> compTypeCodes = new ArrayList<String>();
        for (final ComplaintType complaintType : complaintTypes)
            compTypeCodes.add(complaintType.getCode());
        final ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);
        return positionHierarchyService.getListOfPositionHeirarchyByPositionObjectTypeSubType(objectType.getId(), compTypeCodes,
                fromPosition);
    }

    public PositionHierarchy getExistingEscalation(final PositionHierarchy positionHierarchy) {
        PositionHierarchy existingPosHierarchy = null;
        if (null != positionHierarchy.getObjectSubType() && null != positionHierarchy.getFromPosition()
                && null != positionHierarchy.getToPosition())
            existingPosHierarchy = positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(
                    positionHierarchy.getFromPosition().getId(), positionHierarchy.getObjectType().getId(),
                    positionHierarchy.getObjectSubType());

        return existingPosHierarchy != null ? existingPosHierarchy : null;
    }

    public Escalation getEscalationBycomplaintTypeAndDesignation(final Long complaintTypeId, final Long designationId) {
        return escalationRepository.findByDesignationAndComplaintType(designationId, complaintTypeId);
    }
}