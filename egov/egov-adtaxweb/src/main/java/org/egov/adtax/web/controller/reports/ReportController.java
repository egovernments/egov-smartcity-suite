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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.WordUtils;
import org.egov.adtax.entity.AdvertisementAdditionalTaxRate;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.service.AdvertisementAdditinalTaxRateService;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.penalty.AdvertisementPenaltyCalculator;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.web.utils.WebUtils;
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
@RequestMapping(value = "/advertisement")
public class ReportController {

    private static final String AGENCYADDRESS = "agencyaddress";

    private static final String AGENCYNAME = "agencyname";

    @Autowired
    private ReportService reportService;

    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;
    
    @Autowired
    private AdvertisementDemandService advertisementDemandService;
    
    @Autowired
    private AdvertisementPenaltyCalculator advertisementPenaltyCalculator;
    @Autowired
    private AdvertisementAdditinalTaxRateService advertisementAdditinalTaxRateService;
    @Autowired
    private CityService cityService;
    
    @Autowired
    @Qualifier("messageSource")
    private MessageSource advertisementReportMessageSource;

    @RequestMapping(value = "/permitOrder", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> generatePermitOrder(final HttpServletRequest request,
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
    @ResponseBody
    public ResponseEntity<byte[]> generateDemandNotice(final HttpServletRequest request,
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
            StringBuilder measurement, final Map<String, Object> reportParams, String NOTMENTIONED) {
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
            reportParams.putAll(buildParametersForDemandDetails(advertisementPermitDetail));
            final String cityGrade = getCityGrade();
            if (cityGrade != null && cityGrade.equalsIgnoreCase(AdvertisementTaxConstants.CITY_GRADE_CORPORATION)) {
                reportParams.put("lawAct",advertisementReportMessageSource.getMessage("msg.ap.law.act.corporation",
                        new String[] {}, Locale.getDefault()));
            }else{
                reportParams.put("lawAct", advertisementReportMessageSource.getMessage("msg.ap.law.act.municipality",
                        new String[] {}, Locale.getDefault()));
            }
            reportInput = new ReportRequest(AdvertisementTaxConstants.DEMANDNOTICE, advertisementPermitDetail, reportParams);
        }
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=Demand Notice.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
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
            Integer to = Integer.parseInt(currentFinYearValues[1])-1;
            previousInstallmentDesc = from.toString() + "-" + to.toString();
        }
        for (final AdvertisementAdditionalTaxRate taxRates : additionalTaxRates)
            additionalTaxes.put(taxRates.getReasonCode(), taxRates.getPercentage());

        for (final EgDemandDetails demandDtl : advertisementPermitDetail.getAdvertisement().getDemandId().getEgDemandDetails()) {

            if (demandDtl.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                if (currentInstallemnt != null && currentInstallemnt.getDescription()
                        .equals(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription())) {
                    if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(AdvertisementTaxConstants.DEMANDREASON_ARREAR_ADVERTISEMENTTAX)) {
                        arrInsAdvertisement = arrInsAdvertisement.add(demandDtl.getBalance());
                        arrInsTotalTaxableAmt = arrInsTotalTaxableAmt.add(demandDtl.getBalance());
                    } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(AdvertisementTaxConstants.DEMANDREASON_ADVERTISEMENTTAX)) {
                        curntInsAdvertisement = curntInsAdvertisement.add(demandDtl.getBalance());
                        reportParams.put("curntInsAdvertisement", curntInsAdvertisement.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        curntInsTotalTaxableAmt = curntInsTotalTaxableAmt.add(demandDtl.getBalance());
                    } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE)) {
                        curntInsEncrocFee = demandDtl.getBalance();
                        reportParams.put("curntInsEncrocFee", curntInsEncrocFee.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        curntInsTotalTaxableAmt = curntInsTotalTaxableAmt.add(curntInsEncrocFee);
                    } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(AdvertisementTaxConstants.DEMANDREASON_PENALTY)) {
                        curntInsPenaltyFee = demandDtl.getBalance();
                        reportParams.put("curntInsPenaltyFee", curntInsPenaltyFee.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                    }
                } else {
                    if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(AdvertisementTaxConstants.DEMANDREASON_ARREAR_ADVERTISEMENTTAX) ||
                            demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(AdvertisementTaxConstants.DEMANDREASON_ADVERTISEMENTTAX)) {
                        arrInsAdvertisement = arrInsAdvertisement.add(demandDtl.getBalance());
                        reportParams.put("arrInsAdvertisement", arrInsAdvertisement.setScale(2,BigDecimal.ROUND_HALF_EVEN));
                        arrInsTotalTaxableAmt = arrInsTotalTaxableAmt.add(demandDtl.getBalance());
                    } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE)) {
                        arrInsEncrocFee = arrInsEncrocFee.add(demandDtl.getBalance());
                        reportParams.put("arrInsEncrocFee", arrInsEncrocFee.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        arrInsTotalTaxableAmt = arrInsTotalTaxableAmt.add(demandDtl.getBalance());
                    } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(AdvertisementTaxConstants.DEMANDREASON_PENALTY)) {
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
        curntInsGrossTotal = curntInsTotalTaxableAmt.add(curntInsServiceTax).add(curntInsSwachBharatCess).add(curntInsKrishiKalyanCess);
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

    private BigDecimal calculateAdditionalTaxes( BigDecimal curntInsTotalTaxableAmt,
            final BigDecimal entry) {
        return entry.multiply(curntInsTotalTaxableAmt).divide(BigDecimal.valueOf(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    private String getCityGrade() {
        return cityService.getCityByURL(ApplicationThreadLocals.getDomainName()).getGrade();
    }

    private Map<String, Object> buildParametersForReport(HttpServletRequest request,
            final AdvertisementPermitDetail advertisementPermitDetail) {
        StringBuilder measurement = new StringBuilder();
        final Map<String, Object> reportParams = new HashMap<>();
        String NOTMENTIONED = "Not Mentioned ";

        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String cityLogo = url.concat(AdvertisementTaxConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
        final String ulbName = request.getSession().getAttribute("citymunicipalityname").toString();
        final String cityName = request.getSession().getAttribute("cityname").toString();
        reportParams.put("cityName", cityName);
        reportParams.put("logoPath", cityLogo);
        reportParams.put("ulbName", ulbName);
        reportParams.put("advertisementtitle",
                WordUtils.capitalize(AdvertisementTaxConstants.ADVERTISEMENTDEMANDNOTICETITLE));
     
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
       //   reportParams.put("workFlowAction", workFlowAction);
        reportParams.put("advertisementnumber", advertisementPermitDetail.getAdvertisement().getAdvertisementNumber());
        reportParams.put("permitNumber", advertisementPermitDetail.getPermissionNumber());
        reportParams.put("applicationNumber", advertisementPermitDetail.getApplicationNumber());
        
        if (advertisementPermitDetail.getAgency() != null
                && org.apache.commons.lang.StringUtils.isNotBlank(advertisementPermitDetail.getOwnerDetail())) {
            reportParams.put(AGENCYNAME, advertisementPermitDetail.getAgency().getName() + "/"
                    + advertisementPermitDetail.getOwnerDetail());
            reportParams.put(AGENCYADDRESS, advertisementPermitDetail.getAgency().getAddress());
        } else if (advertisementPermitDetail.getAgency() != null
                && org.apache.commons.lang.StringUtils.isBlank(advertisementPermitDetail.getOwnerDetail())) {
            reportParams.put(AGENCYNAME, advertisementPermitDetail.getAgency().getName());
            reportParams.put(AGENCYADDRESS, advertisementPermitDetail.getAgency().getAddress());
        } else {
            reportParams.put(AGENCYNAME, advertisementPermitDetail.getOwnerDetail());
            reportParams.put(AGENCYADDRESS, NOTMENTIONED);
        }
        
        reportParams.put("address", advertisementPermitDetail.getAdvertisement().getAddress());
        reportParams.put("applicationDate", formatter.format(advertisementPermitDetail.getApplicationDate()));
        reportParams.put("category", advertisementPermitDetail.getAdvertisement().getCategory().getName());
        reportParams.put("subjectMatter",advertisementPermitDetail.getAdvertisementParticular());
        reportParams.put("subCategory",advertisementPermitDetail.getAdvertisement().getSubCategory().getCode());       
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
