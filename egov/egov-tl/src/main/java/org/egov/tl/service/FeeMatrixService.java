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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.tl.entity.FeeMatrix;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.repository.FeeMatrixRepository;
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

    /**
     *
     * @param license
     * @return Will return the list of fees for the selected combination 1.It will fetch all fee types from the system 2. Apply
     * the rule and parameters for that 3. return it by adding to the feeMatrixDetailList 4. Uses switch . So After adding feetype
     * in the system it should be coded here to say how what parameter to be applied for the fetch
     *
     */
    public List<FeeMatrixDetail> findFeeList(final T license) {

        final List<FeeType> allFees = feeTypeService.findAll();
        final Long uomId = license.getTradeName().getLicenseSubCategoryDetails().get(0).getUom().getId();
        String uniqueNo = "";
        final List<AppConfigValues> newRenewAppconfigList = appConfigValueService.getConfigValuesByModuleAndKey("Trade License",
                "Is Fee For New and Renew Same");
        final boolean isnew_renewfee_same = newRenewAppconfigList.get(0).getValue().equals("Y");

        final List<AppConfigValues> permTempAppconfigList = appConfigValueService.getConfigValuesByModuleAndKey("Trade License",
                "Is Fee For Permanent and Temporary Same");
        final boolean ispermanent_temporaryfee_same = permTempAppconfigList.get(0).getValue().equals("Y");

        final LicenseAppType newapp = (LicenseAppType) this.persistenceService.find("from  LicenseAppType where name='New' ");
        final NatureOfBusiness permanent = (NatureOfBusiness) persistenceService
                .find("from org.egov.tl.entity.NatureOfBusiness where   name='Permanent'");
        if (isnew_renewfee_same && ispermanent_temporaryfee_same)
            uniqueNo = generateFeeMatirixUniqueNo(license, newapp, permanent);
        else if (isnew_renewfee_same)
            uniqueNo = generateFeeMatirixUniqueNo(license, newapp);
        else if (ispermanent_temporaryfee_same)
            uniqueNo = generateFeeMatirixUniqueNo(license, permanent);
        else
            uniqueNo = generateFeeMatirixUniqueNo(license);

        final Date applicationDate = license.getApplicationDate();

        final CFinancialYear financialYearByDate = financialYearDAO.getFinancialYearByDate(applicationDate);

        final List<FeeMatrixDetail> feeMatrixDetailList = new ArrayList<FeeMatrixDetail>();
        FeeMatrixDetail feeMatrixDetail = null;
        FeeMatrix feeMatrix = null;
        for (final FeeType fee : allFees)
            if (fee.getFeeProcessType().equals(FeeType.FeeProcessType.RANGE))
                switchLoop: switch (fee.getCode()) {
                // First find License Fee with UOM
                case "LF":
                    feeMatrix = feeMatrixRepository
                            .findByUniqueNo(uniqueNo + "-" + fee.getId() + "-" + uomId + "-" + financialYearByDate.getId());
                    if (feeMatrix == null)
                        throw new ApplicationRuntimeException(
                                "License Fee Structure  is not defined for the selected combination");
                    feeMatrixDetail = feeMatrixDetailService.findByLicenseFeeByRange(feeMatrix, license.getTradeArea_weight(),
                            license.getApplicationDate(), financialYearByDate.getId());
                    if (feeMatrixDetail == null)
                        throw new ApplicationRuntimeException(
                                "License Fee Structure range is not defined for the selected combination");
                    feeMatrixDetailList.add(feeMatrixDetail);
                    break switchLoop;

                }

        return feeMatrixDetailList;
    }

    public List<FeeMatrix> findBySubCategory(final LicenseSubCategory subCategory) {
        return feeMatrixRepository.findBySubCategory(subCategory);
    }
    
    private String generateFeeMatirixUniqueNo(final T license, final NatureOfBusiness permanent) {
        return new StringBuilder().append(permanent.getId()).append("-").append(license.getLicenseAppType().getId())
                .append("-").append(license.getCategory().getId()).append("-").append(license.getTradeName().getId()).toString();
    }

    private String generateFeeMatirixUniqueNo(final T license) {
        return new StringBuilder().append(license.getBuildingType().getId()).append("-")
                .append(license.getLicenseAppType().getId()).append("-").append(license.getCategory().getId())
                .append("-").append(license.getTradeName().getId()).toString();
    }

    private String generateFeeMatirixUniqueNo(final T license, final LicenseAppType apptype) {
        return new StringBuilder().append(license.getBuildingType().getId()).append("-").append(apptype.getId())
                .append("-").append(license.getCategory().getId()).append("-").append(license.getTradeName().getId()).toString();
    }

    private String generateFeeMatirixUniqueNo(final T license, final LicenseAppType apptype,
            final NatureOfBusiness natureOfBusiness) {
        return new StringBuilder().append(natureOfBusiness.getId()).append("-").append(apptype.getId())
                .append("-").append(license.getCategory().getId()).append("-").append(license.getTradeName().getId()).toString();
    }
}