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

package org.egov.collection.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infstr.models.ServiceAccountDetails;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class ServiceDetailsService extends PersistenceService<ServiceDetails, Long> {
    public ServiceDetailsService(Class<ServiceDetails> type) {
        super(type);
        // TODO Auto-generated constructor stub
    }

    public ServiceDetailsService() {
        super(ServiceDetails.class);
    }

    private static final long serialVersionUID = 5581301494846870670L;
    private static final Logger LOGGER = Logger.getLogger(ServiceDetailsService.class);

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CollectionsUtil collectionsUtil;

    @Transactional
    public ServiceDetails persist(ServiceDetails serviceDetails) {
        final List<ServiceAccountDetails> accountList = entityManager
                .createQuery(" from ServiceAccountDetails sa where sa.serviceDetails.id =:serviceDetailId",
                        ServiceAccountDetails.class)
                .setParameter("serviceDetailId", serviceDetails.getId()).getResultList();

        for (final ServiceAccountDetails serviceAccountDetails : accountList) {
            entityManager.createQuery(
                    "delete from ServiceSubledgerInfo where serviceAccountDetail.id=:accountId")
                    .setParameter("accountId", serviceAccountDetails.getId()).executeUpdate();
        }

        entityManager
                .createQuery(" delete from ServiceAccountDetails where serviceDetails.id=:serviceId")
                .setParameter("serviceId", serviceDetails.getId()).executeUpdate();

        if (serviceDetails.getId() == null)
            entityManager.persist(serviceDetails);
        else {
            if (ApplicationThreadLocals.getUserId() != null) {
                final User user = collectionsUtil.getUserById(ApplicationThreadLocals.getUserId());
                serviceDetails.setCreatedBy(user);
                serviceDetails.setModifiedBy(user);
                serviceDetails.setCreatedDate(new Date());
                serviceDetails.setModifiedDate(new Date());
            }
            entityManager.merge(serviceDetails);
        }
        return serviceDetails;
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

}