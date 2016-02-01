package org.egov.adtax.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.egov.adtax.entity.AdvertisementRatesDetails;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.service.AdvertisementRateService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxController {
    
    @Autowired
    private   SubCategoryService subCategoryService;
    protected @Autowired AdvertisementRateService advertisementRateService;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @RequestMapping(value = "/ajax-subCategories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<SubCategory> getDesignations(@RequestParam final Long category,
                        final HttpServletResponse response) {
        
         List<SubCategory> subCategoryList = new ArrayList<SubCategory>();
        if (category != null && !"".equals(category)) {
            subCategoryList = subCategoryService.getAllActiveSubCategoryByCategoryId(category);
            subCategoryList.forEach(subCategory -> subCategory.toString());
        }
        return subCategoryList;
    }
    
    @RequestMapping(value = "/hoarding/calculateTaxAmount", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Double getTaxAmount(@RequestParam final Long unitOfMeasureId,
            @RequestParam final Double measurement, @RequestParam final Long subCategoryId,
            @RequestParam final Long rateClassId) {
        AdvertisementRatesDetails rate = null;

        rate = advertisementRateService.getRatesBySubcategoryUomClassAndMeasurementByFinancialYearInDecendingOrder(
                subCategoryId, unitOfMeasureId, rateClassId, measurement);

        if (rate != null) {
            // get data based on financial year, if not present, get from
            // previous year data.
            // MULTIPLY WITH MEASUREMENT TO GET TOTAL AMOUNT.

            // CHECK WHETHER CALCULATION REQUIRED BASED ON PERUNIT BASIS OR NORMAL
            // WAY ?
            final List<AppConfigValues> calculateSorByUnit = appConfigValuesService.getConfigValuesByModuleAndKey(
                    AdvertisementTaxConstants.MODULE_NAME, AdvertisementTaxConstants.CALCULATESORBYUNIT);
            if (!calculateSorByUnit.isEmpty())
                if (calculateSorByUnit.get(0).getValue().equalsIgnoreCase("NO"))
                    return BigDecimal.valueOf(rate.getAmount()).multiply(BigDecimal.valueOf(measurement))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                else if (calculateSorByUnit.get(0).getValue().equalsIgnoreCase("YES")) {

                    final BigDecimal unitRate = rate.getAdvertisementRate().getUnitrate() != null ? BigDecimal.valueOf(rate
                            .getAdvertisementRate().getUnitrate()) : BigDecimal.ZERO;

                    // MULTIPLY WITH MEASUREMENT TO GET TOTAL AMOUNT.
                    if (unitRate != BigDecimal.valueOf(0))
                        return BigDecimal
                                .valueOf(rate.getAmount())
                                .multiply(
                                        BigDecimal.valueOf(measurement).divide(unitRate, 2, RoundingMode.HALF_UP)
                                                .setScale(0, RoundingMode.UP))
                                .setScale(2, BigDecimal.ROUND_HALF_UP)
                                .doubleValue();
                    else
                        return Double.valueOf(0);
                }
        }

        return Double.valueOf(0);

    }
}
