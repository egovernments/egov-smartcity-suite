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

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityCode;
import static org.egov.infra.utils.ApplicationConstant.NA;
import static org.egov.infra.utils.DateUtils.startOfToday;
import static org.egov.pgr.utils.constants.PGRConstants.CITY_CODE;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_ALL;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_COMPLETED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_PENDING;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REJECTED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REOPENED;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLETED_STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_FUNCTIONARY;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_LOCALITIES;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_ULB;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_WARDS;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_CITY;
import static org.egov.pgr.utils.constants.PGRConstants.NOASSIGNMENT;
import static org.egov.pgr.utils.constants.PGRConstants.PENDING_STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.PGR_INDEX_DATE_FORMAT;
import static org.egov.pgr.utils.constants.PGRConstants.REJECTED_STATUS;
import static org.egov.pgr.utils.constants.PGRConstants.WARD_NUMBER;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.es.CityIndexService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.mapper.BeanMapperConfiguration;
import org.egov.infra.exception.ApplicationValidationException;
import org.egov.infra.utils.DateUtils;
import org.egov.pgr.elasticsearch.entity.ComplaintIndex;
import org.egov.pgr.elasticsearch.entity.contract.ComplaintDashBoardRequest;
import org.egov.pgr.elasticsearch.entity.contract.ComplaintDashBoardResponse;
import org.egov.pgr.elasticsearch.entity.contract.ComplaintResponse;
import org.egov.pgr.elasticsearch.entity.contract.ComplaintSearchRequest;
import org.egov.pgr.elasticsearch.entity.contract.ComplaintSourceResponse;
import org.egov.pgr.elasticsearch.entity.contract.IVRSFeedBackResponse;
import org.egov.pgr.elasticsearch.entity.contract.MonthlyFeedbackCounts;
import org.egov.pgr.elasticsearch.repository.ComplaintIndexAggregationBuilder;
import org.egov.pgr.elasticsearch.repository.ComplaintIndexRepository;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.entity.ReceivingMode;
import org.egov.pgr.entity.enums.ComplaintStatus;
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
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityCode;
import static org.egov.search.elasticsearch.utils.ElasticSearchUtils.fixEmptyPage;
import static org.egov.infra.utils.ApplicationConstant.NA;
import static org.egov.infra.utils.DateUtils.startOfToday;
import static org.egov.pgr.utils.constants.PGRConstants.*;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Transactional(readOnly = true)
public class ComplaintIndexService {

    private static final Logger LOGGER = Logger.getLogger(ComplaintIndexService.class);

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
    private static final String PGR_COMPLAINT_VIEW_URL = "%s/pgr/grievance/view/%s";
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
    private static final String DEPARTMENT_CODE = "departmentCode";
    private static final String FUNCTIONARYWISE = "functionarywise";
    private static final String CREATED_DATE = "createdDate";
    private static final String COMPLAINT_TYPE_CODE = "complaintTypeCode";
    private static final String CATEGORY_ID = "categoryId";
    private static final int GOOD_RATING = 1;
    private static final int BAD_RATING = 2;
    private static final int AVG_RATING = 3;
    private static final int RESPONDED_WITH_FEEDBACK = 3;
    private static final int RESPONDED_WITH_REPEAT_FEEDBACK = 4;
    private static final String COMPLETION_DATE = "completionDate";
    private static final String IF_CLOSED = "ifClosed";
    private static final String TODAY_COMPLAINT_COUNT = "todaysComplaintCount";
    private static final String CALL_STATUS_AGGR = "callStatCountAggr";
    private static final String DATE_AGGR = "dateAggr";

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

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Transactional
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
        final List<Assignment> assignments = getAssignmentsForPosition(position);
        final User assignedUser = !assignments.isEmpty() ? assignments.get(0).getEmployee() : null;
        complaintIndex.setComplaintPeriod(0);
        complaintIndex.setComplaintSLADays(complaint.getComplaintType().getSlaHours());
        complaintIndex.setComplaintAgeingFromDue(0);
        complaintIndex.setIsSLA("Y");
        complaintIndex.setIfSLA(1);
        complaintIndex.setInitialFunctionaryName(
                assignedUser == null ? NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName()
                        : assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName());
        complaintIndex.setInitialFunctionaryAssigneddate(new Date());
        complaintIndex.setInitialFunctionarySLADays(getFunctionarySlaDays(complaint));
        complaintIndex.setInitialFunctionaryAgeingFromDue(0);
        complaintIndex.setInitialFunctionaryIsSLA("Y");
        complaintIndex.setInitialFunctionaryIfSLA(1);
        complaintIndex.setCurrentFunctionaryName(
                assignedUser == null ? NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName()
                        : assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName());
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

