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

package org.egov.collection.config;

import static org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.egov.collection.scheduler.AtomReconciliationJob;
import org.egov.collection.scheduler.AxisReconciliationJob;
import org.egov.collection.scheduler.RemittanceInstrumentJob;
import org.egov.collection.scheduler.SbimopsReconciliationJob;
import org.egov.infra.config.scheduling.QuartzSchedulerConfiguration;
import org.egov.infra.config.scheduling.SchedulerConfigCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@Conditional(SchedulerConfigCondition.class)
public class CollectionSchedulerConfiguration extends QuartzSchedulerConfiguration {

    private static final String INSTRUMENT_TYPE_CASH = "cash";
    private static final String COLLECTION_JOB_GROUP = "COLLECTION_JOB_GROUP";
    private static final String COLLECTION_TRIGGER_GROUP = "COLLECTION_TRIGGER_GROUP";

    @Bean(destroyMethod = "destroy")
    public SchedulerFactoryBean collectionScheduler(DataSource dataSource) {
        SchedulerFactoryBean collectionScheduler = createScheduler(dataSource);
        collectionScheduler.setSchedulerName("collection-scheduler");
        collectionScheduler.setAutoStartup(true);
        collectionScheduler.setOverwriteExistingJobs(true);
        collectionScheduler.setTriggers(
                axisReconciliationCronTrigger().getObject(),
                remittanceCashInstrumentCronTrigger0().getObject(),
                remittanceCashInstrumentCronTrigger1().getObject(),
                sbimopsReconciliationCronTrigger0().getObject(),
                sbimopsReconciliationCronTrigger1().getObject(),
                sbimopsReconciliationCronTrigger2().getObject(),
                sbimopsReconciliationCronTrigger3().getObject(),
                sbimopsReconciliationCronTrigger4().getObject());
        return collectionScheduler;
    }

    private Map<String, String> prepareJobDetailMap() {
        Map<String, String> jobDetailMap = new HashMap<>();
        jobDetailMap.put("userName", "system");
        jobDetailMap.put("cityDataRequired", "true");
        jobDetailMap.put("moduleName", "collection");
        return jobDetailMap;
    }

    @Bean
    public JobDetailFactoryBean axisReconciliationJobDetail() {
        JobDetailFactoryBean axisReconciliationJobDetail = new JobDetailFactoryBean();
        axisReconciliationJobDetail.setGroup(COLLECTION_JOB_GROUP);
        axisReconciliationJobDetail.setName("COLLECTION_AXIS_RECON_JOB");
        axisReconciliationJobDetail.setDurability(true);
        axisReconciliationJobDetail.setJobClass(AxisReconciliationJob.class);
        axisReconciliationJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = prepareJobDetailMap();
        jobDetailMap.put("jobBeanName", "axisReconciliationJob");
        axisReconciliationJobDetail.setJobDataAsMap(jobDetailMap);
        return axisReconciliationJobDetail;
    }

    @Bean
    public CronTriggerFactoryBean axisReconciliationCronTrigger() {
        CronTriggerFactoryBean axisReconciliationCron = new CronTriggerFactoryBean();
        axisReconciliationCron.setJobDetail(axisReconciliationJobDetail().getObject());
        axisReconciliationCron.setGroup(COLLECTION_TRIGGER_GROUP);
        axisReconciliationCron.setName("COLLECTION_AXIS_RECON_TRIGGER");
        axisReconciliationCron.setCronExpression("0 */30 * * * ?");
        axisReconciliationCron.setMisfireInstruction(MISFIRE_INSTRUCTION_DO_NOTHING);
        return axisReconciliationCron;
    }

    @Bean("axisReconciliationJob")
    public AxisReconciliationJob axisReconciliationJob() {
        return new AxisReconciliationJob();
    }

    @Bean
    public JobDetailFactoryBean remittanceCashInstrumentJobDetail0() {
        return createJobDetailFactory(INSTRUMENT_TYPE_CASH, 0);
    }

    @Bean
    public JobDetailFactoryBean remittanceCashInstrumentJobDetail1() {
        return createJobDetailFactory(INSTRUMENT_TYPE_CASH, 1);
    }

