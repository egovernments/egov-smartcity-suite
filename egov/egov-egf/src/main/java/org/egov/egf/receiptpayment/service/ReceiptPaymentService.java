/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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

package org.egov.egf.receiptpayment.service;

import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.model.receiptpayment.ReceiptPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReceiptPaymentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CFinancialYearService cFinancialYearService;


    public List<ReceiptPayment> search(ReceiptPayment receiptPayment) {


        StringBuilder queryStr = new StringBuilder(500);
        queryStr = prepareQuery(receiptPayment, queryStr);
        Query query = getResulQuery(receiptPayment, queryStr);
        List<Object[]> list = query.getResultList();
        List<ReceiptPayment> receiptPaymentList = new ArrayList<>();
        List<Object> obList = getOpeningBalance(receiptPayment).getResultList();

        BigDecimal receiptAmountSum = BigDecimal.ZERO;
        BigDecimal paymentAmountSum = BigDecimal.ZERO;
        BigDecimal openingBalnce = BigDecimal.ZERO;
        BigDecimal closingBalnce = BigDecimal.ZERO;
        ReceiptPayment receipt = new ReceiptPayment();
        obList.forEach(element -> {
            receipt.setCreditAmount(element != null ? BigDecimal.valueOf(Double.parseDouble(element.toString())) : BigDecimal.ZERO);
        });


        openingBalnce = receipt.getCreditAmount();
        receipt.setCreditAmount(BigDecimal.ZERO);
        receipt.setGlcode("Opening Balance");
        receipt.setName("Cash and Bank balance");
        if (openingBalnce.compareTo(BigDecimal.ZERO) >= 0) {
            receipt.setCreditAmount(openingBalnce);
        } else {
            receipt.setDebitAmount(openingBalnce);
        }
        receiptPaymentList.add(receipt);

        list.forEach(element -> {
            ReceiptPayment rp = new ReceiptPayment();

            rp.setGlcode(String.valueOf(element[0] != null ? element[0].toString() : ""));
            rp.setName(element[1] != null ? element[1].toString() : "");
            rp.setDebitAmount(element[2] != null ? BigDecimal.valueOf(Double.parseDouble(element[2].toString())) : BigDecimal.ZERO);
            rp.setCreditAmount(element[3] != null ? BigDecimal.valueOf(Double.parseDouble(element[3].toString())) : BigDecimal.ZERO);
            receiptPaymentList.add(rp);
        });

        for (ReceiptPayment r : receiptPaymentList) {
            if (!r.getGlcode().equalsIgnoreCase("Opening Balance")) {
                receiptAmountSum = receiptAmountSum.add(r.getCreditAmount());
                paymentAmountSum = paymentAmountSum.add(r.getDebitAmount());
            }
        }

        closingBalnce = openingBalnce.add((receiptAmountSum.subtract(paymentAmountSum)));
        ReceiptPayment payment = new ReceiptPayment();

        payment.setGlcode("Closing Balance");
        if (closingBalnce.compareTo(BigDecimal.ZERO) > 0) {
            payment.setCreditAmount(closingBalnce);
        } else {
            payment.setDebitAmount(closingBalnce);
        }
        receiptPaymentList.add(payment);

        return receiptPaymentList;
    }

    private StringBuilder prepareQuery(ReceiptPayment receiptPayment, StringBuilder queryStr) {
        CFinancialYear financialYear = new CFinancialYear();
        if (receiptPayment.getFinancialYear() != null) {
            financialYear = cFinancialYearService.findOne(receiptPayment.getFinancialYear().getId());
        }
        if (receiptPayment.getFund() != null) {
            queryStr.append(" and v.fundid =:fundId ");

        }
        if (financialYear != null && receiptPayment.getPeriod().toString().equalsIgnoreCase("YEARLY")) {
            queryStr.append(" and v.voucherDate >=:startDate and v.voucherDate <=:endDate ");
            receiptPayment.setFromDate(financialYear.getStartingDate());
            receiptPayment.setToDate(financialYear.getEndingDate());

        } else if (receiptPayment.getFromDate() != null && receiptPayment.getToDate() != null) {
            queryStr.append(" and v.voucherDate >=:startDate and v.voucherDate <=:endDate ");
        }


        return queryStr;
    }

    private Query getResulQuery(ReceiptPayment receiptPayment, StringBuilder queryStr) {


        StringBuilder query = new StringBuilder(500);
        query.append("select rp.glcode,rp.name,sum(rp.debitAmount) as debitAmount,sum(rp.creditAmount) as creditAmount from (select  " +
                "g.glcode as glcode,c.name as name ,0 as debitAmount," +
                "g.creditAmount as creditAmount from GeneralLedger g" +
                ",VoucherHeader v,ChartofAccounts c where g.voucherHeaderId=v.id" +
                " and v.status=0 " + queryStr +
                " and v.type='Receipt' and c.id=g.glcodeId  and c.majorcode!='450' ");
        query.append(" Union select  g.glcode as glcode,c.name as name ,g.debitAmount as debitAmount," +
                "0 as creditAmount from GeneralLedger g" +
                ",VoucherHeader v,ChartofAccounts c where g.voucherHeaderId=v.id" +
                " and v.status=0 " + queryStr +
                "and v.type='Payment' and c.id=g.glcodeId  and c.majorcode!='450') as rp group by rp.glcode,rp.name order by rp.glcode");

        return entityManager.createNativeQuery(query.toString()).setParameter("fundId", receiptPayment.getFund().getId())
                .setParameter("startDate", receiptPayment.getFromDate()).setParameter("endDate", receiptPayment.getToDate());
    }

    private Query getOpeningBalance(ReceiptPayment receiptPayment) {
        StringBuilder query = new StringBuilder(500);
        query.append("select sum(openingdebitbalance-openingcreditbalance) as openingBalance from transactionsummary where glcodeid " +
                "in(select id from chartofaccounts where majorcode ='450') and financialyearid=:financialyearId and fundid=:fundId");

        return entityManager.createNativeQuery(query.toString())
                .setParameter("financialyearId", receiptPayment.getFinancialYear().getId()).setParameter("fundId", receiptPayment.getFund().getId());
    }


}