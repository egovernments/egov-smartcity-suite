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
package org.egov.egf.web.actions.voucher;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CVoucherHeader;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.VoucherTypeBean;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.services.voucher.JournalVoucherActionHelper;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@ParentPackage("egov")
@Results({ @Result(name = JournalVoucherAction.NEW, location = "journalVoucher-new.jsp") })
public class JournalVoucherAction extends BaseVoucherAction
{
    private static final Logger LOGGER = Logger.getLogger(JournalVoucherAction.class);
    private static final long serialVersionUID = 1L;
    private List<VoucherDetails> billDetailslist;
    private List<VoucherDetails> subLedgerlist;
    private String target;
    protected String showMode;
    @Autowired
    @Qualifier("voucherService")
    private VoucherService voucherService;
    @Autowired
    @Qualifier("journalVoucherActionHelper")
    private JournalVoucherActionHelper journalVoucherActionHelper;
    private VoucherTypeBean voucherTypeBean;
    private String buttonValue;
    private String message = "";
    private Integer departmentId;
    private String wfitemstate;
    private VoucherHelper voucherHelper;
    private static final String VOUCHERQUERY = " from CVoucherHeader where id=?";
    private static final String ACTIONNAME = "actionName";
    private SimpleWorkflowService<CVoucherHeader> voucherWorkflowService;
    private static final String VHID = "vhid";
    protected EisCommonService eisCommonService;
    @Autowired
    protected AppConfigValueService appConfigValuesService;
    private String cutOffDate;
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date;
    @Autowired
    private BudgetControlTypeService budgetCheckConfigService;

    @Autowired
    private ScriptService scriptService;

