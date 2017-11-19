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

package org.egov.stms.service.es;

import org.apache.commons.lang.StringUtils;
import org.egov.collection.entity.es.CollectionDocument;
import org.egov.collection.repository.es.CollectionDocumentRepository;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.stms.elasticsearch.entity.DailySTCollectionReportSearch;
import org.egov.stms.elasticsearch.entity.SewerageBulkExecutionResponse;
import org.egov.stms.elasticsearch.entity.SewerageCollectFeeSearchRequest;
import org.egov.stms.elasticsearch.entity.SewerageConnSearchRequest;
import org.egov.stms.elasticsearch.entity.SewerageExecutionResult;
import org.egov.stms.elasticsearch.entity.SewerageNoticeSearchRequest;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.masters.pojo.SewerageRateDCBResult;
import org.egov.stms.reports.entity.SewerageBaseRegisterResult;
import org.egov.stms.reports.entity.SewerageNoOfConnReportResult;
import org.egov.stms.repository.es.SewerageIndexRepository;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDCBReporService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;
import static org.egov.infra.utils.DateUtils.endOfGivenDate;
import static org.egov.infra.utils.DateUtils.startOfGivenDate;
import static org.egov.infra.utils.DateUtils.toDateTimeUsingDefaultPattern;
import static org.egov.infra.utils.DateUtils.toDefaultDateTimeFormat;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_FINAL_APPROVED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_TYPE_NAME_CHANGEINCLOSETS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_TYPE_NAME_CLOSECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_TYPE_NAME_NEWCONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CLOSESEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_DONATIONCHARGE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_ESTIMATIONCHARGES_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_SEWERAGETAX_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEE_INSPECTIONCHARGE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.GROUPBYFIELD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULETYPE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULE_NAME;

@Service
@Transactional(readOnly = true)
public class SewerageIndexService {

    private static final String APPLICATION_TYPE = "applicationType";
    private static final String DESC = "desc";
    private static final String TOTALDEMAND_AMOUNT_SUM = "totaldemandAmountSum";
    private static final String DEMAND_AMOUNT_SUM = "demandAmountSum";
    private static final String ARREARSSUM = "arrearssum";
    private static final String PROPERTY_TYPE = "propertyType";
    private static final String WARD = "ward";
    private static final String ACTIVE = "active";
    private static final String SEWERAGE = "sewerage";
    private static final String APPLICATION_DATE = "applicationDate";
    private static final String COLLECTED_ARREAR_AMOUNT = "collectedArrearAmount";
    private static final String EXTRA_ADVANCE_AMOUNT = "extraAdvanceAmount";
    private static final String COLLECTED_DEMAND_AMOUNT = "collectedDemandAmount";
    private static final String DOOR_NO = "doorNo";
    private static final String MOBILE_NUMBER = "mobileNumber";
    private static final String CONSUMER_NAME = "consumerName";
    private static final String SHSC_NUMBER = "shscNumber";
    private static final String CONSUMER_NUMBER = "consumerNumber";
    private static final String ULB_NAME = "ulbName";
    private static final String APPLICATION_STARTDATE = "1998-04-01T00:00:00.000Z";
    private static final String EXECUTION_DATE = "executionDate";

