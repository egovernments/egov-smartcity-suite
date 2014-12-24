package org.egov.works.web.actions.masters;  
  
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.ContractorGrade;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.ContractorDetail;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

import com.opensymphony.xwork2.Action;
  
@Result(name=Action.SUCCESS, type="redirect", location = "contractor.action")  
@ParentPackage("egov")  
public class ContractorAction extends SearchFormAction{  
	private static final Logger logger = Logger.getLogger(ContractorAction.class);
	  
	private PersistenceService<Contractor,Long> contractorService;  
	
	private Contractor contractor = new Contractor();  
	
	private List<Contractor> contractorList=null;
	private List<ContractorDetail> actionContractorDetails = new LinkedList<ContractorDetail>();
	private Long id;
	private String mode;
	private CommonsService commonsService;
//	private MastersManager mastersMgr;
	private WorksService worksService;
	
	//-----------------------Search parameters----------------------------------
	private String 	contractorName;
	private String 	contractorcode;
	private Integer departmentId;
	private Long	gradeId;
	private Date    searchDate;
	private boolean sDisabled;
	//-----------------------------prashant--------------------------------------
	
	/*
	 * added on 28th october for view contractor based on criteria
	*/
	private Integer statusId;	
	private List<ContractorDetail> contractorDetailList=null;
	private PersistenceService<ContractorDetail,Long> contractorDetailService;
	private Integer rowId;
	
	private String sourcepage="";
	private String rcNumber="";
	private Long est_department;
	private Long est_zone;
	private Date est_date;
	private String rcType;
	
	private Long est_function;
	private Long est_fund;
	private Long est_budgetGroup;
	
	
	
	public ContractorAction(){
		addRelatedEntity("bank",Bank.class);
	}
	
	
	public String execute() {  
		return list();  
	}  
	
	public String newform(){  
		return NEW;  
	} 

	public String list() {  
		contractorList= contractorService.findAllBy(" from Contractor con order by code asc"); 
		return INDEX;  
	}  
  
	public String edit(){ 
		contractor=contractorService.findById(contractor.getId(), false);
		return EDIT;
	}
		
	/*
	 *  on 28th 2009 for listing contractor based on criteria 
	 */
	public String viewContractor(){
		addDropdownData("departmentList", getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)"));
		return "viewContractor";
	}
	
	public String viewResult(){		
		logger.debug("Inside viewResult");
		//getContractorListForCriterias();
		setPageSize(WorksConstants.PAGE_SIZE);
		search();
		return "viewContractor";		
	}
	
	public void getContractorListForCriterias(){		
		logger.debug("Inside getContractorListForCriterias");
		String contractorStr = null;		
		//if(statusId !=null || departmentId != null || gradeId != null || (contractorcode != null && !contractorcode.equals("")) || (contractorName != null && !contractorName.equals("")))
			contractorStr=" select distinct contractor from Contractor contractor ";
		
		if(statusId !=null || departmentId != null || gradeId != null) {
			contractorStr=contractorStr+" left outer join fetch contractor.contractorDetails as detail ";
		}
		
		if(statusId !=null || departmentId != null || gradeId != null || (contractorcode != null && !contractorcode.equals("")) || (contractorName != null && !contractorName.equals(""))) {			
			contractorStr=contractorStr + " where contractor.code is not null";
		}
	
		if(StringUtils.isNotEmpty(contractorcode)) {
			contractorStr=contractorStr + " and UPPER(contractor.code) like '%" + contractorcode.toUpperCase()+ "%'";
		}
		
		if(StringUtils.isNotEmpty(contractorName)) {			
			contractorStr=contractorStr +" and UPPER(contractor.name) like '%" + contractorName.toUpperCase() + "%'";
		}
				
		if(statusId !=null) {
			contractorStr=contractorStr + " and detail.status.id =" + statusId;
		}
		
		if(departmentId != null) {
			contractorStr=contractorStr + " and detail.department.id = " + departmentId;
		}
		
		if(gradeId != null) {
			contractorStr=contractorStr + " and detail.grade.id = " + gradeId;
		}
		
		if (contractorStr != null && !contractorStr.equals("")) {			
			contractorList = contractorService.findAllBy(contractorStr);
		}
	}
	
