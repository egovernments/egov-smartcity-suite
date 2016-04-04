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
package org.egov.infra.search.elastic.service;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.infra.search.elastic.entity.CollectionIndex;
import org.egov.infra.search.elastic.repository.CollectionIndexRepository;
import org.egov.infra.utils.EgovThreadLocals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CollectionIndexService {

    private final CollectionIndexRepository collectionIndexRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    public CollectionIndexService(final CollectionIndexRepository collectionIndexRepository) {
        this.collectionIndexRepository = collectionIndexRepository;
    }

    @Transactional
    @Indexing(name = Index.COLLECTION, type = IndexType.COLLECTION_BIFURCATION)
    public CollectionIndex pushCollectionIndex(final CollectionIndex collectionIndex) {
        final CollectionIndex collectionIndexReceipt = findByReceiptNumber(collectionIndex.getReceiptNumber());
        if (collectionIndexReceipt != null) {
            collectionIndexReceipt.setStatus(collectionIndex.getStatus());
            collectionIndexRepository.save(collectionIndexReceipt);
        } else {
            final City cityWebsite = cityService.getCityByURL(EgovThreadLocals.getDomainName());
            collectionIndex.setUlbName(cityWebsite.getName());
            if (cityWebsite.getDistrictName() != null)
                collectionIndex.setDistrictName(cityWebsite.getDistrictName());
            if (cityWebsite.getRegionName() != null)
                collectionIndex.setRegionName(cityWebsite.getRegionName());
            collectionIndexRepository.save(collectionIndex);
        }
        return collectionIndex;
    }

    public CollectionIndex findByReceiptNumber(final String receiptNumber) {
        final City cityWebsite = cityService.getCityByURL(EgovThreadLocals.getDomainName());
        return collectionIndexRepository.findByReceiptNumberAndUlbName(receiptNumber, cityWebsite.getName());
    }

}
