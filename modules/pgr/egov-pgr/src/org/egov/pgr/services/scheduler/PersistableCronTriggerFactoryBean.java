package org.egov.pgr.services.scheduler;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailAwareTrigger;

public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean {

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        //Remove the JobDetail element
        getJobDataMap().remove(JobDetailAwareTrigger.JOB_DETAIL_KEY);
    }
}
