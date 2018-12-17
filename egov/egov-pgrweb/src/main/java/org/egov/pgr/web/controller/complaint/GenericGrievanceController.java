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

package org.egov.pgr.web.controller.complaint;

import org.egov.infra.admin.master.entity.CrossHierarchy;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.entity.ReceivingCenter;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.GrievanceConfigurationService;
import org.egov.pgr.service.ReceivingCenterService;
import org.egov.pgr.service.ReceivingModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.ValidationException;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.utils.ApplicationConstant.ADMIN_HIERARCHY_TYPE;
import static org.egov.pgr.utils.constants.PGRConstants.MODULE_NAME;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
public class GenericGrievanceController {
    private static final String LOCATION = "location";

    @Autowired
    protected ComplaintTypeService complaintTypeService;

    @Autowired
    protected ComplaintService complaintService;

    @Autowired
    protected CrossHierarchyService crossHierarchyService;

    @Autowired
    protected ReceivingCenterService receivingCenterService;

    @Autowired
    protected ReceivingModeService receivingModeService;

    @Autowired
    protected ComplaintTypeCategoryService complaintTypeCategoryService;

    @Autowired
    protected FileStoreUtils fileStoreUtils;

    @Autowired
    protected GrievanceConfigurationService grievanceConfigurationService;

    @ModelAttribute("useAutoCompleteComplaintType")
    public boolean useAutoCompleteComplaintType() {
        return grievanceConfigurationService.useAutoCompleteForComplaintType();
    }

    @ModelAttribute("categories")
    public List<ComplaintTypeCategory> complaintTypeCategories() {
        return complaintTypeCategoryService.findAll();
    }

    @ModelAttribute("complaintTypes")
    public List<ComplaintType> frequentlyFiledComplaintTypes() {
        return complaintTypeService.getFrequentlyFiledComplaints();
    }

    @GetMapping("/complaint/reg-success/{crn}")
    public ModelAndView successView(@ModelAttribute Complaint complaint, @PathVariable String crn) {
        Complaint registeredComplaint = complaint;
        if (isNotBlank(crn) && complaint.isNew())
            registeredComplaint = complaintService.getComplaintByCRN(crn);
        return new ModelAndView("complaint/reg-success", "complaint", registeredComplaint);

    }

    @GetMapping(value = "/grievance/attachment/{fileStoreId}", produces = "*/*")
    @ResponseBody
    public ResponseEntity<InputStreamResource> download(@PathVariable String fileStoreId) {
        return fileStoreUtils.fileAsResponseEntity(fileStoreId, MODULE_NAME, false);
    }

    @GetMapping("/complaint/crn-required")
    @ResponseBody
    public boolean checkCRNRequired(@RequestParam Long receivingCenterId) {
        ReceivingCenter receivingCenter = receivingCenterService.findByRCenterId(receivingCenterId);
        return receivingCenter == null || receivingCenter.isCrnRequired();
    }

    @GetMapping(value = "/complaint/locations", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getAllLocationJSON(@RequestParam String locationName) {
        StringBuilder locationJSONData = new StringBuilder(100);
        locationJSONData.append("[");
        crossHierarchyService
                .getChildBoundaryNameAndBndryTypeAndHierarchyType("Locality", "Location", ADMIN_HIERARCHY_TYPE, "%" + locationName + "%")
                .stream()
                .filter(ch -> ch.getParent().isActive() && ch.getChild().isActive())
                .forEach(location ->
                        locationJSONData.append("{\"name\":\"")
                                .append(location.getChild().getName()).append(" - ").append(location.getParent().getName())
                                .append("\",\"id\":").append(location.getId()).append("},")
                );

        if (locationJSONData.lastIndexOf(",") != -1)
            locationJSONData.deleteCharAt(locationJSONData.lastIndexOf(","));

        locationJSONData.append("]");
        return locationJSONData.toString();
    }

    protected void setReceivingMode(Complaint complaint, String receivingModeCode) {
        complaint.setReceivingMode(receivingModeService.getReceivingModeByCode(receivingModeCode));
    }

    protected String validateAndRegister(Complaint complaint, RedirectAttributes redirectAttributes,
                                         MultipartFile[] files, Model model, BindingResult resultBinder, String registrationPage) {
        if (complaint.getCrossHierarchyId() != null) {
            CrossHierarchy crosshierarchy = crossHierarchyService.findById(complaint.getCrossHierarchyId());
            complaint.setLocation(crosshierarchy.getParent());
            complaint.setChildLocation(crosshierarchy.getChild());
        }
        if (complaint.getLocation() == null && (complaint.getLat() == 0 || complaint.getLng() == 0))
            resultBinder.rejectValue(LOCATION, "location.required");

        if (resultBinder.hasErrors()) {
            if (complaint.getCrossHierarchyId() != null)
                model.addAttribute("crossHierarchyLocation",
                        complaint.getChildLocation().getName() + " - " + complaint.getLocation().getName());
            return registrationPage;
        }

        try {
            complaint.setSupportDocs(fileStoreUtils.addToFileStoreWithImageCompression(MODULE_NAME, files));
            complaintService.createComplaint(complaint);
        } catch (ValidationException e) {
            resultBinder.rejectValue(LOCATION, e.getMessage());
            return registrationPage;
        }
        redirectAttributes.addFlashAttribute("complaint", complaint);
        return "redirect:/complaint/reg-success/" + complaint.getCrn();
    }
}