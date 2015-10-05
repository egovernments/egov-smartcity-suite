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
package org.egov.adtax.web.controller.common;

import java.util.List;

import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.entity.HoardingDocumentType;
import org.egov.adtax.entity.RatesClass;
import org.egov.adtax.entity.RevenueInspector;
import org.egov.adtax.entity.UnitOfMeasure;
import org.egov.adtax.service.AdvertisementDemandService;
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
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.utils.FileStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

public class HoardingControllerSupport {

    protected @Autowired HoardingService hoardingService;
    protected @Autowired SubCategoryService subCategoryService;
    protected @Autowired FileStoreUtils fileStoreUtils;
    protected @Autowired BoundaryService boundaryService;
    protected @Autowired AdvertisementRateService advertisementRateService;
    protected @Autowired HoardingCategoryService hoardingCategoryService;
    protected @Autowired UnitOfMeasureService unitOfMeasureService;
    protected @Autowired RevenueInspectorService revenueInspectorService;
    protected @Autowired RatesClassService ratesClassService;
    protected @Autowired HoardingDocumentTypeService hoardingDocumentTypeService;
    protected @Autowired ApplicationProperties applicationProperties;
    protected @Autowired AdvertisementDemandService advertisementDemandService;

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

}
