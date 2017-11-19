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
import static org.egov.ptis.constants.PropertyTaxConstants.BLOCK;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

@Component
public class BulkBoundaryResultAdaptor implements DataTableJsonAdapter<PropertyMVInfo> {

	private static BoundaryService boundaryService;
	private static BoundaryTypeService boundaryTypeService;
	private static CrossHierarchyService crossHierarchyService;

	public BulkBoundaryResultAdaptor() {
	}

	@Autowired
	public BulkBoundaryResultAdaptor(final BoundaryService boundaryService,
			final BoundaryTypeService boundaryTypeService, final CrossHierarchyService crossHierarchyService) {
		BulkBoundaryResultAdaptor.boundaryService = boundaryService;
		BulkBoundaryResultAdaptor.boundaryTypeService = boundaryTypeService;
		BulkBoundaryResultAdaptor.crossHierarchyService = crossHierarchyService;
	}

	@Override
	public JsonElement serialize(DataTable<PropertyMVInfo> bulkBoundaryResponse, Type type,
			JsonSerializationContext jsc) {
		final List<PropertyMVInfo> bulkBoundaryResult = bulkBoundaryResponse.getData();
		final JsonArray bulkBoundaryResultData = new JsonArray();
		bulkBoundaryResult.forEach(bulkBoundaryResultObj -> {
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", bulkBoundaryResultObj.getPropertyId());
			jsonObject.addProperty("assessmentNumber", bulkBoundaryResultObj.getPropertyId());
			jsonObject.addProperty("doorNo", bulkBoundaryResultObj.getHouseNo());
			jsonObject.addProperty("ownerName", bulkBoundaryResultObj.getOwnerName());
			jsonObject.addProperty("zone",
					bulkBoundaryResultObj.getZone() != null ? bulkBoundaryResultObj.getZone().getName() : "NA");
			jsonObject.addProperty("selectedlocality", bulkBoundaryResultObj.getLocality().getId());
			jsonObject.addProperty("selectedblock", bulkBoundaryResultObj.getBlock().getId());
			jsonObject.addProperty("selectedrevward", bulkBoundaryResultObj.getWard().getId());
			jsonObject.addProperty("selectedWard", bulkBoundaryResultObj.getElectionWard().getId());
			jsonObject.add("electionWards", getBoundaryList(WARD, ADMIN_HIERARCHY_TYPE));
			jsonObject.add("localities", getBoundaryList(LOCALITY, LOCATION_HIERARCHY_TYPE));
			jsonObject.add("blocks", getBlockByLocality(bulkBoundaryResultObj.getLocality().getId()));
			jsonObject.add("revenueWards", getBlockByLocality(bulkBoundaryResultObj.getLocality().getId()));

			bulkBoundaryResultData.add(jsonObject);
		});
		return enhance(bulkBoundaryResultData, bulkBoundaryResponse);
	}

	public JsonArray getBoundaryList(String boundaryTypeName, String hierarchyTypeName) {
		List<Boundary> boundaryList = boundaryService
				.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(boundaryTypeName, hierarchyTypeName);
		JsonArray boundaryArray = new JsonArray();
		for (Boundary boundary : boundaryList) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("bndryId", boundary.getId());
			jsonObject.addProperty("bndryName", boundary.getName());
			boundaryArray.add(jsonObject);
		}
		return boundaryArray;
	}

	public JsonArray getBlockByLocality(Long locality) {
		BoundaryType blockType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(BLOCK,
				REVENUE_HIERARCHY_TYPE);
		final List<Boundary> blockList = crossHierarchyService
				.getParentBoundaryByChildBoundaryAndParentBoundaryType(locality, blockType.getId());
		JsonArray boundaryArray = new JsonArray();
		final List<Long> boundaries = new ArrayList<>();
		for (final Boundary block : blockList) {
			final Boundary ward = block.getParent();
			final JsonObject jsonObject = new JsonObject();
			if (!boundaries.contains(ward.getId())) {
				jsonObject.addProperty("wardId", ward.getId());
				jsonObject.addProperty("wardName", ward.getName());
			}
			jsonObject.addProperty("blockId", block.getId());
			jsonObject.addProperty("blockName", block.getName());
			boundaryArray.add(jsonObject);
			boundaries.add(ward.getId());
		}
		return boundaryArray;
	}
}
