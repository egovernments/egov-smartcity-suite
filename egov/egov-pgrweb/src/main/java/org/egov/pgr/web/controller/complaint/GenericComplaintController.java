/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.web.controller.complaint;

import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.ConfigurationService;
import org.egov.pgr.service.ReceivingCenterService;
import org.egov.pgr.service.ReceivingModeService;
import org.egov.pgr.utils.constants.PGRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Controller
public class GenericComplaintController {

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
    protected ConfigurationService configurationService;

    @ModelAttribute("useAutoCompleteComplaintType")
    public boolean useAutoCompleteComplaintType() {
        return configurationService.useAutoCompleteForComplaintType();
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
        if (isNotBlank(crn) && complaint.isNew())
            complaint = complaintService.getComplaintByCRN(crn);
        return new ModelAndView("complaint/reg-success", "complaint", complaint);

    }

    @GetMapping("/complaint/downloadfile/{fileStoreId}")
    @ResponseBody
    public ResponseEntity download(@PathVariable String fileStoreId) {
        return fileStoreUtils.fileAsResponseEntity(fileStoreId, PGRConstants.MODULE_NAME, false);
    }

    protected void setReceivingMode(Complaint complaint, String receivingModeCode) {
        complaint.setReceivingMode(receivingModeService.getReceivingModeByCode(receivingModeCode));
    }

}