    @Transactional
    public void updateComplaintIndex(final Complaint complaint) {
        // fetch the complaint from index and then update the new fields
        ComplaintIndex complaintIndex = complaintIndexRepository.findByCrnAndCityCode(complaint.getCrn(), getCityCode());
        if(complaintIndex == null)
            throw new ApplicationValidationException("PGR.ES.01");
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
        final List<Assignment> assignments = getAssignmentsForPosition(position);
        final User assignedUser = !assignments.isEmpty() ? assignments.get(0).getEmployee() : null;
        // If complaint is forwarded
        if (complaint.nextOwnerId() != null && !complaint.nextOwnerId().equals(0L)) {
            complaintIndex
                    .setCurrentFunctionaryName(assignedUser == null
                            ? NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName()
                            : assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setCurrentFunctionaryMobileNumber(Objects.nonNull(assignedUser)
                    ? assignedUser.getMobileNumber() : EMPTY);
            complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
            complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaint));
            // Adding this because it was requested to update since the data is getting changed in EIS
            complaintIndex.setInitialFunctionaryName(
                    assignedUser == null ? NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName()
                            : assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setInitialFunctionaryMobileNumber(Objects.nonNull(assignedUser)
                    ? assignedUser.getMobileNumber() : EMPTY);
        }
        complaintIndex = updateComplaintLevelIndexFields(complaintIndex);
        if (complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.COMPLETED.toString())
                || complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.WITHDRAWN.toString())
                || complaintIndex.getComplaintStatusName().equalsIgnoreCase(ComplaintStatus.REJECTED.toString())) {
            complaintIndex.setClosed(true);
            complaintIndex.setComplaintIsClosed("Y");
            complaintIndex.setIfClosed(1);
            complaintIndex.setClosedByFunctionaryName(
                    assignedUser == null ? NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName()
                            : assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName());
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
        updateComplaintIndexStatusRelatedFields(complaintIndex);
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

    public List<Assignment> getAssignmentsForPosition(final Position position) {
        return assignmentService.getAssignmentsForPosition(position.getId(), new Date());
    }

    // This method is used to populate PGR index during complaint escalation
    @Transactional
    public void updateComplaintEscalationIndexValues(final Complaint complaint) {

        // fetch the complaint from index and then update the new fields
        ComplaintIndex complaintIndex = complaintIndexRepository.findByCrnAndCityCode(complaint.getCrn(), getCityCode());
        beanMapperConfiguration.map(complaint, complaintIndex);

        final Position position = complaint.getAssignee();
        final List<Assignment> assignments = getAssignmentsForPosition(position);
        final User assignedUser = assignments.isEmpty() ? null : assignments.get(0).getEmployee();
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
                        assignedUser == null ? NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName()
                                : assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName());
        complaintIndex.setCurrentFunctionaryMobileNumber(Objects.nonNull(assignedUser)
                ? assignedUser.getMobileNumber() : EMPTY);
        complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
        complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaint));
        complaintIndex = updateComplaintLevelIndexFields(complaintIndex);
        int escalationLevel = complaintIndex.getEscalationLevel();
        // For Escalation level1
        if (escalationLevel == 0) {
            complaintIndex.setEscalation1FunctionaryName(
                    assignedUser == null ? NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName()
                            : assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setEscalation1FunctionaryAssigneddate(new Date());
            complaintIndex.setEscalation1FunctionarySLADays(getFunctionarySlaDays(complaint));
            complaintIndex.setEscalation1FunctionaryAgeingFromDue(0);
            complaintIndex.setEscalation1FunctionaryIsSLA("Y");
            complaintIndex.setEscalation1FunctionaryIfSLA(1);
            complaintIndex.setEscalationLevel(++escalationLevel);
        } else if (escalationLevel == 1) {
            // update escalation level 2 fields
            complaintIndex.setEscalation2FunctionaryName(
                    assignedUser == null ? NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName()
                            : assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setEscalation2FunctionaryAssigneddate(new Date());
            complaintIndex.setEscalation2FunctionarySLADays(getFunctionarySlaDays(complaint));
            complaintIndex.setEscalation2FunctionaryAgeingFromDue(0);
            complaintIndex.setEscalation2FunctionaryIsSLA("Y");
            complaintIndex.setEscalation2FunctionaryIfSLA(1);
            complaintIndex.setEscalationLevel(++escalationLevel);
        } else if (escalationLevel == 2) {
            // update escalation level 3 fields
            complaintIndex.setEscalation3FunctionaryName(
                    assignedUser == null ? NOASSIGNMENT + " : " + position.getDeptDesig().getDesignation().getName()
                            : assignedUser.getName() + " : " + position.getDeptDesig().getDesignation().getName());
            complaintIndex.setEscalation3FunctionaryAssigneddate(new Date());
            complaintIndex.setEscalation3FunctionarySLADays(getFunctionarySlaDays(complaint));
            complaintIndex.setEscalation3FunctionaryAgeingFromDue(0);
            complaintIndex.setEscalation3FunctionaryIsSLA("Y");
            complaintIndex.setEscalation3FunctionaryIfSLA(1);
            complaintIndex.setEscalationLevel(++escalationLevel);
        }
        complaintIndex = updateEscalationLevelIndexFields(complaintIndex);
        // update status related fields in index
        updateComplaintIndexStatusRelatedFields(complaintIndex);

        complaintIndexRepository.save(complaintIndex);
    }

    public void updateAllOpenComplaintIndex() {
        final List<Complaint> openComplaints = complaintService.getOpenComplaints();
        for (final Complaint complaint : openComplaints) {
            final TransactionTemplate txTemplate = new TransactionTemplate(transactionTemplate.getTransactionManager());
            txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            try {
                txTemplate.execute(result -> {
                    updateOpenComplaintIndex(complaint);
                    return Boolean.TRUE;
                });
            } catch (final Exception e) {
                LOGGER.error("Error while pushing complaint to Elastic Search for " + complaint.getCrn(), e);
            }
        }
    }

    @Transactional
    public void updateOpenComplaintIndex(final Complaint complaint) {
        // fetch the complaint from index and then update the new fields
        ComplaintIndex complaintIndex = complaintIndexRepository.findByCrnAndCityCode(complaint.getCrn(), getCityCode());
        if (complaintIndex != null) {
            beanMapperConfiguration.map(complaint, complaintIndex);
            setCurrentOwnerDetails(complaint.getAssignee(), complaintIndex);
            complaintIndex = updateComplaintLevelIndexFields(complaintIndex);
            complaintIndex = updateEscalationLevelIndexFields(complaintIndex);
            // update status related fields in index
            complaintIndex = updateComplaintIndexStatusRelatedFields(complaintIndex);
            complaintIndexRepository.save(complaintIndex);
        }
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

    private void setCurrentOwnerDetails(Position currentOwner, ComplaintIndex complaintIndex) {
        if (currentOwner != null) {
            final List<Assignment> assignments = getAssignmentsForPosition(currentOwner);
            final User assignedUser = !assignments.isEmpty() ? assignments.get(0).getEmployee() : null;
            complaintIndex.setCurrentFunctionaryName(assignedUser == null
                    ? NOASSIGNMENT + " : " + currentOwner.getDeptDesig().getDesignation().getName()
                    : assignedUser.getName() + " : " + currentOwner.getDeptDesig().getDesignation().getName());
            complaintIndex.setCurrentFunctionaryMobileNumber(Objects.nonNull(assignedUser)
                    ? assignedUser.getMobileNumber() : EMPTY);
        }
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
                .getEscalationByComplaintTypeAndDesignation(complaint.getComplaintType().getId(), designation.getId());
        return complaintEscalation == null ? 0 : complaintEscalation.getNoOfHrs();
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
        result.put("AvgAgeingInWeeks", Double.isNaN(averageAgeing.getValue()) ? 0 : averageAgeing.getValue() / 7);
        Range satisfactionAverage = consolidatedResponse.getAggregations().get(EXCLUDE_ZERO);
        final Avg averageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations().get("satisfactionAverage");
        result.put("AvgCustomeSatisfactionIndex",
                Double.isNaN(averageSatisfaction.getValue()) ? 0 : averageSatisfaction.getValue());
        result.putAll(getCityDetails(complaintDashBoardRequest));
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

        BoolQueryBuilder todaysComplaintQuery = getFilterQuery(complaintDashBoardRequest, true);
        Range todaysCount = complaintIndexRepository
                .todaysComplaintCount(todaysComplaintQuery)
                .getAggregations().get(TODAY_COMPLAINT_COUNT);
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
                            setAgeingResults(responseDetail, closedCountbucket, GROUP_BY_FIELD_AGEING,
                                    GROUP_BY_FIELD_AGEING_FOR_HOURS);
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
                                setAgeingResults(responseDetail, closedCountbucket, GROUP_BY_FIELD_AGEING,
                                        GROUP_BY_FIELD_AGEING_FOR_HOURS);
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
                                setAgeingResults(responseDetail, closedCountbucket, GROUP_BY_FIELD_AGEING,
                                        GROUP_BY_FIELD_AGEING_FOR_HOURS);
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
                        setAgeingResults(responseDetail, closedCountbucket, GROUP_BY_FIELD_AGEING,
                                GROUP_BY_FIELD_AGEING_FOR_HOURS);
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

        HashMap<String, Object> result = getCityDetails(complaintDashBoardRequest);
        List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();

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

        // For other localities in drill down
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
                final Terms functionaryTerms = departmentBucket.getAggregations().get(FUNCTIONARYWISE);
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

    /*
     * public Map<String, Object> getSourceWiseResponse(final ComplaintDashBoardRequest complaintDashBoardRequest) { final String
     * groupByField = ComplaintIndexAggregationBuilder.getAggregationGroupingField(complaintDashBoardRequest); final
     * SearchResponse sourceWiseResponse = complaintIndexRepository.findAllGrievanceBySource(complaintDashBoardRequest,
     * getFilterQuery(complaintDashBoardRequest), groupByField); HashMap<String, Object> result =
     * getCityDetails(complaintDashBoardRequest); List<ComplaintSourceResponse> responseDetailsList = new ArrayList<>(); if
     * (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_WARDS)) { final Terms ulbTerms =
     * sourceWiseResponse.getAggregations().get(ULBWISE); for (final Bucket ulbBucket : ulbTerms.getBuckets()) { final Terms terms
     * = ulbBucket.getAggregations().get(GROUP_BY_FIELD); responseDetailsList.addAll(getResponseDetailsList(groupByField, terms,
     * complaintDashBoardRequest)); } } else if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_LOCALITIES)) {
     * responseDetailsList = getResponseDetailsList(complaintDashBoardRequest, groupByField, sourceWiseResponse, WARDWISE); } else
     * if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) { responseDetailsList =
     * getComplaintSourceResponses(complaintDashBoardRequest, groupByField, sourceWiseResponse, DEPARTMENTWISE); } else { final
     * Terms terms = sourceWiseResponse.getAggregations().get(GROUP_BY_FIELD); responseDetailsList =
     * getResponseDetailsList(groupByField, terms, complaintDashBoardRequest); } result.put(RESPONSE_DETAILS,
     * responseDetailsList); final List<ComplaintSourceResponse> complaintTypeList = new ArrayList<>(); final Terms terms =
     * sourceWiseResponse.getAggregations().get(COMPLAINT_TYPEWISE); for (final Bucket bucket : terms.getBuckets()) { final
     * ComplaintSourceResponse complaintSouce = new ComplaintSourceResponse();
     * complaintSouce.setComplaintTypeName(bucket.getKey().toString()); final List<HashMap<String, Long>> list = new
     * ArrayList<>(); final Terms sourceTerms = bucket.getAggregations().get("complaintTypeWiseSource"); for (final Bucket
     * sourceBucket : sourceTerms.getBuckets()) { final HashMap<String, Long> sourceMap = new HashMap<>();
     * sourceMap.put(sourceBucket.getKeyAsString(), sourceBucket.getDocCount()); list.add(sourceMap); }
     * complaintSouce.setSourceList(list); complaintTypeList.add(complaintSouce); } result.put(COMPLAINT_TYPEWISE,
     * complaintTypeList); return result; }
     */

    public Map<String, Object> getSourceWiseResponse(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        final String groupByField = ComplaintIndexAggregationBuilder.getAggregationGroupingField(complaintDashBoardRequest);
        final SearchResponse sourceWiseResponse = complaintIndexRepository.findAllGrievanceBySource(complaintDashBoardRequest,
                getFilterQuery(complaintDashBoardRequest), groupByField);

        HashMap<String, Object> result = getCityDetails(complaintDashBoardRequest);
        List<ComplaintSourceResponse> responseDetailsList = new ArrayList<>();
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

    private void setGenericResponse(String groupByField, Bucket bucket, ComplaintResponse complaintResponse) {
        CityIndex city;
        if (CITY_REGION_NAME.equals(groupByField))
            complaintResponse.setRegionName(bucket.getKeyAsString());
        if (CITY_GRADE.equals(groupByField))
            complaintResponse.setUlbGrade(bucket.getKeyAsString());
        if (CITY_DISTRICT_CODE.equals(groupByField)) {
            city = cityIndexService.findByDistrictCode(bucket.getKeyAsString());
            complaintResponse.setDistrictName(city.getDistrictname());
        }
        if (CITY_CODE.equals(groupByField)) {
            city = cityIndexService.findByCitycode(bucket.getKeyAsString());
            complaintResponse.setDistrictName(city.getDistrictname());
            complaintResponse.setUlbName(city.getName());
            complaintResponse.setUlbGrade(city.getCitygrade());
            complaintResponse.setUlbCode(city.getCitycode());
            complaintResponse.setDomainURL(city.getDomainurl());
        }
        if (DEPARTMENT_NAME.equals(groupByField))
            complaintResponse.setDepartmentName(bucket.getKeyAsString());
        if (WARD_NAME.equals(groupByField))
            complaintResponse.setWardName(bucket.getKeyAsString());
        if (LOCALITY_NAME.equals(groupByField))
            complaintResponse.setLocalityName(bucket.getKeyAsString());
    }

    public Page<ComplaintIndex> searchComplaintIndex(ComplaintSearchRequest searchRequest) {
        return fixEmptyPage(complaintIndexRepository.search(searchRequest.query(), searchRequest.getPageRequest()));
    }

    public Page<ComplaintIndex> searchComplaintIndex(BoolQueryBuilder searchQuery) {
        return (Page<ComplaintIndex>) complaintIndexRepository.search(searchQuery);
    }

    private BoolQueryBuilder getFilterQuery(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        return getFilterQuery(complaintDashBoardRequest, false);
    }

    private BoolQueryBuilder getFilterQuery(final ComplaintDashBoardRequest complaintDashBoardRequest, boolean todays) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(termQuery("registered", 1));
        if (todays) {
            boolQuery = boolQuery.must(rangeQuery(CREATED_DATE)
                    .from(startOfToday().toString(PGR_INDEX_DATE_FORMAT))
                    .to(new DateTime().plusDays(1).toString(PGR_INDEX_DATE_FORMAT)));
        } else if (isNotBlank(complaintDashBoardRequest.getFromDate()) && isNotBlank(complaintDashBoardRequest.getToDate())) {
            String fromDate = new DateTime(complaintDashBoardRequest.getFromDate()).withTimeAtStartOfDay()
                    .toString(PGR_INDEX_DATE_FORMAT);
            String toDate = new DateTime(complaintDashBoardRequest.getToDate()).plusDays(1).toString(PGR_INDEX_DATE_FORMAT);
            boolQuery = boolQuery.must(rangeQuery(CREATED_DATE).from(fromDate).to(toDate));
        }
        if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_ULB) ||
                complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_WARDS) ||
                complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_LOCALITIES) ||
                complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) {
            boolQuery = filterBasedOnSource(complaintDashBoardRequest, boolQuery);
            //return boolQuery;
        }
        if (isNotBlank(complaintDashBoardRequest.getRegionName()))
            boolQuery = boolQuery.filter(matchQuery(CITY_REGION_NAME, complaintDashBoardRequest.getRegionName()));
        if (isNotBlank(complaintDashBoardRequest.getUlbGrade()))
            boolQuery = boolQuery.filter(matchQuery(CITY_GRADE, complaintDashBoardRequest.getUlbGrade()));
        if (isNotBlank(complaintDashBoardRequest.getCategoryId()))
            boolQuery = boolQuery.filter(matchQuery(CATEGORY_ID, complaintDashBoardRequest.getCategoryId()));
        if (isNotBlank(complaintDashBoardRequest.getDistrictName()))
            boolQuery = boolQuery
                    .filter(matchQuery(CITY_DISTRICT_NAME, complaintDashBoardRequest.getDistrictName()));
        if (isNotBlank(complaintDashBoardRequest.getUlbCode()))
            boolQuery = boolQuery.filter(matchQuery(CITY_CODE, complaintDashBoardRequest.getUlbCode()));
        if (isNotBlank(complaintDashBoardRequest.getWardNo()))
            boolQuery = boolQuery.filter(matchQuery("wardNo", complaintDashBoardRequest.getWardNo()));
        if (isNotBlank(complaintDashBoardRequest.getDepartmentCode()))
            boolQuery = boolQuery
                    .filter(matchQuery(DEPARTMENT_CODE, complaintDashBoardRequest.getDepartmentCode()));
        if (isNotBlank(complaintDashBoardRequest.getComplaintTypeCode()))
            boolQuery = boolQuery.filter(matchQuery(COMPLAINT_TYPE_CODE,
                    complaintDashBoardRequest.getComplaintTypeCode()));
        if (isNotBlank(complaintDashBoardRequest.getLocalityName()))
            boolQuery = boolQuery.filter(matchQuery(LOCALITY_NAME,
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
            boolQuery = boolQuery.filter(matchQuery(IF_CLOSED, 1)).filter(rangeQuery(fieldName).gte(lowerLimit).lte(upperLimit));

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

    public List<ComplaintIndex> getFeedbackComplaints(final ComplaintDashBoardRequest complaintDashBoardRequest,
            final String fieldName, final String fieldValue) {

        List<ComplaintIndex> complaintList = new ArrayList<>();
        if (isBlank(fieldName) && isBlank(fieldValue)) {
            getComplaintsByField(complaintDashBoardRequest, fieldName, fieldValue, complaintList);
        } else
            for (String callStatus : Arrays.asList(fieldValue.split(","))) {
                getComplaintsByField(complaintDashBoardRequest, fieldName, callStatus, complaintList);
            }
        return complaintList;
    }

    private void getComplaintsByField(ComplaintDashBoardRequest complaintDashBoardRequest, String fieldName, String callStatus,
            List<ComplaintIndex> complaintList) {
        BoolQueryBuilder boolQuery = getIvrsFilterQuery(complaintDashBoardRequest, false);
        List<ComplaintIndex> complaints = complaintIndexRepository.findIvrsComplaints(complaintDashBoardRequest,
                boolQuery, fieldName, callStatus);
        setComplaintViewURL(complaints);
        getFeedbackInWords(complaints);
        complaintList.addAll(complaints);
    }

    private void getFeedbackInWords(List<ComplaintIndex> complaints) {
        for (ComplaintIndex complaint : complaints)
            if (complaint.getFeedbackRating() == GOOD_RATING)
                complaint.setFeedbackInWords("Good");
            else if (complaint.getFeedbackRating() == AVG_RATING)
                complaint.setFeedbackInWords("Average");
            else if (complaint.getFeedbackRating() == BAD_RATING)
                complaint.setFeedbackInWords("Bad");

    }

    public Map<String, Object> getAvrgRating(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        final String groupByField = ComplaintIndexAggregationBuilder.getAggregationGroupingField(complaintDashBoardRequest);
        final SearchResponse functionaryWiseResponse = complaintIndexRepository.findRatingByGroupByField(
                complaintDashBoardRequest,
                getFilterQuery(complaintDashBoardRequest), groupByField);
        List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();
        HashMap<String, Object> details = getCityDetails(complaintDashBoardRequest);
        if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_WARDS)) {
            final Terms ulbTerms = functionaryWiseResponse.getAggregations().get(ULBWISE);
            for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
                final Terms terms = ulbBucket.getAggregations().get(GROUP_BY_FIELD);
                responseDetailsList = getResponseList(groupByField, terms, complaintDashBoardRequest);
            }
        } else if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_LOCALITIES)) {
            responseDetailsList = getComplaintDashBoardResponses(complaintDashBoardRequest, groupByField,
                    functionaryWiseResponse, WARDWISE);
        } else if (complaintDashBoardRequest.getType().equals(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) {
            responseDetailsList = getComplaintDashBoardResponses(complaintDashBoardRequest, groupByField,
                    functionaryWiseResponse, DEPARTMENTWISE);
        } else {
            final Terms terms = functionaryWiseResponse.getAggregations().get(GROUP_BY_FIELD);
            responseDetailsList = getResponseList(groupByField, terms, complaintDashBoardRequest);
        }
        details.put(RESPONSE_DETAILS, responseDetailsList);
        return details;
    }

    private List<ComplaintDashBoardResponse> getComplaintDashBoardResponses(ComplaintDashBoardRequest complaintDashBoardRequest,
            String groupByField,
            SearchResponse functionaryWiseResponse,
            String aggregation) {
        List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();
        final Terms ulbTerms = functionaryWiseResponse.getAggregations().get(ULBWISE);
        for (final Bucket ulbBucket : ulbTerms.getBuckets()) {
            final Terms terms = ulbBucket.getAggregations().get(aggregation);
            for (final Bucket bucket : terms.getBuckets()) {
                final Terms aggTerms = bucket.getAggregations().get(GROUP_BY_FIELD);
                responseDetailsList = getResponseList(groupByField, aggTerms,
                        complaintDashBoardRequest);
            }
        }
        return responseDetailsList;
    }

    private HashMap<String, Object> getCityDetails(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        HashMap<String, Object> cityDetails = new HashMap<>();
        if (isNotBlank(complaintDashBoardRequest.getUlbCode())) {
            CityIndex city = cityIndexService.findOne(complaintDashBoardRequest.getUlbCode());
            if (city != null) {
                cityDetails.put(REGION_NAME, city.getRegionname());
                cityDetails.put(DISTRICT_NAME, city.getDistrictname());
                cityDetails.put(ULB_CODE, city.getCitycode());
                cityDetails.put(ULB_GRADE, city.getCitygrade());
                cityDetails.put(ULB_NAME, city.getName());
                cityDetails.put(DOMAIN_URL, city.getDomainurl());
            }
        }
        return cityDetails;
    }

    private List<ComplaintDashBoardResponse> getResponseList(final String groupByField, final Terms terms,
            final ComplaintDashBoardRequest complaintDashBoardRequest) {
        List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();
        for (final Bucket bucket : terms.getBuckets()) {
            final ComplaintDashBoardResponse complaintDashBoardResponse = new ComplaintDashBoardResponse();
            setGenericResponse(groupByField, bucket, complaintDashBoardResponse);
            if (INITIAL_FUNCTIONARY_NAME.equals(groupByField)
                    && !complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_FUNCTIONARY)) {
                complaintDashBoardResponse.setFunctionaryName(bucket.getKeyAsString());
                final String mobileNumber = complaintIndexRepository.getFunctionryMobileNumber(bucket.getKeyAsString());
                complaintDashBoardResponse.setFunctionaryMobileNumber(defaultString(mobileNumber, NA));
            }
            BigDecimal functionCount = ZERO;
            Terms ulbTerms = bucket.getAggregations().get(CITY_CODE);
            for (Bucket ulbBucket : ulbTerms.getBuckets()) {
                Terms deptTerms = ulbBucket.getAggregations().get("groupByDepartment");
                for (Bucket deptBucket : deptTerms.getBuckets()) {
                    Terms funcTerms = deptBucket.getAggregations().get("groupByInitialFunctionary");
                    functionCount = functionCount.add(BigDecimal.valueOf(funcTerms.getBuckets().size()));
                }
            }
            complaintDashBoardResponse.setFunctionaryCount(functionCount.longValue());
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

    public Map<String, Long> getCrossCityComplaintsCount(String mobileNumber) {
        HashMap<String, Long> complaintsCount = new HashMap<>();
        complaintsCount.put(COMPLAINT_PENDING,
                complaintIndexRepository.countByComplainantMobileAndComplaintStatusNameIn(mobileNumber,
                        Arrays.asList(PENDING_STATUS)));
        complaintsCount.put(COMPLAINT_COMPLETED,
                complaintIndexRepository.countByComplainantMobileAndComplaintStatusNameIn(mobileNumber,
                        Arrays.asList(COMPLETED_STATUS)));
        complaintsCount.put(COMPLAINT_REJECTED,
                complaintIndexRepository.countByComplainantMobileAndComplaintStatusNameIn(mobileNumber,
                        Arrays.asList(REJECTED_STATUS)));
        complaintsCount.put(COMPLAINT_ALL, complaintsCount.get(COMPLAINT_PENDING) + complaintsCount.get(COMPLAINT_COMPLETED)
                + complaintsCount.get(COMPLAINT_REJECTED));
        return complaintsCount;
    }

    public List<IVRSFeedBackResponse> getDetailsBasedOnFeedBack(final ComplaintDashBoardRequest ivrsRequest) {
        List<IVRSFeedBackResponse> feedbackResponseList = new ArrayList<>();
        if (isNotBlank(ivrsRequest.getType())) {
            String aggregationField = ComplaintIndexAggregationBuilder.fetchAggregationField(ivrsRequest.getType());
            SearchResponse completedResponse = complaintIndexRepository.findFeedBackRatingDetails(ivrsRequest,
                    prepareQuery(ivrsRequest),
                    aggregationField);
            Terms closedterms = completedResponse.getAggregations().get("typeAggr");
            for (Bucket closedBucket : closedterms.getBuckets()) {
                IVRSFeedBackResponse feedbackResponse = new IVRSFeedBackResponse();
                Range todaysClosedCount = closedBucket.getAggregations().get(TODAY_COMPLAINT_COUNT);
                feedbackResponse.setTotalComplaint(closedBucket.getDocCount());
                feedbackResponse.setTodaysClosed(todaysClosedCount.getBuckets().get(0).getDocCount());
                setUpperLevelValues(aggregationField, feedbackResponse, closedBucket);
                callStatusAndRatingCount(closedBucket.getAggregations().get(CALL_STATUS_AGGR), feedbackResponse);
                feedbackResponse.setTotalIvrsUpdated(feedbackResponse.getResponded() + feedbackResponse.getNotResponded());
                feedbackResponse.setMonthlyCounts(getMonthlyFeedbackCount(closedBucket.getAggregations().get(DATE_AGGR)));
                feedbackResponseList.add(feedbackResponse);
            }
        } else {
            SearchResponse searchResponse = complaintIndexRepository.findFeedBackRatingWithoutAggr(ivrsRequest,
                    prepareQuery(ivrsRequest));
            ValueCount closedComplaints = searchResponse.getAggregations().get("closedCount");
            Range todayClosedCount = searchResponse.getAggregations().get(TODAY_COMPLAINT_COUNT);
            IVRSFeedBackResponse feedbackResponse = new IVRSFeedBackResponse();
            feedbackResponse.setTotalComplaint(closedComplaints.getValue());
            feedbackResponse.setTodaysClosed(todayClosedCount.getBuckets().get(0).getDocCount());
            callStatusAndRatingCount(searchResponse.getAggregations().get(CALL_STATUS_AGGR), feedbackResponse);
            feedbackResponse.setTotalIvrsUpdated(feedbackResponse.getResponded() + feedbackResponse.getNotResponded());
            feedbackResponse.setMonthlyCounts(getMonthlyFeedbackCount(searchResponse.getAggregations().get(DATE_AGGR)));
            feedbackResponseList.add(feedbackResponse);
        }
        return feedbackResponseList;
    }

    private List<MonthlyFeedbackCounts> getMonthlyFeedbackCount(final Histogram dateAgg) {
        String[] dateArray;
        List<MonthlyFeedbackCounts> monthRateList = new ArrayList<>();
        Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();
        Integer month;
        String monthName;
        for (Histogram.Bucket entry : dateAgg.getBuckets()) {
            MonthlyFeedbackCounts monthStat = new MonthlyFeedbackCounts();
            dateArray = entry.getKeyAsString().split("T");
            month = Integer.valueOf(dateArray[0].split("-", 3)[1]);
            monthName = monthValuesMap.get(month);
            monthStat.setMonthName(monthName);
            prepareMonthlyCallStatCounts(entry, monthStat);
            monthRateList.add(monthStat);
        }
        return monthRateList;
    }

    private void prepareMonthlyCallStatCounts(Histogram.Bucket entry, MonthlyFeedbackCounts monthStat) {
        Terms callStatCountAggr = entry.getAggregations().get(CALL_STATUS_AGGR);
        BigDecimal nonRespondedCount = ZERO;
        for (Bucket callStatBucket : callStatCountAggr.getBuckets()) {
            int callStatus = callStatBucket.getKeyAsNumber().intValue();
            if (RESPONDED_WITH_FEEDBACK == callStatus || RESPONDED_WITH_REPEAT_FEEDBACK == callStatus) {
                monthStat.setRespondedCount(callStatBucket.getDocCount());
                prepareMonthlyRatingCount(callStatBucket, monthStat);
            } else
                nonRespondedCount = nonRespondedCount.add(BigDecimal.valueOf(callStatBucket.getDocCount()));
        }
        monthStat.setNonRespondedCount(nonRespondedCount.longValue());
    }

    private void prepareMonthlyRatingCount(Bucket callStatBucket, MonthlyFeedbackCounts monthStat) {
        Terms countAggr = callStatBucket.getAggregations().get("countAggr");
        if (countAggr != null)
            for (Bucket bucketRating : countAggr.getBuckets()) {
                int fbRating = bucketRating.getKeyAsNumber().intValue();
                if (fbRating == GOOD_RATING)
                    monthStat.setGoodCount(bucketRating.getDocCount());
                else if (fbRating == BAD_RATING)
                    monthStat.setBadCount(bucketRating.getDocCount());
                else if (fbRating == AVG_RATING)
                    monthStat.setBadCount(bucketRating.getDocCount());
            }
    }

    private void callStatusAndRatingCount(Terms callStatTerms, IVRSFeedBackResponse feedbackResponse) {
        BigDecimal notRespondedCount = ZERO;
        for (Bucket callStatBucket : callStatTerms.getBuckets()) {
            if (RESPONDED_WITH_FEEDBACK == callStatBucket.getKeyAsNumber().intValue()
                    || RESPONDED_WITH_REPEAT_FEEDBACK == callStatBucket.getKeyAsNumber().intValue()) {
                feedbackResponse.setResponded(callStatBucket.getDocCount());
                Terms countTerms = callStatBucket.getAggregations().get("countAggr");
                for (Bucket countBucket : countTerms.getBuckets())
                    getDifferentRatingCounts(feedbackResponse, countBucket);
            } else
                notRespondedCount = notRespondedCount.add(BigDecimal.valueOf(callStatBucket.getDocCount()));
        }
        feedbackResponse.setNotResponded(notRespondedCount.longValue());
    }

    private void getDifferentRatingCounts(IVRSFeedBackResponse feedbackResponse, Bucket countBucket) {
        int rating = countBucket.getKeyAsNumber().intValue();
        if (rating == GOOD_RATING) {
            feedbackResponse.setGood(countBucket.getDocCount());
        } else if (rating == AVG_RATING) {
            feedbackResponse.setAverage(countBucket.getDocCount());
        } else if (rating == BAD_RATING) {
            feedbackResponse.setBad(countBucket.getDocCount());
        }
        feedbackResponse.setTotalFeedback(feedbackResponse.getGood() + feedbackResponse.getBad() + feedbackResponse.getAverage());
    }

    private void setUpperLevelValues(String aggregationField,
            IVRSFeedBackResponse feedbackResponse, Bucket termsBucket) {
        String name = termsBucket.getKey().toString();
        final TopHits topHits = termsBucket.getAggregations().get("paramDetails");
        final SearchHit[] hit = topHits.getHits().getHits();
        if (CITY_REGION_NAME.equalsIgnoreCase(aggregationField)) {
            feedbackResponse.setRegionName(name);
        } else if (CITY_DISTRICT_NAME.equalsIgnoreCase(aggregationField)) {
            feedbackResponse.setDistrictName(name);
            setResponse(hit, feedbackResponse);
        } else if (CITY_GRADE.equalsIgnoreCase(aggregationField)) {
            feedbackResponse.setUlbGrade(name);
            setResponse(hit, feedbackResponse);
        } else if (CITY_NAME.equalsIgnoreCase(aggregationField)) {
            feedbackResponse.setUlbName(name);
            setResponse(hit, feedbackResponse);
        } else if (WARD_NUMBER.equalsIgnoreCase(aggregationField)) {
            feedbackResponse.setWardCode(name);
            setResponse(hit, feedbackResponse);
        } else if (DEPARTMENT_CODE.equalsIgnoreCase(aggregationField)) {
            feedbackResponse.setDepartmentName(name);
            setResponse(hit, feedbackResponse);
        } else if (INITIAL_FUNCTIONARY_NAME.equalsIgnoreCase(aggregationField)) {
            feedbackResponse.setFunctionaryName(name);
            setResponse(hit, feedbackResponse);
        } else if (LOCALITY_NAME.equalsIgnoreCase(aggregationField)) {
            feedbackResponse.setLocalityName(name);
            setResponse(hit, feedbackResponse);
        }
    }

    private void setResponse(SearchHit[] hit, IVRSFeedBackResponse feedbackResponse) {
        feedbackResponse.setRegionName(hit[0].field(CITY_REGION_NAME).value());
        feedbackResponse
                .setDistrictName(hit[0].field(CITY_DISTRICT_NAME) == null ? EMPTY : hit[0].field(CITY_DISTRICT_NAME).value());
        feedbackResponse
                .setDistrictCode(hit[0].field(CITY_DISTRICT_CODE) == null ? EMPTY : hit[0].field(CITY_DISTRICT_CODE).value());
        feedbackResponse.setUlbName(hit[0].field(CITY_NAME) == null ? EMPTY : hit[0].field(CITY_NAME).value());
        feedbackResponse.setUlbCode(hit[0].field(CITY_CODE) == null ? EMPTY : hit[0].field(CITY_CODE).value());
        feedbackResponse.setUlbGrade(hit[0].field(CITY_GRADE) == null ? EMPTY : hit[0].field(CITY_GRADE).value());
        feedbackResponse.setLocalityName(hit[0].field(LOCALITY_NAME) == null ? EMPTY : hit[0].field(LOCALITY_NAME).value());
        feedbackResponse.setWardName(hit[0].field(WARD_NAME) == null ? EMPTY : hit[0].field(WARD_NAME).value());
        feedbackResponse.setWardCode(hit[0].field(WARD_NUMBER) == null ? EMPTY : hit[0].field(WARD_NUMBER).value());
        feedbackResponse.setFunctionaryName(
                hit[0].field(INITIAL_FUNCTIONARY_NAME) == null ? EMPTY : hit[0].field(INITIAL_FUNCTIONARY_NAME).value());
        feedbackResponse
                .setFunctionaryMobileNo(hit[0].field(INITIAL_FUNCTIONARY_MOBILE_NUMBER) == null
                        ? EMPTY : hit[0].field(INITIAL_FUNCTIONARY_MOBILE_NUMBER).value());
        feedbackResponse.setDepartmentName(hit[0].field(DEPARTMENT_NAME) == null ? EMPTY : hit[0].field(DEPARTMENT_NAME).value());
        feedbackResponse.setDepartmentCode(hit[0].field(DEPARTMENT_CODE) == null ? EMPTY : hit[0].field(DEPARTMENT_CODE).value());
    }

    private BoolQueryBuilder prepareQuery(final ComplaintDashBoardRequest ivrsRequest) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        boolQuery = boolQuery.must(matchQuery(IF_CLOSED, 1))
                .must(matchQuery("complaintStatusName", "COMPLETED"));
        if (isNotBlank(ivrsRequest.getFromDate()) && isNotBlank(ivrsRequest.getToDate())) {
            String fromDate = new DateTime(ivrsRequest.getFromDate()).withTimeAtStartOfDay().toString(PGR_INDEX_DATE_FORMAT);
            String toDate = new DateTime(ivrsRequest.getToDate()).plusDays(1).toString(PGR_INDEX_DATE_FORMAT);
            boolQuery = boolQuery.must(rangeQuery(COMPLETION_DATE).from(fromDate).to(toDate));
        }
        if (isNotBlank(ivrsRequest.getRegionName()))
            boolQuery = boolQuery.filter(matchQuery(CITY_REGION_NAME, ivrsRequest.getRegionName()));
        if (isNotBlank(ivrsRequest.getDistrictName()))
            boolQuery = boolQuery.filter(matchQuery(CITY_DISTRICT_NAME, ivrsRequest.getDistrictName()));
        if (isNotBlank(ivrsRequest.getWardNo()))
            boolQuery = boolQuery.filter(matchQuery(WARD_NUMBER, ivrsRequest.getWardNo()));
        if (isNotBlank(ivrsRequest.getDepartmentCode()))
            boolQuery = boolQuery.filter(matchQuery(DEPARTMENT_CODE, ivrsRequest.getDepartmentCode()));
        if (isNotBlank(ivrsRequest.getFunctionaryName()))
            boolQuery = boolQuery.filter(matchQuery(INITIAL_FUNCTIONARY_NAME, ivrsRequest.getFunctionaryName()));
        if (isNotBlank(ivrsRequest.getUlbGrade()))
            boolQuery = boolQuery.filter(matchQuery(CITY_GRADE, ivrsRequest.getUlbGrade()));
        if (isNotBlank(ivrsRequest.getUlbCode()))
            boolQuery = boolQuery.filter(matchQuery(CITY_CODE, ivrsRequest.getUlbCode()));
        if (isNotBlank(ivrsRequest.getLocalityName()))
            boolQuery = boolQuery.filter(matchQuery(LOCALITY_NAME, ivrsRequest.getLocalityName()));
        if (isNotBlank(ivrsRequest.getCategoryName()))
            boolQuery = boolQuery.filter(matchQuery("categoryName", ivrsRequest.getCategoryName()));
        if (isNotBlank(ivrsRequest.getComplaintTypeName()))
            boolQuery = boolQuery.filter(matchQuery("complaintTypeName", ivrsRequest.getComplaintTypeName()));
        if (isNotBlank(ivrsRequest.getCategoryId()))
            boolQuery = boolQuery.filter(matchQuery(CATEGORY_ID, ivrsRequest.getCategoryId()));
        if (isNotBlank(ivrsRequest.getComplaintTypeCode()))
            boolQuery = boolQuery.filter(matchQuery(COMPLAINT_TYPE_CODE, ivrsRequest.getComplaintTypeCode()));
        return boolQuery;
    }

    public List<IVRSFeedBackResponse> getCategoryWiseFeedBackDetails(final ComplaintDashBoardRequest ivrsRequest) {
        List<IVRSFeedBackResponse> feedbackResponseList = new ArrayList<>();
        SearchResponse response = complaintIndexRepository.findCategoryWiseFeedBackRatingDetails(ivrsRequest,
                prepareQuery(ivrsRequest));
        if (isNotBlank(ivrsRequest.getCategoryName()) || isNotBlank(ivrsRequest.getCategoryId())) {
            Terms terms = response.getAggregations().get("categoryAggr");
            for (Bucket termsBucket : terms.getBuckets()) {
                Terms complaintTypeTerms = termsBucket.getAggregations().get("complaintTypeAggr");
                for (Bucket complaintTypeBucket : complaintTypeTerms.getBuckets()) {
                    IVRSFeedBackResponse feedbackResponse = new IVRSFeedBackResponse();
                    feedbackResponse.setCompalintType(complaintTypeBucket.getKeyAsString());
                    setCategoryWiseResponse(complaintTypeBucket, feedbackResponse);
                    feedbackResponseList.add(feedbackResponse);
                }
            }
        } else {
            Terms terms = response.getAggregations().get("categoryAggr");
            for (Bucket termsBucket : terms.getBuckets()) {
                IVRSFeedBackResponse feedbackResponse = new IVRSFeedBackResponse();
                feedbackResponse.setComplaintCategory(termsBucket.getKeyAsString());
                setCategoryWiseResponse(termsBucket, feedbackResponse);
                feedbackResponseList.add(feedbackResponse);
            }
        }
        return feedbackResponseList;
    }

    private void setCategoryWiseResponse(Bucket complaintTypeBucket, IVRSFeedBackResponse feedbackResponse) {
        Range todaysClosedCount = complaintTypeBucket.getAggregations().get(TODAY_COMPLAINT_COUNT);
        feedbackResponse.setTotalComplaint(complaintTypeBucket.getDocCount());
        feedbackResponse.setTodaysClosed(todaysClosedCount.getBuckets().get(0).getDocCount());
        feedbackResponse.setMonthlyCounts(getMonthlyFeedbackCount(complaintTypeBucket.getAggregations().get(DATE_AGGR)));
        callStatusAndRatingCount(complaintTypeBucket.getAggregations().get(CALL_STATUS_AGGR), feedbackResponse);
        feedbackResponse.setTotalIvrsUpdated(feedbackResponse.getResponded() + feedbackResponse.getNotResponded());
    }

    private BoolQueryBuilder getIvrsFilterQuery(final ComplaintDashBoardRequest complaintDashBoardRequest, boolean todays) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(matchQuery(IF_CLOSED, 1))
                .must(matchQuery("complaintStatusName", "COMPLETED"));
        if (todays) {
            boolQuery = boolQuery.must(rangeQuery(COMPLETION_DATE)
                    .from(startOfToday().toString(PGR_INDEX_DATE_FORMAT))
                    .to(new DateTime().plusDays(1).toString(PGR_INDEX_DATE_FORMAT)));
        } else if (isNotBlank(complaintDashBoardRequest.getFromDate()) && isNotBlank(complaintDashBoardRequest.getToDate())) {
            String fromDate = new DateTime(complaintDashBoardRequest.getFromDate()).withTimeAtStartOfDay()
                    .toString(PGR_INDEX_DATE_FORMAT);
            String toDate = new DateTime(complaintDashBoardRequest.getToDate()).plusDays(1).toString(PGR_INDEX_DATE_FORMAT);
            boolQuery = boolQuery.must(rangeQuery(COMPLETION_DATE).from(fromDate).to(toDate));
        }
        if (isNotBlank(complaintDashBoardRequest.getRegionName()))
            boolQuery = boolQuery.filter(matchQuery(CITY_REGION_NAME, complaintDashBoardRequest.getRegionName()));
        if (isNotBlank(complaintDashBoardRequest.getUlbGrade()))
            boolQuery = boolQuery.filter(matchQuery(CITY_GRADE, complaintDashBoardRequest.getUlbGrade()));
        if (isNotBlank(complaintDashBoardRequest.getCategoryId()))
            boolQuery = boolQuery.filter(matchQuery(CATEGORY_ID, complaintDashBoardRequest.getCategoryId()));
        if (isNotBlank(complaintDashBoardRequest.getDistrictName()))
            boolQuery = boolQuery
                    .filter(matchQuery(CITY_DISTRICT_NAME, complaintDashBoardRequest.getDistrictName()));
        if (isNotBlank(complaintDashBoardRequest.getUlbCode()))
            boolQuery = boolQuery.filter(matchQuery(CITY_CODE, complaintDashBoardRequest.getUlbCode()));
        if (isNotBlank(complaintDashBoardRequest.getWardNo()))
            boolQuery = boolQuery.filter(matchQuery("wardNo", complaintDashBoardRequest.getWardNo()));
        if (isNotBlank(complaintDashBoardRequest.getDepartmentCode()))
            boolQuery = boolQuery
                    .filter(matchQuery(DEPARTMENT_CODE, complaintDashBoardRequest.getDepartmentCode()));
        if (isNotBlank(complaintDashBoardRequest.getComplaintTypeCode()))
            boolQuery = boolQuery.filter(matchQuery(COMPLAINT_TYPE_CODE,
                    complaintDashBoardRequest.getComplaintTypeCode()));
        if (isNotBlank(complaintDashBoardRequest.getLocalityName()))
            boolQuery = boolQuery.filter(matchQuery(LOCALITY_NAME,
                    complaintDashBoardRequest.getLocalityName()));
        if (isNotBlank(complaintDashBoardRequest.getFunctionaryName()))
            boolQuery = boolQuery.filter(matchQuery(INITIAL_FUNCTIONARY_NAME,
                    complaintDashBoardRequest.getFunctionaryName()));
        return boolQuery;
    }
}
