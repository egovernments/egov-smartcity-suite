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

import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.repository.ComplaintRepository;
import org.egov.pgr.service.es.ComplaintIndexService;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINTS_FILED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINTS_RESOLVED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINTS_UNRESOLVED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_ALL;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_COMPLETED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_PENDING;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REGISTERED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REJECTED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLETED_STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.PENDING_STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.REJECTED_STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.RESOLVED_STATUS;

@Service
@Transactional(readOnly = true)
public class ComplaintService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintStatusService complaintStatusService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private ComplaintIndexService complaintIndexService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ComplaintMessagingService complaintMessagingService;

    @Autowired
    private ComplaintProcessFlowService complaintProcessFlowService;

    @Autowired
    private CitizenComplaintDataPublisher citizenComplaintDataPublisher;

    @Transactional
    public Complaint createComplaint(Complaint complaint) {

        if (isBlank(complaint.getCrn()))
            complaint.setCrn(applicationNumberGenerator.generate());

        complaint.setStatus(complaintStatusService.getByName(COMPLAINT_REGISTERED));
        User user = securityUtils.getCurrentUser();
        complaint.getComplainant().setUserDetail(user);
        if (securityUtils.currentUserIsCitizen()) {
            complaint.getComplainant().setEmail(user.getEmailId());
            complaint.getComplainant().setName(user.getName());
            complaint.getComplainant().setMobile(user.getMobileNumber());
        }
        if (complaint.getLocation() == null && complaint.hasGeoCoordinates()) {
            Boundary location = boundaryService.getBoundaryByGisCoordinates(complaint.getLat(), complaint.getLng());
            if (location != null)
                complaint.setLocation(location);
        }
        complaintProcessFlowService.onRegistration(complaint);
        if (complaint.getComplaintType().getDepartment() != null)
            complaint.setDepartment(complaint.getComplaintType().getDepartment());
        else
            complaint.setDepartment(complaint.getAssignee().getDeptDesig().getDepartment());
        if (complaint.getPriority() == null)
            complaint.setPriority(configurationService.getDefaultComplaintPriority());

        complaintRepository.saveAndFlush(complaint);
        complaintIndexService.createComplaintIndex(complaint);
        if (securityUtils.currentUserIsCitizen())
            citizenComplaintDataPublisher.onRegistration(complaint);
        complaintMessagingService.sendRegistrationMessage(complaint);
        return complaint;
    }

    @Transactional
    public Complaint updateComplaint(Complaint complaint) {
        complaintProcessFlowService.onUpdation(complaint);
        if (complaint.getComplaintType().getDepartment() != null)
            complaint.setDepartment(complaint.getComplaintType().getDepartment());
        else
            complaint.setDepartment(complaint.getAssignee().getDeptDesig().getDepartment());
        complaintRepository.saveAndFlush(complaint);
        complaintIndexService.updateComplaintIndex(complaint);
        citizenComplaintDataPublisher.onUpdation(complaint);
        complaintMessagingService.sendUpdateMessage(complaint);
        return complaint;
    }

    public Complaint getComplaintById(Long complaintID) {
        return complaintRepository.findOne(complaintID);
    }

    public Complaint getComplaintByCRN(String crn) {
        return complaintRepository.findByCrn(crn);
    }

    @ReadOnly
    public Page<Complaint> getLatest(int page, int pageSize) {
        return complaintRepository.findByCreatedByIdNotOrderByCreatedDateDesc(getUserId(), new PageRequest(page - 1, pageSize));
    }

    @ReadOnly
    public Page<Complaint> getMyComplaint(int page, int pageSize) {
        return complaintRepository.findByCreatedByIdOrderByCreatedDateDesc(getUserId(), new PageRequest(page - 1, pageSize));
    }

    @ReadOnly
    public List<Complaint> getNearByComplaint(int page, int pageSize, float lat, float lng, long distance) {
        return complaintRepository.findByNearestComplaint(getUserId(), lat, lng, distance,
                pageSize + 1L, (page - 1L) * pageSize);
    }

    @ReadOnly
    public List<Complaint> getPendingGrievances() {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Complaint.class, "complaint")
                .createAlias("complaint.status", "status");
        criteria.add(Restrictions.in("status.name", PENDING_STATUS));
        criteria.add(
                Restrictions.in("complaint.assignee", positionMasterService.getPositionsForEmployee(getUserId(), new Date())));
        return criteria.list();
    }

    @ReadOnly
    public Page<Complaint> getMyPendingGrievances(int page, int pageSize) {
        return complaintRepository.findByCreatedByIdAndStatusNameInOrderByCreatedDateDesc(getUserId(), PENDING_STATUS,
                new PageRequest(page - 1, pageSize));
    }

    @ReadOnly
    public Page<Complaint> getMyCompletedGrievances(int page, int pageSize) {
        return complaintRepository.findByCreatedByIdAndStatusNameInOrderByCreatedDateDesc(getUserId(), COMPLETED_STATUS,
                new PageRequest(page - 1, pageSize));
    }

    @ReadOnly
    public Page<Complaint> getMyRejectedGrievances(int page, int pageSize) {
        return complaintRepository.findByCreatedByIdAndStatusNameInOrderByCreatedDateDesc(getUserId(), REJECTED_STATUS,
                new PageRequest(page - 1, pageSize));
    }

    @ReadOnly
    public Map<String, Long> getMyComplaintsCount() {
        HashMap<String, Long> complaintsCount = new HashMap<>();
        complaintsCount.put(COMPLAINT_ALL,
                complaintRepository.countByCreatedById(getUserId()));
        complaintsCount.put(COMPLAINT_PENDING,
                complaintRepository.countByCreatedByIdAndStatusNameIn(getUserId(), PENDING_STATUS));
        complaintsCount.put(COMPLAINT_COMPLETED,
                complaintRepository.countByCreatedByIdAndStatusNameIn(getUserId(), COMPLETED_STATUS));
        complaintsCount.put(COMPLAINT_REJECTED,
                complaintRepository.countByCreatedByIdAndStatusNameIn(getUserId(), REJECTED_STATUS));
        return complaintsCount;
    }

    @ReadOnly
    public Map<String, Long> getComplaintsTotalCount() {
        HashMap<String, Long> complaintsCount = new HashMap<>();
        complaintsCount.put(COMPLAINTS_FILED, complaintRepository.count());
        complaintsCount.put(COMPLAINTS_RESOLVED, complaintRepository.countByStatusNameIn(RESOLVED_STATUS));
        complaintsCount.put(COMPLAINTS_UNRESOLVED, complaintRepository.countByStatusNameIn(PENDING_STATUS));
        return complaintsCount;
    }

    @ReadOnly
    public List<Complaint> getOpenComplaints() {
        return complaintRepository.findByStatusNameIn(Arrays.asList(PENDING_STATUS));
    }

    @ReadOnly
    public List<Complaint> getActedUponComplaints(int page, int pageSize) {
        User user = securityUtils.getCurrentUser();
        List<Position> positions = positionMasterService.getPositionsForEmployee(user.getId());
        List<Long> positionIds = new ArrayList<>();
        positions.stream().forEach(position -> positionIds.add(position.getId()));
        return complaintRepository.findRoutedComplaints(positionIds, Arrays.asList(PENDING_STATUS), pageSize + 1L, (page - 1L) * pageSize);
    }

    @ReadOnly
    public List<Complaint> getActedUponComplaintCount() {
        User user = securityUtils.getCurrentUser();
        List<Complaint> complaintList = new ArrayList<>();
        List<Complaint> openComplaints = complaintRepository.findByStatusNameIn(Arrays.asList(PENDING_STATUS));
        List<Position> positions = positionMasterService.getPositionsForEmployee(user.getId());
        openComplaints.forEach(openComplaint -> {
            if (!openComplaint.getStateHistory().isEmpty()) {
                openComplaint.getStateHistory().stream()
                        .sorted(Comparator.comparing(StateHistory::getLastModifiedDate)).findFirst()
                        .ifPresent(stateHistory -> {
                            if (positions.contains(stateHistory.getOwnerPosition()))
                                complaintList.add(openComplaint);
                        });
            }
        });
        return complaintList;
    }
}
