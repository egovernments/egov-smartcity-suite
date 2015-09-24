package org.egov.collection.integration.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseAXIS")
public class ResponseAXIS {
        @XmlAttribute
    protected String transactionId;
        @XmlAttribute
    protected String paymentId;
        @XmlAttribute
    protected String amount;
        @XmlAttribute
    protected String dateTime;
        @XmlAttribute
    protected String mode;
        @XmlAttribute
    protected String referenceNo;
        @XmlAttribute
    protected String transactionType;
        @XmlAttribute
    protected String status;
        @XmlAttribute
    protected String isFlagged;
        @XmlAttribute
    protected String errorCode;
        @XmlAttribute
    protected String error;
        
        @Override
        public String toString() {
                // TODO Auto-generated method stub
                String responseHdfc ="transactionId="+transactionId+";referenceNo="+referenceNo+";errorCode="+errorCode+";paymentId="+paymentId
                                +";amount="+amount+";dateTime="+dateTime+";transactionType="+transactionType+";status="+status+";isFlagged="+isFlagged
                                +";error="+error;
                return responseHdfc;
        }
        /**
         * @return the transactionId
         */
        public String getTransactionId() {
                return transactionId;
        }
        /**
         * @param transactionId the transactionId to set
         */
        public void setTransactionId(String transactionId) {
                this.transactionId = transactionId;
        }
        /**
         * @return the paymentId
         */
        public String getPaymentId() {
                return paymentId;
        }
        /**
         * @param paymentId the paymentId to set
         */
        public void setPaymentId(String paymentId) {
                this.paymentId = paymentId;
        }
        /**
         * @return the amount
         */
        public String getAmount() {
                return amount;
        }
        /**
         * @param amount the amount to set
         */
        public void setAmount(String amount) {
                this.amount = amount;
        }
        /**
         * @return the dateTime
         */
        public String getDateTime() {
                return dateTime;
        }
        /**
         * @param dateTime the dateTime to set
         */
        public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
        }
        /**
         * @return the mode
         */
        public String getMode() {
                return mode;
        }
        /**
         * @param mode the mode to set
         */
        public void setMode(String mode) {
                this.mode = mode;
        }
        /**
         * @return the referenceNo
         */
        public String getReferenceNo() {
                return referenceNo;
        }
        /**
         * @param referenceNo the referenceNo to set
         */
        public void setReferenceNo(String referenceNo) {
                this.referenceNo = referenceNo;
        }
        /**
         * @return the transactionType
         */
        public String getTransactionType() {
                return transactionType;
        }
        /**
         * @param transactionType the transactionType to set
         */
        public void setTransactionType(String transactionType) {
                this.transactionType = transactionType;
        }
        /**
         * @return the status
         */
        public String getStatus() {
                return status;
        }
        /**
         * @param status the status to set
         */
        public void setStatus(String status) {
                this.status = status;
        }
        /**
         * @return the isFlagged
         */
        public String getIsFlagged() {
                return isFlagged;
        }
        /**
         * @param isFlagged the isFlagged to set
         */
        public void setIsFlagged(String isFlagged) {
                this.isFlagged = isFlagged;
        }
        /**
         * @return the errorCode
         */
        public String getErrorCode() {
                return errorCode;
        }
        /**
         * @param errorCode the errorCode to set
         */
        public void setErrorCode(String errorCode) {
                this.errorCode = errorCode;
        }
        /**
         * @return the error
         */
        public String getError() {
                return error;
        }
        /**
         * @param error the error to set
         */
        public void setError(String error) {
                this.error = error;
        }
}