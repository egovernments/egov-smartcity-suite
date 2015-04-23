package org.egov.services.receipt;

import java.util.Date;
import java.util.List;

import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.receipt.ReceiptVoucher;
import org.egov.pims.commons.Position;
import org.egov.eis.service.EisCommonService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;

public class ReceiptService extends PersistenceService<ReceiptVoucher, Long>{
	protected EisCommonService eisCommonService;
	private GenericHibernateDaoFactory genericDao;
	private EisUtilService eisService;
	private  PersistenceService persistenceService;
	public Position getPositionForEmployee(PersonalInformation emp)throws EGOVRuntimeException
	{
		return null;//This fix is for Phoenix Migration.eisCommonService.getPositionforEmp(emp.getIdPersonalInformation());
	}
	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}
	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao; 
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public void createVoucherfromPreApprovedVoucher(ReceiptVoucher rv){
		final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","APPROVEDVOUCHERSTATUS");
		final String approvedVoucherStatus = appList.get(0).getValue();
		rv.getVoucherHeader().setStatus(Integer.valueOf(approvedVoucherStatus));
	}
	public void cancelVoucher(ReceiptVoucher rv){
		final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","cancelledstatus");
		final String approvedVoucherStatus = appList.get(0).getValue();
		rv.getVoucherHeader().setStatus(Integer.valueOf(approvedVoucherStatus));
	}
	public String getDesginationName()
	{
		 PersonalInformation pi = null;//This fix is for Phoenix Migration.eisCommonService.getEmpForUserId(Integer.parseInt(EGOVThreadLocals.getUserId()));
		 Assignment assignment =null;//This fix is for Phoenix Migration. eisCommonService.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
		 return assignment.getDesigId().getDesignationName();
	}
	public Department getDepartmentForWfItem(ReceiptVoucher rv)
	{
		PersonalInformation pi = null;//This fix is for Phoenix Migration.eisCommonService.getEmpForUserId(rv.getCreatedBy().getId());
		Assignment assignment = null;//This fix is for Phoenix Migration.eisCommonService.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
		return assignment.getDeptId();
	}
	public Position getPositionForWfItem(ReceiptVoucher rv)
	{
		return null;//This fix is for Phoenix Migration.eisCommonService.getPositionByUserId(rv.getCreatedBy().getId());
	}
	public Boundary getBoundaryForUser(ReceiptVoucher rv)
	{
		return new EgovCommon().getBoundaryForUser(rv.getCreatedBy());
	}
	public Department getDepartmentForUser(User user) 
	{
		return new EgovCommon().getDepartmentForUser(user, eisCommonService, eisService,persistenceService);
    }
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
}
