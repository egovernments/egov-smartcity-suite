/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pims.client;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.dao.GenericMasterDAO;
import org.egov.pims.model.GenericMaster;
import org.egov.pims.service.EmployeeServiceOld;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Referenced classes of package org.egov.pims.client:
//            GenericForm

public class AfterGenericMasterAction extends DispatchAction {

	public final static Logger LOGGER = Logger.getLogger(AfterGenericMasterAction.class.getClass());

	private EmployeeServiceOld employeeService;

	@Autowired
	private EgovMasterDataCaching masterDataCache;

	@Autowired
	private GenericMasterDAO genericMasterDAO;
	
	public AfterGenericMasterAction() {
	}

	public ActionForward saveDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String target = null;

		String alertMessage = null;
		String className = req.getParameter("className").trim();
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
					throw new ApplicationRuntimeException((new StringBuilder(
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
			target = "success";
			alertMessage = "Executed successfully";
		}

		catch (ApplicationRuntimeException e) {
			target = ERROR;
			LOGGER.error(e.getMessage());
			ApplicationRuntimeException er = new ApplicationRuntimeException(e.getMessage());
			er.initCause(e);
			//throw er;
		} catch (ClassNotFoundException e) {
			target = ERROR;
			LOGGER.error(e.getMessage());
			ApplicationRuntimeException er = new ApplicationRuntimeException(
					(new StringBuilder("class not found Exception : ")).append(
							e.getMessage()).toString());
			er.initCause(e);
			//throw er;
		} catch (Exception ex) {

			target = ERROR;
			   LOGGER.error(ex.getMessage());
			   throw new ApplicationRuntimeException(EXCEPTION + ex.getMessage(),ex);
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
			target = "success";
			alertMessage = "Executed successfully";
		} catch (Exception ex) {
			   LOGGER.error(ex.getMessage());
			   throw new ApplicationRuntimeException(EXCEPTION + ex.getMessage(),ex);
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
			String className = req.getParameter("className").trim();
			GenericMaster genericMaster = genericMasterDAO.getGenericMaster(
					(Integer.valueOf(genericForm.getId())).intValue(), className);
			genericMasterDAO.remove(genericMaster);
			removeFromCache(className);
			req.getSession().removeAttribute("Id");
			target = "success";
			alertMessage = "Executed deleting successfully";
		} catch (ConstraintViolationException ex) {

			target = ERROR;
			alertMessage = "This data can't be deleted as it is being used";
			LOGGER.error(ex.getMessage());
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new ApplicationRuntimeException(EXCEPTION + ex.getMessage(),ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		req.getSession().setAttribute(STR_VIEWMODE, "create");
		return mapping.findForward(target);
	}

	private void removeFromCache(String className) {
		
		try {
			String tagName = (new StringBuilder("egEmp-")).append(className.trim())
					.toString();
			masterDataCache.removeFromCache(tagName);
		} catch (ApplicationRuntimeException e) {
			LOGGER.error(e.getMessage());
			throw new ApplicationRuntimeException(EXCEPTION + e.getMessage(),e);
		}
	}

	private Date getDateString(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		Date d = null;
		try {
			d = dateFormat.parse(dateString);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ApplicationRuntimeException(EXCEPTION + e.getMessage(),e);
		}
		return d;
	}

	/**
	 * @return the eisManagr
	 */

	public EmployeeServiceOld getEmployeeService() {
		return employeeService;
	}

	/**
	 * @param eisManagr the eisManagr to set
	 */
	public void setEmployeeService(EmployeeServiceOld employeeService) {
		this.employeeService = employeeService;
	}
	
	private static final String ERROR = "error";
	private static final String EXCEPTION = "Exception:";
	private static final String STR_VIEWMODE="viewMode";
	
}
