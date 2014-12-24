package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.util.Date;

import org.egov.infstr.models.BaseModel;

/**
 * This class defines Property Master Documents i.e all the Documents which have
 * been submitted by the Citizen to claim the Owner of the Property
 * 
 * @author Girish
 * @version 1.00
 */
public class PropertyDocs extends BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id = null;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BasicProperty getBasicProperty() {
		return basicProperty;
	}
	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}
	public String getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	private BasicProperty basicProperty;
	private String docNumber;
	private String reason;
	private Date modifiedDate;
	private Date createdDate;
}
