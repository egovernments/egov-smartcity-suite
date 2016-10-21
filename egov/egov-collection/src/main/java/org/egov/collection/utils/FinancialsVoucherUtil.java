package org.egov.collection.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
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
                headerdetails.put(VoucherConstant.MODULEID, "10");
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
