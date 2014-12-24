package org.egov.works.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bidder;
import org.egov.commons.ContractorGrade;
import org.egov.commons.Period;
import org.egov.commons.service.BidderTypeService;
import org.egov.commons.service.CommonsService;
import org.egov.commons.service.EntityTypeService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.ContractorDetail;
import org.egov.works.utils.WorksConstants;



public class ContractorService extends PersistenceService<Contractor, Long> implements EntityTypeService,BidderTypeService{

	private CommonsService commonsService;
	private PersistenceService<ContractorGrade,Long> contractorGradeService;
	private DepartmentService departmentService;
	private WorksService worksService;
	private PersistenceService<ContractorDetail,Long> contractorDetailService;	
	
	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	public List<Contractor> getAllActiveEntities(Integer accountDetailTypeId) {
       return findAllBy("select distinct contractorDet.contractor from ContractorDetail contractorDet " +
       		"where contractorDet.status.description=? and contractorDet.status.moduletype=?", "Active","Contractor");
	}
	
	public List<Contractor> filterActiveEntities(String filterKey,
			int maxRecords, Integer accountDetailTypeId) {
		Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		String param = "%" + filterKey.toUpperCase() + "%";
		String qry = "select distinct contractorDet.contractor from ContractorDetail contractorDet " +
				"where contractorDet.status.description=? and contractorDet.status.moduletype=? and (upper(contractorDet.contractor.code) like ? " +
				"or upper(contractorDet.contractor.name) like ?) order by contractorDet.contractor.code,contractorDet.contractor.name";
		return (List<Contractor>) findPageBy(qry, 0, pageSize,
				"Active", "Contractor", param, param).getList();
	}

	@Override
	public List<? extends Bidder> getAllActiveBidders() {
		List<Contractor> entities=new ArrayList<Contractor>();
		entities.addAll(findAllBy("select distinct contractorDet.contractor from ContractorDetail contractorDet " +
       		"where contractorDet.status.description=? and contractorDet.status.moduletype=?", "Active","Contractor"));
	       return entities;
}

	@Override
	public Contractor getBidderByCode(String code) {
		return find("from Contractor where code=?",code);
	}

	@Override
	public Contractor getBidderById(Integer bidderId) {
		return findById(Long.valueOf(bidderId.longValue()),true);
	}
	
