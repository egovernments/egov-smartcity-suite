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

package org.egov.ptis.config;

import org.egov.infra.config.scheduling.QuartzSchedulerConfiguration;
import org.egov.infra.config.scheduling.SchedulerConfigCondition;
import org.egov.ptis.scheduler.BulkBillGenerationJob;
import org.egov.ptis.scheduler.DemandActivationJob;
import org.egov.ptis.scheduler.RecoveryNoticesJob;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING;

@Configuration
@Conditional(SchedulerConfigCondition.class)
public class PtisSchedulerConfiguration extends QuartzSchedulerConfiguration {

    @Bean
    public SchedulerFactoryBean ptisScheduler(DataSource dataSource) {
        SchedulerFactoryBean ptisScheduler = createSchedular(dataSource);
        ptisScheduler.setSchedulerName("ptis-scheduler");
        ptisScheduler.setAutoStartup(true);
        ptisScheduler.setOverwriteExistingJobs(true);
        ptisScheduler.setTriggers(
                ptisBulkBillGenerationCronTrigger0().getObject(),
                ptisBulkBillGenerationCronTrigger1().getObject(),
                ptisBulkBillGenerationCronTrigger2().getObject(),
                ptisBulkBillGenerationCronTrigger3().getObject(),
                ptisBulkBillGenerationCronTrigger4().getObject(),
                demandActivationCronTrigger().getObject()
        );
        return ptisScheduler;
    }

    @Bean(name = "recoveryNoticeScheduler")
    public SchedulerFactoryBean recoveryNoticeScheduler(DataSource dataSource) {
        SchedulerFactoryBean recoveryNoticeScheduler = createSchedular(dataSource, "public");
        recoveryNoticeScheduler.setSchedulerName("recovery-notice-scheduler");
        recoveryNoticeScheduler.setAutoStartup(true);
        recoveryNoticeScheduler.setOverwriteExistingJobs(true);
        recoveryNoticeScheduler.setStartupDelay(1500);
        return recoveryNoticeScheduler;
    }

    @Bean(name = "recoveryNoticeJobDetail")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JobDetailFactoryBean recoveryNoticeJobDetail() {
        JobDetailFactoryBean recoveryNoticeJobDetail = new JobDetailFactoryBean();
        recoveryNoticeJobDetail.setGroup("PTIS_RECOVERY_NOTICE_JOB_GROUP");
        recoveryNoticeJobDetail.setName("PTIS_RECOVERY_NOTICE_JOB");
        recoveryNoticeJobDetail.setDurability(true);
        recoveryNoticeJobDetail.setJobClass(RecoveryNoticesJob.class);
        recoveryNoticeJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = new HashMap<>();
        jobDetailMap.put("jobBeanName", "recoveryNoticesJob");
        jobDetailMap.put("userName", "egovernments");
        jobDetailMap.put("cityDataRequired", "true");
        jobDetailMap.put("moduleName", "ptis");
        recoveryNoticeJobDetail.setJobDataAsMap(jobDetailMap);
        return recoveryNoticeJobDetail;
    }

    @Bean
    public JobDetailFactoryBean demandActivationJobDetail() {
        JobDetailFactoryBean demandActivationJobDetail = new JobDetailFactoryBean();
        demandActivationJobDetail.setGroup("PTIS_JOB_GROUP");
        demandActivationJobDetail.setName("PTIS_DEMAND_ACTIVATION_JOB");
        demandActivationJobDetail.setDurability(true);
        demandActivationJobDetail.setJobClass(DemandActivationJob.class);
        demandActivationJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = new HashMap<>();
        jobDetailMap.put("jobBeanName", "demandActivationJob");
        jobDetailMap.put("userName", "egovernments");
        jobDetailMap.put("cityDataRequired", "true");
        jobDetailMap.put("moduleName", "ptis");
        demandActivationJobDetail.setJobDataAsMap(jobDetailMap);
        return demandActivationJobDetail;
    }

