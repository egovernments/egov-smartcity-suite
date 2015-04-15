/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.web.actions.workflow;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.infra.workflow.inbox.InboxRenderServiceDeligate.SLASH_DELIMIT;
import static org.egov.infstr.utils.DateUtils.getFormattedDate;
import static org.egov.infstr.utils.StringUtils.escapeSpecialChars;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.egov.infstr.annotation.Search;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.workflow.admin.WorkflowAdminService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.lib.rjbac.dept.dao.DepartmentDAO;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;

import com.opensymphony.xwork2.ActionSupport;

/**
 * The Class WorkflowAdminAction. Action to handle all workflow administration related operation. 
 * Supporting Adminstartive task is listed below 
 * [1] Reassignment of Workflow Item.
 */
@ParentPackage("egov")
public class WorkflowAdminAction extends ActionSupport {

        private static final long serialVersionUID = 1L;

        private static final String WF_TYPE = "wfType";

        private transient WorkflowAdminService workflowAdmin;

        private transient InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate;

        private transient DepartmentDAO departmentDao;

        private transient EISServeable eisService;

        private transient String query;

        private transient final Map<String, Object> criteria = new HashMap<String, Object>();

        private transient String stateId;

        private transient Long newOwner;

        private transient Integer deptId;

        private transient Integer desigId;

        /**
         * Initiate the Workflow Administrative task.
         * @return the string
         */
        @Override
        public String execute() {
                return SUCCESS;
        }

        /**
         * Gets the department list.
         * @return the department list
         */
        public List<Department> getDepartmentList() {
                return this.departmentDao.getAllDepartments();
        }

