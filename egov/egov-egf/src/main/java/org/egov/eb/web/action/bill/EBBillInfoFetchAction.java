/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.eb.web.action.bill;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.eb.domain.master.entity.EBDetails;
import org.egov.eb.domain.transaction.entity.EbSchedulerLog;
import org.egov.eb.service.bill.EBBillInfoService;
import org.egov.eb.service.master.EBDetailsService;
import org.egov.eb.service.transaction.EbSchedulerLogService;
import org.egov.eb.utils.EBConstants;
import org.egov.eb.utils.EBUtils;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesHibernateDAO;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBillSubType;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.pims.commons.Position;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EisUtilService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action to allow the selection of billing cycle for fetching bill information
 * 
 * @author nayeem
 *
 */
@Transactional(readOnly=true)
@ParentPackage(value = "egov")
public class EBBillInfoFetchAction extends GenericWorkFlowAction {
	
	private static final Logger LOGGER = Logger.getLogger(EBBillInfoFetchAction.class);
	private static final String job = "BillInfoFetch";
	
	private String billingCycle;
	private EBDetails ebDetails = new EBDetails();
	private List<EBDetails> ebDetailsList = new ArrayList<EBDetails>();
	
	
	private List<String> validActions = Collections.emptyList();
	private EisCommonService eisCommonService; 
	private CommonsService commonsService;
	private String actionName ;
	private SimpleWorkflowService<EBDetails> ebDetailsWorkflowService;
	
	private EbSchedulerLogService ebSchedulerLogService; 
	private EisUtilService eisService;
	
	private EBDetailsService ebDetailsService;

	private String monthAndYear;
	private String region;
	private String targetArea;
	private String eBBillGroup;
	private String mode = FinancialConstants.STRUTS_RESULT_PAGE_NEW;
	
	private VoucherService voucherService;
	private ScriptService scriptExecutionService;
	private Accountdetailtype accDetailType; 
	private CChartOfAccounts coaDebit;
	private SequenceGenerator sequenceGenerator;
	private AppConfigValuesHibernateDAO appConfigValuesHibernateDAO;
	private EBBillInfoService billInfoService;
	private BudgetDetailsHibernateDAO budgetDetailsDAO;
	private EgwStatusHibernateDAO egwStatusHibernateDAO;
	private String billNumber;
	private String type;
	private Map<Long, String> remarksByEbDetailsId = new HashMap<Long, String>();
	private boolean isInfoEnabled = LOGGER.isInfoEnabled();

	@Override
	public StateAware getModel() {
		return ebDetails;
	}
	
	@Override
	public void prepare() {
		super.prepare();
		if (ebDetails.getId() != null) {
			ebDetails = ebDetailsService.findById(ebDetails.getId(), true);
			eBBillGroup = EBUtils.getEBBillGroup(ebDetails);
			String[] billGroupParts = eBBillGroup.split("-");
			monthAndYear = EBUtils.getShortMonthName(Integer.valueOf(billGroupParts[0])) + "-" + billGroupParts[1];
			region = billGroupParts[3];
			targetArea = billGroupParts[2];
		}

		if (LOGGER.isDebugEnabled()) LOGGER.debug("Entered into prepare, ebDetails.id=" + ebDetails.getId());
		

		addDropdownData("billingCycles", EBUtils.TNEB_BILLING_TYPES);
		
		if (ebDetails.getId() != null) {
			ebDetails = ebDetailsService.findById(ebDetails.getId(), true);
			eBBillGroup = EBUtils.getEBBillGroup(ebDetails);
			String[] billGroupParts = eBBillGroup.split("-");
			monthAndYear = EBUtils.getShortMonthName(Integer.valueOf(billGroupParts[0])) + "-" + billGroupParts[1];
			region = billGroupParts[3];
			targetArea = billGroupParts[2];
		}
		
		if (!ebDetailsList.isEmpty()) {
			for (EBDetails ebDetails : ebDetailsList) {
				if (StringUtils.isNotBlank(ebDetails.getRemarks())) {
					remarksByEbDetailsId.put(ebDetails.getId(), ebDetails.getRemarks());
				}
			}
		}
		
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from prepare");
	}
	
	@SkipValidation 
@Action(value="/bill/eBBillInfoFetch-newForm")
	public String newForm() {		
		return NEW;
	}
	
