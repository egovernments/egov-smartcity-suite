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

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.eb.domain.master.bean.EBBillReportBean;
import org.egov.eb.utils.EBUtils;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.utils.FinancialConstants;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Transactional(readOnly=true)
public class EBSchedulerReportAction extends EBBillReportAction{
	
	private static final Logger	LOGGER = Logger.getLogger(EBSchedulerReportAction.class);
	@SkipValidation
@Action(value="/reports/eBSchedulerReport-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	
	@Override
	public Object getModel() {
		return null;
	}

	public void prepareNewForm() 
	{
		super.prepareNewForm();
		addDropdownData("billingCycles", EBUtils.TNEB_BILLING_TYPES);  
	}
	
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "billingCycle", message = "", key = FinancialConstants.REQUIRED)})
	
	@ValidationErrorPage(value=FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
	
	@SkipValidation
	public String Search(){
		
		/*HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);*/
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBSchedulerReportAction | Search | start");
		super.search();
		prepareResults();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBSchedulerReportAction | list | End");
		prepareNewForm();	
			
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBSchedulerReportAction | prepare | start");
		StringBuffer query = getQueryString();
		StringBuffer srchQry = query;
		StringBuffer countQry = new StringBuffer("select count(*) from ( "+query+")");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBSchedulerReportAction | prepare | srchQry >> "+ srchQry);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBSchedulerReportAction | prepare | countQry >> "+ countQry);
		
		return new SearchQuerySQL(srchQry.toString(),countQry.toString(),null);
	}
	
	private StringBuffer getQueryString() {
		
		StringBuffer queryString = new StringBuffer(1000);
		
		heading = "";
		StringBuffer consumerQry= new StringBuffer("");
		
		
		if(null!=parameters.get("billingCycle")[0] && !parameters.get("billingCycle")[0].equalsIgnoreCase("")){    
			consumerQry.append(" AND con.oddorevenbilling = '"+parameters.get("billingCycle")[0]+"'");
		}
		if(null!=parameters.get("code")[0] && !parameters.get("code")[0].equalsIgnoreCase("")){    
			consumerQry.append(" AND con.code = '"+parameters.get("code")[0]+"'");
		}
		if(null!=parameters.get("name")[0] && !parameters.get("name")[0].equals("")){
			consumerQry.append(" AND con.name = '"+parameters.get("name")[0]+"'"); 
		}
		if(null!=parameters.get("region")[0] && !parameters.get("region")[0].equals("")){
			consumerQry.append(" AND con.region = '"+parameters.get("region")[0]+"'");
		}
		if(null!=parameters.get("ward")[0] && !parameters.get("ward")[0].equalsIgnoreCase("")){
			consumerQry.append(" AND con.wardid = "+parameters.get("ward")[0]);
		}
		if(null!=parameters.get("targetArea")[0] && !parameters.get("targetArea")[0].equals("")){
			consumerQry.append(" AND ta.id = "+parameters.get("targetArea")[0]);
		}
		   
				           
		queryString.append("select  distinct con.name as name,con.code,con.region,b.name as ward,ta.name as targetarea,disc.status,to_char(l.createddate,'dd/mm/yyyy') as failureDate, " +
				" disc.message from egf_ebschedulerlogdetails disc,egf_ebschedulerlog l, egf_ebconsumer con left join  " +
				" EGF_WARDTARGETAREA_MAPPING mp on  con.wardid= mp.boundaryid left join egf_target_area ta on mp.targetareaid=ta.id " +
				" left join eg_boundary b on b.id_bndry=mp.mp.boundaryid  where disc.id in  ( select d.id from " +
				" egf_ebschedulerlogdetails d,(select con1.id as dc,max(d.id) as dd from egf_ebschedulerlogdetails d ," +
				" egf_ebconsumer con1 where con1.id= d.consumer group by con1.id ) d1 where d1.dd=d.id and d.status like 'Fail%'" +
				" ) and disc.consumer=con.id and disc.schedulerlogid=l.id " +consumerQry+
				" order by to_char(l.createddate,'dd/mm/yyyy') desc,con.region,con.name ");
	return queryString;
	
	}

	private void prepareResults() {
		
		LOGGER.debug("Entering into prepareResults");
		paginatedList = (EgovPaginatedList) searchResult;
		List<Object[]> list = paginatedList.getList();
		
		for(Object[] object : list) {
			EBBillReportBean reportBean = new EBBillReportBean();
			reportBean.setConsumerNo(getStringValue(object[0]));
			reportBean.setAccountNo(getStringValue(object[1]));
			reportBean.setRegion(getStringValue(object[2]));
			reportBean.setStatus(getStringValue(object[5]));
			reportBean.setTargetArea(getStringValue(object[4]));
			reportBean.setWard(getStringValue(object[3]));
			reportBean.setFailureDate(getStringValue(object[6]));
			reportBean.setMessage(getStringValue(object[7]));
			ebBillDisplayList.add(reportBean);
		}
		paginatedList.setList(ebBillDisplayList);
		LOGGER.debug("Exiting from prepareResults");
	}
	
	
	
}
