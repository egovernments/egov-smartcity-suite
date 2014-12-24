package org.egov.payroll.services.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.reports.BankAdviceReportDTO;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.pims.commons.DesignationMaster;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class PayrollReportService {
	
	private SessionFactory sessionFactory;
	private String billNumForReport;
	

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List getBankAdviceForMonthAndFinYear(Integer month, Integer financialYear, Boolean groupByDept, Boolean groupByEmpType ) {
		
		String selClause = "select ";
		String groupAndOrderByCols = " ";
		if (groupByEmpType==null) groupByEmpType = false;
		if (groupByDept == null) groupByDept = false;
		if (groupByEmpType) {
			selClause += "pay.employee.employeeTypeMaster.name, ";
			groupAndOrderByCols += "pay.employee.employeeTypeMaster.name, ";
		}
		selClause += " bank.name ";
		groupAndOrderByCols += " bank.name ";
		if (groupByDept) {
			selClause += ",pay.empAssignment.deptId.deptName ";
			groupAndOrderByCols += ",pay.empAssignment.deptId.deptName ";
		}
		selClause += ", sum(pay.netPay) ";
		String qryString= selClause + " from Miscbilldetail billmisc, EmpPayroll pay " +
				" left outer join pay.employee.egpimsBankDets as bankdet " +
				" left outer join bankdet.bank as bank " +
				"where pay.month=:month and pay.financialyear.id = :financialyearid " +
				"and pay.billRegister.egBillregistermis.voucherHeader = billmisc.billVoucherHeader " +
				"and billmisc.payVoucherHeader.status = 0 " +
				"group by " + groupAndOrderByCols +
				"order by " + groupAndOrderByCols; 
		Query qry = sessionFactory.getSession().createQuery(qryString);
		qry.setInteger("month", month);
		qry.setInteger("financialyearid", financialYear);
		
		List<Object[]> retList = qry.list(); 
		List<BankAdviceReportDTO> bankAdviceList = new ArrayList<BankAdviceReportDTO>();
		for (Object[] objArray : retList){
			BankAdviceReportDTO dto = new BankAdviceReportDTO();
			int i=0;
			if (groupByEmpType) {
				dto.setEmpType((String)objArray[i]);
				i++;
			} else {
				dto.setEmpType("");
			}
			dto.setBankName((String)objArray[i]);
			i++;
			
			if (groupByDept) {
				dto.setDeptName((String)objArray[i]);
				i++;
			} else {
				dto.setDeptName("");
			}
				
			dto.setNetAmount((BigDecimal)objArray[i]);
			bankAdviceList.add(dto);
		}
		
		return bankAdviceList;
	}
	
	/**
	 * Returns query. Query will returns deductions details by passing given month,year, department and deduction salarycodeid
	 * All fields are mandatory
	 * @param month
	 * @param year
	 * @param department
	 * @param deductionSalId
	 * @param type 
	 * @return String
	 */
	public String getEmployeeSalaryDeductionQuery(Integer month, Integer year, Integer department, Integer billNumId, Integer deductionSalId, String type)
	{
		StringBuffer qryStr = new StringBuffer(500);

		if (type != null && type.equals("selectQuery"))
			qryStr.append(" SELECT  UPPER(code),DECODE(NVL(gender,0),'F','MS.',DECODE(NVL(gender,0),'M','MR.','')) AS gender, UPPER(EMP_FIRSTNAME) as firstname,  UPPER(EMP_MIDDLENAME) as middlename, UPPER(EMP_LASTNAME) as lastname, SUM(amount) as amount  ");
		else
			qryStr.append(" SELECT COUNT(DISTINCT emp.id) AS COUNT ");

		qryStr.append(" FROM EGPAY_DEDUCTIONS deduction, EGPAY_EMPPAYROLL payroll , EG_EMP_ASSIGNMENT assignment,EG_EMPLOYEE emp "
				+ " WHERE deduction.ID_EMPPAYROLL=payroll.ID AND payroll.ID_EMP_ASSIGNMENT=assignment.ID "
				+ " AND (payroll.STATUS IN (SELECT egwstatus.ID FROM EGW_STATUS egwstatus WHERE egwstatus.MODULETYPE= ? "
				+ " AND (egwstatus.DESCRIPTION NOT IN (?)))) AND payroll.id_employee=emp.id ");
		if (month != 0) {
			qryStr.append("  AND payroll.MONTH=? ");
		}
		if (year != 0) {
			qryStr.append(" AND payroll.FINANCIALYEARID= ? ");
		}
		if (department != 0) {
			qryStr.append(" and main_dept=? ");
		}
		if (deductionSalId != 0) {
			qryStr.append(" AND  id_salcode=? ");
		}
        if(billNumId != 0){
        	qryStr.append(" AND  payroll.ID_BILLNUMBER=? ");
        }

		if (type != null && type.equals("selectQuery"))
			qryStr.append(" GROUP BY   payroll.id_employee, GENDER, EMP_FIRSTNAME, EMP_LASTNAME, EMP_MIDDLENAME,code ORDER BY  EMP_FIRSTNAME ");

		return qryStr.toString();
	} 
	
	/**
	 * getting queryString of  employee wise payment for given month and year
	 * bankId and groupByEmpType are optional
	 * @param month
	 * @param financialYear
	 * @param bankId
	 * @param groupByEmpType
	 * @return
	 */
	public String getEmpwisePaymentAdviceForMonthAndFinYear(Integer bankId,Boolean groupByEmpType) {//Integer month, Integer financialYear,
		
		String selectClause = "select new org.egov.payroll.reports.BankAdviceReportDTO( " +
		"pay.employee.employeeTypeMaster.name as emptype ,pay.employee.employeeCode,pay.employee.employeeName," +
		"pay.empAssignment.deptId.deptName," +
		"branch.branchname ,bank.name ,bankdet.accountNumber,pay.netPay )";
		
		return selectClause + getFromQuery(bankId, groupByEmpType) ;//+ " order by bank.name";

		
		}
	/**
	 * returning queryString to get the cont of results
	 * @param bankId
	 * @param groupByEmpType
	 * @return
	 */
	public String getCountEmpwisePaymentAdviceForMonthAndFinYear(Integer bankId,Boolean groupByEmpType)
	{
		String selectClause = "select count(pay.employee.employeeCode) "; 		
		return selectClause + getFromQuery(bankId, groupByEmpType);
	}
	
	/**
	 * @param bankId
	 * @param groupByEmpType
	 * @return
	 */
	private String getFromQuery(Integer bankId, Boolean groupByEmpType) {
		final String BANKIDCHECK=" and bank.id="; 
		String fromClause = " from Miscbilldetail" +
		" billmisc, EmpPayroll pay " +
		" left outer join pay.employee.egpimsBankDets as bankdet " +
		" left outer join bankdet.bank as bank " +
		" left outer join bankdet.branch as branch ";
		
		String whereClause = " where pay.month=? and pay.financialyear.id = ? " +
		"and pay.billRegister.egBillregistermis.voucherHeader = billmisc.billVoucherHeader " +
		"and billmisc.payVoucherHeader.status = 0 ";
		if(bankId!=null && bankId!=0)
		{
			whereClause = whereClause + BANKIDCHECK + bankId;
		}
		
		String  groupByEmpTypeClause = "";
		String fromQuery = fromClause + whereClause ;
		if (groupByEmpType) {
			groupByEmpTypeClause = "  pay.employee.employeeTypeMaster.name,"
					+ "pay.employee.employeeCode,pay.employee.employeeName,pay.empAssignment.deptId.deptName,bankdet.branch.branchname,bank.name,bankdet.accountNumber,pay.netPay ";
			fromQuery=fromQuery+" group by "+ groupByEmpTypeClause +" order by "+groupByEmpTypeClause;
		}		
		
		return fromQuery;
	}
	
	public List getBranchWiseSummaryForMonthAndFinYear(Integer month, Integer financialYear, Integer bankId, Boolean groupByEmpType ) {
		String selClause = "select ";
		String groupAndOrderByCols = " ";
		if (groupByEmpType==null) groupByEmpType = false;
		if (groupByEmpType) {
			selClause += " pay.employee.employeeTypeMaster.name, ";
			groupAndOrderByCols += " pay.employee.employeeTypeMaster.name, ";
		}
		selClause += "bank.name, branch.branchcode, branch.branchname ";
		groupAndOrderByCols += " bank.name, branch.branchcode, branch.branchname ";
		String additionalFilterClause = "";
		if (bankId!=null && bankId > 0)
			additionalFilterClause = " and bank.id = :bankId ";
		String qryString= selClause + " , count(pay.employee) , sum(pay.netPay) from Miscbilldetail billmisc, EmpPayroll pay " +
		" left outer join pay.employee.egpimsBankDets as bankdet " +
		" left outer join bankdet.bank as bank " +
		" left outer join bankdet.branch as branch " +
		" where pay.month=:month and pay.financialyear.id = :financialyearid " +
		"and pay.billRegister.egBillregistermis.voucherHeader = billmisc.billVoucherHeader " +
		"and billmisc.payVoucherHeader.status = 0 " + additionalFilterClause +
	    " group by " + groupAndOrderByCols + 
		" order by " + groupAndOrderByCols; 
		Query qry = sessionFactory.getSession().createQuery(qryString);
		qry.setInteger("month", month);
		qry.setInteger("financialyearid", financialYear);
		if (bankId!=null && bankId > 0)
			qry.setInteger("bankId", bankId);
		List<Object[]> retList = qry.list(); 
		List<BankAdviceReportDTO> branchWiseList = new ArrayList<BankAdviceReportDTO>();
		for (Object[] objArray : retList){
			BankAdviceReportDTO dto = new BankAdviceReportDTO();
			int i=0;
			if (groupByEmpType) {
				dto.setEmpType((String)objArray[i]);
				i++;
			} else {
				dto.setEmpType("");
			}

			dto.setBankName((String)objArray[i]);
			dto.setBranchCode((String)objArray[i+1]);
			dto.setBranchName((String)objArray[i+2]);
			dto.setEmpCount((Long)objArray[i+3]);
			dto.setNetAmount((BigDecimal)objArray[i+4]);
			
			branchWiseList.add(dto);
		}
		
		return branchWiseList;
	}
		
	/**
	 * returns list of Festival Advance deduction for given month, year and department for approved and supplementary payslips
	 * @param month
	 * @param year
	 * @param dept
	 * @return Map
	 */
	public Map<String,Object > getFestivalAdvanceReport(Integer month,Integer year,Integer dept, Integer billNumId) {
		Map<String,Object > queryMap=new HashMap<String,Object >();
		queryMap.put("select",  getFestivalAdvanceReportQuery("select",billNumId));
		queryMap.put("count",  getFestivalAdvanceReportQuery("count", billNumId));
		List<Object> params=new ArrayList<Object>();
		params.add(Integer.valueOf(month));
		params.add(Integer.valueOf(year));
		params.add(PayrollConstants.SALCODE_FEST_ADV);
		params.add(Integer.valueOf(dept));
        if(billNumId != 0){
        	params.add(billNumId);
        }
		queryMap.put("params",  params);
		return queryMap;
	}
	
	/**
	 * getting queryString of employee wise Festival Advance deduction for given department, month and year	 
	 * @param month
	 * @param year
	 * @param department
	 * @return List
	 */
	public List getFestivalAdvanceReportMonthYearDept(Integer month,Integer year,Integer dept, Integer billNumId) {
		String searchquery= getFestivalAdvanceReportQuery("select", billNumId);
		SQLQuery qry = sessionFactory.getSession().createSQLQuery(searchquery);
		qry.setInteger(0, month);
		qry.setInteger(1, year);
		qry.setString(2, PayrollConstants.SALCODE_FEST_ADV);
		qry.setInteger(3, dept);
        if(billNumId != 0){
        	qry.setInteger(4, billNumId);
        }
		return qry.list(); 
	}
	public BigDecimal getTotalFestiveAdvDeduction(Integer month,Integer year,Integer dept, Integer billNumId) {
		String searchquery= getFestivalAdvanceReportQuery("total", billNumId);
		SQLQuery qry = sessionFactory.getSession().createSQLQuery(searchquery);
		qry.setInteger(0, month);
		qry.setInteger(1, year);
		qry.setString(2, PayrollConstants.SALCODE_FEST_ADV);
		qry.setInteger(3, dept);
        if(billNumId != 0){
        	qry.setInteger(4, billNumId);
        }
		return (BigDecimal)qry.uniqueResult();
	}
	
	
	private String getCountFestivalAdvanceReportMonthFinYearDept(Integer month,Integer year,Integer dept, Integer billNumId) {
		return getFestivalAdvanceReportQuery("count", billNumId); 		
	}
	
	private String getFestivalAdvanceReportQuery(String countOrSelectOrTotal, Integer billNumId) { 
        String billNumberCond = "";
        String returnQuery="";
        if(billNumId != 0){
        	billNumberCond = " and payslip.ID_BILLNUMBER=? ";
        }
		String groupAndOrderByCols="code,name,gender,amount,noofinst,instno ";
		String innerSelectFields="ded.amount as amount, upper(emp.code) as code,upper(emp.name) as name,"+
								  " decode(nvl(emp.gender,0),'F','Ms.',decode(nvl(emp.gender,0),'M','Mr.',''))  as gender,"+
								  " adv.NUM_OF_INST as noofinst,sch.INSTALLMENT_NO as instno ";
		String fromClause=" from egpay_emppayroll payslip,eg_employee emp, eg_emp_assignment ass,"+
							" egpay_deductions ded,egpay_salarycodes salcodes,egpay_saladvances adv, egpay_advance_schedule sch ";
		String whereClause=" where payslip.month = ? and payslip.financialyearid=? and salcodes.head = ? "+ 
							" and payslip.status in(select id from egw_status status where status.code!='"+PayrollConstants.PAYSLIP_CANCELLED_STATUS+"'"+
							" and status.moduletype='PaySlip') and payslip.id=ded.id_emppayroll and ded.id_salcode=salcodes.id  "+
							"and ass.id=payslip.id_emp_assignment and payslip.id_employee=emp.id and ass.main_dept = ? "+billNumberCond;
		String advWhereClause=" and sch.id_saladvance=adv.id " + 						
								" and ded.id_advance_scheduler=sch.id and ded.ID_SAL_ADVANCE=adv.id"+
								" and adv.id_salcode=salcodes.id "+
								" and adv.id_employee=payslip.id_employee "
								;
		String sqlquery= "select "+groupAndOrderByCols+" from ("+
								" select "+ innerSelectFields+fromClause+whereClause+advWhereClause+
								")"+
								" group by " +	groupAndOrderByCols+
								" order by " +groupAndOrderByCols;
		String countquery= "select count(code) from ("+
				" select emp.code as code "+fromClause+whereClause+advWhereClause+
				")";
		String totalQuery= "select sum(amount) from ("+
				" select ded.amount as amount "+fromClause+whereClause+advWhereClause+
				")";
		if(countOrSelectOrTotal.equals("select"))
			returnQuery=sqlquery;
		else if(countOrSelectOrTotal.equals("count"))
			returnQuery=countquery;
		else if(countOrSelectOrTotal.equals("total"))
			returnQuery=totalQuery;
				
		return returnQuery;
	}	
	
	/**
	 * returns list of PF and PF advance for given month year and department for approved payslips
	 * @param month
	 * @param financialYear
	 * @param deptId
	 * @return 
	 */
	public Map<String,Object > getPFReportQuery(Integer month,Integer year,Integer dept,String bill)
	{
		billNumForReport = bill;
		Map<String,Object > queryMap=new HashMap<String,Object >();
		queryMap.put("select",  getPfReportQuery("select"));
		queryMap.put("count",  getPfReportQuery("count"));
		List<Object> params=new ArrayList<Object>();
		params.add(Integer.valueOf(month));
		params.add(Integer.valueOf(year));
		params.add(PayrollConstants.SALCODE_GPF);
		params.add(Integer.valueOf(dept));
		params.add(Integer.valueOf(dept));
		if((!billNumForReport.equalsIgnoreCase("0")) && (!billNumForReport.isEmpty()))
		{
			params.add(billNumForReport);
		}
		
		params.add(Integer.valueOf(month));
		params.add(Integer.valueOf(year));
		params.add(PayrollConstants.SALCODE_GPF_ADV);
		params.add(Integer.valueOf(dept));
		params.add(Integer.valueOf(dept));
		if((!billNumForReport.equalsIgnoreCase("0")) && (!billNumForReport.isEmpty()))
		{
			params.add(billNumForReport);
		}
		queryMap.put("params",  params);
		return queryMap;
	}
	public List getPFReportMonthYearDept(Integer month,Integer year,Integer dept,String bill)
	{
		billNumForReport = bill;
		int i=0;
		String searchquery= getPfReportQuery("select");
		SQLQuery qry = sessionFactory.getSession().createSQLQuery(searchquery);
		qry.setInteger(i++, month);
		qry.setInteger(i++, year);
		qry.setString(i++, PayrollConstants.SALCODE_GPF);
		qry.setInteger(i++, dept);
		qry.setInteger(i++, dept);
		if((!billNumForReport.equalsIgnoreCase("0")) && (!billNumForReport.isEmpty()))
		{
			qry.setString(i++,billNumForReport);
		}
		qry.setInteger(i++, month);
		qry.setInteger(i++, year);
		qry.setString(i++, PayrollConstants.SALCODE_GPF_ADV);
		qry.setInteger(i++, dept);
		qry.setInteger(i++, dept);
		if((!billNumForReport.equalsIgnoreCase("0")) && (!billNumForReport.isEmpty()))
		{
			qry.setString(i++,billNumForReport);
		}
		return qry.list();
	}
	public List getTotalForPFReportMonthYearDept(Integer month,Integer year,Integer dept,String bill)
	{
		billNumForReport = bill;
		int i=0;
		String searchquery= getPfReportQuery("select");
		searchquery="select sum(GPFSUbamt) as grandTotPF,sum(GPFADVamt) as grandTotADV, sum(GPFSUbamt+GPFADVamt) as grandTot from ("+searchquery+")";
		SQLQuery qry = HibernateUtil.getCurrentSession().createSQLQuery(searchquery);
		qry.setInteger(i++, month);
		qry.setInteger(i++, year);
		qry.setString(i++, PayrollConstants.SALCODE_GPF);
		qry.setInteger(i++, dept);
		qry.setInteger(i++, dept);
		if((!billNumForReport.equalsIgnoreCase("0")) && (!billNumForReport.isEmpty()))
		{
			qry.setString(i++,billNumForReport);
		}
		qry.setInteger(i++, month);
		qry.setInteger(i++, year);
		qry.setString(i++, PayrollConstants.SALCODE_GPF_ADV);
		qry.setInteger(i++, dept);
		qry.setInteger(i++, dept);
		if((!billNumForReport.equalsIgnoreCase("0")) && (!billNumForReport.isEmpty()))
		{
			qry.setString(i++,billNumForReport);
		}
		return qry.list();
	}
	private String getCountPFReportMonthFinYearDept(Integer month,Integer year,Integer dept)
	{
		return getPfReportQuery("count"); 		
	}
	
	private String getPfReportQuery(String countOrSelect) {
		String billNumQuery = " AND payslip.id_billnumber = ? ";
		String groupAndOrderByCols="PFNum,upper(code),upper(name),head,GPFSUbamt,GPFADVamt,month ,year,dept,noofinst,instno ";
		String innerSelectFields="salcodes.head as head,payslip.month as month,payslip.financialyearid as year,"+
									"department.dept_name as dept,"+
									"emp.gpf_ac_number as pfNum, emp.code as code,decode(nvl(emp.gender,0),'F','Ms.','M','Mr.','')||emp.name as name";
									
		String advSelectfields=" ,0 as GPFSUBamt,decode(salcodes.head,'GPFADV',ded.amount,0) as GPFADVamt,adv.NUM_OF_INST as noofinst,sch.INSTALLMENT_NO as instno" ;
		String pfSelectfields=" ,decode(salcodes.head,'GPFSUB',ded.amount,0) as GPFSUBamt,0 as GPFADVamt,0 as noofinst,0 as instno ";
		String fromClause=" from egpay_emppayroll payslip,eg_employee emp, eg_emp_assignment ass,eg_department department,"+
							"egpay_deductions ded,egpay_salarycodes salcodes "; 
		String advFromClause=",egpay_saladvances adv, egpay_advance_schedule sch ";
		String whereClause=" where payslip.month=? and payslip.financialyearid=? and   salcodes.head= ? "+ 
							" and payslip.status in(select id from egw_status status where status.code!='"+PayrollConstants.PAYSLIP_CANCELLED_STATUS+"'"+
							" and status.moduletype='PaySlip') and payslip.id=ded.id_emppayroll and ded.id_salcode=salcodes.id  "+
							"and ass.id=payslip.id_emp_assignment and payslip.id_employee=emp.id and ass.main_dept=? and department.id_dept=?" +
							" "+(((!billNumForReport.equalsIgnoreCase(""))&&(!billNumForReport.equalsIgnoreCase("0")))?billNumQuery:" ");
		String advWhereClause=" and sch.id_saladvance=adv.id and ded.id_advance_scheduler=sch.id and ded.ID_SAL_ADVANCE=adv.id"+
								" and adv.id_salcode=salcodes.id "+
								" and adv.id_employee=payslip.id_employee ";
		String sqlquery= "select "+groupAndOrderByCols+" from ("+
								" select "+ innerSelectFields+pfSelectfields+fromClause+whereClause+" union "+  
								" select "+ innerSelectFields+advSelectfields+fromClause+advFromClause+whereClause+advWhereClause+
								")"+
								" group by " +	groupAndOrderByCols+
								" order by " +groupAndOrderByCols;
		
		String countquery= "select count(code) from ("+
				" select emp.code as code "+fromClause+whereClause+" union "+
				"select emp.code as code "+fromClause+advFromClause+whereClause+advWhereClause+
				")";
				
		return (countOrSelect.equals("select") ? sqlquery : countquery);
	}
	

	 public List<DesignationMaster> getDesignationList(String codelike)
	 {
			Criteria criteria = sessionFactory.getSession().createCriteria(DesignationMaster.class, "designation");
			criteria.add(Restrictions.ilike("designationName", codelike, MatchMode.START));
			return criteria.list();
	 }
	 
}