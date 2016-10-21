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
package org.egov.lcms.transactions.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.egov.lcms.reports.entity.GenericSubReportResult;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GenericSubReportService {

    @Autowired
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<GenericSubReportResult> getGenericSubReport(final GenericSubReportResult genericSubReportResult) {

        final StringBuilder queryStr = new StringBuilder();
        if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.COURTNAME))
            getAggregateQueryByCourtName(genericSubReportResult, queryStr);
        if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.COURTTYPE))
            getAggregateQueryByCourtType(genericSubReportResult, queryStr);
        if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.PETITIONTYPE))
            getAggregateQueryByPetitionType(genericSubReportResult, queryStr);
        if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.CASESTATUS))
            getAggregateQueryByCaseStatus(genericSubReportResult, queryStr);
        if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.OFFICERINCHRGE))
            getAggregateQueryByOfficerIncharge(genericSubReportResult, queryStr);
        if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.JUDGEMENTOUTCOME))
            getAggregateQueryByJudgementOutcome(genericSubReportResult, queryStr);

        Query queryResult = getCurrentSession().createQuery(queryStr.toString());
        queryResult = setParameterToQuery(genericSubReportResult, queryResult);
        final List<GenericSubReportResult> genericSubReportResultList = queryResult.list();
        return genericSubReportResultList;

    }

    private Query setParameterToQuery(final GenericSubReportResult genericSubReportResultObj, final Query queryResult) {
        if (genericSubReportResultObj.getAggregatedBy().equals(LcmsConstants.CASESTATUS))
            queryResult.setString("moduleType", LcmsConstants.MODULE_TYPE_LEGALCASE);

        if (genericSubReportResultObj.getFromDate() != null)
            queryResult.setDate("fromDate", genericSubReportResultObj.getFromDate());
        if (genericSubReportResultObj.getToDate() != null)
            queryResult.setDate("toDate", genericSubReportResultObj.getToDate());
        if (genericSubReportResultObj.getCourtName() != null)
            queryResult.setInteger("courtName", genericSubReportResultObj.getCourtName());
        if (genericSubReportResultObj.getCourtType() != null)
            queryResult.setString("courtType", genericSubReportResultObj.getCourtType());
        if (genericSubReportResultObj.getPetitionType() != null)
            queryResult.setString("petitionType", genericSubReportResultObj.getPetitionType());
        if (genericSubReportResultObj.getCaseStatus() != null)
            queryResult.setInteger("caseStatus", genericSubReportResultObj.getCaseStatus());
        if (genericSubReportResultObj.getOfficerIncharge() != null)
            queryResult.setString("officerIncharge", genericSubReportResultObj.getOfficerIncharge());
        if (genericSubReportResultObj.getJudgmentType() != null)
            queryResult.setString("judgmentType", genericSubReportResultObj.getJudgmentType());
        if (genericSubReportResultObj.getCaseCategory() != null)
            queryResult.setInteger("casetype", genericSubReportResultObj.getCaseCategory());
        if (StringUtils.isNotBlank(genericSubReportResultObj.getStandingCounsel()))
            queryResult.setString("standingCouncil", genericSubReportResultObj.getStandingCounsel());
        if (genericSubReportResultObj.getOfficerIncharge() != null)
            queryResult.setString("officerIncharge", genericSubReportResultObj.getOfficerIncharge());

        queryResult.setResultTransformer(new AliasToBeanResultTransformer(GenericSubReportResult.class));
        return queryResult;
    }

    private void getAppendQuery(final GenericSubReportResult genericSubReportResult, final StringBuilder queryStr) {

        if (genericSubReportResult.getFromDate() != null)
            queryStr.append(" legalcase.caseDate >=:fromDate  and");
        if (genericSubReportResult.getToDate() != null)
            queryStr.append(" legalcase.caseReceivingDate <=:toDate ");
        if (genericSubReportResult.getCourtName() != null)
            queryStr.append(" and legalcase.courtMaster =:courtName ");
        if (genericSubReportResult.getCaseCategory() != null)
            queryStr.append(" and legalcase.caseTypeMaster =:casetype ");
        if (StringUtils.isNotBlank(genericSubReportResult.getStandingCounsel()))
            queryStr.append(" and legalcase.oppPartyAdvocate like :standingCouncil ");
        if (genericSubReportResult.getOfficerIncharge() != null)
            queryStr.append(" and legalcase.officerIncharge like :officerIncharge ");
        if (genericSubReportResult.getCaseStatus() != null)
            queryStr.append(" and legalcase.status  =:caseStatus ");
        if (genericSubReportResult.getCourtType() != null)
            queryStr.append(" and courtmaster.courtType.courtType like :courtType ");
        if (genericSubReportResult.getJudgmentType() != null)
            queryStr.append(" and judgment.judgmentType.name like :judgmentType ");
        if (genericSubReportResult.getPetitionType() != null)
            queryStr.append(" and legalcase.petitionTypeMaster =:petitionType ");
    }

    private void getAggregateQueryByCourtName(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr) {
        queryStr.append("SELECT COUNT(DISTINCT legalcase.id) as noOfCase,courtmaster.name  as aggregatedBy ");
        queryStr.append(
                "from LegalCase legalcase,CourtMaster courtmaster where legalcase.courtMaster.id=courtmaster.id and ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by courtmaster.name");
        queryStr.append(" order by courtmaster.name");
    }

    private void getAggregateQueryByPetitionType(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr) {
        queryStr.append("SELECT COUNT(DISTINCT legalcase.id) as noOfCase,petmaster.petitionType as aggregatedBy ");
        queryStr.append("from LegalCase legalcase,PetitionTypeMaster petmaster where legalcase.petitionTypeMaster.id=petmaster.id and ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by petmaster.petitionType");
        queryStr.append(" order by petmaster.petitionType");
    }

    private void getAggregateQueryByCourtType(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr) {
        queryStr.append(
                "SELECT COUNT(DISTINCT legalcase.id) as noOfCase,courtmaster.courtType.courtType  as aggregatedBy ");
        queryStr.append(
                "from LegalCase legalcase,CourtMaster courtmaster where legalcase.courtMaster.id=courtmaster.id and ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by courtmaster.courtType.courtType");
        queryStr.append(" order by courtmaster.courtType.courtType");
    }

    private void getAggregateQueryByCaseStatus(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr) {
        queryStr.append("SELECT COUNT(DISTINCT legalcase.id) as noOfCase,egwStatus.description  as aggregatedBy ");
        queryStr.append(
                "from LegalCase legalcase,EgwStatus egwStatus where legalcase.status.id=egwStatus.id and egwStatus.moduletype =:moduleType and ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by egwStatus.description");
        queryStr.append(" order by egwStatus.description");
    }

    private void getAggregateQueryByOfficerIncharge(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr) {
        queryStr.append("SELECT COUNT(DISTINCT legalcase.id) as noOfCase,legalcase.officerIncharge as aggregatedBy ");
        queryStr.append(
                "from LegalCase legalcase where ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by legalcase.officerIncharge");
        queryStr.append(" order by legalcase.officerIncharge");
    }

    private void getAggregateQueryByJudgementOutcome(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr) {
        queryStr.append("SELECT COUNT(DISTINCT legalcase.id) as noOfCase,judgment.judgmentType.name  as aggregatedBy ");
        queryStr.append(
                "from LegalCase legalcase,Judgment judgment  where legalcase.id = judgment.legalCase and ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by judgment.judgmentType.name");
        queryStr.append(" order by judgment.judgmentType.name");
    }
}