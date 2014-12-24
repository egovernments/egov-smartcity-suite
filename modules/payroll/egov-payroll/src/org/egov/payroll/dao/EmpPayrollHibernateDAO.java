package org.egov.payroll.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.models.Script;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payhead.PayheadService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.model.BankDet;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

/**
 * This Class implememets the EmpPayrollDAO for the Hibernate specific
 * Implementation
 * 
 * @author Lokesh,Mamatha,Surya,Jagadeesan
 * @version 2.00
 */

public class EmpPayrollHibernateDAO extends GenericHibernateDAO implements
		EmpPayrollDAO {

	private static final Logger logger = Logger
			.getLogger(EmpPayrollHibernateDAO.class);

	public EmpPayrollHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	public EmpPayroll getPayslipForEmpByMonthAndYear(Integer empId,
			Integer month, Integer year) {
		Query qry = getSession()
				.createQuery(
						"from EmpPayroll pay where pay.month =:month "
								+ "	and pay.employee.idPersonalInformation = :empId and pay.financialyear.id =:year and pay.payType.paytype=:paytype and pay.status.description not in ( :status)");
		qry.setInteger("month", month);
		qry.setInteger("empId", empId);
		qry.setInteger("year", year);
		qry.setString(
				"status",
				GenericDaoFactory
						.getDAOFactory()
						.getAppConfigValuesDAO()
						.getAppConfigValueByDate("Payslip",
								"PayslipCancelledStatus", new Date())
						.getValue());
		qry.setString("paytype", PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);

		return (EmpPayroll) qry.uniqueResult();
	}

	public List<EmpPayroll> getAllPayslipByEmpMonthYear(Integer empId,
			Integer month, Integer year) {
		List<EmpPayroll> payslips = new ArrayList<EmpPayroll>();
		Query qry = getSession()
				.createQuery(
						"from EmpPayroll pay where pay.month =:month "
								+ " and pay.employee.idPersonalInformation = :empId and pay.financialyear.id =:year and pay.status.description not in ( :status )");
		qry.setInteger("month", month);
		qry.setInteger("empId", empId);
		qry.setInteger("year", year);
		qry.setString(
				"status",
				GenericDaoFactory
						.getDAOFactory()
						.getAppConfigValuesDAO()
						.getAppConfigValueByDate("Payslip",
								"PayslipCancelledStatus", new Date())
						.getValue());
		payslips = qry.list();
		return payslips;

	}

	public EmpPayroll getPayslipForEmpByMonthAndYear(Integer empId,
			Integer month, Integer year, Integer paytype) {
		Query qry = getSession()
				.createQuery(
						"from EmpPayroll pay where pay.month =:month "
								+ "	and pay.employee.idPersonalInformation = :empId and pay.financialyear.id =:year and pay.payType.id=:paytype and pay.status.description not in ( :status )");
		qry.setInteger("month", month);
		qry.setInteger("empId", empId);
		qry.setInteger("year", year);
		qry.setInteger("paytype", paytype);
		qry.setString(
				"status",
				GenericDaoFactory
						.getDAOFactory()
						.getAppConfigValuesDAO()
						.getAppConfigValueByDate("Payslip",
								"PayslipCancelledStatus", new Date())
						.getValue());
		return (EmpPayroll) qry.uniqueResult();

	}

	public List getAllPrevPayslipsForEmpByMonthAndYear(Integer empId,
			Integer month, Integer year) {
		List empPayrollList = new ArrayList();
		Query qry = getSession()
				.createQuery(
						"from EmpPayroll pay where "
								+ " pay.employee.idPersonalInformation = :empId and pay.financialyear.id =:year and "
								+ " pay.month in (:monthList) and pay.status.description not in ( :status )");

		qry.setInteger("empId", empId);
		qry.setParameterList("monthList", getListOfPrevMonthsInFinYear(month));
		qry.setInteger("year", year);
		qry.setString(
				"status",
				GenericDaoFactory
						.getDAOFactory()
						.getAppConfigValuesDAO()
						.getAppConfigValueByDate("Payslip",
								"PayslipCancelledStatus", new Date())
						.getValue());
		empPayrollList = qry.list();
		return empPayrollList;
	}

	protected List<BigDecimal> getListOfPrevMonthsInFinYear(Integer month) {
		ArrayList<BigDecimal> prevMonths = new ArrayList<BigDecimal>();
		prevMonths.add(BigDecimal.ZERO);
		if (month > 4) {
			for (int i = 4; i < month; i++) {
				prevMonths.add(new BigDecimal(i));
			}
		} else if (month <= 3) {
			for (int i = 4; i <= 12; i++) {
				prevMonths.add(new BigDecimal(i));
			}
			for (int i = 1; i < month; i++) {
				prevMonths.add(new BigDecimal(i));
			}
		}
		return prevMonths;
	}

	public List getAllPayslipByStatus(EgwStatus status) {
		Query qry = getSession().createQuery(
				"from EmpPayroll empPayroll where empPayroll.status=:status");
		qry.setEntity("status", status);
		return qry.list();
	}

	public List getPayslipsOfYearMonthStatus(String year, BigDecimal month,
			Department department, EgwStatus status) throws Exception {
		try {
			Query qry = getSession()
					.createQuery(
							"from EmpPayroll ep where ep.financialyear.finYearRange=:year and "
									+ "ep.month =:month and ep.status =:status and ep.empAssignment.deptId =:department");
			qry.setString("year", year);
			qry.setBigDecimal("month", month);
			qry.setEntity("status", status);
			qry.setEntity("department", department);
			return qry.list();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	// Not Using this api
	public List getPrevApprovedPayslipsByMonthAndYear(
			GregorianCalendar fromdate, GregorianCalendar todate, Integer deptid) {
		List empPayrollList = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());

		StringBuffer query = new StringBuffer(
				" select distinct(e) from EmpPayroll e,EmployeeView ev where e.month =:month and e.payType.paytype =:paytype and "
						+ " e.employee.idPersonalInformation not in "
						+ " (select ex.employee.idPersonalInformation from EmpException ex "
						+ " where ex.status.moduletype in ('EmpException') and ex.status.description not in ('Closed') and (ex.fromDate is null or ex.fromDate<=to_date(:date,'dd/mm/yy') )) and "
						+ " e.status.moduletype in ('PaySlip') and e.status.description in (:status) and e.employee.idPersonalInformation=ev.id ");

		Query qry = HibernateUtil.getCurrentSession().createQuery(
				query.toString());
		qry.setString(
				"status",
				GenericDaoFactory
						.getDAOFactory()
						.getAppConfigValuesDAO()
						.getAppConfigValueByDate("Payslip",
								"PayslipAuditApprovedStatus", new Date())
						.getValue());

		int flag = 0;
		if (deptid != null && deptid != 0) {
			flag = 1;
			query.append(" and ev.deptId.id=:deptid");
		}
		qry.setString("paytype", PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);

		// FIXME if from date month = to date month then take month as from date
		// month -1
		qry.setInteger("month", (fromdate.get(Calendar.MONTH)) + 1);
		// beacuse gregoriancalendar starts from 0
		qry.setString("date", formatter.format(todate.getTime()));
		if (flag == 1) {
			qry.setInteger("deptid", deptid);
		}
		empPayrollList = qry.list();
		logger.info("payroll list ----------" + empPayrollList.size());
		return empPayrollList;

	}

	// Not Using this api
	public List getPrevSuppPayslipsForEmpExceptionsInMonthAndYear(
			GregorianCalendar fromDate, GregorianCalendar toDate, Integer deptid) {
		List empPayrollList = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		StringBuffer query = new StringBuffer(
				"select distinct(e) from EmpPayroll e,EmployeeView ev where e.month =:prevMonth and e.payType.paytype =:paytype and"
						+ " e.employee.idPersonalInformation in "
						+ " (select ex.employee.idPersonalInformation from EmpException ex "
						+ " where (ex.fromDate <= to_date(:payStartDate,'dd/mm/yyyy') and ex.toDate >= to_date(:payEndDate,'dd/mm/yyyy'))"
						// means that exception should start before month and
						// end after month
						+ "and ex.status.moduletype in ('EmpException') and ex.status.description in ('Approved')) and "
						+ " e.status.moduletype in ('PaySlip') and e.status.description in (:status)  and e.employee.idPersonalInformation=ev.id ");

		int flag = 0;
		if (deptid != null && deptid != 0) {
			flag = 1;
			query.append(" and ev.deptId.id=:deptid");
		}
		Query qry = HibernateUtil.getCurrentSession().createQuery(
				query.toString());
		qry.setInteger("prevMonth", fromDate.get(Calendar.MONTH) + 1);
		qry.setString(
				"status",
				GenericDaoFactory
						.getDAOFactory()
						.getAppConfigValuesDAO()
						.getAppConfigValueByDate("Payslip",
								"PayslipAuditApprovedStatus", new Date())
						.getValue());
		// qry.setInteger("exceptionMonth", toDate.get(Calendar.MONTH)+1);
		qry.setString("paytype", PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION);
		if (flag == 1) {
			qry.setInteger("deptid", deptid);
		}
		qry.setString("payStartDate", formatter.format(fromDate.getTime()));
		qry.setString("payEndDate", formatter.format(toDate.getTime()));
		empPayrollList = qry.list();
		return empPayrollList;

	}

	public List<Integer> getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(
			GregorianCalendar fromdate, GregorianCalendar todate,
			Integer deptid, Integer functionaryId) {
		List<Integer> empPayrollList = new ArrayList<Integer>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());

		StringBuffer query = new StringBuffer(
				" select distinct(ev.id) from EmployeeView ev  "
						+ " where ev.id not in "
						+ " (select ex.employee.idPersonalInformation from EmpException ex "
						+ " where ex.status.moduletype in ('EmpException') and ex.status.description in ('Approved') and (ex.fromDate is null or ex.fromDate<=to_date(:date,'dd/mm/yy') )) "
						+ " and ev.isActive=1 and ev.isPrimary='Y' and ((ev.toDate IS NULL and ev.fromDate <=to_date(:date,'dd/mm/yy') ) OR(ev.fromDate <= to_date(:date,'dd/mm/yy') AND ev.toDate >=to_date(:date,'dd/mm/yy'))) ");

		int flag = 0;
		int flagSecond = 0;
		if (deptid != null && deptid != 0) {
			flag = 1;
			query.append(" and ev.deptId.id=:deptid");
		}
		if (functionaryId != null && functionaryId != 0) {
			flagSecond = 1;
			query.append(" and ev.functionary.id=:functionaryId");
		}
		Query qry = HibernateUtil.getCurrentSession().createQuery(
				query.toString());

		qry.setString("date", formatter.format(todate.getTime()));
		// qry.setDate("fromdate",todate.getTime());
		if (flag == 1) {
			qry.setInteger("deptid", deptid);
		}
		if (flagSecond == 1) {
			qry.setInteger("functionaryId", functionaryId);
		}
		empPayrollList = qry.list();
		logger.info("payroll list ----------inside getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary"
				+ empPayrollList.size());
		return empPayrollList;
	}

	public List<Integer> getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(
			GregorianCalendar fromdate, GregorianCalendar todate,
			Integer deptid, Integer functionaryId, Long functionId,
			Integer billNumberId) {
		List<Integer> empPayrollList = new ArrayList<Integer>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());

		StringBuffer query = new StringBuffer(
				" select distinct(ev.id) from EmployeeView ev  "
						+ " where ev.id not in "
						+ " (select ex.employee.idPersonalInformation from EmpException ex "
						+ " where ex.status.moduletype in ('EmpException') and ex.status.description in ('Approved') and (ex.fromDate is null or ex.fromDate<=to_date(:date,'dd/mm/yy') )) "
						+ " and ev.isActive=1 and ev.isPrimary='Y' and ((ev.toDate IS NULL and ev.fromDate <=to_date(:date,'dd/mm/yy') ) OR(ev.fromDate <= to_date(:date,'dd/mm/yy') AND ev.toDate >=to_date(:date,'dd/mm/yy'))) ");

		int flag = 0;
		int flagSecond = 0;
		if (deptid != null && deptid != 0) {
			flag = 1;
			query.append(" and ev.deptId.id=:deptid");
		}
		if (functionaryId != null && functionaryId != 0) {
			flagSecond = 1;
			query.append(" and ev.functionary.id=:functionaryId");
		}
		if (functionId != null && functionId != 0) {
			query.append(" and ev.functionId.id=:functionId");
		}
		if (billNumberId != null && billNumberId != 0) {
			query.append(" and ev.assignment.position.billNumber.id=:billNumberId");
		}
		Query qry = HibernateUtil.getCurrentSession().createQuery(
				query.toString());

		qry.setString("date", formatter.format(todate.getTime()));
		// qry.setDate("fromdate",todate.getTime());
		if (flag == 1) {
			qry.setInteger("deptid", deptid);
		}
		if (flagSecond == 1) {
			qry.setInteger("functionaryId", functionaryId);
		}
		if (functionId != null && functionId != 0) {
			qry.setLong("functionId", functionId);
		}
		if (billNumberId != null && billNumberId != 0) {
			qry.setInteger("billNumberId", billNumberId);
		}
		empPayrollList = qry.list();
		logger.info("payroll list ----------inside getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary"
				+ empPayrollList.size());
		return empPayrollList;
	}

	public List<Integer> getSuppPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(
			GregorianCalendar fromdate, GregorianCalendar todate,
			Integer deptid, Integer functionaryId) {
		List<Integer> empPayrollList = new ArrayList<Integer>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		StringBuffer query = new StringBuffer(
				" select distinct(ev.id) from EmployeeView ev  "
						+ " where ev.id in "
						+ " (select ex.employee.idPersonalInformation from EmpException ex "
						+ " where ex.status.moduletype in ('EmpException') and ex.status.description in ('Approved') and (ex.fromDate is null or ex.fromDate<=to_date(:date,'dd/mm/yy') )) "
						+ " and ev.isActive=1 and ev.isPrimary='Y' ");// and
																		// ((ev.toDate
																		// IS
																		// NULL
		// and ev.fromDate <=
		// :fromdate ) OR
		// (ev.fromDate <= :fromdate
		// AND ev.toDate >=
		// :fromdate))");
		int flag = 0;
		int flagSecond = 0;
		if (deptid != null && deptid != 0) {
			flag = 1;
			query.append(" and ev.deptId.id=:deptid");
		}
		if (functionaryId != null && functionaryId != 0) {
			flagSecond = 1;
			query.append(" and ev.functionary.id=:functionaryId");
		}
		Query qry = HibernateUtil.getCurrentSession().createQuery(
				query.toString());

		qry.setString("date", formatter.format(todate.getTime()));
		// qry.setDate("fromdate",todate.getTime());
		if (flag == 1) {
			qry.setInteger("deptid", deptid);
		}
		if (flagSecond == 1) {
			qry.setInteger("functionaryId", functionaryId);
		}
		empPayrollList = qry.list();
		logger.info("payroll list ----------" + empPayrollList.size());
		return empPayrollList;
	}

	public List<Integer> getSuppPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(
			GregorianCalendar fromdate, GregorianCalendar todate,
			Integer deptid, Integer functionaryId, Long functionId,
			Integer billNumberId) {
		List<Integer> empPayrollList = new ArrayList<Integer>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		StringBuffer query = new StringBuffer(
				" select distinct(ev.id) from EmployeeView ev  "
						+ " where ev.id in "
						+ " (select ex.employee.idPersonalInformation from EmpException ex "
						+ " where ex.status.moduletype in ('EmpException') and ex.status.description in ('Approved') and (ex.fromDate is null or ex.fromDate<=to_date(:date,'dd/mm/yy') )) "
						+ " and ev.isActive=1 and ev.isPrimary='Y' ");// and
																		// ((ev.toDate
																		// IS
																		// NULL
		// and ev.fromDate <=
		// :fromdate ) OR
		// (ev.fromDate <= :fromdate
		// AND ev.toDate >=
		// :fromdate))");
		int flag = 0;
		int flagSecond = 0;
		if (deptid != null && deptid != 0) {
			flag = 1;
			query.append(" and ev.deptId.id=:deptid");
		}
		if (functionaryId != null && functionaryId != 0) {
			flagSecond = 1;
			query.append(" and ev.functionary.id=:functionaryId");
		}
		if (functionId != null && functionId != 0) {
			query.append(" and ev.functionId.id=:functionId");
		}
		if (billNumberId != null && billNumberId != 0) {
			query.append(" and ev.assignment.position.billNumber.id=:billNumberId");
		}
		Query qry = HibernateUtil.getCurrentSession().createQuery(
				query.toString());

		qry.setString("date", formatter.format(todate.getTime()));
		// qry.setDate("fromdate",todate.getTime());
		if (flag == 1) {
			qry.setInteger("deptid", deptid);
		}
		if (flagSecond == 1) {
			qry.setInteger("functionaryId", functionaryId);
		}
		if (functionId != null && functionId != 0) {
			qry.setLong("functionId", functionId);
		}
		if (billNumberId != null && billNumberId != 0) {
			qry.setInteger("billNumberId", billNumberId);
		}
		empPayrollList = qry.list();
		logger.info("payroll list ----------" + empPayrollList.size());
		return empPayrollList;
	}

	public List getSuppPayEligibleEmpListBetweenFDateAndTDateByDept(
			GregorianCalendar fromdate, GregorianCalendar todate, Integer deptid) {
		PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill
				.getPayrollExterInterface();
		List empPayrollList = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		EgwStatus employedStatus = payrollExternalInterface
				.getStatusByModuleAndDescription(
						PayrollConstants.EMPLOYEE_MODULE,
						PayrollConstants.EMPLOYEE_EMPLOYED);
		StringBuffer query = new StringBuffer(
				" select distinct(ev.id) from EmployeeView ev  "
						+ " where ev.id in "
						+ " (select ex.employee.idPersonalInformation from EmpException ex "
						+ " where ex.status.moduletype in ('EmpException') and ex.status.description in ('Approved') and (ex.fromDate is null or ex.fromDate<=to_date(:date,'dd/mm/yy') )) "
						+ " and ev.isActive=1 and ev.employeeStatus = :employedStatus ");
		int flag = 0;
		if (deptid != null && deptid != 0) {
			flag = 1;
			query.append(" and ev.deptId.id=:deptid");
		}
		Query qry = HibernateUtil.getCurrentSession().createQuery(
				query.toString());

		qry.setString("date", formatter.format(todate.getTime()));
		if (flag == 1) {
			qry.setInteger("deptid", deptid);
		}
		qry.setEntity("employedStatus", employedStatus);
		empPayrollList = qry.list();
		return empPayrollList;
	}

	public EmpPayroll getPrevApprovedPayslipForEmpByDates(
			GregorianCalendar fromDate, GregorianCalendar toDate, Integer empid)
			throws Exception {
		EmpPayroll empPayroll = null;
		try {
			GregorianCalendar finFromDate = (GregorianCalendar) fromDate
					.clone();
			String qry1 = "from EmpPayroll e where e.employee.idPersonalInformation=:empid and e.month=:month and e.payType.paytype=:paytype "
					+ "and e.financialyear.id in (select c.id from CFinancialYear c where c.startingDate<= :fromdate and c.endingDate>= :fromdate)"
					+ " and e.status.moduletype in ('PaySlip') and e.status.description in (:status) ";
			Query qry = HibernateUtil.getCurrentSession().createQuery(qry1);
			qry.setString("paytype",
					PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);
			String status = GenericDaoFactory
					.getDAOFactory()
					.getAppConfigValuesDAO()
					.getAppConfigValueByDate("Payslip",
							"PayslipAuditApprovedStatus", new Date())
					.getValue();
			qry.setString("status", status);
			if (fromDate.get(Calendar.MONTH) == toDate.get(Calendar.MONTH)) {
				// it will return 1 less value (starts with 0)
				if (fromDate.get(Calendar.MONTH) == 0) {
					qry.setInteger("month", 12); // previous to jan is Dec
				} else {
					qry.setInteger("month", fromDate.get(Calendar.MONTH));
				}
				if (fromDate.get(Calendar.MONTH) == 3) { // April - so
															// financial year
															// will change. so
															// fromDate cannot
															// be used to get
															// financial year.
					finFromDate.set(Calendar.MONTH, 2);
				}
			} else {
				qry.setInteger("month", fromDate.get(Calendar.MONTH) + 1);
			}

			qry.setDate("fromdate", finFromDate.getTime());
			qry.setInteger("empid", empid);
			empPayroll = (EmpPayroll) qry.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return empPayroll;
	}

	public EmpPayroll getPrevApprovedSuppPayslipForEmpByDates(
			GregorianCalendar fromDate, GregorianCalendar toDate, Integer empid)
			throws Exception {
		EmpPayroll empPayroll = null;
		GregorianCalendar finFromDate = (GregorianCalendar) fromDate.clone();
		try {
			String qry1 = "from EmpPayroll e where e.employee.idPersonalInformation=:empid and e.month=:month and e.payType.paytype=:paytype "
					+ "and e.financialyear.id in (select c.id from CFinancialYear c where c.startingDate<= :fromdate and c.endingDate>= :fromdate)"
					+ " and e.status.moduletype in ('PaySlip') and e.status.description in (:status) ";
			Query qry = HibernateUtil.getCurrentSession().createQuery(qry1);
			qry.setString("paytype",
					PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION);
			qry.setString(
					"status",
					GenericDaoFactory
							.getDAOFactory()
							.getAppConfigValuesDAO()
							.getAppConfigValueByDate("Payslip",
									"PayslipAuditApprovedStatus", new Date())
							.getValue());
			if (fromDate.get(Calendar.MONTH) == toDate.get(Calendar.MONTH)) {
				// it will return 1 less value (starts with 0)
				if (fromDate.get(Calendar.MONTH) == 0) {
					qry.setInteger("month", 12); // previous to jan is Dec
				} else

				{
					qry.setInteger("month", fromDate.get(Calendar.MONTH));
				}
				if (fromDate.get(Calendar.MONTH) == 3) { // April - so
															// financial year
															// will change. so
															// fromDate cannot
															// be used to get
															// financial year.
					finFromDate.set(Calendar.MONTH, 2);
				}
			} else {
				qry.setInteger("month", fromDate.get(Calendar.MONTH) + 1);
			}
			qry.setDate("fromdate", finFromDate.getTime());
			qry.setInteger("empid", empid);
			empPayroll = (EmpPayroll) qry.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return empPayroll;
	}

	public String getEmpInfoByLastAssignment(Integer empid) throws Exception {

		final String query = "SELECT EV.CODE||'~'||EV.NAME||'~'||EV.ID||'~'||DG.DESIGNATION_NAME||'~'||D.DEPT_NAME||'~'||to_char(EV.DATE_OF_FA,'dd/mm/yyyy' ) "
				+ "AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV ,EG_EMPLOYEE E,EG_DEPARTMENT D,EG_DESIGNATION DG "
				+ "WHERE DG.DESIGNATIONID = EV.DESIGNATIONID AND "
				+ "D.ID_DEPT = EV.DEPT_ID AND "
				+ "(EV.TO_DATE in(SELECT max(EV1.TO_DATE) FROM EG_EIS_EMPLOYEEINFO EV1 where EV1.ID = "
				+ empid
				+ ") ) AND "
				+ "E.ID = EV.ID(+) and "
				+ "ev.id="
				+ empid + " ORDER BY EV.CODE";
		return HibernateUtil.getCurrentSession().doReturningWork(
				new ReturningWork<String>() {
					ResultSet rs;

					@Override
					public String execute(Connection connection)
							throws SQLException {
						String code = "";
						rs = connection.createStatement().executeQuery(query);
						if (rs != null) {
							while (rs.next()) {
								code = rs.getString("code");
							}
						}
						return code;
					}
				});

	}

	public String getEmpInfoByFDateAndTDate(final Integer empid,
			GregorianCalendar fromdate, final GregorianCalendar todate)
			throws Exception {
		logger.info("getallemployeecodes");
		// hibernate query is giving some exception invalid column index
		String data = "";
		return HibernateUtil.getCurrentSession().doReturningWork(
				new ReturningWork<String>() {
					String code = "";
					Statement stmt = null;
					ResultSet rs = null;
					SimpleDateFormat formatter = new SimpleDateFormat(
							"dd/MM/yyyy", Locale.getDefault());
					String toDate = formatter.format(todate.getTime());
					String query = "SELECT EV.CODE||'~'||EV.NAME||'~'||EV.ID||'~'||DG.DESIGNATION_NAME||'~'||D.DEPT_NAME||'~'||to_char(EV.DATE_OF_FA,'dd/mm/yyyy' ) "
							+ "AS \"code\" FROM EG_EIS_EMPLOYEEINFO EV ,EG_EMPLOYEE E,EG_DEPARTMENT D,EG_DESIGNATION DG "
							+ "WHERE DG.DESIGNATIONID = EV.DESIGNATIONID AND "
							+ "D.ID_DEPT = EV.DEPT_ID AND "
							+ "((EV.TO_DATE IS NULL and EV.FROM_DATE <= to_date('"
							+ toDate
							+ "','dd/mm/yyyy') )"
							+ " OR (EV.FROM_DATE <= to_date('"
							+ toDate
							+ "','dd/mm/yyyy') AND "
							+ "EV.TO_DATE >= to_date('"
							+ toDate
							+ "','dd/mm/yyyy'))) AND "
							+ "E.ID = EV.ID(+) and "
							+ "ev.id=" + empid + " ORDER BY EV.CODE";

					public String execute(Connection connection)
							throws SQLException {
						try {
							stmt = connection.createStatement();
							rs = stmt.executeQuery(query);

							if (rs != null) {
								while (rs.next()) {
									code = rs.getString("code");
								}
							}
						}

						catch (SQLException e) {
							logger.error("Error:::: " + e.getMessage());
							throw e;
						} finally {
							HibernateUtil.release(stmt, rs);
						}

						return code;
					}
				});
	}

	public EmpPayroll getPrevApprovedPayslipForEmpByMonthAndYear(Integer empId,
			Integer month, Integer finYear) throws Exception {
		EmpPayroll empPayroll = null;
		try {
			String qry1 = "SELECT {pay.*} FROM EGPAY_EMPPAYROLL pay,egw_status s where pay.FINANCIALYEARID <= :year"
					+ " and ( ADD_MONTHS((SELECT financialyear.STARTINGDATE FROM FINANCIALYEAR where id = pay.financialyearid),pay.month- 4) -"
					+ " ADD_MONTHS((SELECT financialyear.STARTINGDATE FROM FINANCIALYEAR where id =:year ), :month - 4) ) < 0"
					+ " and pay.ID_EMPLOYEE = :empid and rownum =1"
					+ " and s.moduletype in ('PaySlip') and s.description in (:status)  ";
			SQLQuery qry = HibernateUtil.getCurrentSession()
					.createSQLQuery(qry1).addEntity("pay", EmpPayroll.class);
			qry.setString(
					"status",
					GenericDaoFactory
							.getDAOFactory()
							.getAppConfigValuesDAO()
							.getAppConfigValueByDate("Payslip",
									"PayslipAuditApprovedStatus", new Date())
							.getValue());
			qry.setInteger("year", finYear);
			qry.setInteger("month", month);
			qry.setInteger("empid", empId);
			empPayroll = (EmpPayroll) qry.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return empPayroll;

	}

	public List getPayslipsOfYearMonthStatusInternal(String year,
			BigDecimal month, EgwStatus status) {
		Query qry = getSession().createQuery(
				"from EmpPayroll ep where ep.financialyear.finYearRange=:year and "
						+ "ep.month =:month and ep.status =:status ");
		qry.setString("year", year);
		qry.setBigDecimal("month", month);
		qry.setEntity("status", status);

		return qry.list();
	}

	@Deprecated
	public BigDecimal getTotalDeductionAmount(Integer employeeId,
			CChartOfAccounts accountCode, Date toDate, CFinancialYear finYear)
			throws Exception {
		List<Deductions> dedList = new ArrayList<Deductions>();
		BigDecimal amount = BigDecimal.ZERO;
		int month = toDate.getMonth() + 1;
		try {
			Query qry = getSession()
					.createQuery(
							"select D from Deductions D, EmpPayroll P where "
									+ "D.empPayroll.id = P.id and "
									+ "P.month <= :month and "
									+ "P.financialyear = :finYear and "
									+ "P.employee.idPersonalInformation = :employeeId and "
									+ "D.salAdvances = null");
			qry.setInteger("employeeId", employeeId);
			qry.setInteger("month", month);
			qry.setEntity("finYear", finYear);
			dedList = qry.list();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		for (Deductions ded : dedList) {
			if (ded.getSalaryCodes() != null) {
				if (ded.getSalaryCodes().getChartofaccounts() != null
						&& ded.getSalaryCodes().getChartofaccounts()
								.equals(accountCode)) {
					amount = amount.add(ded.getAmount());
				}
				if (ded.getSalaryCodes().getTdsId() != null
						&& ded.getSalaryCodes().getTdsId().getChartofaccounts()
								.equals(accountCode)) {
					amount = amount.add(ded.getAmount());
				}
			}
			if (ded.getChartofaccounts() != null
					&& ded.getChartofaccounts().equals(accountCode)) {
				amount = amount.add(ded.getAmount());
			}
		}
		return amount;
	}

	public List<String> GetListOfDeductionAmount(Integer idEmployee,
			CChartOfAccounts accountCode, Date fromDate, Date toDate)
			throws Exception {
		final List<String> dateAmountList = new ArrayList<String>();
		final Integer idEmployeeFinal = idEmployee;
		final CChartOfAccounts accountCodeFinal = accountCode;
		final Date fromDateFinal = fromDate;
		final Date toDateFinal = toDate;
		return HibernateUtil.getCurrentSession().doReturningWork(
				new ReturningWork<List<String>>() {
					Statement stmt = null;
					ResultSet rs = null;
					Long glcodeId = accountCodeFinal.getId();
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",
							Locale.getDefault());
					String frmDate = sdf.format(fromDateFinal);
					String toDt = sdf.format(toDateFinal);

					public List<String> execute(Connection connection)
							throws SQLException {
						try {
							EgwStatus egwStatus = (EgwStatus) PayrollManagersUtill
									.getCommonsService()
									.getStatusByModuleAndCode(
											PayrollConstants.PAYSLIP_MODULE,
											GenericDaoFactory
													.getDAOFactory()
													.getAppConfigValuesDAO()
													.getAppConfigValueByDate(
															"Payslip",
															"PayslipAuditApprovedStatus",
															new Date())
													.getValue());

							String qry = "select  to_char(vou.VOUCHERDATE,'dd-MON-yy') || '->' || sum(d.amount) as code from egpay_deductions d, "
									+ "egpay_emppayroll pay, voucherheader vou,eg_billregister bill,eg_billregistermis billmis "
									+ "where vou.voucherdate>= to_date('"
									+ frmDate
									+ "','dd-MM-yyyy') and "
									+ "vou.voucherdate <= to_date('"
									+ toDt
									+ "','dd-MM-yyyy') and "
									+ "d.ID_EMPPAYROLL = pay.id and "
									+ "pay.ID_BILLREGISTER = bill.id and bill.id = billmis.billid and billmis.voucherheaderid = vou.id and "
									+ "pay.ID_EMPLOYEE= "
									+ idEmployeeFinal
									+ " and d.ID_SAL_ADVANCE is null and "
									+ "pay.status =   "
									+ egwStatus.getId()
									+ " and "
									+ "((d.ID_ACCOUNTCODE in (select id from chartofaccounts where id = "
									+ glcodeId
									+ ") ) or "
									+ "(d.id_salcode in (select sal1.ID  from  egpay_salarycodes sal1 where sal1.GLCODEID = "
									+ glcodeId
									+ ")) or "
									+ "(d.id_salcode in (select sal.ID from  egpay_salarycodes sal where sal.GLCODEID is null and "
									+ "sal.TDS_ID in (select tds.ID from tds where tds.GLCODEID = "
									+ glcodeId
									+ ")))) "
									+ "group by  vou.VOUCHERDATE";

							logger.info("qry----------" + qry);

							stmt = connection.createStatement();

							rs = stmt.executeQuery(qry);

							if (rs != null) {
								while (rs.next()) {
									dateAmountList.add(rs.getString("code"));
								}
							}
						} catch (SQLException e) {
							logger.error("Error:   " + e.getMessage());
							throw e;
						} finally {
							HibernateUtil.release(stmt, rs);
						}
						return dateAmountList;
					}
				});

	}

	public List<HashMap> getEarningsForEmployeeForFinYear(Integer employeeid,
			Integer finyearid) throws Exception {
		PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill
				.getPayrollExterInterface();

		final StringBuffer query = new StringBuffer(
				" SELECT salcodes.head salhead, sum (DECODE (mth, 4, cnt, NULL)) april, "
						+ "sum (DECODE (mth, 5, cnt, NULL)) may, sum (DECODE (mth, 6, cnt, NULL)) june,"
						+ "sum (DECODE (mth, 7, cnt, NULL)) july,sum (DECODE (mth, 8, cnt, NULL)) august,"
						+ "sum (DECODE (mth, 9, cnt, NULL)) september,sum (DECODE (mth, 10, cnt, NULL)) october,"
						+ "sum (DECODE (mth, 11, cnt, NULL)) november,sum (DECODE (mth, 12, cnt, NULL)) december,"
						+ "sum (DECODE (mth, 1, cnt, NULL)) january,sum (DECODE (mth, 2, cnt, NULL)) february,"
						+ "sum (DECODE (mth, 3, cnt, NULL)) march, SUM (cnt) AS YTD FROM "
						+ "(SELECT id_salcode AS salcode, amount AS cnt, MONTH mth FROM egpay_earnings ear, egpay_emppayroll payroll "
						+ "WHERE ear.id_emppayroll = payroll.ID AND id_employee = ? AND payroll.financialyearid = ? and payroll.status != ?),"
						+ "egpay_salarycodes salcodes WHERE salcode = salcodes.ID GROUP BY salcodes.head, salcodes.order_id  ORDER BY salcodes.order_id");
		final Integer empId = employeeid;
		final Integer finyearId = finyearid;
		final List<HashMap> results = new ArrayList<HashMap>();
		try {
			final EgwStatus egwStatus = (EgwStatus) payrollExternalInterface
					.getStatusByModuleAndDescription(
							PayrollConstants.PAYSLIP_MODULE,
							GenericDaoFactory
									.getDAOFactory()
									.getAppConfigValuesDAO()
									.getAppConfigValueByDate("Payslip",
											"PayslipCancelledStatus",
											new Date()).getValue());
			HibernateUtil.getCurrentSession().doWork(new Work() {
				PreparedStatement stmt = null;
				ResultSet rs = null;

				@Override
				public void execute(Connection connection) throws SQLException {
					try {
						stmt = connection.prepareStatement(query.toString());
						stmt.setInt(1, empId);
						stmt.setInt(2, finyearId);
						stmt.setInt(3, egwStatus.getId());
						rs = stmt.executeQuery();
						int i = 0;
						if (rs != null) {

							while (rs.next()) {
								HashMap temp = new HashMap();
								temp.put("type", "Earnings");
								temp.put("salhead", rs.getString("salhead"));
								temp.put("april", rs.getDouble("april"));
								temp.put("may", rs.getDouble("may"));
								temp.put("june", rs.getDouble("june"));
								temp.put("july", rs.getDouble("july"));
								temp.put("august", rs.getDouble("august"));
								temp.put("september", rs.getDouble("september"));
								temp.put("october", rs.getDouble("october"));
								temp.put("november", rs.getDouble("november"));
								temp.put("december", rs.getDouble("december"));
								temp.put("january", rs.getDouble("january"));
								temp.put("february", rs.getDouble("february"));
								temp.put("march", rs.getDouble("march"));
								temp.put("YTD", rs.getDouble("YTD"));
								results.add(i, temp);
								i++;
							}
						}
					} finally {
						HibernateUtil.release(stmt, rs);
					}

				}
			});
		} catch (Exception e) {
			throw e;
		}
		return results;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.egov.payroll.dao.EmpPayrollDAO#getEarningsAndDeductionsForEmpByMonthAndYearRange(java.lang.Integer,
	 *      java.util.Date,java.util.Date)
	 */
	public List<HashMap> getEarningsAndDeductionsForEmpByMonthAndYearRange(
			Integer employeeid, Date fromDate, Date toDate) throws Exception {
		PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill
				.getPayrollExterInterface();
		final List<HashMap> results = new ArrayList<HashMap>();

		StringBuffer earningAndDeductionQuery = new StringBuffer(
				"SELECT payid,fromdate ");
		// To get the from date and to date by giving the from/to month/year
		// id.
		// PayRollManager payrollManager =
		// PayrollManagersUtill.getPayRollManager();

		// To get the list of earnings/deductions salary heads

		final List earningsPayHeadList = PayrollManagersUtill
				.getPayheadService()
				.getAllSalaryCodesByTypeAsSortedByOrder("E");
		final List deductionsPayHeadList = PayrollManagersUtill
				.getPayheadService()
				.getAllSalaryCodesByTypeAsSortedByOrder("D");

		// To build the sql query for each earning and deduction head
		for (int e = 0; e < earningsPayHeadList.size(); e++) {
			SalaryCodes salaryCodeObj = (SalaryCodes) earningsPayHeadList
					.get(e);
			earningAndDeductionQuery.append(", sum (DECODE (salcode,")
					.append(salaryCodeObj.getId())
					.append(", earnings, NULL)) ");
			if (e == (earningsPayHeadList.size() - 1))// when last records
														// comes in
			{
				earningAndDeductionQuery.append(",SUM (earnings) AS Total");
			}
		}
		for (int d = 0; d < deductionsPayHeadList.size(); d++) {
			SalaryCodes salaryCodeObj = (SalaryCodes) deductionsPayHeadList
					.get(d);
			earningAndDeductionQuery.append(", sum (DECODE (salcode,")
					.append(salaryCodeObj.getId())
					.append(", deductions, NULL)) ");
			if (d == (deductionsPayHeadList.size() - 1))// when last records
														// comes in
			{
				earningAndDeductionQuery.append(",SUM (deductions) AS Total");
			}
		}

		// For netpay calculation
		earningAndDeductionQuery
				.append(" ,(SUM (earnings)-SUM (deductions)) as NetPay ");
		earningAndDeductionQuery.append(" FROM ");
		earningAndDeductionQuery
				.append("(SELECT * FROM (SELECT payroll.id as payid, ear.id_salcode AS salcode, ear.amount AS earnings,0 as deductions, payroll.MONTH as mth, payroll.FINANCIALYEARID as finyear,payroll.fromdate as fromdate "
						+ " FROM egpay_earnings ear, egpay_emppayroll payroll "
						+ " WHERE ear.id_emppayroll = payroll.ID AND payroll.id_employee = ? "
						+ " and payroll.status!= ? "
						+ " and payroll.fromdate >= ? and payroll.fromdate <= ? )"
						+ " union"
						+ " (SELECT payroll.id as payid, ded.id_salcode AS salcode, 0 AS earnings,ded.amount as deductions, payroll.MONTH as mth, payroll.FINANCIALYEARID as finyear,payroll.fromdate as fromdate  "
						+ " FROM egpay_deductions ded, egpay_emppayroll payroll "
						+ " WHERE ded.id_emppayroll = payroll.ID AND payroll.id_employee = ? "
						+ " and payroll.status!= ? "
						+ " and payroll.fromdate >= ? and payroll.fromdate <= ? ))");

		earningAndDeductionQuery.append(" GROUP BY payid , fromdate ");
		earningAndDeductionQuery.append(" ORDER BY fromdate ");

		EgwStatus egwStatus = (EgwStatus) payrollExternalInterface
				.getStatusByModuleAndDescription(
						PayrollConstants.PAYSLIP_MODULE,
						GenericDaoFactory
								.getDAOFactory()
								.getAppConfigValuesDAO()
								.getAppConfigValueByDate("Payslip",
										"PayslipCancelledStatus", new Date())
								.getValue());

		final String query = earningAndDeductionQuery.toString();
		final Integer empIdInt = employeeid;
		final Date from = fromDate;
		final Date to = toDate;
		final EgwStatus egwStatusFinal = egwStatus;
		HibernateUtil.getCurrentSession().doWork(new Work() {
			PreparedStatement stmt = null;
			ResultSet rs = null;

			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					stmt = connection.prepareStatement(query);

					stmt.setInt(1, empIdInt);
					stmt.setDate(3, new java.sql.Date(from.getTime()));
					stmt.setDate(4, new java.sql.Date(to.getTime()));
					stmt.setInt(5, empIdInt);
					stmt.setDate(7, new java.sql.Date(from.getTime()));
					stmt.setDate(8, new java.sql.Date(to.getTime()));
					stmt.setInt(2, egwStatusFinal.getId());
					stmt.setInt(6, egwStatusFinal.getId());
					rs = stmt.executeQuery();
					int i = 0;

					if (rs != null) {

						while (rs.next()) {

							HashMap rowValueMap = new LinkedHashMap();
							int columnCount = 3;
							rowValueMap.put("PayId", rs.getInt(1));
							rowValueMap.put("Month & Year",
									getMonthAndYearStr(rs.getDate(2)));
							for (int j = 0; j < earningsPayHeadList.size(); j++) {
								SalaryCodes salaryCodeObj = (SalaryCodes) earningsPayHeadList
										.get(j);
								rowValueMap.put(salaryCodeObj.getHead() + "",
										rs.getInt(columnCount));
								columnCount++;
							}

							rowValueMap.put("Total Earnings ",
									rs.getInt(columnCount++));

							for (int k = 0; k < deductionsPayHeadList.size(); k++) {
								SalaryCodes salaryCodeObj = (SalaryCodes) deductionsPayHeadList
										.get(k);
								rowValueMap.put(salaryCodeObj.getHead() + "",
										rs.getInt(columnCount));
								columnCount++;
							}

							rowValueMap.put("Total Deductions ",
									rs.getInt(columnCount++));
							rowValueMap.put("Net Pay ", rs.getInt(columnCount));

							results.add(i, rowValueMap);

							i++;
						}

					}
				} finally {
					HibernateUtil.release(stmt, rs);
				}
			}
		});
		return results;

	}

	/**
	 * Purpose : To get the concat of month and year.
	 * 
	 * @param fromdate
	 * @param month
	 * @return
	 */
	public String getMonthAndYearStr(Date fromdate) {
		// String strMonthAndYear="";
		// String mon ="";
		// Date tempDate = fromdate;
		SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy",
				Locale.getDefault());
		String monthYrFormatDate = sdf.format(fromdate);
		/*
		 * if(month==1) mon="Jan"; else if(month==2) mon="Feb"; else
		 * if(month==3) mon="Mar"; else if(month==4) mon="Apr"; else
		 * if(month==5) mon="May"; else if(month==6) mon="Jun"; else
		 * if(month==7) mon="Jul"; else if(month==8) mon="Aug"; else
		 * if(month==9) mon="Sep"; else if(month==10) mon="OCt"; else
		 * if(month==11) mon="Nov"; else if(month==12) mon="Dec";
		 */

		// strMonthAndYear = mon+" "+year;
		return monthYrFormatDate;
	}

	public List<HashMap> getDeductionsForEmployeeForFinYear(Integer employeeid,
			Integer finyearid) throws Exception {
		final PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill
				.getPayrollExterInterface();
		final Integer empId = employeeid;
		final Integer finId = finyearid;
		final List<HashMap> results = new ArrayList<HashMap>();

		final StringBuffer query = new StringBuffer(
				" SELECT salcodes.head salhead, sum (DECODE (mth, 4, cnt, NULL)) april,"
						+ "sum (DECODE (mth, 5, cnt, NULL)) may, sum (DECODE (mth, 6, cnt, NULL)) june,"
						+ "sum (DECODE (mth, 7, cnt, NULL)) july,sum (DECODE (mth, 8, cnt, NULL)) august,"
						+ "sum (DECODE (mth, 9, cnt, NULL)) september,sum (DECODE (mth, 10, cnt, NULL)) october,"
						+ "sum (DECODE (mth, 11, cnt, NULL)) november,sum (DECODE (mth, 12, cnt, NULL)) december,"
						+ "sum (DECODE (mth, 1, cnt, NULL)) january,sum (DECODE (mth, 2, cnt, NULL)) february,"
						+ "sum (DECODE (mth, 3, cnt, NULL)) march, SUM (cnt) AS YTD FROM "
						+ "(SELECT id_salcode AS salcode, amount AS cnt, MONTH mth FROM egpay_deductions ded, egpay_emppayroll payroll "
						+ "WHERE ded.id_emppayroll = payroll.ID AND id_employee = ? AND payroll.financialyearid = ? and payroll.status != ?),"
						+ "egpay_salarycodes salcodes WHERE salcode = salcodes.ID GROUP BY salcodes.head, salcodes.order_id  ORDER BY salcodes.order_id");

		return HibernateUtil.getCurrentSession().doReturningWork(
				new ReturningWork<List<HashMap>>() {
					PreparedStatement stmt = null;
					ResultSet rs = null;

					@Override
					public List<HashMap> execute(Connection connection)
							throws SQLException {
						try {

							stmt = connection.prepareStatement(query.toString());
							stmt.setInt(1, empId);
							stmt.setInt(2, finId);

							EgwStatus egwStatus = (EgwStatus) payrollExternalInterface
									.getStatusByModuleAndDescription(
											PayrollConstants.PAYSLIP_MODULE,
											GenericDaoFactory
													.getDAOFactory()
													.getAppConfigValuesDAO()
													.getAppConfigValueByDate(
															"Payslip",
															"PayslipCancelledStatus",
															new Date())
													.getValue());
							stmt.setInt(3, egwStatus.getId());
							rs = stmt.executeQuery();

							// double totalamts[]=new double[13];
							int i = 0;
							if (rs != null) {
								ArrayList results = new ArrayList();
								boolean isListEmpty = true;
								while (rs.next()) {
									isListEmpty = false;
									Map temp = new HashMap();
									temp.put("type", "Deductions");
									temp.put("salhead", rs.getString("salhead"));
									temp.put("april", rs.getDouble("april"));
									temp.put("may", rs.getDouble("may"));
									temp.put("june", rs.getDouble("june"));
									temp.put("july", rs.getDouble("july"));
									temp.put("august", rs.getDouble("august"));
									temp.put("september",
											rs.getDouble("september"));
									temp.put("october", rs.getDouble("october"));
									temp.put("november",
											rs.getDouble("november"));
									temp.put("december",
											rs.getDouble("december"));
									temp.put("january", rs.getDouble("january"));
									temp.put("february",
											rs.getDouble("february"));
									temp.put("march", rs.getDouble("march"));
									temp.put("YTD", rs.getDouble("YTD"));
									results.add(i, temp);
									i++;
								}

								if (isListEmpty) {
									Map temp = new HashMap();
									temp.put("type", "Deductions");
									temp.put("salhead", "");
									temp.put("april", 0.0);
									temp.put("may", 0.0);
									temp.put("june", 0.0);
									temp.put("july", 0.0);
									temp.put("august", 0.0);
									temp.put("september", 0.0);
									temp.put("october", 0.0);
									temp.put("november", 0.0);
									temp.put("december", 0.0);
									temp.put("january", 0.0);
									temp.put("february", 0.0);
									temp.put("march", 0.0);
									temp.put("YTD", 0.0);
									results.add(i, temp);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							HibernateUtil.release(stmt, rs);
						}

						return results;
					}
				});

	}

	/*
	 * Return latest drawn payslip for passing employee
	 */
	public EmpPayroll getLatestPayslipByEmp(PersonalInformation employee)
			throws Exception {
		try {
			EmpPayroll latestPayslip = null;
			Query qry = getSession()
					.createQuery(
							"from EmpPayroll P where P.createdDate in(select max(Pay.createdDate) from EmpPayroll Pay)");
			List<EmpPayroll> payslipList = qry.list();
			for (EmpPayroll tempPayslip : payslipList) {
				latestPayslip = tempPayslip;
				break;
			}
			return latestPayslip;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	public List<EmpPayroll> getAllPayslipByEmpMonthYearBasedOnDept(
			Integer deptId, Integer month, Integer year, Integer billNum) {

		List<EmpPayroll> payslips = new ArrayList<EmpPayroll>();
		StringBuffer mainQry = new StringBuffer(
				"from EmpPayroll pay where pay.month =:month "
						+ "  and pay.financialyear.id =:year and pay.status.description not in ( :status ) and pay.empAssignment.id in( select id from Assignment  where deptId.id= :deptId)");
		if (null != billNum && billNum != 0) {
			mainQry = mainQry.append(" and pay.billNumber.id =:billNo");
		}
		Query qry = getSession().createQuery(mainQry.toString());

		logger.info("QUERY..........................." + qry);
		qry.setInteger("month", month);
		qry.setInteger("deptId", deptId);
		qry.setInteger("year", year);
		qry.setString(
				"status",
				GenericDaoFactory
						.getDAOFactory()
						.getAppConfigValuesDAO()
						.getAppConfigValueByDate("Payslip",
								"PayslipCancelledStatus", new Date())
						.getValue());
		if (null != billNum && billNum != 0) {
			qry.setInteger("billNo", billNum);
		}
		payslips = qry.list();
		return payslips;

	}

	public Script getScript(String scriptName) {

		Query qry = getSession()
				.createQuery(
						"select S from org.egov.infstr.models.Script S where S.name=:name");
		qry.setString("name", scriptName);
		Script strScript = (Script) qry.uniqueResult();

		return strScript;
	}

	/**
	 * Get department payhead summary by date range
	 */
	public List<HashMap> getDeptPayheadSummary(final Integer month,
			final Integer finyear, Integer deptIds[], final Integer billNumberId)
			throws Exception {
		PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill
				.getPayrollExterInterface();
		final ArrayList results = new ArrayList();

		StringBuffer earningAndDeductionQuery = new StringBuffer(
				"SELECT deptId,payid,todate,billNumber,payscale,designation");
		// added payscale
		// To get the from date and to date by giving the from/to month/year
		// id.
		// PayRollManager payrollManager =
		// PayrollManagersUtill.getPayRollManager();

		// To get the list of earnings/deductions salary heads
		PayheadService payheadService = PayrollManagersUtill
				.getPayheadService();
		try {

			final List earningsPayHeadList = payheadService
					.getAllSalaryCodesByTypeAsSortedByOrder("E");
			final List deductionsPayHeadList = payheadService
					.getAllSalaryCodesByTypeAsSortedByOrder("D");

			// To build the sql query for each earning and deduction head
			for (int e = 0; e < earningsPayHeadList.size(); e++) {
				SalaryCodes salaryCodeObj = (SalaryCodes) earningsPayHeadList
						.get(e);
				earningAndDeductionQuery.append(", sum (DECODE (salcode,")
						.append(salaryCodeObj.getId())
						.append(", earnings, NULL)) ");
				if (e == (earningsPayHeadList.size() - 1))// when last records
															// comes in
				{
					earningAndDeductionQuery.append(",SUM (earnings) AS Total");
				}
			}
			for (int d = 0; d < deductionsPayHeadList.size(); d++) {
				SalaryCodes salaryCodeObj = (SalaryCodes) deductionsPayHeadList
						.get(d);
				earningAndDeductionQuery.append(", sum (DECODE (salcode,")
						.append(salaryCodeObj.getId())
						.append(", deductions, NULL)) ");
				if (d == (deductionsPayHeadList.size() - 1))// when last records
															// comes in
				{
					earningAndDeductionQuery
							.append(",SUM (deductions) AS Total");
				}
			}

			// String[] deptIdss = new String[deptIds.length];
			String deptIdss = "";
			int a = 1;
			for (Integer deptId : deptIds) {
				logger.info("DeptId----------------" + deptId);
				// deptIdss[a] = deptId.toString();

				if (a == deptIds.length) {
					deptIdss = deptIdss + deptId.toString();

				} else {
					deptIdss = deptIdss + deptId.toString() + ",";
				}
				a++;
			}

			String billNumberCond = "";
			if (billNumberId != null) {
				billNumberCond = " and payroll.ID_BILLNUMBER=? ";

			}
			// For netpay calculation
			earningAndDeductionQuery
					.append(" ,(SUM (earnings)-SUM (deductions)) as NetPay ");
			/* earningAndDeductionQuery.append(" ,designation "); */
			earningAndDeductionQuery.append(" FROM ");
			earningAndDeductionQuery
					.append("(SELECT * FROM (SELECT payroll.id as payid, ear.id_salcode AS salcode, ear.amount AS earnings,0 as deductions, payroll.MONTH as mth, payroll.FINANCIALYEARID as finyear,payroll.todate as todate, "
							+ " empInfo.dept_id as deptId, desig.designation_name AS designation, bill.billNumber as billNumber,ph.NAME AS payscale FROM egpay_earnings ear, egpay_emppayroll payroll, "
							+ " EGEIS_BILLNUMBER_MASTER bill, EG_EIS_EMPLOYEEINFO empInfo,eg_designation  desig, EGPAY_PAYSCALE_HEADER ph,EGPAY_PAYSCALE_EMPLOYEE ps "
							+ " WHERE ear.id_emppayroll = payroll.ID AND empInfo.designationid = desig.designationid "
							+ "  AND payroll.id_billnumber=bill.id AND payroll.id_employee=ps.ID_EMPLOYEE AND payroll.todate>=empInfo.from_date AND payroll.todate<=empInfo.to_date AND ps.ID_PAYHEADER=ph.ID AND payroll.id_employee in( empInfo.id) AND "
							+ " empInfo.is_primary='Y' AND empInfo.dept_id in("
							+ deptIdss
							+ ")"
							+ " and ps.id=(select max(id) from EGPAY_PAYSCALE_EMPLOYEE ps1 where ps1.EFFECTIVEFROM<= payroll.fromdate and ps1.ID_EMPLOYEE=payroll.id_employee)"
							+ " and payroll.status!= ? "
							+ billNumberCond
							+ " and payroll.month= ? and payroll.financialyearid= ? AND payroll.ID_EMP_ASSIGNMENT = empInfo.ass_id ) "
							+ " union all "
							+ " (SELECT payroll.id as payid, ded.id_salcode AS salcode, 0 AS earnings,ded.amount as deductions, payroll.MONTH as mth, payroll.FINANCIALYEARID as finyear,payroll.todate as todate,  "
							+ " empInfo.dept_id as deptId,desig.designation_name AS designation, bill.billNumber as billNumber,ph.NAME AS payscale FROM egpay_deductions ded, egpay_emppayroll payroll, "
							+ " EGEIS_BILLNUMBER_MASTER bill, EG_EIS_EMPLOYEEINFO empInfo, eg_designation  desig,EGPAY_PAYSCALE_HEADER ph,EGPAY_PAYSCALE_EMPLOYEE ps "
							+ " WHERE ded.id_emppayroll = payroll.ID AND empInfo.designationid = desig.designationid  "
							+ " AND payroll.id_billnumber=bill.id AND payroll.id_employee=ps.ID_EMPLOYEE AND payroll.todate>=empInfo.from_date AND payroll.todate<=empInfo.to_date AND ps.ID_PAYHEADER=ph.ID AND payroll.id_employee in( empInfo.id) AND "
							+ " empInfo.is_primary='Y' AND empInfo.dept_id in("
							+ deptIdss
							+ ") "
							+ " and ps.id=(select max(id) from EGPAY_PAYSCALE_EMPLOYEE ps1 where ps1.EFFECTIVEFROM<= payroll.fromdate and ps1.ID_EMPLOYEE=payroll.id_employee)"
							+ " and payroll.status!= ? "
							+ billNumberCond
							+ " and payroll.month= ? and payroll.financialyearid= ? AND payroll.ID_EMP_ASSIGNMENT = empInfo.ass_id ))");

			earningAndDeductionQuery
					.append(" GROUP BY deptId,todate,billNumber,designation,payid,payscale ");
			earningAndDeductionQuery
					.append(" ORDER BY deptId,todate,billNumber,designation,payid,payscale");

			final EgwStatus egwStatus = (EgwStatus) payrollExternalInterface
					.getStatusByModuleAndDescription(
							PayrollConstants.PAYSLIP_MODULE,
							GenericDaoFactory
									.getDAOFactory()
									.getAppConfigValuesDAO()
									.getAppConfigValueByDate("Payslip",
											"PayslipCancelledStatus",
											new Date()).getValue());

			final String query = earningAndDeductionQuery.toString();
			HibernateUtil.getCurrentSession().doWork(new Work() {
				PreparedStatement stmt = null;
				ResultSet rs = null;

				@Override
				public void execute(Connection connection) throws SQLException {
					try {

						if (billNumberId == null) {
							stmt.setInt(1, egwStatus.getId());
							stmt.setBigDecimal(2, BigDecimal.valueOf(Long
									.parseLong(month.toString())));
							stmt.setLong(3, finyear);
							stmt.setInt(4, egwStatus.getId());
							stmt.setBigDecimal(5, BigDecimal.valueOf(Long
									.parseLong(month.toString())));
							stmt.setLong(6, finyear);
						}
						if (billNumberId != null) {
							stmt.setInt(1, egwStatus.getId());
							stmt.setInt(2, billNumberId);
							stmt.setBigDecimal(3, BigDecimal.valueOf(Long
									.parseLong(month.toString())));
							stmt.setLong(4, finyear);
							stmt.setInt(5, egwStatus.getId());
							stmt.setInt(6, billNumberId);
							stmt.setBigDecimal(7, BigDecimal.valueOf(Long
									.parseLong(month.toString())));
							stmt.setLong(8, finyear);
						}
						// stmt.setInt(1,employeeid);
						// ArrayDescriptor arrayDescriptor =
						// ArrayDescriptor.createDescriptor("INT_ARRAY",
						// connection);
						// java.sql.Array sqlArray = new
						// oracle.sql.ARRAY(arrayDescriptor,
						// connection, deptIds);
						// stmt.setArray(1,sqlArray);

						// stmt.setInt(5,employeeid);
						// stmt.setArray(5,sqlArray);

						// stmt.setInt(2,egwStatus.getId());

						rs = stmt.executeQuery();
						logger.debug("deptPayheadsummary query executed succesfully-");
						int i = 0;

						if (rs != null) {

							ArrayList results = new ArrayList();
							Long tempDept = 0L;
							HashMap earPayheadMap = new HashMap();
							HashMap dedPayheadMap = new HashMap();
							while (rs.next()) {

								Map rowValueMap = new LinkedHashMap();
								/*
								 * int columnCount = 5;//4; added 1 more
								 * column-billNumber in select clause
								 */
								int columnCount = 7;
								rowValueMap.put("Test", i);
								rowValueMap.put("DeptId", rs.getLong(1));
								rowValueMap.put("PayId", rs.getLong(2));
								// Department deptObj =
								// PayrollManagersUtill.getCommonsManager().find
								rowValueMap.put("Department",
										getDeptByPayslipId(rs.getLong(2)));

								rowValueMap.put("Employee Code",
										getEmpCodeByPayslipId(rs.getLong(2)));
								// rowValueMap.put("Employee Name",
								// getEmpNameByPayslipId(rs.getLong(2)));
								rowValueMap.put("Employee Name",
										getEmpNameByPayslipIdwithGender(rs
												.getLong(2)));
								rowValueMap.put("MonthAndYear",
										getMonthAndYearStr(rs.getDate(3)));
								// billNumber,instamt,numofinst,instno
								rowValueMap.put("billNumber", rs.getString(4));
								rowValueMap.put("gpfinstamt",
										getInstAmount(rs.getLong(2), "GPFADV"));
								rowValueMap.put("gpfnumofinst",
										getNumOfInst(rs.getLong(2), "GPFADV"));
								rowValueMap.put("gpfinstno",
										getInstNo(rs.getLong(2), "GPFADV"));
								rowValueMap.put("festinstamt",
										getInstAmount(rs.getLong(2), "FESTADV"));
								rowValueMap.put("festnumofinst",
										getNumOfInst(rs.getLong(2), "FESTADV"));
								rowValueMap.put("festinstno",
										getInstNo(rs.getLong(2), "FESTADV"));
								rowValueMap.put("Payscale", rs.getString(5));
								rowValueMap.put("Designation", rs.getString(6));

								for (int j = 0; j < earningsPayHeadList.size(); j++) {
									SalaryCodes salaryCodeObj = (SalaryCodes) earningsPayHeadList
											.get(j);
									rowValueMap.put(salaryCodeObj.getHead()
											+ "", rs.getInt(columnCount));
									Integer sumAmnt = (Integer) earPayheadMap
											.get(salaryCodeObj.getHead() + "");
									if (sumAmnt == null) {
										earPayheadMap.put(
												salaryCodeObj.getHead() + "",
												rs.getInt(columnCount));
									} else {
										sumAmnt += rs.getInt(columnCount);
										earPayheadMap.put(
												salaryCodeObj.getHead() + "",
												sumAmnt);
									}
									columnCount++;
								}

								rowValueMap.put("Total Earnings",
										rs.getInt(columnCount++));

								for (int k = 0; k < deductionsPayHeadList
										.size(); k++) {
									SalaryCodes salaryCodeObj = (SalaryCodes) deductionsPayHeadList
											.get(k);
									rowValueMap.put(salaryCodeObj.getHead()
											+ "", rs.getInt(columnCount));
									Integer sumAmnt = (Integer) dedPayheadMap
											.get(salaryCodeObj.getHead() + "");
									if (sumAmnt == null) {
										dedPayheadMap.put(
												salaryCodeObj.getHead() + "",
												rs.getInt(columnCount));
									} else {
										sumAmnt += rs.getInt(columnCount);
										dedPayheadMap.put(
												salaryCodeObj.getHead() + "",
												sumAmnt);
									}
									columnCount++;
								}

								rowValueMap.put("Total Deductions",
										rs.getInt(columnCount++));
								rowValueMap.put("Net Pay",
										rs.getInt(columnCount));
								/*
								 * rowValueMap.put("Designation",
								 * rs.getInt(columnCount));
								 */

								results.add(i, rowValueMap);

								tempDept = rs.getLong(1);
								i++;
							}

						}
					} finally {
						HibernateUtil.release(stmt, rs);
					}

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			throw e;
		}
		return results;

	}

	/**
	 * Get Functionary and Dept Wise PayBillSummary for a month and
	 * financialyear
	 */
	public List<HashMap> getFunctionaryDeptWisePayBillSummary(final Integer month,
			final Integer finYear, final Integer functionaryId, final Integer deptId)
			throws Exception {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		final ArrayList results = new ArrayList();

		StringBuffer earningAndDeductionQuery = new StringBuffer(
				"SELECT functionaryId,dept,payid,empId,fromdate,todate ");

		// To get the list of earnings/deductions salary heads
		PayheadService payheadService = PayrollManagersUtill
				.getPayheadService();
		try {
			final List earningsPayHeadList = payheadService
					.getAllSalaryCodesByTypeAsSortedByOrder("E");
			final List deductionsPayHeadList = payheadService
					.getAllSalaryCodesByTypeAsSortedByOrder("D");

			// To build the sql query for each earning and deduction head
			for (int e = 0; e < earningsPayHeadList.size(); e++) {
				SalaryCodes salaryCodeObj = (SalaryCodes) earningsPayHeadList
						.get(e);
				earningAndDeductionQuery.append(", sum (DECODE (salcode,")
						.append(salaryCodeObj.getId())
						.append(", earnings, NULL)) ");
				if (e == (earningsPayHeadList.size() - 1))// when last records
															// comes in
				{
					earningAndDeductionQuery.append(",SUM (earnings) AS Total");
				}
			}
			for (int d = 0; d < deductionsPayHeadList.size(); d++) {
				SalaryCodes salaryCodeObj = (SalaryCodes) deductionsPayHeadList
						.get(d);
				earningAndDeductionQuery.append(", sum (DECODE (salcode,")
						.append(salaryCodeObj.getId())
						.append(", deductions, NULL)) ");
				if (d == (deductionsPayHeadList.size() - 1))// when last records
															// comes in
				{
					earningAndDeductionQuery
							.append(",SUM (deductions) AS Total");
				}
			}

			// For netpay calculation
			earningAndDeductionQuery
					.append(" ,(SUM (earnings)-SUM (deductions)) as NetPay ");
			earningAndDeductionQuery.append(" FROM ");
			earningAndDeductionQuery
					.append("(SELECT * FROM (SELECT payroll.id as payid, ear.id_salcode AS salcode, ear.amount AS earnings,0 as deductions, payroll.MONTH as mth, payroll.FINANCIALYEARID as finyear,payroll.fromdate as fromdate,payroll.todate as todate, "
							+ " ass.id_functionary as functionaryId,ass.main_dept as dept,payroll.id_employee empId FROM egpay_earnings ear, egpay_emppayroll payroll,eg_emp_assignment ass "
							+ " WHERE ear.id_emppayroll = payroll.ID AND payroll.id_employee in "
							+ "(select id from eg_eis_employeeinfo empview "
							+ " where empview.functionary_id =?"
							+ " and empview.dept_id = ? ) "
							+ " and payroll.status!= ? "
							+ " and payroll.month = ? and payroll.financialyearid = ? AND payroll.ID_EMP_ASSIGNMENT = ass.id )"
							+ " union"
							+ " (SELECT payroll.id as payid, ded.id_salcode AS salcode, 0 AS earnings,ded.amount as deductions, payroll.MONTH as mth, payroll.FINANCIALYEARID as finyear,payroll.fromdate as fromdate,payroll.todate as todate,   "
							+ " ass.id_functionary as functionaryId,ass.main_dept as dept,payroll.id_employee empId FROM egpay_deductions ded, egpay_emppayroll payroll, eg_emp_assignment ass "
							+ " WHERE ded.id_emppayroll = payroll.ID AND payroll.id_employee in "
							+ "(select id from eg_eis_employeeinfo empview "
							+ " where empview.functionary_id =?"
							+ " and empview.dept_id = ? ) "
							+ " and payroll.status!= ? "
							+ " and payroll.month = ? and payroll.financialyearid = ? AND payroll.ID_EMP_ASSIGNMENT = ass.id ))");

			earningAndDeductionQuery
					.append(" GROUP BY functionaryId,dept,payid,empId,fromdate,todate ");
			earningAndDeductionQuery
					.append(" ORDER BY functionaryId,dept,payid,empId,fromdate,todate ");
			final String query = earningAndDeductionQuery.toString();

			final EgwStatus egwStatus = (EgwStatus) PayrollManagersUtill
					.getCommonsService().getStatusByModuleAndCode(
							PayrollConstants.PAYSLIP_MODULE,
							GenericDaoFactory
									.getDAOFactory()
									.getAppConfigValuesDAO()
									.getAppConfigValueByDate("Payslip",
											"PayslipCancelledStatus",
											new Date()).getValue());
			final Integer funcId = functionaryId;
			final Integer departmentId = deptId;
			final Integer monthId = month;
			final Integer finYearId = finYear;

			HibernateUtil.getCurrentSession().doWork(new Work() {
				PreparedStatement stmt = null;
				ResultSet rs = null;

				@Override
				public void execute(Connection connection) throws SQLException {
					try {
						stmt = connection.prepareStatement(query);

						stmt.setInt(1, functionaryId.intValue());
						stmt.setInt(2, deptId.intValue());
						stmt.setInt(3, egwStatus.getId());
						stmt.setInt(4, month);
						stmt.setInt(5, finYear);
						stmt.setInt(6, functionaryId.intValue());
						stmt.setInt(7, deptId.intValue());
						stmt.setInt(8, egwStatus.getId());
						stmt.setInt(9, month);
						stmt.setInt(10, finYear);
						rs = stmt.executeQuery();
						int i = 0;
						int srlNo = 1;
						if (rs != null) {
							ArrayList results = new ArrayList();
							Integer tempFunctionary = 0;
							Long functionaryWiseNetPayTotal = 0L;
							Long grandNetPayTotal = 0L;
							HashMap earPayheadMap = new HashMap();
							HashMap dedPayheadMap = new HashMap();
							// while (rs.next()) {
							// if(!rs.isBeforeFirst() && !rs.isAfterLast()){
							// //Check if
							// resultset is empty - This code is not working.
							if (rs.isBeforeFirst() && rs.isAfterLast()) {
								logger.info("Result set is empty");
							} else {
								while (rs.next()) {
									if ((srlNo - 1) % 15 == 0 && srlNo != 1) { // for
																				// each
																				// ten
																				// records

										HashMap totalMap = new HashMap();
										totalMap.put("SrlNo", " ");
										totalMap.put("FunctionaryId", " ");
										totalMap.put("DeptId", " ");
										totalMap.put("DeptName", " ");
										totalMap.put("PayId", " ");
										totalMap.put("FunctionaryName", " ");
										totalMap.put("EmployeeCode", " ");
										totalMap.put("EmployeeName",
												"Page Total");
										totalMap.put("BankDetail", " ");
										totalMap.put("BasicAndGradePay", " ");
										totalMap.put("AbsentAmountAndDays", " ");
										totalMap.put("MonthAndYear", " ");

										totalMap.putAll(earPayheadMap);
										totalMap.putAll(dedPayheadMap);
										totalMap.put("NetPay",
												functionaryWiseNetPayTotal
														.toString());
										earPayheadMap = new HashMap();
										dedPayheadMap = new HashMap();
										functionaryWiseNetPayTotal = 0L;
										results.add(i++, totalMap);
									}

									Map rowValueMap = new LinkedHashMap();
									int columnCount = 7; // no of column in
															// (SELECT
															// functionaryId,dept,payid,empId,fromdate)
															// + 1

									Map tempEmpMap = new HashMap();
									tempEmpMap = getEmpDetailsByPayslipId(rs
											.getInt(3));
									rowValueMap.put("SrlNo", srlNo);
									rowValueMap.put("FunctionaryId",
											rs.getInt(1));
									rowValueMap.put("DeptId", rs.getInt(2));
									rowValueMap.put("DeptName",
											tempEmpMap.get("deptName"));
									rowValueMap.put("PayId", rs.getInt(3));
									rowValueMap.put("FunctionaryName",
											tempEmpMap.get("functionaryName"));
									rowValueMap.put("EmployeeCode",
											tempEmpMap.get("empCode"));
									rowValueMap.put("EmployeeName",
											tempEmpMap.get("empName"));
									rowValueMap.put("CreatedBy",
											tempEmpMap.get("createdBy"));
									rowValueMap.put("CreatedDate",
											tempEmpMap.get("createdDate"));

									// To get the bank detail of employee
									String tmpBankDtl = "";

									if (!("").equals(tempEmpMap.get("bankName")
											.toString())) {
										tmpBankDtl = tempEmpMap.get("bankName")
												.toString();
									}

									// if both bank name and account no. not
									// empty
									if (!("").equals(tempEmpMap.get(
											"branchName").toString())
											&& !("").equals(tempEmpMap.get(
													"accountNo").toString())) {
										tmpBankDtl = tmpBankDtl
												+ "/"
												+ tempEmpMap.get("branchName")
														.toString()
												+ "-"
												+ tempEmpMap.get("accountNo")
														.toString();
									}
									// if bank name not empty
									else if (!("").equals(tempEmpMap.get(
											"branchName").toString())
											&& ("").equals(tempEmpMap.get(
													"accountNo").toString())) {
										tmpBankDtl = tmpBankDtl
												+ "/"
												+ tempEmpMap.get("branchName")
														.toString();
									}
									// if account no. not empty
									else if (("").equals(tempEmpMap.get(
											"branchName").toString())
											&& !("").equals(tempEmpMap.get(
													"accountNo").toString())) {
										tmpBankDtl = tmpBankDtl
												+ "/"
												+ tempEmpMap.get("accountNo")
														.toString();
									}

									rowValueMap.put("BankDetail", ""
											+ tmpBankDtl);

									rowValueMap.put("MonthAndYear",
											getMonthAndYearStr(rs.getDate(6)));

									for (int j = 0; j < earningsPayHeadList
											.size(); j++) {
										SalaryCodes salaryCodeObj = (SalaryCodes) earningsPayHeadList
												.get(j);
										rowValueMap.put(salaryCodeObj.getHead()
												+ "", rs.getInt(columnCount));
										Integer sumAmnt = (Integer) earPayheadMap
												.get(salaryCodeObj.getHead()
														+ "");
										if (sumAmnt == null) {
											earPayheadMap.put(
													salaryCodeObj.getHead()
															+ "",
													rs.getInt(columnCount));
										} else {
											sumAmnt += rs.getInt(columnCount);
											earPayheadMap.put(
													salaryCodeObj.getHead()
															+ "", sumAmnt);
										}
										columnCount++;
									}

									rowValueMap.put("TotalEarnings",
											rs.getInt(columnCount++));

									for (int k = 0; k < deductionsPayHeadList
											.size(); k++) {
										SalaryCodes salaryCodeObj = (SalaryCodes) deductionsPayHeadList
												.get(k);
										rowValueMap.put(salaryCodeObj.getHead()
												+ "", rs.getInt(columnCount));
										Integer sumAmnt = (Integer) dedPayheadMap
												.get(salaryCodeObj.getHead()
														+ "");
										if (sumAmnt == null) {
											dedPayheadMap.put(
													salaryCodeObj.getHead()
															+ "",
													rs.getInt(columnCount));
										} else {
											sumAmnt += rs.getInt(columnCount);
											dedPayheadMap.put(
													salaryCodeObj.getHead()
															+ "", sumAmnt);
										}
										columnCount++;
									}

									rowValueMap.put("TotalDeductions",
											rs.getInt(columnCount++));
									rowValueMap.put("NetPay",
											rs.getInt(columnCount));
									functionaryWiseNetPayTotal = functionaryWiseNetPayTotal
											+ Long.valueOf(rs
													.getInt(columnCount));
									grandNetPayTotal = grandNetPayTotal
											+ +Long.valueOf(rs
													.getInt(columnCount));

									// For getting basic and grade pay
									// calculation - Change
									// Payheads wrt nmc db. These values has to
									// be read from
									// payroll constants.
									String sumOfBasicGradePay = BigDecimal
											.valueOf(
													Long.valueOf(rowValueMap
															.get(PayrollConstants.BASIC)
															.toString())
															+ Long.valueOf(rowValueMap
																	.get(PayrollConstants.GRADEPAY)
																	.toString()))
											.toPlainString();
									rowValueMap.put(
											"BasicAndGradePay",
											""
													+ rowValueMap
															.get(PayrollConstants.BASIC)
													+ "/"
													+ (rowValueMap
															.get(PayrollConstants.GRADEPAY) != null ? ("" + rowValueMap
															.get(PayrollConstants.GRADEPAY))
															: "") + "/"
													+ sumOfBasicGradePay);

									// For getting absent amount and absent days
									EmployeeAttendenceReport empAtt = new EmployeeAttendenceReport();
									PersonalInformation personalInformation = new PersonalInformation();
									personalInformation = PayrollManagersUtill
											.getEmployeeService()
											.getEmloyeeById(rs.getInt(4));
									CFinancialYear finYearObj = PayrollManagersUtill
											.getCommonsService()
											.getFinancialYearById(
													Long.valueOf(finYear));
									empAtt = PayrollManagersUtill
											.getEmpLeaveService()
											.getEmployeeAttendenceReportBetweenTwoDates(
													rs.getDate(5),
													rs.getDate(6),
													personalInformation);

									Double presentDays = empAtt
											.getNoOfPresents().doubleValue();
									Double unPaidDays = empAtt
											.getNoOfUnPaidleaves()
											.doubleValue();

									String absentAmount = "0";

									if (presentDays != 0.0 && unPaidDays != 0.0) {
										absentAmount = BigDecimal
												.valueOf(
														Long.valueOf(
																rowValueMap
																		.get("TotalEarnings")
																		.toString())
																.doubleValue()
																* unPaidDays
																/ presentDays)
												.setScale(2,
														BigDecimal.ROUND_UP)
												.toPlainString();
									}

									rowValueMap.put(
											"AbsentAmountAndDays",
											""
													+ (Double
															.valueOf(absentAmount)
															- Double.valueOf(
																	absentAmount)
																	.longValue() > 0 ? absentAmount
															: Double.valueOf(
																	absentAmount)
																	.longValue())
													+ "/"
													+ (Double.valueOf(unPaidDays
															.toString())
															- Double.valueOf(
																	unPaidDays
																			.toString())
																	.longValue() > 0 ? unPaidDays
															.toString()
															: Double.valueOf(
																	unPaidDays
																			.toString())
																	.longValue()));

									results.add(i, rowValueMap);

									tempFunctionary = rs.getInt(1);
									i++;
									srlNo++;
								}
							}

							if (srlNo > 1) {

								if (srlNo - 1 % 15 != 0) {// this for printing
															// in last page
															// which contains
															// less than 10
															// records on that
															// page.
									// This for finally functionarywise total
									// row.
									HashMap totalMap1 = new HashMap();
									totalMap1.put("SrlNo", " ");
									totalMap1.put("FunctionaryId", " ");
									totalMap1.put("DeptId", " ");
									totalMap1.put("DeptName", " ");
									totalMap1.put("PayId", " ");
									totalMap1.put("FunctionaryName", " ");
									totalMap1.put("EmployeeCode", " ");
									totalMap1.put("EmployeeName", "Page Total");
									totalMap1.put("BankDetail", " ");
									totalMap1.put("BasicAndGradePay", " ");
									totalMap1.put("AbsentAmountAndDays", " ");
									totalMap1.put("MonthAndYear", " ");
									totalMap1.putAll(earPayheadMap);
									totalMap1.putAll(dedPayheadMap);
									totalMap1.put("NetPay",
											functionaryWiseNetPayTotal
													.toString());
									results.add(i, totalMap1);
								}

								// This for finally functionarywise total row.
								HashMap totalMap2 = new HashMap();
								totalMap2.put("SrlNo", " ");
								totalMap2.put("FunctionaryId", " ");
								totalMap2.put("DeptId", " ");
								totalMap2.put("DeptName", " ");
								totalMap2.put("PayId", " ");
								totalMap2.put("FunctionaryName", " ");
								totalMap2.put("EmployeeCode", " ");
								totalMap2.put("EmployeeName", "Grand Total");
								totalMap2.put("BankDetail", " ");
								totalMap2.put("BasicAndGradePay", " ");
								totalMap2.put("AbsentAmountAndDays", " ");
								totalMap2.put("MonthAndYear", " ");
								totalMap2.putAll(earPayheadMap);
								totalMap2.putAll(dedPayheadMap);
								totalMap2.put("NetPay",
										grandNetPayTotal.toString());
								results.add(++i, totalMap2);
							}

						}
					}

					finally {
						HibernateUtil.release(stmt, rs);
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			throw e;
		}
		return results;

	}

	private String getEmpCodeByPayslipId(Long payId) {
		EmpPayroll payslipObj = (EmpPayroll) findById(payId, false);
		return payslipObj.getEmployee().getEmployeeCode();
	}

	private String getEmpNameByPayslipIdwithGender(Long payId) {

		logger.debug(" payId " + payId);
		EmpPayroll payslipObj = (EmpPayroll) findById(payId, false);
		String empName = payslipObj.getEmployee().getEmployeeFirstName();
		String empMiddleName = payslipObj.getEmployee().getEmployeeMiddleName();
		String empLastName = payslipObj.getEmployee().getEmployeeLastName();

		if (empMiddleName != null) {
			empName = empName + " " + empMiddleName;
		}
		if (empLastName != null) {
			empName = empName + " " + empLastName;
		}

		if (payslipObj.getEmployee().getGender() == 'M') {
			empName = "Mr. " + empName;

		} else if (payslipObj.getEmployee().getGender() == 'F') {
			empName = "Smt. " + empName;
		}

		return empName;
	}

	private String getDeptByPayslipId(Long payId) {
		EmpPayroll payslipObj = (EmpPayroll) findById(payId, false);
		return payslipObj.getEmpAssignment().getDeptId().getDeptName();
	}

	private Map getEmpDetailsByPayslipId(Integer payId) {
		Map empDetailMap = new HashMap();
		EmpPayroll empObj = (EmpPayroll) findById(
				Long.valueOf(payId.toString()), false);
		empDetailMap.put("empCode", empObj.getEmployee().getEmployeeCode());
		empDetailMap.put("empName", empObj.getEmployee().getEmployeeName());
		empDetailMap.put("deptName", empObj.getEmpAssignment().getDeptId()
				.getDeptName());
		empDetailMap.put("functionaryName", empObj.getEmpAssignment()
				.getFunctionary().getName());
		empDetailMap.put("createdBy", empObj.getCreatedBy().getUserName());
		empDetailMap.put("createdDate", empObj.getCreatedDate());

		Set<BankDet> bankDtlSet = new HashSet<BankDet>();
		bankDtlSet = empObj.getEmployee().getEgpimsBankDets();

		BankDet bankDtl = null;

		for (Iterator itr = bankDtlSet.iterator(); itr.hasNext();) {
			bankDtl = (BankDet) itr.next();
			break;
		}

		if (bankDtl != null) {
			if (bankDtl.getBank().getId() != null) {
				empDetailMap.put("bankName", bankDtl.getBank().getName());
			} else {
				empDetailMap.put("bankName", "");
			}

			if (bankDtl.getBranch() != null) {
				empDetailMap.put("branchName", bankDtl.getBranch()
						.getBranchname());
			} else {
				empDetailMap.put("branchName", "");
			}
			empDetailMap.put("accountNo", bankDtl.getAccountNumber());
		} else {
			empDetailMap.put("bankName", "");
			empDetailMap.put("branchName", "");
			empDetailMap.put("accountNo", "");
		}

		return empDetailMap;
	}

	/**
	 * Get Functionary payhead summary by date range
	 */
	public List<HashMap> getFunctionaryPayheadSummary(Integer month,
			Integer year, Integer functionaryIds[], Integer billNumberId)
			throws Exception {
		// PayrollExternalInterface payrollExternalInterface=new
		// PayrollExternalImpl();
		HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<List<HashMap>>() {
			@Override
			public List<HashMap> execute(Connection connection)
			{
				return null;
			}
		}
		);

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
			StringBuffer earningAndDeductionQuery = new StringBuffer(
					"SELECT functionaryId,payid,todate ");
			// To get the from date and to date by giving the from/to month/year
			// id.
			// PayRollManager payrollManager =
			// PayrollManagersUtill.getPayRollManager();

			// To get the list of earnings/deductions salary heads
			PayheadService payheadService = PayrollManagersUtill
					.getPayheadService();
			List earningsPayHeadList = payheadService
					.getAllSalaryCodesByTypeAsSortedByOrder("E");
			List deductionsPayHeadList = payheadService
					.getAllSalaryCodesByTypeAsSortedByOrder("D");

			// To build the sql query for each earning and deduction head
			for (int e = 0; e < earningsPayHeadList.size(); e++) {
				SalaryCodes salaryCodeObj = (SalaryCodes) earningsPayHeadList
						.get(e);
				earningAndDeductionQuery.append(", sum (DECODE (salcode,")
						.append(salaryCodeObj.getId())
						.append(", earnings, NULL)) ");
				if (e == (earningsPayHeadList.size() - 1))// when last records
															// comes in
				{
					earningAndDeductionQuery.append(",SUM (earnings) AS Total");
				}
			}
			for (int d = 0; d < deductionsPayHeadList.size(); d++) {
				SalaryCodes salaryCodeObj = (SalaryCodes) deductionsPayHeadList
						.get(d);
				earningAndDeductionQuery.append(", sum (DECODE (salcode,")
						.append(salaryCodeObj.getId())
						.append(", deductions, NULL)) ");
				if (d == (deductionsPayHeadList.size() - 1))// when last records
															// comes in
				{
					earningAndDeductionQuery
							.append(",SUM (deductions) AS Total");
				}
			}
			String functionaryIdss = "";
			int a = 1;
			for (Integer functionaryId : functionaryIds) {
				logger.info("FunctionaryId-------------------" + functionaryId);
				// deptIdss[a] = deptId.toString();
				if (a == functionaryIds.length) {
					functionaryIdss = functionaryIdss
							+ functionaryId.toString();

				} else {
					functionaryIdss = functionaryIdss
							+ functionaryId.toString() + ",";
				}
				a++;
			}
			String billNumberCond = "";
			if (billNumberId != 0) {
				billNumberCond = " and payroll.ID_BILLNUMBER=? ";

			}

			// For netpay calculation
			earningAndDeductionQuery
					.append(" ,(SUM (earnings)-SUM (deductions)) as NetPay ");
			earningAndDeductionQuery.append(" FROM ");
			earningAndDeductionQuery
					.append("(SELECT * FROM (SELECT payroll.id as payid, ear.id_salcode AS salcode, ear.amount AS earnings,0 as deductions, payroll.MONTH as mth, payroll.FINANCIALYEARID as finyear,payroll.todate as todate, "
							+ " ass.id_functionary as functionaryId  FROM egpay_earnings ear, egpay_emppayroll payroll,eg_emp_assignment ass "
							+ " WHERE ear.id_emppayroll = payroll.ID AND payroll.id_employee in "
							+ "(select id from eg_eis_employeeinfo where functionary_id in("
							+ functionaryIdss
							+ ")) "
							+ " and payroll.status!= ? "
							+ billNumberCond
							+ " and payroll.month = ? and payroll.financialyearid = ? AND payroll.ID_EMP_ASSIGNMENT = ass.id )"
							+ " union"
							+ " (SELECT payroll.id as payid, ded.id_salcode AS salcode, 0 AS earnings,ded.amount as deductions, payroll.MONTH as mth, payroll.FINANCIALYEARID as finyear,payroll.todate as todate,  "
							+ " ass.id_functionary as functionaryId FROM egpay_deductions ded, egpay_emppayroll payroll, eg_emp_assignment ass "
							+ " WHERE ded.id_emppayroll = payroll.ID AND payroll.id_employee in "
							+ "(select id from eg_eis_employeeinfo where functionary_id in("
							+ functionaryIdss
							+ ")) "
							+ " and payroll.status!= ? "
							+ billNumberCond
							+ " and payroll.month = ? and payroll.financialyearid = ? AND payroll.ID_EMP_ASSIGNMENT = ass.id ))");

			earningAndDeductionQuery
					.append(" GROUP BY functionaryId, payid , todate ");
			earningAndDeductionQuery
					.append(" ORDER BY functionaryId,payid,todate ");

			stmt = connection.prepareStatement(earningAndDeductionQuery
					.toString());

			EgwStatus egwStatus = (EgwStatus) PayrollManagersUtill
					.getCommonsService().getStatusByModuleAndCode(
							PayrollConstants.PAYSLIP_MODULE,
							GenericDaoFactory
									.getDAOFactory()
									.getAppConfigValuesDAO()
									.getAppConfigValueByDate("Payslip",
											"PayslipCancelledStatus",
											new Date()).getValue());
			if (billNumberId == 0) {
				stmt.setInt(1, egwStatus.getId());
				stmt.setInt(2, month);
				stmt.setInt(3, year);
				stmt.setInt(4, egwStatus.getId());
				stmt.setInt(5, month);
				stmt.setInt(6, year);
			}
			if (billNumberId != 0) {
				stmt.setInt(1, egwStatus.getId());
				stmt.setInt(2, billNumberId);
				stmt.setInt(3, month);
				stmt.setInt(4, year);
				stmt.setInt(5, egwStatus.getId());
				stmt.setInt(6, billNumberId);
				stmt.setInt(7, month);
				stmt.setInt(8, year);
			}
			rs = stmt.executeQuery();
			int i = 0;
			if (rs != null) {
				ArrayList results = new ArrayList();
				Integer tempFunctionary = 0;
				Long functionaryWiseNetPayTotal = 0L;
				Long grandNetPayTotal = 0L;
				Long functionaryWiseEarTotal = 0L;
				Long grandEarTotal = 0L;
				Long functionaryWiseDedTotal = 0L;
				Long grandDedTotal = 0L;
				HashMap earPayheadMap = new HashMap();
				HashMap dedPayheadMap = new HashMap();
				HashMap grandEarPayheadMap = new HashMap();
				HashMap grandDedPayheadMap = new HashMap();

				while (rs.next()) {
					if (!tempFunctionary.equals(rs.getInt(1))
							&& tempFunctionary != 0) {
						HashMap totalMap = new HashMap();
						totalMap.put("FunctionaryId", " ");
						totalMap.put("PayId", " ");
						totalMap.put("Month & Year", "<b>Total</b>");
						totalMap.put("Employee Code", " ");
						totalMap.put("Employee Name", "<b>Total</b>");
						totalMap.put("NetPay",
								functionaryWiseNetPayTotal.toString());
						totalMap.put("Total Earnings",
								functionaryWiseEarTotal.toString());
						totalMap.put("Total Deductions",
								functionaryWiseDedTotal.toString());
						totalMap.putAll(earPayheadMap);
						totalMap.putAll(dedPayheadMap);
						earPayheadMap = new HashMap();
						dedPayheadMap = new HashMap();
						results.add(i++, totalMap);
						functionaryWiseNetPayTotal = 0L;
						functionaryWiseEarTotal = 0L;
						functionaryWiseDedTotal = 0L;
						// Printing Functionary name on above of the next
						// functionary
						HashMap totalMap1 = new HashMap();
						totalMap1.put("FunctionaryId", " ");
						totalMap1.put("PayId", " ");
						totalMap1.put("Month & Year", " ");
						totalMap1.put("Employee Code", "<b>"
								+ PayrollManagersUtill.getCommonsService()
										.getFunctionaryById(rs.getInt(1))
										.getName() + "<b>");
						totalMap1.put("Employee Name", " ");
						totalMap1.put("NetPay", " ");
						totalMap1.put("Total Earnings", " ");
						totalMap1.put("Total Deductions", " ");
						totalMap1.putAll(earPayheadMap);
						totalMap1.putAll(dedPayheadMap);
						results.add(i++, totalMap1);
					}

					// Putting functionary and dummy value in other place
					if (i == 0) {
						Map rowValueMap1 = new LinkedHashMap();
						int columnCount1 = 4;
						rowValueMap1.put("FunctionaryId", " ");
						rowValueMap1.put("PayId", " ");
						rowValueMap1.put("Month & Year",
								getMonthAndYearStr(rs.getDate(3)));
						rowValueMap1.put("Employee Code", "<b>"
								+ getFunctionaryByPayslipId(rs.getLong(2))
								+ "<b>");
						rowValueMap1.put("Employee Name", " ");

						for (int j = 0; j < earningsPayHeadList.size(); j++) {
							SalaryCodes salaryCodeObj = (SalaryCodes) earningsPayHeadList
									.get(j);
							rowValueMap1.put(salaryCodeObj.getHead() + "", " ");
							columnCount1++;
						}
						rowValueMap1.put("Total Earnings", " ");
						for (int k = 0; k < deductionsPayHeadList.size(); k++) {
							SalaryCodes salaryCodeObj = (SalaryCodes) deductionsPayHeadList
									.get(k);
							rowValueMap1.put(salaryCodeObj.getHead() + "", " ");
							columnCount1++;
						}
						rowValueMap1.put("Total Deductions", " ");
						rowValueMap1.put("NetPay", " ");
						results.add(i++, rowValueMap1);
					}

					Map rowValueMap = new LinkedHashMap();
					int columnCount = 4;
					rowValueMap.put("FunctionaryId", rs.getInt(1));
					rowValueMap.put("PayId", rs.getLong(2));
					rowValueMap.put("Month & Year",
							getMonthAndYearStr(rs.getDate(3)));
					rowValueMap.put("Employee Code",
							getEmpCodeByPayslipId(rs.getLong(2)));
					rowValueMap.put("Employee Name",
							getEmpNameByPayslipId(rs.getLong(2)));

					// Putting earning map for functionary
					for (int j = 0; j < earningsPayHeadList.size(); j++) {
						SalaryCodes salaryCodeObj = (SalaryCodes) earningsPayHeadList
								.get(j);
						rowValueMap.put(salaryCodeObj.getHead() + "",
								rs.getInt(columnCount));
						Integer sumAmnt = (Integer) earPayheadMap
								.get(salaryCodeObj.getHead() + "");
						if (sumAmnt == null) {
							earPayheadMap.put(salaryCodeObj.getHead() + "",
									rs.getInt(columnCount));
						} else {
							sumAmnt += rs.getInt(columnCount);
							earPayheadMap.put(salaryCodeObj.getHead() + "",
									sumAmnt);
						}
						columnCount++;
					}

					// Putting grand total earning map for all functionaries
					int grandColumnCount = 4;
					for (int j = 0; j < earningsPayHeadList.size(); j++) {
						SalaryCodes salaryCodeObj = (SalaryCodes) earningsPayHeadList
								.get(j);
						// rowValueMap.put(salaryCodeObj.getHead() + "",
						// rs.getInt(columnCount));
						Integer sumAmnt = (Integer) grandEarPayheadMap
								.get(salaryCodeObj.getHead() + "");
						if (sumAmnt == null) {
							grandEarPayheadMap.put(
									salaryCodeObj.getHead() + "",
									rs.getInt(grandColumnCount));
						} else {
							sumAmnt += rs.getInt(grandColumnCount);
							grandEarPayheadMap.put(
									salaryCodeObj.getHead() + "", sumAmnt);
						}
						grandColumnCount++;
					}

					functionaryWiseEarTotal = functionaryWiseEarTotal
							+ Long.valueOf(rs.getInt(columnCount));
					grandEarTotal = grandEarTotal
							+ Long.valueOf(rs.getInt(grandColumnCount++));
					rowValueMap.put("Total Earnings", rs.getInt(columnCount++));

					// Putting deduction map for functionary
					for (int k = 0; k < deductionsPayHeadList.size(); k++) {
						SalaryCodes salaryCodeObj = (SalaryCodes) deductionsPayHeadList
								.get(k);
						rowValueMap.put(salaryCodeObj.getHead() + "",
								rs.getInt(columnCount));
						Integer sumAmnt = (Integer) dedPayheadMap
								.get(salaryCodeObj.getHead() + "");
						if (sumAmnt == null) {
							dedPayheadMap.put(salaryCodeObj.getHead() + "",
									rs.getInt(columnCount));
						} else {
							sumAmnt += rs.getInt(columnCount);
							dedPayheadMap.put(salaryCodeObj.getHead() + "",
									sumAmnt);
						}
						columnCount++;
					}

					// Putting grand total deduction map for all functionaries
					for (int k = 0; k < deductionsPayHeadList.size(); k++) {
						SalaryCodes salaryCodeObj = (SalaryCodes) deductionsPayHeadList
								.get(k);
						// rowValueMap.put(salaryCodeObj.getHead() + "",
						// rs.getInt(columnCount));
						Integer sumAmnt = (Integer) grandDedPayheadMap
								.get(salaryCodeObj.getHead() + "");
						if (sumAmnt == null) {
							grandDedPayheadMap.put(
									salaryCodeObj.getHead() + "",
									rs.getInt(grandColumnCount));
						} else {
							sumAmnt += rs.getInt(grandColumnCount);
							grandDedPayheadMap.put(
									salaryCodeObj.getHead() + "", sumAmnt);
						}
						grandColumnCount++;
					}

					functionaryWiseDedTotal = functionaryWiseDedTotal
							+ Long.valueOf(rs.getInt(columnCount));
					grandDedTotal = grandDedTotal
							+ Long.valueOf(rs.getInt(grandColumnCount++));
					rowValueMap.put("Total Deductions",
							rs.getInt(columnCount++));
					rowValueMap.put("NetPay", rs.getInt(columnCount));
					functionaryWiseNetPayTotal = functionaryWiseNetPayTotal
							+ Long.valueOf(rs.getInt(columnCount));
					grandNetPayTotal = grandNetPayTotal
							+ Long.valueOf(rs.getInt(columnCount));

					results.add(i, rowValueMap);

					tempFunctionary = rs.getInt(1);
					i++;
				}
				// Putting the total for each functionary
				HashMap totalMap1 = new HashMap();
				totalMap1.put("FunctionaryId", " ");
				totalMap1.put("PayId", " ");
				totalMap1.put("Month & Year", "<b>Total</b>");
				totalMap1.put("Employee Code", " ");
				totalMap1.put("Employee Name", "<b>Total</b>");
				totalMap1.putAll(earPayheadMap);
				totalMap1.putAll(dedPayheadMap);
				totalMap1.put("NetPay", functionaryWiseNetPayTotal.toString());
				totalMap1.put("Total Deductions",
						functionaryWiseDedTotal.toString());
				totalMap1.put("Total Earnings",
						functionaryWiseEarTotal.toString());
				results.add(i, totalMap1);

				// Putting the grand total for all the functionaries
				HashMap grandtotalMap = new HashMap();
				grandtotalMap.put("FunctionaryId", " ");
				grandtotalMap.put("PayId", " ");
				grandtotalMap.put("Month & Year", " ");
				grandtotalMap.put("Employee Code", " ");
				grandtotalMap.put("Employee Name", "<b>Grand Total</b>");
				grandtotalMap.putAll(grandEarPayheadMap);
				grandtotalMap.putAll(grandDedPayheadMap);
				grandtotalMap.put("NetPay", grandNetPayTotal.toString());
				grandtotalMap.put("Total Deductions", grandDedTotal.toString());
				grandtotalMap.put("Total Earnings", grandEarTotal.toString());
				results.add(++i, grandtotalMap);

				return results;
			}

			return null;
		

	}

	private String getFunctionaryByPayslipId(Long payId) {
		EmpPayroll payslipObj = (EmpPayroll) findById(payId, false);
		return payslipObj.getEmpAssignment().getFunctionary().getName();
	}

	private String getEmpNameByPayslipId(Long payId) {
		EmpPayroll payslipObj = (EmpPayroll) findById(payId, false);
		return payslipObj.getEmployee().getEmployeeName();
	}

	private Integer getInstAmount(Long payId, String payhead) {
		Integer instAmt = new Integer(0);
		EmpPayroll payslipObj = (EmpPayroll) findById(payId, false);
		Set deductionsSet = payslipObj.getDeductionses();
		for (Iterator itr = deductionsSet.iterator(); itr.hasNext();) {
			Deductions ded = (Deductions) itr.next();
			if (ded.getSalaryCodes().getHead().equalsIgnoreCase(payhead)) {
				if (ded.getAmount().floatValue() > 0
						&& ded.getSalAdvances() != null
						&& ded.getSalAdvances().getAdvanceAmt().floatValue() > 0) {
					instAmt = ded.getSalAdvances().getAdvanceAmt().intValue();
				}
			}
		}
		return instAmt;
	}

	private Integer getNumOfInst(Long payId, String payhead) {
		Integer numOfInst = new Integer(0);
		EmpPayroll payslipObj = (EmpPayroll) findById(payId, false);
		Set deductionsSet = payslipObj.getDeductionses();
		for (Iterator itr = deductionsSet.iterator(); itr.hasNext();) {
			Deductions ded = (Deductions) itr.next();
			if (ded.getSalaryCodes().getHead().equalsIgnoreCase(payhead)) {
				if (ded.getAmount().floatValue() > 0
						&& ded.getSalAdvances() != null
						&& ded.getSalAdvances().getNumOfInst().intValue() > 0) {
					numOfInst = ded.getSalAdvances().getNumOfInst().intValue();
				}
			}
		}
		return numOfInst;
	}

	private Integer getInstNo(Long payId, String payhead) {
		Integer instNo = new Integer(0);
		EmpPayroll payslipObj = (EmpPayroll) findById(payId, false);
		Set deductionsSet = payslipObj.getDeductionses();
		for (Iterator itr = deductionsSet.iterator(); itr.hasNext();) {
			Deductions ded = (Deductions) itr.next();
			if (ded.getSalaryCodes().getHead().equalsIgnoreCase(payhead)) {
				if (ded.getAmount().floatValue() > 0
						&& ded.getAdvanceScheduler() != null
						&& ded.getAdvanceScheduler().getInstallmentNo() > 0) {
					instNo = ded.getAdvanceScheduler().getInstallmentNo()
							.intValue();
				}
			}
		}
		return instNo;
	}

	/*
	 * private String getFunctionaryByPayslipId(Integer payId){ EmpPayroll
	 * payslipObj = (EmpPayroll) findById(payId, false); return
	 * payslipObj.getEmpAssignment().getFunctionary().getName(); }
	 */

	/*
	 * private String getEmpNameByPayslipId(Integer payId){ EmpPayroll
	 * payslipObj = (EmpPayroll) findById(payId, false); return
	 * payslipObj.getEmployee().getEmployeeName(); }
	 * 
	 * private Integer getEmpCodeByPayslipId(Integer payId) { EmpPayroll
	 * payslipObj = (EmpPayroll) findById(payId, false); return
	 * payslipObj.getEmployee().getEmployeeCode(); }
	 */

	public List<HashMap> getBankAdviceReportByBillIds(Integer[] billIds)
			throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		final ArrayList results = new ArrayList();
		String allBillids = "";
		int a = 1;
		try {
			for (Integer billId : billIds) {
				logger.info("billid-------------------" + billId);
				if (a == billIds.length) {
					allBillids = allBillids + billId.toString();
				} else {
					allBillids = allBillids + billId.toString() + ",";
				}
				a++;
			}

			final String qry = "select sum(pay.net_pay) as netpay, b.name as bankName from egpay_emppayroll pay,eg_employee emp, "
					+ "egeis_bank_det det, bank b where pay.id_billregister in("
					+ allBillids
					+ ") "
					+ "and pay.id_employee=emp.id and det.id=emp.id "
					+ "and det.bank= b.id group by (b.name)";

			HibernateUtil.getCurrentSession().doWork(new Work() {
				PreparedStatement stmt = null;
				ResultSet rs = null;

				@Override
				public void execute(Connection connection) throws SQLException {
					try {
						stmt = connection.prepareStatement(qry);
						rs = stmt.executeQuery();
						if (rs != null) {

							HashMap hm = new HashMap();
							hm.put("Bank Name", "");
							hm.put("Net Pay", "");
							results.add(hm);
							while (rs.next()) {
								HashMap hm1 = new HashMap();
								hm1.put("Bank Name", rs.getString("bankName"));
								hm1.put("Net Pay", rs.getString("netPay"));
								results.add(hm1);
							}
						}
					} finally {
						HibernateUtil.release(stmt, rs);
					}

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		return results;
	}

}
