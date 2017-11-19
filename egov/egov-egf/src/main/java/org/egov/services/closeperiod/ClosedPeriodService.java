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

package org.egov.services.closeperiod;

import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.commons.service.FinancialYearService;
import org.egov.egf.model.ClosedPeriod;
import org.egov.repository.closeperiod.ClosedPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClosedPeriodService {

	@Autowired
	private final ClosedPeriodRepository closedPeriodRepository;
	private FinancialYearService financialYearService;
	@Autowired
	CFinancialYearService cFinancialYearService;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public ClosedPeriodService(
			final ClosedPeriodRepository closedPeriodRepository) {
		this.closedPeriodRepository = closedPeriodRepository;
	}

	@Transactional
	public ClosedPeriod create(final ClosedPeriod closedPeriod) {
		return closedPeriodRepository.save(closedPeriod); 
	}

	@Transactional
	public ClosedPeriod update(final ClosedPeriod closedPeriod) {
		List<ClosedPeriod> closePer = findAll();

		for (ClosedPeriod cp : closePer) {
			if (cp.getcFinancialYearId().getId() == closedPeriod
					.getcFinancialYearId().getId())

				closedPeriodRepository.delete(cp);

		}

		return closedPeriodRepository.save(closedPeriod);
	}

	@Transactional
	public void delete(final ClosedPeriod closedPeriod) {
		closedPeriodRepository.delete(closedPeriod);

	}

	public List<ClosedPeriod> findAll() {
		return closedPeriodRepository.findAll(new Sort(Sort.Direction.ASC,
				"cFinancialYearId"));
	}

	public ClosedPeriod findOne(Long id) {
		List<ClosedPeriod> closePer = findAll();
		CFinancialYear c = cFinancialYearService.findOne(id);
		ClosedPeriod cc = new ClosedPeriod();

		for (ClosedPeriod cp : closePer) {
			if (cp.getcFinancialYearId().getId() == id) {
				cc.setIsClosed(cp.getIsClosed());
			}

		}

		cc.setcFinancialYearId(c);
		return cc;
	}

	public List<ClosedPeriod> search(ClosedPeriod closedPeriod) {

		ClosedPeriod closedPeriodd = new ClosedPeriod();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ClosedPeriod> createQuery = cb
				.createQuery(ClosedPeriod.class);

		Root<ClosedPeriod> closedPeriods = createQuery.from(ClosedPeriod.class);
		createQuery.select(closedPeriods);
		Metamodel m = entityManager.getMetamodel();
		javax.persistence.metamodel.EntityType<ClosedPeriod> ClosedPeriod_ = m
				.entity(ClosedPeriod.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		createQuery.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<ClosedPeriod> query = entityManager.createQuery(createQuery);

		if (closedPeriod.getcFinancialYearId() != null) {
			ClosedPeriod resultList1 = findOne(closedPeriod
					.getcFinancialYearId().getId());
			List<ClosedPeriod> resultList = new ArrayList<ClosedPeriod>();
			resultList.add(resultList1);
			return resultList;

		}

		List<ClosedPeriod> resultList = findAll();

		List<CFinancialYear> findAll = cFinancialYearService.findAll();
		List<Long> finIds = new ArrayList<Long>();

		for (ClosedPeriod f : resultList) {
			finIds.add(f.getcFinancialYearId().getId());
		}
		for (CFinancialYear f : findAll) {
			if (finIds.contains(f.getId()))
				continue;
			else {
				ClosedPeriod cp = new ClosedPeriod();
				cp.setcFinancialYearId(f);
				resultList.add(cp);
			}
		}
		Collections.sort(resultList);

		return resultList;

	}
}