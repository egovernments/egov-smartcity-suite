package org.egov.pims.client;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.model.AssignmentPrd;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.Probation;
import org.egov.pims.model.Regularisation;
import org.egov.pims.model.ServiceHistory;
import org.egov.pims.service.EmployeeService;

public class AfterServiceAction extends DispatchAction 
{
	 public final static Logger LOGGER = Logger.getLogger(AfterServiceAction.class.getClass());
	 private EmployeeService employeeService;
	 private transient AuditEventService auditEventService;
	 
	public ActionForward saveServiceDetails(ActionMapping mapping,ActionForm form , HttpServletRequest req,HttpServletResponse res)
	{
		String target=null;
		try {
			PIMSForm pIMSForm  = (PIMSForm)form;
			PersonalInformation egpimsPersonalInformation=(PersonalInformation)req.getAttribute("employeeob");
			
			setServiceHistory(pIMSForm,egpimsPersonalInformation,req);
			//set probation
			setProbation(pIMSForm,egpimsPersonalInformation,req);
			//set Regularisation
			setRegularisation(pIMSForm,egpimsPersonalInformation,req);
			employeeService.createEmloyee(egpimsPersonalInformation);
			doAuditing(AuditEntity.EIS_EMPLOYEE,AuditEvent.CREATED,egpimsPersonalInformation);
			target="success";
		} catch(Exception ex)
		{
			   target = "error";
			   LOGGER.error(ex.getMessage());
			   HibernateUtil.rollbackTransaction();
			   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
		}
		
		return mapping.findForward(target);
	}
	public ActionForward modifyServiceDetails(ActionMapping mapping,ActionForm form , HttpServletRequest req,HttpServletResponse res)
	{
		String target=null;
		String alertMessage=null;
		
			try {
				PIMSForm pIMSForm  = (PIMSForm)form;
				String setName="delServices";
				String setProbation = "delProbation";
				String setReg = "delReg";
				PersonalInformation egpimsPersonalInformation =null;
				if( req.getAttribute("employeeob") != null)
				{
					 egpimsPersonalInformation=(PersonalInformation)req.getAttribute("employeeob");
				}
				
				///HibernateUtil.getCurrentSession().lock(egpimsPersonalInformation, LockMode.NONE);
				
				//Service delete set
				Set<Integer> delSet=(Set)req.getSession().getAttribute(setName);
				//probation delete set
				Set<Integer> delProbation=(Set)req.getSession().getAttribute(setProbation);
				
				//Regularisation set
				Set<Integer> delReguralisation=(Set)req.getSession().getAttribute(setReg);
				
				
				setServiceHistory(pIMSForm,egpimsPersonalInformation,req);
				//set probation
				setProbation(pIMSForm,egpimsPersonalInformation,req);
				//set Regularisation
				setRegularisation(pIMSForm,egpimsPersonalInformation,req);
				if(delSet!=null)
				{
					deleteRecord(delSet,egpimsPersonalInformation);
		        }
				if(delProbation!=null)
				{
					deleteProbationRecord(delProbation,egpimsPersonalInformation);

				}
				if(delReguralisation!=null)
				{
					deleteRegularisationRecord(delReguralisation,egpimsPersonalInformation);

				}
				//comment: removeAttribute(setName) is removed 
				//because from service tab was not saving 
				LOGGER.info("1111111Success-------");
				req.getSession().removeAttribute(setName);
				req.getSession().removeAttribute(setProbation);
				req.getSession().removeAttribute(setReg);
				LOGGER.info("Success--------------------");
				//HibernateUtil.getCurrentSession().flush();
				doAuditing(AuditEntity.EIS_EMPLOYEE,AuditEvent.MODIFIED,egpimsPersonalInformation);
				target = "successUpdateEmployee";
				alertMessage="Executed successfully";
			}
			catch(Exception ex)
			{
				   target = "error";

				   LOGGER.error(ex.getMessage());
				   HibernateUtil.rollbackTransaction();
				   throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
				}
		
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}
	private void setServiceHistory(PIMSForm pIMSForm,PersonalInformation egpimsPersonalInformation,HttpServletRequest req) 
	{
		
		try {
			String[] serviceId = req.getParameterValues("idService");
			int length = serviceId.length;
			String[] comments = null;
			String[] commentDate = null;
			String[] reason = null;
			String[] serviceOrderNo = null;
			String[] serviceDocNo = null;
			String[] payScale = null;
			
			comments=(String[])pIMSForm.getComments();
			commentDate=(String[])pIMSForm.getCommentDate();
			reason=(String[])pIMSForm.getReason();
			serviceOrderNo=(String[])pIMSForm.getServiceOrderNo();
			serviceDocNo=(String[])pIMSForm.getServiceDocNo();
			payScale=(String[])pIMSForm.getPayScale();
			
			for(int index=0;index<length ; index++)
			{
				ServiceHistory servHistory = null;
				if(serviceId[index].equals("0"))
				{
					//set all values for new service object
					if(!StringUtils.trimToEmpty(commentDate[index]).equals("")
							&& !StringUtils.trimToEmpty(comments[index]).equals("") )
					{
						servHistory = new ServiceHistory(egpimsPersonalInformation,getDateString(commentDate[index]),comments[index],reason[index],serviceOrderNo[index],serviceDocNo[index],payScale[index]);
						egpimsPersonalInformation.getEgpimsServiceHistory().add(servHistory);
						//egpimsPersonalInformation.addService(servHistory);
					}
					
				}
				else
				{
					//update the existing service object based on service id
					servHistory = employeeService.getServiceId(Integer.valueOf(serviceId[index]));
					setLoop(servHistory,pIMSForm,index);
				}
			}
		}  catch (Exception e) {
			 LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		
		
		
	}
	
	private void setLoop(ServiceHistory servHistory,PIMSForm pIMSForm,int index)
	{
		try {
			String[] comments = null;
			String[] commentDate = null;
			String[] reason = null;
			String[] serviceOrderNo = null;
			String[] serviceDocNo = null;
			String[] payScale=null;
			
			comments=(String[])pIMSForm.getComments();
			commentDate=(String[])pIMSForm.getCommentDate();
			reason=(String[])pIMSForm.getReason();
			serviceOrderNo=(String[])pIMSForm.getServiceOrderNo();
			serviceDocNo=(String[])pIMSForm.getServiceDocNo();
			payScale=(String[])pIMSForm.getPayScale();
			
			if(!StringUtils.trimToEmpty(commentDate[index]).equals(""))
			{
				servHistory.setCommentDate(getDateString(commentDate[index]));
			}
			if(!StringUtils.trimToEmpty(comments[index]).equals(""))
			{
				servHistory.setComments(comments[index]);
			}
			if(!StringUtils.trimToEmpty(reason[index]).equals(""))
			{
				servHistory.setReason(reason[index]);
			}
			if(!StringUtils.trimToEmpty(serviceOrderNo[index]).equals(""))
			{
				servHistory.setOrderNo(serviceOrderNo[index]);
			}
			if(!StringUtils.trimToEmpty(serviceDocNo[index]).equals(""))
			{
				servHistory.setDocNo(Long.valueOf(serviceDocNo[index]));
			}
			if(!StringUtils.trimToEmpty(payScale[index]).equals(""))
			{
				servHistory.setPayScale(payScale[index]);
			}
			
		} catch (Exception e) {
			 LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		
		
	}
	private void deleteRecord(Set<Integer> delSet,PersonalInformation egpimsPersonalInformation)
	{
		try {
			for(Integer serId:delSet)
			{
				ServiceHistory servHistory = employeeService.getServiceId(Integer.valueOf(serId));
				egpimsPersonalInformation.removeService(servHistory);
			}
		} catch (Exception e) {
			
			LOGGER.error(e);
		}
	}
	
	private void deleteProbationRecord(Set<Integer> delProbation,PersonalInformation egpimsPersonalInformation)
	{
		try {
			for(Integer serId:delProbation)
			{
				Probation probation = employeeService.getProbationId(Integer.valueOf(serId));
				egpimsPersonalInformation.removeProbations(probation);
			}
		} catch (Exception e) {
			
			LOGGER.error(e);
		}
	}
	
	private void deleteRegularisationRecord(Set<Integer> delRegularisation,PersonalInformation egpimsPersonalInformation)
	{
		try {
			for(Integer serId:delRegularisation)
			{
				Regularisation regularisation = employeeService.getRegularisationById(Integer.valueOf(serId));
				egpimsPersonalInformation.removeRegularisation(regularisation);
			}
		} catch (Exception e) {
			
			LOGGER.error(e);
		}
	}
	
	
	private java.util.Date getDateString(String dateString)
	{

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			java.util.Date date = null;
			try
			{
				date = dateFormat.parse(dateString);
	
			} catch (Exception e)
			{
				 LOGGER.error(e.getMessage());
				throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
			}
			return date;
	}
	
	private void setProbation(PIMSForm pIMSForm,PersonalInformation egpimsPersonalInformation,HttpServletRequest req)
	{
		try {
			String[] idProbation = req.getParameterValues("idProbation");
			String[] proPostId = null;
			proPostId = (String[])pIMSForm.getProPostId();
			int ArrLen = idProbation.length;

			if(!proPostId[0].equals("0"))
			{
				for (int len = 0; len < ArrLen; len++)
				{

						if(!proPostId[len].equals("0"))
						{
							Probation egpimsProbation = null;

							if(idProbation[len].equals("0"))
							{
								egpimsProbation = new Probation();
								egpimsProbation.setEmployeeId(egpimsPersonalInformation);
								setProbationLoop(egpimsProbation,pIMSForm,len);
								egpimsPersonalInformation.getEgpimsProbations().add(egpimsProbation);
								//egpimsPersonalInformation.addProbations(egpimsProbation);
								//getEisManagr().addProbations(egpimsPersonalInformation,egpimsProbation);
							}
							else
							{
								egpimsProbation = employeeService.getProbationId(Integer.valueOf(idProbation[len]).intValue());
								setProbationLoop(egpimsProbation,pIMSForm,len);
								//getEisManagr().updateProbation(egpimsProbation);
							}
					}
				}
			}
		} catch (EGOVRuntimeException e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		
	}
	private void setProbationLoop(Probation egpimsProbation,PIMSForm pIMSForm,int index)
	{
		try {
			String[] proPostId = null;
			String[] proFrom = null;
			String[] proTo = null;
			String[] proOrderNo = null;
			String[] proOrderDate = null;
			proPostId = (String[])pIMSForm.getProPostId();
			proFrom = (String[])pIMSForm.getProFrom();
			proTo = (String[])pIMSForm.getProTo();
			proOrderNo = (String[])pIMSForm.getProOrderNo();
			proOrderDate = (String[])pIMSForm.getProOrderDate();
			DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
			DesignationMaster designationMaster = designationMasterDAO.getDesignationMaster(Integer.valueOf(proPostId[index]).intValue());
			egpimsProbation.setPostMstr(designationMaster);
			if(proOrderDate[index]!=null&&!proOrderDate[index].equals(""))
			{
				egpimsProbation.setOrderDate(getDateString(proOrderDate[index]));
			}
			if(proOrderNo[index]!=null&&!proOrderNo[index].equals(""))
			{
				egpimsProbation.setOrderNo(proOrderNo[index]);
			}
			if(proFrom[index]!=null&&!proFrom[index].equals(""))
			{
				egpimsProbation.setProbationFromDate(getDateString(proFrom[index]));
				//egpimsProbation.setProbationDeclaredDate(getDateString(proDec[index]));
			}
			
			if(proTo[index]!=null&&!proTo[index].equals(""))
			{
				egpimsProbation.setProbationToDate(getDateString(proTo[index]));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}


	}
	private void setRegularisation(PIMSForm pIMSForm,PersonalInformation egpimsPersonalInformation,HttpServletRequest req)
	{
		try {
			String[] regPostId = null;
			String[] regularisationId = req.getParameterValues("regularisationId");
			regPostId = (String[])pIMSForm.getRegPostId();
			int ArrLenreg = regularisationId.length;
			if(!regPostId[0].equals("0"))
			{
				for (int len = 0; len < ArrLenreg; len++)
				{
					if(!regPostId[len].equals("0"))
					{
							Regularisation egpimsRegularisation = null;
							if(regularisationId[len].equals("0"))
							{
								egpimsRegularisation =new Regularisation();
								egpimsRegularisation.setEmployeeId(egpimsPersonalInformation);
								setRegularisationLoop(  egpimsRegularisation, pIMSForm, len);
								egpimsPersonalInformation.getEgpimsRegularisations().add(egpimsRegularisation);
								//egpimsPersonalInformation.addRegularisation(egpimsRegularisation);
								//getEisManagr().addRegularisation(egpimsPersonalInformation,egpimsRegularisation);
							}
							else
							{
								egpimsRegularisation = employeeService.getRegularisationById(Integer.valueOf(regularisationId[len]).intValue());
								setRegularisationLoop(  egpimsRegularisation, pIMSForm, len);
								//getEisManagr().updateRegularisation(egpimsRegularisation);
							}

					}
				}
			}
		} catch (Exception e) {
			 LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	
	private void setRegularisationLoop(Regularisation egpimsRegularisation,PIMSForm pIMSForm,int len)
	{
		try {
			String[] regPostId = null;
			String[] regDate = null;
			String[] regOrder = null;
			String[] regOrderDate = null;
			regPostId = (String[])pIMSForm.getRegPostId();
			regDate = (String[])pIMSForm.getRegDate();
			regOrder = (String[])pIMSForm.getRegOrder();
			regOrderDate = (String[])pIMSForm.getRegOrderDate();
			DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
			DesignationMaster designationMaster = designationMasterDAO.getDesignationMaster(Integer.valueOf(regPostId[len]).intValue());
			egpimsRegularisation.setPostMstr(designationMaster);
			if(regOrderDate[len]!=null&&!regOrderDate[len].equals(""))
			{
				egpimsRegularisation.setRegularisationDate(getDateString(regDate[len]));
			}
			if(regOrder[len]!=null&&!regOrder[len].equals(""))
			{
				egpimsRegularisation.setOrderNo(regOrder[len]);
			}
			if(regOrderDate[len]!=null&&!regOrderDate[len].equals(""))
			{
				egpimsRegularisation.setOrderDate(getDateString(regOrderDate[len]));
			}
		} catch (Exception e) {
			 LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}

	private void doAuditing(AuditEntity auditEntity, String action,PersonalInformation employee) {
		final String details1 = new StringBuffer("[ Employee code : ").
								append(employee.getEmployeeCode()).append(",  User Name : ").
								append(employee.getUserMaster()==null?"":employee.getUserMaster().getUserName().toString()).append(" ]").toString();
		StringBuffer details2=new StringBuffer(2000);
		for(AssignmentPrd prd:employee.getEgpimsAssignmentPrd())
		{
			if(details2.length() != 0)
				details2.append(" / ");//for next record
			else
				details2.append(" [ ");//for first record
			
			details2=details2.append("From Date: ").append(DateUtils.getDefaultFormattedDate(prd.getFromDate())).append(" ,To Date: ")
					.append(DateUtils.getDefaultFormattedDate(prd.getToDate())).append(",Dept:").append(prd.getEgpimsAssignment().iterator().next().getDeptId().getDeptName())
					.append(",Designation: ").append(prd.getEgpimsAssignment().iterator().next().getDesigId().getDesignationName()).
					append(",Position: ").append(prd.getEgpimsAssignment().iterator().next().getPosition().getName());
			details2=details2.append(",Assignment Type: ").append(prd.getEgpimsAssignment().iterator().next().getIsPrimary()=='Y'?"Primary":"Temporary");
			
		}
		details2.append(" ] ");//for last record
		final AuditEvent auditEvent = new AuditEvent(AuditModule.EIS, auditEntity, action, employee.getEmployeeCode(), details1);		
		auditEvent.setDetails2(details2.toString());
		auditEvent.setPkId(employee.getId().longValue());
		getAuditEventService().createAuditEvent(auditEvent, PersonalInformation.class);
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	private static final String STR_EXCEPTION= "Exception:";

	public AuditEventService getAuditEventService() {
		return auditEventService;
	}
	public void setAuditEventService(AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}
	
}
