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
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.exception.ApplicationException;
import org.egov.infra.utils.StringUtils;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
    private WorksApplicationProperties worksApplicationProperties;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @RequestMapping(value = "/newform", method = RequestMethod.GET)
    public String showUploadEstimatePhotographForm(
            @ModelAttribute(EstimatePhotographService.ESTIMATEPHOTOGRAPHS) final EstimatePhotographs estimatePhotographs,
            final Model model, final HttpServletRequest request,
            @RequestParam(value = WorksConstants.MODE, required = false) final String mode)
            throws ApplicationException {
        if (worksApplicationProperties.lineEstimateRequired())
            setNewFormValuesFromLineEstimateDetails(estimatePhotographs, model, request);
        else
            setNewFormValuesFromAbstractEstimate(estimatePhotographs, model, request);
        return "estimatePhotographs-form";
    }

    private void setNewFormValuesFromAbstractEstimate(final EstimatePhotographs estimatePhotographs, final Model model,
            final HttpServletRequest request) {
        final Long abstractEstimateId = Long.valueOf(request.getParameter("abstractEstimateId"));
        final List<EstimatePhotographs> photographsBefore = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndAbstractEstimate(WorkProgress.BEFORE,
                        abstractEstimateId);
        final List<EstimatePhotographs> photographsAfter = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndAbstractEstimate(WorkProgress.AFTER,
                        abstractEstimateId);
        final List<EstimatePhotographs> photographsOnProcess = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndAbstractEstimate(WorkProgress.DURING,
                        abstractEstimateId);

        final JsonObject photographStages = new JsonObject();

        estimatePhotographService.setPhotographsBeforeJsonArray(photographsBefore, photographStages);
        estimatePhotographService.setPhotographsAfterJsonArray(photographsAfter, photographStages);
        estimatePhotographService.setPhotographsDuringJsonArray(photographsOnProcess, photographStages);

        final AbstractEstimate abstractEstimate = estimateService.getAbstractEstimateById(abstractEstimateId);
        final WorkOrderEstimate woe = workOrderEstimateService
                .getWorkOrderEstimateByEstimateNumber(abstractEstimate.getEstimateNumber());
        model.addAttribute(EstimatePhotographService.ESTIMATEPHOTOGRAPHS, estimatePhotographs);
        estimatePhotographService.setModelValues(model, request, photographStages, woe, abstractEstimate);

    }

    private void setNewFormValuesFromLineEstimateDetails(final EstimatePhotographs estimatePhotographs,
            final Model model, final HttpServletRequest request) {
        final Long lineEstimateDetailsId = Long.valueOf(request.getParameter("lineEstimateDetailsId"));

        final List<EstimatePhotographs> photographsBefore = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(WorkProgress.BEFORE,
                        lineEstimateDetailsId);
        final List<EstimatePhotographs> photographsAfter = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(WorkProgress.AFTER,
                        lineEstimateDetailsId);
        final List<EstimatePhotographs> photographsOnProcess = estimatePhotographService
                .getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(WorkProgress.DURING,
                        lineEstimateDetailsId);
        final JsonObject photographStages = new JsonObject();

        estimatePhotographService.setPhotographsBeforeJsonArray(photographsBefore, photographStages);
        estimatePhotographService.setPhotographsAfterJsonArray(photographsAfter, photographStages);
        estimatePhotographService.setPhotographsDuringJsonArray(photographsOnProcess, photographStages);

        final LineEstimateDetails lineEstimateDetails = lineEstimateDetailService.getById(lineEstimateDetailsId);
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateByEstimateNumber(lineEstimateDetails.getEstimateNumber());
        final AbstractEstimate abstractEstimate = estimateService
                .getAbstractEstimateByEstimateNumber(lineEstimateDetails.getEstimateNumber());
        model.addAttribute("lineEstimateDetails", lineEstimateDetails);
        model.addAttribute(EstimatePhotographService.ESTIMATEPHOTOGRAPHS, estimatePhotographs);
        estimatePhotographService.setModelValues(model, request, photographStages, workOrderEstimate, abstractEstimate);
    }

    @RequestMapping(value = "/upload/{id}/{estimatephotographstage}", method = RequestMethod.POST)
    public @ResponseBody String uploadEstimatePhotographForm(
            @ModelAttribute("estimatePhotographs") final EstimatePhotographs estimatePhotographs,
            @RequestParam("file[]") final MultipartFile[] files, final HttpServletRequest request,
            @PathVariable final Long id, @PathVariable final String estimatephotographstage) throws IOException {
        if (worksApplicationProperties.lineEstimateRequired())
            return uploadEstimatePhotographsFromLE(files, id, estimatephotographstage);
        else
            return uploadEstimatePhotographsFromAE(files, id, estimatephotographstage);
    }

    private String uploadEstimatePhotographsFromAE(final MultipartFile[] files, final Long id,
            final String estimatephotographstage) throws IOException {
        final AbstractEstimate abstractEstimate = estimateService.getAbstractEstimateById(id);

        final List<EstimatePhotographs> estimatePhotographsList = estimatePhotographService
                .getEstimatePhotographs(files, abstractEstimate);
        final JsonArray array = new JsonArray();

        for (final EstimatePhotographs ep : estimatePhotographsList) {
            ep.setAbstractestimate(abstractEstimate);
            saveEstimatePhotograph(estimatephotographstage, array, ep);
        }

        return array.toString();
    }

    private String uploadEstimatePhotographsFromLE(final MultipartFile[] files, final Long id,
            final String estimatephotographstage) throws IOException {
        final LineEstimateDetails lineEstimateDetails = lineEstimateDetailService.getById(id);

        final List<EstimatePhotographs> estimatePhotographsList = estimatePhotographService
                .getEstimatePhotographs(files, lineEstimateDetails);
        final JsonArray array = new JsonArray();

        for (final EstimatePhotographs ep : estimatePhotographsList) {
            ep.setLineEstimateDetails(lineEstimateDetails);
            saveEstimatePhotograph(estimatephotographstage, array, ep);
        }

        return array.toString();
    }

    @RequestMapping(value = "/update/{id}/{estimatephotographstage}/{fileid}", method = RequestMethod.POST)
    public @ResponseBody String updateEstimatePhotographForm(
            @ModelAttribute(EstimatePhotographService.ESTIMATEPHOTOGRAPHS) final EstimatePhotographs estimatePhotographs,
            final HttpServletRequest request, @PathVariable final Long id,
            @PathVariable final String estimatephotographstage, @PathVariable final Long fileid) throws IOException {
        final EstimatePhotographs photographs = estimatePhotographService.getEstimatePhotographByFilestore(fileid);
        estimatePhotographService.delete(photographs);
        return StringUtils.EMPTY;
    }
    
    private void saveEstimatePhotograph(final String estimatephotographstage, final JsonArray array,
            final EstimatePhotographs ep) {
        final JsonObject child = new JsonObject();
        ep.setDateOfCapture(new Date());
        ep.setLatitude(ep.getLatitude());
        ep.setLongitude(ep.getLongitude());
        if (WorkProgress.BEFORE.toString().equalsIgnoreCase(estimatephotographstage))
            ep.setWorkProgress(WorkProgress.BEFORE);
        else if (WorkProgress.AFTER.toString().equalsIgnoreCase(estimatephotographstage))
            ep.setWorkProgress(WorkProgress.AFTER);
        else
            ep.setWorkProgress(WorkProgress.DURING);
        estimatePhotographService.save(ep);
        child.addProperty(EstimatePhotographService.KEY, ep.getFileStore().getId());
        array.add(child);
    }

}
