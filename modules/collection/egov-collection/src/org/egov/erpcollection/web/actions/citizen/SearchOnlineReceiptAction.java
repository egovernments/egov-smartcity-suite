package org.egov.erpcollection.web.actions.citizen;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.erpcollection.models.OnlinePayment;
import org.egov.erpcollection.web.constants.CollectionConstants;
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
		StringBuilder queryString=new StringBuilder(" select distinct onlinePayment from org.egov.erpcollection.models.OnlinePayment onlinePayment");
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