    @Bean
    public CronTriggerFactoryBean demandActivationCronTrigger() {
        CronTriggerFactoryBean demandActivationCron = new CronTriggerFactoryBean();
        demandActivationCron.setJobDetail(demandActivationJobDetail().getObject());
        demandActivationCron.setGroup("PTIS_TRIGGER_GROUP");
        demandActivationCron.setName("PTIS_DEMAND_ACTIVATION_TRIGGER");
        demandActivationCron.setCronExpression("0 15 0 * * ?");
        demandActivationCron.setMisfireInstruction(MISFIRE_INSTRUCTION_DO_NOTHING);
        return demandActivationCron;
    }

    @Bean
    public JobDetailFactoryBean ptisBulkBillGenerationJobDetail0() {
        return createJobDetailFactory(50, 0);
    }

    @Bean
    public JobDetailFactoryBean ptisBulkBillGenerationJobDetail1() {
        return createJobDetailFactory(50, 1);
    }

    @Bean
    public JobDetailFactoryBean ptisBulkBillGenerationJobDetail2() {
        return createJobDetailFactory(50, 2);
    }

    @Bean
    public JobDetailFactoryBean ptisBulkBillGenerationJobDetail3() {
        return createJobDetailFactory(50, 3);
    }

    @Bean
    public JobDetailFactoryBean ptisBulkBillGenerationJobDetail4() {
        return createJobDetailFactory(50, 4);
    }

    @Bean
    public CronTriggerFactoryBean ptisBulkBillGenerationCronTrigger0() {
        return createCronTrigger(ptisBulkBillGenerationJobDetail0(), 0);
    }

    @Bean
    public CronTriggerFactoryBean ptisBulkBillGenerationCronTrigger1() {
        return createCronTrigger(ptisBulkBillGenerationJobDetail1(), 1);
    }

    @Bean
    public CronTriggerFactoryBean ptisBulkBillGenerationCronTrigger2() {
        return createCronTrigger(ptisBulkBillGenerationJobDetail2(), 2);
    }

    @Bean
    public CronTriggerFactoryBean ptisBulkBillGenerationCronTrigger3() {
        return createCronTrigger(ptisBulkBillGenerationJobDetail3(), 3);
    }

    @Bean
    public CronTriggerFactoryBean ptisBulkBillGenerationCronTrigger4() {
        return createCronTrigger(ptisBulkBillGenerationJobDetail4(), 4);
    }

    private JobDetailFactoryBean createJobDetailFactory(int billsCount, int modulo) {
        JobDetailFactoryBean ptisBulkBillGenerationJobDetail = new JobDetailFactoryBean();
        ptisBulkBillGenerationJobDetail.setGroup("PTIS_JOB_GROUP");
        ptisBulkBillGenerationJobDetail.setName("PTIS_BULK_BILL_GEN_" + modulo + "_JOB");
        ptisBulkBillGenerationJobDetail.setDurability(true);
        ptisBulkBillGenerationJobDetail.setJobClass(BulkBillGenerationJob.class);
        ptisBulkBillGenerationJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = new HashMap<>();
        jobDetailMap.put("jobBeanName", "bulkBillGenerationJob" + modulo);
        jobDetailMap.put("modulo", String.valueOf(modulo));
        jobDetailMap.put("billsCount", String.valueOf(billsCount));
        jobDetailMap.put("userName", "egovernments");
        jobDetailMap.put("cityDataRequired", "true");
        jobDetailMap.put("moduleName", "ptis");
        ptisBulkBillGenerationJobDetail.setJobDataAsMap(jobDetailMap);
        return ptisBulkBillGenerationJobDetail;
    }

    private CronTriggerFactoryBean createCronTrigger(JobDetailFactoryBean jobDetail, int modulo) {
        CronTriggerFactoryBean bulkBillGenerationCron = new CronTriggerFactoryBean();
        bulkBillGenerationCron.setJobDetail(jobDetail.getObject());
        bulkBillGenerationCron.setGroup("PTIS_TRIGGER_GROUP");
        bulkBillGenerationCron.setName("PTIS_BULK_BILL_GEN_" + modulo + "_TRIGGER");
        bulkBillGenerationCron.setCronExpression("0 */5 * * * ?");
        bulkBillGenerationCron.setMisfireInstruction(MISFIRE_INSTRUCTION_DO_NOTHING);
        return bulkBillGenerationCron;
    }

}
