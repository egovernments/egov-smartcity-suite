/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.config.scheduling;

import org.egov.infra.scheduler.quartz.QuartzJobAwareBeanFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class QuartzSchedulerConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment env;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        QuartzJobAwareBeanFactory jobFactory = new QuartzJobAwareBeanFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean(destroyMethod = "destroy")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SimpleThreadPoolTaskExecutor taskExecutor() {
        SimpleThreadPoolTaskExecutor taskExecutor = new SimpleThreadPoolTaskExecutor();
        taskExecutor.setInstanceId("AUTO");
        taskExecutor.setInstanceName("ERP_APP_SCHEDULER");
        taskExecutor.setThreadCount(10);
        taskExecutor.setThreadNamePrefix("ERP_APP_SCHEDULER");
        taskExecutor.setThreadPriority(5);
        taskExecutor.setThreadsInheritContextClassLoaderOfInitializingThread(true);
        taskExecutor.setThreadsInheritGroupOfInitializingThread(true);
        taskExecutor.setWaitForJobsToCompleteOnShutdown(true);
        return taskExecutor;
    }

    protected SchedulerFactoryBean createSchedular(DataSource dataSource) {
        return this.createSchedular(dataSource, null);
    }

    protected SchedulerFactoryBean createSchedular(DataSource dataSource, String schemaPrefix) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setAutoStartup(false);
        schedulerFactory.setExposeSchedulerInRepository(true);
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setTaskExecutor(taskExecutor());
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(false);
        schedulerFactory.setQuartzProperties(quartzProperties(schemaPrefix));
        schedulerFactory.setDataSource(dataSource);

        return schedulerFactory;
    }

    private Properties quartzProperties(String schemaPrefix) {
        Properties quartzProps = new Properties();
        String tablePrefix = isBlank(schemaPrefix) ? "QRTZ_" : schemaPrefix + ".QRTZ_";
        //Scheduler config
        quartzProps.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_ID, "AUTO");
        quartzProps.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, "ERP_APP_SCHEDULER");
        quartzProps.put(StdSchedulerFactory.PROP_SCHED_WRAP_JOB_IN_USER_TX, "false");
        quartzProps.put(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, "true");
        quartzProps.put("org.quartz.scheduler.threadsInheritContextClassLoaderOfInitializer", "true");

        //Cluster job store config
        quartzProps.put("org.quartz.jobStore.isClustered", env.getProperty("scheduler.clustered"));
        quartzProps.put("org.quartz.jobStore.clusterCheckinInterval", "60000");
        quartzProps.put("org.quartz.jobStore.acquireTriggersWithinLock", "false");
        quartzProps.put("org.quartz.jobStore.txIsolationLevelReadCommitted", "true");
        quartzProps.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
        quartzProps.put("org.quartz.jobStore.useProperties", "true");
        quartzProps.put("org.quartz.jobStore.dataSource", "quartzDS");
        quartzProps.put("org.quartz.jobStore.nonManagedTXDataSource", "quartzNoTXDS");
        quartzProps.put("org.quartz.jobStore.tablePrefix", tablePrefix);
        quartzProps.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreCMT");
        quartzProps.put("org.quartz.jobStore.dontSetNonManagedTXConnectionAutoCommitFalse", "false");
        quartzProps.put("org.quartz.jobStore.dontSetAutoCommitFalse", "true");

        //Datasource config
        quartzProps.put("org.quartz.dataSource.quartzDS.jndiURL", env.getProperty("default.jdbc.jndi.datasource"));
        quartzProps.put("org.quartz.dataSource.quartzNoTXDS.jndiURL", env.getProperty("default.jdbc.jndi.quartz.datasource"));

        //Logging plugin config
        quartzProps.put("org.quartz.plugin.jobHistory.class", "org.quartz.plugins.history.LoggingJobHistoryPlugin");
        quartzProps.put("org.quartz.plugin.jobHistory.jobToBeFiredMessage", "Job [{1}.{0}] to be fired by trigger [{4}.{3}], re-fire: {7}");
        quartzProps.put("org.quartz.plugin.jobHistory.jobSuccessMessage", "Job [{1}.{0}] execution complete and reports: {8}");
        quartzProps.put("org.quartz.plugin.jobHistory.jobFailedMessage", "Job [{1}.{0}] execution failed with exception: {8}");
        quartzProps.put("org.quartz.plugin.jobHistory.jobWasVetoedMessage", "Job [{1}.{0}] was vetoed. It was to be fired by trigger [{4}.{3}] at: {2, date, dd-MM-yyyy HH:mm:ss.SSS}");
        quartzProps.put("org.quartz.plugin.triggerHistory.class", "org.quartz.plugins.history.LoggingTriggerHistoryPlugin");
        quartzProps.put("org.quartz.plugin.triggerHistory.triggerFiredMessage", "Trigger [{1}.{0}] fired job [{6}.{5}] scheduled at: {2, date, dd-MM-yyyy HH:mm:ss.SSS}, next scheduled at: {3, date, dd-MM-yyyy HH:mm:ss.SSS}");
        quartzProps.put("org.quartz.plugin.triggerHistory.triggerCompleteMessage", "Trigger [{1}.{0}] completed firing job [{6}.{5}] with resulting trigger instruction code: {9}. Next scheduled at: {3, date, dd-MM-yyyy HH:mm:ss.SSS}");
        quartzProps.put("org.quartz.plugin.triggerHistory.triggerMisfiredMessage", "Trigger [{1}.{0}] misfired job [{6}.{5}]. Should have fired at: {3, date, dd-MM-yyyy HH:mm:ss.SSS}");

        //Shutdown plugin config
        quartzProps.put("org.quartz.plugin.shutdownHook.class", "org.quartz.plugins.management.ShutdownHookPlugin");
        quartzProps.put("org.quartz.plugin.shutdownHook.cleanShutdown", "true");

        return quartzProps;
    }
}
