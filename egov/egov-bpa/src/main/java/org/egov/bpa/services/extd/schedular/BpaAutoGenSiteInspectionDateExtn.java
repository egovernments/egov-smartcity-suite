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
package org.egov.bpa.services.extd.schedular;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.InspectionExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.inspection.InspectionExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.utils.DateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 
 * @author Pradeep Kumar C M
 *
 */
@Transactional(readOnly=true)
@SuppressWarnings("unchecked")
public class BpaAutoGenSiteInspectionDateExtn  extends AbstractQuartzJob{
	
	private RegisterBpaExtnService registerBpaExtnService;
	private static final Logger LOGGER    = Logger.getLogger(BpaAutoGenSiteInspectionDateExtn.class);
	private BpaCommonExtnService bpaCommonExtnService;
	private ApplicationContext applicationContext = null;
	private List<InspectionExtn> existingInspectionDetails=new ArrayList<InspectionExtn>();
	private InspectionExtnService inspectionExtnService;
	
	@Transactional
	@Override
	public void executeJob() {
		LOGGER.debug("Start Time ------->"+DateUtils.now());
		LOGGER.debug("Inside Quartz scheduler BpaAutoGenSiteInspectionDate Extn  START");
		applicationContext = getApplicationContext();
		List <InspectionExtn> inspectionList= new ArrayList<InspectionExtn>();
		bpaCommonExtnService = (bpaCommonExtnService == null) ? (BpaCommonExtnService) applicationContext
				.getBean("bpaCommonExtnService") : bpaCommonExtnService;

		registerBpaExtnService = (registerBpaExtnService == null) ? (RegisterBpaExtnService) applicationContext
				.getBean("registerBpaExtnService") : registerBpaExtnService;

				
		//Check flag, whether auto generate inspection date generation required or not.
		String autoGenInspectionDates=bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,BpaConstants.AUTO_GENERATION_INSPCTIONDATES,null);
		
		if(null!=autoGenInspectionDates && !"".equals(autoGenInspectionDates) && autoGenInspectionDates.equalsIgnoreCase("YES")){
			
		
			/*
			 * Get list of registration object, where inspection is not done and records are forwarded to AEorAEE from Citizen/Surveyor.
			 * The forwarded record date should be less than today's date.
			 */
		List<RegistrationExtn> 	registrationObjectList= registerBpaExtnService.getAllBpaRegistrationForSiteInspectionDateUpdation();
		if(registrationObjectList!=null && !registrationObjectList.isEmpty())
		{
			//EgwStatus status_SiteInspection_scheduled= bpaCommonExtnService.getstatusbyCode(BpaConstants.INSPECTIONSCHEDULED);
			
			for(RegistrationExtn regnObject:registrationObjectList)
			{
				existingInspectionDetails=inspectionExtnService.findAllBy("from InspectionExtn where registration=? and isInspected=? order by id desc,inspectionDate desc",regnObject,Boolean.FALSE);
				if(existingInspectionDetails.size()>0){
					//mean, inspection already present. No need to do any thing.
					LOGGER.debug("Inspection already present for property " + regnObject.getPlanSubmissionNum());
				}else
				{
					Date nextInspectionDate= bpaCommonExtnService.getInspectionDateForSchedular(regnObject);
					// create inspection date, send sms, status update details to be saved in change state table.
					// Send SMS to AEE also.
					InspectionExtn inspection=new InspectionExtn();
						inspection.setInspectionDate(nextInspectionDate);
						inspection.setRegistration(regnObject);
						inspectionList.add(inspection);
						inspectionExtnService.save(inspection, bpaCommonExtnService.getUserbyId((EgovThreadLocals.getUserId()))
							, null, Boolean.FALSE, bpaCommonExtnService.getstatusbyCode(BpaConstants.INSPECTIONSCHEDULED));
					 bpaCommonExtnService.createStatusChange(regnObject,regnObject.getEgwStatus());
				}
			}
			
			if(inspectionList.size()>0)
			{
				String allowSms=bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,"SMS_NOTIFICATION_ALLOWED_BPA",null);
				String allowEmail=bpaCommonExtnService.getAppconfigValueResult(BpaConstants.BPAMODULENAME,"EMAIL_NOTIFICATION_ALLOWED_BPA",null);
				
				if(null!=allowSms && allowSms!="" && allowSms.equalsIgnoreCase("YES")){
					for(InspectionExtn inspection : inspectionList ){ 
						if(inspection.getRegistration()!=null && inspection.getInspectionDate()!=null) {
							bpaCommonExtnService.buildSMS(inspection.getRegistration(),BpaConstants.SMSEMAILINSPECTION,BpaConstants.INSPECTIONSTARTDATE_SMSBODYDETAILS, DateUtils.getDefaultFormattedDate(inspection.getInspectionDate()).toString());
							bpaCommonExtnService.buildSMS(inspection.getRegistration(),BpaConstants.SMSAEEONAUTOGENERATIONOFSITEINSPECTIONDATE,BpaConstants.INSPECTIONSTARTDATE_SMS_SUBJECT,DateUtils.getDefaultFormattedDate(inspection.getInspectionDate()).toString());
						}
					}	
				}
				
				if(null!=allowEmail && allowEmail!="" && allowEmail.equalsIgnoreCase("YES")){
					for(InspectionExtn inspection : inspectionList ){
						if(inspection.getRegistration()!=null && inspection.getInspectionDate()!=null) {
						bpaCommonExtnService.buildEmailWithMessage(inspection.getRegistration(),BpaConstants.SMSEMAILINSPECTION,BpaConstants.INSPECTIONSTARTDATE_EMAILBODYDETAILS,BpaConstants.INSPECTIONSTARTDATE_EMAIL_SUBJECT,DateUtils.getDefaultFormattedDate(inspection.getInspectionDate()).toString());
						}
					}
				}
			}
			
		}
	 
		}
		LOGGER.info("End BpaAutoGenSiteInspectionDate Extn");
				
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


	public void setRegisterBpaExtnService(
			RegisterBpaExtnService registerBpaExtnService) {
		this.registerBpaExtnService = registerBpaExtnService;
	}


	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonExtnService) {
		this.bpaCommonExtnService = bpaCommonExtnService;
	}


	public void setInspectionExtnService(InspectionExtnService inspectionExtnService) {
		this.inspectionExtnService = inspectionExtnService;
	}
}
