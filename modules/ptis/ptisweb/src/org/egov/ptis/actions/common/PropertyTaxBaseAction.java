package org.egov.ptis.actions.common;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CAT_MOBILE_TOWER;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_MIXED;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PTAPPROVER_ROLE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PTCREATOR_ROLE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PTVALIDATOR_ROLE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.TENANT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.UNITTYPE_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.UNITTYPE_OPEN_PLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.UNITTYPE_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.USAGES_FOR_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.USAGES_FOR_OPENPLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.USAGES_FOR_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_FORWARD;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.State;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.service.EmployeeService;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.WorkflowBean;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Query;

@ParentPackage("egov")
public abstract class PropertyTaxBaseAction extends BaseFormAction {
	private static Logger LOGGER = Logger.getLogger(PropertyTaxBaseAction.class);
	private static final long serialVersionUID = 1L;

	protected EisCommonsService eisCommonsService;
	protected PropertyTaxUtil propertyTaxUtil;
	protected WorkflowBean workflowBean;
	protected EmployeeService employeeService;
	protected String userDesgn;
	protected Boolean isApprPageReq = Boolean.TRUE;
	protected String indexNumber;
	String actionName = "";
	String wflowAction = "";
	
	/**
	 * <code>true</code> indicating that Data Entry is done
	 */
	protected boolean allChangesCompleted;
	protected String fromDataEntry;
	protected String userRole;
	
	public PropertyTaxBaseAction() {
	}

