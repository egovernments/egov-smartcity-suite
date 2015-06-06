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
package org.egov.ptis.domain.service.transfer;

import static org.egov.dcb.bean.Payment.AMOUNT;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_PRATIVRUTTA;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.commons.Installment;
import org.egov.dcb.bean.Payment;
import org.egov.demand.model.EgBill;
import org.egov.demand.utils.DemandConstants;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.enums.AddressType;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.integration.utils.CollectionHelper;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.entity.demand.FloorwiseDemandCalculations;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyMutationOwner;
import org.egov.ptis.domain.entity.property.PropertyOwner;

public class TransferOwnerService extends PersistenceService<PropertyMutation, Long> {
	private static final Logger LOGGER = Logger.getLogger(TransferOwnerService.class);
	private PersistenceService trnsfOwnerPerService;
	protected PersistenceService<BasicProperty, Long> basicPrpertyService;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

	/*
	 * This method returns property's basic property which is undergoing
	 * mutation
	 */
	public PropertyImpl createPropertyClone(BasicProperty basicProp, PropertyMutation propertyMutation,
			List<PropertyOwner> propertyOwnerProxy, boolean chkIsCorrIsDiff, String corrAddress1, String corrAddress2,
			String corrPinCode, String email, String mobileNo, String docNumber) {
		LOGGER.debug("Inside createPropertyClone method");
		Property oldProperty = basicProp.getProperty();
		Set<PropertyMutation> propertyMutationSet = getPropMutationSet(basicProp, propertyMutation, oldProperty);
		basicProp.setPropMutationSet(propertyMutationSet);
		// cloning property
		Property clonedProperty = oldProperty.createPropertyclone();
		clonedProperty.setPropertyOwnerSet(getNewPropOwnerAdd(clonedProperty, chkIsCorrIsDiff, corrAddress1,
				corrAddress2, corrPinCode, propertyOwnerProxy));
		clonedProperty.setPtDemandSet(cloneDemandSet(clonedProperty, oldProperty));
		basicProp.setAddress(getChangedOwnerContact(basicProp, email, mobileNo));
		clonedProperty.setStatus(STATUS_WORKFLOW);
		clonedProperty.setExtra_field1("");
		clonedProperty.setExtra_field2(NOTICE_PRATIVRUTTA);
		clonedProperty.setExtra_field3("");
		clonedProperty.setExtra_field4("");
		clonedProperty.setDocNumber(docNumber);
		basicProp.addProperty(clonedProperty);
		basicPrpertyService.update(basicProp);
		LOGGER.debug("Exit from createPropertyClone method");
		return (PropertyImpl) clonedProperty;
	}

	private Map<Installment, PTDemandCalculations> getDemandCalMap(Property oldProperty) {
		Map<Installment, PTDemandCalculations> dmdCalMap = new HashMap<Installment, PTDemandCalculations>();
		for (Ptdemand dmd : oldProperty.getPtDemandSet()) {
			dmdCalMap.put(dmd.getEgInstallmentMaster(), dmd.getDmdCalculations());
		}
		return dmdCalMap;

	}

	private Set<Ptdemand> cloneDemandSet(Property clonedProperty, Property oldProperty) {
		Map<Installment, PTDemandCalculations> dmdCalMap = getDemandCalMap(oldProperty);
		Set<Ptdemand> demandSet = new HashSet<Ptdemand>();
		PTDemandCalculations ptDmdCal;
		for (Ptdemand ptDmd : clonedProperty.getPtDemandSet()) {
			PTDemandCalculations OldPTDmdCal = dmdCalMap.get(ptDmd.getEgInstallmentMaster());
			ptDmdCal = new PTDemandCalculations(ptDmd, OldPTDmdCal.getPropertyTax(), OldPTDmdCal.getRateOfTax(), null,
					null, cloneFlrWiseDmdCal(OldPTDmdCal.getFlrwiseDmdCalculations()),
					 OldPTDmdCal.getTaxInfo(), OldPTDmdCal.getAlv());
			ptDmd.setDmdCalculations(ptDmdCal);
			demandSet.add(ptDmd);
		}
		return demandSet;
	}

