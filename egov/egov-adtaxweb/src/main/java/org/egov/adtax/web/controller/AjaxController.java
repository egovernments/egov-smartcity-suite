package org.egov.adtax.web.controller;

import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.service.AdvertisementRateService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.service.penalty.AdvertisementTaxCalculator;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.ptis.wtms.PropertyIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class AjaxController {

    @Autowired
    private SubCategoryService subCategoryService;
    protected @Autowired AdvertisementRateService advertisementRateService;
    
    private  @Autowired AdvertisementTaxCalculator advertisementTaxCalculator;
    @Autowired
    @Qualifier("propertyIntegrationServiceImpl")
    private PropertyIntegrationService propertyIntegrationService;

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

    @RequestMapping(value = "/ajax-assessmentDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AssessmentDetails getAssessmentDetails(@RequestParam String assessmentNoRequest) {

        AssessmentDetails assessmentDetail = propertyIntegrationService.getAssessmentDetailsForFlag(
                assessmentNoRequest, PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
        return assessmentDetail;

    }

    @RequestMapping(value = "/hoarding/calculateTaxAmount", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Double getTaxAmount(@RequestParam final Long unitOfMeasureId,
            @RequestParam final Double measurement, @RequestParam final Long subCategoryId,
            @RequestParam final Long rateClassId) {

        return advertisementTaxCalculator.calculateTaxAmount(unitOfMeasureId, measurement, subCategoryId, rateClassId);

    }
}