        /**
         * Populate designation.
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public void populateDesignation() throws IOException {
                final List<DesignationMaster> designations = this.eisService.getAllDesignationByDept(this.deptId, new Date());
                if ((designations == null) || designations.isEmpty()) {
                        ServletActionContext.getResponse().getWriter().write("ERROR");
                } else {
                        final StringBuilder desigStr = new StringBuilder("[");
                        for (final DesignationMaster designation : designations) {
                                desigStr.append("{name : '").append(designation.getDesignationName()).append("',id:").append(designation.getDesignationId()).append("},");
                        }
                        desigStr.deleteCharAt(desigStr.length() - 1);
                        desigStr.append("]");
                        ServletActionContext.getResponse().getWriter().write(desigStr.toString());
                }
        }

        /**
         * Populate designation.
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public void populateUsersByDeptAndDesig() throws IOException {
                final List<User> users = this.eisService.getUsersByDeptAndDesig(this.deptId, this.desigId, new Date());
                if ((users == null) || users.isEmpty()) {
                        ServletActionContext.getResponse().getWriter().write("ERROR");
                } else {
                        final StringBuilder userStr = new StringBuilder("[");
                        for (final User user : users) {
                                userStr.append("{name : '").append(user.getUsername()).append(" / ").append(user.getName()).append("',id:").append(user.getId()).append("},");
                        }
                        userStr.deleteCharAt(userStr.length() - 1);
                        userStr.append("]");
                        ServletActionContext.getResponse().getWriter().write(userStr.toString());
                }
        }

        /**
         * Populating Auto-complete for Workflow Type.
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public void populateDocumentType() throws IOException {
                final List<WorkflowTypes> workflowTypes = this.workflowAdmin.getWorkflowTypes(this.query);
                final StringBuilder docTypes = new StringBuilder(EMPTY);
                for (final WorkflowTypes workflowType : workflowTypes) {
                        docTypes.append(workflowType.getDisplayName()).append(SLASH_DELIMIT).append(workflowType.getType()).append("\n");
                }
                ServletActionContext.getResponse().getWriter().write(docTypes.toString());
        }

        /**
         * Populate user.
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public void populateUser() throws IOException {
                final Set<User> users = this.workflowAdmin.getAllUserByUserName(this.query);
                final StringBuilder userAndPos = new StringBuilder(EMPTY);
                for (final User user : users) {
                        userAndPos.append(user.getUsername()).append(SLASH_DELIMIT).append(user.getName()).append(" - ").append(user.getId()).append("\n");
                }
                ServletActionContext.getResponse().getWriter().write(userAndPos.toString());
        }

        /**
         * Populate workflow state.
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public void populateWFItemSearchFields() throws IOException, IntrospectionException, ClassNotFoundException {
                final List<String> searchFields = this.getWFItemSearchFields(this.criteria.get(WF_TYPE).toString());
                final StringBuilder wfStates = new StringBuilder(EMPTY);
                for (final String searchField : searchFields) {
                        wfStates.append(searchField).append("\n");
                }
                ServletActionContext.getResponse().getWriter().write(wfStates.toString());
        }

        /**
         * Populate workflow state.
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public void populateWorkflowState() throws IOException {
                final List<String> workflowStates = this.workflowAdmin.getWorkflowStateValues(this.criteria.get(WF_TYPE).toString());
                final StringBuilder wfStates = new StringBuilder(EMPTY);
                for (final String workflowState : workflowStates) {
                        wfStates.append(workflowState).append("\n");
                }
                ServletActionContext.getResponse().getWriter().write(wfStates.toString());
        }

        /**
         * Reassign wf item.
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public void reassignWFItem() throws IOException {
                String status = "";
                for (final String stateID : this.stateId.split(",")) {
                        status = this.workflowAdmin.reassignWFItem(this.criteria.get(WF_TYPE).toString(), stateID, this.newOwner);
                        if (status.equals("ERROR") || status.equals("OWNER-SAME")) {
                                break;
                        }
                }
                ServletActionContext.getResponse().getWriter().write(status);
        }

        /**
         * JSON String creation of Workflow item data.
         * @param states the states
         * @param workflowTypes the workflow types
         * @return the string builder
         * @throws EGOVRuntimeException the eGOV runtime exception
         */
        private StringBuilder renderWFItems(final List<StateAware> states, final WorkflowTypes workflowTypes) throws EGOVRuntimeException {
                final StringBuilder wfItem = new StringBuilder("");
                if ((states != null) && !states.isEmpty()) {
                        wfItem.append("[");
                        for (final StateAware stateAware : states) {
                                final State state = stateAware.getCurrentState();
                                final Position sender = this.inboxRenderServiceDeligate.getStateUserPosition(state);
                                final Position owner = state.getOwnerPosition();
                                final User userSender = this.inboxRenderServiceDeligate.getStateUser(state, sender);
                                final User userOwner = this.inboxRenderServiceDeligate.getStateUser(state, owner);
                                wfItem.append("{Id:'").append(stateAware.myLinkId()).append("#DILIM#").append(workflowTypes.getId()).append("',");
                                wfItem.append("Date:'").append(getFormattedDate(state.getCreatedDate().toDate(), "dd/MM/yyyy hh:mm a")).append("',");
                                wfItem.append("Sender:'").append(this.inboxRenderServiceDeligate.prettyPrintSenderName(sender, userSender)).append("',");
                                wfItem.append("Owner:'").append(this.inboxRenderServiceDeligate.prettyPrintSenderName(owner, userOwner)).append("',");
                                wfItem.append("Task:'").append(workflowTypes.getDisplayName()).append("',");
                                wfItem.append("Status:'").append(state.getValue()).append("',");
                                wfItem.append("Details:'").append(stateAware.getStateDetails() == null ? EMPTY : escapeSpecialChars(stateAware.getStateDetails())).append("',");
                                wfItem.append("Link:'").append(workflowTypes.getLink().replace(":ID", stateAware.myLinkId())).append("'},");
                        }
                        wfItem.deleteCharAt(wfItem.length() - 1);
                        wfItem.append("]");
                }
                return wfItem;
        }

        /**
         * Search wf items.
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public void searchWfItems() throws IOException {
                try {
                        final String wfType = this.criteria.get(WF_TYPE).toString();
                        if ((wfType == null) || wfType.isEmpty()) {
                                ServletActionContext.getResponse().getWriter().write("error");
                                return;
                        }
                        final List<StateAware> wfStates = this.inboxRenderServiceDeligate.getWorkflowItems(this.criteria);
                        final WorkflowTypes workflowTypes = this.inboxRenderServiceDeligate.getWorkflowType(wfType);
                        ServletActionContext.getResponse().getWriter().write(this.renderWFItems(wfStates, workflowTypes).toString());
                } catch (final RuntimeException e) {
                        LOG.error("Error occurred in Workflow Admin search", e);
                        ServletActionContext.getResponse().getWriter().write("error");
                }
        }

        /**
         * Gets the workflow state values.
         * @param wfType the wf type
         * @return the workflow state values
         */
        private List<String> getWFItemSearchFields(final String wfType) throws IntrospectionException, ClassNotFoundException {
                final String wfItemClassName = this.inboxRenderServiceDeligate.getWorkflowType(wfType).getTypeFQN();
                final BeanInfo beanInfo = Introspector.getBeanInfo(Thread.currentThread().getContextClassLoader().loadClass(wfItemClassName));
                final PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
                final List<String> searchFields = new ArrayList<String>();
                for (final PropertyDescriptor propDesc : props) {
                        final Method field = propDesc.getReadMethod();
                        if (field.isAnnotationPresent(Search.class)) {
                                final Search search = field.getAnnotation(Search.class);
                                searchFields.add(getText(wfItemClassName + "." + propDesc.getName()) + "-" + search.searchOp() + "*" + propDesc.getName() + "*" + getText("Search.Operator." + search.searchOp()) + "*" + propDesc.getReadMethod().getReturnType().getSimpleName());
                        }
                }
                return searchFields;
        }

