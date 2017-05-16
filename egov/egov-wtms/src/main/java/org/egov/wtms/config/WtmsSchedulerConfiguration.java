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

package org.egov.wtms.config;

import org.egov.infra.config.scheduling.QuartzSchedulerConfiguration;
import org.egov.infra.config.scheduling.SchedulerConfigCondition;
import org.egov.wtms.scheduler.BulkWaterConnBillGenerationJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING;

@Configuration
@Conditional(SchedulerConfigCondition.class)
public class WtmsSchedulerConfiguration extends QuartzSchedulerConfiguration {

    @Bean(destroyMethod = "destroy")
    public SchedulerFactoryBean wtmsScheduler(DataSource dataSource) {
        SchedulerFactoryBean wtmsScheduler = createScheduler(dataSource);
        wtmsScheduler.setSchedulerName("wtms-scheduler");
        wtmsScheduler.setAutoStartup(true);
        wtmsScheduler.setOverwriteExistingJobs(true);
        wtmsScheduler.setTriggers(
                wtmsBulkBillGenerationCronTrigger0().getObject(),
                wtmsBulkBillGenerationCronTrigger1().getObject(),
                wtmsBulkBillGenerationCronTrigger2().getObject(),
                wtmsBulkBillGenerationCronTrigger3().getObject(),
                wtmsBulkBillGenerationCronTrigger4().getObject(),
                wtmsBulkBillGenerationCronTrigger5().getObject(),
                wtmsBulkBillGenerationCronTrigger6().getObject(),
                wtmsBulkBillGenerationCronTrigger7().getObject(),
                wtmsBulkBillGenerationCronTrigger8().getObject(),
                wtmsBulkBillGenerationCronTrigger9().getObject()
        );
        return wtmsScheduler;
    }

    @Bean("bulkWaterConnBillGenerationJob0")
    public BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob0() {
        BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob = new BulkWaterConnBillGenerationJob();
        bulkWaterConnBillGenerationJob.setBillsCount(50);
        bulkWaterConnBillGenerationJob.setModulo(0);
        return bulkWaterConnBillGenerationJob;
    }

    @Bean("bulkWaterConnBillGenerationJob1")
    public BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob1() {
        BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob = new BulkWaterConnBillGenerationJob();
        bulkWaterConnBillGenerationJob.setBillsCount(50);
        bulkWaterConnBillGenerationJob.setModulo(1);
        return bulkWaterConnBillGenerationJob;
    }

    @Bean("bulkWaterConnBillGenerationJob2")
    public BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob2() {
        BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob = new BulkWaterConnBillGenerationJob();
        bulkWaterConnBillGenerationJob.setBillsCount(50);
        bulkWaterConnBillGenerationJob.setModulo(2);
        return bulkWaterConnBillGenerationJob;
    }

    @Bean("bulkWaterConnBillGenerationJob3")
    public BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob3() {
        BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob = new BulkWaterConnBillGenerationJob();
        bulkWaterConnBillGenerationJob.setBillsCount(50);
        bulkWaterConnBillGenerationJob.setModulo(3);
        return bulkWaterConnBillGenerationJob;
    }

    @Bean("bulkWaterConnBillGenerationJob4")
    public BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob4() {
        BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob = new BulkWaterConnBillGenerationJob();
        bulkWaterConnBillGenerationJob.setBillsCount(50);
        bulkWaterConnBillGenerationJob.setModulo(4);
        return bulkWaterConnBillGenerationJob;
    }

    @Bean("bulkWaterConnBillGenerationJob5")
    public BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob5() {
        BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob = new BulkWaterConnBillGenerationJob();
        bulkWaterConnBillGenerationJob.setBillsCount(50);
        bulkWaterConnBillGenerationJob.setModulo(5);
        return bulkWaterConnBillGenerationJob;
    }

    @Bean("bulkWaterConnBillGenerationJob6")
    public BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob6() {
        BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob = new BulkWaterConnBillGenerationJob();
        bulkWaterConnBillGenerationJob.setBillsCount(50);
        bulkWaterConnBillGenerationJob.setModulo(6);
        return bulkWaterConnBillGenerationJob;
    }

    @Bean("bulkWaterConnBillGenerationJob7")
    public BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob7() {
        BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob = new BulkWaterConnBillGenerationJob();
        bulkWaterConnBillGenerationJob.setBillsCount(50);
        bulkWaterConnBillGenerationJob.setModulo(7);
        return bulkWaterConnBillGenerationJob;
    }

    @Bean("bulkWaterConnBillGenerationJob8")
    public BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob8() {
        BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob = new BulkWaterConnBillGenerationJob();
        bulkWaterConnBillGenerationJob.setBillsCount(50);
        bulkWaterConnBillGenerationJob.setModulo(8);
        return bulkWaterConnBillGenerationJob;
    }

    @Bean("bulkWaterConnBillGenerationJob9")
    public BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob9() {
        BulkWaterConnBillGenerationJob bulkWaterConnBillGenerationJob = new BulkWaterConnBillGenerationJob();
        bulkWaterConnBillGenerationJob.setBillsCount(50);
        bulkWaterConnBillGenerationJob.setModulo(9);
        return bulkWaterConnBillGenerationJob;
    }

