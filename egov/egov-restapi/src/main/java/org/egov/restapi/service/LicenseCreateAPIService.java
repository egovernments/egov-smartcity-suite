/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.restapi.service;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.repository.BoundaryRepository;
import org.egov.infra.admin.master.repository.BoundaryTypeRepository;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.restapi.web.contracts.tradelicense.LicenseCreateRequest;
import org.egov.tl.entity.License;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.repository.LicenseCategoryRepository;
import org.egov.tl.repository.LicenseSubCategoryRepository;
import org.egov.tl.repository.NatureOfBusinessRepository;
import org.egov.tl.service.LicenseApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@Transactional(readOnly = true)
public class LicenseCreateAPIService {

    @Autowired
    private LicenseApplicationService licenseApplicationService;
    @Autowired
    private BoundaryRepository boundaryRepository;
    @Autowired
    private LicenseCategoryRepository licenseCategoryRepository;
    @Autowired
    private LicenseSubCategoryRepository licenseSubCategoryRepository;
    @Autowired
    private NatureOfBusinessRepository natureOfBusinessRepository;
    @Autowired
    private BoundaryTypeRepository boundaryTypeRepository;
    @Autowired
    private CrossHierarchyService crossHierarchyService;

    public License createLicense(LicenseCreateRequest license) {
        TradeLicense tradeLicense = new TradeLicense();
        Licensee licensee = new Licensee();
        licensee.setMobilePhoneNumber(license.getMobilePhoneNumber());
        if (isNotBlank(license.getAadhaarNumber()))
            licensee.setUid(license.getAadhaarNumber());
        licensee.setApplicantName(license.getApplicantName());
        licensee.setFatherOrSpouseName(license.getFatherOrSpouseName());
        licensee.setAddress(license.getLicenseeAddress());
        licensee.setEmailId(license.getEmailId());
        tradeLicense.setNewWorkflow(true);
        tradeLicense.setLicensee(licensee);
        tradeLicense.setApplicationDate(new Date());
        tradeLicense.setAddress(license.getTradeAddress());
        tradeLicense.setNameOfEstablishment(license.getTradeTitle());
        tradeLicense.setTradeArea_weight(license.getTradeMeasure());
        tradeLicense.setCommencementDate(license.getCommencementDate());
        tradeLicense.setNameOfEstablishment(license.getApplicantName());
        tradeLicense.setOwnershipType(license.getOwnershipType());
        if (isNotBlank(license.getAssessmentNo()))
            tradeLicense.setAssessmentNo(license.getAssessmentNo());
        if (isNotBlank(license.getRemarks()))
            tradeLicense.setRemarks(license.getRemarks());
        if (license.getAgreementDate() != null && isNotBlank(license.getAgreementDocNo())) {
            tradeLicense.setAgreementDate(license.getAgreementDate());
            tradeLicense.setAgreementDocNo(license.getAgreementDocNo());
        }
        tradeLicense.setNatureOfBusiness(natureOfBusinessRepository.findOne(license.getNatureOfBusiness()));

        BoundaryType locality = boundaryTypeRepository.findByNameAndHierarchyTypeName("Locality", "LOCATION");
        Boundary childBoundary = boundaryRepository.findByBoundaryTypeAndBoundaryNum(locality, license.getBoundary());
        BoundaryType blockType = boundaryTypeRepository.findByNameAndHierarchyTypeName("Block", "REVENUE");
        List<Boundary> blocks = crossHierarchyService.getParentBoundaryByChildBoundaryAndParentBoundaryType(childBoundary.getId(), blockType.getId());
        blocks.stream().forEach(boundary -> {
            if (boundary.getParent().getBoundaryNum().equals(license.getParentBoundary()))
                tradeLicense.setParentBoundary(boundary.getParent());
        });
        tradeLicense.setBoundary(childBoundary);
        tradeLicense.setCategory(licenseCategoryRepository.findByCodeIgnoreCase(license.getCategory()));
        tradeLicense.setTradeName(licenseSubCategoryRepository.findByCode(license.getSubCategory()));
        return licenseApplicationService.create(tradeLicense, new WorkflowBean());
    }
}
