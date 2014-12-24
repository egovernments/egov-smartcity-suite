package org.egov.payroll.client.empPayroll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.commons.EgwStatus;
import org.egov.dms.models.GenericFile;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.pims.model.PersonalInformation;

public class BeforePayrollDetailAction extends DispatchAction {
	private PayrollExternalInterface payrollExternalInterface;
	private PayRollService payRollService;
	private PersistenceService persistenceService;
	public static final Logger logger = Logger
			.getLogger(BeforePayrollDetailAction.class.getClass());
	String target = "";

	public PayrollExternalInterface getPayrollExternalInterface() {
		return payrollExternalInterface;
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public ActionForward beforeCreate(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String alertMessage = null;
		try {
			populate(req);
			String mode = req.getParameter("mode");
			if ("view".equalsIgnoreCase(mode)) {
				Integer id = Integer.valueOf(req.getParameter("Id"));
				PersonalInformation emp = getPayrollExternalInterface()
						.getEmloyeeById(id);
				List payscaleList = payRollService.getPayStructureByEmp(emp
						.getIdPersonalInformation());
				setFileAttributesInReq(emp, req);
				if (payscaleList == null || payscaleList.isEmpty()) {
					req.setAttribute("masters", "empPayroll");
					req.setAttribute("module", "Payslip");
					req.setAttribute("mode", "ViewModify");
					alertMessage = "Paystructre not yet created for the employee";
					req.setAttribute("alertMessage", alertMessage);
					target = "noPaystructre";
				} else {
					req.setAttribute("employeeOb", emp);
					mode = "view";
					req.getSession().setAttribute("viewMode", mode);
					target = "success";
				}

			} else {
				Integer id = Integer.valueOf(req.getParameter("Id"));
				PersonalInformation emp = getPayrollExternalInterface()
						.getEmloyeeById(id);
				List payscaleList = payRollService.getPayStructureByEmp(emp
						.getIdPersonalInformation());
				setFileAttributesInReq(emp, req);
				if (payscaleList == null || payscaleList.isEmpty()) {
					mode = "create";
				}
				req.setAttribute("employeeOb", emp);
				req.getSession().setAttribute("viewMode", mode);
				target = "success";
			}
		} catch (Exception ex) {
			target = "error";
			logger.error(ex.getMessage());

			throw new EGOVRuntimeException(ex.getMessage(), ex);
		}

		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);

	}

	public void populate(HttpServletRequest req) {
		ArrayList employeeStatusMasterList = (ArrayList) EgovMasterDataCaching
				.getInstance().get("egEmp-EmployeeStatusMaster");
		req.getSession().setAttribute("employeeStatusMasterList",
				employeeStatusMasterList);
		ArrayList recruimentMasterMasterList = (ArrayList) EgovMasterDataCaching
				.getInstance().get("egEmp-RecruimentMaster");
		req.getSession().setAttribute(
				"recruimentMasterMap",
				getPayrollExternalInterface().getMapForList(
						recruimentMasterMasterList));
		ArrayList catMasterList = (ArrayList) EgovMasterDataCaching
				.getInstance().get("egEmp-CategoryMaster");
		req.getSession().setAttribute("catMap",
				getPayrollExternalInterface().getMapForList(catMasterList));
		ArrayList payFixedInMasterList = (ArrayList) EgovMasterDataCaching
				.getInstance().get("egEmp-PayFixedInMaster");
		req.getSession().setAttribute(
				"payFixedInMaster",
				getPayrollExternalInterface().getMapForList(
						payFixedInMasterList));
		ArrayList typeOfPostingMstrList = (ArrayList) EgovMasterDataCaching
				.getInstance().get("egEmp-PostingMaster");
		req.getSession().setAttribute(
				"postingMap",
				getPayrollExternalInterface().getMapForList(
						typeOfPostingMstrList));
		ArrayList typeOfRecMasterList = (ArrayList) EgovMasterDataCaching
				.getInstance().get("egEmp-TypeOfRecruimentMaster");
		req.getSession().setAttribute(
				"recruimentMap",
				getPayrollExternalInterface()
						.getMapForList(typeOfRecMasterList));
		ArrayList bankList = (ArrayList) EgovMasterDataCaching.getInstance()
				.get("egEmp-Bank");
		req.getSession().setAttribute("bankMap", getBankMap(bankList));
		ArrayList branchList = (ArrayList) EgovMasterDataCaching.getInstance()
				.get("egEmp-BANKBRANCH");
		req.getSession().setAttribute("branchMap", getBranchMap(branchList));
		ArrayList statusMasterList = (ArrayList) EgovMasterDataCaching
				.getInstance().get("egEmp-EgwStatus");
		req.getSession().setAttribute("statusMasterMap",
				getStatusMasterMap(statusMasterList));
	}

	public Map getStatusMasterMap(ArrayList list) {
		Map statusMasterMap = new HashMap();
		try {
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				EgwStatus statusMaster = (EgwStatus) iter.next();
				statusMasterMap.put(statusMaster.getId(),
						statusMaster.getDescription());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(), e);
		}
		return statusMasterMap;
	}

	private Map getBankMap(ArrayList bankList) {
		LinkedHashMap depMap = new LinkedHashMap();
		try {
			for (Iterator iter = bankList.iterator(); iter.hasNext();) {
				Bank bank = (Bank) iter.next();
				depMap.put(bank.getId(), bank.getName());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(), e);
		}
		return depMap;
	}

	private Map getBranchMap(ArrayList branchList) {
		LinkedHashMap depMap = new LinkedHashMap();
		try {
			for (Iterator iter = branchList.iterator(); iter.hasNext();) {
				Bankbranch branch = (Bankbranch) iter.next();
				depMap.put(branch.getId(), branch.getBranchname());
				logger.debug(">>>>>>>>>>>>>>>branchmap" + depMap);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(), e);
		}
		return depMap;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	private void setFileAttributesInReq(PersonalInformation emp,
			HttpServletRequest req) {
		if (emp.getCategoryMstr() != null
				&& emp.getCategoryMstr().getFileId() != null) {
			GenericFile file = (GenericFile) persistenceService.getSession()
					.load(GenericFile.class, emp.getCategoryMstr().getFileId());
			if (file != null) {
				req.setAttribute("fileNo", file.getFileNumber());
				req.setAttribute("fileId", file.getId());
			}
		}
	}

	public PayRollService getPayRollService() {
		return payRollService;
	}

	public void setPayRollService(PayRollService payRollService) {
		this.payRollService = payRollService;
	}

}
