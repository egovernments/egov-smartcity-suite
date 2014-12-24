package org.egov.pims.commons.client;

import org.apache.struts.action.ActionForm;

public class PosForm extends ActionForm
{
 
	private String Id;
	private String[] positionHeirId;
	private String[] positionFrom;
	private String[] posFrom;
	private String[] posTo;
	private String[] positionTo;
	private String[] deletePositionSet;
	private String objId;
	public String[] getPositionHeirId() {
		return positionHeirId;
	}
	public void setPositionHeirId(String[] positionHeirId) {
		this.positionHeirId = positionHeirId;
	}
	public String[] getPositionFrom() {
		return positionFrom;
	}
	public void setPositionFrom(String[] positionFrom) {
		this.positionFrom = positionFrom;
	}
	public String[] getPositionTo() {
		return positionTo;
	}
	public void setPositionTo(String[] positionTo) {
		this.positionTo = positionTo;
	}
	public String[] getDeletePositionSet() {
		return deletePositionSet;
	}
	public void setDeletePositionSet(String[] deletePositionSet) {
		this.deletePositionSet = deletePositionSet;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String[] getPosFrom() {
		return posFrom;
	}
	public void setPosFrom(String[] posFrom) {
		this.posFrom = posFrom;
	}
	public String[] getPosTo() {
		return posTo;
	}
	public void setPosTo(String[] posTo) {
		this.posTo = posTo;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	
}
