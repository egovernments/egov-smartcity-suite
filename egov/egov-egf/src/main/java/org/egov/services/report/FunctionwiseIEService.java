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
import org.egov.egf.model.CommonReportBean;
import org.egov.egf.model.FunctionwiseIE;
import org.egov.egf.model.FunctionwiseIEEntry;
import org.egov.egf.model.ReportSearch;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FunctionwiseIEService {

    private static final Logger LOGGER = Logger.getLogger(FunctionwiseIEService.class);
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);
    protected SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Constants.LOCALE);
    @Autowired
    AppConfigValueService appConfigValuesService;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    private String capExpCodeCond = "";
    private String capExpCodesWithQuotesCond = "";

    public Map<String, Map<String, Object>> getFilterQueryVoucher(final ReportSearch reportSearch) throws ApplicationException, ParseException {
        final String excludeStatus = appConfigValuesService.getConfigValuesByModuleAndKey("finance", "statusexcludeReport")
                .get(0)
                .getValue();
        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> params = new HashMap<>();
        StringBuilder appendQry = new StringBuilder(" AND vh.voucherdate>=TO_DATE(:voucherFromDate) AND vh.voucherdate<=TO_DATE(:voucherToDate) ");
        params.put("voucherFromDate", formatter.format(sdf.parse(reportSearch.getStartDate())));
        params.put("voucherToDate", formatter.format(sdf.parse(reportSearch.getEndDate())));
        getFiltersExcludingDate(reportSearch, excludeStatus, appendQry, params);
        queryMap.put(appendQry.toString(), params);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("appendQry==" + appendQry);
        return queryMap;
    }

    public Map<String, Map<String, Object>> getFilterQueryVoucherAsOnDate(final ReportSearch reportSearch) throws ApplicationException, ParseException {

        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final String excludeStatus = appConfigValuesService.getConfigValuesByModuleAndKey("finance", "statusexcludeReport")
                .get(0)
                .getValue();
        final Map<String, Object> params = new HashMap<>();
        StringBuilder appendQry = new StringBuilder(" AND vh.voucherdate>=TO_DATE(:voucherFromDate) ");
        appendQry.append(" AND vh.voucherdate<=TO_DATE(:voucherToDate) ");
        params.put("voucherFromDate", formatter.format(reportSearch.getYearStartDate()));
        params.put("voucherToDate", formatter.format(reportSearch.getAsOnDate()));
        appendQry = getFiltersExcludingDate(reportSearch, excludeStatus, appendQry, params);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("appendQry==" + appendQry);
        queryMap.put(appendQry.toString(), params);
        return queryMap;
    }

    public Map<String, Map<String, Object>> getFilterQueryVoucherAsOnPreviousYearDate(final ReportSearch reportSearch) throws ApplicationException,
            ParseException {

        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final String excludeStatus = appConfigValuesService.getConfigValuesByModuleAndKey("finance", "statusexcludeReport")
                .get(0)
                .getValue();
        final Map<String, Object> params = new HashMap<>();
        StringBuilder appendQry = new StringBuilder(" AND vh.voucherdate>=TO_DATE(:voucherFromDate) ");
        appendQry.append(" AND vh.voucherdate<=TO_DATE(:vouucherToDate) ");
        params.put("voucherFromDate", formatter.format(reportSearch.getPreviousYearStartDate()));
        params.put("voucherToDate", formatter.format(reportSearch.getPreviousYearDate()));
        appendQry = getFiltersExcludingDate(reportSearch, excludeStatus, appendQry, params);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("appendQry==" + appendQry);
        queryMap.put(appendQry.toString(), params);
        return queryMap;
    }

    private StringBuilder getFiltersExcludingDate(final ReportSearch reportSearch,
                                                  final String excludeStatus, StringBuilder appendQry, Map<String, Object> params) {
        appendQry.append(" AND vh.status NOT IN (").append(excludeStatus).append(")");
        if (reportSearch.getFund() != null && reportSearch.getFund().getId() != null) {
            appendQry.append(" AND vh.fundid =:fundId");
            params.put("fundId", reportSearch.getFund().getId());
        }
        if (reportSearch.getFundsource() != null && reportSearch.getFundsource().getId() != null) {
            appendQry.append(" AND vh.fundsourceid =:fundSourceId");
            params.put("fundSourceId", reportSearch.getFundsource().getId());
        }
        if (reportSearch.getDepartment() != null && reportSearch.getDepartment().getId() != null) {
            appendQry.append(" AND vmis.departmentid =:deptId");
            params.put("deptId", reportSearch.getDepartment().getId());
        }
        if (reportSearch.getField() != null && reportSearch.getField().getId() != null) {
            appendQry.append(" AND vmis.divisionid =:divisionId");
            params.put("divisionId", reportSearch.getField().getId());
        }
        if (reportSearch.getScheme() != null && reportSearch.getScheme().getId() != null) {
            appendQry.append(" AND vmis.schemeid =:schemeId");
            params.put("schemeId", reportSearch.getScheme().getId());
        }
        if (reportSearch.getSubScheme() != null && reportSearch.getSubScheme().getId() != null) {
            appendQry.append(" AND vmis.subschemeid =:subSchemeId");
            params.put("subSchemeId", reportSearch.getSubScheme().getId());
        }
        if (reportSearch.getFunctionary() != null && reportSearch.getFunctionary().getId() != null) {
            appendQry.append(" AND vmis.functionaryid =:functionaryId");
            params.put("functionaryId", reportSearch.getFunctionary().getId());
        }
        return appendQry;
    }

    public Map<String, Map<String, Object>> getFilterQueryGL(final ReportSearch reportSearch) {
        final StringBuilder appendQry = new StringBuilder("");
        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> params = new HashMap<>();
        if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null) {
            appendQry.append(" AND gl.functionid = :functionId");
            params.put("functionId", reportSearch.getFunction().getId());
        }
        queryMap.put(appendQry.toString(), params);
        return queryMap;
    }

    public void getMajorCodeList(final FunctionwiseIE functionwiseIE, final ReportSearch reportSearch)
            throws ApplicationException,
            ParseException {
        final List<String> majorCodeList = new ArrayList<String>();
        final Map.Entry<String, Map<String, Object>> queryMapEntry = getFilterQueryVoucher(reportSearch).entrySet().iterator().next();
        final String filterQuery = queryMapEntry.getKey();
        final Map<String, Object> queryParams = queryMapEntry.getValue();
        final Map.Entry<String, Map<String, Object>> queryGLMapEntry = getFilterQueryGL(reportSearch).entrySet().iterator().next();
        final String queryGLString = queryGLMapEntry.getKey();
        final Map<String, Object> queryGLParams = queryGLMapEntry.getValue();
        final StringBuilder sql = new StringBuilder("select distinct SUBSTR(gl.glcode,1,")
                .append(reportSearch.getMinorCodeLen())
                .append("),coa.name from CHARTOFACCOUNTS coa,GENERALLEDGER gl")
                .append(" WHERE gl.functionid is not null and gl.voucherheaderid IN (SELECT vh.id FROM VOUCHERHEADER vh,vouchermis vmis WHERE vh.id=vmis.voucherheaderid ")
                .append(filterQuery).append(" AND coa.TYPE=:coaType AND SUBSTR(gl.glcode,1,").append(reportSearch.getMajorCodeLen()).append(")=coa.glcode) ")
                .append(queryGLString).append(" ORDER BY 1");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("sql====================" + sql);
        final Query query = persistenceService.getSession().createNativeQuery(sql.toString());
        query.setParameter("coaType", reportSearch.getIncExp(), StringType.INSTANCE);
        queryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        queryGLParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        final List<Object[]> list = query.list();
        for (final Object[] obj : list)
            majorCodeList.add(obj[0].toString() + "-" + obj[1].toString());
        functionwiseIE.setMajorCodeList(majorCodeList);
    }

    public List<String> getMinorCodeList(final ReportSearch reportSearch) throws ApplicationException, ParseException {
        final List<String> minorCodeList = new ArrayList<String>();
        final Map.Entry<String, Map<String, Object>> queryMapEntry = getFilterQueryVoucher(reportSearch).entrySet().iterator().next();
        final String filterQuery = queryMapEntry.getKey();
        final Map<String, Object> queryParams = queryMapEntry.getValue();
        final Map.Entry<String, Map<String, Object>> queryGLMapEntry = getFilterQueryGL(reportSearch).entrySet().iterator().next();
        final String queryGLString = queryGLMapEntry.getKey();
        final Map<String, Object> queryGLParams = queryGLMapEntry.getValue();
        final StringBuilder sql = new StringBuilder("select distinct SUBSTR(gl.glcode,1,")
                .append(reportSearch.getMinorCodeLen()).append("),coa.name")
                .append(" from CHARTOFACCOUNTS coa,GENERALLEDGER gl")
                .append(" WHERE gl.functionid is not null and gl.voucherheaderid IN (SELECT vh.id FROM VOUCHERHEADER vh,vouchermis vmis WHERE vh.id=vmis.voucherheaderid ")
                .append(filterQuery).append(" AND coa.TYPE=:coaType AND SUBSTR(gl.glcode,1,").append(reportSearch.getMinorCodeLen()).append(")=coa.glcode) ")
                .append(queryGLString).append(" ORDER BY 1");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("sql====================" + sql);
        final Query query = persistenceService.getSession().createNativeQuery(sql.toString());
        query.setParameter("coaType", reportSearch.getIncExp(), StringType.INSTANCE);
        queryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        queryGLParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        final List<Object[]> list = query.list();
        for (final Object[] obj : list)
            minorCodeList.add(obj[0].toString() + "-" + obj[1].toString());
        return minorCodeList;
    }

    /**
     * @param reportSearch
     * @return
     * @throws ApplicationException
     * @throws ParseException       for Main report getMajor Code and Minor Code for Sub Report get only minor code
     */
    public List<CommonReportBean> getMinorAndMajorCodeList(final ReportSearch reportSearch) throws ApplicationException,
            ParseException {
        StringBuilder sql = new StringBuilder("");
        if (reportSearch.getByDepartment() && reportSearch.getByDetailCode())
            sql.append(" select coa.glcode as accCode,coa.name as name,null as schedule,null as FIEscheduleId,0 as isMajor from Chartofaccounts coa")
                    .append(" where  coa.type=:type and length(coa.glcode)=:glcodeLength and coa.glcode like :glcode and classification=4 and isActiveForPosting=true order by 1 ");
        else if (reportSearch.getByDepartment())
            sql.append(" select coa.glcode as accCode,coa.name as name,mp.schedule as schedule,mp.id as FIEscheduleId,0 as isMajor from Chartofaccounts coa,Schedulemapping mp")
                    .append(" where coa.FIEscheduleId=mp.id and coa.type=:type and length(coa.glcode)=:glcodeLength and coa.FIEscheduleId=:FIEscheduleId  order by 1");
        else
            sql.append(" select coa.glcode as accCode,coa.name as name,mp.schedule as schedule,mp.id as FIEscheduleId, 0 as isMajor from Chartofaccounts coa,Schedulemapping mp")
                    .append(" where coa.FIEscheduleId=mp.id and coa.type=:type and length(coa.glcode)=:glcodeLength")
                    .append(" Union ")
                    .append(" select coa.glcode as accCode,coa.name as name,null as schedule,null as FIEscheduleId ,1 as isMajor from Chartofaccounts coa")
                    .append(" where  coa.type=:type and length(coa.glcode)=:majorCodeLength order by 1");
        final Query query = persistenceService.getSession().createNativeQuery(sql.toString())
                .addScalar("accCode", StringType.INSTANCE)
                .addScalar("name", StringType.INSTANCE)
                .addScalar("schedule", StringType.INSTANCE)
                .addScalar("FIEscheduleId", LongType.INSTANCE)
                .addScalar("isMajor", BooleanType.INSTANCE)
                .setParameter("type", reportSearch.getIncExp(), StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));
        if (reportSearch.getByDetailCode()) {
            query.setParameter("glcode", reportSearch.getGlcode() + "%", StringType.INSTANCE)
                    .setParameter("glcodeLength", reportSearch.getMinorCodeLen(), IntegerType.INSTANCE);
        } else if (reportSearch.getByDepartment()) {
            query.setParameter("FIEscheduleId", reportSearch.getFIEscheduleId(), LongType.INSTANCE)
                    .setParameter("glcodeLength", reportSearch.getMinorCodeLen(), IntegerType.INSTANCE);
        } else {
            query.setParameter("glcodeLength", reportSearch.getMinorCodeLen(), IntegerType.INSTANCE)
                    .setParameter("majorCodeLength", reportSearch.getMajorCodeLen(), IntegerType.INSTANCE);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("----------------" + sql);
        return query.list();

    }

    /**
     * @param reportSearch
     * @return
     * @throws ApplicationException
     * @throws ParseException       for Main report getMajor Code and Minor Code for Sub Report get only minor code
     */
    public List<CommonReportBean> getMinorAndMajorCodeListForCapitalExp(final ReportSearch reportSearch)
            throws ApplicationException,
            ParseException {
        StringBuilder sql = new StringBuilder("");
        if (reportSearch.getByDepartment() && reportSearch.getByDetailCode())
            sql.append(" select coa.glcode as accCode,coa.name as name,null as schedule,null as FIEscheduleId,0 as isMajor from Chartofaccounts coa")
                    .append(" where  coa.type=:type and length(coa.glcode)=:minorCodeLength and coa.glcode like :glcode and classification=4 and isActiveForPosting=true order by 1 ");
        else if (reportSearch.getByDepartment())
            sql.append(" select coa.glcode as accCode,coa.name as name,mp.schedule as schedule,mp.id as FIEscheduleId,0 as isMajor from Chartofaccounts coa,Schedulemapping mp")
                    .append(" where coa.FIEscheduleId=mp.id and coa.type=:type and length(coa.glcode)=:minorCodeLength and coa.FIEscheduleId=:FIEscheduleId  order by 1");
        else
            sql.append(" select coa.glcode as accCode,coa.name as name,mp.schedule as schedule,mp.id as FIEscheduleId, 0 as isMajor from Chartofaccounts coa,Schedulemapping mp")
                    .append(" where coa.FIEscheduleId=mp.id and coa.type=:type and length(coa.glcode)=:minorCodeLength")
                    .append(" Union ")
                    .append(" select coa.glcode as accCode,coa.name as name,null as schedule,null as FIEscheduleId ,1 as isMajor from Chartofaccounts coa")
                    .append(" where  coa.type=:type and length(coa.glcode)=:majorCodeLength and coa.glcode in (").append(capExpCodesWithQuotesCond).append(")").append(" order by 1");
        final Query query = persistenceService.getSession().createNativeQuery(sql.toString())
                .addScalar("accCode", StringType.INSTANCE)
                .addScalar("name", StringType.INSTANCE)
                .addScalar("schedule", StringType.INSTANCE)
                .addScalar("FIEscheduleId", LongType.INSTANCE)
                .addScalar("isMajor", BooleanType.INSTANCE)
                .setString("type", "A")
                .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));
        if (reportSearch.getByDetailCode()) {
            query.setParameter("glcode", reportSearch.getGlcode() + "%", StringType.INSTANCE)
                    .setParameter("minorCodeLength", reportSearch.getMinorCodeLen(), IntegerType.INSTANCE);
        } else if (reportSearch.getByDepartment()) {
            query.setParameter("FIEscheduleId", reportSearch.getFIEscheduleId(), LongType.INSTANCE)
                    .setParameter("minorCodeLength", reportSearch.getMinorCodeLen(), IntegerType.INSTANCE);
        } else {
            query.setParameter("majorCodeLength", reportSearch.getMajorCodeLen(), IntegerType.INSTANCE)
                    .setParameter("minorCodeLength", reportSearch.getMinorCodeLen(), IntegerType.INSTANCE);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("----------------" + sql);
        return query.list();

    }

    public List<CommonReportBean> getIncomeMinorAndMajorCodeList(final ReportSearch reportSearch) throws ApplicationException,
            ParseException {
        StringBuilder sql = new StringBuilder("");
        if (reportSearch.getByDepartment() && reportSearch.getByDetailCode())
            sql.append(" select coa.glcode as accCode,coa.name as name,null as schedule,null as FIEscheduleId,0 as isMajor from Chartofaccounts coa")
                    .append(" where  coa.type=:type and length(coa.glcode)=:minorCodeLength and coa.glcode like :glcode and classification=4 and isActiveForPosting=true order by 1 ");
        else if (reportSearch.getByDepartment())
            sql.append(" select coa.glcode as accCode,coa.name as name,mp.schedule as schedule,mp.id as FIEscheduleId,0 as isMajor from Chartofaccounts coa,Schedulemapping mp")
                    .append(" where coa.FIEscheduleId=mp.id and coa.type=:type and length(coa.glcode)=:minorCodeLength and coa.FIEscheduleId=:FIEscheduleId  order by 1");
        else
            sql.append(" select coa.glcode as accCode,coa.name as name,mp.schedule as schedule,mp.id as FIEscheduleId, 0 as isMajor from Chartofaccounts coa,Schedulemapping mp")
                    .append(" where coa.FIEscheduleId=mp.id and coa.type=:type and length(coa.glcode)=:minorCodeLength")
                    .append(" Union ")
                    .append(" select coa.glcode as accCode,coa.name as name,null as schedule,null as FIEscheduleId ,1 as isMajor from Chartofaccounts coa")
                    .append(" where  coa.type=:type and length(coa.glcode)=:majorCodeLength order by 1");
        final Query query = persistenceService.getSession().createNativeQuery(sql.toString())
                .addScalar("accCode", StringType.INSTANCE)
                .addScalar("name", StringType.INSTANCE)
                .addScalar("schedule", StringType.INSTANCE)
                .addScalar("FIEscheduleId", LongType.INSTANCE)
                .addScalar("isMajor", BooleanType.INSTANCE)
                .setParameter("type", reportSearch.getIncExp(), StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));
        if (reportSearch.getByDetailCode()) {
            query.setParameter("glcode", reportSearch.getGlcode() + "%", StringType.INSTANCE)
                    .setParameter("minorCodeLength", reportSearch.getMinorCodeLen());
        } else if (reportSearch.getByDepartment()) {
            query.setParameter("FIEscheduleId", reportSearch.getFIEscheduleId(), LongType.INSTANCE)
                    .setParameter("minorCodeLength", reportSearch.getMinorCodeLen());
        } else {
            query.setParameter("minorCodeLength", reportSearch.getMinorCodeLen())
                    .setParameter("majorCodeLength", reportSearch.getMajorCodeLen());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("----------------" + sql);
        return query.list();

    }

    public void getAmountList(final FunctionwiseIE functionwiseIE, final ReportSearch reportSearch) throws ApplicationException,
            ParseException {

        final Map.Entry<String, Map<String, Object>> filterQueryVoucherMapEntry = getFilterQueryVoucher(reportSearch).entrySet().iterator().next();
        final String filterQueryVoucherString = filterQueryVoucherMapEntry.getKey();
        final Map<String, Object> voucherParams = filterQueryVoucherMapEntry.getValue();
        final Map.Entry<String, Map<String, Object>> filterQueryGLMapEntry = getFilterQueryGL(reportSearch).entrySet().iterator().next();
        final String filterQueryGLString = filterQueryGLMapEntry.getKey();
        final Map<String, Object> glParams = filterQueryGLMapEntry.getValue();
        final StringBuilder sql = new StringBuilder("SELECT fn.code,fn.name,CONCAT(CONCAT(coa.majorcode,'-'),coa.name),case '")
                .append(reportSearch.getIncExp())
                .append("' when  'I' then (SUM(gl.creditamount)-SUM(gl.debitamount)) when 'E' then (SUM(gl.debitamount)-SUM(gl.creditamount)) else 0 end AS amt ")
                .append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis ")
                .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:majorCodeLength)=coa.glcode AND coa.TYPE=:coaType ")
                .append(" AND fn.id = gl.functionid ").append(filterQueryVoucherString).append(filterQueryGLString)
                .append(" GROUP BY fn.code,fn.name,CONCAT(CONCAT(coa.majorcode,'-'),coa.name) order by 1,3");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("sql===" + sql);
        final Query query = persistenceService.getSession().createNativeQuery(sql.toString());
        query.setParameter("majorCodeLength", reportSearch.getMajorCodeLen(), IntegerType.INSTANCE)
                .setParameter("coaType", reportSearch.getIncExp(), StringType.INSTANCE);
        voucherParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        glParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        final List<Object[]> list = query.list();
        FunctionwiseIEEntry entry = new FunctionwiseIEEntry();
        Map<String, BigDecimal> majorcodeWiseAmount = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> totalAmountMap = new HashMap<String, BigDecimal>();
        String tempFunctionCode = "";
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal grandTotal = BigDecimal.ZERO;
        int i = 1;
        for (final Object[] obj : list) {
            if (tempFunctionCode.equals(obj[0].toString())) {
                if (functionwiseIE.getMajorCodeList().contains(obj[2].toString())) {
                    majorcodeWiseAmount.put(obj[2].toString(), round((BigDecimal) obj[3]));
                    totalIncome = totalIncome.add((BigDecimal) obj[3]);
                }
            } else {
                if (!majorcodeWiseAmount.isEmpty()) {
                    entry.setTotalIncome(round(totalIncome));
                    entry.setMajorcodeWiseAmount(majorcodeWiseAmount);
                    functionwiseIE.add(entry);
                    totalIncome = BigDecimal.ZERO;
                }

                entry = new FunctionwiseIEEntry();
                entry.setSlNo(String.valueOf(i++));
                entry.setFunctionCode(obj[0].toString());
                entry.setFunctionName(obj[1].toString());
                majorcodeWiseAmount = new HashMap<String, BigDecimal>();
                if (functionwiseIE.getMajorCodeList().contains(obj[2].toString())) {
                    majorcodeWiseAmount.put(obj[2].toString(), round((BigDecimal) obj[3]));
                    totalIncome = totalIncome.add((BigDecimal) obj[3]);
                }
            }
            if (totalAmountMap.containsKey(obj[2].toString()))
                totalAmountMap
                        .put(obj[2].toString(), totalAmountMap.get(obj[2].toString()).add((BigDecimal) obj[3]));
            else
                totalAmountMap.put(obj[2].toString(), (BigDecimal) obj[3]);
            grandTotal = grandTotal.add((BigDecimal) obj[3]);
            tempFunctionCode = obj[0].toString();
        }
        if (!majorcodeWiseAmount.isEmpty()) {
            entry.setTotalIncome(round(totalIncome));
            entry.setMajorcodeWiseAmount(majorcodeWiseAmount);
            functionwiseIE.add(entry);

            entry = new FunctionwiseIEEntry();
            entry.setSlNo("");
            entry.setFunctionName("Total for the Period");
            entry.setTotalIncome(round(grandTotal));
            majorcodeWiseAmount = new HashMap<String, BigDecimal>();
            final Iterator it = totalAmountMap.keySet().iterator();
            String key;
            while (it.hasNext()) {
                key = it.next().toString();
                majorcodeWiseAmount.put(key, round(totalAmountMap.get(key)));
            }
            entry.setMajorcodeWiseAmount(majorcodeWiseAmount);
            functionwiseIE.add(entry);
        }
    }

    @SuppressWarnings("unchecked")
    public List<CommonReportBean> getAmountListForMinorCode(final FunctionwiseIE functionwiseIE, final ReportSearch reportSearch)
            throws ApplicationException, ParseException {

        final StringBuilder sql = new StringBuilder("");
        final Map<String, Object> params = new HashMap<>();
        final Query query;

        final Map.Entry<String, Map<String, Object>> voucherQueryMapEntry = getFilterQueryVoucherAsOnDate(reportSearch).entrySet().iterator().next();
        final String voucherQuery = voucherQueryMapEntry.getKey();
        final Map<String, Object> voucherQueryParams = voucherQueryMapEntry.getValue();

        final Map.Entry<String, Map<String, Object>> glQueryMapEntry = getFilterQueryGL(reportSearch).entrySet().iterator().next();
        final String glQuery = glQueryMapEntry.getKey();
        final Map<String, Object> glQueryParams = glQueryMapEntry.getValue();

        if (reportSearch.getByDetailCode()) {
            if (reportSearch.getIncExp().equalsIgnoreCase("E")) {
                sql.append("SELECT SUBSTR(coa.glcode,1,:minorCodeLength) as accCode,d.dept_name as deptName ,(SUM(gl.debitamount)-SUM(gl.creditamount)) AS amount,0 as isMajor ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
            } else {
                sql.append("SELECT SUBSTR(coa.glcode,1,:minorCodeLength) as accCode,d.dept_name as deptName ,(SUM(gl.creditamount)-SUM(gl.debitamount)) AS amount,0 as isMajor ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
            }

            if (reportSearch.getIncExp().equalsIgnoreCase("E")) {
                sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis,eg_department d  ")
                        .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:minorCodeLength)=coa.glcode")
                        .append(" AND (coa.TYPE=:coaType ").append(capExpCodeCond).append(")")
                        .append(" and d.dept_name=:deptName and coa.glcode like :glcode AND fn.id = gl.functionid  and vmis.departmentid=d.id_dept ")
                        .append(voucherQuery).append(glQuery)
                        .append(" GROUP BY  SUBSTR(coa.glcode,1,:minorCodeLength),d.dept_name ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
                params.put("coaType", reportSearch.getIncExp());
            } else {
                sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis,eg_department d  ")
                        .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:minorCodeLength)=coa.glcode")
                        .append(" AND coa.TYPE=:coaType and d.dept_name=:deptName and coa.glcode like :glcode AND fn.id = gl.functionid  and vmis.departmentid=d.id_dept ")
                        .append(voucherQuery).append(glQuery)
                        .append(" GROUP BY  SUBSTR(coa.glcode,1,:minorCodeLength),d.dept_name ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
                params.put("coaType", reportSearch.getIncExp());
            }
            sql.append("order by 2,1 ");
            query = persistenceService.getSession().createNativeQuery(sql.toString()).addScalar("accCode", StringType.INSTANCE).
                    addScalar("amount", BigDecimalType.INSTANCE)
                    .addScalar("isMajor", BooleanType.INSTANCE)
                    .addScalar("deptName", StringType.INSTANCE)
                    .setParameter("glcode", reportSearch.getGlcode() + "%", StringType.INSTANCE)
                    .setParameter("deptName", reportSearch.getDepartment().getName(), StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));

        } else if (reportSearch.getByDepartment()) {
            if (reportSearch.getIncExp().equalsIgnoreCase("E")) {
                sql.append("SELECT SUBSTR(coa.glcode,1,:minorCodeLength) as accCode,d.dept_name as deptName ,(SUM(gl.debitamount)-SUM(gl.creditamount)) AS amount,0 as isMajor ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
            } else {
                sql.append("SELECT SUBSTR(coa.glcode,1,:minorCodeLength) as accCode,d.dept_name as deptName ,(SUM(gl.creditamount)-SUM(gl.debitamount)) AS amount,0 as isMajor ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
            }
            if (reportSearch.getIncExp().equalsIgnoreCase("E")) {
                sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis,eg_department d  ")
                        .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:minorCodeLength)=coa.glcode AND (coa.TYPE=:coaType")
                        .append(capExpCodeCond).append(")").append(" AND fn.id = gl.functionid  and vmis.departmentid=d.id_dept ")
                        .append(voucherQuery).append(glQuery).append(" GROUP BY  SUBSTR(coa.glcode,1,:minorCodeLength),d.dept_name ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
                params.put("coaType", reportSearch.getIncExp());
            } else {
                sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis,eg_department d  ")
                        .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:minorCodeLength)=coa.glcode AND coa.TYPE=:coaType")
                        .append(" AND fn.id = gl.functionid  and vmis.departmentid=d.id_dept ")
                        .append(voucherQuery).append(glQuery).append(" GROUP BY  SUBSTR(coa.glcode,1,:minorCodeLength),d.dept_name ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
                params.put("coaType", reportSearch.getIncExp());
            }
            sql.append("order by 2,1 ");
            query = persistenceService.getSession().createNativeQuery(sql.toString()).addScalar("accCode", StringType.INSTANCE).
                    addScalar("amount", BigDecimalType.INSTANCE)
                    .addScalar("isMajor", BooleanType.INSTANCE)
                    .addScalar("deptName", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));

        } else {
            if (reportSearch.getIncExp().equalsIgnoreCase("E"))
                sql.append("SELECT coa.majorcode as accCode,coa.name as accName,(SUM(gl.debitamount)-SUM(gl.creditamount)) AS amount,1 as isMajor ");
            else
                sql.append("SELECT coa.majorcode as accCode,coa.name as accName,(SUM(gl.creditamount)-SUM(gl.debitamount)) AS amount,1 as isMajor ");

            if (reportSearch.getIncExp().equalsIgnoreCase("E")) {
                sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis  ")
                        .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:majorCodeLength)=coa.glcode AND (coa.TYPE=:coaType")
                        .append(capExpCodeCond).append(")").append(" AND fn.id = gl.functionid ").append(voucherQuery)
                        .append(glQuery).append(" GROUP BY coa.majorcode,coa.name ");
                params.put("majorCodeLength", reportSearch.getMajorCodeLen());
                params.put("coaType", reportSearch.getIncExp());
            } else {
                sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis  ")
                        .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:majorCodeLength)=coa.glcode AND coa.TYPE=:coaType")
                        .append(" AND fn.id = gl.functionid ").append(voucherQuery).append(glQuery).append(" GROUP BY coa.majorcode,coa.name ");
                params.put("majorCodeLength", reportSearch.getMajorCodeLen());
                params.put("coaType", reportSearch.getIncExp());
            }
            if (reportSearch.getIncExp().equalsIgnoreCase("E")) {
                sql.append(" Union SELECT SUBSTR(coa.glcode,1,:minorCodeLength) as accCode, coa.name  as accName,(SUM(gl.debitamount)-SUM(gl.creditamount)) AS amount ,0 as isMajor ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
            } else {
                sql.append(" Union SELECT SUBSTR(coa.glcode,1,:minorCodeLength) as accCode, coa.name  as accName,(SUM(gl.creditamount)-SUM(gl.debitamount)) AS amount ,0 as isMajor ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
            }

            if (reportSearch.getIncExp().equalsIgnoreCase("E")) {
                sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis ")
                        .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:minorCodeLength)=coa.glcode AND (coa.TYPE=:coaType")
                        .append(capExpCodeCond).append(")").append(" AND fn.id = gl.functionid ").append(voucherQuery).append(glQuery)
                        .append(" GROUP BY SUBSTR(coa.glcode,1,:minorCodeLength),coa.name order by 1,2 ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
                params.put("coaType", reportSearch.getIncExp());
            } else {
                sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis ")
                        .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:minorCodeLength)=coa.glcode AND coa.TYPE=:coaType")
                        .append(" AND fn.id = gl.functionid ").append(voucherQuery).append(glQuery).append(" GROUP BY SUBSTR(coa.glcode,1,:minorCodeLength),coa.name order by 1,2 ");
                params.put("minorCodeLength", reportSearch.getMinorCodeLen());
                params.put("coaType", reportSearch.getIncExp());
            }
            query = persistenceService.getSession().createNativeQuery(sql.toString()).addScalar("accCode", StringType.INSTANCE).
                    addScalar("amount", BigDecimalType.INSTANCE)
                    .addScalar("isMajor", BooleanType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));
        }

        voucherQueryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        glQueryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        params.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("sql===" + sql);
        final List<CommonReportBean> list = query.list();
        return list;

    }

    @SuppressWarnings("unchecked")
    public List<CommonReportBean> getPreviousYearAmountListForMinorCode(final FunctionwiseIE functionwiseIE,
                                                                        final ReportSearch reportSearch) throws ApplicationException, ParseException {

        final StringBuilder sql = new StringBuilder("");
        final Query query;

        final Map.Entry<String, Map<String, Object>> voucherQueryMapEntry = getFilterQueryVoucherAsOnPreviousYearDate(reportSearch).entrySet().iterator().next();
        final String voucherQuery = voucherQueryMapEntry.getKey();
        final Map<String, Object> voucherQueryParams = voucherQueryMapEntry.getValue();

        final Map.Entry<String, Map<String, Object>> glQueryMapEntry = getFilterQueryGL(reportSearch).entrySet().iterator().next();
        final String glQuery = glQueryMapEntry.getKey();
        final Map<String, Object> glQueryParams = glQueryMapEntry.getValue();

        if (reportSearch.getByDetailCode()) {
            if (reportSearch.getIncExp().equalsIgnoreCase("E"))
                sql.append("SELECT SUBSTR(coa.glcode,1,").append(reportSearch.getMinorCodeLen())
                        .append(") as accCode,d.dept_name as deptName ,(SUM(gl.debitamount)-SUM(gl.creditamount)) AS amount,0 as isMajor ");
            else
                sql.append("SELECT SUBSTR(coa.glcode,1,").append(reportSearch.getMinorCodeLen())
                        .append(") as accCode,d.dept_name as deptName ,(SUM(gl.creditamount)-SUM(gl.debitamount)) AS amount,0 as isMajor ");

            sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis,eg_department d  ")
                    .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:minCodeLength)=coa.glcode AND coa.TYPE=:coaType ")
                    .append(" and d.dept_name=:deptName and coa.glcode like :glcode AND fn.id = gl.functionid  and vmis.departmentid=d.id_dept ")
                    .append(voucherQuery).append(glQuery)
                    .append(" GROUP BY  SUBSTR(coa.glcode,1,:minCodeLength),d.dept_name ");
            sql.append("order by 2,1 ");
            query = persistenceService.getSession().createNativeQuery(sql.toString()).addScalar("accCode", StringType.INSTANCE).
                    addScalar("amount", BigDecimalType.INSTANCE)
                    .addScalar("isMajor", BooleanType.INSTANCE)
                    .addScalar("deptName", StringType.INSTANCE)
                    .setString("glcode", reportSearch.getGlcode() + "%")
                    .setString("deptName", reportSearch.getDepartment().getName())
                    .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));
            query.setParameter("minCodeLength", reportSearch.getMinorCodeLen(), IntegerType.INSTANCE)
                    .setParameter("coaType", reportSearch.getIncExp(), StringType.INSTANCE);

        } else if (reportSearch.getByDepartment()) {
            if (reportSearch.getIncExp().equalsIgnoreCase("E"))
                sql.append("SELECT SUBSTR(coa.glcode,1,").append(reportSearch.getMinorCodeLen())
                        .append(") as accCode,d.dept_name as deptName ,(SUM(gl.debitamount)-SUM(gl.creditamount)) AS amount,0 as isMajor ");
            else
                sql.append("SELECT SUBSTR(coa.glcode,1,").append(reportSearch.getMinorCodeLen())
                        .append(") as accCode,d.dept_name as deptName ,(SUM(gl.creditamount)-SUM(gl.debitamount)) AS amount,0 as isMajor ");

            sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis,eg_department d  ")
                    .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:minCodeLength)=coa.glcode")
                    .append(" AND coa.TYPE=:coaType AND fn.id = gl.functionid  and vmis.departmentid=d.id_dept ")
                    .append(voucherQuery).append(glQuery)
                    .append(" GROUP BY  SUBSTR(coa.glcode,1,:minCodeLength),d.dept_name ");
            sql.append("order by 2,1 ");
            query = persistenceService.getSession().createNativeQuery(sql.toString()).addScalar("accCode", StringType.INSTANCE).
                    addScalar("amount", BigDecimalType.INSTANCE)
                    .addScalar("isMajor", BooleanType.INSTANCE)
                    .addScalar("deptName", StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));

            query.setParameter("minCodeLength", reportSearch.getMinorCodeLen(), IntegerType.INSTANCE)
                    .setParameter("coaType", reportSearch.getIncExp(), StringType.INSTANCE);
        } else {
            if (reportSearch.getIncExp().equalsIgnoreCase("E"))
                sql.append("SELECT coa.majorcode as accCode,coa.name as accName,(SUM(gl.debitamount)-SUM(gl.creditamount)) AS amount,1 as isMajor ");
            else
                sql.append("SELECT coa.majorcode as accCode,coa.name as accName,(SUM(gl.creditamount)-SUM(gl.debitamount)) AS amount,1 as isMajor ");
            sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis  ")
                    .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:majorCodeLength)=coa.glcode")
                    .append(" AND coa.TYPE=:coaType AND fn.id = gl.functionid ")
                    .append(voucherQuery).append(glQuery).append(" GROUP BY coa.majorcode,coa.name ");
            if (reportSearch.getIncExp().equalsIgnoreCase("E"))
                sql.append(" Union SELECT SUBSTR(coa.glcode,1,").append(reportSearch.getMinorCodeLen())
                        .append(") as accCode, coa.name  as accName,(SUM(gl.debitamount)-SUM(gl.creditamount)) AS amount ,0 as isMajor ");
            else
                sql.append(" Union SELECT SUBSTR(coa.glcode,1,").append(reportSearch.getMinorCodeLen())
                        .append(") as accCode, coa.name  as accName,(SUM(gl.creditamount)-SUM(gl.debitamount)) AS amount ,0 as isMajor ");

            sql.append(" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis ")
                    .append(" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,:minCodeLength)=coa.glcode")
                    .append(" AND coa.TYPE=:coaType AND fn.id = gl.functionid ")
                    .append(voucherQuery).append(glQuery).append(" GROUP BY SUBSTR(coa.glcode,1,:minCodeLength),coa.name order by 1,2 ");
            query = persistenceService.getSession().createNativeQuery(sql.toString()).addScalar("accCode", StringType.INSTANCE).
                    addScalar("amount", BigDecimalType.INSTANCE)
                    .addScalar("isMajor", BooleanType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));
            query.setParameter("minCodeLength", reportSearch.getMinorCodeLen(), IntegerType.INSTANCE)
                    .setParameter("majorCodeLength", reportSearch.getMajorCodeLen(), IntegerType.INSTANCE)
                    .setParameter("coaType", reportSearch.getIncExp(), StringType.INSTANCE);
        }

        voucherQueryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        glQueryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("sql===" + sql);
        final List<CommonReportBean> list = query.list();
        return list;

    }

    public void populateData(final FunctionwiseIE functionwiseIE, final ReportSearch reportSearch) throws ApplicationException,
            ParseException {
        getMajorCodeList(functionwiseIE, reportSearch);
        getAmountList(functionwiseIE, reportSearch);
    }

    public List<CommonReportBean> populateDataWithBudget(final FunctionwiseIE functionwiseIE, final ReportSearch reportSearch)
            throws ApplicationException, ParseException {
        final String capExpCode = appConfigValuesService.getConfigValuesByModuleAndKey
                (Constants.EGF, FinancialConstants.APPCONFIG_COA_MAJORCODE_CAPITAL_EXP_FIE_REPORT).get(0).getValue();
        final String[] temp = capExpCode.split(",");
        // To generate condition for appconfig values.
        StringBuilder capExpCodes = new StringBuilder("");
        for (final String element : temp)
            capExpCodes.append(" or coa.glcode like ").append(element).append("%");
        capExpCodeCond = capExpCodes.toString();
        // To generate major code values from appconfig with quotes.
        StringBuilder capExpCodesWithQuotes = new StringBuilder("");
        for (int i = 0; i < temp.length; i++) {
            capExpCodesWithQuotes.append("'").append(temp[i]).append("'");
            if (i != temp.length - 1)
                capExpCodesWithQuotes.append(",");
        }
        capExpCodesWithQuotesCond = capExpCodesWithQuotes.toString();

        // functionwiseIE.setMinorCodeList(getMinorCodeList(reportSearch));
        final List<CommonReportBean> minorAndMajorCodeList = getMinorAndMajorCodeList(reportSearch);
        if (reportSearch.getIncExp().equalsIgnoreCase("E")) {
            final List<CommonReportBean> minorAndMajorCodeListForCapitalExp = getMinorAndMajorCodeListForCapitalExp(reportSearch);
            minorAndMajorCodeList.addAll(minorAndMajorCodeListForCapitalExp);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("deptName\t Acccode\t Name\t Amount");
        final List<CommonReportBean> amountListForMinorCode = getAmountListForMinorCode(functionwiseIE, reportSearch);
        final List<CommonReportBean> amountPreviousyearListForMinorCode = getPreviousYearAmountListForMinorCode(functionwiseIE,
                reportSearch);
        // if(LOGGER.isDebugEnabled())
        // LOGGER.debug("amountListForMinorCode---------------------------------------------------------------------------------------");
        print(amountListForMinorCode);

        final Map.Entry<String, Map<String, Object>> budgetQueryMapEntry = getBudgetQueryForMinorCodes(reportSearch).entrySet().iterator().next();
        final String queryStr = budgetQueryMapEntry.getKey();
        final Map<String, Object> budgetQueryParams = budgetQueryMapEntry.getValue();
        final List<CommonReportBean> beAmountListForMinorCode = getBudgetAmountListForMinorCode(reportSearch, "BE", queryStr, budgetQueryParams);
        // if(LOGGER.isDebugEnabled())
        // LOGGER.debug("beAmountListForMinorCode---------------------------------------------------------------------------------------");
        print(beAmountListForMinorCode);
        final List<CommonReportBean> reAmountListForMinorCode = getBudgetAmountListForMinorCode(reportSearch, "RE", queryStr, budgetQueryParams);
        // if(LOGGER.isDebugEnabled())
        // LOGGER.debug("reAmountListForMinorCode---------------------------------------------------------------------------------------");
        print(reAmountListForMinorCode);
        final Map.Entry<String, Map<String, Object>> budgetReappQueryMapEntry = getBudgetReappQueryForMinorCodes(reportSearch).entrySet().iterator().next();
        final String reappQueryStr = budgetReappQueryMapEntry.getKey();
        final Map<String, Object> reappQueryParams = budgetReappQueryMapEntry.getValue();
        final List<CommonReportBean> beappAmountListForMinorCode = getBudgetApprAmountListForMinorCode(reportSearch, "BE",
                reappQueryStr, reappQueryParams);
        final List<CommonReportBean> reappAmountListForMinorCode = getBudgetApprAmountListForMinorCode(reportSearch, "RE",
                reappQueryStr, reappQueryParams);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("beappAmountListForMinorCode---------------------------------------------------------------------------------------");
        print(beappAmountListForMinorCode);
        // two logic here 1. put to linked hashmap and get
        // 2. merge by comparing
        final Map<String, CommonReportBean> minorCodeAmountMap = loadIntoMap(amountListForMinorCode, reportSearch);
        final Map<String, CommonReportBean> beAmountMap = loadIntoMap(beAmountListForMinorCode, reportSearch);
        final Map<String, CommonReportBean> reAmountMap = loadIntoMap(reAmountListForMinorCode, reportSearch);
        final Map<String, CommonReportBean> reAppAmountMap = loadIntoMap(reappAmountListForMinorCode, reportSearch);
        final Map<String, CommonReportBean> beAppAmountMap = loadIntoMap(beappAmountListForMinorCode, reportSearch);
        final Map<String, CommonReportBean> prevousAmountMap = loadIntoMap(amountPreviousyearListForMinorCode, reportSearch);

        new ArrayList<CommonReportBean>();
        CommonReportBean beCRB;
        CommonReportBean reCRB;
        CommonReportBean reAppCRB;
        CommonReportBean beAppCRB;
        CommonReportBean glCRB;
        CommonReportBean pyglCRB;
        BigDecimal beSum = BigDecimal.ZERO, reSum = BigDecimal.ZERO, beAppSum = BigDecimal.ZERO, reAppSum = BigDecimal.ZERO, amountSum = BigDecimal.ZERO, pyAmountSum = BigDecimal.ZERO;
        if (reportSearch.getByDepartment() && !reportSearch.getByDetailCode()) {
            final List<CommonReportBean> deptWiseWithBudgetList = new ArrayList<CommonReportBean>();
            CommonReportBean crb;

            for (final Department dept : reportSearch.getDeptList())
                for (final CommonReportBean bean : minorAndMajorCodeList) {
                    final String accCode = dept.getName() + "-" + bean.getAccCode();
                    crb = new CommonReportBean();

                    crb.setName(bean.getName());
                    crb.setDeptName(dept.getName());
                    crb.setAccCode(bean.getAccCode());
                    glCRB = minorCodeAmountMap.get(accCode);
                    pyglCRB = prevousAmountMap.get(accCode);
                    beCRB = beAmountMap.get(accCode);
                    reCRB = reAmountMap.get(accCode);
                    reAppCRB = reAppAmountMap.get(accCode);
                    beAppCRB = beAppAmountMap.get(accCode);
                    if (glCRB != null) {
                        crb.setAmount(glCRB.getAmount());
                        if (glCRB.getIsMajor() == false)
                            amountSum = glCRB.getAmount() == null ? amountSum.add(BigDecimal.ZERO) : amountSum.add(glCRB
                                    .getAmount());
                    }
                    if (pyglCRB != null) {
                        crb.setPyAmount(pyglCRB.getAmount());
                        if (pyglCRB.getIsMajor() == false)
                            pyAmountSum = pyglCRB.getAmount() == null ? pyAmountSum.add(BigDecimal.ZERO) : pyAmountSum
                                    .add(pyglCRB.getAmount());
                    }
                    if (beCRB != null) {
                        crb.setBeAmount(beCRB.getAmount());
                        if (beCRB.getIsMajor() == false)
                            beSum = beCRB.getAmount() == null ? beSum.add(BigDecimal.ZERO) : beSum.add(beCRB.getAmount());
                    }
                    if (reCRB != null) {
                        crb.setReAmount(reCRB.getAmount());
                        if (reCRB.getIsMajor() == false)
                            reSum = reCRB.getAmount() == null ? reSum.add(BigDecimal.ZERO) : reSum.add(reCRB.getAmount());
                    }
                    if (reAppCRB != null) {
                        crb.setReAppAmount(reAppCRB.getAmount());
                        if (reAppCRB.getIsMajor() == false)
                            reAppSum = reAppCRB.getAmount() == null ? reAppSum.add(BigDecimal.ZERO) : reAppSum.add(reAppCRB
                                    .getAmount());
                    }
                    if (beAppCRB != null) {
                        crb.setBeAppAmount(beAppCRB.getAmount());
                        if (beAppCRB.getIsMajor() == false)
                            beAppSum = beAppCRB.getAmount() == null ? beAppSum.add(BigDecimal.ZERO) : beAppSum.add(beAppCRB
                                    .getAmount());
                    }

                    deptWiseWithBudgetList.add(crb);
                }
            final CommonReportBean totalCrb = new CommonReportBean("", "TOTAL", beSum, reSum, beAppSum, reAppSum, amountSum,
                    pyAmountSum);

            deptWiseWithBudgetList.add(totalCrb);
            // print(deptWiseWithBudgetList);
            return deptWiseWithBudgetList;
        } else {
            for (final CommonReportBean crb : minorAndMajorCodeList) {
                final String accCode = crb.getAccCode();
                glCRB = minorCodeAmountMap.get(accCode);
                pyglCRB = prevousAmountMap.get(accCode);
                beCRB = beAmountMap.get(accCode);
                reCRB = reAmountMap.get(accCode);
                reAppCRB = reAppAmountMap.get(accCode);
                beAppCRB = beAppAmountMap.get(accCode);
                if (glCRB != null) {
                    crb.setAmount(glCRB.getAmount());
                    if (glCRB.getIsMajor() == false)
                        amountSum = glCRB.getAmount() == null ? amountSum.add(BigDecimal.ZERO) : amountSum.add(glCRB.getAmount());
                }
                if (pyglCRB != null) {
                    crb.setPyAmount(pyglCRB.getAmount());
                    if (pyglCRB.getIsMajor() == false)
                        pyAmountSum = pyglCRB.getAmount() == null ? pyAmountSum.add(BigDecimal.ZERO) : pyAmountSum.add(pyglCRB
                                .getAmount());
                }
                if (beCRB != null) {
                    crb.setBeAmount(beCRB.getAmount());
                    if (beCRB.getIsMajor() == false)
                        beSum = beCRB.getAmount() == null ? beSum.add(BigDecimal.ZERO) : beSum.add(beCRB.getAmount());
                }
                if (reCRB != null) {
                    crb.setReAmount(reCRB.getAmount());
                    if (reCRB.getIsMajor() == false)
                        reSum = reCRB.getAmount() == null ? reSum.add(BigDecimal.ZERO) : reSum.add(reCRB.getAmount());
                }
                if (reAppCRB != null) {
                    crb.setReAppAmount(reAppCRB.getAmount());
                    if (reAppCRB.getIsMajor() == false)
                        reAppSum = reAppCRB.getAmount() == null ? reAppSum.add(BigDecimal.ZERO) : reAppSum.add(reAppCRB
                                .getAmount());
                }
                if (beAppCRB != null) {
                    crb.setBeAppAmount(beAppCRB.getAmount());
                    if (beAppCRB.getIsMajor() == false)
                        beAppSum = beAppCRB.getAmount() == null ? beAppSum.add(BigDecimal.ZERO) : beAppSum.add(beAppCRB
                                .getAmount());
                }
            }
            final CommonReportBean totalCrb = new CommonReportBean("", "TOTAL", beSum, reSum, beAppSum, reAppSum, amountSum,
                    pyAmountSum);
            minorAndMajorCodeList.add(totalCrb);
        }
        // print(minorAndMajorCodeList);
        return minorAndMajorCodeList;
    }

    public List<CommonReportBean> populateIncomeDataWithBudget(final FunctionwiseIE functionwiseIE,
                                                               final ReportSearch reportSearch)
            throws ApplicationException, ParseException {

        final List<CommonReportBean> minorAndMajorCodeList = getIncomeMinorAndMajorCodeList(reportSearch);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("deptName\t Acccode\t Name\t Amount");
        final List<CommonReportBean> amountListForMinorCode = getAmountListForMinorCode(functionwiseIE, reportSearch);
        final List<CommonReportBean> amountPreviousyearListForMinorCode = getPreviousYearAmountListForMinorCode(functionwiseIE,
                reportSearch);
        final Map.Entry<String, Map<String, Object>> budgetQueryMapEntry = getBudgetQueryForMinorCodes(reportSearch).entrySet().iterator().next();
        final String queryStr = budgetQueryMapEntry.getKey();
        final Map<String, Object> budgetQueryParams = budgetQueryMapEntry.getValue();

        final List<CommonReportBean> beAmountListForMinorCode = getBudgetAmountListForMinorCode(reportSearch, "BE", queryStr, budgetQueryParams);
        // if(LOGGER.isDebugEnabled())
        // LOGGER.debug("beAmountListForMinorCode---------------------------------------------------------------------------------------");
        // print(beAmountListForMinorCode);
        final List<CommonReportBean> reAmountListForMinorCode = getBudgetAmountListForMinorCode(reportSearch, "RE", queryStr, budgetQueryParams);
        // if(LOGGER.isDebugEnabled())
        // LOGGER.debug("reAmountListForMinorCode---------------------------------------------------------------------------------------");
        // print(reAmountListForMinorCode);
        final Map<String, CommonReportBean> minorCodeAmountMap = loadIntoMap(amountListForMinorCode, reportSearch);
        final Map<String, CommonReportBean> beAmountMap = loadIntoMap(beAmountListForMinorCode, reportSearch);
        final Map<String, CommonReportBean> reAmountMap = loadIntoMap(reAmountListForMinorCode, reportSearch);
        final Map<String, CommonReportBean> prevousAmountMap = loadIntoMap(amountPreviousyearListForMinorCode, reportSearch);
        new ArrayList<CommonReportBean>();
        CommonReportBean pyglCRB;
        CommonReportBean glCRB;
        CommonReportBean objBE;
        CommonReportBean objRE;
        BigDecimal beTotal = BigDecimal.ZERO, reTotal = BigDecimal.ZERO, previousSum = BigDecimal.ZERO, amountSum = BigDecimal.ZERO;
        if (reportSearch.getByDepartment() && !reportSearch.getByDetailCode()) {
            final List<CommonReportBean> deptWiseWithBudgetList = new ArrayList<CommonReportBean>();
            CommonReportBean crb;

            for (final Department dept : reportSearch.getDeptList())
                for (final CommonReportBean bean : minorAndMajorCodeList) {
                    final String accCode = dept.getName() + "-" + bean.getAccCode();
                    crb = new CommonReportBean();

                    crb.setName(bean.getName());
                    crb.setDeptName(dept.getName());
                    crb.setAccCode(bean.getAccCode());
                    glCRB = minorCodeAmountMap.get(accCode);
                    pyglCRB = prevousAmountMap.get(accCode);
                    objBE = beAmountMap.get(accCode);
                    objRE = reAmountMap.get(accCode);

                    if (glCRB != null) {
                        crb.setAmount(glCRB.getAmount());
                        if (glCRB.getIsMajor() == false)
                            amountSum = glCRB.getAmount() == null ? amountSum.add(BigDecimal.ZERO) : amountSum.add(glCRB
                                    .getAmount());
                    }
                    if (pyglCRB != null) {
                        crb.setPyAmount(pyglCRB.getAmount());
                        if (pyglCRB.getIsMajor() == false)
                            previousSum = pyglCRB.getAmount() == null ? previousSum.add(BigDecimal.ZERO) : previousSum
                                    .add(pyglCRB.getAmount());
                    }
                    if (objBE != null) {
                        crb.setBeAmount(objBE.getAmount());
                        if (objBE.getIsMajor() == false)
                            beTotal = objBE.getAmount() == null ? beTotal.add(BigDecimal.ZERO) : beTotal.add(objBE.getAmount());
                    }
                    if (objRE != null) {
                        crb.setReAmount(objRE.getAmount());
                        if (objRE.getIsMajor() == false)
                            reTotal = objRE.getAmount() == null ? reTotal.add(BigDecimal.ZERO) : reTotal.add(objRE.getAmount());
                    }
                    deptWiseWithBudgetList.add(crb);
                }
            final CommonReportBean totalCrb = new CommonReportBean("", "TOTAL", beTotal, reTotal, null, null, amountSum,
                    previousSum);
            deptWiseWithBudgetList.add(totalCrb);
            // print(deptWiseWithBudgetList);
            return deptWiseWithBudgetList;
        } else {
            for (final CommonReportBean crb : minorAndMajorCodeList) {
                final String accCode = crb.getAccCode();
                glCRB = minorCodeAmountMap.get(accCode);
                pyglCRB = prevousAmountMap.get(accCode);
                objBE = beAmountMap.get(accCode);
                objRE = reAmountMap.get(accCode);
                if (glCRB != null) {
                    crb.setAmount(glCRB.getAmount());
                    if (glCRB.getIsMajor() == false)
                        amountSum = glCRB.getAmount() == null ? amountSum.add(BigDecimal.ZERO) : amountSum.add(glCRB.getAmount());
                }
                if (pyglCRB != null) {
                    crb.setPyAmount(pyglCRB.getAmount());
                    if (pyglCRB.getIsMajor() == false)
                        previousSum = pyglCRB.getAmount() == null ? previousSum.add(BigDecimal.ZERO) : previousSum.add(pyglCRB
                                .getAmount());
                }
                if (objBE != null) {
                    crb.setBeAmount(objBE.getAmount());
                    if (objBE.getIsMajor() == false)
                        beTotal = objBE.getAmount() == null ? beTotal.add(BigDecimal.ZERO) : beTotal.add(objBE.getAmount());
                }
                if (objRE != null) {
                    crb.setReAmount(objRE.getAmount());
                    if (objRE.getIsMajor() == false)
                        reTotal = objRE.getAmount() == null ? reTotal.add(BigDecimal.ZERO) : reTotal.add(objRE.getAmount());
                }

            }
            final CommonReportBean totalCrb = new CommonReportBean("", "TOTAL", beTotal, reTotal, null, null, amountSum,
                    previousSum);
            minorAndMajorCodeList.add(totalCrb);
        }
        // print(minorAndMajorCodeList);
        return minorAndMajorCodeList;
    }

    private Map<String, CommonReportBean> loadIntoMap(final List<CommonReportBean> amountList, final ReportSearch reportSearch) {
        final Map<String, CommonReportBean> amountMap = new LinkedHashMap<String, CommonReportBean>();
        for (final CommonReportBean crb : amountList)
            if (reportSearch.getByDepartment() && !reportSearch.getByDetailCode())
                amountMap.put(crb.getDeptName() + "-" + crb.getAccCode(), crb);
            else
                amountMap.put(crb.getAccCode(), crb);
        return amountMap;
    }

    private void print(final List<CommonReportBean> crbList) {
        if (LOGGER.isDebugEnabled())
            for (final CommonReportBean crb : crbList)
                if (!crb.isZero())
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(crb.toString());
    }

    private List<CommonReportBean> getBudgetApprAmountListForMinorCode(final ReportSearch reportSearch, final String isBeRe,
                                                                       final String queryStr, final Map<String, Object> queryParams) {
        final Query query;
        if (reportSearch.getAsOnDate().getMonth() == 2 && reportSearch.getAsOnDate().getDate() == 31) {
            if (reportSearch.getByDepartment()) {
                query = persistenceService.getSession().createNativeQuery(queryStr)
                        .addScalar("accCode", StringType.INSTANCE)
                        .addScalar("amount", BigDecimalType.INSTANCE)
                        .addScalar("isMajor", BooleanType.INSTANCE)
                        .addScalar("deptName", StringType.INSTANCE)
                        .setParameter("isBeRe", isBeRe, StringType.INSTANCE)
                        .setParameter("finYearId", reportSearch.getFinYearId(), LongType.INSTANCE)
                        .setParameter("fundId", reportSearch.getFund().getId(), IntegerType.INSTANCE)
                        .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));

                if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                        && reportSearch.getFunction().getId() != -1)
                    query.setParameter("functionId", reportSearch.getFunction().getId(), LongType.INSTANCE);
                if (reportSearch.getByDetailCode()) {
                    query.setParameter("deptName", reportSearch.getDepartment().getName(), StringType.INSTANCE);
                    query.setParameter("glcode", reportSearch.getGlcode() + "%", StringType.INSTANCE);
                } else
                    query.setParameter("FIEscheduleId", reportSearch.getFIEscheduleId(), LongType.INSTANCE);
            } else {
                query = persistenceService.getSession().createNativeQuery(queryStr)
                        .addScalar("accCode", StringType.INSTANCE)
                        .addScalar("amount", BigDecimalType.INSTANCE)
                        .addScalar("isMajor", BooleanType.INSTANCE)
                        .setParameter("isBeRe", isBeRe, StringType.INSTANCE)
                        .setParameter("finYearId", reportSearch.getFinYearId(), LongType.INSTANCE)
                        .setParameter("fundId", reportSearch.getFund().getId(), IntegerType.INSTANCE)
                        .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));
                if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                        && reportSearch.getFunction().getId() != -1)
                    query.setParameter("functionId", reportSearch.getFunction().getId(), LongType.INSTANCE);

            }
        } else if (reportSearch.getByDepartment()) {
            query = persistenceService.getSession().createNativeQuery(queryStr)
                    .addScalar("accCode", StringType.INSTANCE)
                    .addScalar("amount", BigDecimalType.INSTANCE)
                    .addScalar("isMajor", BooleanType.INSTANCE)
                    .addScalar("deptName", StringType.INSTANCE)
                    .setParameter("isBeRe", isBeRe, StringType.INSTANCE)
                    .setParameter("asOnDate", reportSearch.getAsOnDate(), DateType.INSTANCE)
                    .setParameter("finYearId", reportSearch.getFinYearId(), LongType.INSTANCE)
                    .setParameter("fundId", reportSearch.getFund().getId(), IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));

            if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                    && reportSearch.getFunction().getId() != -1)
                query.setParameter("functionId", reportSearch.getFunction().getId(), LongType.INSTANCE);
            if (reportSearch.getByDetailCode()) {
                query.setParameter("deptName", reportSearch.getDepartment().getName(), StringType.INSTANCE);
                query.setParameter("glcode", reportSearch.getGlcode() + "%", StringType.INSTANCE);
            } else
                query.setParameter("FIEscheduleId", reportSearch.getFIEscheduleId(), LongType.INSTANCE);
        } else {
            query = persistenceService.getSession().createNativeQuery(queryStr)
                    .addScalar("accCode", StringType.INSTANCE)
                    .addScalar("amount", BigDecimalType.INSTANCE)
                    .addScalar("isMajor", BooleanType.INSTANCE)
                    .setParameter("isBeRe", isBeRe, StringType.INSTANCE)
                    .setParameter("asOnDate", reportSearch.getAsOnDate(), DateType.INSTANCE)
                    .setParameter("finYearId", reportSearch.getFinYearId(), LongType.INSTANCE)
                    .setParameter("fundId", reportSearch.getFund().getId(), IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));
            if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                    && reportSearch.getFunction().getId() != -1)
                query.setParameter("functionId", reportSearch.getFunction().getId(), LongType.INSTANCE);

        }
        queryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));

        return query.list();

    }

    @SuppressWarnings("unchecked")
    private List<CommonReportBean> getBudgetAmountListForMinorCode(final ReportSearch reportSearch, final String isBeRe,
                                                                   final String queryStr, final Map<String, Object> queryParams) {

        final Query query;
        if (reportSearch.getByDepartment()) {
            query = persistenceService.getSession().createNativeQuery(queryStr)
                    .addScalar("accCode", StringType.INSTANCE)
                    .addScalar("amount", BigDecimalType.INSTANCE)
                    .addScalar("isMajor", BooleanType.INSTANCE)
                    .addScalar("deptName", StringType.INSTANCE)
                    .setParameter("isBeRe", isBeRe, StringType.INSTANCE)
                    .setParameter("asOnDate", reportSearch.getAsOnDate(), DateType.INSTANCE)
                    .setParameter("finYearId", reportSearch.getFinYearId(), LongType.INSTANCE)
                    .setParameter("fundId", reportSearch.getFund().getId(), IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));
            if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                    && reportSearch.getFunction().getId() != -1)
                query.setParameter("functionId", reportSearch.getFunction().getId(), LongType.INSTANCE);
            if (reportSearch.getByDetailCode()) {
                query.setParameter("deptName", reportSearch.getDepartment().getName(), StringType.INSTANCE);
                query.setParameter("glcode", reportSearch.getGlcode() + "%", StringType.INSTANCE);
            } else
                query.setParameter("FIEscheduleId", reportSearch.getFIEscheduleId(), LongType.INSTANCE);
        } else {
            query = persistenceService.getSession().createNativeQuery(queryStr)
                    .addScalar("accCode", StringType.INSTANCE)
                    .addScalar("amount", BigDecimalType.INSTANCE)
                    .addScalar("isMajor", BooleanType.INSTANCE)
                    .setParameter("isBeRe", isBeRe, StringType.INSTANCE)
                    .setParameter("asOnDate", reportSearch.getAsOnDate(), DateType.INSTANCE)
                    .setParameter("finYearId", reportSearch.getFinYearId(), LongType.INSTANCE)
                    .setParameter("fundId", reportSearch.getFund().getId(), IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(CommonReportBean.class));
            if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                    && reportSearch.getFunction().getId() != -1)
                query.setParameter("functionId", reportSearch.getFunction().getId(), LongType.INSTANCE);
        }

        queryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));

        return query.list();
    }

    private Map<String, Map<String, Object>> getBudgetQueryForMinorCodes(final ReportSearch reportSearch) {

        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> queryParams = new HashMap<>();
        final StringBuffer queryStr = new StringBuffer(1024);
        queryStr.append(" select SUBSTR(coa.glcode,1,").append(reportSearch.getMinorCodeLen())
                .append(") as accCode, sum(bd.approvedamount) as amount ,0 as isMajor ");
        if (reportSearch.getByDepartment())
            queryStr.append(",d.dept_name  as deptName ");
        queryStr.append(" from egf_budgetdetail bd , egf_budgetgroup bg,egf_budget b, chartofaccounts coa, eg_wf_states wfs");
        if (reportSearch.getByDetailCode())
            queryStr.append(",eg_department d");
        else if (reportSearch.getByDepartment())
            queryStr.append(",eg_department d, chartofaccounts minorcoa");
        queryStr.append(" where ((bg.maxcode<=coa.id and bg.mincode>=coa.id) or bg.majorcode=coa.id ) and bd.budgetgroup= bg.id  ")
                .append(" and bd.state_id=wfs.id and wfs.created_date<=:asOnDate and wfs.value='END' ")
                .append(" and bd.budget=b.id and b.isbere=:isBeRe and b.financialyearid=:finYearId and bd.fund=:fundId ");
        queryParams.put("asOnDate", reportSearch.getAsOnDate());
        queryParams.put("finYearId", reportSearch.getFinYearId());
        queryParams.put("fundId", reportSearch.getFund().getId());

        if (reportSearch.getByDetailCode()) {
            queryStr.append(" and d.id_dept=bd.executing_department and d.dept_name=:deptName and coa.glcode like :glcode ");
            queryParams.put("deptName", reportSearch.getDepartment().getName());
            queryParams.put("glcode", reportSearch.getGlcode());
        } else if (reportSearch.getByDepartment()) {
            queryStr.append(" and d.id_dept=bd.executing_department  and minorcoa.FIEscheduleId=:FIEscheduleId and  SUBSTR(coa.glcode,1,:minorCodeLength)=minorcoa.glcode ");
            queryParams.put("FIEscheduleId", reportSearch.getFIEscheduleId());
            queryParams.put("minorCodeLength", reportSearch.getMinorCodeLen());
        }
        if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                && reportSearch.getFunction().getId() != -1) {
            queryStr.append("  and bd.function=:functionId ");
            queryParams.put("functionId", reportSearch.getFunction().getId());
        }
        if (reportSearch.getIncExp().equals("E")) {
            queryStr.append(" and (coa.type='E'").append(capExpCodeCond).append(") group by SUBSTR(coa.glcode,1,")
                    .append(reportSearch.getMinorCodeLen()).append(")");
        } else {
            queryStr.append(" and (coa.type='I') group by SUBSTR(coa.glcode,1,").append(reportSearch.getMinorCodeLen()).append(")");
        }
        if (reportSearch.getByDepartment())
            queryStr.append(" ,d.dept_name ");
        if (!reportSearch.getByDepartment()) {
            queryStr.append(" UNION ");
            queryStr.append(" select coa.majorCode as accCode, sum(bd.approvedamount) as amount,1 as isMajor ");
            if (reportSearch.getByDepartment())
                queryStr.append(",d.dept_name  as deptName ");
            queryStr.append(" from egf_budgetdetail bd , egf_budgetgroup bg,egf_budget b, chartofaccounts coa, eg_wf_states wfs ");
            if (reportSearch.getByDepartment())
                queryStr.append(",eg_department d");

            queryStr.append("where ((bg.maxcode<=coa.id and bg.mincode>=coa.id) or bg.majorcode=coa.id ) and bd.budgetgroup= bg.id ")
                    .append(" and bd.budget=b.id and  bd.state_id=wfs.id and wfs.created_date<=:asOnDate and wfs.value='END'  and b.isbere=:isBeRe")
                    .append(" and b.financialyearid=:finYearId and bd.fund=:fundId  ");
            queryParams.put("asOnDate", reportSearch.getAsOnDate());
            queryParams.put("finYearId", reportSearch.getFinYearId());
            queryParams.put("fundId", reportSearch.getFund().getId());
            if (reportSearch.getByDepartment()) {
                queryStr.append(" and d.id_dept=bd.executing_department and coa.FIEscheduleId=:FIEscheduleId ");
                queryParams.put("FIEscheduleId", reportSearch.getFIEscheduleId());
            }
            if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                    && reportSearch.getFunction().getId() != -1) {
                queryStr.append("  and bd.function=:functionId ");
                queryParams.put("functionId", reportSearch.getFunction().getId());
            }
            if (reportSearch.getIncExp().equals("E")) {
                queryStr.append(" and (coa.type='E'").append(capExpCodeCond).append(") and coa.majorcode is not null  group by coa.majorCode ");
            } else
                queryStr.append(" and (coa.type='I') and coa.majorcode is not null  group by coa.majorCode ");
            if (reportSearch.getByDepartment())
                queryStr.append(" d.dept_name");
        }
        queryStr.append(" order by 3,1");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("query is " + queryStr.toString());
        queryMap.put(queryStr.toString(), queryParams);
        return queryMap;
    }

    private Map<String, Map<String, Object>> getBudgetReappQueryForMinorCodes(final ReportSearch reportSearch) {
        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> queryParams = new HashMap<>();
        final StringBuffer queryStr = new StringBuffer(1024);
        queryStr.append(" select SUBSTR(coa.glcode,1,").append(reportSearch.getMinorCodeLen())
                .append(") as accCode, sum(bdr.addition_amount- bdr.deduction_amount) as amount ,0 as isMajor ");
        if (reportSearch.getByDepartment())
            queryStr.append(",d.dept_name  as deptName ");
        queryStr.append(" from egf_budgetdetail bd , egf_budgetgroup bg,egf_budget b, chartofaccounts coa, egf_budget_reappropriation bdr,eg_wf_states wfs");
        if (reportSearch.getByDetailCode())
            queryStr.append(",eg_department d");
        else if (reportSearch.getByDepartment())
            queryStr.append(",eg_department d,chartofaccounts minorcoa ");
        queryStr.append(" where ((bg.maxcode<=coa.id and bg.mincode>=coa.id) or bg.majorcode=coa.id ) and bd.budgetgroup= bg.id  and bdr.budgetdetail=bd.id")
                .append(" and bdr.state_id=wfs.id and wfs.value='END' ");
        if (!(reportSearch.getAsOnDate().getMonth() == 2 && reportSearch.getAsOnDate().getDate() == 31)) {
            queryStr.append(" and wfs.created_date<=:asOnDate ");
            queryParams.put("asOnDate", reportSearch.getAsOnDate());
        }
        queryStr.append(" and bd.budget=b.id and b.isbere=:isBeRe and b.financialyearid=:finYearId and bd.fund=:fundId ");
        queryParams.put("finYearId", reportSearch.getFinYearId());
        queryParams.put("fundId", reportSearch.getFund().getId());
        if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                && reportSearch.getFunction().getId() != -1) {
            queryStr.append("  and bd.function=:functionId ");
            queryParams.put("functionId", reportSearch.getFunction().getId());
        }
        if (reportSearch.getByDetailCode()) {
            queryStr.append(" and d.id_dept=bd.executing_department and d.dept_name=:deptName and coa.glcode like :glcode   ");
            queryParams.put("deptName", reportSearch.getDepartment().getName());
            queryParams.put("glcode", reportSearch.getGlcode());
        } else if (reportSearch.getByDepartment()) {
            queryStr.append(" and d.id_dept=bd.executing_department and minorcoa.FIEscheduleId=:FIEscheduleId and  SUBSTR(coa.glcode,1,:minorCodeLength)=minorcoa.glcode ");
            queryParams.put("FIEscheduleId", reportSearch.getFIEscheduleId());
            queryParams.put("minorCodeLength", reportSearch.getMinorCodeLen());
        }
        queryStr.append(" and (coa.type='E'").append(capExpCodeCond).append(") group by SUBSTR(coa.glcode,1,").append(reportSearch.getMinorCodeLen()).append(")");
        if (reportSearch.getByDepartment())
            queryStr.append(" ,d.dept_name ");
        if (!reportSearch.getByDepartment()) {
            queryStr.append(" UNION ");
            queryStr.append(" select SUBSTR(coa.glcode,1,").append(reportSearch.getMajorCodeLen())
                    .append(") as accCode, sum(bdr.addition_amount- bdr.deduction_amount) as amount,1 as isMajor ");
            if (reportSearch.getByDepartment())
                queryStr.append(",bd.executing_derpartment  as deptName ");
            queryStr.append(" from egf_budgetdetail bd , egf_budgetgroup bg,egf_budget b, chartofaccounts coa,eg_wf_states wfs,egf_budget_reappropriation bdr")
                    .append(" where ((bg.maxcode<=coa.id and bg.mincode>=coa.id) or bg.majorcode=coa.id ) and bd.budgetgroup= bg.id ")
                    .append(" and bdr.budgetdetail=bd.id and bd.budget=b.id and bdr.state_id=wfs.id  and wfs.value='END' and b.isbere=:isBeRe")
                    .append(" and b.financialyearid=:finYearId and bd.fund=:fundId ");
            queryParams.put("finYearId", reportSearch.getFinYearId());
            queryParams.put("fundId", reportSearch.getFund().getId());
            if (!(reportSearch.getAsOnDate().getMonth() == 2 && reportSearch.getAsOnDate().getDate() == 31)) {
                queryStr.append(" and wfs.created_date<=:asOnDate ");
                queryParams.put("asOnDate", reportSearch.getAsOnDate());
            }
            if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                    && reportSearch.getFunction().getId() != -1) {
                queryStr.append("  and bd.function=:functionId ");
                queryParams.put("functionId", reportSearch.getFunction().getId());
            }
            queryStr.append(" and (coa.type='E'").append(capExpCodeCond).append(") group by SUBSTR(coa.glcode,1,").append(reportSearch.getMajorCodeLen()).append(")");
            if (reportSearch.getByDepartment())
                queryStr.append(" bd.executing_derpartment ");
        }
        queryStr.append(" order by 1 desc");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("query is " + queryStr.toString());
        queryMap.put(queryStr.toString(), queryParams);
        return queryMap;
    }

    public void setReportSearch(final ReportSearch reportSearch) {
    }

    public BigDecimal round(final BigDecimal value) {
        final BigDecimal val = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        return val;
    }
}