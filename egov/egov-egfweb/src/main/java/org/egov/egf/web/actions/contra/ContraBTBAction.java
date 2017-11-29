/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
/**
 *
 */
package org.egov.egf.web.actions.contra;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.exility.common.TaskFailedException;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.Vouchermis;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.web.actions.voucher.BaseVoucherAction;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.contra.ContraBean;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.payment.Paymentheader;
import org.egov.model.voucher.VoucherTypeBean;
import org.egov.services.cheque.ChequeService;
import org.egov.services.instrument.InstrumentHeaderService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.payment.PaymentService;
import org.egov.services.report.FundFlowService;
import org.egov.services.voucher.ContraJournalVoucherService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
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

/**
 * @author mani
 */
@ParentPackage("egov")
@Results({
        @Result(name = "new", location = "contraBTB-new.jsp"),
        @Result(name = "edit", location = "contraBTB-edit.jsp"),
        @Result(name = "reverse", location = "contraBTB-reverse.jsp"),
        @Result(name = "view", location = "contraBTB-view.jsp"),
        @Result(name = "success", location = "contraBTB-success.jsp")
})
public class ContraBTBAction extends BaseVoucherAction {
    private static final String DD_MMM_YYYY = "dd-MMM-yyyy";
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);
    private SimpleDateFormat sqlformat = new SimpleDateFormat("dd-MMM-yyyy");
    private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving Data";
    private final static Logger LOGGER = Logger
            .getLogger(ContraBTBAction.class);
    private static final String MDC_CHEQUE = "cheque";
    private static final String MDC_OTHER = "RTGS/NEFT";
    private static final String REVERSE = "reverse";
    private static final long serialVersionUID = 1L;
    private static final String TRANSACTION_FAILED = "Transaction failed";
    private static final String VIEW = "view";
    private Date voucherDate;
    public ContraBean contraBean;
    public Map<String, String> fromBankBranchMap;
    public Map<String, String> ModeOfCollectionMap;
    public Map<String, String> toBankBranchMap;
    private String amount;
    private String button;
    private String sourceGlcode;
    private String destinationGlcode;
    private ContraJournalVoucher contraVoucher;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private InstrumentService instrumentService;
    private String mode;
    @Autowired
    @Qualifier("voucherService")
    private VoucherService voucherService;
    private VoucherTypeBean voucherTypeBean;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    private Long vhId;
    @Autowired
    @Qualifier("paymentService")
    private PaymentService paymentService;
    @Autowired
    @Qualifier("chequeService")
    private ChequeService chequeService;
    private String fromAccnumnar;
    private Fund toFundCode;
    @Autowired
    @Qualifier("fundFlowService")
    private FundFlowService fundFlowService;
    private CGeneralLedger generalled;
    
    private CChartOfAccounts chartofAccountsList;
    private List<CGeneralLedger> generalLedgerDesList = new ArrayList<CGeneralLedger>();
    private List<CGeneralLedger> generalLedgerSrcList = new ArrayList<CGeneralLedger>();
    private Vouchermis vouchermis = new Vouchermis();
    private static Logger logger = Logger.getLogger(ContraBTBAction.class);
    private String toAccnumnar;
    private CVoucherHeader voucherHeader2;
    private CVoucherHeader voucherHeaderDes;
    private CVoucherHeader voucherHeader4;
    private ChartOfAccounts chartOfAccounts;
    @Autowired
    private CreateVoucher createVoucher;
    @Autowired
    @Qualifier("instrumentHeaderService")
    private InstrumentHeaderService instrumentHeaderService;

    @Autowired
    @Qualifier("contraJournalVoucherService")
    private ContraJournalVoucherService contraJournalVoucherService;

    @Autowired
    @Qualifier("contraBTBActionHelper")
    private ContraBTBActionHelper contraBTBActionHelper;

    @Autowired
    private EgovCommon egovCommon;

    @Override
    public void prepare() {
        super.prepare();
        ModeOfCollectionMap = new LinkedHashMap<String, String>();
        // ModeOfCollectionMap.put(MDC_CHEQUE, MDC_CHEQUE);
        ModeOfCollectionMap.put(MDC_OTHER, MDC_OTHER);
        final List<CChartOfAccounts> glCodeList = persistenceService
                .findAllBy("from CChartOfAccounts coa where coa.purposeId=8 and coa.classification=4 and coa.isActiveForPosting=true order by coa.glcode ");
        addDropdownData("interFundList", glCodeList);
        LoadAjaxedDropDowns();
    }

    @SkipValidation
    @Action(value = "/contra/contraBTB-beforeEdit")
    public String beforeEdit() {
        prepareForViewModifyReverse();
        return EDIT;
    }

    @SkipValidation
    @Action(value = "/contra/contraBTB-beforeReverse")
    public String beforeReverse() {
        prepareForViewModifyReverse();
        return REVERSE;
    }

    @SkipValidation
    @Action(value = "/contra/contraBTB-beforeView")
    public String beforeView() {
        prepareForViewModifyReverse();
        return VIEW;
    }

    /**
     * @param chequeService the chequeService to set
     */
    public void setChequeService(final ChequeService chequeService) {
        this.chequeService = chequeService;
    }

    /**
     * @return new page
     * @throws ValidationException <br>
     * The details of transaction is<br>
     * <ol>
     * <li>addToIntrument- cheque information</li>
     * <li>createVoucher- JV creation</li>
     * <li>update instrument -link jv and cheque</li>
     * <li>addTocontraJournalVoucher</li>
     * <li>post to ledger</li>
     * </ol>
     */
    @ValidationErrorPage(value = NEW)
    @Action(value = "/contra/contraBTB-create")
    public String create() throws ValidationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting Bank to Bank Transfer ...");
        try {
            getHibObjectsFromContraBean();
            if (egovCommon.isShowChequeNumber())
                if (contraBean.getModeOfCollection().equals(MDC_CHEQUE))
                    validateChqNumber(contraBean.getChequeNumber(), contraVoucher.getFromBankAccountId().getId(), voucherHeader);
            voucherHeader = contraBTBActionHelper.create(contraBean, contraVoucher, voucherHeader);
            addActionMessage("Bank to Bank Transfer "+ getText("transaction.success") + " with Voucher number: "+ voucherHeader.getVoucherNumber());
            setVhId(voucherHeader.getId());
            LoadAjaxedDropDowns();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Completed Bank to Bank Transfer .");
        } catch (final ValidationException e)
        {
            LoadAjaxedDropDowns();
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e)
        {
            LoadAjaxedDropDowns();
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
        return SUCCESS;
    }

    /**
     * @return edit page <br>
     * Modifies the Bank to Bank Transfer ContraVoucher . &nbsp; &nbsp; &nbsp; Details of transaction:<br>
     * <ol>
     * <li>update the VoucherHeader for all mis attribute changes</li>
     * <li>If Account Numbers changed
     * <ol type="a">
     * <li>cancel the Instrument</li>
     * <li>create new Instrument</li>
     * <li>delete ledger</li>
     * <li>recreate Ledger</li>
     * </ol>
     * <li>If only instrument date and amount is changed update only instrument</li>
     * </ol>
     */
    @Action(value = "/contra/contraBTB-edit")
    public String edit() {
        validateFields();
        getHibObjectsFromContraBean();
        final CVoucherHeader oldVoucher = voucherService.updateVoucherHeader(voucherHeader, voucherTypeBean);

        if (oldVoucher.getRefvhId() != null) {
            voucherHeader2 = oldVoucher;
            voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where voucherNumber=?",
                    oldVoucher.getVoucherNumber());
        } else {
            voucherHeader2 = (CVoucherHeader) persistenceService.find("from CVoucherHeader where voucherNumber=?",
                    oldVoucher.getVoucherNumber());
            voucherHeader4 = new CVoucherHeader();

        }
        if (voucherHeader2 != null)
            if (contraBean.getToFundId() != null && voucherHeader.getFundId().getId().equals(contraBean.getToFundId()))
                throw new ValidationException(
                        Arrays
                                .asList(new ValidationError(
                                        "Same Fund Is Not Allowed Cancel this and create New One",
                                        "Same Fund Is Not Allowed Cancel this and create New One")));
        CVoucherHeader oldVoucher2 = null;
        Fund toFund = null;
        if (voucherHeader2 != null) {
            toFund = (Fund) persistenceService.find("from Fund where id=?", contraBean.getToFundId());

            voucherTypeBean
                    .setVoucherName(FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND);
            voucherHeader4.setFundId(toFund);
            voucherHeader4.setCgvn(voucherHeader2.getCgvn());
            // This fix is for Phoenix Migration.voucherHeader4.setId(voucherHeader2.getId());
            voucherHeader4.setVoucherNumber(voucherHeader2.getVoucherNumber());
            voucherHeader4.setType(voucherHeader2.getType());
            voucherHeader4.setVouchermis(voucherHeader2.getVouchermis());
            voucherHeader4.setVoucherDate(voucherDate);

            voucherTypeBean
                    .setVoucherName(FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND);
            oldVoucher2 = voucherService.updateVoucherHeader(voucherHeader4,
                    voucherTypeBean);

        }

        final ContraJournalVoucher oldContraVoucher = (ContraJournalVoucher) persistenceService
                .find("from ContraJournalVoucher where voucherHeaderId.id=?",
                        oldVoucher.getId());
        ContraJournalVoucher oldContraVoucher2 = null;
        if (voucherHeader2 != null)
            oldContraVoucher2 = (ContraJournalVoucher) persistenceService.find(
                    "from ContraJournalVoucher where voucherHeaderId.id=?",
                    oldVoucher2.getId());
        final List exludeStatusList = getExcludeStatusListForInstruments();
        final InstrumentVoucher instrumentVoucher = (InstrumentVoucher) persistenceService
                .find(
                        "from InstrumentVoucher iv where iv.instrumentHeaderId.statusId not in (?) and voucherHeaderId.id=?",
                        exludeStatusList.get(0), oldVoucher.getId());
        InstrumentVoucher instrumentVoucher2 = null;
        if (voucherHeader2 != null)
            instrumentVoucher2 = (InstrumentVoucher) persistenceService
                    .find(
                            "from InstrumentVoucher iv where iv.instrumentHeaderId.statusId not in (?) and voucherHeaderId.id=?",
                            exludeStatusList.get(0), oldVoucher2.getId());
        if (instrumentVoucher == null) {
            LOGGER
                    .error("System Error :Instrument is not linked with voucher ");
            throw new ApplicationRuntimeException(
                    " System Error :Instrument is not linked with voucher  ");
        }
        final InstrumentHeader oldInstrumentHeader = instrumentVoucher
                .getInstrumentHeaderId();
        if (!oldContraVoucher.getFromBankAccountId().getId().toString().equals(
                contraBean.getFromBankAccountId())
                || !oldContraVoucher.getToBankAccountId().getId().toString()
                        .equals(contraBean.getToBankAccountId())) {
            instrumentService.cancelInstrument(oldInstrumentHeader);
            if (instrumentVoucher2 != null)
                instrumentService.cancelInstrument(instrumentVoucher2
                        .getInstrumentHeaderId());
            persistenceService.getSession().flush();
            if (contraBean.getModeOfCollection().equals(MDC_CHEQUE))
                if (!egovCommon.isShowChequeNumber())
                    try {
                        contraBean.setChequeNumber(chequeService
                                .nextChequeNumber(contraBean
                                        .getFromBankAccountId(), 1,
                                        voucherHeader.getVouchermis()
                                                .getDepartmentid().getId().intValue()));
                    } catch (final ApplicationRuntimeException e) {
                        throw new ValidationException(
                                Arrays
                                        .asList(new ValidationError(
                                                "Exception while getting Cheque Number  ",
                                                e.getMessage())));
                    }
                else
                    validateChqNumber(contraBean.getChequeNumber(),
                            contraVoucher.getFromBankAccountId().getId(),
                            voucherHeader);
            final List<InstrumentHeader> instrumentList = instrumentService
                    .addToInstrument(createInstruments(contraBean,
                            contraVoucher));
            updateInstrument(instrumentList.get(0), oldVoucher);
            // DepositCheque(oldVoucher, contraVoucher, instrumentList.get(0));
            persistenceService.getSession().flush();
            final ContraJournalVoucher contraVoucher = addOrupdateContraJournalVoucher(
                    oldContraVoucher, instrumentList.get(0), oldVoucher);
            if (voucherHeader2 != null) {
                final List<Map<String, Object>> createInstrumentMap = createInstrumentsForReceipt(
                        contraBean, contraVoucher);
                // set is pay cheque to 0 saying it is a receipt cheque
                createInstrumentMap.get(0).put("Is pay cheque", "0");
                final List<InstrumentHeader> instrumentList2 = instrumentService
                        .addToInstrument(createInstrumentMap);
                oldContraVoucher2 = addOrupdateContraJournalVoucher(
                        oldContraVoucher2, instrumentList2.get(0),
                        voucherHeader2);
                updateInstrument(instrumentList2.get(0), voucherHeader2);
            }

            if (contraBean.getToFundId() != null
                    && !voucherHeader.getFundId().getId().equals(
                            contraBean.getToFundId()))
                voucherHeader = createLedgerAndPostForInterfund(voucherHeader,
                        contraVoucher);
            else
                createLedgerAndPost(oldVoucher, contraVoucher);

        } else
            checkAndUpdateInstrument(oldInstrumentHeader, contraVoucher,
                    oldVoucher);
        LoadAjaxedDropDowns();

        addActionMessage(getText("transaction.success")
                + oldVoucher.getVoucherNumber());
        voucherHeader = oldVoucher;
        setVhId(voucherHeader.getId());
        return EDIT;

    }

    /**
     * @param voucherHeader
     * @param contraVoucher2
     * @return
     */
    private CVoucherHeader createLedgerAndPostForInterfund(
            final CVoucherHeader voucher, final ContraJournalVoucher contraVoucher2) {

        try {
            persistenceService.find(
                    "from Fund where id=?", contraBean.getToFundId());

            createVoucher.deleteVoucherdetailAndGL(voucherHeader);
            createVoucher.deleteVoucherdetailAndGL(voucherHeader2);

            persistenceService.getSession().flush();
            HashMap<String, Object> detailMap = null;
            List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
            final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.CREDITAMOUNT, contraBean.getAmount()
                    .toString());
            detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
            detailMap.put(VoucherConstant.GLCODE, contraVoucher
                    .getFromBankAccountId().getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.DEBITAMOUNT, contraBean.getAmount()
                    .toString());
            detailMap.put(VoucherConstant.CREDITAMOUNT, "0");
            detailMap.put(VoucherConstant.GLCODE, contraBean.getSourceGlcode());
            accountdetails.add(detailMap);
            final List<Transaxtion> transactions = createVoucher.createTransaction(null,
                    accountdetails, subledgerDetails, voucher);
            persistenceService.getSession().flush();

            Transaxtion txnList[] = new Transaxtion[transactions.size()];
            txnList = transactions.toArray(txnList);
            final SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
            if (!chartOfAccounts.postTransaxtions(txnList, formatter.format(voucher
                    .getVoucherDate())))
                throw new ValidationException(
                        Arrays
                                .asList(new ValidationError(
                                        EXCEPTION_WHILE_SAVING_DATA,
                                        TRANSACTION_FAILED)));

            accountdetails = new ArrayList<HashMap<String, Object>>();
            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.CREDITAMOUNT, contraBean.getAmount()
                    .toString());
            detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
            detailMap.put(VoucherConstant.GLCODE, contraBean
                    .getDestinationGlcode());
            accountdetails.add(detailMap);

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.DEBITAMOUNT, contraBean.getAmount()
                    .toString());
            detailMap.put(VoucherConstant.CREDITAMOUNT, "0");
            detailMap.put(VoucherConstant.GLCODE, contraVoucher
                    .getToBankAccountId().getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);

            final List<Transaxtion> transactions2 = createVoucher.createTransaction(null,
                    accountdetails, subledgerDetails, voucherHeader2);
            persistenceService.getSession().flush();
            Transaxtion txnList2[] = new Transaxtion[transactions2.size()];
            txnList2 = transactions2.toArray(txnList2);
            if (!chartOfAccounts.postTransaxtions(txnList2, formatter.format(voucherHeader2
                    .getVoucherDate())))
                throw new ValidationException(
                        Arrays
                                .asList(new ValidationError(
                                        EXCEPTION_WHILE_SAVING_DATA,
                                        TRANSACTION_FAILED)));

        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(
                    EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
        } catch (final TaskFailedException e) {
            // handle engine exception
            LOGGER.error(e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(e
                    .getMessage(), e.getMessage())));
        } catch (final SQLException e) {
            LOGGER.error(e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(
                    EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));

        } catch (final Exception e) {
            // handle engine exception
            LOGGER.error(e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(e
                    .getMessage(), e.getMessage())));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Posted to Ledger ");
        return voucher;
    }

    /**
     * @return
     */
    private List<EgwStatus> getExcludeStatusListForInstruments() {
        final List<EgwStatus> exList = new ArrayList<EgwStatus>();
        final EgwStatus statusId = instrumentService
                .getStatusId(FinancialConstants.INSTRUMENT_CANCELLED_STATUS);
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

   
    public ContraJournalVoucher getContraVoucher() {
        return contraVoucher;
    }

    public Map<String, String> getFromBankBranchMap() {
        return fromBankBranchMap;
    }

    /**
     * @return the genericDao
     */

    public String getMode() {
        return mode;
    }

    @Override
    public StateAware getModel() {
        voucherHeader = (CVoucherHeader) super.getModel();
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA);
        if (voucherHeader.getName() == null
                || voucherHeader.getName().isEmpty())
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

    @SkipValidation
    @Action(value = "/contra/contraBTB-newform")
    public String newform() {
        try {

            reset();
            LoadAjaxedDropDowns();
            loadDefalutDates();
            final Date currDate = new Date();
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            contraBean.setChequeDate(sdf.format(currDate));
            voucherDate = sdf.parse(sdf.format(currDate));
        } catch (ParseException e) {


        }
        return NEW;
    }

    public String reset() {
        voucherHeader.reset();
        contraBean.reset();
        setAmount(null);
        return NEW;
    }

    @ValidationErrorPage(value = "reverse")
    @Action(value = "/contra/contraBTB-reverse")
    public String reverse() {
        CVoucherHeader reversalVoucher = null;
        final HashMap<String, Object> reversalVoucherMap = new HashMap<String, Object>();
        reversalVoucherMap.put("Original voucher header id", voucherHeader
                .getId());
        reversalVoucherMap.put("Reversal voucher type", "Receipt");
        reversalVoucherMap.put("Reversal voucher name", "Contra");
        try {
            reversalVoucherMap.put("Reversal voucher date", sdf
                    .parse(getReversalVoucherDate()));
        } catch (final ParseException e1) {
            LOGGER.error("Error in reverse" + e1.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "reversalVocuherDate",
                    "reversalVocuherDate.notinproperformat")));
        }
        reversalVoucherMap.put("Reversal voucher number",
                getReversalVoucherNumber());
        final List<HashMap<String, Object>> reversalList = new ArrayList<HashMap<String, Object>>();
        reversalList.add(reversalVoucherMap);
        try {
            reversalVoucher = createVoucher.reverseVoucher(reversalList);
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error("Error in reverse" + e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "Failed while Reversing", "Failed while Reversing")));
        } catch (final ParseException e) {
            LOGGER.error("Error in reverse" + e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "Date is not in proper Format",
                    "Date is not in proper Format")));

        }
        LoadAjaxedDropDowns();
        addActionMessage(getText("contra.reverse.transaction.success")
                + reversalVoucher.getVoucherNumber());
        // This fix is for Phoenix Migration.voucherHeader.setId(reversalVoucher.getId());
        setVhId(reversalVoucher.getId());
        return REVERSE;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public void setButton(final String button) {
        this.button = button;
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

    public boolean checkIfInterFund() {
        if (voucherHeader.getFundId() != null) {
            contraBean.setFromFundId(voucherHeader.getFundId().getId());
            if (contraBean.getToFundId() != null && contraBean.getFromFundId() != null
                    && !contraBean.getFromFundId().equals(contraBean.getToFundId()))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public void setInstrumentService(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public void setModeOfCollectionMap(
            final Map<String, String> modeOfCollectionMap) {
        ModeOfCollectionMap = modeOfCollectionMap;
    }

    public void setToBankBranchMap(final Map<String, String> toBankBranchMap) {
        this.toBankBranchMap = toBankBranchMap;
    }

    /**
     * @param vhId the vhId to set
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
            if (getReversalVoucherDate() == null)
                addFieldError("reversalVocuherDate",
                        getText("reversalVocuherDate.required"));
            else
                try {
                    sdf.parse(getReversalVoucherDate());
                    checkMandatoryField("voucherNumber",
                            getReversalVoucherNumber(), "voucherNumberRequired");
                } catch (final ParseException e) {
                    LOGGER.error("Error in validate>>>>" + e.getMessage());
                    addFieldError("reversalVocuherDate",
                            getText("reversalVocuherDate.notinproperformat"));
                } catch (final ValidationException e) {
                    LOGGER.error("Error in validate----" + e.getMessage());
                    addFieldError("reversalVocuherNumber",
                            getText("reversalVocuherDate.required"));
                }

        } else {
            if (contraBean.getFromBankId() == null
                    || contraBean.getFromBankId().equals("-1"))
                addFieldError("contraBean.fromBankId",
                        getText("fromBankId.required"));
            if (contraBean.getToBankId() == null
                    || contraBean.getToBankId().equals("-1"))
                addFieldError("contraBean.toBankId()",
                        getText("toBankId.required"));

            if (contraBean.getFromBankAccountId() == null
                    || contraBean.getFromBankAccountId().equals("-1"))
                addFieldError("contraBean.frombankAccountId",
                        getText("fromBankAccountId.required"));
            if (contraBean.getToBankAccountId() == null
                    || contraBean.getToBankAccountId().equals("-1"))
                addFieldError("contraBean.toBankAccountId()",
                        getText("toBankAccountId.required"));
            /*
             * if (contraBean.getToFundId() != null && !contraBean.getToFundId().equals("-1")) { final Fund toFund = (Fund)
             * persistenceService.find("from Fund where id=?", contraBean.getToFundId()); if
             * (!toFund.getCode().equalsIgnoreCase("03")) if (contraBean.getToDepartment() == null ||
             * contraBean.getToDepartment().equals("-1")) addFieldError("contraBean.contraBean.toDepartment()",
             * getText("toDepartment.required")); }
             */
            if (voucherHeader.getVouchermis().getDepartmentid() == null
                    || voucherHeader.getVouchermis().getDepartmentid().getId() == null) {
                addFieldError("voucherHeader.vouchermis.departmentid.id",
                        getText("fromDepartment.required"));
            }
            if (voucherHeader.getVouchermis().getFunction() == null
                    || voucherHeader.getVouchermis().getFunction().getId() == null) {
                addFieldError("voucherHeader.vouchermis.departmentid.id",
                        getText("fromFunction.required"));
            }
            if (egovCommon.isShowChequeNumber()
                    || contraBean.getModeOfCollection().equals(MDC_OTHER)) {
                if (contraBean.getChequeNumber() == null
                        || contraBean.getChequeNumber().isEmpty())
                    addFieldError("contraBean.chequeNumber",
                            getText("ChequeNumber.required"));

                if (contraBean.getChequeDate() == null
                        || contraBean.getChequeDate().isEmpty())
                    addFieldError("contraBean.chequeDate",
                            getText("fromChequeDate.required"));
            }
            if (checkIfInterFund()) {
                if (contraBean.getDestinationGlcode() == null
                        || contraBean.getDestinationGlcode().equals("-1"))
                    addFieldError("contraBean.destinationGlcode",
                            getText("destinationGlcode.required"));
                if (contraBean.getSourceGlcode() == null
                        || contraBean.getSourceGlcode().equals("-1"))
                    addFieldError("contraBean.sourceGlcode",
                            getText("sourceGlcode.required"));
                if (contraBean.getToDepartment() == null
                        || contraBean.getToDepartment().equals("-1"))
                    addFieldError("contraBean.contraBean.toDepartment()",
                            getText("toDepartment.required"));
            }

            if (getAmount() == null)
                addFieldError("amount", getText("amount.required"));
            else
                try {
                    contraBean.setAmount(new BigDecimal(getAmount()));

                    /*
                     * if Voucherdate is of previous years dont check bank balance else{ if (new
                     * BigDecimal(contraBean.getFromBankBalance()).compareTo(contraBean.getAmount()) == -1) {
                     * addActionError(getText( "contra.insufficient.bankbalance", new String[] { "" +
                     * contraBean.getFromBankBalance() })); } }
                     */
                } catch (final NumberFormatException e) {
                    LOGGER.error("Error in validate" + e.getMessage());
                    addFieldError("amount", getText("amount.nonnumeric"));

                }
            if (contraBean.getFromBankAccountId() != null
                    && !contraBean.getFromBankAccountId().equals("-1")
                    && contraBean.getToBankAccountId() != null
                    && !contraBean.getToBankAccountId().equals("-1"))
                if (contraBean.getFromBankAccountId().equalsIgnoreCase(
                        contraBean.getToBankAccountId()))
                    addFieldError("contraBean.fromBankId",
                            getText("same.Account.Transfer.notAllowed"));
            getHibObjectsFromContraBean();
            LoadAjaxedDropDowns();

        }

    }

    private ContraJournalVoucher addOrupdateContraJournalVoucher(
            final ContraJournalVoucher cjv, final InstrumentHeader ih,
            final CVoucherHeader vh) {
        cjv.setInstrumentHeaderId(ih);
        cjv.setVoucherHeaderId(vh);
        getHibObjectsFromContraBean();
        cjv.setFromBankAccountId(contraVoucher.getFromBankAccountId());
        cjv.setToBankAccountId(contraVoucher.getToBankAccountId());
        if (cjv.getId() != null) {
            contraJournalVoucherService.applyAuditing(cjv);
            contraJournalVoucherService.update(cjv);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Contra Journal Voucher Updated");
        } else {
            contraJournalVoucherService.applyAuditing(cjv);
            contraJournalVoucherService.persist(cjv);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Contra Journal Voucher created");
        }
        return cjv;
    }

    private CVoucherHeader callCreateVoucher(final CVoucherHeader voucher,
            final ContraJournalVoucher contraVoucher) {
        try {
            final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
            // update ContraBTB source path
            headerDetails
                    .put(VoucherConstant.SOURCEPATH,
                            "/EGF/contra/contraBTB!beforeView.action?voucherHeader.id=");
            if (voucherHeader.getFundId().getCode().equalsIgnoreCase("03")) {
                final Department department = (Department) persistenceService.find("from Department where code=?", "Z");
                headerDetails.remove(VoucherConstant.DEPARTMENTCODE);
                headerDetails.put(VoucherConstant.DEPARTMENTCODE, department.getCode());
            }
            HashMap<String, Object> detailMap = null;
            final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
            final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.CREDITAMOUNT, contraBean.getAmount()
                    .toString());
            detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
            detailMap.put(VoucherConstant.GLCODE, contraVoucher
                    .getFromBankAccountId().getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.DEBITAMOUNT, contraBean.getAmount()
                    .toString());
            detailMap.put(VoucherConstant.CREDITAMOUNT, "0");
            detailMap.put(VoucherConstant.GLCODE, contraVoucher
                    .getToBankAccountId().getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);
            voucherHeader = createVoucher.createVoucher(headerDetails, accountdetails,
                    subledgerDetails);

        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(
                    EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(e
                    .getMessage(), e.getMessage())));
        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } catch (final Exception e) {
            // handle engine exception
            LOGGER.error(e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(e
                    .getMessage(), e.getMessage())));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Posted to Ledger " + voucherHeader.getId());
        return voucherHeader;

    }

    /**
     * Create inter fund Transfer
     *
     * @param voucher
     * @param contraVoucher
     * @return
     */

    private CVoucherHeader callCreateVoucherForInterFund(
            final CVoucherHeader voucher,
            final ContraJournalVoucher contraVoucher) {
        try {
            final Fund toFund = (Fund) persistenceService.find("from Fund where id=?", contraBean.getToFundId());
            Department toDepartment = new Department();
            if (contraBean.getToDepartment() != null && !contraBean.getToDepartment().equals("-1"))
                toDepartment = (Department) persistenceService.find("from Department where id=?", contraBean.getToDepartment()
                        .longValue());
            // validateInterFundAccount(voucherHeader.getFundId(),toFund);
            final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();

            headerDetails.put(VoucherConstant.VOUCHERNAME,
                    FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND);
            if (voucherHeader.getFundId().getCode().equalsIgnoreCase("03")) {
                final Department department = (Department) persistenceService.find("from Department where code=?", "Z");
                headerDetails.remove(VoucherConstant.DEPARTMENTCODE);
                headerDetails.put(VoucherConstant.DEPARTMENTCODE, department.getCode());
            }
            // update ContraBTB source path
            headerDetails
                    .put(VoucherConstant.SOURCEPATH,
                            "/EGF/contra/contraBTB!beforeView.action?voucherHeader.id=");

            HashMap<String, Object> detailMap = null;
            List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
            final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.CREDITAMOUNT, contraBean.getAmount()
                    .toString());
            detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
            detailMap.put(VoucherConstant.GLCODE, contraVoucher
                    .getFromBankAccountId().getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);
            toFundCode = voucherHeader.getFundId();

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.FUNDCODE, toFund.getCode());
            detailMap.put(VoucherConstant.DEBITAMOUNT, contraBean.getAmount()
                    .toString());
            detailMap.put(VoucherConstant.CREDITAMOUNT, "0");
            detailMap.put(VoucherConstant.GLCODE, contraBean.getSourceGlcode()); // chang
            // e
            // here
            accountdetails.add(detailMap);
            voucherHeader = createVoucher.createVoucher(headerDetails, accountdetails,
                    subledgerDetails);

            // update ContraBTB source path
            // headerDetails.put(VoucherConstant.SOURCEPATH,
            // "/EGF/contra/contraBTB!beforeView.action?voucherHeader.id=");

            accountdetails = new ArrayList<HashMap<String, Object>>();
            // overriding voucherName
            headerDetails.put(VoucherConstant.VOUCHERNAME,
                    FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND);
            headerDetails.put(VoucherConstant.VOUCHERNAME, voucherHeader
                    .getName());
            headerDetails.put(VoucherConstant.FUNDCODE, toFund.getCode());
            if (toFund.getCode().equalsIgnoreCase("03")) {
                final Department department = (Department) persistenceService.find("from Department where code=?", "Z");
                headerDetails.remove(VoucherConstant.DEPARTMENTCODE);
                headerDetails.put(VoucherConstant.DEPARTMENTCODE, department.getCode());
            } else {
                headerDetails.remove(VoucherConstant.DEPARTMENTCODE);
                headerDetails.put(VoucherConstant.DEPARTMENTCODE, toDepartment == null ? "" : toDepartment.getCode());
            }
            headerDetails.remove(VoucherConstant.SCHEMECODE);
            headerDetails.remove(VoucherConstant.SUBSCHEMECODE);
            headerDetails.remove(VoucherConstant.FUNDSOURCECODE);
            headerDetails.remove(VoucherConstant.DIVISIONID);
            headerDetails.remove(VoucherConstant.FUNCTIONARYCODE);
            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.CREDITAMOUNT, contraBean.getAmount()
                    .toString());
            detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
            detailMap.put(VoucherConstant.GLCODE, contraBean
                    .getDestinationGlcode());
            accountdetails.add(detailMap);

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.DEBITAMOUNT, contraBean.getAmount()
                    .toString());
            detailMap.put(VoucherConstant.CREDITAMOUNT, "0");
            detailMap.put(VoucherConstant.GLCODE, contraVoucher
                    .getToBankAccountId().getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);
            voucherHeader2 = createVoucher.createVoucher(headerDetails, accountdetails,
                    subledgerDetails);

        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(
                    EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e
                    .getMessage(), e.getMessage())));
        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (final Exception e) {
            // handle engine exception
            LOGGER.error(e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(e
                    .getMessage(), e.getMessage())));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Posted to Ledger " + voucherHeader.getId());
        return voucherHeader;

    }

    private boolean checkAndUpdateInstrument(
            final InstrumentHeader oldInstrumentHeader,
            final ContraJournalVoucher contraVoucher2,
            final CVoucherHeader oldVoucher) {
        Date newInstrumentDate = null;
        boolean updateInstrument = false;
        try {

            if (contraBean.getModeOfCollection().equalsIgnoreCase(MDC_CHEQUE)) {

                if (oldInstrumentHeader.getInstrumentNumber() != null) {
                    if (!oldInstrumentHeader.getInstrumentNumber()
                            .equalsIgnoreCase(contraBean.getChequeNumber())) {
                        instrumentService.cancelInstrument(oldInstrumentHeader);
                        persistenceService.getSession().flush();
                        validateChqNumber(contraBean.getChequeNumber(),
                                contraVoucher2.getFromBankAccountId().getId(),
                                oldVoucher);
                        final List<InstrumentHeader> instrumentList = instrumentService
                                .addToInstrument(createInstruments(contraBean,
                                        contraVoucher2));
                        updateInstrument(instrumentList.get(0), oldVoucher);
                        // DepositCheque(oldVoucher, contraVoucher,
                        // instrumentList.get(0));
                        persistenceService.getSession().flush();
                        final ContraJournalVoucher contraVoucher = addOrupdateContraJournalVoucher(
                                contraVoucher2, instrumentList.get(0),
                                oldVoucher);
                        createLedgerAndPost(oldVoucher, contraVoucher);
                    } else {
                        newInstrumentDate = Constants.DDMMYYYYFORMAT2
                                .parse(contraBean.getChequeDate());
                        if (!(oldInstrumentHeader.getInstrumentDate()
                                .compareTo(newInstrumentDate) == 0)) {
                            oldInstrumentHeader
                                    .setInstrumentDate(newInstrumentDate);
                            updateInstrument = true;
                        }
                        if (!(oldInstrumentHeader.getInstrumentAmount()
                                .compareTo(contraBean.getAmount()) == 0)) {
                            oldInstrumentHeader.setInstrumentAmount(contraBean
                                    .getAmount());
                            updateInstrument = true;
                        }
                        updateInstrument = true;

                    }
                } else {// if earlier it is Other and now it is cheque then
                    // reset
                    // transaction number and date
                    instrumentService.cancelInstrument(oldInstrumentHeader);
                    persistenceService.getSession().flush();
                    if (!egovCommon.isShowChequeNumber())
                        try {
                            contraBean
                                    .setChequeNumber(chequeService
                                            .nextChequeNumber(contraBean
                                                    .getFromBankAccountId(), 1,
                                                    voucherHeader
                                                            .getVouchermis()
                                                            .getDepartmentid()
                                                            .getId().intValue()));
                        } catch (final ApplicationRuntimeException e) {
                            LOGGER.error(e.getMessage(), e);
                            throw new ValidationException(
                                    Arrays
                                            .asList(new ValidationError(
                                                    "Exception while getting Cheque Number  ",
                                                    e.getMessage())));
                        }
                    validateChqNumber(contraBean.getChequeNumber(),
                            contraVoucher2.getFromBankAccountId().getId(),
                            oldVoucher);
                    final List<InstrumentHeader> instrumentList = instrumentService
                            .addToInstrument(createInstruments(contraBean,
                                    contraVoucher2));
                    updateInstrument(instrumentList.get(0), oldVoucher);
                    // DepositCheque(oldVoucher, contraVoucher,
                    // instrumentList.get(0));
                    persistenceService.getSession().flush();
                    final ContraJournalVoucher contraVoucher = addOrupdateContraJournalVoucher(
                            contraVoucher2, instrumentList.get(0), oldVoucher);
                    createLedgerAndPost(oldVoucher, contraVoucher);

                }
            } else if (contraBean.getModeOfCollection().equalsIgnoreCase(
                    MDC_OTHER))
                // if earlier it is cheque and now it is other then reset
                // instrment number and set transaction number and date
                if (oldInstrumentHeader.getInstrumentNumber() != null) {
                    instrumentService.cancelInstrument(oldInstrumentHeader);
                    persistenceService.getSession().flush();
                    final List<InstrumentHeader> instrumentList = instrumentService
                            .addToInstrument(createInstruments(contraBean,
                                    contraVoucher2));
                    updateInstrument(instrumentList.get(0), oldVoucher);
                    // DepositCheque(oldVoucher, contraVoucher,
                    // instrumentList.get(0));
                    persistenceService.getSession().flush();
                    final ContraJournalVoucher contraVoucher = addOrupdateContraJournalVoucher(
                            contraVoucher2, instrumentList.get(0), oldVoucher);
                    createLedgerAndPost(oldVoucher, contraVoucher);
                } else {
                    // if earlier it is other and and now also other just update
                    newInstrumentDate = Constants.DDMMYYYYFORMAT2
                            .parse(contraBean.getChequeDate());
                    oldInstrumentHeader.setInstrumentDate(null);
                    oldInstrumentHeader.setTransactionDate(newInstrumentDate);
                    updateInstrument = true;

                    if (!(contraBean.getAmount().compareTo(
                            new BigDecimal(oldInstrumentHeader
                                    .getInstrumentAmount().toString())) == 0)) {
                        oldInstrumentHeader.setInstrumentAmount(contraBean
                                .getAmount());
                        oldInstrumentHeader.setTransactionNumber(contraBean
                                .getChequeNumber());
                        updateInstrument = true;
                    }

                }

        } catch (final ParseException e) {
            LOGGER.error(e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage());
        }

        if (updateInstrument) {
            instrumentHeaderService.update(oldInstrumentHeader);
            // logic for second instrument
            if (voucherHeader2 != null) {
                final List exludeStatusList = getExcludeStatusListForInstruments();
                final InstrumentVoucher instrumentVoucher2 = (InstrumentVoucher) persistenceService
                        .find(
                                "from InstrumentVoucher iv where iv.instrumentHeaderId.statusId not in (?) and voucherHeaderId.id=?",
                                exludeStatusList.get(0), voucherHeader2.getId());

                final InstrumentHeader oldInstrumentHeader2 = instrumentVoucher2
                        .getInstrumentHeaderId();
                oldInstrumentHeader2.setBankAccountId(oldInstrumentHeader
                        .getBankAccountId());
                oldInstrumentHeader2.setBankBranchName(oldInstrumentHeader
                        .getBankBranchName());
                oldInstrumentHeader2.setBankId(oldInstrumentHeader.getBankId());
                oldInstrumentHeader2.setDetailKeyId(oldInstrumentHeader
                        .getDetailKeyId());
                oldInstrumentHeader2.setDetailTypeId(oldInstrumentHeader
                        .getDetailTypeId());
                oldInstrumentHeader2.setInstrumentAmount(oldInstrumentHeader
                        .getInstrumentAmount());
                oldInstrumentHeader2.setInstrumentDate(oldInstrumentHeader
                        .getInstrumentDate());
                oldInstrumentHeader2.setInstrumentNumber(oldInstrumentHeader
                        .getInstrumentNumber());
                oldInstrumentHeader2.setInstrumentType(oldInstrumentHeader
                        .getInstrumentType());
                oldInstrumentHeader2.setIsPayCheque("0");
                oldInstrumentHeader2.setPayee(oldInstrumentHeader.getPayee());
                oldInstrumentHeader2.setPayTo(oldInstrumentHeader.getPayTo());
                oldInstrumentHeader2.setTransactionDate(oldInstrumentHeader
                        .getTransactionDate());
                oldInstrumentHeader2.setTransactionNumber(oldInstrumentHeader
                        .getTransactionNumber());
                oldInstrumentHeader2.setTransactionNumber(oldInstrumentHeader
                        .getSurrendarReason());
                oldInstrumentHeader2.setStatusId(oldInstrumentHeader2
                        .getStatusId());
                instrumentHeaderService.update(oldInstrumentHeader2);
            }
            createLedgerAndPost(oldVoucher, contraVoucher2);
        }
        return updateInstrument;
    }

    /**
     * @param oldInstrumentHeader
     * @param oldInstrumentHeader2
     */
    private List<Map<String, Object>> createInstruments(final ContraBean cBean,
            final ContraJournalVoucher cVoucher) {
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();

        Date dt = null;

        iMap.put("Instrument amount", Double.valueOf(cBean.getAmount()
                .toString()));

        iMap.put("Bank code", contraVoucher.getFromBankAccountId()
                .getBankbranch().getBank().getCode());
        iMap.put("Bank branch name", cVoucher.getFromBankAccountId()
                .getBankbranch().getBranchaddress1());
        iMap.put("Bank account id", cVoucher.getFromBankAccountId().getId());

        if (cBean.getModeOfCollection().equalsIgnoreCase(MDC_CHEQUE)) {
            if (!egovCommon.isShowChequeNumber()) {

                try {
                    iMap
                            .put("Instrument number", chequeService
                                    .nextChequeNumber(cVoucher
                                            .getFromBankAccountId().getId()
                                            .toString(), 1, voucherHeader
                                            .getVouchermis().getDepartmentid()
                                            .getId().intValue()));
                } catch (final ApplicationRuntimeException e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new ValidationException(Arrays
                            .asList(new ValidationError(
                                    "Exception while getting Cheque Number  ",
                                    e.getMessage())));
                }

                iMap.put("Instrument date", new Date());
            } else {
                iMap.put("Instrument number", cBean.getChequeNumber());
                try {
                    dt = sdf.parse(contraBean.getChequeDate());
                } catch (final ParseException e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new ValidationException(Arrays
                            .asList(new ValidationError(
                                    "Exception while formatting ChequeDate ",
                                    "TRANSACTION_FAILED")));
                }
                iMap.put("Instrument date", dt);
            }

            iMap.put("Instrument type",
                    FinancialConstants.INSTRUMENT_TYPE_CHEQUE);

        } else {

            iMap.put("Transaction number", cBean.getChequeNumber());
            try {
                dt = sdf.parse(contraBean.getChequeDate());
            } catch (final ParseException e) {
                LOGGER.error(e.getMessage(), e);
                throw new ValidationException(Arrays
                        .asList(new ValidationError(
                                "Exception while formatting ChequeDate ",
                                "TRANSACTION_FAILED")));
            }
            iMap.put("Transaction date", dt);
            // change this to advice type later
            iMap
                    .put("Instrument type",
                            FinancialConstants.INSTRUMENT_TYPE_BANK);
        }
        iMap.put("Is pay cheque", "1");
        iList.add(iMap);
        return iList;
    }

    private List<Map<String, Object>> createInstrumentsForReceipt(
            final ContraBean cBean, final ContraJournalVoucher cVoucher) {
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();

        Date dt = null;

        iMap.put("Instrument amount", Double.valueOf(cBean.getAmount()
                .toString()));

        iMap.put("Bank code", contraVoucher.getToBankAccountId()
                .getBankbranch().getBank().getCode());
        iMap.put("Bank branch name", cVoucher.getToBankAccountId()
                .getBankbranch().getBranchaddress1());
        iMap.put("Bank account id", cVoucher.getToBankAccountId().getId());

        if (cBean.getModeOfCollection().equalsIgnoreCase(MDC_CHEQUE)) {
            if (!egovCommon.isShowChequeNumber()) {

                try {
                    iMap
                            .put("Instrument number", chequeService
                                    .nextChequeNumber(cVoucher
                                            .getToBankAccountId().getId()
                                            .toString(), 1, voucherHeader
                                            .getVouchermis().getDepartmentid()
                                            .getId().intValue()));
                } catch (final ApplicationRuntimeException e) {
                    throw new ValidationException(Arrays
                            .asList(new ValidationError(
                                    "Exception while getting Cheque Number  ",
                                    e.getMessage())));
                }

                iMap.put("Instrument date", new Date());
            } else {
                iMap.put("Instrument number", cBean.getChequeNumber());
                try {
                    dt = sdf.parse(contraBean.getChequeDate());
                } catch (final ParseException e) {
                    throw new ValidationException(Arrays
                            .asList(new ValidationError(
                                    "Exception while formatting ChequeDate ",
                                    "TRANSACTION_FAILED")));
                }
                iMap.put("Instrument date", dt);
            }

            iMap.put("Instrument type",
                    FinancialConstants.INSTRUMENT_TYPE_CHEQUE);

        } else {

            iMap.put("Transaction number", cBean.getChequeNumber());
            try {
                dt = sdf.parse(contraBean.getChequeDate());
            } catch (final ParseException e) {
                throw new ValidationException(Arrays
                        .asList(new ValidationError(
                                "Exception while formatting ChequeDate ",
                                "TRANSACTION_FAILED")));
            }
            iMap.put("Transaction date", dt);
            // change this to advice type later
            iMap
                    .put("Instrument type",
                            FinancialConstants.INSTRUMENT_TYPE_BANK);
        }
        iMap.put("Is pay cheque", "0");
        iList.add(iMap);
        return iList;
    }

    private void createLedgerAndPost(final CVoucherHeader voucher,
            final ContraJournalVoucher contraVoucher) {

        try {
            if (voucherHeader2 != null)
                createLedgerAndPostForInterfund(voucher, contraVoucher);
            else {
                createVoucher.deleteVoucherdetailAndGL(voucher);
                persistenceService.getSession().flush();
                HashMap<String, Object> detailMap = null;
                final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
                final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

                detailMap = new HashMap<String, Object>();
                detailMap.put(VoucherConstant.CREDITAMOUNT, contraBean
                        .getAmount().toString());
                detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
                detailMap.put(VoucherConstant.GLCODE, contraVoucher
                        .getFromBankAccountId().getChartofaccounts()
                        .getGlcode());
                accountdetails.add(detailMap);

                detailMap = new HashMap<String, Object>();
                detailMap.put(VoucherConstant.DEBITAMOUNT, contraBean
                        .getAmount().toString());
                detailMap.put(VoucherConstant.CREDITAMOUNT, "0");
                detailMap.put(VoucherConstant.GLCODE, contraVoucher
                        .getToBankAccountId().getChartofaccounts().getGlcode());
                accountdetails.add(detailMap);
                final List<Transaxtion> transactions = createVoucher.createTransaction(
                        null, accountdetails, subledgerDetails, voucher);
                persistenceService.getSession().flush();

                Transaxtion txnList[] = new Transaxtion[transactions.size()];
                txnList = transactions.toArray(txnList);
                final SimpleDateFormat formatter = new SimpleDateFormat(
                        DD_MMM_YYYY);
                if (!chartOfAccounts.postTransaxtions(txnList, formatter.format(voucher
                        .getVoucherDate())))
                    throw new ValidationException(Arrays
                            .asList(new ValidationError(
                                    EXCEPTION_WHILE_SAVING_DATA,
                                    TRANSACTION_FAILED)));
            }
        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(
                    EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
        } catch (final TaskFailedException e) {
            // handle engine exception
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e
                    .getMessage(), e.getMessage())));
        } catch (final SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(
                    EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));

        } catch (final Exception e) {
            // handle engine exception
            LOGGER.error(e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(e
                    .getMessage(), e.getMessage())));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Posted to Ledger ");
    }

    @SuppressWarnings("unchecked")
    private List<Bankaccount> getAccountNumbers(final Integer branchId,
            final Integer fundId, final String typeOfAccount) {
        List<Bankaccount> accountNumbersList = new ArrayList<Bankaccount>();
        typeOfAccount.split(",");
        if (branchId != null)
            // as per 1781 story
            accountNumbersList = persistenceService
                    .findAllBy(
                            "from Bankaccount account where account.bankbranch.id=? and account.fund.id=?  and account.isactive=true ",
                            branchId, fundId);
        return accountNumbersList;
    }

    @SuppressWarnings("unchecked")
    private Map getBankBranches(final Integer fundId, final String typeOfAccount) {
        typeOfAccount.split(",");
        final Map<String, Object> bankBrmap = new LinkedHashMap();
        if (fundId != null) {
            final List<Object[]> bankBranch = persistenceService
                    .findAllBy(
                            "select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname "
                                    + " FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount "
                                    + " where  bank.isactive=true  and bankBranch.isactive=true and bankaccount.isactive=true and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id"
                                    + " and bankaccount.fund.id=?", fundId);
            for (final Object[] element : bankBranch)
                bankBrmap.put(element[0].toString(), element[1].toString());
        }
        return bankBrmap;
    }

    /**
     * this is same as addRelatedEntity() Since this model is different do it manually
     */
    private void getHibObjectsFromContraBean() {
        final String bankQry = "from Bankaccount where id=?";
        if (contraBean != null && contraBean.getFromBankAccountId() != null && !contraBean.getFromBankAccountId().equals("-1"))
            contraVoucher.setFromBankAccountId((Bankaccount) persistenceService.find(bankQry,
                    Long.valueOf(contraBean.getFromBankAccountId())));
        if (contraBean != null && contraBean.getToBankAccountId() != null
                && !contraBean.getFromBankAccountId().equals("-1"))
            contraVoucher.setToBankAccountId((Bankaccount) persistenceService.find(bankQry,
                    Long.valueOf(contraBean.getToBankAccountId())));
    }

    /*
     * private String getHibDepartmentFromEmployeeView(){ final Integer deptQry="from EmployeeView where employeename=?";
     * if(employeeView!= null && employeeView.Department getDeptId()!=null ) { departmentId.setTodeptId((employeeview))
     * persistenceService.find(deptQry,Integer.) } }
     */

    /**
     * if Bank Account selected(in case of validation errors or view ) Bank Account list else return empty list
     */
    private void loadAccountNumbers() {

        if (contraVoucher != null
                && contraVoucher.getFromBankAccountId() != null)
            addDropdownData("fromAccNumList", getAccountNumbers(contraVoucher
                    .getFromBankAccountId().getBankbranch().getId(), contraBean
                    .getFromFundId(), "RECEIPTS_PAYMENTS,RECEIPTS"));
        else if (contraBean.getFromBankId() != null
                && !contraBean.getFromBankId().equals("-1")) {
            final String fromBankId = contraBean.getFromBankId();

            final String[] split = fromBankId.split("-");
            if (split[1] != null && !split[1].isEmpty())
                if (contraBean.getFromFundId() != null
                        && contraBean.getFromFundId() != -1)

                    addDropdownData("fromAccNumList", getAccountNumbers(Integer
                            .valueOf(split[1]), Integer.valueOf(contraBean
                            .getFromFundId()), "RECEIPTS_PAYMENTS,RECEIPTS"));
                else
                    addDropdownData("fromAccNumList", Collections.EMPTY_LIST);
        } else
            addDropdownData("fromAccNumList", Collections.EMPTY_LIST);
        if (contraVoucher != null && contraVoucher.getToBankAccountId() != null)
            addDropdownData("toAccNumList", getAccountNumbers(contraVoucher
                    .getToBankAccountId().getBankbranch().getId(), contraBean
                    .getToFundId(), "RECEIPTS_PAYMENTS,PAYMENTS"));
        else if (contraBean.getToBankId() != null
                && !contraBean.getToBankId().equals("-1")) {
            final String toBankId = contraBean.getToBankId();
            if (LOGGER.isInfoEnabled())
                LOGGER.info(contraBean.getFromFundId());
            final String[] split = toBankId.split("-");
            if (split[1] != null && !split[1].isEmpty())
                addDropdownData("toAccNumList", getAccountNumbers(Integer
                        .valueOf(split[1]), contraBean.getToFundId(),
                        "RECEIPTS_PAYMENTS,PAYMENTS"));
        } else
            addDropdownData("toAccNumList", Collections.EMPTY_LIST);
    }

    /**
     * Adds empty Map and List for Banks And AccountNumbers for the new action <br>
     * or Adds Banks and AccountNubers which are Selected through ajax for view, modify and reverse action
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
        egovCommon.setPersistenceService(persistenceService);
        egovCommon.setFundFlowService(fundFlowService);
        if (contraVoucher != null
                && contraVoucher.getFromBankAccountId() != null) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(voucherHeader.getVoucherDate());
            BigDecimal fromBalance;
            try {
                fromBalance = egovCommon.getAccountBalance(voucherHeader
                        .getVoucherDate(), contraVoucher.getFromBankAccountId()
                        .getId());
            } catch (final Exception e) {
                LOGGER.error(e.getMessage());
                fromBalance = BigDecimal.valueOf(-1);
            }

            contraBean.setFromBankBalance(fromBalance.setScale(2).toString());

        } else
            contraBean.setFromBankBalance(null);
        if (contraVoucher != null && contraVoucher.getToBankAccountId() != null) {
            BigDecimal toBalance;
            try {
                toBalance = egovCommon.getAccountBalance(voucherHeader
                        .getVoucherDate(), contraVoucher.getToBankAccountId()
                        .getId());
            } catch (final Exception e) {
                LOGGER.error(e.getMessage());
                toBalance = BigDecimal.valueOf(-1);
            }
            contraBean.setToBankBalance(toBalance.setScale(2).toString());
        } else
            contraBean.setToBankBalance(null);

    }

    /**
     * if Bank is selected(in case of validation errors or view )BankAndBranch map else return empty map
     */
    @SuppressWarnings("unchecked")
    private void loadBankBranch() {
        if (voucherHeader.getFundId() != null
                && voucherHeader.getFundId().getId() != null) {
            fromBankBranchMap = getBankBranches(voucherHeader.getFundId()
                    .getId(), "RECEIPTS_PAYMENTS,RECEIPTS");
            toBankBranchMap = getBankBranches(contraBean.getToFundId(),
                    "RECEIPTS_PAYMENTS,PAYMENTS");
        } else {
            fromBankBranchMap = Collections.EMPTY_MAP;
            toBankBranchMap = Collections.EMPTY_MAP;
        }
    }

    /**
     * set all the values to beans used in form for display
     */

    private void prepareForViewModifyReverse() {
        voucherHeader = (CVoucherHeader) persistenceService.find(
                "from CVoucherHeader where id=?", voucherHeader.getId());
        if (voucherHeader.getRefvhId() != null) {
            voucherHeaderDes = voucherHeader;
            voucherHeader = (CVoucherHeader) persistenceService.find(
                    "from CVoucherHeader where id =?", voucherHeader.getRefvhId());
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("voucherHeader.fundId.id"
                    + voucherHeader.getFundId().getId());
        generalLedgerSrcList = persistenceService.findAllBy(
                "from CGeneralLedger where voucherHeaderId = ?", voucherHeader);
        contraVoucher = (ContraJournalVoucher) persistenceService.find(
                "from ContraJournalVoucher where voucherHeaderId=?",
                voucherHeader);
        for (final CGeneralLedger generalled : generalLedgerSrcList)
            if (!generalled.getGlcode().equals(
                    contraVoucher.getFromBankAccountId().getChartofaccounts()
                            .getGlcode()))
                contraBean.setSourceGlcode(generalled.getGlcode());
        contraBean.setFromFundId(voucherHeader.getFundId().getId());
        contraBean.setFromBankAccountId(contraVoucher.getFromBankAccountId()
                .getId().toString());
        fromAccnumnar = contraVoucher.getFromBankAccountId().getNarration();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("fromAccnumnar.............." + fromAccnumnar);
        toAccnumnar = contraVoucher.getToBankAccountId().getNarration();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("toAccnumnar.............." + toAccnumnar);
        if (voucherHeaderDes != null) {
            generalLedgerDesList = persistenceService.findAllBy(
                    "from CGeneralLedger where voucherHeaderId = ?",
                    voucherHeaderDes);
            for (final CGeneralLedger generalled : generalLedgerDesList)
                if (!generalled.getGlcode().equalsIgnoreCase(contraVoucher.getToBankAccountId().getChartofaccounts()
                        .getGlcode()))
                    contraBean.setDestinationGlcode(generalled.getGlcode());
        } else {
            generalLedgerDesList = persistenceService.findAllBy(
                    "from CGeneralLedger where voucherHeaderId.refvhId = ?",
                    voucherHeader.getId());
            for (final CGeneralLedger generalled : generalLedgerDesList)
                if (!generalled.getGlcode().equalsIgnoreCase(contraVoucher.getToBankAccountId().getChartofaccounts()
                        .getGlcode()))
                    contraBean.setDestinationGlcode(generalled.getGlcode());
        }

        contraBean.setToBankAccountId(contraVoucher.getToBankAccountId()
                .getId().toString());
        final String fromBankAndBranchId = contraVoucher.getFromBankAccountId()
                .getBankbranch().getBank().getId().toString()
                + "-"
                + contraVoucher.getFromBankAccountId().getBankbranch().getId()
                        .toString();
        final String toBankAndBranchId = contraVoucher.getToBankAccountId()
                .getBankbranch().getBank().getId().toString()
                + "-"
                + contraVoucher.getToBankAccountId().getBankbranch().getId()
                        .toString();

        contraBean.setFromBankId(fromBankAndBranchId);
        contraBean.setToBankId(toBankAndBranchId);
        contraBean.setToFundId(contraVoucher.getToBankAccountId().getFund()
                .getId());
        final List exludeStatusList = getExcludeStatusListForInstruments();
        final InstrumentVoucher instrumentVoucher = (InstrumentVoucher) persistenceService
                .find(
                        "from InstrumentVoucher iv where iv.instrumentHeaderId.statusId not in (?) and  voucherHeaderId=?",
                        exludeStatusList.get(0), voucherHeader);
        if (instrumentVoucher == null) {
            LOGGER
                    .error("System Error :Instrument is not linked with voucher ");
            throw new ApplicationRuntimeException(
                    " System Error :Instrument is not linked with voucher  ");
        }
        final InstrumentHeader instrumentHeader = instrumentVoucher
                .getInstrumentHeaderId();

        contraBean.setAmount(instrumentHeader.getInstrumentAmount());
        setAmount(contraBean.getAmount().setScale(2).toString());
        if (instrumentHeader.getTransactionNumber() != null) {
            contraBean.setModeOfCollection(MDC_OTHER);
            contraBean.setChequeNumber(instrumentHeader.getTransactionNumber());

            final String chequeDate = Constants.DDMMYYYYFORMAT2
                    .format(instrumentHeader.getTransactionDate());
            contraBean.setChequeDate(chequeDate);
        } else {
            contraBean.setModeOfCollection(MDC_CHEQUE);
            contraBean.setChequeNumber(instrumentHeader.getInstrumentNumber());
            final String chequeDate = Constants.DDMMYYYYFORMAT2
                    .format(instrumentHeader.getInstrumentDate());
            contraBean.setChequeDate(chequeDate);
        }
        LoadAjaxedDropDowns();

    }

    /**
     * @param id
     */
    private void updateInstrument(final InstrumentHeader ih,
            final CVoucherHeader vh) {
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
        iMap.put("Instrument header", ih);
        iMap.put("Voucher header", vh);
        iList.add(iMap);
        instrumentService.updateInstrumentVoucherReference(iList);
    }

    private void validateChqNumber(final String chqNo, final Long bankaccId,
            final CVoucherHeader voucherHeader) {
        if (!instrumentService.isChequeNumberValid(chqNo, bankaccId,
                voucherHeader.getVouchermis().getDepartmentid().getId().intValue(), null))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "Invalid cheque number", "Invalid cheque number")));

    }

    /**
     *
     * @param fromFund
     * @param toFund Checks whether inter fund transfer account code is mapped for funds
     */
    /*
     * private void validateInterFundAccount(Fund fromFund, Fund toFund) { List<ValidationError> errors=new
     * ArrayList<ValidationError>(); if(fromFund.getChartofaccountsByPayglcodeid()==null ) errors.add(new
     * ValidationError("contrabtb.interfund.accountcode.notmapped.fromfund", "Interfund AccountCode not mapped for FromFund"));
     * if( toFund.getChartofaccountsByPayglcodeid()==null) errors.add(new
     * ValidationError("contrabtb.interfund.accountcode.notmapped.tofund", "Interfund AccountCode not mapped for ToFund"));
     * if(errors.size()>0) throw new ValidationException(errors); }
     */
    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public String getToAccnumnar() {
        return toAccnumnar;
    }

    public EgovCommon getEgovCommon() {
        return egovCommon;
    }

    public void setToAccnumnar(final String toAccnumnar) {
        this.toAccnumnar = toAccnumnar;
    }

    public String getSourceGlcode() {
        return sourceGlcode;
    }

    public void setSourceGlcode(final String sourceGlcode) {
        this.sourceGlcode = sourceGlcode;
    }

    public String getDestinationGlcode() {
        return destinationGlcode;
    }

    public void setDestinationGlcode(final String destinationGlcode) {
        this.destinationGlcode = destinationGlcode;
    }

    public VoucherService getVoucherService() {
        return voucherService;
    }

    public void setPaymentheader(final Paymentheader paymentheader) {
    }

    public void setPaymentService(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Integer getUserDepartment() throws ParseException {
        return paymentService.getAssignment().getDepartment().getId().intValue();
    }

    public void setFundFlowService(final FundFlowService fundFlowService) {
        this.fundFlowService = fundFlowService;
    }

    public String getFromAccnumnar() {
        return fromAccnumnar;
    }

    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(final Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    public void setFromAccnumnar(final String fromAccnumnar) {
        this.fromAccnumnar = fromAccnumnar;
    }

    public ChartOfAccounts getChartOfAccounts() {
        return chartOfAccounts;
    }

    public void setChartOfAccounts(ChartOfAccounts chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }
}
