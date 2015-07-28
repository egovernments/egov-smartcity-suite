package org.egov.wtms.web.controller.application;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;

import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class GenericBillGeneratorController {

    private final WaterConnectionDetailsService waterConnectionDetailsService;
    private final ConnectionDemandService connectionDemandService;

    @Autowired
    public GenericBillGeneratorController(final WaterConnectionDetailsService waterConnectionDetailsService,
            final ConnectionDemandService connectionDemandService) {
        this.waterConnectionDetailsService = waterConnectionDetailsService;
        this.connectionDemandService = connectionDemandService;
    }

    @RequestMapping(value = "/generatebill/{consumerCode}", method = GET)
    public String showCollectFeeForm(final Model model, @PathVariable final String consumerCode) {
        return "redirect:/application/collecttax-view?consumerCode=" + consumerCode;
    }

    @RequestMapping(value = "/collecttax-view", method = GET)
    public ModelAndView collectTaxView(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
            final HttpServletRequest request, final Model model) {
        if (request.getParameter("consumerCode") != null)
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(request
                    .getParameter("consumerCode"));
        model.addAttribute(
                "connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(
                        waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        return new ModelAndView("application/collecttax-view", "waterConnectionDetails", waterConnectionDetails);
    }

    @RequestMapping(value = "/generatebill/{consumerCode}", method = POST)
    public String payTax(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
            final RedirectAttributes redirectAttributes, @PathVariable final String consumerCode, final Model model) {
        waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerCode);
        model.addAttribute("collectxml", connectionDemandService.generateBill(consumerCode));
        return "collecttax-redirection";
    }

}