	/* end listing contractor based on criteria  */
	
	public String save(){ 
		populateContractorDetails();
		contractor=contractorService.persist(contractor);
		createAccountDetailKey(contractor);		
		String messageKey="contractor.save.success";
		addActionMessage(getText(messageKey,"The Contractor was saved successfully"));		
		return list();  
	}  
	
	
	public String searchApprvdContractor(){
		return "search";
	}
	
	
	
	/**
	 * This method will take user to the search contractor screen.
	 * @author prashant.gaurav
	 */
	public String searchPage() {
		String negDate = (String) request.get("negDate");
		logger.debug("Negotiation date found :----------"+negDate);
		if(negDate != null) {
			SimpleDateFormat dftDateFormatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			try {
				searchDate = dftDateFormatter.parse(negDate);
			} catch (ParseException e) {
				logger.debug("Negotiation date is not valid, should be in dd/MM/yyyy format");
			}
		}
		
		return "search";
	}
	
	/**
	 * This method witll return the list of contrator based on search criteria entered.
	 * @author prashant.gaurav
	 */
	public String searchResult() {
		searchContractor();
		return "search";
		
	}
	
	/**
	 * This method will see if search parameters are in request.
	 * If yes, will search contractor as per criteria and set add it to result list.
	 * @author prashant.gaurav
	 */
	private void searchContractor() { 
		List<AppConfigValues> configList = worksService.getAppConfigValue("Works", "CONTRACTOR_STATUS"); 
		//Assuming that status is inserted using db script
		String status = configList.get(0).getValue();
		logger.debug("CONTRACTOR_STATUS for the module Works in appconfig table, Found ------"+status+" || Expected ------- 'Active'");
		
		if (sourcepage.equals("estimate")) {
			String mainStr = "select distinct rc.contractor from RateContract rc left outer join fetch rc.contractor.contractorDetails as detail where" ;
			if(StringUtils.isNotEmpty(contractorcode)) {
				mainStr+=" lower(rc.contractor.code) like '%"+contractorcode.trim().toLowerCase()+"%' and ";
			}
			if(StringUtils.isNotEmpty(contractorName)) {
				mainStr+=" lower(rc.contractor.name) like  '%"+contractorName.trim().toLowerCase()+"%' and ";
			}
			if(StringUtils.isNotEmpty(rcNumber)) {
				mainStr+=" lower(rc.rcNumber) like '%"+rcNumber.trim().toLowerCase()+"%' and ";
			}
			if(gradeId != null) {
				mainStr=mainStr + " detail.grade.id = :gradeId and ";
			}
			if(est_date!=null){
					String estDate = est_date.toString();
					SimpleDateFormat dftDateFormatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
					try {
						est_date = dftDateFormatter.parse(estDate);
					} catch (ParseException e) {
						logger.debug("Estimate date is not valid, should be in dd/MM/yyyy format");
					}
				mainStr=mainStr + " rc.rcDate <= :est_date and ";
			}
			if(est_department!=null){
				mainStr=mainStr + " rc.indent.department.id = :est_department and ";
			}
			if(est_zone!=null){
				mainStr=mainStr + " rc.indent.boundary.id = :est_zone and ";
			}
			if(rcType!=null){
				rcType=rcType.toLowerCase();
				mainStr=mainStr + " lower(rc.indent.indentType) = :rcType and ";
			}
			if(est_fund!=null){
				mainStr=mainStr + " rc.indent.fund.id = :est_fund and ";
			}
			if(est_function!=null){
				mainStr=mainStr + " rc.indent.function.id = :est_function and ";
			}
			if(est_budgetGroup!=null){
				mainStr=mainStr + " rc.indent.budgetGroup.id = :est_budgetGroup and ";
			}
			 
			mainStr+=" detail.status.description = :status";
			Query qry = null;
			qry = persistenceService.getSession().createQuery(mainStr);
			qry.setString("status", status);
			if(gradeId != null) {
				qry.setLong("gradeId", gradeId); 
			}
			if(est_date!=null){
				qry.setDate("est_date", est_date);
			}
			if(est_department!=null){
				qry.setLong("est_department", est_department);
			}
			if(est_zone!=null){
				qry.setLong("est_zone", est_zone);
			}
			if(rcType!=null){
				qry.setString("rcType", rcType); 
			}
			if(est_fund!=null){
				qry.setLong("est_fund", est_fund); 
			}
			if(est_function!=null){
				qry.setLong("est_function", est_function); 
			}
			if(est_budgetGroup!=null){
				qry.setLong("est_budgetGroup", est_budgetGroup);  
			}
			contractorList = (List)qry.list();  
		}
		else {
		Criteria criteria = contractorService.getSession().createCriteria(Contractor.class);
			
		if(StringUtils.isNotEmpty(contractorcode)) {
			criteria.add(Restrictions.sqlRestriction("lower({alias}.code) like lower(?)","%"+contractorcode.trim()+"%",StringType.INSTANCE));
		}
		
		if(StringUtils.isNotEmpty(contractorName)) {
			criteria.add(Restrictions.sqlRestriction("lower({alias}.name) like lower(?)","%"+contractorName.trim()+"%",StringType.INSTANCE));
		}
	
		criteria.createAlias("contractorDetails", "detail").createAlias("detail.status", "status");
		criteria.add(Restrictions.eq("status.description", status));
		if(departmentId != null) {
			criteria.add(Restrictions.eq("detail.department.id", departmentId));
		}
		
		if(gradeId != null) {
			criteria.add(Restrictions.eq("detail.grade.id", gradeId));
		}
		
		if(searchDate != null) {
			criteria.add(Restrictions.le("detail.validity.startDate", searchDate))
			.add(Restrictions.or(Restrictions.ge("detail.validity.endDate", searchDate),Restrictions.isNull("detail.validity.endDate")));
		}
		
		criteria.addOrder(Order.asc("name"));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		contractorList = criteria.list();
	}
	}
	
	
	protected void createAccountDetailKey(Contractor cont){
//		Accountdetailtype accountdetailtype=mastersMgr.getAccountdetailtypeByName("contractor");
		Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("contractor");
		
		Accountdetailkey adk=new Accountdetailkey();
		adk.setGroupid(1);
		adk.setDetailkey(cont.getId().intValue());
		adk.setDetailname(accountdetailtype.getAttributename());
		adk.setAccountdetailtype(accountdetailtype);
		commonsService.createAccountdetailkey(adk);
	}
	
