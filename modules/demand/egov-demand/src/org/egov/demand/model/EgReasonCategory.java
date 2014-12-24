package org.egov.demand.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * EgReasonCategory entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class EgReasonCategory implements java.io.Serializable {

	// Fields

	private Long idType;
	private String name;
	private String code;
	private Long orderId;
	private Set egDemandReasonMasters = new HashSet(0);
	private Date lastUpdatedTimeStamp;

	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append(name).append("(").append(code).append(")");
	    return sb.toString();
	}

	// Property accessors

	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	public Long getIdType() {
		return this.idType;
	}

	public void setIdType(Long idType) {
		this.idType = idType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set getEgDemandReasonMasters() {
		return this.egDemandReasonMasters;
	}

	public void setEgDemandReasonMasters(Set egDemandReasonMasters) {
		this.egDemandReasonMasters = egDemandReasonMasters;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	}