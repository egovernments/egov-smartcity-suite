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

package org.egov.ptis.domain.service.property;

import org.egov.commons.dao.InstallmentHibDao;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.service.PenaltyCalculationService;
import org.egov.ptis.client.service.calculator.APTaxCalculator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusValuesDAO;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class PropertyServiceTest {
    @Mock
    private PersistenceService propPerServ;
    @Mock
    private APTaxCalculator taxCalculator;
    @Mock
    private PropertyTaxUtil propertyTaxUtil;
    @Mock
    private EisCommonsService eisCommonsService;
    @Mock
    private ModuleService moduleDao;
    @Mock
    private InstallmentHibDao installmentDao;
    @Mock
    private UserService userService;
    @Mock
    private ApplicationNumberGenerator applicationNumberGenerator;
    @Mock
    private PersistenceService<DocumentType, Long> documentTypePersistenceService;
    @Mock
    private FileStoreService fileStoreService;
    @Mock
    private ApplicationIndexService applicationIndexService;
    @Mock
    private SimpleRestClient simpleRestClient;
    @Mock
    private PtDemandDao ptDemandDAO;
    @Mock
    private BasicPropertyDAO basicPropertyDAO;
    @Mock
    private PropertyStatusValuesDAO propertyStatusValuesDAO;
    @Mock
    private AppConfigValueService appConfigValuesService;
    @Mock
    private DesignationService designationService;
    @Mock
    private DepartmentService departmentService;
    @Mock
    private AssignmentService assignmentService;
    @Mock
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Mock
    private PenaltyCalculationService penaltyCalculationService;
    @Mock
    private PTBillServiceImpl ptBillServiceImpl;
    @Mock
    private EisCommonService eisCommonService;

    @InjectMocks
    private PropertyService propertyService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        MockitoAnnotations.initMocks(this);
        Mockito.when(userService.getUserById(anyLong())).thenReturn(new User());
    }

    @Test
    public void shouldSetReferenceNumberTo1WhenOrderDateIsNull() throws Exception {
        System.out.println(propertyService);
        PropertyStatusValues propertyStatusValues = propertyService.createPropStatVal(new BasicPropertyImpl(), "", new Date(), null, null, null, "ok");
        assertEquals("0001", propertyStatusValues.getReferenceNo());
    }
}