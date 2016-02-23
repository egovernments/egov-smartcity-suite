package org.egov.services.instrument;

import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentType;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class InstrumentTypeService extends PersistenceService<InstrumentType, Long> {

    public InstrumentTypeService(final Class<InstrumentType> instrumentType) {
        this.type = instrumentType;
    }
}
