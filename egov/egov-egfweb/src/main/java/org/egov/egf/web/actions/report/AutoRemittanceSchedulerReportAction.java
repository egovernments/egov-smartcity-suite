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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.egf.model.AutoRemittanceSchedulerReportBean;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.services.recoveries.RecoveryService;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;



@ParentPackage("egov")
@Results({
    @Result(name = AutoRemittanceSchedulerReportAction.NEW, location = "autoRemittanceSchedulerReport-"
            + AutoRemittanceSchedulerReportAction.NEW + ".jsp")
})
public class AutoRemittanceSchedulerReportAction extends SearchFormAction {

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;


    private static final long serialVersionUID = 1L;
    private static final String[] REMITTANCE_SCHEDULER_SCHEDULAR_TYPE = { "Auto", "Manual", "Both" };
    public static final Locale LOCALE = new Locale("en", "IN");
    public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", LOCALE);
    private AutoRemittanceSchedulerReportBean reportBean;
    @Autowired
    @Qualifier("recoveryPersistenceService")
    private RecoveryService recoveryService;
    private String recoveryId;
    private String schedulerType;
    private Date runDateFrom;
    private Date runDateTo;
    private Boolean nextRunDate;
    private static final int PAGE_SIZE = 30;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    private List<Object> paramList;
    StringBuilder dynQuery = new StringBuilder(800);
    private static Logger LOGGER = Logger.getLogger(AutoRemittanceSchedulerReportAction.class);
    private Map recoveryMap = new TreeMap();

    @Action(value = "/report/autoRemittanceSchedulerReport-newform")
    public String newform() {
        return NEW;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        final String query = getSearchQuery();
        final String countQry = "select count(*) from  RemittanceSchedulerLog  as s " + dynQuery + " ";
        setPageSize(PAGE_SIZE);
        return new SearchQueryHQL(query, countQry, paramList);
    }

    private String getSearchQuery() {
        paramList = new ArrayList<Object>();
        String queryBeginning = " ";
        queryBeginning = "select distinct (SELECT COUNT(sp.schId)  from RemittanceSchedulePayment sp  WHERE sp.schId = s.id) AS COUNT, "
                +
                "s.schJobName,s.lastRunDate, s.schType, s.glcode, s.status, s.remarks,s.id from RemittanceSchedulerLog as s ";

        if (schedulerType.equals("Auto")) {
            dynQuery.append(" where  s.schType=?");
            paramList.add(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_AUTO);
        }
        else if (schedulerType.equals("Manual")) {
            dynQuery.append(" where s.schType=?");
            paramList.add(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_MANUAL);
        }
        else {
            dynQuery.append(" where s.schType=? or s.schType=?");
            paramList.add(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_AUTO);
            paramList.add(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_MANUAL);
        }

        if (StringUtils.isNotEmpty(recoveryId)) {
            dynQuery.append(" and  s.glcode=?");
            paramList.add(recoveryId);
        }

        if (runDateFrom != null) {
            dynQuery.append(" and  s.lastRunDate>=to_date(?,'dd/MM/yyyy')");
            paramList.add(DDMMYYYYFORMATS.format(runDateFrom));
        }

        if (runDateTo != null) {
            dynQuery.append(" and  s.lastRunDate<=to_date(?,'dd/MM/yyyy')");
            paramList.add(DDMMYYYYFORMATS.format(runDateTo));
        }

        dynQuery.append(" order by lastRunDate desc");
        return queryBeginning + dynQuery.toString();
    }

    public String searchList() {
        super.search();
        populateResult(searchResult.getList());
        getSession().put("searchResult", searchResult);
        return NEW;
    }

    private void populateResult(final List searchResult) {
        final List srchList = new ArrayList();
        final Iterator itr = searchResult.iterator();
        while (itr.hasNext()) {
            reportBean = new AutoRemittanceSchedulerReportBean();
            final Object[] row = (Object[]) itr.next();
            reportBean.setNumberOfPayments(((Long) row[0]).toString());
            reportBean.setRunDate((Date) row[2]);
            final Character schType = (Character) row[3];
            String schStringType = null;
            if (schType == 'A')
                schStringType = "Auto";
            else if (schType == 'M')
                schStringType = "Manual";
            reportBean.setScheduleType(schStringType);
            final String glcode = (String) row[4];
            String recCoa = "";
            if (StringUtils.isNotEmpty(glcode)) {
                final CChartOfAccounts ca = null;// chartOfAccountsHibernateDAO.getCChartOfAccountsByGlCode(glcode);
                recCoa = ca.getGlcode() + "-" + ca.getName();
            }
            reportBean.setRecoveryCoa(recCoa);
            final String stat = (String) row[5];
            if (stat.equalsIgnoreCase("success")) {
                reportBean.setStatus("Completed");
                reportBean.setRemarks("Success");
            } else {
                reportBean.setStatus(stat);
                reportBean.setRemarks((String) row[6] == null ? (String) row[6] : "");
            }

            srchList.add(reportBean);
        }
        searchResult.clear();
        final HashSet uniqueResult = new HashSet(srchList);
        searchResult.addAll(uniqueResult);
        LOGGER.info("SearchResult Full List size>>>>>>>>>>>>>>>>"
                + searchResult);
    }

    @Override
    public void prepare() {
        super.prepare();
        getRecoveryCOA();
        addDropdownData("schedulerTypeList", Arrays.asList(REMITTANCE_SCHEDULER_SCHEDULAR_TYPE));
    }

    private void getRecoveryCOA()
    {
        final String queryString = "select c.glcode, c.glcode || '-' || c.name from Recovery r join r.chartofaccounts c where r.isactive=true and r.remittanceMode='A'  order by c.glcode ";
        final Query query = persistenceService.getSession().createQuery(queryString);
        final List chartList = query.list();
        final Iterator itr = chartList.iterator();
        while (itr.hasNext())
        {
            final Object[] row = (Object[]) itr.next();
            recoveryMap.put(row[0], row[1]);
        }
    }

    @Override
    public Object getModel() {

        return null;
    }

    public String getRecoveryId() {
        return recoveryId;
    }

    public void setRecoveryId(final String recoveryId) {
        this.recoveryId = recoveryId;
    }

    public String getSchedulerType() {
        return schedulerType;
    }

    public void setSchedulerType(final String schedulerType) {
        this.schedulerType = schedulerType;
    }

    public Date getRunDateFrom() {
        return runDateFrom;
    }

    public void setRunDateFrom(final Date runDateFrom) {
        this.runDateFrom = runDateFrom;
    }

    public Date getRunDateTo() {
        return runDateTo;
    }

    public void setRunDateTo(final Date runDateTo) {
        this.runDateTo = runDateTo;
    }

    public Boolean getNextRunDate() {
        return nextRunDate;
    }

    public void setNextRunDate(final Boolean nextRunDate) {
        this.nextRunDate = nextRunDate;
    }

    public AutoRemittanceSchedulerReportBean getReportBean() {
        return reportBean;
    }

    public void setReportBean(final AutoRemittanceSchedulerReportBean reportBean) {
        this.reportBean = reportBean;
    }

    public Map getRecoveryMap() {
        return recoveryMap;
    }

    public void setRecoveryMap(final Map recoveryMap) {
        this.recoveryMap = recoveryMap;
    }
}