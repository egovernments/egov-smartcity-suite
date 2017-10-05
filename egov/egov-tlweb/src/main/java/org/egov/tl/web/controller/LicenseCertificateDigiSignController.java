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
package org.egov.tl.web.controller;

import org.apache.commons.io.FileUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.tl.entity.License;
import org.egov.tl.service.LicenseCertificateDigiSignService;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Controller
@RequestMapping(value = "/tradelicense")
public class LicenseCertificateDigiSignController {

    @Qualifier("fileStoreService")
    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private LicenseCertificateDigiSignService licenseCertificateDigiSignService;

    @GetMapping("/digisign-transition")
    public String licenseDigiSignTransition(@RequestParam String[] fileStoreIds,
                                            @RequestParam String[] applicationNumbers,
                                            Model model) {
        licenseCertificateDigiSignService.digitalSignTransition(Arrays.asList(applicationNumbers));
        model.addAttribute("successMessage", "Digitally Signed Successfully");
        model.addAttribute("fileStoreId", fileStoreIds.length == 1 ? fileStoreIds[0] : "");
        model.addAttribute("applnum", applicationNumbers.length == 1 ? applicationNumbers[0] : "");

        return "digitalSignature-success";
    }

    @GetMapping(value = "/download/digisign-certificate", produces = APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadSignedLicenseCertificate(@RequestParam String file,
                                                                                @RequestParam String applnum) {

        try {
            File signedFile = fileStoreService.fetch(file, Constants.FILESTORE_MODULECODE);
            byte[] bytes = FileUtils.readFileToByteArray(signedFile);

            return ResponseEntity.
                    ok().
                    contentType(MediaType.parseMediaType("application/pdf")).
                    cacheControl(CacheControl.noCache()).
                    contentLength(bytes.length).
                    header("content-disposition", "inline;filename=\"" + applnum + ".pdf\"").
                    body(new InputStreamResource(new ByteArrayInputStream(bytes)));
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Error while reading file", e);
        }
    }

    @GetMapping(value = "/bulk-digisign")
    public String showLicenseBulkDigiSignForm(Model model) {
        model.addAttribute("licenses",
                licenseCertificateDigiSignService.getLicensePendingForDigiSign());
        return "license-bulk-digisign-form";
    }

    @PostMapping(value = "/bulk-digisign")
    public String bulkDigitalSignature(@RequestParam List<Long> licenseIds, Model model) {

        List<License> licenses = licenseCertificateDigiSignService.generateLicenseCertificateForDigiSign(licenseIds);
        List<String> fileStoreIds = licenses.parallelStream()
                .map(License::getDigiSignedCertFileStoreId).collect(Collectors.toList());
        List<String> applicaitonNumbers = licenses.parallelStream()
                .map(License::getApplicationNumber).collect(Collectors.toList());

        model.addAttribute("fileStoreIds", String.join(",", fileStoreIds));
        model.addAttribute("applicationNo", String.join(",", applicaitonNumbers));
        model.addAttribute("ulbCode", ApplicationThreadLocals.getCityCode());
        return "license-bulk-digisign-forward";
    }

}
