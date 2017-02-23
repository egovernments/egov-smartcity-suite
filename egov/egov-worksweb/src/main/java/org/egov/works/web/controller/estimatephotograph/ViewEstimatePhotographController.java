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
package org.egov.works.web.controller.estimatephotograph;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.infra.exception.ApplicationException;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.EstimatePhotographs;
import org.egov.works.abstractestimate.entity.EstimatePhotographs.WorkProgress;
import org.egov.works.abstractestimate.service.EstimatePhotographService;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateDetailService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonObject;

@Controller
@RequestMapping(value = "/estimatephotograph")
public class ViewEstimatePhotographController {

    @Autowired
    private EstimatePhotographService estimatePhotographService;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private LineEstimateDetailService lineEstimateDetailService;

    @Autowired
    private FileStoreUtils fileStoreUtils;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewEstimatePhotographs(
            @RequestParam(value = WorksConstants.MODE, required = false) final String mode, final Model model,
            final HttpServletRequest request) throws ApplicationException {
        if (worksApplicationProperties.lineEstimateRequired())
            viewEstimatePhotographsFromLE(model, request);
        else
            viewEstimatePhotographsFromAE(model, request);
        return "estimatePhotographs-view";
    }

    private void viewEstimatePhotographsFromAE(final Model model, final HttpServletRequest request) {
        final Long aeId = Long.valueOf(request.getParameter("abstractEstimateId"));
        final List<EstimatePhotographs> photographsBefore = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndAbstractEstimate(WorkProgress.BEFORE, aeId);
        final List<EstimatePhotographs> photographsAfter = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndAbstractEstimate(WorkProgress.AFTER, aeId);
        final List<EstimatePhotographs> photographsOnProcess = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndAbstractEstimate(WorkProgress.DURING, aeId);
        final JsonObject photographStages = new JsonObject();
        estimatePhotographService.setPhotographsBeforeJsonArray(photographsBefore, photographStages);
        estimatePhotographService.setPhotographsAfterJsonArray(photographsAfter, photographStages);
        estimatePhotographService.setPhotographsDuringJsonArray(photographsOnProcess, photographStages);

        final AbstractEstimate abstractEstimate = estimateService.getAbstractEstimateById(aeId);
        final WorkOrderEstimate woe = workOrderEstimateService
                .getWorkOrderEstimateByEstimateNumber(abstractEstimate.getEstimateNumber());
        estimatePhotographService.setModelValues(model, request, photographStages, woe, abstractEstimate);

    }

    private void viewEstimatePhotographsFromLE(final Model model, final HttpServletRequest request) {
        final Long ledId = Long.valueOf(request.getParameter("lineEstimateDetailsId"));
        final List<EstimatePhotographs> photographsBefore = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(WorkProgress.BEFORE, ledId);
        final List<EstimatePhotographs> photographsAfter = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(WorkProgress.AFTER, ledId);
        final List<EstimatePhotographs> photographsOnProcess = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(WorkProgress.DURING, ledId);
        final JsonObject photographStages = new JsonObject();
        estimatePhotographService.setPhotographsBeforeJsonArray(photographsBefore, photographStages);
        estimatePhotographService.setPhotographsAfterJsonArray(photographsAfter, photographStages);
        estimatePhotographService.setPhotographsDuringJsonArray(photographsOnProcess, photographStages);

        final LineEstimateDetails lineEstimateDetails = lineEstimateDetailService.getById(ledId);
        final WorkOrderEstimate woe = workOrderEstimateService
                .getWorkOrderEstimateByEstimateNumber(lineEstimateDetails.getEstimateNumber());
        final AbstractEstimate abstractEstimate = estimateService
                .getAbstractEstimateByEstimateNumber(lineEstimateDetails.getEstimateNumber());
        model.addAttribute("lineEstimateDetails", lineEstimateDetails);
        estimatePhotographService.setModelValues(model, request, photographStages, woe, abstractEstimate);
    }

    @RequestMapping(value = "/downloadphotgraphs", method = RequestMethod.GET)
    public void viewUploadedDocuments(@RequestParam final String fileStoreId, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        fileStoreUtils.fetchFileAndWriteToStream(fileStoreId, WorksConstants.FILESTORE_MODULECODE, false, response);

    }

}
