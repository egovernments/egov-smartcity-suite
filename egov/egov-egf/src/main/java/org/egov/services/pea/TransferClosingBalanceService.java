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
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
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

        deleteNextFYTransactionSummary(nextFinancialYear);

        String fyStartingDate = FORMATDDMMYYYY.format(fy.getStartingDate());
        String fyEndingDate = FORMATDDMMYYYY.format(fy.getEndingDate());

        /*
         * Processing all the COA which are non- subledger codes. Also we will process for all COAs that are control codes which
         * are having data with accountdetail type which is not same as that of what is mentioned in the database. The result data
         * is been inserted into the opening balance Note- COA code for Excess IE is been excluded for processing as this will be
         * taken care separately.
         */

        updateNonControlCodesAndMisMatchsInControlCodes(financialYear, fyStartingDate, fyEndingDate,
                nextFinancialYear);

        /*
         * Processing all control codes both transaction and opening balance and the net balance is been inserted as the opening
         * balance.
         */
        updateControlCodes(financialYear, fyStartingDate, fyEndingDate, nextFinancialYear);

        /*
         * COA for Excess IE transaction balance + Opening balance will be calculated along with the Income - expenses for that
         * year.
         */
        updateIncomeOverExpense(financialYear, fyStartingDate, fyEndingDate, nextFinancialYear);

        updateCurrentYearTransferClosingBalance(fy);
    }

    @Transactional
    public void deleteNextFYTransactionSummary(CFinancialYear nextFinancialYear) {
        getSession().createNativeQuery(
                "delete from TransactionSummary where financialyearid = :financialyearid")
                .setParameter("financialyearid", nextFinancialYear.getId())
                .executeUpdate();
    }

    @Transactional
    public void updateCurrentYearTransferClosingBalance(CFinancialYear fy) {
        fy.setTransferClosingBalance(true);
        financialYearDAO.update(fy);
    }

    /**
     * This function is called to calculate the closing balance for GlCodes of type A,L (Excluding ExcessIE code)
     * <p>
     * Transaction entries for Non-Control codes(1st Query)
     * <p>
     * UNION
     * <p>
     * Opening Balance entries for Non-Control codes(2nd Query)
     * <p>
     * UNION
     * <p>
     * Mismatch Transaction entries for Control codes(3rd Query)
     * <p>
     * UNION
     * <p>
     * Mismatch Opening Balance entries for Control codes(4th Query)
     */

    private void updateNonControlCodesAndMisMatchsInControlCodes(Long financialYear, String fyStartingDate,
                                                                 String fyEndingDate, CFinancialYear nextFinancialYear) {
        StringBuilder query = new StringBuilder();
        query.append(" INSERT INTO TransactionSummary (id, financialYearId, lastmodifiedby, glcodeid,fundId,departmentid,functionid ,openingdebitbalance, openingcreditbalance,")
                .append(" accountdetailtypeid, accountdetailkey,lastmodifieddate)")
                .append(" SELECT nextval('seq_transactionsummary'), ")
                .append(nextFinancialYear.getId())
                .append(" , ")
                .append(ApplicationThreadLocals.getUserId())
                .append(" ,glcodeId AS glCodeId, fundid AS fundId,deptId AS deptId ,functionid AS functionId,CASE WHEN balance > 0 THEN abs(balance)")
                .append(" ELSE 0 END AS openingbalancedebitamount,CASE WHEN balance < 0 THEN abs(balance) ELSE 0 END AS openingbalancecreditamount,null,null,current_date ")
                .append(" FROM ( ")
                .append(" SELECT glcodeId AS glCodeId,fundId AS fundId,deptId AS deptId,functionid AS functionId,SUM(dr) AS dr,SUM(cr) AS cr,SUM(balance) AS balance ")
                .append(" FROM ( ")

                // Transaction entries for Non-Control codes(1st Query)

                .append(" SELECT gl.glcodeId AS glCodeId,vh.fundId AS fundId,mis.departmentid  AS deptId, gl.functionid AS functionId,")
                .append("SUM(CASE WHEN debitamount = 0 THEN 0 ELSE debitamount END) AS dr, ")
                .append(" SUM(CASE WHEN creditAmount = 0 THEN 0 ELSE creditAmount END) AS cr,(SUM(CASE WHEN debitamount = 0 THEN 0")
                .append(" ELSE debitamount END) - SUM(CASE WHEN creditAmount = 0 THEN 0 ELSE creditAmount END)) AS balance ")
                .append(" FROM voucherHeader vh,vouchermis mis,chartOfAccounts coa,generalledger gl LEFT JOIN generalledgerdetail gld ON gl.id = gld.generalledgerid ")
                .append(" WHERE gld.id IS NULL AND vh.id = gl.voucherHeaderId AND gl.glCode=coa.glcode ")
                .append(" AND (coa.purposeid IS NULL OR coa.purposeid NOT IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) )")
                .append(" AND vh.id = mis.voucherheaderid AND vh.voucherDate >=to_date(:fyStartingDate,'dd/mm/yyyy') ")
                .append(" AND vh.voucherDate <=to_date(:fyEndingDate,'dd/mm/yyyy') AND vh.status NOT  IN(4,5) AND coa.type IN('A','L') ")
                .append(" GROUP BY gl.glcodeId,vh.fundId,mis.departmentid,gl.functionid ")

                .append(" UNION ALL ")

                // Opening Balance entries for Non-Control codes(2nd Query)

                .append(" SELECT ts.glcodeid AS glCodeId,ts.fundid AS fundId,ts.departmentid  AS deptId,ts.functionid AS functionId,")
                .append("SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0 ELSE ts.openingdebitbalance END) AS dr, ")
                .append(" SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END) AS cr,(SUM( CASE WHEN ts.openingdebitbalance = 0 THEN 0")
                .append(" ELSE ts.openingdebitbalance END) - SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END)) AS balance ")
                .append(" FROM transactionsummary ts,chartofaccounts coa ")
                .append(" WHERE  ts.ACCOUNTDETAILKEY  IS NULL AND ts.ACCOUNTDETAILTYPEID IS NULL AND coa.id = ts.glcodeid AND (coa.purposeid IS NULL OR coa.purposeid NOT IN")
                .append(" (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE') ) ")
                .append(" AND coa.type IN('A','L') AND ts.financialyearid = :financialYear")
                .append(" GROUP BY ts.glcodeid,ts.fundid ,ts.departmentid ,ts.functionid ")

                .append(" UNION ALL ")

                // Mismatch Transaction entries for Control codes(3rd Query)

                .append(" SELECT gl.glcodeId AS glCodeId,vh.fundId AS fundId,mis.departmentid AS deptId,gl.functionid AS functionId,SUM(CASE WHEN gl.debitamount = 0 THEN 0")
                .append(" ELSE gld.amount END) AS dr, SUM(CASE WHEN gl.creditamount = 0 THEN 0 ELSE gld.amount END) AS cr, ")
                .append(" SUM(CASE WHEN gl.debitamount = 0 THEN 0 ELSE gld.amount END)-SUM(CASE WHEN gl.creditamount = 0 THEN 0 ELSE gld.amount END) AS balance ")
                .append(" FROM voucherHeader vh, vouchermis mis, chartOfAccounts coa,generalledger gl,generalLedgerDetail gld")
                .append(" WHERE  vh.id= gl.voucherHeaderId  AND vh.id =mis.voucherheaderid AND gl.glCode =coa.glcode AND (coa.purposeid IS NULL OR coa.purposeid NOT IN")
                .append(" (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) ) ")
                .append(" AND gl.id  = gld.generalLedgerId AND gld.detailtypeid NOT IN (SELECT coadtl.detailtypeid FROM chartofaccountdetail coadtl WHERE coadtl.glcodeid = coa.id )")
                .append(" AND vh.voucherDate >=to_date(:fyStartingDate,'dd/mm/yyyy') AND vh.voucherDate <=to_date(:fyEndingDate,'dd/mm/yyyy') AND coa.type IN('A','L')")
                .append(" AND vh.status NOT  IN(4,5) ")
                .append(" GROUP BY gl.glcodeId,vh.fundId,mis.departmentid,gl.functionid ")

                .append(" UNION ALL ")

                // Mismatch Opening Balance entries for Control codes(4th Query)
                .append(" SELECT ts.glcodeid AS glCodeId,ts.fundid AS fundId,ts.departmentid  AS deptId,ts.functionid AS functionId,SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0")
                .append(" ELSE ts.openingdebitbalance END) AS dr, ")
                .append(" SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance  END) AS cr,(SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0")
                .append(" ELSE ts.openingdebitbalance END) - SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END)) AS balance ")
                .append(" FROM transactionsummary ts,chartofaccounts coa ")
                .append(" WHERE (ts.accountdetailtypeid is not null and ts.accountdetailtypeid NOT IN (SELECT coadtl.detailtypeid")
                .append(" FROM chartofaccountdetail coadtl WHERE coadtl.glcodeid = coa.id )) AND (coa.purposeid   IS NULL OR coa.purposeid NOT IN")
                .append(" (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) ) ")
                .append(" AND coa.id = ts.glcodeid AND coa.type IN('A','L') AND ts.financialyearid = :financialYear")
                .append(" GROUP BY ts.glcodeid,ts.fundid ,ts.departmentid ,ts.functionid")

                .append(") closingbalance")
                .append(" GROUP BY glcodeId ,fundId ,deptId ,functionid ")
                .append(" ORDER BY glcodeId ,fundId ,deptId ,functionid ) final");

        getSession().createNativeQuery(query.toString())
                .setParameter("fyStartingDate", fyStartingDate, StringType.INSTANCE)
                .setParameter("fyEndingDate", fyEndingDate, StringType.INSTANCE)
                .setParameter("financialYear", financialYear, LongType.INSTANCE)
                .executeUpdate();
    }

    /**
     * This function is called to calculate the closing balance for GlCodes of type A,L (Excluding ExcessIE code)
     * <p>
     * Transaction entries for Control codes(1st Query)
     * <p>
     * UNION
     * <p>
     * Opening Balance entries for Control codes(2nd Query)
     */
    private void updateControlCodes(Long financialYear, String fyStartingDate, String fyEndingDate,
                                    CFinancialYear nextFinancialYear) {
        StringBuilder query = new StringBuilder();
        query.append(" INSERT INTO TransactionSummary (id, financialYearId, lastmodifiedby, glcodeid,fundId,departmentid,functionid , accountdetailtypeid, accountdetailkey,")
                .append("openingdebitbalance, openingcreditbalance,lastmodifieddate)")
                .append(" SELECT nextval('seq_transactionsummary'), ")
                .append(nextFinancialYear.getId())
                .append(" , ")
                .append(ApplicationThreadLocals.getUserId())
                .append(" ,glcodeId AS glCodeId, fundid AS fundId,deptId AS deptId ,functionid AS functionId, detailTypeId  AS detailTypeId,detailKeyId AS detailKeyId,")
                .append(" CASE WHEN balance > 0 THEN abs(balance) ELSE 0 END AS openingbalancedebitamount, CASE WHEN balance < 0 THEN abs(balance) ELSE 0")
                .append(" END AS openingbalancecreditamount,current_date ")
                .append(" FROM ( ")
                .append(" SELECT glcodeId AS glCodeId,fundId AS fundId, deptId AS deptId,functionid AS functionId,detailTypeId  AS detailTypeId,detailKeyId AS detailKeyId,")
                .append("SUM(dr) AS dr,SUM(cr) AS cr,SUM(balance)   AS balance ")
                .append(" FROM (")

                // Transaction entries for Control codes(1st Query)

                .append(" SELECT gl.glcodeId AS glCodeId,vh.fundId AS fundId,mis.departmentid  AS deptId,gl.functionid AS functionId,gld.detailTypeId  AS detailTypeId,")
                .append("gld.detailKeyId AS detailKeyId,SUM(CASE WHEN gl.debitamount = 0 THEN 0 ELSE gld.amount END) AS dr, ")
                .append(" SUM(CASE WHEN gl.creditamount = 0 THEN 0 ELSE gld.amount END) AS cr,SUM(CASE WHEN gl.debitamount = 0 THEN 0 ELSE gld.amount END)-SUM(CASE WHEN gl.creditamount = 0")
                .append("   THEN 0 ELSE gld.amount END) AS balance ")
                .append(" FROM voucherHeader vh,vouchermis mis,chartOfAccounts coa,chartofaccountdetail coadtl,generalledger gl,generalLedgerDetail gld ")
                .append(" WHERE vh.id = gl.voucherHeaderId AND vh.id  =mis.voucherheaderid AND gl.glCode=coa.glcode AND coa.id = coadtl.glcodeid")
                .append(" AND (coa.purposeid   IS NULL OR coa.purposeid NOT IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) ) ")
                .append(" AND gl.id = gld.generalLedgerId AND gld.detailtypeid = coadtl.detailtypeid AND vh.voucherDate  >=to_date(:fyStartingDate,'dd/mm/yyyy')")
                .append(" AND vh.voucherDate  <=to_date(:fyEndingDate,'dd/mm/yyyy') AND coa.type IN('A','L') AND vh.status NOT IN(4,5) ")
                .append(" GROUP BY gl.glcodeId,gld.detailTypeId,gld.detailKeyId,vh.fundId,mis.departmentid,gl.functionid ")

                .append(" UNION ALL ")

                // Opening Balance entries for Control codes(2nd Query)

                .append(" SELECT ts.glcodeid AS glCodeId,ts.fundid AS fundId,ts.departmentid AS deptId,ts.functionid AS functionId,ts.accountdetailtypeid AS detailTypeId ,")
                .append("ts.accountdetailkey AS detailKeyId ,SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0 ELSE ts.openingdebitbalance END) AS dr, ")
                .append(" SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END) AS cr,(SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0")
                .append(" ELSE ts.openingdebitbalance END) - SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END)) AS balance ")
                .append(" FROM transactionsummary ts,chartofaccounts coa,chartofaccountdetail coadtl WHERE coa.id = coadtl.glcodeid AND ts.accountdetailtypeid =coadtl.detailtypeid")
                .append(" AND coa.id = ts.glcodeid AND (coa.purposeid IS NULL OR coa.purposeid NOT IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) ) ")
                .append(" AND coa.type IN('A','L') AND ts.financialyearid = :financialYear")
                .append(" GROUP BY ts.glcodeid,ts.accountdetailtypeid ,ts.accountdetailkey,ts.fundid ,ts.departmentid ,ts.functionid ")

                .append(" ) closingbalance ")
                .append(" GROUP BY glcodeId ,detailTypeId,detailKeyId,fundId ,deptId ,functionid ")
                .append("ORDER BY glcodeId ,detailTypeId,detailKeyId,fundId ,deptId ,functionid ")
                .append(" ) final");
        getSession().createNativeQuery(query.toString())
                .setParameter("fyStartingDate", fyStartingDate, StringType.INSTANCE)
                .setParameter("fyEndingDate", fyEndingDate, StringType.INSTANCE)
                .setParameter("financialYear", financialYear, LongType.INSTANCE)
                .executeUpdate();

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
     * <p>
     * UNION
     * <p>
     * Transaction entries for ExcessIE Code(3rd Query)
     * <p>
     * UNION
     * <p>
     * Opening Balance entries for ExcessIE Code(4th Query)
     */
    private void updateIncomeOverExpense(Long financialYear, String fyStartingDate, String fyEndingDate,
                                         CFinancialYear nextFinancialYear) {
        StringBuilder query = new StringBuilder();
        query.append(" INSERT INTO TransactionSummary (id, financialYearId, lastmodifiedby, glcodeid,fundId,departmentid,functionid ,openingdebitbalance, openingcreditbalance,")
                .append(" accountdetailtypeid, accountdetailkey,lastmodifieddate)")
                .append(" SELECT nextval('seq_transactionsummary'), ")
                .append(nextFinancialYear.getId())
                .append(" , ")
                .append(ApplicationThreadLocals.getUserId())
                .append(" ,(select id from chartofaccounts where purposeid in (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' )), fundid AS fundId,deptId  AS deptId ,")
                .append("functionid  AS functionId,CASE WHEN balance < 0 THEN abs(balance) ELSE 0 END AS openingbalancedebitamount,CASE WHEN balance > 0 THEN abs(balance) ELSE 0")
                .append(" END AS openingbalancecreditamount,null,null,current_date ")
                .append(" FROM ( ")
                .append(" SELECT fundid AS fundId,deptId  AS deptId , functionid   AS functionId, SUM(balance) AS balance ")
                .append(" FROM ( ")

                // (X-Y)

                .append(" SELECT fundid AS fundId, deptId AS deptId ,functionid AS functionId,SUM(Income)-SUM(Expense) AS balance ")
                .append(" FROM ( ")

                // Transaction entries for Income codes(1st Query) (X)

                .append(" SELECT vh.fundid AS fundId,vmis.departmentid AS deptId ,gl.functionid AS functionId,CASE WHEN SUM(gl.creditAmount)-SUM(gl.debitamount) IS NULL THEN 0")
                .append(" ELSE SUM(gl.creditAmount)-SUM(gl.debitamount) END AS Income, 0   AS Expense ")
                .append(" FROM chartofaccounts coa, generalledger gl,voucherHeader vh,vouchermis vmis WHERE vh.ID = gl.VOUCHERHEADERID  AND gl.glcode =coa.glcode")
                .append(" AND vmis.voucherheaderid=vh.id AND vh.VOUCHERDATE >= to_date(:fyStartingDate,'dd/mm/yyyy') AND vh.VOUCHERDATE <= to_date(:fyEndingDate,'dd/mm/yyyy')")
                .append(" AND vh.status NOT IN(4,5)")
                .append(" AND coa.TYPE = 'I' ")
                .append(" GROUP BY vh.fundId,vmis.departmentid,gl.functionid ")

                .append(" UNION ALL ")

                // Transaction entries for Expense codes(2nd Query) (Y)

                .append(" SELECT vh.fundid    AS fundId,vmis.departmentid AS deptId ,gl.functionid AS functionId, 0 AS Income,CASE WHEN SUM(gl.debitamount)-SUM(gl.creditAmount) IS NULL")
                .append(" THEN 0 ELSE SUM(gl.debitamount)-SUM(gl.creditAmount) END AS Expense ")
                .append(" FROM chartofaccounts coa,generalledger gl,voucherHeader vh,vouchermis vmis WHERE vh.ID = gl.VOUCHERHEADERID AND gl.glcode =coa.glcode")
                .append(" AND vmis.voucherheaderid=vh.id AND vh.VOUCHERDATE  >= to_date(:fyStartingDate,'dd/mm/yyyy') AND vh.VOUCHERDATE <= to_date(:fyEndingDate,'dd/mm/yyyy')")
                .append(" AND vh.status NOT IN(4,5) AND coa.TYPE = 'E' ")
                .append(" GROUP BY vh.fundId,vmis.departmentid,gl.functionid ) IncomeAndExpense GROUP BY fundId,deptId,functionId ")

                .append(" UNION ALL ")

                // Transaction entries for ExcessIE Code(3rd Query)

                .append(" SELECT fundid  AS fundId,deptId AS deptId ,functionid  AS functionId, SUM(balance) AS balance ")
                .append(" FROM ( ")
                .append(" SELECT vh.fundid   AS fundId,vmis.departmentid AS deptId ,gl.functionid AS functionId,CASE WHEN SUM(gl.creditAmount)-SUM(gl.debitamount) IS NULL THEN 0")
                .append(" ELSE SUM(gl.creditAmount)-SUM(gl.debitamount) END AS balance ")
                .append(" FROM chartofaccounts coa,generalledger gl,voucherHeader vh,vouchermis vmis ")
                .append(" WHERE vh.ID = gl.VOUCHERHEADERID AND gl.glcode = coa.glcode AND coa.purposeid IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' )")
                .append(" AND vmis.voucherheaderid=vh.id AND vh.VOUCHERDATE >= to_date(:fyStartingDate,'dd/mm/yyyy') AND vh.VOUCHERDATE <= to_date(:fyEndingDate,'dd/mm/yyyy') ")
                .append(" AND vh.status NOT IN(4,5) ")
                .append(" GROUP BY vh.fundId,vmis.departmentid,gl.functionid ")

                .append(" UNION ALL ")

                // Opening Balance entries for ExcessIE Code(4th Query)

                .append(" SELECT ts.fundid AS fundId,ts.departmentid  AS deptId,ts.functionid AS functionId,SUM( ts.openingcreditbalance ) - SUM( ts.openingdebitbalance ) AS balance ")
                .append(" FROM transactionsummary ts,chartofaccounts coa ")
                .append(" WHERE coa.id  = ts.glcodeid AND coa.purposeid IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' ) AND ts.financialyearid = :financialYear ")
                .append(" GROUP BY ts.fundid ,ts.departmentid ,ts.functionid ")

                .append(" ) ExcessIECode ")
                .append(" GROUP BY fundid , deptId ,functionid ")
                .append(" ) IncomeOverExpense ")

                .append(" GROUP BY fundid ,deptId ,functionid ")
                .append(" ) final");

        getSession().createNativeQuery(query.toString())
                .setParameter("fyStartingDate", fyStartingDate, StringType.INSTANCE)
                .setParameter("fyEndingDate", fyEndingDate, StringType.INSTANCE)
                .setParameter("financialYear", financialYear, LongType.INSTANCE)
                .executeUpdate();
    }

}
