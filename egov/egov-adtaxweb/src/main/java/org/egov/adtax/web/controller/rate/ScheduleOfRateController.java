package org.egov.adtax.web.controller.rate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.adtax.entity.AdvertisementRate;
import org.egov.adtax.entity.AdvertisementRatesDetails;
import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.entity.RatesClass;
import org.egov.adtax.entity.UnitOfMeasure;
import org.egov.adtax.service.AdvertisementRateService;
import org.egov.adtax.service.HoardingCategoryService;
import org.egov.adtax.service.RatesClassService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.service.UnitOfMeasureService;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/rates")
public class ScheduleOfRateController {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleOfRateController.class);

    @Autowired
    private AdvertisementRateService advertisementRateService;

    @Autowired
    private HoardingCategoryService hoardingCategoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private RatesClassService ratesClassService;

    @ModelAttribute("rate")
    public AdvertisementRate rate() {
        return new AdvertisementRate();
    }

    @Autowired
    private UnitOfMeasureService unitOfMeasureService;

    public @ModelAttribute("hoardingCategories") List<HoardingCategory> hoardingCategories() {
        return hoardingCategoryService.getAllActiveHoardingCategory();
    }

    public @ModelAttribute("unitOfMeasures") List<UnitOfMeasure> unitOfMeasures() {
        return unitOfMeasureService.getAllActiveUnitOfMeasure();
    }

    public @ModelAttribute("ratesClasses") List<RatesClass> ratesClasses() {
        return ratesClassService.getAllActiveRatesClass();
    }

    @RequestMapping(value = "/search", method = GET)
    public String newScheduleOfRate() {
        LOG.info("Inside Schedule of rate ");
        return "scheduleOfRate-form";

    }

    /**
     * @param rate
     * @param errors
     * @param redirectAttrs
     * @param model
     * @return
     */
    @RequestMapping(value = "/search", method = POST)
    public String searchForm (@Valid @ModelAttribute AdvertisementRate rate, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        List<AdvertisementRatesDetails> advertisementRatesDetails = new ArrayList<AdvertisementRatesDetails>();
       
        if (validateScheduleOfRateSearch(rate, model))
            return "scheduleOfRate-form";

        advertisementRatesDetails = advertisementRateService
                .findScheduleOfRateDetailsByCategorySubcategoryUomAndClass(rate.getCategory(), rate.getSubCategory(),
                        rate.getUnitofmeasure(), rate.getClasstype());

        if (advertisementRatesDetails.size() == 0) {
            advertisementRatesDetails.add(new AdvertisementRatesDetails());
            rate.setAdvertisementRatesDetails(advertisementRatesDetails);
            model.addAttribute("mode", "noDataFound");
        } else {
            rate = advertisementRatesDetails.get(0).getAdvertisementRate();
            model.addAttribute("mode", "dataFound");
        }
        model.addAttribute("rate", rate);
        redirectAttrs.addFlashAttribute("rate", rate);
        return "scheduleOfRate-result";
    }

    /**
     * @param rate
     * @param redirectAttrs
     * @param model
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute AdvertisementRate rate, final RedirectAttributes redirectAttrs,
            final Model model) {

        AdvertisementRate existingRateobject = null;
        final List<AdvertisementRatesDetails> rateDetails = new ArrayList<AdvertisementRatesDetails>();

        // TODO: validate, whether details are correct

        existingRateobject = advertisementRateService.findScheduleOfRateByCategorySubcategoryUomAndClass(rate.getCategory(),
                rate.getSubCategory(), rate.getUnitofmeasure(), rate.getClasstype());
       
        for (final AdvertisementRatesDetails advDtl : rate.getAdvertisementRatesDetails()) {
            if (existingRateobject != null)
                advDtl.setAdvertisementRate(existingRateobject);
            else
                advDtl.setAdvertisementRate(rate);
            rateDetails.add(advDtl);
        }

        if (existingRateobject != null) {
            advertisementRateService.deleteAllInBatch(existingRateobject.getAdvertisementRatesDetails());
            existingRateobject.setAdvertisementRatesDetails(rateDetails);
            rate = advertisementRateService.createScheduleOfRate(existingRateobject);
        } else {
            rate.getAdvertisementRatesDetails().clear();
            rate.setAdvertisementRatesDetails(rateDetails);
            rate = advertisementRateService.createScheduleOfRate(rate);
        }
        redirectAttrs.addFlashAttribute("agency", rate);
        redirectAttrs.addFlashAttribute("message", "message.scheduleofrate.create");
        return "redirect:/rates/success/" + rate.getId();
      
    }

    /**
     * @param id
     * @param rate
     * @return
     */
    @RequestMapping(value = "/success/{id}", method = GET)
    public ModelAndView successView(@PathVariable("id") final Long id, @ModelAttribute final AdvertisementRate rate) {
        return new ModelAndView("scheduleOfRate-success", "rate", advertisementRateService.getScheduleOfRateById(id));

    }

    /**
     * @param rate
     * @param model
     * @return
     */
    private Boolean validateScheduleOfRateSearch(final AdvertisementRate rate, final Model model) {
        Boolean validate = false;
        if (rate != null) {
            if (rate.getCategory() == null || rate.getCategory().getId() == null) {
                model.addAttribute("message", "message.category.ismandatory");
                validate = true;
            }
            if (rate.getSubCategory() == null || rate.getSubCategory().getId() == null) {
                model.addAttribute("message", "message.subcategory.ismandatory");
                validate = true;
            }
            if (rate.getUnitofmeasure() == null || rate.getUnitofmeasure().getId() == null) {
                model.addAttribute("message", "message.uom.ismandatory");
                validate = true;
            }
        }
        return validate;
    }

}
