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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.web.actions.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.model.voucher.BankEntriesNotInBankBook;
import org.egov.services.voucher.BankEntriesNotInBankBookActionHelper;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({
        @Result(name = BankEntriesNotInBankBookAction.NEW, location = "bankEntriesNotInBankBook-new.jsp"),
        @Result(name = BankEntriesNotInBankBookAction.SUCCESS, location = "bankEntriesNotInBankBook-success.jsp")
})
public class BankEntriesNotInBankBookAction extends BasePaymentAction {

    private static final long serialVersionUID = 8336255427935452077L;
    private static final Logger LOGGER = Logger.getLogger(BankEntriesNotInBankBookAction.class);

    private Integer bankaccount;

    private List<BankEntriesNotInBankBook> bankEntriesNotInBankBookList;
    @Autowired
    private BankHibernateDAO bankHibernateDAO;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    @Autowired
    private BankEntriesNotInBankBookActionHelper bankEntriesNotInBankBookActionHelper;

    @Override
    public StateAware getModel() {
        voucherHeader = (CVoucherHeader) super.getModel();
        return voucherHeader;

    }

    @Override
    public void prepare() {
        super.prepare();
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        voucherHeader.setName(FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK);
    }

    public void prepareNewform() {
        addDropdownData("glcodeList", chartOfAccountsHibernateDAO.getAccountCodesListForBankEntries());
        addDropdownData("bankList", bankHibernateDAO.getAllBankHavingBranchAndAccounts());
        addDropdownData("bankBranchList", Collections.EMPTY_LIST);
        addDropdownData("bankAccountList", Collections.EMPTY_LIST);

    }

    @Override
    @SkipValidation
    @Action(value = "/payment/bankEntriesNotInBankBook-newform")
    public String newform() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Resetting all........................... ");
        voucherHeader.reset();
        voucherHeader.setVouchermis(new Vouchermis());
        bankEntriesNotInBankBookList = new ArrayList<BankEntriesNotInBankBook>();
        bankEntriesNotInBankBookList.add(new BankEntriesNotInBankBook());
        loadDefalutDates();
        return NEW;
    }

    @SkipValidation
    @Action(value = "/payment/bankEntriesNotInBankBook-save")
    public String save() {
        try {
            bankEntriesNotInBankBookActionHelper.create(voucherHeader, bankaccount, bankEntriesNotInBankBookList);
        } catch (final ValidationException e) {
            e.printStackTrace();
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            e.printStackTrace();
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }

        return SUCCESS;
    }

    public List<BankEntriesNotInBankBook> getBankEntriesNotInBankBookList() {
        return bankEntriesNotInBankBookList;
    }

    public void setBankEntriesNotInBankBookList(List<BankEntriesNotInBankBook> bankEntriesNotInBankBookList) {
        this.bankEntriesNotInBankBookList = bankEntriesNotInBankBookList;
    }

    public Integer getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(Integer bankaccount) {
        this.bankaccount = bankaccount;
    }

}