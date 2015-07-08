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
/**
 * 
 */
package org.egov.ptis.actions.objection;

import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.actions.view.ViewPropertyAction;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusValuesDAO;
import org.egov.ptis.domain.entity.objection.Objection;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Results({ @Result(name = "new", location = "objection-new.jsp"),
    @Result(name = "message", location = "objection-message.jsp"),
    @Result(name = "view", location = "objection-view.jsp"),
    @Result(name = "ack", location = "objection-ack.jsp")})

public class RevisionPetitionAction extends PropertyTaxBaseAction {

	private static final long serialVersionUID = 1L;

	public static final String STRUTS_RESULT_MESSAGE = "message";
	
	private final Logger LOGGER = Logger.getLogger(RevisionPetitionAction.class);
	private ViewPropertyAction viewPropertyAction = new ViewPropertyAction();
	private Objection objection = new Objection();
	private String propertyId;
	private Map<String, Object> viewMap;
	private PersistenceService<Objection, Long> objectionService;
	protected WorkflowService<Objection> objectionWorkflowService;
	private String ownerName;
	private String propertyAddress;
	private PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	private PersistenceService<Property, Long> propertyImplService;
	private String propTypeId;
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private PropertyService propService;
	   private PropertyStatusValues propStatVal ;
        private String reasonForModify;
        private TreeMap<Integer, String> floorNoMap;
        
	@Autowired
        private PropertyStatusValuesDAO propertyStatusValuesDAO;
        
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;

	private boolean isShowAckMessage;
	@Autowired
        private PtDemandDao ptDemandDAO;
	
	@Autowired
	private UserService userService;
	
        @Autowired
        private PropertyStatusDAO propertyStatusDAO;
        
        @Autowired
        private EgwStatusHibernateDAO egwStatusDAO; 
        @Autowired
        private EisCommonService eisCommonService;
        
	@Autowired
	private ApplicationNumberGenerator applicationNumberGenerator;
	
	public RevisionPetitionAction() {
		
	        addRelatedEntity("basicProperty", BasicPropertyImpl.class);
		addRelatedEntity("referenceProperty.propertyDetail.propertyTypeMaster", PropertyTypeMaster.class);
                addRelatedEntity("referenceProperty", PropertyImpl.class);
                
                addRelatedEntity("referenceProperty.propertyDetail.floorType", FloorType.class);
                addRelatedEntity("referenceProperty.propertyDetail.roofType", RoofType.class);
                addRelatedEntity("referenceProperty.propertyDetail.wallType", WallType.class);
                addRelatedEntity("referenceProperty.propertyDetail.woodType", WoodType.class);
        
                 
                this.addRelatedEntity("referenceProperty.propertyDetail.floorDetails.unitType",
                                PropertyTypeMaster.class);
                this.addRelatedEntity("referenceProperty.propertyDetail.floorDetails.propertyUsage",
                                PropertyUsage.class);
                this.addRelatedEntity("referenceProperty.propertyDetail.floorDetails.propertyOccupation",
                                PropertyOccupation.class);
                this.addRelatedEntity("referenceProperty.propertyDetail.floorDetails.structureClassification",
                                StructureClassification.class);
                this.addRelatedEntity("structureClassification",
                        StructureClassification.class);
	}

	@Override
	public Objection getModel() {

		return objection;
	}

