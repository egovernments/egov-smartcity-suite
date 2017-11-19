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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.common.entity.UOM;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.master.service.ScheduleCategoryService;
import org.egov.works.master.service.ScheduleOfRateService;
import org.egov.works.master.service.UOMService;
import org.egov.works.models.masters.MarketRate;
import org.egov.works.models.masters.SORRate;
import org.egov.works.models.masters.ScheduleCategory;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;

@Results({
        @Result(name = ScheduleOfRateAction.NEW, location = "scheduleOfRate-new.jsp"),
        @Result(name = ScheduleOfRateAction.SEARCH, location = "scheduleOfRate-search.jsp"),
        @Result(name = ScheduleOfRateAction.EDIT, location = "scheduleOfRate-edit.jsp"),
        @Result(name = ScheduleOfRateAction.SUCCESS, location = "scheduleOfRate-success.jsp"),
        @Result(name = ScheduleOfRateAction.VIEW, location = "scheduleOfRate-view.jsp")
})
@ParentPackage("egov")
public class ScheduleOfRateAction extends SearchFormAction {

    private static final long serialVersionUID = -5496042432775969286L;
    @Autowired
    private ScheduleOfRateService scheduleOfRateService;
    @Autowired
    private ScheduleCategoryService scheduleCategoryService;
    @Autowired
    private UOMService uomService;
    private ScheduleOfRate scheduleOfRate = new ScheduleOfRate();
    private List<ScheduleOfRate> scheduleOfRateList = null;
    private List<ScheduleCategory> scheduleCategoryList = null;
    private Long id;
    private String mode;
    private String displData;

    @Required(message = "sor.category.not.null")
    private Long scheduleCategoryId = -1l;
    private String code;
    private String description;

    private Map<Long, String> deletFlagMap = new HashMap<Long, String>();
    private Map<Long, String> deleteFlagMap2 = new HashMap<Long, String>();
    private String estimateDtFlag = WorksConstants.NO;
    private Date estimateDate;
    public static final String flagValue = WorksConstants.YES;

    private List<SORRate> actionRates = new LinkedList<SORRate>();
    private List<MarketRate> actionMarketRates = new LinkedList<MarketRate>();

    private List abstractEstimateList = null;
    private List woeList = null;
    private Date woDate;
    private String woDateFlag = WorksConstants.NO;
    private List<SORRate> editableRateList = new ArrayList<SORRate>();
    public static final String SEARCH = "search";

    public ScheduleOfRateAction() {
        addRelatedEntity("scheduleCategory", ScheduleCategory.class);
        addRelatedEntity("uom", UOM.class);
    }

    @Override
    public String execute() {
        return list();
    }

    @Action(value = "/masters/scheduleOfRate-newform")
    public String newform() {
        return NEW;
    }

    @Override
    @SkipValidation
    @Action(value = "/masters/scheduleOfRate-search")
    public String search() {
        return SEARCH;
    }

    @Action(value = "/masters/scheduleOfRate-searchList")
    public String searchList() {
        setDisplData(WorksConstants.NO);
        scheduleCategoryList = scheduleCategoryService.getAllScheduleCategories();
        return SEARCH;
    }

    public String list() {
        scheduleOfRateList = scheduleOfRateService.getAllScheduleOfRates();
        return SUCCESS;
    }

    @Action(value = "/masters/scheduleOfRate-edit")
    public String edit() {
        scheduleOfRate = scheduleOfRateService.getScheduleOfRateById(id);
        if (StringUtils.isNotBlank(mode) && mode.equalsIgnoreCase(WorksConstants.VIEW))
            return VIEW;
        getRateDetailsForSORId(false);
        getRateDetailsForSORIdForREValidation(false);
        return EDIT;
    }

    @Action(value = "/masters/scheduleOfRate-save")
    public String save() {
        populateRates();
        populateMarketRates();
        getPersistedRateDetails(scheduleOfRate);
        if (mode != null && mode.equals(WorksConstants.EDIT)) {
            getRateDetailsForSORId(true);
            getRateDetailsForSORIdForREValidation(true);
        }
        scheduleOfRateService.persist(scheduleOfRate);
        scheduleOfRate = scheduleOfRateService.findById(scheduleOfRate.getId(), false);
        scheduleOfRateList = new ArrayList<ScheduleOfRate>();
        scheduleOfRateList.add(scheduleOfRate);
        if (StringUtils.isBlank(mode))
            addActionMessage(getText("sor.save.success"));
        else
            addActionMessage(getText("sor.modified.success", new String[] { code }));
        return SUCCESS;
    }

