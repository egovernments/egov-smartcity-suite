package org.egov.services.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.BillsAccountingService;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.payment.ChequeAssignment;
import org.egov.model.payment.PaymentBean;
import org.egov.model.payment.Paymentheader;
import org.egov.payment.dao.PaymentDAOFactory;
import org.egov.payment.dao.PaymentheaderHibernateDAO;
import org.egov.payment.dao.SubledgerpaymentheaderHibernateDAO;
import org.egov.payment.model.Subledgerpaymentheader;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.services.cheque.ChequeService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.report.FundFlowService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.Query;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.transform.Transformers;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.VoucherTypeForULB;
import com.exilant.exility.common.TaskFailedException;

public class PaymentService extends PersistenceService<Paymentheader, Long> {
	private static final Logger LOGGER = Logger.getLogger(PaymentService.class);
	private CommonsService commonsService;
	private EisCommonsService eisCommonsService;
	private EmployeeService employeeService;
	public SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",
			Constants.LOCALE);
	public final SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yyyy", Constants.LOCALE);
	private GenericHibernateDaoFactory genericDao;
	protected PersistenceService persistenceService;
	public List<CChartOfAccounts> purchaseBillGlcodeList = new ArrayList<CChartOfAccounts>();
	public List<CChartOfAccounts> worksBillGlcodeList = new ArrayList<CChartOfAccounts>();
	public List<CChartOfAccounts> salaryBillGlcodeList = new ArrayList<CChartOfAccounts>();
	public List<CChartOfAccounts> contingentBillGlcodeList = new ArrayList<CChartOfAccounts>();
	public List<BigDecimal> cBillGlcodeIdList = null;
	protected List<Miscbilldetail> miscBillList = null;
	private static final String EMPTY_STRING = "";
	private static final String DELIMETER = "~";
	private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving Data";
	private static final String TRANSACTION_FAILED = "Transaction failed";
	private List<HashMap<String, Object>> accountcodedetails = null;
	private List<HashMap<String, Object>> subledgerdetails = null;
	private InstrumentService instrumentService;
	private ChequeService chequeService;
	private UserImpl user = null;
	private int conBillIdlength = 0;
	public Integer selectedRows = 0;
	private Date currentDate = new Date();
	List<InstrumentVoucher> instVoucherList;
	private BillsAccountingService billsAccountingService;
	private FundFlowService fundFlowService;

	CreateVoucher cv = new CreateVoucher();

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public BigDecimal getAccountBalance(String accountId, String voucherDate,
			BigDecimal amount, Long paymentId) throws ParseException {
		EgovCommon common = new EgovCommon();
		common.setPersistenceService(persistenceService);
		common.setGenericDao(genericDao);
		common.setFundFlowService(fundFlowService);
		return common.getAccountBalance(formatter.parse(voucherDate),
				Integer.valueOf(accountId), amount, paymentId);
	}

	public boolean isChequeNoGenerationAuto() {
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO()
				.getConfigValuesByModuleAndKey(Constants.EGF,
						"Cheque_no_generation_auto");
		String chequeNoGeneration = appList.get(0).getValue();
		if (chequeNoGeneration.equalsIgnoreCase("Y"))
			return true;
		else
			return false;
	}

	public Paymentheader createPayment(Map<String, String[]> parameters,
			HashMap<String, Object> headerdetails,
			List<HashMap<String, Object>> accountcodedetails,
			List<HashMap<String, Object>> subledgerdetails,
			Bankaccount bankaccount) {
		CreateVoucher createVoucher = new CreateVoucher();
		CVoucherHeader voucherHeader = createVoucher.createPreApprovedVoucher(
				headerdetails, accountcodedetails, subledgerdetails);
		Paymentheader paymentheader = createPaymentHeader(voucherHeader,
				bankaccount, parameters);
		paymentheader
				.getVoucherheader()
				.getVouchermis()
				.setSourcePath(
						"/EGF/payment/payment!view.action?paymentid="
								+ paymentheader.getId());
		return paymentheader;
	}

	public Paymentheader createPayment(Map<String, String[]> parameters,
			List<PaymentBean> billList, EgBillregister billregister)
			throws EGOVRuntimeException, ValidationException {
		Paymentheader paymentheader = null;
		try {
			accountcodedetails = new ArrayList<HashMap<String, Object>>();
			subledgerdetails = new ArrayList<HashMap<String, Object>>();
			conBillIdlength = 0;
			getGlcodeIds();

			CreateVoucher createVoucher = new CreateVoucher();
			HashMap<String, Object> headerdetails = new HashMap<String, Object>();

			user = (UserImpl) persistenceService.find(
					" from UserImpl where id = ?",
					Integer.valueOf(EGOVThreadLocals.getUserId()));
			if (billList != null && billList.size() > 0
					&& "salary".equalsIgnoreCase(billList.get(0).getExpType()))
				headerdetails.put(VoucherConstant.VOUCHERNAME,
						"Salary Bill Payment");
			else
				headerdetails.put(VoucherConstant.VOUCHERNAME, "Bill Payment");
			headerdetails.put(VoucherConstant.VOUCHERTYPE, "Payment");
			if (parameters.get(VoucherConstant.DESCRIPTION) != null)
				headerdetails.put(VoucherConstant.DESCRIPTION,
						parameters.get(VoucherConstant.DESCRIPTION)[0]);

			if (parameters.get(VoucherConstant.VOUCHERDATE) != null
					&& !parameters.get(VoucherConstant.VOUCHERDATE)[0]
							.equals(EMPTY_STRING))
				headerdetails.put(VoucherConstant.VOUCHERDATE, formatter
						.parse(parameters.get(VoucherConstant.VOUCHERDATE)[0]));

			if (billregister.getEgBillregistermis().getFund() != null)
				headerdetails.put(VoucherConstant.FUNDCODE, billregister
						.getEgBillregistermis().getFund().getCode());

			if (parameters.get(VoucherConstant.VOUCHERNUMBER) != null)
				headerdetails.put(VoucherConstant.VOUCHERNUMBER,
						parameters.get(VoucherConstant.VOUCHERNUMBER)[0]);

			if (billregister.getEgBillregistermis().getEgDepartment() != null)
				headerdetails
						.put(VoucherConstant.DEPARTMENTCODE, billregister
								.getEgBillregistermis().getEgDepartment()
								.getDeptCode());

			if (billregister.getEgBillregistermis().getFundsource() != null)
				headerdetails.put(VoucherConstant.FUNDSOURCECODE, billregister
						.getEgBillregistermis().getFundsource().getCode());

			if (billregister.getEgBillregistermis().getScheme() != null)
				headerdetails.put(VoucherConstant.SCHEMECODE, billregister
						.getEgBillregistermis().getScheme().getCode());

			if (billregister.getEgBillregistermis().getSubScheme() != null)
				headerdetails.put(VoucherConstant.SUBSCHEMECODE, billregister
						.getEgBillregistermis().getSubScheme().getCode());

			if (billregister.getEgBillregistermis().getFunctionaryid() != null)
				headerdetails.put(VoucherConstant.FUNCTIONARYCODE, billregister
						.getEgBillregistermis().getFunctionaryid().getCode());

			if (billregister.getEgBillregistermis().getFieldid() != null)
				headerdetails.put(VoucherConstant.DIVISIONID, billregister
						.getEgBillregistermis().getFieldid().getId());

			String[] contractorids = (parameters.get("contractorIds")[0] == null || parameters
					.get("contractorIds")[0].equals("")) ? null : parameters
					.get("contractorIds")[0].split(",");
			String[] supplierids = (parameters.get("supplierIds")[0] == null || parameters
					.get("supplierIds")[0].equals("")) ? null : parameters
					.get("supplierIds")[0].split(",");
			String[] salaryids = (parameters.get("salaryIds")[0] == null || parameters
					.get("salaryIds")[0].equals("")) ? null : parameters
					.get("salaryIds")[0].split(",");
			String[] contingencyIds = null;

			if (parameters.get("contingentIds") != null)
				contingencyIds = (parameters.get("contingentIds")[0] == null || parameters
						.get("contingentIds")[0].equals("")) ? null
						: parameters.get("contingentIds")[0].split(",");

			miscBillList = new ArrayList<Miscbilldetail>();

			prepareVoucherdetails(contractorids, parameters,
					worksBillGlcodeList, billList);
			if (contractorids != null)
				conBillIdlength = contractorids.length;
			prepareVoucherdetails(supplierids, parameters,
					purchaseBillGlcodeList, billList);
			prepareVoucherdetails(contingencyIds, parameters,
					contingentBillGlcodeList, billList);
			prepareVoucherdetails(salaryids, parameters, salaryBillGlcodeList,
					billList);

			// credit to the bank glcode
			HashMap<String, Object> accdetailsMap = new HashMap<String, Object>();
			Bankaccount ba = (Bankaccount) persistenceService.find(
					" from Bankaccount where id = ? ",
					Integer.valueOf(parameters.get("bankaccount")[0]));
			accdetailsMap.put(VoucherConstant.GLCODE, ba.getChartofaccounts()
					.getGlcode());
			accdetailsMap.put(VoucherConstant.NARRATION, ba
					.getChartofaccounts().getName());
			accdetailsMap.put(VoucherConstant.DEBITAMOUNT, 0);
			accdetailsMap.put(VoucherConstant.CREDITAMOUNT,
					parameters.get("grandTotal")[0]);
			accountcodedetails.add(accdetailsMap);

			CVoucherHeader voucherHeader = createVoucher
					.createPreApprovedVoucher(headerdetails,
							accountcodedetails, subledgerdetails);
			paymentheader = createPaymentHeader(voucherHeader, ba, parameters);

			persistenceService.setType(Miscbilldetail.class);
			for (Miscbilldetail miscbilldetail : miscBillList) {
				miscbilldetail.setPayVoucherHeader(voucherHeader);
				persistenceService.create(miscbilldetail);
			}
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			List<ValidationError> errors = new ArrayList<ValidationError>();
			errors.add(new ValidationError("createPayment", e.getMessage()));
			throw new ValidationException(errors);
		}
		return paymentheader;
	}

	private void prepareVoucherdetails(String[] ids,
			Map<String, String[]> parameters,
			List<CChartOfAccounts> glcodeList, List<PaymentBean> billList) {
		EgBillregister egBillregister = null;
		CGeneralLedger gl = null;
		CGeneralLedgerDetail ledgerDetail = null;
		String tmp = "";
		HashMap<String, BigDecimal> tmpaccdetailsMap = new HashMap<String, BigDecimal>();
		HashMap<String, BigDecimal> tmpsublegDetailMap = new HashMap<String, BigDecimal>();
		HashMap<String, Object> accdetailsMap = null;
		HashMap<String, Object> sublegDetailMap = null;

		if (ids != null)
			for (int i = 0; i < ids.length; i++) // do the aggregation
			{
				egBillregister = (EgBillregister) persistenceService.find(
						"from EgBillregister where id = ? ",
						Long.valueOf(ids[i]));
				generateMiscBill(egBillregister,
						billList.get(i + conBillIdlength).getPaymentAmt());
				gl = getPayableAccount(ids[i], glcodeList, "getGeneralLedger");

				tmp = gl.getGlcodeId().getGlcode() + DELIMETER
						+ gl.getGlcodeId().getName();
				if (tmpaccdetailsMap.get(tmp) == null)
					tmpaccdetailsMap.put(tmp, billList.get(i + conBillIdlength)
							.getPaymentAmt());
				else
					tmpaccdetailsMap.put(
							tmp,
							tmpaccdetailsMap.get(tmp).add(
									billList.get(i + conBillIdlength)
											.getPaymentAmt()));

				Iterator it = gl.getGeneralLedgerDetails().iterator();
				while (it.hasNext()) {
					ledgerDetail = (CGeneralLedgerDetail) it.next();
					if ("Salary".equalsIgnoreCase(billList.get(
							i + conBillIdlength).getExpType())) {
						tmp = gl.getId() + DELIMETER
								+ gl.getGlcodeId().getGlcode() + DELIMETER
								+ ledgerDetail.getDetailTypeId() + DELIMETER
								+ ledgerDetail.getDetailKeyId();
					} else {
						tmp = gl.getGlcodeId().getGlcode() + DELIMETER
								+ ledgerDetail.getDetailTypeId() + DELIMETER
								+ ledgerDetail.getDetailKeyId();
					}
					if (tmpsublegDetailMap.get(tmp) == null)
						tmpsublegDetailMap.put(tmp,
								billList.get(i + conBillIdlength)
										.getPaymentAmt());
					else
						tmpsublegDetailMap.put(
								tmp,
								tmpsublegDetailMap.get(tmp).add(
										billList.get(i + conBillIdlength)
												.getPaymentAmt()));
				}
			}

		// form the accountdetaillist and subledger list for bills
		Iterator conIterator = tmpaccdetailsMap.keySet().iterator();
		String key = "";

		while (conIterator.hasNext()) {
			key = conIterator.next().toString();
			accdetailsMap = new HashMap<String, Object>();
			accdetailsMap.put(VoucherConstant.GLCODE, key.split(DELIMETER)[0]);
			accdetailsMap.put(VoucherConstant.NARRATION,
					key.split(DELIMETER)[1]);
			accdetailsMap.put(VoucherConstant.DEBITAMOUNT,
					tmpaccdetailsMap.get(key));
			accdetailsMap.put(VoucherConstant.CREDITAMOUNT, 0);
			accountcodedetails.add(accdetailsMap);
		}

		conIterator = tmpsublegDetailMap.keySet().iterator();
		while (conIterator.hasNext()) {
			key = conIterator.next().toString();
			sublegDetailMap = new HashMap<String, Object>();
			if (key.split(DELIMETER).length == 4) {
				sublegDetailMap.put(VoucherConstant.GLCODE,
						key.split(DELIMETER)[1]);
				sublegDetailMap.put(VoucherConstant.DETAILTYPEID,
						key.split(DELIMETER)[2]);
				sublegDetailMap.put(VoucherConstant.DETAILKEYID,
						key.split(DELIMETER)[3]);
			} else {
				sublegDetailMap.put(VoucherConstant.GLCODE,
						key.split(DELIMETER)[0]);
				sublegDetailMap.put(VoucherConstant.DETAILTYPEID,
						key.split(DELIMETER)[1]);
				sublegDetailMap.put(VoucherConstant.DETAILKEYID,
						key.split(DELIMETER)[2]);
			}
			sublegDetailMap.put(VoucherConstant.DEBITAMOUNT,
					tmpsublegDetailMap.get(key));
			sublegDetailMap.put(VoucherConstant.CREDITAMOUNT,
					BigDecimal.valueOf(0));
			subledgerdetails.add(sublegDetailMap);
		}

	}

	protected Paymentheader createPaymentHeader(CVoucherHeader voucherHeader,
			Bankaccount ba, Map<String, String[]> parameters) {
		Paymentheader paymentheader = new Paymentheader();
		paymentheader.setType(parameters.get("paymentMode")[0]);
		paymentheader.setVoucherheader(voucherHeader);
		paymentheader.setBankaccount(ba);
		paymentheader.setPaymentAmount(BigDecimal.valueOf(Double
				.valueOf(parameters.get("grandTotal")[0])));
		persistenceService.setType(Paymentheader.class);
		persistenceService.create(paymentheader);
		return paymentheader;
	}

	protected void generateMiscBill(EgBillregister egBillregister,
			BigDecimal paidAmt) {
		Miscbilldetail miscbilldetail = new Miscbilldetail();
		miscbilldetail.setBillnumber(egBillregister.getBillnumber());
		miscbilldetail.setBilldate(egBillregister.getBilldate());
		miscbilldetail.setBillamount(egBillregister.getBillamount());
		miscbilldetail.setPassedamount(egBillregister.getPassedamount());
		miscbilldetail.setPaidamount(paidAmt);
		miscbilldetail.setPaidby(user);
		miscbilldetail.setPaidto(egBillregister.getEgBillregistermis()
				.getPayto());
		miscbilldetail.setBillVoucherHeader(egBillregister
				.getEgBillregistermis().getVoucherHeader());
		miscBillList.add(miscbilldetail);
	}

	public Paymentheader updatePayment(final Map<String, String[]> parameters,
			final List<PaymentBean> billList, final Paymentheader payheader)
			throws EGOVRuntimeException, ValidationException {
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Paymentheader>(){
			@Override
			public Paymentheader execute(Connection connection) throws SQLException {
			Paymentheader paymentheader = null;
		try {
			final Connection con =connection;
			miscBillList = new ArrayList<Miscbilldetail>();
			user = (UserImpl) persistenceService.find(
					" from UserImpl where id = ?",
					Integer.valueOf(EGOVThreadLocals.getUserId()));
			Bankaccount ba = (Bankaccount) persistenceService.find(
					"from Bankaccount where id=?", payheader.getBankaccount()
							.getId());
			paymentheader = (Paymentheader) persistenceService.find(
					" from Paymentheader where id=? ", payheader.getId());
			deleteMiscBill(paymentheader.getVoucherheader().getId(), con);
			CVoucherHeader voucher = updateVoucher(parameters, billList, ba,
					con, payheader);
			// update payment table
			paymentheader.setPaymentAmount(new BigDecimal(parameters
					.get("grandTotal")[0]));
			paymentheader.setType(payheader.getType());
			paymentheader.setBankaccount(ba);
			paymentheader.setVoucherheader(voucher);
			persistenceService.setType(Paymentheader.class);
			persistenceService.update(paymentheader);
			// update miscbill table
			persistenceService.setType(Miscbilldetail.class);
			for (Miscbilldetail miscbilldetail : miscBillList) {
				miscbilldetail.setPayVoucherHeader(paymentheader
						.getVoucherheader());
				persistenceService.create(miscbilldetail);
			}
			
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			List<ValidationError> errors = new ArrayList<ValidationError>();
			errors.add(new ValidationError("createPayment", e.getMessage()));
			throw new ValidationException(errors);
		}
		return paymentheader;
			}});
	
	}

	private CVoucherHeader updateVoucher(Map<String, String[]> parameters,
			List<PaymentBean> billList, Bankaccount ba, Connection con,
			Paymentheader paymentheader) throws Exception {
		final CreateVoucher createVoucher = new CreateVoucher();
		final CVoucherHeader existingVH = (CVoucherHeader) persistenceService
				.find(" from CVoucherHeader where id=?", paymentheader
						.getVoucherheader().getId());
		createVoucher.deleteVoucherdetailAndGL(con, existingVH);
		updateVoucherHeader(parameters, existingVH,
				paymentheader.getVoucherheader(), con);
		prepareVoucherDetailsForModify(billList, parameters, ba);

		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<CVoucherHeader>() {

			@Override
			public CVoucherHeader execute(Connection connection) throws SQLException {
				
				try {
					final List<Transaxtion> transactions = createVoucher.createTransaction(null, accountcodedetails, subledgerdetails, existingVH);
					HibernateUtil.getCurrentSession().flush();
					final ChartOfAccounts engine = ChartOfAccounts.getInstance();
					Transaxtion txnList[] = new Transaxtion[transactions.size()];
					txnList = transactions.toArray(txnList);
					
						if (!engine.postTransaxtions(txnList,connection,sdf.format(existingVH.getVoucherDate()))) {
						throw new ValidationException(Arrays.asList(new ValidationError(
								EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
						}
				}catch (TaskFailedException e) {
					LOGGER.error("Error Occured in PaymanetService | updateVoucher"+e,e);
				}
				catch (Exception e) {
					LOGGER.error("Error Occured in PaymanetService | updateVoucher"+e,e);
				}
				
				return existingVH;
			}
			
		});
		
		
	}

	private void updateVoucherHeader(Map<String, String[]> parameters,
			CVoucherHeader existingVH, CVoucherHeader voucherHeader,
			Connection con) throws Exception {
		EGovernCommon eGovernCommon = new EGovernCommon();
		String fiscal = eGovernCommon.getFiscalPeriod(
				formatter.format(voucherHeader.getVoucherDate()));
		String vNumGenMode = new VoucherTypeForULB()
				.readVoucherTypes("Payment");
		String autoVoucherType = FinancialConstants.PAYMENT_VOUCHERNO_TYPE;
		String manualVoucherNumber = "";
		if (parameters.get("voucherNumberSuffix") != null) {
			manualVoucherNumber = (String) parameters
					.get("voucherNumberSuffix")[0];
		}
		if (!voucherHeader.getVoucherDate().equals(existingVH.getVoucherDate())) {
			Calendar voucherHeaderDate = Calendar.getInstance();
			Calendar existingVHDate = Calendar.getInstance();
			voucherHeaderDate.setTime(voucherHeader.getVoucherDate());
			existingVHDate.setTime(existingVH.getVoucherDate());

			if (!(voucherHeaderDate.get(Calendar.YEAR) == existingVHDate
					.get(Calendar.YEAR))) {
				String voucherNumber = VoucherHelper.getGeneratedVoucherNumber(
						existingVH.getFundId().getId(), autoVoucherType,
						voucherHeader.getVoucherDate(), vNumGenMode,
						manualVoucherNumber);
				existingVH.setVoucherNumber(voucherNumber);

				String fundIdentifier = existingVH.getFundId().getIdentifier()
						.toString();
				String vType = fundIdentifier + "/"
						+ cv.getCgnType(existingVH.getType()) + "/CGVN";
				LOGGER.debug("vType" + vType);

				String eg_voucher = null;
				try {
					eg_voucher = eGovernCommon
							.getEg_Voucher(vType, fiscal, con);
				} catch (Exception e) {
					throw new EGOVRuntimeException(e.getMessage());
				}
				// String
				// vType=existingVH.getVoucherNumber().substring(0,Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
				// LOGGER.debug("Voucher type  : "+ vType);
				// String
				// eg_voucher=eGovernCommon.getEg_Voucher(vType,existingVH.getFiscalPeriodId().toString(),con);
				for (int i = eg_voucher.length(); i < 5; i++) {
					eg_voucher = "0" + eg_voucher;
				}
				existingVH.setDescription(voucherHeader.getDescription());
				existingVH.setVoucherDate(voucherHeader.getVoucherDate());
				existingVH.setCgvn(vType + eg_voucher);
			}

			if (!(voucherHeaderDate.get(Calendar.MONTH) == existingVHDate
					.get(Calendar.MONTH))) {
				String month = Integer.toString(voucherHeaderDate
						.get(Calendar.MONTH) + 1);
				StringBuffer formattedVHNumber = new StringBuffer();
				String[] temp = existingVH.getVoucherNumber().split("/");
				if (month.length() == 1) {
					month = "0" + month;
				}
				temp[temp.length - 2] = month;
				for (int i = 0; i < temp.length; i++) {
					formattedVHNumber.append(temp[i]);
					if (i != 3) {
						formattedVHNumber.append("/");
					}
				}
				existingVH.setVoucherNumber(formattedVHNumber.toString());
			}
		}
		/*
		 * if ("Auto".equalsIgnoreCase(vNumGenMode)) {
		 * LOGGER.debug("Voucher number generation mode is : "+ vNumGenMode);
		 * existingVH
		 * .setVoucherNumber(cmImpl.getTxnNumber(existingVH.getFundId()
		 * .getId().toString(),autoVoucherType,vDate,con)); }else {
		 * existingVH.setVoucherNumber
		 * (parameters.get("voucherNumberPrefix")[0]+parameters
		 * .get("voucherNumberSuffix")[0]); }
		 */
		existingVH.setModifiedDate(new Date());
		existingVH.setModifiedBy(user);
		persistenceService.setType(CVoucherHeader.class);
		persistenceService.update(existingVH);
	}

	private void prepareVoucherDetailsForModify(
			List<PaymentBean> paymentBillList,
			Map<String, String[]> parameters, Bankaccount ba) {
		CGeneralLedger gl = null;
		EgBillregister br = null;
		CGeneralLedgerDetail ledgerDetail = null;
		getGlcodeIds();
		String tmp = "";
		HashMap<String, BigDecimal> tmpaccdetailsMap = new HashMap<String, BigDecimal>();
		HashMap<String, BigDecimal> tmpsublegDetailMap = new HashMap<String, BigDecimal>();
		HashMap<String, Object> accdetailsMap = null;
		HashMap<String, Object> sublegDetailMap = null;
		accountcodedetails = new ArrayList<HashMap<String, Object>>();
		subledgerdetails = new ArrayList<HashMap<String, Object>>();
		for (PaymentBean bean : paymentBillList) // do the aggregation
		{
			if (bean.getIsSelected()) {
				br = (EgBillregister) persistenceService.find(
						"from EgBillregister where billnumber = ?",
						bean.getBillNumber());
				prepareMiscBill(bean, br);
				if (br.getExpendituretype().equals("Works"))
					gl = getPayableAccount(bean.getCsBillId().toString(),
							worksBillGlcodeList,
							"getGeneralLedgerByVoucherHeaderId");
				else if (br.getExpendituretype().equals("Purchase"))
					gl = getPayableAccount(bean.getCsBillId().toString(),
							purchaseBillGlcodeList,
							"getGeneralLedgerByVoucherHeaderId");
				else
					gl = getPayableAccount(bean.getCsBillId().toString(),
							contingentBillGlcodeList,
							"getGeneralLedgerByVoucherHeaderId");

				tmp = gl.getGlcodeId().getGlcode() + DELIMETER
						+ gl.getGlcodeId().getName();
				if (tmpaccdetailsMap.get(tmp) == null)
					tmpaccdetailsMap.put(tmp, bean.getPaymentAmt());
				else
					tmpaccdetailsMap
							.put(tmp,
									tmpaccdetailsMap.get(tmp).add(
											bean.getPaymentAmt()));

				Iterator it = gl.getGeneralLedgerDetails().iterator();
				while (it.hasNext()) {
					ledgerDetail = (CGeneralLedgerDetail) it.next();

					tmp = gl.getGlcodeId().getGlcode() + DELIMETER
							+ ledgerDetail.getDetailTypeId() + DELIMETER
							+ ledgerDetail.getDetailKeyId();
					if (tmpsublegDetailMap.get(tmp) == null)
						tmpsublegDetailMap.put(tmp, bean.getPaymentAmt());
					else
						tmpsublegDetailMap.put(tmp, tmpsublegDetailMap.get(tmp)
								.add(bean.getPaymentAmt()));
				}
			}
		}

		// form the accountdetaillist and subledger list for bills
		Iterator conIterator = tmpaccdetailsMap.keySet().iterator();
		String key = "";

		while (conIterator.hasNext()) {
			key = conIterator.next().toString();
			accdetailsMap = new HashMap<String, Object>();
			accdetailsMap.put(VoucherConstant.GLCODE, key.split(DELIMETER)[0]);
			accdetailsMap.put(VoucherConstant.NARRATION,
					key.split(DELIMETER)[1]);
			accdetailsMap.put(VoucherConstant.DEBITAMOUNT,
					tmpaccdetailsMap.get(key));
			accdetailsMap.put(VoucherConstant.CREDITAMOUNT, 0);
			accountcodedetails.add(accdetailsMap);
		}

		conIterator = tmpsublegDetailMap.keySet().iterator();
		while (conIterator.hasNext()) {
			key = conIterator.next().toString();
			sublegDetailMap = new HashMap<String, Object>();
			sublegDetailMap
					.put(VoucherConstant.GLCODE, key.split(DELIMETER)[0]);
			sublegDetailMap.put(VoucherConstant.DETAILTYPEID,
					key.split(DELIMETER)[1]);
			sublegDetailMap.put(VoucherConstant.DETAILKEYID,
					key.split(DELIMETER)[2]);
			sublegDetailMap.put(VoucherConstant.DEBITAMOUNT,
					tmpsublegDetailMap.get(key));
			subledgerdetails.add(sublegDetailMap);
		}

		accdetailsMap = new HashMap<String, Object>();
		accdetailsMap.put(VoucherConstant.GLCODE, ba.getChartofaccounts()
				.getGlcode());
		accdetailsMap.put(VoucherConstant.NARRATION, ba.getChartofaccounts()
				.getName());
		accdetailsMap.put(VoucherConstant.DEBITAMOUNT, 0);
		accdetailsMap.put(VoucherConstant.CREDITAMOUNT,
				parameters.get("grandTotal")[0]);
		accountcodedetails.add(accdetailsMap);

	}

	protected void prepareMiscBill(PaymentBean bean, EgBillregister br) {
		Miscbilldetail miscbilldetail = new Miscbilldetail();
		miscbilldetail.setBillnumber(bean.getBillNumber());
		miscbilldetail.setBilldate(bean.getBillDate());
		miscbilldetail.setBillamount(bean.getNetAmt());
		miscbilldetail.setPassedamount(bean.getPassedAmt());
		miscbilldetail.setPaidamount(bean.getPaymentAmt());
		miscbilldetail.setPaidby(user);
		miscbilldetail.setPaidto(bean.getPayTo());
		miscbilldetail.setBillVoucherHeader(br.getEgBillregistermis()
				.getVoucherHeader());
		miscBillList.add(miscbilldetail);
	}

	protected void deleteMiscBill(Long payVHId, Connection con) {
		try {
			Statement st = con.createStatement();
			st.executeUpdate("delete from miscbilldetail where PAYVHID="
					+ payVHId);
		}catch (SQLException e) {
			LOGGER.error("Inside exception deleteMiscBill" + e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
	}

	private CGeneralLedger getPayableAccount(String id,
			List<CChartOfAccounts> glcodeIdList, String namedQuery) {
		return (CGeneralLedger) persistenceService.findByNamedQuery(namedQuery,
				Long.valueOf(id), glcodeIdList);
	}

	public void getGlcodeIds() throws EGOVRuntimeException {
		try {
			List<AppConfigValues> appList;
			worksBillGlcodeList = populateGlCodeIds(Constants.WORKS_BILL_PURPOSE_IDS);
			purchaseBillGlcodeList = populateGlCodeIds(Constants.PURCHASE_BILL_PURPOSE_IDS);
			salaryBillGlcodeList = populateGlCodeIds("salaryBillPurposeIds");

			// Contingent Bill
			appList = genericDao.getAppConfigValuesDAO()
					.getConfigValuesByModuleAndKey(Constants.EGF,
							Constants.CONTINGENCY_BILL_PURPOSE_IDS);
			cBillGlcodeIdList = new ArrayList<BigDecimal>();
			if (appList != null && appList.size() > 0) {
				Integer iPurposeIds[] = new Integer[appList.size()];
				int z = 0;
				for (final AppConfigValues appConfigValues : appList) {
					iPurposeIds[z] = Integer.parseInt(appConfigValues
							.getValue());
					z++;
				}
				final List<CChartOfAccounts> coaList = commonsService
						.getAccountCodeByListOfPurposeId(iPurposeIds);
				LOGGER.debug("Size contingentBillGlcodeList" + coaList.size());
				contingentBillGlcodeList = coaList;
				for (CChartOfAccounts coa1 : coaList) {
					// LOGGER.debug("Adding to contingentBillGlcodeList"+coa1.getGlcode()+":::"+coa1.getPurposeId());
					cBillGlcodeIdList.add(BigDecimal.valueOf(coa1.getId()));
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
	}

	private List<CChartOfAccounts> populateGlCodeIds(String appConfigKey)
			throws EGOVException {
		List<CChartOfAccounts> glCodeList = new ArrayList<CChartOfAccounts>();
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO()
				.getConfigValuesByModuleAndKey(Constants.EGF, appConfigKey);
		String purposeids = appList.get(0).getValue();
		if (purposeids != null && !purposeids.equals("")) {
			final String purposeIds[] = purposeids.split(",");
			for (final String purposeId : purposeIds) {
				final List<CChartOfAccounts> coaList = commonsService
						.getAccountCodeByPurpose(Integer.parseInt(purposeId));
				for (CChartOfAccounts coa1 : coaList)
					glCodeList.add(coa1);
			}
		}
		return glCodeList;
	}

	public Map<Long, BigDecimal> getDeductionAmt(List<EgBillregister> billList,
			String type) {
		// getGlcodeIds();
		LOGGER.debug("Calling getDeductionAmt..................................$$$$$$$$$$$$$$$$$$$$$$ ");
		Map<String, List<CChartOfAccounts>> glCodeList = new HashMap<String, List<CChartOfAccounts>>();
		glCodeList.put("Works", worksBillGlcodeList);
		// LOGGER.debug("Works"+glCodeList.size());
		glCodeList.put("Purchase", purchaseBillGlcodeList);
		// LOGGER.debug(glCodeList.size());

		glCodeList.put(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT,
				contingentBillGlcodeList);
		// LOGGER.debug(glCodeList.size());
		glCodeList.put(FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY,
				salaryBillGlcodeList);
		// LOGGER.debug(glCodeList.size());
		Map<Long, BigDecimal> deductionAmtMap = new HashMap<Long, BigDecimal>();
		List<CChartOfAccounts> list = glCodeList.get(type);
		LOGGER.info("Calling getDeductionAmt..................................$$$$$$$$$$$$$$$$$$$$$$ "
				+ list.size());
		if (LOGGER.isDebugEnabled()) {

			for (CChartOfAccounts coa : list) {
				LOGGER.debug("#################################"
						+ coa.getGlcode() + ":::::" + coa.getPurposeId());
			}
		}
		populateDeductionData(billList, deductionAmtMap, type,
				glCodeList.get(type));

		return deductionAmtMap;
	}
	
	public List<Subledgerpaymentheader> getSubledgerpaymentheaderByVoucherHeader(CVoucherHeader voucherHeader)
	{
		SubledgerpaymentheaderHibernateDAO slphDao=PaymentDAOFactory.getDAOFactory().getSubledgerpaymentheaderDAO();
		return slphDao.getSubledgerpaymentheaderByVoucherHeader(voucherHeader);
	}
	
	public List<Paymentheader> getPaymentheaderByVoucherHeader(CVoucherHeader voucherHeader)
	{
		PaymentheaderHibernateDAO phDao=PaymentDAOFactory.getDAOFactory().getPaymentheaderDAO();
		return phDao.getPaymentheaderByVoucherHeader(voucherHeader);
	}
	
	private void populateDeductionData(List<EgBillregister> billList,
			Map<Long, BigDecimal> deductionAmtMap, String type,
			List<CChartOfAccounts> glcodeList) {
		List<Object[]> dedList;
		List<Long> billIds = new ArrayList<Long>();
		if (billList != null && billList.size() != 0)
			for (EgBillregister row : billList) {
				billIds.add(row.getId());
			}
		if (billList != null && billList.size() != 0) {
			dedList = getDeductionList(type, glcodeList);
			if (dedList != null && dedList.size() != 0) {
				for (Object[] obj : dedList) {
					long id = ((BigDecimal) obj[0]).longValue();
					if (billIds.contains(id)) {
						deductionAmtMap.put(id,
								(obj[1] == null) ? BigDecimal.ZERO
										: (BigDecimal) obj[1]);
					}
				}
			}
		}
	}

	private List<Object[]> getDeductionList(String expendituretype,
			List<CChartOfAccounts> glcodeList) {
		List<Object[]> dedList;
		String mainquery = "select bill.id as id, sum (gl.creditAmount) from eg_Billregister bill,eg_billregistermis billmis left join "
				+ "voucherheader vh on vh.id=billmis.voucherheaderid left join (select sum(paidamount) as paidamount,billvhid as billvhid from miscbilldetail  group by "
				+ "billvhid) misc on misc.billvhid=vh.id,GeneralLedger gl where billmis.voucherheaderid is not null and billmis.billid=bill.id and "
				+ "vh.status=0 and bill.expendituretype='"
				+ expendituretype
				+ "' and gl.voucherHeaderId=billmis.voucherHeaderid and gl.glcodeId not in(:glCodeList) and "
				+ "gl.creditAmount>0 and (misc.billvhid is null or (bill.passedamount > misc.paidamount)) group by bill.id";
		dedList = persistenceService.getSession().createSQLQuery(mainquery)
				.setParameterList("glCodeList", glcodeList).list();
		return dedList;
	}

	private List<Object[]> getEarlierPaymentAmtList(String expendituretype) {
		List<Object[]> dedList;
		String mainquery = "select bill.id as id,misc.paidamount from eg_Billregister bill,eg_billregistermis billmis left join "
				+ "voucherheader vh on vh.id=billmis.voucherheaderid left join (select sum(paidamount) as paidamount,billvhid as billvhid from miscbilldetail  misc,voucherheader vh where  misc.payvhid=vh.id and vh.status not in (1,2,4)    group by "
				+ "billvhid) misc on misc.billvhid=vh.id where billmis.voucherheaderid is not null and billmis.billid=bill.id and "
				+ "vh.status=0 and bill.expendituretype='"
				+ expendituretype
				+ "' and (bill.passedamount > misc.paidamount)";
		dedList = persistenceService.getSession().createSQLQuery(mainquery)
				.list();
		return dedList;
	}

	public Map<Long, BigDecimal> getEarlierPaymentAmt(
			List<EgBillregister> billList, String type) {
		Map<Long, BigDecimal> paymentAmtMap = new HashMap<Long, BigDecimal>();
		List<Object[]> paidList;
		List<Long> billIds = new ArrayList<Long>();
		if (billList != null && billList.size() != 0)
			for (EgBillregister row : billList) {
				billIds.add(row.getId());
			}
		if (billList != null && billList.size() != 0) {
			paidList = getEarlierPaymentAmtList(type);
			if (paidList != null && paidList.size() != 0) {
				for (Object[] obj : paidList) {
					long id = ((BigDecimal) obj[0]).longValue();
					if (billIds.contains(id)) {
						paymentAmtMap.put(((BigDecimal) obj[0]).longValue(),
								(obj[1] == null) ? BigDecimal.ZERO
										: (BigDecimal) obj[1]);
					}
				}
			}
		}
		return paymentAmtMap;
	}

	private void validateEntity(EntityType entity) {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if (StringUtils.isBlank(entity.getPanno())
				|| StringUtils.isBlank(entity.getBankname())
				|| StringUtils.isBlank(entity.getBankaccount())
				|| StringUtils.isBlank(entity.getIfsccode())) {
			LOGGER.error("BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
					+ entity.getName());
			errors.add(new ValidationError(
					"paymentMode",
					"BankName, BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
							+ entity.getName()));
			throw new ValidationException(errors);
		}
	}

	/**
	 * if mode is Create - checking with bill id, if mode is modify- checking
	 * with billvoucherid
	 * 
	 * @param bean
	 * @param mode
	 * @throws ValidationException
	 * @throws EGOVException
	 */
	private void validateCBill(PaymentBean bean, String mode)
			throws ValidationException, EGOVException {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		EntityType entity = null;
		List<Object[]> list = null;

		// check the payee deails for payable code
		if (mode.equalsIgnoreCase("Create"))
			list = persistenceService.findAllByNamedQuery(
					"getPayeeDetailsForPayableCode", bean.getBillId(),
					cBillGlcodeIdList);
		else
			list = persistenceService.findAllByNamedQuery(
					"getPayeeDetailsForPayableCodeForVoucher",
					bean.getBillId(), contingentBillGlcodeList);

		if (list == null || list.size() == 0) {
			// check the payeedetails for debit code
			if (mode.equalsIgnoreCase("Create"))
				list = (List<Object[]>) persistenceService.findAllByNamedQuery(
						"getPayeeDetailsForDebitCode", bean.getBillId());
			else
				list = (List<Object[]>) persistenceService.findAllByNamedQuery(
						"getPayeeDetailsForDebitCodeForVoucher",
						bean.getBillId());
			if (list == null || list.size() == 0) {
				LOGGER.error("Sub ledger details are missing for this bill id ->"
						+ bean.getBillId());
				errors.add(new ValidationError("entityType",
						"Sub ledger details are missing for this bill number : "
								+ bean.getBillNumber()));
				throw new ValidationException(errors);
			} else {
				for (Object[] obj : list) {
					entity = getEntity(Integer.valueOf(obj[0].toString()),
							Long.valueOf(obj[1].toString()));
					validateEntity(entity);
				}
			}
		} else {
			for (Object[] obj : list) {
				entity = getEntity(Integer.valueOf(obj[0].toString()),
						Long.valueOf(obj[1].toString()));
				validateEntity(entity);
			}
		}
	}

	public void validateForRTGSPayment(List<PaymentBean> billList, String type)
			throws ValidationException, EGOVException {
		getGlcodeIds();
		EntityType entity = null;
		List<ValidationError> errors = new ArrayList<ValidationError>();
		Object[] obj = null;
		if (billList != null)
			for (PaymentBean bean : billList) {
				if (!bean.getIsSelected())
					continue;

				if (type.equals("Contractor"))
					obj = (Object[]) persistenceService.findByNamedQuery(
							"getGlDetailForPayableCode", bean.getBillId(),
							worksBillGlcodeList);
				else if (type.equals("Supplier"))
					obj = (Object[]) persistenceService.findByNamedQuery(
							"getGlDetailForPayableCode", bean.getBillId(),
							purchaseBillGlcodeList);
				else if (type
						.equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT)) {
					validateCBill(bean, "Create");
				}

				if (type.equals("Contractor") || type.equals("Supplier")) {
					if (obj == null) {
						LOGGER.error("Sub ledger details are missing for this bill id ->"
								+ bean.getBillId());
						errors.add(new ValidationError("entityType",
								"Sub ledger details are missing for this bill number : "
										+ bean.getBillNumber()));
						throw new ValidationException(errors);
					}
					entity = getEntity(Integer.valueOf(obj[0].toString()),
							(Serializable) obj[1]);

					if (type.equals("Supplier")
							&& (StringUtils.isBlank(entity.getTinno())
									|| StringUtils
											.isBlank(entity.getBankname())
									|| StringUtils.isBlank(entity
											.getBankaccount()) || StringUtils
										.isBlank(entity.getIfsccode()))) {
						LOGGER.error("BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
								+ entity.getName());
						errors.add(new ValidationError(
								"paymentMode",
								"BankName, BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
										+ entity.getName()));
						throw new ValidationException(errors);
					} else
						validateEntity(entity);
				}
			}
	}

	public void validateRTGSPaymentForModify(List<PaymentBean> billList)
			throws ValidationException, EGOVException {
		getGlcodeIds();
		EntityType entity = null;
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if (billList != null)
			for (PaymentBean bean : billList) {
				if (!bean.getIsSelected())
					continue;
				Object[] obj = (Object[]) persistenceService
						.find("select gld.detailTypeId,gld.detailKeyId,billmis.egBillregister.expendituretype from CGeneralLedgerDetail gld,CGeneralLedger gl,EgBillregistermis billmis where gl.id=gld.generalLedgerId and billmis.voucherHeader = gl.voucherHeaderId and billmis.voucherHeader.id=?",
								bean.getCsBillId());
				if (obj == null) {
					LOGGER.error("Sub ledger details are missing for this bill number ->"
							+ bean.getBillNumber());
					errors.add(new ValidationError("entityType",
							"Sub ledger details are missing for this bill number->"
									+ bean.getBillNumber()));
					throw new ValidationException(errors);
				}

				if (obj[2]
						.equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT)) {
					validateCBill(bean, "Modify");
				} else {
					entity = getEntity(Integer.valueOf(obj[0].toString()),
							(Serializable) obj[1]);
					if (obj[2].equals("Works")
							&& (StringUtils.isBlank(entity.getPanno())
									|| StringUtils
											.isBlank(entity.getBankname())
									|| StringUtils.isBlank(entity
											.getBankaccount()) || StringUtils
										.isBlank(entity.getIfsccode()))) {
						LOGGER.error("BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
								+ entity.getName());
						errors.add(new ValidationError(
								"paymentMode",
								"BankName, BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
										+ entity.getName()));
						throw new ValidationException(errors);
					}
					if (obj[2].equals("Purchase")
							&& (StringUtils.isBlank(entity.getTinno())
									|| StringUtils
											.isBlank(entity.getBankname())
									|| StringUtils.isBlank(entity
											.getBankaccount()) || StringUtils
										.isBlank(entity.getIfsccode()))) {
						LOGGER.error("BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
								+ entity.getName());
						errors.add(new ValidationError(
								"paymentMode",
								"BankName, BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
										+ entity.getName()));
						throw new ValidationException(errors);
					}
				}
			}
	}

	public EntityType getEntity(Integer detailTypeId, Serializable detailKeyId)
			throws EGOVException {
		EntityType entity;
		try {
			Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService
					.find(" from Accountdetailtype where id=?", detailTypeId);
			try {
				entity = (EntityType) persistenceService.find(" from "
						+ accountdetailtype.getFullQualifiedName()
						+ " where id=? ", Integer.valueOf(detailKeyId + ""));
			} catch (Exception ee) {
				entity = (EntityType) persistenceService.find(" from "
						+ accountdetailtype.getFullQualifiedName()
						+ " where id=? ", Long.valueOf(detailKeyId + ""));
			}
		} catch (Exception e) {
			LOGGER.error("Exception to get EntityType=" + e.getMessage());
			throw new EGOVException("Exception to get EntityType="
					+ e.getMessage());
		}
		return entity;
	}

	public List<PaymentBean> getMiscBillList(final Paymentheader header) {
		List<PaymentBean> paymentBeanList = null;
		Query query = getSession()
				.createSQLQuery(
						"select mb.billvhId as billId,mb.billnumber as billNumber,mb.billdate as billDate,mb.paidto as payTo,mb.amount as netAmt,  "
								+ " mb.passedamount as passedAmt,mb.paidamount as paymentAmt,br.expendituretype as expType from miscbilldetail mb, eg_billregister br , eg_billregistermis mis "
								+ " where mb.payvhid="
								+ header.getVoucherheader().getId()
								+ " and br.id= mis.billid and mis.voucherheaderid=billvhid order by mb.paidto,mb.BILLDATE")
				.addScalar("billId")
				.addScalar("billNumber")
				.addScalar("billDate")
				.addScalar("payTo")
				.addScalar("netAmt")
				.addScalar("passedAmt")
				.addScalar("paymentAmt")
				.addScalar("expType")
				.setResultTransformer(
						Transformers.aliasToBean(PaymentBean.class));
		paymentBeanList = query.list();
		BigDecimal earlierAmt;
		for (PaymentBean bean : paymentBeanList) {
			bean.setIsSelected(true);
			earlierAmt = (BigDecimal) persistenceService
					.find(" select sum(paidamount) from Miscbilldetail where billVoucherHeader.id=?",
							bean.getCsBillId());
			if (earlierAmt == null)
				earlierAmt = BigDecimal.ZERO;
			bean.setEarlierPaymentAmt(earlierAmt.subtract(bean.getPaymentAmt()));
			bean.setPayableAmt(bean.getNetAmt().subtract(
					bean.getEarlierPaymentAmt()));
		}
		return paymentBeanList;
	}

	// this will be used for all paymentVouchers
	List<ChequeAssignment> chequeList = null;

	public List<ChequeAssignment> getPaymentVoucherNotInInstrument(
			Map<String, String[]> parameters, CVoucherHeader voucherHeader)
			throws EGOVException, ParseException {
		StringBuffer sql = new StringBuffer();
		if (!"".equals(parameters.get("fromDate")[0]))
			sql.append(" and vh.voucherDate>='"
					+ sdf.format(formatter.parse(parameters.get("fromDate")[0]))
					+ "' ");
		if (!"".equals(parameters.get("toDate")[0]))
			sql.append(" and vh.voucherDate<='"
					+ sdf.format(formatter.parse(parameters.get("toDate")[0]))
					+ "'");
		if (!StringUtils.isEmpty(voucherHeader.getVoucherNumber()))
			sql.append(" and vh.voucherNumber like '%"
					+ voucherHeader.getVoucherNumber() + "%'");
		if (voucherHeader.getFundId() != null)
			sql.append(" and vh.fundId=" + voucherHeader.getFundId().getId());
		if (voucherHeader.getFundsourceId() != null)
			sql.append(" and vmis.fundsourceId="
					+ voucherHeader.getFundsourceId().getId());
		if (voucherHeader.getVouchermis().getDepartmentid() != null)
			sql.append(" and vmis.departmentid="
					+ voucherHeader.getVouchermis().getDepartmentid().getId());
		if (voucherHeader.getVouchermis().getSchemeid() != null)
			sql.append(" and vmis.schemeid="
					+ voucherHeader.getVouchermis().getSchemeid().getId());
		if (voucherHeader.getVouchermis().getSubschemeid() != null)
			sql.append(" and vmis.subschemeid="
					+ voucherHeader.getVouchermis().getSubschemeid().getId());
		if (voucherHeader.getVouchermis().getFunctionary() != null)
			sql.append(" and vmis.functionaryid="
					+ voucherHeader.getVouchermis().getFunctionary().getId());
		if (voucherHeader.getVouchermis().getDivisionid() != null)
			sql.append(" and vmis.divisionid="
					+ voucherHeader.getVouchermis().getDivisionid().getId());
		sql.append(" and ph.bankaccountnumberid="
				+ parameters.get("bankaccount")[0]);
		sql.append(" and lower(ph.type)=lower('"
				+ parameters.get("paymentMode")[0] + "')");

		final List<AppConfigValues> appList = genericDao
				.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF",
						"APPROVEDVOUCHERSTATUS");
		final String approvedstatus = appList.get(0).getValue();
		List<String> descriptionList = new ArrayList<String>();
		descriptionList.add("New");
		descriptionList.add("Reconciled");
		List<EgwStatus> egwStatusList = commonsService
				.getStatusListByModuleAndCodeList("Instrument", descriptionList);
		String statusId = "";
		for (EgwStatus egwStatus : egwStatusList)
			statusId = statusId + egwStatus.getId() + ",";
		statusId = statusId.substring(0, statusId.length() - 1);

		Bankaccount ba = (Bankaccount) persistenceService.find(
				" from Bankaccount where id=?",
				Integer.valueOf(parameters.get("bankaccount")[0]));
		Query query = null;
		LOGGER.debug("statusId -- > " + statusId);
		List<ChequeAssignment> chequeAssignmentList = null;
		List<ChequeAssignment> cBillChequeAssignmentList = null;
		chequeList = new ArrayList<ChequeAssignment>();
		if (parameters.get("paymentMode")[0]
				.equals(FinancialConstants.MODEOFPAYMENT_CHEQUE)) {
			// DIRECT BANK PAYMENT AND ADVANCE PAYMENT
			query = getSession()
					.createSQLQuery(
							"select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,0 as detailtypeid ,0 as detailkeyid,vh.voucherDate as voucherDate  ,misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill "
									+ " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vh.name in ('Direct Bank Payment','Advance Payment') and vmis.voucherheaderid= vh.id and vh.status ="
									+ approvedstatus
									+ " "
									+ sql
									+ " "
									+ " and vh.id not in (select voucherHeaderId from egf_InstrumentVoucher iv, EGF_INSTRUMENTHEADER ih where iv.INSTRUMENTHEADERID = ih.id and misbill.paidto=ih.PAYTO and ih.ID_STATUS in ("
									+ statusId
									+ ") ) "
									+ " group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto order by misbill.paidto,vh.voucherNumber")
					.addScalar("voucherid")
					.addScalar("voucherNumber")
					.addScalar("detailtypeid")
					.addScalar("detailkeyid")
					.addScalar("voucherDate")
					.addScalar("paidTo")
					.addScalar("paidAmount")
					.addScalar("chequeDate")
					.setResultTransformer(
							Transformers.aliasToBean(ChequeAssignment.class));
			chequeAssignmentList = query.list();

			// CONTRACTOR/SUPLLIER BILL PAYMENT
			query = getSession()
					.createSQLQuery(
							"select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate ,0 as detailtypeid ,0 as detailkeyid ,misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate "
									+ " from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill ,voucherheader billvh, eg_billregister br, eg_billregistermis billmis, generalledger gl left outer join  generalledgerdetail gld "
									+ " on ( gl.id =gld.generalledgerid  ) where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vh.name='Bill Payment' and  vmis.voucherheaderid= vh.id and vh.status ="
									+ approvedstatus
									+ " "
									+ sql
									+ " "
									+ " and gl.voucherheaderid =vh.id  and gl.creditamount>0 and gl.glcodeid in ("
									+ ba.getChartofaccounts().getId()
									+ ") and br.id=billmis.billid and billmis.voucherheaderid=billvh.id and br.expendituretype!='"
									+ FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT
									+ "' and misbill.billvhid=billvh.id "
									+ " and vh.id not in (select voucherHeaderId from egf_InstrumentVoucher iv, EGF_INSTRUMENTHEADER ih where iv.INSTRUMENTHEADERID = ih.id and misbill.paidto=ih.PAYTO and ih.ID_STATUS in ("
									+ statusId
									+ ") ) "
									+ " group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto order by misbill.paidto,vh.voucherNumber")
					.addScalar("voucherid")
					.addScalar("voucherNumber")
					.addScalar("voucherDate")
					.addScalar("detailtypeid")
					.addScalar("detailkeyid")
					.addScalar("paidTo")
					.addScalar("paidAmount")
					.addScalar("chequeDate")
					.setResultTransformer(
							Transformers.aliasToBean(ChequeAssignment.class));
			chequeAssignmentList.addAll(query.list());
			// EXPENSE BILL PAYMENT
			query = getSession()
					.createSQLQuery(
							"select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate ,0 as detailtypeid ,0 as detailkeyid ,misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate "
									+ " from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill ,voucherheader billvh, eg_billregister br, eg_billregistermis billmis, generalledger gl left outer join  generalledgerdetail gld "
									+ " on ( gl.id =gld.generalledgerid  ) where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
									+ approvedstatus
									+ " "
									+ sql
									+ " "
									+ " and gl.voucherheaderid =vh.id  and gl.creditamount>0 and br.id=billmis.billid and billmis.voucherheaderid=billvh.id and br.expendituretype='"
									+ FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT
									+ "' and misbill.billvhid=billvh.id "
									+ " and vh.id not in (select voucherHeaderId from egf_InstrumentVoucher iv, EGF_INSTRUMENTHEADER ih where iv.INSTRUMENTHEADERID = ih.id and misbill.paidto=ih.PAYTO and ih.ID_STATUS in ("
									+ statusId
									+ ") ) "
									+ " group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto order by misbill.paidto,vh.voucherNumber")
					.addScalar("voucherid")
					.addScalar("voucherNumber")
					.addScalar("voucherDate")
					.addScalar("paidAmount")
					.addScalar("chequeDate")
					.addScalar("paidTo")
					.setResultTransformer(
							Transformers.aliasToBean(ChequeAssignment.class));
			cBillChequeAssignmentList = query.list();
			getGlcodeIds();
			for (ChequeAssignment ca : cBillChequeAssignmentList) {
				// check the subledger available for payable code
				query = getSession()
						.createSQLQuery(
								" select gld.detailtypeid,gld.detailkeyid,sum(gld.amount)  "
										+ " from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill ,voucherheader billvh, eg_billregister br, eg_billregistermis billmis, generalledger gl, generalledgerdetail gld "
										+ " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
										+ approvedstatus
										+ " "
										+ sql
										+ " "
										+ " and br.id=billmis.billid and billmis.voucherheaderid=billvh.id and br.expendituretype='"
										+ FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT
										+ "' and misbill.billvhid=billvh.id "
										+ " and gl.voucherheaderid= billvh.id and gl.id= gld.generalledgerid and gl.creditamount>0 and gl.glcodeid in (:glcodeIdList) and misbill.payvhid ="
										+ ca.getVoucherid()
										+ "  "
										+ " and vh.id not in (select voucherHeaderId from egf_InstrumentVoucher iv, EGF_INSTRUMENTHEADER ih where iv.INSTRUMENTHEADERID = ih.id and ih.detailtypeid= gld.detailtypeid and ih.detailkeyid=gld.detailkeyid and ih.ID_STATUS in ("
										+ statusId
										+ ") ) "
										+ " group by gld.detailtypeid,gld.detailkeyid ");
				query.setParameterList("glcodeIdList",
						(Collection) cBillGlcodeIdList);
				List<Object[]> list = query.list();

				if (list == null || list.isEmpty()) {
					// check the subledger available for debit code
					query = getSession()
							.createSQLQuery(
									"select gld.detailtypeid,gld.detailkeyid,sum(gld.amount)  "
											+ " from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill ,voucherheader billvh, eg_billregister br, eg_billregistermis billmis, generalledger gl, generalledgerdetail gld "
											+ " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
											+ approvedstatus
											+ " "
											+ sql
											+ " "
											+ " and br.id=billmis.billid and billmis.voucherheaderid=billvh.id and br.expendituretype='"
											+ FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT
											+ "' and misbill.billvhid=billvh.id "
											+ " and gl.voucherheaderid= billvh.id and gl.id= gld.generalledgerid and gl.debitamount>0 and misbill.payvhid ="
											+ ca.getVoucherid()
											+ "  "
											+ " and vh.id not in (select voucherHeaderId from egf_InstrumentVoucher iv, EGF_INSTRUMENTHEADER ih where iv.INSTRUMENTHEADERID = ih.id and ih.detailtypeid= gld.detailtypeid and ih.detailkeyid=gld.detailkeyid and ih.ID_STATUS in ("
											+ statusId
											+ ") ) "
											+ " group by gld.detailtypeid,gld.detailkeyid ");
					list = query.list();
					if (list == null || list.isEmpty()) // no subledger
					{
						chequeAssignmentList.add(ca);
					} else
						// get the subledger for deductionamt and prepare cheque
						// list
						prepareChequeList(list, ca, true);
				} else {
					prepareChequeList(list, ca, false);
				}
			}
			if (!chequeList.isEmpty())
				chequeAssignmentList.addAll(chequeList);

		} else if (voucherHeader.getName() == null
				|| !voucherHeader.getName().equalsIgnoreCase(
						FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE)) {
			query = getSession()
					.createSQLQuery(
							"select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill "
									+ " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
									+ approvedstatus
									+ " "
									+ sql
									+ " "
									+ " and vh.id not in (select voucherHeaderId from egf_InstrumentVoucher iv, EGF_INSTRUMENTHEADER ih where iv.INSTRUMENTHEADERID = ih.id and ih.ID_STATUS in ("
									+ statusId
									+ ") ) and vh.type='"
									+ FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
									+ "' and vh.name !='"
									+ FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
									+ "'"
									+ " group by vh.id,vh.voucherNumber,vh.voucherDate order by vh.voucherNumber ")
					.addScalar("voucherid")
					.addScalar("voucherNumber")
					.addScalar("voucherDate")
					.addScalar("paidAmount")
					.addScalar("chequeDate")
					.setResultTransformer(
							Transformers.aliasToBean(ChequeAssignment.class));
			chequeAssignmentList = query.list();
		} else {

			query = getSession()
					.createSQLQuery(
							"select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill,Eg_remittance  rem "
									+ " where ph.voucherheaderid=misbill.payvhid and  rem.paymentvhid=vh.id and rem.tdsid="
									+ parameters.get("recoveryId")[0]
									+ "  and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
									+ approvedstatus
									+ " "
									+ sql
									+ " "
									+ " and vh.id not in (select voucherHeaderId from egf_InstrumentVoucher iv, EGF_INSTRUMENTHEADER ih where iv.INSTRUMENTHEADERID = ih.id and ih.ID_STATUS in ("
									+ statusId
									+ ") ) and vh.type='"
									+ FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
									+ "' and vh.name ='"
									+ FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
									+ "'"
									+ " group by vh.id,vh.voucherNumber,vh.voucherDate order by vh.voucherNumber ")
					.addScalar("voucherid")
					.addScalar("voucherNumber")
					.addScalar("voucherDate")
					.addScalar("paidAmount")
					.addScalar("chequeDate")
					.setResultTransformer(
							Transformers.aliasToBean(ChequeAssignment.class));
			chequeAssignmentList = query.list();
		}

		return chequeAssignmentList;
	}

	/**
	 * This method will check for the deduction amount only in the case where
	 * the Journal Voucher will have net payable as a non-control code. In case
	 * if the net payable is a control code, the cheque amount will be same as
	 * the net amaount.
	 * 
	 * @param list
	 * @param ca
	 * @param checkDed
	 * @throws EGOVException
	 */
	private void prepareChequeList(List<Object[]> list, ChequeAssignment ca,
			boolean checkDed) throws EGOVException {
		Map<String, BigDecimal> dedMap = new HashMap<String, BigDecimal>();
		if (checkDed)
			dedMap = getSubledgerAmtForDeduction(ca.getVoucherid());
		String key = "";
		if (list != null && !list.isEmpty())
			for (Object[] ob : list) {
				ChequeAssignment c = new ChequeAssignment();
				c.setChequeDate(ca.getChequeDate());
				c.setVoucherHeaderId(ca.getVoucherid());
				c.setVoucherNumber(ca.getVoucherNumber());
				c.setVoucherDate(ca.getVoucherDate());
				c.setDetailtypeid((BigDecimal) ob[0]);
				c.setDetailkeyid((BigDecimal) ob[1]);
				key = ob[0].toString() + DELIMETER + ob[1].toString();
				if (checkDed)
					c.setPaidAmount((dedMap.get(key) == null ? (BigDecimal) ob[2]
							: ((BigDecimal) ob[2]).subtract(dedMap.get(key))));
				else
					c.setPaidAmount((BigDecimal) ob[2]);

				if (list.size() == 1)
					c.setPaidTo(ca.getPaidTo());
				else
					c.setPaidTo(getEntity(Integer.valueOf(ob[0].toString()),
							(Serializable) ob[1]).getName());
				chequeList.add(c);
			}
	}

	private Map getSubledgerAmtForDeduction(Long payVhId) {
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		Query query = getSession()
				.createSQLQuery(
						" select gld.detailtypeid,gld.detailkeyid,sum(gld.amount) from generalledgerdetail gld,generalledger gl, voucherheader vh, miscbilldetail misbill "
								+ " where vh.id= gl.voucherheaderid and gl.id=gld.generalledgerid and gl.creditamount>0 and gl.glcodeid NOT in (:glcodeIdList) and misbill.billvhid=vh.id "
								+ " and misbill.payvhid ="
								+ payVhId
								+ " group by gld.detailtypeid,gld.detailkeyid ");
		query.setParameterList("glcodeIdList", (Collection) cBillGlcodeIdList);
		List<Object[]> list = query.list();
		if (list != null && !list.isEmpty()) {
			for (Object[] ob : list)
				map.put(ob[0].toString() + DELIMETER + ob[1].toString(),
						(BigDecimal) ob[2]);
		}
		return map;
	}

	public List<InstrumentHeader> createInstrument(
			List<ChequeAssignment> chequeAssignmentList, String paymentMode,
			Integer bankaccount, Map<String, String[]> parameters,
			DepartmentImpl dept) throws EGOVRuntimeException, Exception {
		List<InstrumentHeader> instHeaderList = new ArrayList<InstrumentHeader>();
		List<Long> selectedPaymentVHList = new ArrayList<Long>();
		Map<String, BigDecimal> payeeMap = new HashMap<String, BigDecimal>();
		BigDecimal totalPaidAmt = BigDecimal.ZERO;

		for (ChequeAssignment assignment : chequeAssignmentList) {
			if (assignment.getIsSelected()) {
				selectedPaymentVHList.add(assignment.getVoucherid());
				if (payeeMap.containsKey(assignment.getPaidTo() + DELIMETER
						+ assignment.getDetailtypeid() + DELIMETER
						+ assignment.getDetailkeyid())) // concatenate the
														// amount, if the
														// party's are same
					payeeMap.put(
							assignment.getPaidTo() + DELIMETER
									+ assignment.getDetailtypeid() + DELIMETER
									+ assignment.getDetailkeyid(),
							payeeMap.get(
									assignment.getPaidTo() + DELIMETER
											+ assignment.getDetailtypeid()
											+ DELIMETER
											+ assignment.getDetailkeyid()).add(
									assignment.getPaidAmount()));
				else
					payeeMap.put(assignment.getPaidTo() + DELIMETER
							+ assignment.getDetailtypeid() + DELIMETER
							+ assignment.getDetailkeyid(),
							assignment.getPaidAmount());
				totalPaidAmt = totalPaidAmt.add(assignment.getPaidAmount());
			}
		}
		LOGGER.debug("selectedPaymentList===" + selectedPaymentVHList);
		Bankaccount account = (Bankaccount) persistenceService.find(
				" from Bankaccount where  id=?", bankaccount);
		// get voucherList
		List<CVoucherHeader> voucherList = persistenceService
				.findAllByNamedQuery("getVoucherList", selectedPaymentVHList);

		List<Map<String, Object>> instrumentHeaderList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> instrumentVoucherList = new ArrayList<Map<String, Object>>();

		if (paymentMode.equals(FinancialConstants.MODEOFPAYMENT_CHEQUE)) {
			Map<Long, CVoucherHeader> paymentVoucherMap = new HashMap<Long, CVoucherHeader>();
			for (CVoucherHeader voucherHeader : voucherList) {
				paymentVoucherMap.put(voucherHeader.getId(), voucherHeader);
			}

			if (isChequeNoGenerationAuto()) // if cheque number generation is
											// auto
			{
				// get chequeNumber
				String chequeNo = chequeService.nextChequeNumber(account
						.getId().toString(), payeeMap.size(), dept.getId());
				String[] chequeNoArray = StringUtils.split(chequeNo, ",");

				// create instrument header
				Map<String, String> chequeNoMap = new HashMap<String, String>();
				Iterator iterator = payeeMap.keySet().iterator();
				String key; // paidTo+delimeter+detailtypeid+delimeter+detailkeyid
				int i = 0;
				while (iterator.hasNext()) {
					key = iterator.next().toString();
					instrumentHeaderList.add(prepareInstrumentHeader(account,
							chequeNoArray[i],
							FinancialConstants.MODEOFPAYMENT_CHEQUE
									.toLowerCase(), key.split(DELIMETER)[0],
							payeeMap.get(key), currentDate, key));
					chequeNoMap.put(key, chequeNoArray[i]);
					i++;
				}
				instHeaderList = instrumentService
						.addToInstrument(instrumentHeaderList);

				// create instrument voucher
				for (ChequeAssignment assignment : chequeAssignmentList) {
					if (assignment.getIsSelected()) {
						instrumentVoucherList
								.add(preapreInstrumentVoucher(paymentVoucherMap
										.get(assignment.getVoucherid()),
										account, chequeNoMap.get(assignment
												.getPaidTo()
												+ DELIMETER
												+ assignment.getDetailtypeid()
												+ DELIMETER
												+ assignment.getDetailkeyid()),
										assignment.getPaidTo()));
					}
				}
				instVoucherList = instrumentService
						.updateInstrumentVoucherReference(instrumentVoucherList);
			} else // for manual cheque number
			{
				String text = "";
				Map<String, BigDecimal> partyChequeNoMap = new HashMap<String, BigDecimal>();
				for (ChequeAssignment assignment : chequeAssignmentList) {
					if (assignment.getIsSelected()) {
						text = assignment.getPaidTo() + DELIMETER
								+ assignment.getDetailtypeid() + DELIMETER
								+ assignment.getDetailkeyid() + DELIMETER
								+ assignment.getChequeNumber() + DELIMETER
								+ formatter.format(assignment.getChequeDate());
						if (partyChequeNoMap.containsKey(text))
							partyChequeNoMap.put(
									text,
									partyChequeNoMap.get(text).add(
											assignment.getPaidAmount()));
						else
							partyChequeNoMap.put(text,
									assignment.getPaidAmount());
					}
				}
				Iterator iterator = partyChequeNoMap.keySet().iterator();
				String key;
				while (iterator.hasNext()) // create instrument header
				{
					key = iterator.next().toString();

					instrumentHeaderList.add(prepareInstrumentHeader(account,
							key.split(DELIMETER)[3],
							FinancialConstants.MODEOFPAYMENT_CHEQUE
									.toLowerCase(), key.split(DELIMETER)[0],
							partyChequeNoMap.get(key), formatter.parse(key
									.split(DELIMETER)[4]), key));
				}
				instHeaderList = instrumentService
						.addToInstrument(instrumentHeaderList);
				// create instrument voucher
				for (ChequeAssignment assignment : chequeAssignmentList) {
					if (assignment.getIsSelected()) {
						instrumentVoucherList
								.add(preapreInstrumentVoucher(paymentVoucherMap
										.get(assignment.getVoucherid()),
										account, assignment.getChequeNumber(),
										assignment.getPaidTo()));
					}
				}
				instVoucherList = instrumentService
						.updateInstrumentVoucherReference(instrumentVoucherList);
			} // end of manual cheque number
		} else // if it's cash or RTGS
		{
			if (isChequeNoGenerationAuto()) // if cheque number generation is
											// auto
			{
				String chequeNo = chequeService.nextChequeNumber(account
						.getId().toString(), 1, dept.getId());
				String[] chequeNoArray = StringUtils.split(chequeNo, ",");
				instrumentHeaderList.add(prepareInstrumentHeader(account,
						chequeNoArray[0],
						FinancialConstants.MODEOFPAYMENT_CHEQUE.toLowerCase(),
						parameters.get("inFavourOf")[0], totalPaidAmt,
						currentDate, ""));
			} else {
				instrumentHeaderList.add(prepareInstrumentHeader(account,
						parameters.get("chequeNo")[0],
						FinancialConstants.MODEOFPAYMENT_CHEQUE.toLowerCase(),
						parameters.get("inFavourOf")[0], totalPaidAmt,
						formatter.parse(parameters.get("chequeDt")[0]), ""));
			}
			instHeaderList = instrumentService
					.addToInstrument(instrumentHeaderList);

			List<Paymentheader> paymentList = persistenceService
					.findAllByNamedQuery("getPaymentList",
							selectedPaymentVHList);
			Map<String, Object> instrumentVoucherMap = null;
			for (Paymentheader paymentheader : paymentList) {
				instrumentVoucherMap = new HashMap<String, Object>();
				instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER,
						paymentheader.getVoucherheader());
				instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER,
						instHeaderList.get(0));
				instrumentVoucherList.add(instrumentVoucherMap);
			}
			instVoucherList = instrumentService
					.updateInstrumentVoucherReference(instrumentVoucherList);
		}
		return instHeaderList;
	}

	public List<InstrumentHeader> reassignInstrument(
			List<ChequeAssignment> chequeAssignmentList, String paymentMode,
			Integer bankaccount, Map<String, String[]> parameters,
			DepartmentImpl dept) throws EGOVRuntimeException, Exception {
		List<InstrumentHeader> instHeaderList = new ArrayList<InstrumentHeader>();
		List<Long> selectedPaymentVHList = new ArrayList<Long>();
		Map<String, BigDecimal> payeeMap = new HashMap<String, BigDecimal>();
		BigDecimal totalPaidAmt = BigDecimal.ZERO;

		for (ChequeAssignment assignment : chequeAssignmentList) {
			if (assignment.getIsSelected()) {
				selectedPaymentVHList.add(assignment.getVoucherid());
				if (payeeMap.containsKey(assignment.getPaidTo() + DELIMETER
						+ assignment.getDetailtypeid() + DELIMETER
						+ assignment.getDetailkeyid())) // concatenate the
														// amount, if the
														// party's are same
					payeeMap.put(
							assignment.getPaidTo() + DELIMETER
									+ assignment.getDetailtypeid() + DELIMETER
									+ assignment.getDetailkeyid(),
							payeeMap.get(
									assignment.getPaidTo() + DELIMETER
											+ assignment.getDetailtypeid()
											+ DELIMETER
											+ assignment.getDetailkeyid()).add(
									assignment.getPaidAmount()));
				else
					payeeMap.put(assignment.getPaidTo() + DELIMETER
							+ assignment.getDetailtypeid() + DELIMETER
							+ assignment.getDetailkeyid(),
							assignment.getPaidAmount());
				totalPaidAmt = totalPaidAmt.add(assignment.getPaidAmount());
			}
		}
		LOGGER.debug("selectedPaymentList===" + selectedPaymentVHList);
		Bankaccount account = (Bankaccount) persistenceService.find(
				" from Bankaccount where  id=?", bankaccount);
		// get voucherList
		List<CVoucherHeader> voucherList = persistenceService
				.findAllByNamedQuery("getVoucherList", selectedPaymentVHList);

		List<Map<String, Object>> instrumentHeaderList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> instrumentVoucherList = new ArrayList<Map<String, Object>>();

		if (paymentMode.equals(FinancialConstants.MODEOFPAYMENT_CHEQUE)) {
			Map<Long, CVoucherHeader> paymentVoucherMap = new HashMap<Long, CVoucherHeader>();
			for (CVoucherHeader voucherHeader : voucherList) {
				paymentVoucherMap.put(voucherHeader.getId(), voucherHeader);
			}

			String text = "";
			Map<String, BigDecimal> partyChequeNoMap = new HashMap<String, BigDecimal>();
			for (ChequeAssignment assignment : chequeAssignmentList) {
				if (assignment.getIsSelected()) {
					text = assignment.getPaidTo() + DELIMETER
							+ assignment.getDetailtypeid() + DELIMETER
							+ assignment.getDetailkeyid() + DELIMETER
							+ assignment.getChequeNumber() + DELIMETER
							+ formatter.format(assignment.getChequeDate());
					if (partyChequeNoMap.containsKey(text))
						partyChequeNoMap.put(text, partyChequeNoMap.get(text)
								.add(assignment.getPaidAmount()));
					else
						partyChequeNoMap.put(text, assignment.getPaidAmount());
				}
			}
			instHeaderList = new ArrayList<InstrumentHeader>();
			Iterator iterator = partyChequeNoMap.keySet().iterator();
			String key;

			while (iterator.hasNext()) // create instrument header
			{
				key = iterator.next().toString();
				instHeaderList.add(reassignInstrumentHeader(account,
						key.split(DELIMETER)[3],
						FinancialConstants.MODEOFPAYMENT_CHEQUE.toLowerCase(),
						key.split(DELIMETER)[0], partyChequeNoMap.get(key),
						formatter.parse(key.split(DELIMETER)[4]), key));
			}
			// instHeaderList =
			// instrumentService.addToInstrument(instrumentHeaderList);
			// create instrument voucher
			for (ChequeAssignment assignment : chequeAssignmentList) {
				if (assignment.getIsSelected()) {
					instrumentVoucherList.add(reassignInstrumentVoucher(
							paymentVoucherMap.get(assignment.getVoucherid()),
							account, assignment.getChequeNumber(),
							assignment.getPaidTo()));
				}
			}
			instVoucherList = instrumentService
					.updateInstrumentVoucherReference(instrumentVoucherList);
			// end of manual cheque number
		} else // if it's cash or RTGS
		{

			instHeaderList.add(reassignInstrumentHeader(account,
					parameters.get("chequeNo")[0],
					FinancialConstants.MODEOFPAYMENT_CHEQUE.toLowerCase(),
					parameters.get("inFavourOf")[0], totalPaidAmt,
					formatter.parse(parameters.get("chequeDt")[0]), ""));

			// instHeaderList =
			// instrumentService.addToInstrument(instrumentHeaderList);

			List<Paymentheader> paymentList = persistenceService
					.findAllByNamedQuery("getPaymentList",
							selectedPaymentVHList);
			Map<String, Object> instrumentVoucherMap = null;
			for (Paymentheader paymentheader : paymentList) {
				instrumentVoucherMap = new HashMap<String, Object>();
				instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER,
						paymentheader.getVoucherheader());
				instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER,
						instHeaderList.get(0));
				instrumentVoucherList.add(instrumentVoucherMap);
			}
			instVoucherList = instrumentService
					.updateInstrumentVoucherReference(instrumentVoucherList);
		}
		return instHeaderList;
	}

	protected Map<String, Object> prepareInstrumentHeader(
			final Bankaccount account, final String chqNo,
			final String instType, final String partyName,
			final BigDecimal amount, final Date date, final String key) {
		Map<String, Object> instrumentHeaderMap = new HashMap<String, Object>();
		instrumentHeaderMap.put(VoucherConstant.IS_PAYCHECK, "1");
		instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_TYPE, instType);
		instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_AMOUNT, amount);
		instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_NUMBER, chqNo);
		instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_DATE, date);
		instrumentHeaderMap.put(VoucherConstant.BANK_CODE, account
				.getBankbranch().getBank().getCode());
		instrumentHeaderMap.put(VoucherConstant.PAY_TO, partyName);
		instrumentHeaderMap.put(VoucherConstant.BANKACCOUNTID, account.getId());
		if (!key.equals("")) {
			if (key.split(DELIMETER)[1] != null
					&& !key.split(DELIMETER)[1].equals("")
					&& !key.split(DELIMETER)[1].equals("null"))
				instrumentHeaderMap.put(VoucherConstant.DETAIL_TYPE_ID,
						Integer.valueOf(key.split(DELIMETER)[1]));
			if (key.split(DELIMETER)[2] != null
					&& !key.split(DELIMETER)[2].equals("")
					&& !key.split(DELIMETER)[2].equals("null"))
				instrumentHeaderMap.put(VoucherConstant.DETAIL_KEY_ID,
						Long.valueOf(key.split(DELIMETER)[2]));
		}
		return instrumentHeaderMap;
	}

	protected InstrumentHeader reassignInstrumentHeader(
			final Bankaccount account, final String chqNo,
			final String instType, final String partyName,
			final BigDecimal amount, final Date date, final String key) {
		InstrumentHeader ih = (InstrumentHeader) persistenceService.find(
				"from InstrumentHeader where instrumentNumber=?", chqNo);
		ih.setIsPayCheque("1");
		// ih.setInstrumentType(instType);
		ih.setInstrumentAmount(amount);
		// instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_NUMBER, chqNo);
		ih.setInstrumentDate(date);
		// ih.setBank(account.getBankbranch().getBank().getCode());
		ih.setPayTo(partyName);
		ih.setBankAccountId(account);
		ih.setSurrendarReason(null);
		ih.setStatusId(instrumentService
				.getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS));
		if (!key.equals("")) {
			if (key.split(DELIMETER)[1] != null
					&& !key.split(DELIMETER)[1].equals("")
					&& !key.split(DELIMETER)[1].equals("null"))
				ih.setDetailTypeId((Accountdetailtype) persistenceService.find(
						"from Accountdetailtype where id=?",
						Integer.valueOf(key.split(DELIMETER)[1])));
			if (key.split(DELIMETER)[2] != null
					&& !key.split(DELIMETER)[2].equals("")
					&& !key.split(DELIMETER)[2].equals("null"))
				ih.setDetailKeyId(Long.valueOf(key.split(DELIMETER)[2]));
		}
		return instrumentService.instrumentHeaderService.persist(ih);
	}

	protected Map<String, Object> preapreInstrumentVoucher(
			final CVoucherHeader voucherHeader, final Bankaccount account,
			final String chqNo, final String paidTo) {
		Map<String, Object> instrumentVoucherMap = new HashMap<String, Object>();
		instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER, voucherHeader);
		// get the InstrumnetHeader for the party & chequeno & bankaccountid
		InstrumentHeader instrumentHeader = instrumentService
				.getInstrumentHeader(account.getId(), chqNo, paidTo);
		instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER,
				instrumentHeader);
		return instrumentVoucherMap;
	}

	protected Map<String, Object> reassignInstrumentVoucher(
			final CVoucherHeader voucherHeader, final Bankaccount account,
			final String chqNo, final String paidTo) {

		InstrumentHeader instrumentHeader = instrumentService
				.getInstrumentHeader(account.getId(), chqNo, paidTo);

		List<InstrumentVoucher> findAllBy = (List<InstrumentVoucher>) persistenceService
				.findAllBy("from InstrumentVoucher where instrumentHeaderId=?",
						instrumentHeader);
		for (InstrumentVoucher iv : findAllBy) {
			persistenceService.delete(iv);
		}
		HibernateUtil.getCurrentSession().refresh(instrumentHeader);
		HibernateUtil.getCurrentSession().flush();
		// instrumentHeader=
		// instrumentService.instrumentHeaderService.persist(instrumentHeader);
		Map<String, Object> instrumentVoucherMap = new HashMap<String, Object>();
		instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER, voucherHeader);
		// get the InstrumnetHeader for the party & chequeno & bankaccountid

		instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER,
				instrumentHeader);
		return instrumentVoucherMap;
	}

	/**
	 * returns contingent details having netAmt, PayableAmt, PaymentAmt.
	 * 
	 * @param billList
	 * @param deductionAmtMap
	 * @param paidAmtMap
	 * @return
	 */
	public List<PaymentBean> getCSList(List<EgBillregister> billList,
			Map<Long, BigDecimal> deductionAmtMap,
			Map<Long, BigDecimal> paidAmtMap) {
		List<PaymentBean> contractorList = new ArrayList<PaymentBean>();
		PaymentBean paymentBean = null;
		if (billList != null && !billList.isEmpty()) {
			for (EgBillregister billregister : billList) {
				paymentBean = new PaymentBean();
				paymentBean.setCsBillId(billregister.getId());
				paymentBean.setBillNumber(billregister.getBillnumber());
				paymentBean.setBillDate(billregister.getBilldate());
				paymentBean.setExpType(billregister.getExpendituretype());
				if (billregister.getExpendituretype().equals(
						FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT))
					paymentBean.setPayTo(getPayeeNameForCBill(billregister));
				else
					paymentBean.setPayTo(billregister.getEgBillregistermis()
							.getPayto());
				paymentBean.setDeductionAmt(deductionAmtMap.get(paymentBean
						.getCsBillId()) == null ? BigDecimal.ZERO
						: deductionAmtMap.get(paymentBean.getCsBillId()));
				BigDecimal passedamount = billregister.getPassedamount() == null ? BigDecimal.ZERO
						: billregister.getPassedamount();
				paymentBean.setNetAmt(passedamount.subtract(paymentBean
						.getDeductionAmt() == null ? BigDecimal.ZERO
						: paymentBean.getDeductionAmt()));
				paymentBean.setEarlierPaymentAmt((paidAmtMap.get(paymentBean
						.getCsBillId()) == null ? BigDecimal.ZERO : paidAmtMap
						.get(paymentBean.getCsBillId())));
				paymentBean.setPayableAmt(paymentBean.getNetAmt().subtract(
						paymentBean.getEarlierPaymentAmt()));
				paymentBean.setPaymentAmt(paymentBean.getPayableAmt());
				if (paymentBean.getPaymentAmt().compareTo(BigDecimal.ZERO) == 0)
					continue;
				if (billregister.getEgBillregistermis().getFund() != null)
					paymentBean.setFundName(billregister.getEgBillregistermis()
							.getFund().getName());
				if (billregister.getEgBillregistermis().getEgDepartment() != null)
					paymentBean.setDeptName(billregister.getEgBillregistermis()
							.getEgDepartment().getDeptName());
				if (billregister.getEgBillregistermis().getScheme() != null)
					paymentBean.setSchemeName(billregister
							.getEgBillregistermis().getScheme().getName());
				if (billregister.getEgBillregistermis().getSubScheme() != null)
					paymentBean.setSubschemeName(billregister
							.getEgBillregistermis().getSubScheme().getName());
				if (billregister.getEgBillregistermis().getFunctionaryid() != null)
					paymentBean.setFunctionaryName(billregister
							.getEgBillregistermis().getFunctionaryid()
							.getName());
				if (billregister.getEgBillregistermis().getFundsource() != null)
					paymentBean.setFundsourceName(billregister
							.getEgBillregistermis().getFundsource().getName());
				if (billregister.getEgBillregistermis().getFieldid() != null)
					paymentBean.setFieldName(billregister
							.getEgBillregistermis().getFieldid().getName());
				if (billregister.getEgBillregistermis().getVoucherHeader() != null) {
					paymentBean.setBillVoucherNumber(billregister
							.getEgBillregistermis().getVoucherHeader()
							.getVoucherNumber());
					paymentBean.setBillVoucherDate(billregister
							.getEgBillregistermis().getVoucherHeader()
							.getVoucherDate());
				}

				contractorList.add(paymentBean);
			}
		}
		return contractorList;
	}

	public List<PaymentBean> getCSListForModify(List<EgBillregister> billList,
			Map<Long, BigDecimal> deductionAmtMap,
			Map<Long, BigDecimal> paidAmtMap, List<PaymentBean> miscBillList) {
		List<PaymentBean> contractorList = new ArrayList<PaymentBean>();
		PaymentBean paymentBean = null;
		if (billList != null && !billList.isEmpty()) {
			for (EgBillregister billregister : billList) {
				paymentBean = new PaymentBean();
				paymentBean.setCsBillId(billregister.getEgBillregistermis()
						.getVoucherHeader().getId());
				paymentBean.setBillNumber(billregister.getBillnumber());
				paymentBean.setBillDate(billregister.getBilldate());
				paymentBean.setExpType(billregister.getExpendituretype());
				if (billregister.getExpendituretype().equals(
						FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT))
					paymentBean.setPayTo(getPayeeNameForCBill(billregister));
				else
					paymentBean.setPayTo(billregister.getEgBillregistermis()
							.getPayto());
				paymentBean.setDeductionAmt(deductionAmtMap.get(billregister
						.getId()) == null ? BigDecimal.ZERO : deductionAmtMap
						.get(billregister.getId()));
				paymentBean
						.setPassedAmt(billregister.getPassedamount() == null ? BigDecimal.ZERO
								: billregister.getPassedamount());
				paymentBean.setNetAmt(paymentBean.getPassedAmt().subtract(
						paymentBean.getDeductionAmt() == null ? BigDecimal.ZERO
								: paymentBean.getDeductionAmt()));
				paymentBean.setEarlierPaymentAmt((paidAmtMap.get(billregister
						.getId()) == null ? BigDecimal.ZERO : paidAmtMap
						.get(billregister.getId())));
				paymentBean.setPayableAmt(paymentBean.getNetAmt().subtract(
						paymentBean.getEarlierPaymentAmt()));
				for (PaymentBean miscBillDetails : miscBillList) {
					if (paymentBean.getBillNumber().equals(
							miscBillDetails.getBillNumber())) {
						paymentBean.setPaymentAmt(miscBillDetails
								.getPaymentAmt());
					}
				}
				if (paymentBean.getPaymentAmt().compareTo(BigDecimal.ZERO) == 0)
					continue;
				if (billregister.getEgBillregistermis().getFund() != null)
					paymentBean.setFundName(billregister.getEgBillregistermis()
							.getFund().getName());
				if (billregister.getEgBillregistermis().getEgDepartment() != null)
					paymentBean.setDeptName(billregister.getEgBillregistermis()
							.getEgDepartment().getDeptName());
				if (billregister.getEgBillregistermis().getScheme() != null)
					paymentBean.setSchemeName(billregister
							.getEgBillregistermis().getScheme().getName());
				if (billregister.getEgBillregistermis().getSubScheme() != null)
					paymentBean.setSubschemeName(billregister
							.getEgBillregistermis().getSubScheme().getName());
				if (billregister.getEgBillregistermis().getFunctionaryid() != null)
					paymentBean.setFunctionaryName(billregister
							.getEgBillregistermis().getFunctionaryid()
							.getName());
				if (billregister.getEgBillregistermis().getFundsource() != null)
					paymentBean.setFundsourceName(billregister
							.getEgBillregistermis().getFundsource().getName());
				if (billregister.getEgBillregistermis().getFieldid() != null)
					paymentBean.setFieldName(billregister
							.getEgBillregistermis().getFieldid().getName());
				if (billregister.getEgBillregistermis().getVoucherHeader() != null) {
					paymentBean.setBillVoucherNumber(billregister
							.getEgBillregistermis().getVoucherHeader()
							.getVoucherNumber());
					paymentBean.setBillVoucherDate(billregister
							.getEgBillregistermis().getVoucherHeader()
							.getVoucherDate());
				}

				contractorList.add(paymentBean);
			}
		}
		return contractorList;
	}

	public String getPayeeNameForCBill(EgBillregister bill) {
		List<Object[]> obj = null;
		String payeeName = "";
		// check the payeedetails for payable code
		obj = persistenceService.findAllByNamedQuery(
				"getPayeeDetailsForPayableCode", bill.getId(),
				cBillGlcodeIdList);
		if (obj == null || obj.size() == 0) {
			// check the payeedetails for debit code
			obj = (List<Object[]>) persistenceService.findAllByNamedQuery(
					"getPayeeDetailsForDebitCode", bill.getId());
			if (obj == null || obj.size() == 0)
				payeeName = bill.getEgBillregistermis().getPayto();
			else {
				if (obj.size() > 1)
					payeeName = FinancialConstants.MULTIPLE;
				else
					payeeName = bill.getEgBillregistermis().getPayto();
			}
		} else {
			if (obj.size() > 1)
				payeeName = FinancialConstants.MULTIPLE;
			else
				payeeName = bill.getEgBillregistermis().getPayto();
		}
		return payeeName;
	}

	public Paymentheader createPaymentHeader(CVoucherHeader voucherHeader,
			Integer bankaccountId, String type, BigDecimal amount) {
		Paymentheader paymentheader = new Paymentheader();
		paymentheader.setType(type);
		paymentheader.setVoucherheader(voucherHeader);
		Bankaccount bankaccount = (Bankaccount) persistenceService.find(
				"from Bankaccount where id=?", bankaccountId);
		paymentheader.setBankaccount(bankaccount);
		paymentheader.setPaymentAmount(amount);
		persistenceService.setType(Paymentheader.class);
		persistenceService.create(paymentheader);
		return paymentheader;
	}

	public Paymentheader updatePaymentHeader(Paymentheader paymentheader,
			CVoucherHeader voucherHeader, Integer bankaccountId, String type,
			BigDecimal amount) {
		paymentheader.setType(type);
		paymentheader.setVoucherheader(voucherHeader);
		Bankaccount bankaccount = (Bankaccount) persistenceService.find(
				"from Bankaccount where id=?", bankaccountId);
		paymentheader.setBankaccount(bankaccount);
		paymentheader.setPaymentAmount(amount);
		persistenceService.setType(Paymentheader.class);
		persistenceService.persist(paymentheader);
		return paymentheader;
	}

	@SkipValidation
	public List getPayeeDetailsForExpenseBill(Map<String, String[]> parameters,
			CVoucherHeader voucherHeader) {

		return new ArrayList(); //

	}

	public Position getSuperiourPositionByPosition() {
		return eisCommonsService.getSuperiorPositionByObjType(getPosition(),
				"Payment");
	}

	public Position getPosition() throws EGOVRuntimeException {
		return eisCommonsService.getPositionByUserId(Integer
				.valueOf(EGOVThreadLocals.getUserId()));
	}

	public String getFunctionaryAndDesignation() {
		Assignment assignment = getAssignment();
		return assignment.getFunctionary().getName() + "-"
				+ assignment.getDesigId().getDesignationName();
	}

	public Assignment getAssignment() {
		PersonalInformation pi = employeeService.getEmpForUserId(Integer
				.valueOf(EGOVThreadLocals.getUserId()));
		return employeeService.getAssignmentByEmpAndDate(new Date(),
				pi.getIdPersonalInformation());
	}

	public Position getPositionForEmployee(PersonalInformation emp)
			throws EGOVRuntimeException {
		return employeeService
				.getPositionforEmp(emp.getIdPersonalInformation());
	}

	public PersonalInformation getEmpForCurrentUser() {
		return employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals
				.getUserId()));
	}

	public String getEmployeeNameForPositionId(Position pos)
			throws EGOVRuntimeException {
		PersonalInformation pi = employeeService.getEmployeeforPosition(pos);
		Assignment assignment = employeeService.getAssignmentByEmpAndDate(
				new Date(), pi.getIdPersonalInformation());
		return pi.getEmployeeFirstName() + " ("
				+ assignment.getFunctionary().getName() + "-"
				+ assignment.getDesigId().getDesignationName() + ")";
	}

	public void finalApproval(Long voucherid) {
		billsAccountingService.createVoucherfromPreApprovedVoucher(voucherid);
	}

	
	public void setInstrumentService(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}

	public void setChequeService(ChequeService chequeService) {
		this.chequeService = chequeService;
	}

	public List<InstrumentVoucher> getInstVoucherList() {
		return instVoucherList;
	}

	public void setInstVoucherList(List<InstrumentVoucher> instVoucherList) {
		this.instVoucherList = instVoucherList;
	}

	public FundFlowService getFundFlowService() {
		return fundFlowService;
	}

	public void setFundFlowService(FundFlowService fundFlowService) {
		this.fundFlowService = fundFlowService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setBillsAccountingService(
			BillsAccountingService billsAccountingService) {
		this.billsAccountingService = billsAccountingService;
	}

}
