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
/**
 *
 */
package org.egov.services.report;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.report.FundFlowBean;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author mani
 *
 */
@SuppressWarnings("unchecked")
public class FundFlowService extends PersistenceService {
    private static Logger LOGGER = Logger.getLogger(FundFlowService.class);
    private static final SimpleDateFormat sqlformat = new SimpleDateFormat("dd-MMM-yyyy");
    private static final String START_FINANCIALYEAR_DATE = "01-Apr-2012";
    private @Autowired AppConfigValueService appConfigValuesService;

    public FundFlowService() {
        super(null);
    }

    public FundFlowService(Class type) {
        super(type);
    }
    /**
     * All amounts is in lakhs
     */

    public List<FundFlowBean> getOutStandingPayments(final Date asPerDate, final Long fundId) {
        final String voucherDate = sqlformat.format(asPerDate);
        final List<AppConfigValues> appConfig = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE");
        if (appConfig == null || appConfig.isEmpty())
            throw new ValidationException("", "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is not defined in AppConfig");

        String appConfigValue = "";
        boolean condtitionalAppConfigIsPresent = false;
        String designationName = null;
        String functionaryName = null;
        String stateWithoutCondition = "";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Before Appconfig Check ------");
        for (final AppConfigValues app : appConfig)
        {
            appConfigValue = app.getValue();
            if (appConfigValue.contains(FinancialConstants.DELIMITER_FOR_VOUCHER_STATUS_TO_CHECK_BANK_BALANCE))
            {
                condtitionalAppConfigIsPresent = true;
                final String[] array = appConfigValue
                        .split(FinancialConstants.DELIMITER_FOR_VOUCHER_STATUS_TO_CHECK_BANK_BALANCE);
                if (array.length != 2)
                    throw new ValidationException("", "VOUCHER_STATUS_TO_CHECK_BANK_BALANCE is invalid");
                // Order assumed is first is designation Name, second functionary Name
                designationName = array[0];
                functionaryName = array[1];
            } else
                stateWithoutCondition = appConfigValue;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("After Appconfig Check ------");
        // get BPVs for the cuurent date which are in the workflow
        StringBuffer outstandingPaymentQryStr = new StringBuffer(500);
        if (condtitionalAppConfigIsPresent)
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("condtitionalAppConfigIsPresent ------");
            outstandingPaymentQryStr =
                    outstandingPaymentQryStr
                    .
                    append("SELECT DISTINCT( ba.accountnumber)      AS accountNumber ,  ROUND(SUM(ph.paymentamount)/100000,2) AS outStandingBPV"
                            +
                            " FROM voucherheader vh,paymentheader ph,bankaccount ba,eg_wf_states state, eg_eis_employeeinfo empinfo, "
                            +
                            " eg_designation desg, functionary func "
                            +
                            " where ph.state_id =state.id and empinfo.pos_id= state.owner and empinfo.functionary_id=func.id  and empinfo.isactive=true "
                            +
                            " and empinfo.DESIGNATIONID=desg.DESIGNATIONID and vh.id =ph.voucherheaderid and  ba.id=ph.bankaccountnumberid"
                            +
                            " and desg.DESIGNATION_NAME like '"
                            + designationName
                            + "' and func.NAME like '"
                            + functionaryName + "' ");
            if (fundId != null && fundId != -1)
            {
                outstandingPaymentQryStr.append(" and vh.fundid =" + fundId);
                outstandingPaymentQryStr.append(" and ba.fundid =" + fundId);
            }
            outstandingPaymentQryStr.append(" and vh.voucherdate <='")
            .append(voucherDate)
            .append("'  and vh.voucherdate >='" + START_FINANCIALYEAR_DATE)
            .append("' group by accountNumber  ");
        }
        else
        {
            outstandingPaymentQryStr =
                    outstandingPaymentQryStr
                    .
                    append("SELECT DISTINCT( ba.accountnumber)      AS accountNumber ,  ROUND(SUM(ph.paymentamount)/100000,2) AS outStandingBPV"
                            +
                            " FROM voucherheader vh,paymentheader ph,bankaccount ba,eg_wf_states state where ph.state_id     =state.id "
                            +
                            "	and vh.id =ph.voucherheaderid and  ba.id=ph.bankaccountnumberid");
            if (fundId != null && fundId != -1)
            {
                outstandingPaymentQryStr.append(" and vh.fundid =" + fundId);
                outstandingPaymentQryStr.append(" and ba.fundid =" + fundId);
            }
            outstandingPaymentQryStr.append(" and vh.voucherdate <='")
            .append(voucherDate)
            .append("'  and vh.voucherdate >='" + START_FINANCIALYEAR_DATE)
            .append("' and state.value like '")
            .append(stateWithoutCondition)
            .append("' group by accountNumber  ");
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Out Standing Payment Query " + outstandingPaymentQryStr.toString());
        final Query outstandingQry = getSession().createSQLQuery(outstandingPaymentQryStr.toString())
                .addScalar("accountNumber")
                .addScalar("outStandingBPV")
                .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
        return outstandingQry.list();
    }

    public List<FundFlowBean> getConcurrancePayments(final Date asPerDate, final Long fundId) {
        final String voucherDate = sqlformat.format(asPerDate);
        final List<AppConfigValues> appConfig = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                "PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK");
        if (appConfig == null || appConfig.isEmpty())
            throw new ValidationException("", "PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK is not defined in AppConfig");
        String voucherStatus = "";
        final StringBuffer values = new StringBuffer(200);
        for (final AppConfigValues app : appConfig)
        {
            values.append("'");
            values.append(app.getValue());
            values.append("',");
        }
        // need to ommit the last comma
        voucherStatus = values.substring(0, values.length() - 1);

        // get BPVs for the cuurent date which are in the workflow
        StringBuffer conCurrancePaymentQryStr = new StringBuffer(500);
        conCurrancePaymentQryStr =
                conCurrancePaymentQryStr
                .
                append("SELECT DISTINCT( ba.accountnumber)      AS accountNumber ,  ROUND(SUM(ph.paymentamount)/100000,2) AS concurranceBPV"
                        +
                        " FROM voucherheader vh,paymentheader ph,bankaccount ba,eg_wf_states state where ph.state_id     =state.id "
                        +
                        "	and vh.id =ph.voucherheaderid and  ba.id=ph.bankaccountnumberid and  vh.voucherdate >='"
                        + START_FINANCIALYEAR_DATE + "'");

        if (fundId != null && fundId != -1)
        {
            conCurrancePaymentQryStr.append(" and vh.fundid =" + fundId);
            conCurrancePaymentQryStr.append(" and ba.fundid =" + fundId);
        }
        conCurrancePaymentQryStr.append(" and to_char(created_date,'dd-Mon-yyyy') ='")
        .append(voucherDate)
        .append("' and ( state.value in (")
        .append(voucherStatus)
        .append(")OR vh.status=0 ) group by accountNumber  ");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Concurrancey payment " + conCurrancePaymentQryStr.toString());
        final Query conCurranceQry = getSession().createSQLQuery(conCurrancePaymentQryStr.toString())
                .addScalar("accountNumber")
                .addScalar("concurranceBPV")
                .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
        return conCurranceQry.list();
    }

