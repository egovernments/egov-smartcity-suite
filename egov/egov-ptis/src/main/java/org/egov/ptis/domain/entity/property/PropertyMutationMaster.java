/*
 * ModifyARVReason.java
 * Created on May 4, 2007
 *
 * Copyright 2006 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

/**
 * @author Suhasini.CH TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */

public class PropertyMutationMaster {
	private Integer idMutation = null;
	private String mutationName = null;
	private String mutationDesc = null;
	private String type = null;
	private String code = null;
	private String orderId = null;

	/**
	 * @return Returns the idMutation.
	 */
	public Integer getIdMutation() {
		return idMutation;
	}

	/**
	 * @param idMutation
	 *            The idMutation to set.
	 */
	public void setIdMutation(Integer idMutation) {
		this.idMutation = idMutation;
	}

	/**
	 * @return Returns the mutationDesc.
	 */
	public String getMutationDesc() {
		return mutationDesc;
	}

	/**
	 * @param mutationDesc
	 *            The mutationDesc to set.
	 */
	public void setMutationDesc(String mutationDesc) {
		this.mutationDesc = mutationDesc;
	}

	/**
	 * @return Returns the mutationName.
	 */
	public String getMutationName() {
		return mutationName;
	}

	/**
	 * @param mutationName
	 *            The mutationName to set.
	 */
	public void setMutationName(String mutationName) {
		this.mutationName = mutationName;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getIdMutation()).append("|Name: ").append(getMutationName()).append("|Type: ")
				.append(getType());

		return objStr.toString();
	}
}
