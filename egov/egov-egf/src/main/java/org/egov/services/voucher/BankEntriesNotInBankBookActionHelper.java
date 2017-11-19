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
package org.egov.services.voucher;

import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FunctionDAO;
import org.egov.commons.dao.FunctionaryHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.dao.FundSourceHibernateDAO;
import org.egov.commons.dao.SchemeHibernateDAO;
import org.egov.commons.dao.SubSchemeHibernateDAO;
import org.egov.commons.service.BankAccountService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.brs.BrsEntrieMis;
import org.egov.model.brs.BrsEntries;
import org.egov.model.voucher.BankEntriesNotInBankBook;
import org.egov.services.instrument.BankEntriesService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class BankEntriesNotInBankBookActionHelper {

    @Autowired
    @Qualifier("bankAccountService")
    private BankAccountService bankAccountService;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    @Autowired
    private CreateVoucher createVoucher;

    @Autowired
    private FunctionDAO functionDAO;

    @Autowired
    private FundHibernateDAO fundDAO;

    @Autowired
    private FunctionaryHibernateDAO functionaryDAO;

    @Autowired
    private SchemeHibernateDAO schemeDAO;

    @Autowired
    private SubSchemeHibernateDAO subSchemeDAO;

    @Autowired
    private FundSourceHibernateDAO fundSourceDAO;

    @Autowired
    @Qualifier("voucherHeaderService")
    private VoucherHeaderService voucherHeaderService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    @Qualifier("bankEntriesService")
    private BankEntriesService bankEntriesService;

    @Transactional
    public void create(CVoucherHeader voucherHeader, Integer bankaccount,
            List<BankEntriesNotInBankBook> bankEntriesNotInBankBookList) throws Exception {
        try {
            List<BrsEntries> bankEntries = new ArrayList<BrsEntries>();
            BrsEntries bankEntry = new BrsEntries();
            BrsEntrieMis bankEntryMis = new BrsEntrieMis();
            Bankaccount bankAccount = bankAccountService.findById(bankaccount.longValue(), false);
            CChartOfAccounts coa = new CChartOfAccounts();
            for (BankEntriesNotInBankBook object : bankEntriesNotInBankBookList) {

                if (object.getBeId() != null) {
                    if (object.getCreateVoucher() != null && object.getCreateVoucher()) {
                        bankEntry = new BrsEntries();
                        bankEntry = bankEntriesService.findById(object.getBeId(), false);
                        object.setDate(bankEntry.getTxnDate());
                        object.setType(bankEntry.getType());
                        object.setAmount(bankEntry.getTxnAmount());
                        bankEntry.setVoucherHeaderId(createVoucher(voucherHeader, object, bankAccount, bankEntry.getGlCodeId()));
                        bankEntriesService.update(bankEntry);
                    }
                } else {
                    bankEntry = new BrsEntries();
                    bankEntryMis = new BrsEntrieMis();
                    bankEntry.setBankaccountId(bankAccount);
                    bankEntry.setRefNo(object.getRefNum());
                    bankEntry.setType(object.getType());
                    bankEntry.setTxnDate(object.getDate());
                    bankEntry.setTxnAmount(object.getAmount());
                    bankEntry.setRemarks(object.getRemarks());
                    coa = chartOfAccountsHibernateDAO.findById(Long.valueOf(object.getGlcodeDetail()), false);
                    bankEntry.setGlCodeId(coa);
                    if (voucherHeader.getVouchermis().getFunction() != null
                            && voucherHeader.getVouchermis().getFunction().getId() != null)
                        bankEntryMis
                                .setFunction(functionDAO.getFunctionById(voucherHeader.getVouchermis().getFunction().getId()));
                    if (voucherHeader.getVouchermis().getDepartmentid() != null)
                        bankEntryMis.setDepartment(departmentService.getDepartmentById(voucherHeader.getVouchermis()
                                .getDepartmentid()
                                .getId()));
                    if (voucherHeader.getFundId() != null)
                        bankEntryMis.setFund(fundDAO.fundById(voucherHeader.getFundId().getId(), false));
                    if (voucherHeader.getVouchermis().getSchemeid() != null)
                        bankEntryMis.setScheme(schemeDAO.findById(voucherHeader.getVouchermis().getSchemeid().getId(), false));
                    if (voucherHeader.getVouchermis().getSubschemeid() != null)
                        bankEntryMis.setSubscheme(subSchemeDAO
                                .findById(voucherHeader.getVouchermis().getSubschemeid().getId(), false));
                    if (voucherHeader.getVouchermis().getFundsource() != null)
                        bankEntryMis.setFundsource(fundSourceDAO.findById(voucherHeader.getVouchermis().getFundsource().getId(),
                                false));
                    if (voucherHeader.getVouchermis().getFunctionary() != null)
                        bankEntryMis.setFunctionary(functionaryDAO
                                .findById(voucherHeader.getVouchermis().getFunctionary().getId(), false));
                    bankEntryMis.setBankentries(bankEntry);
                    bankEntry.getBankentriesMis().add(bankEntryMis);
                    if (object.getCreateVoucher() != null && object.getCreateVoucher())
                        bankEntry.setVoucherHeaderId(createVoucher(voucherHeader, object, bankAccount, coa));
                    bankEntries.add(bankEntry);

                }
            }
            for (BrsEntries object : bankEntries) {
                bankEntriesService.persist(object);
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
    }

    @Transactional
    public CVoucherHeader createVoucher(CVoucherHeader voucherHeader, BankEntriesNotInBankBook object, Bankaccount account,
            CChartOfAccounts coa) {
        voucherHeader.setName(FinancialConstants.JOURNALVOUCHER_NAME_GENERAL);
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
        voucherHeader.setVoucherDate(object.getDate());
        final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader);
        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
        final List<HashMap<String, Object>> subledgerdetails = new ArrayList<HashMap<String, Object>>();
        CVoucherHeader voucher = new CVoucherHeader();
        try {
            HashMap<String, Object> detailMap = null;
            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.GLCODE, coa.getGlcode());
            if (object.getType().equals("Receipt")) {
                detailMap.put(VoucherConstant.DEBITAMOUNT, BigDecimal.ZERO);
                detailMap.put(VoucherConstant.CREDITAMOUNT, object.getAmount());
            } else
            {
                detailMap.put(VoucherConstant.DEBITAMOUNT, object.getAmount());
                detailMap.put(VoucherConstant.CREDITAMOUNT, BigDecimal.ZERO);
            }
            accountdetails.add(detailMap);

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts().getGlcode());
            if (object.getType().equals("Receipt")) {
                detailMap.put(VoucherConstant.DEBITAMOUNT, object.getAmount());
                detailMap.put(VoucherConstant.CREDITAMOUNT, BigDecimal.ZERO);
            }
            else
            {
                detailMap.put(VoucherConstant.DEBITAMOUNT, BigDecimal.ZERO);
                detailMap.put(VoucherConstant.CREDITAMOUNT, object.getAmount());
            }
            accountdetails.add(detailMap);
            voucher = createVoucher.createVoucher(headerDetails, accountdetails, subledgerdetails);

            voucher.setStatus(0);
            voucherHeaderService.applyAuditing(voucher);
            voucherHeaderService.persist(voucher);
        } catch (final ValidationException e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return voucher;
    }

    protected HashMap<String, Object> createHeaderAndMisDetails(CVoucherHeader voucherHeader) throws ValidationException
    {
        final HashMap<String, Object> headerdetails = new HashMap<String, Object>();
        headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeader.getName());
        headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeader.getType());
        headerdetails.put((String) VoucherConstant.VOUCHERSUBTYPE, voucherHeader.getVoucherSubType());
        headerdetails.put(VoucherConstant.VOUCHERNUMBER, voucherHeader.getVoucherNumber());
        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
        headerdetails.put(VoucherConstant.DESCRIPTION, voucherHeader.getDescription());

        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            headerdetails.put(VoucherConstant.DEPARTMENTCODE, voucherHeader.getVouchermis().getDepartmentid().getCode());
        if (voucherHeader.getFundId() != null)
            headerdetails.put(VoucherConstant.FUNDCODE, voucherHeader.getFundId().getCode());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            headerdetails.put(VoucherConstant.SCHEMECODE, voucherHeader.getVouchermis().getSchemeid().getCode());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            headerdetails.put(VoucherConstant.SUBSCHEMECODE, voucherHeader.getVouchermis().getSubschemeid().getCode());
        if (voucherHeader.getVouchermis().getFundsource() != null)
            headerdetails.put(VoucherConstant.FUNDSOURCECODE, voucherHeader.getVouchermis().getFundsource().getCode());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            headerdetails.put(VoucherConstant.DIVISIONID, voucherHeader.getVouchermis().getDivisionid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            headerdetails.put(VoucherConstant.FUNCTIONARYCODE, voucherHeader.getVouchermis().getFunctionary().getCode());
        if (voucherHeader.getVouchermis().getFunction() != null)
            headerdetails.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
        return headerdetails;
    }

}