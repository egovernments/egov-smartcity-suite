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
package org.egov.eb.domain.transaction.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.infstr.models.BaseModel;

public class EbSchedulerLog extends BaseModel {
	public static final String STATUS_COMPLETED="Completed";
	public static final String STATUS_SCHEDULED="Scheduled";
	public static final String STATUS_RUNNING="Running";
	public static final String STATUS_FAILED="Failed";
	public static final String STATUS_CANCELLED="Cancelled";
	
	private Date startTime;
	private Date endTime;
	private Long noOfPendingBills;
	private Long noOfBillsProcessed;
	private Long noOfBillsFailed;
	private Long noOfBillsSuccess;
	private Long noOfBillsCreated;
	private String schedulerStatus;
	private String oddOrEvenBilling;
	
	
	
	private Set<EbSchedulerLogDetails> logDetails=new HashSet<EbSchedulerLogDetails>(0);
	
	public Long getNoOfPendingBills() {
		return noOfPendingBills;
	}
	public void setNoOfPendingBills(Long noOfPendingBills) {
		this.noOfPendingBills = noOfPendingBills;
	}
	public String getSchedulerStatus() {
		return schedulerStatus;
	}
	public void setSchedulerStatus(String schedulerStatus) {
		this.schedulerStatus = schedulerStatus;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Long getNoOfBillsProcessed() {
		return noOfBillsProcessed;
	}
	public void setNoOfBillsProcessed(Long noOfBillsProcessed) {
		this.noOfBillsProcessed = noOfBillsProcessed;
	}
	public Long getNoOfBillsCreated() {
		return noOfBillsCreated;
	}
	public void setNoOfBillsCreated(Long noOfBillsCreated) {
		this.noOfBillsCreated = noOfBillsCreated;
	}
	@Override
	public String toString() {
		return "EbSchedulerLog [ noOfPendingBills=" + noOfPendingBills
				+ ", noOfBillsProcessed=" + noOfBillsProcessed
				+ ", noOfBillsCreated=" + noOfBillsCreated + ", schedulerStatus=" + schedulerStatus
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", logDetails=" + logDetails + "]";
	}
	public Set<EbSchedulerLogDetails> getLogDetails() {
		return logDetails;
	}
	public void setLogDetails(Set<EbSchedulerLogDetails> logDetails) {
		this.logDetails = logDetails;
	}
	
	public Long getNoOfBillsFailed() {
			return noOfBillsFailed;
		}
	public String getOddOrEvenBilling() {
		return oddOrEvenBilling;
	}
	public void setOddOrEvenBilling(String oddOrEvenBilling) {
		this.oddOrEvenBilling = oddOrEvenBilling;
	}
	public void setNoOfBillsFailed(Long noOfBillsFailed) {
		this.noOfBillsFailed = noOfBillsFailed;
	}
	public Long getNoOfBillsSuccess() {
		if(noOfBillsProcessed!=null & noOfBillsFailed!=null)
			return noOfBillsProcessed-noOfBillsFailed;
		else
			return null;
			
		
	}
	public void setNoOfBillsSuccess(Long noOfBillsSuccess) {
		this.noOfBillsSuccess = noOfBillsSuccess;
	}
	

}
