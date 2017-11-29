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

package org.egov.services.pea;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

@Transactional(readOnly = true)
public class TransferClosingBalanceService extends PersistenceService {

    private static final SimpleDateFormat FORMATDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);

    @Autowired
    @Qualifier("financialYearDAO")
    private FinancialYearHibernateDAO financialYearDAO;

    public TransferClosingBalanceService() {
        super(null);
    }

    public TransferClosingBalanceService(Class type) {
        super(type);
    }


    @Transactional
    public void transfer(Long financialYear, CFinancialYear fy,
            CFinancialYear nextFinancialYear) {

        Query query = null;

        deleteNextFYTransactionSummary(nextFinancialYear);

        String fyStartingDate = FORMATDDMMYYYY.format(fy.getStartingDate());
        String fyEndingDate = FORMATDDMMYYYY.format(fy.getEndingDate());

        /*
         * Processing all the COA which are non- subledger codes. Also we will process for all COAs that are control codes which
         * are having data with accountdetail type which is not same as that of what is mentioned in the database. The result data
         * is been inserted into the opening balance Note- COA code for Excess IE is been excluded for processing as this will be
         * taken care separately.
         */
        query = getSession().createSQLQuery(
                getQueryForNonControlCodesAndMisMatchsInControlCodes(financialYear, fyStartingDate, fyEndingDate,
                        nextFinancialYear));
        query.executeUpdate();

        /*
         * Processing all control codes both transaction and opening balance and the net balance is been inserted as the opening
         * balance.
         */
        query = null;
        query = getSession().createSQLQuery(
                getQueryForControlCodes(financialYear, fyStartingDate, fyEndingDate, nextFinancialYear));
        query.executeUpdate();

        /*
         * COA for Excess IE transaction balance + Opening balance will be calculated along with the Income - expenses for that
         * year.
         */
        query = null;
        query = getSession().createSQLQuery(
                getQueryForIncomeOverExpense(financialYear, fyStartingDate, fyEndingDate, nextFinancialYear));
        query.executeUpdate();

        updateCurrentYearTransferClosingBalance(fy);
    }

    @Transactional
    public void deleteNextFYTransactionSummary(CFinancialYear nextFinancialYear) {
        Query query = null;
        query = getSession().createSQLQuery(
                "delete from TransactionSummary where financialyearid = "
                        + nextFinancialYear.getId() + "");
        query.executeUpdate();
    }

    @Transactional
    public void updateCurrentYearTransferClosingBalance(CFinancialYear fy) {
        fy.setTransferClosingBalance(true);
        financialYearDAO.update(fy);
    }

    /**
     * This function is called to calculate the closing balance for GlCodes of type A,L (Excluding ExcessIE code)
     * 
     * Transaction entries for Non-Control codes(1st Query)
     * 
     * UNION
     * 
     * Opening Balance entries for Non-Control codes(2nd Query)
     * 
     * UNION
     * 
     * Mismatch Transaction entries for Control codes(3rd Query)
     * 
     * UNION
     * 
     * Mismatch Opening Balance entries for Control codes(4th Query)
     * 
     */

    private String getQueryForNonControlCodesAndMisMatchsInControlCodes(Long financialYear, String fyStartingDate,
            String fyEndingDate, CFinancialYear nextFinancialYear) {
        StringBuilder query = new StringBuilder();
        query.append(" INSERT INTO TransactionSummary (id, financialYearId, lastmodifiedby, glcodeid,fundId,departmentid,functionid ,openingdebitbalance, openingcreditbalance, accountdetailtypeid, accountdetailkey,lastmodifieddate)");
        query.append(" SELECT nextval('seq_transactionsummary'), ");
        query.append(nextFinancialYear.getId());
        query.append(" , ");
        query.append(ApplicationThreadLocals.getUserId());
        query.append(" ,glcodeId AS glCodeId, fundid AS fundId,deptId AS deptId ,functionid AS functionId,CASE WHEN balance > 0 THEN abs(balance) ELSE 0 END AS openingbalancedebitamount,CASE WHEN balance < 0 THEN abs(balance) ELSE 0 END AS openingbalancecreditamount,null,null,current_date ");
        query.append(" FROM ( ");
        query.append(" SELECT glcodeId AS glCodeId,fundId AS fundId,deptId AS deptId,functionid AS functionId,SUM(dr) AS dr,SUM(cr) AS cr,SUM(balance) AS balance ");
        query.append(" FROM ( ");

        // Transaction entries for Non-Control codes(1st Query)

        query.append(" SELECT gl.glcodeId AS glCodeId,vh.fundId AS fundId,mis.departmentid  AS deptId, gl.functionid AS functionId,SUM(CASE WHEN debitamount = 0 THEN 0 ELSE debitamount END) AS dr, ");
        query.append(" SUM(CASE WHEN creditAmount = 0 THEN 0 ELSE creditAmount END) AS cr,(SUM(CASE WHEN debitamount = 0 THEN 0 ELSE debitamount END) - SUM(CASE WHEN creditAmount = 0 THEN 0 ELSE creditAmount END)) AS balance ");
        query.append(" FROM voucherHeader vh,vouchermis mis,chartOfAccounts coa,generalledger gl LEFT JOIN generalledgerdetail gld ON gl.id = gld.generalledgerid ");
        query.append(" WHERE gld.id IS NULL AND vh.id = gl.voucherHeaderId AND gl.glCode=coa.glcode ");
        query.append(" AND (coa.purposeid IS NULL OR coa.purposeid NOT IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) ) AND vh.id = mis.voucherheaderid AND vh.voucherDate >=to_date('");
        query.append(fyStartingDate);
        query.append("','dd/mm/yyyy') ");
        query.append(" AND vh.voucherDate <=to_date('");
        query.append(fyEndingDate);
        query.append("','dd/mm/yyyy') AND vh.status NOT  IN(4,5) AND coa.type IN('A','L') ");
        query.append(" GROUP BY gl.glcodeId,vh.fundId,mis.departmentid,gl.functionid ");

        query.append(" UNION ALL ");

        // Opening Balance entries for Non-Control codes(2nd Query)

        query.append(" SELECT ts.glcodeid AS glCodeId,ts.fundid AS fundId,ts.departmentid  AS deptId,ts.functionid AS functionId,SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0 ELSE ts.openingdebitbalance END) AS dr, ");
        query.append(" SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END) AS cr,(SUM( CASE WHEN ts.openingdebitbalance = 0 THEN 0 ELSE ts.openingdebitbalance END) - SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END)) AS balance ");
        query.append(" FROM transactionsummary ts,chartofaccounts coa ");
        query.append(" WHERE  ts.ACCOUNTDETAILKEY  IS NULL AND ts.ACCOUNTDETAILTYPEID IS NULL AND coa.id = ts.glcodeid AND (coa.purposeid IS NULL OR coa.purposeid NOT IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE') ) ");
        query.append(" AND coa.type IN('A','L') AND ts.financialyearid = ");
        query.append(financialYear);
        query.append(" ");
        query.append(" GROUP BY ts.glcodeid,ts.fundid ,ts.departmentid ,ts.functionid ");

        query.append(" UNION ALL ");

        // Mismatch Transaction entries for Control codes(3rd Query)

        query.append(" SELECT gl.glcodeId AS glCodeId,vh.fundId AS fundId,mis.departmentid AS deptId,gl.functionid AS functionId,SUM(CASE WHEN gl.debitamount = 0 THEN 0 ELSE gld.amount END) AS dr, SUM(CASE WHEN gl.creditamount = 0 THEN 0 ELSE gld.amount END) AS cr, ");
        query.append(" SUM(CASE WHEN gl.debitamount = 0 THEN 0 ELSE gld.amount END)-SUM(CASE WHEN gl.creditamount = 0 THEN 0 ELSE gld.amount END) AS balance ");
        query.append(" FROM voucherHeader vh, vouchermis mis, chartOfAccounts coa,generalledger gl,generalLedgerDetail gld");
        query.append(" WHERE  vh.id= gl.voucherHeaderId  AND vh.id =mis.voucherheaderid AND gl.glCode =coa.glcode AND (coa.purposeid IS NULL OR coa.purposeid NOT IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) ) ");
        query.append(" AND gl.id  = gld.generalLedgerId AND gld.detailtypeid NOT IN (SELECT coadtl.detailtypeid FROM chartofaccountdetail coadtl WHERE coadtl.glcodeid = coa.id ) AND vh.voucherDate >=to_date('");
        query.append(fyStartingDate);
        query.append("','dd/mm/yyyy') AND vh.voucherDate <=to_date('");
        query.append(fyEndingDate);
        query.append("','dd/mm/yyyy') AND coa.type IN('A','L') AND vh.status NOT  IN(4,5) ");
        query.append(" GROUP BY gl.glcodeId,vh.fundId,mis.departmentid,gl.functionid ");

        query.append(" UNION ALL ");

        // Mismatch Opening Balance entries for Control codes(4th Query)
        query.append(" SELECT ts.glcodeid AS glCodeId,ts.fundid AS fundId,ts.departmentid  AS deptId,ts.functionid AS functionId,SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0 ELSE ts.openingdebitbalance END) AS dr, ");
        query.append(" SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance  END) AS cr,(SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0 ELSE ts.openingdebitbalance END) - SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END)) AS balance ");
        query.append(" FROM transactionsummary ts,chartofaccounts coa ");
        query.append(" WHERE (ts.accountdetailtypeid is not null and ts.accountdetailtypeid NOT IN (SELECT coadtl.detailtypeid FROM chartofaccountdetail coadtl WHERE coadtl.glcodeid = coa.id )) AND (coa.purposeid   IS NULL OR coa.purposeid NOT IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) ) ");
        query.append(" AND coa.id = ts.glcodeid AND coa.type IN('A','L') AND ts.financialyearid = ");
        query.append(financialYear);
        query.append("");
        query.append(" GROUP BY ts.glcodeid,ts.fundid ,ts.departmentid ,ts.functionid");

        query.append(") closingbalance");
        query.append(" GROUP BY glcodeId ,fundId ,deptId ,functionid ");
        query.append(" ORDER BY glcodeId ,fundId ,deptId ,functionid ) final");
        return query.toString();

    }

    /**
     * This function is called to calculate the closing balance for GlCodes of type A,L (Excluding ExcessIE code)
     * 
     * Transaction entries for Control codes(1st Query)
     * 
     * UNION
     * 
     * Opening Balance entries for Control codes(2nd Query)
     * 
     * 
     */
    private String getQueryForControlCodes(Long financialYear, String fyStartingDate, String fyEndingDate,
            CFinancialYear nextFinancialYear) {
        StringBuilder query = new StringBuilder();
        query.append(" INSERT INTO TransactionSummary (id, financialYearId, lastmodifiedby, glcodeid,fundId,departmentid,functionid , accountdetailtypeid, accountdetailkey,openingdebitbalance, openingcreditbalance,lastmodifieddate)");
        query.append(" SELECT nextval('seq_transactionsummary'), ");
        query.append(nextFinancialYear.getId());
        query.append(" , ");
        query.append(ApplicationThreadLocals.getUserId());
        query.append(" ,glcodeId AS glCodeId, fundid AS fundId,deptId AS deptId ,functionid AS functionId, detailTypeId  AS detailTypeId,detailKeyId AS detailKeyId, CASE WHEN balance > 0 THEN abs(balance) ELSE 0 END AS openingbalancedebitamount, CASE WHEN balance < 0 THEN abs(balance) ELSE 0 END AS openingbalancecreditamount,current_date ");
        query.append(" FROM ( ");
        query.append(" SELECT glcodeId AS glCodeId,fundId AS fundId, deptId AS deptId,functionid AS functionId,detailTypeId  AS detailTypeId,detailKeyId AS detailKeyId,SUM(dr) AS dr,SUM(cr) AS cr,SUM(balance)   AS balance ");
        query.append(" FROM (");

        // Transaction entries for Control codes(1st Query)

        query.append(" SELECT gl.glcodeId AS glCodeId,vh.fundId AS fundId,mis.departmentid  AS deptId,gl.functionid AS functionId,gld.detailTypeId  AS detailTypeId,gld.detailKeyId AS detailKeyId,SUM(CASE WHEN gl.debitamount = 0 THEN 0 ELSE gld.amount END) AS dr, ");
        query.append(" SUM(CASE WHEN gl.creditamount = 0 THEN 0 ELSE gld.amount END) AS cr,SUM(CASE WHEN gl.debitamount = 0 THEN 0 ELSE gld.amount END)-SUM(CASE WHEN gl.creditamount = 0   THEN 0 ELSE gld.amount END) AS balance ");
        query.append(" FROM voucherHeader vh,vouchermis mis,chartOfAccounts coa,chartofaccountdetail coadtl,generalledger gl,generalLedgerDetail gld ");
        query.append(" WHERE vh.id = gl.voucherHeaderId AND vh.id  =mis.voucherheaderid AND gl.glCode=coa.glcode AND coa.id = coadtl.glcodeid AND (coa.purposeid   IS NULL OR coa.purposeid NOT IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) ) ");
        query.append(" AND gl.id = gld.generalLedgerId AND gld.detailtypeid = coadtl.detailtypeid AND vh.voucherDate  >=to_date('");
        query.append(fyStartingDate);
        query.append("','dd/mm/yyyy') AND vh.voucherDate  <=to_date('");
        query.append(fyEndingDate);
        query.append("','dd/mm/yyyy') AND coa.type IN('A','L') AND vh.status NOT IN(4,5) ");
        query.append(" GROUP BY gl.glcodeId,gld.detailTypeId,gld.detailKeyId,vh.fundId,mis.departmentid,gl.functionid ");

        query.append(" UNION ALL ");

        // Opening Balance entries for Control codes(2nd Query)

        query.append(" SELECT ts.glcodeid AS glCodeId,ts.fundid AS fundId,ts.departmentid AS deptId,ts.functionid AS functionId,ts.accountdetailtypeid AS detailTypeId ,ts.accountdetailkey AS detailKeyId ,SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0 ELSE ts.openingdebitbalance END) AS dr, ");
        query.append(" SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END) AS cr,(SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0 ELSE ts.openingdebitbalance END) - SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END)) AS balance ");
        query.append(" FROM transactionsummary ts,chartofaccounts coa,chartofaccountdetail coadtl WHERE coa.id = coadtl.glcodeid AND ts.accountdetailtypeid =coadtl.detailtypeid AND coa.id = ts.glcodeid AND (coa.purposeid IS NULL OR coa.purposeid NOT IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) ) ");
        query.append(" AND coa.type IN('A','L') AND ts.financialyearid = ");
        query.append(financialYear);
        query.append(" ");
        query.append(" GROUP BY ts.glcodeid,ts.accountdetailtypeid ,ts.accountdetailkey,ts.fundid ,ts.departmentid ,ts.functionid ");

        query.append(" ) closingbalance ");
        query.append(" GROUP BY glcodeId ,detailTypeId,detailKeyId,fundId ,deptId ,functionid ");
        query.append("ORDER BY glcodeId ,detailTypeId,detailKeyId,fundId ,deptId ,functionid ");
        query.append(" ) final");
        return query.toString();

    }

    /**
     * This function is called to calculate the closing balance for GlCodes of type I,E and ExcessIE Code
     * 
     * Transaction entries for Income codes(1st Query) (X)
     * 
     * UNION
     * 
     * Transaction entries for Expense codes(2nd Query) (Y)
     * 
     */

    /**
     * (X-Y)
     * 
     * UNION
     * 
     * Transaction entries for ExcessIE Code(3rd Query)
     * 
     * UNION
     * 
     * Opening Balance entries for ExcessIE Code(4th Query)
     * 
     */
    private String getQueryForIncomeOverExpense(Long financialYear, String fyStartingDate, String fyEndingDate,
            CFinancialYear nextFinancialYear) {
        StringBuilder query = new StringBuilder();
        query.append(" INSERT INTO TransactionSummary (id, financialYearId, lastmodifiedby, glcodeid,fundId,departmentid,functionid ,openingdebitbalance, openingcreditbalance, accountdetailtypeid, accountdetailkey,lastmodifieddate)");
        query.append(" SELECT nextval('seq_transactionsummary'), ");
        query.append(nextFinancialYear.getId());
        query.append(" , ");
        query.append(ApplicationThreadLocals.getUserId());
        query.append(" ,(select id from chartofaccounts where purposeid in (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' )), fundid AS fundId,deptId  AS deptId ,functionid  AS functionId,CASE WHEN balance < 0 THEN abs(balance) ELSE 0 END AS openingbalancedebitamount,CASE WHEN balance > 0 THEN abs(balance) ELSE 0 END AS openingbalancecreditamount,null,null,current_date ");
        query.append(" FROM ( ");
        query.append(" SELECT fundid AS fundId,deptId  AS deptId , functionid   AS functionId, SUM(balance) AS balance ");
        query.append(" FROM ( ");

        // (X-Y)

        query.append(" SELECT fundid AS fundId, deptId AS deptId ,functionid AS functionId,SUM(Income)-SUM(Expense) AS balance ");
        query.append(" FROM ( ");

        // Transaction entries for Income codes(1st Query) (X)

        query.append(" SELECT vh.fundid AS fundId,vmis.departmentid AS deptId ,gl.functionid AS functionId,CASE WHEN SUM(gl.creditAmount)-SUM(gl.debitamount) IS NULL THEN 0 ELSE SUM(gl.creditAmount)-SUM(gl.debitamount) END AS Income, 0   AS Expense ");
        query.append(" FROM chartofaccounts coa, generalledger gl,voucherHeader vh,vouchermis vmis WHERE vh.ID = gl.VOUCHERHEADERID  AND gl.glcode =coa.glcode AND vmis.voucherheaderid=vh.id AND vh.VOUCHERDATE >= to_date('");
        query.append(fyStartingDate);
        query.append("','dd/mm/yyyy') AND vh.VOUCHERDATE <= to_date('");
        query.append(fyEndingDate);
        query.append("','dd/mm/yyyy') AND vh.status NOT IN(4,5)");
        query.append(" AND coa.TYPE = 'I' ");
        query.append(" GROUP BY vh.fundId,vmis.departmentid,gl.functionid ");

        query.append(" UNION ALL ");

        // Transaction entries for Expense codes(2nd Query) (Y)

        query.append(" SELECT vh.fundid    AS fundId,vmis.departmentid AS deptId ,gl.functionid AS functionId, 0 AS Income,CASE WHEN SUM(gl.debitamount)-SUM(gl.creditAmount) IS NULL THEN 0 ELSE SUM(gl.debitamount)-SUM(gl.creditAmount) END AS Expense ");
        query.append(" FROM chartofaccounts coa,generalledger gl,voucherHeader vh,vouchermis vmis WHERE vh.ID = gl.VOUCHERHEADERID AND gl.glcode =coa.glcode AND vmis.voucherheaderid=vh.id AND vh.VOUCHERDATE  >= to_date('");
        query.append(fyStartingDate);
        query.append("','dd/mm/yyyy') AND vh.VOUCHERDATE <= to_date('");
        query.append(fyEndingDate);
        query.append("','dd/mm/yyyy') AND vh.status NOT IN(4,5) AND coa.TYPE = 'E' ");
        query.append(" GROUP BY vh.fundId,vmis.departmentid,gl.functionid ) IncomeAndExpense GROUP BY fundId,deptId,functionId ");

        query.append(" UNION ALL ");

        // Transaction entries for ExcessIE Code(3rd Query)

        query.append(" SELECT fundid  AS fundId,deptId AS deptId ,functionid  AS functionId, SUM(balance) AS balance ");
        query.append(" FROM ( ");
        query.append(" SELECT vh.fundid   AS fundId,vmis.departmentid AS deptId ,gl.functionid AS functionId,CASE WHEN SUM(gl.creditAmount)-SUM(gl.debitamount) IS NULL THEN 0 ELSE SUM(gl.creditAmount)-SUM(gl.debitamount) END AS balance ");
        query.append(" FROM chartofaccounts coa,generalledger gl,voucherHeader vh,vouchermis vmis ");
        query.append(" WHERE vh.ID = gl.VOUCHERHEADERID AND gl.glcode = coa.glcode AND coa.purposeid IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) AND vmis.voucherheaderid=vh.id AND vh.VOUCHERDATE >= to_date('");
        query.append(fyStartingDate);
        query.append("','dd/mm/yyyy') AND vh.VOUCHERDATE <= to_date('");
        query.append(fyEndingDate);
        query.append("','dd/mm/yyyy') ");
        query.append(" AND vh.status NOT IN(4,5) ");
        query.append(" GROUP BY vh.fundId,vmis.departmentid,gl.functionid ");

        query.append(" UNION ALL ");

        // Opening Balance entries for ExcessIE Code(4th Query)

        query.append(" SELECT ts.fundid AS fundId,ts.departmentid  AS deptId,ts.functionid AS functionId,SUM( ts.openingcreditbalance ) - SUM( ts.openingdebitbalance ) AS balance ");
        query.append(" FROM transactionsummary ts,chartofaccounts coa ");
        query.append(" WHERE coa.id  = ts.glcodeid AND coa.purposeid IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) AND ts.financialyearid = ");
        query.append(financialYear);
        query.append(" ");
        query.append(" GROUP BY ts.fundid ,ts.departmentid ,ts.functionid ");

        query.append(" ) ExcessIECode ");
        query.append(" GROUP BY fundid , deptId ,functionid ");
        query.append(" ) IncomeOverExpense ");

        query.append(" GROUP BY fundid ,deptId ,functionid ");
        query.append(" ) final");
        return query.toString();

    }

}
