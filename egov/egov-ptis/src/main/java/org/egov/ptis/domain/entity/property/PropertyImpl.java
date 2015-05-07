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

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.exceptions.InvalidPropertyException;
import org.egov.infra.admin.master.entity.Address;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.joda.time.DateTime;

/**
 * The Implementation of the Property Interface
 *
 * @author Neetu
 * @version 2.00
 * @see org.egov.ptis.domain.entity.property.Property
 */
public class PropertyImpl extends StateAware implements Property {
	private static final Logger LOGGER = Logger.getLogger(PropertyImpl.class);
	private Set<PropertyOwner> propertyOwnerSet = new HashSet<PropertyOwner>();

	private List<PropertyOwner> propertyOwnerProxy = new ArrayList<PropertyOwner>();

	private Set<Citizen> propertyTenantSet = new HashSet<Citizen>();

	private Citizen citizen;

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
	private BigDecimal alv;

	/**
	 * @Size(min=1) is not working when we modify a migrated property, Reason is because
	 * for the migrated property the tax xml is not there so when we try to modify the
	 * migrated property the active property will not be having the unitCalculationDetails
	 *
	 */
	@Valid
	private Set<UnitCalculationDetail> unitCalculationDetails = new HashSet<UnitCalculationDetail>();

	@Override
	public String getDocNumber() {
		return docNumber;
	}

	@Override
	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	@Override
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

	@Override
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

/*	@Override
	public List<ValidationError> validate() {
		return new ArrayList<ValidationError>();
	}
*/
	@Override
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

	@Override
	public Set<Citizen> getPropertyTenantSet() {
		return propertyTenantSet;
	}

	@Override
	public void setPropertyTenantSet(Set<Citizen> propertyTenantSet) {
		this.propertyTenantSet = propertyTenantSet;
	}

	@Override
	public Character getStatus() {
		return status;
	}

	@Override
	public void setStatus(Character status) {
		this.status = status;
	}

	@Override
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	@Override
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Override
	public Character getIsDefaultProperty() {
		return isDefaultProperty;
	}

	@Override
	public void setIsDefaultProperty(Character isDefaultProperty) {
		this.isDefaultProperty = isDefaultProperty;
	}

	@Override
	public Set<PropertyOwner> getPropertyOwnerSet() {
		return propertyOwnerSet;
	}

	@Override
	public void setPropertyOwnerSet(Set<PropertyOwner> propertyOwnerSet) {
		this.propertyOwnerSet = propertyOwnerSet;
	}

	public AbstractProperty getAbstractProperty() {
		return abstractProperty;
	}

	public void setAbstractProperty(AbstractProperty abstractProperty) {
		this.abstractProperty = abstractProperty;
	}

	@Override
	public Address getPropertyAddress() {
		return address;
	}

	@Override
	public void setPropertyAddress(Address address) {
		this.address = address;
	}

	@Override
	public Boolean isVacant() {
		return vacant;
	}

	@Override
	public void setVacant(Boolean vacant) {
		this.vacant = vacant;
	}


	public Citizen getCitizen() {
		return citizen;
	}

	public void setCitizen(Citizen citizen) {
		this.citizen = citizen;
	}

	@Override
	public void addPtDemand(Ptdemand ptDmd) {
		getPtDemandSet().add(ptDmd);
	}

	@Override
	public void removePtDemand(Ptdemand ptDmd) {
		getPtDemandSet().remove(ptDmd);
	}

	@Override
	public void addPropertyOwners(PropertyOwner owner) {
		getPropertyOwnerSet().add(owner);
	}

	@Override
	public void removePropertyOwners(PropertyOwner owner) {
		getPropertyOwnerSet().remove(owner);
	}

	@Override
	public void addPropertyTenants(Citizen citizen) {
		getPropertyTenantSet().add(citizen);
	}

	@Override
	public void removePropertyTenants(Citizen citizen) {
		getPropertyTenantSet().remove(citizen);
	}

	@Override
	public Installment getInstallment() {
		return installment;
	}

	@Override
	public void setInstallment(Installment installment) {
		this.installment = installment;
	}

	public Boolean getVacant() {
		return vacant;
	}

	@Override
	public PropertySource getPropertySource() {
		return propertySource;
	}

	@Override
	public void setPropertySource(PropertySource propertySource) {
		this.propertySource = propertySource;
	}

