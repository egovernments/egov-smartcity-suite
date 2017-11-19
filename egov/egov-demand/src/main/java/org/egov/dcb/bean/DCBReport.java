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
package org.egov.dcb.bean;

import org.egov.commons.Installment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DCBReport {

	private List<String> fieldNames;
	private Map<Installment, DCBRecord> records;
	private Map<Installment, List<Receipt>> receipts;
	private Map<String, BigDecimal> fieldBalanceTotals;
	private BigDecimal totalDmdTax;
	private BigDecimal totalDmdPnlty;
	private BigDecimal totalLpayPnlty;
	private BigDecimal totalColTax;
	private BigDecimal totalColPnlty;
	private BigDecimal totalColLpayPnlty;
	private BigDecimal totalRebate;
	private BigDecimal totalBalance;
	private BigDecimal totalAdvance;
	private String TAX = "TAX";
	private String ADVANCE ="Advance Collection";
	private String WTTAX = "Water Charges"; 
	private String PENALTY = "PENALTY";
	private String FINES = "FINES";
	private BigDecimal totalRcptAmt;

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
		result = prime * result
				+ ((fieldNames == null) ? 0 : fieldNames.hashCode());
		result = prime * result
				+ ((receipts == null) ? 0 : receipts.hashCode());
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
		calculateTotals();
		calculateBalances();
		
	}

	public void calculateBalances() {
		Map<Installment, DCBRecord> records = getRecords();
		List<String> fieldNames = getFieldNames();
		if (records != null && getFieldNames() != null
				&& !getFieldNames().isEmpty() && !records.isEmpty()) {
			fieldBalanceTotals = new HashMap<String, BigDecimal>();
			totalBalance = BigDecimal.ZERO;
			for (Map.Entry<Installment, DCBRecord> record : records.entrySet()) {
				for (String fieldName : fieldNames) {
					if (record.getKey() != null) {
						if (fieldBalanceTotals.containsKey(fieldName)) {
							fieldBalanceTotals.put(
									fieldName,
									record.getValue()
											.getBalances()
											.get(fieldName)
											.add(fieldBalanceTotals
													.get(fieldName)));

						} else {
							fieldBalanceTotals.put(fieldName, record.getValue()
									.getBalances().get(fieldName));
						}
						totalBalance = totalBalance.add(record.getValue()
								.getBalances().get(fieldName));
					}
				}
			}
		}
	}

	public void calculateTotals() {
		Map<Installment, DCBRecord> records = getRecords();
		List<String> fieldNames = getFieldNames();
		if (records != null && getFieldNames() != null
				&& !getFieldNames().isEmpty() && !records.isEmpty()) {
			totalDmdTax = BigDecimal.ZERO;
			totalDmdPnlty = BigDecimal.ZERO;
			totalColTax = BigDecimal.ZERO;
			totalColPnlty = BigDecimal.ZERO;
			totalRebate = BigDecimal.ZERO;
			totalLpayPnlty = BigDecimal.ZERO;
			totalColLpayPnlty = BigDecimal.ZERO;
			totalAdvance = BigDecimal.ZERO;
			for (Map.Entry<Installment, DCBRecord> record : records.entrySet()) {
				for (String fieldName : fieldNames) {
					if (record.getKey() != null) {
						if (fieldName.equals(TAX)) {
							totalDmdTax = totalDmdTax.add(record.getValue()
									.getDemands().get(fieldName));
							totalColTax = totalColTax.add(record.getValue()
									.getCollections().get(fieldName));
							totalRebate = totalRebate.add(record.getValue()
									.getRebates().get(fieldName));
						}
						if (fieldName.equals(WTTAX)) {
                                                    totalDmdTax = totalDmdTax.add(record.getValue()
                                                                    .getDemands().get(fieldName));
                                                    totalColTax = totalColTax.add(record.getValue()
                                                                    .getCollections().get(fieldName));
                                                    totalRebate = totalRebate.add(record.getValue()
                                                                    .getRebates().get(fieldName));
                                            }
						if (fieldName.equals(PENALTY)) {
							totalDmdPnlty = totalDmdPnlty.add(record.getValue()
									.getDemands().get(fieldName));
							totalColPnlty = totalColPnlty.add(record.getValue()
									.getCollections().get(fieldName));
						}
						if (fieldName.equals(FINES)) {
							totalLpayPnlty = totalLpayPnlty.add(record
									.getValue().getDemands().get(fieldName));
							totalColLpayPnlty = totalColLpayPnlty.add(record.getValue()
									.getCollections().get(fieldName));
						}
						if (fieldName.equals(ADVANCE)) {
							totalAdvance = totalAdvance.add(record
									.getValue().getCollections().get(fieldName));
							
						}
					}
				}
			}
		}
	}

	public void backfillReceiptDetails(
			Map<Receipt, List<ReceiptDetail>> receiptBreakups) {
		List<ReceiptDetail> breakup = null;
		for (Map.Entry<Installment, List<Receipt>> entry : receipts.entrySet()) {
			for (Receipt r : entry.getValue()) {
				// resetting to eliminate the "tuple" junk added by the DCB
				// service
				breakup = receiptBreakups.get(r);
				r.setReceiptDetails(breakup);
			}
		}
	}

	public void calculateReceiptTotal() {
		List<Receipt> rcpts = new ArrayList<Receipt>();
		totalRcptAmt = BigDecimal.ZERO;
		for (Map.Entry<Installment, List<Receipt>> receiptMap : getReceipts()
				.entrySet()) {
			for (Receipt r : receiptMap.getValue()) {
				if (!rcpts.contains(r) && r.getReceiptStatus().equals('A')) {
					rcpts.add(r);
					totalRcptAmt = totalRcptAmt.add(r.getReceiptAmt());
				}
			}
		}
	}

	public BigDecimal getTotalDmdTax() {
		return totalDmdTax;
	}

	public void setTotalDmdTax(BigDecimal totalDmdTax) {
		this.totalDmdTax = totalDmdTax;
	}

	public BigDecimal getTotalDmdPnlty() {
		return totalDmdPnlty;
	}

	public void setTotalDmdPnlty(BigDecimal totalDmdPnlty) {
		this.totalDmdPnlty = totalDmdPnlty;
	}

	public BigDecimal getTotalLpayPnlty() {
		return totalLpayPnlty;
	}

	public void setTotalLpayPnlty(BigDecimal totalLpayPnlty) {
		this.totalLpayPnlty = totalLpayPnlty;
	}

	public BigDecimal getTotalColTax() {
		return totalColTax;
	}

	public void setTotalColTax(BigDecimal totalColTax) {
		this.totalColTax = totalColTax;
	}

	public BigDecimal getTotalColPnlty() {
		return totalColPnlty;
	}

	public void setTotalColPnlty(BigDecimal totalColPnlty) {
		this.totalColPnlty = totalColPnlty;
	}

	public BigDecimal getTotalAdvance() {
		return totalAdvance;
	}

	public void setTotalAdvance(BigDecimal totalAdvance) {
		this.totalAdvance = totalAdvance;
	}

	public BigDecimal getTotalColLpayPnlty() {
		return totalColLpayPnlty;
	}

	public void setTotalColLpayPnlty(BigDecimal totalColLpayPnlty) {
		this.totalColLpayPnlty = totalColLpayPnlty;
	}

	public BigDecimal getTotalRebate() {
		return totalRebate;
	}

	public void setTotalRebate(BigDecimal totalRebate) {
		this.totalRebate = totalRebate;
	}

	public BigDecimal getTotalRcptAmt() {
		return totalRcptAmt;
	}

	public void setTotalRcptAmt(BigDecimal totalRcptAmt) {
		this.totalRcptAmt = totalRcptAmt;
	}

	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}

	public Map<String, BigDecimal> getFieldBalanceTotals() {
		return fieldBalanceTotals;
	}

	public BigDecimal getTotalBalance() {
		return totalBalance;
	}

	public String toString() {
		return "fieldNames:" + fieldNames + ":records:" + records
				+ ":receipts:" + receipts;
	}

}
