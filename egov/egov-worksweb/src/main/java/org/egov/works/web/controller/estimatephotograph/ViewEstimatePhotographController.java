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
package org.egov.works.web.controller.estimatephotograph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.EstimatePhotographs;
import org.egov.works.abstractestimate.entity.EstimatePhotographs.WorkProgress;
import org.egov.works.abstractestimate.service.EstimatePhotographService;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateDetailService;
import org.egov.works.models.workorder.WorkOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/estimatephotograph")
public class ViewEstimatePhotographController {
	
	@Autowired
	private EstimatePhotographService estimatePhotographService;

	@Autowired
	private EstimateService estimateService;

	@Autowired
	private LetterOfAcceptanceService letterOfAcceptanceService;

	@Autowired
	private LineEstimateDetailService lineEstimateDetailService;
	
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewMilestoneTemplate(@RequestParam final Long lineEstimateDetailsId, final Model model,
            final HttpServletRequest request)
                    throws ApplicationException {
    	
    	final Long ledId = Long.valueOf(request.getParameter("lineEstimateDetailsId"));
    	final List<EstimatePhotographs> photographsBefore = estimatePhotographService
				.getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(
						WorkProgress.BEFORE, ledId);
		final List<EstimatePhotographs> photographsAfter = estimatePhotographService
				.getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(
						WorkProgress.AFTER, ledId);
		final List<EstimatePhotographs> photographsOnProcess = estimatePhotographService
				.getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(
						WorkProgress.DURING, ledId);
		JsonObject photographStages = new JsonObject();
		JsonArray array = new JsonArray();

		for (EstimatePhotographs ep : photographsBefore) {
			JsonObject before = new JsonObject();
			before.addProperty("name", ep.getFileStore().getFileName());
			before.addProperty("type", "image/jpg");
			before.addProperty("file",
					"/egi/downloadfile?fileStoreId=" + ep.getFileStore().getFileStoreId() + "&moduleName=WMS");
			before.addProperty("key", ep.getFileStore().getId());
			array.add(before);
		}

		photographStages.add("before", array);
		array = new JsonArray();

		for (EstimatePhotographs ep : photographsAfter) {
			JsonObject after = new JsonObject();
			after.addProperty("name", ep.getFileStore().getFileName());
			after.addProperty("type", "image/jpg");
			after.addProperty("file",
					"/egi/downloadfile?fileStoreId=" + ep.getFileStore().getFileStoreId() + "&moduleName=WMS");
			after.addProperty("key", ep.getFileStore().getId());
			array.add(after);
		}

		photographStages.add("after", array);
		array = new JsonArray();

		for (EstimatePhotographs ep : photographsOnProcess) {
			JsonObject during = new JsonObject();
			during.addProperty("name", ep.getFileStore().getFileName());
			during.addProperty("type", "image/jpg");
			during.addProperty("file", "/egi/downloadfile?fileStoreId=" + ep.getFileStore().getFileStoreId() + "&moduleName=WMS");
			during.addProperty("key", ep.getFileStore().getId());
			array.add(during);
		}

		photographStages.add("during", array);
		
		final LineEstimateDetails lineEstimateDetails = lineEstimateDetailService.getById(ledId);
		final WorkOrder workOrder = letterOfAcceptanceService
				.getWorkOrderByEstimateNumber(lineEstimateDetails.getEstimateNumber());
		final AbstractEstimate abstractEstimate = estimateService
				.getAbstractEstimateByEstimateNumber(lineEstimateDetails.getEstimateNumber());
		model.addAttribute("abstractEstimate", abstractEstimate);
		model.addAttribute("workOrder", workOrder);
		model.addAttribute("lineEstimateDetails", lineEstimateDetails);
    	
		model.addAttribute("photographStages", photographStages);
        return "estimatePhotographs-view";
    }

}
