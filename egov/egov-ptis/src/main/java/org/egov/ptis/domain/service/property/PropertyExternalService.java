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
package org.egov.ptis.domain.service.property;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_ALTER_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.APPROVAL_COMMENTS_SUCCESS;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_AUTO;
import static org.egov.ptis.constants.PropertyTaxConstants.BLOCK;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_TYPE_PROPERTY_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_TYPE_VACANTLAND_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.CREATE_CURRENT_STATE_BILL_COLLECTOR_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_CREATE_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.MARK_DEACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.MODIFY_CURRENT_STATE_BILL_COLLECTOR_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_ALTERATION;
import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.NOT_AVAILABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PAID_BY;
import static org.egov.ptis.constants.PropertyTaxConstants.PAYMENT_TYPE_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.PAYMENT_TYPE_FULLY;
import static org.egov.ptis.constants.PropertyTaxConstants.PAYMENT_TYPE_PARTIALLY;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_ACTIVE_ERR_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_ACTIVE_NOT_EXISTS;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_DEACTIVATE_ERR_MSG;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_EXEMPTED_ERR_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_EXEMPTED_ERR_MSG;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_INACTIVE_ERR_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_INACTIVE_ERR_MSG;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MARK_DEACTIVATE_ERR_MSG;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODE_CREATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODE_MODIFY;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_ADD_OR_ALTER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_PREFIX;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN_BIFUR;
import static org.egov.ptis.constants.PropertyTaxConstants.PTIS_COLLECTION_SERVICE_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.SERVICE_CODE_VACANTLANDTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCEOFDATA_MOBILE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_BILL_NOTCREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_DEMAND_INACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_YES_XML_MIGRATION;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_LEN;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_REQUIRED;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_INVALID;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_REQUIRED;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTY_TAX_ASSESSMENT_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_VACANTLAND_ASSESSMENT_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_CODE_WRONG_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_LEN;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_REQUIRED;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_INVALID;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_REQUIRED;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTY_TAX_ASSESSMENT_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_VACANTLAND_ASSESSMENT_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_WRONG_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CASH;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD;
import static org.egov.ptis.constants.PropertyTaxConstants.TOTAL_AMOUNT;
import static org.egov.ptis.constants.PropertyTaxConstants.UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.commons.Area;
import org.egov.commons.Bank;
import org.egov.commons.Installment;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.dcb.bean.ChequePayment;
import org.egov.dcb.bean.Payment;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.CrossHierarchy;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.CorrespondenceAddress;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.bean.SurveyAssessmentDetails;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.integration.utils.CollectionHelper;
import org.egov.ptis.client.model.PenaltyAndRebate;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyMutationDAO;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.document.DocumentTypeDetails;
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
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.TaxExemptionReason;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.BoundaryDetails;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.FloorDetails;
import org.egov.ptis.domain.model.LocalityDetails;
import org.egov.ptis.domain.model.MasterCodeNamePairDetails;
import org.egov.ptis.domain.model.NewPropertyDetails;
import org.egov.ptis.domain.model.OwnerDetails;
import org.egov.ptis.domain.model.OwnerInformation;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.PayPropertyTaxDetails;
import org.egov.ptis.domain.model.PropertyDetails;
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.egov.ptis.domain.model.ReceiptDetails;
import org.egov.ptis.domain.model.RestAssessmentDetails;
import org.egov.ptis.domain.model.RestPropertyTaxDetails;
import org.egov.ptis.domain.model.ViewPropertyDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.master.service.FloorTypeService;
import org.egov.ptis.master.service.RoofTypeService;
import org.egov.ptis.master.service.WallTypeService;
import org.egov.ptis.master.service.WoodTypeService;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

public class PropertyExternalService {
    private static final String ASSESSMENT = "Assessment";
    public static final Integer FLAG_MOBILE_EMAIL = 0;
    public static final Integer FLAG_TAX_DETAILS = 1;
    public static final Integer FLAG_FULL_DETAILS = 2;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private ApplicationContext beanProvider;
    @Autowired
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private PTBillServiceImpl ptBillServiceImpl;
    @Autowired
    @Qualifier("propertyTaxBillable")
    private PropertyTaxBillable propertyTaxBillable;
    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @Autowired
    private CollectionIntegrationService collectionService;
    @Autowired
    private PropertyPersistenceService basicPropertyService;
    @Autowired
    private BoundaryTypeService boundaryTypeService;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
    @Autowired
    private UserService userService;
    @Autowired
    private BankHibernateDAO bankHibernateDAO;
    @Autowired
    private PropertyMutationDAO propertyMutationDAO;
    @Autowired
    private FloorTypeService floorTypeService;
    @Autowired
    private RoofTypeService roofTypeService;
    @Autowired
    private WallTypeService wallTypeService;
    @Autowired
    private WoodTypeService woodTypeService;
    @Autowired
    private InstallmentHibDao installmentDao;
    @Autowired
    private ModuleService moduleService;
    
    public AssessmentDetails loadAssessmentDetails(final String propertyId, final Integer flag,
            final BasicPropertyStatus status) {
        PropertyImpl property;
        AssessmentDetails assessmentDetail = new AssessmentDetails();
        assessmentDetail.setPropertyID(propertyId);
        assessmentDetail.setFlag(flag);
        validate(assessmentDetail);
        BasicProperty basicProperty = initiateBasicProperty(status, assessmentDetail);
        if (basicProperty != null) {
            property = (PropertyImpl)basicProperty.getProperty();
            if (basicProperty.getLatitude() != null && basicProperty.getLongitude() != null) {
                assessmentDetail.setLatitude(basicProperty.getLatitude());
                assessmentDetail.setLongitude(basicProperty.getLongitude());
            }
            if (flag.equals(FLAG_MOBILE_EMAIL))
                loadPrimaryMobileAndEmail(basicProperty, assessmentDetail);
            if (property != null) {
                final PropertyDetails propertyDetails = new PropertyDetails();
                assessmentDetail.setPropertyDetails(propertyDetails);
                if (flag.equals(FLAG_FULL_DETAILS)) {
                    getAsssessmentDetails(basicProperty, assessmentDetail);
                    loadPropertyDues(property, assessmentDetail);
                }
                if (flag.equals(FLAG_TAX_DETAILS))
                    loadPropertyDues(property, assessmentDetail);
                if (assessmentDetail.isExempted()) {
                    assessmentDetail.getPropertyDetails().setTaxDue(ZERO);
                    assessmentDetail.getPropertyDetails().setCurrentTax(ZERO);
                    assessmentDetail.getPropertyDetails().setArrearTax(ZERO);
                }
            }
        }
        return assessmentDetail;
    }

    private void validate(AssessmentDetails assessmentDetail) {
        if (assessmentDetail.getPropertyID() == null || assessmentDetail.getPropertyID().trim().equals(""))
            throw new ApplicationRuntimeException("PropertyID is null or empty!");
        if (assessmentDetail.getFlag() == null || assessmentDetail.getFlag() > 3)
            throw new ApplicationRuntimeException("Invalid Flag");
    }

    private BasicProperty initiateBasicProperty(BasicPropertyStatus status, AssessmentDetails assessmentDetail) {
        BasicProperty basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(assessmentDetail.getPropertyID());
        final ErrorDetails errorDetails = new ErrorDetails();
        if (null != basicProperty) {
            assessmentDetail.setStatus(basicProperty.isActive());
            if (status.equals(BasicPropertyStatus.ACTIVE)) {
                if (basicProperty.isActive()) {
                    checkStatusValues(basicProperty, errorDetails);
                } else {
                    errorDetails.setErrorCode(PROPERTY_ACTIVE_ERR_CODE);
                    errorDetails.setErrorMessage(PROPERTY_ACTIVE_NOT_EXISTS);
                    assessmentDetail.setErrorDetails(errorDetails);
                }
            } else if (status.equals(BasicPropertyStatus.INACTIVE)) {
                if (!basicProperty.isActive()) {
                    checkStatusValues(basicProperty, errorDetails);
                } else {
                    errorDetails.setErrorCode(PROPERTY_INACTIVE_ERR_CODE);
                    errorDetails.setErrorMessage(PROPERTY_INACTIVE_ERR_MSG);
                    assessmentDetail.setErrorDetails(errorDetails);
                }
            } else {
                checkStatusValues(basicProperty, errorDetails);
            }
        } else {
            errorDetails.setErrorCode(PROPERTY_NOT_EXIST_ERR_CODE);
            errorDetails.setErrorMessage(PROPERTY_NOT_EXIST_ERR_MSG_PREFIX
                    + assessmentDetail.getPropertyID() + PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX);
        }
        assessmentDetail.setErrorDetails(errorDetails);
        return basicProperty;
    }

    private void checkStatusValues(BasicProperty basicProperty, ErrorDetails errorDetails) {
        final Set<PropertyStatusValues> statusValues = basicProperty.getPropertyStatusValuesSet();
        if (null != statusValues && !statusValues.isEmpty())
            for (final PropertyStatusValues statusValue : statusValues)
                if (statusValue.getPropertyStatus().getStatusCode() == MARK_DEACTIVE) {
                    errorDetails.setErrorCode(PROPERTY_MARK_DEACTIVATE_ERR_CODE);
                    errorDetails.setErrorMessage(PROPERTY_MARK_DEACTIVATE_ERR_MSG);
                }
    }

    private void loadPrimaryMobileAndEmail(BasicProperty basicProperty, AssessmentDetails assessmentDetail) {
        final User primaryOwner = basicProperty.getPrimaryOwner();
        assessmentDetail.setPrimaryEmail(primaryOwner.getEmailId());
        assessmentDetail.setPrimaryMobileNo(primaryOwner.getMobileNumber());
    }

    private void loadPropertyDues(PropertyImpl property, AssessmentDetails assessmentDetail) {
        final Map<String, BigDecimal> resultmap = ptDemandDAO.getDemandCollMap(property);
        if (null != resultmap && !resultmap.isEmpty()) {
            final BigDecimal currDmd = resultmap.get(CURR_FIRSTHALF_DMD_STR);
            final BigDecimal arrDmd = resultmap.get(ARR_DMD_STR);
            final BigDecimal currCollection = resultmap.get(CURR_FIRSTHALF_COLL_STR);
            final BigDecimal arrColelection = resultmap.get(ARR_COLL_STR);

            final BigDecimal taxDue = currDmd.add(arrDmd).subtract(currCollection).subtract(arrColelection);
            assessmentDetail.getPropertyDetails().setTaxDue(taxDue);
            assessmentDetail.getPropertyDetails().setCurrentTax(currDmd);
            assessmentDetail.getPropertyDetails().setArrearTax(arrDmd);
        }
    }

