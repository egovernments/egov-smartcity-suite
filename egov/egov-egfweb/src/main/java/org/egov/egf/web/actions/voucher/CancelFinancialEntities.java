package org.egov.egf.web.actions.voucher;

import org.egov.commons.CVoucherHeader;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.model.bills.EgBillregister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelFinancialEntities implements CancelBillAndVoucher {
    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public boolean canCancelBill(final EgBillregister billRegister) {

        if (securityUtils.getCurrentUser().getId().longValue() == billRegister.getCreatedBy().getId().longValue())
            return true;
        else
            return false;

    }

    @Override
    public boolean canCancelVoucher(final CVoucherHeader voucher) {
        if (securityUtils.getCurrentUser().getId().longValue() == voucher.getCreatedBy().getId().longValue())
            return true;
        else
            return false;
    }
}
