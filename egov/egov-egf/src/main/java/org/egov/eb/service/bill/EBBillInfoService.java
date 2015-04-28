/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.eb.service.bill;

import static java.util.Calendar.MONTH;
import static org.egov.eb.utils.EBConstants.CODE_BILLINFO_RECEIVED;
import static org.egov.eb.utils.EBUtils.getBillDate;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.eb.domain.master.entity.EBConsumer;
import org.egov.eb.domain.master.entity.EBDetails;
import org.egov.eb.domain.master.entity.TargetArea;
import org.egov.eb.domain.transaction.entity.EbSchedulerLog;
import org.egov.eb.domain.transaction.entity.EbSchedulerLogDetails;
import org.egov.eb.service.master.EBConsumerService;
import org.egov.eb.service.master.EBDetailsService;
import org.egov.eb.service.master.TargetAreaService;
import org.egov.eb.service.transaction.EbSchedulerLogService;
import org.egov.eb.utils.EBConstants;
import org.egov.eb.utils.EBUtils;
import org.egov.eb.webservice.tneb.corp.FetchDataProxy;
import org.egov.eb.webservice.tneb.corp.TempPostRecords3;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.Position;

/**
 * 
 * @author nayeem
 * 
 */
public class EBBillInfoService {

	private static final Logger LOGGER = Logger.getLogger(EBBillInfoService.class);

	private EBConsumerService ebConsumerService;
	private EBDetailsService ebDetailsService;
	private TargetAreaService targetAreaService;
	private WorkflowService<EBDetails> ebDetailsWorkflowService;
	private EbSchedulerLogService ebSchedulerLogService; 
	
	private List<EBDetails> existingEBDetails = new ArrayList<EBDetails>();

	private static final String REMARKS_MISSING_POSITION = "AE is not associated";
	private static final String EGF_CONFIG_FILE = "egf_config.xml";

	
	
