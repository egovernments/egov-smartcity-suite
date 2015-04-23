package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.services.recoveries.RecoveryService;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.SearchFormAction;
import org.hibernate.Query;


@ParentPackage("egov")
public class AutoRemittanceSchedulerReportAction extends SearchFormAction{

	private static final long serialVersionUID = 1L;
	private static final String[] REMITTANCE_SCHEDULER_SCHEDULAR_TYPE={"Auto", "Manual", "Both"};
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private AutoRemittanceSchedulerReportBean reportBean;
	private RecoveryService recoveryService;
	private String recoveryId;
	private String schedulerType;
	private Date runDateFrom;
	private Date runDateTo;
	private Boolean nextRunDate;
	private static final int PAGE_SIZE = 30;
	private List<Object> paramList;
	StringBuilder dynQuery = new StringBuilder(800);
	private static Logger LOGGER=Logger.getLogger(AutoRemittanceSchedulerReportAction.class);
	private Map recoveryMap = new TreeMap();
	
@Action(value="/report/autoRemittanceSchedulerReport-newform")
	public String newform(){
		return NEW;
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String query = getSearchQuery();
		String countQry = "select count(*) from  RemittanceSchedulerLog  as s "  + dynQuery + " ";
		setPageSize(PAGE_SIZE);
		return new SearchQueryHQL(query, countQry, paramList);
	}

	private String getSearchQuery() {
		paramList = new ArrayList<Object>();
		String queryBeginning = " ";
		queryBeginning = "select distinct (SELECT COUNT(sp.schId)  from RemittanceSchedulePayment sp  WHERE sp.schId = s.id) AS COUNT, " + 
				"s.schJobName,s.lastRunDate, s.schType, s.glcode, s.status, s.remarks,s.id from RemittanceSchedulerLog as s ";
		
		if(schedulerType.equals("Auto")) { 
			dynQuery.append(" where  s.schType=?");
			paramList.add(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_AUTO);
		}
		else if(schedulerType.equals("Manual")) {
			dynQuery.append(" where s.schType=?");
			paramList.add(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_MANUAL);
		}
		else {
			dynQuery.append(" where s.schType=? or s.schType=?");
			paramList.add(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_AUTO);
			paramList.add(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_MANUAL);
		}
		
		if(StringUtils.isNotEmpty(recoveryId)) {
			dynQuery.append(" and  s.glcode=?");
			paramList.add(recoveryId);
		}
		
		if(runDateFrom!=null) {
			dynQuery.append(" and  s.lastRunDate>=to_date(?,'dd/MM/yyyy')");
			paramList.add(DDMMYYYYFORMATS.format(runDateFrom));
		}
		
		if(runDateTo!=null) {
			dynQuery.append(" and  s.lastRunDate<=to_date(?,'dd/MM/yyyy')");
			paramList.add(DDMMYYYYFORMATS.format(runDateTo));
		}
		
		dynQuery.append(" order by lastRunDate desc");
		return queryBeginning + dynQuery.toString()  ;
	}
	
	public String searchList() {
		super.search();
		populateResult(searchResult.getList());
	HibernateUtil.getCurrentSession().put("searchResult", searchResult);
		return NEW;
	}
	
	private void populateResult(List searchResult) {
		List srchList = new ArrayList();
		Iterator itr = searchResult.iterator();
		while (itr.hasNext()) {
			reportBean = new AutoRemittanceSchedulerReportBean();
			Object[] row = (Object[]) itr.next();
			reportBean.setNumberOfPayments(((Long) row[0]).toString());
			reportBean.setRunDate((Date) row[2]);
			Character schType = (Character) row[3];
			String schStringType = null;
			if (schType == 'A') {
				schStringType = "Auto";
			} else if (schType == 'M') {
				schStringType = "Manual";
			}
			reportBean.setScheduleType(schStringType);
			String glcode = (String) row[4];
			String recCoa = "";
			if (StringUtils.isNotEmpty(glcode)) {
				CChartOfAccounts ca = new CommonsService()
						.getCChartOfAccountsByGlCode(glcode);
				recCoa = ca.getGlcode() + "-" + ca.getName();
			}
			reportBean.setRecoveryCoa(recCoa);
			String stat = (String) row[5];
			if (stat.equalsIgnoreCase("success")) {
				reportBean.setStatus("Completed");
				reportBean.setRemarks("Success");
			} else {
				reportBean.setStatus(stat);
				reportBean.setRemarks((String) row[6] == null ? (String) row[6]	: "");
			}
			
			srchList.add(reportBean);
		}
		searchResult.clear();
		HashSet uniqueResult = new HashSet(srchList);
		searchResult.addAll(uniqueResult);
		LOGGER.info("SearchResult Full List size>>>>>>>>>>>>>>>>"
				+ searchResult);
	}

	@Override
	public void prepare() {
		super.prepare();
		getRecoveryCOA();
		addDropdownData("schedulerTypeList" , Arrays.asList(REMITTANCE_SCHEDULER_SCHEDULAR_TYPE));
	}

	
	private void getRecoveryCOA()
	{
		String queryString = "select c.glcode, c.glcode || '-' || c.name from Recovery r join r.chartofaccounts c where r.isactive=1 and r.remittanceMode='A'  order by c.glcode ";
		Query query =HibernateUtil.getCurrentSession().createQuery(queryString);
		List chartList = query.list();
		Iterator itr = chartList.iterator();
		while(itr.hasNext()) 
		{
			Object[] row = (Object[]) itr.next();
			recoveryMap.put(row[0], row[1]);
		}
	}
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RecoveryService getRecoveryService() {
		return recoveryService;              
	}
	
	public void setRecoveryService(RecoveryService recoveryService) {
		this.recoveryService = recoveryService;
	}

	public String getRecoveryId() {
		return recoveryId;
	}

	public void setRecoveryId(String recoveryId) {
		this.recoveryId = recoveryId;
	}

	public String getSchedulerType() {
		return schedulerType;
	}

	public void setSchedulerType(String schedulerType) {
		this.schedulerType = schedulerType;
	}

	public Date getRunDateFrom() {
		return runDateFrom;
	}

	public void setRunDateFrom(Date runDateFrom) {
		this.runDateFrom = runDateFrom;
	}

	public Date getRunDateTo() {
		return runDateTo;
	}

	public void setRunDateTo(Date runDateTo) {
		this.runDateTo = runDateTo;
	}

	public Boolean getNextRunDate() {
		return nextRunDate;
	}

	public void setNextRunDate(Boolean nextRunDate) {
		this.nextRunDate = nextRunDate;
	}

	public AutoRemittanceSchedulerReportBean getReportBean() {
		return reportBean;
	}

	public void setReportBean(AutoRemittanceSchedulerReportBean reportBean) {
		this.reportBean = reportBean;
	}

	public Map getRecoveryMap() {
		return recoveryMap;
	}

	public void setRecoveryMap(Map recoveryMap) {
		this.recoveryMap = recoveryMap;
	}
}
