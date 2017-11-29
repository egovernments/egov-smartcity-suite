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

package org.egov.tl.service;

import org.egov.infra.validation.exception.ValidationException;
import org.egov.tl.entity.FeeMatrix;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseSubCategoryDetails;
import org.egov.tl.repository.FeeMatrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FeeMatrixService<T extends License> {

    @Autowired
    private FeeMatrixRepository feeMatrixRepository;

    @Autowired
    private NatureOfBusinessService natureOfBusinessService;

    @Autowired
    private LicenseAppTypeService licenseAppTypeService;

    @Transactional
    public FeeMatrix create(FeeMatrix feeMatrix) {
        for (FeeMatrixDetail fd : feeMatrix.getFeeMatrixDetail())
            fd.setFeeMatrix(feeMatrix);
        return feeMatrixRepository.save(feeMatrix);
    }

    @Transactional
    public FeeMatrix update(FeeMatrix feeMatrix) {
        feeMatrix.getFeeMatrixDetail().removeIf(FeeMatrixDetail::isMarkedForRemoval);
        for (FeeMatrixDetail feeMatrixDetail : feeMatrix.getFeeMatrixDetail())
            feeMatrixDetail.setFeeMatrix(feeMatrix);
        return feeMatrixRepository.saveAndFlush(feeMatrix);
    }

    public List<FeeMatrix> getFeeMatrix(Long licenseCategory, Long subCategory, Long financialYear) {
        return feeMatrixRepository.searchFeeMatrix(licenseCategory, subCategory, financialYear);
    }

    public FeeMatrix getFeeMatrixById(Long id) {
        return feeMatrixRepository.findOne(id);
    }

    public List<FeeMatrixDetail> getLicenseFeeDetails(T license, Date effectiveDate) {
        List<FeeMatrixDetail> licenseFeeDetails = new ArrayList<>();
        for (LicenseSubCategoryDetails subcategoryDetail : license.getTradeName().getLicenseSubCategoryDetails()) {
            FeeMatrix feeMatrix = getFeeMatrix(license, subcategoryDetail.getFeeType(), effectiveDate).
                    orElseThrow(() -> new ValidationException("TL-002", "Fee matrix not defined"));
            FeeMatrixDetail feeMatrixDetail = feeMatrix.getFeeMatrixDetail().
                    parallelStream().
                    filter(detail -> license.getTradeArea_weight().intValue() > detail.getUomFrom()
                            && license.getTradeArea_weight().intValue() <= detail.getUomTo()).
                    findFirst().
                    orElseThrow(() -> new ValidationException("TL-003", "Fee range not defined"));
            licenseFeeDetails.add(feeMatrixDetail);
        }

        return licenseFeeDetails;
    }

    private Optional<FeeMatrix> getFeeMatrix(License license, FeeType feeType, Date effectiveDate) {
        Optional<FeeMatrix> feeMatrix = feeMatrixRepository.findFeeMatrix(license, license.getNatureOfBusiness(),
                feeType, license.getLicenseAppType(), effectiveDate);
        if (!feeMatrix.isPresent() && license.isNewApplication()) {
            feeMatrix = getFeeMatrixForTemporaryLicense(license, feeType, effectiveDate);
        }

        if (!feeMatrix.isPresent() && license.isReNewApplication()) {
            feeMatrix = getFeeMatrixForRenew(license, feeType, effectiveDate);
        }

        return feeMatrix;
    }

    private Optional<FeeMatrix> getFeeMatrixForRenew(License license, FeeType feeType, Date effectiveDate) {
        Optional<FeeMatrix> feeMatrix = getFeeMatrixForTemporaryLicense(license, feeType, effectiveDate);
        if (!feeMatrix.isPresent()) {
            feeMatrix = feeMatrixRepository.findFeeMatrix(license, license.getNatureOfBusiness(), feeType,
                    licenseAppTypeService.getNewLicenseAppType(), effectiveDate);
        }

        if (!feeMatrix.isPresent() && license.isTemporary()) {
            feeMatrix = feeMatrixRepository.findFeeMatrix(license, natureOfBusinessService.getPermanentBusinessNature(),
                    feeType, licenseAppTypeService.getNewLicenseAppType(), effectiveDate);
        }
        return feeMatrix;
    }

    private Optional<FeeMatrix> getFeeMatrixForTemporaryLicense(License license, FeeType feeType, Date effectiveDate) {
        Optional<FeeMatrix> feeMatrix = Optional.empty();
        if (license.isTemporary())
            feeMatrix = feeMatrixRepository.findFeeMatrix(license, natureOfBusinessService.getPermanentBusinessNature(),
                    feeType, license.getLicenseAppType(), effectiveDate);
        return feeMatrix;
    }
}