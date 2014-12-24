package org.egov.works.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.masters.dao.AccountdetailtypeHibernateDAO;
import org.egov.masters.dao.MastersDAOFactory;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.works.utils.WorksConstants;

/**
 * This service method will used to right work related Non-Transactional call to DAO's.
 * @author prashant.gaurav
 *
 */
public class WorksService {
	private static final Logger logger = Logger.getLogger(WorksService.class);
	private GenericHibernateDaoFactory genericHibDao;
	private EmployeeService employeeService;
	private CommonsService commonsService;

	/**
	 * This method will return the value in AppConfigValue table for the given module and key. 
	 * @param moduleName
	 * @param key
	 * @return
	 */
	public List<AppConfigValues> getAppConfigValue(String moduleName,String key){
		return genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
				moduleName, key);
	}
	
	public List<String> getNatureOfWorkAppConfigValues(String moduleName,String key){
		List<AppConfigValues> appValuesList = genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(
				moduleName, key);
		List<String> natureOfWorksList = new ArrayList<String>();
		if(appValuesList!=null && !appValuesList.isEmpty())
		{
			for(AppConfigValues appValue:appValuesList)
				natureOfWorksList.add(appValue.getValue());
		}
		return natureOfWorksList;
	}
	
	public String getWorksConfigValue(String key){
		List<AppConfigValues> configList = getAppConfigValue("Works", key);
		if(!configList.isEmpty())
			return configList.get(0).getValue();  
		return null;		
	}   

	public void setGenericHibDao(GenericHibernateDaoFactory genericHibDao) {
		this.genericHibDao = genericHibDao;
	}
	
	public Accountdetailtype getAccountdetailtypeByName(String name)
	{
		AccountdetailtypeHibernateDAO accDtlTypeDao=MastersDAOFactory.getDAOFactory().getAccountdetailtypeDAO();
		return (Accountdetailtype)accDtlTypeDao.getAccountdetailtypeByName(name);
	}
	
	public Double getConfigval() {
		String configVal = getWorksConfigValue("MAXEXTRALINEITEMPERCENTAGE");
		Double extraPercentage=null;
		if(StringUtils.isNotBlank(configVal))
			extraPercentage = Double.valueOf(configVal); 
		else
			extraPercentage = Double.valueOf("1");
		return extraPercentage;
	}
	
	/*
	 * returns employee name and designation 
	 * @ return String
	 * @ abstractEstimate, employeeService
	 */

	public String getEmpNameDesignation(Position position,Date date){
		String empName="";
		String designationName="";
		//DeptDesig deptDesig= position.getDeptDesigId();
		DesignationMaster designationMaster=position.getDesigId();		
		designationName=designationMaster.getDesignationName();		
		PersonalInformation personalInformation=null;
		try {
			personalInformation=employeeService.getEmpForPositionAndDate(date, position.getId());
		} catch (Exception e) {
			logger.debug("exception "+e);
		}
		
		if(personalInformation!=null && personalInformation.getEmployeeName()!=null)
			empName=personalInformation.getEmployeeName();
			
		return empName+"@"+designationName;
	} 
      public Boolean getEmpList(Date date,Position position) {
	
		List<PersonalInformation> personalInformationList=null; 
		try {
		personalInformationList= employeeService.getEmpListForPositionAndDate(date, position.getId());
		} catch (Exception e) {
			logger.debug("exception "+e);
		}
		if(personalInformationList.size()>1){
			
			return Boolean.TRUE;
		}
		else 
			return Boolean.FALSE;
	}
	
		
	/**
	 * @param employeeService the employeeService to set
	 */
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	/**
	 * if the bigdecimal obj1 is greater than or egual to obj2 then it returns false
	 * @param obj1
	 * @param obj2
	 * @return  
	 */
	public boolean checkBigDecimalValue(BigDecimal obj1,BigDecimal obj2)
	{
		if(obj1==null)return true;
		if(obj2==null)return true;
		if(obj1.compareTo(obj2)== -1)return false;
		if(obj1.compareTo(obj2)== 0)return false;
		return true;
	}
	
	/**
	 * 
	 * @return list of egwstatus objects  
	 */
	public List<EgwStatus> getStatusesByParams(String objStatus,String objSetStatus,String objLastStatus,String objType) {
		List<String> statList = new ArrayList<String>();
		if(StringUtils.isNotBlank(objStatus))
		   statList.add(objStatus);
		if(StringUtils.isNotBlank(objSetStatus) && StringUtils.isNotBlank(objLastStatus)){
			List<String> statusList = Arrays.asList(objSetStatus.split(","));
			for(String stat: statusList){
				if(stat.equals(objLastStatus)){
					statList.add(stat);
					break;
				}
				else{
					statList.add(stat);
				}
			}
		}
		return commonsService.getStatusListByModuleAndCodeList(objType, statList);
	}
	
	public void createAccountDetailKey(Long id, String type){
		Accountdetailtype accountdetailtype=getAccountdetailtypeByName(type);
		Accountdetailkey adk=new Accountdetailkey();
		adk.setGroupid(1);
		adk.setDetailkey(id.intValue());
		adk.setDetailname(accountdetailtype.getAttributename());
		adk.setAccountdetailtype(accountdetailtype);
		commonsService.createAccountdetailkey(adk);
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	public List getWorksRoles(){
		String configVal = getWorksConfigValue("WORKS_ROLES");
		List rolesList = new ArrayList(); 
		if(StringUtils.isNotBlank(configVal)){
			String[] configVals=configVal.split(",");
			for(int i=0; i<configVals.length;i++)
				rolesList.add(configVals[i]);
		}
		return rolesList;
	}

	public List<String> getTendertypeList() {
		String  tenderConfigValues= getWorksConfigValue(WorksConstants.TENDER_TYPE);
		return Arrays.asList(tenderConfigValues.split(","));
	}	
}