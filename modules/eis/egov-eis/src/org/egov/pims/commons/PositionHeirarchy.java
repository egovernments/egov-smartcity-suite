package org.egov.pims.commons;

public class PositionHeirarchy 
{
	Integer id ;
	org.egov.pims.commons.Position posFrom;
	org.egov.pims.commons.Position posTo;
	org.egov.commons.ObjectType objectType;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public org.egov.commons.ObjectType getObjectType() {
		return objectType;
	}
	public void setObjectType(org.egov.commons.ObjectType objectType) {
		this.objectType = objectType;
	}
	public org.egov.pims.commons.Position getPosFrom() {
		return posFrom;
	}
	public void setPosFrom(org.egov.pims.commons.Position posFrom) {
		this.posFrom = posFrom;
	}
	public org.egov.pims.commons.Position getPosTo() {
		return posTo;
	}
	public void setPosTo(org.egov.pims.commons.Position posTo) {
		this.posTo = posTo;
	}
}