	/**
	 * This API creates and returns Contractor object for given Department based on the parameters in the map. If given Department is Public Work then create
	 * Contractor Details for Water Works and Garden by default. If given department is Electrical then create contractor details for Electrical Department only   
	 * Throws validation exception in case of required parameters not passed and any other validations.
	 * @param contractorDetailsMap - HashMap<String,Object> where the following keys are supported:-
	 * "code" 	- the contractor code 
	 * "deptCode"  - department code in which contractor needs to be added 
	 * "name"      - name of the contractor in case of Registration is for individual or company name in case of Company Registration 
	 * "correspondeneceAddress"		- the correspondence address for the contractor
	 * "paymentAddress"		- the address of the contractor in which payment needs to be communicated
	 * "contactPerson"		- The Contact Person name for contractor in case of Registration is for Individual and Company
	 * "email"		- Email address of Contractor/Company for the purpose of communication
	 * "narration"		- Description for Contractor Registration
	 * "panNumber"		- PAN number of contractor/company
	 * "tinNumber"		- TIN number of contractor/company
	 * "bankCode"		- code of the bank in which payment needs to be made
	 * "ifscCode"		- IFSC code of the bank in which payment needs to be made
	 * "bankAccount"	- Bank Account number in which payment needs to be made
	 * "pwdApprovalCode"	- PWD approval code
	 * "registrationNumber"	- Registration number of the contractor for given Department 
	 * "status"	- Registration Status Code of the contractor for given Department - The valid Status codes expected are 'Active', 'Inactive', 'Black-listed', 'Debarred'
	 * "class"	- Class of Contractor(Contractor Grade) 
	 * "startDate"	- Valid Start date of the Registration of contractor - pass in the format dd/mm/yyyy 
	 * "endDate"	- Valid End date of the Registration of contractor - pass in the format dd/mm/yyyy 
	 * @return Contractor Object
	 */
	public Contractor createContractorForDepartment(HashMap<String,Object> contractorDetailsMap) throws ValidationException {
		Contractor contractor =  new Contractor();
		boolean isUpdate = false;
		if(contractorDetailsMap.containsKey(WorksConstants.CODE) && contractorDetailsMap.containsKey(WorksConstants.DEPT_CODE)) {
			Contractor con = find("select distinct cd.contractor from ContractorDetail cd where cd.contractor.code=? and cd.department.deptCode=?", (String)contractorDetailsMap.get(WorksConstants.CODE),(String)contractorDetailsMap.get(WorksConstants.DEPT_CODE));
			if(con !=null ) {
				throw new ValidationException(Arrays.asList(new ValidationError("contractor.exists.for.department","contractor.exists.for.department")));				
			}
		}
		if(contractorDetailsMap.containsKey(WorksConstants.CODE)) {			
			Contractor con = find("select distinct cd.contractor from ContractorDetail cd where cd.contractor.code=? ", (String)contractorDetailsMap.get(WorksConstants.CODE));
			if(con !=null ) {
				contractor = con;
				isUpdate = true;
			}
			else
				contractor.setCode((String)contractorDetailsMap.get(WorksConstants.CODE));			
		}
		
		if(contractorDetailsMap.containsKey(WorksConstants.NAME))
			contractor.setName((String)contractorDetailsMap.get(WorksConstants.NAME));
		
		if(contractorDetailsMap.containsKey(WorksConstants.CORRESPONDENCE_ADDRESS))
			contractor.setCorrespondenceAddress((String)contractorDetailsMap.get(WorksConstants.CORRESPONDENCE_ADDRESS));
		
		if(contractorDetailsMap.containsKey(WorksConstants.PAYMENT_ADDRESS))
			contractor.setPaymentAddress((String)contractorDetailsMap.get(WorksConstants.PAYMENT_ADDRESS));
		
		if(contractorDetailsMap.containsKey(WorksConstants.CONTACT_PERSON))
			contractor.setContactPerson((String)contractorDetailsMap.get(WorksConstants.CONTACT_PERSON));
		
		if(contractorDetailsMap.containsKey(WorksConstants.EMAIL))
			contractor.setEmail((String)contractorDetailsMap.get(WorksConstants.EMAIL));
		
		if(contractorDetailsMap.containsKey(WorksConstants.NARRATION))
			contractor.setNarration((String)contractorDetailsMap.get(WorksConstants.NARRATION));
		
		if(contractorDetailsMap.containsKey(WorksConstants.PAN_NUMBER))
			contractor.setPanNumber((String)contractorDetailsMap.get(WorksConstants.PAN_NUMBER));
		
		if(contractorDetailsMap.containsKey(WorksConstants.TIN_NUMBER))
			contractor.setTinNumber((String)contractorDetailsMap.get(WorksConstants.TIN_NUMBER));
		
		if(contractorDetailsMap.containsKey(WorksConstants.BANK_CODE))
			contractor.setBank(commonsService.getBankByCode((String)contractorDetailsMap.get(WorksConstants.BANK_CODE)));
				
		if(contractorDetailsMap.containsKey(WorksConstants.IFSC_CODE))
			contractor.setIfscCode((String)contractorDetailsMap.get(WorksConstants.IFSC_CODE));
		
		if(contractorDetailsMap.containsKey(WorksConstants.BANK_ACCOUNT))
			contractor.setBankAccount((String)contractorDetailsMap.get(WorksConstants.BANK_ACCOUNT));
		
		if(contractorDetailsMap.containsKey(WorksConstants.PWD_APPROVAL_CODE))
			contractor.setPwdApprovalCode((String)contractorDetailsMap.get(WorksConstants.PWD_APPROVAL_CODE));
		
		if(contractorDetailsMap.containsKey(WorksConstants.DEPT_CODE) && contractorDetailsMap.get(WorksConstants.DEPT_CODE).equals(WorksConstants.DEPT_CODE_PW)) {
			
			ContractorDetail contractorDetail = null;
			// contractor details for Public Work Department			
			contractorDetail = populateContractorDetail(contractorDetailsMap);			
			contractorDetail.setContractor(contractor);
			contractor.addContractorDetail(contractorDetail);
			
			//contractor details for Water Works Department
			contractorDetailsMap.remove(WorksConstants.DEPT_CODE);
			contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_WW);
			contractorDetail = populateContractorDetail(contractorDetailsMap);			
			contractorDetail.setContractor(contractor);
			contractor.addContractorDetail(contractorDetail);
			
			//contractor details for Garden Department
			contractorDetail = new ContractorDetail();
			contractorDetailsMap.remove(WorksConstants.DEPT_CODE);
			contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_GD);
			contractorDetail = populateContractorDetail(contractorDetailsMap);			
			contractorDetail.setContractor(contractor);
			contractor.addContractorDetail(contractorDetail);			
		}
		else if(contractorDetailsMap.containsKey(WorksConstants.DEPT_CODE) && contractorDetailsMap.get(WorksConstants.DEPT_CODE).equals(WorksConstants.DEPT_CODE_ELEC)) {
			
			ContractorDetail contractorDetail = null;
			// contractor details for Electrical Department			
			contractorDetail = populateContractorDetail(contractorDetailsMap);			
			contractorDetail.setContractor(contractor);
			contractor.addContractorDetail(contractorDetail);			
		}
		try{	
			contractor = persist(contractor);
			
			if(!isUpdate)
				createAccountDetailKey(contractor);				
		}
		catch(ValidationException error){	
			throw error;			
		}
		return contractor;
	}
	
	protected void createAccountDetailKey(Contractor cont){
		Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("contractor");		
		Accountdetailkey adk=new Accountdetailkey();
		adk.setGroupid(1);
		adk.setDetailkey(cont.getId().intValue());
		adk.setDetailname(accountdetailtype.getAttributename());
		adk.setAccountdetailtype(accountdetailtype);
		commonsService.createAccountdetailkey(adk);
	}
	
	/**
	 * Returns Active Contractor Id for given Contractor Code and Department Code if it is already registered
	 * @param contractorCode 
	 * @param deptCode
	 * @return contractor id
	 */
	public Long getActiveContractorForDepartment(String contractorCode, String deptCode) {
		Contractor contractor = find("select distinct cd.contractor from ContractorDetail cd where cd.status.description=? " +
				"and cd.status.moduletype=? and cd.contractor.code=? and cd.department.deptCode=?", "Active","Contractor",contractorCode,deptCode);
		if(contractor != null)
			return contractor.getId();
		else
			return null;
	}
	
	/**
	 * This API updates and returns Contractor object for Public Work, Water Works, Garden and Electrical Departments based on the parameters in the map.
	 * If type is 'Update' then replace the Registration no with licence no passed by Licence module. 
	 * If update is for Public work then by default replace the Registration no for Water Works and Garden along with Public Work.
	 * If update is for Electrical then replace the Registration no only for Electrical Department
	 * Throws validation exception in case of required parameters not passed and any other validations.
	 * @param contractorDetailsMap - HashMap<String,Object> where the following keys are supported:-
	 * "code" 	- the contractor code 
	 * "deptCode"  - department code in which contractor needs to be added 
	 * "name"      - name of the contractor in case of Registration is for individual or company name in case of Company Registration 
	 * "correspondeneceAddress"		- the correspondence address for the contractor
	 * "paymentAddress"		- the address of the contractor in which payment needs to be communicated
	 * "contactPerson"		- The Contact Person name for contractor in case of Registration is for Individual and Company
	 * "email"		- Email address of Contractor/Company for the purpose of communication
	 * "narration"		- Description for Contractor Registration
	 * "panNumber"		- PAN number of contractor/company
	 * "tinNumber"		- TIN number of contractor/company
	 * "bankCode"		- code of the bank in which payment needs to be made
	 * "ifscCode"		- IFSC code of the bank in which payment needs to be made
	 * "bankAccount"	- Bank Account number in which payment needs to be made
	 * "pwdApprovalCode"	- PWD approval code
	 * "registrationNumber"	- Registration number of the contractor for given Department 
	 * "status"	- Registration Status Code of the contractor for given Department - The valid Status codes expected are 'Active', 'Inactive', 'Black-listed', 'Debarred'
	 * "class"	- Class of Contractor(Contractor Grade) 
	 * "startDate"	- Valid Start date of the Registration of contractor - pass in the format dd/mm/yyyy 
	 * "endDate"	- Valid End date of the Registration of contractor - pass in the format dd/mm/yyyy 
	 * "contractorUpdateType"	- different types for modifying existing contractor are 'Renewal', 'Update', 'Upgrade', 'Inactive'
	 * @return Contractor Object
	 */
	public Contractor updateContractorForDepartment(HashMap<String,Object> contractorDetailsMap) throws ValidationException {
		List<ContractorDetail> contractorDetails = new ArrayList<ContractorDetail>();
		
		Contractor contractor = new Contractor();
		if(contractorDetailsMap.containsKey(WorksConstants.CODE) && contractorDetailsMap.containsKey(WorksConstants.DEPT_CODE)) {
			
			contractor = find("from Contractor c where c.code=?", (String)contractorDetailsMap.get(WorksConstants.CODE));
			//contractorDetails = contractorDetailService.findAllBy(" from ContractorDetail cd where cd.status.description=? " +
				//"and cd.status.moduletype=? and cd.contractor.code=? and cd.department.deptCode=?","Active","Contractor", (String)contractorDetailsMap.get(WorksConstants.CODE),(String)contractorDetailsMap.get(WorksConstants.DEPT_CODE));
			//if(contractorDetails.isEmpty() ) {
				//throw new ValidationException(Arrays.asList(new ValidationError("contractor.for.department.notfound","contractor.for.department.notfound")));				
			//}
			if(contractor==null) {
				throw new ValidationException(Arrays.asList(new ValidationError("contractor.for.department.notfound","contractor.for.department.notfound")));				
			}
			for(ContractorDetail cd :contractor.getContractorDetails()) {
				if(cd!=null && cd.getDepartment().getDeptCode().equals((String)contractorDetailsMap.get(WorksConstants.DEPT_CODE)) 
						&&  contractorDetailsMap.get(WorksConstants.DEPT_CODE).equals(WorksConstants.DEPT_CODE_ELEC)
						&& cd.getStatus().getDescription().equals("Active") ) {
					contractorDetails.add(cd);
				}
				else if(cd!=null && (contractorDetailsMap.get(WorksConstants.DEPT_CODE).equals(WorksConstants.DEPT_CODE_PW)) 
						&& (cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_PW) 
						|| cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_WW)
								|| cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_GD))
						&& cd.getStatus().getDescription().equals("Active") ) {
					contractorDetails.add(cd);
				}
			}
		}
		if(contractorDetails.isEmpty()) {
			throw new ValidationException(Arrays.asList(new ValidationError("contractor.for.department.notfound","contractor.for.department.notfound")));				
		}
		//contractorDetails=contractor1.getContractorDetails();
		//System.out.println("contractorDetails.get(0).getDepartment().getDeptCode()==>"+contractorDetails.get(0).getDepartment().getDeptCode());
		//Contractor contractor = contractorDetails.get(0).getContractor();
		//Contractor contractor = contractor1;
		
		if(contractorDetailsMap.containsKey(WorksConstants.NAME))
			contractor.setName((String)contractorDetailsMap.get(WorksConstants.NAME));
		
		if(contractorDetailsMap.containsKey(WorksConstants.CORRESPONDENCE_ADDRESS))
			contractor.setCorrespondenceAddress((String)contractorDetailsMap.get(WorksConstants.CORRESPONDENCE_ADDRESS));
		
		if(contractorDetailsMap.containsKey(WorksConstants.PAYMENT_ADDRESS))
			contractor.setPaymentAddress((String)contractorDetailsMap.get(WorksConstants.PAYMENT_ADDRESS));
		
		if(contractorDetailsMap.containsKey(WorksConstants.CONTACT_PERSON))
			contractor.setContactPerson((String)contractorDetailsMap.get(WorksConstants.CONTACT_PERSON));
		
		if(contractorDetailsMap.containsKey(WorksConstants.EMAIL))
			contractor.setEmail((String)contractorDetailsMap.get(WorksConstants.EMAIL));
		
		if(contractorDetailsMap.containsKey(WorksConstants.NARRATION))
			contractor.setNarration((String)contractorDetailsMap.get(WorksConstants.NARRATION));
		
		if(contractorDetailsMap.containsKey(WorksConstants.PAN_NUMBER))
			contractor.setPanNumber((String)contractorDetailsMap.get(WorksConstants.PAN_NUMBER));
		
		if(contractorDetailsMap.containsKey(WorksConstants.TIN_NUMBER))
			contractor.setTinNumber((String)contractorDetailsMap.get(WorksConstants.TIN_NUMBER));
		
		if(contractorDetailsMap.containsKey(WorksConstants.BANK_CODE))
			contractor.setBank(commonsService.getBankByCode((String)contractorDetailsMap.get(WorksConstants.BANK_CODE)));
		
		if(contractorDetailsMap.containsKey(WorksConstants.IFSC_CODE))
			contractor.setIfscCode((String)contractorDetailsMap.get(WorksConstants.IFSC_CODE));
		
		if(contractorDetailsMap.containsKey(WorksConstants.BANK_ACCOUNT))
			contractor.setBankAccount((String)contractorDetailsMap.get(WorksConstants.BANK_ACCOUNT));
		
		if(contractorDetailsMap.containsKey(WorksConstants.PWD_APPROVAL_CODE))
			contractor.setPwdApprovalCode((String)contractorDetailsMap.get(WorksConstants.PWD_APPROVAL_CODE));
		ContractorDetail contractorDetail = null;
		if(contractorDetailsMap.containsKey(WorksConstants.CONTRACTOR_UPDATE_TYPE)) {
			if("Renewal".equals((String)contractorDetailsMap.get(WorksConstants.CONTRACTOR_UPDATE_TYPE))) {
				for(ContractorDetail cd:contractorDetails){			
					if(cd.getStatus().getCode().equals((String)contractorDetailsMap.get(WorksConstants.STATUS)) 
							&& cd.getRegistrationNumber().equals((String)contractorDetailsMap.get(WorksConstants.REGISTRATION_NUMBER))) {
						contractorDetailsMap.remove(WorksConstants.DEPT_CODE);
						if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_PW))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_PW);
						else if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_WW))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_WW);
						else if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_GD))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_GD);
						else if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_ELEC))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_ELEC);
						contractorDetail = populateContractorDetail(contractorDetailsMap);
						contractorDetail.setContractor(contractor);
						contractor.addContractorDetail(contractorDetail);
					}	
					if(cd.getStatus().getCode().equals((String)contractorDetailsMap.get(WorksConstants.STATUS)) 
							&& cd.getRegistrationNumber().equals((String)contractorDetailsMap.get(WorksConstants.REGISTRATION_NUMBER))
							&& cd.getValidity().getEndDate() == null) {						
						cd.getValidity().setEndDate((Date)contractorDetailsMap.get(WorksConstants.END_DATE));
					}		
				}
			}
						
			if("Upgrade".equals((String)contractorDetailsMap.get(WorksConstants.CONTRACTOR_UPDATE_TYPE))) {
				for(ContractorDetail cd:contractorDetails){			
					if(cd.getStatus().getCode().equals((String)contractorDetailsMap.get(WorksConstants.STATUS)) 
							&& cd.getRegistrationNumber().equals((String)contractorDetailsMap.get(WorksConstants.REGISTRATION_NUMBER))) {
						
						contractorDetailsMap.remove(WorksConstants.DEPT_CODE);
						if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_PW))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_PW);
						else if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_WW))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_WW);
						else if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_GD))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_GD);
						else if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_ELEC))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_ELEC);
						contractorDetail = populateContractorDetail(contractorDetailsMap);
						contractorDetail.setContractor(contractor);
						contractor.addContractorDetail(contractorDetail);
					}	
				}
			}
			
			if("Inactive".equals((String)contractorDetailsMap.get(WorksConstants.CONTRACTOR_UPDATE_TYPE))) {
				for(ContractorDetail cd:contractorDetails){			
					if("Inactive".equals((String)contractorDetailsMap.get(WorksConstants.STATUS)) 
							&& cd.getRegistrationNumber().equals((String)contractorDetailsMap.get(WorksConstants.REGISTRATION_NUMBER))) {
						
						contractorDetailsMap.remove(WorksConstants.DEPT_CODE);
						if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_PW))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_PW);
						else if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_WW))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_WW);
						else if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_GD))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_GD);
						else if(cd.getDepartment().getDeptCode().equals(WorksConstants.DEPT_CODE_ELEC))
							contractorDetailsMap.put(WorksConstants.DEPT_CODE, WorksConstants.DEPT_CODE_ELEC);
						contractorDetail = populateContractorDetail(contractorDetailsMap);
						contractorDetail.setContractor(contractor);
						contractor.addContractorDetail(contractorDetail);
					}
					if(cd.getStatus().getCode().equals((String)contractorDetailsMap.get(WorksConstants.STATUS)) 
							&& cd.getRegistrationNumber().equals((String)contractorDetailsMap.get(WorksConstants.REGISTRATION_NUMBER))
							&& cd.getValidity().getEndDate() == null) {						
						cd.getValidity().setEndDate((Date)contractorDetailsMap.get(WorksConstants.END_DATE));
					}
				}
			}
		}
		
		if("Update".equals((String)contractorDetailsMap.get(WorksConstants.CONTRACTOR_UPDATE_TYPE))) {
			for(ContractorDetail cd:contractorDetails){			
				if(cd.getStatus().getCode().equals((String)contractorDetailsMap.get(WorksConstants.STATUS))) {
					
					contractorDetail = cd;
					if(contractorDetailsMap.containsKey(WorksConstants.STATUS)) 
						contractorDetail.setStatus(commonsService.getStatusByModuleAndCode("Contractor",(String)contractorDetailsMap.get(WorksConstants.STATUS)));
					
					if(contractorDetailsMap.containsKey(WorksConstants.CONTRACTOR_CLASS))
						contractorDetail.setGrade(contractorGradeService.find("from ContractorGrade where grade = ?",(String)contractorDetailsMap.get(WorksConstants.CONTRACTOR_CLASS)));
					
					if(contractorDetailsMap.containsKey(WorksConstants.REGISTRATION_NUMBER))
						contractorDetail.setRegistrationNumber((String)contractorDetailsMap.get(WorksConstants.REGISTRATION_NUMBER));
						
					Period period = new Period();
					if(contractorDetailsMap.containsKey(WorksConstants.START_DATE))
						period.setStartDate((Date)contractorDetailsMap.get(WorksConstants.START_DATE));
					if(contractorDetailsMap.containsKey(WorksConstants.END_DATE))
						period.setEndDate((Date)contractorDetailsMap.get(WorksConstants.END_DATE));
					
					contractorDetail.setValidity(period);
					contractorDetail.setContractor(contractor);
					//contractor.addContractorDetail(contractorDetail);
				}	
			}
		}
			
		
		try{
			contractor = persist(contractor);			
		}
		catch(ValidationException error){	
			throw error;			
		}
		return contractor;
	}
	
	private ContractorDetail populateContractorDetail(HashMap<String,Object> contractorDetailsMap) {
		
		ContractorDetail contractorDetail = new ContractorDetail();
		if(contractorDetailsMap.containsKey(WorksConstants.STATUS)) 
		contractorDetail.setStatus(commonsService.getStatusByModuleAndCode("Contractor",(String)contractorDetailsMap.get(WorksConstants.STATUS)));
	
		if(contractorDetailsMap.containsKey(WorksConstants.CONTRACTOR_CLASS))
			contractorDetail.setGrade(contractorGradeService.find("from ContractorGrade where grade = ?",(String)contractorDetailsMap.get(WorksConstants.CONTRACTOR_CLASS)));
		
		if(contractorDetailsMap.containsKey(WorksConstants.REGISTRATION_NUMBER))
			contractorDetail.setRegistrationNumber((String)contractorDetailsMap.get(WorksConstants.REGISTRATION_NUMBER));
		
		if(contractorDetailsMap.containsKey(WorksConstants.DEPT_CODE)) 
			contractorDetail.setDepartment((DepartmentImpl)departmentService.getDepartmentByCode((String)contractorDetailsMap.get(WorksConstants.DEPT_CODE)));
						
		Period period = new Period();
		if(contractorDetailsMap.containsKey(WorksConstants.START_DATE))
			period.setStartDate((Date)contractorDetailsMap.get(WorksConstants.START_DATE));
		if(contractorDetailsMap.containsKey(WorksConstants.END_DATE))
			period.setEndDate((Date)contractorDetailsMap.get(WorksConstants.END_DATE));
		
		contractorDetail.setValidity(period);
		return contractorDetail;		
	}
		
	public void setContractorGradeService(
			PersistenceService<ContractorGrade, Long> contractorGradeService) {
		this.contractorGradeService = contractorGradeService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setContractorDetailService(
			PersistenceService<ContractorDetail, Long> contractorDetailService) {
		this.contractorDetailService = contractorDetailService;
	}
}