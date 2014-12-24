package org.egov.payroll.client.empPayroll;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.model.PayScaleHeader;
import org.egov.payroll.model.PayStructure;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.pims.model.BankDet;
import org.egov.pims.model.CategoryMaster;
import org.egov.pims.model.GenericMaster;
import org.egov.pims.model.PayFixedInMaster;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.RecruimentMaster;
import org.egov.pims.model.TypeOfPostingMaster;
import org.egov.pims.model.TypeOfRecruimentMaster;

public class AfterPayRollDetailAction extends DispatchAction {

	public static final Logger LOGGER = Logger
			.getLogger(AfterPayRollDetailAction.class.getClass());
	PayrollExternalInterface payrollExternalInterface;
	private PayRollService payRollService;
	String target = null;
	private static final String BANKSTR = "bank";
	private AuditEventService auditEventService;
	private StringBuffer details = null;

	public PayrollExternalInterface getPayrollExternalInterface() {
		return payrollExternalInterface;
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public ActionForward saveDetailsEmployee(ActionMapping mapping,
			ActionForm form, HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String alertMessage = null;
		String delPayscale = "delPayscale";
		try {
			PersonalInformation egpimsPersonalInformation = null;
			egpimsPersonalInformation = payrollExternalInterface
					.getEmloyeeById(Integer.valueOf(req.getParameter("Id")));
			DynaBean payrollForm = (DynaBean) form;

			setDetailsOfpimsForCreateAndModify(egpimsPersonalInformation,
					payrollForm, req);
			setBankDet(payrollForm, egpimsPersonalInformation, req);
			setPayScale(payrollForm, egpimsPersonalInformation, req);
			Set<Integer> delSet = (Set) req.getSession().getAttribute(
					delPayscale);

			if (delSet != null) {
				deleteRecord(delSet, egpimsPersonalInformation);
			}

			req.getSession().removeAttribute(delPayscale);

			target = "success";
			alertMessage = "Executed successfully";
		} catch (Exception ex) {
			target = "error";
			LOGGER.error(ex.getMessage());
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(ex.getMessage(), ex);
		}

		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}

	/*
	 * public ActionForward modifyDetailsEmployee(ActionMapping
	 * mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 * throws IOException,ServletException {
	 * 
	 * String target =null; String alertMessage=null;
	 * 
	 * try { DynaBean payrollForm = (DynaBean)form; PersonalInformation
	 * egpimsPersonalInformation = null;
	 * 
	 * if( req.getAttribute("employeeob") != null) {
	 * egpimsPersonalInformation=(PersonalInformation
	 * )req.getAttribute("employeeob"); } if (egpimsPersonalInformation == null)
	 * { target = "error"; alertMessage="Employee not found to update"; } else {
	 * setDetailsOfpimsForCreateAndModify(egpimsPersonalInformation,
	 * payrollForm,req); setPayScale(payrollForm,egpimsPersonalInformation,req);
	 * setBankDet(payrollForm,egpimsPersonalInformation,req); target =
	 * "successUpdateEmployee"; alertMessage="Executed successfully"; } }
	 * catch(Exception ex) { target = "error";
	 * 
	 * LOGGER.error(ex.getMessage()); HibernateUtil.rollbackTransaction(); throw
	 * new EGOVRuntimeException(ex.getMessage(),ex); }
	 * 
	 * req.setAttribute("alertMessage", alertMessage); return
	 * mapping.findForward(target); }
	 */

	private void setPayScale(DynaBean payrollForm,
			PersonalInformation egpimsPersonalInformation,
			HttpServletRequest req) throws Exception {
		try {

			String[] payScaleHeaderId = null;
			String payScaleId[] = req.getParameterValues("payScaleId");
			payScaleHeaderId = (String[]) payrollForm.get("payScaleHeader");
			String[] effDate = (String[]) payrollForm.get("effDate");
			String[] annualIncrDate = (String[]) payrollForm
					.get("annualIncrDate");
			String[] currBasicPay = (String[]) payrollForm.get("currMnthPay");
			String[] currDailyPay = (String[]) payrollForm.get("currDailyPay");
			String[] stagnantPay = (String[]) payrollForm.get("stagnantPayId");
			int Arrps = payScaleHeaderId.length;
			details = new StringBuffer(1000);
			String auditDetails = null;
			String mode = null;
			if (!payScaleHeaderId[0].equals("0")) {
				for (int len = 0; len < Arrps; len++) {
					if (!payScaleHeaderId[len].equals("0")) {
						PayScaleHeader payScaleHeader = getPayRollService()
								.getPayScaleHeaderById(
										Integer.valueOf(payScaleHeaderId[len]));
						PayStructure payStructure = null;
						if (payScaleId[len].equals("0")) {
							payStructure = new PayStructure();
							payStructure.setPayHeader(payScaleHeader);
							payStructure
									.setEffectiveFrom(getDateString(effDate[len]));
							payStructure
									.setAnnualIncrement(getDateString(annualIncrDate[len]));

							if (currBasicPay[len] != null
									&& currBasicPay[len].length() != 0) {
								payStructure.setCurrBasicPay(new BigDecimal(
										currBasicPay[len]));
							} else if ((currDailyPay[len]) != null
									&& currDailyPay[len].length() != 0) {
								payStructure.setDailyPay(new BigDecimal(
										currDailyPay[len]));
							}

							payStructure.setStagnantPay(stagnantPay[len]
									.charAt(0));
							payStructure.setEmployee(egpimsPersonalInformation);
							getPayRollService().createPayStructure(
									payStructure);
							auditDetails = doAuditing(
									AuditEntity.PAYROLL_EMPPAYMASTER,
									PayrollConstants.CREATE_PAYSCALE,
									payStructure, egpimsPersonalInformation);
							mode = PayrollConstants.CREATE_PAYSCALE;
						} else {
							payStructure = getPayRollService()
									.getPayStructureById(
											Integer.valueOf(payScaleId[len]));
							payStructure.setPayHeader(payScaleHeader);
							payStructure
									.setEffectiveFrom(getDateString(effDate[len]));
							payStructure
									.setAnnualIncrement(getDateString(annualIncrDate[len]));

							if (currBasicPay[len] != null
									&& currBasicPay[len].length() != 0) {
								payStructure.setCurrBasicPay(new BigDecimal(
										currBasicPay[len]));
							} else {
								payStructure.setCurrBasicPay(null);
							}

							if ((currDailyPay[len]) != null
									&& currDailyPay[len].length() != 0) {
								payStructure.setDailyPay(new BigDecimal(
										currDailyPay[len]));
							} else {
								payStructure.setDailyPay(null);
							}
							payStructure.setStagnantPay(stagnantPay[len]
									.charAt(0));
							payStructure.setEmployee(egpimsPersonalInformation);
							auditDetails = doAuditing(
									AuditEntity.PAYROLL_EMPPAYMASTER,
									PayrollConstants.MODIFY_PAYSCALE,
									payStructure, egpimsPersonalInformation);
							mode = PayrollConstants.MODIFY_PAYSCALE;
							getPayRollService().updatePayStructure(
									payStructure);

						}
					}
				}
				createAuditEvent(AuditEntity.PAYROLL_EMPPAYMASTER, mode,
						egpimsPersonalInformation, auditDetails);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
	}

	private java.util.Date getDateString(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		java.util.Date date = new java.util.Date();
		try {
			date = dateFormat.parse(dateString);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(), e);
		}
		return date;
	}

	private void setDetailsOfpimsForCreateAndModify(
			PersonalInformation egpimsPersonalInformation,
			DynaBean payrollForm, HttpServletRequest req) {

		try {
			// EIS-Pay Disbursement type

			if (!((String) payrollForm.get("paymentMethod")).equals("0")) {
				egpimsPersonalInformation.setPaymentType((String) payrollForm
						.get("paymentMethod"));
			}

			if (payrollForm.get("dateOfFA") != null
					&& !((String) payrollForm.get("dateOfFA")).equals("")) {
				egpimsPersonalInformation
						.setDateOfFirstAppointment(getDateString((String) payrollForm
								.get("dateOfFA")));
			}
			if (!req.getParameter("mOrId").equals("0")) {

				GenericMaster genericMaster = (GenericMaster) payrollExternalInterface
						.getGenericMaster(
								Integer.valueOf(req.getParameter("mOrId"))
										.intValue(), "RecruimentMaster");
				RecruimentMaster recruimentMaster = (RecruimentMaster) genericMaster;
				egpimsPersonalInformation
						.setModeOfRecruimentMstr(recruimentMaster);
			}
			if (!((String) payrollForm.get("recruitmentTypeId")).equals("0")) {
				GenericMaster genericMaster = (GenericMaster) payrollExternalInterface
						.getGenericMaster(
								Integer.valueOf(
										(String) payrollForm
												.get("recruitmentTypeId"))
										.intValue(), "TypeOfRecruimentMaster");
				TypeOfRecruimentMaster typeOfRecruimentMaster = (TypeOfRecruimentMaster) genericMaster;
				egpimsPersonalInformation
						.setRecruitmentTypeMstr(typeOfRecruimentMaster);
			}
			// for posting Type
			if (!((String) payrollForm.get("postingTypeId")).equals("0")) {
				GenericMaster genericMaster = (GenericMaster) payrollExternalInterface
						.getGenericMaster(
								Integer.valueOf(
										(String) payrollForm
												.get("postingTypeId"))
										.intValue(), "TypeOfPostingMaster");
				TypeOfPostingMaster typeOfPostingMaster = (TypeOfPostingMaster) genericMaster;
				egpimsPersonalInformation
						.setPostingTypeMstr(typeOfPostingMaster);
			}

			if (!((String) payrollForm.get("category")).equals("0")) {
				GenericMaster genericMaster = (GenericMaster) payrollExternalInterface
						.getGenericMaster(
								Integer.valueOf(
										(String) payrollForm.get("category"))
										.intValue(), "CategoryMaster");
				CategoryMaster categoryMaster = (CategoryMaster) genericMaster;
				if (payrollForm.get("fileId") != null
						&& !((String) payrollForm.get("fileId")).trim().equals(
								"")) {
					categoryMaster.setFileId(Long.valueOf((String) payrollForm
							.get("fileId")));
					egpimsPersonalInformation.setCategoryMstr(categoryMaster);
				}
				egpimsPersonalInformation.setCategoryMstr(categoryMaster);
			}

			if (!((String) payrollForm.get("payFixIn")).equals("0")) {
				GenericMaster genericMaster = (GenericMaster) payrollExternalInterface
						.getGenericMaster(
								Integer.valueOf(
										(String) payrollForm.get("payFixIn"))
										.intValue(), "PayFixedInMaster");
				PayFixedInMaster payFixedInMaster = (PayFixedInMaster) genericMaster;
				egpimsPersonalInformation.setPayFixedInMstr(payFixedInMaster);
			}

			if (payrollForm.get("retirementAge") != null
					&& !((String) payrollForm.get("retirementAge")).equals("")) {
				egpimsPersonalInformation.setRetirementAge(Integer
						.valueOf((String) payrollForm.get("retirementAge")));
			}

			if (payrollForm.get("gpf") != null
					&& !((String) payrollForm.get("gpf")).equals("")) {
				egpimsPersonalInformation.setGpfAcNumber((String) payrollForm
						.get("gpf"));
			}
			/*
			 * for govt order number
			 */
			if (payrollForm.get("govtOrderNo") != null
					&& !((String) payrollForm.get("govtOrderNo")).equals("")) {
				egpimsPersonalInformation.setGovtOrderNo((String) payrollForm
						.get("govtOrderNo"));
			}
			/*
			 * Deputation Date
			 */

			if (payrollForm.get("dateOfjoin") != null
					&& !((String) payrollForm.get("dateOfjoin")).equals("")) {
				egpimsPersonalInformation
						.setDateOfjoin(getDateString((String) payrollForm
								.get("dateOfjoin")));
			}
			/*
			 * date of retirement shifted from eisEmployeeGrade to eg_employee
			 */

			if (payrollForm.get("dateOfRetirement") != null
					&& !((String) payrollForm.get("dateOfRetirement"))
							.equals("")) {
				egpimsPersonalInformation
						.setRetirementDate(getDateString((String) payrollForm
								.get("dateOfRetirement")));
			}
		} catch (EGOVRuntimeException e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);
		}

	}

	private void setBankDet(DynaBean payrollForm,
			PersonalInformation egpimsPersonalInformation,
			HttpServletRequest req) {
		try {
			Set bankSet = null;
			bankSet = egpimsPersonalInformation.getEgpimsBankDets();
			BankDet egpimsBankDet = null;
			if (bankSet != null && !bankSet.isEmpty()) {
				Iterator iter = bankSet.iterator();
				while (iter.hasNext()) {
					egpimsBankDet = (BankDet) iter.next();
					egpimsBankDet.setEmployeeId(egpimsPersonalInformation);
					setBankDetails(egpimsBankDet, payrollForm);
				}
			} else {
				if (payrollForm.get("salaryBank") != null
						&& !((String) payrollForm.get("salaryBank")).equals("")
						&& (payrollForm.get(BANKSTR) != null && !((String) payrollForm
								.get(BANKSTR)).equals("0"))) {
					egpimsBankDet = new BankDet();
					egpimsBankDet.setEmployeeId(egpimsPersonalInformation);
					setBankDetails(egpimsBankDet, payrollForm);
				}
			}

			if (!((String) payrollForm.get(BANKSTR)).equals("0")) {
				if (!"create".equals((String) req.getSession().getAttribute(
						"viewMode"))) {
					if (egpimsBankDet != null && egpimsBankDet.getId() != null) {
						payrollExternalInterface.updateBankDet(egpimsBankDet);
					} else {
						payrollExternalInterface.addBankDets(
								egpimsPersonalInformation, egpimsBankDet);
					}
				} else {
					payrollExternalInterface.addBankDets(
							egpimsPersonalInformation, egpimsBankDet);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
	}

	private void setBankDetails(BankDet egpimsBankDet, DynaBean payrollForm) {
		try {

			if (!((String) payrollForm.get(BANKSTR)).equals("0")
					&& !((String) payrollForm.get(BANKSTR)).equals("")) {

				Bank bank = payrollExternalInterface.getBankById(Integer
						.valueOf((String) payrollForm.get(BANKSTR)));
				LOGGER.info(BANKSTR + bank);
				egpimsBankDet.setBank(bank);
			}
			if (!((String) payrollForm.get("branch")).equals("0")
					&& !((String) payrollForm.get("branch")).equals("")) {

				Bankbranch bankBranch = payrollExternalInterface
						.getBankbranchById(Integer.valueOf((String) payrollForm
								.get("branch")));
				egpimsBankDet.setBranch(bankBranch);
			}

			if (payrollForm.get("accountNumber") != null
					&& !((String) payrollForm.get("accountNumber")).equals("")) {
				egpimsBankDet.setAccountNumber(new String((String) payrollForm
						.get("accountNumber")));
			}

			if (((String) payrollForm.get("salaryBank")).equals("0")) {
				egpimsBankDet.setSalaryBank(BigDecimal.ZERO);
			} else {
				egpimsBankDet.setSalaryBank(BigDecimal.ONE);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
	}

	private void deleteRecord(Set<Integer> delSet,
			PersonalInformation egpimsPersonalInformation) {
		try {
			for (Integer serId : delSet) {
				PayStructure payStructure = getPayRollService()
						.getPayStructureById(serId);
				getPayRollService().deletePayStructure(payStructure);

			}
		} catch (Exception e) {

			LOGGER.error(e);
		}
	}

	private String doAuditing(AuditEntity auditEntity, String action,
			PayStructure payscale, PersonalInformation employee) {
		if (details.length() != 0)
			details.append(",");
		return details
				.append("[ Emp code : ")
				.append(employee.getCode())
				.append(", Effective date : ")
				.append(DateUtils.getDefaultFormattedDate(payscale
						.getEffectiveFrom())).append(", Payscale : ")
				.append(payscale.getPayHeader().getName())
				.append(", Basic amount : ").append(payscale.getCurrBasicPay())
				.append(" ]").toString();
	}

	private void createAuditEvent(AuditEntity auditEntity, String action,
			PersonalInformation employee, String details) {
		final AuditEvent auditEvent = new AuditEvent(AuditModule.PAYROLL,
				auditEntity, action, employee.getCode(), details);
		auditEvent.setPkId(Long.valueOf(employee.getId()));
		this.auditEventService.createAuditEvent(auditEvent, PayStructure.class);
	}

	public AuditEventService getAuditEventService() {
		return auditEventService;
	}

	public void setAuditEventService(AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}

	public PayRollService getPayRollService() {
		return payRollService;
	}

	public void setPayRollService(PayRollService payRollService) {
		this.payRollService = payRollService;
	}

}
