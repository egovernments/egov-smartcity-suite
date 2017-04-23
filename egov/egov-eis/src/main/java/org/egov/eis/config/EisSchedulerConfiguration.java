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

package org.egov.eis.config;

import org.egov.eis.service.scheduler.jobs.UserRoleMappingJob;
import org.egov.infra.config.scheduling.QuartzSchedulerConfiguration;
import org.egov.infra.config.scheduling.SchedulerConfigCondition;
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
public class EisSchedulerConfiguration extends QuartzSchedulerConfiguration {

    @Bean(destroyMethod = "destroy")
    public SchedulerFactoryBean eisScheduler(DataSource dataSource) {
        SchedulerFactoryBean eisScheduler = createSchedular(dataSource);
        eisScheduler.setSchedulerName("eis-scheduler");
        eisScheduler.setAutoStartup(true);
        eisScheduler.setOverwriteExistingJobs(true);
        eisScheduler.setTriggers(userRoleMappingCronTrigger().getObject());
        return eisScheduler;
    }

    @Bean("userRoleMappingJob")
    public UserRoleMappingJob userRoleMappingJob() {
        return new UserRoleMappingJob();
    }

    @Bean
    public JobDetailFactoryBean userRoleMappingJobDetail() {
        JobDetailFactoryBean userRoleMappingJobDetail = new JobDetailFactoryBean();
        userRoleMappingJobDetail.setGroup("EIS_JOB_GROUP");
        userRoleMappingJobDetail.setName("EIS_USER_ROLE_MAPPING_JOB");
        userRoleMappingJobDetail.setDurability(true);
        userRoleMappingJobDetail.setJobClass(UserRoleMappingJob.class);
        userRoleMappingJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = new HashMap<>();
        jobDetailMap.put("jobBeanName", "userRoleMappingJob");
        jobDetailMap.put("userName", "egovernments");
        jobDetailMap.put("cityDataRequired", "false");
        jobDetailMap.put("moduleName", "eis");
        userRoleMappingJobDetail.setJobDataAsMap(jobDetailMap);
        return userRoleMappingJobDetail;
    }

    @Bean
    public CronTriggerFactoryBean userRoleMappingCronTrigger() {
        CronTriggerFactoryBean userRoleMappingCron = new CronTriggerFactoryBean();
        userRoleMappingCron.setJobDetail(userRoleMappingJobDetail().getObject());
        userRoleMappingCron.setGroup("EIS_TRIGGER_GROUP");
        userRoleMappingCron.setName("EIS_USER_ROLE_MAPPING_TRIGGER");
        userRoleMappingCron.setCronExpression("0 0 3 * * ?");
        userRoleMappingCron.setMisfireInstruction(MISFIRE_INSTRUCTION_DO_NOTHING);
        return userRoleMappingCron;
    }
}
