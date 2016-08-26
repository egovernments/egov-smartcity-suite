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

import java.util.Date;

import javax.persistence.EntityManager;

import org.egov.lcms.transactions.entity.DailyBoardReportResults;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DailyBoardReportService {

    @Autowired
    private EntityManager entityManager;

    public SQLQuery getDailyBoardReport(final String caseNumber, final String court, final String casetype,
            final String standingcouncil,final String officerIncharge, final String status,final Date nextDate) {

        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select distinct legalObj.casetitle as \"caseTitle\",courtmaster.name  as \"courtName\","
                + "legalObj.casenumber as \"caseNumber\","
                + "petmaster.petitionType as \"petitionType\","
                + " legalObj.appealnum  as \"standingCouncil\",egwStatus.description  as \"caseStatus\", "
                + "legalObj.officerincharge  as \"officerIncharge\","
                + "legalObj.nextdate  as \"nextDate\" "
                + "from EGLC_LEGALCASE legalObj,EGLC_BIPARTISANDETAILS bipart,eglc_court_master courtmaster,eglc_casetype_master casetypemaster,"
                + "eglc_petitiontype_master petmaster,egw_status egwStatus");


        queryStr.append (" where  bipart.legalcase=legalObj.id and legalObj.court=courtmaster.id and "
                + "legalObj.casetype=casetypemaster.id and legalObj.petitiontype=petmaster.id and "
                + "legalObj.status=egwStatus.id and egwStatus.moduletype='Legal Case' ");


        if (caseNumber != null && !caseNumber.isEmpty())
            queryStr.append(" and legalObj.casenumber = " + "'" + caseNumber + "'");
        if (court != null && !"".equals(court))
            queryStr.append(" and courtmaster.id = " + "'" + court + "'");
        if (court != null && !"".equals(court))
            queryStr.append(" and courtmaster.id = " + "'" + court + "'");
        if (casetype != null && !"".equals(casetype))
            queryStr.append(" and casetypemaster.id = " + "'" + casetype + "'");

        if (standingcouncil != null && !"".equals(standingcouncil))
            queryStr.append(" and legalObj.appealnum  like  " + "'" + standingcouncil + "%'");
        if (officerIncharge != null && !officerIncharge.isEmpty())
            queryStr.append(" and legalObj.officerIncharge = " + "'" + officerIncharge + "'");
        if (status != null && !"".equals(status))
            queryStr.append(" and egwStatus.id = " + "'" + status + "'");
        if (nextDate != null )
            queryStr.append(" and legalObj.nextDate = " + "'" + nextDate + "'");

        final SQLQuery finalQuery = entityManager.unwrap(Session.class).createSQLQuery(queryStr.toString());
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(DailyBoardReportResults.class));
        return finalQuery;

    }

}
