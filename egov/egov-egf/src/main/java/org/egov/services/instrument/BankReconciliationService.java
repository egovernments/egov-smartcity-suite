package org.egov.services.instrument;

import org.egov.commons.Bankreconciliation;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class BankReconciliationService extends PersistenceService<Bankreconciliation, Integer> {

    public BankReconciliationService(final Class<Bankreconciliation> bankreconciliation) {
        this.type = bankreconciliation;
    }
}
