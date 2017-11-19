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
package org.egov.restapi.web.rest;

import org.apache.commons.io.IOUtils;
import org.egov.model.bills.EgBillregister;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.RestErrors;
import org.egov.restapi.service.BillService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CreateBillControllerTest extends AbstractContextControllerTest<CreateBillController> {

    @Mock
    private BillService billService;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private CreateBillController createBillController;

    private List<RestErrors> errors;

    private String requestJson;

    private EgBillregister egBillregister;

    private String responseJson;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Override
    protected CreateBillController initController() {
        MockitoAnnotations.initMocks(this);
        return createBillController;
    }

    @Before
    public void setUp() throws IOException {
        errors = new ArrayList<>();
        egBillregister = new EgBillregister();
        egBillregister.setBillnumber("abcd");
        when(billService.createBill(Matchers.any(EgBillregister.class))).thenReturn(egBillregister);
    }

    @Test
    public void shouldCreateBillWhenValidJsonProvided() throws Exception {
        requestJson = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("createbill-Json.json"),
                "UTF-8");
        when(billService.validateBillRegister(Matchers.anyObject())).thenReturn(errors);
        responseJson = createBillController.createContractorBill(requestJson, response);
        final JSONObject jsonObject = new JSONObject(responseJson);
        assertEquals(egBillregister.getBillnumber(), jsonObject.get("billNumber"));
    }

    @Test
    public void shouldFailWhenInValidJsonProvided() throws Exception {
        requestJson = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("createbill-Json.json"),
                "UTF-8");
        final RestErrors restErrors = new RestErrors();
        restErrors.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_DEPARTMENT);
        restErrors.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_DEPARTMENT);
        errors.add(restErrors);
        when(billService.validateBillRegister(Matchers.anyObject())).thenReturn(errors);
        responseJson = createBillController.createContractorBill(requestJson, response);
        final JSONArray jsonArray = new JSONArray(responseJson);
        final JSONObject jsonObject = jsonArray.getJSONObject(0);
        assertEquals(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_DEPARTMENT, jsonObject.get("errorCode"));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenJsonStringIsNull() {
        thrown.expect(NullPointerException.class);
        responseJson = createBillController.createContractorBill(requestJson, response);
    }
}