    @Bean
    public CronTriggerFactoryBean remittanceCashInstrumentCronTrigger0() {
        return createCronTrigger(remittanceCashInstrumentJobDetail0(), INSTRUMENT_TYPE_CASH, 0);
    }

    @Bean
    public CronTriggerFactoryBean remittanceCashInstrumentCronTrigger1() {
        return createCronTrigger(remittanceCashInstrumentJobDetail1(), INSTRUMENT_TYPE_CASH, 1);
    }

    @Bean("remittancecashInstrumentJob0")
    public RemittanceInstrumentJob remittancecashInstrumentJob0() {
        RemittanceInstrumentJob remittanceInstrumentJob = new RemittanceInstrumentJob();
        remittanceInstrumentJob.setModulo(0);
        remittanceInstrumentJob.setInstrumentType(INSTRUMENT_TYPE_CASH);
        return remittanceInstrumentJob;

    }

    @Bean("remittancecashInstrumentJob1")
    public RemittanceInstrumentJob remittancecashInstrumentJob1() {
        RemittanceInstrumentJob remittanceInstrumentJob = new RemittanceInstrumentJob();
        remittanceInstrumentJob.setModulo(1);
        remittanceInstrumentJob.setInstrumentType(INSTRUMENT_TYPE_CASH);
        return remittanceInstrumentJob;

    }

    private JobDetailFactoryBean createJobDetailFactory(String instrumentType, int modulo) {
        JobDetailFactoryBean remittanceInstrumentJobDetail = new JobDetailFactoryBean();
        remittanceInstrumentJobDetail.setGroup(COLLECTION_JOB_GROUP);
        remittanceInstrumentJobDetail.setName(String.format("COLLECTION_REMIT_INSTRMNT_%s%d_JOB", instrumentType, modulo));
        remittanceInstrumentJobDetail.setDurability(true);
        remittanceInstrumentJobDetail.setJobClass(RemittanceInstrumentJob.class);
        remittanceInstrumentJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = prepareJobDetailMap();
        jobDetailMap.put("jobBeanName", String.format("remittance%sInstrumentJob%d", instrumentType, modulo));
        remittanceInstrumentJobDetail.setJobDataAsMap(jobDetailMap);
        return remittanceInstrumentJobDetail;
    }

    private CronTriggerFactoryBean createCronTrigger(JobDetailFactoryBean jobDetail, String instrumentType, int modulo) {
        CronTriggerFactoryBean remittanceCron = new CronTriggerFactoryBean();
        remittanceCron.setJobDetail(jobDetail.getObject());
        remittanceCron.setGroup(COLLECTION_TRIGGER_GROUP);
        remittanceCron.setName(String.format("COLLECTION_REMIT_INSTRMNT_%s%d_TRIGGER", instrumentType, modulo));
        remittanceCron.setCronExpression("0 */30 * * * ?");
        remittanceCron.setMisfireInstruction(MISFIRE_INSTRUCTION_DO_NOTHING);
        return remittanceCron;
    }

    public JobDetailFactoryBean createAtomJobDetailFactory(int modulo) {
        JobDetailFactoryBean atomReconciliationJobDetail = new JobDetailFactoryBean();
        atomReconciliationJobDetail.setGroup(COLLECTION_JOB_GROUP);
        atomReconciliationJobDetail.setName(String.format("COLLECTION_ATOM_RECON_%d_JOB", modulo));
        atomReconciliationJobDetail.setDurability(true);
        atomReconciliationJobDetail.setJobClass(AtomReconciliationJob.class);
        atomReconciliationJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = prepareJobDetailMap();
        jobDetailMap.put("jobBeanName", String.format("atomReconciliationJob%d", modulo));
        atomReconciliationJobDetail.setJobDataAsMap(jobDetailMap);
        return atomReconciliationJobDetail;
    }

    public CronTriggerFactoryBean createAtomCronTrigger(JobDetailFactoryBean jobDetail, int modulo) {
        CronTriggerFactoryBean atomReconciliationCron = new CronTriggerFactoryBean();
        atomReconciliationCron.setJobDetail(jobDetail.getObject());
        atomReconciliationCron.setGroup(COLLECTION_TRIGGER_GROUP);
        atomReconciliationCron.setName(String.format("COLLECTION_ATOM_RECON_%d_TRIGGER", modulo));
        atomReconciliationCron.setCronExpression("0 */30 * * * ?");
        atomReconciliationCron.setMisfireInstruction(MISFIRE_INSTRUCTION_DO_NOTHING);
        return atomReconciliationCron;
    }

