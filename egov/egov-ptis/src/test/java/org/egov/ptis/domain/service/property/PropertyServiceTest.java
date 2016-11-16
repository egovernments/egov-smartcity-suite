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