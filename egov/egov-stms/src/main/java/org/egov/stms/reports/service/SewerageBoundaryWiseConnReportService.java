/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *
 *       Copyright (C) 2016  eGovernments Foundation
 *
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.stms.reports.service;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageBoundaryWiseConnReportService {

    @Qualifier("entityQueryService")
    @Autowired
    private PersistenceService entityQueryService;

    public SQLQuery getNoOfConnectionReport(final String ward, final String block, final String localityName) {
        final StringBuilder query = new StringBuilder(600);
        query.append(
                "Select bndry.name as name, COUNT(CASE WHEN at.code IN ('NEWSEWERAGECONNECTION') THEN 1 END) newconnection , "
                        + " COUNT(CASE WHEN at.code IN ('CHANGEINCLOSETS') THEN 1 END) changeinclosets, "
                        + " COUNT(CASE WHEN at.code IN ('CLOSESEWERAGECONNECTION') THEN 1 END) closeconnection "
                        + " From egswtax_connection con "
                        + " INNER JOIN egswtax_applicationdetails ad ON con.id = ad.connection "
                        + " INNER JOIN egswtax_connectiondetail cd ON ad.connectiondetail = cd.id "
                        + " INNER JOIN egswtax_application_type at ON at.id = ad.applicationtype "
                        + " INNER JOIN egpt_basic_property bp ON cd.propertyidentifier = bp.propertyid "
                        + " INNER JOIN egpt_propertyid ptid ON bp.ID_PROPERTYID = ptid.id "
                        + " INNER JOIN eg_boundary bndry ");
        buildWhereClause(ward, block, localityName, query);
        buildGroupByClause(ward, block, localityName, query);
        return setParameterForNoOfConnReportQuery(query.toString(), ward, block, localityName);

    }

    private StringBuilder buildWhereClause(final String ward, final String block,
            final String locality, final StringBuilder queryString) {
        boolean inputBothValue = false;
        if (StringUtils.isNotBlank(ward)) {
            queryString.append(" ON bndry.id = ptid.WARD_ADM_ID Where ptid.WARD_ADM_ID=:ward");
            inputBothValue = true;
        }
        if (StringUtils.isNotBlank(block))
            queryString.append(" and ptid.ADM1=:block ");
        if (StringUtils.isNotBlank(locality))
            if (inputBothValue)
                queryString.append(" and bndry.localname=:localityName");
            else
                queryString.append(" ON bndry.id = ptid.ADM2 where bndry.localname=:localityName");
        return queryString;
    }

    private void buildGroupByClause(final String ward, final String block, final String localityName,
            final StringBuilder queryString) {
        if (ward == null && block == null && localityName == null)
            queryString.append(" ON bndry.id = ptid.WARD_ADM_ID");
        queryString.append(" group by bndry.name");
    }

    private SQLQuery setParameterForNoOfConnReportQuery(final String query, final String ward, final String block,
            final String localityName) {
        final SQLQuery queryStr = entityQueryService.getSession().createSQLQuery(query);
        if (StringUtils.isNotBlank(ward))
            queryStr.setLong("ward", Long.valueOf(ward));
        if (StringUtils.isNotBlank(block))
            queryStr.setLong("block", Long.valueOf(block));
        if (StringUtils.isNotBlank(localityName))
            queryStr.setString("localityName", localityName);
        return queryStr;
    }
}
