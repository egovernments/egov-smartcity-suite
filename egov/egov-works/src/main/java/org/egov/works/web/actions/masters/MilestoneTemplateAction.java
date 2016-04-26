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
package org.egov.works.web.actions.masters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.MilestoneTemplate;
import org.egov.works.models.masters.MilestoneTemplateActivity;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;

@ParentPackage("egov")
@Results({
        @Result(name = MilestoneTemplateAction.NEW, location = "milestoneTemplate-new.jsp"),
        @Result(name = MilestoneTemplateAction.SEARCH, location = "milestoneTemplate-search.jsp"),
        @Result(name = MilestoneTemplateAction.SUCCESS, location = "milestoneTemplate-success.jsp"),
        @Result(name = MilestoneTemplateAction.EDIT, location = "milestoneTemplate-edit.jsp")

})
public class MilestoneTemplateAction extends SearchFormAction {

    private static final long serialVersionUID = 5517312981738363805L;
    private MilestoneTemplate template = new MilestoneTemplate();
    private PersistenceService<MilestoneTemplate, Long> milestoneTemplateService;
    private Long id;
    private String mode = " ";
    private WorkflowService<MilestoneTemplate> milestoneTemplateWorkflowService;
    private String messageKey;
    private String actionName;
    private String sourcepage;
    private String nextEmployeeName;
    private String nextDesignation;
    private String designation;
    private WorksService worksService;
    private List<MilestoneTemplateActivity> templateActivities = new LinkedList<MilestoneTemplateActivity>();
    public static final String SEARCH = "search";
    public static final String SUCCESS = "success";
    public static final String EDIT = "edit";

    public MilestoneTemplateAction() {
        addRelatedEntity("workType", EgwTypeOfWork.class);
        addRelatedEntity("subType", EgwTypeOfWork.class);
    }

    @Override
    public void prepare() {
        if (id != null)
            template = milestoneTemplateService.findById(id, false);
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        super.prepare();
        setupDropdownDataExcluding("workType", "subType");
        addDropdownData("parentCategoryList",
                getPersistenceService().findAllBy("from EgwTypeOfWork etw where etw.parentid is null"));
        populateCategoryList(ajaxEstimateAction, template.getWorkType() != null);

    }

    @Override
    public Object getModel() {

        return template;
    }

    @Action(value = "/masters/milestoneTemplate-newform")
    @SkipValidation
    public String newform() {
        return NEW;
    }

    @Override
    @SkipValidation
    @Action(value = "/masters/milestoneTemplate-search")
    public String search() {
        return SEARCH;
    }

    @Action(value = "/masters/milestoneTemplate-save")
    public String save() {
        populateActivities();
        template = milestoneTemplateService.persist(template);
        return SUCCESS;
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

    @Action(value = "/masters/milestoneTemplate-edit")
    @SkipValidation
    public String edit() {
        template = milestoneTemplateService.findById(template.getId(), false);
        return EDIT;
    }

    @SkipValidation
    public String searchTemplate() {
        if ("searchForMilestone".equalsIgnoreCase(sourcepage))
            template.setStatus(1);
        return SEARCH;
    }

    @Action(value = "/masters/milestoneTemplate-searchDetails")
    @SkipValidation
    public String searchDetails() {
        if (template.getWorkType() == null || template.getWorkType().getId() == -1) {
            final String messageKey = "milestone.template.search.workType.error";
            addActionError(getText(messageKey));
            return SEARCH;
        }
        setPageSize(WorksConstants.PAGE_SIZE);
        super.search();
        return SEARCH;
    }

    @Override
    public void validate() {
        populateActivities();

        if (null == template.getMilestoneTemplateActivities() || template.getMilestoneTemplateActivities().size() == 0)
            addFieldError("milestone.activity.missing", "Template Activity is not added");
        BigDecimal percentage = BigDecimal.ZERO;
        for (final MilestoneTemplateActivity templateActivities : template.getMilestoneTemplateActivities())
            if (templateActivities.getPercentage() != null)
                percentage = percentage.add(templateActivities.getPercentage());
        if (percentage.compareTo(BigDecimal.valueOf(100)) != 0)
            addFieldError("milestone.activity.total.percentage", "Total activity percentage should be equal to 100%");
    }

    protected void populateCategoryList(
            final AjaxEstimateAction ajaxEstimateAction, final boolean categoryPopulated) {
        if (categoryPopulated) {
            ajaxEstimateAction.setCategory(template.getWorkType().getId());
            ajaxEstimateAction.subcategories();
            addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());
        } else
            addDropdownData("categoryList", Collections.emptyList());
    }

