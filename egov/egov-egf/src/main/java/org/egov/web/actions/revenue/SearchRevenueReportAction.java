package org.egov.web.actions.revenue;

import org.apache.struts2.convention.annotation.Action;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.egf.revenue.Grant;
import org.egov.infra.admin.master.entity.Department;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;

public class SearchRevenueReportAction extends BaseRevenueAction {
/**
	 * 
	 */
private static final long serialVersionUID = 2577746024306261450L;
private static final Logger	LOGGER	= Logger.getLogger(SearchRevenueReportAction.class);
private String jasperpath ="/reports/templates/RevenueReport.jasper";
private ReportHelper reportHelper;
private InputStream inputStream;
private List<String> grantTypeList;	
private Long finYearId;
private String deptId;
private String grantTypeStr;



	public void prepare(){
		grantTypeList=new ArrayList<String>();
		grantTypeList.add(Constants.GRANT_TYPE_CFC);
		grantTypeList.add(Constants.GRANT_TYPE_ET);
		grantTypeList.add(Constants.GRANT_TYPE_SD);
		grantTypeList.add(Constants.GRANT_TYPE_SFC);
		addDropdownData("finanYearList", persistenceService.findAllBy("from CFinancialYear  where isActive=1 order by finYearRange desc"));
		addDropdownData("grtTypeList",grantTypeList);
		addDropdownData("deptList", persistenceService.findAllBy("from Department order by deptName "));
		
	}
@SkipValidation
@Action(value="/revenue/searchRevenueReport-beforeSearch")
public String beforeSearch() {
	return "view";
}

@SuppressWarnings("unchecked")
@SkipValidation
public String search() {
	StringBuffer query = new StringBuffer();
	if(LOGGER.isInfoEnabled())     LOGGER.info("Search Query:-"+"Financials Year id"+ finYearId);
	query.append("select distinct gr.financialYear ,gr.grantType,gr.department From Grant gr where gr.financialYear.id="+this.finYearId);
	if(!this.deptId.equals("-1") && !this.grantTypeStr.equals("-1")){
		query.append(" and gr.grantType='"+this.grantTypeStr+"' and gr.department.id='"+this.deptId+"'");
	}
	else{
		if(!this.grantTypeStr.equals("-1"))
			query.append(" and gr.grantType='"+this.grantTypeStr+"'");
		if(!this.deptId.equals("-1"))
			query.append(" and gr.department.id='"+this.deptId+"'");
	}
	 List<Object[]> findAllBy = persistenceService.findAllBy(query.toString());
	 grantsList=new ArrayList<Grant>();
	//this loop needs to be replaced by query using hibernate facilities
	 for(Object[] ob:findAllBy)
	{
		Grant grant2;
		CFinancialYear fy=(CFinancialYear) ob[0];
		String type=(String) ob[1];
		Department dept=(Department)ob[2];
		grant2 = new Grant();
		grant2.setFinancialYear(fy);
		grant2.setDepartment(dept);
		grant2.setGrantType(type);
		grantsList.add(grant2);
	}
	return "view";      
}
                      
public Long getFinYearId() {
	return finYearId;
}
public void setFinYearId(Long finYearId) {
	this.finYearId = finYearId;
}
public List<String> getGrantTypeList() {
	return grantTypeList;
}
public void setGrantTypeList(List<String> grantTypeList) {
	this.grantTypeList = grantTypeList;
}
public String getGrantTypeStr() {
	return grantTypeStr;
}
public void setGrantTypeStr(String grantTypeStr) {
	this.grantTypeStr = grantTypeStr;
}
public ReportHelper getReportHelper() {
	return reportHelper;
}
public void setReportHelper(ReportHelper reportHelper) {
	this.reportHelper = reportHelper;
}
public InputStream getInputStream() {
	return inputStream;
}
public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
}
public String getDeptId() {
	return deptId;
}
public void setDeptId(String deptId) {
	this.deptId = deptId;
}

}
