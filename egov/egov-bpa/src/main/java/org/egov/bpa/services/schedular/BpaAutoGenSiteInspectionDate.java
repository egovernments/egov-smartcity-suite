/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.services.schedular;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ognl.Ognl;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.Inspection;
import org.egov.bpa.models.Registration;
import org.egov.bpa.services.common.BpaCommonService;
import org.egov.bpa.services.inspection.InspectionService;
import org.egov.bpa.services.register.RegisterBpaService;
import org.egov.commons.EgwStatus;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.utils.DateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.ActionContext;


/**
 * 
 * @author Pradeep Kumar C M
 *
 */
@SuppressWarnings("unchecked")
public class BpaAutoGenSiteInspectionDate  extends AbstractQuartzJob{
	
	private RegisterBpaService registerBpaService;
	private static final Logger LOGGER    = Logger.getLogger(BpaAutoGenSiteInspectionDate.class);
	private BpaCommonService bpaCommonService;
	private ApplicationContext applicationContext = null;
	private List<Inspection> existingInspectionDetails=new ArrayList<Inspection>();
	private InspectionService inspectionService;
	
	public void setBpaCommonService(BpaCommonService bpaCommonService) {
		this.bpaCommonService = bpaCommonService;
	}

	public void setRegisterBpaService(RegisterBpaService registerBpaService) {
		this.registerBpaService = registerBpaService;
	}
	

	public void setInspectionService(InspectionService inspectionService) {
		this.inspectionService = inspectionService;
	}

	
	@Override
	public void executeJob() {
		LOGGER.info("Start Time ------->"+DateUtils.now());
		LOGGER.info("Inside Quartz scheduler BpaAutoGenSiteInspectionDate  START");
		applicationContext = getApplicationContext();
		List <Inspection> inspectionList= new ArrayList<Inspection>();
		bpaCommonService = (bpaCommonService == null) ? (BpaCommonService) applicationContext
				.getBean("bpaCommonService") : bpaCommonService;

		registerBpaService = (registerBpaService == null) ? (RegisterBpaService) applicationContext
				.getBean("registerBpaService") : registerBpaService;

				
		//Check flag, whether auto generate inspection date generation required or not.
		String autoGenInspectionDates=bpaCommonService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,BpaConstants.AUTO_GENERATION_INSPCTIONDATES,null);
		
		if(null!=autoGenInspectionDates && !"".equals(autoGenInspectionDates) && autoGenInspectionDates.equalsIgnoreCase("YES")){
			
		
			/*
			 * Get list of registration object, where inspection is not done and records are forwarded to AEE from assistant.
			 * The forwarded record date should be less than today's date.
			 */
		List<Registration> 	registrationObjectList= registerBpaService.getAllBpaRegistrationForSiteInspectionDateUpdation();
	
		if(registrationObjectList!=null && !registrationObjectList.isEmpty())
		{
			//EgwStatus status_SiteInspection_scheduled= bpaCommonService.getstatusbyCode(BpaConstants.INSPECTIONSCHEDULED);
			
			for(Registration regnObject:registrationObjectList)
			{
				//TODO: THIS CONDITION MAY NOT BE REQUIRED.
				existingInspectionDetails=inspectionService.getInspectionListforRegistrationObject(regnObject);
				
				if(existingInspectionDetails.size()>0){
					//  mean, inspection already present. No need to do any thing.
					LOGGER.info("Inspection already present for property " + regnObject.getPlanSubmissionNum());
				}else
				{
					
					Date nextInspectionDate= bpaCommonService.getInspectionDateForSchedular(regnObject);
				
					// create inspection date, send sms, status update details to be saved in change state table.
					// Send SMS to AEE also.
					Inspection inspection=new Inspection();
						inspection.setInspectionDate(nextInspectionDate);
						inspection.setRegistration(regnObject);
						inspectionList.add(inspection);
						inspectionService.save(inspection, bpaCommonService.getUserbyId(Integer.parseInt(EGOVThreadLocals.getUserId()))
							, null, Boolean.FALSE, bpaCommonService.getstatusbyCode(BpaConstants.INSPECTIONSCHEDULED));
					 bpaCommonService.createStatusChange(regnObject,regnObject.getEgwStatus());
				}
			}
			
			if(inspectionList.size()>0)
			{
				String allowSms=bpaCommonService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,"SMS_NOTIFICATION_ALLOWED_BPA",null);
				String allowEmail=bpaCommonService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,"EMAIL_NOTIFICATION_ALLOWED_BPA",null);
				
				if(null!=allowSms && allowSms!="" && allowSms.equalsIgnoreCase("YES")){
					for(Inspection inspection : inspectionList ){ 
						if(inspection.getRegistration()!=null && inspection.getInspectionDate()!=null) {
							bpaCommonService.buildSMS(inspection.getRegistration(),BpaConstants.SMSEMAILINSPECTION,BpaConstants.INSPECTIONSTARTDATE_SMSBODYDETAILS, DateUtils.getDefaultFormattedDate(inspection.getInspectionDate()).toString());
							bpaCommonService.buildSMS(inspection.getRegistration(),BpaConstants.SMSAEEONAUTOGENERATIONOFSITEINSPECTIONDATE,BpaConstants.INSPECTIONSTARTDATE_SMS_SUBJECT,DateUtils.getDefaultFormattedDate(inspection.getInspectionDate()).toString());
						}
					}	
				}
				
				if(null!=allowEmail && allowEmail!="" && allowEmail.equalsIgnoreCase("YES")){
					for(Inspection inspection : inspectionList ){
						if(inspection.getRegistration()!=null && inspection.getInspectionDate()!=null) {
						bpaCommonService.buildEmailWithMessage(inspection.getRegistration(),BpaConstants.SMSEMAILINSPECTION,BpaConstants.INSPECTIONSTARTDATE_EMAILBODYDETAILS,BpaConstants.INSPECTIONSTARTDATE_EMAIL_SUBJECT,DateUtils.getDefaultFormattedDate(inspection.getInspectionDate()).toString());
						}
					}
				}
			}
			
		}
		
		
		
		
		}
		LOGGER.info("End BpaAutoGenSiteInspectionDate");
				
	}

	 private static String  configLocation[] = {"classpath*:org/egov/infstr/beanfactory/globalApplicationContext.xml",
         "classpath*:org/egov/infstr/beanfactory/egiApplicationContext.xml",
         "classpath*:org/egov/infstr/beanfactory/applicationContext-pims.xml",
         "classpath*:org/egov/infstr/beanfactory/applicationContext-egf.xml",
         "classpath*:org/egov/infstr/beanfactory/applicationContext-bpa.xml",
         "classpath*:org/egov/infstr/beanfactory/applicationContext-erpcollections.xml"}; 
	 
	 
	 protected static ApplicationContext getApplicationContext() {
			ApplicationContext ctx = new ClassPathXmlApplicationContext(configLocation);
			return ctx;
	   }
}
