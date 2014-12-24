package org.egov.payroll.client.payslip;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.dao.FunctionDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FunctionaryDAO;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.dao.DepartmentDAO;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.masters.model.BillNumberMaster;
import org.egov.payroll.model.BatchGenDetails;
import org.egov.payroll.services.payslip.AutoGenerationPaySlipJob;
import org.egov.payroll.services.payslip.IPayslipProcess;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.Assignment;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;

public class CreateBulkPaySlipsAction extends Action {
	private static final Logger LOGGER = Logger
			.getLogger(CreateBulkPaySlipsAction.class);

	private PersistenceService actionService;
	private PersistenceService<BillNumberMaster, Long> billNumberMasterService;
	private Scheduler scheduler;
	private EISServeable eisService;
	private PayRollService payRollService;
	private UserService userService;

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setActionService(PersistenceService actionService) {
		this.actionService = actionService;
	}

	public PersistenceService<BillNumberMaster, Long> getBillNumberMasterService() {
		return billNumberMasterService;
	}

	public void setBillNumberMasterService(
			PersistenceService<BillNumberMaster, Long> billNumberMasterService) {
		this.billNumberMasterService = billNumberMasterService;
	}

	/*
	 * private JobDetail jobDetail;
	 * 
	 * public JobDetail getJobDetail() { return jobDetail; } public void
	 * setJobDetail(JobDetail jobDetail) { this.jobDetail = jobDetail; }
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse responce)
			throws Exception {
		String mode = (String) request.getParameter("mode");
		String target = "success";
		ActionMessages messages = new ActionMessages();
		try {
			if ("beforeBatchPayslip".equals(mode)) {
				target = "beforeBatchPayslip";
				List<Department> departmentList = actionService
						.findAllBy("from DepartmentImpl order by deptName");
				List<DesignationMaster> designationList = actionService
						.findAllBy("from DesignationMaster order by designationName");
				request.getSession().setAttribute("departmentList",
						departmentList);
				request.getSession().setAttribute("deptList",
						getEisService().getDeptsForUser());
				request.getSession().setAttribute("designationList",
						designationList);
			} else {
				SalaryPaySlipForm salaryform = (SalaryPaySlipForm) form;
				String fromdate = salaryform.getEffectiveFrom();
				String todate = salaryform.getEffectiveTo();
				PayRollService payRollService = PayrollManagersUtill
						.getPayRollService();
				String userName = (String) request.getSession().getAttribute(
						"com.egov.user.LoginUserName");
				LOGGER.info("fromdate==" + fromdate + "todate=" + todate
						+ "userName>>>>>>>>>>>" + userName);
				GregorianCalendar fromdate1 = new GregorianCalendar();
				fromdate1.set(Integer.parseInt(fromdate.split("/")[2]),
						Integer.parseInt(fromdate.split("/")[1]) - 1,
						Integer.parseInt(fromdate.split("/")[0]));
				GregorianCalendar todate1 = new GregorianCalendar();
				todate1.set(Integer.parseInt(todate.split("/")[2]),
						Integer.parseInt(todate.split("/")[1]) - 1,
						Integer.parseInt(todate.split("/")[0]));
				String deptid = "0";
				String functionaryId = "0";
				String functionId = "0";
				String[] billNumberIds = new String[0];
				String remarks = salaryform.getRemarks();
				if (salaryform.getDeptId() != null
						&& !salaryform.getDeptId().equals("-1")
						&& !salaryform.getDeptId().equals("")) {
					deptid = salaryform.getDeptId();
				}
				if (salaryform.getFunctionaryId() != null
						&& !salaryform.getFunctionaryId().equals("-1")
						&& !salaryform.getFunctionaryId().equals("")) {
					functionaryId = salaryform.getFunctionaryId();
				}
				if (salaryform.getFunctionId() != null
						&& !salaryform.getFunctionId().equals("-1")
						&& !salaryform.getFunctionId().equals("")) {
					functionId = salaryform.getFunctionId();
				}
				if (salaryform.getBillNumberIds() != null
						&& salaryform.getBillNumberIds().length > 0
						&& !salaryform.getBillNumberIds()[0].equals("-1")
						&& !salaryform.getBillNumberIds()[0].equals("")) {
					billNumberIds = salaryform.getBillNumberIds();
				}

				if (billNumberIds != null && billNumberIds.length > 0) {
					for (int i = 0; i < billNumberIds.length; i++) {
						BatchGenDetails batchgenobj = new BatchGenDetails();
						if (deptid == null || "0".equals(deptid)) {
							batchgenobj.setDepartment(null);
						} else {
							DepartmentDAO deptDao = new DepartmentDAO(
									DepartmentImpl.class,
									HibernateUtil.getCurrentSession());
							batchgenobj.setDepartment((DepartmentImpl) deptDao
									.findById(new Integer(deptid), false));
						}
						if (functionaryId != null) {
							FunctionaryDAO funcDAO = new FunctionaryDAO(
									Functionary.class,
									HibernateUtil.getCurrentSession());
							batchgenobj
									.setFunctionary(funcDAO
											.functionaryById(new Integer(
													functionaryId)));
						}
						if (functionId != null && !"0".equals(functionId)) {
							FunctionDAO funcDAO = new FunctionHibernateDAO(
									CFunction.class,
									HibernateUtil.getCurrentSession());
							batchgenobj.setFunction((CFunction) funcDAO
									.findById(new Long(functionId), false));
						}
						if (payRollService
								.getFinancialYearByDate(todate1.getTime()) == null) {
							throw new EGOVRuntimeException(
									"Financial year is not defined for this given date--"
											+ todate1.getTime());
						} else {
							batchgenobj.setFinancialyear(payRollService
									.getFinancialYearByDate(todate1.getTime()));
						}
						// Add batchgendetails entry for each billnumber
						batchgenobj
								.setBillNumber((BillNumberMaster) getBillNumberMasterService()
										.findByNamedQuery("BILLNUMBER_BY_ID",
												new Integer(billNumberIds[i])));
						batchgenobj.setFromDate(fromdate1.getTime());
						batchgenobj.setToDate(todate1.getTime());
						batchgenobj.setMonth(new BigDecimal(todate1
								.get(Calendar.MONTH) + 1));
						UserService userService = PayrollManagersUtill
								.getUserService();
						User user = userService.getUserByUserName(userName);
						batchgenobj.setCreatedBy(user);
						batchgenobj.setCreatedDate(new Date());
						batchgenobj.setModifiedBy(user);
						batchgenobj.setModifiedDate(new Date());
						batchgenobj.setRemarks(remarks);

						batchgenobj
								.setStatus(PayrollConstants.BATCH_GEN_STATUS_START);

						batchgenobj = payRollService
								.insertBatchGenDetails(batchgenobj);
						// Update with job details
						String cityURL = EGOVThreadLocals.getDomainName();
						String jobGroupName = "Payslip Group Job-" + cityURL;
						String jobName = batchgenobj.getId().toString()
								+ "-Batch Payslips generation";
						batchgenobj.setSchJobGroupName(jobGroupName);
						batchgenobj.setSchJobName(jobName);

						payRollService.updateBatchGenDetals(batchgenobj);

						/*JobDetail jobDetail = new JobDetail(jobName,
								jobGroupName, AutoGenerationPaySlipJob.class);*/
						
