/*
 * PropertyImpl.java Created on Oct 21, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import static org.egov.ptis.constants.PropertyTaxConstants.BUILT_UP_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.InvalidPropertyException;
import org.egov.commons.Installment;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.StateAware;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.citizen.model.Owner;
import org.egov.ptis.domain.entity.demand.Ptdemand;

/**
 * The Implementation of the Property Interface
 * 
 * @author Neetu
 * @version 2.00
 * @see org.egov.ptis.domain.entity.property.Property
 */
public class PropertyImpl extends StateAware implements Property {
	private static final Logger LOGGER = Logger.getLogger(PropertyImpl.class);
	private Set<Owner> propertyOwnerSet = new HashSet<Owner>();

	private List<Owner> propertyOwnerProxy = new ArrayList<Owner>();

	private Set<Owner> propertyTenantSet = new HashSet<Owner>();

	private Owner owner;

	private PropertySource propertySource;

	private Boolean vacant;

	private BasicProperty basicProperty;

	private AbstractProperty abstractProperty;

	private Address address;

	private Character isDefaultProperty;

	private Character status = 'N';

	private Set<Ptdemand> ptDemandSet = new HashSet<Ptdemand>();

	private Character isChecked = 'N';

	private String remarks;

	private Date effectiveDate;

	private PropertyDetail propertyDetail;
	private PropertyModifyReason propertyModifyReason;
	private Set<PtDemandARV> ptDemandARVSet = new HashSet<PtDemandARV>();

	private Installment installment;
	private String extra_field1;

	private String extra_field2;

	/**
	 * Indicates if Notice 127 or 132 has been generated
	 */
	private String extra_field3;

	/**
	 * /** Indicates if Notice Prativrutta has been generated
	 */
	private String extra_field4;

	/**
	 * /** Indicates if Bill has been generated
	 */
	private String extra_field5;

