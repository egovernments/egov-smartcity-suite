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

import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infstr.services.PersistenceService;
import org.egov.wtms.application.entity.GenerateConnectionBill;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GenerateConnectionBillService {

    @Qualifier("entityQueryService")
    @Autowired
    private PersistenceService entityQueryService;

    private static final Logger LOGGER = Logger.getLogger(GenerateConnectionBillService.class);

    @ReadOnly
    public List<GenerateConnectionBill> getBillReportDetails(final String zone, final String ward,
            final String propertyType, final String applicationType, final String connectionType,
            final String consumerCode, final String houseNumber, final String assessmentNumber) throws ParseException {
        final long startTime = System.currentTimeMillis();
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select distinct dcbinfo.hscno as \"hscNo\", dcbinfo.username as \"ownerName\",dcbinfo.propertyid as \"assessmentNo\",dcbinfo.demanddocumentnumber as \"fileStoreID\",");
        queryStr.append(
                "dcbinfo.houseno as \"houseNumber\" , localboundary.localname as \"locality\", dcbinfo.applicationtype as \"applicationType\" , ");
        queryStr.append(
                " dcbinfo.connectiontype as  \"connectionType\" , bill.bill_no as \"billNo\" , bill.issue_date as \"billDate\" from egwtr_mv_bill_view dcbinfo");
        queryStr.append(
                " INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id INNER JOIN eg_boundary localboundary on dcbinfo.locality = localboundary.id");
        queryStr.append(
                " INNER JOIN eg_bill bill on dcbinfo.hscno = bill.consumer_id and dcbinfo.demand= bill.id_demand");
        queryStr.append(" INNER JOIN eg_boundary zoneboundary on dcbinfo.zoneid = zoneboundary.id ");
        queryStr.append(" where dcbinfo.connectionstatus = '" + ConnectionStatus.ACTIVE.toString() + "' ");
        queryStr.append(" and bill.module_id = (select id from eg_module where name ='Water Tax Management')");
        queryStr.append(" and bill.id_bill_type = (select id from eg_bill_type  where code ='MANUAL')");
        queryStr.append(" and bill.is_cancelled ='N' ");
        if (ward != null && !ward.isEmpty())
            queryStr.append(" and wardboundary.name = " + "'" + ward + "'");
        if (zone != null && !zone.isEmpty())
            queryStr.append(" and zoneboundary.name = " + "'" + zone + "'");
        if (consumerCode != null && !consumerCode.isEmpty())
            queryStr.append(" and dcbinfo.hscno = " + "'" + consumerCode + "'");
        if (assessmentNumber != null && !assessmentNumber.isEmpty())
            queryStr.append(" and dcbinfo.propertyid = " + "'" + assessmentNumber + "'");
        if (houseNumber != null && !houseNumber.isEmpty())
            queryStr.append(" and dcbinfo.houseno = " + "'" + houseNumber + "'");
        if (connectionType != null && !connectionType.isEmpty())
            queryStr.append(" and dcbinfo.connectiontype = " + "'" + connectionType + "'");
        if (applicationType != null && !applicationType.isEmpty())
            queryStr.append(" and dcbinfo.applicationtype = " + "'" + applicationType + "'");
        if (propertyType != null && !propertyType.isEmpty())
            queryStr.append(" and dcbinfo.propertytype = " + "'" + propertyType + "'");

        final SQLQuery finalQuery = entityQueryService.getSession().createSQLQuery(queryStr.toString());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("GenerateConnectionBill -- Search Result " + queryStr.toString());
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(GenerateConnectionBill.class));
        final List<GenerateConnectionBill> generateConnectionBillList = finalQuery.list();
        final long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GenerateBill | SearchResult | Time taken(ms) " + (endTime - startTime));
            LOGGER.debug("Exit from SearchResult method");
        }
        return generateConnectionBillList;
    }

    public List<Long> getDocuments(final String consumerCode, final String applicationType) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select filestore.filestoreid from eg_filestoremap filestore,egwtr_documents conndoc,egwtr_application_documents appD,egwtr_connectiondetails conndet,egwtr_connection  "
                        + "conn , egwtr_demand_connection demcon ,eg_demand dem,eg_bill bill, eg_bill_type billtype"
                        + ",egwtr_document_names docName where filestore.id=conndoc.filestoreid and conndet.connection=conn.id and conndet.id=appD.connectiondetailsid and appD.documentnamesid=docName.id and "
                        + " bill.id_demand =demcon.demand and billtype.id = bill.id_bill_type and bill.service_code='WT' and conndoc.applicationdocumentsid=appD.id  "
                        + " and  demcon.connectiondetails=conndet.id and demcon.demand = dem.id and appD.documentnumber=bill.bill_no and bill.is_cancelled='N' and billtype.code='MANUAL' and dem.is_history ='N' and  docName.documentname='DemandBill' "
                        + " ");
        queryStr.append(" and conn.consumercode=  " + "'" + consumerCode + "'");
        queryStr.append(" and docName.applicationtype in(select id from egwtr_application_type where name = '"
                + applicationType + "' )");
        queryStr.append(" order by appD.id desc ");

        final SQLQuery finalQuery = entityQueryService.getSession().createSQLQuery(queryStr.toString());
        final List<Long> waterChargesDocumentsList = finalQuery.list();
        return waterChargesDocumentsList;
    }

    public long getTotalCountofBills(final String zone, final String ward, final String propertyType,
            final String applicationType, final String connectionType, final String consumerCode,
            final String houseNumber, final String assessmentNumber) throws ParseException {

        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select count(distinct dcbinfo.hscno)  from egwtr_mv_bill_view dcbinfo"
                + " INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id INNER JOIN eg_boundary localboundary on dcbinfo.locality = localboundary.id"
                + " INNER JOIN eg_bill bill on dcbinfo.hscno = bill.consumer_id and dcbinfo.demand= bill.id_demand"
                + " INNER JOIN eg_boundary zoneboundary on dcbinfo.zoneid = zoneboundary.id ");
        queryStr.append(" where dcbinfo.connectionstatus = '" + ConnectionStatus.ACTIVE.toString() + "' ");
        queryStr.append(" and bill.module_id = (select id from eg_module where name ='Water Tax Management')");
        queryStr.append(" and bill.id_bill_type = (select id from eg_bill_type  where code ='MANUAL')");
        queryStr.append(" and bill.is_cancelled ='N' ");
        if (ward != null && !ward.isEmpty())
            queryStr.append(" and wardboundary.name = " + "'" + ward + "'");
        if (zone != null && !zone.isEmpty())
            queryStr.append(" and zoneboundary.name = " + "'" + zone + "'");
        if (consumerCode != null && !consumerCode.isEmpty())
            queryStr.append(" and dcbinfo.hscno = " + "'" + consumerCode + "'");
        if (assessmentNumber != null && !assessmentNumber.isEmpty())
            queryStr.append(" and dcbinfo.propertyid = " + "'" + assessmentNumber + "'");
        if (houseNumber != null && !houseNumber.isEmpty())
            queryStr.append(" and dcbinfo.houseno = " + "'" + houseNumber + "'");
        if (connectionType != null && !connectionType.isEmpty())
            queryStr.append(" and dcbinfo.connectiontype = " + "'" + connectionType + "'");
        if (applicationType != null && !applicationType.isEmpty())
            queryStr.append(" and dcbinfo.applicationtype = " + "'" + applicationType + "'");
        if (propertyType != null && !propertyType.isEmpty())
            queryStr.append(" and dcbinfo.propertytype = " + "'" + propertyType + "'");
        final SQLQuery finalQuery = entityQueryService.getSession().createSQLQuery(queryStr.toString());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("GenerateConnectionBill -- count Result " + queryStr.toString());

        final Long count = ((BigInteger) finalQuery.uniqueResult()).longValue();

        return count;
    }

}
