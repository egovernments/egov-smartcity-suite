package org.egov.payroll.web.actions.reports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage ;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.web.actions.reports.LICDetailDTO;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;

@Results({
	@Result(name = "reportview", type = "stream", location = "inputStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" })
	})
@ParentPackage("egov")
public class DeductionReportAction extends SearchFormAction {

	private static final Logger LOGGER = Logger.getLogger(DeductionReportAction.class);
	private PayrollExternalInterface payrollExternalInterface;
	private Integer month=0;
	private String monthStr="";
	private Integer year=0;
	private String yearStr="";
	private Integer department;
	private String billNumber;
	private Integer billNumberId;
	private String billNumberHeading = "";
	private String sourcepage="";
	private InputStream inputStream;
	private String contentType;
	private String fileName;
	private BigDecimal grandTotalIncTax;
	private BigDecimal grandTotalProfTax;
	private BigDecimal grandTotalDCPSAmt;
	private BigDecimal grandTotalDCPSArr;
	private BigDecimal grandTotalDCPS;
	private ReportService reportService;
	private String deptStr;
	private static final String INCOME_TAX_DEDUCTION_TEMPLATE="incomeTaxDeduction";
	public static final String INCOME_TAX_DEDUCTION="incomeTaxDeduction";
    private static final String PROF_TAX_DEDUCTION_TEMPLATE="profTaxDeduction";
    public static final String PROF_TAX_DEDUCTION="profTaxDeduction";
    private static final String LIC_DEDUCTION_TEMPLATE="licDeduction";
    private static final String LIC_DEDUCTION_TEMPLATE_HTML="licDeductionHTML";
    public static final String INCOME_TAX_SAL_HEAD="INCTAX";
	public static final String PF_SAL_HEAD="GPFSUB";
	public static final String LIC_SAL_HEAD="LIC";
	public static final String LIC_DEDUCTION="LICDeduction";
    private static final String DCPS_DEDUCTION_TEMPLATE="dcpsDeduction";
    public static final String DCPS_DEDUCTION="dcpsDeduction";

	private static final String DEDUCTION_GIS_1="GIS-1";
	public static final String DEDUCTION_GIS_2="GIS-2";
	private static final String GIS_DEDUCTION="GISDeduction";
    private BigDecimal grandTotal;


	public Object getModel() {
		return null;
	}

	public void prepare(){
		addDropdownData("finYearList", payrollExternalInterface.getAllActivePostingFinancialYear());
		addDropdownData("departmentList", payrollExternalInterface.getAllDepartments());
		addDropdownData("billNoList",Collections.EMPTY_LIST);
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		List<Object> paramList = null;
		String query=null;
		String countQuery=null;
		if(INCOME_TAX_DEDUCTION.equalsIgnoreCase(sourcepage)) {
			Map queryParams=getEmpIncomeTaxDeductionQueryAndParams(month, year, department,billNumberId);
			paramList=(List<Object>)queryParams.get("params");
			query=(String) queryParams.get("query");
			countQuery=(String) queryParams.get("countQuery");
		}

        if(PROF_TAX_DEDUCTION.equalsIgnoreCase(sourcepage)) {
            Map queryParams=getEmpProfTaxDeductionQueryAndParams(month, year, department, billNumberId);
            paramList=(List<Object>)queryParams.get("params");
            query=(String) queryParams.get("query");
            countQuery=(String) queryParams.get("countQuery");
        }

        if(DCPS_DEDUCTION.equalsIgnoreCase(sourcepage)) {
            Map queryParams=getEmpDcpsDeductionQueryAndParams(month, year, department, billNumberId);
            paramList=(List<Object>)queryParams.get("params");
            query=(String) queryParams.get("query");
            countQuery=(String) queryParams.get("countQuery");
        }

        if(GIS_DEDUCTION.equalsIgnoreCase(sourcepage)) {
            Map queryParams=getGISDeductionQueryAndParams(month, year, department, billNumberId);
            paramList=(List<Object>)queryParams.get("params");
            query=(String) queryParams.get("query");
            countQuery=(String) queryParams.get("countQuery");
        }

		return new  SearchQuerySQL(query, countQuery, paramList);
	}

	public String getMonthName(Integer month) {
		String monthName=null;
		if (month != null && month > 0) {
			Calendar monthCal = Calendar.getInstance();
			monthCal.set(Calendar.MONTH, month-1);
			monthCal.set(Calendar.DATE,1);
			monthName = monthCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		}
		return monthName;
	}

	public String getDeptName(Integer deptId) {
		Department dept=payrollExternalInterface.getDepartment(deptId);
		if(dept!=null) {
			return dept.getDeptName().toUpperCase();
		}
		else {
			return "";
		}
	}
	public String getFinancialYearRange(Integer year) {
		String financialYearRange=null;
		if (year != null) {
			financialYearRange = payrollExternalInterface.findFinancialYearById(Long.valueOf(year)).getFinYearRange();
		}
		return financialYearRange;
	}

