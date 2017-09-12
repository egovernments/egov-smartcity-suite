/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
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
package org.egov.ptis.web.controller.common;

import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_NEW_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_ENDORSEMENT;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.utils.WebUtils;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/endorsementNotice")
public class EndorsementController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReportService reportService;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

    public static final String ENDORSEMENT_NOTICE = "EndorsementNotice";

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<byte[]> getEndorsementNotice(final Model model,
            @RequestParam final String applicantName, @RequestParam final String serviceName, @RequestParam final String remarks,
            @RequestParam final String assessmentNo, @RequestParam final String applicationNo,
            final HttpServletRequest request) throws UnsupportedEncodingException {
        final Map<String, Object> reportParams = new HashMap<>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput;
        InputStream noticePDF = null;
        BasicProperty basicProperty;
        javax.persistence.Query qry;
        final Property property;
        if (serviceName.equalsIgnoreCase(APPLICATION_TYPE_NEW_ASSESSENT)) {
            qry = entityManager.createQuery("from PropertyImpl P where P.id =?");
            qry.setParameter(1, Long.valueOf(assessmentNo));
            property = (Property) qry.getSingleResult();
            basicProperty = property.getBasicProperty();
        } else
            basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        final String noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(NOTICE_TYPE_ENDORSEMENT);
        if (remarks != null) {
            final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            PropertyAckNoticeInfo ackBean = new PropertyAckNoticeInfo();
            final String url = WebUtils.extractRequestDomainURL(request, false);
            final String cityLogo = url.concat(PropertyTaxConstants.IMAGE_CONTEXT_PATH).concat(
                    (String) request.getSession().getAttribute("citylogo"));
            final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
            reportParams.put("logoPath", cityLogo);
            reportParams.put("cityName", cityName);
            reportParams.put("noticeDate", new DateTime().toString(formatter));
            reportParams.put("serviceName", serviceName.replace('_', ' '));
            reportParams.put("applicantName", applicantName.replace("&amp;", "&"));
            reportParams.put("remarks", remarks);
            reportParams.put("applicationNo", applicationNo);
            reportParams.put("upicno", basicProperty.getUpicNo());
            reportParams.put("noticeNumber", noticeNo);
            reportInput = new ReportRequest(ENDORSEMENT_NOTICE, ackBean, reportParams);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition",
                "inline;filename=" + NOTICE_TYPE_ENDORSEMENT + "_" + basicProperty.getUpicNo() + ".pdf");
        if(reportInput != null){
        reportInput.setPrintDialogOnOpenReport(true);
        reportInput.setReportFormat(ReportFormat.PDF);
        }
        reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            noticePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        noticeService.saveNotice(applicationNo, noticeNo, NOTICE_TYPE_ENDORSEMENT,
                basicProperty, noticePDF);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

}