    @Bean
    public JobDetailFactoryBean atomReconciliationJobDetail0() {
        return createAtomJobDetailFactory(0);
    }

    @Bean
    public CronTriggerFactoryBean atomReconciliationCronTrigger0() {
        return createAtomCronTrigger(atomReconciliationJobDetail0(), 0);
    }

    @Bean("atomReconciliationJob0")
    public AtomReconciliationJob atomReconciliationJob0() {
        AtomReconciliationJob atomReconciliationJob = new AtomReconciliationJob();
        atomReconciliationJob.setModulo(0);
        return atomReconciliationJob;
    }

    @Bean
    public JobDetailFactoryBean atomReconciliationJobDetail1() {
        return createAtomJobDetailFactory(1);
    }

    @Bean
    public CronTriggerFactoryBean atomReconciliationCronTrigger1() {
        return createAtomCronTrigger(atomReconciliationJobDetail1(), 1);
    }

    @Bean("atomReconciliationJob1")
    public AtomReconciliationJob atomReconciliationJob1() {
        AtomReconciliationJob atomReconciliationJob = new AtomReconciliationJob();
        atomReconciliationJob.setModulo(1);
        return atomReconciliationJob;
    }

    @Bean
    public JobDetailFactoryBean atomReconciliationJobDetail2() {
        return createAtomJobDetailFactory(2);
    }

    @Bean
    public CronTriggerFactoryBean atomReconciliationCronTrigger2() {
        return createAtomCronTrigger(atomReconciliationJobDetail2(), 2);
    }

    @Bean("atomReconciliationJob2")
    public AtomReconciliationJob atomReconciliationJob2() {
        AtomReconciliationJob atomReconciliationJob = new AtomReconciliationJob();
        atomReconciliationJob.setModulo(2);
        return atomReconciliationJob;
    }

    @Bean
    public JobDetailFactoryBean atomReconciliationJobDetail3() {
        return createAtomJobDetailFactory(3);
    }

    @Bean
    public CronTriggerFactoryBean atomReconciliationCronTrigger3() {
        return createAtomCronTrigger(atomReconciliationJobDetail3(), 3);
    }

    @Bean("atomReconciliationJob3")
    public AtomReconciliationJob atomReconciliationJob3() {
        AtomReconciliationJob atomReconciliationJob = new AtomReconciliationJob();
        atomReconciliationJob.setModulo(3);
        return atomReconciliationJob;
    }

    @Bean
    public JobDetailFactoryBean atomReconciliationJobDetail4() {
        return createAtomJobDetailFactory(4);
    }

    @Bean
    public CronTriggerFactoryBean atomReconciliationCronTrigger4() {
        return createAtomCronTrigger(atomReconciliationJobDetail4(), 4);
    }

    @Bean("atomReconciliationJob4")
    public AtomReconciliationJob atomReconciliationJob4() {
        AtomReconciliationJob atomReconciliationJob = new AtomReconciliationJob();
        atomReconciliationJob.setModulo(4);
        return atomReconciliationJob;
    }

    public JobDetailFactoryBean createSbimopsJobDetailFactory(int modulo) {
        JobDetailFactoryBean sbimopsReconciliationJobDetail = new JobDetailFactoryBean();
        sbimopsReconciliationJobDetail.setGroup(COLLECTION_JOB_GROUP);
        sbimopsReconciliationJobDetail.setName(String.format("COLLECTION_SBIMOPS_RECON_%d_JOB", modulo));
        sbimopsReconciliationJobDetail.setDurability(true);
        sbimopsReconciliationJobDetail.setJobClass(SbimopsReconciliationJob.class);
        sbimopsReconciliationJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = prepareJobDetailMap();
        jobDetailMap.put("jobBeanName", String.format("sbimopsReconciliationJob%d", modulo));
        sbimopsReconciliationJobDetail.setJobDataAsMap(jobDetailMap);
        return sbimopsReconciliationJobDetail;
    }

