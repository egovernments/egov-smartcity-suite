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

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.MarketRate;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.utils.WorksConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AjaxScheduleOfRateAction extends BaseFormAction implements ServletResponseAware {

    /**
     *
     */
    private static final long serialVersionUID = -184734884001856149L;
    private static final Logger logger = Logger.getLogger(AjaxScheduleOfRateAction.class);
    private static final String codeNumberUniqueCheck = "codeNumberUniqueCheck";
    private PersistenceService<ScheduleOfRate, Long> scheduleOfRateService;
    private HttpServletResponse response;
    private static final String MARKETRATEASONDATE = "marketRateAsOnDate";
    private Date asOnDate;
    private Long scheduleId;
    private String scheduleIds;
    private double marketRateValue = 0;
    private double[] marketRateValues;
    private String estimatesExists;

    public double[] getMarketRateValues() {
        return marketRateValues;
    }

    public String getScheduleIds() {
        return scheduleIds;
    }

    public void setScheduleIds(final String scheduleIds) {
        this.scheduleIds = scheduleIds;
    }

    public void setScheduleId(final Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public double getMarketRateValue() {
        return marketRateValue;
    }

    private String codeNo;
    private Long scheduleCategoryId;

    public String codeNumberUniqueCheck() {
        return codeNumberUniqueCheck;
    }

    public boolean getCodenoCheck() {
        ScheduleOfRate scheduleOfRate = null;
        boolean codeNoexistsOrNot = false;
        scheduleOfRate = scheduleOfRateService.find("from ScheduleOfRate sor where sor.code=? and sor.scheduleCategory.id=?",
                codeNo, scheduleCategoryId);
        if (scheduleOfRate != null)
            codeNoexistsOrNot = true;
        return codeNoexistsOrNot;
    }

    public String getByResponseAware() {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        try {
            response.getWriter().write("From Action " + Calendar.getInstance().getTime());
        } catch (final IOException ioex) {
            logger.info("Error while writing to response --from getByResponseAware()");
        }

        return null;
    }

    public String getByDirectResponse() {
        getResponse().setContentType("text/xml");
        getResponse().setHeader("Cache-Control", "no-cache");

        try {
            getResponse().getWriter().write("From Action " + Calendar.getInstance().getTime());
        } catch (final IOException ioex) {
            logger.info("Error while writing to response --from getByResponseAware()");
        }
        return null;
    }

    public String getMarketValueAsOnDate() {
        final String[] schId = scheduleIds.split("~");
        int count = 0;
        final List<Long> params = new LinkedList<Long>();
        for (final String element : schId)
            params.add(Long.valueOf(element));
        marketRateValues = new double[schId.length];
        final List<ScheduleOfRate> scheduleOfRatesList = scheduleOfRateService.findAllByNamedQuery("SCHEDULEOFRATES_BY_IDS",
                params);

        for (final String element : schId)
            for (final ScheduleOfRate scheduleOfRate : scheduleOfRatesList)
                if (element.equals(scheduleOfRate.getId().toString())) {
                    marketRateValue = 0.0;
                    if (scheduleOfRate.hasValidMarketRateFor(asOnDate)) {
                        final MarketRate marketRate = scheduleOfRate.getMarketRateOn(asOnDate);
                        marketRateValue = marketRate.getMarketRate().getValue();
                    }
                    marketRateValues[count] = marketRateValue;
                    count++;
                }
        return MARKETRATEASONDATE;
    }

    public String checkIfEstimateExists() {
        final List<Long> ids = persistenceService.findAllBy(
                "select distinct act.abstractEstimate.id from Activity act where act.schedule.id = ? and act.abstractEstimate.rateContract.id is not null and act.abstractEstimate.egwStatus.code != 'CANCELLED'",
                scheduleId);
        if (ids != null && !ids.isEmpty())
            estimatesExists = WorksConstants.YES;
        else
            estimatesExists = WorksConstants.NO;

        return "checkEstimatesForSOR";
    }

    @Override
    public Object getModel() {

        return null;
    }

    public void setCodeNo(final String codeNo) {
        this.codeNo = codeNo;
    }

    public void setScheduleCategoryId(final Long scheduleCategoryId) {
        this.scheduleCategoryId = scheduleCategoryId;
    }

    public void setScheduleOfRateService(
            final PersistenceService<ScheduleOfRate, Long> scheduleOfRateService) {
        this.scheduleOfRateService = scheduleOfRateService;
    }

    /**
     * Convenience method to get the request
     *
     * @return current request
     */
    protected HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    /**
     * Convenience method to get the response
     *
     * @return current response
     */
    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    @Override
    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }

    public String getEstimatesExists() {
        return estimatesExists;
    }

    public void setEstimatesExists(final String estimatesExists) {
        this.estimatesExists = estimatesExists;
    }
}
