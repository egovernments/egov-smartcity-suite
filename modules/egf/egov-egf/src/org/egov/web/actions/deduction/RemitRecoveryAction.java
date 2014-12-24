/**
 * 
 */
package org.egov.web.actions.deduction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.dao.voucher.VoucherHibernateDAO;
import org.egov.deduction.model.EgRemittance;
import org.egov.deduction.model.EgRemittanceDetail;
import org.egov.deduction.model.EgRemittanceGldtl;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.deduction.RemittanceBean;
import org.egov.model.payment.Paymentheader;
import org.egov.model.recoveries.Recovery;
import org.egov.model.voucher.CommonBean;
import org.egov.pims.commons.DesignationMaster;
import org.egov.services.deduction.RemitRecoveryService;
import org.egov.services.payment.PaymentService;
import org.egov.services.recoveries.RecoveryService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.payment.BasePaymentAction;
import org.egov.web.actions.voucher.CommonAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.HibernateException;
import org.hibernate.jdbc.Work;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DatabaseConnectionException;
import com.opensymphony.xwork2.validator.annotations.Validation;

/**
 * @author manoranjan
 * 
 */
@ParentPackage("egov")
@Validation
public class RemitRecoveryAction extends BasePaymentAction {
	private static final String MESSAGES = "messages";
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger
			.getLogger(RemitRecoveryAction.class);
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> mandatoryFields = new ArrayList<String>();
	private RemittanceBean remittanceBean = new RemittanceBean();
	private FinancialYearHibernateDAO financialYearDAO;
	private RemitRecoveryService remitRecoveryService;
	private VoucherService voucherService;
	private List<RemittanceBean> listRemitBean;
	private RecoveryService recoveryService;
	private CommonAction common;
	private Map<String, String> modeOfCollectionMap = new HashMap<String, String>();
	private PaymentService paymentService;
	private Paymentheader paymentheader;
	private static final String PAYMENTID = "paymentid";
	private static final String VIEW = "view";
	private SimpleWorkflowService<Paymentheader> paymentWorkflowService;
	public boolean showApprove = false;
	private CommonBean commonBean;
	private String modeOfPayment;
	private Integer departmentId;
	private String wfitemstate;
	private String comments;
	private static final String DD_MMM_YYYY = "dd-MMM-yyyy";
	private static final String FAILED = "Transaction failed";
	private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving data";
	// private String typeOfAccount;
	private Recovery recovery;
	private VoucherHibernateDAO voucherHibDAO;
	private EgovCommon egovCommon;
	private boolean showCancel = false;
	private boolean showButtons = true;
	private BigDecimal balance;
	private  ScriptService scriptService;

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Object getModel() {
		voucherHeader = (CVoucherHeader) super.getModel();
		voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
		return voucherHeader;
	}

	public RemitRecoveryAction() {
		voucherHeader.setVouchermis(new Vouchermis());
		addRelatedEntity("vouchermis.departmentid", DepartmentImpl.class);
		addRelatedEntity("fundId", Fund.class);
		addRelatedEntity("vouchermis.schemeid", Scheme.class);
		addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
		addRelatedEntity("vouchermis.functionary", Functionary.class);
		addRelatedEntity("fundsourceId", Fundsource.class);
		addRelatedEntity("vouchermis.divisionid", BoundaryImpl.class);
	}

	@Override
	public void prepare() {
		super.prepare();
		LOGGER.debug("Inside Prepare method");
		List<Recovery> listRecovery = recoveryService.getAllActiveTds();
		LOGGER.debug("RemitRecoveryAction | Tds list size : "
				+ listRecovery.size());
		addDropdownData("recoveryList", listRecovery);
		addDropdownData("bankList", Collections.EMPTY_LIST);
		addDropdownData("accNumList", Collections.EMPTY_LIST);
		modeOfCollectionMap.put("cash", getText("cash.consolidated.cheque"));
	}

	public String newform() {
		LOGGER.debug("RemitRecoveryAction | newform | start");
		reset();
		loadDefalutDates();
		return NEW;
	}

	private void reset() {
		commonBean.reset();
		voucherHeader.reset();
		remittanceBean = new RemittanceBean();
		listRemitBean = null;
	}

