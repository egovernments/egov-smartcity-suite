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
package org.egov.egf.web.actions.voucher;

import com.exilant.GLEngine.ChartOfAccounts;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.VoucherTypeBean;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.services.voucher.JournalVoucherActionHelper;
import org.egov.services.voucher.PreApprovedActionHelper;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({
        @Result(name = "editVoucher", location = "journalVoucherModify-editVoucher.jsp"),
        @Result(name = "view", location = "journalVoucherModify-view.jsp"),
        @Result(name = "message", location = "journalVoucherModify-message.jsp")
})
public class JournalVoucherModifyAction extends BaseVoucherAction {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(JournalVoucherModifyAction.class);
    private VoucherService voucherService;
    private List<VoucherDetails> billDetailslist;
    private List<VoucherDetails> subLedgerlist;
    private String voucherNumManual;
    private String target;
    private String saveMode;
    private String message = "";
    private VoucherTypeBean voucherTypeBean;
    public static final String EXEPMSG = "Exception occured in voucher service while updating voucher ";
    private Integer departmentId;
    private String wfitemstate;
    private VoucherHelper voucherHelper;
    // private boolean isRejectedVoucher=false;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private ChartOfAccounts chartOfAccounts;
    private ChartOfAccounts engine;
    private static final String ACTIONNAME = "actionName";
    private SimpleWorkflowService<CVoucherHeader> voucherWorkflowService;
    private String methodName = "";
    private static final String VHID = "vhid";
    protected EisCommonService eisCommonService;
    private static final String VOUCHERQUERY = " from CVoucherHeader where id=?";
    private String worksVoucherRestrictedDate;
    private FinancialYearDAO financialYearDAO;

    private boolean isOneFunctionCenter;
    private ScriptService scriptService;
    @Autowired
    @Qualifier("preApprovedActionHelper")
    private PreApprovedActionHelper preApprovedActionHelper;

    @Autowired
    @Qualifier("journalVoucherActionHelper")
    private JournalVoucherActionHelper journalVoucherActionHelper;

    @Autowired
    private EgovMasterDataCaching masterDataCache;

