/*package org.egov.payroll.client.advance;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
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
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.AdvanceSchedule;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;

public class AfterSanctionAction extends Action{
private static final Logger LOGGER = Logger.getLogger(AfterAdvanceAction.class);
private static final PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm form, HttpServletRequest request,
																HttpServletResponse response) throws IOException,ServletException {
		
		String target = "";
		try{
			AdvanceManager salaryadvanceManager = PayrollManagersUtill.getSalaryadvanceManager();
			
			AdvanceForm salaryadvanceForm = (AdvanceForm)form;
			HttpSession session = request.getSession();		
			String userName =(String) session.getAttribute("com.egov.user.LoginUserName");
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			User user = payrollExternalInterface.getUserByUserName(userName);
			Advance salaryadvance = salaryadvanceManager.getSalaryadvanceById(Long.parseLong(salaryadvanceForm.getSalaryadvanceId()));
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
				salaryadvance.setInterestType(salaryadvanceForm.getInterestType());
			}
			salaryadvance.setModifiedBy(user);
			salaryadvance.setNumOfInst(new BigDecimal(salaryadvanceForm.getNumberOfInstallments()));
			BigDecimal totalAmt = new BigDecimal(salaryadvanceForm.getTotal());		
			salaryadvance.setPendingAmt(totalAmt);
			salaryadvance.setSanctionedBy(user);
			
			
			//For advance schedule installment
			salaryadvance.setMaintainSchedule(salaryadvanceForm.getMaintainSchedule());
			
			if(salaryadvanceForm.getMaintainSchedule()!=null && salaryadvanceForm.getMaintainSchedule().equals("Y"))// if maintain schedule is Y , then populate advance schedule rows.
			{
				Set<AdvanceSchedule> advanceScheduleSet = new LinkedHashSet<AdvanceSchedule>(0);
				
				int noOfInstallments = salaryadvanceForm.getInstallmentNo().length;
				String installmentNo[]= salaryadvanceForm.getInstallmentNo();
				String principalAmt[]= salaryadvanceForm.getPrincipalInstAmount();
				String interestAmt[]= salaryadvanceForm.getInterestInstAmount();

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
								if(advScheduleObj.getRecover()==null){
									advScheduleObj.setPrincipalAmt(new BigDecimal(principalAmt[i]));
									advScheduleObj.setInterestAmt(new BigDecimal(interestAmt[i]));
								}
								
								formRowCount++;
								break;
							}
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
			else if(salaryadvanceForm.getMaintainSchedule()!=null && salaryadvanceForm.getMaintainSchedule().equals("N") && salaryadvance.getAdvanceSchedules().size()>0)// if maintain schedule is N , then delete advance schedule rows if existed.
			{
				salaryadvance.getAdvanceSchedules().clear();
			}
			
			EgwStatus status=null;
			if("reject".equals(salaryadvanceForm.getSanctionRejectAction())){
				status = payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_REJECT);
				salaryadvance.setStatus(status);
			}
			else{
				salaryadvance.setSanctionNum(salaryadvanceForm.getSanctionNo());
				status = payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_SANCTION);
				salaryadvance.setStatus(status);
			}
			EgwStatus fromStatus = payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE, PayrollConstants.SAL_ADVANCE_CREATE);
			EgwSatuschange statusChanges = new EgwSatuschange();
			statusChanges.setCreatedby(user.getId());
			statusChanges.setFromstatus(fromStatus.getId());
			statusChanges.setTostatus(status.getId());
			statusChanges.setModuleid(2);
			statusChanges.setModuletype(status.getModuletype());
			payrollExternalInterface.createEgwSatuschange(statusChanges);				
			
			salaryadvance.setPaymentType(salaryadvanceForm.getPaymentMethod());
			salaryadvance.setSanctionedDate(formatter.parse(salaryadvanceForm.getSanctionDate()));			
			LOGGER.info("Sal advance date before:" + salaryadvance.getModifiedDate());
			if("reject".equals(salaryadvanceForm.getSanctionRejectAction())){
				salaryadvanceManager.rejectAdvance(salaryadvance);
			}
			else
			{
			salaryadvanceManager.sanctionAdvance(salaryadvance);
			LOGGER.info("Sal advance voucher:" + salaryadvance.getVoucherheader().getVoucherNumber());	
			}
			target = salaryadvanceForm.getSanctionRejectAction();
			salaryadvanceForm = null;
			request.getSession().setAttribute("advanceId", salaryadvance.getId());
								
		}catch(EGOVRuntimeException ex)
        {
			LOGGER.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
            target = "error";
            throw ex;
        }
		catch(Exception e)
		{
			LOGGER.error("Error while getting data>>>>>"+e.getMessage());
			target = "error";
		}			
		return actionMapping.findForward(target);
	}
}
*/