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
package org.egov.restapi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.service.AccountdetailtypeService;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.commons.service.FunctionService;
import org.egov.commons.service.FundService;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.egf.utils.FinancialUtils;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.model.bills.EgBillregister;
import org.egov.restapi.model.BillDetails;
import org.egov.restapi.model.BillPayeeDetails;
import org.egov.restapi.model.BillRegister;
import org.egov.restapi.model.RestErrors;
import org.egov.restapi.web.rest.AbstractContextControllerTest;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.egov.works.master.service.ContractorService;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.models.masters.Contractor;
import org.egov.works.services.ProjectCodeService;
import org.egov.works.utils.WorksConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

public class BillServiceTest extends AbstractContextControllerTest<BillService> {

    @Mock
    private FundService fundService;

    @Mock
    private FunctionService functionService;

    @Mock
    private SchemeService schemeService;

    @Mock
    private SubSchemeService subSchemeService;

    @Mock
    private ChartOfAccountsService chartOfAccountsService;

    @Mock
    private ExpenseBillService expenseBillService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private AccountdetailtypeService accountdetailtypeService;

    @Mock
    private ProjectCodeService projectCodeService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Mock
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Mock
    private ContractorService contractorService;

    @Mock
    private FinancialUtils financialUtils;

    @InjectMocks
    private BillService billService;

    @Mock
    private HttpServletRequest request;

    private List<RestErrors> errors;

    private EgBillregister egBillregister;

    private BillRegister billRegister;

    private BillDetails billDetails1;

    private BillDetails billDetails2;

    private BillPayeeDetails billPayeeDetails1;

    private BillPayeeDetails billPayeeDetails2;

    private List<CChartOfAccounts> chartOfAccounts;

    private CChartOfAccounts chartOfAccount1;

    private CChartOfAccounts chartOfAccount2;

    private final List<ProjectCode> projectCodes = new ArrayList<>();

    private ProjectCode projectCode;

    private final List<Contractor> contractors = new ArrayList<>();

    private Contractor contractor;

    private EgwStatus status;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Override
    protected BillService initController() {
        MockitoAnnotations.initMocks(this);
        return billService;
    }

    @Before
    public void setUp() throws IOException {
        errors = new ArrayList<>();
        egBillregister = new EgBillregister();

        chartOfAccounts = new ArrayList<>();
        chartOfAccount1 = new CChartOfAccounts();
        chartOfAccount2 = new CChartOfAccounts();
        chartOfAccount1.setId(1l);
        chartOfAccount2.setId(2l);
        chartOfAccount1.setClassification(4l);
        chartOfAccounts.add(chartOfAccount1);
        chartOfAccounts.add(chartOfAccount2);

        final CFunction function = new CFunction();
        function.setId(1l);

        final Accountdetailtype accountdetailtype1 = new Accountdetailtype();
        accountdetailtype1.setFullQualifiedName(ProjectCode.class.getName());

        final Accountdetailtype accountdetailtype2 = new Accountdetailtype();
        accountdetailtype2.setFullQualifiedName(Contractor.class.getName());

        projectCode = new ProjectCode();
        projectCode.setId(1l);
        contractor = new Contractor();
        contractor.setId(1l);
        projectCodes.add(projectCode);
        contractors.add(contractor);

        status = new EgwStatus();
        status.setCode(WorksConstants.APPROVED);

        fillBillRegister();

        when(projectCodeService.findActiveProjectCodeByCode(Matchers.anyString())).thenReturn(new ProjectCode());
        when(schemeService.findByCode(Matchers.anyString())).thenReturn(new Scheme());
        when(subSchemeService.findByCode(Matchers.anyString())).thenReturn(new SubScheme());
        when(departmentService.getDepartmentByCode(Matchers.anyString())).thenReturn(new Department());
        when(functionService.findByCode(Matchers.anyString())).thenReturn(function);
        when(fundService.findByCode(Matchers.anyString())).thenReturn(new Fund());
        when(financialYearHibernateDAO.getFinancialYearByDate(Matchers.any())).thenReturn(new CFinancialYear());
        when(chartOfAccountsService.getByGlCode(Matchers.anyString())).thenReturn(chartOfAccount1);
        when(chartOfAccountsHibernateDAO
                .getAccountCodeByPurposeName(Matchers.anyString())).thenReturn(chartOfAccounts);
        when(chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName(Matchers.anyString(), Matchers.anyString()))
                .thenReturn(new Accountdetailtype());
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(new Contractor());
        when(financialUtils.getStatusByModuleAndCode(Matchers.anyString(), Matchers.anyString())).thenReturn(status);
        when(accountdetailtypeService
                .findByName("PROJECTCODE")).thenReturn(accountdetailtype1);
        when(accountdetailtypeService
                .findByName("contractor")).thenReturn(accountdetailtype2);
        when(applicationContext.getBean("projectCodeService")).thenReturn(projectCodeService);
        when(applicationContext.getBean("contractorService")).thenReturn(contractorService);
        when(projectCodeService.filterActiveEntities(Matchers.anyString(), Matchers.anyInt(), Matchers.anyInt()))
                .thenReturn(projectCodes);
        when(contractorService.filterActiveEntities(Matchers.anyString(), Matchers.anyInt(), Matchers.anyInt()))
                .thenReturn(contractors);

        when(expenseBillService.create(Matchers.any(EgBillregister.class), Matchers.anyLong(), Matchers.anyString(),
                Matchers.anyString(), Matchers.anyString())).thenReturn(egBillregister);
    }

