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
package org.egov.wtms.application.service;

import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.wtms.application.entity.DataEntryConnectionReport;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DataEntryConnectionReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @ReadOnly
    public List<DataEntryConnectionReport> getDataEntryConnectionReportDetails(final String ward) throws ParseException {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select dcbinfo.hscno as \"hscNo\", dcbinfo.propertyid as \"assessmentNo\", dcbinfo.username as \"ownerName\", zoneboundary.name as \"zone\", wardboundary.name as \"revenueWard\","
                        + "blockboundary.name as \"block\",localboundary.localname as \"locality\",dcbinfo.address as \"address\" , dcbinfo.mobileno as \"mobileNumber\", dcbinfo.watersource as \"waterSource\" ,  "
                        + "dcbinfo.propertytype as \"propertyType\" , dcbinfo.applicationtype as \"applicationType\", dcbinfo.connectiontype as \"connectionType\",  "
                        + "dcbinfo.usagetype as \"usageType\" , dcbinfo.categorytype as \"category\", dcbinfo.pipesize as \"pipeSizeInInch\",  "
                        + "dcbinfo.aadharno as \"aadharNumber\" ,  dcbinfo.numberofperson as \"noOfPersons\" , dcbinfo.numberofrooms as \"noOfRooms\" , dcbinfo.sumpcapacity as \"sumpCapacity\" , "
                        + " dcbinfo.executiondate as \"connectionDate\"  ,dcbinfo.arr_balance+dcbinfo.curr_balance as \"waterTaxDue\" , "
                        + "dcbinfo.pt_firsthalf_demand + dcbinfo.pt_secondhalf_demand - dcbinfo.pt_firsthalf_collection - dcbinfo.pt_secondhalf_collection as \"propertyTaxDue\" "
                        + "from egwtr_mv_dcb_view dcbinfo"
                        + " INNER JOIN eg_boundary localboundary on dcbinfo.locality = localboundary.id INNER JOIN eg_boundary zoneboundary on dcbinfo.zoneid = zoneboundary.id"
                        + " INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id  INNER JOIN eg_boundary blockboundary on dcbinfo.block = blockboundary.id");
        queryStr.append(
                " where dcbinfo.connectionstatus = 'ACTIVE' and dcbinfo.legacy = true and dcbinfo.approvalnumber IS NULL  and dcbinfo.connectiontype = 'NON_METERED' ");
        if (ward != null && !ward.isEmpty())
            queryStr.append(" and wardboundary.name = " + "'" + ward + "'");
        final SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(DataEntryConnectionReport.class));
        return query.list();

    }
}
