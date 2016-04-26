package org.egov.adtax.web.controller.penaltyRates;

import java.util.ArrayList;
import java.util.List;

import org.egov.adtax.entity.AdvertisementPenaltyRates;
import org.egov.adtax.search.contract.HoardingPenaltyRates;
import org.egov.adtax.service.AdvertisementPenaltyRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "penalty")
public class AdvertisementPenaltyRatesController {

    @Autowired
    private AdvertisementPenaltyRatesService penaltyRatesService;

    @RequestMapping(value = "/change")
    public String changePenaltyRates(@ModelAttribute HoardingPenaltyRates hoardingPenaltyRates, final Model model) {
        hoardingPenaltyRates.setAdvtPenaltyRatesList(penaltyRatesService.findPenaltyRatesInAscendingOrder());
        return "penaltyRates-change";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createPenaltyRates(@ModelAttribute HoardingPenaltyRates hoardingPenaltyRates,
            final RedirectAttributes redirectAttributes, @RequestParam final Long id) {
        List<AdvertisementPenaltyRates> rateList = new ArrayList<AdvertisementPenaltyRates>();
        rateList = penaltyRatesService.findPenaltyRatesInAscendingOrder();

        if (hoardingPenaltyRates != null) {

            for (AdvertisementPenaltyRates rate : rateList) {

                for (AdvertisementPenaltyRates advtPenaltyRates : hoardingPenaltyRates.getAdvtPenaltyRatesList()) {

                    if (advtPenaltyRates.getId() != null && rate.getId().equals(advtPenaltyRates.getId())) {
                        penaltyRatesService.createPenaltyRates(advtPenaltyRates);
                    } else if (advtPenaltyRates.getId() == null) {
                        penaltyRatesService.createPenaltyRates(advtPenaltyRates);
                    }
                }
            }
            for (AdvertisementPenaltyRates rate : rateList) {
                if (!hoardingPenaltyRates.getAdvtPenaltyRatesList().contains(rate)) {
                    penaltyRatesService.delete(rate);
                }
            }
        }
        return "penaltyRate-success";
    }
}
