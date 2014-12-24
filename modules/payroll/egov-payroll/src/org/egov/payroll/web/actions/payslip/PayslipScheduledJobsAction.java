package org.egov.payroll.web.actions.payslip;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.displaytag.pagination.PaginatedList;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.model.BatchGenDetails;
import org.egov.payroll.services.reports.PayrollReportService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.SearchFormAction;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * @author jagadeesan
 *
 */
@ParentPackage("egov")
public class PayslipScheduledJobsAction extends SearchFormAction {

	private static final Logger logger = Logger.getLogger(PayslipScheduledJobsAction.class);
	private static final String VIEW = "view";
	private Scheduler scheduler;
	private String[] deleteJobGroupName=new String[0];
	private String[] deleteJobName=new String[0];
	private static final long serialVersionUID = 1L;
	private PersistenceService<BatchGenDetails, Long> batchGenDtlService;
	private static final SimpleDateFormat stf = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss");
	private BatchGenDetails batchGenDtl= new BatchGenDetails();

	//Values will get set by request paramter when click the job detail link
	private String jobName;
	private String jobGroupName;
	private String jobStatus;

	private PaginatedList schJobs;
	@Override
	public Object getModel() {
		return null;
	}
	
	public void prepare()
	{
		super.prepare(); 
	}
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		/*List<Object> params=new ArrayList<Object>();
		params.add(BigDecimal.valueOf(month));
		params.add(Long.valueOf(year));*/
		String countQuery=getCountQuery();
		SearchQuery searchQuery=new SearchQuerySQL(getSearchQuery(),getCountQuery(),null);   
		return searchQuery;
	}
	@Override
	public String search()  
	{
		super.search(); 
		setSchJobs(searchResult);
		return NEW;
	}
	@SkipValidation
	public String beforeShowScheduledJobs()
	{
		return search();
	}
	
	/*@SkipValidation
	public String availScheduledJobs()
	{
		try {
			Scheduler sched = getScheduler();
			String[] jobGrpNames = sched.getJobGroupNames();
			logger.info("jobgroupnames "+jobGrpNames.length);
			for(int i=0;i<jobGrpNames.length;i++)
			{
				logger.warn("jobgroup names ===="+jobGrpNames[i]);
				
				if(jobGrpNames[i].contains("Payslip Group Job")){
					
					String[] jobNames = sched.getJobNames(jobGrpNames[i]);
					logger.info(jobGrpNames[i]+"/"+jobNames.length);
					
					for(int k=0;k<jobNames.length;k++){
						
						Map listMap = new LinkedHashMap();
						
						listMap.put("rowCount",k+1);
						listMap.put("jobGroupName", jobGrpNames[i]);
						listMap.put("jobName", jobNames[k]);
						
						Trigger[] trigger = sched.getTriggersOfJob(jobNames[k],jobGrpNames[i]);
						boolean isTriggerExist=false;
						
						if(trigger.length>0)
						{
							isTriggerExist=true;
							int triggerStateId = sched.getTriggerState(jobNames[k],jobGrpNames[i]);
							Date startDate = new Date(trigger[0].getStartTime().getTime());
							listMap.put("startDate",stf.format(startDate));
							
							if(trigger[0].getNextFireTime()!=null){
								Date nextFireDate = new Date(trigger[0].getNextFireTime().getTime());
								listMap.put("nextFireDate",stf.format(nextFireDate));
							}
							else
							{
								listMap.put("nextFireDate","");
							}
						}

						if(jobGrpNames[i].contains("Payslip Group Job")){
						//To set department and functionary
							BatchGenDetails batchgenobj = (BatchGenDetails)batchGenDtlService.find("from BatchGenDetails  where schJobGroupName=? and schJobName=?",jobGrpNames[i], jobNames[k]);
							if(batchgenobj!=null){
								
								if(batchgenobj.getStatus()==1){
									listMap.put("status","SCHEDULED");
								}else if(batchgenobj.getStatus()==2 || batchgenobj.getStatus()==3){
									listMap.put("status","COMPLETED");
								}else if(batchgenobj.getStatus()==5){
									if(isTriggerExist){
										listMap.put("status","RUNNING");
									}else{
										listMap.put("status","FAILED");
									}
								}
							}
						}
						logger.warn("job names ===="+jobNames[k]);
						scheduledJobsList.add(listMap);
					}
				}
			}

			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NEW;
	}*/
	
	/*@SkipValidation
	public String deleteScheduledJobs() throws Exception
	{
		try{
			for(int i=0;i<deleteJobGroupName.length;i++)
			{
				Scheduler sched = getScheduler();
				//sched.unscheduleJob(triggerName,triggerGroup);

				if(!deleteJobGroupName[i].equals("") && !deleteJobName[i].equals(""))
				{
					if(sched.deleteJob(deleteJobName[i],deleteJobGroupName[i]))
					{
						//To delete the batch gen details
						BatchGenDetails batchgenobj = (BatchGenDetails)batchGenDtlService.find("from BatchGenDetails  where schJobGroupName=? and schJobName=?",deleteJobGroupName[i], deleteJobName[i]);
						if(batchgenobj!=null)
						{
							batchgenobj.setStatus(PayrollConstants.BATCH_GEN_STATUS_JOBDELETED);
											
						}
					}
				}

			}
		}catch(SchedulerException se){
			logger.error("Exception while delete scheduled jobs in payslipscheduledjobs =="+se.getMessage());
		}
		addActionMessage("Jobs Deleted Successfully");
		String returnVal= search();
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.AUTO);//since in the searchform action flushmode manual ,update is not hapenings
		return returnVal;
	}*/
	
	@SkipValidation
	public String showJobDetails()
	{
		if(jobGroupName.contains("Payslip Group Job")){
			//To set all the details
			batchGenDtl = (BatchGenDetails)batchGenDtlService.find("from BatchGenDetails  where schJobGroupName=? and schJobName=?",jobGroupName, jobName);
		}
		return VIEW;
	}
	public String[] getDeleteJobGroupName() {
		return deleteJobGroupName;
	}

	public void setDeleteJobGroupName(String[] deleteJobGroupName) {
		this.deleteJobGroupName = deleteJobGroupName;
	}

	public String[] getDeleteJobName() {
		return deleteJobName;
	}

	public void setDeleteJobName(String[] deleteJobName) {
		this.deleteJobName = deleteJobName;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroupName() {
		return jobGroupName;
	}

	public void setJobGroupName(String jobGroupName) {
		this.jobGroupName = jobGroupName;
	}
	
	public BatchGenDetails getBatchGenDtl() {
		return batchGenDtl;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public void setBatchGenDtlService(
			PersistenceService<BatchGenDetails, Long> batchGenDtlService) {
		this.batchGenDtlService = batchGenDtlService;
	}
private String getCountQuery()
{	
	return "select count(batchgen.sch_job_name) "+
	"from egpay_batchgendetails batchgen where batchgen.sch_job_group_name like 'Payslip Group Job%'";

}
private String getSearchQuery()
{	
	return "select batchgen.sch_job_name as jobName,batchgen.sch_job_group_name as jobGroupName,"+
	"TO_CHAR((decode(batchgen.status,1,batchgen.createddate,5,batchgen.createddate)),'DD/MM/YYYY HH24:MI:SS') as starTtime, "+
	"decode(batchgen.status,1,TO_CHAR((batchgen.createddate+(2 * (1/24/60))),'DD/MM/YYYY HH24:MI:SS'),5,null) as nextFireTime, "+
	"decode(batchgen.status,1,'SCHEDULED',"+
	"2,'COMPLETED',"+
	"3,'COMPLETED',"+
	"5,'RUNNING',"+
	"'FAILED') AS status "+
	"from egpay_batchgendetails batchgen "+
	"where batchgen.sch_job_group_name like 'Payslip Group Job%' order by batchgen.modifieddate desc";

}

public PaginatedList getSchJobs() {
	return schJobs;
}

public void setSchJobs(PaginatedList schJobs) {
	this.schJobs = schJobs;
}

}