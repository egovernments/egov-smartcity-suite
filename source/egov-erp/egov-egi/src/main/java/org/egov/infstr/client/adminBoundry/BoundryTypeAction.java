package org.egov.infstr.client.adminBoundry;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.delegate.BoundaryTypeDelegate;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeImpl;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;
import org.egov.lib.admbndry.ejb.server.HeirarchyTypeServiceImpl;

public class BoundryTypeAction extends EgovAction {
	private static final Logger LOG = LoggerFactory.getLogger(BoundryTypeAction.class);
	private HeirarchyTypeService heirarchyTypeService = new HeirarchyTypeServiceImpl();
	/**
	 * This method does Create, Update and Delete BoundryType based on the client's call
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @return ActionForward
	 **/

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException, EGOVException {

		try {
			String message = "";
			final javax.servlet.http.HttpSession session = req.getSession();
			final String operation = req.getParameter("operation");
			final BoundaryType boundaryType = new BoundaryTypeImpl();
			final BoundryTypeForm boundryTypeForm = (BoundryTypeForm) form;

			final BoundaryTypeDelegate boundaryTypeDelegate = BoundaryTypeDelegate.getInstance();
			final HeirarchyType heirarchyType = heirarchyTypeService.getHeirarchyTypeByID(Integer.valueOf(boundryTypeForm.getHeirarchyType().trim()));
			boundaryType.setHeirarchyType(heirarchyType);
			boundaryType.setName(boundryTypeForm.getName());
			boundaryType.setParentName(boundryTypeForm.getParentName());
			boundaryType.setBndryTypeLocal(boundryTypeForm.getBndryTypeLocal());

			if ("create".equals(operation)) {
				try {
					boundaryTypeDelegate.createBoundaryType(boundaryType);
					message = "Boundry Type successfully created.";
				} catch (final Exception e) {
					LOG.error("Error occurred while creating Boundary Type.",e);
					req.setAttribute("MESSAGE", "Error occurred while creating Boundary Type.");
					throw new EGOVRuntimeException("Error occurred while creating Boundary Type.");
				}

			} else if ("edit".equals(operation)) {
				try {
					boundaryTypeDelegate.updateBoundaryType(boundaryType);
					message = "Boundry Type successfully modified.";
				} catch (final Exception e) {
					LOG.error("Error occurred while updating Boundary Type.",e);
					req.setAttribute("MESSAGE", "Error occurred while updating Boundary Type");
					throw new EGOVRuntimeException("Error occurred while updating Boundary Type.");

				}

			} else if ("delete".equals(operation)) {
				try {
					boundaryTypeDelegate.deleteBoundaryType(boundaryType);
					message = "Boundry Type successfully deleted.";
				} catch (final Exception e) {
					LOG.error("Error occurred while deleting Boundary Type",e);
					req.setAttribute("MESSAGE", "Error occurred while deleting Boundary Type");
					throw new EGOVRuntimeException("Error occurred while deleting Boundary Type");

				}

			} else {
				throw new EGOVRuntimeException("Boundary Type operation not found.");
			}
			
			session.setAttribute("MESSAGE", message);
			return mapping.findForward("success");
		} catch (final Exception e) {
			LOG.error("Error occurred while loading Boundary Type screen",e);
			req.setAttribute("MESSAGE", "Request cannot be processed due to an internal server error.");
			throw new EGOVRuntimeException("Request cannot be processed due to an internal server error.");
		}

	}
}
