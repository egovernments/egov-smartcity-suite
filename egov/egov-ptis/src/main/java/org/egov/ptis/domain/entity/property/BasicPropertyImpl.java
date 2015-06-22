/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.entity.property;

import static org.egov.ptis.constants.PropertyTaxConstants.WFOWNER;
import static org.egov.ptis.constants.PropertyTaxConstants.WFSTATUS;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.Address;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
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
	private String applicationNo;
	private Boolean active;
	private String upicNo;
	private PropertyID propertyID;
	private PropertyAddress address;
	private Boundary boundary;
	private String oldMuncipalNum;
	private Set<PropertyStatusValues> propertyStatusValuesSet = new HashSet<PropertyStatusValues>();
	private Set<PropertyMutation> propMutationSet = new HashSet<PropertyMutation>();
	private PropertyMutationMaster propertyMutationMaster;
	private Date propOccupationDate;
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
	private Character isBillCreated;
	private String billCrtError;
	private Character isTaxXMLMigrated = 'N';
	private boolean isDemandActive = false;
	private PropertyImpl activeProperty;
	private PropertyImpl inactiveProperty;
	private String regdDocNo;
	private Date regdDocDate;
	private String vacantLandAssmtNo;
	private String source;

	@Override
	public Set<PropertyDocs> getPropertyDocsSet() {
		return propertyDocsSet;
	}

	@Override
	public void setPropertyDocsSet(Set<PropertyDocs> propertyDocsSet) {
		this.propertyDocsSet = propertyDocsSet;
	}

	@Override
	public void addDocs(PropertyDocs propertyDocs) {
		getPropertyDocsSet().add(propertyDocs);
	}

	@Override
	public void removeDocs(PropertyDocs propertyDocs) {
		getPropertyDocsSet().remove(propertyDocs);
	}

	@Override
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

	@Override
	public int hashCode() {
		int hashCode = 0;

		if (getId() != null) {
			hashCode = hashCode + getId().hashCode();
		}
		if (getPropertyID() != null && getAddress() != null) {
			hashCode = hashCode + isActive().hashCode()
					+ getAddress().hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Id: ").append(getId()).append("|");
		sb = (upicNo != null && !upicNo.isEmpty()) ? sb.append("UpicNo: ")
				.append(upicNo) : sb.append("");
		sb = (getBoundary() != null) ? sb.append("|Boundary: ").append(
				getBoundary().getName()) : sb.append("");
		sb = (getStatus() != null) ? sb.append("|Status").append(
				status.getName()) : sb.append("");
		sb.append("Address: ").append(getAddress());

		return sb.toString();
	}

	@Override
	public PropertyID getPropertyID() {
		return propertyID;
	}

	@Override
	public void setPropertyID(PropertyID propertyID) {
		this.propertyID = propertyID;
	}

	public Boolean getActive() {
		return active;
	}

	@Override
	public Boolean isActive() {
		return active;
	}

	@Override
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Returns the default and non-history current installment Property for
	 * basicproperty.
	 *
	 * @return Property
	 */
	@Override
	public Property getProperty() {
		Property returnProperty = null;
		for (Property property : getPropertySet()) {
			if (property.getIsDefaultProperty().equals('Y')
					&& (property.getStatus().equals(
							PropertyTaxConstants.STATUS_DEMAND_INACTIVE) || property
							.getStatus().equals(
									PropertyTaxConstants.STATUS_ISACTIVE))) {
				returnProperty = property;
			}
		}
		return returnProperty;
	}

	@Override
	public Boundary getBoundary() {
		return boundary;
	}

	@Override
	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

	@Override
	public List<ValidationError> validate() {
		return new ArrayList<ValidationError>();
	}

	@Override
	public boolean validateBasicProp() {
		if ((getAddress() == null))
			throw new EGOVRuntimeException(
					"BasicProperty validation failed: Address is not set, Please Check !!");

		if (getCreatedBy() == null)
			throw new EGOVRuntimeException(
					"BasicProperty validation failed: CreatedBy is not set, Please Check !!");

		if (getPropertyID() == null)
			throw new EGOVRuntimeException(
					"BasicProperty validation failed: PropertyID is not set, Please Check !!");

		return true;
	}

	@Override
	public String getUpicNo() {
		return upicNo;
	}

	@Override
	public void setUpicNo(String upicNo) {
		this.upicNo = upicNo;
	}

	@Override
	public String getOldMuncipalNum() {
		return oldMuncipalNum;
	}

	@Override
	public void setOldMuncipalNum(String oldMuncipalNum) {
		this.oldMuncipalNum = oldMuncipalNum;
	}

	@Override
	public void addPropertyStatusValues(
			PropertyStatusValues propertyStatusValues) {
		getPropertyStatusValuesSet().add(propertyStatusValues);
	}

	@Override
	public void removePropertyStatusValues(
			PropertyStatusValues propertyStatusValues) {
		getPropertyStatusValuesSet().remove(propertyStatusValues);
	}

	@Override
	public String getExtraField1() {
		return extraField1;
	}

	@Override
	public void setExtraField1(String extraField1) {
		this.extraField1 = extraField1;
	}

	@Override
	public String getExtraField2() {
		return extraField2;
	}

	@Override
	public void setExtraField2(String extraField2) {
		this.extraField2 = extraField2;
	}

	/*@Override
	public PropertyReference getPropertyReference() {
		return propertyReference;
	}*/

	@Override
	public void setPropertyReference(PropertyReference propertyReference) {
		this.propertyReference = propertyReference;
	}

	@Override
	public Set<PropertyMutation> getPropMutationSet() {
		return propMutationSet;
	}

	@Override
	public void setPropMutationSet(Set<PropertyMutation> propMutationSet) {
		this.propMutationSet = propMutationSet;
	}

	@Override
	public PropertyMutationMaster getPropertyMutationMaster() {
		return propertyMutationMaster;
	}

	@Override
	public void setPropertyMutationMaster(
			PropertyMutationMaster propertyMutationMaster) {
		this.propertyMutationMaster = propertyMutationMaster;
	}

	@Override
	public String getExtraField3() {
		return extraField3;
	}

	@Override
	public void setExtraField3(String extraField3) {
		this.extraField3 = extraField3;
	}

	@Override
	public Set<PropertyStatusValues> getPropertyStatusValuesSet() {
		return propertyStatusValuesSet;
	}

	@Override
	public void setPropertyStatusValuesSet(
			Set<PropertyStatusValues> propertyStatusValuesSet) {
		this.propertyStatusValuesSet = propertyStatusValuesSet;
	}

	@Override
	public Set<Property> getPropertySet() {
		return propertySet;
	}

	@Override
	public void setPropertySet(Set<Property> propertySet) {
		this.propertySet = propertySet;
	}

	@Override
	public void addProperty(Property property) {
		getPropertySet().add(property);
	}

	@Override
	public void removeProperty(Property property) {
		getPropertySet().remove(property);
	}

	@Override
	public PropertyStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(PropertyStatus status) {
		this.status = status;
	}

	@Override
	public String getGisReferenceNo() {
		return gisReferenceNo;
	}

	@Override
	public void setGisReferenceNo(String gisReferenceNo) {
		this.gisReferenceNo = gisReferenceNo;
	}

	@Override
	public Set<PtNotice> getNotices() {
		return this.notices;
	}

	@Override
	public void setNotices(Set<PtNotice> notices) {
		this.notices = notices;
	}

	@Override
	public void addNotice(PtNotice ptNotice) {
		getNotices().add(ptNotice);
	}

	@Override
	public void removeNotice(PtNotice ptNotice) {
		getNotices().remove(ptNotice);
	}

	@Override
	public Set<Objection> getObjections() {
		return objections;
	}

	@Override
	public void setObjections(Set<Objection> objections) {
		this.objections = objections;
	}

	@Override
	public void addObjection(Objection objection) {
		getObjections().add(objection);
	}

	@Override
	public void removeObjection(Objection objection) {
		getObjections().remove(objection);
	}

	@Override
	public Set<Recovery> getRecoveries() {
		return recoveries;
	}

	@Override
	public void setRecoveries(Set<Recovery> recoveries) {
		this.recoveries = recoveries;
	}

	@Override
	public void addRecoveries(Recovery recovery) {
		getRecoveries().add(recovery);
	}

	@Override
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
	@Override
	public Map<String, String> getPropertyWfStatus() {
		Map<String, String> wfMap = new HashMap<String, String>();
		Boolean isPropInWf = Boolean.FALSE;
		String wfOwner = "";
		PropertyImpl wfProperty = null;
		for (Property property : getPropertySet()) {
		        wfProperty = (PropertyImpl) property;
			if (wfProperty.hasState() && (wfProperty.getStatus().equals(PropertyTaxConstants.STATUS_WORKFLOW) || !wfProperty.stateIsEnded())) {
			    break;
			}
			wfProperty = null;
		}

		if (wfProperty != null) {
			isPropInWf = Boolean.TRUE;
			wfOwner = wfProperty.getState().getOwnerPosition().getName();
		}
		wfMap.put(WFSTATUS, isPropInWf.toString());
		wfMap.put(WFOWNER, wfOwner);
		if (wfMap.get(WFSTATUS).equalsIgnoreCase(Boolean.FALSE.toString())) {
			wfMap = propertyInObjectionWf();
		}
		if (wfMap.get(WFSTATUS).equalsIgnoreCase(Boolean.FALSE.toString())) {
			wfMap = propertyInRecoverynWf();
		}
		return wfMap;
	}

	private Map<String, String> propertyInObjectionWf() {
		Map<String, String> wfMap = new HashMap<String, String>();
		Objection wfObjection = null;
		for (Objection objection : getObjections()) {
			wfObjection = objection;
			if (!wfObjection.getState().getValue().equalsIgnoreCase("END")) {
				break;
			}
			wfObjection = null;
		}
		if (wfObjection != null) {
			wfMap.put(WFSTATUS, Boolean.TRUE.toString());
			wfMap.put(WFOWNER, wfObjection.getState().getOwnerUser().getName());
		} else {
			wfMap.put(WFSTATUS, Boolean.FALSE.toString());
			wfMap.put(WFOWNER, "");
		}

		return wfMap;
	}

	private Map<String, String> propertyInRecoverynWf() {
		Boolean isPropInWf = Boolean.FALSE;
		String wfOwner = "";
		Map<String, String> wfMap = new HashMap<String, String>();
		for (Recovery recovery : getRecoveries()) {
			if (!recovery.getState().getValue().equalsIgnoreCase("END")) {
				isPropInWf = Boolean.TRUE;
				wfOwner = recovery.getState().getOwnerUser().getName();
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
	@Override
	public Property getWFProperty() {
		Property returnProperty = null;
		for (Property property : getPropertySet()) {
			if (property.getStatus().equals('W')
					&& property.getIsDefaultProperty().equals('Y')) {
				returnProperty = property;
				break;
			}
		}
		return returnProperty;
	}

	@Override
	public Character getIsMigrated() {
		return isMigrated;
	}

	@Override
	public void setIsMigrated(Character isMigrated) {
		this.isMigrated = isMigrated;
	}

	@Override
	public String getPartNo() {
		return partNo;
	}

	@Override
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	@Override
	public Boolean getAllChangesCompleted() {
		return allChangesCompleted;
	}

	@Override
	public void setAllChangesCompleted(Boolean allChangesCompleted) {
		this.allChangesCompleted = allChangesCompleted;
	}

	@Override
	public Character getIsBillCreated() {
		return isBillCreated;
	}

	@Override
	public void setIsBillCreated(Character isBillCreated) {
		this.isBillCreated = isBillCreated;
	}

	@Override
	public String getBillCrtError() {
		return billCrtError;
	}

	@Override
	public void setBillCrtError(String billCrtError) {
		this.billCrtError = billCrtError;
	}

	/**
	 * @return the isTaxXMLMigrated
	 */
	@Override
	public Character getIsTaxXMLMigrated() {
		return isTaxXMLMigrated;
	}

	/**
	 * @param isTaxXMLMigrated
	 *            the isTaxXMLMigrated to set
	 */
	@Override
	public void setIsTaxXMLMigrated(Character isTaxXMLMigrated) {
		this.isTaxXMLMigrated = isTaxXMLMigrated;
	}

	@Override
	public boolean getIsDemandActive() {
		return isDemandActive;
	}

	@Override
	public void setIsDemandActive(boolean isDemandActive) {
		this.isDemandActive = isDemandActive;
	}

	/**
	 * Gives the Active property i.e., PropertyImpl.status = 'A'
	 *
	 */
	@Override
	public PropertyImpl getActiveProperty() {
		return activeProperty;
	}

	/**
	 * Gives the Inactive property i.e., PropertyImpl.status = 'I'
	 *
	 * Inactive property is the property whose demand is not active
	 */
	@Override
	public PropertyImpl getInactiveProperty() {
		return inactiveProperty;
	}

	public void setActiveProperty(PropertyImpl activeProperty) {
		this.activeProperty = activeProperty;
	}

	public void setInactiveProperty(PropertyImpl inactiveProperty) {
		this.inactiveProperty = inactiveProperty;
	}

	@Override
	public String getApplicationNo() {
		return applicationNo;
	}

	@Override
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	@Override
	public String getVacantLandAssmtNo() {
		return vacantLandAssmtNo;
	}

	@Override
	public void setVacantLandAssmtNo(String vacantLandAssmtNo) {
		this.vacantLandAssmtNo = vacantLandAssmtNo;
	}

	@Override
	public String getRegdDocNo() {
		return regdDocNo;
	}

	@Override
	public void setRegdDocNo(String regdDocNo) {
		this.regdDocNo = regdDocNo;
	}

	@Override
	public Date getRegdDocDate() {
		return regdDocDate;
	}

	@Override
	public void setRegdDocDate(Date regdDocDate) {
		this.regdDocDate = regdDocDate;
	}

	@Override
	public PropertyAddress getAddress() {
		return address;
	}

	@Override
	public void setAddress(PropertyAddress address) {
        this.address = address;		
	}

	@Override
	public Date getPropOccupationDate() {
		return propOccupationDate;
	}

	@Override
	public void setPropOccupationDate(Date propOccupationDate) {
		this.propOccupationDate = propOccupationDate;
		
	}

	@Override
	public String getSource() {
		return source;
	}

	@Override
	public void setSource(String source) {
		this.source = source;
	}
	
}
