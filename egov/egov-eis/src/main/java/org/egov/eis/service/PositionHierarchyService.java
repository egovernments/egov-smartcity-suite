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
package org.egov.eis.service;

import org.egov.eis.entity.PositionHierarchy;
import org.egov.eis.repository.PositionHierarchyRepository;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PositionHierarchyService {

    private final PositionHierarchyRepository positionHierarchyRepository;

    @Autowired
    public PositionHierarchyService(final PositionHierarchyRepository positionHierarchyRepository) {
        this.positionHierarchyRepository = positionHierarchyRepository;
    }

    @Transactional
    public void createPositionHierarchy(final PositionHierarchy positionHierarchy) {
        positionHierarchyRepository.save(positionHierarchy);
    }

    @Transactional
    public void updatePositionHierarchy(final PositionHierarchy positionHierarchy) {
        positionHierarchyRepository.save(positionHierarchy);
    }

    public PositionHierarchy getPositionHierarchyByPosAndObjectType(final Long posId, final Integer objectId) {
        return positionHierarchyRepository.getPositionHierarchyByPosAndObjectType(posId, objectId);
    }

    public PositionHierarchy getPosHirByPosAndObjectTypeAndObjectSubType(final Long posId, final Integer objectId,
                                                                         final String objectSubType) {
        return positionHierarchyRepository.getPosHirByPosAndObjectTypeAndObjectSubType(posId, objectId, objectSubType);
    }

    public void deleteAllInBatch(final List<PositionHierarchy> existingPosHierarchy) {
        positionHierarchyRepository.deleteInBatch(existingPosHierarchy);

    }

    public List<PositionHierarchy> getPositionHeirarchyByFromPositionAndObjectType(Long fromPositionId, Integer objectId) {
        return positionHierarchyRepository.getListOfPositionHeirarchyByFromPositionAndObjectType(fromPositionId, objectId);
    }

    public List<PositionHierarchy> getListOfPositionHeirarchyByFromPositionAndObjectTypeAndSubType(Long fromPositionId, Integer objectId, final String objectSubType) {

        if (fromPositionId != 0 && objectId != 0 && objectSubType != null)
            return positionHierarchyRepository.getListOfPositionHeirarchyByFromPositionAndObjectTypeAndSubType(fromPositionId, objectId, objectSubType);
        else if (fromPositionId == 0 && objectId != 0 && objectSubType != null)
            return positionHierarchyRepository.getPosHirByObjectTypeAndObjectSubType(objectId, objectSubType);
        else if (fromPositionId != 0 && objectId != 0 && objectSubType == null)
            return positionHierarchyRepository.getListOfPositionHeirarchyByFromPositionAndObjectType(fromPositionId, objectId);
        else if (fromPositionId == 0 && objectId != 0 && objectSubType == null)
            return positionHierarchyRepository.getListOfPositionHeirarchyByObjectType(objectId);
        else return Collections.emptyList();
    }

    public List<PositionHierarchy> getListOfPositionHeirarchyByPositionObjectTypeSubType(final Integer objectType,
                                                                                         final List<String> objectSubTypes,
                                                                                         final Position fromPositionId) {
        return positionHierarchyRepository.findPositionHierarchyByObjectSubTypeAndFromPosition(objectType, objectSubTypes,
                fromPositionId);
    }

}
