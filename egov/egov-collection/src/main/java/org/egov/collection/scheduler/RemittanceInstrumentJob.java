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

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public void setRemittanceSchedulerService(RemittanceSchedulerService remittanceSchedulerService) {
        this.remittanceSchedulerService = remittanceSchedulerService;
    }

    public void setModulo(Integer modulo) {
        this.modulo = modulo;
    }
}
