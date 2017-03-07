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
package org.egov.lcms.transactions.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.lcms.entity.es.LegalCaseDocument;
import org.egov.lcms.reports.entity.LcDueReportResult;
import org.egov.lcms.repository.es.LegalCaseDocumentRepository;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LcDueReportService {

    @Autowired
    private LegalCaseDocumentRepository legalCaseDocumentRepository;

    public void buildLcDueReport(final LegalCaseDocument legalcaseDocument, final List<LcDueReportResult> finalResult)
            throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        final SimpleDateFormat myFormat = new SimpleDateFormat(LcmsConstants.DATE_FORMAT_DDMMYYYY);
        final LcDueReportResult dueReportResultObj = new LcDueReportResult();
        dueReportResultObj.setCaseNumber(legalcaseDocument.getCaseNumber());
        dueReportResultObj.setLcNumber(legalcaseDocument.getLcNumber());
        dueReportResultObj.setCaseTitle(legalcaseDocument.getCaseTitle());
        dueReportResultObj.setCaseNumber(legalcaseDocument.getCaseNumber());
        dueReportResultObj.setCourtName(legalcaseDocument.getCourtName());
        dueReportResultObj.setPetName(legalcaseDocument.getPetitionerNames());
        dueReportResultObj.setResName(legalcaseDocument.getRespondantNames());
        dueReportResultObj.setStandingCounsel(legalcaseDocument.getAdvocateName());
        dueReportResultObj.setOfficerIncharge(legalcaseDocument.getOfficerIncharge());
        dueReportResultObj
                .setNextDate(myFormat.format(dateFormat.parse(legalcaseDocument.getNextDate().toString())));
        finalResult.add(dueReportResultObj);
    }

    public List<LegalCaseDocument> findAllLegalcaseDocumentIndexByFilter(final LcDueReportResult dueReportResult,
            final String reportType)
            throws ParseException {

        final BoolQueryBuilder query = getFilterQuery(dueReportResult, reportType);
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(LcmsConstants.LEGALCASE_INDEX_NAME)
                .withQuery(query).withPageable(new PageRequest(0, 250)).build();

        final Iterable<LegalCaseDocument> legalcaseDocumentSearchList = legalCaseDocumentRepository.search(searchQuery);
        final List<LegalCaseDocument> legalcaseDocumentList = new ArrayList<>();
        for (final LegalCaseDocument documentObj : legalcaseDocumentSearchList)
            legalcaseDocumentList.add(documentObj);

        return legalcaseDocumentList;
    }

    private BoolQueryBuilder getFilterQuery(final LcDueReportResult searchRequest, final String reportType)
            throws ParseException {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat newFormat = new SimpleDateFormat(ApplicationConstant.ES_DATE_FORMAT);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery("cityName", ApplicationThreadLocals.getCityName()));
        if (reportType.equals(LcmsConstants.DUEPWRREPORT))
            boolQuery.mustNot(QueryBuilders.existsQuery("pwrDueDate"));
        else if (reportType.equals(LcmsConstants.DUECAREPORT))
            boolQuery.must(QueryBuilders.existsQuery("caDueDate"));

        else if (reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT))
            boolQuery.mustNot(QueryBuilders.matchQuery("status", LcmsConstants.LEGALCASE_STATUS_CLOSED_DESC));

        if (StringUtils.isNotBlank(searchRequest.getFromDate()) && !reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT))
            boolQuery = boolQuery.filter(QueryBuilders.rangeQuery("caseDate")
                    .gte(newFormat.format(formatter.parse(searchRequest.getFromDate())))
                    .lte(new DateTime(newFormat.format(formatter.parse(searchRequest.getToDate())))));

        if (StringUtils.isNotBlank(searchRequest.getOfficerIncharge()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery("officerIncharge", searchRequest.getOfficerIncharge().split("@")[0]));
        if (reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT) && StringUtils.isNotBlank(searchRequest.getFromDate()))
            boolQuery = boolQuery.filter(QueryBuilders.rangeQuery("judgmentImplDate")
                    .gte(newFormat.format(formatter.parse(searchRequest.getFromDate())))
                    .lte(new DateTime(newFormat.format(formatter.parse(searchRequest.getToDate())))));

        return boolQuery;
    }

}
