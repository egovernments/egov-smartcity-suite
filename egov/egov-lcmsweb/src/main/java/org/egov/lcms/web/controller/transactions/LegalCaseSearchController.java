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
package org.egov.lcms.web.controller.transactions;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.EgwStatus;
import org.egov.infra.utils.JsonUtils;
import org.egov.lcms.reports.entity.LegalCaseSearchResult;
import org.egov.lcms.transactions.entity.ReportStatus;
import org.egov.lcms.transactions.service.SearchLegalCaseService;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.web.adaptor.LegalCaseSearchJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/search")
public class LegalCaseSearchController extends GenericLegalCaseController {

    @Autowired
    private SearchLegalCaseService searchLegalCaseService;

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

    @RequestMapping(method = RequestMethod.GET, value = "/searchForm")
    public String searchForm(final Model model) {
        model.addAttribute("currDate", new Date());
        return "searchlegalcase-form";
    }

    @RequestMapping(value = "/legalsearchResult", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String getLegalCaseSearchResult(final Model model,
            @ModelAttribute final LegalCaseSearchResult legalCaseSearchResult, final HttpServletRequest request) {
        final List<LegalCaseSearchResult> legalcaseSearchList = searchLegalCaseService
                .getLegalCaseReport(legalCaseSearchResult);
        final String result = new StringBuilder("{ \"data\":").append(
                JsonUtils.toJSON(legalcaseSearchList, LegalCaseSearchResult.class, LegalCaseSearchJsonAdaptor.class))
                .append("}").toString();
        return result;
    }
}