        /**
         * Sets the department dao.
         * @param departmentDao the new department dao
         */
        public void setDepartmentDao(final DepartmentDAO departmentDao) {
                this.departmentDao = departmentDao;
        }

        /**
         * Sets the dept id.
         * @param deptId the new dept id
         */
        public void setdeptId(final Integer deptId) {
                this.deptId = deptId;
        }

        /**
         * Sets the eis service.
         * @param eisService the new eis service
         */
        public void setEisService(final EISServeable eisService) {
                this.eisService = eisService;
        }

        /**
         * Sets the from date.
         * @param fromDate the new from date
         */
        public void setFromDate(final Date fromDate) {
                this.criteria.put("fromDate", fromDate);
        }

        /**
         * Sets the inbox service.
         * @param inboxService the new inbox service
         */
        public void setInboxRenderServiceDeligate(final InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate) {
                this.inboxRenderServiceDeligate = inboxRenderServiceDeligate;
                this.setInboxServiceInWorkflowService();
        }

        /**
         * Sets the inbox service in workflow service.
         */
        public void setInboxServiceInWorkflowService() {
                if ((this.workflowAdmin != null) && (this.inboxRenderServiceDeligate != null)) {
                        this.workflowAdmin.setInboxRenderServiceDeligate(this.inboxRenderServiceDeligate);
                }
        }

        /**
         * Sets the new owner.
         * @param newOwner the new new owner
         */
        public void setNewOwner(final Long newOwner) {
                this.newOwner = newOwner;
        }

        /**
         * Sets the owner.
         * @param owner the new owner
         */
        public void setOwner(final Long owner) {
                //this.criteria.put("owner", this.inboxRenderServiceDeligate.getPositionForUser(owner, new Date()));
        }

        /**
         * Sets the query.
         * @param query the new query
         */
        public void setQuery(final String query) {
                this.query = query;
        }

        /**
         * Sets the sender.
         * @param sender the new sender
         */
        public void setSender(final Long sender) {
                //this.criteria.put("sender", this.inboxRenderServiceDeligate.getPositionForUser(sender, new Date()));
        }

        /**
         * Sets the state id.
         * @param stateId the new state id
         */
        public void setStateId(final String stateId) {
                this.stateId = stateId;
        }

        /**
         * Sets the to date.
         * @param toDate the new to date
         */
        public void setToDate(final Date toDate) {
                this.criteria.put("toDate", toDate);
        }

        /**
         * Sets the wf state.
         * @param wfState the new wf state
         */
        public void setWfState(final String wfState) {
                this.criteria.put("wfState", wfState);
        }

        /**
         * Sets the wf type.
         * @param wfType the new wf type
         */
        public void setWfType(final String wfType) {
                this.criteria.put(WF_TYPE, wfType);
        }

        /**
         * Sets the workflow admin.
         * @param workflowAdmin the new workflow admin
         */
        public void setWorkflowAdmin(final WorkflowAdminService workflowAdmin) {
                this.workflowAdmin = workflowAdmin;
                this.setInboxServiceInWorkflowService();
        }

        public void setDesigId(final Integer desigId) {
                this.desigId = desigId;
        }

        /**
         * @param searchOp the searchOp to set
         */
        public void setSearchOp(final String searchOp) {
                this.criteria.put("searchOp", searchOp);
        }

        /**
         * @param searchField the searchField to set
         */
        public void setSearchField(final String searchField) {
                this.criteria.put("searchField", searchField);
        }

        /**
         * @param identifier the identifier to set
         */
        public void setIdentifier(final String identifier) {
                this.criteria.put("identifier", identifier);
        }
}
