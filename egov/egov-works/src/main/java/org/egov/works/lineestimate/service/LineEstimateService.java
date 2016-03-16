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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.LineEstimateSearchRequest;
import org.egov.works.lineestimate.entity.LineEstimateSearchResult;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.lineestimate.repository.LineEstimateRepository;
import org.egov.works.models.estimate.EstimateNumberGenerator;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private WorksUtils worksUtils;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public LineEstimateService(final LineEstimateRepository lineEstimateRepository,
            final LineEstimateDetailsRepository lineEstimateDetailsRepository) {
        this.lineEstimateRepository = lineEstimateRepository;
        this.lineEstimateDetailsRepository = lineEstimateDetailsRepository;
    }

    public LineEstimate getLineEstimateById(final Long id) {
        return lineEstimateRepository.findById(id);
    }

    @Transactional
    public LineEstimate create(final LineEstimate lineEstimate, final MultipartFile[] files) throws IOException {
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

        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, newLineEstimate,
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        if (!documentDetails.isEmpty()) {
            lineEstimate.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return newLineEstimate;
    }

    @Transactional
    public LineEstimate update(final LineEstimate lineEstimate, final String removedLineEstimateDetailsIds,
            final MultipartFile[] files) throws IOException {
        final CFinancialYear cFinancialYear = financialYearDAO.getFinancialYearByDate(lineEstimate.getLineEstimateDate());
        for (final LineEstimateDetails lineEstimateDetails : lineEstimate.getLineEstimateDetails()) {
            if (lineEstimateDetails.getLineEstimate() == null) {
                lineEstimateDetails.setLineEstimate(lineEstimate);
                lineEstimateDetails.setEstimateNumber(estimateNumberGenerator
                        .generateEstimateNumber(lineEstimate, cFinancialYear));
            }
        }
        List<LineEstimateDetails> list = new ArrayList<LineEstimateDetails>(lineEstimate.getLineEstimateDetails());
        list = removeDeletedLineEstimateDetails(list, removedLineEstimateDetailsIds);
        for (LineEstimateDetails details : list) {
            details.setId(null);
        }
        lineEstimate.getLineEstimateDetails().clear();
        // TODO: use save instead of saveAndFlush
        final LineEstimate persistedLineEstimate = lineEstimateRepository.saveAndFlush(lineEstimate);

        persistedLineEstimate.setLineEstimateDetails(list);
        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, persistedLineEstimate,
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        if (!documentDetails.isEmpty()) {
            lineEstimate.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        // TODO: use save instead of saveAndFlush
        return lineEstimateRepository.saveAndFlush(persistedLineEstimate);
    }

    public LineEstimate getLineEstimateByLineEstimateNumber(final String lineEstimateNumber) {
        return lineEstimateRepository.findByLineEstimateNumber(lineEstimateNumber);
    }

    @Transactional
    public List<LineEstimateDetails> removeDeletedLineEstimateDetails(final List<LineEstimateDetails> list,
            final String removedLineEstimateDetailsIds) {
        List<LineEstimateDetails> details = new ArrayList<LineEstimateDetails>();
        if (null != removedLineEstimateDetailsIds) {
            String[] ids = removedLineEstimateDetailsIds.split(",");
            List<String> strList = new ArrayList<String>();
            for (String str : ids) {
                strList.add(str);
            }
            for (LineEstimateDetails line : list) {
                if (line.getId() != null) {
                    if (!strList.contains(line.getId().toString()))
                        details.add(line);
                }
                else
                    details.add(line);
            }
        }
        else {
            return list;
        }
        return details;
    }

    public List<LineEstimate> searchLineEstimates(LineEstimateSearchRequest lineEstimateSearchRequest) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(LineEstimate.class)
                .createAlias("lineEstimateDetails", "lineEstimateDetail");
        if (lineEstimateSearchRequest != null) {
            if (lineEstimateSearchRequest.getAdminSanctionNumber() != null) {
                criteria.add(Restrictions.eq("adminSanctionNumber", lineEstimateSearchRequest.getAdminSanctionNumber()));
            }
            if (lineEstimateSearchRequest.getBudgetHead() != null) {
                criteria.add(Restrictions.eq("budgetHead.id", lineEstimateSearchRequest.getBudgetHead()));
            }
            if (lineEstimateSearchRequest.getExecutingDepartment() != null) {
                criteria.add(Restrictions.eq("executingDepartment.id", lineEstimateSearchRequest.getExecutingDepartment()));
            }
            if (lineEstimateSearchRequest.getFunction() != null) {
                criteria.add(Restrictions.eq("function.id", lineEstimateSearchRequest.getFunction()));
            }
            if (lineEstimateSearchRequest.getFund() != null) {
                criteria.add(Restrictions.eq("fund.id", lineEstimateSearchRequest.getFund().intValue()));
            }
            if (lineEstimateSearchRequest.getEstimateNumber() != null) {
                criteria.add(Restrictions.eq("lineEstimateDetail.estimateNumber", lineEstimateSearchRequest.getEstimateNumber()));
            }
            if (lineEstimateSearchRequest.getAdminSanctionFromDate() != null) {
                criteria.add(Restrictions.ge("adminSanctionDate", lineEstimateSearchRequest.getAdminSanctionFromDate()));
            }
            if (lineEstimateSearchRequest.getAdminSanctionToDate() != null) {
                criteria.add(Restrictions.le("adminSanctionDate", lineEstimateSearchRequest.getAdminSanctionToDate()));
            }
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> findLineEstimateNumbers(String name) {
        List<LineEstimateDetails> lineEstimateDetails = lineEstimateDetailsRepository
                .findByEstimateNumberContainingIgnoreCase(name);
        List<String> results = new ArrayList<String>();
        for (LineEstimateDetails details : lineEstimateDetails) {
            results.add(details.getEstimateNumber());
        }
        return results;
    }

    public List<String> findAdminSanctionNumbers(String name) {
        List<LineEstimate> lineEstimates = lineEstimateRepository.findByAdminSanctionNumberContainingIgnoreCase(name);
        List<String> results = new ArrayList<String>();
        for (LineEstimate estimate : lineEstimates) {
            results.add(estimate.getAdminSanctionNumber());
        }
        return results;
    }

    public List<LineEstimateSearchResult> searchLineEstimatesForLOA(LineEstimateSearchRequest lineEstimateSearchRequest) {
        List<LineEstimate> lineEstimates = searchLineEstimates(lineEstimateSearchRequest);
        List<LineEstimateSearchResult> lineEstimateSearchResults = new ArrayList<LineEstimateSearchResult>();
        for (LineEstimate le : lineEstimates) {
            for (LineEstimateDetails led : le.getLineEstimateDetails()) {
                if (lineEstimateSearchRequest.getEstimateNumber() != null) {
                    if (led.getEstimateNumber().equalsIgnoreCase(lineEstimateSearchRequest.getEstimateNumber())) {
                        LineEstimateSearchResult result = new LineEstimateSearchResult();
                        result.setId(le.getId());
                        result.setAdminSanctionNumber(le.getAdminSanctionNumber());
                        result.setCreatedBy(le.getCreatedBy().getName());
                        result.setEstimateAmount(led.getEstimateAmount());
                        result.setEstimateNumber(led.getEstimateNumber());
                        result.setNameOfWork(led.getNameOfWork());
                        if(le.getAdminSanctionBy() != null)
                            result.setApprovedBy(le.getAdminSanctionBy().getName());
                        lineEstimateSearchResults.add(result);
                    }
                }
                else {
                    LineEstimateSearchResult result = new LineEstimateSearchResult();
                    result.setId(le.getId());
                    result.setAdminSanctionNumber(le.getAdminSanctionNumber());
                    result.setCreatedBy(le.getCreatedBy().getName());
                    result.setEstimateAmount(led.getEstimateAmount());
                    result.setEstimateNumber(led.getEstimateNumber());
                    result.setNameOfWork(led.getNameOfWork());
                    if(le.getAdminSanctionBy() != null)
                        result.setApprovedBy(le.getAdminSanctionBy().getName());
                    lineEstimateSearchResults.add(result);
                }
            }
        }
        return lineEstimateSearchResults;
    }
    
    public LineEstimateDetails findByEstimateNumber(final String estimateNumber) {
        return lineEstimateDetailsRepository.findByEstimateNumber(estimateNumber);
    }
}