/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.domain.service.property;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.commons.Installment;
import org.egov.dcb.bean.Payment;
import org.egov.demand.model.EgBill;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.lib.admbndry.CrossHeirarchyImpl;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.integration.utils.CollectionHelper;
import org.egov.ptis.client.model.PenaltyAndRebate;
import org.egov.ptis.client.model.TaxDetails;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.TaxExeptionReason;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.model.ArrearDetails;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.BoundaryDetails;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.LocalityDetails;
import org.egov.ptis.domain.model.MasterCodeNamePairDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.PropertyDetails;
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.egov.ptis.domain.model.ReceiptDetails;
import org.egov.ptis.domain.service.bill.BillService;
import org.springframework.beans.factory.annotation.Autowired;

public class PropertyExternalService {
	public static final Integer FLAG_MOBILE_EMAIL = 0;
	public static final Integer FLAG_TAX_DETAILS = 1;
	public static final Integer FLAG_FULL_DETAILS = 2;
	public static final int FLAG_NONE = 0;
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	@Autowired
	private PtDemandDao ptDemandDAO;
	@Autowired
	private ApplicationContextBeanProvider beanProvider;
	private Long userId;
	private BasicProperty basicProperty;
	private Property property;
	AssessmentDetails assessmentDetail;

	private BillService billService;
	@Autowired
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	@Autowired
	PropertyTaxUtil propertyTaxUtil;
	@Autowired
	private PTBillServiceImpl ptBillServiceImpl;
	@Autowired
	private PropertyTaxBillable propertyTaxBillable;
	@Autowired
	private PropertyTypeMasterDAO propertyTypeMasterDAO;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private BoundaryService boundaryService;

	public AssessmentDetails loadAssessmentDetails(String propertyId, Integer flag) {
		assessmentDetail = new AssessmentDetails();
		assessmentDetail.setPropertyID(propertyId);
		assessmentDetail.setFlag(flag);
		validate();
		initiateBasicProperty();
		if (basicProperty != null) {
			property = basicProperty.getProperty();
			if (flag.equals(FLAG_MOBILE_EMAIL)) {
				loadPrimaryMobileAndEmail();
			}
			if (property != null) {
				PropertyDetails propertyDetails = new PropertyDetails();
				assessmentDetail.setPropertyDetails(propertyDetails);
				if (flag.equals(FLAG_FULL_DETAILS)) {
					getAsssessmentDetails();
					loadPropertyDues();
				}
				if (flag.equals(FLAG_TAX_DETAILS)) {
					loadPropertyDues();
				}
			}
		}
		return assessmentDetail;
	}

	private void validate() {
		if ((assessmentDetail.getPropertyID() == null || assessmentDetail.getPropertyID().trim().equals(""))) {
			throw new EGOVRuntimeException("PropertyID is null or empty!");
		}
		if ((assessmentDetail.getFlag() == null || assessmentDetail.getFlag() > 3)) {
			throw new EGOVRuntimeException("Invalid Flag");
		}
	}

