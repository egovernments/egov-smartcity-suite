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
package org.egov.lcms.web.controller.reports;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.utils.JsonUtils;
import org.egov.lcms.masters.entity.JudgmentType;
import org.egov.lcms.masters.service.JudgmentTypeService;
import org.egov.lcms.reports.entity.LegalCommonReportResult;
import org.egov.lcms.transactions.entity.ReportStatus;
import org.egov.lcms.transactions.service.GenericSubReportService;
import org.egov.lcms.transactions.service.LegalCommonReportService;
import org.egov.lcms.transactions.service.SearchLegalCaseService;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.egov.lcms.web.adaptor.GenericSubReportJsonAdaptor;
import org.egov.lcms.web.controller.transactions.GenericLegalCaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class GenericSubReportController extends GenericLegalCaseController {

    @Autowired
    private JudgmentTypeService judgmentTypeService;

    @Autowired
    private SearchLegalCaseService searchLegalCaseService;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Autowired
    private GenericSubReportService genericSubReportService;

    @Autowired
    private LegalCommonReportService legalCommonReportService;

    public @ModelAttribute("judgmentTypeList") List<JudgmentType> judgmentTypeList() {
        return judgmentTypeService.getActiveJudgementTypes();
    }

    public @ModelAttribute("statusList") List<EgwStatus> statusList() {
        return legalCaseUtil.getStatusForModule();
    }

    public @ModelAttribute("reportStatusList") List<ReportStatus> getReportStatusList() {
        return searchLegalCaseService.getReportStatus();
    }

    public @ModelAttribute("aggregatedByList") List<String> defaultersList() {
        final List<String> aggregatedByList = new ArrayList<>();
        aggregatedByList.add(LcmsConstants.COURTNAME);
        aggregatedByList.add(LcmsConstants.COURTTYPE);
        aggregatedByList.add(LcmsConstants.PETITIONTYPE);
        aggregatedByList.add(LcmsConstants.CASESTATUS);
        aggregatedByList.add(LcmsConstants.OFFICERINCHRGE);
        aggregatedByList.add(LcmsConstants.JUDGEMENTOUTCOME);
        aggregatedByList.add(LcmsConstants.CASECATEGORY);
        return aggregatedByList;
    }

    @RequestMapping(value = "/genericSubReport", method = RequestMethod.GET)
    public String searchForm(final Model model,
            @ModelAttribute final LegalCommonReportResult legalSubReportResult) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("legalSubReportResult", legalSubReportResult);
        return "genericsub-form";
    }

    @RequestMapping(value = "/genericSubResult", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getGenerivSubReportResult(final HttpServletRequest request,
            final HttpServletResponse response,
            @ModelAttribute final LegalCommonReportResult genericSubReportResult)
            throws ParseException {
        final List<LegalCommonReportResult> genericSubResultList = genericSubReportService
                .getGenericSubReports(genericSubReportResult);
        return new StringBuilder("{ \"data\":").append(
                JsonUtils.toJSON(genericSubResultList, LegalCommonReportResult.class, GenericSubReportJsonAdaptor.class))
                .append("}").toString();

    }

    @RequestMapping(value = "/genericdrilldownreportresults", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<LegalCommonReportResult> getDrillDownReportResult(
            @ModelAttribute final LegalCommonReportResult legalCommonReportObj,
            final HttpServletRequest request) throws ParseException {
        return legalCommonReportService.getLegalCommonReportsResults(legalCommonReportObj, StringUtils.EMPTY);

    }

}
