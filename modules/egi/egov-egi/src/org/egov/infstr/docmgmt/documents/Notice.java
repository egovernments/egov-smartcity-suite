/*
 * @(#)Notice.java 3.0, 17 Jun, 2013 11:52:21 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.docmgmt.documents;

import java.util.Date;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.egov.infstr.annotation.Search;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.docmgmt.DocumentObject;

@Node(extend = DocumentObject.class)
public class Notice extends DocumentObject {
	
	private static final long serialVersionUID = 1L;
	private static final String TO_STRING_SEPARATOR = "|";
	
	@Field(jcrMandatory = true)
	private String noticeType;
	@Field(jcrMandatory = true)
	private Date noticeDate;
	@Field
	private String associatedObjectId;
	@Field
	private String addressedTo;
	@Field
	private String address;
	@Field
	private String comments;
	
	public Notice() {
		super();
	}
	
	public Notice(String documentNumber, String moduleName, String noticeType, Date noticeDate) {
		super();
		this.documentNumber = documentNumber;
		this.moduleName = moduleName;
		this.noticeType = noticeType;
		this.noticeDate = noticeDate;
		this.domainName = escapeSpecialChars(EGOVThreadLocals.getDomainName());
	}
	
	@Override
	public String toString() {
	    return new StringBuilder()
        .append(moduleName)
        .append(TO_STRING_SEPARATOR)
        .append(noticeType)
        .append(TO_STRING_SEPARATOR)
        .append(documentNumber)
	    .toString();
	}
	
	@Search(searchOp = Search.Operator.startsWith)
	public String getNoticeType() {
		return noticeType;
	}
	
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	
	@Search(searchOp = Search.Operator.between)
	public Date getNoticeDate() {
		return noticeDate;
	}
	
	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}
	
	public String getAssociatedObjectId() {
		return associatedObjectId;
	}
	
	public void setAssociatedObjectId(String associatedObjectId) {
		this.associatedObjectId = associatedObjectId;
	}
	
	@Search(searchOp = Search.Operator.contains)
	public String getAddressedTo() {
		return addressedTo;
	}
	
	public void setAddressedTo(String addressedTo) {
		this.addressedTo = addressedTo;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
