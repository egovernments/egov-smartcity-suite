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
package org.egov.egf.web.actions.voucher;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.BillsAccountingService;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Relation;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.service.ChartOfAccountDetailService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.masters.model.AccountEntity;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.voucher.PreApprovedVoucher;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.services.bills.BillsService;
import org.egov.services.contra.ContraService;
import org.egov.services.voucher.PreApprovedActionHelper;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;

@Results({
        @Result(name = "editVoucher", type = "redirectAction", location = "journalVoucherModify-beforeModify", params = {
                "namespace",
                "/voucher", "voucherId", "${voucherId}" }),
        @Result(name = "view", location = "preApprovedVoucher-view.jsp"),
        @Result(name = PreApprovedVoucherAction.VOUCHEREDIT, location = "preApprovedVoucher-voucheredit.jsp"),
        @Result(name = "billview", location = "preApprovedVoucher-billview.jsp"),
        @Result(name = "voucherview", location = "preApprovedVoucher-voucherview.jsp"),
        @Result(name = "message", location = "preApprovedVoucher-message.jsp")
})
@ParentPackage("egov")
public class PreApprovedVoucherAction extends GenericWorkFlowAction {
    private final static String FORWARD = "Forward";
    private static final String EGF = "EGF";
    private static final String EMPTY_STRING = "";
    private static final long serialVersionUID = 1L;
    private VoucherService voucherService;
    private CVoucherHeader voucherHeader = new CVoucherHeader();
    private EgBillregister egBillregister = new EgBillregister();
    private SimpleWorkflowService<CVoucherHeader> voucherWorkflowService;
    protected WorkflowBean workflowBean = new WorkflowBean();
    protected EisCommonService eisCommonService;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private EgovCommon egovCommon;
    @Autowired
    @Qualifier("preApprovedActionHelper")
    private PreApprovedActionHelper preApprovedActionHelper;
    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;
    @Autowired
    private VoucherTypeForULB voucherTypeForULB;
    private List<EgBillregister> preApprovedVoucherList;
    protected List<String> headerFields = new ArrayList<String>();
    protected List<String> mandatoryFields = new ArrayList<String>();
    protected EisUtilService eisService;
    @Autowired
    private static BillsService billsService;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private BillsAccountingService billsAccountingService;
    @Autowired
    private BillsService billsManager;
    
    @Autowired
    private ChartOfAccountDetailService chartOfAccountDetailService;
    
    private static final Logger LOGGER = Logger.getLogger(PreApprovedVoucherAction.class);
    protected FinancialYearHibernateDAO financialYearDAO;
    private final PreApprovedVoucher preApprovedVoucher = new PreApprovedVoucher();
    private List<PreApprovedVoucher> billDetailslist;
    private List<PreApprovedVoucher> subLedgerlist;
    @Autowired
    private CreateVoucher createVoucher;
    private ContraJournalVoucher contraVoucher;
    private static final String ERROR = "error";

    private static final String BILLID = "billid";
    protected static final String VOUCHEREDIT = "voucheredit";
    private static final String VHID = "vhid";
    private static final String CGN = "cgn";
    private static final String VOUCHERQUERY = " from CVoucherHeader where id=?";
    private static final String VOUCHERQUERYBYCGN = " from CVoucherHeader where cgn=?";
    private static final String ACCDETAILTYPEQUERY = " from Accountdetailtype where id=?";
    private static final String ACTIONNAME = "actionName";
    private String values = "", from = "";
    private String methodName = "";
    private Integer departmentId;
    private String type;
    private String wfitemstate;
    private String voucherNumber;
    private Boolean displayVoucherNumber = true;
    private String action = "";
    SimpleWorkflowService<ContraJournalVoucher> contraWorkflowService;
    private Map<String, Object> billDetails;
    // private Long vhid;
    private VoucherHelper voucherHelper;
    private JournalVoucherModifyAction journalvouchermodify;
    private boolean showVoucherDate;
    private ScriptService scriptService;
    private String mode = "";
    protected Long voucherId;
    private EgBillregister billRegister;
    @Autowired
    private EgovMasterDataCaching masterDataCache;
    private String cutOffDate;
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date;

