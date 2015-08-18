package org.egov.wtms.web.controller.application;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.utils.DateUtils;
import org.egov.wtms.application.entity.MeterReadingConnectionDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.WaterRatesDetails;
import org.egov.wtms.masters.repository.WaterRatesDetailsRepository;
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

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;
    private WaterConnectionDetails waterConnectionDetails;
    private final WaterConnectionDetailsRepository waterConnectionDetailsRepository;
    private final WaterRatesDetailsRepository waterRatesDetailsRepository;
    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    public MeterReadingController(final WaterConnectionDetailsRepository waterConnectionDetailsRepository,
            final WaterRatesDetailsRepository waterRatesDetailsRepository) {
        this.waterConnectionDetailsRepository = waterConnectionDetailsRepository;
        this.waterRatesDetailsRepository = waterRatesDetailsRepository;
    }

    @ModelAttribute
    public WaterConnectionDetails getWaterConnectionDetails(@PathVariable final String consumerCode) {
        waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerCode);
        return waterConnectionDetails;
    }

    @RequestMapping(value = "/meterentry/{consumerCode}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String consumerCode, final HttpServletRequest request) {
        MeterReadingConnectionDetails meterReadingpriviousObj = null;
        final List<MeterReadingConnectionDetails> meterReadingpriviousObjlist = waterConnectionDetailsRepository
                .findPreviousMeterReadingReading(waterConnectionDetails.getId());
        if (!meterReadingpriviousObjlist.isEmpty())
            meterReadingpriviousObj = meterReadingpriviousObjlist.get(0);
        else {
            meterReadingpriviousObj = new MeterReadingConnectionDetails();
            if (waterConnectionDetails.getConnection().getInitialReading() != null)
                meterReadingpriviousObj.setCurrentReading(waterConnectionDetails.getConnection().getInitialReading());
                //Todo needs to add MeterReading date to Tap Execution date
            else
            meterReadingpriviousObj.setCurrentReading(0l);
        }
        model.addAttribute("mode", "meterEntry");
        model.addAttribute("meterReadingpriviousObj", meterReadingpriviousObj);
        model.addAttribute("meterReadingCurrentObj", new MeterReadingConnectionDetails());
        return "newconnection-meterEntry";
    }

    @RequestMapping(value = "/meterentry/{consumerCode}", method = RequestMethod.POST)
    public String updateMeterEntry(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
            final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model,
            final HttpServletRequest request) {
        final MeterReadingConnectionDetails meterReadingConnectionDeatilObj = new MeterReadingConnectionDetails();
        Long previousReading = 0l;
        if (errors.hasErrors())
            return "newconnection-meterEntry";
        if (null != request.getParameter("previousreading") && !"".equals(request.getParameter("previousreading")))
            previousReading = Long.valueOf(request.getParameter("previousreading"));

        if (Long.valueOf(request.getParameter("metercurrentReading")) < previousReading) {
            final String message = "Current rate should not be less than Previous reading";
            model.addAttribute("message", message);
            return "newconnection-meterEntry";
        }
        waterConnectionDetails = billCalculationAndDemandUpdate(waterConnectionDetails, errors, model, request,
                meterReadingConnectionDeatilObj, previousReading);
        final WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterConnectionDetails);
        redirectAttrs.addFlashAttribute("waterConnectionDetails", savedWaterConnectionDetails);

        return "newconnection-success";
    }

    private WaterConnectionDetails billCalculationAndDemandUpdate(WaterConnectionDetails waterConnectionDetails,
            final BindingResult errors, final Model model, final HttpServletRequest request,
            final MeterReadingConnectionDetails meterReadingConnectionDeatilObj, final Long previousReading) {
        Date currentDate = null;
        Date previousDate = null;
        int noofmonths = 0;
        final String readingDate = request.getParameter("metercurrentReadingDate");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            currentDate = dateFormat.parse(readingDate);
            if(request.getParameter("previousreadingDate")!=null){
            previousDate = dateFormat.parse(request.getParameter("previousreadingDate"));
            }
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        meterReadingConnectionDeatilObj.setCurrentReading(Long.valueOf(request.getParameter("metercurrentReading")));
        meterReadingConnectionDeatilObj.setCurrentReadingDate(currentDate);

        populateMeterReadingDetails(meterReadingConnectionDeatilObj);
        if(previousDate !=null){
        noofmonths = DateUtils.noOfMonths(previousDate, currentDate);
        }else{
            noofmonths = DateUtils.noOfMonths(new Date(), currentDate); 
        }
        final Long currentToPreviousDiffOfUnits = Long.valueOf(request.getParameter("metercurrentReading"))
                - previousReading;
        Long noOfUnitsForPerMonth = 0l;
        if (noofmonths > 0)
            noOfUnitsForPerMonth = currentToPreviousDiffOfUnits / noofmonths;
        else
            noOfUnitsForPerMonth = currentToPreviousDiffOfUnits;
        WaterRatesDetails waterRateDetail = null;
        final List<WaterRatesDetails> waterDetList = waterRatesDetailsRepository
                .findByWaterRate(waterConnectionDetails.getConnectionType(), waterConnectionDetails.getUsageType(),
                        noOfUnitsForPerMonth);
        if (!waterDetList.isEmpty())
            waterRateDetail = waterDetList.get(0);
        final double amountToBeCollectedWithUnitRatePerMonth = noOfUnitsForPerMonth
                * (waterRateDetail != null ? waterRateDetail.getUnitRate() : 0d);
        double finalAmountToBePaid = 0d;
        if (noofmonths > 0)
            finalAmountToBePaid = amountToBeCollectedWithUnitRatePerMonth * noofmonths / 1000;
        else
            finalAmountToBePaid = amountToBeCollectedWithUnitRatePerMonth / 1000;

        if (BigDecimal.valueOf(finalAmountToBePaid).compareTo(BigDecimal.ZERO) > 0)
            waterConnectionDetails = connectionDemandService.updateDemandForMeteredConnection(waterConnectionDetails,
                    BigDecimal.valueOf(finalAmountToBePaid));
        return waterConnectionDetails;
    }

    private void populateMeterReadingDetails(final MeterReadingConnectionDetails meterReadingConnectionDeatilObj) {
        final List<MeterReadingConnectionDetails> meterentryDetailsList = new ArrayList<MeterReadingConnectionDetails>(
                0);
        if (meterReadingConnectionDeatilObj != null)
            if (validMeterEntryDetail(meterReadingConnectionDeatilObj)) {
                meterReadingConnectionDeatilObj.setWaterConnectionDetails(waterConnectionDetails);
                meterentryDetailsList.add(meterReadingConnectionDeatilObj);
            }
        waterConnectionDetails.getMeterConnection().clear();
        waterConnectionDetails.setMeterConnection(meterentryDetailsList);
    }

    private boolean validMeterEntryDetail(final MeterReadingConnectionDetails meterReadingConnectionDetails) {
        if (meterReadingConnectionDetails == null || meterReadingConnectionDetails != null
                && meterReadingConnectionDetails.getCurrentReading() == null
                && meterReadingConnectionDetails.getCurrentReadingDate() == null)
            return false;
        return true;
    }

}