	protected void populateContractorDetails() {		
		contractor.getContractorDetails().clear();   
		 for(ContractorDetail contractorDetail: actionContractorDetails) {
			 if (validContractorDetail(contractorDetail)) {
				 contractorDetail.setDepartment((DepartmentImpl) getPersistenceService().find("from DepartmentImpl where id = ?", contractorDetail.getDepartment().getId()));
				 
				 if(contractorDetail.getContractor().getId() == null && getDisableContractorDepartments().contains(contractorDetail.getDepartment().getDeptCode())) {
					 String msg="Contractor details not allowed to add for Department: "+contractorDetail.getDepartment().getDeptName();
					 throw new ValidationException(Arrays.asList(new ValidationError(null,msg)));					 
				 }
				 
				 contractorDetail.setStatus((EgwStatus) getPersistenceService().find("from EgwStatus where id = ?", contractorDetail.getStatus().getId()));
				 if(contractorDetail.getGrade().getId()==null) {
					 contractorDetail.setGrade(null); 
				 }
				 else {
					 contractorDetail.setGrade((ContractorGrade) getPersistenceService().find("from ContractorGrade where id = ?", contractorDetail.getGrade().getId()));
				 }
				 contractorDetail.setContractor(contractor);
				 contractor.addContractorDetail(contractorDetail);
			 }
			 else if(contractorDetail!=null){
				 if(contractorDetail.getDepartment()== null || contractorDetail.getDepartment().getId() == null) {
					 contractorDetail.setDepartment(null);
				 }
				 else {
					 contractorDetail.setDepartment((DepartmentImpl) getPersistenceService().find("from DepartmentImpl where id = ?", contractorDetail.getDepartment().getId()));
				 }
				 
				 if(contractorDetail.getContractor().getId() == null && contractorDetail.getDepartment() != null && getDisableContractorDepartments().contains(contractorDetail.getDepartment().getDeptCode())) {
					 String msg="Contractor details not allowed to add for Department: "+contractorDetail.getDepartment().getDeptName();
					 throw new ValidationException(Arrays.asList(new ValidationError(null,msg)));					 
				 }
				 
				 if(contractorDetail.getStatus() == null || contractorDetail.getStatus().getId() == null) {
					 contractorDetail.setStatus(null);
				 }
				 else {
					 contractorDetail.setStatus((EgwStatus) getPersistenceService().find("from EgwStatus where id = ?", contractorDetail.getStatus().getId()));
				 }
				 if(contractorDetail.getGrade() == null || contractorDetail.getGrade().getId() == null) {
					contractorDetail.setGrade(null);
				 }
				 else {
					 contractorDetail.setGrade((ContractorGrade) getPersistenceService().find("from ContractorGrade where id = ?", contractorDetail.getGrade().getId())); 
				 }
				 contractorDetail.setContractor(contractor);
				 contractor.addContractorDetail(contractorDetail);
			 }
		 }			
	 }

