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

package org.egov.wtms.web.controller.search;

import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CHANGEOFUSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CLOSINGCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CONNECTIONTYPE_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DATAENTRYEDIT;
import static org.egov.wtms.utils.constants.WaterTaxConstants.EDITCOLLECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.GENERATEBILL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PERMENENTCLOSECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTIONCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SEARCH_MENUTREE_APPLICATIONTYPE_CLOSURE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SEARCH_MENUTREE_APPLICATIONTYPE_COLLECTTAX;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SEARCH_MENUTREE_APPLICATIONTYPE_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERCHARGES_CONSUMERCODE;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.entity.es.ConnectionSearchRequest;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/search/waterSearch/")
public class CommonWaterTaxSearchController {

    private static final String COMMON_FORM_SEARCH = "waterTaxSearch-commonForm";
    private static final String INVALID_CONSUMERNUMBER = "invalid.consumernumber";
    private static final String APPLICATION_NUMBER = "applicationNo";
    private static final String MEESEVA_APPLICATION_NUMBER = "meesevaApplicationNumber";
    private static final String ERROR_MODE = "errorMode";
    private static final String CONNECTION_CLOSED = "connection.closed";
    private static final String MODE = "mode";
    private static final String APPLICATIONTYPE = "applicationType";

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @ModelAttribute
    public ConnectionSearchRequest searchRequest() {
        return new ConnectionSearchRequest();
    }

    @RequestMapping(value = "commonSearch/meterentry", method = RequestMethod.GET)
    public String addMeterEntry(final Model model, final HttpServletRequest request) {
        final String meesevaApplicationNumber = request.getParameter(APPLICATION_NUMBER);
        return commonSearchForm(model, SEARCH_MENUTREE_APPLICATIONTYPE_METERED, meesevaApplicationNumber);
    }

    @RequestMapping(value = "commonSearch/closureconnection", method = RequestMethod.GET)
    public String closeWaterConnection(final Model model, final HttpServletRequest request) {
        final String meesevaApplicationNumber = request.getParameter(APPLICATION_NUMBER);
        return commonSearchForm(model, SEARCH_MENUTREE_APPLICATIONTYPE_CLOSURE, meesevaApplicationNumber);
    }

    @RequestMapping(value = "commonSearch/changeofuse", method = RequestMethod.GET)
    public String waterConnectionChangeOfUsage(final Model model, final HttpServletRequest request) {
        final String meesevaApplicationNumber = request.getParameter(APPLICATION_NUMBER);
        return commonSearchForm(model, CHANGEOFUSE, meesevaApplicationNumber);
    }

    @RequestMapping(value = "commonSearch/additionalconnection", method = RequestMethod.GET)
    public String getAdditionalWaterConnection(final Model model, final HttpServletRequest request) {
        final String meesevaApplicationNumber = request.getParameter(APPLICATION_NUMBER);
        return commonSearchForm(model, ADDNLCONNECTION, meesevaApplicationNumber);
    }

    @RequestMapping(value = "commonSearch/collecttax", method = RequestMethod.GET)
    public String collectTax(final Model model, final HttpServletRequest request) {
        final String meesevaApplicationNumber = request.getParameter(APPLICATION_NUMBER);
        return commonSearchForm(model, SEARCH_MENUTREE_APPLICATIONTYPE_COLLECTTAX, meesevaApplicationNumber);
    }

    @RequestMapping(value = "commonSearch/reconnection", method = RequestMethod.GET)
    public String getReconnectionForm(final Model model, final HttpServletRequest request) {
        final String meesevaApplicationNumber = request.getParameter(APPLICATION_NUMBER);
        return commonSearchForm(model, RECONNECTIONCONNECTION, meesevaApplicationNumber);
    }

    @RequestMapping(value = "commonSearch/editcollection", method = RequestMethod.GET)
    public String editCollection(final Model model, final HttpServletRequest request) {
        final String meesevaApplicationNumber = request.getParameter(APPLICATION_NUMBER);
        return commonSearchForm(model, EDITCOLLECTION, meesevaApplicationNumber);
    }

    @RequestMapping(value = "commonSearch/dataentryedit", method = RequestMethod.GET)
    public String editDataEntry(final Model model, final HttpServletRequest request) {
        final String meesevaApplicationNumber = request.getParameter(APPLICATION_NUMBER);
        return commonSearchForm(model, DATAENTRYEDIT, meesevaApplicationNumber);
    }

    @RequestMapping(value = "commonSearch/generatebill", method = RequestMethod.GET)
    public String generateBill(final Model model, final HttpServletRequest request) {
        final String meesevaApplicationNumber = request.getParameter(APPLICATION_NUMBER);
        return commonSearchForm(model, GENERATEBILL, meesevaApplicationNumber);
    }

