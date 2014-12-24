package org.egov.demand.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * EgBillType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EgBillType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String code;
	private Date createTimeStamp;
	private Date updatedTimeStamp;
	private Set egBills = new HashSet(0);

	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTimeStamp() {
		return this.createTimeStamp;
	}

	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public Date getUpdatedTimeStamp() {
		return this.updatedTimeStamp;
	}

	public void setUpdatedTimeStamp(Date updatedTimeStamp) {
		this.updatedTimeStamp = updatedTimeStamp;
	}

	public Set getEgBills() {
		return this.egBills;
	}

	public void setEgBills(Set egBills) {
		this.egBills = egBills;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}