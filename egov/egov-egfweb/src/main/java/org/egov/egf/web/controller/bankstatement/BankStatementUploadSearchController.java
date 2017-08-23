/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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

package org.egov.egf.web.controller.bankstatement;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.egov.egf.commons.bank.service.CreateBankService;
import org.egov.egf.commons.bankbranch.service.CreateBankBranchService;
import org.egov.egf.web.actions.brs.AutoReconcileHelper;
import org.egov.egf.web.controller.bankstatement.adaptor.DocumentUploadJsonAdaptor;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.model.bills.DocumentUpload;
import org.egov.model.brs.BankStatementUploadFile;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("/bankstatement")
public class BankStatementUploadSearchController {

    private static final int BUFFER_SIZE = 4096;
    @Autowired
    private CreateBankService createBankService;
    @Autowired
    private CreateBankBranchService createBankBranchService;
    @Autowired
    private AutoReconcileHelper autoReconcileHelper;
    @Autowired
    private FileStoreService fileStoreService;

    private void setDropDownValues(final Model model) {
        model.addAttribute("banks", createBankService.getByIsActiveTrueOrderByName());
        model.addAttribute("bankbranches", createBankBranchService.getByIsActiveTrueOrderByBranchname());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(final Model model) {
        setDropDownValues(model);
        model.addAttribute("bankStatementUploadFile", new BankStatementUploadFile());
        return "bankstatement-search";

    }

    @RequestMapping(value = "/ajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(final Model model, @ModelAttribute final BankStatementUploadFile bankStatementUploadFile) {

        List<DocumentUpload> list = autoReconcileHelper.getUploadedFiles(bankStatementUploadFile);
        return new StringBuilder("{ \"data\":")
                .append(toSearchResultJson(list)).append("}")
                .toString();
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DocumentUpload.class, new DocumentUploadJsonAdaptor()).create();
        return gson.toJson(object);
    }

    @RequestMapping(value = "/downloadDoc", method = RequestMethod.GET)
    public void getBillDoc(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        final ServletContext context = request.getServletContext();
        final String fileStoreId = request.getParameter("fileStoreId");
        String fileName = "";
        final File downloadFile = fileStoreService.fetch(fileStoreId, FinancialConstants.FILESTORE_MODULECODE);
        final FileInputStream inputStream = new FileInputStream(downloadFile);

        DocumentUpload doc = autoReconcileHelper.getDocumentsByFileStoreId(fileStoreId);
        if (doc.getFileStore().getFileStoreId().equalsIgnoreCase(fileStoreId))
            fileName = doc.getFileStore().getFileName();

        // get MIME type of the file
        String mimeType = context.getMimeType(downloadFile.getAbsolutePath());
        if (mimeType == null)
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";

        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        // set headers for the response
        final String headerKey = "Content-Disposition";
        final String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader(headerKey, headerValue);

        // get output stream of the response
        final OutputStream outStream = response.getOutputStream();

        final byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1)
            outStream.write(buffer, 0, bytesRead);

        inputStream.close();
        outStream.close();
    }

}
