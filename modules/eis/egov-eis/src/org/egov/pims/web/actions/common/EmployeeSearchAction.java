package org.egov.pims.web.actions.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.SkillMaster;
import org.egov.pims.model.TechnicalGradesMaster;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;

@ParentPackage("egov")

@Results({ 
	@Result(name = "AJAX_RESULT", type = "stream", location = "returnStream", params = { "contentType", "text/plain"})
})
public class EmployeeSearchAction extends BaseFormAction 
{
	private PersistenceService<EmployeeView, Integer> employeeVewService;
	private Integer approverDeptId;
	private Integer approverDesigId;
	private List<EmployeeView> approverEmployeeList = new ArrayList<EmployeeView>(); 
	private EmployeeService employeeService;
	private PersonalInformationService personalInformationService;
	private List<PersonalInformation> activeEmployeeList = new ArrayList<PersonalInformation>();
	private static final String SEARCH_RESULTS = "searchResults";
	private String startsWith;
	private String returnStream ="";
	private Integer branchId;
	List<Bankbranch> listOfBranches = new ArrayList<Bankbranch>();
	List<Bank> listOfBanks = new ArrayList<Bank>();
	private String query;
	PersistenceService<SkillMaster, Integer > skillService;
	private List<TechnicalGradesMaster> gradesList;
	private Integer skillId;
	
	
	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public InputStream getReturnStream() {
		ByteArrayInputStream is = new ByteArrayInputStream(returnStream.getBytes());
		return is;
    }

	
	public PersistenceService<EmployeeView, Integer> getEmployeeVewService() {
		return employeeVewService;
	}
	public void setEmployeeVewService(
			PersistenceService<EmployeeView, Integer> employeeVewService) {
		this.employeeVewService = employeeVewService;
	}
	public Integer getApproverDeptId() {
		return approverDeptId;
	}
	public void setApproverDeptId(Integer approverDeptId) {
		this.approverDeptId = approverDeptId;
	}
	public Integer getApproverDesigId() {
		return approverDesigId;
	}
	public void setApproverDesigId(Integer approverDesigId) {
		this.approverDesigId = approverDesigId;
	}
	public Object getModel() {
		return null;
	}
	
	public String getGradesBySkill()
	{
		System.out.println("persistenceService "+persistenceService);
		SkillMaster skill =(SkillMaster) persistenceService.find("from SkillMaster where  id = ? ",skillId);
		gradesList=new ArrayList<TechnicalGradesMaster>(skill.getSettechnicalGradesMaster());
		return  "gradesList";
	}  
	
	//Used for showing list of approver employee
	public String approverEmpList(){
		return "approverEmpList";
	}
	public List<EmployeeView> getApproverEmployeeList() {
	    if(approverDeptId != null && approverDesigId != null){
		   approverEmployeeList = employeeService.getEmployeeInfoBasedOnDeptAndDesg(approverDeptId, approverDesigId);
		}
		return approverEmployeeList;
	}
	
	//Get emp code by primary assignment.
	public String getEmpListByEmpCodeLike() {

		List<EmployeeView> activeEmp=getPersonalInformationService().getAllActiveEmployeesEmpViewByPrimaryAssignment(startsWith, 30);
		
		for (EmployeeView eView: activeEmp) {
			returnStream = returnStream + eView.getEmployeeCode() + " ~ " + eView.getEmployeeName() + " ~ " + eView.getId() +" ~ " + eView.getDesigId().getDesignationName()+"##";
		}
		return "AJAX_RESULT"; 
}
	
	public String getActiveEmpListByEmpCodeLike() 
	{
		if (StringUtils.isNotBlank(query)) {
			activeEmployeeList=getPersonalInformationService().getActiveEmpListByUserLogin(Integer.valueOf(EGOVThreadLocals.getUserId()), query, 30);
		}
		
		return SEARCH_RESULTS;
	}
	
	public String getBankList()
	{
		listOfBanks = (List<Bank>) persistenceService.findAllBy("from Bank");
		return "bnkautocomplete";
	}
	
	public String getBranchByBankId()
	{
		listOfBranches = (List<Bankbranch>) persistenceService.findAllBy("select B.bankbranchs  from Bank B where B.id=?", Integer.valueOf(branchId));
		return "bnkbrchautocomplete";
	}	
	
	public void setApproverEmployeeList(List<EmployeeView> approverEmployeeList) {
		this.approverEmployeeList = approverEmployeeList;
	}
	
	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	public PersonalInformationService getPersonalInformationService() {
		return personalInformationService;
	}
	public List<PersonalInformation> getActiveEmployeeList() {
		return activeEmployeeList;
	}
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}
	public void setActiveEmployeeList(List<PersonalInformation> activeEmployeeList) {
		this.activeEmployeeList = activeEmployeeList;
	}
	public String getStartsWith() {
		return startsWith;
	}
	public void setStartsWith(String startsWith) {
		this.startsWith = startsWith;
	}
	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	
	public List<Bank> getListOfBanks() {
		return listOfBanks;
	}

	public void setListOfBanks(List<Bank> listOfBanks) {
		this.listOfBanks = listOfBanks;
	}

	public List<Bankbranch> getListOfBranches() {
		return listOfBranches;
	}
	public void setListOfBranches(List<Bankbranch> listOfBranches) {
		this.listOfBranches = listOfBranches;
	}


	public PersistenceService<SkillMaster, Integer> getSkillService() {
		return skillService;
	}


	public void setSkillService(
			PersistenceService<SkillMaster, Integer> skillService) {
		this.skillService = skillService;
	}


	public List<TechnicalGradesMaster> getGradesList() {
		return gradesList;
	}


	public void setGradesList(List<TechnicalGradesMaster> gradesList) {
		this.gradesList = gradesList;
	}


	public Integer getSkillId() {
		return skillId;
	}


	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}
	
}