    @Override
    public StateAware getModel() {
        return voucherHeader;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("departmentList", Collections.EMPTY_LIST);
        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);
    }

    @SkipValidation
    public String list() {
        final EgwStatus egwStatus = egwStatusDAO.getStatusByModuleAndCode("SBILL", "Approved");
        getHeaderMandateFields();
        if (isFieldMandatory("department"))
            preApprovedVoucherList = getPersistenceService()
                    .findAllBy(
                            " from EgBillregister br where br.status=? and br.egBillregistermis.egDepartment.id=? and ( br.egBillregistermis.voucherHeader is null or br.egBillregistermis.voucherHeader in (from CVoucherHeader vh where vh.status =4 )) ",
                            egwStatus, getCurrentDepartment().getId());
        else
            preApprovedVoucherList = getPersistenceService()
                    .findAllBy(
                            " from EgBillregister br where br.status=? and ( br.egBillregistermis.voucherHeader is null or br.egBillregistermis.voucherHeader in (from CVoucherHeader vh where vh.status =4 )) ",
                            egwStatus);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(preApprovedVoucherList);
        return "list";
    }

    @SkipValidation
    @Action(value = "/voucher/preApprovedVoucher-voucher")
    public String voucher() {
        List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "DataEntryCutOffDate");
        if (cutOffDateconfigValue != null && !cutOffDateconfigValue.isEmpty()) {
            try {
                date = df.parse(cutOffDateconfigValue.get(0).getValue());
                cutOffDate = formatter.format(date);
            } catch (ParseException e) {

            }
        }
        egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister where id=?",
                Long.valueOf(parameters.get(BILLID)[0]));
        voucherHeader = egBillregister.getEgBillregistermis().getVoucherHeader();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("egBillregister==" + egBillregister);
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "pjv_saveasworkingcopy_enabled");
        final String pjv_wc_enabled = appList.get(0).getValue();
        // loading aprover user info
        preApprovedVoucher.setVoucherDate(new Date());
        type = egBillregister.getExpendituretype();
        getHeaderMandateFields();
        String purposeValueVN = "";
        try {
            final List<AppConfigValues> configValues = appConfigValuesService
                    .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "VOUCHERDATE_FROM_UI");

            for (final AppConfigValues appConfigVal : configValues)
                purposeValueVN = appConfigVal.getValue();
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Appconfig value for VOUCHERDATE_FROM_UI is not defined in the system");
        }
        if (purposeValueVN.equals("Y"))
            showVoucherDate = true;

        // loadApproverUser(type);
        if ("Y".equals(pjv_wc_enabled)) {
            try {
                // loading the bill detail info.
                getMasterDataForBillVoucher();
            } catch (final Exception e) {
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exp", e.getMessage()));
                throw new ValidationException(errors);
            }
            return VOUCHEREDIT;
        } else {
            try {
                // loading the bill detail info.
                getMasterDataForBill();
            } catch (final Exception e) {
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exp", e.getMessage()));
                throw new ValidationException(errors);
            }
            action = "save";
            return "billview";
        }

    }

    public List<String> getValidActions() {

        List<String> validActions = Collections.emptyList();
        if (!action.equalsIgnoreCase("save"))
            if (null == getModel() || null == getModel().getId() || getModel().getCurrentState().getValue().endsWith("NEW")) {
                validActions = Arrays.asList(FORWARD);
            } else {
                if (getModel().getCurrentState() != null) {
                    validActions = this.customizedWorkFlowService.getNextValidActions(getModel()
                            .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                            getAdditionalRule(), getModel().getCurrentState().getValue(),
                            getPendingActions(), getModel().getCreatedDate());
                }
            }
        else {
            CVoucherHeader model = new CVoucherHeader();
            List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                    "DataEntryCutOffDate");
            if (cutOffDateconfigValue != null && !cutOffDateconfigValue.isEmpty()) {
                if (null == model || null == model.getId() || model.getCurrentState().getValue().endsWith("NEW")) {
                    validActions = Arrays.asList(FORWARD, FinancialConstants.CREATEANDAPPROVE);
                } else {
                    if (model.getCurrentState() != null) {
                        validActions = this.customizedWorkFlowService.getNextValidActions(model
                                .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                                getAdditionalRule(), model.getCurrentState().getValue(),
                                getPendingActions(), model.getCreatedDate());
                    }
                }
            } else {
                if (null == model || null == model.getId() || model.getCurrentState().getValue().endsWith("NEW")) {
                    validActions = Arrays.asList(FORWARD);
                } else {
                    if (model.getCurrentState() != null) {
                        validActions = this.customizedWorkFlowService.getNextValidActions(model
                                .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                                getAdditionalRule(), model.getCurrentState().getValue(),
                                getPendingActions(), model.getCreatedDate());
                    }
                }
            }
        }
        return validActions;
    }

    public String getNextAction() {
        WorkFlowMatrix wfMatrix = null;
        if (!action.equalsIgnoreCase("save")) {
            if (getModel().getId() != null) {
                if (getModel().getCurrentState() != null) {
                    wfMatrix = this.customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
                            getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(), getModel()
                                    .getCurrentState().getValue(),
                            getPendingActions(), getModel()
                                    .getCreatedDate());
                } else {
                    wfMatrix = this.customizedWorkFlowService.getWfMatrix(getModel().getStateType(),
                            getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                            State.DEFAULT_STATE_VALUE_CREATED, getPendingActions(), getModel()
                                    .getCreatedDate());
                }
            }
        } else {
            CVoucherHeader model = new CVoucherHeader();
            if (model.getId() != null) {
                if (model.getCurrentState() != null) {
                    wfMatrix = this.customizedWorkFlowService.getWfMatrix(model.getStateType(),
                            getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(), model
                                    .getCurrentState().getValue(),
                            getPendingActions(), model
                                    .getCreatedDate());
                } else {
                    wfMatrix = this.customizedWorkFlowService.getWfMatrix(model.getStateType(),
                            getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                            State.DEFAULT_STATE_VALUE_CREATED, getPendingActions(), model
                                    .getCreatedDate());
                }
            }
        }
        return wfMatrix == null ? "" : wfMatrix.getNextAction();
    }

    @SkipValidation
    @Action(value = "/voucher/preApprovedVoucher-loadvoucher")
    public String loadvoucher() {
        String result = null;
        voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
        voucherId = Long.valueOf(parameters.get(VHID)[0]);
        boolean ismodifyJv = false;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("voucherHeader==" + voucherHeader);
        if (voucherHeader != null && voucherHeader.getState() != null)
            if (!validateOwner(voucherHeader.getState())) {
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exp", "Invalid User"));
                throw new ValidationException(errors);
            }
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "pjv_saveasworkingcopy_enabled");
        final String pjv_wc_enabled = appList.get(0).getValue();

        type = billsService.getBillTypeforVoucher(voucherHeader);
        if (null == type)
            // when the work flow started internally then define the type value to "default".systems using the API
            // CreateVoucher.createPreApprovedVoucher()
            type = "default";
        try {
            // loading the bill detail info.
            getMasterDataForBillVoucher();
        } catch (final Exception e) {
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        getHeaderMandateFields();
        getSession().put("voucherId", parameters.get(VHID)[0]);

        if (voucherHeader.getState() != null && voucherHeader.getState().getValue().contains("Rejected"))
            if (voucherHeader.getModuleId() == null) {
                final EgBillregistermis billMis = (EgBillregistermis) persistenceService.find(
                        "from EgBillregistermis where voucherHeader.id=?", voucherHeader.getId());
                if (billMis != null) {
                    final State billWorkFlowState = billMis.getEgBillregister().getState();
                    if (billWorkFlowState == null) {
                        result = "editVoucher";
                        ismodifyJv = true;
                    }

                } else if (voucherHeader.getName().equalsIgnoreCase(FinancialConstants.JOURNALVOUCHER_NAME_GENERAL)) {
                    type = FinancialConstants.JOURNALVOUCHER_NAME_GENERAL;
                    result = "editVoucher";
                    ismodifyJv = true;
                }
            }
        // loadApproverUser(type);
        if (!ismodifyJv)
            if ("Y".equals(pjv_wc_enabled))
                result = VOUCHEREDIT;
            else
                result = "voucherview";
        billRegister = (EgBillregister) persistenceService.find(
                "select mis.egBillregister from EgBillregistermis mis where mis.voucherHeader.id=?", voucherHeader.getId());
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private void loadApproverUser(final String type) {
        final String scriptName = "billvoucher.nextDesg";
        departmentId = voucherService.getCurrentDepartment().getId().intValue();
        final Map<String, Object> map = voucherService.getDesgByDeptAndType(type, scriptName);
        if (null == map.get("wfitemstate")) {
            // If the department is mandatory show the logged in users assigned department only.
            if (mandatoryFields.contains("department"))
                addDropdownData("departmentList", voucherHelper.getAllAssgnDeptforUser());
            else
                addDropdownData("departmentList", masterDataCache.get("egi-department"));
            addDropdownData("designationList", (List<Designation>) map.get("designationList"));
            wfitemstate = "";
        } else
            wfitemstate = map.get("wfitemstate").toString();

    }

    @SkipValidation
    @Action(value = "/voucher/preApprovedVoucher-loadvoucherview")
    public String loadvoucherview() throws ApplicationException {

        billDetails = new HashMap<String, Object>();
        if (parameters.get("from") != null && FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA.equals(parameters.get("from")[0])) {
            contraVoucher = (ContraJournalVoucher) persistenceService.find(" from ContraJournalVoucher where id=?",
                    Long.valueOf(parameters.get(VHID)[0]));
            voucherHeader = contraVoucher.getVoucherHeaderId();
            from = FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA;
        } else {
            voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
            from = FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL;
        }
        getMasterDataForBillVoucher();
        getHeaderMandateFields();
        return "view";
    }

    /*
     * @SkipValidation public List<String> getValidActions() { if
     * (FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA.equals(parameters.get("from")[0])) return null; else return null; }
     */

    @SkipValidation
    @Action(value = "/voucher/preApprovedVoucher-approve")
    public String approve() throws ApplicationException {
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
                "classpath:org/serviceconfig-Bean.xml", "classpath:org/egov/infstr/beanfactory/globalApplicationContext.xml",
                "classpath:org/egov/infstr/beanfactory/applicationContext-egf.xml" });
        voucherHeader = (CVoucherHeader) persistenceService.find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
        if (voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA)) {
            contraVoucher = (ContraJournalVoucher) persistenceService.find("from ContraJournalVoucher where id=?",
                    Long.valueOf(parameters.get("contraId")[0]));
            final ContraService contraService = (ContraService) applicationContext.getBean("contraService");
            contraWorkflowService.transition(parameters.get(ACTIONNAME)[0], contraVoucher, parameters.get("comments")[0]);
            contraService.persist(contraVoucher);
            addActionMsg(contraVoucher.getState().getValue(), contraVoucher.getState().getOwnerPosition());
        } else if (voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL)
                || voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA)) {
            voucherWorkflowService.transition(parameters.get(ACTIONNAME)[0], voucherHeader, parameters.get("comments")[0]);
            voucherService.persist(voucherHeader);
            addActionMsg(voucherHeader.getState().getValue(), voucherHeader.getState().getOwnerPosition());
        }
        methodName = "approval";
        getMasterDataForBillVoucher();
        return "view";
    }

    private void addActionMsg(final String stateValue, final Position pos) {
        if (parameters.get(ACTIONNAME)[0].contains("approve"))
            if ("END".equals(stateValue))
                addActionMessage(getText("pjv.voucher.final.approval", new String[] { "The File has been approved" }));
            else
                addActionMessage(getText("pjv.voucher.approved",
                        new String[] { voucherService.getEmployeeNameForPositionId(pos) }));
        else if (parameters.get(ACTIONNAME)[0].contains("co_reject") || parameters.get(ACTIONNAME)[0].contains("aa_reject")
                || "END".equals(stateValue))
            addActionMessage(getText("voucher.cancelled"));
        else
            addActionMessage(getText("pjv.voucher.rejected", new String[] { voucherService.getEmployeeNameForPositionId(pos) }));

    }

    @SkipValidation
    @Action(value = "/voucher/preApprovedVoucher-loadvoucherviewByCGN")
    public String loadvoucherviewByCGN() throws ApplicationException {
        billDetails = new HashMap<String, Object>();
        voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERYBYCGN, parameters.get(CGN)[0]);
        /*
         * List<VoucherDetail> voucherDetailList=new ArrayList<VoucherDetail>(); voucherDetailList = persistenceService.findAllBy(
         * "from VoucherDetail where voucherHeaderId.id=? order by decode(debitAmount,null,0, debitAmount) desc ,decode(creditAmount,null,0, creditAmount) asc"
         * ,voucherHeader.getId()); billDetails.put("voucherDetailList", voucherDetailList);
         */
        getHeaderMandateFields();
        getMasterDataForBillVoucher();
        return "view";
    }

    @ValidationErrorPage("billview")
    @SkipValidation
    @Action(value = "/voucher/preApprovedVoucher-save")
    public String save() throws ValidationException {
        mode = "save";
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("bill id=======" + parameters.get(BILLID)[0]);
            methodName = "save";
            String voucherDate = formatter1.format(voucherHeader.getVoucherDate());
            String cutOffDate1 = null;
            // check budgetary check
            egBillregister = billsService.getBillRegisterById(Integer.valueOf(parameters.get(BILLID)[0]));
            // egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister where id=?",
            // Long.valueOf(parameters.get(BILLID)[0]));
            if (!financialYearDAO.isSameFinancialYear(egBillregister.getBilldate(), voucherHeader.getVoucherDate()))
                throw new ValidationException(
                        "Voucher could not be permitted in the current year for the Bill prepared in the previous financial year/s",
                        "Voucher could not be permitted in the current year for the Bill prepared in the previous financial year/s");
            getMasterDataForBill();
            populateWorkflowBean();
            voucherHeader = preApprovedActionHelper.createVoucherFromBill(voucherHeader, workflowBean,
                    Long.parseLong(parameters.get(BILLID)[0]), voucherNumber, voucherHeader.getVoucherDate());
            if (!cutOffDate.isEmpty() && cutOffDate != null) {
                try {
                    date = sdf.parse(cutOffDate);
                    cutOffDate1 = formatter1.format(date);
                } catch (ParseException e) {

                }
            }
            if (cutOffDate1 != null && voucherDate.compareTo(cutOffDate1) <= 0
                    && FinancialConstants.CREATEANDAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {

                if (voucherHeader.getVouchermis().getBudgetaryAppnumber() == null) {
                    addActionMessage(getText("Voucher created successfully. Voucher No : ")
                            + voucherHeader.getVoucherNumber());
                } else {
                    addActionMessage(getText("Voucher created successfully. Voucher No : ")
                            + voucherHeader.getVoucherNumber()
                            + " And "
                            + getText("budget.recheck.sucessful", new String[] { voucherHeader.getVouchermis()
                                    .getBudgetaryAppnumber() }));
                }
            } else {
                if (voucherHeader.getVouchermis().getBudgetaryAppnumber() == null) {
                    addActionMessage(getText(
                            egBillregister.getExpendituretype() + ".voucher.created",
                            new String[] { voucherHeader.getVoucherNumber(),
                                    voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition()) }));
                } else {
                    addActionMessage(getText(
                            egBillregister.getExpendituretype() + ".voucher.created",
                            new String[] { voucherHeader.getVoucherNumber(),
                                    voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition()) })
                            + " And "
                            + getText("budget.recheck.sucessful", new String[] { voucherHeader.getVouchermis()
                                    .getBudgetaryAppnumber() }));

                }
            }

        } catch (

        final ValidationException e) {
            voucher();
            mode = "";
            if (e.getErrors().get(0).getMessage() != null && !e.getErrors().get(0).getMessage().equals(StringUtils.EMPTY))
                throw new ValidationException(e.getErrors().get(0).getMessage(), e.getErrors().get(0).getMessage());
            else
                throw new ValidationException("Voucher creation failed", "Voucher creation failed");
        } catch (final ApplicationRuntimeException e) {
            voucher();
            mode = "";
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            voucher();
            mode = "";
            if (e.getCause().getClass().equals(ValidationException.class)) {

                final ValidationException s = (ValidationException) e;
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exp", s.getErrors().get(0).getMessage()));
                throw new ValidationException(errors);
            }
            LOGGER.error(e.getMessage());
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            final ValidationException s = (ValidationException) e;
            errors.add(new ValidationError("exception", s.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        }

        getHeaderMandateFields();
        displayVoucherNumber = false;
        return "billview";

    }

    @Action(value = "/voucher/preApprovedVoucher-update")
    public String update() throws ValidationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("voucher id=======" + parameters.get(VHID)[0]);
        methodName = "update";
        try {
            voucherHeader = (CVoucherHeader) voucherService.findById(Long.parseLong(parameters.get(VHID)[0]), false);
            populateWorkflowBean();
            voucherHeader = preApprovedActionHelper.sendForApproval(voucherHeader, workflowBean);
            type = billsService.getBillTypeforVoucher(voucherHeader);
            if (null == type)
                type = "default";

            if (FinancialConstants.BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                addActionMessage(getText("pjv.voucher.rejected",
                        new String[] { voucherService.getEmployeeNameForPositionId(voucherHeader.getState()
                                .getOwnerPosition()) }));
            if (FinancialConstants.BUTTONFORWARD.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                addActionMessage(getText("pjv.voucher.approved",
                        new String[] {
                                voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition()) }));
            if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                addActionMessage(getText("billVoucher.file.canceled"));
            else if (FinancialConstants.BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                if ("Closed".equals(voucherHeader.getState().getValue()))
                    addActionMessage(getText("pjv.voucher.final.approval", new String[] { "The File has been approved" }));
                else
                    addActionMessage(getText("pjv.voucher.approved",
                            new String[] { voucherService.getEmployeeNameForPositionId(voucherHeader.getState()
                                    .getOwnerPosition()) }));
            }
        } catch (final ValidationException e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }

        return "message";
    }

    protected void populateWorkflowBean() {
        workflowBean.setApproverPositionId(approverPositionId);
        workflowBean.setApproverComments(approverComments);
        workflowBean.setWorkFlowAction(workFlowAction);
        workflowBean.setCurrentState(currentState);
    }

    public String saveAsWorkingCopy() throws ValidationException {
        methodName = "saveAsWorkingCopy";
        if (parameters.get(VHID)[0].equals("")) {
            egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister where id=?",
                    Long.valueOf(parameters.get(BILLID)[0]));
            final boolean budgetcheck = voucherService.budgetaryCheck(egBillregister);
            if (budgetcheck) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("saveAsWorkingCopy===========" + billDetailslist + subLedgerlist);
                // TO-DO : User create voucher Api
                final Long vhid = billsAccountingService.createPreApprovedVoucherFromBillForPJV(
                        Integer.valueOf(parameters.get(BILLID)[0]), billDetailslist, subLedgerlist);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("voucher id=======" + vhid);
                voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, vhid);
                voucherHeader.start().withOwner(getPosition());
                addActionMessage(getText("pjv.voucher.wc.created", new String[] { voucherHeader.getVoucherNumber() }));
            } else
                addActionError(getText("pjv.budgetcheck.failed"));
        } else {
            final Long vhid = Long.valueOf(parameters.get(VHID)[0]);
            voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, vhid);
            billsAccountingService.updatePJV(voucherHeader, billDetailslist, subLedgerlist);
            addActionMessage(getText("pjv.voucher.updated"));
        }
        return "voucheredit";
    }

    public void sendForApproval() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("PreApprovedVoucherAction | sendForApproval | Start");
        if (voucherHeader.getId() == null)
            voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
        if (voucherHeader != null && voucherHeader.getState() != null)
            if (!validateOwner(voucherHeader.getState())) {
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exp", "Invalid User"));
                throw new ValidationException(errors);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucherheader==" + voucherHeader.getId() + ", actionname=" + parameters.get(ACTIONNAME)[0]);
        Integer userId = null;
        if (parameters.get("actionName")[0].contains("approve"))
            userId = parameters.get("approverUserId") != null ? Integer.valueOf(parameters.get("approverUserId")[0])
                    : ApplicationThreadLocals.getUserId().intValue();
        else
            userId = voucherHeader.getCreatedBy().getId().intValue();

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("User selected id is : " + userId);
        // voucherWorkflowService.transition(parameters.get(ACTIONNAME)[0] + "|" + userId, voucherHeader,
        // parameters.get("comments")[0]);
        voucherService.persist(voucherHeader);
    }

    public String sendForApprovalForWC() throws ValidationException {
        methodName = "sendForApprovalForWC";
        sendForApproval();
        if (parameters.get(ACTIONNAME)[0].contains("approve"))
            if ("END".equals(voucherHeader.getState().getValue()))
                addActionMessage(getText("pjv.voucher.final.approval", new String[] { "The File has been approved" }));
            else
                addActionMessage(getText("pjv.voucher.approved",
                        new String[] {
                                voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition()) }));
        else
            addActionMessage(getText("pjv.voucher.rejected",
                    new String[] { voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition()) }));
        return "message";
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public List<EgBillregister> getPreApprovedVoucherList() {
        return preApprovedVoucherList;
    }

    @Override
    public void validate() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("validate==============");
        /*
         * if(parameters.get("comments")[0]!=null && parameters.get("comments")[0].length()>1024) addFieldError("comments",
         * "Max. length exceeded");
         */
    }

    public void setEgBillregister(final EgBillregister egBillregister) {
        this.egBillregister = egBillregister;
    }

    public Department getCurrentDepartment() {
        return voucherService.getCurrentDepartment();
    }

    public String getMasterName(final String name) {
        String val = "";
        if (voucherHeader != null) {
            if (name.equals("fund") && voucherHeader.getFundId() != null)
                val = voucherHeader.getFundId().getName();
            else if (name.equals("fundsource") && voucherHeader.getVouchermis().getFundsource() != null)
                val = voucherHeader.getVouchermis().getFundsource().getName();
            else if (name.equals("department") && voucherHeader.getVouchermis().getDepartmentid() != null)
                val = voucherHeader.getVouchermis().getDepartmentid().getName();
            else if (name.equals("scheme") && voucherHeader.getVouchermis().getSchemeid() != null)
                val = voucherHeader.getVouchermis().getSchemeid().getName();
            else if (name.equals("subscheme") && voucherHeader.getVouchermis().getSubschemeid() != null)
                val = voucherHeader.getVouchermis().getSubschemeid().getName();
            else if (name.equals("field") && voucherHeader.getVouchermis().getDivisionid() != null)
                val = voucherHeader.getVouchermis().getDivisionid().getName();
            else if (name.equals("functionary") && voucherHeader.getVouchermis().getFunctionary() != null)
                val = voucherHeader.getVouchermis().getFunctionary().getName();
            else if ("narration".equals(name))
                val = voucherHeader.getDescription();
            else if ("billnumber".equals(name))
                val = ((EgBillregister) getPersistenceService().find(
                        " from EgBillregister br where br.egBillregistermis.voucherHeader=?", voucherHeader)).getBillnumber();
        } else if (name.equals("fund") && egBillregister.getEgBillregistermis().getFund() != null)
            val = egBillregister.getEgBillregistermis().getFund().getName();
        else if (name.equals("fundsource") && egBillregister.getEgBillregistermis().getFundsource() != null)
            val = egBillregister.getEgBillregistermis().getFundsource().getName();
        else if (name.equals("department") && egBillregister.getEgBillregistermis().getEgDepartment() != null)
            val = egBillregister.getEgBillregistermis().getEgDepartment().getName();
        else if (name.equals("scheme") && egBillregister.getEgBillregistermis().getScheme() != null)
            val = egBillregister.getEgBillregistermis().getScheme().getName();
        else if (name.equals("subscheme") && egBillregister.getEgBillregistermis().getSubScheme() != null)
            val = egBillregister.getEgBillregistermis().getSubScheme().getName();
        else if (name.equals("field") && egBillregister.getEgBillregistermis().getFieldid() != null)
            val = egBillregister.getEgBillregistermis().getFieldid().getName();
        else if (name.equals("functionary") && egBillregister.getEgBillregistermis().getFunctionaryid() != null)
            val = egBillregister.getEgBillregistermis().getFunctionaryid().getName();
        else if ("narration".equals(name))
            val = egBillregister.getEgBillregistermis().getNarration();
        else if ("billnumber".equals(name))
            val = egBillregister.getBillnumber();
        return val;
    }

    public String getSourcePath() {
        String sourcePath;
        if (voucherHeader != null && voucherHeader.getGeneralledger() != null && voucherHeader.getGeneralledger().size() > 0)
            sourcePath = voucherHeader.getVouchermis().getSourcePath();
        else
            sourcePath = egBillregister.getEgBillregistermis().getSourcePath();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Source Path = " + sourcePath);
        return sourcePath;
    }

    public Map<String, Object> getMasterName() throws ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getmastername===============");
        final Map<String, Object> names = new HashMap<String, Object>();
        // to get the ledger.
        final List<CGeneralLedger> gllist = getPersistenceService().findAllBy(
                " from CGeneralLedger where voucherHeaderId.id=? order by voucherlineId",
                Long.valueOf(voucherHeader.getId() + ""));
        Map<String, Object> temp = null;
        final List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
        for (final CGeneralLedger gl : gllist) {
            if (gl.getFunctionId() != null)
                names.put(
                        gl.getGlcodeId().getGlcode(),
                        ((CFunction) getPersistenceService().find(" from CFunction where id=?", Long.valueOf(gl.getFunctionId())))
                                .getName());

            // get subledger.
            final List<CGeneralLedgerDetail> gldetailList = getPersistenceService().findAllBy(
                    "from CGeneralLedgerDetail where generalLedgerId.id=?", gl.getId());
            if (gldetailList != null && !gldetailList.isEmpty()) {
                for (final CGeneralLedgerDetail gldetail : gldetailList) {
                    if (chartOfAccountDetailService.getByGlcodeIdAndDetailTypeId(gl.getGlcodeId().getId(),
                            gldetail.getDetailTypeId().getId()) != null) {
                        temp = new HashMap<String, Object>();
                        temp = getAccountDetails(gldetail.getDetailTypeId().getId(), gldetail.getDetailKeyId(), temp);
                        temp.put(Constants.GLCODE, gl.getGlcode());
                        temp.put("amount", gldetail.getAmount());
                        tempList.add(temp);
                    }
                }
                names.put("tempList", tempList);
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("voucher details ==" + names);
        return names;
    }

    public void getMasterDataForBill() throws ApplicationException {
        billDetails = new HashMap<String, Object>();
        CChartOfAccounts coa = null;
        Map<String, Object> temp = null;
        Map<String, Object> payeeMap = null;
        final List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> payeeList = new ArrayList<Map<String, Object>>();

        final List<EgBilldetails> egBillDetails = persistenceService.findAllBy("from EgBilldetails where  egBillregister.id=? ",
                egBillregister.getId());

        for (final EgBilldetails billdetails : egBillDetails) {
            temp = new HashMap<String, Object>();
            if (billdetails.getFunctionid() != null)
                temp.put(
                        Constants.FUNCTION,
                        ((CFunction) getPersistenceService().find("from CFunction where id=?",
                                Long.valueOf(billdetails.getFunctionid() + ""))).getName());
            coa = (CChartOfAccounts) getPersistenceService().find("from CChartOfAccounts where id=?",
                    Long.valueOf(billdetails.getGlcodeid() + ""));
            temp.put(Constants.GLCODE, coa.getGlcode());
            temp.put("accounthead", coa.getName());
            temp.put(Constants.DEBITAMOUNT, billdetails.getDebitamount() == null ? 0 : billdetails.getDebitamount());
            temp.put(Constants.CREDITAMOUNT, billdetails.getCreditamount() == null ? 0 : billdetails.getCreditamount());
            temp.put("billdetailid", billdetails.getId());
            tempList.add(temp);

            for (final EgBillPayeedetails payeeDetails : billdetails.getEgBillPaydetailes()) {
                payeeMap = new HashMap<>();
                if (chartOfAccountDetailService.getByGlcodeIdAndDetailTypeId(
                        payeeDetails.getEgBilldetailsId().getGlcodeid().longValue(),
                        payeeDetails.getAccountDetailTypeId().intValue()) != null) {
                    payeeMap = getAccountDetails(payeeDetails.getAccountDetailTypeId(), payeeDetails.getAccountDetailKeyId(),
                            payeeMap);
                    payeeMap.put(Constants.GLCODE, coa.getGlcode());
                    payeeMap.put(Constants.DEBITAMOUNT,
                            payeeDetails.getDebitAmount() == null ? 0 : payeeDetails.getDebitAmount());
                    payeeMap.put(Constants.CREDITAMOUNT,
                            payeeDetails.getCreditAmount() == null ? 0 : payeeDetails.getCreditAmount());
                    payeeList.add(payeeMap);
                }
                
            }
        }
        billDetails.put("tempList", tempList);
        billDetails.put("payeeList", payeeList);
    }

    public void getMasterDataForBillVoucher() throws ApplicationException {
        billDetails = new HashMap<String, Object>();
        CChartOfAccounts coa = null;
        Map<String, Object> temp = null;
        Map<String, Object> payeeMap = null;
        final List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
        final List<PreApprovedVoucher> payeeList = new ArrayList<PreApprovedVoucher>();
        final List<Long> glcodeIdList = new ArrayList<Long>();
        final List<Accountdetailtype> detailtypeIdList = new ArrayList<Accountdetailtype>();
        PreApprovedVoucher subledger = null;

        if (voucherHeader != null) {
            final List<CGeneralLedger> gllist = getPersistenceService().findAllBy(
                    " from CGeneralLedger where voucherHeaderId.id=? order by id asc", Long.valueOf(voucherHeader.getId() + ""));

            for (final CGeneralLedger gl : gllist) {
                temp = new HashMap<String, Object>();
                if (gl.getFunctionId() != null) {
                    temp.put(
                            Constants.FUNCTION,
                            ((CFunction) getPersistenceService().find("from CFunction where id=?",
                                    Long.valueOf(gl.getFunctionId()))).getName());
                    temp.put("functionid", gl.getFunctionId());
                }
                coa = (CChartOfAccounts) getPersistenceService().find("from CChartOfAccounts where glcode=?", gl.getGlcode());
                temp.put("glcodeid", coa.getId());
                glcodeIdList.add(coa.getId());
                temp.put(Constants.GLCODE, coa.getGlcode());
                temp.put("accounthead", coa.getName());
                temp.put(Constants.DEBITAMOUNT, gl.getDebitAmount() == null ? 0 : gl.getDebitAmount());
                temp.put(Constants.CREDITAMOUNT, gl.getCreditAmount() == null ? 0 : gl.getCreditAmount());
                temp.put("billdetailid", gl.getId());
                tempList.add(temp);
                for (CGeneralLedgerDetail gldetail : gl.getGeneralLedgerDetails()) {
                    if (chartOfAccountDetailService.getByGlcodeIdAndDetailTypeId(gl.getGlcodeId().getId(),
                            gldetail.getDetailTypeId().getId().intValue()) != null) {
                        subledger = new PreApprovedVoucher();
                        subledger.setGlcode(coa);
                        final Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,
                                gldetail.getDetailTypeId().getId());
                        detailtypeIdList.add(detailtype);
                        subledger.setDetailType(detailtype);
                        payeeMap = new HashMap<>();
                        payeeMap = getAccountDetails(gldetail.getDetailTypeId().getId(), gldetail.getDetailKeyId(), payeeMap);
                        subledger.setDetailKey(payeeMap.get(Constants.DETAILKEY) + "");
                        if ((payeeMap.get(Constants.DETAILKEY) + "").contains(Constants.MASTER_DATA_DELETED))
                            addActionError(Constants.VOUCHERERRORMESSAGE);
                        subledger.setDetailCode(payeeMap.get(Constants.DETAILCODE) + "");
                        subledger.setDetailKeyId(gldetail.getDetailKeyId());
                        subledger.setAmount(gldetail.getAmount());
                        subledger.setFunctionDetail(temp.get("function") != null ? temp.get("function").toString() : "");
                        if (gl.getDebitAmount() == null || gl.getDebitAmount() == 0) {
                            subledger.setDebitAmount(BigDecimal.ZERO);
                            subledger.setCreditAmount(gldetail.getAmount());
                        } else {
                            subledger.setDebitAmount(gldetail.getAmount());
                            subledger.setCreditAmount(BigDecimal.ZERO);
                        }
                        payeeList.add(subledger);
                    }
                }
            }
            /*
             * // this is for only vouchers, which do not have the bills List<VoucherDetail> voucherDetailList=new
             * ArrayList<VoucherDetail>(); voucherDetailList = persistenceService.findAllBy(
             * "from VoucherDetail where voucherHeaderId.id=? order by decode(debitAmount,null,0, debitAmount) desc ,decode(creditAmount,null,0, creditAmount) asc"
             * ,voucherHeader.getId()); billDetails.put("voucherDetailList", voucherDetailList);
             */
        } else
            for (final EgBilldetails billdetails : egBillregister.getEgBilldetailes()) {
                temp = new HashMap<String, Object>();
                if (billdetails.getFunctionid() != null) {
                    temp.put(
                            Constants.FUNCTION,
                            ((CFunction) getPersistenceService().find("from CFunction where id=?",
                                    Long.valueOf(billdetails.getFunctionid() + ""))).getName());
                    temp.put("functionid", billdetails.getFunctionid());
                }
                coa = (CChartOfAccounts) getPersistenceService().find("from CChartOfAccounts where id=?",
                        Long.valueOf(billdetails.getGlcodeid() + ""));
                temp.put("glcodeid", coa.getId());
                glcodeIdList.add(coa.getId());
                temp.put(Constants.GLCODE, coa.getGlcode());
                temp.put("accounthead", coa.getName());
                temp.put(Constants.DEBITAMOUNT, billdetails.getDebitamount() == null ? 0 : billdetails.getDebitamount());
                temp.put(Constants.CREDITAMOUNT, billdetails.getCreditamount() == null ? 0 : billdetails.getCreditamount());
                temp.put("billdetailid", billdetails.getId());
                tempList.add(temp);

                for (final EgBillPayeedetails payeeDetails : billdetails.getEgBillPaydetailes()) {
                    if (chartOfAccountDetailService.getByGlcodeIdAndDetailTypeId(
                            payeeDetails.getEgBilldetailsId().getGlcodeid().longValue(),
                            payeeDetails.getAccountDetailTypeId().intValue()) != null) {
                        subledger = new PreApprovedVoucher();
                        subledger.setGlcode(coa);
                        final Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,
                                payeeDetails.getAccountDetailTypeId());
                        detailtypeIdList.add(detailtype);
                        subledger.setDetailType(detailtype);
                        payeeMap = new HashMap<>();
                        payeeMap = getAccountDetails(payeeDetails.getAccountDetailTypeId(), payeeDetails.getAccountDetailKeyId(),
                                payeeMap);
                        subledger.setDetailKey(payeeMap.get(Constants.DETAILKEY) + "");
                        subledger.setDetailCode(payeeMap.get(Constants.DETAILCODE) + "");
                        subledger.setDetailKeyId(payeeDetails.getAccountDetailKeyId());
                        if (payeeDetails.getDebitAmount() == null) {
                            subledger.setDebitAmount(BigDecimal.ZERO);
                            subledger.setCreditAmount(payeeDetails.getCreditAmount());
                        } else {
                            subledger.setDebitAmount(payeeDetails.getDebitAmount());
                            subledger.setCreditAmount(BigDecimal.ZERO);
                        }
                        payeeList.add(subledger);
                    }
                }
            }
        billDetails.put("tempList", tempList);
        billDetails.put("subLedgerlist", payeeList);
        setupDropDownForSL(glcodeIdList);
        setupDropDownForSLDetailtype(detailtypeIdList);
    }

    public Map<String, Object> getAccountDetails(final Integer detailtypeid, final Integer detailkeyid,
            final Map<String, Object> tempMap) throws ApplicationException {
        final Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY, detailtypeid);
        tempMap.put("detailtype", detailtype.getDescription());
        tempMap.put("detailtypeid", detailtype.getId());
        tempMap.put("detailkeyid", detailkeyid);

        egovCommon.setPersistenceService(persistenceService);
        final EntityType entityType = egovCommon.getEntityType(detailtype, detailkeyid);
        if (entityType == null) {
            tempMap.put(Constants.DETAILKEY, detailkeyid + " " + Constants.MASTER_DATA_DELETED);
            tempMap.put(Constants.DETAILCODE, Constants.MASTER_DATA_DELETED);
        } else {
            tempMap.put(Constants.DETAILKEY, entityType.getName());
            tempMap.put(Constants.DETAILCODE, entityType.getCode());
        }
        return tempMap;
    }

    public void setupDropDownForSL(final List<Long> glcodeIdList) {
        List<CChartOfAccounts> glcodeList = null;
        final Query glcodeListQuery = persistenceService.getSession().createQuery(
                " from CChartOfAccounts where id in (select glCodeId from CChartOfAccountDetail) and id in  ( :IDS )");
        glcodeListQuery.setParameterList("IDS", glcodeIdList);
        glcodeList = glcodeListQuery.list();
        if (glcodeIdList.isEmpty())
            dropdownData.put("glcodeList", Collections.EMPTY_LIST);
        else
            dropdownData.put("glcodeList", glcodeList);
    }

    public void setupDropDownForSLDetailtype(final List<Accountdetailtype> detailtypeIdList) {
        dropdownData.put("detailTypeList", detailtypeIdList);
    }

    public EgBillregister getEgBillregister() {
        return egBillregister;
    }

    protected void getHeaderMandateFields() {
        final List<AppConfigValues> appConfigList = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "DEFAULTTXNMISATTRRIBUTES");

        for (final AppConfigValues appConfigVal : appConfigList) {
            final String value = appConfigVal.getValue();
            final String header = value.substring(0, value.indexOf("|"));
            headerFields.add(header);
            final String mandate = value.substring(value.indexOf("|") + 1);
            if (mandate.equalsIgnoreCase("M"))
                mandatoryFields.add(header);
        }
    }

    public boolean shouldShowHeaderField(final String field) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside shouldShowHeaderField menthod");
        if ("vouchernumber".equals(field)) {
            String vNumGenMode = "Manual";
            vNumGenMode = voucherTypeForULB.readVoucherTypes("Journal");
            if (!"Auto".equalsIgnoreCase(vNumGenMode)) {
                mandatoryFields.add("vouchernumber");
                return true;
            } else
                return false;

        } else {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Header field contains = " + headerFields.contains(field));
            return headerFields.contains(field);
        }
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    public Position getPosition() throws ApplicationRuntimeException {
        Position pos;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getPosition====" + ApplicationThreadLocals.getUserId());
        pos = eisCommonService.getPositionByUserId(ApplicationThreadLocals.getUserId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("position===" + pos.getId());
        return pos;
    }

    public String ajaxValidateDetailCode() {
        final String code = parameters.get("code")[0];
        final String index = parameters.get("index")[0];
        final Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,
                Integer.valueOf(parameters.get("detailtypeid")[0]));
        if (adt == null) {
            values = index + "~" + ERROR;
            return "result";
        }
        if (adt.getTablename().equalsIgnoreCase("EG_EMPLOYEE")) {
            final PersonalInformation information = (PersonalInformation) getPersistenceService().find(
                    " from PersonalInformation where employeeCode=? and isActive=true", code);
            if (information == null)
                values = index + "~" + ERROR;
            else
                values = index + "~" + information.getIdPersonalInformation() + "~" + information.getEmployeeFirstName();
        } else if (adt.getTablename().equalsIgnoreCase("RELATION")) {
            final Relation relation = (Relation) getPersistenceService().find(" from Relation where code=? and isactive=true",
                    code);
            if (relation == null)
                values = index + "~" + ERROR;
            else
                values = index + "~" + relation.getId() + "~" + relation.getName();
        } else if (adt.getTablename().equalsIgnoreCase("ACCOUNTENTITYMASTER")) {
            final AccountEntity accountEntity = (AccountEntity) getPersistenceService().find(
                    " from AccountEntity where code=? and isactive=true ", code);
            if (accountEntity == null)
                values = index + "~" + ERROR;
            else
                values = index + "~" + accountEntity.getId() + "~" + accountEntity.getCode();
        }
        return "result";
    }

    protected Boolean validateOwner(final State state) {
        Boolean check = false;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("validating owner for user " + ApplicationThreadLocals.getUserId());
        List<Position> positionsForUser = null;
        positionsForUser = eisService.getPositionsForUser(ApplicationThreadLocals.getUserId(), new Date());
        for (final Position pos : positionsForUser)
            if (pos.getId().equals(state.getOwnerPosition().getId())) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Valid Owner :return true");
                check = true;
            }
        return check;

    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public void setVoucherWorkflowService(final SimpleWorkflowService<CVoucherHeader> voucherWorkflowService) {
        this.voucherWorkflowService = voucherWorkflowService;
    }

    public CVoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    public void setVoucherHeader(final CVoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

    public void setBillsAccountingService(final BillsAccountingService mngr) {
        billsAccountingService = mngr;
    }

    public List<PreApprovedVoucher> getSubLedgerlist() {
        return subLedgerlist;
    }

    public void setSubLedgerlist(final List<PreApprovedVoucher> subLedgerlist) {
        this.subLedgerlist = subLedgerlist;
    }

    public List<PreApprovedVoucher> getBillDetailslist() {
        return billDetailslist;
    }

    public void setBillDetailslist(final List<PreApprovedVoucher> billDetailslist) {
        this.billDetailslist = billDetailslist;
    }

    public String getValues() {
        return values;
    }

    public void setValues(final String values) {
        this.values = values;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }

    public List<String> getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(final List<String> headerFields) {
        this.headerFields = headerFields;
    }

    public List<String> getMandatoryFields() {
        return mandatoryFields;
    }

    public void setMandatoryFields(final List<String> mandatoryFields) {
        this.mandatoryFields = mandatoryFields;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getWfitemstate() {
        return wfitemstate;
    }

    public void setWfitemstate(final String wfitemstate) {
        this.wfitemstate = wfitemstate;
    }

    public ContraJournalVoucher getContraVoucher() {
        return contraVoucher;
    }

    public void setContraVoucher(final ContraJournalVoucher contraVoucher) {
        this.contraVoucher = contraVoucher;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public Map<String, Object> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(final Map<String, Object> billDetails) {
        this.billDetails = billDetails;
    }

    public String getFinConstExpendTypeContingency() {
        return FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT;
    }

    public VoucherHelper getVoucherHelper() {
        return voucherHelper;
    }

    public void setVoucherHelper(final VoucherHelper voucherHelper) {
        this.voucherHelper = voucherHelper;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Boolean getDisplayVoucherNumber() {
        return displayVoucherNumber;
    }

    public void setDisplayVoucherNumber(final Boolean displayVoucherNumber) {
        this.displayVoucherNumber = displayVoucherNumber;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public JournalVoucherModifyAction getJournalvouchermodify() {
        return journalvouchermodify;
    }

    public void setJournalvouchermodify(
            final JournalVoucherModifyAction journalvouchermodify) {
        this.journalvouchermodify = journalvouchermodify;
    }

    public void setContraWorkflowService(final SimpleWorkflowService<ContraJournalVoucher> contraWorkflowService) {
        this.contraWorkflowService = contraWorkflowService;
    }

    public PreApprovedVoucherAction() {
        try {
            addRelatedEntity(VoucherConstant.GLCODE, CChartOfAccounts.class);
            addRelatedEntity("detailType", Accountdetailtype.class);
        } catch (final Exception e) {
            LOGGER.error("Exception in PreApprovedVoucher", e);
            throw new ApplicationRuntimeException(e.getMessage());
        }

    }

    public boolean isShowVoucherDate() {
        return showVoucherDate;
    }

    public void setShowVoucherDate(final boolean showVoucherDate) {
        this.showVoucherDate = showVoucherDate;
    }

    public FinancialYearHibernateDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setFinancialYearDAO(final FinancialYearHibernateDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public static BillsService getBillsService() {
        return billsService;
    }

    public static void setBillsService(final BillsService billsService) {
        PreApprovedVoucherAction.billsService = billsService;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    public EgBillregister getBillRegister() {
        return billRegister;
    }

    public void setBillRegister(EgBillregister billRegister) {
        this.billRegister = billRegister;
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

}