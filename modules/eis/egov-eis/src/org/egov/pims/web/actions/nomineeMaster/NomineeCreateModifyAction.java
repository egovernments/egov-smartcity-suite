package org.egov.pims.web.actions.nomineeMaster;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailkey;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.model.EmployeeNomineeMaster;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.utils.EisManagersUtill;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validation;
/**
 * 
 * @author DivyaShree
 *
 */
@ParentPackage("egov")
@Validation()
public class NomineeCreateModifyAction extends BaseFormAction  
{

	private String id; 
	PersonalInformation eisObj=null;
	private Integer idPersonalInformation; 
	private EmployeeService employeeService;
	private String mode;
	private String ess;
	EmployeeNomineeMaster empNomineeMstr = new EmployeeNomineeMaster();
	List<EmployeeNomineeMaster> egpimsNomineeMaster = new ArrayList<EmployeeNomineeMaster>();
	List<EmployeeNomineeMaster> egpimsNomineeMasterFromDB = new ArrayList<EmployeeNomineeMaster>();
	public List<EmployeeNomineeMaster> getEgpimsNomineeMasterFromDB() {
		return egpimsNomineeMasterFromDB;
	}
	public void setEgpimsNomineeMasterFromDB(
			List<EmployeeNomineeMaster> egpimsNomineeMasterFromDB) {
		this.egpimsNomineeMasterFromDB = egpimsNomineeMasterFromDB;
	}
	public String bank;
	DateFormat smt  = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	SimpleDateFormat sim = new SimpleDateFormat("dd/MM/yyyy");
	
	//private static final String BRANCH_DETAILS="branchDetails"; 
	PersistenceService<PersonalInformation, Long> employeeMastrService;
	public NomineeCreateModifyAction()
	{
		addRelatedEntity("relationType",org.egov.pims.model.EisRelationType.class);
		addRelatedEntity("bankBranch",org.egov.commons.Bankbranch.class);
	}
	public void prepare()
	{
		super.prepare();
		addDropdownData("relationTypeList", getPersistenceService().findAllBy("from EisRelationType"));
		addDropdownData("bankList", getPersistenceService().findAllBy("from Bank"));
		addDropdownData("disburesementTypeList",getPersistenceService().findAllBy("from DisbursementMode"));
		//addDropdownData("bankBranchList", Collections.EMPTY_LIST);
		
	}
	
	public String Create()
	{
		Integer empId=null;
		if(getId()==null || getId().trim().isEmpty())
		{
			
			if(getEss()!=null && getEss().equals("1"))
			{
				PersonalInformation employee=EisManagersUtill.getEmployeeService().getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
				if(employee==null)
				{
					addActionError("Logged in user is not mapped to employee");
				}
				else{
					empId=employee.getIdPersonalInformation();
				}
			}
				
		}
		else
		{
			empId=Integer.valueOf(getId());
		}
		
			eisObj=EisManagersUtill.getEmployeeService().getEmloyeeById(empId);
			egpimsNomineeMaster = getNomineeList(empId,egpimsNomineeMaster);
				
				
				
		return NEW;
	}
	
