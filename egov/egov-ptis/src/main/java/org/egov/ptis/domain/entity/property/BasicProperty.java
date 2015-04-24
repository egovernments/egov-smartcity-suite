/*
 * BasicProperty.java Created on Oct 21, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.ptis.domain.entity.objection.Objection;
import org.egov.ptis.domain.entity.recovery.Recovery;
import org.egov.ptis.notice.PtNotice;

/**
 * This is the interface for the BasicProperty which represents the Basic,
 * Minimum details for the Property. Every Basicproprty should have at least one
 * Property associated with it
 *
 * @author Neetu
 * @version 2.00
 * @see BasicPropertyImpl.java
 */
public interface BasicProperty {
	public Long getId();

	public void setId(Long id);

	public User getCreatedBy();

	public void setCreatedBy(User createdBy);

	public Date getCreatedDate();

	public void setCreatedDate(Date createdDate);

	public User getModifiedBy();

	public void setModifiedBy(User modifiedBy);

	public Date getModifiedDate();

	public void setModifiedDate(Date modifiedDate);

	public Boolean isActive();

	public PropertyID getPropertyID();

	public PropertyAddress getAddress();

	public Property getProperty();

	public void setActive(Boolean active);

	public void setPropertyID(PropertyID propertyID);

	public void setAddress(PropertyAddress address);

	public String getUpicNo();

	public void setUpicNo(String UpicNo);

	public String getOldMuncipalNum();

	public void setOldMuncipalNum(String oldMuncipalNum);

	public Boundary getBoundary();

	public void setBoundary(Boundary boundary);

	public void setPropertyStatusValuesSet(Set<PropertyStatusValues> propertyStatusValuesSet);

	public Set<PropertyStatusValues> getPropertyStatusValuesSet();

	public void addPropertyStatusValues(PropertyStatusValues propertyStatusValues);

	public void removePropertyStatusValues(PropertyStatusValues propertyStatusValues);

	public PropertyReference getPropertyReference();

	public void setPropertyReference(PropertyReference propertyReference);

	public Set<PropertyMutation> getPropMutationSet();

	public void setPropMutationSet(Set<PropertyMutation> propMutationSet);

	public PropertyMutationMaster getPropertyMutationMaster();

	public void setPropertyMutationMaster(PropertyMutationMaster propertyMutationMaster);

	public Date getPropCreateDate();

	public void setPropCreateDate(Date propCreateDate);

	public Set<Property> getPropertySet();

	public void setPropertySet(Set<Property> propertySet);

	public void addProperty(Property property);

	public void removeProperty(Property property);

	public PropertyStatus getStatus();

	public void setStatus(PropertyStatus propertyStatus);

	public String getExtraField1();

	public void setExtraField1(String extraField1);

	public String getExtraField2();

	public void setExtraField2(String extraField2);

	public String getExtraField3();

	public void setExtraField3(String extraField3);

	public boolean validateBasicProp();

	public String getGisReferenceNo();

	public void setGisReferenceNo(String gisReferenceNo);

	public Set<PtNotice> getNotices();

	public void setNotices(Set<PtNotice> noticeForms);

	public void addNotice(PtNotice ptNotice);

	public void removeNotice(PtNotice ptNotice);

	public Set<Objection> getObjections();

	public void setObjections(Set<Objection> objections);

	public void addObjection(Objection objection);

	public void removeObjection(Objection objection);

	public Map<String, String> getPropertyWfStatus();

	public Set<Recovery> getRecoveries() ;

	public void setRecoveries(Set<Recovery> recoveries) ;

	public void addRecoveries(Recovery recovery);

	public void removeRecoveries(Recovery recovery) ;

	public Set<PropertyDocs> getPropertyDocsSet();

	public void setPropertyDocsSet(Set<PropertyDocs> propertyDocsSet);

	public void addDocs(PropertyDocs propertyDocs);

	public void removeDocs(PropertyDocs propertyDocs);

	public String getPartNo();

	public void setPartNo(String partNo);

	public Character getIsMigrated();

	public void setIsMigrated(Character isMigrated);

	public Boolean getAllChangesCompleted();

	public void setAllChangesCompleted(Boolean allChangesCompleted);

	public Property getWFProperty();

	public Character getIsBillCreated();

	public void setIsBillCreated(Character isBillCreated);

	public String getBillCrtError();

	public void setBillCrtError(String billCrtError);

	public Character getIsTaxXMLMigrated();

	public void setIsTaxXMLMigrated(Character isTaxXMLMigrated);

	public boolean getIsDemandActive();

	public void setIsDemandActive(boolean isDemandActive);

	public PropertyImpl getActiveProperty();

	public PropertyImpl getInactiveProperty();

	public void setActiveProperty(PropertyImpl activeProperty);

	public void setInactiveProperty(PropertyImpl inactiveProperty);

}
