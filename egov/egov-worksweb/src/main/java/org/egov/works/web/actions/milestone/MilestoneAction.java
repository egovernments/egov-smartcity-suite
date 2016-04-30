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
package org.egov.works.web.actions.milestone;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.milestone.Milestone;
import org.egov.works.models.milestone.MilestoneActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@ParentPackage("egov")
@Result(name = MilestoneAction.NEW, location = "milestone-new.jsp")
public class MilestoneAction extends BaseFormAction {

    private static final long serialVersionUID = -415095644985621213L;
    private Milestone milestone = new Milestone();
    private PersistenceService<Milestone, Long> milestoneService;
    private Long id;
    private Long woEstimateId;
    private String mode = "";
    private WorkflowService<Milestone> milestoneWorkflowService;
    private static final String SAVE_ACTION = "save";
    private String messageKey;
    private static final String MILESTONE_MODULE_KEY = "Milestone";
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private UserService userService;
    private String actionName;
    private String sourcepage;
    private String nextEmployeeName;
    private String nextDesignation;
    private String designation;
    private WorksService worksService;
    private List<MilestoneActivity> milestoneActivities = new LinkedList<MilestoneActivity>();
    private static final String SOURCE_INBOX = "inbox";

    public MilestoneAction() {
        addRelatedEntity("workOrderEstimate", WorkOrderEstimate.class);
        addRelatedEntity("workType", EgwTypeOfWork.class);
        addRelatedEntity("subType", EgwTypeOfWork.class);
    }

    @Override
    public void prepare() {

        if (id != null)
            milestone = milestoneService.findById(id, false);

        if (woEstimateId != null)
            milestone.setWorkOrderEstimate((WorkOrderEstimate) getPersistenceService().find(
                    "from WorkOrderEstimate where id=?", woEstimateId));
        super.prepare();
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        setupDropdownDataExcluding("workType", "subType", "workOrderEstimate");
        addDropdownData("parentCategoryList",
                getPersistenceService().findAllBy("from EgwTypeOfWork etw where etw.parentid is null"));
        addDropdownData("categoryList", Collections.emptyList());
        addDropdownData("executingDepartmentList",
                getPersistenceService().findAllBy("from Department order by upper(name)"));
    }

    @Override
    public Object getModel() {

        return milestone;
    }

    @SkipValidation
    @Action(value = "/milestone/milestone-newform")
    public String newform() {

        return "new";
    }

    public String save() {
        final String actionName = parameters.get("actionName")[0];

        if (id == null)
            milestone.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(MILESTONE_MODULE_KEY, "NEW"));

        milestone = milestoneService.persist(milestone);
        milestoneWorkflowService.transition(actionName, milestone, milestone.getWorkflowapproverComments());
        milestone = milestoneService.persist(milestone);
        messageKey = "milestone." + actionName;
        addActionMessage(getText(messageKey, "The Milestone was saved successfully"));
        getDesignation(milestone);

        if (SAVE_ACTION.equals(actionName))
            sourcepage = "inbox";

        return SAVE_ACTION.equals(actionName) ? EDIT : SUCCESS;

    }

    public String cancel() {
        if (milestone.getId() != null) {
            milestoneWorkflowService.transition(Milestone.Actions.CANCEL.toString(), milestone,
                    milestone.getWorkflowapproverComments());
            milestone = milestoneService.persist(milestone);
        }
        messageKey = "milestone.cancel";
        getDesignation(milestone);
        return SUCCESS;
    }

    public String reject() {
        milestoneWorkflowService.transition(Milestone.Actions.REJECT.toString(), milestone,
                milestone.getWorkflowapproverComments());
        milestone = milestoneService.persist(milestone);
        messageKey = "milestone.reject";
        getDesignation(milestone);
        return SUCCESS;
    }

    public void getDesignation(final Milestone milestone) {
        if (milestone.getEgwStatus() != null
                && !WorksConstants.NEW.equalsIgnoreCase(milestone.getEgwStatus().getCode())) {
            final String result = worksService.getEmpNameDesignation(milestone.getState().getOwnerPosition(), milestone
                    .getState().getCreatedDate());
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setNextEmployeeName(empName);
                setNextDesignation(designation);
            }
        }
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(final String actionName) {
        this.actionName = actionName;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(final String messageKey) {
        this.messageKey = messageKey;
    }

    public String getSourcepage() {
        return sourcepage;
    }

    public void setSourcepage(final String sourcepage) {
        this.sourcepage = sourcepage;
    }

    public String getNextEmployeeName() {
        return nextEmployeeName;
    }

    public void setNextEmployeeName(final String nextEmployeeName) {
        this.nextEmployeeName = nextEmployeeName;
    }

    public String getNextDesignation() {
        return nextDesignation;
    }

    public void setNextDesignation(final String nextDesignation) {
        this.nextDesignation = nextDesignation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(final String designation) {
        this.designation = designation;
    }

    @SkipValidation
    public String edit() {
        if (SOURCE_INBOX.equalsIgnoreCase(sourcepage) || milestone.getEgwStatus() != null
                && milestone.getEgwStatus().getCode().equals(WorksConstants.NEW)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(milestone, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        return "edit";
    }

    @SkipValidation
    public String search() {

        return "search";
    }

    @Override
    public void validate() {
        populateActivities();

        if (null == milestone.getActivities() || milestone.getActivities().size() == 0)
            addFieldError("milestone.activity.missing", "Milestone Activity is not added");
        BigDecimal percentage = BigDecimal.ZERO;
        for (final MilestoneActivity milestoneActivity : milestone.getActivities())
            if (milestoneActivity.getPercentage() != null)
                percentage = percentage.add(milestoneActivity.getPercentage());
        if (percentage.compareTo(BigDecimal.valueOf(100)) != 0)
            addFieldError("milestone.activity.total.percentage", "Total activity percentage should be equal to 100%");
    }

    public void populateActivities() {
        milestone.getActivities().clear();

        for (final MilestoneActivity activity : milestoneActivities)
            if (activity != null)
                milestone.addActivity(activity);
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(final Milestone milestone) {
        this.milestone = milestone;
    }

    public PersistenceService<Milestone, Long> getMilestoneService() {
        return milestoneService;
    }

    public void setMilestoneService(final PersistenceService<Milestone, Long> milestoneService) {
        this.milestoneService = milestoneService;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<WorkflowAction> getValidActions() {
        return milestoneWorkflowService.getValidActions(milestone);
    }

    public void setMilestoneWorkflowService(final WorkflowService<Milestone> milestoneWorkflowService) {
        this.milestoneWorkflowService = milestoneWorkflowService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public List<MilestoneActivity> getMilestoneActivities() {
        return milestoneActivities;
    }

    public void setMilestoneActivities(final List<MilestoneActivity> milestoneActivities) {
        this.milestoneActivities = milestoneActivities;
    }

    public Long getWoEstimateId() {
        return woEstimateId;
    }

    public void setWoEstimateId(final Long woEstimateId) {
        this.woEstimateId = woEstimateId;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

}
