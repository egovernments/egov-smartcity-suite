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
package org.egov.restapi.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationException;
import org.egov.model.bills.EgBillregister;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.util.JsonConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillPaymentStatusController {
    private static final Logger LOG = Logger.getLogger(BillPaymentStatusController.class);

    @Autowired
    private EgovCommon egovCommon;

    @Autowired
    private ExpenseBillService expenseBillService;

    /**
     * API to get payment status for a Bill Number
     *
     * @param billNumber
     * @param request
     * @return paymentAmount
     */
    @RequestMapping(value = "/egf/bill/getpaymentamount", method = GET, produces = APPLICATION_JSON_VALUE)
    public String getPaymentAmount(@RequestParam final String billNumber,
            final HttpServletRequest request) {
        ApplicationThreadLocals.setUserId(2L);
        BigDecimal paymentAmount;
        EgBillregister egBillregister = null;
        ErrorDetails errorDetails;
        try {
            egBillregister = expenseBillService.getByBillnumber(billNumber);
            if (egBillregister == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NOT_VALID_BILLNUMBER);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NOT_VALID_BILLNUMBER);
                return JsonConvertor.convert(errorDetails);
            }
            paymentAmount = egovCommon.getPaymentAmount(egBillregister.getId());
        } catch (final ApplicationException e) {
            e.printStackTrace();
            final ErrorDetails er = new ErrorDetails();
            er.setErrorCode(e.getMessage());
            er.setErrorMessage(e.getMessage());
            return JsonConvertor.convert(er);
        }
        return JsonConvertor.convert(paymentAmount);
    }
}