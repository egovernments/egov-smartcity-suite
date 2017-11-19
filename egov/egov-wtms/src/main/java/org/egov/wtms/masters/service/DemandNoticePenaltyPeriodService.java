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
package org.egov.wtms.masters.service;

import org.egov.wtms.masters.entity.DemandNoticePenaltyPeriod;
import org.egov.wtms.masters.entity.enums.DemandNoticeIssueFrequency;
import org.egov.wtms.masters.repository.DemandNoticePenaltyPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DemandNoticePenaltyPeriodService {

    private final DemandNoticePenaltyPeriodRepository demandNoticePenaltyPeriodRepository;

    @Autowired
    public DemandNoticePenaltyPeriodService(final DemandNoticePenaltyPeriodRepository demandNoticePenaltyPeriodRepository) {
        this.demandNoticePenaltyPeriodRepository = demandNoticePenaltyPeriodRepository;
    }

    public DemandNoticePenaltyPeriod findBy(final Long demandNoticePenaltyId) {
        return demandNoticePenaltyPeriodRepository.findOne(demandNoticePenaltyId);
    }

    @Transactional
    public DemandNoticePenaltyPeriod createDemandNoticePenaltyPeriod(final DemandNoticePenaltyPeriod demandNoticePenaltyPeriod) {
        return demandNoticePenaltyPeriodRepository.save(demandNoticePenaltyPeriod);
    }

    @Transactional
    public void updateDemandNoticePenaltyPeriod(final DemandNoticePenaltyPeriod demandNoticePenaltyPeriod) {
        demandNoticePenaltyPeriodRepository.save(demandNoticePenaltyPeriod);
    }

    public List<DemandNoticePenaltyPeriod> findAll() {
        return demandNoticePenaltyPeriodRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
    }

    public DemandNoticePenaltyPeriod findByIssueOfDamendNotice(final DemandNoticeIssueFrequency issueOfDamendNotice) {
        return demandNoticePenaltyPeriodRepository.findByIssueOfDamendNotice(issueOfDamendNotice);
    }

    public DemandNoticePenaltyPeriod load(final Long id) {
        return demandNoticePenaltyPeriodRepository.getOne(id);
    }

    public Page<DemandNoticePenaltyPeriod> getListOfDemandNoticePenaltyPeriod(final Integer pageNumber, final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "id");
        return demandNoticePenaltyPeriodRepository.findAll(pageable);
    }

}