	public String fetchEBBills(String billingCycle,Long ebLogId) {
		String resultmessage="";
		EbSchedulerLog ebLog=null;
		if (LOGGER.isDebugEnabled()) 	LOGGER.debug("Entered into fetchEBBills, billingCycle=" + billingCycle);
		if(ebLogId!=null)
		{
		ebLog = ebSchedulerLogService.findById(ebLogId, false);
		}
		ebLog.setSchedulerStatus(EbSchedulerLog.STATUS_RUNNING); 
		
		List<EBConsumer> consumers = ebConsumerService.getAllConsumersByBillingCycle(billingCycle);
		String message = "There are no consumers for the selected billing cycle " + billingCycle;
		if (consumers == null || consumers.isEmpty()) {
			LOGGER.debug(message);
			ebLog.setNoOfPendingBills(Long.valueOf(0));
			ebLog.setNoOfBillsProcessed(Long.valueOf(0));
			ebLog.setNoOfBillsCreated(Long.valueOf(0));
			ebLog.setEndTime(new Date());
			ebLog.setSchedulerStatus(EbSchedulerLog.STATUS_COMPLETED); 
			ebSchedulerLogService.persist(ebLog);
			return message;
		}

		EBDetails ebDetails = null;
		EBDetails ebDetailsOld = null;
		Position position = null;
		Integer month = 0;
		BigDecimal variance = BigDecimal.ZERO;
		TempPostRecords3[] responseData = null;
		Date dueDate = null;
		Map<String, List<Object>> detailsByConsumer = ebConsumerService.getConsumerDetails(billingCycle);

		if (detailsByConsumer == null || detailsByConsumer.isEmpty()) {
			message = "Consumer, Area and position data not found in the system for the billing cycle " + billingCycle;
			LOGGER.debug(message);
			//	throw new ValidationException(Arrays.asList(new ValidationError("", message)));
		}
		List<Object> consumerDetails = null;
		int noOfConsumers=consumers.size();
		Date startTime=new Date();
		int count=0;
		int billsCreated=0;   
		int noOfBillsFailed=0;
		
		

		for (EBConsumer consumer : consumers) {
			
			count++;
			try {
				responseData = new FetchDataProxy().fetchData(consumer.getName(), EBConstants.TNEB_WEBSERVICE_USERNAME,
						EBConstants.TNEB_WEBSERVICE_PASSWORD);
			} catch (RemoteException e)
			{
				LOGGER.error("Error in webservice call for consumer=" + consumer, e);
			}

			if (!validateResponse(responseData,consumer,ebLog)) 
			{
				noOfBillsFailed++;
				LOGGER.error("noOfBillsFailed=" +noOfBillsFailed);
				continue;
			}
			
			
				try {
					dueDate = new SimpleDateFormat("dd/MMM/yyyy").parse(responseData[0].getDuedate());
				} catch (ParseException e) {
					LOGGER.error("Error while parsing due date for consumer=" + consumer, e);
				}				

				// TODO- can we assume the month is same for all the bills??
				month = getMonth(dueDate);
				if (isBillExistsForTheMonth(consumer, month)) {
					if (LOGGER.isInfoEnabled()) 
						LOGGER.info("Bill already exists for the consumer " + consumer.getName()+ " and for the month " + month);
					continue;
				}

					consumerDetails = detailsByConsumer.get(consumer.getCode());

					if (consumerDetails == null) {
						LOGGER.error("Area and Position does not exists for the consumer " + consumer.getCode());
					} else {

						ebDetails = new EBDetails();
						ebDetails.setEbConsumer(consumer);
						ebDetails.setBillAmount(new BigDecimal(responseData[0].getAmount()));
						ebDetailsOld = null;  
						ebDetails.setDueDate(dueDate);
						ebDetails.setMonth(month);
						ebDetails.setWard(consumer.getWard());
						ebDetails.setRegion(consumer.getRegion());
						ebDetails.setArea((TargetArea) consumerDetails.get(0));
						position = (Position) consumerDetails.get(1);
						ebDetails.setPosition(position);
						ebDetails.setStatus(EBUtils.getBillEgwStatusByCode(CODE_BILLINFO_RECEIVED));
						ebDetails.setFinancialyear(EBUtils.getFinancialYearForGivenDate(EBUtils.getBillDate(dueDate)));
						ebDetailsOld = getOldBill(ebDetails);
								// if the ebdetails is immediately previous month ebdetails the setting the previous bill amount
								if (ebDetailsOld!=null )
								{
									ebDetails.setPrevBillAmount(ebDetailsOld.getBillAmount());

									if (ebDetailsOld!=null && ebDetailsOld.getBillAmount().compareTo(BigDecimal.ZERO) > 0)
									{
										variance = ebDetails.getBillAmount().subtract(ebDetailsOld.getBillAmount()).
												divide(ebDetailsOld.getBillAmount().multiply(new BigDecimal("100")),
														BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);								
									}
								}

								ebDetails.setVariance(variance);

						
						if (position == null) 
						{
							ebDetails.setComments(REMARKS_MISSING_POSITION);
						} 
						else
						{
							startWorkflow(ebDetails, position);
						}

					if (ebDetails.getComments() == null) {
						ebDetails.setComments(responseData[0].getMessage());
					} else {
						ebDetails.setComments(ebDetails.getComments() + ", " + responseData[0].getMessage());
					}
					billsCreated++;
				//	ebDetailsService.persist(ebDetails);
					}
		
		if((count % 15)==0)
		{
		HibernateUtil.getCurrentSession().flush();
		HibernateUtil.getCurrentSession().clear();
			Date	now= new Date();
			if(now.getMinutes() >=startTime.getMinutes()+5 )
			{
				if(LOGGER.isInfoEnabled()) 		LOGGER.info("exiting from loop as the time is more than 20 minutes");
				if(noOfConsumers>count)
				{
				resultmessage="NOTE: Not all bills processed . Total bills to  Process:="+noOfConsumers +" , number of bills Processed :"+count;
				if(LOGGER.isInfoEnabled()) 	LOGGER.info(resultmessage);
				}
				break;
			}

		}
	} 
		if(!(noOfConsumers>count))
		{
		resultmessage="All pending records processed";
		if(LOGGER.isInfoEnabled()) 	LOGGER.info(resultmessage);
		}
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from fetchAndCreateBillInfo");
		ebLog.setNoOfPendingBills(Long.valueOf(noOfConsumers));
		ebLog.setNoOfBillsProcessed(Long.valueOf(count));
		ebLog.setNoOfBillsCreated(Long.valueOf(billsCreated));
		ebLog.setNoOfBillsFailed(Long.valueOf(noOfBillsFailed));
		ebLog.setEndTime(new Date());
		ebLog.setSchedulerStatus(EbSchedulerLog.STATUS_COMPLETED); 
		
		ebSchedulerLogService.persist(ebLog);
		if (LOGGER.isDebugEnabled()) LOGGER.debug("details"+ebLog);
		
	return resultmessage;
}
private boolean validateResponse(TempPostRecords3[] responseData,EBConsumer consumer, EbSchedulerLog ebLog) {
	EbSchedulerLogDetails ld=new EbSchedulerLogDetails();
	boolean result = true;
	if(responseData==null)
	{
		LOGGER.info("No data received from webservice for "+consumer.getName());
		ld.setMessage(EBConstants.TNEB_RESPONSE_NORESPONSE);
		ld.setEbConsumer(consumer);
		ld.setStatus(EbSchedulerLogDetails.STATUS_FAILURE);
		result=false;
	}
	else
	{
	LOGGER.debug(responseData[0].getCuscode()+"|"+responseData[0].getDuedate()+"|"+responseData[0].getAmount()+"|"+responseData[0].getMessage());
	
	ld.setConsumerNo(responseData[0].getCuscode());
	ld.setDueDate(responseData[0].getDuedate());
	ld.setAmount(responseData[0].getAmount().toString());
	ld.setMessage(responseData[0].getMessage());
	ld.setEbConsumer(consumer);
	
	if ("NA".equalsIgnoreCase(responseData[0].getDuedate())) 
	{
		LOGGER.debug("No Pending amount " + responseData[0].getDuedate());
		ld.setStatus(EbSchedulerLogDetails.STATUS_FAILURE);
		result = false;
		
	} else
	{	
		ld.setStatus(EbSchedulerLogDetails.STATUS_SUCCESS);
	
	}
	}
	ld.setEbSchedulerLog(ebLog);
	ebLog.getLogDetails().add(ld);
	return result;
	}
/**
 * 
 * @param ebDetails
 * @return
 * API returns the old eb details based on due date -3
 */

private EBDetails getOldBill(EBDetails ebDetails) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(ebDetails.getDueDate());
	calendar.add(Calendar.MONTH, -3);
	List<EBDetails> oldBills = null;//ebDetailsService.findAllBy(" from EBDetails where dueDate>=? and status.description!=? order by dueDate desc", calendar.getTime(),EBConstants.CODE_BILLINFO_CANCELLED );
	   if(oldBills!=null && oldBills.size()>0)
	   {
			   return oldBills.get(0);
	   }
	   else
	   {
		   return null;
	   }
	}
 

