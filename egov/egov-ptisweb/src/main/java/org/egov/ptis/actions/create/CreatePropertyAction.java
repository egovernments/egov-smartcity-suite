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
import static org.egov.ptis.constants.PropertyTaxConstants.DEVIATION_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_CREATE_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.GUARDIAN_RELATION;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_INSPECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_BILL_NOTCREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_YES_XML_MIGRATION;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.demand.dao.EgDemandDao;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.utils.DateUtils;
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
import org.egov.ptis.domain.entity.property.DocumentType;
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
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.TaxExeptionReason;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.egov.ptis.utils.OwnerNameComparator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author parvati
 *
 */
@SuppressWarnings("serial")
@ParentPackage("egov")
@Namespace("/create")
@ResultPath("/WEB-INF/jsp/")
@Results({
		@Result(name = "new", location = "create/createProperty-new.jsp"),
		@Result(name = "ack", location = "create/createProperty-ack.jsp"),
		@Result(name = "view", location = "create/createProperty-view.jsp"),
		@Result(name = CreatePropertyAction.PRINTACK, location = "create/createProperty-printAck.jsp") })
public class CreatePropertyAction extends WorkflowAction {
	private static final String RESULT_ACK = "ack";
	private static final String RESULT_NEW = "new";
	private static final String RESULT_VIEW = "view";
	private static final String MSG_REJECT_SUCCESS = " Property Rejected Successfully ";
	private static final String CREATE = "create";

	private Logger LOGGER = Logger.getLogger(getClass());
	private PropertyImpl property = new PropertyImpl();
	@Autowired
	private PropertyPersistenceService basicPropertyService;
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
	private Long mutationId;
	private String parentIndex;
	private String isAuthProp;
	Date propCompletionDate = null;
	private String amenities;
	private String[] floorNoStr = new String[20];
	private String propTypeId;
	private String propUsageId;
	private String propOccId;
	private Map<Long, String> ZoneBndryMap;
	private List<PropertyOwnerInfo> propOwnerProxy;
	private Map<String, String> propTypeCategoryMap;
	private String propTypeCategoryId;
	private String method;
	FinancialUtil financialUtil = new FinancialUtil();
	private PropertyTypeMaster propTypeMstr;

	private String docNumber;
	private String partNo;
	private String nonResPlotArea;
	private Category propertyCategory;
	@Autowired
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

	private PropertyImpl propWF;// would be current property workflow obj
	private List<PropertyOwnerInfo> propertyOwnerProxy = new ArrayList<PropertyOwnerInfo>();
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private PropertyImpl newProperty = new PropertyImpl();
	private Date currDate;
	private String regdDocNo;
	private Date regdDocDate;
	private String mode = CREATE;
	public static final String PRINTACK = "printAck";
	private ReportService reportService;
	private Integer reportId = -1;
	private boolean approved = false;
	private String northBoundary;
	private String southBoundary;
	private String eastBoundary;
	private String westBoundary;
	private Map<String, String> deviationPercentageMap;
	private Map<String, String> guardianRelationMap;
	private List<DocumentType> documentTypes = new ArrayList<>();

	@Autowired
	private UserService userService;

	private AssignmentService assignmentService;

	@Autowired
	private EisCommonService eisCommonService;

	@Autowired
	private BoundaryService boundaryService;

	@Autowired
	private SecurityUtils securityUtils;

	@Autowired
	private EgDemandDao egDemandDAO;
	
	private ApplicationNumberGenerator applicationNumberGenerator;
	private static final String CREATE_ACK_TEMPLATE = "createProperty_ack";

	public CreatePropertyAction() {
		super();
		property.setPropertyDetail(new BuiltUpProperty());
		property.setBasicProperty(new BasicPropertyImpl());
		this.addRelatedEntity("property", PropertyImpl.class);
		this.addRelatedEntity("property.propertyDetail.propertyTypeMaster", PropertyTypeMaster.class);
		this.addRelatedEntity("property.propertyDetail.floorDetails.unitType", PropertyTypeMaster.class);
		this.addRelatedEntity("property.propertyDetail.floorDetails.propertyUsage", PropertyUsage.class);
		this.addRelatedEntity("property.propertyDetail.floorDetails.propertyOccupation",
				PropertyOccupation.class);
		this.addRelatedEntity("property.propertyDetail.floorDetails.structureClassification",
				StructureClassification.class);
		this.addRelatedEntity("property.propertyDetail.floorDetails.taxExemptedReason",TaxExeptionReason.class);
		this.addRelatedEntity("property.basicProperty.propertyOwnerInfo.owner", Citizen.class);
		this.addRelatedEntity("property.propertyDetail.apartment", Apartment.class);
		addRelatedEntity("property.propertyDetail.floorType", FloorType.class);
		addRelatedEntity("property.propertyDetail.roofType", RoofType.class);
		addRelatedEntity("property.propertyDetail.wallType", WallType.class);
		addRelatedEntity("property.propertyDetail.woodType", WoodType.class);

	}

