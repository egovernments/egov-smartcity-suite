/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.wtms.application.service;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.toDefaultDateTimeFormat;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARISATION_DEMAND_NOTE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.SearchNoticeDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SearchNoticeService {

    private static final String WARD = "ward";
    private static final String ZONE = "zone";
    private static final String CONSUMERCODE = "consumerCode";
    private static final String HOUSENUMBER = "houseNumber";
    private static final String ASSESSMENT_NUMBER = "assessmentNumber";
    private static final String CONNECTION_TYPE = "connectionType";
    private static final String APPLICATION_TYPE = "applicationType";
    private static final String PROPERTY_TYPE = "propertyType";
    private static final String INACTIVE = "INACTIVE";
    private static final String FORMATTED_FROM_DATE = "formattedFromDate";
    private static final String FORMATTED_TO_DATE = "formattedToDate";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    private static final Logger LOGGER = Logger.getLogger(SearchNoticeService.class);

    @ReadOnly
    @SuppressWarnings("unchecked")
    public List<SearchNoticeDetails> getBillReportDetails(final SearchNoticeDetails searchNoticeDetails) {
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
        if (isNotBlank(searchNoticeDetails.getRevenueWard()))
            queryStr.append(" and wardboundary.name =:ward");
        if (isNotBlank(searchNoticeDetails.getZone()))
            queryStr.append(" and zoneboundary.name =:zone");
        if (isNotBlank(searchNoticeDetails.getHscNo()))
            queryStr.append(" and dcbinfo.hscno =:consumerCode");
        if (isNotBlank(searchNoticeDetails.getAssessmentNo()))
            queryStr.append(" and dcbinfo.propertyid =:assessmentNumber");
        if (isNotBlank(searchNoticeDetails.getHouseNumber()))
            queryStr.append(" and dcbinfo.houseno =:houseNumber");
        if (isNotBlank(searchNoticeDetails.getConnectionType()))
            queryStr.append(" and dcbinfo.connectiontype =:connectionType");
        if (isNotBlank(searchNoticeDetails.getApplicationType()))
            queryStr.append(" and dcbinfo.applicationtype =:applicationType");
        if (isNotBlank(searchNoticeDetails.getPropertyType()))
            queryStr.append(" and dcbinfo.propertytype =:propertyType");

        final Query query = entityManager.unwrap(Session.class).createSQLQuery(queryStr.toString());
        setSearchQueryParameters(searchNoticeDetails, null, null, query);
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
    @SuppressWarnings("unchecked")
    public List<SearchNoticeDetails> getSearchResultList(final SearchNoticeDetails searchNoticeDetails) {
        StringBuilder selectQuery = new StringBuilder(1000);
        StringBuilder fromQuery = new StringBuilder(1000);
        StringBuilder whereQuery = new StringBuilder(600);

        selectQuery.append("select propertytype.name as propertyType, applicationtype.name as applicationType, ")
                .append("connection.consumercode as hscNo, connectionaddress.ownername as ownerName, connection.propertyIdentifier as assessmentNo, ")
                .append("conndetails.workordernumber as workOrderNumber, conndetails.workorderdate as workOrderDate, connectionaddress.doornumber as houseNumber, ")
                .append(" connectionaddress.locality as locality, conndetails.connectiontype as connectiontype, ")
                .append(" conndetails.estimationnumber as estimationNumber, conndetails.estimationnoticedate as estimationDate");

        fromQuery.append(" from egwtr_connection connection INNER JOIN egwtr_connectiondetails conndetails on ")
                .append("connection.id=conndetails.connection INNER JOIN egwtr_connection_address connectionaddress on ")
                .append("connectionaddress.connectiondetailsid=conndetails.id INNER JOIN ")
                .append("egwtr_property_type propertytype on conndetails.propertytype=propertytype.id INNER JOIN ")
                .append("egwtr_application_type applicationtype on conndetails.applicationtype=applicationtype.id ");
        whereQuery.append(" where ");
        if (isNotBlank(searchNoticeDetails.getRevenueWard()) && isNotBlank(searchNoticeDetails.getZone())) {
            fromQuery.append(" INNER JOIN eg_boundary wardboundary on connectionaddress.revenueward=wardboundary.id ");
            fromQuery.append(" INNER JOIN eg_boundary zoneboundary on connectionaddress.zone=zoneboundary.id ");
            selectQuery.append(" ,wardboundary.name as revenueWard , zoneboundary.name as zone ");
            whereQuery.append(" wardboundary.name=:ward and zoneboundary.name=:zone and ");
        } else if (isNotBlank(searchNoticeDetails.getRevenueWard())) {
            fromQuery.append(" INNER JOIN eg_boundary boundary on connectionaddress.revenueward=boundary.id ");
            selectQuery.append(" ,boundary.name as revenueWard ");
            whereQuery.append(" boundary.name=:ward and ");
        } else if (isNotBlank(searchNoticeDetails.getZone())) {
            fromQuery.append(" INNER JOIN eg_boundary boundary on connectionaddress.zone=boundary.id ");
            selectQuery.append(" ,boundary.name as zone ");
            whereQuery.append(" boundary.name=:zone and ");
        }

        if (isNotBlank(searchNoticeDetails.getHouseNumber()))
            whereQuery.append(" connectionaddress.doornumber=:houseNumber and ");

        if (isNotBlank(searchNoticeDetails.getPropertyType()))
            whereQuery.append(" propertytype.name=:propertyType and ");

        String formattedFromDate = null;
        String formattedToDate = null;
        String[] dateArray = null;
        if (isNotBlank(searchNoticeDetails.getFromDate())) {
            dateArray = searchNoticeDetails.getFromDate().split("/");
            formattedFromDate = dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];
            whereQuery.append(" conndetails.workorderdate >=(cast(:formattedFromDate as date)) and ");
        }
        if (isNotBlank(searchNoticeDetails.getToDate())) {
            dateArray = searchNoticeDetails.getToDate().split("/");
            formattedToDate = dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];
            whereQuery.append(" conndetails.workorderdate <=(cast(:formattedToDate as date)) and ");
        }

        if (isNotBlank(searchNoticeDetails.getApplicationType()))
            whereQuery.append(" applicationtype.name=:applicationType and ");

        if (isNotBlank(searchNoticeDetails.getConnectionType()))
            whereQuery.append(" conndetails.connectiontype=:connectionType and ");

        if (REGULARISATION_DEMAND_NOTE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
            whereQuery.append(" applicationtype.code=:reglnApplicationType and ");
        else
            whereQuery.append(" applicationtype.code!=:reglnApplicationType and ");

        if (isNotBlank(searchNoticeDetails.getHscNo()))
            whereQuery.append(" connection.consumercode=:consumerCode and ");

        if (isNotBlank(searchNoticeDetails.getAssessmentNo()))
            whereQuery.append(" connection.propertyIdentifier=:assessmentNumber and ");
        whereQuery.append("conndetails.connectionstatus!=:connectionStatus");
        Query query = entityManager.unwrap(Session.class)
                .createSQLQuery(selectQuery.append(fromQuery).append(whereQuery).toString());
        setSearchQueryParameters(searchNoticeDetails, formattedFromDate, formattedToDate, query);
        query.setParameter("reglnApplicationType", REGULARIZE_CONNECTION);
        query.setParameter("connectionStatus", INACTIVE);
        List<Object[]> objectList = query.list();
        return getSearchNoticeList(objectList);
    }

    private Query setSearchQueryParameters(final SearchNoticeDetails searchNoticeDetails, String formattedFromDate,
            String formattedToDate, Query query) {
        if (isNotBlank(searchNoticeDetails.getZone()))
            query.setParameter(ZONE, searchNoticeDetails.getZone());
        if (isNotBlank(searchNoticeDetails.getRevenueWard()))
            query.setParameter(WARD, searchNoticeDetails.getRevenueWard());
        if (isNotBlank(searchNoticeDetails.getPropertyType()))
            query.setParameter(PROPERTY_TYPE, searchNoticeDetails.getPropertyType());
        if (isNotBlank(searchNoticeDetails.getApplicationType()))
            query.setParameter(APPLICATION_TYPE, searchNoticeDetails.getApplicationType());
        if (isNotBlank(searchNoticeDetails.getFromDate()))
            query.setParameter(FORMATTED_FROM_DATE, formattedFromDate);
        if (isNotBlank(searchNoticeDetails.getToDate()))
            query.setParameter(FORMATTED_TO_DATE, formattedToDate);
        if (isNotBlank(searchNoticeDetails.getConnectionType()))
            query.setParameter(CONNECTION_TYPE, searchNoticeDetails.getConnectionType());
        if (isNotBlank(searchNoticeDetails.getHscNo()))
            query.setParameter(CONSUMERCODE, searchNoticeDetails.getHscNo());
        if (isNotBlank(searchNoticeDetails.getHouseNumber()))
            query.setParameter(HOUSENUMBER, searchNoticeDetails.getHouseNumber());
        if (isNotBlank(searchNoticeDetails.getAssessmentNo()))
            query.setParameter(ASSESSMENT_NUMBER, searchNoticeDetails.getAssessmentNo());
        return query;
    }

    public List<SearchNoticeDetails> getSearchNoticeList(final List<Object[]> objectList) {
        List<SearchNoticeDetails> noticeDetailList = new ArrayList<>();
        for (Object[] object : objectList) {
            SearchNoticeDetails searchNoticeDetails = setNoticeObjectFields(object);
            noticeDetailList.add(searchNoticeDetails);
        }
        return noticeDetailList;
    }

    private SearchNoticeDetails setNoticeObjectFields(Object... object) {
        SearchNoticeDetails searchNoticeDetails = new SearchNoticeDetails();
        searchNoticeDetails.setPropertyType(object[0] == null ? EMPTY : object[0].toString());
        searchNoticeDetails.setApplicationType(object[1] == null ? EMPTY : object[1].toString());
        searchNoticeDetails.setHscNo(object[2] == null ? EMPTY : object[2].toString());
        searchNoticeDetails.setOwnerName(object[3] == null ? EMPTY : object[3].toString());
        searchNoticeDetails.setAssessmentNo(object[4] == null ? EMPTY : object[4].toString());
        searchNoticeDetails.setWorkOrderNumber(object[5] == null ? EMPTY : object[5].toString());
        searchNoticeDetails.setWorkOrderDate(object[6] == null ? EMPTY : object[6].toString());
        searchNoticeDetails.setHouseNumber(object[7] == null ? EMPTY : object[7].toString());
        searchNoticeDetails.setLocality(object[8] == null ? EMPTY : object[8].toString());
        searchNoticeDetails.setConnectionType(object[9] == null ? EMPTY : object[9].toString());
        searchNoticeDetails.setEstimationNumber(object[10] == null ? EMPTY : object[10].toString());
        searchNoticeDetails.setEstimationDate(object[11] == null ? EMPTY : object[11].toString());
        return searchNoticeDetails;
    }

    public List<SearchNoticeDetails> buildSearchNoticeDetails(final List<WaterConnectionDetails> waterConnectionDetailsList) {
        List<SearchNoticeDetails> noticeDetailList = new ArrayList<>();
        for (WaterConnectionDetails connectionDetails : waterConnectionDetailsList) {
            AssessmentDetails assessmentDetails = null;
            if (connectionDetails.getConnection().getPropertyIdentifier() != null)
                assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                        connectionDetails.getConnection().getPropertyIdentifier(),
                        PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
            if (assessmentDetails != null) {
                SearchNoticeDetails noticeDetails = new SearchNoticeDetails();
                noticeDetails.setAssessmentNo(connectionDetails.getConnection().getPropertyIdentifier());
                noticeDetails.setHscNo(connectionDetails.getConnection().getConsumerCode());
                noticeDetails.setWorkOrderDate(toDefaultDateTimeFormat(connectionDetails.getWorkOrderDate()));
                noticeDetails.setWorkOrderNumber(connectionDetails.getWorkOrderNumber());
                noticeDetails.setConnectionType(connectionDetails.getConnectionType().name());
                noticeDetails.setFileStoreID(connectionDetails.getFileStore().getFileStoreId());
                noticeDetails.setHouseNumber(assessmentDetails.getHouseNo());

                Iterator<OwnerName> nameIterator = assessmentDetails.getOwnerNames().iterator();
                OwnerName ownerName = null;
                if (nameIterator != null && nameIterator.hasNext())
                    ownerName = nameIterator.next();
                noticeDetails.setOwnerName(ownerName == null ? "N/A" : ownerName.getOwnerName());

                noticeDetails.setLocality(assessmentDetails.getBoundaryDetails().getLocalityName());
                // noticeDetails.
            }
        }
        return noticeDetailList;
    }

    @SuppressWarnings("unchecked")
    public List<Long> getDocuments(final String consumerCode, final String applicationType) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select filestore.filestoreid from eg_filestoremap filestore,egwtr_documents conndoc,egwtr_application_documents appD,egwtr_connectiondetails conndet,egwtr_connection  ")
                .append("conn , egwtr_demand_connection demcon ,eg_demand dem,eg_bill bill, eg_bill_type billtype")
                .append(",egwtr_document_names docName where filestore.id=conndoc.filestoreid and conndet.connection=conn.id and conndet.id=appD.connectiondetailsid and appD.documentnamesid=docName.id and ")
                .append(" bill.id_demand =demcon.demand and billtype.id = bill.id_bill_type and bill.service_code='WT' and conndoc.applicationdocumentsid=appD.id  ")
                .append(" and  demcon.connectiondetails=conndet.id and demcon.demand = dem.id and appD.documentnumber=bill.bill_no and bill.is_cancelled='N' and billtype.code='MANUAL' and dem.is_history ='N' and  docName.documentname='DemandBill' ")
                .append(" and conn.consumercode=:consumerCode")
                .append(" and docName.applicationtype in(select id from egwtr_application_type where name =:applicationType)")
                .append(" order by appD.id desc ");

        final Query query = entityManager.unwrap(Session.class).createSQLQuery(queryStr.toString());
        if (isNotBlank(consumerCode))
            query.setParameter(CONSUMERCODE, consumerCode);
        if (isNotBlank(applicationType))
            query.setParameter(APPLICATION_TYPE, applicationType);
        return query.list();
    }

    public long getTotalCountofBills(final SearchNoticeDetails searchNoticeDetails) {

        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select count(distinct dcbinfo.hscno)  from egwtr_mv_bill_view dcbinfo"
                + " INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id INNER JOIN eg_boundary localboundary on dcbinfo.locality = localboundary.id"
                + " INNER JOIN eg_bill bill on dcbinfo.hscno = bill.consumer_id and dcbinfo.demand= bill.id_demand"
                + " INNER JOIN eg_boundary zoneboundary on dcbinfo.zoneid = zoneboundary.id ");
        queryStr.append(" where dcbinfo.connectionstatus = '" + ConnectionStatus.ACTIVE.toString() + "' ");
        queryStr.append(" and bill.module_id = (select id from eg_module where name ='Water Tax Management')");
        queryStr.append(" and bill.id_bill_type = (select id from eg_bill_type  where code ='MANUAL')");
        queryStr.append(" and bill.is_cancelled ='N' ");
        if (isNotBlank(searchNoticeDetails.getRevenueWard()))
            queryStr.append(" and wardboundary.name =:ward");
        if (isNotBlank(searchNoticeDetails.getZone()))
            queryStr.append(" and zoneboundary.name =:zone");
        if (isNotBlank(searchNoticeDetails.getHscNo()))
            queryStr.append(" and dcbinfo.hscno =:consumerCode");
        if (isNotBlank(searchNoticeDetails.getAssessmentNo()))
            queryStr.append(" and dcbinfo.propertyid =:assessmentNumber");
        if (isNotBlank(searchNoticeDetails.getHouseNumber()))
            queryStr.append(" and dcbinfo.houseno =:houseNumber");
        if (isNotBlank(searchNoticeDetails.getConnectionType()))
            queryStr.append(" and dcbinfo.connectiontype =:connectionType");
        if (isNotBlank(searchNoticeDetails.getApplicationType()))
            queryStr.append(" and dcbinfo.applicationtype =:applicationType");
        if (isNotBlank(searchNoticeDetails.getPropertyType()))
            queryStr.append(" and dcbinfo.propertytype =:propertyType");
        Query query = entityManager.unwrap(Session.class).createSQLQuery(queryStr.toString());
        setSearchQueryParameters(searchNoticeDetails, null, null, query);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("GenerateConnectionBill -- count Result " + queryStr.toString());
        return ((BigInteger) query.uniqueResult()).longValue();
    }

}