	@Validations ( 
			requiredFields = {
					@RequiredFieldValidator(fieldName = "billingCycle", key = FinancialConstants.REQUIRED, message = "")
					} 
			)
	@Transactional
@Action(value="/bill/eBBillInfoFetch-create")
	public String create() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered into fetchBillInfo, billingCycle=" + billingCycle);
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy h:m");
		   List<EbSchedulerLog> scheduledJobs = ebSchedulerLogService.findAllBy("select log from EbSchedulerLog log where trunc(startTime)=trunc(sysdate) and " +
				" schedulerStatus='"+FinancialConstants.SCHEDULER_STATUS_SCHEDULED+"' order by id desc");
		   if(scheduledJobs.size()>0)
		   {
			   String dateTime ="";
			   if(scheduledJobs.get(0).getStartTime()!=null){
				   dateTime = sdf.format(scheduledJobs.get(0).getStartTime());
			   }
			   scheduledJobs.get(0).getStartTime();
			   addActionMessage("A scheduled Job is in Queue at "+dateTime+". Please try after the completion of it " );
			   mode = FinancialConstants.STRUTS_RESULT_PAGE_NEW;
			    return NEW;
		   }
		
		EbSchedulerLog ebLog=new EbSchedulerLog();
		ebLog.setOddOrEvenBilling(billingCycle);
		ebLog.setSchedulerStatus(FinancialConstants.SCHEDULER_STATUS_SCHEDULED);
		
		ebSchedulerLogService.persist(ebLog);
		ebSchedulerLogService.getSession().flush();
		
		if (StringUtils.isNotBlank(type) && "manual".equalsIgnoreCase(type)) {
			
			if (LOGGER.isInfoEnabled()) LOGGER.info("Calling Manual TNEB bill info Fetch");
			ebLog.setStartTime(new Date());
			String fetchResult = billInfoService.fetchEBBills(billingCycle,ebLog.getId());
			addActionMessage(fetchResult);
			
		} else {
			if (LOGGER.isInfoEnabled()) LOGGER.info("Creating a scheduler for TNEB bill info Fetch");
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = null;
			try {
				scheduler = schedulerFactory.getScheduler();
				if (isInfoEnabled) {
					LOGGER.info("fetchBillInfo, scheduler = " + scheduler.getSchedulerName());
				}
			} catch (SchedulerException e) {
				LOGGER.error("Error in getting the scheduler", e);
			}

			Date timeNow = DateUtils.now();
			String jobName = job + "-" + timeNow;
			String groupName = job + "-" + timeNow;
			//This fix is for Phoenix Migration.
			/*JobDetail jobDetail = new JobDetail(jobName, groupName, EBBillInfoFetchJob.class);

			jobDetail.getJobDataMap().put("billingCycle", billingCycle);
			jobDetail.getJobDataMap().put("loggedInUserId", EGOVThreadLocals.getUserId());
			jobDetail.getJobDataMap().put("ebLogId", ebLog.getId().toString());
			jobDetail.setRequestsRecovery(false);*/

			Date runTime = getJobScheduleTime();
			ebLog.setStartTime(runTime);
			//This fix is for Phoenix Migration.
			//Trigger trigger = new SimpleTrigger(jobName, groupName, runTime);

			try {
				if (isInfoEnabled) {
					LOGGER.info("scheduling billInfoFetch now " + runTime);
				}
				//This fix is for Phoenix Migration.
				//scheduler.scheduleJob(jobDetail, trigger);

				//if (!scheduler.isStarted() || scheduler.isInStandbyMode()) {
					scheduler.start();    
				//}
					
					
			} catch (SchedulerException e) {
				LOGGER.error("Error while scheduling the job", e);
				throw new ValidationException(EBUtils.genericErrorMessage);
			}
			addActionMessage("Bill information fetching is successfully scheduled for " + billingCycle + " cycle" );
		}
		
		
		
		if (isInfoEnabled) {
			LOGGER.info("fetchBillInfo - billingCycle=" + billingCycle);
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting from prepare");
		}
		
