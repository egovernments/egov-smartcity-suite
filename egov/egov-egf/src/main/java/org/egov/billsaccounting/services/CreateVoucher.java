/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.billsaccounting.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankreconciliation;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.BankaccountHibernateDAO;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FiscalPeriodHibernateDAO;
import org.egov.commons.dao.FunctionDAO;
import org.egov.commons.dao.FunctionaryHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.dao.FundSourceHibernateDAO;
import org.egov.commons.dao.SchemeHibernateDAO;
import org.egov.commons.dao.SubSchemeHibernateDAO;
import org.egov.commons.dao.VoucherHeaderDAO;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.commons.exception.TooManyValuesException;
import org.egov.commons.service.ChartOfAccountDetailService;
import org.egov.dao.bills.EgBillRegisterHibernateDAO;
import org.egov.egf.autonumber.VouchernumberGenerator;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.AppConfigService;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.voucher.PreApprovedVoucher;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.dao.PersonalInformationDAO;
import org.egov.services.bills.BillsService;
import org.egov.services.voucher.GeneralLedgerDetailService;
import org.egov.services.voucher.GeneralLedgerService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.GLEngine.TransaxtionParameter;
import com.exilant.eGov.src.transactions.VoucherTypeForULB;
import com.exilant.exility.common.TaskFailedException;

/**
 * This Class will create voucher from bill <br>
 * 
 * @author Manikanta created on 15-sep-2008
 *
 */
