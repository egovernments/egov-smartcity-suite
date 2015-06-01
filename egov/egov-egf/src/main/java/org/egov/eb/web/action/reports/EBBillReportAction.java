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

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.eb.domain.master.bean.EBBillReportBean;
import org.egov.eb.domain.master.entity.TargetArea;
import org.egov.eb.service.master.TargetAreaService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Transactional(readOnly=true)
@ParentPackage("egov")   
public class EBBillReportAction extends SearchFormAction{

	private static final Logger	LOGGER = Logger.getLogger(EBBillReportAction.class);

	protected TargetAreaService targetAreaService;
	protected EgovPaginatedList paginatedList;
	protected StringBuffer queryStringFrom = new StringBuffer();
	protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	protected String year ;
	protected String month;
	protected String heading;
	
	protected String billingCycle;
	protected Integer ward;
	protected String code;
	protected String name;
	protected String region;
	protected Long targetArea;
	


	protected FinancialYearHibernateDAO financialYearDAO;	
	
	protected List<EBBillReportBean> ebBillDisplayList = new ArrayList<EBBillReportBean>();
	protected Map<Integer, String> monthMap = new LinkedHashMap<Integer, String> ();

	public EBBillReportAction() {
		super();
	}
	
	@Override
	public Object getModel() {
		return null;
	}
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBBillReportAction | prepare | start");
		String query = getQueryString().toString();
		StringBuffer srchQry = new StringBuffer("select "+query+"");
		StringBuffer countQry = new StringBuffer("select count(*) "+queryStringFrom+"");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBBillReportAction | prepare | srchQry >> "+ srchQry);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBBillReportAction | prepare | countQry >> "+ countQry);
		
		return new SearchQuerySQL(srchQry.toString(),countQry.toString(),null);
	}
	public void prepareNewForm() {
		super.prepare();
	
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside  Prepare ........");
	
		addDropdownData("targetAreaDropDownList", targetAreaService.getAllTargetAreas());
		addDropdownData("regionsList", VoucherHelper.TNEB_REGIONS);
		addDropdownData("financialYearsList", financialYearDAO.getAllActiveFinancialYearList());
		addDropdownData("wardsList", persistenceService.findAllBy("from Boundary where boundaryType.id in(select id from BoundaryType where name='Ward')"));
		monthMap = DateUtils.getAllMonthsWithFullNames();
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug(parameters);
		
	}

	@SkipValidation
