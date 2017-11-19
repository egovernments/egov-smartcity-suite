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
package org.egov.egf.web.actions.bill;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.web.actions.voucher.BaseVoucherAction;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.model.bills.EgBillregister;
import org.egov.model.voucher.CommonBean;
import org.egov.model.voucher.VoucherDetails;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.egov.services.bills.BillsService;
import org.egov.services.bills.EgBillRegisterService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.CheckListHelper;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BaseBillAction extends BaseVoucherAction {
    protected static final long serialVersionUID = 6627521670678057404L;
    protected EisCommonService eisCommonService;
    protected CommonBean commonBean;
    protected static final String REQUIRED = "required";
    protected static final String VIEW = "view";
    protected static final String REVERSE = "reverse";
    protected List<Accountdetailtype> accountDetailTypeList;
    protected List<EntityType> entitiesList;
    protected List<VoucherDetails> billDetailslist;
    protected List<VoucherDetails> subledgerlist;
    protected List<VoucherDetails> billDetailsTableSubledger;
    protected List<VoucherDetails> billDetailTableFinallist;
    protected List<VoucherDetails> billDetailsTableNetFinalList;
    protected List<VoucherDetails> billDetailsTableCreditFinalist;
    protected List<VoucherDetails> billDetailsTableFinal;
    protected List<VoucherDetails> billDetailsTableNetFinal;
    protected List<VoucherDetails> billDetailsTableCreditFinal;
    protected List<CheckListHelper> checkListsTable;
    protected SimpleWorkflowService<EgBillregister> billRegisterWorkflowService;
    protected EgBillRegisterService egBillRegisterService;
    protected EgovCommon egovCommon;
    protected @Autowired AppConfigValueService appConfigValuesService;
    protected CChartOfAccounts defaultNetPayCode;
    protected Long billRegisterId;
    protected static final String FALSE = "false";
    protected static final String TRUE = "true";
    protected List<CChartOfAccounts> netPayList;
    @Autowired
    protected ScriptService scriptService;
    protected String detailTypeIdandName = "";
    protected BillsService billsManager;
    protected String button;
    protected boolean billNumberGenerationAuto;
    protected final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected VoucherService voucherService;
    protected String mode;
    protected String nextLevel;
    protected List<WorkflowAction> validButtons;
    protected Integer departmentId;
    protected EisUtilService eisUtilService;
    protected VoucherHelper voucherHelper;
    @Autowired
    protected ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    @Autowired
    protected FunctionHibernateDAO functionHibernateDAO;
    @Autowired
    protected FinancialYearDAO financialYearDAO;

    protected Date getDefaultDate() {
        final Date currDate = new Date();

        try {
            return sdf.parse(sdf.format(currDate));
        } catch (final ParseException e) {
            throw new ValidationException(Arrays.asList(new ValidationError("Exception while formatting voucher date",
                    "Transaction failed")));
        }
    }

    public boolean isBillNumberGenerationAuto()
    {
        final List<AppConfigValues> configValuesByModuleAndKey = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "Bill_Number_Geneartion_Auto");
        billNumberGenerationAuto = false;
        if (configValuesByModuleAndKey.size() > 0)
            billNumberGenerationAuto = configValuesByModuleAndKey.get(0).getValue().equalsIgnoreCase("y") ? true : false;
        return billNumberGenerationAuto;
    }

    public EisCommonService getEisCommonService() {
        return eisCommonService;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public Position getPosition() throws ApplicationRuntimeException
    {
        return eisCommonService.getPositionByUserId(ApplicationThreadLocals.getUserId());
    }

    public CommonBean getCommonBean() {
        return commonBean;
    }

    public void setCommonBean(final CommonBean commonBean) {
        this.commonBean = commonBean;
    }

    public List<Accountdetailtype> getAccountDetailTypeList() {
        return accountDetailTypeList;
    }

    public void setAccountDetailTypeList(final List<Accountdetailtype> accountDetailTypeList) {
        this.accountDetailTypeList = accountDetailTypeList;
    }

    public List<EntityType> getEntitiesList() {
        return entitiesList;
    }

    public void setEntitiesList(final List<EntityType> entitiesList) {
        this.entitiesList = entitiesList;
    }

    public List<VoucherDetails> getBillDetailslist() {
        return billDetailslist;
    }

    public void setBillDetailslist(final List<VoucherDetails> billDetailslist) {
        this.billDetailslist = billDetailslist;
    }

    public List<VoucherDetails> getSubLedgerlist() {
        return subledgerlist;
    }

    public void setSubLedgerlist(final List<VoucherDetails> subLedgerlist) {
        subledgerlist = subLedgerlist;
    }

    public EgovCommon getEgovCommon() {
        return egovCommon;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public List<CChartOfAccounts> getNetPayList() {
        return netPayList;
    }

    public void setNetPayList(final List<CChartOfAccounts> netPayList) {
        this.netPayList = netPayList;
    }

    public CChartOfAccounts getDefaultNetPayCode() {
        return defaultNetPayCode;
    }

    public void setDefaultNetPayCode(final CChartOfAccounts defaultNetPayCode) {
        this.defaultNetPayCode = defaultNetPayCode;
    }

    public List<VoucherDetails> getBillDetailsTableFinal() {
        return billDetailsTableFinal;
    }

    public void setBillDetailsTableFinal(final List<VoucherDetails> billDetailsTableFinal) {
        this.billDetailsTableFinal = billDetailsTableFinal;
    }

    public List<VoucherDetails> getBillDetailsTableNetFinal() {
        return billDetailsTableNetFinal;
    }

    public void setBillDetailsTableNetFinal(final List<VoucherDetails> billDetailsTableNetFinal) {
        this.billDetailsTableNetFinal = billDetailsTableNetFinal;
    }

    public List<VoucherDetails> getBillDetailsTableCreditFinal() {
        return billDetailsTableCreditFinal;
    }

    public void setBillDetailsTableCreditFinal(final List<VoucherDetails> billDetailsTableCreditFinal) {
        this.billDetailsTableCreditFinal = billDetailsTableCreditFinal;
    }

    public List<VoucherDetails> getBillDetailTableFinallist() {
        return billDetailTableFinallist;
    }

    public void setBillDetailTableFinallist(final List<VoucherDetails> billDetailTableFinallist) {
        this.billDetailTableFinallist = billDetailTableFinallist;
    }

    public List<VoucherDetails> getBillDetailsTableNetFinalList() {
        return billDetailsTableNetFinalList;
    }

    public void setBillDetailsTableNetFinalList(final List<VoucherDetails> billDetailsTableNetFinalList) {
        this.billDetailsTableNetFinalList = billDetailsTableNetFinalList;
    }

    public List<VoucherDetails> getBillDetailsTableCreditFinalist() {
        return billDetailsTableCreditFinalist;
    }

    public void setBillDetailsTableCreditFinalist(final List<VoucherDetails> billDetailsTableCreditFinalist) {
        this.billDetailsTableCreditFinalist = billDetailsTableCreditFinalist;
    }

    public List<VoucherDetails> getSubledgerlist() {
        return subledgerlist;
    }

    public void setSubledgerlist(final List<VoucherDetails> subledgerlist) {
        this.subledgerlist = subledgerlist;
    }

    public List<VoucherDetails> getBillDetailsTableSubledger() {
        return billDetailsTableSubledger;
    }

    public void setBillDetailsTableSubledger(final List<VoucherDetails> billDetailsTableSubledger) {
        this.billDetailsTableSubledger = billDetailsTableSubledger;
    }

    
    public BillsService getBillsService() {
        return billsManager;
    }

    public void setBillsService(final BillsService billsManager) {
        this.billsManager = billsManager;
    }

    public List getBillSubTypes() {
        return persistenceService.findAllBy("from EgBillSubType where expenditureType=? order by name",
                FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
    }

    public Long getBillRegisterId() {
        return billRegisterId;
    }

    public void setBillRegisterId(final Long billRegisterId) {
        this.billRegisterId = billRegisterId;
    }

    public String getDetailTypeIdandName() {
        return detailTypeIdandName;
    }

    public void setDetailTypeIdandName(final String detailTypeIdandName) {
        this.detailTypeIdandName = detailTypeIdandName;
    }

    public SimpleWorkflowService<EgBillregister> getBillRegisterWorkflowService() {
        return billRegisterWorkflowService;
    }

    public void setBillRegisterWorkflowService(final SimpleWorkflowService<EgBillregister> billRegisterWorkflowService) {
        this.billRegisterWorkflowService = billRegisterWorkflowService;
    }


    public EgBillRegisterService getEgBillRegisterService() {
        return egBillRegisterService;
    }

    public void setEgBillRegisterService(EgBillRegisterService egBillRegisterService) {
        this.egBillRegisterService = egBillRegisterService;
    }

    public String getButton() {
        return button;
    }

    public ScriptService getScriptService() {
        return scriptService;
    }

    public void setScriptService(final ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    public void setButton(final String button) {
        this.button = button;
    }

  
    public void setVoucherHelper(final VoucherHelper voucherHelper) {
        this.voucherHelper = voucherHelper;
    }

  
    public VoucherService getVoucherService() {
        return voucherService;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(final String nextLevel) {
        this.nextLevel = nextLevel;
    }

    public List<WorkflowAction> getValidButtons() {
        return validButtons;
    }

    public void setValidButtons(final List<WorkflowAction> validButtons) {
        this.validButtons = validButtons;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public EisUtilService getEisUtilService() {
        return eisUtilService;
    }

    public void setEisUtilService(final EisUtilService eisUtilService) {
        this.eisUtilService = eisUtilService;
    }

    public FunctionHibernateDAO getFunctionHibernateDAO() {
        return functionHibernateDAO;
    }

    public void setFunctionHibernateDAO(final FunctionHibernateDAO functionHibernateDAO) {
        this.functionHibernateDAO = functionHibernateDAO;
    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public void setAppConfigValuesService(
            final AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

    public ChartOfAccountsHibernateDAO getChartOfAccountsHibernateDAO() {
        return chartOfAccountsHibernateDAO;
    }

    public void setChartOfAccountsHibernateDAO(
            final ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO) {
        this.chartOfAccountsHibernateDAO = chartOfAccountsHibernateDAO;
    }

    public FinancialYearDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

}