	@ValidationErrorPage(value = NEW)
	public String search() {
		try {
			LOGGER.debug("RemitRecoveryAction | Search | Start");
			validateFields();
			listRemitBean = remitRecoveryService.getRecoveryDetails(
					remittanceBean, voucherHeader);
			if (listRemitBean == null) {
				listRemitBean = new ArrayList<RemittanceBean>();
			}
		} catch (ValidationException e) {
			throw e;
		}
		return NEW;
	}

	public void prepareRemit() {
		addDropdownData("userList", Collections.EMPTY_LIST);
		loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
		loadDefalutDates();
	}

	@ValidationErrorPage(value = "new")
	public String remit() {
		voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
		LOGGER.debug("RemitRecoveryAction | remit | start");
		LOGGER.debug("RemitRecoveryAction | remit | size before filter"
				+ listRemitBean.size());

		Predicate remitPredicate = new RemittanceBean();
		for (RemittanceBean rbean : listRemitBean) {
			rbean.setPartialAmount(rbean.getAmount());
		}
		CollectionUtils.filter(listRemitBean, remitPredicate);
		LOGGER.debug("RemitRecoveryAction | remit | size after filter"
				+ listRemitBean.size());
		setModeOfPayment(FinancialConstants.MODEOFPAYMENT_CASH);
		LOGGER.debug("RemitRecoveryAction | remit | end");
		return "remitDetail";
	}

	public void prepareCreate() {

	}

	@ValidationErrorPage(value = "remitDetail")
	public String create() {
		try {
			validateFields();
			voucherHeader = createVoucherAndLedger();
			paymentheader = paymentService.createPaymentHeader(voucherHeader,
					Integer.valueOf(commonBean.getAccountNumberId()),
					getModeOfPayment(), remittanceBean.getTotalAmount());
			updateEg_Remittance_glDtl(voucherHeader);
			createMiscBillDetail();
			paymentWorkflowService.start(paymentheader,
					paymentService.getPosition(), "");
			sendForApproval();
			addActionMessage(getText("remittancepayment.transaction.success")
					+ voucherHeader.getVoucherNumber());
		} catch (NumberFormatException e) {
			loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
			loadAjaxedDropDowns();
			throw new ValidationException(Arrays.asList(new ValidationError(e
					.getMessage(), e.getMessage())));
		} catch (EGOVRuntimeException e) {
			loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
			loadAjaxedDropDowns();
			throw new ValidationException(Arrays.asList(new ValidationError(e
					.getMessage(), e.getMessage())));
		} catch (ValidationException e) {
			loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
			loadAjaxedDropDowns();
			throw e;
		}
		return MESSAGES;
	}