						JobDetail jobDetail = new JobDetailImpl(jobName,
								jobGroupName, AutoGenerationPaySlipJob.class);
						//TODO :need to find alternative for this
						//jobDetail.setDurability(true);
						jobDetail.getJobDataMap().put("method", "generateJob");

						LOGGER.info(" batchgenobj.getId().toString() = "
								+ batchgenobj.getId().toString());

						jobDetail.getJobDataMap().put("cityURL", cityURL);
						jobDetail.getJobDataMap().put("JNDI",
								EGOVThreadLocals.getJndiName());
						jobDetail.getJobDataMap().put("HIBNAME",
								EGOVThreadLocals.getHibFactName());
						jobDetail.getJobDataMap().put("BATCHID",
								batchgenobj.getId().toString());
						jobDetail.getJobDataMap().put("USERNAME", userName);
						jobDetail.getJobDataMap().put("userId",
								EGOVThreadLocals.getUserId());

						String approverEmpAssgnId = salaryform
								.getApproverEmpAssignmentId();
						if (approverEmpAssgnId != null) {
							Assignment approverAssignment = PayrollManagersUtill
									.getEmployeeService()
									.getAssignmentById(
											Integer.parseInt(approverEmpAssgnId));
							jobDetail.getJobDataMap().put("approverPosition",
									approverAssignment.getPosition());
						}

