package org.egov.egf.model;

import java.math.BigDecimal;
import java.util.Date;

public class BillPaymentDetails {

    private String cityname;
    private String billVoucherNo;
    private String billVoucherStatus;
    private String paymentVoucherNo;
    private String paymentVoucherStatus;
    private BigDecimal paymentAmount;
    private Date voucherDate;
    private String chequRefNo;


    public String getCityname() {
        return cityname;
    }
    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getBillVoucherNo() {
        return billVoucherNo;
    }
    public void setBillVoucherNo(String billVoucherNo) {
        this.billVoucherNo = billVoucherNo;
    }

    public String getBillVoucherStatus() {
        return billVoucherStatus;
    }
    public void setBillVoucherStatus(String billVoucherStatus) {
        this.billVoucherStatus = billVoucherStatus;
    }

    public String getPaymentVoucherNo() {
        return paymentVoucherNo;
    }
    public void setPaymentVoucherNo(String paymentVoucherNo) {
        this.paymentVoucherNo = paymentVoucherNo;
    }

    public String getPaymentVoucherStatus() {
        return paymentVoucherStatus;
    }

    public void setPaymentVoucherStatus(String paymentVoucherStatus) {
        this.paymentVoucherStatus = paymentVoucherStatus;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }
    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Date getVoucherDate() {
        return voucherDate;
    }
    public void setVoucherDate(Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getChequRefNo() {
        return chequRefNo;
    }
    public void setChequRefNo(String chequRefNo) {
        this.chequRefNo = chequRefNo;
    }

}
