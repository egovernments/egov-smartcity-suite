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

package org.egov.tl.service;

import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.tl.entity.License;
import org.egov.tl.entity.Validity;
import org.egov.tl.repository.ValidityRepository;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ValidityService {

    private final ValidityRepository validityRepository;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    public ValidityService(final ValidityRepository validityRepository) {
        this.validityRepository = validityRepository;
    }

    @Transactional
    public Validity create(final Validity validity) {
        return validityRepository.save(validity);
    }

    @Transactional
    public Validity update(final Validity validity) {
        return validityRepository.save(validity);
    }

    public List<Validity> findAll() {
        return validityRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Validity findOne(final Long id) {
        return validityRepository.findOne(id);
    }

    public List<Validity> search(final Long natureOfBusiness, final Long licenseCategory) {
        if (natureOfBusiness != null && licenseCategory != null)
            return validityRepository.findByNatureOfBusinessIdAndLicenseCategoryId(natureOfBusiness, licenseCategory);
        else if (natureOfBusiness != null)
            return validityRepository.findByNatureOfBusinessId(natureOfBusiness);
        else if (licenseCategory != null)
            return validityRepository.findByLicenseCategoryId(licenseCategory);
        else
            return validityRepository.findAll();
    }

    public void applyLicenseValidity(final License license) {

        List<Validity> validityList = validityRepository.findByNatureOfBusinessIdAndLicenseCategoryId(
                license.getBuildingType().getId(), license.getTradeName().getCategory().getId());

        if (validityList.isEmpty())
            validityList = validityRepository.findByNatureOfBusinessId(license.getBuildingType().getId());

        if (validityList.isEmpty())
            throw new ValidationException("TL-001", "License validity not defined.");
        else {
            final Validity validity = validityList.get(0);

            if (validity.isBasedOnFinancialYear()) {
                Date nextExpiryDate;
                if (license.getDateOfExpiry() == null)
                    nextExpiryDate = new Date();
                else
                    nextExpiryDate = new LocalDate(license.getDateOfExpiry()).plusDays(1).toDate();
                final CFinancialYear currentFinancialYear = financialYearHibernateDAO.getFinancialYearByDate(nextExpiryDate);
                license.setDateOfExpiry(currentFinancialYear.getEndingDate());
            } else {
                LocalDate nextExpiryDate;
                if (license.getDateOfExpiry() == null)
                    nextExpiryDate = new LocalDate();
                else
                    nextExpiryDate = new LocalDate(license.getDateOfExpiry());
                if (validity.getYear() != null && validity.getYear() > 0)
                    nextExpiryDate.plusYears(validity.getYear());
                if (validity.getMonth() != null && validity.getMonth() > 0)
                    nextExpiryDate.plusMonths(validity.getMonth());
                if (validity.getWeek() != null && validity.getWeek() > 0)
                    nextExpiryDate.plusWeeks(validity.getWeek());
                if (validity.getDay() != null && validity.getDay() > 0)
                    nextExpiryDate.plusDays(validity.getDay());
                license.setDateOfExpiry(nextExpiryDate.toDate());
            }
        }

    }

}