    private void populateActivities() {
        template.getMilestoneTemplateActivities().clear();
        for (final MilestoneTemplateActivity activity : templateActivities) {
            if (activity != null)
                template.addMilestoneTemplateActivity(activity);

            // TODO:Fixme - Setting auditable properties by time being since HibernateEventListener is not getting
            // triggered on update of estimate for child objects
            template.setCreatedBy(worksService.getCurrentLoggedInUser());
            template.setCreatedDate(new Date());

        }
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        String dynQuery = " from MilestoneTemplate mt where mt.id is not null ";
        final List<Object> paramList = new ArrayList<Object>();
        if (template.getWorkType() != null && template.getWorkType().getId() != -1) {
            dynQuery = dynQuery + " and mt.workType.id = ? ";
            paramList.add(template.getWorkType().getId());
        }
        if (template.getSubType() != null && template.getSubType().getId() != -1) {
            dynQuery = dynQuery + " and mt.subType.id = ? ";
            paramList.add(template.getSubType().getId());
        }
        if (StringUtils.isNotBlank(template.getCode().trim())) {
            dynQuery = dynQuery + " and UPPER(mt.code) like '%'||?||'%'";
            paramList.add(template.getCode().trim().toUpperCase());
        }
        if (StringUtils.isNotBlank(template.getName().trim())) {
            dynQuery = dynQuery + " and UPPER(mt.name) like '%'||?||'%'";
            paramList.add(template.getName().trim().toUpperCase());
        }
        if (StringUtils.isNotBlank(template.getDescription().trim())) {
            dynQuery = dynQuery + " and UPPER(mt.description) like '%'||?||'%'";
            paramList.add(template.getDescription().trim().toUpperCase());
        }
        if (template.getStatus() != null && template.getStatus() != -1) {
            dynQuery = dynQuery + " and mt.status = ? ";
            paramList.add(template.getStatus());
        }
        final String countQuery = "select distinct count(mt) " + dynQuery;
        return new SearchQueryHQL(dynQuery, countQuery, paramList);
    }

    public MilestoneTemplate getTemplate() {
        return template;
    }

    public void setTemplate(final MilestoneTemplate template) {
        this.template = template;
    }

    public PersistenceService<MilestoneTemplate, Long> getMilestoneTemplateService() {
        return milestoneTemplateService;
    }

    public void setMilestoneTemplateService(
            final PersistenceService<MilestoneTemplate, Long> milestoneTemplateService) {
        this.milestoneTemplateService = milestoneTemplateService;
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

    public List<org.egov.infstr.workflow.Action> getValidActions() {
        return milestoneTemplateWorkflowService.getValidActions(template);
    }

    public void setMilestoneTemplateWorkflowService(final WorkflowService<MilestoneTemplate> milestoneTemplateWorkflowService) {
        this.milestoneTemplateWorkflowService = milestoneTemplateWorkflowService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public List<MilestoneTemplateActivity> getTemplateActivities() {
        return templateActivities;
    }

    public void setTemplateActivities(
            final List<MilestoneTemplateActivity> templateActivities) {
        this.templateActivities = templateActivities;
    }

    public void setUserService(final UserService userService) {
    }

}
