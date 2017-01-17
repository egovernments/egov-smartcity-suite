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

import static org.egov.tl.utils.Constants.DELIMITER_HYPEN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.tl.entity.FeeMatrix;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.LicenseSubCategoryDetails;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.UnitOfMeasurement;
import org.egov.tl.repository.FeeMatrixRepository;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FeeMatrixService<T extends License> {

    private final FeeMatrixRepository feeMatrixRepository;

    @Autowired
    private FeeTypeService feeTypeService;

    @Autowired
    private FeeMatrixDetailService feeMatrixDetailService;

    @Autowired
    private AppConfigValueService appConfigValueService;

    @Autowired
    @Qualifier("entityQueryService")
    private PersistenceService persistenceService;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    public FeeMatrixService(final FeeMatrixRepository feeMatrixRepository) {
        this.feeMatrixRepository = feeMatrixRepository;
    }

    @Transactional
    public FeeMatrix create(final FeeMatrix feeMatrix) {
        feeMatrix.setUniqueNo(feeMatrix.genUniqueNo());
        if (!feeMatrix.getFeeMatrixDetail().isEmpty())
            for (final FeeMatrixDetail fd : feeMatrix.getFeeMatrixDetail())
                fd.setFeeMatrix(feeMatrix);
        return feeMatrixRepository.save(feeMatrix);
    }

    @Transactional
    public FeeMatrix update(final FeeMatrix feeMatrix) {
        feeMatrix.setUniqueNo(feeMatrix.genUniqueNo());
        return feeMatrixRepository.save(feeMatrix);
    }

    public List<FeeMatrix> findAll() {
        return feeMatrixRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public FeeMatrix search(final FeeMatrix feeMatrix) {

        return feeMatrixRepository.findByUniqueNo(feeMatrix.getUniqueNo());
    }

    public List<FeeMatrixDetail> findFeeMatrixByLicense(T license) {
        return findFeeMatrixByGivenDate(license, license.getApplicationDate());
    }

    /**
     * @param license
     * @return Will return the list of fees for the selected combination 1.It will fetch all fee types from the system 2. Apply
     * the rule and parameters for that 3. return it by adding to the feeMatrixDetailList 4. Uses switch . So After adding feetype
     * in the system it should be coded here to say how what parameter to be applied for the fetch
     */
    public List<FeeMatrixDetail> findFeeMatrixByGivenDate(T license, Date givenDate) {

        final List<AppConfigValues> newRenewAppconfigList = appConfigValueService.getConfigValuesByModuleAndKey("Trade License",
                "Is Fee For New and Renew Same");
        final boolean isNewRenewFeeSame = "Y".equalsIgnoreCase(newRenewAppconfigList.get(0).getValue());

        final List<AppConfigValues> permTempAppconfigList = appConfigValueService.getConfigValuesByModuleAndKey("Trade License",
                "Is Fee For Permanent and Temporary Same");
        final boolean isPermanentTemporaryfeeSame = "Y".equalsIgnoreCase(permTempAppconfigList.get(0).getValue());

        final LicenseAppType newapp = (LicenseAppType) this.persistenceService.find("from  LicenseAppType where name='New' ");
        final NatureOfBusiness permanent = (NatureOfBusiness) persistenceService
                .find("from org.egov.tl.entity.NatureOfBusiness where   name='Permanent'");
        String uniqueNo;
        UnitOfMeasurement uom = null;
        final FeeType feeType = feeTypeService.findByName(Constants.LICENSE_FEE_TYPE);
        for (final LicenseSubCategoryDetails scd : license.getTradeName().getLicenseSubCategoryDetails())
            if (scd.getFeeType().equals(feeType))
                uom = scd.getUom();
        if (isNewRenewFeeSame && isPermanentTemporaryfeeSame)
            uniqueNo = generateFeeMatirixUniqueNo(license, newapp, permanent);
        else if (isNewRenewFeeSame)
            uniqueNo = generateFeeMatirixUniqueNo(license, newapp);
        else if (isPermanentTemporaryfeeSame)
            uniqueNo = generateFeeMatirixUniqueNo(license, permanent);
        else
            uniqueNo = generateFeeMatirixUniqueNo(license);

        final List<FeeMatrixDetail> feeMatrixDetailList = new ArrayList<>();
        final CFinancialYear financialYearByDate = financialYearDAO.getFinYearByDate(givenDate);
        for (final FeeType fee : feeTypeService.findAll())
            if (fee.getFeeProcessType().equals(FeeType.FeeProcessType.RANGE))
                switchLoop:switch (fee.getCode()) {
                    // First find License Fee with UOM
                    case "LF":
                        final FeeMatrix feeMatrix = feeMatrixRepository
                                .findByUniqueNo(uniqueNo + DELIMITER_HYPEN + fee.getId() + DELIMITER_HYPEN + uom.getId()
                                        + DELIMITER_HYPEN + financialYearByDate.getId());
                        if (feeMatrix == null)
                            throw new ValidationException("TL-002", "TL-002");
                        final Optional<FeeMatrixDetail> feeMatrixDetail = feeMatrixDetailService.findByLicenseFeeByRange(feeMatrix,
                                license.getTradeArea_weight());
                        if (!feeMatrixDetail.isPresent())
                            throw new ValidationException("TL-003", "TL-003");
                        feeMatrixDetailList.add(feeMatrixDetail.get());
                        break switchLoop;

                }

        return feeMatrixDetailList;
    }

    public List<FeeMatrix> findBySubCategory(final LicenseSubCategory subCategory) {
        return feeMatrixRepository.findBySubCategory(subCategory);
    }

    private String generateFeeMatirixUniqueNo(final T license, final NatureOfBusiness permanent) {
        return new StringBuilder().append(permanent.getId()).append(DELIMITER_HYPEN).append(license.getLicenseAppType().getId())
                .append(DELIMITER_HYPEN).append(license.getCategory().getId()).append(DELIMITER_HYPEN)
                .append(license.getTradeName().getId()).toString();
    }

    private String generateFeeMatirixUniqueNo(final T license) {
        return new StringBuilder().append(license.getNatureOfBusiness().getId()).append(DELIMITER_HYPEN)
                .append(license.getLicenseAppType().getId()).append(DELIMITER_HYPEN).append(license.getCategory().getId())
                .append(DELIMITER_HYPEN).append(license.getTradeName().getId()).toString();
    }

    private String generateFeeMatirixUniqueNo(final T license, final LicenseAppType apptype) {
        return new StringBuilder().append(license.getNatureOfBusiness().getId()).append(DELIMITER_HYPEN).append(apptype.getId())
                .append(DELIMITER_HYPEN).append(license.getCategory().getId()).append(DELIMITER_HYPEN)
                .append(license.getTradeName().getId()).toString();
    }

    private String generateFeeMatirixUniqueNo(final T license, final LicenseAppType apptype,
                                              final NatureOfBusiness natureOfBusiness) {
        return new StringBuilder().append(natureOfBusiness.getId()).append(DELIMITER_HYPEN).append(apptype.getId())
                .append(DELIMITER_HYPEN).append(license.getCategory().getId()).append(DELIMITER_HYPEN)
                .append(license.getTradeName().getId()).toString();
    }
}