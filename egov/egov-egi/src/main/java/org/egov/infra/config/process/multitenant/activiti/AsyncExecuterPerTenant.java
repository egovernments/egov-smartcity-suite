package org.egov.infra.config.process.multitenant.activiti;

import org.activiti.engine.impl.asyncexecutor.AsyncExecutor;
import org.activiti.engine.impl.asyncexecutor.DefaultAsyncJobExecutor;
import org.activiti.engine.impl.asyncexecutor.multitenant.ExecutorPerTenantAsyncExecutor;
import org.activiti.engine.impl.asyncexecutor.multitenant.TenantAwareAcquireAsyncJobsDueRunnable;
import org.activiti.engine.impl.asyncexecutor.multitenant.TenantAwareAcquireTimerJobsRunnable;
import org.activiti.engine.impl.asyncexecutor.multitenant.TenantAwareExecuteAsyncRunnableFactory;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cfg.multitenant.TenantInfoHolder;

public class AsyncExecuterPerTenant extends ExecutorPerTenantAsyncExecutor {
    public AsyncExecuterPerTenant(TenantInfoHolder tenantInfoHolder) {
        super(tenantInfoHolder, null);
    }

    public void addTenantAsyncExecutor(String tenantId, boolean startExecutor) {
        AsyncExecutor tenantExecutor;

        if (tenantAwareAyncExecutorFactory == null) {
            tenantExecutor = new DefaultAsyncJobExecutor();
        } else {
            tenantExecutor = tenantAwareAyncExecutorFactory.createAsyncExecutor(tenantId);
        }

        tenantExecutor.setProcessEngineConfiguration(processEngineConfiguration);

        if (tenantExecutor instanceof DefaultAsyncJobExecutor) {
            DefaultAsyncJobExecutor defaultAsyncJobExecutor = (DefaultAsyncJobExecutor) tenantExecutor;
            defaultAsyncJobExecutor.setAsyncJobsDueRunnable(new TenantAwareAcquireAsyncJobsDueRunnable(defaultAsyncJobExecutor, tenantInfoHolder, tenantId));
            defaultAsyncJobExecutor.setTimerJobRunnable(new TenantAwareAcquireTimerJobsRunnable(defaultAsyncJobExecutor, tenantInfoHolder, tenantId));
            defaultAsyncJobExecutor.setExecuteAsyncRunnableFactory(new TenantAwareExecuteAsyncRunnableFactory(tenantInfoHolder, tenantId));
            defaultAsyncJobExecutor.setResetExpiredJobsRunnable(new TenantAwareResetExpiredJobRunnable(defaultAsyncJobExecutor,tenantInfoHolder, tenantId));
        }
        tenantExecutors.put(tenantId, tenantExecutor);

        if (startExecutor) {
            tenantExecutor.start();
        }
    }

    public void setProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
        for (AsyncExecutor asyncExecutor : tenantExecutors.values()) {
            asyncExecutor.setProcessEngineConfiguration(processEngineConfiguration);
        }
    }
}
