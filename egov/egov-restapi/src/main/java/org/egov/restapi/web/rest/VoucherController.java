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
package org.egov.restapi.web.rest;

import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.commons.CVoucherHeader;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.CreateVoucherHelper;
import org.egov.restapi.model.RestErrors;
import org.egov.restapi.service.ExternalVoucherService;
import org.egov.restapi.util.JsonConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class VoucherController {

    private static final Logger LOG = Logger.getLogger(VoucherController.class);

    @Autowired
    private CreateVoucher createVoucher;

    @Autowired
    private ExternalVoucherService externalVoucherService;

    @RequestMapping(value = "/egf/voucher/create", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String createVoucher(@RequestBody final CreateVoucherHelper createVoucherHelper,
            final HttpServletResponse response) {

        if (LOG.isDebugEnabled())
            LOG.debug("Rest API creating voucher with the data: " + createVoucherHelper);

        ApplicationThreadLocals.setUserId(2L);
        List<RestErrors> errorList = new ArrayList<>(0);
        final RestErrors re = new RestErrors();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            createVoucherHelper.getHeaderDetails().put("voucherdate",
                    formatter.parse(createVoucherHelper.getHeaderDetails().get("voucherdate").toString()));
        } catch (final ParseException e1) {
            re.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_NO_VOUCHERDATE_CODE_JSON_REQUEST);
            re.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_NO_VOUCHERDATE_MSG_JSON_REQUEST);
            errorList.add(re);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return JsonConvertor.convert(errorList);
        }
        errorList = externalVoucherService.validateVoucherReuest(createVoucherHelper);

        if (!errorList.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return JsonConvertor.convert(errorList);
        }
        final JsonObject jsonObject = new JsonObject();

        final CVoucherHeader cVoucherHeader;

        try {
            cVoucherHeader = createVoucher.createPreApprovedVoucher(createVoucherHelper.getHeaderDetails(),
                    createVoucherHelper.getAccountCodeDetails(), createVoucherHelper.getSubledgerDetails());
        } catch (final ApplicationRuntimeException e) {
            re.setErrorCode(e.getMessage());
            re.setErrorMessage(e.getMessage());
            errorList.add(re);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return JsonConvertor.convert(errorList);

        } catch (final ValidationException e) {
            re.setErrorCode(e.getErrors().get(0).getKey());
            re.setErrorMessage(e.getErrors().get(0).getMessage());
            errorList.add(re);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return JsonConvertor.convert(errorList);

        } catch (final Exception e) {
            re.setErrorCode(e.getMessage());
            re.setErrorMessage(e.getMessage());
            errorList.add(re);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return JsonConvertor.convert(errorList);

        }
        jsonObject.addProperty("successMessage", "Voucher Created Successfully");
        jsonObject.addProperty("voucherNumber", cVoucherHeader.getVoucherNumber());
        response.setStatus(HttpServletResponse.SC_CREATED);
        return jsonObject.toString();
    }

}
