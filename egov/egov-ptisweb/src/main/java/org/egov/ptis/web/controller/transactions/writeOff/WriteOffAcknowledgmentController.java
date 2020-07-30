
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
 */

package org.egov.ptis.web.controller.transactions.writeOff;

import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_WOPROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_WRITEOFFROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.WriteOff;
import org.egov.ptis.domain.repository.writeOff.WriteOffRepository;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.writeOff.WriteOffService;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = { "/writeoff" })
public class WriteOffAcknowledgmentController {

    @Autowired
    private WriteOffService writeOffService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    @Autowired
    private ReportService reportService;
    @Autowired
    private FileStoreService fileStoreService;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private WriteOffRepository writeOffRepository;

    public static final String SAMPLE_FILE = "WO Proceedings/";

    @RequestMapping(value = "/generatenotice", method = RequestMethod.GET)
    public String generateSpecialNotice(final Model model, final HttpServletRequest request,
            final HttpSession session) {
        ReportOutput reportOutput;
        String fileStoreIds = null;
        String assessmentno = null;
        String modelId = request.getParameter("modelId");
        if (modelId != null)
            assessmentno = writeOffRepository.getOne(Long.valueOf(modelId)).getBasicProperty().getUpicNo();
        else
            assessmentno = request.getParameter("assessmentno");
        String workFlowAction = request.getParameter("actionType");
        String appoverUser = securityUtils.getCurrentUser().getName();
        String ulbCode;
        boolean digitalSignEnabled;
        String noticeNo;
        ulbCode = ApplicationThreadLocals.getCityCode();
        digitalSignEnabled = propertyTaxCommonUtils.isDigitalSignatureEnabled();
        final WriteOff writeOff = writeOffService.getLatestSpecialNoticeGeneratedWriteOff(assessmentno);
        final PtNotice notice = noticeService.getNoticeByNoticeTypeAndApplicationNumber(NOTICE_TYPE_WRITEOFFROCEEDINGS,
                writeOff.getApplicationNumber());
        InputStream noticePdf = null;
        if (WFLOW_ACTION_STEP_SIGN.equals(workFlowAction) && notice == null) {
            noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(NOTICE_TYPE_WRITEOFFROCEEDINGS);
            reportOutput = generateReport(writeOff, request, appoverUser, noticeNo);
            if (reportOutput != null && reportOutput.getReportOutputData() != null)
                noticePdf = new ByteArrayInputStream(reportOutput.getReportOutputData());
            final PtNotice savedNotice = noticeService.saveNotice(writeOff.getApplicationNumber(), noticeNo,
                    NOTICE_TYPE_WOPROCEEDINGS, writeOff.getBasicProperty(), noticePdf);
            fileStoreIds = savedNotice.getFileStore().getFileStoreId();
        } else if (workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_PREVIEW)) {

            return "redirect:/writeoff/generatenotice/preview/" + assessmentno + "?approver=" + appoverUser;
        }
        model.addAttribute("ulbCode", ulbCode);
        model.addAttribute("fileStoreIds", fileStoreIds);
        model.addAttribute("digitalSignEnabled", digitalSignEnabled);
        if (WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction))
            return "writeoff-dig-sign";

        else
            return "redirect:/writeoff/generatenotice/proceedings/" + assessmentno + "?workFlowAction="
                    + workFlowAction;
    }

    @RequestMapping(value = { "/generatenotice/preview/{assessmentNo}",
            "/generatenotice/proceedings/{assessmentNo}" }, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> generateNotice(final Model model, final HttpServletRequest request,
            @PathVariable final String assessmentNo) {
        ReportOutput noticeOutput = new ReportOutput();
        String fileName;
        String approverName = request.getParameter("approver");
        final WriteOff writeOff = writeOffService.getLatestSpecialNoticeGeneratedWriteOff(assessmentNo);
        final PtNotice notice = noticeService.getNoticeByNoticeTypeAndApplicationNumber(NOTICE_TYPE_WRITEOFFROCEEDINGS,
                writeOff.getApplicationNumber());
        if (notice != null) {
            final FileStoreMapper fsm = notice.getFileStore();
            final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
            byte[] bFile;
            try {
                bFile = FileUtils.readFileToByteArray(file);
            } catch (final IOException e) {

                throw new ApplicationRuntimeException("Exception while generating Write Off Notcie : " + e);
            }
            noticeOutput.setReportOutputData(bFile);
            noticeOutput.setReportFormat(ReportFormat.PDF);
            fileName = notice.getNoticeNo();
        } else {
            noticeOutput = generateReport(writeOff, request, approverName, null);
            fileName = SAMPLE_FILE.concat(assessmentNo);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=" + fileName + ".pdf");
        return new ResponseEntity<>(noticeOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    public ReportOutput generateReport(final WriteOff writeOff, final HttpServletRequest request,
            final String approvedUser, final String noticeNo) {
        ReportOutput reportOutput = null;
        if (writeOff != null) {
            reportOutput = reportService
                    .createReport(writeOffService.generateWriteOffReportRequest(writeOff, noticeNo, approvedUser));
        }
        return reportOutput;
    }

}