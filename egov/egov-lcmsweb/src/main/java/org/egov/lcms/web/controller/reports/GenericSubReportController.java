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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.commons.EgwStatus;
import org.egov.infra.web.utils.WebUtils;
import org.egov.lcms.masters.entity.JudgmentType;
import org.egov.lcms.masters.service.JudgmentTypeService;
import org.egov.lcms.reports.entity.GenericSubReportResult;
import org.egov.lcms.transactions.service.GenericSubReportService;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.egov.lcms.web.adaptor.GenericSubReportAdaptor;
import org.egov.lcms.web.controller.transactions.GenericLegalCaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reports")
public class GenericSubReportController extends GenericLegalCaseController {

    @Autowired
    private GenericSubReportService genericSubReportService;

    @Autowired
    private JudgmentTypeService judgmentTypeService;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @ModelAttribute
    public void getGenericSubReport(final Model model) {
        final GenericSubReportResult genericSubReportResult = new GenericSubReportResult();
        model.addAttribute("genericSubReportResult", genericSubReportResult);
    }

    public @ModelAttribute("judgmentTypeList") List<JudgmentType> judgmentTypeList() {
        return judgmentTypeService.getActiveJudgementTypes();
    }

    public @ModelAttribute("statusList") List<EgwStatus> statusList() {
        return legalCaseUtil.getStatusForModule();
    }

    public @ModelAttribute("aggregatedByList") List<String> defaultersList() {
        final List<String> aggregatedByList = new ArrayList<String>();
        aggregatedByList.add(LcmsConstants.COURTNAME);
        aggregatedByList.add(LcmsConstants.COURTTYPE);
        aggregatedByList.add(LcmsConstants.PETITIONTYPE);
        aggregatedByList.add(LcmsConstants.CASESTATUS);
        aggregatedByList.add(LcmsConstants.OFFICERINCHRGE);
        aggregatedByList.add(LcmsConstants.JUDGEMENTOUTCOME);
        return aggregatedByList;
    }

    @RequestMapping(value = "/genericSubReport", method = RequestMethod.GET)
    public String searchForm(final Model model) {
        model.addAttribute("currDate", new Date());
        return "genericsub-form";
    }

    @ExceptionHandler(Exception.class)
    @RequestMapping(value = "/genericSubResult", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String getGenerivSubReportResult(@RequestParam final String aggregatedBy,
            @RequestParam final Integer caseCategory, @RequestParam final String standingCounsel,
            @RequestParam final String courtType, @RequestParam final Integer courtName,
            @RequestParam final String judgmentType, @RequestParam final Integer petitionType,
            @RequestParam final Integer caseStatus, @RequestParam final String officerIncharge,
            @RequestParam final Date fromDate, @RequestParam final Date toDate, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException, ParseException {

        final GenericSubReportResult genericSubReportResult = new GenericSubReportResult();
        genericSubReportResult.setAggregatedBy(aggregatedBy);
        genericSubReportResult.setCaseCategory(caseCategory);
        genericSubReportResult.setStandingCounsel(standingCounsel);
        genericSubReportResult.setCaseStatus(caseStatus);
        genericSubReportResult.setCourtType(courtType);
        genericSubReportResult.setCourtName(courtName);
        genericSubReportResult.setJudgmentType(judgmentType);
        genericSubReportResult.setPetitionType(petitionType);
        genericSubReportResult.setOfficerIncharge(officerIncharge);
        genericSubReportResult.setFromDate(fromDate);
        genericSubReportResult.setToDate(toDate);
        List<GenericSubReportResult> genericSubResultList = new ArrayList<GenericSubReportResult>();
        genericSubResultList = genericSubReportService.getGenericSubReport(genericSubReportResult);

        final String result = new StringBuilder("{ \"data\":").append(
                WebUtils.toJSON(genericSubResultList, GenericSubReportResult.class, GenericSubReportAdaptor.class))
                .append("}").toString();
        return result;

    }

}
