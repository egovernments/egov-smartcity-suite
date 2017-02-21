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

import org.apache.commons.lang.StringUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.lcms.reports.entity.DailyBoardReportResults;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DailyBoardReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<DailyBoardReportResults> getDailyBoardReports(final DailyBoardReportResults dailyBoardReportResults) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select distinct legalObj  as  legalCase ,courtmaster.name  as  courtName ,petmaster.petitionType as petitionType,");
        queryStr.append(" egwStatus.description  as  caseStatus, position.name as officerInChargeName ");
        queryStr.append(
                " from LegalCase legalObj left join legalObj.officerIncharge position, CourtMaster courtmaster,CaseTypeMaster casetypemaster,");
        queryStr.append(" PetitionTypeMaster petmaster,EgwStatus egwStatus ");
        /*
         * if(dailyBoardReportResults.getOfficerIncharge() != null)
         * queryStr.append(" ,Position position ");
         */
        queryStr.append(" where legalObj.courtMaster.id=courtmaster.id and ");
        queryStr.append(
                " legalObj.caseTypeMaster.id=casetypemaster.id and legalObj.petitionTypeMaster.id=petmaster.id and ");
        queryStr.append(" legalObj.status.id=egwStatus.id and egwStatus.moduletype =:moduleType ");
        if (dailyBoardReportResults.getOfficerIncharge() != null)
            queryStr.append(" and legalObj.officerIncharge.id=position.id ");
        getAppendQuery(dailyBoardReportResults, queryStr);
        Query queryResult = getCurrentSession().createQuery(queryStr.toString());
        queryResult = setParameterToQuery(dailyBoardReportResults, queryResult);
        final List<DailyBoardReportResults> dailyBoardReportList = queryResult.list();
        return dailyBoardReportList;

    }

    private Query setParameterToQuery(final DailyBoardReportResults dailyBoardReportObj, final Query queryResult) {
        queryResult.setString("moduleType", LcmsConstants.MODULE_TYPE_LEGALCASE);

        if (StringUtils.isNotBlank(dailyBoardReportObj.getCaseNumber()))
            queryResult.setString("caseNumber", dailyBoardReportObj.getCaseNumber() + "%");
        if (dailyBoardReportObj.getCourtId() != null)
            queryResult.setInteger("court", dailyBoardReportObj.getCourtId());
        if (dailyBoardReportObj.getOfficerIncharge() != null)
            queryResult.setLong("officerIncharge", dailyBoardReportObj.getOfficerIncharge());
        if (dailyBoardReportObj.getCasecategory() != null)
            queryResult.setInteger("casetype", dailyBoardReportObj.getCasecategory());
        if (StringUtils.isNotBlank(dailyBoardReportObj.getStandingCouncil()))
            queryResult.setString("standingCouncil", dailyBoardReportObj.getStandingCouncil() + "%");
        if (dailyBoardReportObj.getStatusId() != null)
            queryResult.setInteger("status", dailyBoardReportObj.getStatusId());
        if (dailyBoardReportObj.getNextDate() != null)
            queryResult.setString("nextDate", DateUtils.getDefaultFormattedDate(dailyBoardReportObj.getNextDate()));
        if (dailyBoardReportObj.getFromDate() != null)
            queryResult.setDate("fromdate", dailyBoardReportObj.getFromDate());
        if (dailyBoardReportObj.getToDate() != null)
            queryResult.setDate("toDate", dailyBoardReportObj.getToDate());
        if (dailyBoardReportObj.getPetitionTypeId() != null)
            queryResult.setInteger("petiontionType", dailyBoardReportObj.getPetitionTypeId());

        queryResult.setResultTransformer(new AliasToBeanResultTransformer(DailyBoardReportResults.class));
        return queryResult;
    }

    private void getAppendQuery(final DailyBoardReportResults dailyBoardReportObj, final StringBuilder queryStr) {

        if (StringUtils.isNotBlank(dailyBoardReportObj.getCaseNumber()))
            queryStr.append(" and legalObj.caseNumber like :caseNumber ");
        if (dailyBoardReportObj.getCourtId() != null)
            queryStr.append(" and courtmaster.id =:court ");
        if (dailyBoardReportObj.getCasecategory() != null)
            queryStr.append(" and casetypemaster.id =:casetype");
        if (StringUtils.isNotBlank(dailyBoardReportObj.getStandingCouncil()))
            queryStr.append(" and legalObj.oppPartyAdvocate like :standingCouncil ");
        if (dailyBoardReportObj.getStatusId() != null)
            queryStr.append(" and egwStatus.id =:status ");
        if (dailyBoardReportObj.getFromDate() != null)
            queryStr.append(" and legalObj.caseDate >=:fromdate ");
        if (dailyBoardReportObj.getToDate() != null)
            queryStr.append(" and legalObj.caseDate <=:toDate ");
        if (dailyBoardReportObj.getPetitionTypeId() != null)
            queryStr.append(" and petmaster.id =:petiontionType ");
        if (dailyBoardReportObj.getNextDate() != null)
            queryStr.append(" and legalObj.nextDate=:DateUtils.getDefaultFormattedDate(nextDate)");
        if (dailyBoardReportObj.getOfficerIncharge() != null)
            queryStr.append(" and position.id =:officerIncharge ");

    }

}
