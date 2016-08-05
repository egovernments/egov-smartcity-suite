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
package org.egov.works.offlinestatus.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.offlinestatus.repository.OfflineStatusRepository;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OfflineStatusService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OfflineStatusRepository offlineStatusRepository;

    @Autowired
    private WorksUtils worksUtils;

    @Transactional
    public void create(final List<OfflineStatus> offlineStatuses, final Long ObjectId, final String ObjectType) {
        for (final OfflineStatus status : offlineStatuses)
            if (status.getId() == null) {
                status.setObjectId(ObjectId);
                status.setObjectType(ObjectType);
                status.setEgwStatus(worksUtils.getStatusById(status.getEgwStatus().getId()));
                offlineStatusRepository.save(status);
            }
    }

    public List<OfflineStatus> getOfflineStatusByObjectIdAndType(final Long objectId, final String objectType) {
        return offlineStatusRepository.findByObjectIdAndObjectType(objectId, objectType);
    }

    @SuppressWarnings("unchecked")
    public Collection<String> getStatusNameDetails(final String[] statusNames) {
        return CollectionUtils.select(Arrays.asList(statusNames), statusName -> (String) statusName != null);
    }

    public OfflineStatus getOfflineStatusByObjectIdAndObjectTypeAndStatus(final Long objectId, final String objectType,
            final String statusCode) {
        return offlineStatusRepository.findByObjectIdAndObjectTypeAndEgwStatus_code(objectId, objectType, statusCode);
    }

    public OfflineStatus getLastOfflineStatusByObjectIdAndObjectType(final Long objectId, final String objectType) {
        return offlineStatusRepository.getLastOfflineStatusByObjectIdAndObjectType(objectId, objectType);
    }
}
