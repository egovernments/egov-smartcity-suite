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

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.dcb.bean.ChequePayment;
import org.egov.ptis.bean.dashboard.CollReceiptDetails;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.CollectionDetails;
import org.egov.ptis.bean.dashboard.TotalCollectionStats;
import org.egov.ptis.bean.dashboard.PropertyTaxDefaultersRequest;
import org.egov.ptis.bean.dashboard.StateCityInfo;
import org.egov.ptis.bean.dashboard.TaxDefaulters;
import org.egov.ptis.bean.dashboard.TaxPayerResponseDetails;
import org.egov.ptis.service.dashboard.PropTaxDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to provide services for CM Dashboard
 */

@RestController
@RequestMapping(value = { "/public/dashboard", "/dashboard" })
public class CMDashboardController {
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
        LOGGER.debug("Time taken to serve statecityinfo is : " + timeTaken / 1000 + " (secs)");
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
    public TotalCollectionStats getConsolidatedCollDetails() throws IOException {
        Long startTime = System.currentTimeMillis();
        TotalCollectionStats consolidatedCollectionDetails = propTaxDashboardService.getTotalCollectionStats();
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve collectionstats is : " + timeTaken / 1000 + " (secs)");
        return consolidatedCollectionDetails;

    }

    /**
     * Provides Collection Index details across all ULBs
     * 
     * @return response JSON
     * @throws IOException
     */
    @RequestMapping(value = "/collectiondashboard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionDetails getCollectionDetails(@RequestBody String collDetailsRequestStr) throws IOException {
        Long startTime = System.currentTimeMillis();
        CollectionDetailsRequest collectionDetailsRequest = (CollectionDetailsRequest) getObjectFromJSONRequest(
                collDetailsRequestStr, CollectionDetailsRequest.class);
        LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDetailsRequest.getRegionName()
                + ", districtName = " + collectionDetailsRequest.getDistrictName() + ", ulbGrade = "
                + collectionDetailsRequest.getUlbGrade() + ", ulbCode = " + collectionDetailsRequest.getUlbCode()
                + ", fromDate = " + collectionDetailsRequest.getFromDate() + ", toDate = "
                + collectionDetailsRequest.getToDate());
        CollectionDetails collectionDetails = propTaxDashboardService
                .getCollectionIndexDetails(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve collectiondashboard is : " + timeTaken / 1000 + " (secs)");
        return collectionDetails;
    }

    /**
     * Gives the receipts details across all ULBs
     * 
     * @param collectionDetailsRequest
     * @return CollReceiptDetails
     */
    @RequestMapping(value = "/receipttransactions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollReceiptDetails getReceiptTransactions(@RequestBody String rcptDetailsRequestStr) throws IOException {
        Long startTime = System.currentTimeMillis();
        CollectionDetailsRequest collectionDetailsRequest = (CollectionDetailsRequest) getObjectFromJSONRequest(
                rcptDetailsRequestStr, CollectionDetailsRequest.class);
        LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDetailsRequest.getRegionName()
                + ", districtName = " + collectionDetailsRequest.getDistrictName() + ", ulbGrade = "
                + collectionDetailsRequest.getUlbGrade() + ", ulbCode = " + collectionDetailsRequest.getUlbCode()
                + ", fromDate = " + collectionDetailsRequest.getFromDate() + ", toDate = "
                + collectionDetailsRequest.getToDate());
        CollReceiptDetails collReceiptDetails = propTaxDashboardService.getReceiptDetails(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve receipttransactions is : " + timeTaken / 1000 + " (secs)");
        return collReceiptDetails;
    }

    /**
     * Returns Top Ten Tax Performers Across all ULB's
     * 
     * @param collDetailsRequestStr
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/toptentaxers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxPayerResponseDetails getTopTenTaxProducers(@RequestBody String collDetailsRequestStr) throws IOException {
        Long startTime = System.currentTimeMillis();
        CollectionDetailsRequest collectionDetailsRequest = (CollectionDetailsRequest) getObjectFromJSONRequest(
                collDetailsRequestStr, CollectionDetailsRequest.class);
        LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDetailsRequest.getRegionName()
                + ", districtName = " + collectionDetailsRequest.getDistrictName() + ", ulbGrade = "
                + collectionDetailsRequest.getUlbGrade() + ", ulbCode = " + collectionDetailsRequest.getUlbCode()
                + ", fromDate = " + collectionDetailsRequest.getFromDate() + ", toDate = "
                + collectionDetailsRequest.getToDate());
        TaxPayerResponseDetails taxPayerDetails = propTaxDashboardService
                .getTopTenTaxProducers(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve toptentaxers is : " + timeTaken / 1000 + " (secs)");
        return taxPayerDetails;
    }

    /**
     * Returns Top Ten Tax Performers Across all ULB's
     * 
     * @param collDetailsRequestStr
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/bottomtentaxers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaxPayerResponseDetails getBottomTenTaxProducers(@RequestBody String collDetailsRequestStr)
            throws IOException {
        Long startTime = System.currentTimeMillis();
        CollectionDetailsRequest collectionDetailsRequest = (CollectionDetailsRequest) getObjectFromJSONRequest(
                collDetailsRequestStr, CollectionDetailsRequest.class);
        LOGGER.debug("CollectionDetailsRequest input : regionName = " + collectionDetailsRequest.getRegionName()
                + ", districtName = " + collectionDetailsRequest.getDistrictName() + ", ulbGrade = "
                + collectionDetailsRequest.getUlbGrade() + ", ulbCode = " + collectionDetailsRequest.getUlbCode()
                + ", fromDate = " + collectionDetailsRequest.getFromDate() + ", toDate = "
                + collectionDetailsRequest.getToDate());
        TaxPayerResponseDetails taxPayerDetails = propTaxDashboardService
                .getBottomTenTaxProducers(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve bottomtentaxers is : " + timeTaken / 1000 + " (secs)");
        return taxPayerDetails;
    }

    @RequestMapping(value = "/topdefaulters", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaxDefaulters> getTopTaxDefaulters(@RequestBody String taxDefaultersRequestStr) throws IOException {
        Long startTime = System.currentTimeMillis();
        PropertyTaxDefaultersRequest propertyTaxDefaultersRequest = (PropertyTaxDefaultersRequest) getObjectFromJSONRequest(
                taxDefaultersRequestStr, PropertyTaxDefaultersRequest.class);
        LOGGER.debug("PropertyTaxDefaultersRequest input : regionName = " + propertyTaxDefaultersRequest.getRegionName()
                + ", districtName = " + propertyTaxDefaultersRequest.getDistrictName() + ", ulbName = "
                + propertyTaxDefaultersRequest.getUlbName() + ", ulbCode = " + propertyTaxDefaultersRequest.getUlbCode()
                + ", wardName = " + propertyTaxDefaultersRequest.getWardName());
        List<TaxDefaulters> taxDefaulters = propTaxDashboardService.getTaxDefaulters(propertyTaxDefaultersRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken to serve topdefaulters is : " + timeTaken / 1000 + " (secs)");
        return taxDefaulters;
    }

    /**
     * This method is used to get POJO object from JSON request.
     * 
     * @param jsonString
     *            - request JSON string
     * @return
     * @throws IOException
     */
    private Object getObjectFromJSONRequest(String jsonString, Class cls) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
        mapper.setDateFormat(ChequePayment.CHEQUE_DATE_FORMAT);
        return mapper.readValue(jsonString, cls);
    }

}