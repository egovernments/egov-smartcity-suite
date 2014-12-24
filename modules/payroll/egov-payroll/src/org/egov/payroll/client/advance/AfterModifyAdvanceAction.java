package org.egov.payroll.client.advance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwStatus;
import org.egov.commons.ObjectHistory;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.AdvanceSchedule;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;

public class AfterModifyAdvanceAction extends Action{
	private static final Logger LOGGER = Logger.getLogger(AfterModifyAdvanceAction.class);
	private PayrollExternalInterface payrollExternalInterface;
	private ScriptService scriptService;
	

	public ScriptService getScriptService() {
		return scriptService;
	}


	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}


	public ActionForward execute(ActionMapping actionMapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws Exception {

		String target = ""; 
		ActionMessages messages = new ActionMessages();
		try{
			AdvanceService salaryAdvanceService = PayrollManagersUtill.getAdvanceService();
			AdvanceForm salaryadvanceForm = (AdvanceForm)form;
			HttpSession session = request.getSession();		
			String userName =(String) session.getAttribute("com.egov.user.LoginUserName");				
			User user = payrollExternalInterface.getUserByUserName(userName);
			
			Advance salaryadvance = salaryAdvanceService.getSalaryadvanceById(Long.parseLong(salaryadvanceForm.getSalaryadvanceId()));
			
			/*if(salaryadvance.getBillRegister() == null){
				salaryadvance.setInstAmt(new BigDecimal(salaryadvanceForm.getMonthlyPayment()));
				salaryadvance.setPendingAmt(new BigDecimal(salaryadvanceForm.getPendingAmt()));
			}*/
			String arfApprovedStatus=((EgwStatus)PayrollManagersUtill.getPayrollExterInterface().getStatusByModuleAndDescription("ARF", "Approved")).getCode();
			
			if( ("Y".equals(salaryadvance.getIsLegacyAdvance())) || (salaryadvance.getSalaryARF()!=null && arfApprovedStatus.equalsIgnoreCase(salaryadvance.getSalaryARF().getStatus().getCode())) || (salaryadvance.getSalaryARF()==null && PayrollConstants.SAL_ADVANCE_DISBURSE.equalsIgnoreCase(salaryadvance.getStatus().getCode())) ) //For legacy advance/deduction-bankloan/deduction-advance
			{
				salaryadvance.setInstAmt(new BigDecimal(salaryadvanceForm.getMonthlyPayment()));
				salaryadvance.setPendingAmt(new BigDecimal(salaryadvanceForm.getPendingAmt()));
			}
			else if((PayrollConstants.Deduction_BankLoan.equals(salaryadvance.getSalaryCodes().getCategoryMaster().getName()) && 
					salaryadvance.getStatus()!=null && PayrollConstants.SAL_ADVANCE_CREATE.equalsIgnoreCase(salaryadvance.getStatus().getCode()))
					|| (salaryadvance.getSalaryCodes()!=null  && !PayrollConstants.Deduction_BankLoan.equals(salaryadvance.getSalaryCodes().getCategoryMaster().getName()) 
							&& PayrollConstants.SAL_ADVANCE_CREATE.equalsIgnoreCase(salaryadvance.getSalaryARF().getStatus().getCode()))){ //For both deduction-advance & deduction-bankloan with status=created
				salaryadvance.setAdvanceAmt(new BigDecimal(salaryadvanceForm.getAdvAmount()));
				salaryadvance.setInstAmt(new BigDecimal(salaryadvanceForm.getMonthlyPayment()));
				salaryadvance.setAdvanceType(salaryadvanceForm.getAdvanceType());
				if(PayrollConstants.SAL_ADV_TYPE_INTEREST.equals(salaryadvanceForm.getAdvanceType())){
					salaryadvance.setInterestAmt(new BigDecimal(salaryadvanceForm.getInterestAmount()));
					salaryadvance.setInterestPct(new BigDecimal(salaryadvanceForm.getInterestPct()));
					salaryadvance.setInterestType(salaryadvanceForm.getInterestType());
				}	
				if(PayrollConstants.SAL_ADV_TYPE_NONINTEREST.equals(salaryadvanceForm.getAdvanceType())){
					salaryadvance.setInterestAmt(null);
					salaryadvance.setInterestPct(null);
					salaryadvance.setInterestType(null);
				}
				//salaryadvance.setModifiedBy(user);
				salaryadvance.setNumOfInst(new BigDecimal(salaryadvanceForm.getNumberOfInstallments()));
				BigDecimal totalAmt = new BigDecimal(salaryadvanceForm.getTotal());		
				salaryadvance.setPendingAmt(totalAmt);
				
				//check if the advance rule permits creation of this advance (for Bank loan as well as Advance)
				if (!salaryAdvanceService.isValidAdvance(salaryadvance, scriptService)) {
					throw new EGOVRuntimeException("User is not eligible for this advance. Please contact concerned authority");
				}
			}
			

			/*else if("Voucher Created".equalsIgnoreCase(salaryadvance.getBillRegister().getStatus().getDescription())){
				salaryadvance.setInstAmt(new BigDecimal(salaryadvanceForm.getMonthlyPayment()));
			}
			else if("PAYMENT APPROVED".equalsIgnoreCase(salaryadvance.getBillRegister().getStatus().getDescription())){
				salaryadvance.setInstAmt(new BigDecimal(salaryadvanceForm.getMonthlyPayment()));
				salaryadvance.setPendingAmt(new BigDecimal(salaryadvanceForm.getPendingAmt()));
			}*/

			salaryadvance.setMaintainSchedule(salaryadvanceForm.getMaintainSchedule());
			
			if(salaryadvanceForm.getMaintainSchedule()!=null && salaryadvanceForm.getMaintainSchedule().equals("Y"))// if maintain schedule is Y , then populate advance schedule rows.
			{
				Set<AdvanceSchedule> advanceScheduleSet = new LinkedHashSet<AdvanceSchedule>(0);
				
				int noOfInstallments = salaryadvanceForm.getInstallmentNo().length;
				String installmentNo[]= salaryadvanceForm.getInstallmentNo();
				String principalAmt[]= salaryadvanceForm.getPrincipalInstAmount();
				String interestAmt[]= salaryadvanceForm.getInterestInstAmount();
				String recover[]= salaryadvanceForm.getRecover();
				String recoverStr;

				int formRowCount=0;
				
				Set<AdvanceSchedule> removeAdvanceScheduleSet = new LinkedHashSet<AdvanceSchedule>(0);
				advanceScheduleSet.addAll(salaryadvance.getAdvanceSchedules());
				
				if(!advanceScheduleSet.isEmpty())
				{
					Iterator<AdvanceSchedule> itr = advanceScheduleSet.iterator();
					
					while(itr.hasNext()){
						
						boolean isRecordExist =false;
						AdvanceSchedule advScheduleObj =(AdvanceSchedule) itr.next();
						
						for(int i=formRowCount;i<noOfInstallments;i++)
						{
							if(advScheduleObj.getInstallmentNo()!=null && installmentNo[i] !=null && !installmentNo[i].equals("") && advScheduleObj.getInstallmentNo()== Integer.parseInt(installmentNo[i]))
							{
								isRecordExist=true;
								recoverStr = recover[i];
								if(advScheduleObj.getRecover()==null ){
									advScheduleObj.setPrincipalAmt(new BigDecimal(principalAmt[i]));
									advScheduleObj.setInterestAmt(new BigDecimal(interestAmt[i]));
								}
								if(advScheduleObj.getRecover()==null && recoverStr.equalsIgnoreCase("Y")){
									advScheduleObj.setPrincipalAmt(new BigDecimal(principalAmt[i]));
									advScheduleObj.setInterestAmt(new BigDecimal(interestAmt[i]));
									advScheduleObj.setRecover("Y");
								}
								if(advScheduleObj.getRecover()!=null && (recoverStr==null || !recoverStr.equalsIgnoreCase("Y"))){
									advScheduleObj.setPrincipalAmt(new BigDecimal(principalAmt[i]));
									advScheduleObj.setInterestAmt(new BigDecimal(interestAmt[i]));
									advScheduleObj.setRecover(null);
								}
									
								formRowCount++;
								break;
							}
							//advanceScheduleSet.add(advScheduleObj);
						}
						
						if(!isRecordExist)//if record not exist,it means, it need to delete.
						{
							removeAdvanceScheduleSet.add(advScheduleObj);
						}
					}
					
					if(!removeAdvanceScheduleSet.isEmpty()){
						salaryadvance.getAdvanceSchedules().removeAll(removeAdvanceScheduleSet);
					}
				}
				
				//New records need to add
				Set<AdvanceSchedule> addAdvanceScheduleSet = new LinkedHashSet<AdvanceSchedule>(0);
				while(formRowCount<noOfInstallments)
				{
					AdvanceSchedule advScheduleNewObj = new AdvanceSchedule();
					
					advScheduleNewObj.setAdvance(salaryadvance);
					advScheduleNewObj.setInstallmentNo(Integer.parseInt(installmentNo[formRowCount]));
					advScheduleNewObj.setPrincipalAmt(new BigDecimal(principalAmt[formRowCount]));
					advScheduleNewObj.setInterestAmt(new BigDecimal(interestAmt[formRowCount]));
					addAdvanceScheduleSet.add(advScheduleNewObj);
					formRowCount++;
				}
				if(!addAdvanceScheduleSet.isEmpty()){
					salaryadvance.getAdvanceSchedules().addAll(addAdvanceScheduleSet);
				}
			}
			else if(salaryadvanceForm.getMaintainSchedule()!=null && salaryadvanceForm.getMaintainSchedule().equals("N"))// if maintain schedule is N , then delete advance schedule rows if existed.
			{
				if(salaryadvance.getAdvanceSchedules().size()>0){
					salaryadvance.getAdvanceSchedules().clear();
				}
				else
				{
					LOGGER.info("Nothing to do");
				}
			}
			
			salaryAdvanceService.updateSalaryadvance(salaryadvance);
			salaryadvanceForm.setIsSaved("yes");	
			ObjectHistory objHistory = new ObjectHistory();
			objHistory.setModifiedBy(user);
			objHistory.setModifiedDate(new Date());
			objHistory.setObjectType((payrollExternalInterface.getObjectTypeByType("advance")));
			objHistory.setObjectId(Integer.parseInt(salaryadvance.getId().toString()));// changed here
			objHistory.setRemarks(salaryadvanceForm.getRemarks());
			payrollExternalInterface.createObjectHistory(objHistory);
			target = salaryadvanceForm.getAction().toLowerCase();
		}catch(EGOVRuntimeException ex){
			LOGGER.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
            target = "error";
            ActionMessage message = new ActionMessage("errors.message",ex.getMessage());			
            messages.add( ActionMessages.GLOBAL_MESSAGE, message );
            saveMessages( request, messages );
            throw ex;
        }
		catch(Exception e)
		{
			LOGGER.error("Error while getting data>>>>>"+e.getMessage());
			target = "error";
			throw e;
		}		
		return actionMapping.findForward(target);
	}
	
	
	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}
}
