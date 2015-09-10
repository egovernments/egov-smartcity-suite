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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.core.security.user.UserImpl;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.common.entity.UOM;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.masters.MarketRate;
import org.egov.works.models.masters.SORRate;
import org.egov.works.models.masters.ScheduleCategory;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.utils.WorksConstants;

import com.opensymphony.xwork2.Action;

@Result(name = Action.SUCCESS, type = "ServletRedirectResult.class", location = "scheduleOfRate.action")
@ParentPackage("egov")
public class ScheduleOfRateAction extends SearchFormAction {
    /**
     *
     */
    private static final long serialVersionUID = -5496042432775969286L;
    private PersistenceService<ScheduleOfRate, Long> scheduleOfRateService;
    private ScheduleOfRate scheduleOfRate = new ScheduleOfRate();
    private List<ScheduleOfRate> scheduleOfRateList = null;
    private static final List<ScheduleCategory> scheduleCategoryList = null;
    private Long id;
    private String messageKey;
    private String mode;
    private String displData;

    @Required(message = "sor.category.not.null")
    private Long scheduleCategoryId = -1l;
    private String code;
    private String description;

    /* on 9/10/09 */
    private Map<Long, String> deletFlagMap = new HashMap<Long, String>();
    private Map<Long, String> deleteFlagMap2 = new HashMap<Long, String>();
    private String estimateDtFlag = "no";
    private Date estimateDate;
    public static final String flagValue = "yes";

    private List<SORRate> actionRates = new LinkedList<SORRate>();
    private List<MarketRate> actionMarketRates = new LinkedList<MarketRate>();

    private List abstractEstimateList = null;
    private List woeList = null;
    private Date woDate;
    private String woDateFlag = "no";
    private List<SORRate> editableRateList = new ArrayList<SORRate>();

    public ScheduleOfRateAction() {
        addRelatedEntity("scheduleCategory", ScheduleCategory.class);
        addRelatedEntity("uom", UOM.class);
        addRelatedEntity("createdBy", UserImpl.class);
    }

    @Override
    public String execute() {
        return list();
    }

    public String newform() {
        return NEW;
    }

    /*
     * sept2309 @return searchpage
     */
    public String searchList() {
        setDisplData("no");
        scheduleOfRateList = scheduleOfRateService.findAllBy(" from ScheduleOfRate sor order by code asc");
        return "searchpage";
    }

    public String list() {
        scheduleOfRateList = scheduleOfRateService.findAllBy(" from ScheduleOfRate sor order by code asc");
        return INDEX;
    }

    public String edit() {
        scheduleOfRate = scheduleOfRateService.findById(scheduleOfRate.getId(), false);
        if (StringUtils.isNotBlank(mode) && mode.equalsIgnoreCase("view"))
            return "edit";
        getRateDetailsForSORId(false);
        getRateDetailsForSORIdForREValidation(false);
        return "edit";
    }

    public String save() {
        scheduleOfRateService.update(scheduleOfRate);
        return SUCCESS;
    }

