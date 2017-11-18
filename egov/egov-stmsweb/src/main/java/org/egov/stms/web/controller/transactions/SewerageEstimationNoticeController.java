/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.web.controller.transactions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.stms.notice.service.SewerageNoticeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageEstimationNoticeController {

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private SewerageNoticeService sewerageNoticeService;

    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @RequestMapping(value = "/estimationnotice", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateEstimationNotice(final HttpServletRequest request) {
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(request.getParameter("pathVar"));
        return generateReport(sewerageApplicationDetails);
    }

    private ResponseEntity<byte[]> generateReport(final SewerageApplicationDetails sewerageApplicationDetails) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=EstimationNotice.pdf");
        ReportOutput reportOutput = sewerageNoticeService.generateReportOutputDataForEstimation(sewerageApplicationDetails);
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downLoadFieldInspectionAttachment(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        final int BUFFER_SIZE = 4096;
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(request.getParameter("applicationNumber"));
        final ServletContext context = request.getServletContext();
        final File downloadFile = fileStoreService.fetch(sewerageApplicationDetails.getFieldInspections().get(0)
                .getFileStore().getFileStoreId(), SewerageTaxConstants.FILESTORE_MODULECODE);
        final FileInputStream inputStream = new FileInputStream(downloadFile);

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
        final String headerValue = String.format("attachment; filename=\"%s\"", sewerageApplicationDetails
                .getFieldInspections().get(0).getFileStore().getFileName());
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

    @RequestMapping(value = "/closeConnectionNotice", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateCloserNotice(final HttpServletRequest request,
            final HttpSession session) {
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(request.getParameter("pathVar"));
        return generateCloseConnectionReport(sewerageApplicationDetails, session);
    }

    private ResponseEntity<byte[]> generateCloseConnectionReport(final SewerageApplicationDetails sewerageApplicationDetails,
            final HttpSession session) {
        final HttpHeaders headers = new HttpHeaders();
        ReportOutput reportOutput = null;
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=CloseConnectionNotice.pdf");
        reportOutput = sewerageNoticeService.generateReportOutputForSewerageCloseConnection(sewerageApplicationDetails, session);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}