		mode = FinancialConstants.STRUTS_RESULT_PAGE_NEW;
		return NEW;
	}
	
	private Date getJobScheduleTime() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered into getJobScheduleTime");
		}
		//This fix is for Phoenix Migration.
		//Date time = TriggerUtils.getEvenMinuteDate(DateUtils.now());
		Calendar calendar = Calendar.getInstance();
		//calendar.setTime(time);
		calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE) + 1));
		Date runTime = calendar.getTime();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting from getJobScheduleTime");
		}
		
		return runTime;
	}


	/**
	 * Invoked on click of the inbox item
	 * 
	 * <p>
	 * 	 Shows all the EBDetails info for the selected group
	 * </p>
	 * 
	 * @return result page VIEW
	 */
	@SkipValidation
@Action(value="/bill/eBBillInfoFetch-viewInboxItem")
	public String viewInboxItem() {		
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Entered into viewInboxItem, eBBillGroup=" + eBBillGroup);

		this.addDropdownData("approverDepartmentList",
				this.persistenceService.findAllBy("from Department where deptCode='L' order by deptName"));
		
		ebDetailsList = ebDetailsService.getEBDetialsByGroup(eBBillGroup, EGOVThreadLocals.getUserId());
		
		for (EBDetails ebDtls : ebDetailsList) {
			if (ebDtls.getVariance() != null && ebDtls.getVariance().compareTo(BigDecimal.ZERO) != 0) {
				ebDtls.setIsHighlight((ebDtls.getVariance().compareTo(EBUtils.EBBILL_VARIANCE_PERCENTAGE1) < 0 || ebDtls
						.getVariance().compareTo(EBUtils.EBBILL_VARIANCE_PERCENTAGE2) > 0) ? true : false);
			}
			ebDtls.setRemarks(ebDtls.getState().getExtraInfo());
		}
		
		mode = FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
		
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from viewInboxItem");
		return FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
	}
	@Transactional
	@SkipValidation
	public String save() throws ParseException{
		
		LOGGER.info(">>>>>>>>>>"+getActionName());
		String returnValue="view";
		String actionNm=getActionName();
		String anyRowSelection = "N";
		List<EBDetails> ebDetailsListFromDB = new ArrayList<EBDetails>();
		if(eBBillGroup!=null){
		ebDetailsListFromDB = ebDetailsService.getEBDetialsByGroup(eBBillGroup, EGOVThreadLocals.getUserId());
		}
		//server side validation for user selected atleast on object or not 
		for (EBDetails ebDtls : ebDetailsList) {
				if(ebDtls.getIsProcess()){			
	  				anyRowSelection = "Y";
	  				break;
	  			}	 
		}
		if(anyRowSelection == "N"){
			addActionMessage("Select at least one row to approve/forward/reject");
			returnValue="viewMessage";
			return returnValue;
	  	}

		for (EBDetails ebDtlsDb : ebDetailsListFromDB) {
			for (EBDetails ebDtls : ebDetailsList) {
				if (ebDtlsDb.getId().equals(ebDtls.getId())) {
					if (ebDtls.getIsProcess()) {
						ebDtlsDb.setIsProcess(ebDtls.getIsProcess());
						ebDtlsDb.setComments(parameters.get("approverComments")[0]);
						if(actionNm.equalsIgnoreCase("forward")){
						ebDtlsDb.setPosition((Position)persistenceService.find("from Position where id = ? ", Long.parseLong(parameters.get("approverPositionId")[0])));
						}
					}
					break;
				}
			}
		}

		if (actionNm.equalsIgnoreCase("approve")) {

			this.prepareApprove();
			this.validateApprove();
			this.validate();
			billNumber = createExpenseBill();
			for (EBDetails ebDetail : ebDetailsListFromDB) {
				if (ebDetail.getIsProcess()) {
					returnValue = approve(ebDetail, actionNm,
							null != parameters.get("approverComments") ? parameters.get("approverComments")[0] : null);

					if (!remarksByEbDetailsId.isEmpty()) {
						//This fix is for Phoenix Migration.
						//ebDetail.getCurrentState().setText2(remarksByEbDetailsId.get(ebDetail.getId()));
					}
				}
			}
			addActionMessage("Successfully created the expense bill with bill number " + billNumber);
			addActionMessage("TNEB Bill Approved Succesfully");
		} else if (actionNm.equalsIgnoreCase("forward")) {

			EmployeeView nextUser = null;
			for (EBDetails ebDetail : ebDetailsListFromDB) {
				if (ebDetail.getIsProcess()) {
					returnValue = forward(ebDetail, actionNm,
							null != parameters.get("approverComments") ? parameters.get("approverComments")[0] : null);
					nextUser = (EmployeeView) persistenceService.find("from EmployeeView where position.id=?",ebDetail.getPosition().getId());

					if (!remarksByEbDetailsId.isEmpty()) {
						//This fix is for Phoenix Migration.
						//ebDetail.getCurrentState().setText2(remarksByEbDetailsId.get(ebDetail.getId()));
					}
				}

			}

			if (nextUser != null) {
				addActionMessage(" File is forwared successfully  " + nextUser.getUserMaster().getUsername());
			}

		} else if (actionNm.equalsIgnoreCase("cancel")) {

			for (EBDetails ebDetail : ebDetailsListFromDB) {
				if (ebDetail.getIsProcess()) {
					returnValue = cancel(ebDetail, actionNm,
							null != parameters.get("approverComments") ? parameters.get("approverComments")[0] : null);

					if (!remarksByEbDetailsId.isEmpty()) {
						//This fix is for Phoenix Migration.
						//ebDetail.getCurrentState().setText2(remarksByEbDetailsId.get(ebDetail.getId()));
					}
				}
			}
			
			addActionMessage(getText("TNEB Bill Workflow Cancelled"));

		} else if (actionNm.equalsIgnoreCase("reject")) {
			User createdUser = null;
			for (EBDetails ebDetail : ebDetailsListFromDB) {
				if (ebDetail.getIsProcess()) {
					returnValue = reject(ebDetail, actionNm,
							null != parameters.get("approverComments") ? parameters.get("approverComments")[0] : null);
					createdUser = (User) persistenceService
							.find("select user from User user,EmployeeView empview,Position pos where pos.id = ? and pos.id = empview.position.id and empview.userMaster.id = user.id ",
									ebDetail.getPosition().getId());

					if (!remarksByEbDetailsId.isEmpty()) {
						//This fix is for Phoenix Migration.
						//ebDetail.getCurrentState().setText2(remarksByEbDetailsId.get(ebDetail.getId()));
					}
				}

			}
			if (createdUser != null) {
				addActionMessage(getText("File is rejected Sent back to " + createdUser.getUsername()));
			}
		}
		
		
		return returnValue;
	}
	@SkipValidation
	public String forward(EBDetails ebDetail,String workFlowAction,String approverComments) {
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Entered into forward");
		this.validate();
		
		if (hasErrors()) {
			mode = FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
			return FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
		}
		String comments= ((null == approverComments) || "".equals(approverComments.trim()))?"":approverComments;
		ebDetailsWorkflowService.transition(workFlowAction.toLowerCase(),ebDetail, comments);
		 
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from forward");
			return "viewMessage";
	}
	
	@SkipValidation
	public String reject(EBDetails ebDetail,String workFlowAction,String approverComments) {
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Entered into reject");
		
		this.validate();
		
		if (hasErrors()) {
			mode = FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
			return FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
		}
		String comments= ((null == approverComments) || "".equals(approverComments.trim()))?"":approverComments;
		ebDetailsWorkflowService.transition(workFlowAction.toLowerCase(),ebDetail, comments);
		User approverUser=(User)persistenceService.find("from User where id=?",ebDetail.getCreatedBy().getId());
		Position approvePos = eisService.getPrimaryPositionForUser(approverUser.getId(),new Date());
		ebDetail.setPosition(approvePos);
		
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from reject");
		
		return "viewMessage";
	}
	@SkipValidation
	public String cancel(EBDetails ebDetail,String workFlowAction,String approverComments) {
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Entered into cancel");
		
		this.validate();
		
		if (hasErrors()) {
			mode = FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
			return FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
		}
		String comments= ((null == approverComments) || "".equals(approverComments.trim()))?"":approverComments;
		ebDetailsWorkflowService.transition(workFlowAction.toLowerCase(),ebDetail, comments);
		EBDetails eBDetail=ebDetailsService.find("from EBDetails where id = ? ",ebDetail.getId());
		eBDetail.setStatus(getCancelledStatus());            
		ebDetailsService.persist(eBDetail);
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from cancel");
		
		return "viewMessage";
	}
	
	public void prepareApprove() {
		
		boolean isDebugEnabled = LOGGER.isDebugEnabled();
		String errorMsg = "";
		
		accDetailType = (Accountdetailtype) persistenceService.find("from Accountdetailtype " +
				"where name = '" + EBConstants.BILL_ACCOUNTDETAIL_TYPE_NAME + "'");
		
		if (accDetailType == null) {
			errorMsg = "Electricity account detail does not exists in the system";
			if (isDebugEnabled) LOGGER.debug("Electricity account details does not exists in the system");
			throw new ValidationException(Arrays.asList(new ValidationError(errorMsg, errorMsg)));
		}
		
		coaDebit = getChartOfAccounts(EBConstants.BILL_DEBIT_GLCODE_ELECTRICITY);
		
		if (coaDebit == null) {
			errorMsg = "Debit glcode " + EBConstants.BILL_DEBIT_GLCODE_ELECTRICITY + " does not exists in the system";
			if (isDebugEnabled) LOGGER.debug(errorMsg);
			throw new ValidationException(Arrays.asList(new ValidationError(errorMsg, errorMsg)));
		} 
	}
	
	public void validateApprove() {
		CChartOfAccountDetail coad = (CChartOfAccountDetail) persistenceService.find("from CChartOfAccountDetail " +
				"where glCodeId = ? and detailTypeId = ?", coaDebit, accDetailType);
		
		if (coad == null) {
			String errorMsg = EBConstants.BILL_DEBIT_GLCODE_ELECTRICITY + " is not a control code of type Electricity";
			throw new ValidationException(Arrays.asList(new ValidationError(errorMsg, errorMsg)));
		} 
	}
	
	@SkipValidation
	public String approve(EBDetails ebDetail,String workFlowAction,String approverComments) {
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Entered into approve");
		
		if (hasErrors()) {
			mode = FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
			return FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
		}
		ebDetail.setStatus(commonsService.getStatusByModuleAndCode(FinancialConstants.TNEB_MODULETYPE,"APPROVED"));
		
		String comments= ((null == approverComments) || "".equals(approverComments.trim()))?"":approverComments;
		ebDetailsWorkflowService.transition(workFlowAction.toLowerCase(),ebDetail, comments);
		
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from approve");
		
		

		return "viewMessage";
	}
	
	private void checkBudget(EgBillregister egBillRegister) {
		boolean isDebugEnabled = LOGGER.isDebugEnabled();
		
		if (isDebugEnabled) LOGGER.debug("Entered into checkBudget");
		
		int isSufficientBudget = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("financialyearid", EBUtils.getCurrentFinancialYear().getId());
		
		try{	
			ScriptContext scriptContext = ScriptService.createContext("voucherService", voucherService, "bill", egBillRegister);
			isSufficientBudget = (Integer) scriptExecutionService.executeScript("egf.bill.budgetcheck", scriptContext);
			
			if (isDebugEnabled) LOGGER.debug("checkBudget isSufficientBudget=" + isSufficientBudget);
			
			if (isSufficientBudget == 1) {
				egBillRegister.getEgBillregistermis().setBudgetaryAppnumber(budgetDetailsDAO.getBudgetApprNumber(paramMap));
			}
		} catch (ValidationException ve) {
			String errorMsg = "Insufficient budget...!!";
			if (LOGGER.isDebugEnabled()) LOGGER.debug(errorMsg);
			throw new ValidationException(Arrays.asList(new ValidationError(errorMsg, errorMsg)));
		}
		
		if (isDebugEnabled) LOGGER.debug("Exiting from checkBudget");
	}
	@Transactional
	@SuppressWarnings("unchecked")
	private String createExpenseBill() {
		
		boolean isDebugEnabled = LOGGER.isDebugEnabled();
		
		if (isDebugEnabled) LOGGER.debug("Entered into createExpenseBill");

		EgBillregister egBillRegister = new EgBillregister();
		EgBillregistermis egBillregistermis = new EgBillregistermis();
		BigDecimal sumOfBillAmounts = BigDecimal.ZERO;

		List<AppConfigValues> appConfigValues =null;
		HibernateUtil.getCurrentSession()
				.createQuery(
						"from AppConfigValues v inner join fetch v.key k where k.keyName in ('"
								+ FinancialConstants.EB_VOUCHER_PROPERTY_FUND + "', '"
								+ FinancialConstants.EB_VOUCHER_PROPERTY_DEPARTMENT + "', '"
								+ FinancialConstants.EB_VOUCHER_PROPERTY_FUNCTION + "')").list();
				if (appConfigValues == null || appConfigValues.isEmpty()) {
			String errorMsg = "Fund, Department and Function are not configured into the system";
			
			if (isDebugEnabled) LOGGER.debug(errorMsg);
			
			throw new ValidationException(Arrays.asList(new ValidationError(errorMsg, errorMsg)));
		}
 		
		String keyName = null;
		Fund fund = null;
		Department department = null;
		CFunction function = null;

		for (AppConfigValues configValues : appConfigValues) {
			keyName = configValues.getKey().getKeyName();

			if (FinancialConstants.EB_VOUCHER_PROPERTY_FUND.equalsIgnoreCase(keyName)) {
				fund = (Fund) persistenceService.find("from Fund where code = ? ", configValues.getValue());
			} else if (FinancialConstants.EB_VOUCHER_PROPERTY_DEPARTMENT.equalsIgnoreCase(keyName)) {
				department = (Department) persistenceService.find("from Department where deptCode = ?",
						configValues.getValue());
			} else if (FinancialConstants.EB_VOUCHER_PROPERTY_FUNCTION.equalsIgnoreCase(keyName)) {
				function = (CFunction) persistenceService
						.find("from CFunction where code = ?", configValues.getValue());
			}
		}
		
		if (isDebugEnabled) 
			LOGGER.debug("createExpenseBill - fund = " + fund + ", department=" + department + " and function=" + function);

		String errorMsg = null;

		if (fund == null) {
			errorMsg = "Fund is not available";
		} else if (department == null) {
			errorMsg = "Department is not available";
		} else if (function == null) {
			errorMsg = "Function is not available";
		}

		if (errorMsg != null) {
			if (isDebugEnabled) LOGGER.debug(errorMsg);
			
			throw new ValidationException(Arrays.asList(new ValidationError(errorMsg, errorMsg)));
		}

		for (EBDetails ebDtls : ebDetailsList) {
			if (ebDtls.getIsProcess()) {
				sumOfBillAmounts = sumOfBillAmounts.add(ebDtls.getBillAmount());
			}
		}

		egBillRegister.setBillamount(sumOfBillAmounts);
		egBillRegister.setPassedamount(sumOfBillAmounts);
		egBillRegister.setBillstatus(EBConstants.EBEXPENSEBILL_APPROVED_STATUS);
		egBillRegister.setBilltype(FinancialConstants.STANDARD_BILLTYPE_FINALBILL);

		egBillregistermis.setEgBillregister(egBillRegister);
		List<AppConfigValues> appConfigPayTo = appConfigValuesHibernateDAO
				.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
						EBConstants.APPCONFIG_KEY_NMAE_PAYTO);
		if (appConfigPayTo == null || appConfigPayTo.isEmpty()) {
			errorMsg = "Pay to is not configured...";
			if (isDebugEnabled) LOGGER.debug(errorMsg);
			throw new ValidationException(Arrays.asList(new ValidationError(errorMsg, errorMsg)));
		}
		
		egBillregistermis.setPayto(appConfigPayTo.get(0).getValue());
		EgBillSubType egBillSutType =(EgBillSubType) HibernateUtil.getCurrentSession()
				.createQuery("from EgBillSubType where name = :name and expenditureType = :expType")
				.setString("name", EBConstants.BILL_SUB_TYPE_NAME)
				.setString("expType", EBConstants.BILL_EXPENDITURE_TYPE).list();
		egBillregistermis.setEgBillSubType(egBillSutType);
		egBillregistermis.setFund(fund);
		egBillregistermis.setEgDepartment(department);
		egBillregistermis.setFunction(function);
		
		Calendar calendar = Calendar.getInstance();
		
		egBillregistermis.setMonth(new BigDecimal(calendar.get(Calendar.MONTH) + 1));
		egBillregistermis.setFinancialyear(EBUtils.getCurrentFinancialYear());
		egBillregistermis.setLastupdatedtime(new Date());

		egBillRegister.setEgBillregistermis(egBillregistermis);

		// Bill details part
		createBillDetails(egBillRegister, sumOfBillAmounts);
		
		egBillRegister.setBilldate(new Date());
		egBillRegister.setExpendituretype(EBConstants.BILL_EXPENDITURE_TYPE);

		EgwStatus expenseApproveStatus=null;/* = egwStatusHibernateDAO.
				.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
						EBConstants.STATUS_APPROVED_EXPENSE_BILL);*/
		
		egBillRegister.setStatus(expenseApproveStatus);
		egBillRegister.setBillnumber(getBillNumber(egBillRegister));
		
		checkBudget(egBillRegister);

		List<EBDetails> ebDetailsListFromDB = ebDetailsService.getEBDetialsByGroup(eBBillGroup, EGOVThreadLocals.getUserId());

		for (EBDetails ebDtlsDb : ebDetailsListFromDB) {
			for (EBDetails ebDtls : ebDetailsList) {
				if (ebDtlsDb.getId().equals(ebDtls.getId())) {
					if (ebDtls.getIsProcess()) {
						ebDtlsDb.setEgBillregister(egBillRegister);
						//ebDetailsService.update(ebDtlsDb);
					}
					break;
				}
			}
		}
		
		if (isDebugEnabled) LOGGER.debug("Exiting from createExpenseBill");
		return egBillRegister.getBillnumber();
	}

	/**
	 * 
	 * @param egBillRegister
	 * @param sumOfBillAmounts
	 */
	private void createBillDetails(EgBillregister egBillRegister, BigDecimal sumOfBillAmounts) {
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Entered into createBillDetails");

		// Debit bill details
		EgBilldetails egBillDetails = new EgBilldetails();

		egBillDetails.setEgBillregister(egBillRegister);
		egBillDetails.setDebitamount(sumOfBillAmounts);

		egBillDetails.setGlcodeid(new BigDecimal(coaDebit.getId()));
		egBillDetails.setFunctionid(new BigDecimal(egBillRegister.getEgBillregistermis().getFunction().getId()));

		if (isControlCode(coaDebit.getChartOfAccountDetails())) {
			createBillPayeeDetails(egBillDetails, true);
		} else {
			LOGGER.debug(coaDebit.getGlcode() + " is not ELECTRICITY control code");
		}

		egBillRegister.addEgBilldetailes(egBillDetails);

		
		//Credit bill details
		egBillDetails = new EgBilldetails();

		egBillDetails.setEgBillregister(egBillRegister);
		egBillDetails.setCreditamount(sumOfBillAmounts);

		CChartOfAccounts coa = getChartOfAccounts(EBConstants.BILL_CREDIT_GLCODE);
		egBillDetails.setGlcodeid(new BigDecimal(coa.getId()));
		egBillDetails.setFunctionid(new BigDecimal(egBillRegister.getEgBillregistermis().getFunction().getId()));
		
		if (isControlCode(coa.getChartOfAccountDetails())) {
			createBillPayeeDetails(egBillDetails, false);
		} 
		
		egBillRegister.addEgBilldetailes(egBillDetails);
		
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from createBillDetails");
	}

	private boolean isControlCode(Set<CChartOfAccountDetail> chartOfAccountDetails) {
		boolean isElectricityControlCode = false;
		
		for (CChartOfAccountDetail coad : chartOfAccountDetails) {
			if (coad.getDetailTypeId().equals(accDetailType)) {
				isElectricityControlCode = true;
			}
		}
		
		return isElectricityControlCode;
	}
	
	private void createBillPayeeDetails(EgBilldetails egBillDetails, boolean isDebit) {
		if (LOGGER.isDebugEnabled()) LOGGER.debug("starting from createBillPayeeDetails");
		
		EgBillPayeedetails payeeDetails = null;
		
		for (EBDetails ebDtls : ebDetailsList) {
			if (ebDtls.getIsProcess()) {
				payeeDetails = new EgBillPayeedetails();
				payeeDetails.setEgBilldetailsId(egBillDetails);
				
				if (isDebit) {
					payeeDetails.setDebitAmount(ebDtls.getBillAmount());
				} else {
					payeeDetails.setCreditAmount(ebDtls.getBillAmount());
				}
				
				payeeDetails.setAccountDetailKeyId(ebDtls.getEbConsumer().getId().intValue());
				payeeDetails.setAccountDetailTypeId(accDetailType.getId());
				egBillDetails.addEgBillPayeedetail(payeeDetails);
			}
		}
		
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from createBillPayeeDetails");
	}

	private CChartOfAccounts getChartOfAccounts(String glcode) {
		CChartOfAccounts coa = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where glcode = '"
				+ glcode + "' and isActiveForPosting = 1 and classification = 4");
		return coa;
	}
	
	private String getBillNumber(EgBillregister bill) {
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from getBillNumber");

		CFinancialYear financialYear = EBUtils.getFinancialYearForGivenDate(bill.getBilldate());
		String year = financialYear.getFinYearRange();
		
		Script billNumberScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,
				"egf.bill.number.generator").get(0);
		
		ScriptContext scriptContext = ScriptService.createContext("sequenceGenerator", sequenceGenerator, "sItem",
				bill, "year", year);
		
		String billNumber = (String) scriptExecutionService.executeScript(billNumberScript.getName(), scriptContext);

		if (LOGGER.isDebugEnabled()) LOGGER.debug("getBillNumber - billNumber=" + billNumber + "\nExiting from getBillNumber");
		
		return billNumber;

	}
	
	private EgwStatus getCancelledStatus(){
		return commonsService.getStatusByModuleAndCode(FinancialConstants.TNEB_MODULETYPE, "CANCELLED");
	}
	
	@Override
	public void validate() {
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Entered into validate");
		
		List<String> errorParams = new ArrayList<String>();
		
		for (EBDetails ebDtls : ebDetailsList) {
			if (ebDtls.getIsProcess()
					&& (ebDtls.getVariance() != null && ebDtls.getVariance().compareTo(BigDecimal.ZERO) != 0)) {
				if ((ebDtls.getVariance().compareTo(EBUtils.EBBILL_VARIANCE_PERCENTAGE1) < 0 || ebDtls.getVariance()
						.compareTo(EBUtils.EBBILL_VARIANCE_PERCENTAGE2) > 0)
						&& StringUtils.isBlank(ebDtls.getRemarks())) {
					errorParams.add(ebDtls.getEbConsumer().getCode());
					errorParams.add(ebDtls.getEbConsumer().getName());
					addActionError(getText("error.ebbill.comments", errorParams));
				}
			}
		}
		
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Exiting from validate");
	}

	public String getBillingCycle() {
		return billingCycle;
	}

	public void setBillingCycle(String billingCycle) {
		this.billingCycle = billingCycle;
	}

	public EBDetails getEbDetails() {
		return ebDetails;
	}

	public void setEbDetails(EBDetails ebDetails) {
		this.ebDetails = ebDetails;
	}

	public List<EBDetails> getEbDetailsList() {
		return ebDetailsList;
	}

	public void setEbDetailsList(List<EBDetails> ebDetailsList) {
		this.ebDetailsList = ebDetailsList;
	}

	public EBDetailsService getEbDetailsService() {
		return ebDetailsService;
	}

	public void setEbDetailsService(EBDetailsService ebDetailsService) {
		this.ebDetailsService = ebDetailsService;
	}

	public String getMonthAndYear() {
		return monthAndYear;
	}

	public void setMonthAndYear(String monthAndYear) {
		this.monthAndYear = monthAndYear;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getTargetArea() {
		return targetArea;
	}

	public void setTargetArea(String targetArea) {
		this.targetArea = targetArea;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

		
	public EisCommonService getEisCommonService() {
		return eisCommonService;
	}


	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public SimpleWorkflowService<EBDetails> getEbDetailsWorkflowService() {
		return ebDetailsWorkflowService;
	}

	public void setEbDetailsWorkflowService(
			SimpleWorkflowService<EBDetails> ebDetailsWorkflowService) {
		this.ebDetailsWorkflowService = ebDetailsWorkflowService;
	}

	public EisUtilService getEisService() {
		return eisService;
	}
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
	public CommonsService getCommonsService() {
		return commonsService;
	}
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public void setBillInfoService(EBBillInfoService billInfoService) {
		this.billInfoService = billInfoService;
	}

	public void setBudgetDetailsDAO(BudgetDetailsHibernateDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	
	public Date getTodaysDate() {
		return DateUtils.now();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setEbSchedulerLogService(EbSchedulerLogService ebSchedulerLogService) {
		this.ebSchedulerLogService = ebSchedulerLogService;
	}

	public AppConfigValuesHibernateDAO getAppConfigValuesHibernateDAO() {
		return appConfigValuesHibernateDAO;
	}

	public void setAppConfigValuesHibernateDAO(
			AppConfigValuesHibernateDAO appConfigValuesHibernateDAO) {
		this.appConfigValuesHibernateDAO = appConfigValuesHibernateDAO;
	}
	
}
