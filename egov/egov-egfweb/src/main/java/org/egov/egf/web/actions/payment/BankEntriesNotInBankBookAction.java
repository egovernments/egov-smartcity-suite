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
package org.egov.egf.web.actions.payment;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.BankBranchHibernateDAO;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.BankaccountHibernateDAO;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.model.voucher.BankEntriesNotInBankBook;
import org.egov.services.instrument.BankEntriesService;
import org.egov.services.voucher.BankEntriesNotInBankBookActionHelper;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = BankEntriesNotInBankBookAction.NEW, location = "bankEntriesNotInBankBook-new.jsp"),
        @Result(name = BankEntriesNotInBankBookAction.SUCCESS, location = "bankEntriesNotInBankBook-success.jsp")
})
public class BankEntriesNotInBankBookAction extends BasePaymentAction {

    private static final long serialVersionUID = 8336255427935452077L;
    private static final Logger LOGGER = Logger.getLogger(BankEntriesNotInBankBookAction.class);
    private static final SimpleDateFormat FORMATDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);

    private Integer bankaccount;
    
    private Integer bank;
    
    private Integer bank_branch;

    private List<BankEntriesNotInBankBook> bankEntriesNotInBankBookList;
    @Autowired
    private BankHibernateDAO bankHibernateDAO;
    @Autowired
    private BankBranchHibernateDAO bankBranchHibernateDAO;
    @Autowired
    private BankaccountHibernateDAO bankaccountHibernateDAO;
    
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    @Autowired
    private BankEntriesNotInBankBookActionHelper bankEntriesNotInBankBookActionHelper;
    @Autowired
    @Qualifier("bankEntriesService")
    private BankEntriesService bankEntriesService;

    private Long beId;
    
    private String mode;

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
        addDropdownData("bankList", Collections.EMPTY_LIST);
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
    @Action(value = "/payment/bankEntriesNotInBankBook-search")
    public String search() {
        Query query = null;
        query = persistenceService.getSession().createSQLQuery(getQuery())
                .addScalar("refNum", StringType.INSTANCE)
                .addScalar("type", StringType.INSTANCE)
                .addScalar("date", DateType.INSTANCE)
                .addScalar("amount", BigDecimalType.INSTANCE)
                .addScalar("remarks", StringType.INSTANCE)
                .addScalar("glcodeDetail", StringType.INSTANCE)
                .addScalar("beId", LongType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(BankEntriesNotInBankBook.class));
        bankEntriesNotInBankBookList = query.list();
        List<BankEntriesNotInBankBook> tempList = new ArrayList<BankEntriesNotInBankBook>();
        for (BankEntriesNotInBankBook bean : bankEntriesNotInBankBookList)
        {
            bean.setDateId(FORMATDDMMYYYY.format(bean.getDate()));
            tempList.add(bean);
        }
        bankEntriesNotInBankBookList = tempList;
        if (bankEntriesNotInBankBookList.size() == 0)
            bankEntriesNotInBankBookList.add(new BankEntriesNotInBankBook());
        prepareNewform();
        addDropdownData("bankList", bankHibernateDAO.getAllBanksByFund(voucherHeader.getFundId().getId()));
        addDropdownData("bankBranchList", bankBranchHibernateDAO.getAllBankBranchsByBank(bank));
        addDropdownData("bankAccountList", bankaccountHibernateDAO.getBankAccountByBankBranch(bank_branch));
        mode = "save";
        return NEW;
    }

    private String getQuery() {
        String query = "", subQuery = "";
        if (bankaccount != null)
            subQuery = subQuery + "and be.bankaccountid = " + bankaccount;
        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            subQuery = subQuery + "and bemis.departmentid = " + voucherHeader.getVouchermis().getDepartmentid().getId();
        if (voucherHeader.getFundId() != null)
            subQuery = subQuery + "and bemis.fundid = " + voucherHeader.getFundId().getId();
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            subQuery = subQuery + "and bemis.schemeid = " + voucherHeader.getVouchermis().getSchemeid().getId();
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            subQuery = subQuery + "and bemis.subschemeid = " + voucherHeader.getVouchermis().getSubschemeid().getId();
        if (voucherHeader.getVouchermis().getFundsource() != null)
            subQuery = subQuery + "and bemis.fundsourceid = " + voucherHeader.getVouchermis().getFundsource().getId();
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            subQuery = subQuery + "and bemis.divisionid = " + voucherHeader.getVouchermis().getDivisionid().getId();
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            subQuery = subQuery + "and bemis.functionaryid = " + voucherHeader.getVouchermis().getFunctionary().getId();
        if (voucherHeader.getVouchermis().getFunction() != null)
            subQuery = subQuery + "and bemis.functionid = " + voucherHeader.getVouchermis().getFunction().getId();

        query = "select be.id as beId,be.refno as refnum,be.type as type,be.txndate as date,be.txnamount as amount,be.glcodeid as glcodeDetail,be.remarks as remarks "
                + " from bankentries be,bankentries_mis bemis where be.voucherheaderid is null and be.id = bemis.bankentriesid "
                + subQuery;
        return query;

    }

    @SkipValidation
    @Action(value = "/payment/bankEntriesNotInBankBook-save")
    public String save() {
        try {
            bankEntriesNotInBankBookActionHelper.create(voucherHeader, bankaccount, bankEntriesNotInBankBookList);
        } catch (final ValidationException e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }

        return SUCCESS;
    }

    @SkipValidation
    @Action(value = "/payment/ajaxDeleteBankEntries")
    public void ajaxDeleteBankEntries() {
        if (beId != null)
            bankEntriesService.delete(bankEntriesService.findById(beId, false));
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

    public Long getBeId() {
        return beId;
    }

    public void setBeId(Long beId) {
        this.beId = beId;
    }

    public Integer getBank() {
        return bank;
    }

    public void setBank(Integer bank) {
        this.bank = bank;
    }

    public Integer getBank_branch() {
        return bank_branch;
    }

    public void setBank_branch(Integer bank_branch) {
        this.bank_branch = bank_branch;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}