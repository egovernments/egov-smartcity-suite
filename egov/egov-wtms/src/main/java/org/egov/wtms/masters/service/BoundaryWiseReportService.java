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
package org.egov.wtms.masters.service;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoundaryWiseReportService {
    @Qualifier("entityQueryService")
    private @Autowired PersistenceService entityQueryService;

    public SQLQuery getDrillDownReportQuery(final String ward, final String block) {

        final StringBuffer query = new StringBuffer();
        query.append("SELECT bndry.name as name, ");

        query.append("   COUNT(CASE WHEN cs.code IN ('NEWCONNECTION') THEN 1 END) newconnection , "
                + " COUNT(CASE WHEN cs.code IN ('ADDNLCONNECTION') THEN 1 END) addconnection, "
                + " COUNT(CASE WHEN cs.code IN ('CHANGEOFUSE') THEN 1 END) changeofusage, "
                + " COUNT(CASE WHEN cs.code IN ('CLOSINGCONNECTION') THEN 1 END) closeconnection, "
                + " COUNT(CASE WHEN cs.code IN ('RECONNECTION') THEN 1 END) reconnection ");

        query.append("FROM egwtr_connection con  INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection INNER JOIN egwtr_application_type cs ON cd.applicationtype=cs.id INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid INNER JOIN EGPT_PROPERTYID ptid ON bp.ID_PROPERTYID= ptid.id INNER JOIN eg_boundary bndry ON bndry.id=ptid.WARD_ADM_ID");
        buildWhereClause(ward, block, query);
        buildGroupByClause(ward, block, query);
        return setParameterForDrillDownReportQuery(query.toString(), ward, block);
    }

    private void buildGroupByClause(final String ward, final String block, final StringBuffer query) {
        query.append("  group by bndry.name ");

    }

    private StringBuffer buildWhereClause(final String ward, final String block, final StringBuffer queryStr) {
        if (StringUtils.isNotBlank(ward))
            queryStr.append(" WHERE ptid.WARD_ADM_ID=:ward ");
        if (StringUtils.isNotBlank(block))
            queryStr.append(" and ptid.ADM1=:block ");
        return queryStr;

    }

    private SQLQuery setParameterForDrillDownReportQuery(final String querykey, final String ward, final String block) {
        final SQLQuery qry = entityQueryService.getSession().createSQLQuery(querykey);
        if (StringUtils.isNotBlank(ward))
            qry.setLong("ward", Long.valueOf(ward));
        if (StringUtils.isNotBlank(block))
            qry.setLong("block", Long.valueOf(block));
        return qry;

    }

}
