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

package org.egov.egf.web.controller;

import java.util.List;

import org.egov.commons.dao.FunctionDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.egf.web.adaptor.BudgetUploadReportJsonAdaptor;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetUploadReport;
import org.egov.model.service.BudgetUploadReportService;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/budgetuploadreport")
public class BudgetUploadReportController {
	private final static String BUDGETUPLOADREPORT_SEARCH = "budgetuploadreport-search";
	@Autowired
	private BudgetUploadReportService budgetUploadReportService;
	@Autowired
	@Qualifier("budgetService")
	private BudgetService budgetService;
	@Autowired
	private FundHibernateDAO fundDAO;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private FunctionDAO functionDAO;
	@Autowired
	@Qualifier("budgetDetailService")
	private BudgetDetailService budgetDetailService;

	private void prepareNewForm(Model model) {
		model.addAttribute("budgets", budgetService.getBudgetsForUploadReport());
		model.addAttribute("funds", fundDAO.findAllActiveIsLeafFunds());
		model.addAttribute("departments", departmentService.getAllDepartments());
		model.addAttribute("functions", functionDAO.getAllActiveFunctions());
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(Model model) {
		BudgetUploadReport budgetUploadReport = new BudgetUploadReport();
		prepareNewForm(model);
		model.addAttribute("budgetUploadReport", budgetUploadReport);
		return BUDGETUPLOADREPORT_SEARCH;

	}

	@RequestMapping(value = "/ajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearch(Model model,
			@ModelAttribute final BudgetUploadReport budgetUploadReport) {
		List<BudgetUploadReport> searchResultList = budgetUploadReportService
				.search(budgetUploadReport);
		String result = new StringBuilder("{ \"data\":")
				.append(toSearchResultJson(searchResultList)).append("}")
				.toString();
		return result;
	}

	@RequestMapping(value = "/ajax/getReferenceBudget", method = RequestMethod.GET)
	public @ResponseBody String getReferenceBudget(
			@RequestParam("budgetId") Long budgetId) {
		if (budgetId != null) {
			Budget refBudget = budgetService
					.getReferenceBudgetFor(budgetService.findById(budgetId,
							false));

			return refBudget.getName();
		} else
			return null;
	}

	public Object toSearchResultJson(final Object object) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(
				BudgetUploadReport.class, new BudgetUploadReportJsonAdaptor())
				.create();
		final String json = gson.toJson(object);
		return json;
	}
}