    public String create() {
        populateRates();
        populateMarketRates();
        getPersistedRateDetails(scheduleOfRate);
        getRateDetailsForSORId(true);
        getRateDetailsForSORIdForREValidation(true);

        if (scheduleOfRate.getId() == null)
            scheduleOfRateService.persist(scheduleOfRate);
        else
            scheduleOfRateService.merge(scheduleOfRate);
        scheduleOfRate = scheduleOfRateService.findById(scheduleOfRate.getId(), false);
        scheduleOfRateList = new ArrayList<ScheduleOfRate>();
        scheduleOfRateList.add(scheduleOfRate);
        messageKey = "sor.save.success";
        addActionMessage(getText(messageKey, "The SOR was saved successfully"));
        // return list();
        return INDEX;
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

    public void setScheduleOfRateService(
            final PersistenceService<ScheduleOfRate, Long> service) {
        scheduleOfRateService = service;
    }

    @Override
    public void prepare() {
        if (id != null)
            scheduleOfRate = scheduleOfRateService.findById(id, false);
        super.prepare();
        setupDropdownDataExcluding();
        final List<ScheduleOfRate> categories = scheduleOfRateService.findAllBy("from ScheduleCategory sc");
        addDropdownData("scheduleCategoryList", categories);
        addDropdownData("uomlist", scheduleOfRateService.findAllBy("from Uom  order by upper(uom)"));
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
     * prashanth
     *
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

    public List<ScheduleCategory> getScheduleCategoryList() {
        // scheduleCategoryList= scheduleCategoryService.findAllBy(" from
        // ScheduleCategory sc order by code asc");
        // System.out.println("$$$$$$$$$$$inside action
        // getScheduleCategoryList");
        return scheduleCategoryList;
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

    public String searchSorDetails() {
        if (scheduleCategoryId == -1) {
            messageKey = "sor.category.not.null";
            addActionError(getText(messageKey, "Please Select Category"));
            return "searchpage";
        } else {
            setPageSize(WorksConstants.PAGE_SIZE);
            search();
        }
        if (searchResult.getFullListSize() == 0)
            setDisplData("noData");
        else
            setDisplData(flagValue);

        return "searchpage";
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

    /*
     * getRateDetailsForSORId
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
        abstractEstimateList = getPersistenceService().findAllBy(
                "select ae from AbstractEstimate ae, Activity act where act.abstractEstimate=ae and act.abstractEstimate.parent is null and act.abstractEstimate.egwStatus.code <> 'CANCELLED' and act.schedule.id = "
                        + scheduleOfRate.getId());
        final SORRate rate = rateList.get(rateList.size() - 1);
        if (!abstractEstimateList.isEmpty())
            iterateAbstractList(abstractEstimateList, rate, validationMessageRequired);
    }

    public void iterateRateListForRE(final List<SORRate> rateList, final boolean validationMessageFlag) {
        woeList = getPersistenceService().findAllBy(
                "select distinct(woa.workOrderEstimate) from WorkOrderActivity woa where woa.workOrderEstimate.estimate.parent.id is not null and woa.workOrderEstimate.estimate.egwStatus.code<> 'CANCELLED' and exists (select sor.id from ScheduleOfRate sor where sor.id = woa.activity.schedule.id and sor.id = ? )",
                scheduleOfRate.getId());
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
                    final Date startDate = validity.getStartDate().toDate();
                    Date endDate = null;
                    if (validity.getEndDate() != null)
                        endDate = validity.getEndDate().toDate();
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
        }   // end of for abstractestimate
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
                    final Date startDate = validity.getStartDate().toDate();
                    Date endDate = null;
                    if (validity.getEndDate() != null)
                        endDate = validity.getEndDate().toDate();
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
                        deleteFlagMap2.put(rate.getId(), "no");
                }
            }
        }   // end of for wo
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
        final StringBuffer scheduleOfRateSql = new StringBuffer(100);
        String scheduleOfRateStr = "";
        final List<Object> paramList = new ArrayList<Object>();
        scheduleOfRateSql.append(" from ScheduleOfRate sor where sor.scheduleCategory.id=?");
        paramList.add(scheduleCategoryId);

        if (getCode() != null && !getCode().equals("")) {
            scheduleOfRateSql.append(" and UPPER(sor.code) like ?");
            paramList.add("%" + getCode().toUpperCase() + "%");
        }

        if (getDescription() != null && !getDescription().equals("")) {
            scheduleOfRateSql.append(" and UPPER(sor.description) like ?");
            paramList.add("%" + getDescription().toUpperCase() + "%");
        }

        scheduleOfRateStr = scheduleOfRateSql.toString();
        final String countQuery = "select count(*) " + scheduleOfRateStr;
        return new SearchQueryHQL(scheduleOfRateStr, countQuery, paramList);

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
}