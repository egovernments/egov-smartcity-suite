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
package org.egov.wtms.application.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.wtms.application.entity.LegacyReceipts;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.LegacyReceiptsRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LegacyReceiptsSevice {

    protected LegacyReceiptsRepository legacyReceiptsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public LegacyReceiptsSevice(final LegacyReceiptsRepository legacyReceiptsRepository) {
        this.legacyReceiptsRepository = legacyReceiptsRepository;
    }

    public List<LegacyReceipts> findByWaterConnectionDetails(final WaterConnectionDetails waterConnectionDetails) {
        return legacyReceiptsRepository.findByWaterConnectionDetails(waterConnectionDetails);
    }

    public List<LegacyReceipts> findAll() {
        return legacyReceiptsRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
    }

    public LegacyReceipts load(final Long id) {
        return legacyReceiptsRepository.getOne(id);
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public List<LegacyReceipts> createLegacyReceipts(final List<LegacyReceipts> legacyReceipts) {
        return legacyReceiptsRepository.save(legacyReceipts);
    }

    @Transactional
    public void updateLegacyReceipts(final LegacyReceipts legacyReceipts) {
        legacyReceiptsRepository.save(legacyReceipts);
    }

    public LegacyReceipts findByReceiptNumber(final String receiptNumber) {
        return legacyReceiptsRepository.findByReceiptNumber(receiptNumber);
    }

}
