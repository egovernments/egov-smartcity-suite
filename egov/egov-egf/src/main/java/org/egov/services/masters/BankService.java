package org.egov.services.masters;

import org.egov.commons.Bank;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class BankService extends PersistenceService<Bank, Integer> {

}

