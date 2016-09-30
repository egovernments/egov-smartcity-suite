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

package org.egov.pgr.elasticSearch.service;

import static java.util.Arrays.asList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.pgr.elasticSearch.entity.ComplaintIndex;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.EscalationService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.search.domain.Document;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintIndexService {
    @Autowired
    private CityService cityService;

    @Autowired
    private EscalationService escalationService;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private ComplaintService complaintService;

    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public ComplaintIndex createComplaintIndex(final ComplaintIndex complaintIndex) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        complaintIndex.setCitydetails(cityWebsite);
        if (complaintIndex.getReceivingMode().equals(ReceivingMode.MOBILE))
            complaintIndex.setSource("PuraSeva");
        else if (complaintIndex.getReceivingMode().equals(ReceivingMode.WEBSITE)
                && (complaintIndex.getCreatedBy().getType().equals(UserType.CITIZEN)
                        || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM)))
            complaintIndex.setSource("By citizens:ULB Portal");
        else if (complaintIndex.getCreatedBy().getType().equals(UserType.EMPLOYEE)
                || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM))
            complaintIndex.setSource("ULB counter");
        complaintIndex.setIsClosed(false);
        complaintIndex.setComplaintIsClosed('N');
        complaintIndex.setIfClosed(0);
        complaintIndex.setComplaintDuration(0);
        complaintIndex.setDurationRange("");
        // New fields included in PGR Index
        final Position position = complaintIndex.getAssignee();
        final User assignedUser = eisCommonService.getUserForPosition(position.getId(), new Date());
        complaintIndex.setComplaintPeriod(0);
        complaintIndex.setComplaintSLADays(complaintIndex.getComplaintType().getSlaHours());
        complaintIndex.setComplaintAgeingFromDue(0);
        complaintIndex.setIsSLA('Y');
        complaintIndex.setIfSLA(1);
        complaintIndex
                .setInitialFunctionaryName(assignedUser.getName() + ":" + position.getDeptDesig().getDesignation().getName());
        complaintIndex.setInitialFunctionaryAssigneddate(new Date());
        complaintIndex.setInitialFunctionarySLADays(getFunctionarySlaDays(complaintIndex));
        complaintIndex.setInitialFunctionaryAgeingFromDue(0);
        complaintIndex.setInitialFunctionaryIsSLA('Y');
        complaintIndex.setInitialFunctionaryIfSLA(1);
        complaintIndex
                .setCurrentFunctionaryName(assignedUser.getName() + ":" + position.getDeptDesig().getDesignation().getName());
        complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
        complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaintIndex));
        complaintIndex.setCurrentFunctionaryAgeingFromDue(0);
        complaintIndex.setCurrentFunctionaryIsSLA('Y');
        complaintIndex.setCurrentFunctionaryIfSLA(1);
        complaintIndex.setEscalationLevel(0);
        complaintIndex.setReasonForRejection("");
        complaintIndex.setRegistered(1);
        complaintIndex.setInProcess(1);
        complaintIndex.setAddressed(0);
        complaintIndex.setRejected(0);
        complaintIndex.setReOpened(0);

        return complaintIndex;
    }

    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public ComplaintIndex updateComplaintIndex(ComplaintIndex complaintIndex, final Long approvalPosition,
            final String approvalComment) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        complaintIndex.setCitydetails(cityWebsite);
        if (complaintIndex.getReceivingMode().equals(ReceivingMode.MOBILE))
            complaintIndex.setSource("PuraSeva");
        else if (complaintIndex.getReceivingMode().equals(ReceivingMode.WEBSITE)
                && (complaintIndex.getCreatedBy().getType().equals(UserType.CITIZEN)
                        || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM)))
            complaintIndex.setSource("By citizens:ULB Portal");
        else if (complaintIndex.getCreatedBy().getType().equals(UserType.EMPLOYEE)
                || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM))
            complaintIndex.setSource("ULB counter");
        // Update the complaint index object with the existing values
        complaintIndex = populateFromIndex(complaintIndex);
        final Position position = complaintIndex.getAssignee();
        final User assignedUser = eisCommonService.getUserForPosition(position.getId(), new Date());
        // If complaint is forwarded
        if (approvalPosition != null && !approvalPosition.equals(Long.valueOf(0))) {
            complaintIndex
                    .setCurrentFunctionaryName(assignedUser.getName() + ":" + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
            complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaintIndex));
        }
        complaintIndex = updateComplaintLevelIndexFields(complaintIndex);
        if (complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.COMPLETED.toString())
                || complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.WITHDRAWN.toString())
                || complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REJECTED.toString())) {
            complaintIndex.setIsClosed(true);
            complaintIndex.setComplaintIsClosed('Y');
            complaintIndex.setIfClosed(1);
            complaintIndex.setClosedByFunctionaryName(
                    assignedUser.getName() + ":" + position.getDeptDesig().getDesignation().getName());
            final long duration = Math.abs(complaintIndex.getCreatedDate().getTime() - new Date().getTime())
                    / (24 * 60 * 60 * 1000);
            complaintIndex.setComplaintDuration(duration);
            if (duration < 3)
                complaintIndex.setDurationRange("(<3 days)");
            else if (duration < 7)
                complaintIndex.setDurationRange("(3-7 days)");
            else if (duration < 15)
                complaintIndex.setDurationRange("(8-15 days)");
            else if (duration < 30)
                complaintIndex.setDurationRange("(16-30 days)");
            else
                complaintIndex.setDurationRange("(>30 days)");
        } else {
            complaintIndex.setIsClosed(false);
            complaintIndex.setComplaintIsClosed('N');
            complaintIndex.setIfClosed(0);
            complaintIndex.setComplaintDuration(0);
            complaintIndex.setDurationRange("");
        }
        // update status related fields in index
        complaintIndex = updateComplaintIndexStatusRelatedFields(complaintIndex);
        // If complaint is re-opened
        if (complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REOPENED.toString()) &&
                !checkComplaintStatusFromIndex(complaintIndex)) {
            complaintIndex.setComplaintReOpenedDate(new Date());
            complaintIndex.setIsClosed(false);
            complaintIndex.setComplaintIsClosed('N');
            complaintIndex.setIfClosed(0);
        }
        // If complaint is rejected update the Reason For Rejection with comments
        if (complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REJECTED.toString()))
            complaintIndex.setReasonForRejection(approvalComment);

        return complaintIndex;
    }

    // This method is used to populate PGR index during complaint escalation
    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public ComplaintIndex updateComplaintEscalationIndexValues(ComplaintIndex complaintIndex) {
        final Position position = complaintIndex.getAssignee();
        final User assignedUser = eisCommonService.getUserForPosition(position.getId(), new Date());
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        complaintIndex.setCitydetails(cityWebsite);
        if (complaintIndex.getReceivingMode().equals(ReceivingMode.MOBILE))
            complaintIndex.setSource("PuraSeva");
        else if (complaintIndex.getReceivingMode().equals(ReceivingMode.WEBSITE)
                && (complaintIndex.getCreatedBy().getType().equals(UserType.CITIZEN)
                        || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM)))
            complaintIndex.setSource("By citizens:ULB Portal");
        else if (complaintIndex.getCreatedBy().getType().equals(UserType.EMPLOYEE)
                || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM))
            complaintIndex.setSource("ULB counter");
        // Update the complaint index object with the existing values
        complaintIndex = populateFromIndex(complaintIndex);
        // Update current Functionary Complaint index variables
        complaintIndex
                .setCurrentFunctionaryName(assignedUser.getName() + ":" + position.getDeptDesig().getDesignation().getName());
        complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
        complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaintIndex));
        complaintIndex = updateComplaintLevelIndexFields(complaintIndex);
        int escalationLevel = complaintIndex.getEscalationLevel();
        // For Escalation level1
        if (escalationLevel == 0) {
            complaintIndex.setEscalation1FunctionaryName(
                    assignedUser.getName() + ":" + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setEscalation1FunctionaryAssigneddate(new Date());
            complaintIndex.setEscalation1FunctionarySLADays(getFunctionarySlaDays(complaintIndex));
            complaintIndex.setEscalation1FunctionaryAgeingFromDue(0);
            complaintIndex.setEscalation1FunctionaryIsSLA('Y');
            complaintIndex.setEscalation1FunctionaryIfSLA(1);
            complaintIndex.setEscalationLevel(++escalationLevel);
        } else if (escalationLevel == 1) {
            // update escalation level 2 fields
            complaintIndex.setEscalation2FunctionaryName(
                    assignedUser.getName() + ":" + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setEscalation2FunctionaryAssigneddate(new Date());
            complaintIndex.setEscalation2FunctionarySLADays(getFunctionarySlaDays(complaintIndex));
            complaintIndex.setEscalation2FunctionaryAgeingFromDue(0);
            complaintIndex.setEscalation2FunctionaryIsSLA('Y');
            complaintIndex.setEscalation2FunctionaryIfSLA(1);
            complaintIndex.setEscalationLevel(++escalationLevel);
        } else if (escalationLevel == 2) {
            // update escalation level 3 fields
            complaintIndex.setEscalation3FunctionaryName(
                    assignedUser.getName() + ":" + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setEscalation3FunctionaryAssigneddate(new Date());
            complaintIndex.setEscalation3FunctionarySLADays(getFunctionarySlaDays(complaintIndex));
            complaintIndex.setEscalation3FunctionaryAgeingFromDue(0);
            complaintIndex.setEscalation3FunctionaryIsSLA('Y');
            complaintIndex.setEscalation3FunctionaryIfSLA(1);
            complaintIndex.setEscalationLevel(++escalationLevel);
        }
        complaintIndex = updateEscalationLevelIndexFields(complaintIndex);
        // update status related fields in index
        complaintIndex = updateComplaintIndexStatusRelatedFields(complaintIndex);

        return complaintIndex;
    }

    public void updateAllOpenComplaintIndex() {
        final List<Complaint> openComplaints = complaintService.getOpenComplaints();
        for (final Complaint complaint : openComplaints)
            updateOpenComplaintIndex(complaint);
    }

    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public ComplaintIndex updateOpenComplaintIndex(final Complaint complaint) {
        final Complaint savedComplaintIndex = new ComplaintIndex();
        BeanUtils.copyProperties(complaint, savedComplaintIndex);
        ComplaintIndex complaintIndex = ComplaintIndex.method(savedComplaintIndex);
        complaintIndex = populateFromIndex(complaintIndex);
        complaintIndex = updateComplaintLevelIndexFields(complaintIndex);
        complaintIndex = updateEscalationLevelIndexFields(complaintIndex);
        // update status related fields in index
        complaintIndex = updateComplaintIndexStatusRelatedFields(complaintIndex);

        return complaintIndex;
    }

    public ComplaintIndex updateComplaintLevelIndexFields(final ComplaintIndex complaintIndex) {
        // Update complaint level index variables
        final long days = Math.abs(new Date().getTime() - complaintIndex.getCreatedDate().getTime()) / (24 * 60 * 60 * 1000);
        complaintIndex.setComplaintPeriod(days);
        Date lastDateToResolve = DateUtils.addHours(complaintIndex.getCreatedDate(), (int) complaintIndex.getComplaintSLADays());
        final Date currentDate = new Date();
        // If difference is greater than 0 then complaint is within SLA
        if (lastDateToResolve.getTime() - currentDate.getTime() >= 0) {
            complaintIndex.setComplaintAgeingFromDue(0);
            complaintIndex.setIsSLA('Y');
            complaintIndex.setIfSLA(1);
        } else {
            final long ageingDuehours = Math.abs(currentDate.getTime() - lastDateToResolve.getTime()) / (60 * 60 * 1000);
            complaintIndex.setComplaintAgeingFromDue(ageingDuehours);
            complaintIndex.setIsSLA('N');
            complaintIndex.setIfSLA(0);
        }

        // update the initial functionary level variables
        if (complaintIndex.getInitialFunctionaryAssigneddate() != null) {
            lastDateToResolve = DateUtils.addHours(complaintIndex.getInitialFunctionaryAssigneddate(),
                    (int) complaintIndex.getInitialFunctionarySLADays());
            // If difference is greater than 0 then initial functionary is within SLA
            if (lastDateToResolve.getTime() - new Date().getTime() >= 0) {
                complaintIndex.setInitialFunctionaryAgeingFromDue(0);
                complaintIndex.setInitialFunctionaryIsSLA('Y');
                complaintIndex.setInitialFunctionaryIfSLA(1);
            } else {
                final long initialFunctionaryAgeingDueHours = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
                        / (60 * 60 * 1000);
                complaintIndex.setInitialFunctionaryAgeingFromDue(initialFunctionaryAgeingDueHours);
                complaintIndex.setInitialFunctionaryIsSLA('N');
                complaintIndex.setInitialFunctionaryIfSLA(0);
            }
        }

        // update the current functionary level variables
        if (complaintIndex.getCurrentFunctionaryAssigneddate() != null) {
            lastDateToResolve = DateUtils.addHours(complaintIndex.getCurrentFunctionaryAssigneddate(),
                    (int) complaintIndex.getCurrentFunctionarySLADays());
            // If difference is greater than 0 then initial functionary is within SLA
            if (lastDateToResolve.getTime() - new Date().getTime() >= 0) {
                complaintIndex.setCurrentFunctionaryAgeingFromDue(0);
                complaintIndex.setCurrentFunctionaryIsSLA('Y');
                complaintIndex.setCurrentFunctionaryIfSLA(1);
            } else {
                final long currentFunctionaryAgeingDueHours = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
                        / (60 * 60 * 1000);
                complaintIndex.setCurrentFunctionaryAgeingFromDue(currentFunctionaryAgeingDueHours);
                complaintIndex.setCurrentFunctionaryIsSLA('N');
                complaintIndex.setCurrentFunctionaryIfSLA(0);
            }
        }
        return complaintIndex;
    }

    public ComplaintIndex updateEscalationLevelIndexFields(final ComplaintIndex complaintIndex) {
        // update the Escalation1 functionary level variables
        if (complaintIndex.getEscalation1FunctionaryAssigneddate() != null) {
            final Date lastDateToResolve = DateUtils.addHours(complaintIndex.getEscalation1FunctionaryAssigneddate(),
                    (int) complaintIndex.getEscalation1FunctionarySLADays());
            // If difference is greater than 0 then initial functionary is within SLA
            if (lastDateToResolve.getTime() - new Date().getTime() >= 0) {
                complaintIndex.setEscalation1FunctionaryAgeingFromDue(0);
                complaintIndex.setEscalation1FunctionaryIsSLA('Y');
                complaintIndex.setEscalation1FunctionaryIfSLA(1);
            } else {
                final long escalation1FunctionaryAgeingDueHours = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
                        / (60 * 60 * 1000);
                complaintIndex.setEscalation1FunctionaryAgeingFromDue(escalation1FunctionaryAgeingDueHours);
                complaintIndex.setEscalation1FunctionaryIsSLA('N');
                complaintIndex.setEscalation1FunctionaryIfSLA(0);
            }
        }

        // update the Escalation2 functionary level variables
        if (complaintIndex.getEscalation2FunctionaryAssigneddate() != null) {
            final Date lastDateToResolve = DateUtils.addHours(complaintIndex.getEscalation2FunctionaryAssigneddate(),
                    (int) complaintIndex.getEscalation2FunctionarySLADays());
            // If difference is greater than 0 then initial functionary is within SLA
            if (lastDateToResolve.getTime() - new Date().getTime() >= 0) {
                complaintIndex.setEscalation2FunctionaryAgeingFromDue(0);
                complaintIndex.setEscalation2FunctionaryIsSLA('Y');
                complaintIndex.setEscalation2FunctionaryIfSLA(1);
            } else {
                final long escalation2FunctionaryAgeingDueHours = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
                        / (60 * 60 * 1000);
                complaintIndex.setEscalation2FunctionaryAgeingFromDue(escalation2FunctionaryAgeingDueHours);
                complaintIndex.setEscalation2FunctionaryIsSLA('N');
                complaintIndex.setEscalation2FunctionaryIfSLA(0);
            }
        }

        // update the Escalation3 functionary level variables
        if (complaintIndex.getEscalation3FunctionaryAssigneddate() != null) {
            final Date lastDateToResolve = DateUtils.addHours(complaintIndex.getEscalation3FunctionaryAssigneddate(),
                    (int) complaintIndex.getEscalation3FunctionarySLADays());
            // If difference is greater than 0 then initial functionary is within SLA
            if (lastDateToResolve.getTime() - new Date().getTime() >= 0) {
                complaintIndex.setEscalation3FunctionaryAgeingFromDue(0);
                complaintIndex.setEscalation3FunctionaryIsSLA('Y');
                complaintIndex.setEscalation3FunctionaryIfSLA(1);
            } else {
                final long escalation3FunctionaryAgeingDueHours = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
                        / (60 * 60 * 1000);
                complaintIndex.setEscalation3FunctionaryAgeingFromDue(escalation3FunctionaryAgeingDueHours);
                complaintIndex.setEscalation3FunctionaryIsSLA('N');
                complaintIndex.setEscalation3FunctionaryIfSLA(0);
            }
        }
        return complaintIndex;
    }

    private long getFunctionarySlaDays(final ComplaintIndex complaintIndex) {
        final Position position = complaintIndex.getAssignee();
        final Designation designation = assignmentService.getPrimaryAssignmentForPositon(position.getId()).getDesignation();
        final Escalation complaintEscalation = escalationService
                .getEscalationBycomplaintTypeAndDesignation(complaintIndex.getComplaintType().getId(), designation.getId());
        if (complaintEscalation != null) {
            final long slaHours = complaintEscalation.getNoOfHrs();
            return slaHours;
        } else
            return 0;
    }

    public ComplaintIndex populateFromIndex(final ComplaintIndex complaintIndex) {
        final SearchResult searchResult = searchService.search(asList(Index.PGR.toString()),
                asList(IndexType.COMPLAINT.toString()),
                null, complaintIndex.searchFilters(), Sort.NULL, Page.NULL);
        if (!searchResult.getDocuments().isEmpty()) {
            final List<Document> documents = searchResult.getDocuments();
            final LinkedHashMap<String, Object> clausesObject = (LinkedHashMap<String, Object>) documents.get(0).getResource()
                    .get("clauses");
            final LinkedHashMap<String, Object> searchableObject = (LinkedHashMap<String, Object>) documents.get(0).getResource()
                    .get("searchable");

            final double complaintPeriod = clausesObject.get("complaintPeriod") == null ? 0
                    : (double) Double.valueOf(clausesObject.get("complaintPeriod").toString());
            final double complaintDuration = searchableObject.get("complaintDuration") == null ? 0
                    : (double) Double.valueOf(searchableObject.get("complaintDuration").toString());
            final int complaintSlaDays = clausesObject.get("complaintSLADays") == null ? 0
                    : (int) clausesObject.get("complaintSLADays");
            final double complaintAgeingFromDue = clausesObject.get("complaintAgeingFromDue") == null ? 0
                    : (double) Double.valueOf(clausesObject.get("complaintAgeingFromDue").toString());
            final char isSla = clausesObject.get("isSLA") == null ? '-' : clausesObject.get("isSLA").toString().charAt(0);
            final int ifSla = clausesObject.get("ifSLA") == null ? 1 : (int) clausesObject.get("ifSLA");
            final boolean isClosed = searchableObject.get("isClosed") == null ? false
                    : Boolean.valueOf(searchableObject.get("isClosed").toString());
            final char complaintIsClosed = searchableObject.get("complaintIsClosed") == null ? 'N'
                    : searchableObject.get("complaintIsClosed").toString().charAt(0);
            final int ifClosed = searchableObject.get("ifClosed") == null ? 0 : (int) searchableObject.get("ifClosed");
            final String initialFunctionaryName = clausesObject.get("initialFunctionaryName") == null ? ""
                    : clausesObject.get("initialFunctionaryName").toString();
            final int initialFunctionarySlaDays = clausesObject.get("initialFunctionarySLADays") == null ? 0
                    : (int) clausesObject.get("initialFunctionarySLADays");
            final double initialAgeingFromDue = clausesObject.get("initialFunctionaryAgeingFromDue") == null ? 0
                    : (double) Double.valueOf(clausesObject.get("initialFunctionaryAgeingFromDue").toString());
            final char initialFunctionaryIsSla = clausesObject.get("initialFunctionaryIsSLA") == null ? '-'
                    : clausesObject.get("initialFunctionaryIsSLA").toString().charAt(0);
            final int initialFunctionaryIfSla = clausesObject.get("initialFunctionaryIfSLA") == null ? 1
                    : (int) clausesObject.get("initialFunctionaryIfSLA");
            final String currentFunctionaryName = clausesObject.get("currentFunctionaryName") == null ? ""
                    : clausesObject.get("currentFunctionaryName").toString();
            final int currentFunctionarySlaDays = clausesObject.get("currentFunctionarySLADays") == null ? 0
                    : (int) clausesObject.get("currentFunctionarySLADays");
            final double currentAgeingFromDue = clausesObject.get("currentFunctionaryAgeingFromDue") == null ? 0
                    : (double) Double.valueOf(clausesObject.get("currentFunctionaryAgeingFromDue").toString());
            final char currentFunctionaryIsSla = clausesObject.get("currentFunctionaryIsSLA") == null ? '-'
                    : clausesObject.get("currentFunctionaryIsSLA").toString().charAt(0);
            final int currentFunctionaryIfSla = clausesObject.get("currentFunctionaryIfSLA") == null ? 1
                    : (int) clausesObject.get("currentFunctionaryIfSLA");
            final String escalation1FunctionaryName = clausesObject.get("escalation1FunctionaryName") == null ? ""
                    : clausesObject.get("escalation1FunctionaryName").toString();
            final int escalation1FunctionarySlaDays = clausesObject.get("escalation1FunctionarySLADays") == null ? 0
                    : (int) clausesObject.get("escalation1FunctionarySLADays");
            final double escalation1AgeingFromDue = clausesObject.get("escalation1FunctionaryAgeingFromDue") == null ? 0
                    : (double) Double.valueOf(clausesObject.get("escalation1FunctionaryAgeingFromDue").toString());
            final char escalation1FunctionaryIsSla = clausesObject.get("escalation1FunctionaryIsSLA") == null ? '-'
                    : clausesObject.get("escalation1FunctionaryIsSLA").toString().charAt(0);
            final int escalation1FunctionaryIfSla = clausesObject.get("escalation1FunctionaryIfSLA") == null ? 1
                    : (int) clausesObject.get("escalation1FunctionaryIfSLA");
            final String escalation2FunctionaryName = clausesObject.get("escalation2FunctionaryName") == null ? ""
                    : clausesObject.get("escalation2FunctionaryName").toString();
            final int escalation2FunctionarySlaDays = clausesObject.get("escalation2FunctionarySLADays") == null ? 0
                    : (int) clausesObject.get("escalation2FunctionarySLADays");
            final double escalation2AgeingFromDue = clausesObject.get("escalation2FunctionaryAgeingFromDue") == null ? 0
                    : (double) Double.valueOf(clausesObject.get("escalation2FunctionaryAgeingFromDue").toString());
            final char escalation2FunctionaryIsSla = clausesObject.get("escalation2FunctionaryIsSLA") == null ? '-'
                    : clausesObject.get("escalation2FunctionaryIsSLA").toString().charAt(0);
            final int escalation2FunctionaryIfSla = clausesObject.get("escalation2FunctionaryIfSLA") == null ? 1
                    : (int) clausesObject.get("escalation2FunctionaryIfSLA");
            final String escalation3FunctionaryName = clausesObject.get("escalation3FunctionaryName") == null ? ""
                    : clausesObject.get("escalation3FunctionaryName").toString();
            final int escalation3FunctionarySlaDays = clausesObject.get("escalation3FunctionarySLADays") == null ? 0
                    : (int) clausesObject.get("escalation3FunctionarySLADays");
            final double escalation3AgeingFromDue = clausesObject.get("escalation3FunctionaryAgeingFromDue") == null ? 0
                    : (double) Double.valueOf(clausesObject.get("escalation3FunctionaryAgeingFromDue").toString());
            final char escalation3FunctionaryIsSla = clausesObject.get("escalation3FunctionaryIsSLA") == null ? '-'
                    : clausesObject.get("escalation3FunctionaryIsSLA").toString().charAt(0);
            final int escalation3FunctionaryIfSla = clausesObject.get("escalation3FunctionaryIfSLA") == null ? 1
                    : (int) clausesObject.get("escalation3FunctionaryIfSLA");
            final int escalationLevel = clausesObject.get("escalationLevel") == null ? 0
                    : (int) clausesObject.get("escalationLevel");
            final String durationRange = searchableObject.get("durationRange") == null ? ""
                    : searchableObject.get("durationRange").toString();
            final String reasonForRejection = clausesObject.get("reasonForRejection") == null ? ""
                    : clausesObject.get("reasonForRejection").toString();
            final int registered = clausesObject.get("registered") == null ? 1 : (int) clausesObject.get("registered");
            final int reOpened = clausesObject.get("reOpened") == null ? 0 : (int) clausesObject.get("reOpened");

            complaintIndex.setComplaintPeriod(complaintPeriod);
            complaintIndex.setComplaintDuration(complaintDuration);
            complaintIndex.setComplaintSLADays(complaintSlaDays);
            complaintIndex.setComplaintAgeingFromDue(complaintAgeingFromDue);
            complaintIndex.setIsSLA(isSla);
            complaintIndex.setIfSLA(ifSla);
            complaintIndex.setIsClosed(isClosed);
            complaintIndex.setComplaintIsClosed(complaintIsClosed);
            complaintIndex.setIfClosed(ifClosed);
            complaintIndex.setInitialFunctionaryName(initialFunctionaryName);
            if (searchableObject.get("initialFunctionaryAssigneddate") != null)
                complaintIndex.setInitialFunctionaryAssigneddate(
                        formatDate(searchableObject.get("initialFunctionaryAssigneddate").toString()));
            else
                complaintIndex.setInitialFunctionaryAssigneddate(complaintIndex.getCreatedDate());
            complaintIndex.setInitialFunctionarySLADays(initialFunctionarySlaDays);
            complaintIndex.setInitialFunctionaryAgeingFromDue(initialAgeingFromDue);
            complaintIndex.setInitialFunctionaryIsSLA(initialFunctionaryIsSla);
            complaintIndex.setInitialFunctionaryIfSLA(initialFunctionaryIfSla);
            complaintIndex.setCurrentFunctionaryName(currentFunctionaryName);
            if (searchableObject.get("currentFunctionaryAssigneddate") != null)
                complaintIndex.setCurrentFunctionaryAssigneddate(
                        formatDate(searchableObject.get("currentFunctionaryAssigneddate").toString()));
            complaintIndex.setCurrentFunctionarySLADays(currentFunctionarySlaDays);
            complaintIndex.setCurrentFunctionaryAgeingFromDue(currentAgeingFromDue);
            complaintIndex.setCurrentFunctionaryIsSLA(currentFunctionaryIsSla);
            complaintIndex.setCurrentFunctionaryIfSLA(currentFunctionaryIfSla);
            complaintIndex.setEscalation1FunctionaryName(escalation1FunctionaryName);
            if (searchableObject.get("escalation1FunctionaryAssigneddate") != null)
                complaintIndex.setEscalation1FunctionaryAssigneddate(
                        formatDate(searchableObject.get("escalation1FunctionaryAssigneddate").toString()));
            complaintIndex.setEscalation1FunctionarySLADays(escalation1FunctionarySlaDays);
            complaintIndex.setEscalation1FunctionaryAgeingFromDue(escalation1AgeingFromDue);
            complaintIndex.setEscalation1FunctionaryIsSLA(escalation1FunctionaryIsSla);
            complaintIndex.setEscalation1FunctionaryIfSLA(escalation1FunctionaryIfSla);
            complaintIndex.setEscalation2FunctionaryName(escalation2FunctionaryName);
            if (searchableObject.get("escalation2FunctionaryAssigneddate") != null)
                complaintIndex.setEscalation2FunctionaryAssigneddate(
                        formatDate(searchableObject.get("escalation2FunctionaryAssigneddate").toString()));
            complaintIndex.setEscalation2FunctionarySLADays(escalation2FunctionarySlaDays);
            complaintIndex.setEscalation2FunctionaryAgeingFromDue(escalation2AgeingFromDue);
            complaintIndex.setEscalation2FunctionaryIsSLA(escalation2FunctionaryIsSla);
            complaintIndex.setEscalation2FunctionaryIfSLA(escalation2FunctionaryIfSla);
            complaintIndex.setEscalation3FunctionaryName(escalation3FunctionaryName);
            if (searchableObject.get("escalation3FunctionaryAssigneddate") != null)
                complaintIndex.setEscalation3FunctionaryAssigneddate(
                        formatDate(searchableObject.get("escalation3FunctionaryAssigneddate").toString()));
            complaintIndex.setEscalation3FunctionarySLADays(escalation3FunctionarySlaDays);
            complaintIndex.setEscalation3FunctionaryAgeingFromDue(escalation3AgeingFromDue);
            complaintIndex.setEscalation3FunctionaryIsSLA(escalation3FunctionaryIsSla);
            complaintIndex.setEscalation3FunctionaryIfSLA(escalation3FunctionaryIfSla);
            complaintIndex.setEscalationLevel(escalationLevel);
            complaintIndex.setDurationRange(durationRange);
            complaintIndex.setReasonForRejection(reasonForRejection);
            complaintIndex.setRegistered(registered);
            complaintIndex.setReOpened(reOpened);
            if (searchableObject.get("complaintReOpenedDate") != null)
                complaintIndex.setComplaintReOpenedDate(formatDate(searchableObject.get("complaintReOpenedDate").toString()));
        }
        return complaintIndex;
    }

    // returns false if status in index for complaint is REOPENED else will return true
    // This method used to prevent updating reopened date in index for already reopened complaints
    private boolean checkComplaintStatusFromIndex(final ComplaintIndex complaintIndex) {
        final SearchResult searchResult = searchService.search(asList(Index.PGR.toString()),
                asList(IndexType.COMPLAINT.toString()),
                null, complaintIndex.searchFilters(), Sort.NULL, Page.NULL);
        if (!searchResult.getDocuments().isEmpty()) {
            final List<Document> documents = searchResult.getDocuments();
            final LinkedHashMap<String, Object> clausesObject = (LinkedHashMap<String, Object>) documents.get(0).getResource()
                    .get("clauses");
            final String status = clausesObject.get("status") == null ? "" : clausesObject.get("status").toString();
            return status.contains("REOPENED");
        }
        return true;
    }

    public Date formatDate(final String date) {
        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        try {
            return formatter.parse(date);
        } catch (final ParseException e) {
        }
        return null;
    }

    private ComplaintIndex updateComplaintIndexStatusRelatedFields(final ComplaintIndex complaintIndex) {
        if (complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.PROCESSING.toString())
                || complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.FORWARDED.toString())
                || complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REGISTERED.toString())) {
            complaintIndex.setInProcess(1);
            complaintIndex.setAddressed(0);
            complaintIndex.setRejected(0);
        }
        if (complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.COMPLETED.toString())
                || complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.WITHDRAWN.toString())) {
            complaintIndex.setInProcess(0);
            complaintIndex.setAddressed(1);
            complaintIndex.setRejected(0);
        }
        if (complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REJECTED.toString())) {
            complaintIndex.setInProcess(0);
            complaintIndex.setAddressed(0);
            complaintIndex.setRejected(1);
        }
        if (complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REOPENED.toString())) {
            complaintIndex.setInProcess(1);
            complaintIndex.setAddressed(0);
            complaintIndex.setRejected(0);
            complaintIndex.setReOpened(1);
        }
        return complaintIndex;
    }
}