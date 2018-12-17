/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.pgr.service;

import org.apache.commons.lang.time.DateUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.pgr.elasticsearch.service.ComplaintIndexService;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.entity.EscalationHierarchy;
import org.egov.pgr.entity.contract.BulkEscalationRequest;
import org.egov.pgr.entity.contract.EscalationRequest;
import org.egov.pgr.entity.contract.EscalationDTO;
import org.egov.pgr.entity.contract.EscalationTimeSearchRequest;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.repository.EscalationRepository;
import org.egov.pgr.repository.specs.EscalationTimeSpec;
import org.egov.pgr.utils.constants.PGRConstants;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.egov.infra.utils.ApplicationConstant.SYSTEM_USERNAME;
import static org.egov.pgr.entity.enums.ComplaintStatus.FORWARDED;
import static org.egov.pgr.entity.enums.ComplaintStatus.PROCESSING;
import static org.egov.pgr.entity.enums.ComplaintStatus.REGISTERED;
import static org.egov.pgr.entity.enums.ComplaintStatus.REOPENED;

@Service
@Transactional(readOnly = true)
public class ComplaintEscalationService {
    private static final Logger LOG = LoggerFactory.getLogger(ComplaintEscalationService.class);
    private static final String COMPLAINT_STATUS_NAME = "complaintStatus.name";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EscalationRepository escalationRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private GrievanceConfigurationService grievanceConfigurationService;

    @Autowired
    private EscalationHierarchyService escalationHierarchyService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private ComplaintIndexService complaintIndexService;

    @Autowired
    private ComplaintNotificationService complaintNotificationService;

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private DesignationService designationService;

    @Transactional
    public void create(Escalation escalation) {
        escalationRepository.save(escalation);
    }

    @Transactional
    public void update(Escalation escalation) {
        escalationRepository.save(escalation);
    }

    @Transactional
    public void delete(Escalation escalation) {
        escalationRepository.delete(escalation);
    }

