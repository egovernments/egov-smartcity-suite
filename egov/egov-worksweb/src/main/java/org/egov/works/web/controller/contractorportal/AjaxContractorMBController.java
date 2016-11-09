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
package org.egov.works.web.controller.contractorportal;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.works.contractorportal.service.ContractorMBHeaderService;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/contractorportal/mb")
public class AjaxContractorMBController {

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private ContractorMBHeaderService contractorMBHeaderService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/ajaxworkorder-mbheader", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findWorkOrderForMBHeader(@RequestParam final String workOrderNo) {
        return workOrderEstimateService.findWorkOrderForMBHeader(workOrderNo);
    }

    @RequestMapping(value = "/ajax-sendotp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String sendOTPForContractorMB(@RequestParam final String workOrderNo) {
        final WorkOrder workOrder = letterOfAcceptanceService.getApprovedWorkOrder(workOrderNo);
        final String mobileNumber = workOrder.getContractor().getMobileNumber();
        String response = "";
        if (StringUtils.isNotBlank(mobileNumber))
            response = contractorMBHeaderService.sendOTPMessage(mobileNumber).toString();
        else
            response = messageSource.getMessage("msg.contractor.mobilenumber.not.exist",
                    new String[] { ApplicationThreadLocals.getMunicipalityName() }, null);
        return response;
    }

    @RequestMapping(value = "/ajax-validateotp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean validateOTPForContractorMB(@RequestParam final String workOrderNo,
            @RequestParam final String otp) {
        final WorkOrder workOrder = letterOfAcceptanceService.getApprovedWorkOrder(workOrderNo);
        final String mobileNumber = workOrder.getContractor().getMobileNumber();
        boolean success = false;
        if (StringUtils.isNotBlank(otp))
            success = contractorMBHeaderService.isValidOTP(otp, mobileNumber);
        return success;
    }
}