    @Autowired
    private CityService cityService;
    @Autowired
    private SewerageIndexRepository sewerageIndexRepository;
    @Autowired
    private CollectionDocumentRepository collectionDocumentRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;
    @Autowired
    private SewerageDemandService sewerageDemandService;
    @Autowired
    private SewerageDCBReporService sewerageDCBReporService;
    @Autowired
    private InstallmentDao installmentDao;
    @Autowired
    private ModuleService moduleDao;
    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    public SewerageIndex createSewarageIndex(final SewerageApplicationDetails sewerageApplicationDetails,
            final AssessmentDetails assessmentDetails) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());

        final SewerageIndex sewarageIndex = new SewerageIndex();
        sewarageIndex.setUlbName(cityWebsite.getName());
        sewarageIndex.setDistrictName(cityWebsite.getDistrictName());
        sewarageIndex.setRegionName(cityWebsite.getRegionName());
        sewarageIndex.setUlbGrade(cityWebsite.getGrade());
        sewarageIndex.setUlbCode(cityWebsite.getCode());
        sewarageIndex.setApplicationCreatedBy(sewerageApplicationDetails.getCreatedBy().getName());
        sewarageIndex.setId(cityWebsite.getCode().concat("-").concat(sewerageApplicationDetails.getApplicationNumber()));
        sewarageIndex.setApplicationDate(sewerageApplicationDetails.getApplicationDate());
        sewarageIndex.setApplicationNumber(sewerageApplicationDetails.getApplicationNumber());
        sewarageIndex.setApplicationStatus(sewerageApplicationDetails.getStatus() != null
                ? sewerageApplicationDetails.getStatus().getDescription() :EMPTY);
        sewarageIndex.setConsumerNumber(sewerageApplicationDetails.getApplicationNumber());
        sewarageIndex.setApplicationType(sewerageApplicationDetails.getApplicationType() != null
                ? sewerageApplicationDetails.getApplicationType().getName() : EMPTY);
        sewarageIndex.setConnectionStatus(sewerageApplicationDetails.getConnection().getStatus() != null
                ? sewerageApplicationDetails.getConnection().getStatus().name() : EMPTY);
        sewarageIndex.setCreatedDate(sewerageApplicationDetails.getCreatedDate());
        sewarageIndex.setShscNumber(sewerageApplicationDetails.getConnection().getShscNumber() != null
                ? sewerageApplicationDetails.getConnection().getShscNumber() : EMPTY);
        sewarageIndex.setDisposalDate(sewerageApplicationDetails.getDisposalDate());

        sewarageIndex
                .setExecutionDate(sewerageApplicationDetails.getConnection().getExecutionDate());
        sewarageIndex.setIslegacy(sewerageApplicationDetails.getConnection().getLegacy());
        sewarageIndex.setNoOfClosets_nonResidential(
                sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential());
        sewarageIndex
                .setNoOfClosets_residential(sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential());
        sewarageIndex.setPropertyIdentifier(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier() != null
                ? sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier() : EMPTY);
        sewarageIndex.setPropertyType(sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null
                ? sewerageApplicationDetails.getConnectionDetail().getPropertyType().name() : EMPTY);
        if (sewerageApplicationDetails.getEstimationDate() != null)
            sewarageIndex.setEstimationDate(sewerageApplicationDetails.getEstimationDate());
        sewarageIndex
                .setEstimationNumber(sewerageApplicationDetails.getEstimationNumber() != null ? sewerageApplicationDetails
                        .getEstimationNumber() : EMPTY);
        if (sewerageApplicationDetails.getWorkOrderDate() != null)
            sewarageIndex.setWorkOrderDate(sewerageApplicationDetails.getWorkOrderDate());
        sewarageIndex
                .setWorkOrderNumber(sewerageApplicationDetails.getWorkOrderNumber() != null ? sewerageApplicationDetails
                        .getWorkOrderNumber() : EMPTY);
        if (sewerageApplicationDetails.getClosureNoticeDate() != null)
            sewarageIndex
                    .setClosureNoticeDate(sewerageApplicationDetails.getClosureNoticeDate());
        sewarageIndex
                .setClosureNoticeNumber(
                        sewerageApplicationDetails.getClosureNoticeNumber() != null ? sewerageApplicationDetails
                                .getClosureNoticeNumber() : EMPTY);
        if (sewerageApplicationDetails.getRejectionDate() != null)
            sewarageIndex
                    .setRejectionNoticeDate(sewerageApplicationDetails.getRejectionDate());
        sewarageIndex
                .setRejectionNoticeNumber(
                        sewerageApplicationDetails.getRejectionNumber() != null ? sewerageApplicationDetails
                                .getRejectionNumber() : EMPTY);
        Iterator<OwnerName> ownerNameItr = null;
        if (null != assessmentDetails.getOwnerNames())
            ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        final StringBuilder consumerName = new StringBuilder();
        final StringBuilder mobileNumber = new StringBuilder();
        if (null != ownerNameItr && ownerNameItr.hasNext()) {
            final OwnerName primaryOwner = ownerNameItr.next();
            consumerName.append(primaryOwner.getOwnerName() != null ? primaryOwner.getOwnerName() : EMPTY);
            mobileNumber.append(primaryOwner.getMobileNumber() != null ? primaryOwner.getMobileNumber() : EMPTY);
            while (ownerNameItr.hasNext()) {
                final OwnerName secondaryOwner = ownerNameItr.next();
                consumerName.append(",").append(secondaryOwner.getOwnerName() != null ? secondaryOwner.getOwnerName() : EMPTY);
                mobileNumber.append(",")
                        .append(secondaryOwner.getMobileNumber() != null ? secondaryOwner.getMobileNumber() : EMPTY);
            }
        }
        sewarageIndex.setMobileNumber(mobileNumber.toString());
        sewarageIndex.setConsumerName(consumerName.toString());
        sewarageIndex.setDoorNo(assessmentDetails.getHouseNo() != null ? assessmentDetails.getHouseNo() : EMPTY);
        sewarageIndex.setWard(
                assessmentDetails.getBoundaryDetails() != null ? assessmentDetails.getBoundaryDetails().getWardName() : EMPTY);
        sewarageIndex.setRevenueBlock(
                assessmentDetails.getBoundaryDetails() != null ? assessmentDetails.getBoundaryDetails().getBlockName() : EMPTY);
        sewarageIndex.setLocationName(
                assessmentDetails.getBoundaryDetails() != null ? assessmentDetails.getBoundaryDetails().getLocalityName() : EMPTY);
        sewarageIndex
                .setAddress(assessmentDetails.getPropertyAddress() != null ? assessmentDetails.getPropertyAddress() : EMPTY);
        sewarageIndex.setSource(sewerageApplicationDetails.getSource() != null ? sewerageApplicationDetails.getSource()
                : Source.SYSTEM.toString());
        // Setting application status is active or in-active
        sewarageIndex.setActive(sewerageApplicationDetails.isActive());

        // setting connection fees details
        for (final SewerageConnectionFee scf : sewerageApplicationDetails.getConnectionFees()) {

            if (scf.getFeesDetail().getCode().equals(FEES_SEWERAGETAX_CODE))
                sewarageIndex.setSewerageTax(BigDecimal.valueOf(scf.getAmount()));
            if (scf.getFeesDetail().getCode().equals(FEES_DONATIONCHARGE_CODE))
                sewarageIndex.setDonationAmount(BigDecimal.valueOf(scf.getAmount()));
            if (scf.getFeesDetail().getCode().equals(FEE_INSPECTIONCHARGE))
                sewarageIndex.setInspectionCharge(BigDecimal.valueOf(scf.getAmount()));
            if (scf.getFeesDetail().getCode().equals(FEES_ESTIMATIONCHARGES_CODE))
                sewarageIndex.setEstimationCharge(BigDecimal.valueOf(scf.getAmount()));
        }

        // setting demand details
        final List<SewerageRateDCBResult> rateResultList = sewerageDCBReporService
                .getSewerageRateDCBReport(sewerageApplicationDetails);
        final Date currentInstallmentYear = sewerageDemandService.getCurrentInstallment().getInstallmentYear();
        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        BigDecimal totalCollectedDemandamount = BigDecimal.ZERO;
        BigDecimal totalArrearamount = BigDecimal.ZERO;
        BigDecimal totalCollectedArearamount = BigDecimal.ZERO;
        final Calendar calendar = new GregorianCalendar();

        for (final SewerageRateDCBResult demand : rateResultList) {// FIXME: SORT BASED ON INSTALLMENT DESCRIPTION
            final Date installmentYear = installmentDao
                    .getInsatllmentByModuleAndDescription(moduleDao.getModuleByName(MODULE_NAME),
                            demand.getInstallmentYearDescription())
                    .getInstallmentYear();

            String period = null;
            if (sewerageApplicationDetails.getConnection().getExecutionDate() != null)
                calendar.setTime(sewerageApplicationDetails.getConnection().getExecutionDate());
            final int year = calendar.get(Calendar.YEAR);
            period = year + "-" + demand.getInstallmentYearDescription().substring(5, 9);
            sewarageIndex.setPeriod(period != null ? period : EMPTY);

            if (installmentYear.equals(currentInstallmentYear) || installmentYear.after(currentInstallmentYear)) {
                totalDemandAmount = totalDemandAmount.add(demand.getDemandAmount());
                totalCollectedDemandamount = totalCollectedDemandamount.add(demand.getCollectedDemandAmount());

            }
            if (installmentYear.before(currentInstallmentYear)) {
                totalArrearamount = totalArrearamount.add(demand.getDemandAmount());
                totalCollectedArearamount = totalCollectedArearamount.add(demand.getCollectedDemandAmount());

            }
            if (demand.getCollectedAdvanceAmount() != null)
                sewarageIndex.setExtraAdvanceAmount(demand.getCollectedAdvanceAmount());
        }
        sewarageIndex.setDemandAmount(totalDemandAmount != null ? totalDemandAmount : BigDecimal.ZERO);
        sewarageIndex.setCollectedDemandAmount(totalCollectedDemandamount != null ? totalCollectedDemandamount : BigDecimal.ZERO);
        sewarageIndex.setArrearAmount(totalArrearamount != null ? totalArrearamount : BigDecimal.ZERO);
        sewarageIndex.setCollectedArrearAmount(totalCollectedArearamount != null ? totalCollectedArearamount : BigDecimal.ZERO);
        sewarageIndex.setTotalAmount(totalDemandAmount.add(totalArrearamount));
        sewerageIndexRepository.save(sewarageIndex);
        return sewarageIndex;
    }

    public BoolQueryBuilder getQueryFilter(final SewerageConnSearchRequest searchRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery(ACTIVE, true));

        if (StringUtils.isNotBlank(searchRequest.getUlbName()))
            boolQuery.filter(QueryBuilders.matchQuery(ULB_NAME, searchRequest.getUlbName()));
        if (StringUtils.isNotBlank(searchRequest.getConsumerNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(CONSUMER_NUMBER, searchRequest.getConsumerNumber()));
        if (StringUtils.isNotBlank(searchRequest.getShscNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(SHSC_NUMBER, searchRequest.getShscNumber()));
        if (StringUtils.isNotBlank(searchRequest.getApplicantName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchPhrasePrefixQuery(CONSUMER_NAME, searchRequest.getApplicantName()));
        if (StringUtils.isNotBlank(searchRequest.getMobileNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(MOBILE_NUMBER, searchRequest.getMobileNumber()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WARD, searchRequest.getRevenueWard()));
        if (StringUtils.isNotBlank(searchRequest.getDoorNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(DOOR_NO, searchRequest.getDoorNumber()));
        if (searchRequest.getLegacy() != null)
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("islegacy", searchRequest.getLegacy()));
        return boolQuery;
    }

    public Page<SewerageIndex> getSearchResultByBoolQuery(final BoolQueryBuilder boolQuery, final FieldSortBuilder sort,
            final SewerageConnSearchRequest searchRequest) {
        Page<SewerageIndex> resultList;
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE).withQuery(boolQuery)
                .withPageable(new PageRequest(searchRequest.pageNumber(), searchRequest.pageSize(), searchRequest.orderDir(),
                        searchRequest.orderBy()))
                .withSort(sort).build();
        resultList = elasticsearchTemplate.queryForPage(searchQuery, SewerageIndex.class);
        return resultList;
    }

    public BoolQueryBuilder getSearchQueryFilter(final SewerageCollectFeeSearchRequest searchRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery(ULB_NAME, searchRequest.getUlbName()));
        if (StringUtils.isNotBlank(searchRequest.getConsumerNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(CONSUMER_NUMBER, searchRequest.getConsumerNumber()));
        if (StringUtils.isNotBlank(searchRequest.getShscNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(SHSC_NUMBER, searchRequest.getShscNumber()));
        if (StringUtils.isNotBlank(searchRequest.getApplicantName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchPhrasePrefixQuery(CONSUMER_NAME, searchRequest.getApplicantName()));
        if (StringUtils.isNotBlank(searchRequest.getMobileNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(MOBILE_NUMBER, searchRequest.getMobileNumber()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WARD, searchRequest.getRevenueWard()));
        if (StringUtils.isNotBlank(searchRequest.getDoorNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(DOOR_NO, searchRequest.getDoorNumber()));
        return boolQuery;
    }

    public BoolQueryBuilder getSearchQueryForExecuteConnection(final SewerageExecutionResult sewerageExecutionResult) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery(ULB_NAME, sewerageExecutionResult.getUlbName()));
        if (StringUtils.isNotBlank(sewerageExecutionResult.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WARD, sewerageExecutionResult.getRevenueWard()));
        if (StringUtils.isNotBlank(sewerageExecutionResult.getApplicationNumber()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(CONSUMER_NUMBER, sewerageExecutionResult.getApplicationNumber()));
        if (StringUtils.isNotBlank(sewerageExecutionResult.getShscNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(SHSC_NUMBER, sewerageExecutionResult.getShscNumber()));
        if (StringUtils.isNotBlank(sewerageExecutionResult.getApplicationType()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(APPLICATION_TYPE, sewerageExecutionResult.getApplicationType()));
        if (StringUtils.isNotBlank(sewerageExecutionResult.getFromDate())
                && StringUtils.isNotBlank(sewerageExecutionResult.getToDate()))
            boolQuery.filter(QueryBuilders.rangeQuery(APPLICATION_DATE)
                    .from(startOfGivenDate(toDateTimeUsingDefaultPattern(sewerageExecutionResult.getFromDate()))
                            .toString(ES_DATE_FORMAT))
                    .to(endOfGivenDate(toDateTimeUsingDefaultPattern(sewerageExecutionResult.getToDate()))
                            .toString(ES_DATE_FORMAT)));
        else if (StringUtils.isNotBlank(sewerageExecutionResult.getFromDate()))
            boolQuery.filter(QueryBuilders.rangeQuery(APPLICATION_DATE)
                    .from(startOfGivenDate(toDateTimeUsingDefaultPattern(sewerageExecutionResult.getFromDate()))
                            .toString(ES_DATE_FORMAT))
                    .to(DateTime.now().toString(ES_DATE_FORMAT)));
        else if (StringUtils.isNotBlank(sewerageExecutionResult.getToDate()))
            boolQuery.filter(QueryBuilders.rangeQuery(APPLICATION_DATE)
                    .from(APPLICATION_STARTDATE)
                    .to(endOfGivenDate(toDateTimeUsingDefaultPattern(sewerageExecutionResult.getToDate()))
                            .toString(ES_DATE_FORMAT)));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery("applicationStatus", APPLICATION_STATUS_FINAL_APPROVED));
        return boolQuery;
    }

    public Page<SewerageIndex> getCollectSearchResult(final BoolQueryBuilder boolQuery, final FieldSortBuilder sort,
            final SewerageCollectFeeSearchRequest searchRequest) {
        Page<SewerageIndex> resultList;
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE).withQuery(boolQuery)
                .withPageable(new PageRequest(searchRequest.pageNumber(), searchRequest.pageSize(),
                        searchRequest.orderDir(), searchRequest.orderBy()))
                .withSort(sort).build();

        resultList = elasticsearchTemplate.queryForPage(searchQuery, SewerageIndex.class);
        return resultList;
    }

    public List<SewerageIndex> getSearchResultForExecuteConnection(final BoolQueryBuilder boolQuery,
            final FieldSortBuilder sort) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE).withQuery(boolQuery).withSort(sort)
                .build();
        if (searchQuery != null)
            return elasticsearchTemplate.queryForList(searchQuery, SewerageIndex.class);
        else
            return Collections.emptyList();
    }

    public BoolQueryBuilder getDCRSearchResult(final DailySTCollectionReportSearch searchRequest) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery("cityName", searchRequest.getUlbName()));
        if (StringUtils.isNotBlank(searchRequest.getFromDate()))
            boolQuery = boolQuery.filter(QueryBuilders.rangeQuery("receiptDate")
                    .gte(startOfGivenDate(toDateTimeUsingDefaultPattern(searchRequest.getFromDate())).toString(ES_DATE_FORMAT))
                    .lte(endOfGivenDate(toDateTimeUsingDefaultPattern(searchRequest.getToDate())).toString(ES_DATE_FORMAT)));
        if (StringUtils.isNotBlank(searchRequest.getCollectionMode()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("channel", searchRequest.getCollectionMode()));
        if (StringUtils.isNotBlank(searchRequest.getCollectionOperator()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("receiptCreator", searchRequest.getCollectionOperator()));
        if (StringUtils.isNotBlank(searchRequest.getStatus()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("status", searchRequest.getStatus()));
        return boolQuery;
    }

    public BoolQueryBuilder getDCRSewerageSearchResult(final DailySTCollectionReportSearch searchRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery(ULB_NAME, searchRequest.getUlbName()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WARD, searchRequest.getRevenueWard()));
        return boolQuery;
    }

    public List<DailySTCollectionReportSearch> getDCRSewerageReportResult(final DailySTCollectionReportSearch searchRequest,
            final BoolQueryBuilder boolQuery) {
        final List<CollectionDocument> collectionResultList = new ArrayList<>();
        final List<DailySTCollectionReportSearch> dcrCollectionList = new ArrayList<>();
        final List<DailySTCollectionReportSearch> resultList = new ArrayList<>();
        final List<SewerageIndex> sewerageResultList = new ArrayList<>();
        DailySTCollectionReportSearch dcrReportObject;
        final Iterable<CollectionDocument> receiptResultList = collectionDocumentRepository.search(boolQuery);
        for (final CollectionDocument document : receiptResultList)
            collectionResultList.add(document);
        for (final CollectionDocument collectionObject : collectionResultList) {
            dcrReportObject = new DailySTCollectionReportSearch();
            dcrReportObject.setConsumerNumber(collectionObject.getConsumerCode());
            dcrReportObject.setReceiptNumber(collectionObject.getReceiptNumber());
            dcrReportObject.setReceiptDate(toDefaultDateTimeFormat(collectionObject.getReceiptDate()));
            dcrReportObject.setPaidAt(collectionObject.getChannel());
            dcrReportObject.setPaymentMode(collectionObject.getPaymentMode());
            dcrReportObject.setStatus(collectionObject.getStatus());
            if (StringUtils.isNotBlank(collectionObject.getInstallmentFrom()))
                dcrReportObject.setFromDate(collectionObject.getInstallmentFrom());
            if (StringUtils.isNotBlank(collectionObject.getInstallmentTo()))
                dcrReportObject.setToDate(collectionObject.getInstallmentTo());
            dcrReportObject.setArrearAmount(collectionObject.getArrearAmount());
            dcrReportObject.setCurrentAmount(collectionObject.getCurrentAmount());
            dcrReportObject.setTotalAmount(collectionObject.getTotalAmount());
            dcrCollectionList.add(dcrReportObject);
        }

        final BoolQueryBuilder sewerageBoolQuery = getDCRSewerageSearchResult(searchRequest);
        final Iterable<SewerageIndex> iterableResultList = sewerageIndexRepository.search(sewerageBoolQuery);
        for (final SewerageIndex index : iterableResultList)
            sewerageResultList.add(index);
        for (final SewerageIndex sewerageIndex : sewerageResultList)
            for (final DailySTCollectionReportSearch dcrReportObj : dcrCollectionList)
                if (dcrReportObj.getConsumerNumber().equals(sewerageIndex.getConsumerNumber())) {
                    dcrReportObj.setDoorNo(sewerageIndex.getDoorNo());
                    dcrReportObj.setShscNumber(sewerageIndex.getShscNumber());
                    dcrReportObj.setOwnerName(sewerageIndex.getConsumerName());
                    dcrReportObj.setRevenueWard(sewerageIndex.getWard());
                    resultList.add(dcrReportObj);
                }
        return resultList;
    }

    public BoolQueryBuilder getQueryFilterForNotice(final SewerageNoticeSearchRequest searchRequest) {
        BoolQueryBuilder boolQuery = null;
        if (searchRequest.getNoticeType() != null){
            if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_WORK_ORDER))
                boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("workOrderDate")
                        .from(startOfGivenDate(toDateTimeUsingDefaultPattern(searchRequest.getNoticeGeneratedFrom()))
                                .toString(ES_DATE_FORMAT))
                        .to(endOfGivenDate(toDateTimeUsingDefaultPattern(searchRequest.getNoticeGeneratedTo()))
                                .toString(ES_DATE_FORMAT)));
            else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_ESTIMATION))
                boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("estimationDate")
                        .from(startOfGivenDate(toDateTimeUsingDefaultPattern(searchRequest.getNoticeGeneratedFrom()))
                                .toString(ES_DATE_FORMAT))
                        .to(endOfGivenDate(toDateTimeUsingDefaultPattern(searchRequest.getNoticeGeneratedTo()))
                                .toString(ES_DATE_FORMAT)));
            else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_CLOSE_CONNECTION))
                boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("closureNoticeDate")
                        .from(startOfGivenDate(toDateTimeUsingDefaultPattern(searchRequest.getNoticeGeneratedFrom()))
                                .toString(ES_DATE_FORMAT))
                        .to(endOfGivenDate(toDateTimeUsingDefaultPattern(searchRequest.getNoticeGeneratedTo()))
                                .toString(ES_DATE_FORMAT)));
            else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_REJECTION))
                boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("rejectionNoticeDate")
                        .from(startOfGivenDate(toDateTimeUsingDefaultPattern(searchRequest.getNoticeGeneratedFrom()))
                                .toString(ES_DATE_FORMAT))
                        .to(endOfGivenDate(toDateTimeUsingDefaultPattern(searchRequest.getNoticeGeneratedTo()))
                                .toString(ES_DATE_FORMAT)));
        }

        if (boolQuery != null) {
            if (StringUtils.isNotBlank(searchRequest.getUlbName()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(ULB_NAME, searchRequest.getUlbName()));
            if (StringUtils.isNotBlank(searchRequest.getShscNumber()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(SHSC_NUMBER, searchRequest.getShscNumber()));
            if (StringUtils.isNotBlank(searchRequest.getApplicantName()))
                boolQuery = boolQuery.filter(QueryBuilders.matchPhrasePrefixQuery(CONSUMER_NAME, searchRequest.getApplicantName()));
            if (StringUtils.isNotBlank(searchRequest.getMobileNumber()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(MOBILE_NUMBER, searchRequest.getMobileNumber()));
            if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WARD, searchRequest.getRevenueWard()));
            if (StringUtils.isNotBlank(searchRequest.getDoorNumber()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(DOOR_NO, searchRequest.getDoorNumber()));
        }
        return boolQuery;
    }

    public List<SewerageIndex> getNoticeSearchResultByBoolQuery(final BoolQueryBuilder boolQuery) {
        List<SewerageIndex> resultList;
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE).withQuery(boolQuery)
                .withSort(new FieldSortBuilder(CONSUMER_NAME).order(SortOrder.DESC)).build();
        resultList = elasticsearchTemplate.queryForList(searchQuery, SewerageIndex.class);
        return resultList;
    }

    public Page<SewerageIndex> getPagedNoticeSearchResultByBoolQuery(final BoolQueryBuilder boolQuery,
            final SewerageNoticeSearchRequest searchRequest) {
        Page<SewerageIndex> resultList;
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE).withQuery(boolQuery)
                .withPageable(new PageRequest(searchRequest.pageNumber(), searchRequest.pageSize(), searchRequest.orderDir(),
                        searchRequest.orderBy()))
                .build();
        resultList = elasticsearchTemplate.queryForPage(searchQuery, SewerageIndex.class);
        return resultList;
    }

    public Map<String, List<SewerageApplicationDetails>> wardWiseBoolQueryFilter(final String ulbName,
            final List<String> wardList, final List<String> propertyTypeList) {
        final Map<String, List<SewerageApplicationDetails>> dcbMap = new HashMap<>();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery(ULB_NAME, ulbName));
        boolQuery = boolQuery.filter(QueryBuilders.termsQuery(PROPERTY_TYPE, propertyTypeList));
        boolQuery = boolQuery.filter(QueryBuilders.termsQuery(WARD, wardList));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery(ACTIVE, true));

        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE).withQuery(boolQuery)
                .withPageable(new PageRequest(0, 250)).withSort(new FieldSortBuilder(APPLICATION_DATE).order(SortOrder.DESC))
                .build();

        final Iterable<SewerageIndex> searchResultList = sewerageIndexRepository.search(searchQuery);

        for (final SewerageIndex indexObj : searchResultList) {
            List<SewerageApplicationDetails> appList;
            final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                    .findByApplicationNumber(indexObj.getApplicationNumber());

            if (sewerageApplicationDetails != null
                    && !sewerageApplicationDetails.getApplicationType().getCode().equals(CLOSESEWERAGECONNECTION))
                if (dcbMap.get(indexObj.getWard()) != null)
                    dcbMap.get(indexObj.getWard()).add(sewerageApplicationDetails);
                else {
                    appList = new ArrayList<>();
                    appList.add(sewerageApplicationDetails);
                    dcbMap.put(indexObj.getWard(), appList);
                }
        }
        return dcbMap;
    }

    public Map<String, List<SewerageApplicationDetails>> wardWiseConnectionQueryFilter(final List<String> propertyTypeList,
            final String ward, final String ulbName) {
        final Map<String, List<SewerageApplicationDetails>> resultMap = new HashMap<>();
        final List<SewerageApplicationDetails> resultList = new ArrayList<>();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery(ULB_NAME, ulbName));
        boolQuery = boolQuery.filter(QueryBuilders.termsQuery(PROPERTY_TYPE, propertyTypeList));
        if (StringUtils.isNotBlank(ward))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WARD, ward));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery(ACTIVE, true));
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE).withQuery(boolQuery)
                .withPageable(new PageRequest(0, 250)).withSort(new FieldSortBuilder(APPLICATION_DATE).order(SortOrder.DESC))
                .build();

        final Iterable<SewerageIndex> indexList = sewerageIndexRepository.search(searchQuery);
        for (final SewerageIndex index : indexList) {
            final List<SewerageApplicationDetails> appList = new ArrayList<>();
            final SewerageApplicationDetails applicationDetails = sewerageApplicationDetailsService
                    .findByApplicationNumber(index.getApplicationNumber());

            if (resultList.isEmpty())
                resultList.add(applicationDetails);
            if (applicationDetails != null) {
                applicationDetails.setOwnerName(index.getConsumerName());

                if (resultMap.isEmpty())
                    resultMap.put(applicationDetails.getApplicationNumber(), resultList);
                else if (resultMap.get(applicationDetails.getApplicationNumber()) != null)
                    resultMap.get(applicationDetails.getApplicationNumber()).add(applicationDetails);
                else {
                    appList.add(applicationDetails);
                    resultMap.put(applicationDetails.getApplicationNumber(), appList);
                }
            }
        }
        return resultMap;
    }

    public List<SewerageNoOfConnReportResult> searchNoOfApplnQuery(final String ward, final String block, final String locality) {
        final List<SewerageNoOfConnReportResult> resultList = new ArrayList<>();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery(ULB_NAME, ApplicationThreadLocals.getCityName()));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery(ACTIVE, true));
        if (StringUtils.isNotBlank(ward))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WARD, ward));
        if (StringUtils.isNotBlank(block))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("revenueBlock", block));
        if (StringUtils.isNotBlank(locality))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("locationName", locality));

        final SearchResponse consolidatedResponse = elasticsearchTemplate.getClient().prepareSearch(SEWERAGE)
                .setQuery(boolQuery).addAggregation(getCountWithGroupingWardAndOrder(GROUPBYFIELD, WARD, WARD, DESC)
                        .subAggregation(getCountWithGroupingWardAndOrder(GROUPBYFIELD, APPLICATION_TYPE, WARD, DESC)))
                .execute().actionGet();

        final Terms terms = consolidatedResponse.getAggregations().get(GROUPBYFIELD);

        for (final Bucket bucket : terms.getBuckets()) {
            final SewerageNoOfConnReportResult resultObject = new SewerageNoOfConnReportResult();
            resultObject.setName(bucket.getKey().toString());

            final Terms subTerms = bucket.getAggregations().get(GROUPBYFIELD);
            for (final Bucket bucket1 : subTerms.getBuckets()) {
                if (APPLICATION_TYPE_NAME_NEWCONNECTION.equals(bucket1.getKey()))
                    resultObject.setNewconnection(bucket1.getDocCount());
                if (APPLICATION_TYPE_NAME_CHANGEINCLOSETS.equals(bucket1.getKey()))
                    resultObject.setChangeinclosets(bucket1.getDocCount());
                if (APPLICATION_TYPE_NAME_CLOSECONNECTION.equals(bucket1.getKey()))
                    resultObject.setCloseconnection(bucket1.getDocCount());
                resultObject.setTotal(
                        resultObject.getNewconnection() + resultObject.getChangeinclosets() + resultObject.getCloseconnection());
            }
            resultList.add(resultObject);
        }
        return resultList;
    }

    public static AggregationBuilder getCountWithGroupingWardAndOrder(final String aggregationName, final String fieldName,
            final String sortField, final String sortOrder) {

        final TermsBuilder aggregation = AggregationBuilders.terms(aggregationName).field(fieldName).size(75);
        if (StringUtils.isNotBlank(sortField) && StringUtils.isNotEmpty(sortField) && WARD.equalsIgnoreCase(sortField)) {
            Boolean order = true;
            if (StringUtils.isNotEmpty(sortOrder) && StringUtils.isNotBlank(sortOrder)
                    && StringUtils.equalsIgnoreCase(sortOrder, DESC))
                order = false;
            aggregation.order(Terms.Order.aggregation("_count", order));
        }
        return aggregation;
    }

    public Page<SewerageIndex> wardwiseBaseRegisterQueryFilter(final String ulbName,
            final List<String> wardList, final SewerageBaseRegisterResult sewerageBaseRegisterResult) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery(ULB_NAME, ulbName));
        boolQuery = boolQuery.filter(QueryBuilders.termsQuery(WARD, wardList));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery(ACTIVE, true));

        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE).withQuery(boolQuery)
                .withPageable(new PageRequest(sewerageBaseRegisterResult.pageNumber(), sewerageBaseRegisterResult.pageSize(),
                        sewerageBaseRegisterResult.orderDir(), sewerageBaseRegisterResult.orderBy()))
                .build();
        return elasticsearchTemplate.queryForPage(searchQuery, SewerageIndex.class);

    }

    public List<SewerageIndex> getAllwardwiseBaseRegisterOrderByShscNumberAsc(final String ulbName,
            final List<String> wardList) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery(ULB_NAME, ulbName));
        boolQuery = boolQuery.filter(QueryBuilders.termsQuery(WARD, wardList));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery(ACTIVE, true));
        final FieldSortBuilder sort = new FieldSortBuilder(SHSC_NUMBER).order(SortOrder.ASC);

        final SearchQuery countQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE).withQuery(boolQuery).build();
        final long count = elasticsearchTemplate.queryForPage(countQuery, SewerageIndex.class).getTotalElements();
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE)
                .withPageable(new PageRequest(0, (int) count)).withSort(sort).withQuery(boolQuery).build();
        return elasticsearchTemplate.queryForList(searchQuery, SewerageIndex.class);

    }

    public List<BigDecimal> getGrandTotal(final String ulbName,
            final List<String> wardList) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery(ULB_NAME, ulbName));
        boolQuery = boolQuery.filter(QueryBuilders.termsQuery(WARD, wardList));
        final List<BigDecimal> totalValues = new ArrayList<>();

        final SearchRequestBuilder searchRequestBuilder = elasticsearchTemplate.getClient()
                .prepareSearch(SEWERAGE).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.sum(ARREARSSUM).field("arrearAmount"))
                .addAggregation(AggregationBuilders.sum(DEMAND_AMOUNT_SUM).field("demandAmount"))
                .addAggregation(AggregationBuilders.sum(TOTALDEMAND_AMOUNT_SUM).field("totalAmount"))
                .addAggregation(AggregationBuilders.sum(COLLECTED_ARREAR_AMOUNT).field(COLLECTED_ARREAR_AMOUNT))
                .addAggregation(AggregationBuilders.sum(COLLECTED_DEMAND_AMOUNT).field(COLLECTED_DEMAND_AMOUNT))
                .addAggregation(AggregationBuilders.sum(EXTRA_ADVANCE_AMOUNT).field(EXTRA_ADVANCE_AMOUNT));

        final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        if (searchResponse != null && searchResponse.getAggregations() != null) {

            final Aggregations collAggr = searchResponse.getAggregations();
            final Sum arresrsaggr = collAggr.get(ARREARSSUM);
            final Sum demanAmountagr = collAggr.get(DEMAND_AMOUNT_SUM);
            final Sum totalDemandaggr = collAggr.get(TOTALDEMAND_AMOUNT_SUM);
            final Sum collectedArrearAmount = collAggr.get(COLLECTED_ARREAR_AMOUNT);
            final Sum collectedDemandAmount = collAggr.get(COLLECTED_DEMAND_AMOUNT);
            final Sum extraAdvanceAmount = collAggr.get(EXTRA_ADVANCE_AMOUNT);

            totalValues.add(BigDecimal.valueOf(arresrsaggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            totalValues.add(BigDecimal.valueOf(demanAmountagr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            totalValues.add(BigDecimal.valueOf(totalDemandaggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            totalValues.add(BigDecimal.valueOf(collectedArrearAmount.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            totalValues.add(BigDecimal.valueOf(collectedDemandAmount.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            totalValues.add(BigDecimal.valueOf(collectedArrearAmount.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)
                    .add(BigDecimal.valueOf(collectedDemandAmount.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
            totalValues.add(BigDecimal.valueOf(extraAdvanceAmount.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        }
        return totalValues;

    }
    
    public Page<SewerageIndex> getOnlinePaymentSearchResult(final BoolQueryBuilder boolQuery, final FieldSortBuilder sort,
            final SewerageConnSearchRequest searchRequest) {
        Page<SewerageIndex> resultList;
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(SEWERAGE).withQuery(boolQuery)
                .withPageable(new PageRequest(searchRequest.pageNumber(), searchRequest.pageSize(),
                        searchRequest.orderDir(), searchRequest.orderBy()))
                .withSort(sort).build();

        resultList = elasticsearchTemplate.queryForPage(searchQuery, SewerageIndex.class);
        return resultList;
    }

    public List<SewerageExecutionResult> getConnectionExecutionList(SewerageExecutionResult sewerageExecutionResult) {

        sewerageExecutionResult.setUlbName(cityService.getCityByURL(ApplicationThreadLocals.getDomainName()).getName());
        final BoolQueryBuilder boolQuery = getSearchQueryForExecuteConnection(sewerageExecutionResult);
        final FieldSortBuilder sort = new FieldSortBuilder(SHSC_NUMBER).order(SortOrder.DESC);
        List<SewerageIndex> searchResultList = getSearchResultForExecuteConnection(boolQuery, sort);
        List<SewerageExecutionResult> connectionExecutionList = new ArrayList<>();
        if (null != searchResultList && !searchResultList.isEmpty()) {
            for (SewerageIndex searchResult : searchResultList) {
                    SewerageExecutionResult executionResult = new SewerageExecutionResult();
                    executionResult.setApplicationType(searchResult.getApplicationType());
                    executionResult.setApplicationDate(DateUtils.getDefaultFormattedDate(searchResult.getApplicationDate()));
                    executionResult.setApplicationNumber(searchResult.getApplicationNumber());
                    executionResult.setShscNumber(searchResult.getShscNumber());
                    executionResult.setRevenueWard(searchResult.getWard());
                    executionResult.setStatus(searchResult.getApplicationStatus());
                    executionResult.setOwnerName(searchResult.getConsumerName());
                    executionResult
                            .setId(sewerageApplicationDetailsService.findByApplicationNumber(searchResult.getApplicationNumber())
                                    .getId());
                    connectionExecutionList.add(executionResult);
                
            }
        }
        return connectionExecutionList;

    }

    public String validateDate(SewerageBulkExecutionResponse sewerageBulkExecutionResponse,
            List<SewerageApplicationDetails> sewerageApplicationDetailsList) {
        final JSONObject obj = new JSONObject(sewerageBulkExecutionResponse);
        final JSONArray jsonArray = obj.getJSONArray("sewerageExecutionResult");
        String status = EMPTY;
        for (int i = 0; i < jsonArray.length(); ++i) {
            final JSONObject jsonobj = jsonArray.getJSONObject(i);
            SewerageApplicationDetails sewerageApplnDetailsObj = sewerageApplicationDetailsService.findBy(jsonobj.getLong("id"));
            if (!jsonobj.getString(EXECUTION_DATE).isEmpty()) {
                sewerageApplnDetailsObj.getConnection()
                        .setExecutionDate(DateUtils.toDateUsingDefaultPattern(jsonobj.getString(EXECUTION_DATE)));
                if (sewerageApplnDetailsObj.getConnection().getExecutionDate() != null
                        && StringUtils.isNotEmpty(sewerageApplnDetailsObj.getConnection().getExecutionDate().toString())) {
                    if (sewerageApplnDetailsObj.getConnection().getExecutionDate()
                            .compareTo(
                                    DateUtils.toDateUsingDefaultPattern(
                                            DateUtils.getDefaultFormattedDate(sewerageApplnDetailsObj.getApplicationDate()))) <= 0) {
                        status = "DateValidationFailed";
                    } else {
                        sewerageApplicationDetailsList.add(sewerageApplnDetailsObj);
                    }
                }
            }
        }
        return status;
    }

    public Boolean update(List<SewerageApplicationDetails> sewerageList) {
        if (!sewerageList.isEmpty()) {
            for (SewerageApplicationDetails sewerageObj : sewerageList) {
                sewerageObj.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_SANCTIONED, MODULETYPE));
                sewerageObj.getConnection().setStatus(SewerageConnectionStatus.ACTIVE);
                sewerageObj.setActive(true);
                final SewerageApplicationDetails parentSewerageAppDtls = sewerageApplicationDetailsService
                        .findByConnection_ShscNumberAndIsActive(sewerageObj.getConnection().getShscNumber());
                if (parentSewerageAppDtls != null) {
                    parentSewerageAppDtls.setActive(false);
                    sewerageObj.setParent(parentSewerageAppDtls);
                }
                sewerageApplicationDetailsService.updateExecutionDate(sewerageObj);
                sewerageApplicationDetailsService.updatePortalMessage(sewerageObj);
                sewerageApplicationDetailsService.updateIndexes(sewerageObj);
            }
            return true;
        } else
            return false;
    }

}