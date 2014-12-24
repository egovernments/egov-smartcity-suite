package org.egov.pims.commons.client;

import static org.egov.pims.utils.EisConstants.NAMEDQUERY_BILLNUMBER_BY_ID;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.DrawingOfficer;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.commons.dao.PositionMasterDAO;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.masters.model.BillNumberMaster;

/*
 * RegisterAction.java	1.00 Jan 31 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class AfterDesignationMasterAction extends DispatchAction {

	public final static Logger LOGGER = Logger.getLogger(AfterDesignationMasterAction.class.getClass());
	private PersistenceService persistenceService;
	private EisCommonsService eisCommonsService;


	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}


	public ActionForward saveDetails(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String target = null;
		String alertMessage = null;
		DesignationMasterDAO queryMasterDAO = new DesignationMasterDAO();
		DesignationMaster desMaster = new DesignationMaster();
		try {
			DesignationForm designationForm = (DesignationForm) form;
			if (designationForm.getDesignationName() != null && !designationForm.getDesignationName().equals("")) {
				desMaster.setDesignationName(designationForm.getDesignationName().toUpperCase());
			}

			if (designationForm.getSanctionedPostsDesig() != null
					&& !designationForm.getSanctionedPostsDesig().equals("")) {
				desMaster.setSanctionedPosts(Integer.valueOf(designationForm.getSanctionedPostsDesig()));
			}
			if (designationForm.getOutsourcedPostsDesig() != null
					&& !designationForm.getOutsourcedPostsDesig().equals("")) {
				desMaster.setOutsourcedPosts(Integer.valueOf(designationForm.getOutsourcedPostsDesig()));
			}
			if (designationForm.getDesignationDescription() != null
					&& !designationForm.getDesignationDescription().equals("")) {
				desMaster.setDesignationDescription(designationForm.getDesignationDescription());
			}
			Position position = setPosition(desMaster, req, designationForm);
			// getCommonsManager().addPosition(position, desMaster);
			queryMasterDAO.createDesignationMaster(desMaster);
			EgovMasterDataCaching.getInstance().removeFromCache("egEmp-DesignationMaster");

			LOGGER.info(" inside saveDetails ");
			target = "success";
			alertMessage = "Executed successfully";

		} catch (Exception ex) {
			target = "error";
			LOGGER.error(ex.getMessage());
			throw new EGOVRuntimeException(ex.getMessage(), ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);
	}

	public ActionForward modifyDetails(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {

		String target = null;
		String alertMessage = null;

		try {
			LOGGER.info(" inside modifyDetails ");
			DesignationForm designationForm = (DesignationForm) form;
			DesignationMasterDAO queryMasterDAO = new DesignationMasterDAO();
			DesignationMaster desMaster = queryMasterDAO.getDesignationMaster(Integer.valueOf(
					designationForm.getDesignationId()).intValue());
			if (designationForm.getDesignationName() != null && !designationForm.getDesignationName().equals("")) {
				desMaster.setDesignationName(designationForm.getDesignationName().toUpperCase());
			}
			if (designationForm.getSanctionedPostsDesig() != null
					&& !designationForm.getSanctionedPostsDesig().equals("")) {
				desMaster.setSanctionedPosts(Integer.valueOf(designationForm.getSanctionedPostsDesig()));
			}
			if (designationForm.getOutsourcedPostsDesig() != null
					&& !designationForm.getOutsourcedPostsDesig().equals("")) {
				desMaster.setOutsourcedPosts(Integer.valueOf(designationForm.getOutsourcedPostsDesig()));
			}

			if (designationForm.getDesignationDescription() != null
					&& !designationForm.getDesignationDescription().equals("")) {
				desMaster.setDesignationDescription(designationForm.getDesignationDescription());
			}

			setPosition(desMaster, req, designationForm);
			queryMasterDAO.updateDesignationMaster(desMaster);
			EgovMasterDataCaching.getInstance().removeFromCache("egEmp-DesignationMaster");
			target = "modifyfinal";
			alertMessage = "Executed successfully";
		} catch (Exception ex) {
			target = "error";
			LOGGER.info(ex.getMessage());
			throw new EGOVRuntimeException(ex.getMessage(), ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);

	}

	public ActionForward deleteDetails(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {

		String target = null;
		String alertMessage = null;
		try {
			LOGGER.info(" inside modifyDetails ");
			DesignationForm designationForm = (DesignationForm) form;
			DesignationMasterDAO queryMasterDAO = new DesignationMasterDAO();
			DesignationMaster desMaster = queryMasterDAO.getDesignationMaster(Integer.valueOf(
					designationForm.getReportsTo()).intValue());
			queryMasterDAO.removeDesignationMaster(desMaster);
			EgovMasterDataCaching.getInstance().removeFromCache("egEmp-DesignationMaster");
			target = "finaldelete";
			alertMessage = "Executed successfully";
		} catch (Exception ex) {
			target = "error";
			LOGGER.error(ex.getMessage());
			throw new EGOVRuntimeException(ex.getMessage(), ex);
		}
		req.setAttribute("alertMessage", alertMessage);
		return mapping.findForward(target);

	}

	private Position setPosition(DesignationMaster desMaster, HttpServletRequest req, DesignationForm designationForm) {
		// String tpId[]=req.getParameterValues("tpId");
		String tpId[] = designationForm.getTpId();
		String positionName[] = null;
		Position position = null;
		// positionName =req.getParameterValues("positionName");
		positionName = designationForm.getPositionName();
		String[] billNumbers = designationForm.getBillNumberId();
		LOGGER.info("No of BillNumbers: " + ((billNumbers == null) ? 0 : billNumbers.length));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		String deletePositionSet[] = req.getParameterValues("deletePositionSet");
		PositionMasterDAO positionDao = new PositionMasterDAO();
		int ArrLennmtst = tpId.length;
		try {
			if (!positionName[0].equals("0")) {
				LOGGER.info("1");
				for (int len = 0; len < ArrLennmtst; len++) {
					LOGGER.info("2");
					if (!positionName[len].equals("0")) {

						LOGGER.info("3");
						if (tpId[len].equals("0")) {
							LOGGER.info("4");
							position = new Position();
							String[] effDate = null;
							String[] outs = null;
							String[] name = null;
							String drawingOfficer[] = null;
							LOGGER.info("4.1");
							// effDate
							// =req.getParameterValues("efferctiveDate");
							// outs =req.getParameterValues("outsourcedPosts");
							// name =req.getParameterValues("positionName");

							effDate = designationForm.getEffectiveDate();
							outs = designationForm.getOutsourcedPosts();
							name = designationForm.getPositionName();
							drawingOfficer = designationForm.getDrawingOfficer();
							LOGGER.info("4.2");

							position.setSanctionedPosts(Integer.valueOf("1"));

							if (outs[len] != null && !outs[len].equals("")) {

								position.setOutsourcedPosts(Integer.valueOf(outs[len]));
							}

							if (name[len] != null && !name[len].equals("")) {
								position.setName(name[len]);
							}
							position.setDesigId(desMaster);

							if (effDate[len] != null && !effDate[len].equals("")) {
								position.setEfferctiveDate(sdf.parse(effDate[len]));
							}
							if (drawingOfficer[len] != null && !"".equals(drawingOfficer[len])) {
								Integer doId = Integer.parseInt(drawingOfficer[len]);
								position.setDrawingOfficer((DrawingOfficer) persistenceService.find(
										"from DrawingOfficer where id=?", doId));
							}

							if (StringUtils.isNotBlank(billNumbers[len])) {
								BillNumberMaster billNumber = (BillNumberMaster) persistenceService.findByNamedQuery(
										NAMEDQUERY_BILLNUMBER_BY_ID, Integer.valueOf(billNumbers[len]));
								LOGGER.info("AfterDesignationMasterAction.setPosition - before setting position: "
										+ billNumber);								
								position.setBillNumber(billNumber);
								LOGGER.info("AfterDesignationMasterAction.setPosition - after setting position: "
										+ billNumber);
							}

							LOGGER.info("position" + position);

							LOGGER.info("5");
							// PositionMasterDAO positionMasterDAO = new
							// PositionMasterDAO();
							// positionMasterDAO.createPositionMaster(position);
							LOGGER.info("position" + position);

							LOGGER.info("position--" + position);
							LOGGER.info("6");
							PositionMasterDAO positionMasterDAO = new PositionMasterDAO();
							positionMasterDAO.createPositionMaster(position);
							desMaster.getPositionSet().add(position);
						} else {
							position = eisCommonsService.getPositionById(Integer.valueOf(tpId[len]).intValue());
							String[] ins = null;
							String[] effDate = null;
							String[] outs = null;
							String[] name = null;
							String drawingOfficer[] = null;
							// effDate
							// =req.getParameterValues("efferctiveDate");
							// outs =req.getParameterValues("outsourcedPosts");
							// name =req.getParameterValues("positionName");

							effDate = designationForm.getEffectiveDate();
							outs = designationForm.getOutsourcedPosts();
							name = designationForm.getPositionName();
							drawingOfficer = designationForm.getDrawingOfficer();
							position.setSanctionedPosts(Integer.valueOf("1"));

							if (outs[len] != null && !outs[len].equals("")) {

								position.setOutsourcedPosts(Integer.valueOf(outs[len]));
							}
							if (name[len] != null && !name[len].equals("")) {
								position.setName(name[len]);
							}
							position.setDesigId(desMaster);
							if (effDate[len] != null && !effDate[len].equals("")) {
								position.setEfferctiveDate(sdf.parse(effDate[len]));
							}
							if (drawingOfficer[len] != null && !"".equals(drawingOfficer[len])) {
								Integer doId = Integer.parseInt(drawingOfficer[len]);
								position.setDrawingOfficer((DrawingOfficer) persistenceService.find(
										"from DrawingOfficer where id=?", doId));
							} else if ("".equals(drawingOfficer[len])) {
								position.setDrawingOfficer(null);
							}
							if (StringUtils.isNotBlank(billNumbers[len])) {
								BillNumberMaster billNumber = (BillNumberMaster) persistenceService.findByNamedQuery(
										NAMEDQUERY_BILLNUMBER_BY_ID, Integer.valueOf(billNumbers[len]));
								LOGGER.info("AfterDesignationMasterAction.setPosition - before setting position: "
										+ billNumber);								
								position.setBillNumber(billNumber);
								LOGGER.info("AfterDesignationMasterAction.setPosition - after setting position: "
										+ billNumber);
							}
							// getCommonsManager().updatePosition(position);
						}
					}
				}

				Set<Integer> delPositionSet = (Set<Integer>) req.getSession().getAttribute("delPositions");

				if (delPositionSet != null && !delPositionSet.isEmpty()) {
					for (Integer delId : delPositionSet) {

						if (delId != null && !delId.equals("") && delId != 0) {
							Position delPositionObj = positionDao.getPosition(delId);
							if (delPositionObj != null) {
								positionDao.removePosition(delPositionObj);
								LOGGER.info("<<<<<<< Position deleted Successfully !!");
							}

						}
					}
				}
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
		return position;
	}




}
