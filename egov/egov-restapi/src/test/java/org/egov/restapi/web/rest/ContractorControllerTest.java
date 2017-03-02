package org.egov.restapi.web.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
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

    private ContractorHelper contractorHelper;

    private List<RestErrors> errors;

    private String requestJson;

    @Mock
    private ContractorService contractorService;

    @Mock
    private ExternalContractorService externalContractorService;

    private String responseJson;

    @Mock
    private HttpServletRequest request;

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
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractor-Json.json"), "UTF-8");
        when(externalContractorService.validateContactorToCreate(Matchers.anyObject())).thenReturn(errors);
        when(externalContractorService.populateContractorToCreate(Matchers.anyObject())).thenReturn(contractor);
        when(externalContractorService.saveContractor(Matchers.anyObject())).thenReturn(contractor);
        responseJson = contractorController.createContractor(requestJson, request);
        final StringBuilder successMessage = new StringBuilder();
        successMessage.append("\"Contractor data saved successfully with code ").append(contractor.getCode()).append("\"");
        assertEquals(responseJson, successMessage.toString());
    }

    @Test
    public void shouldValidateIsCodeAndNameEmpty() throws IOException {
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractorempty-Json.json"), "UTF-8");
        when(externalContractorService.validateContactorToCreate(Matchers.anyObject())).thenReturn(errors);
        when(externalContractorService.populateContractorToCreate(Matchers.anyObject())).thenReturn(contractor);
        when(externalContractorService.saveContractor(Matchers.anyObject())).thenReturn(contractor);
        responseJson = contractorController.createContractor(requestJson, request);
        final JSONArray jsonArray = new JSONArray(responseJson);
        final JSONObject jsonObject = jsonArray.getJSONObject(0);
        assertEquals(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_JSON_REQUEST, jsonObject.getString("errorCode"));

    }

    @Test
    public void shouldValidateGetContractor() throws IOException {
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractor-Json.json"), "UTF-8");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        responseJson = contractorController.getContractors(requestJson);
        final JSONObject jsonObject = new JSONObject(responseJson);
        assertEquals(RestApiConstants.THIRD_PARTY_ERR_CODE_NOT_EXIST_CONTRACTOR, jsonObject.getString("errorCode"));

    }

    @Test
    public void shouldValidateGetContractorForCode() throws IOException {
        contractorHelper = new ContractorHelper();
        contractorHelper.setCode("JUNITCODE");
        contractorHelper.setName("JUNITNAME");
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractor-Json.json"), "UTF-8");
        when(externalContractorService.populateContractorData(Matchers.anyObject())).thenReturn(contractorHelper);
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(contractor);
        responseJson = contractorController.getContractors(requestJson);
        final JSONObject jsonObject = new JSONObject(responseJson);
        assertEquals(contractorHelper.getCode(), jsonObject.getString("code"));
    }

    @Test
    public void shouldModifyContractor() throws IOException {
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractor-Json.json"), "UTF-8");
        when(externalContractorService.validateContactorToUpdate(Matchers.anyObject())).thenReturn(errors);
        when(externalContractorService.populateContractorToUpdate(Matchers.anyObject())).thenReturn(contractor);
        when(externalContractorService.updateContractor(Matchers.anyObject())).thenReturn(contractor);
        responseJson = contractorController.updateContractor(requestJson, request);
        final StringBuilder successMessage = new StringBuilder();
        successMessage.append("\"Contractor data modified successfully with code ").append(contractor.getCode()).append("\"");
        assertEquals(responseJson, successMessage.toString());
    }

    @Test
    public void shouldValidateModifyContractorIsCodeAndNameEmpty() throws IOException {
        requestJson = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("createcontractorempty-Json.json"), "UTF-8");
        when(externalContractorService.validateContactorToUpdate(Matchers.anyObject())).thenReturn(errors);
        when(externalContractorService.populateContractorToUpdate(Matchers.anyObject())).thenReturn(contractor);
        when(externalContractorService.updateContractor(Matchers.anyObject())).thenReturn(contractor);
        responseJson = contractorController.updateContractor(requestJson, request);
        final JSONObject jsonObject = new JSONObject(responseJson);
        assertEquals(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_JSON_REQUEST, jsonObject.getString("errorCode"));

    }

}