	@SuppressWarnings("unchecked")
	@Override
            public void prepare() {
                // to merge the new values from jsp with existing
                if (objection.getId() != null) {
                    objection = objectionService.findById(objection.getId(), false);
        
                }
                super.prepare();
                setUserInfo();
                setupWorkflowDetails();
        
                List<WallType> wallTypes = getPersistenceService().findAllBy("from WallType order by name");
                List<WoodType> woodTypes = getPersistenceService().findAllBy("from WoodType order by name");
                List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy(
                        "from PropertyTypeMaster order by orderNo");
                List<PropertyMutationMaster> propMutList = getPersistenceService().findAllBy(
                        "from PropertyMutationMaster where type = 'MODIFY' and code in('OBJ')");
                List<String> StructureList = getPersistenceService().findAllBy("from StructureClassification");
                List<PropertyUsage> usageList = getPersistenceService().findAllBy("from PropertyUsage order by usageName");
                List<PropertyOccupation> propOccList = getPersistenceService().findAllBy("from PropertyOccupation");
                List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");
                setFloorNoMap(CommonServices.floorMap());
                addDropdownData("floorType", getPersistenceService().findAllBy("from FloorType order by name"));
                addDropdownData("roofType", getPersistenceService().findAllBy("from RoofType order by name"));
                addDropdownData("wallType", wallTypes);
                addDropdownData("woodType", woodTypes);
                addDropdownData("PropTypeMaster", propTypeList);
                addDropdownData("OccupancyList", propOccList);
                addDropdownData("UsageList", usageList);
                addDropdownData("MutationList", propMutList);
                addDropdownData("StructureList", StructureList);
                addDropdownData("AgeFactorList", ageFacList);
                addDropdownData("Appartments", Collections.EMPTY_LIST);
        
            }
	@SkipValidation
        @Action(value = "/revPetition/revPetition-newForm")
	public String newForm() {
		LOGGER.debug("Entered into newForm");
		getPropertyView(propertyId); 
	 //	Map<String, String> wfMap = objection.getBasicProperty().getPropertyWfStatus();
		if (objection!=null && objection.getBasicProperty()!=null && objection.getBasicProperty().isUnderWorkflow()) {
			addActionMessage(getText("property.state.objected", new String[] { objection
					.getBasicProperty().getUpicNo() }));
			return STRUTS_RESULT_MESSAGE;
		} else {
			setupWorkflowDetails();
		}

		return NEW;
	}
	@Action(value = "/revPetition/revPetition") 
        public String create() {
            LOGGER.debug("ObjectionAction | Create | start " + objection);
            setupWorkflowDetails();
          
            objection.setObjectionNumber(applicationNumberGenerator.generate());
    
            objection.getBasicProperty().setStatus(
                    propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_OBJECTED_STR));
            EgwStatus egwStatus = egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
                    PropertyTaxConstants.OBJECTION_CREATED);
            objection.setEgwStatus(egwStatus);
    
            if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
                updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED, WFLOW_ACTION_STEP_FORWARD);
                // FIX ME
                // objection.getState().setText1(PropertyTaxConstants.OBJECTION_RECORD_SAVED);
            } else {
                updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED, PropertyTaxConstants.OBJECTION_ADDHEARING_DATE);
            }
    
            addActionMessage(getText("objection.success") + objection.getObjectionNumber());
            objectionService.applyAuditing(objection.getState());
            objectionService.persist(objection);
            LOGGER.debug("ObjectionAction | Create | End " + objection);
            return STRUTS_RESULT_MESSAGE;
        }

   
        @Action(value = "/revPetition/revPetition-addHearingDate")
        public String addHearingDate() {
            LOGGER.debug("ObjectionAction | addHearingDate | start " + objection);
            /*
             * vaidatePropertyDetails(); if (hasErrors()) { return "view"; }
             */
            if (objection.getHearings().get(objection.getHearings().size() - 1).getPlannedHearingDt()
                    .before(objection.getRecievedOn())) {
                setupWorkflowDetails();
                throw new ValidationException(Arrays.asList(new ValidationError("accountdetailkey",
                        getText("receivedon.greaterThan.plannedhearingdate"))));
    
            }
            // modifyBasicProp();
            if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
                updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED, PropertyTaxConstants.OBJECTION_ADDHEARING_DATE);
                // FIX ME
                // objection.getState().setText1(PropertyTaxConstants.OBJECTION_HEARINGDATE_SAVED);
    
            } else {
                updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_FIXED,
                        PropertyTaxConstants.OBJECTION_RECORD_GENERATEHEARINGNOTICE);
            }
            // propertyImplService.update(objection.getReferenceProperty());
            objectionService.update(objection);
            LOGGER.debug("ObjectionAction | addHearingDate | End " + objection);
            return STRUTS_RESULT_MESSAGE;
        }

        @ValidationErrorPage(value = "view")
        @Action(value = "/revPetition/revPetition-generateHearingNotice")  
          public String generateHearingNotice() {
            
            if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
                updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_FIXED, PropertyTaxConstants.OBJECTION_RECORD_GENERATEHEARINGNOTICE);
               } else {
                updateStateAndStatus(PropertyTaxConstants.OBJECTION_RECORD_GENERATEHEARINGNOTICE,
                        PropertyTaxConstants.OBJECTION_RECORD_HEARINGDETAILS);
            }

                 PropertyImpl refNewProperty = propService.creteNewPropertyForObjectionWorkflow(objection.getBasicProperty(),
                         objection.getObjectionNumber(), objection.getRecievedOn(), objection.getCreatedBy(), null,
                         PROPERTY_MODIFY_REASON_OBJ);
                 
             /*    PropertyImpl newProperty = (PropertyImpl) propService.createDemand(refNewProperty,
                         oldProperty, propCompletionDate, isfloorDetailsRequired);

              */   
                 //propertyImplService.persist(refNewProperty);
                 objection.getBasicProperty().setUnderWorkflow(Boolean.TRUE);
                 propertyImplService.getSession().flush();
                 objection.setReferenceProperty(refNewProperty);
                 objectionService.update(objection);
            return STRUTS_RESULT_MESSAGE;
        }
	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition/revPetition-recordHearingDetails")  
          public String recordHearingDetails() {
        
                LOGGER.debug("ObjectionAction | recordHearingDetails | start "
                        + objection.getHearings().get(objection.getHearings().size() - 1));
                vaidatePropertyDetails();
        
                if (hasErrors()) {
                    return "view";
                }
        
                // set the auto generated hearing number
                if (null == objection.getHearings().get(objection.getHearings().size() - 1).getHearingNumber()) {
                    String hearingNumber = applicationNumberGenerator.generate();
                    objection.getHearings().get(objection.getHearings().size() - 1).setHearingNumber(hearingNumber);
                    addActionMessage(getText("hearingNum") + hearingNumber);
                }
        
                // IF CURRENT STATUS IS GENERATE HEARING NOTICE, THEN CHANGE STATUS TO
                // HEARING COMPLETED.
                if (objection.getEgwStatus() != null
                        && objection.getEgwStatus().getCode()
                                .equalsIgnoreCase(PropertyTaxConstants.OBJECTION_RECORD_GENERATEHEARINGNOTICE)) {
                    Position position = null;
                    User user = null;
        
                    position = eisCommonService.getPositionByUserId(EgovThreadLocals.getUserId());
                    user = userService.getUserById(EgovThreadLocals.getUserId());
                    EgwStatus egwStatus =   egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
                            PropertyTaxConstants.OBJECTION_HEARING_COMPLETED) ;
                    if (egwStatus != null)
                        objection.setEgwStatus(egwStatus);
        
                    objection.transition(true).withNextAction(PropertyTaxConstants.OBJECTION_RECORD_INSPECTIONDETAILS)
                            .withStateValue(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED).withOwner(position)
                            .withOwner(user).withComments(workflowBean.getComments());
                }
                    
                  /*  if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED,
                                PropertyTaxConstants.OBJECTION_RECORD_INSPECTIONDETAILS);
                    } else if (objection.getHearings().get(objection.getHearings().size() - 1).getInspectionRequired()) {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED,
                                PropertyTaxConstants.OBJECTION_RECORD_INSPECTIONDETAILS);
                    } else {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED,
                                PropertyTaxConstants.OBJECTION_RECORD_OBJECTIONOUTCOME);
                    }*/
                modifyBasicProp();
                propertyImplService.merge(objection.getReferenceProperty()); 
                objectionService.update(objection);
                LOGGER.debug("ObjectionAction | recordHearingDetails | End "
                        + objection.getHearings().get(objection.getHearings().size() - 1));
                return STRUTS_RESULT_MESSAGE;
            
                }

	/**
	 * @description - allows the user to record the inspection details.
	 * @return String
	 */
	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition/revPetition-recordInspectionDetails")  
	public String recordInspectionDetails() {
                    LOGGER.debug("ObjectionAction | recordInspectionDetails | start "
                            + objection.getInspections().get(objection.getInspections().size() - 1));
                    vaidatePropertyDetails();
                    
                    if (hasErrors()) {
                        return "view";
                    }
                    if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED,
                                PropertyTaxConstants.OBJECTION_RECORD_INSPECTIONDETAILS);
                    } else {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_INSPECTION_COMPLETED,
                                PropertyTaxConstants.OBJECTION_RECORD_OBJECTIONOUTCOME);
                    }
                    LOGGER.debug("ObjectionAction | recordInspectionDetails | End "
                            + objection.getInspections().get(objection.getInspections().size() - 1));
                    modifyBasicProp(); 
                    propertyImplService.merge(objection.getReferenceProperty()); 
                    objectionService.update(objection);
                    return STRUTS_RESULT_MESSAGE;
	}

	/**
	 * @description - allows the user to record whether the objection is
	 *              accepted or rejected.
	 * @return String
	 */
	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition/revPetition-recordObjectionOutcome")  
	public String recordObjectionOutcome() {

		LOGGER.debug("ObjectionAction | recordObjectionOutcome | start " + objection);
	//	objection.getBasicProperty().setStatus(propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_CODE_ASSESSED));
		  vaidatePropertyDetails();
		  if (hasErrors()) {
                      return "view";
                  }
                if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workflowBean.getActionName())) {
                      //      updateStateAndStatus("END", "END");
                
                            if (objection.getObjectionRejected()) {
                                addActionMessage(getText("objection.rejected"));
                               /* objection.setEgwStatus(egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
                                        PropertyTaxConstants.OBJECTION_REJECTED));*/
                                updateStateAndStatus(PropertyTaxConstants.OBJECTION_REJECTED,
                                        PropertyTaxConstants.OBJECTION_GENERATE_ENDORSEMENT_NOTICE);
                            } else {
                
                                objection.setEgwStatus(egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
                                        PropertyTaxConstants.OBJECTION_ACCEPTED));
                                updateStateAndStatus(PropertyTaxConstants.OBJECTION_ACCEPTED,
                                        PropertyTaxConstants.OBJECTION_GENERATE_ENDORSEMENT_NOTICE);
                                // initiate the property modify if the action is approve and
                                // objection is accepted
                                //TODO: FIX THIS.
                              /*  propService.initiateModifyWfForObjection(objection.getBasicProperty().getId(),
                                        objection.getObjectionNumber(), objection.getRecievedOn(), objection.getCreatedBy(), null,
                                        PROPERTY_MODIFY_REASON_OBJ);
                                User approverUser = userService.getUserById(objection.getCreatedBy().getId());
                                addActionMessage(getText("initiate.modify.forward") + approverUser.getUsername());*/
        
                    }
                }
                modifyBasicProp();
                propertyImplService.merge(objection.getReferenceProperty()); 
                objectionService.update(objection);
                addActionMessage(getText("objection.outcome.success"));
                LOGGER.debug("ObjectionAction | recordObjectionOutcome | End " + objection);
                return STRUTS_RESULT_MESSAGE;
	}
	
	@ValidationErrorPage(value = "view")
        @Action(value = "/revPetition/revPetition-generateEnodresementNotice")  
    public String generateEnodresementNotice() {
            
	    /*
	     * Change basic property status from Objected to Assessed.
	     */
	    objection.getBasicProperty().setStatus(
                    propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_CODE_ASSESSED));
    
            Position position = null;
            User user = null;
    
            position = eisCommonService.getPositionByUserId(EgovThreadLocals.getUserId());
            user = userService.getUserById(EgovThreadLocals.getUserId());
            /*
             * End workflow 
             */
            objection.end().withStateValue(PropertyTaxConstants.WFLOW_ACTION_END).withOwner(position).withOwner(user)
                    .withComments(workflowBean.getComments());
    
            /*
             * Change workflow object as Active property and Active one to history.
             */      
            objection.getBasicProperty().getProperty().setStatus(STATUS_ISHISTORY);
            objection.getBasicProperty().setUnderWorkflow(Boolean.FALSE);
            objection.getReferenceProperty().setStatus(STATUS_ISACTIVE);
            objectionService.update(objection);
            addActionMessage(getText("objection.endoresementNotice.success"));
            LOGGER.debug("ObjectionAction | generateEnodresementNotice | End " + objection);
            return STRUTS_RESULT_MESSAGE;
    }
	@Action(value = "/revPetition/revPetition-view") 
            public String view() {
                LOGGER.debug("ObjectionAction | view | Start");
                objection = objectionService.findById(Long.valueOf(parameters.get("objectionId")[0]), false);
                getPropertyView(objection.getBasicProperty().getUpicNo());
                if (objection != null && objection.getReferenceProperty() != null) {
                    setReasonForModify(objection.getReferenceProperty().getPropertyDetail().getPropertyMutationMaster()
                            .getCode());
                }
        
                setOwnerName(objection.getBasicProperty().getProperty());
                setPropertyAddress(objection.getBasicProperty().getAddress());
        
                propStatVal = propertyStatusValuesDAO.getLatestPropertyStatusValuesByPropertyIdAndreferenceNo(objection
                        .getBasicProperty().getUpicNo(), objection.getObjectionNumber());
        
                setupWorkflowDetails();
                if (objection != null && objection.getState() != null)
                    setUpWorkFlowHistory(objection.getState().getId());
                setOwnerName(objection.getBasicProperty().getProperty());
                setPropertyAddress(objection.getBasicProperty().getAddress());
                LOGGER.debug("ObjectionAction | view | End");
                return "view";
            }

	public String viewObjectionDetails() {
		LOGGER.debug("ObjectionAction | viewObjectionDetails | Start");
		objection = objectionService.find("from Objection where objectionNumber like ?",
				objection.getObjectionNumber());
		setOwnerName(objection.getBasicProperty().getProperty());
		setPropertyAddress(objection.getBasicProperty().getAddress());
		LOGGER.debug("ObjectionAction | viewObjectionDetails | End");
		return "viewDetails";
	}

	public String updateRecordObjection() {

		objectionService.update(objection);
		if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
			updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED, WFLOW_ACTION_STEP_FORWARD);
			// FIX ME
			// objection.getState().setText1(PropertyTaxConstants.OBJECTION_RECORD_SAVED);
		} else {
			updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED,
					PropertyTaxConstants.OBJECTION_ADDHEARING_DATE);
		}

		return STRUTS_RESULT_MESSAGE;
	}

	private void updateStateAndStatus(String status, String actionToPerform) {
		LOGGER.debug("ObjectionAction | updateStateAndStatus | Start");
		 Position position =null;
                 User user =null;
                 
                 position= eisCommonService.getPositionByUserId(EgovThreadLocals.getUserId());
                 user= userService.getUserById(EgovThreadLocals.getUserId());
                    
                  //  objection.transition().start().withOwner(position).withOwner(user);
                 
                 
                if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
                    /*
                                if (workflowBean.getApproverUserId() != null) {
                                    position = eisCommonService.getPositionByUserId(workflowBean.getApproverUserId());
                                    user = userService.getUserById(workflowBean.getApproverUserId());
                                }
                        */     
                        if(!objection.hasState())
                            {
                                objection.start().withNextAction(actionToPerform).withStateValue(status).withOwner(position)
                                .withOwner(user).withComments(workflowBean.getComments());
                            }   else
                            {  
                               if(!actionToPerform.equals("END")){ 
                             /*      objection.transition(true).withNextAction(actionToPerform).withStateValue(status).withOwner(position)
                                   .withOwner(user).withComments(workflowBean.getComments());
                           */    }else if(actionToPerform.equals("END")){
                                   objection.end().withStateValue(status).withOwner(position)
                                   .withOwner(user).withComments(workflowBean.getComments());
                               }
                            }
                            addActionMessage(getText("file.save"));
        
                } else if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workflowBean.getActionName())) {
                    
                            if (workflowBean.getApproverUserId() != null) {
                                position = eisCommonService.getPositionByUserId(workflowBean.getApproverUserId());
                                user = userService.getUserById(workflowBean.getApproverUserId());
                            }
                            EgwStatus egwStatus = (EgwStatus) persistenceService.find("from EgwStatus where moduletype='"
                                    + PropertyTaxConstants.OBJECTION_MODULE + "' and code='" + status + "'");
                            
                            if(egwStatus!=null)
                                objection.setEgwStatus(egwStatus);  
                           
                                if(!objection.hasState())
                                {
                                    objection.start().withNextAction(actionToPerform).withStateValue(status).withOwner(position)
                                    .withOwner(user).withComments(workflowBean.getComments());
                                }   else
                                { 
                                objection.transition(true).withNextAction(actionToPerform).withStateValue(status).withOwner(position)
                                        .withOwner(user).withComments(workflowBean.getComments());
                                }
                            if (user != null)
                                addActionMessage(getText("objection.forward", new String[] { user.getUsername() }));
        
                }
		LOGGER.debug("ObjectionAction | updateStateAndStatus | End");
	}

        private void modifyBasicProp() {    
            propService.createProperty(objection.getReferenceProperty(), (objection.getReferenceProperty()
                    .getPropertyDetail().getSitalArea() != null
                    && objection.getReferenceProperty().getPropertyDetail().getSitalArea().getArea() != null ? objection
                    .getReferenceProperty().getPropertyDetail().getSitalArea().getArea().toString() : ""), reasonForModify,
                    (objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster() != null ? objection
                            .getReferenceProperty().getPropertyDetail().getPropertyTypeMaster().getId().toString() : null),
                    (objection.getReferenceProperty().getPropertyDetail().getPropertyUsage() != null ? objection
                            .getReferenceProperty().getPropertyDetail().getPropertyUsage().getId().toString() : null),
                    (objection.getReferenceProperty().getPropertyDetail().getPropertyOccupation() != null ? objection
                            .getReferenceProperty().getPropertyDetail().getPropertyOccupation().getId().toString() : null),
                    STATUS_WORKFLOW, objection.getReferenceProperty().getDocNumber(), "",
                    (objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster() != null 
                    && objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)
                    ? Boolean.FALSE : Boolean.TRUE),
                     objection
                            .getReferenceProperty().getPropertyDetail().getFloorType().getId(), objection
                            .getReferenceProperty().getPropertyDetail().getRoofType().getId(), objection
                            .getReferenceProperty().getPropertyDetail().getWallType().getId(), objection
                            .getReferenceProperty().getPropertyDetail().getWoodType().getId());
    
            PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    "from PropertyTypeMaster ptm where ptm.code = ?", PROPTYPE_OPEN_PLOT);
    
            if (objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster() != null
                    && objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster().getId() != null) {
                if ((propTypeMstr != null)
                        && (propTypeMstr.getId() == objection.getReferenceProperty().getPropertyDetail()
                                .getPropertyTypeMaster().getId())) {
    
                    if (objection.getReferenceProperty().getPropertyDetail().getFloorDetails() != null
                            && objection.getReferenceProperty().getPropertyDetail().getFloorDetails().size() > 0) {
                        // Mean currently floor details are present. Change to
                        // vacant property type.
                        propService.changePropertyDetail(objection.getReferenceProperty(), new VacantProperty(), 0);
                    }
                } else {
                    if (objection.getReferenceProperty().getPropertyDetail().getFloorDetails() != null
                            && objection.getReferenceProperty().getPropertyDetail().getFloorDetails().size() == 0) {
                        // Mean currently plot details are present. Change to build
                        // up property type.
    
                        propService.changePropertyDetail(objection.getReferenceProperty(), new BuiltUpProperty(), objection
                                .getReferenceProperty().getPropertyDetail().getFloorDetails().size());
    
                        propService.createAttributeValues(objection.getReferenceProperty(), null);
                    }
    
                }
    
            }
        }

	private void getPropertyView(String propertyId) {
		LOGGER.debug("ObjectionAction | getPropertyView | Start");
		viewPropertyAction.setPersistenceService(persistenceService);
		viewPropertyAction.setBasicPropertyDAO(basicPropertyDAO);
		viewPropertyAction.setPtDemandDAO(ptDemandDAO);
		viewPropertyAction.setPropertyId(propertyId);
		viewPropertyAction.setPropertyTaxUtil(new PropertyTaxUtil());
		viewPropertyAction.setUserService(userService); 
		viewPropertyAction.setSession(this.getSession());
		viewPropertyAction.viewForm();
		objection.setBasicProperty(viewPropertyAction.getBasicProperty());
		viewMap = viewPropertyAction.getViewMap();
		LOGGER.debug("ObjectionAction | getPropertyView | End");
	}

        public void vaidatePropertyDetails() {
            if (reasonForModify == null || reasonForModify.equals("-1")) {
                addActionError(getText("mandatory.rsnForMdfy"));
            }
            validateProperty(objection.getReferenceProperty(), (objection.getReferenceProperty().getPropertyDetail()
                    .getSitalArea() != null
                    && objection.getReferenceProperty().getPropertyDetail().getSitalArea().getArea() != null ? objection
                    .getReferenceProperty().getPropertyDetail().getSitalArea().getArea().toString() : ""), (objection
                    .getReferenceProperty().getPropertyDetail().getDateOfCompletion() != null ? sdf.format(objection
                    .getReferenceProperty().getPropertyDetail().getDateOfCompletion()).toString() : ""), isShowAckMessage,
                    "", "",
                    (objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster() != null ? objection
                            .getReferenceProperty().getPropertyDetail().getPropertyTypeMaster().getId().toString() : null),
                    ownerName, ownerName,(objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster() != null 
                            && objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)
                            ? Boolean.FALSE : Boolean.TRUE), Boolean.TRUE, objection.getReferenceProperty().getPropertyDetail()
                            .getFloorType().getId(), objection.getReferenceProperty().getPropertyDetail().getRoofType()
                            .getId(), objection.getReferenceProperty().getPropertyDetail().getWallType().getId(), objection
                            .getReferenceProperty().getPropertyDetail().getWoodType().getId()); 
    
        }

    public Objection getObjection() {
        return objection;
    }

    public void setObjection(Objection objection) {
        this.objection = objection;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public Map<String, Object> getViewMap() {
        return viewMap;
    }

    public void setObjectionService(PersistenceService<Objection, Long> objectionService) {
        this.objectionService = objectionService;
    }

    public PersistenceService<Property, Long> getPropertyImplService() {
        return propertyImplService;
    }

    public void setPropertyImplService(PersistenceService<Property, Long> propertyImplService) {
        this.propertyImplService = propertyImplService;
    }

    public void setViewPropertyAction(ViewPropertyAction viewPropertyAction) {
        this.viewPropertyAction = viewPropertyAction;
    }

    public void setObjectionWorkflowService(WorkflowService<Objection> objectionWorkflowService) {
        this.objectionWorkflowService = objectionWorkflowService;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(Property property) {
        if (property != null)
            this.ownerName = ptisCacheMgr.buildOwnerFullName(property.getBasicProperty().getPropertyOwnerInfo());
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(Address address) {
        if (address != null)
            this.propertyAddress = ptisCacheMgr.buildAddressByImplemetation(address);
    }

    public void setPropService(PropertyService propService) {
        this.propService = propService;
    }

    public boolean getIsShowAckMessage() {
        return isShowAckMessage;
    }

    public void setIsShowAckMessage(boolean isShowAckMessage) {
        this.isShowAckMessage = isShowAckMessage;
    }

    public void setBasicPropertyDAO(BasicPropertyDAO basicPropertyDAO) {
        this.basicPropertyDAO = basicPropertyDAO;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public PropertyStatusValues getPropStatVal() {
        return propStatVal;
    }

    public void setPropStatVal(PropertyStatusValues propStatVal) {
        this.propStatVal = propStatVal;
    }

    public String getReasonForModify() {
        return reasonForModify;
    }

    public void setReasonForModify(String reasonForModify) {
        this.reasonForModify = reasonForModify;
    }

    public TreeMap<Integer, String> getFloorNoMap() {
        return floorNoMap;
    }

    public void setFloorNoMap(TreeMap<Integer, String> floorNoMap) {
        this.floorNoMap = floorNoMap;
    }

    public String getPropTypeId() {
        return propTypeId;
    }

    public void setPropTypeId(String propTypeId) {
        this.propTypeId = propTypeId;
    }

}
