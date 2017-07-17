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
package org.egov.restapi.web.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.ContractorHelper;
import org.egov.restapi.model.RestErrors;
import org.egov.restapi.service.ExternalContractorService;
import org.egov.works.master.service.ContractorService;
import org.egov.works.models.masters.Contractor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ContractorControllerTest extends AbstractContextControllerTest<ContractorController> {

    @InjectMocks
    private ContractorController contractorController;

    private Contractor contractor;

    private List<RestErrors> errors;

    private String requestJson;

    @Mock
    private ContractorService contractorService;

    @Mock
    private ExternalContractorService externalContractorService;

    private String responseJson;

    @Mock
    private HttpServletResponse response;

    @Override
    protected ContractorController initController() {
        MockitoAnnotations.initMocks(this);
        return contractorController;
    }

    @Before
    public void setUp() {
        errors = new ArrayList<>();
        contractor = new Contractor();
        contractor.setCode("JUNITCODE");
        contractor.setName("JUNITNAME");
        when(contractorService.createContractor(Matchers.any(Contractor.class))).thenReturn(contractor);
    }

    @Test
    public void shouldCreateContractor() throws IOException {
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractor-Json.json"),
                ApplicationConstant.DEFAULT_CHARACTER_ENCODING);
        when(externalContractorService.validateContactorToCreate(Matchers.anyObject())).thenReturn(errors);
        when(externalContractorService.populateContractorToCreate(Matchers.anyObject())).thenReturn(contractor);
        when(externalContractorService.saveContractor(Matchers.anyObject())).thenReturn(contractor);
        responseJson = contractorController.createContractor(requestJson, response);
        final StringBuilder successMessage = new StringBuilder();
        successMessage.append("\"Contractor data saved successfully with code ").append(contractor.getCode()).append("\"");
        assertEquals(responseJson, successMessage.toString());
    }

    @Test
    public void shouldValidateIsCodeAndNameEmpty() throws IOException {
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractorempty-Json.json"),
                ApplicationConstant.DEFAULT_CHARACTER_ENCODING);
        when(externalContractorService.validateContactorToCreate(Matchers.anyObject())).thenReturn(errors);
        when(externalContractorService.populateContractorToCreate(Matchers.anyObject())).thenReturn(contractor);
        when(externalContractorService.saveContractor(Matchers.anyObject())).thenReturn(contractor);
        responseJson = contractorController.createContractor(requestJson, response);
        final JSONArray jsonArray = new JSONArray(responseJson);
        final JSONObject jsonObject = jsonArray.getJSONObject(0);
        assertEquals(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_JSON_REQUEST, jsonObject.getString("errorCode"));

    }

    @Test
    public void shouldValidateGetContractor() throws IOException {
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractor-Json.json"),
                ApplicationConstant.DEFAULT_CHARACTER_ENCODING);
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        responseJson = contractorController.getContractors(requestJson, response);
        final JSONObject jsonObject = new JSONObject(responseJson);
        assertEquals(RestApiConstants.THIRD_PARTY_ERR_CODE_NOT_EXIST_CONTRACTOR, jsonObject.getString("errorCode"));

    }

    @Test
    public void shouldValidateGetContractorForCode() throws IOException {
        final ContractorHelper contractorHelper = new ContractorHelper();
        contractorHelper.setCode("JUNITCODE");
        contractorHelper.setName("JUNITNAME");
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractor-Json.json"),
                ApplicationConstant.DEFAULT_CHARACTER_ENCODING);
        when(externalContractorService.populateContractorData(Matchers.anyObject())).thenReturn(contractorHelper);
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(contractor);
        responseJson = contractorController.getContractors(requestJson, response);
        final JSONObject jsonObject = new JSONObject(responseJson);
        assertEquals(contractorHelper.getCode(), jsonObject.getString("code"));
    }

    @Test
    public void shouldModifyContractor() throws IOException {
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractor-Json.json"),
                ApplicationConstant.DEFAULT_CHARACTER_ENCODING);
        when(externalContractorService.validateContactorToUpdate(Matchers.anyObject())).thenReturn(errors);
        when(externalContractorService.populateContractorToUpdate(Matchers.anyObject())).thenReturn(contractor);
        when(externalContractorService.updateContractor(Matchers.anyObject())).thenReturn(contractor);
        responseJson = contractorController.updateContractor(requestJson, response);
        final StringBuilder successMessage = new StringBuilder();
        successMessage.append("\"Contractor data modified successfully with code ").append(contractor.getCode()).append("\"");
        assertEquals(responseJson, successMessage.toString());
    }

    @Test
    public void shouldValidateModifyContractorIsCodeAndNameEmpty() throws IOException {
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractorempty-Json.json"),
                ApplicationConstant.DEFAULT_CHARACTER_ENCODING);
        when(externalContractorService.validateContactorToUpdate(Matchers.anyObject())).thenReturn(errors);
        when(externalContractorService.populateContractorToUpdate(Matchers.anyObject())).thenReturn(contractor);
        when(externalContractorService.updateContractor(Matchers.anyObject())).thenReturn(contractor);
        responseJson = contractorController.updateContractor(requestJson, response);
        final JSONObject jsonObject = new JSONObject(responseJson);
        assertEquals(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_JSON_REQUEST, jsonObject.getString("errorCode"));

    }

}