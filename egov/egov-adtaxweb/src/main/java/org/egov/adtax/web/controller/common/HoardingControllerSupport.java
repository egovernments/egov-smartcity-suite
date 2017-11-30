/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.adtax.web.controller.common;

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.entity.HoardingDocument;
import org.egov.adtax.entity.HoardingDocumentType;
import org.egov.adtax.entity.RatesClass;
import org.egov.adtax.entity.RevenueInspector;
import org.egov.adtax.entity.UnitOfMeasure;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.AdvertisementRateService;
import org.egov.adtax.service.AdvertisementService;
import org.egov.adtax.service.HoardingCategoryService;
import org.egov.adtax.service.HoardingDocumentTypeService;
import org.egov.adtax.service.RatesClassService;
import org.egov.adtax.service.RevenueInspectorService;
import org.egov.adtax.service.SubCategoryService;
import org.egov.adtax.service.UnitOfMeasureService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.config.core.EnvironmentSettings;
import org.egov.infra.utils.FileStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

public class HoardingControllerSupport extends GenericWorkFlowController {
    protected @Autowired AdvertisementPermitDetailService advertisementPermitDetailService;
    protected @Autowired AdvertisementService advertisementService;
    protected @Autowired SubCategoryService subCategoryService;
    protected @Autowired FileStoreUtils fileStoreUtils;
    protected @Autowired BoundaryService boundaryService;
    protected @Autowired AdvertisementRateService advertisementRateService;
    protected @Autowired HoardingCategoryService hoardingCategoryService;
    protected @Autowired UnitOfMeasureService unitOfMeasureService;
    protected @Autowired RevenueInspectorService revenueInspectorService;
    protected @Autowired RatesClassService ratesClassService;
    protected @Autowired HoardingDocumentTypeService hoardingDocumentTypeService;
    @Autowired
    protected EnvironmentSettings environmentSettings;
    protected @Autowired AdvertisementDemandService advertisementDemandService;
    protected @Autowired FinancialYearDAO financialYearDAO;

    @ModelAttribute("previousFinancialYear")
    public CFinancialYear previousFinancialYear() {
        return financialYearDAO.getPreviousFinancialYearByDate(new Date());
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

    @ModelAttribute("allRateClasses")
    public List<RatesClass> getAllRatesClass() {
        return ratesClassService.getAllRatesClass();
    }

    @ModelAttribute("hoardingDocumentTypes")
    public List<HoardingDocumentType> hoardingDocumentTypes() {
        return hoardingDocumentTypeService.getAllDocumentTypes();
    }

    @ModelAttribute("revenueZones")
    public List<Boundary> revenueZones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(AdvertisementTaxConstants.BOUNDARYTYPE_ZONE,
                AdvertisementTaxConstants.ELECTION_HIERARCHY_TYPE);
    }

