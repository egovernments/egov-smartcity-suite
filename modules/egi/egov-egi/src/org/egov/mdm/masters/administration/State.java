/*
 * @(#)State.java 3.0, 7 Jun, 2013 8:46:22 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.mdm.masters.administration;

public class State {

	private Integer id;
	private String name;
	private String nameLocal;
	private String stateConst;

	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @hibernate.property name="name"
	 * @hibernate.column name="STATENAME"
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @hibernate.property name="nameLocal"
	 * @hibernate.column name="STATENAMELOCAL"
	 * @return Returns the nameLocal.
	 */
	public String getNameLocal() {
		return nameLocal;
	}

	/**
	 * @param nameLocal The nameLocal to set.
	 */
	public void setNameLocal(String nameLocal) {
		this.nameLocal = nameLocal;
	}

	/**
	 * @hibernate.property name="stateConst"
	 * @hibernate.column name="STATECONST"
	 * @return Returns the stateConst.
	 */
	public String getStateConst() {
		return stateConst;
	}

	/**
	 * @param stateConst The stateConst to set.
	 */
	public void setStateConst(String stateConst) {
		this.stateConst = stateConst;
	}

}
