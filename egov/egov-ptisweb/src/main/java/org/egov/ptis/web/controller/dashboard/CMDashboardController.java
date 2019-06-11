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

package org.egov.ptis.web.controller.dashboard;

import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_YYYYMMDD;
import static org.egov.ptis.constants.PropertyTaxConstants.DAY;
import static org.egov.ptis.constants.PropertyTaxConstants.MONTH;
import static org.egov.ptis.constants.PropertyTaxConstants.WEEK;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.bean.dashboard.CollReceiptDetails;
import org.egov.ptis.bean.dashboard.CollectionAnalysis;
import org.egov.ptis.bean.dashboard.CollectionDetails;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.DemandVariance;
import org.egov.ptis.bean.dashboard.MISDCBDetails;
import org.egov.ptis.bean.dashboard.MonthlyDCB;
import org.egov.ptis.bean.dashboard.PropertyTaxDefaultersRequest;
import org.egov.ptis.bean.dashboard.StateCityInfo;
import org.egov.ptis.bean.dashboard.TaxDefaulters;
import org.egov.ptis.bean.dashboard.TaxPayerDetails;
import org.egov.ptis.bean.dashboard.TaxPayerResponseDetails;
import org.egov.ptis.bean.dashboard.TotalCollectionStats;
import org.egov.ptis.bean.dashboard.WeeklyDCB;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.service.dashboard.PropTaxDashboardService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to provide services for CM Dashboard
 */

@RestController
@RequestMapping(value = { "/public/dashboard", "/dashboard" })
public class CMDashboardController {

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
    public List<StateCityInfo> getStateCityInformation() {
        return propTaxDashboardService.getStateCityDetails();
    }

    /**
     * Provides State-wise Collection Statistics for Property Tax, Water Charges
     * and Others
     * 
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/collectionstats", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TotalCollectionStats getConsolidatedCollDetails(final HttpServletRequest request) {
        return propTaxDashboardService.getTotalCollectionStats(request);

    }

    /**
     * Provides Collection Index details across all ULBs
     * 
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/collectiondashboard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionDetails getCollectionDetails(@RequestBody CollectionDetailsRequest collectionDetailsRequest) {
        return propTaxDashboardService
                .getCollectionIndexDetails(collectionDetailsRequest, false);
    }

    /**
     * Gives the receipts details across all ULBs
     * 
     * @param collectionDetailsRequest
     * @return CollReceiptDetails
     */
    @RequestMapping(value = "/receipttransactions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollReceiptDetails getReceiptTransactions(@RequestBody CollectionDetailsRequest collectionDetailsRequest) {
        return propTaxDashboardService.getReceiptDetails(collectionDetailsRequest);
    }

