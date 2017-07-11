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

package org.egov.adtax.web.controller.rate;

import com.google.gson.GsonBuilder;
import org.egov.adtax.entity.AdvertisementRate;
import org.egov.adtax.entity.AdvertisementRatesDetails;
import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.entity.RatesClass;
import org.egov.adtax.entity.UnitOfMeasure;
import org.egov.adtax.service.AdvertisementRateService;
import org.egov.adtax.service.HoardingCategoryService;
import org.egov.adtax.service.RatesClassService;
import org.egov.adtax.service.UnitOfMeasureService;
import org.egov.commons.CFinancialYear;
import org.egov.infra.config.core.GlobalSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/rates")
public class ScheduleOfRateController {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleOfRateController.class);

    @Autowired
    private AdvertisementRateService advertisementRateService;

    @Autowired
    private HoardingCategoryService hoardingCategoryService;

    @Autowired
    private RatesClassService ratesClassService;

    @Autowired
    private UnitOfMeasureService unitOfMeasureService;

    @ModelAttribute("rate")
    public AdvertisementRate rate() {
        return new AdvertisementRate();
    }

    @ModelAttribute("hoardingCategories")
    public List<HoardingCategory> hoardingCategories() {
        return hoardingCategoryService.getAllActiveHoardingCategory();
    }

    @ModelAttribute("unitOfMeasures")
    public List<UnitOfMeasure> unitOfMeasures() {
        return unitOfMeasureService.getAllActiveUnitOfMeasure();
    }

    @ModelAttribute("ratesClasses")
    public List<RatesClass> ratesClasses() {
        return ratesClassService.getAllActiveRatesClass();
    }

    @ModelAttribute("financialYears")
    public List<CFinancialYear> financialyear() {

        return advertisementRateService.getAllFinancialYears();
    }

    @RequestMapping(value = "/subscheduleofrate", method = GET)
    public String newSubScheduleOfRate() {
        LOG.info("Inside Create Sub Schedule of rate ");
        return "subScheduleOfRate-create";
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
    public String searchForm(@Valid @ModelAttribute AdvertisementRate rate, final BindingResult errors,
                             final RedirectAttributes redirectAttrs, final Model model) {
        List<AdvertisementRatesDetails> advertisementRatesDetails;

        if (validateScheduleOfRateSearch(rate, model))
            return "scheduleOfRate-form";

        advertisementRatesDetails = advertisementRateService
                .findScheduleOfRateDetailsByCategorySubcategoryUomAndClass(rate.getCategory(), rate.getSubCategory(),
                        rate.getUnitofmeasure(), rate.getClasstype(), rate.getFinancialyear());

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

    @RequestMapping(value = "getHoardingDcb/{unitFrom}")
    public String viewHoarding(@PathVariable final String category, final Model model) {
        return "report-dcbview";
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
                rate.getSubCategory(), rate.getUnitofmeasure(), rate.getClasstype(), rate.getFinancialyear());

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
            existingRateobject.setUnitrate(rate.getUnitrate());
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

    @RequestMapping(value = "/searchscheduleofrate", method = GET)
    public String newSearchScheduleOfRate() {
        return "scheduleOfRate-search";
    }

    @RequestMapping(value = "/searchscheduleofrate", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchScheduleOfRate(final HttpServletRequest request,
                                       final HttpServletResponse response) {

        final String category = request.getParameter("category");
        final String subCategory = request.getParameter("subCategory");
        final String unitOfMeasure = request.getParameter("uom");
        final String classtype = request.getParameter("rateClass");
        final String finyear = request.getParameter("finyear");
        return "{ \"data\":" + new GsonBuilder().setDateFormat(GlobalSettings.datePattern()).create()
                .toJson(advertisementRateService.getScheduleOfRateSearchResult(category, subCategory, unitOfMeasure, classtype, finyear)) + "}";

    }

}
