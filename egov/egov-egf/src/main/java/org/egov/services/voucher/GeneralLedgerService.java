package org.egov.services.voucher;

import org.egov.commons.CGeneralLedger;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class GeneralLedgerService extends PersistenceService<CGeneralLedger, Long> {
    
    public GeneralLedgerService(final Class<CGeneralLedger> generalLedger) {
        this.type = generalLedger;
    }
}