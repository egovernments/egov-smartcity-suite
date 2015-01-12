/*
 * @(#)AssociatedFile.java 3.0, 17 Jun, 2013 11:54:02 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.docmgmt;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(jcrMixinTypes = JcrConstants.MIX_VERSIONABLE)
public class AssociatedFile implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Field(jcrMandatory = true)
	private String fileName;
	
	@Field(jcrType = JcrConstants.JCR_DATA, jcrMandatory = true)
	private InputStream fileInputStream;
	
	@Field(jcrMandatory = true)
	private String mimeType;
	
	@Field
	private long length;
	
	@Field
	private int createdBy;
	
	@Field
	private int modifiedBy;
	
	@Field
	private Date createdDate;
	
	@Field
	private Date modifiedDate;
	
	@Field
	private String remarks;
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public int getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	
	public int getModifiedBy() {
		return modifiedBy;
	}
	
	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AssociatedFile))
			return false;
		AssociatedFile object = (AssociatedFile) obj;
		return (object.getFileName().equals(fileName));
	}
	
	public int hashCode() {
		int hashCode = 1;
		hashCode = 31 * hashCode + ((fileName == null) ? 0 : fileName.hashCode());
		return hashCode;
	}
	
	public String toString() {
		return fileName;
	}
	
	public long getLength() {
		return length;
	}
	
	public void setLength(long length) {
		this.length = length;
	}
}
