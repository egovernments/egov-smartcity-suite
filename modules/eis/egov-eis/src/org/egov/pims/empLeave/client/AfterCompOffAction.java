package org.egov.pims.empLeave.client;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.dao.StatusMasterDAO;
import org.egov.pims.empLeave.model.Attendence;
import org.egov.pims.empLeave.model.AttendenceType;
import org.egov.pims.empLeave.model.CompOff;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.egov.pims.workflow.compOff.CompOffService;

public class AfterCompOffAction extends DispatchAction
{
	public final static Logger LOGGER = Logger.getLogger(AfterCompOffAction.class.getClass());
	private CompOffService compOffService;
	private final SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	private PersistenceService<Attendence,Integer> persistenceService;
	public CompOffService getCompOffService() {
		return compOffService;
	}
	public void setCompOffService(CompOffService compOffService) {
		this.compOffService = compOffService;
	}
	
	
	public PersistenceService<Attendence, Integer> getPersistenceService() {
		return persistenceService;
	}
	public void setPersistenceService(
			PersistenceService<Attendence, Integer> persistenceService) {
		this.persistenceService = persistenceService;
	}


	private static final String approve="approve";
	private static final String reject="reject";
	
	
	public ActionForward modifyCompOff(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="compOffModified";
		LeaveForm leaveForm=(LeaveForm)form;
		try{
		CompOff compoff=(CompOff)getCompOffService().getSession().load(CompOff.class, Long.valueOf(leaveForm.getCompOffId()));
		compoff.setCompOffDate(sdf.parse(leaveForm.getCompOffDate()));
		req.setAttribute("alertMsg", "CompOff Modified Successfully");
		}catch(Exception e){
			target="error";
			 throw new EGOVRuntimeException(e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	public ActionForward rejectCompOff(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="compOffRejected";
		LeaveForm leaveForm=(LeaveForm)form;
		try{
		CompOff compoff=(CompOff)getCompOffService().getSession().load(CompOff.class, Long.valueOf(leaveForm.getCompOffId()));
		getCompOffService().updateWorkflow(compoff, reject, null, EisConstants.STATUS_REJECTED);
		}
		catch(Exception e)
		{
			target="error";
			 throw new EGOVRuntimeException(e.getMessage(),e);
		}
		req.setAttribute("alertMsg", "CompOff Rejected Successfully");
		return mapping.findForward(target);
	}
	public ActionForward approveCompOff(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res) throws Exception
	{
		String target="compOffApproved";
		LeaveForm leaveForm=(LeaveForm)form;
		try{
		CompOff compoff=(CompOff)getCompOffService().getSession().load(CompOff.class, Long.valueOf(leaveForm.getCompOffId()));
		AttendenceType attype = EisManagersUtill.getEmpLeaveService().getAttendenceTypeByName(EisConstants.COMP_OFF);
		Attendence compAtt = new Attendence();
			compAtt.setAttDate(compoff.getCompOffDate());				
			Calendar cal = new GregorianCalendar();
			int month = compoff.getCompOffDate().getMonth()+1;	
			compAtt.setMonth(month);
			compAtt.setFinancialId(EisManagersUtill.getFinYearForGivenDate(compoff.getCompOffDate()));
			compAtt.setAttendenceType(attype);
			compAtt.setEmployee(compoff.getAttObj().getEmployee());
			EisManagersUtill.getEmpLeaveService().createAttendence(compAtt); 
			
		getCompOffService().updateWorkflow(compoff, approve, null, EisConstants.STATUS_APPROVED);
		}
		catch(Exception e)
		{
			target="error";
			 throw new EGOVRuntimeException(e.getMessage(),e);
		}
		req.setAttribute("alertMsg", "CompOff Approved Successfully");
		return mapping.findForward(target);
	}
	public ActionForward cancelCompOffAfterApproval(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	{
		String target="compOffCancelled";
		LeaveForm leaveForm=(LeaveForm)form;
		try{
		CompOff compoff=(CompOff)getCompOffService().getSession().load(CompOff.class, Long.valueOf(leaveForm.getCompOffId()));
		
		Attendence attendence=getPersistenceService().findByNamedQuery("BY_ATTD_DATE_TYPE_EMP",compoff.getCompOffDate(),EisConstants.COMP_OFF,compoff.getAttObj().getEmployee().getIdPersonalInformation() );
		EisManagersUtill.getEmpLeaveService().deleteAttendence(attendence);
		compoff.setStatus(new StatusMasterDAO().getStatusMaster(EisConstants.STATUS_CANCELLED));
		compoff.setState(null);
		getCompOffService().update(compoff);
		req.setAttribute("alertMsg", "CompOff Cancelled Successfully");
		}catch(Exception e){
			target="error";
			 throw new EGOVRuntimeException(e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
}
