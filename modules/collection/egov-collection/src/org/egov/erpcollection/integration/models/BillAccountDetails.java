package org.egov.erpcollection.integration.models;

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("account")
public class BillAccountDetails implements Comparable<BillAccountDetails>{
	
	@XStreamAsAttribute
	private final String glCode;
	
	@XStreamAsAttribute
	private final Integer order;
	
	@XStreamAsAttribute
	private final String description;
	
	@XStreamAlias("crAmount")
	private final BigDecimal crAmount;
	@XStreamAlias("drAmount")
	private final BigDecimal drAmount;
	private final String functionCode;
	
	@XStreamAsAttribute
	private final Integer isActualDemand;
	
	public BillAccountDetails(String glCode,Integer order,BigDecimal crAmount,BigDecimal drAmount, String functionCode, String description,Integer isActualDemand){
		this.glCode=glCode;
		this.order=order;
		this.crAmount=crAmount;
		this.drAmount=drAmount;
		this.functionCode=functionCode;
		this.description=description;
		this.isActualDemand=isActualDemand;
	}

	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
        .append(order).append(",")
        .append(glCode).append(",")
        .append(crAmount).append(",")
        .append(crAmount).append(",")
        .append(description).append(",")
        .append(isActualDemand);
        return sb.toString();
	}
	
	public String getGlCode() {
		return glCode;
	}
	public Integer getOrder() {
		return order;
	}
	public BigDecimal getDrAmount() {
		return drAmount;
	}
	public BigDecimal getCrAmount() {
		return crAmount;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Integer getIsActualDemand(){
		return isActualDemand;
	}
	
	@Override
	public int compareTo(BillAccountDetails obj) {
		return this.order - obj.order ;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof BillAccountDetails){
			BillAccountDetails account = (BillAccountDetails)obj;
			if(this.glCode.equals(account.glCode) &&
					this.order.equals(account.order)&&
					this.crAmount.equals(account.crAmount) &&
					this.drAmount.equals(account.drAmount) &&
					this.description.equals(account.description) &&
					this.functionCode.equals(account.functionCode) &&
					this.isActualDemand.equals(account.isActualDemand)){
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.glCode.hashCode() + this.order.hashCode()
				+ this.crAmount.hashCode() + this.drAmount.hashCode()
				+ this.description.hashCode() + this.functionCode.hashCode() + this.isActualDemand.hashCode();
	}
}
