package org.egov.wtms.web.controller.application;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.validation.Valid;

import org.egov.wtms.masters.entity.DonationMaster;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.masters.service.DonationMasterService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class DonationMasterController extends GenericConnectionController {

    @Autowired
    private DonationMasterService donationMasterService;

    private final PropertyTypeService propertyTypeService;

    private final ConnectionCategoryService connectionCategoryService;

    private final UsageTypeService usageTypeService;

    private final PipeSizeService pipeSizeService;

    public static final String CONTENTTYPE_JSON = "application/json";

    @Autowired
    public DonationMasterController(final PropertyTypeService propertyTypeService,
            final ConnectionCategoryService connectionCategoryService, final UsageTypeService usageTypeService,
            final PipeSizeService pipeSizeService) {
        this.propertyTypeService = propertyTypeService;
        this.connectionCategoryService = connectionCategoryService;
        this.usageTypeService = usageTypeService;
        this.pipeSizeService = pipeSizeService;

    }

    @RequestMapping(value = "/donationMaster", method = GET)
    public String viewForm(final Model model) {
        final DonationMaster donationMaster = new DonationMaster();
        model.addAttribute("donationMaster", donationMaster);
        model.addAttribute("typeOfConnection", WaterTaxConstants.DONATIONMASTER);
        model.addAttribute("categoryType", connectionCategoryService.getAllActiveConnectionCategory());
        model.addAttribute("propertyType", propertyTypeService.getAllActivePropertyTypes());
        model.addAttribute("usageType", usageTypeService.getActiveUsageTypes());
        model.addAttribute("maxPipeSize", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("minPipeSize", pipeSizeService.getAllActivePipeSize());
        return "donation-master";
    }

    @RequestMapping(value = "/donationMaster", method = RequestMethod.POST)
    public String addDonationMasterDetails(@Valid @ModelAttribute final DonationMaster donationMaster,
            final RedirectAttributes redirectAttrs, final Model model, final BindingResult resultBinder) {
        System.out.println(donationMaster.getPropertyType());
        if (!resultBinder.hasErrors())
            return "donation-master";
        donationMaster.setActive(Boolean.TRUE);
        donationMasterService.createDonationMaster(donationMaster);
        redirectAttrs.addFlashAttribute("donationMaster", donationMaster);
        model.addAttribute("message", "Donation Master Data created successfully");
        return "donation-master-success";
    }
}
