package com.exilant.exility.dataservice;

import com.exilant.exility.common.*;

import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

public class JobService {
	private static final Logger LOGGER = Logger.getLogger(JobService.class);
	static JobService singletonInstance;
	//Job decared as Type help to XML loader to load jobs into job
	protected Job job;
	public  HashMap jobs;
	
	public static JobService getInstance(){
		if (singletonInstance == null)singletonInstance = new JobService();
		return singletonInstance;
	}
	
// singleton pattern 	
	private JobService(){
		super();
		XMLLoader xmlLoader = new XMLLoader();
		URL url = EGovConfig.class.getClassLoader().getResource("config/resource/Jobs.xml");
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("url in JobService=================="+url);
		xmlLoader.load(url.toString(),this);
	}
	public void doService(DataCollection dc, Connection con)throws TaskFailedException{
		String serviceID = dc.getValue("serviceID");
		if (serviceID == null){
			dc.addMessage("exilNoServiceID",serviceID);
		}else{
			Job aJob = (Job)this.jobs.get(serviceID);//Get based on the Id from the hashmap.
			/*if(aJob.previliged(dc.getValue("rolesToValidate"))){
				dc.addMessage("exilRPError","User Has No Previlige");
				throw new TaskFailedException();
			}*/
			if(LOGGER.isDebugEnabled())     LOGGER.debug(aJob);
			if (aJob == null){
				dc.addMessage("exilNoJobDefinition",serviceID);
			}else{
				if (aJob.hasAccess(dc,con))
					aJob.execute(dc, con);
				else
					dc.addMessage("exilNoAccess",serviceID);
			}
		}
	}
}
