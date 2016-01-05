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
package org.egov.web.actions.voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.service.CommonsService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.entity.Script;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.VoucherTypeBean;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;

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
    private VoucherService voucherService;
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
    private CommonsService commonsService;
    private String worksVoucherRestrictedDate;
    @Autowired
    private ScriptService scriptService;

    @SuppressWarnings("unchecked")
    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("approvaldepartmentList", Collections.EMPTY_LIST);
        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);
        // worksVoucherRestrictedDate=
        AppConfigValues appConfigValues = null;
        appConfigValues = (AppConfigValues) persistenceService
                .find("from AppConfigValues where key in "
                        +
                        "(select id from AppConfig where key_name='WORKS VOUCHERS RESTRICTION DATE FROM JV SCREEN' and module.name='EGF' )");
        if (appConfigValues == null)
            throw new ValidationException("Error", "WORKS VOUCHERS RESTRICTION DATE FROM JV SCREEN is not defined");
        else
            setWorksVoucherRestrictedDate(appConfigValues.getValue());

    }

    @SkipValidation
    @Action(value = "/voucher/journalVoucher-newForm")
    public String newForm()
    {
        billDetailslist = new ArrayList<VoucherDetails>();
        subLedgerlist = new ArrayList<VoucherDetails>();
        billDetailslist.add(new VoucherDetails());
        billDetailslist.add(new VoucherDetails());
        subLedgerlist.add(new VoucherDetails());
        // setting the typa as default for reusing billvoucher.nextdesg workflow
        loadApproverUser("default");
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
    public Object getModel() {
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
        HibernateUtil.getCurrentSession().setDefaultReadOnly(false);
        HibernateUtil.getCurrentSession().setFlushMode(FlushMode.AUTO);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherAction | create Method | Start");
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
        voucherHeader.setName(voucherTypeBean.getVoucherName());
        voucherHeader.setType(voucherTypeBean.getVoucherType());
        voucherHeader.setVoucherSubType(voucherTypeBean.getVoucherSubType());

        // String autoVoucherType
        // =EGovConfig.getProperty(FinancialConstants.APPLCONFIGNAME,voucherTypeBean.getVoucherNumType().toLowerCase(),"",FinancialConstants.CATEGORYFORVNO);
        /*
         * for (VoucherDetails voucherDetail : billDetailslist) {
         * voucherDetail.setFunctionIdDetail(voucherHeader.getVouchermis().getFunction().getId()); } for (VoucherDetails
         * voucherDetail : subLedgerlist) {
         * voucherDetail.setFunctionDetail(voucherHeader.getVouchermis().getFunction().getCode()); }
         */
        if (!validateData(billDetailslist, subLedgerlist))
            try {

                createVoucherAndledger(billDetailslist, subLedgerlist);
                if (!"JVGeneral".equalsIgnoreCase(voucherTypeBean.getVoucherName())) {
                    final String totalamount = parameters.get("totaldbamount")[0];
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(" Journal Voucher Action | Bill create | voucher name = " + voucherTypeBean.getVoucherName());
                    voucherService.createBillForVoucherSubType(billDetailslist, subLedgerlist, voucherHeader, voucherTypeBean,
                            new BigDecimal(totalamount));
                }
                voucherHeader.start().withOwner(getPosition()).withComments(voucherHeader.getDescription());
                // sendForApproval(); //Phoenix
                // addActionMessage( voucherHeader.getVoucherNumber() + getText("voucher.created.successfully"));
                // addActionMessage(getText("pjv.voucher.approved",new
                // String[]{voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition())}));
                message = "Voucher  "
                        + voucherHeader.getVoucherNumber()
                        + " Created Sucessfully"
                        + "\\n"
                        + getText("pjv.voucher.approved",
                                new String[] { voucherService.getEmployeeNameForPositionId(voucherHeader.getState()
                                        .getOwnerPosition()) });
                target = "success";

                // billDetailslist.clear();
                // subLedgerlist.clear();
                if (voucherHeader.getVouchermis().getBudgetaryAppnumber() != null)
                    addActionMessage(getText("budget.recheck.sucessful", new String[] { voucherHeader.getVouchermis()
                            .getBudgetaryAppnumber() }));
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("JournalVoucherAction | create  | Success | message === " + message);

                return viewform();
            }

        catch (final ValidationException e) {
            clearMessages();
            if (subLedgerlist.size() == 0)
                subLedgerlist.add(new VoucherDetails());
            voucherHeader.setVoucherNumber(voucherNumber);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            clearMessages();
            if (subLedgerlist.size() == 0)
                subLedgerlist.add(new VoucherDetails());
            voucherHeader.setVoucherNumber(voucherNumber);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        } finally {
            loadApproverUser("default");
        }
        else if (subLedgerlist.size() == 0)
            subLedgerlist.add(new VoucherDetails());
        loadApproverUser("default");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherAction | create Method | End");
        return NEW;
    }

    @SuppressWarnings("unchecked")
    private void loadApproverUser(final String type) {
        final String scriptName = "billvoucher.nextDesg";
        departmentId = voucherService.getCurrentDepartment().getId().intValue();
        final EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
        final Map<String, Object> map = voucherService.getDesgByDeptAndType(type, scriptName);
        if (null == map.get("wfitemstate")) {
            // If the department is mandatory show the logged in users assigned department only.
            if (mandatoryFields.contains("department"))
                addDropdownData("approvaldepartmentList", voucherHelper.getAllAssgnDeptforUser());
            else
                addDropdownData("approvaldepartmentList", masterCache.get("egi-department"));
            addDropdownData("designationList", (List<Designation>) map.get("designationList"));
            wfitemstate = "";
        } else
            wfitemstate = map.get("wfitemstate").toString();
    }

    public Position getPosition() throws ApplicationRuntimeException
    {
        Position pos;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getPosition====" + EgovThreadLocals.getUserId());
        pos = eisCommonService.getPositionByUserId(EgovThreadLocals.getUserId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("position===" + pos.getId());
        return pos;
    }

    public List<Action> getValidActions(final String purpose) {
        final List<Action> validButtons = new ArrayList<Action>();
        final Script validScript = (Script) getPersistenceService().findAllByNamedQuery(Script.BY_NAME, "pjv.validbuttons")
                .get(0);
        final List<String> list = (List<String>) scriptService.executeScript(validScript, ScriptService.createContext(
                "eisCommonServiceBean", eisCommonService, "userId", EgovThreadLocals.getUserId().intValue(), "date", new Date(),
                "purpose", purpose));
        for (final Object s : list)
        {
            if ("invalid".equals(s))
                break;
            final Action action = (Action) getPersistenceService().find(
                    " from org.egov.infstr.workflow.Action where type='CVoucherHeader' and name=?", s.toString());
            validButtons.add(action);
        }
        return validButtons;
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

    public VoucherService getVoucherService() {
        return voucherService;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
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

    public CommonsService getCommonsService() {
        return commonsService;
    }

    public void setCommonsService(final CommonsService commonsService) {
        this.commonsService = commonsService;
    }

    public String getWorksVoucherRestrictedDate() {
        return worksVoucherRestrictedDate;
    }

    public void setWorksVoucherRestrictedDate(final String worksVoucherRestrictedDate) {
        this.worksVoucherRestrictedDate = worksVoucherRestrictedDate;
    }
}