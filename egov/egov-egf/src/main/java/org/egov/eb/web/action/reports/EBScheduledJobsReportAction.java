package org.egov.eb.web.action.reports;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.displaytag.pagination.PaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.SearchFormAction;
import org.hibernate.FlushMode;

public class EBScheduledJobsReportAction extends SearchFormAction {

	
	private static final Logger	LOGGER = Logger.getLogger(EBSchedulerReportAction.class);
	private boolean isDebugEnabled= LOGGER.isDebugEnabled();
	private boolean isInfoEnabled= LOGGER.isInfoEnabled();
	private  String searchQuery="select log from EbSchedulerLog log order by id desc";
	private String countQuery="select count(*) from EbSchedulerLog order by id desc";
	protected PaginatedList ebBillDisplayList;
	
	@Override
	public Object getModel() {
		
		return null;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBBillReportAction | prepare | start");
		return new SearchQueryHQL(searchQuery,countQuery,null);
		
	}
	
	
	@SkipValidation
@Action(value="/reports/eBScheduledJobsReport-search")
	public String search(){
		
	/*HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);*/
		if(isDebugEnabled)  LOGGER.debug("EBScheduledJobsReportAction | Search | start");
		super.search();
		prepareResults();
		if(isDebugEnabled)     LOGGER.debug("EBScheduledJobsReportAction | list | End");
		return FinancialConstants.STRUTS_RESULT_PAGE_RESULT;
	}

	private void prepareResults() {
	
		ebBillDisplayList = searchResult;
	}

	public PaginatedList getEbBillDisplayList() {
		return ebBillDisplayList;
	}

}
