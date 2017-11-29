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
package org.egov.commons.service;

import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.utils.BankAccountType;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Transactional(readOnly = true)
public class BankAccountService extends PersistenceService<Bankaccount, Long> {

    public static final String STANDARD_VOUCHER_TYPE_PAYMENT = "Payment";
    public static final String PAYMENTVOUCHER_NAME_REMITTANCE = "Remittance Payment";
    public static final String PAYMENTVOUCHER_NAME_SALARY = "Salary Bill Payment";
    public static final String BRANCH_ID = "branchId";

    public BankAccountService() {
       super(Bankaccount.class);
    }

    public BankAccountService(final Class<Bankaccount> type) {
        super(type);
    }

    public List<Bankaccount> getBankaccountsHasApprovedPayment(Integer fundId, Integer branchId) {
        List<Bankaccount> bankaccounts = new ArrayList<>();
        final List<String> addedBanks = new ArrayList<>();
        for (final Object[] account : fetchBanAccountNoAndBankNameForApprovedPayment(fundId, branchId)) {
            final String accountNumberAndType = account[0].toString() + "-" + account[1].toString();
            if (!addedBanks.contains(accountNumberAndType)) {
                Bankaccount bankaccount = new Bankaccount();
                bankaccount.setAccountnumber(account[0].toString());
                Bank bank = new Bank();
                bank.setName(account[1].toString());
                Bankbranch branch = new Bankbranch();
                branch.setBank(bank);
                bankaccount.setBankbranch(branch);
                CChartOfAccounts chartofaccounts = new CChartOfAccounts();
                chartofaccounts.setGlcode(account[3].toString());
                bankaccount.setChartofaccounts(chartofaccounts);
                bankaccount.setId(Long.valueOf(account[2].toString()));
                addedBanks.add(accountNumberAndType);
                bankaccounts.add(bankaccount);
            }
        }
        return bankaccounts;
    }

    public List<Bankaccount> getBankaccountsWithAssignedCheques(Integer branchId, String chequeType, Date asOnDate) {
        final List<Bankaccount> bankAccounts =  new ArrayList<>();
        final List<String> addedBanks = new ArrayList<>();
        for (final Object[] account : fetchBankaccountsWithAssignedCheques(branchId, chequeType, asOnDate)) {
            //FIXME did not understand the logic of below line, please correct it
            final String accountNumberAndType = account[0] != null ? account[0].toString()
                    : ""+"-" + account[4] != null ? account[4].toString() : EMPTY;
            if (!addedBanks.contains(accountNumberAndType)) {
                final Bankaccount bankaccount = new Bankaccount();
                bankaccount.setAccountnumber(account[0] != null ? account[0].toString() : EMPTY);
                bankaccount.setId(Long.valueOf(account[2] != null ? account[2].toString() : EMPTY));
                final Bankbranch branch = new Bankbranch();
                final Bank bank = new Bank();
                bank.setName(account[4].toString());
                branch.setBank(bank);
                bankaccount.setBankbranch(branch);
                final CChartOfAccounts chartofaccounts = new CChartOfAccounts();
                chartofaccounts.setGlcode(account[3] != null ? account[3].toString() : EMPTY);
                bankaccount.setChartofaccounts(chartofaccounts);
                addedBanks.add(accountNumberAndType);
                bankAccounts.add(bankaccount);
            }
        }
        return bankAccounts;
    }

    public List<Bankaccount> getBankaccountsWithAssignedRTGS(Integer branchId, Date asOnDate) {
        List<Bankaccount> bankaccounts = new ArrayList<>();
        final List<String> addedBanks = new ArrayList<>();
        for (final Object[] account : fetchBankaccountsWithAssignedRTGS(branchId, asOnDate)) {
            final String accountNumberAndType = account[0].toString() + "-" + account[1].toString();
            if (!addedBanks.contains(accountNumberAndType)) {
                final Bankaccount bankaccount = new Bankaccount();
                final CChartOfAccounts chartofaccounts = new CChartOfAccounts();
                bankaccount.setAccountnumber(account[0].toString());
                bankaccount.setAccounttype(account[1].toString());
                bankaccount.setId(Long.valueOf(account[2].toString()));
                chartofaccounts.setGlcode(account[3].toString());
                bankaccount.setChartofaccounts(chartofaccounts);
                addedBanks.add(accountNumberAndType);
                bankaccounts.add(bankaccount);
            }
        }
        return bankaccounts;
    }