    public String commonSearchForm(final Model model, final String applicationType, final String meesevaApplicationNumber) {
        model.addAttribute(APPLICATIONTYPE, applicationType);
        model.addAttribute(MEESEVA_APPLICATION_NUMBER, meesevaApplicationNumber);
        return COMMON_FORM_SEARCH;
    }

    @RequestMapping(value = "commonSearch-form/", method = RequestMethod.POST)
    public String searchConnectionSubmit(@ModelAttribute final ConnectionSearchRequest searchRequest,
            final BindingResult resultBinder, final Model model, final HttpServletRequest request) {
        WaterConnectionDetails waterConnectionDetails = null;
        final String applicationType = request.getParameter(APPLICATIONTYPE);
        if (searchRequest.getMeesevaApplicationNumber() != null)
            model.addAttribute(MEESEVA_APPLICATION_NUMBER, searchRequest.getMeesevaApplicationNumber());
        if (applicationType != null && applicationType.equals(RECONNECTIONCONNECTION))
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    searchRequest.getConsumerCode(), ConnectionStatus.CLOSED);
        else
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    searchRequest.getConsumerCode(), ConnectionStatus.ACTIVE);
        if (waterConnectionDetails == null) {
            resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, INVALID_CONSUMERNUMBER);
            model.addAttribute(APPLICATIONTYPE, applicationType);
            return COMMON_FORM_SEARCH;
        }

        if (applicationType != null && applicationType.equals(ADDNLCONNECTION))
            if (waterConnectionDetails.getCloseConnectionType() != null && waterConnectionDetails
                    .getCloseConnectionType().equals(PERMENENTCLOSECODE)) {
                model.addAttribute(MODE, ERROR_MODE);
                model.addAttribute(APPLICATIONTYPE, applicationType);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, CONNECTION_CLOSED);
                return COMMON_FORM_SEARCH;
            } else if ((CHANGEOFUSE.equals(waterConnectionDetails.getApplicationType().getCode())
                    || NEWCONNECTION.equals(waterConnectionDetails.getApplicationType().getCode())
                    || RECONNECTIONCONNECTION.equals(waterConnectionDetails.getApplicationType().getCode()))
                    && ConnectionStatus.ACTIVE.equals(waterConnectionDetails.getConnectionStatus()))
                return "redirect:/application/addconnection/"
                        + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute(MODE, ERROR_MODE);
                model.addAttribute(APPLICATIONTYPE, applicationType);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, INVALID_CONSUMERNUMBER);
                return COMMON_FORM_SEARCH;
            }
        if (applicationType != null && applicationType.equals(CHANGEOFUSE))
            if (waterConnectionDetails.getCloseConnectionType() != null && waterConnectionDetails
                    .getCloseConnectionType().equals(PERMENENTCLOSECODE)) {
                model.addAttribute(APPLICATIONTYPE, applicationType);
                model.addAttribute(MODE, ERROR_MODE);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, CONNECTION_CLOSED);
                return COMMON_FORM_SEARCH;
            } else if ((waterConnectionDetails.getApplicationType().getCode().equals(NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(ADDNLCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(CHANGEOFUSE)
                    || RECONNECTIONCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
                return "redirect:/application/changeOfUse/" + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute(APPLICATIONTYPE, applicationType);
                model.addAttribute(MODE, ERROR_MODE);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, INVALID_CONSUMERNUMBER);
                return COMMON_FORM_SEARCH;
            }
        if (applicationType != null
                && applicationType.equals(SEARCH_MENUTREE_APPLICATIONTYPE_CLOSURE))
            if (waterConnectionDetails.getCloseConnectionType() != null && waterConnectionDetails
                    .getCloseConnectionType().equals(PERMENENTCLOSECODE)) {
                model.addAttribute(MODE, ERROR_MODE);
                model.addAttribute(APPLICATIONTYPE, applicationType);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, CONNECTION_CLOSED);
                return COMMON_FORM_SEARCH;
            } else if ((waterConnectionDetails.getApplicationType().getCode().equals(NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(ADDNLCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(CHANGEOFUSE)
                    || waterConnectionDetails.getApplicationType().getCode().equals(RECONNECTIONCONNECTION))
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
                return "redirect:/application/close/" + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute(MODE, ERROR_MODE);
                model.addAttribute(APPLICATIONTYPE, applicationType);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, INVALID_CONSUMERNUMBER);
                return COMMON_FORM_SEARCH;
            }

        if (applicationType != null && applicationType.equals(RECONNECTIONCONNECTION))

            if (waterConnectionDetails.getCloseConnectionType() != null
                    && waterConnectionDetails.getCloseConnectionType().equals(PERMENENTCLOSECODE)) {
                model.addAttribute(MODE, ERROR_MODE);
                model.addAttribute(APPLICATIONTYPE, applicationType);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, CONNECTION_CLOSED);
                return COMMON_FORM_SEARCH;
            } else if (waterConnectionDetails.getApplicationType().getCode().equals(CLOSINGCONNECTION)
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.CLOSED)
                    && waterConnectionDetails.getStatus().getCode()
                            .equals(APPLICATION_STATUS_CLOSERSANCTIONED)
                    && waterConnectionDetails.getCloseConnectionType().equals("T"))
                return "redirect:/application/reconnection/" + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute(MODE, ERROR_MODE);
                model.addAttribute(APPLICATIONTYPE, applicationType);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, INVALID_CONSUMERNUMBER);
                return COMMON_FORM_SEARCH;
            }
        if (applicationType != null
                && applicationType.equals(SEARCH_MENUTREE_APPLICATIONTYPE_METERED))
            if ((waterConnectionDetails.getApplicationType().getCode().equals(NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(ADDNLCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(CHANGEOFUSE))
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE)
                    && waterConnectionDetails.getConnectionType().name()
                            .equals(CONNECTIONTYPE_METERED))
                return "redirect:/application/meterentry/" + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute(MODE, ERROR_MODE);
                model.addAttribute(APPLICATIONTYPE, applicationType);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, INVALID_CONSUMERNUMBER);
                return COMMON_FORM_SEARCH;
            }
        if (applicationType != null && applicationType.equals(DATAENTRYEDIT))
            if ((waterConnectionDetails.getApplicationType().getCode().equals(NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(ADDNLCONNECTION))
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE)
                    && waterConnectionDetails.getLegacy())
                return "redirect:/application/newConnection-editExisting/"
                        + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute(MODE, ERROR_MODE);
                model.addAttribute(APPLICATIONTYPE, applicationType);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, INVALID_CONSUMERNUMBER);
                return COMMON_FORM_SEARCH;
            }
        if (applicationType != null
                && applicationType.equals(SEARCH_MENUTREE_APPLICATIONTYPE_COLLECTTAX)) {
            BigDecimal amoutToBeCollected = BigDecimal.ZERO;
            if (null != waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand())
                amoutToBeCollected = waterConnectionDetailsService.getTotalAmount(waterConnectionDetails);
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
            if (assessmentDetails != null)
                if (amoutToBeCollected.doubleValue() == 0)
                    throw new ApplicationRuntimeException("invalid.collecttax");
                else if ((amoutToBeCollected.doubleValue() > 0
                        && waterConnectionDetails.getConnectionType().equals(ConnectionType.METERED)
                        || waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED))
                        && (waterConnectionDetails.getApplicationType().getCode()
                                .equals(NEWCONNECTION)
                                || waterConnectionDetails.getApplicationType().getCode()
                                        .equals(ADDNLCONNECTION)
                                || waterConnectionDetails.getApplicationType().getCode()
                                        .equals(CHANGEOFUSE))
                        && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
                    return "redirect:/application/generatebill/"
                            + waterConnectionDetails.getConnection().getConsumerCode();
                else {
                    model.addAttribute(MODE, ERROR_MODE);
                    model.addAttribute(APPLICATIONTYPE, applicationType);
                    resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, INVALID_CONSUMERNUMBER);
                    return COMMON_FORM_SEARCH;

                }

        }
        if (applicationType != null && applicationType.equals(EDITCOLLECTION))
            if (waterConnectionDetails.getApplicationType().getCode().equals(NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(ADDNLCONNECTION)
                            && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE)
                            && waterConnectionDetails.getLegacy())
                return "redirect:/application/editCollection/"
                        + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute(MODE, ERROR_MODE);
                model.addAttribute(APPLICATIONTYPE, applicationType);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, INVALID_CONSUMERNUMBER);
                return COMMON_FORM_SEARCH;
            }

        if (applicationType != null && applicationType.equals(GENERATEBILL))
            if (waterConnectionDetails.getApplicationType().getCode().equals(NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(ADDNLCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(CHANGEOFUSE)
                            && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE)
                            && waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED))
                return "redirect:/report/generateBillForHSCNo/"
                        + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute(MODE, ERROR_MODE);
                model.addAttribute(APPLICATIONTYPE, applicationType);
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, INVALID_CONSUMERNUMBER);
                return COMMON_FORM_SEARCH;
            }
        return "";

    }

}