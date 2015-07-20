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
package org.egov.ptis.actions.common;

import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PTVERIFIER_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.TENANT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_CANCEL;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.WorkflowBean;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PropertyTaxBaseAction extends BaseFormAction {
	private static Logger LOGGER = Logger.getLogger(PropertyTaxBaseAction.class);
	private static final long serialVersionUID = 1L;
	private static final String APPROVED = "Approved";
	private static final String REJECTED = "Rejected";
	protected PropertyTaxUtil propertyTaxUtil;
	@Autowired
	private SecurityUtils securityUtils;
	@Autowired
	private UserService userService;
	protected WorkflowBean workflowBean;
	protected String userDesgn;
	protected Boolean isApprPageReq = Boolean.TRUE;
	protected String indexNumber;
	String actionName = "";
	String wflowAction = "";
	protected String userRole;
	@Autowired
	private AssignmentService assignmentService;
	@Autowired
	private InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate;
	@Autowired
	protected EisCommonService eisCommonService;
	@Autowired
	@Qualifier("fileStoreService")
	protected FileStoreService fileStoreService;
	@Autowired
	private SimpleWorkflowService<PropertyImpl> propertyWorkflowService;
	private List<File> uploads = new ArrayList<File>();
	private List<String> uploadFileNames = new ArrayList<String>();
	private List<String> uploadContentTypes = new ArrayList<String>();
	private String nextAction;
	protected String approverName;
	protected Long approverPositionId;
	protected String approverComments;
	protected String workFlowAction;
	protected String ackMessage;

	public List<File> getUpload() {
		return this.uploads;
	}

	public void setUpload(List<File> uploads) {
		this.uploads = uploads;
	}

	public List<String> getUploadFileName() {
		return this.uploadFileNames;
	}

	public void setUploadFileName(List<String> uploadFileNames) {
		this.uploadFileNames = uploadFileNames;
	}

	public List<String> getUploadContentType() {
		return this.uploadContentTypes;
	}

	public void setUploadContentType(List<String> contentTypes) {
		this.uploadContentTypes = contentTypes;
	}

	protected void processAndStoreDocumentsWithReason(BasicProperty basicProperty, String reason) {
		if (!uploads.isEmpty()) {
			int fileCount = 0;
			for (File file : uploads) {
				FileStoreMapper fileStore = fileStoreService.store(file, uploadFileNames.get(fileCount),
						uploadContentTypes.get(fileCount++), "PTIS");
				final PropertyDocs propertyDoc = new PropertyDocs();
				propertyDoc.setSupportDoc(fileStore);
				propertyDoc.setBasicProperty(basicProperty);
				propertyDoc.setReason(reason);
				basicProperty.addDocs(propertyDoc);
			}
		}
	}

	private boolean validateApprover() {
		if (workflowBean != null) {
			LOGGER.debug("Entered into validateApprover, approverUserId: " + workflowBean.getApproverUserId());
		} else {
			LOGGER.debug("validateApprover: workflowBean is NULL");
		}
		if (workflowBean.getApproverUserId() == null || workflowBean.getApproverUserId() == -1) {
			addActionError(getText("property.workflow.approver.errormessage"));
		}
		LOGGER.debug("Exiting from validateApprover");
		return false;
	}

	public void validate() {
		LOGGER.debug("Entered into validate method");
		if (workflowBean.getActionName() != null) {
			String beanActionName[] = workflowBean.getActionName().split(":");
			if (beanActionName.length > 1) {
				wflowAction = beanActionName[1];// save or forward or approve or
				if (WFLOW_ACTION_STEP_FORWARD.equals(wflowAction)) {
					validateApprover();
				}
			}
		}

		LOGGER.debug("Exiting from validate");
	}

	protected List<StateHistory> setUpWorkFlowHistory(Long stateId) {
		List<StateHistory> workflowHisObj = inboxRenderServiceDeligate.getWorkflowHistory(stateId);
		workflowBean.setWorkFlowHistoryItems(workflowHisObj);
		return workflowHisObj;
	}

	@SuppressWarnings("unchecked")
	protected void setupWorkflowDetails() {
		LOGGER.debug("Entered into setupWorkflowDetails | Start");
		if (workflowBean != null) {
			LOGGER.debug("setupWorkflowDetails: Department: " + workflowBean.getDepartmentId() + " Designation: "
					+ workflowBean.getDesignationId());
		}
		AjaxCommonAction ajaxCommonAction = new AjaxCommonAction();
		ajaxCommonAction.setPersistenceService(persistenceService);
		ajaxCommonAction.setDesignationService(new DesignationService());
		ajaxCommonAction.setAssignmentService(getAssignmentService());
		List<Department> departmentsForLoggedInUser = Collections.EMPTY_LIST;
		departmentsForLoggedInUser = propertyTaxUtil.getDepartmentsForLoggedInUser(securityUtils.getCurrentUser());
		workflowBean.setDepartmentList(departmentsForLoggedInUser);
		workflowBean.setDesignationList(Collections.EMPTY_LIST);
		workflowBean.setAppoverUserList(Collections.EMPTY_LIST);
		LOGGER.debug("Exiting from setupWorkflowDetails | End");
	}

	protected void validateProperty(Property property, String areaOfPlot,String dateOfCompletion, boolean chkIsTaxExempted,
			String taxExemptReason, String propTypeId, String propUsageId,
			String propOccId, Long floorTypeId, Long roofTypeId,Long wallTypeId, Long woodTypeId) {

		LOGGER.debug("Entered into validateProperty");

		if (propTypeId == null || propTypeId.equals("-1")) {
			addActionError(getText("mandatory.propType"));
		}
		if (isBlank(property.getPropertyDetail().getCategoryType())) {
			addActionError(getText("mandatory.propcatType"));
		}

		if (propTypeId != null && !propTypeId.equals("-1")) {
			PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService()
					.find("from PropertyTypeMaster ptm where ptm.id = ?",
							Long.valueOf(propTypeId));
			if (propTypeMstr != null) {
				PropertyDetail propertyDetail = property.getPropertyDetail();
				if (propTypeMstr.getCode().equalsIgnoreCase(
						OWNERSHIP_TYPE_VAC_LAND)) {
					if (null != propertyDetail) {
						validateVacantProperty(propertyDetail);
					}
				} else {
					validateBuiltUpProperty(propertyDetail, floorTypeId,roofTypeId, areaOfPlot);
				}
				validateFloor(propTypeMstr, property.getPropertyDetail()
						.getFloorDetails(), property);
			}
		}

		/*
		 * String beanActionName[] = workflowBean.getActionName().split(":"); if
		 * (beanActionName.length > 1) { actionName = beanActionName[0];
		 * wflowAction = beanActionName[1]; }
		 */

		LOGGER.debug("Exiting from validateProperty");
	}

	private void validateVacantProperty(PropertyDetail propertyDetail) {

		if (isBlank(propertyDetail.getSurveyNumber())) {
			addActionError(getText("mandatory.surveyNo"));
		}
		if (isBlank(propertyDetail.getPattaNumber())) {
			addActionError(getText("mandatory.pattaNum"));
		}
		if (null == propertyDetail.getSitalArea().getArea()) {
			addActionError(getText("mandatory.vacantLandArea"));
		}
		if (null == propertyDetail.getDateOfCompletion()) {
			addActionError(getText("mandatory.dtOfCmpln"));
		} /*
		 * else { SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		 * Date occupationDate = null; try { occupationDate =
		 * sdf.parse(dateOfCompletion); } catch (ParseException e) {
		 * LOGGER.error(e.getMessage(), e); } if (occupationDate != null &&
		 * occupationDate.after(new Date())) {
		 * addActionError(getText("mandatory.dtBeforeCurr")); } }
		 */
		if (null == propertyDetail.getCurrentCapitalValue()) {
			addActionError(getText("mandatory.capitalValue"));
		}
		if (null == propertyDetail.getMarketValue()) {
			addActionError(getText("mandatory.marketValue"));
		}

	}

	private void validateBuiltUpProperty(PropertyDetail propertyDetail,
			Long floorTypeId, Long roofTypeId, String areaOfPlot) {
		if (null != propertyDetail.isBuildingPlanDetailsChecked()) {
			if (isBlank(propertyDetail.getBuildingPermissionNo())) {
				addActionError(getText("mandatory.buildingPlanNo"));
			}
			if (null == propertyDetail.getBuildingPermissionDate()) {
				addActionError(getText("mandatory.buildingPlanDate"));
			}
			if (isBlank(propertyDetail.getDeviationPercentage())) {
				addActionError(getText("mandatory.deviationPercentage"));
			}
		}
		if (propertyDetail.isStructure()) {
			if (isBlank(propertyDetail.getSiteOwner())) {
				addActionError(getText("mandatory.siteowner"));
			}
		}
		if (null != propertyDetail.isAppurtenantLandChecked()
				&& null == propertyDetail.getExtentAppartenauntLand()) {
			addActionError(getText("mandatory.extentAppartnant"));
		} else if (null == propertyDetail.isAppurtenantLandChecked()
				&& isBlank(areaOfPlot)) {
			addActionError(getText("mandatory.extentsite"));
		}
		if (floorTypeId == null || floorTypeId == -1) {
			addActionError(getText("mandatory.floorType"));
		}
		if (roofTypeId == null || roofTypeId == -1) {
			addActionError(getText("mandatory.roofType"));
		}
	}

	private void validateFloor(PropertyTypeMaster propTypeMstr, List<Floor> floorList, Property property) {
		LOGGER.debug("Entered into validateFloor \nPropertyTypeMaster:" + propTypeMstr + ", No of floors: "
				+ ((floorList != null) ? floorList : ZERO));

		if (!propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
			if (floorList != null && floorList.size() > 0) {
				for (Floor floor : floorList) {
					List<String> msgParams = null;
					if (floor != null) {
						msgParams = new ArrayList<String>();
						if (floor.getFloorNo() == null || floor.getFloorNo().equals(-10)) {
							addActionError(getText("mandatory.floorNO"));
						}
						msgParams.add(floor.getFloorNo() != null ? CommonServices.getFloorStr(floor.getFloorNo()) : "N/A");

						if (floor.getStructureClassification() == null
								|| floor.getStructureClassification().getId() == null
								|| (floor.getStructureClassification().getId().toString()).equals("-1")) {
							addActionError(getText("mandatory.constType", msgParams));
						}

						if(floor.getDrainage() && null == floor.getNoOfSeats()){
							addActionError(getText("mandatory.noofseats"));
						}
						if (floor.getPropertyUsage() == null || null == floor.getPropertyUsage().getId()
								|| (floor.getPropertyUsage().getId().toString()).equals("-1")) {
							addActionError(getText("mandatory.floor.usage", msgParams));
						}

						if (floor.getPropertyOccupation() == null || null == floor.getPropertyOccupation().getId()
								|| (floor.getPropertyOccupation().getId().toString()).equals("-1")) {
							addActionError(getText("mandatory.floor.occ"));
						} else {
							PropertyOccupation occupancy = (PropertyOccupation) getPersistenceService()
									.find("from PropertyOccupation po where po.id = ?",
											floor.getPropertyOccupation().getId());
							if (occupancy.getOccupation().equalsIgnoreCase(TENANT)
									&& floor.getOccupantName().equals("")) {
								addActionError(getText("mandatory.floor.occupantName"));
							}
						}

						if (floor.getOccupancyDate() == null || floor.getOccupancyDate().equals("")) {
							addActionError(getText("mandatory.floor.docOcc"));
						}
						if (floor.getOccupancyDate() != null && !floor.getOccupancyDate().equals("")) {
							if (floor.getOccupancyDate().after(new Date())) {
								addActionError(getText("mandatory.dtFlrBeforeCurr"));
							}
						}

						if (floor.getBuiltUpArea() == null || (floor.getBuiltUpArea().getArea() == null || floor.getBuiltUpArea().getArea()
										.equals(""))) {
							addActionError(getText("mandatory.assbleArea"));
						}
					}
				}
			}
		}
		LOGGER.debug("Exiting from validate");
	}

	protected void validateHouseNumber(Long wardId, String houseNo, BasicProperty basicProperty) {
		Query qry = getPersistenceService().getSession().createQuery(
						"from BasicPropertyImpl bp where bp.address.houseNoBldgApt = :houseNo and bp.boundary.id = :wardId and bp.active = 'Y'");
		qry.setParameter("houseNo", houseNo);
		qry.setParameter("wardId", wardId);
		// this condition is reqd bcoz, after rejection the validation shouldn't
		// happen for the same houseNo
		if (!qry.list().isEmpty()
				&& (basicProperty == null || (basicProperty != null && !basicProperty.getAddress().getHouseNoBldgApt()
						.equals(houseNo)))) {
			addActionError(getText("houseNo.unique"));
		}
	}

	public void setUserInfo() {
		LOGGER.debug("Entered into setUserInfo");

		Long userId = securityUtils.getCurrentUser().getId();
		LOGGER.debug("setUserInfo: Logged in userId" + userId);
		Designation designation = propertyTaxUtil.getDesignationForUser(userId);
		setUserDesgn(designation.getName());
		User user = userService.getUserById(userId);
		for (Role role : user.getRoles()) {
			if (role.getName().equalsIgnoreCase(ASSISTANT_ROLE) || role.getName().equalsIgnoreCase(PTVERIFIER_ROLE)) {
				setUserRole(role.getName());
				break;
			}
		}
		LOGGER.debug("Exit from setUserInfo");
	}

	public void transitionWorkFlow(PropertyImpl property) {
		final DateTime currentDate = new DateTime();
		User user = securityUtils.getCurrentUser();
		Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
		String designationName = userAssignment.getDesignation().getName();
		Position pos = null;

		if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
			Assignment preOwner = assignmentService.getPrimaryAssignmentForUser(property.getCreatedBy().getId());
			property.transition(true).withSenderName(user.getName())
					.withComments(approverComments).withStateValue("Rejected")
					.withDateInfo(currentDate.toDate())
					// .withStateValue(beanActionName[0])
					.withOwner(preOwner.getPosition())
					.withNextAction(WFLOW_ACTION_STEP_CANCEL);

		} else {
			if (approverPositionId != -1 && null != approverPositionId) {
				pos = (Position) persistenceService.find(
						"from Position where id=?", approverPositionId);
			} else if (COMMISSIONER_DESGN.equals(designationName)) {
				Assignment preOwner = assignmentService.getPrimaryAssignmentForUser(property.getCreatedBy().getId());
				pos = preOwner.getPosition();
			}
			if (null == property.getState()) {
				WorkFlowMatrix wfmatrix = propertyWorkflowService.getWfMatrix(
						property.getStateType(), null, null, null, null, null);
				// WorkFlowMatrix wfmatrix2 =
				// propertyWorkflowService.getWfMatrix(property.getStateType(),null,null,null,"New",null);
				property.transition().start().withSenderName(user.getName())
						.withComments(approverComments)
						.withStateValue(wfmatrix.getNextState())
						.withDateInfo(currentDate.toDate())
						// .withStateValue(beanActionName[0])
						.withOwner(pos)
						.withNextAction(wfmatrix.getNextAction());
			} else {
				if (property.getCurrentState().getNextAction().equalsIgnoreCase("END")
						|| workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_CANCEL)) {
					property.transition(true).end()
							.withSenderName(user.getName())
							.withComments(approverComments)
							.withDateInfo(currentDate.toDate());
				} else {
					WorkFlowMatrix wfmatrix = propertyWorkflowService.getWfMatrix(property.getStateType(), null, null,
									null, property.getCurrentState().getNextAction(), null);
					property.transition(true).withSenderName(user.getName())
							.withComments(approverComments)
							.withStateValue(wfmatrix.getCurrentState())
							.withDateInfo(currentDate.toDate())
							// .withStateValue(beanActionName[0])
							.withOwner(pos)
							.withNextAction(wfmatrix.getNextAction());
				}
			}
		}
		if (approverName != null && !approverName.isEmpty()
				&& !approverName.equalsIgnoreCase("----Choose----")) {
			String approvalmesg = " Succesfully Forwarded to " + approverName+ ".";
			ackMessage = ackMessage == null ? approvalmesg : ackMessage	+ approvalmesg;
		} else if (workFlowAction != null && workFlowAction.equalsIgnoreCase("cancel")) {
			String approvalmesg = " Succesfully Cancelled.";
			ackMessage = ackMessage == null ? approvalmesg : ackMessage	+ approvalmesg;
		}

		LOGGER.debug("Exiting method : transitionWorkFlow");
	}

	/*public void transitionWorkFlow(PropertyImpl property) {
		final DateTime currentDate = new DateTime();
		User user = securityUtils.getCurrentUser();
		Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
		String designationName = userAssignment.getDesignation().getName();
		String beanActionName[] = workflowBean.getActionName().split(":");
		String nextAction = null;
		Long approverUserdId = null;

		if (ASSISTANT_DESGN.equals(designationName)) {
			if (WFLOW_ACTION_STEP_FORWARD.equals(beanActionName[1])) {
				nextAction = designationName + " " + APPROVED;
				Assignment nextOwner = assignmentService.getPrimaryAssignmentForUser(workflowBean.getApproverUserId());
				if (property.hasState()) {
					approverUserdId = workflowBean.getApproverUserId();
					transition(property, beanActionName, nextAction, approverUserdId);
				} else {
					property.transition().start().withSenderName(user.getName())
							.withComments(workflowBean.getComments()).withDateInfo(currentDate.toDate())
							.withStateValue(beanActionName[0]).withOwner(nextOwner.getPosition())
							.withNextAction(nextAction);
				}

			} else if (WFLOW_ACTION_STEP_REJECT.equals(beanActionName[1])) {
				property.transition().end();
			} else {
				property.transition().end();
			}

		} else if (REVENUE_OFFICER_DESGN.equals(designationName)) {
			if (WFLOW_ACTION_STEP_FORWARD.equals(beanActionName[1])) {
				nextAction = designationName + " " + APPROVED;
				approverUserdId = workflowBean.getApproverUserId();
			} else if (WFLOW_ACTION_STEP_REJECT.equals(beanActionName[1])) {
				nextAction = designationName + " " + REJECTED;
				approverUserdId = property.getCreatedBy().getId();
			} else if (WFLOW_ACTION_STEP_APPROVE.equals(beanActionName[1])) {
				nextAction = designationName + " " + APPROVED;
				approverUserdId = property.getCreatedBy().getId();
			}
			if (property.hasState()) {
				transition(property, beanActionName, nextAction, approverUserdId);
			} else {
				Assignment nextOwner = assignmentService.getPrimaryAssignmentForUser(workflowBean.getApproverUserId());
				property.transition().start().withSenderName(user.getName()).withComments(workflowBean.getComments())
						.withDateInfo(currentDate.toDate()).withStateValue(beanActionName[0])
						.withOwner(nextOwner.getPosition()).withNextAction(nextAction);
			}

		} else if (COMMISSIONER_DESGN.equals(designationName)) {
			nextAction = designationName;
			approverUserdId = property.getCreatedBy().getId();
			if (WFLOW_ACTION_STEP_APPROVE.equals(beanActionName[1])) {
				nextAction = nextAction + " " + APPROVED;
			} else if (WFLOW_ACTION_STEP_REJECT.equals(beanActionName[1])) {
				nextAction = nextAction + " " + REJECTED;
			}
			transition(property, beanActionName, nextAction, approverUserdId);
		}

		LOGGER.debug("Exiting method : transitionWorkFlow");
	}*/

	private void transition(PropertyImpl property, String[] beanActionName, String nextAction, Long approverUserdId) {
		final DateTime currentDate = new DateTime();
		Assignment nextOwner = assignmentService.getPrimaryAssignmentForUser(approverUserdId);
		property.transition().withSenderName(securityUtils.getCurrentUser().getName())
				.withComments(workflowBean.getComments()).withDateInfo(currentDate.toDate())
				.withStateValue(beanActionName[0]).withOwner(nextOwner.getPosition()).withNextAction(nextAction);
	}
	
	public void validateApproverDetails(Long approverPositionId,String approverComments, String workFlowAction) {
		if (WFLOW_ACTION_STEP_FORWARD.equals(workFlowAction) || WFLOW_ACTION_STEP_SAVE.equals(workFlowAction)) {
			if (approverPositionId == -1) 
			addActionError(getText("property.workflow.approver.errormessage"));
		}
		if (WFLOW_ACTION_STEP_REJECT.equals(workFlowAction) && isBlank(approverComments)) {
			addActionError(getText("property.workflow.remarks"));
		}

	}

	public WorkflowBean getWorkflowBean() {
		return workflowBean;
	}

	public void setWorkflowBean(WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public String getUserDesgn() {
		return userDesgn;
	}

	public void setUserDesgn(String userDesgn) {
		this.userDesgn = userDesgn;
	}

	public Boolean getIsApprPageReq() {
		return isApprPageReq;
	}

	public void setIsApprPageReq(Boolean isApprPageReq) {
		this.isApprPageReq = isApprPageReq;
	}

	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public AssignmentService getAssignmentService() {
		return assignmentService;
	}

	public void setAssignmentService(AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}
	
	public String getApproverComments() {
		return approverComments;
	}

	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}

	public Long getApproverPositionId() {
		return approverPositionId;
	}

	public void setApproverPositionId(Long approverPositionId) {
		this.approverPositionId = approverPositionId;
	}

	public String getWorkFlowAction() {
		return workFlowAction;
	}

	public void setWorkFlowAction(String workFlowAction) {
		this.workFlowAction = workFlowAction;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	
	public SimpleWorkflowService<PropertyImpl> getPropertyWorkflowService() {
		return propertyWorkflowService;
	}

	public void setPropertyWorkflowService(
			SimpleWorkflowService<PropertyImpl> propertyWorkflowService) {
		this.propertyWorkflowService = propertyWorkflowService;
	}


}