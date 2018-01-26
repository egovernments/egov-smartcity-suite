/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
import org.egov.commons.ObjectType;
import org.egov.commons.service.ObjectTypeService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.PositionHierarchy;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionHierarchyService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.pgr.elasticsearch.service.ComplaintIndexService;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.entity.contract.BulkEscalationGenerator;
import org.egov.pgr.entity.contract.EscalationForm;
import org.egov.pgr.entity.contract.EscalationHelper;
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
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.egov.infra.utils.ApplicationConstant.SYSTEM_USERNAME;
import static org.egov.pgr.entity.enums.ComplaintStatus.FORWARDED;
import static org.egov.pgr.entity.enums.ComplaintStatus.PROCESSING;
import static org.egov.pgr.entity.enums.ComplaintStatus.REGISTERED;
import static org.egov.pgr.entity.enums.ComplaintStatus.REOPENED;
import static org.egov.pgr.utils.constants.PGRConstants.EG_OBJECT_TYPE_COMPLAINT;
import static org.egov.pgr.utils.constants.PGRConstants.MODULE_NAME;

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
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private ObjectTypeService objectTypeService;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private PositionHierarchyService positionHierarchyService;

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

    public List<Escalation> findAllBycomplaintTypeId(Long complaintTypeId) {
        return escalationRepository.findEscalationByComplaintTypeId(complaintTypeId);
    }

    @Transactional
    public void escalateComplaint() {
        try {

            ObjectType objectType = objectTypeService.getObjectTypeByName(EG_OBJECT_TYPE_COMPLAINT);

            if (objectType == null) {
                LOG.warn("Escalation can't be done, Object Type {} not found", EG_OBJECT_TYPE_COMPLAINT);
                return;
            }

            boolean sendMessage = "YES".equalsIgnoreCase(appConfigValuesService
                    .getConfigValuesByModuleAndKey(MODULE_NAME, "SENDEMAILFORESCALATION").get(0).getValue());

            getComplaintsEligibleForEscalation()
                    .forEach(complaint -> findNextOwnerAndEscalate(complaint, objectType, sendMessage));

        } catch (RuntimeException e) {
            // Ignoring and logging exception since exception will cause multi city scheduler to fail for all remaining cities.
            LOG.error("An error occurred, escalation can't be completed ", e);
        }
    }

    private void findNextOwnerAndEscalate(Complaint complaint, ObjectType objectType, boolean sendMessage) {
        PositionHierarchy positionHierarchy = positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(
                complaint.getAssignee().getId(), objectType.getId(), complaint.getComplaintType().getCode());
        Position nextAssignee = null;
        User nextOwner = null;
        if (positionHierarchy != null) {
            nextAssignee = positionHierarchy.getToPosition();
            List<Assignment> superiorAssignments = assignmentService.getAssignmentsForPosition(nextAssignee.getId(),
                    new Date());
            nextOwner = !superiorAssignments.isEmpty() ? superiorAssignments.get(0).getEmployee() : null;
        } else {
            Set<User> users = userService.getUsersByRoleName(PGRConstants.GRO_ROLE_NAME);
            if (!users.isEmpty())
                nextOwner = users.iterator().next();
            if (nextOwner != null) {
                List<Position> positionList = positionMasterService.getPositionsForEmployee(nextOwner.getId(), new Date());
                if (!positionList.isEmpty())
                    nextAssignee = positionList.iterator().next();
                else
                    LOG.warn("Could not escalate, no position defined for Grievance Officer role");
            } else
                LOG.warn("Could not escalate, no user defined for Grievance Officer role");
        }

        updateComplaintEscalation(complaint, nextAssignee, nextOwner, sendMessage);
    }

    private void updateComplaintEscalation(Complaint complaint, Position nextAssignee, User nextOwner,
                                           boolean sendMessage) {
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
        Integer noOfhrs = getHrsToResolve(designation.getId(), complaint.getComplaintType().getId());

        return DateUtils.addHours(new Date(), noOfhrs);
    }

    public Integer getHrsToResolve(Long designationId, Long complaintTypeId) {
        Escalation escalation = escalationRepository.findByDesignationAndComplaintType(designationId, complaintTypeId);
        return escalation != null ? escalation.getNoOfHrs() : configurationService.getDefaultComplaintResolutionTime();
    }

    @Transactional
    public void deleteAllInBatch(List<Escalation> entities) {
        escalationRepository.deleteInBatch(entities);

    }

    public Page<Escalation> getEscalationsTime(EscalationTimeSearchRequest escalationTimeSearchRequest) {
        Pageable pageable = new PageRequest(escalationTimeSearchRequest.pageNumber(),
                escalationTimeSearchRequest.pageSize(),
                escalationTimeSearchRequest.orderDir(), escalationTimeSearchRequest.orderBy());
        return escalationRepository.findAll(EscalationTimeSpec.search(escalationTimeSearchRequest),
                pageable);
    }

    public List<PositionHierarchy> getEscalationObjByComplaintTypeFromPosition(List<ComplaintType> complaintTypes,
                                                                               Position fromPosition) {
        List<String> compTypeCodes = complaintTypes.parallelStream()
                .map(ComplaintType::getCode)
                .collect(Collectors.toList());
        ObjectType objectType = objectTypeService.getObjectTypeByName(EG_OBJECT_TYPE_COMPLAINT);
        return positionHierarchyService.getListOfPositionHeirarchyByPositionObjectTypeSubType(objectType.getId(), compTypeCodes,
                fromPosition);
    }

    public PositionHierarchy getExistingEscalation(PositionHierarchy positionHierarchy) {
        PositionHierarchy existingPosHierarchy = null;
        if (null != positionHierarchy.getObjectSubType() && null != positionHierarchy.getFromPosition()
                && null != positionHierarchy.getToPosition())
            existingPosHierarchy = positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(
                    positionHierarchy.getFromPosition().getId(), positionHierarchy.getObjectType().getId(),
                    positionHierarchy.getObjectSubType());

        return existingPosHierarchy != null ? existingPosHierarchy : null;
    }

    public Escalation getEscalationBycomplaintTypeAndDesignation(Long complaintTypeId, Long designationId) {
        return escalationRepository.findByDesignationAndComplaintType(designationId, complaintTypeId);
    }

    public List<Complaint> getComplaintsEligibleForEscalation() {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Complaint.class, "complaint")
                .createAlias("complaint.status", "complaintStatus");

        criteria.add(Restrictions.disjunction().add(Restrictions.eq(COMPLAINT_STATUS_NAME, REOPENED.name()))
                .add(Restrictions.eq(COMPLAINT_STATUS_NAME, FORWARDED.name()))
                .add(Restrictions.eq(COMPLAINT_STATUS_NAME, PROCESSING.name()))
                .add(Restrictions.eq(COMPLAINT_STATUS_NAME, REGISTERED.name())))
                .add(Restrictions.lt("complaint.escalationDate", new DateTime().toDate()))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    @Transactional
    public void updateEscalationTime(EscalationForm escalationForm) {
        ComplaintType compType = null;
        if (escalationForm.getComplaintType() != null && escalationForm.getComplaintType().getId() != null) {
            compType = complaintTypeService.findBy(escalationForm.getComplaintType().getId());
            List<Escalation> escalationList = findAllBycomplaintTypeId(escalationForm.getComplaintType().getId());
            if (!escalationList.isEmpty())
                deleteAllInBatch(escalationList);
        }
        if (compType != null && escalationForm.getEscalationList() != null && !escalationForm.getEscalationList().isEmpty())
            for (Escalation escalation : escalationForm.getEscalationList()) {
                if (escalation.getDesignation() != null) {
                    Designation desig = designationService.getDesignationById(escalation.getDesignation().getId());
                    escalation.setComplaintType(compType);
                    escalation.setDesignation(desig);
                    escalation.setNoOfHrs(escalation.getNoOfHrs());
                    create(escalation);
                }
            }
    }

    public List<EscalationHelper> viewEscalation(Optional<Long> positionId, Optional<Long> complaintId) {
        ObjectType objectType = objectTypeService.getObjectTypeByName(EG_OBJECT_TYPE_COMPLAINT);
        if (objectType == null) {
            return Collections.emptyList();
        } else {
            ComplaintType complaintType = complaintId.isPresent() ? complaintTypeService.findBy(complaintId.get()) : null;
            List<String> activeComplaintTypeCodes = complaintTypeService.getActiveComplaintTypeCode();
            List<PositionHierarchy> positionHierarchies = new ArrayList<>();
            if (positionId.isPresent())
                positionHierarchyService
                        .getListOfPositionHeirarchyByFromPositionAndObjectTypeAndSubType(positionId.get(), objectType.getId(),
                                complaintType != null ? complaintType.getCode() : null)
                        .stream()
                        .filter(posHir -> activeComplaintTypeCodes.contains(posHir.getObjectSubType()))
                        .collect(Collectors.toList());
            return getEscalationDetailByPositionHierarchy(positionHierarchies);
        }
    }

    public List<EscalationHelper> getEscalationDetailByPositionHierarchy(List<PositionHierarchy> positionHierarchies) {
        List<EscalationHelper> escalationHelpers = new ArrayList<>();
        for (PositionHierarchy posHir : positionHierarchies) {
            EscalationHelper escalationHelper = new EscalationHelper();
            if (posHir.getObjectSubType() != null)
                escalationHelper.setComplaintType(complaintTypeService.findByCode(posHir.getObjectSubType()));
            escalationHelper.setFromPosition(posHir.getFromPosition());
            escalationHelper.setToPosition(posHir.getToPosition());
            escalationHelpers.add(escalationHelper);
        }
        return escalationHelpers;
    }

    @Transactional
    public void updateBulkEscalation(BulkEscalationGenerator bulkEscalationGenerator) {
        for (ComplaintType complaintType : bulkEscalationGenerator.getComplaintTypes()) {
            ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);
            PositionHierarchy positionHierarchy = new PositionHierarchy();
            positionHierarchy.setObjectType(objectType);
            positionHierarchy.setObjectSubType(complaintType.getCode());
            positionHierarchy.setFromPosition(bulkEscalationGenerator.getFromPosition());
            positionHierarchy.setToPosition(bulkEscalationGenerator.getToPosition());
            PositionHierarchy existingPosHierarchy = getExistingEscalation(positionHierarchy);
            if (existingPosHierarchy != null) {
                existingPosHierarchy.setToPosition(bulkEscalationGenerator.getToPosition());
                positionHierarchyService.updatePositionHierarchy(existingPosHierarchy);
            } else
                positionHierarchyService.createPositionHierarchy(positionHierarchy);
        }
    }

    @Transactional
    public void updateEscalation(Long id, EscalationForm escalationForm) {

        ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);
        List<PositionHierarchy> existingPosHierarchy = positionHierarchyService
                .getPositionHeirarchyByFromPositionAndObjectType(id, objectType.getId());
        if (!existingPosHierarchy.isEmpty())
            positionHierarchyService.deleteAllInBatch(existingPosHierarchy);

        for (PositionHierarchy posHierarchy : escalationForm.getPositionHierarchyList())
            if (posHierarchy.getFromPosition() != null && posHierarchy.getToPosition() != null) {
                posHierarchy.setFromPosition(positionMasterService.getPositionById(posHierarchy.getFromPosition().getId()));
                posHierarchy.setToPosition(positionMasterService.getPositionById(posHierarchy.getToPosition().getId()));
                posHierarchy.setObjectType(objectType);
                posHierarchy.setObjectSubType(posHierarchy.getObjectSubType());
                positionHierarchyService.createPositionHierarchy(posHierarchy);
            }
    }
}