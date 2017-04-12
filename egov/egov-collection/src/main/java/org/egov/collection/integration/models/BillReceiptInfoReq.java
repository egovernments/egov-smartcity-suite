package org.egov.collection.integration.models;

public class BillReceiptInfoReq {
    private RequestInfo requestInfo;

    private BillReceiptReq billReceiptInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(final RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public BillReceiptReq getBillReceiptInfo() {
        return billReceiptInfo;
    }

    public void setBillReceiptInfo(final BillReceiptReq billReceiptInfo) {
        this.billReceiptInfo = billReceiptInfo;
    }

}
