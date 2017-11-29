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

package org.egov.tl.web.controller.transactions.legacy;

import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.repository.LicenseRepository;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.LegacyLicenseService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;

@Controller
public class LegacyLicenseController extends GenericWorkFlowController {

    @Autowired
    protected TradeLicenseService tradeLicenseService;

    @Autowired
    protected FeeTypeService feeTypeService;

    @Autowired
    protected BoundaryService boundaryService;

    @Autowired
    protected LicenseCategoryService licenseCategoryService;

    @Autowired
    protected LicenseRepository licenseRepository;

    @Autowired
    protected LegacyLicenseService legacyService;

    @ModelAttribute("boundary")
    public List<Boundary> boundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE);
    }

    @ModelAttribute("ownershipType")
    public Map<String, String> ownership() {
        return Constants.getOwnershipTypes();
    }

    @ModelAttribute("natureOfBusiness")
    public List<NatureOfBusiness> natureOfBusiness() {
        return tradeLicenseService.getAllNatureOfBusinesses();
    }

    @ModelAttribute("category")
    public List<LicenseCategory> category() {
        return licenseCategoryService.getCategories();
    }

    @ModelAttribute("documentTypes")
    public List<LicenseDocumentType> documentsList() {
        return tradeLicenseService.getDocumentTypesByApplicationType(ApplicationType.NEW);
    }

    @ModelAttribute("feeTypeId")
    public Long feeType() {
        return feeTypeService.findByName(Constants.LICENSE_FEE_TYPE).getId();
    }

}