	/**
	 * 
	 * @param vh
	 * 
	 *            1.Creates one EgRemittance with paymentvoucher is set
	 *            2.updates every EgRemittanceGldtl for selected Bills 3.Creates
	 *            EgRemittanceDetail per selected Bill
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void updateEg_Remittance_glDtl(CVoucherHeader vh) {
		// Create Remittance
		EgRemittance remit = new EgRemittance();
		remit.setFund(voucherHeader.getFundId());
		remit.setRecovery(recovery);
		CFinancialYear financialYearByDate = financialYearDAO
				.getFinancialYearByDate(vh.getVoucherDate());
		remit.setFinancialyear(financialYearByDate);
		remit.setCreateddate(new Date());
		remit.setCreatedby(BigDecimal.valueOf(Integer.valueOf(EGOVThreadLocals
				.getUserId())));
		remit.setLastmodifiedby(BigDecimal.valueOf(Integer
				.valueOf(EGOVThreadLocals.getUserId())));
		remit.setLastmodifieddate(new Date());
		remit.setMonth(BigDecimal.valueOf(new Date().getMonth()));
		remit.setVoucherheader(vh);
		remit.setAsOnDate(voucherHeader.getVoucherDate());
		Set<EgRemittanceDetail> egRemittanceDetail = new HashSet<EgRemittanceDetail>();
		for (RemittanceBean rbean : listRemitBean) {
			// create EgRemittanceGldtl
			EgRemittanceGldtl remittancegldtl = (EgRemittanceGldtl) persistenceService
					.find("from EgRemittanceGldtl where id=?",
							rbean.getRemittance_gl_dtlId());
			remittancegldtl.setRemittedamt(rbean.getPartialAmount());
			persistenceService.setType(EgRemittanceGldtl.class);
			persistenceService.persist(remittancegldtl);
			// create EgRemittanceDetail
			EgRemittanceDetail remitDetail = new EgRemittanceDetail();
			remitDetail.setEgRemittance(remit);
			remitDetail.setEgRemittanceGldtl(remittancegldtl);
			remitDetail.setRemittedamt(rbean.getPartialAmount());
			remitDetail.setLastmodifieddate(new Date());
			egRemittanceDetail.add(remitDetail);

		}
		remit.setEgRemittanceDetail(egRemittanceDetail);
		persistenceService.setType(EgRemittance.class);
		persistenceService.persist(remit);
	}

	private CVoucherHeader createVoucherAndLedger() {
		voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
		voucherHeader
				.setName(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE);
		HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
		headerDetails
				.put(VoucherConstant.SOURCEPATH,
						"/EGF/deduction/remitRecovery!beforeView.action?voucherHeader.id=");
		HashMap<String, Object> detailMap = null;
		HashMap<String, Object> subledgertDetailMap = null;
		final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

		detailMap = new HashMap<String, Object>();
		detailMap.put(VoucherConstant.CREDITAMOUNT, remittanceBean
				.getTotalAmount().toString());
		detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
		Bankaccount account = (Bankaccount) persistenceService.find(
				"from Bankaccount where id=?",
				Integer.valueOf(commonBean.getAccountNumberId()));
		detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts()
				.getGlcode());
		accountdetails.add(detailMap);
		detailMap = new HashMap<String, Object>();
		detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
		detailMap.put(VoucherConstant.DEBITAMOUNT, remittanceBean
				.getTotalAmount().toString());
		recovery = (Recovery) persistenceService.find(
				"from Recovery where id=?", remittanceBean.getRecoveryId());
		detailMap.put(VoucherConstant.GLCODE, recovery.getChartofaccounts()
				.getGlcode());
		accountdetails.add(detailMap);
		subledgerDetails = addSubledgerGroupBy(subledgerDetails, recovery
				.getChartofaccounts().getGlcode());
		CreateVoucher cv = new CreateVoucher();
		voucherHeader = cv.createPreApprovedVoucher(headerDetails,
				accountdetails, subledgerDetails);
		return voucherHeader;
	}

	private List<HashMap<String, Object>> addSubledgerGroupBy(
			List<HashMap<String, Object>> subledgerDetails, String glcode) {
		Map<Integer, List<Integer>> detailTypesMap = new HashMap<Integer, List<Integer>>();
		Integer detailTypeId = null;
		List<Integer> detailTypeList = new ArrayList<Integer>();
		for (RemittanceBean rbean : listRemitBean) {
			detailTypeId = rbean.getDetailTypeId();
			if (detailTypeList.contains(detailTypeId)) {
				if (detailTypesMap.get(detailTypeId).contains(
						rbean.getDetailKeyid())) {
					continue;
				} else {
					detailTypesMap.get(detailTypeId)
							.add(rbean.getDetailKeyid());
				}

			} else {
				detailTypeList.add(detailTypeId);
				detailTypesMap.put(detailTypeId, new ArrayList<Integer>());
				detailTypesMap.get(detailTypeId).add(rbean.getDetailKeyid());

			}

		}
		Set<Entry<Integer, List<Integer>>> entrySet = detailTypesMap.entrySet();
		List<RemittanceBean> tempRemitBean = listRemitBean;
		for (Entry<Integer, List<Integer>> o : entrySet) {
			List<Integer> value = (List<Integer>) o.getValue();
			for (Integer detailKey : value) {
				BigDecimal sumPerDetailKey = BigDecimal.ZERO;
				// int lastIndexOf = tempRemitBean.lastIndexOf(detailKey);
				for (RemittanceBean remittanceBean2 : tempRemitBean) {
					if (remittanceBean2.getDetailKeyid() != null
							&& remittanceBean2.getDetailKeyid().equals(
									detailKey)) {
						sumPerDetailKey = sumPerDetailKey.add(remittanceBean2
								.getPartialAmount());
					}
				}
				HashMap<String, Object> subledgertDetailMap = new HashMap<String, Object>();
				subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT,
						sumPerDetailKey);
				subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT,
						BigDecimal.ZERO);
				subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, o
						.getKey().toString());
				subledgertDetailMap.put(VoucherConstant.DETAILKEYID, detailKey);
				subledgertDetailMap.put(VoucherConstant.GLCODE, glcode);
				subledgerDetails.add(subledgertDetailMap);
			}

		}
		return subledgerDetails;
	}

	public Paymentheader getPayment() {
		String paymentid = null;
		// System.out.println("Payment id is"+parameters.get(PAYMENTID));
		if (parameters.get(PAYMENTID) == null) {
			Object obj = getSession().get(PAYMENTID);
			if (obj != null) {
				paymentid = (String) obj;
			}
		} else {
			paymentid = parameters.get(PAYMENTID)[0];
		}
		if (paymentheader == null && paymentid != null) {
			paymentheader = (Paymentheader) persistenceService.find(
					"from Paymentheader where id=?", Long.valueOf(paymentid));
		}
		if (paymentheader == null) {
			paymentheader = new Paymentheader();
		}

		return paymentheader;
	}

	@SkipValidation
	public List<Action> getValidActions() {
		return paymentWorkflowService.getValidActions(getPayment());
	}

	@SuppressWarnings("unchecked")
	private void loadApproverUser(String atype) {
		addDropdownData("userList", Collections.EMPTY_LIST);
		String scriptName = "paymentHeader.nextDesg";
		if (paymentheader != null && paymentheader.getPaymentAmount() != null) {
			LOGGER.info("paymentheader.getPaymentAmount() >>>>>>>>>>>>>>>>>>> :"
					+ paymentheader.getPaymentAmount());
			atype = atype + "|" + paymentheader.getPaymentAmount();
		} else {
			atype = atype + "|";
		}
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		departmentId = voucherService.getCurrentDepartment().getId();
		LOGGER.info("departmentId :" + departmentId);
		Map<String, Object> map = voucherService.getDesgByDeptAndType(atype,
				scriptName);
		addDropdownData("departmentList", masterCache.get("egi-department"));

		List<Map<String, Object>> desgList = (List<Map<String, Object>>) map
				.get("designationList");
		String strDesgId = "", dName = "";
		boolean bDefaultDeptId = false;
		List<Map<String, Object>> designationList = new ArrayList<Map<String, Object>>();
		Map<String, Object> desgFuncryMap;
		for (Map<String, Object> desgIdAndName : desgList) {
			desgFuncryMap = new HashMap<String, Object>();

			if (desgIdAndName.get("designationName") != null) {
				desgFuncryMap.put("designationName",
						(String) desgIdAndName.get("designationName"));
			}

			if (desgIdAndName.get("designationId") != null) {
				strDesgId = (String) desgIdAndName.get("designationId");
				if (strDesgId.indexOf("~") != -1) {
					strDesgId = strDesgId.substring(0, strDesgId.indexOf('~'));
					dName = (String) desgIdAndName.get("designationId");
					dName = dName.substring(dName.indexOf('~') + 1);
					bDefaultDeptId = true;
				}
				desgFuncryMap.put("designationId", strDesgId);
			}
			designationList.add(desgFuncryMap);
		}
		map.put("designationList", designationList);

		addDropdownData("designationList",
				(List<DesignationMaster>) map.get("designationList"));

		if (bDefaultDeptId && !dName.equals("")) {
			DepartmentImpl dept = (DepartmentImpl) persistenceService
					.find("from DepartmentImpl where deptName like '%" + dName
							+ "' ");
			departmentId = dept.getId();
		}
		wfitemstate = map.get("wfitemstate") != null ? map.get("wfitemstate")
				.toString() : "";
	}

	@ValidationErrorPage(value = VIEW)
	@SkipValidation
	public String sendForApproval() {
		paymentheader = getPayment();
		// LOGGER.debug("Paymentheader==" + paymentheader.getId() +
		// ", actionname=" + parameters.get(ACTIONNAME)[0]);
		action = parameters.get(ACTIONNAME)[0];
		if (action.equalsIgnoreCase("cancelPayment"))
			return cancelPayment();

		Integer userId = null;
		if (parameters.get(ACTIONNAME)[0] != null
				&& parameters.get(ACTIONNAME)[0].contains("reject")) {
			userId = paymentheader.getCreatedBy().getId();
		} else if (null != parameters.get("approverUserId")
				&& Integer.valueOf(parameters.get("approverUserId")[0]) != -1) {
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		} else {
			userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		}

		LOGGER.debug("userId  ==" + userId);
		paymentWorkflowService.transition(parameters.get(ACTIONNAME)[0] + "|"
				+ userId, paymentheader, parameters.get("comments")[0]);
		paymentService.persist(paymentheader);
		if (parameters.get(ACTIONNAME)[0].contains("approve")) {
			if ("END".equals(paymentheader.getState().getValue())) {
				addActionMessage(getText("payment.voucher.final.approval"));
			} else {
				addActionMessage(getText("payment.voucher.approved",
						new String[] { paymentService
								.getEmployeeNameForPositionId(paymentheader
										.getState().getOwner()) }));
			}
			setAction(parameters.get(ACTIONNAME)[0]);

		} else {
			addActionMessage(getText("payment.voucher.rejected",
					new String[] { paymentService
							.getEmployeeNameForPositionId(paymentheader
									.getState().getOwner()) }));
		}
		return MESSAGES;
	}

	/**
	 * @return messages
	 */
	private String cancelPayment() {
		voucherHeader = paymentheader.getVoucherheader();
		voucherHeader.setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
		persistenceService.setType(CVoucherHeader.class);
		persistenceService.persist(voucherHeader);
		paymentWorkflowService.end(paymentheader, paymentService.getPosition());
		addActionMessage(getText("payment.voucher.cancelled"));
		return MESSAGES;
	}

