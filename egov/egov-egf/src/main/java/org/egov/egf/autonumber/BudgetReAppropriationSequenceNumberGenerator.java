package org.egov.egf.autonumber;

import org.egov.model.budget.BudgetDetail;
import org.springframework.stereotype.Service;

@Service
public interface BudgetReAppropriationSequenceNumberGenerator {
    public String getNextNumber(BudgetDetail bd);

}
