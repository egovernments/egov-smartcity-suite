/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
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

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.recovery.Recovery;
import org.egov.ptis.notice.PtNotice;

public class BasicPropertyImpl extends BaseModel implements BasicProperty {
    private static final long serialVersionUID = 7842150965429140561L;
    private Boolean active;
    private String upicNo;
    private PropertyID propertyID;
    private PropertyAddress address;
    private Boundary boundary;
    private String oldMuncipalNum;
    private Set<PropertyStatusValues> propertyStatusValuesSet = new HashSet<>();
    private Set<PropertyMutation> propertyMutations = new HashSet<>();
    private PropertyMutationMaster propertyMutationMaster;
    private Date propOccupationDate;
    private Set<Property> propertySet = new HashSet<>();
    private PropertyStatus status;
    private String extraField1;
    private String extraField2;
    private String extraField3;
    private String gisReferenceNo;
    private String partNo;
    private Set<PtNotice> notices = new HashSet<>();
    private Set<RevisionPetition> objections = new HashSet<>();
    private Set<Recovery> recoveries = new HashSet<>();
    private Character source = 'A';
    private Set<PropertyDocs> propertyDocsSet = new HashSet<>();
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
    private List<PropertyOwnerInfo> propertyOwnerInfo = new ArrayList<>();
    private List<PropertyOwnerInfo> propertyOwnerInfoProxy = new ArrayList<>();
    private boolean underWorkflow;
    private Date assessmentdate;
    private List<VacancyRemission> vacancyRemissions = new ArrayList<>();
    private Double longitude;
    private Double latitude;
    private boolean eligible;
    private List<Amalgamation> amalgamations = new ArrayList<>();
    private List<Amalgamation> amalgamationsProxy = new ArrayList<>();
    private Character isIntgBillCreated = 'N';

    @Override
    public List<PropertyOwnerInfo> getPropertyOwnerInfo() {
        return propertyOwnerInfo;
    }

    @Override
    public void setPropertyOwnerInfo(final List<PropertyOwnerInfo> propertyOwnerSet) {
        propertyOwnerInfo = propertyOwnerSet;
    }

    @Override
    public Set<PropertyDocs> getPropertyDocsSet() {
        return propertyDocsSet;
    }

    @Override
    public void addPropertyOwners(final PropertyOwnerInfo ownerInfo) {
        getPropertyOwnerInfo().add(ownerInfo);
    }

    @Override
    public void removePropertyOwners(final PropertyOwnerInfo ownerInfo) {
        getPropertyOwnerInfo().remove(ownerInfo);
    }

    @Override
    public void setPropertyDocsSet(final Set<PropertyDocs> propertyDocsSet) {
        this.propertyDocsSet = propertyDocsSet;
    }

    @Override
    public void addDocs(final PropertyDocs propertyDocs) {
        getPropertyDocsSet().add(propertyDocs);
    }

    @Override
    public void removeDocs(final PropertyDocs propertyDocs) {
        getPropertyDocsSet().remove(propertyDocs);
    }

    @Override
    public PropertyID getPropertyID() {
        return propertyID;
    }

    @Override
    public void setPropertyID(final PropertyID propertyID) {
        this.propertyID = propertyID;
    }

    @Override
    public Boolean getActive() {
        return active;
    }

    @Override
    public Boolean isActive() {
        return active;
    }

    @Override
    public void setActive(final Boolean active) {
        this.active = active;
    }

