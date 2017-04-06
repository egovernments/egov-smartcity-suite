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
package org.egov.bpa.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.egov.bpa.application.service.ApplicationBpaService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.AssignmentAdaptor;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.pims.commons.Designation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Controller
public class BpaAjaxController {
    
    @Autowired
    private DesignationService designationService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private ApplicationBpaService applicationBpaService;

    @RequestMapping(value = "/ajax/getAdmissionFees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BigDecimal isConnectionPresentForProperty(@RequestParam final String serviceType) {
        return applicationBpaService.setAdmissionFeeAmountForRegistration(serviceType);
    }

   

    @RequestMapping(value = "/ajax-designationsByDepartment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Designation> getDesignationsByDepartmentId(
            @ModelAttribute("designations") @RequestParam final Long approvalDepartment) {
        List<Designation> designations = new ArrayList<Designation>();
        if (approvalDepartment != null && approvalDepartment != 0 && approvalDepartment != -1)
            designations = designationService.getAllDesignationByDepartment(approvalDepartment, new Date());
        designations.forEach(designation -> designation.toString());
        return designations;
    }

    @RequestMapping(value = "/ajax-positionsByDepartmentAndDesignation", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String getPositionByDepartmentAndDesignation(@RequestParam final Long approvalDepartment,
            @RequestParam final Long approvalDesignation, final HttpServletResponse response) {
        List<Assignment> assignmentList = new ArrayList<Assignment>();
        if (approvalDepartment != null && approvalDepartment != 0 && approvalDepartment != -1
                && approvalDesignation != null && approvalDesignation != 0 && approvalDesignation != -1) {
            assignmentList = assignmentService.findAllAssignmentsByDeptDesigAndDates(approvalDepartment,
                    approvalDesignation, new Date());
            final Gson jsonCreator = new GsonBuilder().registerTypeAdapter(Assignment.class, new AssignmentAdaptor())
                    .create();
            return jsonCreator.toJson(assignmentList, new TypeToken<Collection<Assignment>>() {
            }.getType());
        }
        return "[]";
    }

}
