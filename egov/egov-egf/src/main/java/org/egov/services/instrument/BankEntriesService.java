package org.egov.services.instrument;

import org.egov.infstr.services.PersistenceService;
import org.egov.model.brs.BrsEntries;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class BankEntriesService extends PersistenceService<BrsEntries, Long> {

    public BankEntriesService(final Class<BrsEntries> brsEntries) {
        this.type = brsEntries;
    }

    public BankEntriesService() {
    }
}
