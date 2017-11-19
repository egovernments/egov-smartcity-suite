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
package org.egov.ptis.client.integration.utils;

import static org.egov.ptis.constants.PropertyTaxConstants.PTIS_COLLECTION_SERVICE_CODE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillAccountDetails;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.collection.integration.models.BillDetails;
import org.egov.collection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.models.BillPayeeDetails;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.PaymentInfo;
import org.egov.collection.integration.models.PaymentInfo.TYPE;
import org.egov.collection.integration.models.PaymentInfoCard;
import org.egov.collection.integration.models.PaymentInfoCash;
import org.egov.collection.integration.models.PaymentInfoChequeDD;
import org.egov.commons.CChartOfAccounts;
import org.egov.dcb.bean.CashPayment;
import org.egov.dcb.bean.ChequePayment;
import org.egov.dcb.bean.CreditCardPayment;
import org.egov.dcb.bean.DDPayment;
import org.egov.dcb.bean.Payment;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.constants.PropertyTaxConstants;

/**
 * Performs collections operations: (1) Fetch the details of a given receipt;
 * (2) Execute a collection for a particular bill and amount.; (3) Search for
 * existing payment ref no.
 */
public class CollectionHelper {
    private static final Logger LOG = Logger.getLogger(CollectionHelper.class);
    private EgBill bill;
    private boolean isMutationFeePayment = false;

    /**
     * Use this constructor when you're only interested in getting the details
     * of a receipt.
     */
    public CollectionHelper() {
    }

    /**
     * Use this constructor when you're doing a collection.
     * 
     * @param bill
     */
    public CollectionHelper(EgBill bill) {
        this.bill = bill;
    }

    /**
     * Executes a collection.
     * 
     * @param payment
     * @return
     */
    public BillReceiptInfo executeCollection(Payment payment, String source) {
        if (!isCollectionPermitted()) {
            throw new ApplicationRuntimeException(
                    "Collection is not allowed - current balance is zero and advance coll exists.");
        }

        List<PaymentInfo> paymentInfoList = preparePaymentInfo(payment);

        LOG.debug("CollectionHelper.executeCollection(): collection is from the field...");
        BillInfoImpl billInfo = prepareBillInfo(payment.getAmount(), COLLECTIONTYPE.F, source);

        return SpringBeanUtil.getCollectionIntegrationService().createReceipt(billInfo, paymentInfoList);
    }

    public BillReceiptInfo generateMiscReceipt(Payment payment) {
        if (!isCollectionPermitted()) {
            throw new ApplicationRuntimeException("Collection is not allowed - Fully paid or excess Paid.");
        }
        List<PaymentInfo> paymentInfoList = preparePaymentInfo(payment);
        BillInfoImpl billInfo = prepareBillInfo(payment.getAmount(), COLLECTIONTYPE.C, null);
        return SpringBeanUtil.getCollectionIntegrationService().createMiscellaneousReceipt(billInfo, paymentInfoList);
    }

    /**
     * Fetches the details of a given receipt number.
     * 
     * @param receiptNumber
     * @return
     */
    public BillReceiptInfo getReceiptInfo(String receiptNumber) {
        return SpringBeanUtil.getCollectionIntegrationService().getReceiptInfo(PTIS_COLLECTION_SERVICE_CODE,
                receiptNumber);
    }

    private List<PaymentInfo> preparePaymentInfo(Payment payment) {
        List<PaymentInfo> paymentInfoList = new ArrayList<PaymentInfo>();
        PaymentInfo paymentInfo = null;
        if (payment != null) {

            if (payment instanceof ChequePayment) {
                ChequePayment chequePayment = (ChequePayment) payment;
                paymentInfo = new PaymentInfoChequeDD(chequePayment.getBankId(), chequePayment.getBranchName(),
                        chequePayment.getInstrumentDate(), chequePayment.getInstrumentNumber(), TYPE.cheque,
                        payment.getAmount());

            } else if (payment instanceof DDPayment) {
                DDPayment chequePayment = (DDPayment) payment;
                paymentInfo = new PaymentInfoChequeDD(chequePayment.getBankId(), chequePayment.getBranchName(),
                        chequePayment.getInstrumentDate(), chequePayment.getInstrumentNumber(), TYPE.dd,
                        payment.getAmount());

            } else if (payment instanceof CreditCardPayment) {
                paymentInfo = prepareCardPaymentInfo((CreditCardPayment) payment, new PaymentInfoCard());

            } else if (payment instanceof CashPayment) {
                paymentInfo = new PaymentInfoCash(payment.getAmount());
            }
        }
        paymentInfoList.add(paymentInfo);
        return paymentInfoList;
    }

