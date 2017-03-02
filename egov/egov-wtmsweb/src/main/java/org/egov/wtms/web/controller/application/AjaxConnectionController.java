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
package org.egov.wtms.web.controller.application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.NewConnectionService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.ApplicationProcessTime;
import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.entity.DonationDetails;
import org.egov.wtms.masters.entity.DonationHeader;
import org.egov.wtms.masters.entity.PipeSize;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.entity.WaterRatesDetails;
import org.egov.wtms.masters.entity.WaterRatesHeader;
import org.egov.wtms.masters.entity.WaterSource;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.masters.service.DonationDetailsService;
import org.egov.wtms.masters.service.DonationHeaderService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterRatesDetailsService;
import org.egov.wtms.masters.service.WaterRatesHeaderService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;

@Controller
public class AjaxConnectionController {

    @Autowired
    private NewConnectionService newConnectionService;

    @Autowired
    private DonationHeaderService donationHeaderService;

    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private WaterRatesHeaderService waterRatesHeaderService;

    @Autowired
    private WaterRatesDetailsService waterRatesDetailsService;

    @Autowired
    private PipeSizeService pipeSizeService;

    @Autowired
    private ConnectionCategoryService connectionCategoryService;

    @Autowired
    private DonationDetailsService donationDetailsService;

    @Autowired
    private ApplicationProcessTimeService applicationProcessTimeService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @RequestMapping(value = "/ajaxconnection/check-primaryconnection-exists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String isConnectionPresentForProperty(@RequestParam final String propertyID) {
        return newConnectionService.checkConnectionPresentForProperty(propertyID);
    }

    @RequestMapping(value = "/ajaxconnection/assignmentByPositionId", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getWorkFlowPositionByDepartmentAndDesignation(
            @RequestParam final Long approvalPositionId, final HttpServletResponse response) {
        return waterConnectionDetailsService.getApprovalPositionOnValidate(approvalPositionId);
    }

    @RequestMapping(value = "/ajax-CategoryTypeByPropertyType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ConnectionCategory> getAllCategoryTypesByPropertyType(
            @RequestParam final Long propertyType, @RequestParam final String connectionType) {
        List<ConnectionCategory> categoryTypes;
        categoryTypes = connectionCategoryService.getAllActiveCategoryTypesByPropertyType(propertyType, connectionType);
        categoryTypes.forEach(categoryType -> categoryType.toString());
        return categoryTypes;
    }

    @RequestMapping(value = "/ajax-UsageTypeByPropertyType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UsageType> getAllUsageTypesByPropertyType(@RequestParam final Long propertyType) {
        List<UsageType> usageTypes;
        usageTypes = usageTypeService.getAllActiveUsageTypesByPropertyType(propertyType);
        usageTypes.forEach(usageType -> usageType.toString());
        return usageTypes;
    }

    @RequestMapping(value = "/ajax-PipeSizesByPropertyType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PipeSize> getAllPipeSizesByPropertyType(@RequestParam final Long propertyType) {
        List<PipeSize> pipesizes;
        pipesizes = pipeSizeService.getAllPipeSizesByPropertyType(propertyType);
        pipesizes.forEach(pipesize -> pipesize.toString());
        return pipesizes;
    }

    /**
     * @param givenDate
     * @param requestConsumerCode
     * @return True or False Based on Entered Date's Month Installment Meter Entry is present
     */
    @RequestMapping(value = "/ajax-meterReadingEntryExist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boolean getMeterReadingEntryExist(
            @ModelAttribute("waterConnectionDetails") @RequestParam final Date givenDate,
            @RequestParam final String requestConsumerCode) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumberOrConsumerCode(requestConsumerCode);
        return connectionDemandService.meterEntryAllReadyExistForCurrentMonth(
                waterConnectionDetails, givenDate);
    }

