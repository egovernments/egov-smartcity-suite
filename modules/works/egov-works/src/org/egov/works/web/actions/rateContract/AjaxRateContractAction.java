package org.egov.works.web.actions.rateContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.rateContract.Indent;
import org.egov.works.models.rateContract.RateContract;
import org.egov.works.models.rateContract.RateContractDetail;
import org.hibernate.Query;

import com.opensymphony.xwork2.Action;

@Results({ 
	@Result(name=Action.SUCCESS, type="redirect", location = "ajaxEstimateRateContract-searchRCResults"),
	@Result(name=Action.SUCCESS, type="redirect", location = "ajaxEstimateRateContract-searchNonSorRCResults")
}) 

@ParentPackage("egov")  
public class AjaxRateContractAction extends BaseFormAction {
	private static final Logger logger = Logger.getLogger(AjaxRateContractAction.class);
	private static final String INDENT = "indents";
	
	private List<Indent>  indent;
	private String fileNum;
	
	private static final String SEARCH_RESULTS = "searchRCResults"; 
	private static final String SEARCH_NONSOR_RESULTS = "searchNonSorRCResults"; 
	private String[] rateContractIds;
	private String[] contractorIds;
	private String query; 
	private String workDetailType;
	private String sorID;
	private String nonSorID;
	private String indentDetailId;
	private RateContractDetail rcDetail=new RateContractDetail();
	private Long contractorId;
	private List<RateContract> rateContracts=new ArrayList<RateContract>();
		
	private Long departmentId;
	private Long zoneId;
	private Date estimateDate;
	private String rcType;	
	private Long functionId;
	private Long fundId;
	private Long budgetGroupId;
	private String rateContractId;
		
	public String execute(){
		return SUCCESS;
	}
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	public AjaxRateContractAction(){}
	
	@SuppressWarnings("unchecked")
	public String loadIndents() {
		
		try {
			if (fileNum == null) {
				
					throw new ValidationException(Arrays.asList(new ValidationError("nobudgetforfunction","Budget head information not available for the chosen function")));
				} 
				else 
				{
					indent = getPersistenceService().findAllBy("select distinct tfd.indent from TenderFileDetail tfd where tfd.indent is not null and tfd.tenderFile.fileNumber=?",fileNum);
				}
			
		} 
		catch (ValidationException egovEx)
		{
			indent = new ArrayList<Indent>();
			addActionError("Unable to get Indent Number for Tender File");
			return INDENT;

		} 
		catch (Exception e) 
		{
			addFieldError("indentunavailable","Unable to get Indent Number for Tender File");
		}
		return INDENT;
	}

	public List<Indent> getIndent() {
		return indent;
	}
	
	public void setIndent(List<Indent> indent) {
		this.indent = indent;
	}
	
	public String getFileNum() {
		return fileNum;
	}
	
	public void setFileNum(String fileNum) {
		this.fileNum = fileNum;
	}
	
	public String searchAjax(){
		return workDetailType.equals("sor")?SEARCH_RESULTS : SEARCH_NONSOR_RESULTS; 
	}
	
	public Collection<RateContractDetail> getScheduleOfRateListforRC() {  
			return workDetailType.equals("sor") ? 
					(persistenceService.findAllBy("from RateContractDetail as rcd where rcd.rateContract.id in("+rateContractIds[0]+") and ((rcd.indentDetail.scheduleOfRate !=null) and rcd.indentDetail.scheduleOfRate.code like concat ('%', ?, '%')"+
					  "or rcd.indentDetail.scheduleOfRate.description like concat ('%', ?, '%'))",query,query)) 
					: (persistenceService.findAllBy("from RateContractDetail as rcd where rcd.rateContract.id in("+rateContractIds[0]+") and ((rcd.indentDetail.nonSor !=null) and rcd.indentDetail.nonSor.description like concat ('%', ?, '%'))",query));
	}
	
	public String findSorRate(){
        rcDetail =(RateContractDetail)persistenceService.find("from RateContractDetail as rcd where rcd.indentDetail.scheduleOfRate.id=? and rcd.indentDetail.id=? and rcd.rateContract.id=?" ,Long.parseLong(sorID),Long.parseLong(indentDetailId),Long.parseLong(rateContractId));
        return "SOR";
	}
	
	public String findNonSorRate(){
        rcDetail =(RateContractDetail)persistenceService.find("from RateContractDetail as rcd where rcd.indentDetail.nonSor.id=? and rcd.indentDetail.id=? and rcd.rateContract.id=?" ,Long.parseLong(nonSorID),Long.parseLong(indentDetailId),Long.parseLong(rateContractId));
        return "NONSOR"; 
	}
	
