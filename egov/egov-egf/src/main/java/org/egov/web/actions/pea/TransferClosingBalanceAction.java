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
package org.egov.web.actions.pea;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.utils.Constants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@ParentPackage("egov")
@Results({
        @Result(name = TransferClosingBalanceAction.NEW, location = "transferClosingBalance-new.jsp")
})
public class TransferClosingBalanceAction extends BaseFormAction {

    private static final long serialVersionUID = 7217194113772563333L;
    private static final SimpleDateFormat FORMATDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);

    @Autowired
    @Qualifier("financialYearDAO")
    private FinancialYearHibernateDAO financialYearDAO;

    private Long financialYear;

    @Override
    public StateAware getModel() {
        return null;

    }

    private CFinancialYear fy;
    private CFinancialYear previousFinancialYear;
    private CFinancialYear nextFinancialYear;
    private String fyStartingDate;
    private String fyEndingDate;

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("financialYearList", financialYearDAO.getAllNotClosedFinancialYears());
    }

    public void prepareNewform() {

    }

    @SkipValidation
    @Action(value = "/pea/transferClosingBalance-new")
    public String newform() {
        return NEW;
    }

    @SkipValidation
    @ValidationErrorPage(value = NEW)
    @Action(value = "/pea/transferClosingBalance-transfer")
    public String transfer() {
        try {
            Query query = null;
            fy = financialYearDAO.getFinancialYearById(financialYear);
            try {
                previousFinancialYear = financialYearDAO.getPreviousFinancialYearByDate(fy.getStartingDate());
            } catch (final Exception e) {
                // Ignore
            }
            try {
                nextFinancialYear = financialYearDAO.getNextFinancialYearByDate(fy.getStartingDate());
            } catch (final Exception e) {
                throw new ValidationException("Next Financial Year is not exist in system or not active",
                        "Next Financial Year is not exist in system or not active");
            }
            if (!validatePreviousFinancialYear())
                throw new ValidationException("Previos Financial Year is Open, it can not be closed",
                        "Previos Financial Year is Open, it can not be closed");
            deleteNextFYTransactionSummary();
            if (nextFinancialYear == null || !nextFinancialYear.getIsActive())
                throw new ValidationException("Next Financial Year is not exist in system or not active",
                        "Next Financial Year is not exist in system or not active");
            fyStartingDate = FORMATDDMMYYYY.format(fy.getStartingDate());
            fyEndingDate = FORMATDDMMYYYY.format(fy.getEndingDate());

            query = persistenceService.getSession().createSQLQuery(getQueryForNonControlCodesAndMisMatchsInControlCodes());
            query.executeUpdate();

            query = null;
            query = persistenceService.getSession().createSQLQuery(getQueryForControlCodes());
            query.executeUpdate();

            query = null;
            query = persistenceService.getSession().createSQLQuery(getQueryForIncomeOverExpense());
            query.executeUpdate();

            updateCurrentYearTransferClosingBalance();
            addActionMessage("Transfer Closing Balance Successful");
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
        return NEW;
    }

    private void deleteNextFYTransactionSummary() {
        Query query = null;
        query = persistenceService.getSession().createSQLQuery(
                "delete from TransactionSummary where financialyearid = "
                        + nextFinancialYear.getId() + "");
        query.executeUpdate();
    }

    private void updateCurrentYearTransferClosingBalance() {
        fy.setTransferClosingBalance(true);
        financialYearDAO.update(fy);
    }

    private boolean validatePreviousFinancialYear() {

        return previousFinancialYear != null ? previousFinancialYear.getIsClosed() : true;

    }

    private String getQueryForNonControlCodesAndMisMatchsInControlCodes() {
        StringBuilder query = new StringBuilder();
        query.append(" INSERT INTO TransactionSummary (id, financialYearId, lastmodifiedby, glcodeid,fundId,departmentid,functionid ,openingdebitbalance, openingcreditbalance, accountdetailtypeid, accountdetailkey,lastmodifieddate)");
        query.append(" SELECT nextval('seq_transactionsummary'), ");
        query.append(nextFinancialYear.getId());
        query.append(" , ");
        query.append(EgovThreadLocals.getUserId());
        query.append(" ,glcodeId AS glCodeId, fundid AS fundId,deptId AS deptId ,functionid AS functionId,CASE WHEN balance > 0 THEN abs(balance) ELSE 0 END AS openingbalancedebitamount,CASE WHEN balance < 0 THEN abs(balance) ELSE 0 END AS openingbalancecreditamount,null,null,current_date ");
        query.append(" FROM ( ");
        query.append(" SELECT glcodeId AS glCodeId,fundId AS fundId,deptId AS deptId,functionid AS functionId,SUM(dr) AS dr,SUM(cr) AS cr,SUM(balance) AS balance ");
        query.append(" FROM ( ");
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
        query.append(" SELECT ts.glcodeid AS glCodeId,ts.fundid AS fundId,ts.departmentid  AS deptId,ts.functionid AS functionId,SUM(CASE WHEN ts.openingdebitbalance = 0 THEN 0 ELSE ts.openingdebitbalance END) AS dr, ");
        query.append(" SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END) AS cr,(SUM( CASE WHEN ts.openingdebitbalance = 0 THEN 0 ELSE ts.openingdebitbalance END) - SUM(CASE WHEN ts.openingcreditbalance = 0 THEN 0 ELSE ts.openingcreditbalance END)) AS balance ");
        query.append(" FROM transactionsummary ts,chartofaccounts coa ");
        query.append(" WHERE  ts.ACCOUNTDETAILKEY  IS NULL AND ts.ACCOUNTDETAILTYPEID IS NULL AND coa.id = ts.glcodeid AND (coa.purposeid IS NULL OR coa.purposeid NOT IN (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE') ) ");
        query.append(" AND coa.type IN('A','L') AND ts.financialyearid = ");
        query.append(financialYear);
        query.append(" ");
        query.append(" GROUP BY ts.glcodeid,ts.fundid ,ts.departmentid ,ts.functionid ");
        query.append(" UNION ALL ");
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

    private String getQueryForControlCodes() {
        StringBuilder query = new StringBuilder();
        query.append(" INSERT INTO TransactionSummary (id, financialYearId, lastmodifiedby, glcodeid,fundId,departmentid,functionid , accountdetailtypeid, accountdetailkey,openingdebitbalance, openingcreditbalance,lastmodifieddate)");
        query.append(" SELECT nextval('seq_transactionsummary'), ");
        query.append(nextFinancialYear.getId());
        query.append(" , ");
        query.append(EgovThreadLocals.getUserId());
        query.append(" ,glcodeId AS glCodeId, fundid AS fundId,deptId AS deptId ,functionid AS functionId, detailTypeId  AS detailTypeId,detailKeyId AS detailKeyId, CASE WHEN balance > 0 THEN abs(balance) ELSE 0 END AS openingbalancedebitamount, CASE WHEN balance < 0 THEN abs(balance) ELSE 0 END AS openingbalancecreditamount,current_date ");
        query.append(" FROM ( ");
        query.append(" SELECT glcodeId AS glCodeId,fundId AS fundId, deptId AS deptId,functionid AS functionId,detailTypeId  AS detailTypeId,detailKeyId AS detailKeyId,SUM(dr) AS dr,SUM(cr) AS cr,SUM(balance)   AS balance ");
        query.append(" FROM (");
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

    private String getQueryForIncomeOverExpense() {
        StringBuilder query = new StringBuilder();
        query.append(" INSERT INTO TransactionSummary (id, financialYearId, lastmodifiedby, glcodeid,fundId,departmentid,functionid ,openingdebitbalance, openingcreditbalance, accountdetailtypeid, accountdetailkey,lastmodifieddate)");
        query.append(" SELECT nextval('seq_transactionsummary'), ");
        query.append(nextFinancialYear.getId());
        query.append(" , ");
        query.append(EgovThreadLocals.getUserId());
        query.append(" ,(select id from chartofaccounts where purposeid in (SELECT id FROM egf_accountcode_purpose WHERE name = 'ExcessIE' )), fundid AS fundId,deptId  AS deptId ,functionid  AS functionId,CASE WHEN balance < 0 THEN abs(balance) ELSE 0 END AS openingbalancedebitamount,CASE WHEN balance > 0 THEN abs(balance) ELSE 0 END AS openingbalancecreditamount,null,null,current_date ");
        query.append(" FROM ( ");
        query.append(" SELECT fundid AS fundId,deptId  AS deptId , functionid   AS functionId, SUM(balance) AS balance ");
        query.append(" FROM ( ");
        query.append(" SELECT fundid AS fundId, deptId AS deptId ,functionid AS functionId,SUM(Income)-SUM(Expense) AS balance ");
        query.append(" FROM ( ");
        query.append(" SELECT vh.fundid AS fundId,vmis.departmentid AS deptId ,gl.functionid AS functionId,CASE WHEN SUM(gl.creditAmount)-SUM(gl.debitamount) IS NULL THEN 0 ELSE SUM(gl.creditAmount)-SUM(gl.debitamount) END AS Income, 0   AS Expense ");
        query.append(" FROM chartofaccounts coa, generalledger gl,voucherHeader vh,vouchermis vmis WHERE vh.ID = gl.VOUCHERHEADERID  AND gl.glcode =coa.glcode AND vmis.voucherheaderid=vh.id AND vh.VOUCHERDATE >= to_date('");
        query.append(fyStartingDate);
        query.append("','dd/mm/yyyy') AND vh.VOUCHERDATE <= to_date('");
        query.append(fyEndingDate);
        query.append("','dd/mm/yyyy') AND vh.status NOT IN(4,5)");
        query.append(" AND coa.TYPE = 'I' ");
        query.append(" GROUP BY vh.fundId,vmis.departmentid,gl.functionid ");
        query.append(" UNION ALL ");
        query.append(" SELECT vh.fundid    AS fundId,vmis.departmentid AS deptId ,gl.functionid AS functionId, 0 AS Income,CASE WHEN SUM(gl.debitamount)-SUM(gl.creditAmount) IS NULL THEN 0 ELSE SUM(gl.debitamount)-SUM(gl.creditAmount) END AS Expense ");
        query.append(" FROM chartofaccounts coa,generalledger gl,voucherHeader vh,vouchermis vmis WHERE vh.ID = gl.VOUCHERHEADERID AND gl.glcode =coa.glcode AND vmis.voucherheaderid=vh.id AND vh.VOUCHERDATE  >= to_date('");
        query.append(fyStartingDate);
        query.append("','dd/mm/yyyy') AND vh.VOUCHERDATE <= to_date('");
        query.append(fyEndingDate);
        query.append("','dd/mm/yyyy') AND vh.status NOT IN(4,5) AND coa.TYPE = 'E' ");
        query.append(" GROUP BY vh.fundId,vmis.departmentid,gl.functionid ) IncomeAndExpense GROUP BY fundId,deptId,functionId ");
        query.append(" UNION ALL ");
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

    public Long getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(Long financialYear) {
        this.financialYear = financialYear;
    }

    public CFinancialYear getFy() {
        return fy;
    }

    public void setFy(CFinancialYear fy) {
        this.fy = fy;
    }

    public CFinancialYear getPreviousFinancialYear() {
        return previousFinancialYear;
    }

    public void setPreviousFinancialYear(CFinancialYear previousFinancialYear) {
        this.previousFinancialYear = previousFinancialYear;
    }

    public CFinancialYear getNextFinancialYear() {
        return nextFinancialYear;
    }

    public void setNextFinancialYear(CFinancialYear nextFinancialYear) {
        this.nextFinancialYear = nextFinancialYear;
    }

    public String getFyStartingDate() {
        return fyStartingDate;
    }

    public void setFyStartingDate(String fyStartingDate) {
        this.fyStartingDate = fyStartingDate;
    }

    public String getFyEndingDate() {
        return fyEndingDate;
    }

    public void setFyEndingDate(String fyEndingDate) {
        this.fyEndingDate = fyEndingDate;
    }

}