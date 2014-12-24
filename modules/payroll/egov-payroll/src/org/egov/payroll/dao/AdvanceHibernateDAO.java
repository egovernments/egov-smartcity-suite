package org.egov.payroll.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.AdvanceSchedule;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the SalaryCodesDAO for the Hibernate specific
 * Implementation
 * 
 * @author Lokesh
 * @version 2.00
 */

public class AdvanceHibernateDAO extends GenericHibernateDAO implements
		AdvanceDAO {

	private static final Logger LOGGER = Logger
			.getLogger(AdvanceHibernateDAO.class);
	public static final String STATUS = "status";

	public AdvanceHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	public List<Advance> getAllSaladvances() {
		List<Advance> salAdvancesList = new ArrayList<Advance>();
		Query qry = getSession().createQuery(
				"from Advance S order by S.employee.employeeCode");
		salAdvancesList = qry.list();
		return salAdvancesList;
	}

	public List<Advance> getAdvancesByEmpId(Integer empId) {
		List<Advance> salAdvancesList = new ArrayList<Advance>();
		Query qry = getSession().createQuery(
				"from Advance S where S.employee.idPersonalInformation =:id ");
		qry.setInteger("id", empId);
		salAdvancesList = qry.list();
		return salAdvancesList;
	}

	public List<Advance> getAllSalaryadvancesbyStatus(EgwStatus status) {
		List<Advance> salaryadvances = new ArrayList<Advance>();
		Query qry = getSession().createQuery(
				"from Advance S where S.status =:status ");
		qry.setEntity(STATUS, status);
		salaryadvances = qry.list();
		return salaryadvances;
	}

	public List<Advance> getAllAdvancesByStatus(EgwStatus status) {
		List<Advance> salAdvancesList = new ArrayList<Advance>();
		Query qry = getSession().createQuery(
				"from Advance S where S.status =:status ");
		qry.setEntity(STATUS, status);
		salAdvancesList = qry.list();
		return salAdvancesList;
	}

	public List<Advance> getAllSalAdvancesByEmpAndStatus(Integer empId,
			EgwStatus status) {
		List<Advance> advances = new ArrayList<Advance>();
		Query qry = getSession().createQuery(
				"from Advance S where S.employee.idPersonalInformation = :empId "
						+ "and S.status = :status");
		qry.setInteger("empId", empId);
		qry.setEntity(STATUS, status);
		advances = qry.list();
		return advances;
	}

	public List<Advance> getAllEligibleAdvancesForEmp(Integer empId) {
		List<Advance> advances = new ArrayList<Advance>();
		PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill
				.getPayrollExterInterface();
		String tmpQry = " from Advance S where S.employee.idPersonalInformation = :empId and S.pendingAmt >= 0 and ";
		String status = GenericDaoFactory
				.getDAOFactory()
				.getAppConfigValuesDAO()
				.getAppConfigValueByDate("Advance", "AdvanceDisbursedStatus",
						new Date()).getValue();

		Query qry1 = getSession()
				.createQuery(
						tmpQry
								+ " S.isLegacyAdvance='N' and S.salaryCodes.categoryMaster.name = :dedBankLoan and S.status = :status");
		qry1.setInteger("empId", empId);
		qry1.setEntity(STATUS, payrollExternalInterface
				.getStatusByModuleAndDescription(
						PayrollConstants.SAL_ADVANCE_MODULE, status));
		qry1.setString("dedBankLoan", PayrollConstants.Deduction_BankLoan);
		advances.addAll(qry1.list());

		Query qry2 = getSession()
				.createQuery(
						tmpQry
								+ " S.salaryCodes.categoryMaster.name = :dedAdv and (S.isLegacyAdvance='N' and S.salaryARF.egAdvanceReqMises.voucherheader.status=0 )");
		qry2.setInteger("empId", empId);
		qry2.setString("dedAdv", PayrollConstants.Deduction_Advance);
		advances.addAll(qry2.list());

		Query qry3 = getSession()
				.createQuery(
						tmpQry
								+ " S.salaryCodes.categoryMaster.name = :dedAdv and S.isLegacyAdvance='Y')");
		qry3.setInteger("empId", empId);
		qry3.setString("dedAdv", PayrollConstants.Deduction_Advance);
		advances.addAll(qry3.list());

		return advances;
	}

	/** Its been called from PFReport **/
	public List<String> getListOfRepaidAdvanceAmount(Integer idEmployee,
			CChartOfAccounts accountCode, Date fromDate, Date toDate) {
		// Deduction-advance which having only glcode. Incase of
		// deduction-bankloan,there will be no voucher created.So, here we are
		// considering only deduction-advance
		List<String> dateAmountList = new ArrayList<String>();
		try {
			Long glcodeId = accountCode.getId();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",
					Locale.getDefault());
			String frmDate = sdf.format(fromDate);
			String toDt = sdf.format(toDate);
			EgwStatus egwStatus = (EgwStatus) CommonsDaoFactory
					.getDAOFactory()
					.getEgwStatusDAO()
					.getStatusByModuleAndCode(
							PayrollConstants.PAYSLIP_MODULE,
							GenericDaoFactory
									.getDAOFactory()
									.getAppConfigValuesDAO()
									.getAppConfigValueByDate("Payslip",
											"PayslipAuditApprovedStatus",
											new Date()).getValue());
			String qry = "select to_char(vou.VOUCHERDATE,'dd-MON-yy') || '->' || sum(d.amount) as code from egpay_deductions d,"
					+ " egpay_emppayroll pay,voucherheader vou ,eg_billregister bill,eg_billregistermis billmis "
					+ "where vou.voucherdate>= to_date('"
					+ frmDate
					+ "','dd-MM-yyyy') and "
					+ "vou.voucherdate <= to_date('"
					+ toDt
					+ "','dd-MM-yyyy') and "
					+ "d.ID_EMPPAYROLL = pay.id and "
					+ "pay.ID_EMPLOYEE= "
					+ idEmployee
					+ " AND d.ID_SAL_ADVANCE in (SELECT adv.id  FROM egpay_saladvances adv  WHERE adv.id_salcode IN    (SELECT salcode.ID    FROM egpay_salarycodes salcode    WHERE salcode.categoryid IN  (SELECT cat.id   FROM egpay_category_master cat  WHERE cat.name='"
					+ PayrollConstants.Deduction_Advance
					+ "'  ) ) ) and "
					+ "pay.status = "
					+ egwStatus.getId()
					+ " and "
					+ "pay.ID_BILLREGISTER = bill.id and bill.id = billmis.billid and billmis.voucherheaderid = vou.id and "
					+ "((d.ID_ACCOUNTCODE in (select id from chartofaccounts where id = "
					+ glcodeId
					+ ") ) or "
					+ "(d.id_salcode in (select sal1.ID  from  egpay_salarycodes sal1 where sal1.GLCODEID = "
					+ glcodeId + "))) " + "group by  vou.VOUCHERDATE";
			LOGGER.info("qry----------" + qry);
			dateAmountList = getSession().createSQLQuery(qry).list();
		}

		catch (HibernateException e) {
			LOGGER.error(e);
		}

		return dateAmountList;

	}

	public List<String> getListOfSanctionedAdvanceAmount(Integer idEmployee,
			CChartOfAccounts accountCode, Date fromDate, Date toDate) {

		// Only for deduction-advance which having only glcode. incase of
		// deduction-bankloan,there will be no voucher created.Here we are
		// considering only deduction-advance
		List<String> dateAmountList = new ArrayList<String>();
		try {

			Long glcodeId = accountCode.getId();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",
					Locale.getDefault());
			String frmDate = sdf.format(fromDate);
			String toDt = sdf.format(toDate);
			EgwStatus arfEgwStatusApproved = ((EgwStatus) PayrollManagersUtill
					.getPayrollExterInterface()
					.getStatusByModuleAndDescription("ARF", "Approved"));

			String qry = "SELECT to_char(vou.VOUCHERDATE,'dd-MON-yy') ||'->'|| sum(adv.advance_amt) as code FROM egpay_saladvances adv, "
					+ "voucherheader vou,egpay_saladvances_arf salarf,eg_advancerequisition advreq,eg_advancerequisitionmis advreqmis "
					+ "WHERE adv.id_employee = "
					+ idEmployee
					+ " AND "
					+ "vou.voucherdate >= to_date('"
					+ frmDate
					+ "','dd-MM-yyyy') and "
					+ "vou.voucherdate <= to_date('"
					+ toDt
					+ "','dd-MM-yyyy') and "
					+ "advreq.statusid = "
					+ arfEgwStatusApproved.getId()
					+ " and "
					+ "advreqmis.voucherheaderid = vou.id and "
					+ "advreqmis.advancerequisitionid = advreq.id and  salarf.id = advreq.id and adv.id = salarf.advance_id and "
					+ "((adv.id_salcode IN(SELECT salcode.ID FROM egpay_salarycodes salcode where salcode.categoryid in (select cat.id from egpay_category_master cat where cat.name='"
					+ PayrollConstants.Deduction_Advance
					+ "' )) )) and  "
					+ "((adv.id_salcode IN(SELECT sal.ID FROM egpay_salarycodes sal WHERE sal.glcodeid = "
					+ glcodeId + "))) " + "group by vou.voucherdate";
			LOGGER.info("qry----------" + qry);
			dateAmountList = HibernateUtil.getCurrentSession()
					.createSQLQuery(qry).list();

		} catch (Exception e) {
			LOGGER.error(e);
		}

		return dateAmountList;
	}

	/**
	 * get advance scheduler by month,financials year for a particular advance
	 * 
	 * @param Advance
	 * @param month
	 * @param finYear
	 * @return AdvanceSchedule
	 */
	public AdvanceSchedule getAdvSchedulerByMonthYear(Advance adv,
			BigDecimal month, CFinancialYear finYear) {
		AdvanceSchedule advScheduler = null;
		Query qry = getSession()
				.createQuery(
						"from AdvanceSchedule advSch where advSch.month = :month and advSch.finYear = :finYear and "
								+ "advSch.advance = :advance ");
		qry.setBigDecimal("month", month);
		qry.setEntity("finYear", finYear);
		qry.setEntity("advance", adv);
		advScheduler = (AdvanceSchedule) qry.uniqueResult();
		return advScheduler;
	}

	/**
	 * Get advance correct schedule which is to be recovered
	 */
	public AdvanceSchedule getRecoverableScheduler(Advance adv) {
		AdvanceSchedule advScheduler = null;
		Query qry = getSession()
				.createQuery(
						"from AdvanceSchedule advSch where advSch.installmentNo =(select min(installmentNo) from "
								+ "AdvanceSchedule where advance = :adv and recover is null) and advSch.advance = :adv");
		qry.setEntity("adv", adv);
		advScheduler = (AdvanceSchedule) qry.uniqueResult();
		return advScheduler;
	}

	/**
	 * Get advance correct schedule which is to be recovered
	 */
	public AdvanceSchedule getAdvSchedulerById(Integer advSchedulerId) {
		AdvanceSchedule advScheduler = null;
		Query qry = getSession()
				.createQuery(
						"from AdvanceSchedule advSch where advSch.id = :advSchedulerId");
		qry.setInteger("advSchedulerId", advSchedulerId);
		advScheduler = (AdvanceSchedule) qry.uniqueResult();
		return advScheduler;
	}

}