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
package org.egov.restapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.CreateVoucherHelper;
import org.egov.restapi.model.RestErrors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ExternalVoucherService {

    public List<RestErrors> validateVoucherReuest(final CreateVoucherHelper createVoucherHelper) {
        final List<RestErrors> errors = new ArrayList<>();
        RestErrors restErrors = null;
        if (createVoucherHelper == null) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_JSON_REQUEST);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_JSON_REQUEST);
            errors.add(restErrors);
        }

        if (createVoucherHelper != null && createVoucherHelper.getHeaderDetails().isEmpty()) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_VOUCHER_HEADERDETAILS_CODE_JSON_REQUEST);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_VOUCHER_HEADERDETAILS_MSG_JSON_REQUEST);
            errors.add(restErrors);
        }

        if (createVoucherHelper != null && createVoucherHelper.getAccountCodeDetails().isEmpty()) {
            restErrors = new RestErrors();
            restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_ACCOUNTCODEDETAILS_CODE_JSON_REQUEST);
            restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_ACCOUNTCODEDETAILS_MSG_JSON_REQUEST);
            errors.add(restErrors);
        }

        if (createVoucherHelper != null && !createVoucherHelper.getHeaderDetails().isEmpty()
                && createVoucherHelper.getHeaderDetails().get("voucherdate") != null) {
            final Date voucherDate = (Date) createVoucherHelper.getHeaderDetails().get("voucherdate");
            if (voucherDate.after(new Date())) {
                restErrors = new RestErrors();
                restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_VOUCHERDATE_CODE_JSON_REQUEST);
                restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_VOUCHERDATE_MSG_JSON_REQUEST);
                errors.add(restErrors);
            }
        }

        return errors;
    }

}
