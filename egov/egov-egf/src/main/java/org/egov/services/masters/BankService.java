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

package org.egov.services.masters;

import org.egov.commons.Bank;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.FinancialConstants;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
public class BankService extends PersistenceService<Bank, Integer> {

    public static final String BANK_BRANCH_ID = "bankBranchId";
    public static final String BANK_BRANCH_NAME = "bankBranchName";
    public static final String FUND_ID = "fundId";

    public BankService() {
        super(Bank.class);
    }

    public BankService(final Class<Bank> type) {
        super(type);
    }

    public List<Map<String,Object>> getPaymentApprovedBankAndBranchName(Integer fundId, Date asOnDate) {
        List<Map<String,Object>> bankBranches = new ArrayList<>();
        final List<String> addedBanks = new ArrayList<>();
        for (final Object[] account : fetchBankAndBranchNameHasApprovedPayment(fundId, asOnDate)) {
            final String bankBranchName = account[1].toString();
            if (!addedBanks.contains(bankBranchName)) {
                addedBanks.add(bankBranchName);
                final Map<String, Object> bankBrmap = new HashMap<>();
                bankBrmap.put(BANK_BRANCH_ID, account[0].toString());
                bankBrmap.put(BANK_BRANCH_NAME, bankBranchName);
                bankBranches.add(bankBrmap);
            }
        }
        return bankBranches;
    }

    public List<Map<String, Object>> getChequeAssignedBankAndBranchName (Date asOnDate){
        List<Map<String, Object>> bankBranches = new ArrayList<>();
        for (final Object[] element : fetchBankAndBankBranchWithAssignedCheques(asOnDate)) {
            Map<String, Object> bankBrmap = new HashMap<>();
            bankBrmap.put(BANK_BRANCH_ID, element[0].toString());
            bankBrmap.put(BANK_BRANCH_NAME, element[1].toString());
            bankBranches.add(bankBrmap);
        }
        return bankBranches;
    }

    public List<Map<String, Object>> getRTGSAssignedBankAndBranchName(Date asOnDate) {
        final List<Object[]> bankBranch = fetchBankAndBranchNameWithRTGSAssigned(asOnDate);
        List<Map<String, Object>> bankBranches = new ArrayList<>();
        for (final Object[] element : bankBranch) {
            Map<String, Object> bankBrmap = new HashMap<>();
            bankBrmap.put(BANK_BRANCH_ID, element[0].toString());
            bankBrmap.put(BANK_BRANCH_NAME, element[1].toString());
            bankBranches.add(bankBrmap);
        }
        return bankBranches;
    }

    public List<Map<String, Object>> getAllBankAndBranchName(Integer fundId) {
        List<Map<String, Object>> bankBranchList = new ArrayList<>();
        for (final Object[] element : fetchAllBankAndBankbranchName(fundId)) {
            Map<String, Object> bankBrmap = new HashMap<>();
            bankBrmap.put(BANK_BRANCH_ID, element[0].toString());
            bankBrmap.put(BANK_BRANCH_NAME, element[1].toString());
            bankBranchList.add(bankBrmap);
        }
        return bankBranchList;
    }

    public List<Map<String, Object>> getBankByFundAndType(Integer fundId, String typeOfAccount) {
        List<Map<String, Object>> bankBranchList = new ArrayList<>();
        for (final Object[] element : fetchBankByFundAndTypeOfAccount(fundId, typeOfAccount)) {
            Map<String, Object> bankBrmap = new HashMap<>();
            bankBrmap.put(BANK_BRANCH_ID, element[0].toString());
            bankBrmap.put(BANK_BRANCH_NAME, element[1].toString());
            bankBranchList.add(bankBrmap);
        }
        return bankBranchList;
    }

    private List<Object[]> fetchBankByFundAndTypeOfAccount(final Integer fundId, final String typeOfAccount) {
        final StringBuilder query = new StringBuilder();
        query.append("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname ").
                append("FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount where  bank.isactive=true  and bankBranch.isactive=true and ").
                append(" bankaccount.isactive=true and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id ");
        if (fundId != null)
            query.append("and bankaccount.fund.id=:fundId and bankaccount.type in(");
        else
            query.append("and bankaccount.type in(");
        if (typeOfAccount.indexOf(',') != -1) {
            String[] strArray = typeOfAccount.split(",");
            int index = 0;
            for (final String type : strArray) {
                query.append("'").append(type).append("'");
                if (strArray.length > ++index)
                    query.append(",");

            }
        } else
            query.append("'").append(typeOfAccount).append("'");

        query.append(") order by 2 ");

        List<Object[]> bankBranch;
        if (fundId != null)
            bankBranch = getSession().createQuery(query.toString()).setInteger(FUND_ID, fundId).list();
        else
            bankBranch = getSession().createQuery(query.toString()).list();
        return bankBranch;
    }

    private List<Object[]> fetchAllBankAndBankbranchName(final Integer fundId) {
        StringBuilder query = new StringBuilder();
        query.append("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),bankBranch.branchname) as bankbranchname ").
                append(" FROM Bank bank,Bankbranch bankBranch,Bankaccount bankaccount  where  bank.isactive=true  and bankBranch.isactive=true and bankaccount.isactive=true ").
                append("and bank.id = bankBranch.bank.id and bankBranch.id = bankaccount.bankbranch.id and bankaccount.fund.id=:fundId order by 2");
        return getSession().createSQLQuery(query.toString())
                .setInteger(FUND_ID, fundId)
                .list();
    }