/**
 * Returns true if the received response data is valid.
 * 
 * response is not valid if 
 * 
 * <li> Due date received is string "NA" </li>
 * <li> Bill amount received is 0 </li> <br />
 * 
 * @param responseData
 * @return true if valid response data else false
 */

private boolean validateResponse(TempPostRecords3[] responseData,String consumerno) {
	boolean result = true;
	if(responseData==null)
	{
		LOGGER.info("No data received from webservice for "+consumerno);
		result=false;
	}
	else
	{
	LOGGER.debug(responseData[0].getCuscode()+"|"+responseData[0].getDuedate()+"|"+responseData[0].getAmount()+"|"+responseData[0].getMessage());
	if ("NA".equalsIgnoreCase(responseData[0].getDuedate())) 
	{
		LOGGER.debug("No Pending amount " + responseData[0].getDuedate());
		result = false;
	} 
	}
	return result;
}


private boolean isBillExistsForTheMonth(EBConsumer consumer, Integer month) {
	EBDetails ebDetails = ebDetailsService.getValidEBDetailsByConsumer(consumer, month);
	return ebDetails != null ? true : false;
}

private Integer getMonth(Date dueDate) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(getBillDate(dueDate));
	return calendar.get(MONTH) + 1; 
}

private EBDetails startWorkflow(EBDetails ebDetails, Position owner) {
	EBDetails ebDtls = null;/*ebDetailsWorkflowService.start(ebDetails, owner);
	ebDetails.changeState(new State(ebDetails.getStateType(), CODE_BILLINFO_RECEIVED, owner, ""));*/
			//This fix is for Phoenix Migration.
	return ebDtls;
}

public void setEbConsumerService(EBConsumerService ebConsumerService) {
	this.ebConsumerService = ebConsumerService;
}

public void setEbDetailsService(EBDetailsService ebDetailsService) {
	this.ebDetailsService = ebDetailsService;
}

public void setEbDetailsWorkflowService(SimpleWorkflowService<EBDetails> ebDetailsWorkflowService) {
	this.ebDetailsWorkflowService = ebDetailsWorkflowService;
}

public TargetAreaService getTargetAreaService() {  
	return targetAreaService;
}

public void setTargetAreaService(TargetAreaService targetAreaService) {
	this.targetAreaService = targetAreaService;
}
public void setEbSchedulerLogService(EbSchedulerLogService ebSchedulerLogService) {
	this.ebSchedulerLogService = ebSchedulerLogService;
}

}