	 protected boolean validContractorDetail(ContractorDetail contractorDetail) {		
		 if (contractorDetail!= null && contractorDetail.getDepartment()!=null && contractorDetail.getStatus()!=null && contractorDetail.getDepartment().getId() != null && contractorDetail.getStatus().getId() != null) {			 
			 return true;
		 }		 
		 return false;
	 }
	 	 
	public Object getModel() {  
		return contractor;  
	}  
  
	public List<Contractor> getContractorList() {  
		return contractorList;  
	}  
  
	public void setContractorService(PersistenceService<Contractor,Long> service) {  
		this.contractorService= service;  
	}
	
	public void prepare() {
		if (id != null) {
			contractor = contractorService.findById(id, false);
	    }
		super.prepare();
		setupDropdownDataExcluding("bank");
		
		List<String> depts = getDisableContractorDepartments();  
		StringBuffer sql = new StringBuffer(100);
		sql.append("from DepartmentImpl dep where dep.deptCode not in (");
		for(int i=0,len=depts.size(); i<len;i++){
			 sql.append("'" + depts.get(i) + "'");
			 if(i<len-1){
				 sql.append(',');
			 }
		 }			 
		 sql.append(") order by upper(dep.deptName)");
		String query = sql.toString();
		  
		if(mode!=null && (mode.equals("edit") || mode.equals("view")))
			addDropdownData("departmentList", getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)"));	
		else
			addDropdownData("departmentList", getPersistenceService().findAllBy(query));	 
			
		addDropdownData("gradeList", getPersistenceService().findAllBy("from ContractorGrade order by upper(grade)"));
		addDropdownData("bankList", getPersistenceService().findAllBy("from Bank where isactive=1 order by upper(name)"));
		addDropdownData("statusList", getPersistenceService().findAllBy("from EgwStatus where moduletype='Contractor'"));		
	}
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Contractor getContractor() {
		return contractor;
	}


	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public List<ContractorDetail> getActionContractorDetails() {
		return actionContractorDetails;
	}


	public void setActionContractorDetails(
			List<ContractorDetail> actionContractorDetails) {
		this.actionContractorDetails = actionContractorDetails;
	}


	public CommonsService getCommonsService() {
		return commonsService;
	}


	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}


