package org.egov.adtax.web.controller.reports;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.service.HoardingService;
import org.egov.adtax.web.controller.GenericController;
import org.egov.infra.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/reports")
public class DcbReportController extends GenericController {

    @Autowired
    private HoardingService hoardingService;
    @Autowired
    private ApplicationProperties applicationProperties;

    @RequestMapping(value = "search-for-dcbreport", method = GET)
    public String searchHoardingForm(@ModelAttribute final HoardingSearch hoardingSearch) {
        return "report-dcb";
    }

    @RequestMapping(value = "search-for-dcbreport", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchHoarding(@ModelAttribute final HoardingSearch hoardingSearch) {
        return "{ \"data\":"
                + new GsonBuilder().setDateFormat(applicationProperties.defaultDatePattern()).create()
                .toJson(hoardingService.getHoardingSearchResult(hoardingSearch)) + "}";
    }

    @RequestMapping(value = "getHoardingDcb/{hoardingNumber}")
    public String viewHoarding(@PathVariable final String hoardingNumber, final Model model) {
        final Hoarding hoarding = hoardingService.getHoardingByHoardingNumber(hoardingNumber);
        model.addAttribute("hoarding", hoarding);
        model.addAttribute("dcbResult", hoardingService.getHoardingWiseDCBResult(hoarding));
        return "report-dcbview";
    }
}
