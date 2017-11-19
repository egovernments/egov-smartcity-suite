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


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.egf.model.IEStatementEntry;
import org.egov.egf.model.Statement;
import org.egov.egf.model.StatementEntry;
import org.egov.egf.model.StatementResultObject;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class ReportService {
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    AppConfigValueService appConfigValuesService;
   //even though it is instance variable it is fine to make it prototype.
  //Minor code length is constant for implementation
    int minorCodeLength;
    List<Character> coaType = new ArrayList<Character>();
    @Autowired
    private FinancialYearHibernateDAO financialYearDAO;
  
    final static Logger LOGGER = Logger.getLogger(ReportService.class);

    public Date getPreviousYearFor(final Date date) {
        final GregorianCalendar previousYearToDate = new GregorianCalendar();
        previousYearToDate.setTime(date);
        final int prevYear = previousYearToDate.get(Calendar.YEAR) - 1;
        previousYearToDate.set(Calendar.YEAR, prevYear);
        return previousYearToDate.getTime();
    }

    public List<Fund> getFunds() {
        final Criteria voucherHeaderCriteria = persistenceService.getSession().createCriteria(
                CVoucherHeader.class);
        final List fundIdList = voucherHeaderCriteria.setProjection(
                Projections.distinct(Projections.property("fundId.id"))).list();
        if (!fundIdList.isEmpty())
            return persistenceService.getSession().createCriteria(Fund.class).add(
                    Restrictions.in("id", fundIdList)).list();
        return new ArrayList<Fund>();
    }

    //TODO- find the api for this in COA hibernate dao
    public String getGlcodeForPurposeCode(final Integer purposeId) {
        final Query query = persistenceService.getSession().createSQLQuery(
                "select majorcode from chartofaccounts where purposeid="
                        + purposeId);
        final List list = query.list();
        String glCode = "";
        if (list.get(0) != null)
            glCode = list.get(0).toString();
        return glCode;
    }

    protected String getFilterQuery(final Statement balanceSheet) {
        String query = "";
        if (balanceSheet.getDepartment() != null
                && balanceSheet.getDepartment().getId() != null
                && balanceSheet.getDepartment().getId() != 0)
            query = query + " and mis.departmentid="
                    + balanceSheet.getDepartment().getId().toString();
        if (balanceSheet.getFunction() != null
                && balanceSheet.getFunction().getId() != null
                && balanceSheet.getFunction().getId() != 0)
            query = query + " and g.functionid="
                    + balanceSheet.getFunction().getId().toString();
        /*if (balanceSheet.getFunctionary() != null
                && balanceSheet.getFunctionary().getId() != null
                && balanceSheet.getFunctionary().getId() != 0)
            query = query + " and mis.functionaryid="
                    + balanceSheet.getFunctionary().getId().toString();
        if (balanceSheet.getField() != null
                && balanceSheet.getField().getId() != null
                && balanceSheet.getField().getId() != 0)
            query = query + " and mis.divisionid="
                    + balanceSheet.getField().getId().toString();*/
        if (balanceSheet.getFund() != null
                && balanceSheet.getFund().getId() != null
                && balanceSheet.getFund().getId() != 0)
            query = query + " and v.fundid="
                    + balanceSheet.getFund().getId().toString();
        return query;
    }

    public String getFundNameForId(final List<Fund> fundList, final Integer id) {
        for (final Fund fund : fundList)
            if (id.equals(fund.getId()))
                return fund.getName();
        return "";
    }

    public String getfundList(final List<Fund> fundList) {
        final StringBuffer fundId = new StringBuffer();
        fundId.append("(");
        for (final Fund fund : fundList)
            fundId.append(fund.getId()).append(",");
        fundId.setLength(fundId.length() - 1);
        fundId.append(")");
        return fundId.toString();
    }

    public BigDecimal divideAndRound(BigDecimal value, final BigDecimal divisor) {
        value = value.divide(divisor, 2, BigDecimal.ROUND_HALF_UP);
        return value;
    }

    protected String getTransactionQuery(final Statement balanceSheet) {
        String query = "";
        if (balanceSheet.getDepartment() != null
                && balanceSheet.getDepartment().getId() != null
                && balanceSheet.getDepartment().getId() != 0)
            query = query + " and ts.departmentid="
                    + balanceSheet.getDepartment().getId().toString();
        if (balanceSheet.getFunction() != null
                && balanceSheet.getFunction().getId() != null
                && balanceSheet.getFunction().getId() != 0)
            query = query + " and ts.functionid="
                    + balanceSheet.getFunction().getId().toString();
/*        if (balanceSheet.getFunctionary() != null
                && balanceSheet.getFunctionary().getId() != null
                && balanceSheet.getFunctionary().getId() != 0)
            query = query + " and ts.functionaryid="
                    + balanceSheet.getFunctionary().getId().toString();
        if (balanceSheet.getField() != null
                && balanceSheet.getField().getId() != null
                && balanceSheet.getField().getId() != 0)
            query = query + " and ts.divisionid="
                    + balanceSheet.getField().getId().toString();*/
        if (balanceSheet.getFund() != null
                && balanceSheet.getFund().getId() != null
                && balanceSheet.getFund().getId() != 0)
            query = query + " and ts.fundid="
                    + balanceSheet.getFund().getId().toString();
        return query;
    }

    public String getFormattedDate(final Date date) {
        final SimpleDateFormat formatter = Constants.DDMMYYYYFORMAT1;
        return formatter.format(date);
    }
    
    public String getFormattedDate2(final Date date) {
        final SimpleDateFormat formatter = Constants.DDMMYYYYFORMAT2;
        return formatter.format(date);
    }
    
//TODO- Use the common method instead
    public String getAppConfigValueFor(final String module, final String key) {
        try {
            return appConfigValuesService
                    .getConfigValuesByModuleAndKey(module, key).get(0)
                    .getValue();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            new ValidationException(Arrays.asList(new ValidationError(key
                    + "is not defined in appconfig", key
                    + "is not defined in appconfig")));
        }
        return "";
    }

    void addFundAmount(final List<Fund> fundList, final Statement type, final BigDecimal divisor,
            final StatementResultObject row) {
        for (int index = 0; index < type.size(); index++) {
            final BigDecimal amount = divideAndRound(row.getAmount(), divisor);
            if (type.get(index).getGlCode() != null
                    && row.getGlCode().equals(type.get(index).getGlCode()))
                type.get(index).getFundWiseAmount().put(
                        getFundNameForId(fundList, Integer.valueOf(row
                                .getFundId())), amount);
        }
    }

    void addFundAmountIE(final List<Fund> fundList, final Statement type, final BigDecimal divisor,
            final StatementResultObject row) {
        for (int index = 0; index < type.sizeIE(); index++) {
            final BigDecimal amount = divideAndRound(row.getAmount(), divisor);

            if (type.getIE(index).getGlCode() != null && row.getGlCode().equals(type.getIE(index).getGlCode()))
                type.getIE(index).getNetAmount().put(getFundNameForId(fundList, Integer.valueOf(row.getFundId())), amount);
        }
    }

    List<StatementResultObject> getRowWithGlCode(
            final List<StatementResultObject> results, final String glCode) {
        final List<StatementResultObject> resultList = new ArrayList<StatementResultObject>();
        for (final StatementResultObject balanceSheetQueryObject : results)
            if (glCode.equalsIgnoreCase(balanceSheetQueryObject.getGlCode())
                    && balanceSheetQueryObject.getAmount().compareTo(BigDecimal.ZERO) != 0)
                resultList.add(balanceSheetQueryObject);
        return resultList;
    }

    protected abstract void addRowsToStatement(Statement balanceSheet,
            Statement assets, Statement liabilities);

    protected List<StatementResultObject> getAllGlCodesFor(
            final String scheduleReportType) {
        final Query query = persistenceService.getSession()
                .createSQLQuery(
                        "select distinct coa.majorcode as glCode,s.schedule as scheduleNumber,"
                                + "s.schedulename as scheduleName,coa.type as type from chartofaccounts coa, schedulemapping s "
                                + "where s.id=coa.scheduleid and coa.classification=2 and s.reporttype = '"
                                + scheduleReportType
                                + "' order by coa.majorcode").addScalar(
                                        "glCode").addScalar("scheduleNumber").addScalar(
                                                "scheduleName").addScalar("type").setResultTransformer(
                                                        Transformers.aliasToBean(StatementResultObject.class));
        return query.list();
    }

    List<StatementResultObject> getTransactionAmount(final String filterQuery,
            final Date toDate, final Date fromDate, final String coaType, final String subReportType) {
    	String    voucherStatusToExclude = getAppConfigValueFor("EGF",
                "statusexcludeReport");
        
        final Query query = persistenceService.getSession()
                .createSQLQuery(
                        "select c.majorcode as glCode,v.fundid as fundId,c.type as type,sum(debitamount)-sum(creditamount) as amount"
                                + " from generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where v.id=mis.voucherheaderid and "
                                + "v.id=g.voucherheaderid and c.type in("
                                + coaType
                                + ") and c.id=g.glcodeid and v.status not in("
                                + voucherStatusToExclude
                                + ")  AND v.voucherdate <= '"
                                + getFormattedDate(toDate)
                                + "' and v.voucherdate >='"
                                + getFormattedDate(fromDate)
                                + "' and substr(c.glcode,1,"
                                + minorCodeLength
                                + ") in "
                                + "(select distinct coa2.glcode from chartofaccounts coa2, schedulemapping s where s.id=coa2.scheduleid and "
                                + "coa2.classification=2 and s.reporttype = '"
                                + subReportType
                                + "') "
                                + filterQuery
                                + " group by c.majorcode,v.fundid,c.type order by c.majorcode")
                                .addScalar("glCode").addScalar("fundId",BigDecimalType.INSTANCE).addScalar("type")
                                .addScalar("amount",BigDecimalType.INSTANCE).setResultTransformer(
                                        Transformers.aliasToBean(StatementResultObject.class));
        return query.list();
    }

    protected Map<String, String> getSubSchedule(final String subReportType) {
        final Map<String, String> scheduleNumberToName = new HashMap<String, String>();
        final List<Object[]> rows = persistenceService.getSession()
                .createSQLQuery(
                        "select s.schedule,sub.subschedulename from egf_subschedule sub,schedulemapping s "
                                + "where sub.reporttype='"
                                + subReportType
                                + "' and sub.SUBSCHNAME=s.REPSUBTYPE").list();
        for (final Object[] row : rows)
            scheduleNumberToName.put(row[0].toString(), row[1].toString());
        return scheduleNumberToName;
    }

    public Date getFromDate(final Statement statement) {
        CFinancialYear financialYear = null;
        if ("Date".equalsIgnoreCase(statement.getPeriod())
                && statement.getAsOndate() != null) {
            final String financialYearId = financialYearDAO.getFinancialYearId(getFormattedDate2(statement.getAsOndate()));
            financialYear = financialYearDAO
                    .getFinancialYearById(Long.valueOf(financialYearId));
            statement.setFinancialYear(financialYear);
        } else
            financialYear = statement.getFinancialYear();
        return financialYear.getStartingDate();
       }

    public Date getToDate(final Statement statement) {
        if ("Date".equalsIgnoreCase(statement.getPeriod())
                && statement.getAsOndate() != null)
            return statement.getAsOndate();
        if ("Half Yearly".equalsIgnoreCase(statement.getPeriod())) {
            final String halfYearly = getAppConfigValueFor("EGF",
                    "bs_report_half_yearly");
            final String[] halfYearComponents = halfYearly.split("/");
            final Calendar fin = Calendar.getInstance();
            fin.setTime(statement.getFinancialYear().getStartingDate());
            final Calendar calendar = Calendar.getInstance();
            calendar.set(fin.get(Calendar.YEAR), Integer
                    .parseInt(halfYearComponents[1]) - 1, Integer
                    .parseInt(halfYearComponents[0]));
            return calendar.getTime();
        }
        return statement.getFinancialYear().getEndingDate();
        
    }

    void addFundAmount(final StatementEntry entry, final Map<String, BigDecimal> fundTotals) {
        for (final Entry<String, BigDecimal> row : entry.getFundWiseAmount()
                .entrySet()) {
            final String key = row.getKey();
            if (!fundTotals.containsKey(key))
                fundTotals.put(key, BigDecimal.ZERO);
            fundTotals.put(key, row.getValue().add(fundTotals.get(key)));
        }
    }

    void addTotalRowToPreviousGroup(final List<StatementEntry> list,
            final Map<String, String> schedueNumberToNameMap, final StatementEntry entry) {
        list.add(new StatementEntry("", schedueNumberToNameMap.get(entry
                .getScheduleNo()), "", null, null, true));
    }

    void addTotalRowToPreviousGroupIE(final List<IEStatementEntry> list,
            final Map<String, String> schedueNumberToNameMap, final IEStatementEntry entry) {
        list.add(new IEStatementEntry("", schedueNumberToNameMap.get(entry
                .getScheduleNo()), true));
    }

    void removeFundsWithNoDataIE(final Statement statement) {
        final Map<String, Boolean> fundToBeRemoved = new HashMap<String, Boolean>();
        for (final Fund fund : statement.getFunds())
            fundToBeRemoved.put(fund.getName(), Boolean.TRUE);
        for (final Iterator<Fund> fund = statement.getFunds().iterator(); fund
                .hasNext();) {
            final Fund next = fund.next();
            for (final IEStatementEntry balanceSheetEntry : statement.getIeEntries())
                if (balanceSheetEntry.getNetAmount().containsKey(
                        next.getName()) || balanceSheetEntry.getPreviousYearAmount().containsKey(
                                next.getName()))
                    fundToBeRemoved.put(next.getName(), Boolean.FALSE);
            if (fundToBeRemoved.get(next.getName()).booleanValue())
                fund.remove();
        }

    }

    void removeFundsWithNoData(final Statement statement) {
        final Map<String, Boolean> fundToBeRemoved = new HashMap<String, Boolean>();
        for (final Fund fund : statement.getFunds())
            fundToBeRemoved.put(fund.getName(), Boolean.TRUE);
        for (final Iterator<Fund> fund = statement.getFunds().iterator(); fund
                .hasNext();) {
            final Fund next = fund.next();
            for (final StatementEntry balanceSheetEntry : statement.getEntries())
                if (balanceSheetEntry.getFundWiseAmount().containsKey(
                        next.getName()))
                    fundToBeRemoved.put(next.getName(), Boolean.FALSE);
            if (fundToBeRemoved.get(next.getName()).booleanValue())
                fund.remove();
        }
    }

    protected void populateSchedule(final Statement statement, final String reportSubType) {
        //TODO change the query parameter
        final Query query = persistenceService.getSession()
                .createSQLQuery(
                        "select c.majorcode,s.schedulename,s.schedule from chartofaccounts c,schedulemapping s "
                                + "where s.id=c.scheduleid and s.reporttype = '"
                                + reportSubType
                                + "' and c.type in('A','L') group by c.majorcode,s.schedulename,s.schedule ORDER BY c.majorcode");
                              //  .setParameter("coaType", coaType);
        //TODO- change the query
        final List<Object[]> scheduleList = query.list();
        for (final Object[] obj : scheduleList)
            for (int index = 0; index < statement.size(); index++) {
                if (obj[0] == null)
                    obj[0] = "";
                if (statement.get(index).getGlCode() != null
                        && obj[0].toString().equals(
                                statement.get(index).getGlCode())) {
                    statement.get(index).setAccountName(obj[1].toString());
                    statement.get(index).setScheduleNo(obj[2].toString());
                }
            }
    }

    protected BigDecimal zeroOrValue(final BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    protected void computeCurrentYearTotals(final Statement statement, final String type1,
            final String type2) {
        for (final StatementEntry balanceSheetEntry : statement.getEntries()) {
            if (type1.equals(balanceSheetEntry.getAccountName())
                    || type2.equals(balanceSheetEntry.getAccountName())
                    || balanceSheetEntry.isDisplayBold())
                continue;
            BigDecimal currentYearTotal = BigDecimal.ZERO;
            for (final Entry<String, BigDecimal> entry : balanceSheetEntry
                    .getFundWiseAmount().entrySet())
                currentYearTotal = currentYearTotal.add(entry.getValue());
            balanceSheetEntry.setCurrentYearTotal(currentYearTotal);
        }
    }

}