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
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_MUTATION_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.OCCUPIER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CAT_MOBILE_TOWER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.TENANT;
import static org.egov.ptis.constants.PropertyTaxConstants.UNITTYPE_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.UNITTYPE_OPEN_PLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.UNITTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_OPENPLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.State;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.WorkflowBean;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PropertyTaxBaseAction extends BaseFormAction {
    private static Logger LOGGER = Logger.getLogger(PropertyTaxBaseAction.class);
    private static final long serialVersionUID = 1L;

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

    /**
     * <code>true</code> indicating that Data Entry is done
     */
    protected boolean allChangesCompleted;
    protected String fromDataEntry;
    protected String userRole;
    private AssignmentService assignmentService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    private List<File> uploads = new ArrayList<File>();
    private List<String> uploadFileNames = new ArrayList<String>();
    private List<String> uploadContentTypes = new ArrayList<String>();

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

    protected List<PropertyDocs> processAndStoreDocumentsWithReason(BasicProperty basicProperty, String reason) {
        List<PropertyDocs> fileStores = new ArrayList<>();
        if (!uploads.isEmpty()) {
           int fileCount = 0; 
           for (File file : uploads) {
               FileStoreMapper fileStore = fileStoreService.store(file, uploadContentTypes.get(fileCount++), "PTIS");
               final PropertyDocs propertyDoc = new PropertyDocs();
               propertyDoc.setSupportDoc(fileStore);
               propertyDoc.setBasicProperty(basicProperty);
               propertyDoc.setReason(DOCS_MUTATION_PROPERTY);
               basicProperty.addDocs(propertyDoc);
           } 
        }
        return fileStores;
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
        ajaxCommonAction.setPersistenceService(persistenceService);
        ajaxCommonAction.setDesignationService(new DesignationService());
        ajaxCommonAction.setAssignmentService(getAssignmentService());
        List<Department> departmentsForLoggedInUser = Collections.EMPTY_LIST;
		departmentsForLoggedInUser = propertyTaxUtil.getDepartmentsForLoggedInUser(securityUtils.getCurrentUser());
        workflowBean.setDepartmentList(departmentsForLoggedInUser);
        workflowBean.setDesignationList(Collections.EMPTY_LIST);
        workflowBean.setAppoverUserList(Collections.EMPTY_LIST);
		/*if (workflowBean.getDepartmentId() != null && workflowBean.getDepartmentId() != -1) {
			ajaxCommonAction.setDepartmentId(workflowBean.getDepartmentId());
			ajaxCommonAction.populateDesignationsByDept();
			workflowBean.setDesignationList(ajaxCommonAction.getDesignationMasterList());
		} else {
			workflowBean.setDesignationList(Collections.EMPTY_LIST);
		}
		if (workflowBean.getDesignationId() != null && workflowBean.getDesignationId() != -1) {
			ajaxCommonAction.setDesignationId(workflowBean.getDesignationId());
			ajaxCommonAction.populateUsersByDeptAndDesignation();
			workflowBean.setAppoverUserList(ajaxCommonAction.getUserList());
		} else {
			workflowBean.setAppoverUserList(Collections.EMPTY_LIST);
		}
		if (workflowBean != null) {
			LOGGER.debug("setupWorkflowDetails: No of Departments: "
					+ ((workflowBean.getDepartmentList() != null) ? workflowBean.getDepartmentList().size() : ZERO)
					+ "\nNo of Designations: "
					+ ((workflowBean.getDesignationList() != null) ? workflowBean.getDesignationList().size() : ZERO));
		}*/
        LOGGER.debug("Exiting from setupWorkflowDetails | End");
    }

	protected void validateProperty(Property property, String areaOfPlot, String dateOfCompletion,
			boolean chkIsTaxExempted, String taxExemptReason, String isAuthProp, String propTypeId, String propUsageId,
			String propOccId, Boolean isfloorDetailsRequired, Boolean isDataUpdate, Long floorTypeId, Long roofTypeId, Long wallTypeId, Long woodTypeId) {

        LOGGER.debug("Entered into validateProperty");
        // this is used for validating Usage for Property type
		/*PropertyUsage propUsage = null;
		String usage = null;*/

		if (floorTypeId == null || floorTypeId == -1) {
			addActionError(getText("mandatory.floorType"));
		}
		if (roofTypeId == null || roofTypeId == -1) {
			addActionError(getText("mandatory.roofType"));
		}
		if (wallTypeId == null || wallTypeId == -1) {
			addActionError(getText("mandatory.wallType"));
		}
		if (woodTypeId == null || woodTypeId == -1) {
			addActionError(getText("mandatory.woodType"));
		}
		if (propTypeId == null || propTypeId.equals(-1)) {
            addActionError(getText("mandatory.propType"));
        }

		/*if (property.getPropertyDetail().getExtra_field6() == null
                || property.getPropertyDetail().getExtra_field6().equals("-1")) {
            addActionError(getText("mandatory.locationFactor"));
		}*/

		/*if (isAuthProp == null) {
            addActionError(getText("mandatory.authProp"));
        }
        if (property.getExtra_field2() == null) {
            addActionError(getText("mandatory.noticeGen"));
        }
        if (property.getIsExemptedFromTax() != null) {
            if (taxExemptReason == null || taxExemptReason.equals("-1")) {
                addActionError(getText("mandatory.taxExmptRsn"));
            }
			
             * if (!(generalTax || sewerageTax || lightingTax || fireServTax ||
             * bigResdBldgTax || educationCess || empGuaCess)) {
             * addActionError("Please select atleast one Service Charges
             * Applicable"); }

		}*/

        if (propTypeId != null && !propTypeId.equals("-1")) {
			PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
					"from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
            if (propTypeMstr != null) {
				/*if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)
                        || propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
                        || propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
                    if (property.getPropertyDetail().getExtra_field1() == null
                            || property.getPropertyDetail().getExtra_field1().equals("-1")) {
                        addActionError(getText("mandatory.genWaterRate"));
                    }
				}*/
                if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {

                    if (propUsageId == null || propUsageId.equals("-1")) {
                        addActionError(getText("mandatory.usage"));
					} /*else {
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
					}*/
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
				/*if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_RESD)
                        || propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)
                        || propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
                    if (property.getPropertyDetail().getExtra_field5() == null
                            || property.getPropertyDetail().getExtra_field5().equals("-1")) {
                        addActionError(getText("mandatory.propTypeCategory"));
                    }
				}*/

				validateFloor(propTypeMstr, property.getPropertyDetail().getFloorDetailsProxy(), isfloorDetailsRequired, property);

				/*if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
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
				}*/

				/*if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
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
                    if (isfloorDetailsRequired) {
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
				}*/
				/*if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
                    String amenity = property.getPropertyDetail().getExtra_field4();
					if (amenity == null || StringUtils.isEmpty(amenity)
							|| StringUtils.equals(amenity, getText("default.select")) || amenity.equals("-1")) {
                        addActionError(getText("mandatory.amenity"));
                    }
				}*/
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

		/*Map<Integer, String> unitAndTaxExemptRsn = new HashMap<Integer, String>();
        Map<Integer, String> unitAndFloorNo = new HashMap<Integer, String>();
        Integer unitNo;
		String[] floorNoAndTaxExemptRsn;*/

        if (!propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
            if ((isGovtProperty && !isfloorDetailsRequired) || !isGovtProperty) {
                if (floorList != null && floorList.size() > 0) {
                    for (FloorIF floor : floorList) {
                        List<String> msgParams = null;
                        usage = null;
                        if (floor != null) {
                            msgParams = new ArrayList<String>();
							/*if (floor.getExtraField1() == null || floor.getExtraField1().equals("")) {
                                addActionError(getText("mandatory.unitNo"));
							}*/

							/*if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_MIXED)) {
                                if (floor.getUnitType().getId().equals(-1L)) {
                                    addActionError(getText("mandatory.unitType"));
                                }
                                if ("-1".equals(floor.getUnitTypeCategory())) {
                                    addActionError(getText("mandatory.unitTypeCategory"));
                                }
							}*/

							/*PropertyTypeMaster unitType = null;
                            if (floor.getUnitType() != null) {
								unitType = (PropertyTypeMaster) getPersistenceService().find(
										"from PropertyTypeMaster ptm where ptm.id = ?", floor.getUnitType().getId());
							}*/

							//boolean isFloorNoRequired = floor.getFloorNo() == null || floor.getFloorNo().toString().equals("-10");

                            /**
							 * 
							 * Making the Floor No mandatory when Property is not Mixed & when property is Mixed and Unit type 
                             * is not Open Plot
							 * 
                             */
							/*if (unitType == null ? isFloorNoRequired : unitType.getCode().equalsIgnoreCase(
									UNITTYPE_OPEN_PLOT) ? false : isFloorNoRequired) {
								addActionError(getText("mandatory.floorNO"));
							}*/
							
							if (floor.getFloorNo() == null || floor.getFloorNo().equals("-1")) {
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

							/*if (!(propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
                                    || propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT))) {
                                if (floor.getWaterRate() == null || floor.getWaterRate().equals("-1")) {
                                    addActionError(getText("mandatory.floorGenWaterRate", msgParams));
                                }
							}*/

							/*if (unitType == null || !unitType.getCode().equalsIgnoreCase(UNITTYPE_OPEN_PLOT)) {*/
                                if (floor.getStructureClassification() == null
                                        || (floor.getStructureClassification().getId().toString()).equals("-1")) {
                                    addActionError(getText("mandatory.constType", msgParams));
                                }

								if (floor.getDepreciationMaster() == null
										|| floor.getDepreciationMaster().getId() == null
                                        || (floor.getDepreciationMaster().getId().toString()).equals("-1")) {
                                    addActionError(getText("mandatory.ageFactor", msgParams));
                                }
							/*}*/

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
							/*if (floor.getPropertyOccupation() != null
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
                                if (propOcc != null && propOcc.getOccupancyCode().equals(OCCUPIER)) {
                                    if (floor.getExtraField2() == null || floor.getExtraField2().equals("")) {
                                        addActionError(getText("mandatory.nameOfOccupier.Occ"));
                                    }
                                }
							}*/

							/*if (unitType != null && unitType.getCode().equalsIgnoreCase(UNITTYPE_OPEN_PLOT)) {
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
*/							

                        }
                    }
                }

				/*String floorNo;
                List<String> msgParams = null;
                String exemptionRsn;
				Set<Integer> unitsWithError = new TreeSet<Integer>();*/

				/*if (!isGovtProperty) {
                    for (FloorIF floor : floorList) {

                        unitNo = Integer.valueOf(floor.getExtraField1());
						floorNo = floor.getFloorNo() == null ? "Open Plot" : CommonServices.getFloorStr(floor
								.getFloorNo());

                        if (unitAndFloorNo.get(unitNo) == null) {
                            unitAndFloorNo.put(unitNo, floorNo);
                        }

						exemptionRsn = floor.getTaxExemptedReason() == null
								|| floor.getTaxExemptedReason().equalsIgnoreCase("-1") ? "select" : floor
								.getTaxExemptedReason();

                        if (unitAndTaxExemptRsn.get(unitNo) == null) {
                            unitAndTaxExemptRsn.put(unitNo, floor.getFloorNo() + "-" + exemptionRsn);
                        } else {

                            floorNoAndTaxExemptRsn = unitAndTaxExemptRsn.get(unitNo).split("-");

                            if (!floorNoAndTaxExemptRsn[1].equalsIgnoreCase(exemptionRsn)) {
                                unitsWithError.add(unitNo);
                                unitAndFloorNo.put(unitNo, unitAndFloorNo.get(unitNo).concat(", ").concat(floorNo));
                            }
                        }
                    }
                    for (Integer unitNum : unitsWithError) {
                        msgParams = new ArrayList<String>();
                        msgParams.add(unitNum.toString());
                        msgParams.add(unitAndFloorNo.get(unitNum));
                        addActionError(getText("mandatory.taxExemptRsn", msgParams));
                    }
				}*/
                }
            }
        LOGGER.debug("Exiting from validate");
    }

    protected void validateHouseNumber(Long wardId, String houseNo, BasicProperty basicProperty) {
        Query qry = getPersistenceService().getSession().createQuery(
                "from BasicPropertyImpl bp where bp.address.houseNoBldgApt = :houseNo and bp.boundary.id = :wardId and bp.active = 'Y'");
        qry.setParameter("houseNo", houseNo);
        qry.setParameter("wardId", wardId);
		//this condition is reqd bcoz, after rejection the validation shouldn't happen for the same houseNo
        if (!qry.list().isEmpty() && (basicProperty == null
				|| (basicProperty != null 
				&& !basicProperty.getAddress().getHouseNoBldgApt().equals(houseNo)))) {
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
            if (role.getName().equalsIgnoreCase(PropertyTaxConstants.ASSISTANT_ROLE)) {
                setUserRole(role.getName());
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
        // FIX ME
		//Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EgovThreadLocals.getUserId()));
        Position position = null;
		//State stateEnd = new State("PropertyImpl", State.DEFAULT_STATE_VALUE_CLOSED, position, "Property Workflow Ended");
		property.transition().end().withStateValue(State.DEFAULT_STATE_VALUE_CLOSED).withOwner(position).withComments("Property Workflow Ended");
        // prevState.setNext(stateEnd);
        // property.setState(stateEnd);
        LOGGER.debug("endWorkFlow: After state change, Property: " + property);
        LOGGER.debug("Exit method endWorkFlow()");
    }

    // protected abstract PropertyImpl property();

    // protected abstract WorkflowService workflowService();

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

    public AssignmentService getAssignmentService() {
        return assignmentService;
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

}