    /**
     * Apportions the paid amount amongst the appropriate GL codes and returns
     * the collections object that can be sent to the collections API for
     * processing.
     * 
     * @param bill
     * @param amountPaid
     * @return
     */
    public BillInfoImpl prepareBillInfo(BigDecimal amountPaid, COLLECTIONTYPE collType, String source) {
        BillInfoImpl billInfoImpl = initialiseFromBill(amountPaid, collType);

        ArrayList<ReceiptDetail> receiptDetails = new ArrayList<ReceiptDetail>();
        List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>(bill.getEgBillDetails());
        Collections.sort(billDetails);

        if(isMutationFeePayment){
        	for (EgBillDetails billDet : billDetails) {
	            receiptDetails.add(initReceiptDetail(billDet.getGlcode(),
	                    billDet.getCrAmount(),
	                    billDet.getCrAmount().subtract(billDet.getDrAmount()), billDet.getDrAmount(),
	                    billDet.getDescription(), billDet.getPurpose()));
	        }
        }else{
	        for (EgBillDetails billDet : billDetails) {
	            receiptDetails.add(initReceiptDetail(billDet.getGlcode(),
	                    BigDecimal.ZERO, // billDet.getCrAmount(),
	                    billDet.getCrAmount().subtract(billDet.getDrAmount()), billDet.getDrAmount(),
	                    billDet.getDescription(), billDet.getPurpose()));
	        }
        	SpringBeanUtil.getPropertyTaxCollection().apportionPaidAmount(String.valueOf(bill.getId()), amountPaid,
        			receiptDetails);
        }
        
        boolean isActualDemand = false;
        for (EgBillDetails billDet : bill.getEgBillDetails()) {
            for (ReceiptDetail rd : receiptDetails) {
                if ((billDet.getGlcode().equals(rd.getAccounthead().getGlcode()))
                        && (billDet.getDescription().equals(rd.getDescription()))) {
                    isActualDemand = billDet.getAdditionalFlag() == 1 ? true : false;
                    BillAccountDetails billAccDetails = new BillAccountDetails(billDet.getGlcode(),
                            billDet.getOrderNo(), rd.getCramount(), rd.getDramount(), billDet.getFunctionCode(),
                            billDet.getDescription(), isActualDemand, PURPOSE.valueOf(billDet.getPurpose()));
                    billInfoImpl.getPayees().get(0).getBillDetails().get(0).addBillAccountDetails(billAccDetails);
                    break;
                }
            }
        }
        billInfoImpl.setTransactionReferenceNumber(bill.getTransanctionReferenceNumber());
        billInfoImpl.setSource(source != null ? source : "");
        return billInfoImpl;
    }

