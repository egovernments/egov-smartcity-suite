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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.lcms.reports.entity.DueReportResult;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DueLegalCaseReportService {
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @SuppressWarnings("unchecked")
    public List<DueReportResult> getLegalCaseReport(final DueReportResult dueReportResult, final String reportType) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select  legalObj  as  legalCase ,courtmaster.name  as  courtName ,");
        queryStr.append(" legalObj.status.code  as  caseStatus,position.name as officerInChargeName ");
        if (reportType.equals(LcmsConstants.DUEEMPLOYEEHEARINGREPORT))
            queryStr.append(" ,empHearing.hearingDate  as  hearingDate ");
        if (reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT))
            queryStr.append(" ,judgementImpl.dateOfCompliance as judgementImplDate ");
        queryStr.append(
                " from LegalCase legalObj left join legalObj.officerIncharge position,CourtMaster courtmaster,CaseTypeMaster casetypemaster,");
        queryStr.append(" PetitionTypeMaster petmaster");
        if (reportType.equals(LcmsConstants.DUEEMPLOYEEHEARINGREPORT)
                || reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT))
            queryStr.append(" ,EgwStatus egwStatus ");
        if (reportType.equals(LcmsConstants.DUEEMPLOYEEHEARINGREPORT))
            queryStr.append(" ,Hearings empHearing ");
        if (reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT))
            queryStr.append(" ,Judgment judgement,JudgmentImpl judgementImpl ");
        if (reportType.equals(LcmsConstants.DUECAREPORT) || reportType.equals(LcmsConstants.DUEPWRREPORT))
            queryStr.append(" ,Pwr egPwr ");
        queryStr.append(" where legalObj.courtMaster.id=courtmaster.id and ");
        queryStr.append(
                " legalObj.caseTypeMaster.id=casetypemaster.id and legalObj.petitionTypeMaster.id=petmaster.id  ");

        if (reportType.equals(LcmsConstants.DUECAREPORT) || reportType.equals(LcmsConstants.DUEPWRREPORT))
            queryStr.append("  and egPwr.legalCase.id=legalObj.id ");
        if (reportType.equals(LcmsConstants.DUEPWRREPORT))
            queryStr.append(" and egPwr.pwrDueDate is null ");

        if (reportType.equals(LcmsConstants.DUECAREPORT))
            queryStr.append(" and egPwr.caDueDate is NOT NULL ");
        if (reportType.equals(LcmsConstants.DUEEMPLOYEEHEARINGREPORT)
                || reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT)) {
            queryStr.append(" and legalObj.status.id=egwStatus.id and egwStatus.moduletype =:moduleType ");
            queryStr.append(" and egwStatus.code in (:statusCodeList ) ");
        }
        if (reportType.equals(LcmsConstants.DUEEMPLOYEEHEARINGREPORT)) {
            queryStr.append(" and empHearing.legalCase.id=legalObj.id  ");
            if (dueReportResult.getCaseFromDate() != null)
                queryStr.append(" and empHearing.hearingDate >=:fromdate ");
            if (dueReportResult.getCaseToDate() != null)
                queryStr.append(" and empHearing.hearingDate <=:toDate ");
        }
        if (reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT)) {
            queryStr.append(" and judgement.legalCase.id=legalObj.id  ");
            queryStr.append(" and judgement.id=judgementImpl.judgment.id  ");
            if (dueReportResult.getCaseFromDate() != null)
                queryStr.append(" and judgementImpl.dateOfCompliance >=:fromdate ");
            if (dueReportResult.getCaseToDate() != null)
                queryStr.append(" and judgementImpl.dateOfCompliance <=:toDate ");

        }

        if (dueReportResult.getOfficerIncharge() != null)
            queryStr.append(" and position.id =:officerIncharge ");

        if (!(reportType.equals(LcmsConstants.DUEEMPLOYEEHEARINGREPORT)
                || reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT)))
            getAppendQuery(dueReportResult, queryStr);

        Query queryResult = getCurrentSession().createQuery(queryStr.toString());
        queryResult = setParametersToQuery(dueReportResult, queryResult, reportType);
        final List<DueReportResult> legalcaseSearchList = queryResult.list();
        return legalcaseSearchList;

    }

    private Query setParametersToQuery(final DueReportResult dueReportResult, final Query queryResult,
            final String reportType) {
        final List<String> statusCodeList = new ArrayList<String>();
        if (reportType.equals(LcmsConstants.DUEEMPLOYEEHEARINGREPORT)
                || reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT))
            queryResult.setString("moduleType", LcmsConstants.MODULE_TYPE_LEGALCASE);
        if (reportType.equals(LcmsConstants.DUEEMPLOYEEHEARINGREPORT)) {
            statusCodeList.add(LcmsConstants.LEGALCASE_STATUS_CREATED);
            statusCodeList.add(LcmsConstants.LEGALCASE_HEARING_STATUS);
            statusCodeList.add(LcmsConstants.LEGALCASE_INTERIMSTAY_STATUS);
            queryResult.setParameterList("statusCodeList", statusCodeList);
        }
        if (reportType.equals(LcmsConstants.DUEJUDGEMENTIMPLPREPORT)) {
            statusCodeList.add(LcmsConstants.LEGALCASE_STATUS_JUDGMENT_IMPLIMENTED);
            queryResult.setParameterList("statusCodeList", statusCodeList);
        }
        if (dueReportResult.getOfficerIncharge() != null)
            queryResult.setLong("officerIncharge", dueReportResult.getOfficerIncharge());

        if (dueReportResult.getCaseFromDate() != null)
            queryResult.setDate("fromdate", dueReportResult.getCaseFromDate());
        if (dueReportResult.getCaseToDate() != null)
            queryResult.setDate("toDate", dueReportResult.getCaseToDate());

        queryResult.setResultTransformer(new AliasToBeanResultTransformer(DueReportResult.class));
        return queryResult;
    }

    private void getAppendQuery(final DueReportResult dueReportResult, final StringBuilder queryStr) {

        if (dueReportResult.getCaseFromDate() != null)
            queryStr.append(" and legalObj.caseDate >=:fromdate ");
        if (dueReportResult.getCaseToDate() != null)
            queryStr.append(" and legalObj.caseDate <=:toDate ");
    }

}
