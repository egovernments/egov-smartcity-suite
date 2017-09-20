/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.adtax.service.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillAccountDetails;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.collection.integration.models.BillDetails;
import org.egov.collection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.models.BillPayeeDetails;
import org.egov.commons.CChartOfAccounts;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdtaxExternalService {

    @Autowired
    private AdvertisementTaxCollection advertisementTaxCollection;

    /**
     * Apportions the paid amount amongst the appropriate GL codes and returns the collections object that can be sent to the
     * collections API for processing.
     *
     * @param bill
     * @param amountPaid
     * @return
     */
    public BillInfoImpl prepareBillInfo(final BigDecimal amountPaid, final COLLECTIONTYPE collType, final EgBill bill,
            final String source) {
        final BillInfoImpl billInfoImpl = initialiseFromBill(amountPaid, collType, bill);

        final ArrayList<ReceiptDetail> receiptDetails = new ArrayList<>(0);
        final List<EgBillDetails> billDetails = new ArrayList<>(bill.getEgBillDetails());
        Collections.sort(billDetails);
        for (final EgBillDetails billDet : billDetails)
            receiptDetails.add(initReceiptDetail(billDet.getGlcode(), BigDecimal.ZERO, // billDet.getCrAmount(),
                    billDet.getCrAmount(), billDet.getDrAmount(), billDet.getDescription()));
        Boolean isActualDemand = false;
        advertisementTaxCollection.apportionPaidAmount(String.valueOf(bill.getId()), amountPaid,
                receiptDetails);

        for (final EgBillDetails billDet : bill.getEgBillDetails())
            for (final ReceiptDetail rd : receiptDetails)
                if (billDet.getGlcode().equals(rd.getAccounthead().getGlcode())
                        && billDet.getDescription().equals(rd.getDescription())) {
                    isActualDemand = billDet.getAdditionalFlag() == 1 ? true : false;
                    BillAccountDetails billAccDetails;
                    billAccDetails = new BillAccountDetails(billDet.getGlcode(), billDet.getOrderNo(), rd.getCramount(),
                            rd.getDramount(), billDet.getFunctionCode(), billDet.getDescription(), isActualDemand,
                            PURPOSE.valueOf(billDet.getPurpose()));
                    billInfoImpl.getPayees().get(0).getBillDetails().get(0).addBillAccountDetails(billAccDetails);
                    break;
                }
        billInfoImpl.setTransactionReferenceNumber(bill.getTransanctionReferenceNumber());
        billInfoImpl.setSource(source);
        return billInfoImpl;
    }

    /**
     * Populates a BillInfo object from the bill -- the GL codes, descripion and dr/cr amounts.
     * 
     * @param bill
     * @return
     */
    private BillInfoImpl initialiseFromBill(final BigDecimal amountPaid, final COLLECTIONTYPE collType,
            final EgBill bill) {
        BillInfoImpl billInfoImpl = null;
        BillPayeeDetails billPayeeDet = null;
        final List<BillPayeeDetails> billPayeeDetList = new ArrayList<>(0);
        final List<String> collModesList = new ArrayList<>();
        final String[] collModes = bill.getCollModesNotAllowed().split(",");
        for (final String coll : collModes)
            collModesList.add(coll);
        billInfoImpl = new BillInfoImpl(bill.getServiceCode(), bill.getFundCode(), bill.getFunctionaryCode(),
                bill.getFundSourceCode(), bill.getDepartmentCode(), "Advertisement Tax Collection", bill.getCitizenName(),
                bill.getPartPaymentAllowed(), bill.getOverrideAccountHeadsAllowed(), collModesList, collType);
        billPayeeDet = new BillPayeeDetails(bill.getCitizenName(), bill.getCitizenAddress(), bill.getEmailId());

        final BillDetails billDetails = new BillDetails(bill.getId().toString(), bill.getCreateDate(),
                bill.getConsumerId(), bill.getConsumerType(), bill.getBoundaryNum().toString(), bill.getBoundaryType(),
                bill.getDescription(),
                amountPaid, // the actual amount paid, which might include
                // advances
                bill.getMinAmtPayable());
        billPayeeDet.addBillDetails(billDetails);
        billPayeeDetList.add(billPayeeDet);
        billInfoImpl.setPayees(billPayeeDetList);
        return billInfoImpl;
    }

    private ReceiptDetail initReceiptDetail(final String glCode, final BigDecimal crAmount,
            final BigDecimal crAmountToBePaid, final BigDecimal drAmount, final String description) {
        final ReceiptDetail receiptDetail = new ReceiptDetail();
        final CChartOfAccounts accountHead = new CChartOfAccounts();
        accountHead.setGlcode(glCode);
        receiptDetail.setAccounthead(accountHead);
        receiptDetail.setDescription(description);
        receiptDetail.setCramount(crAmount);
        receiptDetail.setCramountToBePaid(crAmountToBePaid);
        receiptDetail.setDramount(drAmount);
        return receiptDetail;
    }

}
