package org.egov.collection.scheduler;

import org.egov.collection.integration.services.RemittanceSchedulerService;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;

public class RemittanceInstrumentJob  extends AbstractQuartzJob {

    private static final long serialVersionUID = -8293830861860894611L;
    private transient RemittanceSchedulerService remittanceSchedulerService;
    private String instrumentType;
    private Integer modulo;

    @Override
    public void executeJob() {
        remittanceSchedulerService.remittanceInstrumentProcess(instrumentType, modulo);
    }

    public void setInstrumentType(String instType) {
        this.instrumentType = instType;
    }

    public void setRemittanceSchedulerService(RemittanceSchedulerService remitSchedulerService) {
        this.remittanceSchedulerService = remitSchedulerService;
    }

    public void setModulo(Integer modul) {
        this.modulo = modul;
    }
}
