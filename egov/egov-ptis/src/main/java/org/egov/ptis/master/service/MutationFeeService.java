/*  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) 2016  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  */

package org.egov.ptis.master.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.ptis.domain.model.MutationFeeDetails;
import org.egov.ptis.domain.repository.master.mutationfee.MutationFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MutationFeeService {
    private final MutationFeeRepository mutationFeeRepository;

    @Autowired
    public MutationFeeService(final MutationFeeRepository mutationFeeRepository) {
        this.mutationFeeRepository = mutationFeeRepository;
    }

    public void generateSlabName(MutationFeeDetails mutationFeeDetails) {
        if (mutationFeeDetails.getHighLimit() == null)
            mutationFeeDetails.setSlabName(mutationFeeDetails.getLowLimit().toString().concat("_").concat("ABOVE"));
        else
            mutationFeeDetails.setSlabName(
                    mutationFeeDetails.getLowLimit().toString().concat("_").concat(mutationFeeDetails.getHighLimit().toString()));
    }

    public BigDecimal getMaxHighLimit() {
        return mutationFeeRepository.maxByHighLimit();
    }

    public boolean validateForHighLimitNull() {
        return mutationFeeRepository.findLowLimitForHighLimitNullValue() != null ? true : false;
    }

    public boolean getToDateBySlabName(final String slabName) {
        final Date today = new Date();
        final int result = today.compareTo(mutationFeeRepository.findToDateBySlabName(slabName));
        return result >= 0 ? true : false;
    }

    public boolean findExistingSlabName(final String slabName) {
        return !mutationFeeRepository.findIfSlabNameExists(slabName).isEmpty() ? true : false;
    }

    public Date getLatestToDateForSlabName(final String slabName) {
        return mutationFeeRepository.findLatestToDateForSlabName(slabName);
    }

    public List<MutationFeeDetails> findSlabName() {
        return mutationFeeRepository.getDistinctSlabNamesList();
    }

    public List<MutationFeeDetails> findDuplicateSlabName(final String slabname) {
        return mutationFeeRepository.findBySlabNames(slabname);
    }

    public MutationFeeDetails getMutationFeeById(final Long id) {
        return mutationFeeRepository.findOne(id);
    }

    public boolean validateForMaxHighLimit(final BigDecimal lowlimit) {
        final BigDecimal result = lowlimit.subtract(mutationFeeRepository.maxByHighLimit());
        return result.compareTo(BigDecimal.ONE) == 0 ? true : false;
    }

    @Transactional
    public void createMutationFee(final MutationFeeDetails mutation) {
        mutationFeeRepository.save(mutation);
    }

    public List<MutationFeeDetails> getAllMutationFee() {
        return mutationFeeRepository.selectAllOrderBySlabName();
    }

    @Transactional
    public MutationFeeDetails getDetailsById(final Long id) {
        return mutationFeeRepository.getAllDetailsById(id);
    }
}
