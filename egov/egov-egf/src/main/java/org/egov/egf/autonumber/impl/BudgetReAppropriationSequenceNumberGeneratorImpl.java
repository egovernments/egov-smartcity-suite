package org.egov.egf.autonumber.impl;

import java.io.Serializable;

import org.egov.commons.dao.FinancialYearDAO;
import org.egov.egf.autonumber.BudgetReAppropriationSequenceNumberGenerator;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.model.budget.BudgetDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetReAppropriationSequenceNumberGeneratorImpl implements BudgetReAppropriationSequenceNumberGenerator {

    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;

    /**
     * 
     * Format IsBeRe/seqnumber/financialyear but sequence is running number for a year
     * 
     */
    public String getNextNumber(BudgetDetail bd)
    {
        String finRange = bd.getBudget().getFinancialYear().getFinYearRange();
        String type = bd.getBudget().getIsbere();

        String reAppropriationNumber = "";

        String sequenceName = "";
        if (type.equalsIgnoreCase("RE")) {
            sequenceName = "seq_budget_reapp_seqnum_re_" + finRange;
        } else {
            sequenceName = "seq_budget_reapp_seqnum_be_" + finRange;
        }
        Serializable nextSequence = applicationSequenceNumberGenerator.getNextSequence(sequenceName);

        reAppropriationNumber = String.format("%s/%04d/%s", type, nextSequence, finRange);

        return reAppropriationNumber;
    }
}
