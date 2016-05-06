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

package org.egov.pgr.web.controller.complaint;

import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.ReceivingCenterService;
import org.egov.pgr.utils.constants.PGRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public abstract class GenericComplaintController {

    public static final String ERROR = "error";
    public static final String MESSAGE = "message";

    protected @Autowired ComplaintTypeService complaintTypeService;
    protected @Autowired(required = true) ComplaintService complaintService;
    protected @Autowired CrossHierarchyService crossHierarchyService;
    protected @Autowired ReceivingCenterService receivingCenterService;
    protected @Autowired ComplaintTypeCategoryService complaintTypeCategoryService;
    @Qualifier("fileStoreService")
    protected @Autowired FileStoreService fileStoreService;
    protected @Autowired FileStoreUtils fileStoreUtils;

    public @ModelAttribute("categories") List<ComplaintTypeCategory> complaintTypeCategories() {
        return complaintTypeCategoryService.findAll();
    }

    public @ModelAttribute("complaintTypes") List<ComplaintType> frequentlyFiledComplaintTypes() {
        return complaintTypeService.getFrequentlyFiledComplaints();
    }

    @RequestMapping(value = "/complaint/reg-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute Complaint complaint, final HttpServletRequest request) {
        if (request.getParameter("crn") != null && complaint.isNew())
            complaint = complaintService.getComplaintByCRN(request.getParameter("crn"));
        return new ModelAndView("complaint/reg-success", "complaint", complaint);

    }

    @RequestMapping(value = "/complaint/downloadfile/{fileStoreId}")
    public void download(@PathVariable final String fileStoreId, final HttpServletResponse response) throws IOException {
        fileStoreUtils.fetchFileAndWriteToStream(fileStoreId, PGRConstants.MODULE_NAME, false, response);
    }

}