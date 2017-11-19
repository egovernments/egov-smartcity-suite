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

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.instrument.InstrumentService;
import org.egov.wtms.application.entity.DailyWTCollectionReport;
import org.egov.wtms.application.entity.DefaultersReport;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DailyWTCollectionReportService {

    @Autowired
    public InstrumentService instrumentService;

    @Autowired
    public AssignmentService assignmentService;

    @Autowired
    public AppConfigValueService appConfigValueService;

    @Autowired
    public EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    public WaterConnectionDetailsService waterConnectionDetailsService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Set<User> getUsers() {
        final String operatorDesignation = appConfigValueService
                .getAppConfigValueByDate(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                        CollectionConstants.COLLECTION_DESIGNATIONFORCSCOPERATOR, new Date())
                .getValue();
        return assignmentService.getUsersByDesignations(operatorDesignation.split(","));
    }

    public List<EgwStatus> getStatusByModule() {
        return egwStatusHibernateDAO.getStatusByModule(CollectionConstants.MODULE_NAME_RECEIPTHEADER);
    }

    public List<DailyWTCollectionReport> getCollectionDetails(final Date fromDate, final Date toDate,
            final String collectionMode, final String collectionOperator, final String status) throws ParseException {
        final StringBuilder queryStr = new StringBuilder(500);

        queryStr.append(
                "select distinct receiptheader from ReceiptHeader receiptheader inner join fetch receiptheader.receiptInstrument instHeader"
                        + " inner join fetch instHeader.instrumentType instType where receiptheader.service.name =:service and (receiptdate between :fromDate and :toDate) ");
        if (StringUtils.isNotBlank(collectionMode))
            queryStr.append(" and receiptheader.source =:mode ");
        if (StringUtils.isNotBlank(collectionOperator))
            queryStr.append(" and receiptheader.createdBy.id =:operator ");
        if (StringUtils.isNotBlank(status))
            queryStr.append(" and receiptheader.status.id =:status ");
        queryStr.append(" order by instHeader ");
        final Query query = getCurrentSession().createQuery(queryStr.toString());
        query.setString("service", WaterTaxConstants.EGMODULES_NAME);
        query.setDate("fromDate", new DateTime(fromDate).withTimeAtStartOfDay().toDate());
        query.setDate("toDate", new DateTime(toDate).plusDays(1).toDate());
        if (StringUtils.isNotBlank(collectionMode))
            query.setString("mode", collectionMode);
        if (StringUtils.isNotBlank(collectionOperator))
            query.setLong("operator", Long.valueOf(collectionOperator));
        if (StringUtils.isNotBlank(status))
            query.setLong("status", Long.valueOf(status));
        final List<ReceiptHeader> receiptHeaderList = query.list();
        final List<DailyWTCollectionReport> dailyWTCollectionReportList = new ArrayList<DailyWTCollectionReport>(0);

        for (final ReceiptHeader receiptHeader : receiptHeaderList) {
            BigDecimal currCollection = BigDecimal.ZERO;
            BigDecimal arrCollection = BigDecimal.ZERO;
            final DailyWTCollectionReport result = new DailyWTCollectionReport();
            result.setReceiptNumber(receiptHeader.getReceiptnumber());
            result.setReceiptDate(receiptHeader.getReceiptdate());
            result.setConsumerCode(receiptHeader.getConsumerCode());
            result.setConsumerName(receiptHeader.getPayeeName());
            result.setPaidAt(receiptHeader.getSource());
            final WaterConnectionDetails waterConnection = waterConnectionDetailsService
                    .findByApplicationNumberOrConsumerCode(receiptHeader.getConsumerCode());
            if (null != waterConnection)
                result.setConnectionType(waterConnection.getConnectionType().toString());
            final StringBuilder queryString = new StringBuilder();
            queryString
                    .append("select wardboundary.name as \"wardName\",dcbinfo.houseno as \"houseNo\" from egwtr_mv_dcb_view dcbinfo"
                            + " INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id  where dcbinfo.hscno = '"
                            + receiptHeader.getConsumerCode() + "'");
            final SQLQuery finalQuery = getCurrentSession().createSQLQuery(queryString.toString());
            finalQuery.setResultTransformer(new AliasToBeanResultTransformer(DefaultersReport.class));
            List<DefaultersReport> listforWardAndHsc = new ArrayList<DefaultersReport>();
            listforWardAndHsc = finalQuery.list();
            if (!listforWardAndHsc.isEmpty()) {
                result.setDoorNumber(listforWardAndHsc.get(0).getHouseNo());
                result.setWardName(listforWardAndHsc.get(0).getWardName());
            }
            result.setTotal(receiptHeader.getTotalAmount());
            result.setStatus(receiptHeader.getStatus().getDescription());
            if ("CANCELLED".equalsIgnoreCase(receiptHeader.getStatus().getCode()))
                result.setCancellationDetails(receiptHeader.getReasonForCancellation());
            else
                result.setCancellationDetails("N/A");
            final StringBuilder paymentMode = new StringBuilder(30);
            int count = 0;
            for (final InstrumentHeader instrument : receiptHeader.getReceiptInstrument()) {
                final int instrumentSize = receiptHeader.getReceiptInstrument().size();
                paymentMode.append(instrument.getInstrumentType().getType());
                if (instrumentSize > 1 && count < instrumentSize - 1) {
                    paymentMode.append(",");
                    count++;
                }
            }
            result.setPaidAt(receiptHeader.getSource());
            result.setPaymentMode(paymentMode.toString());
            final List<ReceiptDetail> receiptDetailsList = new ArrayList<ReceiptDetail>(
                    receiptHeader.getReceiptDetails());
            final int lastindex = receiptDetailsList.size() - 2;
            if (null != receiptDetailsList.get(0).getDescription()) {
                final int index = receiptDetailsList.get(0).getDescription().indexOf("-");
                final int hashIndex = receiptDetailsList.get(0).getDescription().indexOf("#");
                final String instDesc = receiptDetailsList.get(0).getDescription().substring(index + 1, hashIndex);
                result.setFromInstallment(instDesc);
            }
            if (null != receiptDetailsList.get(lastindex).getDescription()) {
                final int index = receiptDetailsList.get(lastindex).getDescription().indexOf("-");
                final int hashIndex = receiptDetailsList.get(lastindex).getDescription().indexOf("#");
                final String instDesc = receiptDetailsList.get(lastindex).getDescription().substring(index + 1,
                        hashIndex);
                result.setToInstallment(instDesc);
            }
            for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
                final String rdesc = receiptDetail.getDescription();
                if (null != rdesc) {
                    final String receiptDmdRsnDesc = rdesc.substring(0, receiptDetail.getDescription().indexOf("-"))
                            .trim();
                    String currentInstallment = null;
                    if (Arrays.asList(WaterTaxConstants.CREATECONNECTIONDMDDESC).contains(receiptDmdRsnDesc))
                        currentInstallment = connectionDemandService.getCurrentInstallment(
                                WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.YEARLY, new Date()).getDescription();
                    else if (Arrays.asList(WaterTaxConstants.WATERCHARGESDMDDESC).contains(receiptDmdRsnDesc))
                        if (ConnectionType.METERED.equals(waterConnection.getConnectionType()))
                            currentInstallment = connectionDemandService
                                    .getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.MONTHLY,
                                            new Date())
                                    .getDescription();
                        else if (ConnectionType.NON_METERED.equals(waterConnection.getConnectionType()))
                            currentInstallment = connectionDemandService
                                    .getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null,
                                            new Date())
                                    .getDescription();

                    if (null != rdesc && rdesc.substring(rdesc.indexOf("-") + 1, rdesc.indexOf("#")).trim()
                            .equals(currentInstallment))
                        currCollection = currCollection.add(receiptDetail.getCramount());
                    else if (null != rdesc && !rdesc.substring(rdesc.indexOf("-") + 1, rdesc.indexOf("#")).trim()
                            .equals(currentInstallment))
                        arrCollection = arrCollection.add(receiptDetail.getCramount());
                }
            }
            result.setArrearTotal(null != arrCollection ? arrCollection : new BigDecimal(0));
            result.setCurrentTotal(currCollection);
            result.setTotal(currCollection.add(arrCollection));
            dailyWTCollectionReportList.add(result);
        }

        return dailyWTCollectionReportList;
    }

    public Map<String, String> getCollectionModeMap() {
        final Map<String, String> collectionModeMap = new LinkedHashMap<String, String>(0);
        collectionModeMap.put(Source.ESEVA.toString(), Source.ESEVA.toString());
        collectionModeMap.put(Source.MEESEVA.toString(), Source.MEESEVA.toString());
        collectionModeMap.put(Source.APONLINE.toString(), Source.APONLINE.toString());
        collectionModeMap.put(Source.SOFTTECH.toString(), Source.SOFTTECH.toString());
        collectionModeMap.put(Source.SYSTEM.toString(), Source.SYSTEM.toString());
        return collectionModeMap;
    }
}
