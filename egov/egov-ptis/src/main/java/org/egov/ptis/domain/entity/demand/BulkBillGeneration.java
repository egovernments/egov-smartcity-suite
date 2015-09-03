package org.egov.ptis.domain.entity.demand;

import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.models.BaseModel;

public class BulkBillGeneration extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = -4483307577428349596L;
    private Installment installment;
    private Boundary zone;
    private Boundary ward;

    public Installment getInstallment() {
        return installment;
    }

    public void setInstallment(final Installment installment) {
        this.installment = installment;
    }

    public Boundary getZone() {
        return zone;
    }

    public void setZone(final Boundary zone) {
        this.zone = zone;
    }

    public Boundary getWard() {
        return ward;
    }

    public void setWard(final Boundary ward) {
        this.ward = ward;
    }

}
