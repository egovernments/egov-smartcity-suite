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
package org.egov.adtax.web.controller.reports;

import org.apache.commons.lang.WordUtils;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/advertisement")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;

    @RequestMapping(value = "/permitOrder", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generatePermitOrder(final HttpServletRequest request,
            final HttpSession session) {
        final String errorMessage = "";
        final String workFlowAction = "";
        final AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService
                .findBy(Long.valueOf(request.getParameter("pathVar")));

        if (!errorMessage.isEmpty())
            return redirect(errorMessage);
        return generatePermitOrder( request,advertisementPermitDetail, session, workFlowAction);
    }

    @RequestMapping(value = "/demandNotice", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateDemandNotice(final HttpServletRequest request,
            final HttpSession session) {
        final String errorMessage = "";
        String workFlowAction = "";
        final AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService
                .findBy(Long.valueOf(request.getParameter("pathVar")));
        workFlowAction = (String) session.getAttribute(AdvertisementTaxConstants.WORKFLOW_ACTION);

        if (!errorMessage.isEmpty())
            return redirect(errorMessage);
        return generateDemandNotice(request,advertisementPermitDetail, session, workFlowAction);
    }

    private ResponseEntity<byte[]> generatePermitOrder(HttpServletRequest request, final AdvertisementPermitDetail advertisementPermitDetail,
            final HttpSession session, final String workFlowAction) {
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (null != advertisementPermitDetail) {
            final Map<String, Object> reportParams = buildParametersForReport(request, advertisementPermitDetail);
            reportParams.put("advertisementtitle",
                    WordUtils.capitalize(AdvertisementTaxConstants.ADVERTISEMENTPERMITODERTITLE));
             
            reportInput = new ReportRequest(AdvertisementTaxConstants.PERMITORDER, advertisementPermitDetail,
                    reportParams);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=Permit Order.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    private void buildMeasurementDetailsForJasper(final AdvertisementPermitDetail advertisementPermitDetail,
            StringBuffer measurement, final Map<String, Object> reportParams, String NOTMENTIONED) {
        measurement.append(
                advertisementPermitDetail.getMeasurement() == null ? NOTMENTIONED : advertisementPermitDetail
                        .getMeasurement()).append(" ");

        if (advertisementPermitDetail.getMeasurement() != null)
            measurement.append(advertisementPermitDetail.getUnitOfMeasure().getDescription());

        if (advertisementPermitDetail.getLength() != null)
            measurement.append(" Length : ").append(advertisementPermitDetail.getLength());

        if (advertisementPermitDetail.getBreadth() != null)
            measurement.append(" Breadth : ").append(advertisementPermitDetail.getBreadth());

        if (advertisementPermitDetail.getTotalHeight() != null)
            measurement.append(" Height : ").append(advertisementPermitDetail.getTotalHeight());

        reportParams.put("measurement", measurement.toString());
    }

    private ResponseEntity<byte[]> generateDemandNotice(HttpServletRequest request,final AdvertisementPermitDetail advertisementPermitDetail,
            final HttpSession session, final String workFlowAction) {
        ReportRequest reportInput = null;
        ReportOutput reportOutput = null;
        if (null != advertisementPermitDetail) {
            
            final Map<String, Object> reportParams = buildParametersForReport(request, advertisementPermitDetail);
            reportParams.put("taxamount", advertisementPermitDetail.getTaxAmount() == null ? Long.valueOf(0)
                    : advertisementPermitDetail.getTaxAmount());
            reportParams.put("encroachmentfee", advertisementPermitDetail.getEncroachmentFee() == null ? Long.valueOf(0)
                    : advertisementPermitDetail.getEncroachmentFee());
            reportInput = new ReportRequest(AdvertisementTaxConstants.DEMANDNOTICE, advertisementPermitDetail, reportParams);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=Demand Notice.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    private Map<String, Object> buildParametersForReport(HttpServletRequest request,
            final AdvertisementPermitDetail advertisementPermitDetail) {
        StringBuffer measurement = new StringBuffer();
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        String NOTMENTIONED = "Not Mentioned ";

        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String cityLogo = url.concat(AdvertisementTaxConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
        final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
        reportParams.put("logoPath", cityLogo);
        reportParams.put("cityName", cityName);
        reportParams.put("advertisementtitle",
                WordUtils.capitalize(AdvertisementTaxConstants.ADVERTISEMENTDEMANDNOTICETITLE));
     
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
       //   reportParams.put("workFlowAction", workFlowAction);
        reportParams.put("advertisementnumber", advertisementPermitDetail.getAdvertisement().getAdvertisementNumber());
        reportParams.put("permitNumber", advertisementPermitDetail.getPermissionNumber());
        reportParams.put("applicationNumber", advertisementPermitDetail.getApplicationNumber());
        
        if (advertisementPermitDetail.getAgency() != null
                && org.apache.commons.lang.StringUtils.isNotBlank(advertisementPermitDetail.getOwnerDetail())) {
            reportParams.put("agencyname", advertisementPermitDetail.getAgency().getName() + "/"
                    + advertisementPermitDetail.getOwnerDetail());
            reportParams.put("agencyaddress", advertisementPermitDetail.getAgency().getAddress());
        } else if (advertisementPermitDetail.getAgency() != null
                && org.apache.commons.lang.StringUtils.isBlank(advertisementPermitDetail.getOwnerDetail())) {
            reportParams.put("agencyname", advertisementPermitDetail.getAgency().getName());
            reportParams.put("agencyaddress", advertisementPermitDetail.getAgency().getAddress());
        } else {
            reportParams.put("agencyname", advertisementPermitDetail.getOwnerDetail());
            reportParams.put("agencyaddress", NOTMENTIONED);
        }
        
        reportParams.put("address", advertisementPermitDetail.getAdvertisement().getAddress());
        reportParams.put("applicationDate", formatter.format(advertisementPermitDetail.getApplicationDate()));
        reportParams.put("category", advertisementPermitDetail.getAdvertisement().getCategory().getName());
        reportParams.put("subjectMatter",advertisementPermitDetail.getAdvertisementParticular());
        buildMeasurementDetailsForJasper(advertisementPermitDetail, measurement, reportParams, NOTMENTIONED);
        
        reportParams.put("permitStartDate", formatter.format( advertisementPermitDetail.getPermissionstartdate()));
        reportParams.put("permitEndDate", formatter.format(advertisementPermitDetail.getPermissionenddate()));
        reportParams.put("currdate", formatter.format(new Date()));
        return reportParams;
    }

    private ResponseEntity<byte[]> redirect(String errorMessage) {
        errorMessage = "<html><body><p style='color:red;border:1px solid gray;padding:15px;'>" + errorMessage
                + "</p></body></html>";
        final byte[] byteData = errorMessage.getBytes();
        errorMessage = "";
        return new ResponseEntity<byte[]>(byteData, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/demandNotice/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> viewDemandNoticeReport(@PathVariable final String id,
            final HttpSession session,HttpServletRequest request) {
        final AdvertisementPermitDetail advertisementPermitDetails = advertisementPermitDetailService
                .findBy(Long.valueOf(id));
        return generateDemandNotice(request,advertisementPermitDetails, session, null);
    }

    @RequestMapping(value = "/permitOrder/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> viewPermitOrderReport(@PathVariable final String id,
            final HttpSession session,HttpServletRequest request) {
        final AdvertisementPermitDetail advertisementPermitDetails = advertisementPermitDetailService
                .findBy(Long.valueOf(id));
        if (!AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED
                .equalsIgnoreCase(advertisementPermitDetails.getStatus().getCode()))
            advertisementPermitDetailService.updateStateTransition(advertisementPermitDetails, Long.valueOf(0), "",
                    AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE, AdvertisementTaxConstants.WF_PERMITORDER_BUTTON);
        return generatePermitOrder(request,advertisementPermitDetails, session, null);
    }
}
