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
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;

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
import org.springframework.context.ApplicationContext;
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
    private ApplicationContext beanProvider;
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
    @Qualifier("propertyTaxBillable")
    private PropertyTaxBillable propertyTaxBillable;
    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private BoundaryService boundaryService;
    
    private final List<File> uploads = new ArrayList<File>();
    private final List<String> uploadContentTypes = new ArrayList<String>();
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @Autowired
    private PropertyPersistenceService basicPropertyService;
    private final List<String> uploadFileNames = new ArrayList<String>();

    @Autowired
    private BoundaryTypeService boundaryTypeService;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
    @Autowired
    private UserService userService;

    public AssessmentDetails loadAssessmentDetails(final String propertyId, final Integer flag) {
        assessmentDetail = new AssessmentDetails();
        assessmentDetail.setPropertyID(propertyId);
        assessmentDetail.setFlag(flag);
        validate();
        initiateBasicProperty();
        if (basicProperty != null) {
            property = (PropertyImpl) basicProperty.getProperty();
            if (flag.equals(FLAG_MOBILE_EMAIL))
                loadPrimaryMobileAndEmail();
            if (property != null) {
                final PropertyDetails propertyDetails = new PropertyDetails();
                assessmentDetail.setPropertyDetails(propertyDetails);
                if (flag.equals(FLAG_FULL_DETAILS)) {
                    getAsssessmentDetails();
                    loadPropertyDues();
                }
                if (flag.equals(FLAG_TAX_DETAILS))
                    loadPropertyDues();
            }
        }
        return assessmentDetail;
    }

    private void validate() {
        if (assessmentDetail.getPropertyID() == null || assessmentDetail.getPropertyID().trim().equals(""))
            throw new ApplicationRuntimeException("PropertyID is null or empty!");
        if (assessmentDetail.getFlag() == null || assessmentDetail.getFlag() > 3)
            throw new ApplicationRuntimeException("Invalid Flag");
    }

    private void initiateBasicProperty() {
        basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentDetail.getPropertyID());
        final ErrorDetails errorDetails = new ErrorDetails();
        if (null != basicProperty) {
            // Error Code
            if (!basicProperty.isActive()) {
                errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_CODE);
                errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_MSG);
            } else {
                final Set<PropertyStatusValues> statusValues = basicProperty.getPropertyStatusValuesSet();
                if (null != statusValues && !statusValues.isEmpty())
                    for (final PropertyStatusValues statusValue : statusValues)
                        if (statusValue.getPropertyStatus().getStatusCode() == PropertyTaxConstants.MARK_DEACTIVE) {
                            errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_CODE);
                            errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_MSG);
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
        final PropertyOwnerInfo propOwnerInfo = basicProperty.getPropertyOwnerInfo().get(0);
        assessmentDetail.setPrimaryEmail(propOwnerInfo.getOwner().getEmailId());
        assessmentDetail.setPrimaryMobileNo(propOwnerInfo.getOwner().getMobileNumber());
    }

    private void loadPropertyDues() {
        final Map<String, BigDecimal> resultmap = ptDemandDAO.getDemandCollMap(property);
        if (null != resultmap && !resultmap.isEmpty()) {
            final BigDecimal currDmd = resultmap.get(PropertyTaxConstants.CURR_DMD_STR);
            final BigDecimal arrDmd = resultmap.get(PropertyTaxConstants.ARR_DMD_STR);
            final BigDecimal currCollection = resultmap.get(PropertyTaxConstants.CURR_COLL_STR);
            final BigDecimal arrColelection = resultmap.get(PropertyTaxConstants.ARR_COLL_STR);

            final BigDecimal taxDue = currDmd.add(arrDmd).subtract(currCollection).subtract(arrColelection);
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
            final PropertyDetail propertyDetail = property.getPropertyDetail();
            if (null != propertyDetail) {
                assessmentDetail.getPropertyDetails().setPropertyType(propertyDetail.getPropertyTypeMaster().getType());
                if (propertyDetail.getPropertyUsage() != null)
                    assessmentDetail.getPropertyDetails()
                            .setPropertyUsage(propertyDetail.getPropertyUsage().getUsageName());
                if (null != propertyDetail.getNoofFloors())
                    assessmentDetail.getPropertyDetails().setNoOfFloors(propertyDetail.getNoofFloors());
                else
                    assessmentDetail.getPropertyDetails().setNoOfFloors(0);
            }
        }
    }

    private BoundaryDetails prepareBoundaryInfo(final BasicProperty basicProperty) {
        final BoundaryDetails boundaryDetails = new BoundaryDetails();
        final PropertyID propertyID = basicProperty.getPropertyID();
        if (null != propertyID) {
            if (null != propertyID.getZone()) {
                boundaryDetails.setZoneId(propertyID.getZone().getId());
                boundaryDetails.setZoneNumber(propertyID.getZone().getBoundaryNum());
                boundaryDetails.setZoneName(propertyID.getZone().getName());
                boundaryDetails.setZoneBoundaryType(propertyID.getZone().getBoundaryType().getName());
            }
            if (null != propertyID.getWard()) {
                boundaryDetails.setWardId(propertyID.getWard().getId());
                boundaryDetails.setWardNumber(propertyID.getWard().getBoundaryNum());
                boundaryDetails.setWardName(propertyID.getWard().getName());
                boundaryDetails.setWardBoundaryType(propertyID.getWard().getBoundaryType().getName());
            }
            if (null != propertyID.getArea()) {
                boundaryDetails.setBlockId(propertyID.getArea().getId());
                boundaryDetails.setBlockNumber(propertyID.getArea().getBoundaryNum());
                boundaryDetails.setBlockName(propertyID.getArea().getName());
            }
            if (null != propertyID.getLocality()){
                boundaryDetails.setLocalityId(propertyID.getLocality().getId());
                boundaryDetails.setLocalityName(propertyID.getLocality().getName());
            }
            if (null != propertyID.getStreet()){
                boundaryDetails.setStreetId(propertyID.getStreet().getId());
                boundaryDetails.setStreetName(propertyID.getStreet().getName());
            }
        }
        return boundaryDetails;
    }

    private Set<OwnerName> prepareOwnerInfo(final Property property) {
        final List<PropertyOwnerInfo> propertyOwners = property.getBasicProperty().getPropertyOwnerInfo();
        final Set<OwnerName> ownerNames = new HashSet<OwnerName>();
        if (propertyOwners != null && !propertyOwners.isEmpty())
            for (final PropertyOwnerInfo propertyOwner : propertyOwners) {
                final OwnerName ownerName = new OwnerName();
                ownerName.setAadhaarNumber(propertyOwner.getOwner().getAadhaarNumber());
                ownerName.setOwnerName(propertyOwner.getOwner().getName());
                ownerName.setMobileNumber(propertyOwner.getOwner().getMobileNumber());
                ownerName.setEmailId(propertyOwner.getOwner().getEmailId());
                ownerNames.add(ownerName);
            }
        return ownerNames;
    }

    public PropertyTaxDetails getPropertyTaxDetails(final String assessmentNo) {
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        PropertyTaxDetails propertyTaxDetails = null;
        final ErrorDetails errorDetails = new ErrorDetails();
        if (null != basicProperty)
            propertyTaxDetails = getPropertyTaxDetails(basicProperty);
        else {
            propertyTaxDetails = new PropertyTaxDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_CODE);
            errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_PREFIX + assessmentNo
                    + PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX);
            propertyTaxDetails.setErrorDetails(errorDetails);
        }
        return propertyTaxDetails;
    }

    public List<PropertyTaxDetails> getPropertyTaxDetails(final String circleName, final String zoneName, final String wardName,
            final String blockName, final String ownerName, final String doorNo, final String aadhaarNumber,
            final String mobileNumber) {
        List<PropertyTaxDetails> propTxDetailsList = null;
        final List<BasicProperty> basicPropertyList = basicPropertyDAO.getBasicPropertiesForTaxDetails(circleName, zoneName,
                wardName, blockName, ownerName, doorNo, aadhaarNumber, mobileNumber);
        if (null != basicPropertyList) {
            propTxDetailsList = new ArrayList<PropertyTaxDetails>();
            for (final BasicProperty basicProperty : basicPropertyList) {
                final PropertyTaxDetails propertyTaxDetails = getPropertyTaxDetails(basicProperty);
                propTxDetailsList.add(propertyTaxDetails);
            }
        }
        return propTxDetailsList;
    }

    // TODO: Needs to check whether this method is required or not.
    // Also existing authentication functionality can be used
    public Boolean authenticateUser(final String username, final String password) {
        Boolean isAuthenticated = false;
        if (username.equals("mahesh") && password.equals("demo"))
            isAuthenticated = true;
        return isAuthenticated;
    }

    private Boolean getArrears(final Map<String, BigDecimal> demandCollMap) {
        Boolean hasArrear = false;
        final BigDecimal arrDue = demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR));
        if (null != arrDue && !arrDue.equals(BigDecimal.ZERO))
            hasArrear = true;
        return hasArrear;
    }

    private Map<String, BigDecimal> getDemandYearPenalty(final String applicationNo) {
        final Map<String, BigDecimal> dmndYearPenaltyMap = new HashMap<String, BigDecimal>();
        BigDecimal penalty = BigDecimal.ZERO;
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(applicationNo);
        final PropertyTaxBillable propTaxBill = (PropertyTaxBillable) beanProvider
                .getBean(PropertyTaxConstants.BEANNAME_PROPERTY_TAX_BILLABLE);
        propTaxBill.setBasicProperty(basicProperty);
        propTaxBill.setLevyPenalty(true);
        final Map<Installment, PenaltyAndRebate> penalties = propTaxBill.getCalculatedPenalty();
        final Set<Installment> installments = penalties.keySet();
        for (final Installment installment : installments) {
            final PenaltyAndRebate penaltyAndRebate = penalties.get(installment);
            penalty = penaltyAndRebate.getPenalty();
            dmndYearPenaltyMap.put(installment.getDescription(), penalty);
        }
        return dmndYearPenaltyMap;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    private TaxDetails getCurrentTaxDetails(final String applicationNo) {
        TaxDetails taxDetails = null;
        final Set<String> demandYearSet = ptDemandDAO.getDemandYears(applicationNo);
        Map<String, BigDecimal> demandYearPenaltyMap = null;
        if (null != demandYearSet && !demandYearSet.isEmpty()) {
            demandYearPenaltyMap = getDemandYearPenalty(applicationNo);
            final List<Object> list = ptDemandDAO.getPropertyTaxDetails(applicationNo);
            final Object[] demandYearsArr = demandYearSet.toArray();
            final String currentDemandYear = (String) demandYearsArr[demandYearsArr.length - 1];
            taxDetails = new TaxDetails();
            taxDetails.setDemandYear(currentDemandYear);
            taxDetails.setPenalty(demandYearPenaltyMap.get(currentDemandYear));
            for (final Object record : list) {
                final Object[] data = (Object[]) record;
                final String taxType = (String) data[0];
                final BigInteger actVal = (BigInteger) data[2];
                final BigDecimal taxVal = new BigDecimal(actVal);
                if (currentDemandYear.equalsIgnoreCase((String) data[1]))
                    taxDetails = getTaxDetailsByType(taxDetails, taxType, taxVal);
            }
        }
        return taxDetails;
    }

    private Set<TaxDetails> getArrearsTaxDetails(final String applicationNo) {
        final Set<TaxDetails> arrearsTaxDetails = new LinkedHashSet<TaxDetails>();
        final Set<String> demandYearSet = ptDemandDAO.getDemandYears(applicationNo);
        final Map<String, BigDecimal> demandYearPenaltyMap = getDemandYearPenalty(applicationNo);
        if (null != demandYearSet && !demandYearSet.isEmpty()) {
            final List<Object> list = ptDemandDAO.getPropertyTaxDetails(applicationNo);
            final Object[] demandYearsArr = demandYearSet.toArray();
            for (int i = 0; i < demandYearsArr.length - 1; i++) {
                final String demandYear = (String) demandYearsArr[i];
                TaxDetails taxDetails = new TaxDetails();
                taxDetails.setDemandYear(demandYear);
                taxDetails.setPenalty(demandYearPenaltyMap.get(demandYear));
                for (final Object record : list) {
                    final Object[] data = (Object[]) record;
                    final String taxType = (String) data[0];
                    final BigInteger actVal = (BigInteger) data[2];
                    final BigDecimal taxVal = new BigDecimal(actVal);
                    if (demandYear.equalsIgnoreCase((String) data[1]))
                        taxDetails = getTaxDetailsByType(taxDetails, taxType, taxVal);
                }
                arrearsTaxDetails.add(taxDetails);
            }
        }
        return arrearsTaxDetails;
    }

    private TaxDetails getTaxDetailsByType(final TaxDetails taxDetails, final String taxType, final BigDecimal taxVal) {
        if (taxType.equalsIgnoreCase(PropertyTaxConstants.LIB_CESS))
            taxDetails.setLibCess(taxVal);
        if (taxType.equalsIgnoreCase(PropertyTaxConstants.SEW_TAX))
            taxDetails.setSewarageTax(taxVal);
        if (taxType.equalsIgnoreCase(PropertyTaxConstants.GEN_TAX))
            taxDetails.setGeneralTax(taxVal);
        if (taxType.equalsIgnoreCase(PropertyTaxConstants.EDU_CESS))
            taxDetails.setEduCess(taxVal);
        return taxDetails;
    }

    private PropertyTaxDetails getPropertyTaxDetails(final BasicProperty basicProperty) {
        final PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
        final ErrorDetails errorDetails = new ErrorDetails();
        String guardianName = "";
        final String assessmentNo = basicProperty.getUpicNo();
        if (null != basicProperty) {
            if (!basicProperty.isActive()) {
                errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_CODE);
                errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_MSG);
            } else {
                final Set<PropertyStatusValues> statusValues = basicProperty.getPropertyStatusValuesSet();
                if (null != statusValues && !statusValues.isEmpty())
                    for (final PropertyStatusValues statusValue : statusValues)
                        if (statusValue.getPropertyStatus().getStatusCode() == PropertyTaxConstants.MARK_DEACTIVE) {
                            errorDetails.setErrorCode(PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_CODE);
                            errorDetails.setErrorMessage(PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_MSG);
                        }
            }
            final Property property = basicProperty.getProperty();
            final Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMap(property);

            final List<PropertyOwnerInfo> propOwnerInfos = property.getBasicProperty().getPropertyOwnerInfo();
            final StringBuffer ownerNameStr = new StringBuffer();
            for (int i = 0; i < propOwnerInfos.size(); i++) {
                final PropertyOwnerInfo propOwnerInfo = propOwnerInfos.get(i);
                final String ownerName = propOwnerInfo.getOwner().getName();
                if (null != ownerName && ownerName.trim().length() != 0) {
                    if (ownerNameStr.toString().length() != 0)
                        ownerNameStr.append(", ");
                    ownerNameStr.append(ownerName);
                    guardianName = propOwnerInfo.getOwner().getGuardian();
                }
            }
            final String ownerAddress = PropertyTaxUtil.getOwnerAddress(property.getBasicProperty().getPropertyOwnerInfo());
            propertyTaxDetails.setGuardianName(guardianName);
            propertyTaxDetails.setOwnerName(ownerNameStr.toString());
            propertyTaxDetails.setOwnerAddress(ownerAddress);
            propertyTaxDetails.setHasArrears(getArrears(demandCollMap));
            final TaxDetails currentTaxDetails = getCurrentTaxDetails(assessmentNo);
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
            final Set<ArrearDetails> arrearDetailsSet = new LinkedHashSet<ArrearDetails>();
            if (propertyTaxDetails.getHasArrears()) {
                final Set<TaxDetails> arrearsTaxDetails = getArrearsTaxDetails(assessmentNo);
                if (null != arrearsTaxDetails && !arrearsTaxDetails.isEmpty()) {
                    for (final TaxDetails taxDetails : arrearsTaxDetails) {
                        final ArrearDetails arrearDetails = new ArrearDetails();
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

    public ReceiptDetails payPropertyTax(final String assessmentNo, final String paymentMode, final BigDecimal totalAmount,
            final String paidBy,final String transanctionId) {
        ReceiptDetails receiptDetails = null;
        ErrorDetails errorDetails = null;
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        propertyTaxBillable.setBasicProperty(basicProperty);
        propertyTaxBillable.setUserId(Long.valueOf("16"));
        EgovThreadLocals.setUserId(Long.valueOf("16"));
        propertyTaxBillable.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty
                .getPropertyID().getWard().getBoundaryNum().toString()));
        propertyTaxBillable.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_MANUAL));
        propertyTaxBillable.setLevyPenalty(Boolean.TRUE);
        propertyTaxBillable.setTransanctionReferenceNumber(transanctionId);
        final EgBill egBill = ptBillServiceImpl.generateBill(propertyTaxBillable);

        final CollectionHelper collectionHelper = new CollectionHelper(egBill);
        final Map<String, String> paymentDetailsMap = new HashMap<String, String>();
        paymentDetailsMap.put(PropertyTaxConstants.TOTAL_AMOUNT, totalAmount.toString());
        paymentDetailsMap.put(PropertyTaxConstants.PAID_BY, paidBy);
        final Payment payment = Payment.create(paymentMode, paymentDetailsMap);
        final BillReceiptInfo billReceiptInfo = collectionHelper.executeCollection(payment);

        if (null != billReceiptInfo) {
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
            receiptDetails.setTransactionId(billReceiptInfo.getManualReceiptNumber());
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);

            receiptDetails.setErrorDetails(errorDetails);
        }
        return receiptDetails;
    }

    public ErrorDetails payWaterTax(final String consumerNo, final String paymentMode, final BigDecimal totalAmount,
            final String paidBy) {
        ErrorDetails errorDetails = validatePaymentDetails(consumerNo, paymentMode, totalAmount, paidBy);
        if (null != errorDetails)
            return errorDetails;
        else {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
        }
        return errorDetails;
    }

    public List<MasterCodeNamePairDetails> getPropertyTypeMasterDetails() {
        final List<MasterCodeNamePairDetails> propTypeMasterDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<PropertyTypeMaster> propertyTypeMasters = propertyTypeMasterDAO.findAll();
        for (final PropertyTypeMaster propertyTypeMaster : propertyTypeMasters) {
            final MasterCodeNamePairDetails propTypeMasterDetails = new MasterCodeNamePairDetails();
            propTypeMasterDetails.setCode(propertyTypeMaster.getCode());
            propTypeMasterDetails.setName(propertyTypeMaster.getType());
            propTypeMasterDetailsList.add(propTypeMasterDetails);
        }
        return propTypeMasterDetailsList;
    }

    public PropertyTypeMaster getPropertyTypeMasterByCode(final String propertyTypeMasterCode) {
        final Query qry = entityManager.createQuery("from PropertyTypeMaster ptm where ptm.code = :propertyTypeMasterCode");
        qry.setParameter("propertyTypeMasterCode", propertyTypeMasterCode);
        final PropertyTypeMaster propertyTypeMaster = (PropertyTypeMaster) qry.getSingleResult();
        return propertyTypeMaster;
    }

    public List<MasterCodeNamePairDetails> getPropertyTypeCategoryDetails(final String categoryCode) {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        Map<String, String> codeNameMap = null;
        final PropertyTypeMaster propertyTypeMasters = propertyTypeMasterDAO.getPropertyTypeMasterByCode(categoryCode);
        if (null != propertyTypeMasters) {
            if (propertyTypeMasters.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND))
                codeNameMap = PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
            else
                codeNameMap = PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
            if (null != codeNameMap && !codeNameMap.isEmpty())
                for (final String code : codeNameMap.keySet()) {
                    final MasterCodeNamePairDetails mstrCodeNamepairDetails = new MasterCodeNamePairDetails();
                    mstrCodeNamepairDetails.setCode(code);
                    mstrCodeNamepairDetails.setName(codeNameMap.get(code));
                    mstrCodeNamePairDetailsList.add(mstrCodeNamepairDetails);
                }
        }
        return mstrCodeNamePairDetailsList;
    }

    public List<MasterCodeNamePairDetails> getPropertyTypes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        MasterCodeNamePairDetails mstrCodeNamepairDetails = null;
        final Map<String, String> vacLandMap = PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
        final Map<String, String> nonVacLandMap = PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;

        for (final String key : vacLandMap.keySet()) {
            mstrCodeNamepairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamepairDetails.setCode(key);
            mstrCodeNamepairDetails.setName(vacLandMap.get(key));
            mstrCodeNamePairDetailsList.add(mstrCodeNamepairDetails);
        }

        for (final String code : nonVacLandMap.keySet()) {
            mstrCodeNamepairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamepairDetails.setCode(code);
            mstrCodeNamepairDetails.setName(nonVacLandMap.get(code));
            mstrCodeNamePairDetailsList.add(mstrCodeNamepairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public List<MasterCodeNamePairDetails> getPropertyTypes(final String categoryCode) {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        Map<String, String> codeNameMap = null;
        final PropertyTypeMaster propertyTypeMasters = propertyTypeMasterDAO.getPropertyTypeMasterByCode(categoryCode);
        if (null != propertyTypeMasters) {
            if (propertyTypeMasters.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND))
                codeNameMap = PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
            else
                codeNameMap = PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
            if (null != codeNameMap && !codeNameMap.isEmpty())
                for (final String code : codeNameMap.keySet()) {
                    final MasterCodeNamePairDetails mstrCodeNamepairDetails = new MasterCodeNamePairDetails();
                    mstrCodeNamepairDetails.setCode(code);
                    mstrCodeNamepairDetails.setName(codeNameMap.get(code));
                    mstrCodeNamePairDetailsList.add(mstrCodeNamepairDetails);
                }
        }
        return mstrCodeNamePairDetailsList;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getApartmentsAndComplexes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<Apartment> apartmentList = entityManager.createQuery("from Apartment").getResultList();
        if (null != apartmentList)
            for (final Apartment apartment : apartmentList) {
                final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
                mstrCodeNamePairDetails.setCode(apartment.getCode());
                mstrCodeNamePairDetails.setName(apartment.getName());
                mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
            }
        return mstrCodeNamePairDetailsList;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getReasonsForChangeProperty(final String reason) {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final Query qry = entityManager.createQuery("from PropertyMutationMaster pmm where pmm.type = :type");
        qry.setParameter("type", reason);
        final List<PropertyMutationMaster> propMutationMasterList = qry.getResultList();
        if (null != propMutationMasterList)
            for (final PropertyMutationMaster propMutationMaster : propMutationMasterList) {
                final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
                mstrCodeNamePairDetails.setCode(propMutationMaster.getCode());
                mstrCodeNamePairDetails.setName(propMutationMaster.getMutationName());
                mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
            }
        return mstrCodeNamePairDetailsList;
    }

    public List<MasterCodeNamePairDetails> getLocalities() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<Boundary> localityList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                PropertyTaxConstants.LOCALITY, PropertyTaxConstants.LOCATION_HIERARCHY_TYPE);
        if (null != localityList)
            for (final Boundary boundary : localityList) {
                final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
                mstrCodeNamePairDetails.setCode(boundary.getBoundaryNum().toString().concat("~").concat(boundary.getName()));
                mstrCodeNamePairDetails.setName(boundary.getName());
                mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
            }
        return mstrCodeNamePairDetailsList;
    }

    public LocalityDetails getLocalityDetailsByLocalityCode(final String localityCode) {
        final Long boundaryNo = Long.valueOf(localityCode.substring(0, localityCode.indexOf("~")).trim());
        final String name = localityCode.substring(localityCode.indexOf("~") + 1).trim();
        LocalityDetails localityDetails = null;
        Query qry = entityManager.createQuery("from Boundary b where b.boundaryNum = :boundaryNo and b.name = :name");
        qry.setParameter("boundaryNo", boundaryNo);
        qry.setParameter("name", name);
        List list = qry.getResultList();
        if (null != list && !list.isEmpty()) {
            localityDetails = new LocalityDetails();
            final Boundary boundary = (Boundary) list.get(0);
            qry = entityManager.createQuery("from CrossHierarchy cr where cr.child = :child");
            qry.setParameter("child", boundary);
            list = qry.getResultList();
            if (null != list && !list.isEmpty()) {
                final CrossHierarchy crossHeirarchyImpl = (CrossHierarchy) list.get(0);
                qry = entityManager.createQuery("from Boundary b where b.id = :id");
                qry.setParameter("id", crossHeirarchyImpl.getParent().getId());
                list = qry.getResultList();
                if (null != list && !list.isEmpty()) {
                    final Boundary block = (Boundary) list.get(0);
                    localityDetails.setBlockName(block.getName());
                    qry = entityManager.createQuery("from Boundary b where b.id = :id");
                    qry.setParameter("id", block.getParent().getId());
                    list = qry.getResultList();
                    if (null != list && !list.isEmpty()) {
                        final Boundary ward = (Boundary) list.get(0);
                        localityDetails.setWardName(ward.getName());
                        qry = entityManager.createQuery("from Boundary b where b.id = :id");
                        qry.setParameter("id", ward.getParent().getId());
                        list = qry.getResultList();
                        if (null != list && !list.isEmpty()) {
                            final Boundary zone = (Boundary) list.get(0);
                            localityDetails.setZoneName(zone.getName());
                        }
                    }
                }
            }
        }
        return localityDetails;
    }

    public List<MasterCodeNamePairDetails> getElectionBoundaries() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<Boundary> electionWardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE, PropertyTaxConstants.ELECTION_HIERARCHY_TYPE);
        for (final Boundary boundary : electionWardList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(boundary.getBoundaryNum().toString());
            mstrCodeNamePairDetails.setName(boundary.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public Boundary getElectionBoundaryByCode(final String electionBoundaryCode) {
        final BoundaryType boundaryType = boundaryTypeService
                .getBoundaryTypeByName(PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE);
        final Boundary electionBoundary = boundaryService.getBoundaryByTypeAndNo(boundaryType,
                Long.valueOf(electionBoundaryCode));
        return electionBoundary;
    }

    public List<MasterCodeNamePairDetails> getEnumerationBlocks() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<Boundary> enumerationBlockList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE, PropertyTaxConstants.ELECTION_HIERARCHY_TYPE);
        for (final Boundary boundary : enumerationBlockList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(boundary.getBoundaryNum().toString());
            mstrCodeNamePairDetails.setName(boundary.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getFloorTypes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<FloorType> floorTypeList = entityManager.createQuery("from FloorType order by name").getResultList();
        for (final FloorType floorType : floorTypeList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(floorType.getCode());
            mstrCodeNamePairDetails.setName(floorType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public FloorType getFloorTypeByCode(final String code) {
        final Query qry = entityManager.createQuery("from FloorType ft where ft.code = :code");
        qry.setParameter("code", code);
        final FloorType floorType = (FloorType) qry.getSingleResult();
        return floorType;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getRoofTypes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<RoofType> roofTypeList = entityManager.createQuery("from RoofType order by name").getResultList();
        for (final RoofType roofType : roofTypeList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(roofType.getCode());
            mstrCodeNamePairDetails.setName(roofType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public RoofType getRoofTypeByCode(final String code) {
        final Query qry = entityManager.createQuery("from RoofType rt where rt.code = :code");
        qry.setParameter("code", code);
        final RoofType roofType = (RoofType) qry.getSingleResult();
        return roofType;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getWallTypes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<WallType> wallTypeList = entityManager.createQuery("from WallType order by name").getResultList();
        for (final WallType wallType : wallTypeList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(wallType.getCode());
            mstrCodeNamePairDetails.setName(wallType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public WallType getWallTypeByCode(final String code) {
        final Query qry = entityManager.createQuery("from WallType wt where wt.code = :code");
        qry.setParameter("code", code);
        final WallType wallType = (WallType) qry.getSingleResult();
        return wallType;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getWoodTypes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<WoodType> woodTypeList = entityManager.createQuery("from WoodType order by name").getResultList();
        for (final WoodType woodType : woodTypeList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(woodType.getCode());
            mstrCodeNamePairDetails.setName(woodType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public WoodType getWoodTypeByCode(final String code) {
        final Query qry = entityManager.createQuery("from WoodType wt where wt.code = :code");
        qry.setParameter("code", code);
        final WoodType woodType = (WoodType) qry.getSingleResult();
        return woodType;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getBuildingClassifications() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<StructureClassification> structClsfList = entityManager.createQuery("from StructureClassification")
                .getResultList();
        for (final StructureClassification structClsf : structClsfList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(structClsf.getConstrTypeCode());
            mstrCodeNamePairDetails.setName(structClsf.getTypeName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public StructureClassification getStructureClassificationByCode(final String classificationCode) {
        final Query qry = entityManager.createQuery("from StructureClassification sc where sc.constrTypeCode =:code");
        qry.setParameter("code", classificationCode);
        return (StructureClassification) qry.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getNatureOfUsages() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<PropertyUsage> usageList = entityManager.createQuery("from PropertyUsage order by usageName").getResultList();
        for (final PropertyUsage propUsage : usageList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(propUsage.getUsageCode());
            mstrCodeNamePairDetails.setName(propUsage.getUsageName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public PropertyUsage getPropertyUsageByUsageCde(final String usageCode) {
        final Query qry = entityManager.createQuery("from PropertyUsage pu where pu.usageCode = :usageCode order by usageName");
        qry.setParameter("usageCode", usageCode);
        final PropertyUsage propertyUsage = (PropertyUsage) qry.getSingleResult();
        return propertyUsage;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getOccupancies() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<PropertyOccupation> propOccupList = entityManager.createQuery("from PropertyOccupation").getResultList();
        for (final PropertyOccupation propOccup : propOccupList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(propOccup.getOccupancyCode());
            mstrCodeNamePairDetails.setName(propOccup.getOccupation());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public PropertyOccupation getPropertyOccupationByOccupancyCode(final String occupancyCode) {
        final Query qry = entityManager.createQuery("from PropertyOccupation po where po.occupancyCode = :occupancyCode");
        qry.setParameter("occupancyCode", occupancyCode);
        final PropertyOccupation propertyOccupation = (PropertyOccupation) qry.getSingleResult();
        return propertyOccupation;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getExemptionCategories() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<TaxExeptionReason> taxExeptionReasonList = entityManager.createQuery("from TaxExeptionReason order by name")
                .getResultList();
        for (final TaxExeptionReason taxExeptionReason : taxExeptionReasonList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(taxExeptionReason.getCode());
            mstrCodeNamePairDetails.setName(taxExeptionReason.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public TaxExeptionReason getTaxExemptionReasonByCode(final String exemptionReasonCode) {
        TaxExeptionReason taxExeptionReason = null;
        final Query qry = entityManager.createQuery("from TaxExeptionReason ter where ter.code = :code");
        qry.setParameter("code", "" + exemptionReasonCode);
        final List list = qry.getResultList();
        if (null != list && !list.isEmpty())
            taxExeptionReason = (TaxExeptionReason) qry.getSingleResult();
        return taxExeptionReason;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getApproverDepartments() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<Department> approverDepartmentList = entityManager.createQuery("from Department order by name")
                .getResultList();
        for (final Department approverDepartment : approverDepartmentList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(approverDepartment.getCode());
            mstrCodeNamePairDetails.setName(approverDepartment.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

	public NewPropertyDetails createNewProperty(final String propertyTypeMasterCode, final String propertyCategoryCode,
			final String apartmentCmplxCode, final List<OwnerDetails> ownerDetailsList, final String mutationReasonCode,
			final String extentOfSite, final Boolean isExtentAppurtenantLand, final String occupancyCertificationNo,
			final Boolean isSuperStructure, final String siteOwnerName, final Boolean isBuildingPlanDetails, 
			final String buildingPermissionNo, final String buildingPermissionDate,
			final String percentageDeviation, final String regdDocNo, final String regdDocDate, 
			final String localityCode, final String street,
			final String electionWardCode, final String doorNo, final String enumerationBlockCode, final String pinCode,
			final Boolean isCorrAddrDiff, final String corrAddr1, final String corrAddr2, final String corrPinCode,
			final Boolean hasLift, final Boolean hasToilet, final Boolean hasWaterTap, final Boolean hasElectricity,
			final Boolean hasAttachedBathroom, final Boolean hasWaterHarvesting, final Boolean hasCable, final String floorTypeCode,
			final String roofTypeCode, final String wallTypeCode, final String woodTypeCode,
			final List<FloorDetails> floorDetailsList, final String surveyNumber, final String pattaNumber,
			final Float vacantLandArea, final Double marketValue, final Double currentCapitalValue, final String effectiveDate,
			final String completionDate, final String northBoundary, final String southBoundary,
			final String eastBoundary, final String westBoundary, final List<Document> documents)
            throws ParseException {

        NewPropertyDetails newPropertyDetails = null;
        BasicProperty basicProperty = createBasicProperty(propertyTypeMasterCode, propertyCategoryCode, apartmentCmplxCode, 
        		mutationReasonCode, ownerDetailsList, extentOfSite, isExtentAppurtenantLand, occupancyCertificationNo, 
        		isSuperStructure, siteOwnerName, isBuildingPlanDetails, buildingPermissionNo, buildingPermissionDate,
    			percentageDeviation, regdDocNo, regdDocDate, localityCode, street, doorNo, electionWardCode, pinCode, 
    			isCorrAddrDiff, corrAddr1, corrAddr2, corrPinCode, hasLift, hasToilet, hasWaterTap, hasElectricity, 
    			hasAttachedBathroom, hasWaterHarvesting, hasCable, floorTypeCode, roofTypeCode, wallTypeCode, 
    			woodTypeCode, floorDetailsList, surveyNumber, pattaNumber, vacantLandArea, marketValue, 
    			currentCapitalValue, effectiveDate, completionDate, northBoundary, southBoundary, eastBoundary,
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
            final ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
            newPropertyDetails.setErrorDetails(errorDetails);
        }
        return newPropertyDetails;
    }

	private BasicProperty createBasicProperty(final String propertyTypeMasterCode, final String propertyCategoryCode,
			final String apartmentCmplxCode, final String mutationReasonCode, final List<OwnerDetails> ownerDetailsList,
			final String extentOfSite, final Boolean isExtentAppurtenantLand, final String occupancyCertificationNo,
			final Boolean isSuperStructure, final String siteOwnerName, final Boolean isBuildingPlanDetails, 
			final String buildingPermissionNo, final String buildingPermissionDate, final String percentageDeviation, 
			final String regdDocNo, final String regdDocDate, final String localityCode, final String street,
			final String doorNo, final String electionWardCode, final String pinCode, final Boolean isCorrAddrDiff,
			final String corrAddr1, final String corrAddr2, final String corrPinCode,
			final Boolean lift, final Boolean toilet, final Boolean waterTap, final Boolean electricity,
			final Boolean attachedBathroom, final Boolean waterHarvesting, final Boolean cable,
			final String floorTypeCode, final String roofTypeCode, final String wallTypeCode, final String woodTypeCode,
			final List<FloorDetails> floorDetailsList, final String surveyNumber, final String pattaNumber,
			final Float vacantLandArea, final Double marketValue, final Double currentCapitalValue, final String effectiveDate,
			final String completionDate, final String northBoundary, final String southBoundary, 
			final String eastBoundary, final String westBoundary,
			final List<Document> documents) throws ParseException {
        final BasicProperty basicProperty = new BasicPropertyImpl();
        basicProperty.setRegdDocNo(regdDocNo);
        basicProperty.setRegdDocDate(convertStringToDate(regdDocDate));
        basicProperty.setActive(Boolean.TRUE);
        basicProperty.setSource(PropertyTaxConstants.SOURCEOFDATA_MEESEWA);
        // Creating Property Address object
        final String addressString = "";
        final PropertyAddress propAddress = createPropAddress(localityCode, street, doorNo, addressString, pinCode,
                isCorrAddrDiff, doorNo, corrAddr1, corrAddr2, corrPinCode);
        basicProperty.setAddress(propAddress);
        
        // Creating PropertyID object based on basic property, localityCode and
        // boundary map direction
        final PropertyID propertyID = createPropertID(basicProperty, localityCode, electionWardCode, northBoundary,
                southBoundary, eastBoundary, westBoundary);
        basicProperty.setPropertyID(propertyID);

        // Get PropertyStatus object to set the status of the property
        final PropertyStatus propertyStatus = getPropertyStatus();
        basicProperty.setStatus(propertyStatus);

        // Set underworkflow status as true
        basicProperty.setUnderWorkflow(Boolean.TRUE);

        // Set PropertyMutationMaster object
        final PropertyMutationMaster propertyMutationMaster = getPropertyMutationMaster(mutationReasonCode);
        basicProperty.setPropertyMutationMaster(propertyMutationMaster);
        PropertyService propService = beanProvider.getBean("propService", PropertyService.class);
        basicProperty.addPropertyStatusValues(propService.createPropStatVal(basicProperty, PROP_CREATE_RSN, null, null,
                null, null, null));
        // Set isBillCreated property value as false
        basicProperty.setIsBillCreated(PropertyTaxConstants.STATUS_BILL_NOTCREATED);

        final PropertyTypeMaster propertyTypeMaster = getPropertyTypeMasterByCode(propertyTypeMasterCode);
        final PropertyUsage propertyUsage = getPropertyUsageByUsageCde(floorDetailsList.get(0).getNatureOfUsageCode());
        final PropertyOccupation propertyOccupation = getPropertyOccupationByOccupancyCode(
                floorDetailsList.get(0).getOccupancyCode());
        final FloorType floorType = getFloorTypeByCode(floorTypeCode);
        final RoofType roofType = getRoofTypeByCode(roofTypeCode);
        final WallType wallType = getWallTypeByCode(wallTypeCode);
        final WoodType woodType = getWoodTypeByCode(woodTypeCode);
        // TODO: Need to check whether this field is really required
        final String nonResPlotArea = null;
        final PropertyImpl propertyImpl = createPropertyWithBasicDetails();
        propertyImpl.setBasicProperty(basicProperty);
        propertyImpl.getPropertyDetail().setFloorDetailsProxy(getFloorList(floorDetailsList));
        propertyImpl.getBasicProperty().setPropertyID(propertyID);
        if(documents != null) {
        	propertyImpl.setDocuments(documents);
        }
        final TaxExeptionReason taxExemptedReason = getTaxExemptionReasonByCode(floorDetailsList.get(0)
                .getExemptionCategoryCode());
        propertyImpl.setTaxExemptedReason(taxExemptedReason);
        Apartment apartment = getApartmentByCode(apartmentCmplxCode);
        propertyImpl.getPropertyDetail().setApartment(apartment);
        propertyImpl.getPropertyDetail().setOccupancyCertificationNo(occupancyCertificationNo);
        
        propertyImpl.getPropertyDetail().setStructure(isSuperStructure);
        if(isSuperStructure) {
        	propertyImpl.getPropertyDetail().setSiteOwner(siteOwnerName);
        }
        
        propertyImpl.getPropertyDetail().setBuildingPlanDetailsChecked(isBuildingPlanDetails);
        if(isBuildingPlanDetails) {
            propertyImpl.getPropertyDetail().setBuildingPermissionNo(buildingPermissionNo);
            propertyImpl.getPropertyDetail().setBuildingPermissionDate(convertStringToDate(buildingPermissionDate));
            propertyImpl.getPropertyDetail().setDeviationPercentage(percentageDeviation);
        }
        
        propertyImpl.getPropertyDetail().setLift(lift);
        propertyImpl.getPropertyDetail().setToilets(toilet);
        propertyImpl.getPropertyDetail().setWaterTap(waterTap);
        propertyImpl.getPropertyDetail().setElectricity(electricity);
        propertyImpl.getPropertyDetail().setAttachedBathRoom(attachedBathroom);
        propertyImpl.getPropertyDetail().setWaterHarvesting(waterHarvesting);
        propertyImpl.getPropertyDetail().setCable(cable);
        
        propertyImpl.getPropertyDetail().setCorrAddressDiff(isCorrAddrDiff);
        propertyImpl.getPropertyDetail().setAppurtenantLandChecked(isExtentAppurtenantLand);
        if(isExtentAppurtenantLand) {
        	propertyImpl.getPropertyDetail().setCurrentCapitalValue(currentCapitalValue);
        	propertyImpl.getPropertyDetail().setSurveyNumber(surveyNumber);
        	propertyImpl.getPropertyDetail().setPattaNumber(pattaNumber);
        	Area area = new Area();
        	area.setArea(vacantLandArea);
        	propertyImpl.getPropertyDetail().setCommVacantLand(area);
        	propertyImpl.getPropertyDetail().setMarketValue(marketValue);
        	propertyImpl.getPropertyDetail().setEffectiveDate(convertStringToDate(effectiveDate));
        }
        
        property = propService.createProperty(propertyImpl, extentOfSite, mutationReasonCode,
                propertyTypeMaster.getId().toString(), propertyUsage.getId().toString(),
                propertyOccupation.getId().toString(), PropertyTaxConstants.STATUS_ISACTIVE, regdDocNo, nonResPlotArea,
                floorType.getId(), roofType.getId(), wallType.getId(), woodType.getId(),
                taxExemptedReason != null ? taxExemptedReason.getId() : null);
        property.setStatus(PropertyTaxConstants.STATUS_ISACTIVE);
        property.setPropertyModifyReason(PROP_CREATE_RSN);
        property.getPropertyDetail().setCategoryType(propertyCategoryCode);
        basicProperty.addProperty(property);

        Date propCompletionDate = null;
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService
                    .getLowestDtOfCompFloorWise(property.getPropertyDetail().getFloorDetailsProxy());
        else
            propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
        basicProperty.setPropOccupationDate(propCompletionDate);

        if (property != null && !property.getDocuments().isEmpty())
            propService.processAndStoreDocument(property.getDocuments());
        propService.createDemand(propertyImpl, propCompletionDate);
        return basicProperty;
    }

    private PropertyStatus getPropertyStatus() {
        final Query qry = entityManager.createQuery("from PropertyStatus where statusCode = :statusCode");
        qry.setParameter("statusCode", PropertyTaxConstants.PROPERTY_STATUS_WORKFLOW);
        final PropertyStatus propertyStatus = (PropertyStatus) qry.getSingleResult();
        return propertyStatus;
    }

    private PropertyAddress createPropAddress(final String localityCode, final String street, final String doorNo,
            final String addressString, final String pinCode, final Boolean isCorrespondenceAddrDiff, final String coorDoorNo,
            final String corrAddr1, final String corrAddr2, final String corrPinCode) {
        final Address propAddr = new PropertyAddress();
        propAddr.setHouseNoBldgApt(doorNo);
        if (null != street && !street.isEmpty())
            propAddr.setStreetRoadLine(street);
        final LocalityDetails localityDetails = getLocalityDetailsByLocalityCode(localityCode);
        if (null != localityDetails)
            propAddr.setAreaLocalitySector(localityDetails.getBlockName());

        if (pinCode != null && !pinCode.isEmpty())
            propAddr.setPinCode(pinCode);
        Address ownerAddress = null;
        if (isCorrespondenceAddrDiff) {
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

    private Address createCorrespondenceAddress(final String corrDoorNo, final String corrAddr1, final String corrAddr2,
            final String corrPinCode) {
        final Address ownerCorrAddr = new CorrespondenceAddress();
        ownerCorrAddr.setHouseNoBldgApt(corrDoorNo);
        ownerCorrAddr.setAreaLocalitySector(corrAddr1);
        ownerCorrAddr.setStreetRoadLine(corrAddr2);
        ownerCorrAddr.setPinCode(corrPinCode);
        return ownerCorrAddr;
    }

    private PropertyID createPropertID(final BasicProperty basicProperty, final String localityCode,
            final String electionBoundaryCode,
            final String northBoundary, final String southBoundary, final String eastBoundary, final String westBoundary) {
        final PropertyID propertyID = new PropertyID();

        final Long boundaryNo = Long.valueOf(localityCode.substring(0, localityCode.indexOf("~")).trim());
        final String boundaryName = localityCode.substring(localityCode.indexOf("~") + 1).trim();

        final Boundary block = getBoundaryAsBlock(boundaryNo, boundaryName);
        final Boundary ward = getBoundaryAsWard(block);
        final Boundary zone = getBoundaryAsZone(ward);
        final Boundary locality = getBoundaryAsLocality(boundaryNo, boundaryName);
        basicProperty.setBoundary(boundaryService.getBoundaryById(ward.getId()));
        propertyID.setArea(block);
        propertyID.setWard(ward);
        propertyID.setZone(zone);
        propertyID.setLocality(locality);
        /*
         * propertyID.setCreatedDate(new Date()); propertyID.setModifiedDate(new Date());
         */
        propertyID.setLocality(locality);
        propertyID.setBasicProperty(basicProperty);
        final Boundary electionBoundary = getElectionBoundaryByCode(electionBoundaryCode);
        if (null != electionBoundary)
            propertyID.setElectionBoundary(electionBoundary);
        if (null != northBoundary && northBoundary.trim().length() != 0)
            propertyID.setNorthBoundary(northBoundary);
        if (null != southBoundary && southBoundary.trim().length() != 0)
            propertyID.setSouthBoundary(southBoundary);
        if (null != eastBoundary && eastBoundary.trim().length() != 0)
            propertyID.setEastBoundary(eastBoundary);
        if (null != westBoundary && westBoundary.trim().length() != 0)
            propertyID.setWestBoundary(westBoundary);
        propertyID.setBasicProperty(basicProperty);
        return propertyID;
    }

    private Boundary getBoundaryAsBlock(final Long boundaryNo, final String boundaryName) {
        Query qry = entityManager.createQuery("from Boundary b where b.boundaryNum = :boundaryNo and b.name = :name");
        qry.setParameter("boundaryNo", boundaryNo);
        qry.setParameter("name", boundaryName);
        final Boundary boundary = (Boundary) qry.getSingleResult();
        qry = entityManager.createQuery("from CrossHierarchy cr where cr.child = :child");
        qry.setParameter("child", boundary);
        final CrossHierarchy crossHeirarchyImpl = (CrossHierarchy) qry.getSingleResult();
        qry = entityManager.createQuery("from Boundary b where b.id = :id");
        qry.setParameter("id", crossHeirarchyImpl.getParent().getId());
        final Boundary block = (Boundary) qry.getSingleResult();
        return block;
    }

    private Boundary getBoundaryAsWard(final Boundary block) {
        final Query qry = entityManager.createQuery("from Boundary b where b.id = :id");
        qry.setParameter("id", block.getParent().getId());
        final Boundary ward = (Boundary) qry.getSingleResult();
        return ward;
    }

    private Boundary getBoundaryAsZone(final Boundary ward) {
        final Query qry = entityManager.createQuery("from Boundary b where b.id = :id");
        qry.setParameter("id", ward.getParent().getId());
        final Boundary zone = (Boundary) qry.getSingleResult();
        return zone;
    }

    private Boundary getBoundaryAsLocality(final Long boundaryNo, final String boundaryName) {
        final Query qry = entityManager
                .createQuery("from Boundary b where b.boundaryNum = :boundaryNum and b.name = :boundaryName");
        qry.setParameter("boundaryNum", boundaryNo);
        qry.setParameter("boundaryName", boundaryName);
        final Boundary locality = (Boundary) qry.getSingleResult();
        return locality;
    }

    private PropertyMutationMaster getPropertyMutationMaster(final String mutationReasonCode) {
        final Query qry = entityManager.createQuery("from PropertyMutationMaster pmm where pmm.code = :mutationReasonCode");
        qry.setParameter("mutationReasonCode", mutationReasonCode);
        final PropertyMutationMaster propMutationMaster = (PropertyMutationMaster) qry.getSingleResult();
        return propMutationMaster;
    }

    /**
     * This method is used to validate the payment details to do the payments.
     * 
     * @param assessmentNo - assessment number or property number
     * @param paymentMode - mode of payment
     * @param totalAmount - total amount
     * @param paidBy - name of the payer
     * @return
     */
    public ErrorDetails validatePaymentDetails(final String assessmentNo, final String paymentMode, final BigDecimal totalAmount,
            final String paidBy) {
        ErrorDetails errorDetails = null;
        if (assessmentNo == null || assessmentNo.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_REQUIRED);
        } else {
            if (assessmentNo.trim().length() > 0 && assessmentNo.trim().length() < 10) {
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

        if (paymentMode == null || paymentMode.trim().length() == 0) {
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

    private String formatDate(final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    private Date convertStringToDate(final String dateInString) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        final Date stringToDate = sdf.parse(dateInString);
        return stringToDate;
    }

    private void processAndStoreDocumentsWithReason(final BasicProperty basicProperty, final String reason) {
        if (!uploads.isEmpty()) {
            int fileCount = 0;
            for (final File file : uploads) {
                final FileStoreMapper fileStore = fileStoreService.store(file, uploadFileNames.get(fileCount),
                        uploadContentTypes.get(fileCount++), FILESTORE_MODULE_NAME);
                final PropertyDocs propertyDoc = new PropertyDocs();
                propertyDoc.setSupportDoc(fileStore);
                propertyDoc.setBasicProperty(basicProperty);
                propertyDoc.setReason(reason);
                basicProperty.addDocs(propertyDoc);
            }
        }
    }

    private PropertyImpl createPropertyWithBasicDetails() {
        final PropertyImpl propertyImpl = new PropertyImpl();
        propertyImpl.setPropertyDetail(new BuiltUpProperty());
        propertyImpl.setBasicProperty(new BasicPropertyImpl());
        return propertyImpl;
    }

    private List<Floor> getFloorList(final List<FloorDetails> floorDetailsList) throws ParseException {
        final List<Floor> floorList = new ArrayList<Floor>();
        for (final FloorDetails floorDetials : floorDetailsList) {
            final Floor floor = new Floor();
            floor.setFloorNo(Integer.valueOf(floorDetials.getFloorNoCode()));
            floor.setStructureClassification(
                    getStructureClassificationByCode(floorDetials.getBuildClassificationCode()));
            floor.setPropertyUsage(getPropertyUsageByUsageCde(floorDetials.getNatureOfUsageCode()));
            floor.setPropertyOccupation(getPropertyOccupationByOccupancyCode(floorDetials.getOccupancyCode()));
            floor.setOccupantName(floorDetials.getOccupantName());
            floor.setOccupancyDate(convertStringToDate(floorDetials.getConstructionDate()));
            floor.setCreatedDate(convertStringToDate(floorDetials.getConstructionDate()));
            final Area builtUpArea = new Area();
            builtUpArea.setArea(floorDetials.getPlinthArea());
            floor.setBuiltUpArea(builtUpArea);
            floor.setDrainage(floorDetials.getDrainageCode());
            floor.setNoOfSeats(floorDetials.getNoOfSeats());
            floorList.add(floor);
        }
        return floorList;
    }

    private List<PropertyOwnerInfo> getPropertyOwnerInfoList(final List<OwnerDetails> ownerDetailsList) {
        final List<PropertyOwnerInfo> proeprtyOwnerInfoList = new ArrayList<PropertyOwnerInfo>();
        for (final OwnerDetails ownerDetais : ownerDetailsList) {
            final PropertyOwnerInfo ownerInfo = new PropertyOwnerInfo();
            final User owner = new User();
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
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        PropertyService propService = beanProvider.getBean("propService", PropertyService.class);
        final List<DocumentType> documentTypes = propService.getPropertyCreateDocumentTypes();
        for (final DocumentType documentType : documentTypes) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(documentType.getId().toString());
            mstrCodeNamePairDetails.setName(documentType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public DocumentType getDocumentTypeByCode(final String docTypeCode) {
        PropertyService propService = beanProvider.getBean("propService", PropertyService.class);
        final List<DocumentType> documentTypes = propService.getPropertyCreateDocumentTypes();
        DocumentType documentType = null;
        for (final DocumentType docType : documentTypes)
            if (docType.getId() == Long.valueOf(docTypeCode))
                documentType = docType;
        return documentType;
    }

    // TODO: Need to uncomment when it is required to check whether aadhaar number or mobile number is exists or not
    /*
     * public ErrorDetails isAadhaarNumberExist(List<OwnerDetails> ownerDetailsList) { ErrorDetails errorDetails = null; for
     * (OwnerDetails ownerDetails : ownerDetailsList) { Query qry =
     * entityManager.createQuery("from User u where u.aadhaarNumber =:aadhaarNumber"); qry.setParameter("aadhaarNumber",
     * ownerDetails.getAadhaarNo()); List list = qry.getResultList(); if (null != list && !list.isEmpty()) { errorDetails = new
     * ErrorDetails(); errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_AADHAAR_NUMBER_EXISTS);
     * errorDetails.setErrorMessage(MessageFormat.format( PropertyTaxConstants.THIRD_PARTY_ERR_MSG_AADHAAR_NUMBER_EXISTS,
     * ownerDetails.getAadhaarNo())); } } return errorDetails; } public ErrorDetails isMobileNumberExist(List<OwnerDetails>
     * ownerDetailsList) { ErrorDetails errorDetails = null; for (OwnerDetails ownerDetails : ownerDetailsList) { Query qry =
     * entityManager.createQuery("from User u where u.mobileNumber =:mobileNumber"); qry.setParameter("mobileNumber",
     * ownerDetails.getMobileNumber()); List list = qry.getResultList(); if (null != list && !list.isEmpty()) { errorDetails = new
     * ErrorDetails(); errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_MOBILE_NUMBER_EXISTS);
     * errorDetails.setErrorMessage(MessageFormat.format( PropertyTaxConstants.THIRD_PARTY_ERR_MSG_MOBILE_NUMBER_EXISTS,
     * ownerDetails.getMobileNumber())); } } return errorDetails; }
     */

    /**
     * This method is used to get document's list to upload the documents.
     * 
     * @param photoAsmntStream - photo of assessment input stream object
     * @param photoAsmntDisp - photo of assessment content disposition object
     * @param bldgPermCopyStream - building permission copy input stream object
     * @param bldgPermCopyDisp - building permission copy content disposition object
     * @param atstdCopyPropDocStream - attested copy of property document input stream object
     * @param atstdCopyPropDocDisp - attested copy of property document content disposition object
     * @param nonJudcStampStream - non judicial stamp input stream object
     * @param nonJudcStampDisp - non judicial stamp content disposition object
     * @param afdvtBondStream - affidavit bond paper input stream object
     * @param afdvtBondDisp - affidavit bond paper content disposition object
     * @param deathCertCopyStream - death certificate copy input stream object
     * @param deathCertCopyDisp - death certificate copy content disposition object
     * @return document - list of document
     */
    public List<Document> getDocuments(final InputStream photoAsmntStream, final FormDataContentDisposition photoAsmntDisp,
            final InputStream bldgPermCopyStream, final FormDataContentDisposition bldgPermCopyDisp,
            final InputStream atstdCopyPropDocStream, final FormDataContentDisposition atstdCopyPropDocDisp,
            final InputStream nonJudcStampStream, final FormDataContentDisposition nonJudcStampDisp,
            final InputStream afdvtBondStream,
            final FormDataContentDisposition afdvtBondDisp, final InputStream deathCertCopyStream,
            final FormDataContentDisposition deathCertCopyDisp) {
        final List<Document> documents = new ArrayList<Document>();
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

    public List<Document> getDocuments(final MultipartFile photoAsmntDisp, final MultipartFile bldgPermCopyDisp,
            final MultipartFile atstdCopyPropDocDisp, final MultipartFile nonJudcStampDisp, final MultipartFile afdvtBondDisp,
            final MultipartFile deathCertCopyDisp) throws IOException {
        final List<Document> documents = new ArrayList<Document>();
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
     * @param inputStream - InputStream object coming as request
     * @param formDataContentDisposition - FormDataContentDisposition object coming as request
     * @return document - Document object
     */
    private Document createDocument(final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition) {
        final Document document = new Document();
        final List<File> files = new ArrayList<File>();
        final List<String> contentTypes = new ArrayList<String>();
        final List<String> fileNames = new ArrayList<String>();
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

    private Document createDocument(final InputStream inputStream, final String fileName) {
        final Document document = new Document();
        final List<File> files = new ArrayList<File>();
        final List<String> contentTypes = new ArrayList<String>();
        final List<String> fileNames = new ArrayList<String>();
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
     * @param uploadedInputStream - InputStream object
     * @param fileName - name od the file
     * @return file - File object
     */
    private File writeToFile(final InputStream uploadedInputStream, final String fileName) {
        final File file = new File(fileName);
        try {
            final OutputStream out = new FileOutputStream(new File(fileName));
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1)
                out.write(bytes, 0, read);
            out.flush();
            out.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private PropertyImpl transitionWorkFlow(final PropertyImpl property) {
        final DateTime currentDate = new DateTime();
        final User user = userService.getUserById(EgovThreadLocals.getUserId());
        final String approverComments = "Property has been successfully forwarded.";
        final String currentState = "Created";
        PropertyService propService = beanProvider.getBean("propService", PropertyService.class);
        final Assignment assignment = propService.getUserPositionByZone(property.getBasicProperty());
        final Position pos = assignment.getPosition();

        final WorkFlowMatrix wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null,
                null, PropertyTaxConstants.NEW_ASSESSMENT, currentState, null);
        property.transition().start().withSenderName(user.getName()).withComments(approverComments)
        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
        .withNextAction(wfmatrix.getNextAction());
        return property;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getDocumentTypes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<DocumentType> documentTypesList = entityManager.createQuery("from DocumentType order by id").getResultList();
        for (final DocumentType documentType : documentTypesList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(documentType.getId().toString());
            mstrCodeNamePairDetails.setName(documentType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }
    
    private Apartment getApartmentByCode(String apartmentCode) {
    	Query qry = entityManager.createQuery("from Apartment ap where ap.code = :code");
    	qry.setParameter("code", apartmentCode);
    	Apartment apartment = (Apartment)qry.getSingleResult();
    	return apartment;
    }
}