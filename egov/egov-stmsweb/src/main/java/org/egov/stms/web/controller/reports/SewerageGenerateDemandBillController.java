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

package org.egov.stms.web.controller.reports;

import static org.egov.stms.utils.constants.SewerageTaxConstants.FILESTORE_MODULECODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.NOTICE_TYPE_DEMAND_BILL_NOTICE;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.stms.notice.entity.SewerageNotice;
import org.egov.stms.notice.service.SewerageNoticeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDCBReporService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/reports")
public class SewerageGenerateDemandBillController {

    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private SewerageDCBReporService sewerageDCBReportService;

    @Autowired
    private SewerageNoticeService sewerageNoticeService;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private SewerageDemandService sewerageDemandService;

    @Autowired
    private MessageSource messageSource;

    private static final Logger LOGGER = Logger.getLogger(SewerageGenerateDemandBillController.class);

    @RequestMapping(value = "/generate-sewerage-demand-bill/{consumernumber}/{assessmentnumber}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generateSewerageDemandBill(@PathVariable final String consumernumber,
            @PathVariable final String assessmentnumber, final HttpServletRequest request) {
        ReportOutput reportOutput = new ReportOutput();
        final HttpHeaders headers = new HttpHeaders();
        SewerageNotice sewerageNotice = null;
        SewerageApplicationDetails sewerageApplicationDetails =null;
        String errorMessage = "";
        if (consumernumber != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(consumernumber);

        if(sewerageApplicationDetails!=null && sewerageApplicationDetails.getApplicationNumber()!=null){
            sewerageNotice = sewerageNoticeService.findByNoticeTypeAndApplicationNumber(NOTICE_TYPE_DEMAND_BILL_NOTICE,
                    sewerageApplicationDetails.getApplicationNumber());
            // GET DEMAND BILL IF ALREADY SAVED
            if (sewerageNotice != null && sewerageNotice.getFileStore() != null) {
    
                final FileStoreMapper fmp = sewerageNotice.getFileStore();
                if (fmp != null) {
                    reportOutput = new ReportOutput();
                    final File file = fileStoreService.fetch(fmp, FILESTORE_MODULECODE);
                    try {
                        reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
                    } catch (final IOException e) {
                        LOGGER.error("Error in loading sewerage demand bill" + e.getMessage(), e);
                    }
                    reportOutput.setReportFormat(ReportFormat.PDF);
                }
            } else if (sewerageDemandService
                    .checkAnyTaxIsPendingToCollectExcludingAdvance(sewerageApplicationDetails.getCurrentDemand()))
                reportOutput = sewerageDCBReportService.generateAndSaveDemandBillNotice(sewerageApplicationDetails,
                        sewerageThirdPartyServices.getPropertyDetails(assessmentnumber, request));
            else {
                errorMessage = messageSource.getMessage("err.demandbill.demandpaid", new String[] { "" }, null);
                return redirect(errorMessage);
            }
            if (reportOutput != null && reportOutput.getReportOutputData() != null) {
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
                headers.add("content-disposition", "inline;filename=DemandBill.pdf");
            }
        }
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    private ResponseEntity<byte[]> redirect(String errorMessage) {
        errorMessage = "<html><body><p style='color:red;border:1px solid gray;padding:15px;'>" + errorMessage
                + "</p></body></html>";
        final byte[] byteData = errorMessage.getBytes();
        errorMessage = "";
        return new ResponseEntity<byte[]>(byteData, HttpStatus.CREATED);
    }
}
