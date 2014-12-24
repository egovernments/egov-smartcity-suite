package org.egov.erpcollection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.erpcollection.models.Challan;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.DateUtils;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov") 
public class SearchChallanAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private Integer serviceId=-1;
	private Date fromDate;
	private Date toDate;
	private Integer status=-1;
	private Integer departmentId=-1;
	private String challanNumber;
	private List<ReceiptHeader> results= new ArrayList<ReceiptHeader>();
	private String target="new";
	private final static String sourcePage="search";
	
	@Override
	public Object getModel() {
		return null;
	}
	public void prepare() {
		super.prepare();
		setupDropdownDataExcluding();
		addDropdownData("departmentList", getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_ALL_DEPARTMENTS));
		addDropdownData("serviceList",getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_CHALLAN_SERVICES,CollectionConstants.CHALLAN_SERVICE_TYPE));
	}
	
	public SearchChallanAction(){
		super();
	}
	public String reset() {
		results = null;
		serviceId=-1;
		challanNumber = "";
		fromDate=null;
		toDate=null;
		status=-1;
		return SUCCESS;
	}
	public List getChallanStatuses () {
		return persistenceService.findAllBy(
				"from EgwStatus s where moduletype=? order by description",
				Challan.class.getSimpleName());
	}
	public String search() {
		StringBuilder queryString=new StringBuilder(" select distinct receipt from org.egov.erpcollection.models.ReceiptHeader receipt");
		StringBuilder criteria = new StringBuilder();
		StringBuilder joinString = new StringBuilder();
		StringBuilder whereString = new StringBuilder(" order by receipt.createdDate desc");
		ArrayList<Object> params = new ArrayList<Object>();
		if (StringUtils.isNotBlank(getChallanNumber())) {
			criteria.append(" upper(receipt.challan.challanNumber) like ? ");
			params.add("%" + getChallanNumber().toUpperCase() + "%");
		}
		if(getDepartmentId()!=-1){
			criteria.append(getJoinOperand(criteria)).append(" receipt.receiptMisc.department.id = ? ");
			params.add(getDepartmentId());
		}
		if (getStatus() != -1) {
			criteria.append(getJoinOperand(criteria)).append(" receipt.challan.status.id = ? ");
			params.add(getStatus());
		}
		if (getFromDate() != null) {
			criteria.append(getJoinOperand(criteria)).append(" receipt.challan.challanDate >= ? ");
			params.add(fromDate);
		}
		if (getToDate() != null) {
			criteria.append(getJoinOperand(criteria)).append(" receipt.challan.challanDate < ? ");
			params.add(DateUtils.add(toDate, Calendar.DATE, 1));
		}
		if (getServiceId() != -1) {
			criteria.append(getJoinOperand(criteria)).append(" receipt.challan.service.id = ? ");
			params.add(Long.valueOf(getServiceId()));
		}
		criteria.append(getJoinOperand(criteria)).append(" receipt.receipttype = ? ");
		params.add(CollectionConstants.RECEIPT_TYPE_CHALLAN);
		
		queryString.append(StringUtils.isBlank(joinString.toString())?"":joinString);
		queryString.append(StringUtils.isBlank(criteria.toString())?"":" where ").append(criteria);
		queryString.append(whereString);
		results=getPersistenceService().findAllBy(queryString.toString(),params.toArray());
		if(results.size()>500){
			 results.clear();
			 throw new ValidationException(Arrays.asList(new ValidationError(
					 "searchchallan.changecriteria","More than 500 results found.Please add more search criteria")));
			 
		}
		target="searchresult";
		return SUCCESS;
	}
	
	private String getJoinOperand(StringBuilder criteria) {
		return StringUtils.isBlank(criteria.toString())?"":" and ";
	}
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getChallanNumber() {
		return challanNumber;
	}

	public void setChallanNumber(String challanNumber) {
		this.challanNumber = challanNumber;
	}
	
	public String getTarget() {
		return target;
	}
	public List<ReceiptHeader> getResults() {
		return results;
	}
	
	public Integer getDepartmentId(){
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId){
		this.departmentId=departmentId;
	}
	
	public String getSourcePage() {
		return sourcePage;
	}
}
