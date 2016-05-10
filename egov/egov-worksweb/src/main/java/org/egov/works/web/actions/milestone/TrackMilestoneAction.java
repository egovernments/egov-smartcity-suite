/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.actions.milestone;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.entity.MilestoneActivity;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.entity.TrackMilestoneActivity;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@ParentPackage("egov")
@Result(name = TrackMilestoneAction.NEW, location = "trackMilestone-new.jsp")
public class TrackMilestoneAction extends BaseFormAction {

    private static final long serialVersionUID = -3165064901699566106L;
    private TrackMilestone trackMilestone = new TrackMilestone();
    private PersistenceService<TrackMilestone, Long> trackMilestoneService;
    private Long id;
    private Long stateValue;
    private Long woEstimateId;
    private String mode = "";
    private WorkflowService<TrackMilestone> trackMilestoneWorkflowService;
    private static final String SAVE_ACTION = "save";
    private String messageKey;
    private static final String TRACK_MILESTONE_MODULE_KEY = "TrackMilestone";
    @Autowired
    private UserService userService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    private String actionName;
    private String sourcepage;
    private String nextEmployeeName;
    private String nextDesignation;
    private String designation;
    private WorksService worksService;
    private List<TrackMilestoneActivity> trackMilestoneActivities = new LinkedList<TrackMilestoneActivity>();
    private static final String SOURCE_INBOX = "inbox";
    private static final String MODE_MODIFY = "modify";
    private Long milestoneId;

    public TrackMilestoneAction() {
        addRelatedEntity("milestone", Milestone.class);
        addRelatedEntity("milestoneActivity", MilestoneActivity.class);
    }

    @Override
    public void prepare() {

        if (id != null)
            trackMilestone = trackMilestoneService.findById(id, false);

        if (woEstimateId != null)
            if (getTrackedMilestone(woEstimateId) == null || mode.equalsIgnoreCase("view")) {
                final List<TrackMilestoneActivity> trackMilestoneActivityList = new ArrayList<TrackMilestoneActivity>();
                Milestone milestone;
                if (milestoneId != null && mode.equalsIgnoreCase("view"))
                    milestone = (Milestone) getPersistenceService()
                            .find("from Milestone m where m.id= ? ", milestoneId);
                else
                    milestone = (Milestone) getPersistenceService()
                            .find("from Milestone m where m.id=(select max(m1.id) from Milestone m1 where m1.workOrderEstimate.id=?)",
                                    woEstimateId);

                if (mode.equalsIgnoreCase("view") && milestone.getTrackMilestone() != null
                        && milestone.getTrackMilestone().size() != 0) {
                    TrackMilestone tempTrackMilestone;
                    if (milestoneId != null)
                        tempTrackMilestone = (TrackMilestone) getPersistenceService().find(
                                "from TrackMilestone tm where tm.milestone.id = ? )", milestoneId);
                    else
                        tempTrackMilestone = (TrackMilestone) getPersistenceService()
                                .find("from TrackMilestone tm where tm.egwStatus.code<>? and tm.id=(select max(tm1.id) from TrackMilestone tm1 where tm1.milestone.id=?)",
                                        "NEW", milestone.getId());
                    if (tempTrackMilestone != null) {
                        trackMilestone = tempTrackMilestone;
                        stateValue = trackMilestone.getState().getId();
                    } else
                        trackMilestone.setMilestone(milestone);
                } else
                    trackMilestone.setMilestone(milestone);

                if (trackMilestone.getMilestone() != null && trackMilestone.getId() == null) {
                    for (final MilestoneActivity milestoneActivity : trackMilestone.getMilestone().getActivities()) {
                        final TrackMilestoneActivity trackMilestoneActivity = new TrackMilestoneActivity();
                        trackMilestoneActivity.setMilestoneActivity(milestoneActivity);
                        trackMilestoneActivityList.add(trackMilestoneActivity);
                    }
                    trackMilestone.setActivities(trackMilestoneActivityList);
                    if (mode.equalsIgnoreCase("view"))
                        stateValue = milestone.getState().getId();
                }

            } else {
                trackMilestone = getTrackedMilestone(woEstimateId);
                mode = "modify";
            }
        super.prepare();
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        setupDropdownDataExcluding("milestone", "milestoneActivity");
        addDropdownData("executingDepartmentList",
                getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)"));
    }

    @Override
    public Object getModel() {

        return trackMilestone;
    }

    @SkipValidation
    @Action(value = "/milestone/trackMilestone-newform")
    public String newform() {
        if (trackMilestone.getStatus() != null && trackMilestone.getStatus().getCode().equals(WorksConstants.NEW)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(trackMilestone, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        }
        return NEW;
    }

    @SkipValidation
    public String view() {

        return NEW;
    }

    public String save() {
        final String actionName = parameters.get("actionName")[0];

        if (id == null)
            trackMilestone.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(TRACK_MILESTONE_MODULE_KEY, "NEW"));

        if (mode.equalsIgnoreCase("modify") && trackMilestone.getStatus().getCode().equalsIgnoreCase("APPROVED"))
            trackMilestone.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(TRACK_MILESTONE_MODULE_KEY, "NEW"));
        /*
         * TODO - check for application for commenting out this line for any issues
         */
        // trackMilestone.setState(null);
        if (trackMilestone.getIsProjectCompleted() == null)
            trackMilestone.setIsProjectCompleted(Boolean.FALSE);
        trackMilestone = trackMilestoneService.persist(trackMilestone);
        trackMilestoneWorkflowService.transition(actionName, trackMilestone,
                trackMilestone.getApprovalComent());
        trackMilestone = trackMilestoneService.persist(trackMilestone);
        messageKey = "trackMilestone." + actionName;
        addActionMessage("Estimate - "
                + trackMilestone.getMilestone().getWorkOrderEstimate().getEstimate().getEstimateNumber());
        addActionMessage(getText(messageKey));
        getDesignation(trackMilestone);
        mode = "";

