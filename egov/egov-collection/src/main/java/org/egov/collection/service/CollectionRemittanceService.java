package org.egov.collection.service;

import java.io.Serializable;
import java.util.List;

import org.egov.collection.entity.ReceiptHeader;

public abstract class CollectionRemittanceService implements Serializable {
    private static final long serialVersionUID = 494234993113078236L;

    public abstract List<ReceiptHeader> createBankRemittance(final String[] serviceNameArr,
            final String[] totalCashAmount, final String[] totalChequeAmount, final String[] totalCardAmount,
            final String[] totalOnlineAmount, final String[] receiptDateArray, final String[] fundCodeArray,
            final String[] departmentCodeArray, final Integer accountNumberId, final Integer positionUser,
            final String[] receiptNumberArray);
}
