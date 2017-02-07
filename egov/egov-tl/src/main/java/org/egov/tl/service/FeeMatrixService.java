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

package org.egov.tl.service;

import org.egov.infra.validation.exception.ValidationException;
import org.egov.tl.entity.FeeMatrix;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseSubCategoryDetails;
import org.egov.tl.entity.NatureOfBusiness;
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
        for (LicenseSubCategoryDetails licenseSubCategoryDetail : license.getTradeName().getLicenseSubCategoryDetails()) {
            Optional<FeeMatrix> feeMatrix = getFeeMatrix(license, licenseSubCategoryDetail.getFeeType(), effectiveDate);
            if (!feeMatrix.isPresent())
                throw new ValidationException("TL-002", "TL-002");
            Optional<FeeMatrixDetail> feeMatrixDetail = feeMatrix.get().getFeeMatrixDetail().
                    parallelStream().filter(detail -> license.getTradeArea_weight().intValue() > detail.getUomFrom()
                    && license.getTradeArea_weight().intValue() <= detail.getUomTo()).findFirst();
            if (!feeMatrixDetail.isPresent())
                throw new ValidationException("TL-003", "TL-003");
            licenseFeeDetails.add(feeMatrixDetail.get());
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

    private Optional<FeeMatrix> getFeeMatrixForRenew(final License license, final FeeType feeType, final Date effectiveDate) {
        Optional<FeeMatrix> feeMatrix = getFeeMatrixForTemporaryLicense(license, feeType, effectiveDate);
        if (!feeMatrix.isPresent()) {
            LicenseAppType newLicenseAppType = licenseAppTypeService.getNewLicenseAppType();
            feeMatrix = feeMatrixRepository.findFeeMatrix(license, license.getNatureOfBusiness(), feeType,
                    newLicenseAppType, effectiveDate);
            feeMatrix = feeMatrix.isPresent() && feeMatrix.get().isSameForNewAndRenew() ? feeMatrix : Optional.empty();
        }

        if (!feeMatrix.isPresent()) {
            NatureOfBusiness natureOfBusiness = natureOfBusinessService.getPermanentBusinessNature();
            LicenseAppType newLicenseAppType = licenseAppTypeService.getNewLicenseAppType();
            feeMatrix = feeMatrixRepository.findFeeMatrix(license, natureOfBusiness, feeType,
                    newLicenseAppType, effectiveDate);
            feeMatrix = feeMatrix.isPresent() && feeMatrix.get().isSameForPermanentAndTemporary() &&
                    feeMatrix.get().isSameForNewAndRenew() ? feeMatrix : Optional.empty();
        }
        return feeMatrix;
    }

    private Optional<FeeMatrix> getFeeMatrixForTemporaryLicense(License license, FeeType feeType, Date effectiveDate) {
        Optional<FeeMatrix> feeMatrix = Optional.empty();
        if (license.isTemporary()) {
            NatureOfBusiness natureOfBusiness = natureOfBusinessService.getPermanentBusinessNature();
            feeMatrix = feeMatrixRepository.findFeeMatrix(license, natureOfBusiness, feeType,
                    license.getLicenseAppType(), effectiveDate);
            feeMatrix = feeMatrix.isPresent() && feeMatrix.get().isSameForPermanentAndTemporary() ? feeMatrix : Optional.empty();
        }
        return feeMatrix;
    }
}