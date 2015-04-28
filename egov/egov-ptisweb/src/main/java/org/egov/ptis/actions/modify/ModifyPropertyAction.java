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
package org.egov.ptis.actions.modify;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.AMALG_AUDIT_ACTION;
import static org.egov.ptis.constants.PropertyTaxConstants.AREA_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.AUDITDATA_STRING_SEP;
import static org.egov.ptis.constants.PropertyTaxConstants.BIFUR_AUDIT_ACTION;
import static org.egov.ptis.constants.PropertyTaxConstants.DATAUPDATE_AUDIT_ACTION;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_RSNS_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_AMALGAMATE_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_BIFURCATE_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_MODIFY_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.MODIFY_AUDIT_ACTION;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE127;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE134;
import static org.egov.ptis.constants.PropertyTaxConstants.PIPE_CHAR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_AMALG;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_BIFURCATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_COURT_RULE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_DATA_ENTRY;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_DATA_UPDATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_MODIFY;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_INACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_MARK_DEACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;
import static org.egov.ptis.constants.PropertyTaxConstants.PTCREATOR_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPSTATVALUE_BY_BASICPROPID_CODE_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.RESIDENTIAL_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.VOUCH_CREATE_RSN_DEACTIVATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFOWNER;
import static org.egov.ptis.constants.PropertyTaxConstants.WFSTATUS;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_NOTICE_GENERATION_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.modifyReasons;
import static org.egov.ptis.constants.PropertyTaxConstants.BUILT_UP_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Address;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.ValidationError;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.actions.workflow.WorkflowAction;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.client.util.FinancialUtil;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.client.workflow.WorkflowDetails;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.dao.property.PropertyStatusValuesDAO;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.egov.web.annotation.ValidationErrorPage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")

public class ModifyPropertyAction extends WorkflowAction {
	private static final String NO = "No";
	private static final String YES = "Yes";
	private static final String RESULT_ACK = "ack";
	private static final String RESULT_ERROR = "error";
	private Logger LOGGER = Logger.getLogger(getClass());
	private PersistenceService<BasicProperty, Long> basicPrpertyService;
	private PersistenceService<Property, Long> propertyImplService;
	private PersistenceService<FloorIF, Long> floorService;
	private BasicProperty basicProp;
	private PropertyImpl oldProperty = new PropertyImpl();
	private PropertyImpl propertyModel = new PropertyImpl();
	private boolean chkIsTaxExempted;
	private String taxExemptReason;
	private String areaOfPlot;
	private Map<String, String> waterMeterMap;
	private boolean generalTax;
	private boolean sewerageTax;
	private boolean lightingTax;
	private boolean fireServTax;
	private boolean bigResdBldgTax;
	private boolean educationCess;
	private boolean empGuaCess;
	private TreeMap<Integer, String> floorNoMap;
	private String reasonForModify;
	private String dateOfCompletion;
	private String modifyRsn;
	private Map<String, String> modifyReasonMap;
	private String ownerName;
	private String propAddress;
	private String corrsAddress;
	private String[] amalgPropIds;
	private PropertyService propService;
	private String courtOrdNum;
	private String orderDate;
	private String judgmtDetails;
	private String isAuthProp;
	private String amalgStatus;
	private BasicProperty amalgPropBasicProp;
	private String oldpropId;
	private String oldOwnerName;
	private String oldPropAddress;
	private String objNum;
	private String objDate;
	private String ackMessage;
	private Map<String, String> amenitiesMap;
	private String propTypeId;
	private String propTypeCategoryId;
	private String propUsageId;
	private String propOccId;
	private String amenities;
	private String[] floorNoStr = new String[100]; // Increased from 20 to 100
													// bcoz of
													// ArrayIndexOutOfBoundsException
	private BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
	List<ValidationError> errors = new ArrayList<ValidationError>();
	private PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	int i = 0;
	private PropertyImpl propWF;// would be current property workflow obj
	private Map<String, String> propTypeCategoryMap;
	FinancialUtil financialUtil = new FinancialUtil();

	private String docNumber;
	private Category propertyCategory;

	private boolean isfloorDetailsRequired;
	private String areaId;
	//UserDAO userDao = new UserDAO();
	BoundaryDAO boundaryDao;
	private boolean updateData;
	private PropertyAddress propertyAddr;
	private String parcelId;
	private String northBound;
	private String southBound;
	private String eastBound;
	private String westBound;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	private String errorMessage;
	private String partNo;
	private List<PropertyOwner> propertyOwners = new ArrayList<PropertyOwner>();
	private String modificationType;
	
	private boolean isTenantFloorPresent;
	private String mode;
	
	@Autowired
	private EisCommonService eisCommonService;
	
	public ModifyPropertyAction() {
		super();
		propertyModel.setPropertyDetail(new BuiltUpProperty());
		this.addRelatedEntity("propertyDetail.propertyTypeMaster", PropertyTypeMaster.class);
		this.addRelatedEntity("propertyDetail.floorDetailsProxy.unitType", PropertyTypeMaster.class);
		this.addRelatedEntity("propertyDetail.floorDetailsProxy.propertyUsage", PropertyUsage.class);
		this.addRelatedEntity("propertyDetail.floorDetailsProxy.propertyOccupation", PropertyOccupation.class);
		this.addRelatedEntity("propertyDetail.floorDetailsProxy.structureClassification", StructureClassification.class);
	}
	
	@SkipValidation
	@Override
	public Object getModel() {
		return propertyModel;
	}
	
	/**
	 * @return
	 */
	private boolean isFromDataEntry() {
		return fromDataEntry != null && fromDataEntry.equals("true");
	}

	@SkipValidation
	public String modifyForm() {
		LOGGER.debug("Entered into modifyForm, \nIndexNumber: " + indexNumber + ", BasicProperty: " + basicProp
				+ ", OldProperty: " + oldProperty + ", PropertyModel: " + propertyModel);
		String target = "";

		target = populateFormData();

		LOGGER.debug("modifyForm: IsAuthProp: " + getIsAuthProp() + ", AreaOfPlot: " + getAreaOfPlot()
				+ ", PropTypeId: " + getPropTypeId() + ", PropTypeCategoryId: " + getPropTypeCategoryId()
				+ ", PropUsageId: " + getPropUsageId() + ", PropOccId: " + getPropOccId());
		LOGGER.debug("Exiting from modifyForm");
		return target;
	}

	private String populateFormData() {
		LOGGER.debug("Entered into populateFormData");
		String target = "";
		Map<String, String> wfMap = basicProp.getPropertyWfStatus();
		PropertyImpl propertyImpl = null;
		String wfStatus = wfMap.get(WFSTATUS);
		if (wfStatus.equalsIgnoreCase("TRUE")
				&& modifyRsn != null
				&& (!PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(modifyRsn) && !PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ
						.equalsIgnoreCase(modifyRsn))) {
			getSession().put(WFOWNER, wfMap.get(WFOWNER));
			target = "workFlowError";
		} else {
			
			setOldProperty((PropertyImpl) getBasicProp().getProperty());
			if (propWF == null) {

				if (PropertyTaxConstants.PROPERTY_MODIFY_REASON_DATA_UPDATE.equals(modifyRsn)) {
					// Using the existing active egpt_property object
					propertyImpl = oldProperty;
				} else {
					propertyImpl = (PropertyImpl) oldProperty.createPropertyclone();
				}

				if (!modifyRsn.equals(PROPERTY_MODIFY_REASON_BIFURCATE)
						&& !modifyRsn.equals(PROPERTY_MODIFY_REASON_AMALG)) {
					setAreaId(basicProp.getPropertyID().getArea().getId().toString());
				}

			} else {
				propertyImpl = propWF;
				
				/*if (PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ.equalsIgnoreCase(modifyRsn) && !basicProp.getAllChangesCompleted()) {
					allChangesCompleted = basicProp.getAllChangesCompleted();
					modifyRsn = PROPERTY_MODIFY_REASON_DATA_ENTRY;
				} else {
					setModifyRsn(propertyImpl.getPropertyDetail().getPropertyMutationMaster().getCode());
				}*/
				
				// setReasonForModify is only for work flow revert changes
				setReasonForModify(propertyImpl.getPropertyDetail().getPropertyMutationMaster().getCode());
				if (!modifyRsn.equals(PROPERTY_MODIFY_REASON_BIFURCATE)
						&& !modifyRsn.equals(PROPERTY_MODIFY_REASON_AMALG)) {
					if (propWF.getAreaBndry() != null) {
						setAreaId(propWF.getAreaBndry().getId().toString());
					} else {
						setAreaId(basicProp.getPropertyID().getArea().getId().toString());
					}
				}
			}

			propertyImpl.setExtra_field1("");
			propertyImpl.setExtra_field3("");
			propertyImpl.setExtra_field4("");
			propertyImpl.setExtra_field5("");
			setProperty(propertyImpl);

			setOwnerName(ptisCacheMgr.buildOwnerFullName(propertyModel.getPropertyOwnerSet()));
			setPropAddress(ptisCacheMgr.buildAddressByImplemetation(getBasicProp().getAddress()));

			if (isNotBlank(propertyImpl.getExtra_field6())) {
				String[] addFields = propertyImpl.getExtra_field6().split("\\|");
				propertyAddr = new PropertyAddress();
				propertyAddr.setStreetRoadLine(isBlank(addFields[0]) ? null : addFields[0]);
				propertyAddr.setHouseNoBldgApt(isBlank(addFields[1]) ? null : addFields[1]);
				propertyAddr.setDoorNumOld(isBlank(addFields[2]) ? null : addFields[2]);
				propertyAddr.setPinCode(isBlank(addFields[3]) ? null : addFields[3]);
				propertyAddr.setMobileNo(isBlank(addFields[4]) ? null : addFields[4]);
				propertyAddr.setEmailAddress(isBlank(addFields[5]) ? null : addFields[5]);

				propertyAddr.setExtraField1(isBlank(addFields[6]) ? null : addFields[6]);
				propertyAddr.setExtraField2(isBlank(addFields[7]) ? null : addFields[7]);
				propertyAddr.setExtraField3(isBlank(addFields[8]) ? null : addFields[8]);
				propertyAddr.setExtraField4(isBlank(addFields[9]) ? null : addFields[9]);
				parcelId = isBlank(addFields[10]) ? null : addFields[10];
				northBound = isBlank(addFields[11]) ? null : addFields[11];
				southBound = isBlank(addFields[12]) ? null : addFields[12];
				eastBound = isBlank(addFields[13]) ? null : addFields[13];
				westBound = isBlank(addFields[14]) ? null : addFields[14];
			} else {
				propertyAddr = basicProp.getAddress();
				parcelId = basicProp.getGisReferenceNo();
				northBound = basicProp.getPropertyID().getNorthBoundary();
				southBound = basicProp.getPropertyID().getSouthBoundary();
				eastBound = basicProp.getPropertyID().getEastBoundary();
				westBound = basicProp.getPropertyID().getWestBoundary();
			}

			corrsAddress = PropertyTaxUtil.getOwnerAddress(propertyModel.getPropertyOwnerSet());

			if (basicProp.getExtraField1() != null) {
				setIsAuthProp(basicProp.getExtraField1());
			}
			if (propertyModel.getPropertyDetail().getSitalArea() != null) {
				setAreaOfPlot(propertyModel.getPropertyDetail().getSitalArea().getArea().toString());
			}
			if (propertyModel.getPropertyDetail().getFloorDetails() != null
					&& propertyModel.getPropertyDetail().getFloorDetails().size() > 0) {
				// isfloorDetailsRequired is enable the check box to be
				// marked if floordetails present for State Govt property
				if (propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
						.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
						|| propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
								.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					isfloorDetailsRequired = false;
				}
				List flrDetsProxy = new ArrayList();
				for (FloorIF floor : propertyModel.getPropertyDetail().getFloorDetails()) {
					if (floor.getPropertyOccupation() != null
							&& floor.getPropertyOccupation().getOccupancyCode().equalsIgnoreCase(PropertyTaxConstants.TENANT)) {
						isTenantFloorPresent = true;
					}
					flrDetsProxy.add(floor);
				}
				propertyModel.getPropertyDetail().setFloorDetailsProxy(flrDetsProxy);
			} else {
				if (propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
						.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
						|| propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
								.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					isfloorDetailsRequired = true;
				}
			}
			PropertyTypeMaster propertyType = propertyModel.getPropertyDetail().getPropertyTypeMaster();
			propTypeId = propertyType.getId().toString();
			prepareCategoryMap(propertyModel.getPropertyDetail().getPropertyTypeMaster());
			prepareUsageList(propTypeId);

			if (propertyModel.getPropertyDetail().getExtra_field5() != null) {
				setPropTypeCategoryId(propertyModel.getPropertyDetail().getExtra_field5().toString());
			}

			if (propertyModel.getPropertyDetail().getPropertyUsage() != null) {
				propUsageId = propertyModel.getPropertyDetail().getPropertyUsage().getId().toString();
			}

			if (propertyModel.getPropertyDetail().getPropertyOccupation() != null) {
				propOccId = propertyModel.getPropertyDetail().getPropertyOccupation().getId().toString();
			}
			
			setDateOfCompletion(new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY).format(basicProp
					.getPropCreateDate()));
			
			setDocNumber(propertyModel.getDocNumber());
			target = NEW;
		}