						// this is to make jobs as a stateful jobs
						jobDetail.requestsRecovery();
						CronTrigger cronTrigger = new CronTriggerImpl(cityURL
								+ batchgenobj.getId().toString(),
								"Payslip Group Trigger-" + cityURL);
						//cronTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
						cronTrigger.getMisfireInstruction();
						String cronExpr = null;
						// getting from config table instead of config xml file
						cronExpr = GenericDaoFactory
								.getDAOFactory()
								.getAppConfigValuesDAO()
								.getAppConfigValueByDate("Payslip",
										"BatchPayslipGenCronExp", new Date())
								.getValue();
						String payslipGenMode = GenericDaoFactory
								.getDAOFactory()
								.getAppConfigValuesDAO()
								.getAppConfigValueByDate("Payslip",
										"BatchPayslipGenMode", new Date())
								.getValue();
						// EGovConfig.getProperty("payroll_egov_config.xml","mode",
						// "", "CronExp");
						if (payslipGenMode != null
								&& payslipGenMode.equalsIgnoreCase("dev")) {
							GregorianCalendar currdate = new GregorianCalendar();
							currdate.add(Calendar.SECOND, 120);
							// 0 55 11 * * ? ==>
							// Seconds/Minutes/Hours/Day-of-Month/Month/Day-of-Week/Year(optional
							// field)
							int mon = currdate.get(Calendar.MONTH);
							int tempMon = mon + 1;
							cronExpr = currdate.get(Calendar.SECOND) + " "
									+ currdate.get(Calendar.MINUTE) + " "
									+ currdate.get(Calendar.HOUR_OF_DAY) + " "
									+ currdate.get(Calendar.DAY_OF_MONTH) + " "
									+ tempMon + " ? "
									+ currdate.get(Calendar.YEAR);
						}
						LOGGER.info(cronExpr);
						if (cronExpr == null || "".equals(cronExpr)) {
							throw new EGOVRuntimeException(
									"Scheduler time has not been configured.");
						} else {
							LOGGER.info(cronExpr);
							//cronTrigger.setCronExpression(cronExpr);
							scheduler.scheduleJob(jobDetail, cronTrigger);

							LOGGER.debug("Job scheduled now ..");
						}

					}
				}

				request.getSession()
						.setAttribute(
								"alertMessage",
								"Payslips have been scheduled to be generated. This process will take some time depending on the number of payslips. Please check back later.");
			}
			return mapping.findForward(target);
		} catch (EGOVRuntimeException egovExp) {
			/*
			 * LOGGER.error(egovExp.getMessage());
			 * HibernateUtil.rollbackTransaction(); return
			 * mapping.findForward("error");
			 */
			ActionMessage message = new ActionMessage("errors.message",
					egovExp.getMessage());
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
			saveMessages(request, messages);
			LOGGER.error("Exception $ -------> " + egovExp.getMessage());
			throw new EGOVRuntimeException(egovExp.getMessage(), egovExp);
		} catch (Exception e) {
			/*
			 * LOGGER.error(e.getMessage());
			 * HibernateUtil.rollbackTransaction(); return
			 * mapping.findForward("error");
			 */
			ActionMessage message = new ActionMessage("errors.message",
					e.getMessage());
			messages.add(ActionMessages.GLOBAL_MESSAGE, message);
			saveMessages(request, messages);
			LOGGER.error("Exception $ -------> " + e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);
		}

	}

	protected String getMessageBundle(HttpServletRequest request, String key) {
		MessageResources messageResources = null;
		messageResources = getResources(request);
		return messageResources.getMessage(key);
	}

	public EISServeable getEisService() {
		return eisService;
	}

	public void setEisService(EISServeable eisService) {
		this.eisService = eisService;
	}

	public PayRollService getPayRollService() {
		return payRollService;
	}

	public void setPayRollService(PayRollService payRollService) {
		this.payRollService = payRollService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