	public Map getEmpIncomeTaxDeductionQueryAndParams(Integer month, Integer financialYearId, Integer deptId, Integer billNumId) {
		HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
		 String billNumberCond1 = "";
		 String billNumberCond2 = "";
	        if(billNumId != 0){
	        	billNumberCond1 = " and payroll2.ID_BILLNUMBER=? ";
	        	billNumberCond2 = " and payroll.ID_BILLNUMBER=? ";
	        }
		String query="SELECT upper(empcode),upper(empname),decode(nvl(empgender,0),'F','Ms.',decode(nvl(empgender,0),'M','Mr.','')),upper(desgName),round(totalgrosspay,2),round(pf1,2)," +
				"round(lic1,2),round(incometax1,2),round(totalnetpay,2) FROM (SELECT payroll2.id_employee AS empid2, " +
				"emp.code as empcode,emp.name as empname, desig.designation_name as desgName,emp.gender as empgender," +
				"SUM(payroll2.gross_pay) AS totalgrosspay,SUM(payroll2.net_pay) AS totalnetpay FROM egpay_emppayroll payroll2 " +
				"INNER JOIN egpay_deductions deductions2 ON payroll2.id= deductions2.id_emppayroll  " +
				"INNER JOIN egpay_salarycodes salcodes2  ON salcodes2.id =deductions2.id_salcode,eg_employee emp," +
				"eg_emp_assignment assig1,eg_designation desig,egw_status status1 WHERE salcodes2.head=? AND payroll2.id_employee=emp.id  " +
				"AND payroll2.id_emp_assignment= assig1.id AND assig1.designationid=desig.designationid AND payroll2.status=status1.id " +
				"AND status1.code!=? AND payroll2.month=? AND payroll2.financialyearid=?  " +billNumberCond1+
				"AND assig1.main_dept=? GROUP BY payroll2.id_employee,emp.code,emp.name,desig.designation_name,emp.gender) payroll1 " +
				"INNER JOIN (SELECT empid AS empid1,SUM(incometax) AS incometax1,SUM(lic) AS lic1,SUM(PF) AS pf1 " +
				"FROM (SELECT payroll.id_employee AS empid,SUM(DECODE(salcodes.head,?,deductions.amount,0)) AS incometax," +
				"SUM(DECODE(salcodes.head,?,deductions.amount,0)) AS lic,SUM(DECODE(salcodes.head,?,deductions.amount,0)) AS pf " +
				"FROM egpay_emppayroll payroll LEFT OUTER JOIN egpay_deductions deductions ON payroll.id= deductions.id_emppayroll " +
				"LEFT OUTER JOIN egpay_salarycodes salcodes ON salcodes.id=deductions.id_salcode,eg_emp_assignment assig,egw_status status " +
				"WHERE payroll.id_emp_assignment=assig.id AND payroll.status=status.id AND status.code!=? AND payroll.month=? " +
				"AND financialyearid=? AND assig.main_dept=? AND salcodes.head IN (?,?,?) " +billNumberCond2+
				"GROUP BY payroll.id_employee,salcodes.head) deductionview GROUP BY empid) deductionview1 ON empid2=empid1 order by empname";


		String countQuery="SELECT count(upper(empcode)) FROM (SELECT payroll2.id_employee AS empid2, " +
			"emp.code as empcode,emp.name as empname, desig.designation_name as desgName,emp.gender as empgender," +
			"SUM(payroll2.gross_pay) AS totalgrosspay,SUM(payroll2.net_pay) AS totalnetpay FROM egpay_emppayroll payroll2 " +
			"INNER JOIN egpay_deductions deductions2 ON payroll2.id= deductions2.id_emppayroll  " +
			"INNER JOIN egpay_salarycodes salcodes2  ON salcodes2.id =deductions2.id_salcode,eg_employee emp," +
			"eg_emp_assignment assig1,eg_designation desig,egw_status status1 WHERE salcodes2.head=? AND payroll2.id_employee=emp.id  " +
			"AND payroll2.id_emp_assignment= assig1.id AND assig1.designationid=desig.designationid AND payroll2.status=status1.id " +
			"AND status1.code!=? AND payroll2.month=? AND payroll2.financialyearid=?  " +billNumberCond1+
			"AND assig1.main_dept=? GROUP BY payroll2.id_employee,emp.code,emp.name,desig.designation_name,emp.gender) payroll1 " +
			"INNER JOIN (SELECT empid AS empid1,SUM(incometax) AS incometax1,SUM(lic) AS lic1,SUM(PF) AS pf1 " +
			"FROM (SELECT payroll.id_employee AS empid,SUM(DECODE(salcodes.head,?,deductions.amount,0)) AS incometax," +
			"SUM(DECODE(salcodes.head,?,deductions.amount,0)) AS lic,SUM(DECODE(salcodes.head,?,deductions.amount,0)) AS pf " +
			"FROM egpay_emppayroll payroll LEFT OUTER JOIN egpay_deductions deductions ON payroll.id= deductions.id_emppayroll " +
			"LEFT OUTER JOIN egpay_salarycodes salcodes ON salcodes.id=deductions.id_salcode,eg_emp_assignment assig,egw_status status " +
			"WHERE payroll.id_emp_assignment=assig.id AND payroll.status=status.id AND status.code!=? AND payroll.month=? " +
			"AND financialyearid=? AND assig.main_dept=? AND salcodes.head IN (?,?,?) " +billNumberCond2+
			"GROUP BY payroll.id_employee,salcodes.head) deductionview GROUP BY empid) deductionview1 ON empid2=empid1";
			List<Object> paramList = new ArrayList<Object>();

		paramList.add(INCOME_TAX_SAL_HEAD);
		paramList.add(PayrollConstants.PAYSLIP_CANCELLED_STATUS);
		paramList.add(month);
		paramList.add(financialYearId);
		if(billNumId != 0){
        	paramList.add(billNumId);
        }
		paramList.add(deptId);
		paramList.add(INCOME_TAX_SAL_HEAD);
		paramList.add(LIC_SAL_HEAD);
		paramList.add(PF_SAL_HEAD);
		paramList.add(PayrollConstants.PAYSLIP_CANCELLED_STATUS);
		paramList.add(month);
		paramList.add(financialYearId);
		paramList.add(deptId);
		paramList.add(INCOME_TAX_SAL_HEAD);
		paramList.add(LIC_SAL_HEAD);
		paramList.add(PF_SAL_HEAD);
		if(billNumId != 0){
        	paramList.add(billNumId);
        }

		queryAndParams.put("query", query);
		queryAndParams.put("countQuery", countQuery);
		queryAndParams.put("params", paramList);

		return queryAndParams;
	}

	public Map getEmpLICDeductionQueryAndParams(Integer month, Integer financialYearId, Integer deptId,String bill) {
		HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
		String billNumQuery = " AND payroll.id_billnumber = ? ";
		String query="SELECT emp.id AS empid,upper(emp.code) AS empcode," +
				"upper(DECODE(NVL(emp.gender,0),'F','Ms. ',DECODE(NVL(emp.gender,0),'M','Mr. ',''))||emp.name) AS empname," +
				"deductions.reference_no AS policyno,deductions.amount  AS amount,0  AS total FROM egpay_emppayroll payroll " +
				"INNER JOIN egpay_deductions deductions ON payroll.id= deductions.id_emppayroll " +
				"INNER JOIN egpay_salarycodes salcodes ON deductions.id_salcode=salcodes.id, egw_status status,  eg_emp_assignment assig," +
				"eg_employee emp WHERE status.code!=? AND salcodes.head=? AND payroll.status=status.id " +
				"AND payroll.id_emp_assignment=assig.id AND payroll.id_employee=emp.id AND assig.main_dept=? AND payroll.month=? " +
				"AND payroll.financialyearid=? " +(((!bill.equalsIgnoreCase(""))&&(!bill.equalsIgnoreCase("0")))?billNumQuery:" ")+
				" UNION ALL " +
				"SELECT empid,  NULL AS empcode,  NULL AS empname,  NULL AS policyno,  0 amount,total AS total " +
				"FROM   (SELECT emp.id AS empid,SUM(deductions.amount) AS total FROM egpay_emppayroll payroll " +
				"INNER JOIN egpay_deductions deductions ON payroll.id= deductions.id_emppayroll " +
				"INNER JOIN egpay_salarycodes salcodes ON deductions.id_salcode=salcodes.id, egw_status status,eg_emp_assignment " +
				"assig,eg_employee emp WHERE status.code!=? AND salcodes.head=? AND payroll.status=status.id " +
				"AND payroll.id_emp_assignment=assig.id AND payroll.id_employee=emp.id AND assig.main_dept=? AND payroll.month=? " +
				"AND payroll.financialyearid=?" +(((!bill.equalsIgnoreCase(""))&&(!bill.equalsIgnoreCase("0")))?billNumQuery:" ")+
				" GROUP BY emp.id)  ORDER BY empid,total";

		List<Object> paramList = new ArrayList<Object>();

		paramList.add(PayrollConstants.PAYSLIP_CANCELLED_STATUS);
		paramList.add(LIC_SAL_HEAD);
		paramList.add(deptId);
		paramList.add(month);
		paramList.add(financialYearId);
		if((!bill.equalsIgnoreCase("0")) && (!bill.isEmpty()))
		{
			paramList.add(bill);
		}
		paramList.add(PayrollConstants.PAYSLIP_CANCELLED_STATUS);
		paramList.add(LIC_SAL_HEAD);
		paramList.add(deptId);
		paramList.add(month);
		paramList.add(financialYearId);
		if((!bill.equalsIgnoreCase("0")) && (!bill.isEmpty()))
		{
			paramList.add(bill);
		}

		queryAndParams.put("query", query);
		queryAndParams.put("params", paramList);

		return queryAndParams;
	}


