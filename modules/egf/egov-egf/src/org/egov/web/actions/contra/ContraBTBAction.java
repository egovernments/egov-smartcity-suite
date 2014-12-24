/**
 * 
 */
package org.egov.web.actions.contra;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.contra.ContraBean;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.voucher.VoucherTypeBean;
import org.egov.services.cheque.ChequeService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.report.FundFlowService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.voucher.BaseVoucherAction;
import org.egov.web.actions.voucher.CommonAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.HibernateException;
import org.hibernate.jdbc.Work;
import org.apache.log4j.Logger;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.Validation;

/**
 * @author mani
 */
@ParentPackage("egov")
@Validation
public class ContraBTBAction extends BaseVoucherAction {
	private static final String			DD_MMM_YYYY					= "dd-MMM-yyyy";
	SimpleDateFormat					sdf							= new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);
	private static final String			EXCEPTION_WHILE_SAVING_DATA	= "Exception while saving Data";
	private final static Logger			LOGGER						= Logger.getLogger(ContraBTBAction.class);
	private static final String			MDC_CHEQUE					= "cheque";
	private static final String			MDC_OTHER					= "other";
	private static final String			REVERSE						= "reverse";
	private static final long			serialVersionUID			= 1L;
	private static final String			TRANSACTION_FAILED			= "Transaction failed";
	private static final String			VIEW						= "view";
	public ContraBean					contraBean;
	public Map<String, String>			fromBankBranchMap;
	public Map<String, String>			ModeOfCollectionMap;
	public Map<String, String>			toBankBranchMap;
	private String						amount;
	private String						button;
	private ContraJournalVoucher		contraVoucher;
	private InstrumentService			instrumentService;
	private String						mode;
	private VoucherService				voucherService;
	private VoucherTypeBean				voucherTypeBean;
	CommonAction						commonAction;
	private GenericHibernateDaoFactory	genericDao;
	private Long						vhId;
	private EgovCommon egovCommon;
	private ChequeService	chequeService;
	private String fromAccnumnar;
	private FundFlowService fundFlowService;
	public void setFundFlowService(FundFlowService fundFlowService) {
		this.fundFlowService = fundFlowService;
	}

	public String getFromAccnumnar() {
		return fromAccnumnar;
	}

	public void setFromAccnumnar(String fromAccnumnar) {
		this.fromAccnumnar = fromAccnumnar;
	}

	private String toAccnumnar;
	
	
	
	/**
	 * @param chequeService the chequeService to set
	 */
	public void setChequeService(ChequeService chequeService) {
		this.chequeService = chequeService;
	}

	@SkipValidation
	public String beforeEdit() {
		prepareForViewModifyReverse();
		return EDIT;
	}
	
	@SkipValidation
	public String beforeReverse() {
		prepareForViewModifyReverse();
		return REVERSE;
	}
	
	@SkipValidation
	public String beforeView() {
		prepareForViewModifyReverse();
		return VIEW;
	}
	
	/**
	 * @return new page
	 * @throws ValidationException
	 * <br>
	 *             The details of transaction is<br>
	 *             <ol>
	 *             <li>addToIntrument- cheque information</li>
	 *             <li>createVoucher- JV creation</li>
	 *             <li>update instrument -link jv and cheque</li>
	 *             <li>addTocontraJournalVoucher</li>
	 *             <li>post to ledger</li>
	 *             </ol>
	 */
	
	public String create() throws ValidationException {
		LOGGER.debug("Starting Bank to Bank Transfer ...");
		try {
			getHibObjectsFromContraBean();
			if(egovCommon.isShowChequeNumber())
			{
			if(contraBean.getModeOfCollection().equals(MDC_CHEQUE))
				{
				validateChqNumber(contraBean.getChequeNumber(), contraVoucher.getFromBankAccountId().getId(), voucherHeader);
				}
			}
			final List<InstrumentHeader> instrumentList = instrumentService.addToInstrument(createInstruments(contraBean, contraVoucher));
			voucherHeader = callCreateVoucher(voucherHeader, contraVoucher);
			LOGGER.debug("Voucher Created");
			updateInstrument(instrumentList.get(0), voucherHeader);
			//DepositCheque(voucherHeader, contraVoucher, instrumentList.get(0));
			contraVoucher = addOrupdateContraJournalVoucher(contraVoucher, instrumentList.get(0), voucherHeader);
			addActionMessage(getText("transaction.success") + voucherHeader.getVoucherNumber());
			setVhId(voucherHeader.getId());
			LoadAjaxedDropDowns();
			LOGGER.debug("Completed Bank to Bank Transfer .");
		} catch (final ValidationException e) {
			LoadAjaxedDropDowns();
			throw e;
		}
		return NEW;
	}
	
	/**
	 * @return edit page <br>
	 *         Modifies the Bank to Bank Transfer ContraVoucher . &nbsp; &nbsp;
	 *         &nbsp; Details of transaction:<br>
	 *         <ol>
	 *         <li>update the VoucherHeader for all mis attribute changes</li>
	 *         <li>If Account Numbers changed<ol type="a">
	 *         <li>cancel the Instrument</li>
	 *         <li>create new Instrument</li>
	 *         <li>delete ledger</li>
	 *         <li>recreate Ledger</li>
	 *         </ol>
	 *         <li>If only instrument date and amount is changed update only
	 *         instrument</li></ol>
	 */
	public String edit() {
		validateFields();
		getHibObjectsFromContraBean();
		
		final CVoucherHeader oldVoucher = voucherService.updateVoucherHeader(voucherHeader, voucherTypeBean);
		final ContraJournalVoucher oldContraVoucher = (ContraJournalVoucher) persistenceService.find("from ContraJournalVoucher where voucherHeaderId.id=?",
				oldVoucher.getId());
		List exludeStatusList=getExcludeStatusListForInstruments();
		final InstrumentVoucher instrumentVoucher = (InstrumentVoucher) persistenceService.find("from InstrumentVoucher iv where iv.instrumentHeaderId.statusId not in (?) and voucherHeaderId.id=?",exludeStatusList.get(0), oldVoucher
				.getId());
		if (instrumentVoucher == null) {
			LOGGER.error("System Error :Instrument is not linked with voucher ");
			throw new EGOVRuntimeException(" System Error :Instrument is not linked with voucher  ");
		}
		final InstrumentHeader oldInstrumentHeader = instrumentVoucher.getInstrumentHeaderId();
		if (!oldContraVoucher.getFromBankAccountId().getId().toString().equals(contraBean.getFromBankAccountId())
				|| !oldContraVoucher.getToBankAccountId().getId().toString().equals(contraBean.getToBankAccountId())) {
			instrumentService.cancelInstrument(oldInstrumentHeader);
			HibernateUtil.getCurrentSession().flush();
			if(contraBean.getModeOfCollection().equals(MDC_CHEQUE))
			{
				if(!egovCommon.isShowChequeNumber())
				{
					try {
						contraBean.setChequeNumber(chequeService.nextChequeNumber(contraBean.getFromBankAccountId(), 1, voucherHeader.getVouchermis().getDepartmentid().getId()));
					} catch (EGOVRuntimeException e) {
						throw new ValidationException(Arrays.asList(new ValidationError("Exception while getting Cheque Number  ",e.getMessage())));
					}
				}else
				{
					validateChqNumber(contraBean.getChequeNumber(), contraVoucher.getFromBankAccountId().getId(), voucherHeader);
				}
			}
			final List<InstrumentHeader> instrumentList = instrumentService.addToInstrument(createInstruments(contraBean, contraVoucher));
			updateInstrument(instrumentList.get(0), oldVoucher);
			//DepositCheque(oldVoucher, contraVoucher, instrumentList.get(0));
			HibernateUtil.getCurrentSession().flush();
			final ContraJournalVoucher contraVoucher = addOrupdateContraJournalVoucher(oldContraVoucher, instrumentList.get(0), oldVoucher);
			createLedgerAndPost(oldVoucher, contraVoucher);
		}
		else {
			checkAndUpdateInstrument(oldInstrumentHeader, contraVoucher, oldVoucher);
		}
			LoadAjaxedDropDowns();
			
		addActionMessage(getText("transaction.success") + oldVoucher.getVoucherNumber());
		voucherHeader=oldVoucher;
		setVhId(voucherHeader.getId());
		return EDIT;
		
	}
	
	/**
	 * @return
	 */
	private List<EgwStatus> getExcludeStatusListForInstruments() {
		List<EgwStatus> exList=new ArrayList<EgwStatus>();
		EgwStatus statusId = instrumentService.getStatusId(FinancialConstants.INSTRUMENT_CANCELLED_STATUS);
		exList.add(statusId);
		return exList;
	}

	@Override
	public String execute() {
		return NEW;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public String getButton() {
		return button;
	}
	
	public CommonAction getCommonAction() {
		return commonAction;
	}
	
	public ContraJournalVoucher getContraVoucher() {
		return contraVoucher;
	}
	
	public Map<String, String> getFromBankBranchMap() {
		return fromBankBranchMap;
	}
	
	/**
	 * @return the genericDao
	 */
	public GenericHibernateDaoFactory getGenericDao() {
		return genericDao;
	}
	
	public String getMode() {
		return mode;
	}
	
	@Override
	public Object getModel() {
		voucherHeader = (CVoucherHeader) super.getModel();
		voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA);
		voucherHeader.setName(FinancialConstants.CONTRAVOUCHER_NAME_BTOB);
		return voucherHeader;
	}
	
	public Map<String, String> getModeOfCollectionMap() {
		return ModeOfCollectionMap;
	}
	
	public Map<String, String> getToBankBranchMap() {
		return toBankBranchMap;
	}
	
	/**
	 * @return the vhId
	 */
	public Long getVhId() {
		return vhId;
	}
	
	public VoucherTypeBean getVoucherTypeBean() {
		return voucherTypeBean;
	}
	
	@Override
	@SkipValidation
	public String newform() {
		LoadAjaxedDropDowns();
		reset();
		loadDefalutDates();
		final Date currDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		contraBean.setChequeDate(sdf.format(currDate));
		return NEW;
	}
	
	@Override
	public void prepare() {
		super.prepare();
		ModeOfCollectionMap = new LinkedHashMap<String, String>();
		ModeOfCollectionMap.put(MDC_CHEQUE, MDC_CHEQUE);
		ModeOfCollectionMap.put(MDC_OTHER, MDC_OTHER);
		
		// contraBean.setModeOfCollection("cheque");
		LoadAjaxedDropDowns();
	}
	
	public String reset() {
		voucherHeader.reset();
		contraBean.reset();
		setAmount(null);
		return NEW;
	}
	
	@ValidationErrorPage(value = "reverse")
	public String reverse() {
		final CreateVoucher cv = new CreateVoucher();
		CVoucherHeader reversalVoucher = null;
		final HashMap<String, Object> reversalVoucherMap = new HashMap<String, Object>();
		reversalVoucherMap.put("Original voucher header id", voucherHeader.getId());
		reversalVoucherMap.put("Reversal voucher type", "Receipt");
		reversalVoucherMap.put("Reversal voucher name", "Contra");
		try {
			reversalVoucherMap.put("Reversal voucher date", sdf.parse(getReversalVoucherDate()));
		} catch (final ParseException e1) {
			throw new ValidationException(Arrays.asList(new ValidationError("reversalVocuherDate", "reversalVocuherDate.notinproperformat")));
		}
		reversalVoucherMap.put("Reversal voucher number", getReversalVoucherNumber());
		final List<HashMap<String, Object>> reversalList = new ArrayList<HashMap<String, Object>>();
		reversalList.add(reversalVoucherMap);
		try {
			reversalVoucher = cv.reverseVoucher(reversalList);
		} catch (final EGOVRuntimeException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("Failed while Reversing", "Failed while Reversing")));
		} catch (final ParseException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("Date is not in proper Format", "Date is not in proper Format")));
			
		}
		LoadAjaxedDropDowns();
		addActionMessage(getText("contra.reverse.transaction.success") + reversalVoucher.getVoucherNumber());
		voucherHeader.setId(reversalVoucher.getId());
		setVhId(reversalVoucher.getId());
		return REVERSE;
	}
	
	public void setAmount(final String amount) {
		this.amount = amount;
	}
	
	public void setButton(final String button) {
		this.button = button;
	}
	
	public void setCommonAction(final CommonAction commonAction) {
		this.commonAction = commonAction;
	}
	
	public void setContraBean(final ContraBean contraBean) {
		this.contraBean = contraBean;
	}
	
	// setters
	public void setContraVoucher(final ContraJournalVoucher cjv) {
		contraVoucher = cjv;
	}
	
	public void setFromBankBranchMap(final Map<String, String> fromBankBranchMap) {
		this.fromBankBranchMap = fromBankBranchMap;
	}
	
	/**
	 * @param genericDao
	 *            the genericDao to set
	 */
	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	
	public void setInstrumentService(final InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}
	
	public void setMode(final String mode) {
		this.mode = mode;
	}
	
	public void setModeOfCollectionMap(final Map<String, String> modeOfCollectionMap) {
		ModeOfCollectionMap = modeOfCollectionMap;
	}
	
	public void setToBankBranchMap(final Map<String, String> toBankBranchMap) {
		this.toBankBranchMap = toBankBranchMap;
	}
	
	/**
	 * @param vhId
	 *            the vhId to set
	 */
	public void setVhId(final Long vhId) {
		this.vhId = vhId;
	}
	
	public void setVoucherService(final VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	
	public void setVoucherTypeBean(final VoucherTypeBean voucherTypeBean) {
		this.voucherTypeBean = voucherTypeBean;
	}
	
	@Override
	public void validate() {
		if (getButton().contains("Reverse")) {
			if (getReversalVoucherDate() == null) {
				addFieldError("reversalVocuherDate", getText("reversalVocuherDate.required"));
			}
			else {
				try {
					sdf.parse(getReversalVoucherDate());
					checkMandatoryField("voucherNumber", getReversalVoucherNumber(), "voucherNumberRequired");
				} catch (final ParseException e) {
					addFieldError("reversalVocuherDate", getText("reversalVocuherDate.notinproperformat"));
				} catch (final ValidationException e) {
					addFieldError("reversalVocuherNumber", getText("reversalVocuherDate.required"));
				}
				
			}
			
		}
		else {
			
			if (contraBean.getFromBankId() == null || contraBean.getFromBankId().equals("-1")) {
				addFieldError("contraBean.fromBankId", getText("fromBankId.required"));
			}
			if (contraBean.getToBankId() == null || contraBean.getToBankId().equals("-1")) {
				addFieldError("contraBean.toBankId()", getText("toBankId.required"));
			}
			if (contraBean.getFromBankAccountId() == null || contraBean.getFromBankAccountId().equals("-1")) {
				addFieldError("contraBean.frombankAccountId", getText("fromBankAccountId.required"));
			}
			if (contraBean.getToBankAccountId() == null || contraBean.getToBankAccountId().equals("-1")) {
				addFieldError("contraBean.toBankAccountId()", getText("toBankAccountId.required"));
			}
			if(egovCommon.isShowChequeNumber() || contraBean.getModeOfCollection().equals(MDC_OTHER) )
			{
				if (contraBean.getChequeNumber() == null || contraBean.getChequeNumber().isEmpty()) {
					addFieldError("contraBean.chequeNumber", getText("ChequeNumber.required"));
				}
				
				if (contraBean.getChequeDate() == null || contraBean.getChequeDate().isEmpty()) {
					addFieldError("contraBean.chequeDate", getText("fromChequeDate.required"));
				}
			}
						
			if (getAmount() == null) {
				addFieldError("amount", getText("amount.required"));
			}
			else {
				try {
					contraBean.setAmount(new BigDecimal(getAmount()));
					if(new BigDecimal(contraBean.getFromBankBalance()).compareTo(contraBean.getAmount()) == -1 && isBankBalanceMandatory()){
						addActionError(getText("contra.insufficient.bankbalance",new String[]{""+contraBean.getFromBankBalance()}));
					}
				} catch (final NumberFormatException e) {
					addFieldError("amount", getText("amount.nonnumeric"));
					
				}
			}
			if (contraBean.getFromBankAccountId() != null && !contraBean.getFromBankAccountId().equals("-1") && contraBean.getToBankAccountId() != null
					&& !contraBean.getToBankAccountId().equals("-1")) {
				if (contraBean.getFromBankAccountId().equalsIgnoreCase(contraBean.getToBankAccountId())) {
					addFieldError("contraBean.fromBankId", getText("same.Account.Transfer.notAllowed"));
				}
				
			}
			getHibObjectsFromContraBean();
			LoadAjaxedDropDowns();
			
		}
		
	}
	
	private ContraJournalVoucher addOrupdateContraJournalVoucher(final ContraJournalVoucher cjv, final InstrumentHeader ih, final CVoucherHeader vh) {
		cjv.setInstrumentHeaderId(ih);
		final PersistenceService<ContraJournalVoucher, Long> contraJVService = new PersistenceService<ContraJournalVoucher, Long>();
		contraJVService.setType(ContraJournalVoucher.class);
		contraJVService.setSessionFactory(new SessionFactory());
		cjv.setVoucherHeaderId(vh);
		getHibObjectsFromContraBean();
		cjv.setFromBankAccountId(contraVoucher.getFromBankAccountId());
		cjv.setToBankAccountId(contraVoucher.getToBankAccountId());
		if (cjv.getId() != null) {
			contraJVService.update(cjv);
			LOGGER.info("Contra Journal Voucher Updated");
		}
		else {
			contraJVService.persist(cjv);
			LOGGER.info("Contra Journal Voucher created");
		}
		return cjv;
	}
	
	private CVoucherHeader callCreateVoucher(final CVoucherHeader voucher, final ContraJournalVoucher contraVoucher) {
		try {
			final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
			// update ContraBTB source path
			headerDetails.put(VoucherConstant.SOURCEPATH, "/EGF/contra/contraBTB!beforeView.action?voucherHeader.id=");
			
			HashMap<String, Object> detailMap = null;
			final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
			final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
			
			detailMap = new HashMap<String, Object>();
			detailMap.put(VoucherConstant.CREDITAMOUNT, contraBean.getAmount().toString());
			detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
			detailMap.put(VoucherConstant.GLCODE, contraVoucher.getFromBankAccountId().getChartofaccounts().getGlcode());
			accountdetails.add(detailMap);
			
			detailMap = new HashMap<String, Object>();
			detailMap.put(VoucherConstant.DEBITAMOUNT, contraBean.getAmount().toString());
			detailMap.put(VoucherConstant.CREDITAMOUNT, "0");
			detailMap.put(VoucherConstant.GLCODE, contraVoucher.getToBankAccountId().getChartofaccounts().getGlcode());
			accountdetails.add(detailMap);
			final CreateVoucher cv = new CreateVoucher();
			voucherHeader = cv.createVoucher(headerDetails, accountdetails, subledgerDetails);
			
		} catch (final HibernateException e) {
			LOGGER.error(e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
		} catch (final EGOVRuntimeException e) {
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		} catch (final ValidationException e) {
			throw e;
		} catch (final Exception e) {
			// handle engine exception
			LOGGER.error(e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		}
		LOGGER.debug("Posted to Ledger " + voucherHeader.getId());
		return voucherHeader;
		
	}
	
	private boolean checkAndUpdateInstrument(final InstrumentHeader oldInstrumentHeader, final ContraJournalVoucher contraVoucher2,
			final CVoucherHeader oldVoucher) {
		Date newInstrumentDate = null;
		boolean updateInstrument = false;
		try {
			
			if (contraBean.getModeOfCollection().equalsIgnoreCase(MDC_CHEQUE)) {
				
				if (oldInstrumentHeader.getInstrumentNumber() != null) {
				if (!oldInstrumentHeader.getInstrumentNumber().equalsIgnoreCase(contraBean.getChequeNumber())) {
					instrumentService.cancelInstrument(oldInstrumentHeader);
					HibernateUtil.getCurrentSession().flush();
					validateChqNumber(contraBean.getChequeNumber(), contraVoucher2.getFromBankAccountId().getId(), oldVoucher);
					final List<InstrumentHeader> instrumentList = instrumentService.addToInstrument(createInstruments(contraBean, contraVoucher2));
					updateInstrument(instrumentList.get(0), oldVoucher);
					//	DepositCheque(oldVoucher, contraVoucher, instrumentList.get(0));
					HibernateUtil.getCurrentSession().flush();
					final ContraJournalVoucher contraVoucher = addOrupdateContraJournalVoucher(contraVoucher2, instrumentList.get(0), oldVoucher);
					createLedgerAndPost(oldVoucher, contraVoucher);
				}else
				{
					newInstrumentDate = Constants.DDMMYYYYFORMAT2.parse(contraBean.getChequeDate());
					if (!(oldInstrumentHeader.getInstrumentDate().compareTo(newInstrumentDate)==0)) 
					{
						oldInstrumentHeader.setInstrumentDate(newInstrumentDate);
						updateInstrument = true;
					}
					if(!(oldInstrumentHeader.getInstrumentAmount().compareTo(contraBean.getAmount())==0))
					{
						oldInstrumentHeader.setInstrumentAmount(contraBean.getAmount());
						updateInstrument = true;
					}
					updateInstrument = true;
				
				}
				}
				else {// if earlier it is Other and now it is cheque then reset
						// transaction number and date
					instrumentService.cancelInstrument(oldInstrumentHeader);
					HibernateUtil.getCurrentSession().flush();
					if(!egovCommon.isShowChequeNumber())
					{
						try {
							contraBean.setChequeNumber(chequeService.nextChequeNumber(contraBean.getFromBankAccountId(), 1, voucherHeader.getVouchermis().getDepartmentid().getId()));
						} catch (EGOVRuntimeException e) {
							throw new ValidationException(Arrays.asList(new ValidationError("Exception while getting Cheque Number  ",e.getMessage())));
						}
					}
					validateChqNumber(contraBean.getChequeNumber(), contraVoucher2.getFromBankAccountId().getId(), oldVoucher);
					final List<InstrumentHeader> instrumentList = instrumentService.addToInstrument(createInstruments(contraBean, contraVoucher2));
					updateInstrument(instrumentList.get(0), oldVoucher);
					//DepositCheque(oldVoucher, contraVoucher, instrumentList.get(0));
					HibernateUtil.getCurrentSession().flush();
					final ContraJournalVoucher contraVoucher = addOrupdateContraJournalVoucher(contraVoucher2, instrumentList.get(0), oldVoucher);
					createLedgerAndPost(oldVoucher, contraVoucher);
					
					
				}
			}
			else if (contraBean.getModeOfCollection().equalsIgnoreCase(MDC_OTHER)) {
				// if earlier it is cheque and now it is other then reset
				// instrment number and set transaction number and date
				if (oldInstrumentHeader.getInstrumentNumber() != null) {
					instrumentService.cancelInstrument(oldInstrumentHeader);
					HibernateUtil.getCurrentSession().flush();
					final List<InstrumentHeader> instrumentList = instrumentService.addToInstrument(createInstruments(contraBean, contraVoucher2));
					updateInstrument(instrumentList.get(0), oldVoucher);
					//DepositCheque(oldVoucher, contraVoucher, instrumentList.get(0));
					HibernateUtil.getCurrentSession().flush();
					final ContraJournalVoucher contraVoucher = addOrupdateContraJournalVoucher(contraVoucher2, instrumentList.get(0), oldVoucher);
					createLedgerAndPost(oldVoucher, contraVoucher);
				}
				else {
					// if earlier it is other and and now also other just update
					newInstrumentDate = Constants.DDMMYYYYFORMAT2.parse(contraBean.getChequeDate());
					oldInstrumentHeader.setInstrumentDate(null);
					oldInstrumentHeader.setTransactionDate(newInstrumentDate);
					updateInstrument = true;
					
					if (!(contraBean.getAmount().compareTo(new BigDecimal(oldInstrumentHeader.getInstrumentAmount().toString())) == 0)) {
						oldInstrumentHeader.setInstrumentAmount(contraBean.getAmount());
						oldInstrumentHeader.setTransactionNumber(contraBean.getChequeNumber());
						updateInstrument = true;
					}
					
				}
			}
			
		} catch (final ParseException e) {
			throw new EGOVRuntimeException(e.getMessage());
		}
		
		if (updateInstrument) {
			instrumentService.instrumentHeaderService.update(oldInstrumentHeader);
			createLedgerAndPost(oldVoucher, contraVoucher2);
		}
		return updateInstrument;
	}
	
	private List<Map<String, Object>> createInstruments(final ContraBean cBean, final ContraJournalVoucher cVoucher) {
		final Map<String, Object> iMap = new HashMap<String, Object>();
		final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
		
		Date dt = null;
		
		
		
		iMap.put("Instrument amount", Double.valueOf(cBean.getAmount().toString()));
		
		iMap.put("Bank code", contraVoucher.getFromBankAccountId().getBankbranch().getBank().getCode());
		iMap.put("Bank branch name", cVoucher.getFromBankAccountId().getBankbranch().getBranchaddress1());
		iMap.put("Bank account id", cVoucher.getFromBankAccountId().getId());
		
		
		
		if (cBean.getModeOfCollection().equalsIgnoreCase(MDC_CHEQUE)) {
			if(!egovCommon.isShowChequeNumber())
			{
				
				try {
					iMap.put("Instrument number",chequeService.nextChequeNumber(cVoucher.getFromBankAccountId().getId().toString(), 1, voucherHeader.getVouchermis().getDepartmentid().getId()));
				} catch (EGOVRuntimeException e) {
					throw new ValidationException(Arrays.asList(new ValidationError("Exception while getting Cheque Number  ",e.getMessage())));
				}
				
				iMap.put("Instrument date", new Date());
			}
			else
			{
				iMap.put("Instrument number", cBean.getChequeNumber());
				try {
					dt=sdf.parse(contraBean.getChequeDate());
				} catch (ParseException e) {
					throw new ValidationException(Arrays.asList(new ValidationError("Exception while formatting ChequeDate ", "TRANSACTION_FAILED")));
				}
				iMap.put("Instrument date", dt);
			}
			
			iMap.put("Instrument type", FinancialConstants.INSTRUMENT_TYPE_CHEQUE);
			
		}
		else {
			
			iMap.put("Transaction number", cBean.getChequeNumber());
			try {
				dt=sdf.parse(contraBean.getChequeDate());
			} catch (ParseException e) {
				throw new ValidationException(Arrays.asList(new ValidationError("Exception while formatting ChequeDate ", "TRANSACTION_FAILED")));
			}
			iMap.put("Transaction date", dt);
			// change this to advice type later
			iMap.put("Instrument type", FinancialConstants.INSTRUMENT_TYPE_CARD);
		}
		iMap.put("Is pay cheque", "1");
		iList.add(iMap);
		return iList;
	}
	
	private void createLedgerAndPost(final CVoucherHeader voucher, final ContraJournalVoucher contraVoucher) {
		HibernateUtil.getCurrentSession().doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				final CreateVoucher createVoucher = new CreateVoucher();
				try {
					createVoucher.deleteVoucherdetailAndGL(connection, voucher);
					HibernateUtil.getCurrentSession().flush();
					HashMap<String, Object> detailMap = null;
					final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
					final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
					
					detailMap = new HashMap<String, Object>();
					detailMap.put(VoucherConstant.CREDITAMOUNT, contraBean.getAmount().toString());
					detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
					detailMap.put(VoucherConstant.GLCODE, contraVoucher.getFromBankAccountId().getChartofaccounts().getGlcode());
					accountdetails.add(detailMap);
					
					detailMap = new HashMap<String, Object>();
					detailMap.put(VoucherConstant.DEBITAMOUNT, contraBean.getAmount().toString());
					detailMap.put(VoucherConstant.CREDITAMOUNT, "0");
					detailMap.put(VoucherConstant.GLCODE, contraVoucher.getToBankAccountId().getChartofaccounts().getGlcode());
					accountdetails.add(detailMap);
					final CreateVoucher cv = new CreateVoucher();
					final List<Transaxtion> transactions = cv.createTransaction(null,accountdetails, subledgerDetails, voucher);
					HibernateUtil.getCurrentSession().flush();
					
					final ChartOfAccounts engine = ChartOfAccounts.getInstance();
					Transaxtion txnList[] = new Transaxtion[transactions.size()];
					txnList = transactions.toArray(txnList);
					final SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
					if (!engine.postTransaxtions(txnList, connection, formatter.format(voucher.getVoucherDate()))) {
						throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
					}
				} catch (final HibernateException e) {
					LOGGER.error(e.getMessage());
					throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
				} catch (final TaskFailedException e) {
					// handle engine exception
					LOGGER.error(e.getMessage());
					throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
				} catch (final SQLException e) {
					LOGGER.error(e.getMessage());
					throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
					
				} catch (final Exception e) {
					// handle engine exception
					LOGGER.error(e.getMessage());
					throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
				}
				LOGGER.debug("Posted to Ledger ");
			}
		});
	}
	
	/**
	 * @param instrumentHeader
	 * @param contraVoucher2
	 * @param voucherHeader
	 */
	private void DepositCheque(final CVoucherHeader voucherHeader, final ContraJournalVoucher contraVoucher2, final InstrumentHeader instrumentHeader) {
		final Map<String, Object> iMap = new HashMap<String, Object>();
		final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
		// List<InstrumentHeader> iHeaderList = createInstruements();
		iMap.put("Instrument header", instrumentHeader);
		iMap.put("Instrument status date", voucherHeader.getVoucherDate());
		iMap.put("Status id", FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
		iMap.put("Bank account id", contraVoucher2.getFromBankAccountId());
		iList.add(iMap);
		instrumentService.updateInstrumentOtherDetails(iList);
	}
	
	@SuppressWarnings("unchecked")
	private List<Bankaccount> getAccountNumbers(final Integer branchId , Integer fundId,String typeOfAccount) {
		List<Bankaccount> accountNumbersList = new ArrayList<Bankaccount>();
		String [] strArray = typeOfAccount.split(",");
		if (branchId != null) {
			accountNumbersList = persistenceService.findAllBy("from Bankaccount account where account.bankbranch.id=? and account.fund.id=?  and account.isactive='1' and account.type in (?, ?)", branchId,fundId,(String)strArray[0], (String)strArray[1]);
		}
		return accountNumbersList;
	}
	
	@SuppressWarnings("unchecked")
	private Map getBankBranches(final Integer fundId , String typeOfAccount) {
		String [] strArray = typeOfAccount.split(",");
		final Map<String, Object> bankBrmap = new LinkedHashMap();
		if (fundId != null) {
			final List<Object[]> bankBranch = persistenceService
					.findAllBy(
							"select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname "
									+ " FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount "
									+ " where  bank.isactive='1'  and bankBranch.isactive='1' and bankaccount.isactive='1' and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id"
									+ " and bankaccount.fund.id=? and bankaccount.type in (?,?)", fundId,(String)strArray[0], (String)strArray[1]);
			for (final Object[] element : bankBranch) {
				bankBrmap.put(element[0].toString(), element[1].toString());
			}
		}
		return bankBrmap;
	}
	
	/**
	 * this is same as addRelatedEntity() Since this model is different do it
	 * manually
	 */
	private void getHibObjectsFromContraBean() {
		final String bankQry = "from Bankaccount where id=?";
		if (contraBean != null && contraBean.getFromBankAccountId() != null && !contraBean.getFromBankAccountId().equals("-1")) {
			contraVoucher.setFromBankAccountId((Bankaccount) persistenceService.find(bankQry, Integer.valueOf(contraBean.getFromBankAccountId())));
		}
		if (contraBean != null && contraBean.getToBankAccountId() != null && !contraBean.getFromBankAccountId().equals("-1")) {
			contraVoucher.setToBankAccountId((Bankaccount) persistenceService.find(bankQry, Integer.valueOf(contraBean.getToBankAccountId())));
		}
	}
	
	/**
	 * if Bank Account selected(in case of validation errors or view ) Bank
	 * Account list else return empty list
	 */
	private void loadAccountNumbers() {
		
		if (contraVoucher != null && contraVoucher.getFromBankAccountId() != null) {
			addDropdownData("fromAccNumList", getAccountNumbers(contraVoucher.getFromBankAccountId().getBankbranch().getId(),voucherHeader.getFundId().getId(),"RECEIPTS_PAYMENTS,RECEIPTS"));
		}
		else if (contraBean.getFromBankId() != null && !contraBean.getFromBankId().equals("-1")) {
			final String fromBankId = contraBean.getFromBankId();
			final String[] split = fromBankId.split("-");
			if (split[1] != null && !split[1].isEmpty()) {
				addDropdownData("fromAccNumList", getAccountNumbers(Integer.valueOf(split[1]),voucherHeader.getFundId().getId(),"RECEIPTS_PAYMENTS,RECEIPTS"));
			}
		}
		
		else {
			
			addDropdownData("fromAccNumList", Collections.EMPTY_LIST);
		}
		if (contraVoucher != null && contraVoucher.getToBankAccountId() != null) {
			addDropdownData("toAccNumList", getAccountNumbers(contraVoucher.getToBankAccountId().getBankbranch().getId(),voucherHeader.getFundId().getId(),"RECEIPTS_PAYMENTS,PAYMENTS"));
		}
		else if (contraBean.getToBankId() != null && !contraBean.getToBankId().equals("-1")) {
			final String toBankId = contraBean.getToBankId();
			final String[] split = toBankId.split("-");
			if (split[1] != null && !split[1].isEmpty()) {
				addDropdownData("toAccNumList", getAccountNumbers(Integer.valueOf(split[1]),voucherHeader.getFundId().getId(),"RECEIPTS_PAYMENTS,PAYMENTS"));
			}
		}
		else {
			addDropdownData("toAccNumList", Collections.EMPTY_LIST);
		}
	}
	
	/**
	 * Adds empty Map and List for Banks And AccountNumbers for the new action <br>
	 * or Adds Banks and AccountNubers which are Selected through ajax for view,
	 * modify and reverse action
	 */
	
	private void LoadAjaxedDropDowns() {
		loadSchemeSubscheme();
		loadBankBranch();
		loadAccountNumbers();
		loadBankBalances();
	}
	
	/**
	 * 
	 */
	private void loadBankBalances() {
		final EgovCommon common = new EgovCommon();
		common.setPersistenceService(persistenceService);
		common.setGenericDao(genericDao);
		common.setFundFlowService(fundFlowService);
		if (contraVoucher != null && contraVoucher.getFromBankAccountId() != null) {
			LOGGER.info(voucherHeader.getVoucherDate());
			BigDecimal fromBalance;
			try {
				fromBalance = common.getAccountBalance(voucherHeader.getVoucherDate(), contraVoucher.getFromBankAccountId().getId());
			} catch (Exception e) {
				fromBalance=BigDecimal.valueOf(-1);
			}
			
			contraBean.setFromBankBalance(fromBalance.setScale(2).toString());
			
		}
		else {
			contraBean.setFromBankBalance(null);
		}
		if (contraVoucher != null && contraVoucher.getToBankAccountId() != null) {
			BigDecimal toBalance;
			try {
				toBalance = common.getAccountBalance(voucherHeader.getVoucherDate(), contraVoucher.getToBankAccountId().getId());
			} catch (Exception e) {
				toBalance=BigDecimal.valueOf(-1);
			}
			contraBean.setToBankBalance(toBalance.setScale(2).toString());
		}
		else {
			contraBean.setToBankBalance(null);
		}
		
	}
	
	/**
	 * if Bank is selected(in case of validation errors or view )BankAndBranch
	 * map else return empty map
	 */
	@SuppressWarnings("unchecked")
	private void loadBankBranch() {
		if (voucherHeader.getFundId() != null && voucherHeader.getFundId().getId() != null) {
			fromBankBranchMap = getBankBranches(voucherHeader.getFundId().getId(),"RECEIPTS_PAYMENTS,RECEIPTS");
			toBankBranchMap = getBankBranches(voucherHeader.getFundId().getId(),"RECEIPTS_PAYMENTS,PAYMENTS");
		}
		else {
			fromBankBranchMap = Collections.EMPTY_MAP;
			toBankBranchMap = Collections.EMPTY_MAP;
		}
		
	}
	/**
	 * set all the values to beans used in form for display
	 */
	
	private void prepareForViewModifyReverse() {
		voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", voucherHeader.getId());
		LOGGER.debug("voucherHeader.fundId.id" + voucherHeader.getFundId().getId());
		contraVoucher = (ContraJournalVoucher) persistenceService.find("from ContraJournalVoucher where voucherHeaderId=?", voucherHeader);
		contraBean.setFromBankAccountId(contraVoucher.getFromBankAccountId().getId().toString());
		fromAccnumnar=contraVoucher.getFromBankAccountId().getNarration();
		LOGGER.info("fromAccnumnar.............."+fromAccnumnar);
		toAccnumnar=contraVoucher.getToBankAccountId().getNarration();  
		LOGGER.info("toAccnumnar.............."+toAccnumnar);
		contraBean.setToBankAccountId(contraVoucher.getToBankAccountId().getId().toString());
		final String fromBankAndBranchId = contraVoucher.getFromBankAccountId().getBankbranch().getBank().getId().toString() + "-"
				+ contraVoucher.getFromBankAccountId().getBankbranch().getId().toString();
		final String toBankAndBranchId = contraVoucher.getToBankAccountId().getBankbranch().getBank().getId().toString() + "-"
				+ contraVoucher.getToBankAccountId().getBankbranch().getId().toString();
		
		contraBean.setFromBankId(fromBankAndBranchId);
		contraBean.setToBankId(toBankAndBranchId);
		List exludeStatusList=getExcludeStatusListForInstruments();
		final InstrumentVoucher instrumentVoucher = (InstrumentVoucher) persistenceService
				.find("from InstrumentVoucher iv where iv.instrumentHeaderId.statusId not in (?) and  voucherHeaderId=?",exludeStatusList.get(0),voucherHeader);
		if (instrumentVoucher == null) {
			LOGGER.error("System Error :Instrument is not linked with voucher ");
			throw new EGOVRuntimeException(" System Error :Instrument is not linked with voucher  ");
		}
		final InstrumentHeader instrumentHeader = instrumentVoucher.getInstrumentHeaderId();
		
		contraBean.setAmount(instrumentHeader.getInstrumentAmount());
		setAmount(contraBean.getAmount().setScale(2).toString());
		if (instrumentHeader.getTransactionNumber() != null) {
			contraBean.setModeOfCollection(MDC_OTHER);
			contraBean.setChequeNumber(instrumentHeader.getTransactionNumber());
			
			final String chequeDate = Constants.DDMMYYYYFORMAT2.format(instrumentHeader.getTransactionDate());
			contraBean.setChequeDate(chequeDate);
		}
		else {
			contraBean.setModeOfCollection(MDC_CHEQUE);
			contraBean.setChequeNumber(instrumentHeader.getInstrumentNumber());
			final String chequeDate = Constants.DDMMYYYYFORMAT2.format(instrumentHeader.getInstrumentDate());
			contraBean.setChequeDate(chequeDate);
		}
		LoadAjaxedDropDowns(); 
		
	}
	
	/**
	 * @param id
	 */
	private void updateInstrument(final InstrumentHeader ih, final CVoucherHeader vh) {
		final Map<String, Object> iMap = new HashMap<String, Object>();
		final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
		iMap.put("Instrument header", ih);
		iMap.put("Voucher header", vh);
		iList.add(iMap);
		instrumentService.updateInstrumentVoucherReference(iList);
	}
	
	private void validateChqNumber(final String chqNo, final int bankaccId, final CVoucherHeader voucherHeader) {
		if (!instrumentService.isChequeNumberValid(chqNo, bankaccId, voucherHeader.getVouchermis().getDepartmentid().getId())) {
			throw new ValidationException(Arrays.asList(new ValidationError("Invalid cheque number", "Invalid cheque number")));
		}
	}
	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}
	public String getToAccnumnar() {
		return toAccnumnar;
	}

	public EgovCommon getEgovCommon() {
		return egovCommon;
	}

	public void setToAccnumnar(String toAccnumnar) {
		this.toAccnumnar = toAccnumnar;
	}

	public VoucherService getVoucherService() {
		return voucherService;
	}


}
