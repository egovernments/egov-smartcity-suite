/*
 * @(#)CrossHeirarchyImpl.java 3.0, 16 Jun, 2013 3:35:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

public class CrossHeirarchyImpl implements CrossHeirarchy {

	private Integer id;
	private Boundary parent;
	private Boundary child;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boundary getParent() {
		return parent;
	}

	public void setParent(Boundary parent) {
		this.parent = parent;
	}

	public Boundary getChild() {
		return child;
	}

	public void setChild(Boundary child) {
		this.child = child;
	}

}
