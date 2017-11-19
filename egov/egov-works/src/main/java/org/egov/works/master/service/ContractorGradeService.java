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

package org.egov.works.master.service;

import org.egov.commons.ContractorGrade;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.utils.WorksConstants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContractorGradeService extends PersistenceService<ContractorGrade, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    public ContractorGradeService() {
        super(ContractorGrade.class);
    }

    public ContractorGradeService(final Class<ContractorGrade> type) {
        super(type);
    }

    public ContractorGrade getContractorGradeById(final Long contractorGradeId) {
        final ContractorGrade contractorGrade = entityManager.find(ContractorGrade.class, contractorGradeId);
        return contractorGrade;
    }

    public List<ContractorGrade> getAllContractorGrades() {
        final Query query = entityManager.createQuery("from ContractorGrade order by upper(grade)");
        final List<ContractorGrade> contractorGradeList = query.getResultList();
        return contractorGradeList;
    }

    public SearchQuery prepareSearchQuery(final Map<String, Object> criteriaMap) {
        final StringBuffer contractorGradeSql = new StringBuffer(100);
        String contractorGradeStr = "";
        final List<Object> paramList = new ArrayList<Object>();
        contractorGradeSql.append(" from ContractorGrade cg");
        final String grade = (String) criteriaMap.get(WorksConstants.GRADE);
        final Double minAmount = (Double) criteriaMap.get(WorksConstants.MIN_AMOUNT);
        final Double maxAmount = (Double) criteriaMap.get(WorksConstants.MAX_AMOUNT);
        if (grade != null && !grade.trim().equals("") || minAmount != -1 || maxAmount != -1)
            contractorGradeSql.append(" where 1=1");

        if (grade != null && !grade.trim().equals("")) {
            contractorGradeSql.append(" and UPPER(cg.grade) like ?");
            paramList.add("%" + grade.trim().toUpperCase() + "%");
        }

        if (minAmount != -1) {
            contractorGradeSql.append(" and cg.minAmount = ?");
            paramList.add(BigDecimal.valueOf(minAmount));
        }

        if (maxAmount != -1) {
            contractorGradeSql.append(" and cg.maxAmount = ?");
            paramList.add(BigDecimal.valueOf(maxAmount));
        }
        contractorGradeSql.append(" group by cg.id");
        contractorGradeStr = contractorGradeSql.toString();
        final String countQuery = "select count(*) " + contractorGradeStr;
        return new SearchQueryHQL(contractorGradeStr, countQuery, paramList);
    }

    public ContractorGrade findByContractorClass(final String contractorClass) {
        final String query = "from ContractorGrade as cg where upper(cg.grade) = '" + contractorClass.toUpperCase() + "'";
        return find(query);
    }

}
