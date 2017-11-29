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

package org.egov.commons.service;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.repository.AccountEntityRepository;
import org.egov.commons.utils.EntityType;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.masters.model.AccountEntity;
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
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AccountEntityService implements EntityTypeService {

    private final AccountEntityRepository accountEntityRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AccountdetailtypeService accountdetailtypeService;

    @Autowired
    public AccountEntityService(final AccountEntityRepository accountEntityRepository) {
        this.accountEntityRepository = accountEntityRepository;
    }

    @Autowired
    private AccountDetailKeyService accountDetailKeyService;

    @Transactional
    public AccountEntity create(AccountEntity accountEntity) {
        AccountEntity accountEntitytmp = new AccountEntity();
        accountEntitytmp = accountEntityRepository.save(accountEntity);
        Accountdetailkey ac = new Accountdetailkey();
        ac.setDetailkey(accountEntitytmp.getId());
        ac.setDetailname(accountEntitytmp.getName());
        ac.setGroupid(1);
        ac.setAccountdetailtype(accountEntitytmp.getAccountdetailtype());
        accountDetailKeyService.create(ac);
        return accountEntitytmp;
    }

    @Transactional
    public AccountEntity update(final AccountEntity accountEntity) {

        return accountEntityRepository.save(accountEntity);
    }

    public List<AccountEntity> findAll() {
        return accountEntityRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public AccountEntity findByName(String name) {
        return accountEntityRepository.findByName(name);
    }

    public AccountEntity findByCode(String code) {
        return accountEntityRepository.findByCode(code);
    }

    public AccountEntity findOne(Integer id) {
        return accountEntityRepository.findOne(id);
    }

    public List<AccountEntity> search(AccountEntity accountEntity) {

        if (accountEntity.getAccountdetailtype() != null && accountEntity.getAccountdetailtype().getId() == null) {
            accountEntity.setAccountdetailtype(null);
        } else {
            accountEntity.setAccountdetailtype(accountdetailtypeService.findOne(accountEntity.getAccountdetailtype().getId()));
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AccountEntity> createQuery = cb.createQuery(AccountEntity.class);
        Root<AccountEntity> accountEntitys = createQuery.from(AccountEntity.class);
        createQuery.select(accountEntitys);
        Metamodel m = entityManager.getMetamodel();
        javax.persistence.metamodel.EntityType<AccountEntity> AccountEntity_ = m.entity(AccountEntity.class);

        List<Predicate> predicates = new ArrayList<Predicate>();
        if (accountEntity.getName() != null) {
            String name = "%" + accountEntity.getName().toLowerCase() + "%";
            predicates.add(cb.isNotNull(accountEntitys.get("name")));
            predicates.add(cb
                    .like(cb.lower(accountEntitys.get(AccountEntity_.getDeclaredSingularAttribute("name", String.class))), name));
        }
        if (accountEntity.getCode() != null) {
            String code = "%" + accountEntity.getCode().toLowerCase() + "%";
            predicates.add(cb.isNotNull(accountEntitys.get("code")));
            predicates.add(cb
                    .like(cb.lower(accountEntitys.get(AccountEntity_.getDeclaredSingularAttribute("code", String.class))), code));
        }
        if (accountEntity.getAccountdetailtype() != null) {
            predicates.add(cb.equal(accountEntitys.get("accountdetailtype"), accountEntity.getAccountdetailtype()));
        }

        createQuery.where(predicates.toArray(new Predicate[] {}));
        TypedQuery<AccountEntity> query = entityManager.createQuery(createQuery);
        // query.setFlushMode(FlushModeType.COMMIT);

        List<AccountEntity> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<? extends EntityType> getAllActiveEntities(Integer accountDetailTypeId) {
        Accountdetailtype accountdetailtype = accountdetailtypeService.findOne(accountDetailTypeId);
        List<AccountEntity> activeEntityList = accountEntityRepository.findByAccountdetailtypeAndIsactive(accountdetailtype,
                true);
        return activeEntityList;
    }

    @Override
    public List<? extends EntityType> filterActiveEntities(String filterKey, int maxRecords,
            Integer accountDetailTypeId) {
        // final Integer pageSize = maxRecords > 0 ? maxRecords : null;
        // Pageable pageable= new PageRequest(1, maxRecords);
        final List<EntityType> entities = new ArrayList<EntityType>();
        filterKey = "%" + filterKey + "%";
        List<AccountEntity> pagedEntities = accountEntityRepository.findBy20(accountDetailTypeId, filterKey);
        entities.addAll(pagedEntities);
        return entities;
    }

    @Override
    public List getAssetCodesForProjectCode(Integer accountdetailkey) throws ValidationException {
        return null;
    }

    @Override
    public List<? extends EntityType> validateEntityForRTGS(List<Long> idsList) throws ValidationException {

        return null;
    }

    @Override
    public List<? extends EntityType> getEntitiesById(List<Long> idsList) throws ValidationException {
        /*
         * Iterable<Integer> it; for(Long l:idsList) { } return accountEntityRepository.findAll(it);
         */
        return null;
    }
}