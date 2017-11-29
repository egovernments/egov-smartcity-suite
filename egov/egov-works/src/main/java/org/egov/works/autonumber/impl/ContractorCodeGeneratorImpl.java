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
package org.egov.works.autonumber.impl;

import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.infra.utils.StringUtils;
import org.egov.works.autonumber.ContractorCodeGenerator;
import org.egov.works.master.service.ContractorService;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.ContractorDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
public class ContractorCodeGeneratorImpl implements ContractorCodeGenerator {
    private static final String CONTRACTOR_CODE_SEQ_PREFIX = "SEQ_CONTRACTOR_CODE";
    @Autowired
    private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;

    @Autowired
    private ContractorService contractorService;

    @Override
    @Transactional(readOnly = true)
    public String getNextNumber(final Contractor contractor) {
        final ContractorDetail contractorDetail = contractor.getContractorDetails().get(0);
        final Serializable sequenceNumber = applicationSequenceNumberGenerator
                .getNextSequence(CONTRACTOR_CODE_SEQ_PREFIX);
        if (validateGrade(contractorDetail) && !StringUtils.isBlank(contractorDetail.getCategory())
                && !StringUtils.isBlank(contractor.getName()) && contractor.getName().length() >= 4) {
            final String contractorClass = contractorService
                    .getContractorClassShortName(contractorDetail.getGrade().getGrade());

            return String.format("%s%s%s%04d", !StringUtils.isBlank(contractorClass) ? contractorClass : "",
                    contractorDetail.getCategory().substring(0, 1), contractor.getName().substring(0, 4),
                    sequenceNumber);
        } else if (!StringUtils.isBlank(contractorDetail.getCategory()) && !StringUtils.isBlank(contractor.getName())
                && contractor.getName().length() >= 4)
            return String.format("%s%s%04d", contractorDetail.getCategory().substring(0, 1),
                    contractor.getName().substring(0, 4), sequenceNumber);
        else if (validateGrade(contractorDetail) && !StringUtils.isBlank(contractor.getName())
                && contractor.getName().length() >= 4) {
            final String contractorClass = contractorService
                    .getContractorClassShortName(contractorDetail.getGrade().getGrade());
            return String.format("%s%s%04d", !StringUtils.isBlank(contractorClass) ? contractorClass : "",
                    contractor.getName().substring(0, 4), sequenceNumber);
        } else if (!StringUtils.isBlank(contractor.getName()) && contractor.getName().length() >= 4)
            return String.format("%s%04d", contractor.getName().substring(0, 4), sequenceNumber);
        else
            return null;
    }

    private boolean validateGrade(final ContractorDetail contractorDetail) {
        return contractorDetail.getGrade() != null && !StringUtils.isBlank(contractorDetail.getGrade().getGrade());
    }

}