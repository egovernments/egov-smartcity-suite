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

package org.egov.pgr.elasticsearch.service;

import org.apache.commons.lang.time.DateUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.es.CityIndexService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.mapper.BeanMapperConfiguration;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.entity.ReceivingMode;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.elasticsearch.entity.contract.ComplaintDashBoardRequest;
import org.egov.pgr.elasticsearch.entity.contract.ComplaintDashBoardResponse;
import org.egov.pgr.elasticsearch.entity.ComplaintIndex;
import org.egov.pgr.elasticsearch.entity.contract.ComplaintSourceResponse;
import org.egov.pgr.elasticsearch.repository.ComplaintIndexRepository;
import org.egov.pgr.elasticsearch.repository.ComplaintIndexAggregationBuilder;
import org.egov.pgr.service.ComplaintEscalationService;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ReceivingModeService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityCode;
import static org.egov.infra.utils.ApplicationConstant.NA;
import static org.egov.pgr.utils.constants.PGRConstants.CITY_CODE;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REOPENED;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_FUNCTIONARY;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_LOCALITIES;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_ULB;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_WARDS;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_CITY;
import static org.egov.pgr.utils.constants.PGRConstants.NOASSIGNMENT;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@Service
@Transactional(readOnly = true)
public class ComplaintIndexService {

    private static final String SOURCE = "source";

    private static final String GROUP_BY_FIELD_AGEING_FOR_HOURS = "groupByFieldAgeingForHours";

    private static final String GROUP_BY_FIELD_AGEING = "groupByFieldAgeing";

    private static final String INITIAL_FUNCTIONARY_MOBILE_NUMBER = "initialFunctionaryMobileNumber";

    private static final String DOMAIN_URL = "domainURL";

    private static final String ULB_NAME = "ulbName";

    private static final String ULB_GRADE = "ulbGrade";

    private static final String ULB_CODE = "ulbCode";

    private static final String DISTRICT_NAME = "districtName";

    private static final String REGION_NAME = "regionName";

    private static final String INITIAL_FUNCTIONARY_NAME = "initialFunctionaryName";

    private static final String CITY_GRADE = "cityGrade";

    private static final String CITY_REGION_NAME = "cityRegionName";

    private static final String GROUP_BY_FIELD = "groupByField";

    private static final String ULBWISE = "ulbwise";

    private static final String LOCALITY_NAME = "localityName";

    private static final String RE_OPENED_COMPLAINT_COUNT = "reOpenedComplaintCount";
    private static final String NOLOCALITY = "nolocality";
    private static final String CLOSED_COMPLAINT_COUNT = "closedComplaintCount";
    private static final String COMPLAINTRECORD = "complaintrecord";
    private static final String GROUP_BY_FIELD_SATISFACTION_AVERAGE = "groupByFieldSatisfactionAverage";
    private static final String GROUP_FIELDWISE_OPEN_AND_CLOSED_COUNT = "groupFieldWiseOpenAndClosedCount";
    private static final String WARDWISE = "wardwise";
    private static final String DEPARTMENTWISE = "departmentwise";
    private static final String PGR_COMPLAINT_VIEW_URL = "%s/pgr/complaint/view/%s";
    private static final String EXCLUDE_ZERO = "excludeZero";
    private static final String CITY_NAME = "cityName";
    private static final String CITY_DISTRICT_NAME = "cityDistrictName";
    private static final String WARD_NAME = "wardName";
    private static final String GROUP_BY_FIELD_SLA = "groupByFieldSla";
    private static final String DEPARTMENT_NAME = "departmentName";
    private static final String COMPLAINT_TYPEWISE = "complaintTypeWise";
    private static final String COMPLAINTS = "complaints";
    private static final String COMPLAINT_COUNT = "complaintCount";
    private static final String CITY_DISTRICT_CODE = "cityDistrictCode";
    private static final String RESPONSE_DETAILS = "responseDetails";

    @Autowired
    private CityService cityService;

    @Autowired
    private ComplaintEscalationService escalationService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private CityIndexService cityIndexService;

    @Autowired
    private ComplaintIndexRepository complaintIndexRepository;

    @Autowired
    private BeanMapperConfiguration beanMapperConfiguration;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ReceivingModeService receivingModeService;