    public List<Escalation> getAllEscalationByComplaintTypeId(Long complaintTypeId) {
        return escalationRepository.findEscalationByComplaintTypeId(complaintTypeId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 1500)
    public void escalateComplaint() {
        boolean sendMessage = grievanceConfigurationService.sendMessageOnEscalation();
        getComplaintsEligibleForEscalation()
                .stream()
                .filter(Complaint::transitionInprogress)
                .forEach(complaint -> findNextOwnerAndEscalate(complaint, sendMessage));

    }

    private void findNextOwnerAndEscalate(Complaint complaint, boolean sendMessage) {
        EscalationHierarchy escalationHierarchy = escalationHierarchyService.getHierarchyByFromPositionAndGrievanceType(
                complaint.getAssignee().getId(), complaint.getComplaintType().getId());
        Position nextAssignee = null;
        User nextOwner = null;
        if (escalationHierarchy == null) {
            Set<User> users = userService.getUsersByRoleName(PGRConstants.GRO_ROLE_NAME);
            if (!users.isEmpty())
                nextOwner = users.iterator().next();
            if (nextOwner != null) {
                List<Position> assigneePositions = positionMasterService.getPositionsForEmployee(nextOwner.getId(), new Date());
                if (assigneePositions.isEmpty())
                    LOG.warn("Could not escalate, no position defined for Grievance Officer role");
                else
                    nextAssignee = assigneePositions.iterator().next();
            } else {
                LOG.warn("Could not escalate, no user defined for Grievance Officer role");
            }
        } else {
            nextAssignee = escalationHierarchy.getToPosition();
            List<Assignment> superiorAssignments = assignmentService.getAssignmentsForPosition(nextAssignee.getId(), new Date());
            nextOwner = superiorAssignments.isEmpty() ? null : superiorAssignments.get(0).getEmployee();
        }

        updateComplaintEscalation(complaint, nextAssignee, nextOwner, sendMessage);
    }

    private void updateComplaintEscalation(Complaint complaint, Position nextAssignee, User nextOwner, boolean sendMessage) {
        if (nextAssignee != null && nextOwner != null && !nextAssignee.equals(complaint.getAssignee())) {
            Position previousAssignee = complaint.getAssignee();
            complaint.setEscalationDate(getExpiryDate(complaint));
            complaint.setAssignee(nextAssignee);
            complaint.setCurrentOwner(nextOwner);
            complaint.transition().progress()
                    .withOwner(nextAssignee).withComments("Complaint is escalated")
                    .withDateInfo(new Date())
                    .withStateValue(complaint.getStatus().getName())
                    .withSenderName(userService.getUserByUsername(SYSTEM_USERNAME).getName());
            complaintRepository.saveAndFlush(complaint);
            complaintIndexService.updateComplaintEscalationIndexValues(complaint);
            if (sendMessage)
                complaintNotificationService.sendEscalationMessage(complaint, nextOwner, previousAssignee);

        }
    }

    public Date getExpiryDate(Complaint complaint) {
        Designation designation = complaint.getAssignee().getDeptDesig().getDesignation();
        Integer resolutionSLAHrs = getResolutionSLAHrs(designation.getId(), complaint.getComplaintType().getId());
        return DateUtils.addHours(new Date(), resolutionSLAHrs);
    }

    public Integer getResolutionSLAHrs(Long designationId, Long complaintTypeId) {
        Escalation escalation = escalationRepository.findByDesignationAndComplaintType(designationId, complaintTypeId);
        return escalation == null ? grievanceConfigurationService.getDefaultComplaintResolutionTime() : escalation.getNoOfHrs();
    }

    @Transactional
    public void deleteAllInBatch(List<Escalation> entities) {
        escalationRepository.deleteInBatch(entities);

    }

    public Page<Escalation> getEscalationsTime(EscalationTimeSearchRequest escalationTimeSearchRequest) {
        Pageable pageable = new PageRequest(escalationTimeSearchRequest.pageNumber(), escalationTimeSearchRequest.pageSize()
                , escalationTimeSearchRequest.orderDir(), escalationTimeSearchRequest.orderBy());
        return escalationRepository.findAll(EscalationTimeSpec.search(escalationTimeSearchRequest), pageable);
    }

    public List<EscalationHierarchy> getEscalationObjByComplaintTypeFromPosition(List<ComplaintType> complaintTypes,
                                                                                 Position fromPosition) {
        return escalationHierarchyService.getHeirarchiesByFromPositionAndGrievanceTypes(complaintTypes, fromPosition);
    }

    public EscalationHierarchy getExistingEscalation(EscalationHierarchy escalationHierarchy) {
        EscalationHierarchy existingPosHierarchy = null;
        if (escalationHierarchy.getGrievanceType() != null && escalationHierarchy.getFromPosition() != null
                && escalationHierarchy.getToPosition() != null)
            existingPosHierarchy = escalationHierarchyService.getHierarchyByFromPositionAndGrievanceType(
                    escalationHierarchy.getFromPosition().getId(), escalationHierarchy.getGrievanceType().getId());

        return existingPosHierarchy;
    }

    public Escalation getEscalationByComplaintTypeAndDesignation(Long complaintTypeId, Long designationId) {
        return escalationRepository.findByDesignationAndComplaintType(designationId, complaintTypeId);
    }

    public List<Complaint> getComplaintsEligibleForEscalation() {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Complaint.class, "complaint")
                .createAlias("complaint.status", "complaintStatus");

        criteria.add(Restrictions.disjunction()
                .add(Restrictions.eq(COMPLAINT_STATUS_NAME, REOPENED.name()))
                .add(Restrictions.eq(COMPLAINT_STATUS_NAME, FORWARDED.name()))
                .add(Restrictions.eq(COMPLAINT_STATUS_NAME, PROCESSING.name()))
                .add(Restrictions.eq(COMPLAINT_STATUS_NAME, REGISTERED.name())))
                .add(Restrictions.lt("complaint.escalationDate", new Date()))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    @Transactional
    public void updateEscalationTime(EscalationRequest escalationRequest) {
        ComplaintType complaintType = null;
        if (escalationRequest.getComplaintType() != null && escalationRequest.getComplaintType().getId() != null) {
            complaintType = complaintTypeService.findBy(escalationRequest.getComplaintType().getId());
            List<Escalation> escalationList = getAllEscalationByComplaintTypeId(escalationRequest.getComplaintType().getId());
            if (!escalationList.isEmpty())
                deleteAllInBatch(escalationList);
        }
        if (complaintType != null && escalationRequest.getEscalationList() != null && !escalationRequest.getEscalationList().isEmpty())
            for (Escalation escalation : escalationRequest.getEscalationList()) {
                if (escalation.getDesignation() != null) {
                    escalation.setComplaintType(complaintType);
                    escalation.setDesignation(designationService.getDesignationById(escalation.getDesignation().getId()));
                    escalation.setNoOfHrs(escalation.getNoOfHrs());
                    create(escalation);
                }
            }
    }

    public List<EscalationDTO> viewEscalation(Long positionId, Long complaintId) {
        List<ComplaintType> activeComplaintTypes = complaintTypeService.findActiveComplaintTypes();
        List<EscalationHierarchy> escalationHierarchies = escalationHierarchyService
                .getHeirarchiesByFromPositionAndGrievanceType(positionId, complaintId);
        escalationHierarchies.stream()
                .filter(escalationHierarchy -> activeComplaintTypes.contains(escalationHierarchy.getGrievanceType()))
                .collect(Collectors.toList());
        return getEscalationDetailByEscalationHierarchy(escalationHierarchies);
    }

    public List<EscalationDTO> getEscalationDetailByEscalationHierarchy(List<EscalationHierarchy> escalationHierarchies) {
        List<EscalationDTO> escalationDTOS = new ArrayList<>();
        for (EscalationHierarchy posHir : escalationHierarchies) {
            EscalationDTO escalationDTO = new EscalationDTO();
            if (posHir.getGrievanceType() != null)
                escalationDTO.setComplaintType(posHir.getGrievanceType());
            escalationDTO.setFromPosition(posHir.getFromPosition());
            escalationDTO.setToPosition(posHir.getToPosition());
            escalationDTOS.add(escalationDTO);
        }
        return escalationDTOS;
    }

    @Transactional
    public void updateBulkEscalation(BulkEscalationRequest bulkEscalationRequest) {
        for (ComplaintType complaintType : bulkEscalationRequest.getComplaintTypes()) {
            EscalationHierarchy escalationHierarchy = new EscalationHierarchy();
            escalationHierarchy.setGrievanceType(complaintType);
            escalationHierarchy.setFromPosition(bulkEscalationRequest.getFromPosition());
            escalationHierarchy.setToPosition(bulkEscalationRequest.getToPosition());
            EscalationHierarchy existingPosHierarchy = getExistingEscalation(escalationHierarchy);
            if (existingPosHierarchy == null) {
                escalationHierarchyService.save(escalationHierarchy);
            } else {
                existingPosHierarchy.setToPosition(bulkEscalationRequest.getToPosition());
                escalationHierarchyService.save(existingPosHierarchy);
            }

        }
    }

    @Transactional
    public void updateEscalation(Long id, EscalationRequest escalationRequest) {

        List<EscalationHierarchy> existingPosHierarchy = escalationHierarchyService
                .getHeirarchyByFromPosition(id);
        if (!existingPosHierarchy.isEmpty())
            escalationHierarchyService.deleteAllInBatch(existingPosHierarchy);

        for (EscalationHierarchy escalationHierarchy : escalationRequest.getEscalationHierarchyList())
            if (escalationHierarchy.getFromPosition() != null && escalationHierarchy.getToPosition() != null) {
                escalationHierarchy.setFromPosition(positionMasterService.getPositionById(escalationHierarchy.getFromPosition().getId()));
                escalationHierarchy.setToPosition(positionMasterService.getPositionById(escalationHierarchy.getToPosition().getId()));
                escalationHierarchy.setGrievanceType(escalationHierarchy.getGrievanceType());
                escalationHierarchyService.save(escalationHierarchy);
            }
    }
}