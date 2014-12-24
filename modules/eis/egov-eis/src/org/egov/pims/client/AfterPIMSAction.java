/**
 * AfterPIMSAction	1.00
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.client;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.ObjectHistory;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FunctionaryDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.service.GenericCommonsService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.address.dao.AddressDAO;
import org.egov.lib.address.dao.AddressTypeDAO;
import org.egov.lib.address.model.Address;
import org.egov.lib.address.model.AddressTypeMaster;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.commons.dao.PositionMasterDAO;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.AssignmentPrd;
import org.egov.pims.model.BloodGroupMaster;
import org.egov.pims.model.CommunityMaster;
import org.egov.pims.model.DetOfEnquiryOfficer;
import org.egov.pims.model.DisciplinaryPunishment;
import org.egov.pims.model.DisciplinaryPunishmentApproval;
import org.egov.pims.model.EmployeeDepartment;
import org.egov.pims.model.EmployeeGroupMaster;
import org.egov.pims.model.EmployeeStatusMaster;
import org.egov.pims.model.GenericMaster;
import org.egov.pims.model.GradeMaster;
import org.egov.pims.model.LangKnown;
import org.egov.pims.model.LanguagesKnownMaster;
import org.egov.pims.model.LanguagesQulifiedMaster;
import org.egov.pims.model.LtcPirticulars;
import org.egov.pims.model.PersonAddress;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.ReligionMaster;
import org.egov.pims.model.StatusMaster;
import org.egov.pims.model.TrainingPirticulars;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.Session;
//import org.egov.infstr.utils.ServiceLocator;
//import org.egov.lib.rjbac.user.ejb.api.UserManagerHome;

/*
 * deepak yn,DivyaShree
 * creates all the masters for an employee
 *
 *
 */
public class AfterPIMSAction extends DispatchAction
{
	  public final static Logger LOGGER = Logger.getLogger(AfterPIMSAction.class.getClass());
	  private EmployeeService employeeService;
	  private GenericCommonsService genericCommonsService;
	  private CommonsService commonsService;
	  private DepartmentService departmentService;

	  private UserService userService;

	  private transient PersonalInformationService personalInformationService;

	protected boolean returnToken(HttpServletRequest req)
		{
			if(!isTokenValid(req, true))
			{
				return true;
			}
			else
			{
				return false;
			}
		}

	  protected boolean checkAutoGenerateCode()
	  {
		  if(EisManagersUtill.getEisCommonsService().isEmployeeAutoGenerateCodeYesOrNo())
		  {
			  return true;
		  }
		  else
		  {
			  return false;
		  }
	  }


