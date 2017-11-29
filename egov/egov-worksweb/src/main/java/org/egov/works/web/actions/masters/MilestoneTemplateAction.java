/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.works.web.actions.masters;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.works.master.service.MilestoneTemplateService;
import org.egov.works.models.masters.MilestoneTemplate;
import org.egov.works.models.masters.MilestoneTemplateActivity;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = MilestoneTemplateAction.NEW, location = "milestoneTemplate-new.jsp"),
        @Result(name = MilestoneTemplateAction.SEARCH, location = "milestoneTemplate-search.jsp"),
        @Result(name = MilestoneTemplateAction.SUCCESS, location = "milestoneTemplate-success.jsp"),
        @Result(name = MilestoneTemplateAction.EDIT, location = "milestoneTemplate-edit.jsp"),
        @Result(name = MilestoneTemplateAction.VIEW, location = "milestoneTemplate-view.jsp")

})
public class MilestoneTemplateAction extends SearchFormAction {

    private static final long serialVersionUID = 5517312981738363805L;
    private MilestoneTemplate template = new MilestoneTemplate();

    @Autowired
    private MilestoneTemplateService milestoneTemplateService;

    @Autowired
    private WorksService worksService;

    Long id;
    private String mode;
    private String messageKey;
    private String sourcepage;

    private List<MilestoneTemplateActivity> templateActivities = new LinkedList<MilestoneTemplateActivity>();
    public static final String SEARCH = "search";
    public static final String SUCCESS = "success";
    public static final String EDIT = "edit";
    public static final String VIEW = "view";

    public MilestoneTemplateAction() {
        addRelatedEntity("typeOfWork", EgwTypeOfWork.class);
        addRelatedEntity("subTypeOfWork", EgwTypeOfWork.class);
    }

    @Override
    public void prepare() {
        if (id != null)
            template = milestoneTemplateService.getMilestoneTemplateById(id);
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        super.prepare();
        setupDropdownDataExcluding("typeOfWork", "subTypeOfWork");
        addDropdownData("parentCategoryList",
                getPersistenceService().findAllBy("from EgwTypeOfWork etw where etw.parentid is null"));
        populateCategoryList(ajaxEstimateAction, template.getTypeOfWork() != null);
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

    @Action(value = "/masters/milestoneTemplate-view")
    @SkipValidation
    public String view() {
        return VIEW;
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
        template = milestoneTemplateService.save(template);
        if (StringUtils.isBlank(mode))
            addActionMessage(getText("milestonetemplate.save.success", new String[] { template.getCode() }));
        else
            addActionMessage(getText("milestonetemplate.modified.success", new String[] { template.getCode() }));
        return SUCCESS;
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

    @Action(value = "/masters/milestoneTemplate-edit")
    @SkipValidation
    public String edit() {
        template = milestoneTemplateService.getMilestoneTemplateById(id);
        if (mode.equals("edit"))
            return EDIT;
        return VIEW;
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
        if (template.getTypeOfWork() == null || template.getTypeOfWork().getId() == -1) {
            final String messageKey = "milestone.template.search.typeOfWork.error";
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
        if (milestoneTemplateService.getCode(template.getCode()) && id == null)
            addFieldError("Unique.MilestoneTemplate.code", getText("milestonetemplate.code.isunique"));
        if (null == template.getMilestoneTemplateActivities() || template.getMilestoneTemplateActivities().size() == 0)
            addFieldError("milestone.activity.missing", getText("milestone.activity.missing"));
        Double percentage = 0.0;
        for (final MilestoneTemplateActivity templateActivities : template.getMilestoneTemplateActivities())
            if (templateActivities.getPercentage() != null)
                percentage += templateActivities.getPercentage();
        if (percentage != 100)
            addFieldError("milestone.activity.total.percentage", getText("milestone.activity.total.percentage"));
    }

    protected void populateCategoryList(
            final AjaxEstimateAction ajaxEstimateAction, final boolean categoryPopulated) {
        if (categoryPopulated) {
            ajaxEstimateAction.setCategory(template.getTypeOfWork().getId());
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
            activity.setMilestoneTemplate(template);

            // TODO:Fixme - Setting auditable properties by time being since HibernateEventListener is not getting
            // triggered on update of estimate for child objects
            template.setCreatedBy(worksService.getCurrentLoggedInUser());
            template.setCreatedDate(new Date());
            activity.setCreatedBy(worksService.getCurrentLoggedInUser());
            activity.setCreatedDate(new Date());
            activity.setLastModifiedBy(worksService.getCurrentLoggedInUser());
            activity.setLastModifiedDate(new Date());
        }
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        String dynQuery = " from MilestoneTemplate mt where mt.id is not null ";
        final List<Object> paramList = new ArrayList<Object>();
        if (template.getTypeOfWork() != null && template.getTypeOfWork().getId() != -1) {
            dynQuery = dynQuery + " and mt.typeOfWork.id = ? ";
            paramList.add(template.getTypeOfWork().getId());
        }
        if (template.getSubTypeOfWork() != null && template.getSubTypeOfWork().getId() != -1) {
            dynQuery = dynQuery + " and mt.subTypeOfWork.id = ? ";
            paramList.add(template.getSubTypeOfWork().getId());
        }
        if (StringUtils.isNotBlank(template.getCode().trim())) {
            dynQuery = dynQuery + " and UPPER(mt.code) like '%'||?||'%'";
            paramList.add(template.getCode().trim().toUpperCase());
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

    public List<MilestoneTemplateActivity> getTemplateActivities() {
        return templateActivities;
    }

    public WorksService getWorksService() {
        return worksService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public void setTemplateActivities(
            final List<MilestoneTemplateActivity> templateActivities) {
        this.templateActivities = templateActivities;
    }

}
