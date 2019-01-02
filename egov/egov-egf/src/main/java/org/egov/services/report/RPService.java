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
package org.egov.services.report;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.egf.model.Statement;
import org.egov.egf.model.StatementResultObject;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.*;

public class RPService extends ScheduleService {
    final static Logger LOGGER = Logger.getLogger(RPService.class);
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    public List<Object> getTransactionType(final String scheduleNo) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getReceiptScheduleNoAndName............");
        StringBuffer query = new StringBuffer();
        query = query.append("select transaction_type from egf_rpreport_schedulemaster where schedule_no=:scheduleNo ");
        final List<Object> result = persistenceService.getSession().createNativeQuery(query.toString())
                .setParameter("scheduleNo", scheduleNo, StringType.INSTANCE).list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished getReceiptScheduleNoAndName..........." + query.toString());

        return result;
    }

    public List<StatementResultObject> getScheduleNoAndName() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getReceiptScheduleNoAndName............");
        StringBuffer query = new StringBuffer();
        query = query.append("select m.schedule_no as scheduleNumber, m.schedule_name as scheduleName,m.transaction_type as type")
                .append(" from egf_rpreport_schedulemaster m where is_subschedule=0 order by m.transaction_type desc,m.id ");

        final Query queryObj = persistenceService.getSession().createNativeQuery(query.toString()).addScalar("scheduleNumber")
                .addScalar("scheduleName").addScalar("type")
                .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));

        final List<StatementResultObject> result = queryObj.list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished getReceiptScheduleNoAndName..........." + query.toString());

        return result;
    }

    public Map<String, Map<String, Object>> getConditionalQuery(final CFinancialYear finId, final Statement statement) {
        final StringBuffer query = new StringBuffer();
        final Map<String, Map<String, Object>> map = new HashMap<>();
        final Map<String, Object> params = new HashMap<>();
        if (statement.getPeriod().equals("Yearly")) {
            query.append(" and vh.voucherdate between :voucherFromDate And :voucherToDate");
            params.put("voucherFromDate", getFormattedDate(finId.getStartingDate()));
            params.put("voucherToDate", getFormattedDate(finId.getEndingDate()));
        } else if (statement.getPeriod().equals("Date Range")) {
            query.append(" and vh.voucherdate between :voucherFromDate And :voucherToDate");
            params.put("voucherFromDate", getFormattedDate(statement.getFromDate()));
            params.put("voucherToDate", getFormattedDate(statement.getToDate()));
        }
        if (statement.getFund() != null && statement.getFund().getId() != null && statement.getFund().getId() != 0) {
            query.append(" AND rpmap.is_consolidated = 0 and rpmap.fund_code = :fundCode");
            params.put("fundCode", statement.getFund().getCode());
        } else
            query.append("AND rpmap.is_consolidated = 1 ");

        map.put(query.toString(), params);
        return map;
    }

    public List<StatementResultObject> getData(final CFinancialYear finId, final Statement statement) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getData............");
        StringBuffer queryNG = new StringBuffer();
        StringBuffer queryG = new StringBuffer();
        String amountNG = "";
        String amountG = "";
        final Map.Entry<String, Map<String, Object>> entry = getConditionalQuery(finId, statement).entrySet().iterator().next();
        final String SelConditionQuery = entry.getKey();
        final Map<String, Object> params = entry.getValue();
        final Map<String, Object> amountNgParams = new HashMap<>();
        final Map<String, Object> amountGParams = new HashMap<>();

        if (statement.getCurrencyInAmount().equals(new BigDecimal(1))) {
            amountNG = "case when rpm.transaction_type = 'R' then SUM(gl.creditamount) else SUM(gl.debitamount) end  AS amount";
            amountG = "SUM(gl.creditamount)  AS amount";
        } else {
            amountNG = new StringBuilder("case when rpm.transaction_type  = 'R' then SUM(round(gl.creditamount/:currencyInAmt")
                    .append(",0)) else SUM(round(gl.debitamount/currencyInAmt,0)) end  AS amount").toString();
            amountNgParams.put("currencyInAmt", statement.getCurrencyInAmount());
            amountG = "SUM(round(gl.creditamount/:currencyInAmt,0))  AS amount";
            amountGParams.put("currencyInAmt", statement.getCurrencyInAmount());
        }

        queryNG = queryNG.append("SELECT rpm.schedule_no as scheduleNumber, ").append(amountNG).append(" , rpm.transaction_type as type  ")
                .append(" ,rpm.schedule_name as scheduleName FROM egf_rpreport_schedulemaster rpm, egf_rpreport_schedulemapping rpmap,")
                .append("  voucherheader vh, generalledger gl, fiscalperiod p, financialyear f")
                .append(" WHERE rpm.id     = rpmap.rpscheduleid AND vh.id        = gl.voucherheaderid AND rpmap.glcode = gl.glcode")
                .append(" and p.id = vh.fiscalperiodid and f.id = p.financialyearid and vh.status <> 4 and f.id = :finId")
                .append(SelConditionQuery).append(" and vh.name <> 'JVGeneral' group by rpm.schedule_no,rpm.transaction_type,rpm.schedule_name");

        final Query queryObjNG = persistenceService.getSession().createNativeQuery(queryNG.toString()).addScalar("scheduleNumber")
                .addScalar("scheduleName")
                .addScalar("amount").addScalar("type")
                .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));

        params.entrySet().forEach(rec -> queryObjNG.setParameter(rec.getKey(), rec.getValue()));
        amountNgParams.entrySet().forEach(rec -> queryObjNG.setParameter(rec.getKey(), rec.getValue()));
        queryObjNG.setParameter("finId", finId.getId(), LongType.INSTANCE);

        final List<StatementResultObject> resultNG = queryObjNG.list();

        queryG = queryG.append("SELECT rpm.schedule_no as scheduleNumber, ").append(amountG).append(" , rpm.transaction_type as type  ")
                .append(" ,rpm.schedule_name as scheduleName FROM egf_rpreport_schedulemaster rpm, egf_rpreport_schedulemapping rpmap,")
                .append("  voucherheader vh,  generalledger gl, fiscalperiod p, financialyear f,  egf_instrumentheader ih,  egf_instrumentvoucher iv,  egw_status s")
                .append(" WHERE rpm.id     = rpmap.rpscheduleid AND vh.id        = gl.voucherheaderid AND rpmap.glcode = gl.glcode and p.id = vh.fiscalperiodid")
                .append(" and f.id = p.financialyearid  AND ih.id        = iv.instrumentheaderid  AND ih.id_status = s.id and vh.status <> 4  and vh.type = 'Journal Voucher' ")
                .append(" and iv.voucherheaderid = vh.id  and s.moduletype = 'Instrument'  and s.description in ('Deposited','Reconciled') and f.id = :finId")
                .append(SelConditionQuery).append(" and vh.name = 'JVGeneral' group by rpm.schedule_no,rpm.transaction_type,rpm.schedule_name");

        final Query queryObjG = persistenceService.getSession().createNativeQuery(queryG.toString()).addScalar("scheduleNumber")
                .addScalar("scheduleName")
                .addScalar("amount").addScalar("type")
                .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));

        params.entrySet().forEach(rec -> queryObjG.setParameter(rec.getKey(), rec.getValue()));
        amountGParams.entrySet().forEach(rec -> queryObjG.setParameter(rec.getKey(), rec.getValue()));
        queryObjG.setParameter("finId", finId.getId(), LongType.INSTANCE);

        final List<StatementResultObject> resultG = queryObjG.list();

        final List<StatementResultObject> finalResult = new ArrayList<StatementResultObject>();
        for (final StatementResultObject entryNG : resultG) {
            boolean found = false;
            inner:
            for (final StatementResultObject entryG : resultNG)
                if (entryNG.getScheduleNumber().equals(entryG.getScheduleNumber())
                        && entryNG.getGlCode().equals(entryG.getGlCode())) {
                    entryG.setAmount(entryNG.getAmount().add(entryG.getAmount()));
                    found = true;
                    break inner;
                }
            if (found == false)
                if (entryNG != null)
                    finalResult.add(entryNG);
        }
        resultNG.addAll(finalResult);
        return resultNG;
    }

    public List<StatementResultObject> getConsolidatedResult(final CFinancialYear finId, final Statement statement) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getConsolidatedResult............");
        StringBuffer queryNG = new StringBuffer();
        StringBuffer queryG = new StringBuffer();
        String amountNG = "";
        String amountG = "";
        final Map.Entry<String, Map<String, Object>> entry = getConditionalQuery(finId, statement).entrySet().iterator().next();
        final String SelConditionQuery = entry.getKey();
        final Map<String, Object> params = entry.getValue();
        final Map<String, Object> amountNgParams = new HashMap<>();
        final Map<String, Object> amountGParams = new HashMap<>();

        if (statement.getCurrencyInAmount().equals(new BigDecimal(1))) {
            amountNG = "case when rpm.transaction_type = 'R' then SUM(gl.creditamount) else SUM(gl.debitamount) end  AS amount";
            amountG = "SUM(gl.creditamount)  AS amount";
        } else {
            amountNG = "case when rpm.transaction_type = 'R' then SUM(round(gl.creditamount/:currencyInAmt,0))  else SUM(round(gl.debitamount/:currencyInAmt,0))  AS amount";
            amountNgParams.put("currencyInAmt", statement.getCurrencyInAmount());
            amountG = "SUM(round(gl.creditamount/:currencyInAmt,0))  AS amount";
            amountGParams.put("currencyInAmt", statement.getCurrencyInAmount());
        }

        queryNG = queryNG.append("SELECT rpm.schedule_no as scheduleNumber, ").append(amountNG).append(" ,rpmap.fund_Code as fundCode, ")
                .append(" rpm.schedule_name as scheduleName, rpm.transaction_type as type FROM egf_rpreport_schedulemaster rpm,")
                .append("  egf_rpreport_schedulemapping rpmap,  voucherheader vh,  generalledger gl, fiscalperiod p, financialyear f")
                .append(" WHERE rpm.id     = rpmap.rpscheduleid AND vh.id        = gl.voucherheaderid AND rpmap.glcode = gl.glcode and p.id = vh.fiscalperiodid")
                .append(" and f.id = p.financialyearid and vh.status <> 4 and f.id = :findId")
                .append(SelConditionQuery).append(" and vh.name <> 'JVGeneral' group by rpm.schedule_no,rpmap.fund_Code,rpm.schedule_name,rpm.transaction_type");

        final Query queryObjNG = persistenceService.getSession().createNativeQuery(queryNG.toString()).addScalar("scheduleNumber")
                .addScalar("amount")
                .addScalar("fundCode").addScalar("scheduleName").addScalar("type")
                .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));

        params.entrySet().forEach(rec -> queryObjNG.setParameter(rec.getKey(), rec.getValue()));
        amountNgParams.entrySet().forEach(rec -> queryObjNG.setParameter(rec.getKey(), rec.getValue()));
        queryObjNG.setParameter("finId", finId.getId(), LongType.INSTANCE);

        final List<StatementResultObject> resultNG = queryObjNG.list();

        queryG = queryG.append("SELECT rpm.schedule_no as scheduleNumber, ").append(amountG).append(" ,rpmap.fund_Code as fundCode, ")
                .append(" rpm.schedule_name as scheduleName, rpm.transaction_type as type FROM egf_rpreport_schedulemaster rpm,  egf_rpreport_schedulemapping rpmap,  voucherheader vh,")
                .append("  generalledger gl, fiscalperiod p, financialyear f,  egf_instrumentheader ih,  egf_instrumentvoucher iv,  egw_status s")
                .append(" WHERE rpm.id     = rpmap.rpscheduleid AND vh.id        = gl.voucherheaderid AND rpmap.glcode = gl.glcode and p.id = vh.fiscalperiodid")
                .append(" and f.id = p.financialyearid AND ih.id        = iv.instrumentheaderid  AND ih.id_status = s.id and vh.status <> 4 and vh.type = 'Journal Voucher' ")
                .append(" and iv.voucherheaderid = vh.id  and s.moduletype = 'Instrument'  and s.description in ('Deposited','Reconciled') and f.id = :finId")
                .append(SelConditionQuery).append(" and vh.name = 'JVGeneral' group by rpm.schedule_no,rpmap.fund_Code,rpm.schedule_name,rpm.transaction_type");

        final Query queryObjG = persistenceService.getSession().createNativeQuery(queryG.toString()).addScalar("scheduleNumber")
                .addScalar("amount")
                .addScalar("fundCode").addScalar("scheduleName").addScalar("type")
                .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));

        params.entrySet().forEach(rec -> queryObjG.setParameter(rec.getKey(), rec.getValue()));
        amountGParams.entrySet().forEach(rec -> queryObjG.setParameter(rec.getKey(), rec.getValue()));
        queryObjG.setParameter("finId", finId.getId(), LongType.INSTANCE);

        final List<StatementResultObject> resultG = queryObjG.list();

        final List<StatementResultObject> finalResult = new ArrayList<StatementResultObject>();
        for (final StatementResultObject entryNG : resultG) {
            boolean found = false;
            inner:
            for (final StatementResultObject entryG : resultNG)
                if (entryNG.getScheduleNumber().equals(entryG.getScheduleNumber())
                        && entryNG.getGlCode().equals(entryG.getGlCode())) {
                    entryG.setAmount(entryNG.getAmount().add(entryG.getAmount()));
                    found = true;
                    break inner;
                }
            if (found == false)
                if (entryNG != null)
                    finalResult.add(entryNG);
        }
        resultNG.addAll(finalResult);

        return resultNG;
    }

    public List<Object[]> getSubScheduleMaster(final String scheduleNo, final String fundCode) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getSubScheduleMaster............");
        StringBuffer query = new StringBuffer();

        query = query.append("SELECT rpm1.schedule_no, rpm1.schedule_name, rpm1.id ")
                .append("FROM egf_rpreport_schedulemaster rpm1, egf_rpreport_schedulemaster rpm2, egf_rpreport_schedulemapping rpmap ")
                .append("WHERE rpm1.id             = rpmap.subschedule_id and rpm2.id = rpmap.rpscheduleid AND rpmap.fund_code = :fundCode")
                .append("and rpm2.schedule_no = :scheduleNo AND rpmap.is_consolidated=0 group by rpm1.schedule_no, rpm1.schedule_name, rpm1.id order by rpm1.id");

        final List<Object[]> result = persistenceService.getSession().createNativeQuery(query.toString())
                .setParameter("fundCode", fundCode, StringType.INSTANCE)
                .setParameter("scheduleNo", scheduleNo, StringType.INSTANCE)
                .list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished getSubScheduleMaster..........." + query.toString());

        return result;
    }

    public List<Object[]> getSubScheduleMasterConsolidated(final String scheduleNo) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getSubScheduleMaster............");
        StringBuffer query = new StringBuffer();

        query = query.append("SELECT rpm1.schedule_no, rpm1.schedule_name, rpm1.id ")
                .append("FROM egf_rpreport_schedulemaster rpm1, egf_rpreport_schedulemaster rpm2, egf_rpreport_schedulemapping rpmap ")
                .append("WHERE rpm1.id             = rpmap.subschedule_id and rpm2.id = rpmap.rpscheduleid ")
                .append("and rpm2.schedule_no = :scheduleNo AND rpmap.is_consolidated=1 group by rpm1.schedule_no, rpm1.schedule_name, rpm1.id order by rpm1.id");

        final List<Object[]> result = persistenceService.getSession().createNativeQuery(query.toString())
                .setParameter("scheduleNo", scheduleNo, StringType.INSTANCE)
                .list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished getSubScheduleMaster..........." + query.toString());

        return result;
    }

    public List<Object[]> getfundMaster() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getfundMaster............");
        StringBuffer query = new StringBuffer();

        query = query.append("select f.code, f.name from fund f order by code");

        final List<Object[]> result = persistenceService.getSession().createNativeQuery(query.toString()).list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished getfundMaster..........." + query.toString());

        return result;
    }

    public List<Object[]> getDetailGlcodeNonSubSchedule(final String scheduleNo, final String fundCode) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getReceiptScheduleNoAndName............");
        StringBuffer query = new StringBuffer();

        query = query
                .append("select rpmap.glcode, coa.name from egf_rpreport_schedulemaster rpm, egf_rpreport_schedulemapping rpmap,")
                .append("chartofaccounts COA where rpm.id = rpmap.rpscheduleid and rpmap.fund_code=:fundCode  and")
                .append(" coa.glcode=rpmap.glcode and  rpmap.is_consolidated=0 and rpm.schedule_no=:scheduleNo and rpmap.subschedule_id is null  order by COA.glcode");

        final List<Object[]> result = persistenceService.getSession().createNativeQuery(query.toString())
                .setParameter("fundCode", fundCode, StringType.INSTANCE)
                .setParameter("scheduleNo", scheduleNo, StringType.INSTANCE)
                .list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished getReceiptScheduleNoAndName..........." + query.toString());

        return result;
    }

    public List<Object[]> getDetailGlcodeNonSubScheduleConsolidated(final String scheduleNo) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getDetailGlcodeNonSubScheduleConsolidated............");
        StringBuffer query = new StringBuffer();

        query = query
                .append("select rpmap.glcode, coa.name from egf_rpreport_schedulemaster rpm, egf_rpreport_schedulemapping rpmap,")
                .append("chartofaccounts COA where rpm.id = rpmap.rpscheduleid  and")
                .append(" coa.glcode=rpmap.glcode and  rpmap.is_consolidated=1 and rpm.schedule_no=:scheduleNo and rpmap.subschedule_id is null  order by COA.glcode");

        final List<Object[]> result = persistenceService.getSession().createNativeQuery(query.toString())
                .setParameter("scheduleNo", scheduleNo, StringType.INSTANCE)
                .list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished getReceiptScheduleNoAndName..........." + query.toString());

        return result;
    }

    public List<Object[]> getDetailGlcodeSubSchedule(final String scheduleNo, final String fundCode) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getDetailGlcodeSubSchedule............");
        StringBuffer query = new StringBuffer();

        query = query.append("SELECT rpmap.glcode, rpmss.schedule_no ")
                .append("FROM egf_rpreport_schedulemaster rpm, egf_rpreport_schedulemaster rpmss, egf_rpreport_schedulemapping rpmap ")
                .append("WHERE rpm.id             = rpmap.rpscheduleid and rpmss.id = rpmap.subschedule_id AND rpmap.fund_code =:fundCode ")
                .append("AND rpmap.is_consolidated=0 AND rpm.schedule_no = :scheduleNo ORDER BY rpmap.glcode");

        final List<Object[]> result = persistenceService.getSession().createNativeQuery(query.toString())
                .setParameter("fundCode", fundCode, StringType.INSTANCE)
                .setParameter("scheduleNo", scheduleNo, StringType.INSTANCE)
                .list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished getDetailGlcodeSubSchedule..........." + query.toString());

        return result;
    }

    public List<Object[]> getDetailGlcodeSubScheduleConsolidated(final String scheduleNo) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getDetailGlcodeSubSchedule............");
        StringBuffer query = new StringBuffer();

        query = query.append("SELECT rpmap.glcode, rpmss.schedule_no ")
                .append("FROM egf_rpreport_schedulemaster rpm, egf_rpreport_schedulemaster rpmss, egf_rpreport_schedulemapping rpmap ")
                .append("WHERE rpm.id             = rpmap.rpscheduleid and rpmss.id = rpmap.subschedule_id ")
                .append("AND rpmap.is_consolidated=1 AND rpm.schedule_no  =:scheduleNo ORDER BY rpmap.glcode");

        final List<Object[]> result = persistenceService.getSession().createNativeQuery(query.toString())
                .setParameter("scheduleNo", scheduleNo, StringType.INSTANCE)
                .list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished getDetailGlcodeSubSchedule..........." + query.toString());

        return result;
    }

    public List<Object[]> getGlcodeForConsolidatedReport(final String scheduleNo) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getDetailGlcodeForConsolidatedReport............");
        StringBuffer query = new StringBuffer();

        query = query
                .append("select rpmap.glcode, coa.name from egf_rpreport_schedulemaster rpm, egf_rpreport_schedulemapping rpmap,")
                .append("chartofaccounts COA where rpm.id = rpmap.rpscheduleid  and")
                .append(" coa.glcode=rpmap.glcode and  rpmap.is_consolidated=1 and rpm.schedule_no=:scheduleNo order by COA.glcode");

        final List<Object[]> result = persistenceService.getSession().createNativeQuery(query.toString())
                .setParameter("scheduleNo", scheduleNo, StringType.INSTANCE)
                .list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished getDetailGlcodeForConsolidatedReport..........." + query.toString());

        return result;
    }

    public List<StatementResultObject> getDetailData(final CFinancialYear finId, final String transactionType,
                                                     final String scheduleNo,
                                                     final Statement statement) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getData.....Testing.......");
        StringBuffer queryNG = new StringBuffer();
        StringBuffer queryG = new StringBuffer();
        String amountNG = "";
        String amountG = "";

        final Map.Entry<String, Map<String, Object>> queryEntry = getDateRangeQuery(finId, statement).entrySet().iterator().next();
        final Map<String, Object> queryParams = queryEntry.getValue();
        final String dateCondition = queryEntry.getKey();
        final Map<String, Object> amountNgParams = new HashMap<>();
        final Map<String, Object> amountGParams = new HashMap<>();

        if (statement.getCurrencyInAmount().equals(new BigDecimal(1))) {
            amountNG = "case when rpm.transaction_type = 'R' then SUM(gl.creditamount) else SUM(gl.debitamount) end   AS amount";
            amountG = "SUM(gl.creditamount)  AS amount";
        } else {
            amountNG = "case when rpm.transaction_type = 'R' then SUM(round(gl.creditamount/:currencyAmt,0)) else SUM(round(gl.debitamount/:currencyAmt,0))  AS amount";
            amountNgParams.put("currencyAmt", statement.getCurrencyInAmount());
            amountG = "SUM(round(gl.creditamount/:currencyAmt,0))  AS amount";
            amountGParams.put("currencyAmt", statement.getCurrencyInAmount());
        }

        queryNG = queryNG
                .append("SELECT rpmap.glcode as glCode, ").append(amountNG)
                .append(" , rpmap.fund_Code as fundCode, rpm.transaction_type as type, rpm.schedule_no as scheduleNumber ")
                .append(" FROM egf_rpreport_schedulemaster rpm,  egf_rpreport_schedulemapping rpmap,  voucherheader vh,  generalledger gl, fiscalperiod p, financialyear f")
                .append(" WHERE rpm.id     = rpmap.rpscheduleid AND vh.id        = gl.voucherheaderid AND rpmap.glcode = gl.glcode and p.id = vh.fiscalperiodid")
                .append(" and f.id = p.financialyearid and vh.status <> 4 and f.id = :fundId")
                // check if this trasaction type can be joined with rpmap type
                .append(" and rpm.transaction_type=:transactionType and vh.name <> 'JVGeneral'")
                .append(dateCondition).append(" and rpm.schedule_no=:scheduleNo")
                .append(" group by rpmap.glcode, rpmap.fund_Code ,rpm.transaction_type, rpm.schedule_no ORDER BY rpm.schedule_no, rpmap.glcode");

        final Query detailQueryNG = persistenceService.getSession().createNativeQuery(queryNG.toString()).addScalar("glCode")
                .addScalar("amount").addScalar("fundCode").addScalar("type").addScalar("scheduleNumber")
                .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));

        queryParams.entrySet().forEach(entry -> detailQueryNG.setParameter(entry.getKey(), entry.getValue()));
        amountNgParams.entrySet().forEach(entry -> detailQueryNG.setParameter(entry.getKey(), entry.getValue()));
        detailQueryNG.setParameter("fundId", finId.getId(), LongType.INSTANCE)
                .setParameter("transactionType", transactionType, StringType.INSTANCE)
                .setParameter("scheduleNo", scheduleNo, StringType.INSTANCE);

        final List<StatementResultObject> resultNG = detailQueryNG.list();

        queryG = queryG
                .append("SELECT rpmap.glcode as glCode, ").append(amountG)
                .append(" , rpmap.fund_Code as fundCode, rpm.transaction_type as type, rpm.schedule_no as scheduleNumber ")
                .append(" FROM egf_rpreport_schedulemaster rpm,  egf_rpreport_schedulemapping rpmap,  voucherheader vh,  generalledger gl, fiscalperiod p,")
                .append(" financialyear f,  egf_instrumentheader ih,  egf_instrumentvoucher iv,  egw_status s")
                .append(" WHERE rpm.id     = rpmap.rpscheduleid AND vh.id        = gl.voucherheaderid AND rpmap.glcode = gl.glcode and p.id = vh.fiscalperiodid")
                .append(" and f.id = p.financialyearid AND ih.id        = iv.instrumentheaderid  AND ih.id_status = s.id and vh.status <> 4")
                .append(" and vh.type = 'Journal Voucher'  and iv.voucherheaderid = vh.id  and s.moduletype = 'Instrument'  and s.description in ('Deposited','Reconciled')")
                .append(" and f.id = :fundId")
                // check if this trasaction type can be joined with rpmap type
                .append(" and rpm.transaction_type=:transactionType and vh.name = 'JVGeneral'")
                .append(dateCondition).append(" and rpm.schedule_no=:scheduleNo")
                .append(" group by rpmap.glcode, rpmap.fund_Code ,rpm.transaction_type, rpm.schedule_no ORDER BY  rpm.schedule_no ,rpmap.glcode");

        final Query detailQueryG = persistenceService.getSession().createNativeQuery(queryG.toString()).addScalar("glCode")
                .addScalar("amount").addScalar("fundCode").addScalar("type").addScalar("scheduleNumber")
                .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));

        queryParams.entrySet().forEach(entry -> detailQueryG.setParameter(entry.getKey(), entry.getValue()));
        amountGParams.entrySet().forEach(entry -> detailQueryG.setParameter(entry.getKey(), entry.getValue()));
        detailQueryG.setParameter("fundId", finId.getId(), LongType.INSTANCE)
                .setParameter("transactionType", transactionType, StringType.INSTANCE)
                .setParameter("scheduleNo", scheduleNo, StringType.INSTANCE);

        final List<StatementResultObject> resultG = detailQueryG.list();

        final List<StatementResultObject> finalResult = new ArrayList<StatementResultObject>();
        for (final StatementResultObject entryNG : resultG) {
            boolean found = false;
            inner:
            for (final StatementResultObject entryG : resultNG)
                if (entryNG.getScheduleNumber().equals(entryG.getScheduleNumber())
                        && entryNG.getGlCode().equals(entryG.getGlCode())) {
                    entryG.setAmount(entryNG.getAmount().add(entryG.getAmount()));
                    found = true;
                    break inner;
                }
            if (found == false)
                if (entryNG != null)
                    finalResult.add(entryNG);
        }
        resultNG.addAll(finalResult);

        return resultNG;
    }

    public List<StatementResultObject> getCurrentYearConsolidatedReportForGlcode(final CFinancialYear finId,
                                                                                 final String transactionType,
                                                                                 final String scheduleNo, final Statement statement) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting getData............");
        StringBuffer queryNG = new StringBuffer();
        StringBuffer queryG = new StringBuffer();
        String amountNG = "";
        String amountG = "";

        final Map.Entry<String, Map<String, Object>> queryEntry = getDateRangeQuery(finId, statement).entrySet().iterator().next();
        final Map<String, Object> queryParams = queryEntry.getValue();
        final String dateCondition = queryEntry.getKey();
        final Map<String, Object> amountNgParams = new HashMap<>();
        final Map<String, Object> amountGParams = new HashMap<>();

        if (statement.getCurrencyInAmount().equals(new BigDecimal(1))) {
            amountNG = "case when rpm.transaction_type = 'R' then SUM(gl.creditamount) else SUM(gl.debitamount) end  AS amount";
            amountG = "SUM(gl.creditamount)  AS amount";
        } else {
            amountNG = "case when rpm.transaction_type = 'R' then SUM(round(gl.creditamount/currencyAmt,0)) else SUM(round(gl.debitamount/currencyAmt,0))  AS amount";
            amountNgParams.put("currencyAmt", statement.getCurrencyInAmount());
            amountG = "SUM(round(gl.creditamount/currencyAmt,0))  AS amount";
            amountGParams.put("currencyAmt", statement.getCurrencyInAmount());
        }

        queryNG = queryNG.append("SELECT rpmap.glcode as glCode, ").append(amountNG)
                .append(" , rpmap.fund_Code as fundCode ,rpm.transaction_type as type, rpm.schedule_no as scheduleNumber")
                .append(" FROM egf_rpreport_schedulemaster rpm,  egf_rpreport_schedulemapping rpmap,  voucherheader vh,  generalledger gl, fiscalperiod p, financialyear f")
                .append(" WHERE rpm.id     = rpmap.rpscheduleid AND vh.id        = gl.voucherheaderid AND rpmap.glcode = gl.glcode and p.id = vh.fiscalperiodid and f.id = p.financialyearid")
                .append(" and vh.status <> 4 and f.id = :fundId and rpm.transaction_type=:transactionType  and vh.name <> 'JVGeneral'")
                .append(dateCondition).append(" group by rpmap.glcode, rpmap.fund_Code ,rpm.transaction_type, rpm.schedule_no");

        final Query detailQueryNG = persistenceService.getSession().createNativeQuery(queryNG.toString()).addScalar("glCode")
                .addScalar("amount")
                .addScalar("fundCode").addScalar("type").addScalar("scheduleNumber")
                .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));

        queryParams.entrySet().forEach(entry -> detailQueryNG.setParameter(entry.getKey(), entry.getValue()));
        amountNgParams.entrySet().forEach(entry -> detailQueryNG.setParameter(entry.getKey(), entry.getValue()));
        detailQueryNG.setParameter("fundId", finId.getId(), LongType.INSTANCE)
                .setParameter("transactionType", transactionType, StringType.INSTANCE);

        final List<StatementResultObject> resultNG = detailQueryNG.list();

        queryG = queryG.append("SELECT rpmap.glcode as glCode, ").append(amountG)
                .append(" , rpmap.fund_Code as fundCode ,rpm.transaction_type as type, rpm.schedule_no as scheduleNumber")
                .append(" FROM egf_rpreport_schedulemaster rpm,  egf_rpreport_schedulemapping rpmap,  voucherheader vh,  generalledger gl, fiscalperiod p,")
                .append(" financialyear f,  egf_instrumentheader ih,  egf_instrumentvoucher iv,  egw_status s")
                .append(" WHERE rpm.id     = rpmap.rpscheduleid AND vh.id        = gl.voucherheaderid AND rpmap.glcode = gl.glcode and p.id = vh.fiscalperiodid")
                .append(" and f.id = p.financialyearid AND ih.id        = iv.instrumentheaderid  AND ih.id_status = s.id and vh.status <> 4")
                .append(" and vh.type = 'Journal Voucher' and iv.voucherheaderid = vh.id  and s.moduletype = 'Instrument'  and s.description in ('Deposited','Reconciled')")
                .append(" and f.id = :fundId and rpm.transaction_type=:transactionType and vh.name = 'JVGeneral'")
                .append(dateCondition).append(" group by rpmap.glcode, rpmap.fund_Code ,rpm.transaction_type, rpm.schedule_no");

        final Query detailQueryG = persistenceService.getSession().createNativeQuery(queryG.toString()).addScalar("glCode")
                .addScalar("amount")
                .addScalar("fundCode").addScalar("type").addScalar("scheduleNumber")
                .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));

        queryParams.entrySet().forEach(entry -> detailQueryG.setParameter(entry.getKey(), entry.getValue()));
        amountGParams.entrySet().forEach(entry -> detailQueryG.setParameter(entry.getKey(), entry.getValue()));
        detailQueryG.setParameter("fundId", finId.getId(), LongType.INSTANCE)
                .setParameter("transactionType", transactionType, StringType.INSTANCE);

        final List<StatementResultObject> resultG = detailQueryG.list();

        final List<StatementResultObject> finalResult = new ArrayList<StatementResultObject>();
        for (final StatementResultObject entryNG : resultG) {
            boolean found = false;
            inner:
            for (final StatementResultObject entryG : resultNG)
                if (entryNG.getScheduleNumber().equals(entryG.getScheduleNumber())
                        && entryNG.getGlCode().equals(entryG.getGlCode())) {
                    entryG.setAmount(entryNG.getAmount().add(entryG.getAmount()));
                    found = true;
                    break inner;
                }
            if (found == false)
                if (entryNG != null)
                    finalResult.add(entryNG);
        }
        resultNG.addAll(finalResult);

        return resultNG;
    }

    public Map<String, Map<String, Object>> getDateRangeQuery(final CFinancialYear finId, final Statement statement) {
        final StringBuffer query = new StringBuffer();
        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> params = new HashMap<>();
        /*
         * if(statement.getPeriod().equals("Yearly")){
         * query.append(" and vh.voucherdate between '"+getFormattedDate(finId.getStartingDate
         * ())+"' And '"+getFormattedDate(finId.getEndingDate())+"'"); }else if(statement.getPeriod().equals("Date Range")){
         * query.
         * append(" and vh.voucherdate between '"+getFormattedDate(statement.getFromDate())+"' And '"+getFormattedDate(statement
         * .getToDate())+"'"); }
         */
        if (statement.getFund() != null && statement.getFund().getId() != null && statement.getFund().getId() != 0) {
            query.append(" AND rpmap.is_consolidated = 0 and rpmap.fund_code = :fundCode");
            params.put("fundCode", statement.getFund().getCode());
        } else
            query.append("AND rpmap.is_consolidated = 1 ");
        queryMap.put(query.toString(), params);
        return queryMap;
    }

    public Date getCurrentYearToDate(final Statement statement) {
        if ("Date Range".equalsIgnoreCase(statement.getPeriod()) && statement.getToDate() != null
                && statement.getFromDate() != null)
            return statement.getToDate();
        else
            return statement.getFinancialYear().getEndingDate();
    }

    public Date getPreviousYearToDate(final Statement statement) {
        if ("Date Range".equalsIgnoreCase(statement.getPeriod()) && statement.getToDate() != null
                && statement.getFromDate() != null)
            return getPreviousYearFor(statement.getToDate());
        else
            return getPreviousYearFor(statement.getFinancialYear().getEndingDate());
    }

    public Date getPreviousYearFor(final Date date) {
        final GregorianCalendar previousYearToDate = new GregorianCalendar();
        previousYearToDate.setTime(date);
        final int prevYear = previousYearToDate.get(Calendar.YEAR) - 1;
        previousYearToDate.set(Calendar.YEAR, prevYear);
        return previousYearToDate.getTime();
    }

}