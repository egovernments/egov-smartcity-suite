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

package org.egov.stms.config;

import org.egov.infra.config.scheduling.QuartzSchedulerConfiguration;
import org.egov.infra.config.scheduling.SchedulerConfigCondition;
import org.egov.stms.service.scheduler.jobs.GenerateDemandForSewerageTaxJob;
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
public class StmsSchedulerConfiguration extends QuartzSchedulerConfiguration {

    @Bean(destroyMethod = "destroy")
    public SchedulerFactoryBean stmsScheduler(DataSource dataSource) {
        SchedulerFactoryBean stmsScheduler = createScheduler(dataSource);
        stmsScheduler.setSchedulerName("stms-scheduler");
        stmsScheduler.setAutoStartup(true);
        stmsScheduler.setOverwriteExistingJobs(true);
        stmsScheduler.setTriggers(stmsDemandGenerationCronTrigger().getObject());
        return stmsScheduler;
    }

    @Bean("generateDemandForSewerageTaxJob")
    public GenerateDemandForSewerageTaxJob generateDemandForSewerageTaxJob() {
        return new GenerateDemandForSewerageTaxJob();
    }

    @Bean
    public JobDetailFactoryBean stmsDemandGenerationJobDetail() {
        JobDetailFactoryBean stmsDemandGenerationJobDetail = new JobDetailFactoryBean();
        stmsDemandGenerationJobDetail.setGroup("STMS_JOB_GROUP");
        stmsDemandGenerationJobDetail.setName("STMS_DEMAND_GENERATION_JOB");
        stmsDemandGenerationJobDetail.setDurability(true);
        stmsDemandGenerationJobDetail.setJobClass(GenerateDemandForSewerageTaxJob.class);
        stmsDemandGenerationJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = new HashMap<>();
        jobDetailMap.put("jobBeanName", "generateDemandForSewerageTaxJob");
        jobDetailMap.put("userName", "egovernments");
        jobDetailMap.put("cityDataRequired", "true");
        jobDetailMap.put("moduleName", "stms");
        stmsDemandGenerationJobDetail.setJobDataAsMap(jobDetailMap);
        return stmsDemandGenerationJobDetail;
    }

    @Bean
    public CronTriggerFactoryBean stmsDemandGenerationCronTrigger() {
        CronTriggerFactoryBean demandGenerationCron = new CronTriggerFactoryBean();
        demandGenerationCron.setJobDetail(stmsDemandGenerationJobDetail().getObject());
        demandGenerationCron.setGroup("STMS_TRIGGER_GROUP");
        demandGenerationCron.setName("STMS_DEMAND_GENERATION_TRIGGER");
        demandGenerationCron.setCronExpression("0 */30 * * * ?");
        demandGenerationCron.setMisfireInstruction(MISFIRE_INSTRUCTION_DO_NOTHING);
        return demandGenerationCron;
    }
}