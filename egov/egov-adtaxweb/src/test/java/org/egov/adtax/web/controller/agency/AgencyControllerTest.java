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
package org.egov.adtax.web.controller.agency;

import org.egov.adtax.entity.Agency;
import org.egov.adtax.entity.enums.AgencyStatus;
import org.egov.adtax.service.AgencyService;
import org.egov.adtax.web.controller.AbstractContextControllerTest;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AgencyControllerTest extends AbstractContextControllerTest<AgencyController> {

    @Mock
    private AgencyService agencyService;

    @Mock
    private SecurityUtils securityUtils;
    @InjectMocks
    private AgencyController controller;

    @Mock
    User user;

    MockMvc mockMvc;

    @Before
    public void before() {
        when(securityUtils.getCurrentUser()).thenReturn(user);
        mockMvc = mvcBuilder.build();
    }

    @Test
    public void getCreateAgency() throws Exception {
        mockMvc.perform(get("/agency/create"))
                .andExpect(view().name("agency-form"))
                .andExpect(status().isOk());
    }

    @Test
    public void postCreateAgency() throws Exception {
        final Double depositAmount = 100.0;
        mockMvc.perform(post("/agency/create").param("name", "testing").param("depositAmount", "100.0").param("code", "testing")
                .param("mobileNumber", "9999999999").param("status", "ACTIVE")).andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/agency/success/testing"));
        final ArgumentCaptor<Agency> argumentCaptor = ArgumentCaptor.forClass(Agency.class);
        verify(agencyService).createAgency(argumentCaptor.capture());
        final Agency createdAgency = argumentCaptor.getValue();
        assertTrue(createdAgency.isNew());
        assertEquals(createdAgency.getName(), "testing");
        assertEquals(createdAgency.getCode(), "testing");
        assertEquals(createdAgency.getMobileNumber(), "9999999999");
        assertEquals(createdAgency.getDepositAmount(), depositAmount);
        assertEquals(createdAgency.getStatus(), AgencyStatus.ACTIVE);
    }

    @Test
    public void validateAgency() throws Exception {
        mockMvc.perform(post("/agency/create")).andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("agency", "code"))
                .andExpect(model().attributeHasFieldErrors("agency", "depositAmount"))
                .andExpect(model().attributeHasFieldErrors("agency", "mobileNumber"))
                .andExpect(model().attributeHasFieldErrors("agency", "status"))
                .andExpect(view().name("agency-form"));
        verify(agencyService, never()).createAgency(any(Agency.class));
    }

    @Test
    public void getSearchAgency() throws Exception {
        mockMvc.perform(get("/agency/search"))
                .andExpect(view().name("agency-search"))
                .andExpect(status().isOk());
    }

    @Test
    public void postSearchAgency() throws Exception {
        mockMvc.perform(post("/agency/search").param("code", "testing"))
                .andExpect(redirectedUrl("/agency/update/testing"));
    }

    @Override
    protected AgencyController initController() {
        initMocks(this);
        return controller;
    }
}