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

package org.egov.ptis.web.controller.dashboard;
import static org.egov.ptis.constants.PropertyTaxConstants. DASHBOARD_GROUPING_ULBWISE;
import static org.egov.ptis.constants.PropertyTaxConstants. DAY;
import static org.egov.ptis.constants.PropertyTaxConstants. MONTH;
import static org.egov.ptis.constants.PropertyTaxConstants. WEEK;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.bean.dashboard.CollReceiptDetails;
import org.egov.ptis.bean.dashboard.CollectionAnalysis;
import org.egov.ptis.bean.dashboard.CollectionDetails;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.MISDCBDetails;
import org.egov.ptis.bean.dashboard.MonthlyDCB;
import org.egov.ptis.bean.dashboard.PropertyTaxDefaultersRequest;
import org.egov.ptis.bean.dashboard.StateCityInfo;
import org.egov.ptis.bean.dashboard.TaxDefaulters;
import org.egov.ptis.bean.dashboard.TaxPayerResponseDetails;
import org.egov.ptis.bean.dashboard.TotalCollectionStats;
import org.egov.ptis.bean.dashboard.WeeklyDCB;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.service.dashboard.PropTaxDashboardService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to provide services for CM Dashboard
 */

@RestController
@RequestMapping(value = { "/public/dashboard", "/dashboard" })
public class CMDashboardController {
    private static final String MILLISECS = " (millisecs)";

    private static final Logger LOGGER = LoggerFactory.getLogger(CMDashboardController.class);

    @Autowired
    private PropTaxDashboardService propTaxDashboardService;