    /**
     * Returns Top Ten Tax Payers Across all ULB's
     * 
     * @param collDetailsRequestStr
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/toptentaxers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxPayerResponseDetails getTopTenTaxProducers(@RequestBody CollectionDetailsRequest collectionDetailsRequest) {
        return propTaxDashboardService
                .getTopTenTaxProducers(collectionDetailsRequest);
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
            @RequestBody CollectionDetailsRequest collectionDetailsRequest) {
        return propTaxDashboardService
                .getBottomTenTaxProducers(collectionDetailsRequest);
    }

    @RequestMapping(value = "/topdefaulters", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaxDefaulters> getTopTaxDefaulters(
            @RequestBody PropertyTaxDefaultersRequest propertyTaxDefaultersRequest) {
        return propTaxDashboardService.getTaxDefaulters(propertyTaxDefaultersRequest);
    }
    
    /**
     * Provides Collection Index details across all ULBs for MIS Reports
     * 
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/targetmis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CollectionDetails getCollectionDetailsForTargetMIS(CollectionDetailsRequest collectionDetailsRequest) {
        return propTaxDashboardService
                .getCollectionIndexDetails(collectionDetailsRequest, true);
    }
    
    /**
     * Provides citywise DCB details across all ULBs for MIS Reports
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/citywisedcb", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MISDCBDetails getDCBDetailsForMIS(CollectionDetailsRequest collectionDetailsRequest) {
        MISDCBDetails misDCBDetails = new MISDCBDetails();
        List<WeeklyDCB> weekwiseDCBDetails;
        List<MonthlyDCB> monthwiseDCBDetails;
        if (StringUtils.isBlank(collectionDetailsRequest.getIntervalType()))
            misDCBDetails.setDcbDetails(propTaxDashboardService.getDCBDetails(collectionDetailsRequest));
        else {
            String startDate;
            String endDate;
            if (WEEK.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())) {
                String monthStartDateStr = collectionDetailsRequest.getYear().concat("-")
                        .concat(collectionDetailsRequest.getMonth()).concat("-").concat("01");
                LocalDate monthStDate = new LocalDate(monthStartDateStr);
                // Fetch the start date of the 1st week of the month and the last day of the month
                LocalDate weekStart = monthStDate.dayOfWeek().withMinimumValue();
                LocalDate endOfMonth = monthStDate.dayOfMonth().withMaximumValue();
                startDate = weekStart.toString(PropertyTaxConstants.DATE_FORMAT_YYYYMMDD);
                endDate = endOfMonth.toString(PropertyTaxConstants.DATE_FORMAT_YYYYMMDD);
                collectionDetailsRequest.setFromDate(startDate);
                collectionDetailsRequest.setToDate(endDate);
            }
        }
        if (WEEK.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())) {
            weekwiseDCBDetails = propTaxDashboardService.getWeekwiseDCBDetails(collectionDetailsRequest,
                    collectionDetailsRequest.getIntervalType());
            misDCBDetails.setWeeklyDCBDetails(weekwiseDCBDetails);
        } else if (MONTH.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())) {
            monthwiseDCBDetails = propTaxDashboardService.getMonthwiseDCBDetails(collectionDetailsRequest,
                    collectionDetailsRequest.getIntervalType());
            misDCBDetails.setMonthlyDCBDetails(monthwiseDCBDetails);
        }
        return misDCBDetails;
    }
    
    /**
     * Provides collection analysis data across all ULBs for MIS Reports
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/collectionanalysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody public CollectionAnalysis getCollectionAnalysisForMIS(CollectionDetailsRequest collectionDetailsRequest) {
        if (StringUtils.isNotBlank(collectionDetailsRequest.getIntervalType())) {
            String startDate = StringUtils.EMPTY;
            String endDate = StringUtils.EMPTY;
            if (WEEK.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())) {
                // Prepare the start date based on the month number and year
                String monthStartDateStr = collectionDetailsRequest.getYear().concat("-")
                        .concat(collectionDetailsRequest.getMonth()).concat("-").concat("01");
                LocalDate monthStDate = new LocalDate(monthStartDateStr);
                // Fetch the start date of the 1st week of the month and the last day of the month
                LocalDate weekStart = monthStDate.dayOfWeek().withMinimumValue();
                LocalDate endOfMonth = monthStDate.dayOfMonth().withMaximumValue();
                startDate = weekStart.toString(PropertyTaxConstants.DATE_FORMAT_YYYYMMDD);
                endDate = endOfMonth.toString(PropertyTaxConstants.DATE_FORMAT_YYYYMMDD);
            } else if (DAY.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())) {
                // Prepare the first and last days of the week based on the month, year and week of month values
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
                DateTime date = new DateTime().withYear(Integer.parseInt(collectionDetailsRequest.getYear()))
                        .withMonthOfYear(Integer.parseInt(collectionDetailsRequest.getMonth()));
                Calendar cal = date.toCalendar(Locale.getDefault());
                cal.set(Calendar.DAY_OF_WEEK, 2);
                cal.set(Calendar.WEEK_OF_MONTH, Integer.parseInt(collectionDetailsRequest.getWeek()));
                DateTime weekStartDate = new DateTime(cal).withMillisOfDay(0);
                startDate = weekStartDate.toString(PropertyTaxConstants.DATE_FORMAT_YYYYMMDD);
                Date weekEndDate = DateUtils.addDays(weekStartDate.toDate(), 6);
                endDate = dateFormat.format(weekEndDate);
            }
            if (WEEK.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())
                    || DAY.equalsIgnoreCase(collectionDetailsRequest.getIntervalType())) {
                collectionDetailsRequest.setFromDate(startDate);
                collectionDetailsRequest.setToDate(endDate);
            }
        }
        return propTaxDashboardService.getCollectionAnalysisData(collectionDetailsRequest,
                collectionDetailsRequest.getIntervalType());
    }
    
    /**
     * API provides Daily Target information across all cities
     * @param collectionDetailsRequest
     * @return CollectionDetails
     */
    @RequestMapping(value = "/dailytarget", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionDetails getDailyTargetDetails(CollectionDetailsRequest collectionDetailsRequest) {
        return propTaxDashboardService.getDailyTarget(collectionDetailsRequest);
    }

    @RequestMapping(value = "/demanddetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DemandVariance> getDemandDetails(CollectionDetailsRequest collectionDetailsRequest) {
        return propTaxDashboardService.getDemandVariationDetails(collectionDetailsRequest);

    }
    
    /**
     * API to fetch top and bottom 10 achievers based on the type - billcollector/revenueinspector/revenueofficer
     * @param collectionDetailsRequest
     * @return map of top and bottom 10 achievers
     */
    @RequestMapping(value = "/ptcollectionranking", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<TaxPayerDetails>> getRankings(CollectionDetailsRequest collectionDetailsRequest){
        return propTaxDashboardService.getCollectionRankings(collectionDetailsRequest);
    }
    
    
}