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
package org.egov.eb.web.action.reports;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.displaytag.pagination.PaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.SearchFormAction;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly=true)
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