	public Map getEmpProfTaxDeductionQueryAndParams(Integer month, Integer financialYearId, Integer deptId, Integer billNumId) {
        HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
        String billNumberCond = "";
        if(billNumId != 0){
        	billNumberCond = " and emppay.ID_BILLNUMBER=? ";
        }
        String query="SELECT emp.code AS empCode, decode(nvl(gender,0),'F','Ms. ',decode(nvl(gender,0),'M','Mr. ',''))||upper(emp.name) AS empName, SUM(emppay.gross_pay) AS grossSalary, SUM(payded.amount) AS profTax "+
		"FROM egpay_deductions payded, egpay_salarycodes salcode, egpay_emppayroll emppay, eg_employee emp, eg_emp_assignment empass, egw_status stat "+
		"WHERE payded.id_salcode    =salcode.id AND salcode.head ='PROFTAX' AND emppay.id = payded.id_emppayroll AND emp.id = emppay.id_employee "+
		"AND emppay.financialyearid = ? AND emppay.month = ? AND empass.id = emppay.id_emp_assignment AND empass.main_dept = ? and stat.id = emppay.status "+
		"and stat.moduletype = 'PaySlip' and stat.description != ? "+billNumberCond+
		"GROUP BY emp.code, emp.name, decode(nvl(gender,0),'F','Ms. ',decode(nvl(gender,0),'M','Mr. ','')) ORDER BY emp.name";


        String countQuery="SELECT count(emp.code) "+
        		"FROM egpay_deductions payded, egpay_salarycodes salcode, egpay_emppayroll emppay, eg_employee emp, eg_emp_assignment empass, egw_status stat "+
        		"WHERE payded.id_salcode    =salcode.id AND salcode.head ='PROFTAX' AND emppay.id = payded.id_emppayroll AND emp.id = emppay.id_employee "+
        		"AND emppay.financialyearid = ? AND emppay.month = ? AND empass.id = emppay.id_emp_assignment AND empass.main_dept = ? and stat.id = emppay.status "+
        		"and stat.moduletype = 'PaySlip' and stat.description! = ? "+billNumberCond;
            List<Object> paramList = new ArrayList<Object>();

        paramList.add(financialYearId);
        paramList.add(month);
        paramList.add(deptId);
        paramList.add(PayrollConstants.PAYSLIP_CANCELLED_STATUS);
        if(billNumId != 0){
        	paramList.add(billNumId);
        }

        queryAndParams.put("query", query);
        queryAndParams.put("countQuery", countQuery);
        queryAndParams.put("params", paramList);

        return queryAndParams;
    }

	public Map getEmpDcpsDeductionQueryAndParams(Integer month, Integer financialYearId, Integer deptId, Integer billNumId) {
        HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
        String billNumberCond1 = "";
        if(billNumId != 0){
        	billNumberCond1 = " and emppay.ID_BILLNUMBER=? ";
        }
        

        String query="select decode((payroll1.empCode),null,payroll2.empCode,payroll1.empCode) as empcode," +
        			"decode((payroll1.empName),null,payroll2.empName,payroll1.empName) as name,"+
        			"decode((payroll1.empdesg),null,payroll2.empdesg,payroll1.empdesg) as desig ,"+
        			"NVL(payroll1.dcpssub,'-') AS DCPSSUB,NVL(payroll1.dcpsamt,0) as DCPSAMT,NVL(payroll2.dcpsarr,0) AS DCPSARR from "+
						"((SELECT emp.code AS empCode, DECODE(NVL(gender,0),'F','Ms. ',DECODE(NVL(gender,0),'M','Mr. ',''))||upper(emp.name) AS empName, "+
						 "upper(desg.designation_name) AS empdesg, payded.reference_no   AS DCPSSUB, SUM(payded.amount) AS DCPSAMT "+
						"FROM egpay_deductions payded, egpay_salarycodes salcode, egpay_emppayroll emppay, eg_employee emp, eg_emp_assignment empass, "+
						 " egw_status stat, eg_designation desg "+
						"WHERE payded.id_salcode    =salcode.id AND salcode.head ='DCPSCURR' AND emppay.id = payded.id_emppayroll AND emp.id = emppay.id_employee "+
						"AND emppay.financialyearid = ? AND emppay.month = ? AND empass.id = emppay.id_emp_assignment AND empass.main_dept = ? AND stat.id = emppay.status "+
						"AND stat.moduletype        = 'PaySlip' AND stat.description != ? AND desg.designationid = empass.designationid "+
						" AND payded.amount is not null and payded.amount>0 "+billNumberCond1+
						"GROUP BY emp.code, emp.name, DECODE(NVL(gender,0),'F','Ms. ',DECODE(NVL(gender,0),'M','Mr. ','')), payded.reference_no, desg.designation_name "+
						"ORDER BY emp.name) payroll1 "+
						"full outer join "+
						"(SELECT emp.code AS empCode, DECODE(NVL(gender,0),'F','Ms. ',DECODE(NVL(gender,0),'M','Mr. ',''))||upper(emp.name) AS empName, "+
						 "upper(desg.designation_name) AS empdesg, payded.reference_no   AS DCPSSUB, SUM(payded.amount) AS DCPSARR "+
						"FROM egpay_deductions payded, egpay_salarycodes salcode, egpay_emppayroll emppay, eg_employee emp, eg_emp_assignment empass, "+
						 " egw_status stat, eg_designation desg "+
						"WHERE payded.id_salcode    =salcode.id AND salcode.head ='DCPSARR' AND emppay.id = payded.id_emppayroll AND emp.id = emppay.id_employee "+
						"AND emppay.financialyearid = ? AND emppay.month = ? AND empass.id = emppay.id_emp_assignment AND empass.main_dept = ? AND stat.id = emppay.status "+
						"AND stat.moduletype        = 'PaySlip' AND stat.description != ? AND desg.designationid = empass.designationid "+
						" AND payded.amount is not null and payded.amount>0 "+billNumberCond1+
						"GROUP BY emp.code, emp.name, DECODE(NVL(gender,0),'F','Ms. ',DECODE(NVL(gender,0),'M','Mr. ','')), payded.reference_no, desg.designation_name "+
						"ORDER BY emp.name) payroll2 on payroll2.empCode=payroll1.empCode)";


        String countQuery="select count(decode((payroll1.empCode),null,payroll2.empCode,payroll1.empCode)) from "+
				"((SELECT emp.code AS empCode, DECODE(NVL(gender,0),'F','Ms. ',DECODE(NVL(gender,0),'M','Mr. ',''))||upper(emp.name) AS empName, "+
				 "desg.designation_name AS empdesg, payded.reference_no   AS DCPSSUB, SUM(payded.amount) AS DCPSAMT "+
				"FROM egpay_deductions payded, egpay_salarycodes salcode, egpay_emppayroll emppay, eg_employee emp, eg_emp_assignment empass, "+
				 " egw_status stat, eg_designation desg "+
				"WHERE payded.id_salcode    =salcode.id AND salcode.head ='DCPSCURR' AND emppay.id = payded.id_emppayroll AND emp.id = emppay.id_employee "+
				"AND emppay.financialyearid = ? AND emppay.month = ? AND empass.id = emppay.id_emp_assignment AND empass.main_dept = ? AND stat.id = emppay.status "+
				"AND stat.moduletype        = 'PaySlip' AND stat.description != ? AND desg.designationid = empass.designationid "+
				" AND payded.amount is not null and payded.amount>0 "+billNumberCond1+
				"GROUP BY emp.code, emp.name, DECODE(NVL(gender,0),'F','Ms. ',DECODE(NVL(gender,0),'M','Mr. ','')), payded.reference_no, desg.designation_name "+
				"ORDER BY emp.name) payroll1 "+
				"full outer join "+
				"(SELECT emp.code AS empCode, DECODE(NVL(gender,0),'F','Ms. ',DECODE(NVL(gender,0),'M','Mr. ',''))||upper(emp.name) AS empName, "+
				 "upper(desg.designation_name) AS empdesg, payded.reference_no   AS DCPSSUB, SUM(payded.amount) AS DCPSARR "+
				"FROM egpay_deductions payded, egpay_salarycodes salcode, egpay_emppayroll emppay, eg_employee emp, eg_emp_assignment empass, "+
				 " egw_status stat, eg_designation desg "+
				"WHERE payded.id_salcode    =salcode.id AND salcode.head ='DCPSARR' AND emppay.id = payded.id_emppayroll AND emp.id = emppay.id_employee "+
				"AND emppay.financialyearid = ? AND emppay.month = ? AND empass.id = emppay.id_emp_assignment AND empass.main_dept = ? AND stat.id = emppay.status "+
				"AND stat.moduletype        = 'PaySlip' AND stat.description != ? AND desg.designationid = empass.designationid "+
				" AND payded.amount is not null and payded.amount>0 "+billNumberCond1+
				"GROUP BY emp.code, emp.name, DECODE(NVL(gender,0),'F','Ms. ',DECODE(NVL(gender,0),'M','Mr. ','')), payded.reference_no, desg.designation_name "+
				"ORDER BY emp.name) payroll2 on payroll2.empCode=payroll1.empCode)";
            List<Object> paramList = new ArrayList<Object>();

        paramList.add(financialYearId);
        paramList.add(month);
        paramList.add(deptId);
        paramList.add(PayrollConstants.PAYSLIP_CANCELLED_STATUS);
        if(billNumId != 0){
        	paramList.add(billNumId);
        }
        paramList.add(financialYearId);
        paramList.add(month);
        paramList.add(deptId);
        paramList.add(PayrollConstants.PAYSLIP_CANCELLED_STATUS);
        if(billNumId != 0){
        	paramList.add(billNumId);
        }
        LOGGER.debug("query "+ query);
        LOGGER.debug("countQuery  "+ countQuery);
        queryAndParams.put("query", query);
        queryAndParams.put("countQuery", countQuery);
        queryAndParams.put("params", paramList);

        return queryAndParams;
    }