@Service
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateVoucher {
	private static final String DD_MMM_YYYY = "dd-MMM-yyyy";
	private static final String DD_MM_YYYY = "dd/MM/yyyy";
	private static final String REVERSAL_VOUCHER_DATE = "Reversal voucher date";
	private static final String VOUCHER_HEADER_ID = "Original voucher header id";

	final private static Logger LOGGER = Logger.getLogger(CreateVoucher.class);
	// Expenditure Types
	private final static String CONBILL = "Works";
	private final static String SUPBILL = "Purchase";
	private final static String SALBILL = "Salary";
	private final static String PENSBILL = "Pension";
	private final static String GRATBILL = "Gratuity";
	// messages
	private final static String FUNDMISSINGMSG = "Fund is not used in Bill ,cannot create Voucher";
	private static final String FAILED = "Transaction failed";
	private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving data";
	private final String ISREQUIRED = ".required";
	private final String SELECT = "  Please Select  ";
	@Autowired
	private AppConfigService appConfigService;
	@Autowired
	private VoucherTypeForULB voucherTypeForULB;
	@Autowired
	private AppConfigValueService appConfigValuesService;
	@Autowired
	@Qualifier("persistenceService")
	private PersistenceService persistenceService;
	 
	@Autowired
	private AutonumberServiceBeanResolver beanResolver;

	// add here for other bills

	// bill related common variables for back end updation
	private SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY);
	private SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
	@Autowired
	private BillsService billsService;
	@Autowired
	private FundHibernateDAO fundDAO;

	@Autowired
	private ChartOfAccounts chartOfAccounts;

	@Autowired
	private FunctionaryHibernateDAO functionaryDAO;
	@Autowired
	private FinancialYearDAO financialYearDAO;
	@Autowired
	private SchemeHibernateDAO schemeDAO;
	@Autowired
	private SubSchemeHibernateDAO subSchemeDAO;
	@Autowired
	private FundSourceHibernateDAO fundSourceDAO;
	@Autowired
	private FunctionDAO functionDAO;
	@Autowired
	private ChartOfAccountsHibernateDAO chartOfAccountsDAO;
	@Autowired
	private VoucherHeaderDAO voucherHeaderDAO;
	@Autowired
	private BankaccountHibernateDAO bankAccountDAO;
	@Autowired
	private BankHibernateDAO bankDAO;


	@Autowired
	private EgBillRegisterHibernateDAO egBillRegisterHibernateDAO;

	@Autowired
	@Qualifier("voucherService")
	private VoucherService voucherService; 
 
	@Autowired
	private BoundaryService boundaryService;
	
	 @Autowired
	private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;

	private static final String ERR = "Exception in CreateVoucher";
	private static final String DEPTMISSINGMSG = "Department is missing in the Bill cannot proceed creating vouvher";
	private static final String IS_MISSING = "is missing";
	private static final String NAME = "Reversal voucher name";
	private static final String IS_EMPTY = "is empty";
	private static final String TYPE = "Reversal voucher type";
	private static final String REVERSAL_VOUCHER_NUMBER = "Reversal voucher number";

	@Autowired
	private DepartmentService deptM;
	@Autowired
	private BoundaryService boundary;

	@Autowired
	private UserService userMngr;
	@Autowired
	private EisCommonService eisCommonService;

	@Autowired
	private HierarchyTypeService hierarchyTypeService;
	PersistenceService<Bankreconciliation, Integer> bankReconSer;
	PersistenceService<EgBillregistermis, Integer> billMisSer;
	PersistenceService<EgBilldetails, Integer> billDetailSer;
	PersistenceService<Fund, Integer> fundService;
	@Autowired
	@Qualifier("generalLedgerService")
	private GeneralLedgerService generalLedgerService;
	@Autowired
	@Qualifier("generalLedgerDetailService")
	private GeneralLedgerDetailService generalLedgerDetailService;

	@Autowired
	private PersonalInformationDAO personalInformationDAO;
	@Autowired
	private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;
	@Autowired
	private FiscalPeriodHibernateDAO fiscalPeriodHibernateDAO;
	
	@Autowired
	private ChartOfAccountDetailService chartOfAccountDetailService;

	public CreateVoucher() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Initializing CreateVoucher Service");
		 
	}

	/**
	 * creates voucher From billId
	 * 
	 * @param billId
	 * @param voucherDate
	 *            TODO
	 * @return voucherheaderId long
	 * @throws ApplicationRuntimeException
	 * @throws SQLException
	 * @throws Exception
	 */

    public long createVoucherFromBill(final int billId, String voucherStatus,
            final String voucherNumber, final Date voucherDate)
            throws ApplicationRuntimeException, SQLException,
            TaskFailedException {
        CVoucherHeader vh = null;
        try {
            if (voucherStatus == null) {
                final List vStatusList = appConfigValuesService
                        .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "PREAPPROVEDVOUCHERSTATUS");

                if (!vStatusList.isEmpty() && vStatusList.size() == 1) {
                    final AppConfigValues appVal = (AppConfigValues) vStatusList
                            .get(0);
                    voucherStatus = appVal.getValue();
                } else
                    throw new ApplicationRuntimeException(
                            "PREAPPROVEDVOUCHERSTATUS"
                                    + "is not defined in AppConfig values cannot proceed creating voucher");
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" ---------------Generating Voucher for Bill-------");
            EgBillregister egBillregister = null;
            egBillregister = billsService.getBillRegisterById(Integer
                    .valueOf(billId));
			/*
			 * identify the bill type and delegate get the fund and fundsource
			 * check for mandatory fields for implementation if missing throw
			 * exception department is mandatory for implementation type fund is
			 * mandatory for all implementations
			 */
			final EgBillregistermis billMis = egBillregister
					.getEgBillregistermis();
			// checking voucher already exists or not for this bill
			try {
				CVoucherHeader result;
				if (billMis.getVoucherHeader() != null) {
					result = (CVoucherHeader) voucherService
							.find("select vh from CVoucherHeader vh where vh.id = ? and vh.status!=?",
									billMis.getVoucherHeader().getId(),
									FinancialConstants.CANCELLEDVOUCHERSTATUS);
					if (result != null)
						throw new ApplicationRuntimeException("Voucher "
								+ result.getVoucherNumber()
								+ " already exists for this bill ");
				}
			} catch (final Exception e) {
				throw new ApplicationRuntimeException(e.getMessage());
			}
			final Fund fund = billMis.getFund();
			if (fund == null) {
				LOGGER.error(FUNDMISSINGMSG);
				throw new ApplicationRuntimeException(FUNDMISSINGMSG);
			} else
				fund.getId();
			final String deptMandatory = EGovConfig.getProperty(
					"egf_config.xml", "deptRequired", "", "general");
			if (deptMandatory.equalsIgnoreCase("Y"))
				if (billMis.getEgDepartment() == null)
					throw new ApplicationRuntimeException(DEPTMISSINGMSG);

			final Fundsource fundSrc = billMis.getFundsource();
			if (fundSrc != null)
				Integer.valueOf(fundSrc.getId().toString());

			if (billMis.getScheme() != null)
				billMis.getScheme().getId();
			if (billMis.getSubScheme() != null)
				billMis.getSubScheme().getId();
			final String expType = egBillregister.getExpendituretype();
			String voucherType = null;
			String voucherSubType = null;
			String name="";
			if (expType.equalsIgnoreCase(CONBILL)) {
				name = "Contractor Journal";
				voucherSubType = FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL;
			} else if (expType.equalsIgnoreCase(SUPBILL)) {
				name = "Supplier Journal";
				if (null != billMis.getEgBillSubType()
						&& billMis.getEgBillSubType().getName()
								.equalsIgnoreCase("Fixed Asset"))
					voucherSubType = FinancialConstants.JOURNALVOUCHER_NAME_FIXEDASSETJOURNAL;
				else
					voucherSubType = FinancialConstants.JOURNALVOUCHER_NAME_PURCHASEJOURNAL;
			} else if (expType.equalsIgnoreCase(SALBILL)) {
				name = "Salary Journal";
				voucherSubType = FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL;
			}
			// Pension,Gratuity are saved as Expense Bill
			else if (expType
					.equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT)) {
				name = FinancialConstants.JOURNALVOUCHER_NAME_EXPENSEJOURNAL;
				voucherSubType = FinancialConstants.JOURNALVOUCHER_NAME_EXPENSEJOURNAL;
			} else if (expType.equalsIgnoreCase(PENSBILL)) {
				name = "Pension Journal";
				voucherSubType = FinancialConstants.JOURNALVOUCHER_NAME_PENSIONJOURNAL;
			} else if (expType.equalsIgnoreCase(GRATBILL)) {
				name = "Gratuity Journal";
				voucherSubType = FinancialConstants.CBILL_VOUCHERNO_TYPE;
			} else {
				name = "JVGeneral";
				voucherSubType = FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL;
			}
			voucherType = FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL;
			final HashMap<String, Object> headerDetails = new HashMap<String, Object>();
			HashMap<String, Object> detailMap = null;
			HashMap<String, Object> subledgertDetailMap = null;
			Set<EgBillPayeedetails> subLedgerlist;
			final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
			final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
			final Set<EgBilldetails> billDetailslist = egBillregister
					.getEgBilldetailes();
			detailMap = new HashMap<String, Object>();
			new HashMap<String, Object>();

			headerDetails.put(VoucherConstant.VOUCHERNAME, name);
			headerDetails.put(VoucherConstant.VOUCHERTYPE, voucherType);
			headerDetails.put("vouchersubtype", voucherSubType);
			new SimpleDateFormat(DD_MMM_YYYY);
			headerDetails.put(VoucherConstant.VOUCHERNUMBER,
					voucherNumber == null ? "" : voucherNumber);
			Date dt = new Date();
			Date vdt;
			String purposeValueVN = "";
			final String purposeValue = "";

			/**
			 * Starting to check for voucher date First check if the value needs
			 * to be read from the UI. If YES, check if the value is passed from
			 * the UI, and then set data if present and throw error else. If NO,
			 * check if the value needs to be read from the bill If YES, set the
			 * voucher date same as that of bill date. If NO, set the value as
			 * system date
			 **/
			try {
				final List<AppConfigValues> configValues = appConfigValuesService
						.getConfigValuesByModuleAndKey(
								FinancialConstants.MODULE_NAME_APPCONFIG,
								"VOUCHERDATE_FROM_UI");

				for (final AppConfigValues appConfigVal : configValues)
					purposeValueVN = appConfigVal.getValue();
			} catch (final Exception e) {
				throw new ApplicationRuntimeException(
						"Appconfig value for VOUCHERDATE_FROM_UI is not defined in the system");
			}
			if (purposeValueVN.equals("Y")) {
				if (voucherDate == null)
					throw new ValidationException(
							Arrays.asList(new ValidationError(
									"Voucherdate Should be entered by user",
									"voucherfrombill.voucherdate.mandatory")));
				else {
					dt = voucherDate;
					vdt = dt;
				}

			} else {

				try {
					final List<AppConfigValues> configValues = appConfigValuesService
							.getConfigValuesByModuleAndKey(
									FinancialConstants.MODULE_NAME_APPCONFIG,
									"USE BILLDATE IN CREATE VOUCHER FROM BILL");

					for (final AppConfigValues appConfigVal : configValues)
						purposeValueVN = appConfigVal.getValue();
				} catch (final Exception e) {
					throw new ApplicationRuntimeException(
							"Appconfig value for USE BILLDATE IN CREATE VOUCHER FROM BILL is not defined in the system");
				}
				if (purposeValue.equals("Y")) {
					vdt = egBillregister.getBilldate();
					dt = egBillregister.getBilldate();
				} else
					vdt = dt;
			}
			headerDetails.put(VoucherConstant.VOUCHERDATE, vdt);
			if (egBillregister.getId() != null)
				headerDetails.put("billid", egBillregister.getId());
			if (billMis.getSourcePath() != null)
				headerDetails.put(VoucherConstant.SOURCEPATH,
						billMis.getSourcePath());
			if (billMis.getEgDepartment() != null)
				headerDetails.put(VoucherConstant.DEPARTMENTCODE, billMis
						.getEgDepartment().getCode());
			if (billMis.getFund() != null)
				headerDetails.put(VoucherConstant.FUNDCODE, billMis.getFund()
						.getCode());
			if (billMis.getScheme() != null)
				headerDetails.put(VoucherConstant.SCHEMECODE, billMis
						.getScheme().getCode());
			if (billMis.getSubScheme() != null)
				headerDetails.put(VoucherConstant.SUBSCHEMECODE, billMis
						.getSubScheme().getCode());
			if (billMis.getFundsource() != null)
				headerDetails.put(VoucherConstant.FUNDSOURCECODE, billMis
						.getFundsource().getCode());
			if (billMis.getFieldid() != null)
				if (billMis.getFieldid().getId() != null)
					headerDetails.put(VoucherConstant.DIVISIONID, billMis
							.getFieldid().getId().toString());
			if (billMis.getFunctionaryid() != null)
				headerDetails.put(VoucherConstant.FUNCTIONARYCODE, billMis
						.getFunctionaryid().getCode());
			// TODO- read the fnction from billdetails. We can remove this
			if (billMis.getFunction() != null)
				headerDetails.put(VoucherConstant.FUNCTIONCODE, billMis
						.getFunction().getCode());

			for (final EgBilldetails egBilldetails : billDetailslist) {

				// persistenceService.setSessionFactory(new SessionFactory());
				detailMap = new HashMap<String, Object>();
				if (null != egBilldetails.getFunctionid()) {
					/*
					 * CFunction function = (CFunction)
					 * persistenceService.getSession().load(CFunction.class,
					 * (egBilldetails.getFunctionid()).longValue());
					 * detailMap.put(VoucherConstant.FUNCTIONCODE,
					 * function.getCode());
					 */
				}
				detailMap.put(VoucherConstant.DEBITAMOUNT, egBilldetails
						.getDebitamount() == null ? BigDecimal.ZERO
						: egBilldetails.getDebitamount());
				detailMap.put(VoucherConstant.CREDITAMOUNT, egBilldetails
						.getCreditamount() == null ? BigDecimal.ZERO
						: egBilldetails.getCreditamount());
				final String glcode = persistenceService
						.getSession()
						.createQuery(
								"select glcode from CChartOfAccounts where id = "
										+ egBilldetails.getGlcodeid()
												.longValue()).list().get(0)
						.toString();
				detailMap.put(VoucherConstant.GLCODE, glcode);
				accountdetails.add(detailMap);
				subLedgerlist = egBilldetails.getEgBillPaydetailes();
                                for (final EgBillPayeedetails egBillPayeedetails : subLedgerlist) {
                                    subledgertDetailMap = new HashMap<>();
                                    if (chartOfAccountDetailService.getByGlcodeIdAndDetailTypeId(
                                            egBillPayeedetails.getEgBilldetailsId().getGlcodeid().longValue(),
                                            egBillPayeedetails.getAccountDetailTypeId().intValue()) != null) {
                                        subledgertDetailMap
                                                .put(VoucherConstant.DEBITAMOUNT,
                                                        egBillPayeedetails.getDebitAmount() == null ? BigDecimal.ZERO
                                                                : egBillPayeedetails
                                                                        .getDebitAmount());
                                        subledgertDetailMap
                                                .put(VoucherConstant.CREDITAMOUNT,
                                                        egBillPayeedetails.getCreditAmount() == null ? BigDecimal.ZERO
                                                                : egBillPayeedetails
                                                                        .getCreditAmount());
                                        subledgertDetailMap.put(VoucherConstant.DETAILTYPEID,
                                                egBillPayeedetails.getAccountDetailTypeId());
                                        subledgertDetailMap.put(VoucherConstant.DETAILKEYID,
                                                egBillPayeedetails.getAccountDetailKeyId());
                                        subledgertDetailMap.put(VoucherConstant.GLCODE, glcode);
                                        subledgerDetails.add(subledgertDetailMap);
                                    }
                                }
			}
			vh = createPreApprovedVoucher(headerDetails, accountdetails,
					subledgerDetails);
			egBillregister.getEgBillregistermis().setVoucherHeader(vh);

		} catch (final ValidationException e) {
			LOGGER.error(e.getErrors());
			final List<ValidationError> errors = new ArrayList<ValidationError>();
			errors.add(new ValidationError("exp", e.getErrors().get(0)
					.getMessage()));
			throw new ValidationException(errors);
		} catch (final Exception e) {
			LOGGER.error("Error in create voucher from bill" + e.getMessage());
			throw new ApplicationRuntimeException(e.getMessage());
		}
		return vh.getId().longValue();
	}

	/**
	 * creates voucher From billId
	 * 
	 * @param billId
	 * @return voucherheaderId long
	 * @throws ApplicationRuntimeException
	 * @throws SQLException
	 * @throws Exception
	 */

	public long createVoucherFromBillForPJV(final int billId,
			final String voucherStatus,
			final List<PreApprovedVoucher> voucherdetailList,
			final List<PreApprovedVoucher> subLedgerList)
			throws ApplicationRuntimeException, SQLException,
			TaskFailedException {
		final CVoucherHeader vh = null;
		try {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(" ---------------Generating Voucher-------");
			EgBillregister egBillregister = null;
			egBillregister = billsService.getBillRegisterById(Integer
					.valueOf(billId));
			/*
			 * identify the bill type and delegate get the fund and fundsource
			 * check for mandatory fields for implementation if missing throw
			 * exception department is mandatory for implementation type fund is
			 * mandatory for all implementations
			 */
			String name="";
			final EgBillregistermis billMis = egBillregister
					.getEgBillregistermis();
			final Fund fund = billMis.getFund();
			if (fund == null) {
				LOGGER.error(FUNDMISSINGMSG);
				throw new ApplicationRuntimeException(FUNDMISSINGMSG);
			} else
				fund.getId();
			final String deptMandatory = EGovConfig.getProperty(
					"egf_config.xml", "deptRequired", "", "general");
			if (deptMandatory.equalsIgnoreCase("Y"))
				if (billMis.getEgDepartment() == null)
					throw new ApplicationRuntimeException(DEPTMISSINGMSG);

			final Fundsource fundSrc = billMis.getFundsource();
			if (fundSrc != null)
				Integer.valueOf(fundSrc.getId().toString());

			if (billMis.getScheme() != null)
				billMis.getScheme().getId();
			if (billMis.getSubScheme() != null)
				billMis.getSubScheme().getId();
			final String expType = egBillregister.getExpendituretype();
			if (expType.equalsIgnoreCase(CONBILL)) {
				name = "Contractor Journal";
			} else if (expType.equalsIgnoreCase(SUPBILL)) {
				name = "Supplier Journal";
/*				if (null != billMis.getEgBillSubType()
						&& billMis.getEgBillSubType().getName()
								.equalsIgnoreCase("Fixed Asset")) {
				} else {
				}
*/			} else if (expType.equalsIgnoreCase(SALBILL)) {
				name = "Salary Journal";
			}
			// Pension,Gratuity are saved as contingency Bill
			else if (expType
					.equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT)) {
				name = FinancialConstants.JOURNALVOUCHER_NAME_EXPENSEJOURNAL;
			} else if (expType.equalsIgnoreCase(PENSBILL)) {
				name = "Pension Journal";
			} else if (expType.equalsIgnoreCase(GRATBILL)) {
				name = "Gratuity Journal";
			} else {
				name = "JVGeneral";
			}

			// vh=createVoucherheaderAndPostGLForPJV(fundId,egBillregister,fundSrcId,schemeId,subSchemeId,name,cgnCode,voucherType,voucherdetailList,subLedgerList);
		} catch (final Exception e) {
			LOGGER.error("Error in createVoucherFromBillForPJV "
					+ e.getMessage());
			throw new ApplicationRuntimeException(e.getMessage());
		}
		return vh.getId().longValue();
	}

	/**
	 * @description - This method is used to create the vouchers for the
	 *              preapproved vouchers.
	 * @param vouhcerheaderid
	 *            - the vouhcerheaderid of the preapproved voucher.
	 * @param status
	 *            - status of the vouchers.
	 * @return void - This method does not return anything as its only create
	 *         the vouchers for the preapproved vouchers.s
	 */

	public void createVoucherFromPreApprovedVoucher(final long vouhcerheaderid,
			final String status) throws ApplicationRuntimeException {
		try {
			final CVoucherHeader vh = (CVoucherHeader) voucherHeaderDAO
					.findById(vouhcerheaderid, false);
			vh.setStatus(Integer.valueOf(status));
			voucherHeaderDAO.update(vh);

		} catch (final Exception e) {
			LOGGER.error(e.getMessage());
			throw new ApplicationRuntimeException(e.getMessage());

		}

	}

	/**
	 * This API is to create a voucher from the list of HashMap values that are
	 * passed. This will take care of various types of vouchers like -receipt,
	 * payments and journal vouchers
	 * 
	 * @param headerdetails
	 *            <p>
	 *            HashMap<String, Object> headerdetails will have the data
	 *            required for the voucher header and mis :
	 *            <p>
	 *            vouchername -This will be the name of the voucher, will need
	 *            to set the values from enumeration (mandatory)
	 *            <p>
	 *            vouchertype -This will be the type of the voucher, will need
	 *            to set the values from enumeration (mandatory)
	 *            <p>
	 *            description -This will be the description of the voucher
	 *            (optional)
	 *            <p>
	 *            vouchernumber - This will be the vouchernumber if not set will
	 *            be populated (optional)
	 *            <p>
	 *            voucherdate - This is the date on which the voucher needs to
	 *            be created (mandatory)
	 *            <p>
	 *            fundid - This will be the id from the fund master (mandatory)
	 *            <p>
	 *            moduleid - This will be the id from module master,(not present
	 *            now). Any external system sending data needs to set the value
	 *            for this . If value not set will take as null (optional)
	 *            <p>
	 *            divisionid - This is the fieldid from the boundary master
	 *            (optional)
	 *            <p>
	 *            departmentid - This is the departmentid from the department
	 *            master (optional)
	 *            <p>
	 *            fundsourceid - This is the fundsourceid from the fund source
	 *            master (optional)
	 *            <p>
	 *            schemeid - This is the schemeid from the scheme master
	 *            (optional)
	 *            <p>
	 *            subschemeid - This is the subschemeid from the subscheme
	 *            master (optional)
	 *            <p>
	 *            status - This is the status of voucher . If not set will be
	 *            taken care (optional)
	 *            <p>
	 *            originalvoucher - This is the reference voucherid in care of
	 *            reversal voucher (optional)
	 *            <p>
	 *            refvoucher - This the reference voucherheaderid in case of
	 *            dependency vouchers (optional)
	 *            <p>
	 *            budgetCheckReq -Boolean- This is a flag set in vouchermis and
	 *            decided whether budget check should be done or not . 'true' is
	 *            yes 'false' is no . Default is 'true' (optional).
	 *            <p>
	 * @param accountcodedetails
	 *            <p>
	 *            HashMap<String, Object> accountcodedetails will have data
	 *            required for the ledger details
	 *            <p>
	 *            glcodeid - This the ledger codeid from the chartofaccounts
	 *            master. (mandatory)
	 *            <p>
	 *            debitamount - This is the debit amount for that voucher
	 *            (optional)
	 *            <P>
	 *            creditamount - This is the credit amount for that voucher
	 *            (optional)
	 *            <p>
	 *            functionid - This is the functionid from the function master
	 *            (optional)
	 *            <p>
	 *            narration -This is the narration if any for that account code
	 *            (optional)
	 *            <p>
	 *
	 *            <p>
	 * @param subledgerdetails
	 *            <p>
	 *            HashMap<String, Object> subledgerdetails will have the
	 *            subledger details only for all the control codes in the
	 *            voucher.
	 *            <p>
	 *            glcodeid -This the ledger codeid from the chartofaccounts
	 *            master. (mandatory)
	 *            <p>
	 *            detailtypeid - This is the detailtypeid from the detailtype
	 *            object (mandatory)
	 *            <p>
	 *            detailkeyid - This is the detailkey from the detailkey object
	 *            (mandatory) <detailamount> - This is the amount for that
	 *            detailkey (mandatory) <tdsid> - This is the id from the
	 *            recovery master.If the glcode used is mapped in the recovery
	 *            master then this data is mandatory.
	 * @return voucherheader object in case of success and null in case of fail.
	 * @throws ApplicationRuntimeException
	 */
	@Transactional
	public CVoucherHeader createPreApprovedVoucher(
			final HashMap<String, Object> headerdetails,
			final List<HashMap<String, Object>> accountcodedetails,
			final List<HashMap<String, Object>> subledgerdetails)
			throws ApplicationRuntimeException, ValidationException {
		final AppConfig appConfig = appConfigService
				.getAppConfigByKeyName("PREAPPROVEDVOUCHERSTATUS");
		if (null != appConfig && null != appConfig.getConfValues())
			for (final AppConfigValues appConfigVal : appConfig
					.getConfValues())
				headerdetails.put(VoucherConstant.STATUS,
						Integer.valueOf(appConfigVal.getValue()));
		else
			throw new ApplicationRuntimeException(
					"Appconfig value for PREAPPROVEDVOUCHERSTATUS is not defined in the system");
		CVoucherHeader vh;
		try {
			vh = createVoucher(headerdetails, accountcodedetails,
					subledgerdetails);
			/*if (vh.getModuleId() != null)
				startWorkflow(vh);*/ 
			//if u need workflow enable above lines and fix workflow
		} catch (final ValidationException ve) {
			LOGGER.error(ERR, ve);
			final List<ValidationError> errors = new ArrayList<ValidationError>();
			errors.add(new ValidationError("exp", ve.getErrors().get(0)
					.getMessage()));
			throw new ValidationException(errors);
		} catch (final Exception e) {
			LOGGER.error(ERR, e);
			throw new ApplicationRuntimeException(e.getMessage());
		}
		return vh;
	}

	/**
	 * action name ////// check all junits, EisCommonsManger is added in create
	 * voucher constructor voucher service need to pass
	 * 
	 * @param voucherheader
	 * @throws ValidationException
	 */

	public void startWorkflow(final CVoucherHeader voucherheader)
			throws ValidationException {
		try {
			final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					new String[] {
							"classpath:org/serviceconfig-Bean.xml",
							"classpath:org/egov/infstr/beanfactory/globalApplicationContext.xml",
							"classpath:org/egov/infstr/beanfactory/applicationContext-egf.xml",
							"classpath:org/egov/infstr/beanfactory/applicationContext-pims.xml" });
			if (voucherheader.getType().equals(
					FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL)
					|| voucherheader.getType().equals(
							FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT)) {
				LOGGER.error("Calling StartWorkflow...in create voucher.....for ......ContraJournalVoucher......"
						+ voucherheader.getType()
						+ " ----"
						+ voucherheader.getName());
				final String billtype = egBillRegisterHibernateDAO
						.getBillTypeforVoucher(voucherheader);
				if (billtype == null) {
					applicationContext.getBean("voucherWorkflowService");
					voucherheader.transition().start().withOwner(getPosition());
					// voucherWorkflowService.transition("aa_approve",
					// voucherheader, "Created"); // action name need to pass
					// Position position =
					// eisCommonService.getPositionByUserId(ApplicationThreadLocals.getUserId());

					final VoucherService vs = (VoucherService) applicationContext
							.getBean("voucherService");
					final PersistenceService persistenceService = (PersistenceService) applicationContext
							.getBean("persistenceService");
					final Position nextPosition = getNextPosition(
							voucherheader, vs, persistenceService, null);
					voucherheader.transition().progressWithStateCopy()
							.withStateValue("WORKFLOW INITIATED")
							.withOwner(nextPosition)
							.withComments("WORKFLOW STARTED");
				}
			}
			/*
			 * this logic is moved to top since both have same workflow else
			 * if(voucherheader
			 * .getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT
			 * )) { // ReceiptVoucher rv=new ReceiptVoucher();
			 * PersistenceService<ReceiptVoucher, Long> persistenceService = new
			 * PersistenceService<ReceiptVoucher, Long>();
			 * //persistenceService.setType(ReceiptVoucher.class);
			 * rv.setId(voucherheader.getId());
			 * rv.setVoucherHeader(voucherheader);
			 * persistenceService.create(rv);
			 * SimpleWorkflowService<ReceiptVoucher> receiptWorkflowService =
			 * (SimpleWorkflowService)
			 * applicationContext.getBean("receiptWorkflowService");
			 * receiptWorkflowService.start(rv, getPosition());
			 * receiptWorkflowService.transition("co_approve", rv, "Created");
			 * // action name need to pass }
			 */

		} catch (final Exception e) {
			final List<ValidationError> errors = new ArrayList<ValidationError>();
			LOGGER.error(ERR, e);
			errors.add(new ValidationError(
					"Exp in startWorkflow for JV/Receipt voucher=", e
							.getMessage()));
			throw new ValidationException(errors);
		}
	}

	private Position getNextPosition(final CVoucherHeader voucherheader,
			final VoucherService vs,
			final PersistenceService persistenceService, final Position position) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Getting next Position for WorkFlow");
		final Position nextPosition = null;
		Department department = vs.getTempDepartmentForWfItem(voucherheader,
				position);
		if (department == null) {
			LOGGER.warn("Not able to get tempoaray Assignment defaulting to primary asssigment");
			department = vs.getDepartmentForWfItem(voucherheader);
			if (department == null) {
				final List<ValidationError> errors = new ArrayList<ValidationError>();
				errors.add(new ValidationError("Unable to get Temp Department",
						"Unable to get Temp Department"));
				throw new ValidationException(errors);
			}
		}
		String functionaryName = "";
		String designationName = "";
		designationName = "SECTION MANAGER";
		Boundary boundaryForUser = null;
		if (department.getCode().equalsIgnoreCase("A")) {
			final HierarchyType hierarchyTypeByName = hierarchyTypeService
					.getHierarchyTypeByName("ADMINISTRATION");
			final List topBoundaries = boundaryService
					.getTopLevelBoundaryByHierarchyType(hierarchyTypeByName);
			if (topBoundaries != null && topBoundaries.size() > 0)
				boundaryForUser = (Boundary) topBoundaries.get(0);
			functionaryName = "Compilation";
		} else {
			boundaryForUser = vs.getBoundaryForUser(voucherheader);
			functionaryName = "ZONE";
		}
		final Designation next_desig = (Designation) persistenceService
				.find("from Designation  where upper(name)=upper(?)",
						designationName);
		final Functionary functionary = (Functionary) persistenceService.find(
				"from Functionary where upper(name)=upper(?)", functionaryName);

		try {
			personalInformationDAO.getEmployeeByFunctionary(department.getId(),
					next_desig.getId(), boundaryForUser.getId(),
					functionary.getId());
		} catch (final TooManyValuesException e) {
			LOGGER.error(e.getMessage(), e);
			final List<ValidationError> errors = new ArrayList<ValidationError>();
			errors.add(new ValidationError("Too Many  Employee Exists",
					"Too Many  Employee Exists"));
			throw new ValidationException(errors);

		} catch (final NoSuchObjectException e) {
			LOGGER.error(e.getMessage(), e);
			final List<ValidationError> errors = new ArrayList<ValidationError>();
			errors.add(new ValidationError("No  Employee Exists",
					"No  Employee Exists"));
			throw new ValidationException(errors);
		}
		// TODO: Get employee instead of personalinformation and fix the issue
		// nextPosition=vs.getPositionForEmployee(employeeByFunctionary) ;
		if (nextPosition != null) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Returning next Position for WorkFlow"
						+ nextPosition.getName());
		} else
			LOGGER.error("Could not get next Position for WorkFlow");
		return nextPosition;

	}
