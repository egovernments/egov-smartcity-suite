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

package org.egov.ptis.web.controller.transactions.bulkboundaryupdation;

import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.BLOCK;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.bean.BulkBoundaryRequest;
import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;
import org.egov.ptis.domain.service.bulkboundaryupdatation.BulkBoundaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;

@Controller
@RequestMapping(value = "/bulkboundaryupdation")
public class BulkBoundaryRequestController {

	private static final String BULK_BOUNDARY_FORM = "bulkboundary-form";

	@Autowired
	private BoundaryService boundaryService;

	@Autowired
	private BulkBoundaryService bulkBoundaryUpdationService;

	@ModelAttribute
	public BulkBoundaryRequest bulkBoundaryRequest() {
		return new BulkBoundaryRequest();
	}

	@ModelAttribute
	public PropertyMVInfo propertyMVInfo() {
		return new PropertyMVInfo();
	}

	@ModelAttribute("zonelist")
	public List<Boundary> zones() {
		return boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE, REVENUE_HIERARCHY_TYPE);
	}
	@ModelAttribute("wardlist")
	public List<Boundary> wards() {
		return boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, REVENUE_HIERARCHY_TYPE);
	}
	@ModelAttribute("blocklist")
	public List<Boundary> blocks() {
		return boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(BLOCK, REVENUE_HIERARCHY_TYPE);
	}

	@ModelAttribute("localityList")
	public List<Boundary> localities() {
		return boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY,LOCATION_HIERARCHY_TYPE);
	}

	@ModelAttribute("electionWardList")
	public List<Boundary> electionWards() {
		return boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, ADMIN_HIERARCHY_TYPE);
	}

	@GetMapping("/form")
	public String bulkBoundaryRequestForm(final Model model) {
		model.addAttribute("PropertyMVInfo", propertyMVInfo());
		return BULK_BOUNDARY_FORM;
	}
	
	
	@RequestMapping(value = {"/ajaxBoundary-wardByBlock"}, method = RequestMethod.GET)
    public void blockByWard(@RequestParam Long blockId , HttpServletResponse response) throws IOException {
		Boundary block=boundaryService.getBoundaryById(blockId);
		final List<JsonObject> jsonObjects = new ArrayList<>();
		final Boundary ward =block.getParent();
			final JsonObject jsonObj = new JsonObject();
			jsonObj.addProperty("wardId", ward.getId());
			jsonObj.addProperty("wardName", ward.getName());
			jsonObjects.add(jsonObj);
		IOUtils.write(jsonObjects.toString(), response.getWriter());
    }

	@PostMapping(value = "/search", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String searchForm(@ModelAttribute final BulkBoundaryRequest bulkBoundaryRequest, final Model model) {
		return new DataTable<>(bulkBoundaryUpdationService.pagedBulkBoundaryRecords(bulkBoundaryRequest),
				bulkBoundaryRequest.draw()).toJson(BulkBoundaryResultAdaptor.class);
	}

}