	@Override
	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	@Override
	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	@Override
	public Set<Ptdemand> getPtDemandSet() {
		return ptDemandSet;
	}

	@Override
	public void setPtDemandSet(Set<Ptdemand> ptDemandSet) {
		this.ptDemandSet = ptDemandSet;
	}

	@Override
	public PropertyDetail getPropertyDetail() {
		return propertyDetail;
	}

	@Override
	public void setPropertyDetail(PropertyDetail propertyDetail) {
		this.propertyDetail = propertyDetail;
	}

	@Override
	public Character getIsChecked() {
		return isChecked;
	}

	@Override
	public void setIsChecked(Character isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public PropertyModifyReason getPropertyModifyReason() {
		return propertyModifyReason;
	}

	@Override
	public void setPropertyModifyReason(PropertyModifyReason propertyModifyReason) {
		this.propertyModifyReason = propertyModifyReason;
	}

	@Override
	public Set<PtDemandARV> getPtDemandARVSet() {
		return ptDemandARVSet;
	}

	@Override
	public void setPtDemandARVSet(Set<PtDemandARV> ptDemandARVSet) {
		this.ptDemandARVSet = ptDemandARVSet;
	}

	@Override
	public void addPtDemandARV(PtDemandARV ptDemandARV) {
		LOGGER.debug("PropertyImpl.addPtDemandARVSet");
		getPtDemandARVSet().add(ptDemandARV);
	}

	@Override
	public String getExtra_field1() {
		return extra_field1;
	}

	@Override
	public void setExtra_field1(String extra_field1) {
		this.extra_field1 = extra_field1;
	}

	@Override
	public String getExtra_field2() {
		return extra_field2;
	}

	@Override
	public void setExtra_field2(String extra_field2) {
		this.extra_field2 = extra_field2;
	}

	@Override
	public String getExtra_field3() {
		return extra_field3;
	}

	@Override
	public void setExtra_field3(String extra_field3) {
		this.extra_field3 = extra_field3;
	}

	@Override
	public String getExtra_field4() {
		return extra_field4;
	}

	@Override
	public void setExtra_field4(String extra_field4) {
		this.extra_field4 = extra_field4;
	}

	@Override
	public String getExtra_field5() {
		return extra_field5;
	}

	@Override
	public void setExtra_field5(String extra_field5) {
		this.extra_field5 = extra_field5;
	}

	@Override
	public String getExtra_field6() {
		return extra_field6;
	}

	@Override
	public void setExtra_field6(String extra_field6) {
		this.extra_field6 = extra_field6;
	}

	@Override
	public Boolean getIsExemptedFromTax() {
		return isExemptedFromTax;
	}

	@Override
	public void setIsExemptedFromTax(Boolean isExemptedFromTax) {
		this.isExemptedFromTax = isExemptedFromTax;
	}

	@Override
	public String getTaxExemptReason() {
		return taxExemptReason;
	}

	@Override
	public void setTaxExemptReason(String taxExemptReason) {
		this.taxExemptReason = taxExemptReason;
	}

	@Override
	public List<PropertyOwner> getPropertyOwnerProxy() {
		// getPropertyOwnerSet().addAll(propertyOwnerProxy);
		//propertyOwnerProxy.addAll(getPropertyOwnerSet());
		return propertyOwnerProxy;
	}

	@Override
	public void setPropertyOwnerProxy(List<PropertyOwner> propertyOwnerProxy) {
		this.propertyOwnerProxy = propertyOwnerProxy;
		getPropertyOwnerSet().addAll(propertyOwnerProxy);
	}

	/*
	 * This method creates a clone of Property
	 */
	@Override
	public Property createPropertyclone() {
		LOGGER.debug("Inside PropertyImpl clone method  " + toString());
		PropertyImpl newProp = new PropertyImpl();
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
		//newProp.setPropertyTenantSet(cloneTenants());
		newProp.setPtDemandSet(cloneDemand());
		newProp.setRemarks(getRemarks());
		newProp.setVacant(getVacant());
		newProp.setIsExemptedFromTax(getIsExemptedFromTax());
		newProp.setTaxExemptReason(getTaxExemptReason());
		newProp.setDocNumber(getDocNumber());
		newProp.setState(getState());
		newProp.setCreatedDate(new DateTime());
		newProp.setLastModifiedDate(new DateTime());
		newProp.addAllUnitCalculationDetails(cloneUnitCalculationDetails());
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
	private Set<PropertyOwner> cloneOwners() {
		Set<PropertyOwner> newOwnerSet = new HashSet<PropertyOwner>();
		PropertyOwner newOwner;
		for (PropertyOwner owner : getPropertyOwnerSet()) {
			newOwner = new PropertyOwner(owner.getAadhaarNumber(),owner.getActivationCode(),owner.isActive(),owner.getAddress(),
					owner.getAltContactNumber(),
					owner.getDob(),owner.getEmailId(),owner.getGender(),owner.getLocale(),owner.getMobileNumber(),owner.getName(),owner.getPan(),
					owner.getPassword(),owner.getSalutation(),owner.getSource(),owner.getUsername(),
					owner.getOrderNo(),owner.getProperty());
			newOwner.setAddress(cloneAddrList(newOwner.getAddress()));
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
			newdemandSet.add((Ptdemand) demand.clone());
		}
		return newdemandSet;
	}

	/*
	 * This method returns Tenant details as a Set
	 */
	//FIX ME
	/*private Set<Owner> cloneTenants() {
		Set<Owner> newTenantSet = new HashSet<Owner>();
		Owner newTenant;
		for (Owner tenant : getPropertyTenantSet()) {
			//TODO -- Fix me (Commented to Resolve compilation issues)
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
	}*/

	/*
	 * This method returns Owner Address details as a Set
	 */
	private List<Address> cloneAddrList(List<Address> addressList) {
		List<Address> newAddressList = new ArrayList<Address>();
		for (Address ownAddress : addressList) {
			Address ownerAddr = new Address();
			ownerAddr.setType(ownAddress.getType());
			ownerAddr.setLandmark(ownAddress.getLandmark());
			ownerAddr.setPinCode(ownAddress.getPinCode());
			ownerAddr.setHouseNoBldgApt(ownAddress.getHouseNoBldgApt());
			newAddressList.add(ownerAddr);
		}
		return newAddressList;
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
					flr.getManualAlv(), flr.getUnitType(), flr.getUnitTypeCategory(), flr.getWaterRate(), flr.getAlv(),
					flr.getTaxExemptedReason(), flr.getRentAgreementDetail());
			flrDtlsSet.add(floor);
		}
		return flrDtlsSet;
	}

