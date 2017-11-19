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
package org.egov.lcms.transactions.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.lcms.entity.es.HearingsDocument;
import org.egov.lcms.entity.es.LegalCaseDocument;
import org.egov.lcms.reports.entity.LegalCommonReportResult;
import org.egov.lcms.repository.es.HearingsDocumentRepository;
import org.egov.lcms.repository.es.LegalCaseDocumentRepository;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class LegalCommonReportService {

    private static final String COURTNAME = "courtName";
    private static final String PETITIONTYPE = "petitionType";
    private static final String CASETYPE = "caseType";
    private static final String COURTTYPE = "courtType";
    private static final String CASESTATUS = "status";
    private static final String OFFICERINCHRGE = "officerIncharge";
    private static final String STANDINGCOUNSEL = "advocateName";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";
    private static final String CASE_DATE = "caseDate";
    private static final String JUDGEMENTOUTCOME = "judgmentOutcome";

    @Autowired
    private LegalCaseDocumentRepository legalCaseDocumentRepository;

    @Autowired
    private HearingsDocumentRepository hearingsDocumentRepository;

    public List<LegalCommonReportResult> getLegalCommonReportsResults(final LegalCommonReportResult legalCommonReport,
            final String reportType) throws ParseException {

        final List<LegalCaseDocument> legalcaseDocumentList = findAllLegalcaseDocumentIndexByFilter(legalCommonReport,
                reportType);
        return prepareLegalCaseDocumentList(legalcaseDocumentList);

    }

    public List<LegalCommonReportResult> prepareLegalCaseDocumentList(final List<LegalCaseDocument> legalcaseDocumentList)
            throws ParseException {
        final List<LegalCommonReportResult> documentList = new ArrayList<>(0);
        for (final LegalCaseDocument legalcaseDocument : legalcaseDocumentList)
            documentList.addAll(buildLegalReport(legalcaseDocument));
        return documentList;

    }

    public List<LegalCaseDocument> findAllLegalcaseDocumentIndexByFilter(final LegalCommonReportResult legalCommonReportResult,
            final String reportType) throws ParseException {

        final Iterable<LegalCaseDocument> legalcaseDocumentSearchList = getLegalcaseIndex(legalCommonReportResult, reportType);
        final List<LegalCaseDocument> legalcaseDocumentList = new ArrayList<>();
        for (final LegalCaseDocument documentObj : legalcaseDocumentSearchList)
            legalcaseDocumentList.add(documentObj);

        return legalcaseDocumentList;
    }

    private Iterable<LegalCaseDocument> getLegalcaseIndex(final LegalCommonReportResult legalCommonReportResult,
            final String reportType)
            throws ParseException {
        final BoolQueryBuilder query = getFilterQuery(legalCommonReportResult, reportType);
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(LcmsConstants.LEGALCASE_INDEX_NAME)
                .withQuery(query).withPageable(new PageRequest(0, 250)).build();

        final Iterable<LegalCaseDocument> legalcaseDocumentSearchList = legalCaseDocumentRepository.search(searchQuery);
        return legalcaseDocumentSearchList;
    }

    public List<LegalCommonReportResult> buildLegalReport(final LegalCaseDocument legalcaseDocument)
            throws ParseException {
        final List<LegalCommonReportResult> finalResult = new ArrayList<>();

        final SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        final SimpleDateFormat myFormat = new SimpleDateFormat(LcmsConstants.DATE_FORMAT_DDMMYYYY);
        final LegalCommonReportResult legalCommonResultObj = new LegalCommonReportResult();
        legalCommonResultObj.setCaseNumber(legalcaseDocument.getCaseNumber() != null ? legalcaseDocument.getCaseNumber() : "");
        legalCommonResultObj.setLcNumber(legalcaseDocument.getLcNumber() != null ? legalcaseDocument.getLcNumber() : "");
        legalCommonResultObj.setCaseTitle(legalcaseDocument.getCaseTitle() != null ? legalcaseDocument.getCaseTitle() : "");
        legalCommonResultObj.setCourtName(legalcaseDocument.getCourtName() != null ? legalcaseDocument.getCourtName() : "");
        legalCommonResultObj
                .setPetitionerName(legalcaseDocument.getPetitionerNames() != null ? legalcaseDocument.getPetitionerNames() : "");
        legalCommonResultObj
                .setRespondantName(legalcaseDocument.getRespondantNames() != null ? legalcaseDocument.getRespondantNames() : "");
        legalCommonResultObj
                .setStandingCounsel(legalcaseDocument.getAdvocateName() != null ? legalcaseDocument.getAdvocateName() : "");
        legalCommonResultObj
                .setOfficerIncharge(legalcaseDocument.getOfficerIncharge() != null ? legalcaseDocument.getOfficerIncharge() : "");
        legalCommonResultObj.setCaseStatus(legalcaseDocument.getStatus() != null ? legalcaseDocument.getStatus() : "");
        legalCommonResultObj.setReportStatus(legalcaseDocument.getSubStatus() != null ? legalcaseDocument.getSubStatus() : "");
        legalCommonResultObj
                .setPetitionType(legalcaseDocument.getPetitionType() != null ? legalcaseDocument.getPetitionType() : "");
        legalCommonResultObj
                .setNextDate(legalcaseDocument.getNextDate() != null
                        ? myFormat.format(dateFormat.parse(legalcaseDocument.getNextDate().toString())) : "");
        finalResult.add(legalCommonResultObj);
        return finalResult;

    }

    private BoolQueryBuilder getFilterQuery(final LegalCommonReportResult searchRequest, final String reportType)
            throws ParseException {
        final SimpleDateFormat formatter = new SimpleDateFormat(LcmsConstants.DATE_FORMAT);
        final SimpleDateFormat newFormat = new SimpleDateFormat(ApplicationConstant.ES_DATE_FORMAT);
        final Map<String, Integer> monthValuesMapnumber = LegalCaseUtil.getAllMonthsInNumber();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery("cityName", ApplicationThreadLocals.getCityName()));
        if (StringUtils.isNotBlank(searchRequest.getAggregatedByValue())) {

            if (searchRequest.getAggregatedBy().equals(LcmsConstants.COURTNAME))
                boolQuery.filter(QueryBuilders.matchQuery(COURTNAME, searchRequest.getAggregatedByValue()));
            if (searchRequest.getAggregatedBy().equals(LcmsConstants.PETITIONTYPE))
                boolQuery.filter(QueryBuilders.matchQuery(PETITIONTYPE, searchRequest.getAggregatedByValue()));
            else if (searchRequest.getAggregatedBy().equals(LcmsConstants.CASECATEGORY))
                boolQuery.filter(QueryBuilders.matchQuery(CASETYPE, searchRequest.getAggregatedByValue()));
            else if (searchRequest.getAggregatedBy().equals(LcmsConstants.CASESTATUS))
                boolQuery.filter(QueryBuilders.matchQuery(CASESTATUS, searchRequest.getAggregatedByValue()));
            else if (searchRequest.getAggregatedBy().equals(LcmsConstants.COURTTYPE))
                boolQuery.filter(QueryBuilders.matchQuery(COURTTYPE, searchRequest.getAggregatedByValue()));
            else if (searchRequest.getAggregatedBy().equals(LcmsConstants.OFFICERINCHRGE))
                boolQuery.filter(QueryBuilders.matchQuery(OFFICERINCHRGE, searchRequest.getAggregatedByValue().split("@")[0]));
            else if (searchRequest.getAggregatedBy().equals(LcmsConstants.STANDINGCOUNSEL))
                boolQuery.filter(QueryBuilders.matchQuery(STANDINGCOUNSEL, searchRequest.getAggregatedByValue()));
            else if (searchRequest.getAggregatedBy().equals(LcmsConstants.JUDGEMENTOUTCOME))
                boolQuery.filter(QueryBuilders.matchQuery(JUDGEMENTOUTCOME, searchRequest.getAggregatedByValue()));
        }
        if (StringUtils.isNotBlank(searchRequest.getMonth()) && StringUtils.isNotBlank(searchRequest.getYear())) {
            final Integer monthName = monthValuesMapnumber.get(searchRequest.getMonth());
            // Prepare the start date based on the month number and year
            final String monthYearStartDateStr = searchRequest.getYear().concat("-")
                    .concat(monthName.toString()).concat("-").concat("01");
            final LocalDate monthYearStDate = new LocalDate(monthYearStartDateStr);
            // Fetch the start date of the 1st week of the month and the last day of the month
            if (MONTH.equalsIgnoreCase(searchRequest.getPeriod())) {
                final LocalDate weekStart = monthYearStDate.dayOfWeek().withMinimumValue();
                final LocalDate endOfMonth = monthYearStDate.dayOfMonth().withMaximumValue();
                final String startDate = weekStart.toString(LcmsConstants.DATE_FORMAT);
                final String endDate = endOfMonth.toString(LcmsConstants.DATE_FORMAT);
                boolQuery = boolQuery.filter(QueryBuilders.rangeQuery(CASE_DATE)
                        .gte(newFormat.format(formatter.parse(startDate)))
                        .lte(new DateTime(newFormat.format(formatter.parse(endDate)))));
            }
            if (YEAR.equalsIgnoreCase(searchRequest.getPeriod())) {
                final LocalDate monthStart = monthYearStDate.dayOfMonth().withMinimumValue();
                final LocalDate endOfYear = monthYearStDate.dayOfYear().withMaximumValue();
                final String startDate = monthStart.toString(LcmsConstants.DATE_FORMAT);
                final String endDate = endOfYear.toString(LcmsConstants.DATE_FORMAT);
                boolQuery = boolQuery.filter(QueryBuilders.rangeQuery(CASE_DATE)
                        .gte(newFormat.format(formatter.parse(startDate)))
                        .lte(new DateTime(newFormat.format(formatter.parse(endDate)))));
            }

        }
        if (reportType != null) {
            if (reportType.equals(LcmsConstants.DUEPWRREPORT))
                boolQuery.mustNot(QueryBuilders.existsQuery("pwrDueDate"));
            else if (reportType.equals(LcmsConstants.DUECAREPORT))
                boolQuery.must(QueryBuilders.existsQuery("caDueDate"));

            else if (reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT))
                boolQuery.mustNot(QueryBuilders.matchQuery("status", LcmsConstants.LEGALCASE_STATUS_CLOSED_DESC));

            if (reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT)
                    && StringUtils.isNotBlank(searchRequest.getCaseFromDate()))
                boolQuery = boolQuery.filter(QueryBuilders.rangeQuery("judgmentImplDate")
                        .gte(newFormat.format(formatter.parse(searchRequest.getCaseFromDate())))
                        .lte(new DateTime(newFormat.format(formatter.parse(searchRequest.getCaseToDate())))));

            if (reportType.equals(LcmsConstants.DUEEMPLOYEEHEARINGREPORT)) {
                final List<String> statusCodeList = new ArrayList<>();
                statusCodeList.add(LcmsConstants.LEGALCASE_STATUS_HEARING_DESC);
                statusCodeList.add(LcmsConstants.LEGALCASE_INTERIMSTAY_STATUS_DESC);
                boolQuery.must(QueryBuilders.termsQuery(CASESTATUS, statusCodeList));
                if (searchRequest.getLcNumber() != null)
                    boolQuery = boolQuery.filter(QueryBuilders.matchQuery("lcNumber", searchRequest.getLcNumber()));

                if (StringUtils.isNotBlank(searchRequest.getCaseFromDate()))
                    boolQuery = boolQuery.filter(QueryBuilders.rangeQuery(CASE_DATE)
                            .gte(newFormat.format(formatter.parse(searchRequest.getCaseFromDate())))
                            .lte(new DateTime(newFormat.format(formatter.parse(searchRequest.getCaseToDate())))));

            }
        } else if (StringUtils.isNotBlank(searchRequest.getCaseFromDate()))
            boolQuery = boolQuery.filter(QueryBuilders.rangeQuery(CASE_DATE)
                    .gte(newFormat.format(formatter.parse(searchRequest.getCaseFromDate())))
                    .lte(new DateTime(newFormat.format(formatter.parse(searchRequest.getCaseToDate())))));

        if (StringUtils.isNotBlank(searchRequest.getOfficerIncharge()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(OFFICERINCHRGE, searchRequest.getOfficerIncharge().split("@")[0]));
        if (StringUtils.isNotBlank(searchRequest.getCaseCategory()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(CASETYPE, searchRequest.getCaseCategory()));
        if (StringUtils.isNotBlank(searchRequest.getCourtName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(COURTNAME, searchRequest.getCourtName()));
        if (StringUtils.isNotBlank(searchRequest.getCaseStatus()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(CASESTATUS, searchRequest.getCaseStatus()));
        if (StringUtils.isNotBlank(searchRequest.getCourtType()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(COURTTYPE, searchRequest.getCourtType()));
        if (StringUtils.isNotBlank(searchRequest.getPetitionType()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(PETITIONTYPE, searchRequest.getPetitionType()));
        if (StringUtils.isNotBlank(searchRequest.getJudgmentType()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(JUDGEMENTOUTCOME, searchRequest.getJudgmentType()));
        if (StringUtils.isNotBlank(searchRequest.getStandingCounsel()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery("advocateName", searchRequest.getStandingCounsel()));
        if (StringUtils.isNotBlank(searchRequest.getReportStatus()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery("subStatus", searchRequest.getReportStatus()));

        return boolQuery;
    }

    public List<LegalCommonReportResult> getEmployeeHearingResult(final LegalCommonReportResult legalCommonReport,
            final String reportType)
            throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        final SimpleDateFormat myFormat = new SimpleDateFormat(LcmsConstants.DATE_FORMAT_DDMMYYYY);
        BoolQueryBuilder query;
        SearchQuery searchQuery;
        Iterable<LegalCaseDocument> legalcaseDocumentSearchList;
        final List<LegalCommonReportResult> finalResult = new ArrayList<>();
        final List<HearingsDocument> hearingsDocumentList = findAllHearingsIndexByFilter(legalCommonReport);
        for (final HearingsDocument hearingsDocument : hearingsDocumentList) {
            legalCommonReport.setLcNumber(hearingsDocument.getLcNumber());
            query = getFilterQuery(legalCommonReport, reportType);
            searchQuery = new NativeSearchQueryBuilder().withIndices(LcmsConstants.LEGALCASE_INDEX_NAME)
                    .withQuery(query).build();
            legalcaseDocumentSearchList = legalCaseDocumentRepository.search(searchQuery);
            LegalCommonReportResult hearingsDueReportResultObj;
            if (legalcaseDocumentSearchList != null)
                for (final LegalCaseDocument legalcaseDocumentIndex : legalcaseDocumentSearchList) {
                    hearingsDueReportResultObj = new LegalCommonReportResult();
                    hearingsDueReportResultObj.setCaseNumber(legalcaseDocumentIndex.getCaseNumber());
                    hearingsDueReportResultObj.setLcNumber(legalcaseDocumentIndex.getLcNumber());
                    hearingsDueReportResultObj.setCaseTitle(legalcaseDocumentIndex.getCaseTitle());
                    hearingsDueReportResultObj.setCaseNumber(legalcaseDocumentIndex.getCaseNumber());
                    hearingsDueReportResultObj.setCourtName(legalcaseDocumentIndex.getCourtName());
                    hearingsDueReportResultObj.setPetitionerName(legalcaseDocumentIndex.getPetitionerNames());
                    hearingsDueReportResultObj.setRespondantName(legalcaseDocumentIndex.getRespondantNames());
                    hearingsDueReportResultObj.setStandingCounsel(legalcaseDocumentIndex.getAdvocateName());
                    hearingsDueReportResultObj.setOfficerIncharge(legalcaseDocumentIndex.getOfficerIncharge());

                    hearingsDueReportResultObj
                            .setNextDate(myFormat.format(dateFormat.parse(legalcaseDocumentIndex.getNextDate().toString())));
                    finalResult.add(hearingsDueReportResultObj);
                }

        }
        return finalResult;
    }

    public List<HearingsDocument> findAllHearingsIndexByFilter(final LegalCommonReportResult searchRequest)
            throws ParseException {
        final BoolQueryBuilder query = getHearingsFilterQuery(searchRequest);
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(LcmsConstants.HEARINGS_INDEX_NAME)
                .withQuery(query).withPageable(new PageRequest(0, 250)).build();
        final Iterable<HearingsDocument> hearingsDocumentSearchList = hearingsDocumentRepository.search(searchQuery);
        final List<HearingsDocument> hearingsDocumentList = new ArrayList<>();
        for (final HearingsDocument documentObj : hearingsDocumentSearchList)
            hearingsDocumentList.add(documentObj);

        return hearingsDocumentList;

    }

    private BoolQueryBuilder getHearingsFilterQuery(final LegalCommonReportResult searchRequest) throws ParseException {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat newFormat = new SimpleDateFormat(ApplicationConstant.ES_DATE_FORMAT);
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        boolQuery = boolQuery.filter(QueryBuilders.termQuery("cityName", ApplicationThreadLocals.getCityName()));

        if (StringUtils.isNotBlank(searchRequest.getCaseFromDate()))
            boolQuery = boolQuery.filter(QueryBuilders.rangeQuery("hearingDate")
                    .gte(newFormat.format(formatter.parse(searchRequest.getCaseFromDate())))
                    .lte(new DateTime(newFormat.format(formatter.parse(searchRequest.getCaseToDate())))));

        return boolQuery;
    }

}
