package org.egov.services.voucher;

import org.egov.commons.CGeneralLedgerDetail;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class GeneralLedgerDetailService extends PersistenceService<CGeneralLedgerDetail, Long> {
    
    public GeneralLedgerDetailService(final Class<CGeneralLedgerDetail> generalLedgerDetail) {
        this.type = generalLedgerDetail;
    }
}
