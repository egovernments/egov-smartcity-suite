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

package org.egov.mrs.masters.service;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.mrs.domain.enums.MarriageFeeCriteriaType;
import org.egov.mrs.masters.entity.MarriageFee;
import org.egov.mrs.masters.repository.MarriageFeeRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MarriageFeeService {

    @Autowired
    private MarriageFeeRepository marriageFeeRepository;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    protected AppConfigValueService appConfigValuesService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public void create(final MarriageFee fee) {
        marriageFeeRepository.save(fee);
    }

    @Transactional
    public MarriageFee update(final MarriageFee fee) {
        return marriageFeeRepository.saveAndFlush(fee);
    }

    public MarriageFee getFee(final Long id) {
        return marriageFeeRepository.findById(id);
    }

    public List<MarriageFee> getAll() {
        return marriageFeeRepository.findAll();
    }

    public MarriageFee getFeeForDays(Long days) {
        return marriageFeeRepository.findByToDaysLessThanEqual(days);
    }

    public MarriageFee getFeeForDate(Date date) {
        Long daysAfterMarriage = ChronoUnit.DAYS.between(date.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime(),
                LocalDateTime.now());

        return getFeeForDays(daysAfterMarriage);
    }

    public MarriageFee getFeeForCriteria(String criteria) {
        return marriageFeeRepository.findByCriteria(criteria);
    }

    public List<MarriageFee> searchFee(MarriageFee fee) {
        final Criteria criteria = buildSearchCriteria(fee);
        return criteria.list();
    }
    
    public AppConfigValues getDaysValidationAppConfValue(final String moduleName, final String keyName) {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, keyName);
        return !appConfigValues.isEmpty() ? appConfigValues.get(0) : null;
    }

    public List<MarriageFee> getActiveGeneralTypeFeeses() {
        final Criteria criteria = getCurrentSession().createCriteria(MarriageFee.class);
        criteria.add(Restrictions.eq("feeType", MarriageFeeCriteriaType.GENERAL)).add(Restrictions.eq("active", Boolean.TRUE));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<MarriageFee> searchRegistrationFeesWithGeneralType(MarriageFee fee) {
        final Criteria criteria = buildSearchCriteria(fee);
        criteria.add(Restrictions.eq("feeType", MarriageFeeCriteriaType.GENERAL));
        return criteria.list();

    }

    public Criteria buildSearchCriteria(MarriageFee fee) {
        final Criteria criteria = getCurrentSession().createCriteria(MarriageFee.class);

        if (null != fee.getCriteria())
            criteria.add(Restrictions.ilike("criteria", fee.getCriteria().trim(),
                    MatchMode.ANYWHERE));

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

}
