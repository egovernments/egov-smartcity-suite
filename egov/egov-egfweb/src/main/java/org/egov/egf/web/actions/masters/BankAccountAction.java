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
package org.egov.egf.web.actions.masters;

import com.google.gson.GsonBuilder;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.Fund;
import org.egov.commons.service.BankAccountService;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.commons.utils.BankAccountType;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.utils.EGovConfig;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
public class BankAccountAction extends JQueryGridActionSupport {
    private static final long serialVersionUID = 1L;
    String code = EGovConfig.getProperty("egf_config.xml", "glcodeMaxLength",
			"", "AccountCode");
    private String mode;
    private String newGLCode = "", coaID = "", glCode = "";
    private Integer bankBranchId;
    @Autowired
    @Qualifier("bankAccountService")
    private BankAccountService bankAccountService;
    @Autowired
    @Qualifier("chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;
    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Override
    public String execute() {
        if ("CRUD".equals(mode))
            try {
                if (oper.equals(ADD))
                    addBankAccount();
                else if (oper.equals(EDIT))
                    editBankAccount();
                else if (oper.equals(DELETE))
                    deleteBankAccount();
                sendAJAXResponse(SUCCESS);
            } catch (final RuntimeException e) {
                sendAJAXResponse("failed");
                throw new ApplicationRuntimeException(
                        "Error occurred in Bank Account.", e);
            }
        else if ("LIST_BRANCH_ACC".equals(mode))
            listAllBankBranchAccounts();
        return null;
    }

    private void addBankAccount() {
        final Bankbranch bankBranch = persistenceService
                .getSession().load(Bankbranch.class, bankBranchId);
        new Date();
        final Bankaccount bankAccount = new Bankaccount();
        final HttpServletRequest request = ServletActionContext.getRequest();
        bankAccount.setBankbranch(bankBranch);
        try {
            if (autoBankAccountGLCodeEnabled()) {
                if (!request.getParameter("accounttype").equalsIgnoreCase("")) {
                    newGLCode = prepareBankAccCode(
                            request.getParameter("accounttype").split("#")[0],
                            code);
                    coaID = postInChartOfAccounts(newGLCode, request
                                    .getParameter("accounttype").split("#")[0],
                            request.getParameter("accountnumber"));
                    if (coaID != null && coaID.length() > 0) {
                        final CChartOfAccounts chartofaccounts = persistenceService
                                .getSession().load(CChartOfAccounts.class,
                                        Long.parseLong(coaID));
                        bankAccount.setChartofaccounts(chartofaccounts);
                    }
                }
            } else {
                if (!request.getParameter("glcode").equalsIgnoreCase("")) {
                    glCode = request.getParameter("glcode");
                    validateGlCode(glCode);
                    CChartOfAccounts COA = chartOfAccountsService
                            .find("select coa from CChartOfAccounts coa where coa.glcode = ?",
                                    glCode);
                    bankAccount.setChartofaccounts(COA);
                }
            }
        } catch (final Exception e) {
            sendAJAXResponse(e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage());
        }
        populateBankAccountDetail(bankAccount);
        bankAccountService.applyAuditing(bankAccount);
        bankAccountService.persist(bankAccount);
    }

    public String prepareBankAccCode(final String accID, final String code)
            throws Exception {
        String glCode = "";
        Long glcode;
        Long tempCode = 0L;
        glCode = (String) persistenceService
                .find("select glcode from CChartOfAccounts where id=?) order by glcode desc",
                        Long.parseLong(accID));
        final String subminorvalue = EGovConfig.getProperty("egf_config.xml",
                "subminorvalue", "", "AccountCode");
        glCode = glCode.substring(0, Integer.parseInt(subminorvalue));
        glCode = (String) persistenceService
                .find("select glcode from CChartOfAccounts where glcode like ? || '%' order by glcode desc",
                        glCode);
        final String zero = EGovConfig.getProperty("egf_config.xml",
                "zerofill", "", "AccountCode");
        if (glCode.length() == Integer.parseInt(code)) {
            glcode = Long.parseLong(glCode);
            tempCode = glcode + 1;
        } else {
            glCode = glCode + zero;
            glcode = Long.parseLong(glCode);
            tempCode = glcode + 1;
        }
        glCode = Long.toString(tempCode);
        return glCode;
    }

    public String postInChartOfAccounts(final String glCode,
                                        final String parentId, final String accNumber) throws Exception {
        final Bankbranch bankBranch = persistenceService
                .getSession().load(Bankbranch.class, bankBranchId);
        int majorCodeLength = 0;
        majorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF,
                "coa_majorcode_length"));
        final CChartOfAccounts chart = new CChartOfAccounts();
        chart.setGlcode(glCode);
        chart.setName(bankBranch.getBank().getName() + " "
                + bankBranch.getBranchname() + " " + accNumber);
        chart.setParentId(Long.parseLong(parentId));
        chart.setType('A');
        chart.setClassification(Long.parseLong("4"));
        chart.setIsActiveForPosting(true);
        chart.setMajorCode(chart.getGlcode().substring(0, majorCodeLength));
        chartOfAccountsService.persist(chart);
        return String.valueOf(chart.getId());
    }

    private void validateGlCode(String glCode) {
        CChartOfAccounts COA = chartOfAccountsService.find(
                "select coa from CChartOfAccounts coa where coa.glcode = ?",
                glCode);
        Bankaccount account = null;
        AccountCodePurpose purpose = null;
        if (COA == null)
            throw new ApplicationRuntimeException("Given glcode does not exist");
        if (glCode != null) {
            CGeneralLedger glList = (CGeneralLedger) persistenceService
                    .find("select gl from CGeneralLedger gl where gl.glcodeId.glcode=? and gl.voucherHeaderId.status not in (4) ",
                            glCode);
            if (glList != null)
                throw new ApplicationRuntimeException(
                        "Transaction already exist for given glcode");

        }
        if (COA != null) {
            account = bankAccountService
                    .find("select ba from Bankaccount ba where ba.chartofaccounts.glcode = ?",
                            glCode);
            if (account != null)
                throw new ApplicationRuntimeException(
                        "Given glcode is already mapped to another bank account - "
                                + account.getAccountnumber());
        }
        if (!COA.getIsActiveForPosting())
            throw new ApplicationRuntimeException(
                    "Given glcode is not active for posting");
        if (COA.getChartOfAccountDetails() != null
                && !COA.getChartOfAccountDetails().isEmpty())
            throw new ApplicationRuntimeException(
                    "Given glcode should not be a control code");
        if (COA.getType() != null && !COA.getType().equals('A')) {
            throw new ApplicationRuntimeException(
                    "Given glcode should be of type Assets");
        }
        if (COA.getPurposeId() == null) {
            throw new ApplicationRuntimeException(
                    "Given glcode is not mapped with any purpose ");
        }
        if (COA.getPurposeId() != null) {
            purpose = (AccountCodePurpose) persistenceService
                    .find("select purpose from AccountCodePurpose purpose where purpose.id = ?",
                            COA.getPurposeId());
            if (purpose != null
                    && !purpose.getName().contains("Bank Account Codes"))
                throw new ApplicationRuntimeException(
                        "Given glcode should be of purpose Bank Account Codes");
        }

    }

    private void editBankAccount() {
        final Bankaccount bankAccount = bankAccountService
                .getSession().get(Bankaccount.class, id.longValue());
        populateBankAccountDetail(bankAccount);
        bankAccountService.applyAuditing(bankAccount);
        bankAccountService.update(bankAccount);
    }

    private void deleteBankAccount() {
        final Bankaccount bankBranch = bankAccountService
                .getSession().load(Bankaccount.class, id.longValue());
        persistenceService.delete(bankBranch);
    }

    private void populateBankAccountDetail(final Bankaccount bankAccount) {
        final HttpServletRequest request = ServletActionContext.getRequest();
        bankAccount.setAccountnumber(request.getParameter("accountnumber"));
        bankAccount.setAccounttype(getAccountType(request
                .getParameter("glcode")));

        if (org.apache.commons.lang.StringUtils.isNotBlank(request
                .getParameter("fundname"))) {
            final Fund fund = persistenceService.getSession().load(
                    Fund.class,
                    Integer.valueOf(request.getParameter("fundname")));
            bankAccount.setFund(fund);
        }
        bankAccount
                .setIsactive(request.getParameter("active").equals("Y"));
        bankAccount.setNarration(request.getParameter("narration"));
        if (org.apache.commons.lang.StringUtils.isNotBlank(request
                .getParameter("typename"))) {
            final BankAccountType type = BankAccountType.valueOf(request
                    .getParameter("typename"));
            bankAccount.setType(type);
        }
        bankAccount.setPayTo(request.getParameter("payto"));
    }

    public String getAccountType(String glCode) {
        String name = (String) persistenceService
                .find("select name from CChartOfAccounts where id=(select parentId from CChartOfAccounts where glcode = ?)",
                        glCode);
        return name;
    }

    public Boolean autoBankAccountGLCodeEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService
                .getConfigValuesByModuleAndKey(Constants.EGF,
                        "auto_bankaccount_glcode").get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    private void listAllBankBranchAccounts() {
        final List<Bankaccount> bankAccounts = getPagedResult(
                Bankaccount.class, "bankbranch.id", bankBranchId).getList();
        final List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        String glCode = "";

        for (final Bankaccount bankaccount : bankAccounts)
            try {
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", bankaccount.getId());
                jsonObject.put("accountnumber", bankaccount.getAccountnumber());
                jsonObject.put("fundname", bankaccount.getFund().getName());
                jsonObject.put("narration", bankaccount.getNarration());
                jsonObject.put("payto", bankaccount.getPayTo());
                jsonObject.put("typename", bankaccount.getType() == null ? ""
                        : bankaccount.getType().name());
                jsonObject.put("active", bankaccount.getIsactive() ? "Y" : "N");
                glCode = (String) persistenceService
                        .find("select glcode from CChartOfAccounts where id=(select chartofaccounts.id from Bankaccount where accountnumber = ?)",
                                bankaccount.getAccountnumber());
                jsonObject.put("glcode", glCode);
                jsonObject.put("accounttype", getAccountType(glCode));
                jsonObjects.add(jsonObject);
            } catch (final JSONException e) {
                sendAJAXResponse("error");
            }
        final String jsonString = new GsonBuilder().create()
                .toJson(jsonObjects);
        sendAJAXResponse(constructJqGridResponse(jsonString));
    }

    String getAppConfigValueFor(final String module, final String key) {
        return appConfigValuesService
                .getConfigValuesByModuleAndKey(module, key).get(0).getValue();
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public void setBankBranchId(final Integer bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public void setAppConfigValuesService(
            final AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

}