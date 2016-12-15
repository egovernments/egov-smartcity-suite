
/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.collection.web.controller.dashboard;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.collection.bean.dashboard.CollectionDashBoardRequest;
import org.egov.collection.bean.dashboard.TaxPayerDashBoardResponseDetails;
import org.egov.collection.bean.dashboard.TotalCollectionDashBoardStats;
import org.egov.collection.bean.dashboard.TotalCollectionStatistics;
import org.egov.collection.service.dashboard.CollectionDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/public/dashboard", "/dashboard" })
public class CollectionDashboardController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionDashboardController.class);

    @Autowired
    private CollectionDashboardService collectionDashboardService;

    /**
     * Provides State-wise Collection Statistics for Property Tax, Water Charges
     * and Other Revenue
     *
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/otherrevenuecollectionstats", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TotalCollectionDashBoardStats getConsolidatedCollDetails(final HttpServletRequest request)
            throws IOException {
        final Long startTime = System.currentTimeMillis();
        final TotalCollectionDashBoardStats consolidatedCollectionDetails = collectionDashboardService
                .getTotalCollectionStats(request);
        final Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve collectionstats is : " + timeTaken + " (millisecs)");
        return consolidatedCollectionDetails;

    }

    /**
     * Provides Collection Index details across all ULBs
     *
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/otherrevenuecollectiondashboard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TotalCollectionStatistics> getCollectionDetails(
            @RequestBody final CollectionDashBoardRequest collectionDetailsRequest) throws IOException {
        final Long startTime = System.currentTimeMillis();
        LOGGER.debug("CollectionDashBoardRequest input : regionName = " + collectionDetailsRequest.getRegionName()
                + ", districtName = " + collectionDetailsRequest.getDistrictName() + ", ulbGrade = "
                + collectionDetailsRequest.getUlbGrade() + ", ulbCode = " + collectionDetailsRequest.getUlbCode()
                + ", fromDate = " + collectionDetailsRequest.getFromDate() + ", toDate = "
                + collectionDetailsRequest.getToDate() + ", type = " + collectionDetailsRequest.getType());
        final List<TotalCollectionStatistics> collectionDetails = collectionDashboardService
                .getCollectionIndexDetails(collectionDetailsRequest);
        final Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve collectiondashboard is : " + timeTaken + " (millisecs)");
        return collectionDetails;
    }

    /**
     * Returns Top Ten Tax Performers Across all ULB's
     *
     * @param collDetailsRequestStr
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/otherrevenuetoptencollection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxPayerDashBoardResponseDetails getTopTenTaxProducers(
            @RequestBody final CollectionDashBoardRequest collectionDashBoardRequest) throws IOException {
        final Long startTime = System.currentTimeMillis();
        LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDashBoardRequest.getRegionName()
                + ", districtName = " + collectionDashBoardRequest.getDistrictName() + ", ulbGrade = "
                + collectionDashBoardRequest.getUlbGrade() + ", ulbCode = " + collectionDashBoardRequest.getUlbCode()
                + ", fromDate = " + collectionDashBoardRequest.getFromDate() + ", toDate = "
                + collectionDashBoardRequest.getToDate() + ", type = " + collectionDashBoardRequest.getType());
        final TaxPayerDashBoardResponseDetails taxPayerDetails = collectionDashboardService
                .getTopTenTaxProducers(collectionDashBoardRequest);
        final Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve toptentaxers is : " + timeTaken + " (millisecs)");
        return taxPayerDetails;
    }

    /**
     * Returns Top Ten Tax Performers Across all ULB's
     *
     * @param collDetailsRequestStr
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/otherrevenuebottomtencollection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxPayerDashBoardResponseDetails getBottomTenTaxProducers(
            @RequestBody final CollectionDashBoardRequest collectionDashBoardRequest) throws IOException {
        final Long startTime = System.currentTimeMillis();
        LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDashBoardRequest.getRegionName()
                + ", districtName = " + collectionDashBoardRequest.getDistrictName() + ", ulbGrade = "
                + collectionDashBoardRequest.getUlbGrade() + ", ulbCode = " + collectionDashBoardRequest.getUlbCode()
                + ", fromDate = " + collectionDashBoardRequest.getFromDate() + ", toDate = "
                + collectionDashBoardRequest.getToDate() + ", type = " + collectionDashBoardRequest.getType());
        final TaxPayerDashBoardResponseDetails taxPayerDetails = collectionDashboardService
                .getBottomTenTaxProducers(collectionDashBoardRequest);
        final Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve bottomtentaxers is : " + timeTaken + " (millisecs)");
        return taxPayerDetails;
    }

}