    @SuppressWarnings("unchecked")
    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("approvaldepartmentList", Collections.EMPTY_LIST);
        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);
    }

    @SkipValidation
    @Action(value = "/voucher/journalVoucher-newForm")
    public String newForm()
    {
        List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "DataEntryCutOffDate");
        if (cutOffDateconfigValue != null && !cutOffDateconfigValue.isEmpty())
        {
            try {
                date = df.parse(cutOffDateconfigValue.get(0).getValue());
                cutOffDate = formatter.format(date);
            } catch (ParseException e) {

            }
        }
        billDetailslist = new ArrayList<VoucherDetails>();
        subLedgerlist = new ArrayList<VoucherDetails>();
        billDetailslist.add(new VoucherDetails());
        billDetailslist.add(new VoucherDetails());
        subLedgerlist.add(new VoucherDetails());
        // setting the typa as default for reusing billvoucher.nextdesg workflow
        showMode = NEW;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("JournalVoucherAction | new | End");
        return NEW;
    }

    @SkipValidation
    public String viewform()
    {
        showMode = "view";
        // loadApproverUser("default");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("JournalVoucherAction | new | End");
        return NEW;
    }

    @Override
    public StateAware getModel() {
        voucherHeader = (CVoucherHeader) super.getModel();
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
        // voucherHeader.setName(FinancialConstants.JOURNALVOUCHER_NAME_GENERAL);
        return voucherHeader;

    };

    /**
     *
     * @return
     * @throws Exception
     */
    @SkipValidation
    @Action(value = "/voucher/journalVoucher-create")
    public String create() throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherAction | create Method | Start");
        String voucherDate = formatter1.format(voucherHeader.getVoucherDate());
        String cutOffDate1 = null;
        removeEmptyRowsAccoutDetail(billDetailslist);
        removeEmptyRowsSubledger(subLedgerlist);
        target = "";
        // for manual voucher number.
        // voucherNumType
        final String voucherNumber = voucherHeader.getVoucherNumber();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Bill details List size  : " + billDetailslist.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Sub ledger details List size  : " + subLedgerlist.size());
        loadSchemeSubscheme();
        validateFields();
        if (!validateData(billDetailslist, subLedgerlist))
            try {
                if (!"JVGeneral".equalsIgnoreCase(voucherTypeBean.getVoucherName())) {
                    voucherTypeBean.setTotalAmount(parameters.get("totaldbamount")[0]);
                }
                populateWorkflowBean();
                voucherHeader = journalVoucherActionHelper.createVoucher(billDetailslist, subLedgerlist, voucherHeader,
                        voucherTypeBean, workflowBean);

                if (!cutOffDate.isEmpty() && cutOffDate!=null )
                {
                    try {
                        date = sdf.parse(cutOffDate);
                        cutOffDate1 = formatter1.format(date);
                    } catch (ParseException e) {

                    }
                }
                if (cutOffDate1 != null && voucherDate.compareTo(cutOffDate1) <= 0
                        && FinancialConstants.CREATEANDAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                {
                    if (voucherHeader.getVouchermis().getBudgetaryAppnumber() == null)
                    {
                        message = "Voucher  "
                                + voucherHeader.getVoucherNumber()
                                + " Created Sucessfully";
                        target = "success";
                    }

                    else
                    {
                        message = "Voucher  "
                                + voucherHeader.getVoucherNumber()
                                + " Created Sucessfully"
                                + "\\n"
                                + "And "
                                + getText("budget.recheck.sucessful", new String[] { voucherHeader.getVouchermis()
                                        .getBudgetaryAppnumber() });
                        target = "success";

                    }
                }

                else
                {
                    if (voucherHeader.getVouchermis().getBudgetaryAppnumber() == null)
                    {
                        message = "Voucher  "
                                + voucherHeader.getVoucherNumber()
                                + " Created Sucessfully"
                                + "\\n"
                                + getText("pjv.voucher.approved",
                                        new String[] { voucherService.getEmployeeNameForPositionId(voucherHeader.getState()
                                                .getOwnerPosition()) });
                        target = "success";
                    }

                    else
                    {
                        message = "Voucher  "
                                + voucherHeader.getVoucherNumber()
                                + " Created Sucessfully"
                                + "\\n"
                                + "And "
                                + getText("budget.recheck.sucessful", new String[] { voucherHeader.getVouchermis()
                                        .getBudgetaryAppnumber() })
                                + "\\n"
                                + getText("pjv.voucher.approved",
                                        new String[] { voucherService.getEmployeeNameForPositionId(voucherHeader.getState()
                                                .getOwnerPosition()) });

                        target = "success";

                    }
                }
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("JournalVoucherAction | create  | Success | message === " + message);

                return viewform();
            }

            catch (final ValidationException e) {
                // clearMessages();
                if (subLedgerlist.size() == 0)
                    subLedgerlist.add(new VoucherDetails());
                voucherHeader.setVoucherNumber(voucherNumber);
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
                if (e.getErrors().get(0).getMessage() != null && e.getErrors().get(0).getMessage() != "")
                    throw new ValidationException(e.getErrors().get(0).getMessage(), e.getErrors().get(0).getMessage());
                else
                    throw new ValidationException("Voucher creation failed", "Voucher creation failed");

            } catch (final Exception e) {

                clearMessages();
                if (subLedgerlist.size() == 0)
                    subLedgerlist.add(new VoucherDetails());
                voucherHeader.setVoucherNumber(voucherNumber);
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exp", e.getMessage()));
                throw new ValidationException(errors);
            } finally {
            }
        else if (subLedgerlist.size() == 0)
            subLedgerlist.add(new VoucherDetails());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherAction | create Method | End");
        return NEW;
    }

    public List<String> getValidActions() {
        List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "DataEntryCutOffDate");
        List<String> validActions = Collections.emptyList();
        if (cutOffDateconfigValue != null && !cutOffDateconfigValue.isEmpty())
        {
            if (null == voucherHeader || null == voucherHeader.getId()
                    || voucherHeader.getCurrentState().getValue().endsWith("NEW")) {
                validActions = Arrays.asList(FinancialConstants.BUTTONFORWARD, FinancialConstants.CREATEANDAPPROVE);
            } else {
                if (voucherHeader.getCurrentState() != null) {
                    validActions = this.customizedWorkFlowService.getNextValidActions(voucherHeader
                            .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                            getAdditionalRule(), voucherHeader.getCurrentState().getValue(),
                            getPendingActions(), voucherHeader.getCreatedDate());
                }
            }
        }
        else
        {
            if (null == voucherHeader || null == voucherHeader.getId()
                    || voucherHeader.getCurrentState().getValue().endsWith("NEW")) {
                // read from constant
                validActions = Arrays.asList(FinancialConstants.BUTTONFORWARD);
            } else {
                if (voucherHeader.getCurrentState() != null) {
                    validActions = this.customizedWorkFlowService.getNextValidActions(voucherHeader
                            .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                            getAdditionalRule(), voucherHeader.getCurrentState().getValue(),
                            getPendingActions(), voucherHeader.getCreatedDate());
                }
            }
        }
        return validActions;
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

    public List<VoucherDetails> getBillDetailslist() {
        return billDetailslist;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(final String target) {
        this.target = target;
    }

    public VoucherTypeBean getVoucherTypeBean() {
        return voucherTypeBean;
    }

    public void setVoucherTypeBean(final VoucherTypeBean voucherTypeBean) {
        this.voucherTypeBean = voucherTypeBean;
    }

    @ValidationErrorPage(value = "new")
    public String saveAndView() throws Exception {
        try {
            buttonValue = "view";
            return create();
        } catch (final ValidationException e) {
            throw e;
        }
    }

    @ValidationErrorPage(value = "new")
    public String saveAndPrint() throws Exception {
        try {
            buttonValue = "print";
            return create();
        } catch (final ValidationException e) {
            throw e;
        }
    }

    @ValidationErrorPage(value = "new")
    public String saveAndNew() throws Exception {
        try {
            buttonValue = "new";
            return create();
        } catch (final ValidationException e) {
            throw e;
        }
    }

    @ValidationErrorPage(value = "new")
    public String saveAndClose() throws Exception {
        buttonValue = "close";
        return create();
    }

    public String getMessage() {
        return message;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(final String buttonValue) {
        this.buttonValue = buttonValue;
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

    public EisCommonService getEisCommonService() {
        return eisCommonService;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public String getShowMode() {
        return showMode;
    }

    public void setShowMode(final String showMode) {
        this.showMode = showMode;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

}