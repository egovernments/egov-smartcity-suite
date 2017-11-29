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

import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.entity.DonationDetails;
import org.egov.wtms.masters.entity.DonationHeader;
import org.egov.wtms.masters.entity.PipeSize;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.repository.DonationDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DonationDetailsService {

    private final DonationDetailsRepository donationDetailsRepository;

    @Autowired
    public DonationDetailsService(final DonationDetailsRepository donationDetailsRepository) {
        this.donationDetailsRepository = donationDetailsRepository;
    }

    public DonationDetails findBy(final Long donationDetailsId) {
        return donationDetailsRepository.findOne(donationDetailsId);
    }

    @Transactional
    public DonationDetails persistDonationDetails(final DonationDetails donationDetails) {
        return donationDetailsRepository.save(donationDetails);
    }

    public DonationDetails load(final Long id) {
        return donationDetailsRepository.getOne(id);
    }

    public DonationDetails findByDonationHeader(final DonationHeader donationHeader) {
        return donationDetailsRepository.findByDonationHeader(donationHeader);
    }

    public List<DonationDetails> findAll() {
        return donationDetailsRepository.findAll(new Sort(Sort.Direction.DESC, "donationHeader.propertyType",
                "donationHeader.category", "donationHeader.usageType", "donationHeader.minPipeSize"));
    }

    // findDonationDetailsByPropertyAndCategoryAndUsageandPipeSize
    public DonationDetails findDonationDetailsByPropertyAndCategoryAndUsageandPipeSize(final PropertyType propertyType,
            final ConnectionCategory categoryType, final UsageType usageType, final PipeSize minPipeSize) {
        return donationDetailsRepository.findDonationDetailsByPropertyAndCategoryAndUsageandPipeSize(propertyType,
                categoryType, usageType, minPipeSize);
    }

    public DonationDetails findByDonationHeaderAndFromDateAndToDate(final DonationHeader donationHeader,
            final Date fromDate, final Date toDate) {
        return donationDetailsRepository.findByDonationHeaderAndFromDateAndToDate(donationHeader, fromDate, toDate);
    }

}
