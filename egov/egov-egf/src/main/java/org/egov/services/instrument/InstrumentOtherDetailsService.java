package org.egov.services.instrument;

import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class InstrumentOtherDetailsService extends PersistenceService<InstrumentOtherDetails, Long> {

    public InstrumentOtherDetailsService(final Class<InstrumentOtherDetails> instrumentOtherDetails) {
        this.type = instrumentOtherDetails;
    }
}