    protected void populateRates() {
        for (final SORRate rate : actionRates)
            if (validRate(rate))
                scheduleOfRate.addSorRate(rate);
    }

    protected boolean validRate(final SORRate rate) {
        if (rate != null)
            return true;
        return false;
    }

    protected void populateMarketRates() {
        for (final MarketRate marketRate : actionMarketRates)
            if (validMarketRate(marketRate))
                scheduleOfRate.addMarketRate(marketRate);
    }

    protected boolean validMarketRate(final MarketRate marketRate) {
        if (marketRate != null)
            return true;
        return false;
    }

    @Override
    public Object getModel() {
        return scheduleOfRate;
    }

    public Collection<ScheduleOfRate> getScheduleOfRateList() {
        return scheduleOfRateList;
    }

    @Override
    public void prepare() {
        if (id != null)
            scheduleOfRate = scheduleOfRateService.findById(id, false);
        super.prepare();
        scheduleCategoryList = scheduleCategoryService.getAllScheduleCategories();
        addDropdownData("scheduleCategoryList", scheduleCategoryList);
        addDropdownData("uomlist", uomService.getAllUOMs());
    }

    private void getPersistedRateDetails(final ScheduleOfRate sor) {
        List<SORRate> rateList = null;
        rateList = scheduleOfRate.getSorRates();

        for (final SORRate rt : rateList)
            if (rt.getId() != null)
                editableRateList.add(rt);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ScheduleOfRate getScheduleOfRate() {
        return scheduleOfRate;
    }

    public void setScheduleOfRate(final ScheduleOfRate scheduleOfRate) {
        this.scheduleOfRate = scheduleOfRate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    /**
     * @return the displData
     */
    public String getDisplData() {
        return displData;
    }

    /**
     * @param displData the displData to set
     */
    public void setDisplData(final String displData) {
        this.displData = displData;
    }

    /**
     * @return the scheduleCategoryId
     * @Validation @RequiredStringValidator(message="Please select a category")
     */
    public Long getScheduleCategoryId() {
        return scheduleCategoryId;
    }

    /**
     * @param scheduleCategoryId the scheduleCategoryId to set
     */
    public void setScheduleCategoryId(final Long scheduleCategoryId) {
        this.scheduleCategoryId = scheduleCategoryId;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the sorCode to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    @Action(value = "/masters/scheduleOfRate-searchSorDetails")
    public String searchSorDetails() {
        if (scheduleCategoryId == -1) {
            addActionError(getText("sor.category.not.null"));
            return SEARCH;
        } else {
            setPageSize(WorksConstants.PAGE_SIZE);
            super.search();
        }
        if (searchResult.getFullListSize() == 0)
            setDisplData(WorksConstants.NO_DATA);
        else
            setDisplData(flagValue);

        return SEARCH;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     *
     * @param validationMessageRequired
     */
    public void getRateDetailsForSORId(final boolean validationMessageRequired) {
        if (scheduleOfRate.getId() != null && validationMessageRequired)
            if (!editableRateList.isEmpty())
                iterateRateList(editableRateList, validationMessageRequired);
    }

    public void getRateDetailsForSORIdForREValidation(final boolean validationMessageFlag) {
        if (scheduleOfRate.getId() != null && validationMessageFlag)
            if (!editableRateList.isEmpty())
                iterateRateListForRE(editableRateList, validationMessageFlag);
    }

    public void iterateRateList(final List<SORRate> rateList, final boolean validationMessageRequired) {
        abstractEstimateList = scheduleOfRateService.getAllAbstractEstimateByScheduleOrRateId(scheduleOfRate.getId());
        final SORRate rate = rateList.get(rateList.size() - 1);
        if (!abstractEstimateList.isEmpty())
            iterateAbstractList(abstractEstimateList, rate, validationMessageRequired);
    }

    public void iterateRateListForRE(final List<SORRate> rateList, final boolean validationMessageFlag) {
        woeList = scheduleOfRateService.getAllWorkOrderEstimateByScheduleOfRateId(scheduleOfRate.getId());
        final SORRate rate = rateList.get(rateList.size() - 1);
        if (!woeList.isEmpty())
            iterateWOList(woeList, rate, validationMessageFlag);
    }

    public void iterateAbstractList(final List abstractEstimateList, final SORRate rate,
            final boolean validationMessageRequired) {
        AbstractEstimate abstractEstimate = null;
        final Map<Integer, String> trackFlagMap = new HashMap<Integer, String>();
        for (int i = 0; i < abstractEstimateList.size(); i++) {
            abstractEstimate = (AbstractEstimate) abstractEstimateList.get(i);
            if (abstractEstimate != null) {
                estimateDate = abstractEstimate.getEstimateDate();
                if (rate != null) {
                    final Period validity = rate.getValidity();
                    final Date startDate = validity.getStartDate();
                    Date endDate = null;
                    if (validity.getEndDate() != null)
                        endDate = validity.getEndDate();
                    boolean flag = false;
                    if (startDate != null && rate.getId() != null)
                        flag = checkGivenDateWithinRange(estimateDate, startDate, endDate);
                    if (flag) {
                        deletFlagMap.put(rate.getId(), flagValue);
                        setEstimateDtFlag(flagValue);
                        trackFlagMap.put(0, flagValue);
                        if (validationMessageRequired)
                            throw new ValidationException(Arrays.asList(new ValidationError("sor.estimateDate.overlap.error",
                                    getText("sor.estimateDate.overlap.error"))));
                    } else if (!trackFlagMap.isEmpty()) {
                        final String value = trackFlagMap.get(0);
                        if (value != null && !value.equalsIgnoreCase(flagValue))
                            deletFlagMap.put(rate.getId(), "no");
                    }
                }
            }
        }        // end of for abstractestimate
    }

    public void iterateWOList(final List woeList, final SORRate rate, final boolean validationMessageFlag) {
        WorkOrder revisionWO = null;
        WorkOrder parentWO = null;
        WorkOrderEstimate woe = null;
        final Map<Integer, String> trackFlagMap = new HashMap<Integer, String>();
        for (int i = 0; i < woeList.size(); i++) {
            woe = (WorkOrderEstimate) woeList.get(i);
            revisionWO = woe.getWorkOrder();
            parentWO = revisionWO.getParent();
            if (parentWO != null) {
                woDate = parentWO.getWorkOrderDate();
                if (rate != null) {
                    final Period validity = rate.getValidity();
                    final Date startDate = validity.getStartDate();
                    Date endDate = null;
                    if (validity.getEndDate() != null)
                        endDate = validity.getEndDate();
                    boolean flag = false;
                    if (startDate != null && rate.getId() != null)
                        flag = checkGivenDateWithinRangeWO(woDate, startDate, endDate);
                    if (flag) {
                        deleteFlagMap2.put(rate.getId(), flagValue);
                        setWoDateFlag(flagValue);
                        trackFlagMap.put(0, flagValue);
                        // This flag is added to display validation message, only when called from create() method, not from
                        // edit() method.
                        if (validationMessageFlag)
                            validateWODate(flag, woeList);
                    }
                } else if (!trackFlagMap.isEmpty()) {
                    final String value = trackFlagMap.get(0);
                    if (value != null && !value.equalsIgnoreCase(flagValue))
                        deleteFlagMap2.put(rate.getId(), WorksConstants.NO);
                }
            }
        }      // end of for wo
    }

    public void validateWODate(final boolean flag, final List woList) {
        final WorkOrderEstimate woe1 = (WorkOrderEstimate) woList.get(0);
        if (flag == true)
            if (woList.size() == 1)
                throw new ValidationException(Arrays.asList(new ValidationError("sor.RE.woDate.overlap",
                        getText("sor.RE.woDate.overlap", new String[] { woe1.getEstimate().getEstimateNumber() }))));
            else
                throw new ValidationException(Arrays
                        .asList(new ValidationError("sor.multipleRE.woDate.overlap", getText("sor.multipleRE.woDate.overlap"))));

    }

    private static boolean isWithinDateRangeOfEstimateOrWO(final Date dateToSearch, final Date startdate, final Date enddate) {

        if (enddate == null) {
            if (startdate.before(dateToSearch))
                return true;
        } else if (startdate.before(dateToSearch) && dateToSearch.after(enddate))
            return true;
        return false;
    }

    /**
     * @return the deletFlagMap
     */
    public Map<Long, String> getDeletFlagMap() {
        return deletFlagMap;
    }

    /**
     * @param deletFlagMap the deletFlagMap to set
     */
    public void setDeletFlagMap(final Map<Long, String> deletFlagMap) {
        this.deletFlagMap = deletFlagMap;
    }

    /**
     * @return the estimateDtFlag
     */
    public String getEstimateDtFlag() {
        return estimateDtFlag;
    }

    /**
     * @param estimateDtFlag the estimateDtFlag to set
     */
    public void setEstimateDtFlag(final String estimateDtFlag) {
        this.estimateDtFlag = estimateDtFlag;
    }

    /**
     * @return the estimateDate
     */
    public Date getEstimateDate() {
        return estimateDate;
    }

    /**
     * @param estimateDate the estimateDate to set
     */
    public void setEstimateDate(final Date estimateDate) {
        this.estimateDate = estimateDate;
    }

    public boolean checkGivenDateWithinRange(final Date estimateDate, final Date startDate, final Date endDate) {
        if (estimateDate == null)
            return false;
        else
            return isWithinDateRangeOfEstimateOrWO(estimateDate, startDate, endDate);
    }

    public boolean checkGivenDateWithinRangeWO(final Date woDate, final Date startDate, final Date endDate) {
        if (woDate == null)
            return false;
        else
            return isWithinDateRangeOfEstimateOrWO(woDate, startDate, endDate);
    }

    /**
     * @return the actionRates
     */
    public List<SORRate> getActionRates() {
        return actionRates;
    }

    /**
     * @param actionRates the actionRates to set
     */
    public void setActionRates(final List<SORRate> actionRates) {
        this.actionRates = actionRates;
    }

    /**
     * @return the actionMarketRates
     */
    public List<MarketRate> getActionMarketRates() {
        return actionMarketRates;
    }

    /**
     * @param actionMarketRates the actionMarketRates to set
     */
    public void setActionMarketRates(final List<MarketRate> actionMarketRates) {
        this.actionMarketRates = actionMarketRates;
    }

    /**
     * @return the abstractEstimateList
     */
    public List getAbstractEstimateList() {
        return abstractEstimateList;
    }

    /**
     * @param abstractEstimateList the abstractEstimateList to set
     */
    public void setAbstractEstimateList(final List abstractEstimateList) {
        this.abstractEstimateList = abstractEstimateList;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        return scheduleOfRateService.prepareSearchQuery(scheduleCategoryId, code, description);
    }

    public Map<Long, String> getDeleteFlagMap2() {
        return deleteFlagMap2;
    }

    public void setDeleteFlagMap2(final Map<Long, String> deleteFlagMap2) {
        this.deleteFlagMap2 = deleteFlagMap2;
    }

    public Date getWoDate() {
        return woDate;
    }

    public void setWoDate(final Date woDate) {
        this.woDate = woDate;
    }

    public String getWoDateFlag() {
        return woDateFlag;
    }

    public void setWoDateFlag(final String woDateFlag) {
        this.woDateFlag = woDateFlag;
    }

    public List getWoeList() {
        return woeList;
    }

    public void setWoeList(final List woeList) {
        this.woeList = woeList;
    }

    public List<SORRate> getEditableRateList() {
        return editableRateList;
    }

    public void setEditableRateList(final List<SORRate> editableRateList) {
        this.editableRateList = editableRateList;
    }

    public List<ScheduleCategory> getScheduleCategoryList() {
        return scheduleCategoryList;
    }

    public void setScheduleCategoryList(final List<ScheduleCategory> scheduleCategoryList) {
        this.scheduleCategoryList = scheduleCategoryList;
    }

    public void setScheduleOfRateList(final List<ScheduleOfRate> scheduleOfRateList) {
        this.scheduleOfRateList = scheduleOfRateList;
    }
}