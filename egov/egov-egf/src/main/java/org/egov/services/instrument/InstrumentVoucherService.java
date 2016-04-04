package org.egov.services.instrument;

import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentVoucher;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class InstrumentVoucherService extends PersistenceService<InstrumentVoucher, Long> {

    public InstrumentVoucherService(final Class<InstrumentVoucher> instrumentVoucher) {
        this.type = instrumentVoucher;
    }
}
