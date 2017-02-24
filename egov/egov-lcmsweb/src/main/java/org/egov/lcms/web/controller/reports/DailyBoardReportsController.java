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
package org.egov.lcms.web.controller.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.lcms.entity.es.LegalCaseDocument;
import org.egov.lcms.reports.entity.DailyBoardReportResults;
import org.egov.lcms.repository.es.LegalCaseDocumentRepository;
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
@RequestMapping(value = "/reports")
public class DailyBoardReportsController extends GenericLegalCaseController {

    @Autowired
    private LegalCaseDocumentRepository legalCaseDocumentRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/dailyBoardReport")
    public String searchForm(final Model model) {
        model.addAttribute("dailyBoardReportResult", new DailyBoardReportResults());
        model.addAttribute("currentDate", new Date());
        return "dailyboardreport-form";
    }

    @RequestMapping(value = "/dailyBoardReportresults", method = RequestMethod.POST)
    @ResponseBody
    public List<DailyBoardReportResults> getDailyBoardReport(@ModelAttribute final DailyBoardReportResults dailyBoardReport)
            throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        final SimpleDateFormat myFormat = new SimpleDateFormat(LcmsConstants.DATE_FORMAT_DDMMYYYY);
        List<LegalCaseDocument> legalcaseDocumentTempList;
        final List<DailyBoardReportResults> finalResult = new ArrayList<>();
        DailyBoardReportResults dailyBoardReportResultObj;
        legalcaseDocumentTempList = findAllLegalcaseDocumentIndexByFilter(dailyBoardReport);
        for (final LegalCaseDocument legalcaseDocumentIndex : legalcaseDocumentTempList) {
            dailyBoardReportResultObj = new DailyBoardReportResults();
            dailyBoardReportResultObj.setLcNumber(legalcaseDocumentIndex.getLcNumber());
            dailyBoardReportResultObj.setCaseTitle(legalcaseDocumentIndex.getCaseTitle());
            dailyBoardReportResultObj.setCourtName(legalcaseDocumentIndex.getCourtName());
            dailyBoardReportResultObj.setCaseNumber(legalcaseDocumentIndex.getCaseNumber());
            dailyBoardReportResultObj.setPetitionerName(legalcaseDocumentIndex.getPetitionerNames());
            dailyBoardReportResultObj.setRespondantName(legalcaseDocumentIndex.getRespondantNames());
            dailyBoardReportResultObj.setPetitionType(legalcaseDocumentIndex.getPetitionType());
            dailyBoardReportResultObj.setStandingCouncil(legalcaseDocumentIndex.getAdvocateName());
            dailyBoardReportResultObj.setOfficerIncharge(legalcaseDocumentIndex.getOfficerIncharge());
            dailyBoardReportResultObj.setCaseStatus(legalcaseDocumentIndex.getStatus());
            dailyBoardReportResultObj
                    .setNextDate(myFormat.format(dateFormat.parse(legalcaseDocumentIndex.getNextDate().toString())));
            finalResult.add(dailyBoardReportResultObj);
        }
        return finalResult;

    }

    public List<LegalCaseDocument> findAllLegalcaseDocumentIndexByFilter(final DailyBoardReportResults dailyBoardReport)
            throws ParseException {

        final BoolQueryBuilder query = getFilterQuery(dailyBoardReport);
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(LcmsConstants.LEGALCASE_INDEX_NAME)
                .withQuery(query).withPageable(new PageRequest(0, 250)).build();

        final Iterable<LegalCaseDocument> legalcaseDocumentSearchList = legalCaseDocumentRepository.search(searchQuery);
        final List<LegalCaseDocument> legalcaseDocumentList = new ArrayList<>();
        for (final LegalCaseDocument documentObj : legalcaseDocumentSearchList)
            legalcaseDocumentList.add(documentObj);

        return legalcaseDocumentList;
    }

    private BoolQueryBuilder getFilterQuery(final DailyBoardReportResults searchRequest) throws ParseException {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat newFormat = new SimpleDateFormat(ApplicationConstant.ES_DATE_FORMAT);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("cityName", ApplicationThreadLocals.getCityName()));

        if (StringUtils.isNotBlank(searchRequest.getFromDate()))
            boolQuery = boolQuery.filter(QueryBuilders.rangeQuery("caseDate")
                    .gte(newFormat.format(formatter.parse(searchRequest.getFromDate())))
                    .lte(new DateTime(newFormat.format(formatter.parse(searchRequest.getToDate())))));

        if (StringUtils.isNotBlank(searchRequest.getCaseCategory()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("caseType", searchRequest.getCaseCategory()));

        if (StringUtils.isNotBlank(searchRequest.getOfficerIncharge()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery("officerIncharge", searchRequest.getOfficerIncharge().split("@")[0]));

        return boolQuery;
    }

}