	@Override
	public StateAware getModel() {
		return property;
	}

	@SkipValidation
	@Action(value = "/createProperty-newForm")
	public String newForm() {
		return RESULT_NEW;
	}

	@Action(value = "/createProperty-create")
	public String create() {
		LOGGER.debug("create: Property creation started, Property: " + property + ", zoneId: "
				+ zoneId + ", wardId: " + wardId + ", blockId: " + blockId + ", areaOfPlot: "
				+ areaOfPlot + ", dateOfCompletion: " + dateOfCompletion + ", chkIsTaxExempted: "
				+ chkIsTaxExempted + ", taxExemptReason: " + taxExemptReason + ", isAuthProp: "
				+ isAuthProp + ", propTypeId: " + propTypeId + ", propUsageId: " + propUsageId
				+ ", propOccId: " + propOccId);
		long startTimeMillis = System.currentTimeMillis();
		BasicProperty basicProperty = createBasicProp(STATUS_ISACTIVE);
		LOGGER.debug("create: BasicProperty after creatation: " + basicProperty);
		basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
		processAndStoreDocumentsWithReason(basicProperty, DOCS_CREATE_PROPERTY);
		// this should be appending to messgae
		transitionWorkFlow(property);
		basicPropertyService.applyAuditing(property.getState());
		basicPropertyService.persist(basicProperty);
		setBasicProp(basicProperty);
		setAckMessage("Property Created Successfully in System with application number : ");
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		LOGGER.info("create: Property created successfully in system" + "; Time taken(ms) = "
				+ elapsedTimeMillis);
		LOGGER.debug("create: Property creation ended");
		return RESULT_ACK;
	}

