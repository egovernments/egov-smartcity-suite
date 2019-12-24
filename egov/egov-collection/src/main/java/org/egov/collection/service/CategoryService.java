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

package org.egov.collection.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.infstr.models.ServiceCategory;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

	private static final Logger LOGGER = Logger.getLogger(CategoryService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public ServiceCategory persist(ServiceCategory serviceCategory) {
		getCurrentSession().saveOrUpdate(serviceCategory);
		return serviceCategory;
	}

	public Optional<ServiceCategory> getServiceCategoryByCode(String code) {
		final Query query = entityManager.createNamedQuery(CollectionConstants.QUERY_SERVICE_CATEGORY_BY_CODE);
		query.setParameter(1, code);
		return query.getResultList().stream().findFirst();
	}

	public Optional<ServiceCategory> getServiceCategoryByCodeAndId(String code, long id) {
		final Query query = entityManager.createNamedQuery(CollectionConstants.QUERY_SERVICE_CATEGORY_BY_CODE_ID);
		query.setParameter(1, code);
		query.setParameter(2, id);
		return query.getResultList().stream().findFirst();
	}

	public List<ServiceCategory> getActiveServiceCategoryList() {
		return entityManager.createNamedQuery(CollectionConstants.QUERY_ACTIVE_SERVICE_CATEGORY).getResultList();
	}

	private Session getCurrentSession() {
		return this.entityManager.unwrap(Session.class);
	}

}