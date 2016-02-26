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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * Created on June 7, 2006
 * @author Tilak
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.springframework.beans.factory.annotation.Autowired;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

public class TrialBalance
{

    Query pstmt;
    List<Object[]> resultset;
    String endDate, startDate;
    String fundId;
    public String reqFundId[];
    public String reqFundName[];
    String fundcondition = "";
    List<TrialBalanceBean> al = new ArrayList<TrialBalanceBean>();
    ArrayList formatedArrlist = new ArrayList();
    HashMap debitAmountHs = new HashMap();
    HashMap creditAmountHs = new HashMap();
    HashMap totDrAmtHs = new HashMap();
    HashMap totCrAmtHs = new HashMap();
    double totalDr = 0.0, totalCr = 0.0, totalOpgBal = 0.0, totalClosingBal = 0.0;
    TrialBalanceBean tb;
    CommnFunctions cf = new CommnFunctions();
    java.util.Date dt = new java.util.Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    NumberFormat numberformatter = new DecimalFormat("##############0.00");
    EGovernCommon egc = new EGovernCommon();
    private static final Logger LOGGER = Logger.getLogger(TrialBalance.class);
    private static TaskFailedException taskExc;
    private BigDecimal totalClosingBalance = BigDecimal.ZERO;
    private BigDecimal totalOpeningBalance = BigDecimal.ZERO;
    private BigDecimal totalDebitAmount = BigDecimal.ZERO;
    private BigDecimal totalCreditAmount = BigDecimal.ZERO;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    // This method is called by the TrialBalance.jsp
    public ArrayList getTBReport(final String asOnDate, final String fId, final String departmentId, final String functionaryId,
            final String functionCodeId,
            final String fieldId) throws Exception
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getTBReport | Depaartment ID >>>>>>>>>>>>>>>>>>>>> := " + departmentId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getTBReport | Functionary ID >>>>>>>>>>>>>>>>>>>>> := " + functionaryId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getTBReport | Function Code ID >>>>>>>>>>>>>>>>>>>>> := " + functionCodeId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getTBReport | Field ID >>>>>>>>>>>>>>>>>>>>> := " + fieldId);
        isCurDate(asOnDate);
        try
        {
            fundId = fId;
            if (LOGGER.isInfoEnabled())
                LOGGER.info("fundid: " + fundId);

            if (fundId != null && !fundId.equalsIgnoreCase(""))
                fundcondition = " and fundid=?";
            else {
                fundcondition = " and fundid in (select id from fund where isactive=true and isnotleaf!=true )";
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("fund cond query  " + fundcondition);
            }
            dt = sdf.parse(asOnDate);
            endDate = formatter.format(dt);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("EndDate --> " + endDate);
            setDates(endDate);
            cf.getFundList(fundId, startDate, endDate);
            reqFundId = cf.reqFundId;
            reqFundName = cf.reqFundName;

            getReport(departmentId, functionaryId, functionCodeId, fieldId);
            formatReport();
        } catch (final Exception exception)
        {
            LOGGER.error("EXP in getTBReport" + exception.getMessage(), exception);
            throw exception;
        }

        return formatedArrlist;

    }

    public String getDateTime() throws Exception
    {
        final SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        return formatter1.format(sdf1.parse(egc.getCurrentDateTime()));
    }

    private void getReport(final String departmentId, final String functionaryId, final String functionCodeId,
            final String fieldId) throws Exception

    {
        String voucherMisTable = "";
        String misClause = "";
        String misDeptCond = "";
        String tsDeptCond = "";
        String functionaryCond = "";
        String tsfunctionaryCond = "";
        String functionIdCond = "";
        String tsFunctionIdCond = "";
        String fieldIdCond = "";
        String tsFieldIdCond = "";
        if (null != departmentId && !departmentId.trim().equals("")
                || null != functionaryId && !functionaryId.trim().equals("") ||
                null != fieldId && !fieldId.trim().equals("")) {
            voucherMisTable = ",vouchermis mis ";
            misClause = " and mis.voucherheaderid=vh.id ";
        }

        if (null != departmentId && !departmentId.trim().equals("")) {
            misDeptCond = " and mis.DEPARTMENTID= ?";
            tsDeptCond = " and DEPARTMENTID= ?";
        }
        if (null != functionaryId && !functionaryId.trim().equals("")) {
            functionaryCond = " and mis.FUNCTIONARYID= ?";
            tsfunctionaryCond = " and FUNCTIONARYID= ?";
        }
        if (null != functionCodeId && !functionCodeId.trim().equals("")) {
            functionIdCond = " and gl.voucherheaderid in (select distinct(voucherheaderid) from generalledger where functionid =?)";
            tsFunctionIdCond = " and FUNCTIONID= ?";
        }
        if (null != fieldId && !fieldId.trim().equals("")) {
            fieldIdCond = " and mis.divisionId= ?";
            tsFieldIdCond = " and divisionId= ?";
        }
        String defaultStatusExclude = null;
        final List<AppConfigValues> listAppConfVal = appConfigValuesService.
                getConfigValuesByModuleAndKey("finance", "statusexcludeReport");
        if (null != listAppConfVal)
            defaultStatusExclude = listAppConfVal.get(0).getValue();
        else
            throw new ApplicationRuntimeException("Exlcude statusses not  are not defined for Reports");
        final String query = " SELECT gl.glcode AS \"glcode\" ,coa.name AS \"accountHead\" ,vh.fundid AS \"fundId\",(SUM(debitamount)+SUM((SELECT case when SUM(OPENINGDEBITBALANCE)=NULL then 0 else SUM(OPENINGDEBITBALANCE) end FROM transactionsummary WHERE"
                + " financialyearid=(SELECT id FROM financialyear WHERE startingdate<='"
                + endDate
                + "' AND endingdate>='"
                + endDate
                + "')"
                + " AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode) AND fundid=vh.fundid"
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + "))/COUNT(*))-"
                + " (SUM(creditamount)+SUM((SELECT  case when SUM(OPENINGCREDITBALANCE)=NULL then 0 else SUM(OPENINGCREDITBALANCE) end FROM"
                + " transactionsummary WHERE financialyearid=(SELECT id FROM financialyear  WHERE startingdate<='"
                + endDate
                + "' AND endingdate>='"
                + endDate
                + "')"
                + " AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode) AND fundid=vh.fundid"
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + "))/COUNT(*) ) as \"amount\" "
                + " FROM generalledger gl,chartofaccounts   coa,voucherheader vh "
                + voucherMisTable
                + " WHERE coa.glcode=gl.glcode AND gl.voucherheaderid=vh.id"
                + misClause
                + " AND vh.status not in ("
                + defaultStatusExclude
                + ") "
                + " AND  vh.voucherdate<='"
                + endDate
                + "' AND vh.voucherdate>=(SELECT startingdate FROM financialyear WHERE  startingdate<='"
                + endDate
                + "' AND   endingdate>='"
                + endDate
                + "') "
                + fundcondition
                + " "
                + misDeptCond
                + functionaryCond
                + functionIdCond
                + fieldIdCond
                + " GROUP BY gl.glcode,coa.name,vh.fundid    HAVING (SUM(debitamount)>0 OR SUM(creditamount)>0)    And"
                + " (SUM(debitamount)+SUM((SELECT case when SUM(OPENINGDEBITBALANCE)=NULL then 0 else SUM(OPENINGDEBITBALANCE) end  FROM"
                + " transactionsummary WHERE  financialyearid=(SELECT id FROM financialyear       WHERE startingdate <='"
                + endDate
                + "'"
                + " AND endingdate >='"
                + endDate
                + "') AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode) "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + "))/COUNT(*))-"
                + " (SUM(creditamount)+SUM((SELECT  case when SUM(OPENINGCREDITBALANCE)=NULL then 0 else SUM(OPENINGCREDITBALANCE) end FROM"
                + " transactionsummary WHERE financialyearid=(SELECT id FROM financialyear    WHERE startingdate<='"
                + endDate
                + "' AND endingdate>='"
                + endDate
                + "') "
                + " AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode)  "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + "))/COUNT(*) )<>0"
                + " union"
                + " SELECT coa.glcode AS \"glcode\" ,coa.name AS \"name\" , fu.id as \"fundId\", SUM((SELECT case when SUM(OPENINGDEBITBALANCE) = NULL then 0 else SUM(OPENINGDEBITBALANCE) end"
                + " FROM transactionsummary WHERE financialyearid=(SELECT id FROM financialyear WHERE  startingdate<='"
                + endDate
                + "' AND endingdate>='"
                + endDate
                + "')"
                + " AND glcodeid =(SELECT id FROM chartofaccounts WHERE  glcode=coa.glcode) AND fundid= (select id from fund where id=fu.id)"
                + " "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + ")) - SUM((SELECT  case when SUM(OPENINGCREDITBALANCE) = NULL then 0 else SUM(OPENINGCREDITBALANCE) end as \"amount\" FROM transactionsummary WHERE"
                + " financialyearid=(SELECT id FROM financialyear       WHERE startingdate<='"
                + endDate
                + "' AND endingdate>='"
                + endDate
                + "') AND glcodeid =(SELECT id FROM chartofaccounts"
                + " WHERE glcode=coa.glcode)AND fundid= (select id from fund where id=fu.id)"
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + ")) "
                + " FROM chartofaccounts  coa, fund fu  WHERE  fu.id IN(SELECT fundid from transactionsummary WHERE financialyearid = (SELECT id FROM financialyear WHERE startingdate<='"
                + endDate
                + "' "
                + " AND endingdate>='"
                + endDate
                + "') "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + " AND glcodeid =(SELECT id   FROM chartofaccounts WHERE  glcode=coa.glcode) ) AND coa.id NOT IN(SELECT glcodeid FROM generalledger gl,voucherheader vh "
                + voucherMisTable
                + " WHERE "
                + " vh.status not in ("
                + defaultStatusExclude
                + ") "
                + misClause
                + misDeptCond
                + functionaryCond
                + functionIdCond
                + fieldIdCond
                + " AND vh.id=gl.voucherheaderid AND vh.fundid=fu.id AND vh.voucherdate<='"
                + endDate
                + "' AND vh.voucherdate>=(SELECT startingdate FROM financialyear WHERE  startingdate<='"
                + endDate
                + "' AND   endingdate>='"
                + endDate
                + "') "
                + fundcondition
                + ")"
                + " GROUP BY coa.glcode,coa.name, fu.id"
                + " HAVING((SUM((SELECT case when SUM(OPENINGDEBITBALANCE) = NULL then 0 else SUM(OPENINGDEBITBALANCE) end FROM transactionsummary WHERE"
                + " financialyearid=(SELECT id FROM financialyear       WHERE startingdate<='"
                + endDate
                + "' AND endingdate>='"
                + endDate
                + "') AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=coa.glcode) "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + " )) >0 )"
                + " OR (SUM((SELECT  SUM(OPENINGCREDITBALANCE) = NULL then 0 else SUM(OPENINGCREDITBALANCE) FROM transactionsummary WHERE financialyearid=(SELECT id FROM financialyear WHERE startingdate<='"
                + endDate
                + "' AND endingdate>='"
                + endDate
                + "')"
                + " AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=coa.glcode)     "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond + tsFunctionIdCond + tsFieldIdCond + "))>0 ))  ORDER BY \"glcode\"";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("&&&query  " + query);
        try
        {
            HashMap drAmtSubList = null;
            HashMap crAmtSubList = null;
            String glcode = "";
            String name = "";
            Double amount = new Double(0);
            String fuId = "";
            final BigDecimal totalDr[] = new BigDecimal[reqFundId.length + 2];
            final BigDecimal totalCr[] = new BigDecimal[reqFundId.length + 2];

            int j = 1;
            pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query);

            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Jva lue " + j);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);

            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);
            // pstmt.setString(j++, endDate);
            // pstmt.setString(j++, endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(j++, fundId);
            if (null != departmentId && !departmentId.trim().equals(""))
                pstmt.setString(j++, departmentId);
            if (null != functionaryId && !functionaryId.trim().equals(""))
                pstmt.setString(j++, functionaryId);
            if (null != functionCodeId && !functionCodeId.trim().equals(""))
                pstmt.setString(j++, functionCodeId);
            if (null != fieldId && !fieldId.trim().equals(""))
                pstmt.setString(j++, fieldId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("j value is " + j);

            resultset = pstmt.list();
            for (final Object[] element : resultset) {
                glcode = element[0].toString();
                name = element[1].toString();
                fuId = element[2].toString();
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("fuId::::" + fuId);
                amount = Double.parseDouble(element[3].toString());
                Double debAmt = new Double(0);
                Double creAmt = new Double(0);
                if (amount > 0)
                    debAmt = amount;
                else if (amount < 0)
                    creAmt = amount;

                if (LOGGER.isInfoEnabled())
                    LOGGER.info("amount::::" + amount);
                drAmtSubList = new HashMap();
                crAmtSubList = new HashMap();

                if (!debitAmountHs.containsKey(glcode))
                {
                    tb = new TrialBalanceBean();
                    tb.setAccCode(glcode);
                    tb.setAccName(name);
                    al.add(tb);
                    // BigDecimal fundTotal=new BigDecimal(0.00);
                    for (final String element2 : reqFundId)
                        if (element2.equalsIgnoreCase(fuId))
                        {
                            drAmtSubList.put(element2, debAmt);
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info(" debAmt::::" + debAmt);
                            debitAmountHs.put(glcode, drAmtSubList);
                        }
                        else
                        {
                            drAmtSubList.put(element2, new Double(0));
                            crAmtSubList.put(element2, new Double(0));
                            debitAmountHs.put(glcode, drAmtSubList);
                        }

                }

                else
                {
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("inside else fuId:::" + fuId + " debAmt::::" + debAmt);
                    ((HashMap) debitAmountHs.get(glcode)).put(fuId, debAmt);
                }

                if (!totDrAmtHs.containsKey(glcode))
                {
                    for (int i = 0; i < reqFundId.length; i++)
                        if (reqFundId[i].equalsIgnoreCase(fuId))
                        {
                            if (totalDr[i] == null)
                                totalDr[i] = BigDecimal.ZERO;
                            totalDr[i] = totalDr[i].add(BigDecimal.valueOf(Double.parseDouble(((HashMap) debitAmountHs
                                    .get(glcode)).get(reqFundId[i]).toString())));
                            totDrAmtHs.put(reqFundId[i], totalDr[i]);
                        }
                } else
                    for (int i = 0; i < reqFundId.length; i++)
                        if (reqFundId[i].equalsIgnoreCase(fuId))
                        {
                            totalDr[i] = totalDr[i].add(BigDecimal.valueOf(Double.parseDouble(((HashMap) debitAmountHs
                                    .get(glcode)).get(fuId).toString())));
                            totDrAmtHs.put(fuId, totalDr[i]);
                        }

                if (!creditAmountHs.containsKey(glcode))
                {
                    for (final String element2 : reqFundId)
                        if (element2.equalsIgnoreCase(fuId))
                            crAmtSubList.put(element2, creAmt);
                        else
                        {
                            drAmtSubList.put(element2, new Double(0));
                            crAmtSubList.put(element2, new Double(0));
                            debitAmountHs.put(glcode, drAmtSubList);
                        }
                    creditAmountHs.put(glcode, crAmtSubList);

                } else
                    ((HashMap) creditAmountHs.get(glcode)).put(fuId, creAmt);

                if (!totCrAmtHs.containsKey(glcode))
                {
                    for (int i = 0; i < reqFundId.length; i++)
                        if (reqFundId[i].equalsIgnoreCase(fuId))
                        {
                            if (totalCr[i] == null)
                                totalCr[i] = BigDecimal.ZERO;
                            totalCr[i] = totalCr[i].add(BigDecimal.valueOf(Double.parseDouble(((HashMap) creditAmountHs
                                    .get(glcode)).get(reqFundId[i]).toString())));
                            totCrAmtHs.put(reqFundId[i], totalCr[i].negate());
                        }
                } else
                    for (int i = 0; i < reqFundId.length; i++)
                        if (reqFundId[i].equalsIgnoreCase(fuId))
                        {
                            totalCr[i] = totalCr[i].add(BigDecimal.valueOf(Double.parseDouble(((HashMap) creditAmountHs
                                    .get(glcode)).get(fuId).toString())));
                            totCrAmtHs.put(fuId, totalCr[i].negate());
                        }
            }

        } catch (final Exception e)
        {
            LOGGER.error("Error in getReport" + e.getMessage(), e);
            throw taskExc;
        }
    }

    private void formatReport() throws Exception
    {
        HashMap formatReport = null;
        TrialBalanceBean tb = null;
        Set set = null;
        Double debitAmount = 0.0;
        Double creditAmount = 0.0;
        BigDecimal drGrandTotal = new BigDecimal("0.00");
        BigDecimal crGrandTotal = new BigDecimal("0.00");
        BigDecimal diffTotal = new BigDecimal("0.00");
        TreeSet keyList = null;
        TreeSet keyListTotDr = null;
        TreeSet keyListTotCr = null;
        TreeSet keyListCredit = null;
        Set setTotDr = null;
        Set setTotCr = null;
        Set setCredit = null;
        if (LOGGER.isInfoEnabled())
            LOGGER.info(">>>>>  al.size()  " + al.size());

        for (int i = 0; i <= al.size(); i++)
        {
            if (i != 0 && i == al.size())
            {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(">>>>> i==al.size():  " + i);
                final HashMap formatReport1 = new HashMap();
                formatReport1.put("serialNo", "<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
                formatReport1.put("accCode", "<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
                formatReport1.put("accName",
                        "<hr noshade color=black size=1><b>Opening Balanace Diff:<hr noshade color=black size=1></b>");

                formatReport = new HashMap();
                formatReport.put("serialNo", "<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
                formatReport.put("accCode", "<hr noshade color=black size=1><b>Total:<hr noshade color=black size=1></b>");
                formatReport.put("accName", "<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
                setTotDr = totDrAmtHs.keySet();
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(">>>>> setTotDeb:  " + setTotDr);
                keyListTotDr = new TreeSet(setTotDr);
                for (final Iterator val = keyListTotDr.iterator(); val.hasNext();)
                {
                    final String fundId = val.next().toString();
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info(">>>>> fundId:  " + fundId);
                    BigDecimal totDr = new BigDecimal("0.00");
                    totDr = (BigDecimal) totDrAmtHs.get(fundId);
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info(">>>>> totDr:  " + totDr);
                    drGrandTotal = drGrandTotal.add(totDr);
                    setTotCr = totCrAmtHs.keySet();
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info(">>>>> setTotCr:  " + setTotCr);
                    keyListTotCr = new TreeSet(setTotCr);
                    for (final Iterator val2 = keyListTotCr.iterator(); val2.hasNext();)
                    {
                        BigDecimal totCr = new BigDecimal("0.00");
                        final String fundId1 = val2.next().toString();
                        if (fundId.equals(fundId1))
                        {
                            totCr = (BigDecimal) totCrAmtHs.get(fundId1);
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info(">>>>> totDr:  " + totDr);
                            crGrandTotal = crGrandTotal.add(totCr);
                            final BigDecimal diff = totDr.subtract(totCr);
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("diff--->" + diff);
                            if (diff.compareTo(new BigDecimal("0.00")) < 0.00)
                            {
                                final BigDecimal diff1 = diff.multiply(new BigDecimal(-1));
                                formatReport1.put("debitAmount" + fundId, "<hr noshade color=black size=1><b>"
                                        + numberToString(diff1.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                                        + "<hr noshade color=black size=1></b>");
                                formatReport.put("debitAmount" + fundId, "<hr noshade color=black size=1><b>"
                                        + numberToString(totDr.subtract(diff).setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                                        + "<hr noshade color=black size=1></b>");
                                formatReport.put("creditAmount" + fundId, "<hr noshade color=black size=1><b>"
                                        + numberToString(totCr.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                                        + "<hr noshade color=black size=1></b>");
                            }
                            else
                            {
                                formatReport1.put("creditAmount" + fundId, "<hr noshade color=black size=1><b>"
                                        + numberToString(diff.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                                        + "<hr noshade color=black size=1></b>");
                                formatReport.put("debitAmount" + fundId, "<hr noshade color=black size=1><b>"
                                        + numberToString(totDr.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                                        + "<hr noshade color=black size=1></b>");
                                formatReport.put("creditAmount" + fundId, "<hr noshade color=black size=1><b>"
                                        + numberToString(totCr.add(diff).setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                                        + "<hr noshade color=black size=1></b>");
                            }
                        }
                    }
                }

                if (LOGGER.isInfoEnabled())
                    LOGGER.info(">>>>> drGrandTotal:  " + drGrandTotal);
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(">>>>> crGrandTotal:  " + crGrandTotal);
                diffTotal = drGrandTotal.subtract(crGrandTotal);
                if (diffTotal.compareTo(new BigDecimal("0.00")) < 0.00)
                {
                    final BigDecimal diffTotal1 = diffTotal.multiply(new BigDecimal(-1));
                    formatReport1.put("drTotal",
                            "<hr noshade color=black size=1><b>"
                                    + numberToString(diffTotal1.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                                    + "</b><hr noshade color=black size=1>");
                    formatReport1.put("crTotal",
                            "<hr noshade color=black size=1><b>" + numberToString(new BigDecimal(0).toString())
                            + "</b><hr noshade color=black size=1>");
                    formatReport.put(
                            "drTotal",
                            "<hr noshade color=black size=1><b>"
                                    + numberToString(drGrandTotal.subtract(diffTotal).setScale(2, BigDecimal.ROUND_HALF_UP)
                                            .toString()) + "</b><hr noshade color=black size=1>");
                    formatReport.put(
                            "crTotal",
                            "<hr noshade color=black size=1><b>"
                                    + numberToString(crGrandTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                                    + "</b><hr noshade color=black size=1>");
                }
                else
                {
                    formatReport1.put("crTotal",
                            "<hr noshade color=black size=1><b>"
                                    + numberToString(diffTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                                    + "</b><hr noshade color=black size=1>");
                    formatReport1.put("drTotal",
                            "<hr noshade color=black size=1><b>" + numberToString(new BigDecimal(0).toString())
                            + "</b><hr noshade color=black size=1>");
                    formatReport.put(
                            "drTotal",
                            "<hr noshade color=black size=1><b>"
                                    + numberToString(drGrandTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                                    + "</b><hr noshade color=black size=1>");
                    formatReport.put("crTotal", "<hr noshade color=black size=1><b>"
                            + numberToString(crGrandTotal.add(diffTotal).setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                            + "</b><hr noshade color=black size=1>");
                }
                formatedArrlist.add(formatReport1);
                formatedArrlist.add(formatReport);
            }

            if (i < al.size())
            {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(">>>>>  i  " + i);
                tb = al.get(i);
                formatReport = new HashMap();
                formatReport = new HashMap();
                formatReport.put("serialNo", i + 1 + "");
                formatReport.put("accCode", tb.getAccCode());
                formatReport.put("accName", tb.getAccName());
                if (debitAmountHs.get(tb.getAccCode()) != null)
                {
                    set = null;
                    set = ((HashMap) debitAmountHs.get(tb.getAccCode())).keySet();
                    keyList = new TreeSet(set);
                    BigDecimal glcodeDrTotal = new BigDecimal("0.00");
                    for (final Iterator val = keyList.iterator(); val.hasNext();)
                    {
                        final String fundId = val.next().toString();
                        debitAmount = (Double) ((HashMap) debitAmountHs.get(tb.getAccCode())).get(fundId);
                        glcodeDrTotal = glcodeDrTotal.add(new BigDecimal(debitAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
                        formatReport.put("debitAmount" + fundId, numberToString(debitAmount.toString()));
                        formatReport.put("drTotal", "<b>" + numberToString(glcodeDrTotal.toString()) + "</b>");
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info(">>> debitAmount  >>" + debitAmount);
                    }
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info(">>> glcodeDrTotal  >>" + glcodeDrTotal);
                }
                else
                {
                    set = debitAmountHs.keySet();
                    keyList = new TreeSet(set);

                    for (final Iterator val = keyList.iterator(); val.hasNext();)
                    {
                        final String fundId = val.next().toString();
                        debitAmount = (Double) debitAmountHs.get(fundId);
                        formatReport.put("debitAmount" + fundId, numberToString(debitAmount.toString()));
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info(">>> debitAmount  >>" + debitAmount);
                    }
                }

                if (creditAmountHs.get(tb.getAccCode()) != null)
                {
                    setCredit = ((HashMap) creditAmountHs.get(tb.getAccCode())).keySet();
                    keyListCredit = new TreeSet(setCredit);
                    BigDecimal glcodeCrTotal = new BigDecimal("0.00");
                    for (final Iterator val = keyListCredit.iterator(); val.hasNext();)
                    {
                        final String fundId = val.next().toString();
                        creditAmount = (Double) ((HashMap) creditAmountHs.get(tb.getAccCode())).get(fundId);
                        if (creditAmount != 0.0)
                        {
                            final Double creditAmount1 = creditAmount * -1;
                            formatReport.put("creditAmount" + fundId, numberToString(creditAmount1.toString()));
                        } else
                            formatReport.put("creditAmount" + fundId, numberToString(creditAmount.toString()));
                        glcodeCrTotal = glcodeCrTotal
                                .add(new BigDecimal(creditAmount * -1).setScale(2, BigDecimal.ROUND_HALF_UP));
                        formatReport.put("crTotal", "<b>" + numberToString(glcodeCrTotal.toString()) + "<b>");
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info(">>> creditAmount  >>" + creditAmount);
                    }
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info(">>> glcodeCrTotal  >>" + glcodeCrTotal);
                }
                else
                {
                    setCredit = null;
                    setCredit = creditAmountHs.keySet();
                    keyListCredit = new TreeSet(setCredit);

                    for (final Iterator val = keyListCredit.iterator(); val.hasNext();)
                    {
                        final String fundId = val.next().toString();
                        creditAmount = (Double) creditAmountHs.get(fundId);
                        if (creditAmount != 0.0)
                            // Double creditAmount1=creditAmount*-1;
                            formatReport.put("creditAmount" + fundId, numberToString(creditAmount.toString()));
                        else
                            formatReport.put("creditAmount" + fundId, numberToString(creditAmount.toString()));
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info(">>> creditAmount  >>" + creditAmount);
                    }
                }

                if (LOGGER.isInfoEnabled())
                    LOGGER.info(">>> formatReport  >>" + formatReport);
                formatedArrlist.add(formatReport);
            }
        }
    }

    public void isCurDate(final String VDate) throws TaskFailedException {

        final EGovernCommon egc = new EGovernCommon();
        try {
            final String today = egc.getCurrentDate();
            final String[] dt2 = today.split("/");
            final String[] dt1 = VDate.split("/");

            final int ret = Integer.parseInt(dt2[2]) > Integer.parseInt(dt1[2]) ? 1 : Integer.parseInt(dt2[2]) < Integer
                    .parseInt(dt1[2]) ? -1 : Integer.parseInt(dt2[1]) > Integer.parseInt(dt1[1]) ? 1 : Integer
                            .parseInt(dt2[1]) < Integer.parseInt(dt1[1]) ? -1
                                    : Integer.parseInt(dt2[0]) > Integer.parseInt(dt1[0]) ? 1 : Integer.parseInt(dt2[0]) < Integer
                                            .parseInt(dt1[0]) ? -1 : 0;
                                    if (ret == -1)
                                        throw new Exception();

        } catch (final Exception ex) {
            LOGGER.error("Exception in isCurDate" + ex.getMessage(), ex);
            throw new TaskFailedException("Date Should be within the today's date");
        }

    }

    /**
     * Set Current Strting and Ending dates as well as Previous Strting and Ending dates
     * @param endDate
     * @param con
     * @throws Exception
     */
    private void setDates(final String endDate) throws Exception
    {

        try
        {
            List<Object[]> rs = null;
            final String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" " +
                    "FROM financialYear WHERE startingDate <= ? AND endingDate >= ?";
            final Query pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setString(0, endDate);
            pst.setString(1, endDate);
            rs = pst.list();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("query: " + query);
            /* rs = stmt.executeQuery(query); */
            for (final Object[] element : rs) {
                startDate = element[0].toString();
                this.endDate = endDate;
            }
            if (rs == null || rs.size() == 0)
                throw new Exception("Reports not defined for this financial year");
        } catch (final Exception ex)
        {
            LOGGER.error("Error in getting Starting date" + ex.getMessage(), ex);
            // Throwing the same exception
            throw ex;
        }

    }

    public static StringBuffer numberToString(final String strNumberToConvert)
    {
        String strNumber = "", signBit = "";
        if (strNumberToConvert.startsWith("-"))
        {
            strNumber = "" + strNumberToConvert.substring(1, strNumberToConvert.length());
            signBit = "-";
        }
        else
            strNumber = "" + strNumberToConvert;
        final DecimalFormat dft = new DecimalFormat("##############0.00");
        final String strtemp = "" + dft.format(Double.parseDouble(strNumber));
        StringBuffer strbNumber = new StringBuffer(strtemp);
        final int intLen = strbNumber.length();

        for (int i = intLen - 6; i > 0; i = i - 2)
            strbNumber.insert(i, ',');
        if (signBit.equals("-"))
            strbNumber = strbNumber.insert(0, "-");
        return strbNumber;
    }

    // This method is called by the TrialBalance.jsp for single fund and date range
    public ArrayList getTBReportForDateRange(final String strtDate, final String toDate, final String fId,
            final String departmentId,
            final String functionaryId, final String functionCodeId, final String fieldId) throws TaskFailedException
    {
        // isCurDate(con,toDate);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getTBReportForDateRange | Depaartment ID >>>>>>>>>>>>>>>>>>>>> := " + departmentId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getTBReportForDateRange | Functionary ID >>>>>>>>>>>>>>>>>>>>> := " + functionaryId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getTBReportForDateRange | Function Code ID >>>>>>>>>>>>>>>>>>>>> := " + functionCodeId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getTBReportForDateRange | Field ID >>>>>>>>>>>>>>>>>>>>> := " + fieldId);
        try
        {
            fundId = fId;
            dt = sdf.parse(strtDate);
            startDate = formatter.format(dt);
            dt = sdf.parse(toDate);
            endDate = formatter.format(dt);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("EndDate --> " + endDate + " fundid  " + fundId);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("StartDate --> " + startDate + " fundid  " + fundId);
            getTBReport(departmentId, functionaryId, functionCodeId, fieldId);
            formatTBReport();
        } catch (final Exception e)
        {
            LOGGER.error("Exception in getTBReportForDateRange" + e.getMessage(), e);
            throw taskExc;
        }

        return (ArrayList) al;

    }

    private void getTBReport(final String departmentId, final String functionaryId, final String functionCodeId,
            final String fieldId) throws Exception
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getTBReport");
        String voucherMisTable = "";
        String misClause = "";
        String misDeptCond = "";
        String tsDeptCond = "";
        String functionaryCond = "";
        String tsfunctionaryCond = "";
        String functionIdCond = "";
        String tsFunctionIdCond = "";
        String fieldIdCond = "";
        String tsFieldIdCond = "";
        if (null != departmentId && !departmentId.trim().equals("")
                || null != functionaryId && !functionaryId.trim().equals("")
                || null != fieldId && !fieldId.trim().equals("")) {
            voucherMisTable = ",vouchermis mis ";
            misClause = " and mis.voucherheaderid=vh.id ";
        }

        if (null != departmentId && !departmentId.trim().equals("")) {
            misDeptCond = " and mis.DEPARTMENTID= ?";
            tsDeptCond = " and ts.DEPARTMENTID= ?";
        }
        if (null != functionaryId && !functionaryId.trim().equals("")) {
            functionaryCond = " and mis.FUNCTIONARYID= ?";
            tsfunctionaryCond = " and ts.FUNCTIONARYID= ?";
        }
        if (null != functionCodeId && !functionCodeId.trim().equals("")) {
            functionIdCond = " and gl.functionid =?";
            tsFunctionIdCond = " and ts.FUNCTIONID= ?";
        }
        if (null != fieldId && !fieldId.trim().equals("")) {
            fieldIdCond = " and mis.divisionId= ?";
            tsFieldIdCond = " and divisionId= ?";
        }
        String defaultStatusExclude = null;
        final List<AppConfigValues> listAppConfVal = appConfigValuesService.
                getConfigValuesByModuleAndKey("finance", "statusexcludeReport");
        if (null != listAppConfVal)
            defaultStatusExclude = listAppConfVal.get(0).getValue();
        else
            throw new ApplicationRuntimeException("Exlcude statusses not  are not defined for Reports");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("get Opening balance for all account codes");
        // get Opening balance for all account codes
        final String openingBalanceStr = "SELECT coa.glcode AS accCode ,coa.name  AS accName, SUM(ts.openingcreditbalance) as creditOPB,sum(ts.openingdebitbalance) as debitOPB"
                +
                " FROM transactionsummary ts,chartofaccounts coa,financialyear fy "
                +
                " WHERE ts.glcodeid=coa.id  AND ts.financialyearid=fy.id and ts.fundid=?"
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond + tsFieldIdCond +
                " AND fy.startingdate<=TO_DATE(?) AND fy.endingdate>=TO_DATE(?) " +
                " GROUP BY ts.glcodeid,coa.glcode,coa.name ORDER BY coa.glcode ASC";
        int i = 0;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Query Str" + openingBalanceStr);
        final Query openingBalanceQry = HibernateUtil.getCurrentSession().createSQLQuery(openingBalanceStr)
                .addScalar("accCode")
                .addScalar("accName")
                .addScalar("creditOPB", BigDecimalType.INSTANCE)
                .addScalar("debitOPB", BigDecimalType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(TrialBalanceBean.class));
        openingBalanceQry.setLong(i++, Long.valueOf(fundId));
        if (null != departmentId && !departmentId.trim().equals(""))
            openingBalanceQry.setLong(i++, Long.valueOf(departmentId));
        if (null != functionaryId && !functionaryId.trim().equals(""))
            openingBalanceQry.setLong(i++, Long.valueOf(functionaryId));
        if (null != functionCodeId && !functionCodeId.trim().equals(""))
            openingBalanceQry.setLong(i++, Long.valueOf(functionCodeId));
        if (null != fieldId && !fieldId.trim().equals(""))
            openingBalanceQry.setLong(i++, Long.valueOf(fieldId));
        openingBalanceQry.setString(i++, startDate);
        openingBalanceQry.setString(i++, endDate);
        final List<TrialBalanceBean> openingBalanceList = openingBalanceQry.list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("get Opening balance for all account codes reulted in " + openingBalanceList.size());

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("get till date balance for all account codes");
        // get till date balance for all account codes
        final String tillDateOPBStr = "SELECT coa.glcode AS accCode ,coa.name  AS accName, SUM(gl.creditAmount) as tillDateCreditOPB,sum(gl.debitAmount) as tillDateDebitOPB"
                +
                " FROM generalledger  gl,chartofaccounts coa,financialyear fy,Voucherheader vh "
                + voucherMisTable
                +
                " WHERE gl.glcodeid=coa.id and vh.id=gl.voucherheaderid  and vh.fundid=? "
                + misClause
                + misDeptCond
                + functionaryCond + functionIdCond + fieldIdCond +
                " AND vh.voucherdate>=fy.startingdate AND vh.voucherdate<=TO_DATE(TO_DATE(?)-1)" +
                " AND fy.startingdate<=TO_DATE(?) AND fy.endingdate>=TO_DATE(?)" +
                " AND vh.status not in (" + defaultStatusExclude + ")" +
                " GROUP BY gl.glcodeid,coa.glcode,coa.name ORDER BY coa.glcode ASC";
        i = 0;
        final Query tillDateOPBQry = HibernateUtil.getCurrentSession().createSQLQuery(tillDateOPBStr)
                .addScalar("accCode")
                .addScalar("accName")
                .addScalar("tillDateCreditOPB", BigDecimalType.INSTANCE)
                .addScalar("tillDateDebitOPB", BigDecimalType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(TrialBalanceBean.class));
        tillDateOPBQry.setLong(i++, Long.valueOf(fundId));

        if (null != departmentId && !departmentId.trim().equals(""))
            tillDateOPBQry.setLong(i++, Long.valueOf(departmentId));
        if (null != functionaryId && !functionaryId.trim().equals(""))
            tillDateOPBQry.setLong(i++, Long.valueOf(functionaryId));
        if (null != functionCodeId && !functionCodeId.trim().equals(""))
            tillDateOPBQry.setLong(i++, Long.valueOf(functionCodeId));
        if (null != fieldId && !fieldId.trim().equals(""))
            tillDateOPBQry.setLong(i++, Long.valueOf(fieldId));

        tillDateOPBQry.setString(i++, startDate);
        tillDateOPBQry.setString(i++, startDate);
        tillDateOPBQry.setString(i++, endDate);
        final List<TrialBalanceBean> tillDateOPBList = tillDateOPBQry.list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("get till date balance for all account codes reulted in " + tillDateOPBList.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("get current debit and credit sum for all account codes  ");
        // get current debit and credit sum for all account codes
        final String currentDebitCreditStr = "SELECT coa.glcode AS accCode ,coa.name  AS accName, SUM(gl.creditAmount) as creditAmount,sum(gl.debitAmount) as debitAmount"
                +
                " FROM generalledger gl,chartofaccounts coa,financialyear fy,Voucherheader vh "
                + voucherMisTable
                +
                " WHERE gl.glcodeid=coa.id and vh.id= gl.voucherheaderid AND  vh.fundid=? "
                + misClause
                + misDeptCond
                + functionaryCond + functionIdCond + fieldIdCond +
                " AND vh.voucherdate>=TO_DATE(?) AND vh.voucherdate<=TO_DATE(?)" +
                " AND fy.startingdate<=TO_DATE(?) AND fy.endingdate>=TO_DATE(?)" +
                " AND vh.status not in (" + defaultStatusExclude + ") " +
                " GROUP BY gl.glcodeid,coa.glcode,coa.name ORDER BY coa.glcode ASC";
        i = 0;
        final Query currentDebitCreditQry = HibernateUtil.getCurrentSession().createSQLQuery(currentDebitCreditStr)
                .addScalar("accCode")
                .addScalar("accName")
                .addScalar("creditAmount", BigDecimalType.INSTANCE)
                .addScalar("debitAmount", BigDecimalType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(TrialBalanceBean.class));
        currentDebitCreditQry.setLong(i++, Long.valueOf(fundId));

        if (null != departmentId && !departmentId.trim().equals(""))
            currentDebitCreditQry.setLong(i++, Long.valueOf(departmentId));
        if (null != functionaryId && !functionaryId.trim().equals(""))
            currentDebitCreditQry.setLong(i++, Long.valueOf(functionaryId));
        if (null != functionCodeId && !functionCodeId.trim().equals(""))
            currentDebitCreditQry.setLong(i++, Long.valueOf(functionCodeId));
        if (null != fieldId && !fieldId.trim().equals(""))
            currentDebitCreditQry.setLong(i++, Long.valueOf(fieldId));
        currentDebitCreditQry.setString(i++, startDate);
        currentDebitCreditQry.setString(i++, endDate);
        currentDebitCreditQry.setString(i++, startDate);
        currentDebitCreditQry.setString(i++, endDate);
        final List<TrialBalanceBean> currentDebitCreditList = currentDebitCreditQry.list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("get current debit and credit sum for all account codes resulted in   " + currentDebitCreditList.size());
        final Map<String, TrialBalanceBean> tbMap = new LinkedHashMap<String, TrialBalanceBean>();
        totalClosingBalance = BigDecimal.ZERO;
        totalOpeningBalance = BigDecimal.ZERO;

        /**
         * out of 3 list put one(openingBalanceList) into Linked hash map with accountcode as key So that if other two lists has
         * entry for an account code it will be merged else new entry will added to map finally return the contents of the map as
         * list
         */
        if (!openingBalanceList.isEmpty())
            for (final TrialBalanceBean tb : openingBalanceList)
            {
                tb.setOpeningBalance(tb.getDebitOPB().subtract(tb.getCreditOPB()));
                tb.setClosingBalance(tb.getOpeningBalance());
                tbMap.put(tb.getAccCode(), tb);

            }
        for (final TrialBalanceBean tillDateTB : tillDateOPBList)
            if (null != tbMap.get(tillDateTB.getAccCode()))
            {
                final BigDecimal opb = tbMap.get(tillDateTB.getAccCode()).getOpeningBalance()
                        .add(tillDateTB.getTillDateDebitOPB().subtract(tillDateTB.getTillDateCreditOPB()));
                tbMap.get(tillDateTB.getAccCode()).setOpeningBalance(opb);
                tbMap.get(tillDateTB.getAccCode()).setClosingBalance(opb);

            } else
            {
                tillDateTB.setOpeningBalance(tillDateTB.getTillDateDebitOPB().subtract(tillDateTB.getTillDateCreditOPB()));
                tillDateTB.setClosingBalance(tillDateTB.getOpeningBalance());
                tbMap.put(tillDateTB.getAccCode(), tillDateTB);
            }
        BigDecimal cb = BigDecimal.ZERO;
        for (final TrialBalanceBean currentAmounts : currentDebitCreditList)
            if (null != tbMap.get(currentAmounts.getAccCode()))
            {

                tbMap.get(currentAmounts.getAccCode()).setDebitAmount(currentAmounts.getDebitAmount());
                tbMap.get(currentAmounts.getAccCode()).setCreditAmount(currentAmounts.getCreditAmount());
                cb = tbMap.get(currentAmounts.getAccCode()).getOpeningBalance().add(currentAmounts.getDebitAmount())
                        .subtract(currentAmounts.getCreditAmount());
                tbMap.get(currentAmounts.getAccCode()).setClosingBalance(cb);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("old amounts" + totalOpeningBalance + "    " + totalClosingBalance);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Current amounts" + tbMap.get(currentAmounts.getAccCode()).getOpeningBalance() + "    " + cb);
                totalOpeningBalance = totalOpeningBalance.add(tbMap.get(currentAmounts.getAccCode()).getOpeningBalance());
                totalClosingBalance = totalClosingBalance.add(cb);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("After Amounts" + totalOpeningBalance + "    " + totalClosingBalance);
            } else
            {
                currentAmounts.setOpeningBalance(BigDecimal.ZERO);
                cb = currentAmounts.getOpeningBalance().add(currentAmounts.getDebitAmount())
                        .subtract(currentAmounts.getCreditAmount());
                currentAmounts.setClosingBalance(cb);
                currentAmounts.setOpeningBalance(BigDecimal.ZERO);
                tbMap.put(currentAmounts.getAccCode(), currentAmounts);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("old getTBReport" + totalOpeningBalance + "    " + totalClosingBalance);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Current amounts" + tbMap.get(currentAmounts.getAccCode()).getOpeningBalance() + "    " + cb);
                totalClosingBalance = totalClosingBalance.add(cb);
                totalOpeningBalance = totalOpeningBalance.add(currentAmounts.getOpeningBalance());
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("After getTBReport" + totalOpeningBalance + "    " + totalClosingBalance);

            }
        al.addAll(tbMap.values());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting getTBReport" + totalOpeningBalance + "    " + totalClosingBalance);

    }

    private void formatTBReport()

    {

        for (final TrialBalanceBean tb : al)
        {
            if (null == tb.getOpeningBalance())
            {
                tb.setOpeningBal("0.00");
                tb.setOpeningBalance(BigDecimal.ZERO);
            }
            else if (tb.getOpeningBalance().compareTo(BigDecimal.ZERO) > 0)
                tb.setOpeningBal(numberToString(tb.getOpeningBalance().toString()).toString() + " Dr");
            else if (tb.getOpeningBalance().compareTo(BigDecimal.ZERO) < 0)
                tb.setOpeningBal(numberToString(tb.getOpeningBalance().multiply(new BigDecimal(-1)).toString()).toString()
                        + " Cr");
            else
                tb.setOpeningBal(numberToString(tb.getOpeningBalance().toString()).toString());
            if (null == tb.getClosingBalance())
            {
                tb.setClosingBal("0.00");
                tb.setClosingBalance(BigDecimal.ZERO);
            }

            else if (tb.getClosingBalance().compareTo(BigDecimal.ZERO) > 0)
                tb.setClosingBal(numberToString(tb.getClosingBalance().toString()).toString() + " Dr");
            else if (tb.getClosingBalance().compareTo(BigDecimal.ZERO) < 0)
                tb.setClosingBal(numberToString(tb.getClosingBalance().multiply(new BigDecimal(-1)).toString()).toString()
                        + " Cr");
            else
                tb.setClosingBal(tb.getClosingBalance().setScale(2).toString());
            if (tb.getDebitAmount() != null)
                tb.setDebit(numberToString(tb.getDebitAmount().toString()).toString());
            else
            {
                tb.setDebit("0.00");
                tb.setDebitAmount(BigDecimal.ZERO);
            }
            if (tb.getCreditAmount() != null)
                tb.setCredit(numberToString(tb.getCreditAmount().toString()).toString());
            else
            {
                tb.setCredit("0.00");
                tb.setCreditAmount(BigDecimal.ZERO);
            }
            totalDebitAmount = totalDebitAmount.add(tb.getDebitAmount());
            totalCreditAmount = totalCreditAmount.add(tb.getCreditAmount());

        }

        final TrialBalanceBean tb = new TrialBalanceBean();
        tb.setSerialNo("<hr noshade color=black size=1><b>Sl No:<hr noshade color=black size=1></b>");
        tb.setAccCode("<hr noshade color=black size=1><b>Total               :<hr noshade color=black size=1></b>");
        tb.setAccName("<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
        if (totalOpeningBalance.compareTo(BigDecimal.ZERO) > 0)
            tb.setOpeningBal("<hr noshade color=black size=1><b>" + numberToString(totalOpeningBalance.toString()).toString()
                    + " Dr<hr noshade color=black size=1></b>");
        else if (totalOpeningBalance.compareTo(BigDecimal.ZERO) < 0)
        {
            totalOpeningBalance = totalOpeningBalance.multiply(new BigDecimal(-1));
            tb.setOpeningBal("<hr noshade color=black size=1><b>" + numberToString(totalOpeningBalance.toString()).toString()
                    + " Cr<hr noshade color=black size=1></b>");
        } else
            tb.setOpeningBal("<hr noshade color=black size=1><b>" + numberToString(totalOpeningBalance.toString()).toString()
                    + " Dr<hr noshade color=black size=1></b>");
        if (totalClosingBalance.compareTo(BigDecimal.ZERO) > 0)
            tb.setClosingBal("<hr noshade color=black size=1><b>" + numberToString(totalClosingBalance.toString()).toString()
                    + " Dr<hr noshade color=black size=1></b>");
        else if (totalClosingBalance.compareTo(BigDecimal.ZERO) < 0)
        {
            totalClosingBalance = totalClosingBalance.multiply(new BigDecimal(-1));
            tb.setClosingBal("<hr noshade color=black size=1><b>" + numberToString(totalClosingBalance.toString()).toString()
                    + " Cr<hr noshade color=black size=1></b>");
        } else
            tb.setClosingBal("<hr noshade color=black size=1><b>" + numberToString(totalClosingBalance.toString()).toString()
                    + " Dr<hr noshade color=black size=1></b>");
        tb.setDebit("<hr noshade color=black size=1><b>" + numberToString(totalDebitAmount.toString()).toString()
                + "<hr noshade color=black size=1></b>");
        tb.setCredit("<hr noshade color=black size=1><b>" + numberToString(totalCreditAmount.toString()).toString()
                + "<hr noshade color=black size=1></b>");
        al.add(tb);
    }
}