	private void populateFormData() {
		PropertyDetail propertyDetail = property.getPropertyDetail();
		if (propertyDetail != null) {
		        if (propertyDetail.getFloorDetails().size() > 0) {
                              setFloorDetails(property);
                        }
			setPropTypeId(propertyDetail.getPropertyTypeMaster().getId().toString());
			if (propTypeId != null && !propTypeId.trim().isEmpty() && !propTypeId.equals("-1")) {
				propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
						"from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
				if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
					setPropTypeCategoryMap(VAC_LAND_PROPERTY_TYPE_CATEGORY);
				} else {
					setPropTypeCategoryMap(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
				}
			} 
			
			if (!propertyDetail.getPropertyType().equals(VACANT_PROPERTY)) {
				propertyDetail.setCategoryType(propertyDetail.getCategoryType());
				if (property.getPropertyDetail().getFloorType() != null) {
					floorTypeId = property.getPropertyDetail().getFloorType().getId();
				}
				if (property.getPropertyDetail().getRoofType() != null) {
					roofTypeId = property.getPropertyDetail().getRoofType().getId();
				}
				if (property.getPropertyDetail().getWallType() != null) {
					wallTypeId = property.getPropertyDetail().getWallType().getId();
				}
				if (property.getPropertyDetail().getWoodType() != null) {
					woodTypeId = property.getPropertyDetail().getWoodType().getId();
				}
				if (property.getPropertyDetail().getSitalArea() != null) {
					setAreaOfPlot(property.getPropertyDetail().getSitalArea().getArea().toString());
				}
			} 
		}

		if (basicProp != null) {
		        basicProp.setPropertyOwnerInfoProxy(basicProp.getPropertyOwnerInfo());
			setRegdDocDate(basicProp.getRegdDocDate());
			setRegdDocNo(basicProp.getRegdDocNo());
			setMutationId(basicProp.getPropertyMutationMaster().getId());
			if (null != basicProp.getAddress()) {
				setHouseNumber(basicProp.getAddress().getHouseNoBldgApt());
				setAddressStr(basicProp.getAddress().getLandmark());
				setPinCode(basicProp.getAddress().getPinCode());
			}

			for (PropertyOwnerInfo ownerInfo : basicProp.getPropertyOwnerInfo()) {
				for (Address corrAddress : ownerInfo.getOwner().getAddress()) {
					if (isNotBlank(corrAddress.getLandmark())) {
						setCorrAddress1(corrAddress.getLandmark());
						setCorrAddress2(corrAddress.getAreaLocalitySector());
						setCorrPinCode(corrAddress.getPinCode());
						chkIsCorrIsDiff = true;
					}
				}
			}
			if (null != basicProp.getPropertyID()) {
				PropertyID propBoundary = basicProp.getPropertyID();
				setNorthBoundary(propBoundary.getNorthBoundary());
				setSouthBoundary(propBoundary.getSouthBoundary());
				setEastBoundary(propBoundary.getEastBoundary());
				setWestBoundary(propBoundary.getWestBoundary());
				if (null != propBoundary.getLocality().getId()) {
					setLocality(boundaryService.getBoundaryById(
							propBoundary.getLocality().getId()).getId());
				}
				if (null != propBoundary.getElectionBoundary()
						&& null != propBoundary.getElectionBoundary().getId()) {
					setElectionWardId(boundaryService.getBoundaryById(
							propBoundary.getElectionBoundary().getId()).getId());
				}
				if (null != propBoundary.getZone().getId()) {
					Boundary zone = propBoundary.getZone();
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
		}
	}

	@SkipValidation
	@Action(value = "/createProperty-view")
	public String view() {
		LOGGER.debug("Entered into view, BasicProperty: " + basicProp + ", Property: " + property
				+ ", userDesgn: " + userDesgn);
		String currState = property.getState().getValue();
		populateFormData();
		if (currState.endsWith(WF_STATE_REJECTED) || REVENUE_INSPECTOR_DESGN.equalsIgnoreCase(userDesgn)) {
			//populateFormData();
			mode = EDIT;
			return RESULT_NEW;
		} else {
			mode = VIEW;
			setAddressStr(basicProp.getAddress().toString());
			corrAddress1 = basicProp.getAddress().toString();
			setDocNumber(property.getDocNumber());
			LOGGER.debug("IndexNumber: "
					+ indexNumber
					+ ", GenWaterRate: "
					+ genWaterRate
					+ ", Amenities: "
					+ amenities
					+ "NoOfFloors: "
					+ ((getFloorDetails() != null) ? getFloorDetails().size()
							: "Floor list is NULL") + " Exiting from view");
			return RESULT_VIEW;
		}
	}

	@SkipValidation
	@Action(value = "/createProperty-forward")
	public String forward() {
		if (mode.equalsIgnoreCase(EDIT)) {
			this.validate();
			if (hasErrors()) {
				return RESULT_NEW;
			}
			updatePropertyDetails();
		} else {
			validateApproverDetails();
			if (hasErrors()) {
				return RESULT_VIEW;
			}
		}
		transitionWorkFlow(property);

		if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction)) {
			return approve();
		} else if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
			return reject();
		}

