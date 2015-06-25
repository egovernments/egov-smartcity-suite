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
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

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
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.actions.view.ViewPropertyAction;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.entity.objection.Objection;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyStatus;
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

public class ObjectionAction extends PropertyTaxBaseAction {

	private static final long serialVersionUID = 1L;

	public static final String STRUTS_RESULT_MESSAGE = "message";
	
	private final Logger LOGGER = Logger.getLogger(ObjectionAction.class);
	private ViewPropertyAction viewPropertyAction = new ViewPropertyAction();
	private Objection objection = new Objection();
	private String propertyId;
	private Map<String, Object> viewMap;
	private PersistenceService<Objection, Long> objectionService;
	protected WorkflowService<Objection> objectionWorkflowService;
	private String ownerName;
	private String propertyAddress;
	private PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	private PropertyService propService;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
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
	
	public ObjectionAction() {
		addRelatedEntity("basicProperty", BasicPropertyImpl.class);
	}

	@Override
	public Object getModel() {

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
	}
	@SkipValidation
        @Action(value = "/objection/objection-newForm")
	public String newForm() {
		LOGGER.debug("Entered into newForm");

		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);

		if (basicProperty.getIsMigrated().equals(PropertyTaxConstants.STATUS_MIGRATED)
				&& basicProperty.getPropertySet().size() == 1) {
			isShowAckMessage = true;
			return STRUTS_RESULT_MESSAGE;

		}

		getPropertyView(propertyId);
		Map<String, String> wfMap = objection.getBasicProperty().getPropertyWfStatus();
		if (wfMap.get("WFSTATUS").equalsIgnoreCase("TRUE")) {
			addActionMessage(getText("property.state.objected", new String[] { objection
					.getBasicProperty().getUpicNo() }));
			return STRUTS_RESULT_MESSAGE;
		} else {
			setupWorkflowDetails();
		}