    @RequestMapping(value = "/ajax-consumerCodeExistFordataEntry", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boolean validateconsumerNumberForDataEntry(
            @ModelAttribute("waterConnectionDetails") @RequestParam final String consumerCode) {
        Boolean enteredMonthReadingExist = Boolean.FALSE;
        if (consumerCode != null) {
            final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumberOrConsumerCode(consumerCode);

            if (waterConnectionDetails != null
                    && waterConnectionDetails.getConnection().getConsumerCode().equals(consumerCode))
                enteredMonthReadingExist = Boolean.TRUE;
        }
        return enteredMonthReadingExist;
    }

    @RequestMapping(value = "/ajax-donationheadercombination", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getDonationAmountByAllCombinatons(@RequestParam final PropertyType propertyType,
            @RequestParam final ConnectionCategory categoryType, @RequestParam final UsageType usageType,
            @RequestParam final Long maxPipeSize, @RequestParam final Long minPipeSize,
            @RequestParam final Date fromDate, @RequestParam final Date toDate, @RequestParam final Boolean activeid) {
        final SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        final PipeSize minPipesizeObj = pipeSizeService.findOne(minPipeSize);
        final PipeSize maxPipesizeObj = pipeSizeService.findOne(maxPipeSize);
        final List<DonationHeader> donationHeaderTempList = donationHeaderService
                .findDonationDetailsByPropertyAndCategoryAndUsageandPipeSize(propertyType, categoryType, usageType,
                        minPipesizeObj.getSizeInInch(), maxPipesizeObj.getSizeInInch());
        DonationDetails donationDetails = null;
        if (!donationHeaderTempList.isEmpty())
            for (final DonationHeader donationHeaderTemp : donationHeaderTempList) {
                donationDetails = donationDetailsService.findByDonationHeaderAndFromDateAndToDate(donationHeaderTemp,
                        fromDate, toDate);
                if (donationDetails != null)
                    break;
            }
        if (donationDetails != null && donationDetails.getDonationHeader().isActive() == activeid) {
            final JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("maxPipeSize", donationDetails.getDonationHeader().getMaxPipeSize().getCode());
            jsonObj.addProperty("minPipeSize", donationDetails.getDonationHeader().getMinPipeSize().getCode());
            jsonObj.addProperty("fromDate", dateformat.format(donationDetails.getFromDate()).toString());
            jsonObj.addProperty("toDate", dateformat.format(donationDetails.getToDate()).toString());
            return jsonObj.toString();
        } else
            return "";
    }

    @RequestMapping(value = "/ajax-minimumpipesizeininch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public double getMinimumPipeSizeInInch(@RequestParam final Long minPipeSize) {
        final PipeSize minPipesizeObj = pipeSizeService.findOne(minPipeSize);
        return minPipesizeObj.getSizeInInch();
    }

    @RequestMapping(value = "/ajax-maximumpipesizeininch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public double getMaximumPipeSizeInInch(@RequestParam final Long maxPipeSize) {
        final PipeSize maxPipesizeObj = pipeSizeService.findOne(maxPipeSize);
        return maxPipesizeObj.getSizeInInch();
    }

    @RequestMapping(value = "/ajax-WaterRatescombination", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String geWaterRatesByAllCombinatons(
            @ModelAttribute("waterRatesHeader") @RequestParam final ConnectionType categoryType,
            @RequestParam final WaterSource waterSource, @RequestParam final UsageType usageType,
            @RequestParam final PipeSize pipeSize, @RequestParam final Date fromDate, @RequestParam final Date toDate,
            @RequestParam final Boolean activeid) {
        final SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        final List<WaterRatesHeader> waterRatesHeaderList = waterRatesHeaderService
                .findByConnectionTypeAndUsageTypeAndWaterSourceAndPipeSize(categoryType, usageType, waterSource,
                        pipeSize);
        WaterRatesDetails waterRatesDetails = null;
        if (!waterRatesHeaderList.isEmpty())
            for (final WaterRatesHeader waterRatesHeaderTemp : waterRatesHeaderList) {
                waterRatesDetails = waterRatesDetailsService.findByWaterRatesHeaderAndFromDateAndToDate(
                        waterRatesHeaderTemp, fromDate, toDate);
                if (waterRatesDetails != null)
                    break;
            }
        if (waterRatesDetails != null && waterRatesDetails.getWaterRatesHeader().isActive() == activeid) {
            final JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("fromDate", dateformat.format(waterRatesDetails.getFromDate()).toString());
            jsonObj.addProperty("toDate", dateformat.format(waterRatesDetails.getToDate()).toString());
            return jsonObj.toString();
        } else
            return "";
    }

    @RequestMapping(value = "/ajax-getapplicationprocesstime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public double getApplicationProcessTime(@RequestParam final Long applicationType,
            @RequestParam final Long categoryType) {
        ApplicationProcessTime applicationprocessTime;
        applicationprocessTime = applicationProcessTimeService.findByApplicationTypeandCategory(
                applicationTypeService.findBy(applicationType), connectionCategoryService.findOne(categoryType));
        if (applicationprocessTime == null)
            return 0;
        else
            return applicationprocessTime.getProcessingTime();
    }

    @RequestMapping(value = "/ajax-isdonationamount-editable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean isDonationChargesEditAllowed(@RequestParam final String connectionType) {
        List<AppConfigValues> appConfigValues;
        if (connectionType.equalsIgnoreCase(WaterTaxConstants.METERED))
            appConfigValues = waterTaxUtils.getAppConfigValueByModuleNameAndKeyName(WaterTaxConstants.MODULE_NAME,
                    WaterTaxConstants.IS_METEREDDONATIONAMOUNT_MANUAL);
        else
            appConfigValues = waterTaxUtils.getAppConfigValueByModuleNameAndKeyName(WaterTaxConstants.MODULE_NAME,
                    WaterTaxConstants.IS_NONMETEREDDONATIONAMOUNT_MANUAL);

        if (appConfigValues != null && !appConfigValues.isEmpty())
            return WaterTaxConstants.APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfigValues.get(0).getValue());
        else
            return false;
    }
    
    @RequestMapping(value = "/ajax-getPropertyIdByConsumerCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public  String getPropertyIdentifier(@RequestParam final String consumerCode) {
       final  WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(consumerCode,ConnectionStatus.ACTIVE);
        if (waterConnectionDetails == null)
            return "";
        else
            return waterConnectionDetails.getConnection().getPropertyIdentifier();
    }

}