        if (SAVE_ACTION.equals(actionName))
            sourcepage = "inbox";

        return SAVE_ACTION.equals(actionName) ? EDIT : SUCCESS;

    }

    public String cancel() {
        if (trackMilestone.getId() != null) {
            trackMilestoneWorkflowService.transition(TrackMilestone.Actions.CANCEL.toString(), trackMilestone,
                    trackMilestone.getApprovalComent());
            trackMilestone = trackMilestoneService.persist(trackMilestone);
        }
        messageKey = "trackMilestone.cancel";
        getDesignation(trackMilestone);
        return SUCCESS;
    }

    public String reject() {
        trackMilestoneWorkflowService.transition(TrackMilestone.Actions.REJECT.toString(), trackMilestone,
                trackMilestone.getApprovalComent());
        trackMilestone = trackMilestoneService.persist(trackMilestone);
        messageKey = "trackMilestone.reject";
        getDesignation(trackMilestone);
        return SUCCESS;
    }

    private TrackMilestone getTrackedMilestone(final Long woEstimateId) {

        final TrackMilestone trackMilestone = (TrackMilestone) getPersistenceService().find(
                "from TrackMilestone tm where tm.milestone.workOrderEstimate.id=? and tm.egwStatus.code<>?  ",
                woEstimateId, "CANCELLED");
        return trackMilestone;
    }

    public void getDesignation(final TrackMilestone trackMilestone) {
        if (trackMilestone.getStatus() != null
                && !WorksConstants.NEW.equalsIgnoreCase(trackMilestone.getStatus().getCode())) {
            final String result = worksService.getEmpNameDesignation(trackMilestone.getState().getOwnerPosition(),
                    trackMilestone.getState().getCreatedDate());
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
        if ((SOURCE_INBOX.equalsIgnoreCase(sourcepage) || MODE_MODIFY.equalsIgnoreCase(mode))
                && trackMilestone.getStatus() != null
                && !trackMilestone.getStatus().getCode()
                        .equals(TrackMilestone.TrackMilestoneStatus.APPROVED.toString())
                && !trackMilestone.getStatus().getCode()
                        .equals(TrackMilestone.TrackMilestoneStatus.CANCELLED.toString())
                || trackMilestone.getStatus() != null
                        && trackMilestone.getStatus().getCode().equals(WorksConstants.NEW)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(trackMilestone, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        return EDIT;
    }

    @SkipValidation
    public String search() {

        return "search";
    }

    @SkipValidation
    public String workflowHistory() {
        return "history";
    }

    @Override
    public void validate() {
        populateActivities();

        if (null == trackMilestone.getActivities() || trackMilestone.getActivities().size() == 0)
            addFieldError("milestone.activity.missing", "Milestone Activity is not added");
        BigDecimal percentage = BigDecimal.ZERO;
        for (final TrackMilestoneActivity trackmilestoneActivity : trackMilestone.getActivities()) {
            if (trackmilestoneActivity.getCompletedPercentage() != null)
                percentage = trackmilestoneActivity.getCompletedPercentage();
            if (percentage.compareTo(BigDecimal.valueOf(100)) == 1) {
                addFieldError("milestone.activity.total.percentage",
                        "Total activity percentage should not be greater than 100%");
                break;

            }
        }
    }

    public void populateActivities() {
        trackMilestone.getActivities().clear();

        for (final TrackMilestoneActivity activity : trackMilestoneActivities)
            if (activity != null) {
                final MilestoneActivity milestoneActivity = (MilestoneActivity) getPersistenceService().find(
                        "from MilestoneActivity where id=?", activity.getMilestoneActivity().getId());
                activity.setMilestoneActivity(milestoneActivity);
                trackMilestone.addActivity(activity);
            }
    }

    public TrackMilestone getTrackMilestone() {
        return trackMilestone;
    }

    public void setTrackMilestone(final TrackMilestone trackMilestone) {
        this.trackMilestone = trackMilestone;
    }

    public PersistenceService<TrackMilestone, Long> getTrackMilestoneService() {
        return trackMilestoneService;
    }

    public void setTrackMilestoneService(final PersistenceService<TrackMilestone, Long> trackMilestoneService) {
        this.trackMilestoneService = trackMilestoneService;
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
        return trackMilestoneWorkflowService.getValidActions(trackMilestone);
    }

    public void setTrackMilestoneWorkflowService(final WorkflowService<TrackMilestone> trackMilestoneWorkflowService) {
        this.trackMilestoneWorkflowService = trackMilestoneWorkflowService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public List<TrackMilestoneActivity> getTrackMilestoneActivities() {
        return trackMilestoneActivities;
    }

    public void setTrackMilestoneActivities(final List<TrackMilestoneActivity> trackMilestoneActivities) {
        this.trackMilestoneActivities = trackMilestoneActivities;
    }

    public Long getWoEstimateId() {
        return woEstimateId;
    }

    public void setWoEstimateId(final Long woEstimateId) {
        this.woEstimateId = woEstimateId;
    }

    public Long getStateValue() {
        return stateValue;
    }

    public void setStateValue(final Long stateValue) {
        this.stateValue = stateValue;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public Long getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(final Long milestoneId) {
        this.milestoneId = milestoneId;
    }
}
