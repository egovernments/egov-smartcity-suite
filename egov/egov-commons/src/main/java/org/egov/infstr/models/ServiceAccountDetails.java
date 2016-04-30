package org.egov.infstr.models;

import org.egov.commons.CChartOfAccounts;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

public class ServiceAccountDetails {
	
	private Long id;
	
	private BigDecimal amount = BigDecimal.ZERO;
	
	private CChartOfAccounts glCodeId;
	
	private ServiceDetails serviceDetails;
	
	private Set<ServiceSubledgerInfo> subledgerDetails = new LinkedHashSet<ServiceSubledgerInfo>(0);

	
	public void addSubledgerDetails(ServiceSubledgerInfo subledgerInfo) {
       getSubledgerDetails().add(subledgerInfo);
    }
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return null != this.amount ?this.amount.setScale(2, BigDecimal.ROUND_HALF_EVEN):null;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the glCodeId
	 */
	public CChartOfAccounts getGlCodeId() {
		return glCodeId;
	}

	/**
	 * @param glCodeId the glCodeId to set
	 */
	public void setGlCodeId(CChartOfAccounts glCodeId) {
		this.glCodeId = glCodeId;
	}

	/**
	 * @return the serviceDetails
	 */
	public ServiceDetails getServiceDetails() {
		return serviceDetails;
	}

	/**
	 * @param serviceDetails the serviceDetails to set
	 */
	public void setServiceDetails(ServiceDetails serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Set<ServiceSubledgerInfo> getSubledgerDetails() {
		return subledgerDetails;
	}

	public void setSubledgerDetails(Set<ServiceSubledgerInfo> subledgerDetails) {
		this.subledgerDetails = subledgerDetails;
	}
}
