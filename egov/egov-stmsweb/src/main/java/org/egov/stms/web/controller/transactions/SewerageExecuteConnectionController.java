/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
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
 *      Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *      For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *      For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
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
package org.egov.stms.web.controller.transactions;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.REVENUE_WARD;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.stms.elasticsearch.entity.SewerageBulkExecutionResponse;
import org.egov.stms.elasticsearch.entity.SewerageExecutionResult;
import org.egov.stms.masters.repository.SewerageApplicationTypeRepository;
import org.egov.stms.service.es.SewerageIndexService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.utils.SewerageExecutionResultAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageExecuteConnectionController {

    @Autowired
    protected MessageSource messageSource;
    @Autowired
    private SewerageIndexService sewerageIndexService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private SewerageApplicationTypeRepository sewerageApplicationTypeRepository;

    @ModelAttribute("revenueWards")
    public List<Boundary> revenueWardList() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                REVENUE_WARD, REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute
    public SewerageExecutionResult sewerageExecutionResult() {
        return new SewerageExecutionResult();
    }

    @RequestMapping(value = "/connexecutionsearch", method = RequestMethod.GET)
    public String showExecutionConnection(final Model model) {
        model.addAttribute("applicationtype", sewerageApplicationTypeRepository.findAll());
        return "sewerageExecution-search";
    }

    @RequestMapping(value = "/ajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String executeSewerage(@ModelAttribute SewerageExecutionResult sewerageExecutionResult) {
        List<SewerageExecutionResult> connectionExecutionList = sewerageIndexService
                .getConnectionExecutionList(sewerageExecutionResult);
        return new StringBuilder("{ \"data\":").append(toJSON(connectionExecutionList, SewerageExecutionResult.class,
                SewerageExecutionResultAdapter.class)).append("}").toString();
    }

    @RequestMapping(value = "/connexecutionupdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateData(@RequestBody SewerageBulkExecutionResponse sewerageBulkExecutionResponse) {
        List<SewerageApplicationDetails> sewerageApplicationDetailsList = new ArrayList<>();
        String validationstatus = sewerageIndexService.validateDate(sewerageBulkExecutionResponse,
                sewerageApplicationDetailsList);
        Boolean updateStatus = sewerageIndexService.update(sewerageApplicationDetailsList);
        String response;
        if (sewerageBulkExecutionResponse.getSewerageExecutionResult().length <= 0) {
            response = "EmptyList";
        } else if (!validationstatus.isEmpty()) {
            response = validationstatus;
        } else if (!updateStatus) {
            response = "UpdateExecutionFailed";
        } else
            response = "Success";
        return response;
    }
}