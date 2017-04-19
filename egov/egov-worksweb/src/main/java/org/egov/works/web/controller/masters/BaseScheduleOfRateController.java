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
package org.egov.works.web.controller.masters;

import java.util.Date;
import java.util.List;

import org.egov.infra.persistence.entity.component.Period;
import org.egov.infra.validation.regex.Constants;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.masters.entity.MarketRate;
import org.egov.works.masters.entity.SORRate;
import org.egov.works.masters.entity.ScheduleOfRate;
import org.egov.works.masters.service.ScheduleOfRateService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

@Controller
public abstract class BaseScheduleOfRateController {

    protected static final String SCHEDULEOFRATE = "scheduleOfRate";

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    protected void validateScheduleOfRate(final ScheduleOfRate scheduleOfRate, final BindingResult resultBinder) {
        final ScheduleOfRate existingScheduleOfRate = scheduleOfRateService
                .getByCodeAndScheduleCategoryId(scheduleOfRate.getCode(), scheduleOfRate.getScheduleCategory().getId());
        if (existingScheduleOfRate != null && !existingScheduleOfRate.getId().equals(scheduleOfRate.getId()))
            resultBinder.reject("error.scheduleofrate.exists", new String[] { scheduleOfRate.getCode() },
                    "error.scheduleofrate.exists");

        if (scheduleOfRate.getCode() == null)
            resultBinder.reject("error.scheduleofrate.code", "error.scheduleofrate.code");

        if (scheduleOfRate.getScheduleCategory() == null)
            resultBinder.reject("error.scheduleofrate.schedulecategory", "error.scheduleofrate.schedulecategory");

        if (scheduleOfRate.getDescription() == null)
            resultBinder.reject("error.scheduleofrate.description", "error.scheduleofrate.description");

        if (scheduleOfRate.getUom() == null)
            resultBinder.reject("error.scheduleofrate.uom", "error.scheduleofrate.uom");

        checkValidationForPatterns(scheduleOfRate, resultBinder);

    }

    private void checkValidationForPatterns(final ScheduleOfRate scheduleOfRate, final BindingResult resultBinder) {
        if (scheduleOfRate.getCode() != null
                && !scheduleOfRate.getCode().matches(WorksConstants.ALPHANUMERICWITHHYPHENSLASH))
            resultBinder.reject("error.scheduleofrate.code.invalid", "error.scheduleofrate.code.invalid");
        if (scheduleOfRate.getDescription() != null
                && !scheduleOfRate.getDescription().matches(Constants.ALPHANUMERICWITHSPECIALCHAR))
            resultBinder.reject("error.scheduleofrate.description.invalid", "error.scheduleofrate.description.invalid");

    }

    protected void validateSORRateDetails(final ScheduleOfRate scheduleOfRate, final BindingResult resultBinder) {
        int index = 0;
        if (scheduleOfRate.getTempSorRates() == null && scheduleOfRate.getTempSorRates().isEmpty())
            resultBinder.reject("error.sorrate.atleastone", "error.sorrate.atleastone");
        else {
            compareStartDateAndEndDateBetweenStages(scheduleOfRate.getTempSorRates(), resultBinder);
            for (final SORRate sorRate : scheduleOfRate.getTempSorRates()) {
                compareStartDateAndEndDate(resultBinder, sorRate);
                if (sorRate.getRate() == null)
                    resultBinder.rejectValue("tempSorRates[" + index + "].rate", "error.scheduleofrate.sorrate.value");
                if (sorRate.getValidity().getStartDate() == null)
                    resultBinder.rejectValue("tempSorRates[" + index + "].validity.startDate",
                            "error.contractordetail.fromdate");
                index++;
            }
        }
    }

    private void compareStartDateAndEndDateBetweenStages(final List<?> tempObjList, final BindingResult resultBinder) {
        if (tempObjList.get(0) instanceof SORRate)
            compareDatesForSorRates(tempObjList, resultBinder);
        else
            compareDatesForMarketRates(tempObjList, resultBinder);
    }