		basicProp.setUnderWorkflow(true);
		basicPropertyService.applyAuditing(property.getState());
		basicProp.addProperty(property);
		basicPropertyService.persist(basicProp);
		LOGGER.debug("forward: Property forward started " + property);
		long startTimeMillis = System.currentTimeMillis();
		setDocNumber(getDocNumber());
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		LOGGER.debug("forward: Time taken(ms) = " + elapsedTimeMillis);
		LOGGER.debug("forward: Property forward ended");
		return RESULT_ACK;
	}

	public void updatePropertyDetails() {
		updatePropertyId(basicProp);
		//updateOwnerDetails(basicProp);
		final Character status = STATUS_WORKFLOW;
		//basicPropertyService.createUserIfNotExist(basicProp);
		basicPropertyService.createOwners(property,basicProp,corrAddress1,corrAddress2,corrPinCode);
		PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService()
                        .find("from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?",PROP_CREATE_RSN, mutationId);
                basicProp.setPropertyMutationMaster(propertyMutationMaster);
		property = propService.createProperty(property, getAreaOfPlot(),
                        propertyMutationMaster.getCode(), propTypeId, propUsageId, propOccId, status,
                        getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(),
                        getWallTypeId(), getWoodTypeId());
		//createOwners(basicProp);
		//createUserIfNotExist(basicProp);
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode()
				.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
			propCompletionDate = propService.getLowestDtOfCompFloorWise(property
					.getPropertyDetail().getFloorDetails());
		} else {
			propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
		}
		basicProp.setPropOccupationDate(propCompletionDate);

		if (property != null && !property.getDocuments().isEmpty()) {
			propService.processAndStoreDocument(property.getDocuments());
		}
		
		if ((propTypeMstr != null && propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))) {
			property.setPropertyDetail(propService.changePropertyDetail(property,property.getPropertyDetail(),0).getPropertyDetail());
		}
		property.setBasicProperty(basicProp);
		propService.createDemand(property, basicProp.getPropOccupationDate());
		
	}
	
	private void updatePropertyId(BasicProperty basicProperty) {
		PropertyID propertyId = basicProperty.getPropertyID();
		propertyId.setZone(boundaryService.getBoundaryById(getZoneId()));
		propertyId.setWard(boundaryService.getBoundaryById(getWardId()));
		propertyId.setElectionBoundary(boundaryService.getBoundaryById(getElectionWardId()));
		propertyId.setModifiedDate(new Date());
		propertyId.setModifiedDate(new Date());
		propertyId.setArea(boundaryService.getBoundaryById(getBlockId()));
		propertyId.setLocality(boundaryService.getBoundaryById(getLocality()));
		propertyId.setEastBoundary(getEastBoundary());
		propertyId.setWestBoundary(getWestBoundary());
		propertyId.setNorthBoundary(getNorthBoundary());
		propertyId.setSouthBoundary(getSouthBoundary());
	}
	
	@SkipValidation
	@Action(value = "/createProperty-approve")
	public String approve() {
		LOGGER.debug("approve: Property approval started");
		LOGGER.debug("approve: Property: " + property);
		LOGGER.debug("approve: BasicProperty: " + basicProp);
		property.setStatus(STATUS_ISACTIVE);
		String assessmentNo = propertyTaxNumberGenerator.generateAssessmentNumber();
		basicProp.setUpicNo(assessmentNo);
		basicProp.setUnderWorkflow(false);
		PropertyStatus propStatus = (PropertyStatus) getPersistenceService().find(
                        "from PropertyStatus where statusCode=?", PROPERTY_STATUS_APPROVED);
		basicProp.setStatus(propStatus);
		approved = true;
		setWardId(basicProp.getPropertyID().getWard().getId());
		processAndStoreDocumentsWithReason(basicProp, DOCS_CREATE_PROPERTY);
		basicPropertyService.applyAuditing(property.getState());
		basicPropertyService.update(basicProp);
		setAckMessage("Property Approved Successfully by : "
				+ userService.getUserById(securityUtils.getCurrentUser().getId()).getName()
				+ " with assessment number : ");
		LOGGER.debug("approve: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
		LOGGER.debug("approve: Property approval ended");
		return RESULT_ACK;
	}

	@SkipValidation
	@Action(value = "/createProperty-reject")
	public String reject() {
		LOGGER.debug("reject: Property rejection started");
		basicPropertyService.applyAuditing(property.getState());
		if (property.getPropertyDetail().getApartment() != null
				&& property.getPropertyDetail().getApartment().getId() != null) {
			Apartment apartment = (Apartment) basicPropertyService.find(
					"From Apartment where id = ?", property.getPropertyDetail().getApartment()
							.getId());
			property.getPropertyDetail().setApartment(apartment);
		} else {
			property.getPropertyDetail().setApartment(null);
		}
		basicProp.setUnderWorkflow(true);
		basicPropertyService.persist(basicProp);
		setAckMessage(MSG_REJECT_SUCCESS + " and forwarded to initiator "
				+ property.getCreatedBy().getUsername() + " with application No :");
		LOGGER.debug("reject: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
		LOGGER.debug("reject: Property rejection ended");

		return RESULT_ACK;
	}

	private void setFloorDetails(Property property) {
		LOGGER.debug("Entered into setFloorDetails, Property: " + property);
		List<Floor> floorList = property.getPropertyDetail().getFloorDetails();
		property.getPropertyDetail().setFloorDetailsProxy(floorList);
		int i = 0;
		for (Floor flr : floorList) {
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

		LOGGER.debug("Entered into prepare, ModelId: " + getModelId() + ", PropTypeId: "
				+ propTypeId + ", ZoneId: " + zoneId + ", WardId: " + wardId);

		currDate = new Date();
		setUserInfo();
		if (isNotBlank(getModelId())) {
			property = (PropertyImpl) getPersistenceService().findByNamedQuery(
					QUERY_PROPERTYIMPL_BYID, Long.valueOf(getModelId()));
			basicProp = property.getBasicProperty();
			LOGGER.debug("prepare: Property by ModelId: " + property);
			LOGGER.debug("prepare: BasicProperty on property: " + basicProp);
		}
		this.documentTypes = propService.getPropertyCreateDocumentTypes();
		List<FloorType> floorTypeList = getPersistenceService().findAllBy(
				"from FloorType order by name");
		List<RoofType> roofTypeList = getPersistenceService().findAllBy(
				"from RoofType order by name");
		List<WallType> wallTypeList = getPersistenceService().findAllBy(
				"from WallType order by name");
		List<WoodType> woodTypeList = getPersistenceService().findAllBy(
				"from WoodType order by name");
		List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy(
				"from PropertyTypeMaster order by orderNo");
		List<PropertyOccupation> propOccList = getPersistenceService().findAllBy(
				"from PropertyOccupation");
		List<PropertyMutationMaster> mutationList = getPersistenceService().findAllBy(
				"from PropertyMutationMaster pmm where pmm.type=?", PROP_CREATE_RSN);
		List<PropertyUsage> usageList = getPersistenceService().findAllBy(
				"from PropertyUsage order by usageName");

		List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");
		List<String> StructureList = getPersistenceService().findAllBy(
				"from StructureClassification");
		List<String> apartmentsList = getPersistenceService().findAllBy(
				"from Apartment order by name");
		List<String> taxExemptionReasonList = getPersistenceService().findAllBy(
				"from TaxExeptionReason order by name");

		List<Boundary> localityList = boundaryService
				.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY,
						LOCATION_HIERARCHY_TYPE);
		List<Boundary> electionWardList = boundaryService
				.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ELECTIONWARD_BNDRY_TYPE,
						ELECTION_HIERARCHY_TYPE);
		List<Boundary> enumerationBlockList = boundaryService
				.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ELECTIONWARD_BNDRY_TYPE,
						ELECTION_HIERARCHY_TYPE);

		setDeviationPercentageMap(DEVIATION_PERCENTAGE);
		setGuardianRelationMap(GUARDIAN_RELATION);
		addDropdownData("PropTypeMaster", propTypeList);
		addDropdownData("floorType", floorTypeList);
		addDropdownData("roofType", roofTypeList);
		addDropdownData("wallType", wallTypeList);
		addDropdownData("woodType", woodTypeList);
		addDropdownData("apartments", apartmentsList);

		addDropdownData("UsageList", usageList);
		addDropdownData("OccupancyList", propOccList);
		addDropdownData("StructureList", StructureList);
		addDropdownData("AgeFactorList", ageFacList);
		addDropdownData("MutationList", mutationList);
		addDropdownData("LocationFactorList", Collections.EMPTY_LIST);
		setFloorNoMap(CommonServices.floorMap());
		addDropdownData("localityList", localityList);
		addDropdownData("electionWardList", electionWardList);
		addDropdownData("enumerationBlockList", enumerationBlockList);
		addDropdownData("taxExemptionReasonList", taxExemptionReasonList);
		if (propTypeId != null && !propTypeId.trim().isEmpty() && !propTypeId.equals("-1")) {
			propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
					"from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
			if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
				setPropTypeCategoryMap(VAC_LAND_PROPERTY_TYPE_CATEGORY);
			} else {
				setPropTypeCategoryMap(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
			}
		} else {
			setPropTypeCategoryMap(Collections.EMPTY_MAP);
		}
		// tax exempted properties
		addDropdownData("taxExemptedList", CommonServices.getTaxExemptedList());

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
				+ ((getDropdownData().get("UsageList") != null) ? getDropdownData()
						.get("UsageList") : "List is NULL")
				+ ", TaxExemptedReasonList: "
				+ ((getDropdownData().get("taxExemptedList") != null) ? getDropdownData()
						.get("taxExemptedList") : "List is NULL"));

		LOGGER.debug("Exiting from prepare");
	}

	private BasicProperty createBasicProp(Character status) {
		LOGGER.debug("Entered into createBasicProp, Property: " + property + ", status: " + status
				+ ", ParcelId: " + parcelID + ", wardId: " + wardId);

		BasicProperty basicProperty = new BasicPropertyImpl();
		PropertyStatus propStatus = (PropertyStatus) getPersistenceService().find(
				"from PropertyStatus where statusCode=?", PROPERTY_STATUS_WORKFLOW);
		basicProperty.setRegdDocDate(property.getBasicProperty().getRegdDocDate());
		basicProperty.setRegdDocNo(property.getBasicProperty().getRegdDocNo());
		basicProperty.setActive(Boolean.TRUE);
		basicProperty.setApplicationNo(applicationNumberGenerator.generate());
		basicProperty.setAddress(createPropAddress());
		basicProperty.setPropertyID(createPropertyID(basicProperty));
		basicProperty.setStatus(propStatus);
		basicProperty.setUnderWorkflow(true);
		PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService()
				.find("from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?",
						PROP_CREATE_RSN, mutationId);
		basicProperty.setPropertyMutationMaster(propertyMutationMaster);
		basicProperty.addPropertyStatusValues(propService.createPropStatVal(basicProperty,
				"CREATE", null, null, null, null, getParentIndex()));
		basicProperty.setBoundary(boundaryService.getBoundaryById(getWardId()));
		basicProperty.setIsBillCreated(STATUS_BILL_NOTCREATED);
		basicPropertyService.createOwners(property,basicProperty,corrAddress1,corrAddress2,corrPinCode);
		property.setBasicProperty(basicProperty);

		/*
		 * isfloorDetailsRequired is used to check if floor details have to be
		 * entered for State Govt property or not if isfloorDetailsRequired -
		 * true : no floor details created false : floor details created
		 */
		property = propService.createProperty(property, getAreaOfPlot(),
				propertyMutationMaster.getCode(), propTypeId, propUsageId, propOccId, status,
				getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(),
				getWallTypeId(), getWoodTypeId());
		property.setStatus(status);

		LOGGER.debug("createBasicProp: Property after call to PropertyService.createProperty: "	+ property);
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode()
				.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
			propCompletionDate = propService.getLowestDtOfCompFloorWise(property
					.getPropertyDetail().getFloorDetails());
		} else {
			propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
		}
		basicProperty.setPropOccupationDate(propCompletionDate);

		if (property != null && !property.getDocuments().isEmpty()) {
			propService.processAndStoreDocument(property.getDocuments());
		}
		
		if ((propTypeMstr != null && propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))) {
			property.setPropertyDetail(changePropertyDetail());
		}
		basicProperty.addProperty(property);
		propService.createDemand(property, propCompletionDate);
		LOGGER.debug("BasicProperty: " + basicProperty + "\nExiting from createBasicProp");
		return basicProperty;
	}

	private PropertyImpl createPropertyDueToReject(Character status, boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createPropertyDueToReject, Property: " + property + ", status: "
				+ status + ", ParcelId: " + parcelID + ", wardId: " + wardId);
		PropertyImpl newProperty = new PropertyImpl();
		// saving partno by removing preceding zeros ("0")
		basicProp.setPartNo(removeStart(partNo, "0"));
		basicProp.setGisReferenceNo(getParcelID());
		basicProp.setAddress(createPropAddress());
		basicProp.setPropertyID(createPropertyID(basicProp));
		basicProp.getPropertyID();
		PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService()
				.find("from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?",
						PROP_CREATE_RSN, mutationId);
		basicProp.setPropertyMutationMaster(propertyMutationMaster);
		basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, "CREATE", null,
				null, null, null, getParentIndex()));
		basicProp.setBoundary(boundaryService.getBoundaryById(getWardId()));
		newProperty = propService.createProperty(property, getAreaOfPlot(),
				propertyMutationMaster.getCode(), propTypeId, propUsageId, propOccId, status,
				getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(),
				getWallTypeId(), getWoodTypeId());
		LOGGER.debug("createPropertyDueToReject: Property after call to PropertyService.createProperty: "
				+ property);
		basicProp.setExtraField1(isAuthProp);
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode()
				.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
			propCompletionDate = propService.getLowestDtOfCompFloorWise(property
					.getPropertyDetail().getFloorDetails());
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
		//createOwners(basicProp);
		newProperty.setBasicProperty(basicProp);

		if ((propTypeMstr != null) && propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND)) {
			newProperty.setPropertyDetail(changePropertyDetail());
		}

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
		VacantProperty vacantProperty = new VacantProperty(
				propertyDetail.getSitalArea(),
				propertyDetail.getTotalBuiltupArea(),
				propertyDetail.getCommBuiltUpArea(),
				propertyDetail.getPlinthArea(),
				propertyDetail.getCommVacantLand(),
				propertyDetail.getNonResPlotArea(),
				false,
				propertyDetail.getSurveyNumber(),
				propertyDetail.getFieldVerified(),
				propertyDetail.getFieldVerificationDate(),
				propertyDetail.getFloorDetails(),
				propertyDetail.getPropertyDetailsID(),
				propertyDetail.getWater_Meter_Num(),
				propertyDetail.getElec_Meter_Num(),
				0,
				propertyDetail.getFieldIrregular(),
				propertyDetail.getDateOfCompletion(),
				propertyDetail.getProperty(),
				propertyDetail.getUpdatedTime(),
				propertyDetail.getPropertyUsage(),
				null,
				propertyDetail.getPropertyTypeMaster(),
				propertyDetail.getPropertyType(),
				propertyDetail.getInstallment(),
				propertyDetail.getPropertyOccupation(),
				propertyDetail.getPropertyMutationMaster(),
				propertyDetail.getComZone(),
				propertyDetail.getCornerPlot(),
				propertyDetail.getExtentSite() != null ? propertyDetail
						.getExtentSite() : 0.0,
				propertyDetail.getExtentAppartenauntLand() != null ? propertyDetail
						.getExtentAppartenauntLand() : 0.0,
				propertyDetail.getFloorType(), propertyDetail.getRoofType(),
				propertyDetail.getWallType(), propertyDetail.getWoodType(),
				propertyDetail.isLift(), propertyDetail.isToilets(),
				propertyDetail.isWaterTap(), propertyDetail.isStructure(),
				propertyDetail.isElectricity(),
				propertyDetail.isAttachedBathRoom(),
				propertyDetail.isWaterHarvesting(), propertyDetail.isCable(),
				propertyDetail.getSiteOwner(), propertyDetail.getPattaNumber(),
				propertyDetail.getCurrentCapitalValue(),
				propertyDetail.getMarketValue(),
				propertyDetail.getCategoryType(),
				propertyDetail.getOccupancyCertificationNo(),
				propertyDetail.getBuildingPermissionNo(),
				propertyDetail.getBuildingPermissionDate(),
				propertyDetail.getDeviationPercentage(),
				propertyDetail.isAppurtenantLandChecked(),
				propertyDetail.isBuildingPlanDetailsChecked());

		vacantProperty.setManualAlv(propertyDetail.getManualAlv());
		vacantProperty.setOccupierName(propertyDetail.getOccupierName());

		LOGGER.debug("Exiting from changePropertyDetail");
		return vacantProperty;
	}
	

	
	private PropertyAddress createPropAddress() {

		LOGGER.debug("Entered into createPropAddress, \nAreaId: "
				+ getBlockId() + ", House Number: " + getHouseNumber()
				+ ", OldHouseNo: " + ", AddressStr: " + getAddressStr()
				+ ", PinCode: " + getPinCode());

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
		if (isChkIsCorrIsDiff()) {
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
		propertyId.setEastBoundary(getEastBoundary());
		propertyId.setWestBoundary(getWestBoundary());
		propertyId.setNorthBoundary(getNorthBoundary());
		propertyId.setSouthBoundary(getSouthBoundary());
		propertyId.setBasicProperty(basicProperty);
		LOGGER.debug("PropertyID: " + propertyId + "\nExiting from createPropertyID");
		return propertyId;
	}

	@Override
	public void validate() {
		LOGGER.debug("Entered into validate\nZoneId: " + zoneId + ", WardId: " + wardId
				+ ", AreadId: " + blockId + ", HouseNumber: " + houseNumber + ", PinCode: "
				+ pinCode + ", ParcelID:" + parcelID + ", MutationId: " + mutationId + ", PartNo: "
				+ partNo);

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

		for (PropertyOwnerInfo owner : property.getBasicProperty().getPropertyOwnerInfoProxy()) {
			if (owner != null) {
				if (StringUtils.isBlank(owner.getOwner().getAadhaarNumber())) {
					addActionError(getText("mandatory.adharNo"));
				}
				if (StringUtils.isBlank(owner.getOwner().getSalutation())) {
					addActionError(getText("mandatory.salutation"));
				}
				if (StringUtils.isBlank(owner.getOwner().getName())) {
					addActionError(getText("mandatory.ownerName"));
				}
				if (null == owner.getOwner().getGender()) {
					addActionError(getText("mandatory.gender"));
				}
				if (StringUtils.isBlank(owner.getOwner().getMobileNumber())) {
					addActionError(getText("mandatory.mobilenumber"));
				}
				if (StringUtils.isBlank(owner.getOwner().getEmailId())) {
					addActionError(getText("mandatory.emailId"));
				}
			}
		}

		validateProperty(property, areaOfPlot, dateOfCompletion, chkIsTaxExempted, taxExemptReason,
				propTypeId, propUsageId, propOccId, floorTypeId, roofTypeId, wallTypeId, woodTypeId);

		if(isBlank(pinCode)) {
		    addActionError(getText("mandatory.pincode"));
		}
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
		validateApproverDetails();
		super.validate();
	}

	@SkipValidation
	@Action(value = "/createProperty-printAck")
	public String printAck() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String url = WebUtils.extractRequestDomainURL(request, false);
		String imagePath = url.concat(PropertyTaxConstants.IMAGES_BASE_PATH).concat(
				ReportUtil.fetchLogo());
		PropertyAckNoticeInfo ackBean = new PropertyAckNoticeInfo();
		Map<String, Object> reportParams = new HashMap<String, Object>();
		ackBean.setOwnerName(basicProp.getFullOwnerName());
		ackBean.setOwnerAddress(basicProp.getAddress().toString());
		ackBean.setApplicationDate(basicProp.getCreatedDate());
		ackBean.setApplicationNo(basicProp.getApplicationNo());
		ackBean.setApprovedDate(property.getState().getCreatedDate().toDate());
		Date tempNoticeDate = DateUtils.add(property.getState().getCreatedDate().toDate(),
				Calendar.DAY_OF_MONTH, 15);
		ackBean.setNoticeDueDate(tempNoticeDate);
		reportParams.put("logoPath", imagePath);
		reportParams.put("loggedInUsername", propertyTaxUtil.getLoggedInUser(getSession())
				.getName());
		ReportRequest reportInput = new ReportRequest(CREATE_ACK_TEMPLATE, ackBean, reportParams);
		reportInput.setReportFormat(FileFormat.PDF);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		reportId = ReportViewerUtil.addReportToSession(reportOutput, getSession());
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

	public String getNorthBoundary() {
		return northBoundary;
	}

	public void setNorthBoundary(String northBoundary) {
		this.northBoundary = northBoundary;
	}

	public String getSouthBoundary() {
		return southBoundary;
	}

	public void setSouthBoundary(String southBoundary) {
		this.southBoundary = southBoundary;
	}

	public String getEastBoundary() {
		return eastBoundary;
	}

	public void setEastBoundary(String eastBoundary) {
		this.eastBoundary = eastBoundary;
	}

	public String getWestBoundary() {
		return westBoundary;
	}

	public void setWestBoundary(String westBoundary) {
		this.westBoundary = westBoundary;
	}

	public Map<String, String> getDeviationPercentageMap() {
		return deviationPercentageMap;
	}

	public void setDeviationPercentageMap(Map<String, String> deviationPercentageMap) {
		this.deviationPercentageMap = deviationPercentageMap;
	}

	public Map<String, String> getGuardianRelationMap() {
		return guardianRelationMap;
	}

	public void setGuardianRelationMap(Map<String, String> guardianRelationMap) {
		this.guardianRelationMap = guardianRelationMap;
	}

	public List<DocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}
	
        @Override
        public String getAdditionalRule() {
                return NEW_ASSESSMENT;
        }
	
}