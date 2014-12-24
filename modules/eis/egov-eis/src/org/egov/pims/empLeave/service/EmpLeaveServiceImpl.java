/*
 * Created on Oct 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.pims.empLeave.service;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.UtilityMethods;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.dao.EisDAOFactory;
import org.egov.pims.dao.PersonalInformationDAO;
import org.egov.pims.dao.StatusMasterDAO;
import org.egov.pims.empLeave.dao.AttendenceDAO;
import org.egov.pims.empLeave.dao.AttendenceTypeDAO;
import org.egov.pims.empLeave.dao.CalendarYearDao;
import org.egov.pims.empLeave.dao.CompOffDAO;
import org.egov.pims.empLeave.dao.HolidaysUlbDAO;
import org.egov.pims.empLeave.dao.LeaveApplicationDAO;
import org.egov.pims.empLeave.dao.LeaveApprovalDAO;
import org.egov.pims.empLeave.dao.LeaveDAOFactory;
import org.egov.pims.empLeave.dao.LeaveMasterDAO;
import org.egov.pims.empLeave.dao.LeaveOpeningBalanceDAO;
import org.egov.pims.empLeave.dao.TypeOfLeaveMasterDAO;
import org.egov.pims.empLeave.dao.WdaysconstntsDAO;
import org.egov.pims.empLeave.model.Attendence;
import org.egov.pims.empLeave.model.AttendenceType;
import org.egov.pims.empLeave.model.CalendarYear;
import org.egov.pims.empLeave.model.CompOff;
import org.egov.pims.empLeave.model.DatePeriodFY;
import org.egov.pims.empLeave.model.EmpLeaveOpenbalanceDTO;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.empLeave.model.HolidaysUlb;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.empLeave.model.LeaveApproval;
import org.egov.pims.empLeave.model.LeaveCard;
import org.egov.pims.empLeave.model.LeaveMaster;
import org.egov.pims.empLeave.model.LeaveOpeningBalance;
import org.egov.pims.empLeave.model.TypeOfLeaveMaster;
import org.egov.pims.empLeave.model.ViewLeaveTxns;
import org.egov.pims.empLeave.model.Wdaysconstnts;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.AssignmentPrd;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.SearchEmpDTO;
import org.egov.pims.model.StatusMaster;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;

/**
 * @author deepak,DivyaShree
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EmpLeaveServiceImpl implements EmpLeaveService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.infstr.utils.ejb.support.AbstractStatelessSessionBean#ejbCreate()
	 */
	private static final Logger LOGGER = Logger.getLogger(EmpLeaveServiceImpl.class);

	//FIXME: should not be a member variable. Read this in the method isLeaveCalBased
	
	
	
	private static final DateFormat UTILDATEFORMATTER = new SimpleDateFormat(
			"dd/MM/yyyy",Locale.getDefault());



	private Session session;
	private CommonsService commonsService;

	

	List appliedLeaves = new ArrayList();

	List rejectedLeaves = new ArrayList();

	List approvedLeaves = new ArrayList();

	private List listOfLeveTrnx = null;

	private List listOfLeaveCard = null;

	public LeaveOpeningBalance addLeaveOpeningBalance(
			LeaveOpeningBalance leaveOpeningBalance) {
		LeaveOpeningBalance leaveOpening = new LeaveOpeningBalance();
		LeaveOpeningBalanceDAO leaveOpeningBalanceDAO = LeaveDAOFactory
				.getDAOFactory().getLeaveOpeningBalanceDAO();
		if (leaveOpeningBalance != null)
		{
			leaveOpening = (LeaveOpeningBalance) leaveOpeningBalanceDAO
					.create(leaveOpeningBalance);
		}
		return leaveOpening;
	}

	public void updateLeaveMaster(LeaveMaster leaveMaster) {
		try {
			LeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
					.getLeaveMasterDAO();
			if (leaveMaster != null)
			{
				leaveMasterDAO.update(leaveMaster);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public Map getMapOfAttendenceByMonth(Integer month,
			CFinancialYear cFinancialYear) {
		AttendenceDAO adtendence = LeaveDAOFactory.getDAOFactory()
				.getAttendenceDAO();
		return adtendence.getMapOfAttendenceByMonth(month, cFinancialYear);
	}

	public TypeOfLeaveMaster getTypeOfLeaveMasterByName(String name) {
		TypeOfLeaveMaster typeOfLeaveMaster = null;
		TypeOfLeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
				.getTypeOfLeaveMasterDAO();
		if (name != null && !name.equals(""))
		{
			typeOfLeaveMaster = (TypeOfLeaveMaster) leaveMasterDAO
					.getTypeOfLeaveMasterByName(name);
		}
		return typeOfLeaveMaster;

	}

	public void RejectLeaves(String cityURL, String jndi, String hibFactName)
			 {
		try {
			SetDomainJndiHibFactNames.setThreadLocals(cityURL, jndi,
					hibFactName);
			Set emSet = EisManagersUtill.getEmployeeService().getAllPIMap().keySet();
			for (Iterator iter = emSet.iterator(); iter.hasNext();) {
				Integer empId = (Integer) iter.next();
				Set leaveApplicationSet = getLeaveApplicationsForEmpID(empId);
				java.util.Date toDate = new java.util.Date();
				for (Iterator iterleave = leaveApplicationSet.iterator(); iterleave
						.hasNext();) {
					LeaveApplication leaveApplication = (LeaveApplication) iterleave
							.next();
					java.util.Date leaveDate = null;
					if (leaveApplication.getStatusId().getName().equals(
							org.egov.pims.utils.EisConstants.STATUS_APPLIED)) {
						leaveDate = leaveApplication.getToDate();
						if (toDate.after(leaveDate)) {
							setLeaveAsRejected(
									leaveApplication,
									org.egov.pims.utils.EisConstants.STATUS_REJECTED);

						}

					}

				}

			}

		} catch (Exception e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

	}

	public StatusMaster getStatusMasterByName(String name) {
		StatusMasterDAO statusMasterDAO = new StatusMasterDAO();
		return statusMasterDAO.getStatusMaster(name);
	}

	public void populateLeaveBalences(String cityURL, String jndi,
			String hibFactName) {

		try {
			SetDomainJndiHibFactNames.setThreadLocals(cityURL, jndi,
					hibFactName);
			Map<Integer, LeaveOpeningBalance> map = new HashMap<Integer, LeaveOpeningBalance>();
			String finId = EisManagersUtill.getCommonsService()
					.getCurrYearFiscalId();
			CFinancialYear financialYear = EisManagersUtill.getCommonsService()
					.findFinancialYearById(Long.valueOf(finId));
			
			
			java.util.Date stFyDate = financialYear.getStartingDate();
			Calendar calendarFy = Calendar.getInstance();
			calendarFy.setTime(stFyDate);
			Calendar calendartoDate = Calendar.getInstance();
			java.util.Date toDate = new java.util.Date();
			calendartoDate.setTime(toDate);
			if ((calendartoDate.MONTH == calendarFy.MONTH)
					&& (calendarFy.MONTH == 3)) {
				Set emSet = EisManagersUtill.getEmployeeService().getAllPIMap()
						.keySet();
				LeaveOpeningBalance leaveOpeningBalance = null;

				for (Iterator iter = emSet.iterator(); iter.hasNext();) {
					Integer empId = (Integer) iter.next();

					PersonalInformation personalInformation = EisManagersUtill
							.getEmployeeService().getEmloyeeById(empId);
					leaveOpeningBalance = new LeaveOpeningBalance();
					Set leaveApplicationSet = getLeaveApplicationsForEmpID(empId);

					for (Iterator iterleave = leaveApplicationSet.iterator(); iterleave
							.hasNext();) {
						LeaveApplication leaveApplication = (LeaveApplication) iterleave
								.next();
						if (leaveApplication.getTypeOfLeaveMstr()
								.getAccumulate().toString().equals("1")) {
							Float availableBalence = getAvailableLeavs(empId,
									leaveApplication.getTypeOfLeaveMstr()
											.getId(), new java.sql.Date(toDate
											.getTime()));
							leaveOpeningBalance
									.setEmployeeId(personalInformation);
							leaveOpeningBalance.setFinancialId(financialYear);
							leaveOpeningBalance
									.setNoOfLeavesAvai(availableBalence);
							leaveOpeningBalance
									.setTypeOfLeaveMstr(leaveApplication
											.getTypeOfLeaveMstr());
							LeaveOpeningBalance leaveOpening = addLeaveOpeningBalance(leaveOpeningBalance);
							map.put(empId, leaveOpening);
						}

					}
				}

			}

		} catch (Exception e) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

	}

	private void setLeaveAsRejected(LeaveApplication leaveApplication,
			String statusName) {
		try {
			StatusMaster statusMaster = getStatusMasterByName(statusName);
			leaveApplication.setStatusId(statusMaster);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public List getListOfLeaveMastersForDesID(Integer desigId) {
		LeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
				.getLeaveMasterDAO();
		return leaveMasterDAO.getListOfLeaveMastersForDesID(desigId);
	}

	public String getApplicationNumber() {
		String appNo = "";
		try {
			while (true) {
				appNo = UtilityMethods.getRandomString();
				if (checkApplicationNoForLeave(appNo)) {
					continue;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return appNo;
	}

	public void create(LeaveMaster leaveMaster) {
		try {
			LeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
					.getLeaveMasterDAO();
			leaveMasterDAO.create(leaveMaster);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public LeaveMaster getLeaveMasterById(Integer leaveMasterId) {
		LeaveMaster leaveMaster = null;
		try {
			LeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
					.getLeaveMasterDAO();
			if (leaveMasterId != null)
			{
				leaveMaster = (LeaveMaster) leaveMasterDAO.findById(
						leaveMasterId, false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return leaveMaster;
	}

	public Wdaysconstnts getWdaysconstntsByID(Integer id) {
		Wdaysconstnts wdaysconstnts = null;
		try {
			WdaysconstntsDAO wdaysconstntsDAO = LeaveDAOFactory.getDAOFactory()
					.getWdaysconstntsDAO();
			if (id != null)
			{
				wdaysconstnts = (Wdaysconstnts) wdaysconstntsDAO
						.getWdaysconstntsByID(id);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return wdaysconstnts;
	}

	public void updateWdaysconstnts(Wdaysconstnts wdaysconstnts) {
		try {
			WdaysconstntsDAO wdaysconstntsDAO = LeaveDAOFactory.getDAOFactory()
					.getWdaysconstntsDAO();
			wdaysconstntsDAO.update(wdaysconstnts);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public AttendenceType getAttendenceTypeId(Integer id) {
		AttendenceTypeDAO attendenceTypeDAO = LeaveDAOFactory.getDAOFactory()
				.getAttendenceTypeDAO();
		return attendenceTypeDAO.getAttendenceTypeByID(id);
	}

	public AttendenceType getAttendenceTypeByName(String typeName) {
		AttendenceTypeDAO attendenceTypeDAO = LeaveDAOFactory.getDAOFactory()
				.getAttendenceTypeDAO();
		return attendenceTypeDAO.getAttendenceTypeByName(typeName);
	}

	public HolidaysUlb getHolidayId(Integer holId) {
		HolidaysUlb holidaysUlb = null;
		try {
			HolidaysUlbDAO holidaysUlbHibernateDAO = LeaveDAOFactory
					.getDAOFactory().getHolidaysUlbDAO();
			if (holId != null)
				holidaysUlb = (HolidaysUlb) holidaysUlbHibernateDAO.findById(
						holId, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return holidaysUlb;

	}

	public List getHolidayListByFinalsialYearId(CFinancialYear cFinancialYear) {
		HolidaysUlbDAO holidaysUlbHibernateDAO = LeaveDAOFactory
				.getDAOFactory().getHolidaysUlbDAO();
		return holidaysUlbHibernateDAO
				.getHolidayListByFinalsialYearId(cFinancialYear);
	}

	public List getAllWDconstant() {
		WdaysconstntsDAO wdaysconstntsDAO = LeaveDAOFactory.getDAOFactory()
				.getWdaysconstntsDAO();
		return wdaysconstntsDAO.findAll();
	}

	public List getHolidaysUlbsFotFinalsialYearId(CFinancialYear cFinancialYear) {
		HolidaysUlbDAO holidaysUlbHibernateDAO = LeaveDAOFactory
				.getDAOFactory().getHolidaysUlbDAO();
		return holidaysUlbHibernateDAO
				.getHolidaysUlbsFotFinalsialYearId(cFinancialYear);
	}

	public void updateHolidaysUlb(HolidaysUlb holidaysUlb) {
		try {
			HolidaysUlbDAO holidaysUlbHibernateDAO = LeaveDAOFactory
					.getDAOFactory().getHolidaysUlbDAO();
			holidaysUlbHibernateDAO.update(holidaysUlb);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public void createHolidaysUlb(HolidaysUlb holidaysUlb) {
		try {
			HolidaysUlbDAO holidaysUlbHibernateDAO = LeaveDAOFactory
					.getDAOFactory().getHolidaysUlbDAO();
			holidaysUlbHibernateDAO.create(holidaysUlb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public Attendence createAttendence(Attendence att) {
		AttendenceDAO adtendence = LeaveDAOFactory.getDAOFactory()
				.getAttendenceDAO();
		return (Attendence) adtendence.create(att);
	}

	public void addCompOff(CompOff compOff) {
		try {
			CompOffDAO compOffDao = LeaveDAOFactory.getDAOFactory()
					.getCompOffDAO();
			compOffDao.create(compOff);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

	}

	public CompOff getCompOffById(Integer compOffId) {
		CompOffDAO compOffDao = LeaveDAOFactory.getDAOFactory().getCompOffDAO();
		return compOffDao.getCompOffByID(compOffId);

	}

	public void updateCompOff(CompOff compOff) {
		try {
			CompOffDAO compOffDao = LeaveDAOFactory.getDAOFactory()
					.getCompOffDAO();
			compOffDao.update(compOff);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

	}

	public void updateAttendence(Attendence att) {
		try {
			AttendenceDAO adtendence = LeaveDAOFactory.getDAOFactory()
					.getAttendenceDAO();
			adtendence.update(att);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public Attendence getAttendenceById(Integer id) {
		AttendenceDAO adtendence = LeaveDAOFactory.getDAOFactory()
				.getAttendenceDAO();
		return adtendence.getAttendenceByID(id);
	}

	public void deleteAttendence(Attendence att) {
		try {
			AttendenceDAO adtendence = LeaveDAOFactory.getDAOFactory()
					.getAttendenceDAO();
			adtendence.delete(att);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public TypeOfLeaveMaster getTypeOfLeaveMasterById(
			Integer typeOfLeaveMasterId) {
		TypeOfLeaveMaster typeOfLeaveMaster = null;
		TypeOfLeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
				.getTypeOfLeaveMasterDAO();
		if (typeOfLeaveMasterId != null)
		{
			typeOfLeaveMaster = (TypeOfLeaveMaster) leaveMasterDAO.findById(typeOfLeaveMasterId, false);
		}
		return typeOfLeaveMaster;
	}

	public void updateLeaveApproval(LeaveApproval leaveApproval) {
		try {
			LeaveApprovalDAO leaveApprovalDAO = LeaveDAOFactory.getDAOFactory()
					.getLeaveApprovalDAO();
			if (leaveApproval != null)
			{
				leaveApprovalDAO.update(leaveApproval);
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public void createLeaveApplication(Integer workingDays,
			PersonalInformation employee, String leaveType, String reason) {
		try {
			LeaveApplication leaveApplication = validate(workingDays, employee,
					leaveType, reason);
			LeaveApplicationDAO leaveApplicationDAO = LeaveDAOFactory
					.getDAOFactory().getLeaveApplicationDAO();
			 leaveApplicationDAO.create(leaveApplication);
			addLeaveApplication(employee, leaveApplication);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

	}

	/*
	 * Api for leave open balance
	 * 
	 */
	/*
	 * public void createOpenLeaveBalance(LeaveOpeningBalance leaveOpenBalance) {
	 * try { LeaveOpeningBalanceDAO leaveOpeningBalance =
	 * LeaveDAOFactory.getDAOFactory().getLeaveOpeningBalanceDAO();
	 * LeaveBalanceForm
	 * leaveBalance=(LeaveBalanceForm)leaveOpeningBalance.create(leaveOpeningBalance);
	 *  } catch (RuntimeException e) { // TODO Auto-generated catch block
	 *  //HibernateUtil.rollbackTransaction(); throw new
	 * EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e); } }
	 */

	private LeaveApplication validate(Integer workingDays,
			PersonalInformation employee, String leaveType, String reason) {
		try {
			LeaveApplication leaveApplication = new LeaveApplication();
			if (leaveApplication.getFromDate() != null)
				leaveApplication.setFromDate(null);
			if (leaveApplication.getToDate() != null)
				leaveApplication.setToDate(null);
			leaveApplication.setWorkingDays(workingDays);
			leaveApplication.setReason(reason);
			leaveApplication.setEmployeeId(employee);
			TypeOfLeaveMaster typeOfLeaveMaster = getTypeOfLeaveMasterByName(leaveType);
			leaveApplication.setTypeOfLeaveMstr(typeOfLeaveMaster);
			StatusMaster statusMaster = getStatusMasterByName(EisConstants.STATUS_APPROVED);
			leaveApplication.setStatusId(statusMaster);
			leaveApplication.setApplicationNumber(getApplicationNumber());
			String finId = EisManagersUtill.getCommonsService()
					.getCurrYearFiscalId();
			CFinancialYear financialYear = EisManagersUtill.getCommonsService()
					.findFinancialYearById(Long.valueOf(finId));
			leaveApplication.setFinancialY(financialYear);
			return leaveApplication;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

	}

	public void addLeaveApproval(LeaveApproval leaveApproval)
			throws SQLException {
		try {
			LeaveApprovalDAO leaveApprovalDAO = LeaveDAOFactory.getDAOFactory()
					.getLeaveApprovalDAO();
			if (leaveApproval != null)
				leaveApprovalDAO.create(leaveApproval);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public LeaveApproval getLeaveApprovalById(Integer leaveApprovalId) {
		LeaveApproval leaveApproval = null;
		try {
			LeaveApprovalDAO leaveApprovalDAO = LeaveDAOFactory.getDAOFactory()
					.getLeaveApprovalDAO();
			if (leaveApprovalId != null)
				leaveApproval = (LeaveApproval) leaveApprovalDAO.findById(
						leaveApprovalId, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return leaveApproval;
	}

	public void updateLeaveApplication(LeaveApplication leaveApplication) {
		try {
			LeaveApplicationDAO leaveApplicationDAO = LeaveDAOFactory
					.getDAOFactory().getLeaveApplicationDAO();
			if (leaveApplication != null)
				leaveApplicationDAO.update(leaveApplication);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public void addLeaveApplication(PersonalInformation employee,
			LeaveApplication leaveApplication) {
		try {
			employee.addLeaveApplication(leaveApplication);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public void createLeaveApplication(LeaveApplication leaveApplication) {
		try {
			LeaveApplicationDAO leaveApplicationDAO = LeaveDAOFactory
					.getDAOFactory().getLeaveApplicationDAO();
			leaveApplicationDAO.create(leaveApplication);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public LeaveApplication getLeaveApplicationById(Integer leaveApplicationId) {
		LeaveApplication leaveApplication = null;
		try {
			LeaveApplicationDAO leaveApplicationDAO = LeaveDAOFactory
					.getDAOFactory().getLeaveApplicationDAO();
			if (leaveApplicationId != null)
				leaveApplication = (LeaveApplication) leaveApplicationDAO
						.findById(Long.valueOf(leaveApplicationId.intValue()), false);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return leaveApplication;
	}

	
	public List getLeaveMastersForDesID(Integer desigId) {
		LeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
				.getLeaveMasterDAO();
		return leaveMasterDAO.getListOfLeaveMastersForDesID(desigId);
	}

	public List getLeaveApplicationsRejectedEmpID(Integer empId) {
		setApprovedLeavesForEmpID(empId);
		return rejectedLeaves;
	}

	public List getLeaveApplicationsApprovedEmpID(Integer empId) {
		setApprovedLeavesForEmpID(empId);
		return approvedLeaves;
	}

	public List getLeaveApplicationsAppliedEmpID(Integer empId) {
		setApprovedLeavesForEmpID(empId);
		return appliedLeaves;
	}

	private void setApprovedLeavesForEmpID(Integer empId) {

		try {
			appliedLeaves = new ArrayList();
			rejectedLeaves = new ArrayList();
			approvedLeaves = new ArrayList();

			Set leaveApplicationSet = getLeaveApplicationsForEmpID(empId);
			for (Iterator iter = leaveApplicationSet.iterator(); iter.hasNext();) {
				LeaveApplication leaveApplication = (LeaveApplication) iter
						.next();
				// Accept leave application for previous dates also
				// setStatusAsRejected(leaveApplication);
				if (leaveApplication.getStatusId().getName().equals(
						org.egov.pims.utils.EisConstants.STATUS_APPLIED))
					appliedLeaves.add(leaveApplication);
				else if (leaveApplication.getStatusId().getName().equals(
						org.egov.pims.utils.EisConstants.STATUS_REJECTED))
					rejectedLeaves.add(leaveApplication);
				else if (leaveApplication.getStatusId().getName().equals(
						org.egov.pims.utils.EisConstants.STATUS_APPROVED))
					approvedLeaves.add(leaveApplication);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

	}

	public boolean checkApplicationNoForLeave(String appNo) {

		session = HibernateUtil.getCurrentSession();
		boolean b = false;
		try {
			Query qry = session
					.createQuery("select dp.applicationNumber from  LeaveApplication dp where upper(dp.applicationNumber) = :appNo ");

			if (appNo != null) {
				qry.setString("appNo", appNo);

			}
			if (qry.list() != null && !qry.list().isEmpty()) {
				Object obj = null;
				for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
					obj = (Object) iter.next();
					b = true;
				}

			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return b;
	}

	public boolean checkSanctionNoForLeave(String sanctionNo) {
		session = HibernateUtil.getCurrentSession();
		boolean b = false;
		try {
			Query qry = session
					.createQuery("select dp.sanctionNo from  LeaveApproval dp where upper(dp.sanctionNo) = :sanctionNo ");
			if (sanctionNo != null) {
				qry.setString("sanctionNo", sanctionNo);

			}
			if (qry.list() != null && !qry.list().isEmpty()) {
				Object obj = null;
				for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
					obj = (Object) iter.next();
					b = true;
				}

			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return b;
	}

	public Set getLeaveApplicationsForEmpID(Integer empId) {

		PersonalInformationDAO personalInformationDAO = EisDAOFactory
				.getDAOFactory().getPersonalInformationDAO();
		PersonalInformation personalInformation = (PersonalInformation) personalInformationDAO
				.findById(empId, false);
		return personalInformation.getLeaveApplicationSet();

	}

	public Map getleaveTypesForDesignation(Integer desigId) {
		LeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
				.getLeaveMasterDAO();
		HashMap leaveTypeMap = new HashMap();
		List applist = leaveMasterDAO.getListOfLeaveMastersForDesID(desigId);
		for (Iterator iter = applist.iterator(); iter.hasNext();) {
			LeaveMaster leaveMaster = (LeaveMaster) iter.next();
			TypeOfLeaveMaster typeOfLeaveMaster = leaveMaster
					.getTypeOfLeaveMstr();
			leaveTypeMap.put(typeOfLeaveMaster.getId(), typeOfLeaveMaster
					.getName());
		}
		return leaveTypeMap;
	}

	public boolean checkPayEligible(Integer applicationId) {
		boolean b = false;

		try {
			LeaveApprovalDAO leaveApprovalDAO = LeaveDAOFactory.getDAOFactory()
					.getLeaveApprovalDAO();
			LeaveApproval leaveApproval = leaveApprovalDAO
					.getLeaveApprovalByApplicationID(applicationId);
			if (leaveApproval != null && leaveApproval.getPayElegible().toString().equals("1")) 
			{
				
					b = true;
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return b;

	}

	public LeaveApproval getLeaveApprovalByApplicationID(Integer applicationId) {
		LeaveApprovalDAO leaveApprovalDAO = LeaveDAOFactory.getDAOFactory()
				.getLeaveApprovalDAO();
		LeaveApproval leaveApproval = leaveApprovalDAO
				.getLeaveApprovalByApplicationID(applicationId);
		return leaveApproval;

	}

	private Map getMapOfLeaveDayAndPayEligible(
			LeaveApplication leaveApplication, CFinancialYear cfinancial) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
			CFinancialYear financial = cfinancial;
			Set holidaySet = getHolidaySet(financial);
			if (checkPayEligible(Integer.valueOf((int)leaveApplication.getId().longValue())) && leaveApplication.getNoOfLeavesAvai() < leaveApplication.getWorkingDays()) {
				if (sdf.format(leaveApplication.getFromDate()).equals(
						sdf.format(leaveApplication.getToDate()))) {
					if (!holidaySet.contains(sdf.format(leaveApplication
							.getFromDate()))) {
						map.put(sdf.format(leaveApplication.getFromDate()),Integer.valueOf(1));
					}
				} else {
					Calendar fromCalendar = Calendar.getInstance();
					fromCalendar.setTime(leaveApplication.getFromDate());
					Calendar toCalendar = Calendar.getInstance();
					toCalendar.setTime(leaveApplication.getToDate());

					float count = leaveApplication.getNoOfLeavesAvai();

					if (count >= 1.0f) {
						do {

							if (!holidaySet.contains(new java.sql.Date(
									fromCalendar.getTime().getTime())
									.toString())) {

								if (count <= 0) {
									map.put(sdf.format(fromCalendar.getTime()),
											Integer.valueOf(0));

								} else {
									map.put(sdf.format(fromCalendar.getTime()),
											Integer.valueOf(1));

								}

								count--;

							}

							fromCalendar.add(Calendar.DATE, 1);

						} while (fromCalendar.getTime().getTime() < toCalendar
								.getTime().getTime()
								|| fromCalendar.getTime().getTime() == toCalendar
										.getTime().getTime());
					}

				}
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return map;
	}
	
	public Boolean isLeaveCalendarBased() {
		// FIXME: code to read appconfig value and return 
		String calOrfinBased=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate(EisConstants.MODULE_LEAVEAPP,EisConstants.MODULE_KEY_ISCALENDERBASED,new Date()).getValue();
		return "calendarBased".equals(calOrfinBased);
	}

	public int calculateNoOfWorkingDaysBweenTwoDates(java.util.Date fromDate,
			java.util.Date toDate) {
		
		
		try {
			int wDays = 0;
			if(fromDate!=null && toDate!=null )
			{
			List<DatePeriodFY> objDateList = EisManagersUtill
					.getListOfFinYrandDate(fromDate, toDate);
			DatePeriodFY datePeriodFirst = null;
			DatePeriodFY datePeriodSecond = null;
			// FIXME handle null check
			
				if (objDateList.size() == 1) {
					datePeriodFirst = (DatePeriodFY) objDateList.get(0);
				
					wDays = calculateNoOfWorkingDaysBweenTwoDatesByFinYear(
							fromDate, toDate, datePeriodFirst.getFinancial());
	
				} else if (objDateList.size() == 2) {
					datePeriodFirst = (DatePeriodFY) objDateList.get(0);
	
					datePeriodSecond = (DatePeriodFY) objDateList.get(1);
					
				wDays = wDays
								+ calculateNoOfWorkingDaysBweenTwoDatesByFinYear(
										datePeriodFirst.getFromDate(), datePeriodFirst
												.getToDate(), datePeriodFirst
												.getFinancial());
						wDays = wDays
								+ calculateNoOfWorkingDaysBweenTwoDatesByFinYear(
										datePeriodSecond.getFromDate(),
										datePeriodSecond.getToDate(), datePeriodSecond
												.getFinancial());
					
				}
			}
			
			return wDays;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	private int calculateNoOfWorkingDaysBweenTwoDatesByFinYear(
			java.util.Date fromDate, java.util.Date toDate,
			CFinancialYear cfinancial) {
		try {
			int wDays = 0;
			Set holidaySet=null;
			CFinancialYear financial = cfinancial;
			holidaySet = getHolidaySet(financial);
			if(isHolidayEnclosed())
			{
				wDays=calculateNoOfWorkingDaysForHolidayEnclosed(fromDate, toDate,cfinancial);
			}
			else
			{
				SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
				if (sdf.format(fromDate).equals(sdf.format(toDate))) {
					if (!holidaySet.contains(sdf.format(fromDate))) {
						wDays = 1;
					}
	
				} else {
					Calendar fromCalendar = Calendar.getInstance();
					fromCalendar.setTime(fromDate);
					Calendar toCalendar = Calendar.getInstance();
					toCalendar.setTime(toDate);
					int count = 0;
					do {
						if (!holidaySet.contains(new java.sql.Date(fromCalendar
								.getTime().getTime()).toString())) {
							count++;
	
						}
						fromCalendar.add(Calendar.DATE, 1);
	
					} while (fromCalendar.getTime().getTime() < toCalendar
							.getTime().getTime()
							|| fromCalendar.getTime().getTime() == toCalendar
									.getTime().getTime());
	
					wDays = count;
				}
			}
			return wDays;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}
	
	/*
	 * Added new Api for including holidays if isHolidayEnclosed() is Y
	 * 
	 */
	
	public int calculateNoOfWorkingDaysForHolidayEnclosed(java.util.Date fromDate, java.util.Date toDate,CFinancialYear cfinancial)
	{
		int wDays = 0;
		Set holidaySet=null;
		CFinancialYear financial = cfinancial;
		holidaySet = getHolidaySet(financial);
		SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
		if (sdf.format(fromDate).equals(sdf.format(toDate)))
		{
			//FIXME - Check for nullpointer here
			if (!holidaySet.contains(sdf.format(fromDate))) 
			{
				wDays = 1;
			}

		} 
		else 
		{
			Calendar fromCalendar = Calendar.getInstance();
			fromCalendar.setTime(fromDate);
			Calendar toCalendar = Calendar.getInstance();
			toCalendar.setTime(toDate);
			
			//FIXME - Can use wdasys directly
			int count = 0;
			do {
				
					count++;
					fromCalendar.add(Calendar.DATE, 1);

			} while (fromCalendar.getTime().getTime() < toCalendar
					.getTime().getTime()
					|| fromCalendar.getTime().getTime() == toCalendar
							.getTime().getTime());

			wDays = count;
		}
		return wDays;
	}

	public List getNoOfWorkingDaysBweenTwoDates(java.util.Date fromDate,
			java.util.Date toDate) {
		try {
			List<String> wdList = new ArrayList<String>();
			List objDateList = EisManagersUtill.getListOfFinYrandDate(fromDate,toDate);
			if (objDateList.size() == 1) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList.get(0);				
				wdList = getNoOfWorkingDaysBweenTwoDatesByFinYear(fromDate,toDate, finYrandDateFirst.getFinancial());
			} 
			else if (objDateList.size() == 2) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList.get(0);
				DatePeriodFY finYrandDateSecond = (DatePeriodFY) objDateList.get(1);				
				wdList.addAll((Collection) getNoOfWorkingDaysBweenTwoDatesByFinYear(
									finYrandDateFirst.getFromDate(),
									finYrandDateFirst.getToDate(),
									finYrandDateFirst.getFinancial()));
	
				wdList.addAll((Collection) getNoOfWorkingDaysBweenTwoDatesByFinYear(
									finYrandDateSecond.getFromDate(),
									finYrandDateSecond.getToDate(),
									finYrandDateSecond.getFinancial()));
			}
			return wdList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	//Api Changing for Holiday Enlosed
	public List getNoOfWorkingDaysBweenTwoDatesByFinYear(java.util.Date fromDate, java.util.Date toDate,CFinancialYear cfinancial) {
		try {
			List<String> wdList = new ArrayList<String>();
			Set holidaySet =null;
			SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
			CFinancialYear financial = cfinancial;
			holidaySet = getHolidaySet(financial);
			if(isHolidayEnclosed())
			{
				if (sdf.format(fromDate).equals(sdf.format(toDate))) 
				{
					if (!holidaySet.contains(sdf.format(fromDate))) 
					{
						wdList.add(sdf.format(fromDate.getTime()));
					}
	
				} 
				else 
				{
					Calendar fromCalendar = Calendar.getInstance();
					fromCalendar.setTime(fromDate);
					Calendar toCalendar = Calendar.getInstance();
					toCalendar.setTime(toDate);
	
					do 
					{
						
						wdList.add(new java.sql.Date(fromCalendar.getTime()
									.getTime()).toString());
						fromCalendar.add(Calendar.DATE, 1);
	
					} while (fromCalendar.getTime().getTime() < toCalendar
							.getTime().getTime()
							|| fromCalendar.getTime().getTime() == toCalendar
									.getTime().getTime());
	
				}
			}
			else
			{
				if (sdf.format(fromDate).equals(sdf.format(toDate))) 
				{
					if (!holidaySet.contains(sdf.format(fromDate))) 
					{
						wdList.add(sdf.format(fromDate.getTime()));
					}
	
				} 
				else 
				{
					Calendar fromCalendar = Calendar.getInstance();
					fromCalendar.setTime(fromDate);
					Calendar toCalendar = Calendar.getInstance();
					toCalendar.setTime(toDate);
	
					do 
					{
						if (!holidaySet.contains(new java.sql.Date(fromCalendar
								.getTime().getTime()).toString())) 
						{
							wdList.add(new java.sql.Date(fromCalendar.getTime()
									.getTime()).toString());
						}
						fromCalendar.add(Calendar.DATE, 1);
	
					} while (fromCalendar.getTime().getTime() < toCalendar
							.getTime().getTime()
							|| fromCalendar.getTime().getTime() == toCalendar
									.getTime().getTime());
	
				}
			}
			return wdList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

	}
	
	
	public Set listOfDaysWorkedForAnEmployeeForGivenMonth(int month,
			PersonalInformation personalInformation,
			CFinancialYear cFinancialYear, Boolean b, int CurYear) {
		Set<String> pdList = null;
		try {
			Map mp = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(
					month, cFinancialYear);
			if (b.equals(Boolean.TRUE)) {
				int dy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				Calendar cal = Calendar.getInstance();
				cal.set(CurYear, month - 1, dy);

				pdList = listOfDaysWorkedForAnEmployeebetweenTwoDates(
						(java.sql.Date) mp.get(STR_START_DATE), new java.sql.Date(
								cal.getTime().getTime()), personalInformation);
			} else {
				pdList = listOfDaysWorkedForAnEmployeebetweenTwoDates(
						(java.sql.Date) mp.get(STR_START_DATE), (java.sql.Date) mp
								.get("endDate"), personalInformation);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

		return pdList;
	}

	public Set listOfDaysWorkedForAnEmployeebetweenTwoDates(Date fromDate,
			Date toDate, PersonalInformation personalInformation) throws Exception {

		Set<String> wdList = new HashSet<String>();
		try {
			List objDateList = EisManagersUtill.getListOfFinYrandDate(fromDate,
					toDate);
			if (objDateList.size() == 1) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList
						.get(0);
				wdList = getPresentAttAcrossFinYear(fromDate, toDate,
						personalInformation, finYrandDateFirst.getFinancial());

			} else if (objDateList.size() == 2) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList
						.get(0);
				DatePeriodFY finYrandDateSecond = (DatePeriodFY) objDateList
						.get(1);
				wdList.addAll((Collection) getPresentAttAcrossFinYear(
						new java.sql.Date(finYrandDateFirst.getFromDate()
								.getTime()), new java.sql.Date(
								finYrandDateFirst.getToDate().getTime()),
						personalInformation, finYrandDateFirst.getFinancial()));
				wdList
						.addAll((Collection) getPresentAttAcrossFinYear(
								new java.sql.Date(finYrandDateSecond
										.getFromDate().getTime()),
								new java.sql.Date(finYrandDateSecond
										.getToDate().getTime()),
								personalInformation, finYrandDateSecond
										.getFinancial()));
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return wdList;
	}

	private Set getPresentAttAcrossFinYear(Date fromDate, Date toDate,
			PersonalInformation personalInformation,
			CFinancialYear cFinancialYear) {

		Set<String> pdList = new HashSet<String>();
		try {
			AttendenceDAO attendenceDAO = LeaveDAOFactory.getDAOFactory()
					.getAttendenceDAO();
			SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
			Set holSet = getHolidaySet(cFinancialYear);
			for (Iterator iter = attendenceDAO
					.getListOfPresentDaysForEmployeebetweenDates(fromDate,
							toDate, personalInformation).iterator(); iter
					.hasNext();) {
				Attendence attendence = (Attendence) iter.next();
				if (!holSet.contains(sdf.format(attendence.getAttDate())))
					pdList.add(sdf.format(attendence.getAttDate()));

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return pdList;
	}

	public Set listHalfPresentForAnEmployeeForGivenMonth(int month,
			PersonalInformation personalInformation,
			CFinancialYear cFinancialYear, Boolean b, int CurYear) {
		Set<String> pdList = null;
		try {
			Map mp = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(
					month, cFinancialYear);
			if (b.equals(Boolean.TRUE)) {
				int dy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				Calendar cal = Calendar.getInstance();
				cal.set(CurYear, month - 1, dy);
				pdList = listHalfPresentForAnEmployeebetweenTwoDates(
						(java.sql.Date) mp.get(STR_START_DATE), new java.sql.Date(
								cal.getTime().getTime()), personalInformation);
			} else {
				pdList = listHalfPresentForAnEmployeebetweenTwoDates(
						(java.sql.Date) mp.get(STR_START_DATE), (java.sql.Date) mp
								.get("endDate"), personalInformation);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

		return pdList;
	}

	public Set listHalfPresentForAnEmployeebetweenTwoDates(Date fromDate,
			Date toDate, PersonalInformation personalInformation) {
		Set<String> wdList = new HashSet<String>();
		try {
			List objDateList = EisManagersUtill.getListOfFinYrandDate(fromDate,
					toDate);
			if (objDateList.size() == 1) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList
						.get(0);
				wdList = getHalfPresentAttAcrossFinYear(fromDate, toDate,
						personalInformation, finYrandDateFirst.getFinancial());
			} else if (objDateList.size() == 2) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList
						.get(0);
				DatePeriodFY finYrandDateSecond = (DatePeriodFY) objDateList
						.get(1);
				wdList.addAll((Collection) getHalfPresentAttAcrossFinYear(
						new java.sql.Date(finYrandDateFirst.getFromDate()
								.getTime()), new java.sql.Date(
								finYrandDateFirst.getToDate().getTime()),
						personalInformation, finYrandDateFirst.getFinancial()));
				wdList
						.addAll((Collection) getHalfPresentAttAcrossFinYear(
								new java.sql.Date(finYrandDateSecond
										.getFromDate().getTime()),
								new java.sql.Date(finYrandDateSecond
										.getToDate().getTime()),
								personalInformation, finYrandDateSecond
										.getFinancial()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return wdList;
	}

	public Map<String, List> leavesMapForHalf = new HashMap<String, List>();

	private Set getHalfPresentAttAcrossFinYear(Date fromDate, Date toDate,
			PersonalInformation personalInformation,
			CFinancialYear cFinancialYear) {

		AttendenceDAO attendenceDAO = LeaveDAOFactory.getDAOFactory()
				.getAttendenceDAO();
		Set<String> pdList = new HashSet<String>();
		SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
		
		Map<String, List> leavesMap = new HashMap<String, List>();
		Map<java.util.Date, String> pWMap = new HashMap<java.util.Date, String>();
		List paidList = null;
		List unPaidList = null;
		try {

			for (Iterator iter = attendenceDAO
					.getListOfHalfPresentDaysForEmployeebetweenDates(fromDate,
							toDate, personalInformation).iterator(); iter
					.hasNext();) {
				Attendence attendence = (Attendence) iter.next();
				LeaveApplicationDAO leaveApplicationDAO = LeaveDAOFactory
						.getDAOFactory().getLeaveApplicationDAO();
				LeaveApplication leaveApplication = null;
				pdList.add(sdf.format(attendence.getAttDate()));
				if (leaveApplicationDAO.getLeaveApplicationByEmpAndDate(
						new java.sql.Date(attendence.getAttDate().getTime()),
						personalInformation) != null) {
					leaveApplication = leaveApplicationDAO
							.getLeaveApplicationByEmpAndDate(new java.sql.Date(
									attendence.getAttDate().getTime()),
									personalInformation);
					if (checkPayEligible(Integer.valueOf((int)leaveApplication.getId().longValue()))) {

						
						


						if (checkPayEligible(Integer.valueOf((int)leaveApplication.getId().longValue())) == true
								&& leaveApplication.getNoOfLeavesAvai() >= leaveApplication
										.getWorkingDays()) {
							pWMap = new HashMap<java.util.Date, String>();
							pWMap.put(sdf.parse(sdf.format(attendence
									.getAttDate())), "Half");
							if (leavesMap.containsKey("paidLeaves")) {
								((List) leavesMap.get("paidLeaves")).add(pWMap);
							} else {
								paidList = new ArrayList();
								paidList.add(pWMap);
								leavesMap.put("paidLeaves", paidList);
							}

						} else if (checkPayEligible(Integer.valueOf((int)leaveApplication.getId().longValue())) == true
								&& leaveApplication.getNoOfLeavesAvai() < leaveApplication
										.getWorkingDays()) {
							pWMap = new HashMap<java.util.Date, String>();

							Map mp = getMapOfLeaveDayAndPayEligible(
									leaveApplication, getFinantialObJ(sdf
											.format(attendence.getAttDate())));

							if (leaveApplication.getNoOfLeavesAvai() >= 1.0f) {
								if (((Integer) mp.get(sdf.format(attendence
										.getAttDate()))).equals(Integer.valueOf(1))) {

									pWMap.put(sdf.parse(sdf.format(attendence
											.getAttDate())), "Half");
									if (leavesMap.containsKey("paidLeaves")) {
										((List) leavesMap.get("paidLeaves"))
												.add(pWMap);
									} else {
										paidList = new ArrayList();
										paidList.add(pWMap);
										leavesMap.put("paidLeaves", paidList);
									}

								} else {

									pWMap.put(sdf.parse(sdf.format(attendence
											.getAttDate())), "Half");
									if (leavesMap.containsKey("unpaidLeaves")) {
										((List) leavesMap.get("unpaidLeaves"))
												.add(pWMap);
									} else {
										unPaidList = new ArrayList();
										unPaidList.add(pWMap);
										leavesMap.put("unpaidLeaves",
												unPaidList);

									}
								}
							} else {
								pWMap = new HashMap<java.util.Date, String>();
								pWMap.put(sdf.parse(sdf.format(attendence
										.getAttDate())), "Half");
								if (leavesMap.containsKey("unpaidLeaves")) {
									((List) leavesMap.get("unpaidLeaves"))
											.add(pWMap);
								} else {
									unPaidList = new ArrayList();
									unPaidList.add(pWMap);
									leavesMap.put("unpaidLeaves", unPaidList);
								}
							}

						}
					
					} else {
						pWMap = new HashMap<java.util.Date, String>();
						pWMap.put(sdf
						.parse(sdf.format(attendence.getAttDate())),
						"Half");
				if (leavesMap.containsKey("unpaidLeaves")) {
					((List) leavesMap.get("unpaidLeaves")).add(pWMap);
				} else {
					unPaidList = new ArrayList();
					unPaidList.add(pWMap);
					leavesMap.put("unpaidLeaves", unPaidList);
				}
				}

				}

			}
		} catch (Exception e) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		leavesMapForHalf = leavesMap;
		return pdList;
	}

	public Set listOfDaysAbsentForAnEmployeeForGivenMonth(int month,
			PersonalInformation personalInformation,
			CFinancialYear cFinancialYear, Boolean b, int CurYear) {
		Set<Attendence> pdList = null;
		try {
			Map mp = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(
					month, cFinancialYear);
			if (b.equals(Boolean.TRUE)) {
				int dy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				Calendar cal = Calendar.getInstance();
				cal.set(CurYear, month - 1, dy);
				pdList = listOfDaysAbsentForAnEmployeeForGivendates(
						(java.sql.Date) mp.get(STR_START_DATE), new java.sql.Date(
								cal.getTime().getTime()), personalInformation);
			} else {
				pdList = listOfDaysAbsentForAnEmployeeForGivendates(
						(java.sql.Date) mp.get(STR_START_DATE), (java.sql.Date) mp
								.get("endDate"), personalInformation);
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);

		}
		return pdList;

	}

	public Set listOfDaysAbsentForAnEmployeeForGivendates(java.util.Date fromDate, java.util.Date toDate,PersonalInformation personalInformation){
		Set<Attendence> wdList = new HashSet<Attendence>();
		try {
			List objDateList = EisManagersUtill.getListOfFinYrandDate(fromDate,toDate);
			if (objDateList.size() == 1) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList.get(0);
				wdList = getAbsentAttAcrossFinYear(fromDate, toDate,personalInformation, finYrandDateFirst.getFinancial());
			}else if (objDateList.size() == 2) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList.get(0);
				DatePeriodFY finYrandDateSecond = (DatePeriodFY) objDateList.get(1);
				wdList.addAll((Collection) getAbsentAttAcrossFinYear(
								new java.sql.Date(finYrandDateFirst.getFromDate().getTime()), 
								new java.sql.Date(finYrandDateFirst.getToDate().getTime()),
								personalInformation, finYrandDateFirst.getFinancial()));
				wdList.addAll((Collection) getAbsentAttAcrossFinYear(
								new java.sql.Date(finYrandDateSecond.getFromDate().getTime()),
								new java.sql.Date(finYrandDateSecond.getToDate().getTime()),
								personalInformation, finYrandDateSecond.getFinancial()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return wdList;
	}

	private Set getAbsentAttAcrossFinYear(Date fromDate, Date toDate,PersonalInformation personalInformation,CFinancialYear cFinancialYear){
		Set<Attendence> adList = new HashSet<Attendence>();
		try {
			AttendenceDAO attendenceDAO = LeaveDAOFactory.getDAOFactory().getAttendenceDAO();
			for (Iterator iter = attendenceDAO.getListOfAbsentDaysForEmployeebetweenDates(fromDate,	toDate, personalInformation, 
																					cFinancialYear).iterator(); iter.hasNext();) {
				Attendence attendence = (Attendence) iter.next();
				adList.add(attendence);
			}
		} catch (Exception e) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return adList;
	}

	public Set listOfCompOffsForAnEmployeeForGivenMonth(int month,
			PersonalInformation personalInformation,
			CFinancialYear cFinancialYear, Boolean b, int CurYear) {
		Set<String> pdList = null;
		try {
			Map mp = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(
					month, cFinancialYear);
			if (b.equals(Boolean.TRUE)) {
				int dy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				Calendar cal = Calendar.getInstance();
				cal.set(CurYear, month - 1, dy);
				pdList = listOfDaysCompOffsForAnEmployeeForGivendates(
						(java.sql.Date) mp.get(STR_START_DATE), new java.sql.Date(
								cal.getTime().getTime()), personalInformation);
			} else {
				pdList = listOfDaysCompOffsForAnEmployeeForGivendates(
						(java.sql.Date) mp.get(STR_START_DATE), (java.sql.Date) mp
								.get("endDate"), personalInformation);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

		return pdList;

	}

	public Set listOfDaysCompOffsForAnEmployeeForGivendates(Date fromDate, Date toDate, PersonalInformation personalInformation) {
		Set<String> wdList = new HashSet<String>();
		try {
			List objDateList = EisManagersUtill.getListOfFinYrandDate(fromDate, toDate);
			if (objDateList.size() == 1) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList.get(0);
				wdList = getCompOffsAttAcrossFinYear(fromDate, toDate, personalInformation);
			} 
			else if (objDateList.size() == 2) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList.get(0);
				DatePeriodFY finYrandDateSecond = (DatePeriodFY) objDateList.get(1);
				wdList.addAll((Collection) getCompOffsAttAcrossFinYear(
						new java.sql.Date(finYrandDateFirst.getFromDate()
								.getTime()), new java.sql.Date(
								finYrandDateFirst.getToDate().getTime()),
						personalInformation));
				wdList.addAll((Collection) getCompOffsAttAcrossFinYear(
								new java.sql.Date(finYrandDateSecond
										.getFromDate().getTime()),
								new java.sql.Date(finYrandDateSecond
										.getToDate().getTime()),
								personalInformation));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return wdList;
	}

	private Set getCompOffsAttAcrossFinYear(Date fromDate, Date toDate,
			PersonalInformation personalInformation) {

		Set<String> attAbList = new HashSet<String>();
		try {
			AttendenceDAO attendenceDAO = LeaveDAOFactory.getDAOFactory().getAttendenceDAO();
			SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
			for (Iterator iter = attendenceDAO
					.getListOfCompOffDaysForEmployeebetweenDates(fromDate,
							toDate, personalInformation).iterator(); iter
					.hasNext();) {
				Attendence attendence = (Attendence) iter.next();
				attAbList.add(sdf.format(attendence.getAttDate()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return attAbList;
	}

	public Map listOfPaidDaysForAnEmployeeForGivenMonth(int month,
			PersonalInformation personalInformation,
			CFinancialYear cFinancialYear, Boolean b, int CurYear) {
		Map mp = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(
				month, cFinancialYear);
		Map<String, List> leavesMap = null;
		try {
			if (b.equals(Boolean.TRUE)) {
				int dy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				Calendar cal = Calendar.getInstance();
				cal.set(CurYear, month - 1, dy);
				leavesMap = listOfPaidDaysForAnEmployeeForGivenDates(
						(java.sql.Date) mp.get(STR_START_DATE), new java.sql.Date(
								cal.getTime().getTime()), personalInformation);
			} else {
				leavesMap = listOfPaidDaysForAnEmployeeForGivenDates(
						(java.sql.Date) mp.get(STR_START_DATE), (java.sql.Date) mp
								.get("endDate"), personalInformation);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return leavesMap;
	}

	public List getListOfAbsentDaysForEmployeeForGivenMonth(int month,
			PersonalInformation personalInformation,
			CFinancialYear cFinancialYear, Boolean b, int CurYear) {
		Map mp = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(
				month, cFinancialYear);
		List attList = null;
		try {
			AttendenceDAO attendenceDAO = LeaveDAOFactory.getDAOFactory()
					.getAttendenceDAO();
			if (b.equals(Boolean.TRUE)) {
				int dy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				Calendar cal = Calendar.getInstance();
				cal.set(CurYear, month - 1, dy);
				attList = attendenceDAO
						.getListOfAbsentDaysForEmployeebetweenDates(
								(java.sql.Date) mp.get(STR_START_DATE),
								new java.sql.Date(cal.getTime().getTime()),
								personalInformation, cFinancialYear);
			} else {
				attList = attendenceDAO
						.getListOfAbsentDaysForEmployeebetweenDates(
								(java.sql.Date) mp.get(STR_START_DATE),
								(java.sql.Date) mp.get("endDate"),
								personalInformation, cFinancialYear);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return attList;
	}

	public Map listOfPaidDaysForAnEmployeeForGivenDates(Date fromDate,Date toDate, PersonalInformation personalInformation) {
		AttendenceDAO attendenceDAO = LeaveDAOFactory.getDAOFactory().getAttendenceDAO();
		Map leavesMap = attendenceDAO.getMapOfPaidAndUnpaidDaysForEmployeebetweenDates(fromDate,toDate, personalInformation);
		return leavesMap;
	}

	/*
	 * public Map listOfPaidDaysForAnEmployeeForGivenDates(Date fromDate,Date
	 * toDate,PersonalInformation personalInformation) { Map<java.util.Date,String>
	 * pWMap = new HashMap<java.util.Date,String>(); Map<String,List>
	 * leavesMap = new HashMap<String,List>(); List paidList = new ArrayList();
	 * List unPaidList = new ArrayList(); Set presentDaysInMonth =
	 * listOfDaysWorkedForAnEmployeebetweenTwoDates(fromDate,toDate,personalInformation);
	 * SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE);
	 * LeaveApplicationDAO leaveApplicationDAO =
	 * LeaveDAOFactory.getDAOFactory().getLeaveApplicationDAO();
	 * LeaveApplication leaveApplication =null; try {
	 * logger.info("qqqqqqqqqq"+personalInformation.getIdPersonalInformation());
	 * logger.info("qqqqqqqqqq"+getNoOfWorkingDaysBweenTwoDates(fromDate,toDate));
	 * for(Iterator iter =
	 * getNoOfWorkingDaysBweenTwoDates(fromDate,toDate).iterator();iter.hasNext();) {
	 * String wdStr = (String)iter.next();
	 * 
	 * logger.info("qqqqqqqqqq"+presentDaysInMonth.contains(wdStr));
	 * logger.info("qqqqqqqqqq"+leaveApplicationDAO.getLeaveApplicationByEmpAndDate(new
	 * java.sql.Date(sdf.parse(wdStr).getTime()) , personalInformation)!=null);
	 * if(!presentDaysInMonth.contains(wdStr) &&
	 * leaveApplicationDAO.getLeaveApplicationByEmpAndDate(new
	 * java.sql.Date(sdf.parse(wdStr).getTime()) , personalInformation)!=null) {
	 * leaveApplication =
	 * leaveApplicationDAO.getLeaveApplicationByEmpAndDate(new
	 * java.sql.Date(sdf.parse(wdStr).getTime()) , personalInformation);
	 * logger.info("qqqqqqqqqq"+wdStr);
	 * if(checkPayEligible(leaveApplication.getId())==false) {
	 * logger.info("checkPayEligiblefalse"+wdStr);
	 * 
	 * pWMap = new HashMap<java.util.Date,String>();
	 * if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().toString().equals("1") &&
	 * leaveApplication.getTwoHdLeaves().equals('1')) {
	 * pWMap.put(sdf.parse(wdStr), "Full"); logger.info("FullIsHalfDay"+wdStr);
	 *  } else
	 * if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().toString().equals("1")) {
	 * pWMap.put(sdf.parse(wdStr), "HalfAndAb"); logger.info("HalfAndAb"+wdStr);
	 *  } else { pWMap.put(sdf.parse(wdStr), "Full"); logger.info("Full"+wdStr);
	 *  }
	 * 
	 * if(leavesMap.containsKey("unpaidLeaves")) {
	 * ((List)leavesMap.get("unpaidLeaves")).add(pWMap); } else { unPaidList =
	 * new ArrayList(); unPaidList.add(pWMap); leavesMap.put("unpaidLeaves",
	 * unPaidList); } logger.info("leavesMapunpaidLeaves"+leavesMap);
	 *  } else {
	 * 
	 * if(checkPayEligible(leaveApplication.getId())==true &&
	 * leaveApplication.getNoOfLeavesAvai()>=leaveApplication.getWorkingDays()) {
	 * logger.info("checkPayEligibletrue"+wdStr); pWMap = new HashMap<java.util.Date,String>();
	 * 
	 * if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().toString().equals("1") &&
	 * leaveApplication.getTwoHdLeaves().equals('1')) {
	 * pWMap.put(sdf.parse(wdStr), "Full");
	 * logger.info("IsHalfDaycheckPayEligibletrue"+wdStr);
	 *  } else
	 * if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().toString().equals("1")) {
	 * pWMap.put(sdf.parse(wdStr), "HalfAndAb");
	 * logger.info("HalfAndAbcheckPayEligibletrue"+wdStr); } else {
	 * pWMap.put(sdf.parse(wdStr), "Full");
	 * logger.info("FullcheckPayEligibletrue"+wdStr);
	 *  } if(leavesMap.containsKey("paidLeaves")) {
	 * ((List)leavesMap.get("paidLeaves")).add(pWMap); } else { paidList = new
	 * ArrayList(); paidList.add(pWMap); leavesMap.put("paidLeaves", paidList); }
	 * logger.info("checkPayEligibletrue"+leavesMap); } else
	 * if(checkPayEligible(leaveApplication.getId())==true &&
	 * leaveApplication.getNoOfLeavesAvai() < leaveApplication.getWorkingDays()) {
	 * pWMap = new HashMap<java.util.Date,String>();
	 * logger.info("checkPayEligibletrue<"+wdStr); Map mp =
	 * getMapOfLeaveDayAndPayEligible(leaveApplication,getFinantialObJ(wdStr));
	 * logger.info("checkPayEligibletrue<"+mp);
	 * if(leaveApplication.getNoOfLeavesAvai()>=1.0f) {
	 * logger.info("checkPayEligibletrue<getNoOfLeavesAvai()>=1.0f");
	 * if(((Integer)mp.get(wdStr)).equals(new Integer(1))) {
	 * 
	 * if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().toString().equals("1") &&
	 * leaveApplication.getTwoHdLeaves().equals('1')) {
	 * pWMap.put(sdf.parse(wdStr), "Full"); logger.info("checkPayEligibletrue<getIsHalfDay");
	 *  } else
	 * if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().toString().equals("1")) {
	 * pWMap.put(sdf.parse(wdStr), "HalfAndAb");
	 * logger.info("checkPayEligibletrue<HalfAndAb"); } else {
	 * pWMap.put(sdf.parse(wdStr), "Full"); logger.info("checkPayEligibletrue<Full");
	 *  } if(leavesMap.containsKey("paidLeaves")) {
	 * ((List)leavesMap.get("paidLeaves")).add(pWMap); } else { paidList = new
	 * ArrayList(); paidList.add(pWMap); leavesMap.put("paidLeaves", paidList); }
	 * logger.info("checkPayEligibletrue<leavesMap");
	 *  } else {
	 * 
	 * if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().toString().equals("1") &&
	 * leaveApplication.getTwoHdLeaves().equals('1')) {
	 * pWMap.put(sdf.parse(wdStr), "Full"); logger.info("checkPayEligibletrue<getIsHalfDay2"+wdStr);
	 *  } else
	 * if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().toString().equals("1")) {
	 * pWMap.put(sdf.parse(wdStr), "HalfAndAb");
	 * logger.info("checkPayEligibletrue<HalfAndAb2"+wdStr); } else {
	 * pWMap.put(sdf.parse(wdStr), "Full"); logger.info("checkPayEligibletrue<Full2"+wdStr);
	 *  } if(leavesMap.containsKey("unpaidLeaves")) {
	 * ((List)leavesMap.get("unpaidLeaves")).add(pWMap); } else { unPaidList =
	 * new ArrayList(); unPaidList.add(pWMap); leavesMap.put("unpaidLeaves",
	 * unPaidList); } logger.info("checkPayEligibletrue<leavesMap2"+leavesMap); } }
	 * else { pWMap = new HashMap<java.util.Date,String>();
	 * 
	 * logger.info("checkPayEliszdgsdgggggggggggggggibletrue<leavesMap2");
	 * 
	 * if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().toString().equals("1") &&
	 * leaveApplication.getTwoHdLeaves().equals('1')) {
	 * pWMap.put(sdf.parse(wdStr), "Full");
	 *  } else
	 * if(leaveApplication.getTypeOfLeaveMstr().getIsHalfDay().toString().equals("1")) {
	 * pWMap.put(sdf.parse(wdStr), "HalfAndAb"); } else {
	 * pWMap.put(sdf.parse(wdStr), "Full");
	 *  } if(leavesMap.containsKey("unpaidLeaves")) {
	 * ((List)leavesMap.get("unpaidLeaves")).add(pWMap); } else { unPaidList =
	 * new ArrayList(); unPaidList.add(pWMap); leavesMap.put("unpaidLeaves",
	 * unPaidList); } }
	 *  } }
	 *  } } } catch(Exception e) {  throw new
	 * EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e); } return
	 * leavesMap; }
	 * 
	 */
	private CFinancialYear getFinantialObJ(String strFormat) {
		CFinancialYear cFinancialYear = null;
		SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
		try {
			java.util.Date date = sdf.parse(strFormat);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			SimpleDateFormat formatter = new SimpleDateFormat(STR_DAYMONYEAR,Locale.getDefault());
			String fromFinId = formatter.format(calendar.getTime());
			cFinancialYear = commonsService.findFinancialYearById(Long.valueOf(fromFinId));

		} catch (Exception e) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

		return cFinancialYear;
	}

	public Set listOfWorkingDaysInMonth(int monthId,
			CFinancialYear cFinancialYear) {
		if (cFinancialYear != null)
		{
			HibernateUtil.getCurrentSession().lock(cFinancialYear,
					LockMode.NONE);
		}
		Set<String> wdList = new HashSet<String>();
		try {
			if (cFinancialYear != null)
				{
					java.util.Date startingDate = cFinancialYear.getStartingDate();
					Set holidaySet = getHolidaySet(cFinancialYear);
					Calendar calendar = Calendar.getInstance();
					int m = monthId - 1;
					int year = Integer.valueOf(startingDate.toString().substring(0, 4)).intValue();
					Calendar cal = null;
					if (m < 3)
					{
						cal = new GregorianCalendar(year + 1, m, 1);
					}
					else
					{
						cal = new GregorianCalendar(year, m, 1);
					}
					int noOfDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
					// int
					// noOfDaysInMonth=EisManagersUtill.getMonthsVsDays(m).intValue();
					for (int i = 1; i <= noOfDaysInMonth; i++) {
						if (m < 3) {
							calendar.set(year + 1, m, i);
						} else {
							calendar.set(year, m, i);
						}
						if (!holidaySet.contains(new java.sql.Date(calendar.getTime()
								.getTime()).toString())) {
							wdList.add(new java.sql.Date(calendar.getTime().getTime())
									.toString());
						}
					}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return wdList;
	}

	private Set listOfWorkingDaysInMonthTillToDate(int monthId,
			CFinancialYear cFinancialYear) {

		Set<String> wdList = new HashSet<String>();
		try {
			HibernateUtil.getCurrentSession().lock(cFinancialYear,
					LockMode.NONE);
			java.util.Date startingDate = cFinancialYear.getStartingDate();
			Set holidaySet = getHolidaySet(cFinancialYear);
			Calendar calendar = Calendar.getInstance();
			int m = monthId - 1;
			int year = Integer.valueOf((startingDate.toString().substring(0, 4)))
					.intValue();
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			for (int i = 1; i <= day; i++) {
				if (m < 3) {
					calendar.set(year + 1, m, i);
				} else {
					calendar.set(year, m, i);
				}
				if (!holidaySet.contains(new java.sql.Date(calendar.getTime()
						.getTime()).toString())) {
					wdList.add(new java.sql.Date(calendar.getTime().getTime())
							.toString());
				}
			}
		} catch (HibernateException e) {

			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		} catch (Exception e) {

			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return wdList;
	}

	public Set getHolidaySet(CFinancialYear cFinancialYear) {
		
		Set<String> holidaySet = new HashSet<String>();
		try {
			HolidaysUlbDAO holidaysUlbHibernateDAO = LeaveDAOFactory
					.getDAOFactory().getHolidaysUlbDAO();
			List holidaysUlbList = holidaysUlbHibernateDAO
					.getHolidayListByFinalsialYearId(cFinancialYear);

			Wdaysconstnts wdaysconstnts = getWDconstant();
			Collection wdaysconstntsColl = wdaysconstnts
					.getListOfHolidays(cFinancialYear);
			Collection sunDColl = EisManagersUtill
					.getSundaysForGivenCurrentFinYear(cFinancialYear);

			holidaySet.addAll((Collection) holidaysUlbList);
			holidaySet.addAll((Collection) wdaysconstntsColl);
			holidaySet.addAll((Collection) sunDColl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);

		}
		return holidaySet;
	}
	public Integer getNextVal() {

		session = HibernateUtil.getCurrentSession();
		Integer id = Integer.valueOf(0);
		try {
			Query qry = session.createSQLQuery(
					"SELECT LEAVE_APP_SEQ.nextval as id from dual").addScalar(
					"id", IntegerType.INSTANCE);
			if (qry.list() != null && !qry.list().isEmpty()) {
				Integer obj = null;
				for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
					obj = (Integer) iter.next();
					id = obj;
				}

			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
		
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return id;
	}

	public Float getLeaveBalancesByempIDandTypeId(Integer empId,
			Integer typeOfLeaveId) {
		session = HibernateUtil.getCurrentSession();
		String finId = EisManagersUtill.getCommonsService()
				.getCurrYearFiscalId();
		CFinancialYear financialYear = EisManagersUtill.getCommonsService()
				.findFinancialYearById(Long.valueOf(finId));
		float ob = 0.0f;
		try {
			Query qry = session
					.createQuery("select lb.noOfLeavesAvai from  LeaveOpeningBalance lb where "
							+ "lb.employeeId.idPersonalInformation = :empId and  lb.typeOfLeaveMstr.id = :typeOfLeaveId "
							+ "and lb.financialId = :financialYear");
			qry.setEntity("financialYear", financialYear);
			if (empId != null) {
				qry.setInteger("empId", empId);

			}
			if (typeOfLeaveId != null) {
				qry.setInteger("typeOfLeaveId", typeOfLeaveId);

			}
			if (qry.list() != null && !qry.list().isEmpty()) {
				Float obj = null;
				for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
					obj = (Float) iter.next();
					ob += obj;
				}

			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return ob;
	}
	
	public Float getLeaveBalancesForGivenDateByempIDandTypeId(Integer empId,
			Integer typeOfLeaveId,Date givenDate) 
	{
		session = HibernateUtil.getCurrentSession();
		//FIXME: change to get financial based on givendate
		String dateId="";
		Date myGivenDate = givenDate;
		LOGGER.debug("EMP ID"+empId+"LEAVE TYPE"+typeOfLeaveId);
		Query qry=null;
		CFinancialYear financialYear=null;
		CalendarYear calendarYear=null;
		CFinancialYear finId = null;
		SimpleDateFormat smt = new SimpleDateFormat(STR_DAYMONYEAR,Locale.getDefault());
		if(isLeaveCalendarBased())
		{
			if(myGivenDate==null)
			{
				myGivenDate = new Date();
				dateId=getYearIdByGivenDate(smt.format(myGivenDate));
			}
			else
			{
				dateId=getYearIdByGivenDate(smt.format(myGivenDate));
			}
			calendarYear=getCalendarYearById(Long.valueOf(dateId));
			
		}
		else
		{
			if(myGivenDate==null)
			{
				myGivenDate = new Date();
				finId=EisManagersUtill.getCommonsService().getFinancialYearByDate(myGivenDate);
				dateId = (finId.getId()).toString();
			}
			else
			{
				finId=EisManagersUtill.getCommonsService().getFinancialYearByDate(myGivenDate);
				dateId = (finId.getId()).toString();
			}
		    financialYear = EisManagersUtill.getCommonsService().findFinancialYearById(Long.valueOf(dateId));
		}
		float ob = 0.0f;
		try {
			String  main = "select lb.noOfLeavesAvai from  LeaveOpeningBalance lb where "
							+ "lb.employeeId.idPersonalInformation = :empId and  lb.typeOfLeaveMstr.id = :typeOfLeaveId";
							
			
			if(financialYear!=null)
			{
				main+=" and lb.financialId = :financialYear";
			}
			if(calendarYear!=null)
			{
				
				main+=" and lb.calendarId = :calendarYear";
				
			}
			
			qry=session.createQuery(main);
			
			
			if(financialYear!=null)
			{
				qry.setEntity("financialYear", financialYear);
			}
			if(calendarYear!=null)
			{
				qry.setEntity("calendarYear", calendarYear);
			}
			
			if (empId != null) {
				qry.setInteger("empId", empId);

			}
			if (typeOfLeaveId != null) {
				qry.setInteger("typeOfLeaveId", typeOfLeaveId);

			}
			if (qry.list() != null && !qry.list().isEmpty()) {
				Float obj = null;
				for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
					obj = (Float) iter.next();
					ob += obj;
				}

			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		
		LOGGER.info("1ST API RESULT------"+ob);
		return ob;
	
	}

	public float getLeavesEarnedinTheGivenYear(Integer empId,
			Integer type, Date givenDate) {

		float earnedLeaves = 0.0f;
		Integer desigId = Integer.valueOf(0);
		String isProrated=EGovConfig.getAppConfigValue(EisConstants.MODULE_LEAVEAPP,EisConstants.ISPRORATED,"yes");
		LeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
		.getLeaveMasterDAO();
		if(isProrated.equalsIgnoreCase("yes"))
		{
		try {
			List list = EisManagersUtill.getEmployeeService()
					.getHistoryOfEmpForCurrentFinY(empId, givenDate);
			// logger.info("list.size = "+list.size());
			java.sql.Date todesDate = null;
			java.sql.Date fromdesDate = null;
			for (Iterator ietr = list.iterator(); ietr.hasNext();) {
				SearchEmpDTO searchEmpDTO = (SearchEmpDTO) ietr.next();
				desigId = searchEmpDTO.getDesID();
				Integer noOfLeaves = leaveMasterDAO.getNoOfDaysForDesIDandType(
						desigId, type);
				// logger.info("desigId = "+desigId);
				// logger.info("noOfLeaves = "+noOfLeaves);
				// logger.info("type = "+type);
				todesDate = searchEmpDTO.getToDate();
				// logger.info("todesDate = "+todesDate);
				fromdesDate = searchEmpDTO.getFromDate();
				// logger.info("fromdesDate = "+fromdesDate);
				float todesDateMonth = todesDate.getMonth();
				float fromdesDateMonth = fromdesDate.getMonth();
				/*
				 * if(todesDateMonth<3) todesDateMonth =todesDate.getMonth()+9;
				 * if(fromdesDateMonth<3) fromdesDateMonth
				 * =fromdesDate.getMonth()+9; float month =
				 * todesDateMonth-fromdesDateMonth;
				 */

				float month = getNumberOfMonths(
						(Date) UTILDATEFORMATTER.parse(UTILDATEFORMATTER.format(fromdesDate)),
						(java.util.Date) UTILDATEFORMATTER
								.parse(UTILDATEFORMATTER.format(todesDate)));
				 LOGGER.info("Before : months = "+month);
				float days = getDaysDiff(fromdesDate, todesDate);
				 LOGGER.info("days = "+days);
				int year = -1;
				int noOfDaysInMonth = -1;
				Calendar cal = null;
				if (getDaysForDate(todesDate) > getDaysForDate(fromdesDate)) {
					year = Integer.valueOf(todesDate.toString().substring(0, 4))
							.intValue();
					cal = new GregorianCalendar(year, new Float(todesDateMonth)
							.intValue(), 1);
					noOfDaysInMonth = cal
							.getActualMaximum(Calendar.DAY_OF_MONTH);
					if (noOfDaysInMonth != -1)
						month = month + days
								/ (Integer.valueOf(noOfDaysInMonth)).floatValue();
				} else if (days == 0) {
					year = Integer.valueOf(todesDate.toString().substring(0, 4))
							.intValue();
					cal = new GregorianCalendar(year, new Float(todesDateMonth)
							.intValue(), 1);
					noOfDaysInMonth = cal
							.getActualMaximum(Calendar.DAY_OF_MONTH);
					month = month + 1
							/ (Integer.valueOf(noOfDaysInMonth)).floatValue();
				} else if (getDaysForDate(todesDate) < getDaysForDate(fromdesDate)) {
					year =Integer.valueOf(fromdesDate.toString().substring(0, 4))
							.intValue();
					cal = new GregorianCalendar(year, new Float(
							fromdesDateMonth).intValue(), 1);
					noOfDaysInMonth = cal
							.getActualMaximum(Calendar.DAY_OF_MONTH);
					month = month + days
							/ (Integer.valueOf(noOfDaysInMonth)).floatValue();
				}
				 LOGGER.info("After : months = "+month);
				// check this out
				earnedLeaves = earnedLeaves
						+ (month * noOfLeaves.floatValue() / 12.f);
				 LOGGER.info("earnedLeaves = "+earnedLeaves);

			}
		} catch (Exception e) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		}else
		{
			AssignmentPrd assPeriod=EisManagersUtill.getEmployeeService().getAssignmentPrdByEmpAndDate(givenDate, empId);
			
				Set<Assignment> empAssignmentSet=assPeriod.getEgpimsAssignment();
				for(Assignment assignment:empAssignmentSet)
				{
					desigId=assignment.getDesigId().getDesignationId();
					break;
				}
				earnedLeaves=	leaveMasterDAO.getNoOfDaysForDesIDandType(
						desigId, type);
			
		}
		LOGGER.info("EARNED LEAVES---"+earnedLeaves);
		return earnedLeaves;
	}

	private float getDaysDiff(Date fromDate, Date toDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		float days = Float.valueOf((Integer.valueOf(sdf.format(toDate).substring(0, 2)) - Integer.valueOf(
						sdf.format(fromDate).substring(0, 2))));
		return days;
	}

	private float getDaysForDate(Date date) {
		float days = 0.0f;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		 days = Integer.valueOf(sdf.format(date).substring(0, 2)).floatValue();
		return days;
	}

	/**
	 * Returns the number of months between the the 2 given dates
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getNumberOfMonths(java.util.Date startDate,
			java.util.Date endDate) {
		int diffMonth = 0;
		try {
			assert startDate.before(endDate);
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(startDate);
			int startMonth = calendar.get(Calendar.MONTH) + 1;
			int startYear = calendar.get(Calendar.YEAR);

			calendar.setTime(endDate);
			int endMonth = calendar.get(Calendar.MONTH) + 1;
			int endYear = calendar.get(Calendar.YEAR);

			// logger.info("startMonth " + startMonth +" startYear " +
			// startYear);
			// logger.info("\n endMonth "+ endMonth+ " endYear " + endYear);

			if (startYear < endYear) {
				endMonth += (endYear - startYear) * 12;
			}
			diffMonth = endMonth - startMonth;
			// logger.info("diffMonth "+diffMonth);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);

		}

		return diffMonth;
	}

	public Integer getLeavesAvailedinTheYear(Integer empId, Integer type,Date givenDate) {
		int wDays = 0;
		session = HibernateUtil.getCurrentSession();
		List leaveAppList = null;
		SimpleDateFormat sqlUtil = new SimpleDateFormat(STR_DAYMONYEAR,Locale.getDefault());
		SimpleDateFormat formatter = new SimpleDateFormat(STR_DAYMONYEAR,Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		java.util.Date stFyDate=null;
		java.util.Date nxtFyDate=null;
		CFinancialYear nextFinancialYear=null;
		CalendarYear nextCalendarYear=null;
		CalendarYear calYr =null;
		CFinancialYear financialYear=null;
		String nxtId="";
		try {
			
			if(isLeaveCalendarBased())
			{
				String calId = EisManagersUtill.getEmpLeaveService().getYearIdByGivenDate(sqlUtil.format(givenDate));
				if(calId==null)
				{
				
				 throw new DuplicateElementException("Calendar year not set");
				}
				else 
				{
					 calYr=getCalendarYearById(Long.valueOf(calId));
				}
				calendar.setTime(calYr.getEndingDate());
				calendar.add(Calendar.DATE, 1);
				nxtId =getYearIdByGivenDate(formatter.format(calendar.getTime()));
				nextCalendarYear=getCalendarYearById(Long.valueOf(nxtId));
				stFyDate=calYr.getStartingDate();
				nxtFyDate=nextCalendarYear.getStartingDate();
			}
			else
			{
				
				String finId = sqlUtil.format(givenDate);
				if(finId==null)
				{
					throw new DuplicateElementException("Financial year not set");
					
				}
				else
					{
					 financialYear=commonsService.findFinancialYearById(Long.valueOf(finId));
					}
				calendar.setTime(financialYear.getEndingDate());
				calendar.add(Calendar.DATE, 1);
			
				nxtId =formatter.format(calendar.getTime());
				nextFinancialYear = commonsService.findFinancialYearById(Long.valueOf(nxtId));
				stFyDate = financialYear.getStartingDate();
				nxtFyDate = nextFinancialYear.getStartingDate();
			}
			String mainStr = "select  la.workingDays,la.fromDate,la.toDate from LeaveApplication la  where la.employeeId.idPersonalInformation = :empId and la.toDate >= :toDate and la.fromDate < :fromDate and la.statusId.name = :statusName  and  la.typeOfLeaveMstr.id = :ltId ";
			Query qry = session.createQuery(mainStr);

			if (empId.intValue() != 0) {
				qry.setInteger("empId", empId);

			}
			qry.setDate("toDate", new java.sql.Date(stFyDate.getTime()));
			qry.setDate("fromDate", new java.sql.Date(nxtFyDate.getTime()));
			qry.setString("statusName",
					org.egov.pims.utils.EisConstants.STATUS_APPROVED);
			qry.setInteger("ltId", type);
			leaveAppList = qry.list();
			for (Iterator iter = leaveAppList.iterator(); iter.hasNext();) {
				Object[] objArray = (Object[]) iter.next();
				wDays = wDays
						+ calculateLeave(objArray,stFyDate,
								nxtFyDate);
			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}

		LOGGER.info("WORKING DAYS TOTAL"+wDays);
		return wDays;
	}

	private Integer calculateLeave(Object[] objArray,
			java.util.Date stFyDate,
			java.util.Date nxtFyDate) {
		int len = objArray.length;
		int worDays = 0;
		try {
			Integer workingDays = 0;
			java.sql.Date fromDate = null;
			java.sql.Date toDate = null;
			java.sql.Date fromdesDate = null;
			java.sql.Date todesDate = null;
			for (int i = 0; i < len; i++) {
				if (i == 0) {
					workingDays = (Integer) objArray[i];
				}

				else if (i == 1) {
					fromDate = (java.sql.Date) objArray[i];
				} else if (i == 2) {
					toDate = (java.sql.Date) objArray[i];
				}
				if (i == 2) {
					int wd = 0;
					Calendar rightNow = Calendar.getInstance();
					if (fromDate.getTime() <= stFyDate.getTime()) {
						fromdesDate = new java.sql.Date(stFyDate.getTime());
						todesDate = toDate;
						wd = calculateNoOfWorkingDaysBweenTwoDates(fromdesDate,
								todesDate);
					}
					if (toDate.getTime() >= nxtFyDate.getTime()) {
						fromdesDate = toDate;
						rightNow.setTime(nxtFyDate);
						rightNow.set(Calendar.YEAR,
								rightNow.get(Calendar.YEAR) - 1);
						rightNow.set(rightNow.get(Calendar.YEAR), 2, 31);
						todesDate = new java.sql.Date(rightNow.getTime()
								.getTime());
						wd = calculateNoOfWorkingDaysBweenTwoDates(fromdesDate,
								todesDate);
					}
					if (wd == 0) {
						
						worDays = workingDays;
						
						
					} else {
						worDays = wd;
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return worDays;
	}

	public Float getMaxEligibleinTheCurrentYear(Integer empId, Integer type,
			Date givenDate) {
		return new Float(getLeaveBalancesForGivenDateByempIDandTypeId(empId, type,givenDate)
				.floatValue()
				+ getLeavesEarnedinTheGivenYear(empId, type, givenDate));
	}

	public Float getAvailableLeavs(Integer empId, Integer type, Date givenDate) {
		//FIXME: get Calendar Year and Fin Year based on givenData
		Float eligible = getMaxEligibleinTheCurrentYear(empId, type,
				givenDate);
		Integer availed = getLeavesAvailedinTheYear(empId, type,givenDate);
				
		Integer encashed = getLeavesEncashedinTheYear(empId, type,givenDate);
		float available = 0.0f;
		available = eligible.floatValue() - availed.floatValue()
				- encashed.floatValue();
		return new Float(available);
	}

	/*
	 * added for leave encashment payslip
	 */
	public Float getMaxEligibleInGivenFinancialYear(Integer empId,
			Integer type, Date givenDate, CFinancialYear cFinancialsYear) {
		return new Float(getLeaveBalancesByempIDandTypeId(empId, type)
				.floatValue()
				+ getLeavesEarnedinTheGivenFinYear(empId, type, givenDate,
						cFinancialsYear));
	}

	public float getLeavesEarnedinTheGivenFinYear(Integer empId, Integer type,
			Date givenDate, CFinancialYear cFinancialsYear) {

		float earnedLeaves = 0.0f;
		try {
			LeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
					.getLeaveMasterDAO();
			List list = EisManagersUtill.getEmployeeService()
					.getHistoryOfEmpForGivenFinY(empId, givenDate,
							cFinancialsYear);
			// logger.info("list.size = "+list.size());
			Integer desigId = Integer.valueOf(0);
			java.sql.Date todesDate = null;
			java.sql.Date fromdesDate = null;
			for (Iterator ietr = list.iterator(); ietr.hasNext();) {
				SearchEmpDTO searchEmpDTO = (SearchEmpDTO) ietr.next();
				desigId = searchEmpDTO.getDesID();
				Integer noOfLeaves = leaveMasterDAO.getNoOfDaysForDesIDandType(
						desigId, type);
				// logger.info("desigId = "+desigId);
				// logger.info("noOfLeaves = "+noOfLeaves);
				// logger.info("type = "+type);
				todesDate = searchEmpDTO.getToDate();
				// logger.info("todesDate = "+todesDate);
				fromdesDate = searchEmpDTO.getFromDate();
				// logger.info("fromdesDate = "+fromdesDate);
				float todesDateMonth = todesDate.getMonth();
				float fromdesDateMonth = fromdesDate.getMonth();
				/*
				 * if(todesDateMonth<3) todesDateMonth =todesDate.getMonth()+9;
				 * if(fromdesDateMonth<3) fromdesDateMonth
				 * =fromdesDate.getMonth()+9; float month =
				 * todesDateMonth-fromdesDateMonth;
				 */

				float month = getNumberOfMonths(
						(Date) UTILDATEFORMATTER
								.parse(UTILDATEFORMATTER.format(fromdesDate)),
						(Date) UTILDATEFORMATTER
								.parse(UTILDATEFORMATTER.format(todesDate)));
				// logger.info("Before : months = "+month);
				float days = getDaysDiff(fromdesDate, todesDate);
				// logger.info("days = "+days);
				int year = -1;
				int noOfDaysInMonth = -1;
				Calendar cal = null;
				if (getDaysForDate(todesDate) > getDaysForDate(fromdesDate)) {
					year = Integer.valueOf(todesDate.toString().substring(0, 4))
							.intValue();
					cal = new GregorianCalendar(year, new Float(todesDateMonth)
							.intValue(), 1);
					noOfDaysInMonth = cal
							.getActualMaximum(Calendar.DAY_OF_MONTH);
					if (noOfDaysInMonth != -1)
						month = month + days
								/ (Integer.valueOf(noOfDaysInMonth)).floatValue();
				} else if (days == 0) {
					year =Integer.valueOf(todesDate.toString().substring(0, 4))
							.intValue();
					cal = new GregorianCalendar(year, new Float(todesDateMonth)
							.intValue(), 1);
					noOfDaysInMonth = cal
							.getActualMaximum(Calendar.DAY_OF_MONTH);
					month = month + 1
							/ (Integer.valueOf(noOfDaysInMonth)).floatValue();
				} else if (getDaysForDate(todesDate) < getDaysForDate(fromdesDate)) {
					year = Integer.valueOf(fromdesDate.toString().substring(0, 4))
							.intValue();
					cal = new GregorianCalendar(year, new Float(
							fromdesDateMonth).intValue(), 1);
					noOfDaysInMonth = cal
							.getActualMaximum(Calendar.DAY_OF_MONTH);
					month = month + days
							/ (Integer.valueOf(noOfDaysInMonth)).floatValue();
				}
				// logger.info("After : months = "+month);
				// check this out
				earnedLeaves = earnedLeaves
						+ (month * noOfLeaves.floatValue() / 12.f);
				// logger.info("earnedLeaves = "+earnedLeaves);

			}
		} catch (Exception e) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return earnedLeaves;
	}

	public Integer getLeavesEncashedinTheYear(Integer empId, Integer type,Date givenDate) {
		int wDays = 0;
		session = HibernateUtil.getCurrentSession();
		SimpleDateFormat sqlUtil = new SimpleDateFormat(STR_DAYMONYEAR,Locale.getDefault());
		String calId="";
		String finId="";
		CalendarYear calendarYear=null;
		CFinancialYear cFinancialYear=null;
		List<LeaveApplication> leaveAppList = null;
		try {
			//get calendar object for the given date
			if(isLeaveCalendarBased())
			{
				calId = getYearIdByGivenDate(sqlUtil.format(givenDate));
				calendarYear=getCalendarYearById(Long.valueOf(calId));
			}
			else
			{
			//get financial object for the given date
				finId = sqlUtil.format(givenDate);
				cFinancialYear=commonsService.findFinancialYearById(Long.valueOf(finId));
			}
			String mainStr = "from LeaveApplication la  "
					+ "where la.employeeId.idPersonalInformation = :empId and la.statusId.name = :statusName  and  la.typeOfLeaveMstr.id = :ltId"
					+ " and la.isEncashment = :encashment";
			
			//FIXME: to do check isCalendarYearBased and then build the query
			
			if(calendarYear!=null)
			   mainStr+=" and la.calYear = :calendarYear";
			
		    if(cFinancialYear!=null)
			   mainStr+=" and la.financialY = :cFinancialYear";
			
			Query qry = session.createQuery(mainStr);

			if (empId.intValue() != 0) {
				qry.setInteger("empId", empId);

			}
			qry.setString("statusName",
					org.egov.pims.utils.EisConstants.STATUS_APPROVED);
			qry.setInteger("ltId", type);
			qry.setInteger("encashment", Integer.valueOf(1));
			
				
			if(calendarYear!=null)
				qry.setEntity("calendarYear", calendarYear);
			
			
			if(cFinancialYear!=null)
				qry.setEntity("cFinancialYear", cFinancialYear);
			
			
			leaveAppList = qry.list();
			for (LeaveApplication la : leaveAppList)
			{
				wDays = wDays + la.getWorkingDays();
			}
		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}

		return wDays;
	}

	

	private void runBuLogic(Integer empId, CFinancialYear financialY,CalendarYear calYear,String statusName) {

		session = HibernateUtil.getCurrentSession();
		setApprovedLeavesForEmpID(empId);
		StringBuffer mainStr =new StringBuffer(200) ;
		mainStr.append("SELECT la.noOfLeavesAvai,la.applicationNumber,la.statusId.id,la.employeeId.idPersonalInformation,la.sanctionNo,"
				+ "la.typeOfLeaveMstr.id,la.fromDate,la.toDate,la.workingDays,la.id,la.desigId.designationId from LeaveApplication la   "
				+ " where la.employeeId.idPersonalInformation =:empId  ");//and la.statusId.name = :statusName";
		if(financialY!=null)
		{
			mainStr.append(" and la.financialY  = :financialY");
		}
		else if(calYear!=null)
		{
			mainStr.append(" and la.calYear =:calYear");
		}
		if(statusName!=null && !statusName.isEmpty())
		{
			mainStr.append(" and la.statusId.name ='"+statusName+"'");
		}
		List leaveAppList =null;

		try {
			Query qry = session.createQuery(mainStr.toString());

			listOfLeveTrnx = new ArrayList();
			listOfLeaveCard = new ArrayList();
			qry.setInteger("empId", empId);
			if(financialY!=null)
			{
			qry.setEntity("financialY", financialY);
			}
			else if(calYear!=null)
			{
				qry.setEntity("calYear", calYear);
			}
			/*qry.setString("statusName",
					org.egov.pims.utils.EisConstants.STATUS_APPROVED);*/
			leaveAppList = qry.list();
			for (Iterator iter = leaveAppList.iterator(); iter.hasNext();) {
				Object[] objArray = (Object[]) iter.next();
				LeaveReports(objArray);
			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}

	}

	private void LeaveReports(Object[] objArray) {
		try {
			int len = objArray.length;
			ViewLeaveTxns viewLeaveTxns = null;
			LeaveCard leaveCard = null;
			Integer statusId = null;
			Integer id = null;
			Integer leaveTypeId = null;
			java.sql.Date fromDate = null;
			java.sql.Date toDate = null;
			Integer worDays = null;
			Integer designIdDays = null;
			Float noOfLeavesAv = null;
			String appNo = null;
			Long appId = null;
			String sanNo = null;
			DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
			DesignationMaster designationMaster = null;
			TypeOfLeaveMaster typeOfLeaveMaster = null;
			for (int i = 0; i < len; i++) {
				if (i == 0) {

					noOfLeavesAv = (Float) objArray[i];
				}

				else if (i == 1) {
					appNo = (String) objArray[i];
				} else if (i == 2) {
					statusId = (Integer) objArray[i];
				} else if (i == 3) {
					id = (Integer) objArray[i];
				} else if (i == 4) {
					sanNo = (String) objArray[i];
				} else if (i == 5) {
					leaveTypeId = (Integer) objArray[i];
				} else if (i == 6) {
					fromDate = (java.sql.Date) objArray[i];
				} else if (i == 7) {
					toDate = (java.sql.Date) objArray[i];
				} else if (i == 8) {
					worDays = (Integer) objArray[i];
				} else if (i == 9) {
					appId = (Long) objArray[i];
				} else if (i == 10) {
					designIdDays = (Integer) objArray[i];
				}

				if (i == 10) {
					viewLeaveTxns = new ViewLeaveTxns();
					leaveCard = new LeaveCard();
					viewLeaveTxns.setSan(sanNo);
					typeOfLeaveMaster = getTypeOfLeaveMasterById(leaveTypeId);
					viewLeaveTxns.setType(typeOfLeaveMaster.getName());
					leaveCard.setType(typeOfLeaveMaster.getName());
					viewLeaveTxns.setTo(toDate);
					viewLeaveTxns.setFrom(fromDate);
					viewLeaveTxns.setTotal(worDays);
					viewLeaveTxns.setStatusId(statusId);
					viewLeaveTxns.setAppNo(appNo);
					leaveCard.setTo(toDate);
					leaveCard.setFrom(fromDate);
					leaveCard.setAppNo(appNo);
					leaveCard.setEar(getLeavesEarnedinTheGivenYear(id,
							leaveTypeId, fromDate));
					leaveCard.setBal((noOfLeavesAv) - (worDays));
					leaveCard.setMax(getMaxEligibleinTheCurrentYear(id,
							leaveTypeId, fromDate));
					leaveCard.setAvailed(worDays.floatValue());
					leaveCard.setBalPreFy(getLeaveBalancesByempIDandTypeId(id,
							leaveTypeId));
					leaveCard.setSan(sanNo);
					leaveCard.setStatusId(statusId);
					designationMaster = designationMasterDAO
							.getDesignationMaster(designIdDays);
					viewLeaveTxns.setDesname(designationMaster
							.getDesignationName());
					boolean b = checkPayEligible(Integer.valueOf((int)appId.longValue()));
					if (b)
					{
						viewLeaveTxns.setPayElegible(Character.valueOf('Y'));
					}
					else
					{
						viewLeaveTxns.setPayElegible(Character.valueOf('N'));
					}
					listOfLeveTrnx.add(viewLeaveTxns);
					listOfLeaveCard.add(leaveCard);

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

	}

	public List getListOfLeaveTranx(Integer empId, CFinancialYear financialY) {
		
		//pass calendar year 
		String currnetCalYear=getCurrentYearId();
		CalendarYear calYear=getCalendarYearById(Long.valueOf(currnetCalYear));
		if(isLeaveCalendarBased())
		{
			if(empId!=null && calYear!=null)
				runBuLogic(empId, null,calYear,null);
		}
		else
		{
			if(empId!=null && financialY!=null)
				runBuLogic(empId, financialY,null,null);
		}
		return (List) listOfLeveTrnx;
	}

	public List getListOfLeaveCard(Integer empId, CFinancialYear financialY) {
		
		//pass calendar year 
		String currnetCalYear=getCurrentYearId();
		CalendarYear calYear=getCalendarYearById(Long.valueOf(currnetCalYear));
		if(isLeaveCalendarBased())
		{
			if(empId!=null && calYear!=null)
				runBuLogic(empId, null,calYear,EisConstants.STATUS_APPROVED);
		}
		else
		{
			if(empId!=null && financialY!=null)
				runBuLogic(empId, financialY,null,EisConstants.STATUS_APPROVED);
		}
		return (List) listOfLeaveCard;
	}

	public Map getMapOfLeaveType(Date date, PersonalInformation employee) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			LeaveApplicationDAO leaveApplicationDAO = LeaveDAOFactory
					.getDAOFactory().getLeaveApplicationDAO();
			LeaveApplication leaveApplication = null;
			if (employee != null)
				leaveApplication = leaveApplicationDAO
						.getLeaveApplicationByEmpAndDate(date, employee);
			if (leaveApplication != null) {
				map.put("Leave", leaveApplication.getId());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return map;

	}

	public Wdaysconstnts getWDconstant() {
		WdaysconstntsDAO wdaysconstntsDAO = LeaveDAOFactory.getDAOFactory()
				.getWdaysconstntsDAO();
		
		return wdaysconstntsDAO.getWdaysconstnts();

	}

	public Integer getActiveWdaysconstnts() {
		session = HibernateUtil.getCurrentSession();
		Integer id = null;
		try {

			Query qry = session
					.createQuery("select wc.id from Wdaysconstnts wc where wc.isactive = :isActive");
			qry.setCharacter("isActive", Character.valueOf('1'));
			if (qry.list() != null && !qry.list().isEmpty()) {
				Integer obj = null;
				for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
					obj = (Integer) iter.next();
					id = obj;
				}

			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return id;
	}

	public List getListByAppNoAndLeaveType(String applicationNumber,
			Integer leaveType, Integer empId) {
		List list = new ArrayList();
		session = HibernateUtil.getCurrentSession();

		try {
			String mainStr = "";
			mainStr = "SELECT la.id  from  LeaveApplication la,PersonalInformation pif WHERE pif.idPersonalInformation =la.employeeId.idPersonalInformation AND  pif.idPersonalInformation =:empId ";
			if (!leaveType.equals(0))
				mainStr += " and la.typeOfLeaveMstr = :leaveType ";
			if (applicationNumber != null && !applicationNumber.equals(""))
				mainStr += " and upper(trim(la.applicationNumber)) = :applicationNumber ";
			Query qry = session.createQuery(mainStr);

			qry.setInteger("empId", empId);
			if (!leaveType.equals(0)) {
				qry.setInteger("leaveType", leaveType);
			}
			if (applicationNumber != null && !applicationNumber.equals("")) {
				qry.setString("applicationNumber", applicationNumber);
			}

			for (Iterator iter = qry.iterate(); iter.hasNext();) {
				list.add(getLeaveApplicationById((Integer) iter.next()));
			}
		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return list;
	}

	public EmployeeAttendenceReport getEmployeeAttendenceReport(int month,PersonalInformation personalInformation,CFinancialYear cFinancialYear){
		EmployeeAttendenceReport employeeAttendenceReport = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		try {
			Map mpFYMap = EisManagersUtill.getFYMap();
		    Map mpcuFY = (Map) mpFYMap.get(cFinancialYear.getId());
			int cuyer =Integer.valueOf((String) mpcuFY.get(month));
			Integer noOfWdays = Integer.valueOf(0);
			Date fromDate;
			Date toDate;
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(cuyer, month - 1, 1);
			int noOfDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			fromDate = cal.getTime();
			if (Calendar.getInstance().get(Calendar.MONTH) + 1 == month	&& Calendar.getInstance().get(Calendar.YEAR) == cuyer) {				
				//noOfWdays = listOfWorkingDaysInMonthTillToDate(month,cFinancialYear).size();
				toDate = Calendar.getInstance().getTime();
			} else {
				//noOfWdays = listOfWorkingDaysInMonth(month, cFinancialYear).size();
				GregorianCalendar tocal = new GregorianCalendar();
				tocal.set(cuyer, month - 1, noOfDaysInMonth);
				toDate = tocal.getTime();
			}
			fromDate = sdf.parse(sdf.format(fromDate));
			toDate = sdf.parse(sdf.format(toDate));
			employeeAttendenceReport = getEmployeeAttendenceReportBetweenTwoDates(fromDate, toDate, personalInformation);
			employeeAttendenceReport.setNoOfWorkingDaysInMonth(employeeAttendenceReport.getNoOfWorkingDaysbetweenDates());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return employeeAttendenceReport;
	}

    /**
     *  This Api is used for paySlip and Reports
     * @param fromDate
     * @param toDate
     * @param personalInformation
     * @return employeeAttendenceReport
     */
	public EmployeeAttendenceReport getEmployeeAttendenceReportBetweenTwoDates(java.util.Date fromDate, java.util.Date toDate,PersonalInformation personalInformation) 
	{
		EmployeeAttendenceReport employeeAttendenceReport = new EmployeeAttendenceReport();
		float halfpre = 0;
		float fullpre = 0;
		Integer compOffs = Integer.valueOf(0);
		float paidLeaves = 0.0f;
		float unPaidLeaves = 0.0f;
		float absent = 0;
		float fltPaidDays = 0.0f;
		Integer intDaysInMonth =Integer.valueOf(0);
		Integer intDaysFromDateOfJoin = Integer.valueOf(0);
		Date myFromDate = fromDate;
		Date myToDate = toDate;		
		// If Employee DOA is greater than myToDate, then set all the values to 0.
		if(personalInformation.getDateOfFirstAppointment()!= null && personalInformation.getDateOfFirstAppointment().after(myToDate))
		{
			employeeAttendenceReport.setDaysInMonth(new Integer(0));
			employeeAttendenceReport.setNoOfDaysfromDateOfJoin(new Integer(0));
			employeeAttendenceReport.setNoOfAbsentInHolidays(new Float(0.0));
			employeeAttendenceReport.setNoOfAbsents(new Float(0.0));
			employeeAttendenceReport.setNoOfCompOff(new Integer(0));
			employeeAttendenceReport.setNoOfHalfPresents(new Integer(0));
			employeeAttendenceReport.setNoOfOverTime(new Integer(0));
			employeeAttendenceReport.setNoOfPaidDays(new Float(0.0));
			employeeAttendenceReport.setNoOfPaidleaves(new Float(0.0));
			employeeAttendenceReport.setNoOfPresents(new Float(0.0));
			employeeAttendenceReport.setNoOfUnPaidleaves(new Float(0.0));
			employeeAttendenceReport.setNoOfWorkingDaysbetweenDates(new Integer(0));
			employeeAttendenceReport.setNoOfWorkingDaysInMonth(new Integer(0));
		}
		else
		{
			/*
			 * if employee joined after the from date ie:02/072008 get working days
			 * from that joining date excluding holidays
			 */
			//Get working days in a Month
			employeeAttendenceReport.setDaysInMonth(listOfCalenderDays(myFromDate, myToDate));
			if (personalInformation!=null && personalInformation.getDateOfFirstAppointment()!= null && personalInformation.getDateOfFirstAppointment().after(myFromDate)) 
			{
				//set fromDate = DateOfFirstAppointment of the employee to get correct count of days for attendance and leave
				// excluding the days before joining date for a given month.			
				if(personalInformation.getDateOfFirstAppointment().before(myToDate))
					myFromDate=personalInformation.getDateOfFirstAppointment();			
				employeeAttendenceReport.setNoOfDaysfromDateOfJoin(listOfCalenderDays(myFromDate, myToDate));
			}
			if(personalInformation!=null &&
					personalInformation.getIdPersonalInformation()!=null && 
					personalInformation.getRetirementDate()!=null && (personalInformation.getRetirementDate().before(myToDate)))
					{				
						myToDate=personalInformation.getRetirementDate();
						employeeAttendenceReport.setNoOfDaysfromDateOfJoin(listOfCalenderDays(myFromDate, myToDate));				
					}		
			//Holidays for 2 different dates
			Set holidaySet=listOfHolidaysForTwoDates(myFromDate,myToDate);
			
			//Changed "Calling getNoOfWorkingDaysBweenTwoDatesForEmployee instead of getNoOfWorkingDaysBweenTwoDates to get the value from attendance table"
			employeeAttendenceReport.setNoOfWorkingDaysbetweenDates(getNoOfWorkingDaysBweenTwoDatesForEmployee(myFromDate, myToDate, personalInformation).size());
			employeeAttendenceReport.setNoOfCompOff(listOfDaysCompOffsForAnEmployeeForGivendates(myFromDate, myToDate, personalInformation).size());
			employeeAttendenceReport.setNoOfHalfPresents(listHalfPresentForAnEmployeebetweenTwoDates(myFromDate, myToDate, personalInformation).size());
			employeeAttendenceReport.setNoOfOverTime(listOfOvertimeForAnEmployeeBetweenTwoDates(myFromDate, myToDate, personalInformation).size());
			//Changed "Getting no of absent days by considering the Holiday attendence entry if available otherwise Ulb holiday set"
			absent = getTotalNoOfAbsentDaysForAnEmployeeByDates(myFromDate, myToDate, personalInformation, holidaySet);
			//absent = listOfDaysAbsentForAnEmployeeForGivendates(myFromDate,myToDate, personalInformation).size()+getNumberOfAbsentInHolidays(holidaySet,personalInformation);
	
			Map mp = listOfPaidDaysForAnEmployeeForGivenDates(myFromDate, myToDate,personalInformation);
			if (mp != null && !mp.isEmpty()) 
			{
				Set keySet = mp.keySet();
				for (Iterator iter = keySet.iterator(); iter.hasNext();) 
				{
					String leaveType = (String) iter.next();
					Integer attTypeCount = 0;
					if (leaveType.equals(EisConstants.PRESENT) || EisConstants.COMPOFF_ELIG.equals(leaveType)) 
					{
						attTypeCount = (Integer) mp.get(leaveType);
						fullpre = fullpre + 1.0f * attTypeCount.floatValue();
					} else if (leaveType.equals(EisConstants.HALFPRESENT)) 
					{
						attTypeCount = (Integer) mp.get(leaveType);
						halfpre = halfpre + 0.5f * attTypeCount.floatValue();
						absent = absent + 0.5f * attTypeCount.floatValue();
					}
					// Absent list is got seperately
					/*
					 * else if(paidOrUnPaid.equals(EisConstants.ABSENT)) {
					 * leaveCount = (Integer)mp.get(paidOrUnPaid); absent =
					 * absent + 1.0f*leaveCount.floatValue(); }
					 */
					
					//comment comp off as it is calculating from listHalfPresentForAnEmployeebetweenTwoDates api
					/*else if (leaveType.equals(EisConstants.COMP_OFF)) 
					{
						attTypeCount = (Integer) mp.get(leaveType);
						compOffs = compOffs + 1 * attTypeCount;
						logger.info("comp off inner loop>>>"+compOffs);
					}*/ 
					else if (leaveType.equals(EisConstants.LEAVE_PAID)|| leaveType.equals(EisConstants.TWOHALFLEAVE_PAID)) 
					{
						attTypeCount = (Integer) mp.get(leaveType);
						paidLeaves = paidLeaves + 1.0f* attTypeCount.floatValue();
					} else if (leaveType.equals(EisConstants.HALFLEAVE_PAID)) 
					{
						attTypeCount = (Integer) mp.get(leaveType);
						paidLeaves = paidLeaves + 0.5f* attTypeCount.floatValue();
						halfpre = halfpre + 0.5f * attTypeCount.floatValue();
					} else if (leaveType.equals(EisConstants.LEAVE_UNPAID)|| leaveType.equals(EisConstants.TWOHALFLEAVE_UNPAID)) 
					{
						attTypeCount = (Integer) mp.get(leaveType);
						unPaidLeaves = unPaidLeaves + 1.0f
								* attTypeCount.floatValue();
					} else if (leaveType.equals(EisConstants.HALFLEAVE_UNPAID)) 
					{
						attTypeCount = (Integer) mp.get(leaveType);
						unPaidLeaves = unPaidLeaves + 0.5f* attTypeCount.floatValue();
					}
				}
			}
			
			LOGGER.info("<<<<<<<<<<<<<<<< fullpre =" + fullpre);
			LOGGER.info("<<<<<<<<<<<<<<<< halfpre =" + halfpre);
			LOGGER.info("<<<<<<<<<<<<<<<< absent =" + absent);
			LOGGER.info("<<<<<<<<<<<<<<<< compOffs =" + compOffs);
			LOGGER.info("<<<<<<<<<<<<<<<< paidLeaves =" + paidLeaves);
			LOGGER.info("<<<<<<<<<<<<<<<< unPaidLeaves =" + unPaidLeaves);
			
			employeeAttendenceReport.setNoOfPresents(fullpre + halfpre);
			employeeAttendenceReport.setNoOfAbsents(absent);
			employeeAttendenceReport.setNoOfUnPaidleaves(unPaidLeaves);
			employeeAttendenceReport.setNoOfPaidleaves(paidLeaves);
			/*
			 * comment comp off from report
			 */
			//employeeAttendenceReport.setNoOfCompOff(compOffs);
			//set paid days
			if(employeeAttendenceReport.getDaysInMonth()!=null)
			{
				intDaysInMonth=employeeAttendenceReport.getDaysInMonth();
			}
			if(employeeAttendenceReport.getNoOfDaysfromDateOfJoin()!=null)
			{
				intDaysFromDateOfJoin=employeeAttendenceReport.getNoOfDaysfromDateOfJoin();
				fltPaidDays=intDaysFromDateOfJoin.floatValue()-absent-unPaidLeaves;
				if (mp != null && !mp.isEmpty()) 
				{
					Set keySet = mp.keySet();
					if (!keySet.contains(EisConstants.PRESENT)) 
					{
						employeeAttendenceReport.setNoOfPaidDays(Float.valueOf(0));
					} 
					else
					{
						employeeAttendenceReport.setNoOfPaidDays(fltPaidDays);
					}
				}
				else
				{
					employeeAttendenceReport.setNoOfPaidDays(Float.valueOf(0));
				}
				
			}
			else 
			{
				fltPaidDays=intDaysInMonth.floatValue()-absent-unPaidLeaves;
				if (mp != null && !mp.isEmpty()) 
				{
					
						Set keySet = mp.keySet();
						if (!keySet.contains(EisConstants.PRESENT)) 
							{
							employeeAttendenceReport.setNoOfPaidDays(Float.valueOf(0));
							} 
						else{
						
								employeeAttendenceReport.setNoOfPaidDays(fltPaidDays);
							}
				}			
				else
				{
					employeeAttendenceReport.setNoOfPaidDays(Float.valueOf(0));
				}
			}
		}
		return employeeAttendenceReport;
	}

	private Set listOfOvertimeForAnEmployeeBetweenTwoDates(Date fromDate,
			Date toDate, PersonalInformation personalInformation)
	{
		Set<String> wdList = new HashSet<String>();
		Date myFromDate = fromDate;
		Date myToDate = toDate;
		try {
			List objDateList = EisManagersUtill.getListOfFinYrandDate(myFromDate,
					myToDate);
			if (objDateList.size() == 1) {
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList
						.get(0);
				wdList = getOverTimeAttAcrossFinYear(myFromDate, myToDate,
						personalInformation, finYrandDateFirst.getFinancial());
			} else if (objDateList.size() == 2) 
			{
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList
						.get(0);
				DatePeriodFY finYrandDateSecond = (DatePeriodFY) objDateList
						.get(1);
				wdList.addAll((Collection) getOverTimeAttAcrossFinYear(
						new java.sql.Date(finYrandDateFirst.getFromDate()
								.getTime()), new java.sql.Date(
								finYrandDateFirst.getToDate().getTime()),
						personalInformation, finYrandDateFirst.getFinancial()));
				wdList
						.addAll((Collection) getOverTimeAttAcrossFinYear(
								new java.sql.Date(finYrandDateSecond
										.getFromDate().getTime()),
								new java.sql.Date(finYrandDateSecond
										.getToDate().getTime()),
								personalInformation, finYrandDateSecond
										.getFinancial()));
			}
		} catch (Exception e) {
			
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return wdList;
		
	}

	private Set<String> getOverTimeAttAcrossFinYear(Date fromDate, Date toDate,
			PersonalInformation personalInformation, CFinancialYear financial) {
		
		AttendenceDAO attendenceDAO = LeaveDAOFactory.getDAOFactory().getAttendenceDAO();
		Set<String> pdList = new HashSet<String>();
		SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
		
		
		for (Iterator iter = attendenceDAO
				.getListOfOverTimeForAnEmployeeBetweenDates(fromDate,
						toDate, personalInformation).iterator(); iter
				.hasNext();) {
			Attendence attendence = (Attendence) iter.next();
			LeaveApplicationDAO leaveApplicationDAO = LeaveDAOFactory
					.getDAOFactory().getLeaveApplicationDAO();
			LeaveApplication leaveApplication = null;
			pdList.add(sdf.format(attendence.getAttDate()));
		}
		return pdList;
	}

	private int listOfCalenderDays(Date fromDate, Date toDate) {
		int wDays = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
			if (sdf.format(fromDate).equals(sdf.format(toDate))) {
				wDays = 1;
			} 
			else {
				Calendar fromCalendar = Calendar.getInstance();
				fromCalendar.setTime(fromDate);
				Calendar toCalendar = Calendar.getInstance();
				toCalendar.setTime(toDate);
				int count = 0;
				do {
					count++;
					fromCalendar.add(Calendar.DATE, 1);
				} while (fromCalendar.getTime().getTime() < toCalendar
						.getTime().getTime()
						|| fromCalendar.getTime().getTime() == toCalendar
								.getTime().getTime());

				wDays = count;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return wDays;

	}

	public Attendence checkAttendenceByEmpAndDte(Integer empId,
			java.util.Date toDate) {
		AttendenceDAO adtendence = LeaveDAOFactory.getDAOFactory()
				.getAttendenceDAO();
		return adtendence.checkAttendenceByEmpAndDte(empId, toDate);
	}

	public List getListCompOffObjects(Integer empId) {
		PersonalInformation personalInformation = EisManagersUtill
				.getEmployeeService().getEmloyeeById(empId);
		StatusMasterDAO statusMasterDAO = new StatusMasterDAO();
		StatusMaster statusMaster = statusMasterDAO
				.getStatusMaster(org.egov.pims.utils.EisConstants.STATUS_REJECTED);
		List compList = getCompOffObj(personalInformation);
		try {
			if (compList != null && !compList.isEmpty()) {
				List compRemoveList = new ArrayList();
				for (Iterator iter = compList.iterator(); iter.hasNext();) {
					CompOff obj = (CompOff) iter.next();
					if (obj.getAttObj() != null)
						HibernateUtil.getCurrentSession().lock(obj.getAttObj(),
								LockMode.NONE);
					boolean b = getCuttOffDate(obj.getAttObj().getAttDate());
					if (b == false) {
						compRemoveList.add(obj);
						iter.remove();
					}

				}
				if (!compRemoveList.isEmpty()) {
					for (Iterator itr = compRemoveList.iterator(); itr
							.hasNext();) {
						CompOff compobj = (CompOff) itr.next();
						compobj.setStatus(statusMaster);
					}
				}
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return compList;
	}

	private List getCompOffObj(PersonalInformation personalInformation) {
		session = HibernateUtil.getCurrentSession();
		List<CompOff> compList = new ArrayList<CompOff>();
		try {
			Query qry = session
					.createQuery("SELECT CO.id from CompOff CO,Attendence att  where att.id = CO.attObj.id and att.employee =:personalInformation AND CO.status.name = :status  ");
			if (personalInformation != null) {
				qry.setEntity("personalInformation", personalInformation);
				qry.setString("status",
						org.egov.pims.utils.EisConstants.STATUS_APPLIED);

			}
			if (qry.list() != null && !qry.list().isEmpty()) {
				CompOff compOff = null;
				for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
					Long obj = (Long)iter.next();
					compOff = getCompOffById(obj.intValue());
					compList.add(compOff);
				}

			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}

		return compList;
	}

	private boolean getCuttOffDate(java.util.Date date) {
		boolean b = false;
		try {
			Calendar calendarcutoff = Calendar.getInstance();
			calendarcutoff.setTime(date);
			calendarcutoff.add(Calendar.DATE, 90);
			java.util.Date toDate = new java.util.Date();
			if (calendarcutoff.getTime().after(toDate)) {
				b = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return b;

	}

	public Map searchEmployeeForAttendence(Integer departmentId,
			Integer designationId, String code, String name, String searchAll,
			String finYear, String monthId, Integer funId,String fromSearchName,String toSearchName,
			Integer functionId,Integer employeeTypeId,Integer billId) {

		LOGGER.info("Inside searchEmployeeForAttendence ::::");
		LOGGER.info("departmentId=" + departmentId);
		LOGGER.info("designationId=" + designationId);
		LOGGER.info("code=" + code);
		LOGGER.info("name=" + name);
		LOGGER.info("searchAll=" + searchAll);
		LOGGER.info("funId=" + funId);
		LOGGER.info("fromSearchName=" + fromSearchName);
		LOGGER.info("toSearchName=" + toSearchName);
		LOGGER.info("Function= " +functionId);
		LOGGER.info("employeeTypeId= "+employeeTypeId);
		LOGGER.info("billId= "+billId);
		session = HibernateUtil.getCurrentSession();
		Map<Integer, Map> mp = new LinkedHashMap<Integer, Map>();
		List employeeList = new ArrayList();
		Map<PersonalInformation, Map> persVsAtt = null;
		try {

			String mainStrSql = "";
			String unionStrSql = "";
			String unionStrmainStr = "";
			String lastStrSql = "";
		/*	String desStr="";
			String desStrSql="";*/
			/**
			 * Get end date for a given month and selected financial year
			 */
			Map strEndDaysMap = null;
			int monthInt = -1;
			if (monthId != null && monthId != "")
				monthInt = (Integer.valueOf(monthId)).intValue();
			LOGGER.info("<<<<<<<<<<<< monthInt=" + monthInt);
			FinancialYearDAO finYearDAO = CommonsDaoFactory.getDAOFactory()
					.getFinancialYearDAO();
			CFinancialYear financialY = null;
			LOGGER.info("<<<<<<<<<<<< finYear=" + finYear);
			Long longFinYr = null;
			if (finYear != null && finYear != "")
				longFinYr = Long.valueOf(finYear);
			LOGGER.info("<<<<<<<<<<<< longFinYr=" + longFinYr);
			if (longFinYr != null)
				financialY = (CFinancialYear) finYearDAO.findById(longFinYr,
						false);
			LOGGER.info("<<<<<<<<<<<< financialY=" + financialY);
			if (monthInt != -1 && financialY != null)
				strEndDaysMap = EisManagersUtill.getStartingAndEndingDateForMonthAanFyer(monthInt,financialY);
			LOGGER.info("$$$$$$$$$$ strEndDaysMap=" + strEndDaysMap);
			java.sql.Date endDate = null;
			java.sql.Date startDate = null;
			//Considering for Start day of the month Instead of End day 
			//fix for to give attendance to employee in the last month of his assignment,if he has assignment (for say 10 days). 
			//NA from assignment next day onwards for the whole month.
			//getting map for startDate and endDate
			if (strEndDaysMap != null && !strEndDaysMap.isEmpty())
			{
				endDate = (java.sql.Date) strEndDaysMap.get("endDate");
				startDate = (java.sql.Date) strEndDaysMap.get("startDate");
				
			}
			LOGGER.info("$$$$$$$$$$ endDate=" + endDate);
            
			
			String mainStr = "SELECT * FROM (SELECT * FROM (SELECT ev.ISACTIVE,ev.STATUS,ev.Code ,ev.name,ev.id as emp,ev.DESIGNATIONID,ev.DEPT_ID,ev.POS_ID,ev.PRD_ID,ev.ASS_ID,ev.FUNCTIONARY_ID ,ev.FUNCTION_ID ,ev.EMPLOYEE_TYPE,desig.DESIGNATION_NAME FROM EG_EIS_EMPLOYEEINFO ev,EG_DESIGNATION desig";
			unionStrmainStr = "SELECT sl.*,0,SYSDATE,0,0,0,0 FROM (SELECT * FROM (SELECT ev.ISACTIVE,ev.STATUS,ev.Code ,ev.name,ev.id as emp,ev.DESIGNATIONID,ev.DEPT_ID,ev.POS_ID,ev.PRD_ID,ev.ASS_ID,ev.FUNCTIONARY_ID ,ev.FUNCTION_ID ,ev.EMPLOYEE_TYPE,desig.DESIGNATION_NAME FROM EG_EIS_EMPLOYEEINFO ev,EG_DESIGNATION desig";
			//desStr="SELECT ed.DESIGNATIONID,ed.DEPTID,ed.DESIGNATION_NAME,0,0,0,0,0,0,0,0,0,0 from EG_DESIGNATION ed,EG_EIS_EMPLOYEEINFO ev";
			
			if (("false".equals(searchAll) && designationId.intValue() != 0)) {
				mainStr += " where ev.DESIGNATIONID=desig.DESIGNATIONID and ev.is_primary='Y' and ev.DATE_OF_FA<= :endDate and (((ev.TO_DATE IS NULL AND ev.from_date <= :endDate ) " +
						"OR (ev.from_date <= :endDate AND ev.TO_DATE >= :endDate) or (ev.from_date <= :endDate AND ev.TO_DATE <= :endDate))))) sl , " +
						"EGEIS_ATTENDENCE att WHERE sl.emp = att.EMP_ID(+) and sl.ISACTIVE = '1' and sl.status=(SELECT id FROM  egw_status es " +
						"WHERE es.DESCRIPTION='"+EisConstants.STATUS_TYPE_EMPLOYED+"' and es.MODULETYPE='"+EisConstants.STATUS_MODULE_TYPE+" " +")";
				unionStrmainStr += " where ev.DESIGNATIONID=desig.DESIGNATIONID and ev.is_primary='Y' and (((ev.TO_DATE IS NULL AND ev.from_date <= :endDate ) OR (ev.from_date <=" +
						" :endDate AND ev.TO_DATE >= :endDate)or (ev.from_date <= :endDate AND ev.TO_DATE <= :endDate))))) " +
						"sl WHERE sl.ISACTIVE = '1' and sl.status=(SELECT id FROM  egw_status es WHERE es.DESCRIPTION='" +
						""+EisConstants.STATUS_TYPE_EMPLOYED+"' and es.MODULETYPE='"+EisConstants.STATUS_MODULE_TYPE+" " +
								"') ";
				//desStr+="where ev.DESIGNATIONID=ed.DESIGNATIONID)";
			} else if (("true".equals(searchAll) && designationId.intValue() != 0)) {
				mainStr += " where ev.DESIGNATIONID=desig.DESIGNATIONID and ev.is_primary='Y' and ev.DATE_OF_FA<= :endDate and (((ev.TO_DATE IS NULL AND ev.from_date <= :endDate ) OR (ev.from_date <= :endDate AND ev.TO_DATE >= :endDate)or (ev.from_date <= :endDate AND ev.TO_DATE <= :endDate))))) sl , EGEIS_ATTENDENCE att WHERE sl.emp = att.EMP_ID(+) and sl.status=(SELECT id FROM  egw_status es WHERE es.DESCRIPTION='"+EisConstants.STATUS_TYPE_EMPLOYED+"' and es.MODULETYPE='"+EisConstants.STATUS_MODULE_TYPE+" and ev.DESIGNATIONID=ed.DESIGNATIONID') ";
				unionStrmainStr += " where ev.DESIGNATIONID=desig.DESIGNATIONID and ev.is_primary='Y' and ev.DATE_OF_FA<= :endDate and (((ev.TO_DATE IS NULL AND ev.from_date <= :endDate ) OR (ev.from_date <= :endDate AND ev.TO_DATE >= :endDate))))) sl WHERE sl.status=(SELECT id FROM  egw_status es WHERE es.DESCRIPTION='"+EisConstants.STATUS_TYPE_EMPLOYED+"' and es.MODULETYPE='"+EisConstants.STATUS_MODULE_TYPE+" and ev.DESIGNATIONID=ed.DESIGNATIONID') ";
				//desStr+="where ev.DESIGNATIONID=ed.DESIGNATIONID)";
			} else if ("true".equals(searchAll)
					&& designationId.intValue() == 0) {

				mainStrSql += " select * from (select * from (SELECT ev.Code ,ev.STATUS ,ev.name,ev.id  as emp ,ev.DESIGNATIONID,ev.DEPT_ID,ev.POS_ID,ev.PRD_ID,ev.ASS_ID,ev.FUNCTIONARY_ID,desig.DESIGNATION_NAME FROM EG_EIS_EMPLOYEEINFO ev,EG_DESIGNATION desig where ev.DESIGNATIONID=desig.DESIGNATIONID and ev.is_primary='Y' and ev.DATE_OF_FA<= :endDate and (((ev.TO_DATE IS NULL AND ev.from_date <= :endDate ) OR (ev.from_date <= :endDate AND ev.TO_DATE >= :endDate)))"
						+ " UNION( SELECT  ev.Code ,ev.STATUS,ev.name,ev.id as emp,ev.DESIGNATIONID,ev.DEPT_ID,ev.PRD_ID,ev.ASS_ID,ev.FUNCTIONARY_ID  FROM EG_EIS_EMPLOYEEINFO ev where ev.is_primary='Y' and ev.from_date IN (SELECT MAX(evn.from_date) FROM EG_EIS_EMPLOYEEINFO "
						+ " evn GROUP BY evn.id))))  sl , EGEIS_ATTENDENCE att WHERE sl.emp = att.EMP_ID(+) and sl.status=(SELECT id FROM  egw_status es WHERE es.DESCRIPTION='"+EisConstants.STATUS_TYPE_EMPLOYED+"' and es.MODULETYPE='"+EisConstants.STATUS_MODULE_TYPE+"')";
				unionStrSql += " select sl.*,0,SYSDATE,0,0,0,0 from (select * from (SELECT ev.Code ,ev.STATUS,ev.name,ev.id  as emp ,ev.DESIGNATIONID,ev.DEPT_ID,ev.POS_ID,ev.PRD_ID,ev.ASS_ID,ev.FUNCTIONARY_ID,desig.DESIGNATION_NAME FROM EG_EIS_EMPLOYEEINFO ev,EG_DESIGNATION desig where ev.DESIGNATIONID=desig.DESIGNATIONID and  ev.is_primary='Y' and ev.DATE_OF_FA<= :endDate and (((ev.TO_DATE IS NULL AND ev.from_date <= :endDate ) OR (ev.from_date <= :endDate AND ev.TO_DATE >= :endDate)))"
						+ " UNION( SELECT  ev.Code ,ev.STATUS,ev.name,ev.id as emp,ev.DESIGNATIONID,ev.DEPT_ID,ev.PRD_ID,ev.ASS_ID,ev.FUNCTIONARY_ID  FROM EG_EIS_EMPLOYEEINFO ev where ev.is_primary='Y' and ev.from_date IN (SELECT MAX(evn.from_date) FROM EG_EIS_EMPLOYEEINFO "
						+ " evn GROUP BY evn.id))))  sl WHERE  sl.status=(SELECT id FROM  egw_status es WHERE es.DESCRIPTION='"+EisConstants.STATUS_TYPE_EMPLOYED+"' and es.MODULETYPE='"+EisConstants.STATUS_MODULE_TYPE+"') ";
              //desStrSql+="SELECT ed.DESIGNATIONID,ed.DEPTID,ed.DESIGNATION_NAME from EG_DESIGNATION ed,EG_EIS_EMPLOYEEINFO ev WHERE ev.DESIGNATIONID=ed.DESIGNATIONID";
				if (code != null && !code.equals("")) {
					mainStrSql += " and upper(trim(Code)) = :employeeCode ";
					unionStrSql += " and upper(trim(Code)) = :employeeCode ";
					
			
				}

				if (departmentId.intValue() != 0) {
					mainStrSql += " and DEPT_ID = :deptId ";
					unionStrSql += " and DEPT_ID = :deptId ";
				

				}
				if (billId.intValue() != 0) {
					mainStrSql += " and POS_ID in(select ID from EG_POSITION where ID_BILLNUMBER= :billId)  ";
					unionStrSql += " and POS_ID in(select ID from EG_POSITION where ID_BILLNUMBER= :billId) ";
					
					
				}
				
				if (functionId.intValue() != 0) {
					mainStrSql += " and FUNCTION_ID = :functionId ";
					unionStrSql += " and FUNCTION_ID = :functionId ";
					

				}
				
				if (employeeTypeId.intValue() != 0) {
					mainStrSql += " and EMPLOYEE_TYPE = :employeeTypeId ";
					unionStrSql += " and EMPLOYEE_TYPE = :employeeTypeId ";
					

				}
				
				if (name != null && !name.equals("")) {
					mainStrSql += " and trim(upper(name)) like '%"
							+ name.trim().toUpperCase() + "%'";
					unionStrSql += " and trim(upper(name)) like '%"
							+ name.trim().toUpperCase() + "%'";
					
				}
				if (monthId != null && !monthId.equals("")) {

					mainStrSql += " and (att.MONTH = :monthId or att.MONTH is null)";
				}
				if (finYear != null && !finYear.equals("")) {

					mainStrSql += " and (att.FIN_YEAR_ID  = :finYear or att.FIN_YEAR_ID  is null)";
				}
				if (funId.intValue() != 0) {
					mainStrSql += " and FUNCTIONARY_ID = :funId ";
					unionStrSql += " and FUNCTIONARY_ID = :funId ";
					

				}
				unionStrSql +=" and sl.emp not in(select emp_id from EGEIS_ATTENDENCE  where month=:monthId and FIN_YEAR_ID = :finYear)  ";
				lastStrSql = "select * from ( " + mainStrSql + ") union ( "
						+ unionStrSql + " )";
			} else {
				mainStr += " where ev.DESIGNATIONID=desig.DESIGNATIONID and ev.is_primary='Y' and ev.DATE_OF_FA<= :endDate and (((ev.TO_DATE IS NULL AND ev.from_date <= :endDate ) OR (ev.from_date <= :endDate AND ev.TO_DATE >= :startDate))))) sl , EGEIS_ATTENDENCE att WHERE sl.emp = att.EMP_ID(+) and sl.status=(SELECT id FROM  egw_status es WHERE es.DESCRIPTION='"+EisConstants.STATUS_TYPE_EMPLOYED+"' and es.MODULETYPE='"+EisConstants.STATUS_MODULE_TYPE+"')";
				unionStrmainStr += " where ev.DESIGNATIONID=desig.DESIGNATIONID and ev.is_primary='Y' and ev.DATE_OF_FA<= :endDate and (((ev.TO_DATE IS NULL AND ev.from_date <= :endDate ) OR (ev.from_date <= :endDate AND ev.TO_DATE >= :startDate))))) sl WHERE sl.status=(SELECT id FROM  egw_status es WHERE es.DESCRIPTION='"+EisConstants.STATUS_TYPE_EMPLOYED+"' and es.MODULETYPE='"+EisConstants.STATUS_MODULE_TYPE+"')";
                //desStr+= " where ev.DESIGNATIONID=ed.DESIGNATIONID ";
			}
			if (code != null && !code.equals("")) {
				mainStr += " and upper(trim(Code)) = :employeeCode ";
				unionStrmainStr += " and upper(trim(Code)) = :employeeCode ";
				
			}
			if (departmentId.intValue() != 0) {
				mainStr += " and DEPT_ID = :deptId ";
				unionStrmainStr += " and DEPT_ID = :deptId ";
				
			}
			if (billId.intValue() != 0) {
				mainStr += " and POS_ID in(select ID from EG_POSITION where ID_BILLNUMBER= :billId)  ";
				unionStrmainStr += " and POS_ID in(select ID from EG_POSITION where ID_BILLNUMBER= :billId) ";
				
			}
			
			if (functionId.intValue() != 0) {
				mainStr += " and FUNCTION_ID = :functionId ";
				unionStrmainStr += " and FUNCTION_ID = :functionId ";
				

			}
			
			if (employeeTypeId.intValue() != 0) {
				mainStr += " and EMPLOYEE_TYPE = :employeeTypeId ";
				unionStrmainStr += " and EMPLOYEE_TYPE = :employeeTypeId ";
				

			}
			
			if(fromSearchName!=null && !fromSearchName.equals("0") && toSearchName!=null && !toSearchName.equals("0"))
			{
				mainStr += "and (upper(name) between '"+fromSearchName.trim().toUpperCase()+"' and '"+toSearchName.trim().toUpperCase()+"' or upper(name) like '"+toSearchName.trim().toUpperCase()+"%')";
				unionStrmainStr +=  "and (upper(name) between '"+fromSearchName.trim().toUpperCase()+"' and '"+toSearchName.trim().toUpperCase()+"' or upper(name) like '"+toSearchName.trim().toUpperCase()+"%')";
				
			}

			if (name != null && !name.equals("")) {
				mainStr += " and trim(upper(name)) like '%"
						+ name.trim().toUpperCase() + "%'";
				unionStrmainStr += " and trim(upper(name)) like '%"
						+ name.trim().toUpperCase() + "%'";
		
			}
			if (designationId.intValue() != 0) {
				mainStr += " and  DESIGNATIONID= :designationId ";
				unionStrmainStr += " and  DESIGNATIONID= :designationId ";

			}
			if (funId.intValue() != 0) {
				mainStr += " and FUNCTIONARY_ID = :funId ";
				unionStrmainStr += " and FUNCTIONARY_ID = :funId ";

			}
			if (monthId != null && !monthId.equals(""))
				mainStr += " and (att.MONTH = :monthId or att.MONTH is null)";
			if (finYear != null && !finYear.equals(""))
				mainStr += " and (att.FIN_YEAR_ID  = :finYear or att.FIN_YEAR_ID  is null)";
			unionStrmainStr+=" and sl.emp not in(select emp_id from EGEIS_ATTENDENCE  where month=:monthId and FIN_YEAR_ID = :finYear)  ";
			//desStr+=" and  ev.DESIGNATIONID=ed.DESIGNATIONID";
			String lastmainStr = "select * from (( " + mainStr + ") union ( "
					+ unionStrmainStr + " ))order by DESIGNATION_NAME";
			Query qry = null;

			// get the attendance object itself instead of getting columns.
			// All these values are obtained from attendance object itself.
			if (searchAll.equals("true") && designationId.intValue() == 0) {
				qry = session.createSQLQuery(lastStrSql).addScalar("EMP",
						IntegerType.INSTANCE).addEntity("Attendence",
						Attendence.class);
			} else {
				qry = session.createSQLQuery(lastmainStr).addScalar("EMP",
						IntegerType.INSTANCE).addEntity("Attendence",
						Attendence.class);
			}

			LOGGER.info("qryqryqryqryqryqryqry" + qry.toString());

			if (endDate != null)
			{
				qry.setDate("endDate", endDate);
			}
			if (startDate!=null)
			{
				qry.setDate("startDate", startDate);
			}
			if (code != null && !code.equals("")) {
				qry.setString("employeeCode", code.toUpperCase());

			}
			if (departmentId.intValue() != 0) {
				qry.setInteger("deptId", departmentId);

			}
			if (billId.intValue() != 0) {
				qry.setInteger("billId", billId);

			}
			if(functionId.intValue()!=0)
			{
				qry.setInteger("functionId",functionId);
			}
			
			if(employeeTypeId.intValue()!=0)
			{
				qry.setInteger("employeeTypeId", employeeTypeId);
			}
			if (designationId.intValue() != 0) {
				qry.setInteger("designationId", designationId);

			}
			if (monthId != null && !monthId.equals("")) {
				qry.setInteger("monthId", Integer.valueOf(monthId));

			}
			if (finYear != null && !monthId.equals("")) {
				qry.setLong("finYear", Integer.valueOf(finYear));

			}
			if (funId.intValue() != 0) {
				qry.setInteger("funId", funId);

			}
			//employeeList = qry.list();
			LinkedHashSet empSet = new LinkedHashSet(qry.list());
			//LOGGER.info("empSet="+empSet);
			employeeList = new ArrayList(empSet);
			Map<Integer, Map> secondaryMap = new HashMap<Integer, Map>();
			List empIdsList = new ArrayList();
			if (employeeList != null && !employeeList.isEmpty()) {
				for (Iterator iter = employeeList.iterator(); iter.hasNext();) {
					Object[] objArray = (Object[]) iter.next();
					addListAttEmployee(objArray, mp, secondaryMap, monthId,
							finYear);
					if (objArray != null && objArray[0] != null) {
						Integer empid = (Integer) objArray[0];
						//LOGGER.info("empid="+empid);
						if (empid != null && !empIdsList.contains(empid))
							empIdsList.add(empid);
					}
				}
			}
			List list = null;
			//List list=new ArrayList(distinctResults);
			if (empIdsList != null && !empIdsList.isEmpty()) {
				list = EisManagersUtill.getEmployeeService()
						.getListOfPersonalInformationByEmpIdsList(empIdsList);
			}
			if (list != null && !list.isEmpty()) {
				persVsAtt = getMapOfPersonalInfoVsAtt(list, mp);
			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return persVsAtt;

	}

	/**
	 * This method creates a map of empID Vs (day Vs Attendance) for each
	 * employee
	 * 
	 * @param attendence
	 * @param finalMap
	 * @param secondary
	 * @param monthId
	 * @param finYear
	 */
	private void addListAttEmployee(Object[] objArray, Map finalMap,
			Map secondary, String monthId, String finYear) {
		try {
			int len = objArray.length;
			Integer empid = Integer.valueOf(0);
			Integer day = Integer.valueOf(0);
			Integer mon = Integer.valueOf(0);
			Integer fin = Integer.valueOf(0);
			Attendence attendence = null;
			Map mySecondary = secondary;
			for (int i = 0; i < len; i++) {
				if (i == 0) {
					empid = (Integer) objArray[i];
				}
				if (i == 1) {
					if (objArray[i] != null) {
						attendence = (Attendence) objArray[i];
					}
					if (attendence != null && attendence.getId() != null) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy/MM/dd",Locale.getDefault());
						String dat = null;
						if (attendence.getAttDate() != null)
							dat = sdf.format(attendence.getAttDate());
						if (dat != null)
							day = Integer.valueOf(dat.substring(8));
						mon = (Integer) attendence.getMonth();
						if (attendence.getFinancialId() != null
								&& attendence.getFinancialId().getId() != null)
							fin = Integer.valueOf(attendence.getFinancialId()
									.getId().intValue());
					} else
						attendence = new Attendence();
				}
				if (empid != null) {
					if (finalMap.containsKey(empid)) {
						if (((Map) finalMap.get(empid)).get(day) == null) {
							if (mon.equals(Integer.valueOf(monthId.trim()))
									&& fin.equals(Integer.valueOf(finYear.trim()))) {
								((Map) finalMap.get(empid))
										.put(day, attendence);
							}
						}
					} else {
						mySecondary = new HashMap();
						if (mon.equals(Integer.valueOf(monthId.trim()))
								&& fin.equals(Integer.valueOf(finYear.trim()))) {
							mySecondary.put(day, attendence);
						}
						finalMap.put(empid, mySecondary);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	private Map getMapOfPersonalInfoVsAtt(List list, Map mp) 
	{
		Map persVsAtt = null;
		if (list != null && mp != null) 
		{
			persVsAtt = new LinkedHashMap(); 
			/*for (Iterator itr = list.iterator(); itr.hasNext();) {
				PersonalInformation persInfo = (PersonalInformation) itr.next();*/
				for (Iterator itr2 = mp.keySet().iterator(); itr2.hasNext();) 
				{
					Integer empId = (Integer) itr2.next();
					/*LOGGER.info("------------------------------------------");
					LOGGER.info("empid111="+empId);*/
					for (Iterator itr = list.iterator(); itr.hasNext();) 
					{
						PersonalInformation persInfo = (PersonalInformation) itr.next();
						if (persInfo.getIdPersonalInformation().intValue() == empId.intValue()) 
						{
							persVsAtt.put(persInfo, mp.get(empId));
						}
					}
				}
			}
		return persVsAtt;
	}

	private List searchEmployeeForAtt(Integer departmentId,
			Integer designationId, String code, String name, String searchAll,
			Integer fun) throws Exception {
		session = HibernateUtil.getCurrentSession();
		List<EmployeeView> employeeList = null;
		try {
			LOGGER.info("Inside searchEmployeeForAtt ::::");
			LOGGER.info("departmentId=" + departmentId);
			LOGGER.info("designationId=" + designationId);
			LOGGER.info("code=" + code);
			LOGGER.info("name=" + name);
			LOGGER.info("searchAll=" + searchAll);
			LOGGER.info("fun=" + fun);
			//(SELECT es.id FROM  egw_status es WHERE es.DESCRIPTION = :EisConstants.STATUS_TYPE_EMPLOYED and 
			//es.MODULETYPE='"+EisConstants.STATUS_MODULE_TYPE+"')
			String mainStr = "from EmployeeView ev WHERE isPrimary='Y' and ((SYSDATE BETWEEN ev.fromDate AND ev.toDate ) " +
							 " OR (ev.fromDate <SYSDATE AND ev.toDate is null ) OR" +
							 " ( ev.toDate = (SELECT MAX(e.toDate)  FROM EmployeeView e WHERE NOT EXISTS " +
							 "(SELECT e1.id FROM EmployeeView e1 WHERE SYSDATE BETWEEN e1.fromDate AND e1.toDate AND " +
							 "e1.id = ev.id)AND e.toDate < SYSDATE AND e.id =ev.id ))) and " +
							 "ev.employeeStatus.description = :description and " +
							 "ev.employeeStatus.moduletype = :moduleType";
			if (code != null && !code.equals(""))
				mainStr += " and upper(trim(ev.employeeCode)) = :employeeCode ";
			if (departmentId.intValue() != 0)
				mainStr += " and   ev.deptId.id= :deptId";
			if (designationId.intValue() != 0)
				mainStr += " and ev.desigId.designationId = :designationId ";
			if (name != null && !name.equals("")) {
				mainStr += " and trim(upper(ev.employeeName))  like '%"
						+ name.trim().toUpperCase() + "%'";
			}
			if (fun.intValue() != 0) {
				mainStr += " and ev.functionary.id = :fun";
			}
			Query qry = null;
			qry = session.createQuery(mainStr);
			LOGGER.info("qryqryqryqry" + qry.toString());			
			qry.setString("description", EisConstants.STATUS_TYPE_EMPLOYED);
			qry.setString("moduleType", EisConstants.STATUS_MODULE_TYPE);
			if (code != null && !code.equals("")) {
				qry.setString("employeeCode", code.toUpperCase());
			}
			if (departmentId.intValue() != 0) {
				qry.setInteger("deptId", departmentId);
			}
			if (designationId.intValue() != 0) {
				qry.setInteger("designationId", designationId);
			}
			if (fun.intValue() != 0) {
				qry.setInteger("fun", fun);

			}
			employeeList = (List) qry.list();
			LOGGER.info("qryqryqryqry" + qry.toString());

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return employeeList;
	}

	public List<EmployeeAttendenceReport> searchEmployeeForAttRept(Integer designationId,
			 String code,
			String name, String searchAll, String month, String finYear,Map<String,Integer> finParams
			) {
		ArrayList<EmployeeAttendenceReport> dataElCol = new ArrayList<EmployeeAttendenceReport>();		
		try {
			
			Integer departmentId=finParams.get("departmentId")==null?0:finParams.get("departmentId");
			Integer functionaryId=finParams.get("functionaryId")==null?0:finParams.get("functionaryId");
			Integer functionId=finParams.get("functionId")==null?0:finParams.get("functionId");
			
			CFinancialYear financialYear = EisManagersUtill.getCommonsService().findFinancialYearById(Long.valueOf(finYear));
			List employeeList = searchEmployeeForAtt(departmentId,designationId, code, name, searchAll, functionId);
			//FIXME : This employeeList has multiple rows for same employee with different assignment periods.			
			EmployeeView employeeView = null;
			LOGGER.info("kkkkkkkkkkkkkkkkkk" + employeeList);
			for (Iterator iter = employeeList.iterator(); iter.hasNext();) 
			{
				employeeView = (EmployeeView) iter.next();
				EmployeeAttendenceReport employeeAttendenceReport = null;
				if (month != null && !month.equals("")) 
				{
					employeeAttendenceReport = getEmployeeAttendenceReport(Integer.valueOf(month), EisManagersUtill.getEmployeeService().getEmloyeeById(employeeView.getId()),financialYear);
					employeeAttendenceReport.setEmployeeId(employeeView.getId());
					LOGGER.info("kkkkkkkkkkkkkkkkkk" + employeeView.getId());
					dataElCol.add(employeeAttendenceReport);
				}

			}
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		}
		return dataElCol;

	}

	public boolean checkLeaveReportsForAnEmployee(Integer empId) {
		boolean b = false;
		try {
			LeaveApplicationDAO leaveApplicationDAO = LeaveDAOFactory
					.getDAOFactory().getLeaveApplicationDAO();
			b = leaveApplicationDAO.checkLeaveReportsForAnEmployee(empId);
		} catch (Exception e) {
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return b;

	}

	public boolean checkLeave(Integer empId) {
		boolean b = false;
		try {
			LeaveApplicationDAO leaveApplicationDAO = LeaveDAOFactory
					.getDAOFactory().getLeaveApplicationDAO();
			b = leaveApplicationDAO.checkLeaveForAnEmployee(empId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return b;

	}

	public Integer getNextValForAttId() {

		session = HibernateUtil.getCurrentSession();
		Integer id = Integer.valueOf(0);
		try {
			Query qry = session.createSQLQuery(
					"SELECT SEQ_ATTENDENCE.nextval as id from dual").addScalar(
					"id", IntegerType.INSTANCE);
			if (qry.list() != null && !qry.list().isEmpty()) {
				Integer obj = null;
				for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
					obj = (Integer) iter.next();
					id = obj;
				}

			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return id;
	}

	public Integer getNextValForCompOffId() {

		session = HibernateUtil.getCurrentSession();
		Integer id =Integer.valueOf(0);
		try {
			Query qry = session.createSQLQuery(
					"SELECT SEQ_COMPOFF.nextval as id from dual").addScalar(
					"id", IntegerType.INSTANCE);
			if (qry.list() != null && !qry.list().isEmpty()) {
				Integer obj = null;
				for (Iterator iter = qry.list().iterator(); iter.hasNext();) {
					obj = (Integer) iter.next();
					id = obj;
				}

			}

		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			
			//HibernateUtil.rollbackTransaction();

			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return id;
	}

	public LeaveOpeningBalance getLeaveOpeningBalanceByEmpID(Integer empId,
			Integer leaveType) {
		LeaveOpeningBalanceDAO lvOpeningDAO = LeaveDAOFactory.getDAOFactory()
				.getLeaveOpeningBalanceDAO();
		return lvOpeningDAO.getLeaveOpeningBalanceByEmpID(empId, leaveType);
	}

	public HolidaysUlb getHolidayById(Integer holidayId) {
		HolidaysUlb holiday = null;
		try {
			HolidaysUlbDAO holidayDAO = LeaveDAOFactory.getDAOFactory()
					.getHolidaysUlbDAO();
			holiday = holidayDAO.getHolidaysUlbByID(holidayId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return holiday;
	}

	public void deleteHoliday(HolidaysUlb holiday) {
		try {
			HolidaysUlbDAO holidayDAO = LeaveDAOFactory.getDAOFactory()
					.getHolidaysUlbDAO();
			holidayDAO.delete(holiday);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public List<LeaveApplication> getEncashmentLeaveApplicationByStatus(
			String statusName) throws Exception {
		try {
			LeaveApplicationDAO leaveDAO = LeaveDAOFactory.getDAOFactory()
					.getLeaveApplicationDAO();
			return leaveDAO.getEncashmentLeaveApplicationByStatus(statusName);
		} catch (Exception e) {
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	public List<LeaveApplication> getLeaveApplicationByEmpStatus(Integer empId,
			StatusMaster status) throws Exception {
		try {
			LeaveApplicationDAO leaveDAO = LeaveDAOFactory.getDAOFactory()
					.getLeaveApplicationDAO();
			return leaveDAO.getLeaveApplicationByEmpStatus(empId, status);
		} catch (Exception e) {
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}
	//Add java doc comments
	
	/**
	 * searchEmployeeForLeaveOpeningBalance is a method to seach Leave 
	 * open balance 
	 * @param departmentId,designationId,code,name,finYear,TypeOfLeaveMstr
	 * @return List of Employee and Leave Availables
	 * @author Divya
	 */
	public List searchEmployeeForLeaveOpeningBalance(Integer departmentId,
			Integer designationId, String code, String name, Integer finYear,
			Integer TypeOfLeaveMstr) throws Exception {
		session = HibernateUtil.getCurrentSession();
	
		List<EmployeeView> employeeList = null;
		List leaveOpenBalanceList = null;
		List list = null;
		
		try {
			/*
			 * query to obtain all the employee based on desgID or code or name
			 * or deptId
			 */
			String mainStr = "from EmployeeView ev WHERE ev.isPrimary='Y' and ((SYSDATE BETWEEN ev.fromDate AND ev.toDate ) OR( ev.toDate = (SELECT MAX(e.toDate) FROM EmployeeView e WHERE NOT EXISTS (SELECT e1.id FROM EmployeeView e1 WHERE SYSDATE BETWEEN e1.fromDate AND e1.toDate AND e1.id = ev.id)AND e.toDate < SYSDATE AND e.id =ev.id )))";
			if(code!=null&&!code.equals(""))
				mainStr +=" and upper(trim(ev.employeeCode)) = :employeeCode";		
			if(departmentId.intValue() != 0)
				mainStr +=" and ev.deptId.id= :deptId ";
			if(designationId.intValue() != 0)
				mainStr += " and ev.desigId.designationId = :designationId ";
			if(name!= null && !name.equals(""))
			{
				mainStr += " and trim(upper(ev.employeeName))  like '%"+name.trim().toUpperCase()+"%'";
			}
			Query qry = null;
			qry = session.createQuery(mainStr);
			
			if (code != null && !code.equals("")) {
				qry.setString("employeeCode", code.toUpperCase());
			}
			if (departmentId.intValue() != 0) {
				qry.setInteger("deptId", departmentId);
			}
			if (designationId.intValue() != 0) {
				qry.setInteger("designationId", designationId);
			}
			employeeList = (List) qry.list();
			List <PersonalInformation> listFinal = new ArrayList();
			// new changes
			//query changed to avoid multiple row for Leave Opening balance 
			if(employeeList!=null && !employeeList.isEmpty())
			{
				String leaveStrtemp = "from LeaveOpeningBalance lob where lob.employeeId.idPersonalInformation in(:employeeIdList) ";
				
				if (finYear.intValue() != 0) {
					leaveStrtemp += " and lob.financialId.id = :finYear  ";
				}
				
				if (TypeOfLeaveMstr.intValue() != 0) {
					leaveStrtemp += " and lob.typeOfLeaveMstr.id = :TypeOfLeaveMstr";
				}
				Query query = null;
				query = session.createQuery(leaveStrtemp);
				
				if(employeeList.size()<=1000)
				{
					if (finYear.intValue() != 0) {
						query.setInteger("finYear", finYear);
		
					}
					if (TypeOfLeaveMstr.intValue() != 0) {
						query.setInteger("TypeOfLeaveMstr", TypeOfLeaveMstr);
		
					}	
					Iterator iter = employeeList.iterator();
					List employeeIdList=new ArrayList();
					while (iter.hasNext()) {
						EmployeeView emp = (EmployeeView) iter.next();
						employeeIdList.add(emp.getId());
					}
					query.setParameterList("employeeIdList", employeeIdList);
				}
				else
				{
					if (finYear.intValue() != 0) {
						query.setInteger("finYear", finYear);
		
					}
					if (TypeOfLeaveMstr.intValue() != 0) {
						query.setInteger("TypeOfLeaveMstr", TypeOfLeaveMstr);
		
					}		
	//				If it exceeds 1000, get the list for each 1000 employees iteratively.
					Iterator iter = employeeList.iterator();
					List employeeIdList=new ArrayList();
					while (iter.hasNext()) {
						EmployeeView emp = (EmployeeView) iter.next();
						employeeIdList.add(emp.getId());
					}
					int noOfSublists = employeeIdList.size()/1000;
					int remainingItems= employeeIdList.size() % 1000;		
					int initialVal=0;					
					for(int i=1; i<=noOfSublists; i++)
					{				
						query.setParameterList("employeeIdList", employeeIdList.subList(initialVal,i*1000));		
						leaveOpenBalanceList = query.list();
						listFinal.addAll(leaveOpenBalanceList);
						initialVal=i*1000;
					}
					query.setParameterList("employeeIdList", employeeIdList.subList(initialVal,initialVal+remainingItems));		
						
				}	
						
				listFinal.addAll((List)query.list());
			}
			list=getEmployeesWithLeaveOpenBal(employeeList,listFinal);
			
		} catch (HibernateException he) {
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);
		} catch (Exception he) {
			

			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(), he);

		}
		return list;

	}
	
	
	// Rename to getEmployeesWithLeaveOpenBal
	//Add java doc
	/**
	 * 
	 * getEmployeesWithLeaveOpenBal is method which merges both Employee's list
	 * and leave Available if Exist else empty String
	 * @param employeeList,LeaveBalanceList
	 * @return List
	 * @author Divya
	 */
	public List getEmployeesWithLeaveOpenBal(List employeeList,List LeaveBalanceList)
	{
		List employeeLeaveList = new ArrayList();
		
		if (employeeList != null && !employeeList.isEmpty()) {
			
			
			
			for (Iterator iter = employeeList.iterator();iter.hasNext();) 
			{
				EmpLeaveOpenbalanceDTO empLeaveBalance = new EmpLeaveOpenbalanceDTO();
				String availableBalance="";
				Integer LeaveId=null;
				EmployeeView emp = (EmployeeView) iter.next();
				if(LeaveBalanceList!=null && !LeaveBalanceList.isEmpty())
				{
					for(Iterator iterLeave=LeaveBalanceList.iterator(); iterLeave.hasNext();)
					{
						LeaveOpeningBalance leaveBalance=(LeaveOpeningBalance)iterLeave.next();
						if(emp.getId().intValue()==leaveBalance.getEmployeeId().getIdPersonalInformation().intValue())
						{
							availableBalance=String.valueOf(leaveBalance.getNoOfLeavesAvai().intValue());
							LeaveId=leaveBalance.getId();
							break;
						}
					}
				}
								
				empLeaveBalance.setEmpCode(emp.getEmployeeCode());
				empLeaveBalance.setEmpName(emp.getEmployeeName());
				empLeaveBalance.setEmpId(emp.getId().toString());
				empLeaveBalance.setAvailableLeaves(availableBalance);
				empLeaveBalance.setLeaveId(LeaveId);
				employeeLeaveList.add(empLeaveBalance);
				
			}
		}
		return employeeLeaveList;
	}
	public List getListOfAccumulativeTypeOfLeaves()
	{
		List lstAccLeaveTypes = null;
		TypeOfLeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory().getTypeOfLeaveMasterDAO();
		lstAccLeaveTypes = (List) leaveMasterDAO.getListOfAccumulativeTypeOfLeaves();
		return lstAccLeaveTypes;
	}
	
	public String getCurrentYearId()
	{
		String currentYear="";
		CalendarYearDao calendarDao=LeaveDAOFactory.getDAOFactory().getCalendarDao();
		currentYear=calendarDao.getCurrentYearId();
		return currentYear;
	}
	public String getYearIdByGivenDate(String estDate)
	{
		String currentYear="";
		CalendarYearDao calendarDao=LeaveDAOFactory.getDAOFactory().getCalendarDao();
		currentYear=calendarDao.getYearIdByGivenDate(estDate);
		return currentYear;
	}
	public CalendarYear getCalendarYearById(Long id)
	{
		CalendarYear calendar=null;
		try {
			CalendarYearDao calendarDao= LeaveDAOFactory.getDAOFactory().getCalendarDao();
		    calendar=(CalendarYear)calendarDao.findById(id, false);
			return calendar;
		} catch (RuntimeException e) {
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public List<CalendarYear> getAllCalendarYearList()
    {
		CalendarYearDao calendarDao= LeaveDAOFactory.getDAOFactory().getCalendarDao();
        return calendarDao.findAll();
    }
	 //set of holiday list based on calendar year
	public List getHolidayListByCalendarYearID(CalendarYear calendarYr)
	{
		HolidaysUlbDAO holidaysUlbHibernateDAO = LeaveDAOFactory.getDAOFactory().getHolidaysUlbDAO();
		return holidaysUlbHibernateDAO.getHolidayListByCalendarYearID(calendarYr);
	}
	public Boolean isHolidayEnclosed()throws Exception {
		
		boolean isHolidayEnclosed = false;
		String holidayEnclosed=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Leave Application","ISHOLIDAY_ENCLOSED",new Date()).getValue();
		if("Y".equals(holidayEnclosed))
		{
			isHolidayEnclosed=true;
		}
		return isHolidayEnclosed;
	}
	
	//Api added for report
	public List getNoOfWorkingDaysByFinYearForReport(
			java.util.Date fromDate, java.util.Date toDate,
			CFinancialYear cfinancial)throws Exception {
		try {
			List<String> wdList = new ArrayList<String>();
			Set holidaySet =null;
			SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
			CFinancialYear financial = cfinancial;
			holidaySet = getHolidaySet(financial);
			if (sdf.format(fromDate).equals(sdf.format(toDate))) 
				{
					if (!holidaySet.contains(sdf.format(fromDate))) 
					{
						wdList.add(sdf.format(fromDate.getTime()));
					}
	
				} 
				else 
				{
					Calendar fromCalendar = Calendar.getInstance();
					fromCalendar.setTime(fromDate);
					Calendar toCalendar = Calendar.getInstance();
					toCalendar.setTime(toDate);
	
					do 
					{
						if (!holidaySet.contains(new java.sql.Date(fromCalendar
								.getTime().getTime()).toString())) 
						{
							wdList.add(new java.sql.Date(fromCalendar.getTime()
									.getTime()).toString());
						}
						fromCalendar.add(Calendar.DATE, 1);
	
					} while (fromCalendar.getTime().getTime() < toCalendar
							.getTime().getTime()
							|| fromCalendar.getTime().getTime() == toCalendar
									.getTime().getTime());
	
				}
			
			return wdList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}

	}
	//Api to count Holiday made Absent for a month
	public int getNumberOfAbsentInHolidays(Set holidaySetForMonth,PersonalInformation personalInformation)
	{
		int AbsentforHoliday=0;
		SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
		
		try {
			for(Iterator itr =holidaySetForMonth.iterator();itr.hasNext(); )
			{
				String wd = (String)itr.next();
				Attendence attendence = checkAttendenceByEmpAndDte(personalInformation.getIdPersonalInformation(),sdf.parse(wd));
				if(attendence!=null && attendence.attendenceType.getName().equals(org.egov.pims.utils.EisConstants.ABSENT))
				{
					
						AbsentforHoliday++;
					
				}
			}
		} catch (ParseException e) {
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
			
		}
		return AbsentforHoliday;
	}
	
	//Api to list the Holiday for the given dates
	public Set listOfHolidaysForTwoDates(java.util.Date fromDate, java.util.Date toDate)
	{
		Set<String> wdList = new HashSet<String>();
		try {
			List objDateList = EisManagersUtill.getListOfFinYrandDate(fromDate,toDate);
			if (objDateList.size() == 1) 
			{
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList.get(0);
				wdList = listOfHolidaysForDate(fromDate, toDate,finYrandDateFirst.getFinancial());
			} 
			else if (objDateList.size() == 2)
			{
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList.get(0);
				DatePeriodFY finYrandDateSecond = (DatePeriodFY) objDateList.get(1);
				wdList.addAll((Collection) listOfHolidaysForDate(new java.sql.Date(finYrandDateFirst.getFromDate()
						.getTime()), new java.sql.Date(
								finYrandDateFirst.getToDate().getTime()),finYrandDateFirst.getFinancial()));
				wdList.addAll((Collection) listOfHolidaysForDate(
								new java.sql.Date(finYrandDateSecond
										.getFromDate().getTime()),
								new java.sql.Date(finYrandDateSecond
										.getToDate().getTime()),finYrandDateSecond.getFinancial()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
		return wdList;
	}
	
	
	
	
	//Api to count Holiday made Absent for a from date, toDate
	public Set listOfHolidaysForDate(java.util.Date fromDate, java.util.Date toDate,
			CFinancialYear cfinancial)
	{
		try {
			
			
			Set<String> wdList = new HashSet<String>();
			Set holidaySet =null;
			SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
			CFinancialYear financial = cfinancial;
			holidaySet = getHolidaySet(financial);
			Calendar fromCalendar = Calendar.getInstance();
			fromCalendar.setTime(fromDate);
			Calendar toCalendar = Calendar.getInstance();
			toCalendar.setTime(toDate);
	
					do 
					{
						if (holidaySet.contains(new java.sql.Date(fromCalendar
								.getTime().getTime()).toString())) 
						{
							wdList.add(sdf.format(fromCalendar.getTime()));
						}
						fromCalendar.add(Calendar.DATE, 1);
	
					} while (fromCalendar.getTime().getTime() < toCalendar
							.getTime().getTime()
							|| fromCalendar.getTime().getTime() == toCalendar
									.getTime().getTime());
	
				
			
					
			return wdList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}
	public Boolean checkForCompOffApprove(Integer AttId) throws Exception
	{
		boolean isCompDateApproved = false;
		session = HibernateUtil.getCurrentSession();
		try
		{
			Query qry =null;
		
			if(AttId!=null)
			{
				qry = session.createQuery("from CompOff ca  where ca.attObj.id =:AttId");
			}
			if(qry!=null)
			{
				if(AttId!=null)
				{
					qry.setInteger("AttId",AttId);
				}
				LOGGER.info("TEST QUERY----"+qry);
				if(qry.list()!=null&&!qry.list().isEmpty())
				{
					isCompDateApproved = true;
				}
			}

		}
		catch (HibernateException he) {
				
				   //HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);
			} catch (Exception he) {
				
				   //HibernateUtil.rollbackTransaction();

				throw new EGOVRuntimeException(STR_EXCEPTION + he.getMessage(),he);

	}
			LOGGER.info("TEST FOR BOOLEAN STATUS-----"+isCompDateApproved);
		return isCompDateApproved;
	}
	
/**
 * This Api check whether its self approval from config
 * @return boolean value
 */
public Boolean isSelfApproval(){
		
		boolean isSelfApproval = false;
		String selfApproval="Y";
		AppConfigValues configValue=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Leave Application","SELF_APPROVAL",new Date());
		//if selfApproval is true then single step approval
			if(null!=configValue)
			{
				selfApproval=configValue.getValue();
				selfApproval = selfApproval.toUpperCase();
			}
			if("Y".equals(selfApproval))
			{
				isSelfApproval=true;
			}
		
		return isSelfApproval; 
	}

public Boolean isLeaveAvailForDateEmpIdStatus(Date givenDate,Long empid)        
{
	Criteria criteria=HibernateUtil.getCurrentSession().createCriteria(LeaveApplication.class).
	add(Restrictions.and(Restrictions.le("fromDate",givenDate), Restrictions.ge("toDate",givenDate)));
	criteria=criteria.createAlias("employeeId", "employee").add(Restrictions.eq("employee.idPersonalInformation", empid.intValue())); 
	criteria=criteria.createAlias("statusId", "leaveStatus").add(Restrictions.in("leaveStatus.name",new String[]{EisConstants.STATUS_APPLIED,EisConstants.STATUS_APPROVED}));
	return criteria.list().size()==0?Boolean.valueOf(false):Boolean.valueOf(true);     
	
} 

/**
  * Based on config key api decides for Auto or Manual
  * if strleaveAutoOrManaul-Auto(Auto WorkFlow) 
  * else Manual
  * @return true for Manaul
  */
public Boolean isLeaveWfAutoOrManaul()
{
	boolean leaveAutoOrManaul = false;
	String strleaveAutoOrManaul="Auto";
	AppConfigValues configValue=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Leave Application","LeaveAutoOrManualWorkFlow",new Date());
	//if selfApproval is true then single step approval
		if(null!=configValue)
		{
			strleaveAutoOrManaul=configValue.getValue();
			
		}
		if("Manual".equals(strleaveAutoOrManaul))
		{
			leaveAutoOrManaul=true;
		}
	
	return leaveAutoOrManaul;
}

	
	public List getNoOfWorkingDaysBweenTwoDatesForEmployee(java.util.Date fromDate, java.util.Date toDate, PersonalInformation employee){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
			List<String> wdList = new ArrayList<String>();
			List<Attendence> attendenceList = LeaveDAOFactory.getDAOFactory().getAttendenceDAO().getAttendenceListInDateRangeForEmployee(fromDate, toDate, employee);
			if(attendenceList.isEmpty()){
				wdList = getNoOfWorkingDaysBweenTwoDates(fromDate, toDate);
			}
			else{				
				Set<String> holidaySet = new HashSet<String>(); 
				List<Attendence> holidayAttendenceList = LeaveDAOFactory.getDAOFactory().getAttendenceDAO().getHolidayAttendenceListInDateRangeForEmployee(fromDate, toDate, employee);
				for(Attendence holAtten : holidayAttendenceList){
					holidaySet.add(sdf.format(holAtten.getAttDate()));
				}
								
				if(isHolidayEnclosed())
				{
					if (sdf.format(fromDate).equals(sdf.format(toDate))) 
					{
						if (!holidaySet.contains(sdf.format(fromDate))) 
						{
							wdList.add(sdf.format(fromDate.getTime()));
						}		
					} 
					else 
					{
						Calendar fromCalendar = Calendar.getInstance();
						fromCalendar.setTime(fromDate);
						Calendar toCalendar = Calendar.getInstance();
						toCalendar.setTime(toDate);		
						do 
						{							
							wdList.add(new java.sql.Date(fromCalendar.getTime().getTime()).toString());
							fromCalendar.add(Calendar.DATE, 1);		
						}while (fromCalendar.getTime().getTime() < toCalendar
								.getTime().getTime()
								|| fromCalendar.getTime().getTime() == toCalendar
										.getTime().getTime());
		
					}
				}
				else
				{
					if (sdf.format(fromDate).equals(sdf.format(toDate))) 
					{
						if (!holidaySet.contains(sdf.format(fromDate))) 
						{
							wdList.add(sdf.format(fromDate.getTime()));
						}		
					} 
					else 
					{
						Calendar fromCalendar = Calendar.getInstance();
						fromCalendar.setTime(fromDate);
						Calendar toCalendar = Calendar.getInstance();
						toCalendar.setTime(toDate);		
						do 
						{
							if (!holidaySet.contains(new java.sql.Date(fromCalendar.getTime().getTime()).toString())) 
							{
								wdList.add(new java.sql.Date(fromCalendar.getTime().getTime()).toString());
							}
							fromCalendar.add(Calendar.DATE, 1);		
						} while (fromCalendar.getTime().getTime() < toCalendar
								.getTime().getTime()
								|| fromCalendar.getTime().getTime() == toCalendar
										.getTime().getTime());
		
					}
				}
			}
			return wdList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(), e);
		}
	}

	private int getTotalNoOfAbsentDaysForAnEmployeeByDates(Date fromDate, Date toDate, PersonalInformation personalInformation, Set holidaySet){
		int noOfdays = listOfDaysAbsentForAnEmployeeForGivendates(fromDate,toDate, personalInformation).size();
		List<Attendence> attendenceList = LeaveDAOFactory.getDAOFactory().getAttendenceDAO().getAttendenceListInDateRangeForEmployee(fromDate, toDate, personalInformation);
		if(attendenceList.isEmpty()){			
			noOfdays += getNumberOfAbsentInHolidays(holidaySet,personalInformation);
		}
		else{
			holidaySet = new HashSet<String>();
			List<Attendence> holidayAttendenceList = LeaveDAOFactory.getDAOFactory().getAttendenceDAO().getHolidayAttendenceListInDateRangeForEmployee(fromDate, toDate, personalInformation);
			SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
			for(Attendence holAtten : holidayAttendenceList){
				holidaySet.add(sdf.format(holAtten.getAttDate().getTime()));
			}
			noOfdays += getNumberOfAbsentInHolidays(holidaySet,personalInformation);
		}		
		return noOfdays; 
	}
	
	/**
	 * Returning set of holidays in string format for an employee in between two dates
	 * @param fromDate
	 * @param toDate
	 * @param employee
	 * @return
	 */
	public Set<String> getHolidaySetForEmployeInDateRange(Date fromDate, Date toDate, PersonalInformation employee){
		Set<String> holidaySet = new HashSet<String>();
		List<Attendence> attendenceList = LeaveDAOFactory.getDAOFactory().getAttendenceDAO().getAttendenceListInDateRangeForEmployee(fromDate, toDate, employee);
		if(attendenceList.isEmpty()){
			holidaySet = listOfHolidaysForTwoDates(fromDate, toDate);
		}
		else{
			List<Attendence> holidayAttendenceList = LeaveDAOFactory.getDAOFactory().getAttendenceDAO().getHolidayAttendenceListInDateRangeForEmployee(fromDate, toDate, employee);
			SimpleDateFormat sdf = new SimpleDateFormat(STR_YEARMONDATE,Locale.getDefault());
			for(Attendence holAtten : holidayAttendenceList){
				holidaySet.add(sdf.format(holAtten.getAttDate().getTime()));
			}
			//if max attendance date is before end of the month,
			//then calculate the holidays from max attendance date to end of the month and add to holiday set
			Date maxAttDate=getMaxAttDateForEmpDateRange(fromDate,toDate,employee);
			LOGGER.info("getMaxAttDate " +maxAttDate+"todate "+toDate);
			if(null!=maxAttDate&&toDate.after(maxAttDate))
			{		
				maxAttDate.setDate(maxAttDate.getDate()+1);
				holidaySet.addAll(listOfHolidaysForTwoDates(maxAttDate, toDate));
			}
			
		}		
		return holidaySet;
	}
	private Date getMaxAttDateForEmpDateRange(Date fromDate,Date toDate, PersonalInformation employee)
	{
		Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Attendence.class,"att")
		.setProjection(Projections.max("att.attDate"))
		.createAlias("attendenceType", "attType")		
		.add(Restrictions.eq("att.employee", employee))    	
    	.add(Restrictions.between("att.attDate",fromDate,toDate))
    	.add(Restrictions.or(Restrictions.eq("attType.name", EisConstants.HOLIDAY),Restrictions.eq("attType.name", EisConstants.COMPOFF_ELIG)));    	
		return(Date) criteria.uniqueResult();
	}
	public List getAccumulatedLeaveTypes() {
		TypeOfLeaveMasterDAO leaveMasterDAO = LeaveDAOFactory.getDAOFactory()
				.getTypeOfLeaveMasterDAO();
		HashMap leaveTypeMap = new HashMap();
		return leaveMasterDAO.getListOfAccumulativeTypeOfLeaves();
	}
	private final static String STR_START_DATE="startDate";
	
	private final static String STR_EXCEPTION="Exception:";
	private final static String STR_YEARMONDATE="yyyy-MM-dd";
	private final static String STR_DAYMONYEAR="dd-MMM-yyyy";

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	

}


