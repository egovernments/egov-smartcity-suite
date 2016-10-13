/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.masters.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.mrs.domain.enums.MarriageFeeCriteriaType;
import org.egov.mrs.masters.entity.Fee;
import org.egov.mrs.masters.repository.FeeRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FeeService {

	private final FeeRepository feeRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Autowired
	public FeeService(final FeeRepository feeRepository) {
		this.feeRepository = feeRepository;
	}

	@Transactional
	public void create(final Fee fee) {
		feeRepository.save(fee);
	}

	@Transactional
	public Fee update(final Fee fee) {
		return feeRepository.saveAndFlush(fee);
	}

	public Fee getFee(final Long id) {
		return feeRepository.findById(id);
	}

	public List<Fee> getAll() {
		return feeRepository.findAll();
	}

	public Fee getFeeForDays(Long days) {
		return feeRepository.findByToDaysLessThanEqual(days);
	}

	public Fee getFeeForDate(Date date) {
		Long daysAfterMarriage = ChronoUnit.DAYS.between(date.toInstant()
				.atZone(ZoneId.systemDefault()).toLocalDateTime(),
				LocalDateTime.now());

		return getFeeForDays(daysAfterMarriage);
	}

	public Fee getFeeForCriteria(String criteria) {
		return feeRepository.findByCriteria(criteria);
	}
	
	public List<Fee> searchFee(Fee fee) {
		  final Criteria criteria = buildSearchCriteria(fee);
	        return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Fee> searchRegistrationFeesWithGeneralType(Fee fee) {
		final Criteria criteria = buildSearchCriteria(fee);
		criteria.add(Restrictions.eq("feeType", MarriageFeeCriteriaType.GENERAL));
		return criteria.list();
		
	}
	
	public Criteria buildSearchCriteria(Fee fee) {
		final Criteria criteria = getCurrentSession().createCriteria(Fee.class);

		if (null != fee.getCriteria())
			criteria.add(Restrictions.ilike("criteria", fee.getCriteria().trim(),
					MatchMode.ANYWHERE));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

}
