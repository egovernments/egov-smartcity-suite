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

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.getFormattedDate;
import static org.egov.infra.utils.DateUtils.noOfMonthsBetween;
import static org.egov.infra.utils.DateUtils.toDateUsingDefaultPattern;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MONTHLY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
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
import org.egov.wtms.application.service.WaterDemandConnectionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class MeterReadingController {
    private static final String PREVIOUSREADING = "previousreading";
    private static final String NEWCONNECTIONMETERENTRY = "newconnection-meterEntry";
    private static final String METERCURRENTREADING = "metercurrentReading";
    private static final String REDIRECT_TO_METERDEMANDNOTICE = "redirect:/application/meterdemandnotice?pathVar=";
    private static final String ERROR_METER_RATE_NOT_PRESENT = "err.metered.rate.not.present";
    private static final String METER_CURRENT_READING_DATE = "metercurrentReadingDate";

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
    
    @Autowired
    private WaterDemandConnectionService waterDemandConnectionService;

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
        MeterReadingConnectionDetails meterReadingPreviousObj = null;
        final List<MeterReadingConnectionDetails> meterReadingPreviousObjList = waterConnectionDetailsRepository
                .findPreviousMeterReading(waterConnectionDetails.getId());
        if (meterReadingPreviousObjList == null || meterReadingPreviousObjList.isEmpty()) {
            meterReadingPreviousObj = new MeterReadingConnectionDetails();

            if (waterConnectionDetails.getConnection().getInitialReading() != null)
                meterReadingPreviousObj.setCurrentReading(waterConnectionDetails.getConnection().getInitialReading());
            else if (waterConnectionDetails.getExistingConnection() == null) {
                meterReadingPreviousObj.setCurrentReading(0l);
                meterReadingPreviousObj.setCurrentReadingDate(waterConnectionDetails.getExecutionDate());
            } else {
                meterReadingPreviousObj.setCurrentReadingDate(waterConnectionDetails.getExistingConnection().getReadingDate());
                meterReadingPreviousObj.setCurrentReading(waterConnectionDetails.getExistingConnection().getCurrentReading());
            }
        } else
            meterReadingPreviousObj = meterReadingPreviousObjList.get(0);

        model.addAttribute("meterReadingpriviousObj", meterReadingPreviousObj);
        model.addAttribute("waterConnectionDetails", waterConnectionDetails);
        if (waterConnectionDetails.getExecutionDate() != null)
            model.addAttribute("executionDate", toDefaultDateFormat(waterConnectionDetails.getExecutionDate()));
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        model.addAttribute(
                "connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(
                        waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("mode", "meterEntry");
        model.addAttribute("meterReadingCurrentObj", new MeterReadingConnectionDetails());
        final BigDecimal waterTaxDueForParent = waterConnectionDetailsService.getTotalAmount(waterConnectionDetails);
        model.addAttribute("waterTaxDueforParent", waterTaxDueForParent);
        return NEWCONNECTIONMETERENTRY;
    }

    @RequestMapping(value = "/meterentry/{consumerCode}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String consumerCode, final HttpServletRequest request) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.ACTIVE);

        if (connectionDemandService.meterEntryAllReadyExistForCurrentMonth(waterConnectionDetails, new Date()))
            return REDIRECT_TO_METERDEMANDNOTICE
                    + waterConnectionDetails.getConnection().getConsumerCode();
        else
            return loadViewData(model, waterConnectionDetails);

    }

    @RequestMapping(value = "/meterentry/{consumerCode}", method = RequestMethod.POST)
    public String updateMeterEntry(@Valid @ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model,
            final HttpServletRequest request) {
        Date givenDate = null;
        if (request.getParameter(METER_CURRENT_READING_DATE) != null)
            givenDate = toDateUsingDefaultPattern(request.getParameter(METER_CURRENT_READING_DATE));
        if (connectionDemandService.meterEntryAllReadyExistForCurrentMonth(waterConnectionDetails, givenDate))
            return REDIRECT_TO_METERDEMANDNOTICE
                    + waterConnectionDetails.getConnection().getConsumerCode();
        final MeterReadingConnectionDetails meterReadingConnectionDetailObj = new MeterReadingConnectionDetails();
        Long previousReading = 0l;
        Long currentReadingValue = null;
        Boolean meterDamaged;
        Boolean currentMonthIncluded = false;
        if ("true".equals(request.getParameter("waterConnectionDetails.meterConnection.isMeterDamaged"))) {
            meterDamaged = true;
            meterReadingConnectionDetailObj.setMeterDamaged(meterDamaged);
        }
        Date currentDate = null;
        Date previousDate = null;
        currentDate = toDateUsingDefaultPattern(request.getParameter(METER_CURRENT_READING_DATE));
        if (request.getParameter("previousreadingDate") != null)
            previousDate = toDateUsingDefaultPattern(request.getParameter("previousreadingDate"));
        if (currentDate != null) {
            final DateTime currentDateTime = new DateTime(currentDate);
            final int midday = currentDateTime.dayOfMonth().getMaximumValue() / 2;
            final DateTime midDate = new DateTime(currentDate).withDayOfMonth(midday);
            final DateTime previousMonthStartDate = new DateTime(currentDate).minusMonths(1).withDayOfMonth(1);

            if (currentDate.before(midDate.toDate())
                    && isPreviousMonthDemandExist(previousMonthStartDate, waterConnectionDetails))
                errors.reject("err.invalid.meter.reading.date", new String[] {
                        getFormattedDate(previousMonthStartDate.toDate(), "MMMM YYYY"),
                        getFormattedDate(midDate.toDate(), "dd MMMM YYYY") },
                        "err.invalid.meter.reading.date");
            else if (!currentDate.before(midDate.toDate()))
                currentMonthIncluded = true;
        }

        if (errors.hasErrors()) {
            loadViewData(model, waterConnectionDetails);
            return NEWCONNECTIONMETERENTRY;
        }

        if (isNotBlank(request.getParameter(PREVIOUSREADING)))
            previousReading = Long.valueOf(request.getParameter(PREVIOUSREADING));

        if (isNotBlank(request.getParameter(METERCURRENTREADING)))
            currentReadingValue = Long.valueOf(request.getParameter(METERCURRENTREADING));

        if (currentReadingValue != null && currentReadingValue < previousReading)
            throw new ValidationException("err.invalid.meter.reading");
        final WaterConnectionDetails waterconnectionDetails = billCalculationAndDemandUpdate(waterConnectionDetails, request,
                meterReadingConnectionDetailObj, previousReading, currentDate, previousDate, currentMonthIncluded);
        final WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterconnectionDetails);
        waterConnectionDetailsService.updateIndexes(savedWaterConnectionDetails);
        return REDIRECT_TO_METERDEMANDNOTICE
                + savedWaterConnectionDetails.getConnection().getConsumerCode();
    }

    private WaterConnectionDetails billCalculationAndDemandUpdate(final WaterConnectionDetails waterConnectionDetails,
            final HttpServletRequest request, final MeterReadingConnectionDetails meterReadingConnectionDetailObj,
            final Long previousReading, final Date currentDate, final Date previousDate, final Boolean currentMonthIncluded) {
        int noOfMonths;
        Date previousReadingDate = null;
        if (previousDate != null)
            previousReadingDate = previousDate;
        BigDecimal noOfUnitsPerMonth = BigDecimal.ZERO;
        if (isNotBlank(request.getParameter(METERCURRENTREADING)))
            meterReadingConnectionDetailObj.setCurrentReading(Long.valueOf(request.getParameter(METERCURRENTREADING)));
        meterReadingConnectionDetailObj.setCurrentReadingDate(currentDate);
        DateTime previousMidDate = null;
        if (previousReadingDate != null) {
            final DateTime previousDateTime = new DateTime(previousReadingDate);
            final int previousMidday = previousDateTime.dayOfMonth().getMaximumValue() / 2;
            previousMidDate = new DateTime(previousDateTime).withDayOfMonth(previousMidday);
        }
        final DateTime currentDateTime = new DateTime(currentDate);
        final int currentMidday = currentDateTime.dayOfMonth().getMaximumValue() / 2;
        final DateTime currentMidDate = new DateTime(currentDate).withDayOfMonth(currentMidday);

        populateMeterReadingDetails(meterReadingConnectionDetailObj, waterConnectionDetails);
        if (previousReadingDate == null) {
            noOfMonths = noOfMonthsBetween(waterConnectionDetails.getExecutionDate(), currentDate);
            previousReadingDate = waterConnectionDetails.getExecutionDate();
        } else
            noOfMonths = noOfMonthsBetween(previousReadingDate, currentDate);

        if (previousReadingDate != null && previousMidDate != null && previousReadingDate.before(previousMidDate.toDate()) &&
                !currentDate.before(currentMidDate.toDate()))
            noOfMonths++;

        if (!meterReadingConnectionDetailObj.isMeterDamaged()) {
            final Long currentToPreviousDiffOfUnits = Long.valueOf(request.getParameter(METERCURRENTREADING))
                    - previousReading;
            if (noOfMonths > 0)
                noOfUnitsPerMonth = BigDecimal.valueOf(currentToPreviousDiffOfUnits)
                        .divide(BigDecimal.valueOf(noOfMonths), 0, BigDecimal.ROUND_HALF_UP);
            else
                noOfUnitsPerMonth = BigDecimal.valueOf(currentToPreviousDiffOfUnits);
        }
        WaterConnectionDetails waterconnectionDetails = null;
        if (meterReadingConnectionDetailObj.isMeterDamaged())
            waterconnectionDetails = calculateDemandForDamagedMeter(waterConnectionDetails, previousReadingDate, noOfMonths,
                    currentMonthIncluded);
        else {
            final BigDecimal finalAmountToBePaid = calculateAmountTobePaid(waterConnectionDetails, noOfUnitsPerMonth)
                    .setScale(0, BigDecimal.ROUND_HALF_UP);

            if (finalAmountToBePaid.compareTo(BigDecimal.ZERO) > 0)
                waterconnectionDetails = connectionDemandService.updateDemandForMeteredConnection(waterConnectionDetails,
                        finalAmountToBePaid, currentDate, previousReadingDate, noOfMonths, currentMonthIncluded);
            else
                throw new ApplicationRuntimeException("err.no.amount.due");
        }
        return waterconnectionDetails;
    }

    private WaterConnectionDetails calculateDemandForDamagedMeter(final WaterConnectionDetails waterConnectionDetails,
            final Date previousDate, final int noOfMonths, final Boolean currentMonthIncluded) {
        BigDecimal amountValue;
        new WaterConnectionDetails();
        Installment currentInstallment;
        List<Installment> newInstallmentList;
        DateTime dateTime = new DateTime(previousDate);
        DateTime lastInstReadingDate;
        DateTime lastInstStartDate;

        newInstallmentList = installmentDao.getInstallmentsByModuleForGivenDateAndInstallmentType(
                moduleService.getModuleByName(MODULE_NAME), previousDate, MONTHLY);
        currentInstallment = connectionDemandService.getCurrentInstallment(MODULE_NAME, MONTHLY, new Date());
        if (newInstallmentList.isEmpty() || !newInstallmentList.contains(currentInstallment))
            newInstallmentList.add(currentInstallment);

        for (final Installment installment : newInstallmentList) {
            lastInstReadingDate = dateTime.minusMonths(6);
            lastInstStartDate = lastInstReadingDate.withDayOfMonth(1).withTimeAtStartOfDay();
            final List<Installment> lastInstallmentList = installmentDao
                    .getInstallmentsByModuleBetweenFromDateAndToDateAndInstallmentType(
                            moduleService.getModuleByName(MODULE_NAME),
                            lastInstStartDate.toDate(), installment.getFromDate(), MONTHLY);

            amountValue = calculateDamagedMeterAverageDemand(lastInstallmentList, waterConnectionDetails);
            if (amountValue.compareTo(BigDecimal.ZERO) > 0)
                connectionDemandService.updateDemandForMeteredConnection(waterConnectionDetails,
                        amountValue, installment.getFromDate(),
                        previousDate, noOfMonths, currentMonthIncluded);
            dateTime = new DateTime(installment.getFromDate());
            dateTime = dateTime.plusMonths(1);
        }
        return waterConnectionDetails;
    }

    private BigDecimal calculateAmountTobePaid(final WaterConnectionDetails waterConnectionDetails,
            final BigDecimal noOfUnitsPerMonth) {
        MeteredRates meteredRates = null;
        UsageSlab usageSlab = null;
        if (!noOfUnitsPerMonth.equals(BigDecimal.ZERO))
            usageSlab = usageSlabService
                    .getUsageSlabForWaterVolumeConsumed(waterConnectionDetails.getUsageType().getName(),
                            noOfUnitsPerMonth.longValue());
        if (usageSlab != null && usageSlab.getSlabName() != null)
            meteredRates = meteredRatesService.findBySlabName(usageSlab.getSlabName());
        else if (!noOfUnitsPerMonth.equals(BigDecimal.ZERO))
            throw new ApplicationRuntimeException("err.usageslab.not.present");
        BigDecimal amountToBeCollected;
        if (meteredRates == null || meteredRates.getSlabName() == null)
            throw new ApplicationRuntimeException(ERROR_METER_RATE_NOT_PRESENT);
        else
            amountToBeCollected = getAmountToBeCollected(meteredRates, usageSlab, noOfUnitsPerMonth);
        return amountToBeCollected;
    }

    public BigDecimal getAmountToBeCollected(final MeteredRates meteredRates, final UsageSlab usageSlab,
            final BigDecimal noOfUnitsPerMonth) {
        MeteredRatesDetail meteredRatesDetail;
        meteredRatesDetail = meteredRatesDetailService.getActiveRateforSlab(meteredRates.getSlabName(), new Date());
        if (meteredRatesDetail == null
                || meteredRatesDetail.getRateAmount() == null && meteredRatesDetail.getFlatAmount() == null)
            throw new ApplicationRuntimeException(ERROR_METER_RATE_NOT_PRESENT);
        else if (meteredRatesDetail.isRecursive())
            return calculateDemandWithRecursiveAmount(usageSlab, meteredRatesDetail, noOfUnitsPerMonth);
        else if (meteredRatesDetail.getRateAmount() != null && meteredRatesDetail.getRateAmount() != 0)
            return BigDecimal.valueOf(meteredRatesDetail.getRateAmount()).multiply(noOfUnitsPerMonth);
        else if (meteredRatesDetail.getFlatAmount() != null && meteredRatesDetail.getFlatAmount() != 0)
            return BigDecimal.valueOf(meteredRatesDetail.getFlatAmount());
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateDamagedMeterAverageDemand(final List<Installment> installmentList,
            final WaterConnectionDetails waterConnectionDetails) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        int count = 0;
        Set<EgDemandDetails> demandDtlSet = null;
        BigDecimal meterDemandAmount = BigDecimal.ZERO;
        final EgDemand demand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        if (demand != null)
            demandDtlSet = demand.getEgDemandDetails();
        for (final Installment installment : installmentList)
            for (final EgDemandDetails demandDetail : demandDtlSet)
                if (demandDetail.getEgDemandReason().getEgInstallmentMaster().equals(installment)) {
                    count++;
                    meterDemandAmount = meterDemandAmount.add(demandDetail.getAmount());
                }
        if (count != 0)
            totalAmount = meterDemandAmount.divide(BigDecimal.valueOf(count), 0, BigDecimal.ROUND_HALF_UP);
        return totalAmount;
    }

    private BigDecimal calculateDemandWithRecursiveAmount(final UsageSlab usageSlab, final MeteredRatesDetail meteredRatesDetail,
            final BigDecimal numberOfUnitsPerMonth) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal amtValue;
        BigDecimal amount1;
        BigDecimal amount2;
        if (meteredRatesDetail.getFlatAmount() != null && meteredRatesDetail.getFlatAmount() != 0
                && numberOfUnitsPerMonth.compareTo(BigDecimal.valueOf(usageSlab.getFromVolume())) > -1) {
            amtValue = numberOfUnitsPerMonth.subtract(BigDecimal.valueOf(usageSlab.getFromVolume())).add(BigDecimal.ONE)
                    .divide(BigDecimal.valueOf(meteredRatesDetail.getRecursiveFactor()), 0, BigDecimal.ROUND_CEILING);
            totalAmount = BigDecimal.valueOf(meteredRatesDetail.getFlatAmount()).add(amtValue
                    .multiply(BigDecimal.valueOf(meteredRatesDetail.getRecursiveAmount())));
        } else if (meteredRatesDetail.getRateAmount() != null && meteredRatesDetail.getRateAmount() != 0
                && numberOfUnitsPerMonth.compareTo(BigDecimal.valueOf(usageSlab.getFromVolume())) > -1) {
            amount1 = BigDecimal.valueOf(usageSlab.getFromVolume()).subtract(BigDecimal.ONE)
                    .multiply(BigDecimal.valueOf(meteredRatesDetail.getRateAmount()));
            amount2 = numberOfUnitsPerMonth
                    .subtract(BigDecimal.valueOf(usageSlab.getFromVolume()))
                    .add(BigDecimal.ONE)
                    .divide(BigDecimal.valueOf(meteredRatesDetail.getRecursiveFactor()), 0, BigDecimal.ROUND_CEILING);
            amtValue = amount2.multiply(BigDecimal.valueOf(meteredRatesDetail.getRecursiveAmount())).setScale(0,
                    BigDecimal.ROUND_HALF_UP);
            totalAmount = amount1.add(amtValue);
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
        return meterReadingConnectionDetails.getCurrentReading() == null
                && meterReadingConnectionDetails.getCurrentReadingDate() == null ? false : true;
    }

    public Boolean isPreviousMonthDemandExist(final DateTime previousMonthStartDate,
            final WaterConnectionDetails waterConnectionDetails) {
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                moduleService.getModuleByName(MODULE_NAME), previousMonthStartDate.toDate(), MONTHLY);
        final EgDemand demand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        if (installment != null && demand != null)
            for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                if (demandDetail.getEgDemandReason().getEgInstallmentMaster().getId() == installment.getId())
                    return true;
        return false;
    }
}