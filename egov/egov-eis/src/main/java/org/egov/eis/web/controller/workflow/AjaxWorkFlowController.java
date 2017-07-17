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
package org.egov.eis.web.controller.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.AssignmentAdaptor;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
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
public class AjaxWorkFlowController {

    @Autowired
    private CustomizedWorkFlowService customizedWorkFlowService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private AssignmentService assignmentService;

    /**
     *
     * @param departmentRule
     * @param currentState
     * @param type
     * @param amountRule
     * @param additionalRule
     * @param pendingAction
     * @param approvalDepartment
     * @return List of Designation
     */
    @RequestMapping(value = "/ajaxWorkFlow-getDesignationsByObjectType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Designation> getDesignationsByObjectType(
            @ModelAttribute("designations") @RequestParam final String departmentRule, @RequestParam final String currentState,
            @RequestParam final String type,
            @RequestParam final String amountRule, @RequestParam final String additionalRule,
            @RequestParam final String pendingAction, @RequestParam final Long approvalDepartment) {

        List<Designation> designationList = customizedWorkFlowService.getNextDesignations(type,
                departmentRule, null, additionalRule, currentState,
                pendingAction, new Date());
        if (designationList.isEmpty())
            designationList = designationService.getAllDesignationByDepartment(approvalDepartment, new Date());
        return designationList;

    }
    
    /**
    *
    * @param departmentRule
    * @param currentState
    * @param type
    * @param amountRule
    * @param additionalRule
    * @param pendingAction
    * @param approvalDepartment
    * @return List of Designation
    */
   @RequestMapping(value = "/ajaxWorkFlow-getDesignationsForActiveAssignmentsByObjectType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
   public @ResponseBody List<Designation> getDesignationsForActiveAssignmentsByObjectType(
           @ModelAttribute("designations") @RequestParam final String departmentRule, @RequestParam final String currentState,
           @RequestParam final String type,
           @RequestParam final String amountRule, @RequestParam final String additionalRule,
           @RequestParam final String pendingAction, @RequestParam final Long approvalDepartment) {

       List<Designation> designationList = customizedWorkFlowService.getNextDesignationsForActiveAssignments(type,
               departmentRule, null, additionalRule, currentState,
               pendingAction, new Date());
       if (designationList.isEmpty())
           designationList = designationService.getAllDesignationByDepartment(approvalDepartment, new Date());
       return designationList;

   }

   /**
    * Temporary API to get next designations based on logged in user designation
    * @param departmentRule
    * @param currentState
    * @param type
    * @param amountRule
    * @param additionalRule
    * @param pendingAction
    * @param approvalDepartment
    * @return List of Designation
    */
   @RequestMapping(value = "/ajaxWorkFlow-getDesignationsForActiveAssignmentsByObjectTypeAndDesignation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
   public @ResponseBody List<Designation> getDesignationsForActiveAssignmentsByObjectTypeAndDesignation(
           @ModelAttribute("designations") @RequestParam final String departmentRule, @RequestParam final String currentState,
           @RequestParam final String type,
           @RequestParam final String amountRule, @RequestParam final String additionalRule,
           @RequestParam final String pendingAction, @RequestParam final Long approvalDepartment,
           @RequestParam final String currentDesignation) {

       List<Designation> designationList = customizedWorkFlowService.getNextDesignationsForActiveAssignments(type,
               departmentRule, null, additionalRule, currentState,
               pendingAction, new Date(), currentDesignation);
       if (designationList.isEmpty())
           designationList = designationService.getAllDesignationByDepartment(approvalDepartment, new Date());
       return designationList;

   }
   
    /**
     * Temporary API to get next designations based on logged in user designation
     * @param departmentRule
     * @param currentState
     * @param type
     * @param amountRule
     * @param additionalRule
     * @param pendingAction
     * @param approvalDepartment
     * @return List of Designation
     */
    @RequestMapping(value = "/ajaxWorkFlow-getDesignationsByObjectTypeAndDesignation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Designation> getDesignationsByObjectTypeAndDesignation(
            @ModelAttribute("designations") @RequestParam final String departmentRule, @RequestParam final String currentState,
            @RequestParam final String type,
            @RequestParam final String amountRule, @RequestParam final String additionalRule,
            @RequestParam final String pendingAction, @RequestParam final Long approvalDepartment,
            @RequestParam final String currentDesignation) {

        List<Designation> designationList = customizedWorkFlowService.getNextDesignations(type,
                departmentRule, null, additionalRule, currentState,
                pendingAction, new Date(), currentDesignation);
        if (designationList.isEmpty())
            designationList = designationService.getAllDesignationByDepartment(approvalDepartment, new Date());
        return designationList;

    }

    /**
     *
     * @param approvalDepartment
     * @param approvalDesignation
     * @return Approver For workflow
     */
    @RequestMapping(value = "/ajaxWorkFlow-positionsByDepartmentAndDesignation", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String getWorkFlowPositionByDepartmentAndDesignation(@RequestParam final Long approvalDepartment,
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
