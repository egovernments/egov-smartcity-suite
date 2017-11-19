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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.infra.exception.ApplicationException;
import org.egov.model.bills.EgBillregister;
import org.egov.restapi.constants.RestApiConstants;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BillPaymentStatusControllerTest extends AbstractContextControllerTest<BillPaymentStatusController> {

    @Mock
    private EgovCommon egovCommon;

    @Mock
    private ExpenseBillService expenseBillService;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private BillPaymentStatusController billPaymentStatusController;

    private String requestJson;

    private EgBillregister egBillregister;

    private String responseJson;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Override
    protected BillPaymentStatusController initController() {
        MockitoAnnotations.initMocks(this);
        return billPaymentStatusController;
    }

    @Before
    public void setUp() throws ApplicationException {
        egBillregister = new EgBillregister();
        egBillregister.setId(1l);
        egBillregister.setBillnumber("abcd");
    }

    @Test
    public void shouldReturnPaymentAmount() throws Exception {
        when(expenseBillService.getByBillnumber(Matchers.anyString())).thenReturn(egBillregister);
        when(egovCommon.getPaymentAmount(Matchers.anyLong())).thenReturn(BigDecimal.valueOf(1000));
        responseJson = billPaymentStatusController.getPaymentAmount("1234", response);
        assertEquals("1000", responseJson);
    }

    @Test
    public void shouldGetErrorsWhenInvalidBillNumber() {
        when(expenseBillService.getByBillnumber(null)).thenReturn(null);
        responseJson = billPaymentStatusController.getPaymentAmount(null, response);
        final JSONObject jsonObject = new JSONObject(responseJson);
        assertEquals(RestApiConstants.THIRD_PARTY_ERR_CODE_NOT_VALID_BILLNUMBER, jsonObject.get("errorCode"));
    }

    @Test
    public void shouldThrowApplicationExceptionWhenBillIdIsNull() throws ApplicationException {
        when(expenseBillService.getByBillnumber(Matchers.anyString())).thenReturn(egBillregister);
        when(egovCommon.getPaymentAmount(Matchers.anyLong())).thenThrow(new ApplicationException(""));
        responseJson = billPaymentStatusController.getPaymentAmount(requestJson, response);
        final JSONObject jsonObject = new JSONObject(responseJson);
        assertEquals(StringUtils.EMPTY, jsonObject.get("errorCode"));
    }
}