    /**
     * All Payment Bank Accounts
     */
    public List<FundFlowBean> getAllpaymentAccounts(final Long fundId) {
        final StringBuffer allPaymentAccounts = new StringBuffer(500);
        allPaymentAccounts
        .append("select ba.id as bankAccountId, ba.accountnumber as accountNumber, coa.glcode as glcode,b.code as bankName ,fd.name as fundName "
                +
                "from Chartofaccounts  coa,  fund fd, bankaccount ba left outer join bankbranch bb  on ba.branchid=bb.id left outer "
                +
                "join bank b on bb.bankid=b.id where  coa.id=ba.glcodeid and ba.fundid= fd.id and ba.isactive=true and ba.type in ('PAYMENTS','RECEIPTS_PAYMENTS') ");
        if (fundId != null && fundId != -1)
            allPaymentAccounts.append("and ba.fundid=" + fundId);
        else
            allPaymentAccounts.append(" order by fd.code,b.code,coa.glcode,ba.accountnumber");

        final Query allPaymentAccountsQry = getSession().createSQLQuery(allPaymentAccounts.toString())
                .addScalar("bankAccountId")
                .addScalar("accountNumber")
                .addScalar("glcode")
                .addScalar("bankName")
                .addScalar("fundName")
                .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
        return allPaymentAccountsQry.list();
    }

