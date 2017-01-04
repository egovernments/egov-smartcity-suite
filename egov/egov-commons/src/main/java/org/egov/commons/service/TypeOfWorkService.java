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
package org.egov.commons.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.TypeOfWorkSearchRequest;
import org.egov.commons.repository.TypeOfWorkRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TypeOfWorkService {

    private final TypeOfWorkRepository typeOfWorkRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TypeOfWorkService(final TypeOfWorkRepository typeOfWorkRepository) {
        this.typeOfWorkRepository = typeOfWorkRepository;
    }

    @Transactional
    public EgwTypeOfWork create(final EgwTypeOfWork typeOfWork) {
        return typeOfWorkRepository.save(typeOfWork);
    }

    public EgwTypeOfWork getTypeOfWorkById(final Long typeOfWorkId) {
        return typeOfWorkRepository.findOne(typeOfWorkId);
    }

    public EgwTypeOfWork getTypeOfWorkByCode(final String code) {
        return typeOfWorkRepository.findByCodeIgnoreCase(code);
    }

    public List<EgwTypeOfWork> findAll() {
        return typeOfWorkRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public List<EgwTypeOfWork> searchTypeOfWorkToView(final TypeOfWorkSearchRequest searchRequestTypeOfWork) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(EgwTypeOfWork.class)
                .addOrder(Order.asc("createdDate"));
        if (searchRequestTypeOfWork != null) {
            if (searchRequestTypeOfWork.getTypeOfWorkCode() != null)
                criteria.add(Restrictions.eq("code", searchRequestTypeOfWork.getTypeOfWorkCode()));
            if (searchRequestTypeOfWork.getTypeOfWorkName() != null)
                criteria.add(Restrictions.eq("name", searchRequestTypeOfWork.getTypeOfWorkName()));
            criteria.add(Restrictions.isNull("parentid.id"));
        }

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();

    }

    public List<EgwTypeOfWork> getTypeOfWorkByPartyType(final String partyTypeCode) {
        return typeOfWorkRepository.findByPartyType(partyTypeCode.toUpperCase());
    }

    public List<EgwTypeOfWork> getByParentidAndEgPartytype(final Long parentId, final String partyTypeCode) {
        return typeOfWorkRepository.findByParentidAndEgPartytype(parentId, partyTypeCode.toUpperCase());
    }

    public List<EgwTypeOfWork> searchSubTypeOfWorkToView(final TypeOfWorkSearchRequest typeOfWorkSearchRequest) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(EgwTypeOfWork.class)
                .addOrder(Order.asc("createdDate"));
        if (typeOfWorkSearchRequest != null) {
            if (typeOfWorkSearchRequest.getTypeOfWorkParentId() != null)
                criteria.add(Restrictions.eq("parentid.id", typeOfWorkSearchRequest.getTypeOfWorkParentId()));
            if (typeOfWorkSearchRequest.getTypeOfWorkCode() != null)
                criteria.add(Restrictions.eq("code", typeOfWorkSearchRequest.getTypeOfWorkCode()));
            if (typeOfWorkSearchRequest.getTypeOfWorkName() != null)
                criteria.add(Restrictions.eq("name", typeOfWorkSearchRequest.getTypeOfWorkName()));
            criteria.add(Restrictions.isNotNull("parentid.id"));
        }

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Transactional
    public EgwTypeOfWork update(final EgwTypeOfWork typeOfWork) {
        return typeOfWorkRepository.save(typeOfWork);
    }

    public List<EgwTypeOfWork> getActiveTypeOfWorksByPartyType(final String partyTypeCode) {
        return typeOfWorkRepository
                .findByParentid_idIsNullAndEgPartytype_codeContainingIgnoreCaseAndActiveTrueOrderByName(partyTypeCode);
    }

    public List<EgwTypeOfWork> getActiveSubTypeOfWorksByParentIdAndPartyType(final Long parentid,
            final String partyTypeCode) {
        return typeOfWorkRepository.findByParentid_idAndEgPartytype_codeContainingIgnoreCaseAndActiveTrueOrderByName(
                parentid, partyTypeCode);
    }
}
