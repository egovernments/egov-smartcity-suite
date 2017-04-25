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

package org.egov.stms.transactions.service;

import static org.egov.stms.utils.constants.SewerageTaxConstants.ARREARSEWERAGETAX;
import static org.egov.stms.utils.constants.SewerageTaxConstants.BOUNDARYTYPE_WARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_ADVANCE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_SEWERAGETAX_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.HIERARCHYTYPE_REVENUE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULE_NAME;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.service.FinancialYearService;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.web.utils.WebUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.stms.autonumber.SewerageDemandBillNumberGenerator;
import org.egov.stms.entity.SewerageDCBReportResult;
import org.egov.stms.masters.pojo.DCBReportWardwiseResult;
import org.egov.stms.masters.pojo.SewerageRateDCBResult;
import org.egov.stms.masters.pojo.SewerageRateResultComparatorByInstallment;
import org.egov.stms.notice.entity.SewerageNotice;
import org.egov.stms.notice.service.SewerageNoticeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageDCBReporService {

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private FinancialYearService financialYearService;

    @Autowired
    private SewerageNoticeService sewerageNoticeService;

    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    public static final String SEWERAGEDEMANDBILL = "sewerageDemandBill";

    public List<SewerageRateDCBResult> getSewerageRateDCBReport(final SewerageApplicationDetails sewerageApplicationDetails) {
        final List<SewerageRateDCBResult> rateResultList = new ArrayList<>();
        Map<String, Map<Date, BigDecimal>> receiptMap;
        Map<Date, BigDecimal> receiptDtlMap;
        Map<String, Map<String, Map<Date, BigDecimal>>> receiptApplDtlMap;
        final HashMap<String, SewerageRateDCBResult> sewerageReportMap = new HashMap<>();
        if (sewerageApplicationDetails.getConnection() != null) {

            SewerageRateDCBResult dcbResult = new SewerageRateDCBResult();
            if (sewerageApplicationDetails.getCurrentDemand() != null
                    && sewerageApplicationDetails.getCurrentDemand().getEgDemandDetails() != null)
                for (final EgDemandDetails demandDtl : sewerageApplicationDetails.getCurrentDemand().getEgDemandDetails()) {
                    final SewerageRateDCBResult rateResult = sewerageReportMap
                            .get(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());
                    if (rateResult == null) {
                        // TODO: Handle Penalty cases in future.

                        if (demandDtl.getAmtCollected() == null)
                            demandDtl.setAmtCollected(BigDecimal.ZERO);
                        dcbResult = new SewerageRateDCBResult();
                        dcbResult.setInstallmentYearDescription(
                                demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());
                        dcbResult.setInstallmentYearId(demandDtl.getEgDemandReason().getEgInstallmentMaster().getId());
                        if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(ARREARSEWERAGETAX)) {
                            dcbResult.setArrearAmount(demandDtl.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                            dcbResult.setCollectedArrearAmount(demandDtl.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(FEES_SEWERAGETAX_CODE)) {
                            dcbResult.setDemandAmount(dcbResult.getDemandAmount().add(demandDtl.getAmount()));
                            dcbResult.setCollectedDemandAmount(
                                    dcbResult.getCollectedDemandAmount().add(demandDtl.getAmtCollected()));
                        } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(FEES_ADVANCE_CODE)) {
                            dcbResult.setAdvanceAmount(demandDtl.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                            dcbResult
                                    .setCollectedAdvanceAmount(demandDtl.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        sewerageReportMap.put(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription(), dcbResult);
                    } else {

                        dcbResult = sewerageReportMap
                                .get(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());

                        if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(ARREARSEWERAGETAX)) {
                            dcbResult.setArrearAmount(
                                    dcbResult.getArrearAmount().add(demandDtl.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                            dcbResult.setCollectedArrearAmount(dcbResult.getCollectedArrearAmount()
                                    .add(demandDtl.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(FEES_SEWERAGETAX_CODE)) {
                            dcbResult.setDemandAmount(
                                    dcbResult.getDemandAmount().add(demandDtl.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                            dcbResult.setCollectedDemandAmount(dcbResult.getCollectedDemandAmount()
                                    .add(demandDtl.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        } else if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(FEES_ADVANCE_CODE)) {
                            dcbResult.setAdvanceAmount(dcbResult.getAdvanceAmount()
                                    .add(demandDtl.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                            dcbResult.setCollectedAdvanceAmount(dcbResult.getCollectedAdvanceAmount()
                                    .add(demandDtl.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
                        }
                        sewerageReportMap.put(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription(), dcbResult);
                    }
                }

            receiptApplDtlMap = new TreeMap<>();

            for (final SewerageApplicationDetails detail : sewerageApplicationDetails.getConnection().getApplicationDetails()) {
                receiptMap = new TreeMap<>();
                if (detail.getCurrentDemand() != null && !detail.getCurrentDemand().getEgDemandDetails().isEmpty())
                    for (final EgDemandDetails demandDetail : detail.getCurrentDemand().getEgDemandDetails()) {
                        receiptDtlMap = new HashMap<>();
                        for (final EgdmCollectedReceipt receipt : demandDetail.getEgdmCollectedReceipts()) {
                            receiptDtlMap.put(receipt.getReceiptDate(),
                                    receipt.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                            receiptMap.put(receipt.getReceiptNumber(), receiptDtlMap);

                        }
                        if (!receiptMap.isEmpty())
                            receiptApplDtlMap.put(detail.getApplicationNumber(), receiptMap);
                    }
            }

            dcbResult.setReceipts(receiptApplDtlMap);

            if (sewerageReportMap.size() > 0)
                sewerageReportMap.forEach((key, value) -> {
                    rateResultList.add(value);
                });

        }
        Collections.sort(rateResultList, new SewerageRateResultComparatorByInstallment());
        return rateResultList;

    }

    public List<DCBReportWardwiseResult> getSewerageRateDCBWardwiseReport(
            final Map<String, List<SewerageApplicationDetails>> applicationDtlMap, final String propertyType) {
        final List<DCBReportWardwiseResult> dcbReportList = new ArrayList<>();
        final Map<String, DCBReportWardwiseResult> dcbReportMap = new HashMap<>();
        final Map<String, DCBReportWardwiseResult> dcbMap = new HashMap<>();
        DCBReportWardwiseResult dcbResult;

        for (final Map.Entry<String, List<SewerageApplicationDetails>> entry : applicationDtlMap.entrySet()) {
            Boundary boundary = null;
            BoundaryType boundaryType;
            final List<Boundary> boundaryList = new ArrayList<>();
            dcbResult = new DCBReportWardwiseResult();
            for (final SewerageApplicationDetails appDetails : entry.getValue())
                if (appDetails != null && appDetails.getCurrentDemand() != null
                        && appDetails.getCurrentDemand().getEgDemandDetails() != null) {
                    dcbResult.setNoofassessments(entry.getValue().size());
                    for (final EgDemandDetails demandDetails : appDetails.getCurrentDemand().getEgDemandDetails()) {
                        dcbResult.setRevenueWard(entry.getKey());
                        if (null != propertyType)
                            dcbResult.setPropertyType(propertyType);
                        boundaryType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(BOUNDARYTYPE_WARD,
                                HIERARCHYTYPE_REVENUE);
                        boundaryList.addAll(boundaryService.getBondariesByNameAndTypeOrderByBoundaryNumAsc(entry.getKey(),
                                boundaryType.getId()));
                        if (!boundaryList.isEmpty())
                            boundary = boundaryList.get(0);
                        if (boundary != null)
                            dcbResult.setWardId(boundary.getId());
                        final DCBReportWardwiseResult rateResult = dcbReportMap.get(entry.getKey());
                        if (rateResult == null) {
                            dcbResult.setInstallmentYearDescription(
                                    demandDetails.getEgDemandReason().getEgInstallmentMaster().getDescription());
                            buildArrearAndCurrentDemandTax(dcbResult, demandDetails);

                        } else {
                            dcbResult = dcbReportMap.get(entry.getKey());

                            buildArrearAndCurrentDemandTax(dcbResult, demandDetails);

                        }
                        dcbResult.setTotal_demand(dcbResult.getArr_demand().add(dcbResult.getCurr_demand()));
                        dcbResult.setTotal_collection(dcbResult.getArr_collection().add(dcbResult.getCurr_collection()));
                        dcbResult.setTotal_balance(dcbResult.getArr_balance().add(dcbResult.getCurr_balance()));

                        dcbReportMap.put(entry.getKey(), dcbResult);
                    }
                }
            dcbMap.put(entry.getKey(), dcbResult);

        }
        if (dcbMap.size() > 0)
            dcbMap.forEach((key, value) -> {
                dcbReportList.add(value);
            });

        return dcbReportList;
    }

    private void buildArrearAndCurrentDemandTax(final DCBReportWardwiseResult dcbResult, final EgDemandDetails demandDetails) {
        if (demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()
                .equalsIgnoreCase(ARREARSEWERAGETAX)) {
            dcbResult.setArr_demand(dcbResult.getArr_demand()
                    .add(demandDetails.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
            dcbResult.setArr_collection(dcbResult.getArr_collection()
                    .add(demandDetails.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
            dcbResult.setArr_balance(dcbResult.getArr_balance()
                    .add(demandDetails.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .subtract(demandDetails.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP))));
        } else if (demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()
                .equalsIgnoreCase(FEES_SEWERAGETAX_CODE)) {
            dcbResult.setCurr_demand(
                    dcbResult.getCurr_demand().add(demandDetails.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
            dcbResult.setCurr_collection(dcbResult.getCurr_collection()
                    .add(demandDetails.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
            dcbResult.setCurr_balance(dcbResult.getCurr_balance()
                    .add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()).setScale(2,
                            BigDecimal.ROUND_HALF_UP)));
        }
    }

    public List<DCBReportWardwiseResult> getSewerageDCBWardConnections(
            final Map<String, List<SewerageApplicationDetails>> applicationDtlMap, final String propertyType) {

        final List<DCBReportWardwiseResult> dcbReportList = new ArrayList<>();
        final Map<String, DCBReportWardwiseResult> dcbReportMap = new HashMap<>();
        DCBReportWardwiseResult dcbResult;

        for (final Map.Entry<String, List<SewerageApplicationDetails>> entry : applicationDtlMap.entrySet()) {

            final List<SewerageApplicationDetails> applicationList = new ArrayList<>();
            if (entry.getValue() != null)
                applicationList.addAll(entry.getValue());

            for (final SewerageApplicationDetails detail : applicationList) {
                dcbResult = new DCBReportWardwiseResult();
                if (detail != null && detail.getCurrentDemand() != null
                        && detail.getCurrentDemand().getEgDemandDetails() != null) {
                    dcbResult.setShscnumber(detail.getConnection().getShscNumber());
                    dcbResult.setOwnerName(detail.getOwnerName());
                    for (final EgDemandDetails demandDetails : detail.getCurrentDemand().getEgDemandDetails()) {
                        dcbResult.setApplicationNumber(entry.getKey());
                        if (propertyType != null)
                            dcbResult.setPropertyType(propertyType);
                        final DCBReportWardwiseResult rateResult = dcbReportMap.get(entry.getKey());
                        if (rateResult == null) {
                            dcbResult.setInstallmentYearDescription(
                                    demandDetails.getEgDemandReason().getEgInstallmentMaster().getDescription());
                            buildArrearAndCurrentDemandTax(dcbResult, demandDetails);
                            dcbReportMap.put(entry.getKey(), dcbResult);
                        } else {
                            dcbResult = dcbReportMap.get(entry.getKey());

                            if (demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(ARREARSEWERAGETAX)) {
                                dcbResult.setArr_demand(dcbResult.getArr_demand()
                                        .add(demandDetails.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                                dcbResult.setArr_collection(dcbResult.getArr_collection()
                                        .add(demandDetails.getAmtCollected()).setScale(2, BigDecimal.ROUND_HALF_UP));
                                dcbResult.setArr_balance(dcbResult.getArr_balance()
                                        .add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()).setScale(2,
                                                BigDecimal.ROUND_HALF_UP)));
                            } else if (demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(FEES_ADVANCE_CODE))
                                dcbResult.setAdvanceAmount(demandDetails.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP));
                            else if (demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                    .equalsIgnoreCase(FEES_SEWERAGETAX_CODE)) {
                                dcbResult.setCurr_demand(dcbResult.getCurr_demand()
                                        .add(demandDetails.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                                dcbResult.setCurr_collection(dcbResult.getCurr_collection()
                                        .add(demandDetails.getAmtCollected().setScale(2, BigDecimal.ROUND_HALF_UP)));
                                dcbResult.setCurr_balance(dcbResult.getCurr_balance()
                                        .add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()).setScale(2,
                                                BigDecimal.ROUND_HALF_UP)));

                                dcbResult.setTotal_demand(dcbResult.getArr_demand().add(dcbResult.getCurr_demand()));
                                dcbResult.setTotal_collection(dcbResult.getArr_collection().add(dcbResult.getCurr_collection()));
                                dcbResult.setTotal_balance(dcbResult.getArr_balance().add(dcbResult.getCurr_balance()));
                            }

                            dcbReportMap.put(entry.getKey(), dcbResult);
                        }
                    }
                }
            }

        }
        if (dcbReportMap.size() > 0)
            dcbReportMap.forEach((key, value) -> {
                dcbReportList.add(value);
            });

        return dcbReportList;
    }

    @Transactional
    public ReportOutput generateAndSaveDemandBillNotice(final SewerageApplicationDetails sewerageApplicationDetails,
            final AssessmentDetails propertyOwnerDetails, final HttpServletRequest request, final HttpSession session) {
        ReportOutput reportOutput;
        SewerageNotice sewerageNotice = null;
        String demandBillNumber;
        InputStream generateNoticePDF;
        final SewerageDemandBillNumberGenerator demandBillNumberGenerator = beanResolver
                .getAutoNumberServiceFor(SewerageDemandBillNumberGenerator.class);
        demandBillNumber = demandBillNumberGenerator.generateSewerageDemandBillNumber(sewerageApplicationDetails);

        reportOutput = generateSewerageDemandBillNotice(sewerageApplicationDetails, demandBillNumber, propertyOwnerDetails,
                session,
                request);
        if (reportOutput != null && reportOutput.getReportOutputData() != null) {
            generateNoticePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
            sewerageNotice = sewerageNoticeService.buildDemandBillNotice(sewerageApplicationDetails, generateNoticePDF,
                    demandBillNumber);
        }
        if (sewerageNotice != null) {
            sewerageApplicationDetails.addNotice(sewerageNotice);
            sewerageApplicationDetailsService.save(sewerageApplicationDetails);
        }
        return reportOutput;
    }

    public ReportOutput generateSewerageDemandBillNotice(final SewerageApplicationDetails sewerageApplicationDetails,
            final String demandBillNumber, final AssessmentDetails propertyOwnerDetails,
            final HttpSession session, final HttpServletRequest request) {
        final Map<String, Object> reportParams = new HashMap<>();
        ReportRequest reportInput;
        BigDecimal sewerageTax = BigDecimal.ZERO;
        final Installment currentInstallment = installmentDao
                .getInsatllmentByModuleForGivenDate(moduleService.getModuleByName(MODULE_NAME), new Date());
        final List<SewerageRateDCBResult> dcbResultList = getPendingSewerageTaxForCurrentYearInstallment(
                sewerageApplicationDetails);

        reportParams.put("demandBillNumber", demandBillNumber);
        final SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        final String fromDate = date.format(currentInstallment.getFromDate());

        final String url = WebUtils.extractRequestDomainURL(request, false);
        reportParams.put("cityLogo", url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
                .concat((String) request.getSession().getAttribute("citylogo")));
        reportParams.put("currInstallmentFromDate", fromDate);
        reportParams.put("financialYear", currentInstallment.getFinYearRange());
        reportParams.put("shscnumber", sewerageApplicationDetails.getConnection().getShscNumber());
        reportParams.put("houseno", propertyOwnerDetails.getHouseNo());
        reportParams.put("municipality", session.getAttribute("citymunicipalityname"));
        reportParams.put("district", session.getAttribute("districtName"));
        if (propertyOwnerDetails.getOwnerNames() != null && !propertyOwnerDetails.getOwnerNames().isEmpty())
            for (final OwnerName propertyOwner : propertyOwnerDetails.getOwnerNames())
                reportParams.put("ownername", propertyOwner.getOwnerName());
        if (propertyOwnerDetails.getBoundaryDetails() != null) {
            reportParams.put("localityname", propertyOwnerDetails.getBoundaryDetails().getLocalityName());
            reportParams.put("revenueWardNo", propertyOwnerDetails.getBoundaryDetails().getWardName());
            reportParams.put("blockname", propertyOwnerDetails.getBoundaryDetails().getBlockName());
        }
        final SewerageDCBReportResult reportResultList = new SewerageDCBReportResult();
        reportResultList.setDcbReportList(dcbResultList);
        for (final SewerageRateDCBResult dcbresultObject : dcbResultList)
            sewerageTax = sewerageTax.add(dcbresultObject.getDemandAmount());
        reportParams.put("seweragetax", sewerageTax);
        reportInput = new ReportRequest(SEWERAGEDEMANDBILL, reportResultList, reportParams);
        return reportService.createReport(reportInput);
    }

    public List<SewerageRateDCBResult> getPendingSewerageTaxForCurrentYearInstallment(
            final SewerageApplicationDetails sewerageApplicationDetails) {
        final List<SewerageRateDCBResult> rateResultList = new ArrayList<>();
        final HashMap<String, SewerageRateDCBResult> sewerageReportMap = new HashMap<>();
        if (sewerageApplicationDetails.getConnection() != null) {


            SewerageRateDCBResult dcbResult;
            if (sewerageApplicationDetails.getCurrentDemand() != null
                    && sewerageApplicationDetails.getCurrentDemand().getEgDemandDetails() != null)
                for (final EgDemandDetails demandDtl : sewerageApplicationDetails.getCurrentDemand().getEgDemandDetails())
                    if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(FEES_SEWERAGETAX_CODE)
                            && demandDtl.getAmount().subtract(demandDtl.getAmtCollected())
                                    .compareTo(BigDecimal.ZERO) > 0) {

                        final SewerageRateDCBResult rateResult = sewerageReportMap
                                .get(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());
                        if (rateResult == null) {

                            dcbResult = new SewerageRateDCBResult();
                            dcbResult.setInstallmentYearDescription(
                                    demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());
                            dcbResult.setDemandAmount(
                                    dcbResult.getDemandAmount().add(demandDtl.getAmount().subtract(demandDtl.getAmtCollected())));
                            sewerageReportMap.put(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription(),
                                    dcbResult);

                        } else {
                            dcbResult = sewerageReportMap
                                    .get(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());
                            dcbResult.setDemandAmount(
                                    dcbResult.getDemandAmount().add(demandDtl.getAmount().subtract(demandDtl.getAmtCollected())));
                            sewerageReportMap.put(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription(),
                                    dcbResult);
                        }
                    }

            if (sewerageReportMap.size() > 0)
                sewerageReportMap.forEach((key, value) -> {
                    rateResultList.add(value);
                });

        }
        return rateResultList;

    }

}