/*
 * eGov SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.wtms.application.entity.SearchNoticeDetails;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SearchNoticeService {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(SearchNoticeService.class);

    @ReadOnly
    public List<SearchNoticeDetails> getBillReportDetails(final SearchNoticeDetails searchNoticeDetails,
            final String zone, final String ward,
            final String propertyType, final String applicationType, final String connectionType,
            final String consumerCode, final String houseNumber, final String assessmentNumber,
            final String fromDate, final String toDate) {
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
            queryStr.append(" and wardboundary.name =:ward");
        if (zone != null && !zone.isEmpty())
            queryStr.append(" and zoneboundary.name =:zone");
        if (consumerCode != null && !consumerCode.isEmpty())
            queryStr.append(" and dcbinfo.hscno =:consumerCode");
        if (assessmentNumber != null && !assessmentNumber.isEmpty())
            queryStr.append(" and dcbinfo.propertyid =:assessmentNumber");
        if (houseNumber != null && !houseNumber.isEmpty())
            queryStr.append(" and dcbinfo.houseno =:houseNumber");
        if (connectionType != null && !connectionType.isEmpty())
            queryStr.append(" and dcbinfo.connectiontype =:connectionType");
        if (applicationType != null && !applicationType.isEmpty())
            queryStr.append(" and dcbinfo.applicationtype =:applicationType");
        if (propertyType != null && !propertyType.isEmpty())
            queryStr.append(" and dcbinfo.propertytype =:propertyType");

        final Query query = entityManager.unwrap(Session.class).createSQLQuery(queryStr.toString());
        if (isNotBlank(ward))
            query.setParameter("ward", ward);
        if (isNotBlank(zone))
            query.setParameter("zone", zone);
        if (isNotBlank(consumerCode))
            query.setParameter("consumerCode", consumerCode);
        if (isNotBlank(assessmentNumber))
            query.setParameter("houseNumber", houseNumber);
        if (isNotBlank(assessmentNumber))
            query.setParameter("assessmentNumber", assessmentNumber);
        if (isNotBlank(connectionType))
            query.setParameter("connectionType", connectionType);
        if (isNotBlank(applicationType))
            query.setParameter("applicationType", applicationType);
        if (isNotBlank(propertyType))
            query.setParameter("propertyType", propertyType);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("GenerateConnectionBill -- Search Result " + queryStr.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(SearchNoticeDetails.class));
        final long endTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GenerateBill | SearchResult | Time taken(ms) " + (endTime - startTime));
            LOGGER.debug("Exit from SearchResult method");
        }

        return query.list();
    }

    @ReadOnly
    public List<SearchNoticeDetails> getSanctionOrderDetails(final SearchNoticeDetails searchNoticeDetails,
            final String zone, final String ward, final String propertyType,
            final String applicationType, final String connectionType, final String consumerCode, final String houseNumber,
            final String assessmentNumber, final String fromDate, final String toDate) {
        String formattedFromDate = null;
        String formattedToDate = null;
        String[] arr = null;
        if (isNotBlank(fromDate)) {
            arr = fromDate.split("/");
            formattedFromDate = arr[2] + "-" + arr[1] + "-" + arr[0];
        }
        if (isNotBlank(toDate)) {
            arr = toDate.split("/");
            formattedToDate = arr[2] + "-" + arr[1] + "-" + arr[0];
        }

        final StringBuilder queryString = new StringBuilder();
        queryString.append(
                "select distinct dcbinfo.hscno as \"hscNo\", dcbinfo.username as \"ownerName\", dcbinfo.propertyid as \"assessmentNo\", ");
        queryString.append(
                "dcbinfo.houseno as \"houseNumber\", localboundary.localname as \"locality\", dcbinfo.applicationtype as \"applicationType\" , ");
        queryString.append("dcbinfo.workorderdate as \"workOrderDate\", dcbinfo.workordernumber as \"workOrderNumber\", ");
        queryString.append("dcbinfo.connectiontype as \"connectionType\" from egwtr_mv_dcb_view dcbinfo ");
        queryString.append("INNER JOIN eg_boundary zoneboundary on dcbinfo.zoneid=zoneboundary.id ");
        queryString.append(
                " INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id INNER JOIN eg_boundary localboundary on dcbinfo.locality = localboundary.id");

        if (ward != null && !ward.isEmpty())
            queryString.append(" and wardboundary.name =:ward");
        if (zone != null && !zone.isEmpty())
            queryString.append(" and zoneboundary.name =:zone");
        if (consumerCode != null && !consumerCode.isEmpty())
            queryString.append(" and dcbinfo.hscno =:consumerCode");
        if (assessmentNumber != null && !assessmentNumber.isEmpty())
            queryString.append(" and dcbinfo.propertyid =:assessmentNumber");
        if (houseNumber != null && !houseNumber.isEmpty())
            queryString.append(" and dcbinfo.houseno =:houseNumber");
        if (connectionType != null && !connectionType.isEmpty())
            queryString.append(" and dcbinfo.connectiontype =:connectionType");
        if (applicationType != null && !applicationType.isEmpty())
            queryString.append(" and dcbinfo.applicationtype =:applicationType");
        if (propertyType != null && !propertyType.isEmpty())
            queryString.append(" and dcbinfo.propertytype =:propertyType");
        if (formattedFromDate != null && !formattedFromDate.isEmpty())
            queryString.append(" and dcbinfo.workorderdate >=:formattedFromDate");
        if (formattedToDate != null && !formattedToDate.isEmpty())
            queryString.append(" and dcbinfo.workorderdate <=:formattedToDate");

        final Query query = entityManager.unwrap(Session.class).createSQLQuery(queryString.toString());
        if (isNotBlank(ward))
            query.setParameter("ward", ward);
        if (isNotBlank(zone))
            query.setParameter("zone", zone);
        if (isNotBlank(consumerCode))
            query.setParameter("consumerCode", consumerCode);
        if (isNotBlank(assessmentNumber))
            query.setParameter("assessmentNumber", assessmentNumber);
        if (isNotBlank(houseNumber))
            query.setParameter("houseNumber", houseNumber);
        if (isNotBlank(connectionType))
            query.setParameter("connectionType", connectionType);
        if (isNotBlank(applicationType))
            query.setParameter("applicationType", applicationType);
        if (isNotBlank(propertyType))
            query.setParameter("propertyType", propertyType);
        if (isNotBlank(formattedFromDate))
            query.setParameter("formattedFromDate", formattedFromDate);
        if (isNotBlank(formattedToDate))
            query.setParameter("formattedToDate", formattedToDate);

        query.setResultTransformer(new AliasToBeanResultTransformer(SearchNoticeDetails.class));
        return query.list();
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
        queryStr.append(" and conn.consumercode=:consumerCode");
        queryStr.append(" and docName.applicationtype in(select id from egwtr_application_type where name =:applicationType)");
        queryStr.append(" order by appD.id desc ");

        final Query query = entityManager.unwrap(Session.class).createSQLQuery(queryStr.toString());
        if (isNotBlank(consumerCode))
            query.setParameter("consumerCode", consumerCode);
        if (isNotBlank(applicationType))
            query.setParameter("applicationType", applicationType);
        final List<Long> waterChargesDocumentsList = query.list();
        return waterChargesDocumentsList;
    }

    public long getTotalCountofBills(final String zone, final String ward, final String propertyType,
            final String applicationType, final String connectionType, final String consumerCode,
            final String houseNumber, final String assessmentNumber) {

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
            queryStr.append(" and wardboundary.name =:ward");
        if (zone != null && !zone.isEmpty())
            queryStr.append(" and zoneboundary.name =:zone");
        if (consumerCode != null && !consumerCode.isEmpty())
            queryStr.append(" and dcbinfo.hscno =:consumerCode");
        if (assessmentNumber != null && !assessmentNumber.isEmpty())
            queryStr.append(" and dcbinfo.propertyid =:assessmentNumber");
        if (houseNumber != null && !houseNumber.isEmpty())
            queryStr.append(" and dcbinfo.houseno =:houseNumber");
        if (connectionType != null && !connectionType.isEmpty())
            queryStr.append(" and dcbinfo.connectiontype =:connectionType");
        if (applicationType != null && !applicationType.isEmpty())
            queryStr.append(" and dcbinfo.applicationtype =:applicationType");
        if (propertyType != null && !propertyType.isEmpty())
            queryStr.append(" and dcbinfo.propertytype =:propertyType");
        final Query query = entityManager.unwrap(Session.class).createSQLQuery(queryStr.toString());
        if (isNotBlank(ward))
            query.setParameter("ward", ward);
        if (isNotBlank(zone))
            query.setParameter("zone", zone);
        if (isNotBlank(consumerCode))
            query.setParameter("consumerCode", consumerCode);
        if (isNotBlank(assessmentNumber))
            query.setParameter("assessmentNumber", assessmentNumber);
        if (isNotBlank(houseNumber))
            query.setParameter("houseNumber", houseNumber);
        if (isNotBlank(connectionType))
            query.setParameter("connectionType", connectionType);
        if (isNotBlank(applicationType))
            query.setParameter("applicationType", applicationType);
        if (isNotBlank(propertyType))
            query.setParameter("propertyType", propertyType);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("GenerateConnectionBill -- count Result " + queryStr.toString());
        return ((BigInteger) query.uniqueResult()).longValue();
    }

}
