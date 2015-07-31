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

import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEVIATION_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.HEARING_TIMINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkFlowMatrix;
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
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyID;
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
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.revisionPetition.RevisionPetitionService;
import org.egov.ptis.notice.PtNotice;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Results({ @Result(name = "new", location = "objection-new.jsp"),
		@Result(name = "message", location = "objection-message.jsp"),
		@Result(name = "notice", location = "objection-notice.jsp"),

		@Result(name = "view", location = "objection-view.jsp"), @Result(name = "ack", location = "objection-ack.jsp") })
public class RevisionPetitionAction extends PropertyTaxBaseAction {

	private static final long serialVersionUID = 1L;

	public static final String STRUTS_RESULT_MESSAGE = "message";
	public static final String NOTICE = "notice";
	private final Logger LOGGER = Logger.getLogger(RevisionPetitionAction.class);
	private ViewPropertyAction viewPropertyAction = new ViewPropertyAction();
	private Objection objection = new Objection();
	private String propertyId;
	private Map<String, Object> viewMap;
	private RevisionPetitionService revisionPetitionService; 
	protected WorkflowService<Objection> objectionWorkflowService;
	private String ownerName;
	private String propertyAddress;
	private PersistenceService<Property, Long> propertyImplService;
	private String propTypeId;
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String[] floorNoStr = new String[100];
	
	private PropertyService propService;
	private PropertyStatusValues propStatVal;
	private String reasonForModify;
	private TreeMap<Integer, String> floorNoMap;
	private Map<String, String> deviationPercentageMap;
	private TreeMap<String, String> hearingTimingMap;
	
	private List<DocumentType> documentTypes = new ArrayList<>();
	  private String northBoundary;
	    private String southBoundary;
	    private String eastBoundary;
	    private String westBoundary;
        private Map<String, String> propTypeCategoryMap;
	private Integer reportId = -1;
	@Autowired
	private PropertyStatusValuesDAO propertyStatusValuesDAO;
	@Autowired
	private ReportService reportService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;

	@Autowired
	protected SimpleWorkflowService<Objection> revisionPetitionWorkFlowService;
	
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

