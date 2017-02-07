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

    public List<GenericSubReportResult> getGenericSubReport(final GenericSubReportResult genericSubReportResult,
            final Boolean clickOnCount) {

        final StringBuilder queryStr = new StringBuilder();
        if (genericSubReportResult.getAggregatedBy() != null) {

            if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.COURTNAME))
                getAggregateQueryByCourtName(genericSubReportResult, queryStr, clickOnCount);
            if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.COURTTYPE))
                getAggregateQueryByCourtType(genericSubReportResult, queryStr, clickOnCount);
            if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.PETITIONTYPE))
                getAggregateQueryByPetitionType(genericSubReportResult, queryStr, clickOnCount);
            if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.CASESTATUS))
                getAggregateQueryByCaseStatus(genericSubReportResult, queryStr, clickOnCount);
            if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.OFFICERINCHRGE))
                getAggregateQueryByOfficerIncharge(genericSubReportResult, queryStr, clickOnCount);
            if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.JUDGEMENTOUTCOME))
                getAggregateQueryByJudgementOutcome(genericSubReportResult, queryStr, clickOnCount);
            if (genericSubReportResult.getAggregatedBy().equals(LcmsConstants.CASECATEGORY))
                getAggregateQueryByCaseCategory(genericSubReportResult, queryStr, clickOnCount);

        } else {
            getLegalCaseSubReportStatus(genericSubReportResult, queryStr, clickOnCount);
            getAppendQuery(genericSubReportResult, queryStr);
        }

        Query queryResult = getCurrentSession().createQuery(queryStr.toString());
        queryResult = setParameterToQuery(genericSubReportResult, queryResult, clickOnCount);
        final List<GenericSubReportResult> genericSubReportResultList = queryResult.list();
        return genericSubReportResultList;

    }

    private Query setParameterToQuery(final GenericSubReportResult genericSubReportResultObj, final Query queryResult,
            final Boolean clickOnCount) {

        if (clickOnCount)
            queryResult.setString("aggreagatedByValue", genericSubReportResultObj.getAggregatedByValue());

        queryResult.setString("moduleType", LcmsConstants.MODULE_TYPE_LEGALCASE);

        if (genericSubReportResultObj.getFromDate() != null)
            queryResult.setDate("fromDate", genericSubReportResultObj.getFromDate());
        if (genericSubReportResultObj.getToDate() != null)
            queryResult.setDate("toDate", genericSubReportResultObj.getToDate());
        if (genericSubReportResultObj.getCourtId() != null)
            queryResult.setInteger("courtName", genericSubReportResultObj.getCourtId());
        if (genericSubReportResultObj.getCourtType() != null)
            queryResult.setInteger("courtType", genericSubReportResultObj.getCourtType());
        if (genericSubReportResultObj.getPetitionTypeId() != null)
            queryResult.setInteger("petitionType", genericSubReportResultObj.getPetitionTypeId());
        if (genericSubReportResultObj.getCaseStatus() != null)
            queryResult.setString("status", genericSubReportResultObj.getCaseStatus());
        if (genericSubReportResultObj.getOfficerIncharge() != null)
            queryResult.setLong("officerIncharge", genericSubReportResultObj.getOfficerIncharge());
        if (genericSubReportResultObj.getJudgmentTypeId() != null)
            queryResult.setInteger("judgmentType", genericSubReportResultObj.getJudgmentTypeId());
        if (StringUtils.isNotBlank(genericSubReportResultObj.getStandingCounsel()))
            queryResult.setString("standingCouncil", genericSubReportResultObj.getStandingCounsel());
        if (genericSubReportResultObj.getOfficerIncharge() != null)
            queryResult.setLong("officerIncharge", genericSubReportResultObj.getOfficerIncharge());
        if (genericSubReportResultObj.getCasecategoryId() != null)
            queryResult.setInteger("caseType", genericSubReportResultObj.getCasecategoryId());
        if (genericSubReportResultObj.getReportStatusId() != null)
            queryResult.setInteger("casesubStatus", genericSubReportResultObj.getReportStatusId());

        queryResult.setResultTransformer(new AliasToBeanResultTransformer(GenericSubReportResult.class));
        return queryResult;
    }

    private void getAppendQuery(final GenericSubReportResult genericSubReportResult, final StringBuilder queryStr) {

        if (genericSubReportResult.getFromDate() != null)
            queryStr.append("  and legalcase.caseDate >=:fromDate  ");

        if (genericSubReportResult.getToDate() != null)
            queryStr.append(" and legalcase.caseDate <=:toDate ");

        if (StringUtils.isNotBlank(genericSubReportResult.getStandingCounsel()))
            queryStr.append(" and legalcase.oppPartyAdvocate =:standingCouncil ");
        if (genericSubReportResult.getOfficerIncharge() != null)
            queryStr.append(" and position.id =:officerIncharge ");
        if (genericSubReportResult.getCaseStatus() != null)
            queryStr.append(" and egwStatus.code =:status ");
        if (genericSubReportResult.getCourtType() != null)
            queryStr.append(" and courtmaster.courtType.id =:courtType ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" and judgment.judgmentType.id =:judgmentType ");
        if (genericSubReportResult.getPetitionTypeId() != null)
            queryStr.append(" and petmaster.id =:petitionType ");
        if (genericSubReportResult.getCasecategoryId() != null)
            queryStr.append(" and casetypemaster.id =:caseType ");
        if (genericSubReportResult.getCourtId() != null)
            queryStr.append(" and courtmaster.id =:courtName ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" and reportStatus.id =:casesubStatus ");
    }

    private void getAggregateQueryByCourtName(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append(" SELECT COUNT(DISTINCT legalcase.id) as noOfCase ");
        else
            queryStr.append(" select distinct legalcase  as  legalCase ");
        queryStr.append(",courtmaster.name  as aggregatedBy ");
        queryStr.append(" from LegalCase legalcase,CourtMaster courtmaster  ");
        queryStr.append(" ,CaseTypeMaster casetypemaster , PetitionTypeMaster petmaster,EgwStatus egwStatus ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" ,Judgment judgment ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" ,ReportStatus reportStatus ");
        queryStr.append(" where legalcase.courtMaster.id=courtmaster.id and ");
        queryStr.append(
                "  legalcase.caseTypeMaster.id=casetypemaster.id and legalcase.petitionTypeMaster.id=petmaster.id ");
        queryStr.append(" and legalcase.status.id=egwStatus.id and egwStatus.moduletype =:moduleType  ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" and legalcase.id = judgment.legalCase ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" and legalcase.reportStatus.id=reportStatus.id ");
        if (clickOnCount)
            queryStr.append(" and legalcase.courtMaster.name=:aggreagatedByValue ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by courtmaster.name");
        if (clickOnCount)
            queryStr.append(" ,legalcase");
        queryStr.append(" order by courtmaster.name");
    }

    private void getAggregateQueryByPetitionType(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append(" SELECT COUNT(DISTINCT legalcase.id) as noOfCase ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",petmaster.petitionType as aggregatedBy ");
        queryStr.append("from LegalCase legalcase,CourtMaster courtmaster  ");
        queryStr.append(" , CaseTypeMaster casetypemaster , PetitionTypeMaster petmaster,EgwStatus egwStatus ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" ,Judgment judgment ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" ,ReportStatus reportStatus ");
        queryStr.append(" where legalcase.petitionTypeMaster.id=petmaster.id  and ");
        queryStr.append(
                "  legalcase.caseTypeMaster.id=casetypemaster.id  and legalcase.courtMaster.id=courtmaster.id ");
        queryStr.append(" and legalcase.status.id=egwStatus.id and egwStatus.moduletype =:moduleType  ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" and legalcase.id = judgment.legalCase ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" and legalcase.reportStatus.id=reportStatus.id ");
        if (clickOnCount)
            queryStr.append("and legalcase.petitionTypeMaster.petitionType=:aggreagatedByValue ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by petmaster.petitionType");
        if (clickOnCount)
            queryStr.append(" ,legalcase");
        queryStr.append(" order by petmaster.petitionType");
    }

    private void getAggregateQueryByCourtType(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append(" SELECT COUNT(DISTINCT legalcase.id) as noOfCase ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",courtmaster.courtType.courtType  as aggregatedBy ");
        queryStr.append("from LegalCase legalcase,CourtMaster courtmaster ");
        queryStr.append(" , CaseTypeMaster casetypemaster , PetitionTypeMaster petmaster,EgwStatus egwStatus ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" ,Judgment judgment ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" ,ReportStatus reportStatus ");
        queryStr.append(
                " where legalcase.courtMaster.id=courtmaster.id and legalcase.petitionTypeMaster.id=petmaster.id  ");
        queryStr.append(" and legalcase.caseTypeMaster.id=casetypemaster.id  ");
        queryStr.append(" and legalcase.status.id=egwStatus.id and egwStatus.moduletype =:moduleType ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append("  and legalcase.id = judgment.legalCase ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" and legalcase.reportStatus.id=reportStatus.id ");
        if (clickOnCount)
            queryStr.append(" and legalcase.courtMaster.courtType.courtType=:aggreagatedByValue ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by courtmaster.courtType.courtType");
        if (clickOnCount)
            queryStr.append(" ,legalcase");
        queryStr.append(" order by courtmaster.courtType.courtType");
    }

    private void getAggregateQueryByCaseStatus(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append(" SELECT COUNT(DISTINCT legalcase.id) as noOfCase ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",egwStatus.description  as aggregatedBy ");
        queryStr.append("from LegalCase legalcase,EgwStatus egwStatus ");
        queryStr.append(" ,CourtMaster courtmaster ,CaseTypeMaster casetypemaster , PetitionTypeMaster petmaster ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" ,Judgment judgment ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" ,ReportStatus reportStatus ");
        queryStr.append(
                " where legalcase.courtMaster.id=courtmaster.id and legalcase.petitionTypeMaster.id=petmaster.id ");
        queryStr.append(" and legalcase.caseTypeMaster.id=casetypemaster.id ");
        queryStr.append(" and  legalcase.status.id=egwStatus.id and egwStatus.moduletype =:moduleType ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" and legalcase.id = judgment.legalCase ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" and legalcase.reportStatus.id=reportStatus.id ");
        if (clickOnCount)
            queryStr.append(" and egwStatus.description=:aggreagatedByValue ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by egwStatus.description");
        if (clickOnCount)
            queryStr.append(" ,legalcase");
        queryStr.append(" order by egwStatus.description");
    }

    private void getAggregateQueryByOfficerIncharge(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append(" SELECT COUNT(DISTINCT legalcase.id) as noOfCase ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",position.name as aggregatedBy ");
        queryStr.append("from LegalCase legalcase,EgwStatus egwStatus, Position position ");
        queryStr.append(" ,CourtMaster courtmaster ,CaseTypeMaster casetypemaster , PetitionTypeMaster petmaster ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" ,Judgment judgment ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" ,ReportStatus reportStatus ");
        queryStr.append(
                " where legalcase.courtMaster.id=courtmaster.id and legalcase.petitionTypeMaster.id=petmaster.id ");
        queryStr.append("  and legalcase.caseTypeMaster.id=casetypemaster.id  ");
        queryStr.append(" and  legalcase.status.id=egwStatus.id and egwStatus.moduletype =:moduleType ");
        queryStr.append(" and  legalcase.officerIncharge=position.id ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" and legalcase.id = judgment.legalCase ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" and legalcase.reportStatus.id=reportStatus.id ");
        if (clickOnCount)
            queryStr.append("and position.name=:aggreagatedByValue ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by position.name");
        if (clickOnCount)
            queryStr.append(" ,legalcase");
        queryStr.append(" order by position.name");
    }

    private void getAggregateQueryByJudgementOutcome(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append(" SELECT COUNT(DISTINCT legalcase.id) as noOfCase ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",judgment.judgmentType.name  as aggregatedBy ");
        queryStr.append(" from LegalCase legalcase,Judgment judgment  ");

        queryStr.append(
                " ,CourtMaster courtmaster ,CaseTypeMaster casetypemaster , PetitionTypeMaster petmaster,EgwStatus egwStatus ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" ,ReportStatus reportStatus ");
        queryStr.append(" where legalcase.id = judgment.legalCase and ");
        queryStr.append("  legalcase.courtMaster.id=courtmaster.id and legalcase.petitionTypeMaster.id=petmaster.id ");
        queryStr.append(" and  legalcase.caseTypeMaster.id=casetypemaster.id ");
        queryStr.append(" and legalcase.status.id=egwStatus.id and egwStatus.moduletype =:moduleType ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" and legalcase.reportStatus.id=reportStatus.id ");
        if (clickOnCount)
            queryStr.append(" and judgment.judgmentType.name=:aggreagatedByValue ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by judgment.judgmentType.name");
        if (clickOnCount)
            queryStr.append(" ,legalcase");
        queryStr.append(" order by judgment.judgmentType.name");
    }

    private void getAggregateQueryByCaseCategory(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append(" SELECT COUNT(DISTINCT legalcase.id) as noOfCase ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",casetypemaster.caseType as aggregatedBy ");
        queryStr.append("from LegalCase legalcase,CaseTypeMaster casetypemaster ");
        queryStr.append(" ,CourtMaster courtmaster , PetitionTypeMaster petmaster,EgwStatus egwStatus ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" ,Judgment judgment ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" ,ReportStatus reportStatus ");
        queryStr.append(" where legalcase.caseTypeMaster.id=casetypemaster.id ");
        queryStr.append(
                " and legalcase.courtMaster.id=courtmaster.id and legalcase.petitionTypeMaster.id=petmaster.id ");
        queryStr.append(" and legalcase.status.id=egwStatus.id and egwStatus.moduletype =:moduleType ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" and legalcase.id = judgment.legalCase ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append(" and legalcase.reportStatus.id=reportStatus.id ");
        if (clickOnCount)
            queryStr.append(" and legalcase.caseTypeMaster.caseType=:aggreagatedByValue ");
        getAppendQuery(genericSubReportResult, queryStr);
        queryStr.append("group by casetypemaster.caseType");
        if (clickOnCount)
            queryStr.append(" ,legalcase");
        queryStr.append(" order by casetypemaster.caseType");

    }

    public void getLegalCaseSubReportStatus(final GenericSubReportResult genericSubReportResult,
            final StringBuilder queryStr, final Boolean clickOnCount) {

        queryStr.append("select distinct legalcase  as  legalCase ,courtmaster.name  as  courtName ,");
        queryStr.append(" egwStatus.code  as  caseStatus ");
        queryStr.append(" from LegalCase legalcase left join legalcase.officerIncharge position,CourtMaster courtmaster,CaseTypeMaster casetypemaster,");
        queryStr.append(" PetitionTypeMaster petmaster,EgwStatus egwStatus,ReportStatus reportStatus");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" ,Judgment judgment ");
        queryStr.append(" where legalcase.courtMaster.id=courtmaster.id and ");
        queryStr.append(
                " legalcase.caseTypeMaster.id=casetypemaster.id and legalcase.petitionTypeMaster.id=petmaster.id and ");
        queryStr.append(" legalcase.status.id=egwStatus.id and egwStatus.moduletype =:moduleType  ");
        if (genericSubReportResult.getReportStatusId() != null)
            queryStr.append("  and legalcase.reportStatus.id = reportStatus.id ");
        if (genericSubReportResult.getJudgmentTypeId() != null)
            queryStr.append(" and legalcase.id = judgment.legalCase ");

    }

}