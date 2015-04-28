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
package org.egov.collection.web.actions.citizen;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.infstr.models.ServiceDetails;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov") 
public class SearchOnlineReceiptAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private Long serviceTypeId=Long.valueOf(-1);
	private Long 	referenceId;
	private Date fromDate;
	private Date toDate;
	private Integer searchTransactionStatus=-1;
	private List<OnlinePayment> results= new ArrayList<OnlinePayment>();
	private String target="new";
	
	@Override
	public Object getModel() {
		return null;
	}

	
	public SearchOnlineReceiptAction() {
		super();
		addRelatedEntity("serviceType", ServiceDetails.class,"serviceName");
	}
	
	public String reset() {
		results = null;
		serviceTypeId=Long.valueOf(-1);
		fromDate=null;
		toDate=null;
		searchTransactionStatus=-1;
		return SUCCESS;
	}
	
	public void prepare() {
		super.prepare();
		setupDropdownDataExcluding();
	}
	
	public List getOnlineReceiptStatuses () {
		return persistenceService.findAllByNamedQuery(
				CollectionConstants.QUERY_ALL_STATUSES_FOR_MODULE, 
				OnlinePayment.class.getSimpleName());
	}
	
	public List getOnlineReceiptTransitionStatuses () {
		List<String> statusCodes = new ArrayList<String>();
		statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS);
		statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED);
		statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED);
		return persistenceService.findAllByNamedQuery(
				CollectionConstants.QUERY_STATUSES_FOR_MODULE_AND_CODES, 
				OnlinePayment.class.getSimpleName(),statusCodes);
	}
	
	public String search() {
		StringBuilder queryString=new StringBuilder(" select distinct onlinePayment from org.egov.collection.entity.OnlinePayment onlinePayment");
		StringBuilder criteria = new StringBuilder();
		StringBuilder joinString = new StringBuilder();
		StringBuilder whereString = new StringBuilder();//" order by receipt.createdDate desc");
		ArrayList<Object> params = new ArrayList<Object>();
		if (getReferenceId()!=null) {
			criteria.append("onlinePayment.receiptHeader.id = ? ");
			params.add(getReferenceId());
		}
		if (getSearchTransactionStatus() != -1) {
			criteria.append(getJoinOperand(criteria)).append(" onlinePayment.status.id = ? " );
			params.add(getSearchTransactionStatus());
		}
		if (getFromDate() != null) {
			criteria.append(getJoinOperand(criteria)).append(" trunc(onlinePayment.createdDate) >= ? ");
			params.add(fromDate);
		}
		if (getToDate() != null) {
			criteria.append(getJoinOperand(criteria)).append(" onlinePayment.createdDate <= ? ");
			Calendar newTodate= Calendar.getInstance();
			newTodate.setTime(toDate);
			newTodate.add(Calendar.DATE, 1);
			params.add(newTodate.getTime());
		}
		if (getServiceTypeId() != -1) {
			criteria.append(getJoinOperand(criteria)).append(" onlinePayment.receiptHeader.service.id = ? ");
			params.add(Long.valueOf(getServiceTypeId()));
		}
		
		queryString.append(StringUtils.isBlank(joinString.toString())?"":joinString);
		queryString.append(StringUtils.isBlank(criteria.toString())?"":" where ").append(criteria);
		queryString.append(whereString);
		
		results=getPersistenceService().findAllBy(queryString.toString(),params.toArray());
		
		target="searchresult";
		return SUCCESS;
	}

	private String getJoinOperand(StringBuilder criteria) {
		return StringUtils.isBlank(criteria.toString())?"":" and ";
	}

	public List<OnlinePayment> getResults() {
		return results;
	}


	public Integer getSearchTransactionStatus() {
		return searchTransactionStatus;
	}

	public void setSearchTransactionStatus(Integer searchTransactionStatus) {
		this.searchTransactionStatus = searchTransactionStatus;
	}
	
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceType) {
		this.serviceTypeId = serviceType;
	}
	
	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	} 

}
