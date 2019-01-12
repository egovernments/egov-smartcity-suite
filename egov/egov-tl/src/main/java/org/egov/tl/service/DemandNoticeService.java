/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.tl.service;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.PenaltyRates;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.contracts.DemandNoticeRequest;
import org.egov.tl.entity.contracts.LicenseDemandDetail;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
import static org.egov.infra.config.core.ApplicationThreadLocals.getDomainURL;
import static org.egov.infra.config.core.ApplicationThreadLocals.getMunicipalityName;
import static org.egov.infra.reporting.engine.ReportFormat.PDF;
import static org.egov.infra.utils.DateUtils.currentDateToDefaultDateFormat;
import static org.egov.infra.utils.DateUtils.getAllMonths;
import static org.egov.infra.utils.DateUtils.getDefaultFormattedDate;
import static org.egov.infra.utils.DateUtils.toYearFormat;
import static org.egov.infra.utils.PdfUtils.appendFiles;
import static org.egov.tl.utils.Constants.CITY_GRADE_CORPORATION;
import static org.egov.tl.utils.Constants.RENEW_APPTYPE_CODE;

@Service
@Transactional(readOnly = true)
public class DemandNoticeService {

    private static final String WITH = " with ";
    private static final String CURRENT = "current";
    private static final String ARREAR = "arrear";
    private static final String PENALTY = "penalty";
    private static final String TL_CORPORATION_ACT = "TL_CORPORATION_ACT";
    private static final String TL_DEFAULT_ACT = "TL_MUNICIPALITY_ACT";

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    @Autowired
    private LicenseUtils licenseUtils;

    @Autowired
    private InstallmentHibDao installmentDao;

    @Autowired
    private PenaltyRatesService penaltyRatesService;

    @Autowired
    private CityService cityService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private LicenseAppTypeService licenseAppTypeService;

    @Autowired
    private CFinancialYearService cFinancialYearService;

    @Autowired
    private LicenseConfigurationService licenseConfigurationService;

    public ReportOutput generateReport(Long licenseId) {
        Map<String, Object> reportParams = new ConcurrentHashMap<>();
        TradeLicense license = tradeLicenseService.getLicenseById(licenseId);
        reportParams.put("license", license);
        reportParams.put("cityUrl", getDomainURL());
        reportParams.put("cityName", getMunicipalityName());
        reportParams.put("currentDate", currentDateToDefaultDateFormat());

        Installment currentInstallment = license.getCurrentDemand().getEgInstallmentMaster();
        reportParams.put("installmentYear", getFinancialYearRange(currentInstallment));

        String actDeclaration = licenseConfigurationService.getValueByKey(
                CITY_GRADE_CORPORATION.equals(cityService.getCityGrade()) ? TL_CORPORATION_ACT : TL_DEFAULT_ACT);
        reportParams.put("actDeclaration", actDeclaration);

        List<Installment> previousInstallment = installmentDao.fetchPreviousInstallmentsInDescendingOrderByModuleAndDate(
                licenseUtils.getModule(), currentInstallment.getToDate(), 1);
        if (!previousInstallment.isEmpty()) {
            reportParams.put("lastyear", getFinancialYearRange(previousInstallment.get(0)));

            Date endDateOfPreviousFinancialYear = new DateTime(previousInstallment.get(0).getFromDate())
                    .withMonthOfYear(12)
                    .withDayOfMonth(31).toDate();
            reportParams.put("endDateOfPreviousFinancialYear", getDefaultFormattedDate(endDateOfPreviousFinancialYear));

            Map<String, BigDecimal> licenseFees = tradeLicenseService
                    .getOutstandingFeeForDemandNotice(license, currentInstallment, previousInstallment.get(0));

            BigDecimal currLicenseFee = ZERO;
            BigDecimal arrLicenseFee = ZERO;
            BigDecimal arrLicensePenalty = ZERO;
            if (licenseFees != null) {
                currLicenseFee = licenseFees.get(CURRENT) == null ? ZERO : licenseFees.get(CURRENT).setScale(0, ROUND_HALF_UP);
                arrLicenseFee = licenseFees.get(ARREAR) == null ? ZERO : licenseFees.get(ARREAR).setScale(0, ROUND_HALF_UP);
                arrLicensePenalty = licenseFees.get(PENALTY) == null ? ZERO : licenseFees.get(PENALTY).setScale(0, ROUND_HALF_UP);
            }

            List<LicenseDemandDetail> monthWiseDemandDetails = new LinkedList<>();
            getMonthWiseLatePenaltyFeeDetails(license, currentInstallment, currLicenseFee, arrLicenseFee,
                    arrLicensePenalty, monthWiseDemandDetails);

            reportParams.put("monthWiseDemandDetails", monthWiseDemandDetails);
            reportParams.put("licenseFee", currLicenseFee);
            reportParams.put("penaltyFee", arrLicensePenalty);
            reportParams.put("arrearLicenseFee", arrLicenseFee);
            reportParams.put("totalLicenseFee", currLicenseFee.add(arrLicenseFee).add(arrLicensePenalty).setScale(0, ROUND_HALF_UP));
            reportParams.put("currentYear", toYearFormat(currentInstallment.getFromDate()));
            LicenseAppType licenseAppType = licenseAppTypeService.getLicenseAppTypeByCode(license.getIsActive()
                    ? RENEW_APPTYPE_CODE : license.getLicenseAppType().getCode());
            reportParams.put("penaltyCalculationMessage",
                    getPenaltyRateDetails(penaltyRatesService.getPenaltyRatesByLicenseAppType(licenseAppType),
                            currentInstallment, licenseAppType));
        }
        return reportService.createReport(new ReportRequest("tl_demand_notice", license, reportParams));
    }

