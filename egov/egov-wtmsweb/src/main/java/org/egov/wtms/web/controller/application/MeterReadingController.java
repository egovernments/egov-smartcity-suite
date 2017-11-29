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
 *
 */
package org.egov.wtms.web.controller.application;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.wtms.application.entity.MeterReadingConnectionDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.MeteredRates;
import org.egov.wtms.masters.entity.MeteredRatesDetail;
import org.egov.wtms.masters.entity.UsageSlab;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.repository.WaterRatesDetailsRepository;
import org.egov.wtms.masters.service.MeteredRatesDetailService;
import org.egov.wtms.masters.service.MeteredRatesService;
import org.egov.wtms.masters.service.UsageSlabService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Controller
@RequestMapping(value = "/application")
public class MeterReadingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeterReadingController.class);
    private static final String PREVIOUSREADING = "previousreading";
    private static final String NEWCONNECTIONMETERENTRY = "newconnection-meterEntry";
    private static final String METERCURRENTREADING = "metercurrentReading";
    private static final String REDIRECT_TO_METERDEMANDNOTICE = "redirect:/application/meterdemandnotice?pathVar=";
    private static final String ERROR_METER_RATE_NOT_PRESENT = "err.metered.rate.not.present";

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private UsageSlabService usageSlabService;

    @Autowired
    private MeteredRatesService meteredRatesService;

    @Autowired
    private MeteredRatesDetailService meteredRatesDetailService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    private final WaterConnectionDetailsRepository waterConnectionDetailsRepository;
    private final ConnectionDemandService connectionDemandService;

    @Autowired
    public MeterReadingController(final WaterConnectionDetailsRepository waterConnectionDetailsRepository,
            final WaterRatesDetailsRepository waterRatesDetailsRepository,
            final ConnectionDemandService connectionDemandService) {
        this.waterConnectionDetailsRepository = waterConnectionDetailsRepository;
        this.connectionDemandService = connectionDemandService;
    }

    @ModelAttribute
    public WaterConnectionDetails getWaterConnectionDetails(@PathVariable final String consumerCode) {
        return waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.ACTIVE);

    }

    private String loadViewData(final Model model,
            final WaterConnectionDetails waterConnectionDetails) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        model.addAttribute("waterConnectionDetails", waterConnectionDetails);
        model.addAttribute("executionDate", formatter.format(waterConnectionDetails.getExecutionDate()));
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        model.addAttribute(
                "connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(
                        waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("mode", "meterEntry");
        model.addAttribute("meterReadingCurrentObj", new MeterReadingConnectionDetails());
        final BigDecimal waterTaxDueforParent = waterConnectionDetailsService.getTotalAmount(waterConnectionDetails);
        model.addAttribute("waterTaxDueforParent", waterTaxDueforParent);
        return NEWCONNECTIONMETERENTRY;
    }

    @RequestMapping(value = "/meterentry/{consumerCode}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String consumerCode, final HttpServletRequest request) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.ACTIVE);
        MeterReadingConnectionDetails meterReadingpriviousObj;
        final List<MeterReadingConnectionDetails> meterReadingpriviousObjlist = waterConnectionDetailsRepository
                .findPreviousMeterReadingReading(waterConnectionDetails.getId());
        if (!meterReadingpriviousObjlist.isEmpty())
            meterReadingpriviousObj = meterReadingpriviousObjlist.get(0);
        else {
            meterReadingpriviousObj = new MeterReadingConnectionDetails();
            if (waterConnectionDetails.getConnection().getInitialReading() != null)
                meterReadingpriviousObj.setCurrentReading(waterConnectionDetails.getConnection().getInitialReading());
            else
                meterReadingpriviousObj.setCurrentReading(0l);
        }
        model.addAttribute("meterReadingpriviousObj", meterReadingpriviousObj);
        if (connectionDemandService.meterEntryAllReadyExistForCurrentMonth(waterConnectionDetails, new Date()))
            return REDIRECT_TO_METERDEMANDNOTICE
                    + waterConnectionDetails.getConnection().getConsumerCode();
        else
            return loadViewData(model, waterConnectionDetails);

    }

    @RequestMapping(value = "/meterentry/{consumerCode}", method = RequestMethod.POST)
    public String updateMeterEntry(@ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model,
            final HttpServletRequest request) {
        final String sourceChannel = request.getParameter("Source");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date givenDate = null;
        try {
            givenDate = dateFormat.parse(request.getParameter("metercurrentReadingDate"));
        } catch (final ParseException e) {
            LOGGER.error("Exception while parsing date " + e);
        }
        if (connectionDemandService.meterEntryAllReadyExistForCurrentMonth(waterConnectionDetails, givenDate))
            return REDIRECT_TO_METERDEMANDNOTICE
                    + waterConnectionDetails.getConnection().getConsumerCode();
        final MeterReadingConnectionDetails meterReadingConnectionDetailObj = new MeterReadingConnectionDetails();
        Long previousReading = 0l;
        Long currentReadingValue = null;
        Boolean meterDamaged;
        if ("true".equals(request.getParameter("waterConnectionDetails.meterConnection.isMeterDamaged"))) {
            meterDamaged = true;
            meterReadingConnectionDetailObj.setMeterDamaged(meterDamaged);
        }
        if (errors.hasErrors())
            return NEWCONNECTIONMETERENTRY;

        if (null != request.getParameter(PREVIOUSREADING) && !"".equals(request.getParameter(PREVIOUSREADING)))
            previousReading = Long.valueOf(request.getParameter(PREVIOUSREADING));

        if (StringUtils.isNotBlank(request.getParameter(METERCURRENTREADING)))
            currentReadingValue = Long.valueOf(request.getParameter(METERCURRENTREADING));

        if (currentReadingValue != null && currentReadingValue < previousReading) {
            final String message = "Current rate should not be less than Previous reading";
            model.addAttribute("message", message);
            return NEWCONNECTIONMETERENTRY;
        }
        final WaterConnectionDetails waterconnectionDetails = billCalculationAndDemandUpdate(waterConnectionDetails, request,
                meterReadingConnectionDetailObj, previousReading, dateFormat);
        final WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterconnectionDetails);
        waterConnectionDetailsService.updateIndexes(savedWaterConnectionDetails, sourceChannel);
        return REDIRECT_TO_METERDEMANDNOTICE
                + savedWaterConnectionDetails.getConnection().getConsumerCode();
    }

    private WaterConnectionDetails billCalculationAndDemandUpdate(final WaterConnectionDetails waterConnectionDetails,
            final HttpServletRequest request,
            final MeterReadingConnectionDetails meterReadingConnectionDetailObj, final Long previousReading,
            final SimpleDateFormat dateFormat) {
        Date currentDate = null;
        Date previousDate = null;
        int noofmonths;
        Long noOfUnitsPerMonth = 0l;
        final String readingDate = request.getParameter("metercurrentReadingDate");

        try {
            currentDate = dateFormat.parse(readingDate);
            if (request.getParameter("previousreadingDate") != null)
                previousDate = dateFormat.parse(request.getParameter("previousreadingDate"));
        } catch (final ParseException e) {
            LOGGER.error("Exception while parsing date " + e);
        }
        if (StringUtils.isNotBlank(request.getParameter(METERCURRENTREADING)))
            meterReadingConnectionDetailObj.setCurrentReading(Long.valueOf(request.getParameter(METERCURRENTREADING)));
        meterReadingConnectionDetailObj.setCurrentReadingDate(currentDate);

        populateMeterReadingDetails(meterReadingConnectionDetailObj, waterConnectionDetails);
        if (previousDate != null)
            noofmonths = DateUtils.noOfMonthsBetween(previousDate, currentDate);
        else
            noofmonths = DateUtils.noOfMonthsBetween(new Date(), currentDate);

        if (!meterReadingConnectionDetailObj.isMeterDamaged()) {
            final Long currentToPreviousDiffOfUnits = Long.valueOf(request.getParameter(METERCURRENTREADING))
                    - previousReading;
            if (noofmonths > 0)
                noOfUnitsPerMonth = currentToPreviousDiffOfUnits / noofmonths;
            else
                noOfUnitsPerMonth = currentToPreviousDiffOfUnits;
        }
        WaterConnectionDetails waterconnectionDetails;
        if (meterReadingConnectionDetailObj.isMeterDamaged())
            waterconnectionDetails = calculateDemandForDamagedMeter(waterConnectionDetails, previousDate, noofmonths);
        else {
            final double finalAmountToBePaid = calculateAmountTobePaid(waterConnectionDetails, noOfUnitsPerMonth);

            if (BigDecimal.valueOf(finalAmountToBePaid).compareTo(BigDecimal.ZERO) > 0)
                waterconnectionDetails = connectionDemandService.updateDemandForMeteredConnection(waterConnectionDetails,
                        BigDecimal.valueOf(finalAmountToBePaid), currentDate, previousDate, noofmonths);
            else
                throw new ApplicationRuntimeException("err.no.amount.due");
        }
        return waterconnectionDetails;
    }

    private WaterConnectionDetails calculateDemandForDamagedMeter(final WaterConnectionDetails waterConnectionDetails,
            final Date previousDate, final int noofmonths) {
        Double amountValue;
        new WaterConnectionDetails();
        Installment currentInstallment;
        List<Installment> newInstallmentList;
        DateTime dateTime = new DateTime(previousDate);
        DateTime lastInstReadingDate;
        DateTime lastInstStartDate;

        newInstallmentList = installmentDao.getInstallmentsByModuleForGivenDateAndInstallmentType(
                moduleService.getModuleByName(WaterTaxConstants.MODULE_NAME),
                previousDate, WaterTaxConstants.MONTHLY);
        currentInstallment = connectionDemandService.getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME,
                WaterTaxConstants.MONTHLY, new Date());
        if (newInstallmentList.isEmpty() || !newInstallmentList.contains(currentInstallment))
            newInstallmentList.add(currentInstallment);

        for (final Installment instalmentVal : newInstallmentList) {

            lastInstReadingDate = dateTime.minusMonths(6);
            lastInstStartDate = lastInstReadingDate.withDayOfMonth(1).withTimeAtStartOfDay();
            final List<Installment> lastInstallmentList = installmentDao
                    .getInstallmentsByModuleBetweenFromDateAndToDateAndInstallmentType(
                            moduleService.getModuleByName(WaterTaxConstants.MODULE_NAME),
                            lastInstStartDate.toDate(), instalmentVal.getFromDate(), WaterTaxConstants.MONTHLY);

            amountValue = calculateDamagedMeterAverageDemand(lastInstallmentList, waterConnectionDetails);
            if (BigDecimal.valueOf(amountValue).compareTo(BigDecimal.ZERO) > 0)
                connectionDemandService.updateDemandForMeteredConnection(waterConnectionDetails,
                        BigDecimal.valueOf(amountValue), instalmentVal.getFromDate(), previousDate, noofmonths);
            dateTime = new DateTime(instalmentVal.getFromDate());
            dateTime = dateTime.plusMonths(1);
        }
        return waterConnectionDetails;
    }

    private double calculateAmountTobePaid(final WaterConnectionDetails waterConnectionDetails,
            final Long noOfUnitsPerMonth) {
        MeteredRates meteredRates = null;
        MeteredRatesDetail meteredRatesDetail;
        Double amountToBeCollected = 0d;
        UsageSlab usageSlab = null;
        if (noOfUnitsPerMonth != 0)
            usageSlab = usageSlabService
                    .getUsageSlabForWaterVolumeConsumed(waterConnectionDetails.getUsageType().getName(), noOfUnitsPerMonth);
        if (usageSlab != null && usageSlab.getSlabName() != null)
            meteredRates = meteredRatesService.findBySlabName(usageSlab.getSlabName());
        else if (noOfUnitsPerMonth != 0)
            throw new ApplicationRuntimeException("err.usageslab.not.present");

        if (meteredRates != null && meteredRates.getSlabName() != null) {
            meteredRatesDetail = meteredRatesDetailService.getActiveRateforSlab(meteredRates.getSlabName(), new Date());
            if (meteredRatesDetail != null) {
                if (meteredRatesDetail.getRateAmount() == null && meteredRatesDetail.getFlatAmount() == null)
                    throw new ApplicationRuntimeException(ERROR_METER_RATE_NOT_PRESENT);
                else if (meteredRatesDetail.isRecursive())
                    amountToBeCollected = calculateDemandWithRecursiveAmount(usageSlab, meteredRatesDetail, noOfUnitsPerMonth);
                else if (meteredRatesDetail.getRateAmount() != null && meteredRatesDetail.getRateAmount() != 0)
                    amountToBeCollected = meteredRatesDetail.getRateAmount() * noOfUnitsPerMonth;
                else if (meteredRatesDetail.getFlatAmount() != null && meteredRatesDetail.getFlatAmount() != 0)
                    amountToBeCollected = meteredRatesDetail.getFlatAmount();
            } else
                throw new ApplicationRuntimeException(ERROR_METER_RATE_NOT_PRESENT);
        } else
            throw new ApplicationRuntimeException(ERROR_METER_RATE_NOT_PRESENT);
        return amountToBeCollected;
    }

    private Double calculateDamagedMeterAverageDemand(final List<Installment> installmentList,
            final WaterConnectionDetails waterConnectionDetails) {
        Double totalAmount = 0d;
        int count = 0;
        Set<EgDemandDetails> demandDtlSet = null;
        BigDecimal meterDemandAmount = BigDecimal.ZERO;
        final EgDemand demand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        if (demand != null)
            demandDtlSet = demand.getEgDemandDetails();
        for (final Installment installment : installmentList)
            for (final EgDemandDetails demandDetail : demandDtlSet)
                if (demandDetail.getEgDemandReason().getEgInstallmentMaster().equals(installment)) {
                    count++;
                    meterDemandAmount = meterDemandAmount.add(demandDetail.getAmount());
                }
        if (count != 0)
            totalAmount = meterDemandAmount.doubleValue() / count;
        return totalAmount;
    }

    private Double calculateDemandWithRecursiveAmount(final UsageSlab usageSlab, final MeteredRatesDetail meteredRatesDetail,
            final Long numberOfUnitsPerMonth) {
        Double totalAmount = 0d;
        Double amtValue;
        Double amount1;
        Double amount2;
        if (meteredRatesDetail.getFlatAmount() != null && meteredRatesDetail.getFlatAmount() != 0
                && numberOfUnitsPerMonth >= usageSlab.getFromVolume()) {
            amtValue = ((double) numberOfUnitsPerMonth - usageSlab.getFromVolume() + 1) / meteredRatesDetail.getRecursiveFactor();
            totalAmount = meteredRatesDetail.getFlatAmount() + Math.ceil(amtValue) * meteredRatesDetail.getRecursiveAmount();
        } else if (meteredRatesDetail.getRateAmount() != null && meteredRatesDetail.getRateAmount() != 0
                && numberOfUnitsPerMonth >= usageSlab.getFromVolume()) {
            amount1 = ((double) usageSlab.getFromVolume() - 1) * meteredRatesDetail.getRateAmount();
            amount2 = ((double) numberOfUnitsPerMonth - usageSlab.getFromVolume() + 1) / meteredRatesDetail.getRecursiveFactor();
            amtValue = Math.ceil(amount2) * meteredRatesDetail.getRecursiveAmount();
            totalAmount = amount1 + amtValue;
        }
        return totalAmount;
    }

    private void populateMeterReadingDetails(final MeterReadingConnectionDetails meterReadingConnectionDeatilObj,
            final WaterConnectionDetails waterConnectionDetails) {
        final List<MeterReadingConnectionDetails> meterentryDetailsList = new ArrayList<>(
                0);
        if (meterReadingConnectionDeatilObj != null && validMeterEntryDetail(meterReadingConnectionDeatilObj)) {
            meterReadingConnectionDeatilObj.setWaterConnectionDetails(waterConnectionDetails);
            meterentryDetailsList.add(meterReadingConnectionDeatilObj);
        }
        waterConnectionDetails.getMeterConnection().clear();
        waterConnectionDetails.setMeterConnection(meterentryDetailsList);
    }

    private boolean validMeterEntryDetail(final MeterReadingConnectionDetails meterReadingConnectionDetails) {
        if (meterReadingConnectionDetails.getCurrentReading() == null
                && meterReadingConnectionDetails.getCurrentReadingDate() == null)
            return false;
        return true;
    }

}