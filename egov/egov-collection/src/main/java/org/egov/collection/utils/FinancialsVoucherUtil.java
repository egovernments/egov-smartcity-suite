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

package org.egov.collection.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.collection.constants.CollectionConstants;
import org.egov.commons.CVoucherHeader;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FinancialsVoucherUtil {

    private static final Logger LOGGER = Logger.getLogger(FinancialsVoucherUtil.class);
    @Autowired
    private CreateVoucher createVoucher;

    @Transactional
    public CVoucherHeader createApprovedVoucher(final Map<String, Object> headerdetails,
            final List<HashMap<String, Object>> accountcodedetails, final List<HashMap<String, Object>> subledgerdetails) {

        CVoucherHeader voucherHeaders = null;
        try {
            if (headerdetails instanceof HashMap) {
                // fetch from eg_modules once have master data in place
                headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);
                voucherHeaders = createVoucher.createVoucher((HashMap<String, Object>) headerdetails,
                        accountcodedetails, subledgerdetails);
            }
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error("Exception while creating voucher!", e);
            throw e;
        }
        return voucherHeaders;

    }

    /**
     * Get the pre-approval voucher created from financials
     *
     * @param headerdetails
     * @param accountcodedetails
     * @param subledgerdetails
     * @return CVoucherHeader
     */

    public CVoucherHeader createPreApprovalVoucher(final Map<String, Object> headerdetails,
            final List<HashMap<String, Object>> accountcodedetails, final List<HashMap<String, Object>> subledgerdetails)
            throws ApplicationRuntimeException {
        CVoucherHeader voucherHeaders = null;
        try {
            if (headerdetails instanceof HashMap)
                voucherHeaders = createVoucher.createPreApprovedVoucher((HashMap<String, Object>) headerdetails,
                        accountcodedetails, subledgerdetails);
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error("Exception while creating voucher!", e);
            throw e;
        }
        return voucherHeaders;
    }

}