    private void getAsssessmentDetails(BasicProperty basicProperty, AssessmentDetails assessmentDetail) {

        // Owner Details
        assessmentDetail.setBoundaryDetails(prepareBoundaryInfo(basicProperty));
        assessmentDetail.setHouseNo(basicProperty.getAddress().getHouseNoBldgApt());
        assessmentDetail.setPropertyAddress(basicProperty.getAddress().toString());
        PropertyImpl property = (PropertyImpl)basicProperty.getProperty();
        if (null != property) {
            assessmentDetail.setOwnerNames(prepareOwnerInfo(property));
            assessmentDetail.setExempted(property.getIsExemptedFromTax());
            // Property Details
            final PropertyDetail propertyDetail = property.getPropertyDetail();
            if (null != propertyDetail) {
                assessmentDetail.getPropertyDetails().setPropertyType(propertyDetail.getPropertyTypeMaster().getType());
                if (propertyDetail.getPropertyUsage() != null)
                    assessmentDetail.getPropertyDetails().setPropertyUsage(
                            propertyDetail.getPropertyUsage().getUsageName());
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
            if (null != propertyID.getElectionBoundary()) {
                boundaryDetails.setAdminWardId(propertyID.getElectionBoundary().getId());
                boundaryDetails.setAdminWardNumber(propertyID.getElectionBoundary().getBoundaryNum());
                boundaryDetails.setAdminWardName(propertyID.getElectionBoundary().getName());
                boundaryDetails.setAdminWardBoundaryType(propertyID.getElectionBoundary().getBoundaryType().getName());
            }
            if (null != propertyID.getArea()) {
                boundaryDetails.setBlockId(propertyID.getArea().getId());
                boundaryDetails.setBlockNumber(propertyID.getArea().getBoundaryNum());
                boundaryDetails.setBlockName(propertyID.getArea().getName());
            }
            if (null != propertyID.getLocality()) {
                boundaryDetails.setLocalityId(propertyID.getLocality().getId());
                boundaryDetails.setLocalityName(propertyID.getLocality().getName());
            }
            if (null != propertyID.getStreet()) {
                boundaryDetails.setStreetId(propertyID.getStreet().getId());
                boundaryDetails.setStreetName(propertyID.getStreet().getName());
            }
        }
        return boundaryDetails;
    }

    private Set<OwnerName> prepareOwnerInfo(final Property property) {
        final List<PropertyOwnerInfo> propertyOwners = property.getBasicProperty().getPropertyOwnerInfo();
        final Set<OwnerName> ownerNames = new HashSet<OwnerName>(0);
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

    public PropertyTaxDetails getPropertyTaxDetails(final String assessmentNo, final String category) {
        PropertyTaxDetails propertyTaxDetails;
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        final ErrorDetails errorDetails = new ErrorDetails();
        if (basicProperty != null) {
        	Property property = basicProperty.getProperty();
        	if(property != null && property.getIsExemptedFromTax()){
        		propertyTaxDetails = new PropertyTaxDetails();
                    errorDetails.setErrorCode(PROPERTY_EXEMPTED_ERR_CODE);
                    errorDetails.setErrorMessage(PROPERTY_EXEMPTED_ERR_MSG);
                    propertyTaxDetails.setErrorDetails(errorDetails);
        	} else {
	            propertyTaxDetails = getPropertyTaxDetails(basicProperty, category);
	            if (propertyTaxDetails.getErrorDetails() == null) {
	                errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
	                errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);
	                propertyTaxDetails.setErrorDetails(errorDetails);
	            }
        	}
        } else {
            propertyTaxDetails = new PropertyTaxDetails();
            errorDetails.setErrorCode(PROPERTY_NOT_EXIST_ERR_CODE);
            errorDetails.setErrorMessage(PROPERTY_NOT_EXIST_ERR_MSG_PREFIX + assessmentNo
                    + PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX);
            propertyTaxDetails.setErrorDetails(errorDetails);
        }
        return propertyTaxDetails;
    }
    
    public List<PropertyTaxDetails> getPropertyTaxDetails(final String assessmentNo, final String ownerName,
            final String mobileNumber, final String category, final String doorNo) {
        final List<BasicProperty> basicProperties = basicPropertyDAO.getBasicPropertiesForTaxDetails(assessmentNo, ownerName,
                mobileNumber, category, doorNo);
        List<PropertyTaxDetails> propTxDetailsList = new ArrayList<PropertyTaxDetails>();
        if (null != basicProperties && !basicProperties.isEmpty()) {
            for (final BasicProperty basicProperty : basicProperties) {
                final PropertyTaxDetails propertyTaxDetails = getPropertyTaxDetails(basicProperty, category);
                propTxDetailsList.add(propertyTaxDetails);
            }
        } else {
            PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
            final ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PROPERTY_NOT_EXIST_ERR_CODE);
            errorDetails.setErrorMessage(ASSESSMENT
                    + PROPERTY_NOT_EXIST_ERR_MSG_SUFFIX);
            propertyTaxDetails.setErrorDetails(errorDetails);
            propTxDetailsList.add(propertyTaxDetails);
        }
        return propTxDetailsList;
    }