	private Set<FloorwiseDemandCalculations> cloneFlrWiseDmdCal(Set<FloorwiseDemandCalculations> flrDmdCal) {
		FloorwiseDemandCalculations flrWiseDmdCal;
		Set<FloorwiseDemandCalculations> floorDmdCalSet = new HashSet<FloorwiseDemandCalculations>();
		for (FloorwiseDemandCalculations flrCal : flrDmdCal) {
			flrWiseDmdCal = new FloorwiseDemandCalculations(null, flrCal.getFloor(), flrCal.getPTDemandCalculations(),
					new Date(), new Date(), flrCal.getCategoryAmt(), flrCal.getOccupancyRebate(),
					flrCal.getConstructionRebate(), flrCal.getDepreciation(), flrCal.getUsageRebate());
			floorDmdCalSet.add(flrWiseDmdCal);
		}
		return floorDmdCalSet;
	}

	/*
	 * This method returns Property Mutation as a Set
	 */
	private Set<PropertyMutation> getPropMutationSet(BasicProperty basicProp, PropertyMutation propertyMutation,
			Property oldProperty) {
		PropertyMutationMaster propMutMstr = null;
		if (propertyMutation.getPropMutationMstr() != null
				&& propertyMutation.getPropMutationMstr().getIdMutation() != -1) {
			propMutMstr = (PropertyMutationMaster) trnsfOwnerPerService.find(
					"from PropertyMutationMaster PM where PM.idMutation = ?",
					Integer.valueOf(propertyMutation.getPropMutationMstr().getIdMutation()).intValue());
		}
		propertyMutation.setRefPid(null);
		propertyMutation.setBasicProperty(basicProp);
		propertyMutation.setApplicationNo(propertyTaxNumberGenerator.generateNameTransApplNo(basicProp.getBoundary()));
		propertyMutation.setMutationDate(propertyMutation.getMutationDate());
		propertyMutation.setMutationOwnerSet(getMutOwners(oldProperty, propertyMutation));
		propertyMutation.setPropMutationMstr(propMutMstr);
		Set<PropertyMutation> propertyMutationSet = new HashSet();
		propertyMutationSet.add(propertyMutation);
		return propertyMutationSet;
	}

	/*
	 * This method returns Mutation Owner details as a Set for Property
	 * undergoing Mutation
	 */
	private Set<PropertyMutationOwner> getMutOwners(Property prop, PropertyMutation propertyMutation) {
		Set<PropertyMutationOwner> mutOwnerSet = new HashSet<PropertyMutationOwner>();
		for (PropertyOwner ownerprop : prop.getPropertyOwnerSet()) {
			PropertyMutationOwner propMutOwner = new PropertyMutationOwner();
			propMutOwner.setOwnerId(ownerprop.getId().intValue());
			propMutOwner.setPropertyMutation(propertyMutation);
			mutOwnerSet.add(propMutOwner);
		}
		return mutOwnerSet;
	}