	@ValidationErrorPage(value = VIEW)
	@SkipValidation
	public String approve() {
		paymentheader = getPayment();
		if (paymentheader.getState().getValue() != null
				&& !paymentheader.getState().getValue().isEmpty()
				&& paymentheader.getState().getValue().contains("Reject")) {
			voucherHeader.setId(paymentheader.getVoucherheader().getId());
			showCancel = true;
			return beforeEdit();
		}
		showApprove = true;
		voucherHeader.setId(paymentheader.getVoucherheader().getId());
		prepareForViewModifyReverse();
		loadApproverUser(voucherHeader.getType());
		return VIEW;
	}

	/**
	 * @return
	 */
	public String beforeEdit() {
		showApprove = true;
		voucherHeader.setId(paymentheader.getVoucherheader().getId());
		prepareForViewModifyReverse();
		loadApproverUser(voucherHeader.getType());
		return EDIT;
	}

	@ValidationErrorPage(value = EDIT)
	public String edit() {
		try {
			validateFields();
			voucherHeader = voucherService.updateVoucherHeader(voucherHeader);
			reCreateLedger();
			paymentheader = (Paymentheader) persistenceService.find(
					"from Paymentheader where voucherheader=?", voucherHeader);
			paymentService.updatePaymentHeader(paymentheader, voucherHeader,
					Integer.valueOf(commonBean.getAccountNumberId()),
					getModeOfPayment(), remittanceBean.getTotalAmount());
			// updateMiscBillDetail();
			sendForApproval();
			addActionMessage(getText("remittancepayment.transaction.success")
					+ voucherHeader.getVoucherNumber());
		} catch (ValidationException e) {

			loadApproverUser(voucherHeader.getType());
			loadAjaxedDropDowns();
			throw e;
		}
		return MESSAGES;
	}

