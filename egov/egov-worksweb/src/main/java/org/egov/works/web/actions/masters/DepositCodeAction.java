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
package org.egov.works.web.actions.masters;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.dao.FundSourceHibernateDAO;
import org.egov.commons.repository.CFinancialYearRepository;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.master.service.DepositCodeService;
import org.egov.works.models.estimate.ProjectCodeGenerator;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.models.masters.DepositCodeGenerator;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.egov.works.web.actions.estimate.AjaxFinancialDetailAction;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({
        @Result(name = DepositCodeAction.NEW, location = "depositCode-new.jsp"),
        @Result(name = DepositCodeAction.SUCCESS, location = "depositCode-success.jsp"),
        @Result(name = DepositCodeAction.SEARCH, location = "depositCode-search.jsp")

})
public class DepositCodeAction extends BaseFormAction {

    private static final long serialVersionUID = 3780456881338376293L;
    @Required(message = "sor.category.not.null")
    private DepositCode depositCode = new DepositCode();
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private WorksService worksService;
    @Autowired
    private FundSourceHibernateDAO fundSourceHibernateDAO;
    @Autowired
    private FundHibernateDAO fundHibernateDAO;
    private String currentFinancialYearId;
    private List<String> list;
    private boolean depCode;
    private EgovCommon egovCommon;
    private DepositCodeGenerator depositCodeGenerator;
    private static final boolean ISACTIVE = true;
    private String codeName;
    private String description;
    private String estimateNumber;
    private Date completionDate;
    private String lastVoucherDate;
    private final Map<String, Object> projectDetails = new HashMap<String, Object>();
    @Autowired
    private DepositCodeService depositCodeService;
    @Autowired
    private CFinancialYearRepository cFinancialYearRepository;
    public static final String SEARCH = "search";
    private List<DepositCode> depositCodeList = null;

    @Override
    public Object getModel() {

        return depositCode;
    }

    @Action(value = "/masters/depositCode-search")
    public String searchDepositCode() {
        setDepositCodeList(depositCodeService.getAllDepositCodes());
        return SEARCH;
    }

    @Override
    public void prepare() {
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        ajaxEstimateAction.setAssignmentService(assignmentService);
        final AjaxFinancialDetailAction ajaxFinancialDetailAction = new AjaxFinancialDetailAction();
        ajaxFinancialDetailAction.setPersistenceService(getPersistenceService());
        super.prepare();
        setupDropdownDataExcluding("typeOfWork", "subTypeOfWork", "fundSource", "function", "ward", "zone", "fund", "scheme",
                "subScheme");
        addDropdownData("financialYearList", cFinancialYearRepository.getAllFinancialYears());
        addDropdownData("fundList", fundHibernateDAO.findAllActiveFunds());
        try {
            addDropdownData("fundSourceList", fundSourceHibernateDAO.findAllActiveIsLeafFundSources());
        } catch (final Exception e) {
            addFieldError("fundsourceunavailable", getText("deposit.fundsourc.load"));
        }
    }

    @Override
    public String execute() {
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
        String queryStr = "from DepositCode ";
        if (estimateNumber != null && !StringUtils.isEmpty(estimateNumber))
            queryStr = queryStr + " and upper(ae.estimateNumber) like '%'||:estimateNumber||'%'";
        final Query query = getPersistenceService().getSession().createQuery(queryStr);
        if (estimateNumber != null && !StringUtils.isEmpty(estimateNumber))
            query.setString("estimateNumber", estimateNumber);
        final AbstractEstimate estimate = (AbstractEstimate) query.uniqueResult();
        if (estimate == null)
            addActionError(getText("search.noresultfound"));
        else {
            egovCommon.getExpenditureDetailsforProject(estimate.getProjectCode().getId(), new Date());
            projectDetails.put("estimate", estimate);

        }
    }

    public DepositCodeAction() {
        addRelatedEntity("fundSource", Fundsource.class);
        addRelatedEntity("fund", Fund.class);
        addRelatedEntity("financialYear", CFinancialYear.class);
    }

    @Action(value = "/masters/depositcode-save")
    public String save() {
        depositCode.setCode(depositCodeGenerator.getAutoGeneratedDepositCode(depositCode));
        depositCode.setIsActive(ISACTIVE);
        depositCode = depositCodeService.persist(depositCode);
        depositCodeService.createAccountDetailKey(depositCode); // Persists an Entry in ACCOUNTDETAILKEY Table
        return SUCCESS;
    }

    @Action(value = "/masters/depositCode-newform")
    public String newform() {
        return NEW;
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

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public Map<String, Object> getProjectDetails() {
        return projectDetails;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(final Date completionDate) {
        this.completionDate = completionDate;
    }

    public void setContractorBillService(final ContractorBillService contractorBillService) {
    }

    public String getLastVoucherDate() {
        return lastVoucherDate;
    }

    public List<DepositCode> getDepositCodeList() {
        return depositCodeList;
    }

    public void setDepositCodeList(final List<DepositCode> depositCodeList) {
        this.depositCodeList = depositCodeList;
    }
}
