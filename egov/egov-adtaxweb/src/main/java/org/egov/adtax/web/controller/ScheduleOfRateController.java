package org.egov.adtax.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.entity.AdvertisementRate;
import org.egov.adtax.entity.UnitOfMeasure;
import org.egov.adtax.service.HoardingCategoryService;
import org.egov.adtax.service.RatesService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.service.UnitOfMeasureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/rates")
public class ScheduleOfRateController {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleOfRateController.class);
    
    @Autowired
    private  RatesService ratesService;
   
    @Autowired
    private  HoardingCategoryService hoardingCategoryService;

    @Autowired
    private  SubCategoryService subCategoryService;

    @Autowired
    private  UnitOfMeasureService unitOfMeasureService;
    
    public @ModelAttribute("hoardingCategories") List<HoardingCategory> hoardingCategories() {
        return hoardingCategoryService.getAllActiveHoardingCategory();
    }   
    
    public @ModelAttribute("unitOfMeasures") List<UnitOfMeasure> unitOfMeasures() {
        return unitOfMeasureService.getAllActiveUnitOfMeasure();
    } 
    
    @RequestMapping(value = "/scheduleOfRate-onload", method = GET)
    public String newScheduleOfRate(@ModelAttribute final AdvertisementRate rates,
            final Model model) {
        LOG.info("Inside Schedule of rate ");
          return "scheduleOfRate-form";
    
    }
}
