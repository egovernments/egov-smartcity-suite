package org.egov.egf.web.actions.voucher;

import org.egov.commons.CVoucherHeader;
import org.egov.model.bills.EgBillregister;

public interface CancelBillAndVoucher {
    public boolean canCancelBill(EgBillregister bill);

    public boolean canCancelVoucher(CVoucherHeader voucher);
}
