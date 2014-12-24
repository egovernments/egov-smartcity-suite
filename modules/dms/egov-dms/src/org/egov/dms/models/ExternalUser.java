/*
 * @(#)ExternalUser.java 3.0, 15 Jul, 2013 9:38:34 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.models;

import java.io.Serializable;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * The Class ExternalUser.
 */
public class ExternalUser implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** The id. */
	private Long id;
	/** The user type. */
	@Valid
	private ExternalUserType userType;
	/** The user name. */
	@Length(min=1, max=100,message="err.usrnamelen")
	private String userName;
	/** The user address. */
	@Length(max=300,message="err.usraddrlen")
	private String userAddress;
	/** The user ph number. */
	@Length(max=50,message="err.usrphnolen")
	private String userPhNumber;
	/** The user email id. */
	@Email(message="err.emailinvalid")
	private String userEmailId;
	/** The to whom the user addressed to.. */
	@Length(max=150,message="err.addr")
	private String addressedTo;
	/** The user source. */
	@Valid
	private FileSource userSource;
	@Length(max=150,message="err.outfilenumlen")
	private String outboundFileNumber;
	
	/**
	 * Gets the user name.
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Sets the user name.
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Gets the user address.
	 * @return the userAddress
	 */
	public String getUserAddress() {
		return userAddress;
	}
	
	/**
	 * Sets the user address.
	 * @param userAddress the userAddress to set
	 */
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	
	/**
	 * Gets the user ph number.
	 * @return the userPhNumber
	 */
	public String getUserPhNumber() {
		return userPhNumber;
	}
	
	/**
	 * Sets the user ph number.
	 * @param userPhNumber the userPhNumber to set
	 */
	public void setUserPhNumber(String userPhNumber) {
		this.userPhNumber = userPhNumber;
	}
	
	/**
	 * Gets the user email id.
	 * @return the userEmailId
	 */
	public String getUserEmailId() {
		return userEmailId;
	}
	
	/**
	 * Sets the user email id.
	 * @param userEmailId the userEmailId to set
	 */
	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}
	
	/**
	 * Gets the id.
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Gets the user type.
	 * @return the userType
	 */
	public ExternalUserType getUserType() {
		return userType;
	}
	
	/**
	 * Sets the user type.
	 * @param userType the userType to set
	 */
	public void setUserType(ExternalUserType userType) {
		this.userType = userType;
	}
	
	/**
	 * Gets the user source.
	 * @return the user source
	 */
	public FileSource getUserSource() {
		return userSource;
	}
	
	/**
	 * Sets the user source.
	 * @param userSource the new user source
	 */
	public void setUserSource(FileSource userSource) {
		this.userSource = userSource;
	}

	
	/**
	 * @return the addressedTo
	 */
	public String getAddressedTo() {
		return addressedTo;
	}

	
	/**
	 * @param addressedTo the addressedTo to set
	 */
	public void setAddressedTo(String addressedTo) {
		this.addressedTo = addressedTo;
	}

	public String getOutboundFileNumber() {
		return outboundFileNumber;
	}

	public void setOutboundFileNumber(String outboundFileNumber) {
		this.outboundFileNumber = outboundFileNumber;
	}
}
