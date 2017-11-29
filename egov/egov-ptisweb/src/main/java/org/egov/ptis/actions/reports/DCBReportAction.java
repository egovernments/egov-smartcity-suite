/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.ptis.actions.reports;

import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Results({ @Result(name = DCBReportAction.SEARCH, location = "dCBReport-search.jsp") })
public class DCBReportAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = -5711373030821252347L;
    private final Logger LOGGER = Logger.getLogger(getClass());
    public static final String SEARCH = "search";
    private Long zoneId;
    private Map<Long, String> wardBndryMap;
    List<Boundary> wardList;
    private String mode; // stores current mode (ex: zone / ward / block / property)
    private Long boundaryId; // stores selected boundary id
    private String selectedModeBndry; // Used to traverse back. Block -> Ward -> Zone
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;

    @Override
    public Object getModel() {

        return null;
    }

    @Override
    public void prepare() {
        wardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, REVENUE_HIERARCHY_TYPE);
        final List<PropertyTypeMaster> propTypeList = propertyTypeMasterDAO.findBuiltUpOwnerShipTypes();
        addDropdownData("PropTypeMaster", propTypeList);
        setWardBndryMap(CommonServices.getFormattedBndryMap(wardList));
        wardBndryMap.put(0l, "All");
    }

    @SkipValidation
    @Action(value = "/reports/dCBReport-search")
    public String search() {
        mode = "ward";
        return SEARCH;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Long zoneId) {
        this.zoneId = zoneId;
    }

    public Map<Long, String> getWardBndryMap() {
        return wardBndryMap;
    }

    public void setWardBndryMap(Map<Long, String> wardBndryMap) {
        this.wardBndryMap = wardBndryMap;
    }

    public List<Boundary> getWardList() {
        return wardList;
    }

    public void setWardList(List<Boundary> wardList) {
        this.wardList = wardList;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public Long getBoundaryId() {
        return boundaryId;
    }

    public void setBoundaryId(final Long boundaryId) {
        this.boundaryId = boundaryId;
    }

    public String getSelectedModeBndry() {
        return selectedModeBndry;
    }

    public void setSelectedModeBndry(final String selectedModeBndry) {
        this.selectedModeBndry = selectedModeBndry;
    }
}
