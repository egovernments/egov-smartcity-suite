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
package org.egov.egf.voucher.service;

import org.egov.egf.contract.model.ErrorDetail;
import org.egov.egf.contract.model.VoucherContract;
import org.egov.egf.contract.model.VoucherRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContractVoucherService {
    
    public List<ErrorDetail> validateVoucherReuest(final VoucherContract request) {
        final List<ErrorDetail> errors = new ArrayList<>();
        ErrorDetail errorDetails = new ErrorDetail();
        
        if (request == null) {
            errorDetails.setErrorCode("EGF-VOUCHER-1");
            errorDetails.setErrorMessage("Please send valid JSON request");
            errors.add(errorDetails);
        }
        
        VoucherRequest VoucherRequest = request.getVouchers().get(0);
        
        if (VoucherRequest.getLedgers().size() > 100) {
            errorDetails.setErrorCode("EGF-VOUCHER-2");
            errorDetails.setErrorMessage("Account Details size cannot be greater then 30");
            errors.add(errorDetails);
        }

        if (request != null && VoucherRequest.getLedgers().isEmpty()) {
            errorDetails.setErrorCode("EGF-VOUCHER-5");
            errorDetails.setErrorMessage("Account Details List cannot be empty");
            errors.add(errorDetails);
        }
        
        validateVoucherDate(request, errors, errorDetails, VoucherRequest);
     
        return errors;
    }

    private void validateVoucherDate(final VoucherContract request, final List<ErrorDetail> errors, ErrorDetail errorDetails,
            VoucherRequest VoucherRequest) {
        if(VoucherRequest.getVoucherDate().isEmpty()) {
            errorDetails.setErrorCode("EGF-VOUCHER-3");
            errorDetails.setErrorMessage("Voucher Date cannot be empty");
            errors.add(errorDetails);
        }
        
        if (request != null && !VoucherRequest.getVoucherDate().isEmpty()) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date voucherDate = null;
            try {
                voucherDate = (Date) formatter.parse(VoucherRequest.getVoucherDate());
            } catch (ParseException e) {
                errorDetails.setErrorCode("EGF-VOUCHER-3");
                errorDetails.setErrorMessage("Please send the Voucher Date in DD-MM-YYYY format");
                errors.add(errorDetails);
            }
            if (voucherDate != null && voucherDate.after(new Date())) {
                errorDetails.setErrorCode("EGF-VOUCHER-4");
                errorDetails.setErrorMessage("Voucher date cannot be future date");
                errors.add(errorDetails);
            }
        }
    }

}
