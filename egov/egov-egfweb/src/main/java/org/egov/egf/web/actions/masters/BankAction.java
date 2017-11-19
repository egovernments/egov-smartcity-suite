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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bank;
import org.egov.commons.utils.BankAccountType;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.services.masters.BankService;
import org.egov.utils.Constants;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = BankAction.MODIFY, location = "bank-modify.jsp"),
        @Result(name = BankAction.SUCCESS, location = "bank.jsp"),
        @Result(name = BankAction.VIEW, location = "bank-view.jsp"),
        @Result(name = BankAction.SEARCH, location = "bank-search.jsp") })
public class BankAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private Bank bank = new Bank();
    private boolean isActive;
    public static final String MODIFY = "modify";
    public static final String SEARCH = "search";
    private String mode;

    // For jquery BankName auto complete
    private String term;

    private BankService bankService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Override
    @SkipValidation
    @Actions({
            @Action(value = "/masters/bank"),
            @Action(value = "/masters/bank-execute")
    })
    public String execute() {

        if ("MODIFY".equals(mode) || "VIEW".equals(mode)) {
            if (StringUtils.isBlank(bank.getName())) {
                addDropdownData("bankList", bankService.findAll("name"));
                return SEARCH;
            } else {
                bank = bankService.find("FROM Bank WHERE name = ?", bank.getName());
                if (bank == null)
                    return SEARCH;
                else {
                    if (bank.getIsactive())
                        isActive = true;
                    else
                        isActive = false;
                    if ("MODIFY".equals(mode))
                        return MODIFY;
                    else
                        return VIEW;
                }
            }
        } else if ("UNQ_NAME".equals(mode))
            checkUniqueBankName();
        else if ("UNQ_CODE".equals(mode))
            checkUniqueBankCode();
        else if ("AUTO_COMP_BANK_NAME".equals(mode))
            populateBankNames();
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return bank;
    }

    @ValidationErrorPage(value = MODIFY)
    @Action(value = "/masters/bank-save")
    public String save() {
        try {
            if (isActive)
                bank.setIsactive(true);
            else
                bank.setIsactive(false);

            if (bank.getId() == null) {
                // TODO Dirty Code can be avoided by extending BaseModel for Bank
                final Date currentDate = new Date();
                bank.setCreatedDate(currentDate);
                bank.setCreatedBy(bankService.getSession().load(User.class, ApplicationThreadLocals.getUserId()));
                bank.setLastModifiedDate(currentDate);
                bank.setLastModifiedBy(bankService.getSession().load(User.class, ApplicationThreadLocals.getUserId()));
                bankService.persist(bank);
            } else {
                final Date currentDate = new Date();
                bank.setCreatedDate(currentDate);
                bank.setCreatedBy(bankService.getSession().load(User.class, ApplicationThreadLocals.getUserId()));
                bank.setLastModifiedDate(currentDate);
                bank.setLastModifiedBy(bankService.getSession().load(User.class, ApplicationThreadLocals.getUserId()));
                bankService.update(bank);
            }
            addActionMessage(getText("Bank Saved Successfully"));
        } catch (final ConstraintViolationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError("Duplicate Bank", "Duplicate Bank")));
        } catch (final Exception e) {
            addActionMessage(getText("Bank information can't be saved."));
            throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator",
                    "An error occured contact Administrator")));
        }
        return "modify";
    }

    private void checkUniqueBankCode() {
        final Bank bank = bankService.find("from Bank where lower(code)=?", this.bank.getCode().toLowerCase());
        writeToAjaxResponse(String.valueOf(bank == null));
    }

    private void checkUniqueBankName() {
        final Bank bank = bankService.find("from Bank where lower(name)=?", this.bank.getName().toLowerCase());
        writeToAjaxResponse(String.valueOf(bank == null));
    }

    private void populateBankNames() {
        final JSONArray jsonArray = new JSONArray(persistenceService.findAllBy("select name FROM Bank WHERE lower(name) like ?",
                StringUtils.lowerCase(term + "%")));
        writeToAjaxResponse(jsonArray.toString());
    }

    public String getBankAccountTypesJSON() {
        final StringBuilder bankAcTypesJson = new StringBuilder(":;");
        for (final BankAccountType value : BankAccountType.values())
            bankAcTypesJson.append(value.name()).append(":").append(value.name()).append(";");
        bankAcTypesJson.deleteCharAt(bankAcTypesJson.lastIndexOf(";"));
        return bankAcTypesJson.toString();
    }

    public Boolean isAutoBankAccountGLCodeEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                Constants.EGF, "auto_bankaccount_glcode").get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public String getFundsJSON() {
        final List<Object[]> funds = persistenceService.findAllBy("SELECT id, name FROM Fund WHERE isactive=?", true);
        final StringBuilder fundJson = new StringBuilder(":;");
        for (final Object[] fund : funds)
            fundJson.append(fund[0]).append(":").append(fund[1]).append(";");
        fundJson.deleteCharAt(fundJson.lastIndexOf(";"));
        return fundJson.toString();
    }

    public String getAccountTypesJSON() {
        final List<Object[]> accounttypes = persistenceService
                .findAllBy("SELECT name,id FROM CChartOfAccounts WHERE glcode LIKE '4502%' AND classification=2 ORDER BY glcode");
        final StringBuilder accountdetailtypeJson = new StringBuilder("{\"\":\"\",");
        for (final Object[] accType : accounttypes) {
            accType[0] = org.egov.infra.utils.StringUtils.escapeJavaScript((String) accType[0]);
            accountdetailtypeJson.append("\"").append(accType[1] + "#" + accType[0]).append("\"").append(":").append("\"")
                    .append(accType[0]).append("\"").append(",");
        }
        accountdetailtypeJson.deleteCharAt(accountdetailtypeJson.lastIndexOf(","));
        return accountdetailtypeJson.append("}").toString();
    }

    @Override
    public void validate() {
        if (bank.getName().equals(""))
            addFieldError("name", getText("bank.name.field.required"));
        if (bank.getCode().equals(""))
            addFieldError("code", getText("bank.code.field.required"));
    }

    private void writeToAjaxResponse(final String response) {
        try {
            final HttpServletResponse httpResponse = ServletActionContext.getResponse();
            final Writer httpResponseWriter = httpResponse.getWriter();
            IOUtils.write(response, httpResponseWriter);
            IOUtils.closeQuietly(httpResponseWriter);
        } catch (final IOException e) {
            LOG.error("Error occurred while processing Ajax response", e);
        }
    }

    public void setTerm(final String term) {
        this.term = term;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public void setBankService(final BankService bankService) {
        this.bankService = bankService;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final boolean isActive) {
        this.isActive = isActive;
    }
}