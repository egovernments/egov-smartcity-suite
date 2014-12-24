package org.egov.pims.workflow.compOff;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.dao.StatusMasterDAO;
import org.egov.pims.empLeave.model.CompOff;
import org.egov.pims.model.StatusMaster;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author DivyaShree
 *
 */
public class CompOffService extends PersistenceService<CompOff,Integer> 
{
	private SimpleWorkflowService<CompOff> compOffWfService;
		
	public final static Logger LOGGER = Logger.getLogger(CompOffService.class.getClass());
	
	
	
	public CompOff createcompOffWorkFlow(CompOff compOff,Position pos)
	{
		
		try{
			  //Position position = EisManagersUtill.getEisCommonsManager().getPositionByUserId(leaveapplication.getEmployeeId().getUserMaster().getId());
			  if(pos==null) {
				  throw new EGOVRuntimeException("The Employe for whom creating CompOff does not have position assigned");
			  }
			  else{
				  LOGGER.info("POSITION ID--"+pos.getId());
				  LOGGER.info("Position Name---"+pos.getName());
				  compOffWfService.start(compOff, pos, "Created by Creator");			 
				  			 
				  /*String desigName = position.getDesigId().getDesignationName().replaceAll(" ","");
				  String actionName = desigName + "_approve";*/
				  String actionName = "user" + "_approve";
				  compOff.getCurrentState().setNextAction("Approval Pending");
				  LOGGER.info("check the userCreated"+compOff.getCreatedBy().getUserName());
				  compOffWfService.transition(actionName,compOff,"created by clerk");
				  			 
				 
			  }
			  				  
		  }catch (ValidationException e) {
			  String errorMsg = "";
			  for(ValidationError vr : e.getErrors()){
				  errorMsg += vr.getMessage(); 				  
			  }
			  throw new EGOVRuntimeException(errorMsg+"");
		  }
		  return compOff;
	}

	
	public CompOff updateCompOff(CompOff compOff,String actionName,String comment)
	{
		LOGGER.info("coming to update---");
		try{
			StatusMaster statusMaster =new StatusMasterDAO().getStatusMaster(actionName.trim());
			compOff.setStatus(statusMaster);  
			//compOffWfService.transition(actionName, compOff, comment);
		  }catch (ValidationException e) {
			  String errorMsg = "";
			  for(ValidationError vr : e.getErrors()){
				  LOGGER.info("UPDATE--validation error---------");
				  errorMsg += vr.getMessage(); 				  
			  }
			  throw new EGOVRuntimeException(errorMsg+" - During workflow updation");
		  }
		  return compOff;
	}
	public void updateWorkflow(CompOff compOff,String approveOrReject,String comments,String statusId)
	{
		Position pos=
			EisManagersUtill.getEisCommonsService().getPositionByUserId(compOff.getAttObj().getEmployee().getUserMaster().getId());		 
		StatusMasterDAO statusMasterDAO =new StatusMasterDAO();
		if(pos==null)
		{
			throw new EGOVRuntimeException("No position defined for employee for whom CompOff created.");
		}
		else
		{
			CompOff compoff=compOffWfService.transition(approveOrReject,compOff,comments );
			compoff.setState(null);
		}
	}

	public SimpleWorkflowService<CompOff> getCompOffWfService() {
		return compOffWfService;
	}
	
	public void setCompOffWfService(SimpleWorkflowService<CompOff> compOffWfService) {
		this.compOffWfService = compOffWfService;
	}
public List<CompOff> getCompOffByEmpAndWorkedOndate(Date workedonFromDate,Date workedontoDate,List empList)
{
	if(empList.size()>=1000)
		throw new EGOVRuntimeException("Search Results Exceeding more than 1000 results, Please try with some more search criteria");
	else
	{
	Criteria criteria=getSession().createCriteria(CompOff.class).
	createAlias("attObj", "attendence").
	createAlias("attendence.employee", "employeeObj").
	add(Restrictions.in("employeeObj.idPersonalInformation", empList));
	if(workedontoDate==null)
		criteria.add(Restrictions.ge("attendence.attDate", workedonFromDate));
	else
		criteria.add(Restrictions.between("attendence.attDate", workedonFromDate, workedontoDate));
	
	return criteria.list();
	}
}
public Boolean isCompOffAvailForDateEmpId(Date comoffDate,Long empid) { 
	Criteria criteria=HibernateUtil.getCurrentSession().createCriteria(CompOff.class).createAlias("attObj", "attendence").
	createAlias("attendence.employee", "employeeObj").
	add(Restrictions.eq("compOffDate", comoffDate)).
	add(Restrictions.eq("employeeObj.idPersonalInformation", empid.intValue())).
	createAlias("status", "statusObj").add(Restrictions.in("statusObj.name", new String[]{EisConstants.STATUS_APPLIED,EisConstants.STATUS_APPROVED}));
	return criteria.list().size()==0?Boolean.valueOf(false):Boolean.valueOf(true); 
}

/**
 * Deciding the compoff workflow either manual or auto
 * @return 'manual' for manual workflow and 'auto' for automatic workflow
 */
public String typeOfCompoffWF(){
	String compoffWfType = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate(EisConstants.MODULE_LEAVEAPP,EisConstants.MODULE_KEY_LEAVEWF,new Date()).getValue();
	return compoffWfType;
}

/**
 * Getting superior position
 * @param position
 * @param objType
 * @return position
 */
public Position getSuperiorPosition(Position position,String objType){
	Position posTemp = null;
	posTemp =EisManagersUtill.getEisCommonsService().getSuperiorPositionByObjType(position, objType);
	return posTemp;
}
}
