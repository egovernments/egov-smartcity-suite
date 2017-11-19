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

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.master.service.ScheduleCategoryService;
import org.egov.works.models.masters.ScheduleCategory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = ScheduleCategoryAction.NEW, location = "scheduleCategory-new.jsp"),
        @Result(name = ScheduleCategoryAction.EDIT, location = "scheduleCategory-edit.jsp"),
        @Result(name = ScheduleCategoryAction.SUCCESS, location = "scheduleCategory-success.jsp")
})
public class ScheduleCategoryAction extends BaseFormAction {

    private static final long serialVersionUID = 8722637434208106061L;

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;
    private ScheduleCategory scheduleCategory = new ScheduleCategory();
    private List<ScheduleCategory> scheduleCategoryList = null;
    private Long id;
    private String mode;
    private String code;

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String execute() {
        return list();
    }

    @Action(value = "/masters/scheduleCategory-newform")
    public String newform() {
        return NEW;
    }

    public String list() {
        scheduleCategoryList = scheduleCategoryService.getAllScheduleCategories();
        return NEW;
    }

    @Action(value = "/masters/scheduleCategory-edit")
    public String edit() {
        return EDIT;
    }

    @Override
    public void prepare() {
        scheduleCategoryList = scheduleCategoryService.getAllScheduleCategories();
        if (id != null)
            scheduleCategory = scheduleCategoryService.findById(id, false);
        super.prepare();
    }

    @Action(value = "/masters/scheduleCategory-save")
    public String save() {
        if (mode.equals("edit") && !scheduleCategoryService.checkForSOR(id)) {
            addActionMessage(getText("scheduleCategory.modify.validate.message"));
            return EDIT;
        } else if (mode.equals("edit") && scheduleCategoryService.checkForScheduleCategory(code)) {
            addActionMessage(getText("scheduleCategory.code.isunique"));
            return EDIT;
        } else
            scheduleCategoryService.persist(scheduleCategory);
        addActionMessage(getText("schedule.category.save.success"));
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return scheduleCategory;
    }

    public List<ScheduleCategory> getScheduleCategoryList() {
        return scheduleCategoryList;
    }

    public void setScheduleCategoryList(final List<ScheduleCategory> scheduleCategoryList) {
        this.scheduleCategoryList = scheduleCategoryList;
    }

    public ScheduleCategory getScheduleCategory() {
        return scheduleCategory;
    }

    public void setScheduleCategory(
            final ScheduleCategory scheduleCategory) {
        this.scheduleCategory = scheduleCategory;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

}