    /**
     * Gives the State-City information across all ULBs
     * 
     * @return string
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    @RequestMapping(value = "/statecityinfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StateCityInfo> getStateCityInformation() throws IOException {
        Long startTime = System.currentTimeMillis();
        final List<StateCityInfo> stateDetails = propTaxDashboardService.getStateCityDetails();
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve statecityinfo is : " + timeTaken + MILLISECS);
        return stateDetails;
    }

    /**
     * Provides State-wise Collection Statistics for Property Tax, Water Charges
     * and Others
     * 
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/collectionstats", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TotalCollectionStats getConsolidatedCollDetails(final HttpServletRequest request) throws IOException {
        Long startTime = System.currentTimeMillis();
        TotalCollectionStats consolidatedCollectionDetails = propTaxDashboardService.getTotalCollectionStats(request);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve collectionstats is : " + timeTaken + MILLISECS);
        return consolidatedCollectionDetails;

    }

    /**
     * Provides Collection Index details across all ULBs
     * 
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/collectiondashboard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionDetails getCollectionDetails(@RequestBody CollectionDetailsRequest collectionDetailsRequest)
            throws IOException {
        Long startTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDetailsRequest.getRegionName()
                + ", districtName = " + collectionDetailsRequest.getDistrictName() + ", ulbGrade = "
                + collectionDetailsRequest.getUlbGrade() + ", ulbCode = " + collectionDetailsRequest.getUlbCode()
                + ", fromDate = " + collectionDetailsRequest.getFromDate() + ", toDate = "
                + collectionDetailsRequest.getToDate() + ", type = " + collectionDetailsRequest.getType());
        CollectionDetails collectionDetails = propTaxDashboardService
                .getCollectionIndexDetails(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve collectiondashboard is : " + timeTaken + MILLISECS);
        return collectionDetails;
    }

    /**
     * Gives the receipts details across all ULBs
     * 
     * @param collectionDetailsRequest
     * @return CollReceiptDetails
     */
    @RequestMapping(value = "/receipttransactions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollReceiptDetails getReceiptTransactions(@RequestBody CollectionDetailsRequest collectionDetailsRequest)
            throws IOException {
        Long startTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDetailsRequest.getRegionName()
                + ", districtName = " + collectionDetailsRequest.getDistrictName() + ", ulbGrade = "
                + collectionDetailsRequest.getUlbGrade() + ", ulbCode = " + collectionDetailsRequest.getUlbCode()
                + ", fromDate = " + collectionDetailsRequest.getFromDate() + ", toDate = "
                + collectionDetailsRequest.getToDate() + ", type = " + collectionDetailsRequest.getType());
        CollReceiptDetails collReceiptDetails = propTaxDashboardService.getReceiptDetails(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve receipttransactions is : " + timeTaken + MILLISECS);
        return collReceiptDetails;
    }

    /**
     * Returns Top Ten Tax Payers Across all ULB's
     * 
     * @param collDetailsRequestStr
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/toptentaxers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxPayerResponseDetails getTopTenTaxProducers(@RequestBody CollectionDetailsRequest collectionDetailsRequest)
            throws IOException {
        Long startTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDetailsRequest.getRegionName()
                + ", districtName = " + collectionDetailsRequest.getDistrictName() + ", ulbGrade = "
                + collectionDetailsRequest.getUlbGrade() + ", ulbCode = " + collectionDetailsRequest.getUlbCode()
                + ", fromDate = " + collectionDetailsRequest.getFromDate() + ", toDate = "
                + collectionDetailsRequest.getToDate() + ", type = " + collectionDetailsRequest.getType());
        TaxPayerResponseDetails taxPayerDetails = propTaxDashboardService
                .getTopTenTaxProducers(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve toptentaxers is : " + timeTaken + MILLISECS);
        return taxPayerDetails;
    }

    /**
     * Returns Bottom Ten Tax Payers Across all ULB's
     * 
     * @param collDetailsRequestStr
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/bottomtentaxers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxPayerResponseDetails getBottomTenTaxProducers(
            @RequestBody CollectionDetailsRequest collectionDetailsRequest) throws IOException {
        Long startTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDetailsRequest.getRegionName()
                + ", districtName = " + collectionDetailsRequest.getDistrictName() + ", ulbGrade = "
                + collectionDetailsRequest.getUlbGrade() + ", ulbCode = " + collectionDetailsRequest.getUlbCode()
                + ", fromDate = " + collectionDetailsRequest.getFromDate() + ", toDate = "
                + collectionDetailsRequest.getToDate() + ", type = " + collectionDetailsRequest.getType());
        TaxPayerResponseDetails taxPayerDetails = propTaxDashboardService
                .getBottomTenTaxProducers(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve bottomtentaxers is : " + timeTaken + MILLISECS);
        return taxPayerDetails;
    }

    @RequestMapping(value = "/topdefaulters", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaxDefaulters> getTopTaxDefaulters(
            @RequestBody PropertyTaxDefaultersRequest propertyTaxDefaultersRequest) throws IOException {
        Long startTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("PropertyTaxDefaultersRequest input : regionName = " + propertyTaxDefaultersRequest.getRegionName()
                + ", districtName = " + propertyTaxDefaultersRequest.getDistrictName() + ", type = "
                + propertyTaxDefaultersRequest.getType() + ", ulbCode = " + propertyTaxDefaultersRequest.getUlbCode()
                + ", wardName = " + propertyTaxDefaultersRequest.getWardName());
        List<TaxDefaulters> taxDefaulters = propTaxDashboardService.getTaxDefaulters(propertyTaxDefaultersRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve topdefaulters is : " + timeTaken + MILLISECS);
        return taxDefaulters;
    }
    
    /**
     * Provides Collection Index details across all ULBs for MIS Reports
     * 
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/targetmis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody public CollectionDetails getCollectionDetailsForTargetMIS(@RequestParam("regionName") String regionName, @RequestParam("districtName") String districtName,
            @RequestParam("ulbGrade") String ulbGrade, @RequestParam("ulbCode") String ulbCode, @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate, @RequestParam("type") String type, @RequestParam("propertyType") String propertyType)
            throws IOException {
        CollectionDetailsRequest collectionDetailsRequest = new CollectionDetailsRequest();
        populateCollectionDetailsRequest(collectionDetailsRequest, regionName, districtName, ulbGrade, ulbCode, fromDate, toDate,
                type, propertyType);

        Long startTime = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDetailsRequest.getRegionName()
                + ", districtName = " + collectionDetailsRequest.getDistrictName() + ", ulbGrade = "
                + collectionDetailsRequest.getUlbGrade() + ", ulbCode = " + collectionDetailsRequest.getUlbCode()
                + ", fromDate = " + collectionDetailsRequest.getFromDate() + ", toDate = "
                + collectionDetailsRequest.getToDate() + ", type = " + collectionDetailsRequest.getType());
        CollectionDetails collectionDetails = propTaxDashboardService
                .getCollectionIndexDetails(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve targetmis is : " + timeTaken + MILLISECS);
        return collectionDetails;
    }

    private void populateCollectionDetailsRequest(CollectionDetailsRequest collectionDetailsRequest, String regionName,
            String districtName, String ulbGrade, String ulbCode,
            String fromDate, String toDate, String type, String propertyType) {
        collectionDetailsRequest.setRegionName(regionName);
        collectionDetailsRequest.setDistrictName(districtName);
        collectionDetailsRequest.setUlbGrade(ulbGrade);
        collectionDetailsRequest.setUlbCode(ulbCode);
        collectionDetailsRequest.setFromDate(fromDate);
        collectionDetailsRequest.setToDate(toDate);
        collectionDetailsRequest.setType(type);
        collectionDetailsRequest.setPropertyType(propertyType);
    }
    
    /**
     * Provides citywise DCB details across all ULBs for MIS Reports
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/citywisedcb", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MISDCBDetails getDCBDetailsForMIS(CollectionDetailsRequest collectionDetailsRequest)
            throws IOException {
        MISDCBDetails misDCBDetails = new MISDCBDetails();
        List<WeeklyDCB> weekwiseDCBDetails;
        List<MonthlyDCB> monthwiseDCBDetails;
        Long startTime = System.currentTimeMillis();
        if (StringUtils.isBlank(collectionDetailsRequest.getIntervalType())){
            misDCBDetails.setDcbDetails(propTaxDashboardService.getDCBDetails(collectionDetailsRequest));
        }
        else{ 
            String startDate;
            String endDate;
            if (WEEK.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())){
                String monthStartDateStr = collectionDetailsRequest.getYear().concat("-").concat(collectionDetailsRequest.getMonth()).concat("-").concat("01");
                LocalDate monthStDate = new LocalDate(monthStartDateStr);
                //Fetch the start date of the 1st week of the month and the last day of the month
                LocalDate weekStart = monthStDate.dayOfWeek().withMinimumValue();
                LocalDate endOfMonth = monthStDate.dayOfMonth().withMaximumValue();
                startDate = weekStart.toString(PropertyTaxConstants.DATE_FORMAT_YYYYMMDD);
                endDate = endOfMonth.toString(PropertyTaxConstants.DATE_FORMAT_YYYYMMDD);
                collectionDetailsRequest.setFromDate(startDate);
                collectionDetailsRequest.setToDate(endDate);
            }
        }
        
        if(WEEK.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())){
            weekwiseDCBDetails = propTaxDashboardService.getWeekwiseDCBDetails(collectionDetailsRequest,
                    collectionDetailsRequest.getIntervalType());
            misDCBDetails.setWeeklyDCBDetails(weekwiseDCBDetails);
        } else if (MONTH.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())) {
            monthwiseDCBDetails = propTaxDashboardService.getMonthwiseDCBDetails(collectionDetailsRequest,
                    collectionDetailsRequest.getIntervalType());
            misDCBDetails.setMonthlyDCBDetails(monthwiseDCBDetails);
        }
    
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve citywisedcb is : " + timeTaken + MILLISECS);
        return misDCBDetails;
    }
    
    /**
     * Provides collection analysis data across all ULBs for MIS Reports
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/collectionanalysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody public CollectionAnalysis getCollectionAnalysisForMIS(CollectionDetailsRequest collectionDetailsRequest) 
                    throws IOException {
        if(StringUtils.isNotBlank(collectionDetailsRequest.getIntervalType())){
            String startDate=StringUtils.EMPTY;
            String endDate=StringUtils.EMPTY;
            if(WEEK.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())){
                //Prepare the start date based on the month number and year
                String monthStartDateStr = collectionDetailsRequest.getYear().concat("-")
                        .concat(collectionDetailsRequest.getMonth()).concat("-").concat("01");
                LocalDate monthStDate = new LocalDate(monthStartDateStr);
                //Fetch the start date of the 1st week of the month and the last day of the month
                LocalDate weekStart = monthStDate.dayOfWeek().withMinimumValue();
                LocalDate endOfMonth = monthStDate.dayOfMonth().withMaximumValue();
                startDate = weekStart.toString(PropertyTaxConstants.DATE_FORMAT_YYYYMMDD);
                endDate = endOfMonth.toString(PropertyTaxConstants.DATE_FORMAT_YYYYMMDD);
            } else if(DAY.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())){
                //Prepare the first and last days of the week based on the month, year and week of month values
                DateTime date = new DateTime().withYear(Integer.parseInt(collectionDetailsRequest.getYear()))
                        .withMonthOfYear(Integer.parseInt(collectionDetailsRequest.getMonth()));
                Calendar cal = date.toCalendar(Locale.getDefault());
                cal.set(Calendar.DAY_OF_WEEK, 2);
                cal.set(Calendar.WEEK_OF_MONTH, Integer.parseInt(collectionDetailsRequest.getWeek()));
                DateTime weekStartDate = new DateTime(cal).withMillisOfDay(0);
                startDate = weekStartDate.toString(PropertyTaxConstants.DATE_FORMAT_YYYYMMDD);
                Date weekEndDate = DateUtils.addDays(weekStartDate.toDate(), 6);
                endDate = PropertyTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(weekEndDate);
            }
            if(WEEK.equalsIgnoreCase(collectionDetailsRequest.getIntervalType()) 
                    || DAY.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())){
                collectionDetailsRequest.setFromDate(startDate);
                collectionDetailsRequest.setToDate(endDate);
            }
        }

        Long startTime = System.currentTimeMillis();
        CollectionAnalysis collectionAnalysis = propTaxDashboardService.getCollectionAnalysisData(collectionDetailsRequest,
                collectionDetailsRequest.getIntervalType());
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve collectionanalysis is : " + timeTaken + MILLISECS);
        return collectionAnalysis;
    }
    
    /**
     * API provides Daily Target information across all cities
     * @param collectionDetailsRequest
     * @return CollectionDetails
     */
    @RequestMapping(value = "/dailytarget", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionDetails getDailyTargetDetails(
            @RequestParam("regionName") String regionName, @RequestParam("districtName") String districtName,
            @RequestParam("ulbGrade") String ulbGrade, @RequestParam("ulbCode") String ulbCode,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate, @RequestParam("type") String type,
            @RequestParam("propertyType") String propertyType)
            throws IOException {
        CollectionDetailsRequest collectionDetailsRequest = new CollectionDetailsRequest();
        populateCollectionDetailsRequest(collectionDetailsRequest, regionName, districtName, ulbGrade, ulbCode, fromDate, toDate,
                DASHBOARD_GROUPING_ULBWISE, propertyType);
        Long startTime = System.currentTimeMillis();
        CollectionDetails collectionDetails= propTaxDashboardService.getDailyTarget(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken to serve dailytarget is : " + timeTaken + MILLISECS);
        return collectionDetails;
    }

    

}