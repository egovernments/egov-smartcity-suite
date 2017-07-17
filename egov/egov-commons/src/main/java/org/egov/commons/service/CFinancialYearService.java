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

package org.egov.commons.service;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.repository.CFinancialYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CFinancialYearService {

    private final CFinancialYearRepository cFinancialYearRepository;

    @Autowired
    public CFinancialYearService(final CFinancialYearRepository cFinancialYearRepository) {
        this.cFinancialYearRepository = cFinancialYearRepository;
    }

    @Transactional
    public CFinancialYear create(final CFinancialYear cFinancialYear) {
        return cFinancialYearRepository.save(cFinancialYear);
    }

    @Transactional
    public CFinancialYear update(final CFinancialYear cFinancialYear) {
        return cFinancialYearRepository.save(cFinancialYear);
    }

    public List<CFinancialYear> findAll() {
        return cFinancialYearRepository.findAll(new Sort(Sort.Direction.ASC, "finYearRange"));
    }

    public List<CFinancialYear> getAllFinancialYears() {
        return cFinancialYearRepository.getAllFinancialYears();
    }

    public CFinancialYear findOne(final Long id) {
        return cFinancialYearRepository.findOne(id);
    }

    public List<CFinancialYear> search(final CFinancialYear cFinancialYear) {
        if (cFinancialYear.getFinYearRange() != null)
            return cFinancialYearRepository.findByFinancialYearRange(cFinancialYear.getFinYearRange());
        else
            return findAll();
    }

    public Date getNextFinancialYearStartingDate() {
        final List<CFinancialYear> cFinYear = cFinancialYearRepository.getFinYearLastDate();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(cFinYear.get(0).getEndingDate());
        cal.add(Calendar.DATE, +1);
        return cal.getTime();
    }

    public CFiscalPeriod findByFiscalName(final String name) {
        return cFinancialYearRepository.findByFiscalName(name);
    }

    public CFinancialYear getFinancialYearByDate(Date date) {
        return cFinancialYearRepository.getFinancialYearByDate(date);
    }

    public List<CFinancialYear> getFinancialYearNotClosed() {
        return cFinancialYearRepository.findByIsClosedFalseOrderByFinYearRangeDesc();
    }

    public CFinancialYear getFinacialYearByYearRange(String finYearRange) {
        return cFinancialYearRepository.findByFinYearRange(finYearRange);
    }

    public List<CFinancialYear> getFinancialYears(List<Long> financialYearList) {
        return cFinancialYearRepository.findByIdIn(financialYearList);
    }

    public CFinancialYear getPreviousFinancialYearForDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cFinancialYearRepository.getFinancialYearByDate(cal.getTime());
    }


}