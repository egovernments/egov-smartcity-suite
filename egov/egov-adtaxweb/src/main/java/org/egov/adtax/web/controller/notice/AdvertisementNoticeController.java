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
package org.egov.adtax.web.controller.notice;

import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.WF_PERMITORDER_BUTTON;
import static org.egov.infra.reporting.engine.ReportDisposition.INLINE;
import static org.egov.infra.utils.StringUtils.append;
import static org.egov.infra.web.utils.WebUtils.reportToResponseEntity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.notice.AdvertisementNoticeService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/advertisement")
public class AdvertisementNoticeController {

    private static final String DEMAND_NOTICE = "demand_notice_";
    private static final String PERMIT_ORDER = "permit_order_";
    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;
    @Autowired
    private AdvertisementNoticeService advertisementNoticeService;
    @Autowired
    private CityService cityService;

    @GetMapping("/permitOrder")
    public @ResponseBody ResponseEntity<InputStreamResource> generatePermitOrder(final HttpServletRequest request,
            final HttpSession session) {
        final AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService
                .findBy(Long.valueOf(request.getParameter("pathVar")));
        ReportOutput reportOutput = advertisementNoticeService.generatePermitOrder(advertisementPermitDetail,
                getUlbDetails(request));
        reportOutput.setReportName(append(PERMIT_ORDER, advertisementPermitDetail.getAdvertisement().getAdvertisementNumber()));
        reportOutput.setReportDisposition(INLINE);
        return reportToResponseEntity(reportOutput);
    }

    @GetMapping("/demandNotice")
    public @ResponseBody ResponseEntity<InputStreamResource> generateDemandNotice(final HttpServletRequest request,
            final HttpSession session) {
        final AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService
                .findBy(Long.valueOf(request.getParameter("pathVar")));
        ReportOutput reportOutput = advertisementNoticeService.generateDemandNotice(advertisementPermitDetail,
                getUlbDetails(request));
        reportOutput.setReportName(append(DEMAND_NOTICE, advertisementPermitDetail.getAdvertisement().getAdvertisementNumber()));
        reportOutput.setReportDisposition(INLINE);
        return reportToResponseEntity(reportOutput);
    }

    @GetMapping("/demandNotice/{id}")
    public @ResponseBody ResponseEntity<InputStreamResource> viewDemandNoticeReport(@PathVariable final String id,
            final HttpSession session, HttpServletRequest request) {
        final AdvertisementPermitDetail advertisementPermitDetails = advertisementPermitDetailService
                .findBy(Long.valueOf(id));
        ReportOutput reportOutput = advertisementNoticeService.generateDemandNotice(advertisementPermitDetails,
                getUlbDetails(request));
        reportOutput.setReportName(append(DEMAND_NOTICE, advertisementPermitDetails.getAdvertisement().getAdvertisementNumber()));
        reportOutput.setReportDisposition(INLINE);
        return reportToResponseEntity(reportOutput);
    }

    @GetMapping("/permitOrder/{id}")
    public @ResponseBody ResponseEntity<InputStreamResource> viewPermitOrderReport(@PathVariable final String id,
            final HttpSession session, HttpServletRequest request) {
        final AdvertisementPermitDetail advertisementPermitDetails = advertisementPermitDetailService
                .findBy(Long.valueOf(id));
        if (!APPLICATION_STATUS_ADTAXPERMITGENERATED
                .equalsIgnoreCase(advertisementPermitDetails.getStatus().getCode()))
            advertisementPermitDetailService.updateStateTransition(advertisementPermitDetails, Long.valueOf(0), "",
                    CREATE_ADDITIONAL_RULE, WF_PERMITORDER_BUTTON);
        ReportOutput reportOutput = advertisementNoticeService.generatePermitOrder(advertisementPermitDetails,
                getUlbDetails(request));
        reportOutput.setReportName(append(PERMIT_ORDER, advertisementPermitDetails.getAdvertisement().getAdvertisementNumber()));
        reportOutput.setReportDisposition(INLINE);
        return reportToResponseEntity(reportOutput);
    }

    private Map<String, Object> getUlbDetails(HttpServletRequest request) {
        Map<String, Object> ulbDetailsReportParams = new HashMap<>();
        ulbDetailsReportParams.put("cityName", request.getSession().getAttribute("cityname").toString());
        ulbDetailsReportParams.put("logoPath", cityService.getCityLogoPath());
        ulbDetailsReportParams.put("ulbName", cityService.getMunicipalityName());
        return ulbDetailsReportParams;
    }
}
