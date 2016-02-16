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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.lineestimate.repository.LineEstimateRepository;
import org.egov.works.models.estimate.EstimateNumberGenerator;
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

    private final LineEstimateRepository lineEstimateRepository;
    
    private final LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private LineEstimateNumberGenerator lineEstimateNumberGenerator;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService<EgwStatus, Integer> persistenceService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private EstimateNumberGenerator estimateNumberGenerator;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public LineEstimateService(final LineEstimateRepository lineEstimateRepository, final LineEstimateDetailsRepository lineEstimateDetailsRepository) {
        this.lineEstimateRepository = lineEstimateRepository;
        this.lineEstimateDetailsRepository = lineEstimateDetailsRepository;
    }

    public LineEstimate getLineEstimateById(final Long id) {
        return lineEstimateRepository.findById(id);
    }

    @Transactional
    public LineEstimate create(final LineEstimate lineEstimate) {
        lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULE_NAME_LINEESTIMATE,
                WorksConstants.WF_STATE_CREATED_LINEESTIMATE));
        final CFinancialYear cFinancialYear = financialYearDAO.getFinancialYearByDate(lineEstimate.getLineEstimateDate());
        for (final LineEstimateDetails lineEstimateDetail : lineEstimate.getLineEstimateDetails()) {
            final String estimateNumber = estimateNumberGenerator.generateEstimateNumber(lineEstimate, cFinancialYear);
            lineEstimateDetail.setEstimateNumber(estimateNumber);
            lineEstimateDetail.setLineEstimate(lineEstimate);
        }
        if (lineEstimate.getLineEstimateNumber() == null || lineEstimate.getLineEstimateNumber().isEmpty()) {
            final String lineEstimateNumber = lineEstimateNumberGenerator.generateLineEstimateNumber(lineEstimate,
                    cFinancialYear);
            lineEstimate.setLineEstimateNumber(lineEstimateNumber);
        }
        final LineEstimate newLineEstimate = lineEstimateRepository.save(lineEstimate);
        return newLineEstimate;
    }

    @Transactional
    public LineEstimate update(final LineEstimate lineEstimate, final String removedLineEstimateDetailsIds) {
        final List<LineEstimateDetails> list = new ArrayList<LineEstimateDetails>(getLineEstimateDetials(lineEstimate, removedLineEstimateDetailsIds));
        final LineEstimate lineEstimate1 = removeDeletedLineEstimateDetails(lineEstimate, removedLineEstimateDetailsIds);
        if (null != removedLineEstimateDetailsIds) {
            lineEstimate1.getLineEstimateDetails().addAll(list);
        }
        final CFinancialYear cFinancialYear = financialYearDAO.getFinancialYearByDate(lineEstimate.getLineEstimateDate());
        for (final LineEstimateDetails lineEstimateDetails : lineEstimate1.getLineEstimateDetails()) {
            if (lineEstimateDetails.getLineEstimate() == null) {
                lineEstimateDetails.setLineEstimate(lineEstimate1);
                lineEstimateDetails.setEstimateNumber(estimateNumberGenerator.generateEstimateNumber(lineEstimate1, cFinancialYear));
            }
        }
        return lineEstimateRepository.saveAndFlush(lineEstimate1);
    }

    public LineEstimate getLineEstimateByLineEstimateNumber(final String lineEstimateNumber) {
        return lineEstimateRepository.findByLineEstimateNumber(lineEstimateNumber);
    }
    
    private List<LineEstimateDetails> getLineEstimateDetials(final LineEstimate lineEstimate, final String removedLineEstimateDetailsIds) {
        List<LineEstimateDetails> lineEstimateDetailsList = new ArrayList<LineEstimateDetails>();
        for(LineEstimateDetails lineEstimateDetials : lineEstimate.getLineEstimateDetails()) {
            LineEstimateDetails lineEstimateDetial = new LineEstimateDetails();
            if(lineEstimateDetials.getId() == null) {
                lineEstimateDetial.setId(lineEstimateDetials.getId());
                lineEstimateDetial.setEstimateAmount(lineEstimateDetials.getEstimateAmount());
                lineEstimateDetial.setNameOfWork(lineEstimateDetials.getNameOfWork());
                lineEstimateDetial.setEstimateNumber(lineEstimateDetials.getEstimateNumber());
                lineEstimateDetailsList.add(lineEstimateDetial);
            }
        }
        return lineEstimateDetailsList;
    }
    
    @Transactional
    public LineEstimate removeDeletedLineEstimateDetails(final LineEstimate lineEstimate,
            final String removedLineEstimateDetailsIds) {
        if (null != removedLineEstimateDetailsIds)
            for (final String id : removedLineEstimateDetailsIds.split(","))
                lineEstimate.getLineEstimateDetails().remove(lineEstimateDetailsRepository.findOne(Long.valueOf(id)));

        return lineEstimate;
    }
}