    public Map getGISDeductionQueryAndParams(Integer month, Integer financialYearId, Integer deptId, Integer billNumId) {
        HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
        String billNumberCond = "";
        if(billNumId != 0){
        	billNumberCond = " and empprl.ID_BILLNUMBER=? ";
        }
        String query="SELECT emp.code as empCode, DECODE(NVL(emp.gender,0),'F','Ms. ',DECODE(NVL(emp.gender,0),'M','Mr. ',''))||upper(emp.name) as empName," +
        		" desgn.designation_name as designation,  emp.date_of_birth as dob," +
        		" emp.retirement_date as dor, sum(dedctn.amount) as amount FROM egpay_emppayroll empprl,  egpay_salarycodes salcd,  eg_employee emp," +
        		" egpay_deductions dedctn,  eg_emp_assignment assgn,  eg_designation desgn,  egw_status stat" +
        		" WHERE empprl.status = stat.id AND stat.code !=? AND stat.moduletype='PaySlip' AND empprl.id_employee = emp.id" +
        		" AND empprl.id_emp_assignment= assgn.id AND assgn.designationid =desgn.designationid AND empprl.id =dedctn.id_emppayroll" +
        		" AND dedctn.id_salcode = salcd.id AND salcd.head IN (?,?) AND assgn.main_dept = ?" +
        		" AND empprl.month = ? AND empprl.financialyearid  = ?" +billNumberCond+
        		" group by emp.code,emp.name, DECODE(NVL(emp.gender,0),'F','Ms. ',DECODE(NVL(emp.gender,0),'M','Mr. ','')), desgn.designation_name,  emp.date_of_birth,  emp.retirement_date ORDER BY emp.code";


        String countQuery="SELECT COUNT(empCode) FROM (SELECT emp.code as empCode " +
        		" FROM egpay_emppayroll empprl,  egpay_salarycodes salcd,  eg_employee emp," +
        		" egpay_deductions dedctn,  eg_emp_assignment assgn,  eg_designation desgn,  egw_status stat" +
        		" WHERE empprl.status = stat.id AND stat.code !=? AND stat.moduletype='PaySlip' AND empprl.id_employee = emp.id" +
        		" AND empprl.id_emp_assignment= assgn.id AND assgn.designationid =desgn.designationid AND empprl.id =dedctn.id_emppayroll" +
        		" AND dedctn.id_salcode = salcd.id AND salcd.head IN (?,?) AND assgn.main_dept = ?" +
        		" AND empprl.month = ? AND empprl.financialyearid  = ?"+billNumberCond+" GROUP BY emp.code)";

            List<Object> paramList = new ArrayList<Object>();

        paramList.add(PayrollConstants.PAYSLIP_CANCELLED_STATUS);
        paramList.add(DEDUCTION_GIS_1);
        paramList.add(DEDUCTION_GIS_2);
        paramList.add(deptId);
        paramList.add(month);
        paramList.add(financialYearId);
        if(billNumId != 0){
        	paramList.add(billNumId);
        }

        queryAndParams.put("query", query);
        queryAndParams.put("countQuery", countQuery);
        queryAndParams.put("params", paramList);

        return queryAndParams;
    }

    private List populateIncomeTaxDeduction(List empList){

		List<Object[]> retList = empList;
		List<EmpDeductionInfo> empDeductionInfoList = new ArrayList<EmpDeductionInfo>();

		for (Object[] objArray : retList){
			EmpDeductionInfo empDeductionInfo = new EmpDeductionInfo();
			empDeductionInfo.setEmpCode((String)objArray[0]);
			if(objArray[2]!=null) {
				empDeductionInfo.setEmpName((String)objArray[2]+" "+(String)objArray[1]);
			}
			else {
				empDeductionInfo.setEmpName((String)objArray[1]);
			}
			empDeductionInfo.setDesignation((String)objArray[3]);
			empDeductionInfo.setGrossSalary(((BigDecimal)objArray[4]));
			empDeductionInfo.setTotalPF((BigDecimal)objArray[5]);
			empDeductionInfo.setTotalLIC((BigDecimal)objArray[6]);
			empDeductionInfo.setTotalIncomeTax((BigDecimal)objArray[7]);
			empDeductionInfo.setNetSalary(((BigDecimal)objArray[8]));
			empDeductionInfoList.add(empDeductionInfo);
		}
		return empDeductionInfoList;
	}

    private List populateLICDeduction(List empList){

		List<Object[]> retList = empList;
		List<EmpDeductionInfo> empDeductionInfoList = new ArrayList<EmpDeductionInfo>();
		Map<BigDecimal,EmpDeductionInfo> empMap=new HashMap<BigDecimal,EmpDeductionInfo>();
		EmpDeductionInfo empDeductionInfo=null;
		for (Object[] objArray : retList){
			BigDecimal id=(BigDecimal)objArray[0];
			if(!empMap.containsKey(id)) {
				empDeductionInfo = new EmpDeductionInfo();
				empDeductionInfo.setEmpCode((String)objArray[1]);
				empDeductionInfo.setEmpName((String)objArray[2]);

				LICDetailDTO licDetailDTO=new LICDetailDTO();
				licDetailDTO.setPolicyNo((String) objArray[3]);
				licDetailDTO.setLicAmount((BigDecimal)objArray[4]);

				empDeductionInfo.getLicDetails().add(licDetailDTO);
				empMap.put(id, empDeductionInfo);
				empDeductionInfoList.add(empDeductionInfo);
			}
			else {
				BigDecimal totalLIC=(BigDecimal)objArray[5];
				if(totalLIC.compareTo(BigDecimal.ZERO)==0) {
					LICDetailDTO licDetailDTO=new LICDetailDTO();
					licDetailDTO.setPolicyNo((String) objArray[3]);
					licDetailDTO.setLicAmount((BigDecimal)objArray[4]);
					empMap.get(id).getLicDetails().add(licDetailDTO);
				}
				else {
					empMap.get(id).setTotalLIC(totalLIC);
				}
			}
		}
		return empDeductionInfoList;
	}