    @ModelAttribute("zones")
    public List<Boundary> zones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(AdvertisementTaxConstants.BOUNDARYTYPE_ZONE,
                AdvertisementTaxConstants.ADMINISTRATION_HIERARCHY_TYPE);
    }

    @ModelAttribute("localities")
    public List<Boundary> localities() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                AdvertisementTaxConstants.BOUNDARYTYPE_LOCALITY, AdvertisementTaxConstants.LOCATION_HIERARCHY_TYPE);
    }

    @ModelAttribute("revenueWards")
    public List<Boundary> revenueWards() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                AdvertisementTaxConstants.BOUNDARYTYPE_ELECTIONWARD, AdvertisementTaxConstants.ELECTION_HIERARCHY_TYPE);
    }

    protected void storeHoardingDocuments(final AdvertisementPermitDetail advertisementPermitDetail) {
        advertisementPermitDetail.getAdvertisement().getDocuments().forEach(document -> {
            document.setFiles(fileStoreUtils.addToFileStore(document.getAttachments(), "ADTAX"));
        });
    }

    protected void updateHoardingDocuments(final AdvertisementPermitDetail advertisementPermitDetail) {
        advertisementPermitDetail.getAdvertisement().getDocuments().forEach(document -> {
            if(document.getAttachments() != null)
                document.addFiles(fileStoreUtils.addToFileStore(document.getAttachments(), "ADTAX"));
        });
    }

    protected void validateHoardingDocs(final AdvertisementPermitDetail advertisementPermitDetail,
            final BindingResult resultBinder) {
        int index = 0;
        for (final HoardingDocument document : advertisementPermitDetail.getAdvertisement().getDocuments()) {
            if (document.getDoctype().isMandatory() && document.getAttachments()[0].getSize() == 0)
                resultBinder.rejectValue("advertisement.documents[" + index + "].attachments", "hoarding.doc.mandatory");
            else if (document.isEnclosed() && document.getAttachments()[0].getSize() == 0)
                resultBinder.rejectValue("advertisement.documents[" + index + "].attachments", "hoarding.doc.not.enclosed");
            index++;
        }
    }

    protected void validateHoardingDocsOnUpdate(final AdvertisementPermitDetail advertisementPermitDetail,
            final BindingResult resultBinder, final RedirectAttributes redirAttrib) {
        int index = 0;
        for (final HoardingDocument document : advertisementPermitDetail.getAdvertisement().getDocuments()) {
            if (document.getDoctype().isMandatory() && document.getFiles().size() == 0
                    && document.getAttachments()[0].getSize() == 0) {
                resultBinder.rejectValue("advertisement.documents[" + index + "].attachments", "hoarding.doc.mandatory");
                redirAttrib.addFlashAttribute("message", "hoarding.doc.not.enclosed");
            } else if (document.isEnclosed() && document.getFiles().size() == 0 && document.getAttachments()[0].getSize() == 0) {
                resultBinder.rejectValue("advertisement.documents[" + index + "].attachments", "hoarding.doc.not.enclosed");
                redirAttrib.addFlashAttribute("message", "hoarding.doc.not.enclosed");
            }
            index++;
        }

    }

    protected Boolean checkTaxAlreadyCollectedForAdvertisement(final Advertisement advertisement) {
        Boolean taxAlreadyCollectedForDemandInAnyYear = false;
        if (advertisement != null && advertisement.getDemandId() != null
                && advertisement.getDemandId().getEgDemandDetails() != null)
            for (final EgDemandDetails demandDtl : advertisement.getDemandId().getEgDemandDetails())
                for (final EgdmCollectedReceipt collRecpt : demandDtl.getEgdmCollectedReceipts())
                    if (!collRecpt.isCancelled()) {
                        taxAlreadyCollectedForDemandInAnyYear = true;
                        break;
                    }
        return taxAlreadyCollectedForDemandInAnyYear;
    }

    protected void validateAdvertisementDetails(final AdvertisementPermitDetail advertisementPermitDetail,
            final BindingResult resultBinder) {

        if (advertisementPermitDetail.getAgency() == null && advertisementPermitDetail.getOwnerDetail() == null)
            resultBinder.rejectValue("agency", "invalid.eitherAgencyOrOwnerDetailRequired");

        if (advertisementPermitDetail.getAdvertisement() != null &&
                advertisementPermitDetail.getAdvertisement().getPropertyNumber() == null
                && advertisementPermitDetail.getAdvertisement().getCategory() != null
                && advertisementPermitDetail.getAdvertisement().getCategory().isPropertyMandatory())
            resultBinder.rejectValue("advertisement.propertyNumber", "invalid.propertyIdIsMandatoryForCategory");
        // TODO: SAVE AUTOCALCULATED AMOUNT IN BACKEND.
        
        if (advertisementPermitDetail.getPermissionstartdate() != null
                && advertisementPermitDetail.getPermissionenddate() != null
                && advertisementPermitDetail.getPermissionstartdate().after(
                        advertisementPermitDetail.getPermissionenddate()))
            resultBinder.rejectValue("permissionstartdate", "invalid.permissionFromDateAndToDateCompare");
    }   
}
