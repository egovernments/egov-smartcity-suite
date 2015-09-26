/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.adtax.web.controller.hoarding;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.entity.HoardingDocument;
import org.egov.adtax.entity.HoardingDocumentType;
import org.egov.adtax.entity.RatesClass;
import org.egov.adtax.entity.RevenueInspector;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.entity.UnitOfMeasure;
import org.egov.adtax.service.AdvertisementRateService;
import org.egov.adtax.service.HoardingCategoryService;
import org.egov.adtax.service.HoardingDocumentTypeService;
import org.egov.adtax.service.HoardingService;
import org.egov.adtax.service.RatesClassService;
import org.egov.adtax.service.RevenueInspectorService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.service.UnitOfMeasureService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.utils.FileStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hoarding")
public class CreateHoardingController {

    private @Autowired HoardingService hoardingService;

    private @Autowired HoardingCategoryService hoardingCategoryService;

    private @Autowired UnitOfMeasureService unitOfMeasureService;

    private @Autowired SubCategoryService subCategoryService;

    private @Autowired RevenueInspectorService revenueInspectorService;

    private @Autowired RatesClassService ratesClassService;

    private @Autowired HoardingDocumentTypeService hoardingDocumentTypeService;

    private @Autowired FileStoreUtils fileStoreUtils;

    private @Autowired BoundaryService boundaryService;

    private @Autowired AdvertisementRateService advertisementRateService;

    @ModelAttribute
    public Hoarding hoarding() {
        return new Hoarding();
    }

    @ModelAttribute("hoardingCategories")
    public List<HoardingCategory> hoardingCategories() {
        return hoardingCategoryService.getAllActiveHoardingCategory();
    }

    @ModelAttribute("uom")
    public List<UnitOfMeasure> uom() {
        return unitOfMeasureService.getAllActiveUnitOfMeasure();
    }

    @ModelAttribute("revenueInspectors")
    public List<RevenueInspector> revenueInspectors() {
        return revenueInspectorService.findAllActiveRevenueInspectors();
    }

    @ModelAttribute("rateClasses")
    public List<RatesClass> rateClasses() {
        return ratesClassService.getAllActiveRatesClass();
    }

    @ModelAttribute("hoardingDocumentTypes")
    public List<HoardingDocumentType> hoardingDocumentTypes() {
        return hoardingDocumentTypeService.getAllDocumentTypes();
    }

    @ModelAttribute("revenueZones")
    public List<Boundary> revenueZones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone", "ELECTION");
    }
    
    @ModelAttribute("zones")
    public List<Boundary> zones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone", "ADMINISTRATION");
    }

    @RequestMapping(value = "child-boundaries", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<Boundary> childBoundaries(@RequestParam final Long parentBoundaryId) {
        return boundaryService.getActiveChildBoundariesByBoundaryId(parentBoundaryId);
    }
    
    
    @RequestMapping(value = "calculateTaxAmount", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Double getTaxAmount(@RequestParam final Long unitOfMeasureId,
            @RequestParam final Double measurement,
            @RequestParam final Long subCategoryId,
            @RequestParam final Long rateClassId) {
        Double rate = Double.valueOf(0);
        rate= advertisementRateService.getAmountBySubcategoryUomClassAndMeasurement(subCategoryId, unitOfMeasureId, rateClassId, measurement);
        if(rate==null) return Double.valueOf(0);      
        //TODO MULTIPLY WITH MEASUREMENT TO GET TOTAL AMOUNT. 
        return (BigDecimal.valueOf(rate).multiply(BigDecimal.valueOf(measurement)).setScale(2, BigDecimal.ROUND_HALF_UP)).doubleValue();
    }
    
    @RequestMapping(value = "subcategories", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<SubCategory> hoardingSubcategories(@RequestParam final Long categoryId) {
        return subCategoryService.getAllActiveSubCategoryByCategoryId(categoryId);
    }

    @RequestMapping(value = "create", method = GET)
    public String createHoardingForm() {
        return "hoarding-create";
    }

    @RequestMapping(value = "create", method = POST)
    public String createHoarding(@Valid @ModelAttribute final Hoarding hoarding, final BindingResult resultBinder,
            final RedirectAttributes redirAttrib) {
        validateHoardingDocs(hoarding, resultBinder);
        if (resultBinder.hasErrors())
            return "hoarding-create";
        storeHoardingDocuments(hoarding);
        hoardingService.createHoarding(hoarding);
        redirAttrib.addFlashAttribute("message", "hoarding.create.success");
        return "redirect:/hoarding/create";
    }

    private void storeHoardingDocuments(final Hoarding hoarding) {
        hoarding.getDocuments().forEach(document -> {
            document.setFiles(fileStoreUtils.addToFileStore(document.getAttachments(), "ADTAX"));
        });
    }

    private void validateHoardingDocs(final Hoarding hoarding, final BindingResult resultBinder) {
        int index = 0;
        for (final HoardingDocument document : hoarding.getDocuments()) {
            if (document.getDoctype().isMandatory() && document.getAttachments()[0].getSize() == 0)
                resultBinder.rejectValue("documents[" + index+ "].attachments", "hoarding.doc.mandatory");
            else if (document.isEnclosed() && document.getAttachments()[0].getSize() == 0)
                resultBinder.rejectValue("documents[" + index+ "].attachments", "hoarding.doc.not.enclosed");
            index++;
        }
    }
}
