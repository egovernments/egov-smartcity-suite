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

import org.egov.eb.domain.master.entity.EBConsumer;
import org.egov.infstr.models.BaseModel;

public class EbSchedulerLogDetails extends BaseModel {
	
	//if bill is created/already exists it is success else it is failure
	public static final String STATUS_FAILURE="Failure";
	public static final String STATUS_SUCCESS="Success";

	private EbSchedulerLog ebSchedulerLog;
	private EBConsumer ebConsumer;
	//Below 4 are fetched from TNEB so saving as string as we dont know what data it returns (may be no or string)
	private String consumerNo;
	private String dueDate;
	private String amount;
	private String message;
	
    private String status;
	public EbSchedulerLog getEbSchedulerLog() {
		return ebSchedulerLog;
	}
	public void setEbSchedulerLog(EbSchedulerLog ebSchedulerLog) {
		this.ebSchedulerLog = ebSchedulerLog;
	}
	
	public EBConsumer getEbConsumer() {
		return ebConsumer;
	}
	public void setEbConsumer(EBConsumer ebConsumer) {
		this.ebConsumer = ebConsumer;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getConsumerNo() {
		return consumerNo;
	}
	public void setConsumerNo(String consumerNo) {
		this.consumerNo = consumerNo;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
    
}
