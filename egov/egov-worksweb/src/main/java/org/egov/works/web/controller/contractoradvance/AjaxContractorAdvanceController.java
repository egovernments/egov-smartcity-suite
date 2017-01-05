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
package org.egov.works.web.controller.contractoradvance;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.contractoradvance.entity.SearchRequestContractorRequisition;
import org.egov.works.contractoradvance.service.ContractorAdvanceService;
import org.egov.works.web.adaptor.SearchContractorAdvanceJsonAdaptor;
import org.egov.works.web.adaptor.SearchContractorAdvanceToCancelJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Controller
@RequestMapping(value = "/contractoradvance")
public class AjaxContractorAdvanceController {

    @Autowired
    private SearchContractorAdvanceJsonAdaptor searchContractorAdvanceJsonAdaptor;

    @Autowired
    private ContractorAdvanceService contractorAdvanceService;

    @Autowired
    private SearchContractorAdvanceToCancelJsonAdaptor searchContractorAdvanceToCancelJsonAdaptor;

    @RequestMapping(value = "/ajaxarfnumbers-searchcr", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getAdvanceRequisitionNumberToSearchCR(@RequestParam final String advanceRequisitionNumber) {
        return contractorAdvanceService.getAdvanceRequisitionNumberToSearchCR(advanceRequisitionNumber);
    }

    @RequestMapping(value = "/ajaxworkordernumbers-searchcr", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getWorkOrderNumberToSearchCR(@RequestParam final String workOrderNumber) {
        return contractorAdvanceService.getWorkOrderNumberToSearchCR(workOrderNumber);
    }

    @RequestMapping(value = "/ajaxcontractors-searchcr", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getContractorToSearchCR(@RequestParam final String contractorName) {
        return contractorAdvanceService.getContractorsToSearchCR(contractorName);
    }

    @RequestMapping(value = "/ajaxsearch-contractorrequisition", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchWorkOrdersToCreateCR(
            @ModelAttribute final SearchRequestContractorRequisition searchRequestContractorRequisition) {
        final List<ContractorAdvanceRequisition> contractorAdvanceRequisitionList = contractorAdvanceService
                .searchContractorAdvance(searchRequestContractorRequisition);
        final String result = new StringBuilder("{ \"data\":")
                .append(searchContractorAdvanceRequisition(contractorAdvanceRequisitionList))
                .append("}").toString();
        return result;
    }

    public Object searchContractorAdvanceRequisition(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder
                .registerTypeAdapter(ContractorAdvanceRequisition.class, searchContractorAdvanceJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/validatearf/{workOrderEstimateId}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String validateContractorAdvance(@PathVariable final Long workOrderEstimateId,
            final HttpServletRequest request, final HttpServletResponse response) {
        final JsonObject jsonObject = new JsonObject();
        contractorAdvanceService.validateARFInDrafts(null, workOrderEstimateId, jsonObject, null);
        contractorAdvanceService.validateARFInWorkFlow(null, workOrderEstimateId, jsonObject, null);
        if (jsonObject.toString().length() > 2) {
            sendAJAXResponse(jsonObject.toString(), response);
            return "";
        }
        return null;
    }

    protected void sendAJAXResponse(final String msg, final HttpServletResponse response) {
        try {
            final Writer httpResponseWriter = response.getWriter();
            IOUtils.write(msg, httpResponseWriter);
            IOUtils.closeQuietly(httpResponseWriter);
        } catch (final IOException e) {
            throw new ApplicationRuntimeException("error.validate.re");
        }
    }

    @RequestMapping(value = "/ajaxadvancebillnumbers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getAdvanceBillNumber(@RequestParam final String advanceBillNumber) {
        return contractorAdvanceService.findAdvanceBillNumber(advanceBillNumber);
    }

    @RequestMapping(value = "/ajaxarfnumbers-cancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getAdvanceRequistionNumberToCancelContractorAdvance(
            @RequestParam final String advanceRequisitionNumber) {
        return contractorAdvanceService.findAdvanceRequisitionNumberToCancelContractorAdvance(advanceRequisitionNumber);
    }

    @RequestMapping(value = "/ajaxcontractors-cancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getContractorsToCancelContractorAdvance(@RequestParam final String contractorName) {
        return contractorAdvanceService.findContractorsToCancelContractorAdvance(contractorName);
    }

    @RequestMapping(value = "/ajaxloanumbers-cancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> getWorkOrderNumberToCancelContractorAdvance(@RequestParam final String workOrderNumber) {
        return contractorAdvanceService.findWorkOrderNumberToCancelContractorAdvance(workOrderNumber);
    }

    @RequestMapping(value = "/cancel/ajax-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchContractorAdvanceToCancel(
            @ModelAttribute final SearchRequestContractorRequisition searchRequestContractorRequisition) {
        final List<ContractorAdvanceRequisition> contractorAdvanceRequisitionList = contractorAdvanceService
                .searchContractorAdvanceToCancel(searchRequestContractorRequisition);
        final String result = new StringBuilder("{ \"data\":")
                .append(searchContractorAdvanceToCancel(contractorAdvanceRequisitionList))
                .append("}").toString();
        return result;
    }

    public Object searchContractorAdvanceToCancel(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder
                .registerTypeAdapter(ContractorAdvanceRequisition.class, searchContractorAdvanceToCancelJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

}