	/**
	 * 
	 */
	private void reCreateLedger() {

		final CreateVoucher createVoucher = new CreateVoucher();
		try {
			HibernateUtil.getCurrentSession().doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {

					createVoucher.deleteVoucherdetailAndGL(connection,
							voucherHeader);
				
					HibernateUtil.getCurrentSession().flush();
					HashMap<String, Object> detailMap = null;
					HashMap<String, Object> subledgertDetailMap = null;
					final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
					List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

					detailMap = new HashMap<String, Object>();
					detailMap.put(VoucherConstant.CREDITAMOUNT, remittanceBean
							.getTotalAmount().toString());
					detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
					Bankaccount account = (Bankaccount) persistenceService
							.find("from Bankaccount where id=?", Integer
									.valueOf(commonBean.getAccountNumberId()));
					detailMap.put(VoucherConstant.GLCODE, account
							.getChartofaccounts().getGlcode());
					accountdetails.add(detailMap);
					detailMap = new HashMap<String, Object>();
					detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
					detailMap.put(VoucherConstant.DEBITAMOUNT, remittanceBean
							.getTotalAmount().toString());
					recovery = (Recovery) persistenceService.find(
							"from Recovery where id=?",
							remittanceBean.getRecoveryId());
					detailMap.put(VoucherConstant.GLCODE, recovery
							.getChartofaccounts().getGlcode());
					accountdetails.add(detailMap);
					subledgerDetails = addSubledgerGroupBy(subledgerDetails,
							recovery.getChartofaccounts().getGlcode());

					final List<Transaxtion> transactions = createVoucher
							.createTransaction(null, accountdetails,
									subledgerDetails, voucherHeader);
					HibernateUtil.getCurrentSession().flush();

					ChartOfAccounts engine = null;
					try {
						engine = ChartOfAccounts.getInstance();
					} catch (TaskFailedException e) {
						LOGGER.error(e.getMessage(), e);
						throw new ValidationException(Arrays.asList(new ValidationError(
								EXCEPTION_WHILE_SAVING_DATA, FAILED)));
					}
					
					Transaxtion txnList[] = new Transaxtion[transactions.size()];
					txnList = transactions.toArray(txnList);
					final SimpleDateFormat formatter = new SimpleDateFormat(
							DD_MMM_YYYY);
					try{
					if (!engine.postTransaxtions(txnList, connection,
							formatter.format(voucherHeader.getVoucherDate()))) {
						throw new ValidationException(Arrays
								.asList(new ValidationError(
										"Exception While Saving Data",
										"Transaction Failed")));
					}
					}catch(Exception ex) {
						throw new ValidationException(Arrays.asList(new ValidationError(
					EXCEPTION_WHILE_SAVING_DATA, FAILED)));
					}
						}});

				
		} catch (HibernateException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ValidationException(Arrays.asList(new ValidationError(
					EXCEPTION_WHILE_SAVING_DATA, FAILED)));
		} catch (NumberFormatException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ValidationException(Arrays.asList(new ValidationError(
					EXCEPTION_WHILE_SAVING_DATA, FAILED)));
		} catch (DatabaseConnectionException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ValidationException(Arrays.asList(new ValidationError(
					EXCEPTION_WHILE_SAVING_DATA, FAILED)));
		} catch (EGOVRuntimeException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ValidationException(Arrays.asList(new ValidationError(
					EXCEPTION_WHILE_SAVING_DATA, FAILED)));
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new ValidationException(Arrays.asList(new ValidationError(
					EXCEPTION_WHILE_SAVING_DATA, FAILED)));
		}

	}

	public String beforeView() {
		prepareForViewModifyReverse();
		wfitemstate = "END"; // requird to hide the approver drop down when view
								// is form source
		showApprove = true;
		showButtons = false;
		// loadApproverUser(voucherHeader.getType());
		return VIEW;
	}

	@SuppressWarnings("unchecked")
	private void prepareForViewModifyReverse() {
		remittanceBean = new RemittanceBean();
		voucherHeader = (CVoucherHeader) persistenceService.find(
				"from CVoucherHeader where id=?", voucherHeader.getId());
		paymentheader = (Paymentheader) persistenceService.find(
				"from Paymentheader where voucherheader=?", voucherHeader);
		commonBean.setAmount(paymentheader.getPaymentAmount());
		commonBean.setAccountNumberId(paymentheader.getBankaccount().getId()
				.toString());
		commonBean.setAccnumnar(paymentheader.getBankaccount().getNarration());

		String bankBranchId = paymentheader.getBankaccount().getBankbranch()
				.getBank().getId()
				+ "-" + paymentheader.getBankaccount().getBankbranch().getId();

		commonBean.setBankId(bankBranchId);
		setModeOfPayment(paymentheader.getType());
		remittanceBean.setTotalAmount(paymentheader.getPaymentAmount());
		setComments(getText("payment.comments", new String[] { paymentheader
				.getPaymentAmount().toPlainString() }));
		getRemittanceFromVoucher();

		loadAjaxedDropDowns();
		// find it last so that rest of the data loaded
		if (!"view".equalsIgnoreCase(showMode)) {
			validateUser("balancecheck");
		}
	}

	private void getRemittanceFromVoucher() {
		listRemitBean = new ArrayList<RemittanceBean>();

		List<EgRemittance> remitList = (List<EgRemittance>) persistenceService
				.findAllBy("from EgRemittance where voucherheader=?",
						voucherHeader);
		for (EgRemittance remit : remitList) {
			RemittanceBean rbean = null;
			Set<EgRemittanceDetail> egRemittanceDetail = remit
					.getEgRemittanceDetail();
			for (EgRemittanceDetail remitDtl : egRemittanceDetail) {
				rbean = new RemittanceBean();
				rbean.setPartialAmount(remitDtl.getRemittedamt());
				rbean.setAmount(remitDtl.getRemittedamt());
				rbean.setDetailTypeId(remitDtl.getEgRemittanceGldtl()
						.getGeneralledgerdetail().getAccountdetailtype()
						.getId());
				rbean.setDetailKeyid(remitDtl.getEgRemittanceGldtl()
						.getGeneralledgerdetail().getDetailkeyid().intValue());
				rbean.setRemittance_gl_dtlId(remitDtl.getEgRemittanceGldtl()
						.getId());
				EntityType entity = voucherHibDAO.getEntityInfo(remitDtl
						.getEgRemittanceGldtl().getGeneralledgerdetail()
						.getDetailkeyid().intValue(), remitDtl
						.getEgRemittanceGldtl().getGeneralledgerdetail()
						.getAccountdetailtype().getId());
				rbean.setPartyCode(entity.getCode());
				rbean.setPartyName(entity.getName());
				rbean.setPanNo(entity.getPanno());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				rbean.setVoucherDate(sdf.format(remitDtl.getEgRemittanceGldtl()
						.getGeneralledgerdetail().getGeneralledger()
						.getVoucherHeaderId().getVoucherDate()));
				rbean.setVoucherNumber(remitDtl.getEgRemittanceGldtl()
						.getGeneralledgerdetail().getGeneralledger()
						.getVoucherHeaderId().getVoucherNumber());
				rbean.setVoucherName(remitDtl.getEgRemittanceGldtl()
						.getGeneralledgerdetail().getGeneralledger()
						.getVoucherHeaderId().getName());
				listRemitBean.add(rbean);
			}

			if (remittanceBean.getRecoveryId() == null) {
				remittanceBean.setRecoveryId(remit.getRecovery().getId());
			}

		}
	}

	@SuppressWarnings("unchecked")
	@SkipValidation
	public boolean validateUser(String purpose) {
		Script validScript = (Script) scriptService.findAllByNamedQuery(Script.BY_NAME,"Paymentheader.show.bankbalance").get(0);
		List<String> list = (List<String>) scriptService.executeScript(validScript,scriptService.createContext("persistenceService", paymentService, "purpose",purpose));
		if (list.get(0).equals("true")) {
			try {
				commonBean
						.setAvailableBalance(egovCommon.getAccountBalance(new Date(), paymentheader.getBankaccount().getId(), paymentheader.getPaymentAmount(), paymentheader.getId()));
				balance = commonBean.getAvailableBalance();
				return true;
			} catch (Exception e) {
				balance = BigDecimal.valueOf(-1);

				return true;
			}
		} else {
			return false;
		}
	}

	private void loadAjaxedDropDowns() {
		loadSchemeSubscheme();
		loadBankBranchForFundAndType();
		loadBankAccountNumberForFundAndType();
	}

	private void loadBankBranchForFundAndType() {
		common.setFundId(voucherHeader.getFundId().getId());
		common.setTypeOfAccount("RECEIPTS_PAYMENTS,PAYMENTS");
		common.ajaxLoadBanksByFundAndType();
		addDropdownData("bankList", common.getBankBranchList());
	}

	private void loadBankAccountNumberForFundAndType() {
		Bankaccount bankaccount = null;

		if (paymentheader != null) {
			bankaccount = paymentheader.getBankaccount();
			common.setBranchId(bankaccount.getBankbranch().getId());
			common.setBankId(bankaccount.getBankbranch().getBank().getId());
		} else {
			if (commonBean.getAccountNumberId() != null
					&& !commonBean.getAccountNumberId().equals("-1")
					&& !commonBean.getAccountNumberId().equals("")) {
				bankaccount = (Bankaccount) persistenceService.find(
						"from Bankaccount where id=?",
						Integer.valueOf(commonBean.getAccountNumberId()));
				common.setBranchId(bankaccount.getBankbranch().getId());
				common.setBankId(bankaccount.getBankbranch().getBank().getId());
			}
		}
		if (common.getBranchId() != null) {
			common.setTypeOfAccount("RECEIPTS_PAYMENTS,PAYMENTS");
			common.ajaxLoadAccNumAndType();
			addDropdownData("accNumList", common.getAccNumList());
		} else {
			addDropdownData("accNumList", Collections.EMPTY_LIST);
		}
	}

	private void createMiscBillDetail() {
		Miscbilldetail miscbillDetail = new Miscbilldetail();
		// miscbillDetail.setBillnumber(commonBean.getDocumentNumber());
		// miscbillDetail.setBilldate(commonBean.getDocumentDate());
		miscbillDetail.setBillamount(remittanceBean.getTotalAmount());
		miscbillDetail.setPaidamount(remittanceBean.getTotalAmount());
		miscbillDetail.setPassedamount(remittanceBean.getTotalAmount());
		miscbillDetail.setPayVoucherHeader(voucherHeader);
		// miscbillDetail.setBillVoucherHeader(vhid);
		miscbillDetail.setPaidto(recovery.getRemitted());
		persistenceService.setType(Miscbilldetail.class);
		persistenceService.persist(miscbillDetail);

	}

	private void updateMiscBillDetail() {
		Miscbilldetail miscbillDetail = (Miscbilldetail) persistenceService
				.find(" from Miscbilldetail where payVoucherHeader=?",
						voucherHeader);
		miscbillDetail.setBillamount(remittanceBean.getTotalAmount());
		miscbillDetail.setPaidamount(remittanceBean.getTotalAmount());
		miscbillDetail.setPassedamount(remittanceBean.getTotalAmount());
		miscbillDetail.setPayVoucherHeader(voucherHeader);
		// miscbillDetail.setBillVoucherHeader(vhid);
		miscbillDetail.setPaidto(recovery.getRemitted());
		persistenceService.setType(Miscbilldetail.class);
		persistenceService.persist(miscbillDetail);
	}

	protected void loadDefalutDates() {
		final Date currDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			voucherHeader.setVoucherDate(sdf.parse(sdf.format(currDate)));
		} catch (final ParseException e) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"Exception while formatting voucher date",
					"Transaction failed")));
		}
	}

	public CVoucherHeader getVoucherHeader() {
		return voucherHeader;
	}

	public void setVoucherHeader(CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
	}

	public RemittanceBean getRemittanceBean() {
		return remittanceBean;
	}

	public void setRemittanceBean(RemittanceBean remittanceBean) {
		this.remittanceBean = remittanceBean;
	}

	public void setRemitRecoveryService(
			RemitRecoveryService remitRecoveryService) {
		this.remitRecoveryService = remitRecoveryService;
	}

	public List<RemittanceBean> getListRemitBean() {
		return listRemitBean;
	}

	public void setListRemitBean(List<RemittanceBean> listRemitBean) {
		this.listRemitBean = listRemitBean;
	}

	public void setRecoveryService(RecoveryService recoveryService) {
		this.recoveryService = recoveryService;
	}

	public void setCommon(CommonAction common) {
		this.common = common;
	}

	public Paymentheader getPaymentheader() {
		return paymentheader;
	}

	public void setPaymentheader(Paymentheader paymentheader) {
		this.paymentheader = paymentheader;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public void setPaymentWorkflowService(
			SimpleWorkflowService<Paymentheader> paymentWorkflowService) {
		this.paymentWorkflowService = paymentWorkflowService;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}

	public void setCommonBean(CommonBean commonBean) {
		this.commonBean = commonBean;
	}

	public CommonBean getCommonBean() {
		return this.commonBean;
	}

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public Map<String, String> getModeOfCollectionMap() {
		return modeOfCollectionMap;
	}

	public void setModeOfCollectionMap(Map<String, String> modeOfCollectionMap) {
		this.modeOfCollectionMap = modeOfCollectionMap;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public void setVoucherHibDAO(VoucherHibernateDAO voucherHibDAO) {
		this.voucherHibDAO = voucherHibDAO;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getWfitemstate() {
		return wfitemstate;
	}

	public void setWfitemstate(String wfitemstate) {
		this.wfitemstate = wfitemstate;
	}

	public boolean isShowButtons() {
		return showButtons;
	}

	public void setShowButtons(boolean showButtons) {
		this.showButtons = showButtons;
	}

	public boolean isShowCancel() {
		return showCancel;
	}

	public void setShowCancel(boolean showCancel) {
		this.showCancel = showCancel;
	}

}
