/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.stms.service.es;

import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_TYPE_NAME_CHANGEINCLOSETS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_TYPE_NAME_CLOSECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_TYPE_NAME_NEWCONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CLOSESEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_DONATIONCHARGE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_ESTIMATIONCHARGES_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_SEWERAGETAX_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEE_INSPECTIONCHARGE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.GROUPBYFIELD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULE_NAME;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.egov.collection.entity.es.CollectionDocument;
import org.egov.collection.repository.es.CollectionDocumentRepository;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.stms.elasticSearch.entity.DailySTCollectionReportSearch;
import org.egov.stms.elasticSearch.entity.SewerageCollectFeeSearchRequest;
import org.egov.stms.elasticSearch.entity.SewerageConnSearchRequest;
import org.egov.stms.elasticSearch.entity.SewerageNoticeSearchRequest;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.masters.pojo.SewerageRateDCBResult;
import org.egov.stms.reports.entity.SewerageNoOfConnReportResult;
import org.egov.stms.repository.es.SewerageIndexRepository;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDCBReporService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageIndexService {

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
                ? sewerageApplicationDetails.getStatus().getDescription() : "");
        sewarageIndex.setConsumerNumber(sewerageApplicationDetails.getApplicationNumber());
        sewarageIndex.setApplicationType(sewerageApplicationDetails.getApplicationType() != null
                ? sewerageApplicationDetails.getApplicationType().getName() : "");
        sewarageIndex.setConnectionStatus(sewerageApplicationDetails.getConnection().getStatus() != null
                ? sewerageApplicationDetails.getConnection().getStatus().name() : "");
        sewarageIndex.setCreatedDate(sewerageApplicationDetails.getCreatedDate());
        sewarageIndex.setShscNumber(sewerageApplicationDetails.getConnection().getShscNumber() != null
                ? sewerageApplicationDetails.getConnection().getShscNumber() : "");
        sewarageIndex.setDisposalDate(sewerageApplicationDetails.getDisposalDate());

        sewarageIndex
                .setExecutionDate(sewerageApplicationDetails.getConnection().getExecutionDate());
        sewarageIndex.setIslegacy(sewerageApplicationDetails.getConnection().getLegacy());
        sewarageIndex.setNoOfClosets_nonResidential(
                sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential());
        sewarageIndex
                .setNoOfClosets_residential(sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential());
        sewarageIndex.setPropertyIdentifier(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier() != null
                ? sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier() : "");
        sewarageIndex.setPropertyType(sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null
                ? sewerageApplicationDetails.getConnectionDetail().getPropertyType().name() : "");
        if (sewerageApplicationDetails.getEstimationDate() != null)
            sewarageIndex.setEstimationDate(sewerageApplicationDetails.getEstimationDate());
        sewarageIndex
                .setEstimationNumber(sewerageApplicationDetails.getEstimationNumber() != null ? sewerageApplicationDetails
                        .getEstimationNumber() : "");
        if (sewerageApplicationDetails.getWorkOrderDate() != null)
            sewarageIndex.setWorkOrderDate(sewerageApplicationDetails.getWorkOrderDate());
        sewarageIndex
                .setWorkOrderNumber(sewerageApplicationDetails.getWorkOrderNumber() != null ? sewerageApplicationDetails
                        .getWorkOrderNumber() : "");
        if (sewerageApplicationDetails.getClosureNoticeDate() != null)
            sewarageIndex
                    .setClosureNoticeDate(sewerageApplicationDetails.getClosureNoticeDate());
        sewarageIndex
                .setClosureNoticeNumber(
                        sewerageApplicationDetails.getClosureNoticeNumber() != null ? sewerageApplicationDetails
                                .getClosureNoticeNumber() : "");
        if (sewerageApplicationDetails.getRejectionDate() != null)
            sewarageIndex
                    .setRejectionNoticeDate(sewerageApplicationDetails.getRejectionDate());
        sewarageIndex
                .setRejectionNoticeNumber(
                        sewerageApplicationDetails.getRejectionNumber() != null ? sewerageApplicationDetails
                                .getRejectionNumber() : "");
        Iterator<OwnerName> ownerNameItr = null;
        if (null != assessmentDetails.getOwnerNames())
            ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        final StringBuilder consumerName = new StringBuilder();
        final StringBuilder mobileNumber = new StringBuilder();
        if (null != ownerNameItr && ownerNameItr.hasNext()) {
            final OwnerName primaryOwner = ownerNameItr.next();
            consumerName.append(primaryOwner.getOwnerName() != null ? primaryOwner.getOwnerName() : "");
            mobileNumber.append(primaryOwner.getMobileNumber() != null ? primaryOwner.getMobileNumber() : "");
            while (ownerNameItr.hasNext()) {
                final OwnerName secondaryOwner = ownerNameItr.next();
                consumerName.append(",").append(secondaryOwner.getOwnerName() != null ? secondaryOwner.getOwnerName() : "");
                mobileNumber.append(",")
                        .append(secondaryOwner.getMobileNumber() != null ? secondaryOwner.getMobileNumber() : "");
            }
        }
        sewarageIndex.setMobileNumber(mobileNumber.toString());
        sewarageIndex.setConsumerName(consumerName.toString());
        sewarageIndex.setDoorNo(assessmentDetails.getHouseNo() != null ? assessmentDetails.getHouseNo() : "");
        sewarageIndex.setWard(
                assessmentDetails.getBoundaryDetails() != null ? assessmentDetails.getBoundaryDetails().getWardName() : "");
        sewarageIndex.setRevenueBlock(
                assessmentDetails.getBoundaryDetails() != null ? assessmentDetails.getBoundaryDetails().getBlockName() : "");
        sewarageIndex.setLocationName(
                assessmentDetails.getBoundaryDetails() != null ? assessmentDetails.getBoundaryDetails().getLocalityName() : "");
        sewarageIndex
                .setAddress(assessmentDetails.getPropertyAddress() != null ? assessmentDetails.getPropertyAddress() : "");
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
            sewarageIndex.setPeriod(period != null ? period : StringUtils.EMPTY);

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

    // TODO: CHECK LIKE CASES WORKING OR NOT IN CASE OF SEARCH BY CONSUMER NAME
    public BoolQueryBuilder getQueryFilter(final SewerageConnSearchRequest searchRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("active", true));
        if (StringUtils.isNotBlank(searchRequest.getConsumerNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerNumber", searchRequest.getConsumerNumber()));
        if (StringUtils.isNotBlank(searchRequest.getShscNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("shscNumber", searchRequest.getShscNumber()));
        if (StringUtils.isNotBlank(searchRequest.getApplicantName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerName", searchRequest.getApplicantName()));
        if (StringUtils.isNotBlank(searchRequest.getMobileNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("mobileNumber", searchRequest.getMobileNumber()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("ward", searchRequest.getRevenueWard()));
        if (StringUtils.isNotBlank(searchRequest.getDoorNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("doorNo", searchRequest.getDoorNumber()));
        if (searchRequest.getLegacy() != null)
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("islegacy", searchRequest.getLegacy()));
        return boolQuery;
    }

    public List<SewerageIndex> getSearchResultByBoolQuery(final BoolQueryBuilder boolQuery, final FieldSortBuilder sort) {
        List<SewerageIndex> resultList;
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("sewerage").withQuery(boolQuery)
                .withSort(sort).build();
        resultList = elasticsearchTemplate.queryForList(searchQuery, SewerageIndex.class);
        return resultList;
    }

    public BoolQueryBuilder getSearchQueryFilter(final SewerageCollectFeeSearchRequest searchRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery("ulbName", searchRequest.getUlbName()));
        if (StringUtils.isNotBlank(searchRequest.getConsumerNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerNumber", searchRequest.getConsumerNumber()));
        if (StringUtils.isNotBlank(searchRequest.getShscNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("shscNumber", searchRequest.getShscNumber()));
        if (StringUtils.isNotBlank(searchRequest.getApplicantName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerName", searchRequest.getApplicantName()));
        if (StringUtils.isNotBlank(searchRequest.getMobileNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("mobileNumber", searchRequest.getMobileNumber()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("ward", searchRequest.getRevenueWard()));
        if (StringUtils.isNotBlank(searchRequest.getDoorNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("doorNo", searchRequest.getDoorNumber()));
        return boolQuery;
    }

    public List<SewerageIndex> getCollectSearchResult(final BoolQueryBuilder boolQuery, final FieldSortBuilder sort) {
        List<SewerageIndex> resultList;
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("sewerage").withQuery(boolQuery)
                .withSort(sort).build();

        resultList = elasticsearchTemplate.queryForList(searchQuery, SewerageIndex.class);
        return resultList;
    }

    public BoolQueryBuilder getDCRSearchResult(final DailySTCollectionReportSearch searchRequest) throws ParseException {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery("cityName", searchRequest.getUlbName()));
        if (StringUtils.isNotBlank(searchRequest.getFromDate()))
            boolQuery = boolQuery.filter(QueryBuilders.rangeQuery("receiptDate")
                    .gte(newFormat.format(formatter.parse(searchRequest.getFromDate())))
                    .lte(new DateTime(newFormat.format(formatter.parse(searchRequest.getToDate()))).plusDays(1).toDate()));
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
                .filter(QueryBuilders.matchQuery("ulbName", searchRequest.getUlbName()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("ward", searchRequest.getRevenueWard()));
        return boolQuery;
    }

    public List<DailySTCollectionReportSearch> getDCRSewerageReportResult(final DailySTCollectionReportSearch searchRequest,
            final BoolQueryBuilder boolQuery) throws ParseException {
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

            final SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            final SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            dcrReportObject.setReceiptDate(myFormat.format(dateFormat.parse(collectionObject.getReceiptDate().toString())));
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
        if (searchRequest.getNoticeType() != null)
            if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_WORK_ORDER))
                boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("workOrderDate")
                        .from(searchRequest.getNoticeGeneratedFrom())
                        .to(searchRequest.getNoticeGeneratedTo()));
            else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_ESTIMATION))
                boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("estimationDate")
                        .from(searchRequest.getNoticeGeneratedFrom())
                        .to(searchRequest.getNoticeGeneratedTo()));
            else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_CLOSE_CONNECTION))
                boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("closureNoticeDate")
                        .from(searchRequest.getNoticeGeneratedFrom())
                        .to(searchRequest.getNoticeGeneratedTo()));
            else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_REJECTION))
                boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("rejectionNoticeDate")
                        .from(searchRequest.getNoticeGeneratedFrom())
                        .to(searchRequest.getNoticeGeneratedTo()));
        if (boolQuery != null) {
            if (StringUtils.isNotBlank(searchRequest.getUlbName()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery("ulbName", searchRequest.getUlbName()));
            if (StringUtils.isNotBlank(searchRequest.getShscNumber()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery("shscNumber", searchRequest.getShscNumber()));
            if (StringUtils.isNotBlank(searchRequest.getApplicantName()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerName", searchRequest.getApplicantName()));
            if (StringUtils.isNotBlank(searchRequest.getMobileNumber()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery("mobileNumber", searchRequest.getMobileNumber()));
            if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery("ward", searchRequest.getRevenueWard()));
            if (StringUtils.isNotBlank(searchRequest.getDoorNumber()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery("doorNo", searchRequest.getDoorNumber()));
        }
        return boolQuery;
    }

    public List<SewerageIndex> getNoticeSearchResultByBoolQuery(final BoolQueryBuilder boolQuery) {
        List<SewerageIndex> resultList;
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("sewerage").withQuery(boolQuery)
                .withSort(new FieldSortBuilder("consumerName").order(SortOrder.DESC)).build();
        resultList = elasticsearchTemplate.queryForList(searchQuery, SewerageIndex.class);
        return resultList;
    }

    public Map<String, List<SewerageApplicationDetails>> wardWiseBoolQueryFilter(final String ulbName,
            final List<String> wardList, final List<String> propertyTypeList) {
        final Map<String, List<SewerageApplicationDetails>> dcbMap = new HashMap<>();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("ulbName", ulbName));
        boolQuery = boolQuery.filter(QueryBuilders.termsQuery("propertyType", propertyTypeList));
        boolQuery = boolQuery.filter(QueryBuilders.termsQuery("ward", wardList));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery("active", true));

        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("sewerage").withQuery(boolQuery)
                .withPageable(new PageRequest(0, 250)).withSort(new FieldSortBuilder("applicationDate").order(SortOrder.DESC))
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
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("ulbName", ulbName));
        boolQuery = boolQuery.filter(QueryBuilders.termsQuery("propertyType", propertyTypeList));
        if (StringUtils.isNotBlank(ward))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("ward", ward));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery("active", true));
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("sewerage").withQuery(boolQuery)
                .withPageable(new PageRequest(0, 250)).withSort(new FieldSortBuilder("applicationDate").order(SortOrder.DESC))
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
                .filter(QueryBuilders.matchQuery("ulbName", ApplicationThreadLocals.getCityName()));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery("active", true));
        if (StringUtils.isNotBlank(ward))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("ward", ward));
        if (StringUtils.isNotBlank(block))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("revenueBlock", block));
        if (StringUtils.isNotBlank(locality))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("locationName", locality));

        final SearchResponse consolidatedResponse = elasticsearchTemplate.getClient().prepareSearch("sewerage")
                .setQuery(boolQuery).addAggregation(getCountWithGroupingWardAndOrder(GROUPBYFIELD, "ward", "ward", "desc")
                        .subAggregation(getCountWithGroupingWardAndOrder(GROUPBYFIELD, "applicationType", "ward", "desc")))
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
        if (StringUtils.isNotBlank(sortField) && StringUtils.isNotEmpty(sortField) && "ward".equalsIgnoreCase(sortField)) {
            Boolean order = true;
            if (StringUtils.isNotEmpty(sortOrder) && StringUtils.isNotBlank(sortOrder)
                    && StringUtils.equalsIgnoreCase(sortOrder, "desc"))
                order = false;
            aggregation.order(Terms.Order.aggregation("_count", order));
        }
        return aggregation;
    }

    public List<SewerageIndex> wardwiseBaseRegisterQueryFilter(final String ulbName,
            final List<String> wardList) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("ulbName", ulbName));
        boolQuery = boolQuery.filter(QueryBuilders.termsQuery("ward", wardList));
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery("active", true));

        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("sewerage").withQuery(boolQuery)
                .withPageable(new PageRequest(0, 250)).withSort(new FieldSortBuilder("shscNumber").order(SortOrder.DESC))
                .build();
        // FIXME: DONOT HARDCODE 250 ITEMS .
        final List<SewerageIndex> searchResultList = elasticsearchTemplate.queryForList(searchQuery, SewerageIndex.class);
        return searchResultList;
    }

}