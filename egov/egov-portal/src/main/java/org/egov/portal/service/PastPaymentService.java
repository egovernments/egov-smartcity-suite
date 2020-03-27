/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.portal.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.portal.entity.SearchPastPaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PastPaymentService {

    private static final String PAYMENT_SEARCH_QUERY = "select distinct receipt from org.egov.collection.entity.ReceiptHeader receipt where receipt.status.code not in (:pending,:failed) ";
    private static final String ORDER_BY_CLAUSE = "  order by receipt.receiptdate desc";
    private static final String RECEIPT_STATUS_CODE_FAILED = "FAILED";
    private static final String RECEIPT_STATUS_CODE_PENDING = "PENDING";
    private static final int DEFAULT_RESULT_SIZE = 3;
    private static final int MAX_RESULT_SIZE = 100;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SecurityUtils securityUtils;

    @SuppressWarnings("unchecked")
    public List<Object> findPastPaymentsByCitizenUser() {
        final StringBuilder paymentSearchQuery = new StringBuilder(PAYMENT_SEARCH_QUERY);
        paymentSearchQuery.append(" and receipt.createdBy.id = :userId ").append(ORDER_BY_CLAUSE);
        final javax.persistence.Query qry = entityManager.createQuery(paymentSearchQuery.toString());
        qry.setParameter("pending", RECEIPT_STATUS_CODE_PENDING);
        qry.setParameter("failed", RECEIPT_STATUS_CODE_FAILED);
        qry.setParameter("userId", securityUtils.getCurrentUser().getId());
        return qry.setMaxResults(DEFAULT_RESULT_SIZE).getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<Object> findAllPastPaymentsByCitizenUser(final SearchPastPaymentRequest pastPaymentRequest) {
        final javax.persistence.Query qry = entityManager.createQuery(getPaymentSearchQuery(pastPaymentRequest));
        qry.setParameter("pending", RECEIPT_STATUS_CODE_PENDING);
        qry.setParameter("failed", RECEIPT_STATUS_CODE_FAILED);
        qry.setParameter("serviceName", pastPaymentRequest.getServiceName());
        if (pastPaymentRequest.getFromDate() != null)
            qry.setParameter("fromDate", pastPaymentRequest.getFromDate());
        if (pastPaymentRequest.getToDate() != null)
            qry.setParameter("toDate", DateUtils.add(pastPaymentRequest.getToDate(), Calendar.DATE, 1));
        qry.setParameter("userId", securityUtils.getCurrentUser().getId());
        return qry.setMaxResults(MAX_RESULT_SIZE).getResultList();

    }

    private String getPaymentSearchQuery(SearchPastPaymentRequest pastPaymentRequest) {
        final List<Object> params = new ArrayList<>();
        final StringBuilder searchQueryString = new StringBuilder(PAYMENT_SEARCH_QUERY);

        searchQueryString.append(" and receipt.service.name = :serviceName ");

        if (pastPaymentRequest.getFromDate() != null) {
            searchQueryString.append(" and receipt.receiptdate >= :fromDate ");
            params.add(pastPaymentRequest.getFromDate());
        }
        if (pastPaymentRequest.getToDate() != null) {
            searchQueryString.append(" and receipt.receiptdate < :toDate ");
            params.add(DateUtils.add(pastPaymentRequest.getToDate(), Calendar.DATE, 1));
        }
        searchQueryString.append(" and receipt.createdBy.id = :userId ");

        return searchQueryString.append(ORDER_BY_CLAUSE).toString();

    }

}
