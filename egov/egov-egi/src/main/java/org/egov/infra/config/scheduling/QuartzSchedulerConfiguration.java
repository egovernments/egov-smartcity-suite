/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.config.scheduling;

import org.egov.infra.scheduler.quartz.QuartzJobAwareBeanFactory;
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

import static org.quartz.impl.StdSchedulerFactory.AUTO_GENERATE_INSTANCE_ID;
import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_BATCH_TIME_WINDOW;
import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_INSTANCE_ID;
import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME;
import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_MAX_BATCH_SIZE;
import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_SCHEDULER_THREADS_INHERIT_CONTEXT_CLASS_LOADER_OF_INITIALIZING_THREAD;
import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_WRAP_JOB_IN_USER_TX;

public class QuartzSchedulerConfiguration {

    private static final String APP_SCHEDULER_NAME = "ERP_APP_SCHEDULER";
    private static final String FALSE = "false";
    private static final String TRUE = "true";

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
        taskExecutor.setInstanceName(APP_SCHEDULER_NAME);
        taskExecutor.setThreadCount(10);
        taskExecutor.setThreadNamePrefix(APP_SCHEDULER_NAME);
        taskExecutor.setThreadPriority(5);
        taskExecutor.setThreadsInheritContextClassLoaderOfInitializingThread(true);
        taskExecutor.setThreadsInheritGroupOfInitializingThread(true);
        taskExecutor.setWaitForJobsToCompleteOnShutdown(true);
        return taskExecutor;
    }

    protected SchedulerFactoryBean createScheduler(DataSource dataSource) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setAutoStartup(false);
        schedulerFactory.setExposeSchedulerInRepository(true);
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setTaskExecutor(taskExecutor());
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(false);
        schedulerFactory.setQuartzProperties(quartzProperties());
        schedulerFactory.setDataSource(dataSource);

        return schedulerFactory;
    }

    private Properties quartzProperties() {
        Properties quartzProps = new Properties();

        //Scheduler config
        quartzProps.put(PROP_SCHED_INSTANCE_ID, AUTO_GENERATE_INSTANCE_ID);
        quartzProps.put(PROP_SCHED_INSTANCE_NAME, APP_SCHEDULER_NAME);
        quartzProps.put(PROP_SCHED_WRAP_JOB_IN_USER_TX, FALSE);
        quartzProps.put(PROP_SCHED_MAX_BATCH_SIZE, 10);
        quartzProps.put(PROP_SCHED_BATCH_TIME_WINDOW, 1000);
        quartzProps.put(PROP_SCHED_SCHEDULER_THREADS_INHERIT_CONTEXT_CLASS_LOADER_OF_INITIALIZING_THREAD, TRUE);

        //Cluster job store config
        quartzProps.put("org.quartz.jobStore.isClustered", env.getProperty("scheduler.clustered"));
        quartzProps.put("org.quartz.jobStore.clusterCheckinInterval", "60000");
        quartzProps.put("org.quartz.jobStore.acquireTriggersWithinLock", FALSE);
        quartzProps.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
        quartzProps.put("org.quartz.jobStore.useProperties", TRUE);
        quartzProps.put("org.quartz.jobStore.dataSource", "quartzDS");
        quartzProps.put("org.quartz.jobStore.nonManagedTXDataSource", "quartzNoTXDS");
        quartzProps.put("org.quartz.jobStore.tablePrefix", env.getProperty("scheduler.default.table.prefix"));
        quartzProps.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreCMT");
        quartzProps.put("org.quartz.jobStore.dontSetNonManagedTXConnectionAutoCommitFalse", FALSE);
        quartzProps.put("org.quartz.jobStore.dontSetAutoCommitFalse", TRUE);

        //Datasource config
        quartzProps.put("org.quartz.dataSource.quartzDS.jndiURL", env.getProperty("default.jdbc.jndi.datasource"));
        quartzProps.put("org.quartz.dataSource.quartzNoTXDS.jndiURL", env.getProperty("scheduler.datasource.jndi.url"));

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
        quartzProps.put("org.quartz.plugin.shutdownHook.cleanShutdown", TRUE);

        return quartzProps;
    }
}
