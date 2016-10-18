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
package org.egov.works.lineestimate.service;

import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.works.autonumber.BudgetAppropriationNumberGenerator;
import org.egov.works.lineestimate.entity.LineEstimateAppropriation;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.repository.LineEstimateAppropriationRepository;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineEstimateAppropriationService {

    private final LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private LineEstimateAppropriationRepository lineEstimateAppropriationRepository;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    public LineEstimateAppropriationService(final LineEstimateDetailsRepository lineEstimateDetailsRepository) {
        this.lineEstimateDetailsRepository = lineEstimateDetailsRepository;
    }

    @Transactional
    public void save(final LineEstimateDetails lineEstimateDetails) {
        lineEstimateDetailsRepository.save(lineEstimateDetails);
    }

    @Transactional
    public void update(final LineEstimateDetails lineEstimateDetails) {
        lineEstimateDetailsRepository.saveAndFlush(lineEstimateDetails);
    }

    @Transactional
    public void delete(final LineEstimateDetails lineEstimateDetails) {
        lineEstimateDetailsRepository.delete(lineEstimateDetails);
    }

    @Transactional
    public void delete(final List<LineEstimateDetails> lineEstimateDetailsList) {
        lineEstimateDetailsRepository.delete(lineEstimateDetailsList);
    }

    public LineEstimateDetails getById(final Long id) {
        return lineEstimateDetailsRepository.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public EgwStatus getStatusByModuleAndCode(final String moduleType, final String code) {
        return egwStatusHibernateDAO.getStatusByModuleAndCode(moduleType, code);
    }

    // @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public LineEstimateAppropriation findLatestByLineEstimateDetails_EstimateNumber(final String estimateNumber) {
        return lineEstimateAppropriationRepository
                .findLatestByLineEstimateDetails_EstimateNumber(estimateNumber);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public String generateBudgetAppropriationNumber(final LineEstimateDetails lineEstimateDetails) {
        final BudgetAppropriationNumberGenerator e = beanResolver
                .getAutoNumberServiceFor(BudgetAppropriationNumberGenerator.class);
        final String budgetAppropriationNumber = e.getNextNumber(lineEstimateDetails);
        return budgetAppropriationNumber;
    }
}