/**
 * 
 */
	@Deprecated
	public void startWorkflow(final ContraJournalVoucher cjv)
			throws ValidationException {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Starting Contra Journal Voucher Workflow....startWorkflow(ContraJournalVoucher cjv)...");

		try {
			if (cjv.getVoucherHeaderId().getState() == null) {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Calling StartWorkflow...in create voucher.....for ......ContraJournalVoucher.....................................................................................");
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("fetching voucherWorkflowService from application context.......");
				final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
						new String[] {
								"classpath:org/serviceconfig-Bean.xml",
								"classpath:org/egov/infstr/beanfactory/globalApplicationContext.xml",
								"classpath:org/egov/infstr/beanfactory/applicationContext-egf.xml",
								"classpath:org/egov/infstr/beanfactory/applicationContext-pims.xml" });
				applicationContext.getBean("voucherWorkflowService");
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("completed voucherWorkflowService from application context.......");
				cjv.getVoucherHeaderId().transition().start().withOwner(getPosition());
				// voucherWorkflowService.transition("am_approve",
				// cjv.getVoucherHeaderId(), "Created"); // action name need to
				// pass
				final Position position = eisCommonService
						.getPositionByUserId(ApplicationThreadLocals.getUserId());
				cjv.transition().progressWithStateCopy().withStateValue("WORKFLOW INITIATED")
						.withOwner(position).withComments("WORKFLOW STARTED");
				final VoucherService vs = (VoucherService) applicationContext
						.getBean("voucherService");
				final PersistenceService persistenceService = (PersistenceService) applicationContext
						.getBean("persistenceService");
				final Position nextPosition = getNextPosition(
						cjv.getVoucherHeaderId(), vs, persistenceService, null);
				cjv.transition().progressWithStateCopy().withStateValue("WORKFLOW INITIATED")
						.withOwner(nextPosition)
						.withComments("WORKFLOW STARTED");
			}
		} catch (final Exception e) {
			final List<ValidationError> errors = new ArrayList<ValidationError>();
			LOGGER.error(ERR, e);
			errors.add(new ValidationError("Exp in startWorkflow for Contra=",
					e.getMessage()));
			throw new ValidationException(errors);
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Completed Contra Journal Voucher Workflow.......");
	}

	/**
	 * only for contra workflow
	 * 
	 * @param voucherHeader
	 * @throws ValidationException
	 *             Uses VoucherWorkflow since contra and brv workflows are same
	 */

	public void startWorkflowForCashUpdate(final CVoucherHeader voucherHeader)
			throws ValidationException {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Starting  Journal Voucher Workflow.  for contra......");

		try {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Calling StartWorkflow...For Cash");
			final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					new String[] {
							"classpath:org/serviceconfig-Bean.xml",
							"classpath:org/egov/infstr/beanfactory/globalApplicationContext.xml",
							"classpath:org/egov/infstr/beanfactory/applicationContext-egf.xml",
							"classpath:org/egov/infstr/beanfactory/applicationContext-pims.xml" });
			applicationContext.getBean("voucherWorkflowService");
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("completed voucherWorkflowService from application context.......");
			voucherHeader.transition().start().withOwner(getPosition());
			final VoucherService vs = (VoucherService) applicationContext
					.getBean("voucherService");
			final PersistenceService persistenceService = (PersistenceService) applicationContext
					.getBean("persistenceService");
			final Position nextPosition = getNextPosition(voucherHeader, vs,
					persistenceService, null);
			voucherHeader.transition().progressWithStateCopy().withStateValue("Forwarded")
					.withOwner(nextPosition).withComments("Forwarded");

		} catch (final Exception e) {
			final List<ValidationError> errors = new ArrayList<ValidationError>();
			LOGGER.error(ERR, e);
			errors.add(new ValidationError("Exp in startWorkflow for Contra=",
					e.getMessage()));
			throw new ValidationException(errors);
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Completed Contra Journal Voucher Workflow.......");
	}

	public Position getPosition() throws ApplicationRuntimeException {
		Position pos;
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("getPosition====" + ApplicationThreadLocals.getUserId());
		pos = eisCommonService
				.getPositionByUserId(ApplicationThreadLocals.getUserId());
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("position===" + pos.getId());
		return null;// pos;
	}

	/**
	 * This API is to create a voucher from the list of HashMap values that are
	 * passed. This will take care of various types of vouchers like -receipt,
	 * payments and journal vouchers
	 * 
	 * @param headerdetails
	 *            <p>
	 *            HashMap<String, Object> headerdetails will have the data
	 *            required for the voucher header and mis :
	 *            <p>
	 *            vouchername -This will be the name of the voucher, will need
	 *            to set the values from enumeration (mandatory)
	 *            <p>
	 *            vouchertype -This will be the type of the voucher, will need
	 *            to set the values from enumeration (mandatory)
	 *            <p>
	 *            description -This will be the description of the voucher
	 *            (optional)
	 *            <p>
	 *            vouchernumber - This will be the vouchernumber if not set will
	 *            be populated (optional)
	 *            <p>
	 *            voucherdate - This is the date on which the voucher needs to
	 *            be created (mandatory)
	 *            <p>
	 *            fundid - This will be the id from the fund master (mandatory)
	 *            <p>
	 *            functionid - This is the functionid from the function master
	 *            (optional)
	 *            <p>
	 *            moduleid - This will be the id from module master,(not present
	 *            now). Any external system sending data needs to set the value
	 *            for this . If value not set will take as null (optional)
	 *            <p>
	 *            divisionid - This is the fieldid from the boundary master
	 *            (optional)
	 *            <p>
	 *            departmentid - This is the departmentid from the department
	 *            master (optional)
	 *            <p>
	 *            fundsourceid - This is the fundsourceid from the fund source
	 *            master (optional)
	 *            <p>
	 *            schemeid - This is the schemeid from the scheme master
	 *            (optional)
	 *            <p>
	 *            subschemeid - This is the subschemeid from the subscheme
	 *            master (optional)
	 *            <p>
	 *            status - This is the status of voucher . If not set will be
	 *            taken care (optional)
	 *            <p>
	 *            originalvoucher - This is the reference voucherid in care of
	 *            reversal voucher (optional)
	 *            <p>
	 *            refvoucher - This the reference voucherheaderid in case of
	 *            dependency vouchers (optional)
	 *            <p>
	 *            vouchersubtype - This is the sub voucher type in case of
	 *            Journals where there are various kinds (optional)
	 *            <p>
	 *            budgetCheckReq -Boolean- This is a flag set in vouchermis and
	 *            decided whether budget check should be done or not . 'true' is
	 *            yes 'false' is no . Default is 'true' (optional).
	 *            <p>
	 *            billid-this is the bill number for which voucher is getting
	 *            created . (optional)
	 *            <p>
	 * @param accountcodedetails
	 *            <p>
	 *            HashMap<String, Object> accountcodedetails will have data
	 *            required for the ledger details
	 *            <p>
	 *            glcodeid - This the ledger codeid from the chartofaccounts
	 *            master. (mandatory)
	 *            <p>
	 *            debitamount - This is the debit amount for that voucher
	 *            (optional)
	 *            <P>
	 *            creditamount - This is the credit amount for that voucher
	 *            (optional)
	 *            <p>
	 *            functionid - This is the functionid from the function master
	 *            (optional)
	 *            <p>
	 *            narration -This is the narration if any for that account code
	 *            (optional)
	 *            <p>
	 *
	 *            <p>
	 * @param subledgerdetails
	 *            <p>
	 *            HashMap<String, Object> subledgerdetails will have the
	 *            subledger details only for all the control codes in the
	 *            voucher.
	 *            <p>
	 *            glcodeid -This the ledger codeid from the chartofaccounts
	 *            master. (mandatory)
	 *            <p>
	 *            detailtypeid - This is the detailtypeid from the detailtype
	 *            object (mandatory)
	 *            <p>
	 *            detailkeyid - This is the detailkey from the detailkey object
	 *            (mandatory) <detailamount> - This is the amount for that
	 *            detailkey (mandatory) <tdsid> - This is the id from the
	 *            recovery master.If the glcode used is mapped in the recovery
	 *            master then this data is mandatory.
	 * @return voucherheader object in case of success and null in case of fail.
	 * @throws ApplicationRuntimeException
	 */
	@Transactional
	public CVoucherHeader createVoucher(
			final HashMap<String, Object> headerdetails,
			final List<HashMap<String, Object>> accountcodedetails,
			final List<HashMap<String, Object>> subledgerdetails)
			throws ApplicationRuntimeException {
		CVoucherHeader vh;
		Vouchermis mis;

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("start | createVoucher API");
		try {
			validateMandateFields(headerdetails);
			validateLength(headerdetails);
			validateVoucherMIS(headerdetails);
			validateTransaction(accountcodedetails, subledgerdetails);
			validateFunction(headerdetails, accountcodedetails);
			vh = createVoucherHeader(headerdetails);
			mis = createVouchermis(headerdetails);
			mis.setVoucherheaderid(vh);
			vh.setVouchermis(mis);
			// insertIntoVoucherHeader(vh);

			if (LOGGER.isDebugEnabled())
				LOGGER.debug("start | insertIntoVoucherHeader");
			final String vdt = formatter.format(vh.getVoucherDate());
			String fiscalPeriod = null;
			try {
				fiscalPeriod = getFiscalPeriod(vdt);
			} catch (final TaskFailedException e) {
				throw new ApplicationRuntimeException(
						"error while getting fiscal period");
			}
			if (null == fiscalPeriod)
				throw new ApplicationRuntimeException(
						"Voucher Date not within an open period or Financial year not open for posting, fiscalPeriod := "
								+ fiscalPeriod);
			vh.setFiscalPeriodId(Integer.valueOf(fiscalPeriod));
			
			vh.setCgvn(getCGVNNumber(vh));

			try {
				if (!isUniqueVN(vh.getVoucherNumber(), vdt))
					throw new ValidationException(
							Arrays.asList(new ValidationError(
									"Duplicate Voucher Number",
									"Duplicate Voucher Number")));
			} catch (final Exception e) {
				LOGGER.error(ERR, e);
				throw new ApplicationRuntimeException(e.getMessage());
			}
			voucherService.applyAuditing(vh);
			if (LOGGER.isInfoEnabled())
				LOGGER.info("++++++++++++++++++" + vh.toString());
			voucherService.persist(vh);
			if (null != vh.getVouchermis().getSourcePath()
					&& null == vh.getModuleId()
					&& vh.getVouchermis().getSourcePath().length() == vh
							.getVouchermis().getSourcePath().indexOf("=") + 1) {
				final StringBuffer sourcePath = new StringBuffer();
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Source Path received : "
							+ vh.getVouchermis().getSourcePath());
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Voucher Header Id  : " + vh.getId());
				sourcePath.append(vh.getVouchermis().getSourcePath()).append(
						vh.getId().toString());
				vh.getVouchermis().setSourcePath(sourcePath.toString());
				voucherService.applyAuditing(vh);
				voucherService.update(vh);
			}

			if (LOGGER.isDebugEnabled())
				LOGGER.debug("End | insertIntoVoucherHeader");

			// insertIntoRecordStatus(vh);
			final List<Transaxtion> transactions = createTransaction(
					headerdetails, accountcodedetails, subledgerdetails, vh);
			//persistenceService.getSession().flush();
			// engine = ChartOfAccounts.getInstance();
			// setChartOfAccounts();
			Transaxtion txnList[] = new Transaxtion[transactions.size()];
			txnList = transactions.toArray(txnList);
			final SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
			if (!chartOfAccounts.postTransaxtions(txnList,
					formatter.format(vh.getVoucherDate())))
				throw new ApplicationRuntimeException("Voucher creation Failed");
		}

		catch (final ValidationException ve) {
			final List<ValidationError> errors = new ArrayList<ValidationError>();
			errors.add(new ValidationError("exp", ve.getErrors().get(0)
					.getMessage()));
			throw new ValidationException(errors);
		} catch (final Exception e) {
			LOGGER.error(ERR, e);
			throw new ApplicationRuntimeException(e.getMessage());
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("End | createVoucher API");
		return vh;

	}

	/**
	 *
	 * @param headerdetails
	 * @param accountcodedetails
	 *            Validates based on Single Function for a transaction
	 *
	 */
	private void validateFunction(final HashMap<String, Object> headerdetails,
			final List<HashMap<String, Object>> accountcodedetails) {

		final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "ifRestrictedToOneFunctionCenter");
		if (appConfigValues == null) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("app config ifRestrictedToOneFunctionCenter is not defined");
			throw new ValidationException("Error",
					"Use Single Function For a transaction is not defined");
		} else if (appConfigValues.get(0).getValue().equalsIgnoreCase("No")) {

			// Keep last two lines when making single function mandatory.
			// Now this will support both.
			// If function is added in header it will consider it for all
			// details else
			// it will use from detail.
			boolean foundInHeader = false;
			final Map functionMap = new HashMap<String, String>();// to find
			// duplicates.
			// finally
			// we should
			// have one
			// and only
			// entry
			// in
			// this
			String functionCodeInHeader = "";
			// 1. check if function is passed in header
			if (null != headerdetails.get(VoucherConstant.FUNCTIONCODE)) {

				functionCodeInHeader = (String) headerdetails
						.get(VoucherConstant.FUNCTIONCODE);
				functionMap.put(functionCodeInHeader, functionCodeInHeader);
				foundInHeader = true;
			}
			if (foundInHeader)
				for (final HashMap<String, Object> accDetailMap : accountcodedetails)
					accDetailMap.put(VoucherConstant.FUNCTIONCODE,
							headerdetails.get(VoucherConstant.FUNCTIONCODE));

			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Not a single Function Transaction  No need of Further check on function");
			return;
		} else if (appConfigValues.get(0).getValue().equalsIgnoreCase("Yes")) {
			boolean foundInHeader = false;
			boolean atLeastOneMissing = false;
			final Map functionMap = new HashMap<String, String>();// to find
			// duplicates.
			// finally
			// we should
			// have one
			// and only
			// entry
			// in
			// this
			String functionCodeInHeader = "";
			String functionCodeInDetail = "";

			// 1. check if function is passed in header
			if (null != headerdetails.get(VoucherConstant.FUNCTIONCODE)) {

				functionCodeInHeader = (String) headerdetails
						.get(VoucherConstant.FUNCTIONCODE);
				functionMap.put(functionCodeInHeader, functionCodeInHeader);
				foundInHeader = true;
			}

			// 2. Check if function is passed in Details
			for (final HashMap<String, Object> accDetailMap : accountcodedetails)
				if (null != accDetailMap.get(VoucherConstant.FUNCTIONCODE)
						&& "" != accDetailMap.get(VoucherConstant.FUNCTIONCODE)) {
					functionCodeInDetail = accDetailMap.get(
							VoucherConstant.FUNCTIONCODE).toString();
					functionMap.put(functionCodeInDetail, functionCodeInDetail);
				} else
					// Mark if Function is missing in detail level
					atLeastOneMissing = true;
			// If no function is passed or more than one passed throw error
			if (functionMap.size() > 1) {
				LOGGER.error("multiple functions found in Header and details Cannot Continue Transaction....");
				throw new ValidationException(
						Arrays.asList(new ValidationError(
								"multiple functions found in Header and details",
								"multiple.functions.found.in.Header.and.details")));
			}
			// commented by Shamili - functionMap.isEmpty() needs to be
			// validated when function is mandatory
			// Uncommenting since one function center is mandatory
			if (functionMap.isEmpty()) {
				LOGGER.error("Function not found in Header or details Cannot Continue Transaction....");
				throw new ValidationException(
						Arrays.asList(new ValidationError(
								"function not found in Header or details",
								"function.not.found.in.Header.or.details")));
			}
			// since we passed the above check, there is one function.If it is
			// not in header then it must be from detail
			if (!foundInHeader)
				headerdetails.put(VoucherConstant.FUNCTIONCODE,
						functionCodeInDetail);
			// Now fetch it from the header which we have populated
			if (atLeastOneMissing)
				for (final HashMap<String, Object> accDetailMap : accountcodedetails)
					if (null == accDetailMap.get(VoucherConstant.FUNCTIONCODE)
							|| "" == accDetailMap
									.get(VoucherConstant.FUNCTIONCODE))
						accDetailMap
								.put(VoucherConstant.FUNCTIONCODE,
										headerdetails
												.get(VoucherConstant.FUNCTIONCODE));
		}

	}

	/**
	 * @param headerdetails
	 */
	private void validateLength(final HashMap<String, Object> headerdetails) {
		if (headerdetails.get(VoucherConstant.DESCRIPTION) != null
				&& headerdetails.get(VoucherConstant.DESCRIPTION).toString()
						.length() > 250)
			throw new ValidationException(Arrays.asList(new ValidationError(
					"voucher.description.exceeds.max.length",
					"Narration exceeds maximum length")));
		final String vNumGenMode = voucherTypeForULB
				.readVoucherTypes(headerdetails
						.get(VoucherConstant.VOUCHERTYPE).toString());
		if (!vNumGenMode.equals("Auto")
				&& headerdetails.get(VoucherConstant.VOUCHERNUMBER) != null) {
			final int typeLength = Integer
					.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH);
			final int voucherNumberColumnLength = 30;// length in the db
			final int fundIdentfierLength = 1;
			if (headerdetails.get(VoucherConstant.VOUCHERNUMBER).toString()
					.length() > voucherNumberColumnLength
					- (typeLength + fundIdentfierLength)) {
				final String voucheNumberErrMsg = " VoucherNumber length should be lessthan "
						+ (voucherNumberColumnLength - (typeLength + fundIdentfierLength));
				throw new ValidationException(
						Arrays.asList(new ValidationError(
								"voucher.number.exceeds.max.length",
								voucheNumberErrMsg)));
			}
		}

	}

	// used for reversal

	protected void insertIntoVoucherHeader(final CVoucherHeader vh)
			throws ApplicationRuntimeException {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("start | insertIntoVoucherHeader");
		final String vdt = formatter.format(vh.getVoucherDate());
		String fiscalPeriod = null;
		try {
			CFiscalPeriod fis = fiscalPeriodHibernateDAO
					.getFiscalPeriodByDate(vh.getVoucherDate());
			if (fis != null)
				fiscalPeriod = fis.getId().toString();

		} catch (final Exception e) {
			throw new ApplicationRuntimeException(
					"error while getting fiscal period");
		}
		if (null == fiscalPeriod)
			throw new ApplicationRuntimeException(
					"Voucher Date not within an open period or Financial year not open for posting, fiscalPeriod := "
							+ fiscalPeriod);
		vh.setFiscalPeriodId(Integer.valueOf(fiscalPeriod));
		
		vh.setCgvn(getCGVNNumber(vh));

		try {
			if (!isUniqueVN(vh.getVoucherNumber(), vdt))
				throw new ValidationException(
						Arrays.asList(new ValidationError(
								"Duplicate Voucher Number",
								"Duplicate Voucher Number")));
		} catch (final Exception e) {
			LOGGER.error(ERR, e);
			throw new ApplicationRuntimeException(e.getMessage());
		}
		vh.setCreatedBy(userMngr.getUserById(Long.valueOf(ApplicationThreadLocals
				.getUserId())));
		if (LOGGER.isInfoEnabled())
			LOGGER.info("++++++++++++++++++" + vh.toString());
		voucherService.persist(vh);
		if (null != vh.getVouchermis().getSourcePath()
				&& null == vh.getModuleId()
				&& vh.getVouchermis().getSourcePath().length() == vh
						.getVouchermis().getSourcePath().indexOf("=") + 1) {
			final StringBuffer sourcePath = new StringBuffer();
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Source Path received : "
						+ vh.getVouchermis().getSourcePath());
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Voucher Header Id  : " + vh.getId());
			sourcePath.append(vh.getVouchermis().getSourcePath()).append(
					vh.getId().toString());
			vh.getVouchermis().setSourcePath(sourcePath.toString());
			voucherService.update(vh);
		}

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("End | insertIntoVoucherHeader");
	}

	protected String getCgnType(String vouType) {
		String vType = vouType.toUpperCase().replaceAll(" ", "");
		String cgnType = null;
		String typetoCheck = vType;
		if (vType.equalsIgnoreCase("JOURNAL VOUCHER"))
			typetoCheck = "JOURNALVOUCHER";

		switch (voucherTypeEnum.valueOf(typetoCheck.toUpperCase())) {
		case JOURNALVOUCHER:
			cgnType = "JVG";
			break;
		case CONTRA:
			cgnType = "CSL";
			break;
		case RECEIPT:
			cgnType = "MSR";
			break;
		case PAYMENT:
			cgnType = "DBP";
			break;
		default://do nothing
		        break;
		}
		return cgnType;
	}

	/**
	 * This method will validate all the master data that are passed. This will
	 * also check if the data send are correct with respect to the inter master
	 * dependency.
	 * 
	 * @param headerdetails
	 * @throws ApplicationRuntimeException
	 */
	public void validateVoucherMIS(final HashMap<String, Object> headerdetails)
			throws ApplicationRuntimeException {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("START | validateVoucherMIS");
		// Validate Department.
		Department dept = null;
		if (headerdetails.containsKey(VoucherConstant.DEPARTMENTCODE)
				&& null != headerdetails.get(VoucherConstant.DEPARTMENTCODE)) {
			dept = deptM.getDepartmentByCode(headerdetails.get(
					VoucherConstant.DEPARTMENTCODE).toString());
			if (dept == null)
				throw new ApplicationRuntimeException("not a valid Department");
		}

		if (null != headerdetails.get(VoucherConstant.FUNCTIONARYCODE)) {
			final Functionary functionary = functionaryDAO
					.getFunctionaryByCode(BigDecimal.valueOf(Long
							.valueOf(headerdetails.get(
									VoucherConstant.FUNCTIONARYCODE).toString())));
			if (null == functionary)
				throw new ApplicationRuntimeException("not a valid functionary");
		}
		// validate fund.
		String fundCode = null;
		Fund fund = null;
		if (headerdetails.containsKey(VoucherConstant.FUNDCODE)
				&& null != headerdetails.get(VoucherConstant.FUNDCODE)) {
			fundCode = headerdetails.get(VoucherConstant.FUNDCODE).toString();
			fund = fundDAO.fundByCode(fundCode);
			if (null == fund)
				throw new ApplicationRuntimeException("not a valid fund");
		} else
			throw new ApplicationRuntimeException("fund value is missing");
		// validate Scheme
		Scheme scheme = null;
		if (headerdetails.containsKey(VoucherConstant.SCHEMECODE)
				&& null != headerdetails.get(VoucherConstant.SCHEMECODE)) {
			final String schemecode = headerdetails.get(
					VoucherConstant.SCHEMECODE).toString();
			scheme = schemeDAO.getSchemeByCode(schemecode);
			if (null == scheme)
				throw new ApplicationRuntimeException("not a valid scheme");
			if (!fund.getId().equals(scheme.getFund().getId()))
				throw new ApplicationRuntimeException(
						"This scheme does not belong to this fund");
		}
		// validate subscheme
		SubScheme subScheme = null;
		if (headerdetails.containsKey(VoucherConstant.SUBSCHEMECODE)
				&& null != headerdetails.get(VoucherConstant.SUBSCHEMECODE)) {
			final String subSchemeCode = headerdetails.get(
					VoucherConstant.SUBSCHEMECODE).toString();
			subScheme = subSchemeDAO.getSubSchemeByCode(subSchemeCode);
			if (null == subScheme)
				throw new ApplicationRuntimeException("not a valid subscheme");
			if (!subScheme.getScheme().getId().equals(scheme.getId()))
				throw new ApplicationRuntimeException(
						"This subscheme does not belong to this scheme");
		}
		// validate fundsource
		if (headerdetails.containsKey(VoucherConstant.FUNDSOURCECODE)
				&& null != headerdetails.get(VoucherConstant.FUNDSOURCECODE)) {
			final Fundsource fundsource = fundSourceDAO
					.getFundSourceByCode(headerdetails.get(
							VoucherConstant.FUNDSOURCECODE).toString());
			if (null == fundsource)
				throw new ApplicationRuntimeException("not a valid fund source");
		}

		if (headerdetails.containsKey(VoucherConstant.DIVISIONID)
				&& null != headerdetails.get(VoucherConstant.DIVISIONID))
			if (null == boundary.getBoundaryById(Long.parseLong(headerdetails
					.get(VoucherConstant.DIVISIONID).toString())))
				throw new ApplicationRuntimeException("not a valid divisionid");
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("END | validateVoucherMIS");
	}

	public void validateMandateFields(
			final HashMap<String, Object> headerdetails) {

		List<String> headerMandateFields = getHeaderMandateFields();
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Inside Validate Method");
		checkMandatoryField("vouchernumber",
				headerdetails.get(VoucherConstant.VOUCHERNUMBER), headerdetails,headerMandateFields);
		checkMandatoryField("voucherdate",
				headerdetails.get(VoucherConstant.VOUCHERDATE), headerdetails,headerMandateFields);
		checkMandatoryField("fund",
				headerdetails.get(VoucherConstant.FUNDCODE), headerdetails,headerMandateFields);
		checkMandatoryField("department",
				headerdetails.get(VoucherConstant.DEPARTMENTCODE),
				headerdetails,headerMandateFields);
		checkMandatoryField("scheme",
				headerdetails.get(VoucherConstant.SCHEMECODE), headerdetails,headerMandateFields);
		checkMandatoryField("subscheme",
				headerdetails.get(VoucherConstant.SUBSCHEMECODE), headerdetails,headerMandateFields);
		checkMandatoryField("functionary",
				headerdetails.get(VoucherConstant.FUNCTIONARYCODE),
				headerdetails,headerMandateFields);
		// checkMandatoryField("function",headerdetails.get(VoucherConstant.FUNCTIONCODE),headerdetails);
		checkMandatoryField("fundsource",
				headerdetails.get(VoucherConstant.FUNDSOURCECODE),
				headerdetails,headerMandateFields);
		checkMandatoryField("field",
				headerdetails.get(VoucherConstant.DIVISIONID), headerdetails,headerMandateFields);

	}

	private void validateVoucherType(String vouType) {
		String voucherType = vouType.toUpperCase().replaceAll(" ", "");
		boolean typeFound = false;
		final voucherTypeEnum[] allvoucherTypeEnum = voucherTypeEnum.values();
		for (final voucherTypeEnum voucherTypeEnum : allvoucherTypeEnum)
			if (voucherTypeEnum.toString().equalsIgnoreCase(voucherType)) {
				typeFound = true;
				break;
			}
		if (!typeFound)
			throw new ApplicationRuntimeException("Voucher type is not valid");
	}

	@Transactional
	@SuppressWarnings("deprecation")
	public CVoucherHeader createVoucherHeader(
			final HashMap<String, Object> headerdetails)
			throws ApplicationRuntimeException, Exception {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("START | createVoucherHeader");
		// Connection con = null;
		Query query = null;
		final CVoucherHeader cVoucherHeader = new CVoucherHeader();
		try {
			// String voucherSubType="";
			cVoucherHeader.setName(headerdetails.get(
					VoucherConstant.VOUCHERNAME).toString());
			String voucherType = headerdetails.get(VoucherConstant.VOUCHERTYPE)
					.toString();
			cVoucherHeader.setType(headerdetails.get(
					VoucherConstant.VOUCHERTYPE).toString());
			String vNumGenMode = null;

			// -- Voucher Type checking. --START
			if (FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL
					.equalsIgnoreCase(voucherType))
				vNumGenMode = voucherTypeForULB.readVoucherTypes("Journal");
			else
				vNumGenMode = voucherTypeForULB.readVoucherTypes(voucherType);
			// --END --
			voucherType = voucherType.toUpperCase().replaceAll(" ", "");

			String voucherSubType = null;
			if (headerdetails.get(VoucherConstant.VOUCHERSUBTYPE) != null) {
				voucherSubType = (String) headerdetails
						.get(VoucherConstant.VOUCHERSUBTYPE);
				voucherSubType = voucherSubType.toUpperCase().replaceAll(" ",
						"");
			}

			// why it is type,subtype where api expects subtype,type ?
			// if()
			final String voucherNumberPrefix = getVoucherNumberPrefix(
					voucherType, voucherSubType);
			String voucherNumber = null;
			if (headerdetails.get(VoucherConstant.DESCRIPTION) != null)
				cVoucherHeader.setDescription(headerdetails.get(
						VoucherConstant.DESCRIPTION).toString());
			final Date voucherDate = (Date) headerdetails
					.get(VoucherConstant.VOUCHERDATE);
			cVoucherHeader.setVoucherDate(voucherDate);
		Fund	fundByCode = fundDAO.fundByCode(headerdetails.get(
					VoucherConstant.FUNDCODE).toString());

			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Voucher Type is :" + voucherType);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("vNumGenMode is  :" + vNumGenMode);

			if (headerdetails.get(VoucherConstant.VOUCHERNUMBER) != null)
				voucherNumber = headerdetails
						.get(VoucherConstant.VOUCHERNUMBER).toString();
			if (null != headerdetails.get(VoucherConstant.MODULEID))
				vNumGenMode = "Auto";
			
			cVoucherHeader.setFundId(fundByCode); 
			if (vNumGenMode.equals("Auto")) {
				cVoucherHeader.setVoucherNumberPrefix(voucherNumberPrefix);
				VouchernumberGenerator v = beanResolver.getAutoNumberServiceFor(VouchernumberGenerator.class);

				final String strVoucherNumber = v.getNextNumber(cVoucherHeader);

				cVoucherHeader.setVoucherNumber(strVoucherNumber);
			}
			/*
			 * if("Auto".equalsIgnoreCase(vNumGenMode) || null !=
			 * headerdetails.get(VoucherConstant.MODULEID)){
			 * if(LOGGER.isDebugEnabled())
			 * LOGGER.debug("Generating auto voucher number"); SimpleDateFormat
			 * df = new SimpleDateFormat(DD_MM_YYYY); String vDate =
			 * df.format(voucherDate);
			 * cVoucherHeader.setVoucherNumber(cmImpl.getTxnNumber
			 * (fundId.toString(),voucherNumberPrefix,vDate,con)); }else {
			 * voucherNumber =
			 * headerdetails.get(VoucherConstant.VOUCHERNUMBER).toString();
			 * query=persistenceService.getSession().createQuery(
			 * "select f.identifier from Fund f where id=:fundId");
			 * query.setInteger("fundId", fundId); String fundIdentifier =
			 * query.uniqueResult().toString();
			 * cVoucherHeader.setVoucherNumber(new
			 * StringBuffer().append(fundIdentifier
			 * ).append(voucherNumberPrefix). append(voucherNumber).toString());
			 * }
			 */

		 
			if (headerdetails.containsKey(VoucherConstant.MODULEID)
					&& null != headerdetails.get(VoucherConstant.MODULEID)) {
				cVoucherHeader.setModuleId(Integer.valueOf(headerdetails.get(
						VoucherConstant.MODULEID).toString()));
				cVoucherHeader.setIsConfirmed(Integer.valueOf(1));
			} else {
				// Fix Me
				/*
				 * PersistenceService<AppConfig, Integer> appConfigSer;
				 * appConfigSer = new PersistenceService<AppConfig, Integer>();
				 * appConfigSer.setSessionFactory(new SessionFactory());
				 * appConfigSer.setType(AppConfig.class); AppConfig appConfig=
				 * (AppConfig)
				 * appConfigSer.find("from AppConfig where key_name =?",
				 * "JournalVoucher_ConfirmonCreate"); if(null != appConfig &&
				 * null!= appConfig.getValues() ){ for (AppConfigValues
				 * appConfigVal : appConfig.getValues()) {
				 * cVoucherHeader.
				 * setIsConfirmed(Integer.valueOf(appConfigVal.getValue())); } }
				 */
			}

			if (headerdetails.containsKey(VoucherConstant.STATUS)
					&& null != headerdetails.get(VoucherConstant.STATUS))
				cVoucherHeader.setStatus(Integer.valueOf(headerdetails.get(
						VoucherConstant.STATUS).toString()));
			else {
				final List list = appConfigValuesService
						.getConfigValuesByModuleAndKey("EGF",
								"DEFAULTVOUCHERCREATIONSTATUS");
				cVoucherHeader.setStatus(Integer
						.parseInt(((AppConfigValues) list.get(0)).getValue()));
			}

			if (null != headerdetails.get(VoucherConstant.ORIGIONALVOUCHER)) {

				final Long origionalVId = Long.parseLong(headerdetails.get(
						VoucherConstant.ORIGIONALVOUCHER).toString());
				query = persistenceService.getSession().createQuery(
						"from CVoucherHeader where id=:id");
				query.setLong("id", origionalVId);
				if (query.list().size() == 0)
					throw new ApplicationRuntimeException(
							"Not a valid origional voucherheader id");
				else
					cVoucherHeader.setOriginalvcId(origionalVId);
			}

			cVoucherHeader.setRefvhId((Long) headerdetails
					.get(VoucherConstant.REFVOUCHER));
			cVoucherHeader.setEffectiveDate(new Date());
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Printing Voucher Details------------------------------------------------------------------------------");
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(cVoucherHeader.toString());
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Printing Voucher Details------------------------------------------------------------------------------");
		} catch (final ValidationException e) {
			LOGGER.error(e.getMessage());
			throw e;
		} catch (final Exception e) {
			LOGGER.error(e);
			throw new Exception(e.getMessage());

		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("END | createVoucherHeader");
		return cVoucherHeader;
	}

	enum voucherTypeEnum {
		JOURNALVOUCHER, CONTRA, RECEIPT, PAYMENT;
	}

	enum voucherSubTypeEnum {
		JOURNALVOUCHER, CONTRA, RECEIPT, PAYMENT, PURCHASEJOURNAL, PENSIONJOURNAL, PURCHASE, WORKS, CONTRACTORJOURNAL, FIXEDASSETJOURNAL, FIXEDASSET, PENSION, WORKSJOURNAL, CONTINGENTJOURNAL, SALARY, SALARYJOURNAL, EXPENSE, EXPENSEJOURNAL, JVGENERAL;
	}

	// we cannot provide enum for all names so we need to find a way
	// or code it for all standard type like CJV,SJV,PJV,EJV

	private String getVoucherNumberPrefix(final String type, String vsubtype) {

		// if sub type is null use type
		if (vsubtype == null)
			vsubtype = type;
		String subtype = vsubtype.toUpperCase().trim();
		String voucherNumberPrefix = null;
		String typetoCheck = subtype;

		if (subtype.equalsIgnoreCase("JOURNAL VOUCHER"))
			typetoCheck = "JOURNALVOUCHER";

		switch (voucherSubTypeEnum.valueOf(typetoCheck)) {
		case JVGENERAL:
			voucherNumberPrefix = FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
			break;
		case JOURNALVOUCHER:
			voucherNumberPrefix = FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
			break;
		case CONTRA:
			voucherNumberPrefix = FinancialConstants.CONTRA_VOUCHERNO_TYPE;
			break;
		case RECEIPT:
			voucherNumberPrefix = FinancialConstants.RECEIPT_VOUCHERNO_TYPE;
			break;
		case PAYMENT:
			voucherNumberPrefix = FinancialConstants.PAYMENT_VOUCHERNO_TYPE;
			break;
		case PURCHASEJOURNAL:
			voucherNumberPrefix = FinancialConstants.PURCHBILL_VOUCHERNO_TYPE;
			break;
		case WORKS:
			voucherNumberPrefix = FinancialConstants.WORKSBILL_VOUCHERNO_TYPE;
			break;
		case CONTRACTORJOURNAL:
			voucherNumberPrefix = FinancialConstants.WORKSBILL_VOUCHERNO_TYPE;
			break;
		case WORKSJOURNAL:
			voucherNumberPrefix = FinancialConstants.WORKSBILL_VOUCHERNO_TYPE;
			break;
		case FIXEDASSETJOURNAL:
			voucherNumberPrefix = FinancialConstants.FIXEDASSET_VOUCHERNO_TYPE;
			break;
		case CONTINGENTJOURNAL:
			voucherNumberPrefix = FinancialConstants.CBILL_VOUCHERNO_TYPE;
			break;
		case PURCHASE:
			voucherNumberPrefix = FinancialConstants.PURCHBILL_VOUCHERNO_TYPE;
			break;
		case EXPENSEJOURNAL:
			voucherNumberPrefix = FinancialConstants.CBILL_VOUCHERNO_TYPE;
			break;
		case EXPENSE:
			voucherNumberPrefix = FinancialConstants.CBILL_VOUCHERNO_TYPE;
			break;
		case SALARYJOURNAL:
			voucherNumberPrefix = FinancialConstants.SALBILL_VOUCHERNO_TYPE;
			break;
		case SALARY:
			voucherNumberPrefix = FinancialConstants.SALBILL_VOUCHERNO_TYPE;
			break;
		case FIXEDASSET:
			voucherNumberPrefix = FinancialConstants.FIXEDASSET_VOUCHERNO_TYPE;
			break;
		case PENSIONJOURNAL:
			voucherNumberPrefix = FinancialConstants.PENBILL_VOUCHERNO_TYPE;
			break;
		case PENSION:
			voucherNumberPrefix = FinancialConstants.PENBILL_VOUCHERNO_TYPE;
			break;
		default: // if subtype is invalid then use type
			if (voucherNumberPrefix == null)
				voucherNumberPrefix = checkwithvouchertype(type);
			break;
		}
		return voucherNumberPrefix;

	}

	/**
	 *
	 */
	private String checkwithvouchertype(final String type) {
		String typetoCheck = type;
		if (type.equalsIgnoreCase("JOURNAL VOUCHER"))
			typetoCheck = "JOURNALVOUCHER";
		String voucherNumberPrefix = null;
		switch (voucherTypeEnum.valueOf(typetoCheck)) {
		case JOURNALVOUCHER:
			voucherNumberPrefix = FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
			break;
		case CONTRA:
			voucherNumberPrefix = FinancialConstants.CONTRA_VOUCHERNO_TYPE;
			break;
		case RECEIPT:
			voucherNumberPrefix = FinancialConstants.RECEIPT_VOUCHERNO_TYPE;
			break;
		case PAYMENT:
			voucherNumberPrefix = FinancialConstants.PAYMENT_VOUCHERNO_TYPE;
			break;
		default://do nothing
		        break;
		}
		return voucherNumberPrefix;

	}

	@Transactional
	public Vouchermis createVouchermis(
			final HashMap<String, Object> headerdetails)
			throws ApplicationRuntimeException {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("START | createVouchermis");
		final Vouchermis vouchermis = new Vouchermis();
		if (headerdetails.containsKey(VoucherConstant.DEPARTMENTCODE)
				&& null != headerdetails.get(VoucherConstant.DEPARTMENTCODE)) {
			final String departmentCode = headerdetails.get(
					VoucherConstant.DEPARTMENTCODE).toString();
			vouchermis.setDepartmentid(deptM
					.getDepartmentByCode(departmentCode));
		}
		if (headerdetails.containsKey(VoucherConstant.SCHEMECODE)
				&& null != headerdetails.get(VoucherConstant.SCHEMECODE)) {
			final String schemecode = headerdetails.get(
					VoucherConstant.SCHEMECODE).toString();
			vouchermis.setSchemeid(schemeDAO.getSchemeByCode(schemecode));
		}
		if (headerdetails.containsKey(VoucherConstant.SUBSCHEMECODE)
				&& null != headerdetails.get(VoucherConstant.SUBSCHEMECODE)) {
			final String subschemecode = headerdetails.get(
					VoucherConstant.SUBSCHEMECODE).toString();
			vouchermis.setSubschemeid(subSchemeDAO
					.getSubSchemeByCode(subschemecode));
		}

		if (headerdetails.containsKey(VoucherConstant.FUNDSOURCECODE)
				&& null != headerdetails.get(VoucherConstant.FUNDSOURCECODE)) {
			final String fundsourcecode = headerdetails.get(
					VoucherConstant.FUNDSOURCECODE).toString();
			vouchermis.setFundsource(fundSourceDAO
					.getFundSourceByCode(fundsourcecode));
		}
		if (null != headerdetails.get(VoucherConstant.FUNCTIONARYCODE))
			vouchermis
					.setFunctionary(functionaryDAO
							.getFunctionaryByCode(BigDecimal.valueOf(Long
									.valueOf(headerdetails.get(
											VoucherConstant.FUNCTIONARYCODE)
											.toString()))));
		if (headerdetails.containsKey(VoucherConstant.FUNCTIONCODE)
				&& null != headerdetails.get(VoucherConstant.FUNCTIONCODE)) {
			final String functionCode = headerdetails.get(
					VoucherConstant.FUNCTIONCODE).toString();
			vouchermis.setFunction(functionDAO.getFunctionByCode(functionCode));
		}

		if (null != headerdetails.get(VoucherConstant.SOURCEPATH))
			vouchermis.setSourcePath(headerdetails.get(
					VoucherConstant.SOURCEPATH).toString());
		if (headerdetails.containsKey(VoucherConstant.DIVISIONID)
				&& null != headerdetails.get(VoucherConstant.DIVISIONID))
			vouchermis.setDivisionid(boundary.getBoundaryById(Long
					.parseLong(headerdetails.get(VoucherConstant.DIVISIONID)
							.toString())));
		if (headerdetails.containsKey(VoucherConstant.BUDGETCHECKREQ)
				&& null != headerdetails.get(VoucherConstant.BUDGETCHECKREQ))
			vouchermis.setBudgetCheckReq((Boolean) headerdetails
					.get(VoucherConstant.BUDGETCHECKREQ));
		else
			vouchermis.setBudgetCheckReq(true);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("END | createVouchermis");
		return vouchermis;
	}

	public void validateTransaction(
			final List<HashMap<String, Object>> accountcodedetails,
			final List<HashMap<String, Object>> subledgerdetails)
			throws ApplicationRuntimeException, Exception {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("START | validateTransaction");
		// List<Transaxtion> transaxtionList = new ArrayList<Transaxtion>();
		BigDecimal totaldebitAmount = BigDecimal.valueOf(0);
		BigDecimal totalcreditAmount = BigDecimal.valueOf(0);
		final Map<String, BigDecimal> accDetAmtMap = new HashMap<String, BigDecimal>();
		for (final HashMap<String, Object> accDetailMap : accountcodedetails) {

			String glcode = null;

			final BigDecimal debitAmount = new BigDecimal(accDetailMap.get(
					VoucherConstant.DEBITAMOUNT).toString());
			final BigDecimal creditAmount = new BigDecimal(accDetailMap.get(
					VoucherConstant.CREDITAMOUNT).toString());

			totaldebitAmount = totaldebitAmount.add(debitAmount);
			totalcreditAmount = totalcreditAmount.add(creditAmount);
			if (accDetailMap.containsKey(VoucherConstant.GLCODE)
					&& null != accDetailMap.get(VoucherConstant.GLCODE)) {
				glcode = accDetailMap.get(VoucherConstant.GLCODE).toString();
				if (null == chartOfAccountsDAO
						.getCChartOfAccountsByGlCode(glcode))
					throw new ApplicationRuntimeException(
							"Not a valid account code" + glcode);
			} else
				throw new ApplicationRuntimeException(
						"glcode is missing or null");
			if (debitAmount.compareTo(BigDecimal.ZERO) != 0
					&& creditAmount.compareTo(BigDecimal.ZERO) != 0)
				throw new ApplicationRuntimeException(
						"Both debit amount and credit amount cannot be greater than zero");
			if (debitAmount.compareTo(BigDecimal.ZERO) == 0
					&& creditAmount.compareTo(BigDecimal.ZERO) == 0)
				throw new ApplicationRuntimeException(
						"debit and credit both amount is Zero");
			if (null != accDetailMap.get(VoucherConstant.FUNCTIONCODE)
					&& "" != accDetailMap.get(VoucherConstant.FUNCTIONCODE)) {
				final String functionCode = accDetailMap.get(
						VoucherConstant.FUNCTIONCODE).toString();
				if (null == functionDAO.getFunctionByCode(functionCode))
					throw new ApplicationRuntimeException(
							"not a valid function code");
			}
			if (debitAmount.compareTo(BigDecimal.ZERO) != 0) {
				if (null != accDetAmtMap.get(VoucherConstant.DEBIT + glcode)) {
					final BigDecimal accountCodeTotDbAmt = accDetAmtMap.get(
							VoucherConstant.DEBIT + glcode).add(debitAmount);
					accDetAmtMap.put(VoucherConstant.DEBIT + glcode,
							accountCodeTotDbAmt);
				} else
					accDetAmtMap.put(VoucherConstant.DEBIT + glcode,
							debitAmount);

			} else if (creditAmount.compareTo(BigDecimal.ZERO) != 0)
				if (null != accDetAmtMap.get(VoucherConstant.CREDIT + glcode)) {
					final BigDecimal accountCodeTotCrAmt = accDetAmtMap.get(
							VoucherConstant.CREDIT + glcode).add(creditAmount);
					accDetAmtMap.put(VoucherConstant.CREDIT + glcode,
							accountCodeTotCrAmt);
				} else
					accDetAmtMap.put(VoucherConstant.CREDIT + glcode,
							creditAmount);
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Total Debit  amount   :" + totaldebitAmount);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Total Credit amount   :" + totalcreditAmount);
		totaldebitAmount = totaldebitAmount.setScale(2,
				BigDecimal.ROUND_HALF_UP);
		totalcreditAmount = totalcreditAmount.setScale(2,
				BigDecimal.ROUND_HALF_UP);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Total Debit  amount after round off :"
					+ totaldebitAmount);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Total Credit amount after round off :"
					+ totalcreditAmount);
		if (totaldebitAmount.compareTo(totalcreditAmount) != 0)
			throw new ApplicationRuntimeException(
					"total debit and total credit amount is not matching");
		final Map<String, BigDecimal> subledAmtmap = new HashMap<String, BigDecimal>();
		for (final HashMap<String, Object> subdetailDetailMap : subledgerdetails) {
			String glcode = null;
			String detailtypeid = null;
			String detailKeyId = null;
			if (null != subdetailDetailMap.get(VoucherConstant.GLCODE)) {
				glcode = subdetailDetailMap.get(VoucherConstant.GLCODE)
						.toString();
				if (null == chartOfAccountsDAO
						.getCChartOfAccountsByGlCode(glcode))
					throw new ApplicationRuntimeException("not a valid glcode");
			} else
				throw new ApplicationRuntimeException("glcode is missing");
			final Query querytds = persistenceService.getSession().createQuery(
					"select t.id from Recovery t where "
							+ "t.chartofaccounts.glcode=:glcode");
			querytds.setString("glcode", glcode);
			querytds.setCacheable(true);
			if (null != querytds.list()
					&& querytds.list().size() > 0
					&& null == subdetailDetailMap.get(VoucherConstant.TDSID)
					&& null != subdetailDetailMap
							.get(VoucherConstant.CREDITAMOUNT)
					&& new BigDecimal(subdetailDetailMap.get(
							VoucherConstant.CREDITAMOUNT).toString())
							.compareTo(BigDecimal.ZERO) != 0) {
				/*
				 * Commenting out throw ApplicationRuntimeException since we are
				 * using the same API for create Journal Voucher. There we are
				 * not setting the TDS id..
				 */
				// throw new
				// ApplicationRuntimeException("Recovery detail is missing for glcode :"+glcode);
			}
			// validate the glcode is a subledger code or not.

			final Query query = persistenceService.getSession().createQuery(
					"from CChartOfAccountDetail cd,CChartOfAccounts c where "
							+ "cd.glCodeId = c.id and c.glcode=:glcode");

			query.setString(VoucherConstant.GLCODE, glcode);
			query.setCacheable(true);
			if (null == query.list() || query.list().size() == 0)
				throw new ApplicationRuntimeException(
						"This code is not a control code" + glcode);

			// validate subledger Detailtypeid

			if (null != subdetailDetailMap.get(VoucherConstant.DETAILTYPEID)) {
				detailtypeid = subdetailDetailMap.get(
						VoucherConstant.DETAILTYPEID).toString();
				final Session session = persistenceService.getSession();
				final Query qry = session
						.createQuery("from CChartOfAccountDetail cd,CChartOfAccounts c where "
								+ "cd.glCodeId = c.id and c.glcode=:glcode and cd.detailTypeId.id=:detailTypeId");
				qry.setString(VoucherConstant.GLCODE, glcode);
				qry.setInteger("detailTypeId", Integer.valueOf(detailtypeid));
				qry.setCacheable(true);
				if (null == qry.list() || qry.list().size() == 0)
					throw new ApplicationRuntimeException(
							"The subledger type mapped to this account code is not correct "
									+ glcode);
			} else
				throw new ApplicationRuntimeException(
						"Subledger type value is missing for account code "
								+ glcode);

			if (null != subdetailDetailMap.get(VoucherConstant.DETAILKEYID)) {
				detailKeyId = subdetailDetailMap.get(
						VoucherConstant.DETAILKEYID).toString();
				final Session session = persistenceService.getSession();
				final Query qry = session
						.createQuery("from Accountdetailkey adk where adk.accountdetailtype.id=:detailtypeid and adk.detailkey=:detailkey");
				qry.setInteger(VoucherConstant.DETAILTYPEID,
						Integer.valueOf(detailtypeid));
				qry.setInteger("detailkey", Integer.valueOf(detailKeyId));
				qry.setCacheable(true);
				if (null == qry.list() || qry.list().size() == 0)
					throw new ApplicationRuntimeException(
							"Subledger data is not valid for account code "
									+ glcode);
			} else
				throw new ApplicationRuntimeException("detailkeyid is missing");

			if (null != subdetailDetailMap.get(VoucherConstant.DEBITAMOUNT)
					&& new BigDecimal(subdetailDetailMap.get(
							VoucherConstant.DEBITAMOUNT).toString())
							.compareTo(BigDecimal.ZERO) != 0) {
				final BigDecimal dbtAmount = new BigDecimal(subdetailDetailMap
						.get(VoucherConstant.DEBITAMOUNT).toString());
				if (null != subledAmtmap.get(VoucherConstant.DEBIT + glcode))
					subledAmtmap.put(VoucherConstant.DEBIT + glcode,
							subledAmtmap.get(VoucherConstant.DEBIT + glcode)
									.add(dbtAmount));
				else
					subledAmtmap.put(VoucherConstant.DEBIT + glcode, dbtAmount);

			} else if (null != subdetailDetailMap
					.get(VoucherConstant.CREDITAMOUNT)
					&& new BigDecimal(subdetailDetailMap.get(
							VoucherConstant.CREDITAMOUNT).toString())
							.compareTo(BigDecimal.ZERO) != 0) {
				final BigDecimal creditAmt = new BigDecimal(subdetailDetailMap
						.get(VoucherConstant.CREDITAMOUNT).toString());
				if (null != subledAmtmap.get(VoucherConstant.CREDIT + glcode))
					subledAmtmap.put(VoucherConstant.CREDIT + glcode,
							subledAmtmap.get(VoucherConstant.CREDIT + glcode)
									.add(creditAmt));
				else
					subledAmtmap
							.put(VoucherConstant.CREDIT + glcode, creditAmt);

			} else
				throw new ApplicationRuntimeException(
						"Incorrect Sub ledger amount supplied for glcode : "
								+ glcode);

		}

		for (final HashMap<String, Object> accDetailMap : accountcodedetails) {

			final String glcode = accDetailMap.get(VoucherConstant.GLCODE)
					.toString();

			if (null != subledAmtmap.get(VoucherConstant.DEBIT + glcode))
				// changed since equals does considers decimal values eg 20.0 is
				// not equal to 2
				if (subledAmtmap.get(VoucherConstant.DEBIT + glcode).compareTo(
						accDetAmtMap.get(VoucherConstant.DEBIT + glcode)) != 0)
					throw new ApplicationRuntimeException(
							"Total of subleger debit amount is not matching with the account code amount "
									+ glcode);
			if (null != subledAmtmap.get(VoucherConstant.CREDIT + glcode))
				// changed since equals does considers decimal values eg 20.0 is
				// not equal to 2
				if (subledAmtmap.get(VoucherConstant.CREDIT + glcode)
						.compareTo(
								accDetAmtMap.get(VoucherConstant.CREDIT
										+ glcode)) != 0)
					throw new ApplicationRuntimeException(
							"Total of subleger credit amount is not matching with the account code amount "
									+ glcode);

		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("END | validateTransaction");

	}

	public List<Transaxtion> createTransaction(
			final HashMap<String, Object> headerdetails,
			final List<HashMap<String, Object>> accountcodedetails,
			final List<HashMap<String, Object>> subledgerdetails,
			final CVoucherHeader vh) throws ApplicationRuntimeException {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Start | createTransaction ");
		final List<Transaxtion> transaxtionList = new ArrayList<Transaxtion>();
		try {
			Integer voucherLineId = 1;
			for (final HashMap<String, Object> accDetailMap : accountcodedetails) {
				final String glcode = accDetailMap.get(VoucherConstant.GLCODE)
						.toString();

				final String debitAmount = accDetailMap.get(
						VoucherConstant.DEBITAMOUNT).toString();
				final String creditAmount = accDetailMap.get(
						VoucherConstant.CREDITAMOUNT).toString();
				String functionId = null;
				String functioncode = null;
				if (null != accDetailMap.get(VoucherConstant.NARRATION))
					accDetailMap.get(VoucherConstant.NARRATION).toString();
				if (null != accDetailMap.get(VoucherConstant.FUNCTIONCODE)
						&& "" != accDetailMap.get(VoucherConstant.FUNCTIONCODE)) {
					functioncode = accDetailMap.get(
							VoucherConstant.FUNCTIONCODE).toString();
					functionId = functionDAO
							.getFunctionByCode(
									accDetailMap.get(
											VoucherConstant.FUNCTIONCODE)
											.toString()).getId().toString();
					if (LOGGER.isDebugEnabled())
						LOGGER.debug("functionId>>>>>>>> " + functionId);
				}
				final CChartOfAccounts chartOfAcc = chartOfAccountsDAO
						.getCChartOfAccountsByGlCode(glcode);
				/*
				 * VoucherDetail voucherDetail = new VoucherDetail();
				 * voucherDetail.setLineId(lineId++);
				 * voucherDetail.setVoucherHeaderId(vh);
				 * voucherDetail.setGlCode(chartOfAcc.getGlcode());
				 * voucherDetail.setAccountName(chartOfAcc.getName());
				 * voucherDetail.setDebitAmount(new BigDecimal(debitAmount));
				 * voucherDetail.setCreditAmount(new BigDecimal(creditAmount));
				 * voucherDetail.setNarration(narration); // insert into voucher
				 * detail. insertIntoVoucherDetail(voucherDetail);
				 * vh.addVoucherDetail(voucherDetail);
				 */
				final Transaxtion transaction = new Transaxtion();
				transaction.setGlCode(chartOfAcc.getGlcode());
				transaction.setGlName(chartOfAcc.getName()); 
				transaction.setVoucherLineId(String.valueOf(voucherLineId++));
				transaction.setVoucherHeaderId(vh.getId().toString());
				transaction.setCrAmount(creditAmount);
				transaction.setDrAmount(debitAmount);
				transaction.setFunctionId(functionId);
				if (headerdetails != null
						&& headerdetails.get("billid") != null)
					transaction.setBillId((Long) headerdetails.get("billid"));
				
			 
				final ArrayList reqParams = new ArrayList();
				for (final HashMap<String, Object> sublegDetailMap : subledgerdetails) {

					final String detailGlCode = sublegDetailMap.get(
							VoucherConstant.GLCODE).toString();
					final String detailtypeid = sublegDetailMap.get(
							VoucherConstant.DETAILTYPEID).toString();
					if (sublegDetailMap
							.containsKey(VoucherConstant.FUNCTIONCODE)
							&& null != sublegDetailMap
									.get(VoucherConstant.FUNCTIONCODE)
							&& "" != sublegDetailMap
									.get(VoucherConstant.FUNCTIONCODE)) {
						final String detailFunctionCode = sublegDetailMap.get(
								VoucherConstant.FUNCTIONCODE).toString();
						if (glcode.equals(detailGlCode) && functioncode != null
								&& functioncode.equals(detailFunctionCode)) {
							final TransaxtionParameter reqData = new TransaxtionParameter();
							final Accountdetailtype adt = (Accountdetailtype) accountdetailtypeHibernateDAO
									.findById(Integer.valueOf(detailtypeid),
											false);
							reqData.setDetailName(adt.getAttributename());
							reqData.setGlcodeId(chartOfAcc.getId().toString());
							if (null != sublegDetailMap
									.get(VoucherConstant.DEBITAMOUNT)
									&& new BigDecimal(sublegDetailMap.get(
											VoucherConstant.DEBITAMOUNT)
											.toString())
											.compareTo(BigDecimal.ZERO) != 0)
								reqData.setDetailAmt(sublegDetailMap.get(
										VoucherConstant.DEBITAMOUNT).toString());
							else
								reqData.setDetailAmt(sublegDetailMap.get(
										VoucherConstant.CREDITAMOUNT)
										.toString());

							reqData.setDetailKey(sublegDetailMap.get(
									VoucherConstant.DETAILKEYID).toString());
							reqData.setDetailTypeId(detailtypeid);
							reqData.setTdsId(sublegDetailMap.get("tdsId") != null ? sublegDetailMap
									.get("tdsId").toString() : null);
							reqParams.add(reqData);
						}
					} else if (glcode.equals(detailGlCode)) {
						final TransaxtionParameter reqData = new TransaxtionParameter();
						final Accountdetailtype adt = (Accountdetailtype) accountdetailtypeHibernateDAO
								.findById(Integer.valueOf(detailtypeid), false);
						reqData.setDetailName(adt.getAttributename());
						reqData.setGlcodeId(chartOfAcc.getId().toString());
						if (null != sublegDetailMap
								.get(VoucherConstant.DEBITAMOUNT)
								&& new BigDecimal(sublegDetailMap.get(
										VoucherConstant.DEBITAMOUNT).toString())
										.compareTo(BigDecimal.ZERO) != 0)
							reqData.setDetailAmt(sublegDetailMap.get(
									VoucherConstant.DEBITAMOUNT).toString());
						else
							reqData.setDetailAmt(sublegDetailMap.get(
									VoucherConstant.CREDITAMOUNT).toString());

						reqData.setDetailKey(sublegDetailMap.get(
								VoucherConstant.DETAILKEYID).toString());
						reqData.setDetailTypeId(detailtypeid);
						reqData.setTdsId(sublegDetailMap.get("tdsId") != null ? sublegDetailMap
								.get("tdsId").toString() : null);
						reqParams.add(reqData);
					}

				}
				if (reqParams != null && reqParams.size() > 0)
					transaction.setTransaxtionParam(reqParams);
				transaxtionList.add(transaction);
			}
		} catch (final Exception e) {
			LOGGER.error("Exception occured while posting data into voucher detail and transaction");
			throw new ApplicationRuntimeException(
					"Exception occured while posting data into voucher detail and transaction"
							+ e.getMessage());
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("END | createTransaction ");
		return transaxtionList;
	}

	public Functionary getFunctionaryByCode(final BigDecimal code) {
		// functionarySer.setType(Functionary.class);
		final Functionary functionary = (Functionary) persistenceService.find(
				"from Functionary where code=?", code);
		return functionary;

	}

	public void validateVoucherHeader(final CVoucherHeader voucherHeader) {
		if (null == voucherHeader)
			throw new ApplicationRuntimeException(
					"voucherHeader object passed is null");
		if (null == voucherHeader.getType()
				|| !voucherHeader.getType().equalsIgnoreCase(
						voucherTypeEnum.RECEIPT.toString()))
			throw new ApplicationRuntimeException("Voucher type is not Receipt");
	}

	public void validateReceiptDetails(
			final HashMap<String, Object> receiptdetails) {
		String modeofcollection = null;
		if (null == receiptdetails)
			throw new ApplicationRuntimeException("receiptdetails is null");
		if (null == receiptdetails.get(VoucherConstant.MODEOFCOLLECTION))
			throw new ApplicationRuntimeException("modeofcollection is null");
		else {
			modeofcollection = chkModeOfCollection(receiptdetails.get(
					VoucherConstant.MODEOFCOLLECTION).toString());
			if (null == modeofcollection)
				throw new ApplicationRuntimeException(
						"Not a valid modeofcollection");
		}
		if (VoucherConstant.BANK.equalsIgnoreCase(modeofcollection))
			validateBankDetails(receiptdetails);

		if (null == receiptdetails.get(VoucherConstant.NETAMOUNT))
			throw new ApplicationRuntimeException("Net amount is null");
	}

	public void validateBankDetails(final HashMap<String, Object> receiptdetails) {
		String bankCode = null;
		String bankBranchCode = null;
		String bankAccNumber = null;
		if (null == receiptdetails.get(VoucherConstant.BANKCODE))
			throw new ApplicationRuntimeException("Bank Code is null");
		if (null == receiptdetails.get(VoucherConstant.BANKBRANCHCODE))
			throw new ApplicationRuntimeException("Bank branch code  is null");
		if (null == receiptdetails.get(VoucherConstant.BANKACCOUNTNUMBER))
			throw new ApplicationRuntimeException("Bank Account number is null");
		bankCode = receiptdetails.get(VoucherConstant.BANKCODE).toString();
		bankBranchCode = receiptdetails.get(VoucherConstant.BANKBRANCHCODE)
				.toString();
		bankAccNumber = receiptdetails.get(VoucherConstant.BANKACCOUNTNUMBER)
				.toString();
		final Bankaccount bankAccount = bankAccountDAO
				.getBankAccountByAccBranchBank(bankAccNumber, bankBranchCode,
						bankCode);
		if (null == bankAccount)
			throw new ApplicationRuntimeException(
					"not a valid bank account number");
		else
			receiptdetails.put(VoucherConstant.BANKACCID, bankAccount.getId());
	}

	/**
	 *
	 * @param instrumentdetails
	 */
	public void validateInstrumentdetails(
			final List<HashMap<String, Object>> instrumentdetailsList,
			final HashMap<String, Object> receiptdetails) {
		String bankCode = null;
		BigDecimal chequeAmounts = BigDecimal.valueOf(0);
		for (final HashMap<String, Object> instrumentdetails : instrumentdetailsList) {
			if (null == instrumentdetails.get(VoucherConstant.INSTRUMENTNO))
				throw new ApplicationRuntimeException("Cheque number is null");
			if (null == instrumentdetails.get(VoucherConstant.INSTRUMENTDATE))
				throw new ApplicationRuntimeException("Cheque date is null");
			if (null == instrumentdetails.get(VoucherConstant.INSTRUMENTAMOUNT))
				throw new ApplicationRuntimeException("Cheque amount is null");
			else
				chequeAmounts = chequeAmounts.add(new BigDecimal(
						instrumentdetails.get(VoucherConstant.INSTRUMENTAMOUNT)
								.toString()));
			if (null != instrumentdetails.get(VoucherConstant.BANKCODE)) {
				bankCode = instrumentdetails.get(VoucherConstant.BANKCODE)
						.toString();
				if (null == bankDAO.getBankByCode(bankCode))
					throw new ApplicationRuntimeException(
							"not a valid bank code");
			}
		}
		if (!chequeAmounts
				.equals(receiptdetails.get(VoucherConstant.NETAMOUNT)))
			throw new ApplicationRuntimeException(
					"total cheque amount is not matching with net amount");

	}

	/*
	 * protected void postInReceiptHeader(final HashMap<String, Object>
	 * receiptdetails,Object CVoucherHeader,Chequedetail chequeDetail){
	 * rcptHeaderService = new PersistenceService<ReceiptHeader, Integer>(); //
	 * rcptHeaderService.setSessionFactory(new SessionFactory());
	 * rcptHeaderService.setType(ReceiptHeader.class); String modeOfCollection =
	 * receiptdetails.get(VoucherConstant.MODEOFCOLLECTION).toString();
	 * BigDecimal netAmount = new
	 * BigDecimal(receiptdetails.get(VoucherConstant.NETAMOUNT).toString());
	 * CVoucherHeader voucherHeader = (CVoucherHeader) CVoucherHeader;
	 * ReceiptHeader receiptHeader = new ReceiptHeader(); //Setting data into
	 * receipt header
	 * receiptHeader.setModeOfCollection(chkModeOfCollection(modeOfCollection));
	 * receiptHeader.setCashAmount(netAmount);
	 * receiptHeader.setVoucherHeaderId(voucherHeader);
	 * receiptHeader.setType(voucherHeader.getType()); if(null !=
	 * receiptdetails.get(VoucherConstant.MANNUALRECEIPTNUMBER)){
	 * receiptHeader.setManualReceiptNo
	 * (receiptdetails.get(VoucherConstant.MANNUALRECEIPTNUMBER).toString()); }
	 * if(null != receiptdetails.get(VoucherConstant.NARRATION)){
	 * receiptHeader.setManualReceiptNo
	 * (receiptdetails.get(VoucherConstant.NARRATION).toString()); } if(null !=
	 * receiptdetails.get(VoucherConstant.REVENUESOURCE)){
	 * receiptHeader.setRevenueSource
	 * ((receiptdetails.get(VoucherConstant.REVENUESOURCE).toString())); }
	 * if(null != receiptdetails.get(VoucherConstant.WARDID)){
	 * receiptHeader.setRevenueSource
	 * ((receiptdetails.get(VoucherConstant.WARDID).toString())); }
	 * receiptHeader.setIsReversed(0); if(null != chequeDetail){
	 * receiptHeader.setChequeId(chequeDetail); }if(null !=
	 * receiptdetails.get(VoucherConstant.RECEIPTNUMBER)){
	 * receiptHeader.setReceiptNo(
	 * receiptdetails.get(VoucherConstant.RECEIPTNUMBER).toString()); } if(null
	 * != receiptdetails.get(VoucherConstant.MANUUALRECEIPTNO)){
	 * receiptHeader.setManualReceiptNo
	 * (receiptdetails.get(VoucherConstant.MANUUALRECEIPTNO).toString()); }
	 * if(null != receiptdetails.get(VoucherConstant.NARRATION)){
	 * receiptHeader.setNarration
	 * (receiptdetails.get(VoucherConstant.NARRATION).toString()); }
	 * rcptHeaderService.persist(receiptHeader); }
	 */

	enum modeOfCollectionEnum {
		Cheque, Cash, Bank;
	}

	private String chkModeOfCollection(final String mode) {
		String collectionMode = null;
		try {
			switch (Integer.valueOf(mode)) {
			case 1:
				collectionMode = modeOfCollectionEnum.Cheque.toString();
				break;
			case 2:
				collectionMode = modeOfCollectionEnum.Cash.toString();
				break;
			case 3:
				collectionMode = modeOfCollectionEnum.Bank.toString();
				break;
			}
		} catch (final Exception e) {
			LOGGER.error(ERR, e);
			throw new ApplicationRuntimeException(
					"Not a valid modeofcollection");
		}

		return collectionMode;
	}

	

	public void updatePJV(final CVoucherHeader vh,
			final List<PreApprovedVoucher> detailList,
			final List<PreApprovedVoucher> subledgerlist)
			throws ApplicationRuntimeException {
		try {
			// delete the existing voucherdetail, gl entries.
			deleteVoucherdetailAndGL(vh);

			HashMap<String, Object> detailMap = null;
			HashMap<String, Object> subledgerMap = null;
			final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
			final List<HashMap<String, Object>> subledgerdetails = new ArrayList<HashMap<String, Object>>();
			CChartOfAccounts coa = null;
			CFunction function = null;
			// iterate it
			for (final PreApprovedVoucher detail : detailList) {
				detailMap = new HashMap<String, Object>();
				detailMap.put(VoucherConstant.GLCODE, detail.getGlcodeDetail());
				detailMap.put(VoucherConstant.DEBITAMOUNT,
						detail.getDebitAmountDetail());
				detailMap.put(VoucherConstant.CREDITAMOUNT,
						detail.getCreditAmountDetail());
				if (detail.getFunctionIdDetail() != null) {
					function = functionDAO.getFunctionById(detail
							.getFunctionIdDetail());
					detailMap.put(VoucherConstant.FUNCTIONCODE,
							function.getCode());
				}
				accountdetails.add(detailMap);
			}
			if (subledgerlist != null)
				for (final PreApprovedVoucher subledger : subledgerlist) {
					subledgerMap = new HashMap<String, Object>();
					coa = (CChartOfAccounts) chartOfAccountsDAO.findById(
							subledger.getGlcode().getId(), false);
					subledgerMap.put(VoucherConstant.GLCODE, coa.getGlcode());
					subledgerMap.put(VoucherConstant.DETAILTYPEID, subledger
							.getDetailType().getId());
					subledgerMap.put(VoucherConstant.DETAILKEYID,
							subledger.getDetailKeyId());
					if (subledger.getDebitAmountDetail() == null
							|| subledger.getDebitAmountDetail().compareTo(
									BigDecimal.ZERO) == 0)
						subledgerMap.put(VoucherConstant.CREDITAMOUNT,
								subledger.getCreditAmount());
					else
						subledgerMap.put(VoucherConstant.DEBITAMOUNT,
								subledger.getDebitAmount());
					subledgerdetails.add(subledgerMap);
				}
			final List<Transaxtion> transactions = createTransaction(null,
					accountdetails, subledgerdetails, vh);
			persistenceService.getSession().flush();
			Transaxtion txnList[] = new Transaxtion[transactions.size()];
			txnList = transactions.toArray(txnList);
			final SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
			if (!chartOfAccounts.postTransaxtions(txnList,
					formatter.format(vh.getVoucherDate())))
				throw new ApplicationRuntimeException("Voucher creation Failed");
		} catch (final Exception e) {
			LOGGER.error("Inside exception updatePJV" + e.getMessage());
			throw new ApplicationRuntimeException(e.getMessage());
		}
	}

	public void deleteVoucherdetailAndGL(final CVoucherHeader vh)
			throws SQLException, ApplicationRuntimeException {
		try {
			Query pstmt1 = null;
			Query pstmt2 = null;
			Query pstmt3 = null;
			Query pstmt4 = null;

			final String glQry = "select id from generalledger where voucherheaderid= ?";
			final String glidQry = "select id from generalledgerdetail where generalledgerid= ?";
			final String delQry = "delete from EG_REMITTANCE_GLDTL where gldtlid= ?";
			final String delQrr = "delete from generalledgerdetail where generalledgerid=?";
			final String delgl = " delete from generalledger where voucherheaderid=?";
			final String delvh = " delete from voucherdetail where voucherheaderid=?";
			pstmt1 = persistenceService.getSession().createSQLQuery(glQry);
			pstmt1.setFloat(0, vh.getId());

			final List<Object[]> rs = pstmt1.list();
			List<Object[]> rs1 = null;
			boolean delete = false;
			while (rs != null && rs.size() > 0) {
				pstmt2 = persistenceService.getSession()
						.createSQLQuery(glidQry);
				pstmt2.setLong(0, Long.parseLong(rs.get(1).toString()));
				rs1 = pstmt2.list();
				while (rs1 != null && rs1.size() > 0) {
					delete = true;
					pstmt3 = persistenceService.getSession().createSQLQuery(
							delQry);
					pstmt3.setLong(0, Long.parseLong(rs1.get(1).toString()));
					pstmt3.executeUpdate();
				}
				if (delete) {
					pstmt4 = persistenceService.getSession().createSQLQuery(
							delQrr);
					pstmt4.setLong(0, Long.parseLong(rs1.get(1).toString()));
					pstmt4.executeUpdate();
				}
			}
			pstmt1 = persistenceService.getSession().createSQLQuery(delgl);
			pstmt1.setLong(0, vh.getId());
			pstmt1.executeUpdate();

			pstmt1 = persistenceService.getSession().createSQLQuery(delvh);
			pstmt1.setLong(0, vh.getId());
			pstmt1.executeUpdate();

		} catch (final Exception e) {
			LOGGER.error("Inside exception deleteVoucherdetailAndGL"
					+ e.getMessage());
			throw new ApplicationRuntimeException(e.getMessage());
		}
	}

	/**
	 * HashMap<String, Object> voucher details to reverse a voucher
	 * <p>
	 * Original voucher header id -long- is the voucher header id of the voucher
	 * to be reversed later callled originalvoucher
	 * <p>
	 * Reversal voucher name -String - name of the reversal voucher (mandatory)
	 * <p>
	 * Reversal voucher type -String - type of the reversal voucher (mandatory)
	 * <p>
	 * Reversal voucher date - Date('dd/MM/yyyy') date of the reversal vocuher
	 * <p>
	 * Reversal voucher number - String - voucher number of reversal voucher
	 * Newly created voucher is called reversal voucher
	 * 
	 * @param paramList
	 * @return
	 */
	@Transactional
	public CVoucherHeader reverseVoucher(
			final List<HashMap<String, Object>> paramList)
					throws ApplicationRuntimeException, ParseException {
		// -- Reversal Voucher date check ----
		CVoucherHeader reversalVoucherObj = new CVoucherHeader();
		CVoucherHeader originalVocher=null;
		for (final HashMap<String, Object> paramMap : paramList) {

			if (paramMap.get(VOUCHER_HEADER_ID) == null)
				throw new IllegalArgumentException(VOUCHER_HEADER_ID
						+ IS_MISSING);
			else {
				try {  
					originalVocher = voucherService.find("from CVoucherHeader where id=?",(Long) paramMap.get(VOUCHER_HEADER_ID));
								 
				} catch (final Exception e) {
					throw new ApplicationRuntimeException("cannot find "
							+ VOUCHER_HEADER_ID + "in the system");
				}
				reversalVoucherObj.setOriginalvcId(originalVocher.getId());
			}

			if (paramMap.get(REVERSAL_VOUCHER_DATE) == null) {
				final SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY);
				final Date reversalVoucherDate = sdf.parse(sdf
						.format(originalVocher.getVoucherDate()));
				reversalVoucherObj.setVoucherDate(reversalVoucherDate);
			} else {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Voucher  end REVERSAL_VOUCHER_DAT :"
							+ paramMap.get(REVERSAL_VOUCHER_DATE));
				final Date reversalVoucherDate = sdf.parse(sdf
						.format(paramMap
								.get(REVERSAL_VOUCHER_DATE)));
				reversalVoucherObj.setVoucherDate( reversalVoucherDate);
			}
		}
		originalVocher = (CVoucherHeader) persistenceService.find(
				"from CVoucherHeader where id=?",
				reversalVoucherObj.getOriginalvcId());
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("original voucher is "
					+ reversalVoucherObj.getOriginalvcId());
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("reversalVoucherObj getVoucherDate is "
					+ reversalVoucherObj.getVoucherDate());
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("originalVocher getVoucherDate is "
					+ originalVocher.getVoucherDate());
		if (reversalVoucherObj.getVoucherDate().before(
				originalVocher.getVoucherDate())) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Reversal Voucher Date should be greater than the Origianal Voucher Date");
			throw new ValidationException(
					Arrays.asList(new ValidationError(
							"reversal.voucher.date.validate",
							"Reversal Voucher Date should be greater than the Original Voucher Date")));
		}



		reversalVoucherObj.setName(originalVocher.getName());
		reversalVoucherObj.setType(originalVocher.getType());
		reversalVoucherObj.setVoucherSubType(originalVocher
				.getVoucherSubType());

		reversalVoucherObj.setVoucherNumber("");

		reversalVoucherObj.setDescription(originalVocher.getDescription());
		reversalVoucherObj.setVouchermis(originalVocher.getVouchermis());
		reversalVoucherObj.setFundId(originalVocher.getFundId());

		final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(reversalVoucherObj);

		List<CGeneralLedger> orginalLedgerEntries = generalLedgerService
				.findAllBy("from CGeneralLedger where voucherHeaderId=?",
						originalVocher);
		HashMap<String, Object> detailMap = null;
		HashMap<String, Object> subledgerMap = null;
		final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
		final List<HashMap<String, Object>> subledgerdetails = new ArrayList<HashMap<String, Object>>();



		CFunction function = null;
		for (final CGeneralLedger ledger : orginalLedgerEntries) {
			detailMap = new HashMap<String, Object>();
			detailMap.put(VoucherConstant.GLCODE, ledger.getGlcode());
			//debit becomes credit ,credit becomes debit
			detailMap.put(VoucherConstant.DEBITAMOUNT,ledger.getCreditAmount());
			detailMap.put(VoucherConstant.CREDITAMOUNT,ledger.getDebitAmount());

			if (ledger.getFunctionId() != null) {
				function = functionDAO.getFunctionById(ledger
						.getFunctionId().longValue());
				detailMap.put(VoucherConstant.FUNCTIONCODE,function.getCode());
			}
			List<CGeneralLedgerDetail> ledgerDetailSet = generalLedgerDetailService
					.findAllBy(
							"from CGeneralLedgerDetail where generalLedgerId.id=?",
							ledger.getId());
			for (final CGeneralLedgerDetail ledgerDetail : ledgerDetailSet) {
				subledgerMap = new HashMap<String, Object>();
				subledgerMap.put(VoucherConstant.GLCODE, ledger.getGlcode());
				subledgerMap.put(VoucherConstant.DETAILTYPEID, ledgerDetail.getDetailTypeId().getId());
				subledgerMap.put(VoucherConstant.DETAILKEYID,ledgerDetail.getDetailKeyId());
				//even for subledger debit becomes credit ,credit becomes debit
				if (BigDecimal.valueOf(ledger.getDebitAmount()).compareTo(BigDecimal.ZERO)!=0)
					subledgerMap.put(VoucherConstant.CREDITAMOUNT,ledgerDetail.getAmount());
				else
					subledgerMap.put(VoucherConstant.DEBITAMOUNT,ledgerDetail.getAmount().setScale(2,BigDecimal.ROUND_HALF_EVEN));

				subledgerdetails.add(subledgerMap);
			}
			accountdetails.add(detailMap);
		}
		try{
			reversalVoucherObj = createVoucher(headerDetails, accountdetails, subledgerdetails);
		} catch (final HibernateException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ValidationException(Arrays.asList(new ValidationError(
					EXCEPTION_WHILE_SAVING_DATA, FAILED)));
		} catch (final ApplicationRuntimeException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ValidationException(Arrays.asList(new ValidationError(e
					.getMessage(), e.getMessage())));
		} catch (final ValidationException e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new ValidationException(Arrays.asList(new ValidationError(e
					.getMessage(), e.getMessage())));
		}
		   
			originalVocher.setStatus(1);
			originalVocher.setEffectiveDate(new Date());
			voucherService.applyAuditing(originalVocher);
			voucherService.update(originalVocher);
		
			reversalVoucherObj.setOriginalvcId(originalVocher.getId());
			reversalVoucherObj.setStatus(2);
			voucherService.applyAuditing(reversalVoucherObj);
			voucherService.persist(reversalVoucherObj);
			
			

			return reversalVoucherObj;
		}

	


	private CVoucherHeader validateAndAssignReversalVoucherParams(
			final List<HashMap<String, Object>> paramList)
			throws ParseException {
		final CVoucherHeader reversalVoucher = new CVoucherHeader();
		for (final HashMap<String, Object> paramMap : paramList) {
			CVoucherHeader originalVoucher = null;
			if (paramMap.get(VOUCHER_HEADER_ID) == null)
				throw new IllegalArgumentException(VOUCHER_HEADER_ID
						+ IS_MISSING);
			else {
				try {
					originalVoucher = (CVoucherHeader) voucherHeaderDAO
							.findById((Long) paramMap.get(VOUCHER_HEADER_ID),
									false);
				} catch (final Exception e) {
					throw new ApplicationRuntimeException("cannot find "
							+ VOUCHER_HEADER_ID + "in the system");
				}
				reversalVoucher.setOriginalvcId(originalVoucher.getId());

			}
			if (paramMap.get(NAME) == null
					|| ((String) paramMap.get(NAME)).isEmpty())
				throw new IllegalArgumentException(NAME + IS_MISSING + "or"
						+ IS_EMPTY);
			else
				reversalVoucher.setName((String) paramMap.get(NAME));
			if (paramMap.get(TYPE) == null
					|| ((String) paramMap.get(TYPE)).isEmpty())
				throw new IllegalArgumentException(TYPE + IS_MISSING + "or"
						+ IS_EMPTY);
			else {
				validateVoucherType((String) paramMap.get(TYPE));
				reversalVoucher
						.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
			}
			if (paramMap.get(REVERSAL_VOUCHER_DATE) == null) {
				final SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY);
				final Date reversalVoucherDate = sdf.parse(sdf
						.format(originalVoucher.getVoucherDate()));
				reversalVoucher.setVoucherDate(reversalVoucherDate);
			} else {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("vo end REVERSAL_VOUCHER_DAT :"
							+ paramMap.get(REVERSAL_VOUCHER_DATE));
				reversalVoucher.setVoucherDate((Date) paramMap
						.get(REVERSAL_VOUCHER_DATE));
			}
			// it may be null and taken care in calling api
			reversalVoucher.setVoucherNumber((String) paramMap
					.get(REVERSAL_VOUCHER_NUMBER));

		}
		return reversalVoucher;
	}

	protected void validateMandotyFields(
			final HashMap<String, Object> headerdetails) {

	}

	protected void checkMandatoryField(final String fieldName,
			final Object value, final HashMap<String, Object> headerdetails,List<String> mandatoryFields) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Filed name :=" + fieldName + " Value = :" + value);
		String vNumGenMode = null;
		if (fieldName.equals("vouchernumber")) {
			if (headerdetails.get(VoucherConstant.VOUCHERTYPE) == null)
				throw new ValidationException(
						Arrays.asList(new ValidationError(
								VoucherConstant.VOUCHERTYPE + ISREQUIRED,
								VoucherConstant.VOUCHERTYPE + ISREQUIRED)));
			else {
				validateVoucherType(headerdetails.get(
						VoucherConstant.VOUCHERTYPE).toString());
				if (FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL
						.equalsIgnoreCase(headerdetails.get(
								VoucherConstant.VOUCHERTYPE).toString()))
					vNumGenMode = voucherTypeForULB.readVoucherTypes("Journal");
				else
					vNumGenMode = voucherTypeForULB
							.readVoucherTypes(headerdetails.get(
									VoucherConstant.VOUCHERTYPE).toString());
				if (!"Auto".equalsIgnoreCase(vNumGenMode)
						&& (value == null || ((String) value).isEmpty())
						&& headerdetails.get(VoucherConstant.MODULEID) == null)
					throw new ValidationException(
							Arrays.asList(new ValidationError(SELECT
									+ fieldName, SELECT + fieldName)));
			}
		} else if (mandatoryFields.contains(fieldName)
				&& (value == null || StringUtils.isEmpty(value.toString())))
			throw new ValidationException(Arrays.asList(new ValidationError(
					SELECT + fieldName, SELECT + fieldName)));
	}

	@SuppressWarnings("unchecked")
    protected List<String> getHeaderMandateFields() {
		List<String> mandatoryFields=new ArrayList<String>();
        final List<AppConfigValues> appConfigList = appConfigValuesService.getConfigValuesByModuleAndKey(
                FinancialConstants.MODULE_NAME_APPCONFIG, "DEFAULTTXNMISATTRRIBUTES");

        for (final AppConfigValues appConfigVal : appConfigList)
        {
            final String value = appConfigVal.getValue();
            final String header = value.substring(0, value.indexOf("|"));
            final String mandate = value.substring(value.indexOf("|") + 1);
            if (mandate.equalsIgnoreCase("M"))
                mandatoryFields.add(header);
        }
        return mandatoryFields;
    }



	private HashMap<String, Object> createHeaderAndMisDetails(
			CVoucherHeader voucherHeader) throws ValidationException {
		final HashMap<String, Object> headerdetails = new HashMap<String, Object>();
		// All reversal will be GJV
		headerdetails.put(VoucherConstant.VOUCHERNAME,
				FinancialConstants.JOURNALVOUCHER_NAME_GENERAL);
		headerdetails.put(VoucherConstant.VOUCHERTYPE,
				FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
		headerdetails.put((String) VoucherConstant.VOUCHERSUBTYPE,
				voucherHeader.getVoucherSubType());
		headerdetails.put(VoucherConstant.VOUCHERNUMBER,
				voucherHeader.getVoucherNumber());
		headerdetails.put(VoucherConstant.VOUCHERDATE,
				voucherHeader.getVoucherDate());
		headerdetails.put(VoucherConstant.DESCRIPTION,
				voucherHeader.getDescription());

		if (voucherHeader.getVouchermis().getDepartmentid() != null)
			headerdetails.put(VoucherConstant.DEPARTMENTCODE, voucherHeader
					.getVouchermis().getDepartmentid().getCode());
		if (voucherHeader.getFundId() != null)
			headerdetails.put(VoucherConstant.FUNDCODE, voucherHeader
					.getFundId().getCode());
		if (voucherHeader.getVouchermis().getSchemeid() != null)
			headerdetails.put(VoucherConstant.SCHEMECODE, voucherHeader
					.getVouchermis().getSchemeid().getCode());
		if (voucherHeader.getVouchermis().getSubschemeid() != null)
			headerdetails.put(VoucherConstant.SUBSCHEMECODE, voucherHeader
					.getVouchermis().getSubschemeid().getCode());
		if (voucherHeader.getVouchermis().getFundsource() != null)
			headerdetails.put(VoucherConstant.FUNDSOURCECODE, voucherHeader
					.getVouchermis().getFundsource().getCode());
		if (voucherHeader.getVouchermis().getDivisionid() != null)
			headerdetails.put(VoucherConstant.DIVISIONID, voucherHeader
					.getVouchermis().getDivisionid().getId());
		if (voucherHeader.getVouchermis().getFunctionary() != null)
			headerdetails.put(VoucherConstant.FUNCTIONARYCODE, voucherHeader
					.getVouchermis().getFunctionary().getCode());
		if (voucherHeader.getVouchermis().getFunction() != null)
			headerdetails.put(VoucherConstant.FUNCTIONCODE, voucherHeader
					.getVouchermis().getFunction().getCode());
		return headerdetails;
	}
	
	 public String getCGVNNumber(CVoucherHeader vh)
	    {
	        String cgvnNumber = "";

	        String sequenceName = "";

	        final CFiscalPeriod fiscalPeriod = fiscalPeriodHibernateDAO.getFiscalPeriodByDate(vh.getVoucherDate());
	        if (fiscalPeriod == null)
	            throw new ApplicationRuntimeException("Fiscal period is not defined for the voucher date");
	        sequenceName = "sq_" + vh.getFundId().getIdentifier() + "_" + getCgnType(vh.getType()).toLowerCase() + "_cgvn_"
	                + fiscalPeriod.getName();
	        Serializable nextSequence = applicationSequenceNumberGenerator.getNextSequence(sequenceName);

	        cgvnNumber = String.format("%s/%s/%s%010d", vh.getFundId().getIdentifier(), getCgnType(vh.getType()), "CGVN",
	                nextSequence);

	        return cgvnNumber;
	    }

	public AppConfigService getAppConfigService() {
		return appConfigService;
	}

	public void setAppConfigService(final AppConfigService appConfigService) {
		this.appConfigService = appConfigService;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(
			final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public FinancialYearDAO getFinancialYearDAO() {
		return financialYearDAO;
	}

	public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public AppConfigValueService getAppConfigValuesService() {
		return appConfigValuesService;
	}

	public void setAppConfigValuesService(
			final AppConfigValueService appConfigValuesService) {
		this.appConfigValuesService = appConfigValuesService;
	}

	
	public boolean isUniqueVN(String vcNum, final String vcDate) throws Exception, TaskFailedException {
	       boolean isUnique = false;
	       String fyStartDate = "", fyEndDate = "";
	       vcNum = vcNum.toUpperCase();
	       Query pst = null;                           
	       List<Object[]> rs = null;
	       try {
	           final String query1 = "SELECT to_char(startingDate, 'DD-Mon-YYYY') AS \"startingDate\", to_char(endingDate, 'DD-Mon-YYYY') AS \"endingDate\" FROM financialYear WHERE startingDate <= '"
	                   + vcDate + "' AND endingDate >= '" + vcDate + "'";
	           pst = persistenceService.getSession().createSQLQuery(query1);
	           rs = pst.list();
	           if (rs != null && rs.size() > 0)
	               for (final Object[] element : rs) {
	                   fyStartDate = element[0].toString();
	                   fyEndDate = element[1].toString();
	               }
	           final String query2 = "SELECT id FROM voucherHeader WHERE voucherNumber = '" + vcNum + "' AND voucherDate>='"
	                   + fyStartDate
	                   + "' AND voucherDate<='" + fyEndDate + "' and status!=4";
	           pst = persistenceService.getSession().createSQLQuery(query2);
	           rs = pst.list();
	           if (rs != null && rs.size() > 0) {
	               if (LOGGER.isDebugEnabled())
	                   LOGGER.debug("Duplicate Voucher Number");
	           } else
	               isUnique = true;
	       } catch (final Exception ex) {
	           LOGGER.error("error in finding unique VoucherNumber");
	            throw new ApplicationRuntimeException("error in finding unique VoucherNumber"); 
	       } finally {
	           
	       }
	       return isUnique;
	   }
	
	public String getFiscalPeriod(final String vDate) throws TaskFailedException {
		BigInteger fiscalPeriod = null;
		final String sql = "select id from fiscalperiod  where '" + vDate + "' between startingdate and endingdate";
		try {
			final Query pst = persistenceService.getSession().createSQLQuery(sql);
			final List<BigInteger> rset = pst.list();
			fiscalPeriod = rset != null ? rset.get(0) : BigInteger.ZERO;
		} catch (final Exception e) {
			LOGGER.error("Exception..." + e.getMessage());
			throw new TaskFailedException(e.getMessage());
		}
		return fiscalPeriod.toString();
	}
	
}