    private void compareDatesForMarketRates(final List<?> tempObjList, final BindingResult resultBinder) {
        for (int i = 1; i < tempObjList.size(); i++)
            if (tempObjList.get(i - 1) != null && tempObjList.get(i) != null
                    && ((MarketRate) tempObjList.get(i - 1)).getValidity().getEndDate() != null) {
                final Date previousStageEndDate = ((MarketRate) tempObjList.get(i - 1)).getValidity().getEndDate();
                final Date currentStageStartDate = ((MarketRate) tempObjList.get(i)).getValidity().getStartDate();
                if (currentStageStartDate.compareTo(previousStageEndDate) < 0)
                    resultBinder.reject("error.marketrate.startdate.enddate", "error.marketrate.startdate.enddate");
            }
    }

    private void compareDatesForSorRates(final List<?> tempObjList, final BindingResult resultBinder) {
        for (int i = 1; i < tempObjList.size(); i++)
            if (tempObjList.get(i - 1) != null && tempObjList.get(i) != null
                    && ((SORRate) tempObjList.get(i - 1)).getValidity().getEndDate() != null) {
                final Date previousStageEndDate = ((SORRate) tempObjList.get(i - 1)).getValidity().getEndDate();
                final Date currentStageStartDate = ((SORRate) tempObjList.get(i)).getValidity().getStartDate();
                if (currentStageStartDate.compareTo(previousStageEndDate) < 0)
                    resultBinder.reject("error.sorrate.startdate.enddate", "error.sorrate.startdate.enddate");
            }
    }

    private void compareStartDateAndEndDate(final BindingResult resultBinder, final Object obj) {
        Period validity;
        if (obj.getClass() == MarketRate.class) {
            validity = ((MarketRate) obj).getValidity();
            checkDateValidity(resultBinder, validity, "marketrate");
        } else {
            validity = ((SORRate) obj).getValidity();
            checkDateValidity(resultBinder, validity, "sorrate");
        }
    }

    private void checkDateValidity(final BindingResult resultBinder, final Period validity, final String errorCode) {
        if (validity != null && validity.getStartDate() != null && validity.getEndDate() != null
                && validity.getStartDate().compareTo(validity.getEndDate()) > 0)
            resultBinder.reject("error." + errorCode + ".invaliddaterange", "error." + errorCode + ".invaliddaterange");
    }

    protected void validateMarketRateDetails(final ScheduleOfRate scheduleOfRate, final BindingResult resultBinder) {
        int index = 0;
        if (!scheduleOfRate.getTempMarketRates().isEmpty()
                && scheduleOfRate.getTempMarketRates().get(0).getMarketRate().getValue() != 0.0) {
            compareStartDateAndEndDateBetweenStages(scheduleOfRate.getTempMarketRates(), resultBinder);
            for (final MarketRate marketRate : scheduleOfRate.getTempMarketRates()) {
                compareStartDateAndEndDate(resultBinder, marketRate);
                if (marketRate.getMarketRate() == null)
                    resultBinder.rejectValue("tempMarketRates[" + index + "].marketRate",
                            "error.scheduleofrate.marketrate.value");
                if (marketRate.getValidity().getStartDate() == null)
                    resultBinder.rejectValue("tempMarketRates[" + index + "].validity.startDate",
                            "error.contractordetail.fromdate");
                index++;
            }
        }
    }

    protected void getRateDetailsForSORForAEValidation(final ScheduleOfRate scheduleOfRate,
            final BindingResult resultBinder) {
        if (scheduleOfRate.getId() != null) {
            final List<AbstractEstimate> abstractEstimateList = scheduleOfRateService
                    .getAllAbstractEstimateByScheduleOrRateId(scheduleOfRate.getId());
            final List<SORRate> rateList = scheduleOfRate.getTempSorRates();
            final SORRate rate = rateList.get(rateList.size() - 1);
            if (!abstractEstimateList.isEmpty())
                iterateAbstractList(abstractEstimateList, rate, resultBinder);
        }
    }