    public List<PropertyTaxDetails> getPropertyTaxDetails(final String circleName, final String zoneName,
            final String wardName, final String blockName, final String ownerName, final String doorNo,
            final String aadhaarNumber, final String mobileNumber) {
        List<PropertyTaxDetails> propTxDetailsList = null;
        final List<BasicProperty> basicPropertyList = basicPropertyDAO.getBasicPropertiesForTaxDetails(circleName,
                zoneName, wardName, blockName, ownerName, doorNo, aadhaarNumber, mobileNumber);
        if (null != basicPropertyList) {
            propTxDetailsList = new ArrayList<PropertyTaxDetails>();
            for (final BasicProperty basicProperty : basicPropertyList) {
                final PropertyTaxDetails propertyTaxDetails = getPropertyTaxDetails(basicProperty, null);
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

    private PropertyTaxDetails getPropertyTaxDetails(final BasicProperty basicProperty, String category) {
        final PropertyTaxDetails propertyTaxDetails = new PropertyTaxDetails();
        final ErrorDetails errorDetails = new ErrorDetails();
        if (null != basicProperty) {
            final String assessmentNo = basicProperty.getUpicNo();
            if (!basicProperty.isActive()) {
                errorDetails.setErrorCode(PROPERTY_DEACTIVATE_ERR_CODE);
                errorDetails.setErrorMessage(PROPERTY_DEACTIVATE_ERR_MSG);
                propertyTaxDetails.setErrorDetails(errorDetails);
            } else {
                final Set<PropertyStatusValues> statusValues = basicProperty.getPropertyStatusValuesSet();
                if (null != statusValues && !statusValues.isEmpty())
                    for (final PropertyStatusValues statusValue : statusValues)
                        if (statusValue.getPropertyStatus().getStatusCode() == MARK_DEACTIVE) {
                            errorDetails.setErrorCode(PROPERTY_MARK_DEACTIVATE_ERR_CODE);
                            errorDetails.setErrorMessage(PROPERTY_MARK_DEACTIVATE_ERR_MSG);
                        }
            }
            final Property property = basicProperty.getProperty();
            ptDemandDAO.getDemandCollMap(property);
            
            if (!StringUtils.isBlank(category)) {
                String propType = property.getPropertyDetail().getPropertyTypeMaster().getCode();
                if (CATEGORY_TYPE_PROPERTY_TAX.equals(category)) {
                    if (propType.equals(OWNERSHIP_TYPE_VAC_LAND)) {
                        errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_PROPERTY_TAX_ASSESSMENT_NOT_FOUND);
                        errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_PROPERTY_TAX_ASSESSMENT_NOT_FOUND);
                        propertyTaxDetails.setErrorDetails(errorDetails);
                        return propertyTaxDetails;
                    }
                } else if (CATEGORY_TYPE_VACANTLAND_TAX.equals(category)) {
                    if (!propType.equals(OWNERSHIP_TYPE_VAC_LAND)) {
                        errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_VACANTLAND_ASSESSMENT_NOT_FOUND);
                        errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_VACANTLAND_ASSESSMENT_NOT_FOUND);
                        propertyTaxDetails.setErrorDetails(errorDetails);
                        return propertyTaxDetails;
                    }
                } else {
                    errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_WRONG_CATEGORY);
                    errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_WRONG_CATEGORY);
                    propertyTaxDetails.setErrorDetails(errorDetails);
                    return propertyTaxDetails;
                }
            }
            final List<PropertyOwnerInfo> propOwnerInfos = property.getBasicProperty().getPropertyOwnerInfo();
            propertyTaxDetails.setOwnerDetails(new ArrayList<OwnerDetails>(0));
            OwnerDetails ow;
            for (int i = 0; i < propOwnerInfos.size(); i++) {
                final PropertyOwnerInfo propOwnerInfo = propOwnerInfos.get(i);
                final String ownerName = propOwnerInfo.getOwner().getName();
                if (null != ownerName && ownerName.trim().length() != 0) {
                    ow = new OwnerDetails();
                    ow.setOwnerName(ownerName);
                    ow.setMobileNo(propOwnerInfo.getOwner().getMobileNumber());
                    propertyTaxDetails.getOwnerDetails().add(ow);
                }
            }
            propertyTaxDetails.setPropertyAddress(property.getBasicProperty().getAddress().toString());
            propertyTaxDetails.setAssessmentNo(property.getBasicProperty().getUpicNo());
            propertyTaxDetails.setLocalityName(property.getBasicProperty().getPropertyID().getLocality().getName());

            propertyTaxBillable.setBasicProperty(basicProperty);
            propertyTaxBillable.setLevyPenalty(Boolean.TRUE);
            Map<Installment, PenaltyAndRebate> calculatedPenalty = propertyTaxBillable.getCalculatedPenalty();

            final List<Object> list = ptDemandDAO.getPropertyTaxDetails(assessmentNo);
            if (list.size() > 0)
                propertyTaxDetails.setTaxDetails(new ArrayList<RestPropertyTaxDetails>(0));
            else {
                return propertyTaxDetails;
            }
            String loopInstallment = "";
            RestPropertyTaxDetails arrearDetails = null;
            BigDecimal total = BigDecimal.ZERO;
            for (final Object record : list) {

                final Object[] data = (Object[]) record;
                final String taxType = (String) data[0];

                final String installment = (String) data[1];
                final Double dmd = (Double) data[2];
                final Double col = (Double) data[3];
                final BigDecimal demand = BigDecimal.valueOf(dmd.doubleValue());
                final BigDecimal collection = BigDecimal.valueOf(col.doubleValue());
                if (loopInstallment.isEmpty()) {
                    loopInstallment = installment;
                    arrearDetails = new RestPropertyTaxDetails();
                    arrearDetails.setInstallment(installment);
                }
                if (loopInstallment.equals(installment)) {

                    if (DEMANDRSN_CODE_PENALTY_FINES.equalsIgnoreCase(taxType))
                        arrearDetails.setPenalty(demand.subtract(collection));
                    else if (DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY.equalsIgnoreCase(taxType))
                        arrearDetails.setChqBouncePenalty(demand.subtract(collection));
                    else
                        total = total.add(demand.subtract(collection));

                } else {
                    arrearDetails.setTaxAmount(total);
                    // penalty calculation is entirely moved to next loop . So
                    // no need to add it here
                    // arrearDetails.setTotalAmount(total.add(arrearDetails.getPenalty()).add(
                    // arrearDetails.getChqBouncePenalty()));
                    arrearDetails.setTotalAmount(total.add(arrearDetails.getChqBouncePenalty()));
                    propertyTaxDetails.getTaxDetails().add(arrearDetails);
                    loopInstallment = installment;
                    arrearDetails = new RestPropertyTaxDetails();
                    arrearDetails.setInstallment(installment);
                    total = BigDecimal.ZERO;
                    if (DEMANDRSN_CODE_PENALTY_FINES.equalsIgnoreCase(taxType))
                        arrearDetails.setPenalty(demand.subtract(collection));
                    else if (DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY.equalsIgnoreCase(taxType))
                        arrearDetails.setChqBouncePenalty(demand.subtract(collection));
                    else
                        total = total.add(demand.subtract(collection));

                }
            }
            if (arrearDetails != null) {
                arrearDetails.setTaxAmount(total);
                arrearDetails.setTotalAmount(total.add(arrearDetails.getChqBouncePenalty()));
                propertyTaxDetails.getTaxDetails().add(arrearDetails);
            }

            Set<Installment> keySet = calculatedPenalty.keySet();

            // for all years data
            Outer: for (RestPropertyTaxDetails details : propertyTaxDetails.getTaxDetails()) {
                // loop trough the penalty
                Inner: for (Installment inst : keySet) {
                    if (inst.getDescription().equalsIgnoreCase(details.getInstallment())) {
                        details.setPenalty(calculatedPenalty.get(inst).getPenalty());
                        details.setRebate(calculatedPenalty.get(inst).getRebate());
                        details.setTotalAmount(details.getTotalAmount().add(calculatedPenalty.get(inst).getPenalty()));
                        if (details.getRebate() != null) {
                            details.setTotalAmount(details.getTotalAmount().subtract(details.getRebate()));
                        }

                        break Inner;
                    }
                }

            }
        }

        return propertyTaxDetails;
    }

    public ReceiptDetails payPropertyTax(final PayPropertyTaxDetails payPropertyTaxDetails, String propertyType) {
        ReceiptDetails receiptDetails = null;
        ErrorDetails errorDetails = null;
        BigDecimal totalAmountToBePaid = BigDecimal.ZERO;
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(payPropertyTaxDetails
                .getAssessmentNo());
        if(propertyType.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
        	propertyTaxBillable.setVacantLandTaxPayment(true);
        
        propertyTaxBillable.setBasicProperty(basicProperty);
        propertyTaxBillable.setUserId(2L);
        ApplicationThreadLocals.setUserId(2L);
        propertyTaxBillable.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty
                .getPropertyID().getWard().getBoundaryNum().toString()));
        propertyTaxBillable.setBillType(egBillDAO.getBillTypeByCode(BILLTYPE_AUTO));
        propertyTaxBillable.setLevyPenalty(Boolean.TRUE);
        propertyTaxBillable.setTransanctionReferenceNumber(payPropertyTaxDetails.getTransactionId());
        final EgBill egBill = ptBillServiceImpl.generateBill(propertyTaxBillable);
        
        for(EgBillDetails billDetails : egBill.getEgBillDetails()){
        	if(!billDetails.getDescription().contains(PropertyTaxConstants.DEMANDRSN_STR_ADVANCE) 
    				&& billDetails.getCrAmount().compareTo(BigDecimal.ZERO) > 0){
        		totalAmountToBePaid = totalAmountToBePaid.add(billDetails.getCrAmount());
    		}
        }

        final CollectionHelper collectionHelper = new CollectionHelper(egBill);
        final Map<String, String> paymentDetailsMap = new HashMap<String, String>();
        paymentDetailsMap.put(TOTAL_AMOUNT, payPropertyTaxDetails.getPaymentAmount().toString());
        paymentDetailsMap.put(PAID_BY, payPropertyTaxDetails.getPaidBy());
        if (THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(payPropertyTaxDetails
                .getPaymentMode().toLowerCase())
                || THIRD_PARTY_PAYMENT_MODE_DD.equalsIgnoreCase(payPropertyTaxDetails
                        .getPaymentMode().toLowerCase())) {
            paymentDetailsMap.put(ChequePayment.INSTRUMENTNUMBER, payPropertyTaxDetails.getChqddNo());
            paymentDetailsMap.put(ChequePayment.INSTRUMENTDATE,
                    ChequePayment.CHEQUE_DATE_FORMAT.format(payPropertyTaxDetails.getChqddDate()));
            paymentDetailsMap.put(ChequePayment.BRANCHNAME, payPropertyTaxDetails.getBranchName());
            final Long validatesBankId = validateBank(payPropertyTaxDetails.getBankName());
            paymentDetailsMap.put(ChequePayment.BANKID, validatesBankId.toString());
        }
        final Payment payment = Payment.create(payPropertyTaxDetails.getPaymentMode().toLowerCase(), paymentDetailsMap);
        final BillReceiptInfo billReceiptInfo = collectionHelper.executeCollection(payment,
                payPropertyTaxDetails.getSource());

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
            receiptDetails.setPaymentAmount(billReceiptInfo.getTotalAmount());
            receiptDetails.setPaymentMode(payPropertyTaxDetails.getPaymentMode());
            receiptDetails.setTransactionId(billReceiptInfo.getManualReceiptNumber());
            
            String[] paidFrom = null;
            String[] paidTo = null;
            Installment fromInstallment = null;
            Installment toInstallment = null;
	        if(totalAmountToBePaid.compareTo(BigDecimal.ZERO) > 0){
	            List<ReceiptAccountInfo> receiptAccountsList = new ArrayList<ReceiptAccountInfo>(billReceiptInfo.getAccountDetails());
	            Collections.sort(receiptAccountsList, new Comparator<ReceiptAccountInfo>() {
	                @Override
	                public int compare(ReceiptAccountInfo rcptAcctInfo1, ReceiptAccountInfo rcptAcctInfo2) {
	                	if(rcptAcctInfo1.getOrderNumber() != null && rcptAcctInfo2.getOrderNumber() != null)
		                    return rcptAcctInfo1.getOrderNumber()
		                            .compareTo(rcptAcctInfo2.getOrderNumber());
						return 0;
	                }
	            });
	            for(ReceiptAccountInfo rcptAcctInfo : receiptAccountsList){
	            	if(rcptAcctInfo.getCrAmount().compareTo(ZERO)>0 
	            			&& !rcptAcctInfo.getDescription().contains(PropertyTaxConstants.DEMANDRSN_STR_ADVANCE)){
	            		if(paidFrom == null)
	            			paidFrom = rcptAcctInfo.getDescription().split("-",2);
	            		paidTo = rcptAcctInfo.getDescription().split("-",2);
	            	}
	            }
	            
	            if(paidFrom != null)
	            	fromInstallment = installmentDao.getInsatllmentByModuleAndDescription(moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME), paidFrom[1].toString());
	            if(paidTo != null)
	            	toInstallment = installmentDao.getInsatllmentByModuleAndDescription(moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME), paidTo[1].toString());
	        }
	        /**
	         * If collection is done for complete current financial year only, todate shown is last date of current financial year and payment type is 'Fully'.
	        	In case, collection is done for complete current financial year with advance, todate shown is last date of current financial year and payment type is 'Advance'.
	        	In case, collection is only for advance, collection period will be blank and payment type is 'Advance'
	         */
            if(totalAmountToBePaid.compareTo(BigDecimal.ZERO)==0){
            	receiptDetails.setPaymentPeriod(StringUtils.EMPTY);
            	receiptDetails.setPaymentType(PAYMENT_TYPE_ADVANCE);
            }
            else
            	receiptDetails.setPaymentPeriod(DateUtils.getDefaultFormattedDate(fromInstallment.getFromDate())
            		.concat(" to ").concat(DateUtils.getDefaultFormattedDate(toInstallment.getToDate())));
            
            if(payPropertyTaxDetails.getPaymentAmount().compareTo(totalAmountToBePaid) > 0)
            	receiptDetails.setPaymentType(PAYMENT_TYPE_ADVANCE);
            else if(totalAmountToBePaid.compareTo(payPropertyTaxDetails.getPaymentAmount())>0)
            	receiptDetails.setPaymentType(PAYMENT_TYPE_PARTIALLY);
            else
            	receiptDetails.setPaymentType(PAYMENT_TYPE_FULLY);
            
            basicPropertyService.update(basicProperty);
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);

            receiptDetails.setErrorDetails(errorDetails);
        }
        return receiptDetails;
    }

    private Long validateBank(final String bankCodeOrName) {

        Bank bank = bankHibernateDAO.getBankByCode(bankCodeOrName);
        if (bank == null)
            // Tries by name if code not found
            bank = bankHibernateDAO.getBankByCode(bankCodeOrName);
        return new Long(bank.getId());

    }

    public ErrorDetails payWaterTax(final String consumerNo, final String paymentMode, final BigDecimal totalAmount,
            final String paidBy) {
        ErrorDetails errorDetails = validatePaymentDetails(consumerNo, paymentMode, totalAmount, paidBy);
        if (null != errorDetails)
            return errorDetails;
        else {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);
        }
        return errorDetails;
    }

    public List<MasterCodeNamePairDetails> getPropertyTypeMasterDetails() {
        final List<MasterCodeNamePairDetails> propTypeMasterDetailsList = new ArrayList<MasterCodeNamePairDetails>(0);
        final List<PropertyTypeMaster> propertyTypeMasters = propertyTypeMasterDAO.findAllExcludeEWSHS();
        for (final PropertyTypeMaster propertyTypeMaster : propertyTypeMasters) {
            final MasterCodeNamePairDetails propTypeMasterDetails = new MasterCodeNamePairDetails();
            propTypeMasterDetails.setCode(propertyTypeMaster.getCode());
            propTypeMasterDetails.setName(propertyTypeMaster.getType());
            propTypeMasterDetailsList.add(propTypeMasterDetails);
        }
        return propTypeMasterDetailsList;
    }

    public PropertyTypeMaster getPropertyTypeMasterByCode(final String propertyTypeMasterCode) {
        return propertyTypeMasterDAO.getPropertyTypeMasterByCode(propertyTypeMasterCode);
    }

    public List<MasterCodeNamePairDetails> getPropertyTypeCategoryDetails(final String categoryCode) {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>(0);
        Map<String, String> codeNameMap = null;
        final PropertyTypeMaster propertyTypeMasters = propertyTypeMasterDAO.getPropertyTypeMasterByCode(categoryCode);
        if (null != propertyTypeMasters) {
            if (propertyTypeMasters.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                codeNameMap = VAC_LAND_PROPERTY_TYPE_CATEGORY;
            else
                codeNameMap = NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
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
        final Map<String, String> vacLandMap = VAC_LAND_PROPERTY_TYPE_CATEGORY;
        final Map<String, String> nonVacLandMap = NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;

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

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getApartmentsAndComplexes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>(0);
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
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>(0);
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

    public List<MasterCodeNamePairDetails> getBoundariesByBoundaryTypeAndHierarchyType(final String boundaryType, final String hierarchyType) {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>(0);
        final List<Boundary> boundaryList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(boundaryType,hierarchyType);
        if (boundaryList != null)
            for (final Boundary boundary : boundaryList) {
                final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
                mstrCodeNamePairDetails.setCode(boundary.getBoundaryNum().toString());
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

    public Boundary getBoundaryByNumberAndType(final String boundaryNum, final String boundaryTypeName, final String hierarchyName) {
        final BoundaryType boundaryType = boundaryTypeService
                .getBoundaryTypeByNameAndHierarchyTypeName(boundaryTypeName,hierarchyName);
        final Boundary boundary = boundaryService.getBoundaryByTypeAndNo(boundaryType,
                Long.valueOf(boundaryNum));
        return boundary;
    }
    
    public List<MasterCodeNamePairDetails> getEnumerationBlocks() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<Boundary> enumerationBlockList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ELECTIONWARD_BNDRY_TYPE,
                		ELECTION_HIERARCHY_TYPE);
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
            mstrCodeNamePairDetails.setCode(floorType.getId().toString());
            mstrCodeNamePairDetails.setName(floorType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getRoofTypes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<RoofType> roofTypeList = entityManager.createQuery("from RoofType order by name").getResultList();
        for (final RoofType roofType : roofTypeList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(roofType.getId().toString());
            mstrCodeNamePairDetails.setName(roofType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getWallTypes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<WallType> wallTypeList = entityManager.createQuery("from WallType order by name").getResultList();
        for (final WallType wallType : wallTypeList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(wallType.getId().toString());
            mstrCodeNamePairDetails.setName(wallType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getWoodTypes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<WoodType> woodTypeList = entityManager.createQuery("from WoodType order by name").getResultList();
        for (final WoodType woodType : woodTypeList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(woodType.getId().toString());
            mstrCodeNamePairDetails.setName(woodType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
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
        final List<PropertyUsage> usageList = entityManager.createQuery("from PropertyUsage order by usageName")
                .getResultList();
        for (final PropertyUsage propUsage : usageList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(propUsage.getUsageCode());
            mstrCodeNamePairDetails.setName(propUsage.getUsageName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public PropertyUsage getPropertyUsageByUsageCde(final String usageCode) {
        final Query qry = entityManager
                .createQuery("from PropertyUsage pu where pu.usageCode = :usageCode order by usageName");
        qry.setParameter("usageCode", usageCode);
        final PropertyUsage propertyUsage = (PropertyUsage) qry.getSingleResult();
        return propertyUsage;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getOccupancies() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<PropertyOccupation> propOccupList = entityManager.createQuery("from PropertyOccupation")
                .getResultList();
        for (final PropertyOccupation propOccup : propOccupList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(propOccup.getOccupancyCode());
            mstrCodeNamePairDetails.setName(propOccup.getOccupation());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public PropertyOccupation getPropertyOccupationByOccupancyCode(final String occupancyCode) {
        final Query qry = entityManager
                .createQuery("from PropertyOccupation po where po.occupancyCode = :occupancyCode");
        qry.setParameter("occupancyCode", occupancyCode);
        final PropertyOccupation propertyOccupation = (PropertyOccupation) qry.getSingleResult();
        return propertyOccupation;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getExemptionCategories() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
        final List<TaxExemptionReason> taxExemptionReasonList = entityManager.createQuery(
                "from TaxExemptionReason where isActive = true order by name").getResultList();
        for (final TaxExemptionReason taxExemptionReason : taxExemptionReasonList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(taxExemptionReason.getCode());
            mstrCodeNamePairDetails.setName(taxExemptionReason.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    public TaxExemptionReason getTaxExemptionReasonByCode(final String exemptionReasonCode) {
        TaxExemptionReason taxExemptionReason = null;
        final Query qry = entityManager.createQuery("from TaxExemptionReason ter where ter.code = :code");
        qry.setParameter("code", "" + exemptionReasonCode);
        final List list = qry.getResultList();
        if (null != list && !list.isEmpty())
            taxExemptionReason = (TaxExemptionReason) qry.getSingleResult();
        return taxExemptionReason;
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
    		final String exemptionCode, final String apartmentCmplxCode, final List<OwnerInformation> ownerDetailsList,
            final String mutationReasonCode, final String extentOfSite, final Boolean isExtentAppurtenantLand,
            final String occupancyCertificationNo, final String regdDocNo, final String regdDocDate, 
            final String localityCode, final String blockNum, final String zoneNum, final String street, final String electionWardCode,
            final String doorNo, final String enumerationBlockCode, final String pinCode, final Boolean isCorrAddrDiff,
            final String corrAddr1, final String corrAddr2, final String corrPinCode, final Boolean hasLift,
            final Boolean hasToilet, final Boolean hasWaterTap, final Boolean hasElectricity,
            final Boolean hasAttachedBathroom, final Boolean hasWaterHarvesting, final Boolean hasCable,
            final String floorTypeCode, final String roofTypeCode, final String wallTypeCode,
            final String woodTypeCode, final List<FloorDetails> floorDetailsList, final String surveyNumber,
            final String pattaNumber, final Float vacantLandArea, final Double marketValue,
            final Double currentCapitalValue, final String effectiveDate,
            final String northBoundary, final String southBoundary, final String eastBoundary,
            final String westBoundary, final String parentPropertyNumber, final List<Document> documents) throws ParseException {

        NewPropertyDetails newPropertyDetails = null;
        final PropertyService propService = beanProvider.getBean("propService", PropertyService.class);
        BasicProperty basicProperty = createBasicProperty(propertyTypeMasterCode, propertyCategoryCode, exemptionCode,
                apartmentCmplxCode, mutationReasonCode, ownerDetailsList, extentOfSite, isExtentAppurtenantLand,
                occupancyCertificationNo, regdDocNo, regdDocDate, localityCode, blockNum, zoneNum, street, doorNo,
                electionWardCode, pinCode, isCorrAddrDiff, corrAddr1, corrAddr2, corrPinCode, hasLift, hasToilet,
                hasWaterTap, hasElectricity, hasAttachedBathroom, hasWaterHarvesting, hasCable, floorTypeCode,
                roofTypeCode, wallTypeCode, woodTypeCode, floorDetailsList, surveyNumber, pattaNumber, vacantLandArea,
                marketValue, currentCapitalValue, effectiveDate, northBoundary, southBoundary,
                eastBoundary, westBoundary, parentPropertyNumber, documents, propService);
        PropertyImpl property = (PropertyImpl)basicProperty.getProperty();
        List<File> fileAttachments = new ArrayList<File>(0);
        List<String> uploadContentTypes = new ArrayList<String>(0);
        List<String> uploadFileNames = new ArrayList<String>(0);
        basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
        processAndStoreDocumentsWithReason(basicProperty, DOCS_CREATE_PROPERTY, fileAttachments, uploadFileNames, uploadContentTypes);
        basicProperty.setPropertyOwnerInfoProxy(getPropertyOwnerInfoList(ownerDetailsList));
        basicPropertyService.createOwners(property, basicProperty, basicProperty.getAddress());
        transitionWorkFlow(property, propService, PROPERTY_MODE_CREATE);
        basicPropertyService.applyAuditing(property.getState());
        basicProperty = basicPropertyService.persist(basicProperty);
        if (null != basicProperty) {
            newPropertyDetails = new NewPropertyDetails();
            newPropertyDetails.setApplicationNo(basicProperty.getProperty().getApplicationNo());
            final ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);
            newPropertyDetails.setErrorDetails(errorDetails);
        }
        return newPropertyDetails;
    }

    private BasicProperty createBasicProperty(final String propertyTypeMasterCode, final String propertyCategoryCode,
    		final String exemptionCode, final String apartmentCmplxCode, final String mutationReasonCode,
            final List<OwnerInformation> ownerDetailsList, final String extentOfSite,
            final Boolean isExtentAppurtenantLand, final String occupancyCertificationNo, final String regdDocNo, final String regdDocDate,
            final String localityNum,final String blockNum, final String zoneNum, final String street,
            final String doorNo, final String electionWardCode, final String pinCode, final Boolean isCorrAddrDiff,
            final String corrAddr1, final String corrAddr2, final String corrPinCode, final Boolean lift,
            final Boolean toilet, final Boolean waterTap, final Boolean electricity, final Boolean attachedBathroom,
            final Boolean waterHarvesting, final Boolean cable, final String floorTypeId, final String roofTypeId,
            final String wallTypeId, final String woodTypeId, final List<FloorDetails> floorDetailsList,
            final String surveyNumber, final String pattaNumber, final Float vacantLandArea, final Double marketValue,
            final Double currentCapitalValue, final String effectiveDate,
            final String northBoundary, final String southBoundary, final String eastBoundary,
            final String westBoundary, final String parentPropertyNumber, final List<Document> documents, final PropertyService propService) throws ParseException {
        final BasicProperty basicProperty = new BasicPropertyImpl();
        final PropertyImpl property;
        basicProperty.setRegdDocNo(regdDocNo);
        basicProperty.setRegdDocDate(convertStringToDate(regdDocDate));
        basicProperty.setActive(Boolean.TRUE);
        basicProperty.setSource(SOURCEOFDATA_MOBILE);
        // Creating Property Address object
        final Boundary block = getBoundaryByNumberAndType(blockNum, BLOCK,REVENUE_HIERARCHY_TYPE);
        final PropertyAddress propAddress = createPropAddress(localityNum, block, doorNo, pinCode,
                isCorrAddrDiff, corrAddr1, corrAddr2, corrPinCode);
        basicProperty.setAddress(propAddress);

        // Creating PropertyID object based on basic property, localityCode and boundary map direction
        final PropertyID propertyID = createPropertID(basicProperty, localityNum, block, electionWardCode, zoneNum, northBoundary,
                southBoundary, eastBoundary, westBoundary);
        basicProperty.setPropertyID(propertyID);

        // Get PropertyStatus object to set the status of the property
        final PropertyStatus propertyStatus = getPropertyStatus();
        basicProperty.setStatus(propertyStatus);
        basicProperty.setUnderWorkflow(Boolean.TRUE);

        // Set PropertyMutationMaster object
        final PropertyMutationMaster propertyMutationMaster = getPropertyMutationMaster(mutationReasonCode);
        basicProperty.setPropertyMutationMaster(propertyMutationMaster);
        //need to pass parent property index, in case of bifurcation
        if (propertyMutationMaster.getCode().equals(PROP_CREATE_RSN_BIFUR))
            basicProperty.addPropertyStatusValues(propService.createPropStatVal(basicProperty, PROP_CREATE_RSN, null, null,
                    null, null, parentPropertyNumber));
        // Set isBillCreated property value as false
        basicProperty.setIsBillCreated(STATUS_BILL_NOTCREATED);
        
        final PropertyTypeMaster propertyTypeMaster = getPropertyTypeMasterByCode(propertyTypeMasterCode);
        final PropertyImpl propertyImpl = createPropertyWithBasicDetails(propertyTypeMasterCode);
        
        propertyImpl.getPropertyDetail().setCorrAddressDiff(isCorrAddrDiff);
        propertyImpl.getPropertyDetail().setAppurtenantLandChecked(isExtentAppurtenantLand);
        propertyImpl.getPropertyDetail().setEffectiveDate(convertStringToDate(effectiveDate));
        propertyImpl.setBasicProperty(basicProperty);
        propertyImpl.getBasicProperty().setPropertyID(propertyID);
        if (documents != null)
            propertyImpl.setDocuments(documents);
        if(StringUtils.isNotBlank(exemptionCode)){
	        final TaxExemptionReason taxExemptedReason = getTaxExemptionReasonByCode(exemptionCode);
	        propertyImpl.setTaxExemptedReason(taxExemptedReason);
        }
        if(StringUtils.isNotBlank(apartmentCmplxCode)){
        	final Apartment apartment = getApartmentByCode(apartmentCmplxCode);
            propertyImpl.getPropertyDetail().setApartment(apartment);
        }
        
        propertyImpl.getPropertyDetail().setOccupancyCertificationNo(occupancyCertificationNo);
        
        if(!propertyTypeMasterCode.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)){
        	final FloorType floorType = floorTypeService.getFloorTypeById(Long.valueOf(floorTypeId));
            final RoofType roofType = roofTypeService.getRoofTypeById(Long.valueOf(roofTypeId));
            WallType wallType = null;
            WoodType woodType = null;
            if(StringUtils.isNotBlank(wallTypeId))
            	wallType = wallTypeService.getWallTypeById(Long.valueOf(wallTypeId));
            if(StringUtils.isNotBlank(wallTypeId))
            	woodType = woodTypeService.getWoodTypeById(Long.valueOf(woodTypeId));
            
            propertyImpl.getPropertyDetail().setFloorDetailsProxy(getFloorList(floorDetailsList));
            propertyImpl.getPropertyDetail().setLift(lift);
            propertyImpl.getPropertyDetail().setToilets(toilet);
            propertyImpl.getPropertyDetail().setWaterTap(waterTap);
            propertyImpl.getPropertyDetail().setElectricity(electricity);
            propertyImpl.getPropertyDetail().setAttachedBathRoom(attachedBathroom);
            propertyImpl.getPropertyDetail().setWaterHarvesting(waterHarvesting);
            propertyImpl.getPropertyDetail().setCable(cable);
            
            property = propService.createProperty(propertyImpl, extentOfSite, mutationReasonCode,
                    propertyTypeMaster.getId().toString(),
                    null, null, STATUS_ISACTIVE, regdDocNo, null, floorType != null ? floorType.getId() : null,
                    roofType != null ? roofType.getId() : null, wallType != null ? wallType.getId() : null,
                    woodType != null ? woodType.getId() : null, null, null, null, null);
        } else {
        	propertyImpl.getPropertyDetail().setDateOfCompletion(convertStringToDate(effectiveDate));
        	propertyImpl.getPropertyDetail().setCurrentCapitalValue(currentCapitalValue);
            propertyImpl.getPropertyDetail().setSurveyNumber(surveyNumber);
            propertyImpl.getPropertyDetail().setPattaNumber(pattaNumber);
            final Area area = new Area();
            area.setArea(vacantLandArea);
            propertyImpl.getPropertyDetail().setSitalArea(area);
            propertyImpl.getPropertyDetail().setMarketValue(marketValue);
            property = propService.createProperty(propertyImpl, extentOfSite, mutationReasonCode,
                    propertyTypeMaster.getId().toString(),
                    null, null, STATUS_ISACTIVE, regdDocNo, null, null, null,
                    null, null, null, null, null, null);
        }
        
        property.setStatus(STATUS_DEMAND_INACTIVE);
        property.setPropertyModifyReason(PROP_CREATE_RSN);
        property.getPropertyDetail().setCategoryType(propertyCategoryCode);
        basicProperty.addProperty(property);

        Date propCompletionDate = null;
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail()
                    .getFloorDetailsProxy());
        else
        	propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
        
        basicProperty.setPropOccupationDate(propCompletionDate);

        if (property != null && !property.getDocuments().isEmpty())
            propService.processAndStoreDocument(property.getDocuments());
        try {
            propService.createDemand(propertyImpl, propCompletionDate);
        } catch (TaxCalculatorExeption e) {


        }
        return basicProperty;
    }

    private PropertyStatus getPropertyStatus() {
        final Query qry = entityManager.createQuery("from PropertyStatus where statusCode = :statusCode");
        qry.setParameter("statusCode", PROPERTY_STATUS_WORKFLOW);
        final PropertyStatus propertyStatus = (PropertyStatus) qry.getSingleResult();
        return propertyStatus;
    }

    private PropertyAddress createPropAddress(final String localityNum, final Boundary block, final String doorNo,
            final String pinCode, final Boolean isCorrespondenceAddrDiff,
            final String corrAddr1, final String corrAddr2, final String corrPinCode) {

        final Address propAddr = new PropertyAddress();
        propAddr.setHouseNoBldgApt(doorNo);
        propAddr.setAreaLocalitySector(getBoundaryByNumberAndType(localityNum,LOCALITY_BNDRY_TYPE,LOCATION_HIERARCHY_TYPE).getName());
        String cityName = ApplicationThreadLocals.getCityName();
        propAddr.setStreetRoadLine(block.getParent().getName());
        propAddr.setCityTownVillage(cityName);

        if (StringUtils.isNotBlank(pinCode))
            propAddr.setPinCode(pinCode);
        
        Address ownerAddress = null;
        if (isCorrespondenceAddrDiff)
            ownerAddress = createCorrespondenceAddress(doorNo, corrAddr1, corrAddr2, corrPinCode);
        else {
            ownerAddress = new CorrespondenceAddress();
            ownerAddress.setAreaLocalitySector(propAddr.getAreaLocalitySector());
            ownerAddress.setHouseNoBldgApt(propAddr.getHouseNoBldgApt());
            ownerAddress.setStreetRoadLine(propAddr.getStreetRoadLine());
            ownerAddress.setPinCode(propAddr.getPinCode());
        }
        
        return (PropertyAddress) propAddr;
    }

    private Address createCorrespondenceAddress(final String corrDoorNo, final String corrAddr1,
            final String corrAddr2, final String corrPinCode) {
        final Address ownerCorrAddr = new CorrespondenceAddress();
        ownerCorrAddr.setHouseNoBldgApt(corrDoorNo);
        ownerCorrAddr.setAreaLocalitySector(corrAddr1);
        ownerCorrAddr.setStreetRoadLine(corrAddr2);
        ownerCorrAddr.setPinCode(corrPinCode);
        return ownerCorrAddr;
    }

    private PropertyID createPropertID(final BasicProperty basicProperty, final String localityNum, final Boundary block,
            final String electionBoundaryNum, final String zoneNum, final String northBoundary, final String southBoundary,
            final String eastBoundary, final String westBoundary) {

    	final PropertyID propertyID = new PropertyID();
        final Boundary ward = block.getParent();
        final Boundary zone = getBoundaryByNumberAndType(zoneNum,ZONE,REVENUE_HIERARCHY_TYPE);
        final Boundary locality = getBoundaryByNumberAndType(localityNum, LOCALITY_BNDRY_TYPE,LOCATION_HIERARCHY_TYPE);
        propertyID.setArea(block);
        propertyID.setWard(ward);
        propertyID.setZone(zone);
        propertyID.setLocality(locality);
        propertyID.setBasicProperty(basicProperty);
        final Boundary electionBoundary = getBoundaryByNumberAndType(electionBoundaryNum,WARD,ADMIN_HIERARCHY_TYPE);
        if (electionBoundary != null){
            propertyID.setElectionBoundary(electionBoundary);
            basicProperty.setBoundary(electionBoundary);
        }
        if (StringUtils.isNotBlank(northBoundary))
            propertyID.setNorthBoundary(northBoundary);
        if (StringUtils.isNotBlank(southBoundary))
            propertyID.setSouthBoundary(southBoundary);
        if (StringUtils.isNotBlank(eastBoundary))
            propertyID.setEastBoundary(eastBoundary);
        if (StringUtils.isNotBlank(westBoundary))
            propertyID.setWestBoundary(westBoundary);
        propertyID.setBasicProperty(basicProperty);
        return propertyID;
    }

    private PropertyMutationMaster getPropertyMutationMaster(final String mutationReasonCode) {
        final Query qry = entityManager
                .createQuery("from PropertyMutationMaster pmm where pmm.code = :mutationReasonCode");
        qry.setParameter("mutationReasonCode", mutationReasonCode);
        final PropertyMutationMaster propMutationMaster = (PropertyMutationMaster) qry.getSingleResult();
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
    public ErrorDetails validatePaymentDetails(final String assessmentNo, final String paymentMode,
            final BigDecimal totalAmount, final String paidBy) {
        ErrorDetails errorDetails = null;
        if (assessmentNo == null || assessmentNo.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_REQUIRED);
            errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_REQUIRED);
        } else {
            if (assessmentNo.trim().length() > 0 && assessmentNo.trim().length() < 10) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_LEN);
                errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_LEN);
            }
            if (!basicPropertyDAO.isAssessmentNoExist(assessmentNo)) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
                errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
            }
        }

        if (paymentMode == null || paymentMode.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_PAYMENT_MODE_REQUIRED);
            errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_PAYMENT_MODE_REQUIRED);
        } else if (!THIRD_PARTY_PAYMENT_MODE_CASH.equalsIgnoreCase(paymentMode.trim())
                && !THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(paymentMode.trim())
                && !THIRD_PARTY_PAYMENT_MODE_DD.equalsIgnoreCase(paymentMode.trim())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_PAYMENT_MODE_INVALID);
            errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_PAYMENT_MODE_INVALID);
        }
        return errorDetails;
    }

    private String formatDate(final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    public Date convertStringToDate(final String dateInString) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        final Date stringToDate = sdf.parse(dateInString);
        return stringToDate;
    }

    private void processAndStoreDocumentsWithReason(final BasicProperty basicProperty, final String reason,
            final List<File> fileAttachments, final List<String> uploadFileNames, final List<String> uploadContentTypes) {
        if (!fileAttachments.isEmpty()) {
            int fileCount = 0;
            for (final File file : fileAttachments) {
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

    private PropertyImpl createPropertyWithBasicDetails(final String propertyType) {
        final PropertyImpl propertyImpl = new PropertyImpl();
        if(propertyType.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
        	propertyImpl.setPropertyDetail(new VacantProperty());
        else
        	propertyImpl.setPropertyDetail(new BuiltUpProperty());
        propertyImpl.setBasicProperty(new BasicPropertyImpl());
        return propertyImpl;
    }

    private List<Floor> getFloorList(final List<FloorDetails> floorDetailsList) throws ParseException {
        final List<Floor> floorList = new ArrayList<Floor>(0);
        for (final FloorDetails floorDetails : floorDetailsList) {
            final Floor floor = new Floor();
            floor.setFloorNo(Integer.valueOf(floorDetails.getFloorNoCode()));
            floor.setStructureClassification(getStructureClassificationByCode(floorDetails.getBuildClassificationCode()));
            floor.setPropertyUsage(getPropertyUsageByUsageCde(floorDetails.getNatureOfUsageCode()));
            floor.setPropertyOccupation(getPropertyOccupationByOccupancyCode(floorDetails.getOccupancyCode()));
            floor.setFirmName(floorDetails.getFirmName());
            floor.setOccupantName(floorDetails.getOccupantName());
            floor.setConstructionDate(convertStringToDate(floorDetails.getConstructionDate()));
            floor.setOccupancyDate(convertStringToDate(floorDetails.getOccupancyDate()));
            floor.setCreatedDate(new Date());
            final Area builtUpArea = new Area();
            builtUpArea.setArea(floorDetails.getPlinthArea());
            builtUpArea.setBreadth(floorDetails.getPlinthBreadth());
            builtUpArea.setLength(floorDetails.getPlinthLength());
            floor.setBuiltUpArea(builtUpArea);
            floor.setUnstructuredLand(floorDetails.getUnstructuredLand());
            floor.setBuildingPermissionNo(floorDetails.getBuildingPermissionNo());
            if(StringUtils.isNotBlank(floorDetails.getBuildingPermissionDate()))
            	floor.setBuildingPermissionDate(convertStringToDate(floorDetails.getBuildingPermissionDate()));
            final Area buildingPlanPlinthArea = new Area();
            buildingPlanPlinthArea.setArea(floorDetails.getBuildingPlanPlinthArea());
            floor.setBuildingPlanPlinthArea(buildingPlanPlinthArea);

            floorList.add(floor);
        }
        return floorList;
    }

    private List<PropertyOwnerInfo> getPropertyOwnerInfoList(final List<OwnerInformation> ownerInfoList) {
        final List<PropertyOwnerInfo> proeprtyOwnerInfoList = new ArrayList<PropertyOwnerInfo>(0);
        for (final OwnerInformation ownerInfo : ownerInfoList) {
            final PropertyOwnerInfo propOwner = new PropertyOwnerInfo();
            final User owner = new User();
            owner.setAadhaarNumber(ownerInfo.getAadhaarNo());
            owner.setSalutation(ownerInfo.getSalutationCode());
            owner.setName(ownerInfo.getName());
            owner.setGender(Gender.valueOf(ownerInfo.getGender()));
            owner.setMobileNumber(ownerInfo.getMobileNumber());
            owner.setEmailId(ownerInfo.getEmailId());
            owner.setGuardianRelation(ownerInfo.getGuardianRelation());
            owner.setGuardian(ownerInfo.getGuardian());
            propOwner.setOwner(owner);
            proeprtyOwnerInfoList.add(propOwner);
        }
        return proeprtyOwnerInfoList;
    }

    public BillReceiptInfo validateTransanctionIdPresent(final String transantion, String propertyType) {
        if (propertyType.equals(OWNERSHIP_TYPE_VAC_LAND))
            return collectionService.getReceiptInfo(SERVICE_CODE_VACANTLANDTAX, transantion);
        else
            return collectionService.getReceiptInfo(PTIS_COLLECTION_SERVICE_CODE, transantion);
    }

    private PropertyImpl transitionWorkFlow(PropertyImpl property, PropertyService propService, String mode) {
        final DateTime currentDate = new DateTime();
        final User user = userService.getUserById(ApplicationThreadLocals.getUserId());
        final String approverComments = APPROVAL_COMMENTS_SUCCESS;
        String currentState = StringUtils.EMPTY; 
        String additionalRule = StringUtils.EMPTY; 
        String natureOftask = StringUtils.EMPTY;
        if(mode.equals(PROPERTY_MODE_CREATE)){
        	currentState = CREATE_CURRENT_STATE_BILL_COLLECTOR_APPROVED;
        	additionalRule = NEW_ASSESSMENT;
        	natureOftask = NATURE_NEW_ASSESSMENT;
        } else {
        	currentState = MODIFY_CURRENT_STATE_BILL_COLLECTOR_APPROVED;
        	additionalRule = ADDTIONAL_RULE_ALTER_ASSESSMENT;
        	natureOftask = NATURE_ALTERATION;
        }
        final Assignment assignment = propService.getUserPositionByZone(property.getBasicProperty(), true);
        final Position pos = assignment.getPosition();

        final WorkFlowMatrix wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null, null,
        		additionalRule, currentState, null);
        property.transition().start().withSenderName(user.getName()).withComments(approverComments)
                .withStateValue(wfmatrix.getCurrentState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                .withNextAction(UD_REVENUE_INSPECTOR_APPROVAL_PENDING).withNatureOfTask(natureOftask);
        return property;
    }

    @SuppressWarnings("unchecked")
    public List<MasterCodeNamePairDetails> getDocumentTypes() {
        final List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>(0);
        final List<DocumentType> documentTypesList = entityManager.createQuery("from DocumentType order by id")
                .getResultList();
        for (final DocumentType documentType : documentTypesList) {
            final MasterCodeNamePairDetails mstrCodeNamePairDetails = new MasterCodeNamePairDetails();
            mstrCodeNamePairDetails.setCode(documentType.getId().toString());
            mstrCodeNamePairDetails.setName(documentType.getName());
            mstrCodeNamePairDetailsList.add(mstrCodeNamePairDetails);
        }
        return mstrCodeNamePairDetailsList;
    }

    private Apartment getApartmentByCode(final String apartmentCode) {
        final Query qry = entityManager.createQuery("from Apartment ap where ap.code = :code");
        qry.setParameter("code", apartmentCode);
        final Apartment apartment = (Apartment) qry.getSingleResult();
        return apartment;
    }
    
    /**
     * Fetches Assessment Details - owner details, tax dues, plinth area, mutation fee related information
     * @param assessmentNo
     * @return
     */
    public RestAssessmentDetails fetchAssessmentDetails(final String assessmentNo) {
        PropertyImpl property;
        RestAssessmentDetails assessmentDetails = new RestAssessmentDetails();
        BasicProperty basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(assessmentNo);
        if(basicProperty != null){
        	assessmentDetails.setAssessmentNo(basicProperty.getUpicNo());
    		assessmentDetails.setPropertyAddress(basicProperty.getAddress().toString());
    		property = (PropertyImpl) basicProperty.getProperty();
            assessmentDetails.setLocalityName(basicProperty.getPropertyID().getLocality().getName());
            if (property != null) {
            	assessmentDetails.setOwnerDetails(prepareOwnerInfo(property));
            	if(property.getPropertyDetail().getTotalBuiltupArea() != null && property.getPropertyDetail().getTotalBuiltupArea().getArea() != null)
            		assessmentDetails.setPlinthArea(property.getPropertyDetail().getTotalBuiltupArea().getArea());
            	Ptdemand currentPtdemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
            	BigDecimal totalTaxDue = BigDecimal.ZERO;
            	if(currentPtdemand != null){
            		for(EgDemandDetails demandDetails : currentPtdemand.getEgDemandDetails()){
            			if(demandDetails.getAmount().compareTo(demandDetails.getAmtCollected()) > 0){
            				totalTaxDue = totalTaxDue.add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()));
            			}
            		}
            	}
            	assessmentDetails.setTotalTaxDue(totalTaxDue);
            }
        }
        PropertyMutation propertyMutation = getLatestPropertyMutationByAssesmentNo(assessmentNo);
        if(propertyMutation != null){
        	assessmentDetails.setMutationFee(propertyMutation.getMutationFee());
        	if(StringUtils.isNotBlank(propertyMutation.getReceiptNum())){
    			assessmentDetails.setIsMutationFeePaid("Y");
    			assessmentDetails.setFeeReceipt(propertyMutation.getReceiptNum());
    			Date receiptDate = null;
    			final Query qry = entityManager.createQuery("select receiptdate from ReceiptHeader where receiptnumber = :receiptNum");
    	        qry.setParameter("receiptNum", propertyMutation.getReceiptNum());
    	        receiptDate = (Date) qry.getSingleResult();
    	        assessmentDetails.setFeeReceiptDate(new SimpleDateFormat("dd/MM/yyyy").format(receiptDate));
    		}
    		else
    			assessmentDetails.setIsMutationFeePaid("N");
    		
    		assessmentDetails.setApplicationNo(propertyMutation.getApplicationNo());
        }else{
    		assessmentDetails.setIsMutationFeePaid("N");
    		assessmentDetails.setFeeReceipt("");
    		assessmentDetails.setFeeReceiptDate("");
    		assessmentDetails.setApplicationNo("");
    	}
        return assessmentDetails;
    }

    /**
     * Fetches Assessment Details - owner details, tax dues, plinth area, mutation fee related information - used in MeeSeva
     * @param applicationNo
     * @return RestAssessmentDetails
     */
    public RestAssessmentDetails loadAssessmentDetails(final String applicationNo) {
        //FIXME move this method to meeseva itself
        RestAssessmentDetails assessmentDetails = new RestAssessmentDetails();
        PropertyMutation propertyMutation = getPropertyMutationByAssesmentNoAndApplicationNo(null, applicationNo);
        BasicProperty basicProperty;
        PropertyImpl property;
        if (propertyMutation != null) {
            basicProperty = propertyMutation.getBasicProperty();
            if (basicProperty != null) {
                assessmentDetails.setAssessmentNo(basicProperty.getUpicNo());
                assessmentDetails.setPropertyAddress(basicProperty.getAddress().toString());
                property = (PropertyImpl) basicProperty.getProperty();
                assessmentDetails.setLocalityName(basicProperty.getPropertyID().getLocality().getName());
                if (property != null) {
                    assessmentDetails.setOwnerDetails(prepareOwnerInfo(property));
                    if (property.getPropertyDetail().getTotalBuiltupArea() != null
                            && property.getPropertyDetail().getTotalBuiltupArea().getArea() != null)
                        assessmentDetails.setPlinthArea(property.getPropertyDetail().getTotalBuiltupArea().getArea());
                    Ptdemand currentPtdemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
                    BigDecimal totalTaxDue = BigDecimal.ZERO;
                    if (currentPtdemand != null) {
                        for (EgDemandDetails demandDetails : currentPtdemand.getEgDemandDetails()) {
                            if (demandDetails.getAmount().compareTo(demandDetails.getAmtCollected()) > 0) {
                                totalTaxDue = totalTaxDue
                                        .add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()));
                            }
                        }
                    }
                    assessmentDetails.setTotalTaxDue(totalTaxDue);
                }
            }
            if (StringUtils.isNotBlank(propertyMutation.getReceiptNum())) {
                assessmentDetails.setFeeReceipt(propertyMutation.getReceiptNum());
                assessmentDetails.setIsMutationFeePaid("Y");
                assessmentDetails.setMutationFee(BigDecimal.ZERO);
            } else {
                assessmentDetails.setIsMutationFeePaid("N");
                assessmentDetails.setMutationFee(propertyMutation.getMutationFee());
            }

            assessmentDetails.setApplicationNo(propertyMutation.getApplicationNo());
        } else {
            assessmentDetails.setIsMutationFeePaid("N");
        }
        return assessmentDetails;
    }

    /**
     * API for Mutation Fee Payment
     * @param payPropertyTaxDetails
     * @return ReceiptDetails
     */
    public ReceiptDetails payMutationFee(final PayPropertyTaxDetails payPropertyTaxDetails) {
        ReceiptDetails receiptDetails = null;
        ErrorDetails errorDetails = null;
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(payPropertyTaxDetails
                .getAssessmentNo());
        PropertyMutation propertyMutation = getLatestPropertyMutationByAssesmentNo(payPropertyTaxDetails.getAssessmentNo());
        propertyTaxBillable.setBasicProperty(basicProperty);
        ApplicationThreadLocals.setUserId(2L);
        propertyTaxBillable.setTransanctionReferenceNumber(payPropertyTaxDetails.getTransactionId());
        propertyTaxBillable.setMutationFeePayment(Boolean.TRUE);
        propertyTaxBillable.setMutationFee(payPropertyTaxDetails.getPaymentAmount());
        propertyTaxBillable.setCallbackForApportion(Boolean.FALSE);
        if(propertyMutation != null)
        	propertyTaxBillable.setMutationApplicationNo(propertyMutation.getApplicationNo());
        propertyTaxBillable.setUserId(ApplicationThreadLocals.getUserId());
        propertyTaxBillable.setReferenceNumber(propertyTaxNumberGenerator.generateManualBillNumber(basicProperty.getPropertyID()));
        
        final EgBill egBill = ptBillServiceImpl.generateBill(propertyTaxBillable);
        final CollectionHelper collectionHelper = new CollectionHelper(egBill);
        final Map<String, String> paymentDetailsMap = new HashMap<String, String>();
        paymentDetailsMap.put(TOTAL_AMOUNT, payPropertyTaxDetails.getPaymentAmount().toString());
        paymentDetailsMap.put(PAID_BY, egBill.getCitizenName());
        if (THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(payPropertyTaxDetails
                .getPaymentMode().toLowerCase())
                || THIRD_PARTY_PAYMENT_MODE_DD.equalsIgnoreCase(payPropertyTaxDetails
                        .getPaymentMode().toLowerCase())) {
            paymentDetailsMap.put(ChequePayment.INSTRUMENTNUMBER, payPropertyTaxDetails.getChqddNo());
            paymentDetailsMap.put(ChequePayment.INSTRUMENTDATE,
                    ChequePayment.CHEQUE_DATE_FORMAT.format(payPropertyTaxDetails.getChqddDate()));
            paymentDetailsMap.put(ChequePayment.BRANCHNAME, payPropertyTaxDetails.getBranchName());
            final Long validatesBankId = validateBank(payPropertyTaxDetails.getBankName());
            paymentDetailsMap.put(ChequePayment.BANKID, validatesBankId.toString());
        }
        final Payment payment = Payment.create(payPropertyTaxDetails.getPaymentMode().toLowerCase(), paymentDetailsMap);
        collectionHelper.setIsMutationFeePayment(true);
        final BillReceiptInfo billReceiptInfo = collectionHelper.executeCollection(payment,
                payPropertyTaxDetails.getSource());

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
            receiptDetails.setPaymentAmount(billReceiptInfo.getTotalAmount());
            receiptDetails.setPaymentMode(payPropertyTaxDetails.getPaymentMode());
            receiptDetails.setTransactionId(billReceiptInfo.getManualReceiptNumber());
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);

            receiptDetails.setErrorDetails(errorDetails);
        }
        return receiptDetails;
    }
    
    /**
     * Validate the payment amount entered for mutation
     * @param assessmentNo
     * @param paymentAmount
     * @return boolean
     */
    public boolean validateMutationFee(String assessmentNo, BigDecimal paymentAmount) {
        boolean validFee = true;
        PropertyMutation propertyMutation = getLatestPropertyMutationByAssesmentNo(assessmentNo);
        if (propertyMutation != null) {
            if (paymentAmount.compareTo(propertyMutation.getMutationFee()) > 0) {
                validFee = false;
            }
        } else {
            validFee = false;
        }
        return validFee;
    }
    
    /**
     * Fetch PropertyMutation for given assessmentNo and applicationNo
     * @param assessmentNo
     * @param applicationNo
     * @return PropertyMutation
     */
    public PropertyMutation getPropertyMutationByAssesmentNoAndApplicationNo(String assessmentNo, String applicationNo) {
        PropertyMutation propertyMutation = propertyMutationDAO
                .getPropertyMutationForAssessmentNoAndApplicationNumber(assessmentNo, applicationNo);
        return propertyMutation;
    }
    
    /**
     * Fetch PropertyMutation for given assessmentNo
     * @param assessmentNo
     * @return PropertyMutation
     */
    public PropertyMutation getLatestPropertyMutationByAssesmentNo(String assessmentNo){
        PropertyMutation propertyMutation = propertyMutationDAO.getPropertyLatestMutationForAssessmentNo(assessmentNo);
        return propertyMutation;
    }
    
    /**
     * API provides List of ward-block-locality mapping for Revenue Wards
     * @return List
     */
    public List<Object[]> getWardBlockLocalityMapping(){
    	StringBuilder queryString = new StringBuilder();
        queryString.append("select parent.parent.boundaryNum as wardnum, parent.parent.name as wardname, parent.boundaryNum as blocknum,");
        queryString.append(" parent.name as blockname, child.boundaryNum as localitynum, child.name as localityname");
        queryString.append(" from CrossHierarchy ch, Boundary parent, Boundary child");
        queryString.append(" where ch.parent.id = parent.id  and ch.child.id = child.id  and ch.parentType.name=:block and");
        queryString.append(" ch.childType.name=:locality and parent.parent.boundaryType.hierarchyType.name=:hierarchyType");
    	List<Object[]> boundaryDetails = entityManager.unwrap(Session.class).createQuery(queryString.toString())
    			.setParameter("block", BLOCK)
    			.setParameter("locality", LOCALITY_BNDRY_TYPE)
    			.setParameter("hierarchyType", REVENUE_HIERARCHY_TYPE).list();
    	return boundaryDetails;
    }
    
    /**
     * API provides ward-wise property details
     * @param ulbCode
     * @param wardNum
     * @return List
     */
    public List<ViewPropertyDetails> getPropertyDetailsForTheWard(String wardNum) {
        Boundary ward = getBoundaryByNumberAndType(wardNum, WARD, REVENUE_HIERARCHY_TYPE);
        List<ViewPropertyDetails> propertyDetails = new ArrayList<>();
        List<BasicProperty> basicProperties = basicPropertyDAO.getActiveBasicPropertiesForWard(ward.getId());
        if (!basicProperties.isEmpty()) {
            ViewPropertyDetails viewPropDetails;
            for (BasicProperty basicProperty : basicProperties) {
                viewPropDetails = new ViewPropertyDetails();
                prepareProperyDetailsInfo(basicProperty, viewPropDetails);
                propertyDetails.add(viewPropDetails);
            }
        }
        return propertyDetails;
    }

    /**
     * API to set each property details
     * @param basicProperty
     * @param viewPropertyDetails
     */
    private void prepareProperyDetailsInfo(BasicProperty basicProperty, ViewPropertyDetails viewPropertyDetails) {
        Property property = basicProperty.getProperty();
        viewPropertyDetails.setOldAssessmentNumber(basicProperty.getOldMuncipalNum());
        viewPropertyDetails.setAssessmentNumber(basicProperty.getUpicNo());
        viewPropertyDetails
                .setCategory(basicProperty.getProperty().getPropertyDetail().getPropertyTypeMaster().getType());
        PropertyID propertyID = basicProperty.getPropertyID();
        if (property != null) {
            PropertyDetail propertyDetail = property.getPropertyDetail();
            viewPropertyDetails.setExemption(
                    property.getTaxExemptedReason() == null ? NOT_AVAILABLE : property.getTaxExemptedReason().getName());
            Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
            if (ptDemand != null) {
                if (ptDemand.getDmdCalculations() != null && ptDemand.getDmdCalculations().getAlv() != null)
                    viewPropertyDetails.setArv(ptDemand.getDmdCalculations().getAlv());
                else
                    viewPropertyDetails.setArv(BigDecimal.ZERO);
            }
            populatePropertyDetails(basicProperty, viewPropertyDetails, propertyID, propertyDetail);
        }

        populateOwnerAndAddressDetails(basicProperty, viewPropertyDetails, propertyID);
    }

    /**
     * API to populate owner and address details
     * @param basicProperty
     * @param viewPropertyDetails
     * @param ownerAddress
     * @param propertyID
     */
    public void populateOwnerAndAddressDetails(BasicProperty basicProperty, ViewPropertyDetails viewPropertyDetails,
            PropertyID propertyID) {
        if (!basicProperty.getPropertyOwnerInfo().isEmpty()) {
            for (PropertyOwnerInfo propOwner : basicProperty.getPropertyOwnerInfo()) {
                List<Address> addrSet = propOwner.getOwner().getAddress();
                for (final Address address : addrSet) {
                    viewPropertyDetails
                            .setDoorNo(address.getHouseNoBldgApt() == null ? NOT_AVAILABLE : address.getHouseNoBldgApt());
                    break;
                }
            }
            viewPropertyDetails.setOwnerDetails(getOwnerDetails(basicProperty));
        }
        viewPropertyDetails.setZoneName(propertyID.getZone().getName());
        viewPropertyDetails.setWardName(propertyID.getWard().getName());
        viewPropertyDetails.setBlockName(propertyID.getArea().getName());
        viewPropertyDetails.setLocalityName(propertyID.getLocality().getName());
        viewPropertyDetails.setElectionWardName(propertyID.getElectionBoundary().getName());
        viewPropertyDetails.setEnumerationBlockName(NOT_AVAILABLE);
    }

    /**
     * API to set property level details
     * @param basicProperty
     * @param viewPropertyDetails
     * @param propertyID
     * @param propertyDetail
     */
    public void populatePropertyDetails(BasicProperty basicProperty, ViewPropertyDetails viewPropertyDetails,
            PropertyID propertyID, PropertyDetail propertyDetail) {
        viewPropertyDetails.setEffectiveDate(DateUtils.getDefaultFormattedDate(basicProperty.getPropOccupationDate()));
        viewPropertyDetails
                .setPropertyTypeMaster(PropertyTaxConstants.PROPERTY_TYPE_CATEGORIES.get(propertyDetail.getCategoryType()));
        viewPropertyDetails.setApartmentCmplx(
                propertyDetail.getApartment() == null ? NOT_AVAILABLE : propertyDetail.getApartment().getName());
        viewPropertyDetails.setExtentOfSite(
                propertyDetail.getSitalArea() == null ? NOT_AVAILABLE : propertyDetail.getSitalArea().getArea().toString());
        viewPropertyDetails.setExtentAppartenauntLand(propertyDetail.getExtentAppartenauntLand() == null ? NOT_AVAILABLE
                : propertyDetail.getExtentAppartenauntLand().toString());
        viewPropertyDetails.setRegdDocNo(basicProperty.getRegdDocNo());
        if (basicProperty.getRegdDocDate() != null)
            viewPropertyDetails.setRegdDocDate(DateUtils.getDefaultFormattedDate(basicProperty.getRegdDocDate()));
        viewPropertyDetails.setMutationReason(propertyDetail.getPropertyMutationMaster().getMutationName());

        if (!propertyDetail.getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            getConstructionTypeDetails(viewPropertyDetails, propertyDetail);
            viewPropertyDetails.setFloorDetails(getFloorDetails(propertyDetail));
        } else {
            getVacantLandDetails(viewPropertyDetails, propertyDetail, propertyID);
        }
        final Query query = entityManager.createNamedQuery("DOCUMENT_TYPE_DETAILS_BY_ID");
        query.setParameter(1, propertyID.getBasicProperty().getId());
        List<DocumentTypeDetails> docTypeDetailsList = query.getResultList();
        if (!docTypeDetailsList.isEmpty()) {
            DocumentTypeDetails docTypeDetails = docTypeDetailsList.get(0);
            if (docTypeDetails != null) {
                viewPropertyDetails.setDocType(docTypeDetails.getDocumentName());
                viewPropertyDetails.setMroProcNo(docTypeDetails.getProceedingNo());
                if (docTypeDetails.getProceedingDate() != null)
                    viewPropertyDetails.setMroProcDate(DateUtils.getDefaultFormattedDate(docTypeDetails.getProceedingDate()));
                viewPropertyDetails.setCourtName(docTypeDetails.getCourtName());
                viewPropertyDetails.setTwSigned(docTypeDetails.isSigned());
            }
        }
    }

    /**
     * API to set the construction details of the property
     * @param viewPropertyDetails
     * @param propertyDetail
     */
    private void getConstructionTypeDetails(ViewPropertyDetails viewPropertyDetails, PropertyDetail propertyDetail) {
        viewPropertyDetails.setFloorType(propertyDetail.getFloorType().getName());
        viewPropertyDetails.setRoofType(propertyDetail.getRoofType().getName());
        viewPropertyDetails
                .setWallType(propertyDetail.getWallType() == null ? NOT_AVAILABLE : propertyDetail.getWallType().getName());
        viewPropertyDetails
                .setWoodType(propertyDetail.getWoodType() == null ? NOT_AVAILABLE : propertyDetail.getWoodType().getName());
    }

    /**
     * API to set owner details
     * @param basicProperty
     * @return List
     */
    private List<OwnerInformation> getOwnerDetails(BasicProperty basicProperty) {
        List<OwnerInformation> ownerDetails = new ArrayList<>();
        OwnerInformation ownerInfo;
        User owner;
        for (PropertyOwnerInfo propOwnerInfo : basicProperty.getPropertyOwnerInfo()) {
            ownerInfo = new OwnerInformation();
            owner = propOwnerInfo.getOwner();
            ownerInfo.setAadhaarNo(owner.getAadhaarNumber());
            ownerInfo.setMobileNumber(owner.getMobileNumber());
            ownerInfo.setName(owner.getName());
            ownerInfo.setGender(owner.getGender().name());
            ownerInfo.setEmailId(owner.getEmailId());
            ownerInfo.setGuardianRelation(owner.getGuardianRelation());
            ownerInfo.setGuardian(owner.getGuardian());

            ownerDetails.add(ownerInfo);
        }
        return ownerDetails;
    }

    /**
     * API to set floor details
     * @param propertyDetail
     * @return List
     */
    private List<FloorDetails> getFloorDetails(PropertyDetail propertyDetail) {
        List<FloorDetails> floorDetails = new ArrayList<>();
        FloorDetails floorDet;
        for (Floor floor : propertyDetail.getFloorDetails()) {
            floorDet = new FloorDetails();
            floorDet.setFloorNoCode(FLOOR_MAP.get(floor.getFloorNo()));
            floorDet.setBuildClassificationCode(floor.getStructureClassification().getDescription());
            floorDet.setNatureOfUsageCode(floor.getPropertyUsage().getUsageName());
            floorDet.setFirmName(StringUtils.isBlank(floor.getFirmName()) ? NOT_AVAILABLE : floor.getFirmName());
            floorDet.setOccupancyCode(floor.getPropertyOccupation().getOccupation());
            floorDet.setOccupantName(StringUtils.isBlank(floor.getOccupantName()) ? NOT_AVAILABLE : floor.getOccupantName());
            floorDet.setConstructionDate(floor.getConstructionDate() == null ? NOT_AVAILABLE
                    : DateUtils.getDefaultFormattedDate(floor.getConstructionDate()));
            floorDet.setOccupancyDate(DateUtils.getDefaultFormattedDate(floor.getOccupancyDate()));
            floorDet.setPlinthLength(floor.getBuiltUpArea().getLength());
            floorDet.setPlinthBreadth(floor.getBuiltUpArea().getBreadth());
            floorDet.setPlinthArea(floor.getBuiltUpArea().getArea());
            floorDet.setBuildingPermissionNo(floor.getBuildingPermissionNo());
            floorDet.setBuildingPermissionDate(floor.getBuildingPermissionDate() == null ? NOT_AVAILABLE
                    : DateUtils.getDefaultFormattedDate(floor.getBuildingPermissionDate()));
            floorDet.setBuildingPlanPlinthArea(
                    floor.getBuildingPlanPlinthArea() == null ? 0.0F : floor.getBuildingPlanPlinthArea().getArea());
            floorDet.setUnitRate(floor.getFloorDmdCalc().getCategoryAmt().doubleValue());

            floorDetails.add(floorDet);
        }
        return floorDetails;
    }

    /**
     * API to set Vacant land details
     * @param viewPropertyDetails
     * @param propertyDetail
     * @param propertyID
     */
    private void getVacantLandDetails(ViewPropertyDetails viewPropertyDetails, PropertyDetail propertyDetail,
            PropertyID propertyID) {
        viewPropertyDetails.setSurveyNumber(propertyDetail.getSurveyNumber());
        viewPropertyDetails.setPattaNumber(propertyDetail.getPattaNumber());
        viewPropertyDetails.setVacantLandArea(propertyDetail.getSitalArea().getArea());
        viewPropertyDetails.setMarketValue(propertyDetail.getMarketValue());
        viewPropertyDetails.setCurrentCapitalValue(propertyDetail.getCurrentCapitalValue());
        viewPropertyDetails.setEffectiveDate(DateUtils.getDefaultFormattedDate(propertyDetail.getDateOfCompletion()));
        if (propertyDetail.getVacantLandPlotArea() != null)
            viewPropertyDetails.setVlPlotArea(propertyDetail.getVacantLandPlotArea().getName());
        if (propertyDetail.getLayoutApprovalAuthority() != null)
            viewPropertyDetails.setLaAuthority(propertyDetail.getLayoutApprovalAuthority().getName());
        viewPropertyDetails.setLpNo(propertyDetail.getLayoutPermitNo());
        if (propertyDetail.getLayoutPermitDate() != null)
            viewPropertyDetails.setLpDate(DateUtils.getDefaultFormattedDate(propertyDetail.getLayoutPermitDate()));
        viewPropertyDetails.setNorthBoundary(propertyID.getNorthBoundary());
        viewPropertyDetails.setEastBoundary(propertyID.getEastBoundary());
        viewPropertyDetails.setWestBoundary(propertyID.getWestBoundary());
        viewPropertyDetails.setSouthBoundary(propertyID.getSouthBoundary());
    }
    
    /**
     * Gives the count of properties for the given input criteria
     * @param transactionType
     * @param fromDate
     * @param toDate
     * @return
     * @throws ParseException
     */
    public Long getPropertiesCount(String transactionType, String fromDate, String toDate) throws ParseException{
    	StringBuilder queryString = new StringBuilder();
        queryString.append("select count(distinct prop.basicProperty.id) from PropertyImpl prop, PropertyMaterlizeView pmv ");
        queryString.append(" where prop.basicProperty.id = pmv.basicPropertyID and (cast(prop.createdDate as date)) between :fromDate and :toDate ");
        queryString.append(" and upper(prop.propertyModifyReason) like :modifyReason and prop.status in ('A','I') ");
        if(transactionType.equalsIgnoreCase(PropertyTaxConstants.TRANSACTION_TYPE_CREATE))
        	queryString.append(" and prop.demolitionReason is null ");
        else if(transactionType.equalsIgnoreCase(PropertyTaxConstants.TRANSACTION_TYPE_DEMOLITION))
        	queryString.append(" and prop.demolitionReason is not null ");
        final Query qry = entityManager.createQuery(queryString.toString());
      	qry.setParameter("fromDate", convertStringToDate(fromDate));
       	qry.setParameter("toDate", convertStringToDate(toDate));
       	qry.setParameter("modifyReason", "%".concat(transactionType.toUpperCase()).concat("%"));
    	return (Long) qry.getResultList().get(0);
    }
    
    /**
     * Gives details of the properties for the selected input criteria
     * @param transactionType
     * @param fromDate
     * @param toDate
     * @return
     * @throws ParseException
     */
    public List<SurveyAssessmentDetails> getPropertyDetailsForSurvey(String transactionType, String fromDate, String toDate) throws ParseException{
    	StringBuilder queryString = new StringBuilder();
    	List<SurveyAssessmentDetails> assessmentDetailsList = new ArrayList<>();
        queryString.append("select prop, pmv.houseNo, pmv.propertyAddress, pmv.sitalArea, (coalesce(pmv.arrearDemand,0)+coalesce(pmv.aggrArrearPenaly,0)+coalesce(pmv.aggrCurrFirstHalfPenaly,0)+");
        queryString.append("coalesce(pmv.aggrCurrSecondHalfPenaly,0)+coalesce(pmv.aggrCurrFirstHalfDmd,0)+coalesce(pmv.aggrCurrSecondHalfDmd,0)) from PropertyImpl prop, PropertyMaterlizeView pmv ");
        queryString.append(" where prop.basicProperty.id = pmv.basicPropertyID and (cast(prop.createdDate as date)) between :fromDate and :toDate ");
        queryString.append(" and upper(prop.propertyModifyReason) like :modifyReason and prop.status in ('A','I') ");
        if(transactionType.equalsIgnoreCase(PropertyTaxConstants.TRANSACTION_TYPE_CREATE))
        	queryString.append(" and prop.demolitionReason is null ");
        else if(transactionType.equalsIgnoreCase(PropertyTaxConstants.TRANSACTION_TYPE_DEMOLITION))
        	queryString.append(" and prop.demolitionReason is not null ");
        final Query qry = entityManager.createQuery(queryString.toString());
      	qry.setParameter("fromDate", convertStringToDate(fromDate));
       	qry.setParameter("toDate", convertStringToDate(toDate));
       	qry.setParameter("modifyReason", "%".concat(transactionType.toUpperCase()).concat("%"));
       	
    	List<Object[]> propertyDetails = qry.getResultList();
    	if(!propertyDetails.isEmpty()){
    		SurveyAssessmentDetails assessmentDetails;
    		for(Object[] objArr : propertyDetails){
    			assessmentDetails = new SurveyAssessmentDetails();
    			preparePropertyDetails(objArr, assessmentDetails);
    			assessmentDetailsList.add(assessmentDetails);
    		}
    	}
    	return assessmentDetailsList;
    }
    
    private void preparePropertyDetails(Object[] obj,SurveyAssessmentDetails assessmentDetails){
    	Property property = (Property) obj[0];
    	BasicProperty basicProperty = property.getBasicProperty();
    	assessmentDetails.setAssessmentNo(basicProperty.getUpicNo());
    	assessmentDetails.setDoorNo(obj[1].toString());
        assessmentDetails.setPropertyAddress(obj[2].toString());
        assessmentDetails.setPropertyType(property.getPropertyDetail().getPropertyTypeMaster().getType());
        assessmentDetails.setPropertyCategory(PropertyTaxConstants.PROPERTY_TYPE_CATEGORIES.get(property.getPropertyDetail().getCategoryType()));
        assessmentDetails.setAssessmentYear(DateUtils.toYearFormat(basicProperty.getPropOccupationDate()));
        assessmentDetails.setTotalTax(new BigDecimal(obj[4].toString()));
        assessmentDetails.setTotalSitalArea(new BigDecimal(obj[3].toString()));
        assessmentDetails.setOwnerDetails(getOwnerDetails(basicProperty));
    }
    
    /**
     * API to update property - used in Mobile App
     * @param viewPropertyDetails
     * @return NewPropertyDetails
     * @throws ParseException
     */
    public NewPropertyDetails updateProperty(ViewPropertyDetails viewPropertyDetails) throws ParseException{
    	NewPropertyDetails newPropertyDetails = null;
    	final PropertyService propService = beanProvider.getBean("propService", PropertyService.class);
    	BasicProperty basicProperty = updateBasicProperty(viewPropertyDetails, propService);
    	PropertyImpl property = (PropertyImpl)basicProperty.getWFProperty();
    	basicProperty.setUnderWorkflow(Boolean.TRUE);
    	transitionWorkFlow(property, propService,PROPERTY_MODE_MODIFY);
        basicPropertyService.applyAuditing(property.getState());
        propService.updateIndexes(property, PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT);
        if (basicProperty.getWFProperty() != null && basicProperty.getWFProperty().getPtDemandSet() != null
                && !basicProperty.getWFProperty().getPtDemandSet().isEmpty()) {
            for (Ptdemand ptDemand : basicProperty.getWFProperty().getPtDemandSet()) {
                basicPropertyService.applyAuditing(ptDemand.getDmdCalculations());
            }
        }
        basicProperty = basicPropertyService.update(basicProperty);
        if (basicProperty != null) {
            newPropertyDetails = new NewPropertyDetails();
            newPropertyDetails.setApplicationNo(basicProperty.getWFProperty().getApplicationNo());
            final ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(THIRD_PARTY_ERR_MSG_SUCCESS);
            newPropertyDetails.setErrorDetails(errorDetails);
        }
    	return newPropertyDetails;
    }
    
    /**
     * Updates the BasicProperty based on the input
     * @param viewPropertyDetails
     * @param propService
     * @return
     * @throws ParseException
     */
    private BasicProperty updateBasicProperty(ViewPropertyDetails viewPropertyDetails, PropertyService propService) throws ParseException{
    	BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(viewPropertyDetails.getAssessmentNumber());
    	PropertyImpl oldProperty = (PropertyImpl) basicProperty.getProperty();
    	PropertyImpl propertyImpl = createPropertyWithBasicDetails(viewPropertyDetails.getPropertyTypeMaster());
    	Date propCompletionDate = null;
        final PropertyTypeMaster propertyTypeMaster = getPropertyTypeMasterByCode(viewPropertyDetails.getPropertyTypeMaster());
        propertyImpl.getPropertyDetail().setAppurtenantLandChecked(viewPropertyDetails.getIsExtentAppurtenantLand());
        propertyImpl.getPropertyDetail().setEffectiveDate(convertStringToDate(viewPropertyDetails.getEffectiveDate()));
        if(StringUtils.isNotBlank(viewPropertyDetails.getApartmentCmplx())){
        	final Apartment apartment = getApartmentByCode(viewPropertyDetails.getApartmentCmplx());
            propertyImpl.getPropertyDetail().setApartment(apartment);
        }
        propertyImpl.getPropertyDetail().setOccupancyCertificationNo(viewPropertyDetails.getOccupancyCertificationNo());
        final PropertyMutationMaster propMutMstr = getPropertyMutationMaster(PROPERTY_MODIFY_REASON_ADD_OR_ALTER);
        basicProperty.setPropertyMutationMaster(propMutMstr);
        
        if(!propertyTypeMaster.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)){
        	final FloorType floorType = floorTypeService.getFloorTypeById(Long.valueOf(viewPropertyDetails.getFloorType()));
            final RoofType roofType = roofTypeService.getRoofTypeById(Long.valueOf(viewPropertyDetails.getRoofType()));
            WallType wallType = null;
            WoodType woodType = null;
            if(StringUtils.isNotBlank(viewPropertyDetails.getWallType()))
            	wallType = wallTypeService.getWallTypeById(Long.valueOf(viewPropertyDetails.getWallType()));
            if(StringUtils.isNotBlank(viewPropertyDetails.getWoodType()))
            	woodType = woodTypeService.getWoodTypeById(Long.valueOf(viewPropertyDetails.getWoodType()));
            
        	propertyImpl.getPropertyDetail().setFloorDetailsProxy(getFloorList(viewPropertyDetails.getFloorDetails()));
            propertyImpl.getPropertyDetail().setLift(viewPropertyDetails.getHasLift());
            propertyImpl.getPropertyDetail().setToilets(viewPropertyDetails.getHasToilet());
            propertyImpl.getPropertyDetail().setWaterTap(viewPropertyDetails.getHasWaterTap());
            propertyImpl.getPropertyDetail().setElectricity(viewPropertyDetails.getHasElectricity());
            propertyImpl.getPropertyDetail().setAttachedBathRoom(viewPropertyDetails.getHasAttachedBathroom());
            propertyImpl.getPropertyDetail().setWaterHarvesting(viewPropertyDetails.getHasWaterHarvesting());
            propertyImpl.getPropertyDetail().setCable(viewPropertyDetails.getHasCableConnection());
            
            propertyImpl.getPropertyDetail().setExtentSite(Double.valueOf(viewPropertyDetails.getExtentOfSite()));
            propertyImpl = propService.createProperty(propertyImpl, viewPropertyDetails.getExtentOfSite(), 
            		propMutMstr.getCode(),propertyTypeMaster.getId().toString(),
                    null, null, STATUS_WORKFLOW, propertyImpl.getDocNumber(), null, floorType != null ? floorType.getId() : null,
                    roofType != null ? roofType.getId() : null, wallType != null ? wallType.getId() : null,
                    woodType != null ? woodType.getId() : null, null, null, null, null);
        }else{
        	propertyImpl.getPropertyDetail().setDateOfCompletion(convertStringToDate(viewPropertyDetails.getEffectiveDate()));
        	propertyImpl.getPropertyDetail().setCurrentCapitalValue(viewPropertyDetails.getCurrentCapitalValue());
            propertyImpl.getPropertyDetail().setSurveyNumber(viewPropertyDetails.getSurveyNumber());
            propertyImpl.getPropertyDetail().setPattaNumber(viewPropertyDetails.getPattaNumber());
            final Area area = new Area();
            area.setArea(viewPropertyDetails.getVacantLandArea());
            propertyImpl.getPropertyDetail().setSitalArea(area);
            propertyImpl.getPropertyDetail().setMarketValue(viewPropertyDetails.getMarketValue());
            
            propertyImpl = propService.createProperty(propertyImpl, String.valueOf(viewPropertyDetails.getVacantLandArea()), 
            		propMutMstr.getCode(), propertyTypeMaster.getId().toString(),
                    null, null, STATUS_WORKFLOW, propertyImpl.getDocNumber(), null, null, null,
                    null, null, null, null, null, null);
        }
        if (!propertyTypeMaster.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyImpl.getPropertyDetail()
                    .getFloorDetailsProxy());
        else
            propCompletionDate = propertyImpl.getPropertyDetail().getDateOfCompletion();
        basicProperty.setPropOccupationDate(propCompletionDate);

        propertyImpl.setPropertyModifyReason(PROPERTY_MODIFY_REASON_ADD_OR_ALTER);
        propertyImpl.setBasicProperty(basicProperty);
        propertyImpl.setEffectiveDate(propCompletionDate);
        final Long oldPropTypeId = oldProperty.getPropertyDetail().getPropertyTypeMaster().getId();
        final PropertyTypeMaster vacantPropTypeMstr = propertyTypeMasterDAO.getPropertyTypeMasterByCode(OWNERSHIP_TYPE_VAC_LAND); 
        
        //if modifying from OPEN_PLOT to OTHERS or from OTHERS to OPEN_PLOT property type
        if ((oldPropTypeId == vacantPropTypeMstr.getId() && propertyTypeMaster.getId() != vacantPropTypeMstr.getId() || oldPropTypeId != vacantPropTypeMstr
                .getId() && propertyTypeMaster.getId() == vacantPropTypeMstr.getId())
                && !propertyImpl.getStatus().equals(STATUS_WORKFLOW))
            if (vacantPropTypeMstr != null
                    && vacantPropTypeMstr.getId() == propertyTypeMaster.getId())
                changePropertyDetail(propertyImpl, new VacantProperty(), 0);
            else
                changePropertyDetail(propertyImpl, new BuiltUpProperty(), propertyImpl.getPropertyDetail()
                        .getNoofFloors());

        Property modProperty = null;
        try {
            modProperty = propService.modifyDemand(propertyImpl, oldProperty);
            
        } catch (TaxCalculatorExeption e) {

        }

        if (modProperty != null && !modProperty.getDocuments().isEmpty())
            propService.processAndStoreDocument(modProperty.getDocuments());

        if (modProperty == null)
        	basicProperty.addProperty(propertyImpl);
        else
        	basicProperty.addProperty(modProperty);
    	
    	return basicProperty;
    }
    
    /**
     * Changes the property details to BuiltUpProperty or VacantProperty
     * @param modProperty - the property which is getting modified
     * @param propDetail - PropertyDetail type, either BuiltUpProperty or VacantProperty
     * @param numOfFloors - the no. of floors which is depending on PropertyDetail
     */
    private void changePropertyDetail(final Property modProperty, final PropertyDetail propDetail,
            final Integer numOfFloors) {

        final PropertyDetail propertyDetail = modProperty.getPropertyDetail();

        propDetail.setSitalArea(propertyDetail.getSitalArea());
        propDetail.setTotalBuiltupArea(propertyDetail.getTotalBuiltupArea());
        propDetail.setCommBuiltUpArea(propertyDetail.getCommBuiltUpArea());
        propDetail.setPlinthArea(propertyDetail.getPlinthArea());
        propDetail.setCommVacantLand(propertyDetail.getCommVacantLand());
        propDetail.setSurveyNumber(propertyDetail.getSurveyNumber());
        propDetail.setFieldVerified(propertyDetail.getFieldVerified());
        propDetail.setFieldVerificationDate(propertyDetail.getFieldVerificationDate());
        propDetail.setFloorDetails(propertyDetail.getFloorDetails());
        propDetail.setPropertyDetailsID(propertyDetail.getPropertyDetailsID());
        propDetail.setWater_Meter_Num(propertyDetail.getWater_Meter_Num());
        propDetail.setElec_Meter_Num(propertyDetail.getElec_Meter_Num());
        propDetail.setNoofFloors(numOfFloors);
        propDetail.setFieldIrregular(propertyDetail.getFieldIrregular());
        propDetail.setDateOfCompletion(propertyDetail.getDateOfCompletion());
        propDetail.setProperty(propertyDetail.getProperty());
        propDetail.setUpdatedTime(propertyDetail.getUpdatedTime());
        propDetail.setPropertyTypeMaster(propertyDetail.getPropertyTypeMaster());
        propDetail.setPropertyType(propertyDetail.getPropertyType());
        propDetail.setInstallment(propertyDetail.getInstallment());
        propDetail.setPropertyOccupation(propertyDetail.getPropertyOccupation());
        propDetail.setPropertyMutationMaster(propertyDetail.getPropertyMutationMaster());
        propDetail.setComZone(propertyDetail.getComZone());
        propDetail.setCornerPlot(propertyDetail.getCornerPlot());
        propDetail.setCable(propertyDetail.isCable());
        propDetail.setAttachedBathRoom(propertyDetail.isAttachedBathRoom());
        propDetail.setElectricity(propertyDetail.isElectricity());
        propDetail.setWaterTap(propertyDetail.isWaterTap());
        propDetail.setWaterHarvesting(propertyDetail.isWaterHarvesting());
        propDetail.setLift(propertyDetail.isLift());
        propDetail.setToilets(propertyDetail.isToilets());
        propDetail.setFloorType(propertyDetail.getFloorType());
        propDetail.setRoofType(propertyDetail.getRoofType());
        propDetail.setWallType(propertyDetail.getWallType());
        propDetail.setWoodType(propertyDetail.getWoodType());
        propDetail.setExtentSite(propertyDetail.getExtentSite());
        propDetail.setExtentAppartenauntLand(propertyDetail.getExtentAppartenauntLand());
        if (numOfFloors == 0)
            propDetail.setPropertyUsage(propertyDetail.getPropertyUsage());
        else
            propDetail.setPropertyUsage(null);
        propDetail.setManualAlv(propertyDetail.getManualAlv());
        propDetail.setOccupierName(propertyDetail.getOccupierName());

        modProperty.setPropertyDetail(propDetail);

    }

    public String getPropertyType(String assessmentno) {
        String pType = "";
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentno);
        PropertyTypeMaster ptypeMaster = basicProperty.getProperty().getPropertyDetail().getPropertyTypeMaster();
        if (ptypeMaster != null)
            pType = ptypeMaster.getCode();
        return pType;
    }
}
