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

