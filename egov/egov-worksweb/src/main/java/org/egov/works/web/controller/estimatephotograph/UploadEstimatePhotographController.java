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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/estimatephotograph")
public class UploadEstimatePhotographController {

	@Autowired
	private LineEstimateDetailService lineEstimateDetailService;

	@Autowired
	private EstimatePhotographService estimatePhotographService;

	@Autowired
	private EstimateService estimateService;

	@Autowired
	private LetterOfAcceptanceService letterOfAcceptanceService;

	@RequestMapping(value = "/newform", method = RequestMethod.GET)
	public String showUploadEstimatePhotographForm(
			@ModelAttribute("estimatePhotographs") final EstimatePhotographs estimatePhotographs, final Model model,
			final HttpServletRequest request) throws ApplicationException {
		final Long lineEstimateDetailsId = Long.valueOf(request.getParameter("lineEstimateDetailsId"));

		final List<EstimatePhotographs> photographsBefore = estimatePhotographService
				.getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(
						WorkProgress.BEFORE, lineEstimateDetailsId);
		final List<EstimatePhotographs> photographsAfter = estimatePhotographService
				.getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(
						WorkProgress.AFTER, lineEstimateDetailsId);
		final List<EstimatePhotographs> photographsOnProcess = estimatePhotographService
				.getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(
						WorkProgress.DURING, lineEstimateDetailsId);
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

		final LineEstimateDetails lineEstimateDetails = lineEstimateDetailService.getById(lineEstimateDetailsId);
		final WorkOrder workOrder = letterOfAcceptanceService
				.getWorkOrderByEstimateNumber(lineEstimateDetails.getEstimateNumber());
		final AbstractEstimate abstractEstimate = estimateService
				.getAbstractEstimateByEstimateNumber(lineEstimateDetails.getEstimateNumber());
		model.addAttribute("estimatePhotographs", estimatePhotographs);
		model.addAttribute("abstractEstimate", abstractEstimate);
		model.addAttribute("workOrder", workOrder);
		model.addAttribute("photographStages", photographStages);
		model.addAttribute("lineEstimateDetails", lineEstimateDetails);
		model.addAttribute("estimatePhotographTrackStage", WorkProgress.values());
		return "estimatePhotographs-form";
	}

	@RequestMapping(value = "/upload/{id}/{estimatephotographstage}", method = RequestMethod.POST)
	public @ResponseBody String uploadEstimatePhotographForm(
			@ModelAttribute("estimatePhotographs") final EstimatePhotographs estimatePhotographs,
			@RequestParam("file[]") final MultipartFile[] files, final HttpServletRequest request,
			@PathVariable final Long id, @PathVariable final String estimatephotographstage) throws IOException {
		
		final LineEstimateDetails lineEstimateDetails = lineEstimateDetailService.getById(id);
		
		final List<EstimatePhotographs> estimatePhotographsList = estimatePhotographService
				.getEstimatePhotographs(files, lineEstimateDetails);
		JsonArray array = new JsonArray();
		//TODO : remove this comment when preview required in create/modify screen
		/*
		 * JsonObject photographStages = new JsonObject();
		 * final List<EstimatePhotographs> photographsBefore = estimatePhotographService
				.getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(
						WorkProgress.BEFORE, id);
		final List<EstimatePhotographs> photographsAfter = estimatePhotographService
				.getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(
						WorkProgress.AFTER, id);
		final List<EstimatePhotographs> photographsDuring = estimatePhotographService
				.getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(
						WorkProgress.DURING, id);
		JsonObject photographStages = new JsonObject();
		JsonArray array = new JsonArray();
		
		if (estimatephotographstage.toUpperCase().equals(WorkProgress.AFTER.toString()))
			getEstimatePhotographByWorkProgress(photographsAfter, photographStages, array);
		else if (estimatephotographstage.toUpperCase().equals(WorkProgress.BEFORE.toString()))
			getEstimatePhotographByWorkProgress(photographsBefore, photographStages, array);
		else
			getEstimatePhotographByWorkProgress(photographsDuring, photographStages, array);
			
			add this before returning
			photographStages.add("response", array);*/

		
				
		for (final EstimatePhotographs ep : estimatePhotographsList) {
			final JsonObject child = new JsonObject();
			ep.setDateOfCapture(new Date());
			ep.setLineEstimateDetails(lineEstimateDetails);
			ep.setLatitude(ep.getLatitude());
			ep.setLongitude(ep.getLongitude());
			if (WorkProgress.BEFORE.toString().equalsIgnoreCase(estimatephotographstage))
				ep.setWorkProgress(WorkProgress.BEFORE);
			else if (WorkProgress.AFTER.toString().equalsIgnoreCase(estimatephotographstage))
				ep.setWorkProgress(WorkProgress.AFTER);
			else
				ep.setWorkProgress(WorkProgress.DURING);
			estimatePhotographService.save(ep);
			child.addProperty("key", ep.getFileStore().getId());
			//TODO : remove this comment when preview required in create/modify screen
			//child.addProperty("name", ep.getFileStore().getFileName());
			//child.addProperty("type", "image/jpg");
			//child.addProperty("file", "/egi/downloadfile?fileStoreId=" + ep.getFileStore().getFileStoreId() + "&moduleName=WMS");
			array.add(child);
		}
		
		return array.toString();
	}

//TODO : remove this comment when preview required in create/modify screen 
//	private void getEstimatePhotographByWorkProgress(final List<EstimatePhotographs> photographsBefore, JsonObject photographStages,
//			JsonArray array) {
//		for (EstimatePhotographs ep : photographsBefore) {
//			JsonObject before = new JsonObject();
//			before.addProperty("name", ep.getFileStore().getFileName());
//			before.addProperty("type", "image/jpg");
//			before.addProperty("file",
//					"/egi/downloadfile?fileStoreId=" + ep.getFileStore().getFileStoreId() + "&moduleName=WMS");
//			before.addProperty("key", ep.getFileStore().getId());
//			array.add(before);
//		}
//
//		photographStages.add("response", array);
//	}

	@RequestMapping(value = "/update/{id}/{estimatephotographstage}/{fileid}", method = RequestMethod.POST)
	public @ResponseBody String updateEstimatePhotographForm(
			@ModelAttribute("estimatePhotographs") final EstimatePhotographs estimatePhotographs,
			final HttpServletRequest request, @PathVariable final Long id,
			@PathVariable final String estimatephotographstage, @PathVariable final Long fileid) throws IOException {
		EstimatePhotographs photographs = estimatePhotographService.getEstimatePhotographByFilestore(fileid);
		estimatePhotographService.delete(photographs);
		return "";
	}

}
