package org.egov.demand.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.infstr.commons.Module;

/**
 * EgDemandReasonMaster entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class EgDemandReasonMaster implements java.io.Serializable {

	// Fields

	private Long id;
	private Module egModule;
	private EgReasonCategory egReasonCategory;
	private String reasonMaster;
	private String isDebit;
	private String code;
	private Long orderId;
	private Date createTimeStamp;
	private Date lastUpdatedTimeStamp;
	private Set<EgDemandReason> egDemandReasons = new HashSet<EgDemandReason>(0);

	public String toString() {
	    return code;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Module getEgModule() {
		return this.egModule;
	}

	public void setEgModule(Module egModule) {
		this.egModule = egModule;
	}

	public EgReasonCategory getEgReasonCategory() {
		return this.egReasonCategory;
	}

	public void setEgReasonCategory(EgReasonCategory egReasonCategory) {
		this.egReasonCategory = egReasonCategory;
	}

	public String getReasonMaster() {
		return this.reasonMaster;
	}

	public void setReasonMaster(String reasonMaster) {
		this.reasonMaster = reasonMaster;
	}

	public String getIsDebit() {
		return this.isDebit;
	}

	public void setIsDebit(String isDebit) {
		this.isDebit = isDebit;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getCreateTimeStamp() {
		return this.createTimeStamp;
	}

	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public Set<EgDemandReason> getEgDemandReasons() {
		return this.egDemandReasons;
	}

	public void setEgDemandReasons(Set<EgDemandReason> egDemandReasons) {
		this.egDemandReasons = egDemandReasons;
	}

	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

}