    public List<Bankaccount> getBankAccounts(Integer fundId, Integer branchId, Integer bankId, String typeOfAccount) {
        List<Bankaccount> bankaccounts;
        if (isNotBlank(typeOfAccount)) {
            if (typeOfAccount.indexOf(',') != -1) {
                final String[] strArray = typeOfAccount.split(",");
                if (fundId != null) {
                    bankaccounts = findAllBy(
                            "from Bankaccount ba where ba.bankbranch.id=? and ba.fund.id=? and ba.bankbranch.bank.id=? and isactive=true and type in (?, ?) order by ba.chartofaccounts.glcode",
                            branchId, fundId, bankId, BankAccountType.valueOf(strArray[0].toUpperCase()),
                            BankAccountType.valueOf(strArray[1].toUpperCase()));
                } else {
                    bankaccounts = findAllBy(
                            "from Bankaccount ba where ba.bankbranch.id=? and  ba.bankbranch.bank.id=? and isactive=true and type in (?, ?) order by ba.chartofaccounts.glcode",
                            branchId, bankId, BankAccountType.valueOf(strArray[0]),
                            BankAccountType.valueOf(strArray[1]));
                }
            } else if (fundId != null) {
                bankaccounts = findAllBy(
                        "from Bankaccount ba where ba.bankbranch.id=? and ba.fund.id=? and ba.bankbranch.bank.id=? and isactive=true and type in (?) order by ba.chartofaccounts.glcode",
                        branchId, fundId, bankId, typeOfAccount);
            } else {
                bankaccounts = findAllBy(
                        "from Bankaccount ba where ba.bankbranch.id=?  and ba.bankbranch.bank.id=? and isactive=true and type in (?) order by ba.chartofaccounts.glcode",
                        branchId, bankId, typeOfAccount);
            }
        } else if (fundId != null) {
            bankaccounts = findAllBy(
                    "from Bankaccount ba where ba.bankbranch.id=? and ba.fund.id=? and ba.bankbranch.bank.id=? and isactive=true order by ba.chartofaccounts.glcode",
                    branchId, fundId, bankId);
        } else {
            bankaccounts = findAllBy(
                    "from Bankaccount ba where ba.bankbranch.id=?  and ba.bankbranch.bank.id=? and isactive=true order by ba.chartofaccounts.glcode",
                    branchId, bankId);
        }
        return bankaccounts;
    }