    private List populateProfTaxDeduction(List empList){

        List<Object[]> retList = empList;
        List<EmpDeductionInfo> empDeductionInfoList = new ArrayList<EmpDeductionInfo>();

        for (Object[] objArray : retList){
            EmpDeductionInfo empDeductionInfo = new EmpDeductionInfo();
            empDeductionInfo.setEmpCode((String)objArray[0]);
            empDeductionInfo.setEmpName((String)objArray[1]);
            empDeductionInfo.setGrossSalary(((BigDecimal)objArray[2]));
            empDeductionInfo.setProfTax(((BigDecimal)objArray[3]));
            empDeductionInfoList.add(empDeductionInfo);
        }
        return empDeductionInfoList;
    }

    private List populateDcpsDeduction(List empList){

        List<Object[]> retList = empList;
        List<EmpDeductionInfo> empDeductionInfoList = new ArrayList<EmpDeductionInfo>();

        for (Object[] objArray : retList){
            EmpDeductionInfo empDeductionInfo = new EmpDeductionInfo();
            empDeductionInfo.setEmpCode((String)objArray[0]);
            empDeductionInfo.setEmpName((String)objArray[1]);
            empDeductionInfo.setDesignation((String)objArray[2]);
            empDeductionInfo.setDcpsSubNo((String)objArray[3]);
            empDeductionInfo.setDcpsAmt(((BigDecimal)objArray[4]));
            empDeductionInfo.setDcpsArr(((BigDecimal)objArray[5]));
            empDeductionInfo.setDcpsTotal(empDeductionInfo.getDcpsAmt().add(empDeductionInfo.getDcpsArr()));
            empDeductionInfoList.add(empDeductionInfo);
        }
        return empDeductionInfoList;
    }

    private List populateGISDeduction(List empList){
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        List<Object[]> gisDeductionList = empList;
        List<EmpDeductionInfo> empDeductionInfoList = new ArrayList<EmpDeductionInfo>();
        BigDecimal temp=BigDecimal.ZERO;
        for (Object[] objArray : gisDeductionList){
            EmpDeductionInfo empDeductionInfo = new EmpDeductionInfo();
            empDeductionInfo.setEmpCode((String)objArray[0]);
            empDeductionInfo.setEmpName((String)objArray[1]);
            empDeductionInfo.setDesignation((String)objArray[2]);
            if(objArray[3]!=null)
	            empDeductionInfo.setEmpDateOfBirth(formatter.format(objArray[3]).toString());
            if(objArray[4]!=null)
            	empDeductionInfo.setEmpDateOfRetirement(formatter.format(objArray[4]).toString());
            empDeductionInfo.setAmount((BigDecimal)objArray[5]);
            empDeductionInfoList.add(empDeductionInfo);
        }
        return empDeductionInfoList;
    }

	private List getLICDeductionList() {
		if(null==billNumber || billNumber.equalsIgnoreCase(""))
		{
			billNumberId=0;
		}
		Map queryParams=getEmpLICDeductionQueryAndParams(month,year,department,billNumberId.toString());
		Query query=HibernateUtil.getCurrentSession().createSQLQuery((String)queryParams.get("query"));
		Object[] params=((List<Object>)queryParams.get("params")).toArray();

		if(params != null && params.length > 0) {
			for (int index = 0; index < params.length; index++) {
				query.setParameter(index, params[index]);
			}
		}

		List<Object[]> retList = query.list();
		if(retList!=null && !retList.isEmpty()) {
			return populateLICDeduction(query.list());
		}
		else {
			return Collections.EMPTY_LIST;
		}
	}

	private List getIncomeTaxDeductionList() {
		Map queryParams=getEmpIncomeTaxDeductionQueryAndParams(month,year,department,billNumberId);
		Query query=HibernateUtil.getCurrentSession().createSQLQuery((String)queryParams.get("query"));
		Object[] params=((List<Object>)queryParams.get("params")).toArray();

		if(params != null && params.length > 0) {
			for (int index = 0; index < params.length; index++) {
				query.setParameter(index, params[index]);
			}
		}

		List<Object[]> retList = query.list();
		if(retList!=null && !retList.isEmpty()) {
			return populateIncomeTaxDeduction(query.list());
		}
		else {
			return Collections.EMPTY_LIST;
		}
	}


    private List getProfTaxDeductionList() {
        Map queryParams=getEmpProfTaxDeductionQueryAndParams(month,year,department,billNumberId);
        Query query=HibernateUtil.getCurrentSession().createSQLQuery((String)queryParams.get("query"));
		Object[] params=((List<Object>)queryParams.get("params")).toArray();

		if(params != null && params.length > 0) {
			for (int index = 0; index < params.length; index++) {
				query.setParameter(index, params[index]);
			}
		}
        List<Object[]> retList = query.list();
        if(retList!=null && !retList.isEmpty()) {
            return populateProfTaxDeduction(query.list());
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }

    private List getDcpsDeductionList() {
        Map queryParams=getEmpDcpsDeductionQueryAndParams(month,year,department,billNumberId);
        Query query=HibernateUtil.getCurrentSession().createSQLQuery((String)queryParams.get("query"));
		Object[] params=((List<Object>)queryParams.get("params")).toArray();

		if(params != null && params.length > 0) {
			for (int index = 0; index < params.length; index++) {
				query.setParameter(index, params[index]);
			}
		}
        List<Object[]> retList = query.list();
        if(retList!=null && !retList.isEmpty()) {
            return populateDcpsDeduction(query.list());
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }

    private List getGISDeductionList() {
		Map queryParams=getGISDeductionQueryAndParams(month,year,department, billNumberId);
		Query query=HibernateUtil.getCurrentSession().createSQLQuery((String)queryParams.get("query"));
		Object[] params=((List<Object>)queryParams.get("params")).toArray();

		if(params != null && params.length > 0) {
			for (int index = 0; index < params.length; index++) {
				query.setParameter(index, params[index]);
			}
		}

		List<Object[]> retList = query.list();
		if(retList!=null && !retList.isEmpty()) {
			return populateGISDeduction(query.list());
		}
		else {
			return Collections.EMPTY_LIST;
		}
	}

	@ValidationErrorPage(INDEX)
	public String exportIncomeTaxPDF() {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		loadBillNoByDepartment(department);
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		reportParams.put("month", getMonthName(month));
		reportParams.put("year", getFinancialYearRange(year));
		reportParams.put("deptName",getDeptName(department));
		if((null != billNumber && !billNumber.equalsIgnoreCase("0")) && (!billNumber.isEmpty()))
			billNumberHeading = "BillNumber: "+billNumber;
		reportParams.put("billNo",getBillNumberHeading());
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getIncomeTaxDeductionList();
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		ReportRequest reportInput = new ReportRequest(INCOME_TAX_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.PDF);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.PDF);
		this.fileName="IncomeTaxDeductions." + FileFormat.PDF.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return "reportview";
	}

	@ValidationErrorPage(INDEX)
	public String exportIncomeTaxRTF() {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		loadBillNoByDepartment(department);
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		reportParams.put("month", getMonthName(month));
		reportParams.put("year", getFinancialYearRange(year));
		reportParams.put("deptName",getDeptName(department));
		if((null != billNumber &&!billNumber.equalsIgnoreCase("0")) && (!billNumber.isEmpty()))
			billNumberHeading = "BillNumber: "+billNumber;
		reportParams.put("billNo",getBillNumberHeading());
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getIncomeTaxDeductionList();
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		ReportRequest reportInput = new ReportRequest(INCOME_TAX_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.RTF);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.RTF);
		this.fileName="IncomeTaxDeductions." + FileFormat.RTF.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

		return "reportview";
	}

