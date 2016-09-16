package org.egov.council.web.controller;

import static org.egov.council.utils.constants.CouncilConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.council.utils.constants.CouncilConstants.WARD;
import static org.egov.infra.web.utils.WebUtils.toJSON;

import java.util.List;

import org.egov.council.entity.CouncilPreamble;
import org.egov.council.service.CouncilPreambleService;
import org.egov.council.web.adaptor.CouncilPreambleJsonAdaptor;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/councilreports")
public class CouncilReportsController {

	private final static String COUNCILPREAMBLE_WARDWISE_SEARCH = "preamblewardwise-report";

	@Autowired
	private CouncilPreambleService councilPreambleService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private BoundaryService boundaryService;

	public @ModelAttribute("departments") List<Department> getDepartmentList() {
		return departmentService.getAllDepartments();
	}

	public @ModelAttribute("wards") List<Boundary> getWardsList() {
		return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, REVENUE_HIERARCHY_TYPE);
	}

	@RequestMapping(value = "/preamblewardwise/search", method = RequestMethod.GET)
	public String getSearchView(Model model) {
		model.addAttribute("councilPreamble", new CouncilPreamble());
		return COUNCILPREAMBLE_WARDWISE_SEARCH;
	}

	@RequestMapping(value = "/preamblewardwise/search-result", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String searchPreambleWardwise(Model model,
			@ModelAttribute final CouncilPreamble councilPreamble, final BindingResult errors) {
		if (errors.hasErrors()) {
			return COUNCILPREAMBLE_WARDWISE_SEARCH;
		}
		List<CouncilPreamble> searchResultList = councilPreambleService
				.searchPreambleForWardwiseReport(councilPreamble);
		final String prambleJsonData = new StringBuilder("{\"data\":")
				.append(toJSON(searchResultList, CouncilPreamble.class, CouncilPreambleJsonAdaptor.class)).append("}")
				.toString();
		return prambleJsonData;
	}
}