	private void initiateBasicProperty() {
		basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentDetail.getPropertyID());
		ErrorDetails errorDetails = new ErrorDetails();
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

		} else {
			errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_CODE);
			errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_PREFIX
					+ assessmentDetail.getPropertyID() + PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX);
		}
		assessmentDetail.setErrorDetails(errorDetails);
	}

	private void loadPrimaryMobileAndEmail() {
		PropertyOwnerInfo propOwnerInfo = basicProperty.getPropertyOwnerInfo().get(0);
		assessmentDetail.setPrimaryEmail(propOwnerInfo.getOwner().getEmailId());
		assessmentDetail.setPrimaryMobileNo(propOwnerInfo.getOwner().getMobileNumber());
	}

	private void loadPropertyDues() {
		Map<String, BigDecimal> resultmap = ptDemandDAO.getDemandCollMap(property);
		if (null != resultmap && !resultmap.isEmpty()) {
			BigDecimal currDmd = resultmap.get(PropertyTaxConstants.CURR_DMD_STR);
			BigDecimal arrDmd = resultmap.get(PropertyTaxConstants.ARR_DMD_STR);
			BigDecimal currCollection = resultmap.get(PropertyTaxConstants.CURR_COLL_STR);
			BigDecimal arrColelection = resultmap.get(PropertyTaxConstants.ARR_COLL_STR);

			BigDecimal taxDue = currDmd.add(arrDmd).subtract(currCollection).subtract(arrColelection);
			assessmentDetail.getPropertyDetails().setTaxDue(taxDue);
			assessmentDetail.getPropertyDetails().setCurrentTax(currDmd);
			assessmentDetail.getPropertyDetails().setArrearTax(arrDmd);
		}
	}

	private void getAsssessmentDetails() {

		// Owner Details
		assessmentDetail.setBoundaryDetails(prepareBoundaryInfo(basicProperty));
		assessmentDetail.setPropertyAddress(basicProperty.getAddress().toString());
		if (null != property) {
			assessmentDetail.setOwnerNames(prepareOwnerInfo(property));
			// Property Details
			PropertyDetail propertyDetail = property.getPropertyDetail();
			if (null != propertyDetail) {
				assessmentDetail.getPropertyDetails().setPropertyType(propertyDetail.getPropertyTypeMaster().getType());
				if (propertyDetail.getPropertyUsage() != null) {
					assessmentDetail.getPropertyDetails()
							.setPropertyUsage(propertyDetail.getPropertyUsage().getUsageName());
				}
				if(null!=propertyDetail.getNo_of_floors())
				    assessmentDetail.getPropertyDetails().setNoOfFloors(propertyDetail.getNo_of_floors());
				else
				    assessmentDetail.getPropertyDetails().setNoOfFloors(0);
			}
		}
	}

	private BoundaryDetails prepareBoundaryInfo(BasicProperty basicProperty) {
		BoundaryDetails boundaryDetails = new BoundaryDetails();
		PropertyID propertyID = basicProperty.getPropertyID();
		if (null != propertyID) {
			if (null != propertyID.getZone()) {
				boundaryDetails.setZoneNumber(propertyID.getZone().getBoundaryNum());
				boundaryDetails.setZoneName(propertyID.getZone().getName());
				boundaryDetails.setZoneBoundaryType(propertyID.getZone().getBoundaryType().getName());
			}
			if (null != propertyID.getWard()) {
				boundaryDetails.setWardNumber(propertyID.getWard().getBoundaryNum());
				boundaryDetails.setWardName(propertyID.getWard().getName());
				boundaryDetails.setWardBoundaryType(propertyID.getWard().getBoundaryType().getName());
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
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
		PropertyTaxDetails propertyTaxDetails = null;
		ErrorDetails errorDetails = new ErrorDetails();
		if (null != basicProperty) {
			propertyTaxDetails = getPropertyTaxDetails(basicProperty);
		} else {
			propertyTaxDetails = new PropertyTaxDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_CODE);
			errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_PREFIX + assessmentNo
					+ PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX);
			propertyTaxDetails.setErrorDetails(errorDetails);
		}
		return propertyTaxDetails;
	}

	public List<PropertyTaxDetails> getPropertyTaxDetails(String circleName, String zoneName, String wardName,
			String blockName, String ownerName, String doorNo) {
		List<PropertyTaxDetails> propTxDetailsList = null;
		List<BasicProperty> basicPropertyList = basicPropertyDAO.getBasicPropertiesForTaxDetails(circleName, zoneName,
				wardName, blockName, ownerName, doorNo);
		if (null != basicPropertyList) {
			propTxDetailsList = new ArrayList<PropertyTaxDetails>();
			for (BasicProperty basicProperty : basicPropertyList) {
				PropertyTaxDetails propertyTaxDetails = getPropertyTaxDetails(basicProperty);
				propTxDetailsList.add(propertyTaxDetails);
			}
		}
		return propTxDetailsList;
	}

	// TODO: Needs to check whether this method is required or not.
	// Also existing authentication functionality can be used
	public Boolean authenticateUser(String username, String password) {
		Boolean isAuthenticated = false;
		if (username.equals("mahesh") && password.equals("demo")) {
			isAuthenticated = true;
		}
		return isAuthenticated;
	}

	private Boolean getArrears(Map<String, BigDecimal> demandCollMap) {
		Boolean hasArrear = false;
		BigDecimal arrDue = demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR));
		if (null != arrDue && !arrDue.equals(BigDecimal.ZERO)) {
			hasArrear = true;
		}
		return hasArrear;
	}

	private Map<String, BigDecimal> getDemandYearPenalty(String applicationNo) {
		Map<String, BigDecimal> dmndYearPenaltyMap = new HashMap<String, BigDecimal>();
		BigDecimal penalty = BigDecimal.ZERO;
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(applicationNo);
		PropertyTaxBillable propTaxBill = (PropertyTaxBillable) beanProvider
				.getBean(PropertyTaxConstants.BEANNAME_PROPERTY_TAX_BILLABLE);
		propTaxBill.setBasicProperty(basicProperty);
		propTaxBill.setLevyPenalty(true);
		Map<Installment, PenaltyAndRebate> penalties = propTaxBill.getCalculatedPenalty();
		Set<Installment> installments = penalties.keySet();
		for (Installment installment : installments) {
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
		Map<String, BigDecimal> demandYearPenaltyMap = null;
		if (null != demandYearSet && !demandYearSet.isEmpty()) {
			demandYearPenaltyMap = getDemandYearPenalty(applicationNo);
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
		Map<String, BigDecimal> demandYearPenaltyMap = getDemandYearPenalty(applicationNo);
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

	private PropertyTaxDetails getPropertyTaxDetails(BasicProperty basicProperty) {
		PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
		ErrorDetails errorDetails = new ErrorDetails();
		String guardianName = "";
		String assessmentNo = basicProperty.getUpicNo();
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
		}
		return propertyTaxDetails;
	}

	public ReceiptDetails payPropertyTax(String assessmentNo, String paymentMode, BigDecimal totalAmount, String paidBy,
			String username, String password) {
		ReceiptDetails receiptDetails = null;
		ErrorDetails errorDetails = null;
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
		propertyTaxBillable.setBasicProperty(basicProperty);
		propertyTaxBillable.setUserId(Long.valueOf("16"));
		EgovThreadLocals.setUserId(Long.valueOf("16"));
		billService = (BillService)beanProvider.getBean("billService");
		propertyTaxBillable.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty
				.getPropertyID().getWard().getBoundaryNum().toString()));
		propertyTaxBillable.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_MANUAL));
		propertyTaxBillable.setLevyPenalty(Boolean.TRUE);
		EgBill egBill = ptBillServiceImpl.generateBill(propertyTaxBillable);
		
		CollectionHelper collectionHelper = new CollectionHelper(egBill);
		Map<String, String> paymentDetailsMap = new HashMap<String, String>();
		paymentDetailsMap.put(PropertyTaxConstants.TOTAL_AMOUNT, totalAmount.toString());
		paymentDetailsMap.put(PropertyTaxConstants.PAID_BY, paidBy);
		Payment payment = Payment.create(paymentMode, paymentDetailsMap);
		BillReceiptInfo billReceiptInfo =  collectionHelper.executeCollection(payment);
		
		if(null != billReceiptInfo) {
			receiptDetails = new ReceiptDetails();
			receiptDetails.setReceiptNo(billReceiptInfo.getReceiptNum());
			receiptDetails.setReceiptDate(formatDate(billReceiptInfo.getReceiptDate()));
			receiptDetails.setPayeeName(billReceiptInfo.getPayeeName());
			receiptDetails.setPayeeAddress(billReceiptInfo.getPayeeAddress());
			receiptDetails.setBillReferenceNo(billReceiptInfo.getBillReferenceNum());
			receiptDetails.setServiceName(billReceiptInfo.getServiceName());
			receiptDetails.setDescription(billReceiptInfo.getDescription());
			receiptDetails.setPaidBy(billReceiptInfo.getPaidBy());
			receiptDetails.setTotalAmountPaid(billReceiptInfo.getTotalAmount());
			receiptDetails.setCollectionType(billReceiptInfo.getCollectionType());
			
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
			
			receiptDetails.setErrorDetails(errorDetails);
		}
		return receiptDetails;
	}
	
	public ErrorDetails payWaterTax(String consumerNo, String paymentMode, BigDecimal totalAmount, String paidBy,
			String username, String password) {
		ErrorDetails errorDetails = validatePaymentDetails(consumerNo, paymentMode, totalAmount, paidBy);
		if(null != errorDetails) {
			return errorDetails;
		} else {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
		}
		return errorDetails;
	}
	
	public List<MasterCodeNamePairDetails> getPropertyTypeMasterDetails() {
		List<MasterCodeNamePairDetails> propTypeMasterDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<PropertyTypeMaster> propertyTypeMasters = propertyTypeMasterDAO.findAll();
		for(PropertyTypeMaster propertyTypeMaster : propertyTypeMasters) {
			MasterCodeNamePairDetails propTypeMasterDetails = new MasterCodeNamePairDetails();
			propTypeMasterDetails.setCode(propertyTypeMaster.getCode());
			propTypeMasterDetails.setName(propertyTypeMaster.getType());
			propTypeMasterDetailsList.add(propTypeMasterDetails);
		}
		return propTypeMasterDetailsList;
	}
	
	public List<MasterCodeNamePairDetails> getPropertyTypeCategoryDetails(String categoryCode) {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		Map<String, String> codeNameMap = null;
		PropertyTypeMaster propertyTypeMasters = propertyTypeMasterDAO.getPropertyTypeMasterByCode(categoryCode);
		if (null != propertyTypeMasters) {
			if (propertyTypeMasters.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)) {
				codeNameMap = PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
			} else {
				codeNameMap = PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
			}
			if (null != codeNameMap && !codeNameMap.isEmpty()) {
				for (String code : codeNameMap.keySet()) {
					MasterCodeNamePairDetails mstrCodeNamepairDetails = new MasterCodeNamePairDetails();
					mstrCodeNamepairDetails.setCode(code);
					mstrCodeNamepairDetails.setName(codeNameMap.get(code));
					mstrCodeNamePairDetailsList.add(mstrCodeNamepairDetails);
				}
			}
		}
		return mstrCodeNamePairDetailsList;
	}

	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getApartmentsAndComplexes() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<Apartment> apartmentList = (List<Apartment>)entityManager.createQuery("from Apartment").getResultList();
		if (null != apartmentList) {
			for (Apartment apartment : apartmentList) {
				MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
				mstrCodeNamePairDetails.setCode(apartment.getCode());
				mstrCodeNamePairDetails.setName(apartment.getName());
				mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
			}
		}
		return mstrCodeNamePairDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getReasonsForCreateProperty() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		Query qry = entityManager.createQuery("from PropertyMutationMaster pmm where pmm.type = :type");
		qry.setParameter("type", PropertyTaxConstants.PROP_CREATE_RSN);
		List<PropertyMutationMaster> propMutationMasterList = (List<PropertyMutationMaster>)qry.getResultList();
		if (null != propMutationMasterList) {
			for (PropertyMutationMaster propMutationMaster : propMutationMasterList) {
				MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
				mstrCodeNamePairDetails.setCode(propMutationMaster.getCode());
				mstrCodeNamePairDetails.setName(propMutationMaster.getMutationName());
				mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
			}
		}
		return mstrCodeNamePairDetailsList;
	}
	
	public List<MasterCodeNamePairDetails> getLocalities() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<Boundary> localityList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
				PropertyTaxConstants.LOCALITY, PropertyTaxConstants.LOCATION_HIERARCHY_TYPE);
		if (null != localityList) {
			for (Boundary boundary : localityList) {
				MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
				mstrCodeNamePairDetails.setCode(boundary.getBoundaryNum().toString().concat("~").concat(boundary.getName()));
				mstrCodeNamePairDetails.setName(boundary.getName());
				mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
			}
		}
		return mstrCodeNamePairDetailsList;
	}
	
	public LocalityDetails getLocalityDetailsByBoundaryNo(Long boundaryNo, String name) {
		LocalityDetails localityDetails = null;
		Query qry = entityManager.createQuery("from Boundary b where b.boundaryNum = :boundaryNo and b.name = :name");
		qry.setParameter("boundaryNo", boundaryNo);
		qry.setParameter("name", name);
		List list = qry.getResultList();
		if(null != list && !list.isEmpty()) {
			localityDetails = new LocalityDetails();
			Boundary boundary =  (Boundary)list.get(0);
			qry = entityManager.createQuery("from CrossHeirarchyImpl cr where cr.child = :child");
			qry.setParameter("child", boundary);
			list = qry.getResultList();
			if(null != list && !list.isEmpty()) {
				CrossHeirarchyImpl crossHeirarchyImpl = (CrossHeirarchyImpl)list.get(0);
				qry = entityManager.createQuery("from Boundary b where b.id = :id");
				qry.setParameter("id", crossHeirarchyImpl.getParent().getId());
				list = qry.getResultList();
				if(null != list && !list.isEmpty()) {
					Boundary block = (Boundary)list.get(0);
					localityDetails.setBlockName(block.getName());
					qry = entityManager.createQuery("from Boundary b where b.id = :id");
					qry.setParameter("id", block.getParent().getId());
					list = qry.getResultList();
					if(null != list && !list.isEmpty()) {
						Boundary ward = (Boundary)list.get(0);
						localityDetails.setWardName(ward.getName());
						qry = entityManager.createQuery("from Boundary b where b.id = :id");
						qry.setParameter("id", ward.getParent().getId());
						list = qry.getResultList();
						if(null != list && !list.isEmpty()) {
							Boundary zone = (Boundary)list.get(0);
							localityDetails.setZoneName(zone.getName());
						}
					}
				}
			}
		}
		return localityDetails;
	}
	
	public List<MasterCodeNamePairDetails> getElectionWards() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<Boundary> electionWardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
				PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE, PropertyTaxConstants.ELECTION_HIERARCHY_TYPE);
		for (Boundary boundary : electionWardList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(boundary.getBoundaryNum().toString());
			mstrCodeNamePairDetails.setName(boundary.getName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	public List<MasterCodeNamePairDetails> getEnumerationBlocks() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<Boundary> enumerationBlockList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
				PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE, PropertyTaxConstants.ELECTION_HIERARCHY_TYPE);
		for (Boundary boundary : enumerationBlockList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(boundary.getBoundaryNum().toString());
			mstrCodeNamePairDetails.setName(boundary.getName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getFloorTypes() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<FloorType> floorTypeList = (List<FloorType>)entityManager.createQuery("from FloorType order by name").getResultList();
		for (FloorType floorType : floorTypeList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(floorType.getCode());
			mstrCodeNamePairDetails.setName(floorType.getName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getRoofTypes() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<RoofType> roofTypeList = (List<RoofType>)entityManager.createQuery("from RoofType order by name").getResultList();
		for (RoofType roofType : roofTypeList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(roofType.getCode());
			mstrCodeNamePairDetails.setName(roofType.getName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getWallTypes() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<WallType> wallTypeList = (List<WallType>)entityManager.createQuery("from WallType order by name").getResultList();
		for (WallType wallType : wallTypeList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(wallType.getCode());
			mstrCodeNamePairDetails.setName(wallType.getName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getWoodTypes() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<WoodType> woodTypeList = (List<WoodType>)entityManager.createQuery("from WoodType order by name").getResultList();
		for (WoodType woodType : woodTypeList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(woodType.getCode());
			mstrCodeNamePairDetails.setName(woodType.getName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getBuildingClassifications() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<StructureClassification> structClsfList = (List<StructureClassification>)entityManager.createQuery("from StructureClassification").getResultList();
		for (StructureClassification structClsf : structClsfList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(structClsf.getConstrTypeCode());
			mstrCodeNamePairDetails.setName(structClsf.getTypeName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getNatureOfUsages() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<PropertyUsage> usageList = (List<PropertyUsage>)entityManager.createQuery("from PropertyUsage order by usageName").getResultList();
		for (PropertyUsage propUsage : usageList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(propUsage.getUsageCode());
			mstrCodeNamePairDetails.setName(propUsage.getUsageName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getOccupancies() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<PropertyOccupation> propOccupList = (List<PropertyOccupation>)entityManager.createQuery("from PropertyOccupation").getResultList();
		for (PropertyOccupation propOccup : propOccupList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(propOccup.getOccupancyCode());
			mstrCodeNamePairDetails.setName(propOccup.getOccupation());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getExemptionCategories() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<TaxExeptionReason> taxExeptionReasonList = (List<TaxExeptionReason>)entityManager.createQuery("from TaxExeptionReason order by name").getResultList();
		for (TaxExeptionReason taxExeptionReason : taxExeptionReasonList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(taxExeptionReason.getCode());
			mstrCodeNamePairDetails.setName(taxExeptionReason.getName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getApproverDepartments() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<Department> approverDepartmentList = (List<Department>)entityManager.createQuery("from Department order by name").getResultList();
		for (Department approverDepartment : approverDepartmentList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(approverDepartment.getCode());
			mstrCodeNamePairDetails.setName(approverDepartment.getName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	/**
	 * This method is used to validate the payment details to do the payments.
	 * 
	 * @param assessmentNo
	 *            - assessment number or property number
	 * @param paymentMode
	 *            - mode of payment
	 * @param totalAmount
	 *            - total amount
	 * @param paidBy
	 *            - name of the payer
	 * @return
	 */
	public ErrorDetails validatePaymentDetails(String assessmentNo, String paymentMode, BigDecimal totalAmount, String paidBy) {
		ErrorDetails errorDetails = null;
		if(assessmentNo == null || assessmentNo.trim().length() == 0) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_REQUIRED);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_REQUIRED);
		} else {
			if(assessmentNo.trim().length() > 0 && assessmentNo.trim().length() < 10) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_LEN);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_LEN);
			}
			if (!basicPropertyDAO.isAssessmentNoExist(assessmentNo)) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
				errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
			}
		}
		
		if(paymentMode == null || paymentMode.trim().length() == 0) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_REQUIRED);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_REQUIRED);
		} else if (!PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CASH.equalsIgnoreCase(paymentMode.trim())
				&& !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(paymentMode.trim())) {
			errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_INVALID);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_INVALID);
		}
		return errorDetails;
	}

	private String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		return sdf.format(date);
	}
}