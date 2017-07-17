/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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
package org.egov.ptis.domain.service.property;

import java.util.ArrayList;
import java.util.List;

import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.OwnerAudit;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.repository.OwnerAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OwnerAuditService {
    @Autowired
    private OwnerAuditRepository ownerAuditRepository;

    public List<OwnerAudit> setOwnerAuditDetails(final BasicProperty basicProperty) {
        List<OwnerAudit> ownerAuditList = new ArrayList<>();
        for (final PropertyOwnerInfo ownerInfo : basicProperty.getPropertyOwnerInfo()) {
            OwnerAudit ownerAudit = new OwnerAudit();
            ownerAudit.setBasicproperty(basicProperty.getId());
            if (ownerInfo != null) {
                ownerAudit.setDoorNo(basicProperty.getAddress().getHouseNoBldgApt());
                ownerAudit.setUserId(ownerInfo.getOwner().getId());
                ownerAudit.setAadhaarNo(ownerInfo.getOwner().getAadhaarNumber());
                ownerAudit.setMobileNo(ownerInfo.getOwner().getMobileNumber());
                ownerAudit.setOwnerName(ownerInfo.getOwner().getName());
                ownerAudit.setGender(ownerInfo.getOwner().getGender().name());
                ownerAudit.setEmailId(ownerInfo.getOwner().getEmailId());
                ownerAudit.setGuardianRelation(ownerInfo.getOwner().getGuardianRelation());
                ownerAudit.setGuardianName(ownerInfo.getOwner().getGuardian());
            }
            ownerAuditList.add(ownerAudit);
        }

        return ownerAuditList;
    }

    @Transactional
    public void saveOwnerDetails(final List<OwnerAudit> ownerAudit) {

        for (OwnerAudit owner : ownerAudit)
            ownerAuditRepository.save(owner);
    }

}