//	public MastersManager getMastersMgr() {
//		return mastersMgr;
//	}
//
//
//	public void setMastersMgr(MastersManager mastersMgr) {
//		this.mastersMgr = mastersMgr;
//	}


	public String getContractorName() {
		return contractorName;
	}


	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}


	public String getContractorcode() {
		return contractorcode;
	}


	public void setContractorcode(String contractorcode) {
		this.contractorcode = contractorcode;
	}


	public Integer getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}


	public Long getGradeId() {
		return gradeId;
	}


	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public Date getSearchDate() {
		return searchDate;
	}


	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	/**
	 * @return the statusId
	 */
	public Integer getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the contractorDetailList
	 */
	public List<ContractorDetail> getContractorDetailList() {
		return contractorDetailList;
	}

	/**
	 * @param contractorDetailList the contractorDetailList to set
	 */
	public void setContractorDetailList(List<ContractorDetail> contractorDetailList) {
		this.contractorDetailList = contractorDetailList;
	}


	/**
	 * @return the contractorDetailService
	 */
	public PersistenceService<ContractorDetail, Long> getContractorDetailService() {
		return contractorDetailService;
	}


	/**
	 * @param contractorDetailService the contractorDetailService to set
	 */
	public void setContractorDetailService(
			PersistenceService<ContractorDetail, Long> contractorDetailService) {
		this.contractorDetailService = contractorDetailService;
	}	
	
	public boolean isSDisabled() {
		return sDisabled;
	}


	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		
		String contractorStr = null;
		//if(statusId !=null || departmentId != null || gradeId != null || (contractorcode != null && !contractorcode.equals("")) || (contractorName != null && !contractorName.equals("")))
			contractorStr=" from ContractorDetail detail ";
		
		if(statusId !=null || departmentId != null || gradeId != null || (contractorcode != null && !contractorcode.equals("")) || (contractorName != null && !contractorName.equals("")))			
			contractorStr=contractorStr + " where detail.contractor.code is not null";
		
		if(contractorcode != null && !contractorcode.equals(""))
			contractorStr=contractorStr + " and UPPER(detail.contractor.code) like '%" + contractorcode.toUpperCase()+ "%'";
		
		if(contractorName != null && !contractorName.equals(""))
			contractorStr=contractorStr +" and UPPER(detail.contractor.name) like '%" + contractorName.toUpperCase() + "%'";
		
		if(statusId !=null)
			contractorStr=contractorStr + " and detail.status.id =" + statusId;
		
		if(departmentId != null)
			contractorStr=contractorStr + " and detail.department.id = " + departmentId;
		
		if(gradeId != null)
			contractorStr=contractorStr + " and detail.grade.id = " + gradeId;
		
		String query="select distinct detail.contractor "+contractorStr;
		
		String countQuery = "select count(distinct detail.contractor) " + contractorStr;
		return new SearchQueryHQL(query, countQuery, null);
	}


	public Integer getRowId() {
		return rowId;
	}


	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}


	public String getSourcepage() {
		return sourcepage;
	}


	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}


	public String getRcNumber() {
		return rcNumber;
	}


	public void setRcNumber(String rcNumber) {
		this.rcNumber = rcNumber;
	}


	public Long getEst_department() {
		return est_department;
	}


	public void setEst_department(Long est_department) {
		this.est_department = est_department;
	}


	public Long getEst_zone() {
		return est_zone;
	}


	public void setEst_zone(Long est_zone) {
		this.est_zone = est_zone;
	}

	public Date getEst_date() {
		return est_date;
	}


	public void setEst_date(Date est_date) {
		this.est_date = est_date;
	}


	public String getRcType() {
		return rcType;
	}


	public void setRcType(String rcType) {
		this.rcType = rcType;
	}


	public Long getEst_function() {
		return est_function;
	}


	public void setEst_function(Long est_function) {
		this.est_function = est_function;
	}


	public Long getEst_fund() {
		return est_fund;
	}


	public void setEst_fund(Long est_fund) {
		this.est_fund = est_fund;
	}


	public Long getEst_budgetGroup() {
		return est_budgetGroup;
	}


	public void setEst_budgetGroup(Long est_budgetGroup) {
		this.est_budgetGroup = est_budgetGroup;
	}
	
	public List<String> getDisableContractorDepartments() { 
		String depts = worksService.getWorksConfigValue("CREATECONTRACTOR_DISABLE_DEPT");
		if(depts!=null)
			return Arrays.asList(depts.split(","));
		return new ArrayList<String>();
	}
}