	public ActionForward saveDetailsEmployee(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target =null;
		String alertMessage=null;
		String strErrorMsg;
		ActionMessages messages = new ActionMessages();
		HttpSession session = req.getSession();
		String userName =(String) session.getAttribute("com.egov.user.LoginUserName");
		User userLog =  userService.getUserByUserName(userName);
		AddressDAO addressDao = GenericDaoFactory.getDAOFactory().getAddressDao();

		if (returnToken(req))
		{
			target = "goToAssignment";

		}
		else
		{
		try
		{
			//UserService userManager=getEisUserManagr();
			PIMSForm pIMSForm  = (PIMSForm)form;
			String empCode="";
			Boolean empAutoEmpCode= false;
			String codeNum=null;
			   UserImpl user = new UserImpl();
				PersonalInformation egpimsPersonalInformation = new PersonalInformation();
				setDetailsOfpimsForCreateAndModify( egpimsPersonalInformation, pIMSForm,user);
				egpimsPersonalInformation.setIsActive(1);
				if(checkAutoGenerateCode())
				{
				    codeNum = personalInformationService.generateEmployeeCode(egpimsPersonalInformation);
				}
				else
				{
					empCode = (String)pIMSForm.getEmployeeCode();
					if(!StringUtils.trimToEmpty(empCode).equals(""))
					{
						empAutoEmpCode=EisManagersUtill.getEisCommonsService().checkEmpCode(empCode);
						if(empAutoEmpCode)
						{
						    throw new DuplicateElementException("Employee code "+ empCode +" already exist");
						}
						else
						{
							codeNum = empCode;
						}
					}
				}



				LOGGER.debug("<<<<<< EmployeeCode = "+codeNum);
				if(codeNum == null)
				{
					throw new EGOVRuntimeException("Unable to get Employee Code.");
				}
				egpimsPersonalInformation.setEmployeeCode(codeNum.toUpperCase());


				PersonAddress egpimsPersonAddresspre =new PersonAddress();
				String presentAdd   = (String)pIMSForm.getPropertyNopre();
				if(presentAdd != null && !presentAdd.equals(""))
				{
					Address objAddress = new Address ();
					setAdressForPresent(objAddress,pIMSForm,req,MODULE_CREATE);
					egpimsPersonAddresspre.setPersonAddress(objAddress);
					egpimsPersonAddresspre.setEmployeeId(egpimsPersonalInformation);
					egpimsPersonalInformation.addPersonAddresses(egpimsPersonAddresspre);
					addressDao.create(objAddress);
				}
				String per   = (String)pIMSForm.getPropertyNoper();

				//TODO change check to per and not not to pre
				if(per!=null && !per.equals(""))
				{
					Address objAddressper = new Address ();
					setAdressForPermenent(objAddressper,pIMSForm,req,MODULE_CREATE);
					PersonAddress egpimsPersonAddressper =new PersonAddress();
					egpimsPersonAddressper.setPersonAddress(objAddressper);
					egpimsPersonAddressper.setEmployeeId(egpimsPersonalInformation);
					egpimsPersonalInformation.addPersonAddresses(egpimsPersonAddressper);
					addressDao.create(objAddressper);
				}
				setLangKnown(egpimsPersonalInformation,req);
				egpimsPersonalInformation.setCreatedBy(userLog);
				egpimsPersonalInformation.setCreatedTime(new Date());
					if(((String)pIMSForm.getUserStatus()).equals("0"))
					{
							user.setIsActive(Integer.valueOf(0));
					}
					else
					{
						user.setIsActive(Integer.valueOf(1));
					}
					user.setUserName((String)pIMSForm.getUserFirstName());
					user.setPwdReminder((String)pIMSForm.getUserFirstName()+"_"+codeNum);
					user.setPwd(user.getPwdReminder());//usermanager.createUser will take plain pwd and encrypt it and set the encrypted value back
					user.setIsSuspended('N');
					user.setPwdModifiedDate(new Date());
					if(null!=user.getUserName() && !user.getUserName().trim().equals(""))
					{
						UserImpl existingUser= (UserImpl) userService.getUserByUserName(user.getUserName());// to check if the selected/entered user in db
						if(existingUser!=null)
						{
							egpimsPersonalInformation.setUserMaster(existingUser);
						}
						else
						{
							egpimsPersonalInformation.setUserMaster(user);
							userService.createUser(user);
						}
					}

					LOGGER.info("********Timestamp 1 -->" + egpimsPersonalInformation.getLastmodifieddate() );
					setAssignmentCreate(pIMSForm,egpimsPersonalInformation,req);

					LOGGER.info("********Timestamp 2 -->" + egpimsPersonalInformation.getLastmodifieddate() );
					egpimsPersonalInformation.setCreatedTime(new Date());
					LOGGER.info("********Timestamp 3 -->" + egpimsPersonalInformation.getLastmodifieddate() );
					req.getSession().setAttribute("master","Employee");
					req.setAttribute(STR_EMPLOYEEOBJ,egpimsPersonalInformation);
					//req.setAttribute("Id",egpimsPersonalInformation.getIdPersonalInformation());
					//logger.info("after save!! " + egpimsPersonalInformation.getIdPersonalInformation());
					//TODO REMOVE HibernateUtil.getCurrentSession().flush();
					//HibernateUtil.getCurrentSession().flush();
					target = "successEmployee";

	}
		catch(DuplicateElementException invalidExp)
		{
			handleDuplicateException(req, messages, invalidExp);
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
	}
		req.setAttribute(STR_ALERT, alertMessage);
		return mapping.findForward(target);
}

	public ActionForward saveEmployee(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
			String target =null;
		    //PersonalInformation egpimsPersonalInformation = new PersonalInformation();
		    PersonalInformation egpimsPersonalInformation=(PersonalInformation)req.getAttribute(STR_EMPLOYEEOBJ);
		    employeeService.createEmloyee(egpimsPersonalInformation);
		    target="successCreateEmployee";
		    return mapping.findForward(target);

	}
		private void setLangKnown(PersonalInformation egpimsPersonalInformation,HttpServletRequest req)
	{
		try {
			String[] langKnown = req.getParameterValues("lanId");
			 if(langKnown!=null)
			 {
			 	int langLength =langKnown.length;

			 	for(int i=0;i<langLength;i++)
			 	{
			 		if(!langKnown[i].equals("0"))
			 		{
				 		String langMasterid = (String)langKnown[i];
				 		GenericMaster genericMaster = (GenericMaster)employeeService.getGenericMaster(Integer.valueOf(langMasterid).intValue(),"LanguagesKnownMaster");
				 		LanguagesKnownMaster languagesKnownMaster =(LanguagesKnownMaster)genericMaster;
						LangKnown lanKnown = new LangKnown();
						if(languagesKnownMaster!=null)
						{
				 		lanKnown.setLangKnown(languagesKnownMaster);
						}
				 		egpimsPersonalInformation.addLangKnown(lanKnown);
			 		}
			 	}
			 }
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}


public ActionForward modifyDetailsEmployee(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
{
		String target =null;
		String alertMessage=null;
		ActionMessages messages = new ActionMessages();
		if (returnToken(req))
		{
		    target = STR_GOTO_BEFORE_SEARCH;
		}
		else
		{
		try
		{

			    PIMSForm pIMSForm  = (PIMSForm)form;
				String employeeId = req.getParameter("Id").trim();
				PersonalInformation egpimsPersonalInformation =null;
				egpimsPersonalInformation = employeeService.getEmloyeeById(Integer.valueOf(employeeId));
				UserImpl user = null;
				String codeNum = (String)pIMSForm.getEmployeeCode().toUpperCase();
				String formUser = (String)pIMSForm.getUserFirstName();//User name on the form
				if(!formUser.trim().equals("")) // to avoid inserting null to username in eg_user, update the emp without username
				{
					user = (UserImpl) userService.getUserByUserName(formUser);// to get the selected/entered user if already existing in db		
					//If user already exists, then map the employee to that user. Otherwise create a new user and map to the employee.						
					 
					if(null==user)
					{
						// user does not exist, so we need to create one						
							user = new UserImpl();
	
							if(((String)pIMSForm.getUserStatus()).equals("0"))
							{
								user.setIsActive(Integer.valueOf(0));
							}
							else
							{
								user.setIsActive(Integer.valueOf(1));
							}
							user.setUserName((String)pIMSForm.getUserFirstName());
							user.setPwdReminder((String)pIMSForm.getUserFirstName()+"_"+codeNum);
							user.setPwd(user.getPwdReminder());//usermanager.createUser will take plain pwd and encrypt it and set the encrypted value back
							user.setIsSuspended('N');
							user.setPwdModifiedDate(new Date());
	
					}					
				
				} 	
				if(user!=null)
				{
					if(((String)pIMSForm.getUserStatus()).equals("0"))
					{
							user.setIsActive(Integer.valueOf(0));
					}
					else
					{
						user.setIsActive(Integer.valueOf(1));
					}	
				}	
				egpimsPersonalInformation.setUserMaster(user);
				setDetailsOfpimsForCreateAndModify( egpimsPersonalInformation, pIMSForm,user);
				if(pIMSForm.getEmployeeActiveCheckbox())
				{
					egpimsPersonalInformation.setIsActive(1);
				}
				else
				{
					egpimsPersonalInformation.setIsActive(0);
				}
				if(null!=user &&null==user.getId()){
					userService.createUser(user);}
				deleteLangKnown(egpimsPersonalInformation);
				setLangKnown(egpimsPersonalInformation,req);


				modifyAssignmentCreate(pIMSForm,egpimsPersonalInformation,req);
				//There is no need for this call as associated Hib object would be updated by HibFilter
				//getEisManagr().updateEmloyee(egpimsPersonalInformation);
				ObjectHistory objHistory = new ObjectHistory();
				objHistory.setModifiedBy(user);
				objHistory.setModifiedDate(new Date());
				objHistory.setObjectType(commonsService.getObjectTypeByType("employee"));
				objHistory.setObjectId(egpimsPersonalInformation.getIdPersonalInformation());
				objHistory.setRemarks((String)pIMSForm.getModifyremarks());
				commonsService.createObjectHistory(objHistory);
				//HibernateUtil.getCurrentSession().flush();
				req.setAttribute(STR_EMPLOYEEOBJ,egpimsPersonalInformation);
				target = "successModify";



	}
		catch(DuplicateElementException invalidExp)
		{
			handleDuplicateException(req, messages, invalidExp);
		}
		catch(Exception ex)
		{
		   target = STR_ERROR;
		   LOGGER.error(ex.getMessage());
		   HibernateUtil.rollbackTransaction();
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
		}
		req.setAttribute(STR_ALERT, alertMessage);
		return mapping.findForward(target);

}

public ActionForward modifyDetailsAddress (ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
{
	String target =null;
	AddressDAO addressDao = GenericDaoFactory.getDAOFactory().getAddressDao();

	PIMSForm pIMSForm  = (PIMSForm)form;
	PersonalInformation egpimsPersonalInformation = null;
	if( req.getAttribute(STR_EMPLOYEEOBJ) != null)
	{
		 egpimsPersonalInformation=(PersonalInformation)req.getAttribute(STR_EMPLOYEEOBJ);
	}
	Address objAddress = null;
	Address objAddressper = null;
	Map  addressMap   = getMapOfAddress(egpimsPersonalInformation);
	Iterator itr = addressMap.keySet().iterator();
	while(itr.hasNext())
	{
		String addname = (String)itr.next();
		if("PRESENTADDRESS".equals(addname))
		{
			objAddress = (Address)addressMap.get(addname);
		}
		if(PERMANENTADDRESS.equals(addname))
		{
			objAddressper = (Address)addressMap.get(addname);
			//objAddressper.set//
		}
	}
	if (objAddress != null) {
		setAdressForPresent(objAddress,pIMSForm,req,STR_MODIFY);
		if(pIMSForm.getPropertyNopre()!=null && ((String)pIMSForm.getPropertyNopre()).length() > 0)
		{
			addressDao.update(objAddress);
		}
	} else {
		if(pIMSForm.getPropertyNopre()!=null && ((String)pIMSForm.getPropertyNopre()).length() > 0)
		{
			objAddress = new Address();
			setAdressForPresent(objAddress,pIMSForm,req,STR_MODIFY);
			PersonAddress egpimsPersonAddresspre =new PersonAddress();
			egpimsPersonAddresspre.setEmployeeId(egpimsPersonalInformation);
			egpimsPersonAddresspre.setPersonAddress(objAddress);
			egpimsPersonalInformation.addPersonAddresses(egpimsPersonAddresspre);

		}
	}

	String per = (String)req.getParameter(STR_PROPERTYNOPER);
	if(objAddressper!=null)
	{
		setAdressForPermenent(objAddressper,pIMSForm,req,STR_MODIFY);

		if(per!=null && !per.equals(""))
		{

		    addressDao.update(objAddressper);

		}
	}
	else
	{
		if(pIMSForm.getPropertyNoper()!=null && ((String)pIMSForm.getPropertyNoper()).length() > 0)
		{
			objAddressper = new Address();
			setAdressForPermenent(objAddressper,pIMSForm,req,STR_MODIFY);
			PersonAddress egpimsPersonAddressper =new PersonAddress();
			egpimsPersonAddressper.setEmployeeId(egpimsPersonalInformation);
			egpimsPersonAddressper.setPersonAddress(objAddressper);
			egpimsPersonalInformation.addPersonAddresses(egpimsPersonAddressper);

		}

	}
	req.setAttribute(STR_EMPLOYEEOBJ,egpimsPersonalInformation);
	target = "successPayroll";
	return mapping.findForward(target);
}

private void deleteLangKnown(PersonalInformation personalInformation)
{
	try {
		employeeService.deleteLangKnownForEmp(personalInformation);
	} catch (Exception e) {
		LOGGER.debug(e.getMessage());
		throw new EGOVRuntimeException(e.getMessage(),e);
	}
}
/*
public ActionForward saveDetailsAssignment(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException
{
	String target =null;
	String alertMessage=null;
	DynaBean pIMSForm = (DynaBean)form;
	if (!isTokenValid(req, true))
	{
	    target = new String("goToBeforeSearch");
	}
	else
	{
		try
		{
				String id = req.getParameter("Id");
				PersonalInformation egpimsPersonalInformation =null;
				if( id!=null&&!id.equals(""))
				{
					egpimsPersonalInformation = getEisManagr().getEmloyeeById(new Integer(id.trim()));
					logger.info("egpimsPersonalInformation"+egpimsPersonalInformation);

				}
				if(egpimsPersonalInformation!=null)
				{
					String fromDate[]=null;
					fromDate = (String[])pIMSForm.get("fromDate");
					int ArrLen = fromDate.length;
					String counter[]=req.getParameterValues("counter");
					logger.info("ArrLen"+ArrLen);
					if(fromDate[0]!=null&&!fromDate[0].equals(""))
					{
						for (int len = 0; len < ArrLen; len++)
						{
							if(fromDate[len]!=null && !fromDate[len].equals(""))
							{
								AssignmentPrd egEmpAssignmentPrd = new AssignmentPrd();
								Assignment egEmpAssignment= new Assignment();
								addAssignmentPrdForEmployee(egEmpAssignmentPrd,egEmpAssignment,len,req,pIMSForm,egpimsPersonalInformation,counter[len]);
								egEmpAssignmentPrd.setEmployeeId(egpimsPersonalInformation);
								getEisManagr().createAssignmentPrd(egEmpAssignmentPrd);
								egEmpAssignment.setAssignmentPrd(egEmpAssignmentPrd);
								getEisManagr().createAssignment(egEmpAssignment);
								egEmpAssignmentPrd.addAssignment(egEmpAssignment);
								populateDepartment(egEmpAssignment,req,new Integer(len).toString());
								logger.info("addAssignment");
								getEisManagr().addAssignmentPrd(egpimsPersonalInformation,egEmpAssignmentPrd);
							}

						}
					}

				}
				getEisManagr().updateEmloyee(egpimsPersonalInformation);
				logger.info("updateEmloyee");
					HibernateUtil.getCurrentSession().flush();
					target = "success";
					alertMessage="Executed saving Assignment successfully";

	}catch(Exception ex)
		{
		   target = "error";
		   logger.info("Exception Encountered!!!"+ex.getMessage());
		   ex.printStackTrace();
		   HibernateUtil.rollbackTransaction();
		   throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
		}
	}
	logger.info("target"+target);
	req.setAttribute("alertMessage", alertMessage);
	return mapping.findForward(target);
}
*/
/*
public ActionForward modifyDetailsAssignment(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException
{
String target =null;
String alertMessage=null;

if (!isTokenValid(req, true))
{
    target = new String("goToBeforeSearch");
}
else
{
		try
		{
			DynaBean pIMSForm = (DynaBean)form;
			PersonalInformation egpimsPersonalInformation = null;
			String id = req.getParameter("Id");
			if( id!=null&&!id.equals(""))
			{
				egpimsPersonalInformation = getEisManagr().getEmloyeeById(new Integer(id.trim()));
			}
			if(egpimsPersonalInformation!=null)
			{
				String[] assignmentId = req.getParameterValues("assignmentId");
				String fromDate[]=null;
				fromDate = (String[])pIMSForm.get("fromDate");
				String counter[]=req.getParameterValues("counter");
				int ArrLen = assignmentId.length;
				logger.info("ArrLen"+ArrLen);
				if(fromDate[0]!=null && !fromDate[0].equals(""))
				{
					for (int len = 0; len < ArrLen; len++)
					{
						if(fromDate[len]!=null && !fromDate[len].equals(""))
						{
							AssignmentPrd assignmentPrd = null;
							Assignment egEmpAssignment=null;
							if(fromDate[len]!=null && !fromDate[len].equals(""))
							{
								if(assignmentId[len].equals("0"))
								{
									assignmentPrd = new AssignmentPrd();
									egEmpAssignment= new Assignment();
									addAssignmentPrdForEmployee(assignmentPrd,egEmpAssignment,len,req,pIMSForm,egpimsPersonalInformation,counter[len]);
									assignmentPrd.setEmployeeId(egpimsPersonalInformation);
									getEisManagr().createAssignmentPrd(assignmentPrd);
									egEmpAssignment.setAssignmentPrd(assignmentPrd);
									getEisManagr().createAssignment(egEmpAssignment);
									getEisManagr().addAssignmentPrd(egpimsPersonalInformation,assignmentPrd);
									populateDepartment(egEmpAssignment,req,new Integer(len).toString());

								}
								else
								{
									assignmentPrd = getEisManagr().getAssignmentPrdById(new Integer(assignmentId[len]));
									egEmpAssignment =getassSetForPrd(assignmentPrd);
									addAssignmentPrdForEmployee(assignmentPrd,egEmpAssignment,len,req,pIMSForm,egpimsPersonalInformation,counter[len]);
									assignmentPrd.addAssignment(egEmpAssignment);
									assignmentPrd.setEmployeeId(egpimsPersonalInformation);
									egEmpAssignment.setAssignmentPrd(assignmentPrd);
									getEisManagr().updateAssignment(egEmpAssignment);
									getEisManagr().updateAssignmentPrd(assignmentPrd);
									getEisManagr().deleteEmpDepForAss(egEmpAssignment);
									populateDepartment(egEmpAssignment,req,new Integer(len).toString());

								}
							}
						}
					}
				}
			}
			HibernateUtil.getCurrentSession().flush();
			target = "success";
			alertMessage="Executed saving Assignment successfully";

	}catch(Exception ex)
		{
		   target = "error";
		   logger.info("Exception Encountered!!!"+ex.getMessage());
		   ex.printStackTrace();
		   HibernateUtil.rollbackTransaction();
		   throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
		}

}
logger.info("target"+target);
	req.setAttribute("alertMessage", alertMessage);
	return mapping.findForward(target);
}
*/
public void setAssignmentCreate(PIMSForm pIMSForm,PersonalInformation egpimsPersonalInformation,HttpServletRequest req)throws DuplicateElementException

{
		try
		{
				if(egpimsPersonalInformation!=null)
				{
					String fromDate[]=null;
					fromDate = (String[])pIMSForm.getFromDate();
					String isPrimary[] = (String[])pIMSForm.getIsPrimary();
					String[] checkHodDept=pIMSForm.getDepartmentIdOfHod();
					int ArrLen = fromDate.length;
					if(fromDate[0]!=null&&!fromDate[0].equals(""))
					{
						for (int len = 0; len < ArrLen; len++)
						{

							if(fromDate[len]!=null && !fromDate[len].equals(""))
							{
								LOGGER.debug("fromDate [len]"+fromDate[len]);
								AssignmentPrd egEmpAssignmentPrd = new AssignmentPrd();
								Assignment egEmpAssignment= new Assignment();
								addAssignmentPrdForEmployee(egEmpAssignmentPrd,egEmpAssignment,len,req,pIMSForm,Integer.valueOf(len).toString());
								egEmpAssignmentPrd.setEmployeeId(egpimsPersonalInformation);
								//getEisManagr().createAssignmentPrd(egEmpAssignmentPrd);
								egEmpAssignment.setAssignmentPrd(egEmpAssignmentPrd);
								employeeService.createAssignment(egEmpAssignment);
								egEmpAssignmentPrd.addAssignment(egEmpAssignment);
								LOGGER.info("egEmpAssignment"+egEmpAssignment.getId());
								String checkhod=req.getParameter("checkhod");

								if(checkHodDept!=null && checkHodDept.length>0 )
								{
									populateDepartment(egEmpAssignment,req,pIMSForm.getDepartmentIdOfHod()[len]);

								}
								if("Yes".equals(isPrimary[len])){
									egEmpAssignment.setIsPrimary('Y');
								}
								else if("No".equals(isPrimary[len])){
									egEmpAssignment.setIsPrimary('N');
								}
								egpimsPersonalInformation.addAssignmentPrd(egEmpAssignmentPrd);
							}

						}

					}
				}

	}
		catch(DuplicateElementException e)
		{
			throw e;
		}
		catch(Exception ex)
		{
			LOGGER.error(ex.getMessage());
		   	throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
		}
}

public void modifyAssignmentCreate(PIMSForm pIMSForm,PersonalInformation egpimsPersonalInformation,HttpServletRequest req)throws DuplicateElementException
{
	try
	{
				if(egpimsPersonalInformation!=null)
				{
					String fromDate[]=null;
					String isPrimary[]=null;
					fromDate = (String[])pIMSForm.getFromDate();
					String assignmentId[]=req.getParameterValues("assignmentId");
					isPrimary = (String[])pIMSForm.getIsPrimary();
					String[] checkHodDept=pIMSForm.getDepartmentIdOfHod();

					int ArrProMo  = assignmentId.length;
					//removing all the assignmentperiod from employee
					Set<AssignmentPrd> assignmentPrdSet=egpimsPersonalInformation.getEgpimsAssignmentPrd();
					Iterator<AssignmentPrd> assignmentPrdItr=assignmentPrdSet.iterator();
					while(assignmentPrdItr.hasNext())
					{
						AssignmentPrd assignmentprd=assignmentPrdItr.next();
						LOGGER.info("deleting assignmentprd"+assignmentprd.getId());
						assignmentPrdItr.remove();
					}

					if(fromDate[0]!=null&&!fromDate[0].equals(""))
					{
						for (int len = 0; len < ArrProMo; len++)
						{
							LOGGER.info("assignmentId"+assignmentId[len]);

							if(fromDate[len]!=null && !fromDate[len].equals(""))
							{
							if(assignmentId[len].equals("0"))
							{
								AssignmentPrd egEmpAssignmentPrd = new AssignmentPrd();
								Assignment egEmpAssignment= new Assignment();
								addAssignmentPrdForEmployee(egEmpAssignmentPrd,egEmpAssignment,len,req,pIMSForm,Integer.valueOf(len).toString());
								egEmpAssignmentPrd.setEmployeeId(egpimsPersonalInformation);
								//getEisManagr().createAssignmentPrd(egEmpAssignmentPrd);
								egEmpAssignment.setAssignmentPrd(egEmpAssignmentPrd);
								employeeService.createAssignment(egEmpAssignment);
								egEmpAssignmentPrd.addAssignment(egEmpAssignment);
								if("Yes".equals(isPrimary[len])){
									egEmpAssignment.setIsPrimary('Y');
								}
								else if("No".equals(isPrimary[len])){
									egEmpAssignment.setIsPrimary('N');
								}

								if(checkHodDept!=null && checkHodDept.length>0 )
								{
									populateDepartment(egEmpAssignment,req,pIMSForm.getDepartmentIdOfHod()[len]);

								}

								egpimsPersonalInformation.addAssignmentPrd(egEmpAssignmentPrd);
							}
							else
							{
								AssignmentPrd assignmentPrd = null;
								Assignment egEmpAssignment=null;
								egEmpAssignment = employeeService.getAssignmentById(Integer.valueOf(assignmentId[len]));
								assignmentPrd=egEmpAssignment.getAssignmentPrd();
								if ((assignmentPrd != null) && (egEmpAssignment != null)){
									addAssignmentPrdForEmployee(assignmentPrd,egEmpAssignment,len,req,pIMSForm,Integer.valueOf(len).toString());
									//assignmentPrd.addAssignment(egEmpAssignment);
									assignmentPrd.setEmployeeId(egpimsPersonalInformation);
									egEmpAssignment.setAssignmentPrd(assignmentPrd);
									if("Yes".equals(isPrimary[len])){
										egEmpAssignment.setIsPrimary('Y');
									}
									else if("No".equals(isPrimary[len])){
										egEmpAssignment.setIsPrimary('N');
									}
									//getEisManagr().updateAssignment(egEmpAssignment);
									//getEisManagr().updateAssignmentPrd(assignmentPrd);
									//getEisManagr().deleteEmpDepForAss(egEmpAssignment);//chk what is hte use of this
									Set<EmployeeDepartment> empDeptSet=egEmpAssignment.getDeptSet();
									Iterator iter =empDeptSet.iterator();
									EmployeeDepartment empdeptforAss=null;
									for(;iter.hasNext();)
									{
										empdeptforAss=(EmployeeDepartment)iter.next();
										iter.remove();
										egEmpAssignment.removeEmpDept(empdeptforAss);
										getCurrentSession().delete(empdeptforAss);
									}

									if(checkHodDept!=null && checkHodDept.length>0 )
									{
									populateDepartment(egEmpAssignment,req,pIMSForm.getDepartmentIdOfHod()[len]);

									}
									egpimsPersonalInformation.addAssignmentPrd(assignmentPrd);
								} else{
									LOGGER.error("Assignment Period not found for employee");
									throw new Exception("Assignment not Found");
								}

							}
							}


						}

					}
				}

	}
	catch(DuplicateElementException e)
	{
		throw e;
	}
	catch(Exception ex)
		{
		   LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}



}

public ActionForward saveDetailsDisciplinary(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException
{
	String target =null;
	String alertMessage=null;
	PIMSForm pIMSForm=(PIMSForm)form;
	DisciplinaryPunishment disciplinaryPunishment = new DisciplinaryPunishment();
	if (returnToken(req))
	{
	    target =STR_GOTO_BEFORE_SEARCH;
	}
	else
	{
		try
		{

			String id = req.getParameter("Id");
			PersonalInformation egpimsPersonalInformation =null;
			Integer userId =(Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
			EisManagersUtill.getUserService().getUserByID(userId);

			if( id!=null&&!id.equals("")){
				egpimsPersonalInformation = employeeService.getEmloyeeById(Integer.valueOf(id.trim()));
			}
		//	Set<DisciplinaryPunishment> disciplinaryPanishments = egpimsPersonalInformation.getEgpimsDisciplinaryPunishments();
			if(egpimsPersonalInformation!=null)
			{
				setDisplinaryFields(disciplinaryPunishment,req);
				disciplinaryPunishment.setApplicationNumber((String)pIMSForm.getApplicationNumber());
				disciplinaryPunishment.setEmployeeId(egpimsPersonalInformation);
				String[] enquiryOfficeName = null;
				enquiryOfficeName = req.getParameterValues("enquiryOfficeName");
				int arrLen = enquiryOfficeName.length;
				java.util.List employess = new ArrayList();
				for (int len = 0; len < arrLen; len++){
					PersonalInformation emp1 = employeeService.getEmloyeeById(Integer.parseInt(((String[])pIMSForm.getEmploid())[len]));
					employess.add(emp1);
				}
				if(arrLen>1){
					for (int len = 0; len < arrLen; len++){
						if(enquiryOfficeName[len]!=null && !enquiryOfficeName[len].equals("")){
							DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer = new DetOfEnquiryOfficer();
							setEOFields(egpimsDetOfEnquiryOfficer,len,req);
							//PersonalInformation emp1 = (PersonalInformation)employess.get(len);
							PersonalInformation emp1 = employeeService.getEmloyeeById(Integer.parseInt(((String[])pIMSForm.getEmploid())[len]));
							egpimsDetOfEnquiryOfficer.setEmployeeId(emp1);
							egpimsDetOfEnquiryOfficer.setDisciplinarypunishmentId(disciplinaryPunishment);
							emp1.getEgpimsDetailsEnquiryOfficers().add(egpimsDetOfEnquiryOfficer);
							//egpimsDetOfEnquiryOfficer=getEisManagr().createDetOfEnquiryOfficer(egpimsDetOfEnquiryOfficer);
							//HibernateUtil.getCurrentSession().flush();
							//emp1.getEgpimsDetailsEnquiryOfficers().add(egpimsDetOfEnquiryOfficer);
							//getEisManagr().updateEmloyee(emp1);
							//egpimsDetOfEnquiryOfficer=getEisManagr().createDetOfEnquiryOfficer(egpimsDetOfEnquiryOfficer);
						    //Use set
							disciplinaryPunishment.getEgpimsDetOfEnquiryOfficers().add(egpimsDetOfEnquiryOfficer);

							//getEisManagr().addDetOfEnquiryOfficer(disciplinaryPunishment,egpimsDetOfEnquiryOfficer);
						}
					}
				}
				else{
					 if(enquiryOfficeName[0]!=null&&!enquiryOfficeName[0].equals("")){
						DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer = new DetOfEnquiryOfficer();
						setEOFields(egpimsDetOfEnquiryOfficer,0,req);

						PersonalInformation emp1 = employeeService.getEmloyeeById(Integer.parseInt(((String[])pIMSForm.getEmploid())[0]));
						egpimsDetOfEnquiryOfficer.setEmployeeId(emp1);
						egpimsDetOfEnquiryOfficer.setDisciplinarypunishmentId(disciplinaryPunishment);
						emp1.getEgpimsDetailsEnquiryOfficers().add(egpimsDetOfEnquiryOfficer);
						disciplinaryPunishment.getEgpimsDetOfEnquiryOfficers().add(egpimsDetOfEnquiryOfficer);
					//	disciplinaryPunishment.getEgpimsDetOfEnquiryOfficers().add(egpimsDetOfEnquiryOfficer);
						//egpimsDetOfEnquiryOfficer=getEisManagr().createDetOfEnquiryOfficer(egpimsDetOfEnquiryOfficer);
						//disciplinaryPunishment.addDetOfEnquiryOfficer(egpimsDetOfEnquiryOfficer);
						//getEisManagr().addDetOfEnquiryOfficer(disciplinaryPunishment,egpimsDetOfEnquiryOfficer);
					}
				}
			}
			egpimsPersonalInformation.getEgpimsDisciplinaryPunishments().add(disciplinaryPunishment);
			employeeService.updateEmloyee(egpimsPersonalInformation);
			//disciplinaryPunishment.setEgpimsDetOfEnquiryOfficers(enquirySet);
			//getEisManagr().createDisciplinaryPunishment(disciplinaryPunishment);
			//getEisManagr().addDisciplinaryPunishment(egpimsPersonalInformation, disciplinaryPunishment);

			target = STR_SUCCESS;
			alertMessage="Executed saving Disciplinary successfully";

		}
		catch(Exception ex)
		{
			   target = STR_ERROR;
			   LOGGER.error(ex.getMessage());
			   throw new EGOVRuntimeException(ex.getMessage(),ex);
		}
	}
	req.setAttribute(STR_ALERT, alertMessage);
	return mapping.findForward(target);
}

public ActionForward modifyDetailsDisciplinary(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException
{
	String target =null;
	String alertMessage=null;
	if (returnToken(req))
	{
	    target = STR_GOTO_BEFORE_SEARCH;
	}
	else
	{
	try
	{
		DisciplinaryPunishment egpimsDisciplinaryPunishment = null;
		String id = req.getParameter("disiplinaryId").trim();

		if( id!=null&&!id.equals(""))
		{
			egpimsDisciplinaryPunishment =employeeService.getDisciplinaryPunishmentById(Integer.valueOf(id));
		}
		if(egpimsDisciplinaryPunishment!=null)
		{
			setDisplinaryFields(egpimsDisciplinaryPunishment,req);
			String[] idemployee = req.getParameterValues(STR_EMPLOID);
			String[] enquiryOfficeName = null;
			enquiryOfficeName =req.getParameterValues("enquiryOfficeName");
			int ArrLen =idemployee.length;

			Set set = (Set)req.getSession().getAttribute("DelSet");

			LOGGER.info("arg0"+set);
			if(set!=null&&!set.isEmpty())
			{
				DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer =null;
				for(Iterator iter = set.iterator();iter.hasNext();)
				{
					Integer idEoff = (Integer)iter.next();
					if(employeeService.getEnquiryOfficerById(idEoff)!=null)
					{
						LOGGER.info("arg0");
						egpimsDetOfEnquiryOfficer = employeeService.getEnquiryOfficerById(idEoff);
						egpimsDetOfEnquiryOfficer.setDisciplinarypunishmentId(egpimsDisciplinaryPunishment);
						LOGGER.info("arg0"+egpimsDetOfEnquiryOfficer);
						employeeService.deleteDetOfEnquiryOfficer(egpimsDetOfEnquiryOfficer);
					}
				}
			}
			if(enquiryOfficeName[0]!=null && !enquiryOfficeName[0].equals(""))
			{
				for (int len = 0; len < ArrLen; len++)
				{
					if(enquiryOfficeName[len]!=null && !enquiryOfficeName[len].equals(""))
					{

				DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer =null;
				if(idemployee[len].equals("0"))
				{

					egpimsDetOfEnquiryOfficer = new DetOfEnquiryOfficer();
					egpimsDetOfEnquiryOfficer.setDisciplinarypunishmentId(egpimsDisciplinaryPunishment);
					setEOFields(egpimsDetOfEnquiryOfficer,len,req);
					employeeService.createDetOfEnquiryOfficer(egpimsDetOfEnquiryOfficer);
					employeeService.addDetOfEnquiryOfficer(egpimsDisciplinaryPunishment,egpimsDetOfEnquiryOfficer);
				}
				else if(idemployee[len] != null && !idemployee[len].equals("0") && !idemployee[len].equals(""))
				{

					egpimsDetOfEnquiryOfficer = employeeService.getEnquiryOfficerById(Integer.valueOf(idemployee[len]));
					egpimsDetOfEnquiryOfficer.setDisciplinarypunishmentId(egpimsDisciplinaryPunishment);
					setEOFields(egpimsDetOfEnquiryOfficer,len,req);
					employeeService.updateDetOfEnquiryOfficer(egpimsDetOfEnquiryOfficer);

				}
					}
				}
			}
			employeeService.updateDisciplinaryPunishment(egpimsDisciplinaryPunishment);
		}

		target = STR_SUCCESS;
		alertMessage="Executed saving Disciplinary successfully";
	}catch(Exception ex)
	{
	   target = STR_ERROR;
	   LOGGER.error(ex.getMessage());
	   HibernateUtil.rollbackTransaction();
	   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
	}
	}
	req.setAttribute(STR_ALERT, alertMessage);
	return mapping.findForward(target);

}

public ActionForward saveDisciplinaryApproval(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException
{
	String target =null;
	String alertMessage=null;
	Integer userId =(Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
	User user = EisManagersUtill.getUserService().getUserByID(userId);
	DisciplinaryPunishmentApproval disciplinaryPunishmentApproval = new DisciplinaryPunishmentApproval();
	PIMSForm disciplinaryAppForm = (PIMSForm)form;
	String disciplinaryId = disciplinaryAppForm.getDispId();
	DisciplinaryPunishment disciplinaryPunishment = employeeService.getDisciplinaryPunishmentById(Integer.valueOf(disciplinaryId));
	if (returnToken(req))
	{
	    target = STR_SUCCESS;
	}
	else
	{
	try
	{
		disciplinaryPunishmentApproval.setSanctionNo(getSanctionNo(disciplinaryPunishment.getEmployeeId()));
		disciplinaryPunishmentApproval.setDisciplinaryPunishmentId(disciplinaryPunishment);
		disciplinaryPunishmentApproval.setApprovedBy(user);
		StatusMaster statusMaster =new StatusMaster();
		statusMaster.setId(Integer.valueOf(disciplinaryAppForm.getStatusId()));
		//disciplinaryPunishment.setStatusId(statusMaster);
		employeeService.addDisiplinaryApproval(disciplinaryPunishmentApproval);
		//HibernateUtil.getCurrentSession().flush();
		req.setAttribute("disciplinaryPunishmentApproval", disciplinaryPunishmentApproval.getSanctionNo());
		target = "successApproval";
		alertMessage="Executed saving LeaveApproval successfully";

}catch(Exception ex)
	{
	   target = STR_ERROR;
       LOGGER.error(ex.getMessage());
	   HibernateUtil.rollbackTransaction();
	   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
	}
	}
	req.setAttribute(STR_ALERT, alertMessage);
	return mapping.findForward(target);
}
public ActionForward saveAvailedParticulars(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException
{
	String target =null;
	String alertMessage=null;
	if (returnToken(req))
	{
	    target = STR_GOTO_BEFORE_SEARCH;
	}
	else
	{
	try
	{
			String id = req.getParameter("Id");
			PersonalInformation egpimsPersonalInformation =null;
			if( id!=null&&!id.equals(""))
			{
				egpimsPersonalInformation = employeeService.getEmloyeeById(Integer.valueOf(id.trim()));
			}
			if(egpimsPersonalInformation!=null)
			{
				PIMSForm ltcForm = (PIMSForm)form;
				String[] bYear = null;
				bYear = req.getParameterValues("bYear");
				int ArrLen = bYear.length;

				if(bYear[0]!=null && !bYear[0].equals(""))
				{
					for (int len = 0; len < ArrLen; len++)
					{
						if(bYear[len]!=null && !bYear[len].equals(""))
						{

							LtcPirticulars egpimsLtcPirticulars = new LtcPirticulars();
							egpimsLtcPirticulars.setEmployeeId(egpimsPersonalInformation);
							setLtcPirticularsFields(egpimsLtcPirticulars,ltcForm,len,req);
							employeeService.addLtcPirticulars(egpimsPersonalInformation,egpimsLtcPirticulars);
						}
					}
				}
			}
					//HibernateUtil.getCurrentSession().flush();
					target = STR_SUCCESS;
					alertMessage="Executed saving AvailedParticulars successfully";

}catch(Exception ex)
	{
	   target = STR_ERROR;
       LOGGER.error(ex.getMessage());
	   HibernateUtil.rollbackTransaction();
	   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
	}
	}
	req.setAttribute(STR_ALERT, alertMessage);
	return mapping.findForward(target);
}

public ActionForward modifyAvailedParticulars(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException
{
String target =null;
String alertMessage=null;
if (returnToken(req))
{
    target = STR_GOTO_BEFORE_SEARCH;
}
else
{
	try
	{
		PIMSForm ltcForm = (PIMSForm)form;
		PersonalInformation egpimsPersonalInformation = null;
		String id = req.getParameter("Id");
		if( id!=null&&!id.equals(""))
		{
			egpimsPersonalInformation = employeeService.getEmloyeeById(Integer.valueOf(id.trim()));
		}
		if(egpimsPersonalInformation!=null)
		{
			String[] tpId = req.getParameterValues("tpId");
			String[] bYear = null;
			bYear = req.getParameterValues("bYear");
			int ArrLen = tpId.length;

			if(bYear[0]!=null && !bYear[0].equals(""))
			{
				for (int len = 0; len < ArrLen; len++)
				{
					if(bYear[len]!=null && !bYear[len].equals(""))
					{

						LtcPirticulars egpimsLtcPirticulars = null;
						if(tpId[len].equals("0"))
						{
							egpimsLtcPirticulars = new LtcPirticulars();
							egpimsLtcPirticulars.setEmployeeId(egpimsPersonalInformation);
							setLtcPirticularsFields(egpimsLtcPirticulars,ltcForm,len,req);
							employeeService.addLtcPirticulars(egpimsPersonalInformation,egpimsLtcPirticulars);
						}
						else
						{
							egpimsLtcPirticulars=employeeService.getLtcPirticularsById(Integer.valueOf(tpId[len]));
							setLtcPirticularsFields(egpimsLtcPirticulars,ltcForm,len,req);
							employeeService.updateLtcPirticulars(egpimsLtcPirticulars);
						}

					}

				}
			}
		}
		//HibernateUtil.getCurrentSession().flush();
		target = STR_SUCCESS;
		alertMessage="Executed saving AvailedParticulars successfully";

}catch(Exception ex)
	{
	   target = STR_ERROR;
       LOGGER.error(ex.getMessage());
	   HibernateUtil.rollbackTransaction();
	   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
	}
}
	req.setAttribute(STR_ALERT, alertMessage);
	return mapping.findForward(target);
}

public ActionForward saveTraningPirticulars(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException
{
	String target =null;
	String alertMessage=null;
	if (returnToken(req))
	{
	    target = STR_GOTO_BEFORE_SEARCH;
	}
	else
	{
	try
	{
			String id = req.getParameter("Id");
			PersonalInformation egpimsPersonalInformation =null;
			if( id!=null&&!id.equals(""))
			{

				egpimsPersonalInformation = employeeService.getEmloyeeById(Integer.valueOf(id.trim()));
			}
			if(egpimsPersonalInformation!=null)
			{
				PIMSForm training = (PIMSForm)form;
				String[] course = null;
				course=(String[])training.getCourse();
				int ArrLen = course.length;

				if(course[0]!=null && !course[0].equals(""))
				{
					for (int len = 0; len < ArrLen; len++)
					{
						if(course[len]!=null && !course[len].equals(""))
						{

							TrainingPirticulars egpimsTrainingPirticulars = new TrainingPirticulars();
							egpimsTrainingPirticulars.setEmployeeId(egpimsPersonalInformation);
							setTrainingPirticularsFields(egpimsTrainingPirticulars,training,len);
							employeeService.addTrainingPirticularses(egpimsPersonalInformation,egpimsTrainingPirticulars);
						}
					}
				}
			}
				//HibernateUtil.getCurrentSession().flush();
				target = STR_SUCCESS;
				alertMessage="Executed saving TraningPirticulars successfully";
	}catch(Exception ex)
	{
	   target = STR_ERROR;
	   LOGGER.error(ex.getMessage());
	   HibernateUtil.rollbackTransaction();
	   throw new EGOVRuntimeException(ex.getMessage(),ex);
	}
	}
	req.setAttribute(STR_ALERT, alertMessage);
	return mapping.findForward(target);
}

public ActionForward modifyTraningPirticulars(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
throws IOException,ServletException
{
	String target =null;
	String alertMessage=null;
	if (returnToken(req))
	{
	    target = STR_GOTO_BEFORE_SEARCH;
	}
	else
	{
	try
	{
		PIMSForm training = (PIMSForm)form;
		PersonalInformation egpimsPersonalInformation = null;
		String id = req.getParameter("Id");
		if( id!=null&&!id.equals(""))
		{
			egpimsPersonalInformation = employeeService.getEmloyeeById(Integer.valueOf(id.trim()));
		}
		if(egpimsPersonalInformation!=null)
		{
			String[] tpId = req.getParameterValues("tpId");
			String[] course = null;
			course = (String[])training.getCourse();
			int ArrLen = tpId.length;
			if(course[0]!=null && !course[0].equals(""))
			{
				for (int len = 0; len < ArrLen; len++)
				{
					if(course[len]!=null && !course[len].equals(""))
					{


						TrainingPirticulars egpimsTrainingPirticulars = null;
						if(tpId[len].equals("0"))
						{
							egpimsTrainingPirticulars = new TrainingPirticulars();
							egpimsTrainingPirticulars.setEmployeeId(egpimsPersonalInformation);
							setTrainingPirticularsFields(egpimsTrainingPirticulars,training,len);
							employeeService.addTrainingPirticularses(egpimsPersonalInformation,egpimsTrainingPirticulars);
						}
						else
						{
							egpimsTrainingPirticulars= employeeService.getTrainingPirticularsById(Integer.valueOf(tpId[len]));
							setTrainingPirticularsFields(egpimsTrainingPirticulars,training,len);
							employeeService.updateTrainingPirticulars(egpimsTrainingPirticulars);
						}
					}
				}
			}
			Set delSet=(Set)req.getSession().getAttribute("tranningObjSet");
			if(delSet!=null)
			{
				for(Iterator it=delSet.iterator();it.hasNext();)
				{
				    Integer trainningId=(Integer)it.next();
				    TrainingPirticulars trainingPirticularObj=employeeService.getTrainingPirticularsById(trainningId);
				    if(trainingPirticularObj!=null)
				    {
				    	egpimsPersonalInformation.removeTrainingPirticularses(trainingPirticularObj);
				    }


				}
	         }
			req.getSession().removeAttribute("tranningObjSet");
		}
		//HibernateUtil.getCurrentSession().flush();
		target = STR_SUCCESS;
		alertMessage="Executed saving TraningPirticulars successfully";
}catch(Exception ex)
	{
	   target = STR_ERROR;
       LOGGER.error(ex.getMessage());
	   HibernateUtil.rollbackTransaction();
	   throw new EGOVRuntimeException(ex.getMessage(),ex);
	}
	}
	req.setAttribute(STR_ALERT, alertMessage);
	return mapping.findForward(target);

}

	private Map getMapOfAddress(PersonalInformation egpimsPersonalInformation)
	{
		Map map =new HashMap();

		try {
			Set setofadd = egpimsPersonalInformation.getEgpimsPersonAddresses();

			if (setofadd != null) {
				Iterator ietr = setofadd.iterator();

				while(ietr.hasNext())
				{
					PersonAddress egpimsPersonAddresses =(PersonAddress)ietr.next();
					Address add = egpimsPersonAddresses.getPersonAddress();
					AddressTypeMaster addTypeMaster= add.getAddTypeMaster();
					String addstr=null;
					if(addTypeMaster!=null && addTypeMaster.getAddressTypeName()!=null)
					{
						addstr = addTypeMaster.getAddressTypeName();
						map.put(addstr,add);
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);

		}
		return map;
	}
	private Session getCurrentSession()
	{
	    try {
			HibernateUtil.beginTransaction();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	    return HibernateUtil.getCurrentSession();
	}
	private java.util.Date getDateString(String dateString)
	{

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		java.util.Date d = null;
		try
		{
			d = dateFormat.parse(dateString);

		} catch (Exception e)
		{
			LOGGER.debug(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		return d;
}

	private void setDetailsOfpimsForCreateAndModify(PersonalInformation egpimsPersonalInformation,PIMSForm pIMSForm,UserImpl user)
	{

		try {
			if(pIMSForm.getEmployeeCode()!=null&&!((String)pIMSForm.getEmployeeCode()).equals(""))
			{
				egpimsPersonalInformation.setEmployeeCode((String)pIMSForm.getEmployeeCode().toUpperCase());
			}
			if(pIMSForm.getEmployeeDob()!=null&&!((String)pIMSForm.getEmployeeDob()).equals(""))
			{
				egpimsPersonalInformation.setDateOfBirth(getDateString((String)pIMSForm.getEmployeeDob()));
			}
			if(pIMSForm.getFirstName()!=null && !((String)pIMSForm.getFirstName()).equalsIgnoreCase(FIRSTNAME))
			{
				egpimsPersonalInformation.setEmployeeFirstName((String)pIMSForm.getFirstName());
				if(pIMSForm.getUserFirstName()!=null&&!pIMSForm.getUserFirstName().equalsIgnoreCase(""))
				{
					user.setFirstName((String)pIMSForm.getFirstName());
				}	
			}
			if(pIMSForm.getMiddleName()!=null && !((String)pIMSForm.getMiddleName()).equalsIgnoreCase(MIDDLENAME))
			{
				egpimsPersonalInformation.setEmployeeMiddleName((String)pIMSForm.getMiddleName());
				if(pIMSForm.getUserFirstName()!=null&&!pIMSForm.getUserFirstName().equalsIgnoreCase(""))
				{
					user.setMiddleName((String)pIMSForm.getUserMiddleName());
				}
			}
			if(pIMSForm.getDeathDate()!=null && !((String)pIMSForm.getDeathDate()).equals(""))
			{
				egpimsPersonalInformation.setDeathDate(getDateString((String)pIMSForm.getDeathDate()));
			}
			else
			{
				egpimsPersonalInformation.setDeathDate(null);
			}
			if(pIMSForm.getLastName()!=null&&!((String)pIMSForm.getLastName()).equalsIgnoreCase(LASTNAME))
			{
				egpimsPersonalInformation.setEmployeeLastName((String)pIMSForm.getLastName());
				if(pIMSForm.getUserFirstName()!=null&&!pIMSForm.getUserFirstName().equalsIgnoreCase(""))
				{
					user.setLastName((String)pIMSForm.getUserLastName());
				}
			}
			if(!((String)pIMSForm.getMiddleName()).equalsIgnoreCase(MIDDLENAME))
			{
				egpimsPersonalInformation.setEmployeeName((String)pIMSForm.getFirstName()+" "+(String)pIMSForm.getMiddleName());
				if(!((String)pIMSForm.getLastName()).equalsIgnoreCase(LASTNAME))
				{
					egpimsPersonalInformation.setEmployeeName(egpimsPersonalInformation.getEmployeeName()+" "+(String)pIMSForm.getLastName());
				}
			}
			else if(!((String)pIMSForm.getLastName()).equalsIgnoreCase(LASTNAME))
			{
				egpimsPersonalInformation.setEmployeeName((String)pIMSForm.getFirstName()+" "+(String)pIMSForm.getLastName());
				if(!((String)pIMSForm.getMiddleName()).equalsIgnoreCase(MIDDLENAME))
				{
					egpimsPersonalInformation.setEmployeeName(egpimsPersonalInformation.getEmployeeName()+" "+(String)pIMSForm.getMiddleName());
				}
			}
			else
			{
				egpimsPersonalInformation.setEmployeeName((String)pIMSForm.getFirstName());
			}
			if(pIMSForm.getFatherfirstName()!=null&&!((String)pIMSForm.getFatherfirstName()).equalsIgnoreCase(FIRSTNAME))
			{
				egpimsPersonalInformation.setFatherHusbandFirstName((String)pIMSForm.getFatherfirstName());
			}
			if(pIMSForm.getFathermiddleName()!=null&&!((String)pIMSForm.getFathermiddleName()).equalsIgnoreCase(MIDDLENAME))
			{
				egpimsPersonalInformation.setFatherHusbandMiddleName((String)pIMSForm.getFathermiddleName());
			}
			if(pIMSForm.getFatherlastName()!=null&&!((String)pIMSForm.getFatherlastName()).equalsIgnoreCase(LASTNAME))
			{
				egpimsPersonalInformation.setFatherHusbandLastName((String)pIMSForm.getFatherlastName());
			}
			if(pIMSForm.getPanNumber()!=null&&!((String)pIMSForm.getPanNumber()).equals(""))
			{
				egpimsPersonalInformation.setPanNumber((String)pIMSForm.getPanNumber());
			}
			if(pIMSForm.getDateOfFA()!=null&&!((String)pIMSForm.getDateOfFA()).equals(""))
			{
				egpimsPersonalInformation.setDateOfFirstAppointment(getDateString((String)pIMSForm.getDateOfFA()));
			}
			/*
			 * Deputation Date
			 */

			if(pIMSForm.getDateOfjoin()!=null&&!((String)pIMSForm.getDateOfjoin()).equals(""))
			{
				egpimsPersonalInformation.setDateOfjoin(getDateString((String)pIMSForm.getDateOfjoin()));
			}
			/*
			 * date of retirement shifted from eisEmployeeGrade to eg_employee
			 */

			if(pIMSForm.getDateOfRetirement()!=null&&!((String)pIMSForm.getDateOfRetirement()).equals(""))
			{
				egpimsPersonalInformation.setRetirementDate(getDateString((String)pIMSForm.getDateOfRetirement()));
			}
			if(pIMSForm.getRetirementAge()!=null&&!((String)pIMSForm.getRetirementAge()).equals(""))
			{
				egpimsPersonalInformation.setRetirementAge(Integer.valueOf((String)pIMSForm.getRetirementAge()));
			}
			if(pIMSForm.getMotherTounge()!=null&&!((String)pIMSForm.getMotherTounge()).equals(""))
			{
				egpimsPersonalInformation.setMotherTonuge((String)pIMSForm.getMotherTounge());
			}
			if(!((String)pIMSForm.getStatusMaster()).equals("0"))
			{
				GenericMaster genericMaster = (GenericMaster)employeeService.getGenericMaster(Integer.valueOf((String)pIMSForm.getStatusMaster()).intValue(),"EmployeeStatusMaster");

				EmployeeStatusMaster employeeStatusMaster =(EmployeeStatusMaster)genericMaster;
				if(employeeStatusMaster!=null)
				{
				egpimsPersonalInformation.setEmployeeTypeMaster(employeeStatusMaster);
			     }
			}
			if(!"".equals((String)pIMSForm.getStatusTypeId()))
			{
				EgwStatus statEgwMaster = (EgwStatus)commonsService.findEgwStatusById(Integer.parseInt((String)pIMSForm.getStatusTypeId()));

				if(statEgwMaster!=null)
				{
				egpimsPersonalInformation.setStatusMaster(statEgwMaster);
				}
			}
			
			
			
			if((String)pIMSForm.getReligionId()!=null &&  !((String)pIMSForm.getReligionId()).equals("0"))
			{

				GenericMaster genericMaster = (GenericMaster)employeeService.getGenericMaster(Integer.valueOf((String)pIMSForm.getReligionId()).intValue(),"ReligionMaster");
				ReligionMaster religionMaster =(ReligionMaster)genericMaster;
				if(religionMaster!=null)
				{
				egpimsPersonalInformation.setReligionMstr(religionMaster);
				}
			}
			if((String)pIMSForm.getBloodGroup()!=null &&  !((String)pIMSForm.getBloodGroup()).equals("0"))
			{
				GenericMaster genericMaster = (GenericMaster)employeeService.getGenericMaster(Integer.valueOf((String)pIMSForm.getBloodGroup()).intValue(),"BloodGroupMaster");
				BloodGroupMaster bloodGroupMaster =(BloodGroupMaster)genericMaster;
				if(bloodGroupMaster!=null)
				{
				  egpimsPersonalInformation.setBloodGroupMstr(bloodGroupMaster);
				}
			}
			if((String)pIMSForm.getCommunityId()!=null && !((String)pIMSForm.getCommunityId()).equals("0"))
			{
				GenericMaster genericMaster = (GenericMaster)employeeService.getGenericMaster(Integer.valueOf((String)pIMSForm.getCommunityId()).intValue(),"CommunityMaster");
				CommunityMaster communityMaster =(CommunityMaster)genericMaster;
				if(communityMaster!=null)
				{
				egpimsPersonalInformation.setCommunityMstr(communityMaster);
				}
			}
			if(pIMSForm.getGender()!= null && !((String)pIMSForm.getGender()).equals("0"))
			{
					 if(((String)pIMSForm.getGender()).equals("M"))
					 {
						egpimsPersonalInformation.setGender(Character.valueOf('M'));
					 }
					else
					{
						egpimsPersonalInformation.setGender(Character.valueOf('F'));
					}
			}
			else
			{
				egpimsPersonalInformation.setGender(Character.valueOf('0'));
			}
			if(((String)pIMSForm.getPhand()).equals("0"))
			{
				egpimsPersonalInformation.setIsHandicapped(Character.valueOf('0'));
			}
			else
			{
				egpimsPersonalInformation.setIsHandicapped(Character.valueOf('1'));
			}

			if(((String)pIMSForm.getIsMed()).equals("0"))
			{
				egpimsPersonalInformation.setIsMedReportAvailable(Character.valueOf('0'));
			}
			 else
			 {
				egpimsPersonalInformation.setIsMedReportAvailable(Character.valueOf('1'));
			 }


			String identMarks1 = " ";
			String identMarks2 = " ";
			if(pIMSForm.getIdentificationMarks1()!=null && !((String)pIMSForm.getIdentificationMarks1()).equals(""))
			{
				identMarks1 = (String)pIMSForm.getIdentificationMarks1();

			}
			if(pIMSForm.getIdentificationMarks2()!=null && !((String)pIMSForm.getIdentificationMarks2()).equals(""))
			{

					identMarks2 = (String)pIMSForm.getIdentificationMarks2();

			}

			egpimsPersonalInformation.setIdentificationMarks1(identMarks1);
			egpimsPersonalInformation.setIdentificationMarks2(identMarks2);

			if((String)pIMSForm.getTamillangaugequlified()!=null && !((String)pIMSForm.getTamillangaugequlified()).equals("0"))
			{
				GenericMaster genericMaster = (GenericMaster)employeeService.getGenericMaster(Integer.valueOf((String)pIMSForm.getTamillangaugequlified()).intValue(),"LanguagesQulifiedMaster");
				LanguagesQulifiedMaster languagesQulifiedMaster =(LanguagesQulifiedMaster)genericMaster;
				if(languagesQulifiedMaster!=null)
				{
				egpimsPersonalInformation.setLangQulMstr(languagesQulifiedMaster);
				}

			}

			if(pIMSForm.getBasic()!=null&&!((String)pIMSForm.getBasic()).equals(""))
			{
				egpimsPersonalInformation.setBasicPay(new BigDecimal((String)pIMSForm.getBasic()));
			}
			if(pIMSForm.getSpl()!=null&&!((String)pIMSForm.getSpl()).equals(""))
			{
				egpimsPersonalInformation.setSplPay(new BigDecimal((String)pIMSForm.getSpl()));
			}
			if(pIMSForm.getPpSg()!=null&&!((String)pIMSForm.getPpSg()).equals(""))
			{
				egpimsPersonalInformation.setPpSgppPay(new BigDecimal((String)pIMSForm.getPpSg()));
			}
			
			if( pIMSForm.getEmpGrpMstr()!=null && !((String)pIMSForm.getEmpGrpMstr()).equals("0") )
			{
				EmployeeGroupMaster grpCatMstr = (EmployeeGroupMaster)employeeService.getGenericMaster(Integer.valueOf(pIMSForm.getEmpGrpMstr()),"EmployeeGroupMaster");				
				egpimsPersonalInformation.setGroupCatMstr(grpCatMstr);
			}


		} catch (EGOVRuntimeException e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}


	private void setAdressForPresent(Address objAddress,PIMSForm pIMSForm,HttpServletRequest req, String mode)
	{
		try {
			String pre = (String)req.getParameter("streetNamepre");
			String propertyNopre = (String)req.getParameter(STR_PROPERTYNPPRE);

			if(objAddress.getAddTypeMaster()==null)
			{
				AddressTypeMaster objAddressTypepre =null;
				AddressTypeDAO addressTypeDAO = GenericDaoFactory.getDAOFactory().getAddressTypeDao();
				objAddressTypepre =		addressTypeDAO.getAddressType("PRESENTADDRESS");
				objAddress.setAddTypeMaster(objAddressTypepre);
			}
			if(mode.equals(MODULE_CREATE))
			{
				if(propertyNopre!=null && !propertyNopre.equals(""))
				{
					objAddress.setStreetAddress1(propertyNopre);
				}
				if (pre!=null && !pre.equals(""))
				{
					objAddress.setStreetAddress2(pre);
				}
			}
			else if(STR_MODIFY.equals(mode))
			{
				if(propertyNopre!=null)
				{
					objAddress.setStreetAddress1(propertyNopre);
				}
				if(pre!=null)
				{
					objAddress.setStreetAddress2(pre);
				}
			}

			if(pIMSForm.getLocalitypre()!=null && !(((String)pIMSForm.getLocalitypre()).equals("")))
			{
				objAddress.setLocality((String)pIMSForm.getLocalitypre());

			}
			if(pIMSForm.getCitypre()!=null && !(((String)pIMSForm.getCitypre()).equals("")))
			{

					objAddress.setCityTownVillage((String)pIMSForm.getCitypre());

			}
			if(pIMSForm.getPinCodepre()!=null && !((String)pIMSForm.getPinCodepre()).equals("null") && !((String)pIMSForm.getPinCodepre()).equals(""))
			{

					objAddress.setPinCode(Integer.valueOf((String)pIMSForm.getPinCodepre()));

			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}

	private void setAdressForPermenent(Address objAddressper,PIMSForm pIMSForm,HttpServletRequest req,String mode)
	{
		try {
			String per = (String)req.getParameter("streetNameper");
			String permanentAddr = (String)req.getParameter(STR_PROPERTYNOPER);

			if(objAddressper.getAddTypeMaster() != null)
			{
				//objAddressper = new Address();
				AddressTypeMaster objAddressTypeper =new AddressTypeMaster();
				AddressTypeDAO addressTypeDAO = GenericDaoFactory.getDAOFactory().getAddressTypeDao();
				objAddressTypeper =		addressTypeDAO.getAddressType(PERMANENTADDRESS);

				objAddressper.setAddTypeMaster(objAddressTypeper);
			}

			if( pIMSForm.getPropertyNoper()!=null && !((String)pIMSForm.getPropertyNoper()).equals(""))
			{


			objAddressper.setStreetAddress1((String)pIMSForm.getPropertyNoper());
			}
			if(mode.equals(MODULE_CREATE))
			{
				if((per!=null && !per.equals("") )|| ( permanentAddr!=null && !permanentAddr.equals("")))
				{
					AddressTypeMaster objAddressTypeper =new AddressTypeMaster();
					AddressTypeDAO addressTypeDAO = GenericDaoFactory.getDAOFactory().getAddressTypeDao();
					objAddressTypeper =		addressTypeDAO.getAddressType(PERMANENTADDRESS);
					objAddressper.setAddTypeMaster(objAddressTypeper);
					objAddressper.setStreetAddress2(per);
				}
			}
			else if(STR_MODIFY.equals(mode))
			{
				AddressTypeMaster objAddressTypeper =new AddressTypeMaster();
				AddressTypeDAO addressTypeDAO = GenericDaoFactory.getDAOFactory().getAddressTypeDao();
				objAddressTypeper =		addressTypeDAO.getAddressType(PERMANENTADDRESS);
				objAddressper.setAddTypeMaster(objAddressTypeper);


				if(per!=null)
				{
					objAddressper.setStreetAddress2(per);

				}
				if(pIMSForm.getPropertyNoper()!=null)
				{
					objAddressper.setStreetAddress1((String)pIMSForm.getPropertyNoper());
				}
			}
			if(pIMSForm.getLocalityper()!=null && !(((String)pIMSForm.getLocalityper()).equals("")))
			{
				objAddressper.setLocality((String)pIMSForm.getLocalityper());

			}
			if(pIMSForm.getCityper()!=null && !(((String)pIMSForm.getCityper()).equals("")))
			{
				objAddressper.setCityTownVillage((String)pIMSForm.getCityper());

			}
			if(pIMSForm.getPinCodeper()!=null &&!((String)pIMSForm.getPinCodeper()).equals("null") && !((String)pIMSForm.getPinCodeper()).equals(""))
			{

					objAddressper.setPinCode(Integer.valueOf((String)pIMSForm.getPinCodeper()));

			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}


	}


	private void addAssignmentPrdForEmployee(AssignmentPrd egEmpAssignmentPrd,Assignment egEmpAssignment,int len,HttpServletRequest req,PIMSForm pIMSForm,String counter)throws DuplicateElementException
	{
		try {
			String fromDate[]=null;
			String toDate[]=null;
			String fundId[]=null;

			String gradeId[]=null;

			String functionId[]=null;
			String designationId[]=null;
			String functionaryId[]=null;
			String ptcallocation[]=null;
			String assignmentOrderNo[]=null;
			fromDate = (String[])pIMSForm.getFromDate();
			toDate = (String[])pIMSForm.getToDate();
			fundId = (String[])pIMSForm.getFundId();
			/*
			 * Grade Changes
			 */
			gradeId = (String[])pIMSForm.getGradeId();

			functionId = (String[])pIMSForm.getFunctionId();
			designationId = (String[])pIMSForm.getDesgId();
			functionaryId = (String[])pIMSForm.getFunctionaryId();
			ptcallocation = (String[])pIMSForm.getPtcallocation();
			/*
			 * govt order no for each assignment
			 */
			assignmentOrderNo = (String[])pIMSForm.getAssignmentOrderNo();

			String posId[]=req.getParameterValues("posId");
			String[] mainDept = req.getParameterValues("mainDepartmentId");
			int mainD = Integer.valueOf(mainDept[Integer.valueOf(counter)]);



			egEmpAssignmentPrd.setFromDate(getDateString(fromDate[len]));
			if(toDate[len]!=null&&!toDate[len].equals(""))
			{
				egEmpAssignmentPrd.setToDate(getDateString(toDate[len]));
			}



/*
				if(!designationId[len].equals("0"))
				{
					if(len!=0)
					{
						if(!designationId[len].equals(designationId[len-1]))
						{
							try
							{
								EisManager eisManager = EisManagersUtill.getEisManagr();
								DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
								ExceptionManager exceptionManager = PayrollManagersUtill.getExceptionManager();
								CommonsManager commonManager = EisManagersUtill.getEisCommonManagr();
								UserManager userManager = EisManagersUtill.getUserManager();
								HttpSession session = req.getSession();
								String userName =(String) session.getAttribute("com.egov.user.LoginUserName");
								User user = userManager.getUserByUserName(userName);
								EgwStatus status = commonManager.getStatusByModuleAndDescription(PayrollConstants.EMP_EXCEPTION_MODILE,PayrollConstants.EMP_EXCEPTION_CREATED_STATUS);
								EmpException empException = new EmpException();
								empException.setEmployee(personalInformation);
								empException.setExceptionMstr(exceptionManager.getExceptionMstrById(new Integer(EisConstants.INT_TYPE_DESIGNATION)));
								empException.setComments("designation changed from "+designationMasterDAO.getDesignationMaster(new Integer(designationId[len-1]).intValue()).getDesignationName()+" to "+designationMasterDAO.getDesignationMaster(new Integer(designationId[len]).intValue()).getDesignationName());
								java.util.Date dte = getDateString(fromDate[len]);
								empException.setFinancialyear(commonManager.findFinancialYearById(new Long(commonManager.getFinancialYearId((sdf.format(dte))))));
								empException.setMonth(new BigDecimal(dte.getMonth()+1));
								empException.setStatus(status);
								empException.setUserid(user);
								exceptionManager.checkAndCreateEmpException(empException);
							}catch(Exception ex)
							{
							    ex.printStackTrace();
							    HibernateUtil.rollbackTransaction();
							    throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
							}
						}
					}
					else
					{
						if("modify".equals((String)req.getSession().getAttribute("viewMode")))
						{
							if(!egEmpAssignment.getDesigId().getDesignationId().equals(new Integer(designationId[len])))
							{
								//exception
								try
								{

									DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
									ExceptionManager exceptionManager = PayrollManagersUtill.getExceptionManager();
									CommonsManager commonManager = EisManagersUtill.getEisCommonManagr();
									UserManager userManager = EisManagersUtill.getUserManager();
									HttpSession session = req.getSession();
									String userName =(String) session.getAttribute("com.egov.user.LoginUserName");
									User user = userManager.getUserByUserName(userName);
									EgwStatus status = commonManager.getStatusByModuleAndDescription(PayrollConstants.EMP_EXCEPTION_MODILE,PayrollConstants.EMP_EXCEPTION_CREATED_STATUS);
									EmpException empException = new EmpException();
									empException.setEmployee(personalInformation);
									empException.setExceptionMstr(exceptionManager.getExceptionMstrById(new Integer(EisConstants.INT_TYPE_DESIGNATION)));
									empException.setComments("designation changed from "+designationMasterDAO.getDesignationMaster(egEmpAssignment.getDesigId().getDesignationId()).getDesignationName()+" to "+designationMasterDAO.getDesignationMaster(new Integer(designationId[len]).intValue()).getDesignationName());
									java.util.Date dte = getDateString(fromDate[len]);
									empException.setFinancialyear(commonManager.findFinancialYearById(new Long(commonManager.getFinancialYearId((sdf.format(dte))))));
									empException.setMonth(new BigDecimal(dte.getMonth()+1));
									empException.setStatus(status);
									empException.setUserid(user);
									exceptionManager.checkAndCreateEmpException(empException);
								}catch(Exception ex)
								{
									logger.info("Exception Encountered!!!"+ex.getMessage());
								    ex.printStackTrace();
								    HibernateUtil.rollbackTransaction();
								    throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
								}
							}
						}
					}
				}
*/
				if(!posId[len].equals("0"))
				{
					PositionMasterDAO  positionMasterDAO = new PositionMasterDAO();
					Position  pos  = new Position();
					if(!"undefined".equals(posId[len]) && posId[len] != "")
					{
						pos = positionMasterDAO.getPosition(Integer.valueOf(posId[len]).intValue());
					}
					else
					{
						throw new DuplicateElementException("Trying to create an employee with wrong Position");
					}

					egEmpAssignment.setPosition(pos);
				}
				if(!designationId[len].equals("0"))
				{
					DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
					DesignationMaster designationMaster=null;
					if(!"undefined".equals(designationId[len]) && designationId[len] != "")
					{
					 designationMaster = designationMasterDAO.getDesignationMaster(Integer.valueOf(designationId[len]).intValue());
					}
					else
					{
						throw new DuplicateElementException("Trying to create an employee with wrong designation");
					}

					egEmpAssignment.setDesigId(designationMaster);
				}
				if(!fundId[len].equals("0"))
				{
					Fund fund =null;
					try
					{
						FundHibernateDAO fundHibernateDAO = new FundHibernateDAO(Fund.class,HibernateUtil.getCurrentSession());
						 fund = fundHibernateDAO.fundById(Integer.valueOf(fundId[len]));
					}
					catch(Exception ex)
					{
						throw new EGOVRuntimeException(ex.getMessage(),ex);
					}
					egEmpAssignment.setFundId(fund);
				}
				if(!functionId[len].equals("0"))
				{
					CFunction cFunction = null;
					try
					{
						cFunction = commonsService.findFunctionById(Long.valueOf(functionId[len]));
					}
					catch(Exception ex)
					{
						throw new EGOVRuntimeException(ex.getMessage(),ex);
					}
					egEmpAssignment.setFunctionId(cFunction);
				}
				if(!functionaryId[len].equals("0"))
				{
					FunctionaryDAO functionaryHibernateDAO = new FunctionaryDAO(Functionary.class,getCurrentSession());
					Functionary functionary = (Functionary)functionaryHibernateDAO.findById(Integer.valueOf(functionaryId[len]),false);
					egEmpAssignment.setFunctionary(functionary);

				}
				if(!assignmentOrderNo[len].equals("0"))
				{
					if(assignmentOrderNo[len]!=null && !assignmentOrderNo[len].equals(""))
						{
							LOGGER.info("Asignment value"+assignmentOrderNo[len]);
							egEmpAssignment.setGovtOrderNo(assignmentOrderNo[len]);
						}

				}

				/*
				 * Set Grade
				 */
				if(!gradeId[len].equals("0"))
				{
					GenericMaster genericMaster=null;
					try
					{
						genericMaster=(GenericMaster)employeeService.getGenericMaster(Integer.valueOf(gradeId[len]).intValue(),"GradeMaster");
						GradeMaster gradeMaster =(GradeMaster)genericMaster;
						egEmpAssignment.setGradeId(gradeMaster);
					}
					catch(Exception ex)
					{
						throw new EGOVRuntimeException(ex.getMessage(),ex);
					}


				}

				if(mainD!=0)
				{
					Department department = null;
					try
					{
						department = departmentService.getDepartment(Integer.valueOf(mainD));
					}
					catch(Exception e){throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);}
					egEmpAssignment.setDeptId(department);
				}
				if(ptcallocation[len]!=null&&!ptcallocation[len].equals(""))
				{
					egEmpAssignment.setPctAllocation(ptcallocation[len]);
				}
		} catch(DuplicateElementException e)
		{
			throw e;
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}


	}

	private void populateDepartment(Assignment egEmpAssignment,HttpServletRequest req,String departmentIdOfHod )
	{
		try {
			LOGGER.info("egEmpAssignment"+egEmpAssignment.getId());


			String[] deptHod=null;
			EmployeeDepartment employeeDepartment =null;
			 if(departmentIdOfHod!=null && !departmentIdOfHod.trim().equals(""))
			 {
			 	LOGGER.info("departmentIdOfHod"+departmentIdOfHod);

			 		deptHod=departmentIdOfHod.split("#");
			 		for(int i=0;i<deptHod.length;i++)
			 		{
			 		if(!(deptHod[i].equals("0") && deptHod[i].trim().equals("")))
			 		{
			 			try
			 			{
			 				employeeDepartment = new EmployeeDepartment();
			 				DepartmentImpl depObj = (DepartmentImpl)departmentService.getDepartment(Integer.valueOf(deptHod[i]));
			 				employeeDepartment.setHodept(depObj);
					 		employeeDepartment.setDept(depObj);
					 		employeeDepartment.setAssignment(egEmpAssignment);
					 		egEmpAssignment.addEmpDept(employeeDepartment);
			 			}
			 			catch(Exception e)
			 			{
			 				throw new EGOVRuntimeException(e.getMessage(),e);
			 			}
				 	}
			 	}
			 }
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}



	private void setDisplinaryFields(DisciplinaryPunishment egpimsDisciplinaryPunishment,HttpServletRequest req)
	{


		try {
			if(req.getParameter("natureOfAlligations")!=null&&!req.getParameter("natureOfAlligations").equals(""))
			{
				egpimsDisciplinaryPunishment.setNatureOfAlligations(req.getParameter("natureOfAlligations"));
			}
			if(req.getParameter("chargeMemoNumber")!=null&&!req.getParameter("chargeMemoNumber").equals(""))
			{
				egpimsDisciplinaryPunishment.setChargeMemoNo(req.getParameter("chargeMemoNumber"));
			}
			if(req.getParameter("chargeMemoDate")!=null&&!req.getParameter("chargeMemoDate").equals(""))
			{
				egpimsDisciplinaryPunishment.setChargeMemoDate(getDateString(req.getParameter("chargeMemoDate")));
			}
			if(req.getParameter("chargeMemoServedDate")!=null&&!req.getParameter("chargeMemoServedDate").equals(""))
			{
				egpimsDisciplinaryPunishment.setChargeMemoServedDate(getDateString(req.getParameter("chargeMemoServedDate")));
			}
			if(req.getParameter("natureOfDisposal")!=null&&!req.getParameter("natureOfDisposal").equals(""))
			{
				egpimsDisciplinaryPunishment.setNatureOfDisposal(req.getParameter("natureOfDisposal"));
			}
			if(req.getParameter("natureOfPunisment")!=null&&!req.getParameter("natureOfPunisment").equals(""))
			{
				egpimsDisciplinaryPunishment.setNatureOfPunisment(req.getParameter("natureOfPunisment"));
			}
			if(req.getParameter("absent").equals("0"))
			{
				egpimsDisciplinaryPunishment.setIsUnauthorisedAbsent(Character.valueOf('0'));
			}
			else
			{
				egpimsDisciplinaryPunishment.setIsUnauthorisedAbsent(Character.valueOf('1'));
			}
			if(req.getParameter(STR_REQ_FROM) !=null&&!req.getParameter(STR_REQ_FROM).equals(""))
			{
				if(req.getParameter(STR_REQ_FROM).equals("na"))
				{
					java.util.Date date = null;
					egpimsDisciplinaryPunishment.setAbsentFrom(date);
				}
				else
				{
					egpimsDisciplinaryPunishment.setAbsentFrom(getDateString(req.getParameter(STR_REQ_FROM)));

				}

			}
			if(req.getParameter("to") !=null&&!req.getParameter("to").equals(""))
			{
				if(req.getParameter("to").equals("na"))
				{
					java.util.Date date = null;
					egpimsDisciplinaryPunishment.setAbsentTo(date);
				}
				else
				{
					egpimsDisciplinaryPunishment.setAbsentTo(getDateString(req.getParameter("to")));

				}

			}

			if(req.getParameter("whetherSuspended").equals("0"))
			{
				egpimsDisciplinaryPunishment.setWhetherSuspended(Character.valueOf('0'));
			}
			else
			{
				egpimsDisciplinaryPunishment.setWhetherSuspended(Character.valueOf('1'));
			}
			if(req.getParameter(STR_REQ_DOS) !=null&&!req.getParameter(STR_REQ_DOS).equals(""))
			{
				if(req.getParameter(STR_REQ_DOS).equals("na"))
				{
					java.util.Date date = null;
					egpimsDisciplinaryPunishment.setDateOfSuspension(date);
				}
				else
				{
					egpimsDisciplinaryPunishment.setDateOfSuspension(getDateString(req.getParameter(STR_REQ_DOS)));

				}

			}
			if(req.getParameter(STR_REQ_DOR) !=null&&!req.getParameter(STR_REQ_DOR).equals(""))
			{
				if(req.getParameter(STR_REQ_DOR).equals("na"))
				{
					java.util.Date date = null;
					egpimsDisciplinaryPunishment.setDateOfReinstatement(date);
				}
				else
				{
					egpimsDisciplinaryPunishment.setDateOfReinstatement(getDateString(req.getParameter(STR_REQ_DOR)));

				}

			}
			if(req.getParameter("wsp").equals("0"))
			{
				egpimsDisciplinaryPunishment.setIsSubsistencePaid(Character.valueOf('0'));
			}
			else
			{
				egpimsDisciplinaryPunishment.setIsSubsistencePaid(Character.valueOf('1'));
			}
			if(req.getParameter("punismentOrderDate")!=null&&!req.getParameter("punismentOrderDate").equals(""))
			{
				egpimsDisciplinaryPunishment.setPunisOrderDate(getDateString(req.getParameter("punismentOrderDate")));
			}
			if(req.getParameter("howSuspention")!=null&&!req.getParameter("howSuspention").equals(""))
			{
				egpimsDisciplinaryPunishment.setHowSuspention(req.getParameter("howSuspention"));
			}
			if(req.getParameter("punismenteffectiveDate")!=null&&!req.getParameter("punismenteffectiveDate").equals(""))
			{
				egpimsDisciplinaryPunishment.setPunEffectDate(getDateString(req.getParameter("punismenteffectiveDate")));
			}

			StatusMaster statusMaster = new StatusMaster();
			statusMaster.setId(Integer.valueOf(1));
			//egpimsDisciplinaryPunishment.setStatusId(statusMaster);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

}
	private void setEOFields(DetOfEnquiryOfficer egpimsDetOfEnquiryOfficer,int len,HttpServletRequest req)
	{
		try {

			String[] enquiryOfficeName = null;
			String[] eoDesignation = null;
			String[] eoNoDate = null;
			String[] eoReDate = null;

			enquiryOfficeName = req.getParameterValues("enquiryOfficeName");
			eoDesignation = req.getParameterValues("eoDesignation");
			eoNoDate = req.getParameterValues("eoNoDate");
			eoReDate = req.getParameterValues("eoReDate");

			if(enquiryOfficeName[len]!=null&&!enquiryOfficeName[len].equals(""))
			{
				egpimsDetOfEnquiryOfficer.setEnquiryOfficerName(enquiryOfficeName[len]);
			}
			if(eoDesignation[len]!=null&&!eoDesignation[len].equals(""))
			{
				egpimsDetOfEnquiryOfficer.setDesignation(eoDesignation[len]);
			}
			if(eoNoDate[len]!=null&&!eoNoDate[len].equals(""))
			{
				egpimsDetOfEnquiryOfficer.setNominatedDate(getDateString(eoNoDate[len]));
			}
			if(eoReDate[len]!=null&&!eoReDate[len].equals(""))
			{
				egpimsDetOfEnquiryOfficer.setReportDate(getDateString(eoReDate[len]));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	private void setLtcPirticularsFields(LtcPirticulars egpimsLtcPirticulars,PIMSForm ltcForm,int len,HttpServletRequest req)
	{
		try {
			String[] bYear = null;
			String[] leaveTypeAvailed = null;
			String[] claimed = null;
			String[] orderNo = null;
			String[] dateAvailed = null;
			bYear = req.getParameterValues("bYear");
			leaveTypeAvailed = ltcForm.getLeaveTypeAvailed();
			orderNo = ltcForm.getOrderNo();
			claimed = ltcForm.getClaimed();
			dateAvailed = ltcForm.getDateAvailed();
			if(bYear[len]!=null&&!bYear[len].equals(""))
			{
				egpimsLtcPirticulars.setBlockYear(bYear[len]);
			}
			if(leaveTypeAvailed[len]!=null&&!leaveTypeAvailed[len].equals(""))
			{
				egpimsLtcPirticulars.setLeaveType(leaveTypeAvailed[len]);
			}
			if(orderNo[len]!=null&&!orderNo[len].equals(""))
			{
				egpimsLtcPirticulars.setOrderNo(orderNo[len]);
			}
			if(claimed[len]!=null&&!claimed[len].equals(""))
			{
				egpimsLtcPirticulars.setClaimedWayFare(claimed[len]);
			}
			if(dateAvailed[len]!=null&&!dateAvailed[len].equals(""))
			{
				egpimsLtcPirticulars.setOrderDate(getDateString(dateAvailed[len]));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	private void setTrainingPirticularsFields(TrainingPirticulars egpimsTrainingPirticulars,PIMSForm training,int len)
	{
		try {
			String[] course = null;
			String[] institution = null;
			String[] city = null;
			String[] fromTp = null;
			String[] toTp = null;
			course = (String[])training.getCourse();
			institution = (String[])training.getInstitution();
			fromTp = (String[])training.getFromTp();
			city =(String[])training.getCity();
			toTp = (String[])training.getToTp();
			if(course[len]!=null&&!course[len].equals(""))
			{
				egpimsTrainingPirticulars.setCourse(course[len]);
			}
			if(institution[len]!=null&&!institution[len].equals(""))
			{
				egpimsTrainingPirticulars.setInstitution(institution[len]);
			}
			if(city[len]!=null&&!city[len].equals(""))
			{
				egpimsTrainingPirticulars.setCity(city[len]);
			}
			if(toTp[len]!=null&&!toTp[len].equals(""))
			{
				egpimsTrainingPirticulars.setPotTo(getDateString(toTp[len]));
			}
			if(fromTp[len]!=null&&!fromTp[len].equals(""))
			{
				egpimsTrainingPirticulars.setPotFrom(getDateString(fromTp[len]));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}

	private String getSanctionNo(PersonalInformation personalInformation)
	{
		try {
			String sNo= "";
			try
			{
				FinancialYearDAO finDAO= CommonsDaoFactory.getDAOFactory().getFinancialYearDAO();
				Assignment assignment =employeeService.getLatestAssignmentForEmployee(personalInformation.getIdPersonalInformation());
				String dept  = assignment.getDeptId().getDeptName();
				String finId = finDAO.getCurrYearFiscalId();
				CFinancialYear financialYear = commonsService.findFinancialYearById(Long.valueOf(finId));
				String finYear = financialYear.getFinYearRange();
				Integer seqNum = employeeService.getNextVal();
				sNo= finYear+"/"+dept+"/Disiplinary/"+seqNum;
			}
			catch(Exception e){throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);}
			return sNo;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}

	}

	public PersonalInformationService getPersonalInformationService() {
		return personalInformationService;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}
	
	
	public EmployeeService getEmployeeService(){
		return employeeService;
	}
	
	public void setEmployeeService(EmployeeService employeeService){
		this.employeeService=employeeService;
	}
	
	public GenericCommonsService getGenericCommonsService(){
		return genericCommonsService;
	}
	
	public void setGenericCommonsService(GenericCommonsService genericCommonsService){
		this.genericCommonsService=genericCommonsService;
	}
	
	public CommonsService getCommonsService(){
		return commonsService;
	}
	
	public void setCommonsService(CommonsService commonsService){
		this.commonsService=commonsService;
	}
	
	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private void handleDuplicateException(HttpServletRequest request,
			ActionMessages messages, Exception e) {
		ActionMessage message = new ActionMessage("errors.message",e.getMessage());
		messages.add( ActionMessages.GLOBAL_MESSAGE, message );
		saveMessages( request, messages );
		LOGGER.error("Exception $ -------> "+e.getMessage());
		throw new EGOVRuntimeException(e.getMessage(),e);
	}
	
	/*private void doAuditing(AuditEntity auditEntity, String action,PersonalInformation employee) {
		final String details1 = new StringBuffer("[ Employee code : ").
								append(employee.getEmployeeCode()).append(",  User Name : ").
								append(employee.getUserMaster()==null?"":employee.getUserMaster().getUserName().toString()).append(" ]").toString();
		StringBuffer details2=new StringBuffer(2000);
		for(AssignmentPrd prd:employee.getEgpimsAssignmentPrd())
		{
			details2=details2.append("From Date: ").append(prd.getFromDate().toString()).append(" ,To Date:")
					.append(prd.getToDate().toString()).append(",Dept:").append(prd.getEgpimsAssignment().iterator().next().getDeptId().getDeptName())
					.append(",Designation:").append(prd.getEgpimsAssignment().iterator().next().getDesigId().getDesignationName()).
					append(",Position:").append(prd.getEgpimsAssignment().iterator().next().getPosition().getName()).append("/");
		}
		final AuditEvent auditEvent = new AuditEvent(AuditModule.EIS, auditEntity, action, employee.getEmployeeCode(), details1);		
		auditEvent.setPkId(employee.getId().longValue());
		getAuditEventService().createAuditEvent(auditEvent, PersonalInformation.class);
	}*/

	private static final String FIRSTNAME="First Name";
	private static final String LASTNAME="Last Name";
	private static final String MIDDLENAME="Middle Name";

	private static final String STR_EMPLOYEECODE= "employeeCode";

	private static final String STR_ALERT= "alertMessage";

	private static final String STR_GOTO_BEFORE_SEARCH= "goToBeforeSearch";

	private static final String STR_EXCEPTION= "Exception:";

	private static final String STR_PROPERTYNOPER = "propertyNoper";

	private static final String STR_PROPERTYNPPRE= "propertyNopre";

	private static final String MODULE_CREATE="create";

	private static final String STR_ERROR ="error";

	private static final String PERMANENTADDRESS="PERMANENTADDRESS";

	private static final String STR_SUCCESS ="success";

	private static final String STR_EMPLOYEEOBJ = "employeeob";
	private static final String STR_USERFIRSTNAME ="userFirstName";
	private static final String STR_MODIFY ="modify";
	private static final String STR_EMPLOID ="emploid";
	private static final String STR_REQFIRSTNAME="firstName";
	private static final String STR_REQMIDDLENAME="middleName";
	private static final String STR_REQLASTNAME="lastName";
	private static final String STR_PINCODE_PRE="pinCodepre";
	private static final String STR_PINCODEPER="pinCodeper";
	private static final String STR_REQ_FROM="from";
	private static final String STR_REQ_DOS="dos";
	private static final String STR_REQ_DOR="dor";

}