	/*
	 * This method returns changed owner corr address as a Set
	 */
	public Set<PropertyOwner> getNewPropOwnerAdd(Property clonedProperty, boolean chkIsCorrIsDiff, String corrAddress1,
			String corrAddress2, String corrPinCode, List<PropertyOwner> propertyOwnerProxy) {
		AddressType addrTypeMstr = (AddressType) trnsfOwnerPerService.find(
				"from AddressTypeMaster where addressTypeName = ?", "OWNER_ADDR_TYPE");
		Set<PropertyOwner> ownSet = new HashSet<PropertyOwner>();
		PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
		Address ownerAddr = null;
		if (chkIsCorrIsDiff) {
			ownerAddr = new PropertyAddress();
			if (corrAddress1 != null && !corrAddress1.isEmpty()) {
				corrAddress1 = propertyTaxUtil.antisamyHackReplace(corrAddress1);
			}
			if (corrAddress2 != null && !corrAddress2.isEmpty()) {
				corrAddress2 = propertyTaxUtil.antisamyHackReplace(corrAddress2);
			}
			ownerAddr.setType(addrTypeMstr);
			ownerAddr.setLandmark(corrAddress1);
			if (StringUtils.isNotEmpty(corrPinCode) || StringUtils.isNotBlank(corrPinCode)) {
				ownerAddr.setPinCode(corrPinCode);
			}
		} else {
			PropertyAddress propAddress = clonedProperty.getBasicProperty().getAddress();
			ownerAddr = propAddress;
		}
		int orderNo = 1;
		for (PropertyOwner owner : propertyOwnerProxy) {
			PropertyOwner newOwner = new PropertyOwner();
			String ownerName = owner.getName();
			ownerName = propertyTaxUtil.antisamyHackReplace(ownerName);
			newOwner.setName(ownerName);
			newOwner.setOrderNo(orderNo);
			newOwner.addAddress(ownerAddr);
			ownSet.add(newOwner);
			orderNo++;
		}
		return ownSet;
	}

	/*
	 * This method returns modified Owner Details for email and contact number
	 */
	private PropertyAddress getChangedOwnerContact(BasicProperty bp, String email, String mobileNo) {
		PropertyAddress propAddr = bp.getAddress();
		if (email != null && email != "") {
			propAddr.setEmailAddress(email);
		}
		if (mobileNo != null && mobileNo != "") {
			propAddr.setMobileNo(mobileNo);
		}
		return propAddr;
	}

	/**
	 * Generates Miscellaneous receipt
	 * 
	 * @param basicProperty
	 * @param amount
	 * @return BillReceiptInfo
	 */
	public BillReceiptInfo generateMiscReceipt(BasicProperty basicProperty, BigDecimal amount) {
		LOGGER.debug("Inside generateMiscReceipt method, Mutation Amount: " + amount);
		org.egov.ptis.client.integration.impl.PropertyImpl property = new org.egov.ptis.client.integration.impl.PropertyImpl();
		PropertyTaxBillable billable = new PropertyTaxBillable();
		billable.setBasicProperty(basicProperty);
		billable.setIsMiscellaneous(Boolean.TRUE);
		billable.setMutationFee(amount);
		billable.setCollectionType(DemandConstants.COLLECTIONTYPE_COUNTER);
		billable.setCallbackForApportion(Boolean.FALSE);
		billable.setUserId(Long.valueOf(EgovThreadLocals.getUserId()));
		billable.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty.getPropertyID()
				.getWard().getBoundaryNum().toString()));
		property.setBillable(billable);
		EgBill bill = property.createBill();
		CollectionHelper collHelper = new CollectionHelper(bill);
		Payment payment = preparePayment(amount);
		return collHelper.generateMiscReceipt(payment);
	}

	/**
	 * Prepares payment information
	 * @param amount
	 * @return
	 */
	private Payment preparePayment(BigDecimal amount) {
		LOGGER.debug("Inside preparePayment method, Mutation Amount: " + amount);
		Map<String, String> payDetailMap = new HashMap<String, String>();
		payDetailMap.put(AMOUNT, String.valueOf(amount));
		Payment payment = Payment.create(Payment.CASH, payDetailMap);
		LOGGER.debug("Exit from preparePayment method ");
		return payment;
	}

	public PersistenceService getTrnsfOwnerPerService() {
		return trnsfOwnerPerService;
	}

	public void setTrnsfOwnerPerService(PersistenceService trnsfOwnerPerService) {
		this.trnsfOwnerPerService = trnsfOwnerPerService;
	}

	public PersistenceService<BasicProperty, Long> getBasicPrpertyService() {
		return basicPrpertyService;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return propertyTaxNumberGenerator;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

}
