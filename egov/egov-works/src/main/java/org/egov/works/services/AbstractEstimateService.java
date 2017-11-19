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
package org.egov.works.services;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.common.entity.UOM;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetUsage;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimateAppropriation;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.autonumber.EstimateNumberGenerator;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.models.estimate.BudgetNumberGenerator;
import org.egov.works.models.estimate.DepositWorksUsage;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.models.estimate.ProjectCodeGenerator;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class AbstractEstimateService extends PersistenceService<AbstractEstimate, Long> {
    private static final Logger logger = Logger.getLogger(AbstractEstimateService.class);

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    private BudgetNumberGenerator budgetNumberGenerator;
    @Autowired
    private ProjectCodeGenerator projectcodeGenerator;
    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;
    @Autowired
    private ProjectCodeService projectCodeService;
    private PersistenceService<AbstractEstimateAppropriation, Long> estimateAppropriationService;
    private DepositWorksUsageService depositWorksUsageService;

    private BudgetDetailsDAO budgetDetailsDAO;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private AssignmentService assignmentService;
    private BudgetGroupDAO budgetGroupDAO;
    private WorksService worksService;

    public static final String APPROVED = "APPROVED";
    public static final String UNAPPROVED = "UNAPPROVED";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private EgovCommon egovCommon;
    private static final String FROM_DATE = "fromDate";
    private static final String TO_DATE = "toDate";
    private PersistenceService<BudgetUsage, Long> budgetUsageService;
    private WorkOrderService workOrderService;
    private WorksPackageService workspackageService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

    public void setBudgetNumberGenerator(final BudgetNumberGenerator budgetNumberGenerator) {
        this.budgetNumberGenerator = budgetNumberGenerator;
    }

    public AbstractEstimateService(Class<AbstractEstimate> type) {
        super(type);
    }

    public AbstractEstimateService() {
        super(AbstractEstimate.class);
    }

    /**
     * This method invokes the script service to generate the budget appropriation number. This method is invoked from the work
     * flow rules.
     *
     * @param entity an instance of <code>AbstractEstimate</code>
     * @return a <code>String<code> containing the generated the budget appropriation number.
     */
    public String getBudgetAppropriationNumber(final AbstractEstimate entity) {
        CFinancialYear finYear = null;
        try {
            if (isPreviousYearApprRequired(entity.getFinancialDetails().get(0)))
                finYear = getPreviousFinancialYear();
            else
                finYear = getCurrentFinancialYear(new Date());

            return budgetNumberGenerator.getBudgetApprNo(entity, finYear);
        } catch (final ValidationException sequenceException) {
            throw sequenceException;
        }
    }

    /**
     * The method return true if the estimate number has to be re-generated
     *
     * @param entity an instance of <code>AbstractEstimate</code> containing the estimate date
     * @param financialYear an instance of <code>CFinancialYear</code> representing the financial year for the estimate date.
     * @return a boolean value indicating if the estimate number change is required.
     */
    private boolean estimateNumberChangeRequired(final AbstractEstimate entity, final CFinancialYear financialYear) {
        final String[] estNum = entity.getEstimateNumber().split("/");

        if (estNum[0].equals(entity.getExecutingDepartment().getCode())
                && estNum[1].equals(financialYear.getFinYearRange()))
            return false;
        return true;
    }

    public void setEstimateNumber(final AbstractEstimate entity) {
        final CFinancialYear financialYear = getCurrentFinancialYear(entity.getEstimateDate());
        if (entity.getEstimateNumber() == null || entity.getEstimateNumber() != null
                && estimateNumberChangeRequired(entity, financialYear)){
            EstimateNumberGenerator e = beanResolver.getAutoNumberServiceFor(EstimateNumberGenerator.class);
            final String estimateNumber = e.getEstimateNumber(entity, financialYear);
            entity.setEstimateNumber(estimateNumber);
        }
    }

    /**
     * This method generates and set the project code for the estimate
     *
     * @param entity an instance of <code>AbstractEstimate</code> for the project code is to be generated.
     */
    public void setProjectCode(final AbstractEstimate entity) {
        final CFinancialYear finYear = getCurrentFinancialYear(entity.getEstimateDate());
        final ProjectCode projectCode = new ProjectCode(entity, null);
        projectCode.setCode(projectcodeGenerator.generateProjectcode(entity, finYear));

        projectCode.setCodeName(entity.getName());
        projectCode.setDescription(entity.getName());
        projectCode.setActive(true);
        projectCode.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(ProjectCode.class.getSimpleName(),
                WorksConstants.DEFAULT_PROJECTCODE_STATUS));
        entity.setProjectCode(projectCode);
        projectCode.addEstimate(entity);
        projectCodeService.persist(projectCode);
        createAccountDetailKey(projectCode);
    }

    protected void createAccountDetailKey(final ProjectCode proj) {
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("PROJECTCODE");
        final Accountdetailkey adk = new Accountdetailkey();
        adk.setGroupid(1);
        adk.setDetailkey(proj.getId().intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        adk.setAccountdetailtype(accountdetailtype);
        accountdetailkeyHibernateDAO.create(adk);

    }

    /**
     * This method performs the required validations and persists the <code>FinancialDetail</code> object
     *
     * @param financialDetail the <code>FinancialDetail</code> object to be persisted.
     * @param estimate the parent <code>AbstractEstimate</code> object
     * @return the persisted <code>AbstractEstimate</code> object
     */
    public AbstractEstimate persistFinancialDetail(final FinancialDetail financialDetail,
            final AbstractEstimate estimate) {
        super.validate(estimate);

        estimate.getFinancialDetails().clear();
        estimate.addFinancialDetails(financialDetail);

        return super.persist(estimate);
    }

    public List<AppConfigValues> getAppConfigValue(final String moduleName, final String key) {
        return appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, key);
    }

    public boolean checkForBudgetaryAppropriation(final FinancialDetail financialDetail, final String appropriationNumber)
            throws ValidationException {
        Long finyrId;
        double budgApprAmnt;
        Date budgetAppDate = null;
        if (isPreviousYearApprRequired(financialDetail))
            budgetAppDate = getPreviousFinancialYear().getEndingDate();
        else
            budgetAppDate = new Date();
        // CFinancialYear
        // estimateDate_finYear=financialYearHibernateDAO.getFinYearByDate(financialDetail.getAbstractEstimate().getEstimateDate());
        final CFinancialYear budgetApprDate_finYear = financialYearHibernateDAO.getFinYearByDate(budgetAppDate);
        final List<Long> budgetheadid = new ArrayList<Long>();
        budgetheadid.add(financialDetail.getBudgetGroup().getId());
        boolean flag = false;

        finyrId = budgetApprDate_finYear.getId();
        if (budgetAppDate.compareTo(financialDetail.getAbstractEstimate().getEstimateDate()) >= 0)
            for (final MultiYearEstimate multiYearEstimate : financialDetail.getAbstractEstimate()
                    .getMultiYearEstimates())
                if (multiYearEstimate != null && multiYearEstimate.getFinancialYear().getId().compareTo(finyrId) == 0
                        && multiYearEstimate.getPercentage() > 0) {
                    budgApprAmnt = financialDetail.getAbstractEstimate().getTotalAmount().getValue();
                    final double percAmt = budgApprAmnt * multiYearEstimate.getPercentage() / 100;
                    flag = checkConsumeEncumbranceBudget(financialDetail, finyrId, percAmt, budgetheadid, appropriationNumber);
                    if (flag != true)
                        return flag;
                }
        return flag;
    }

    public boolean checkConsumeEncumbranceBudget(final FinancialDetail financialDetail, final Long finyrId,
            final double budgApprAmnt, final List<Long> budgetheadid, final String appropriationNumber) {
        final boolean flag = true;
        final BudgetUsage budgetUsage = budgetDetailsDAO.consumeEncumbranceBudget(
                appropriationNumber == null ? null : appropriationNumber,
                finyrId,
                Integer.valueOf(11),
                financialDetail.getAbstractEstimate().getEstimateNumber(),
                Integer.parseInt(financialDetail.getAbstractEstimate().getUserDepartment().getId().toString()),
                financialDetail.getFunction() == null ? null : financialDetail.getFunction().getId(),
                financialDetail.getFunctionary() == null ? null : financialDetail.getFunctionary().getId(),
                financialDetail.getScheme() == null ? null : financialDetail.getScheme().getId(),
                financialDetail.getSubScheme() == null ? null : financialDetail.getSubScheme().getId(),
                financialDetail.getAbstractEstimate().getWard() == null ? null : Integer.parseInt(financialDetail
                        .getAbstractEstimate().getWard().getId().toString()),
                financialDetail.getBudgetGroup() == null ? null : budgetheadid,
                financialDetail.getFund() == null ? null : financialDetail.getFund().getId(), budgApprAmnt);

        if (budgetUsage != null)
            persistBudgetAppropriationDetails(financialDetail.getAbstractEstimate(), budgetUsage);
        else
            return false;

        return flag;
    }

    public boolean releaseBudgetOnReject(final FinancialDetail financialDetail) throws ValidationException {

        final AbstractEstimateAppropriation estimateAppropriation = estimateAppropriationService.findByNamedQuery(
                "getLatestBudgetUsageForEstimate", financialDetail.getAbstractEstimate().getId());
        final List<Long> budgetheadid = new ArrayList<Long>();
        budgetheadid.add(financialDetail.getBudgetGroup().getId());
        BudgetUsage budgetUsage = null;
        final boolean flag = false;
        // Generate Budget Rejection no here
        final String budgetRejectionNumber = "BC/" + estimateAppropriation.getBudgetUsage().getAppropriationnumber();
        budgetUsage = budgetDetailsDAO.releaseEncumbranceBudget(
                budgetRejectionNumber == null ? null : budgetRejectionNumber,
                estimateAppropriation.getBudgetUsage().getFinancialYearId().longValue(),
                Integer.valueOf(11),
                financialDetail.getAbstractEstimate().getEstimateNumber(),
                Integer.parseInt(financialDetail.getAbstractEstimate().getUserDepartment().getId().toString()),
                financialDetail.getFunction() == null ? null : financialDetail.getFunction().getId(),
                financialDetail.getFunctionary() == null ? null : financialDetail.getFunctionary().getId(),
                financialDetail.getScheme() == null ? null : financialDetail.getScheme().getId(),
                financialDetail.getSubScheme() == null ? null : financialDetail.getSubScheme().getId(),
                financialDetail.getAbstractEstimate().getWard() == null ? null : Integer.parseInt(financialDetail
                        .getAbstractEstimate().getWard().getId().toString()),
                financialDetail.getBudgetGroup() == null ? null : budgetheadid,
                financialDetail.getFund() == null ? null : financialDetail.getFund().getId(), estimateAppropriation
                        .getBudgetUsage().getConsumedAmount());

        if (financialDetail.getAbstractEstimate() != null)
            persistBudgetReleaseDetails(financialDetail.getAbstractEstimate(), budgetUsage);
        return flag;
    }

    /**
     * returns the sanctioned budget for the year
     *
     * @param paramMap
     * @return
     * @throws ValidationException
     */

    public BigDecimal getTotalGrantForYear(final FinancialDetail fd) throws ValidationException {
        BigDecimal val = BigDecimal.ZERO;
        final Map<String, Object> searchMap = new HashMap<String, Object>();
        CFinancialYear finYear = null;

        if (fd != null) {
            finYear = fd.getAbstractEstimate().getLeastFinancialYearForEstimate();
            searchMap.put("financialyearid", Long.valueOf(finYear.getId()));
        }

        if (fd != null) {
            final List<BudgetGroup> budgetheadid = new ArrayList<BudgetGroup>();
            budgetheadid.add(fd.getBudgetGroup());
            if (fd.getFunction() != null && fd.getFunction().getId() != null)
                searchMap.put("functionid", fd.getFunction().getId());
            if (fd.getFunctionary() != null && fd.getFunctionary().getId() != null)
                searchMap.put("functionaryid", fd.getFunctionary().getId());
            if (fd.getFund() != null && fd.getFund().getId() != null)
                searchMap.put("fundid", fd.getFund().getId());
            if (fd.getBudgetGroup() != null && fd.getBudgetGroup().getId() != null)
                searchMap.put("budgetheadid", budgetheadid);
            if (fd.getScheme() != null && fd.getScheme().getId() != null)
                searchMap.put("schemeid", fd.getScheme().getId());
            if (fd.getSubScheme() != null && fd.getSubScheme().getId() != null)
                searchMap.put("subschemeid", fd.getSubScheme().getId());
            if (fd.getAbstractEstimate().getUserDepartment() != null)
                searchMap.put("deptid", fd.getAbstractEstimate().getUserDepartment().getId());
            if (fd.getAbstractEstimate().getWard().getId() != null)
                searchMap.put("boundaryid", fd.getAbstractEstimate().getWard().getId());
        }

        try {
            val = budgetDetailsDAO.getBudgetedAmtForYear(searchMap);
        } catch (final ValidationException valEx) {
            logger.error(valEx);
        }
        return val;
    }

    /**
     * returns the sanctioned budget for the year
     *
     * @param paramMap
     * @return
     * @throws ValidationException
     */

    public BigDecimal getTotalGrantForYear(final FinancialDetail fd, final Long financialyearid)
            throws ValidationException {
        BigDecimal val = BigDecimal.ZERO;
        final Map<String, Object> searchMap = new HashMap<String, Object>();

        searchMap.put("financialyearid", financialyearid);

        if (fd != null) {
            final List<BudgetGroup> budgetheadid = new ArrayList<BudgetGroup>();
            budgetheadid.add(fd.getBudgetGroup());
            if (fd.getFunction() != null && fd.getFunction().getId() != null)
                searchMap.put("functionid", fd.getFunction().getId());
            if (fd.getFunctionary() != null && fd.getFunctionary().getId() != null)
                searchMap.put("functionaryid", fd.getFunctionary().getId());
            if (fd.getFund() != null && fd.getFund().getId() != null)
                searchMap.put("fundid", fd.getFund().getId());
            if (fd.getBudgetGroup() != null && fd.getBudgetGroup().getId() != null)
                searchMap.put("budgetheadid", budgetheadid);
            if (fd.getScheme() != null && fd.getScheme().getId() != null)
                searchMap.put("schemeid", fd.getScheme().getId());
            if (fd.getSubScheme() != null && fd.getSubScheme().getId() != null)
                searchMap.put("subschemeid", fd.getSubScheme().getId());
            if (fd.getAbstractEstimate().getUserDepartment() != null)
                searchMap.put("deptid", fd.getAbstractEstimate().getUserDepartment().getId());
            if (fd.getAbstractEstimate().getWard().getId() != null)
                searchMap.put("boundaryid", fd.getAbstractEstimate().getWard().getId());
        }

        try {
            val = budgetDetailsDAO.getBudgetedAmtForYear(searchMap);
        } catch (final ValidationException valEx) {
            logger.error(valEx);
        }
        return val;
    }

    /**
     * returns the sanctioned budget for the year
     *
     * @param paramMap
     * @return
     * @throws ValidationException
     */

    public BigDecimal getTotalGrantForYear(final FinancialDetail fd, final Long financialyearid, final Integer deptid)
            throws ValidationException {
        BigDecimal val = BigDecimal.ZERO;
        final Map<String, Object> searchMap = new HashMap<String, Object>();

        searchMap.put("financialyearid", financialyearid);
        searchMap.put("deptid", deptid);
        if (fd != null) {
            final List<BudgetGroup> budgetheadid = new ArrayList<BudgetGroup>();
            budgetheadid.add(fd.getBudgetGroup());
            if (fd.getFunction() != null && fd.getFunction().getId() != null)
                searchMap.put("functionid", fd.getFunction().getId());
            if (fd.getFunctionary() != null && fd.getFunctionary().getId() != null)
                searchMap.put("functionaryid", fd.getFunctionary().getId());
            if (fd.getFund() != null && fd.getFund().getId() != null)
                searchMap.put("fundid", fd.getFund().getId());
            if (fd.getBudgetGroup() != null && fd.getBudgetGroup().getId() != null)
                searchMap.put("budgetheadid", budgetheadid);
            if (fd.getScheme() != null && fd.getScheme().getId() != null)
                searchMap.put("schemeid", fd.getScheme().getId());
            if (fd.getSubScheme() != null && fd.getSubScheme().getId() != null)
                searchMap.put("subschemeid", fd.getSubScheme().getId());

            if (fd.getAbstractEstimate().getWard().getId() != null)
                searchMap.put("boundaryid", fd.getAbstractEstimate().getWard().getId());
        }

        try {
            val = budgetDetailsDAO.getBudgetedAmtForYear(searchMap);
        } catch (final ValidationException valEx) {
            logger.error(valEx);
        }
        return val;
    }

    /**
     * returns the sanctioned budget for the year as on the date passed
     *
     * @param paramMap
     * @return
     * @throws ValidationException
     */

    public BigDecimal getTotalGrantForYearAsOnDate(final FinancialDetail fd, final Long financialyearid,
            final Integer deptid, final Date asOnDate) throws ValidationException {
        BigDecimal val = BigDecimal.ZERO;
        final Map<String, Object> searchMap = new HashMap<String, Object>();

        searchMap.put("financialyearid", financialyearid);
        searchMap.put("deptid", deptid);
        if (fd != null) {
            final List<BudgetGroup> budgetheadid = new ArrayList<BudgetGroup>();
            budgetheadid.add(fd.getBudgetGroup());
            if (fd.getFunction() != null && fd.getFunction().getId() != null)
                searchMap.put("functionid", fd.getFunction().getId());
            if (fd.getFunctionary() != null && fd.getFunctionary().getId() != null)
                searchMap.put("functionaryid", fd.getFunctionary().getId());
            if (fd.getFund() != null && fd.getFund().getId() != null)
                searchMap.put("fundid", fd.getFund().getId());
            if (fd.getBudgetGroup() != null && fd.getBudgetGroup().getId() != null)
                searchMap.put("budgetheadid", budgetheadid);
            if (fd.getScheme() != null && fd.getScheme().getId() != null)
                searchMap.put("schemeid", fd.getScheme().getId());
            if (fd.getSubScheme() != null && fd.getSubScheme().getId() != null)
                searchMap.put("subschemeid", fd.getSubScheme().getId());

            if (fd.getAbstractEstimate().getWard().getId() != null)
                searchMap.put("boundaryid", fd.getAbstractEstimate().getWard().getId());
        }

        try {
            val = budgetDetailsDAO.getBudgetedAmtForYearAsOnDate(searchMap, asOnDate);
        } catch (final ValidationException valEx) {
            logger.error(valEx);
        }
        return val;
    }

    /**
     * returns the planning budget percentage
     *
     * @param paramMap
     * @return
     * @throws ValidationException
     */
    public BigDecimal getPlanningBudgetPercentage(final FinancialDetail fd, final Long financialyearid,
            final Integer deptid) throws ValidationException {
        BigDecimal val = BigDecimal.ZERO;
        final Map<String, Object> searchMap = new HashMap<String, Object>();

        searchMap.put("financialyearid", financialyearid);
        searchMap.put("deptid", deptid);
        if (fd != null) {
            final List<BudgetGroup> budgetheadid = new ArrayList<BudgetGroup>();
            budgetheadid.add(fd.getBudgetGroup());
            if (fd.getFunction() != null && fd.getFunction().getId() != null)
                searchMap.put("functionid", fd.getFunction().getId());
            if (fd.getFunctionary() != null && fd.getFunctionary().getId() != null)
                searchMap.put("functionaryid", fd.getFunctionary().getId());
            if (fd.getFund() != null && fd.getFund().getId() != null)
                searchMap.put("fundid", fd.getFund().getId());
            if (fd.getBudgetGroup() != null && fd.getBudgetGroup().getId() != null)
                searchMap.put("budgetheadid", budgetheadid);
            if (fd.getScheme() != null && fd.getScheme().getId() != null)
                searchMap.put("schemeid", fd.getScheme().getId());
            if (fd.getSubScheme() != null && fd.getSubScheme().getId() != null)
                searchMap.put("subschemeid", fd.getSubScheme().getId());

            if (fd.getAbstractEstimate().getWard().getId() != null)
                searchMap.put("boundaryid", fd.getAbstractEstimate().getWard().getId());
        }

        try {
            val = budgetDetailsDAO.getPlanningPercentForYear(searchMap);
        } catch (final ValidationException valEx) {
            logger.error(valEx);
        }
        return val;
    }

    /**
     * This method will return budgetary appropriation done estimate list for budgetHead
     *
     * @param
     */
    public Map<String, List> getApprovedAppropriationDetailsForBugetHead(final AbstractEstimate viewEstimate,
            final BigDecimal totalGrantPerc) {
        if (logger.isDebugEnabled())
            logger.debug("1---inside getApprovedAppropriationDetailsForBugetHead------");
        final List<BudgetFolioDetail> approvedBudgetFolioResultList = new ArrayList<BudgetFolioDetail>();
        final Map<String, Object> queryParamMap = new HashMap<String, Object>();
        FinancialDetail fd = null;
        Long deptId = null;
        Long functionId = null;
        Integer fundId = null;
        Long budgetHeadId = null;
        Long financialYearId = null;
        Map<String, List> budgetFolioMap = null;

        // fundId,ExecutionDepartmentId,functionId,moduleId,financialYearId,budgetgroupId,fromDate,toDate
        // and Order By
        if (viewEstimate != null && viewEstimate.getFinancialDetails().get(0) != null)
            fd = viewEstimate.getFinancialDetails().get(0);

        if (fd != null) {
            if (fd.getBudgetGroup() != null && fd.getBudgetGroup().getId() != null) {
                budgetHeadId = viewEstimate.getFinancialDetails().get(0).getBudgetGroup().getId();
                queryParamMap.put("budgetgroupId", budgetHeadId);
            }
            if (fd.getAbstractEstimate().getUserDepartment() != null) {
                deptId = fd.getAbstractEstimate().getUserDepartment().getId();
                queryParamMap.put("ExecutionDepartmentId", deptId);
            }
            if (fd.getFunction() != null && fd.getFunction().getId() != null) {
                functionId = fd.getFunction().getId();
                queryParamMap.put("functionId", functionId);
            }

            if (fd.getFund() != null && fd.getFund() != null && fd.getFund().getId() != null) {
                fundId = fd.getFund().getId();
                queryParamMap.put("fundId", fundId);
            }
            if (fd.getAbstractEstimate() != null && fd.getAbstractEstimate().getLeastFinancialYearForEstimate() != null
                    && fd.getAbstractEstimate().getLeastFinancialYearForEstimate().getId() != null) {
                financialYearId = fd.getAbstractEstimate().getLeastFinancialYearForEstimate().getId();
                queryParamMap.put("financialYearId", financialYearId);
                addFinancialYearToQuery(fd, queryParamMap);
            }
        }
        final Integer moduleId = 11;
        queryParamMap.put("moduleId", moduleId);

        final List<BudgetUsage> budgetUsageList = egovCommon.getListBudgetUsage(queryParamMap);

        if (budgetUsageList != null && !budgetUsageList.isEmpty())
            budgetFolioMap = addApprovedEstimateResultList(approvedBudgetFolioResultList, budgetUsageList,
                    totalGrantPerc);
        return budgetFolioMap;
    }

    public Map<String, List> getApprovedAppropriationDetailsForBugetHead(final Map<String, Object> queryParamMap) {
        final List<BudgetFolioDetail> approvedBudgetFolioResultList = new ArrayList<BudgetFolioDetail>();
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        if (queryParamMap.get("budgetheadid") != null) {
            final List<BudgetGroup> budgetheadid = (List) queryParamMap.get("budgetheadid");
            final BudgetGroup bg = budgetheadid.get(0);
            paramMap.put("budgetgroupId", bg.getId());
        }
        if (queryParamMap.get("deptid") != null)
            paramMap.put("ExecutionDepartmentId", queryParamMap.get("deptid"));
        if (queryParamMap.get("functionid") != null)
            paramMap.put("functionId", queryParamMap.get("functionid"));
        if (queryParamMap.get("fundid") != null)
            paramMap.put("fundId", queryParamMap.get("fundid"));
        if (queryParamMap.get("financialyearid") != null)
            paramMap.put("financialYearId", queryParamMap.get("financialyearid"));
        if (queryParamMap.get(FROM_DATE) != null)
            paramMap.put(FROM_DATE, queryParamMap.get(FROM_DATE));
        if (queryParamMap.get(TO_DATE) != null)
            paramMap.put(TO_DATE, queryParamMap.get(TO_DATE));
        final Integer moduleId = 11;
        paramMap.put("moduleId", moduleId);
        final List<BudgetUsage> budgetUsageList = budgetDetailsDAO.getListBudgetUsage(paramMap);
        if (budgetUsageList != null && !budgetUsageList.isEmpty())
            return addApprovedEstimateResultList(approvedBudgetFolioResultList, budgetUsageList, new BigDecimal(
                    queryParamMap.get("totalGrantPerc").toString()));
        return new HashMap<String, List>();
    }

    public void addFinancialYearToQuery(final FinancialDetail fd, final Map<String, Object> queryParamMap) {
        if (fd.getAbstractEstimate().getLeastFinancialYearForEstimate().getStartingDate() != null)
            queryParamMap.put(FROM_DATE, fd.getAbstractEstimate().getLeastFinancialYearForEstimate().getStartingDate());
        queryParamMap.put(TO_DATE, new Date());
    }

    public Map<String, List> addApprovedEstimateResultList(final List<BudgetFolioDetail> budgetFolioResultList,
            final List<BudgetUsage> budgetUsageList, final BigDecimal totalGrantPerc) {
        int srlNo = 1;
        Double cumulativeTotal = 0.00D;
        BigDecimal balanceAvailable = BigDecimal.ZERO;
        final Map<String, List> budgetFolioMap = new HashMap<String, List>();
        for (final BudgetUsage budgetUsage : budgetUsageList) {
            final BudgetFolioDetail budgetFolioDetail = new BudgetFolioDetail();
            budgetFolioDetail.setSrlNo(srlNo++);

            final AbstractEstimate estimate = find("from AbstractEstimate ae where ae.estimateNumber=?",
                    budgetUsage.getReferenceNumber());
            if (estimate != null) {
                budgetFolioDetail.setEstimateNo(estimate.getEstimateNumber());
                budgetFolioDetail.setNameOfWork(estimate.getName());
                budgetFolioDetail.setWorkValue(estimate.getTotalAmount().getValue());
                budgetFolioDetail.setEstimateDate(sdf.format(estimate.getEstimateDate()));

            }

            budgetFolioDetail.setBudgetApprNo(budgetUsage.getAppropriationnumber());
            budgetFolioDetail.setCumulativeTotal(cumulativeTotal);
            balanceAvailable = totalGrantPerc.subtract(new BigDecimal(cumulativeTotal));
            budgetFolioDetail.setBalanceAvailable(balanceAvailable);
            budgetFolioDetail.setAppDate(sdf.format(new Date(budgetUsage.getUpdatedTime().getTime())));
            budgetFolioDetail.setAppType(getApporpriationType(budgetUsage.getId()));
            budgetFolioResultList.add(budgetFolioDetail);

            if (budgetUsage.getReleasedAmount() > 0) {
                cumulativeTotal = cumulativeTotal - budgetUsage.getReleasedAmount();
                budgetFolioDetail.setAppropriatedValue(0.0 - budgetUsage.getReleasedAmount());
            } else {
                cumulativeTotal = cumulativeTotal + budgetUsage.getConsumedAmount();
                budgetFolioDetail.setAppropriatedValue(budgetUsage.getConsumedAmount());
            }
        }
        final List calculatedValuesList = new ArrayList();
        calculatedValuesList.add(cumulativeTotal);
        calculatedValuesList.add(totalGrantPerc.subtract(new BigDecimal(cumulativeTotal)));
        budgetFolioMap.put("budgetFolioList", budgetFolioResultList);
        budgetFolioMap.put("calculatedValues", calculatedValuesList);
        return budgetFolioMap;
    }

    public Map<String, Object> createBudgetFolioHeaderJasperObject(final AbstractEstimate ae,
            final BigDecimal totalGrant, final BigDecimal totalGrantPer) {
        final HashMap<String, Object> budgetFolioMapObject = new HashMap<String, Object>();
        String departmentName = "";
        String functionCenter = "";
        String budgetHead = "";
        String fund = "";
        FinancialDetail fd = null;

        if (ae != null && ae.getExecutingDepartment() != null && ae.getExecutingDepartment().getName() != null)
            departmentName = ae.getExecutingDepartment().getName();

        if (ae != null && ae.getFinancialDetails() != null && ae.getFinancialDetails().get(0) != null)
            fd = ae.getFinancialDetails().get(0);

        if (fd != null) {
            if (fd.getFunction() != null && fd.getFunction() != null && fd.getFunction().getName() != null)
                functionCenter = ae.getFinancialDetails().get(0).getFunction().getName();
            if (fd.getBudgetGroup() != null && fd.getBudgetGroup().getName() != null)
                budgetHead = fd.getBudgetGroup().getName();
            if (fd.getFund() != null && fd.getFund().getName() != null)
                fund = fd.getFund().getName();
        }
        budgetFolioMapObject.put("departmentName", departmentName);
        budgetFolioMapObject.put("functionCenter", functionCenter);
        budgetFolioMapObject.put("budgetHead", budgetHead);
        budgetFolioMapObject.put("fund", fund);
        budgetFolioMapObject.put("totalGrant", totalGrant);
        budgetFolioMapObject.put("totalGrantPer", totalGrantPer);
        return budgetFolioMapObject;
    }

    public String getApporpriationType(final long budgetUsageId) {
        String appType = "Regular";
        if (estimateAppropriationService != null) {
            final List allReadyAppropriatedBudgetUsageList = estimateAppropriationService
                    .findAllBy(
                            "from AbstractEstimateAppropriation where abstractEstimate.id=(select max(abstractEstimate.id) from AbstractEstimateAppropriation where budgetUsage.id=?) and budgetUsage.id<?",
                            budgetUsageId, budgetUsageId);
            if (allReadyAppropriatedBudgetUsageList.size() != 0)
                appType = "Re-Appropriation";
        }
        return appType;
    }

    public BudgetGroupDAO getBudgetGroupDAO() {
        return budgetGroupDAO;
    }

    public void setBudgetGroupDAO(final BudgetGroupDAO budgetGroupDAO) {
        this.budgetGroupDAO = budgetGroupDAO;
    }

    public BudgetDetailsDAO getBudgetDetailsDAO() {
        return budgetDetailsDAO;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public Assignment getLatestAssignmentForCurrentLoginUser() {
        final Long currentLoginUserId = worksService.getCurrentLoggedInUserId();
        Assignment assignment = null;
        if (currentLoginUserId != null)
            assignment = assignmentService.getPrimaryAssignmentForEmployee(currentLoginUserId);
        return assignment;
    }

    public CFinancialYear getCurrentFinancialYear(final Date estimateDate) {
        return financialYearHibernateDAO.getFinYearByDate(estimateDate);
    }

    public CFinancialYear getPreviousFinancialYear() {
        return financialYearHibernateDAO.getFinancialYearById(Long.parseLong(financialYearHibernateDAO.getPrevYearFiscalId()));
    }

    public Date getLatestApprYearEndDate(final FinancialDetail financialDetail) {
        final AbstractEstimateAppropriation estimateAppropriation = estimateAppropriationService.findByNamedQuery(
                "getLatestBudgetUsageForEstimate", financialDetail.getAbstractEstimate().getId());
        if (estimateAppropriation != null)
            return financialYearHibernateDAO.getFinancialYearById(
                    estimateAppropriation.getBudgetUsage().getFinancialYearId().longValue()).getEndingDate();
        else
            return new Date();

    }

    public BigDecimal getBudgetAvailable(final AbstractEstimate estimate, final Date date) throws ValidationException {
        final BigDecimal budgetAvailable = BigDecimal.ZERO;
        Long finYearId = null;
        final List<Long> budgetheadid = new ArrayList<Long>();
        if (date == null)
            finYearId = financialYearHibernateDAO.getFinYearByDate(new Date()).getId();
        else
            finYearId = financialYearHibernateDAO.getFinYearByDate(date).getId();
        if (estimate.getFinancialDetails() != null && estimate.getFinancialDetails().size() > 0) {
            final FinancialDetail financialDetail = estimate.getFinancialDetails().get(0);
            budgetheadid.add(financialDetail.getBudgetGroup().getId());
            return budgetDetailsDAO.getPlanningBudgetAvailable(finYearId, Integer.parseInt(estimate.getUserDepartment()
                    .getId().toString()), financialDetail.getFunction() == null ? null : financialDetail.getFunction()
                    .getId(),
                    null, financialDetail.getScheme() == null ? null : financialDetail.getScheme().getId(),
                    financialDetail.getSubScheme() == null ? null : financialDetail.getSubScheme().getId(), Integer
                            .parseInt(estimate.getWard().getId().toString()),
                    financialDetail.getBudgetGroup() == null ? null : budgetheadid,
                    financialDetail.getFund() == null ? null : financialDetail.getFund().getId());
        } else
            return budgetAvailable;
    }

    public Boolean isPreviousYearApprRequired(final FinancialDetail financialDetail) {
        if ("yes".equalsIgnoreCase(worksService.getWorksConfigValue("PREVIOUS_YEAR_APPROPRIATION_ALLOWED"))
                && financialDetail != null && financialDetail.getApprYear() != null
                && WorksConstants.PREVIOUS_APPROPRIATION_YEAR.equalsIgnoreCase(financialDetail.getApprYear()))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    public BigDecimal getBudgetAvailable(final Integer departmentId, final Long functionId, final Integer fundId,
            final Long budgetGroupId, final Long finYearId) throws ValidationException {
        if (logger.isDebugEnabled())
            logger.debug(
                    "Start of getBudgetAvailable(Long functionId,Integer fundId,Long budgetGroupId,Long finYearId) : functionId="
                            + functionId + "fundId:" + fundId + "budgetGroupId=" + budgetGroupId + "finYearId=" + finYearId);
        final List<Long> budgetheadid = new ArrayList<Long>();
        if (functionId == null || fundId == null || budgetGroupId == null || finYearId == null || departmentId == null)
            throw new ApplicationRuntimeException(
                    "Error:Invalid Argument passed to getBudgetAvailable(Integer departmentId,Long functionId,Integer fundId,Long budgetGroupId,Long finYearId)");
        budgetheadid.add(budgetGroupId);

        return budgetDetailsDAO.getPlanningBudgetAvailable(finYearId, departmentId, functionId, null, null, null, null,
                budgetheadid, fundId);
    }

    public List<AbstractEstimate> getAbEstimateListById(final String estId) {
        final String[] estValues = estId.split("`~`");
        final Long[] estIdLong = new Long[estValues.length];
        final Set<Long> abIdentifierSet = new HashSet<Long>();
        int j = 0;
        for (final String estValue : estValues)
            if (StringUtils.isNotBlank(estValue)) {
                estIdLong[j] = Long.valueOf(estValue);
                j++;
            }
        abIdentifierSet.addAll(Arrays.asList(estIdLong));
        return findAllByNamedQuery("ABSTRACTESTIMATELIST_BY_ID", abIdentifierSet);
    }

    public Money getWorkValueIncludingTaxesForEstList(final List<AbstractEstimate> abList) {
        double amt = 0;
        if (!abList.isEmpty())
            for (final AbstractEstimate ab : abList)
                amt += ab.getWorkValueIncludingTaxes().getValue();
        return new Money(amt);
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public EgovCommon getEgovCommon() {
        return egovCommon;
    }

    public boolean checkForBudgetaryAppropriationForDepositWorks(final FinancialDetail financialDetail,
            final String appropriationNumber)
            throws ValidationException {
        boolean flag = false;
        final Date appDate = new Date();
        double depApprAmnt = 0.0;
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("DEPOSITCODE");

        // In case of deposit work whole amount will be appropriated at once.
        if (appDate.compareTo(financialDetail.getAbstractEstimate().getEstimateDate()) >= 0)
            depApprAmnt = financialDetail.getAbstractEstimate().getTotalAmount().getValue();

        final BigDecimal creditBalance = egovCommon.getDepositAmountForDepositCode(new Date(), financialDetail.getCoa()
                .getGlcode(), financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail
                .getAbstractEstimate().getDepositCode().getId().intValue());
        BigDecimal utilizedAmt = depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail,
                appDate);
        BigDecimal balance = BigDecimal.ZERO;
        if (utilizedAmt == null) {
            balance = creditBalance;
            utilizedAmt = BigDecimal.ZERO;
        } else
            balance = creditBalance.subtract(utilizedAmt);

        if (balance.doubleValue() >= depApprAmnt) {
            DepositWorksUsage depositWorksUsage = new DepositWorksUsage();
            final CFinancialYear budgetApprDate_finYear = financialYearHibernateDAO.getFinYearByDate(appDate);
            depositWorksUsage.setTotalDepositAmount(creditBalance);
            depositWorksUsage.setConsumedAmount(new BigDecimal(depApprAmnt));
            depositWorksUsage.setReleasedAmount(BigDecimal.ZERO);
            depositWorksUsage.setAppropriationNumber(appropriationNumber);
            depositWorksUsage.setAbstractEstimate(financialDetail.getAbstractEstimate());
            depositWorksUsage.setAppropriationDate(appDate);
            depositWorksUsage.setFinancialYear(budgetApprDate_finYear);
            depositWorksUsage.setDepositCode(financialDetail.getAbstractEstimate().getDepositCode());
            depositWorksUsage.setCoa(financialDetail.getCoa());
            depositWorksUsage = depositWorksUsageService.persist(depositWorksUsage);
            persistDepositCodeAppDetails(depositWorksUsage);
            flag = true;
        }
        return flag;
    }

    public boolean releaseDepositWorksAmountOnReject(final FinancialDetail financialDetail) throws ValidationException {
        boolean flag = false;
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("DEPOSITCODE");
        final AbstractEstimateAppropriation estimateAppropriation = estimateAppropriationService.findByNamedQuery(
                "getLatestDepositWorksUsageForEstimate", financialDetail.getAbstractEstimate().getId());
        final BigDecimal creditBalance = egovCommon.getDepositAmountForDepositCode(new Date(), financialDetail.getCoa()
                .getGlcode(), financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail
                .getAbstractEstimate().getDepositCode().getId().intValue());
        // Generate Budget Rejection no here
        final String budgetRejectionNumber = "BC/" + estimateAppropriation.getDepositWorksUsage().getAppropriationNumber();
        final double releaseAmount = estimateAppropriation.getDepositWorksUsage().getConsumedAmount().doubleValue();
        DepositWorksUsage depositWorksUsage = new DepositWorksUsage();
        depositWorksUsage.setTotalDepositAmount(creditBalance);
        depositWorksUsage.setConsumedAmount(BigDecimal.ZERO);
        depositWorksUsage.setReleasedAmount(new BigDecimal(releaseAmount));
        depositWorksUsage.setAppropriationNumber(budgetRejectionNumber);
        depositWorksUsage.setAbstractEstimate(financialDetail.getAbstractEstimate());
        depositWorksUsage.setAppropriationDate(new Date());
        depositWorksUsage.setFinancialYear(estimateAppropriation.getDepositWorksUsage().getFinancialYear());
        depositWorksUsage.setCoa(financialDetail.getCoa());
        depositWorksUsage.setDepositCode(financialDetail.getAbstractEstimate().getDepositCode());
        depositWorksUsage = depositWorksUsageService.persist(depositWorksUsage);
        persistReleaseDepositWorksAmountDetails(depositWorksUsage);
        flag = true;
        return flag;
    }

    public void setDepositWorksUsageService(final DepositWorksUsageService depositWorksUsageService) {
        this.depositWorksUsageService = depositWorksUsageService;
    }

    public DepositWorksUsageService getDepositWorksUsageService() {
        return depositWorksUsageService;
    }

    private void persistBudgetAppropriationDetails(final AbstractEstimate abstractEstimate,
            final BudgetUsage budgetUsage) {
        AbstractEstimateAppropriation estimateAppropriation = null;
        final Integer finYearId = budgetUsage.getFinancialYearId();
        final Date endingDate = financialYearHibernateDAO.getFinancialYearById(finYearId.longValue()).getEndingDate();
        estimateAppropriation = estimateAppropriationService.findByNamedQuery("getBudgetUsageForEstimateByFinYear",
                abstractEstimate.getId(), finYearId.intValue());

        if (estimateAppropriation != null) {
            estimateAppropriation.setBalanceAvailable(getBudgetAvailable(abstractEstimate, endingDate));
            estimateAppropriation.setBudgetUsage(budgetUsage);
        } else {
            estimateAppropriation = new AbstractEstimateAppropriation();
            estimateAppropriation.setAbstractEstimate(abstractEstimate);
            estimateAppropriation.setBalanceAvailable(getBudgetAvailable(abstractEstimate, endingDate));
            estimateAppropriation.setBudgetUsage(budgetUsage);
        }
        estimateAppropriationService.persist(estimateAppropriation);
    }

    private void persistDepositCodeAppDetails(final DepositWorksUsage depositWorksUsage) {
        AbstractEstimateAppropriation estimateAppropriation = null;
        final int finYearId = financialYearHibernateDAO.getFinYearByDate(new Date()).getId().intValue();
        final BigDecimal creditBalance = depositWorksUsage.getTotalDepositAmount();
        final AbstractEstimate abstractEstimate = depositWorksUsage.getAbstractEstimate();
        BigDecimal utilizedAmt = depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(abstractEstimate
                .getFinancialDetails().get(0), depositWorksUsage.getCreatedDate());
        BigDecimal balance = BigDecimal.ZERO;
        if (utilizedAmt == null) {
            balance = creditBalance;
            utilizedAmt = BigDecimal.ZERO;
        } else
            balance = creditBalance.subtract(utilizedAmt);

        estimateAppropriation = estimateAppropriationService.findByNamedQuery(
                "getDepositWorksUsageForEstimateByFinYear", abstractEstimate.getId(), finYearId);
        if (estimateAppropriation != null) {
            estimateAppropriation.setBalanceAvailable(balance);
            estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
        } else {
            estimateAppropriation = new AbstractEstimateAppropriation();
            estimateAppropriation.setAbstractEstimate(abstractEstimate);
            estimateAppropriation.setBalanceAvailable(balance);
            estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
        }
        estimateAppropriationService.persist(estimateAppropriation);
    }

    private void persistBudgetReleaseDetails(final AbstractEstimate abstractEstimate, final BudgetUsage budgetUsage) {
        AbstractEstimateAppropriation estimateAppropriation = null;
        estimateAppropriation = estimateAppropriationService.findByNamedQuery("getLatestBudgetUsageForEstimate",
                abstractEstimate.getId());
        final Integer finYearId = estimateAppropriation.getBudgetUsage().getFinancialYearId();
        final Date endingDate = financialYearHibernateDAO.getFinancialYearById(finYearId.longValue()).getEndingDate();
        estimateAppropriation.setBalanceAvailable(getBudgetAvailable(abstractEstimate, endingDate));
        estimateAppropriation.setBudgetUsage(budgetUsage);
        estimateAppropriationService.persist(estimateAppropriation);
    }

    private void persistReleaseDepositWorksAmountDetails(final DepositWorksUsage depositWorksUsage) {
        AbstractEstimateAppropriation estimateAppropriation = null;
        final BigDecimal creditBalance = depositWorksUsage.getTotalDepositAmount();
        final AbstractEstimate abstractEstimate = depositWorksUsage.getAbstractEstimate();
        final BigDecimal utilizedAmt = depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(abstractEstimate
                .getFinancialDetails().get(0), new Date());
        final BigDecimal balance = creditBalance.subtract(utilizedAmt);
        estimateAppropriation = estimateAppropriationService.findByNamedQuery("getLatestDepositWorksUsageForEstimate",
                abstractEstimate.getId());
        estimateAppropriation.setBalanceAvailable(balance);
        estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
        estimateAppropriationService.persist(estimateAppropriation);
    }

    /*
     * This API will return amount to be appropriated for estimate in the given financial year.
     * @param estimate,finYearId
     * @return 0 if no appropriation required otherwise appropriation amount
     */
    public Double getEstimateAppropriationAmountForFinyear(final AbstractEstimate estimate, final Integer finYearId) {
        Double percentage = 0.0;
        Double appropriationAmount = 0.0;
        if (logger.isDebugEnabled())
            logger.debug("start of getEstimateAppropriationAmountByFinyear() || estimate number="
                    + estimate.getEstimateNumber());
        if (estimate == null || finYearId == null)
            throw new ApplicationRuntimeException("Invalid argument passed to getEstimateAppropriationAmountForFinyear()");
        for (final MultiYearEstimate multiYearEstimate : estimate.getMultiYearEstimates())
            if (multiYearEstimate.getFinancialYear().getId().intValue() == finYearId) {
                percentage = multiYearEstimate.getPercentage();
                break;
            }
        if (percentage != 0.0)
            appropriationAmount = estimate.getTotalAmount().getValue() * (percentage / 100);
        if (logger.isDebugEnabled())
            logger.debug("end of getEstimateAppropriationAmountByFinyear() ||appropariation amount=" + appropriationAmount
                    + "||estimate number ||" + estimate.getEstimateNumber() + " Finyear ||" + finYearId);
        return appropriationAmount;

    }

    public void setEstimateAppropriationService(
            final PersistenceService<AbstractEstimateAppropriation, Long> estimateAppropriationService) {
        this.estimateAppropriationService = estimateAppropriationService;
    }

    public PersistenceService<AbstractEstimateAppropriation, Long> getEstimateAppropriationService() {
        return estimateAppropriationService;
    }

    public List<UOM> prepareUomListByExcludingSpecialUoms(final List<UOM> uomList) {
        final Set<String> exceptionSor = worksService.getExceptionSOR().keySet();
        final List<UOM> newList = new ArrayList<UOM>();
        for (final UOM uom : uomList)
            if (!exceptionSor.contains(uom.getUom()))
                newList.add(uom);
        return newList;
    }

    public List<BudgetUsage> getBudgetUsageListForEstNo(final String estNumber) {
        List<BudgetUsage> buList = new ArrayList<BudgetUsage>();
        buList = budgetUsageService.findAllBy("from BudgetUsage  where referenceNumber = ?) ", estNumber);
        return buList;
    }

    /**
     * This function returns List of objects containing workOder Id and WorkOrder number. This method in turn calls
     * getWorkOrderDetails() by passing estimateId as parameter which runs the query to get the list of objects.
     *
     * @param estimateId
     * @return List<Object> containing workOder Id and WorkOrder number
     */
    public List<Object> getWODetailsForEstimateId(final Long estimateId) {
        List<Object> woDetails = new ArrayList<Object>();
        woDetails = workOrderService.getWorkOrderDetails(estimateId);
        return woDetails;
    }

    /**
     * This function returns List of objects containing worksPackage Id and worksPackage number. This method in turn calls
     * getWorksPackageDetails() by passing estimateId as parameter which runs the query to get the list of objects.
     *
     * @param estimateId
     * @return List<Object> containing worksPackage Id and worksPackage number
     */
    public List<Object> getWPDetailsForEstimateId(final Long estimateId) {
        List<Object> wpDetails = new ArrayList<Object>();
        wpDetails = workspackageService.getWorksPackageDetails(estimateId);
        return wpDetails;
    }

    public String getLatestEstimateAppropriationNumber(final AbstractEstimate estimate) {
        String appropriationNumber = null;
        final AbstractEstimateAppropriation estimateAppropriation = estimateAppropriationService.findByNamedQuery(
                "getLatestBudgetUsageForEstimate", estimate.getId());
        if (estimateAppropriation != null)
            if (estimateAppropriation.getBudgetUsage() != null)
                appropriationNumber = estimateAppropriation.getBudgetUsage().getAppropriationnumber();
            else
                appropriationNumber = estimateAppropriation.getDepositWorksUsage().getAppropriationNumber();
        return appropriationNumber;
    }

    public PersistenceService<BudgetUsage, Long> getBudgetUsageService() {
        return budgetUsageService;
    }

    public void setBudgetUsageService(final PersistenceService<BudgetUsage, Long> budgetUsageService) {
        this.budgetUsageService = budgetUsageService;
    }

    public WorkOrderService getWorkOrderService() {
        return workOrderService;
    }

    public void setWorkOrderService(final WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    public WorksPackageService getWorkspackageService() {
        return workspackageService;
    }

    public void setWorkspackageService(final WorksPackageService workspackageService) {
        this.workspackageService = workspackageService;
    }

}