    private List<Object[]> fetchBankAndBranchNameWithRTGSAssigned(final Date asOnDate) {
        StringBuilder vouchersWithNewInstrumentsQuery = new StringBuilder().append("select voucherheaderid from egf_instrumentvoucher eiv,egf_instrumentheader ih,").
                append(" egw_status egws where eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New' and ").
                append(" ih.transactionNumber is not null");

        StringBuilder queryString = new StringBuilder();
        queryString = queryString.append("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),").
                append("bankBranch.branchname) as bankbranchname from  voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount, ").
                append(" paymentheader ph where  ph.voucherheaderid=vh.id and vh.id  in (").append(vouchersWithNewInstrumentsQuery).
                append(") and bank.isactive=true  and bankBranch.isactive=true ").
                append(" and  bank.id = bankBranch.bankid and bankBranch.id = bankaccount.BRANCHID and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and vh.voucherdate <= :date").
                append(" and ph.bankaccountnumberid=bankaccount.id  and bankaccount.isactive=true order by 2");
        return getSession().createSQLQuery(queryString.toString())
                .setParameter("date", asOnDate)
                .list();
    }

    private List<Object[]> fetchBankAndBankBranchWithAssignedCheques(Date asOnDate) {
        final StringBuilder vouchersWithNewInstrumentsQuery = new StringBuilder().append("select voucherheaderid from egf_instrumentvoucher eiv,egf_instrumentheader ih,").
                        append(" egw_status egws where eiv.instrumentheaderid=ih.id and egws.id=ih.id_status and egws.moduletype='Instrument' and egws.description='New' ");
        StringBuilder queryString = new StringBuilder();
        queryString.append("select DISTINCT concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),").
                append("bankBranch.branchname) as bankbranchname from  voucherheader vh,Bank bank,Bankbranch bankBranch,Bankaccount bankaccount, ").
                append(" paymentheader ph where ph.voucherheaderid=vh.id and vh.id  in (").append(vouchersWithNewInstrumentsQuery.toString()).
                append(") and bank.isactive=true  and bankBranch.isactive=true ").
                append(" and  bank.id = bankBranch.bankid and bankBranch.id = bankaccount.BRANCHID and bankaccount.type in ('RECEIPTS_PAYMENTS','PAYMENTS') and vh.voucherdate <= :date").
                append(" and ph.bankaccountnumberid=bankaccount.id  and bankaccount.isactive=true order by 2");
        return getSession().createSQLQuery(queryString.toString())
                .setParameter("date", asOnDate)
                .list();
    }

    private List<Object[]> fetchBankAndBranchNameHasApprovedPayment(Integer fundId, Date asOnDate) {
        StringBuilder queryString = new StringBuilder();
        // query to fetch vouchers for which no cheque has been assigned
        queryString.append("select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),").
                append(" bankBranch.branchname) as bankbranchname  from Bank bank,  Bankbranch bankBranch,   Bankaccount bankaccount where bankaccount.id in ( ").
                append(" select DISTINCT ph.bankaccountnumberid from  paymentheader ph,egf_instrumentvoucher iv right outer join voucherheader vh on ").
                append(" vh.id =iv.VOUCHERHEADERID where ph.voucherheaderid=vh.id  and  vh.status=0  and ").append(" ph.voucherheaderid=vh.id  and iv.VOUCHERHEADERID is null ");
        if (fundId != null && fundId > 0)
            queryString.append(" and vh.fundid=:fundId");
        queryString.append(" and vh.name NOT IN ( '").
                append(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE).append("','").
                append(FinancialConstants.PAYMENTVOUCHER_NAME_SALARY).append("') ").
                append("and vh.voucherdate <= :asOnDate ) AND bank.id = bankBranch.bankid AND bank.isactive=true AND bankBranch.isactive=true ").
                append("AND bankaccount.type IN ('RECEIPTS_PAYMENTS','PAYMENTS') AND bankBranch.id = bankaccount.branchid");
        if (fundId != null && fundId > 0)
            queryString.append(" and bankaccount.fundid=:fundId");
        queryString.append(" union select distinct concat(concat(bank.id,'-'),bankBranch.id) as bankbranchid,concat(concat(bank.name,' '),").
                append("bankBranch.branchname) as bankbranchname from Bank bank,  Bankbranch bankBranch,   Bankaccount bankaccount where bankaccount.id in ( ").
                append(" select DISTINCT ph.bankaccountnumberid from egf_instrumentvoucher iv,voucherheader vh,").
                append(" paymentheader ph,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, ").
                append("(select bankid,bankaccountid,instrumentnumber,max(id) as id from egf_instrumentheader group by bankid,bankaccountid,").
                append("instrumentnumber) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber ").
                append(" and max_rec.id=ih1.id) ih where ph.voucherheaderid=vh.id and vh.status=0  and ph.voucherheaderid=vh.id and iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and ").
                append("ih.id_status=egws.id and egws.description in  ('Surrendered','Surrender_For_Reassign')");
        if (fundId != null && fundId > 0)
            queryString.append(" and vh.fundid=:fundId");
        queryString.append("  and vh.voucherdate <= :asOnDate and vh.name NOT IN ( '").
                append(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE).append("','").
                append(FinancialConstants.PAYMENTVOUCHER_NAME_SALARY).append("' ) ) ").
                append(" AND bank.id = bankBranch.bankid AND bank.isactive=true AND bankBranch.isactive=true ").
                append("AND bankaccount.type IN ('RECEIPTS_PAYMENTS','PAYMENTS') AND bankBranch.id = bankaccount.branchid");
        if (fundId != null && fundId > 0)
            queryString.append(" and bankaccount.fundid=:fundId");
        return getSession().createSQLQuery(queryString.toString())
                .setInteger(FUND_ID, fundId)
                .setDate("asOnDate", asOnDate)
                .list();
    }
}
