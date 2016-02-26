/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.actions.masters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.service.CommonsService;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.model.bills.EgBillregister;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.models.estimate.ProjectCodeGenerator;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.models.masters.DepositCodeGenerator;
import org.egov.works.models.masters.NatureOfWork;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.egov.works.web.actions.estimate.AjaxFinancialDetailAction;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Result(name = SubledgerCodeAction.NEW, location = "subledgerCode-new.jsp")
public class SubledgerCodeAction extends BaseFormAction {

    private static final long serialVersionUID = 3780456881338376293L;
    private DepositCode depositCode = new DepositCode();
    private static final Logger logger = Logger.getLogger(SubledgerCodeAction.class);
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private CommonsService commonsService;
    private static final String ADMIN_HIERARCHY_TYPE = "ADMINISTRATION";
    private WorksService worksService;
    private String currentFinancialYearId;
    private static final String SCRIPT_NAME = "works.subledgerCode.showFields";
    private List<String> list;
    private boolean depCode;
    private boolean prjctCode;
    private ProjectCode projectCode = new ProjectCode();
    private PersistenceService<DepositCode, Integer> depositCodeService;
    private PersistenceService<ProjectCode, Long> projectCodeService;
    private ContractorBillService contractorBillService;
    private EgovCommon egovCommon;
    private DepositCodeGenerator depositCodeGenerator;
    private ProjectCodeGenerator projectcodeGenerator;
    private static final Boolean ISACTIVE = true;
    private String codeName;
    private String description;
    private Long projectCodeId;
    private String estimateNumber;
    private Double projectValue;
    private Date completionDate;
    private Boolean isProjectClose;
    private String lastVoucherDate;
    private final Map<String, Object> projectDetails = new HashMap<String, Object>();
    @Autowired
    private ScriptService scriptService;
    @Autowired
    private HierarchyTypeService hierarchyTypeService;
    @Autowired
    private BoundaryTypeService boundaryTypeService;
    @Autowired
    private BoundaryService boundaryService;

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return depositCode;
    }

    @Override
    public void prepare() {
        logger.debug("Inside Prepare Method..........");
        isProjectClose = Boolean.FALSE;
        // To Read list of Fields to be shown in UI from the script.
        // Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,SCRIPT_NAME).get(0);
        if (prjctCode) {
            final ScriptContext scriptContext = ScriptService.createContext("depCode", null, "projCode", projectCode);
            list = (List<String>) scriptService.executeScript(SCRIPT_NAME, scriptContext);
            // list = (List<String>) validScript.eval(Script.createContext("depCode",null,"projCode",projectCode));
        } else {
            depCode = true;  // Onload to set DepositCode Radio Button
            ScriptService.createContext("depCode", depositCode, "projCode", null);
        }
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        ajaxEstimateAction.setAssignmentService(assignmentService);
        final AjaxFinancialDetailAction ajaxFinancialDetailAction = new AjaxFinancialDetailAction();
        ajaxFinancialDetailAction.setPersistenceService(getPersistenceService());
        super.prepare();
        setupDropdownDataExcluding("typeOfWork", "subTypeOfWork", "fundSource", "function", "ward", "zone", "fund", "scheme",
                "subScheme");
        addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=true"));
        addDropdownData("typeOfWorkList",
                getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
        // TODO: Need to uncomment
        // addDropdownData("functionList", commonsService.getAllFunction());
        // TODO: Need to remove
        addDropdownData("functionList", new ArrayList<CFunction>());
        // TO load Fund and FundSource dropdown with appconfig defined values in case of SubLedgerDepositCode
        if (null != null && (list.isEmpty() || !list.isEmpty()) && depCode) {
            String config = worksService.getWorksConfigValue("SLDEPOSITCODE_SHOW_FUNDS");
            List<String> code = new ArrayList<String>();
            if (config == null)
                addDropdownData("fundList", code);
            else {
                for (int i = 0; i < config.split(",").length; i++)
                    code.add(config.split(",")[i]);
                addDropdownData("fundList", getPersistenceService().findAllByNamedQuery("getListOfFundsForCodes", code));
            }

            config = worksService.getWorksConfigValue("SLDEPOSITCODE_SHOW_FUNDSOURCE");
            code = new ArrayList<String>();
            if (config == null)
                addDropdownData("fundSourceList", code);
            else {
                for (int i = 0; i < config.split(",").length; i++)
                    code.add(config.split(",")[i]);
                addDropdownData("fundSourceList",
                        getPersistenceService().findAllByNamedQuery("getListOfFundSourceForCodes", code));
            }

        } else {
            // TODO: Uncomment
            // addDropdownData("fundList", commonsService.getAllActiveIsLeafFunds());
            // TODO: Remove
            addDropdownData("fundList", new ArrayList<Fund>());
            try {
                // TODO : Uncomment
                // addDropdownData("fundSourceList", commonsService.getAllActiveIsLeafFundSources());
                // TODO : Remove
                addDropdownData("fundSourceList", new ArrayList<Fundsource>());
            } catch (final Exception e) {
                logger.error("---Unable to load fund source information---" + e.getMessage());
                addFieldError("fundsourceunavailable", "Unable to load fund source information");
            }
        }

        /**
         * Fetch Zone Dropdown List
         */
        // TODO: Uncomment
        // addDropdownData("zoneList", getAllZone());
        // addDropdownData("wardList", Collections.emptyList());
        addDropdownData("subTypeOfWorkList", Collections.emptyList());
        addDropdownData("schemeList", Collections.emptyList());
        addDropdownData("subSchemeList", Collections.emptyList());
        final CFinancialYear financialYear = getCurrentFinancialYear();
        if (financialYear != null)
            currentFinancialYearId = financialYear.getId().toString();
    }

    /* To Populate Zone DropDown */
    public List<Boundary> getAllZone() {
        final HierarchyType hType = hierarchyTypeService.getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
        List<Boundary> zoneList = null;
        final BoundaryType bType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType("zone", hType);
        zoneList = boundaryService.getAllBoundariesByBoundaryTypeId(bType.getId());
        return zoneList;
    }

    /* To Get Current Financial Year Range */
    protected CFinancialYear getCurrentFinancialYear() {
        final String finyearRange = worksService.getWorksConfigValue("FINANCIAL_YEAR_RANGE");
        if (StringUtils.isNotBlank(finyearRange))
            return commonsService.getFinancialYearByFinYearRange(finyearRange);
        else
            return commonsService.getFinancialYearById(Long.valueOf(commonsService.getCurrYearFiscalId()));
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    public String close() {
        isProjectClose = Boolean.TRUE;
        projectCode = projectCodeService.findById(projectCodeId, false);
        projectCode.setEgwStatus(commonsService.getStatusByModuleAndCode(ProjectCode.class.getSimpleName(), "CLOSED"));
        projectCode.setProjectValue(projectValue);
        projectCode.setCompletionDate(completionDate);
        projectCodeService.persist(projectCode);
        return SUCCESS;
    }

    public String changeStatus() {

        return "changeStatus";
    }

    public String searchProjectDetails() {
        search();

        return "changeStatus";
    }

    private void search() {
        List<Map<String, String>> expenditureDetails = null;
        String queryStr = "from AbstractEstimate ae where ae.projectCode.id=:projectCodeId";
        if (estimateNumber != null && !StringUtils.isEmpty(estimateNumber))
            queryStr = queryStr + " and upper(ae.estimateNumber) like '%'||:estimateNumber||'%'";
        final Query query = getPersistenceService().getSession().createQuery(queryStr);
        if (estimateNumber != null && !StringUtils.isEmpty(estimateNumber))
            query.setString("estimateNumber", estimateNumber);
        query.setLong("projectCodeId", projectCodeId);
        final AbstractEstimate estimate = (AbstractEstimate) query.uniqueResult();
        if (estimate == null)
            addActionError(getText("search.noresultfound"));
        else {
            expenditureDetails = egovCommon.getExpenditureDetailsforProject(estimate.getProjectCode().getId(), new Date());
            projectDetails.put("estimate", estimate);
            populateVoucherDetails(expenditureDetails, estimate);
        }
    }

    private void populateVoucherDetails(List<Map<String, String>> expenditureDetails, final AbstractEstimate estimate) {
        final List<Long> billIds = new ArrayList<Long>();
        final List<HashMap<String, Object>> voucherdetails = new ArrayList<HashMap<String, Object>>();
        Date lastVoucherCreatedDate = null;
        Date lastBillCreatedDate = null;
        Double totalExpense = 0.0;
        boolean isVoucherExist = false;
        HashMap<String, Object> voucher = null;
        if (expenditureDetails == null || expenditureDetails.isEmpty()) {
            logger.debug("No Voucher Exists in the system for the Estimate: " + estimate.getEstimateNumber());
            expenditureDetails = Collections.EMPTY_LIST;
        } else if (logger.isDebugEnabled())
            logger.debug("Voucher Exists in the system for the Estimate: " + estimate.getEstimateNumber() + "||Vouchers :"
                    + expenditureDetails);
        for (final Map<String, String> expenditureDetail : expenditureDetails) {
            voucher = new HashMap<String, Object>();
            voucher.put("voucherNumber", expenditureDetail.get("VoucherNumber"));
            voucher.put("amount", Double.valueOf(expenditureDetail.get("Amount")));
            totalExpense = totalExpense + Double.valueOf(expenditureDetail.get("Amount"));
            if (expenditureDetail.get("BillId") != null) {
                voucher.put("billNumber", expenditureDetail.get("BillNumber"));
                billIds.add(Long.valueOf(expenditureDetail.get("BillId")));
            } else
                voucher.put("billNumber", "");
            if (lastVoucherCreatedDate == null)
                lastVoucherCreatedDate = DateUtils.getDate(expenditureDetail.get("VoucherDate"), "dd/MM/yyyy");
            else if (!lastVoucherCreatedDate.after(DateUtils.getDate(expenditureDetail.get("VoucherDate"), "dd/MM/yyyy")))
                lastVoucherCreatedDate = DateUtils.getDate(expenditureDetail.get("VoucherDate"), "dd/MM/yyyy");

            voucherdetails.add(voucher);
        }
        for (final EgBillregister egbr : contractorBillService.getListOfApprovedBillforEstimate(estimate, new Date())) {
            for (final Long id : billIds)
                if (id.longValue() == egbr.getId().longValue()) {
                    isVoucherExist = true;
                    break;
                } else
                    isVoucherExist = false;
            if (!isVoucherExist) {
                voucher = new HashMap<String, Object>();
                voucher.put("voucherNumber", "");
                voucher.put("amount", egbr.getBillamount());
                voucher.put("date", DateUtils.getFormattedDate(egbr.getBilldate(), "dd/MM/yyyy"));
                voucher.put("billNumber", egbr.getBillnumber());
                totalExpense = totalExpense + egbr.getBillamount().doubleValue();
                voucherdetails.add(voucher);
                if (lastBillCreatedDate == null)
                    lastBillCreatedDate = egbr.getBilldate();
                else if (!lastBillCreatedDate.after(egbr.getBilldate()))
                    lastBillCreatedDate = egbr.getBilldate();

            }
            isVoucherExist = false;
        }
        projectDetails.put("totalExpense", totalExpense);
        projectDetails.put("voucherDetails", voucherdetails);

        if (lastVoucherCreatedDate == null && lastBillCreatedDate != null)
            projectDetails.put("lastCreatedBillorVoucherDate", DateUtils.getFormattedDate(lastBillCreatedDate, "dd/MM/yyyy"));
        if (lastBillCreatedDate == null && lastVoucherCreatedDate != null)
            projectDetails.put("lastCreatedBillorVoucherDate", DateUtils.getFormattedDate(lastVoucherCreatedDate, "dd/MM/yyyy"));

        if (lastVoucherCreatedDate != null && lastBillCreatedDate != null)
            if (lastVoucherCreatedDate.after(lastBillCreatedDate))
                projectDetails.put("lastCreatedBillorVoucherDate",
                        DateUtils.getFormattedDate(lastVoucherCreatedDate, "dd/MM/yyyy"));
            else
                projectDetails.put("lastCreatedBillorVoucherDate", DateUtils.getFormattedDate(lastBillCreatedDate, "dd/MM/yyyy"));
        lastVoucherDate = (String) projectDetails.get("lastCreatedBillorVoucherDate");
    }

    public SubledgerCodeAction() {
        addRelatedEntity("fundSource", Fundsource.class);
        addRelatedEntity("department", Department.class);
        addRelatedEntity("fund", Fund.class);
        addRelatedEntity("natureOfWork", NatureOfWork.class);
        addRelatedEntity("typeOfWork", EgwTypeOfWork.class);
        addRelatedEntity("subTypeOfWork", EgwTypeOfWork.class);
        addRelatedEntity("function", CFunction.class);
        addRelatedEntity("ward", Boundary.class);
        addRelatedEntity("zone", Boundary.class);
        addRelatedEntity("scheme", Scheme.class);
        addRelatedEntity("subScheme", SubScheme.class);
        addRelatedEntity("financialYear", CFinancialYear.class);
    }

    public String save() {
        logger.debug("Inside save method().....Account Entity Type DepositCode : " + depCode + ".....ProjectCode : " + prjctCode);
        if (depCode) {
            try {
                depositCode.setCode(depositCodeGenerator.getAutoGeneratedDepositCode(depositCode));
            } catch (final ValidationException sequenceException) {
                final CFinancialYear financialYear = depositCode.getFinancialYear();
                final List<ValidationError> errorList = sequenceException.getErrors();
                for (final ValidationError error : errorList)
                    if (error.getMessage().contains("DatabaseSequenceFirstTimeException")) {
                        prepare();
                        depositCode.setFinancialYear(financialYear);
                        throw new ValidationException(Arrays.asList(new ValidationError("error", error.getMessage())));
                    }
                throw sequenceException;
            }
            depositCode.setIsActive(ISACTIVE);
            depositCodeService.persist(depositCode);  // Persists an Entry in EGW_DEPOSITCODE Table
            worksService.createAccountDetailKey(depositCode.getId(), "DEPOSITCODE"); // Persists an Entry in ACCOUNTDETAILKEY
            // Table
        } else if (prjctCode) {
            try {
                projectCode.setCode(projectcodeGenerator.getAutoGeneratedProjectCode(depositCode));
            } catch (final ValidationException sequenceException) {
                final List<ValidationError> errorList = sequenceException.getErrors();
                for (final ValidationError error : errorList)
                    if (error.getMessage().contains("DatabaseSequenceFirstTimeException")) {
                        prepare();
                        throw new ValidationException(Arrays.asList(new ValidationError("error", error.getMessage())));
                    }
                throw sequenceException;
            }
            projectCode.setCodeName(codeName);
            projectCode.setDescription(description);
            projectCode.setActive(ISACTIVE);
            projectCode.setEgwStatus(commonsService.getStatusByModuleAndCode(ProjectCode.class.getSimpleName(),
                    WorksConstants.DEFAULT_PROJECTCODE_STATUS));
            projectCodeService.persist(projectCode); // Persists an Entry in EGW_PROJECTCODE Table
            worksService.createAccountDetailKey(projectCode.getId(), "PROJECTCODE"); // Persists an Entry in ACCOUNTDETAILKEY
            // Table
        }
        return SUCCESS;
    }

    @Action(value = "/masters/subledgerCode-newform")
    public String newform() {
        return NEW;
    }

    public CommonsService getCommonsService() {
        return commonsService;
    }

    public void setCommonsService(final CommonsService commonsService) {
        this.commonsService = commonsService;
    }

    public WorksService getWorksService() {
        return worksService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public String getCurrentFinancialYearId() {
        return currentFinancialYearId;
    }

    public void setCurrentFinancialYearId(final String currentFinancialYearId) {
        this.currentFinancialYearId = currentFinancialYearId;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(final List<String> list) {
        this.list = list;
    }

    public DepositCode getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(final DepositCode depositCode) {
        this.depositCode = depositCode;
    }

    public boolean isPrjctCode() {
        return prjctCode;
    }

    public void setPrjctCode(final boolean prjctCode) {
        this.prjctCode = prjctCode;
    }

    public void setProjectCode(final ProjectCode projectCode) {
        this.projectCode = projectCode;
    }

    public ProjectCode getProjectCode() {
        return projectCode;
    }

    public void setDepositCodeService(
            final PersistenceService<DepositCode, Integer> depositCodeService) {
        this.depositCodeService = depositCodeService;
    }

    public void setDepositCodeGenerator(final DepositCodeGenerator depositCodeGenerator) {
        this.depositCodeGenerator = depositCodeGenerator;
    }

    public boolean isDepCode() {
        return depCode;
    }

    public void setDepCode(final boolean depCode) {
        this.depCode = depCode;
    }

    public void setProjectcodeGenerator(final ProjectCodeGenerator projectcodeGenerator) {
        this.projectcodeGenerator = projectcodeGenerator;
    }

    public void setProjectCodeService(
            final PersistenceService<ProjectCode, Long> projectCodeService) {
        this.projectCodeService = projectCodeService;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(final String codeName) {
        this.codeName = codeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getProjectCodeId() {
        return projectCodeId;
    }

    public void setProjectCodeId(final Long projectCodeId) {
        this.projectCodeId = projectCodeId;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public Map<String, Object> getProjectDetails() {
        return projectDetails;
    }

    public Double getProjectValue() {
        return projectValue;
    }

    public void setProjectValue(final Double projectValue) {
        this.projectValue = projectValue;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(final Date completionDate) {
        this.completionDate = completionDate;
    }

    public Boolean getIsProjectClose() {
        return isProjectClose;
    }

    public void setContractorBillService(final ContractorBillService contractorBillService) {
        this.contractorBillService = contractorBillService;
    }

    public String getLastVoucherDate() {
        return lastVoucherDate;
    }
}
