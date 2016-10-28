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

import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERCHARGES_CONSUMERCODE;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

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
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/search/waterSearch/")
public class CommonWaterTaxSearchController {

    private static final String COMMON_FORM_SEARCH = "waterTaxSearch-commonForm";
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

    @RequestMapping(value = "commonSearch/", method = RequestMethod.GET)
    public String commonSearchForm() {
        return COMMON_FORM_SEARCH;
    }

    @RequestMapping(value = "commonSearch/", method = RequestMethod.POST)
    public String searchConnectionSubmit(@ModelAttribute final ConnectionSearchRequest searchRequest,
            final BindingResult resultBinder, final Model model, @RequestParam String applicationType,
            final HttpServletRequest request) {
        WaterConnectionDetails waterConnectionDetails = null;
        if (applicationType != null && applicationType.equals(WaterTaxConstants.RECONNECTIONCONNECTION))
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    searchRequest.getConsumerCode(), ConnectionStatus.CLOSED);
        else
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    searchRequest.getConsumerCode(), ConnectionStatus.ACTIVE);
        applicationType = request.getParameter("applicationType");
        if (waterConnectionDetails == null) {
            resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "invalid.consumernumber");
            return COMMON_FORM_SEARCH;
        }

        if (applicationType != null && applicationType.equals(WaterTaxConstants.ADDNLCONNECTION))
            if (waterConnectionDetails.getCloseConnectionType() != null && (waterConnectionDetails
                    .getCloseConnectionType().equals(WaterTaxConstants.PERMENENTCLOSECODE)
                    || waterConnectionDetails.getCloseConnectionType().equals(WaterTaxConstants.TEMPERARYCLOSECODE))) {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "connection.closed");
                return COMMON_FORM_SEARCH;
            } else if (waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
                return "redirect:/application/addconnection/"
                        + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "invalid.consumernumber");
                return COMMON_FORM_SEARCH;
            }
        if (applicationType != null && applicationType.equals(WaterTaxConstants.CHANGEOFUSE))
            if (waterConnectionDetails.getCloseConnectionType() != null && (waterConnectionDetails
                    .getCloseConnectionType().equals(WaterTaxConstants.PERMENENTCLOSECODE)
                    || waterConnectionDetails.getCloseConnectionType().equals(WaterTaxConstants.TEMPERARYCLOSECODE))) {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "connection.closed");
                return COMMON_FORM_SEARCH;
            } else if ((waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.ADDNLCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.CHANGEOFUSE))
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
                return "redirect:/application/changeOfUse/" + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "invalid.consumernumber");
                return COMMON_FORM_SEARCH;
            }
        if (applicationType != null
                && applicationType.equals(WaterTaxConstants.SEARCH_MENUTREE_APPLICATIONTYPE_CLOSURE))
            if (waterConnectionDetails.getCloseConnectionType() != null && (waterConnectionDetails
                    .getCloseConnectionType().equals(WaterTaxConstants.PERMENENTCLOSECODE)
                    || waterConnectionDetails.getCloseConnectionType().equals(WaterTaxConstants.TEMPERARYCLOSECODE))) {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "connection.closed");
                return COMMON_FORM_SEARCH;
            } else if ((waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.ADDNLCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.CHANGEOFUSE))
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE)
                    && waterConnectionDetails.getCloseConnectionType() == null)
                return "redirect:/application/close/" + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "invalid.consumernumber");
                return COMMON_FORM_SEARCH;
            }

        if (applicationType != null && applicationType.equals(WaterTaxConstants.RECONNECTIONCONNECTION))
            if (waterConnectionDetails.getCloseConnectionType() != null
                    && waterConnectionDetails.getCloseConnectionType().equals(WaterTaxConstants.PERMENENTCLOSECODE)) {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "connection.closed");
                return COMMON_FORM_SEARCH;
            } else if (waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.CLOSINGCONNECTION)
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.CLOSED)
                    && waterConnectionDetails.getStatus().getCode()
                            .equals(WaterTaxConstants.APPLICATION_STATUS_CLOSERSANCTIONED)
                    && waterConnectionDetails.getCloseConnectionType().equals("T"))
                return "redirect:/application/reconnection/" + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "invalid.consumernumber");
                return COMMON_FORM_SEARCH;
            }
        if (applicationType != null
                && applicationType.equals(WaterTaxConstants.SEARCH_MENUTREE_APPLICATIONTYPE_METERED))
            if ((waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.ADDNLCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.CHANGEOFUSE))
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE)
                    && waterConnectionDetails.getConnectionType().name()
                            .equals(WaterTaxConstants.CONNECTIONTYPE_METERED))
                return "redirect:/application/meterentry/" + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "invalid.consumernumber");
                return COMMON_FORM_SEARCH;
            }
        if (applicationType != null && applicationType.equals(WaterTaxConstants.DATAENTRYEDIT))
            if ((waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.ADDNLCONNECTION))
                    && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE)
                    && waterConnectionDetails.getLegacy())
                return "redirect:/application/newConnection-editExisting/"
                        + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "invalid.consumernumber");
                return COMMON_FORM_SEARCH;
            }
        if (applicationType != null
                && applicationType.equals(WaterTaxConstants.SEARCH_MENUTREE_APPLICATIONTYPE_COLLECTTAX)) {
            BigDecimal amoutToBeCollected = BigDecimal.ZERO;
            if (null != waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand())
                amoutToBeCollected = waterConnectionDetailsService.getTotalAmount(waterConnectionDetails);
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
            if (assessmentDetails != null)
                if ((amoutToBeCollected.doubleValue() > 0
                        && waterConnectionDetails.getConnectionType().equals(ConnectionType.METERED)
                        || waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED))
                        && (waterConnectionDetails.getApplicationType().getCode()
                                .equals(WaterTaxConstants.NEWCONNECTION)
                                || waterConnectionDetails.getApplicationType().getCode()
                                        .equals(WaterTaxConstants.ADDNLCONNECTION)
                                || waterConnectionDetails.getApplicationType().getCode()
                                        .equals(WaterTaxConstants.CHANGEOFUSE))
                        && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
                    return "redirect:/application/generatebill/"
                            + waterConnectionDetails.getConnection().getConsumerCode();
                else {
                    model.addAttribute("mode", "errorMode");
                    resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "invalid.consumernumber");
                    return COMMON_FORM_SEARCH;

                }

        }
        if (applicationType != null && applicationType.equals(WaterTaxConstants.EDITCOLLECTION))
            if (waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.ADDNLCONNECTION)
                            && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE)
                            && waterConnectionDetails.getLegacy())
                return "redirect:/application/editCollection/"
                        + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "invalid.consumernumber");
                return COMMON_FORM_SEARCH;
            }

        if (applicationType != null && applicationType.equals(WaterTaxConstants.GENERATEBILL))
            if (waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.NEWCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.ADDNLCONNECTION)
                    || waterConnectionDetails.getApplicationType().getCode().equals(WaterTaxConstants.CHANGEOFUSE)
                            && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE)
                            && waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED))
                return "redirect:/report/generateBillForHSCNo/"
                        + waterConnectionDetails.getConnection().getConsumerCode();
            else {
                model.addAttribute("mode", "errorMode");
                resultBinder.rejectValue(WATERCHARGES_CONSUMERCODE, "invalid.consumernumber");
                return COMMON_FORM_SEARCH;
            }

        return "";

    }

}