package org.egov.erpcollection.models;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;


public class ReceiptDetail implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private static final String TO_STRING_SEP = "-";
    private Long id;
	private ReceiptHeader receiptHeader;
	private CChartOfAccounts accounthead;
	private BigDecimal dramount;
	
	/**
	 * A <code>BigDecimal</code> representing the actual amount to be paid by the user
	 */
	private BigDecimal cramountToBePaid;
	
	/**
	 * A <code>BigDecimal</code> representing the actual amount paid by the user
	 */
	private BigDecimal cramount;
	private Long ordernumber;
	
	/**
	 * A <code>String</code> representing the glcode description sent from billing system
	 */
	private String description;
	private CFunction function;
	
	/**
	 * A <code>Long</code> representing the glcode is part of actual demand or not
	 */
	private Long isActualDemand;
	
	private Set<AccountPayeeDetail> accountPayeeDetails = new HashSet<AccountPayeeDetail>(0);
	
	private CFinancialYear financialYear;

	public ReceiptDetail() {
	}

	public ReceiptDetail(CChartOfAccounts account, CFunction function, BigDecimal cramountToBePaid, 
			BigDecimal drAmount, BigDecimal crAmount,Long order, String description, Long isActualDemand,ReceiptHeader receiptHeader) {
		this.accounthead = account;
		this.function = function;
		this.cramountToBePaid = cramountToBePaid;
		this.dramount = drAmount;
		this.cramount = crAmount;
		this.ordernumber = order;
		this.description = description;
		this.isActualDemand= isActualDemand;
		this.receiptHeader = receiptHeader;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
        .append(ordernumber).append(TO_STRING_SEP)
        .append(accounthead.getGlcode()).append(TO_STRING_SEP)
        .append(cramountToBePaid).append(TO_STRING_SEP)
        .append(cramount).append(TO_STRING_SEP)
        .append(dramount).append(TO_STRING_SEP)
        .append(description).append(TO_STRING_SEP)
        .append(isActualDemand);
        return sb.toString();
    }
    
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReceiptHeader getReceiptHeader() {
		return this.receiptHeader;
	}

	public void setReceiptHeader(
			ReceiptHeader receiptHeader) {
		this.receiptHeader = receiptHeader;
	}

	
	public BigDecimal getDramount() {
		return this.dramount;
	}

	public void setDramount(BigDecimal dramount) {
		this.dramount = dramount;
	}

	public BigDecimal getCramount() {
		return this.cramount;
	}

	public void setCramount(BigDecimal cramount) {
		this.cramount = cramount;
	}

	/**
	 * Sets both DR and CR amounts to zero.
	 */
	public void zeroDrAndCrAmounts() {
        this.dramount = BigDecimal.ZERO;
	    this.cramount = BigDecimal.ZERO;
	}
	
	public Long getOrdernumber() {
		return this.ordernumber;
	}

	public void setOrdernumber(Long ordernumber) {
		this.ordernumber = ordernumber;
	}

	public CFunction getFunction() {
		return this.function;
	}

	public void setFunction(CFunction function) {
		this.function = function;
	}

	/**
	 * @return the accounthead
	 */
	public CChartOfAccounts getAccounthead() {
		return accounthead;
	}

	/**
	 * @param accounthead the accounthead to set
	 */
	public void setAccounthead(CChartOfAccounts accounthead) {
		this.accounthead = accounthead;
	}
	
	
	
	public BigDecimal getCramountToBePaid() {
		return cramountToBePaid;
	}

	public void setCramountToBePaid(BigDecimal cramountToBePaid) {
		this.cramountToBePaid = cramountToBePaid;
	}
	
	public Set<AccountPayeeDetail> getAccountPayeeDetails() {
		return this.accountPayeeDetails;
	}

	public void setAccountPayeeDetails(Set<AccountPayeeDetail> accountPayeeDetails) {
		this.accountPayeeDetails = accountPayeeDetails;
	}
	public void addAccountPayeeDetail(AccountPayeeDetail accountPayeeDetails) {
		getAccountPayeeDetails().add(accountPayeeDetails);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public CFinancialYear getFinancialYear(){
		return financialYear;
	}
	public void setFinancialYear(CFinancialYear financialYear){
		this.financialYear=financialYear;
	}

	/**
	 * @return the isActualDemand
	 */
	public Long getIsActualDemand() {
		return isActualDemand;
	}

	/**
	 * @param isActualDemand the isActualDemand to set
	 */
	public void setIsActualDemand(Long isActualDemand) {
		this.isActualDemand = isActualDemand;
	}
}