    /**
     * Returns the default and non-history current installment Property for basicproperty.
     *
     * @return Property
     */
    @Override
    public Property getProperty() {
        Property returnProperty = null;
        for (final Property property : getPropertySet())
            if (property.getIsDefaultProperty().equals('Y')
                    && (property.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE) || property
                            .getStatus().equals(PropertyTaxConstants.STATUS_ISACTIVE)))
                returnProperty = property;
        return returnProperty;
    }

    @Override
    public Boundary getBoundary() {
        return boundary;
    }

    @Override
    public void setBoundary(final Boundary boundary) {
        this.boundary = boundary;
    }

    @Override
    public List<ValidationError> validate() {
        return new ArrayList<>();
    }

    @Override
    public boolean validateBasicProp() {
        if (getAddress() == null)
            throw new ApplicationRuntimeException(
                    "BasicProperty validation failed: Address is not set, Please Check !!");

        if (getCreatedBy() == null)
            throw new ApplicationRuntimeException(
                    "BasicProperty validation failed: CreatedBy is not set, Please Check !!");

        if (getPropertyID() == null)
            throw new ApplicationRuntimeException(
                    "BasicProperty validation failed: PropertyID is not set, Please Check !!");

        return true;
    }

    @Override
    public String getUpicNo() {
        return upicNo;
    }

    @Override
    public void setUpicNo(final String upicNo) {
        this.upicNo = upicNo;
    }

    @Override
    public String getOldMuncipalNum() {
        return oldMuncipalNum;
    }

    @Override
    public void setOldMuncipalNum(final String oldMuncipalNum) {
        this.oldMuncipalNum = oldMuncipalNum;
    }

    @Override
    public void addPropertyStatusValues(final PropertyStatusValues propertyStatusValues) {
        getPropertyStatusValuesSet().add(propertyStatusValues);
    }

    @Override
    public void removePropertyStatusValues(final PropertyStatusValues propertyStatusValues) {
        getPropertyStatusValuesSet().remove(propertyStatusValues);
    }

    @Override
    public String getExtraField1() {
        return extraField1;
    }

    @Override
    public void setExtraField1(final String extraField1) {
        this.extraField1 = extraField1;
    }

    @Override
    public String getExtraField2() {
        return extraField2;
    }

    @Override
    public void setExtraField2(final String extraField2) {
        this.extraField2 = extraField2;
    }

    /*
     * @Override public PropertyReference getPropertyReference() { return propertyReference; }
     */

    @Override
    public void setPropertyReference(final PropertyReference propertyReference) {
        //Do Nothing
    }

    @Override
    public Set<PropertyMutation> getPropertyMutations() {
        return propertyMutations;
    }

    @Override
    public void setPropertyMutations(final Set<PropertyMutation> propertyMutations) {
        this.propertyMutations = propertyMutations;
    }

    @Override
    public PropertyMutationMaster getPropertyMutationMaster() {
        return propertyMutationMaster;
    }

    @Override
    public void setPropertyMutationMaster(final PropertyMutationMaster propertyMutationMaster) {
        this.propertyMutationMaster = propertyMutationMaster;
    }

    @Override
    public String getExtraField3() {
        return extraField3;
    }

    @Override
    public void setExtraField3(final String extraField3) {
        this.extraField3 = extraField3;
    }

    @Override
    public Set<PropertyStatusValues> getPropertyStatusValuesSet() {
        return propertyStatusValuesSet;
    }

    @Override
    public void setPropertyStatusValuesSet(final Set<PropertyStatusValues> propertyStatusValuesSet) {
        this.propertyStatusValuesSet = propertyStatusValuesSet;
    }

    @Override
    public Set<Property> getPropertySet() {
        return propertySet;
    }

    @Override
    public void setPropertySet(final Set<Property> propertySet) {
        this.propertySet = propertySet;
    }

    @Override
    public void addProperty(final Property property) {
        getPropertySet().add(property);
    }

    @Override
    public void removeProperty(final Property property) {
        getPropertySet().remove(property);
    }

    @Override
    public PropertyStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(final PropertyStatus status) {
        this.status = status;
    }

    @Override
    public String getGisReferenceNo() {
        return gisReferenceNo;
    }

    @Override
    public void setGisReferenceNo(final String gisReferenceNo) {
        this.gisReferenceNo = gisReferenceNo;
    }

    @Override
    public Set<PtNotice> getNotices() {
        return notices;
    }

    @Override
    public void setNotices(final Set<PtNotice> notices) {
        this.notices = notices;
    }

    @Override
    public void addNotice(final PtNotice ptNotice) {
        getNotices().add(ptNotice);
    }

    @Override
    public void removeNotice(final PtNotice ptNotice) {
        getNotices().remove(ptNotice);
    }

    @Override
    public Set<RevisionPetition> getObjections() {
        return objections;
    }

    @Override
    public void setObjections(final Set<RevisionPetition> objections) {
        this.objections = objections;
    }

    @Override
    public void addObjection(final RevisionPetition objection) {
        getObjections().add(objection);
    }

    @Override
    public void removeObjection(final RevisionPetition objection) {
        getObjections().remove(objection);
    }

    @Override
    public Set<Recovery> getRecoveries() {
        return recoveries;
    }

    @Override
    public void setRecoveries(final Set<Recovery> recoveries) {
        this.recoveries = recoveries;
    }

    @Override
    public void addRecoveries(final Recovery recovery) {
        getRecoveries().add(recovery);
    }

    @Override
    public void removeRecoveries(final Recovery recovery) {
        getRecoveries().remove(recovery);
    }

    /**
     * <p>
     * Returns a Map with two key-value pairs
     * <li>The value of key WFSTATUS gives the work flow status for given property (TRUE - in work flow, FALSE - not in work
     * flow).</li>
     * <li>The value of key WFOWNER gives the owner of work flow object if property is under work flow otherwise gives blank
     * string.</li> Note: This implementation may change from client to client
     * </p>
     *
     * @param {@link BasicProperty} basicProperty
     * @return {@link Map}
     */
    @Override
    public Map<String, String> getPropertyWfStatus() {
        Map<String, String> wfMap = new HashMap<>();
        Boolean isPropInWf = Boolean.FALSE;
        String wfOwner = "";
        PropertyImpl wfProperty = null;
        for (final Property property : getPropertySet()) {
            wfProperty = (PropertyImpl) property;
            if (wfProperty.hasState() && wfProperty.getStatus().equals(PropertyTaxConstants.STATUS_WORKFLOW))
                break;
            wfProperty = null;
        }

        if (wfProperty != null) {
            isPropInWf = Boolean.TRUE;
            wfOwner = wfProperty.getState() != null ? wfProperty.getState().getOwnerPosition().getName() : "";
        }
        wfMap.put(WFSTATUS, isPropInWf.toString());
        wfMap.put(WFOWNER, wfOwner);
        if (wfMap.get(WFSTATUS).equalsIgnoreCase(Boolean.FALSE.toString()))
            wfMap = propertyInObjectionWf();
        if (wfMap.get(WFSTATUS).equalsIgnoreCase(Boolean.FALSE.toString()))
            wfMap = propertyInRecoverynWf();
        return wfMap;
    }

    private Map<String, String> propertyInObjectionWf() {
        final Map<String, String> wfMap = new HashMap<>();
        RevisionPetition wfObjection = null;
        for (final RevisionPetition objection : getObjections()) {
            wfObjection = objection;
            if (wfObjection.hasState() && !wfObjection.transitionCompleted())
                break;
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
        final Map<String, String> wfMap = new HashMap<>();
        for (final Recovery recovery : getRecoveries())
            if (!recovery.transitionCompleted()) {
                isPropInWf = Boolean.TRUE;
                wfOwner = recovery.getState().getOwnerUser().getName();
                break;
            }
        wfMap.put(WFSTATUS, isPropInWf.toString());
        wfMap.put(WFOWNER, wfOwner);
        return wfMap;
    }

    /**
     * Returns the default and workflow current installment Property for basicproperty.
     *
     * @return Property
     */
    @Override
    public Property getWFProperty() {
        Property returnProperty = null;
        for (final Property property : getPropertySet())
            if (property.getStatus().equals('W') && property.getIsDefaultProperty().equals('Y')) {
                returnProperty = property;
                break;
            }
        return returnProperty;
    }

    @Override
    public String getPartNo() {
        return partNo;
    }

    @Override
    public void setPartNo(final String partNo) {
        this.partNo = partNo;
    }

    @Override
    public Boolean getAllChangesCompleted() {
        return allChangesCompleted;
    }

    @Override
    public void setAllChangesCompleted(final Boolean allChangesCompleted) {
        this.allChangesCompleted = allChangesCompleted;
    }

    @Override
    public Character getIsBillCreated() {
        return isBillCreated;
    }

    @Override
    public void setIsBillCreated(final Character isBillCreated) {
        this.isBillCreated = isBillCreated;
    }

    @Override
    public String getBillCrtError() {
        return billCrtError;
    }

    @Override
    public void setBillCrtError(final String billCrtError) {
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
     * @param isTaxXMLMigrated the isTaxXMLMigrated to set
     */
    @Override
    public void setIsTaxXMLMigrated(final Character isTaxXMLMigrated) {
        this.isTaxXMLMigrated = isTaxXMLMigrated;
    }

    @Override
    public boolean getIsDemandActive() {
        return isDemandActive;
    }

    @Override
    public void setIsDemandActive(final boolean isDemandActive) {
        this.isDemandActive = isDemandActive;
    }

    /**
     * Gives the Active property i.e., PropertyImpl.status = 'A'
     */
    @Override
    public PropertyImpl getActiveProperty() {
        return activeProperty;
    }

    /**
     * Gives the Inactive property i.e., PropertyImpl.status = 'I' Inactive property is the property whose demand is not active
     */
    @Override
    public PropertyImpl getInactiveProperty() {
        return inactiveProperty;
    }

    @Override
    public void setActiveProperty(final PropertyImpl activeProperty) {
        this.activeProperty = activeProperty;
    }

    @Override
    public void setInactiveProperty(final PropertyImpl inactiveProperty) {
        this.inactiveProperty = inactiveProperty;
    }

    @Override
    public String getVacantLandAssmtNo() {
        return vacantLandAssmtNo;
    }

    @Override
    public void setVacantLandAssmtNo(final String vacantLandAssmtNo) {
        this.vacantLandAssmtNo = vacantLandAssmtNo;
    }

    @Override
    public String getRegdDocNo() {
        return regdDocNo;
    }

    @Override
    public void setRegdDocNo(final String regdDocNo) {
        this.regdDocNo = regdDocNo;
    }

    @Override
    public Date getRegdDocDate() {
        return regdDocDate;
    }

    @Override
    public void setRegdDocDate(final Date regdDocDate) {
        this.regdDocDate = regdDocDate;
    }

    @Override
    public PropertyAddress getAddress() {
        return address;
    }

    @Override
    public void setAddress(final PropertyAddress address) {
        this.address = address;
    }

    @Override
    public Date getPropOccupationDate() {
        return propOccupationDate;
    }

    @Override
    public void setPropOccupationDate(final Date propOccupationDate) {
        this.propOccupationDate = propOccupationDate;

    }

    @Override
    public boolean isUnderWorkflow() {
        return underWorkflow;
    }

    @Override
    public void setUnderWorkflow(final boolean underWorkflow) {
        this.underWorkflow = underWorkflow;
    }

    @Override
    public String getFullOwnerName() {
        final StringBuilder ownerName = new StringBuilder();
        for (final PropertyOwnerInfo ownerInfo : getPropertyOwnerInfo())
            ownerName.append(ownerInfo.getOwner().getName()).append(", ");
        if (ownerName.length() > 2)
            ownerName.deleteCharAt(ownerName.length() - 2);
        return ownerName.toString();
    }

    @Override
    public User getPrimaryOwner() {
        User user = new User();
        for (final PropertyOwnerInfo ownerInfo : getPropertyOwnerInfo()) {
            user = ownerInfo.getOwner();
            break;
        }
        return user;
    }

    @Override
    public String getMobileNumber() {
        return getPropertyOwnerInfo().get(0).getOwner().getMobileNumber();
    }

    @Override
    public String getAadharNumber() {
        return getPropertyOwnerInfo().get(0).getOwner().getAadhaarNumber();
    }

    @Override
    public Map<String, String> getOwnerMap() {
        Map<String, String> ownerMap = new HashMap<>();
        final StringBuilder ownerName = new StringBuilder();
        final StringBuilder mobileNo = new StringBuilder();
        final StringBuilder aadharNo = new StringBuilder();
        User owner = null;
        for (final PropertyOwnerInfo ownerInfo : getPropertyOwnerInfo()) {
            owner = ownerInfo.getOwner();
            ownerName.append(owner.getName()).append(", ");
            if (owner.getMobileNumber() != null && owner.getMobileNumber().trim().length() > 0)
                mobileNo.append(owner.getMobileNumber()).append(", ");
            if (owner.getAadhaarNumber() != null && owner.getAadhaarNumber().trim().length() > 0)
                aadharNo.append(owner.getAadhaarNumber()).append(", ");
        }
        if (ownerName.length() > 2)
            ownerName.deleteCharAt(ownerName.length() - 2);
        if (mobileNo.length() > 2)
            mobileNo.deleteCharAt(mobileNo.length() - 2);
        if (aadharNo.length() > 2)
            aadharNo.deleteCharAt(aadharNo.length() - 2);

        ownerMap.put("OWNERNAME", ownerName.toString());
        ownerMap.put("MOBILENO", mobileNo.toString());
        ownerMap.put("AADHARNO", aadharNo.toString());
        return ownerMap;
    }

    @Override
    public List<PropertyOwnerInfo> getPropertyOwnerInfoProxy() {
        return propertyOwnerInfoProxy;
    }

    @Override
    public void setPropertyOwnerInfoProxy(final List<PropertyOwnerInfo> propertyOwnerInfoProxy) {
        this.propertyOwnerInfoProxy = propertyOwnerInfoProxy;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (upicNo == null ? 0 : upicNo.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BasicPropertyImpl other = (BasicPropertyImpl) obj;
        if (upicNo == null) {
            if (other.upicNo != null)
                return false;
        } else if (!upicNo.equals(other.upicNo))
            return false;
        return true;
    }

    @Override
    public Character getSource() {
        return source;
    }

    @Override
    public void setSource(final Character source) {
        this.source = source;

    }

    @Override
    public Date getAssessmentdate() {
        return assessmentdate;
    }

    @Override
    public void setAssessmentdate(Date assessmentdate) {
        this.assessmentdate = assessmentdate;
    }

    @Override
    public List<VacancyRemission> getVacancyRemissions() {
        return vacancyRemissions;
    }

    @Override
    public void setVacancyRemissions(List<VacancyRemission> vacancyRemissions) {
        this.vacancyRemissions = vacancyRemissions;
    }

    @Override
    public Property getPropertyForBasicProperty() {
        if (null != this.getProperty())
            return this.getProperty();
        else
            return this.getActiveProperty();
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean isEligible() {
        return eligible;
    }

    @Override
    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    @Override
    public List<Amalgamation> getAmalgamations() {
        return amalgamations;
    }

    @Override
    public void setAmalgamations(List<Amalgamation> amalgamations) {
        this.amalgamations = amalgamations;
    }

    @Override
    public List<Amalgamation> getAmalgamationsProxy() {
        return amalgamationsProxy;
    }

    @Override
    public void setAmalgamationsProxy(List<Amalgamation> amalgamationsProxy) {
        this.amalgamationsProxy = amalgamationsProxy;
    }

    @Override
    public void addAmalgamations(Amalgamation amalgamation) {
        getAmalgamations().add(amalgamation);
    }

    public Character getIsIntgBillCreated() {
        return isIntgBillCreated;
    }

    public void setIsIntgBillCreated(Character isIntgBillCreated) {
        this.isIntgBillCreated = isIntgBillCreated;
    }

}
