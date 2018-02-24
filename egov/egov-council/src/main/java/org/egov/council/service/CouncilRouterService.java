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

package org.egov.council.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilPreamble;
import org.egov.council.entity.CouncilRouter;
import org.egov.council.repository.CouncilRouterRepository;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional(readOnly = true)
public class CouncilRouterService {

    private final CouncilRouterRepository councilRouterRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CouncilRouterService(final CouncilRouterRepository councilRouterRepository) {
        this.councilRouterRepository = councilRouterRepository;
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public CouncilRouter create(final CouncilRouter councilRouter) {
        return councilRouterRepository.save(councilRouter);
    }

    @Transactional
    public CouncilRouter update(final CouncilRouter councilRouter) {
        return councilRouterRepository.save(councilRouter);
    }

    public CouncilRouter findById(final Long id) {
        return councilRouterRepository.findById(id);
    }

    public List<CouncilRouter> search(CouncilRouter councilRouter) {
        final Criteria criteria = getCurrentSession().createCriteria(
                CouncilRouter.class);

        if (councilRouter.getDepartment() != null)
            criteria.add(Restrictions.eq("department", councilRouter.getDepartment()));

        if (councilRouter.getPosition() != null)
            criteria.add(Restrictions.eq("position", councilRouter.getPosition()));

        if (councilRouter.getType() != null)
            criteria.add(Restrictions.eq("type", councilRouter.getType()));

        return criteria.list();
    }

    
    public Position getCouncilAssignee(CouncilPreamble councilPreamble) {
        CouncilRouter councilRouter = getCouncilRouter(councilPreamble);
        if (councilRouter == null)
            throw new ApplicationRuntimeException(councilPreamble.getReferenceNumber()+","+
             "No clerk is configured to receive preamble");
        else
            return councilRouter.getPosition();
    }
    
    public CouncilRouter getCouncilRouter(CouncilPreamble councilPreamble) {
        CouncilRouter councilRouter;
        councilRouter = councilRouterRepository
                    .findByTypeAndDepartment(councilPreamble.getTypeOfPreamble(), councilPreamble.getDepartment());
            if (councilRouter == null) {               
                councilRouter = councilRouterRepository.findByType(councilPreamble.getTypeOfPreamble());               
            }
        
        return councilRouter;
    }
    
    public void validateCouncilRouter(CouncilRouter councilRouter, BindingResult errors) {
        CouncilRouter router = councilRouterRepository.findByAllParams(councilRouter.getDepartment().getId(),
                councilRouter.getType(), councilRouter.getPosition().getId());
        if (router != null && (councilRouter.getId() == null || councilRouter.getId() != router.getId())) {
            errors.reject("Duplicate entry", "Council Router is already exist");
        }

    }
}