    /**
     * Populates a BillInfo object from the bill -- the GL codes, descripion and
     * dr/cr amounts.
     * 
     * @param bill
     * @return
     */
    private BillInfoImpl initialiseFromBill(BigDecimal amountPaid, COLLECTIONTYPE collType) {
        BillInfoImpl billInfoImpl = null;
        BillPayeeDetails billPayeeDet = null;
        List<BillPayeeDetails> billPayeeDetList = new ArrayList<BillPayeeDetails>();
        List<String> collModesList = new ArrayList<String>();
        String[] collModes = bill.getCollModesNotAllowed().split(",");
        for (String coll : collModes) {
            collModesList.add(coll);
        }
        billInfoImpl = new BillInfoImpl(bill.getServiceCode(), bill.getFundCode(), bill.getFunctionaryCode(),
                bill.getFundSourceCode(), bill.getDepartmentCode(), bill.getDisplayMessage(), bill.getCitizenName(),
                bill.getPartPaymentAllowed(), bill.getOverrideAccountHeadsAllowed(), collModesList, collType);
        billPayeeDet = new BillPayeeDetails(bill.getCitizenName(), bill.getCitizenAddress(), bill.getEmailId());

        BillDetails billDetails = new BillDetails(bill.getId().toString(), bill.getCreateDate(), bill.getConsumerId(),bill.getConsumerType(),
                bill.getBoundaryNum().toString(), bill.getBoundaryType(), bill.getDescription(), amountPaid, // the
                                                                                                             // actual
                                                                                                             // amount
                                                                                                             // paid,
                                                                                                             // which
                                                                                                             // might
                                                                                                             // include
                                                                                                             // advances
                bill.getMinAmtPayable());
        billPayeeDet.addBillDetails(billDetails);
        billPayeeDetList.add(billPayeeDet);
        billInfoImpl.setPayees(billPayeeDetList);
        return billInfoImpl;
    }

    private ReceiptDetail initReceiptDetail(String glCode, BigDecimal crAmount, BigDecimal crAmountToBePaid,
            BigDecimal drAmount, String description, String purpose) {
        ReceiptDetail receiptDetail = new ReceiptDetail();
        CChartOfAccounts accountHead = new CChartOfAccounts();
        accountHead.setGlcode(glCode);
        receiptDetail.setAccounthead(accountHead);
        receiptDetail.setDescription(description);
        receiptDetail.setCramount(crAmount);
        receiptDetail.setCramountToBePaid(crAmountToBePaid);
        receiptDetail.setDramount(drAmount);
        receiptDetail.setPurpose(purpose);
        return receiptDetail;
    }

    private PaymentInfoCard prepareCardPaymentInfo(CreditCardPayment cardPayment, PaymentInfoCard paymentInfoCard) {
        paymentInfoCard.setInstrumentNumber(cardPayment.getCreditCardNo());
        paymentInfoCard.setInstrumentAmount(cardPayment.getAmount());
        paymentInfoCard.setExpMonth(cardPayment.getExpMonth());
        paymentInfoCard.setExpYear(cardPayment.getExpYear());
        paymentInfoCard.setCvvNumber(cardPayment.getCvv());
        paymentInfoCard.setCardTypeValue(cardPayment.getCardType());
        paymentInfoCard.setTransactionNumber(cardPayment.getTransactionNumber());
        return paymentInfoCard;
    }

    private boolean isCollectionPermitted() {
        boolean allowed = thereIsBalanceToBePaid();
        LOG.debug("isCollectionPermitted() returned: " + allowed);
        return allowed;
    }

    private boolean thereIsBalanceToBePaid() {
        boolean result = false;
        BigDecimal balance = BigDecimal.ZERO;
        for (EgBillDetails bd : bill.getEgBillDetails()) {
            balance = balance.add(bd.balance());
            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean thereIsCurrentBalanceToBePaid() {
        boolean result = false;
        BigDecimal currentBal = BigDecimal.ZERO;
        for (Map.Entry<String, String> entry : PropertyTaxConstants.GLCODEMAP_FOR_CURRENTTAX.entrySet()) {
            currentBal = currentBal.add(bill.balanceForGLCode(entry.getValue()));
        }
        if (currentBal != null && currentBal.compareTo(BigDecimal.ZERO) > 0) {
            result = true;
        }
        return result;
    }

    EgBill getBill() {
        return bill;
    }

    void setBill(EgBill bill) {
        this.bill = bill;
    }

	public boolean getIsMutationFeePayment() {
		return isMutationFeePayment;
	}

	public void setIsMutationFeePayment(boolean isMutationFeePayment) {
		this.isMutationFeePayment = isMutationFeePayment;
	}

}
