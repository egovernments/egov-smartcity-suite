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
import javax.persistence.PersistenceContext;

import org.egov.lcms.reports.entity.TimeSeriesReportResult;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TimeSeriesReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<TimeSeriesReportResult> getTimeSeriesReports(final TimeSeriesReportResult timeSeriesReportResults,
            final Boolean clickOnCount) {

        final StringBuilder queryStr = new StringBuilder();
        if (timeSeriesReportResults.getAggregatedBy().equals(LcmsConstants.COURTNAME))
            getAggregateQueryByCourtName(timeSeriesReportResults, queryStr, clickOnCount);

        if (timeSeriesReportResults.getAggregatedBy().equals(LcmsConstants.PETITIONTYPE))
            getAggregateQueryByPetitionType(timeSeriesReportResults, queryStr, clickOnCount);

        if (timeSeriesReportResults.getAggregatedBy().equals(LcmsConstants.CASECATEGORY))
            getAggregateQueryByCaseCategory(timeSeriesReportResults, queryStr, clickOnCount);

        if (timeSeriesReportResults.getAggregatedBy().equals(LcmsConstants.CASESTATUS))
            getAggregateQueryByCaseStatus(timeSeriesReportResults, queryStr, clickOnCount);

        if (timeSeriesReportResults.getAggregatedBy().equals(LcmsConstants.COURTTYPE))
            getAggregateQueryByCourtType(timeSeriesReportResults, queryStr, clickOnCount);

        if (timeSeriesReportResults.getAggregatedBy().equals(LcmsConstants.OFFICERINCHRGE))
            getAggregateQueryByOfficerIncharge(timeSeriesReportResults, queryStr, clickOnCount);

        if (timeSeriesReportResults.getAggregatedBy().equals(LcmsConstants.STANDINGCOUNSEL))
            getAggregateQueryByStandingCounsel(timeSeriesReportResults, queryStr, clickOnCount);

        Query queryResult = getCurrentSession().createQuery(queryStr.toString());
        queryResult = setParameterToQuery(timeSeriesReportResults, queryResult, clickOnCount);
        final List<TimeSeriesReportResult> timeSeriesReportList = queryResult.list();
        return timeSeriesReportList;

    }

    private Query setParameterToQuery(final TimeSeriesReportResult timeSeriesReportObj, final Query queryResult,
            final Boolean clickOnCount) {

        if (clickOnCount) {
            queryResult.setString("aggreagatedByValue", timeSeriesReportObj.getAggregatedByValue());
            if (timeSeriesReportObj.getPeriod().equals(LcmsConstants.AGG_MONTH))
                queryResult.setString("month", timeSeriesReportObj.getMonth());
            queryResult.setInteger("year", timeSeriesReportObj.getYear());
        }
        if (timeSeriesReportObj.getAggregatedBy().equals(LcmsConstants.CASESTATUS))
            queryResult.setString("moduleType", LcmsConstants.MODULE_TYPE_LEGALCASE);

        if (timeSeriesReportObj.getFromDate() != null)
            queryResult.setDate("fromDate", timeSeriesReportObj.getFromDate());
        if (timeSeriesReportObj.getToDate() != null)
            queryResult.setDate("toDate", timeSeriesReportObj.getToDate());

        queryResult.setResultTransformer(new AliasToBeanResultTransformer(TimeSeriesReportResult.class));
        return queryResult;
    }

    private void getAppendQuery(final TimeSeriesReportResult timeSeriesReportObj, final StringBuilder queryStr) {

        if (timeSeriesReportObj.getFromDate() != null)
            queryStr.append("  legalcase.caseDate >=:fromDate and ");
        if (timeSeriesReportObj.getToDate() != null)
            queryStr.append("  legalcase.caseDate <=:toDate ");
    }

    private void getAggregateQueryByCourtName(final TimeSeriesReportResult timeSeriesReportResults,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append("SELECT COUNT(DISTINCT legalcase.id) as count ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(" ,courtmaster.name  as aggregatedBy,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon') as month,");
        queryStr.append(" extract(year from legalcase.caseDate) as year ");
        queryStr.append(" from LegalCase legalcase,CourtMaster courtmaster ");
        queryStr.append(" where legalcase.courtMaster.id=courtmaster.id and ");
        if (clickOnCount) {
            queryStr.append(" legalcase.courtMaster.name=:aggreagatedByValue ");
            if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
                queryStr.append(" and to_char(legalcase.caseDate,'Mon')=:month ");
            queryStr.append(" and extract(year from legalcase.caseDate)=:year ");

        }
        getAppendQuery(timeSeriesReportResults, queryStr);
        queryStr.append(" group by courtmaster.name,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        if (clickOnCount)
            queryStr.append(" legalcase,");
        queryStr.append(" extract(year from legalcase.caseDate) ");
        queryStr.append(" order by courtmaster.name ,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        queryStr.append(" extract(year from legalcase.caseDate) ");

    }

    private void getAggregateQueryByPetitionType(final TimeSeriesReportResult timeSeriesReportResults,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append("SELECT COUNT(DISTINCT legalcase.id) as count");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",petmaster.petitionType  as aggregatedBy,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon') as month,");
        queryStr.append(" extract(year from legalcase.caseDate) as year ");
        queryStr.append(" from LegalCase legalcase,PetitionTypeMaster petmaster ");

        queryStr.append("where legalcase.petitionTypeMaster.id=petmaster.id and ");
        if (clickOnCount) {
            queryStr.append(" legalcase.petitionTypeMaster.petitionType=:aggreagatedByValue ");
            if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
                queryStr.append(" and to_char(legalcase.caseDate,'Mon')=:month ");
            queryStr.append(" and extract(year from legalcase.caseDate)=:year ");

        }
        getAppendQuery(timeSeriesReportResults, queryStr);
        queryStr.append(" group by petmaster.petitionType");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" ,to_char(legalcase.caseDate,'Mon') ,");
        if (clickOnCount)
            queryStr.append(" legalcase,");
        queryStr.append(" extract(year from legalcase.caseDate) ");
        queryStr.append(" order by petmaster.petitionType ,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        queryStr.append(" extract(year from legalcase.caseDate) ");

    }

    private void getAggregateQueryByCaseCategory(final TimeSeriesReportResult timeSeriesReportResults,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append("SELECT COUNT(DISTINCT legalcase.id) as count ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",casetypemaster.caseType  as aggregatedBy,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon') as month,");
        queryStr.append(" extract(year from legalcase.caseDate) as year ");
        queryStr.append(" from LegalCase legalcase,CaseTypeMaster casetypemaster ");

        queryStr.append(" where legalcase.caseTypeMaster.id=casetypemaster.id and ");
        if (clickOnCount) {
            queryStr.append(" legalcase.caseTypeMaster.caseType=:aggreagatedByValue ");
            if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
                queryStr.append(" and to_char(legalcase.caseDate,'Mon')=:month ");
            queryStr.append(" and extract(year from legalcase.caseDate)=:year ");

        }

        getAppendQuery(timeSeriesReportResults, queryStr);
        queryStr.append(" group by casetypemaster.caseType,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        if (clickOnCount)
            queryStr.append(" legalcase,");
        queryStr.append(" extract(year from legalcase.caseDate) ");
        queryStr.append(" order by casetypemaster.caseType ,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        queryStr.append(" extract(year from legalcase.caseDate) ");
    }

    private void getAggregateQueryByCaseStatus(final TimeSeriesReportResult timeSeriesReportResults,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append("SELECT COUNT(DISTINCT legalcase.id) as count ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",egwStatus.description  as aggregatedBy,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon') as month,");
        queryStr.append(" extract(year from legalcase.caseDate) as year ");
        queryStr.append(
                " from LegalCase legalcase,EgwStatus egwStatus  where legalcase.status.id=egwStatus.id and egwStatus.moduletype =:moduleType and ");
        if (clickOnCount) {
            queryStr.append(" egwStatus.description=:aggreagatedByValue ");
            if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
                queryStr.append(" and to_char(legalcase.caseDate,'Mon')=:month ");
            queryStr.append(" and extract(year from legalcase.caseDate)=:year ");

        }
        getAppendQuery(timeSeriesReportResults, queryStr);
        queryStr.append(" group by egwStatus.description,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        if (clickOnCount)
            queryStr.append(" legalcase,");
        queryStr.append(" extract(year from legalcase.caseDate) ");
        queryStr.append(" order by egwStatus.description ,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        queryStr.append(" extract(year from legalcase.caseDate) ");

    }

    private void getAggregateQueryByCourtType(final TimeSeriesReportResult timeSeriesReportResults,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append(" SELECT COUNT(DISTINCT legalcase.id) as count ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",courtmaster.courtType.courtType  as aggregatedBy,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon') as month,");
        queryStr.append(" extract(year from legalcase.caseDate) as year ");
        queryStr.append(" from LegalCase legalcase,CourtMaster courtmaster ");

        queryStr.append(" where legalcase.courtMaster.id=courtmaster.id  and ");
        if (clickOnCount) {
            queryStr.append(" legalcase.courtMaster.courtType.courtType=:aggreagatedByValue ");
            if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
                queryStr.append(" and to_char(legalcase.caseDate,'Mon')=:month ");
            queryStr.append(" and extract(year from legalcase.caseDate)=:year ");

        }

        getAppendQuery(timeSeriesReportResults, queryStr);
        queryStr.append(" group by courtmaster.courtType.courtType,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        if (clickOnCount)
            queryStr.append(" legalcase,");
        queryStr.append(" extract(year from legalcase.caseDate) ");
        queryStr.append(" order by courtmaster.courtType.courtType ,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        queryStr.append(" extract(year from legalcase.caseDate) ");

    }

    private void getAggregateQueryByOfficerIncharge(final TimeSeriesReportResult timeSeriesReportResults,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append("SELECT COUNT(DISTINCT legalcase.id) as count ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",position.name as aggregatedBy,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon') as month,");
        queryStr.append(" extract(year from legalcase.caseDate) as year ");
        queryStr.append(" from LegalCase legalcase,Position position where legalcase.officerIncharge=position.id and ");
        if (clickOnCount) {
            queryStr.append(" position.name=:aggreagatedByValue ");
            if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
                queryStr.append(" and to_char(legalcase.caseDate,'Mon')=:month ");
            queryStr.append(" and extract(year from legalcase.caseDate)=:year ");

        }

        getAppendQuery(timeSeriesReportResults, queryStr);
        queryStr.append(" group by position.name,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        if (clickOnCount)
            queryStr.append(" legalcase,");
        queryStr.append(" extract(year from legalcase.caseDate) ");
        queryStr.append(" order by position.name ,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        queryStr.append(" extract(year from legalcase.caseDate) ");
    }

    private void getAggregateQueryByStandingCounsel(final TimeSeriesReportResult timeSeriesReportResults,
            final StringBuilder queryStr, final Boolean clickOnCount) {
        if (!clickOnCount)
            queryStr.append(" SELECT COUNT(DISTINCT legalcase.id) as count ");
        else
            queryStr.append("select distinct legalcase  as  legalCase ");
        queryStr.append(",advocate.name as aggregatedBy,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon') as month,");
        queryStr.append(" extract(year from legalcase.caseDate) as year ");
        queryStr.append(" from LegalCase legalcase ,LegalCaseAdvocate legalCaseAdvocates ,AdvocateMaster advocate ");

        queryStr.append("where legalcase.id=legalCaseAdvocates.legalCase ");
        queryStr.append(" and advocate.isActive='true' and ");
        if (clickOnCount) {
            queryStr.append(" advocate.name=:aggreagatedByValue ");
            if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
                queryStr.append(" and to_char(legalcase.caseDate,'Mon')=:month ");
            queryStr.append(" and extract(year from legalcase.caseDate)=:year ");

        }
        getAppendQuery(timeSeriesReportResults, queryStr);
        queryStr.append(" group by advocate.name,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        if (clickOnCount)
            queryStr.append(" legalcase,");
        queryStr.append(" extract(year from legalcase.caseDate) ");
        queryStr.append(" order by advocate.name ,");
        if (timeSeriesReportResults.getPeriod().equals(LcmsConstants.AGG_MONTH))
            queryStr.append(" to_char(legalcase.caseDate,'Mon'), ");
        queryStr.append(" extract(year from legalcase.caseDate) ");
    }

}
