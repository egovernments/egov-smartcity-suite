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
package org.egov.portal.firm.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.portal.entity.Firm;
import org.egov.portal.entity.FirmUser;
import org.egov.portal.entity.SearchRequestFirm;
import org.egov.portal.firm.repository.FirmRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FirmService {

    @Autowired
    private FirmRepository firmRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserService userService;

    @Transactional
    public Firm create(final Firm firm) {
        return firmRepository.save(firm);
    }

    public Firm getFirmById(final Long firmId) {
        return firmRepository.findOne(firmId);
    }
    
    public Firm getFirmByPan(final String pan) {
        return firmRepository.findByPan(pan);
    }
    
    @Transactional
    public void createFirm(final Firm firm) {
        FirmUser firmUsers = null;
        firm.getFirmUsers().clear();
        for (final FirmUser firmUser : firm.getTempFirmUsers()) {
            User user;
            firmUsers = createFirmUserObject();
            user = userService.getUserByUsername(firmUser.getEmailId());
            if(user == null) {
                user = createUserObject();
                user.setUsername(firmUser.getEmailId());
                user.setName(firmUser.getName());
                user.setMobileNumber(firmUser.getMobileNumber());
                user.setPassword(passwordEncoder.encode(firmUser.getMobileNumber()));
                user.setEmailId(firmUser.getEmailId());
                user.setType(UserType.BUSINESS);
                user.setActive(true);
                user.setPwdExpiryDate(DateUtils.addMonths(getNewDate(), 6));
                
                user=userService.createUser(user);
            }
            firmUsers.setUser(user);
           
            firmUsers.setMobileNumber(firmUser.getMobileNumber());
            firmUsers.setEmailId(firmUser.getEmailId());
            firmUsers.setName(firmUser.getName());
            firmUsers.setFirm(firm);
            firm.getFirmUsers().add(firmUsers);
        }
        firmRepository.save(firm);
    }

    private Date getNewDate() {
        return new Date();
    }

    public static Date addDays(Date d, int days)
    {
        d.setTime(d.getTime() + days * 1000 * 60 * 60 * 24);
        return d;
    }

    private FirmUser createFirmUserObject() {
        return new FirmUser();
    }

    private User createUserObject() {
        return new User();
    }

    public List<Firm> searchFirm(final SearchRequestFirm searchRequestFirm) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Firm.class)
                .addOrder(Order.asc("createdDate"));
        if (searchRequestFirm != null) {
            if (searchRequestFirm.getFirmName() != null)
                criteria.add(
                        Restrictions.ilike("firmName", searchRequestFirm.getFirmName(), MatchMode.ANYWHERE));
            if (searchRequestFirm.getPan() != null)
                criteria.add(Restrictions.eq("pan", searchRequestFirm.getPan()).ignoreCase());
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
    
}