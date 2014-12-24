/*
 * BasicPropertyImpl.java Created on Oct 26, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.entity.property;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFOWNER;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFSTATUS;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.lib.admbndry.Boundary;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.commons.service.EisCommonsServiceImpl;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.objection.Objection;
import org.egov.ptis.domain.entity.recovery.Recovery;
import org.egov.ptis.notice.PtNotice;

/**
 * BasicPropertyImpl is the Implementation Class for BasicProperty
 * 
 * @author Neetu
 * @version 2.00
 * 
 * @see BasicProperty
 */

public class BasicPropertyImpl extends BaseModel implements BasicProperty {
	private Boolean active;
	private String upicNo;
	private PropertyID propertyID;
	private PropertyAddress address;
	private Boundary boundary;
	private String oldMuncipalNum;
	private Set<PropertyStatusValues> propertyStatusValuesSet = new HashSet<PropertyStatusValues>();
	private Set<PropertyMutation> propMutationSet = new HashSet<PropertyMutation>();
	private PropertyMutationMaster propertyMutationMaster;
	private Date propCreateDate;
	private Set<Property> propertySet = new HashSet<Property>();
	private PropertyReference propertyReference;
	private PropertyStatus status;
	private String extraField1;
	private String extraField2;
	private String extraField3;
	private String gisReferenceNo;
	private String partNo;
	private Set<PtNotice> notices = new HashSet<PtNotice>();
	private Set<Objection> objections = new HashSet<Objection>();
	private Set<Recovery> recoveries = new HashSet<Recovery>();
	private Character isMigrated = 'N';
	private Set<PropertyDocs> propertyDocsSet = new HashSet<PropertyDocs>();
	private Boolean allChangesCompleted;	

	public Set<PropertyDocs> getPropertyDocsSet() {
		return propertyDocsSet; 
	}

	public void setPropertyDocsSet(Set<PropertyDocs> propertyDocsSet) {
		this.propertyDocsSet = propertyDocsSet;
	}

	public void addDocs(PropertyDocs propertyDocs) {
		getPropertyDocsSet().add(propertyDocs);
	}