	/**
	 * Returns the clone of UnitCalculationDetail
	 *
	 * @return set of UnitCalculaitonDetail
	 */
	private Set<UnitCalculationDetail> cloneUnitCalculationDetails() {
		Set<UnitCalculationDetail> unitCalculationDetailClones = new HashSet<UnitCalculationDetail>();

		for (UnitCalculationDetail unitCalcDetail : this.getUnitCalculationDetails()) {
			unitCalculationDetailClones.add(new UnitCalculationDetail(unitCalcDetail));
		}

		return unitCalculationDetailClones;
	}

	@Override
	public String getStateDetails() {
		String upicNo = (basicProperty.getUpicNo() != null && !basicProperty.getUpicNo().isEmpty()) ? basicProperty
				.getUpicNo() : "";
		return upicNo + " - " + getCreatedBy().getName();
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

	@Override
	public BigDecimal getAlv() {
		return this.alv;
	}

	@Override
	public void setAlv(BigDecimal alv) {
		this.alv = alv;
	}

	@Override
	public Set<UnitCalculationDetail> getUnitCalculationDetails() {
		return unitCalculationDetails;
	}

	@Override
	public void setUnitCalculationDetails(Set<UnitCalculationDetail> unitCalculationDetails) {
		this.unitCalculationDetails = unitCalculationDetails;
	}

	@Override
	public void addUnitCalculationDetails(UnitCalculationDetail unitCalculationDetail) {
		unitCalculationDetail.setProperty(this);
	    getUnitCalculationDetails().add(unitCalculationDetail);
	}

	@Override
	public void addAllUnitCalculationDetails(Set<UnitCalculationDetail> unitCalculationDetailsSet) {
		for (UnitCalculationDetail unitCalcDetail : unitCalculationDetailsSet) {
			this.addUnitCalculationDetails(unitCalcDetail);
		}
	}

}
