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

package org.egov.tl.service.masters;

import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.LicenseSubCategoryDetails;
import org.egov.tl.repository.LicenseSubCategoryRepository;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.LicenseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.egov.tl.utils.Constants.TRADELICENSE;

@Service
@Transactional(readOnly = true)
public class LicenseSubCategoryService {
    
    @Autowired
    private LicenseSubCategoryRepository licenseSubCategoryRepository;

    @Autowired
    private LicenseCategoryService licenseCategoryService;

    @Autowired
    private FeeTypeService feeTypeService;

    @Autowired
    private UnitOfMeasurementService unitOfMeasurementService;

    @Autowired
    private LicenseTypeService licenseTypeService;
    
    @Transactional
    public LicenseSubCategory create(LicenseSubCategory subCategory, List<LicenseSubCategoryDetails> details, Long categoryId){
        if(categoryId!=null){
            subCategory.setCategory(licenseCategoryService.findById(categoryId));
        }
        subCategory.setLicenseType(licenseTypeService.getLicenseTypeByName(TRADELICENSE));
        for (LicenseSubCategoryDetails scDetails : details) {
            if(scDetails!=null){
                scDetails.setSubCategory(subCategory);
                scDetails.setFeeType(feeTypeService.findById(scDetails.getFeeType().getId()));
                scDetails.setRateType(scDetails.getRateType());
                scDetails.setUom(unitOfMeasurementService.findById(scDetails.getUom().getId()));
                subCategory.addLicenseSubCategoryDetails(scDetails);
            }
        }
        return  licenseSubCategoryRepository.save(subCategory);
    }

    public List<LicenseSubCategory> findAllSubCategoryByCategory(final Long categoryId) {
        return licenseSubCategoryRepository.findAllByCategoryId(categoryId);
    }
    
    public LicenseSubCategory findSubCategoryByName(final String name) {
        return licenseSubCategoryRepository.findByName(name);
    }

    public LicenseSubCategory findSubCategoryByCode(final String code) {
        return licenseSubCategoryRepository.findByCode(code);
    }
    
    public LicenseSubCategory findById(Long subCategoryId){
        return licenseSubCategoryRepository.findOne(subCategoryId);
    }
    
    public List<LicenseSubCategory> findAll(){
        return licenseSubCategoryRepository.findAll();
    }

    public List<LicenseSubCategory> getLicenseSubCategoriesByLicenseTypeName(String licenseTypeName) {
        return licenseSubCategoryRepository.findByLicenseType_Name(licenseTypeName);
    }

}