	public String populateRateContractForContractor(){
		
		String mainStr = "from RateContract as rc where rc.egwStatus.code='APPROVED' and rc.contractor.id in("+contractorIds[0]+") " ;
		
		if(estimateDate!=null){
				String estDate = estimateDate.toString();
				SimpleDateFormat dftDateFormatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
				try {
					estimateDate = dftDateFormatter.parse(estDate);
				} catch (ParseException e) {
					logger.debug("Estimate date is not valid, should be in dd/MM/yyyy format");
				}
			mainStr=mainStr + " and rc.rcDate <= :estimateDate ";
		}
		if(departmentId!=null && departmentId!=-1){
			mainStr=mainStr + " and rc.indent.department.id = :departmentId ";
		}
		if(zoneId!=null){
			mainStr=mainStr + " and rc.indent.boundary.id = :zoneId ";
		}
		if(rcType!=null){
			rcType=rcType.toLowerCase();
			mainStr=mainStr + " and lower(rc.indent.indentType) = :rcType ";
		}
		if(rcType!=null && rcType.equalsIgnoreCase("Amount")){
			if(fundId!=null && fundId!=-1){
				mainStr=mainStr + " and rc.indent.fund.id = :fundId ";
			}
			if(functionId!=null && functionId!=-1){
				mainStr=mainStr + " and rc.indent.function.id = :functionId ";
			}
			if(budgetGroupId!=null && budgetGroupId!=-1){
				mainStr=mainStr + " and rc.indent.budgetGroup.id = :budgetGroupId ";
			}
		}
		 
		Query qry = null;
		qry = persistenceService.getSession().createQuery(mainStr);
				
		if(estimateDate!=null){
			qry.setDate("estimateDate", estimateDate);
		}
		if(departmentId!=null && departmentId!=-1){
			qry.setLong("departmentId", departmentId);
		}
		if(zoneId!=null){
			qry.setLong("zoneId", zoneId);
		}
		if(rcType!=null){
			qry.setString("rcType", rcType); 
		}
		if(rcType!=null && rcType.equalsIgnoreCase("Amount")){
		if(fundId!=null && fundId!=-1){
			qry.setLong("fundId", fundId); 
		}
		if(functionId!=null && functionId!=-1){
			qry.setLong("functionId", functionId); 
		}
		if(budgetGroupId!=null && budgetGroupId!=-1){
			qry.setLong("budgetGroupId", budgetGroupId);  
		}
		}
		rateContracts = (List)qry.list(); 
		//rateContracts=(List<RateContract>)persistenceService.findAllBy("from RateContract as rc where rc.contractor.id in("+contractorIds[0]+")");
		return "rateContract";
		
	}
	
	public List<RateContract> getRateContracts() {
		return rateContracts;
	}

	public void setRateContracts(List<RateContract> rateContracts) {
		this.rateContracts = rateContracts;
	}

	public String[] getRateContractIds() {
		return rateContractIds;
	}

	public void setRateContractIds(String[] rateContractIds) {
		this.rateContractIds = rateContractIds;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getWorkDetailType() {
		return workDetailType;
	}

	public void setWorkDetailType(String workDetailType) {
		this.workDetailType = workDetailType;
	}

	public String getSorID() {
		return sorID;
	}

	public void setSorID(String sorID) {
		this.sorID = sorID;
	}

	public RateContractDetail getRcDetail() {
		return rcDetail;
	}

	public void setRcDetail(RateContractDetail rcDetail) {
		this.rcDetail = rcDetail;
	}

	public String getNonSorID() {
		return nonSorID;
	}

	public void setNonSorID(String nonSorID) {
		this.nonSorID = nonSorID;
	}

	public String getIndentDetailId() {
		return indentDetailId;
	}

	public void setIndentDetailId(String indentDetailId) {
		this.indentDetailId = indentDetailId;
	}
	
	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public String[] getContractorIds() {
		return contractorIds;
	}

	public void setContractorIds(String[] contractorIds) {
		this.contractorIds = contractorIds;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public String getRcType() {
		return rcType;
	}

	public void setRcType(String rcType) {
		this.rcType = rcType;
	}

	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public Long getFundId() {
		return fundId;
	}

	public void setFundId(Long fundId) {
		this.fundId = fundId;
	}

	public Long getBudgetGroupId() {
		return budgetGroupId;
	}

	public void setBudgetGroupId(Long budgetGroupId) {
		this.budgetGroupId = budgetGroupId;
	}

	public String getRateContractId() {
		return rateContractId;
	}

	public void setRateContractId(String rateContractId) {
		this.rateContractId = rateContractId;
	}

}