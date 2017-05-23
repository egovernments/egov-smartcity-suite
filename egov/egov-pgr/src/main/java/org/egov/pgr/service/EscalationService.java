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

import static org.egov.pgr.utils.constants.PGRConstants.EG_OBJECT_TYPE_COMPLAINT;
import static org.egov.pgr.utils.constants.PGRConstants.MODULE_NAME;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DateUtils;
import org.egov.commons.ObjectType;
import org.egov.commons.service.ObjectTypeService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.PositionHierarchy;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionHierarchyService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pgr.config.properties.PgrApplicationProperties;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.entity.dto.EscalationTimeSearchRequest;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.repository.EscalationRepository;
import org.egov.pgr.repository.specs.EscalationTimeSpec;
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
    private ComplaintCommunicationService complaintCommunicationService;

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

            final ObjectType objectType = objectTypeService.getObjectTypeByName(EG_OBJECT_TYPE_COMPLAINT);

            if (objectType == null) {
                LOG.warn("Escalation can't be done, Object Type {} not found", EG_OBJECT_TYPE_COMPLAINT);
                return;
            }

            final boolean sendMessage = "YES".equalsIgnoreCase(appConfigValuesService
                    .getConfigValuesByModuleAndKey(MODULE_NAME, "SENDEMAILFORESCALATION").get(0).getValue());

            complaintService.getComplaintsEligibleForEscalation()
                    .forEach(complaint -> findNextOwnerAndEscalate(complaint, objectType, sendMessage));

        } catch (final RuntimeException e) {
            // Ignoring and logging exception since exception will cause multi city scheduler to fail for all remaining cities.
            LOG.error("An error occurred, escalation can't be completed ", e);
        }
    }

    private void findNextOwnerAndEscalate(final Complaint complaint, final ObjectType objectType, final boolean sendMessage) {
        final PositionHierarchy positionHierarchy = positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(
                complaint.getAssignee().getId(), objectType.getId(), complaint.getComplaintType().getCode());
        Position nextAssignee = null;
        User nextOwner = null;
        if (positionHierarchy != null) {
            nextAssignee = positionHierarchy.getToPosition();
            final List<Assignment> superiorAssignments = assignmentService.getAssignmentsForPosition(nextAssignee.getId(),
                    new Date());
            nextOwner = !superiorAssignments.isEmpty() ? superiorAssignments.get(0).getEmployee() : null;
        } else {
            final Set<User> users = userService.getUsersByRoleName(PGRConstants.GRO_ROLE_NAME);
            if (!users.isEmpty())
                nextOwner = users.iterator().next();
            if (nextOwner != null) {
                final List<Position> positionList = positionMasterService.getPositionsForEmployee(nextOwner.getId(), new Date());
                if (!positionList.isEmpty())
                    nextAssignee = positionList.iterator().next();
                else
                    LOG.warn("Could not escalate, no position defined for Grievance Officer role");
            } else
                LOG.warn("Could not escalate, no user defined for Grievance Officer role");
        }

        updateComplaintEscalation(complaint, nextAssignee, nextOwner, sendMessage);
    }

    private void updateComplaintEscalation(final Complaint complaint, final Position nextAssignee, final User nextOwner,
            final boolean sendMessage) {
        if (nextAssignee != null && nextOwner != null && !nextAssignee.equals(complaint.getAssignee())) {
            final Position previousAssignee = complaint.getAssignee();
            complaint.setEscalationDate(getExpiryDate(complaint));
            complaint.setAssignee(nextAssignee);
            complaint.transition().progress()
                    .withOwner(nextAssignee).withComments("Complaint is escalated")
                    .withDateInfo(new Date())
                    .withStateValue(complaint.getStatus().getName())
                    .withSenderName(securityUtils.getCurrentUser().getName());
            complaintRepository.saveAndFlush(complaint);
            complaintIndexService.updateComplaintEscalationIndexValues(complaint);
            if (sendMessage)
                complaintCommunicationService.sendEscalationMessage(complaint, nextOwner, previousAssignee);

        }
    }

    public Date getExpiryDate(final Complaint complaint) {
        final Designation designation = complaint.getAssignee().getDeptDesig().getDesignation();
        final Integer noOfhrs = getHrsToResolve(designation.getId(), complaint.getComplaintType().getId());

        return DateUtils.addHours(new Date(), noOfhrs);
    }

    public Integer getHrsToResolve(final Long designationId, final Long complaintTypeId) {
        final Escalation escalation = escalationRepository.findByDesignationAndComplaintType(designationId, complaintTypeId);
        return escalation != null ? escalation.getNoOfHrs() : pgrApplicationProperties.defaultResolutionTime();
    }

    @Transactional
    public void deleteAllInBatch(final List<Escalation> entities) {
        escalationRepository.deleteInBatch(entities);

    }

    public Page<Escalation> getEscalationsTime(final EscalationTimeSearchRequest escalationTimeSearchRequest) {
        final Pageable pageable = new PageRequest(escalationTimeSearchRequest.pageNumber(),
                escalationTimeSearchRequest.pageSize(),
                escalationTimeSearchRequest.orderDir(), escalationTimeSearchRequest.orderBy());
        return escalationRepository.findAll(EscalationTimeSpec.search(escalationTimeSearchRequest),
                pageable);
    }

    public List<PositionHierarchy> getEscalationObjByComplaintTypeFromPosition(final List<ComplaintType> complaintTypes,
            final Position fromPosition) {
        final List<String> compTypeCodes = complaintTypes.parallelStream()
                .map(ComplaintType::getCode)
                .collect(Collectors.toList());
        final ObjectType objectType = objectTypeService.getObjectTypeByName(EG_OBJECT_TYPE_COMPLAINT);
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