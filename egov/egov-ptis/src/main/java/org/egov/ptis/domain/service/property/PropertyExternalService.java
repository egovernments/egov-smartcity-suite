package org.egov.ptis.domain.service.property;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.BoundaryDetails;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.PropertyDetails;
import org.springframework.beans.factory.annotation.Autowired;

public class PropertyExternalService {
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	@Autowired
	private PtDemandDao ptDemandDAO;

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
		Set<PropertyOwner> propertyOwners = property.getPropertyOwnerSet();
		Set<OwnerName> ownerNames = new HashSet<OwnerName>();
		if (propertyOwners != null && !propertyOwners.isEmpty()) {
			for (PropertyOwner propertyOwner : propertyOwners) {
				OwnerName ownerName = new OwnerName();
				ownerName.setAadhaarNumber(propertyOwner.getAadhaarNumber());
				ownerName.setOwnerName(propertyOwner.getName());
				ownerName.setMobileNumber(propertyOwner.getMobileNumber());
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

}