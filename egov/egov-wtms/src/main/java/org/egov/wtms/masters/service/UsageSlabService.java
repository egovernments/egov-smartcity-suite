/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

package org.egov.wtms.masters.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infstr.services.Page;
import org.egov.wtms.application.entity.UsageSlabSearchRequest;
import org.egov.wtms.masters.entity.UsageSlab;
import org.egov.wtms.masters.repository.UsageSlabRepository;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsageSlabService {

    @Autowired
    private UsageSlabRepository usageSlabRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(final UsageSlab usageSlab) {
        usageSlabRepository.save(usageSlab);
    }

    public Page<UsageSlab> search(final UsageSlabSearchRequest usageSlabSearchRequest) {
        final StringBuilder queryString = new StringBuilder(512);
        queryString.append("from UsageSlab U where U.active=:active ");
        if (usageSlabSearchRequest.getUsage() != null)
            queryString.append(" and U.usage=:usage");
        if (usageSlabSearchRequest.getSlabName() != null)
            queryString.append(" and U.slabName=:slabName");
        if (usageSlabSearchRequest.getFromVolume() != null)
            queryString.append(" and U.fromVolume=:fromVolume");
        if (usageSlabSearchRequest.getToVolume() != null)
            queryString.append(" and U.toVolume=:toVolume");
        final Query query = entityManager.unwrap(Session.class).createQuery(queryString.toString());

        query.setParameter("active", usageSlabSearchRequest.isActive());

        if (usageSlabSearchRequest.getUsage() != null)
            query.setParameter("usage", usageSlabSearchRequest.getUsage());
        if (usageSlabSearchRequest.getSlabName() != null)
            query.setParameter("slabName", usageSlabSearchRequest.getSlabName());
        if (usageSlabSearchRequest.getFromVolume() != null)
            query.setParameter("fromVolume", usageSlabSearchRequest.getFromVolume());
        if (usageSlabSearchRequest.getToVolume() != null)
            query.setParameter("toVolume", usageSlabSearchRequest.getToVolume());
        query.setParameter("active", usageSlabSearchRequest.isActive());
        final int size = getTotalRecords(query);
        return new Page<>(query, usageSlabSearchRequest.pageNumber(), usageSlabSearchRequest.pageSize(), size);
    }

    public UsageSlab findById(final Long id) {
        return usageSlabRepository.findOne(id);
    }

    public List<UsageSlab> getActiveUsageSlabs() {
        return usageSlabRepository.findByActiveTrue();
    }

    public int getTotalRecords(final Query query) {
        return query.list().size();
    }

    public UsageSlab checkSlabOverlap(final String usage, final Long fromVolume, final Long toVolume) {
        final List<UsageSlab> usageSlabList = usageSlabRepository.isSlabOverLap(usage, fromVolume, toVolume);
        if (usageSlabList.isEmpty())
            return new UsageSlab();
        return usageSlabList.get(0);
    }

    @SuppressWarnings("unchecked")
    public UsageSlab checkSlabGap(final String usage, final Long fromVolume, final Long toVolume) {
        List<UsageSlab> usageSlabList;
        final List<UsageSlab> slabList = usageSlabRepository.getActiveSlabLessThanGivenFromVolume(usage, fromVolume);
        if (!slabList.isEmpty()) {
            final UsageSlab slab = slabList.get(0);
            if (slab.getFromVolume() != null && slab.getToVolume() == null)
                return slab;
        }

        final StringBuilder queryString = new StringBuilder();
        queryString.append("from UsageSlab u where u.usage=:usage ");
        if (fromVolume != null)
            queryString.append(" and (u.fromVolume<=:fromVolume and u.toVolume<=:fromVolume) ");
        if (toVolume != null)
            queryString.append(" or (u.fromVolume>=:toVolume and u.toVolume>=:toVolume)");
        if (!slabList.isEmpty())
            queryString.append(" order by fromVolume desc");
        else
            queryString.append(" order by fromVolume asc");

        final Query query = entityManager.unwrap(Session.class).createQuery(queryString.toString());

        if (usage != null)
            query.setParameter("usage", usage);
        if (fromVolume != null)
            query.setParameter("fromVolume", fromVolume);
        if (toVolume != null)
            query.setParameter("toVolume", toVolume);
        usageSlabList = query.list();
        if (usageSlabList.isEmpty())
            return new UsageSlab();
        return usageSlabList.get(0);
    }

    public UsageSlab findBySlabName(final String slabName) {
        return usageSlabRepository.findBySlabName(slabName);
    }

}
