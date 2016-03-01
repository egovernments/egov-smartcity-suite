package org.egov.services.instrument;

import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class InstrumentHeaderService extends PersistenceService<InstrumentHeader, Long> {

    public InstrumentHeaderService(final Class<InstrumentHeader> instrumentHeader) {
        this.type = instrumentHeader;
    }
}
