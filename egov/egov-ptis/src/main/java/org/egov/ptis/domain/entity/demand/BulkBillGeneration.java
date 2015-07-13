package org.egov.ptis.domain.entity.demand;

import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.models.BaseModel;

public class BulkBillGeneration extends BaseModel{
    
    private Installment installment;
    private Boundary zone;
    private Boundary ward;
    
    public Installment getInstallment() {
        return installment;
    }
    public void setInstallment(Installment installment) {
        this.installment = installment;
    }
    public Boundary getZone() {
        return zone;
    }
    public void setZone(Boundary zone) {
        this.zone = zone;
    }
    public Boundary getWard() {
        return ward;
    }
    public void setWard(Boundary ward) {
        this.ward = ward;
    }

}
