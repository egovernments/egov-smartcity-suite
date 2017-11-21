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
package org.egov.lcms.masters.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.infra.admin.master.entity.BusinessUser;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.lcms.autonumber.AdvocateUserNameGenerator;
import org.egov.lcms.masters.entity.AdvocateMaster;
import org.egov.lcms.masters.repository.AdvocateMasterRepository;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvocateMasterService implements EntityTypeService {

    private final AdvocateMasterRepository advocateMasterRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;

    @Autowired
    private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private AdvocateUserNameGenerator advocateUserNameGenerator;
    
    @Autowired
    public AdvocateMasterService(final AdvocateMasterRepository advocateMasterRepository) {
        this.advocateMasterRepository = advocateMasterRepository;
    }

    @Transactional
    public AdvocateMaster persist(final AdvocateMaster advocateMaster) {
        return advocateMasterRepository.save(advocateMaster);
    }

    public List<AdvocateMaster> findAll() {
        return advocateMasterRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public List<AdvocateMaster> getActiveAdvocateMaster() {
        return advocateMasterRepository.findByIsActiveTrueOrderByNameAsc();
    }

    public AdvocateMaster findByName(final String name) {
        return advocateMasterRepository.findByName(name);
    }

    public List<AdvocateMaster> getAllAdvocatesByNameLikeAndIsSeniorAdvocate(final String name,
            final Boolean isSeniorAdvocate) {
        return advocateMasterRepository.findByNameContainingIgnoreCaseAndIsSeniorAdvocate(name, isSeniorAdvocate);
    }

    public AdvocateMaster findOne(final Long id) {
        return advocateMasterRepository.findOne(id);
    }

    @Transactional
    public void createAccountDetailKey(final AdvocateMaster ad) {
        final Accountdetailtype accountdetailtype = accountdetailtypeHibernateDAO.getAccountdetailtypeByName("lawyer");
        final Accountdetailkey accountdetailkey = new Accountdetailkey();
        accountdetailkey.setGroupid(1);
        accountdetailkey.setDetailkey(ad.getId().intValue());
        accountdetailkey.setDetailname(accountdetailtype.getAttributename());
        accountdetailkey.setAccountdetailtype(accountdetailtype);
        accountdetailkeyHibernateDAO.create(accountdetailkey);
    }

    @Transactional
    public void createAdvocateUser(final AdvocateMaster advocateMaster) {
        User user = null;
        if (advocateMaster.getAdvocateUser() != null && advocateMaster.getAdvocateUser().getId() != null)
            user = userService.getUserById(advocateMaster.getAdvocateUser().getId());
        if (user == null) {
            final BusinessUser businessUser = new BusinessUser();
            user = createNewAdvocateUser(advocateMaster, businessUser);
            advocateMaster.setAdvocateUser(user);
        } else {
            user.setMobileNumber(advocateMaster.getMobileNumber());
            user.setEmailId(advocateMaster.getEmail() != null ? advocateMaster.getEmail() : "");
            user.setName(advocateMaster.getName());
            user.setSalutation(advocateMaster.getSalutation());
            user.setPan(advocateMaster.getPanNumber());
            advocateMaster.setAdvocateUser(user);
        }

    }

    private User createNewAdvocateUser(final AdvocateMaster advocateMaster, final BusinessUser businessUser) {
        businessUser.setMobileNumber(advocateMaster.getMobileNumber());
        businessUser.setEmailId(advocateMaster.getEmail() != null ? advocateMaster.getEmail() : "");
        businessUser.setName(advocateMaster.getName());
        businessUser.setSalutation(advocateMaster.getSalutation());
        businessUser.setPassword(passwordEncoder.encode("demo"));
        businessUser.setPwdExpiryDate(DateTime.now().plusMonths(12).toDate());
        businessUser.setPan(advocateMaster.getPanNumber());
        businessUser.setActive(true);
        businessUser.setUsername(advocateUserNameGenerator.generateAdvocateUserName(advocateMaster));
        businessUser.addRole(roleService.getRoleByName(LcmsConstants.ROLE_EMP_PORTAL_ACCESS));
        businessUser.addRole(roleService.getRoleByName(LcmsConstants.ROLE_BUSINESS));
        businessUser.addRole(roleService.getRoleByName(LcmsConstants.ROLE_TPSTANDINGCOUNSEL));
        return userService.createUser(businessUser);
    }
    
    public List<AdvocateMaster> search(final AdvocateMaster advocateMaster) {

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<AdvocateMaster> createQuery = cb.createQuery(AdvocateMaster.class);
        final Root<AdvocateMaster> advocateMasterObj = createQuery.from(AdvocateMaster.class);
        createQuery.select(advocateMasterObj);
        final Metamodel m = entityManager.getMetamodel();
        final javax.persistence.metamodel.EntityType<AdvocateMaster> AdvocateMaster = m.entity(AdvocateMaster.class);

        final List<AdvocateMaster> resultList;
        final List<Predicate> predicates = new ArrayList<Predicate>();
        if (advocateMaster.getName() == null && advocateMaster.getMobileNumber() == null
                && advocateMaster.getEmail() == null)
            resultList = findAll();
        else {
            if (advocateMaster.getName() != null) {
                final String name = "%" + advocateMaster.getName().toLowerCase() + "%";
                predicates.add(cb.isNotNull(advocateMasterObj.get("name")));
                predicates
                        .add(cb.like(
                                cb.lower(advocateMasterObj
                                        .get(AdvocateMaster.getDeclaredSingularAttribute("name", String.class))),
                                name));
            }
            if (advocateMaster.getMobileNumber() != null) {
                final String mobileNumber = "%" + advocateMaster.getMobileNumber() + "%";
                predicates.add(cb.isNotNull(advocateMasterObj.get("mobileNumber")));
                predicates.add(cb.like(
                        cb.lower(advocateMasterObj
                                .get(AdvocateMaster.getDeclaredSingularAttribute("mobileNumber", String.class))),
                        mobileNumber));
            }
            if (advocateMaster.getEmail() != null) {
                final String email = "%" + advocateMaster.getEmail() + "%";
                predicates.add(cb.isNotNull(advocateMasterObj.get("email")));
                predicates
                        .add(cb.like(
                                cb.lower(advocateMasterObj
                                        .get(AdvocateMaster.getDeclaredSingularAttribute("email", String.class))),
                                email));
            }

            createQuery.where(predicates.toArray(new Predicate[] {}));
            final TypedQuery<AdvocateMaster> query = entityManager.createQuery(createQuery);

            resultList = query.getResultList();
        }
        return resultList;
    }

    @Override
    public List<EntityType> getAllActiveEntities(final Integer advocateId) {
        final List<EntityType> entities = new ArrayList<>();
        final List<AdvocateMaster> advocateNames = getActiveAdvocateMaster();
        entities.addAll(advocateNames);
        return entities;
    }

    @Override
    public List<? extends EntityType> filterActiveEntities(final String filterKey, final int maxRecords,
            final Integer accountDetailTypeId) {
        return advocateMasterRepository.findByNameLike(filterKey + "%");
    }

    @Override
    public List getAssetCodesForProjectCode(final Integer accountdetailkey)
            throws ValidationException {
        return null;
    }

    @Override
    public List<AdvocateMaster> validateEntityForRTGS(final List<Long> idsList) throws ValidationException {
        return null;

    }

    @Override
    public List<AdvocateMaster> getEntitiesById(final List<Long> idsList) throws ValidationException {
        return null;

    }

}