    public CronTriggerFactoryBean createSbimopsCronTrigger(JobDetailFactoryBean jobDetail, int modulo) {
        CronTriggerFactoryBean sbimopsReconciliationCron = new CronTriggerFactoryBean();
        sbimopsReconciliationCron.setJobDetail(jobDetail.getObject());
        sbimopsReconciliationCron.setGroup(COLLECTION_TRIGGER_GROUP);
        sbimopsReconciliationCron.setName(String.format("COLLECTION_SBIMOPS_RECON_%d_TRIGGER", modulo));
        sbimopsReconciliationCron.setCronExpression("0 */45 * * * ?");
        sbimopsReconciliationCron.setMisfireInstruction(MISFIRE_INSTRUCTION_DO_NOTHING);
        return sbimopsReconciliationCron;
    }

    @Bean
    public JobDetailFactoryBean sbimopsReconciliationJobDetail0() {
        return createSbimopsJobDetailFactory(0);
    }

    @Bean
    public CronTriggerFactoryBean sbimopsReconciliationCronTrigger0() {
        return createSbimopsCronTrigger(sbimopsReconciliationJobDetail0(), 0);
    }

    @Bean("sbimopsReconciliationJob0")
    public SbimopsReconciliationJob sbimopsReconciliationJob0() {
        SbimopsReconciliationJob sbimopsReconciliationJob = new SbimopsReconciliationJob();
        sbimopsReconciliationJob.setModulo(0);
        return sbimopsReconciliationJob;
    }

    @Bean
    public JobDetailFactoryBean sbimopsReconciliationJobDetail1() {
        return createSbimopsJobDetailFactory(1);
    }

    @Bean
    public CronTriggerFactoryBean sbimopsReconciliationCronTrigger1() {
        return createSbimopsCronTrigger(sbimopsReconciliationJobDetail1(), 1);
    }

    @Bean("sbimopsReconciliationJob1")
    public SbimopsReconciliationJob sbimopsReconciliationJob1() {
        SbimopsReconciliationJob sbimopsReconciliationJob = new SbimopsReconciliationJob();
        sbimopsReconciliationJob.setModulo(1);
        return sbimopsReconciliationJob;
    }

    @Bean
    public JobDetailFactoryBean sbimopsReconciliationJobDetail2() {
        return createSbimopsJobDetailFactory(2);
    }

    @Bean
    public CronTriggerFactoryBean sbimopsReconciliationCronTrigger2() {
        return createSbimopsCronTrigger(sbimopsReconciliationJobDetail2(), 2);
    }

    @Bean("sbimopsReconciliationJob2")
    public SbimopsReconciliationJob sbimopsReconciliationJob2() {
        SbimopsReconciliationJob sbimopsReconciliationJob = new SbimopsReconciliationJob();
        sbimopsReconciliationJob.setModulo(2);
        return sbimopsReconciliationJob;
    }

    @Bean
    public JobDetailFactoryBean sbimopsReconciliationJobDetail3() {
        return createSbimopsJobDetailFactory(3);
    }

    @Bean
    public CronTriggerFactoryBean sbimopsReconciliationCronTrigger3() {
        return createSbimopsCronTrigger(sbimopsReconciliationJobDetail3(), 3);
    }

    @Bean("sbimopsReconciliationJob3")
    public SbimopsReconciliationJob sbimopsReconciliationJob3() {
        SbimopsReconciliationJob sbimopsReconciliationJob = new SbimopsReconciliationJob();
        sbimopsReconciliationJob.setModulo(3);
        return sbimopsReconciliationJob;
    }

    @Bean
    public JobDetailFactoryBean sbimopsReconciliationJobDetail4() {
        return createSbimopsJobDetailFactory(4);
    }

    @Bean
    public CronTriggerFactoryBean sbimopsReconciliationCronTrigger4() {
        return createSbimopsCronTrigger(sbimopsReconciliationJobDetail4(), 4);
    }

    @Bean("sbimopsReconciliationJob4")
    public SbimopsReconciliationJob sbimopsReconciliationJob4() {
        SbimopsReconciliationJob sbimopsReconciliationJob = new SbimopsReconciliationJob();
        sbimopsReconciliationJob.setModulo(4);
        return sbimopsReconciliationJob;
    }

}
