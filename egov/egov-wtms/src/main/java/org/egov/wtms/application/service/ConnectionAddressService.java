/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.wtms.application.service;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.ConnectionAddressRepository;
import org.egov.wtms.masters.entity.ConnectionAddress;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConnectionAddressService {

    @Autowired
    private ConnectionAddressRepository connectionAddressRepository;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private BoundaryService boundaryService;

    @Transactional
    public void save(final ConnectionAddress connectionAddress) {
        connectionAddressRepository.save(connectionAddress);
    }

    @Transactional
    public void createConnectionAddress(final WaterConnectionDetails waterConnectionDetails) {
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(), PropertyExternalService.FLAG_FULL_DETAILS,
                BasicPropertyStatus.ALL);

        if (assessmentDetails == null)
            throw new ApplicationRuntimeException("err.assessment.details.not.present");
        else {
            String errorKey = StringUtils.EMPTY;
            ConnectionAddress connectionAddress = new ConnectionAddress();
            if (assessmentDetails.getBoundaryDetails().getZoneId() == null)
                errorKey = "err.zone.id.not.present";
            if (assessmentDetails.getBoundaryDetails().getWardId() == null)
                errorKey = "err.ward.id.not.present";
            if (assessmentDetails.getBoundaryDetails().getAdminWardId() == null)
                errorKey = "err.adminward.id.not.present";
            if (assessmentDetails.getBoundaryDetails().getLocalityId() == null)
                errorKey = "err.locality.id.not.present";
            if (StringUtils.isNoneBlank(errorKey))
                throw new ApplicationRuntimeException(errorKey);
            connectionAddress.setZone(boundaryService.getBoundaryById(assessmentDetails.getBoundaryDetails().getZoneId()));
            connectionAddress.setRevenueWard(boundaryService.getBoundaryById(assessmentDetails.getBoundaryDetails().getWardId()));
            connectionAddress
                    .setAdminWard(boundaryService.getBoundaryById(assessmentDetails.getBoundaryDetails().getAdminWardId()));
            connectionAddress
                    .setLocality(boundaryService.getBoundaryById(assessmentDetails.getBoundaryDetails().getLocalityId()));
            if (assessmentDetails.getOwnerNames() != null) {
                Iterator<OwnerName> ownerNameItr = null;
                ownerNameItr = assessmentDetails.getOwnerNames().iterator();
                if (ownerNameItr != null && ownerNameItr.hasNext()) {
                    final OwnerName primaryOwner = ownerNameItr.next();
                    connectionAddress.setOwnerName(
                            primaryOwner == null || primaryOwner.getOwnerName() == null ? EMPTY : primaryOwner.getOwnerName());
                }

                connectionAddress.setDoorNumber(assessmentDetails.getHouseNo());
                connectionAddress.setAddress(assessmentDetails.getPropertyAddress());
                connectionAddress.setWaterConnectionDetails(waterConnectionDetails);
                save(connectionAddress);
            }
        }
    }
}
