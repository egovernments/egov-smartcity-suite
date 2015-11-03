package org.egov.services.masters;

import org.egov.commons.Bank;
import org.egov.infstr.services.PersistenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="bankService")
@Transactional(readOnly = true)
public class BankService extends PersistenceService<Bank, Integer> {

}

