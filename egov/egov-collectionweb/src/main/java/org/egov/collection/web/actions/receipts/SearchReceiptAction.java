package org.egov.erpcollection.web.actions.receipts;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.config.ParentPackage;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.web.actions.SearchFormAction;

@ParentPackage("egov") 
public class SearchReceiptAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private Integer serviceTypeId=-1;
	private Integer counterId=-1;
	private Integer userId=-1;
	private String instrumentType;
	private String receiptNumber;
	private Date fromDate;
	private Date toDate;
	private Integer searchStatus=-1;
	private List<ReceiptHeader> results= new ArrayList<ReceiptHeader>();
	private String target="new";
	private String manualReceiptNumber;
	
	@Override
	public Object getModel() {
		return null;
	}

	public Integer getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Integer serviceType) {
		this.serviceTypeId = serviceType;
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}


	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
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

	
	public SearchReceiptAction() {
		super();
		addRelatedEntity("serviceType", ServiceDetails.class,"serviceName");
	}
	
	public String reset() {
		results = null;
		setPage(1);
		serviceTypeId=-1;
		counterId = -1;
		receiptNumber = "";
		fromDate=null;
		toDate=null;
		instrumentType="";
		searchStatus=-1;
		return SUCCESS;
	}
	
	public void prepare() {
		super.prepare();
		setupDropdownDataExcluding();
		addDropdownData("counterList", getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_ACTIVE_COUNTERS));
		addDropdownData("instrumentTypeList",getPersistenceService().findAllBy("from InstrumentType i where i.isActive = 1 order by type"));
		addDropdownData("userList",getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_CREATEDBYUSERS_OF_RECEIPTS));
	}
	
	public List getReceiptStatuses () {
		return persistenceService.findAllBy(
				"from EgwStatus s where moduletype=? and code != ? order by description",
				ReceiptHeader.class.getSimpleName(), CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
	}
	
	public String search() {
		target="searchresult";
		return super.search();
	}
	
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public SearchQuery prepareQuery(String sortField, String sortDir) {
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuilder searchQueryString = new StringBuilder("select distinct receipt ");
		StringBuilder countQueryString = new StringBuilder("select count(distinct receipt) ");
		StringBuilder fromString = new StringBuilder(" from org.egov.erpcollection.models.ReceiptHeader receipt ");
		final String orderByString = " order by receipt.createdDate desc";		

		// Get only those receipts whose status is NOT PENDING
		StringBuilder criteriaString = new StringBuilder(" where receipt.status.code != ? ");
		params.add(CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
		
		if (StringUtils.isNotBlank(getInstrumentType())) {
			fromString.append(" inner join receipt.receiptInstrument as instruments ");
			criteriaString.append(" and instruments.instrumentType.type = ? ");
			params.add(getInstrumentType());
		}

		if (StringUtils.isNotBlank(getReceiptNumber())) {
			criteriaString.append(" and upper(receiptNumber) like ? ");
			params.add("%" + getReceiptNumber().toUpperCase() + "%");
		}
		if (StringUtils.isNotBlank(getManualReceiptNumber())) {
			criteriaString.append(" and upper(receipt.manualreceiptnumber) like ? ");
			params.add("%" + getManualReceiptNumber().toUpperCase() + "%");
		}
		if (getSearchStatus() != -1) {
			criteriaString.append(" and receipt.status.id = ? ");
			params.add(getSearchStatus());
		}
		if (getFromDate() != null) {
			criteriaString.append(" and receipt.createdDate >= ? ");
			params.add(fromDate);
		}
		if (getToDate() != null) {
			criteriaString.append(" and receipt.createdDate < ? ");
			params.add(DateUtils.add(toDate, Calendar.DATE, 1));
		}
		if (getServiceTypeId() != -1) {
			criteriaString.append(" and receipt.service.id = ? ");
			params.add(Long.valueOf(getServiceTypeId()));
		}
		if (getCounterId() != -1) {
			criteriaString.append(" and receipt.location.id = ? ");
			params.add(getCounterId());
		}
		
		if(getUserId()!=-1){
			criteriaString.append(" and receipt.createdBy.id = ? ");
			params.add(userId);
		}
	
		String searchQuery = searchQueryString.append(fromString).append(criteriaString).append(orderByString).toString();
		String countQuery = countQueryString.append(fromString).append(criteriaString).append(orderByString).toString();
		
		return new SearchQueryHQL(searchQuery, countQuery, params);
	}
  
	public Integer getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(Integer searchStatus) {
		this.searchStatus = searchStatus;
	}

	public SearchQuery prepareQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getManualReceiptNumber() {
		return manualReceiptNumber;
	}

	public void setManualReceiptNumber(String manualReceiptNumber) {
		this.manualReceiptNumber = manualReceiptNumber;
	}
}
