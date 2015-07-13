package org.egov.ptis.domain.entity.demand;

import org.egov.commons.Installment;
import org.egov.infstr.models.BaseModel;

public class BulkBillGeneration extends BaseModel{
    
    private String wardNumber;
    private Installment installment;
    
    public String getWardNumber() {
        return wardNumber;
    }
    public void setWardNumber(String wardNumber) {
        this.wardNumber = wardNumber;
    }
    public Installment getInstallment() {
        return installment;
    }
    public void setInstallment(Installment installment) {
        this.installment = installment;
    }

}
