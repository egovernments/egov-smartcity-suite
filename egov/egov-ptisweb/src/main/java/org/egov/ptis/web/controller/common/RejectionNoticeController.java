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

package org.egov.ptis.web.controller.common;

import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_REJECTION;
import static org.egov.ptis.constants.PropertyTaxConstants.REJECTION_NOTICE_TEMPLATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
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
@RequestMapping(value = { "/rejectionnotice" })

public class RejectionNoticeController {
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
    private BasicPropertyDAO basicPropertyDAO;

    @RequestMapping(value = "/generaterejectionnotice", method = RequestMethod.GET)
    public String generateRejectionNotice(final Model model, final HttpServletRequest request,
            final HttpSession session) {
        ReportOutput reportOutput;
        BasicProperty basicProperty;
        String ulbCode = "";
        String fileStoreIds = null;
        String assessmentNo = request.getParameter("assessmentNo");
        String workFlowAction = request.getParameter("actionType");
        String approvalComent = request.getParameter("approvalComment");
        String applicationNumber = request.getParameter("applicationNumber");
        String serviceName = request.getParameter("transactionType");
        String stateId = request.getParameter("stateId");
        boolean digitalSignEnabled;
        InputStream noticePdf = null;
        String noticeNo;
        javax.persistence.Query qry;
        final Property property;
        ulbCode = ApplicationThreadLocals.getCityCode();
        digitalSignEnabled = propertyTaxCommonUtils.isDigitalSignatureEnabled();
        if (serviceName.equalsIgnoreCase(NATURE_NEW_ASSESSMENT)) {
            qry = entityManager.createQuery("from PropertyImpl P where P.id =:id");
            qry.setParameter("id", Long.valueOf(assessmentNo));
            property = (Property) qry.getSingleResult();
            basicProperty = property.getBasicProperty();
        } else
            basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        final PtNotice notice = noticeService.getNoticeByNoticeTypeAndApplicationNumber(NOTICE_TYPE_REJECTION,
                applicationNumber);
        qry = entityManager.createQuery("from StateHistory sh where sh.state.id =:state order by id desc");
        qry.setParameter("state", Long.valueOf(stateId));
        List<StateHistory> stateHistory = qry.getResultList();
        for (StateHistory<Position> history : stateHistory)
            if (approvalComent == null && history.getOwnerPosition().getName().contains(COMMISSIONER_DESGN))
                if (!history.getComments().isEmpty() && history.getState().getComments().isEmpty()) {
                    approvalComent = history.getComments();
                    break;
                } else
                    approvalComent = history.getState().getComments();
        if (WFLOW_ACTION_STEP_SIGN.equals(workFlowAction) && notice == null) {
            noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(NOTICE_TYPE_REJECTION);
            reportOutput = generateReport(basicProperty, request, approvalComent, applicationNumber,
                    serviceName);
            if (reportOutput != null && reportOutput.getReportOutputData() != null)
                noticePdf = new ByteArrayInputStream(reportOutput.getReportOutputData());
            final PtNotice savedNotice = noticeService.saveNotice(applicationNumber, noticeNo,
                    NOTICE_TYPE_REJECTION, basicProperty, noticePdf);
            fileStoreIds = savedNotice.getFileStore().getFileStoreId();
        } else if (workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_PREVIEW))
            return "redirect:/rejectionnotice/generaterejectionnotice/preview/" + assessmentNo + "?serviceName=" + serviceName
                    + "&approvalComent=" + approvalComent + "&applicationNumber=" + applicationNumber;
        model.addAttribute("ulbCode", ulbCode);
        model.addAttribute("fileStoreIds", fileStoreIds);
        model.addAttribute("digitalSignEnabled", digitalSignEnabled);
        if (WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction))
            return "digital-signature";

        else
            return "redirect:/rejectionnotice/generaterejectionnotice/proceedings/" + assessmentNo + "?workFlowAction="
                    + workFlowAction + "&approvalComent=" + approvalComent + "&applicationNumber=" + applicationNumber
                    + "&serviceName=" + serviceName;
    }

    @RequestMapping(value = { "/generaterejectionnotice/preview/{assessmentNo}",
            "/generaterejectionnotice/proceedings/{assessmentNo}" }, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> generateNotice(final Model model, final HttpServletRequest request,
            @PathVariable final String assessmentNo, final String applicationNumber, String serviceName) {
        ReportOutput noticeOutput = new ReportOutput();
        String fileName;
        BasicProperty basicProperty;
        javax.persistence.Query qry;
        final Property property;
        String remarks = request.getParameter("approvalComent");
        if (serviceName.equalsIgnoreCase(NATURE_NEW_ASSESSMENT)) {
            qry = entityManager.createQuery("from PropertyImpl P where P.id =:id");
            qry.setParameter("id", Long.valueOf(assessmentNo));
            property = (Property) qry.getSingleResult();
            basicProperty = property.getBasicProperty();
        } else
            basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);

        final PtNotice notice = noticeService.getNoticeByNoticeTypeAndApplicationNumber(NOTICE_TYPE_REJECTION,
                applicationNumber);
        if (notice != null) {
            final FileStoreMapper fsm = notice.getFileStore();
            final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
            byte[] bFile;
            try {
                bFile = FileUtils.readFileToByteArray(file);
            } catch (final IOException e) {

                throw new ApplicationRuntimeException("Exception while generating Rejection Notcie : " + e);
            }
            noticeOutput.setReportOutputData(bFile);
            noticeOutput.setReportFormat(ReportFormat.PDF);
            fileName = notice.getNoticeNo();
        } else {
            noticeOutput = generateReport(basicProperty, request, remarks, applicationNumber, serviceName);
            fileName = NOTICE_TYPE_REJECTION.concat(assessmentNo);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=" + fileName + ".pdf");
        return new ResponseEntity<>(noticeOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    public ReportOutput generateReport(final BasicProperty basicProperty, final HttpServletRequest request,
            final String remarks, String applicationNumber,
            String serviceName) {
        ReportOutput reportOutput = null;
        if (basicProperty != null)
            reportOutput = reportService
                    .createReport(generateRejectionReportRequest(basicProperty, request, remarks,
                            applicationNumber, serviceName));
        return reportOutput;
    }

    public ReportRequest generateRejectionReportRequest(final BasicProperty basicProperty, HttpServletRequest request,
            final String remarks, final String applicationNumber, String serviceName) {
        ReportRequest reportInput = null;
        PropertyAckNoticeInfo noticeInfo = new PropertyAckNoticeInfo();
        if (basicProperty != null) {
            final Map<String, Object> reportParams = new HashMap<>();
            final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
            reportParams.put("cityName", cityName);
            reportParams.put("remarks", remarks);
            reportParams.put("applicationNo", applicationNumber);
            reportParams.put("serviceName", serviceName);
            reportParams.put("applicantName", basicProperty.getFullOwnerName());
            noticeInfo.setAssessmentNo(basicProperty.getUpicNo());
            reportInput = new ReportRequest(REJECTION_NOTICE_TEMPLATE, noticeInfo, reportParams);
        }
        if (reportInput != null) {
            reportInput.setPrintDialogOnOpenReport(true);
            reportInput.setReportFormat(ReportFormat.PDF);
        }
        return reportInput;
    }

}
