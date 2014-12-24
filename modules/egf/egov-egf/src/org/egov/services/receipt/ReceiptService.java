package org.egov.services.receipt;

import java.util.Date;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.user.User;
import org.egov.model.receipt.ReceiptVoucher;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;

public class ReceiptService extends PersistenceService<ReceiptVoucher, Long> {
	private EmployeeService employeeService;
	private EisCommonsService eisCommonsService;
	// protected EisManager eisManager;
	// protected EisCommonsManager eisCommonsManager;
	private GenericHibernateDaoFactory genericDao;
	private EisUtilService eisService;
	private PersistenceService persistenceService;

	public Position getPositionForEmployee(PersonalInformation emp)
			throws EGOVRuntimeException {
		return employeeService
				.getPositionforEmp(emp.getIdPersonalInformation());
	}

	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void createVoucherfromPreApprovedVoucher(ReceiptVoucher rv) {
		final List<AppConfigValues> appList = genericDao
				.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF",
						"APPROVEDVOUCHERSTATUS");
		final String approvedVoucherStatus = appList.get(0).getValue();
		rv.getVoucherHeader().setStatus(Integer.valueOf(approvedVoucherStatus));
	}

	public void cancelVoucher(ReceiptVoucher rv) {
		final List<AppConfigValues> appList = genericDao
				.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF",
						"cancelledstatus");
		final String approvedVoucherStatus = appList.get(0).getValue();
		rv.getVoucherHeader().setStatus(Integer.valueOf(approvedVoucherStatus));
	}

	public String getDesginationName() {
		PersonalInformation pi = employeeService.getEmpForUserId(Integer
				.parseInt(EGOVThreadLocals.getUserId()));
		Assignment assignment = employeeService.getAssignmentByEmpAndDate(
				new Date(), pi.getIdPersonalInformation());
		return assignment.getDesigId().getDesignationName();
	}

	public Department getDepartmentForWfItem(ReceiptVoucher rv) {
		PersonalInformation pi = employeeService.getEmpForUserId(rv
				.getCreatedBy().getId());
		Assignment assignment = employeeService.getAssignmentByEmpAndDate(
				new Date(), pi.getIdPersonalInformation());
		return assignment.getDeptId();
	}

	public Position getPositionForWfItem(ReceiptVoucher rv) {
		return eisCommonsService.getPositionByUserId(rv.getCreatedBy().getId());
	}

	public Boundary getBoundaryForUser(ReceiptVoucher rv) {
		return new EgovCommon().getBoundaryForUser(rv.getCreatedBy());
	}

	public Department getDepartmentForUser(User user) {
		return new EgovCommon().getDepartmentForUser(user, employeeService,
				eisService, persistenceService);
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

}