    public void createComplaintIndex(final Complaint complaint) {
        final ComplaintIndex complaintIndex = new ComplaintIndex();
        beanMapperConfiguration.map(complaint, complaintIndex);

        final City city = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        complaintIndex.setCityCode(city.getCode());
        complaintIndex.setCityDistrictCode(city.getDistrictCode());
        complaintIndex.setCityDistrictName(city.getDistrictName());
        complaintIndex.setCityGrade(city.getGrade());
        complaintIndex.setCityDomainUrl(city.getDomainURL());
        complaintIndex.setCityName(city.getName());
        complaintIndex.setCityRegionName(city.getRegionName());
        complaintIndex.setSource(complaint.getReceivingMode().getName());
        complaintIndex.setClosed(false);
        complaintIndex.setComplaintIsClosed("N");
        complaintIndex.setIfClosed(0);
        complaintIndex.setComplaintDuration(0);
        complaintIndex.setDurationRange("");
        final Position position = complaint.getAssignee();
        final List<Assignment> assignments = assignmentService.getAssignmentsForPosition(position.getId(), new Date());
        final User assignedUser = !assignments.isEmpty() ? assignments.get(0).getEmployee() : null;
        complaintIndex.setComplaintPeriod(0);
        complaintIndex.setComplaintSLADays(complaint.getComplaintType().getSlaHours());
        complaintIndex.setComplaintAgeingFromDue(0);
        complaintIndex.setIsSLA("Y");
        complaintIndex.setIfSLA(1);
        complaintIndex.setInitialFunctionaryName(
                assignedUser != null ? assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName()
                        : NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName());
        complaintIndex.setInitialFunctionaryAssigneddate(new Date());
        complaintIndex.setInitialFunctionarySLADays(getFunctionarySlaDays(complaint));
        complaintIndex.setInitialFunctionaryAgeingFromDue(0);
        complaintIndex.setInitialFunctionaryIsSLA("Y");
        complaintIndex.setInitialFunctionaryIfSLA(1);
        complaintIndex.setCurrentFunctionaryName(
                assignedUser != null ? assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName()
                        : NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName());
        complaintIndex.setCurrentFunctionaryMobileNumber(Objects.nonNull(assignedUser)
                ? assignedUser.getMobileNumber() : EMPTY);
        complaintIndex.setInitialFunctionaryMobileNumber(Objects.nonNull(assignedUser)
                ? assignedUser.getMobileNumber() : EMPTY);
        complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
        complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaint));
        complaintIndex.setCurrentFunctionaryAgeingFromDue(0);
        complaintIndex.setCurrentFunctionaryIsSLA("Y");
        complaintIndex.setCurrentFunctionaryIfSLA(1);
        complaintIndex.setEscalationLevel(0);
        complaintIndex.setReasonForRejection("");
        complaintIndex.setRegistered(1);
        complaintIndex.setInProcess(1);
        complaintIndex.setAddressed(0);
        complaintIndex.setRejected(0);
        complaintIndex.setReOpened(0);
        // New fields included in PGR Index
        complaintIndex.setComplaintAgeingdaysFromDue(0);

        complaintIndexRepository.save(complaintIndex);
    }

    public void updateComplaintIndex(final Complaint complaint) {
        // fetch the complaint from index and then update the new fields
        ComplaintIndex complaintIndex = complaintIndexRepository.findByCrnAndCityCode(complaint.getCrn(), getCityCode());
        final String status = complaintIndex.getComplaintStatusName();
        beanMapperConfiguration.map(complaint, complaintIndex);

        final City city = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        complaintIndex.setCityCode(city.getCode());
        complaintIndex.setCityDistrictCode(city.getDistrictCode());
        complaintIndex.setCityDistrictName(city.getDistrictName());
        complaintIndex.setCityGrade(city.getGrade());
        complaintIndex.setCityDomainUrl(city.getDomainURL());
        complaintIndex.setCityName(city.getName());
        complaintIndex.setCityRegionName(city.getRegionName());
        final Position position = complaint.getAssignee();
        final List<Assignment> assignments = assignmentService.getAssignmentsForPosition(position.getId(), new Date());
        final User assignedUser = !assignments.isEmpty() ? assignments.get(0).getEmployee() : null;
        // If complaint is forwarded
        if (complaint.nextOwnerId() != null && !complaint.nextOwnerId().equals(0L)) {
            complaintIndex
                    .setCurrentFunctionaryName(assignedUser != null
                            ? assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName()
                            : NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setCurrentFunctionaryMobileNumber(Objects.nonNull(assignedUser)
                    ? assignedUser.getMobileNumber() : EMPTY);
            complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
            complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaint));
        }
        complaintIndex = updateComplaintLevelIndexFields(complaintIndex);
        if (complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.COMPLETED.toString())
                || complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.WITHDRAWN.toString())
                || complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.REJECTED.toString())) {
            complaintIndex.setClosed(true);
            complaintIndex.setComplaintIsClosed("Y");
            complaintIndex.setIfClosed(1);
            complaintIndex.setClosedByFunctionaryName(
                    assignedUser != null ? assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName()
                            : NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName());
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
            complaintIndex.setClosed(false);
            complaintIndex.setComplaintIsClosed("N");
            complaintIndex.setIfClosed(0);
            complaintIndex.setComplaintDuration(0);
            complaintIndex.setDurationRange("");
        }
        // update status related fields in index
        complaintIndex = updateComplaintIndexStatusRelatedFields(complaintIndex);
        // If complaint is re-opened
        if (complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.REOPENED.toString()) &&
                !status.contains(COMPLAINT_REOPENED)) {
            complaintIndex.setComplaintReOpenedDate(new Date());
            complaintIndex.setClosed(false);
            complaintIndex.setComplaintIsClosed("N");
            complaintIndex.setIfClosed(0);
        }
        // If complaint is rejected update the Reason For Rejection with comments
        if (complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.REJECTED.toString()))
            complaintIndex.setReasonForRejection(complaint.approverComment());

        complaintIndexRepository.save(complaintIndex);
    }

    // This method is used to populate PGR index during complaint escalation
    public void updateComplaintEscalationIndexValues(final Complaint complaint) {

        // fetch the complaint from index and then update the new fields
        ComplaintIndex complaintIndex = complaintIndexRepository.findByCrnAndCityCode(complaint.getCrn(), getCityCode());
        beanMapperConfiguration.map(complaint, complaintIndex);

        final Position position = complaint.getAssignee();
        final List<Assignment> assignments = assignmentService.getAssignmentsForPosition(position.getId(), new Date());
        final User assignedUser = !assignments.isEmpty() ? assignments.get(0).getEmployee() : null;
        final City city = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        complaintIndex.setCityCode(city.getCode());
        complaintIndex.setCityDistrictCode(city.getDistrictCode());
        complaintIndex.setCityDistrictName(city.getDistrictName());
        complaintIndex.setCityGrade(city.getGrade());
        complaintIndex.setCityDomainUrl(city.getDomainURL());
        complaintIndex.setCityName(city.getName());
        complaintIndex.setCityRegionName(city.getRegionName());
        // Update current Functionary Complaint index variables
        complaintIndex
                .setCurrentFunctionaryName(
                        assignedUser != null ? assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName()
                                : NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName());
        complaintIndex.setCurrentFunctionaryMobileNumber(Objects.nonNull(assignedUser)
                ? assignedUser.getMobileNumber() : EMPTY);
        complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
        complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaint));
        complaintIndex = updateComplaintLevelIndexFields(complaintIndex);
        int escalationLevel = complaintIndex.getEscalationLevel();
        // For Escalation level1
        if (escalationLevel == 0) {
            complaintIndex.setEscalation1FunctionaryName(
                    assignedUser != null ? assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName()
                            : NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setEscalation1FunctionaryAssigneddate(new Date());
            complaintIndex.setEscalation1FunctionarySLADays(getFunctionarySlaDays(complaint));
            complaintIndex.setEscalation1FunctionaryAgeingFromDue(0);
            complaintIndex.setEscalation1FunctionaryIsSLA("Y");
            complaintIndex.setEscalation1FunctionaryIfSLA(1);
            complaintIndex.setEscalationLevel(++escalationLevel);
        } else if (escalationLevel == 1) {
            // update escalation level 2 fields
            complaintIndex.setEscalation2FunctionaryName(
                    assignedUser != null ? assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName()
                            : NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setEscalation2FunctionaryAssigneddate(new Date());
            complaintIndex.setEscalation2FunctionarySLADays(getFunctionarySlaDays(complaint));
            complaintIndex.setEscalation2FunctionaryAgeingFromDue(0);
            complaintIndex.setEscalation2FunctionaryIsSLA("Y");
            complaintIndex.setEscalation2FunctionaryIfSLA(1);
            complaintIndex.setEscalationLevel(++escalationLevel);
        } else if (escalationLevel == 2) {
            // update escalation level 3 fields
            complaintIndex.setEscalation3FunctionaryName(
                    assignedUser != null ? assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName()
                            : NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setEscalation3FunctionaryAssigneddate(new Date());
            complaintIndex.setEscalation3FunctionarySLADays(getFunctionarySlaDays(complaint));
            complaintIndex.setEscalation3FunctionaryAgeingFromDue(0);
            complaintIndex.setEscalation3FunctionaryIsSLA("Y");
            complaintIndex.setEscalation3FunctionaryIfSLA(1);
            complaintIndex.setEscalationLevel(++escalationLevel);
        }
        complaintIndex = updateEscalationLevelIndexFields(complaintIndex);
        // update status related fields in index
        complaintIndex = updateComplaintIndexStatusRelatedFields(complaintIndex);

        complaintIndexRepository.save(complaintIndex);
    }

    public void updateAllOpenComplaintIndex() {
        final List<Complaint> openComplaints = complaintService.getOpenComplaints();
        for (final Complaint complaint : openComplaints)
            updateOpenComplaintIndex(complaint);
    }

    public void updateOpenComplaintIndex(final Complaint complaint) {
        // fetch the complaint from index and then update the new fields
        ComplaintIndex complaintIndex = complaintIndexRepository.findByCrnAndCityCode(complaint.getCrn(), getCityCode());
        beanMapperConfiguration.map(complaint, complaintIndex);

        complaintIndex = updateComplaintLevelIndexFields(complaintIndex);
        complaintIndex = updateEscalationLevelIndexFields(complaintIndex);
        // update status related fields in index
        complaintIndex = updateComplaintIndexStatusRelatedFields(complaintIndex);

        complaintIndexRepository.save(complaintIndex);
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
            complaintIndex.setComplaintAgeingdaysFromDue(0);
            complaintIndex.setIsSLA("Y");
            complaintIndex.setIfSLA(1);
        } else {
            final long ageingDuehours = Math.abs(currentDate.getTime() - lastDateToResolve.getTime()) / (60 * 60 * 1000);
            final long ageingDueDays = Math.abs(currentDate.getTime() - lastDateToResolve.getTime()) / (24 * 60 * 60 * 1000);
            complaintIndex.setComplaintAgeingFromDue(ageingDuehours);
            complaintIndex.setComplaintAgeingdaysFromDue(ageingDueDays);
            complaintIndex.setIsSLA("N");
            complaintIndex.setIfSLA(0);
        }

        // update the initial functionary level variables
        if (complaintIndex.getInitialFunctionaryAssigneddate() != null) {
            lastDateToResolve = DateUtils.addHours(complaintIndex.getInitialFunctionaryAssigneddate(),
                    (int) complaintIndex.getInitialFunctionarySLADays());
            // If difference is greater than 0 then initial functionary is within SLA
            if (lastDateToResolve.getTime() - new Date().getTime() >= 0) {
                complaintIndex.setInitialFunctionaryAgeingFromDue(0);
                complaintIndex.setInitialFunctionaryIsSLA("Y");
                complaintIndex.setInitialFunctionaryIfSLA(1);
            } else {
                final long initialFunctionaryAgeingDueHours = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
                        / (60 * 60 * 1000);
                complaintIndex.setInitialFunctionaryAgeingFromDue(initialFunctionaryAgeingDueHours);
                complaintIndex.setInitialFunctionaryIsSLA("N");
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
                complaintIndex.setCurrentFunctionaryIsSLA("Y");
                complaintIndex.setCurrentFunctionaryIfSLA(1);
            } else {
                final long currentFunctionaryAgeingDueHours = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
                        / (60 * 60 * 1000);
                complaintIndex.setCurrentFunctionaryAgeingFromDue(currentFunctionaryAgeingDueHours);
                complaintIndex.setCurrentFunctionaryIsSLA("N");
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
                complaintIndex.setEscalation1FunctionaryIsSLA("Y");
                complaintIndex.setEscalation1FunctionaryIfSLA(1);
            } else {
                final long escalation1FunctionaryAgeingDueHours = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
                        / (60 * 60 * 1000);
                complaintIndex.setEscalation1FunctionaryAgeingFromDue(escalation1FunctionaryAgeingDueHours);
                complaintIndex.setEscalation1FunctionaryIsSLA("N");
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
                complaintIndex.setEscalation2FunctionaryIsSLA("Y");
                complaintIndex.setEscalation2FunctionaryIfSLA(1);
            } else {
                final long escalation2FunctionaryAgeingDueHours = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
                        / (60 * 60 * 1000);
                complaintIndex.setEscalation2FunctionaryAgeingFromDue(escalation2FunctionaryAgeingDueHours);
                complaintIndex.setEscalation2FunctionaryIsSLA("N");
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
                complaintIndex.setEscalation3FunctionaryIsSLA("Y");
                complaintIndex.setEscalation3FunctionaryIfSLA(1);
            } else {
                final long escalation3FunctionaryAgeingDueHours = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
                        / (60 * 60 * 1000);
                complaintIndex.setEscalation3FunctionaryAgeingFromDue(escalation3FunctionaryAgeingDueHours);
                complaintIndex.setEscalation3FunctionaryIsSLA("N");
                complaintIndex.setEscalation3FunctionaryIfSLA(0);
            }
        }
        return complaintIndex;
    }

    private ComplaintIndex updateComplaintIndexStatusRelatedFields(final ComplaintIndex complaintIndex) {
        if (complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.PROCESSING.toString())
                || complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.FORWARDED.toString())
                || complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.REGISTERED.toString())) {
            complaintIndex.setInProcess(1);
            complaintIndex.setAddressed(0);
            complaintIndex.setRejected(0);
        }
        if (complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.COMPLETED.toString())
                || complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.WITHDRAWN.toString())) {
            complaintIndex.setInProcess(0);
            complaintIndex.setAddressed(1);
            complaintIndex.setRejected(0);
        }
        if (complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.REJECTED.toString())) {
            complaintIndex.setInProcess(0);
            complaintIndex.setAddressed(0);
            complaintIndex.setRejected(1);
        }
        if (complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.REOPENED.toString())) {
            complaintIndex.setInProcess(1);
            complaintIndex.setAddressed(0);
            complaintIndex.setRejected(0);
            complaintIndex.setReOpened(1);
        }
        return complaintIndex;
    }

    private long getFunctionarySlaDays(final Complaint complaint) {
        final Position position = complaint.getAssignee();
        final Designation designation = position.getDeptDesig().getDesignation();
        final Escalation complaintEscalation = escalationService
                .getEscalationBycomplaintTypeAndDesignation(complaint.getComplaintType().getId(), designation.getId());
        if (complaintEscalation != null)
            return complaintEscalation.getNoOfHrs();
        else
            return 0;
    }

    // These are the methods for dashboard api's written

    public Map<String, Object> getGrievanceReport(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        final String groupByField = ComplaintIndexAggregationBuilder.getAggregationGroupingField(complaintDashBoardRequest);
        final Map<String, SearchResponse> response = complaintIndexRepository.findAllGrievanceByFilter(
                complaintDashBoardRequest,
                getFilterQuery(complaintDashBoardRequest), groupByField);

        final SearchResponse consolidatedResponse = response.get("consolidatedResponse");
        final SearchResponse tableResponse = response.get("tableResponse");
        final HashMap<String, Object> result = new HashMap<>();
        final List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();
        final ValueCount totalCount = response.get("consolidatedResponse").getAggregations().get("countAggregation");
        result.put("TotalComplaint", totalCount.getValue());
        final Filter filter = consolidatedResponse.getAggregations().get("agg");
        final Avg averageAgeing = filter.getAggregations().get("AgeingInWeeks");
        result.put("AvgAgeingInWeeks", averageAgeing.getValue() / 7);
        Range satisfactionAverage = consolidatedResponse.getAggregations().get(EXCLUDE_ZERO);
        final Avg averageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations().get("satisfactionAverage");
        result.put("AvgCustomeSatisfactionIndex", averageSatisfaction.getValue());

        if (isNotBlank(complaintDashBoardRequest.getUlbCode())) {
            final CityIndex city = cityIndexService.findOne(complaintDashBoardRequest.getUlbCode());
            result.put(REGION_NAME, city.getRegionname());
            result.put(DISTRICT_NAME, city.getDistrictname());
            result.put(ULB_CODE, city.getCitycode());
            result.put(ULB_GRADE, city.getCitygrade());
            result.put(ULB_NAME, city.getName());
            result.put(DOMAIN_URL, city.getDomainurl());
        }

        // To get the count of closed and open complaints
        Terms terms = consolidatedResponse.getAggregations().get("closedCount");
        for (final Bucket bucket : terms.getBuckets())
            if (bucket.getKeyAsNumber().intValue() == 1)
                result.put("ClosedComplaints", bucket.getDocCount());
            else
                result.put("OpenComplaints", bucket.getDocCount());

        // To get the count of closed and open complaints
        terms = consolidatedResponse.getAggregations().get("slaCount");
        for (final Bucket bucket : terms.getBuckets())
            if (bucket.getKeyAsNumber().intValue() == 1)
                result.put("WithinSLACount", bucket.getDocCount());
            else
                result.put("OutSideSLACount", bucket.getDocCount());
        final Range currentYearCount = consolidatedResponse.getAggregations().get("currentYear");
        result.put("CYTDComplaint", currentYearCount.getBuckets().get(0).getDocCount());

        final Range todaysCount = consolidatedResponse.getAggregations().get("todaysComplaintCount");
        result.put("todaysComplaintsCount", todaysCount.getBuckets().get(0).getDocCount());

        // For Dynamic results based on grouping fields

        if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_WARDS)) {
            // Fetch ulblevel aggregation
            final Terms ulbTerms = tableResponse.getAggregations().get(ULBWISE);
            for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
                terms = ulbBucket.getAggregations().get(GROUP_BY_FIELD);
                for (final Bucket bucket : terms.getBuckets()) {
                    final ComplaintDashBoardResponse responseDetail = populateResponse(complaintDashBoardRequest, bucket,
                            groupByField);
                    responseDetail.setTotalComplaintCount(bucket.getDocCount());

                    final TopHits topHits = bucket.getAggregations().get(COMPLAINTRECORD);
                    final SearchHit[] hit = topHits.getHits().getHits();
                    responseDetail.setUlbCode(hit[0].field(CITY_CODE).getValue());
                    responseDetail.setUlbName(hit[0].field(CITY_NAME).getValue());
                    responseDetail.setDistrictName(hit[0].field(CITY_DISTRICT_NAME).getValue());
                    responseDetail.setWardName(hit[0].field(WARD_NAME).getValue());
                    satisfactionAverage = bucket.getAggregations().get(EXCLUDE_ZERO);
                    final Avg groupByFieldAverageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations()
                            .get(GROUP_BY_FIELD_SATISFACTION_AVERAGE);
                    if (Double.isNaN(groupByFieldAverageSatisfaction.getValue()))
                        responseDetail.setAvgSatisfactionIndex(0);
                    else
                        responseDetail.setAvgSatisfactionIndex(groupByFieldAverageSatisfaction.getValue());

                    final Terms openAndClosedTerms = bucket.getAggregations().get(GROUP_FIELDWISE_OPEN_AND_CLOSED_COUNT);
                    for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                        if (closedCountbucket.getKeyAsNumber().intValue() == 1) {
                            responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                            final Terms slaTerms = closedCountbucket.getAggregations().get(GROUP_BY_FIELD_SLA);
                            for (final Bucket slaBucket : slaTerms.getBuckets())
                                if (slaBucket.getKeyAsNumber().intValue() == 1)
                                    responseDetail.setClosedWithinSLACount(slaBucket.getDocCount());
                                else
                                    responseDetail.setClosedOutSideSLACount(slaBucket.getDocCount());
                            // To set Ageing Buckets Result
                            setAgeingResults(responseDetail, closedCountbucket, GROUP_BY_FIELD_AGEING, GROUP_BY_FIELD_AGEING_FOR_HOURS);
                        } else {
                            responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());
                            final Terms slaTerms = closedCountbucket.getAggregations().get(GROUP_BY_FIELD_SLA);
                            for (final Bucket slaBucket : slaTerms.getBuckets())
                                if (slaBucket.getKeyAsNumber().intValue() == 1)
                                    responseDetail.setOpenWithinSLACount(slaBucket.getDocCount());
                                else
                                    responseDetail.setOpenOutSideSLACount(slaBucket.getDocCount());
                        }
                    responseDetailsList.add(responseDetail);
                }
            }
        } else if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_LOCALITIES)) {
            // Fetch ulblevel aggregation
            final Terms ulbTerms = tableResponse.getAggregations().get(ULBWISE);
            for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
                final Terms wardTerms = ulbBucket.getAggregations().get(WARDWISE);
                for (final Bucket wardBucket : wardTerms.getBuckets()) {
                    terms = wardBucket.getAggregations().get(GROUP_BY_FIELD);
                    for (final Bucket bucket : terms.getBuckets()) {
                        final ComplaintDashBoardResponse responseDetail = populateResponse(complaintDashBoardRequest, bucket,
                                groupByField);
                        responseDetail.setTotalComplaintCount(bucket.getDocCount());
                        final TopHits topHits = bucket.getAggregations().get(COMPLAINTRECORD);
                        final SearchHit[] hit = topHits.getHits().getHits();
                        responseDetail.setUlbCode(hit[0].field(CITY_CODE).getValue());
                        responseDetail.setUlbName(hit[0].field(CITY_NAME).getValue());
                        responseDetail.setDistrictName(hit[0].field(CITY_DISTRICT_NAME).getValue());
                        responseDetail.setWardName(hit[0].field(WARD_NAME).getValue());
                        responseDetail.setLocalityName(hit[0].field(LOCALITY_NAME).getValue());
                        satisfactionAverage = bucket.getAggregations().get(EXCLUDE_ZERO);
                        final Avg groupByFieldAverageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations()
                                .get(GROUP_BY_FIELD_SATISFACTION_AVERAGE);
                        if (Double.isNaN(groupByFieldAverageSatisfaction.getValue()))
                            responseDetail.setAvgSatisfactionIndex(0);
                        else
                            responseDetail.setAvgSatisfactionIndex(groupByFieldAverageSatisfaction.getValue());

                        final Terms openAndClosedTerms = bucket.getAggregations().get(GROUP_FIELDWISE_OPEN_AND_CLOSED_COUNT);
                        for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                            if (closedCountbucket.getKeyAsNumber().intValue() == 1) {
                                responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                                final Terms slaTerms = closedCountbucket.getAggregations().get(GROUP_BY_FIELD_SLA);
                                for (final Bucket slaBucket : slaTerms.getBuckets())
                                    if (slaBucket.getKeyAsNumber().intValue() == 1)
                                        responseDetail.setClosedWithinSLACount(slaBucket.getDocCount());
                                    else
                                        responseDetail.setClosedOutSideSLACount(slaBucket.getDocCount());
                                // To set Ageing Buckets Result
                                setAgeingResults(responseDetail, closedCountbucket, GROUP_BY_FIELD_AGEING, GROUP_BY_FIELD_AGEING_FOR_HOURS);
                            } else {
                                responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());
                                final Terms slaTerms = closedCountbucket.getAggregations().get(GROUP_BY_FIELD_SLA);
                                for (final Bucket slaBucket : slaTerms.getBuckets())
                                    if (slaBucket.getKeyAsNumber().intValue() == 1)
                                        responseDetail.setOpenWithinSLACount(slaBucket.getDocCount());
                                    else
                                        responseDetail.setOpenOutSideSLACount(slaBucket.getDocCount());
                            }
                        responseDetailsList.add(responseDetail);
                    }
                }
            }
        } else if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) {
            // Fetch ulblevel aggregation
            final Terms ulbTerms = tableResponse.getAggregations().get(ULBWISE);
            for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
                final Terms deptTerms = ulbBucket.getAggregations().get(DEPARTMENTWISE);
                for (final Bucket deptBucket : deptTerms.getBuckets()) {
                    terms = deptBucket.getAggregations().get(GROUP_BY_FIELD);
                    for (final Bucket bucket : terms.getBuckets()) {
                        final ComplaintDashBoardResponse responseDetail = populateResponse(complaintDashBoardRequest, bucket,
                                groupByField);
                        responseDetail.setTotalComplaintCount(bucket.getDocCount());
                        final TopHits topHits = bucket.getAggregations().get(COMPLAINTRECORD);
                        final SearchHit[] hit = topHits.getHits().getHits();
                        responseDetail.setUlbCode(hit[0].field(CITY_CODE).getValue());
                        responseDetail.setUlbName(hit[0].field(CITY_NAME).getValue());
                        responseDetail.setDistrictName(hit[0].field(CITY_DISTRICT_NAME).getValue());
                        responseDetail.setDepartmentName(hit[0].field(DEPARTMENT_NAME).getValue());
                        if (hit[0].field(INITIAL_FUNCTIONARY_MOBILE_NUMBER) != null)
                            responseDetail.setFunctionaryMobileNumber(hit[0].field(INITIAL_FUNCTIONARY_MOBILE_NUMBER).getValue());
                        responseDetail.setFunctionaryName(bucket.getKeyAsString());
                        satisfactionAverage = bucket.getAggregations().get(EXCLUDE_ZERO);
                        final Avg groupByFieldAverageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations()
                                .get(GROUP_BY_FIELD_SATISFACTION_AVERAGE);
                        if (Double.isNaN(groupByFieldAverageSatisfaction.getValue()))
                            responseDetail.setAvgSatisfactionIndex(0);
                        else
                            responseDetail.setAvgSatisfactionIndex(groupByFieldAverageSatisfaction.getValue());

                        final Terms openAndClosedTerms = bucket.getAggregations().get(GROUP_FIELDWISE_OPEN_AND_CLOSED_COUNT);
                        for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                            if (closedCountbucket.getKeyAsNumber().intValue() == 1) {
                                responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                                final Terms slaTerms = closedCountbucket.getAggregations().get(GROUP_BY_FIELD_SLA);
                                for (final Bucket slaBucket : slaTerms.getBuckets())
                                    if (slaBucket.getKeyAsNumber().intValue() == 1)
                                        responseDetail.setClosedWithinSLACount(slaBucket.getDocCount());
                                    else
                                        responseDetail.setClosedOutSideSLACount(slaBucket.getDocCount());
                                // To set Ageing Buckets Result
                                setAgeingResults(responseDetail, closedCountbucket, GROUP_BY_FIELD_AGEING, GROUP_BY_FIELD_AGEING_FOR_HOURS);
                            } else {
                                responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());
                                final Terms slaTerms = closedCountbucket.getAggregations().get(GROUP_BY_FIELD_SLA);
                                for (final Bucket slaBucket : slaTerms.getBuckets())
                                    if (slaBucket.getKeyAsNumber().intValue() == 1)
                                        responseDetail.setOpenWithinSLACount(slaBucket.getDocCount());
                                    else
                                        responseDetail.setOpenOutSideSLACount(slaBucket.getDocCount());
                            }
                        responseDetailsList.add(responseDetail);
                    }
                }
            }
        } else {
            terms = tableResponse.getAggregations().get(GROUP_BY_FIELD);
            for (final Bucket bucket : terms.getBuckets()) {
                final ComplaintDashBoardResponse responseDetail = populateResponse(complaintDashBoardRequest, bucket,
                        groupByField);
                responseDetail.setTotalComplaintCount(bucket.getDocCount());

                satisfactionAverage = bucket.getAggregations().get(EXCLUDE_ZERO);
                final Avg groupByFieldAverageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations()
                        .get(GROUP_BY_FIELD_SATISFACTION_AVERAGE);
                if (Double.isNaN(groupByFieldAverageSatisfaction.getValue()))
                    responseDetail.setAvgSatisfactionIndex(0);
                else
                    responseDetail.setAvgSatisfactionIndex(groupByFieldAverageSatisfaction.getValue());

                final Terms openAndClosedTerms = bucket.getAggregations().get(GROUP_FIELDWISE_OPEN_AND_CLOSED_COUNT);
                for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                    if (closedCountbucket.getKeyAsNumber().intValue() == 1) {
                        responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                        final Terms slaTerms = closedCountbucket.getAggregations().get(GROUP_BY_FIELD_SLA);
                        for (final Bucket slaBucket : slaTerms.getBuckets())
                            if (slaBucket.getKeyAsNumber().intValue() == 1)
                                responseDetail.setClosedWithinSLACount(slaBucket.getDocCount());
                            else
                                responseDetail.setClosedOutSideSLACount(slaBucket.getDocCount());
                        // To set Ageing Buckets Result
                        setAgeingResults(responseDetail, closedCountbucket, GROUP_BY_FIELD_AGEING, GROUP_BY_FIELD_AGEING_FOR_HOURS);
                    } else {
                        responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());
                        final Terms slaTerms = closedCountbucket.getAggregations().get(GROUP_BY_FIELD_SLA);
                        for (final Bucket slaBucket : slaTerms.getBuckets())
                            if (slaBucket.getKeyAsNumber().intValue() == 1)
                                responseDetail.setOpenWithinSLACount(slaBucket.getDocCount());
                            else
                                responseDetail.setOpenOutSideSLACount(slaBucket.getDocCount());
                    }
                responseDetailsList.add(responseDetail);
            }
        }

        if (groupByField.equals(LOCALITY_NAME)) {
            final SearchResponse localityMissingResponse = response.get("noLocality");
            final Missing noLocalityTerms = localityMissingResponse.getAggregations().get(NOLOCALITY);
            final ComplaintDashBoardResponse responseDetail = new ComplaintDashBoardResponse();
            responseDetail.setLocalityName(NA);
            responseDetail.setTotalComplaintCount(noLocalityTerms.getDocCount());
            satisfactionAverage = noLocalityTerms.getAggregations().get(EXCLUDE_ZERO);
            final Avg groupByFieldAverageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations()
                    .get(GROUP_BY_FIELD_SATISFACTION_AVERAGE);
            if (Double.isNaN(groupByFieldAverageSatisfaction.getValue()))
                responseDetail.setAvgSatisfactionIndex(0);
            else
                responseDetail.setAvgSatisfactionIndex(groupByFieldAverageSatisfaction.getValue());

            final Terms openAndClosedTerms = noLocalityTerms.getAggregations().get(GROUP_FIELDWISE_OPEN_AND_CLOSED_COUNT);
            for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                if (closedCountbucket.getKeyAsNumber().intValue() == 1) {
                    responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                    final Terms slaTerms = closedCountbucket.getAggregations().get(GROUP_BY_FIELD_SLA);
                    for (final Bucket slaBucket : slaTerms.getBuckets())
                        if (slaBucket.getKeyAsNumber().intValue() == 1)
                            responseDetail.setClosedWithinSLACount(slaBucket.getDocCount());
                        else
                            responseDetail.setClosedOutSideSLACount(slaBucket.getDocCount());
                    // To set Ageing Buckets Result
                    setAgeingResults(responseDetail, closedCountbucket, GROUP_BY_FIELD_AGEING, GROUP_BY_FIELD_AGEING_FOR_HOURS);
                } else {
                    responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());
                    final Terms slaTerms = closedCountbucket.getAggregations().get(GROUP_BY_FIELD_SLA);
                    for (final Bucket slaBucket : slaTerms.getBuckets())
                        if (slaBucket.getKeyAsNumber().intValue() == 1)
                            responseDetail.setOpenWithinSLACount(slaBucket.getDocCount());
                        else
                            responseDetail.setOpenOutSideSLACount(slaBucket.getDocCount());
                }
            responseDetailsList.add(responseDetail);
        }

        result.put(RESPONSE_DETAILS, responseDetailsList);

        final List<ComplaintDashBoardResponse> complaintTypeList = new ArrayList<>();
        // For complaintTypeWise result
        terms = tableResponse.getAggregations().get(COMPLAINT_TYPEWISE);
        for (final Bucket bucket : terms.getBuckets()) {
            ComplaintDashBoardResponse complaintType = new ComplaintDashBoardResponse();
            complaintType = setDefaultValues(complaintType);
            complaintType.setComplaintTypeName(bucket.getKey().toString());
            complaintType.setTotalComplaintCount(bucket.getDocCount());

            satisfactionAverage = bucket.getAggregations().get(EXCLUDE_ZERO);
            final Avg complaintTypeAverageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations()
                    .get("complaintTypeSatisfactionAverage");
            if (Double.isNaN(complaintTypeAverageSatisfaction.getValue()))
                complaintType.setAvgSatisfactionIndex(0);
            else
                complaintType.setAvgSatisfactionIndex(complaintTypeAverageSatisfaction.getValue());

            final Terms openAndClosedTerms = bucket.getAggregations().get("complaintTypeWiseOpenAndClosedCount");
            for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                if (closedCountbucket.getKeyAsNumber().intValue() == 1) {
                    complaintType.setClosedComplaintCount(closedCountbucket.getDocCount());
                    final Terms slaTerms = closedCountbucket.getAggregations().get("complaintTypeSla");
                    for (final Bucket slaBucket : slaTerms.getBuckets())
                        if (slaBucket.getKeyAsNumber().intValue() == 1)
                            complaintType.setClosedWithinSLACount(slaBucket.getDocCount());
                        else
                            complaintType.setClosedOutSideSLACount(slaBucket.getDocCount());

                    // To set Ageing Buckets Result
                    setAgeingResults(complaintType, closedCountbucket, "ComplaintTypeAgeing", "hourwiseComplaintTypeAgeing");
                } else {
                    complaintType.setOpenComplaintCount(closedCountbucket.getDocCount());
                    final Terms slaTerms = closedCountbucket.getAggregations().get("complaintTypeSla");
                    for (final Bucket slaBucket : slaTerms.getBuckets())
                        if (slaBucket.getKeyAsNumber().intValue() == 1)
                            complaintType.setOpenWithinSLACount(slaBucket.getDocCount());
                        else
                            complaintType.setOpenOutSideSLACount(slaBucket.getDocCount());
                }
            complaintTypeList.add(complaintType);
        }
        result.put("complaintTypes", complaintTypeList);

        return result;
    }

    private void setAgeingResults(ComplaintDashBoardResponse responseDetail, Bucket closedCountbucket, String weeklyAggregation,
                                  String hourlyAggregation) {
        Range ageingRange = closedCountbucket.getAggregations().get(weeklyAggregation);
        Range.Bucket rangeBucket = ageingRange.getBuckets().get(0);
        responseDetail.setAgeingGroup1(rangeBucket.getDocCount());
        rangeBucket = ageingRange.getBuckets().get(1);
        responseDetail.setAgeingGroup2(rangeBucket.getDocCount());
        rangeBucket = ageingRange.getBuckets().get(2);
        responseDetail.setAgeingGroup3(rangeBucket.getDocCount());
        rangeBucket = ageingRange.getBuckets().get(3);
        responseDetail.setAgeingGroup4(rangeBucket.getDocCount());
        Range hourlyAgeingRange = closedCountbucket.getAggregations().get(hourlyAggregation);
        Range.Bucket hourlyRangeBucket = hourlyAgeingRange.getBuckets().get(0);
        responseDetail.setAgeingGroup5(hourlyRangeBucket.getDocCount());
        hourlyRangeBucket = hourlyAgeingRange.getBuckets().get(1);
        responseDetail.setAgeingGroup6(hourlyRangeBucket.getDocCount());
        hourlyRangeBucket = hourlyAgeingRange.getBuckets().get(2);
        responseDetail.setAgeingGroup7(hourlyRangeBucket.getDocCount());
        hourlyRangeBucket = hourlyAgeingRange.getBuckets().get(3);
        responseDetail.setAgeingGroup8(hourlyRangeBucket.getDocCount());
    }

    public Map<String, Object> getComplaintTypeReport(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        final String groupByField = ComplaintIndexAggregationBuilder.getAggregationGroupingField(complaintDashBoardRequest);
        final Map<String, SearchResponse> complaintTypeResponse = complaintIndexRepository.findAllGrievanceByComplaintType(
                complaintDashBoardRequest,
                getFilterQuery(complaintDashBoardRequest),
                groupByField);

        final HashMap<String, Object> result = new HashMap<>();
        if (isNotBlank(complaintDashBoardRequest.getUlbCode())) {
            final CityIndex city = cityIndexService.findOne(complaintDashBoardRequest.getUlbCode());
            result.put(REGION_NAME, city.getRegionname());
            result.put(DISTRICT_NAME, city.getDistrictname());
            result.put(ULB_CODE, city.getCitycode());
            result.put(ULB_GRADE, city.getCitygrade());
            result.put(ULB_NAME, city.getName());
            result.put(DOMAIN_URL, city.getDomainurl());
        }
        final List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();

        // For Dynamic results based on grouping fields
        final Terms terms = complaintTypeResponse.get("tableResponse").getAggregations().get(GROUP_BY_FIELD);
        for (final Bucket bucket : terms.getBuckets()) {

            final ComplaintDashBoardResponse responseDetail = populateResponse(complaintDashBoardRequest, bucket, groupByField);
            responseDetail.setTotalComplaintCount(bucket.getDocCount());

            final Terms openAndClosedTerms = bucket.getAggregations().get(CLOSED_COMPLAINT_COUNT);
            for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                if (closedCountbucket.getKeyAsNumber().intValue() == 1)
                    responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                else
                    responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());

            final Terms reOpenedComplaints = bucket.getAggregations().get(RE_OPENED_COMPLAINT_COUNT);
            for (final Bucket reOpenedCountbucket : reOpenedComplaints.getBuckets())
                if (reOpenedCountbucket.getKeyAsNumber().intValue() == 1)
                    responseDetail.setReOpenedComplaintCount(reOpenedCountbucket.getDocCount());

            responseDetailsList.add(responseDetail);
        }

        //For other localities in drill down
        if (groupByField.equals(LOCALITY_NAME)) {
            final SearchResponse localityMissingResponse = complaintTypeResponse.get("otherLocalities");
            final Missing noLocalityTerms = localityMissingResponse.getAggregations().get(NOLOCALITY);
            final ComplaintDashBoardResponse responseDetail = new ComplaintDashBoardResponse();
            responseDetail.setTotalComplaintCount(noLocalityTerms.getDocCount());
            responseDetail.setLocalityName("Others");
            final Terms openAndClosedTerms = noLocalityTerms.getAggregations().get(CLOSED_COMPLAINT_COUNT);
            for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                if (closedCountbucket.getKeyAsNumber().intValue() == 1)
                    responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                else
                    responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());
            final Terms reOpenedComplaints = noLocalityTerms.getAggregations().get(RE_OPENED_COMPLAINT_COUNT);
            for (final Bucket reOpenedCountbucket : reOpenedComplaints.getBuckets())
                if (reOpenedCountbucket.getKeyAsNumber().intValue() == 1)
                    responseDetail.setReOpenedComplaintCount(reOpenedCountbucket.getDocCount());

            responseDetailsList.add(responseDetail);
        }
        result.put(COMPLAINTS, responseDetailsList);
        return result;
    }

    // This method is used to return all functionary details response
    public Map<String, Object> getAllFunctionaryResponse(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        final SearchResponse complaintTypeResponse = complaintIndexRepository.findByAllFunctionary(complaintDashBoardRequest,
                getFilterQuery(complaintDashBoardRequest));
        final HashMap<String, Object> result = new HashMap<>();
        final List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();
        // Fetch ulblevel aggregation
        final Terms ulbTerms = complaintTypeResponse.getAggregations().get(ULBWISE);
        for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
            final Terms departmentTerms = ulbBucket.getAggregations().get(DEPARTMENTWISE);
            // Fetch departmentLevel data in each ulb
            for (final Bucket departmentBucket : departmentTerms.getBuckets()) {
                final Terms functionaryTerms = departmentBucket.getAggregations().get("functionarywise");
                // Fetch functionaryLevel data in each department
                for (final Bucket functionaryBucket : functionaryTerms.getBuckets()) {
                    final ComplaintDashBoardResponse responseDetail = new ComplaintDashBoardResponse();
                    responseDetail.setTotalComplaintCount(functionaryBucket.getDocCount());
                    responseDetail.setFunctionaryName(functionaryBucket.getKeyAsString());

                    final TopHits topHits = functionaryBucket.getAggregations().get(COMPLAINTRECORD);
                    final SearchHit[] hit = topHits.getHits().getHits();
                    responseDetail.setUlbCode(hit[0].field(CITY_CODE).getValue());
                    responseDetail.setUlbName(hit[0].field(CITY_NAME).getValue());
                    responseDetail.setDistrictName(hit[0].field(CITY_DISTRICT_NAME).getValue());
                    responseDetail.setDepartmentName(hit[0].field(DEPARTMENT_NAME).getValue());
                    String initialFunctionaryNumber;
                    if (hit[0].field(INITIAL_FUNCTIONARY_MOBILE_NUMBER) == null)
                        initialFunctionaryNumber = NA;
                    else
                        initialFunctionaryNumber = hit[0].field(INITIAL_FUNCTIONARY_MOBILE_NUMBER).getValue();
                    responseDetail.setFunctionaryMobileNumber(initialFunctionaryNumber);

                    final Terms openAndClosedTerms = functionaryBucket.getAggregations().get(CLOSED_COMPLAINT_COUNT);
                    for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                        if (closedCountbucket.getKeyAsNumber().intValue() == 1)
                            responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                        else
                            responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());

                    final Terms reOpenedComplaints = functionaryBucket.getAggregations().get(RE_OPENED_COMPLAINT_COUNT);
                    for (final Bucket reOpenedCountbucket : reOpenedComplaints.getBuckets())
                        if (reOpenedCountbucket.getKeyAsNumber().intValue() == 1)
                            responseDetail.setReOpenedComplaintCount(reOpenedCountbucket.getDocCount());

                    responseDetailsList.add(responseDetail);
                }
            }
        }
        result.put(COMPLAINTS, responseDetailsList);
        return result;
    }

    // This method is used to return all ulb details response in complaits dashboard
    public Map<String, Object> getAllUlbResponse(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        final SearchResponse ulbWiseResponse = complaintIndexRepository.findByAllUlb(complaintDashBoardRequest,
                getFilterQuery(complaintDashBoardRequest));

        final HashMap<String, Object> result = new HashMap<>();
        final List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();
        // Fetch ulblevel aggregation
        final Terms ulbTerms = ulbWiseResponse.getAggregations().get(ULBWISE);
        for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
            final ComplaintDashBoardResponse responseDetail = new ComplaintDashBoardResponse();
            responseDetail.setTotalComplaintCount(ulbBucket.getDocCount());
            final TopHits topHits = ulbBucket.getAggregations().get(COMPLAINTRECORD);
            final SearchHit[] hit = topHits.getHits().getHits();
            responseDetail.setUlbCode(hit[0].field(CITY_CODE).getValue());
            responseDetail.setUlbName(hit[0].field(CITY_NAME).getValue());
            responseDetail.setDistrictName(hit[0].field(CITY_DISTRICT_NAME).getValue());
            final Terms openAndClosedTerms = ulbBucket.getAggregations().get(COMPLAINT_COUNT);
            for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                if (closedCountbucket.getKeyAsNumber().intValue() == 1)
                    responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                else
                    responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());

            final Terms reOpenedComplaints = ulbBucket.getAggregations().get(RE_OPENED_COMPLAINT_COUNT);
            for (final Bucket reOpenedCountbucket : reOpenedComplaints.getBuckets())
                if (reOpenedCountbucket.getKeyAsNumber().intValue() == 1)
                    responseDetail.setReOpenedComplaintCount(reOpenedCountbucket.getDocCount());

            responseDetailsList.add(responseDetail);
        }
        result.put(COMPLAINTS, responseDetailsList);
        return result;
    }

    // This method is used to return all ulb details response in complaits dashboard
    public Map<String, Object> getAllWardResponse(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        final SearchResponse ulbWiseResponse = complaintIndexRepository.findBYAllWards(complaintDashBoardRequest,
                getFilterQuery(complaintDashBoardRequest));

        final HashMap<String, Object> result = new HashMap<>();
        final List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();
        // Fetch ulblevel aggregation
        final Terms ulbTerms = ulbWiseResponse.getAggregations().get(ULBWISE);
        for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
            // Fetch wardlevel aggregation
            final Terms wardTerms = ulbBucket.getAggregations().get(WARDWISE);
            for (final Bucket wardBucket : wardTerms.getBuckets()) {
                final ComplaintDashBoardResponse responseDetail = new ComplaintDashBoardResponse();
                responseDetail.setTotalComplaintCount(wardBucket.getDocCount());
                final TopHits topHits = wardBucket.getAggregations().get(COMPLAINTRECORD);
                final SearchHit[] hit = topHits.getHits().getHits();
                responseDetail.setUlbCode(hit[0].field(CITY_CODE).getValue());
                responseDetail.setUlbName(hit[0].field(CITY_NAME).getValue());
                responseDetail.setDistrictName(hit[0].field(CITY_DISTRICT_NAME).getValue());
                responseDetail.setWardName(hit[0].field(WARD_NAME).getValue());
                final Terms openAndClosedTerms = wardBucket.getAggregations().get(COMPLAINT_COUNT);
                for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                    if (closedCountbucket.getKeyAsNumber().intValue() == 1)
                        responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                    else
                        responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());

                final Terms reOpenedComplaints = wardBucket.getAggregations().get(RE_OPENED_COMPLAINT_COUNT);
                for (final Bucket reOpenedCountbucket : reOpenedComplaints.getBuckets())
                    if (reOpenedCountbucket.getKeyAsNumber().intValue() == 1)
                        responseDetail.setReOpenedComplaintCount(reOpenedCountbucket.getDocCount());

                responseDetailsList.add(responseDetail);
            }
        }
        result.put(COMPLAINTS, responseDetailsList);
        return result;
    }

    // This method is used to return all locality details response in complaits dashboard
    public Map<String, Object> getAllLocalityResponse(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        final SearchResponse localityWiseResponse = complaintIndexRepository.findBYAllLocalities(complaintDashBoardRequest,
                getFilterQuery(complaintDashBoardRequest));

        final HashMap<String, Object> result = new HashMap<>();
        final List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();

        // Fetch ulblevel aggregation
        final Terms ulbTerms = localityWiseResponse.getAggregations().get(ULBWISE);
        for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
            // Fetch wardlevel aggregation
            final Terms wardTerms = ulbBucket.getAggregations().get(WARDWISE);
            for (final Bucket wardBucket : wardTerms.getBuckets()) {
                final Terms localityTerms = wardBucket.getAggregations().get("localitywise");
                for (final Bucket localityBucket : localityTerms.getBuckets()) {
                    final ComplaintDashBoardResponse responseDetail = new ComplaintDashBoardResponse();
                    responseDetail.setTotalComplaintCount(localityBucket.getDocCount());
                    final TopHits topHits = localityBucket.getAggregations().get(COMPLAINTRECORD);
                    final SearchHit[] hit = topHits.getHits().getHits();
                    responseDetail.setUlbCode(hit[0].field(CITY_CODE).getValue());
                    responseDetail.setUlbName(hit[0].field(CITY_NAME).getValue());
                    responseDetail.setDistrictName(hit[0].field(CITY_DISTRICT_NAME).getValue());
                    responseDetail.setWardName(hit[0].field(WARD_NAME).getValue());
                    responseDetail.setLocalityName(hit[0].field(LOCALITY_NAME).getValue());
                    final Terms openAndClosedTerms = localityBucket.getAggregations().get(COMPLAINT_COUNT);
                    for (final Bucket closedCountbucket : openAndClosedTerms.getBuckets())
                        if (closedCountbucket.getKeyAsNumber().intValue() == 1)
                            responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
                        else
                            responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());

                    final Terms reOpenedComplaints = wardBucket.getAggregations().get(RE_OPENED_COMPLAINT_COUNT);
                    for (final Bucket reOpenedCountbucket : reOpenedComplaints.getBuckets())
                        if (reOpenedCountbucket.getKeyAsNumber().intValue() == 1)
                            responseDetail.setReOpenedComplaintCount(reOpenedCountbucket.getDocCount());
                    responseDetailsList.add(responseDetail);
                }
            }
        }
        final Missing noLocalityTerms = localityWiseResponse.getAggregations().get(NOLOCALITY);
        final ComplaintDashBoardResponse responseDetail = new ComplaintDashBoardResponse();
        responseDetail.setTotalComplaintCount(noLocalityTerms.getDocCount());
        responseDetail.setLocalityName(NA);
        final Terms openAndClosedComplaintsCount = noLocalityTerms.getAggregations().get("noLocalityComplaintCount");
        for (final Bucket noLocalityCountBucket : openAndClosedComplaintsCount.getBuckets())
            if (noLocalityCountBucket.getKeyAsNumber().intValue() == 1)
                responseDetail.setClosedComplaintCount(noLocalityCountBucket.getDocCount());
            else
                responseDetail.setOpenComplaintCount(noLocalityCountBucket.getDocCount());
        responseDetailsList.add(responseDetail);

        result.put(COMPLAINTS, responseDetailsList);
        return result;
    }

    public Map<String, Object> getSourceWiseResponse(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        final String groupByField = ComplaintIndexAggregationBuilder.getAggregationGroupingField(complaintDashBoardRequest);
        final SearchResponse sourceWiseResponse = complaintIndexRepository.findAllGrievanceBySource(complaintDashBoardRequest,
                getFilterQuery(complaintDashBoardRequest), groupByField);

        final HashMap<String, Object> result = new HashMap<>();
        List<ComplaintSourceResponse> responseDetailsList = new ArrayList<>();

        if (isNotBlank(complaintDashBoardRequest.getUlbCode())) {
            final CityIndex city = cityIndexService.findOne(complaintDashBoardRequest.getUlbCode());
            result.put(REGION_NAME, city.getRegionname());
            result.put(DISTRICT_NAME, city.getDistrictname());
            result.put(ULB_CODE, city.getCitycode());
            result.put(ULB_GRADE, city.getCitygrade());
            result.put(ULB_NAME, city.getName());
            result.put(DOMAIN_URL, city.getDomainurl());
        }
        if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_WARDS)) {
            final Terms ulbTerms = sourceWiseResponse.getAggregations().get(ULBWISE);
            for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
                final Terms terms = ulbBucket.getAggregations().get(GROUP_BY_FIELD);

                responseDetailsList = getResponseDetailsList(groupByField, terms, responseDetailsList, complaintDashBoardRequest);
            }

        } else if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_LOCALITIES)) {
            final Terms ulbTerms = sourceWiseResponse.getAggregations().get(ULBWISE);
            for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
                final Terms wardTerms = ulbBucket.getAggregations().get(WARDWISE);
                for (final Bucket wardBucket : wardTerms.getBuckets()) {
                    final Terms terms = wardBucket.getAggregations().get(GROUP_BY_FIELD);
                    responseDetailsList = getResponseDetailsList(groupByField, terms, responseDetailsList,
                            complaintDashBoardRequest);
                }

            }
        } else if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) {
            final Terms ulbTerms = sourceWiseResponse.getAggregations().get(ULBWISE);
            for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
                final Terms deptTerms = ulbBucket.getAggregations().get(DEPARTMENTWISE);
                for (final Bucket deptBucket : deptTerms.getBuckets()) {
                    final Terms terms = deptBucket.getAggregations().get(GROUP_BY_FIELD);
                    responseDetailsList = getResponseDetailsList(groupByField, terms, responseDetailsList,
                            complaintDashBoardRequest);
                }
            }
        } else {
            final Terms terms = sourceWiseResponse.getAggregations().get(GROUP_BY_FIELD);
            responseDetailsList = getResponseDetailsList(groupByField, terms, responseDetailsList, complaintDashBoardRequest);
        }

        result.put(RESPONSE_DETAILS, responseDetailsList);

        final List<ComplaintSourceResponse> complaintTypeList = new ArrayList<>();
        final Terms terms = sourceWiseResponse.getAggregations().get(COMPLAINT_TYPEWISE);
        for (final Bucket bucket : terms.getBuckets()) {
            final ComplaintSourceResponse complaintSouce = new ComplaintSourceResponse();
            complaintSouce.setComplaintTypeName(bucket.getKey().toString());
            final List<HashMap<String, Long>> list = new ArrayList<>();
            final Terms sourceTerms = bucket.getAggregations().get("complaintTypeWiseSource");
            for (final Bucket sourceBucket : sourceTerms.getBuckets()) {
                final HashMap<String, Long> sourceMap = new HashMap<>();
                sourceMap.put(sourceBucket.getKeyAsString(), sourceBucket.getDocCount());
                list.add(sourceMap);
            }
            complaintSouce.setSourceList(list);
            complaintTypeList.add(complaintSouce);
        }

        result.put(COMPLAINT_TYPEWISE, complaintTypeList);

        return result;

    }

    private List<ComplaintSourceResponse> getResponseDetailsList(final String groupByField, final Terms terms,
                                                                 final List<ComplaintSourceResponse> responseDetailsList, final ComplaintDashBoardRequest complaintDashBoardRequest) {
        for (final Bucket bucket : terms.getBuckets()) {
            final ComplaintSourceResponse complaintSouce = new ComplaintSourceResponse();
            final TopHits topHits = bucket.getAggregations().get(COMPLAINTRECORD);
            final SearchHit[] hit = topHits.getHits().getHits();
            complaintSouce.setUlbName(hit[0].field(CITY_NAME).getValue());
            complaintSouce.setDistrictName(hit[0].field(CITY_DISTRICT_NAME).getValue());
            complaintSouce.setWardName(hit[0].field(WARD_NAME).getValue());
            if (hit[0].field(DEPARTMENT_NAME) != null) {
                complaintSouce.setDepartmentName(hit[0].field(DEPARTMENT_NAME).getValue());
            }
            if (hit[0].field(INITIAL_FUNCTIONARY_NAME) != null)
                complaintSouce.setFunctionaryName(hit[0].field(INITIAL_FUNCTIONARY_NAME).getValue());
            if (hit[0].field(INITIAL_FUNCTIONARY_MOBILE_NUMBER) != null)
                complaintSouce.setFunctionaryMobileNumber(hit[0].field(INITIAL_FUNCTIONARY_MOBILE_NUMBER).getValue());
            CityIndex city;
            if (CITY_REGION_NAME.equals(groupByField))
                complaintSouce.setRegionName(bucket.getKeyAsString());
            if (CITY_GRADE.equals(groupByField))
                complaintSouce.setUlbGrade(bucket.getKeyAsString());
            if (CITY_DISTRICT_CODE.equals(groupByField)) {
                city = cityIndexService.findByDistrictCode(bucket.getKeyAsString());
                complaintSouce.setDistrictName(city.getDistrictname());
            }
            if (CITY_CODE.equals(groupByField)) {
                city = cityIndexService.findByCitycode(bucket.getKeyAsString());
                complaintSouce.setDistrictName(city.getDistrictname());
                complaintSouce.setUlbName(city.getName());
                complaintSouce.setUlbGrade(city.getCitygrade());
                complaintSouce.setUlbCode(city.getCitycode());
                complaintSouce.setDomainURL(city.getDomainurl());
            }
            if (DEPARTMENT_NAME.equals(groupByField))
                complaintSouce.setDepartmentName(bucket.getKeyAsString());
            if (WARD_NAME.equals(groupByField))
                complaintSouce.setWardName(bucket.getKeyAsString());
            if (LOCALITY_NAME.equals(groupByField))
                complaintSouce.setLocalityName(bucket.getKeyAsString());
            if (INITIAL_FUNCTIONARY_NAME.equals(groupByField)
                    && !complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) {
                complaintSouce.setFunctionaryName(bucket.getKeyAsString());
                final String mobileNumber = complaintIndexRepository.getFunctionryMobileNumber(bucket.getKeyAsString());
                complaintSouce.setFunctionaryMobileNumber(defaultString(mobileNumber, NA));
            }
            final List<HashMap<String, Long>> list = new ArrayList<>();
            final Terms sourceTerms = bucket.getAggregations().get("groupByFieldSource");
            for (final Bucket sourceBucket : sourceTerms.getBuckets()) {
                final HashMap<String, Long> sourceMap = new HashMap<>();
                sourceMap.put(sourceBucket.getKeyAsString(), sourceBucket.getDocCount());
                list.add(sourceMap);
            }
            complaintSouce.setSourceList(list);
            responseDetailsList.add(complaintSouce);
        }
        return responseDetailsList;
    }

    public Iterable<ComplaintIndex> searchComplaintIndex(final BoolQueryBuilder searchQuery) {
        return complaintIndexRepository.search(searchQuery);
    }

    private BoolQueryBuilder getFilterQuery(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(termQuery("registered", 1));
        if (isNotBlank(complaintDashBoardRequest.getFromDate()) && isNotBlank(complaintDashBoardRequest.getToDate()))
            boolQuery = boolQuery.must(rangeQuery("createdDate").from(complaintDashBoardRequest.getFromDate()).to(complaintDashBoardRequest.getToDate()));
        if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_ULB) ||
                complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_WARDS) ||
                complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_LOCALITIES) ||
                complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) {
            boolQuery = filterBasedOnSource(complaintDashBoardRequest, boolQuery);
            return boolQuery;
        }
        if (isNotBlank(complaintDashBoardRequest.getRegionName()))
            boolQuery = boolQuery.filter(matchQuery(CITY_REGION_NAME, complaintDashBoardRequest.getRegionName()));
        if (isNotBlank(complaintDashBoardRequest.getUlbGrade()))
            boolQuery = boolQuery.filter(matchQuery(CITY_GRADE, complaintDashBoardRequest.getUlbGrade()));
        if (isNotBlank(complaintDashBoardRequest.getCategoryId()))
            boolQuery = boolQuery.filter(matchQuery("categoryId", complaintDashBoardRequest.getCategoryId()));
        if (isNotBlank(complaintDashBoardRequest.getDistrictName()))
            boolQuery = boolQuery
                    .filter(matchQuery(CITY_DISTRICT_NAME, complaintDashBoardRequest.getDistrictName()));
        if (isNotBlank(complaintDashBoardRequest.getUlbCode()))
            boolQuery = boolQuery.filter(matchQuery(CITY_CODE, complaintDashBoardRequest.getUlbCode()));
        if (isNotBlank(complaintDashBoardRequest.getWardNo()))
            boolQuery = boolQuery.filter(matchQuery("wardNo", complaintDashBoardRequest.getWardNo()));
        if (isNotBlank(complaintDashBoardRequest.getDepartmentCode()))
            boolQuery = boolQuery
                    .filter(matchQuery("departmentCode", complaintDashBoardRequest.getDepartmentCode()));
        if (isNotBlank(complaintDashBoardRequest.getComplaintTypeCode()))
            boolQuery = boolQuery.filter(matchQuery("complaintTypeCode",
                    complaintDashBoardRequest.getComplaintTypeCode()));
        if (isNotBlank(complaintDashBoardRequest.getLocalityName()))
            boolQuery = boolQuery.filter(matchQuery("localityName",
                    complaintDashBoardRequest.getLocalityName()));
        if (isNotBlank(complaintDashBoardRequest.getFunctionaryName()))
            boolQuery = boolQuery.filter(matchQuery(INITIAL_FUNCTIONARY_NAME,
                    complaintDashBoardRequest.getFunctionaryName()));
        boolQuery = filterBasedOnSource(complaintDashBoardRequest, boolQuery);

        return boolQuery;
    }

    private BoolQueryBuilder filterBasedOnSource(ComplaintDashBoardRequest complaintDashBoardRequest,
                                                 BoolQueryBuilder boolQuery) {
        BoolQueryBuilder sourceQuery = boolQuery;
        if (isNotBlank(complaintDashBoardRequest.getIncludedSources()))
            sourceQuery = sourceQuery.must(termsQuery(SOURCE, complaintDashBoardRequest.getIncludedSources().split("~")));
        if (isNotBlank(complaintDashBoardRequest.getExcludedSources()))
            sourceQuery = sourceQuery.mustNot(termsQuery(SOURCE, complaintDashBoardRequest.getExcludedSources().split("~")));
        return sourceQuery;
    }

    private ComplaintDashBoardResponse populateResponse(final ComplaintDashBoardRequest complaintDashBoardRequest,
                                                        final Bucket bucket,
                                                        final String groupByField) {
        ComplaintDashBoardResponse responseDetail = new ComplaintDashBoardResponse();

        responseDetail = setDefaultValues(responseDetail);

        CityIndex city;

        if (CITY_REGION_NAME.equals(groupByField))
            responseDetail.setRegionName(bucket.getKeyAsString());
        if (CITY_GRADE.equals(groupByField))
            responseDetail.setUlbGrade(bucket.getKeyAsString());
        if (CITY_CODE.equals(groupByField) && DASHBOARD_GROUPING_CITY.equalsIgnoreCase(complaintDashBoardRequest.getType())) {
            city = cityIndexService.findOne(bucket.getKeyAsString());
            responseDetail.setUlbName(city.getName());
            responseDetail.setUlbCode(city.getCitycode());
        }

        if (CITY_DISTRICT_CODE.equals(groupByField)) {
            city = cityIndexService.findByDistrictCode(bucket.getKeyAsString());
            responseDetail.setDistrictName(city.getDistrictname());
        }
        // When UlbGrade is selected group by Ulb
        if (CITY_CODE.equals(groupByField) &&
                !complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_CITY)) {
            city = cityIndexService.findOne(bucket.getKeyAsString());
            responseDetail.setDistrictName(city.getDistrictname());
            responseDetail.setUlbName(city.getName());
            responseDetail.setUlbGrade(city.getCitygrade());
            responseDetail.setUlbCode(city.getCitycode());
            responseDetail.setDomainURL(city.getDomainurl());
        }
        // When UlbCode is passed without type group by department else by type
        if (DEPARTMENT_NAME.equals(groupByField))
            responseDetail.setDepartmentName(bucket.getKeyAsString());
        if (WARD_NAME.equals(groupByField))
            responseDetail.setWardName(bucket.getKeyAsString());
        if (LOCALITY_NAME.equals(groupByField))
            responseDetail.setLocalityName(bucket.getKeyAsString());
        if (INITIAL_FUNCTIONARY_NAME.equals(groupByField)
                && !complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) {
            responseDetail.setFunctionaryName(bucket.getKeyAsString());
            final String mobileNumber = complaintIndexRepository.getFunctionryMobileNumber(bucket.getKeyAsString());
            responseDetail.setFunctionaryMobileNumber(mobileNumber);
        }
        return responseDetail;
    }

    private ComplaintDashBoardResponse setDefaultValues(final ComplaintDashBoardResponse response) {
        response.setDistrictName(EMPTY);
        response.setUlbName(EMPTY);
        response.setWardName(EMPTY);
        response.setDepartmentName(EMPTY);
        response.setFunctionaryName(EMPTY);
        response.setLocalityName(EMPTY);
        response.setComplaintTypeName(EMPTY);
        response.setUlbGrade(EMPTY);
        response.setUlbCode(EMPTY);
        response.setDomainURL(EMPTY);

        return response;
    }

    public List<String> getSourceNameList() {
        final List<String> sourceList = new ArrayList<>();
        final List<ReceivingMode> receivingModes = receivingModeService.getReceivingModes();
        for (final ReceivingMode receivingMode : receivingModes)
            sourceList.add(receivingMode.getName());
        return sourceList;
    }

    public List<ComplaintIndex> getFunctionaryWiseComplaints(final String functionaryName) {
        final List<ComplaintIndex> complaints = complaintIndexRepository.findAllComplaintsBySource(INITIAL_FUNCTIONARY_NAME,
                functionaryName);
        setComplaintViewURL(complaints);
        return complaints;
    }

    public List<ComplaintIndex> getLocalityWiseComplaints(final String localityName) {
        final List<ComplaintIndex> complaints = complaintIndexRepository.findAllComplaintsBySource(LOCALITY_NAME,
                localityName);
        setComplaintViewURL(complaints);
        return complaints;
    }

    // This is a generic method to fetch all complaints based on fieldName and its value
    public List<ComplaintIndex> getFilteredComplaints(final ComplaintDashBoardRequest complaintDashBoardRequest,
                                                      final String fieldName, final String fieldValue, Integer lowerLimit, Integer upperLimit) {
        BoolQueryBuilder boolQuery = getFilterQuery(complaintDashBoardRequest);
        if (isNotBlank(fieldValue))
            boolQuery.filter(matchQuery(fieldName, fieldValue));
        else
            boolQuery = boolQuery.filter(matchQuery("ifClosed", 1)).filter(rangeQuery(fieldName).gte(lowerLimit).lte(upperLimit));

        final List<ComplaintIndex> complaints = complaintIndexRepository.findAllComplaintsByField(complaintDashBoardRequest,
                boolQuery);
        setComplaintViewURL(complaints);
        return complaints;
    }

    private void setComplaintViewURL(List<ComplaintIndex> complaints) {
        for (ComplaintIndex complaint : complaints)
            if (isNotBlank(complaint.getCityCode())) {
                final CityIndex city = cityIndexService.findOne(complaint.getCityCode());
                complaint.setUrl(format(PGR_COMPLAINT_VIEW_URL, city.getDomainurl(), complaint.getCrn()));
            }
    }


    public Map<String, Object> getAvrgRating(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        final String groupByField = ComplaintIndexAggregationBuilder.getAggregationGroupingField(complaintDashBoardRequest);
        final SearchResponse functionaryWiseResponse = complaintIndexRepository.findRatingByGroupByField(complaintDashBoardRequest,
                getFilterQuery(complaintDashBoardRequest), groupByField);

        final HashMap<String, Object> result = new HashMap<>();
        List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();

        if (isNotBlank(complaintDashBoardRequest.getUlbCode())) {
            final CityIndex city = cityIndexService.findOne(complaintDashBoardRequest.getUlbCode());
            result.put(REGION_NAME, city.getRegionname());
            result.put(DISTRICT_NAME, city.getDistrictname());
            result.put(ULB_CODE, city.getCitycode());
            result.put(ULB_GRADE, city.getCitygrade());
            result.put(ULB_NAME, city.getName());
            result.put(DOMAIN_URL, city.getDomainurl());
        }
        if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_WARDS)) {
            final Terms ulbTerms = functionaryWiseResponse.getAggregations().get(ULBWISE);
            for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
                final Terms terms = ulbBucket.getAggregations().get(GROUP_BY_FIELD);

                responseDetailsList = getResponseList(groupByField, terms, responseDetailsList, complaintDashBoardRequest);
            }

        } else if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_LOCALITIES)) {
            final Terms ulbTerms = functionaryWiseResponse.getAggregations().get(ULBWISE);
            for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
                final Terms wardTerms = ulbBucket.getAggregations().get(WARDWISE);
                for (final Bucket wardBucket : wardTerms.getBuckets()) {
                    final Terms terms = wardBucket.getAggregations().get(GROUP_BY_FIELD);
                    responseDetailsList = getResponseList(groupByField, terms, responseDetailsList,
                            complaintDashBoardRequest);
                }

            }
        } else if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) {
            final Terms ulbTerms = functionaryWiseResponse.getAggregations().get(ULBWISE);
            for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
                final Terms deptTerms = ulbBucket.getAggregations().get(DEPARTMENTWISE);
                for (final Bucket deptBucket : deptTerms.getBuckets()) {
                    final Terms terms = deptBucket.getAggregations().get(GROUP_BY_FIELD);
                    responseDetailsList = getResponseList(groupByField, terms, responseDetailsList,
                            complaintDashBoardRequest);
                }
            }
        } else {
            final Terms terms = functionaryWiseResponse.getAggregations().get(GROUP_BY_FIELD);
            responseDetailsList = getResponseList(groupByField, terms, responseDetailsList, complaintDashBoardRequest);
        }

        result.put(RESPONSE_DETAILS, responseDetailsList);

        return result;

    }

    private List<ComplaintDashBoardResponse> getResponseList(final String groupByField, final Terms terms,
                                                             final List<ComplaintDashBoardResponse> responseDetailsList, final ComplaintDashBoardRequest complaintDashBoardRequest) {
        for (final Bucket bucket : terms.getBuckets()) {
            final ComplaintDashBoardResponse complaintDashBoardResponse = new ComplaintDashBoardResponse();
            final TopHits topHits = bucket.getAggregations().get(COMPLAINTRECORD);
            final SearchHit[] hit = topHits.getHits().getHits();
            complaintDashBoardResponse.setUlbName(hit[0].field(CITY_NAME).getValue());
            complaintDashBoardResponse.setDistrictName(hit[0].field(CITY_DISTRICT_NAME).getValue());
            complaintDashBoardResponse.setWardName(hit[0].field(WARD_NAME).getValue());
            if (hit[0].field(DEPARTMENT_NAME) != null) {
                complaintDashBoardResponse.setDepartmentName(hit[0].field(DEPARTMENT_NAME).getValue());
            }
            if (hit[0].field(INITIAL_FUNCTIONARY_NAME) != null)
                complaintDashBoardResponse.setFunctionaryName(hit[0].field(INITIAL_FUNCTIONARY_NAME).getValue());
            if (hit[0].field(INITIAL_FUNCTIONARY_MOBILE_NUMBER) != null)
                complaintDashBoardResponse.setFunctionaryMobileNumber(hit[0].field(INITIAL_FUNCTIONARY_MOBILE_NUMBER).getValue());
            CityIndex city;
            if (CITY_REGION_NAME.equals(groupByField))
                complaintDashBoardResponse.setRegionName(bucket.getKeyAsString());
            if (CITY_GRADE.equals(groupByField))
                complaintDashBoardResponse.setUlbGrade(bucket.getKeyAsString());
            if (CITY_DISTRICT_CODE.equals(groupByField)) {
                city = cityIndexService.findByDistrictCode(bucket.getKeyAsString());
                complaintDashBoardResponse.setDistrictName(city.getDistrictname());
            }
            if (CITY_CODE.equals(groupByField)) {
                city = cityIndexService.findByCitycode(bucket.getKeyAsString());
                complaintDashBoardResponse.setDistrictName(city.getDistrictname());
                complaintDashBoardResponse.setUlbName(city.getName());
                complaintDashBoardResponse.setUlbGrade(city.getCitygrade());
                complaintDashBoardResponse.setUlbCode(city.getCitycode());
                complaintDashBoardResponse.setDomainURL(city.getDomainurl());
            }
            if (DEPARTMENT_NAME.equals(groupByField))
                complaintDashBoardResponse.setDepartmentName(bucket.getKeyAsString());
            if (WARD_NAME.equals(groupByField))
                complaintDashBoardResponse.setWardName(bucket.getKeyAsString());
            if (LOCALITY_NAME.equals(groupByField))
                complaintDashBoardResponse.setLocalityName(bucket.getKeyAsString());
            if (INITIAL_FUNCTIONARY_NAME.equals(groupByField)
                    && !complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) {
                complaintDashBoardResponse.setFunctionaryName(bucket.getKeyAsString());
                final String mobileNumber = complaintIndexRepository.getFunctionryMobileNumber(bucket.getKeyAsString());
                complaintDashBoardResponse.setFunctionaryMobileNumber(defaultString(mobileNumber, NA));
            }
            final Terms functionaryTerms = bucket.getAggregations().get("groupByInitialFunctionary");
            List<String> functionaryNames = new ArrayList<>();
            for (final Bucket fnBucket : functionaryTerms.getBuckets())
                functionaryNames.add(fnBucket.getKeyAsString());
            complaintDashBoardResponse.setFunctionaryCount(functionaryNames.size());
            updateClosedAndAvgSatisfactionIndex(bucket, complaintDashBoardResponse);
            responseDetailsList.add(complaintDashBoardResponse);
        }
        return responseDetailsList;
    }

    private void updateClosedAndAvgSatisfactionIndex(Bucket bucket, ComplaintDashBoardResponse complaintDashBoardResponse) {
        Terms closedCount = bucket.getAggregations().get("closedcount");
        for (Bucket ratingbucket : closedCount.getBuckets()) {
            if (ratingbucket.getKeyAsNumber().intValue() == 1)
                complaintDashBoardResponse.setClosedComplaintCount(ratingbucket.getDocCount());
            Range satisfactionAverage = ratingbucket.getAggregations().get(EXCLUDE_ZERO);

            final Avg averageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations().get("satisfactionAverage");
            if (Double.isNaN(averageSatisfaction.getValue()))
                complaintDashBoardResponse.setAvgSatisfactionIndex(0);
            else
                complaintDashBoardResponse.setAvgSatisfactionIndex(averageSatisfaction.getValue());

        }
    }
}