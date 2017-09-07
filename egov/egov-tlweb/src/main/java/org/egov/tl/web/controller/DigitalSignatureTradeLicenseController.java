/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.tl.web.controller;

import org.apache.commons.io.FileUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Controller
@RequestMapping(value = "/digitalSignature")
public class DigitalSignatureTradeLicenseController {

    @Qualifier("fileStoreService")
    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private TradeLicenseService tradeLicenseService;

    @RequestMapping(value = "/tradeLicense/transitionWorkflow")
    public String transitionWorkflow(HttpServletRequest request, Model model) {
        String fileStoreIds = request.getParameter("fileStoreId");
        HttpSession session = request.getSession();

        Map<String, String> appNoFileStoreIdsMap = (Map<String, String>) session
                .getAttribute(Constants.FILE_STORE_ID_APPLICATION_NUMBER);

        tradeLicenseService.digitalSignTransition(fileStoreIds, appNoFileStoreIdsMap);
        model.addAttribute("successMessage", "Digitally Signed Successfully");
        model.addAttribute("fileStoreId", !fileStoreIds.isEmpty() ? fileStoreIds : "");
        model.addAttribute("applnum", appNoFileStoreIdsMap.get(fileStoreIds));
        return "digitalSignature-success";
    }


    @RequestMapping(value = "/tradeLicense/downloadSignedLicenseCertificate", produces = APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadSignedLicenseCertificate(@RequestParam String file, @RequestParam String applnum) {

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

}
