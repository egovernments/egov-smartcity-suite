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

import org.apache.commons.io.IOUtils;
import org.egov.lcms.transactions.entity.LegalCaseReportResult;
import org.egov.lcms.transactions.entity.LegalCaseReportResultAdaptor;
import org.egov.lcms.transactions.service.SearchLegalCaseService;
import org.hibernate.SQLQuery;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.egov.infra.web.utils.WebUtils.toJSON;

@Controller
@RequestMapping(value = "/search")
public class LegalCaseSearchController extends GenericLegalCaseController {

	@Autowired
	private SearchLegalCaseService searchLegalCaseService;

	@ModelAttribute
	private void getLegalCaseReport(final Model model) {
		final LegalCaseReportResult legalCaseReportResult = new LegalCaseReportResult();
		model.addAttribute("legalCaseReportResult", legalCaseReportResult);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/searchForm")
	public String saechForm(final Model model) {
		model.addAttribute("currDate", new Date());
		return "search-legalCaseForm";
	}

	@ExceptionHandler(Exception.class)
	@RequestMapping(value = "/legalsearchResult", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody void getLegalCaseSearchResult(@RequestParam final String caseNumber,
			@RequestParam final String lcNumber, @RequestParam final String court, @RequestParam final String caseType,
			@RequestParam final String standingCouncil, @RequestParam final String courtType,
			@RequestParam final String isStatusExcluded, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		Boolean caseExcluded = Boolean.FALSE;
		if (request.getParameter("isStatusExcluded").equals("true"))
			caseExcluded = Boolean.TRUE;
		List<LegalCaseReportResult> legalcaseSearchList = new ArrayList<LegalCaseReportResult>();
		final SQLQuery query = searchLegalCaseService.getLegalCaseReport(request.getParameter("caseNumber"),
				request.getParameter("lcNumber"), request.getParameter("court"), request.getParameter("caseType"),
				request.getParameter("standingCouncil"), request.getParameter("courtType"), null, caseExcluded);
		legalcaseSearchList = query.list();
		String result = null;
		result = new StringBuilder("{ \"data\":").append(toJSON(legalcaseSearchList, LegalCaseReportResult.class, LegalCaseReportResultAdaptor.class)).append("}").toString();
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		IOUtils.write(result, response.getWriter());
	}
}
