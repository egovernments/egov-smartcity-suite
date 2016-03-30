package org.egov.collection.entity;

import java.math.BigInteger;

public class CollectionSummaryReport {

    private String source;
    private String serviceName;
    private String cashCount;
    private Double cashAmount;
    private String chequeddCount;
    private Double chequeddAmount;
    private String onlineCount;
    private Double onlineAmount;
    private String totalReceiptCount;
    private Double totalAmount;
    private String employeeName;
    private String counterName;

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCashCount() {
        return cashCount;
    }

    public void setCashCount(final String cashCount) {
        this.cashCount = cashCount;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(final Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getChequeddCount() {
        return chequeddCount;
    }

    public void setChequeddCount(final String chequeddCount) {
        this.chequeddCount = chequeddCount;
    }

    public Double getChequeddAmount() {
        return chequeddAmount;
    }

    public void setChequeddAmount(final Double chequeddAmount) {
        this.chequeddAmount = chequeddAmount;
    }

    public String getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(final String onlineCount) {
        this.onlineCount = onlineCount;
    }

    public Double getOnlineAmount() {
        return onlineAmount;
    }

    public void setOnlineAmount(final Double onlineAmount) {
        this.onlineAmount = onlineAmount;
    }

    public String getTotalReceiptCount() {
        return totalReceiptCount;
    }

    public void setTotalReceiptCount(final String totalReceiptCount) {
        this.totalReceiptCount = totalReceiptCount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(final String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(final String counterName) {
        this.counterName = counterName;
    }
}
