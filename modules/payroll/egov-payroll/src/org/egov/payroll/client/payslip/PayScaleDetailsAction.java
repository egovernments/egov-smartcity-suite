package org.egov.payroll.client.payslip;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.ObjectHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.dao.SalaryCodesDAO;
import org.egov.payroll.model.IncrementSlabsForPayScale;
import org.egov.payroll.model.PayScaleDetails;
import org.egov.payroll.model.PayScaleHeader;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.dao.EisDAOFactory;
import org.egov.pims.model.PayFixedInMaster;

/**
 * @author Lokesh
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PayScaleDetailsAction extends Action {
	private static final Logger LOGGER = Logger.getLogger(PayScaleDetailsAction.class);
	private static final String TYPESTR = "type";
	private PersistenceService<org.egov.infstr.workflow.Action,Long> actionService;
	PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill.getPayrollExterInterface();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String target = " ";
		try {
			//String mode = request.getParameter("mode");

			/*if(("viewLatestPayscale").equalsIgnoreCase(mode)){
				String PayslipId = request.getParameter("payslipId");
				EmpPayroll payslipObj = PayrollManagersUtill.getPayRollManager().getPayslipById(Long.parseLong(PayslipId));
				PayScaleHeader payHeader = PayrollManagersUtill.getPayRollManager().getLastPayscaleByEmp(payslipObj.getEmployee().getIdPersonalInformation());
				PayStructure paystructure = PayrollManagersUtill.getPayRollManager().getPayStructureForEmpByDate(payslipObj.getEmployee().getIdPersonalInformation(), payslipObj.getToDate());
				request.getSession().setAttribute("paystructure", paystructure);
				request.getSession().setAttribute("payHeader", payHeader);
				target = "viewPayscale";
			}
			else{*/
				SalaryPaySlipForm salPaySlipForm = (SalaryPaySlipForm) form;
				PayRollService payRollService = PayrollManagersUtill.getPayRollService();
				javax.servlet.http.HttpSession session = request.getSession();
				String userName =(String) session.getAttribute("com.egov.user.LoginUserName");
				User lastModifiedby = payrollExternalInterface.getUserByUserName(userName);
	
				if(salPaySlipForm.getPayScaleName()!=null && !salPaySlipForm.getPayScaleName().trim().equals(""))
				{
					PayScaleHeader payHeader=null;
					if(request.getParameter(TYPESTR)!=null && !request.getParameter(TYPESTR).trim().equals("")
							&& request.getParameter("id")!=null && !request.getParameter("id").trim().equals("") &&
							("modify").equals(request.getParameter("mode")))
					{
						String id = request.getParameter("id");
						payHeader = (PayScaleHeader) payRollService.getPayScaleHeaderById(new Integer(id.trim()));
						LOGGER.info("payHeader >>>>>>>>>> " + payHeader.getName());
						List<PayScaleDetails> deletePayScaleDetails=new ArrayList<PayScaleDetails>();
						Set<PayScaleDetails> PayScaleDetailsSet=payHeader.getPayscaleDetailses();
						if(payHeader.getPayscaleDetailses()!=null && payHeader.getPayscaleDetailses().size()!=0)
						{
							for(Iterator<PayScaleDetails> iter = payHeader.getPayscaleDetailses().iterator(); iter.hasNext();)
							{
								PayScaleDetails payDetails = (PayScaleDetails)iter.next();
								deletePayScaleDetails.add(payDetails);
							}
						}
						if(!deletePayScaleDetails.isEmpty())
						{
							Iterator<PayScaleDetails> itr=deletePayScaleDetails.iterator();
							while(itr.hasNext())
							{
								PayScaleDetails payDetails = (PayScaleDetails) itr.next();
								PayScaleDetailsSet.remove(payDetails);
								payRollService.deletePayScaleDetails(payDetails);
							}
	
						}
						//It deletes the old increment details(from db) of the payscaleheader object
						List<IncrementSlabsForPayScale> deleteIncrSlabDetails=new ArrayList<IncrementSlabsForPayScale>();
						Set<IncrementSlabsForPayScale> incrSlabDetailsSet=payHeader.getIncrSlabs();
						if(payHeader.getIncrSlabs()!=null && payHeader.getIncrSlabs().size()!=0)
						{
							for(Iterator<IncrementSlabsForPayScale> iter = payHeader.getIncrSlabs().iterator(); iter.hasNext();)
							{
								IncrementSlabsForPayScale incrSlabDetails = (IncrementSlabsForPayScale)iter.next();
								deleteIncrSlabDetails.add(incrSlabDetails);
							}
						}
						if(!deleteIncrSlabDetails.isEmpty())
						{
							Iterator<IncrementSlabsForPayScale> itr=deleteIncrSlabDetails.iterator();
							while(itr.hasNext())
							{
								IncrementSlabsForPayScale incrSlabDetails = (IncrementSlabsForPayScale) itr.next();
								incrSlabDetailsSet.remove(incrSlabDetails);
								payRollService.deleteIncrSlabDetails(incrSlabDetails);
							}
						}
	
						session.setAttribute("payscaleModify", "payscaleModify");
					}
					else
					{
						payHeader =new PayScaleHeader();
						//ruleScriptList = new ArrayList<Script>();
						session.setAttribute("payscaleModify", "payscaleCreate");
					}
					payHeader.setAmountFrom(new BigDecimal(salPaySlipForm.getAmountFrom()));
					if(!("").equals(salPaySlipForm.getType().trim()) ||!("-1").equals(salPaySlipForm.getType().trim()) )
					{
						payHeader.setType(salPaySlipForm.getType().trim());
					}
					if(salPaySlipForm.getRuleScript()!=null && !("").equals(salPaySlipForm.getRuleScript().trim()))
					{
						String query = " from org.egov.infstr.workflow.Action act where act.id = ? ";
						org.egov.infstr.workflow.Action ruleScript = (org.egov.infstr.workflow.Action)actionService.find(query,Long.parseLong(salPaySlipForm.getRuleScript()));
						payHeader.setRuleScript(ruleScript);
					}else{
						payHeader.setRuleScript(null);
					}
					
					payHeader.setAmountTo(new BigDecimal(salPaySlipForm.getAmountTo()));
					payHeader.setName(salPaySlipForm.getPayScaleName());

					if(salPaySlipForm.getPayCommision()!=null && !salPaySlipForm.getPayCommision().trim().equals(""))
					{
						Integer payComId = Integer.parseInt(salPaySlipForm.getPayCommision());
						PayFixedInMaster payCommision = (PayFixedInMaster)EisDAOFactory.getDAOFactory().getPayFixedInMasterDAO().findById(payComId, false);
						payHeader.setPayCommision(payCommision);
					}
					payHeader.setEffectiveFrom(getDateString(salPaySlipForm.getEffectiveFrom()));
					String pct[] = salPaySlipForm.getPct();
					String payHead[] = salPaySlipForm.getPayHead();
					String payHeadAmount[] = salPaySlipForm.getPayHeadAmount();
					Set<PayScaleDetails> payDetailsSet = new HashSet<PayScaleDetails>();
					if (payHead != null && payHead.length != 0) {
						for (int i = 0; i < payHead.length; i++) {
							if (!payHeadAmount[i].trim().equals("")
									&& payHeadAmount[i] != null) {
								String salCodeId = payHead[i];
								BigDecimal amountForPayHead = new BigDecimal(
										payHeadAmount[i]);
								//if (amountForPayHead.intValue() > 0) {
									PayScaleDetails payDetails = new PayScaleDetails();
									payDetails.setAmount(amountForPayHead);
									payDetails.setPayHeader(payHeader); 
									if(pct[i]!=null && !pct[i].trim().equals(""))
									{
										payDetails.setPct(new BigDecimal(pct[i]));
									}
									SalaryCodesDAO salaryCodesDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
									SalaryCodes salaryCodes= (SalaryCodes)salaryCodesDAO.findById(new Integer(salCodeId), false);
									payDetails.setSalaryCodes(salaryCodes);
									payDetailsSet.add(payDetails);
								//}
							}
						}
					}
					
					String incSlabFrmAmt[] = salPaySlipForm.getIncSlabFrmAmt();
					String incSlabToAmt[] = salPaySlipForm.getIncSlabToAmt();
					String incrementAmt[] = salPaySlipForm.getIncSlabAmt();
					Set<IncrementSlabsForPayScale> incDetailsSet = new HashSet<IncrementSlabsForPayScale>();
					if (incSlabFrmAmt != null && incSlabFrmAmt.length != 0) {
						for (int i = incSlabFrmAmt.length-1; i >= 0 ; i--) {
							if (!incSlabFrmAmt[i].trim().equals("")
									&& incSlabFrmAmt[i] != null) {

								BigDecimal fromAmt = new BigDecimal(incSlabFrmAmt[i]);
								BigDecimal toAmt = new BigDecimal(incSlabToAmt[i]);
								BigDecimal incrAmt = new BigDecimal(incrementAmt[i]);
								IncrementSlabsForPayScale incrSlbObj=new IncrementSlabsForPayScale();
								incrSlbObj.setIncSlabAmt(incrAmt);
								incrSlbObj.setIncSlabToAmt(toAmt);
								incrSlbObj.setIncSlabFrmAmt(fromAmt);
								incrSlbObj.setPayHeader(payHeader);
								//payDetails.setSalaryCodes(salaryCodes);
								incDetailsSet.add(incrSlbObj);
								//}
							}
						}
					}
					
					if(!payDetailsSet.isEmpty())
					{
						payHeader.setPayscaleDetailses(payDetailsSet);
					}
					
					//if(!incDetailsSet.isEmpty())
					//{
						payHeader.setIncrSlabs(incDetailsSet);
					//}
					
					if(request.getParameter(TYPESTR)!=null && !request.getParameter(TYPESTR).trim().equals("") &&
							!("modify").equals(request.getParameter("mode")))
					{
						LOGGER.info("pay structure created   !!! " );
						payRollService.createPayHeader(payHeader);
					}
					else
					{
						LOGGER.info("pay structure updated   !!! " );
						payRollService.updatePayHeader(payHeader);
					}
					EgovMasterDataCaching.getInstance().removeFromCache("pay-payScaleHeader");
					ObjectHistory objHistory = new ObjectHistory();
					objHistory.setModifiedBy(lastModifiedby);
					objHistory.setObjectId(payHeader.getId());
					objHistory.setObjectType(payrollExternalInterface.getObjectTypeByType("payscale"));
					objHistory.setRemarks(salPaySlipForm.getModifyRemarks());
					payrollExternalInterface.createObjectHistory(objHistory);
					//session.setAttribute("ruleScriptList", ruleScriptList);
					session.setAttribute("payHeader", payHeader);
					if(payHeader.getPayCommision() != null)
						LOGGER.info("payCom----"+payHeader.getPayCommision().getName());
					target = "success";
				}
			//}

		} catch (EGOVRuntimeException ex) {
			LOGGER.error("EGOVRuntimeException Encountered!!!"
					+ ex.getMessage());
			//target = "failure";
			
			HibernateUtil.rollbackTransaction();
			return mapping.findForward("error");
		} catch (Exception e) {
			LOGGER.error("Error while getting data>>>>>" + e.getMessage());
			//target = "failure";
			
			HibernateUtil.rollbackTransaction();
			return mapping.findForward("error");
		}
		return mapping.findForward(target);
	}
	
	private java.util.Date getDateString(String dateString)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		java.util.Date d = new java.util.Date();
		try
		{
			d = dateFormat.parse(dateString);

		} catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		return d;
	}

	public PersistenceService getActionService() {
		return actionService;
	}

	public void setActionService(PersistenceService actionService) {
		this.actionService = actionService;
	}

}
