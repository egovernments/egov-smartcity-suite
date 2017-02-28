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

package org.egov.works.masters.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.ContractorClassSearchRequest;
import org.egov.commons.ContractorGrade;
import org.egov.works.masters.repository.ContractorGradeRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("contractorGradeService")
@Transactional(readOnly = true)
public class ContractorGradeService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ContractorGradeRepository contractorGradeRepository;

    public ContractorGrade getContractorGradeById(final Long contractorGradeId) {
        return contractorGradeRepository.findOne(contractorGradeId);
    }

    public List<ContractorGrade> getAllContractorGrades() {
        return contractorGradeRepository.findAll(new Sort(Sort.Direction.ASC, "grade"));
    }

    @Transactional
    public ContractorGrade save(final ContractorGrade contractorGrade) {
        return contractorGradeRepository.save(contractorGrade);
    }

    public List<ContractorGrade> searchContractorClassToView(
            final ContractorClassSearchRequest contractorClassSearchRequest) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(ContractorGrade.class)
                .addOrder(Order.asc("createdDate"));
        if (contractorClassSearchRequest != null) {
            if (contractorClassSearchRequest.getContractorClass() != null)
                criteria.add(Restrictions.eq("grade", contractorClassSearchRequest.getContractorClass()));
            if (contractorClassSearchRequest.getMinAmount() != null)
                criteria.add(Restrictions.eq("minAmount", new BigDecimal(contractorClassSearchRequest.getMinAmount())));
            if (contractorClassSearchRequest.getMaxAmount() != null)
                criteria.add(Restrictions.eq("maxAmount", new BigDecimal(contractorClassSearchRequest.getMaxAmount())));
        }

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Transactional
    public ContractorGrade update(final ContractorGrade contractorGrade) {
        return contractorGradeRepository.save(contractorGrade);
    }

    public List<String> getAllContractorMinAmounts() {
        return contractorGradeRepository.findByContractorClassMinAmount();
    }

    public List<String> getAllContractorMaxAmounts() {
        return contractorGradeRepository.findByContractorClassMaxAmount();
    }

    public ContractorGrade getByMinAndMaxAmount(final BigDecimal minAmount, final BigDecimal maxAmount) {
        return contractorGradeRepository.findByMinAndMaxAmount(minAmount, maxAmount);
    }

    public ContractorGrade getByContractorClass(final String grade) {
        return contractorGradeRepository.findByGrade(grade);
    }

    public ContractorGrade findByContractorClass(final String contractorClass) {
        return contractorGradeRepository.findByGradeIgnoreCase(contractorClass.toUpperCase());
    }

}