    private void getMonthWiseLatePenaltyFeeDetails(TradeLicense license, Installment currentInstallment,
                                                   BigDecimal currLicenseFee, BigDecimal arrLicenseFee,
                                                   BigDecimal arrLicensePenalty, List<LicenseDemandDetail> monthWiseDemandDetails) {

        String currentInstallmentYear = toYearFormat(currentInstallment.getFromDate());

        for (Map.Entry<Integer, String> month : getAllMonths().entrySet()) {
            DateTime financialYearDate = new DateTime(currentInstallment.getFromDate()).withMonthOfYear(month.getKey());
            Date monthEndDate = new DateTime(financialYearDate).withDayOfMonth(financialYearDate.dayOfMonth().getMaximumValue()).toDate();
            BigDecimal penaltyAmt = penaltyRatesService
                    .calculatePenalty(license, currentInstallment.getFromDate(), monthEndDate, currLicenseFee);

            LicenseDemandDetail demandBillDtl = new LicenseDemandDetail();
            demandBillDtl.setLicenseFee(currLicenseFee);
            demandBillDtl.setPenalty(penaltyAmt.setScale(0, ROUND_HALF_UP));
            demandBillDtl.setArrersWithPenalty(arrLicenseFee.add(arrLicensePenalty));
            demandBillDtl.setMonth(month.getValue().concat(", ").concat(currentInstallmentYear));
            demandBillDtl.setTotalDues(arrLicenseFee.add(arrLicensePenalty).add(currLicenseFee).add(penaltyAmt).setScale(0, ROUND_HALF_UP));
            monthWiseDemandDetails.add(demandBillDtl);
        }
    }

    private String getPenaltyRateDetails(List<PenaltyRates> penaltyRates, Installment currentInstallment, LicenseAppType licenseAppType) {
        StringBuilder penaltylist = new StringBuilder();

        Long fromRange = penaltyRatesService.getMinFromRange(licenseAppType);
        Long toRange = penaltyRatesService.getMaxToRange(licenseAppType);

        for (PenaltyRates penaltyRate : penaltyRates) {
            LocalDate currentInstallmentStartDate = LocalDate.fromDateFields(currentInstallment.getFromDate()).plusDays(1);
            LocalDate currentStartDate = LocalDate.fromDateFields(currentInstallment.getFromDate());
            if (penaltyRate.getRate() <= ZERO.doubleValue()) {
                penaltylist.append("Before ")
                        .append(getDefaultFormattedDate(currentInstallmentStartDate.plusDays(penaltyRate.getToRange().intValue()).toDate()))
                        .append(" without penalty\n");
            }
            if (penaltyRate.getRate() > ZERO.doubleValue()) {
                if (penaltyRate.getToRange() >= toRange) {
                    penaltylist.append("    After ")
                            .append(getDefaultFormattedDate(currentStartDate.plusDays(penaltyRate.getFromRange().intValue()).toDate()))
                            .append(WITH).append(penaltyRate.getRate().intValue()).append("% penalty");
                } else if (penaltyRate.getFromRange() <= fromRange) {
                    penaltylist.append("Before ")
                            .append(getDefaultFormattedDate(currentInstallmentStartDate.plusDays(penaltyRate.getToRange().intValue()).toDate()))
                            .append(WITH).append(penaltyRate.getRate().intValue()).append("% penalty\n");
                } else {
                    penaltylist.append("    From ")
                            .append(getDefaultFormattedDate(currentInstallmentStartDate.plusDays(penaltyRate.getFromRange().intValue()).toDate()))
                            .append(" to ")
                            .append(getDefaultFormattedDate(currentStartDate.plusDays(penaltyRate.getToRange().intValue()).toDate()))
                            .append(WITH).append(penaltyRate.getRate().intValue()).append("% penalty\n");
                }
            }
        }
        return penaltylist.toString();
    }

    @Transactional(readOnly = true, timeout = 7200)
    public ReportOutput generateBulkDemandNotice(DemandNoticeRequest searchRequest) {
        ReportOutput reportOutput = new ReportOutput();
        reportOutput.setReportName("demand_notices");
        reportOutput.setReportFormat(PDF);
        List<DemandNoticeRequest> demandNotices = tradeLicenseService.getLicenseDemandNotices(searchRequest);
        if (demandNotices.isEmpty()) {
            reportOutput.setReportOutputData("No Data".getBytes());
        } else {
            List<InputStream> demandNoticePDFStreams = new ArrayList<>();
            for (DemandNoticeRequest tlNotice : demandNotices) {
                demandNoticePDFStreams.add(generateReport(tlNotice.getLicenseId()).asInputStream());
            }
            reportOutput.setReportOutputData(appendFiles(demandNoticePDFStreams));
        }
        return reportOutput;
    }


    private String getFinancialYearRange(Installment installment) {
        return cFinancialYearService.getFinancialYearByDate(installment.getFromDate()).getFinYearRange();
    }
}