		return NEW;
	}
	@Action(value = "/objection/objection") 
                public String create() {
                    LOGGER.debug("ObjectionAction | Create | start " + objection);
                    setupWorkflowDetails();
                    // objection.setObjectionNumber(propertyTaxNumberGenerator.getObjectionNumber());
                    // //COMMENTED EARLIER NUMBER GENERATION LOGIC.
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

	@Action(value = "/objection/objection-addHearingDate")  
                public String addHearingDate() {
                    LOGGER.debug("ObjectionAction | addHearingDate | start " + objection);
            
                    if (objection.getHearings().get(objection.getHearings().size() - 1).getPlannedHearingDt()
                            .before(objection.getRecievedOn())) {
                        setupWorkflowDetails();
                        throw new ValidationException(Arrays.asList(new ValidationError("accountdetailkey",
                                getText("receivedon.greaterThan.plannedhearingdate"))));
            
                    }
                    if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED, PropertyTaxConstants.OBJECTION_ADDHEARING_DATE);
                        // FIX ME
                        // objection.getState().setText1(PropertyTaxConstants.OBJECTION_HEARINGDATE_SAVED);
            
                    } else {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_FIXED,
                                PropertyTaxConstants.OBJECTION_RECORD_HEARINGDETAILS);
                    }
                    objectionService.persist(objection);
                    LOGGER.debug("ObjectionAction | addHearingDate | End " + objection);
                    return STRUTS_RESULT_MESSAGE;
                }

	@ValidationErrorPage(value = "view")
	@Action(value = "/objection/objection-recordHearingDetails")  
          public String recordHearingDetails() {
            
                    LOGGER.debug("ObjectionAction | recordHearingDetails | start "
                            + objection.getHearings().get(objection.getHearings().size() - 1));
                    // set the auto generated hearing number
                    if (null == objection.getHearings().get(objection.getHearings().size() - 1).getHearingNumber()) {
                        /*
                         * String hearingNumber =
                         * propertyTaxNumberGenerator.getHearingNumber(objection
                         * .getBasicProperty().getBoundary().getParent());
                         */
                        String hearingNumber = applicationNumberGenerator.generate();
                        objection.getHearings().get(objection.getHearings().size() - 1).setHearingNumber(hearingNumber);
                        addActionMessage(getText("hearingNum") + hearingNumber);
                    }
            
                    if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_FIXED,
                                PropertyTaxConstants.OBJECTION_RECORD_HEARINGDETAILS);
                    } else if (objection.getHearings().get(objection.getHearings().size() - 1).getInspectionRequired()) {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED,
                                PropertyTaxConstants.OBJECTION_RECORD_INSPECTIONDETAILS);
                    } else {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED,
                                PropertyTaxConstants.OBJECTION_RECORD_OBJECTIONOUTCOME);
                    }
                    objectionService.persist(objection);
                    LOGGER.debug("ObjectionAction | recordHearingDetails | End "
                            + objection.getHearings().get(objection.getHearings().size() - 1));
                    return STRUTS_RESULT_MESSAGE;
            
                }

	/**
	 * @description - allows the user to record the inspection details.
	 * @return String
	 */
	@ValidationErrorPage(value = "view")
	@Action(value = "/objection/objection-recordInspectionDetails")  
	public String recordInspectionDetails() {
                    LOGGER.debug("ObjectionAction | recordInspectionDetails | start "
                            + objection.getInspections().get(objection.getInspections().size() - 1));
            
                    if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED,
                                PropertyTaxConstants.OBJECTION_RECORD_INSPECTIONDETAILS);
                    } else {
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_INSPECTION_COMPLETED,
                                PropertyTaxConstants.OBJECTION_RECORD_OBJECTIONOUTCOME);
                    }
                    LOGGER.debug("ObjectionAction | recordInspectionDetails | End "
                            + objection.getInspections().get(objection.getInspections().size() - 1));
                    objectionService.persist(objection);
                    return STRUTS_RESULT_MESSAGE;
	}

	/**
	 * @description - allows the user to record whether the objection is
	 *              accepted or rejected.
	 * @return String
	 */
	@ValidationErrorPage(value = "view")
	@Action(value = "/objection/objection-recordObjectionOutcome")  
	public String recordObjectionOutcome() {

		LOGGER.debug("ObjectionAction | recordObjectionOutcome | start " + objection);
		objection.getBasicProperty().setStatus(propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_CODE_ASSESSED));
				
		
        if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workflowBean.getActionName())) {
                    updateStateAndStatus("END", "END");
        
                    if (objection.getObjectionRejected()) {
                        addActionMessage(getText("objection.rejected"));
                        objection.setEgwStatus(egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
                                PropertyTaxConstants.OBJECTION_REJECTED));
        
                    } else {
        
                        objection.setEgwStatus(egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
                                PropertyTaxConstants.OBJECTION_ACCEPTED));
        
                        // initiate the property modify if the action is approve and
                        // objection is accepted
                        //TODO: FIX THIS.
                      /*  propService.initiateModifyWfForObjection(objection.getBasicProperty().getId(),
                                objection.getObjectionNumber(), objection.getRecievedOn(), objection.getCreatedBy(), null,
                                PROPERTY_MODIFY_REASON_OBJ);
                        User approverUser = userService.getUserById(objection.getCreatedBy().getId());
                        addActionMessage(getText("initiate.modify.forward") + approverUser.getUsername());*/

            } 
        } else {
			if (objection.getHearings().get(objection.getHearings().size() - 1)
					.getInspectionRequired()) {
				updateStateAndStatus(PropertyTaxConstants.OBJECTION_INSPECTION_COMPLETED,
						PropertyTaxConstants.OBJECTION_RECORD_OBJECTIONOUTCOME);
			} else {
				updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED,
						PropertyTaxConstants.OBJECTION_RECORD_OBJECTIONOUTCOME);
			}
		}
		objectionService.update(objection);
		addActionMessage(getText("objection.outcome.success"));
		LOGGER.debug("ObjectionAction | recordObjectionOutcome | End " + objection);
		return STRUTS_RESULT_MESSAGE;
	}
	@Action(value = "/objection/objection-view") 
	public String view() {
		LOGGER.debug("ObjectionAction | view | Start");
		objection = objectionService
				.findById(Long.valueOf(parameters.get("objectionId")[0]), false);
		getPropertyView(objection.getBasicProperty().getUpicNo());
		setupWorkflowDetails();
		if(objection!=null && objection.getState()!=null)
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
                               if(!actionToPerform.equals("END")) 
                                objection.transition(true).withNextAction(actionToPerform).withStateValue(status).withOwner(position)
                                    .withOwner(user).withComments(workflowBean.getComments());
                               else
                                   objection.end().withStateValue(status).withOwner(position)
                                   .withOwner(user).withComments(workflowBean.getComments());
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

	private void getPropertyView(String propertyId) {
		LOGGER.debug("ObjectionAction | getPropertyView | Start");
		viewPropertyAction.setPersistenceService(persistenceService);
		viewPropertyAction.setBasicPropertyDAO(basicPropertyDAO);
		viewPropertyAction.setPtDemandDAO(ptDemandDAO);
		viewPropertyAction.setPropertyId(propertyId);
		viewPropertyAction.setPropertyTaxUtil(new PropertyTaxUtil());
		viewPropertyAction.setSession(this.getSession());
		viewPropertyAction.viewForm();
		objection.setBasicProperty(viewPropertyAction.getBasicProperty());
		viewMap = viewPropertyAction.getViewMap();
		LOGGER.debug("ObjectionAction | getPropertyView | End");
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

		this.ownerName = ptisCacheMgr.buildOwnerFullName(property.getPropertyOwnerSet());
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(Address address) {

		this.propertyAddress = ptisCacheMgr.buildAddressByImplemetation(address);
	}

	public void setPropService(PropertyService propService) {
		this.propService = propService;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
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
	
}