		/*
		this.addRelatedEntity("referenceProperty.propertyDetail.floorDetails.unitType", PropertyTypeMaster.class);
		this.addRelatedEntity("referenceProperty.propertyDetail.floorDetails.propertyUsage", PropertyUsage.class);
		this.addRelatedEntity("referenceProperty.propertyDetail.floorDetails.propertyOccupation",
				PropertyOccupation.class);
		this.addRelatedEntity("referenceProperty.propertyDetail.floorDetails.structureClassification",
				StructureClassification.class);
*/		this.addRelatedEntity("structureClassification", StructureClassification.class);
                this.addRelatedEntity("referenceProperty.propertyDetail.apartment", Apartment.class);
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
			objection = revisionPetitionService.findById(objection.getId(), false);

		}
		super.prepare();
		setUserInfo();
		//setupWorkflowDetails();
		documentTypes = propService.getPropertyModificationDocumentTypes();
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
		     final List<String> apartmentsList = getPersistenceService().findAllBy(
		                "from Apartment order by name");
		        final List<String> taxExemptionReasonList = getPersistenceService().findAllBy(
		                "from TaxExeptionReason order by name");
		         
		addDropdownData("wallType", wallTypes);
		addDropdownData("woodType", woodTypes);
		addDropdownData("PropTypeMaster", propTypeList);
		addDropdownData("OccupancyList", propOccList);
		addDropdownData("UsageList", usageList);
		addDropdownData("MutationList", propMutList);
		addDropdownData("StructureList", StructureList);
		addDropdownData("AgeFactorList", ageFacList);
                addDropdownData("apartments", apartmentsList);
                addDropdownData("taxExemptionReasonList", taxExemptionReasonList);
                
                populatePropertyTypeCategory();
                setDeviationPercentageMap(DEVIATION_PERCENTAGE);
                setHearingTimingMap(HEARING_TIMINGS);
               
	}

	@SkipValidation
	@Action(value = "/revPetition/revPetition-newForm")
	public String newForm() {
		LOGGER.debug("Entered into newForm");
		getPropertyView(propertyId);
		// Map<String, String> wfMap =
		// objection.getBasicProperty().getPropertyWfStatus();
		if (objection != null && objection.getBasicProperty() != null && objection.getBasicProperty().isUnderWorkflow()) {
			addActionMessage(getText("property.state.objected",
					new String[] { objection.getBasicProperty().getUpicNo() }));
			return STRUTS_RESULT_MESSAGE;
		} /*else {
			setupWorkflowDetails();
		}*/

		return NEW;
	}

	@Action(value = "/revPetition/revPetition")
	public String create() {
		LOGGER.debug("ObjectionAction | Create | start " + objection);
		//setupWorkflowDetails();

		objection.setObjectionNumber(applicationNumberGenerator.generate());

		objection.getBasicProperty().setStatus(
				propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_OBJECTED_STR));
		EgwStatus egwStatus = egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
				PropertyTaxConstants.OBJECTION_CREATED);
		objection.setEgwStatus(egwStatus);
		
		if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction)) {
			updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED, WFLOW_ACTION_STEP_FORWARD);
			// FIX ME
			// objection.getState().setText1(PropertyTaxConstants.OBJECTION_RECORD_SAVED);
		} else {
			updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED, PropertyTaxConstants.OBJECTION_ADDHEARING_DATE);
		}

		addActionMessage(getText("objection.success") + objection.getObjectionNumber());
		revisionPetitionService.applyAuditing(objection.getState());
		revisionPetitionService.createRevisionPetition(objection);
		//objectionService.persist(objection);
		LOGGER.debug("ObjectionAction | Create | End " + objection);
		return STRUTS_RESULT_MESSAGE;
	}

	@Action(value = "/revPetition/revPetition-addHearingDate")
        public String addHearingDate() {
    
            LOGGER.debug("ObjectionAction | addHearingDate | start " + objection);
            InputStream hearingNoticePdf = null;
            ReportOutput reportOutput = new ReportOutput();
            /*
             * vaidatePropertyDetails(); if (hasErrors()) { return "view"; }
             */
            if (objection.getHearings().get(objection.getHearings().size() - 1).getPlannedHearingDt()
                    .before(objection.getRecievedOn())) {
                //setupWorkflowDetails();
               /* throw new ValidationException(Arrays.asList(new ValidationError("accountdetailkey",
                        getText("receivedon.greaterThan.plannedhearingdate"))));
            */    addActionMessage(getText("receivedon.greaterThan.plannedhearingdate"));
            }else {
            // modifyBasicProp();
            if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction)) {
                updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED, PropertyTaxConstants.OBJECTION_ADDHEARING_DATE);
                // FIX ME
                // objection.getState().setText1(PropertyTaxConstants.OBJECTION_HEARINGDATE_SAVED);
    
            } else {
                updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_FIXED,
                        PropertyTaxConstants.OBJECTION_RECORD_GENERATEHEARINGNOTICE);
            }
            // propertyImplService.update(objection.getReferenceProperty());
    
            reportOutput = createHearingNoticeReport(reportOutput, objection);
    
            if (reportOutput != null && reportOutput.getReportOutputData() != null) {
                hearingNoticePdf = new ByteArrayInputStream(reportOutput.getReportOutputData());
            }
            // reportId = ReportViewerUtil.addReportToSession(reportOutput,
            // getSession());
            noticeService.saveNotice(objection.getObjectionNumber(),
                    PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE, objection.getBasicProperty(),
                    hearingNoticePdf);// Save Notice
            //objectionService.update(objection);
            revisionPetitionService.updateRevisionPetition(objection);
            LOGGER.debug("ObjectionAction | addHearingDate | End " + objection); 
            }
            return STRUTS_RESULT_MESSAGE;
        }

        private ReportOutput createHearingNoticeReport(ReportOutput reportOutput, Objection objection) {
            reportOutput.setReportFormat(FileFormat.PDF);
            Map reportParams = new HashMap<String, Object>();
    
            ReportRequest reportRequest = null;
    
            if (objection != null) {
                reportParams.put("logoPath", propertyTaxUtil.logoBasePath());
                reportParams.put("cityName", ReportUtil.getCityName());
                reportParams.put("recievedBy", objection.getRecievedBy());
    
                if (objection.getHearings() != null && objection.getHearings().size() > 0
                        && objection.getHearings().get(objection.getHearings().size() - 1).getPlannedHearingDt() != null)
                    reportParams.put(
                            "hearingNoticeDate",
                            sdf.format(objection.getHearings().get(objection.getHearings().size() - 1)
                                    .getPlannedHearingDt()));
                else
                    reportParams.put("hearingNoticeDate", "");
                reportParams.put("currentDate", sdf.format(new Date()));
                reportParams.put("receivedOn", sdf.format(objection.getRecievedOn()));
                reportParams.put("docNumberObjection", objection.getObjectionNumber());
                reportParams.put("HouseNo", objection.getBasicProperty().getApplicationNo());
                reportParams.put("hearingTime", objection.getHearings().get(objection.getHearings().size() - 1)
                        .getHearingTime());
    
                reportRequest = new ReportRequest(PropertyTaxConstants.REPORT_TEMPLATENAME_REVISIONPETITION_HEARINGNOTICE,
                        objection, reportParams);
                reportOutput = reportService.createReport(reportRequest);
            }
            return reportOutput;
        }

	@ValidationErrorPage(value = "view")
	@Action(value = "/revPetition/revPetition-generateHearingNotice")
	public String generateHearingNotice() {

		if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction)) {
			updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_FIXED,
					PropertyTaxConstants.OBJECTION_RECORD_GENERATEHEARINGNOTICE);
		} else {
			updateStateAndStatus(PropertyTaxConstants.OBJECTION_RECORD_GENERATEHEARINGNOTICE,
					PropertyTaxConstants.OBJECTION_RECORD_HEARINGDETAILS);
		}

		PropertyImpl refNewProperty = propService.creteNewPropertyForObjectionWorkflow(objection.getBasicProperty(),
				objection.getObjectionNumber(), objection.getRecievedOn(), objection.getCreatedBy(), null,
				PROPERTY_MODIFY_REASON_OBJ);

		/*
		 * PropertyImpl newProperty = (PropertyImpl)
		 * propService.createDemand(refNewProperty, oldProperty,
		 * propCompletionDate, isfloorDetailsRequired);
		 */
		// propertyImplService.persist(refNewProperty);
		objection.getBasicProperty().setUnderWorkflow(Boolean.TRUE);
		propertyImplService.getSession().flush();
		objection.setReferenceProperty(refNewProperty);
		//objectionService.update(objection);
		revisionPetitionService.updateRevisionPetition(objection);
		// return STRUTS_RESULT_MESSAGE;
	return STRUTS_RESULT_MESSAGE;

	}

	
	  @ValidationErrorPage(value = "view")
    @Action(value = "/revPetition/revPetition-printHearingNotice")
    public String printHearingNotice() {

	 objection = revisionPetitionService.findById(Long.valueOf(parameters.get("objectionId")[0]), false);
	  
        ReportOutput reportOutput = new ReportOutput();
        if (objection != null && objection.getObjectionNumber() != null) {
            PtNotice ptNotice = noticeService.getPtNoticeByNoticeNumberAndNoticeType(objection.getObjectionNumber(),
                    PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE);

            FileStoreMapper fsm = ptNotice.getFileStore();
            File file = fileStoreService.fetch(fsm, PTMODULENAME);
            byte[] bFile;
            try {
                bFile = FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                e.printStackTrace();
                throw new EGOVRuntimeException("Exception while generating Hearing Notcie : " + e);
            }
            reportOutput.setReportOutputData(bFile);
            reportOutput.setReportFormat(FileFormat.PDF);
            reportId = ReportViewerUtil.addReportToSession(reportOutput, getSession());
        } else {
             addActionMessage(getText("objection.nohearingNotice"));
        }
        return NOTICE;
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
			addActionMessage(getText("hearingNum") + " "+ hearingNumber);
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
			EgwStatus egwStatus = egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
					PropertyTaxConstants.OBJECTION_HEARING_COMPLETED);
			if (egwStatus != null)
				objection.setEgwStatus(egwStatus);

			objection.transition(true).withNextAction(PropertyTaxConstants.OBJECTION_RECORD_INSPECTIONDETAILS)
					.withStateValue(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED).withOwner(position)
					.withOwner(user).withComments(approverComments);
		}

		modifyBasicProp();
		propertyImplService.merge(objection.getReferenceProperty());
		//objectionService.update(objection);
		  revisionPetitionService.updateRevisionPetition(objection);   
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
		if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction)) {
			updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED,
					PropertyTaxConstants.OBJECTION_RECORD_INSPECTIONDETAILS);
		} else {
			updateStateAndStatus(PropertyTaxConstants.OBJECTION_INSPECTION_COMPLETED,
					PropertyTaxConstants.OBJECTION_INSPECTION_VERIFY);
		}
		LOGGER.debug("ObjectionAction | recordInspectionDetails | End "
				+ objection.getInspections().get(objection.getInspections().size() - 1));
		modifyBasicProp();
		propertyImplService.merge(objection.getReferenceProperty());
		//objectionService.update(objection);
		  revisionPetitionService.updateRevisionPetition(objection);   
		return STRUTS_RESULT_MESSAGE;
	}
	@ValidationErrorPage(value = "view")
        @Action(value = "/revPetition/revPetition-validateInspectionDetails")
        public String validateInspectionDetails() {
                LOGGER.debug("ObjectionAction | recordInspectionDetails | start "
                                + objection.getInspections().get(objection.getInspections().size() - 1));
               
              
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_INSPECTION_VERIFY,
                                        PropertyTaxConstants.OBJECTION_RECORD_OBJECTIONOUTCOME);
               
                LOGGER.debug("ObjectionAction | recordInspectionDetails | End "
                                + objection.getInspections().get(objection.getInspections().size() - 1));
                revisionPetitionService.updateRevisionPetition(objection);   
                return STRUTS_RESULT_MESSAGE;
        }
	@ValidationErrorPage(value = "view")
        @Action(value = "/revPetition/revPetition-rejectInspectionDetails")
        public String rejectInspectionDetails() {
                LOGGER.debug("ObjectionAction | recordInspectionDetails | start "
                                + objection.getInspections().get(objection.getInspections().size() - 1));
               
              
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED,
                                        PropertyTaxConstants.OBJECTION_RECORD_INSPECTIONDETAILS);
               
                LOGGER.debug("ObjectionAction | recordInspectionDetails | End "
                                + objection.getInspections().get(objection.getInspections().size() - 1));
                revisionPetitionService.updateRevisionPetition(objection);   
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
                InputStream endoresementPdf = null;
                ReportOutput reportOutput = new ReportOutput();
                LOGGER.debug("ObjectionAction | recordObjectionOutcome | start " + objection);
                // objection.getBasicProperty().setStatus(propertyStatusDAO.getPropertyStatusByCode(PropertyTaxConstants.STATUS_CODE_ASSESSED));
               // vaidatePropertyDetails();
                if (hasErrors()) {
                    return "view";
                }
                if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)) {
                    // updateStateAndStatus("END", "END");
        
                    if (objection.getObjectionRejected()) {
                        addActionMessage(getText("objection.rejected"));
                        /*
                         * objection.setEgwStatus(egwStatusDAO.getStatusByModuleAndCode(
                         * PropertyTaxConstants.OBJECTION_MODULE,
                         * PropertyTaxConstants.OBJECTION_REJECTED));
                         */
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_REJECTED,
                                PropertyTaxConstants.OBJECTION_GENERATE_ENDORSEMENT_NOTICE);
                    } else {
        
                        objection.setEgwStatus(egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
                                PropertyTaxConstants.OBJECTION_ACCEPTED));
                        updateStateAndStatus(PropertyTaxConstants.OBJECTION_ACCEPTED,
                                PropertyTaxConstants.OBJECTION_GENERATE_ENDORSEMENT_NOTICE);
                        // initiate the property modify if the action is approve and
                        // objection is accepted
                        // TODO: FIX THIS.
                        /*
                         * propService.initiateModifyWfForObjection(objection.
                         * getBasicProperty().getId(), objection.getObjectionNumber(),
                         * objection.getRecievedOn(), objection.getCreatedBy(), null,
                         * PROPERTY_MODIFY_REASON_OBJ); User approverUser =
                         * userService.getUserById(objection.getCreatedBy().getId());
                         * addActionMessage(getText("initiate.modify.forward") +
                         * approverUser.getUsername());
                         */
        
                    }
                }
                //modifyBasicProp();
        
                reportOutput = createEndoresement(reportOutput, objection);
                if (reportOutput != null && reportOutput.getReportOutputData() != null) {
                    endoresementPdf = new ByteArrayInputStream(reportOutput.getReportOutputData());
                }
                noticeService.saveNotice(
                        objection.getObjectionNumber().concat(
                                PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT_PREFIX),
                        PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT, objection.getBasicProperty(),
                        endoresementPdf);
        
               // propertyImplService.merge(objection.getReferenceProperty());
               // objectionService.update(objection);
                revisionPetitionService.updateRevisionPetition(objection);   
                addActionMessage(getText("objection.outcome.success"));
                LOGGER.debug("ObjectionAction | recordObjectionOutcome | End " + objection);
                return STRUTS_RESULT_MESSAGE;
            }

            private ReportOutput createEndoresement(ReportOutput reportOutput, Objection objection) {
        
                reportOutput.setReportFormat(FileFormat.PDF);
                Map reportParams = new HashMap<String, Object>();
        
                ReportRequest reportRequest = null;
                if (objection != null) {
                    Map<String, BigDecimal> currentDemand = ptDemandDAO.getDemandCollMap(objection.getReferenceProperty());
                    Map<String, BigDecimal> earlierDemand = ptDemandDAO.getDemandCollMap(objection.getBasicProperty()
                            .getProperty());
        
                    reportParams.put("logoPath", propertyTaxUtil.logoBasePath());
                    reportParams.put("cityName", ReportUtil.getCityName());
                    reportParams.put("recievedBy", objection.getRecievedBy());
                    reportParams.put("docNumberObjection", objection.getObjectionNumber());
        
                    reportParams.put("currentDate", sdf.format(new Date()));
                    reportParams.put("receivedOn", sdf.format(objection.getRecievedOn()));
                    reportParams.put("HouseNo", objection.getBasicProperty().getApplicationNo());
                    reportParams.put("wardNumber", objection.getBasicProperty().getBoundary());
                    reportParams.put("AnnualPropertyTaxTo",
                            (currentDemand.get(CURR_DMD_STR).divide(BigDecimal.valueOf(2)).setScale(2)));
                    reportParams.put("AnnualPropertyTaxFrom",
                            (earlierDemand.get(CURR_DMD_STR).divide(BigDecimal.valueOf(2)).setScale(2)));
                    reportParams.put("HalfYearPropertyTaxTo", currentDemand.get(CURR_DMD_STR).setScale(2).toString());
                    reportParams.put("HalfYearPropertyTaxFrom", earlierDemand.get(CURR_DMD_STR).setScale(2).toString());
        
                    reportRequest = new ReportRequest(PropertyTaxConstants.REPORT_TEMPLATENAME_REVISIONPETITION_ENDORSEMENT,
                            objection, reportParams);
                    reportOutput = reportService.createReport(reportRequest);
                }
                return reportOutput;
        
            }

    @Action(value = "/revPetition/revPetition-printEnodresementNotice")
        public String printEnodresementNotice() {
    
            objection = revisionPetitionService.findById(Long.valueOf(parameters.get("objectionId")[0]), false);
            
            ReportOutput reportOutput = new ReportOutput();
            if (objection != null && objection.getObjectionNumber() != null) {
                PtNotice ptNotice = noticeService.getPtNoticeByNoticeNumberAndNoticeType(objection.getObjectionNumber()
                        .concat(PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT_PREFIX),
                        PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT);
                if (ptNotice != null) {
                    FileStoreMapper fsm = ptNotice.getFileStore();
                    File file = fileStoreService.fetch(fsm, PTMODULENAME);
                    byte[] bFile;
                    try {
                        bFile = FileUtils.readFileToByteArray(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new EGOVRuntimeException("Exception while generating Hearing Notcie : " + e);
                    }
                    reportOutput.setReportOutputData(bFile);
                    reportOutput.setReportFormat(FileFormat.PDF);
                    reportId = ReportViewerUtil.addReportToSession(reportOutput, getSession());
                }
            } else {
                addActionMessage(getText("objection.noendoresementNotice"));
            }
    
            return NOTICE;
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
                            .withComments(approverComments);
            
                    /*
                     * Change workflow object as Active property and Active one to history.
                     */
                    objection.getBasicProperty().getProperty().setStatus(STATUS_ISHISTORY);
                    objection.getBasicProperty().setUnderWorkflow(Boolean.FALSE);
                    objection.getReferenceProperty().setStatus(STATUS_ISACTIVE);
                   // objectionService.update(objection);
                    revisionPetitionService.updateRevisionPetition(objection);   
                    
                    addActionMessage(getText("objection.endoresementNotice.success"));
                    LOGGER.debug("ObjectionAction | generateEnodresementNotice | End " + objection);
                    /* return STRUTS_RESULT_MESSAGE; */
                    ReportOutput reportOutput = new ReportOutput();
                    if (objection != null && objection.getObjectionNumber() != null) {
                        PtNotice ptNotice = noticeService.getPtNoticeByNoticeNumberAndNoticeType(objection.getObjectionNumber()
                                .concat(PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT_PREFIX),
                                PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_ENDORSEMENT);
                        if (ptNotice != null) {
                            FileStoreMapper fsm = ptNotice.getFileStore();
                            File file = fileStoreService.fetch(fsm, PTMODULENAME);
                            byte[] bFile;
                            try {
                                bFile = FileUtils.readFileToByteArray(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new EGOVRuntimeException("Exception while generating Hearing Notcie : " + e);
                            }
                            reportOutput.setReportOutputData(bFile);
                            reportOutput.setReportFormat(FileFormat.PDF);
                            reportId = ReportViewerUtil.addReportToSession(reportOutput, getSession());
                        }
                    }
                    return NOTICE;
                }
	 private void setFloorDetails(final Property property) {
	        LOGGER.debug("Entered into setFloorDetails, Property: " + property);

	        final List<Floor> floors = property.getPropertyDetail().getFloorDetails();
	        property.getPropertyDetail().setFloorDetailsProxy(floors);
	        
	        int i = 0;
	        for (final Floor flr : floors) {
	            floorNoStr[i] = propertyTaxUtil.getFloorStr(flr.getFloorNo());
	            i++;
	        }

	        LOGGER.debug("Exiting from setFloorDetails: ");
	    }                

	@Action(value = "/revPetition/revPetition-view")
                public String view() {
                    LOGGER.debug("ObjectionAction | view | Start");
                    objection = revisionPetitionService.findById(Long.valueOf(parameters.get("objectionId")[0]), false);
                    getPropertyView(objection.getBasicProperty().getUpicNo());
                    
                    if (objection!=null && objection.getBasicProperty()!=null &&  objection.getBasicProperty().getPropertyID() != null) {
                        final PropertyID propertyID = objection.getBasicProperty().getPropertyID();
                        northBoundary = propertyID.getNorthBoundary();
                        southBoundary = propertyID.getSouthBoundary();
                        eastBoundary = propertyID.getEastBoundary();
                        westBoundary = propertyID.getWestBoundary();
                    }
                    populatePropertyTypeCategory();
                    
                    if (objection != null && objection.getReferenceProperty() != null) {
                        setReasonForModify(objection.getReferenceProperty().getPropertyDetail().getPropertyMutationMaster()
                                .getCode());
                        if (objection.getReferenceProperty().getPropertyDetail().getFloorDetails().size() > 0)
                            setFloorDetails(objection.getReferenceProperty());
                    }
            
                    
                    
                    setOwnerName(objection.getBasicProperty().getProperty());
                    setPropertyAddress(objection.getBasicProperty().getAddress());
            
                    propStatVal = propertyStatusValuesDAO.getLatestPropertyStatusValuesByPropertyIdAndreferenceNo(objection
                            .getBasicProperty().getUpicNo(), objection.getObjectionNumber());
            
                    //setupWorkflowDetails();
                    if (objection != null && objection.getState() != null)
                        setUpWorkFlowHistory(objection.getState().getId());
                    setOwnerName(objection.getBasicProperty().getProperty());
                    setPropertyAddress(objection.getBasicProperty().getAddress());
                    LOGGER.debug("ObjectionAction | view | End");
                    return "view";
                }

	public String viewObjectionDetails() {
		LOGGER.debug("ObjectionAction | viewObjectionDetails | Start");
		objection = revisionPetitionService
				.find("from Objection where objectionNumber like ?", objection.getObjectionNumber());
		setOwnerName(objection.getBasicProperty().getProperty());
		setPropertyAddress(objection.getBasicProperty().getAddress());
		LOGGER.debug("ObjectionAction | viewObjectionDetails | End");
		return "viewDetails";
	}

	public String updateRecordObjection() {

	//	objectionService.update(objection);
		revisionPetitionService.updateRevisionPetition(objection);   
	         
		if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction)) {
			updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED, WFLOW_ACTION_STEP_FORWARD);
			// FIX ME
			// objection.getState().setText1(PropertyTaxConstants.OBJECTION_RECORD_SAVED);
		} else {
			updateStateAndStatus(PropertyTaxConstants.OBJECTION_CREATED, PropertyTaxConstants.OBJECTION_ADDHEARING_DATE);
		}

		return STRUTS_RESULT_MESSAGE;
	}

	private void updateStateAndStatus(String status, String actionToPerform) {
		LOGGER.debug("ObjectionAction | updateStateAndStatus | Start");
		Position position = null;Position inspectionUserPosition = null;
		User user = null,loggedInUser=null;

		position = eisCommonService.getPositionByUserId(EgovThreadLocals.getUserId());
		user = userService.getUserById(EgovThreadLocals.getUserId());
		loggedInUser=user;
	
             if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {	
                 LOGGER.info("REJECTION CASE TO BE HANDLE."); 
                 
                 List <StateHistory>  stateHistoryList= objection.getState().getHistory();
                 for(StateHistory stateHistoryObj:stateHistoryList)
                 {
                     if(stateHistoryObj.getValue().equalsIgnoreCase("HEARING COMPLETED"))
                     {
                         inspectionUserPosition= stateHistoryObj.getOwnerPosition();
                     }
                 }
                 WorkFlowMatrix  wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
                         getAdditionalRule(), PropertyTaxConstants.WF_STATE_REJECTED, null);
                 objection.setEgwStatus(egwStatusDAO.getStatusByModuleAndCode(PropertyTaxConstants.OBJECTION_MODULE,
                         PropertyTaxConstants.OBJECTION_HEARING_COMPLETED));
                 
                 if(inspectionUserPosition!=null){
                 objection.transition(true).withNextAction(wfmatrix.getNextAction()).withStateValue(PropertyTaxConstants.WF_STATE_REJECTED).withOwner(inspectionUserPosition).withSenderName(loggedInUser.getName())
                 .withDateInfo(new DateTime().toDate()).withComments(approverComments);
                 Assignment assignment=assignmentService.getPrimaryAssignmentForPositionAndDate(inspectionUserPosition.getId(), new Date());
                 if(assignment!=null)
                     addActionMessage(getText("objection.forward", new String[] { assignment.getEmployee().getUsername() }));
                 }
              
             }
             else	
             {
                        WorkFlowMatrix wfmatrix = null;
                        if (null!=objection.getState()) {
                            wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
                                    getAdditionalRule(), objection.getCurrentState().getValue(), null);
                        } else {
                            wfmatrix = revisionPetitionWorkFlowService.getWfMatrix(objection.getStateType(), null, null,
                                    getAdditionalRule(), null, null);
                        }
            
            
                 
            		if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(workFlowAction)) {
            
                            if (null==objection.getState()) {
                                objection.start().withNextAction(actionToPerform).withStateValue(wfmatrix.getNextState())
                                        .withOwner(position).withSenderName(loggedInUser.getName())
                                        .withNextAction(wfmatrix.getNextAction()).withOwner(user).withComments(approverComments);
                            } else {
            
                                if (actionToPerform.equals("END")) {
                                    objection.end().withStateValue(wfmatrix.getNextState()).withOwner(position)
                                            .withSenderName(loggedInUser.getName()).withNextAction(wfmatrix.getNextAction())
                                            .withDateInfo(new DateTime().toDate()).withComments(approverComments);
                                }
                            }
                            addActionMessage(getText("file.save"));
            
            		} else { //if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(workFlowAction)) {
            
            		    
            		  if (null != approverPositionId && approverPositionId != -1) {
            				//position = eisCommonService.getPositionByUserId(approverPositionId);
            				position = (Position) persistenceService.find("from Position where id=?",
            		                        approverPositionId);
            				//user = userService.getUserById(approverPositionId);
            			}
            			EgwStatus egwStatus = (EgwStatus) persistenceService.find("from EgwStatus where moduletype='"
            					+ PropertyTaxConstants.OBJECTION_MODULE + "' and code='" + status + "'");
            
            			if (egwStatus != null)
            				objection.setEgwStatus(egwStatus);
            
            			if (null==objection.getState()) {
            				objection.start().withNextAction(actionToPerform).withStateValue(wfmatrix.getNextState()).withOwner(position).withSenderName(loggedInUser.getName())
            						 .withNextAction(wfmatrix.getNextAction()).withComments(approverComments);
            			} else {
            				objection.transition(true).withNextAction(actionToPerform).withStateValue(wfmatrix.getNextState()).withOwner(position).withSenderName(loggedInUser.getName())
            						.withDateInfo(new DateTime().toDate()) .withNextAction(wfmatrix.getNextAction()).withComments(approverComments);
            			}
            		
            			  if (approverName != null && !approverName.isEmpty()
            		                && !approverName.equalsIgnoreCase("----Choose----")) {
            			  addActionMessage(getText("objection.forward", new String[] { approverName }));
            			  }
            			  else if (user != null)
            				addActionMessage(getText("objection.forward", new String[] { user.getUsername() }));
            
            		}  
            		LOGGER.debug("ObjectionAction | updateStateAndStatus | End");
            	}
             }
	private void modifyBasicProp() {
	    Date propCompletionDate = null;
	    PropertyImpl createdNewProperty =  propService.createProperty(objection.getReferenceProperty(), (objection.getReferenceProperty()
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
				(objection.getReferenceProperty().getPropertyDetail().getFloorType() != null ? objection
                                        .getReferenceProperty().getPropertyDetail().getFloorType().getId() : null),
                                        (objection.getReferenceProperty().getPropertyDetail().getRoofType() != null ? objection
                                                .getReferenceProperty().getPropertyDetail().getRoofType().getId() : null),
                                                (objection.getReferenceProperty().getPropertyDetail().getWallType() != null ? objection
                                                        .getReferenceProperty().getPropertyDetail().getWallType().getId() : null),
                                                        (objection.getReferenceProperty().getPropertyDetail().getWoodType() != null ? objection
                                                                .getReferenceProperty().getPropertyDetail().getWoodType().getId() : null));
                                
	         updatePropertyID(objection.getBasicProperty());
		PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
				"from PropertyTypeMaster ptm where ptm.code = ?", OWNERSHIP_TYPE_VAC_LAND);

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
					 propCompletionDate = objection.getReferenceProperty().getPropertyDetail().getDateOfCompletion();
				}
			} else {
				if (objection.getReferenceProperty().getPropertyDetail().getFloorDetails() != null
						&& objection.getReferenceProperty().getPropertyDetail().getFloorDetails().size() == 0) {
					// Mean currently plot details are present. Change to build
					// up property type.

					propService.changePropertyDetail(objection.getReferenceProperty(), new BuiltUpProperty(), objection
							.getReferenceProperty().getPropertyDetail().getFloorDetails().size());
					 propCompletionDate = propService.getLowestDtOfCompFloorWise(objection.getReferenceProperty().getPropertyDetail()
                                                 .getFloorDetails());
				}

			}
			propService.modifyDemand(objection.getReferenceProperty(), (PropertyImpl) objection.getBasicProperty().getProperty());
		/*	PropertyImpl newProperty = (PropertyImpl) propService.createDemand(createdNewProperty, propCompletionDate);
			
			
			Property modProperty = null;
	                Property previousProperty = objection.getBasicProperty().getProperty();
	                if (previousProperty == null) {
	                        LOGGER.info("modifyBasicProp, Could not get the previous property. DCB for arrears will be incorrect");
	                } else {
	                        modProperty = propService.createDemandForModify(previousProperty, newProperty, propCompletionDate);
	                        modProperty = propService.createArrearsDemand(previousProperty, propCompletionDate, newProperty);
	                  }	*/
			
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
		validateProperty(
				objection.getReferenceProperty(),
				(objection.getReferenceProperty().getPropertyDetail().getSitalArea() != null
						&& objection.getReferenceProperty().getPropertyDetail().getSitalArea()
								.getArea() != null ? objection.getReferenceProperty()
						.getPropertyDetail().getSitalArea().getArea().toString() : ""),
				(objection.getReferenceProperty().getPropertyDetail().getDateOfCompletion() != null ? sdf
						.format(objection.getReferenceProperty().getPropertyDetail()
								.getDateOfCompletion()).toString() : ""),
				isShowAckMessage,
				"",
				(objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster() != null ? objection
						.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster().getId()
						.toString()
						: null), ownerName, ownerName,(objection.getReferenceProperty().getPropertyDetail().getFloorType() != null ? objection
		                                        .getReferenceProperty().getPropertyDetail().getFloorType().getId() : null),
		                                        (objection.getReferenceProperty().getPropertyDetail().getRoofType() != null ? objection
		                                                .getReferenceProperty().getPropertyDetail().getRoofType().getId() : null),
		                                                (objection.getReferenceProperty().getPropertyDetail().getWallType() != null ? objection
		                                                        .getReferenceProperty().getPropertyDetail().getWallType().getId() : null),
		                                                        (objection.getReferenceProperty().getPropertyDetail().getWoodType() != null ? objection
		                                                                .getReferenceProperty().getPropertyDetail().getWoodType().getId() : null));

	}
	 private void populatePropertyTypeCategory() {
	      PropertyTypeMaster propTypeMstr=null;
	        if (propTypeId != null && !propTypeId.trim().isEmpty() && !propTypeId.equals("-1"))
	            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
	                    "from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
	        else if (objection!=null && objection.getReferenceProperty()!=null &&  objection.getReferenceProperty().getPropertyDetail() != null
	                && objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster() != null
	                && !objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster().getId().equals(-1))
	            propTypeMstr = objection.getReferenceProperty().getPropertyDetail().getPropertyTypeMaster();
	        else if (objection.getBasicProperty() != null)
	            propTypeMstr = objection.getBasicProperty().getProperty().getPropertyDetail().getPropertyTypeMaster();

	        if (propTypeMstr != null) {
	            if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
	                setPropTypeCategoryMap(VAC_LAND_PROPERTY_TYPE_CATEGORY);
	            else
	                setPropTypeCategoryMap(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
	        } else
	            setPropTypeCategoryMap(Collections.emptyMap());
	    }
	 
	 
	public Map<String, String> getPropTypeCategoryMap() {
        return propTypeCategoryMap;
    }

    public void setPropTypeCategoryMap(Map<String, String> propTypeCategoryMap) {
        this.propTypeCategoryMap = propTypeCategoryMap;
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

	public RevisionPetitionService getRevisionPetitionService() {
        return revisionPetitionService;
    }
	  private void updatePropertyID(final BasicProperty basicProperty) {
	        final PropertyID propertyId = basicProperty.getPropertyID();
	        if (propertyId != null) {
	            propertyId.setEastBoundary(getEastBoundary());
	            propertyId.setWestBoundary(getWestBoundary());
	            propertyId.setNorthBoundary(getNorthBoundary());
	            propertyId.setSouthBoundary(getSouthBoundary());
	        }
	    }
    public void setRevisionPetitionService(RevisionPetitionService revisionPetitionService) {
        this.revisionPetitionService = revisionPetitionService;
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
			this.ownerName = property.getBasicProperty().getFullOwnerName();
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(Address address) {
		if (address != null)
			this.propertyAddress = address.toString();
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

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	 public Map<String, String> getDeviationPercentageMap() {
	        return deviationPercentageMap;
	    }

	    public void setDeviationPercentageMap(Map<String, String> deviationPercentageMap) {
	        this.deviationPercentageMap = deviationPercentageMap;
	    }

        public String[] getFloorNoStr() {
            return floorNoStr;
        }

        public void setFloorNoStr(String[] floorNoStr) {
            this.floorNoStr = floorNoStr;
        }

        public List<DocumentType> getDocumentTypes() {
            return documentTypes;
        }

        public void setDocumentTypes(List<DocumentType> documentTypes) {
            this.documentTypes = documentTypes;
        }

        public TreeMap<String, String> getHearingTimingMap() {
            return hearingTimingMap;
        }

        public void setHearingTimingMap(TreeMap<String, String> hearingTimingMap) {
            this.hearingTimingMap = hearingTimingMap;
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

        public SimpleWorkflowService<Objection> getRevisionPetitionWorkFlowService() {
            return revisionPetitionWorkFlowService;
        }

        public void setRevisionPetitionWorkFlowService(SimpleWorkflowService<Objection> revisionPetitionWorkFlowService) {
            this.revisionPetitionWorkFlowService = revisionPetitionWorkFlowService;
        }      
	    
}
