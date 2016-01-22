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
package org.egov.collection.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.collection.entity.OnlinePaymentResult;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;

@Service
public class CollectionReportService {

    @PersistenceContext
    EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(CollectionReportService.class);

    public SQLQuery getOnlinePaymentReportData(final String districtName, final String ulbName, final String fromDate,
            final String toDate, final String transactionId) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append("select * from public.onlinepayment_view opv where 1=1");

        if (StringUtils.isNotBlank(districtName))
            queryStr.append(" and opv.districtName=:districtName ");
        if (StringUtils.isNotBlank(ulbName))
            queryStr.append(" and opv.ulbName=:ulbName ");
        if (StringUtils.isNotBlank(fromDate))
            queryStr.append(" and opv.transactiondate>=:fromDate ");
        if (StringUtils.isNotBlank(toDate))
            queryStr.append(" and opv.transactiondate<=:toDate ");
        if (StringUtils.isNotBlank(transactionId))
            queryStr.append(" and opv.transactionnumber like :transactionnumber ");
        queryStr.append(" order by receiptdate desc ");

        final SQLQuery query = entityManager.unwrap(Session.class).createSQLQuery(queryStr.toString());

        if (StringUtils.isNotBlank(districtName))
            query.setString("districtName", districtName);
        if (StringUtils.isNotBlank(ulbName))
            query.setString("ulbName", ulbName);
        try {
            if (StringUtils.isNotBlank(fromDate))
                query.setDate("fromDate", dateFormatter.parse(fromDate));
            if (StringUtils.isNotBlank(toDate))
                query.setDate("toDate", dateFormatter.parse(toDate));
        } catch (final ParseException e) {
            LOGGER.error("Exception parsing Date" + e.getMessage());
        }
        if (StringUtils.isNotBlank(transactionId))
            query.setString("transactionnumber",  "%"+transactionId+"%");
        queryStr.append(" order by opv.receiptdate desc");
        query.setResultTransformer(new AliasToBeanResultTransformer(OnlinePaymentResult.class));
        return query;
    }

    public List getUlbNames(String districtName) {
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append("select distinct ulbname from public.onlinepayment_view opv where 1=1");
        if (StringUtils.isNotBlank(districtName))
            queryStr.append(" and opv.districtName=:districtName ");
        final SQLQuery query = entityManager.unwrap(Session.class).createSQLQuery(queryStr.toString());
        if (StringUtils.isNotBlank(districtName))
            query.setString("districtName", districtName);
        return query.list();
    }

    public List getDistrictNames() {
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append("select distinct districtname from public.onlinepayment_view");
        final SQLQuery query = entityManager.unwrap(Session.class).createSQLQuery(queryStr.toString());
        return query.list();
    }

}
