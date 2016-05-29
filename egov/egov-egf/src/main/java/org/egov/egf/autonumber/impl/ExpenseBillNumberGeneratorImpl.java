package org.egov.egf.autonumber.impl;

import java.io.Serializable;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.egf.autonumber.ExpenseBillNumberGenerator;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.model.bills.EgBillregister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseBillNumberGeneratorImpl implements ExpenseBillNumberGenerator {

    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;

    /**
     * 
     * Format DepartmentCode/EJV/seqnumber/financialyear but sequence is running number for a year
     * 
     */
    public String getNextNumber(EgBillregister br)
    {
        String expenseBillNumber = "";

        String sequenceName = "";

        final CFinancialYear financialYear = financialYearDAO.getFinancialYearByDate(br.getBilldate());
        if (financialYear == null)
            throw new ApplicationRuntimeException("Financial Year is not defined for the voucher date");
        sequenceName = "seq_expense_billnumber_" + financialYear.getFinYearRange();
        Serializable nextSequence = applicationSequenceNumberGenerator.getNextSequence(sequenceName);

        expenseBillNumber = String.format("%s/%s/%04d/%s", br.getEgBillregistermis().getEgDepartment().getCode(), "EJV",
                nextSequence, financialYear.getFinYearRange());

        return expenseBillNumber;
    }
}
