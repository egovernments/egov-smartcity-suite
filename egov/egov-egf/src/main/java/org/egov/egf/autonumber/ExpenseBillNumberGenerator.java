package org.egov.egf.autonumber;

import org.egov.model.bills.EgBillregister;
import org.springframework.stereotype.Service;

@Service
public interface ExpenseBillNumberGenerator {
    public String getNextNumber(EgBillregister br);

}
