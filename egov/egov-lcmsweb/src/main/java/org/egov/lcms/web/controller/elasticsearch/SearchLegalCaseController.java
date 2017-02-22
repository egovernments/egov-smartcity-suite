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
package org.egov.lcms.web.controller.elasticsearch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.lcms.entity.es.LegalCaseDocument;
import org.egov.lcms.reports.entity.LegalCaseSearchResult;
import org.egov.lcms.repository.es.LegalCaseDocumentRepository;
import org.egov.lcms.transactions.entity.ReportStatus;
import org.egov.lcms.transactions.service.SearchLegalCaseService;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.egov.lcms.web.controller.transactions.GenericLegalCaseController;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/elasticsearch/legalcasesearch")
public class SearchLegalCaseController extends GenericLegalCaseController {

    @Autowired
    private SearchLegalCaseService searchLegalCaseService;

    @Autowired
    private LegalCaseDocumentRepository legalCaseDocumentRepository;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @ModelAttribute
    public LegalCaseSearchResult searchRequest() {
        return new LegalCaseSearchResult();
    }

    public @ModelAttribute("statusList") List<EgwStatus> getStatusList() {
        return legalCaseUtil.getStatusForModule();
    }

    public @ModelAttribute("reportStatusList") List<ReportStatus> getReportStatusList() {
        return searchLegalCaseService.getReportStatus();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String searchForm(final Model model) {
        model.addAttribute("currDate", new Date());
        return "legalcasesearch-from";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<LegalCaseSearchResult> searchLegalcase(@ModelAttribute final LegalCaseSearchResult searchRequest)
            throws ParseException {
        List<LegalCaseDocument> legalcaseDocumentTempList = new ArrayList<LegalCaseDocument>();
        final List<LegalCaseSearchResult> finalResult = new ArrayList<LegalCaseSearchResult>();
        legalcaseDocumentTempList = findAllLegalcaseDocumentIndexByFilter(searchRequest);
        for (final LegalCaseDocument legalcaseDocumentIndex : legalcaseDocumentTempList) {
            final LegalCaseSearchResult legalcaseSearchResultObj = new LegalCaseSearchResult();
            legalcaseSearchResultObj.setLcNumber(legalcaseDocumentIndex.getLcNumber());
            legalcaseSearchResultObj.setCaseNumber(legalcaseDocumentIndex.getCaseNumber());
            legalcaseSearchResultObj.setCaseTitle(legalcaseDocumentIndex.getCaseTitle());
            legalcaseSearchResultObj.setCourtName(legalcaseDocumentIndex.getCourtName());
            legalcaseSearchResultObj.setStandingCouncil(legalcaseDocumentIndex.getAdvocateName());
            legalcaseSearchResultObj.setCaseStatus(legalcaseDocumentIndex.getStatus());
            legalcaseSearchResultObj.setPetName(legalcaseDocumentIndex.getPetitionerNames());
            legalcaseSearchResultObj.setResName(legalcaseDocumentIndex.getRespondantNames());
            finalResult.add(legalcaseSearchResultObj);
        }
        return finalResult;

    }

    public List<LegalCaseDocument> findAllLegalcaseDocumentIndexByFilter(final LegalCaseSearchResult searchRequest)
            throws ParseException {

        final BoolQueryBuilder query = getFilterQuery(searchRequest);
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(LcmsConstants.LEGALCASE_INDEX_NAME)
                .withQuery(query).withPageable(new PageRequest(0, 250)).build();

        final Iterable<LegalCaseDocument> legalcaseDocumentSearchList = legalCaseDocumentRepository.search(searchQuery);
        final List<LegalCaseDocument> legalcaseDocumentList = new ArrayList<>();
        for (final LegalCaseDocument documentObj : legalcaseDocumentSearchList)
            legalcaseDocumentList.add(documentObj);

        return legalcaseDocumentList;
    }

    private BoolQueryBuilder getFilterQuery(final LegalCaseSearchResult searchRequest) throws ParseException {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat newFormat = new SimpleDateFormat(ApplicationConstant.ES_DATE_FORMAT);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("cityName", ApplicationThreadLocals.getCityName()));

        if (StringUtils.isNotBlank(searchRequest.getFromDate()))
            boolQuery = boolQuery.filter(QueryBuilders.rangeQuery("caseDate")
                    .gte(newFormat.format(formatter.parse(searchRequest.getFromDate())))
                    .lte(new DateTime(newFormat.format(formatter.parse(searchRequest.getToDate())))));
        if (StringUtils.isNotBlank(searchRequest.getCaseNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("caseNumber", searchRequest.getCaseNumber()));
        if (StringUtils.isNotBlank(searchRequest.getLcNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("lcNumber", searchRequest.getLcNumber()));
        if (StringUtils.isNotBlank(searchRequest.getCourtName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("courtName", searchRequest.getCourtName()));

        if (StringUtils.isNotBlank(searchRequest.getCaseType()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("caseType", searchRequest.getCaseType()));

        if (StringUtils.isNotBlank(searchRequest.getCourtTypes()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("courtType", searchRequest.getCourtTypes()));

        if (StringUtils.isNotBlank(searchRequest.getStandingCouncil()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("advocateName", searchRequest.getStandingCouncil()));

        if (StringUtils.isNotBlank(searchRequest.getCaseStatus()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("status", searchRequest.getCaseStatus()));

        if (StringUtils.isNotBlank(searchRequest.getPetitionType()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("petitionType", searchRequest.getPetitionType()));

        if (StringUtils.isNotBlank(searchRequest.getReportStatus()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("subStatus", searchRequest.getReportStatus()));

        if (searchRequest.getIsStatusExcluded() != null) {
            final List<String> statusCodeList = new ArrayList<String>();
            statusCodeList.add(LcmsConstants.LEGALCASE_STATUS_JUDGMENT_IMPLIMENTED_DESC);
            statusCodeList.add(LcmsConstants.LEGALCASE_STATUS_CLOSED_DESC);
            boolQuery = boolQuery.mustNot(QueryBuilders.termsQuery("status", statusCodeList));
        }

        return boolQuery;
    }

}
