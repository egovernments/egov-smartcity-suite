package org.egov.pims.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.dao.GenericMasterDAO;
import org.egov.pims.model.GenericMaster;
import org.egov.pims.service.EmployeeService;

import org.hibernate.exception.ConstraintViolationException;

// Referenced classes of package org.egov.pims.client:
//            GenericForm

public class AfterGenericMasterAction extends DispatchAction {

	public final static Logger LOGGER = Logger.getLogger(AfterGenericMasterAction.class.getClass());

	private EmployeeService employeeService;

	public AfterGenericMasterAction() {
	}

	public ActionForward saveDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = null;

		String alertMessage = null;
		String className = req.getParameter("className").trim();
		GenericMasterDAO genericMasterDAO = new GenericMasterDAO();
		GenericMaster genericMaster = null;
		try {
			GenericForm genericForm = (GenericForm) form;
			genericMaster = (GenericMaster) Class.forName(
					(new StringBuilder("org.egov.pims.model.")).append(
							className).toString()).newInstance();

			if (genericForm.getName() != null
					&& !genericForm.getName().equals("")) {
				String code = genericForm.getName().trim().toUpperCase();
				if (employeeService.checkDuplication(code, className)) {
					throw new DuplicateElementException((new StringBuilder(
							"duplicate ")).append(className).toString());
				}
			}
			if (genericForm.getFromDate() != null
					&& !genericForm.getFromDate().equals("")) {
				genericMaster.setFromDate(getDateString(genericForm
						.getFromDate()));
			}
			if (genericForm.getToDate() != null
					&& !genericForm.getToDate().equals("")) {
				genericMaster.setToDate(getDateString(genericForm.getToDate()));
			}
			if (genericForm.getName() != null
					&& !genericForm.getName().equals("")) {
				genericMaster.setName(genericForm.getName());
			}
			genericMasterDAO.create(genericMaster);
			removeFromCache(className);
			HibernateUtil.getCurrentSession().flush();
			target = "success";
			alertMessage = "Executed successfully";
		}

		catch (DuplicateElementException e) {
			target = ERROR;
			LOGGER.error(e.getMessage());
			EGOVRuntimeException er = new EGOVRuntimeException(e.getMessage());
			er.initCause(e);
			//throw er;
		} catch (ClassNotFoundException e) {
			target = ERROR;
			LOGGER.error(e.getMessage());
			EGOVRuntimeException er = new EGOVRuntimeException(
					(new StringBuilder("class not found Exception : ")).append(
							e.getMessage()).toString());
			er.initCause(e);
			//throw er;
		} catch (Exception ex) {

			target = ERROR;
			   LOGGER.error(ex.getMessage());
			   //HibernateUtil.rollbackTransaction();
			   throw new EGOVRuntimeException(EXCEPTION + ex.getMessage(),ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}

	public ActionForward modifyDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = null;
		String alertMessage = null;
		try {

			GenericForm genericForm = (GenericForm) form;
			GenericMasterDAO genericMasterDAO = new GenericMasterDAO();
			String className = req.getParameter("className").trim();

			GenericMaster genericMastr = genericMasterDAO.getGenericMaster(
					(Integer.valueOf(genericForm.getId())).intValue(), className);

			if (genericForm.getFromDate() != null
					&& !genericForm.getFromDate().equals("")) {
				genericMastr.setFromDate(getDateString(genericForm
						.getFromDate()));
			}
			if (genericForm.getToDate() != null
					&& !genericForm.getToDate().equals("")) {
				genericMastr.setToDate(getDateString(genericForm.getToDate()));
			}
			if (genericForm.getName() != null
					&& !genericForm.getName().equals("")) {
				genericMastr.setName(genericForm.getName());
			}
			genericMasterDAO.update(genericMastr);
			removeFromCache(className);
			req.getSession().removeAttribute("Id");
			HibernateUtil.getCurrentSession().flush();
			target = "success";
			alertMessage = "Executed successfully";
		} catch (Exception ex) {
				target = ERROR;
			   LOGGER.error(ex.getMessage());
			   //HibernateUtil.rollbackTransaction();
			   throw new EGOVRuntimeException(EXCEPTION + ex.getMessage(),ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		req.getSession().setAttribute(STR_VIEWMODE, "create");
		return mapping.findForward(target);
	}

	public ActionForward deleteDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = null;
		String alertMessage = null;
		try {

			GenericForm genericForm = (GenericForm) form;
			GenericMasterDAO genericMasterDAO = new GenericMasterDAO();
			String className = req.getParameter("className").trim();
			GenericMaster genericMaster = genericMasterDAO.getGenericMaster(
					(Integer.valueOf(genericForm.getId())).intValue(), className);
			genericMasterDAO.remove(genericMaster);
			removeFromCache(className);
			req.getSession().removeAttribute("Id");
			HibernateUtil.getCurrentSession().flush();
			target = "success";
			alertMessage = "Executed deleting successfully";
		} catch (ConstraintViolationException ex) {

			target = ERROR;
			alertMessage = "This data can't be deleted as it is being used";
			LOGGER.error(ex.getMessage());
			//HibernateUtil.rollbackTransaction();
		} catch (Exception ex) {
			target = ERROR;
			alertMessage = "This can't be deleted";
			LOGGER.error(ex.getMessage());
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(EXCEPTION + ex.getMessage(),ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		req.getSession().setAttribute(STR_VIEWMODE, "create");
		return mapping.findForward(target);
	}

	private void removeFromCache(String className) {
		
		try {
			String tagName = (new StringBuilder("egEmp-")).append(className.trim())
					.toString();
			EgovMasterDataCaching.getInstance().removeFromCache(tagName);
		} catch (EGOVRuntimeException e) {
			// Exception Handled
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(EXCEPTION + e.getMessage(),e);
		}
	}

	private Date getDateString(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		Date d = null;
		try {
			d = dateFormat.parse(dateString);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(EXCEPTION + e.getMessage(),e);
		}
		return d;
	}

	/**
	 * @return the eisManagr
	 */

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	/**
	 * @param eisManagr the eisManagr to set
	 */
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	private static final String ERROR = "error";
	private static final String EXCEPTION = "Exception:";
	private static final String STR_VIEWMODE="viewMode";
	
}
