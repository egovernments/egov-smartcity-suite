package org.egov.wtms.autonumber;

import org.springframework.stereotype.Service;

@Service
public interface BillReferenceNumberGenerator {

    public String generateBillNumber(final String installmentYear);
}
