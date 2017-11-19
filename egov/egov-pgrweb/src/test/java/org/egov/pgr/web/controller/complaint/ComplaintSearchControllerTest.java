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

package org.egov.pgr.web.controller.complaint;

import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.ReceivingModeService;
import org.egov.pgr.web.controller.AbstractContextControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author elzan
 */
public class ComplaintSearchControllerTest extends AbstractContextControllerTest<ComplaintSearchController> {

    private MockMvc mockMvc;
    @Mock
    private ComplaintService complaintService;
    @Mock
    private ComplaintStatusService complaintStatusService;
    @Mock
    private ComplaintTypeService complaintTypeService;
    @Mock
    private AssignmentService assignmentService;
    @Mock
    private SecurityUtils securityUtils;
    @Mock
    private CityService cityWebsiteService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private ReceivingModeService receivingModeService;

    @InjectMocks
    private ComplaintSearchController complaintSearchController;

    @Override
    protected ComplaintSearchController initController() {
        initMocks(this);
        return complaintSearchController;
    }

    @Before
    public void before() {
        mockMvc = mvcBuilder.build();
        final List<Department> departmentList = new ArrayList<>();
        when(departmentService.getAllDepartments()).thenReturn(departmentList);

        final List<ComplaintStatus> complaintStatusList = new ArrayList<>();
        when(complaintStatusService.getAllComplaintStatus()).thenReturn(complaintStatusList);

        final List receivingModes = new ArrayList();
        when(receivingModeService.getReceivingModes()).thenReturn(receivingModes);

        final List complaintTypeList = new ArrayList<ComplaintType>();
        when(complaintTypeService.findActiveComplaintTypes()).thenReturn(complaintTypeList);
    }

    @Test
    public void shouldRetrieveSearchPage() throws Exception {
        mockMvc.perform(get("/complaint/citizen/anonymous/search")).andExpect(view().name("complaint-search"))
                .andExpect(status().isOk());
    }

    /*@Test
    public void shouldSearchForGivenRequest() throws Exception {
        when(searchService.search(anyList(), anyList(), anyString(), any(Filters.class), eq(Sort.NULL), eq(Page.NULL)))
                .thenReturn(
                        SearchResult.from(Classpath.readAsString("complaintSearchControllerTest-searchResponse.json")));

        mockMvc.perform(post("/complaint/citizen/anonymous/search").param("searchText", "road").param("complaintNumber",
                "CRN123")).andExpect(status().isOk()).andReturn();

        final ArgumentCaptor<Filters> filterCaptor = ArgumentCaptor.forClass(Filters.class);

        verify(searchService).search(eq(asList(Index.PGR.toString())), eq(asList(IndexType.COMPLAINT.toString())),
                eq("road"), filterCaptor.capture(), eq(Sort.NULL), eq(Page.NULL));

        final Filters actualFilters = filterCaptor.getValue();
        final Filter filter = actualFilters.getAndFilters().get(0);
        assertThat(filter.field(), is("clauses.crn"));
        assertThat(filter, instanceOf(TermsStringFilter.class));
        assertThat(((TermsStringFilter) filter).values()[0], is("CRN123"));
    }

    @Test
    public void shouldSearchForGivenDateRange() throws Exception {
        when(searchService.search(anyList(), anyList(), anyString(), any(Filters.class), eq(Sort.NULL), eq(Page.NULL)))
                .thenReturn(
                        SearchResult.from(Classpath.readAsString("complaintSearchControllerTest-searchResponse.json")));

        mockMvc.perform(post("/complaint/citizen/anonymous/search").param("complaintNumber", "CRN123")
                .param("complaintDate", "today")).andExpect(status().isOk()).andReturn();

        final ArgumentCaptor<Filters> filterCaptor = ArgumentCaptor.forClass(Filters.class);

        verify(searchService).search(eq(asList(Index.PGR.toString())), eq(asList(IndexType.COMPLAINT.toString())), eq(null),
                filterCaptor.capture(), eq(Sort.NULL), eq(Page.NULL));

        final Filters actualFilters = filterCaptor.getValue();
        final Filter filter = actualFilters.getAndFilters().get(0);
        assertThat(filter.field(), is("clauses.crn"));
        assertThat(filter, instanceOf(TermsStringFilter.class));
        assertThat(((TermsStringFilter) filter).values()[0], is("CRN123"));
    }*/

}