@Action(value="/reports/eBBillReport-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "month", message = "", key = FinancialConstants.REQUIRED),
			@RequiredFieldValidator(fieldName = "year", message = "", key = FinancialConstants.REQUIRED)})
	
	@ValidationErrorPage(value=FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
	
	
	
	@SkipValidation
@Action(value="/reports/eBBillReport-ajaxSearch")
	public String ajaxSearch(){
		
		/*/HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);*/
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBBillReportAction | Search | start");
		year = (String) persistenceService.find("select finYearRange from CFinancialYear cfinancialyear where cfinancialyear.id = ? ",Long.parseLong(parameters.get("year")[0]));
		monthMap = DateUtils.getAllMonthsWithFullNames();
		month = monthMap.get(Integer.parseInt(parameters.get("month")[0]));
		super.search();
		prepareResults();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBBillReportAction | list | End");
			
		return "results";
	}
	
	private StringBuffer getQueryString() {
		
		StringBuffer queryString = new StringBuffer();
		
		String monthQry="";
		String yearQry="";
		heading = "";
		StringBuffer consumerQry= new StringBuffer("");
		StringBuffer targetAreaQry= new StringBuffer("");
		
		if(null!=parameters.get("month")[0] && !parameters.get("month")[0].equalsIgnoreCase("-1")){
			monthQry=" AND ebd.month = "+parameters.get("month")[0];
		}
		if(null!=parameters.get("year")[0] && !parameters.get("year")[0].equalsIgnoreCase("-1")){
			yearQry=" AND fy.id = "+parameters.get("year")[0];
		}
		if(null!=parameters.get("code")[0] && !parameters.get("code")[0].equalsIgnoreCase("")){
			consumerQry=consumerQry.append(" AND ebc.code = '"+parameters.get("code")[0]+"'");
		}
		if(null!=parameters.get("name")[0] && !parameters.get("name")[0].equals("")){
			consumerQry=consumerQry.append(" AND ebc.name = '"+parameters.get("name")[0]+"'"); 
		}
		if(null!=parameters.get("region")[0] && !parameters.get("region")[0].equals("")){
			heading = heading +" and Region - "+ parameters.get("region")[0];
			consumerQry=consumerQry.append(" AND ebd.region = '"+parameters.get("region")[0]+"'");
		}
		if(null!=parameters.get("ward")[0] && !parameters.get("ward")[0].equalsIgnoreCase("")){
			consumerQry=consumerQry.append(" AND ebd.wardid = "+parameters.get("ward")[0]);
			Boundary ward = (Boundary) persistenceService.find("from Boundary where id = ? ",Integer.parseInt(parameters.get("ward")[0]));
			heading = heading +" and Ward - "+ ward.getName();
		}
		if(null!=parameters.get("targetArea")[0] && !parameters.get("targetArea")[0].equals("")){
			targetAreaQry=targetAreaQry.append(" AND ebd.target_area_id = "+parameters.get("targetArea")[0]);
			TargetArea ta = (TargetArea) persistenceService.find("from TargetArea where id = ? ",Long.parseLong(parameters.get("targetArea")[0]));
			heading = heading +" and TargetArea - "+ ta.getName();
		}
		
				           
		queryString = queryString
				.append(" ebd.billdate AS billDate,ebc.code AS consumerNo,ebc.name AS accountNo,ebd.region AS region,ebd.dueDate AS dueDate,egws.code AS status," +
						" ta.name AS targetArea,bo.name AS ward,ebd.billamount AS amount,bill.billnumber AS billNo,"+
						//vh.vouchernumber AS voucherNo,payvh.vouchernumber AS paymentNo,
						" DECODE(ebd.position_id,null,'-',(SELECT DISTINCT (egu.user_name)  FROM eg_eis_employeeinfo emp,eg_user egu,eg_position egp WHERE egp.id = ebd.position_id AND "+
						" emp.pos_id  = egp.id AND emp.user_id = egu.id_user)) AS owner ," +
						" ebd.receiptno AS receiptNo ,ebd.recieptdate AS receiptDate " );
	    queryStringFrom = queryStringFrom
	    		 .append(" FROM egf_ebdetails ebd LEFT OUTER JOIN eg_billregister bill ON ebd.billid = bill.id "+
	    				 //LEFT OUTER JOIN voucherheader vh ON ebd.voucherheaderid = vh.id LEFT OUTER JOIN voucherheader payvh ON ebd.payvhid = payvh.id LEFT OUTER JOIN eg_billregistermis mis ON mis.voucherheaderid = vh.id AND bill.id = mis.billid  
						" LEFT OUTER JOIN eg_boundary bo ON ebd.wardid = bo.id_bndry LEFT OUTER JOIN egf_target_area ta ON ebd.target_area_id = ta.id "+
						" ,egf_ebconsumer ebc,financialyear fy,egw_status egws WHERE ebd.consumerno = ebc.id AND ebd.financialyearid = fy.id"+
						yearQry+"  "+consumerQry+" " +monthQry+" "+targetAreaQry+" AND egws.id = ebd.status"+
						"  ORDER BY ebd.billno,ebd.createddate,ebc.code");
	    queryString = queryString.append(queryStringFrom);
		 
	return queryString;
	
	}

	private void prepareResults() {
		
		LOGGER.debug("Entering into prepareResults");
		paginatedList = (EgovPaginatedList) searchResult;
		List<Object[]> list = paginatedList.getList();
		
		for(Object[] object : list) {
			EBBillReportBean reportBean = new EBBillReportBean();
			//reportBean.setBillDate(getDateValue(object[0]));
			reportBean.setConsumerNo(getStringValue(object[1]));
			reportBean.setAccountNo(getStringValue(object[2]));
			reportBean.setRegion(getStringValue(object[3]));
			reportBean.setDueDate(getDateValue(object[4]));
			reportBean.setStatus(getStringValue(object[5]));
			reportBean.setTargetArea(getStringValue(object[6]));
			reportBean.setWard(getStringValue(object[7]));
			reportBean.setAmount(getBigDecimalValue(object[8]));
			reportBean.setBillNo(getStringValue(object[9]));
			//reportBean.setVoucherNo(getStringValue(object[11]));
			//reportBean.setPaymentNo(getStringValue(object[12]));
			reportBean.setOwner(getStringValue(object[10]));
			reportBean.setReceiptNo(getStringValue(object[11]));
			reportBean.setReceiptDate(getDateValue(object[12]));
			
			ebBillDisplayList.add(reportBean);
		}
		paginatedList.setList(ebBillDisplayList);
		LOGGER.debug("Exiting from prepareResults");
	}

	protected String getStringValue(Object object) {
		return object != null?object.toString():"";
	}
	protected String getDateValue(Object object) {
		
		return object != null?formatter.format((Date) object):"";
	}
	private BigDecimal getBigDecimalValue(Object object) {
		return object!= null? new BigDecimal(object.toString()).setScale(2):BigDecimal.ZERO.setScale(2);
	}
	public void setTargetAreaService(TargetAreaService targetAreaService) {
		this.targetAreaService = targetAreaService;
	}
	
	public TargetAreaService getTargetAreaService() {
		return targetAreaService;
	}
	public FinancialYearHibernateDAO getFinancialYearDAO() {
		return financialYearDAO;
	}

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public List<EBBillReportBean> getEbBillDisplayList() {
		return ebBillDisplayList;
	}

	public void setEbBillDisplayList(List<EBBillReportBean> ebBillDisplayList) {
		this.ebBillDisplayList = ebBillDisplayList;
	}

	public Map<Integer, String> getMonthMap() {
		return monthMap;
	}

	public void setMonthMap(Map<Integer, String> monthMap) {
		this.monthMap = monthMap;
	}
	public EgovPaginatedList getPaginatedList() {
		return paginatedList;
	}

	public void setPaginatedList(EgovPaginatedList paginatedList) {
		this.paginatedList = paginatedList;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getBillingCycle() {
		return billingCycle;
	}

	public void setBillingCycle(String billingCycle) {
		this.billingCycle = billingCycle;
	}

	public Integer getWard() {
		return ward;
	}

	public void setWard(Integer ward) {
		this.ward = ward;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Long getTargetArea() {
		return targetArea;
	}

	public void setTargetArea(Long targetArea) {
		this.targetArea = targetArea;
	}



}