	@SuppressWarnings("unchecked")
	public String createNominee()
	{

		eisObj = employeeService.getEmloyeeById(Integer.valueOf(idPersonalInformation));
		egpimsNomineeMasterFromDB = getNomineeList(eisObj.getId(),egpimsNomineeMasterFromDB);
		Iterator<EmployeeNomineeMaster> oldNomineeIterator = egpimsNomineeMasterFromDB.listIterator();
		Set<EmployeeNomineeMaster> newNomineeMasterListtoAdd = new HashSet<EmployeeNomineeMaster>();
		
		while(oldNomineeIterator.hasNext()){
			EmployeeNomineeMaster oldNominee  = oldNomineeIterator.next();
			
			Iterator<EmployeeNomineeMaster> newNomineeIterator = egpimsNomineeMaster.listIterator();
			while(newNomineeIterator.hasNext()){
				EmployeeNomineeMaster newNominee  = newNomineeIterator.next();

				if(null != newNominee && null!= newNominee.getId() && 
						newNominee.getId().equals(oldNominee.getId()))
					
				{
					//: TODO copy the contents from new nominee to old nominee 
					setNomineeAttributes(oldNominee, newNominee);
					newNomineeMasterListtoAdd.add(oldNominee);
					newNomineeIterator.remove();
					break;
				}
			}
		}
		
		if(!egpimsNomineeMaster.isEmpty())
		{
			for(EmployeeNomineeMaster nominee:egpimsNomineeMaster){
				if(nominee!=null){
					nominee.setEmployeeId(eisObj);
					nominee.setModifiedDate(new Date());
					nominee.setBankBranch(null!=nominee.getBankBranch().getId()?nominee.getBankBranch():null);
					newNomineeMasterListtoAdd.add(nominee);
				}
			}
		}
		
		eisObj.getEgpimsNomineeMaster().clear();
		eisObj.getEgpimsNomineeMaster().addAll(newNomineeMasterListtoAdd);
		
		if(egpimsNomineeMasterFromDB.isEmpty()){
			employeeMastrService.setType(PersonalInformation.class);
			employeeMastrService.persist(eisObj);
			
		}
		else{
			employeeMastrService.setType(PersonalInformation.class);
			employeeMastrService.merge(eisObj);
		}
		egpimsNomineeMaster = new ArrayList<EmployeeNomineeMaster>(eisObj.getEgpimsNomineeMaster());
		for(EmployeeNomineeMaster nominee: eisObj.getEgpimsNomineeMaster()){
			if(null!=nominee.getId())
			{	
				Accountdetailkey key = (Accountdetailkey) persistenceService.find("from Accountdetailkey where detailkey=? and accountdetailtype in (select id from Accountdetailtype where tablename='EGEIS_NOMINEE_MASTER')", nominee.getId().intValue());
				if(null==key)
				{
					createAccountForNominee(nominee);
				}
			}	
		}
		addActionMessage(getMessage("NomineeCreateModify.Msg"));
		setMode("view");
		return NEW;
	}
	
	
	protected String getMessage(final String key) {
		return getText(key);
	}
	
	private void createAccountForNominee(EmployeeNomineeMaster nomineeId)
	{
		employeeService.createNomineeMaster(nomineeId);
		
	}
	
	@SkipValidation
	//move to ajax action
	/*public String ajaxLoadBranch()
	{
		
		subCategories = getPersistenceService().findAllBy("from Bankbranch where bank.id=?", Integer.valueOf(bank));
		addDropdownData("bankBranchList", subCategories);
		return BRANCH_DETAILS;
	}*/
	
	@Override
	public Object getModel() {
		
		return empNomineeMstr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PersonalInformation getEisObj() {
		return eisObj;
	}

	public void setEisObj(PersonalInformation eisObj) {
		this.eisObj = eisObj;
	}
	public void setEmployeeMastrService(
			PersistenceService<PersonalInformation, Long> employeeMastrService) {
		this.employeeMastrService = employeeMastrService;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	public Integer getIdPersonalInformation() {
		return idPersonalInformation;
	}
	public void setIdPersonalInformation(Integer idPersonalInformation) {
		this.idPersonalInformation = idPersonalInformation;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	public List<EmployeeNomineeMaster> getEgpimsNomineeMaster() {
		return egpimsNomineeMaster;
	}
	public void setEgpimsNomineeMaster(List<EmployeeNomineeMaster> egpimsNomineeMaster) {
		this.egpimsNomineeMaster = egpimsNomineeMaster;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getEss() {
		return ess;
	}
	public void setEss(String ess) {
		this.ess = ess;
	}
	
	public List<EmployeeNomineeMaster> getNomineeList(Integer empId,List<EmployeeNomineeMaster> nomineeMster) 
	{
		nomineeMster = (List<EmployeeNomineeMaster>) getPersistenceService().findAllBy("from EmployeeNomineeMaster where employeeId.idPersonalInformation=? order by id",empId);
		return nomineeMster;
	}
	
	public void setNomineeAttributes(EmployeeNomineeMaster oldNominee,EmployeeNomineeMaster newNominee)
	{
		oldNominee.setNomineeName(newNominee.getNomineeName());
		oldNominee.setNomineeDob(newNominee.getNomineeDob());
		oldNominee.setNomineeAge(newNominee.getNomineeAge());
		oldNominee.setRelationType(newNominee.getRelationType());
		oldNominee.setMaritalStatus(newNominee.getMaritalStatus());
		oldNominee.setIsActive(newNominee.getIsActive());
		oldNominee.setAccountNumber(newNominee.getAccountNumber());
		oldNominee.setBankBranch(null!=newNominee.getBankBranch().getId()?newNominee.getBankBranch():null);
		oldNominee.setIsWorking(newNominee.getIsWorking());
		oldNominee.setGuardianName(newNominee.getGuardianName());
		oldNominee.setGuardianRelationship(newNominee.getGuardianRelationship());
		oldNominee.setNomineeAddress(newNominee.getNomineeAddress());
		oldNominee.setDisburseType(newNominee.getDisburseType());
	}
}
