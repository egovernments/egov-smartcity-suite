package org.egov.pims.empLeave.client;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobToExEndFyToPopulateLeaveBalences implements Job {

	
	EmpLeaveService empLeaveService = null;
	

	public void generateJob(String cityURL, String jndi, String hibFactName) {
		try {
			empLeaveService.populateLeaveBalences(cityURL, jndi, hibFactName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
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


	public EmpLeaveService getEmpLeaveService() {
		return empLeaveService;
	}

	public void setEmpLeaveService(EmpLeaveService empLeaveService) {
		this.empLeaveService = empLeaveService;
	}


	
	
}