    /**
     * get All Receipt Bank Accounts for the selected Fund
     *
     */
    public List<FundFlowBean> getAllReceiptAccounts(final Long fundId) {
        final StringBuffer allAccounts = new StringBuffer(500);
        allAccounts
        .append("select ba.id as bankAccountId, ba.accountnumber as accountNumber, coa.glcode as glcode,b.code as bankName ,fd.name as fundName, "
                +
                " case when ba.narration = null then 0 else (case when instr(ba.narration,'"
                + FinancialConstants.BANKACCOUNT_WALKIN_PAYMENT_DESCRIPTION
                + "',1)  = 1 then 1 else 0 end ) end as walkinPaymentAccount "
                +
                " from Chartofaccounts  coa, fund fd, bankaccount ba left outer join bankbranch bb  on ba.branchid=bb.id left outer "
                +
                " join bank b on bb.bankid=b.id where coa.id=ba.glcodeid and ba.fundid= fd.id and ba.isactive=true and ba.type in ('RECEIPTS') ");
        if (fundId != null && fundId != -1)
            allAccounts.append(" and ba.fundid=" + fundId);
        else
            allAccounts.append(" order by fd.code, walkinPaymentAccount, b.code,coa.glcode,ba.accountnumber");
        final Query allAccountsQry = getSession().createSQLQuery(allAccounts.toString())
                .addScalar("bankAccountId")
                .addScalar("accountNumber")
                .addScalar("glcode")
                .addScalar("bankName")
                .addScalar("fundName")
                .addScalar("walkinPaymentAccount", BooleanType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
        final List<FundFlowBean> allAccountslist = allAccountsQry.list();
        return allAccountslist;

    }

    /**
     * @return
     */
    public List<FundFlowBean> getContraReceiptsForTheDay(final Date asPerDate, final Long fundId) {
        final String voucherDate = sqlformat.format(asPerDate);
        StringBuffer temp = new StringBuffer(1000);
        temp = temp
                .append(" SELECT gl.glcodeid as codeId, ba.accountnumber as accountNumber, b.name as bankName,round(SUM(case when gl.debitamount = NULL then 0 else gl.debitamount end)/100000,2) AS btbReceipt FROM"
                        +
                        " contrajournalvoucher CV  , voucherheader vh ,  generalledger gl, bankaccount ba, bankbranch bb,bank b WHERE ");
        if (fundId != null && fundId != -1)
            temp.append(" vh.fundid =" + fundId + " AND ba.fundid         =" + fundId + " and");
        temp.append(" vh.voucherdate    ='" + voucherDate + "'and gl.voucherheaderid= vh.id " +
                "  and vh.name in ('" + FinancialConstants.CONTRAVOUCHER_NAME_BTOB + "','"
                + FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND + "') " +
                "  and cv.voucherheaderid=vh.id and ba.id= cv.tobankaccountid and ba.glcodeid= gl.glcodeid AND vh.status =0   " +
                "  and bb.bankid= b.id and ba.branchid=bb.id GROUP BY GL.GLCODEID,ba.accountnumber,b.name");

        List<FundFlowBean> tempPayList;
        final Query tempQry = getSession().createSQLQuery(temp.toString())
                .addScalar("accountNumber")
                .addScalar("bankName")
                .addScalar("btbReceipt")
                .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
        tempPayList = tempQry.list();
        return tempPayList;
    }

    /**
     * get Receipt Bank Accounts of selected Fund which has Contra payment for current day
     * @param voucherDate
     * @return
     */
    public List<FundFlowBean> getContraPaymentsForTheDay(final Date asPerDate, final Long fundId) {
        final String voucherDate = sqlformat.format(asPerDate);
        StringBuffer qry = new StringBuffer(1000);
        qry = qry
                .append(" SELECT gl.glcodeid as codeId, ba.accountnumber as accountNumber, b.name as bankName, round(SUM(case when gl.creditamount = NULL then 0 else gl.creditamount end)/100000,2) AS btbPayment FROM"
                        +
                        " contrajournalvoucher CV  , voucherheader vh ,  generalledger gl, bankaccount ba, bankbranch bb,bank b WHERE ");
        if (fundId != null && fundId != -1)
            qry.append(" vh.fundid =" + fundId + " AND ba.fundid         =" + fundId + " and ");
        qry.append("vh.voucherdate    ='"
                + voucherDate
                + "'and gl.voucherheaderid= vh.id "
                +
                "  and vh.name in ('"
                + FinancialConstants.CONTRAVOUCHER_NAME_BTOB
                + "','"
                + FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND
                + "') "
                +
                "  and cv.voucherheaderid=vh.id and ba.id= cv.frombankaccountid and ba.glcodeid= gl.glcodeid AND vh.status =0  and ba.fundid=vh.fundid  "
                +
                "  and bb.bankid= b.id and ba.branchid=bb.id GROUP BY gl.glcodeId,ba.accountnumber,b.name ");
        List<FundFlowBean> tempList;
        final Query q = getSession().createSQLQuery(qry.toString())
                .addScalar("accountNumber")
                .addScalar("bankName")
                .addScalar("btbPayment")
                .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
        tempList = q.list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("account containg transactions ------" + tempList.size());
        return tempList;
    }

    /**
     * get Receipt Bank Accounts of selected Fund which has Contra payment for current day When you use contraJournal voucher put
     * fund condition for voucherheader which will remove duplicate entry duplicate is coming since Interfund transfer creates two
     * vouchers with two different funds.
     * @param voucherDate
     * @return
     */
    public List<FundFlowBean> getContraPaymentsForTheDayFromPaymentBanks(final Date asPerDate, final Long fundId) {
        final String voucherDate = sqlformat.format(asPerDate);
        StringBuffer qry = new StringBuffer(1000);
        qry = qry
                .append(" SELECT gl.glcodeid as codeId, ba.accountnumber as accountNumber, b.name as bankName, round(SUM(case when gl.creditamount = NULL then 0 else gl.creditamount end)/100000,2) AS btbPayment FROM"
                        +
                        " contrajournalvoucher CV  , voucherheader vh ,  generalledger gl, bankaccount ba, bankbranch bb,bank b WHERE  ");
        if (fundId != null && fundId != -1)
            qry = qry.append(" vh.fundid =" + fundId + " AND ba.fundid =" + fundId + " and ");
        qry = qry
                .append(" vh.voucherdate    ='"
                        + voucherDate
                        + "'and gl.voucherheaderid= vh.id "
                        +
                        "  and vh.name in ('"
                        + FinancialConstants.CONTRAVOUCHER_NAME_BTOB
                        + "','"
                        + FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND
                        + "') "
                        +
                        "  and cv.voucherheaderid=vh.id and ba.id= cv.frombankaccountid and ba.glcodeid= gl.glcodeid AND vh.status =0 and ba.fundid=vh.fundid  "
                        +
                        "  and bb.bankid= b.id and ba.branchid=bb.id GROUP BY gl.glcodeId,ba.accountnumber,b.name");
        List<FundFlowBean> tempList;
        final Query q = getSession().createSQLQuery(qry.toString())
                .addScalar("accountNumber")
                .addScalar("bankName")
                .addScalar("btbPayment")
                .setResultTransformer(Transformers.aliasToBean(FundFlowBean.class));
        tempList = q.list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("account containg transactions ------" + tempList.size());
        return tempList;
    }

    public BigDecimal getBankBalance(final Long bankaccountId, Date asPerDate, final Long bankAccGlcodeId)
    {

        try {
            asPerDate = sqlformat.parse(sqlformat.format(asPerDate));
            final Calendar calfrom = Calendar.getInstance();
            calfrom.setTime(asPerDate);
            calfrom.set(Calendar.HOUR, 0);
            calfrom.set(Calendar.MINUTE, 0);
            calfrom.set(Calendar.SECOND, 0);
            calfrom.set(Calendar.AM_PM, Calendar.AM);
            asPerDate = calfrom.getTime();

        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError("cannot.format.date",
                    "Failed during date Formatting ")));
        }
        if (bankaccountId == null)
            throw new ValidationException(Arrays.asList(new ValidationError("bankaccount.id.is.null",
                    "BankAccountId is not provided")));
       // setType(FundFlowBean.class);
        final FundFlowBean fundFlowBean = (FundFlowBean) this.find(
                "from FundFlowBean where bankAccountId=? and to_date(reportDate)=?",
                BigDecimal.valueOf(bankaccountId), asPerDate);
        // Means Report is not Generated
        if (fundFlowBean == null)
            throw new ValidationException(Arrays.asList(new ValidationError("fund.flow.report.not.generated.for.the.day",
                    "Fund Flow Report is not Generated Balance check Failed")));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Querying and getting the bank balance");
        BigDecimal bankBalance = fundFlowBean.getOpeningBalance().add(fundFlowBean.getCurrentReceipt());// since all amounts in
                                                                                                        // lakh multiply by lakh
                                                                                                        // and return
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("value from fundflow = " + bankBalance);
        bankBalance = bankBalance.multiply(new BigDecimal(100000));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("value from fundflow*1lakh = " + bankBalance);
        bankBalance = bankBalance.subtract(getContraPayment(bankaccountId, asPerDate, bankAccGlcodeId));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("after contra payment = " + bankBalance);
        bankBalance = bankBalance.add(getContraReceipt(bankaccountId, asPerDate, bankAccGlcodeId));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("after adding contra Receipt = " + bankBalance);
        bankBalance = bankBalance.subtract(getOutStandingPayment(bankaccountId, asPerDate));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" BankBalance for " + bankaccountId + " is " + bankBalance);
        return bankBalance;
    }

    /**
     * it is for single bankaccount
     * @param asPerDate
     * @param bankId TODO
     * @param bankaccountId
     */
    private BigDecimal getContraPayment(final Long bankaccountId, final Date asPerDate, final Long accountGlcodeId) {
        final StringBuffer qry = new StringBuffer(100);
        if (accountGlcodeId != null)
            qry.append(" select case when SUM(case when gl.creditamount = NULL then 0 else gl.creditamount end) = null then 0 else SUM(case when gl.creditamount = NULL then 0 else gl.creditamount end) end as payment from "
                    +
                    "  voucherheader vh, generalledger gl where vh.id=gl.voucherheaderid and gl.glcodeId="
                    + accountGlcodeId
                    +
                    "  and vh.name in ('"
                    + FinancialConstants.CONTRAVOUCHER_NAME_BTOB
                    + "','"
                    + FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND
                    + "') "
                    +
                    "  and vh.fiscalperiodid in (select id from fiscalperiod where financialyearid=(select f.id from financialyear f where CURRENT_DATE between f.startingdate and f.endingdate))"
                    +
                    " and vh.voucherdate='" + sqlformat.format(asPerDate) + "'   and vh.status =0");
        else
            qry.append(" select case when SUM(case when gl.creditamount = NULL then 0 else gl.creditamount end) = null then 0 else SUM(case when gl.creditamount = NULL then 0 else gl.creditamount end) end as payment from BankAccount  acc,"
                    +
                    " voucherheader vh, generalledger gl where vh.id=gl.voucherheaderid and acc.glcodeId= gl.glcodeId "
                    +
                    "  and vh.name in ('"
                    + FinancialConstants.CONTRAVOUCHER_NAME_BTOB
                    + "','"
                    + FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND
                    + "') "
                    +
                    " and vh.voucherdate='"
                    + sqlformat.format(asPerDate)
                    + "'  and acc.id="
                    + bankaccountId
                    + " and vh.status =0");
        final List list = getSession().createSQLQuery(qry.toString()).list();
        final BigDecimal contraPayment = (BigDecimal) list.get(0);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Contra Payments For BankId " + accountGlcodeId + " And Date " + sqlformat.format(asPerDate) + " is : "
                    + contraPayment);
        return contraPayment;
    }

    /**
     * it is for single bankaccount
     * @param bankaccountId
     * @param asPerDate
     *
     */
    private BigDecimal getContraReceipt(final Long bankaccountId, final Date asPerDate, final Long accountGlcodeId) {
        final StringBuffer qry = new StringBuffer(100);
        if (accountGlcodeId != null)
            qry.append(" select case when SUM(case when gl.debitamount = NULL then 0 else gl.debitamount end) = null then 0 else SUM(case when gl.debitamount = NULL then 0 else gl.debitamount end) end as receipt from "
                    +
                    " voucherheader vh, generalledger gl where vh.id=gl.voucherheaderid and gl.glcodeId="
                    + accountGlcodeId
                    +
                    "  and vh.name in ('"
                    + FinancialConstants.CONTRAVOUCHER_NAME_BTOB
                    + "','"
                    + FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND
                    + "') "
                    +
                    "  and vh.fiscalperiodid in (select id from fiscalperiod where financialyearid=(select f.id from financialyear f where CURRENT_DATE between f.startingdate and f.endingdate))"
                    +
                    " and vh.voucherdate='" + sqlformat.format(asPerDate) + "'  and vh.status =0");
        else
            qry.append(" select case when SUM(case when gl.debitamount = NULL then 0 else gl.debitamount end) = null then 0 else SUM(case when gl.debitamount = NULL then 0 else gl.debitamount end) end as receipt from BankAccount acc,"
                    +
                    " voucherheader vh, generalledger gl where vh.id=gl.voucherheaderid and acc.glcodeid= gl.glcodeid "
                    +
                    "  and vh.name in ('"
                    + FinancialConstants.CONTRAVOUCHER_NAME_BTOB
                    + "','"
                    + FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND
                    + "') "
                    +
                    " and vh.voucherdate='"
                    + sqlformat.format(asPerDate)
                    + "'  and acc.id="
                    + bankaccountId
                    + " and vh.status =0");
        final List list = getSession().createSQLQuery(qry.toString()).list();
        final BigDecimal contraReceipt = (BigDecimal) list.get(0);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Contra Receipt For BankId " + accountGlcodeId + " And Date " + sqlformat.format(asPerDate) + " is : "
                    + contraReceipt);
        return contraReceipt;
    }

    /**
     * it is for single bankaccount Will return the concurrence done for the day for the give bank account
     * @param bankaccountId
     * @param asPerDate
     * @return
     */
    private BigDecimal getOutStandingPayment(final Long bankaccountId, final Date asPerDate)
    {
        final List<AppConfigValues> appConfig = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF,
                "PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK");
        if (appConfig == null || appConfig.isEmpty())
            throw new ValidationException("", "PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK is not defined in AppConfig");
        String voucherStatus = "";
        final StringBuffer values = new StringBuffer(200);
        for (final AppConfigValues app : appConfig)
        {
            values.append("'");
            values.append(app.getValue());
            values.append("',");
        }
        // need to ommit the last comma
        voucherStatus = values.substring(0, values.length() - 1);

        StringBuffer outstandingPaymentQryStr = new StringBuffer(500);
        outstandingPaymentQryStr =
                outstandingPaymentQryStr
                .
                append("SELECT case when SUM(case when ph.paymentamount = null then 0 else ph.paymentamount end ) = null then 0 else SUM(case when ph.paymentamount = null then 0 else ph.paymentamount) end AS concurranceBPV"
                        +
                        " FROM voucherheader vh right join  paymentheader ph on vh.id=ph.voucherheaderid,bankaccount ba,eg_wf_states state where ph.state_id     =state.id "
                        +
                        "	and vh.id =ph.voucherheaderid and  ba.id=ph.bankaccountnumberid and ba.id="
                        + bankaccountId
                        + "")
                        .append(" and vh.fiscalperiodid in (select id from fiscalperiod where financialyearid=(select f.id from financialyear f where CURRENT_DATE between f.startingdate and f.endingdate))")
                        .append(" and vh.voucherdate >= '")
                        .append(START_FINANCIALYEAR_DATE + "' ")
                        .append(" and to_char(created_date,'dd-Mon-yyyy') ='")
                        .append(sqlformat.format(asPerDate))
                        .append("' and ( state.value in (")
                        .append(voucherStatus)

                        .append(")OR vh.status=0 ) group by accountNumber  ");

        BigDecimal outStandingPayment = BigDecimal.ZERO;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Executing outstandingPaymentQryStr query----------------------------------------------"
                    + outstandingPaymentQryStr);
        final List list = getSession().createSQLQuery(outstandingPaymentQryStr.toString()).list();
        if (!list.isEmpty())
            outStandingPayment = (BigDecimal) list.get(0);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("OutStanding payments for BankId " + bankaccountId + " And Date " + sqlformat.format(asPerDate)
                    + " is : " + outStandingPayment);
        return outStandingPayment;
    }

}