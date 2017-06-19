/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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

package org.egov.egf.web.controller.es.dashboard;

import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.egf.bean.dashboard.FinancialsBudgetDetailResponse;
import org.egov.egf.bean.dashboard.FinancialsDetailResponse;
import org.egov.egf.bean.dashboard.FinancialsDetailsRequest;
import org.egov.egf.es.utils.FinancialsDashBoardUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.services.es.dashboard.FinancialsDashboardService;
import org.egov.utils.FinancialConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = {"/public/findashboard", "/findashboard"})
public class FinancialsDashboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialsDashboardController.class);

    @Autowired
    private FinancialsDashboardService financialsDashboardService;

    @Autowired
    private CFinancialYearService cFinancialYearService;

    /**
     * Provides Financials voucher Index details across all ULBs
     *
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FinancialsDetailResponse> getFinancialDetails(final FinancialsDetailsRequest financialsDetailsRequest)
            throws IOException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("financialsDetailsRequest input : regionName = " + financialsDetailsRequest.getRegion()
                    + ", districtName = " + financialsDetailsRequest.getDistrict() + ", ulbGrade = "
                    + financialsDetailsRequest.getGrade() + ", ulbCode = " + financialsDetailsRequest.getUlbCode()
                    + ", fromDate = " + financialsDetailsRequest.getFromDate() + ", toDate = "
                    + financialsDetailsRequest.getToDate() + ", aggregationlevel = "
                    + financialsDetailsRequest.getAggregationLevel());
        setAsOnDate(financialsDetailsRequest);
        final BoolQueryBuilder boolQuery = FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest);
        final String aggrField = FinancialsDashBoardUtils.getAggregationGroupingField(financialsDetailsRequest);
        return financialsDashboardService.getFinancialsData(financialsDetailsRequest, boolQuery, aggrField);
    }

    private void setAsOnDate(final FinancialsDetailsRequest financialsDetailsRequest) {

        CFinancialYear financialYear;
        if (financialsDetailsRequest.getToDate() != null) {
            financialYear = cFinancialYearService
                    .getFinancialYearByDate(DateUtils.getDate(financialsDetailsRequest.getToDate(), "yyyy-MM-dd"));
            financialsDetailsRequest
                    .setFromDate(FinancialConstants.DATEFORMATTER_YYYY_MM_DD.format(financialYear.getStartingDate()));
            financialsDetailsRequest.setCurrentFinancialYear(financialYear.getFinYearRange());
            financialsDetailsRequest.setLastFinancialYear(cFinancialYearService
                    .getPreviousFinancialYearForDate(DateUtils.getDate(financialsDetailsRequest.getToDate(), "yyyy-MM-dd"))
                    .getFinYearRange());
        } else {
            financialYear = cFinancialYearService.getFinancialYearByDate(DateUtils.now());
            financialsDetailsRequest.setToDate(FinancialConstants.DATEFORMATTER_YYYY_MM_DD.format(DateUtils.now()));
            financialsDetailsRequest
                    .setFromDate(FinancialConstants.DATEFORMATTER_YYYY_MM_DD.format(financialYear.getStartingDate()));
            financialsDetailsRequest.setCurrentFinancialYear(financialYear.getFinYearRange());
            financialsDetailsRequest.setLastFinancialYear(cFinancialYearService
                    .getPreviousFinancialYearForDate(DateUtils.now()).getFinYearRange());
        }

    }

    @RequestMapping(value = "/statewisebudget", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FinancialsBudgetDetailResponse> getFinDetails(final FinancialsDetailsRequest financialsDetailsRequest)
            throws IOException {
        setAsOnDate(financialsDetailsRequest);
        final BoolQueryBuilder boolQuery = FinancialsDashBoardUtils.prepareWhereClauseForBudget(financialsDetailsRequest);
        final String aggrField = FinancialsDashBoardUtils.getAggregationGroupingFieldForBudget(financialsDetailsRequest);
        return financialsDashboardService.getBudgetData(financialsDetailsRequest, boolQuery, aggrField);
    }

}
