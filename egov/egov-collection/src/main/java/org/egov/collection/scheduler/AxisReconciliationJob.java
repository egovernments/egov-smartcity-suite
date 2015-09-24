package org.egov.collection.scheduler;

import org.egov.collection.integration.services.SchedularService;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AxisReconciliationJob extends AbstractQuartzJob {

    private static final long serialVersionUID = -8293830861860894611L;
    SchedularService schedularService;


    @Override
    public void executeJob() {
        schedularService.reconcileAXIS();
    }


    public void setSchedularService(final SchedularService schedularService) {
        this.schedularService = schedularService;
    }
}
