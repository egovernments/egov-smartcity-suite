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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.stms.notice.entity.SewerageNotice;
import org.egov.stms.notice.service.SewerageNoticeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageWorkOrderNoticeController {

    @Autowired
    private MessageSource messageSource;

  
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private SewerageNoticeService sewerageNoticeService;

    @RequestMapping(value = "/workordernotice", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> createWorkOrderReport(final HttpServletRequest request,
            final HttpSession session) throws IOException {
        String errorMessage = "";
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(request.getParameter("pathVar"));
        if (!errorMessage.isEmpty())
            return redirect();
        return generateReport(sewerageApplicationDetails, session, request);
    }

    private ResponseEntity<byte[]> generateReport(final SewerageApplicationDetails sewerageApplicationDetails,
            final HttpSession session, final HttpServletRequest request) throws IOException {
        final HttpHeaders headers = new HttpHeaders();
        ReportOutput reportOutput = new ReportOutput();
        InputStream generateNoticePDF;
        SewerageNotice sewerageNotice = sewerageNoticeService.findByNoticeNoAndNoticeType(
                sewerageApplicationDetails.getWorkOrderNumber(), SewerageTaxConstants.NOTICE_TYPE_WORK_ORDER_NOTICE);
        if (sewerageNotice != null && sewerageNotice.getFileStore() != null) {
            final FileStoreMapper fmp = sewerageNotice.getFileStore();
            final File file = fileStoreService.fetch(fmp, SewerageTaxConstants.FILESTORE_MODULECODE);
            reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
            reportOutput.setReportFormat(FileFormat.PDF);
        } else {
            reportOutput = sewerageNoticeService.generateReportOutputForWorkOrder(sewerageApplicationDetails, session, request);
            if (reportOutput != null && reportOutput.getReportOutputData() != null) {
                generateNoticePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
                sewerageNotice = sewerageNoticeService.saveWorkOrderNotice(sewerageApplicationDetails, generateNoticePDF);
                if (sewerageNotice != null) {
                    sewerageApplicationDetails.addNotice(sewerageNotice);
                    sewerageApplicationDetailsService.save(sewerageApplicationDetails);
                }
            }
        }
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=WorkOrderNotice.pdf");
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    private String validateWorkOrder(final SewerageApplicationDetails sewerageApplicationDetails, final Boolean isView) {
        String errorMessage=null;
       if(sewerageApplicationDetails!=null) { 
        if (sewerageApplicationDetails.getConnection().getLegacy())
            errorMessage = messageSource.getMessage("err.validate.workorder.for.legacy", new String[] { "" }, null);
        else if (isView && null == sewerageApplicationDetails.getWorkOrderNumber())
            return buildErrorMessage(sewerageApplicationDetails);
        else if (!isView
                && !sewerageApplicationDetails.getStatus().getCode()
                        .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_WOGENERATED))
           return buildErrorMessage(sewerageApplicationDetails);
       }
        return errorMessage;
    }

    private String buildErrorMessage(final SewerageApplicationDetails sewerageApplicationDetails) {
        String errorMessage;
        errorMessage = messageSource.getMessage("err.validate.workorder.view",
                new String[] { sewerageApplicationDetails.getApplicationNumber() }, null);
        return errorMessage;
    }

    @RequestMapping(value = "/workorder/view/{applicationNumber}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> viewReport(@PathVariable final String applicationNumber,
            final HttpSession session, final HttpServletRequest request) throws IOException {
        String errorMessage ;
        final SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(applicationNumber);
        errorMessage= validateWorkOrder(sewerageApplicationDetails, true);
        if (errorMessage!=null && !errorMessage.isEmpty())
            return redirect();
        return generateReport(sewerageApplicationDetails, session, request);
    }

    private ResponseEntity<byte[]> redirect() {
        String errorMessage = "";
        errorMessage = "<html><body><p style='color:red;border:1px solid gray;padding:15px;'>" + errorMessage
                + "</p></body></html>";
        final byte[] byteData = errorMessage.getBytes();
        errorMessage = "";
        return new ResponseEntity<byte[]>(byteData, HttpStatus.CREATED);
    }

}
