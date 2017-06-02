/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.web.struts.actions.workflow;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrixDetails;
import org.egov.infra.workflow.matrix.service.WorkFlowMatrixService;
import org.egov.pims.commons.Designation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@ParentPackage("egov")
public class WorkFlowMatrixAction extends BaseFormAction {

        private static final long serialVersionUID = 1L;
        public WorkFlowMatrix workFlowMatrix = new WorkFlowMatrix();
        private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowMatrixAction.class);
        private String mode;
        private Boolean amountRule;
        private Date fromDate;
        private Date toDate;
        private BigDecimal fromAmount;
        private BigDecimal toAmount;
        private String additionalRule;
        private Long objectType;
        private String[] department;
        private String[] departmentSelected;
        private String additionalRuleSelected;
        private List<Department> departmentList = new ArrayList<Department>();
        private List<WorkflowTypes> objectList = new ArrayList<WorkflowTypes>();
        private List<String> additionlRuleList = new ArrayList();
        private WorkFlowMatrixService workFlowMatrixService;
        private Long objectTypeid;
        private List additionalRuleList;
        private List<WorkFlowMatrixDetails> workFlowMatrixDetails = new ArrayList<WorkFlowMatrixDetails>();
        private List<WorkFlowMatrixDetails> workFlowMatrixRejectDetails = new ArrayList<WorkFlowMatrixDetails>();
        private List<Designation> designationList = new ArrayList();
        private List stateList = new ArrayList();
        private List statusList = new ArrayList();
        private List buttonList = new ArrayList();
        private List actionList = new ArrayList();
        private List<WorkFlowMatrixDetails> viewList = new ArrayList<WorkFlowMatrixDetails>();
        private String objectTypeView;
        private Date fromDateView;
        private Date toDateView;
        private BigDecimal fromQtyView;
        private BigDecimal toQtyView;
        private String additionalRuleView;
        private String searchAction;
        private Date modifyToDate;
        private Date legacyDate;
        private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        private String departmentstring;
        private WorkFlowMatrixDetails rejectionDetail;
        public static final String REJECTED = "Rejected";
        public static final String DEFAULT = "ANY";
        public static final String SEARCH = "search";
        public static final String RESULTS = "results";
        public static final String VIEW = "view";
        public static final String MODIFY = "modify";
        public static final String OBJECTTYPE = "ObjectType";
        public static final String ADDITIONALRULE = "AdditionalRule";
        public static final String FROMDATE = "FromDate";
        public static final String TODATE = "ToDate";
        public static final String FROMAMOUNT = "FromAmount";
        public static final String TOAMOUNT = "ToAmount";
        public static final String DEPARTMENTS = "Departments";
        public static final String MODIFYDATE = "ModifyDate";

        @Override
        public void prepare() {

                LOGGER.info("Prepare Method is called");
                super.prepare();
                final Department anyDept = new Department();
                anyDept.setName(DEFAULT);
                anyDept.setCode(DEFAULT);
                this.departmentList = this.workFlowMatrixService.getdepartmentList();
                this.departmentList.add(anyDept);
                this.objectList = this.workFlowMatrixService.getobjectTypeList();
                addDropdownData("objectTypeList", this.objectList);
                addDropdownData("departmentList", this.departmentList);
                if (getObjectType() != null) {
                        this.additionlRuleList = this.workFlowMatrixService.getAdditionalRulesforObject(getObjectType());
                        this.stateList = this.workFlowMatrixService.getDetailsforObject(getObjectType()).get("StateList");
                        this.statusList = this.workFlowMatrixService.getDetailsforObject(getObjectType()).get("StatusList");
                        this.buttonList = this.workFlowMatrixService.getDetailsforObject(getObjectType()).get("ButtonsList");
                        this.actionList = this.workFlowMatrixService.getDetailsforObject(getObjectType()).get("ActionsList");
                }
                this.designationList = this.workFlowMatrixService.getdesignationList();
                addDropdownData("additionalRuleList", this.additionlRuleList);
                addDropdownData("stateList", this.stateList);
                addDropdownData("statusList", this.statusList);
                addDropdownData("buttonList", this.buttonList);
                addDropdownData("designationList", this.designationList);
                addDropdownData("actionList", this.actionList);
                LOGGER.info("Prepare Method Ended");
        }

        @Override
        public Object getModel() {
                return null;
        }

        /**
         * This method displays the create workflow matrix screen
         * @return
         */
        public String newForm() {

                setAmountRule(Boolean.FALSE);
                return NEW;
        }

        /**
         * This method displays the view workflow matrix screen
         * @return
         */
        public String viewForm() {
                setMode(SEARCH);
                return SEARCH;
        }

        /**
         * This method returns list of workflowmatrixdetails containing workflowmatrix objects
         * @return
         */
        public String viewMatrixResults() {
                LOGGER.info("viewMatrixResults Method is called");
                final HashMap workflowheaderparams = getHeaderParams();
                getWorkFlowMatrixObject(workflowheaderparams);
                setMode(RESULTS);
                LOGGER.info("viewMatrixResults Method is ended");
                return SEARCH;
        }

        /**
         * This method checks for any existing matrix (validation for duplicates) for the enterd objecttype,date,fromqty,toqty and department and returns matrixdetails table on success else displays error message
         * @return
         */
        public String buildWorkflow() {
                LOGGER.info("buildWorkflow Method is called");
                setDepartmentSelected(getDepartment());
                setAdditionalRuleSelected(getAdditionalRule());
                if (!checkEistingMatrix()) {
                        addActionError("WorkFlow already exists for " + getDepartmentstring() + " departments");
                        return NEW;
                }
                final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                Date formatfromDate = null;
                Date formatCurrentDate = null;
                try {
                        formatCurrentDate = dateFormat.parse(dateFormat.format(new Date()));
                        formatfromDate = dateFormat.parse(dateFormat.format(getFromDate()));

                } catch (final ParseException e) {
                }

                if (formatfromDate.before(formatCurrentDate) && getDepartment().length > 1) {
                        addActionError("Only one department can be selected if you are entering matrix for older dates ");
                        return NEW;
                }
                if (formatfromDate.before(formatCurrentDate)) {
                        if (!checkExistingMatrixforLegacy()) {
                                final Calendar calendar = Calendar.getInstance();
                                calendar.setTime(getLegacyDate());
                                calendar.add(Calendar.DAY_OF_YEAR, -1);

                                addActionMessage("A workflow is already present from " + this.sdf.format(getLegacyDate()) + ". The workflow you are about to add will be valid for records created between " + this.sdf.format(getFromDate()) + " and "
                                                + this.sdf.format(calendar.getTime()));

                        }
                }
                setMode(EDIT);
                this.workFlowMatrixDetails.add(new WorkFlowMatrixDetails());
                this.workFlowMatrixRejectDetails.add(new WorkFlowMatrixDetails());
                LOGGER.info("buildWorkflow Method is ended");
                return NEW;
        }

        /**
         * This method deletes the workflow matrix object
         * @return
         */
        private void deleteWorkFlowMatrixObject(final HashMap workflowsearchparams) {
                this.workFlowMatrixService.deleteWorkFlowforObject(workflowsearchparams);
        }

        /**
         * This method checks for any existing matrix (validation for duplicates) for the enterd objecttype,date,fromqty,toqty and department and returns true if none found else returns false
         * @return
         */
        private boolean checkEistingMatrix() {
                LOGGER.info("checkEistingMatrix Method is called");
                final HashMap workflowheaderparams = getHeaderParams();
                final List checkmatrixList = this.workFlowMatrixService.checkIfMatrixExists(workflowheaderparams);
                if (checkmatrixList == null) {
                        return true;
                } else {
                        final StringBuffer departmentString = new StringBuffer();
                        for (final Long matrixid : (List<Long>) checkmatrixList) {
                if (!new String(departmentString).contains(this.workFlowMatrixService.getWorkFlowObjectbyId(matrixid).getDepartment())) {
                                        departmentString.append(" ");
                                        departmentString.append(this.workFlowMatrixService.getWorkFlowObjectbyId(matrixid).getDepartment());
                                        departmentString.append(",");
                                }

                        }
            setDepartmentstring(departmentString.substring(0, departmentString.length() - 1));
                        LOGGER.info("checkEistingMatrix Method is ended");
                        return false;
                }

        }

        /**
         * This method checks for any existing matrix for legacy date, for the entered objecttype,date,fromqty,toqty and department and returns true if none found else returns false
         * @return
         */
        private boolean checkExistingMatrixforLegacy() {
                LOGGER.info("checkExistingMatrixforLegacy Method is called");
                final HashMap workflowheaderparams = getHeaderParams();
                setLegacyDate(this.workFlowMatrixService.checkLegacyMatrix(workflowheaderparams));
                LOGGER.info("checkExistingMatrixforLegacy Method is ended");
        return this.legacyDate == null;

        }

        /**
         * This method gets the additional rules defined for the objecttype in WorkFlowAdditionalRule
         * @return
         */
        public String getAdditionalRuleforObjecttype() {

                this.additionalRuleList = this.workFlowMatrixService.getAdditionalRulesforObject(getObjectTypeid());
                return "additionalRule";
        }

        /**
         * This method creates the workflow matrix for the selected objecttype
         * @return
         */
        public String createWorkFlowforobjects() {
                LOGGER.info("createWorkFlowforobjects Method is called");
                final List<WorkFlowMatrixDetails> matrixList = getWorkFlowMatrixDetails();
                Collections.sort(matrixList);
                prepareWorkFlowMatrixDetails(matrixList);
                setDepartmentSelected(getDepartment());
                setAdditionalRuleSelected(getAdditionalRule());
                setMode(VIEW);
                LOGGER.info("createWorkFlowforobjects Method is ended");
                return NEW;

        }

        /**
         * This method is called to create the workflow matrix object from the workflowmatrixdetails entered and to persist in the db
         * @return
         */
        private String prepareWorkFlowMatrixDetails(final List<WorkFlowMatrixDetails> matrixList) {
                LOGGER.info("prepareWorkFlowMatrixDetails Method is called");
                final List<WorkFlowMatrix> actWorkFlowMatrixList = new LinkedList<WorkFlowMatrix>();
                for (int i = 0; i < matrixList.size(); i++) {
                        final WorkFlowMatrix wfMatrixObj = new WorkFlowMatrix();
                        wfMatrixObj.setFromDate(getFromDate());
                        if (getLegacyDate() != null) {
                                setToDate((getLegacyDate()));
                        }
                        wfMatrixObj.setToDate(getToDate());
                        wfMatrixObj.setObjectType((this.workFlowMatrixService.getobjectTypebyId(getObjectType())).getType());
                        wfMatrixObj.setFromQty(getFromAmount());
                        wfMatrixObj.setToQty(getToAmount());
                        wfMatrixObj.setAdditionalRule(getAdditionalRule() != "-1" ? getAdditionalRule() : null);
                        actWorkFlowMatrixList.add(wfMatrixObj);
                }

                if (getMode().equals(MODIFY)) {
                        if (updateExistingMatrix()) {
                                this.workFlowMatrixService.save(createActualWorkFlowMatrixDetails(actWorkFlowMatrixList, matrixList), getDepartment());
                        } else {
                                deleteWorkFlowMatrixObject(getSearchParams());
                                this.workFlowMatrixService.save(createActualWorkFlowMatrixDetails(actWorkFlowMatrixList, matrixList), getDepartment());
                                // addActionMessage("The Matrix you are trying to modify was created today ,It has been deleted and a new matrix is created");
                                return NEW;
                        }

                } else {
                        this.workFlowMatrixService.save(createActualWorkFlowMatrixDetails(actWorkFlowMatrixList, matrixList), getDepartment());
                }
                LOGGER.info("prepareWorkFlowMatrixDetails Method is ended");
                return NEW;

        }

        /**
         * This method is called to create the workflow matrix object from the workflowmatrixdetails entered
         * @return
         */
        private List<WorkFlowMatrix> createActualWorkFlowMatrixDetails(final List<WorkFlowMatrix> ActualwfMatrixObjList, final List<WorkFlowMatrixDetails> matrixList) {
                LOGGER.info("createActualWorkFlowMatrixDetails Method is called");
                for (int i = 0; i < ActualwfMatrixObjList.size(); i++) {
                        final WorkFlowMatrix wfMatrixObj = ActualwfMatrixObjList.get(i);
                        wfMatrixObj.setNextState(matrixList.get(i).getState());
                        wfMatrixObj.setNextAction(matrixList.get(i).getAction());
                        wfMatrixObj.setNextDesignation(matrixList.get(i).getDesignationString());
                        wfMatrixObj.setNextStatus(matrixList.get(i).getStatus());
                        wfMatrixObj.setValidActions(matrixList.get(i).getButtonString());
                        wfMatrixObj.setAdditionalRule(!getAdditionalRule().equals("-1") ? getAdditionalRule() : null);
                        if (i == 0) {
                                wfMatrixObj.setCurrentState("NEW");
                                wfMatrixObj.setCurrentStatus(matrixList.get(i).getStatus());
                        } else {
                                wfMatrixObj.setCurrentState(ActualwfMatrixObjList.get(i - 1).getNextState());
                                wfMatrixObj.setCurrentStatus(ActualwfMatrixObjList.get(i - 1).getNextStatus());
                                wfMatrixObj.setPendingActions(ActualwfMatrixObjList.get(i - 1).getNextAction());
                                wfMatrixObj.setCurrentDesignation(ActualwfMatrixObjList.get(i - 1).getNextDesignation());
                        }
                }
                if (ActualwfMatrixObjList.size() > 1) {
                        if (getWorkFlowMatrixRejectDetails().get(0).getRejectAction() == null) {
                                final WorkFlowMatrix matrixforReject = ActualwfMatrixObjList.get(0).clone();
                                matrixforReject.setCurrentState(REJECTED);
                                ActualwfMatrixObjList.add(matrixforReject);
                        } else {
                                final List<WorkFlowMatrix> RejectionList = new ArrayList();
                                final List<WorkFlowMatrixDetails> matrixDetList = getWorkFlowMatrixRejectDetails();
                                if (matrixDetList.size() > 0) {
                                        for (int i = 0; i < matrixDetList.size(); i++) {
                                                final WorkFlowMatrix wfMatrixObj = new WorkFlowMatrix();
                                                wfMatrixObj.setFromDate(getFromDate());
                                                if (getLegacyDate() != null) {
                                                        setToDate((getLegacyDate()));
                                                }
                                                wfMatrixObj.setToDate(getToDate());
                                                wfMatrixObj.setObjectType((this.workFlowMatrixService.getobjectTypebyId(getObjectType())).getType());
                                                wfMatrixObj.setFromQty(getFromAmount());
                                                wfMatrixObj.setToQty(getToAmount());
                                                wfMatrixObj.setAdditionalRule(!getAdditionalRule().equals("-1") ? getAdditionalRule() : null);
                                                if (i == 0) {
                                                        wfMatrixObj.setCurrentState("Rejected");
                                                } else {
                                                        wfMatrixObj.setCurrentState(RejectionList.get(i - 1).getNextState());
                                                        wfMatrixObj.setCurrentStatus(RejectionList.get(i - 1).getNextStatus());
                                                        wfMatrixObj.setPendingActions(RejectionList.get(i - 1).getNextAction());
                                                        wfMatrixObj.setCurrentDesignation(RejectionList.get(i - 1).getNextDesignation());
                                                }
                                                wfMatrixObj.setNextDesignation(matrixDetList.get(i).getRejectdesignationString());
                                                wfMatrixObj.setNextAction(matrixDetList.get(i).getRejectAction());
                                                wfMatrixObj.setNextState(matrixDetList.get(i).getRejectState());
                                                wfMatrixObj.setNextStatus(matrixDetList.get(i).getRejectStatus());
                                                wfMatrixObj.setValidActions(matrixDetList.get(i).getRejectbuttonString());
                                                RejectionList.add(wfMatrixObj);
                                        }
                                }
                ActualwfMatrixObjList.addAll(RejectionList);
                        }
                }
                LOGGER.info("createActualWorkFlowMatrixDetails Method is ended");
                return ActualwfMatrixObjList;
        }

        /**
         * This method is called to get the workflowmatrixdetails containing workflowmatrix objects
         * @return
         */
        private List<WorkFlowMatrixDetails> getWorkFlowMatrixObject(final HashMap<String, String> workFlowObjectMap) {

                this.viewList = this.workFlowMatrixService.getWorkFlowMatrixObjectForView(workFlowObjectMap);
                return this.viewList;
        }

        /**
         * This method is called to display the modify screen for the selected objecttype,department,date,from and to amount
         * @return
         */
        public String prepareModifyForm() {
                LOGGER.info("prepareModifyForm Method is called");
                final HashMap workflowsearchparams = getSearchParams();
                setDepartmentSelected(getDepartment());
                setAdditionalRuleSelected((String) workflowsearchparams.get("AdditionalRule"));
                if (workflowsearchparams.get("FromAmount") != null) {
                        setFromAmount((BigDecimal) (workflowsearchparams.get("FromAmount")));
                        setToAmount((BigDecimal) (workflowsearchparams.get("ToAmount")));
                        setAmountRule(Boolean.TRUE);
                } else {
                        setAmountRule(Boolean.FALSE);
                }
                final List<WorkFlowMatrix> matrixList = this.workFlowMatrixService.getWorkFlowforObjectforModify(workflowsearchparams);
                int sublistIndex = matrixList.size();
                for (int i = 0; i < matrixList.size(); i++) {
                        if (matrixList.get(i).getNextAction().equals("END")) {
                                sublistIndex = i;
                        }
                }
                List<WorkFlowMatrixDetails> workFlowList = new LinkedList();
                List<WorkFlowMatrixDetails> rejectionList = new LinkedList();
                workFlowList = this.workFlowMatrixService.prepareWorkFlowMatrixDetailsList(matrixList.subList(0, sublistIndex + 1), workFlowList, Boolean.TRUE);
                rejectionList = this.workFlowMatrixService.prepareWorkFlowMatrixDetailsList(matrixList.subList(sublistIndex + 1, matrixList.size()), rejectionList, Boolean.FALSE);
                if (rejectionList.isEmpty()) {
                        rejectionList.add(new WorkFlowMatrixDetails());
                }

                this.workFlowMatrixDetails.addAll(workFlowList);
                setMode(MODIFY);
                setModifyToDate(new Date());
                setFromDate(new Date());
                this.workFlowMatrixRejectDetails.addAll(rejectionList);
                LOGGER.info("prepareModifyForm Method is ended");
                return NEW;
        }

        /**
         * This method is called to delete the workflow matrix the selected objecttype,department,date,from and to amount
         * @return
         */
        public String deleteWorkFlowMatrix() {

                LOGGER.info("deleteWorkFlowMatrix Method is called");
                final HashMap workflowsearchparams = getSearchParams();
                deleteWorkFlowMatrixObject(workflowsearchparams);
                final HashMap workflowheaderparams = getHeaderParams();
                getWorkFlowMatrixObject(workflowheaderparams);
                setMode(RESULTS);
                addActionMessage("The Matrix was successfully deleted");
                LOGGER.info("deleteWorkFlowMatrix Method is ended");
                return "search";

        }

        /**
         * This method is called to modify the existing workflow matrix(To date is set)during the modify.
         * @return
         */
        private Boolean updateExistingMatrix() {

                final HashMap workflowheaderparams = getSearchParams();
                return this.workFlowMatrixService.updateWorkFlowforObject(workflowheaderparams);

        }

        /**
         * This method is called to get all the parameters entered from the create and search screens
         * @return
         */
        private HashMap getHeaderParams() {
                LOGGER.info("getHeaderParams Method is called");
                final HashMap workflowheaderparams = new HashMap();
                workflowheaderparams.put(OBJECTTYPE, getObjectType());
                workflowheaderparams.put(ADDITIONALRULE, getAdditionalRule());
                workflowheaderparams.put(FROMDATE, getFromDate());
                workflowheaderparams.put(TODATE, getToDate());
                workflowheaderparams.put(FROMAMOUNT, getFromAmount());
                workflowheaderparams.put(TOAMOUNT, getToAmount());
                workflowheaderparams.put(DEPARTMENTS, getDepartment());
                workflowheaderparams.put(MODIFYDATE, getModifyToDate());
                LOGGER.info("getHeaderParams Method is ended");
                return workflowheaderparams;
        }

        /**
         * This method is called to get all the parameters entered from the search screens which are passed as hidden values
         * @return
         */
        private HashMap getSearchParams() {
                LOGGER.info("getSearchParams Method is called");
                final HashMap workflowsearchparams = new HashMap();
                workflowsearchparams.put(OBJECTTYPE, getObjectTypeView());
                workflowsearchparams.put(ADDITIONALRULE, getAdditionalRuleView());
                workflowsearchparams.put(FROMDATE, getFromDateView());
                workflowsearchparams.put(TODATE, getToDateView());
                workflowsearchparams.put(FROMAMOUNT, getFromQtyView());
                workflowsearchparams.put(TOAMOUNT, getToQtyView());
                workflowsearchparams.put(DEPARTMENTS, getDepartment());
                workflowsearchparams.put(MODIFYDATE, getModifyToDate());
                LOGGER.info("getSearchParams Method is ended");
                return workflowsearchparams;
        }

        public String getSearchAction() {
                return this.searchAction;
        }

        public void setSearchAction(final String searchAction) {
                this.searchAction = searchAction;
        }

        public String getObjectTypeView() {
                return this.objectTypeView;
        }

        public void setObjectTypeView(final String objectTypeView) {
                this.objectTypeView = objectTypeView;
        }

        public Date getFromDateView() {
                return this.fromDateView;
        }

        public void setFromDateView(final Date fromDateView) {
                this.fromDateView = fromDateView;
        }

        public Date getToDateView() {
                return this.toDateView;
        }

        public void setToDateView(final Date toDateView) {
                this.toDateView = toDateView;
        }

        public String getAdditionalRuleView() {
                return this.additionalRuleView;
        }

        public void setAdditionalRuleView(final String additionalRuleView) {
                this.additionalRuleView = additionalRuleView;
        }

        public List<WorkFlowMatrixDetails> getViewList() {
                return this.viewList;
        }

        public void setViewList(final List<WorkFlowMatrixDetails> viewList) {
                this.viewList = viewList;
        }

        public List getButtonList() {
                return this.buttonList;
        }

        public void setButtonList(final List buttonList) {
                this.buttonList = buttonList;
        }

        public List getStatusList() {
                return this.statusList;
        }

        public void setStatusList(final List statusList) {
                this.statusList = statusList;
        }

        public List getStateList() {
                return this.stateList;
        }

        public void setStateList(final List stateList) {
                this.stateList = stateList;
        }

        public List<Designation> getDesignationList() {
                return this.designationList;
        }

        public void setDesignationList(final List<Designation> designationList) {
                this.designationList = designationList;
        }

        public List<WorkFlowMatrixDetails> getWorkFlowMatrixDetails() {
                return this.workFlowMatrixDetails;
        }

        public void setWorkFlowMatrixDetails(final List<WorkFlowMatrixDetails> workFlowMatrixDetails) {
                this.workFlowMatrixDetails = workFlowMatrixDetails;
        }

        public List getAdditionalRuleList() {
                return this.additionalRuleList;
        }

        public void setAdditionalRuleList(final List additionalRuleList) {
                this.additionalRuleList = additionalRuleList;
        }

        public Long getObjectTypeid() {
                return this.objectTypeid;
        }

        public void setObjectTypeid(final Long objectTypeid) {
                this.objectTypeid = objectTypeid;
        }

        public String[] getDepartmentSelected() {
                return this.departmentSelected;
        }

        public void setDepartmentSelected(final String[] departmentSelected) {
                this.departmentSelected = departmentSelected;
        }

        public String getAdditionalRuleSelected() {
                return this.additionalRuleSelected;
        }

        public void setAdditionalRuleSelected(final String additionalRuleSelected) {
                this.additionalRuleSelected = additionalRuleSelected;
        }

        public WorkFlowMatrixService getWorkFlowMatrixService() {
                return this.workFlowMatrixService;
        }

        public void setWorkFlowMatrixService(final WorkFlowMatrixService workFlowMatrixService) {
                this.workFlowMatrixService = workFlowMatrixService;
        }

        public String[] getDepartment() {
                return this.department;
        }

        public void setDepartment(final String[] department) {
                this.department = department;
        }

        public String getAdditionalRule() {
                return this.additionalRule;
        }

        public void setAdditionalRule(final String additionalRule) {
                this.additionalRule = additionalRule;
        }

        public Long getObjectType() {
                return this.objectType;
        }

        public void setObjectType(final Long objectType) {
                this.objectType = objectType;
        }

        public Boolean getAmountRule() {
                return this.amountRule;
        }

        public void setAmountRule(final Boolean amountRule) {
                this.amountRule = amountRule;
        }

        public Date getFromDate() {
                return this.fromDate;
        }

        public void setFromDate(final Date fromDate) {
                this.fromDate = fromDate;
        }

        public Date getToDate() {
                return this.toDate;
        }

        public void setToDate(final Date toDate) {
                this.toDate = toDate;
        }

        public BigDecimal getFromAmount() {
                return this.fromAmount;
        }

        public void setFromAmount(final BigDecimal fromAmount) {
                this.fromAmount = fromAmount;
        }

        public BigDecimal getToAmount() {
                return this.toAmount;
        }

        public void setToAmount(final BigDecimal toAmount) {
                this.toAmount = toAmount;
        }

        public String getMode() {
                return this.mode;
        }

        public void setMode(final String mode) {
                this.mode = mode;
        }

        public WorkFlowMatrix getWorkFlowMatrix() {
                return this.workFlowMatrix;
        }

        public void setWorkFlowMatrix(final WorkFlowMatrix workFlowMatrix) {
                this.workFlowMatrix = workFlowMatrix;
        }

        public BigDecimal getFromQtyView() {
                return this.fromQtyView;
        }

        public void setFromQtyView(final BigDecimal fromQtyView) {
                this.fromQtyView = fromQtyView;
        }

        public BigDecimal getToQtyView() {
                return this.toQtyView;
        }

        public void setToQtyView(final BigDecimal toQtyView) {
                this.toQtyView = toQtyView;
        }

        public Date getModifyToDate() {
                return this.modifyToDate;
        }

        public void setModifyToDate(final Date modifyToDate) {
                this.modifyToDate = modifyToDate;
        }

        public Date getLegacyDate() {
                return this.legacyDate;
        }

        public void setLegacyDate(final Date legacyDate) {
                this.legacyDate = legacyDate;
        }

        public WorkFlowMatrixDetails getRejectionDetail() {
                return this.rejectionDetail;
        }

        public void setRejectionDetail(final WorkFlowMatrixDetails rejectionDetail) {
                this.rejectionDetail = rejectionDetail;
        }

        public String getDepartmentstring() {
                return this.departmentstring;
        }

        public void setDepartmentstring(final String departmentstring) {
                this.departmentstring = departmentstring;
        }

        public List<WorkFlowMatrixDetails> getWorkFlowMatrixRejectDetails() {
                return this.workFlowMatrixRejectDetails;
        }

        public void setWorkFlowMatrixRejectDetails(final List<WorkFlowMatrixDetails> workFlowMatrixRejectDetails) {
                this.workFlowMatrixRejectDetails = workFlowMatrixRejectDetails;
        }
}