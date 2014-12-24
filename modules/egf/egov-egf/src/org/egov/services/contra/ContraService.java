package org.egov.services.contra;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankreconciliation;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.VoucherDetail;
import org.egov.commons.service.CommonsService;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.user.User;
import org.egov.model.contra.ContraBean;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.voucher.PayInBean;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.services.instrument.InstrumentService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;

import com.exilant.GLEngine.Transaxtion;

/**
 * 
 * @author msahoo
 * 
 */

public class ContraService extends
		PersistenceService<ContraJournalVoucher, Long> {
	private static final Logger LOGGER = Logger.getLogger(ContraService.class);
	private CommonsService commonsService;
	private EisCommonsService eisCommonsService;
	private EmployeeService employeeService;
	private PersistenceService persistenceService;
	private PersistenceService<ContraJournalVoucher, Long> contrajournalService;
	private PersistenceService<Bankreconciliation, Integer> bankReconService;
	private PersistenceService<VoucherDetail, Long> vdPersitSer;
	private InstrumentService instrumentService;
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yyyy", Locale.ENGLISH);
	private GenericHibernateDaoFactory genericDao;
	private EisUtilService eisService;
	private int preapprovalStatus = 0;

	public ContraService() throws Exception {

	}

	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public Position getPositionForWfItem(ContraJournalVoucher rv) {
		return eisCommonsService.getPositionByUserId(rv.getCreatedBy().getId());
	}

	public Department getDepartmentForUser(User user) {
		return new EgovCommon().getDepartmentForUser(user, employeeService,
				eisService, persistenceService);
	}

	public ContraJournalVoucher updateIntoContraJournal(
			CVoucherHeader voucherHeader, ContraBean contraBean) {
		ContraJournalVoucher existingCJV;
		try {
			existingCJV = contrajournalService.find(
					"from ContraJournalVoucher where voucherHeaderId=?",
					voucherHeader);
			existingCJV.setToBankAccountId(commonsService
					.getBankaccountById(Integer.valueOf(contraBean
							.getAccountNumberId())));
			contrajournalService.update(existingCJV);
		} catch (HibernateException e) {
			LOGGER.debug("Exception occuerd while postiong into contractorJournal");
			throw new HibernateException(e);
		} catch (Exception e) {
			LOGGER.debug("Exception occuerd while postiong into contractorJournal");
			throw new HibernateException(e);

		}
		return existingCJV;
	}

	public Bankreconciliation updateBankreconciliation(
			InstrumentHeader instrHeader, ContraBean contraBean) {
		Bankreconciliation existingBR;
		try {
			Long iHeaderId = instrHeader.getId();
			LOGGER.debug("instrHeader.getId() = " + iHeaderId);
			existingBR = bankReconService.find(
					"from Bankreconciliation where instrumentHeaderId=?",
					iHeaderId);
			existingBR.setAmount(contraBean.getAmount());
			existingBR.setBankaccount(commonsService.getBankaccountById(Integer
					.valueOf(contraBean.getAccountNumberId())));
			bankReconService.update(existingBR);
		} catch (HibernateException e) {
			LOGGER.debug("Exception occuerd while updateBankreconciliation" + e);
			throw new HibernateException(e);
		} catch (Exception e) {
			LOGGER.debug("Exception occuerd while updateBankreconciliation" + e);
			throw new HibernateException(e);

		}

		return existingBR;
	}

	public List<Transaxtion> postInTransaction(CVoucherHeader voucherHeader,
			ContraBean contraBean) {

		List<Transaxtion> transaxtionList = new ArrayList<Transaxtion>();
		VoucherDetail vDetailBank = new VoucherDetail();
		Transaxtion transactionBank = new Transaxtion();

		vDetailBank.setLineId(1);
		vDetailBank.setVoucherHeaderId(voucherHeader);
		CChartOfAccounts bankAccountCode = commonsService.getBankaccountById(
				Integer.valueOf(contraBean.getAccountNumberId()))
				.getChartofaccounts();
		vDetailBank.setGlCode(bankAccountCode.getGlcode());
		vDetailBank.setAccountName(bankAccountCode.getName());
		vDetailBank.setDebitAmount(contraBean.getAmount());
		vDetailBank.setCreditAmount(BigDecimal.ZERO);
		vDetailBank.setNarration(voucherHeader.getVouchermis().getNarration());

		transactionBank.setVoucherLineId("1");
		transactionBank.setGlCode(bankAccountCode.getGlcode());
		transactionBank.setGlName(bankAccountCode.getName());
		transactionBank.setVoucherHeaderId(voucherHeader.getId().toString());
		transactionBank.setDrAmount(contraBean.getAmount().toString());
		transactionBank.setCrAmount("0");

		VoucherDetail vDetailCash = new VoucherDetail();
		Transaxtion transactionCash = new Transaxtion();

		vDetailCash.setLineId(2);
		vDetailCash.setVoucherHeaderId(voucherHeader);
		CChartOfAccounts cashAccountCode = commonsService
				.getCChartOfAccountsByGlCode(contraBean.getCashInHand());
		vDetailCash.setGlCode(cashAccountCode.getGlcode());
		vDetailCash.setAccountName(cashAccountCode.getName());
		vDetailCash.setDebitAmount(BigDecimal.ZERO);
		vDetailCash.setCreditAmount(contraBean.getAmount());
		vDetailCash.setNarration(voucherHeader.getVouchermis().getNarration());

		transactionCash.setVoucherLineId("2");
		transactionCash.setGlCode(cashAccountCode.getGlcode());
		transactionCash.setGlName(cashAccountCode.getName());
		transactionCash.setVoucherHeaderId(voucherHeader.getId().toString());
		transactionCash.setDrAmount("0");
		transactionCash.setCrAmount(contraBean.getAmount().toString());

		try {
			vdPersitSer.persist(vDetailBank);
			vdPersitSer.persist(vDetailCash);
		} catch (HibernateException e) {
			LOGGER.debug("Exception Occured in Contra Service while preparing transaction"
					+ e);
			throw new HibernateException(e);
		}

		transaxtionList.add(transactionBank);
		transaxtionList.add(transactionCash);

		return transaxtionList;
	}

	public Map<String, Object> getCTBVoucher(final String voucherId,
			ContraBean contraBean) {
		LOGGER.debug("ContraService | getCTBVoucher | Start");
		Map<String, Object> voucherMap = new HashMap<String, Object>();
		CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService
				.find("from CVoucherHeader where id=?", Long.valueOf(voucherId));
		voucherMap.put(Constants.VOUCHERHEADER, voucherHeader);
		InstrumentVoucher iVoucher = (InstrumentVoucher) persistenceService
				.find("from InstrumentVoucher where voucherHeaderId=?",
						voucherHeader);
		Bankaccount bankAccount = iVoucher.getInstrumentHeaderId()
				.getBankAccountId();
		contraBean.setAccountNumberId(bankAccount.getId().toString());
		contraBean.setAccnumnar(bankAccount.getNarration());
		contraBean.setBankBranchId(bankAccount.getBankbranch().getBank()
				.getId()
				+ "-" + bankAccount.getBankbranch().getId().toString());
		LOGGER.debug("Cash amount = "
				+ iVoucher.getInstrumentHeaderId().getInstrumentAmount());
		contraBean.setAmount(iVoucher.getInstrumentHeaderId()
				.getInstrumentAmount());
		contraBean.setChequeNumber(iVoucher.getInstrumentHeaderId()
				.getTransactionNumber());
		if (iVoucher.getInstrumentHeaderId().getTransactionDate() != null) {
			contraBean.setChequeDate(Constants.DDMMYYYYFORMAT2.format(iVoucher
					.getInstrumentHeaderId().getTransactionDate()));
		}
		voucherMap.put("contrabean", contraBean);
		return voucherMap;

	}

	public Map<String, Object> getpayInSlipVoucher(final Long voucherId,
			ContraBean contraBean, List<PayInBean> iHeaderList) {
		LOGGER.debug("ContraService | getpayInSlipVoucher | Start");
		final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd",
				Locale.ENGLISH);
		Map<String, Object> voucherMap = new HashMap<String, Object>();
		iHeaderList = new ArrayList<PayInBean>();
		CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService
				.find("from CVoucherHeader where id=?", voucherId);
		voucherMap.put(Constants.VOUCHERHEADER, voucherHeader);
		List<InstrumentOtherDetails> iOther = (List<InstrumentOtherDetails>) persistenceService
				.findAllBy("from InstrumentOtherDetails where payinslipId=?",
						voucherHeader);
		Bankaccount bankAccount = iOther.get(0).getInstrumentHeaderId()
				.getBankAccountId();
		contraBean.setAccountNumberId(bankAccount.getId().toString());
		contraBean.setAccnumnar(bankAccount.getNarration());
		contraBean.setBankBranchId(bankAccount.getBankbranch().getBank()
				.getId()
				+ "-" + bankAccount.getBankbranch().getId().toString());
		voucherMap.put("contraBean", contraBean);
		PayInBean payInBean;
		BigDecimal totalInstrAmt = BigDecimal.ZERO;
		for (InstrumentOtherDetails instrumentOtherDetails : iOther) {
			InstrumentHeader iHeader = instrumentOtherDetails
					.getInstrumentHeaderId();
			int index = 0;
			payInBean = new PayInBean();
			payInBean.setInstId(Long.valueOf(iHeader.getId().toString()));
			payInBean.setInstrumentNumber(iHeader.getInstrumentNumber());
			try {
				payInBean.setInstrumentDate(formatter.format(formatter1
						.parse(iHeader.getInstrumentDate().toString())));
				InstrumentVoucher iVoucher = (InstrumentVoucher) persistenceService
						.find("from InstrumentVoucher where instrumentHeaderId=?",
								iHeader);
				payInBean.setVoucherDate(formatter.format(formatter1
						.parse(iVoucher.getVoucherHeaderId().getVoucherDate()
								.toString())));
			} catch (ParseException e) {
				LOGGER.error("Exception Occured while Parsing instrument date"
						+ e.getMessage());
			}
			payInBean.setInstrumentAmount(iHeader.getInstrumentAmount()
					.toString());
			payInBean.setVoucherNumber(instrumentOtherDetails.getPayinslipId()
					.getVoucherNumber());
			payInBean.setSelectChq(true);
			payInBean.setSerialNo(++index);
			iHeaderList.add(payInBean);
			totalInstrAmt = totalInstrAmt.add(iHeader.getInstrumentAmount());
		}
		voucherMap.put("iHeaderList", iHeaderList);
		voucherMap.put("totalInstrAmt", totalInstrAmt.toString());
		return voucherMap;

	}

	/**
	 * This method will be called for remit to bank in case of cheque/dd/card
	 * deposit where a Contra Voucher is generated
	 * 
	 * @param payInId
	 * @param toBankaccountGlcode
	 * @param instrumentHeader
	 */

	public void updateCheque_DD_Card_Deposit(Long payInId,
			String toBankaccountGlcode, InstrumentHeader instrumentHeader) {

		PersistenceService<AppConfig, Integer> appConfigSer;
		appConfigSer = new PersistenceService<AppConfig, Integer>();
		appConfigSer.setSessionFactory(new SessionFactory());
		appConfigSer.setType(AppConfig.class);

		AppConfig appConfig = (AppConfig) appConfigSer.find(
				"from AppConfig where key_name =?", "PREAPPROVEDVOUCHERSTATUS");
		if (null != appConfig && null != appConfig.getAppDataValues()) {
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
				preapprovalStatus = Integer.valueOf(appConfigVal.getValue());
			}
		} else
			throw new EGOVRuntimeException(
					"Appconfig value for PREAPPROVEDVOUCHERSTATUS is not defined in the system");

		CVoucherHeader payIn = (CVoucherHeader) persistenceService.find(
				"from CVoucherHeader where id=?", payInId);
		Bankaccount depositedBankAccount = (Bankaccount) persistenceService
				.find("from Bankaccount where chartofaccounts.glcode=?",
						toBankaccountGlcode);

		updateInstrumentAndPayin(payIn, depositedBankAccount, instrumentHeader,
				FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
		ContraJournalVoucher cjv = addToContra(payIn, depositedBankAccount,
				instrumentHeader);
		addToBankRecon(payIn, instrumentHeader);
		/*
		 * if(cjv.getVoucherHeaderId().getModuleId()!=null &&
		 * payIn.getStatus()==preapprovalStatus) new
		 * CreateVoucher().startWorkflow(cjv);
		 */
	}

	/**
	 * This method will be called for remit to bank in case of cheque/dd/card
	 * deposit where a Receipt Voucher is generated
	 * 
	 * @param payInId
	 * @param toBankaccountGlcode
	 * @param instrumentHeader
	 */
	public void updateCheque_DD_Card_Deposit_Receipt(Long receiptId,
			String toBankaccountGlcode, InstrumentHeader instrumentHeader) {
		CVoucherHeader payIn = (CVoucherHeader) persistenceService.find(
				"from CVoucherHeader where id=?", receiptId);
		Bankaccount depositedBankAccount = (Bankaccount) persistenceService
				.find("from Bankaccount where chartofaccounts.glcode=?",
						toBankaccountGlcode);
		updateInstrumentAndPayin(payIn, depositedBankAccount, instrumentHeader,
				FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
		addToBankRecon(payIn, instrumentHeader);
	}

	/**
	 * used by modules which are integrating
	 * 
	 * @return
	 */

	public void updateCashDeposit(Long payInId, String toBankaccountGlcode,
			InstrumentHeader instrumentHeader) {
		LOGGER.debug("Contra Service | updateCashDeposit | Start");

		PersistenceService<AppConfig, Integer> appConfigSer;
		appConfigSer = new PersistenceService<AppConfig, Integer>();
		appConfigSer.setSessionFactory(new SessionFactory());
		appConfigSer.setType(AppConfig.class);

		AppConfig appConfig = (AppConfig) appConfigSer.find(
				"from AppConfig where key_name =?", "PREAPPROVEDVOUCHERSTATUS");
		if (null != appConfig && null != appConfig.getAppDataValues()) {
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
				preapprovalStatus = Integer.valueOf(appConfigVal.getValue());
			}
		} else
			throw new EGOVRuntimeException(
					"Appconfig value for PREAPPROVEDVOUCHERSTATUS is not defined in the system");

		CVoucherHeader payIn = (CVoucherHeader) persistenceService.find(
				"from CVoucherHeader where id=?", payInId);
		Bankaccount depositedBankAccount = (Bankaccount) persistenceService
				.find("from Bankaccount where chartofaccounts.glcode=?",
						toBankaccountGlcode);

		updateInstrumentAndPayin(payIn, depositedBankAccount, instrumentHeader,
				FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
		ContraJournalVoucher cjv = addToContra(payIn, depositedBankAccount,
				instrumentHeader);
		addToBankRecon(payIn, instrumentHeader);
		/*
		 * if(cjv.getVoucherHeaderId().getModuleId()!=null &&
		 * payIn.getStatus()==preapprovalStatus ) new
		 * CreateVoucher().startWorkflow(cjv);
		 */
	}

	public void createVoucherfromPreApprovedVoucher(ContraJournalVoucher cjv) {
		final List<AppConfigValues> appList = genericDao
				.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF",
						"APPROVEDVOUCHERSTATUS");
		final String approvedVoucherStatus = appList.get(0).getValue();
		cjv.getVoucherHeaderId().setStatus(
				Integer.valueOf(approvedVoucherStatus));
	}

	public void cancelVoucher(ContraJournalVoucher cjv) {
		final List<AppConfigValues> appList = genericDao
				.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF",
						"cancelledstatus");
		final String approvedVoucherStatus = appList.get(0).getValue();
		cjv.getVoucherHeaderId().setStatus(
				Integer.valueOf(approvedVoucherStatus));
	}

	public String getDesginationName() {
		PersonalInformation pi = employeeService.getEmpForUserId(Integer
				.parseInt(EGOVThreadLocals.getUserId()));
		Assignment assignment = employeeService.getAssignmentByEmpAndDate(
				new Date(), pi.getIdPersonalInformation());
		return assignment.getDesigId().getDesignationName();
	}

	public Department getDepartmentForWfItem(ContraJournalVoucher cjv) {
		PersonalInformation pi = employeeService.getEmpForUserId(cjv
				.getCreatedBy().getId());
		Assignment assignment = employeeService.getAssignmentByEmpAndDate(
				new Date(), pi.getIdPersonalInformation());
		return assignment.getDeptId();
	}

	public Boundary getBoundaryForUser(ContraJournalVoucher rv) {
		return new EgovCommon().getBoundaryForUser(rv.getCreatedBy());
	}

	public Position getPositionForEmployee(PersonalInformation emp)
			throws EGOVRuntimeException {
		return employeeService
				.getPositionforEmp(emp.getIdPersonalInformation());
	}

	private void addToBankRecon(CVoucherHeader payIn,
			InstrumentHeader instrumentHeader) {
		instrumentService.addToBankReconcilation(payIn, instrumentHeader);

	}

	private ContraJournalVoucher addToContra(CVoucherHeader payIn,
			Bankaccount depositedBank, InstrumentHeader instrumentHeader) {
		ContraJournalVoucher cjv = new ContraJournalVoucher();
		cjv.setToBankAccountId(depositedBank);
		cjv.setInstrumentHeaderId(instrumentHeader);
		cjv.setVoucherHeaderId(payIn);
		contrajournalService.persist(cjv);
		return cjv;
	}

	private void updateInstrumentAndPayin(CVoucherHeader payIn,
			Bankaccount account, InstrumentHeader instrumentHeader,
			String status) {

		Map<String, Object> iMap = new HashMap<String, Object>();
		List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
		// List<InstrumentHeader> iHeaderList = createInstruements();
		iMap.put("Instrument header", instrumentHeader);
		iMap.put("Payin slip id", payIn);
		iMap.put("Instrument status date", payIn.getVoucherDate());
		iMap.put("Status id", status);
		iMap.put("Bank account id", account);
		iList.add(iMap);
		instrumentService.updateInstrumentOtherDetails(iList);
	}

	@SuppressWarnings("unchecked")
	public void editInstruments(Long voucherId) {

		List<InstrumentOtherDetails> iOtherdetails = (List<InstrumentOtherDetails>) persistenceService
				.findAllBy(
						"from InstrumentOtherDetails  io where payinslipId.id=?",
						voucherId);

		for (InstrumentOtherDetails instrumentOtherDetails : iOtherdetails) {
			instrumentService.editInstruments(instrumentOtherDetails);
		}
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public PersistenceService<ContraJournalVoucher, Long> getContrajournalService() {
		return contrajournalService;
	}

	public void setContrajournalService(
			PersistenceService<ContraJournalVoucher, Long> contrajournalService) {
		this.contrajournalService = contrajournalService;
	}

	public PersistenceService<Bankreconciliation, Integer> getBankReconService() {
		return bankReconService;
	}

	public void setBankReconService(
			PersistenceService<Bankreconciliation, Integer> bankReconService) {
		this.bankReconService = bankReconService;
	}

	public PersistenceService<VoucherDetail, Long> getVdPersitSer() {
		return vdPersitSer;
	}

	public void setVdPersitSer(
			PersistenceService<VoucherDetail, Long> vdPersitSer) {
		this.vdPersitSer = vdPersitSer;
	}

	public InstrumentService getInstrumentService() {
		return instrumentService;
	}

	public void setInstrumentService(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
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

}