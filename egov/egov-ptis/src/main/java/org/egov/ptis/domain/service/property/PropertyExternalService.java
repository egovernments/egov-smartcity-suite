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
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.text.ParseException;
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

import org.apache.commons.io.FilenameUtils;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.dcb.bean.Payment;
import org.egov.demand.model.EgBill;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.CrossHierarchy;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.CorrespondenceAddress;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
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
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatus;
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
import org.egov.ptis.domain.model.FloorDetails;
import org.egov.ptis.domain.model.LocalityDetails;
import org.egov.ptis.domain.model.MasterCodeNamePairDetails;
import org.egov.ptis.domain.model.NewPropertyDetails;
import org.egov.ptis.domain.model.OwnerDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.PropertyDetails;
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.egov.ptis.domain.model.ReceiptDetails;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.multipart.MultipartFile;

import com.sun.jersey.core.header.FormDataContentDisposition;

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
	private PropertyImpl property;
	AssessmentDetails assessmentDetail;

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
	@Autowired
	private PropertyService propService;
	private List<File> uploads = new ArrayList<File>();
	private List<String> uploadContentTypes = new ArrayList<String>();
	@Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
	@Autowired
    private PropertyPersistenceService basicPropertyService;
	private List<String> uploadFileNames = new ArrayList<String>();

	@Autowired
	private BoundaryTypeService boundaryTypeService;
	@Autowired
    protected AssignmentService assignmentService;
	@Autowired
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
	@Autowired
	private UserService userService;

	public AssessmentDetails loadAssessmentDetails(String propertyId, Integer flag) {
		assessmentDetail = new AssessmentDetails();
		assessmentDetail.setPropertyID(propertyId);
		assessmentDetail.setFlag(flag);
		validate();
		initiateBasicProperty();
		if (basicProperty != null) {
			property = (PropertyImpl) basicProperty.getProperty();
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
			throw new ApplicationRuntimeException("PropertyID is null or empty!");
		}
		if ((assessmentDetail.getFlag() == null || assessmentDetail.getFlag() > 3)) {
			throw new ApplicationRuntimeException("Invalid Flag");
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
				if(null!=propertyDetail.getNoofFloors())
				    assessmentDetail.getPropertyDetails().setNoOfFloors(propertyDetail.getNoofFloors());
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
			String blockName, String ownerName, String doorNo, String aadhaarNumber, String mobileNumber) {
		List<PropertyTaxDetails> propTxDetailsList = null;
		List<BasicProperty> basicPropertyList = basicPropertyDAO.getBasicPropertiesForTaxDetails(circleName, zoneName,
				wardName, blockName, ownerName, doorNo, aadhaarNumber, mobileNumber);
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

	public ReceiptDetails payPropertyTax(String assessmentNo, String paymentMode, BigDecimal totalAmount, String paidBy) {
		ReceiptDetails receiptDetails = null;
		ErrorDetails errorDetails = null;
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
		propertyTaxBillable.setBasicProperty(basicProperty);
		propertyTaxBillable.setUserId(Long.valueOf("16"));
		EgovThreadLocals.setUserId(Long.valueOf("16"));
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
	
	public ErrorDetails payWaterTax(String consumerNo, String paymentMode, BigDecimal totalAmount, String paidBy) {
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
	
	public PropertyTypeMaster getPropertyTypeMasterByCode(String propertyTypeMasterCode) {
		Query qry = entityManager.createQuery("from PropertyTypeMaster ptm where ptm.code = :propertyTypeMasterCode");
		qry.setParameter("propertyTypeMasterCode", propertyTypeMasterCode);
		PropertyTypeMaster propertyTypeMaster = (PropertyTypeMaster)qry.getSingleResult();
		return propertyTypeMaster;
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
	
	public List<MasterCodeNamePairDetails> getPropertyTypes() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		MasterCodeNamePairDetails mstrCodeNamepairDetails = null;
		Map<String, String> vacLandMap = PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
		Map<String, String> nonVacLandMap = PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
		
		for(String key : vacLandMap.keySet()) {
			 mstrCodeNamepairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamepairDetails.setCode(key);
			mstrCodeNamepairDetails.setName(vacLandMap.get(key));
			mstrCodeNamePairDetailsList.add(mstrCodeNamepairDetails);
		}

		for (String code : nonVacLandMap.keySet()) {
			 mstrCodeNamepairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamepairDetails.setCode(code);
			mstrCodeNamepairDetails.setName(nonVacLandMap.get(code));
			mstrCodeNamePairDetailsList.add(mstrCodeNamepairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	public List<MasterCodeNamePairDetails> getPropertyTypes(String categoryCode) {
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
	public List<MasterCodeNamePairDetails> getReasonsForChangeProperty(String reason) {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		Query qry = entityManager.createQuery("from PropertyMutationMaster pmm where pmm.type = :type");
		qry.setParameter("type", reason);
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
	
	public LocalityDetails getLocalityDetailsByLocalityCode(String localityCode) {
		Long boundaryNo = Long.valueOf(localityCode.substring(0, localityCode.indexOf("~")).trim());
		String name = localityCode.substring(localityCode.indexOf("~") + 1).trim();
		LocalityDetails localityDetails = null;
		Query qry = entityManager.createQuery("from Boundary b where b.boundaryNum = :boundaryNo and b.name = :name");
		qry.setParameter("boundaryNo", boundaryNo);
		qry.setParameter("name", name);
		List list = qry.getResultList();
		if (null != list && !list.isEmpty()) {
			localityDetails = new LocalityDetails();
			Boundary boundary = (Boundary) list.get(0);
			qry = entityManager.createQuery("from CrossHierarchy cr where cr.child = :child");
			qry.setParameter("child", boundary);
			list = qry.getResultList();
			if (null != list && !list.isEmpty()) {
				CrossHierarchy crossHeirarchyImpl = (CrossHierarchy) list.get(0);
				qry = entityManager.createQuery("from Boundary b where b.id = :id");
				qry.setParameter("id", crossHeirarchyImpl.getParent().getId());
				list = qry.getResultList();
				if (null != list && !list.isEmpty()) {
					Boundary block = (Boundary) list.get(0);
					localityDetails.setBlockName(block.getName());
					qry = entityManager.createQuery("from Boundary b where b.id = :id");
					qry.setParameter("id", block.getParent().getId());
					list = qry.getResultList();
					if (null != list && !list.isEmpty()) {
						Boundary ward = (Boundary) list.get(0);
						localityDetails.setWardName(ward.getName());
						qry = entityManager.createQuery("from Boundary b where b.id = :id");
						qry.setParameter("id", ward.getParent().getId());
						list = qry.getResultList();
						if (null != list && !list.isEmpty()) {
							Boundary zone = (Boundary) list.get(0);
							localityDetails.setZoneName(zone.getName());
						}
					}
				}
			}
		}
		return localityDetails;
	}
	
	public List<MasterCodeNamePairDetails> getElectionBoundaries() {
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
	
	public Boundary getElectionBoundaryByCode(String electionBoundaryCode) {
		BoundaryType boundaryType = boundaryTypeService
				.getBoundaryTypeByName(PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE);
		Boundary electionBoundary = boundaryService.getBoundaryByTypeAndNo(boundaryType,
				Long.valueOf(electionBoundaryCode));
		return electionBoundary;
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

	public FloorType getFloorTypeByCode(String code) {
		Query qry = entityManager.createQuery("from FloorType ft where ft.code = :code");
		qry.setParameter("code", code);
		FloorType floorType = (FloorType)qry.getSingleResult();
		return floorType;
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

	public RoofType getRoofTypeByCode(String code) {
		Query qry = entityManager.createQuery("from RoofType rt where rt.code = :code");
		qry.setParameter("code", code);
		RoofType roofType = (RoofType)qry.getSingleResult();
		return roofType;
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

	public WallType getWallTypeByCode(String code) {
		Query qry = entityManager.createQuery("from WallType wt where wt.code = :code");
		qry.setParameter("code", code);
		WallType wallType = (WallType)qry.getSingleResult();
		return wallType;
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

	public WoodType getWoodTypeByCode(String code) {
		Query qry = entityManager.createQuery("from WoodType wt where wt.code = :code");
		qry.setParameter("code", code);
		WoodType woodType = (WoodType)qry.getSingleResult();
		return woodType;
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
	
	public StructureClassification getStructureClassificationByCode(String classificationCode) {
		Query qry = entityManager.createQuery("from StructureClassification sc where sc.constrTypeCode =:code");
		qry.setParameter("code", classificationCode);
		return (StructureClassification)qry.getSingleResult();
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
	
	public PropertyUsage getPropertyUsageByUsageCde(String usageCode) {
		Query qry = entityManager.createQuery("from PropertyUsage pu where pu.usageCode = :usageCode order by usageName");
		qry.setParameter("usageCode", usageCode);
		PropertyUsage propertyUsage = (PropertyUsage)qry.getSingleResult();
		return propertyUsage;
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
	
	public PropertyOccupation getPropertyOccupationByOccupancyCode(String occupancyCode) {
		Query qry = entityManager.createQuery("from PropertyOccupation po where po.occupancyCode = :occupancyCode");
		qry.setParameter("occupancyCode", occupancyCode);
		PropertyOccupation propertyOccupation = (PropertyOccupation)qry.getSingleResult();
		return propertyOccupation;
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
	
	public TaxExeptionReason getTaxExemptionReasonByCode(String exemptionReasonCode) {
		TaxExeptionReason taxExeptionReason = null;
		Query qry = entityManager.createQuery("from TaxExeptionReason ter where ter.code = :code");
		qry.setParameter("code", ""+exemptionReasonCode);
		List list = qry.getResultList();
		if(null != list && !list.isEmpty()) {
			taxExeptionReason = (TaxExeptionReason)qry.getSingleResult();
		}
		return taxExeptionReason;
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
	
	public NewPropertyDetails createNewProperty(String propertyTypeMasterCode, String propertyCategoryCode, 
			String apartmentCmplxCode, List<OwnerDetails> ownerDetailsList, String mutationReasonCode,
			String extentOfSite, String isExtentAppurtenantLand, String occupancyCertificationNo,
			Boolean isSuperStructure, Boolean isBuildingPlanDetails, String regdDocNo, String regdDocDate,
			String localityCode, String street, String electionWardCode, String doorNo, String enumerationBlockCode,
			String pinCode, Boolean isCorrAddrDiff, String corrAddr1, String corrAddr2, String corrPinCode,
			Boolean hasLift, Boolean hasToilet, Boolean hasWaterTap, Boolean hasElectricity, String hasAttachedBathroom,
			String hasWaterHarvesting, String floorTypeCode, String roofTypeCode, String wallTypeCode,
			String woodTypeCode, List<FloorDetails> floorDetailsList, String surveyNumber, String pattaNumber,
			Double vacantLandArea, Double marketValue, Double currentCapitalValue, String completionDate,
			String northBoundary, String southBoundary, String eastBoundary, String westBoundary, List<Document> documents)
					throws ParseException {

		NewPropertyDetails newPropertyDetails = null;
		BasicProperty basicProperty = createBasicProperty(propertyTypeMasterCode, propertyCategoryCode, mutationReasonCode, ownerDetailsList,
				extentOfSite, regdDocNo, regdDocDate, localityCode, street, doorNo, electionWardCode, pinCode,
				isCorrAddrDiff, corrAddr1, corrAddr2, corrPinCode, floorTypeCode, roofTypeCode, wallTypeCode,
				woodTypeCode, floorDetailsList, completionDate, northBoundary, southBoundary, eastBoundary,
				westBoundary, documents);
		basicProperty.setIsTaxXMLMigrated(PropertyTaxConstants.STATUS_YES_XML_MIGRATION);
		processAndStoreDocumentsWithReason(basicProperty, PropertyTaxConstants.DOCS_CREATE_PROPERTY);
		basicProperty.setPropertyOwnerInfoProxy(getPropertyOwnerInfoList(ownerDetailsList));
		basicPropertyService.createOwners(property, basicProperty, basicProperty.getAddress());
		transitionWorkFlow(property);
		basicPropertyService.applyAuditing(property.getState());
		basicProperty = basicPropertyService.persist(basicProperty);
		if (null != basicProperty) {
			newPropertyDetails = new NewPropertyDetails();
			newPropertyDetails.setApplicationNo(basicProperty.getProperty().getApplicationNo());
			ErrorDetails errorDetails = new ErrorDetails();
			errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
			errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
			newPropertyDetails.setErrorDetails(errorDetails);
		}
		return newPropertyDetails;
	}
	
	private BasicProperty createBasicProperty(String propertyTypeMasterCode, String propertyCategoryCode, String mutationReasonCode,
			List<OwnerDetails> ownerDetailsList, String extentOfSite, String regdDocNo, String regdDocDate,
			String localityCode, String street, String doorNo, String electionWardCode, String pinCode,
			Boolean isCorrAddrDiff, String corrAddr1, String corrAddr2, String corrPinCode, String floorTypeCode,
			String roofTypeCode, String wallTypeCode, String woodTypeCode, List<FloorDetails> floorDetailsList,
			String completionDate, String northBoundary, String southBoundary, String eastBoundary, String westBoundary, 
			List<Document> documents)
					throws ParseException {
		BasicProperty basicProperty = new BasicPropertyImpl();
		basicProperty.setRegdDocNo(regdDocNo);
		basicProperty.setRegdDocDate(convertStringToDate(regdDocDate));
		basicProperty.setActive(Boolean.TRUE);
		// Creating Property Address object
		String addressString = "";
		PropertyAddress propAddress = createPropAddress(localityCode, street, doorNo, addressString, pinCode,
				isCorrAddrDiff, doorNo, corrAddr1, corrAddr2, corrPinCode);
		basicProperty.setAddress(propAddress);

		// Creating PropertyID object based on basic property, localityCode and
		// boundary map direction
		PropertyID propertyID = createPropertID(basicProperty, localityCode, electionWardCode, northBoundary,
				southBoundary, eastBoundary, westBoundary);
		basicProperty.setPropertyID(propertyID);

		// Get PropertyStatus object to set the status of the property
		PropertyStatus propertyStatus = getPropertyStatus();
		basicProperty.setStatus(propertyStatus);

		// Set underworkflow status as true
		basicProperty.setUnderWorkflow(Boolean.TRUE);

		// Set PropertyMutationMaster object
		PropertyMutationMaster propertyMutationMaster = getPropertyMutationMaster(mutationReasonCode);
		basicProperty.setPropertyMutationMaster(propertyMutationMaster);
		basicProperty.addPropertyStatusValues(propService.createPropStatVal(basicProperty, PROP_CREATE_RSN, null, null,
                null, null, null)); 
		// Set isBillCreated property value as false
		basicProperty.setIsBillCreated(PropertyTaxConstants.STATUS_BILL_NOTCREATED);

		PropertyTypeMaster propertyTypeMaster = getPropertyTypeMasterByCode(propertyTypeMasterCode);
		PropertyUsage propertyUsage = getPropertyUsageByUsageCde(floorDetailsList.get(0).getNatureOfUsageCode());
		PropertyOccupation propertyOccupation = getPropertyOccupationByOccupancyCode(
				floorDetailsList.get(0).getOccupancyCode());
		FloorType floorType = getFloorTypeByCode(floorTypeCode);
		RoofType roofType = getRoofTypeByCode(roofTypeCode);
		WallType wallType = getWallTypeByCode(wallTypeCode);
		WoodType woodType = getWoodTypeByCode(woodTypeCode);
		// TODO: Need to check whether this field is really required
		String nonResPlotArea = null; 
		PropertyImpl propertyImpl = createPropertyWithBasicDetails();
		propertyImpl.setBasicProperty(basicProperty);
		propertyImpl.getPropertyDetail().setFloorDetailsProxy(getFloorList(floorDetailsList));
		propertyImpl.getBasicProperty().setPropertyID(propertyID);
		propertyImpl.setDocuments(documents);
		TaxExeptionReason taxExemptedReason = getTaxExemptionReasonByCode(floorDetailsList.get(0).getExemptionCategoryCode());
		propertyImpl.setTaxExemptedReason(taxExemptedReason);
		property = propService.createProperty(propertyImpl, extentOfSite, mutationReasonCode,
				propertyTypeMaster.getId().toString(), propertyUsage.getId().toString(),
				propertyOccupation.getId().toString(), PropertyTaxConstants.STATUS_ISACTIVE, regdDocNo, nonResPlotArea,
				floorType.getId(), roofType.getId(), wallType.getId(), woodType.getId(), taxExemptedReason != null ? taxExemptedReason.getId() : null);
		property.setStatus(PropertyTaxConstants.STATUS_ISACTIVE);
		property.setPropertyModifyReason(PROP_CREATE_RSN);
		property.getPropertyDetail().setCategoryType(propertyCategoryCode);
		basicProperty.addProperty(property);
		
		Date propCompletionDate = null;
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
			propCompletionDate = propService
					.getLowestDtOfCompFloorWise(property.getPropertyDetail().getFloorDetailsProxy());
		} else {
			propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
		}
		basicProperty.setPropOccupationDate(propCompletionDate);

		if (property != null && !property.getDocuments().isEmpty()) {
			propService.processAndStoreDocument(property.getDocuments());
		}
		propService.createDemand(propertyImpl, propCompletionDate);
		return basicProperty;
	}
	
	private PropertyStatus getPropertyStatus() {
		Query qry = entityManager.createQuery("from PropertyStatus where statusCode = :statusCode");
		qry.setParameter("statusCode", PropertyTaxConstants.PROPERTY_STATUS_WORKFLOW);
		PropertyStatus propertyStatus = (PropertyStatus)qry.getSingleResult();
		return propertyStatus;
	}
	
	private PropertyAddress createPropAddress(String localityCode, String street, String doorNo, String addressString, String pinCode, Boolean isCorrespondenceAddrDiff, String coorDoorNo, String corrAddr1, String corrAddr2, String corrPinCode) {
        Address propAddr = new PropertyAddress();
        propAddr.setHouseNoBldgApt(doorNo);
        if(null != street && !street.isEmpty()) {
        	propAddr.setStreetRoadLine(street);
        }
        LocalityDetails localityDetails = getLocalityDetailsByLocalityCode(localityCode);
        if(null != localityDetails) {
        	propAddr.setAreaLocalitySector(localityDetails.getBlockName());
        }

        if (pinCode != null && !pinCode.isEmpty()) {
        	propAddr.setPinCode(pinCode);
        }
        Address ownerAddress = null;
        if(isCorrespondenceAddrDiff) {
        	ownerAddress = createCorrespondenceAddress(doorNo, corrAddr1, corrAddr2, corrPinCode);
        } else {
        	ownerAddress = new CorrespondenceAddress();
            ownerAddress.setAreaLocalitySector(propAddr.getAreaLocalitySector());
            ownerAddress.setHouseNoBldgApt(propAddr.getHouseNoBldgApt());
            ownerAddress.setStreetRoadLine(propAddr.getStreetRoadLine());
            ownerAddress.setPinCode(propAddr.getPinCode());
        }
        System.out.println("PropertyAddress: " + propAddr + "\nExiting from createPropAddress");
        return (PropertyAddress) propAddr;
    }
	
	private Address createCorrespondenceAddress(String corrDoorNo, String corrAddr1, String corrAddr2, String corrPinCode) {
		Address ownerCorrAddr = new CorrespondenceAddress();
		ownerCorrAddr.setHouseNoBldgApt(corrDoorNo);
		ownerCorrAddr.setAreaLocalitySector(corrAddr1);
		ownerCorrAddr.setStreetRoadLine(corrAddr2);
		ownerCorrAddr.setPinCode(corrPinCode);
		return ownerCorrAddr;
	}
	
	private PropertyID createPropertID(BasicProperty basicProperty, String localityCode, String electionBoundaryCode,
			String northBoundary, String southBoundary, String eastBoundary, String westBoundary) {
		PropertyID propertyID = new PropertyID();

		Long boundaryNo = Long.valueOf(localityCode.substring(0, localityCode.indexOf("~")).trim());
		String boundaryName = localityCode.substring(localityCode.indexOf("~") + 1).trim();

		Boundary block = getBoundaryAsBlock(boundaryNo, boundaryName);
		Boundary ward = getBoundaryAsWard(block);
		Boundary zone = getBoundaryAsZone(ward);
		Boundary locality = getBoundaryAsLocality(boundaryNo, boundaryName);
		propertyID.setArea(block);
		propertyID.setWard(ward);
		propertyID.setZone(zone);
		propertyID.setLocality(locality);
		/*propertyID.setCreatedDate(new Date());
		propertyID.setModifiedDate(new Date());*/
		propertyID.setLocality(locality);
		propertyID.setBasicProperty(basicProperty);
		Boundary electionBoundary = getElectionBoundaryByCode(electionBoundaryCode);
		if (null != electionBoundary) {
			propertyID.setElectionBoundary(electionBoundary);
		}
		if (null != northBoundary && northBoundary.trim().length() != 0) {
			propertyID.setNorthBoundary(northBoundary);
		}
		if (null != southBoundary && southBoundary.trim().length() != 0) {
			propertyID.setSouthBoundary(southBoundary);
		}
		if (null != eastBoundary && eastBoundary.trim().length() != 0) {
			propertyID.setEastBoundary(eastBoundary);
		}
		if (null != westBoundary && westBoundary.trim().length() != 0) {
			propertyID.setWestBoundary(westBoundary);
		}
		propertyID.setBasicProperty(basicProperty);
		return propertyID;
	}
	
	private Boundary getBoundaryAsBlock(Long boundaryNo, String boundaryName) {
		Query qry = entityManager.createQuery("from Boundary b where b.boundaryNum = :boundaryNo and b.name = :name");
		qry.setParameter("boundaryNo", boundaryNo);
		qry.setParameter("name", boundaryName);
		Boundary boundary = (Boundary) qry.getSingleResult();
		qry = entityManager.createQuery("from CrossHierarchy cr where cr.child = :child");
		qry.setParameter("child", boundary);
		CrossHierarchy crossHeirarchyImpl = (CrossHierarchy) qry.getSingleResult();
		qry = entityManager.createQuery("from Boundary b where b.id = :id");
		qry.setParameter("id", crossHeirarchyImpl.getParent().getId());
		Boundary block = (Boundary) qry.getSingleResult();
		return block;
	}
	
	private Boundary getBoundaryAsWard(Boundary block) {
		Query qry = entityManager.createQuery("from Boundary b where b.id = :id");
		qry.setParameter("id", block.getParent().getId());
		Boundary ward = (Boundary) qry.getSingleResult();
		return ward;
	}

	private Boundary getBoundaryAsZone(Boundary ward) {
		Query qry = entityManager.createQuery("from Boundary b where b.id = :id");
		qry.setParameter("id", ward.getParent().getId());
		Boundary zone = (Boundary) qry.getSingleResult();
		return zone;
	}

	private Boundary getBoundaryAsLocality(Long boundaryNo, String boundaryName) {
		Query qry = entityManager
				.createQuery("from Boundary b where b.boundaryNum = :boundaryNum and b.name = :boundaryName");
		qry.setParameter("boundaryNum", boundaryNo);
		qry.setParameter("boundaryName", boundaryName);
		Boundary locality = (Boundary) qry.getSingleResult();
		return locality;
	}

	private PropertyMutationMaster getPropertyMutationMaster(String mutationReasonCode) {
		Query qry = entityManager.createQuery("from PropertyMutationMaster pmm where pmm.code = :mutationReasonCode");
		qry.setParameter("mutationReasonCode", mutationReasonCode);
		PropertyMutationMaster propMutationMaster = (PropertyMutationMaster) qry.getSingleResult();
		return propMutationMaster;
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(date);
	}
	
	private Date convertStringToDate(String dateInString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date stringToDate = sdf.parse(dateInString);
		return stringToDate;
	}

	private void processAndStoreDocumentsWithReason(BasicProperty basicProperty, String reason) {
		if (!uploads.isEmpty()) {
			int fileCount = 0;
			for (final File file : uploads) {
				final FileStoreMapper fileStore = fileStoreService.store(file, uploadFileNames.get(fileCount),
						uploadContentTypes.get(fileCount++), "PTIS");
				final PropertyDocs propertyDoc = new PropertyDocs();
				propertyDoc.setSupportDoc(fileStore);
				propertyDoc.setBasicProperty(basicProperty);
				propertyDoc.setReason(reason);
				basicProperty.addDocs(propertyDoc);
			}
		}
	}

	private PropertyImpl createPropertyWithBasicDetails() {
		PropertyImpl propertyImpl = new PropertyImpl();
		propertyImpl.setPropertyDetail(new BuiltUpProperty());
		propertyImpl.setBasicProperty(new BasicPropertyImpl());
		return propertyImpl;
	}
	
	private List<Floor> getFloorList(List<FloorDetails> floorDetailsList) throws ParseException {
		List<Floor> floorList = new ArrayList<Floor>();
		for (FloorDetails floorDetials : floorDetailsList) {
			Floor floor = new Floor();
			floor.setFloorNo(Integer.valueOf(floorDetials.getFloorNoCode()));
			floor.setStructureClassification(
					getStructureClassificationByCode(floorDetials.getBuildClassificationCode()));
			floor.setPropertyUsage(getPropertyUsageByUsageCde(floorDetials.getNatureOfUsageCode()));
			floor.setPropertyOccupation(getPropertyOccupationByOccupancyCode(floorDetials.getOccupancyCode()));
			floor.setOccupantName(floorDetials.getOccupantName());
			floor.setOccupancyDate(convertStringToDate(floorDetials.getConstructionDate()));
			floor.setCreatedDate(convertStringToDate(floorDetials.getConstructionDate()));
			Area builtUpArea = new Area();
			builtUpArea.setArea(floorDetials.getPlinthArea());
			floor.setBuiltUpArea(builtUpArea);
			floor.setDrainage(floorDetials.getDrainageCode());
			floor.setNoOfSeats(floorDetials.getNoOfSeats());
			floorList.add(floor);
		}
		return floorList;
	}
	
	private List<PropertyOwnerInfo> getPropertyOwnerInfoList(List<OwnerDetails> ownerDetailsList) {
		List<PropertyOwnerInfo> proeprtyOwnerInfoList = new ArrayList<PropertyOwnerInfo>();
		for (OwnerDetails ownerDetais : ownerDetailsList) {
			PropertyOwnerInfo ownerInfo = new PropertyOwnerInfo();
			User owner = new User();
			owner.setAadhaarNumber(ownerDetais.getAadhaarNo());
			owner.setSalutation(ownerDetais.getSalutationCode());
			owner.setName(ownerDetais.getName());
			owner.setGender(Gender.valueOf(ownerDetais.getGender()));
			owner.setMobileNumber(ownerDetais.getMobileNumber());
			owner.setEmailId(ownerDetais.getEmailId());
			owner.setGuardianRelation(ownerDetais.getGuardianRelation());
			owner.setGuardian(ownerDetais.getGuardian());
			ownerInfo.setOwner(owner);
			proeprtyOwnerInfoList.add(ownerInfo);
		}
		return proeprtyOwnerInfoList;
	}
	
	public List<MasterCodeNamePairDetails> getPropertyCreateDocumentTypes() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<DocumentType> documentTypes = propService.getPropertyCreateDocumentTypes();
		for (DocumentType documentType : documentTypes) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(documentType.getId().toString());
			mstrCodeNamePairDetails.setName(documentType.getName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
	
	public DocumentType getDocumentTypeByCode(String docTypeCode) {
		List<DocumentType> documentTypes = propService.getPropertyCreateDocumentTypes();
		DocumentType documentType = null;
		for(DocumentType docType : documentTypes) {
			if(docType.getId() == Long.valueOf(docTypeCode)) {
				documentType = docType;
			}
		}
		return documentType;
	}
	
	//TODO: Need to uncomment when it is required to check whether aadhaar number or mobile number is exists or not
	/*public ErrorDetails isAadhaarNumberExist(List<OwnerDetails> ownerDetailsList) {
		ErrorDetails errorDetails = null;
		for (OwnerDetails ownerDetails : ownerDetailsList) {
			Query qry = entityManager.createQuery("from User u where u.aadhaarNumber =:aadhaarNumber");
			qry.setParameter("aadhaarNumber", ownerDetails.getAadhaarNo());
			List list = qry.getResultList();
			if (null != list && !list.isEmpty()) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_AADHAAR_NUMBER_EXISTS);
				errorDetails.setErrorMessage(MessageFormat.format(
						PropertyTaxConstants.THIRD_PARTY_ERR_MSG_AADHAAR_NUMBER_EXISTS, ownerDetails.getAadhaarNo()));
			}
		}
		return errorDetails;
	}

	public ErrorDetails isMobileNumberExist(List<OwnerDetails> ownerDetailsList) {
		ErrorDetails errorDetails = null;
		for (OwnerDetails ownerDetails : ownerDetailsList) {
			Query qry = entityManager.createQuery("from User u where u.mobileNumber =:mobileNumber");
			qry.setParameter("mobileNumber", ownerDetails.getMobileNumber());
			List list = qry.getResultList();
			if (null != list && !list.isEmpty()) {
				errorDetails = new ErrorDetails();
				errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_MOBILE_NUMBER_EXISTS);
				errorDetails.setErrorMessage(MessageFormat.format(
						PropertyTaxConstants.THIRD_PARTY_ERR_MSG_MOBILE_NUMBER_EXISTS, ownerDetails.getMobileNumber()));
			}
		}
		return errorDetails;
	}*/
	
	/**
	 * This method is used to get document's list to upload the documents.
	 * 
	 * @param photoAsmntStream
	 *            - photo of assessment input stream object
	 * @param photoAsmntDisp
	 *            - photo of assessment content disposition object
	 * @param bldgPermCopyStream
	 *            - building permission copy input stream object
	 * @param bldgPermCopyDisp
	 *            - building permission copy content disposition object
	 * @param atstdCopyPropDocStream
	 *            - attested copy of property document input stream object
	 * @param atstdCopyPropDocDisp
	 *            - attested copy of property document content disposition
	 *            object
	 * @param nonJudcStampStream
	 *            - non judicial stamp input stream object
	 * @param nonJudcStampDisp
	 *            - non judicial stamp content disposition object
	 * @param afdvtBondStream
	 *            - affidavit bond paper input stream object
	 * @param afdvtBondDisp
	 *            - affidavit bond paper content disposition object
	 * @param deathCertCopyStream
	 *            - death certificate copy input stream object
	 * @param deathCertCopyDisp
	 *            - death certificate copy content disposition object
	 * @return document - list of document
	 */
	public List<Document> getDocuments(InputStream photoAsmntStream, FormDataContentDisposition photoAsmntDisp,
			InputStream bldgPermCopyStream, FormDataContentDisposition bldgPermCopyDisp,
			InputStream atstdCopyPropDocStream, FormDataContentDisposition atstdCopyPropDocDisp,
			InputStream nonJudcStampStream, FormDataContentDisposition nonJudcStampDisp, InputStream afdvtBondStream,
			FormDataContentDisposition afdvtBondDisp, InputStream deathCertCopyStream,
			FormDataContentDisposition deathCertCopyDisp) {
		List<Document> documents = new ArrayList<Document>();
		DocumentType documentType = null;
		Document document = null;
		if (photoAsmntStream != null && photoAsmntDisp != null) {
			documentType = getDocumentTypeByCode(PropertyTaxConstants.THIRD_PARTY_PHOTO_OF_ASSESSMENT_CODE);
			document = createDocument(photoAsmntStream, photoAsmntDisp);
			document.setType(documentType);
			documents.add(document);
		}

		if (bldgPermCopyStream != null && bldgPermCopyDisp != null) {
			documentType = getDocumentTypeByCode(PropertyTaxConstants.THIRD_PARTY_BUILDING_PERMISSION_COPY_CODE);
			document = createDocument(bldgPermCopyStream, bldgPermCopyDisp);
			document.setType(documentType);
			documents.add(document);
		}

		if (atstdCopyPropDocStream != null && atstdCopyPropDocDisp != null) {
			documentType = getDocumentTypeByCode(PropertyTaxConstants.THIRD_PARTY_ATTESTED_COPY_PROPERTY_DOCUMENT_CODE);
			document = createDocument(atstdCopyPropDocStream, atstdCopyPropDocDisp);
			document.setType(documentType);
			documents.add(document);
		}

		if (nonJudcStampStream != null && nonJudcStampDisp != null) {
			documentType = getDocumentTypeByCode(PropertyTaxConstants.THIRD_PARTY_NON_JUDICIAL_STAMP_PAPERS_CODE);
			document = createDocument(nonJudcStampStream, nonJudcStampDisp);
			document.setType(documentType);
			documents.add(document);
		}

		if (afdvtBondStream != null && afdvtBondDisp != null) {
			documentType = getDocumentTypeByCode(
					PropertyTaxConstants.THIRD_PARTY_NOTARIZED_AFFIDAVIT_CUM_IDEMNITY_BOND_CODE);
			document = createDocument(afdvtBondStream, afdvtBondDisp);
			document.setType(documentType);
			documents.add(document);
		}

		if (deathCertCopyStream != null && deathCertCopyDisp != null) {
			documentType = getDocumentTypeByCode(PropertyTaxConstants.THIRD_PARTY_DEATH_CERTIFICATE_COPY_CODE);
			document = createDocument(deathCertCopyStream, deathCertCopyDisp);
			document.setType(documentType);
			documents.add(document);
		}
		return documents;
	}
	
	public List<Document> getDocuments(MultipartFile photoAsmntDisp, MultipartFile bldgPermCopyDisp, MultipartFile atstdCopyPropDocDisp, MultipartFile nonJudcStampDisp, MultipartFile afdvtBondDisp, MultipartFile deathCertCopyDisp) throws IOException {
		List<Document> documents = new ArrayList<Document>();
		DocumentType documentType = null;
		Document document = null;
		
		if (!photoAsmntDisp.isEmpty() && photoAsmntDisp != null) {
			documentType = getDocumentTypeByCode(PropertyTaxConstants.THIRD_PARTY_PHOTO_OF_ASSESSMENT_CODE);
			document = createDocument(photoAsmntDisp.getInputStream(), photoAsmntDisp.getName());
			document.setType(documentType);
			documents.add(document);
		}

		if (!bldgPermCopyDisp.isEmpty() && bldgPermCopyDisp != null) {
			documentType = getDocumentTypeByCode(PropertyTaxConstants.THIRD_PARTY_BUILDING_PERMISSION_COPY_CODE);
			document = createDocument(bldgPermCopyDisp.getInputStream(), bldgPermCopyDisp.getName());
			document.setType(documentType);
			documents.add(document);
		}

		if (!atstdCopyPropDocDisp.isEmpty() && atstdCopyPropDocDisp != null) {
			documentType = getDocumentTypeByCode(PropertyTaxConstants.THIRD_PARTY_ATTESTED_COPY_PROPERTY_DOCUMENT_CODE);
			document = createDocument(atstdCopyPropDocDisp.getInputStream(), atstdCopyPropDocDisp.getName());
			document.setType(documentType);
			documents.add(document);
		}

		if (!nonJudcStampDisp.isEmpty() && nonJudcStampDisp != null) {
			documentType = getDocumentTypeByCode(PropertyTaxConstants.THIRD_PARTY_NON_JUDICIAL_STAMP_PAPERS_CODE);
			document = createDocument(nonJudcStampDisp.getInputStream(), nonJudcStampDisp.getName());
			document.setType(documentType);
			documents.add(document);
		}

		if (!afdvtBondDisp.isEmpty() && afdvtBondDisp != null) {
			documentType = getDocumentTypeByCode(
					PropertyTaxConstants.THIRD_PARTY_NOTARIZED_AFFIDAVIT_CUM_IDEMNITY_BOND_CODE);
			document = createDocument(afdvtBondDisp.getInputStream(), afdvtBondDisp.getName());
			document.setType(documentType);
			documents.add(document);
		}

		if (!deathCertCopyDisp.isEmpty() && deathCertCopyDisp != null) {
			documentType = getDocumentTypeByCode(PropertyTaxConstants.THIRD_PARTY_DEATH_CERTIFICATE_COPY_CODE);
			document = createDocument(deathCertCopyDisp.getInputStream(), deathCertCopyDisp.getName());
			document.setType(documentType);
			documents.add(document);
		}
		return documents;
	}
	
	/**
	 * This method is used to create Document object to upload the files.
	 * 
	 * @param inputStream
	 *            - InputStream object coming as request
	 * @param formDataContentDisposition
	 *            - FormDataContentDisposition object coming as request
	 * @return document - Document object
	 */
	private Document createDocument(InputStream inputStream, FormDataContentDisposition formDataContentDisposition) {
		Document document = new Document();
		List<File> files = new ArrayList<File>();
		List<String> contentTypes = new ArrayList<String>();
		List<String> fileNames = new ArrayList<String>();
		File file = null;
		if (inputStream != null && formDataContentDisposition != null) {
			fileNames.add(formDataContentDisposition.getFileName());
			document.setUploadsFileName(fileNames);
			file = writeToFile(inputStream, formDataContentDisposition.getFileName());
			files.add(file);
			document.setUploads(files);
			contentTypes.add(MessageFormat.format(PropertyTaxConstants.THIRD_PARTY_CONTENT_TYPE,
					FilenameUtils.getExtension(file.getPath())));
			document.setUploadsContentType(contentTypes);
		}
		return document;
	}
	
	private Document createDocument(InputStream inputStream, String fileName) {
		Document document = new Document();
		List<File> files = new ArrayList<File>();
		List<String> contentTypes = new ArrayList<String>();
		List<String> fileNames = new ArrayList<String>();
		File file = null;
		if (inputStream != null && fileName != null) {
			fileNames.add(fileName);
			document.setUploadsFileName(fileNames);
			file = writeToFile(inputStream, fileName);
			files.add(file);
			document.setUploads(files);
			contentTypes.add(MessageFormat.format(PropertyTaxConstants.THIRD_PARTY_CONTENT_TYPE,
					FilenameUtils.getExtension(file.getPath())));
			document.setUploadsContentType(contentTypes);
		}
		return document;
	}
	
	/**
	 * This method is used to convert incoming InputStream object to File object
	 * 
	 * @param uploadedInputStream
	 *            - InputStream object
	 * @param fileName
	 *            - name od the file
	 * @return file - File object
	 */
	private File writeToFile(InputStream uploadedInputStream, String fileName) {
		File file = new File(fileName);
		try {
			OutputStream out = new FileOutputStream(new File(fileName));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	private PropertyImpl transitionWorkFlow(final PropertyImpl property) {
        DateTime currentDate = new DateTime();
        User user = userService.getUserById(EgovThreadLocals.getUserId());
        String approverComments = "Property has been successfully forwarded.";
        String currentState = "Created";
        Assignment assignment = propService.getUserPositionByZone(property.getBasicProperty());
        Position pos = assignment.getPosition();

        WorkFlowMatrix wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null,
                null, PropertyTaxConstants.NEW_ASSESSMENT, currentState, null);
        property.transition().start().withSenderName(user.getName()).withComments(approverComments)
                .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                .withNextAction(wfmatrix.getNextAction());
        return property;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterCodeNamePairDetails> getDocumentTypes() {
		List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
		List<DocumentType> documentTypesList = (List<DocumentType>)entityManager.createQuery("from DocumentType order by id").getResultList();
		for (DocumentType documentType : documentTypesList) {
			MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
			mstrCodeNamePairDetails.setCode(documentType.getId().toString());
			mstrCodeNamePairDetails.setName(documentType.getName());
			mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
		}
		return mstrCodeNamePairDetailsList;
	}
}