/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.tl.web.controller;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.service.AbstractLicenseService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.egov.infra.utils.DateUtils.currentDateToDefaultDateFormat;
import static org.egov.infra.utils.DateUtils.toYearFormat;

@Controller
@RequestMapping("/demandnotice")
public class DemandNoticeController {
    private final Map<String, Object> reportParams = new HashMap<>();
    @Autowired
    private TradeLicenseService tradeLicenseService;
    @Autowired
    private LicenseUtils licenseUtils;
    @Autowired
    private InstallmentHibDao installmentDao;
    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> generateDemandNotice(@RequestParam Long licenseId) {
        final TradeLicense license = tradeLicenseService.getLicenseById(licenseId);
        return generateReport(license);
    }

    private ResponseEntity<byte[]> generateReport(TradeLicense license) {
        if (license != null) {
            Installment currentInstallment = licenseUtils.getCurrInstallment(licenseUtils.getModule("Trade License"));
            reportParams.put("cityName", ApplicationThreadLocals.getMunicipalityName());
            reportParams.put("licenseNumber", license.getLicenseNumber());
            reportParams.put("ownerName", license.getLicensee().getApplicantName());
            reportParams.put("tradeNature", license.getTradeName().getName());
            reportParams.put("tradeName", license.getNameOfEstablishment());
            reportParams.put("tradeAddress", license.getAddress());
            reportParams.put("cityUrl", ApplicationThreadLocals.getDomainURL());
            reportParams.put("installmentYear", toYearFormat(currentInstallment.getFromDate()) + "-" +
                    toYearFormat(currentInstallment.getToDate()));
            reportParams.put("currentDate", currentDateToDefaultDateFormat());
            reportParams.put("lastyear", toYearFormat(installmentDao.getInsatllmentByModuleForGivenDate(
                    licenseUtils.getModule("Trade License"), new DateTime().minusYears(1).toDate()).getToDate()));
            BigDecimal currLicenseFee;
            BigDecimal arrLicenseFee;
            Map<String, Map<String, BigDecimal>> map = tradeLicenseService.getOutstandingFee(license);
            Map<String, BigDecimal> licenseFees = map.get("License Fee");
            if (licenseFees != null) {
                currLicenseFee = licenseFees.get("current") == null ? BigDecimal.ZERO : licenseFees.get("current");
                arrLicenseFee = licenseFees.get("arrear") == null ? BigDecimal.ZERO : licenseFees.get("arrear");
            } else {
                currLicenseFee = BigDecimal.ZERO;
                arrLicenseFee = BigDecimal.ZERO;
            }

            BigDecimal totalAmount = currLicenseFee.add(arrLicenseFee);
            BigDecimal licensewithIniPenalty = totalAmount
                    .add(AbstractLicenseService.percentage(totalAmount, BigDecimal.valueOf(25)));
            BigDecimal licenseFeeWithSecLvlPenalty = totalAmount
                    .add(AbstractLicenseService.percentage(totalAmount, BigDecimal.valueOf(50)));
            reportParams.put("licenseFee", currLicenseFee);
            reportParams.put("arrearLicenseFee", arrLicenseFee);
            reportParams.put("totalLicenseFee", totalAmount);
            reportParams.put("currentYear", toYearFormat(currentInstallment.getFromDate()));
            reportParams.put("licensewithIniPenalty", licensewithIniPenalty);
            reportParams.put("licenseFeeWithSecLvlPenalty", licenseFeeWithSecLvlPenalty);
        }

        final ReportRequest reportInput = new ReportRequest("tldemandnotice", license, reportParams);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=License Demand Notice.pdf");
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}