    private void fillBillRegister() {
        billRegister = new BillRegister();
        billDetails1 = new BillDetails();
        billDetails2 = new BillDetails();
        billPayeeDetails1 = new BillPayeeDetails();
        billPayeeDetails2 = new BillPayeeDetails();
        billRegister.setProjectCode("1234");
        billRegister.setBillDate(DateUtils.getDate("20-02-2017", "dd-MM-yyyy"));
        billRegister.setBillType("Final Bill");
        billRegister.setPayTo("manoj");
        billRegister.setBillAmount(BigDecimal.valueOf(1000));
        billRegister.setNarration("abcd");
        billRegister.setPartyBillNumber("bill001");
        billRegister.setPartyBillDate(DateUtils.getDate("20-02-2017", "dd-MM-yyyy"));
        billRegister.setDepartmentCode("ENG");
        billRegister.setFunctionCode("202102");
        billRegister.setFundCode("01");

        billDetails1.setGlcode("4120043");
        billDetails1.setDebitAmount(BigDecimal.valueOf(1000));
        billDetails2.setGlcode("3401002");
        billDetails2.setCreditAmount(BigDecimal.valueOf(1000));

        billRegister.getBillDetails().add(billDetails1);
        billRegister.getBillDetails().add(billDetails2);

        billPayeeDetails1.setGlcode("4120043");
        billPayeeDetails1.setAccountDetailType("PROJECTCODE");
        billPayeeDetails1.setAccountDetailKey("1234");
        billPayeeDetails1.setDebitAmount(BigDecimal.valueOf(1000));

        billPayeeDetails2.setGlcode("3401002");
        billPayeeDetails2.setAccountDetailType("contractor");
        billPayeeDetails2.setAccountDetailKey("Cl2PMano0003");
        billPayeeDetails2.setCreditAmount(BigDecimal.valueOf(1000));

        billRegister.getBillPayeeDetails().add(billPayeeDetails1);
        billRegister.getBillPayeeDetails().add(billPayeeDetails2);
    }

    @Test
    public void shouldValidateBillRegister() {
        errors = billService.validateBillRegister(billRegister);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldPopulateEgBillregister() throws ClassNotFoundException {
        billService.populateBillRegister(egBillregister, billRegister);
        assertEquals(2, egBillregister.getEgBilldetailes().size());
        assertEquals(WorksConstants.APPROVED, egBillregister.getStatus().getCode());
    }

    @Test
    public void shouldCreateBill() throws ClassNotFoundException {
        billService.populateBillRegister(egBillregister, billRegister);
        final EgBillregister savedEgBillregister = billService.createBill(egBillregister);
        assertEquals(savedEgBillregister.getEgBilldetailes().size(), egBillregister.getEgBilldetailes().size());
    }

    @Test
    public void shouldGiveErrorsIfMandatoryFieldsNotPresent() {
        billRegister.setProjectCode("");
        billRegister.setBillType("");
        billRegister.setPayTo("");
        billRegister.setDepartmentCode("");
        billRegister.setFunctionCode("");
        billRegister.setFundCode("");
        errors = billService.validateBillRegister(billRegister);
        assertEquals(6, errors.size());
    }

    @Test
    public void shouldGiveErrorsIfBillDatesNotProper() {
        billRegister.setBillDate(DateUtils.getDate("20-02-2011", "dd-MM-yyyy"));
        billRegister.setPartyBillDate(DateUtils.getDate("20-02-2018", "dd-MM-yyyy"));
        when(financialYearHibernateDAO.getFinancialYearByDate(Matchers.any()))
                .thenThrow(new ApplicationRuntimeException("Financial Year is not active For Posting."));
        errors = billService.validateBillRegister(billRegister);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldGiveErrorForInvalidNameOfWork() {
        billRegister.setNameOfWork("Building Contruction\n'");
        when(projectCodeService.findActiveProjectCodeByCode(Matchers.anyString())).thenReturn(null);
        errors = billService.validateBillRegister(billRegister);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldGiveErrorsIfBillDetailsEmpty() {
        billRegister.getBillDetails().clear();
        billRegister.getBillPayeeDetails().clear();
        errors = billService.validateBillRegister(billRegister);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldGiveErrorsIfInvalidGlcode() {
        when(chartOfAccountsService.getByGlCode(Matchers.anyString())).thenReturn(null);
        errors = billService.validateBillRegister(billRegister);
        assertEquals(4, errors.size());
    }

    @Test
    public void shouldGiveErrorsIfInvalidDetailGlcode() {
        chartOfAccount1.setClassification(1l);
        errors = billService.validateBillRegister(billRegister);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldGiveErrorsIfAmountNegative() {
        billRegister.getBillDetails().get(0).setDebitAmount(new BigDecimal(-1000));
        billRegister.getBillDetails().get(1).setCreditAmount(new BigDecimal(-1000));
        errors = billService.validateBillRegister(billRegister);
        assertEquals(4, errors.size());
    }

    @Test
    public void shouldGiveErrorsIfInvalidPayeeDetails() {
        billRegister.getBillPayeeDetails().get(0).setGlcode("");
        billRegister.getBillPayeeDetails().get(0).setAccountDetailType("");
        billRegister.getBillPayeeDetails().get(0).setAccountDetailKey("abcd123");
        billRegister.getBillPayeeDetails().get(0).setCreditAmount(null);
        billRegister.getBillPayeeDetails().get(0).setDebitAmount(null);
        errors = billService.validateBillRegister(billRegister);
        assertEquals(4, errors.size());
    }
}