    private List<Object[]> fetchBankaccountsWithAssignedRTGS(final Integer branchId, final Date asOnDate) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode ").
                append(" from  voucherheader vh,chartofaccounts coa,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,paymentheader ph,  ").
                append("egf_instrumentvoucher eiv,egf_instrumentheader ih,egw_status egws ").
                append("where ph.voucherheaderid=vh.id and coa.id=bankaccount.glcodeid and vh.id=eiv.VOUCHERHEADERID and ").
                append("  eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New' and ih.transactionNumber is not null").
                append("and ih.instrumenttype=(select id from egf_instrumenttype where upper(type)='CHEQUE') and ispaycheque='1' ").
                append(" and bank.isactive=true  and bankBranch.isactive=true and bankaccount.isactive=true ").
                append(" and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.branchid=:branchId").
                append("  and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and vh.voucherdate <= :asOnDate and ph.bankaccountnumberid=bankaccount.id  order by vh.voucherdate desc");
        return getSession().createSQLQuery(queryString.toString()).
                setDate("asOnDate", asOnDate).
                setInteger(BRANCH_ID, branchId).
                list();
    }

    private List<Object[]> fetchBankaccountsWithAssignedCheques(final Integer branchId, String chequeType, final Date asOnDate) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select bankaccount.accountnumber as accountnumber,bankaccount.accounttype as accounttype,cast(bankaccount.id as integer) as id,coa.glcode as glCode ,bank.name as bankName").
                append(" from  voucherheader vh,chartofaccounts coa,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount,paymentheader ph,  ").
                append("egf_instrumentvoucher eiv,egf_instrumentheader ih,egw_status egws ").
                append("where ph.voucherheaderid=vh.id and coa.id=bankaccount.glcodeid and vh.id=eiv.VOUCHERHEADERID and ").
                append("  eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New' ").
                append("and ih.instrumenttype=(select id from egf_instrumenttype where upper(type)=:type) and ispaycheque='1' ").
                append(" and bank.isactive=true  and bankBranch.isactive=true and bankaccount.isactive=true ").
                append(" and bank.id = bankBranch.bankid and bankBranch.id = bankaccount.branchid and bankaccount.branchid=:branchId").
                append("  and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and vh.voucherdate <= :asOnDate").
                append(" and ph.bankaccountnumberid=bankaccount.id  order by vh.voucherdate desc");
        return getSession().createSQLQuery(queryString.toString()).
                setDate("asOnDate", asOnDate).
                setInteger(BRANCH_ID, branchId).
                setString("type", isBlank(chequeType) ? "CHEQUE" : chequeType).
                list();
    }

    private List<Object[]> fetchBanAccountNoAndBankNameForApprovedPayment(final Integer fundId, final Integer branchId) {
        StringBuilder queryString = new StringBuilder();
        // query to fetch vouchers for which no cheque has been assigned
        queryString.append("SELECT  bankaccount.accountnumber AS accountnumber,  bank.name AS bankName,").
                append(" CAST(bankaccount.id AS INTEGER) AS id, coa.glcode AS glCode  FROM chartofaccounts coa, bankaccount bankaccount ,bankbranch branch,bank bank ").
                append(" WHERE bankaccount.ID IN (SELECT DISTINCT PH.bankaccountnumberid  ").
                append(" FROM   paymentheader ph,  voucherheader vh left OUTER JOIN egf_instrumentvoucher iv ON vh.id =iv.VOUCHERHEADERID").
                append(" WHERE ph.voucherheaderid  =vh.id AND vh.status=0 AND VH.FUNDID=:fundId AND ph.voucherheaderid    =vh.id").
                append(" AND iv.VOUCHERHEADERID   IS NULL AND vh.name NOT IN ( 'Remittance Payment','Salary Bill Payment' ))").
                append(" AND coa.id = bankaccount.glcodeid AND bankaccount.type     IN ('RECEIPTS_PAYMENTS','PAYMENTS') AND bankaccount.fundid =:fundId").
                append(" AND bankaccount.branchid = branch.id and branch.bankid = bank.id and  bankaccount.branchid  =:branchId").
                append(" and bankaccount.isactive=true union select bankaccount.accountnumber as accountnumber,bank.name as bankName,").
                append("cast(bankaccount.id as integer) as id,coa.glcode as glCode from chartofaccounts coa, Bankaccount bankaccount  ,bankbranch branch,bank bank ").
                append(" where bankaccount.branchid = branch.id and branch.bankid = bank.id and  bankaccount.id in(SELECT DISTINCT PH.bankaccountnumberid  from  ").
                append(" egf_instrumentvoucher iv,voucherheader vh, paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, ").
                append(" (select bankid,bankaccountid,instrumentnumber,max(id) as id from egf_instrumentheader group by bankid,bankaccountid,").
                append(" instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber ").
                append(" and max_rec.id=ih1.id) ih where ph.voucherheaderid=vh.id  and vh.fundid=:fundId").
                append(" and vh.status=0 and  ph.voucherheaderid=vh.id and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id ").
                append(" and ph.bankaccountnumberid=bankaccount.id  and vh.type='").append(STANDARD_VOUCHER_TYPE_PAYMENT).append("'").
                append(" and vh.name NOT IN ( '").append(PAYMENTVOUCHER_NAME_REMITTANCE).append("','").
                append(PAYMENTVOUCHER_NAME_SALARY).append("' ) and ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign') )").
                append(" and coa.id=bankaccount.glcodeid and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS')  and bankaccount.branchid=:branchId");
        if (fundId != null && fundId > 0)
                queryString.append(" and bankaccount.fundid=:fundId");

        return getSession().createSQLQuery(queryString.toString()).
                setInteger("fundId", fundId).
                setInteger(BRANCH_ID, branchId).
                list();
    }

}