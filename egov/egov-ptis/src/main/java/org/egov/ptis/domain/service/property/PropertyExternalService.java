package org.egov.ptis.domain.service.property;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.commons.Installment;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.ptis.client.model.PenaltyAndRebate;
import org.egov.ptis.client.model.TaxDetails;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.model.ArrearDetails;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.BoundaryDetails;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.PropertyDetails;
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.springframework.beans.factory.annotation.Autowired;

public class PropertyExternalService {
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	@Autowired
	private PtDemandDao ptDemandDAO;
	@Autowired
    private ApplicationContextBeanProvider beanProvider;
	private Long userId;
	
	public AssessmentDetails getPropertyDetails(String propertyId) {

		AssessmentDetails assessmentDetail = new AssessmentDetails();
		PropertyDetails propertyDetails = new PropertyDetails();
		ErrorDetails errorDetails = new ErrorDetails();
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
		if (null != basicProperty) {
			// Error Code
			if (!basicProperty.isActive()) {
				errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_CODE);
				errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_MSG);
			} else {
				Set<PropertyStatusValues> statusValues = basicProperty.getPropertyStatusValuesSet();
				if (null != statusValues && !statusValues.isEmpty()) {
					for (PropertyStatusValues statusValue : statusValues) {
						if (statusValue.getPropertyStatus().getStatusCode() == PropertyTaxConstants.MARK_DEACTIVE) {
							errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_CODE);
							errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_MSG);
						}
					}
				}
			}

			// Owner Details
			Property property = basicProperty.getProperty();
			assessmentDetail.setBoundaryDetails(prepareBoundaryInfo(basicProperty));
			assessmentDetail.setPropertyAddress(getPropertyAddress(property));
			if (null != property) {
				assessmentDetail.setOwnerNames(prepareOwnerInfo(property));
				// Property Details
				PropertyDetail propertyDetail = property.getPropertyDetail();
				if (null != propertyDetail) {
					propertyDetails.setPropertyType(propertyDetail.getPropertyType());
					if (propertyDetail.getPropertyUsage() != null) {
						propertyDetails.setPropertyUsage(propertyDetail.getPropertyUsage().getUsageName());
					}
				}

				Map<String, BigDecimal> resultmap = ptDemandDAO.getDemandCollMap(property);
				if (null != resultmap && !resultmap.isEmpty()) {
					BigDecimal currDmd = resultmap.get(PropertyTaxConstants.CURR_DMD_STR);
					BigDecimal arrDmd = resultmap.get(PropertyTaxConstants.ARR_DMD_STR);
					BigDecimal currCollection = resultmap.get(PropertyTaxConstants.CURR_COLL_STR);
					BigDecimal arrColelection = resultmap.get(PropertyTaxConstants.ARR_COLL_STR);

					// Calculating tax dues
					BigDecimal taxDue = currDmd.add(arrDmd).subtract(currCollection).subtract(arrColelection);
					propertyDetails.setTaxDue(taxDue);
				}
			}

		} else {
			errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_CODE);
			errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_PREFIX + propertyId
					+ PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX);
		}

		// Assessment Details
		assessmentDetail.setPropertyID(propertyId);
		assessmentDetail.setPropertyDetails(propertyDetails);
		assessmentDetail.setErrorDetails(errorDetails);

		return assessmentDetail;
	}

	private BoundaryDetails prepareBoundaryInfo(BasicProperty basicProperty) {
		BoundaryDetails boundaryDetails = new BoundaryDetails();
		PropertyID propertyID = basicProperty.getPropertyID();
		if (null != propertyID) {
			if (null != propertyID.getZone()) {
				boundaryDetails.setZoneNumber(propertyID.getZone().getBoundaryNum());
				boundaryDetails.setZoneName(propertyID.getZone().getName());
			}
			if (null != propertyID.getWard()) {
				boundaryDetails.setWardNumber(propertyID.getWard().getBoundaryNum());
				boundaryDetails.setWardName(propertyID.getWard().getName());
			}
			if (null != propertyID.getArea()) {
				boundaryDetails.setBlockNumber(propertyID.getArea().getBoundaryNum());
				boundaryDetails.setBlockName(propertyID.getArea().getName());
			}
			if (null != propertyID.getLocality()) {
				boundaryDetails.setLocalityName(propertyID.getLocality().getName());
			}
			if (null != propertyID.getStreet()) {
				boundaryDetails.setStreetName(propertyID.getStreet().getName());
			}
		}
		return boundaryDetails;
	}

	private Set<OwnerName> prepareOwnerInfo(Property property) {
		List<PropertyOwnerInfo> propertyOwners = property.getBasicProperty().getPropertyOwnerInfo();
		Set<OwnerName> ownerNames = new HashSet<OwnerName>();
		if (propertyOwners != null && !propertyOwners.isEmpty()) {
			for (PropertyOwnerInfo propertyOwner : propertyOwners) {
				OwnerName ownerName = new OwnerName();
				ownerName.setAadhaarNumber(propertyOwner.getOwner().getAadhaarNumber());
				ownerName.setOwnerName(propertyOwner.getOwner().getName());
				ownerName.setMobileNumber(propertyOwner.getOwner().getMobileNumber());
				ownerName.setEmailId(propertyOwner.getOwner().getEmailId());
				ownerNames.add(ownerName);
			}
		}
		return ownerNames;
	}

	private String getPropertyAddress(Property property) {
		StringBuffer propertAddr = new StringBuffer();
		String houseNo = property.getBasicProperty().getAddress().getHouseNoBldgApt();
		String landmark = property.getBasicProperty().getAddress().getLandmark();
		String pinCode = property.getBasicProperty().getAddress().getPinCode();
		if (houseNo != null && houseNo.trim().length() != 0) {
			propertAddr.append(houseNo);
		}
		if (landmark != null && landmark.trim().length() != 0) {
			if (propertAddr.toString().length() != 0) {
				propertAddr.append(", ");
			}
			propertAddr.append(landmark);
		}
		if (pinCode != null && pinCode.trim().length() != 0) {
			if (propertAddr.toString().length() != 0) {
				propertAddr.append(", ");
			}
			propertAddr.append(pinCode);
		}
		return propertAddr.toString();
	}

	public PropertyTaxDetails getPropertyTaxDetails(String assessmentNo) {
		PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);

		ErrorDetails errorDetails = new ErrorDetails();
		if (null != basicProperty) {
			if (!basicProperty.isActive()) {
				errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_CODE);
				errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_MSG);
			} else {
				Set<PropertyStatusValues> statusValues = basicProperty.getPropertyStatusValuesSet();
				if (null != statusValues && !statusValues.isEmpty()) {
					for (PropertyStatusValues statusValue : statusValues) {
						if (statusValue.getPropertyStatus().getStatusCode() == PropertyTaxConstants.MARK_DEACTIVE) {
							errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_CODE);
							errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_MSG);
						}
					}
				}
			}
			Property property = basicProperty.getProperty();
			Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMap(property);

			String guardianName = "";
			List<PropertyOwnerInfo> propOwnerInfos = property.getBasicProperty().getPropertyOwnerInfo();
			StringBuffer ownerNameStr = new StringBuffer();
			for (int i = 0; i < propOwnerInfos.size(); i++) {
				PropertyOwnerInfo propOwnerInfo = propOwnerInfos.get(i);
				String ownerName = propOwnerInfo.getOwner().getName();
				if (null != ownerName && ownerName.trim().length() != 0) {
					if (ownerNameStr.toString().length() != 0) {
						ownerNameStr.append(", ");
					}
					ownerNameStr.append(ownerName);
					guardianName = propOwnerInfo.getOwner().getGuardian();
				}
			}
			String ownerAddress = PropertyTaxUtil.getOwnerAddress(property.getBasicProperty().getPropertyOwnerInfo());
			propertyTaxDetails.setGuardianName(guardianName);
			propertyTaxDetails.setOwnerName(ownerNameStr.toString());
			propertyTaxDetails.setOwnerAddress(ownerAddress);
			propertyTaxDetails.setHasArrears(getArrears(demandCollMap));
			TaxDetails currentTaxDetails = getCurrentTaxDetails(assessmentNo);
			if (null != currentTaxDetails) {
				propertyTaxDetails.setTaxAmt(currentTaxDetails.getGeneralTax());
				propertyTaxDetails.setEducationCess(currentTaxDetails.getEduCess());
				propertyTaxDetails.setLibraryCess(currentTaxDetails.getLibCess());
				propertyTaxDetails.setPropertyTax(currentTaxDetails.getGeneralTax());
				propertyTaxDetails.setPenalty(currentTaxDetails.getPenalty());
				propertyTaxDetails.setUnAuthPenalty(currentTaxDetails.getUnAuthPenalty());
				propertyTaxDetails.setDemandYear(currentTaxDetails.getDemandYear());
				propertyTaxDetails.setTotalTaxAmt(currentTaxDetails.getTotalTax());
			}
			Set<ArrearDetails> arrearDetailsSet = new LinkedHashSet<ArrearDetails>();
			if (propertyTaxDetails.getHasArrears()) {
				Set<TaxDetails> arrearsTaxDetails = getArrearsTaxDetails(assessmentNo);
				if (null != arrearsTaxDetails && !arrearsTaxDetails.isEmpty()) {
					for (TaxDetails taxDetails : arrearsTaxDetails) {
						ArrearDetails arrearDetails = new ArrearDetails();
						arrearDetails.setPenalty(taxDetails.getPenalty());
						arrearDetails.setTax(taxDetails.getGeneralTax());
						arrearDetails.setTotalSum(taxDetails.getTotalTax());
						arrearDetails.setDemandYear(taxDetails.getDemandYear());
						arrearDetails.setEducationCess(taxDetails.getEduCess());
						arrearDetails.setLibraryCess(taxDetails.getLibCess());
						arrearDetails.setPropertyTax(taxDetails.getGeneralTax());
						arrearDetails.setUnAuthPenalty(taxDetails.getUnAuthPenalty());
						arrearDetailsSet.add(arrearDetails);
					}
					propertyTaxDetails.setArrearDetails(arrearDetailsSet);
				}
			}
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
			propertyTaxDetails.setErrorDetails(errorDetails);
		} else {
			errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_CODE);
			errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_PREFIX + assessmentNo
					+ PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX);
		}
		return propertyTaxDetails;
	}
	
	// TODO: Needs to check whether this method is required or not. 
	// Also existing authentication functionality can be used
	public Boolean authenticateUser(String username, String password) {
		Boolean isAuthenticated = false;
		if(username.equals("ESEVATPT") && password.equals("ESEVA123")) {
			isAuthenticated = true;
		}
		return isAuthenticated;
	}
	
	private Boolean getArrears(Map<String, BigDecimal> demandCollMap) {
		Boolean hasArrear = false;
		BigDecimal arrDue = demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR));
		if(null != arrDue && !arrDue.equals(BigDecimal.ZERO)) {
			hasArrear = true;
		} 
		return hasArrear;
	}
	
	private Map<String, BigDecimal> getDemandYearPenalty(String applicationNo) {
		Map<String, BigDecimal> dmndYearPenaltyMap = new HashMap<String, BigDecimal> ();
		BigDecimal penalty = BigDecimal.ZERO;
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(applicationNo);
		PropertyTaxBillable propTaxBill = (PropertyTaxBillable) beanProvider.getBean(PropertyTaxConstants.BEANNAME_PROPERTY_TAX_BILLABLE);
		propTaxBill.setBasicProperty(basicProperty);
		propTaxBill.setLevyPenalty(true);
		Map<Installment, PenaltyAndRebate> penalties = propTaxBill.getCalculatedPenalty();
		Set<Installment> installments = penalties.keySet();
		for(Installment installment : installments) {
			PenaltyAndRebate penaltyAndRebate = penalties.get(installment);
			penalty = penaltyAndRebate.getPenalty();
			dmndYearPenaltyMap.put(installment.getDescription(), penalty);
		}
		return dmndYearPenaltyMap;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	private TaxDetails getCurrentTaxDetails(String applicationNo) {
		TaxDetails taxDetails = null;
		Set<String> demandYearSet = ptDemandDAO.getDemandYears(applicationNo);
		Map<String, BigDecimal> demandYearPenaltyMap =  getDemandYearPenalty(applicationNo);
		if (null != demandYearSet && !demandYearSet.isEmpty()) {
			List<Object> list = ptDemandDAO.getPropertyTaxDetails(applicationNo);
			Object[] demandYearsArr = demandYearSet.toArray();
			String currentDemandYear = (String) demandYearsArr[demandYearsArr.length - 1];
			taxDetails = new TaxDetails();
			taxDetails.setDemandYear(currentDemandYear);
			taxDetails.setPenalty(demandYearPenaltyMap.get(currentDemandYear));
			for (Object record : list) {
				Object[] data = (Object[]) record;
				String taxType = (String) data[0];
				BigInteger actVal = (BigInteger) data[2];
				BigDecimal taxVal = new BigDecimal(actVal);
				if (currentDemandYear.equalsIgnoreCase((String) data[1])) {
					taxDetails = getTaxDetailsByType(taxDetails, taxType, taxVal);
				}
			}
		}
		return taxDetails;
	}

	private Set<TaxDetails> getArrearsTaxDetails(String applicationNo) {
		Set<TaxDetails> arrearsTaxDetails = new LinkedHashSet<TaxDetails>();
		Set<String> demandYearSet = ptDemandDAO.getDemandYears(applicationNo);
		Map<String, BigDecimal> demandYearPenaltyMap =  getDemandYearPenalty(applicationNo);
		if (null != demandYearSet && !demandYearSet.isEmpty()) {
			List<Object> list = ptDemandDAO.getPropertyTaxDetails(applicationNo);
			Object[] demandYearsArr = demandYearSet.toArray();
			for (int i = 0; i < demandYearsArr.length - 1; i++) {
				String demandYear = (String) demandYearsArr[i];
				TaxDetails taxDetails = new TaxDetails();
				taxDetails.setDemandYear(demandYear);
				taxDetails.setPenalty(demandYearPenaltyMap.get(demandYear));
				for (Object record : list) {
					Object[] data = (Object[]) record;
					String taxType = (String) data[0];
					BigInteger actVal = (BigInteger) data[2];
					BigDecimal taxVal = new BigDecimal(actVal);
					if (demandYear.equalsIgnoreCase((String) data[1])) {
						taxDetails = getTaxDetailsByType(taxDetails, taxType, taxVal);
					}
				}
				arrearsTaxDetails.add(taxDetails);
			}
		}
		return arrearsTaxDetails;
	}
	
	private TaxDetails getTaxDetailsByType(TaxDetails taxDetails, String taxType, BigDecimal taxVal) {
		if (taxType.equalsIgnoreCase(PropertyTaxConstants.LIB_CESS)) {
			taxDetails.setLibCess(taxVal);
		}
		if (taxType.equalsIgnoreCase(PropertyTaxConstants.SEW_TAX)) {
			taxDetails.setSewarageTax(taxVal);
		}
		if (taxType.equalsIgnoreCase(PropertyTaxConstants.GEN_TAX)) {
			taxDetails.setGeneralTax(taxVal);
		}
		if (taxType.equalsIgnoreCase(PropertyTaxConstants.EDU_CESS)) {
			taxDetails.setEduCess(taxVal);
		}
		return taxDetails;
	}
}