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
package org.egov.wtms.web.controller.application;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.wtms.application.entity.MeterReadingConnectionDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.MeterReadingConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.MeteredRates;
import org.egov.wtms.masters.entity.MeteredRatesDetail;
import org.egov.wtms.masters.entity.UsageSlab;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.repository.WaterRatesDetailsRepository;
import org.egov.wtms.masters.service.MeteredRatesDetailService;
import org.egov.wtms.masters.service.MeteredRatesService;
import org.egov.wtms.masters.service.UsageSlabService;
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

@Controller
@RequestMapping(value = "/application")
public class MeterReadingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeterReadingController.class);
    private static final String PREVIOUSREADING = "previousreading";
    private static final String NEWCONNECTIONMETERENTRY = "newconnection-meterEntry";
    private static final String METERCURRENTREADING = "metercurrentReading";
    private static final String REDIRECT_TO_METERDEMANDNOTICE = "redirect:/application/meterdemandnotice?pathVar=";

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private UsageSlabService usageSlabService;

    @Autowired
    private MeteredRatesService meteredRatesService;

    @Autowired
    private MeteredRatesDetailService meteredRatesDetailService;

    @Autowired
    private MeterReadingConnectionDetailsService meterReadingConnectionDetailsService;

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
        final MeterReadingConnectionDetails meterReadingConnectionDeatilObj = new MeterReadingConnectionDetails();
        Long previousReading = 0l;
        Boolean meterDamaged;
        if ("true".equals(request.getParameter("waterConnectionDetails.meterConnection.isMeterDamaged"))) {
            meterDamaged = true;
            meterReadingConnectionDeatilObj.setMeterDamaged(meterDamaged);
        }
        if (errors.hasErrors())
            return NEWCONNECTIONMETERENTRY;

        if (null != request.getParameter(PREVIOUSREADING) && !"".equals(request.getParameter(PREVIOUSREADING)))
            previousReading = Long.valueOf(request.getParameter(PREVIOUSREADING));

        if (Long.valueOf(request.getParameter(METERCURRENTREADING)) < previousReading) {
            final String message = "Current rate should not be less than Previous reading";
            model.addAttribute("message", message);
            return NEWCONNECTIONMETERENTRY;
        }
        final WaterConnectionDetails waterconnectionDetails = billCalculationAndDemandUpdate(waterConnectionDetails, request,
                meterReadingConnectionDeatilObj, previousReading, dateFormat);
        final WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterconnectionDetails);
        waterConnectionDetailsService.updateIndexes(savedWaterConnectionDetails, sourceChannel);
        return REDIRECT_TO_METERDEMANDNOTICE
                + savedWaterConnectionDetails.getConnection().getConsumerCode();
    }

    private WaterConnectionDetails billCalculationAndDemandUpdate(final WaterConnectionDetails waterConnectionDetails,
            final HttpServletRequest request,
            final MeterReadingConnectionDetails meterReadingConnectionDeatilObj, final Long previousReading,
            final SimpleDateFormat dateFormat) {
        Date currentDate = null;
        Date previousDate = null;
        int noofmonths;

        final String readingDate = request.getParameter("metercurrentReadingDate");

        try {
            currentDate = dateFormat.parse(readingDate);
            if (request.getParameter("previousreadingDate") != null)
                previousDate = dateFormat.parse(request.getParameter("previousreadingDate"));
        } catch (final ParseException e) {
            LOGGER.error("Exception while parsing date " + e);
        }
        meterReadingConnectionDeatilObj.setCurrentReading(Long.valueOf(request.getParameter(METERCURRENTREADING)));
        meterReadingConnectionDeatilObj.setCurrentReadingDate(currentDate);

        populateMeterReadingDetails(meterReadingConnectionDeatilObj, waterConnectionDetails);
        if (previousDate != null)
            noofmonths = DateUtils.noOfMonthsBetween(previousDate, currentDate);
        else
            noofmonths = DateUtils.noOfMonthsBetween(new Date(), currentDate);
        final Long currentToPreviousDiffOfUnits = Long.valueOf(request.getParameter(METERCURRENTREADING))
                - previousReading;
        Long noOfUnitsForPerMonth;
        if (noofmonths > 0)
            noOfUnitsForPerMonth = currentToPreviousDiffOfUnits / noofmonths;
        else
            noOfUnitsForPerMonth = currentToPreviousDiffOfUnits;

        final double finalAmountToBePaid = calculateAmountTobePaid(waterConnectionDetails, noofmonths,
                noOfUnitsForPerMonth);
        WaterConnectionDetails waterconnectionDetails;
        if (BigDecimal.valueOf(finalAmountToBePaid).compareTo(BigDecimal.ZERO) > 0)
            waterconnectionDetails = connectionDemandService.updateDemandForMeteredConnection(waterConnectionDetails,
                    BigDecimal.valueOf(finalAmountToBePaid), currentDate);
        else
            throw new ApplicationRuntimeException("err.no.amount.due");
        return waterconnectionDetails;
    }

    private double calculateAmountTobePaid(final WaterConnectionDetails waterConnectionDetails, final int noofmonths,
            final Long noOfUnitsForPerMonth) {
        MeteredRates meteredRates = null;
        MeteredRatesDetail meteredRatesDetail;
        Double amountToBeCollected = 0d;
        Long averageUnitsPerMonth = 0l;
        if (!waterConnectionDetails.getMeterConnection().isEmpty()
                && waterConnectionDetails.getMeterConnection().get(0).isMeterDamaged()) {
            final DateTime datetime = new DateTime();
            int count = 0;
            Long noOfVolumeConsumed = 0l;
            List<MeterReadingConnectionDetails> meterConnDetailList;
            final DateTime lastInstReadingDate = datetime.minusMonths(6);
            final DateTime lastInstReadingStartDate = lastInstReadingDate.withDayOfMonth(1).withTimeAtStartOfDay();
            meterConnDetailList = meterReadingConnectionDetailsService.getLastSixMonthsMeterReadings(waterConnectionDetails,
                    lastInstReadingStartDate);
            if (!meterConnDetailList.isEmpty() && meterConnDetailList.size() >= 2) {
                for (int i = 1; i < meterConnDetailList.size(); i++) {
                    count++;
                    noOfVolumeConsumed = noOfVolumeConsumed + (meterConnDetailList.get(i - 1).getCurrentReading()
                            - meterConnDetailList.get(i).getCurrentReading());
                }
                if (count != 0)
                    averageUnitsPerMonth = noOfVolumeConsumed / count;
            } else
                averageUnitsPerMonth = noOfUnitsForPerMonth;
        } else
            averageUnitsPerMonth = noOfUnitsForPerMonth;

        final UsageSlab usageSlab = usageSlabService
                .getUsageSlabForWaterVolumeConsumed(waterConnectionDetails.getUsageType().getName(), averageUnitsPerMonth);
        if (usageSlab != null && usageSlab.getSlabName() != null)
            meteredRates = meteredRatesService.findBySlabName(usageSlab.getSlabName());
        else
            throw new ApplicationRuntimeException("err.usageslab.not.present");
        if (meteredRates != null && meteredRates.getSlabName() != null) {
            meteredRatesDetail = meteredRatesDetailService.getActiveRateforSlab(meteredRates.getSlabName(), new Date());
            if (meteredRatesDetail != null) {
                if (meteredRatesDetail.getRateAmount() == null)
                    throw new ApplicationRuntimeException("err.metered.rate.not.present");
                else if (noofmonths > 0 && meteredRatesDetail.getRateAmount() != null && meteredRatesDetail.getRateAmount() != 0)
                    amountToBeCollected = noofmonths * meteredRatesDetail.getRateAmount();
                else
                    amountToBeCollected = meteredRatesDetail.getRateAmount();
            } else
                throw new ApplicationRuntimeException("err.metered.rate.not.present");
        }
        return amountToBeCollected;
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