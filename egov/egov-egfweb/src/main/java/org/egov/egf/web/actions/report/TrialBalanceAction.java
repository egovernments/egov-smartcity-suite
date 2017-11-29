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
package org.egov.egf.web.actions.report;

import com.exilant.eGov.src.reports.TrialBalanceBean;
import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.report.ReportBean;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Results(value = {
        @Result(name = "new", location = "trialBalance-new.jsp"),
        @Result(name = "trialBalance-PDF", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/pdf", "contentDisposition",
                "no-cache;filename=trialBalance.pdf" }),
        @Result(name = "trialBalance-XLS", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/xls", "contentDisposition",
                "no-cache;filename=trialBalance.xls" }),
        @Result(name = "trialBalance-HTML", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "text/html", "contentDisposition",
                "no-cache;filename=trialBalance.html" })
})
@ParentPackage("egov")
public class TrialBalanceAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = 6795651373310407180L;

    public class COAcomparator implements Comparator<TrialBalanceBean> {
        @Override
        public int compare(final TrialBalanceBean o1, final TrialBalanceBean o2) {
            return o1.getAccCode().compareTo(o2.getAccCode());
        }

    }

    private static final Logger LOGGER = Logger.getLogger(TrialBalanceAction.class);
    private ReportBean rb = new ReportBean();
    protected InputStream inputStream;
    private CityService cityService;
    private City cityWebsite;
    private String heading = "";
    public String reqFundId[];
    public String reqFundName[];
    public Date finStartDate;
    public Date todayDate;
    private BigDecimal totalClosingBalance = BigDecimal.ZERO;
    private BigDecimal totalOpeningBalance = BigDecimal.ZERO;
    private BigDecimal totalDebitAmount = BigDecimal.ZERO;
    private BigDecimal totalCreditAmount = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private final SimpleDateFormat mmddyyyyformatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    List<TrialBalanceBean> al = new ArrayList<TrialBalanceBean>();
    List<TrialBalanceBean> nonZeroItemsList = new ArrayList<TrialBalanceBean>();
    private ReportHelper reportHelper;
    private List<Fund> fundList;
    private Map<String, BigDecimal> fundWiseTotalMap = new LinkedHashMap<String, BigDecimal>();
    private FinancialYearDAO financialYearDAO;
    private String removeEntrysWithZeroAmount = "";

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    private Date startDate = new Date();
    private Date endDate = new Date();
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    @Autowired
    private EgovMasterDataCaching masterDataCache;

    @Override
    public Object getModel() {
        return rb;
    }

    @Override
    public void prepare()
    {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();

        addDropdownData("fundList", masterDataCache.get("egi-fund"));
        addDropdownData("departmentList", masterDataCache.get("egi-department"));
        addDropdownData("functionaryList", masterDataCache.get("egi-functionary"));
        addDropdownData("fieldList", masterDataCache.get("egi-ward"));
        addDropdownData("functionList", masterDataCache.get("egi-function"));

    }

    @Action(value = "/report/trialBalance-newForm")
    public String newForm()
    {

        return "new";
    }

    public String exportTrialBalance()
    {
        try {
            heading = generateHeading();
            cityWebsite = cityService.getCityByURL((String) getSession().get("cityurl"));
            if (rb.getExportType().equalsIgnoreCase("xls"))
            {
                inputStream = reportHelper.exportXls(inputStream,
                        reportHelper.exportTBDateRange(al, cityWebsite.getName(), rb, heading, fundList, "xls"));
                return "trialBalance-XLS";
            }
            else if (rb.getExportType().equalsIgnoreCase("pdf"))
            {
                inputStream = reportHelper.exportPdf(inputStream,
                        reportHelper.exportTBDateRange(al, cityWebsite.getName(), rb, heading, fundList, null));
                return "trialBalance-PDF";
            }
            else
            {
                inputStream = reportHelper.exportHtml(inputStream,
                        reportHelper.exportTBDateRange(al, cityWebsite.getName(), rb, heading, fundList, null), "px");
                return "new";
            }
        } catch (final JRException e) {
            LOGGER.error(e, e);
        } catch (final IOException e) {
            LOGGER.error(e, e);
        } catch (final Exception e) {
            LOGGER.error(e, e);
        }
        return "new";
    }

    @SkipValidation
    @Action(value = "/report/trialBalance-search")
    @ReadOnly
    public String search()
    {
        if (rb.getReportType().equalsIgnoreCase("daterange"))
        {
            String sDate = parameters.get("fromDate")[0];
            String eDate = parameters.get("toDate")[0];
            Date dt = new Date();
            Date dd = dt;
            try {
                dt = sdf.parse(sDate);
            } catch (ParseException e1) {

            }

            CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(dt);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                dd = sdf.parse(eDate);
            } catch (ParseException e1) {

            }
            String endFormat = formatter.format(dd);
            String endDate1 = formatter.format(finYearByDate.getEndingDate());

            if (endFormat.compareTo(endDate1) > 0)
            {
                addActionError(getText("Start Date and End Date should be in same financial year"));
                return "new";

            }
        }
        try {
            final List<AppConfigValues> configValues = appConfigValuesService.
                    getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                            FinancialConstants.REMOVE_ENTRIES_WITH_ZERO_AMOUNT_IN_REPORT);

            for (final AppConfigValues appConfigVal : configValues)
                removeEntrysWithZeroAmount = appConfigVal.getValue();
        } catch (final Exception e) {
            throw new ApplicationRuntimeException(
                    "Appconfig value for remove entries with zero amount in report is not defined in the system");
        }
        if (rb.getReportType().equalsIgnoreCase("daterange"))
        {
            getReportForDateRange();
            formatTBReport();
        }
        else
        {
            if (rb.getFundId() == null)
                fundList = masterDataCache.get("egi-fund");
            else
            {
                fundList = new ArrayList<Fund>();
                fundList.add((Fund) persistenceService.find("from Fund where id=?", rb.getFundId()));
            }
            gererateReportForAsOnDate();
        }
        if (al.size() >= 1)
            return exportTrialBalance();
        else
        {
            addActionMessage("No Data Found");
            return "new";
        }

    }

    private void gererateReportForAsOnDate()

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
        String fundcondition = "";
        List<TrialBalanceBean> forAllFunds = new ArrayList<TrialBalanceBean>();

        if (rb.getFundId() != null)
            fundcondition = " and fundid=:fundId";
        else
            fundcondition = " and fundid in (select id from fund where isactive=true and isnotleaf!=true )";
        // if(LOGGER.isInfoEnabled()) LOGGER.info("fund cond query  "+fundcondition);
        if (null != rb.getDepartmentId() || null != rb.getFunctionaryId()) {
            voucherMisTable = ",vouchermis mis ";
            misClause = " and mis.voucherheaderid=vh.id ";
        }

        if (null != rb.getDepartmentId()) {
            misDeptCond = " and mis.DEPARTMENTID= :departmentId";
            tsDeptCond = " and DEPARTMENTID= :departmentId";
        }
        if (null != rb.getFunctionaryId()) {
            functionaryCond = " and mis.FUNCTIONARYID= :functionaryId";
            tsfunctionaryCond = " and FUNCTIONARYID= :functionaryId";
        }
        if (null != rb.getFunctionId()) {
            functionIdCond = " and gl.voucherheaderid in (select distinct(voucherheaderid) from generalledger where functionid =:functionId)";
            tsFunctionIdCond = " and FUNCTIONID= functionId";
        }
        if (null != rb.getDivisionId()) {
            fieldIdCond = " and mis.divisionId= :divisionId";
            tsFieldIdCond = " and divisionId= :divisionId";
        }
        String defaultStatusExclude = null;
        final List<AppConfigValues> listAppConfVal = appConfigValuesService.
                getConfigValuesByModuleAndKey("EGF", "statusexcludeReport");
        if (null != listAppConfVal)
            defaultStatusExclude = listAppConfVal.get(0).getValue();
        else
            throw new ApplicationRuntimeException("Exlcude statusses not  are not defined for Reports");
        final String query = " SELECT gl.glcode AS \"accCode\" ,coa.name AS \"accName\" ,vh.fundid AS \"fundId\",(SUM(debitamount)+SUM((SELECT case when SUM(OPENINGDEBITBALANCE)  is null  then 0 else SUM(OPENINGDEBITBALANCE) end FROM transactionsummary WHERE"
                + " financialyearid=(SELECT id FROM financialyear WHERE startingdate<=:toDate AND endingdate>=:toDate)"
                + " AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode) AND fundid=vh.fundid"
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + "))/COUNT(*))-"
                + " (SUM(creditamount)+SUM((SELECT  case when SUM(OPENINGCREDITBALANCE)  is null  then 0 else SUM(OPENINGCREDITBALANCE) end FROM"
                + " transactionsummary WHERE financialyearid=(SELECT id FROM financialyear  WHERE startingdate<=:toDate AND endingdate>=:toDate)"
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
                + " AND  vh.voucherdate<=:toDate AND vh.voucherdate>=(SELECT startingdate FROM financialyear WHERE  startingdate<=:toDate AND   endingdate>=:toDate) "
                + fundcondition
                + " "
                + misDeptCond
                + functionaryCond
                + functionIdCond
                + fieldIdCond
                + " GROUP BY gl.glcode,coa.name,vh.fundid    HAVING (SUM(debitamount)>0 OR SUM(creditamount)>0)    And"
                + " (SUM(debitamount)+SUM((SELECT case when SUM(OPENINGDEBITBALANCE) IS NULL then 0 else SUM(OPENINGDEBITBALANCE) end FROM"
                + " transactionsummary WHERE  financialyearid=(SELECT id FROM financialyear       WHERE startingdate <=:toDate"
                + " AND endingdate >=:toDate) AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode) "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + "))/COUNT(*))-"
                + " (SUM(creditamount)+SUM((SELECT  case when SUM(OPENINGCREDITBALANCE) IS NULL then 0 else SUM(OPENINGCREDITBALANCE) end FROM"
                + " transactionsummary WHERE financialyearid=(SELECT id FROM financialyear    WHERE startingdate<=:toDate AND endingdate>=:toDate) "
                + " AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode)  "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + "))/COUNT(*) )<>0"
                + " union"
                + " SELECT coa.glcode AS \"accCode\" ,coa.name AS \"accName\" , fu.id as \"fundId\", SUM((SELECT case when SUM(OPENINGDEBITBALANCE) IS NULL then 0 else SUM(OPENINGDEBITBALANCE) end "
                + " FROM transactionsummary WHERE financialyearid=(SELECT id FROM financialyear WHERE  startingdate<=:toDate AND endingdate>=:toDate)"
                + " AND glcodeid =(SELECT id FROM chartofaccounts WHERE  glcode=coa.glcode) AND fundid= (select id from fund where id=fu.id)"
                + " "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + ")) - SUM((SELECT  case when SUM(OPENINGCREDITBALANCE) IS NULL then 0 else SUM(OPENINGCREDITBALANCE) end as \"amount\" FROM transactionsummary WHERE"
                + " financialyearid=(SELECT id FROM financialyear       WHERE startingdate<=:toDate AND endingdate>=:toDate) AND glcodeid =(SELECT id FROM chartofaccounts"
                + " WHERE glcode=coa.glcode)AND fundid= (select id from fund where id=fu.id)"
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + ")) "
                + " FROM chartofaccounts  coa, fund fu  WHERE  fu.id IN(SELECT fundid from transactionsummary WHERE financialyearid = (SELECT id FROM financialyear WHERE startingdate<=:toDate "
                + " AND endingdate>=:toDate) "
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
                + " AND vh.id=gl.voucherheaderid AND vh.fundid=fu.id AND vh.voucherdate<=:toDate AND vh.voucherdate>=(SELECT startingdate FROM financialyear WHERE  startingdate<=:toDate AND   endingdate>=:toDate) "
                + fundcondition
                + ")"
                + " GROUP BY coa.glcode,coa.name, fu.id"
                + " HAVING((SUM((SELECT case when SUM(OPENINGDEBITBALANCE) IS NULL then 0 else SUM(OPENINGDEBITBALANCE) end FROM transactionsummary WHERE"
                + " financialyearid=(SELECT id FROM financialyear       WHERE startingdate<=:toDate AND endingdate>=:toDate) AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=coa.glcode) "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond
                + tsFunctionIdCond
                + tsFieldIdCond
                + " )) >0 )"
                + " OR (SUM((SELECT  case when SUM(OPENINGCREDITBALANCE) IS NULL then 0 else SUM(OPENINGCREDITBALANCE) end FROM transactionsummary WHERE financialyearid=(SELECT id FROM financialyear WHERE startingdate<=:toDate AND endingdate>=:toDate)"
                + " AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=coa.glcode)     "
                + fundcondition
                + tsDeptCond
                + tsfunctionaryCond + tsFunctionIdCond + tsFieldIdCond + "))>0 ))  ORDER BY \"accCode\"";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("&&&query  " + query);
        try
        {
            new Double(0);
            final SQLQuery SQLQuery = persistenceService.getSession().createSQLQuery(query);
            SQLQuery.addScalar("accCode")
                    .addScalar("accName")
                    .addScalar("fundId", StringType.INSTANCE)
                    .addScalar("amount", BigDecimalType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(TrialBalanceBean.class));
            if (null != rb.getFundId())
                SQLQuery.setInteger("fundId", rb.getFundId());
            if (null != rb.getDepartmentId())
                SQLQuery.setInteger("departmentId", rb.getDepartmentId());
            if (null != rb.getFunctionaryId())
                SQLQuery.setInteger("functionaryId", rb.getFunctionaryId());
            if (null != rb.getFunctionId())
                SQLQuery.setInteger("functionId", rb.getFunctionId());
            if (null != rb.getDivisionId())
                SQLQuery.setInteger("divisionId", rb.getDivisionId());
            if (null != rb.getFromDate())
                SQLQuery.setDate("fromDate", rb.getFromDate());
            SQLQuery.setDate("toDate", rb.getToDate());
            if (LOGGER.isInfoEnabled())
                LOGGER.info("query ---->" + SQLQuery);
            forAllFunds = SQLQuery.list();

        } catch (final Exception e)
        {
            LOGGER.error("Error in getReport" + e.getMessage(), e);

        }

        for (final Fund f : fundList)
            fundWiseTotalMap.put(f.getId() + "_amount", BigDecimal.ZERO);
        // List<>
        try {
            final Map<String, TrialBalanceBean> nonDuplicateMap = new LinkedHashMap<String, TrialBalanceBean>();

            for (final TrialBalanceBean tb : forAllFunds)
                if (nonDuplicateMap.containsKey(tb.getAccCode()))
                {
                    // tb1=nonDuplicateMap.get(tb.getAccCode());

                    if (tb.getAmount().signum() == -1)
                    {
                        nonDuplicateMap.get(tb.getAccCode()).addToAmountMap(tb.getFundId() + "_amount",
                                numberToString(tb.getAmount().abs().toString()).toString() + " Cr");
                        if (nonDuplicateMap.get(tb.getAccCode()).getCreditAmount() != null)
                            nonDuplicateMap.get(tb.getAccCode()).setCreditAmount(
                                    nonDuplicateMap.get(tb.getAccCode()).getCreditAmount().add(tb.getAmount()));
                        else
                            nonDuplicateMap.get(tb.getAccCode()).setCreditAmount(tb.getAmount());
                        totalAmount = fundWiseTotalMap.get(tb.getFundId() + "_amount").subtract(tb.getAmount().abs());
                        fundWiseTotalMap.put(tb.getFundId() + "_amount", totalAmount);

                    }
                    else if (tb.getAmount().signum() == 1)
                    {
                        nonDuplicateMap.get(tb.getAccCode()).addToAmountMap(tb.getFundId() + "_amount",
                                numberToString(tb.getAmount().toString()).toString() + " Dr");
                        if (nonDuplicateMap.get(tb.getAccCode()).getDebitAmount() != null)
                            nonDuplicateMap.get(tb.getAccCode()).setDebitAmount(
                                    nonDuplicateMap.get(tb.getAccCode()).getDebitAmount().add(tb.getAmount()));
                        else
                            nonDuplicateMap.get(tb.getAccCode()).setDebitAmount(tb.getAmount());
                        totalAmount = fundWiseTotalMap.get(tb.getFundId() + "_amount").add(tb.getAmount());
                        fundWiseTotalMap.put(tb.getFundId() + "_amount", totalAmount);

                    }
                }
                else {
                    if (tb.getAmount().signum() == -1)
                    {
                        tb.addToAmountMap(tb.getFundId() + "_amount", numberToString(tb.getAmount().abs().toString()).toString()
                                + " Cr");
                        tb.setCreditAmount(tb.getAmount());
                        totalAmount = fundWiseTotalMap.get(tb.getFundId() + "_amount").subtract(tb.getAmount().abs());
                        fundWiseTotalMap.put(tb.getFundId() + "_amount", totalAmount);

                    }

                    else if (tb.getAmount().signum() == 1)
                    {
                        tb.addToAmountMap(tb.getFundId() + "_amount", numberToString(tb.getAmount().toString()).toString()
                                + " Dr");
                        tb.setDebitAmount(tb.getAmount());
                        totalAmount = fundWiseTotalMap.get(tb.getFundId() + "_amount").add(tb.getAmount());
                        fundWiseTotalMap.put(tb.getFundId() + "_amount", totalAmount);
                    }
                    nonDuplicateMap.put(tb.getAccCode(), tb);

                }
            final Collection<TrialBalanceBean> values = nonDuplicateMap.values();
            for (final TrialBalanceBean tb : values)
            {
                if (tb.getDebitAmount() != null)
                    tb.setDebit(numberToString(tb.getDebitAmount().toString()).toString() + " Dr");
                else
                    tb.setDebit("0.00");
                if (tb.getCreditAmount() != null)
                    tb.setCredit(numberToString(tb.getCreditAmount().abs().toString()).toString() + " Cr");
                else
                    tb.setCredit("0.00");
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(tb);
                if (tb.getDebitAmount() != null && tb.getCreditAmount() != null)
                {
                    final BigDecimal add = tb.getDebitAmount().subtract(tb.getCreditAmount().abs());
                    totalCreditAmount = totalCreditAmount.add(add);
                    if (add.signum() == -1)
                        tb.setAmount1(numberToString(add.abs().toString()) + " Cr");
                    else
                        tb.setAmount1(numberToString(add.toString()) + " Dr");
                } else if (tb.getDebitAmount() != null)
                    tb.setAmount1(numberToString(tb.getDebitAmount().toString()) + " Dr");
                else if (tb.getCreditAmount() != null)
                    tb.setAmount1(numberToString(tb.getCreditAmount().abs().toString()) + " Cr");
                else
                    tb.setAmount1("0.00");

            }

            al.addAll(values);
            /*
             * for(TrialBalanceBean c:al) { if(LOGGER.isInfoEnabled()) LOGGER.info("Items Before Sorting"+c); }
             */
            Collections.sort(al, new COAcomparator());

            /*
             * for(TrialBalanceBean c:al) { if(LOGGER.isInfoEnabled()) LOGGER.info("Items After Sorting"+c); }
             */
            final TrialBalanceBean tbTotal = new TrialBalanceBean();
            tbTotal.setAccCode("Total");
            for (final String key : fundWiseTotalMap.keySet())
            {
                String totalStr = "0.0";
                final BigDecimal total = fundWiseTotalMap.get(key);
                if (total != null && total.signum() == -1)
                    totalStr = numberToString(total.abs().toString()) + " Cr";
                else if (total != null && total.signum() == 1)
                    totalStr = numberToString(total.toString()) + " Dr";
                tbTotal.addToAmountMap(key, totalStr);

                if (totalCreditAmount != null && totalCreditAmount.signum() == -1)
                    totalStr = numberToString(total.abs().toString()) + " Cr";
                else if (totalCreditAmount != null && totalCreditAmount.signum() == 1)
                    totalStr = numberToString(total.toString()) + " Dr";
                tbTotal.setAmount1(totalStr);
            }

            al.add(tbTotal);

        } catch (final Exception e) {


        }

    }

    private void getReportForDateRange() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getTBReport | Getting result for Date Range");
        String voucherMisTable = "";
        String misClause = "";
        String misDeptCond = "";
        String tsDeptCond = "";
        String functionaryCond = "";
        String tsfunctionaryCond = "";
        String functionIdCond = "";
        String tsFunctionIdCond = "";
        String tsdivisionIdCond = "";
        String misdivisionIdCond = "";
        if (null != rb.getDepartmentId() || null != rb.getFunctionaryId() || null != rb.getDivisionId()) {
            voucherMisTable = ",vouchermis mis ";
            misClause = " and mis.voucherheaderid=vh.id ";
        }

        if (null != rb.getDepartmentId()) {
            misDeptCond = " and mis.DepartmentId= :departmentId";
            tsDeptCond = " and ts.DepartmentId= :departmentId";
        }
        if (null != rb.getFunctionaryId()) {
            functionaryCond = " and mis.FunctionaryId= :functionaryId";
            tsfunctionaryCond = " and ts.FunctionaryId= :functionaryId";
        }
        if (null != rb.getFunctionId()) {
            functionIdCond = " and gl.functionid =:functionId";
            tsFunctionIdCond = " and ts.FUNCTIONID= :functionId";
        }
        if (null != rb.getDivisionId()) {
            misdivisionIdCond = " and mis.divisionId= :divisionId";
            tsdivisionIdCond = " and ts.divisionId= :divisionId";
        }
        String defaultStatusExclude = null;
        final List<AppConfigValues> listAppConfVal = appConfigValuesService.
                getConfigValuesByModuleAndKey("EGF", "statusexcludeReport");
        if (null != listAppConfVal)
            defaultStatusExclude = listAppConfVal.get(0).getValue();
        else
            throw new ApplicationRuntimeException("Exlcude statusses not  are not defined for Reports");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("get Opening balance for all account codes");
        // get Opening balance for all account codes
        final String openingBalanceStr = "SELECT coa.glcode AS accCode ,coa.name  AS accName, SUM(ts.openingcreditbalance) as creditOPB,"
                +
                "sum(ts.openingdebitbalance) as debitOPB" +
                " FROM transactionsummary ts,chartofaccounts coa,financialyear fy " +
                " WHERE ts.glcodeid=coa.id  AND ts.financialyearid=fy.id and ts.FundId=:fundId " +
                tsDeptCond + tsfunctionaryCond + tsFunctionIdCond + tsdivisionIdCond +
                " AND fy.startingdate<=:fromDate AND fy.endingdate>=:toDate " +
                " GROUP BY ts.glcodeid,coa.glcode,coa.name ORDER BY coa.glcode ASC";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Query Str" + openingBalanceStr);
        final Query openingBalanceQry = persistenceService.getSession().createSQLQuery(openingBalanceStr)
                .addScalar("accCode")
                .addScalar("accName")
                .addScalar("creditOPB", BigDecimalType.INSTANCE)
                .addScalar("debitOPB", BigDecimalType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(TrialBalanceBean.class));

        openingBalanceQry.setInteger("fundId", rb.getFundId());

        if (null != rb.getDepartmentId())
            openingBalanceQry.setInteger("departmentId", rb.getDepartmentId());
        if (null != rb.getFunctionaryId())
            openingBalanceQry.setInteger("functionaryId", rb.getFunctionaryId());
        if (null != rb.getFunctionId())
            openingBalanceQry.setInteger("functionId", rb.getFunctionId());
        if (null != rb.getDivisionId())
            openingBalanceQry.setInteger("divisionId", rb.getDivisionId());
        openingBalanceQry.setDate("fromDate", rb.getFromDate());
        openingBalanceQry.setDate("toDate", rb.getToDate());
        final List<TrialBalanceBean> openingBalanceList = openingBalanceQry.list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Opening balance query ---->" + openingBalanceQry);

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
                " WHERE gl.glcodeid=coa.id and vh.id=gl.voucherheaderid  and vh.fundid=:fundId "
                + misClause
                + misDeptCond
                + functionaryCond + functionIdCond + misdivisionIdCond +
                " AND vh.voucherdate>=fy.startingdate AND vh.voucherdate<=:fromDateMinus1 " +
                " AND fy.startingdate<=:fromDate AND fy.endingdate>=:toDate" +
                " AND vh.status not in (" + defaultStatusExclude + ")" +
                " GROUP BY gl.glcodeid,coa.glcode,coa.name ORDER BY coa.glcode ASC";
        final Query tillDateOPBQry = persistenceService.getSession().createSQLQuery(tillDateOPBStr)
                .addScalar("accCode")
                .addScalar("accName")
                .addScalar("tillDateCreditOPB", BigDecimalType.INSTANCE)
                .addScalar("tillDateDebitOPB", BigDecimalType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(TrialBalanceBean.class));
        tillDateOPBQry.setInteger("fundId", rb.getFundId());

        if (null != rb.getDepartmentId())
            tillDateOPBQry.setInteger("departmentId", rb.getDepartmentId());
        if (null != rb.getFunctionaryId())
            tillDateOPBQry.setInteger("functionaryId", rb.getFunctionaryId());
        if (null != rb.getFunctionId())
            tillDateOPBQry.setInteger("functionId", rb.getFunctionId());
        if (null != rb.getDivisionId())
            tillDateOPBQry.setInteger("divisionId", rb.getDivisionId());

        tillDateOPBQry.setDate("fromDate", rb.getFromDate());
        // tillDateOPBQry.setDate("fromDate",rb.getFromDate());
        tillDateOPBQry.setDate("toDate", rb.getToDate());
        final Calendar cal = Calendar.getInstance();
        cal.setTime(rb.getFromDate());
        cal.add(Calendar.DATE, -1);
        tillDateOPBQry.setDate("fromDateMinus1", cal.getTime());
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
                " WHERE gl.glcodeid=coa.id and vh.id= gl.voucherheaderid AND  vh.fundid=:fundId "
                + misClause
                + misDeptCond
                + functionaryCond + functionIdCond + misdivisionIdCond +
                " AND vh.voucherdate>=:fromDate AND vh.voucherdate<=:toDate " +
                " AND fy.startingdate<=:fromDate AND fy.endingdate>=:toDate" +
                " AND vh.status not in (" + defaultStatusExclude + ") " +
                " GROUP BY gl.glcodeid,coa.glcode,coa.name ORDER BY coa.glcode ASC";
        final Query currentDebitCreditQry = persistenceService.getSession().createSQLQuery(currentDebitCreditStr)
                .addScalar("accCode")
                .addScalar("accName")
                .addScalar("creditAmount", BigDecimalType.INSTANCE)
                .addScalar("debitAmount", BigDecimalType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(TrialBalanceBean.class));
        currentDebitCreditQry.setInteger("fundId", rb.getFundId());
        if (null != rb.getDepartmentId())
            currentDebitCreditQry.setInteger("departmentId", rb.getDepartmentId());
        if (null != rb.getFunctionaryId())
            currentDebitCreditQry.setInteger("functionaryId", rb.getFunctionaryId());
        if (null != rb.getFunctionId())
            currentDebitCreditQry.setInteger("functionId", rb.getFunctionId());
        if (null != rb.getDivisionId())
            currentDebitCreditQry.setInteger("divisionId", rb.getDivisionId());
        currentDebitCreditQry.setDate("fromDate", rb.getFromDate());
        currentDebitCreditQry.setDate("toDate", rb.getToDate());

        final List<TrialBalanceBean> currentDebitCreditList = currentDebitCreditQry.list();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("closing balance query ---->" + currentDebitCreditQry);
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
        /*
         * for(TrialBalanceBean c:al) { if(LOGGER.isInfoEnabled()) LOGGER.info("Items Before Sorting"+c); }
         */
        Collections.sort(al, new COAcomparator());

        /*
         * for(TrialBalanceBean c:al) { if(LOGGER.isInfoEnabled()) LOGGER.info("Items After Sorting"+c); }
         */
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

        tb.setAccCode("   Total  ");
        tb.setAccName("");
        if (totalOpeningBalance.compareTo(BigDecimal.ZERO) > 0)
            tb.setOpeningBal(numberToString(totalOpeningBalance.toString()).toString() + " Dr");
        else if (totalOpeningBalance.compareTo(BigDecimal.ZERO) < 0)
        {
            totalOpeningBalance = totalOpeningBalance.abs();
            tb.setOpeningBal(numberToString(totalOpeningBalance.toString()).toString() + " Cr");
        } else
            tb.setOpeningBal("0.00");
        if (totalClosingBalance.compareTo(BigDecimal.ZERO) > 0)
            tb.setClosingBal(numberToString(totalClosingBalance.toString()).toString() + " Dr");
        else if (totalClosingBalance.compareTo(BigDecimal.ZERO) < 0)
        {
            totalClosingBalance = totalClosingBalance.abs();
            tb.setClosingBal(numberToString(totalClosingBalance.abs().toString()).toString() + " Cr");
        } else
            tb.setClosingBal("0.00");
        tb.setDebit(numberToString(totalDebitAmount.toString()).toString());
        tb.setCredit(numberToString(totalCreditAmount.toString()).toString());
        al.add(tb);
        if (removeEntrysWithZeroAmount.equalsIgnoreCase("Yes"))
            removeEntrysWithZeroAmount(al);
    }

    private void removeEntrysWithZeroAmount(final List<TrialBalanceBean> taBean) {
        for (final TrialBalanceBean trailBalance : taBean)
            if (!(trailBalance.getOpeningBal().equalsIgnoreCase("0.00") && trailBalance.getCredit().equalsIgnoreCase("0.00") &&
                    trailBalance.getDebit().equalsIgnoreCase("0.00") && trailBalance.getClosingBal().equalsIgnoreCase("0.00")))
                nonZeroItemsList.add(trailBalance);
        al = nonZeroItemsList;
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

    public InputStream getInputStream() {
        return inputStream;
    }

    private String generateHeading() {

        final StringBuffer heading = new StringBuffer(256);
        heading.append(" Trial Balance ");
        setTodayDate(new Date());
        if (rb.getFundId() != null)
        {
            heading.append(" For ");
            final String name = (String) persistenceService.find("select name from Fund where id=?", rb.getFundId());
            heading.append(name);
        } else
            heading.append(" For All Funds ");
        if (rb.getFromDate() != null)
        {
            heading.append(" From ");

            final String name = mmddyyyyformatter.format(rb.getFromDate());
            heading.append(name);

        } else
        {
            final CFinancialYear financialYearByDate = financialYearDAO.getFinancialYearByDate(rb.getToDate());
            heading.append(" From ");
            final String name = mmddyyyyformatter.format(financialYearByDate.getStartingDate());
            setFinStartDate(financialYearByDate.getStartingDate());
            heading.append(name);
        }

        if (rb.getToDate() != null)
        {
            heading.append(" To ");
            final String name = mmddyyyyformatter.format(rb.getToDate());
            heading.append(name);
        }

        if (rb.getFunctionId() != null)
        {
            heading.append(" in ");
            final String code = (String) persistenceService.find("select name from CFunction where id=?",
                    Long.valueOf(rb.getFunctionId()));
            heading.append(code);
            rb.setFunctionName(code);
        }

        if (rb.getDepartmentId() != null)
            heading.append(" For  "
                    + (String) persistenceService.find("select name from Department where id=?",
                            (rb.getDepartmentId()).longValue()));

        if (rb.getFunctionaryId() != null)
            heading.append(" For  "
                    + (String) persistenceService.find("select name from Functionary where id=?", rb.getFunctionaryId()));
        if (rb.getDivisionId() != null)
            heading.append(" For  "
                    + (String) persistenceService.find("select name from Boundary where id=?", rb.getDivisionId()));
        return heading.toString();
    }

    public ReportBean getRb() {
        return rb;
    }

    public CityService getCityWebsiteService() {
        return cityService;
    }

    public String getHeading() {
        return heading;
    }

    public List<TrialBalanceBean> getAl() {
        return al;
    }

    public ReportHelper getReportHelper() {
        return reportHelper;
    }

    public void setRb(final ReportBean rb) {
        this.rb = rb;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setCityService(final CityService cityService) {
        this.cityService = cityService;
    }

    public void setHeading(final String heading) {
        this.heading = heading;
    }

    public void setAl(final List<TrialBalanceBean> al) {
        this.al = al;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public List<Fund> getFundList() {
        return fundList;
    }

    public void setFundList(final List<Fund> fundList) {
        this.fundList = fundList;
    }

    public Map<String, BigDecimal> getFundWiseTotalMap() {
        return fundWiseTotalMap;
    }

    public void setFundWiseTotalMap(final Map<String, BigDecimal> fundWiseTotalMap) {
        this.fundWiseTotalMap = fundWiseTotalMap;
    }

    public Date getFinStartDate() {
        return finStartDate;
    }

    public void setFinStartDate(final Date finStartDate) {
        this.finStartDate = finStartDate;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(final Date todayDate) {
        this.todayDate = todayDate;
    }

    public String getRemoveEntrysWithZeroAmount() {
        return removeEntrysWithZeroAmount;
    }

    public void setRemoveEntrysWithZeroAmount(final String removeEntrysWithZeroAmount) {
        this.removeEntrysWithZeroAmount = removeEntrysWithZeroAmount;
    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public void setAppConfigValuesService(
            AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

}