    protected void getRateDetailsForSORForREValidation(final ScheduleOfRate scheduleOfRate,
            final BindingResult resultBinder) {
        if (scheduleOfRate.getId() != null) {
            final List<WorkOrderEstimate> woeList = scheduleOfRateService
                    .getAllWorkOrderEstimateByScheduleOfRateId(scheduleOfRate.getId());
            final List<SORRate> rateList = scheduleOfRate.getTempSorRates();
            final SORRate rate = rateList.get(rateList.size() - 1);
            if (!woeList.isEmpty())
                iterateWOList(woeList, rate, resultBinder, scheduleOfRate);
        }
    }

    public void iterateAbstractList(final List<AbstractEstimate> abstractEstimateList, final SORRate rate,
            final BindingResult resultBinder) {
        AbstractEstimate abstractEstimate;
        for (int i = 0; i < abstractEstimateList.size(); i++)
            if (!resultBinder.hasErrors()) {
                abstractEstimate = abstractEstimateList.get(i);
                checkValidationForOverlappingAE(rate, resultBinder, abstractEstimate);
            }
    }

    private void checkValidationForOverlappingAE(final SORRate rate, final BindingResult resultBinder,
            final AbstractEstimate abstractEstimate) {
        if (abstractEstimate != null) {
            final Date estimateDate = abstractEstimate.getEstimateDate();
            if (rate != null) {
                final boolean flag = setDateRangeFlagValue(rate, estimateDate);
                if (flag)
                    resultBinder.reject("error.sorestimatedate.overlaperror", "error.sorestimatedate.overlaperror");
            }
        }
    }

    private boolean setDateRangeFlagValue(final SORRate rate, final Date date) {
        final Period validity = rate.getValidity();
        final Date startDate = validity.getStartDate();
        final Date endDate = validity.getEndDate();
        if (startDate != null && rate.getId() != null && date == null)
            return false;
        else
            return isWithinDateRangeOfEstimateOrWO(date, startDate, endDate);
    }

    public void iterateWOList(final List<WorkOrderEstimate> woeList, final SORRate rate,
            final BindingResult resultBinder, final ScheduleOfRate scheduleOfRate) {
        WorkOrder revisionWO;
        WorkOrder parentWO;
        WorkOrderEstimate woe;
        for (int i = 0; i < woeList.size(); i++)
            if (!resultBinder.hasErrors()) {
                woe = woeList.get(i);
                revisionWO = woe.getWorkOrder();
                parentWO = revisionWO.getParent();
                checkValidationForOverlappingRE(woeList, rate, resultBinder, scheduleOfRate, parentWO);
            }
    }

    private void checkValidationForOverlappingRE(final List<WorkOrderEstimate> woeList, final SORRate rate,
            final BindingResult resultBinder, final ScheduleOfRate scheduleOfRate, final WorkOrder parentWO) {
        if (parentWO != null) {
            final Date woDate = parentWO.getWorkOrderDate();
            if (rate != null) {
                final boolean flag = setDateRangeFlagValue(rate, woDate);
                if (flag)
                    validateWODate(flag, woeList, resultBinder, scheduleOfRate);
            }
        }
    }

    private static boolean isWithinDateRangeOfEstimateOrWO(final Date dateToSearch, final Date startdate,
            final Date enddate) {
        if (enddate == null) {
            if (startdate.before(dateToSearch))
                return true;
        } else if (startdate.before(dateToSearch) && dateToSearch.after(enddate))
            return true;
        return false;
    }

    public void validateWODate(final boolean flag, final List<WorkOrderEstimate> woList,
            final BindingResult resultBinder, final ScheduleOfRate scheduleOfRate) {
        if (flag && woList.size() == 1)
            resultBinder.reject("error.sorre.wodateoverlap", new String[] { scheduleOfRate.getCode() },
                    "error.sorre.wodateoverlap");
        else
            resultBinder.reject("error.sormultiplere.wodateoverlap", "error.sormultiplere.wodateoverlap");
    }
}
