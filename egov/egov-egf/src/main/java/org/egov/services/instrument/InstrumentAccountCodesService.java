package org.egov.services.instrument;

import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentAccountCodes;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class InstrumentAccountCodesService extends PersistenceService<InstrumentAccountCodes, Long> {

    public InstrumentAccountCodesService(final Class<InstrumentAccountCodes> instrumentAccountCodes) {
        this.type = instrumentAccountCodes;
    }
}
