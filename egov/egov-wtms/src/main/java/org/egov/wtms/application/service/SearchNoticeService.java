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
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATIONSTATUSCLOSED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.END;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ESTIMATION_NOTICE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULETYPE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PROCEEDING_FOR_CLOSER_OF_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PROCEEDING_FOR_RECONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARISATION_DEMAND_NOTE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSINGCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REJECTION_NOTICE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.SearchNoticeDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.hibernate.Query;
import org.hibernate.Session;
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

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @ReadOnly
    @SuppressWarnings("unchecked")
    public List<SearchNoticeDetails> getSearchResultList(final SearchNoticeDetails searchNoticeDetails,
            CFinancialYear financialYear) {
        StringBuilder selectQuery = new StringBuilder(1000);
        StringBuilder fromQuery = new StringBuilder(1000);
        StringBuilder whereQuery = new StringBuilder(600);

        selectQuery.append("select propertytype.name as propertyType, applicationtype.name as applicationType, ")
                .append("connection.consumercode as hscNo, connectionaddress.ownername as ownerName, connection.propertyIdentifier as assessmentNo, ")
                .append("conndetails.workordernumber as workOrderNumber, conndetails.workorderdate as workOrderDate, connectionaddress.doornumber as houseNumber, ")
                .append(" connectionaddress.locality as locality, conndetails.connectiontype as connectiontype, conndetails.applicationnumber as applicationNumber ");
        if (ESTIMATION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
                || REGULARISATION_DEMAND_NOTE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
                || REJECTION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
            selectQuery.append(", en.estimationnumber as estimationNumber, en.estimationnoticedate as estimationDate");
        else
            selectQuery.append(", CAST(NULL AS character varying) as estimationNumber, CAST(NULL AS date) as estimationDate");

        fromQuery.append(" from egwtr_connection connection INNER JOIN egwtr_connectiondetails conndetails on ")
                .append("connection.id=conndetails.connection INNER JOIN egwtr_connection_address connectionaddress on ")
                .append("connectionaddress.connectiondetailsid=conndetails.id INNER JOIN ")
                .append("egwtr_property_type propertytype on conndetails.propertytype=propertytype.id INNER JOIN ")
                .append("egwtr_application_type applicationtype on conndetails.applicationtype=applicationtype.id ");
        if (ESTIMATION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
                || REGULARISATION_DEMAND_NOTE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
                || REJECTION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
            fromQuery.append(" INNER JOIN egwtr_estimation_notice en on conndetails.id = en.CONNECTIONDETAILS ");

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
            if (ESTIMATION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
                    || REGULARISATION_DEMAND_NOTE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
                    || REJECTION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
                whereQuery.append(" en.estimationnoticedate >=(cast(:formattedFromDate as date)) and ");
            else
                whereQuery.append(" conndetails.workorderdate >=(cast(:formattedFromDate as date)) and ");
        }
        if (isNotBlank(searchNoticeDetails.getToDate())) {
            dateArray = searchNoticeDetails.getToDate().split("/");
            formattedToDate = dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];
            if (ESTIMATION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
                    || REGULARISATION_DEMAND_NOTE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
                    || REJECTION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
                whereQuery.append(" en.estimationnoticedate >=(cast(:formattedToDate as date)) and ");
            else
                whereQuery.append(" conndetails.workorderdate <=(cast(:formattedToDate as date)) and ");
        }

		if (isNotBlank(searchNoticeDetails.getApplicationType())) {
			if (REJECTION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
				whereQuery.append(" en.applicationType =:applicationType and ");
			else
				whereQuery.append(" applicationtype.code=:applicationType and ");
		}
        if (isNotBlank(searchNoticeDetails.getConnectionType()))
            whereQuery.append(" conndetails.connectiontype=:connectionType and ");

		if (!REJECTION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())) {
			if (REGULARISATION_DEMAND_NOTE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
				whereQuery.append(" applicationtype.code=:reglnApplicationType and ");
			else
				whereQuery.append(
						" applicationtype.code!=:reglnApplicationType and conndetails.state_id = (select id from eg_wf_states where value in (:stateValues) and status = 2 and id = conndetails.state_id) and ");
		}

        if (PROCEEDING_FOR_CLOSER_OF_CONNECTION.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
            whereQuery.append(" connectionStatus=:closureStatus and statusid=:status and ");

        if (isNotBlank(searchNoticeDetails.getHscNo()))
            whereQuery.append(" connection.consumercode=:consumerCode and ");

        if (isNotBlank(searchNoticeDetails.getAssessmentNo()))
            whereQuery.append(" connection.propertyIdentifier=:assessmentNumber and ");

        if (financialYear != null) {
            if (ESTIMATION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
                    || REGULARISATION_DEMAND_NOTE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
                    || REJECTION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
                whereQuery.append(
                        " cast(en.estimationnoticedate as date) between cast(:financialStartDate as date) and cast(:financialEndDate as date) and en.ishistory = false and ");
            else if (PROCEEDING_FOR_CLOSER_OF_CONNECTION.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
                whereQuery.append(
                        " cast(conndetails.closeapprovaldate as date) between cast(:financialStartDate as date) and cast(:financialEndDate as date) and ");
            else if (PROCEEDING_FOR_RECONNECTION.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
                whereQuery.append(
                        " cast(conndetails.reconnectionapprovaldate as date) between cast(:financialStartDate as date) and cast(:financialEndDate as date) and ");
            else
                whereQuery.append(
                        " cast(conndetails.workorderdate as date) between cast(:financialStartDate as date) and cast(:financialEndDate as date) and ");
        }

        if (ESTIMATION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
            whereQuery.append(" conndetails.executiondate is not null and ");
		if (ESTIMATION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
				|| REGULARISATION_DEMAND_NOTE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
			whereQuery.append(" en.noticeType = 'ESTIMATIONNOTICE' and ");

		if (REJECTION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
				&& (!RECONNECTION.equalsIgnoreCase(searchNoticeDetails.getApplicationType())
						&& !CLOSINGCONNECTION.equalsIgnoreCase(searchNoticeDetails.getApplicationType())))
			whereQuery.append(
					" en.noticeType = 'REJECTIONNOTICE' and conndetails.connectionstatus=:connectionStatus order by en.estimationNoticeDate desc ");
		else
			whereQuery.append(" conndetails.connectionstatus!=:connectionStatus");

		Query query = entityManager.unwrap(Session.class)
                .createSQLQuery(selectQuery.append(fromQuery).append(whereQuery).toString());
        setSearchQueryParameters(searchNoticeDetails, formattedFromDate, formattedToDate, query, financialYear);
		if (!REJECTION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))
			query.setParameter("reglnApplicationType", REGULARIZE_CONNECTION);
		if (!(REGULARISATION_DEMAND_NOTE.equalsIgnoreCase(searchNoticeDetails.getNoticeType())
				|| REJECTION_NOTICE.equalsIgnoreCase(searchNoticeDetails.getNoticeType()))){
       		query.setParameterList("stateValues", Arrays.asList(END, APPLICATIONSTATUSCLOSED));
        }
        if (PROCEEDING_FOR_CLOSER_OF_CONNECTION.equalsIgnoreCase(searchNoticeDetails.getNoticeType())) {
            query.setParameter("closureStatus", ConnectionStatus.CLOSED.toString());
            EgwStatus closureSanctionedStatus = waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CLOSERSANCTIONED,
                    MODULETYPE);
            if (closureSanctionedStatus != null)
                query.setParameter("status", closureSanctionedStatus.getId());
        }
        query.setParameter("connectionStatus", INACTIVE);

        List<Object[]> objectList = query.list();
        return getSearchNoticeList(objectList);
    }

    private Query setSearchQueryParameters(final SearchNoticeDetails searchNoticeDetails, String formattedFromDate,
            String formattedToDate, Query query, CFinancialYear financialYear) {
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
        if (financialYear != null) {
            query.setParameter("financialStartDate", financialYear.getStartingDate());
            query.setParameter("financialEndDate", financialYear.getEndingDate());
        }
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
        searchNoticeDetails.setApplicationNumber(object[10] == null ? EMPTY : object[10].toString());
        searchNoticeDetails.setEstimationNumber(object[11] == null ? EMPTY : object[11].toString());
        searchNoticeDetails.setEstimationDate(object[12] == null ? EMPTY : object[12].toString());
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

    public SearchNoticeDetails buildNoticeDetails(final WaterConnectionDetails waterConnectionDetails) {
        SearchNoticeDetails noticeDetails = new SearchNoticeDetails();
        AssessmentDetails assessmentDetails = null;
        if (waterConnectionDetails.getConnection().getPropertyIdentifier() != null)
            assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        if (assessmentDetails != null) {
            noticeDetails.setAssessmentNo(waterConnectionDetails.getConnection().getPropertyIdentifier());
            noticeDetails.setHscNo(waterConnectionDetails.getConnection().getConsumerCode());
            noticeDetails.setConnectionType(waterConnectionDetails.getConnectionType().name());
            noticeDetails.setHouseNumber(assessmentDetails.getHouseNo());
            Iterator<OwnerName> nameIterator = assessmentDetails.getOwnerNames().iterator();
            OwnerName ownerName = null;
            if (nameIterator != null && nameIterator.hasNext())
                ownerName = nameIterator.next();
            noticeDetails.setOwnerName(ownerName == null ? "N/A" : ownerName.getOwnerName());
            noticeDetails.setLocality(assessmentDetails.getBoundaryDetails().getLocalityName());
        }
        return noticeDetails;
    }
}
