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
package org.egov.adtax.web.controller.ratesClass;

import org.egov.adtax.service.RatesClassService;
import org.egov.adtax.web.controller.AbstractContextControllerTest;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class RatesClassControllerTest extends AbstractContextControllerTest<RatesClassController> {

    @Mock
    private RatesClassService ratesClassService;

    @Mock
    private SecurityUtils securityUtils;
    @InjectMocks
    private RatesClassController controller;

    @Mock
    User user;

    MockMvc mockMvc;

    @Before
    public void before() {
        when(securityUtils.getCurrentUser()).thenReturn(user);
        mockMvc = mvcBuilder.build();
    }

    @Test
    public void getCreateRatesClass() throws Exception {
        mockMvc.perform(get("/ratesclass/create"))
                .andExpect(view().name("ratesClass-form"))
                .andExpect(status().isOk());
    }

/*    @Test
    public void postCreateRatesClass() throws Exception {
        mockMvc.perform(post("/ratesclass/create").param("description", "1")).andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/ratesclass/success/1"));
        final ArgumentCaptor<RatesClass> argumentCaptor = ArgumentCaptor.forClass(RatesClass.class);
        verify(ratesClassService).createRatesClass(argumentCaptor.capture());
        final RatesClass createdClass = argumentCaptor.getValue();
        assertTrue(createdClass.isNew());
        assertEquals(createdClass.getId(), "1");
    }*/

   /* @Test
    public void validateRatesClass() throws Exception {
        mockMvc.perform(post("/ratesclass/create")).andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("ratesClass", "description"))
                .andExpect(view().name("ratesClass-form"));
        verify(ratesClassService, never()).createRatesClass(any(RatesClass.class));
    }*/


    @Override
    protected RatesClassController initController() {
        initMocks(this);
        return controller;
    }
}