	/**
	 * This method starts the workflow. Property is transitioned to NEW state.
	 * The workflow item is available in the creator's DRAFTS.
	 */

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
				wflowAction = beanActionName[1];// save or forward or approve or reject
			}
		}
		if (WFLOW_ACTION_STEP_FORWARD.equals(wflowAction)) {
			validateApprover();
		}
		LOGGER.debug("Exiting from validate");
	}

	@SuppressWarnings("unchecked")
	protected void setupWorkflowDetails() {
		LOGGER.debug("Entered into setupWorkflowDetails | Start");
		if (workflowBean != null) {
			LOGGER.debug("setupWorkflowDetails: Department: " + workflowBean.getDepartmentId() + " Designation: "
					+ workflowBean.getDesignationId());
		}
		AjaxCommonAction ajaxCommonAction = new AjaxCommonAction();
		ajaxCommonAction.setEmployeeService(employeeService);
		ajaxCommonAction.setPersistenceService(persistenceService);
		List<Department> departmentsForLoggedInUser = Collections.EMPTY_LIST;
		departmentsForLoggedInUser = propertyTaxUtil.getDepartmentsForLoggedInUser((getSession()));
		workflowBean.setDepartmentList(departmentsForLoggedInUser);
		if (workflowBean.getDepartmentId() != null && workflowBean.getDepartmentId() != -1) {
			ajaxCommonAction.setDepartmentId(workflowBean.getDepartmentId());
			ajaxCommonAction.populateDesignationsByDept();
			workflowBean.setDesignationList(ajaxCommonAction.getDesignationMasterList());
		} else {
			workflowBean.setDesignationList(Collections.EMPTY_LIST);
		}
		if (workflowBean.getDesignationId() != null && workflowBean.getDesignationId() != -1) {
			ajaxCommonAction.setDesignationId(workflowBean.getDesignationId());
			ajaxCommonAction.populateUsersByDesignation();
			workflowBean.setAppoverUserList(ajaxCommonAction.getUserList());
		} else {
			workflowBean.setAppoverUserList(Collections.EMPTY_LIST);
		}
		if (workflowBean != null) {
			LOGGER.debug("setupWorkflowDetails: No of Departments: "
					+ ((workflowBean.getDepartmentList() != null) ? workflowBean.getDepartmentList().size() : ZERO)
					+ "\nNo of Designations: "
					+ ((workflowBean.getDesignationList() != null) ? workflowBean.getDesignationList().size() : ZERO));
		}
		LOGGER.debug("Exiting from setupWorkflowDetails | End");
	}

	protected void validateProperty(Property property, String areaOfPlot, String dateOfCompletion,
			boolean chkIsTaxExempted, String taxExemptReason, String isAuthProp, String propTypeId, String propUsageId,
			String propOccId, Boolean isfloorDetailsRequired, Boolean isDataUpdate) {

		LOGGER.debug("Entered into validateProperty");
		// this is used for validating Usage for Property type
		PropertyUsage propUsage = null;
		String usage = null;

		if (propTypeId == null || propTypeId.equals("-1")) {
			addActionError(getText("mandatory.propType"));
		}

		if (property.getPropertyDetail().getExtra_field6() == null
				|| property.getPropertyDetail().getExtra_field6().equals("-1")) {
			addActionError(getText("mandatory.locationFactor"));
		}

		if (isAuthProp == null) {
			addActionError(getText("mandatory.authProp"));
		}
		if (property.getExtra_field2() == null) {
			addActionError(getText("mandatory.noticeGen"));
		}
		if (property.getIsExemptedFromTax() != null) {
			if (taxExemptReason == null || taxExemptReason.equals("-1")) {
				addActionError(getText("mandatory.taxExmptRsn"));
			}
			/*
			 * if (!(generalTax || sewerageTax || lightingTax || fireServTax ||
			 * bigResdBldgTax || educationCess || empGuaCess)) {
			 * addActionError("Please select atleast one Service Charges
			 * Applicable"); }
			 */
		}

		if (propTypeId != null && !propTypeId.equals("-1")) {
			PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
					"from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
			if (propTypeMstr != null) {
				if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)
						|| propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
						|| propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					if (property.getPropertyDetail().getExtra_field1() == null
							|| property.getPropertyDetail().getExtra_field1().equals("-1")) {
						addActionError(getText("mandatory.genWaterRate"));
					}
				}
				if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {

					if (propUsageId == null || propUsageId.equals("-1")) {
						addActionError(getText("mandatory.usage"));
					} else {
						propUsage = (PropertyUsage) getPersistenceService().find("from PropertyUsage pu where pu.id=?",
								Long.valueOf(propUsageId));

						List<String> msgParams = new ArrayList<String>();

						if (propUsage != null) {
							usage = propUsage.getUsageName();
							msgParams.add(usage);
						}

						if (usage != null && !USAGES_FOR_OPENPLOT.contains(usage)) {
							addActionError(getText("validate.usageForOpenPlot", msgParams));
						}
					}
					if (propOccId == null || propOccId.equals("-1")) {
						addActionError(getText("mandatory.occ"));
					}
					if (dateOfCompletion == null || dateOfCompletion.equals("")
							|| dateOfCompletion.equals("DD/MM/YYYY")) {
						addActionError(getText("mandatory.dtOfCmpln"));
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						Date occupationDate = null;
						try {
							occupationDate = sdf.parse(dateOfCompletion);
						} catch (ParseException e) {
							LOGGER.error(e.getMessage(), e);
						}
						if (occupationDate.after(new Date())) {
							addActionError(getText("mandatory.dtBeforeCurr"));
						}
					}
				}
				if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
					if (areaOfPlot == null || areaOfPlot.equals("")) {
						addActionError(getText("mandatory.areaOfPlot"));
					}
				}
				if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_RESD)
						|| propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)
						|| propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
					if (property.getPropertyDetail().getExtra_field5() == null
							|| property.getPropertyDetail().getExtra_field5().equals("-1")) {
						addActionError(getText("mandatory.propTypeCategory"));
					}
				}

				validateFloor(propTypeMstr, property.getPropertyDetail().getFloorDetailsProxy(), isfloorDetailsRequired, property);

				if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
					String propRent = property.getPropertyDetail().getExtra_field2();
					PropertyOccupation propertyOccupation = (PropertyOccupation) getPersistenceService().find(
							"from PropertyOccupation po where po.id=?", Long.valueOf(propOccId));
					String occupancy = null;
					if (propertyOccupation != null) {
						occupancy = propertyOccupation.getOccupancyCode();
					}
					if (occupancy != null && occupancy.equals(TENANT)) {
						if (propRent == null || StringUtils.isEmpty(propRent)) {
							addActionError(getText("mandatory.rent"));
						} else {
							Pattern p = Pattern.compile("[^0-9]");
							Matcher m = p.matcher(propRent);
							if (m.find()) {
								addActionError(getText("mandatory.validRent"));
							}
						}
						if (property.getPropertyDetail().getOccupierName() == null 
								|| property.getPropertyDetail().getOccupierName().equals("")) {
							addActionError(getText("mandatory.nameOfOccupier"));
						}
					}
				}

				if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
						|| propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					String bldngCost = property.getPropertyDetail().getExtra_field3();
					if (bldngCost == null || StringUtils.isEmpty(bldngCost)) {
						addActionError(getText("mandatory.bldngCost"));
					} else {
						Pattern p = Pattern.compile("[^0-9]");
						Matcher m = p.matcher(bldngCost);
						if (m.find()) {
							addActionError(getText("mandatory.validBldngCost"));
						}
					}
					if(isfloorDetailsRequired){
					if (dateOfCompletion == null || dateOfCompletion.equals("")
							|| dateOfCompletion.equals("DD/MM/YYYY")) {
						addActionError(getText("mandatory.dtOfCmpln"));
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						Date occupationDate = null;
						try {
							occupationDate = sdf.parse(dateOfCompletion);
						} catch (ParseException e) {
							LOGGER.error(e.getMessage(), e);
						}
						if (occupationDate.after(new Date())) {
							addActionError(getText("mandatory.dtBeforeCurr"));
						}
					}
				  }	
				}
				if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					String amenity = property.getPropertyDetail().getExtra_field4();
					if (amenity == null || StringUtils.isEmpty(amenity)
							|| StringUtils.equals(amenity, getText("default.select")) || amenity.equals("-1")) {
						addActionError(getText("mandatory.amenity"));
					}
				}
			}
		}

		if (!isDataUpdate) {
			String beanActionName[] = workflowBean.getActionName().split(":");
			if (beanActionName.length > 1) {
				actionName = beanActionName[0];
				wflowAction = beanActionName[1];// save or forward or approve or
												// reject
			}
		}
		
		LOGGER.debug("Exiting from validateProperty");
	}

	private void validateFloor(PropertyTypeMaster propTypeMstr, List<FloorImpl> floorList,
			Boolean isfloorDetailsRequired, Property property) {
		LOGGER.debug("Entered into validateFloor \nPropertyTypeMaster:" + propTypeMstr + ", No of floors: "
				+ ((floorList != null) ? floorList : ZERO));
		PropertyUsage propUsage = null;
		String usage = null;
		
		Boolean isGovtProperty = propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
				|| propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT);
		
		if (!propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if ((isGovtProperty && !isfloorDetailsRequired)	|| !isGovtProperty) {
				if (floorList != null && floorList.size() > 0) {
					for (FloorIF floor : floorList) {
						List<String> msgParams = null;
						usage = null;
						if (floor != null) {
							msgParams = new ArrayList<String>();
							if (floor.getExtraField1() == null || floor.getExtraField1().equals("")) {
								addActionError(getText("mandatory.unitNo"));
							}
							
							if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_MIXED)) {
								if (floor.getUnitType().getId().equals(-1L)) {
									addActionError(getText("mandatory.unitType"));
								} 
								if ("-1".equals(floor.getUnitTypeCategory())) {
									addActionError(getText("mandatory.unitTypeCategory"));
								}
							}
							
							if (!propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_MIXED)
									&& (floor.getFloorNo() == null || floor.getFloorNo().toString().equals("-10"))) {
								addActionError(getText("mandatory.floorNO"));
							}
							
							msgParams.add(floor.getExtraField1() != null ? floor.getExtraField1() : "N/A");
							msgParams.add(floor.getFloorNo() != null ? CommonServices.getFloorStr(floor
									.getFloorNo()) : "N/A");
							
							if (floor.getBuiltUpArea() == null
									|| (floor.getBuiltUpArea().getArea() == null || floor.getBuiltUpArea().getArea()
											.equals(""))) {
								addActionError(getText("mandatory.assbleArea"));
							}
							if (floor.getPropertyUsage() == null
									|| (floor.getPropertyUsage().getId().toString()).equals("-1")) {								
								addActionError(getText("mandatory.floor.usage", msgParams));
							} else {
								propUsage = (PropertyUsage) getPersistenceService().find(
										"from PropertyUsage pu where pu.id=?", floor.getPropertyUsage().getId());
								usage = propUsage.getUsageName();
								msgParams.add(usage);
								msgParams.add(propTypeMstr.getType());
							}
							if (floor.getPropertyOccupation() == null
									|| (floor.getPropertyOccupation().getId().toString()).equals("-1")) {
								addActionError(getText("mandatory.floor.occ"));
							}
							
							if (!(propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
									|| propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT))) {
								if (floor.getWaterRate() == null || floor.getWaterRate().equals("-1")) {
									addActionError(getText("mandatory.floorGenWaterRate", msgParams));
								}
							}
							
							PropertyTypeMaster unitType = null;
							if (floor.getUnitType() != null) {							
								unitType = (PropertyTypeMaster) getPersistenceService().find(
										"from PropertyTypeMaster ptm where ptm.id = ?", floor.getUnitType().getId());
							}
							
							if (unitType == null || !unitType.getCode().equalsIgnoreCase(UNITTYPE_OPEN_PLOT)) {
								if (floor.getStructureClassification() == null
										|| (floor.getStructureClassification().getId().toString()).equals("-1")) {									
									addActionError(getText("mandatory.constType", msgParams));
								}

								if (floor.getDepreciationMaster() == null
										|| floor.getDepreciationMaster().getId() == null
										|| (floor.getDepreciationMaster().getId().toString()).equals("-1")) {									
									addActionError(getText("mandatory.ageFactor", msgParams));
								} 
							}
							
							if (floor.getExtraField3() == null || floor.getExtraField3().equals("")) {
								addActionError(getText("mandatory.floor.docOcc"));
							}
							if (floor.getExtraField3() != null && !floor.getExtraField3().equals("")
									&& !floor.getExtraField3().equalsIgnoreCase("DD/MM/YYYY")) {
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date occupationDate = null;
								try {
									occupationDate = sdf.parse(floor.getExtraField3());
								} catch (ParseException e) {
									LOGGER.error(e.getMessage(), e);
								}
								if (occupationDate.after(new Date())) {
									addActionError(getText("mandatory.dtFlrBeforeCurr"));
								}
							}
							if (floor.getPropertyOccupation() != null
									&& !(floor.getPropertyOccupation().getId().toString()).equals("-1")) {
								PropertyOccupation propOcc = (PropertyOccupation) getPersistenceService().find(
										"from PropertyOccupation po where po.id = ?",
										floor.getPropertyOccupation().getId());
								if (propOcc != null && propOcc.getOccupancyCode().equals(TENANT)) {
									if (floor.getRentPerMonth() == null || floor.getRentPerMonth().equals("")) {
										addActionError(getText("mandatory.rent"));
									}
									if (floor.getExtraField2() == null || floor.getExtraField2().equals("")) {
										addActionError(getText("mandatory.nameOfOccupier"));
									}
								}
							}
							
							if (unitType != null && unitType.getCode().equalsIgnoreCase(UNITTYPE_OPEN_PLOT)) {
								if (usage != null && !USAGES_FOR_OPENPLOT.contains(usage)) {
									addActionError(getText("validate.usageForOpenPlot", msgParams));
								}
							} else if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
									|| propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)
									|| propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_RESD)
									|| (unitType != null && unitType.getCode().equalsIgnoreCase(UNITTYPE_RESD))) {

								if (usage != null && !USAGES_FOR_RESD.contains(usage)) {
									addActionError(getText("validate.usageForFloor", msgParams));
								}
							} else if (usage != null && (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD) 
														 || (unitType != null && unitType.getCode().equalsIgnoreCase(UNITTYPE_NON_RESD)))) {
								if (!USAGES_FOR_NON_RESD.contains(usage)) {
									addActionError(getText("validate.usageForFloor", msgParams));
								}
							}
							
							if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)
									|| (unitType != null && unitType.getCode().equalsIgnoreCase(UNITTYPE_NON_RESD))) {
								if (!property.getPropertyDetail().getExtra_field5()
										.equalsIgnoreCase(PROPTYPE_CAT_MOBILE_TOWER)) {

									if (floor.getExtraField6() == null || floor.getExtraField6().equals("")) {
										addActionError(getText("mandatory.InterWallArea"));
									}
								}
							}
						}											
					}
				}
				
			}
		}
		LOGGER.debug("Exiting from validate");
	}

	protected void validateHouseNumber(Integer wardId, String houseNo, BasicProperty basicProperty) {
		Query qry = getPersistenceService().getSession().createQuery(
				"from BasicPropertyImpl bp where bp.address.houseNo = :houseNo and bp.boundary.id = :wardId and bp.active = 'Y'");
		qry.setParameter("houseNo", houseNo);
		qry.setParameter("wardId", wardId);
		//this condition is reqd bcoz, after rejection the validation shouldn't happen for the same houseNo
		if (!qry.list().isEmpty() && (basicProperty == null 
				|| (basicProperty != null 
				&& !basicProperty.getAddress().getHouseNo().equals(houseNo)))) {
			addActionError(getText("houseNo.unique"));
		}
	}
	
	public void setUserInfo() {
		LOGGER.debug("Entered into setUserInfo");

		UserDAO userDao = new UserDAO();
		Integer userId = Integer.valueOf(propertyTaxUtil.getLoggedInUser(getSession()).getId());
		LOGGER.debug("setUserInfo: Logged in userId" + userId);
		DesignationMaster desgn = propertyTaxUtil.getDesignationForUser(userId);
		setUserDesgn(desgn.getDesignationName());
		User user = userDao.getUserByID(userId);
		for (Role role : user.getRoles()) {
			if (role.getRoleName().equalsIgnoreCase(PTCREATOR_ROLE) || role.getRoleName().equalsIgnoreCase(PTVALIDATOR_ROLE) 
					|| role.getRoleName().equalsIgnoreCase(PTAPPROVER_ROLE)) {
				setUserRole(role.getRoleName());
				break;
			}
		}
		LOGGER.debug("Exit from setUserInfo");
	}

	/**
	 * This method ends the workflow Manually. The Property is transitioned to
	 * END state.
	 */
	public void endWorkFlow(PropertyImpl property) {
		LOGGER.debug("Enter method endWorkFlow, Property: " + property);

		State prevState = property.getCurrentState();
		Position position = eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		State stateEnd = new State("PropertyImpl", State.END, position, "Property Workflow Ended");
		prevState.setNext(stateEnd);
		property.setState(stateEnd);
		LOGGER.debug("endWorkFlow: After state change, Property: " + property);
		LOGGER.debug("Exit method endWorkFlow()");
	}
	
	//protected abstract PropertyImpl property();

	//protected abstract WorkflowService workflowService();

	public WorkflowBean getWorkflowBean() {
		return workflowBean;
	}

	public void setWorkflowBean(WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
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

	public boolean isAllChangesCompleted() {
		return allChangesCompleted;
	}

	public void setAllChangesCompleted(boolean allChangesCompleted) {
		this.allChangesCompleted = allChangesCompleted;
	}

	public String getFromDataEntry() {
		return fromDataEntry;
	}

	public void setFromDataEntry(String fromDataEntry) {
		this.fromDataEntry = fromDataEntry;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
}
