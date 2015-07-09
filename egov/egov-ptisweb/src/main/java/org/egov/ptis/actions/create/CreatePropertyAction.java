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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.create;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.removeStart;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_CREATE_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE127;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE134;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.RESIDENTIAL_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_BILL_NOTCREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_YES_XML_MIGRATION;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_COMMISSIONER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REVENUE_OFFICER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.CorrespondenceAddress;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.actions.workflow.WorkflowAction;
import org.egov.ptis.client.util.FinancialUtil;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDetail;
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
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.egov.ptis.utils.OwnerNameComparator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.egov.infstr.utils.DateUtils;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;

/**
 * @author parvati
 *
 */
@SuppressWarnings("serial")
@ParentPackage("egov")
@Namespace("/create")
@ResultPath("/WEB-INF/jsp/")
@Results({ @Result(name = "new", location = "create/createProperty-new.jsp"),
		@Result(name = "ack", location = "create/createProperty-ack.jsp"),
		@Result(name = "view", location = "create/createProperty-view.jsp"),
		@Result(name = CreatePropertyAction.PRINTACK, location = "create/createProperty-printAck.jsp")})
public class CreatePropertyAction extends WorkflowAction {
	private static final String NO = "No";
	private static final String YES = "Yes";
	private static final String RESULT_ACK = "ack";
	private static final String RESULT_NEW = "new";
	private static final String RESULT_VIEW = "view";
	private static final String MSG_REJECT_SUCCESS = " Property Rejected Successfully ";
	private static final String CREATE = "create";

	private Logger LOGGER = Logger.getLogger(getClass());
	private PropertyImpl property = new PropertyImpl();
	@Autowired
	private PropertyPersistenceService basicPropertyService;
	private PersistenceService<Property, Long> propertyImplService;
	private Long zoneId;
	private Long wardId;
	private Long blockId;
	private Long electionWardId;
	private String wardName;
	private String zoneName;
	private String blockName;
	private Long locality;
	private Long floorTypeId;
	private Long roofTypeId;
	private Long wallTypeId;
	private Long woodTypeId;
	private Long ownershipType;
	private Double extentSite = null;
	private String vacantLandNo;
	private String extentAppartenauntLand = null;
	private String houseNumber;
	private String oldHouseNo;
	private String addressStr;
	private String pinCode;
	private String parcelID;
	private String areaOfPlot;
	private String applicationDate;
	private String dateOfCompletion;
	private String applicationNo;
	private TreeMap<Integer, String> floorNoMap;
	private boolean chkIsTaxExempted;
	private String taxExemptReason;
	private boolean chkIsCorrIsDiff;
	private String corrAddress1;
	private String corrAddress2;
	private String corrPinCode;
	private boolean generalTax;
	private boolean sewerageTax;
	private boolean lightingTax;
	private boolean fireServTax;
	private boolean bigResdBldgTax;
	private boolean educationCess;
	private boolean empGuaCess;
	private String genWaterRate;
	private BasicProperty basicProp;
	private Map<String, String> waterMeterMap;
	@Autowired
	private PropertyService propService;
	private Map<String, String> amenitiesMap;
	private Long mutationId;
	private String parentIndex;
	private String isAuthProp;
	Date propCompletionDate = null;
	private String amenities;
	private String[] floorNoStr = new String[20];
	private String propTypeId;
	private String propUsageId;
	private String propOccId;
	private String ackMessage;
	private Map<Long, String> ZoneBndryMap;
	private List<PropertyOwnerInfo> propOwnerProxy;
	private Map<String, String> propTypeCategoryMap;
	private String propTypeCategoryId;
	private String method;
	FinancialUtil financialUtil = new FinancialUtil();

	private String khasraNumber;
	private String mauza;
	private String citySurveyNumber;
	private String sheetNumber;
	private String floorName;

	private PropertyTypeMaster propTypeMstr;

	private String docNumber;
	private String partNo;
	private String nonResPlotArea;
	private Category propertyCategory;
	@Autowired
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

	private boolean isfloorDetailsRequired = true;
	private PropertyImpl propWF;// would be current property workflow obj
	private List<PropertyOwnerInfo> propertyOwnerProxy = new ArrayList<PropertyOwnerInfo>();
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private PropertyImpl newProperty = new PropertyImpl();
	private Date currDate;
	private Integer buildingPermissionNo;
	private Date buildingPermissionDate;
	private String regdDocNo;
	private Date regdDocDate;
	private String mode = CREATE;
	public static final String PRINTACK = "printAck";
	private ReportService reportService;
	private Integer reportId = -1;
	private boolean approved = false;
	
	@Autowired
	private UserService userService;

	private AssignmentService assignmentService;

	@Autowired
	private EisCommonService eisCommonService;

	@Autowired
	private BoundaryService boundaryService;

	@Autowired
	private SecurityUtils securityUtils;
	
	private ApplicationNumberGenerator applicationNumberGenerator;
	private static final String CREATE_ACK_TEMPLATE = "createProperty_ack";

	public CreatePropertyAction() {
		super();
		property.setPropertyDetail(new BuiltUpProperty());
		property.setBasicProperty(new BasicPropertyImpl());

		this.addRelatedEntity("propertyDetail.propertyTypeMaster", PropertyTypeMaster.class);
		this.addRelatedEntity("propertyDetail.floorDetails.unitType", PropertyTypeMaster.class);
		this.addRelatedEntity("propertyDetail.floorDetails.propertyUsage", PropertyUsage.class);
		this.addRelatedEntity("propertyDetail.floorDetails.propertyOccupation", PropertyOccupation.class);
		this.addRelatedEntity("propertyDetail.floorDetails.structureClassification", StructureClassification.class);
		this.addRelatedEntity("propertyOwnerInfo.owner", Citizen.class);

	}

	@Override
	public Object getModel() {
		return property;
	}

	@SkipValidation
	@Action(value = "/createProperty-newForm")
	public String newForm() {
		applicationNo = applicationNumberGenerator.generate();
		return RESULT_NEW;
	}

	@Action(value = "/createProperty-create")
	public String create() {
		LOGGER.debug("create: Property creation started, Property: " + property + ", zoneId: " + zoneId + ", wardId: "
				+ wardId + ", blockId: " + blockId + ", areaOfPlot: " + areaOfPlot + ", dateOfCompletion: "
				+ dateOfCompletion + ", chkIsTaxExempted: " + chkIsTaxExempted + ", taxExemptReason: "
				+ taxExemptReason + ", isAuthProp: " + isAuthProp + ", propTypeId: " + propTypeId + ", propUsageId: "
				+ propUsageId + ", propOccId: " + propOccId);
		long startTimeMillis = System.currentTimeMillis();
		BasicProperty basicProperty = createBasicProp(STATUS_ISACTIVE, isfloorDetailsRequired);
		LOGGER.debug("create: BasicProperty after creatation: " + basicProperty);
		basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
		processAndStoreDocumentsWithReason(basicProperty, DOCS_CREATE_PROPERTY);
		transitionWorkFlow(property);
		basicPropertyService.applyAuditing(property.getState());
		basicPropertyService.persist(basicProperty);
		setBasicProp(basicProperty);
		setAckMessage("Property Created Successfully in System with application number : " );
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		LOGGER.info("create: Property created successfully in system" + "; Time taken(ms) = " + elapsedTimeMillis);
		LOGGER.debug("create: Property creation ended");
		return RESULT_ACK;
	}

