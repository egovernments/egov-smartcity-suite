/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) <2015>  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *  	1) All versions of this program, verbatim or modified must carry this
 *  	   Legal Notice.
 *
 *  	2) Any misrepresentation of the origin of the material is prohibited. It
 *  	   is required that all modified versions of this material be marked in
 *  	   reasonable ways as different from the original version.
 *
 *  	3) This license does not grant any rights to any user of the program
 *  	   with regards to rights under trademark law for use of the trade names
 *  	   or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.domain.web;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.tradelicense.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

@Result(
        name = Action.SUCCESS, type = "redirect", location = "CommonTradeLicenseAjaxAction.action")
@Results({ @Result(
        name = "AJAX_RESULT", type = "redirect", location = "returnStream", params = { "contentType", "text/plain" }) })
@ParentPackage("egov")
public class CommonTradeLicenseAjaxAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CommonTradeLicenseAjaxAction.class);
    protected LicenseUtils licenseUtils;
    private int zoneId;
    private List<Boundary> divisionList = new LinkedList<Boundary>();
    @Autowired
    private BoundaryService boundaryService;

    /**
     * Populate wards.
     *
     * @return the string
     */
    public String populateDivisions() {
        try {
            final Boundary boundary = boundaryService.getBoundaryById(Long.valueOf(zoneId));
            final String cityName = licenseUtils.getAllCity().get(0).getName();
            if (!boundary.getName().equals(cityName))
                divisionList = boundaryService.getChildBoundariesByBoundaryId(Long.valueOf(zoneId));
        } catch (final Exception e) {
            LOGGER.error("populateDivisions() - Error while loading divisions ." + e.getMessage());
            addFieldError("divisions", "Unable to load division information");
            throw new EGOVRuntimeException("Unable to load division information", e);
        }
        return "ward";
    }

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Convenience method to get the response
     *
     * @return current response
     */

    public HttpServletResponse getServletResponse() {
        return ServletActionContext.getResponse();
    }

    public LicenseUtils getLicenseUtils() {
        return licenseUtils;
    }

    public void setLicenseUtils(final LicenseUtils licenseUtils) {
        this.licenseUtils = licenseUtils;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(final int zoneId) {
        this.zoneId = zoneId;
    }

    public List<Boundary> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(final List<Boundary> divisionList) {
        this.divisionList = divisionList;
    }

}