    @Bean
    public JobDetailFactoryBean wtmsBulkBillGenerationJobDetail0() {
        return createJobDetailFactory(0);
    }

    @Bean
    public JobDetailFactoryBean wtmsBulkBillGenerationJobDetail1() {
        return createJobDetailFactory(1);
    }

    @Bean
    public JobDetailFactoryBean wtmsBulkBillGenerationJobDetail2() {
        return createJobDetailFactory(2);
    }

    @Bean
    public JobDetailFactoryBean wtmsBulkBillGenerationJobDetail3() {
        return createJobDetailFactory(3);
    }

    @Bean
    public JobDetailFactoryBean wtmsBulkBillGenerationJobDetail4() {
        return createJobDetailFactory(4);
    }

    @Bean
    public JobDetailFactoryBean wtmsBulkBillGenerationJobDetail5() {
        return createJobDetailFactory(5);
    }

    @Bean
    public JobDetailFactoryBean wtmsBulkBillGenerationJobDetail6() {
        return createJobDetailFactory(6);
    }

    @Bean
    public JobDetailFactoryBean wtmsBulkBillGenerationJobDetail7() {
        return createJobDetailFactory(7);
    }

    @Bean
    public JobDetailFactoryBean wtmsBulkBillGenerationJobDetail8() {
        return createJobDetailFactory(8);
    }

    @Bean
    public JobDetailFactoryBean wtmsBulkBillGenerationJobDetail9() {
        return createJobDetailFactory(9);
    }

    @Bean
    public CronTriggerFactoryBean wtmsBulkBillGenerationCronTrigger0() {
        return createCronTrigger(wtmsBulkBillGenerationJobDetail0(), 0);
    }

    @Bean
    public CronTriggerFactoryBean wtmsBulkBillGenerationCronTrigger1() {
        return createCronTrigger(wtmsBulkBillGenerationJobDetail1(), 1);
    }

    @Bean
    public CronTriggerFactoryBean wtmsBulkBillGenerationCronTrigger2() {
        return createCronTrigger(wtmsBulkBillGenerationJobDetail2(), 2);
    }

    @Bean
    public CronTriggerFactoryBean wtmsBulkBillGenerationCronTrigger3() {
        return createCronTrigger(wtmsBulkBillGenerationJobDetail3(), 3);
    }

    @Bean
    public CronTriggerFactoryBean wtmsBulkBillGenerationCronTrigger4() {
        return createCronTrigger(wtmsBulkBillGenerationJobDetail4(), 4);
    }

    @Bean
    public CronTriggerFactoryBean wtmsBulkBillGenerationCronTrigger5() {
        return createCronTrigger(wtmsBulkBillGenerationJobDetail5(), 5);
    }

    @Bean
    public CronTriggerFactoryBean wtmsBulkBillGenerationCronTrigger6() {
        return createCronTrigger(wtmsBulkBillGenerationJobDetail6(), 6);
    }

    @Bean
    public CronTriggerFactoryBean wtmsBulkBillGenerationCronTrigger7() {
        return createCronTrigger(wtmsBulkBillGenerationJobDetail7(), 7);
    }

    @Bean
    public CronTriggerFactoryBean wtmsBulkBillGenerationCronTrigger8() {
        return createCronTrigger(wtmsBulkBillGenerationJobDetail8(), 8);
    }

    @Bean
    public CronTriggerFactoryBean wtmsBulkBillGenerationCronTrigger9() {
        return createCronTrigger(wtmsBulkBillGenerationJobDetail9(), 9);
    }

    private JobDetailFactoryBean createJobDetailFactory(int modulo) {
        JobDetailFactoryBean wtmsBulkBillGenerationJobDetail = new JobDetailFactoryBean();
        wtmsBulkBillGenerationJobDetail.setGroup("WTMS_JOB_GROUP");
        wtmsBulkBillGenerationJobDetail.setName("WTMS_BULK_BILL_GEN_" + modulo + "_JOB");
        wtmsBulkBillGenerationJobDetail.setDurability(true);
        wtmsBulkBillGenerationJobDetail.setJobClass(BulkWaterConnBillGenerationJob.class);
        wtmsBulkBillGenerationJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = new HashMap<>();
        jobDetailMap.put("jobBeanName", "bulkWaterConnBillGenerationJob" + modulo);
        jobDetailMap.put("userName", "egovernments");
        jobDetailMap.put("cityDataRequired", "true");
        jobDetailMap.put("moduleName", "wtms");
        wtmsBulkBillGenerationJobDetail.setJobDataAsMap(jobDetailMap);
        return wtmsBulkBillGenerationJobDetail;
    }

    private CronTriggerFactoryBean createCronTrigger(JobDetailFactoryBean jobDetail, int modulo) {
        CronTriggerFactoryBean bulkBillGenerationCron = new CronTriggerFactoryBean();
        bulkBillGenerationCron.setJobDetail(jobDetail.getObject());
        bulkBillGenerationCron.setGroup("WTMS_TRIGGER_GROUP");
        bulkBillGenerationCron.setName("WTMS_BULK_BILL_GEN_" + modulo + "_TRIGGER");
        bulkBillGenerationCron.setCronExpression("0 30 17 * * ? 2050");
        bulkBillGenerationCron.setMisfireInstruction(MISFIRE_INSTRUCTION_DO_NOTHING);
        return bulkBillGenerationCron;
    }

}
