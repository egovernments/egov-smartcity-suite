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

package org.egov.adtax.service.notice;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.ADVERTISEMENTDEMANDNOTICETITLE;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.ADVERTISEMENTPERMITODERTITLE;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.CITY_GRADE_CORPORATION;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.DEMANDNOTICE;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.DEMANDREASON_ADVERTISEMENTTAX;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.DEMANDREASON_ARREAR_ADVERTISEMENTTAX;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.DEMANDREASON_PENALTY;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.PERMITORDER;
import static org.egov.infra.utils.DateUtils.getDefaultFormattedDate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.egov.adtax.entity.AdvertisementAdditionalTaxRate;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.service.AdvertisementAdditinalTaxRateService;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.penalty.AdvertisementPenaltyCalculator;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementNoticeService {
    private static final String AGENCYADDRESS = "agencyaddress";
    private static final String AGENCYNAME = "agencyname";

    @Autowired
    private AdvertisementDemandService advertisementDemandService;
    @Autowired
    private AdvertisementPenaltyCalculator advertisementPenaltyCalculator;
    @Autowired
    private AdvertisementAdditinalTaxRateService advertisementAdditinalTaxRateService;
    @Autowired
    private CityService cityService;
    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource advertisementReportMessageSource;
    @Autowired
    private ReportService reportService;

    public ReportOutput generatePermitOrder(final AdvertisementPermitDetail advertisementPermitDetail,
            final Map<String, Object> ulbDetailsReportParams) {
        ReportRequest reportInput = null;
        if (null != advertisementPermitDetail) {
            final Map<String, Object> reportParams = buildParametersForReport(advertisementPermitDetail);
            reportParams.putAll(ulbDetailsReportParams);
            reportParams.put("advertisementtitle",
                    WordUtils.capitalize(ADVERTISEMENTPERMITODERTITLE));

            reportInput = new ReportRequest(PERMITORDER, advertisementPermitDetail,
                    reportParams);
            reportInput.setReportFormat(ReportFormat.PDF);
        }
        return reportService.createReport(reportInput);
    }

    public ReportOutput generateDemandNotice(final AdvertisementPermitDetail advertisementPermitDetail,
            final Map<String, Object> ulbDetailsReportParams) {
        ReportRequest reportInput = null;
        if (null != advertisementPermitDetail) {
            final Map<String, Object> reportParams = buildParametersForReport(advertisementPermitDetail);
            reportParams.putAll(ulbDetailsReportParams);
            reportParams.putAll(buildParametersForDemandDetails(advertisementPermitDetail));
            final String cityGrade = getCityGrade();
            if (cityGrade != null && cityGrade.equalsIgnoreCase(CITY_GRADE_CORPORATION)) {
                reportParams.put("lawAct", advertisementReportMessageSource.getMessage("msg.ap.law.act.corporation",
                        new String[] {}, Locale.getDefault()));
            } else {
                reportParams.put("lawAct", advertisementReportMessageSource.getMessage("msg.ap.law.act.municipality",
                        new String[] {}, Locale.getDefault()));
            }
            reportInput = new ReportRequest(DEMANDNOTICE, advertisementPermitDetail, reportParams);
            reportInput.setReportFormat(ReportFormat.PDF);
        }
        return reportService.createReport(reportInput);
    }

    private void buildMeasurementDetailsForJasper(final AdvertisementPermitDetail advertisementPermitDetail,
            StringBuilder measurement, final Map<String, Object> reportParams, String notMentioned) {
        measurement.append(
                advertisementPermitDetail.getMeasurement() == null ? notMentioned : advertisementPermitDetail
                        .getMeasurement())
                .append(" ");

        if (advertisementPermitDetail.getMeasurement() != null)
            measurement.append(advertisementPermitDetail.getUnitOfMeasure().getDescription());

        if (advertisementPermitDetail.getLength() != null)
            measurement.append(" Length : ").append(advertisementPermitDetail.getLength());

        if (advertisementPermitDetail.getWidth() != null)
            measurement.append(" Breadth : ").append(advertisementPermitDetail.getWidth());

        if (advertisementPermitDetail.getTotalHeight() != null)
            measurement.append(" Height : ").append(advertisementPermitDetail.getTotalHeight());

        reportParams.put("measurement", measurement.toString());
    }

    private Map<String, Object> buildParametersForDemandDetails(final AdvertisementPermitDetail advertisementPermitDetail) {
        final Map<String, Object> reportParams = new HashMap<>();

        BigDecimal curntInsAdvertisement = BigDecimal.ZERO;
        BigDecimal curntInsEncrocFee = BigDecimal.ZERO;
        BigDecimal curntInsPenaltyFee = BigDecimal.ZERO;
        BigDecimal curntInsServiceTax = BigDecimal.ZERO;
        BigDecimal curntInsSwachBharatCess = BigDecimal.ZERO;
        BigDecimal curntInsKrishiKalyanCess = BigDecimal.ZERO;
        BigDecimal curntInsTotalTaxableAmt = BigDecimal.ZERO;

        BigDecimal curntInsNetTotal;
        BigDecimal arrInsNetTotal;
        BigDecimal arrInsGrossTotal;
        BigDecimal curntInsGrossTotal;

        BigDecimal arrInsAdvertisement = BigDecimal.ZERO;
        BigDecimal arrInsEncrocFee = BigDecimal.ZERO;
        BigDecimal arrInsPenaltyFee = BigDecimal.ZERO;
        BigDecimal arrInsServiceTax = BigDecimal.ZERO;
        BigDecimal arrInsSwachBharatCess = BigDecimal.ZERO;
        BigDecimal arrInsKrishiKalyanCess = BigDecimal.ZERO;
        BigDecimal arrInsTotalTaxableAmt = BigDecimal.ZERO;
        String previousInstallmentDesc = null;
        Installment currentInstallemnt = advertisementPermitDetail.getAdvertisement().getDemandId().getEgInstallmentMaster();
        List<Installment> previousInstallemnt = advertisementDemandService
                .getPreviousInstallment(currentInstallemnt.getFromDate());
        Map<Installment, BigDecimal> penaltyAmountMap = advertisementPenaltyCalculator
                .getPenaltyByInstallment(advertisementPermitDetail);
        final Map<String, BigDecimal> additionalTaxes = new HashMap<>();
        final List<AdvertisementAdditionalTaxRate> additionalTaxRates = advertisementAdditinalTaxRateService
                .getAllActiveAdditinalTaxRates();
        if (previousInstallemnt.isEmpty()) {
            String currentFinYear = currentInstallemnt.getFinYearRange();
            String[] currentFinYearValues = currentFinYear.split("-");
            Integer from = Integer.parseInt(currentFinYearValues[0]) - 1;
            Integer to = Integer.parseInt(currentFinYearValues[1]) - 1;
            previousInstallmentDesc = from.toString() + "-" + to.toString();
        }
        for (final AdvertisementAdditionalTaxRate taxRates : additionalTaxRates)
            additionalTaxes.put(taxRates.getReasonCode(), taxRates.getPercentage());

        for (final EgDemandDetails demandDtl : advertisementPermitDetail.getAdvertisement().getDemandId().getEgDemandDetails()) {

            if (demandDtl.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                if (currentInstallemnt != null && currentInstallemnt.getDescription()
                        .equals(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription())) {
                    if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(DEMANDREASON_ARREAR_ADVERTISEMENTTAX)) {
                        arrInsAdvertisement = arrInsAdvertisement.add(demandDtl.getBalance());
                        arrInsTotalTaxableAmt = arrInsTotalTaxableAmt.add(demandDtl.getBalance());
                    } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(DEMANDREASON_ADVERTISEMENTTAX)) {
                        curntInsAdvertisement = curntInsAdvertisement.add(demandDtl.getBalance());
                        reportParams.put("curntInsAdvertisement", curntInsAdvertisement.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        curntInsTotalTaxableAmt = curntInsTotalTaxableAmt.add(demandDtl.getBalance());
                    } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(DEMANDREASON_ENCROCHMENTFEE)) {
                        curntInsEncrocFee = demandDtl.getBalance();
                        reportParams.put("curntInsEncrocFee", curntInsEncrocFee.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        curntInsTotalTaxableAmt = curntInsTotalTaxableAmt.add(curntInsEncrocFee);
                    } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(DEMANDREASON_PENALTY)) {
                        curntInsPenaltyFee = demandDtl.getBalance();
                        reportParams.put("curntInsPenaltyFee", curntInsPenaltyFee.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    }
                } else {
                    if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(DEMANDREASON_ARREAR_ADVERTISEMENTTAX) ||
                            demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(DEMANDREASON_ADVERTISEMENTTAX)) {
                        arrInsAdvertisement = arrInsAdvertisement.add(demandDtl.getBalance());
                        reportParams.put("arrInsAdvertisement", arrInsAdvertisement.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        arrInsTotalTaxableAmt = arrInsTotalTaxableAmt.add(demandDtl.getBalance());
                    } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(DEMANDREASON_ENCROCHMENTFEE)) {
                        arrInsEncrocFee = arrInsEncrocFee.add(demandDtl.getBalance());
                        reportParams.put("arrInsEncrocFee", arrInsEncrocFee.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        arrInsTotalTaxableAmt = arrInsTotalTaxableAmt.add(demandDtl.getBalance());
                    } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(DEMANDREASON_PENALTY)) {
                        arrInsPenaltyFee = arrInsPenaltyFee.add(demandDtl.getBalance());
                        reportParams.put("arrInsPenaltyFee", arrInsPenaltyFee.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    }
                }
            }
        }

        // Add penalty into reports
        for (final Map.Entry<Installment, BigDecimal> penaltyMap : penaltyAmountMap.entrySet())
            if (currentInstallemnt != null
                    && currentInstallemnt.getDescription().equalsIgnoreCase(penaltyMap.getKey().getDescription())) {
                curntInsPenaltyFee = curntInsPenaltyFee.add(penaltyMap.getValue());
                reportParams.put("curntInsPenaltyFee", curntInsPenaltyFee);
            } else {
                arrInsPenaltyFee = arrInsPenaltyFee.add(penaltyMap.getValue());
                reportParams.put("arrInsPenaltyFee", arrInsPenaltyFee);
            }

        for (final Map.Entry<String, BigDecimal> entry : additionalTaxes.entrySet()) {
            if ("Service_Tax".equalsIgnoreCase(entry.getKey())) {
                curntInsServiceTax = calculateAdditionalTaxes(curntInsTotalTaxableAmt, entry.getValue());
                arrInsServiceTax = calculateAdditionalTaxes(arrInsTotalTaxableAmt, entry.getValue());
            } else if ("ADTAX_SB_CESS".equalsIgnoreCase(entry.getKey())) {
                curntInsSwachBharatCess = calculateAdditionalTaxes(curntInsTotalTaxableAmt, entry.getValue());
                arrInsSwachBharatCess = calculateAdditionalTaxes(arrInsTotalTaxableAmt, entry.getValue());
            } else if ("ADTAX_KRISHI_CES".equalsIgnoreCase(entry.getKey())) {
                curntInsKrishiKalyanCess = calculateAdditionalTaxes(curntInsTotalTaxableAmt, entry.getValue());
                arrInsKrishiKalyanCess = calculateAdditionalTaxes(arrInsTotalTaxableAmt, entry.getValue());
            }
        }
        // set additional taxes details and installment
        reportParams.put("currentInstallmentDesc", currentInstallemnt.getDescription());
        reportParams.put("previousInstallmentDesc",
                previousInstallemnt.isEmpty() ? previousInstallmentDesc : previousInstallemnt.get(0).getDescription());
        reportParams.put("curntInsServiceTax", curntInsServiceTax.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        reportParams.put("curntInsKrishiKalyanCess", curntInsKrishiKalyanCess.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        reportParams.put("curntInsSwachBharatCess", curntInsSwachBharatCess.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        reportParams.put("arrInsServiceTax", arrInsServiceTax.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        reportParams.put("arrInsSwachBharatCess", arrInsSwachBharatCess.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        reportParams.put("arrInsKrishiKalyanCess", arrInsKrishiKalyanCess.setScale(2, BigDecimal.ROUND_HALF_EVEN));

        // sum demand details
        reportParams.put("curntInsTotalTaxableAmt", curntInsTotalTaxableAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        reportParams.put("arrInsTotalTaxableAmt", arrInsTotalTaxableAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        curntInsGrossTotal = curntInsTotalTaxableAmt.add(curntInsServiceTax).add(curntInsSwachBharatCess)
                .add(curntInsKrishiKalyanCess);
        reportParams.put("curntInsGrossTotal", curntInsGrossTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        curntInsNetTotal = curntInsGrossTotal.add(curntInsPenaltyFee);
        reportParams.put("curntInsNetTotal", curntInsNetTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        arrInsGrossTotal = arrInsTotalTaxableAmt.add(arrInsServiceTax).add(arrInsSwachBharatCess).add(arrInsKrishiKalyanCess);
        reportParams.put("arrInsGrossTotal", arrInsGrossTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        arrInsNetTotal = arrInsGrossTotal.add(arrInsPenaltyFee);
        reportParams.put("arrInsNetTotal", arrInsNetTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        reportParams.put("advertisementTaxSum",
                curntInsAdvertisement.add(arrInsAdvertisement).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        reportParams.put("encrochmentFeeSum", curntInsEncrocFee.add(arrInsEncrocFee).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        reportParams.put("serviceTaxSum", curntInsServiceTax.add(arrInsServiceTax).setScale(0, BigDecimal.ROUND_HALF_UP));
        reportParams.put("swachBharatCessSum",
                curntInsSwachBharatCess.add(arrInsSwachBharatCess).setScale(0, BigDecimal.ROUND_HALF_UP));
        reportParams.put("krishiKalyanCessSum",
                curntInsKrishiKalyanCess.add(arrInsKrishiKalyanCess).setScale(0, BigDecimal.ROUND_HALF_UP));
        reportParams.put("penalitySum", curntInsPenaltyFee.add(arrInsPenaltyFee).setScale(2, BigDecimal.ROUND_HALF_EVEN));

        reportParams.put("adParticular", advertisementPermitDetail.getAdvertisementParticular());
        reportParams.put("durationOfAdvt", advertisementPermitDetail.getAdvertisementDuration());
        reportParams.put("class", advertisementPermitDetail.getAdvertisement().getRateClass().getDescription());
        reportParams.put("revenueWard", advertisementPermitDetail.getAdvertisement().getWard().getName());
        reportParams.put("electionWard", advertisementPermitDetail.getAdvertisement().getElectionWard() != null
                ? advertisementPermitDetail.getAdvertisement().getElectionWard().getName() : "");
        return reportParams;
    }

    private BigDecimal calculateAdditionalTaxes(BigDecimal curntInsTotalTaxableAmt,
            final BigDecimal entry) {
        return entry.multiply(curntInsTotalTaxableAmt).divide(BigDecimal.valueOf(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    private String getCityGrade() {
        return cityService.getCityByURL(ApplicationThreadLocals.getDomainName()).getGrade();
    }

    private Map<String, Object> buildParametersForReport(final AdvertisementPermitDetail advertisementPermitDetail) {
        StringBuilder measurement = new StringBuilder();
        final Map<String, Object> reportParams = new HashMap<>();
        String notMentioned = "Not Mentioned ";

        reportParams.put("advertisementtitle",
                WordUtils.capitalize(ADVERTISEMENTDEMANDNOTICETITLE));

        reportParams.put("advertisementnumber", advertisementPermitDetail.getAdvertisement().getAdvertisementNumber());
        reportParams.put("permitNumber", advertisementPermitDetail.getPermissionNumber());
        reportParams.put("applicationNumber", advertisementPermitDetail.getApplicationNumber());

        if (advertisementPermitDetail.getAgency() != null
                && isNotBlank(advertisementPermitDetail.getOwnerDetail())) {
            reportParams.put(AGENCYNAME, advertisementPermitDetail.getAgency().getName() + "/"
                    + advertisementPermitDetail.getOwnerDetail());
            reportParams.put(AGENCYADDRESS, advertisementPermitDetail.getAgency().getAddress());
        } else if (advertisementPermitDetail.getAgency() != null
                && isBlank(advertisementPermitDetail.getOwnerDetail())) {
            reportParams.put(AGENCYNAME, advertisementPermitDetail.getAgency().getName());
            reportParams.put(AGENCYADDRESS, defaultIfBlank(advertisementPermitDetail.getAgency().getAddress(), notMentioned));
        } else {
            reportParams.put(AGENCYNAME, advertisementPermitDetail.getOwnerDetail());
            reportParams.put(AGENCYADDRESS, notMentioned);
        }

        reportParams.put("address", advertisementPermitDetail.getAdvertisement().getAddress());
        reportParams.put("applicationDate", getDefaultFormattedDate(advertisementPermitDetail.getApplicationDate()));
        reportParams.put("category", advertisementPermitDetail.getAdvertisement().getCategory().getName());
        reportParams.put("subjectMatter", advertisementPermitDetail.getAdvertisementParticular());
        reportParams.put("subCategory", advertisementPermitDetail.getAdvertisement().getSubCategory().getCode());
        buildMeasurementDetailsForJasper(advertisementPermitDetail, measurement, reportParams, notMentioned);

        reportParams.put("permitStartDate", getDefaultFormattedDate(advertisementPermitDetail.getPermissionstartdate()));
        reportParams.put("permitEndDate", getDefaultFormattedDate(advertisementPermitDetail.getPermissionenddate()));
        reportParams.put("currdate", getDefaultFormattedDate(new Date()));
        reportParams.put("lastPaymentPaidYear",
                advertisementDemandService.getLastPaymentPaidFinYear(advertisementPermitDetail.getAdvertisement()));
        return reportParams;
    }

}
