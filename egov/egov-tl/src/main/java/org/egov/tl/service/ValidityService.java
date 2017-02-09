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

import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.tl.entity.License;
import org.egov.tl.entity.Validity;
import org.egov.tl.repository.ValidityRepository;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ValidityService {

    @Autowired
    private ValidityRepository validityRepository;

    @Transactional
    public Validity create(Validity validity) {
        return validityRepository.save(validity);
    }

    @Transactional
    public Validity update(Validity validity) {
        return validityRepository.save(validity);
    }

    public List<Validity> findAll() {
        return validityRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Validity findOne(Long id) {
        return validityRepository.findOne(id);
    }

    public List<Validity> search(Long natureOfBusiness, Long licenseCategory) {
        if (natureOfBusiness != null && licenseCategory != null)
            return Arrays.asList(validityRepository.findByNatureOfBusinessIdAndLicenseCategoryId(natureOfBusiness, licenseCategory));
        else if (natureOfBusiness != null)
            return validityRepository.findByNatureOfBusinessId(natureOfBusiness);
        else if (licenseCategory != null)
            return validityRepository.findByLicenseCategoryId(licenseCategory);
        else
            return validityRepository.findAll();
    }

    public Validity getApplicableLicenseValidity(final License license) {
        return Optional.
                ofNullable(validityRepository.findByNatureOfBusinessIdAndLicenseCategoryId(
                        license.getNatureOfBusiness().getId(), license.getCategory().getId())).
                orElse(validityRepository.findByNatureOfBusinessIdAndLicenseCategoryIsNull(
                        license.getNatureOfBusiness().getId()));
    }


    public void applyLicenseValidity(License license) {
        Validity validity = getApplicableLicenseValidity(license);
        if (validity == null)
            throw new ValidationException("TL-001", "License validity not defined.");
        if (license.isLegacy()) {
            applyValidityToLegacyLicense(license, validity);
        } else {
            applyValidityToLicense(license, validity);
        }
    }

    private void applyValidityToLicense(License license, Validity validity) {
        if (validity.isBasedOnFinancialYear()) {
            license.setDateOfExpiry(license.getCurrentDemand().getEgInstallmentMaster().getToDate());
        } else {
            applyLicenseValidityBasedOnCustomValidity(license, validity);
        }
    }

    private void applyValidityToLegacyLicense(License license, Validity validity) {
        license.getCurrentDemand().getEgDemandDetails().stream().
                filter(demandDetail -> demandDetail.getAmount().subtract(demandDetail.getAmtCollected()).doubleValue() <= 0).
                max(Comparator.comparing(EgDemandDetails::getInstallmentEndDate)).
                ifPresent(demandDetail -> {
                            if (validity.isBasedOnFinancialYear())
                                license.setDateOfExpiry(demandDetail.getInstallmentEndDate());
                            else
                                applyLicenseValidityBasedOnCustomValidity(license, validity);
                        }
                );
    }

    private void applyLicenseValidityBasedOnCustomValidity(License license, Validity validity) {
        LocalDate nextExpiryDate = new LocalDate(license.isNewApplication() ? license.getCommencementDate() :
                license.getCurrentDemand().getEgInstallmentMaster().getFromDate());
        if (validity.getYear() != null && validity.getYear() > 0)
            nextExpiryDate = nextExpiryDate.plusYears(validity.getYear());
        if (validity.getMonth() != null && validity.getMonth() > 0)
            nextExpiryDate = nextExpiryDate.plusMonths(validity.getMonth());
        if (validity.getWeek() != null && validity.getWeek() > 0)
            nextExpiryDate = nextExpiryDate.plusWeeks(validity.getWeek());
        if (validity.getDay() != null && validity.getDay() > 0)
            nextExpiryDate = nextExpiryDate.plusDays(validity.getDay());
        license.setDateOfExpiry(nextExpiryDate.toDate());
    }
}
