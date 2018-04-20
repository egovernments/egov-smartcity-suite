/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.tl.service;

import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseDocument;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Transactional
    public void update(TradeLicense license) {
        licenseRepository.save(license);
    }

    public License getLicenseById(Long id) {
        return licenseRepository.findOne(id);
    }

    public License getLicenseByApplicationNumber(String applicationNumber) {
        return licenseRepository.findByApplicationNumber(applicationNumber);
    }

    public License getLicenseByOldLicenseNumber(String oldLicenseNumber) {
        return licenseRepository.findByOldLicenseNumber(oldLicenseNumber);
    }

    public boolean validateMandatoryDocument(TradeLicense license) {

        List<LicenseDocument> supportDocs = license.getLicenseDocuments()
                .stream()
                .filter(licenseDocument -> licenseDocument.getType().isMandatory()
                        && licenseDocument.getMultipartFiles().stream().anyMatch(MultipartFile::isEmpty))
                .collect(Collectors.toList());

        List<LicenseDocument> existingDocs = new ArrayList<>();
        if (license.getDocuments().stream().anyMatch(licenseDocument -> !licenseDocument.getFiles().isEmpty())) {
            existingDocs.addAll(license.getDocuments()
                    .stream()
                    .filter(licenseDocument -> licenseDocument.getType().getApplicationType().equals
                            (ApplicationType.valueOf(license.getLicenseAppType().getName().toUpperCase())) && licenseDocument.getId() != null)
                    .collect(Collectors.toList()));
        }

        List<String> supportDocType = supportDocs
                .stream().map(LicenseDocument::getType).map(LicenseDocumentType::getName).collect(Collectors.toList());

        List<String> existingDocsType = existingDocs
                .stream().map(LicenseDocument::getType).map(LicenseDocumentType::getName).collect(Collectors.toList());

        return !supportDocs.isEmpty() &&
                supportDocs.stream()
                        .anyMatch(licenseDocument -> licenseDocument.getMultipartFiles().stream().anyMatch(MultipartFile::isEmpty))
                && (existingDocs.isEmpty() || !supportDocType.stream().filter(
                licenseDocumentType -> !existingDocsType.contains(licenseDocumentType)).collect(Collectors.toList()).isEmpty());
    }
}
