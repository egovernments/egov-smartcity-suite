package org.egov.infra.config.process.multitenant.activiti;

import org.activiti.engine.impl.asyncexecutor.AsyncExecutor;
import org.activiti.engine.impl.asyncexecutor.ResetExpiredJobsRunnable;
import org.activiti.engine.impl.asyncexecutor.multitenant.ExecutorPerTenantAsyncExecutor;
import org.activiti.engine.impl.cfg.multitenant.TenantInfoHolder;

public class TenantAwareResetExpiredJobRunnable extends ResetExpiredJobsRunnable{

    protected TenantInfoHolder tenantInfoHolder;
    protected String tenantId;

    public TenantAwareResetExpiredJobRunnable(final AsyncExecutor asyncExecutor, TenantInfoHolder tenantInfoHolder, String tenantId) {
        super(asyncExecutor);
        this.tenantInfoHolder = tenantInfoHolder;
        this.tenantId = tenantId;
    }

    protected ExecutorPerTenantAsyncExecutor getTenantAwareAsyncExecutor() {
        return (ExecutorPerTenantAsyncExecutor) asyncExecutor;
    }

    @Override
    public synchronized void run() {
        tenantInfoHolder.setCurrentTenantId(tenantId);
        super.run();
        tenantInfoHolder.clearCurrentTenantId();
    }
}
