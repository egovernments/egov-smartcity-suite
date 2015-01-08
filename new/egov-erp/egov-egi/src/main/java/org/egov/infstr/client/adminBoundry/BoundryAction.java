/*
 * @(#)BoundryAction.java 3.0, 18 Jun, 2013 1:49:52 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.client.delegate.BoundaryDelegate;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.ejb.api.HeirarchyTypeService;
import org.egov.lib.admbndry.ejb.server.HeirarchyTypeServiceImpl;


public class BoundryAction extends EgovAction {

    private static final Logger LOG = LoggerFactory.getLogger(BoundryAction.class);
    private HeirarchyTypeService heirarchyTypeService = new HeirarchyTypeServiceImpl();
    
    /**
     * This method does Create, Update and Delete Boundary based on the client's call
     * @param ActionMapping mapping
     * @param ActionForm form
     * @param HttpServletRequest req
     * @param HttpServletResponse res
     * @return ActionForward
     */

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException, Exception {

        String target = "success";
        if(!this.isTokenValid(req)) {
            return  mapping.findForward("failure");
        }
        
        String message = "";

        try {

            final javax.servlet.http.HttpSession session = req.getSession();
            final String operation = (String) session.getAttribute("operation");
            final BoundryForm boundaryForm = (BoundryForm) form;
            final BoundaryDelegate boundaryDelegate = BoundaryDelegate.getInstance();

            int BndryId = 0;
            if (session.getAttribute("BndryIdValue") != null) {
                BndryId = (Integer) session.getAttribute("BndryIdValue");
            }

            if (operation.equals("create")) { // If user select to Create a Boundary

                try {

                    final Boundary boundary = this.createBoundaryObject(boundaryForm, session);
                    if (session.getAttribute("BndryIdValue") != null) {
                        boundary.setTopLevelBoundaryID(BndryId);
                        session.removeAttribute("BndryIdValue");
                    }

                    HeirarchyType heirarchyType = null;
                    if (session.getAttribute("heirarchyType") != null) {
                        final int heirarchyID = (Integer) session.getAttribute("heirarchyType");
                        heirarchyType = heirarchyTypeService.getHeirarchyTypeByID(heirarchyID);
                        session.removeAttribute("heirarchyType");
                    }
                    if (boundaryForm.getHeirarchyType() != null) {
                        heirarchyType = heirarchyTypeService.getHeirarchyTypeByID(Integer.parseInt(boundaryForm.getHeirarchyType().trim()));
                    }

                    final Boundary bndry = boundaryDelegate.createBoundary(boundary, operation, heirarchyType);
                    boundaryDelegate.createCityWebsite(boundaryForm, bndry);

                    if (bndry != null) {
                        message = "Boundary successfully created";
                    } else {
                        message = "Boundary creation failed";
                    }

                } catch (final Exception e) {
                    target = "error";
                    message = "Sorry ! Boundary could not be created";
                    LOG.error("Error in BoundaryAction at create Boundary", e);
                }

            } else if (operation.equals("edit")) { // If user select to Update a Boundary

                try {

                    final Boundary boundary = this.createBoundaryObject(boundaryForm, session);
                    if (session.getAttribute("BndryIdValue") != null) {
                        boundary.setTopLevelBoundaryID(BndryId);
                        session.removeAttribute("BndryIdValue");
                    }

                    if (session.getAttribute("myBoundaryId") == null) {
                        throw new EGOVRuntimeException("Boundary Id not found to update the Boundary");
                    } else {
                        boundary.setId(Integer.parseInt(session.getAttribute("myBoundaryId").toString().trim()));
                        session.removeAttribute("myBoundaryId");
                    }

                    boundaryDelegate.removeCityWebsite(BndryId);
                    boundaryDelegate.updateBoundary(boundary);
                    boundaryDelegate.createCityWebsite(boundaryForm, BndryId);

                    message = "Boundary update successfully done";

                } catch (final Exception e) {
                    target = "error";
                    message = "Sorry ! Boundary updation failed.";
                    LOG.error("Error in BoundaryAction at Update Boundary", e);
                }

            } else if (operation.equals("delete")) { // If user select to Delete a Boundary
                try {

                    final String myBoundaryId = (String) session.getAttribute("myBoundaryId");

                    if (myBoundaryId == null) {
                        throw new EGOVRuntimeException("Boundary Id not found to delete the Boundary");
                    }

                    session.removeAttribute("myBoundaryId");

                    boundaryDelegate.removeCityWebsite(BndryId);
                    boundaryDelegate.deactivateBoundary(Integer.parseInt(myBoundaryId.trim()));

                    message = "Boundary successfully deleted";

                } catch (final Exception e) {
                    target = "error";
                    message = "Sorry ! Boundary deletion failed.";
                    LOG.error("Error in BoundaryAction at Delete Boundary", e);
                }

            }
        } catch (final Exception e) {
            target = "error";
            message = "Error occurred while doing Boundary Admin operation";
            LOG.error("Error in BoundaryAction", e);
        }

        finally {
            if ("error".equals(target)) {
                throw new EGOVRuntimeException(message);
            }
            req.setAttribute("MESSAGE", message);           
            this.resetToken(req);
        }

        return mapping.findForward(target);

    }

    /**
     * Used to create a Boundary object from BoundaryForm and HttpSession Object
     * @param boundaryForm
     * @param session
     * @return {@link Boundary}
     * @throws Exception
     **/
    private Boundary createBoundaryObject(final BoundryForm boundaryForm, final HttpSession session) throws Exception {

        final Boundary boundary = new BoundaryImpl();

        if (session.getAttribute("boundaryNum") != null) {
            final BigInteger boundaryNum = (BigInteger) session.getAttribute("boundaryNum");
            boundary.setBoundaryNum(boundaryNum);
        }
        if ((boundaryForm.getBoundaryNum() != null) && !boundaryForm.getBoundaryNum().equals("0") && !boundaryForm.getBoundaryNum().trim().equals("")) {
            boundary.setBoundaryNum(new BigInteger(boundaryForm.getBoundaryNum()));
        }
        if ((boundaryForm.getName() != null) && !boundaryForm.getName().equals("")) {
            boundary.setName(boundaryForm.getName());
        } else {
            final String name = (String) session.getAttribute("name");
            boundary.setName(name);
            session.removeAttribute("name");
        }

        if (boundaryForm.getBndryTypeHeirarchyLevel() != null) {
            boundary.setBndryTypeHeirarchyLevel(boundaryForm.getBndryTypeHeirarchyLevel());
        }

        if ((boundaryForm.getParentBoundaryNum() != null) && !boundaryForm.getParentBoundaryNum().trim().equals("")) {
            boundary.setParentBoundaryNum(new BigInteger(boundaryForm.getParentBoundaryNum()));
        }
        if (session.getAttribute("boundaryNum") != null) {
            final BigInteger boundaryNum = (BigInteger) session.getAttribute("boundaryNum");
            boundary.setParentBoundaryNum(boundaryNum);
            session.removeAttribute("boundaryNum");
        }
        if (boundaryForm.getTopLevelBoundaryID() != null) {
            boundary.setTopLevelBoundaryID(boundaryForm.getTopLevelBoundaryID());
        }

        if (session.getAttribute("bndryTypeHeirarchyLevel") != null) {
            final int bndryTypeHeirarchyLevel = (Integer) session.getAttribute("bndryTypeHeirarchyLevel");
            boundary.setBndryTypeHeirarchyLevel(bndryTypeHeirarchyLevel);
            session.removeAttribute("bndryTypeHeirarchyLevel");
        }
        if (boundaryForm.getCityNameLocal() != "") {
            boundary.setBndryNameLocal(boundaryForm.getCityNameLocal());
        }

        final SimpleDateFormat dateFormater = new SimpleDateFormat("MM/dd/yyyy");
        if ((boundaryForm.getFromDate() != null) && !boundaryForm.getFromDate().equals("")) {
            String fromDate = boundaryForm.getFromDate();
            fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
            boundary.setFromDate(dateFormater.parse(fromDate));
        }

        if ((boundaryForm.getToDate() != null) && !boundaryForm.getToDate().equals("")) {
            String toDate = boundaryForm.getToDate();
            toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
            boundary.setToDate(dateFormater.parse(toDate));
        }
        return boundary;

    }
}