	public void removeDocs(PropertyDocs propertyDocs) {
		getPropertyDocsSet().remove(propertyDocs);
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof BasicPropertyImpl))
			return false;

		final BasicPropertyImpl other = (BasicPropertyImpl) obj;

		if (getId() != null || other.getId() != null) {
			if (getId().equals(other.getId())) {
				return true;
			}
		}
		if (getPropertyID() != null || other.getPropertyID() != null) {
			if (getPropertyID().equals(other.getPropertyID())) {
				return true;
			} else
				return false;
		} else
			return false;
	}

	public int hashCode() {
		int hashCode = 0;

		if (getId() != null) {
			hashCode = hashCode + getId().hashCode();
		}
		if (getPropertyID() != null) {
			hashCode = hashCode + isActive().hashCode() + getAddress().hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Id: ").append(getId()).append("|");				
		sb = (upicNo != null && !upicNo.isEmpty()) ? sb.append("UpicNo: ").append(upicNo) : sb.append("");
		sb = (getBoundary() != null) ? sb.append("|Boundary: ").append(getBoundary().getName()) : sb.append("");
		sb = (getStatus() != null) ? sb.append("|Status").append(status.getName()) : sb.append("");
		sb.append("Address: ").append(getAddress());
		
		return sb.toString();
	}

	public PropertyID getPropertyID() {
		return propertyID;
	}

	public void setPropertyID(PropertyID propertyID) {
		this.propertyID = propertyID;
	}

	public Boolean getActive() {
		return active;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setAddress(PropertyAddress address) {
		this.address = address;
	}

	public PropertyAddress getAddress() {
		return address;
	}

	/**
	 * Returns the default and non-history current installment Property for
	 * basicproperty.
	 * 
	 * @return Property
	 */
	public Property getProperty() {
		Property returnProperty = null;
		for (Property property : getPropertySet()) {
			if (property.getStatus().equals('A') && property.getIsDefaultProperty().equals('Y')) {
				returnProperty = property;
			}
		}
		return returnProperty;
	}

	public Boundary getBoundary() {
		return boundary;
	}

	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

	public List<ValidationError> validate() {
		return new ArrayList<ValidationError>();
	}

	public boolean validateBasicProp() {
		if ((getAddress() == null) || (getAddress().validate() == false))
			throw new EGOVRuntimeException("BasicProperty validation failed: Address is not set, Please Check !!");

		if (getCreatedBy() == null)
			throw new EGOVRuntimeException("BasicProperty validation failed: CreatedBy is not set, Please Check !!");

		if (getPropertyID() == null)
			throw new EGOVRuntimeException("BasicProperty validation failed: PropertyID is not set, Please Check !!");

		return true;
	}

	public String getUpicNo() {
		return upicNo;
	}

	public void setUpicNo(String upicNo) {
		this.upicNo = upicNo;
	}

	public String getOldMuncipalNum() {
		return oldMuncipalNum;
	}

	public void setOldMuncipalNum(String oldMuncipalNum) {
		this.oldMuncipalNum = oldMuncipalNum;
	}

	public void addPropertyStatusValues(PropertyStatusValues propertyStatusValues) {
		getPropertyStatusValuesSet().add(propertyStatusValues);
	}

	public void removePropertyStatusValues(PropertyStatusValues propertyStatusValues) {
		getPropertyStatusValuesSet().remove(propertyStatusValues);
	}

	public String getExtraField1() {
		return extraField1;
	}

	public void setExtraField1(String extraField1) {
		this.extraField1 = extraField1;
	}

	public String getExtraField2() {
		return extraField2;
	}

	public void setExtraField2(String extraField2) {
		this.extraField2 = extraField2;
	}

	public PropertyReference getPropertyReference() {
		return propertyReference;
	}

	public void setPropertyReference(PropertyReference propertyReference) {
		this.propertyReference = propertyReference;
	}

	public Set<PropertyMutation> getPropMutationSet() {
		return propMutationSet;
	}

	public void setPropMutationSet(Set<PropertyMutation> propMutationSet) {
		this.propMutationSet = propMutationSet;
	}

	public PropertyMutationMaster getPropertyMutationMaster() {
		return propertyMutationMaster;
	}

	public void setPropertyMutationMaster(PropertyMutationMaster propertyMutationMaster) {
		this.propertyMutationMaster = propertyMutationMaster;
	}

	public String getExtraField3() {
		return extraField3;
	}

	public void setExtraField3(String extraField3) {
		this.extraField3 = extraField3;
	}

	public Set<PropertyStatusValues> getPropertyStatusValuesSet() {
		return propertyStatusValuesSet;
	}

	public void setPropertyStatusValuesSet(Set<PropertyStatusValues> propertyStatusValuesSet) {
		this.propertyStatusValuesSet = propertyStatusValuesSet;
	}

	public Date getPropCreateDate() {
		return propCreateDate;
	}

	public void setPropCreateDate(Date propCreateDate) {
		this.propCreateDate = propCreateDate;
	}

	public Set<Property> getPropertySet() {
		return propertySet;
	}

	public void setPropertySet(Set<Property> propertySet) {
		this.propertySet = propertySet;
	}

	public void addProperty(Property property) {
		getPropertySet().add(property);
	}

	public void removeProperty(Property property) {
		getPropertySet().remove(property);
	}

	public PropertyStatus getStatus() {
		return status;
	}

	public void setStatus(PropertyStatus status) {
		this.status = status;
	}

	public String getGisReferenceNo() {
		return gisReferenceNo;
	}

	public void setGisReferenceNo(String gisReferenceNo) {
		this.gisReferenceNo = gisReferenceNo;
	}

	public Set<PtNotice> getNotices() {
		return this.notices;
}

	public void setNotices(Set<PtNotice> notices) {
		this.notices = notices;
	}

	public void addNotice(PtNotice ptNotice) {
		getNotices().add(ptNotice);
	}

	public void removeNotice(PtNotice ptNotice) {
		getNotices().remove(ptNotice);
	}

	public Set<Objection> getObjections() {
		return objections;
	}

	public void setObjections(Set<Objection> objections) {
		this.objections = objections;
	}

	public void addObjection(Objection objection) {
		getObjections().add(objection);
	}

	public void removeObjection(Objection objection) {
		getObjections().remove(objection);
	}

	public Set<Recovery> getRecoveries() {
		return recoveries;
	}

	public void setRecoveries(Set<Recovery> recoveries) {
		this.recoveries = recoveries;
	}
	public void addRecoveries(Recovery recovery) {
		getRecoveries().add(recovery);
	}

	public void removeRecoveries(Recovery recovery) {
		getRecoveries().remove(recovery);
	}
	/**
	 * <p>
	 * Returns a Map with two key-value pairs
	 * <li>The value of key WFSTATUS gives the work flow status for given
	 * property (TRUE - in work flow, FALSE - not in work flow).</li>
	 * <li>The value of key WFOWNER gives the owner of work flow object if
	 * property is under work flow otherwise gives blank string.</li> Note: This
	 * implementation may change from client to client
	 * </p>
	 * 
	 * @param {@link BasicProperty} basicProperty
	 * @return {@link Map}
	 */
	public Map<String, String> getPropertyWfStatus() {
		Map<String, String> wfMap = new HashMap<String, String>();
		Boolean isPropInWf = Boolean.FALSE;
		String wfOwner = "";
		PropertyImpl wfProperty = null;
		EisCommonsService eisCommonMgr = new EisCommonsServiceImpl();
		for (Property property : getPropertySet()) {
			wfProperty = (PropertyImpl) property;
			if (wfProperty.getStatus().equals(PropertyTaxConstants.STATUS_WORKFLOW)
					|| !wfProperty.getState().getValue().equalsIgnoreCase("END")) {
				break;
			}
			wfProperty = null;
		}
		
		if (wfProperty != null) {
			isPropInWf = Boolean.TRUE;
			wfOwner = eisCommonMgr.getUserforPosition(wfProperty.getState().getOwner()).getFirstName();
		}
		wfMap.put(WFSTATUS, isPropInWf.toString());
		wfMap.put(WFOWNER, wfOwner);
		if(wfMap.get(WFSTATUS).equalsIgnoreCase(Boolean.FALSE.toString())){
			wfMap = propertyInObjectionWf();
		}
		if(wfMap.get(WFSTATUS).equalsIgnoreCase(Boolean.FALSE.toString())){
			wfMap = propertyInRecoverynWf();
		}
		return wfMap;
	}
	
	private Map<String, String>  propertyInObjectionWf(){
		Map<String, String> wfMap = new HashMap<String, String>();
		EisCommonsService eisCommonMgr = new EisCommonsServiceImpl();
		Objection wfObjection = null;
		for (Objection objection : getObjections()) {
			wfObjection = (Objection) objection;
			if (!wfObjection.getState().getValue().equalsIgnoreCase("END")) {
				break;
			}
			wfObjection = null;
		}
		if (wfObjection != null) {
			wfMap.put(WFSTATUS,  Boolean.TRUE.toString());
			wfMap.put(WFOWNER, eisCommonMgr.getUserforPosition(wfObjection.getState().getOwner()).getFirstName());
		}else{
			wfMap.put(WFSTATUS,  Boolean.FALSE.toString());
			wfMap.put(WFOWNER, "");
		}
		
		return wfMap;
	}
	
	private Map<String, String>  propertyInRecoverynWf(){
		Boolean isPropInWf = Boolean.FALSE;
		String wfOwner = "";
		Map<String, String> wfMap = new HashMap<String, String>();
		EisCommonsService eisCommonMgr = new EisCommonsServiceImpl();
		for (Recovery recovery : getRecoveries()) {
			if (!recovery.getState().getValue().equalsIgnoreCase("END")) {
				isPropInWf = Boolean.TRUE;
				wfOwner = eisCommonMgr.getUserforPosition(recovery.getState().getOwner()).getFirstName();
				break;
			}
			
		}
		wfMap.put(WFSTATUS, isPropInWf.toString());
		wfMap.put(WFOWNER, wfOwner);
		return wfMap;
	}
	
	/**
	 * Returns the default and workflow current installment Property for
	 * basicproperty.
	 * 
	 * @return Property
	 */
	public Property getWFProperty() {
		Property returnProperty = null;
		for (Property property : getPropertySet()) {
			if (property.getStatus().equals('W') && property.getIsDefaultProperty().equals('Y')) {
				returnProperty = property;
				break;
			}
		}
		return returnProperty;
	}

	public Character getIsMigrated() {
		return isMigrated;
	}

	public void setIsMigrated(Character isMigrated) {
		this.isMigrated = isMigrated;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public Boolean getAllChangesCompleted() {
		return allChangesCompleted;
	}

	public void setAllChangesCompleted(Boolean allChangesCompleted) {
		this.allChangesCompleted = allChangesCompleted;
	}
	
}