	@SkipValidation
	@Action(value = "/createProperty-view")
	public String view() {
		LOGGER.debug("Entered into view, BasicProperty: " + basicProp + ", Property: " + property + ", userDesgn: "
				+ userDesgn);
		String nextAction = property.getState().getNextAction();
		if (!nextAction.equals(WFLOW_ACTION_STEP_COMMISSIONER_APPROVED) && !nextAction.equals(WFLOW_ACTION_STEP_REVENUE_OFFICER_APPROVED)
				&& (ASSISTANT_DESGN.equalsIgnoreCase(userDesgn) || REVENUE_OFFICER_DESGN.equalsIgnoreCase(userDesgn))) {
			mode = EDIT;
			return RESULT_NEW;
		} else {
			mode = VIEW;
			PropertyDetail propertyDetail = property.getPropertyDetail();

			if (propertyDetail.getExtra_field4() != null && !propertyDetail.getExtra_field4().trim().isEmpty()) {
				setAmenities(CommonServices.getAmenitiesDtls(propertyDetail.getExtra_field4()));
			}

			if (propertyDetail.getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
					|| propertyDetail.getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)
					|| propertyDetail.getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
				setGenWaterRate(CommonServices.getWaterMeterDtls(propertyDetail.getExtra_field1()));
			}

			if (propertyDetail.getFloorDetails().size() > 0) {
				setFloorDetails(property);
			}

			setDocNumber(property.getDocNumber());
			LOGGER.debug("IndexNumber: " + indexNumber + ", GenWaterRate: " + genWaterRate + ", Amenities: "
					+ amenities + "NoOfFloors: "
					+ ((getFloorDetails() != null) ? getFloorDetails().size() : "Floor list is NULL")
					+ " Exiting from view");
			return RESULT_VIEW;
		}
	}

	@SkipValidation
	@Action(value = "/createProperty-forward")
	public String forward() {
		LOGGER.debug("forward: Property forward started " + property);
		this.validate();
		if (hasErrors()) {
			mode = EDIT;
			return RESULT_NEW;
		}
		long startTimeMillis = System.currentTimeMillis();
		transitionWorkFlow(property);
		basicPropertyService.applyAuditing(property.getState());
		updatePropertyDetails();
		basicProp.addProperty(property);
		basicPropertyService.persist(basicProp);
		setDocNumber(getDocNumber());
		User approverUser = userService.getUserById(getWorkflowBean().getApproverUserId());
		setAckMessage("Property forwarded successfully to " + approverUser.getUsername() + " with application Number : ");
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		LOGGER.debug("forward: Property forwarded successfully to " + approverUser.getUsername()
				+ "; Time taken(ms) = " + elapsedTimeMillis);
		LOGGER.debug("forward: Property forward ended");
		return RESULT_ACK;
	}
	
	private void updatePropertyDetails() {
		if (property.getPropertyDetail().getApartment() != null	&& property.getPropertyDetail().getApartment().getId() != null) {
			Apartment apartment = (Apartment) basicPropertyService.find("From Apartment where id = ?",
					property.getPropertyDetail().getApartment().getId());
			property.getPropertyDetail().setApartment(apartment);
		} else {
			property.getPropertyDetail().setApartment(null);
		}
		updateBasicProperty(basicProp);
		if (!property.getPropertyDetail().getPropertyType().equals(VACANT_PROPERTY)) {
			for (Floor floor : property.getPropertyDetail().getFloorDetails()) {
				User user = securityUtils.getCurrentUser();
				if (floor.getId() == null) {
					floor.setPropertyDetail(property.getPropertyDetail());
					floor.setCreatedDate(new Date());
					floor.setModifiedDate(new Date());
					floor.setCreatedBy(user);
					floor.setModifiedBy(user);
				}
			}
		} else {
			property.getPropertyDetail().setFloorDetails(null);
		}
		
		int ownersCount = property.getBasicProperty().getPropertyOwnerInfo().size();
		for (PropertyOwnerInfo ownerInfo : property.getBasicProperty().getPropertyOwnerInfo()) {
			if (ownerInfo.getId() == null) {
				ownerInfo.setOrderNo(ownersCount++);
				ownerInfo.getOwner().setPassword("NOT SET");
				ownerInfo.getOwner().setUsername(ownerInfo.getOwner().getMobileNumber());
			}
		}
	}
    
	private void updateBasicProperty(BasicProperty basicProperty) {
		basicProperty.setRegdDocNo(getRegdDocNo());
		basicProperty.setRegdDocDate(getRegdDocDate());
		basicProperty.setVacantLandAssmtNo(getVacantLandNo());
		PropertyID propertyId = basicProperty.getPropertyID();
		//propertyId.setZone(zone);
		//basicProperty.setPropertyID(createPropertyID(basicProperty));
		//basicProperty.setAddress(createPropAddress());
	}  
	
	@SkipValidation
	@Action(value = "/createProperty-approve")
	public String approve() {
		LOGGER.debug("approve: Property approval started");
		LOGGER.debug("approve: Property: " + property);
		LOGGER.debug("approve: BasicProperty: " + basicProp);
		if (REVENUE_OFFICER_DESGN.equalsIgnoreCase(userDesgn)) {
			this.validate();
			if (hasErrors()) {
				mode = EDIT;
				return RESULT_NEW;
			}
			updatePropertyDetails();
		}
		property.setStatus(STATUS_ISACTIVE);
		String indexNum = propertyTaxNumberGenerator.generateIndexNumber();
		basicProp.setUpicNo(indexNum);
		if (property.getPropertyDetail().getApartment() != null	&& property.getPropertyDetail().getApartment().getId() != null) {
			Apartment apartment = (Apartment) basicPropertyService.find("From Apartment where id = ?",
					property.getPropertyDetail().getApartment().getId());
			property.getPropertyDetail().setApartment(apartment);
		} else {
			property.getPropertyDetail().setApartment(null);
		}
		approved = true;
		setWardId(basicProp.getPropertyID().getWard().getId());
		processAndStoreDocumentsWithReason(basicProp, DOCS_CREATE_PROPERTY);
		transitionWorkFlow(property);
		basicPropertyService.applyAuditing(property.getState());
		basicPropertyService.update(basicProp);
		setAckMessage("Property Approved Successfully by : " + userService.getUserById(securityUtils.getCurrentUser().getId()).getName() + " with assessment number : ");
		LOGGER.debug("approve: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
		LOGGER.debug("approve: Property approval ended");
		return RESULT_ACK;
	}

	@SkipValidation
	@Action(value = "/createProperty-reject")
	public String reject() {
		LOGGER.debug("reject: Property rejection started");
		if (REVENUE_OFFICER_DESGN.equalsIgnoreCase(userDesgn)) {
			this.validate();
			if (hasErrors()) {
				mode = EDIT;
				return RESULT_NEW;
			}
			updatePropertyDetails();
		}
		transitionWorkFlow(property);
		basicPropertyService.applyAuditing(property.getState());
		if (property.getPropertyDetail().getApartment() != null
				&& property.getPropertyDetail().getApartment().getId() != null) {
			Apartment apartment = (Apartment) basicPropertyService.find("From Apartment where id = ?",
					property.getPropertyDetail().getApartment().getId());
			property.getPropertyDetail().setApartment(apartment);
		} else {
			property.getPropertyDetail().setApartment(null);
		}
		basicPropertyService.persist(basicProp);
		setAckMessage(MSG_REJECT_SUCCESS + " and forwarded to initiator " + property.getCreatedBy().getUsername() +" with application No :");
		LOGGER.debug("reject: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
		LOGGER.debug("reject: Property rejection ended");

		return RESULT_ACK;
	}

	private void setFloorDetails(Property property) {
		LOGGER.debug("Entered into setFloorDetails, Property: " + property);
		List<Floor> flrDtSet = property.getPropertyDetail().getFloorDetails();
		int i = 0;
		for (Floor flr : flrDtSet) {
			floorNoStr[i] = (propertyTaxUtil.getFloorStr(flr.getFloorNo()));
			LOGGER.debug("setFloorDetails: floorNoStr[" + i + "]->" + floorNoStr[i]);
			i++;
		}
		LOGGER.debug("Exiting from setFloorDetails");
	}

	public List<Floor> getFloorDetails() {
		return new ArrayList<Floor>(property.getPropertyDetail().getFloorDetails());
	}

	@Override
	@SuppressWarnings("unchecked")
	@SkipValidation
	public void prepare() {

		LOGGER.debug("Entered into prepare, ModelId: " + getModelId() + ", PropTypeId: " + propTypeId + ", ZoneId: "
				+ zoneId + ", WardId: " + wardId);

		currDate = new Date();
		setUserInfo();
		if (isNotBlank(getModelId())) {
			property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
					Long.valueOf(getModelId()));
			basicProp = property.getBasicProperty();
			PropertyDetail propertyDetail = property.getPropertyDetail();
			if (propertyDetail != null) {
				setFloorTypeId(propertyDetail.getFloorType().getId());
				setWallTypeId(propertyDetail.getWallType().getId());
				setRoofTypeId(propertyDetail.getRoofType().getId());
				setWoodTypeId(propertyDetail.getWallType().getId());
				setPropTypeId(propertyDetail.getPropertyTypeMaster().getId().toString());
			}

			if (basicProp != null) {
				setApplicationNo(basicProp.getApplicationNo());
				setVacantLandNo(basicProp.getVacantLandAssmtNo());
				setRegdDocDate(basicProp.getRegdDocDate());
				setRegdDocNo(basicProp.getRegdDocNo());
				setMutationId(basicProp.getPropertyMutationMaster().getId());
				if (null != basicProp.getAddress()) {
					setHouseNumber(basicProp.getAddress().getHouseNoBldgApt());
					setAddressStr(basicProp.getAddress().getLandmark());
					setPinCode(basicProp.getAddress().getPinCode());
				}

				if (null != basicProp.getPropertyID()) {
					PropertyID propBoundary = basicProp.getPropertyID();
					if (null != propBoundary.getLocality().getId()) {
						setLocality(boundaryService.getBoundaryById(propBoundary.getLocality().getId()).getId());
					}
					if (null != propBoundary.getElectionBoundary()
							&& null != propBoundary.getElectionBoundary().getId()) {
						setElectionWardId(
								boundaryService.getBoundaryById(propBoundary.getElectionBoundary().getId()).getId());
					}
					if (null != propBoundary.getWard().getId()) {
						Boundary zone = propBoundary.getWard();
						setZoneId(boundaryService.getBoundaryById(zone.getId()).getId());
						setZoneName(zone.getName());
					}
					if (null != propBoundary.getWard().getId()) {
						Boundary ward = propBoundary.getWard();
						setWardId(boundaryService.getBoundaryById(ward.getId()).getId());
						setWardName(ward.getName());
					}
					if (null != propBoundary.getArea().getId()) {
						Boundary area = propBoundary.getArea();
						setBlockId(boundaryService.getBoundaryById(area.getId()).getId());
						setBlockName(area.getName());
					}
				}

				for (PropertyStatusValues statusValue : basicProp.getPropertyStatusValuesSet()) {
					setBuildingPermissionDate(statusValue.getBuildingPermissionDate());
					setBuildingPermissionNo(statusValue.getBuildingPermissionNo());
				}
			}
			LOGGER.debug("prepare: Property by ModelId: " + property);
			LOGGER.debug("prepare: BasicProperty on property: " + basicProp);
		}

		List<FloorType> floorTypeList = getPersistenceService().findAllBy("from FloorType order by name");
		List<RoofType> roofTypeList = getPersistenceService().findAllBy("from RoofType order by name");
		List<WallType> wallTypeList = getPersistenceService().findAllBy("from WallType order by name");
		List<WoodType> woodTypeList = getPersistenceService().findAllBy("from WoodType order by name");
		List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy(
				"from PropertyTypeMaster order by orderNo");
		List<PropertyOccupation> propOccList = getPersistenceService().findAllBy("from PropertyOccupation");
		List<PropertyMutationMaster> mutationList = getPersistenceService().findAllBy(
				"from PropertyMutationMaster pmm where pmm.type=?", PROP_CREATE_RSN);
		List<String> authPropList = new ArrayList<String>(0);
		List<PropertyUsage> usageList = getPersistenceService().findAllBy("from PropertyUsage order by usageName");

		List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");
		List<String> StructureList = getPersistenceService().findAllBy("from StructureClassification");
		List<String> apartmentsList = getPersistenceService().findAllBy("from Apartment order by name");

		List<Boundary> localityList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY,
				LOCATION_HIERARCHY_TYPE);
		List<Boundary> electionWardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
				ELECTIONWARD_BNDRY_TYPE, ELECTION_HIERARCHY_TYPE);

		authPropList.add(YES);
		authPropList.add(NO);
		List<String> noticeTypeList = new ArrayList<String>();
		noticeTypeList.add(NOTICE127);
		noticeTypeList.add(NOTICE134);
		addDropdownData("PropTypeMaster", propTypeList);
		addDropdownData("floorType", floorTypeList);
		addDropdownData("roofType", roofTypeList);
		addDropdownData("wallType", wallTypeList);
		addDropdownData("woodType", woodTypeList);
		addDropdownData("apartments", apartmentsList);

		StringBuilder unitTypeQuery = new StringBuilder().append("from PropertyTypeMaster where code in ('")
				.append(PROPTYPE_OPEN_PLOT).append("', '").append(PROPTYPE_RESD).append("', '")
				.append(PROPTYPE_NON_RESD).append("') order by orderNo");

		addDropdownData("UnitTypes", getPersistenceService().findAllBy(unitTypeQuery.toString()));
		addDropdownData("UsageList", Collections.EMPTY_LIST);
		addDropdownData("UsageList", usageList);
		addDropdownData("OccupancyList", propOccList);
		addDropdownData("StructureList", StructureList);
		addDropdownData("AgeFactorList", ageFacList);
		setWaterMeterMap(CommonServices.getWaterMeterMstr());
		addDropdownData("LocationList", Collections.EMPTY_LIST);
		addDropdownData("AuthPropList", authPropList);
		addDropdownData("NoticeTypeList", noticeTypeList);
		addDropdownData("MutationList", mutationList);
		addDropdownData("LocationFactorList", Collections.EMPTY_LIST);
		setAmenitiesMap(CommonServices.getAmenities());
		setFloorNoMap(CommonServices.floorMap());
		addDropdownData("localityList", localityList);
		addDropdownData("electionWardList", electionWardList);

		if (propTypeId != null && !propTypeId.trim().isEmpty() && !propTypeId.equals("-1")) {
			propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
					"from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
			if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
				setPropTypeCategoryMap(RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
			} else if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
				setPropTypeCategoryMap(NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
			} else if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
				setPropTypeCategoryMap(PropertyTaxConstants.OPEN_PLOT_PROPERTY_TYPE_CATEGORY);
			}

			else {
				setPropTypeCategoryMap(Collections.EMPTY_MAP);
			}
		} else {
			setPropTypeCategoryMap(Collections.EMPTY_MAP);
		}
		// tax exempted properties
		addDropdownData("taxExemptedList", CommonServices.getTaxExemptedList());
		setupWorkflowDetails();

		super.prepare();
		LOGGER.debug("prepare: PropTypeList: "
				+ ((propTypeList != null) ? propTypeList : "NULL")
				+ ", PropOccuList: "
				+ ((propOccList != null) ? propOccList : "NLL")
				+ ", MutationList: "
				+ ((mutationList != null) ? mutationList : "NULL")
				+ ", AgeFactList: "
				+ ((ageFacList != null) ? ageFacList : "NULL")
				+ "UsageList: "
				+ ((getDropdownData().get("UsageList") != null) ? getDropdownData().get("UsageList") : "List is NULL")
				+ ", TaxExemptedReasonList: "
				+ ((getDropdownData().get("taxExemptedList") != null) ? getDropdownData().get("taxExemptedList")
						: "List is NULL"));

		LOGGER.debug("Exiting from prepare");
	}

	private BasicProperty createBasicProp(Character status, boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createBasicProp, Property: " + property + ", status: " + status + ", ParcelId: "
				+ parcelID + ", wardId: " + wardId);

		BasicProperty basicProperty = new BasicPropertyImpl();
		PropertyStatus propStatus = (PropertyStatus) getPersistenceService().find(
				"from PropertyStatus where statusCode=?", PROPERTY_STATUS_WORKFLOW);

		basicProperty.setActive(Boolean.TRUE);
		basicProperty.setAddress(createPropAddress());
		basicProperty.setApplicationNo(getApplicationNo());
		basicProperty.setVacantLandAssmtNo(getVacantLandNo());
		basicProperty.setPropertyID(createPropertyID(basicProperty));
		basicProperty.setStatus(propStatus);
		basicProperty.setRegdDocDate(getRegdDocDate());
		basicProperty.setRegdDocNo(getRegdDocNo());
		PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService().find(
				"from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?", PROP_CREATE_RSN, mutationId);
		basicProperty.setPropertyMutationMaster(propertyMutationMaster);
		basicProperty.addPropertyStatusValues(propService.createPropStatVal(basicProperty, "CREATE", null, null, null,
				null, getParentIndex(), getBuildingPermissionDate(), getBuildingPermissionNo()));
		basicProperty.setBoundary(boundaryService.getBoundaryById(getWardId()));
		basicProperty.setIsBillCreated(STATUS_BILL_NOTCREATED);
		createOwners(basicProperty);
		property.setBasicProperty(basicProperty);

		/*
		 * isfloorDetailsRequired is used to check if floor details have to be
		 * entered for State Govt property or not if isfloorDetailsRequired -
		 * true : no floor details created false : floor details created
		 */
		property = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(), propTypeId,
				propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(), isfloorDetailsRequired,
				getFloorTypeId(), getRoofTypeId(), getWallTypeId(), getWoodTypeId());
		property.setStatus(status);

		LOGGER.debug("createBasicProp: Property after call to PropertyService.createProperty: " + property);
		basicProperty.setExtraField1(isAuthProp);
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if ((property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT) || property
					.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT))
					&& isfloorDetailsRequired) {
				propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
			} else {
				propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail()
						.getFloorDetails());
			}
		} else {
			propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
		}
		Date lowestInstDate = propertyTaxUtil.getStartDateOfLowestInstallment();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lowestInstDate);

		/*
		 * if (propCompletionDate.before(calendar.getTime())) {
		 * propCompletionDate = calendar.getTime(); }
		 */

		basicProperty.setPropOccupationDate(propService.getPropOccupatedDate(getDateOfCompletion()));

		if ((propTypeMstr != null) && propTypeMstr.getCode().equals(PROPTYPE_OPEN_PLOT)) {
			property.setPropertyDetail(changePropertyDetail());
		}

		property.getPropertyDetail().setEffective_date(calendar.getTime());
		basicProperty.addProperty(property);
		propService.createDemand(property, null, propCompletionDate, isfloorDetailsRequired);
		LOGGER.debug("BasicProperty: " + basicProperty + "\nExiting from createBasicProp");
		return basicProperty;
	}

	private PropertyImpl createPropertyDueToReject(Character status, boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createPropertyDueToReject, Property: " + property + ", status: " + status
				+ ", ParcelId: " + parcelID + ", wardId: " + wardId);
		PropertyImpl newProperty = new PropertyImpl();
		// saving partno by removing preceding zeros ("0")
		basicProp.setPartNo(removeStart(partNo, "0"));
		basicProp.setGisReferenceNo(getParcelID());
		basicProp.setAddress(createPropAddress());
		basicProp.setPropertyID(createPropertyID(basicProp));
		basicProp.getPropertyID();
		PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService().find(
				"from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?", PROP_CREATE_RSN, mutationId);
		basicProp.setPropertyMutationMaster(propertyMutationMaster);
		basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, "CREATE", null, null, null, null,
				getParentIndex(), null, null));
		basicProp.setBoundary(boundaryService.getBoundaryById(getWardId()));
		/*
		 * isfloorDetailsRequired is used to check if floor details have to be
		 * entered for State Govt property or not if isfloorDetailsRequired -
		 * true : no floor details created false : floor details created
		 */
		newProperty = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(),
				propTypeId, propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(),
				isfloorDetailsRequired, getFloorTypeId(), getRoofTypeId(), getWallTypeId(), getWoodTypeId());
		LOGGER.debug("createPropertyDueToReject: Property after call to PropertyService.createProperty: " + property);
		basicProp.setExtraField1(isAuthProp);
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if ((property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT) || property
					.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT))
					&& isfloorDetailsRequired) {
				propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
			} else {
				propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail()
						.getFloorDetails());
			}
		} else {
			propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
		}
		Date lowestInstDate = propertyTaxUtil.getStartDateOfLowestInstallment();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lowestInstDate);

		if (propCompletionDate.before(calendar.getTime())) {
			propCompletionDate = calendar.getTime();
		}

		basicProp.setPropOccupationDate(propCompletionDate);
		createOwners(basicProp);
		newProperty.setBasicProperty(basicProp);
		// newProperty.getCitizen().setMobileNumber(getMobileNo());
		// newProperty.getCitizen().setEmailId(getEmail());

		if ((propTypeMstr != null) && propTypeMstr.getCode().equals(PROPTYPE_OPEN_PLOT)) {
			newProperty.setPropertyDetail(changePropertyDetail());
		}

		newProperty.getPropertyDetail().setEffective_date(propCompletionDate);
		basicProp.addProperty(newProperty);
		// propService.createDemand(newProperty, null, propCompletionDate,
		// isfloorDetailsRequired);
		basicPropertyService.update(basicProp);

		LOGGER.debug("BasicProperty: " + basicProp + "\nExiting from createPropertyDueToReject");
		return newProperty;
	}

	/**
	 * Changes the property details from {@link BuiltUpProperty} to
	 * {@link VacantProperty}
	 * 
	 * @return vacant property details
	 * 
	 * @see org.egov.ptis.domain.entity.property.VacantProperty
	 * 
	 */

	private VacantProperty changePropertyDetail() {

		LOGGER.debug("Entered into changePropertyDetail, Property is Vacant land");

		PropertyDetail propertyDetail = property.getPropertyDetail();
		VacantProperty vacantProperty = new VacantProperty(propertyDetail.getSitalArea(),
				propertyDetail.getTotalBuiltupArea(), propertyDetail.getCommBuiltUpArea(),
				propertyDetail.getPlinthArea(), propertyDetail.getCommVacantLand(), propertyDetail.getNonResPlotArea(),
				false, propertyDetail.getSurveyNumber(), propertyDetail.getFieldVerified(),
				propertyDetail.getFieldVerificationDate(), propertyDetail.getFloorDetails(),
				propertyDetail.getPropertyDetailsID(), propertyDetail.getWater_Meter_Num(),
				propertyDetail.getElec_Meter_Num(), 0, propertyDetail.getFieldIrregular(),
				propertyDetail.getCompletion_year(), propertyDetail.getEffective_date(),
				propertyDetail.getDateOfCompletion(), propertyDetail.getProperty(), propertyDetail.getUpdatedTime(),
				propertyDetail.getPropertyUsage(), null, propertyDetail.getPropertyTypeMaster(),
				propertyDetail.getPropertyType(), propertyDetail.getInstallment(),
				propertyDetail.getPropertyOccupation(), propertyDetail.getPropertyMutationMaster(),
				propertyDetail.getComZone(), propertyDetail.getCornerPlot(), propertyDetail.getExtentSite(),
				propertyDetail.getExtentAppartenauntLand(), propertyDetail.getFloorType(),
				propertyDetail.getRoofType(), propertyDetail.getWallType(), propertyDetail.getWoodType(),
				propertyDetail.isLift(), propertyDetail.isToilets(), propertyDetail.isWaterTap(),
				propertyDetail.isStructure(), propertyDetail.isDrainage(), propertyDetail.isElectricity(),
				propertyDetail.isAttachedBathRoom(), propertyDetail.isWaterHarvesting(), propertyDetail.isCable(),
				propertyDetail.getSiteOwner());

		vacantProperty.setExtra_field1(propertyDetail.getExtra_field1());
		vacantProperty.setExtra_field2(propertyDetail.getExtra_field2());
		vacantProperty.setExtra_field3(propertyDetail.getExtra_field3());
		vacantProperty.setExtra_field4(propertyDetail.getExtra_field4());
		vacantProperty.setExtra_field5(propertyDetail.getExtra_field5());
		vacantProperty.setExtra_field6(propertyDetail.getExtra_field6());
		vacantProperty.setManualAlv(propertyDetail.getManualAlv());
		vacantProperty.setOccupierName(propertyDetail.getOccupierName());

		LOGGER.debug("Exiting from changePropertyDetail");
		return vacantProperty;
	}

	private void createOwners(BasicProperty basicProperty) {
		LOGGER.debug("Entered into createOwners, Property: " + property);

		LOGGER.debug("createOwners:  CorrAddress1: " + getCorrAddress1() + ", CorrAddress2: " + getCorrAddress2()
				+ ", CorrPinCode: " + getCorrPinCode());
		String addrStr1;
		String addrStr2;
		int orderNo = 0;
		for (PropertyOwnerInfo ownerInfo : property.getBasicProperty().getPropertyOwnerInfo()) {
			orderNo++;
			if (ownerInfo != null) {
				ownerInfo.setOrderNo(orderNo);
				ownerInfo.getOwner().setPassword("NOT SET");
				ownerInfo.getOwner().setUsername(ownerInfo.getOwner().getMobileNumber());
				Address ownerAddr = new CorrespondenceAddress();
				addrStr1 = getCorrAddress1();
				addrStr2 = getCorrAddress2();
				addrStr1 = propertyTaxUtil.antisamyHackReplace(addrStr1);
				addrStr2 = propertyTaxUtil.antisamyHackReplace(addrStr2);
				ownerAddr.setLandmark(addrStr1);
				ownerAddr.setAreaLocalitySector(addrStr2);
				if (getCorrPinCode() != null && !getCorrPinCode().isEmpty()) {
					ownerAddr.setPinCode(getCorrPinCode());
				}
				LOGGER.debug("createOwners: OwnerAddress: " + ownerAddr);
				ownerInfo.getOwner().addAddress(ownerAddr);
				ownerInfo.setBasicProperty(basicProperty);
			}
			basicProperty.addPropertyOwners(ownerInfo);
		}
	}

	private PropertyAddress createPropAddress() {
		/*
		 * LOGGER.debug("Entered into createPropAddress, \nAreaId: " +
		 * getBlockId() + ", House Number: " + getHouseNumber() +
		 * ", OldHouseNo: " + getOldHouseNo() + ", AddressStr: " +
		 * getAddressStr() + ", MobileNo: " + getMobileNo() + ", Email: " +
		 * getEmail() + ", PinCode: " + getPinCode() + ", KhasraNumber: " +
		 * getKhasraNumber() + ", Mauza:" + getMauza() + ", CitySurveyNumber: "
		 * + getCitySurveyNumber() + ", SheetNumber:" + getSheetNumber());
		 */

		Address propAddr = new PropertyAddress();
		StringBuffer addrStr1 = new StringBuffer();
		StringBuffer addrStr2 = new StringBuffer();
		propAddr.setHouseNoBldgApt(getHouseNumber());
		addrStr1.append(getHouseNumber());

		if (getAddressStr() != null && !getAddressStr().isEmpty()) {
			String addressStr = getAddressStr();
			addressStr = propertyTaxUtil.antisamyHackReplace(addressStr);
			propAddr.setLandmark(addressStr);
			addrStr1.append(", " + addressStr);
		}

		addrStr2.append(boundaryService.getBoundaryById(getBlockId()).getName());

		if (getPinCode() != null && !getPinCode().isEmpty()) {
			propAddr.setPinCode((getPinCode()));
		}
		if (!isChkIsCorrIsDiff()) {
			setCorrAddress1(addrStr1.toString());
			setCorrAddress2(addrStr2.toString());
			if (getPinCode() != null && !getPinCode().isEmpty()) {
				setCorrPinCode(getPinCode());
			}

		}
		LOGGER.debug("PropertyAddress: " + propAddr + "\nExiting from createPropAddress");
		return (PropertyAddress) propAddr;
	}

	private PropertyID createPropertyID(BasicProperty basicProperty) {
		PropertyID propertyId = new PropertyID();
		propertyId.setZone(boundaryService.getBoundaryById(getZoneId()));
		propertyId.setWard(boundaryService.getBoundaryById(getWardId()));
		propertyId.setElectionBoundary(boundaryService.getBoundaryById(getElectionWardId()));
		propertyId.setCreatedDate(new Date());
		propertyId.setModifiedDate(new Date());
		propertyId.setModifiedDate(new Date());
		propertyId.setArea(boundaryService.getBoundaryById(getBlockId()));
		propertyId.setLocality(boundaryService.getBoundaryById(getLocality()));
		propertyId.setBasicProperty(basicProperty);
		LOGGER.debug("PropertyID: " + propertyId + "\nExiting from createPropertyID");
		return propertyId;
	}

	@Override
	public void validate() {
		LOGGER.debug("Entered into validate\nZoneId: " + zoneId + ", WardId: " + wardId + ", AreadId: " + blockId
				+ ", HouseNumber: " + houseNumber + ", PinCode: " + pinCode + ", ParcelID:" + parcelID
				+ ", MutationId: " + mutationId + ", PartNo: " + partNo);
		
		if (isBlank(applicationNo)) {
			addActionError(getText("mandatory.applicationNo"));
		}
		if (isBlank(vacantLandNo)) {
			addActionError(getText("mandatory.vacantLandNo"));
		}
		if (locality == null || locality == -1) {
			addActionError(getText("mandatory.localityId"));
		}
		
		if (wardId == null || wardId == -1) {
			addActionError(getText("mandatory.ward"));
		} else if (!StringUtils.isBlank(houseNumber)) {
			validateHouseNumber(wardId, houseNumber, basicProp);
		} else {
			addActionError(getText("mandatory.doorNo"));
		}
		
		if (null == property.getPropertyDetail() && property.getPropertyDetail().getExtentAppartenauntLand() == 0.0) {
			addActionError(getText("mandatory.extentAppartenauntLand"));
		}
		
		for (PropertyOwnerInfo owner : property.getBasicProperty().getPropertyOwnerInfo()) {
			if (owner != null) {
				if (StringUtils.isBlank(owner.getOwner().getName())) {
					addActionError(getText("mandatory.ownerName"));
				}
				if (StringUtils.isBlank(owner.getOwner().getAadhaarNumber())) {
					addActionError(getText("mandatory.adharNo"));
				}
				if (StringUtils.isBlank(owner.getOwner().getMobileNumber())) {
					addActionError(getText("mandatory.mobilenumber"));
				}
				if (StringUtils.isBlank(owner.getOwner().getEmailId())) {
					addActionError(getText("mandatory.emailId"));
				}
			}
		}
		
		validateProperty(property, areaOfPlot, dateOfCompletion, chkIsTaxExempted,
				taxExemptReason, isAuthProp, propTypeId, propUsageId, propOccId, 
				isfloorDetailsRequired, null, floorTypeId, roofTypeId, wallTypeId, woodTypeId);
		
		if (chkIsCorrIsDiff) {
			if (isBlank(corrAddress1)) {
				addActionError(getText("mandatory.corr.addr1"));
			}
			if (isBlank(corrAddress2)) {
				addActionError(getText("mandatory.corr.addr2"));
			}
			if (isBlank(corrPinCode) && corrPinCode.length() < 6) {
				addActionError(getText("mandatory.corr.pincode.size"));
			}
		}
		super.validate();
	}


	private void setPropertyWfData() {
		property.setExtra_field1("");
		// property.setExtra_field2("");
		property.setExtra_field3("");
		property.setExtra_field4("");
		property.setExtra_field5("");
		setPartNo(basicProp.getPartNo());
		setParcelID(basicProp.getGisReferenceNo());
		if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			setDateOfCompletion(sdf.format(property.getPropertyDetail().getEffective_date()));
			if (property.getPropertyDetail().getNonResPlotArea() != null
					&& !property.getPropertyDetail().getNonResPlotArea().equals("")) {
				setNonResPlotArea(property.getPropertyDetail().getNonResPlotArea().getArea().toString());
			}
		}

		PropertyID propId = basicProp.getPropertyID();
		setZoneId(propId.getZone().getId());
		setWardId(propId.getWard().getId());
		setBlockId(propId.getArea().getId());

		setMutationId(basicProp.getPropertyMutationMaster().getId());
		PropertyAddress propAddress = basicProp.getAddress();
		setHouseNumber(propAddress.getHouseNoBldgApt());

		if (propAddress.getLandmark() != null) {
			setAddressStr(propAddress.getLandmark());
		}
		if (propAddress.getAreaLocalitySector() != null) {
			setAddressStr(propAddress.getAreaLocalitySector());
		}
		if (propAddress.getPinCode() != null) {
			setPinCode(propAddress.getPinCode().toString());
		}

		/*
		 * List<PropertyOwnerInfo> ownerSet = property.getPropertyOwnerInfo();
		 * if (ownerSet != null && !ownerSet.isEmpty()) { List propOwProxy = new
		 * ArrayList(); for (PropertyOwnerInfo owner : ownerSet) { List<Address>
		 * addrSet = owner.getOwner().getAddress(); for (Address address :
		 * addrSet) { if(address.getLandmark() != null) {
		 * setCorrAddress2(address.getLandmark()); }
		 * if(address.getAreaLocalitySector() != null) {
		 * setCorrAddress2(address.getAreaLocalitySector()); }
		 * if(address.getPinCode() != null) {
		 * setCorrPinCode(address.getPinCode().toString()); } break; }
		 * propOwProxy.add(owner); }
		 * property.setPropertyOwnerProxy(propOwProxy); }
		 */
		if (basicProp.getExtraField1() != null) {
			setIsAuthProp(basicProp.getExtraField1());
		}
		if (property.getPropertyDetail().getSitalArea() != null) {
			setAreaOfPlot(property.getPropertyDetail().getSitalArea().getArea().toString());
		}
		if (property.getPropertyDetail().getFloorDetails() != null
				&& property.getPropertyDetail().getFloorDetails().size() > 0) {
			// isfloorDetailsRequired is enable the check box to be marked if
			// floordetails present for State Govt property
			if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
				isfloorDetailsRequired = false;
			}
			List flrDetsProxy = new ArrayList();
			for (Floor floor : property.getPropertyDetail().getFloorDetails()) {
				flrDetsProxy.add(floor);
			}
			// property.getPropertyDetail().setFloorDetails(property.getPropertyDetail().getFloorDetails());
			property.getPropertyDetail().setFloorDetails(flrDetsProxy);
		} else {
			if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
				isfloorDetailsRequired = true;
			}
		}
		PropertyTypeMaster propertyType = property.getPropertyDetail().getPropertyTypeMaster();
		propTypeId = propertyType.getId().toString();
		if (propertyType.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
			setPropTypeCategoryMap(RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
		} else if (propertyType.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
			setPropTypeCategoryMap(NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
		} else if (propertyType.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			setPropTypeCategoryMap(PropertyTaxConstants.OPEN_PLOT_PROPERTY_TYPE_CATEGORY);
		} else {
			setPropTypeCategoryMap(Collections.EMPTY_MAP);
		}

		if (property.getPropertyDetail().getExtra_field5() != null) {
			setPropTypeCategoryId(property.getPropertyDetail().getExtra_field5().toString());
		}

		prepareUsageList(propTypeId);
		if (property.getPropertyDetail().getPropertyUsage() != null) {
			propUsageId = property.getPropertyDetail().getPropertyUsage().getId().toString();
		}
		if (property.getPropertyDetail().getPropertyOccupation() != null) {
			propOccId = property.getPropertyDetail().getPropertyOccupation().getId().toString();
		}
		setDocNumber(property.getDocNumber());
	}

	private void prepareUsageList(String propTypeId) {
		LOGGER.debug("Entered into prepareUsageList, PropTypeId: " + propTypeId);
		if (propTypeId == null || propTypeId.equals("-1")) {
			addDropdownData("UsageList", Collections.EMPTY_LIST);
		} else {
			addDropdownData("UsageList", CommonServices.usagesForPropType(Integer.parseInt(propTypeId)));
		}
		LOGGER.debug("Exiting from prepareUsageList");
	}

	@SkipValidation
	@Action(value="/createProperty-printAck")
	public String printAck(){
		PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
		HttpServletRequest request = ServletActionContext.getRequest();
		String url= WebUtils.extractRequestDomainURL(request, false);
		String imagePath = url.concat(PropertyTaxConstants.IMAGES_BASE_PATH).concat(ReportUtil.fetchLogo());
		PropertyAckNoticeInfo ackBean = new PropertyAckNoticeInfo();
		Map<String, Object> reportParams = new HashMap<String, Object>();
		ackBean.setOwnerName(ptisCacheMgr.buildOwnerFullName(basicProp));
		ackBean.setOwnerAddress(ptisCacheMgr.buildAddressFromAddress(basicProp.getAddress()));
		ackBean.setApplicationDate(basicProp.getCreatedDate());
		ackBean.setApplicationNo(basicProp.getApplicationNo());
		ackBean.setApprovedDate(property.getState().getCreatedDate().toDate());
		Date tempNoticeDate = DateUtils.add(property.getState().getCreatedDate().toDate(), Calendar.DAY_OF_MONTH, 15);
		ackBean.setNoticeDueDate(tempNoticeDate);
		reportParams.put("logoPath", imagePath);
		reportParams.put("loggedInUsername", propertyTaxUtil.getLoggedInUser(getSession()).getName());
		ReportRequest reportInput = new ReportRequest(CREATE_ACK_TEMPLATE,ackBean, reportParams);
		reportInput.setReportFormat(FileFormat.PDF);
		ReportOutput reportOutput = reportService.createReport(reportInput);  
		reportId = ReportViewerUtil.addReportToSession(reportOutput,getSession());
		return PRINTACK;
	}
	
	@Override
	public PropertyImpl getProperty() {
		return property;
	}

	@Override
	public void setProperty(PropertyImpl property) {
		this.property = property;
	}

	public void setPropertyImplService(PersistenceService<Property, Long> propertyImplService) {
		this.propertyImplService = propertyImplService;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public Long getBlockId() {
		return blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getOldHouseNo() {
		return oldHouseNo;
	}

	public void setOldHouseNo(String oldHouseNo) {
		this.oldHouseNo = oldHouseNo;
	}

	public String getAddressStr() {
		return addressStr;
	}

	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getParcelID() {
		return parcelID;
	}

	public void setParcelID(String parcelID) {
		this.parcelID = parcelID;
	}

	public String getAreaOfPlot() {
		return areaOfPlot;
	}

	public void setAreaOfPlot(String areaOfPlot) {
		this.areaOfPlot = areaOfPlot;
	}

	public String getDateOfCompletion() {
		return dateOfCompletion;
	}

	public void setDateOfCompletion(String dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
	}

	public TreeMap<Integer, String> getFloorNoMap() {
		return floorNoMap;
	}

	public void setFloorNoMap(TreeMap<Integer, String> floorNoMap) {
		this.floorNoMap = floorNoMap;
	}

	public boolean isChkIsTaxExempted() {
		return chkIsTaxExempted;
	}

	public void setChkIsTaxExempted(boolean chkIsTaxExempted) {
		this.chkIsTaxExempted = chkIsTaxExempted;
	}

	public String getTaxExemptReason() {
		return taxExemptReason;
	}

	public void setTaxExemptReason(String taxExemptReason) {
		this.taxExemptReason = taxExemptReason;
	}

	public boolean isChkIsCorrIsDiff() {
		return chkIsCorrIsDiff;
	}

	public void setChkIsCorrIsDiff(boolean chkIsCorrIsDiff) {
		this.chkIsCorrIsDiff = chkIsCorrIsDiff;
	}

	public String getCorrAddress1() {
		return corrAddress1;
	}

	public void setCorrAddress1(String corrAddress1) {
		this.corrAddress1 = corrAddress1;
	}

	public String getCorrAddress2() {
		return corrAddress2;
	}

	public void setCorrAddress2(String corrAddress2) {
		this.corrAddress2 = corrAddress2;
	}

	public String getCorrPinCode() {
		return corrPinCode;
	}

	public void setCorrPinCode(String corrPinCode) {
		this.corrPinCode = corrPinCode;
	}

	public boolean isGeneralTax() {
		return generalTax;
	}

	public void setGeneralTax(boolean generalTax) {
		this.generalTax = generalTax;
	}

	public boolean isSewerageTax() {
		return sewerageTax;
	}

	public void setSewerageTax(boolean sewerageTax) {
		this.sewerageTax = sewerageTax;
	}

	public boolean isLightingTax() {
		return lightingTax;
	}

	public void setLightingTax(boolean lightingTax) {
		this.lightingTax = lightingTax;
	}

	public boolean isFireServTax() {
		return fireServTax;
	}

	public void setFireServTax(boolean fireServTax) {
		this.fireServTax = fireServTax;
	}

	public boolean isBigResdBldgTax() {
		return bigResdBldgTax;
	}

	public void setBigResdBldgTax(boolean bigResdBldgTax) {
		this.bigResdBldgTax = bigResdBldgTax;
	}

	public boolean isEducationCess() {
		return educationCess;
	}

	public void setEducationCess(boolean educationCess) {
		this.educationCess = educationCess;
	}

	public boolean isEmpGuaCess() {
		return empGuaCess;
	}

	public void setEmpGuaCess(boolean empGuaCess) {
		this.empGuaCess = empGuaCess;
	}

	@Override
	public String getGenWaterRate() {
		return genWaterRate;
	}

	@Override
	public void setGenWaterRate(String genWaterRate) {
		this.genWaterRate = genWaterRate;
	}

	public BasicProperty getBasicProp() {
		return basicProp;
	}

	public void setBasicProp(BasicProperty basicProp) {
		this.basicProp = basicProp;
	}

	public Map<String, String> getWaterMeterMap() {
		return waterMeterMap;
	}

	public void setWaterMeterMap(Map<String, String> waterMeterMap) {
		this.waterMeterMap = waterMeterMap;
	}

	/**
	 * This implementation transitions the <code>PropertyImpl</code> to the next
	 * workflow state.
	 */
	/*
	 * @Override protected PropertyImpl property() { return property; }
	 */

	public PropertyService getPropService() {
		return propService;
	}

	public void setPropService(PropertyService propService) {
		this.propService = propService;
	}

	public Map<String, String> getAmenitiesMap() {
		return amenitiesMap;
	}

	public void setAmenitiesMap(Map<String, String> amenitiesMap) {
		this.amenitiesMap = amenitiesMap;
	}

	public Long getMutationId() {
		return mutationId;
	}

	public void setMutationId(Long mutationId) {
		this.mutationId = mutationId;
	}

	public String getParentIndex() {
		return parentIndex;
	}

	public void setParentIndex(String parentIndex) {
		this.parentIndex = parentIndex;
	}

	public String getIsAuthProp() {
		return isAuthProp;
	}

	public void setIsAuthProp(String isAuthProp) {
		this.isAuthProp = isAuthProp;
	}

	public String getAmenities() {
		return amenities;
	}

	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}

	public String[] getFloorNoStr() {
		return floorNoStr;
	}

	public void setFloorNoStr(String[] floorNoStr) {
		this.floorNoStr = floorNoStr;
	}

	public String getPropTypeId() {
		return propTypeId;
	}

	public void setPropTypeId(String propTypeId) {
		this.propTypeId = propTypeId;
	}

	public String getPropUsageId() {
		return propUsageId;
	}

	public void setPropUsageId(String propUsageId) {
		this.propUsageId = propUsageId;
	}

	public String getPropOccId() {
		return propOccId;
	}

	public void setPropOccId(String propOccId) {
		this.propOccId = propOccId;
	}

	public String getAckMessage() {
		return ackMessage;
	}

	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
	}

	/*
	 * public Address getCorrAddress() { return
	 * property.getPropertyOwnerProxy().get(0).getAddress().iterator().next(); }
	 */

	public Map<Long, String> getZoneBndryMap() {
		return ZoneBndryMap;
	}

	public void setZoneBndryMap(Map<Long, String> ZoneBndryMap) {
		this.ZoneBndryMap = ZoneBndryMap;
	}

	@SuppressWarnings("unchecked")
	public List<PropertyOwnerInfo> getPropOwnerProxy() {
		Collections.sort(propOwnerProxy, new OwnerNameComparator());
		return propOwnerProxy;
	}

	public void setPropOwnerProxy(List<PropertyOwnerInfo> propOwnerProxy) {
		this.propOwnerProxy = propOwnerProxy;
	}

	public Map<String, String> getPropTypeCategoryMap() {
		return propTypeCategoryMap;
	}

	public void setPropTypeCategoryMap(Map<String, String> propTypeCategoryMap) {
		this.propTypeCategoryMap = propTypeCategoryMap;
	}

	public String getPropTypeCategoryId() {
		return propTypeCategoryId;
	}

	public void setPropTypeCategoryId(String propTypeCategoryId) {
		this.propTypeCategoryId = propTypeCategoryId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getKhasraNumber() {
		return khasraNumber;
	}

	public String getMauza() {
		return mauza;
	}

	public String getCitySurveyNumber() {
		return citySurveyNumber;
	}

	public String getSheetNumber() {
		return sheetNumber;
	}

	public void setKhasraNumber(String khasraNumber) {
		this.khasraNumber = khasraNumber;
	}

	public void setMauza(String mauza) {
		this.mauza = mauza;
	}

	public void setCitySurveyNumber(String citySurveyNumber) {
		this.citySurveyNumber = citySurveyNumber;
	}

	public void setSheetNumber(String sheetNumber) {
		this.sheetNumber = sheetNumber;
	}

	public PropertyTypeMaster getPropTypeMstr() {
		return propTypeMstr;
	}

	public void setPropTypeMstr(PropertyTypeMaster propTypeMstr) {
		this.propTypeMstr = propTypeMstr;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public Category getPropertyCategory() {
		return propertyCategory;
	}

	public void setPropertyCategory(Category propertyCategory) {
		this.propertyCategory = propertyCategory;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getNonResPlotArea() {
		return nonResPlotArea;
	}

	public void setNonResPlotArea(String nonResPlotArea) {
		this.nonResPlotArea = nonResPlotArea;
	}

	public boolean isIsfloorDetailsRequired() {
		return isfloorDetailsRequired;
	}

	public void setIsfloorDetailsRequired(boolean isfloorDetailsRequired) {
		this.isfloorDetailsRequired = isfloorDetailsRequired;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public List<PropertyOwnerInfo> getPropertyOwnerProxy() {
		return propertyOwnerProxy;
	}

	public void setPropertyOwnerProxy(List<PropertyOwnerInfo> propertyOwnerProxy) {
		this.propertyOwnerProxy = propertyOwnerProxy;
	}

	public PropertyImpl getNewProperty() {
		return newProperty;
	}

	public void setNewProperty(PropertyImpl newProperty) {
		this.newProperty = newProperty;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public Date getCurrDate() {
		return currDate;
	}

	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}

	public Double getExtentSite() {
		return extentSite;
	}

	public void setExtentSite(Double extentSite) {
		this.extentSite = extentSite;
	}

	public String getVacantLandNo() {
		return vacantLandNo;
	}

	public void setVacantLandNo(String vacantLandNo) {
		this.vacantLandNo = vacantLandNo;
	}

	public String getExtentAppartenauntLand() {
		return extentAppartenauntLand;
	}

	public void setExtentAppartenauntLand(String extentAppartenauntLand) {
		this.extentAppartenauntLand = extentAppartenauntLand;
	}

	public Long getLocality() {
		return locality;
	}

	public void setLocality(Long locality) {
		this.locality = locality;
	}

	public Long getFloorTypeId() {
		return floorTypeId;
	}

	public void setFloorTypeId(Long floorTypeId) {
		this.floorTypeId = floorTypeId;
	}

	public Long getRoofTypeId() {
		return roofTypeId;
	}

	public void setRoofTypeId(Long roofTypeId) {
		this.roofTypeId = roofTypeId;
	}

	public Long getWallTypeId() {
		return wallTypeId;
	}

	public void setWallTypeId(Long wallTypeId) {
		this.wallTypeId = wallTypeId;
	}

	public Long getWoodTypeId() {
		return woodTypeId;
	}

	public void setWoodTypeId(Long woodTypeId) {
		this.woodTypeId = woodTypeId;
	}

	public Long getOwnershipType() {
		return ownershipType;
	}

	public void setOwnershipType(Long ownershipType) {
		this.ownershipType = ownershipType;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Integer getBuildingPermissionNo() {
		return buildingPermissionNo;
	}

	public void setBuildingPermissionNo(Integer buildingPermissionNo) {
		this.buildingPermissionNo = buildingPermissionNo;
	}

	public Date getBuildingPermissionDate() {
		return buildingPermissionDate;
	}

	public void setBuildingPermissionDate(Date buildingPermissionDate) {
		this.buildingPermissionDate = buildingPermissionDate;
	}

	public String getRegdDocNo() {
		return regdDocNo;
	}

	public void setRegdDocNo(String regdDocNo) {
		this.regdDocNo = regdDocNo;
	}

	public Date getRegdDocDate() {
		return regdDocDate;
	}

	public void setRegdDocDate(Date regdDocDate) {
		this.regdDocDate = regdDocDate;
	}

	public AssignmentService getAssignmentService() {
		return assignmentService;
	}

	public void setAssignmentService(AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public Long getElectionWardId() {
		return electionWardId;
	}

	public void setElectionWardId(Long electionWardId) {
		this.electionWardId = electionWardId;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}

	public void setBoundaryService(BoundaryService boundaryService) {
		this.boundaryService = boundaryService;
	}

	public void setSecurityUtils(SecurityUtils securityUtils) {
		this.securityUtils = securityUtils;
	}

	public void setbasicPropertyService(PropertyPersistenceService basicPropertyService) {
		this.basicPropertyService = basicPropertyService;
	}

	public void setPropCompletionDate(Date propCompletionDate) {
		this.propCompletionDate = propCompletionDate;
	}

	public void setFinancialUtil(FinancialUtil financialUtil) {
		this.financialUtil = financialUtil;
	}

	public void setPropWF(PropertyImpl propWF) {
		this.propWF = propWF;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public ApplicationNumberGenerator getApplicationNumberGenerator() {
		return applicationNumberGenerator;
	}

	public void setApplicationNumberGenerator(ApplicationNumberGenerator applicationNumberGenerator) {
		this.applicationNumberGenerator = applicationNumberGenerator;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
}