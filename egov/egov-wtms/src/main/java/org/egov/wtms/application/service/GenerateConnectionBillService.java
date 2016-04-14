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
package org.egov.wtms.application.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.demand.model.EgBill;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GenerateConnectionBillService {

    @PersistenceContext
    private EntityManager entityManager;

    @Qualifier("entityQueryService")
    private @Autowired PersistenceService entityQueryService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<GenerateConnectionBill> getBillReportDetails(final String zone, final String ward,
            final String propertyType, final String applicationType, final String connectionType,
            final String consumerCode, final String houseNumber, final String assessmentNumber) throws ParseException {

        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select dcbinfo.hscno as \"hscNo\", dcbinfo.username as \"ownerName\",dcbinfo.propertyid as \"assementNo\","
                + "dcbinfo.houseno as \"houseNumber\" , localboundary.localname as \"locality\",  "
                + " dcbinfo.connectiontype as  \"connectionType\" from egwtr_mv_dcb_view dcbinfo"
                + " INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id INNER JOIN eg_boundary localboundary on"
                + " dcbinfo.locality = localboundary.id  INNER JOIN eg_boundary zoneboundary on dcbinfo.zoneid = zoneboundary.id ");
        queryStr.append(" where dcbinfo.connectionstatus = 'ACTIVE' ");
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

        final SQLQuery finalQuery = getCurrentSession().createSQLQuery(queryStr.toString());
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(GenerateConnectionBill.class));
        List<GenerateConnectionBill> generateConnectionBillList = new ArrayList<GenerateConnectionBill>();
        generateConnectionBillList = finalQuery.list();
        return generateConnectionBillList;
    }

    public List<GenerateConnectionBill> getBillData(final List<GenerateConnectionBill> generateConnectionBillList) {

        for (final GenerateConnectionBill connectionbill : generateConnectionBillList) {
            final EgBill egbill = getBIll(connectionbill.getHscNo());
            if (egbill != null) {
                connectionbill.setBillNo(egbill.getBillNo());
                connectionbill.setBillDate(egbill.getIssueDate().toString());
            }
        }
        return generateConnectionBillList;
    }

    public EgBill getBIll(final String consumerCode) {
        final String query = " select bill From EgBill bill,EgBillType billtype,WaterConnection conn,WaterConnectionDetails connDet,EgwStatus status,WaterDemandConnection conndem "
                + "where billtype.id=bill.egBillType and billtype.code='MANUAL' and bill.consumerId = conn.consumerCode and conn.id=connDet.connection and connDet.id=conndem.waterConnectionDetails and bill.egDemand=conndem.demand and connDet.connectionType='NON_METERED' and "
                + "connDet.connectionStatus='ACTIVE' and connDet.status=status.id and status.moduletype='WATERTAXAPPLICATION' and status.code='SANCTIONED' "
                + "and conn.consumerCode = ? ";
        final EgBill egBill = (EgBill) entityQueryService.find(query, consumerCode);
        return egBill;
    }

    public List<Long> getDocuments(final String consumerCode, final String applicationType) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select filestore.filestoreid from eg_filestoremap filestore,egwtr_documents conndoc,egwtr_application_documents appD,egwtr_connectiondetails conndet,egwtr_connection "
                + "conn,egwtr_document_names docNmae where filestore.id=conndoc.filestoreid and conndet.connection=conn.id and conndet.id=appD.connectiondetailsid and appD.documentnamesid=docNmae.id"
                + " and conndoc.applicationdocumentsid=appD.id and docNmae.documentname='DemandBill' " + " ");
        queryStr.append(" and conn.consumercode=  " + "'" + consumerCode + "'");
        queryStr.append(" and docNmae.applicationtype in(select id from egwtr_application_type where code = '"
                + applicationType + "' )");

        final SQLQuery finalQuery = getCurrentSession().createSQLQuery(queryStr.toString());
        final List<Long> waterChargesDocumentsList = finalQuery.list();
        return waterChargesDocumentsList;
    }

}
