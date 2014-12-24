package org.egov.pims.empLeave.client;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobToExecuteAllRejectedLeaves implements Job {

	
	EmpLeaveService empLeaveService = null;


	public JobToExecuteAllRejectedLeaves() {


	}

	public void generateJob(String cityURL, String jndi, String hibFactName) {
		try {
			empLeaveService.RejectLeaves(cityURL, jndi, hibFactName);
		} catch (Exception e) {
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			
			Object[] jdArgs = (Object[]) arg0.getJobDetail().getJobDataMap().get("args");
			String CityURL = (String) jdArgs[0];
			String jndi = (String) jdArgs[1];
			String hibFactName = (String) jdArgs[2];
			generateJob(CityURL,jndi,hibFactName);
		} catch (RuntimeException e) {
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
	}
	private final static String STR_EXCEPTION = "Exception:";
}