	@ValidationErrorPage(INDEX)
	public String exportIncomeTaxExcel() {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		loadBillNoByDepartment(department);
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		reportParams.put("month", getMonthName(month));
		reportParams.put("year", getFinancialYearRange(year));
		reportParams.put("deptName",getDeptName(department));
		if((null != billNumber && !billNumber.equalsIgnoreCase("0")) && (!billNumber.isEmpty()))
			billNumberHeading = "BillNumber: "+billNumber;
		reportParams.put("billNo",getBillNumberHeading());
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getIncomeTaxDeductionList();
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		ReportRequest reportInput = new ReportRequest(INCOME_TAX_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.XLS);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.XLS);
		this.fileName="IncomeTaxDeductions." + FileFormat.XLS.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return "reportview";
	}

	@ValidationErrorPage(INDEX)
	public String exportLICPDF() {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		loadBillNoByDepartment(department);
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		reportParams.put("month", getMonthName(month));
		reportParams.put("year", getFinancialYearRange(year));
		reportParams.put("deptName",getDeptName(department));
		if(billNumber!=null && !billNumber.equalsIgnoreCase("") && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
        reportParams.put("billNum",getBillNumberHeading());
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getLICDeductionList();
		if(empDeductionInfoList.size()<=0) {
			addFieldError("searchResult", "No Record Found");
			return INDEX;
		}
		Collections.sort(empDeductionInfoList,new EmpDeductionInfo());
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		ReportRequest reportInput = new ReportRequest(LIC_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.PDF);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.PDF);
		this.fileName="licDeductions." + FileFormat.PDF.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return "reportview";
	}

	@ValidationErrorPage(INDEX)
	public String exportLICHTML() {
		Map<String, Object> reportParams = new HashMap<String, Object>();

		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		reportParams.put("month", getMonthName(month));
		reportParams.put("year", getFinancialYearRange(year));
		reportParams.put("deptName",getDeptName(department));
		if(null != billNumber && (!billNumber.equalsIgnoreCase("")) && (!billNumber.isEmpty()))
        {
			billNumberHeading = "BillNumber: "+billNumber;
        }
		reportParams.put("billNum",getBillNumberHeading());
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getLICDeductionList();
		if(empDeductionInfoList.size()<=0) {
			addFieldError("searchResult", "No Record Found");
			return INDEX;
		}
		Collections.sort(empDeductionInfoList,new EmpDeductionInfo());
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		ReportRequest reportInput = new ReportRequest(LIC_DEDUCTION_TEMPLATE_HTML,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.HTM);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.HTM);
		this.fileName="licDeductions." + FileFormat.HTM.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

		return "reportview";
	}

	@ValidationErrorPage(INDEX)
	public String exportLICExcel() {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		loadBillNoByDepartment(department);
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}
		reportParams.put("month", getMonthName(month));
		reportParams.put("year", getFinancialYearRange(year));
		reportParams.put("deptName",getDeptName(department));
		if(null != billNumber && (!billNumber.equalsIgnoreCase("")) && (!billNumber.isEmpty()))
        {
			billNumberHeading = "BillNumber: "+billNumber;
        }
		reportParams.put("billNum",getBillNumberHeading());
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getLICDeductionList();
		if(empDeductionInfoList.size()<=0) {
			addFieldError("searchResult", "No Record Found");
			return INDEX;
		}
		Collections.sort(empDeductionInfoList,new EmpDeductionInfo());
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		ReportRequest reportInput = new ReportRequest(LIC_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.XLS);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.XLS);
		this.fileName="licDeductions." + FileFormat.XLS.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

		return "reportview";
	}

	@ValidationErrorPage(INDEX)
    public String exportProfTaxPDF() {
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
        Map<String, Object> reportParams = new HashMap<String, Object>();
        reportParams.put("month", getMonthName(month));
        reportParams.put("year", getFinancialYearRange(year));
        reportParams.put("deptName",getDeptName(department));
        reportParams.put("billNo",getBillNumberHeading());
        List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getProfTaxDeductionList();
        reportParams.put("noOfEmp", empDeductionInfoList.size());
        ReportRequest reportInput = new ReportRequest(PROF_TAX_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
        reportInput.setReportFormat(FileFormat.PDF);
        this.contentType = ReportViewerUtil.getContentType(FileFormat.PDF);
        this.fileName="ProfessionalTaxDeductions." + FileFormat.PDF.toString().toLowerCase();
        ReportOutput reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        loadBillNoByDepartment(department);
        return "reportview";
    }

	@ValidationErrorPage(INDEX)
    public String exportProfTaxExcel() {
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
        Map<String, Object> reportParams = new HashMap<String, Object>();
        reportParams.put("month", getMonthName(month));
        reportParams.put("year", getFinancialYearRange(year));
        reportParams.put("deptName",getDeptName(department));
        reportParams.put("billNo",getBillNumberHeading());
        List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getProfTaxDeductionList();
        reportParams.put("noOfEmp", empDeductionInfoList.size());
        ReportRequest reportInput = new ReportRequest(PROF_TAX_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
        reportInput.setReportFormat(FileFormat.XLS);
        this.contentType = ReportViewerUtil.getContentType(FileFormat.XLS);
        this.fileName="ProfessionalTaxDeductions." + FileFormat.XLS.toString().toLowerCase();
        ReportOutput reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        loadBillNoByDepartment(department);
        return "reportview";
    }

	@ValidationErrorPage(INDEX)
    public String exportDcpsDeductionPDF() {
        Map<String, Object> reportParams = new HashMap<String, Object>();
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
        reportParams.put("month", getMonthName(month));
        reportParams.put("year", getFinancialYearRange(year));
        reportParams.put("deptName",getDeptName(department));
        reportParams.put("billNo",getBillNumberHeading());
        List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getDcpsDeductionList();
        reportParams.put("noOfEmp", empDeductionInfoList.size());
        ReportRequest reportInput = new ReportRequest(DCPS_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
        reportInput.setReportFormat(FileFormat.PDF);
        this.contentType = ReportViewerUtil.getContentType(FileFormat.PDF);
        this.fileName="DCPSDeductions." + FileFormat.PDF.toString().toLowerCase();
        ReportOutput reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        loadBillNoByDepartment(department);
        return "reportview";
    }

	@ValidationErrorPage(INDEX)
    public String exportDcpsDeductionExcel() {
        Map<String, Object> reportParams = new HashMap<String, Object>();
        loadBillNoByDepartment(department);
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
        reportParams.put("month", getMonthName(month));
        reportParams.put("year", getFinancialYearRange(year));
        reportParams.put("deptName",getDeptName(department));
        reportParams.put("billNo",getBillNumberHeading());
        List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getDcpsDeductionList();
        reportParams.put("noOfEmp", empDeductionInfoList.size());
        ReportRequest reportInput = new ReportRequest(DCPS_DEDUCTION_TEMPLATE,empDeductionInfoList, reportParams);
        reportInput.setReportFormat(FileFormat.XLS);
        this.contentType = ReportViewerUtil.getContentType(FileFormat.XLS);
        this.fileName="DCPSDeductions." + FileFormat.XLS.toString().toLowerCase();
        ReportOutput reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return "reportview";
    }

	private BigDecimal getTotalIncomeTax(Integer month, Integer financialYearId, Integer deptId,Integer billNumberId) {
		BigDecimal grandTotal=BigDecimal.ZERO;
		StringBuffer qry=new StringBuffer("select sum(deductions.amount) from egpay_emppayroll payroll " +
				"INNER JOIN egpay_deductions deductions on payroll.id= deductions.id_emppayroll " +
				"INNER JOIN egpay_salarycodes salcodes on salcodes.id=deductions.id_salcode," +
				"eg_emp_assignment assig,egw_status status WHERE payroll.id_emp_assignment=assig.id " +
				"AND payroll.status=status.id AND salcodes.head=:salcode AND status.code!=:status " +
				"AND payroll.month=:month AND payroll.financialyearid=:finyear AND assig.main_dept=:deptId");

		if(null!=billNumberId && 0!=billNumberId)
		{
			qry.append(" AND payroll.id_billnumber=:billId");
		}

		Query query=HibernateUtil.getCurrentSession().createSQLQuery(qry.toString());
		query.setString("salcode", INCOME_TAX_SAL_HEAD);
		query.setString("status",PayrollConstants.PAYSLIP_CANCELLED_STATUS);
		query.setInteger("month", month);
		query.setInteger("finyear", financialYearId);
		query.setInteger("deptId", deptId);
		if(null!=billNumberId && 0!=billNumberId)
		{
			query.setInteger("billId", billNumberId);
		}

		grandTotal=(BigDecimal) query.uniqueResult();

		return grandTotal;
	}

	private BigDecimal getTotalProfessionalTax(Integer month, Integer financialYearId, Integer deptId,Integer billNumberId) {
		BigDecimal grandTotal=BigDecimal.ZERO;
		StringBuffer qry=new StringBuffer("SELECT SUM(payded.amount) "+
        		"FROM egpay_deductions payded, egpay_salarycodes salcode, egpay_emppayroll emppay, eg_employee emp, eg_emp_assignment empass, egw_status stat "+
        		"WHERE payded.id_salcode    =salcode.id AND salcode.head ='PROFTAX' AND emppay.id = payded.id_emppayroll AND emp.id = emppay.id_employee "+
        		"AND emppay.financialyearid = :finyear AND emppay.month = :month AND empass.id = emppay.id_emp_assignment AND empass.main_dept = :deptId and stat.id = emppay.status "+
        		"and stat.moduletype = 'PaySlip' and stat.description! = :status ");


		if(null!=billNumberId && 0!=billNumberId)
		{
			qry.append(" AND emppay.id_billnumber=:billId");
		}

		Query query=HibernateUtil.getCurrentSession().createSQLQuery(qry.toString());

		query.setInteger("finyear", financialYearId);
		query.setInteger("month", month);
		query.setInteger("deptId", deptId);
		query.setString("status",PayrollConstants.PAYSLIP_CANCELLED_STATUS);
		if(null!=billNumberId && 0!=billNumberId)
		{
			query.setInteger("billId", billNumberId);
		}

		grandTotal=(BigDecimal) query.uniqueResult();

		return grandTotal;
	}

	private BigDecimal getTotalDCPSDeductionAmt(Integer month, Integer financialYearId, Integer deptId, Integer billNumId) {
        String billNumberCond = "";
        if(billNumId != 0){
        	billNumberCond = " and emppay.ID_BILLNUMBER=:billNumId ";
        }
		BigDecimal grandTotal=BigDecimal.ZERO;
		String qry="SELECT NVL(SUM(payded.amount),0)    AS DCPSAMT FROM "+
					"egpay_deductions payded, egpay_salarycodes salcode, egpay_emppayroll emppay, eg_employee emp, eg_emp_assignment empass, "+
					 " egw_status stat, eg_designation desg "+
					"WHERE payded.id_salcode    =salcode.id AND salcode.head ='DCPSCURR' AND emppay.id = payded.id_emppayroll "+
					"AND emp.id                 = emppay.id_employee AND emppay.financialyearid = :finyear AND emppay.month = :month "+
					"AND empass.id              = emppay.id_emp_assignment AND empass.main_dept = :deptId AND stat.id = emppay.status "+
					"AND stat.moduletype        = 'PaySlip' AND stat.description != :status AND desg.designationid = empass.designationid "+billNumberCond;

		Query query=HibernateUtil.getCurrentSession().createSQLQuery(qry);

		query.setInteger("finyear", financialYearId);
		query.setInteger("month", month);
		query.setInteger("deptId", deptId);
		query.setString("status",PayrollConstants.PAYSLIP_CANCELLED_STATUS);
        if(billNumId != 0){
        	query.setInteger("billNumId", billNumId);
        }

		grandTotal=(BigDecimal) query.uniqueResult();

		return grandTotal;
	}

	private BigDecimal getTotalDCPSDeductionArr(Integer month, Integer financialYearId, Integer deptId, Integer billNumId) {
        String billNumberCond = "";
        if(billNumId != 0){
        	billNumberCond = " and emppay1.ID_BILLNUMBER=:billNumId ";
        }
		BigDecimal grandTotal=BigDecimal.ZERO;
		String qry="SELECT NVL(SUM(payded1.amount),0) "+
					"FROM egpay_deductions payded1, egpay_salarycodes salcode1, egpay_emppayroll emppay1, eg_employee emp1, "+
					 " eg_emp_assignment empass1, egw_status stat1, eg_designation desg1 "+
					"WHERE payded1.id_salcode    =salcode1.id AND salcode1.head ='DCPSARR' AND emppay1.id = payded1.id_emppayroll "+
					"AND emp1.id                 = emppay1.id_employee AND emppay1.financialyearid = :finyear AND emppay1.month = :month "+
					"AND empass1.id              = emppay1.id_emp_assignment AND empass1.main_dept = :deptId AND stat1.id = emppay1.status "+
					"AND stat1.moduletype        = 'PaySlip' AND stat1.description != :status AND desg1.designationid = empass1.designationid "+billNumberCond;

		Query query=HibernateUtil.getCurrentSession().createSQLQuery(qry);

		query.setInteger("finyear", financialYearId);
		query.setInteger("month", month);
		query.setInteger("deptId", deptId);
		query.setString("status",PayrollConstants.PAYSLIP_CANCELLED_STATUS);
        if(billNumId != 0){
        	query.setInteger("billNumId", billNumId);
        }

		grandTotal=(BigDecimal) query.uniqueResult();

		return grandTotal;
	}

	private BigDecimal getTotalGISDeduction(Integer month, Integer financialYearId, Integer deptId,Integer billNumberId) {
		BigDecimal temp=BigDecimal.ZERO;
		StringBuffer qry = new StringBuffer("SELECT sum(dedctn.amount)" +
        		" FROM egpay_emppayroll empprl,  egpay_salarycodes salcd,  eg_employee emp," +
        		" egpay_deductions dedctn,  eg_emp_assignment assgn,  eg_designation desgn,  egw_status stat" +
        		" WHERE empprl.status = stat.id AND stat.code !=:status AND stat.moduletype='PaySlip' AND empprl.id_employee = emp.id" +
        		" AND empprl.id_emp_assignment= assgn.id AND assgn.designationid =desgn.designationid AND empprl.id =dedctn.id_emppayroll" +
        		" AND dedctn.id_salcode = salcd.id AND salcd.head IN (:giscode1,:giscode2) AND assgn.main_dept =:deptId" +
        		" AND empprl.month =:month AND empprl.financialyearid  =:finyear ");
		if(null!=billNumberId && 0!=billNumberId)
		{
			qry.append("AND empprl.id_billnumber=:billId");
		}

		Query query=HibernateUtil.getCurrentSession().createSQLQuery(qry.toString());

		query.setString("giscode1", DEDUCTION_GIS_1);
		query.setString("giscode2", DEDUCTION_GIS_2);
		query.setString("status",PayrollConstants.PAYSLIP_CANCELLED_STATUS);
		query.setInteger("month", month);
		query.setInteger("finyear", financialYearId);
		query.setInteger("deptId", deptId);
		if(null!=billNumberId && 0!=billNumberId)
		{
			query.setInteger("billId", billNumberId);
		}

		temp=(BigDecimal) query.uniqueResult();

		return temp;
	}
	@ValidationErrorPage(INDEX)
	public String exportGISDeductionPDF() {
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("month", getMonthName(month));
		reportParams.put("year", getFinancialYearRange(year));
		reportParams.put("department", payrollExternalInterface.getDepartment(getDepartment()).getDeptName());
		List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getGISDeductionList();
		reportParams.put("noOfEmp", empDeductionInfoList.size());
		reportParams.put("billNo",getBillNumberHeading());
		ReportRequest reportInput = new ReportRequest(GIS_DEDUCTION,empDeductionInfoList, reportParams);
		reportInput.setReportFormat(FileFormat.PDF);
		this.contentType = ReportViewerUtil.getContentType(FileFormat.PDF);
		this.fileName="GISDeductions." + FileFormat.PDF.toString().toLowerCase();
		ReportOutput reportOutput = reportService.createReport(reportInput);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		loadBillNoByDepartment(department);
		return "reportview";
	}

	@ValidationErrorPage(INDEX)
    public String exportGISDeductionExcel() {
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}
        Map<String, Object> reportParams = new HashMap<String, Object>();
        reportParams.put("month", getMonthName(month));
        reportParams.put("year", getFinancialYearRange(year));
        reportParams.put("department", payrollExternalInterface.getDepartment(getDepartment()).getDeptName());
        List<EmpDeductionInfo> empDeductionInfoList =(List<EmpDeductionInfo>) getGISDeductionList();
        reportParams.put("noOfEmp", empDeductionInfoList.size());
        reportParams.put("billNo",getBillNumberHeading());
        ReportRequest reportInput = new ReportRequest(GIS_DEDUCTION,empDeductionInfoList, reportParams);
        reportInput.setReportFormat(FileFormat.XLS);
        this.contentType = ReportViewerUtil.getContentType(FileFormat.XLS);
        this.fileName="GISDeductions." + FileFormat.XLS.toString().toLowerCase();
        ReportOutput reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        loadBillNoByDepartment(department);
        return "reportview";
    }

	@Override
	public String execute()
	{
		return INDEX;
	}

	@Override
	@ValidationErrorPage(INDEX)
	public String search() {
		setPageSize(30);
		if(month<=0 || year<=0 || department<=0) {
			addFieldError("searchCriteria", "Please select all search criteria");
			return INDEX;
		}

		super.search();
		if(billNumber!=null && !billNumber.isEmpty()){
			billNumberHeading = "BillNumber: "+billNumber;
		}

		if(INCOME_TAX_DEDUCTION.equalsIgnoreCase(sourcepage)) {
			if(searchResult.getFullListSize()>0) {
				List<EmpDeductionInfo> deductionInfoList=populateIncomeTaxDeduction(searchResult.getList());
				searchResult.getList().clear();
				searchResult.getList().addAll(deductionInfoList);
				if((getPageSize()*(getPage()-1)+searchResult.getList().size())==searchResult.getFullListSize()) {
					grandTotalIncTax=getTotalIncomeTax(month, year, department,billNumberId);
				}
			}
		}

		if(PROF_TAX_DEDUCTION.equalsIgnoreCase(sourcepage)) {
            if(searchResult.getFullListSize()>0) {
                List<EmpDeductionInfo> deductionInfoList=populateProfTaxDeduction(searchResult.getList());
                searchResult.getList().clear();
                searchResult.getList().addAll(deductionInfoList);
				if((getPageSize()*(getPage()-1)+searchResult.getList().size())==searchResult.getFullListSize()) {
					grandTotalProfTax=getTotalProfessionalTax(month, year, department,billNumberId);
				}
            }
        }

		if(DCPS_DEDUCTION.equalsIgnoreCase(sourcepage)) {
            if(searchResult.getFullListSize()>0) {
                List<EmpDeductionInfo> deductionInfoList=populateDcpsDeduction(searchResult.getList());
                searchResult.getList().clear();
                searchResult.getList().addAll(deductionInfoList);
				if((getPageSize()*(getPage()-1)+searchResult.getList().size())==searchResult.getFullListSize()) {
					grandTotalDCPSAmt=getTotalDCPSDeductionAmt(month, year, department, billNumberId);
					grandTotalDCPSArr=getTotalDCPSDeductionArr(month, year, department, billNumberId);
					grandTotalDCPS=grandTotalDCPSAmt.add(grandTotalDCPSArr);
				}
            }
        }

        if(GIS_DEDUCTION.equalsIgnoreCase(sourcepage)) {
            if(searchResult.getFullListSize()>0) {
                List<EmpDeductionInfo> deductionInfoList=populateGISDeduction(searchResult.getList());
                searchResult.getList().clear();
                searchResult.getList().addAll(deductionInfoList);
                if((getPageSize()*(getPage()-1)+searchResult.getList().size())==searchResult.getFullListSize()) {
					grandTotal=getTotalGISDeduction(month, year, department,billNumberId);
				}
            }
        }

        loadBillNoByDepartment(department);

        return INDEX;
	}

	private  void loadBillNoByDepartment(Integer deptId)
	{
		if(deptId != 0)
		  addDropdownData("billNoList",persistenceService.findAllBy("from BillNumberMaster b where  b.department.id = ? ",
				deptId));
	   else
		  addDropdownData("billNoList",Collections.EMPTY_LIST);

	}
	public PayrollExternalInterface getPayrollExternalInterface() {
		return payrollExternalInterface;
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getMonthStr() {
		return monthStr;
	}

	public void setMonthStr(String monthStr) {
		this.monthStr = monthStr;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}

	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	public BigDecimal getGrandTotalIncTax() {
		return grandTotalIncTax;
	}

	public String getDeptStr() {
		return getDeptName(department);
	}

	public String getYearStr() {
		return getFinancialYearRange(year);
	}

	public BigDecimal getGrandTotalProfTax() {
		return grandTotalProfTax;
	}

	public void setGrandTotalProfTax(BigDecimal grandTotalProfTax) {
		this.grandTotalProfTax = grandTotalProfTax;
	}

	public BigDecimal getGrandTotalDCPSAmt() {
		return grandTotalDCPSAmt;
	}

	public void setGrandTotalDCPSAmt(BigDecimal grandTotalDCPSDed) {
		this.grandTotalDCPSAmt = grandTotalDCPSDed;
	}

	public BigDecimal getGrandTotalDCPSArr() {
		return grandTotalDCPSArr;
	}

	public void setGrandTotalDCPSArr(BigDecimal grandTotalDCPSArr) {
		this.grandTotalDCPSArr = grandTotalDCPSArr;
	}

	public BigDecimal getGrandTotalDCPS() {
		return grandTotalDCPS;
	}

	public void setGrandTotalDCPS(BigDecimal grandTotalDCPS) {
		this.grandTotalDCPS = grandTotalDCPS;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Integer getBillNumberId() {
		return billNumberId;
	}

	public void setBillNumberId(Integer billNumberId) {
		this.billNumberId = billNumberId;
	}

	public String getBillNumberHeading() {
		return billNumberHeading;
	}

	public void setBillNumberHeading(String billNumberHeading) {
		this.billNumberHeading = billNumberHeading;
	}

}