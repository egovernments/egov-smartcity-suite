/*
 * @(#)BeforeBoundaryAction.java 3.0, 18 Jun, 2013 1:46:12 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.EgovAction;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BeforeBoundaryAction extends EgovAction {
    private static final Logger LOG = LoggerFactory.getLogger(BeforeBoundaryAction.class);

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        try {
            final BoundryForm boundryForm = (BoundryForm) form;
            //req.setAttribute("boundariesList", new BoundaryDAO(null).getAllBoundariesByBndryTypeId(Integer.valueOf(boundryForm.getName())));
            //req.setAttribute("boundryTypeObj", new BoundaryTypeDAO(null).getBoundaryType(Integer.valueOf(boundryForm.getName())));
            saveToken(req);
            return mapping.findForward("success");
        } catch (final Exception ex) {
            LOG.error("Error occurred while doing Boundary operation.",ex);
            throw new EGOVRuntimeException("Error occurred while doing Boundary operation.");
        }

    }
}
