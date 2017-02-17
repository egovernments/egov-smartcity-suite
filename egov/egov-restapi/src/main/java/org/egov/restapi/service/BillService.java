package org.egov.restapi.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.service.AccountdetailtypeService;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.service.FunctionService;
import org.egov.commons.service.FundService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.BillDetails;
import org.egov.restapi.model.BillPayeeDetails;
import org.egov.restapi.model.BillRegister;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.services.ProjectCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BillService {

    @Autowired
    private FundService fundService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private SubSchemeService subSchemeService;

    @Autowired
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    private ExpenseBillService expenseBillService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AccountdetailtypeService accountdetailtypeService;

    @Autowired
    private ProjectCodeService projectCodeService;

    @Autowired
    private ApplicationContext applicationContext;

    public ErrorDetails validateBillDetails(final BillRegister externalBillRegister) {
        ErrorDetails errorDetails = null;
        if (externalBillRegister.getDepartmentCode() == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_DEPARTMENT);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_DEPARTMENT);
        } else if (externalBillRegister.getFunctionCode() == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_FUNCTION);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_FUNCTION);
        } else if (externalBillRegister.getProjectCode() == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_WINCODE);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_WINCODE);
        } else if (externalBillRegister.getBillDate() == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_BILLDATE);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_BILLDATE);
        } else if (externalBillRegister.getBillType() == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_BILLTYPE);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_BILLTYPE);
        } else if (externalBillRegister.getFundCode() == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_FUND);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_FUND);
        } /*
           * else if (!externalBillRegister.getExternalBillDetails().isEmpty()) { errorDetails = new ErrorDetails();
           * errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_DEBITCOA);
           * errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NO_DEBITCOA); }
           */
        return errorDetails;
    }

    public void populateBillRegister(final EgBillregister egBillregister, final BillRegister billRegister) throws ClassNotFoundException {
        populateEgBillregister(egBillregister, billRegister);
        populateEgBillregisterMis(egBillregister, billRegister);

        for (final BillDetails details : billRegister.getBillDetails())
            populateEgBilldetails(egBillregister, details, billRegister);
    }

    private void populateEgBillregister(final EgBillregister egBillregister, final BillRegister billRegister) {
        egBillregister.setBilldate(billRegister.getBillDate());
        egBillregister.setBilltype(billRegister.getBillType());
        egBillregister.setBillamount(billRegister.getBillAmount());
    }

    private void populateEgBillregisterMis(final EgBillregister egBillregister, final BillRegister billRegister) {
        final EgBillregistermis egBillregistermis = new EgBillregistermis();
        egBillregistermis.setFunction(functionService.findByCode(billRegister.getFunctionCode()));
        egBillregistermis.setFund(fundService.findByCode(billRegister.getFundCode()));
        egBillregistermis.setScheme(schemeService.findByCode(billRegister.getSchemeCode()));
        egBillregistermis.setSubScheme(subSchemeService.findByCode(billRegister.getSubSchemeCode()));
        egBillregistermis.setEgDepartment(departmentService.getDepartmentByCode(billRegister.getDepartmentCode()));
        egBillregistermis.setPayto(billRegister.getPayTo());
        egBillregistermis.setNarration(billRegister.getNarration());
        egBillregistermis.setPartyBillNumber(billRegister.getPartyBillNumber());
        egBillregistermis.setPartyBillDate(billRegister.getPartyBillDate());

        egBillregister.setEgBillregistermis(egBillregistermis);
    }

    private void populateEgBilldetails(final EgBillregister egBillregister, final BillDetails details,
            final BillRegister billRegister) throws ClassNotFoundException {
        final EgBilldetails egBilldetails = new EgBilldetails();
        egBilldetails.setGlcodeid(BigDecimal.valueOf(chartOfAccountsService.getByGlCode(details.getGlcode()).getId()));
        egBilldetails.setCreditamount(details.getCreditAmount());
        egBilldetails.setDebitamount(details.getDebitAmount());
        egBilldetails.setEgBillregister(egBillregister);
        egBilldetails.setLastupdatedtime(new Date());
        egBilldetails.setFunctionid(BigDecimal.valueOf(egBillregister.getEgBillregistermis().getFunction().getId()));
        for (final BillPayeeDetails payeeDetails : billRegister.getBillPayeeDetails())
            populateEgBillPayeedetails(egBilldetails, payeeDetails, details, billRegister);
        egBillregister.addEgBilldetailes(egBilldetails);
    }

    @SuppressWarnings("unchecked")
    private void populateEgBillPayeedetails(final EgBilldetails egBilldetails, final BillPayeeDetails payeeDetails,
            final BillDetails details, final BillRegister billRegister) throws ClassNotFoundException {
        final EgBillPayeedetails billPayeedetails = new EgBillPayeedetails();
        if (payeeDetails.getGlcode() != null && payeeDetails.getGlcode().equals(details.getGlcode())) {
            billPayeedetails.setCreditAmount(details.getCreditAmount());
            billPayeedetails.setDebitAmount(details.getDebitAmount());
            final Accountdetailtype detailType = accountdetailtypeService
                    .findByName(payeeDetails.getAccountDetailType());
            billPayeedetails.setAccountDetailTypeId(detailType.getId());
            if (payeeDetails.getAccountDetailType() != null) {
                List<EntityType> entities;
                final String table = detailType.getFullQualifiedName();
                final Class<?> service = Class.forName(table);
                String simpleName = service.getSimpleName();
                simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";

                final EntityTypeService entityService = (EntityTypeService) applicationContext.getBean(simpleName);
                entities = (List<EntityType>) entityService
                        .filterActiveEntities(billRegister.getProjectCode(), 10, detailType.getId());
                if (entities.isEmpty())
                    entities = (List<EntityType>) entityService
                            .filterActiveEntities(billRegister.getPayTo(), 10, detailType.getId());
                billPayeedetails.setAccountDetailKeyId(entities.get(0).getEntityId());
            }
            billPayeedetails.setEgBilldetailsId(egBilldetails);
            billPayeedetails.setLastUpdatedTime(new Date());
            egBilldetails.addEgBillPayeedetail(billPayeedetails);
        }
    }

    public void createProjectCode(final BillRegister billRegister) {
        final ProjectCode projectCode = projectCodeService.findActiveProjectCodeByCode(billRegister.getProjectCode());
        if (projectCode == null)
            projectCodeService.createProjectCode(billRegister.getProjectCode(), billRegister.getNameOfWork());
    }

    public EgBillregister createBill(final EgBillregister egBillregister) {
        return expenseBillService.create(egBillregister, null, null, null, "Create And Approve");
    }
}
