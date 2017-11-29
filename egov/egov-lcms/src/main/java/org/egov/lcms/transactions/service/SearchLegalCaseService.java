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
package org.egov.lcms.transactions.service;

import org.apache.commons.lang.StringUtils;
import org.egov.lcms.reports.entity.LegalCaseSearchResult;
import org.egov.lcms.transactions.entity.ReportStatus;
import org.egov.lcms.transactions.repository.ReportStatusRepository;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchLegalCaseService {

    @Autowired
    private ReportStatusRepository reportStatusRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<LegalCaseSearchResult> getLegalCaseReport(final LegalCaseSearchResult legalCaseSearchResultObj) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select distinct legalObj  as  legalCase ,courtmaster.name  as  courtName ,");
        queryStr.append(" egwStatus.code  as  caseStatus ");
        queryStr.append(" from LegalCase legalObj,CourtMaster courtmaster,CaseTypeMaster casetypemaster,");
        queryStr.append(" PetitionTypeMaster petmaster,EgwStatus egwStatus,ReportStatus reportStatus");
        queryStr.append(" where legalObj.courtMaster.id=courtmaster.id and ");
        queryStr.append(
                " legalObj.caseTypeMaster.id=casetypemaster.id and legalObj.petitionTypeMaster.id=petmaster.id and ");
        queryStr.append(" legalObj.status.id=egwStatus.id and egwStatus.moduletype =:moduleType ");
        if (legalCaseSearchResultObj.getReportStatusId() != null)
            queryStr.append("  and legalObj.reportStatus.id = reportStatus.id ");

        getAppendQuery(legalCaseSearchResultObj, queryStr);
        Query queryResult = getCurrentSession().createQuery(queryStr.toString());
        queryResult = setParametersToQuery(legalCaseSearchResultObj, queryResult);
        final List<LegalCaseSearchResult> legalcaseSearchList = queryResult.list();
        return legalcaseSearchList;

    }

    private Query setParametersToQuery(final LegalCaseSearchResult legalCaseSearchResultObj, final Query queryResult) {
        queryResult.setString("moduleType", LcmsConstants.MODULE_TYPE_LEGALCASE);
        if (StringUtils.isNotBlank(legalCaseSearchResultObj.getLcNumber()))
            queryResult.setString("lcNumber", legalCaseSearchResultObj.getLcNumber());
        if (StringUtils.isNotBlank(legalCaseSearchResultObj.getCaseNumber()))
            queryResult.setString("caseNumber", legalCaseSearchResultObj.getCaseNumber() + "%");
        if (legalCaseSearchResultObj.getCourtId() != null)
            queryResult.setInteger("court", legalCaseSearchResultObj.getCourtId());
        if (legalCaseSearchResultObj.getCasecategory() != null)
            queryResult.setInteger("casetype", legalCaseSearchResultObj.getCasecategory());
        if (legalCaseSearchResultObj.getCourtType() != null)
            queryResult.setInteger("courttype", legalCaseSearchResultObj.getCourtType());
        if (StringUtils.isNotBlank(legalCaseSearchResultObj.getStandingCouncil()))
            queryResult.setString("standingcoouncil", legalCaseSearchResultObj.getStandingCouncil() + "%");
        if (legalCaseSearchResultObj.getStatusId() != null)
            queryResult.setInteger("status", legalCaseSearchResultObj.getStatusId());

        if (legalCaseSearchResultObj.getCaseFromDate() != null)
            queryResult.setDate("fromdate", legalCaseSearchResultObj.getCaseFromDate());
        if (legalCaseSearchResultObj.getCaseToDate() != null)
            queryResult.setDate("toDate", legalCaseSearchResultObj.getCaseToDate());
        if (legalCaseSearchResultObj.getPetitionTypeId() != null)
            queryResult.setInteger("petiontionType", legalCaseSearchResultObj.getPetitionTypeId());
        if (legalCaseSearchResultObj.getReportStatusId() != null)
            queryResult.setInteger("reportStatus", legalCaseSearchResultObj.getReportStatusId());
        if (legalCaseSearchResultObj.getIsStatusExcluded() != null) {
            final List<String> statusCodeList = new ArrayList<String>();
            statusCodeList.add(LcmsConstants.LEGALCASE_STATUS_CLOSED);
            statusCodeList.add(LcmsConstants.LEGALCASE_STATUS_JUDGMENT_IMPLIMENTED);
            queryResult.setParameterList("statusCodeList", statusCodeList);
        }
        queryResult.setResultTransformer(new AliasToBeanResultTransformer(LegalCaseSearchResult.class));
        return queryResult;
    }

    private void getAppendQuery(final LegalCaseSearchResult legalCaseSearchResultOblj, final StringBuilder queryStr) {
        if (StringUtils.isNotBlank(legalCaseSearchResultOblj.getLcNumber()))
            queryStr.append(" and legalObj.lcNumber =:lcNumber");
        if (StringUtils.isNotBlank(legalCaseSearchResultOblj.getCaseNumber()))
            queryStr.append(" and legalObj.caseNumber like :caseNumber ");
        if (legalCaseSearchResultOblj.getCourtId() != null)
            queryStr.append(" and courtmaster.id =:court ");
        if (legalCaseSearchResultOblj.getCasecategory() != null)
            queryStr.append(" and casetypemaster.id =:casetype");
        if (legalCaseSearchResultOblj.getCourtType() != null)
            queryStr.append(" and courtmaster.id =:courttype ");
        if (StringUtils.isNotBlank(legalCaseSearchResultOblj.getStandingCouncil()))
            queryStr.append(" and legalObj.oppPartyAdvocate like :standingcoouncil ");
        if (legalCaseSearchResultOblj.getStatusId() != null)
            queryStr.append(" and egwStatus.id =:status ");
        if (legalCaseSearchResultOblj.getCaseFromDate() != null)
            queryStr.append(" and legalObj.caseDate >=:fromdate ");
        if (legalCaseSearchResultOblj.getCaseToDate() != null)
            queryStr.append(" and legalObj.caseDate <=:toDate ");
        if (legalCaseSearchResultOblj.getPetitionTypeId() != null)
            queryStr.append(" and petmaster.id =:petiontionType ");
        if (legalCaseSearchResultOblj.getIsStatusExcluded() != null)
            queryStr.append(" and egwStatus.code not in (:statusCodeList ) ");
        if (legalCaseSearchResultOblj.getReportStatusId() != null)
            queryStr.append(" and reportStatus.id =:reportStatus ");
    }

    public List<ReportStatus> getReportStatus() {
        final List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        return reportStatusList;
    }

}
