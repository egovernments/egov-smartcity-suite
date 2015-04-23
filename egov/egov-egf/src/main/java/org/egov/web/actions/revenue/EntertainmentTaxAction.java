package org.egov.web.actions.revenue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.CFinancialYear;
import org.egov.egf.revenue.Grant;
import org.egov.infra.admin.master.entity.Department;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.hibernate.Query;

@Results(value={
		@Result(name="PDF",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=EntertainmentTax.pdf"}),
		@Result(name="XLS",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=EntertainmentTax.xls"})
})
public class EntertainmentTaxAction extends BaseRevenueAction {
	
	private String jasperpath ="/reports/templates/EntertainmentReport.jasper";
	private ReportHelper reportHelper;
	private InputStream inputStream;
	
	public void prepare(){
		super.prepare();
		periodList = new ArrayList<String>();
		periodList.add(Constants.PERIOD_QUARTER1);
		periodList.add(Constants.PERIOD_QUARTER2);
		periodList.add(Constants.PERIOD_QUARTER3);
		periodList.add(Constants.PERIOD_QUARTER4);
		setGrantsType(Constants.GRANT_TYPE_ET);
	}
	public String getUlbName() {
		Query query = HibernateUtil.getCurrentSession().createSQLQuery(
				"select name from companydetail");
		List<String> result = query.list();
		if (result != null)
			return result.get(0);
		return "";
	} 
	
	Map<String, Object> getParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String header = "";
		paramMap.put("ulbName", getUlbName());	
		paramMap.put("heading", header);
		return paramMap;
	}
	
	public String exportPdf() throws JRException, IOException {
		
		List<Object> dataSource = new ArrayList<Object>();
		for (Grant row : grantsList) {
			if(row.getDepartment().getId()!=null){
			for(Department dep:departmentList){
				if(dep.getId().equals(row.getDepartment().getId())){
					row.setDepartment(dep);
				}
			}
			}
			for(CFinancialYear fin:finYearList){
				if(fin.getId().equals(row.getFinancialYear().getId())){
					row.setFinancialYear(fin);
				}
			}
			dataSource.add(row);
		}
		setInputStream(reportHelper.exportPdf(getInputStream(), jasperpath,
				getParamMap(), dataSource));
		return "PDF";
	}

	public String exportXls() throws JRException, IOException {
		List<Object> dataSource = new ArrayList<Object>();
		for (Grant row : grantsList) {
			if(row.getDepartment().getId()!=null){
			for(Department dep:departmentList){
				if(dep.getId().equals(row.getDepartment().getId())){
					row.setDepartment(dep);
				}
			}
			}
			for(CFinancialYear fin:finYearList){
				if(fin.getId().equals(row.getFinancialYear().getId())){
					row.setFinancialYear(fin);
				}
			}
			dataSource.add(row);
		}
		setInputStream(reportHelper.exportXls(getInputStream(), jasperpath,
				getParamMap(), dataSource));
		return "XLS";
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

}