		LOGGER.debug("populateFormData - target : " + target + "\n Exiting from populateFormData");
		return target;
	}

	@SkipValidation
	public String view() {
		LOGGER.debug("Entered into view, BasicProperty: " + basicProp + ", ModelId: " + getModelId());

		if (getModelId() != null) {
			propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
					Long.valueOf(getModelId()));
			setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
			LOGGER.debug("view: PropertyModel by model id: " + propertyModel);
		}

		String currWfState = propertyModel.getState().getValue();
		// Code commented for reverting work flow changes, do not remove.
		/*
		 * if (PTCREATOR_ROLE.equals(userRole) &&
		 * !currWfState.endsWith(WF_STATE_NOTICE_GENERATION_PENDING) &&
		 * (modifyRsn.equals(PROPERTY_MODIFY_REASON_BIFURCATE) ||
		 * modifyRsn.equals(PROPERTY_MODIFY_REASON_AMALG) ||
		 * modifyRsn.equals(PROPERTY_MODIFY_REASON_DATA_ENTRY) ||
		 * modifyRsn.equals(PROPERTY_MODIFY_REASON_MODIFY) || modifyRsn
		 * .equals(PROPERTY_MODIFY_REASON_OBJ))) {
		 * 
		 * if (basicProp.getUpicNo() != null &&
		 * !basicProp.getUpicNo().isEmpty()) {
		 * setIndexNumber(basicProp.getUpicNo()); }
		 * 
		 * modifyForm(); if (!modifyRsn.equals(PROPERTY_MODIFY_REASON_BIFURCATE)
		 * && !modifyRsn.equals(PROPERTY_MODIFY_REASON_AMALG)) { if
		 * (propertyModel.getAreaBndry() != null) {
		 * setAreaId(propertyModel.getAreaBndry().getId().toString()); } else {
		 * setAreaId(basicProp.getPropertyID().getArea().getId().toString()); }
		 * } return NEW; }
		 */

		corrsAddress = PropertyTaxUtil.getOwnerAddress(propertyModel.getPropertyOwnerSet());

		amalgPropIds = new String[10];
		if (basicProp.getAllChangesCompleted() != null) {
			setAllChangesCompleted(basicProp.getAllChangesCompleted());
		}

		if (propertyModel.getPropertyDetail().getExtra_field4() != null
				&& !propertyModel.getPropertyDetail().getExtra_field4().isEmpty()) {
			setAmenities(CommonServices.getAmenitiesDtls(propertyModel.getPropertyDetail().getExtra_field4()));
		}

		if (propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
				|| propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
						.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)
				|| propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
						.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			setGenWaterRate(CommonServices.getWaterMeterDtls(propertyModel.getPropertyDetail().getExtra_field1()));
		}

		if (propertyModel.getPropertyDetail().getFloorDetails().size() > 0) {
			setFloorDetails(propertyModel);
		}

		if (!currWfState.endsWith(WF_STATE_NOTICE_GENERATION_PENDING)) {
			int i = 0;
			for (PropertyStatusValues propstatval : basicProp.getPropertyStatusValuesSet()) {
				if (propstatval.getIsActive().equals("W")) {
					setPropStatValForView(propstatval);
					LOGGER.debug("view: PropertyStatusValues for new modify screen: " + propstatval);
				}
				// setting the amalgamated properties
				LOGGER.debug("view: Amalgamated property ids:");
				if (PROP_CREATE_RSN.equals(propstatval.getPropertyStatus().getStatusCode())
						&& propstatval.getIsActive().equals("Y")) {
					if (propstatval.getReferenceBasicProperty() != null) {
						amalgPropIds[i] = propstatval.getReferenceBasicProperty().getUpicNo();
						LOGGER.debug(amalgPropIds[i] + ", ");
						i++;
					}
				}
			}
		}

		if (currWfState.endsWith(WF_STATE_NOTICE_GENERATION_PENDING)) {
			setIsApprPageReq(Boolean.FALSE);
			if (basicProp.getUpicNo() != null && !basicProp.getUpicNo().isEmpty()) {
				setIndexNumber(basicProp.getUpicNo());
			}

			int i = 0;
			for (PropertyStatusValues propstatval : basicProp.getPropertyStatusValuesSet()) {
				if (propstatval.getIsActive().equals("Y")) {
					setPropStatValForView(propstatval);
					LOGGER.debug("PropertyStatusValues for view modify screen: " + propstatval);
				}
				// setting the amalgamated properties
				LOGGER.debug("view: Amalgamated property ids:");
				if (PROP_CREATE_RSN.equals(propstatval.getPropertyStatus().getStatusCode())
						&& propstatval.getIsActive().equals("Y")) {
					if (propstatval.getReferenceBasicProperty() != null) {
						amalgPropIds[i] = propstatval.getReferenceBasicProperty().getUpicNo();
						LOGGER.debug(amalgPropIds[i] + ", ");
						i++;
					}
				}
			}
		}

		if (isNotBlank(propertyModel.getExtra_field6())) {
			String[] addFields = propertyModel.getExtra_field6().split("\\|");
			propertyAddr = new PropertyAddress();
			propertyAddr.setStreetRoadLine(isBlank(addFields[0]) ? null : addFields[0]);
			propertyAddr.setHouseNoBldgApt(isBlank(addFields[1]) ? null : addFields[1]);
			propertyAddr.setDoorNumOld(isBlank(addFields[2]) ? null : addFields[2]);
			propertyAddr.setPinCode(isBlank(addFields[3]) ? null : (addFields[3]));
			propertyAddr.setMobileNo(isBlank(addFields[4]) ? null : addFields[4]);
			propertyAddr.setEmailAddress(isBlank(addFields[5]) ? null : addFields[5]);

			propertyAddr.setExtraField1(isBlank(addFields[6]) ? null : addFields[6]);
			propertyAddr.setExtraField2(isBlank(addFields[7]) ? null : addFields[7]);
			propertyAddr.setExtraField3(isBlank(addFields[8]) ? null : addFields[8]);
			propertyAddr.setExtraField4(isBlank(addFields[9]) ? null : addFields[9]);
			parcelId = isBlank(addFields[10]) ? null : addFields[10];
			northBound = isBlank(addFields[11]) ? null : addFields[11];
			southBound = isBlank(addFields[12]) ? null : addFields[12];
			eastBound = isBlank(addFields[13]) ? null : addFields[13];
			westBound = isBlank(addFields[14]) ? null : addFields[14];
		} else {
			propertyAddr = basicProp.getAddress();
			parcelId = basicProp.getGisReferenceNo();
			northBound = basicProp.getPropertyID().getNorthBoundary();
			southBound = basicProp.getPropertyID().getSouthBoundary();
			eastBound = basicProp.getPropertyID().getEastBoundary();
			westBound = basicProp.getPropertyID().getWestBoundary();
		}

		setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
		setDocNumber(propertyModel.getDocNumber());
		setPropertyCategory((Category) persistenceService.find("from Category c where c.id = ?",
				Long.valueOf(propertyModel.getPropertyDetail().getExtra_field6())));
		LOGGER.debug("view: ModifyReason: " + getModifyRsn());
		LOGGER.debug("Exiting from view");
		return "view";
	}

	@ValidationErrorPage(value = "new")
	public String save() {
		LOGGER.debug("save: Property modification started, ModelId: " + getModelId());
		long startTimeMillis = System.currentTimeMillis();

		if (PTCREATOR_ROLE.equals(userRole) && PROPERTY_MODIFY_REASON_OBJ.equals(modifyRsn)) {
			if (propWF == null) {
				propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
						Long.valueOf(getModelId()));
				LOGGER.debug("save: Workflow property: " + propWF);
			}
			basicProp = propWF.getBasicProperty();
			LOGGER.debug("save: BasicProperty on workflow property: " + basicProp);
			propWF.setStatus(STATUS_ISHISTORY);// workflow object made history
			// for Objection
			propWF.setRemarks("objection property made history");
			endWorkFlow(propWF);
		} else if (PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(modifyRsn)
				|| PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn)) {
			if (propWF == null && getModelId() != null && !getModelId().equals("")) {
				propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
						Long.valueOf(getModelId()));
				LOGGER.debug("save: Workflow property: " + propWF);
				basicProp = propWF.getBasicProperty();
			} else {
				populateBasicProp();
			}
			LOGGER.debug("save: BasicProperty on workflow property: " + basicProp);
		} else {
			populateBasicProp();
		}
		
		oldProperty = (PropertyImpl) basicProp.getProperty();

		// docs upload

		if (getDocNumber() != null && !getDocNumber().equals("")) {
			PropertyDocs pd = createPropertyDocs(basicProp, getDocNumber(), modifyRsn);
			basicProp.addDocs(pd);
		}

		if (propWF != null && propWF.getStatus().equals(STATUS_WORKFLOW)) {
			propWF.setStatus(STATUS_ISHISTORY);
			endWorkFlow(propWF);
		}

		String objectionRemarks = "Objection approved, ignored for notices";
		
		boolean objectionDataEntryBegun = PROPERTY_MODIFY_REASON_DATA_ENTRY.equalsIgnoreCase(modificationType) && !allChangesCompleted;
		
		if (PROPERTY_MODIFY_REASON_OBJ.equalsIgnoreCase(modifyRsn) && !objectionDataEntryBegun) {	

			objectTheOldProperty(objectionRemarks);
			
			propWF.setStatus(PropertyTaxConstants.STATUS_OBJECTED);
			propWF.setRemarks(propWF.getRemarks() == null ? objectionRemarks : propWF
					.getRemarks().concat(objectionRemarks));
			
			basicProp.setAllChangesCompleted(true);
			propertyModel.setStatus(PropertyTaxConstants.STATUS_DEMAND_INACTIVE);							
			
		} else if (objectionDataEntryBegun) {
			
			objectTheOldProperty(objectionRemarks);
			
			propWF.setStatus(PropertyTaxConstants.STATUS_ISACTIVE);
			propWF.setRemarks(propWF.getRemarks() == null ? PropertyTaxConstants.STATUS_OBJECTED_STR : oldProperty
					.getRemarks().concat(PropertyTaxConstants.STATUS_OBJECTED_STR));
			
			propertyModel.setStatus(STATUS_WORKFLOW);
		
		} else {
			
			if (PROPERTY_MODIFY_REASON_DATA_ENTRY.equalsIgnoreCase(modifyRsn)) {
				if (allChangesCompleted) {	
			
					//for (Property property : basicProp.getPropertySet()) {
					String remarks = oldProperty.getRemarks();
					
					if (StringUtils.isNotBlank(remarks) &&  remarks.contains(PropertyTaxConstants.STATUS_OBJECTED_STR)) {
						
						oldProperty.setStatus(PropertyTaxConstants.STATUS_OBJECTED);
						oldProperty.getRemarks().replaceFirst(PropertyTaxConstants.STATUS_OBJECTED_STR, objectionRemarks);
						
					} else {
						oldProperty.setStatus(STATUS_ISHISTORY);
					}
					
					basicProp.setAllChangesCompleted(allChangesCompleted);
					propertyModel.setStatus(PropertyTaxConstants.STATUS_DEMAND_INACTIVE);
				} else {
					propertyModel.setStatus(STATUS_WORKFLOW);
				}
			} else {
				oldProperty.setStatus(STATUS_ISHISTORY);
				propertyModel.setStatus(PropertyTaxConstants.STATUS_DEMAND_INACTIVE);
			}
		}
		
		modifyBasicProp(getDocNumber());
		
		// Not creating Voucher for Data Entry facility		
		if (allChangesCompleted || PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)
				|| PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
			createVoucher(); // Creates Voucher
		}

		transitionWorkFlow();
		setAmalgPropInactive();
		// when reason is objection, property sat value isactive should be
		// W,this used for memo
		if (!(PROPERTY_MODIFY_REASON_OBJ.equals(modifyRsn) || PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(modifyRsn))) {
			propService.setWFPropStatValActive(basicProp);
		}

		boolean isModifyOrObjection = PROPERTY_MODIFY_REASON_MODIFY.equals(getModifyRsn())
				|| PROPERTY_MODIFY_REASON_OBJ.equals(getModifyRsn());
		/**
		 * The old property will be made history and the workflow property will
		 * be made active only when all the changes are completed in case of
		 * modify reason is 'DATA ENTRY' or 'MODIFY'
		 */
		if (((PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(getModifyRsn()) || isModifyOrObjection) && allChangesCompleted)

				|| (!PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(getModifyRsn()) && !isModifyOrObjection)) {

			/**
			 * area is made an editable field due to new requirement(so
			 * modification of area is applicable in PropertyID(area) and
			 * PropertyAddress(block) and this is not applicable in case of
			 * bifur and amalg
			 */
			if (!PROPERTY_MODIFY_REASON_AMALG.equals(getModifyRsn())
					&& !PROPERTY_MODIFY_REASON_BIFURCATE.equals(getModifyRsn())) {
				if (propertyModel.getAreaBndry() != null) {
					basicProp.getPropertyID().setArea(propertyModel.getAreaBndry());
					//basicProp.getAddress().setBlock(propertyModel.getAreaBndry().getName());
				}
			}
		}
		
		propertyTaxUtil.makeTheEgBillAsHistory(basicProp);
		endWorkFlow(oldProperty);
					
		basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, PROPERTY_MODIFY_REASON_MODIFY,
				basicProp.getPropCreateDate(), null, null, null, null));
		
		basicPrpertyService.update(basicProp);
		
		if (PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn) || PROPERTY_MODIFY_REASON_OBJ.equals(modifyRsn)
				|| PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(modifyRsn)) {
			modifyPropertyAuditTrail(basicProp, propertyModel, MODIFY_AUDIT_ACTION, null);
		} else if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
			modifyPropertyAuditTrail(basicProp, propertyModel, BIFUR_AUDIT_ACTION, null);
		} else if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)) {
			modifyPropertyAuditTrail(basicProp, propertyModel, AMALG_AUDIT_ACTION, null);
		}
		
		if (PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn) || PROPERTY_MODIFY_REASON_OBJ.equals(modifyRsn)) {
			setAckMessage("Property Modified Successfully in System with Index Number : ");
		} else if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
			setAckMessage("Property Bifurcated Successfully in System with Index Number : ");
		} else if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)) {
			setAckMessage("Property Amalgamated Successfully in System with Index Number : ");
		} else if (PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(modifyRsn)) {
			setAckMessage("Property Modified Successfully in System");
		}
		addActionMessage(getText("property.save.success", new String[] { propertyModel.getBasicProperty().getUpicNo() }));
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		LOGGER.info("save: Property modified successfully in system with Index Number: " + basicProp.getUpicNo()
				+ "; Time taken(ms) = " + elapsedTimeMillis);
		LOGGER.debug("save: Property modification ended");
		return RESULT_ACK;
	}

	/**
	 * @param objectionRemarks
	 */
	private void objectTheOldProperty(String objectionRemarks) {
		oldProperty.setStatus(PropertyTaxConstants.STATUS_OBJECTED);
		oldProperty.setRemarks(oldProperty.getRemarks() == null ? objectionRemarks : oldProperty
				.getRemarks().concat(objectionRemarks));
	}

	@ValidationErrorPage(value = "new")
	public String forwardModify() {// when creator(role) forwards
		LOGGER.debug("forwardModify: Modify property started " + propertyModel);
		long startTimeMillis = System.currentTimeMillis();
		if (propertyModel.getBasicProperty() == null && getModelId() != null && !getModelId().trim().isEmpty()) {
			propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
					Long.valueOf(getModelId()));

			LOGGER.debug("forwardModify: Workflow property: " + propWF);
			basicProp = propWF.getBasicProperty();
			setBasicProp(basicProp);
		} else {
			populateBasicProp();
		}

		oldProperty = (PropertyImpl) basicProp.getProperty();
		modifyBasicProp(getDocNumber());
		transitionWorkFlow();
		setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());

		if (propWF != null && modifyReasons.contains(modifyRsn)) {
			/**
			 * delete the existing workflow property and create a new workflow
			 * property if the property is under data entry or if the record is
			 * forwarded after rejection
			 */
			if (propWF.getStatus().equals(STATUS_WORKFLOW)) {
				basicProp.removeProperty(propWF);
				propertyImplService.delete(propWF);
				basicPrpertyService.update(basicProp);
			}
		}

		prepareAckMsg();
		addActionMessage(getText("property.forward.success", new String[] { propertyModel.getBasicProperty()
				.getUpicNo() }));
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		LOGGER.info("forwardModify: Modify property forwarded successfully; Time taken(ms) = " + elapsedTimeMillis);
		LOGGER.debug("forwardModify: Modify property forward ended");
		return RESULT_ACK;
	}

	@SkipValidation
	public String forwardView() // when validator(role) forwards
	{
		LOGGER.debug("Entered into forwardView");
		super.validate();
		propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
				Long.valueOf(getModelId()));
		LOGGER.debug("forwardView: Workflow property: " + propertyModel);
		if (hasErrors()) {
			if (propertyModel.getPropertyDetail().getExtra_field1() != null
					&& !propertyModel.getPropertyDetail().getExtra_field1().isEmpty()) {
				setGenWaterRate(CommonServices.getWaterMeterDtls(propertyModel.getPropertyDetail().getExtra_field1()));
			}
			LOGGER.debug("forwardView: Exiting forwardView with validation errors");
			return "view";
		}
		transitionWorkFlow();
		setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
		prepareAckMsg();
		addActionMessage(getText("property.forward.success", new String[] { propertyModel.getBasicProperty()
				.getUpicNo() }));
		LOGGER.debug("Exiting from forwardView");
		return RESULT_ACK;
	}

	@SkipValidation
	public String approve() {
		LOGGER.debug("Enter method approve");
		amalgPropIds = new String[10];
		propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
				Long.valueOf(getModelId()));
		LOGGER.debug("approve: Workflow property: " + propertyModel);
		basicProp = propertyModel.getBasicProperty();
		oldProperty = (PropertyImpl) basicProp.getProperty();

		if (PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(getModifyRsn())) {
			String indexNum = propertyTaxNumberGenerator.generateIndexNumber(basicProp.getPropertyID().getWard()
					.getBoundaryNum().toString());
			basicProp.setUpicNo(indexNum);
		}

		transitionWorkFlow();
		int i = 0;
		for (PropertyStatusValues propstatval : basicProp.getPropertyStatusValuesSet()) {
			if (propstatval.getIsActive().equals("Y")) {
				if (PROP_CREATE_RSN.equals(propstatval.getPropertyStatus().getStatusCode())) {
					if (propstatval.getReferenceBasicProperty() != null) {
						amalgPropIds[i] = propstatval.getReferenceBasicProperty().getUpicNo();
						i++;
					}
				}
			}
		}
		setAmalgPropInactive();
		if (!PROPERTY_MODIFY_REASON_OBJ.equals(modifyRsn)) {
			propService.setWFPropStatValActive(basicProp);
		}

		if ((basicProp.getAllChangesCompleted() != null && basicProp.getAllChangesCompleted())
				|| (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn) || PROPERTY_MODIFY_REASON_BIFURCATE
						.equals(modifyRsn))) {
			createVoucher(); // Creates voucher
		}
		
		if (PROPERTY_MODIFY_REASON_OBJ.equalsIgnoreCase(modifyRsn)) {
			
			for (Property property : basicProp.getPropertySet()) {
				property.setStatus(PropertyTaxConstants.STATUS_OBJECTED);
				property.setRemarks(property.getRemarks().concat("Objection approved, ignored for notices"));
			}
			
			propertyModel.setStatus(STATUS_ISACTIVE);
			propertyTaxUtil.makeTheEgBillAsHistory(basicProp);
			endWorkFlow(oldProperty);
		}

		// check the value here in case of objection and modify -- remove this while committing
		setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());

		/**
		 * The old property will be made history and the workflow property will
		 * be made active only when all the changes are completed in case of
		 * modify reason is 'DATA ENTRY' or 'MODIFY'
		 */
		if (((PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(getModifyRsn()) || PROPERTY_MODIFY_REASON_MODIFY
				.equals(getModifyRsn())) && basicProp.getAllChangesCompleted())

		|| (!PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(getModifyRsn()))) {

			/**
			 * area is made a editable field due to new requirement(so
			 * modification of area is applicable in PropertyID(area) and
			 * PropertyAddress(block) and this is not applicable in case of
			 * bifur and amalg
			 */
			if (!PROPERTY_MODIFY_REASON_AMALG.equals(getModifyRsn())
					&& !PROPERTY_MODIFY_REASON_BIFURCATE.equals(getModifyRsn())) {
				if (propertyModel.getAreaBndry() != null) {
					basicProp.getPropertyID().setArea(propertyModel.getAreaBndry());
					//FIX ME
					//basicProp.getAddress().setBlock(propertyModel.getAreaBndry().getName());
				}
			}

			
			propertyModel.setStatus(STATUS_ISACTIVE);
			oldProperty.setStatus(STATUS_ISHISTORY);
			propertyTaxUtil.makeTheEgBillAsHistory(basicProp);
			endWorkFlow(oldProperty);
		}
		// upload docs
		if (propertyModel.getDocNumber() != null && !propertyModel.getDocNumber().equals("")) {
			PropertyDocs pd = createPropertyDocs(basicProp, propertyModel.getDocNumber(), modifyRsn);
			basicProp.addDocs(pd);
		}
		if (PROPERTY_MODIFY_REASON_MODIFY.equals(getModifyRsn())) {
			updateAddress();
			String[] addFields = propertyModel.getExtra_field6().split("\\|");

			basicProp.setGisReferenceNo(isBlank(addFields[10]) ? null : addFields[10]);
			basicProp.getPropertyID().setNorthBoundary(isBlank(addFields[11]) ? null : addFields[11]);
			basicProp.getPropertyID().setSouthBoundary(isBlank(addFields[12]) ? null : addFields[12]);
			basicProp.getPropertyID().setEastBoundary(isBlank(addFields[13]) ? null : addFields[13]);
			basicProp.getPropertyID().setWestBoundary(isBlank(addFields[14]) ? null : addFields[14]);
		}
		
		propertyModel.setExtra_field6(null);
		basicPrpertyService.update(basicProp);
		setBasicProp(basicProp);

		if (PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn) || PROPERTY_MODIFY_REASON_OBJ.equals(modifyRsn)
				|| PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(modifyRsn)) {
			if (PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn)) {
				modifyPropertyAuditTrail(basicProp, propertyModel, MODIFY_AUDIT_ACTION, null);
			}
			setAckMessage("Property Modified Successfully in System with Index Number : ");
		} else if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
			modifyPropertyAuditTrail(basicProp, propertyModel, BIFUR_AUDIT_ACTION, null);
			setAckMessage("Property Bifurcated Successfully in System with Index Number : ");
		} else if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)) {
			modifyPropertyAuditTrail(basicProp, propertyModel, AMALG_AUDIT_ACTION, null);
			setAckMessage("Property Amalgamated Successfully in System with Index Number : ");
		}

		addActionMessage(getText("property.approve.success", new String[] { propertyModel.getBasicProperty()
				.getUpicNo() }));
		LOGGER.debug("Exiting approve");
		return RESULT_ACK;
	}

	@SkipValidation
	public String reject() {
		LOGGER.debug("reject: Property rejection started");
		propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
				Long.valueOf(getModelId()));
		LOGGER.debug("reject: Property: " + propertyModel);
		BasicProperty basicProperty = propertyModel.getBasicProperty();
		// this is required bcoz this property moves to initiators inbox and he
		// must be able to edit/modify the details
		if (!(PROPERTY_MODIFY_REASON_OBJ.equals(getModifyRsn()) || PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn) || PROPERTY_MODIFY_REASON_BIFURCATE
				.equals(modifyRsn))) {
			propertyModel.getBasicProperty().setAllChangesCompleted(false);
		}
		setBasicProp(basicProperty);
		LOGGER.debug("reject: BasicProperty: " + basicProperty);
		transitionWorkFlow();
		setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
		setAckMessage("Property Rejected Successfully and forwarded to initiator : "
				+ propertyModel.getCreatedBy().getUsername() + " with Index Number : ");
		LOGGER.debug("reject: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
		LOGGER.debug("reject: Property rejection ended");

		return RESULT_ACK;
	}

	@SkipValidation
	public String editOwnerForm() {
		LOGGER.debug("Entered into editOwnerForm, edit facility for Owner Name and PartNO, indexNumber: " + indexNumber);		
		setOwnerName(ptisCacheMgr.buildOwnerFullName(basicProp.getProperty().getPropertyOwnerSet()));
		setPropertyOwners(new ArrayList<PropertyOwner>(basicProp.getProperty().getPropertyOwnerSet()));
		return "ownerForm";
	}

	@SkipValidation
	public String updateOwner() {
		LOGGER.debug("Entered into updateOwner");
		Set<PropertyOwner> existingOwners = basicProp.getProperty().getPropertyOwnerSet();
		List<PropertyOwner> newOwners = getPropertyOwners();
		StringBuilder auditDetail1 = new StringBuilder();
		PropertyOwner propertyOwner = null;
		int index = 0;
		
		if (!newOwners.isEmpty()) {
			for (PropertyOwner propOwner : existingOwners) {
				propertyOwner = newOwners.get(index);
				propOwner.setOrderNo(index + 1);
				LOGGER.debug("updateOwner new owner " + propertyOwner.getName());
				auditDetail1.append("Owner ").append(index + 1).append(":").append(propOwner.getName())
						.append(PIPE_CHAR).append(propertyOwner.getName());
				propOwner.setName(propertyOwner.getName());
				index++;
			}
		}
		auditDetail1.append("Part No : ").append(basicProp.getPartNo()).append(PIPE_CHAR).append(partNo);
		LOGGER.debug("updateOwner, old part no="+ basicProp.getPartNo() + ", new part no=" + partNo);
		basicProp.setPartNo(partNo);			
		//propertyTaxUtil.generateAuditEvent(PropertyTaxConstants.EDIT_OWNER_AUDIT_ACTION, basicProp,auditDetail1.toString(), null);
		setAckMessage(getText("property.editowner.success"));
		return RESULT_ACK;
	}

	@SuppressWarnings("unchecked")
	public void prepare() {

		LOGGER.debug("Entered into preapre, ModelId: " + getModelId());
		// super.prepare();
		if (getModelId() != null && !getModelId().isEmpty()) {
			setBasicProp((BasicProperty) getPersistenceService().find(
					"select prop.basicProperty from PropertyImpl prop where prop.id=?", Long.valueOf(getModelId())));
			LOGGER.debug("prepare: BasicProperty: " + basicProp);
			propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
					Long.valueOf(getModelId()));
		} else if (indexNumber != null && !indexNumber.trim().isEmpty()) {
			setBasicProp((BasicProperty) getPersistenceService().findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
					indexNumber));
		}

		List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy(
				"from PropertyTypeMaster order by orderNo");
		List<PropertyOccupation> propOccList = getPersistenceService().findAllBy("from PropertyOccupation");
		List<PropertyMutationMaster> propMutList = getPersistenceService()
				.findAllBy(
						"from PropertyMutationMaster where type = 'MODIFY' and code not in('AMALG','BIFURCATE','OBJ', 'DATA_ENTRY')");
		List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");
		List<String> authPropList = new ArrayList<String>();
		authPropList.add(YES);
		authPropList.add(NO);
		List<String> noticeTypeList = new ArrayList<String>();
		noticeTypeList.add(NOTICE127);
		noticeTypeList.add(NOTICE134);

		addDropdownData("PropTypeMaster", propTypeList);

		StringBuilder unitTypeQuery = new StringBuilder().append("from PropertyTypeMaster where code in ('")
				.append(PROPTYPE_OPEN_PLOT).append("', '").append(PROPTYPE_RESD).append("', '")
				.append(PROPTYPE_NON_RESD).append("') order by orderNo");

		addDropdownData("UnitTypes", getPersistenceService().findAllBy(unitTypeQuery.toString()));

		setWaterMeterMap(CommonServices.getWaterMeterMstr());

		addDropdownData("OccupancyList", propOccList);
		addDropdownData("StructureList", Collections.EMPTY_LIST);
		addDropdownData("AuthPropList", authPropList);
		addDropdownData("NoticeTypeList", noticeTypeList);
		addDropdownData("AgeFactorList", ageFacList);
		addDropdownData("MutationList", propMutList);
		addDropdownData("LocationFactorList", Collections.EMPTY_LIST);
		setPropTypeCategoryMap(Collections.EMPTY_MAP);
		setFloorNoMap(CommonServices.floorMap());
		setAmenitiesMap(CommonServices.getAmenities());
		PropertyTypeMaster propType;

		if (propTypeId != null && !propTypeId.isEmpty()) {
			propType = (PropertyTypeMaster) persistenceService.find("from PropertyTypeMaster where id=?",
					Long.valueOf(propTypeId));
			prepareCategoryMap(propType);
		} else if (basicProp != null && propWF == null) {
			propType = basicProp.getProperty().getPropertyDetail().getPropertyTypeMaster();
			propTypeId = propType.getId().toString();
			prepareCategoryMap(propType);
		} else if (propWF != null) {
			propType = propWF.getPropertyDetail().getPropertyTypeMaster();
			propTypeId = propType.getId().toString();
			prepareCategoryMap(propType);
		}
		prepareUsageList(propTypeId);

		if (getBasicProp() != null) {
			setPropAddress(ptisCacheMgr.buildAddressByImplemetation(getBasicProp().getAddress()));
			prepareAreaDropDownData(basicProp.getPropertyID().getWard().getId());
		}

		if (propWF != null) {
			setOwnerName(ptisCacheMgr.buildOwnerFullName(propWF.getPropertyOwnerSet()));
			Set<PropertyOwner> ownerSet = propWF.getPropertyOwnerSet();
			if (ownerSet != null && !ownerSet.isEmpty()) {
				for (PropertyOwner owner : ownerSet) {
					Set<Address> addrSet = (Set<Address>) owner.getAddress();
					for (Address address : addrSet) {
						corrsAddress = ptisCacheMgr.buildAddressByImplemetation(address);
						break;
					}
				}
			}
			for (PropertyStatusValues propstatval : basicProp.getPropertyStatusValuesSet()) {
				if (propstatval.getIsActive().equals("W")) {
					setPropStatValForView(propstatval);
				}
			}
		}

		// tax exempted properties
		addDropdownData("taxExemptedList", CommonServices.getTaxExemptedList());
		setupWorkflowDetails();
		setUserInfo();
		LOGGER.debug("Exiting from preapre, ModelId: " + getModelId());

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

	private void prepareCategoryMap(PropertyTypeMaster propType) {
		LOGGER.debug("Entered into prepareCategoryMap, PropTypeMaster: " + propType);

		if (propType != null && !propType.equals("-1")) {
			if (propType.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
				setPropTypeCategoryMap(RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
			} else if (propType.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
				setPropTypeCategoryMap(NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
			} else if (propType.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
				setPropTypeCategoryMap(PropertyTaxConstants.OPEN_PLOT_PROPERTY_TYPE_CATEGORY);
			} else {
				setPropTypeCategoryMap(Collections.EMPTY_MAP);
			}
		} else {
			setPropTypeCategoryMap(Collections.EMPTY_MAP);
		}

		LOGGER.debug("Exiting from prepareCategoryMap");
	}

	private void modifyBasicProp(String docNumber) {
		LOGGER.debug("Entered into modifyBasicProp, BasicProperty: " + basicProp);
		LOGGER.debug("modifyBasicProp: PropTypeId: " + propTypeId + ", PropUsageId: " + propUsageId + ", PropOccId: "
				+ propOccId + ", statusModifyRsn: " + modifyRsn + ", ReasonForModify: " + reasonForModify
				+ ", NoOfAmalgmatedProps: " + ((amalgPropIds != null) ? amalgPropIds.length : "NULL"));

		Date propCompletionDate = null;
		String mutationCode = null;
		Character status = STATUS_WORKFLOW;
		PropertyTypeMasterDAO propTypeMstrDao = PropertyDAOFactory.getDAOFactory().getPropertyTypeMasterDAO();
		PropertyTypeMaster proptypeMstr = propTypeMstrDao.getPropertyTypeMasterById(Integer.valueOf(propTypeId));
		if (!proptypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if ((proptypeMstr.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT) || proptypeMstr.getCode()
					.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) && isfloorDetailsRequired) {
				propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
			} else {
				propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
						.getFloorDetailsProxy());
			}

		} else {
			propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
		}
		// AMALG & BIFUR
		if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn) || PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
			basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, getModifyRsn(),
					propCompletionDate, null, null, null, null));
			if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)) {
				propService.createAmalgPropStatVal(amalgPropIds, basicProp);
			}
			mutationCode = getModifyRsn();
		} else if (PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn)) { // MODIFY
			basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, PROPERTY_MODIFY_REASON_MODIFY,
					propCompletionDate, null, null, null, null));
			mutationCode = getReasonForModify();
		} else if (PROPERTY_MODIFY_REASON_COURT_RULE.equals(getReasonForModify())) { // COURT_RULE
			basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, PROPERTY_MODIFY_REASON_MODIFY,
					propCompletionDate, getCourtOrdNum(), propService.getPropOccupatedDate(getOrderDate()),
					getJudgmtDetails(), null));
			mutationCode = getReasonForModify();
		} else if (PROPERTY_MODIFY_REASON_OBJ.equals(getModifyRsn())) { // OBJ
			PropertyStatusValues wfPropStatVal = (PropertyStatusValues) persistenceService.findByNamedQuery(
					QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProp.getUpicNo(), "W",
					PROPERTY_MODIFY_REASON_MODIFY);
			wfPropStatVal.setExtraField1(sdf.format(propCompletionDate));
			
			if (StringUtils.isNotBlank(modificationType)
					&& modificationType.equalsIgnoreCase(PROPERTY_MODIFY_REASON_DATA_ENTRY)) {
				mutationCode = PROPERTY_MODIFY_REASON_DATA_ENTRY;
			} else {
				mutationCode = getModifyRsn();
			}
		} else if (PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(getModifyRsn())) {
			if (getModelId() != null && !getModelId().equals("")) {
				PropertyImpl propertyWF = (PropertyImpl) getPersistenceService().findByNamedQuery(
						QUERY_PROPERTYIMPL_BYID, Long.valueOf(getModelId()));
				if (propertyWF.getStatus().equals(STATUS_WORKFLOW)) {
					PropertyStatusValues wfPropStatVal = (PropertyStatusValues) persistenceService.findByNamedQuery(
							QUERY_PROPSTATVALUE_BY_BASICPROPID_CODE_ISACTIVE, basicProp.getId(), "W",
							PROPERTY_MODIFY_REASON_MODIFY);
					if (wfPropStatVal != null) {
						wfPropStatVal.setExtraField1(sdf.format(propCompletionDate));
					} else if (allChangesCompleted) {
						basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp,
								PROPERTY_MODIFY_REASON_MODIFY, propCompletionDate, null, null, null, null));
					}
				}
			} else {
				basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp,
						PROPERTY_MODIFY_REASON_MODIFY, propCompletionDate, null, null, null, null));
			}

			mutationCode = getModifyRsn();
		}
		
		basicProp.setExtraField1(isAuthProp);
		
		if (PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(getModifyRsn())
				|| PROPERTY_MODIFY_REASON_MODIFY.equals(getModifyRsn())
				|| PROPERTY_MODIFY_REASON_OBJ.equalsIgnoreCase(getModifyRsn())) {
			
			basicProp.setAllChangesCompleted(allChangesCompleted);
		}
		basicProp.setPropCreateDate(propCompletionDate);
		
		setProperty(propService.createProperty(propertyModel, getAreaOfPlot(), mutationCode, propTypeId, propUsageId,
				propOccId, status, propertyModel.getDocNumber(), null, isfloorDetailsRequired));
		
		propertyModel = (PropertyImpl) propService.createOwnersForNew(propertyModel, oldProperty);
		propertyModel.setBasicProperty(basicProp);
		propertyModel.setEffectiveDate(propCompletionDate);
		propertyModel.getPropertyDetail().setEffective_date(propCompletionDate);
		
		if (!modifyRsn.equals(PROPERTY_MODIFY_REASON_BIFURCATE) && !modifyRsn.equals(PROPERTY_MODIFY_REASON_AMALG)) {
			if (!getAreaId().equals("-1")) {
				propertyModel.setAreaBndry(boundaryDao.getBoundary(Long.valueOf(getAreaId())));
			}
		}

		PropertyImpl newProperty = (PropertyImpl) propService.createDemand(propertyModel, oldProperty, propCompletionDate, isfloorDetailsRequired);

		PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
				"from PropertyTypeMaster ptm where ptm.code = ?", PROPTYPE_OPEN_PLOT);

		Long oldPropTypeId = oldProperty.getPropertyDetail().getPropertyTypeMaster().getId();

		/*
		 * if modifying from OPEN_PLOT to OTHERS or from OTHERS to OPEN_PLOT
		 * property type
		 */
		if ((oldPropTypeId == propTypeMstr.getId() && Long.parseLong(propTypeId) != propTypeMstr.getId())
				|| (oldPropTypeId != propTypeMstr.getId() && Long.parseLong(propTypeId) == propTypeMstr.getId())) {

			if ((propTypeMstr != null) && (StringUtils.equals(propTypeMstr.getId().toString(), propTypeId))) {
				changePropertyDetail(newProperty, new VacantProperty(), 0);
			} else {
				changePropertyDetail(newProperty, new BuiltUpProperty(), newProperty.getPropertyDetail()
						.getNo_of_floors());
			}

		}

		Property modProperty = null;
		Property previousProperty = null;

		if (PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(getModifyRsn()) && propWF != null) {
			previousProperty = propWF;
		} else if (PROPERTY_MODIFY_REASON_OBJ.equalsIgnoreCase(getModifyRsn())) {
			previousProperty = PropertyTaxUtil.getLatestProperty(basicProp, PropertyTaxConstants.STATUS_ISHISTORY);
		} else {
			previousProperty = oldProperty;
		}
		
		if (previousProperty == null) {
			LOGGER.info("modifyBasicProp, Could not get the previous property. DCB for arrears will be incorrect");
		} else {
			modProperty = propService.createDemandForModify(previousProperty, newProperty, propCompletionDate);
			modProperty = propService.createArrearsDemand(previousProperty, propCompletionDate, newProperty);
		}
		
		Map<Installment, Set<EgDemandDetails>> demandDetailsSetByInstallment = null;
		List<Installment> installments  = null;
		//getCurrrentDemand(basicProp.getProperty()).getEgDemandDetails()
			
		Set<EgDemandDetails> oldEgDemandDetailsSet = getOldDemandDetails(oldProperty, newProperty);
		demandDetailsSetByInstallment = getEgDemandDetailsSetByInstallment(oldEgDemandDetailsSet);
		installments = new ArrayList<Installment>(demandDetailsSetByInstallment.keySet());
		Collections.sort(installments);

		for (Installment inst : installments) {
			Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<String, BigDecimal>();

			for (String rsn : DEMAND_RSNS_LIST) {

				EgDemandDetails newDmndDtls = propService.getEgDemandDetailsForReason(
						demandDetailsSetByInstallment.get(inst), rsn);

				if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null) {
					// If there is collection then add to map
					if (newDmndDtls.getAmtCollected().compareTo(BigDecimal.ZERO) > 0) {
						dmdRsnAmt.put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(),
								newDmndDtls.getAmtCollected());
					}
				}
			}

			propService.getExcessCollAmtMap().put(inst, dmdRsnAmt);
		}

		Ptdemand currentDemand = getCurrrentDemand(modProperty);
		
		demandDetailsSetByInstallment = getEgDemandDetailsSetByInstallment(currentDemand.getEgDemandDetails());

		installments = new ArrayList<Installment>(demandDetailsSetByInstallment.keySet());

		Collections.sort(installments);

		for (Installment inst : installments) {
			Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<String, BigDecimal>();

			for (String rsn : DEMAND_RSNS_LIST) {

				EgDemandDetails newDmndDtls = propService.getEgDemandDetailsForReason(
						demandDetailsSetByInstallment.get(inst), rsn);

				if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null) {
					BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(newDmndDtls.getAmount());
					// If there is extraColl then add to map
					if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
						dmdRsnAmt
								.put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(), extraCollAmt);
						newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
						newDmndDtls.setLastUpdatedTimeStamp(new Date());
					}
				}
			}

			propService.getExcessCollAmtMap().put(inst, dmdRsnAmt);
		}

		LOGGER.info("Excess Collection - " + propService.getExcessCollAmtMap());

		propService.adjustExcessCollectionAmount(installments, demandDetailsSetByInstallment, currentDemand);
			

		if (modProperty != null) {
			modProperty.setDocNumber(docNumber);
		}
		/*
		 * in case of modify we are storing user entered address and other
		 * fields like parcelid, boundaries into extrafield6 of property during
		 * the work flow and will save data to actual object at the time of
		 * final approval
		 */
		if (PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn)) {
			StringBuilder string = new StringBuilder(PropertyTaxUtil.buildAddress(propertyAddr));
			string.append("|").append(isBlank(parcelId) ? " " : parcelId).append("|")
					.append(isBlank(northBound) ? " " : northBound).append("|")
					.append(isBlank(southBound) ? " " : southBound).append("|")
					.append(isBlank(eastBound) ? " " : eastBound).append("|")
					.append(isBlank(westBound) ? " " : westBound).append("|");
			modProperty.setExtra_field6(string.toString());
		}

		if (modProperty == null) {
			basicProp.addProperty(newProperty);
		} else {
			basicProp.addProperty(modProperty);
		}
		
		basicPrpertyService.update(basicProp);

		if (!newProperty.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			propService.createAttributeValues(newProperty, null);
		}

		LOGGER.debug("Exiting modifyBasicProp");
	}
	
	private Set<EgDemandDetails> getOldDemandDetails(Property oldProperty, Property newProperty) {
		
		Set<EgDemandDetails> oldDemandDetails = new HashSet<EgDemandDetails>();
		
		for (EgDemandDetails dd : getCurrrentDemand(oldProperty).getEgDemandDetails()) {
			if (dd.getEgDemandReason().getEgInstallmentMaster().getFromDate().before(newProperty.getEffectiveDate())) {
				oldDemandDetails.add(dd);
			}
		}
		
		return oldDemandDetails;
	}
	
	private Map<Installment, Set<EgDemandDetails>> getEgDemandDetailsSetByInstallment(
			Set<EgDemandDetails> demandDetailsSet) {
		Map<Installment, Set<EgDemandDetails>> newEgDemandDetailsSetByInstallment = new HashMap<Installment, Set<EgDemandDetails>>();

		for (EgDemandDetails dd : demandDetailsSet) {

			if (dd.getAmtCollected() == null) {
				dd.setAmtCollected(ZERO);
			}

			if (newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()) == null) {
				Set<EgDemandDetails> ddSet = new HashSet<EgDemandDetails>();
				ddSet.add(dd);
				newEgDemandDetailsSetByInstallment.put(dd.getEgDemandReason().getEgInstallmentMaster(), ddSet);
			} else {
				newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()).add(dd);
			}
		}
		
		return newEgDemandDetailsSetByInstallment;
	}

	/**
	 * @param property
	 * @return
	 */
	private Ptdemand getCurrrentDemand(Property property) {
		Ptdemand currentDemand = null;
		
		for (Ptdemand ptdemand : property.getPtDemandSet()) {
			if (ptdemand.getEgInstallmentMaster().equals(PropertyTaxUtil.getCurrentInstallment())) {
				currentDemand = ptdemand;
				break;
			}
		}
		return currentDemand;
	}

	/**
	 * Changes the property details to {@link BuiltUpProperty} or
	 * {@link VacantProperty}
	 * 
	 * @param modProperty
	 *            the property which is getting modified
	 * 
	 * @param propDetail
	 *            the {@link PropertyDetail} type, either
	 *            {@link BuiltUpProperty} or {@link VacantProperty}
	 * 
	 * @param numOfFloors
	 *            the no. of floors which is dependent on {@link PropertyDetail}
	 * 
	 * @see {@link PropertyDetail}, {@link BuiltUpProperty},
	 *      {@link VacantProperty}
	 */

	private void changePropertyDetail(Property modProperty, PropertyDetail propDetail, Integer numOfFloors) {

		LOGGER.debug("Entered into changePropertyDetail, Property is Vacant Land");

		PropertyDetail propertyDetail = modProperty.getPropertyDetail();

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
		propDetail.setNo_of_floors(numOfFloors);
		propDetail.setFieldIrregular(propertyDetail.getFieldIrregular());
		propDetail.setCompletion_year(propertyDetail.getCompletion_year());
		propDetail.setEffective_date(propertyDetail.getEffective_date());
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

		if (numOfFloors == 0) {
			propDetail.setPropertyUsage(propertyDetail.getPropertyUsage());
		} else {
			propDetail.setPropertyUsage(null);
		}

		propDetail.setExtra_field1(propertyDetail.getExtra_field1());
		propDetail.setExtra_field2(propertyDetail.getExtra_field2());
		propDetail.setExtra_field3(propertyDetail.getExtra_field3());
		propDetail.setExtra_field4(propertyDetail.getExtra_field4());
		propDetail.setExtra_field5(propertyDetail.getExtra_field5());
		propDetail.setExtra_field6(propertyDetail.getExtra_field6());
		propDetail.setManualAlv(propertyDetail.getManualAlv());
		propDetail.setOccupierName(propertyDetail.getOccupierName());

		modProperty.setPropertyDetail(propDetail);

		LOGGER.debug("Exiting from changePropertyDetail");
	}

	private void populateBasicProp() {
		LOGGER.debug("Entered into populateBasicProp");
		if (basicProp == null) {
			if (indexNumber != null && !indexNumber.trim().isEmpty()) {
				setBasicProp((BasicProperty) getPersistenceService().findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
						indexNumber));
			} else if (getModelId() != null && !getModelId().equals("")) {
				setBasicProp(((PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
						Long.valueOf(getModelId()))).getBasicProperty());
			}
		}
		LOGGER.debug("Exiting from populateBasicProp");
	}

	private void modifyBasicPropForObj(PropertyImpl propWF) {
		LOGGER.debug("Entered into modifyBasicPropForObj, PropertyWF: " + propWF);
		Date propCompletionDate = null;
		String mutationCode = null;
		PropertyDetail propDet = null;
		PropertyTypeMasterDAO propTypeMstrDao = PropertyDAOFactory.getDAOFactory().getPropertyTypeMasterDAO();
		PropertyTypeMaster proptypeMstr = propTypeMstrDao.getPropertyTypeMasterById(Integer.valueOf(propTypeId));

		if (!proptypeMstr.getCode().equalsIgnoreCase("OPEN_PLOT")) {
			propDet = new BuiltUpProperty();
			propDet.setProperty(propWF);
			// propWF.setPropertyDetail(new BuiltUpProperty());
			// propWF.getPropertyDetail().setProperty(propWF);
			propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
					.getFloorDetailsProxy());
		} else {
			// propWF.setPropertyDetail(new VacantProperty());
			propDet = new VacantProperty();
			propDet.setProperty(propWF);
			propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
		}
		if (PROPERTY_MODIFY_REASON_OBJ.equals(getModifyRsn())) { // OBJ
			PropertyStatusValues wfPropStatVal = (PropertyStatusValues) persistenceService.findByNamedQuery(
					QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProp.getUpicNo(), "W",
					PROPERTY_MODIFY_REASON_MODIFY);
			wfPropStatVal.setExtraField1(sdf.format(propCompletionDate));
			mutationCode = getModifyRsn();
		}
		basicProp.setExtraField1(isAuthProp);
		if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
			Area area = new Area();
			area.setArea(new Float(areaOfPlot));
			propDet.setSitalArea(area);
		}
		Installment currentInstall = (Installment) persistenceService.find(
				"from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
				PTMODULENAME, new Date(), new Date());
		PropertyMutationMaster propMutMstr = (PropertyMutationMaster) persistenceService.find(
				"from PropertyMutationMaster PM where upper(PM.code) = ?", mutationCode);

		propDet.setPropertyTypeMaster(proptypeMstr);
		propDet.setPropertyMutationMaster(propMutMstr);
		propDet.setExtra_field1(propertyModel.getPropertyDetail().getExtra_field1());
		if (propUsageId != null) {
			PropertyUsage usage = (PropertyUsage) persistenceService.find("from PropertyUsage pu where pu.id = ?",
					Long.valueOf(propUsageId));
			propDet.setPropertyUsage(usage);
		} else {
			propDet.setPropertyUsage(null);
		}
		if (propOccId != null) {
			PropertyOccupation occupancy = (PropertyOccupation) persistenceService.find(
					"from PropertyOccupation po where po.id = ?", Long.valueOf(propOccId));
			propDet.setPropertyOccupation(occupancy);
		} else {
			propDet.setPropertyOccupation(null);
		}
		propDet.setExtra_field2(propertyModel.getPropertyDetail().getExtra_field2());
		propDet.setExtra_field3(propertyModel.getPropertyDetail().getExtra_field3());
		propDet.setExtra_field4(propertyModel.getPropertyDetail().getExtra_field4());
		propDet.setUpdatedTime(new Date());

		if (!proptypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if (!mutationCode.equals("NEW")) {
				propDet.setFloorDetails(new HashSet<FloorIF>());
			}
			for (FloorIF floor : propertyModel.getPropertyDetail().getFloorDetailsProxy()) {
				if (floor != null) {
					floor.setCreatedTimeStamp(new Date());
					PropertyUsage usage = (PropertyUsage) persistenceService.find(
							"from PropertyUsage pu where pu.id = ?", floor.getPropertyUsage().getId());
					PropertyOccupation occupancy = (PropertyOccupation) persistenceService.find(
							"from PropertyOccupation po where po.id = ?", floor.getPropertyOccupation().getId());
					StructureClassification structureClass = (StructureClassification) persistenceService.find(
							"from StructureClassification sc where sc.id = ?", floor.getStructureClassification()
									.getId());
					floor.setPropertyUsage(usage);
					floor.setPropertyOccupation(occupancy);
					floor.setStructureClassification(structureClass);
					propDet.addFloor(floor);
				}
			}
		} else {
			propDet.setFloorDetailsProxy(Collections.EMPTY_LIST);
			propDet.setFloorDetails(Collections.EMPTY_SET);
			PropertyOccupation occupancy = (PropertyOccupation) persistenceService.find(
					"from PropertyOccupation po where po.id = ?", propWF.getPropertyDetail().getPropertyOccupation()
							.getId());
			propDet.setPropertyOccupation(occupancy);
			PropertyUsage usage = (PropertyUsage) persistenceService.find("from PropertyUsage pu where pu.id = ?",
					Long.valueOf(propUsageId));
			propDet.setPropertyUsage(usage);
		}

		propWF.setExtra_field2(propertyModel.getExtra_field2());
		propWF.setInstallment(currentInstall);
		propWF.setEffectiveDate(currentInstall.getFromDate());
		propWF.setLastModifiedBy(propertyTaxUtil.getLoggedInUser(getSession()));
		propWF.setLastModifiedDate(new DateTime());
		propWF.setCreatedDate(new DateTime());

		Set<Ptdemand> ptdmdset = new HashSet<Ptdemand>();
		for (Ptdemand demand : propWF.getPtDemandSet()) {
			demand.getEgDemandDetails().clear();
		}
		// propWF.setPtDemandSet(ptdmdset);
		propWF.setPropertyDetail(propDet);
		propWF.getPropertyDetail().setPtDemandSet(ptdmdset);
		// propService.createDemand(propWF, propCompletionDate);
		propService.createDemandForModify(oldProperty, propertyModel, propCompletionDate);
		propService.createArrearsDemand(oldProperty, propCompletionDate, propWF);

		// setProperty(propWF);
		basicProp = basicPrpertyService.update(basicProp);
		if (!proptypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			propService.createAttributeValues(propertyModel, currentInstall);
		}

		LOGGER.debug("Exiting from modifyBasicPropForObj");
	}

	@SkipValidation
	public String getStatus() {
		LOGGER.debug("Entered into getStatus");
		checkAmalgStatus();
		LOGGER.debug("Exiting from getStatus");
		return "showStatus";
	}

	private void checkAmalgStatus() {
		LOGGER.debug("Entered into checkAmalgStatus, OldPropId: " + oldpropId);

		List<String> code = new ArrayList<String>();
		PropertyStatusValuesDAO propStatusValDAO = PropertyDAOFactory.getDAOFactory().getPropertyStatusValuesDAO();
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		BigDecimal currDemand = BigDecimal.ZERO;
		BigDecimal currDemandDue = BigDecimal.ZERO;
		BigDecimal currCollection = BigDecimal.ZERO;
		BigDecimal arrDemand = BigDecimal.ZERO;
		BigDecimal arrCollection = BigDecimal.ZERO;
		BigDecimal arrearsDue = BigDecimal.ZERO;
		PropertyStatusValues propStatVal = null;
		code.add(PROPERTY_STATUS_MARK_DEACTIVE);
		amalgPropBasicProp = basicPropertyDAO.getBasicPropertyByPropertyID(oldpropId);
		LOGGER.debug("Amalgmated BasicProperty: " + amalgPropBasicProp);
		Map<String, String> wfMap = null;
		String wfStatus = null;
		if (amalgPropBasicProp != null) {
			propStatVal = propStatusValDAO.getLatestPropertyStatusValuesByPropertyIdAndCode(oldpropId, code);
			wfMap = amalgPropBasicProp.getPropertyWfStatus();
			wfStatus = wfMap.get(WFSTATUS);
			if (!wfStatus.equalsIgnoreCase("TRUE")) {
				PropertyImpl oldProp = (PropertyImpl) amalgPropBasicProp.getProperty();
				setOldOwnerName(ptisCacheMgr.buildOwnerFullName(oldProp.getPropertyOwnerSet()));
				setOldPropAddress(ptisCacheMgr.buildAddressByImplemetation(amalgPropBasicProp.getAddress()));

				Map<String, BigDecimal> DmdCollMap = ptDemandDao.getDemandCollMap(oldProp);
				currDemand = DmdCollMap.get("CURR_DMD");
				arrDemand = DmdCollMap.get("ARR_DMD");
				currCollection = DmdCollMap.get("CURR_COLL");
				arrCollection = DmdCollMap.get("ARR_COLL");
				currDemandDue = currDemand.subtract(currCollection);
				arrearsDue = arrDemand.subtract(arrCollection);
			}
		}

		if (amalgPropBasicProp == null) {
			setAmalgStatus("Property does not Exist");
		} else if (propStatVal != null) {
			setAmalgStatus("Property is Marked for Deactivation");
		} else if (!amalgPropBasicProp.isActive()) {
			setAmalgStatus("Property is Deactivated");
		} else if (wfStatus.equalsIgnoreCase("TRUE")) {
			setAmalgStatus("This Property Under Work flow in " + wfMap.get(WFOWNER)
					+ "'s inbox. Please finish pending work flow before doing any transactions on it.");
		} else if (currDemandDue.compareTo(BigDecimal.ZERO) == 1 || arrearsDue.compareTo(BigDecimal.ZERO) == 1) {
			setAmalgStatus("Property has Pending Balance");
		} else {
			setAmalgStatus("Property is Ready for Amalgamation");
		}

		LOGGER.debug("AmalgStatus: " + getAmalgStatus() + "\nExiting from checkAmalgStatus");
	}

	private void setFloorDetails(Property property) {
		LOGGER.debug("Entered into setFloorDetails, Property: " + property);

		Set<FloorIF> flrDtSet = property.getPropertyDetail().getFloorDetails();
		int i = 0;
		for (FloorIF flr : flrDtSet) {
			floorNoStr[i] = (propertyTaxUtil.getFloorStr(flr.getFloorNo()));
			i++;
		}

		LOGGER.debug("Exiting from setFloorDetails: ");
	}

	public List<FloorIF> getFloorDetails() {
		return new ArrayList<FloorIF>(propertyModel.getPropertyDetail().getFloorDetails());
	}

	public void validate() {
		LOGGER.debug("Entered into validate, ReasonForModify: " + reasonForModify + ", ModifyRsn: " + modifyRsn);
		validateProperty(propertyModel, areaOfPlot, dateOfCompletion, chkIsTaxExempted, taxExemptReason, isAuthProp,
				propTypeId, propUsageId, propOccId, isfloorDetailsRequired, isUpdateData());

		if (reasonForModify == null || reasonForModify.equals("-1")) {
			if (modifyRsn != null && !modifyReasons.contains(modifyRsn)) {
				addActionError(getText("mandatory.rsnForMdfy"));
			}
		}
		if (PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn)) {
			if (reasonForModify == null || reasonForModify.equals("-1")) {
				addActionError(getText("mandatory.rsnForMdfy"));
			}
		}
		if (reasonForModify != null && !reasonForModify.equals("-1")
				&& PROPERTY_MODIFY_REASON_COURT_RULE.equals(reasonForModify)) {
			if (courtOrdNum == null || courtOrdNum.equals("")) {
				addActionError(getText("mandatory.courtOrdNo"));
			}
			if (orderDate == null || orderDate.equals("") || orderDate.equals("DD/MM/YYYY")) {
				addActionError(getText("mandatory.courtOrdDt"));
			}
			if (judgmtDetails != null || !judgmtDetails.equals("")) {
				if (judgmtDetails.length() > 1000) {
					addActionError(getText("judgmtDet.length"));
				}
			}
		}
		if (modifyRsn != null && PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)) {
			for (String amalgId : amalgPropIds) {
				if (amalgId == null || amalgId.equals("")) {
					addActionError(getText("propToBeAmal"));
				} else {
					setOldpropId(amalgId);
					checkAmalgStatus();
					if (!getAmalgStatus().equals("Property is Ready for Amalgamation")) {
						addActionError(getAmalgStatus());
					}
				}
			}
		}

		if (PROPERTY_MODIFY_REASON_DATA_UPDATE.equals(modifyRsn) || PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn)) {
			if (StringUtils.isBlank(parcelId)) {
				addActionError(getText("mandatory.parcelId"));
			}
		}

		super.validate();

		if ((PROPERTY_MODIFY_REASON_DATA_UPDATE.equals(modifyRsn) || PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn))
				&& propertyAddr.getHouseNoBldgApt() != null) {
			validateHouseNumber(basicProp.getBoundary().getId(), propertyAddr.getHouseNoBldgApt(), basicProp);
		}

		LOGGER.debug("Exiting from validate, BasicProperty: " + getBasicProp());
	}

	private void setAmalgPropInactive() {
		LOGGER.debug("Entered into setAmalgPropInactive: " + propertyModel);

		if (PROPERTY_MODIFY_REASON_AMALG
				.equals(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode())) {
			for (String amalgId : amalgPropIds) {
				if (amalgId != null && !amalgId.equals("")) {
					BasicProperty amalgBasicProp = (BasicProperty) getPersistenceService().findByNamedQuery(
							PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO, amalgId);
					PropertyStatusValues amalgPropStatVal = new PropertyStatusValues();
					PropertyStatus propertyStatus = (PropertyStatus) getPersistenceService().find(
							"from PropertyStatus where statusCode=?", PROPERTY_STATUS_INACTIVE);
					amalgPropStatVal.setIsActive("Y");
					amalgPropStatVal.setPropertyStatus(propertyStatus);
					amalgPropStatVal.setReferenceDate(new Date());
					amalgPropStatVal.setReferenceNo("0001");
					amalgPropStatVal.setRemarks("Property Amalgamated");
					amalgBasicProp.addPropertyStatusValues(amalgPropStatVal);
					Map<Installment, Map<String, BigDecimal>> amounts = propService
							.prepareRsnWiseDemandForPropToBeDeactivated(amalgBasicProp.getProperty());
					financialUtil.createVoucher(amalgBasicProp.getUpicNo(), amounts, VOUCH_CREATE_RSN_DEACTIVATE);
					amalgBasicProp.setActive(Boolean.FALSE);
					amalgBasicProp.setStatus(propertyStatus);
					// At final approval a new PropetyStatusValues has to
					// created with status INACTIVE and
					// set the amalgBasicProp status as INACTIVE and ISACTIVE as
					// 'N'
					amalgPropStatVal.setBasicProperty(amalgBasicProp);
				}
			}
		}
		LOGGER.debug("Exiting from setAmalgPropInactive");
	}

	private void setPropStatValForView(PropertyStatusValues propstatval) {
		LOGGER.debug("Entered into setPropStatValForView " + propstatval);
		String mutationCode = null;
		
		if (PROPERTY_MODIFY_REASON_MODIFY.equals(propstatval.getPropertyStatus().getStatusCode())) {
			// setting the court rule details
			if (PROPERTY_MODIFY_REASON_COURT_RULE.equals(propWF.getPropertyDetail().getPropertyMutationMaster()
					.getCode())) {
				setCourtOrdNum(propstatval.getReferenceNo());
				setOrderDate(sdf.format(propstatval.getReferenceDate()));
				setJudgmtDetails(propstatval.getRemarks());
			}
			
			mutationCode = propWF.getPropertyDetail().getPropertyMutationMaster().getCode();
			
			if (propstatval.getReferenceNo().startsWith(PropertyTaxConstants.OBJECTION_SEQ_STR)) {
				setObjNum(propstatval.getReferenceNo());
				setObjDate(sdf.format(propstatval.getReferenceDate()));
			}
		}
		if (propWF.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			setDateOfCompletion(propstatval.getExtraField1());
		}

		LOGGER.debug("Entered into setPropStatValForView");
	}

	private void prepareAckMsg() {
		LOGGER.debug("Entered into prepareAckMsg, ModifyRsn: " + modifyRsn);
		//FIX ME
		//User approverUser = userDao.getUserByID(getWorkflowBean().getApproverUserId());
		User approverUser = null;

		if (PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn) || PROPERTY_MODIFY_REASON_OBJ.equals(modifyRsn)
				|| PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(modifyRsn)) {
			setAckMessage(getText("property.modify.forward.success", new String[] { approverUser.getUsername() }));
		} else if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
			setAckMessage(getText("property.bifur.forward.success", new String[] { approverUser.getUsername() }));
		} else if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)) {
			setAckMessage(getText("property.amalg.forward.success", new String[] { approverUser.getUsername() }));
		}

		LOGGER.debug("AckMessage: " + getAckMessage() + "\nExiting from prepareAckMsg");
	}

	/**
	 * Creates voucher whenever there is change(increase/decrease) in demand.
	 */
	private void createVoucher() {

		LOGGER.debug("Entered into Create Voucher method, ModelProperty: " + propertyModel);
		Map<Installment, Map<String, BigDecimal>> amtsOld = propService.prepareRsnWiseDemandForOldProp(basicProp
				.getProperty());
		LOGGER.info("createVoucher: Old property demands===> " + amtsOld);
		Map<Installment, Map<String, BigDecimal>> amtsNew = propService.populateTaxesForVoucherCreation(propertyModel);
		LOGGER.info("createVoucher: New property demands===>" + amtsNew);
		Map<Installment, Map<String, BigDecimal>> amounts = new HashMap<Installment, Map<String, BigDecimal>>();
		BigDecimal amt = ZERO;
		for (Installment inst : amtsNew.keySet()) {
			Map<String, BigDecimal> amtMap = new HashMap<String, BigDecimal>();
			if (amtsOld.get(inst) != null) {
				for (String dmdRsn : amtsNew.get(inst).keySet()) {
					if (amtsOld.get(inst).get(dmdRsn) != null) {
						// subtracting the old value from the new value
						amt = amtsNew.get(inst).get(dmdRsn).subtract(amtsOld.get(inst).get(dmdRsn));
					} else {
						amt = amtsNew.get(inst).get(dmdRsn);
					}
					// if demand change amount not equal to zero then adding
					// that amount to amtMap.
					if (!amt.setScale(2, ROUND_HALF_UP).equals(BigDecimal.ZERO.setScale(2))) {
						amtMap.put(dmdRsn, amt);
					}
				}
				// to check whether the amtMap contains all the old demand
				// reasons or not
				for (String rsn : amtsOld.get(inst).keySet()) {
					// if map doesn't contain the old dmdrsn and
					// new property doesn't have the old demand reason
					if (!amtMap.containsKey(rsn) && amtsNew.get(inst).get(rsn) == null) {
						amtMap.put(rsn, amtsOld.get(inst).get(rsn).negate());
					}
				}
				if (amtMap.size() > 0) {
					amounts.put(inst, amtMap);
				}
			} else {
				amounts.put(inst, amtsNew.get(inst));
			}
		}

		if (PROPERTY_MODIFY_REASON_DATA_ENTRY.equalsIgnoreCase(propertyModel.getPropertyDetail()
				.getPropertyMutationMaster().getCode())) {
			amounts = amtsNew;
		}

		// If Change in demand ( Either increment or decrement )
		LOGGER.info("createVoucher: Amounts===>" + amounts);
		if (amounts.size() > 0) {
			financialUtil.createVoucher(propertyModel.getBasicProperty().getUpicNo(), amounts, propertyModel
					.getPropertyDetail().getPropertyMutationMaster().getMutationName());
		} else {
			LOGGER.info("createVoucher: No demand change : Voucher is not created");
		}

		LOGGER.debug("Exiting from createVoucher");
	}

	private PropertyDocs createPropertyDocs(BasicProperty basicProperty, String docNumber, String modifyReason) {
		PropertyDocs pd = new PropertyDocs();
		pd.setDocNumber(docNumber);
		pd.setBasicProperty(basicProperty);
		if (PROPERTY_MODIFY_REASON_MODIFY.equals(modifyRsn) || PROPERTY_MODIFY_REASON_OBJ.equals(modifyRsn)
				|| PROPERTY_MODIFY_REASON_DATA_ENTRY.equals(modifyRsn)) {
			pd.setReason(DOCS_MODIFY_PROPERTY);
		} else if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
			pd.setReason(DOCS_BIFURCATE_PROPERTY);
		} else if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)) {
			pd.setReason(DOCS_AMALGAMATE_PROPERTY);
		}

		return pd;
	}

	private void transitionWorkFlow() {
		
		if (workflowBean != null) {
			LOGGER.debug("Entered method : transitionWorkFlow. Action : " + workflowBean.getActionName() + "Property: "
					+ propertyModel);
		} else {
			LOGGER.debug("transitionWorkFlow: workflowBean is NULL");
		}

		workflowAction = propertyTaxUtil.initWorkflowAction(propertyModel, workflowBean,
				Integer.valueOf(EGOVThreadLocals.getUserId()), eisCommonService);
		
		if (workflowAction.isNoWorkflow()) {
			startWorkFlow();
		}

		if (workflowAction.isStepRejectAndOwnerNextPositionSame()) {
			endWorkFlow();
		} else {		
			workflowAction.changeState();
		}
		
		if (workflowAction.isNoticeGenerated()) {
			endWorkFlow();
		}

		LOGGER.debug("transitionWorkFlow: Property transitioned to " + propertyModel.getState().getValue());
		propertyImplService.persist(propertyModel);

		LOGGER.debug("Exiting method : transitionWorkFlow");
	}

	@SuppressWarnings("unchecked")
	private void prepareAreaDropDownData(Long wardId) {
		LOGGER.debug("Entered into prepareAreaDropDownData, WardId: " + wardId);
		List<Boundary> areaNewList = new ArrayList<Boundary>();
		areaNewList = getPersistenceService()
				.findAllBy(
						"from Boundary BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
						AREA_BNDRY_TYPE, wardId);
		addDropdownData("areaList", areaNewList);
		LOGGER.debug("NoOfAreas in the ward: " + ((areaNewList != null) ? areaNewList.size() : "List is NULL")
				+ "\nExiting from prepareAreaDropDownData");
	}

	private void modifyPropertyAuditTrail(BasicProperty basicProperty, Property property, String action,
			String auditDetails2) {
		Map<String, String> propTypeCategoryMap = new TreeMap<String, String>();
		String propCat = "";
		String locFact = "";
		StringBuilder auditDetail1 = new StringBuilder();
		if (property.getPropertyDetail().getExtra_field5() != null) {
			PropertyTypeMaster propType = property.getPropertyDetail().getPropertyTypeMaster();

			if (propType != null) {
				if (propType.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
					propTypeCategoryMap.putAll(PropertyTaxConstants.RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
				} else if (propType.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
					propTypeCategoryMap.putAll(PropertyTaxConstants.NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
				} else if (propType.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
					propTypeCategoryMap.putAll(PropertyTaxConstants.OPEN_PLOT_PROPERTY_TYPE_CATEGORY);
				}
			}
			propCat = propTypeCategoryMap.get(property.getPropertyDetail().getExtra_field5());
		}
		if (property.getPropertyDetail().getExtra_field6() != null) {
			Category category = (Category) persistenceService.find("from Category c where c.id = ?",
					Long.valueOf(property.getPropertyDetail().getExtra_field6()));
			locFact = category.getCategoryName();
		}
		auditDetail1
				.append("Property Type : ")
				.append(property.getPropertyDetail().getPropertyTypeMaster().getType() != null ? property
						.getPropertyDetail().getPropertyTypeMaster().getType() : "")
				.append(AUDITDATA_STRING_SEP)
				.append("Property Category : ")
				.append(propCat)
				.append(AUDITDATA_STRING_SEP)
				.append("Location Factor : ")
				.append(locFact)
				.append(AUDITDATA_STRING_SEP)
				.append("Authorised Property : ")
				.append(basicProperty.getExtraField1() != null ? basicProperty.getExtraField1() : "")
				.append(AUDITDATA_STRING_SEP)
				.append("Reason For Modification : ")
				.append(property.getPropertyDetail().getPropertyMutationMaster() != null ? property.getPropertyDetail()
						.getPropertyMutationMaster().getMutationName() : "");
		LOGGER.debug("Audit String : " + auditDetail1.toString());
		//propertyTaxUtil.generateAuditEvent(action, basicProperty, auditDetail1.toString(), auditDetails2);
	}

	public PropertyImpl updatePropertyForMigratedProp(PropertyImpl property, String areaOfPlot, String mutationCode,
			String propTypeId, String propUsageId, String propOccId, String docnumber, String nonResPlotArea,
			boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into modifyPropertyForMigratedProp");
		LOGGER.debug("modifyPropertyForMigratedProp: Property: " + property + ", areaOfPlot: " + areaOfPlot
				+ ", mutationCode: " + mutationCode + ",propTypeId: " + propTypeId + ", propUsageId: " + propUsageId
				+ ", propOccId: " + propOccId);

		if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
			Area area = new Area();
			area.setArea(new Float(areaOfPlot));
			property.getPropertyDetail().setSitalArea(area);
		}

		if (nonResPlotArea != null && !nonResPlotArea.isEmpty()) {
			Area area = new Area();
			area.setArea(new Float(nonResPlotArea));
			property.getPropertyDetail().setNonResPlotArea(area);
		}
		// [Nayeem] the fieldVerified value is 'N' for migrated property
		// Check with Ramki as wat to set for Data Update.
		property.getPropertyDetail().setFieldVerified('Y');
		property.getPropertyDetail().setProperty(property);
		PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) persistenceService.find(
				"from PropertyTypeMaster PTM where PTM.id = ?", Long.valueOf(propTypeId));
		String propTypeCode = propTypeMstr.getCode();
		if (propTypeMstr != null) {
			if (!(propTypeCode.equals(PROPTYPE_NON_RESD) || propTypeCode.equals(PROPTYPE_RESD) || propTypeCode
					.equals(PROPTYPE_OPEN_PLOT))) {
				// extra_field5 contains the property type category, so setting
				// to null for other than
				// NR, R & OP i.e., for Govt. Property & Mixed
				property.getPropertyDetail().setExtra_field5(null);
			}
		}

		boolean isNofloors = (PROPTYPE_OPEN_PLOT.equals(propTypeCode) || (PROPTYPE_CENTRAL_GOVT.equals(propTypeCode) || PROPTYPE_STATE_GOVT
				.equals(propTypeCode) && isfloorDetailsRequired));

		if (propUsageId != null && isNofloors) {
			PropertyUsage usage = (PropertyUsage) persistenceService.find("from PropertyUsage pu where pu.id = ?",
					Long.valueOf(propUsageId));
			property.getPropertyDetail().setPropertyUsage(usage);
		} else {
			property.getPropertyDetail().setPropertyUsage(null);
		}

		if (propOccId != null && isNofloors) {
			PropertyOccupation occupancy = (PropertyOccupation) persistenceService.find(
					"from PropertyOccupation po where po.id = ?", Long.valueOf(propOccId));
			property.getPropertyDetail().setPropertyOccupation(occupancy);
		} else {
			property.getPropertyDetail().setPropertyOccupation(null);
		}

		if (propTypeMstr.getCode().equals(PROPTYPE_OPEN_PLOT)) {
			property.getPropertyDetail().setPropertyType(VACANT_PROPERTY);
		} else {
			property.getPropertyDetail().setPropertyType(BUILT_UP_PROPERTY);
		}

		property.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
		propertyModel.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
		propertyModel.getPropertyDetail().setPropertyMutationMaster(
				property.getPropertyDetail().getPropertyMutationMaster());
		property.getPropertyDetail().setUpdatedTime(new Date());

		propService.createFloors(propertyModel, mutationCode, propUsageId, propOccId, isfloorDetailsRequired);

		for (FloorIF floor : property.getPropertyDetail().getFloorDetails()) {
			for (FloorIF newFloorInfo : propertyModel.getPropertyDetail().getFloorDetails()) {
				if (floor.getId().equals(newFloorInfo.getId())) {
					floor.setExtraField1(newFloorInfo.getExtraField1());
					floor.setUnitType(newFloorInfo.getUnitType());
					floor.setUnitTypeCategory(newFloorInfo.getUnitTypeCategory());
					floor.setFloorNo(newFloorInfo.getFloorNo());
					floor.setExtraField7(newFloorInfo.getExtraField7());
					floor.setExtraField2(newFloorInfo.getExtraField2());
					floor.setBuiltUpArea(newFloorInfo.getBuiltUpArea());
					floor.setPropertyUsage(newFloorInfo.getPropertyUsage());
					floor.setPropertyOccupation(newFloorInfo.getPropertyOccupation());
					floor.setWaterRate(newFloorInfo.getWaterRate());
					floor.setExtraField3(newFloorInfo.getExtraField3());
					floor.setStructureClassification(newFloorInfo.getStructureClassification());
					floor.setDepreciationMaster(newFloorInfo.getDepreciationMaster());
					floor.setRentPerMonth(newFloorInfo.getRentPerMonth());
					floor.setExtraField4(newFloorInfo.getExtraField4());
					floor.setExtraField5(newFloorInfo.getExtraField5());
					floor.setExtraField6(newFloorInfo.getExtraField6());
					floor.setManualAlv(newFloorInfo.getManualAlv());
					break;
				}
			}
		}
		property.getPropertyDetail().setNo_of_floors(property.getPropertyDetail().getFloorDetails().size());
		property.setDocNumber(docnumber);
		LOGGER.debug("Exiting from createProperty");
		return property;
	}

	private void updateBasicPropForMigratedProp(String docNumber, PropertyImpl existingProp) {
		LOGGER.debug("Entered into modifyBasicPropForMigratedProp, BasicProperty: " + basicProp);
		LOGGER.debug("modifyBasicPropForMigratedProp: PropTypeId: " + propTypeId + ", PropUsageId: " + propUsageId
				+ ", PropOccId: " + propOccId + ", statusModifyRsn: " + modifyRsn + ", ReasonForModify: "
				+ reasonForModify);

		Date propCompletionDate = null;
		PropertyTypeMasterDAO propTypeMstrDao = PropertyDAOFactory.getDAOFactory().getPropertyTypeMasterDAO();
		PropertyTypeMaster proptypeMstr = propTypeMstrDao.getPropertyTypeMasterById(Integer.valueOf(propTypeId));
		if (!proptypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if ((proptypeMstr.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT) || proptypeMstr.getCode()
					.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) && isfloorDetailsRequired) {
				propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
			} else {
				propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
						.getFloorDetailsProxy());
			}

		} else {
			propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
		}

		basicProp.setExtraField1(isAuthProp);
		basicProp.setPropCreateDate(propCompletionDate);
		existingProp = updatePropertyForMigratedProp(existingProp, getAreaOfPlot(), PROP_CREATE_RSN, propTypeId,
				propUsageId, propOccId, propertyModel.getDocNumber(), null, isfloorDetailsRequired);
		existingProp.setBasicProperty(basicProp);
		existingProp.setExtra_field2(propertyModel.getExtra_field2());
		existingProp.setEffectiveDate(propCompletionDate);
		existingProp.getPropertyDetail().setEffective_date(propCompletionDate);
		existingProp.getPropertyDetail().setExtra_field1(propertyModel.getPropertyDetail().getExtra_field1());
		existingProp.getPropertyDetail().setExtra_field2(propertyModel.getPropertyDetail().getExtra_field2());
		existingProp.getPropertyDetail().setExtra_field3(propertyModel.getPropertyDetail().getExtra_field3());
		existingProp.getPropertyDetail().setExtra_field4(propertyModel.getPropertyDetail().getExtra_field4());
		existingProp.getPropertyDetail().setExtra_field5(propertyModel.getPropertyDetail().getExtra_field5());
		existingProp.getPropertyDetail().setExtra_field6(propertyModel.getPropertyDetail().getExtra_field6());
		existingProp.getPropertyDetail().setManualAlv(propertyModel.getPropertyDetail().getManualAlv());
		existingProp.getPropertyDetail().setOccupierName(propertyModel.getPropertyDetail().getOccupierName());

		existingProp.setDocNumber(docNumber);
		updateAddress();
		basicProp.setGisReferenceNo(parcelId);
		basicProp.getPropertyID().setNorthBoundary(northBound);
		basicProp.getPropertyID().setSouthBoundary(southBound);
		basicProp.getPropertyID().setEastBoundary(eastBound);
		basicProp.getPropertyID().setWestBoundary(westBound);

		HibernateUtil.getCurrentSession().merge(existingProp);
		basicPrpertyService.update(basicProp);

		LOGGER.debug("Exiting modifyBasicPropForMigratedProp");
	}

	@ValidationErrorPage(value = "new")
	public String updateData() {
		LOGGER.debug("updateData: Property modification started for Migrated Property, PropertyId: " + propertyModel);
		long startTimeMillis = System.currentTimeMillis();

		if (ASSISTANT_ROLE.equals(userRole)) {

			PropertyImpl nonHistoryProperty = (PropertyImpl) basicProp.getProperty();
			if (getDocNumber() != null && !getDocNumber().equals("")) {
				PropertyDocs pd = createPropertyDocs(basicProp, getDocNumber(), modifyRsn);
				basicProp.addDocs(pd);
			}

			updateBasicPropForMigratedProp(getDocNumber(), nonHistoryProperty);
			setAckMessage("Migrated Property updated Successfully in System with Index Number: ");

			modifyPropertyAuditTrail(basicProp, propertyModel, DATAUPDATE_AUDIT_ACTION, null);

			long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
			LOGGER.info("updateData: Property modified successfully in system with Index Number: "
					+ basicProp.getUpicNo() + "; Time taken(ms) = " + elapsedTimeMillis);
		}

		return RESULT_ACK;
	}

	@SkipValidation
	public String modifyOrDataUpdateForm() {
		LOGGER.debug("Entered into modifyOrDataUpdateForm");
		String resultPage = "";
		if (PROPERTY_MODIFY_REASON_DATA_UPDATE.equals(modifyRsn) && basicProp.getIsMigrated().equals('N')) {
			setErrorMessage(" This is not a migrated property ");
			resultPage = RESULT_ERROR;
		} else {
			resultPage = populateFormData();
		}

		LOGGER.debug("Exiting from modifyOrDataUpdateForm");
		return resultPage;
	}

	private void updateAddress() {
		LOGGER.debug("Entered into updateAddress");

		PropertyAddress addr = basicProp.getAddress();
		addr.setMobileNo(propertyAddr.getMobileNo());
		addr.setEmailAddress(propertyAddr.getEmailAddress());
		addr.setHouseNoBldgApt(propertyAddr.getHouseNoBldgApt());
		addr.setDoorNumOld(propertyAddr.getDoorNumOld());
		addr.setStreetRoadLine(propertyAddr.getStreetRoadLine());
		addr.setPinCode(propertyAddr.getPinCode());
		addr.setExtraField1(propertyAddr.getExtraField1());
		addr.setExtraField2(propertyAddr.getExtraField2());
		addr.setExtraField3(propertyAddr.getExtraField3());
		addr.setExtraField4(propertyAddr.getExtraField4());

		LOGGER.debug("Exiting from updateAddress");
	}

	public BasicProperty getBasicProp() {
		return basicProp;
	}

	public void setBasicProp(BasicProperty basicProp) {
		this.basicProp = basicProp;
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

	public String getModifyRsn() {
		return modifyRsn;
	}

	public void setModifyRsn(String modifyRsn) {
		this.modifyRsn = modifyRsn;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getPropAddress() {
		return propAddress;
	}

	public void setPropAddress(String propAddress) {
		this.propAddress = propAddress;
	}

	public String getAreaOfPlot() {
		return areaOfPlot;
	}

	public void setAreaOfPlot(String areaOfPlot) {
		this.areaOfPlot = areaOfPlot;
	}

	public Map<String, String> getWaterMeterMap() {
		return waterMeterMap;
	}

	public void setWaterMeterMap(Map<String, String> waterMeterMap) {
		this.waterMeterMap = waterMeterMap;
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

	public TreeMap<Integer, String> getFloorNoMap() {
		return floorNoMap;
	}

	public void setFloorNoMap(TreeMap<Integer, String> floorNoMap) {
		this.floorNoMap = floorNoMap;
	}

	public String getReasonForModify() {
		return reasonForModify;
	}

	public void setReasonForModify(String reasonForModify) {
		this.reasonForModify = reasonForModify;
	}

	public String getDateOfCompletion() {
		return dateOfCompletion;
	}

	public void setDateOfCompletion(String dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
	}

	public Map<String, String> getModifyReasonMap() {
		return modifyReasonMap;
	}

	public void setModifyReasonMap(Map<String, String> modifyReasonMap) {
		this.modifyReasonMap = modifyReasonMap;
	}

	public String[] getAmalgPropIds() {
		return amalgPropIds;
	}

	public void setAmalgPropIds(String[] amalgPropIds) {
		this.amalgPropIds = amalgPropIds;
	}

	public PropertyService getPropService() {
		return propService;
	}

	public void setPropService(PropertyService propService) {
		this.propService = propService;
	}

	public String getCourtOrdNum() {
		return courtOrdNum;
	}

	public void setCourtOrdNum(String courtOrdNum) {
		this.courtOrdNum = courtOrdNum;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getJudgmtDetails() {
		return judgmtDetails;
	}

	public void setJudgmtDetails(String judgmtDetails) {
		this.judgmtDetails = judgmtDetails;
	}

	public PropertyImpl getOldProperty() {
		return oldProperty;
	}

	public void setOldProperty(PropertyImpl oldProperty) {
		this.oldProperty = oldProperty;
	}

	public PropertyImpl getProperty() {
		return propertyModel;
	}

	public void setProperty(PropertyImpl property) {
		this.propertyModel = property;
	}

	public String getIsAuthProp() {
		return isAuthProp;
	}

	public void setIsAuthProp(String isAuthProp) {
		this.isAuthProp = isAuthProp;
	}

	public PersistenceService<BasicProperty, Long> getBasicPrpertyService() {
		return basicPrpertyService;
	}

	public void setPropertyImplService(PersistenceService<Property, Long> propertyImplService) {
		this.propertyImplService = propertyImplService;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public String getAmalgStatus() {
		return amalgStatus;
	}

	public void setAmalgStatus(String amalgStatus) {
		this.amalgStatus = amalgStatus;
	}

	public BasicProperty getAmalgPropBasicProp() {
		return amalgPropBasicProp;
	}

	public void setAmalgPropBasicProp(BasicProperty amalgPropBasicProp) {
		this.amalgPropBasicProp = amalgPropBasicProp;
	}

	public String getOldpropId() {
		return oldpropId;
	}

	public void setOldpropId(String oldpropId) {
		this.oldpropId = oldpropId;
	}

	public String getOldOwnerName() {
		return oldOwnerName;
	}

	public void setOldOwnerName(String oldOwnerName) {
		this.oldOwnerName = oldOwnerName;
	}

	public String getOldPropAddress() {
		return oldPropAddress;
	}

	public void setOldPropAddress(String oldPropAddress) {
		this.oldPropAddress = oldPropAddress;
	}

	public Map<String, String> getAmenitiesMap() {
		return amenitiesMap;
	}

	public void setAmenitiesMap(Map<String, String> amenitiesMap) {
		this.amenitiesMap = amenitiesMap;
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

	public String getCorrsAddress() {
		return corrsAddress;
	}

	public void setCorrsAddress(String corrsAddress) {
		this.corrsAddress = corrsAddress;
	}

	public String[] getFloorNoStr() {
		return floorNoStr;
	}

	public void setFloorNoStr(String[] floorNoStr) {
		this.floorNoStr = floorNoStr;
	}

	public String getObjNum() {
		return objNum;
	}

	public void setObjNum(String objNum) {
		this.objNum = objNum;
	}

	public String getObjDate() {
		return objDate;
	}

	public void setObjDate(String objDate) {
		this.objDate = objDate;
	}

	public String getAckMessage() {
		return ackMessage;
	}

	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
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

	public String getAmenities() {
		return amenities;
	}

	public void setAmenities(String amenities) {
		this.amenities = amenities;
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

	public boolean isIsfloorDetailsRequired() {
		return isfloorDetailsRequired;
	}

	public void setIsfloorDetailsRequired(boolean isfloorDetailsRequired) {
		this.isfloorDetailsRequired = isfloorDetailsRequired;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public BoundaryDAO getBoundaryDao() {
		return boundaryDao;
	}

	public void setBoundaryDao(BoundaryDAO boundaryDao) {
		this.boundaryDao = boundaryDao;
	}

	public boolean isUpdateData() {
		return updateData;
	}

	public void setUpdateData(boolean updateData) {
		this.updateData = updateData;
	}

	public PersistenceService<FloorIF, Long> getFloorService() {
		return floorService;
	}

	public void setFloorService(PersistenceService<FloorIF, Long> floorService) {
		this.floorService = floorService;
	}

	public PropertyAddress getPropertyAddr() {
		return propertyAddr;
	}

	public void setPropertyAddr(PropertyAddress propertyAddr) {
		this.propertyAddr = propertyAddr;
	}

	public String getNorthBound() {
		return northBound;
	}

	public void setNorthBound(String northBound) {
		this.northBound = northBound;
	}

	public String getSouthBound() {
		return southBound;
	}

	public void setSouthBound(String southBound) {
		this.southBound = southBound;
	}

	public String getEastBound() {
		return eastBound;
	}

	public void setEastBound(String eastBound) {
		this.eastBound = eastBound;
	}

	public String getWestBound() {
		return westBound;
	}

	public void setWestBound(String westBound) {
		this.westBound = westBound;
	}

	public String getParcelId() {
		return parcelId;
	}

	public void setParcelId(String parcelId) {
		this.parcelId = parcelId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return propertyTaxNumberGenerator;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public List<PropertyOwner> getPropertyOwners() {
		return propertyOwners;
	}

	public void setPropertyOwners(List<PropertyOwner> propertyOwners) {
		this.propertyOwners = propertyOwners;
	}

	public WorkflowDetails getWorkflowAction() {
		return workflowAction;
	}

	public void setWorkflowAction(WorkflowDetails workflowAction) {
		this.workflowAction = workflowAction;
	}
	
	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getModificationType() {
		return modificationType;
	}

	public void setModificationType(String modificationType) {
		this.modificationType = modificationType;
	}
	
	public boolean getIsTenantFloorPresent() {
		return this.isTenantFloorPresent;
	}
	
	public void setIsTenantFloorPresent(boolean isTenantFloorPresent) {
		this.isTenantFloorPresent = isTenantFloorPresent;
	}
	
	public String getMode() {
		return this.mode;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}
}