    @SuppressWarnings("unchecked")
    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("approvaldepartmentList", Collections.EMPTY_LIST);
        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);
        setOneFunctionCenterValue();
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/voucher/journalVoucherModify-beforeModify")
    public String beforeModify() {
        String voucherHeaderId = null;
        List<Position> positionsForUser = null;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("JournalVoucherModifyAction | loadvouchers | Start ");
        if (voucherHeader != null && voucherHeader.getId() != null)
            voucherHeaderId = voucherHeader.getId().toString();
        else
            voucherHeaderId = parameters.get("voucherId")[0];
        isOneFunctionCenter = voucherHeader.getIsRestrictedtoOneFunctionCenter();
        if (voucherHeaderId != null)
            voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(voucherHeaderId));
        final Map<String, Object> vhInfoMap = voucherService.getVoucherInfo(voucherHeader.getId());
        voucherHeader = (CVoucherHeader) vhInfoMap.get(Constants.VOUCHERHEADER);
        try {
            if (voucherHeader != null && voucherHeader.getState() != null)
                if (voucherHeader.getState().getValue().contains("Rejected")) {
                    positionsForUser = eisService.getPositionsForUser(ApplicationThreadLocals.getUserId(), new Date());
                }
                else if (voucherHeader.getState().getValue().contains("Closed")) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Valid Owner :return true");
                } else if (parameters.get("showMode")[0].equalsIgnoreCase("view")) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Valid Owner :return true");
                } else
                    throw new ApplicationRuntimeException("Invalid Aceess");
            setOneFunctionCenterValue();
        } catch (final ApplicationRuntimeException e) {
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", "Invalid Aceess"));
            throw new ValidationException(errors);
        }
        billDetailslist = (List<VoucherDetails>) vhInfoMap.get(Constants.GLDEATILLIST);
        subLedgerlist = (List<VoucherDetails>) vhInfoMap.get("subLedgerDetail");
        getBillInfo();
        loadSchemeSubscheme();
        loadFundSource();
        // loadApproverUser("default");
        if (null != parameters.get("showMode") && parameters.get("showMode")[0].equalsIgnoreCase("view")) {
            return "view";
        }

        return "editVoucher";
    }

    @ValidationErrorPage(value = "editVoucher")
    public String saveAndPrint() throws Exception {
        try {
            saveMode = "saveprint";
            return update();
        } catch (final ValidationException e) {
            throw e;
        }
    }

    private void sendForApproval()
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("journalVoucherModifyAction | sendForApproval | Start");
        if (voucherHeader.getId() == null)
            voucherHeader = (CVoucherHeader) voucherService.findById(Long.parseLong(parameters.get("voucherId")[0]), false);
        populateWorkflowBean();
        voucherHeader = preApprovedActionHelper.sendForApproval(voucherHeader, workflowBean);
        if (FinancialConstants.BUTTONFORWARD.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(getText("pjv.voucher.approved",
                    new String[] { voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition()) }));
        if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            addActionMessage(getText("billVoucher.file.canceled"));
            if (!"JVGeneral".equalsIgnoreCase(voucherHeader.getName()))
                cancelBill(voucherHeader.getId());
        }
    }

    private void validateBeforeEdit(final CVoucherHeader voucherHeader) {

        try {
            financialYearDAO.getFinancialYearByDate(voucherHeader.getVoucherDate());
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }

    }

    private void addActionMsg(final String stateValue, final Position pos)
    {

        if (parameters.get(ACTIONNAME)[0].contains("approve"))
            if ("END".equals(stateValue))
                addActionMessage(getText("pjv.voucher.final.approval", new String[] { "The File has been approved" }));
            else {
                addActionMessage(getText("pjv.voucher.modified", new String[] { voucherHeader.getVoucherNumber() }));
                addActionMessage(getText("pjv.voucher.approved",
                        new String[] { voucherService.getEmployeeNameForPositionId(pos) }));
            }
        else if (parameters.get(ACTIONNAME)[0].contains("ao_reject") || parameters.get(ACTIONNAME)[0].contains("aa_reject")
                || "END".equals(stateValue))
            addActionMessage(getText("voucher.cancelled"));
        else
            addActionMessage(getText("pjv.voucher.rejected", new String[] { voucherService.getEmployeeNameForPositionId(pos) }));

    }

    @ValidationErrorPage(value = "editVoucher")
    @SuppressWarnings("deprecation")
    @Action(value = "/voucher/journalVoucherModify-update")
    public String update() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("JournalVoucherModifyAction | updateVoucher | Start");
        target = "";
        loadSchemeSubscheme();

        validateFields();
        if (voucherHeader.getId() == null)
            voucherHeader.setId(Long.valueOf(parameters.get(VHID)[0]));
        validateBeforeEdit(voucherHeader);
        CVoucherHeader oldVh = voucherHeader;
        populateWorkflowBean();
        if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            voucherHeader = (CVoucherHeader) voucherService.findById(Long.parseLong(parameters.get(VHID)[0]), false);
            sendForApproval();
            return "message";
        }
        if (null != voucherNumManual && StringUtils.isNotEmpty(voucherNumManual))
            voucherHeader.setVoucherNumber(voucherNumManual);
        voucherHeader.setIsRestrictedtoOneFunctionCenter(isOneFunctionCenter);

        removeEmptyRowsAccoutDetail(billDetailslist);
        removeEmptyRowsSubledger(subLedgerlist);

        try {
            if (!validateData(billDetailslist, subLedgerlist)) {
                voucherHeader = journalVoucherActionHelper.editVoucher(billDetailslist, subLedgerlist, voucherHeader,
                        voucherTypeBean, workflowBean, parameters.get("totaldbamount")[0]);
                target = "success";
            }
            else {
                throw new ValidationException("InValid data", "InValid data");
            }
            if (subLedgerlist.size() == 0) {
                subLedgerlist.add(new VoucherDetails());
                // setOneFunctionCenterValue();
                resetVoucherHeader();
            } else
                // setOneFunctionCenterValue();
                resetVoucherHeader();

            if (FinancialConstants.BUTTONFORWARD.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                addActionMessage(getText("pjv.voucher.approved",
                        new String[] { voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition()) }));
        } catch (final ValidationException e) {
            resetVoucherHeader();
            voucherHeader = oldVh;
            setOneFunctionCenterValue();
            if (subLedgerlist.size() == 0)
                subLedgerlist.add(new VoucherDetails());
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            resetVoucherHeader();
            voucherHeader = oldVh;
            setOneFunctionCenterValue();
            if (subLedgerlist.size() == 0)
                subLedgerlist.add(new VoucherDetails());
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("JournalVoucherModifyAction | updateVoucher | End");
        return "message";
    }

    private void cancelBill(final Long vhId) {
        final StringBuffer billQuery = new StringBuffer();
        final String statusQuery = "(select stat.id from  egw_status  stat where stat.moduletype=:module and stat.description=:description)";
        final String cancelQuery = "Update eg_billregister set billstatus=:billstatus , statusid =" + statusQuery
                + " where  id=:billId";
        String moduleType = "", description = "", billstatus = "";
        final EgBillregistermis billMis = (EgBillregistermis) persistenceService.find(
                "from  EgBillregistermis  mis where voucherHeader.id=?", vhId);

        if (billMis != null && billMis.getEgBillregister().getState() == null) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("....Cancelling Bill Associated with the Voucher....");
            billQuery.append(
                    "select bill.expendituretype,bill.id from CVoucherHeader vh,EgBillregister bill ,EgBillregistermis mis")
                    .append(" where vh.id=mis.voucherHeader and bill.id=mis.egBillregister and vh.id=" + vhId);
            final Object[] bill = (Object[]) persistenceService.find(billQuery.toString()); // bill[0] contains expendituretype
                                                                                            // and
            // bill[1] contaons billid

            if (FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY.equalsIgnoreCase(bill[0].toString())) {
                billstatus = FinancialConstants.SALARYBILL;
                description = FinancialConstants.SALARYBILL_CANCELLED_STATUS;
                moduleType = FinancialConstants.SALARYBILL;
            }
            else if (FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT.equalsIgnoreCase(bill[0].toString()))
            {
                billstatus = FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS;
                description = FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS;
                moduleType = FinancialConstants.CONTINGENCYBILL_FIN;
            }
            else if (FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE.equalsIgnoreCase(bill[0].toString()))
            {
                billstatus = FinancialConstants.SUPPLIERBILL_CANCELLED_STATUS;
                description = FinancialConstants.SUPPLIERBILL_CANCELLED_STATUS;
                moduleType = FinancialConstants.SUPPLIERBILL;
            }
            else if (FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS.equalsIgnoreCase(bill[0].toString()))
            {
                billstatus = FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS;
                description = FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS;
                moduleType = FinancialConstants.CONTRACTORBILL;
            }

            final Query billQry = persistenceService.getSession().createSQLQuery(cancelQuery.toString());
            billQry.setString("module", moduleType);
            billQry.setString("description", description);
            billQry.setString("billstatus", billstatus);
            billQry.setLong("billId", (Long) bill[1]);
            billQry.executeUpdate();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Bill Cancelled Successfully" + bill[1]);
        }
    }

    public Position getPosition() throws ApplicationRuntimeException
    {
        Position pos;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getPosition====" + ApplicationThreadLocals.getUserId());
        pos = eisCommonService.getPositionByUserId(ApplicationThreadLocals.getUserId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("position===" + pos.getId());
        return pos;
    }

    @SkipValidation
    public List<WorkflowAction> getValidActions(final String purpose) {
        final List<WorkflowAction> validButtons = new ArrayList<WorkflowAction>();
        final List<String> list = (List<String>) scriptService.executeScript("pjv.validbuttons", ScriptService.createContext(
                "eisCommonServiceBean", eisCommonService, "userId", ApplicationThreadLocals.getUserId().intValue(), "date", new Date(),
                "purpose", purpose));
        for (final Object s : list)
        {
            if ("invalid".equals(s))
                break;
            final WorkflowAction action = (WorkflowAction) getPersistenceService().find(
                    " from WorkflowAction where type='CVoucherHeader' and name=?", s.toString());
            validButtons.add(action);
        }
        return validButtons;
    }

    @SuppressWarnings("unchecked")
    private void loadApproverUser(final String type) {
        final String scriptName = "billvoucher.nextDesg";
        departmentId = voucherService.getCurrentDepartment().getId().intValue();
        final Map<String, Object> map = voucherService.getDesgByDeptAndType(type, scriptName);
        if (null == map.get("wfitemstate")) {
            // If the department is mandatory show the logged in users assigned department only.
            if (mandatoryFields.contains("department"))
                addDropdownData("approvaldepartmentList", voucherHelper.getAllAssgnDeptforUser());
            else
                addDropdownData("approvaldepartmentList", masterDataCache.get("egi-department"));
            addDropdownData("designationList", (List<Designation>) map.get("designationList"));
            wfitemstate = "";
        } else
            wfitemstate = map.get("wfitemstate").toString();

    }

    public void getBillInfo() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("JournalVoucherModify | getBillInfo | Start");
        final EgBillregister billRegister = (EgBillregister) persistenceService
                .find("from EgBillregister br where br.egBillregistermis.voucherHeader.id=" + voucherHeader.getId());
        /**
         * If its not General JV.
         */
        if (null != billRegister) {
            voucherTypeBean.setPartyBillNum(billRegister.getEgBillregistermis().getPartyBillNumber());
            voucherTypeBean.setPartyName(billRegister.getEgBillregistermis().getPayto());
            voucherTypeBean.setPartyBillDate(billRegister.getEgBillregistermis().getPartyBillDate());
            voucherTypeBean.setBillNum(billRegister.getBillnumber());
            voucherTypeBean.setBillDate(billRegister.getBilldate());
            if (null == billRegister.getEgBillregistermis().getEgBillSubType())
                voucherTypeBean.setVoucherSubType(billRegister.getExpendituretype());
            else
                voucherTypeBean.setVoucherSubType(billRegister.getEgBillregistermis().getEgBillSubType().getName());
        } else
            voucherTypeBean.setVoucherSubType(voucherHeader.getName());

    }

    public VoucherService getVoucherService() {
        return voucherService;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public List<VoucherDetails> getBillDetailslist() {
        return billDetailslist;
    }

    public VoucherTypeBean getVoucherTypeBean() {
        return voucherTypeBean;
    }

    public void setVoucherTypeBean(final VoucherTypeBean voucherTypeBean) {
        this.voucherTypeBean = voucherTypeBean;
    }

    public void setBillDetailslist(final List<VoucherDetails> billDetailslist) {
        this.billDetailslist = billDetailslist;
    }

    public List<VoucherDetails> getSubLedgerlist() {
        return subLedgerlist;
    }

    public void setSubLedgerlist(final List<VoucherDetails> subLedgerlist) {
        this.subLedgerlist = subLedgerlist;
    }

    @Override
    public String getVoucherNumManual() {
        return voucherNumManual;
    }

    @Override
    public void setVoucherNumManual(final String voucherNumManual) {
        this.voucherNumManual = voucherNumManual;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(final String target) {
        this.target = target;
    }

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(final String saveMode) {
        this.saveMode = saveMode;
    }

    public String getWfitemstate() {
        return wfitemstate;
    }

    public void setWfitemstate(final String wfitemstate) {
        this.wfitemstate = wfitemstate;
    }

    public VoucherHelper getVoucherHelper() {
        return voucherHelper;
    }

    public void setVoucherHelper(final VoucherHelper voucherHelper) {
        this.voucherHelper = voucherHelper;
    }

    public SimpleWorkflowService<CVoucherHeader> getVoucherWorkflowService() {
        return voucherWorkflowService;
    }

    public void setVoucherWorkflowService(
            final SimpleWorkflowService<CVoucherHeader> voucherWorkflowService) {
        this.voucherWorkflowService = voucherWorkflowService;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }

    public EisCommonService getEisCommonService() {
        return eisCommonService;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getWorksVoucherRestrictedDate() {
        return worksVoucherRestrictedDate;
    }

    public void setWorksVoucherRestrictedDate(final String worksVoucherRestrictedDate) {
        this.worksVoucherRestrictedDate = worksVoucherRestrictedDate;
    }

    @Override
    public boolean isOneFunctionCenter() {
        return isOneFunctionCenter;
    }

    public void setOneFunctionCenter(final boolean isOneFunctionCenter) {
        this.isOneFunctionCenter = isOneFunctionCenter;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    /*
     * public boolean isRejectedVoucher() { return isRejectedVoucher; } public void setRejectedVoucher(boolean isRejectedVoucher)
     * { this.isRejectedVoucher = isRejectedVoucher; }
     */
    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }
}