	private String extra_field6;
	private BigDecimal manualAlv;
	private String occupierName;
	private Boolean isExemptedFromTax = false;
	private String taxExemptReason;
	private String docNumber;
	private Boundary areaBndry;
	
	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PropertyImpl)) {
			return false;
		}

		final PropertyImpl other = (PropertyImpl) obj;

		if (getId() != null && other.getId() != null) {
			if (getId().equals(other.getId())) {
				return true;
			} else {
				return false;
			}

		} else if ((getPropertySource() != null || other.getPropertySource() != null)
				&& (getBasicProperty() != null || other.getBasicProperty() != null)) {
			if ((getPropertySource().equals(other.getPropertySource()))
					&& (getBasicProperty().equals(other.getBasicProperty()))
					&& getInstallment().equals(other.getInstallment()) && (getStatus().equals(other.getStatus()))) {
				return true;
			} else {
				return false;
			}
		}
		return false;

	}

	public int hashCode() {
		int hashCode = 0;
		if (getId() != null) {
			hashCode = hashCode + getId().hashCode();
		}
		if ((getPropertySource() != null) && (getBasicProperty() != null)) {
			hashCode = hashCode + getPropertySource().hashCode() + getBasicProperty().hashCode()
					+ getInstallment().hashCode() + getStatus().hashCode();
		}

		return hashCode;
	}

	public List<ValidationError> validate() {
		return new ArrayList<ValidationError>();
	}

	public boolean validateProperty() throws InvalidPropertyException {

		if (getBasicProperty() == null) {
			throw new InvalidPropertyException("PropertyImpl.validate : BasicProperty is not Set, Please Check !!");
		}

		if (getCreatedBy() == null) {
			throw new InvalidPropertyException("PropertyImpl.validate : Created By is not Set, Please Check !!");
		}
		if (getPropertySource() == null)
			throw new InvalidPropertyException("PropertyImpl.validate : PropertySource is not set, Please Check !!");
		else if (getPropertySource().validate() == false)
			throw new InvalidPropertyException(
					"PropertyImpl.validate : PropertySource validate() failed, Please Check !!");

		return true;
	}

	public Set<Owner> getPropertyTenantSet() {
		return propertyTenantSet;
	}

	public void setPropertyTenantSet(Set<Owner> propertyTenantSet) {
		this.propertyTenantSet = propertyTenantSet;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Character getIsDefaultProperty() {
		return isDefaultProperty;
	}

	public void setIsDefaultProperty(Character isDefaultProperty) {
		this.isDefaultProperty = isDefaultProperty;
	}

	public Set<Owner> getPropertyOwnerSet() {
		return propertyOwnerSet;
	}

	public void setPropertyOwnerSet(Set<Owner> propertyOwnerSet) {
		this.propertyOwnerSet = propertyOwnerSet;
	}

	public AbstractProperty getAbstractProperty() {
		return abstractProperty;
	}

	public void setAbstractProperty(AbstractProperty abstractProperty) {
		this.abstractProperty = abstractProperty;
	}

	public org.egov.lib.address.model.Address getPropertyAddress() {
		return address;
	}

	public void setPropertyAddress(org.egov.lib.address.model.Address address) {
		this.address = address;
	}

	public Boolean isVacant() {
		return vacant;
	}

	public void setVacant(Boolean vacant) {
		this.vacant = vacant;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public void addPtDemand(Ptdemand ptDmd) {
		getPtDemandSet().add(ptDmd);
	}

	public void removePtDemand(Ptdemand ptDmd) {
		getPtDemandSet().remove(ptDmd);
	}

	public void addPropertyOwners(Owner owner) {
		getPropertyOwnerSet().add(owner);
	}

	public void removePropertyOwners(Owner owner) {
		getPropertyOwnerSet().remove(owner);
	}

	public void addPropertyTenants(Owner owner) {
		getPropertyTenantSet().add(owner);
	}

	public void removePropertyTenants(Owner owner) {
		getPropertyTenantSet().remove(owner);
	}

	public Installment getInstallment() {
		return installment;
	}

	public void setInstallment(Installment installment) {
		this.installment = installment;
	}

	public Boolean getVacant() {
		return vacant;
	}

	public PropertySource getPropertySource() {
		return propertySource;
	}

	public void setPropertySource(PropertySource propertySource) {
		this.propertySource = propertySource;
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public Set<Ptdemand> getPtDemandSet() {
		return ptDemandSet;
	}

	public void setPtDemandSet(Set<Ptdemand> ptDemandSet) {
		this.ptDemandSet = ptDemandSet;
	}

	public PropertyDetail getPropertyDetail() {
		return propertyDetail;
	}

	public void setPropertyDetail(PropertyDetail propertyDetail) {
		this.propertyDetail = propertyDetail;
	}

	public Character getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Character isChecked) {
		this.isChecked = isChecked;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public PropertyModifyReason getPropertyModifyReason() {
		return propertyModifyReason;
	}

	public void setPropertyModifyReason(PropertyModifyReason propertyModifyReason) {
		this.propertyModifyReason = propertyModifyReason;
	}

	public Set<PtDemandARV> getPtDemandARVSet() {
		return ptDemandARVSet;
	}

	public void setPtDemandARVSet(Set<PtDemandARV> ptDemandARVSet) {
		this.ptDemandARVSet = ptDemandARVSet;
	}

	public void addPtDemandARV(PtDemandARV ptDemandARV) {
		LOGGER.debug("PropertyImpl.addPtDemandARVSet");
		getPtDemandARVSet().add(ptDemandARV);
	}

	public String getExtra_field1() {
		return extra_field1;
	}

	public void setExtra_field1(String extra_field1) {
		this.extra_field1 = extra_field1;
	}

	public String getExtra_field2() {
		return extra_field2;
	}

	public void setExtra_field2(String extra_field2) {
		this.extra_field2 = extra_field2;
	}

	public String getExtra_field3() {
		return extra_field3;
	}

	public void setExtra_field3(String extra_field3) {
		this.extra_field3 = extra_field3;
	}

	public String getExtra_field4() {
		return extra_field4;
	}

	public void setExtra_field4(String extra_field4) {
		this.extra_field4 = extra_field4;
	}

	public String getExtra_field5() {
		return extra_field5;
	}

	public void setExtra_field5(String extra_field5) {
		this.extra_field5 = extra_field5;
	}

	public String getExtra_field6() {
		return extra_field6;
	}

	public void setExtra_field6(String extra_field6) {
		this.extra_field6 = extra_field6;
	}

	public Boolean getIsExemptedFromTax() {
		return isExemptedFromTax;
	}

	public void setIsExemptedFromTax(Boolean isExemptedFromTax) {
		this.isExemptedFromTax = isExemptedFromTax;
	}

	public String getTaxExemptReason() {
		return taxExemptReason;
	}

	public void setTaxExemptReason(String taxExemptReason) {
		this.taxExemptReason = taxExemptReason;
	}
	
	public List<Owner> getPropertyOwnerProxy() {
		// getPropertyOwnerSet().addAll(propertyOwnerProxy);
		//propertyOwnerProxy.addAll(getPropertyOwnerSet());
		return propertyOwnerProxy;
	}

	public void setPropertyOwnerProxy(List<Owner> propertyOwnerProxy) {
		this.propertyOwnerProxy = propertyOwnerProxy;
		getPropertyOwnerSet().addAll(propertyOwnerProxy);
	}

	/*
	 * This method creates a clone of Property
	 */
	public Property createPropertyclone() {
		LOGGER.debug("Inside PropertyImpl clone method  " + toString());
		// toString()
		Property newProp = new PropertyImpl();
		newProp.setBasicProperty(getBasicProperty());
		newProp.setId(null);
		newProp.setEffectiveDate(getEffectiveDate());
		newProp.setExtra_field1(getExtra_field1());
		newProp.setExtra_field2(getExtra_field2());
		newProp.setExtra_field3(getExtra_field3());
		newProp.setExtra_field4(getExtra_field4());
		newProp.setExtra_field5(getExtra_field5());
		newProp.setExtra_field6(getExtra_field6());
		newProp.setInstallment(getInstallment());
		newProp.setIsChecked(getIsChecked());
		newProp.setIsDefaultProperty(getIsDefaultProperty());
		newProp.setStatus(getStatus());
		newProp.setPropertyDetail(clonePropertyDetail(newProp));
		newProp.setPropertyModifyReason(getPropertyModifyReason());
		newProp.setPropertyOwnerSet(cloneOwners());
		newProp.setPropertySource(getPropertySource());
		newProp.setPropertyTenantSet(cloneTenants());
		newProp.setPtDemandSet(cloneDemand());
		newProp.setRemarks(getRemarks());
		newProp.setVacant(getVacant());
		newProp.setIsExemptedFromTax(getIsExemptedFromTax());
		newProp.setTaxExemptReason(getTaxExemptReason());
		newProp.setDocNumber(getDocNumber());
		return newProp;
	}

	@Override
	public String toString() {
		StringBuilder sbf = new StringBuilder();
		sbf.append("Id: ").append(getId()).append("|BasicProperty: ");
		sbf = (getBasicProperty() != null) ? sbf.append(getBasicProperty().getUpicNo()) : sbf.append("");
		sbf.append("|IsDefaultProperty:").append(getIsDefaultProperty()).append("|Status:").append(getStatus()).append(
				"|PropertySource: ").append(null != getPropertySource() ? getPropertySource().getName() : null).append(
				"|Installment: ").append(getInstallment()).append("|extraField1: ").append(getExtra_field1()).append(
				"|extraField2: ").append(getExtra_field2()).append("|extraField3: ").append(getExtra_field3()).append(
				"|extraField4: ").append(getExtra_field4()).append("|extraField5: ").append(getExtra_field5()).append(
				"|extraField6: ").append(getExtra_field6());

		return sbf.toString();
	}

	/*
	 * This method returns Owner details as a Set
	 */
	private Set<Owner> cloneOwners() {
		Set<Owner> newOwnerSet = new HashSet<Owner>();
		Owner newOwner;
		for (Owner owner : getPropertyOwnerSet()) {
			newOwner = new Owner(null, owner.getSsn(), owner.getPanNumber(), owner.getPassportNumber(), owner
					.getDrivingLicenceNumber(), owner.getRationCardNumber(), owner.getVoterRegistrationNumber(), owner
					.getFirstName(), owner.getMiddleName(), owner.getLastName(), owner.getBirthDate(), owner
					.getHomePhone(), owner.getOfficePhone(), owner.getMobilePhone(), owner.getFax(), owner
					.getEmailAddress(), owner.getOccupation(), owner.getJobStatus(), owner.getLocale(), owner
					.getFirstNameLocal(), owner.getMiddleNameLocal(), owner.getLastNameLocal(), owner.getOwnerTitle(),
					owner.getOwnertitleLocal());
			newOwner.setAddressSet(cloneAddrSet(owner.getAddressSet()));
			newOwnerSet.add(newOwner);

		}
		return newOwnerSet;
	}

	/*
	 * This method returns Demand details as a Set
	 */
	private Set<Ptdemand> cloneDemand() {
		Set<Ptdemand> newdemandSet = new HashSet<Ptdemand>();
		for (Ptdemand demand : getPtDemandSet()) {
			Ptdemand ptDemand = (Ptdemand) demand.clone();
			if (demand.getDmdCalculations() != null) {
				ptDemand.setDmdCalculations(demand.getDmdCalculations());
			}
			newdemandSet.add((Ptdemand) demand.clone());
		}
		return newdemandSet;
	}

	/*
	 * This method returns Tenant details as a Set
	 */
	private Set<Owner> cloneTenants() {
		Set<Owner> newTenantSet = new HashSet<Owner>();
		Owner newTenant;
		for (Owner tenant : getPropertyTenantSet()) {
			newTenant = new Owner(null, tenant.getSsn(), tenant.getPanNumber(), tenant.getPassportNumber(), tenant
					.getDrivingLicenceNumber(), tenant.getRationCardNumber(), tenant.getVoterRegistrationNumber(),
					tenant.getFirstName(), tenant.getMiddleName(), tenant.getLastName(), tenant.getBirthDate(), tenant
							.getHomePhone(), tenant.getOfficePhone(), tenant.getMobilePhone(), tenant.getFax(), tenant
							.getEmailAddress(), tenant.getOccupation(), tenant.getJobStatus(), tenant.getLocale(),
					tenant.getFirstNameLocal(), tenant.getMiddleNameLocal(), tenant.getLastNameLocal(), tenant
							.getOwnerTitle(), tenant.getOwnertitleLocal());
			newTenant.setAddressSet(cloneAddrSet(tenant.getAddressSet()));
			newTenantSet.add(newTenant);

		}
		return newTenantSet;
	}

	/*
	 * This method returns Owner Address details as a Set
	 */
	private Set<Address> cloneAddrSet(Set<Address> addressSet) {
		Set<Address> newAddressSet = new HashSet<Address>();
		for (Address ownAddress : addressSet) {
			Address ownerAddr = new Address();
			ownerAddr.setAddTypeMaster(ownAddress.getAddTypeMaster());
			ownerAddr.setStreetAddress1(ownAddress.getStreetAddress1());
			ownerAddr.setStreetAddress2(ownAddress.getStreetAddress2());
			ownerAddr.setPinCode(ownAddress.getPinCode());
			newAddressSet.add(ownerAddr);
		}
		return newAddressSet;
	}

	/*
	 * This method returns Property details of a property
	 */
	private PropertyDetail clonePropertyDetail(Property newProperty) {
		PropertyDetail propDetails = null;
		if (getPropertyDetail().getPropertyType().toString().equals(BUILT_UP_PROPERTY)) {
			BuiltUpProperty bup = (BuiltUpProperty) getPropertyDetail();
			propDetails = new BuiltUpProperty(getPropertyDetail().getSitalArea(), getPropertyDetail()
					.getTotalBuiltupArea(), getPropertyDetail().getCommBuiltUpArea(), getPropertyDetail()
					.getPlinthArea(), getPropertyDetail().getCommVacantLand(), getPropertyDetail().getNonResPlotArea(),
					bup.isIrregular(), getPropertyDetail().getSurveyNumber(), getPropertyDetail().getFieldVerified(),
					getPropertyDetail().getFieldVerificationDate(), cloneFlrDtls(), null, getPropertyDetail()
							.getWater_Meter_Num(), getPropertyDetail().getElec_Meter_Num(), getPropertyDetail()
							.getNo_of_floors(), getPropertyDetail().getFieldIrregular(), getPropertyDetail()
							.getDateOfCompletion(), getPropertyDetail().getEffective_date(), newProperty,
					getPropertyDetail().getUpdatedTime(), getPropertyDetail().getPropertyUsage(), getPropertyDetail()
							.getDateOfCompletion(), bup.getCreationReason(), getPropertyDetail()
							.getPropertyTypeMaster(), getPropertyDetail().getPropertyType(), getPropertyDetail()
							.getInstallment(), getPropertyDetail().getPropertyMutationMaster(), getPropertyDetail()
							.getComZone(), getPropertyDetail().getCornerPlot(), getPropertyDetail()
							.getPropertyOccupation());
		} else if (getPropertyDetail().getPropertyType().toString().equals(VACANT_PROPERTY)) {
			VacantProperty vcp = (VacantProperty) getPropertyDetail();
			propDetails = new VacantProperty(getPropertyDetail().getSitalArea(), getPropertyDetail()
					.getTotalBuiltupArea(), getPropertyDetail().getCommBuiltUpArea(), getPropertyDetail()
					.getPlinthArea(), getPropertyDetail().getCommVacantLand(), getPropertyDetail().getNonResPlotArea(),
					vcp.getIrregular(), getPropertyDetail().getSurveyNumber(), getPropertyDetail().getFieldVerified(),
					getPropertyDetail().getFieldVerificationDate(), cloneFlrDtls(), null, getPropertyDetail()
							.getWater_Meter_Num(), getPropertyDetail().getElec_Meter_Num(), getPropertyDetail()
							.getNo_of_floors(), getPropertyDetail().getFieldIrregular(), getPropertyDetail()
							.getCompletion_year(), getPropertyDetail().getEffective_date(), getPropertyDetail()
							.getDateOfCompletion(), newProperty, getPropertyDetail().getUpdatedTime(),
					getPropertyDetail().getPropertyUsage(), vcp.getCreationReason(), getPropertyDetail()
							.getPropertyTypeMaster(), getPropertyDetail().getPropertyType(), getPropertyDetail()
							.getInstallment(), getPropertyDetail().getPropertyOccupation(), getPropertyDetail()
							.getPropertyMutationMaster(), getPropertyDetail().getComZone(), getPropertyDetail()
							.getCornerPlot());
		}
		propDetails.setExtra_field1(getPropertyDetail().getExtra_field1());
		propDetails.setExtra_field2(getPropertyDetail().getExtra_field2());
		propDetails.setExtra_field3(getPropertyDetail().getExtra_field3());
		propDetails.setExtra_field4(getPropertyDetail().getExtra_field4());
		propDetails.setExtra_field5(getPropertyDetail().getExtra_field5());
		propDetails.setExtra_field6(getPropertyDetail().getExtra_field6());
		return propDetails;
	}

	/*
	 * This method returns Floor details as a Set
	 */
	private Set<FloorIF> cloneFlrDtls() {
		FloorIF floor = null;
		Set<FloorIF> flrDtlsSet = new HashSet<FloorIF>();
		for (FloorIF flr : getPropertyDetail().getFloorDetails()) {
			floor = new FloorImpl(flr.getConstructionTypeSet(), flr.getStructureClassification(),
					flr.getPropertyUsage(), flr.getPropertyOccupation(), flr.getFloorNo(), flr.getDepreciationMaster(),
					flr.getBuiltUpArea(), flr.getFloorArea(), flr.getWaterMeter(), flr.getElectricMeter(), null, null,
					flr.getRentPerMonth(), flr.getExtraField1(), flr.getExtraField2(), flr.getExtraField3(),
					flr.getExtraField4(), flr.getExtraField5(), flr.getExtraField6(), flr.getExtraField7(),
					flr.getManualAlv(), flr.getUnitType(), flr.getUnitTypeCategory(), flr.getWaterRate());
			flrDtlsSet.add(floor);
		}
		return flrDtlsSet;
	}

	@Override
	public String getStateDetails() {
		String upicNo = (basicProperty.getUpicNo() != null && !basicProperty.getUpicNo().isEmpty()) ? basicProperty
				.getUpicNo() : "";
		return upicNo + " - " + createdBy.getUserName();
	}

	@Override
	public BigDecimal getManualAlv() {
		return manualAlv;
	}

	@Override
	public void setManualAlv(BigDecimal manualAlv) {
		this.manualAlv = manualAlv;
	}

	@Override
	public String getOccupierName() {
		return occupierName;
	}

	@Override
	public void setOccupierName(String occupierName) {
		this.occupierName = occupierName;
	}

	@Override
	public Boundary getAreaBndry() {
		return areaBndry;
	}

	@Override
	public void setAreaBndry(Boundary areaBndry) {
		this.areaBndry = areaBndry;
	}


}
