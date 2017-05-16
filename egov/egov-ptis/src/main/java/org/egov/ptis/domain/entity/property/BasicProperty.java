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

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.recovery.Recovery;
import org.egov.ptis.notice.PtNotice;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the interface for the BasicProperty which represents the Basic, Minimum details for the Property. Every Basicproprty
 * should have at least one Property associated with it
 *
 * @author Neetu
 * @version 2.00
 * @see BasicPropertyImpl.java
 */
public interface BasicProperty extends Serializable {

    Long getId();

    void setId(Long id);

    User getCreatedBy();

    void setCreatedBy(User createdBy);

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    User getModifiedBy();

    void setModifiedBy(User modifiedBy);

    Date getModifiedDate();

    void setModifiedDate(Date modifiedDate);

    Boolean isActive();

    PropertyID getPropertyID();

    void setPropertyID(PropertyID propertyID);

    PropertyAddress getAddress();

    void setAddress(PropertyAddress address);

    Property getProperty();

    String getUpicNo();

    void setUpicNo(String UpicNo);

    String getOldMuncipalNum();

    void setOldMuncipalNum(String oldMuncipalNum);

    Boundary getBoundary();

    void setBoundary(Boundary boundary);

    Set<PropertyStatusValues> getPropertyStatusValuesSet();

    void setPropertyStatusValuesSet(Set<PropertyStatusValues> propertyStatusValuesSet);

    void addPropertyStatusValues(PropertyStatusValues propertyStatusValues);

    void removePropertyStatusValues(PropertyStatusValues propertyStatusValues);

    void setPropertyReference(PropertyReference propertyReference);

    // PropertyReference getPropertyReference();

    Set<PropertyMutation> getPropertyMutations();

    void setPropertyMutations(Set<PropertyMutation> propMutationSet);

    PropertyMutationMaster getPropertyMutationMaster();

    void setPropertyMutationMaster(PropertyMutationMaster propertyMutationMaster);

    Date getPropOccupationDate();

    void setPropOccupationDate(Date propOccupationDate);

    Set<Property> getPropertySet();

    void setPropertySet(Set<Property> propertySet);

    void addProperty(Property property);

    void removeProperty(Property property);

    PropertyStatus getStatus();

    void setStatus(PropertyStatus propertyStatus);

    String getExtraField1();

    void setExtraField1(String extraField1);

    String getExtraField2();

    void setExtraField2(String extraField2);

    String getExtraField3();

    void setExtraField3(String extraField3);

    boolean validateBasicProp();

    String getGisReferenceNo();

    void setGisReferenceNo(String gisReferenceNo);

    Set<PtNotice> getNotices();

    void setNotices(Set<PtNotice> noticeForms);

    void addNotice(PtNotice ptNotice);

    void removeNotice(PtNotice ptNotice);

    Set<RevisionPetition> getObjections();

    void setObjections(Set<RevisionPetition> objections);

    void addObjection(RevisionPetition objection);

    void removeObjection(RevisionPetition objection);

    Map<String, String> getPropertyWfStatus();

    Set<Recovery> getRecoveries();

    void setRecoveries(Set<Recovery> recoveries);

    void addRecoveries(Recovery recovery);

    void removeRecoveries(Recovery recovery);

    Set<PropertyDocs> getPropertyDocsSet();

    void setPropertyDocsSet(Set<PropertyDocs> propertyDocsSet);

    void addDocs(PropertyDocs propertyDocs);

    void removeDocs(PropertyDocs propertyDocs);

    String getPartNo();

    void setPartNo(String partNo);

    Boolean getAllChangesCompleted();

    void setAllChangesCompleted(Boolean allChangesCompleted);

    Property getWFProperty();

    Character getIsBillCreated();

    void setIsBillCreated(Character isBillCreated);

    String getBillCrtError();

    void setBillCrtError(String billCrtError);

    Character getIsTaxXMLMigrated();

    void setIsTaxXMLMigrated(Character isTaxXMLMigrated);

    boolean getIsDemandActive();

    void setIsDemandActive(boolean isDemandActive);

    PropertyImpl getActiveProperty();

    void setActiveProperty(PropertyImpl activeProperty);

    PropertyImpl getInactiveProperty();

    void setInactiveProperty(PropertyImpl inactiveProperty);

    String getVacantLandAssmtNo();

    void setVacantLandAssmtNo(String vacantLandAssmtNo);

    String getRegdDocNo();

    void setRegdDocNo(String regdDocNo);

    Date getRegdDocDate();

    void setRegdDocDate(Date regdDocDate);

    List<PropertyOwnerInfo> getPropertyOwnerInfo();

    void setPropertyOwnerInfo(List<PropertyOwnerInfo> propertyOwnerSet);

    void addPropertyOwners(PropertyOwnerInfo ownerInfo);

    void removePropertyOwners(PropertyOwnerInfo ownerInfo);

    boolean isUnderWorkflow();

    void setUnderWorkflow(boolean underWorkflow);

    String getFullOwnerName();

    String getMobileNumber();

    String getAadharNumber();

    Map<String, String> getOwnerMap();

    Boolean getActive();

    void setActive(Boolean active);

    List<PropertyOwnerInfo> getPropertyOwnerInfoProxy();

    void setPropertyOwnerInfoProxy(List<PropertyOwnerInfo> propertyOwnerInfoProxy);

    User getPrimaryOwner();

    Character getSource();

    void setSource(Character source);

    Date getAssessmentdate();

    void setAssessmentdate(Date assessmentdate);

    List<VacancyRemission> getVacancyRemissions();

    void setVacancyRemissions(List<VacancyRemission> vacancyRemissionSet);

    Property getPropertyForBasicProperty();

    Double getLongitude();

    void setLongitude(Double longitude);

    Double getLatitude();

    void setLatitude(Double latitude);

    boolean isEligible();

    void setEligible(boolean eligible);

    List<Amalgamation> getAmalgamations();

    void setAmalgamations(List<Amalgamation> amalgamations);

    List<Amalgamation> getAmalgamationsProxy();

    void setAmalgamationsProxy(List<Amalgamation> amalgamationsProxy);

    void addAmalgamations(Amalgamation amalgamation);

    Character getIsIntgBillCreated();

    void setIsIntgBillCreated(Character isIntgBillCreated);
}
