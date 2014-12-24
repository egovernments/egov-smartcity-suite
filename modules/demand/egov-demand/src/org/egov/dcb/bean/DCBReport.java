package org.egov.dcb.bean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.Installment;

public class DCBReport {

    private List<String> fieldNames;
    private Map<Installment, DCBRecord> records;
    private Map<Installment, List<Receipt>> receipts;
    private Map<String, BigDecimal> fieldBalanceTotals;
    private BigDecimal totalBalance;

    public Map<Installment, List<Receipt>> getReceipts() {
        return receipts;
    }

    public void setReceipts(Map<Installment, List<Receipt>> receipts) {
        this.receipts = receipts;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fieldNames == null) ? 0 : fieldNames.hashCode());
        result = prime * result + ((receipts == null) ? 0 : receipts.hashCode());
        result = prime * result + ((records == null) ? 0 : records.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DCBReport other = (DCBReport) obj;
        if (fieldNames == null) {
            if (other.fieldNames != null)
                return false;
        } else if (!fieldNames.equals(other.fieldNames))
            return false;
        if (receipts == null) {
            if (other.receipts != null)
                return false;
        } else if (!receipts.equals(other.receipts))
            return false;
        if (records == null) {
            if (other.records != null)
                return false;
        } else if (!records.equals(other.records))
            return false;
        return true;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public Map<Installment, DCBRecord> getRecords() {
        return records;
    }

    public void setRecords(Map<Installment, DCBRecord> records) {
        this.records = records;
        calculateBalances();
    }

    public void calculateBalances() {
        Map<Installment, DCBRecord> records = getRecords();
        List<String> fieldNames = getFieldNames();
        if (records != null && getFieldNames() != null && !getFieldNames().isEmpty()
                && !records.isEmpty()) {
            fieldBalanceTotals = new HashMap<String, BigDecimal>();
            totalBalance = BigDecimal.ZERO;
            for (Map.Entry<Installment, DCBRecord> record : records.entrySet()) {
                for (String fieldName : fieldNames) {
                    if (record.getKey() != null) {
                        if (fieldBalanceTotals.containsKey(fieldName)) {
                            fieldBalanceTotals.put(fieldName, record.getValue().getBalances().get(
                                    fieldName).add(fieldBalanceTotals.get(fieldName)));

                        } else {
                            fieldBalanceTotals.put(fieldName, record.getValue().getBalances().get(
                                    fieldName));
                        }
                        totalBalance=totalBalance.add(record.getValue().getBalances().get(fieldName));
                    }
                }
            }
        }
    }

    public Map<String, BigDecimal> getFieldBalanceTotals() {
        return fieldBalanceTotals;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public String toString() {
        return "fieldNames:" + fieldNames + ":records:" + records + ":receipts:" + receipts;
    }

    public void backfillReceiptDetails(Map<Receipt, List<ReceiptDetail>> receiptBreakups) {
        List<ReceiptDetail> breakup = null;
        for (Map.Entry<Installment, List<Receipt>> entry : receipts.entrySet()) {
            for (Receipt r : entry.getValue()) {
                // resetting to eliminate the "tuple" junk added by the DCB service 
                breakup = receiptBreakups.get(r);
                r.setReceiptDetails(breakup);
            }
        }
    }
}

