/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.works.lineestimate.service;

import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.repository.LineEstimateRepository;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineEstimateService {

    @PersistenceContext
    private EntityManager entityManager;

    protected LineEstimateRepository lineEstimateRepository;

    @Autowired
    private LineEstimateNumberGenerator lineEstimateNumberGenerator;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService<EgwStatus, Integer> persistenceService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public LineEstimateService(final LineEstimateRepository lineEstimateRepository) {
        this.lineEstimateRepository = lineEstimateRepository;
    }

    public EgwStatus getStatusByCodeAndModuleType(final String code, final String moduleName) {
        return persistenceService.find("from EgwStatus where moduleType=? and code=?", moduleName, code);
    }

    public LineEstimate getLineEstimateById(final Long id) {
        return lineEstimateRepository.findById(id);
    }

    public LineEstimate getLineEstimateByLineEstimateNumber(final String lineEstimateNumber) {
        return lineEstimateRepository.getLineEstimateByLineEstimateNumber(lineEstimateNumber);
    }

    @Transactional
    public LineEstimate create(final LineEstimate lineEstimate) {
        lineEstimate.setStatus(getStatusByCodeAndModuleType(WorksConstants.WF_STATE_CREATED_LINEESTIMATE,
                WorksConstants.MODULE_NAME_LINEESTIMATE));
        lineEstimate.setLineEstimateDetails(lineEstimate.getLineEstimateDetails().parallelStream().collect(Collectors.toList()));
        for (final LineEstimateDetails lineEstimateDetail : lineEstimate.getLineEstimateDetails())
            lineEstimateDetail.setLineEstimate(lineEstimate);
        if (lineEstimate.getLineEstimateNumber() == null || lineEstimate.getLineEstimateNumber().isEmpty()) {
            final CFinancialYear cFinancialYear = financialYearDAO.getFinancialYearByDate(lineEstimate.getLineEstimateDate());
            final String lineEstimateNumber = lineEstimateNumberGenerator.generateLineEstimateNumber(lineEstimate,
                    cFinancialYear);
            lineEstimate.setLineEstimateNumber(lineEstimateNumber);
        }
        final LineEstimate newLineEstimate = lineEstimateRepository.save(lineEstimate);
        return newLineEstimate;
    }
}
