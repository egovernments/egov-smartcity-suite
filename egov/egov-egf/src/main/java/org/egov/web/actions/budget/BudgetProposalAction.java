/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.web.actions.budget;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetProposalBean;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.BudgetDetailHelper;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;



@ParentPackage("egov")
/*
 * @Results(value={
 * @Result(name="PDF",type=StreamResult.class,value=Constants.INPUT_STREAM,
 * params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants
 * .CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=BudgetReport.pdf"}),
 * @Result(name="XLS",type=StreamResult.class,value=Constants.INPUT_STREAM,
 * params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants
 * .CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=BudgetReport.xls"}) })
 */
@Results({
    @Result(name = "reportview", location = "budgetProposal-reportview.jsp"),
    @Result(name = "message", location = "budgetProposal-message.jsp"),
    @Result(name = Constants.DETAILLIST, location = "budgetProposal-" + Constants.DETAILLIST + ".jsp"),
    @Result(name = "failure", location = "budgetProposal-failure.jsp"),
    @Result(name = "reportview", type = "stream", location = "inputStream", params = { "contentType", "${contentType}",
            "contentDisposition", "attachment; filename=${fileName}" })
})
public class BudgetProposalAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final String ACTIONNAME = "actionName";
    private static final Logger LOGGER = Logger.getLogger(BudgetProposalAction.class);
    private BudgetProposalBean bpBean = new BudgetProposalBean();
    private BudgetDetail budgetDetail;
    private Budget topBudget;
    private VoucherService voucherService;
    protected FinancialYearHibernateDAO financialYearDAO;
    protected String currentfinYearRange = "";
    protected String nextfinYearRange = "";
    protected String previousfinYearRange = "";
    protected String twopreviousfinYearRange = "";
    private List<BudgetDetail> budgetDetailList = new ArrayList<BudgetDetail>();
    protected List<BudgetDetail> savedbudgetDetailList = new ArrayList<BudgetDetail>();
    protected BudgetDetailConfig budgetDetailConfig;
    protected BudgetDetailService budgetDetailService;
    protected BudgetService budgetService;
    private BudgetDetailHelper budgetDetailHelper;
    private Map<Long, String> previuosYearBudgetDetailMap = new HashMap<Long, String>();
    private Map<Long, String> beforePreviousYearBudgetDetailMap = new HashMap<Long, String>();
    private Map<String, BigDecimal> budgetDetailIdsAndAmount = new HashMap<String, BigDecimal>();
    private Map<String, BigDecimal> previousYearBudgetDetailIdsAndAmount = new HashMap<String, BigDecimal>();
    private Map<String, BigDecimal> twopreviousYearBudgetDetailIdsAndAmount = new HashMap<String, BigDecimal>();

    private final Map<String, BigDecimal> uniqueNoAndBEMap = new HashMap<String, BigDecimal>();
    private final Map<String, BigDecimal> uniqueNoAndApprMap = new HashMap<String, BigDecimal>();

    private final Map<String, String> majorCodeAndNameMap = new TreeMap<String, String>();
    private String wfitemstate;
    private Integer defaultDept;
    private Department department;
    private List<BudgetProposalBean> bpBeanList = new ArrayList<BudgetProposalBean>();
    private static final String HEADING = "heading";
    private static final String MAJORCODE = "majorcode";
    private static final String DETAIL = "detail";
    private static final String TOTAL = "total";
    private static final String FAILURE = "failure";
    private static final String SUCCESSFUL = "successful";
    private Date asOndate;
    private Date headerAsOnDate;
    private @Autowired AppConfigValueService appConfigValuesService;
    private InputStream inputStream;
    private ReportHelper reportHelper;
    private Long docNo;

    protected WorkflowService<Budget> budgetWorkflowService;
    protected EisCommonService eisCommonService;
    private boolean consolidatedScreen = false;
    private boolean allfunctionsArrived;
    private Integer approverUserId;
    private Integer userId;
    private String comment;
    private String actionName = "";

    private CFinancialYear financialYear;
    private CFinancialYear prevFinancialYear;
    private CFinancialYear nextFinancialYear;
    private CFinancialYear beforeLastFinancialYear;
    private boolean hod;
    private boolean updateApprovedRE;
    private boolean asstFMU = false;
    private boolean functionHeading = true;
    private boolean deptHeading = true;
    private String functionsNotYetReceiced;
    private Map<Long, BudgetGroup> budgetGroupMap;
    private Map<Integer, Fund> fundMap;
    private Map<Long, CFunction> functionMap;
    private Map<Integer, Department> deptMap;
    List<AppConfigValues> excludelist = new ArrayList<AppConfigValues>();
    protected EisUtilService eisService;

    // report
    private String contentType;
    private String fileName;
    private ReportService reportService;
    private String amountField;
    private BigDecimal amount;
    private Long detailId;
    private String factor;
    private Long validId;
    private final BigDecimal bigThousand = new BigDecimal(1000);

    @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public String execute() throws Exception {

        super.execute();
        return update();
    }

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return budgetDetail;
    }

    @Override
    public void prepare() {
        super.prepare();
    }

    @Action(value = "/budget/budgetProposal-modifyDetailList")
    public String modifyDetailList() {
        loadToMasterDataMap();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("starting modifyDetailList...");
        if (parameters.get("budgetDetail.id")[0] != null) {
            budgetDetail = (BudgetDetail) persistenceService.find("from BudgetDetail where id=?",
                    Long.valueOf(parameters.get("budgetDetail.id")[0]));
            setTopBudget(budgetDetail.getBudget());
        }
        /*
         * computePreviousYearRange(); computeTwopreviousYearRange();
         */
        financialYear = topBudget.getFinancialYear();
        currentfinYearRange = financialYear.getFinYearRange();

        nextFinancialYear = getFinancialYearDAO().getNextFinancialYearByDate(financialYear.getStartingDate());
        nextfinYearRange = nextFinancialYear.getFinYearRange();

        prevFinancialYear = getFinancialYearDAO().getPreviousFinancialYearByDate(financialYear.getStartingDate());
        previousfinYearRange = prevFinancialYear.getFinYearRange();

        beforeLastFinancialYear = getFinancialYearDAO().getTwoPreviousYearByDate(financialYear.getStartingDate());
        twopreviousfinYearRange = beforeLastFinancialYear.getFinYearRange();

        budgetDetailApprove();
        loadApproverUser(savedbudgetDetailList);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("completed modifyDetailList");
        /* return Constants.DETAILLIST; */
        setDocNo(budgetDetail.getDocumentNumber());
        return Constants.DETAILLIST;
    }

    @Action(value = "/budget/budgetProposal-modifyBudgetDetailList")
    public String modifyBudgetDetailList() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting modifyBudgetDetailList..............");

        populateBudgetDetailReport();
        loadApproverUser(budgetDetailList);
        return Constants.DETAILLIST;
    }

    private void populateBudgetDetailReport() {
        loadToMasterDataMap();
        if (budgetDetail.getBudget().getId() != null)
        {
            topBudget = budgetService.find("from Budget where id=?", budgetDetail.getBudget().getId());
            budgetDetail = (BudgetDetail) persistenceService.find("from BudgetDetail where budget.id=?",
                    Long.valueOf(parameters.get("budgetDetail.budget.id")[0]));
        }
        consolidatedScreen = budgetDetailService.toBeConsolidated();
        hod = isHOD();
        if (hod)
            allfunctionsArrived = validateForAllFunctionsMappedForDept(topBudget, getPosition());

        financialYear = topBudget.getFinancialYear();
        currentfinYearRange = financialYear.getFinYearRange();

        nextFinancialYear = getFinancialYearDAO().getNextFinancialYearByDate(financialYear.getStartingDate());
        nextfinYearRange = nextFinancialYear.getFinYearRange();

        prevFinancialYear = getFinancialYearDAO().getPreviousFinancialYearByDate(financialYear.getStartingDate());
        previousfinYearRange = prevFinancialYear.getFinYearRange();

        beforeLastFinancialYear = getFinancialYearDAO().getTwoPreviousYearByDate(financialYear.getStartingDate());
        twopreviousfinYearRange = beforeLastFinancialYear.getFinYearRange();

        budgetApprove();
        if (isConsolidatedScreen())
            divideByThousand();
        loadApproverUser(budgetDetailList);

    }

    private void loadToMasterDataMap() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting loadToMasterDataMap...... ");
        final List<BudgetGroup> bgList = masterDataCache.get("egf-budgetGroup");
        budgetGroupMap = new HashMap<Long, BudgetGroup>();
        for (final BudgetGroup bg : bgList)
            budgetGroupMap.put(bg.getId(), bg);
        final List<CFunction> fnList = masterDataCache.get("egi-function");
        functionMap = new HashMap<Long, CFunction>();
        for (final CFunction fn : fnList)
            functionMap.put(fn.getId(), fn);

        final List<Fund> fundList = masterDataCache.get("egi-fund");
        fundMap = new HashMap<Integer, Fund>();
        for (final Fund f : fundList)
            fundMap.put(f.getId(), f);
        final List<Department> deptList = masterDataCache.get("egi-department");
        deptMap = new HashMap<Integer, Department>();
        for (final Department d : deptList)
            deptMap.put(d.getId().intValue(), d);
        excludelist = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF, "exclude_status_forbudget_actual");
        if (excludelist.isEmpty())
            throw new ValidationException("", "exclude_status_forbudget_actual is not defined in AppConfig");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished loadToMasterDataMap...... ");
    }

    @SkipValidation
    public String ajaxUpdateBudgetDetail() {

        if (validateOwner())
        {
            if (factor.equalsIgnoreCase("thousand"))
                amount = amount.multiply(bigThousand);

            final String query = "update egf_budgetdetail set  " + amountField
                    + "=:amount,Modifiedby=:modifiedby,modifieddate=:modifiedate  where id=:detailId";
            if (LOGGER.isInfoEnabled())
                LOGGER.info(query);

            final Query updateQuery = HibernateUtil.getCurrentSession().createSQLQuery(query)
                    .setBigDecimal("amount", amount)
                    .setLong("detailId", detailId)
                    .setDate("modifiedate", new java.sql.Date(new Date().getTime()))
                    .setInteger("modifiedby", EgovThreadLocals.getUserId().intValue());
            final int executeUpdate = updateQuery.executeUpdate();
            if (executeUpdate == 1)
                return SUCCESSFUL;
            else
                return FAILURE;

        } else
            return FAILURE;

    }

    @Action(value = "/budget/budgetProposal-ajaxDeleteBudgetDetail")
    public String ajaxDeleteBudgetDetail() {
        try {
            if (bpBean.getId() != null && bpBean.getNextYrId() != null) {
                HibernateUtil
                .getCurrentSession()
                .createSQLQuery(
                        "delete from egf_budgetdetail where id in (" + bpBean.getId() + "," + bpBean.getNextYrId() + ")")
                        .executeUpdate();
                HibernateUtil.getCurrentSession().flush();
            }
        } catch (final HibernateException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("error while deleting");
            return FAILURE;
        }
        return SUCCESSFUL;
    }

    public boolean isHod() {
        return hod;
    }

    public void setHod(final boolean hod) {
        this.hod = hod;
    }

    public void budgetDetailApprove() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting budgetDetailApprove()..............");

        // if u want only selected function centre filter here by owner fecthing logic to be changed only
        final String query = " from BudgetDetail bd where bd.budget=? and (state.value='END' or state.owner=?) and bd.function="
                + budgetDetail.getFunction().getId() + "  order by bd.function.name,bd.budgetGroup.name";
        savedbudgetDetailList = budgetDetailService.findAllBy(query, topBudget, getPosition());

        // check what actuals needs to be shown for next year be AND possible remove if
        /* CFinancialYear previousYearFor = budgetDetailHelper.getPreviousYearFor(topBudget.getFinancialYear()); */
        if (!savedbudgetDetailList.isEmpty()) {
            populateMajorCodewiseData();
            populateNextYrBEinBudgetDetailList();
            populateMajorCodewiseDetailData();
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("finished loading detail List--------------------------------------------------------------");
    }

    public void budgetApprove() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting budgetApprove..............");
        final List<CFunction> functionList = persistenceService.findAllBy(
                "select distinct f from BudgetDetail bd inner join bd.function as f  where budget=? order by f.name ",
                topBudget);
        populateMajorCodewiseDataAcrossFunction();
        int i = 0;

        for (final CFunction function : functionList) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Starting budgetDetailApprove...for functin ....centre......." + function.getName() + "     count "
                        + ++i);
            budgetDetail = (BudgetDetail) persistenceService.find(
                    "from BudgetDetail where budget=? and function=? order by budgetGroup.name", topBudget, function);
            budgetDetailApprove();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Finished budgetDetailApprove...for functin ....centre......." + function.getName());
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished budgetApprove");
    }

    public void divideByThousand() {
        final BigDecimal bigThousand = new BigDecimal(1000);
        final BigDecimal bigZero = new BigDecimal(0).setScale(2);

        for (final BudgetProposalBean bpBean : bpBeanList)
            if (!bpBean.getRowType().equals(HEADING)) {
                if (bpBean.getPreviousYearActuals() != null && !bpBean.getPreviousYearActuals().equalsIgnoreCase("0.00"))
                    bpBean.setPreviousYearActuals(new BigDecimal(bpBean.getPreviousYearActuals()).divide(bigThousand)
                            .setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                else
                    bpBean.setPreviousYearActuals("0");
                if (bpBean.getTwoPreviousYearActuals() != null && !bpBean.getTwoPreviousYearActuals().equalsIgnoreCase("0.00"))
                    bpBean.setTwoPreviousYearActuals(new BigDecimal(bpBean.getTwoPreviousYearActuals()).divide(bigThousand)
                            .setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                else
                    bpBean.setTwoPreviousYearActuals("0");
                if (bpBean.getCurrentYearActuals() != null && !bpBean.getCurrentYearActuals().equalsIgnoreCase("0.00"))
                    bpBean.setCurrentYearActuals(new BigDecimal(bpBean.getCurrentYearActuals()).divide(bigThousand)
                            .setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                else
                    bpBean.setCurrentYearActuals("0");

                if (bpBean.getCurrentYearBE() != null && !bpBean.getCurrentYearBE().equalsIgnoreCase("0.00"))
                    bpBean.setCurrentYearBE(new BigDecimal(bpBean.getCurrentYearBE()).divide(bigThousand)
                            .setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                else
                    bpBean.setCurrentYearBE("0");
                if (bpBean.getReappropriation() != null && !bpBean.getReappropriation().equalsIgnoreCase("0.00"))
                    bpBean.setReappropriation(new BigDecimal(bpBean.getReappropriation()).divide(bigThousand)
                            .setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                else
                    bpBean.setReappropriation("0");
                if (bpBean.getTotal() != null && !bpBean.getTotal().equalsIgnoreCase("0.00"))
                    bpBean.setTotal(new BigDecimal(bpBean.getTotal()).divide(bigThousand).setScale(0, BigDecimal.ROUND_HALF_UP)
                            .toString());
                else
                    bpBean.setTotal("0");
                if (bpBean.getAnticipatory() != null && !bpBean.getAnticipatory().equalsIgnoreCase("0.00"))
                    bpBean.setAnticipatory(new BigDecimal(bpBean.getAnticipatory()).divide(bigThousand)
                            .setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                else
                    bpBean.setAnticipatory("0");
                if (bpBean.getProposedRE() != null && !bpBean.getProposedRE().equals(bigZero))
                    bpBean.setProposedRE(bpBean.getProposedRE().divide(bigThousand).setScale(0, BigDecimal.ROUND_HALF_UP));
                else
                    bpBean.setProposedRE(bigZero.setScale(0));
                if (bpBean.getProposedBE() != null && !bpBean.getProposedBE().equals(bigZero))
                    bpBean.setProposedBE(bpBean.getProposedBE().divide(bigThousand).setScale(0, BigDecimal.ROUND_HALF_UP));
                else
                    bpBean.setProposedBE(bigZero.setScale(0));

                if (updateApprovedRE) {

                    bpBean.setApprovedRE(bpBean.getProposedRE());
                    if (bpBean.getBudgetGroup().startsWith("2101001") || bpBean.getBudgetGroup().startsWith("2101002"))
                        bpBean.setApprovedBE(bpBean.getApprovedRE().multiply(new BigDecimal(1.25))
                                .setScale(0, BigDecimal.ROUND_HALF_UP));
                    else
                        bpBean.setApprovedBE(bpBean.getProposedRE().setScale(0, BigDecimal.ROUND_HALF_UP));

                } else {
                    if (bpBean.getApprovedRE() != null && !bpBean.getApprovedRE().equals(bigZero))
                        bpBean.setApprovedRE(bpBean.getApprovedRE().divide(bigThousand).setScale(0, BigDecimal.ROUND_HALF_UP));
                    else
                        bpBean.setApprovedRE(bigZero.setScale(0));
                    if (bpBean.getApprovedBE() != null && !bpBean.getApprovedBE().equals(bigZero))
                        bpBean.setApprovedBE(bpBean.getApprovedBE().divide(bigThousand).setScale(0, BigDecimal.ROUND_HALF_UP));
                    else
                        bpBean.setApprovedBE(bigZero.setScale(0));
                }
            }
    }

    void populateMajorCodewiseData() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting populateMajorCodewiseData()...........");
        final Map<String, BigDecimal> majorCodeAndCurrentActuals = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndPreviousActuals = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndBeforePreviousActuals = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndBEMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndAppropriationMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndAnticipatoryMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndOriginalMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndBENextYrMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndApprovedMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndBENextYrApprovedMap = new HashMap<String, BigDecimal>();

        final Position pos = getPosition();

        majorCodeAndNameMap.clear();

        final List<Object[]> resultMajorCode = budgetDetailService.fetchMajorCodeAndName(topBudget, budgetDetail,
                budgetDetail.getFunction(), pos);
        addToMap(resultMajorCode, majorCodeAndNameMap);

        final List<Object[]> resultCurrentActuals = budgetDetailService.fetchMajorCodeAndActuals(financialYear, topBudget,
                asOndate, budgetDetail.getFunction(), budgetDetail.getExecutingDepartment(), pos);
        addToMapStringBigDecimal(resultCurrentActuals, majorCodeAndCurrentActuals);

        final List<Object[]> resultPreviousActuals = budgetDetailService.fetchMajorCodeAndActuals(prevFinancialYear, topBudget,
                null,
                budgetDetail.getFunction(), budgetDetail.getExecutingDepartment(), pos);
        addToMapStringBigDecimal(resultPreviousActuals, majorCodeAndPreviousActuals);

        final List<Object[]> resultBeforePreviousActuals = budgetDetailService.fetchMajorCodeAndActuals(beforeLastFinancialYear,
                topBudget, null, budgetDetail.getFunction(), budgetDetail.getExecutingDepartment(), pos);
        addToMapStringBigDecimal(resultBeforePreviousActuals, majorCodeAndBeforePreviousActuals);

        final List<Object[]> resultMajorCodeBE = budgetDetailService.fetchMajorCodeAndBEAmount(topBudget, budgetDetail,
                budgetDetail.getFunction(), pos);
        addToMapStringBigDecimal(resultMajorCodeBE, majorCodeAndBEMap);

        final List<Object[]> resultMajorCodeAppropriation = budgetDetailService.fetchMajorCodeAndAppropriation(topBudget,
                budgetDetail,
                budgetDetail.getFunction(), pos, asOndate);
        addToMapStringBigDecimal(resultMajorCodeAppropriation, majorCodeAndAppropriationMap);

        final List<Object[]> resultMajorCodeAncipatory = budgetDetailService.fetchMajorCodeAndAnticipatory(topBudget,
                budgetDetail,
                budgetDetail.getFunction(), pos);
        // addToMapStringBigDecimal(resultMajorCodeAncipatory,majorCodeAndAnticipatoryMap,majorCodeAndOriginalMap,majorCodeAndApprovedMap);
        for (final Object[] row : resultMajorCodeAncipatory) {
            majorCodeAndAnticipatoryMap.put(row[0].toString(), ((BigDecimal) row[1]).setScale(2));
            majorCodeAndOriginalMap.put(row[0].toString(), ((BigDecimal) row[2]).setScale(2));
            majorCodeAndApprovedMap.put(row[0].toString(), ((BigDecimal) row[3]).setScale(2));
        }

        // List<Object[]> resultMajorCodeOriginal = budgetDetailService.fetchMajorCodeAndOriginalAmount(topBudget, budgetDetail,
        // budgetDetail.getFunction(), pos);
        // addToMapStringBigDecimal(resultMajorCodeOriginal,majorCodeAndOriginalMap);

        final List<Object[]> resultMajorCodeBENextYr = budgetDetailService.fetchMajorCodeAndBENextYr(topBudget, budgetDetail,
                budgetDetail.getFunction(), pos);
        // addToMapStringBigDecimal(resultMajorCodeBENextYr,majorCodeAndBENextYrMap,majorCodeAndBENextYrApprovedMap);

        for (final Object[] row : resultMajorCodeBENextYr) {
            majorCodeAndBENextYrMap.put(row[0].toString(), ((BigDecimal) row[1]).setScale(2));
            majorCodeAndBENextYrApprovedMap.put(row[0].toString(), ((BigDecimal) row[2]).setScale(2));

        }

        // List<Object[]> resultMajorCodeApproved = budgetDetailService.fetchMajorCodeAndApprovedAmount(topBudget, budgetDetail,
        // budgetDetail.getFunction(), pos);
        // addToMapStringBigDecimal(resultMajorCodeApproved,majorCodeAndApprovedMap);

        // List<Object[]> resultMajorCodeBENextYrApproved = budgetDetailService.fetchMajorCodeAndBENextYrApproved(topBudget,
        // budgetDetail, budgetDetail.getFunction(), pos);
        // addToMapStringBigDecimal(resultMajorCodeBENextYrApproved,majorCodeAndBENextYrApprovedMap);

        if (deptHeading) {
            bpBeanList.add(new BudgetProposalBean(budgetDetail.getExecutingDepartment().getName(), HEADING));
            deptHeading = false;
        }
        if (functionHeading) {
            bpBeanList.add(new BudgetProposalBean("FUNCTIONWISE BUDGET SUMMARY", HEADING));
            functionHeading = false;
        }

        bpBeanList.add(new BudgetProposalBean("FUNCTION CENTRE-" + budgetDetail.getFunction().getName(), HEADING));

        final BudgetProposalBean bpbeanTotal = new BudgetProposalBean();

        for (final Map.Entry<String, String> entry : majorCodeAndNameMap.entrySet()) {
            final String formValue = getAppConfigValueByKey("functionWiseBudgetReport-" + entry.getKey());
            final BudgetProposalBean bpbean = new BudgetProposalBean();
            bpbean.setReference(formValue);
            bpbean.setRowType(MAJORCODE);
            bpbean.setBudgetGroup(entry.getValue());
            bpbean.setCurrentYearActuals(majorCodeAndCurrentActuals.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    .toString() : majorCodeAndCurrentActuals.get(entry.getKey()).toString());
            bpbean.setPreviousYearActuals(majorCodeAndPreviousActuals.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    .toString() : majorCodeAndPreviousActuals.get(entry.getKey()).toString());
            bpbean.setTwoPreviousYearActuals(majorCodeAndBeforePreviousActuals.get(entry.getKey()) == null ? BigDecimal.ZERO
                    .setScale(2).toString() : majorCodeAndBeforePreviousActuals.get(entry.getKey()).toString());
            bpbean.setCurrentYearBE(majorCodeAndBEMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2).toString()
                    : majorCodeAndBEMap.get(entry.getKey()).toString());
            bpbean.setReappropriation(majorCodeAndAppropriationMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    .toString() : majorCodeAndAppropriationMap.get(entry.getKey()).toString());
            bpbean.setTotal(new BigDecimal(bpbean.getCurrentYearBE()).add(new BigDecimal(bpbean.getReappropriation())).toString());
            bpbean.setAnticipatory(majorCodeAndAnticipatoryMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    .toString() : majorCodeAndAnticipatoryMap.get(entry.getKey()).toString());
            bpbean.setProposedRE(majorCodeAndOriginalMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    : majorCodeAndOriginalMap.get(entry.getKey()));
            bpbean.setProposedBE(majorCodeAndBENextYrMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    : majorCodeAndBENextYrMap.get(entry.getKey()));
            bpbean.setApprovedRE(majorCodeAndApprovedMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    : majorCodeAndApprovedMap.get(entry.getKey()));
            bpbean.setApprovedBE(majorCodeAndBENextYrApprovedMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    : majorCodeAndBENextYrApprovedMap.get(entry.getKey()));
            bpBeanList.add(bpbean);

            computeTotal(bpbeanTotal, bpbean);
        }
        bpBeanList.add(bpbeanTotal);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished populateMajorCodewiseData()");
    }

    void populateMajorCodewiseDataAcrossFunction() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting populateMajorCodewiseDataAcrossFunction..............");
        final Map<String, BigDecimal> majorCodeAndCurrentActuals = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndPreviousActuals = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndBeforePreviousActuals = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndBEMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndAppropriationMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndAnticipatoryMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndOriginalMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndBENextYrMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndApprovedMap = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> majorCodeAndBENextYrApprovedMap = new HashMap<String, BigDecimal>();

        final Position pos = getPosition();

        final List<Object[]> resultMajorCode = budgetDetailService.fetchMajorCodeAndName(topBudget, budgetDetail, null, pos);
        addToMap(resultMajorCode, majorCodeAndNameMap);

        final List<Object[]> resultCurrentActuals = budgetDetailService.fetchMajorCodeAndActuals(financialYear, topBudget,
                asOndate, null, budgetDetail.getExecutingDepartment(), pos);
        addToMapStringBigDecimal(resultCurrentActuals, majorCodeAndCurrentActuals);

        final List<Object[]> resultPreviousActuals = budgetDetailService.fetchMajorCodeAndActuals(prevFinancialYear, topBudget,
                null,
                null, budgetDetail.getExecutingDepartment(), pos);
        addToMapStringBigDecimal(resultPreviousActuals, majorCodeAndPreviousActuals);

        final List<Object[]> resultBeforePreviousActuals = budgetDetailService.fetchMajorCodeAndActuals(beforeLastFinancialYear,
                topBudget, null, null, budgetDetail.getExecutingDepartment(), pos);
        addToMapStringBigDecimal(resultBeforePreviousActuals, majorCodeAndBeforePreviousActuals);

        final List<Object[]> resultMajorCodeBE = budgetDetailService
                .fetchMajorCodeAndBEAmount(topBudget, budgetDetail, null, pos);
        addToMapStringBigDecimal(resultMajorCodeBE, majorCodeAndBEMap);

        final List<Object[]> resultMajorCodeAppropriation = budgetDetailService.fetchMajorCodeAndAppropriation(topBudget,
                budgetDetail,
                null, pos, asOndate);
        addToMapStringBigDecimal(resultMajorCodeAppropriation, majorCodeAndAppropriationMap);

        budgetDetailService.fetchMajorCodeAndAnticipatory(topBudget, budgetDetail,
                null, pos);
        addToMapStringBigDecimal(resultMajorCodeAppropriation, majorCodeAndAnticipatoryMap);

        final List<Object[]> resultMajorCodeOriginal = budgetDetailService.fetchMajorCodeAndOriginalAmount(topBudget,
                budgetDetail,
                null, pos);
        addToMapStringBigDecimal(resultMajorCodeOriginal, majorCodeAndOriginalMap);

        final List<Object[]> resultMajorCodeBENextYr = budgetDetailService
                .fetchMajorCodeAndBENextYr(topBudget, budgetDetail, null, pos);
        addToMapStringBigDecimal(resultMajorCodeBENextYr, majorCodeAndBENextYrMap);

        final List<Object[]> resultMajorCodeApproved = budgetDetailService.fetchMajorCodeAndApprovedAmount(topBudget,
                budgetDetail,
                null, pos);
        addToMapStringBigDecimal(resultMajorCodeApproved, majorCodeAndApprovedMap);

        final List<Object[]> resultMajorCodeBENextYrApproved = budgetDetailService.fetchMajorCodeAndBENextYrApproved(topBudget,
                budgetDetail, null, pos);
        addToMapStringBigDecimal(resultMajorCodeBENextYrApproved, majorCodeAndBENextYrApprovedMap);

        bpBeanList.add(new BudgetProposalBean(budgetDetail.getExecutingDepartment().getName(), HEADING));
        deptHeading = false;
        bpBeanList.add(new BudgetProposalBean("DEPARTMENTWISE BUDGET SUMMARY", HEADING));
        // bpBeanList.add(new BudgetProposalBean("FUNCTIONWISE EXPENSE BUDGET SUMMARY", this.HEADING));

        // bpBeanList.add(new BudgetProposalBean("FUNCTION CENTRE-"+budgetDetail.getFunction().getName(), this.HEADING));

        final BudgetProposalBean bpbeanTotal = new BudgetProposalBean();

        for (final Map.Entry<String, String> entry : majorCodeAndNameMap.entrySet()) {
            final String formValue = getAppConfigValueByKey("functionWiseBudgetReport-" + entry.getKey());
            final BudgetProposalBean bpbean = new BudgetProposalBean();
            bpbean.setReference(formValue);
            bpbean.setRowType(MAJORCODE);
            bpbean.setBudgetGroup(entry.getValue());
            bpbean.setCurrentYearActuals(majorCodeAndCurrentActuals.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    .toString() : majorCodeAndCurrentActuals.get(entry.getKey()).toString());
            bpbean.setPreviousYearActuals(majorCodeAndPreviousActuals.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    .toString() : majorCodeAndPreviousActuals.get(entry.getKey()).toString());
            bpbean.setTwoPreviousYearActuals(majorCodeAndBeforePreviousActuals.get(entry.getKey()) == null ? BigDecimal.ZERO
                    .setScale(2).toString() : majorCodeAndBeforePreviousActuals.get(entry.getKey()).toString());
            bpbean.setCurrentYearBE(majorCodeAndBEMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2).toString()
                    : majorCodeAndBEMap.get(entry.getKey()).toString());
            bpbean.setReappropriation(majorCodeAndAppropriationMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    .toString() : majorCodeAndAppropriationMap.get(entry.getKey()).toString());
            bpbean.setTotal(new BigDecimal(bpbean.getCurrentYearBE()).add(new BigDecimal(bpbean.getReappropriation())).toString());
            bpbean.setAnticipatory(majorCodeAndAnticipatoryMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    .toString() : majorCodeAndAnticipatoryMap.get(entry.getKey()).toString());
            bpbean.setProposedRE(majorCodeAndOriginalMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    : majorCodeAndOriginalMap.get(entry.getKey()));
            bpbean.setProposedBE(majorCodeAndBENextYrMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    : majorCodeAndBENextYrMap.get(entry.getKey()));
            bpbean.setApprovedRE(majorCodeAndApprovedMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    : majorCodeAndApprovedMap.get(entry.getKey()));
            bpbean.setApprovedBE(majorCodeAndBENextYrApprovedMap.get(entry.getKey()) == null ? BigDecimal.ZERO.setScale(2)
                    : majorCodeAndBENextYrApprovedMap.get(entry.getKey()));
            bpBeanList.add(bpbean);

            computeTotal(bpbeanTotal, bpbean);
        }
        bpBeanList.add(bpbeanTotal);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished populateMajorCodewiseDataAcrossFunction");
    }

    void populateMajorCodewiseDetailData() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting populateMajorCodewiseDetailData()................");
        final List<String> mandatoryFields = new ArrayList<String>();
        mandatoryFields.add("fund");
        mandatoryFields.add("function");
        mandatoryFields.add("executingDepartment");

        final Position pos = getPosition();

        final CFinancialYear financialYear = topBudget.getFinancialYear();
        final CFinancialYear lastFinancialYearByDate = getFinancialYearDAO().getPreviousFinancialYearByDate(
                financialYear.getStartingDate());
        final CFinancialYear beforeLastFinancialYearByDate = null;// getFinancialYearDAO().getTwoPreviousYearByDate(financialYear.getStartingDate());

        final List<Object[]> resultCurrentActuals = budgetDetailService.fetchActualsForFinYear(financialYear, mandatoryFields,
                topBudget, null, asOndate, budgetDetail.getExecutingDepartment().getId().intValue(), budgetDetail
                .getFunction().getId(), excludelist);
        addToMapStringBigDecimal(resultCurrentActuals, budgetDetailIdsAndAmount);

        final List<Object[]> resultPreviousActuals = budgetDetailService.fetchActualsForFinYear(lastFinancialYearByDate,
                mandatoryFields, topBudget, null, null, budgetDetail.getExecutingDepartment().getId().intValue(), budgetDetail
                .getFunction().getId(), excludelist);
        addToMapStringBigDecimal(resultPreviousActuals, previousYearBudgetDetailIdsAndAmount);

        final List<Object[]> resultTwoPreviousActuals = budgetDetailService.fetchActualsForFinYear(beforeLastFinancialYearByDate,
                mandatoryFields, topBudget, null, null, budgetDetail.getExecutingDepartment().getId().intValue(), budgetDetail
                .getFunction().getId(), excludelist);
        addToMapStringBigDecimal(resultTwoPreviousActuals, twopreviousYearBudgetDetailIdsAndAmount);

        final List<Object[]> resultUniqueNoBE = budgetDetailService.fetchUniqueNoAndBEAmount(topBudget, budgetDetail,
                budgetDetail.getFunction(), pos);
        addToMapStringBigDecimal(resultUniqueNoBE, uniqueNoAndBEMap);

        final List<Object[]> resultUniqueNoAppr = budgetDetailService.fetchUniqueNoAndApprAmount(topBudget, budgetDetail,
                budgetDetail.getFunction(), pos);
        addToMapStringBigDecimal(resultUniqueNoAppr, uniqueNoAndApprMap);

        for (final Map.Entry<String, String> entry : majorCodeAndNameMap.entrySet()) {
            final String formValue = getAppConfigValueByKey("functionWiseBudgetReport-" + entry.getKey());
            boolean detailExist = false;
            boolean headerAdded = false;

            final BudgetProposalBean bpbeanTotal = new BudgetProposalBean();
            for (final BudgetDetail bd : savedbudgetDetailList)
                if (entry.getKey().equals(bd.getBudgetGroup().getMinCode().getMajorCode())) {
                    detailExist = true;
                    if (headerAdded == false) {
                        bpBeanList.add(new BudgetProposalBean(entry.getValue(), HEADING, formValue));
                        headerAdded = true;
                    }
                    final BudgetProposalBean bpbean = new BudgetProposalBean();

                    populateBudgetProposalBean(bpbean, bd);
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(bd.getUniqueNo() + "---" + budgetDetailIdsAndAmount.get(bd.getUniqueNo()));

                    bpBeanList.add(bpbean);

                    computeTotal(bpbeanTotal, bpbean);
                }
            if (detailExist)
                bpBeanList.add(bpbeanTotal);
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished populateMajorCodewiseDetailData()");
    }

    private void populateBudgetProposalBean(final BudgetProposalBean bpbean, final BudgetDetail bd) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting populateBudgetProposalBean..... ");
        bpbean.setRowType(DETAIL);
        bpbean.setId(bd.getId());
        bpbean.setNextYrId(bd.getNextYrId());
        bpbean.setBudget(topBudget.getName());
        bpbean.setFund(fundMap.get(bd.getFund().getId()).getName());
        bpbean.setFunction(functionMap.get(bd.getFunction().getId()).getName());
        bpbean.setBudgetGroup(budgetGroupMap.get(bd.getBudgetGroup().getId()).getName());
        bpbean.setExecutingDepartment(deptMap.get(bd.getExecutingDepartment().getId()).getCode());
        bpbean.setPreviousYearActuals(previousYearBudgetDetailIdsAndAmount.get(bd.getUniqueNo()) == null ? BigDecimal.ZERO
                .setScale(2).toString() : previousYearBudgetDetailIdsAndAmount.get(bd.getUniqueNo()).toString());
        bpbean.setTwoPreviousYearActuals(twopreviousYearBudgetDetailIdsAndAmount.get(bd.getUniqueNo()) == null ? BigDecimal.ZERO
                .setScale(2).toString() : twopreviousYearBudgetDetailIdsAndAmount.get(bd.getUniqueNo()).toString());
        bpbean.setCurrentYearActuals(budgetDetailIdsAndAmount.get(bd.getUniqueNo()) == null ? BigDecimal.ZERO.setScale(2)
                .toString() : budgetDetailIdsAndAmount.get(bd.getUniqueNo()).toString());
        final BigDecimal lastBEAmount = uniqueNoAndBEMap.get(bd.getUniqueNo()) == null ? BigDecimal.ZERO.setScale(2)
                : uniqueNoAndBEMap
                        .get(bd.getUniqueNo());
        final BigDecimal approvedReAppropriationsTotal = uniqueNoAndApprMap.get(bd.getUniqueNo()) == null ? BigDecimal.ZERO
                .setScale(2)
                : uniqueNoAndApprMap.get(bd.getUniqueNo());
        bpbean.setCurrentYearBE(lastBEAmount.toString());
        bpbean.setReappropriation(approvedReAppropriationsTotal.toString());
        bpbean.setTotal(lastBEAmount.add(approvedReAppropriationsTotal).toString());
        bpbean.setAnticipatory(bd.getAnticipatoryAmount().setScale(2).toString());
        bpbean.setProposedRE(bd.getOriginalAmount().setScale(2));
        bpbean.setProposedBE(bd.getNextYroriginalAmount().setScale(2));
        bpbean.setApprovedRE(bd.getApprovedAmount().setScale(2));
        bpbean.setApprovedBE(bd.getNextYrapprovedAmount().setScale(2));
        if (LOGGER.isInfoEnabled())
            LOGGER.info("before bd.getstate().getExtraInfo1()");
        // bpbean.setRemarks(bd.getstate().getExtraInfo1());
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished populateBudgetProposalBean..... ");
    }

    private Map<String, String> addToMap(final List<Object[]> tempList, final Map<String, String> resultMap) {
        for (final Object[] row : tempList)
            resultMap.put(row[0].toString(), row[1].toString());
        return resultMap;
    }

    private Map<String, BigDecimal> addToMapStringBigDecimal(final List<Object[]> tempList,
            final Map<String, BigDecimal> resultMap) {
        for (final Object[] row : tempList)
            resultMap.put(row[0].toString(), ((BigDecimal) row[1]).setScale(2));
        return resultMap;
    }

    public void populateNextYrBEinBudgetDetailList() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("starting populateNextYrBEinBudgetDetailList..................");
        if (!savedbudgetDetailList.isEmpty())
            for (final BudgetDetail budgetDetail : savedbudgetDetailList) {
                final BudgetDetail nextYrbudgetDetail = (BudgetDetail) persistenceService.find(
                        "from BudgetDetail where uniqueNo=? and budget.referenceBudget=?", budgetDetail.getUniqueNo(),
                        budgetDetail.getBudget());
                // budgetDetail.setNextYrId(nextYrbudgetDetail.getId());
                budgetDetail.setNextYroriginalAmount(nextYrbudgetDetail.getOriginalAmount());
                budgetDetail.setNextYrapprovedAmount(nextYrbudgetDetail.getApprovedAmount());
                budgetDetail.setNextYrId(nextYrbudgetDetail.getId());
            }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Completed populateNextYrBEinBudgetDetailList");
    }

    @SkipValidation
    private void loadApproverUser(final List<BudgetDetail> budgetDetailList)
    {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting loadApproverUser.....");
        final Map<String, Object> map = voucherService.getDesgBYPassingWfItem("BudgetDetail.nextDesg", null, budgetDetail
                .getExecutingDepartment().getId().intValue());
        addDropdownData("departmentList", masterDataCache.get("egi-department"));
        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);

        final List<Map<String, Object>> desgList = (List<Map<String, Object>>) map.get("designationList");
        String strDesgId = "", dName = "";
        boolean bDefaultDeptId = false;
        final List<Map<String, Object>> designationList = new ArrayList<Map<String, Object>>();
        Map<String, Object> desgFuncryMap;
        for (final Map<String, Object> desgIdAndName : desgList) {
            desgFuncryMap = new HashMap<String, Object>();

            if (desgIdAndName.get("designationName") != null)
                desgFuncryMap.put("designationName", desgIdAndName.get("designationName"));

            if (desgIdAndName.get("designationId") != null) {
                strDesgId = (String) desgIdAndName.get("designationId");
                if (strDesgId.indexOf("~") != -1) {
                    strDesgId = strDesgId.substring(0, strDesgId.indexOf('~'));
                    dName = (String) desgIdAndName.get("designationId");
                    dName = dName.substring(dName.indexOf('~') + 1);
                    bDefaultDeptId = true;
                }
                desgFuncryMap.put("designationId", strDesgId);
            }
            designationList.add(desgFuncryMap);
        }
        map.put("designationList", designationList);

        addDropdownData("designationList", (List<Designation>) map.get("designationList"));
        if (bDefaultDeptId && !dName.equals("")) {
            final Department dept = (Department) persistenceService.find("from Department where deptName like '%" + dName + "' ");
            defaultDept = dept.getId().intValue();
        }
        wfitemstate = map.get("wfitemstate") != null ? map.get("wfitemstate").toString() : "";
        if (LOGGER.isInfoEnabled())
            LOGGER.info("finished loadApproverUser.....");
    }

    public boolean isConsolidatedScreen() {
        return consolidatedScreen;
    }

    public void setConsolidatedScreen(final boolean consolidatedScreen) {
        this.consolidatedScreen = consolidatedScreen;
    }

    public boolean isAllfunctionsArrived() {
        return allfunctionsArrived;
    }

    public void setAllfunctionsArrived(final boolean allfunctionsArrived) {
        this.allfunctionsArrived = allfunctionsArrived;
    }

    public Integer getApproverUserId() {
        return approverUserId;
    }

    public void setApproverUserId(final Integer approverUserId) {
        this.approverUserId = approverUserId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(final String actionName) {
        this.actionName = actionName;
    }

    public void setBudgetWorkflowService(
            final WorkflowService<Budget> budgetWorkflowService) {
        this.budgetWorkflowService = budgetWorkflowService;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public Date subtractYear(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    private String getAppConfigValueByKey(final String key) {
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF", key);
        String appValue = "-1";
        if (appList.isEmpty())
            appValue = " ";
        else
            appValue = appList.get(0).getValue();
        return appValue;
    }

    void computeTotal(final BudgetProposalBean bpbeanTotal, final BudgetProposalBean bpbean) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting computeTotal................");
        bpbeanTotal.setPreviousYearActuals(bpbeanTotal.getPreviousYearActuals() == null ? bpbean.getPreviousYearActuals()
                : new BigDecimal(bpbeanTotal.getPreviousYearActuals()).add(new BigDecimal(bpbean.getPreviousYearActuals()))
                .toString());
        bpbeanTotal.setTwoPreviousYearActuals(bpbeanTotal.getTwoPreviousYearActuals() == null ? bpbean
                .getTwoPreviousYearActuals() : new BigDecimal(bpbeanTotal.getTwoPreviousYearActuals()).add(
                        new BigDecimal(bpbean.getTwoPreviousYearActuals())).toString());
        bpbeanTotal.setCurrentYearActuals(bpbeanTotal.getCurrentYearActuals() == null ? bpbean.getCurrentYearActuals()
                : new BigDecimal(bpbeanTotal.getCurrentYearActuals()).add(new BigDecimal(bpbean.getCurrentYearActuals()))
                .toString());
        bpbeanTotal.setCurrentYearBE(bpbeanTotal.getCurrentYearBE() == null ? bpbean.getCurrentYearBE() : new BigDecimal(
                bpbeanTotal.getCurrentYearBE()).add(new BigDecimal(bpbean.getCurrentYearBE())).toString());
        bpbeanTotal.setReappropriation(bpbeanTotal.getReappropriation() == null ? bpbean.getReappropriation() : new BigDecimal(
                bpbeanTotal.getReappropriation()).add(new BigDecimal(bpbean.getReappropriation())).toString());
        bpbeanTotal.setTotal(bpbeanTotal.getTotal() == null ? bpbean.getTotal() : new BigDecimal(bpbeanTotal.getTotal()).add(
                new BigDecimal(bpbean.getTotal())).toString());
        bpbeanTotal.setAnticipatory(bpbeanTotal.getAnticipatory() == null ? bpbean.getAnticipatory() : new BigDecimal(
                bpbeanTotal.getAnticipatory()).add(new BigDecimal(bpbean.getAnticipatory())).toString());
        bpbeanTotal.setProposedRE(bpbeanTotal.getProposedRE() == null ? bpbean.getProposedRE() : bpbeanTotal.getProposedRE()
                .add(bpbean.getProposedRE()).setScale(2));
        bpbeanTotal.setProposedBE(bpbeanTotal.getProposedBE() == null ? bpbean.getProposedBE() : bpbeanTotal.getProposedBE()
                .add(bpbean.getProposedBE()).setScale(2));
        bpbeanTotal.setApprovedRE(bpbeanTotal.getApprovedRE() == null ? bpbean.getApprovedRE() : bpbeanTotal.getApprovedRE()
                .add(bpbean.getApprovedRE()).setScale(2));
        bpbeanTotal.setApprovedBE(bpbeanTotal.getApprovedBE() == null ? bpbean.getApprovedBE() : bpbeanTotal.getApprovedBE()
                .add(bpbean.getApprovedBE()).setScale(2));
        bpbeanTotal.setRowType(TOTAL);
        bpbeanTotal.setBudgetGroup("TOTAL");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished computeTotal");
    }

    
    @SkipValidation
    @Action(value = "/budget/budgetProposal-update")
    public String update()
    {
        // Only save the items
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Stating updation .....");
        if (approverUserId != null && approverUserId != -1)
            userId = approverUserId;
        else
            userId = EgovThreadLocals.getUserId().intValue();

        topBudget = budgetService.find("from Budget where id=?", topBudget.getId());
        final Position positionByUserId = eisCommonService.getPositionByUserId(userId.longValue());
        final PersonalInformation empForCurrentUser = budgetDetailService.getEmpForCurrentUser();
        String name = "";
        if (empForCurrentUser != null)
            name = empForCurrentUser.getName();
        if (name == null)
            name = empForCurrentUser.getEmployeeFirstName();

        if (actionName.contains("save"))
        {
            if (consolidatedScreen)
                save(BigDecimal.valueOf(1000));
            else
                save(null);
            addActionMessage("Budget/BudgetDetails Saved Succesfully");

        }
        // Save and Push the items
        else if (actionName.contains("forward"))
        {
            final boolean hod = isHOD();
            if (consolidatedScreen || hod)
            {
                if (hod)
                    save(null);
                else
                    save(BigDecimal.valueOf(1000));
                topBudget.transition(true).withStateValue("Forwarded by " + name).withOwner(positionByUserId)
                .withComments(comment);

            }
            else {

                saveWithForward(positionByUserId, name, hod);
                if (isNextUserHOD(approverUserId) || hod)
                    topBudget.transition(true).withStateValue("Forwarded by " + name).withOwner(positionByUserId)
                    .withComments(comment);

            }
            addActionMessage("Budget/BudgetDetails Forwarded Succesfully to "
                    + budgetService.getEmployeeNameAndDesignationForPosition(positionByUserId));

        }
        // Final approval
        else if (actionName.contains("approve"))
        {
            save(BigDecimal.valueOf(1000));
            topBudget.transition(true).withStateValue("END").withOwner(positionByUserId).withComments(comment);
            addActionMessage("Budget/BudgetDetails Approved Succesfully ");
        }
        budgetService.persist(topBudget);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed updation .....");
        return "message";
    }

    
    private void save(final BigDecimal multiplicationFactor)
    {
        String columntoupdate = "originalAmount";
        if (multiplicationFactor != null)
        {
            columntoupdate = "approvedAmount";
        }
        final String sql = "update egf_budgetdetail set " + columntoupdate + "=:amount,document_Number=:docNo where id=:id";
        String commentsql = "";
        if (isHOD())
            commentsql = "update eg_wf_states set  text1=:text1, value='END' where id=(select state_id from egf_budgetdetail where  id=:id)";
        else
            commentsql = "update eg_wf_states set  text1=:text1 where id=(select state_id from egf_budgetdetail where  id=:id)";
        final SQLQuery updateQuery = HibernateUtil.getCurrentSession().createSQLQuery(sql);
        final SQLQuery updateCommentQuery = HibernateUtil.getCurrentSession().createSQLQuery(commentsql);
        int i = 0;

        for (final BudgetProposalBean bpBean : bpBeanList)
        {

            if (bpBean == null || bpBean.getId() == null)
                continue;
            if (multiplicationFactor != null)
                updateQuery.setBigDecimal("amount", bpBean.getApprovedRE().multiply(multiplicationFactor));
            else
                updateQuery.setBigDecimal("amount", bpBean.getProposedRE());

            updateQuery.setString("docNo", bpBean.getDocumentNumber() == null ? null : bpBean.getDocumentNumber().toString());
            updateQuery.setLong("id", bpBean.getId());
            updateQuery.executeUpdate();
            if (multiplicationFactor != null)
                updateQuery.setBigDecimal("amount", bpBean.getApprovedBE().multiply(multiplicationFactor));
            else
                updateQuery.setBigDecimal("amount", bpBean.getProposedBE());
            updateQuery.setLong("id", bpBean.getNextYrId());
            updateQuery.executeUpdate();
            updateCommentQuery.setString("text1", bpBean.getRemarks());
            updateCommentQuery.setLong("id", bpBean.getId());
            updateCommentQuery.executeUpdate();

            i++;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Updated  " + i + "record.....");
            if (i % 10 == 0)
            {
                HibernateUtil.getCurrentSession().flush();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("flushed for " + i + "record.....");

            }

        }
        HibernateUtil.getCurrentSession().flush();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed Save .....");
    }

    
    private void saveWithForward(final Position pos, final String name, final boolean hod)
    {

        BudgetDetail bd = null;
        BudgetDetail nextYearBd = null;
        String stateString = "Forwarded by " + name;
        int i = 0;
        if (hod)
            stateString = "END";
        for (final BudgetProposalBean bpBean : bpBeanList)
        {
            if (bpBean == null || bpBean.getId() == null)
                continue;
            bd = budgetDetailService.find("from BudgetDetail where id=?", bpBean.getId());
            bd.setOriginalAmount(bpBean.getProposedRE());
            if (bpBean.getDocumentNumber() != null)
                bd.setDocumentNumber(bpBean.getDocumentNumber());
            bd.transition(true).withStateValue(stateString).withOwner(pos).withComments(bpBean.getRemarks());
            budgetDetailService.persist(bd);

            nextYearBd = budgetDetailService.find("from BudgetDetail where id=?", bpBean.getNextYrId());
            nextYearBd.setOriginalAmount(bpBean.getProposedBE());
            nextYearBd.transition(true).withStateValue(stateString).withOwner(pos).withComments(bpBean.getRemarks());
            budgetDetailService.persist(nextYearBd);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Updated  " + i + "record.....");
            i++;
            if (i % 10 == 0)
            {
                HibernateUtil.getCurrentSession().flush();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("flushed for " + i + "record.....");
            }

        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed saveWithForward .....");
    }

    
    public String modifyList()
    {
        if (budgetDetail.getBudget().getId() != null)
            topBudget = budgetService.find("from Budget where id=?", budgetDetail.getBudget().getId());
        consolidatedScreen = budgetDetailService.toBeConsolidated();
        if (isHOD())
            allfunctionsArrived = validateForAllFunctionsMappedForDept(topBudget, getPosition());

        return "detailList";

    }

    private boolean isHOD()
    {
        // TODO: Now employee is extending user so passing userid to get assingment -- changes done by Vaibhav
        final Assignment empAssignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(EgovThreadLocals.getUserId(),
                new Date());
        if (empAssignment.getDesignation().getName().equalsIgnoreCase("assistant"))
        {
            asstFMU = true;
            final BudgetDetail approvedBd = (BudgetDetail) persistenceService.find(
                    " from  BudgetDetail where budget=? and approvedAmount>0 ", topBudget);
            if (approvedBd != null)
                updateApprovedRE = false;
            else
                updateApprovedRE = true;
        } else if (empAssignment.getDesignation().getName().equalsIgnoreCase("CHIEF ACCOUNTS OFFICER"))
            asstFMU = true;

        return eisCommonService.isHod(empAssignment.getId());
    }

    public Position getPosition() throws ApplicationRuntimeException
    {
        Position pos;
        // TODO: Now employee is extending user so passing userid to get assingment -- changes done by Vaibhav
        pos = eisCommonService.getPrimaryAssignmentPositionForEmp(EgovThreadLocals.getUserId());
        return pos;
    }

    private boolean isNextUserHOD(final Integer approverUserId)
    {
        final Assignment empAssignment = eisCommonService
                .getLatestAssignmentForEmployeeByToDate(approverUserId.longValue(), new Date());
        return eisCommonService.isHod(empAssignment.getId());
    }

    private boolean validateForAllFunctionsMappedForDept(final Budget topBudget, final Position position)
    {
        final BudgetDetail bd = budgetDetailService.find("from BudgetDetail  where budget.id=?", topBudget.getId());
        final String Query = "select distinct(f.name) as functionid from eg_dept_functionmap m,function f where departmentid=" +
                bd.getExecutingDepartment().getId() + " and f.id= m.functionid and m.budgetaccount_Type='"
                + budgetDetailHelper.accountTypeForFunctionDeptMap(topBudget.getName()) + "'" +
                " minus " +
                " select distinct(f.name) as functionid from egf_budgetdetail bd,eg_wf_states s,function f where bd.budget="
                + topBudget.getId() + " and bd.state_id=s.id and s.owner=" + position.getId()
                + " and bd.function=f.id order by functionid";
        final Query functionsNotUsed = HibernateUtil.getCurrentSession()
                .createSQLQuery(Query);
        final List<String> notUsedList = functionsNotUsed.list();

        if (notUsedList.size() > 0) {
            functionsNotYetReceiced = "";
            for (final String s : notUsedList)
                functionsNotYetReceiced = functionsNotYetReceiced + s + " ,";
            return false;

        } else
            return true;
    }

    public List<org.egov.infstr.workflow.Action> getValidActions() {
        List<org.egov.infstr.workflow.Action> validButtons = null;
        validButtons = budgetWorkflowService.getValidActions(getTopBudget());
        return validButtons;

    }

    public String capitalize(final String value) {
        if (value == null || value.length() == 0)
            return value;
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    @SuppressWarnings("unchecked")
    public String getUlbName() {
        final Query query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
        final List<String> result = query.list();
        if (result != null)
            return result.get(0);
        return "";
    }

    @Action(value = "/budget/budgetProposal-generatePdf")
    public String generatePdf() throws Exception {
        try {
            bpBeanList = new ArrayList<BudgetProposalBean>();
            populateBudgetDetailReport();
            final Map<String, Object> reportParams = new HashMap<String, Object>();
            reportParams.put("title", getUlbName());
            reportParams.put("subtitle", getTopBudget().getName() != null ? "Budget-:" + getTopBudget().getName() : "");
            reportParams.put("amount", isConsolidatedScreen() ? "Amount in Thousand" : "Amount in Rupees");
            reportParams.put("twopreviousfinYearRange", twopreviousfinYearRange);
            reportParams.put("previousfinYearRange", previousfinYearRange);
            reportParams.put("currentfinYearRange", currentfinYearRange);
            reportParams.put("nextfinYearRange", nextfinYearRange);
            // bpBeanList=new ArrayList<BudgetProposalBean>();
            String templateName = "";
            if (isConsolidatedScreen() || isHod())
            {
                templateName = "budgetProposalReport";
                fileName = "BudgetProposalReport." + FileFormat.PDF.toString().toLowerCase();
            } else
            {
                templateName = "budgetProposalReport-draft";
                fileName = "BudgetProposalReport-draft." + FileFormat.PDF.toString().toLowerCase();
            }

            final ReportRequest reportInput = new ReportRequest(templateName, bpBeanList, reportParams);
            reportInput.setReportFormat(FileFormat.PDF);
            contentType = ReportViewerUtil.getContentType(FileFormat.PDF);
            final ReportOutput reportOutput = reportService.createReport(reportInput);
            if (reportOutput != null && reportOutput.getReportOutputData() != null)
                inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        } catch (final Exception e) {
            LOGGER.error(e, e);
        }
        return "reportview";
        /*
         * bpBeanList=new ArrayList<BudgetProposalBean>(); populateBudgetDetailReport(); String title=getUlbName() ; String
         * subtitle=getTopBudget().getName()!=null?"Budget-:"+getTopBudget().getName():""; JasperPrint jasper =
         * reportHelper.generateBudgetReportForHOD
         * (bpBeanList,title,subtitle,twopreviousfinYearRange,previousfinYearRange,currentfinYearRange
         * ,nextfinYearRange,isConsolidatedScreen()); inputStream = reportHelper.exportPdf(inputStream, jasper); return "PDF";
         */
    }

    /*
     * public String generateXls() throws Exception{ //String subtitle="Amount in Rupess"; bpBeanList=new
     * ArrayList<BudgetProposalBean>(); populateBudgetDetailReport(); String title=getUlbName() ; String
     * subtitle=getTopBudget().getName()!=null?"Budget-:"+getTopBudget().getName():""; JasperPrint jasper =
     * reportHelper.generateBudgetReportForHOD
     * (bpBeanList,title,subtitle,twopreviousfinYearRange,previousfinYearRange,currentfinYearRange
     * ,nextfinYearRange,isConsolidatedScreen()); inputStream = reportHelper.exportXls(inputStream, jasper); return "XLS"; }
     */

    @Action(value = "/budget/budgetProposal-generateXls")
    public String generateXls() {
        try {
            bpBeanList = new ArrayList<BudgetProposalBean>();
            populateBudgetDetailReport();
            final Map<String, Object> reportParams = new HashMap<String, Object>();
            reportParams.put("title", getUlbName());
            reportParams.put("subtitle", getTopBudget().getName() != null ? "Budget-:" + getTopBudget().getName() : "");
            reportParams.put("amount", isConsolidatedScreen() ? "Amount in Thousand" : "Amount in Rupees");
            reportParams.put("twopreviousfinYearRange", twopreviousfinYearRange);
            reportParams.put("previousfinYearRange", previousfinYearRange);
            reportParams.put("currentfinYearRange", currentfinYearRange);
            reportParams.put("nextfinYearRange", nextfinYearRange);
            // bpBeanList=new ArrayList<BudgetProposalBean>();
            String templateName = "";
            if (isConsolidatedScreen() || isHod())
            {
                templateName = "budgetProposalReport";
                fileName = "BudgetProposalReport." + FileFormat.XLS.toString().toLowerCase();
            } else
            {
                templateName = "budgetProposalReport-draft";
                fileName = "BudgetProposalReport-draft." + FileFormat.XLS.toString().toLowerCase();
            }

            final ReportRequest reportInput = new ReportRequest(templateName, bpBeanList, reportParams);
            reportInput.setReportFormat(FileFormat.XLS);
            contentType = ReportViewerUtil.getContentType(FileFormat.XLS);

            final ReportOutput reportOutput = reportService.createReport(reportInput);
            if (reportOutput != null && reportOutput.getReportOutputData() != null)
                inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        } catch (final Exception e) {
            LOGGER.error(e, e);
        }

        return "reportview";
    }

    protected Boolean validateOwner()
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("validating owner for user " + EgovThreadLocals.getUserId());
        List<Position> positionsForUser = null;
        positionsForUser = eisService.getPositionsForUser(Long.valueOf(EgovThreadLocals.getUserId()), new Date());
        State state = null;
        if (factor.equalsIgnoreCase("thousand"))
            state = (State) persistenceService
            .find("select b.state from Budget b where b.id =(select bd.budget.id from BudgetDetail bd where bd.id=?) ",
                    validId);
        else
            state = (State) persistenceService.find("select bd.state from BudgetDetail bd where bd.id=? ", validId);
        if (state != null && positionsForUser.contains(state.getOwnerPosition()))
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Valid Owner :return true");
            return true;
        } else
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Invalid  Owner :return false");
            return false;
        }
    }

    public BudgetProposalBean getBpBean() {
        return bpBean;
    }

    public void setBpBean(final BudgetProposalBean bpBean) {
        this.bpBean = bpBean;
    }

    public BudgetDetail getBudgetDetail() {
        return budgetDetail;
    }

    public void setBudgetDetail(final BudgetDetail budgetDetail) {
        this.budgetDetail = budgetDetail;
    }

    public Budget getTopBudget() {
        return topBudget;
    }

    public void setTopBudget(final Budget topBudget) {
        this.topBudget = topBudget;
    }

    public VoucherService getVoucherService() {
        return voucherService;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public FinancialYearHibernateDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setFinancialYearDAO(final FinancialYearHibernateDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public String getCurrentfinYearRange() {
        return currentfinYearRange;
    }

    public void setCurrentfinYearRange(final String currentfinYearRange) {
        this.currentfinYearRange = currentfinYearRange;
    }

    public String getNextfinYearRange() {
        return nextfinYearRange;
    }

    public void setNextfinYearRange(final String nextfinYearRange) {
        this.nextfinYearRange = nextfinYearRange;
    }

    public String getPreviousfinYearRange() {
        return previousfinYearRange;
    }

    public void setPreviousfinYearRange(final String previousfinYearRange) {
        this.previousfinYearRange = previousfinYearRange;
    }

    public String getTwopreviousfinYearRange() {
        return twopreviousfinYearRange;
    }

    public void setTwopreviousfinYearRange(final String twopreviousfinYearRange) {
        this.twopreviousfinYearRange = twopreviousfinYearRange;
    }

    public List<BudgetDetail> getBudgetDetailList() {
        return budgetDetailList;
    }

    public void setBudgetDetailList(final List<BudgetDetail> budgetDetailList) {
        this.budgetDetailList = budgetDetailList;
    }

    public List<BudgetDetail> getSavedbudgetDetailList() {
        return savedbudgetDetailList;
    }

    public void setSavedbudgetDetailList(final List<BudgetDetail> savedbudgetDetailList) {
        this.savedbudgetDetailList = savedbudgetDetailList;
    }

    public BudgetDetailConfig getBudgetDetailConfig() {
        return budgetDetailConfig;
    }

    public void setBudgetDetailConfig(final BudgetDetailConfig budgetDetailConfig) {
        this.budgetDetailConfig = budgetDetailConfig;
    }

    public BudgetDetailService getBudgetDetailService() {
        return budgetDetailService;
    }

    public void setBudgetDetailService(final BudgetDetailService budgetDetailService) {
        this.budgetDetailService = budgetDetailService;
    }

    public BudgetService getBudgetService() {
        return budgetService;
    }

    public void setBudgetService(final BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public Map<Long, String> getPreviuosYearBudgetDetailMap() {
        return previuosYearBudgetDetailMap;
    }

    public void setPreviuosYearBudgetDetailMap(
            final Map<Long, String> previuosYearBudgetDetailMap) {
        this.previuosYearBudgetDetailMap = previuosYearBudgetDetailMap;
    }

    public Map<Long, String> getBeforePreviousYearBudgetDetailMap() {
        return beforePreviousYearBudgetDetailMap;
    }

    public void setBeforePreviousYearBudgetDetailMap(
            final Map<Long, String> beforePreviousYearBudgetDetailMap) {
        this.beforePreviousYearBudgetDetailMap = beforePreviousYearBudgetDetailMap;
    }

    public Map<String, BigDecimal> getBudgetDetailIdsAndAmount() {
        return budgetDetailIdsAndAmount;
    }

    public void setBudgetDetailIdsAndAmount(
            final Map<String, BigDecimal> budgetDetailIdsAndAmount) {
        this.budgetDetailIdsAndAmount = budgetDetailIdsAndAmount;
    }

    public Map<String, BigDecimal> getPreviousYearBudgetDetailIdsAndAmount() {
        return previousYearBudgetDetailIdsAndAmount;
    }

    public void setPreviousYearBudgetDetailIdsAndAmount(
            final Map<String, BigDecimal> previousYearBudgetDetailIdsAndAmount) {
        this.previousYearBudgetDetailIdsAndAmount = previousYearBudgetDetailIdsAndAmount;
    }

    public Map<String, BigDecimal> getTwopreviousYearBudgetDetailIdsAndAmount() {
        return twopreviousYearBudgetDetailIdsAndAmount;
    }

    public void setTwopreviousYearBudgetDetailIdsAndAmount(
            final Map<String, BigDecimal> twopreviousYearBudgetDetailIdsAndAmount) {
        this.twopreviousYearBudgetDetailIdsAndAmount = twopreviousYearBudgetDetailIdsAndAmount;
    }

    public String getWfitemstate() {
        return wfitemstate;
    }

    public void setWfitemstate(final String wfitemstate) {
        this.wfitemstate = wfitemstate;
    }

    public Integer getDefaultDept() {
        return defaultDept;
    }

    public Date getHeaderAsOnDate() {
        return headerAsOnDate = asOndate != null ? asOndate : new Date();
    }

    /*
     * public void setHeaderAsOnDate(Date headerAsOnDate) { this.headerAsOnDate = headerAsOnDate; }
     */

    public void setDefaultDept(final Integer defaultDept) {
        this.defaultDept = defaultDept;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public static String getActionname() {
        return ACTIONNAME;
    }

    public List<BudgetProposalBean> getBpBeanList() {
        return bpBeanList;
    }

    public void setBpBeanList(final List<BudgetProposalBean> bpBeanList) {
        this.bpBeanList = bpBeanList;
    }

    public Date getAsOndate() {
        return asOndate;
    }

    public void setAsOndate(final Date asOndate) {
        this.asOndate = asOndate;
    }

    public String getFunctionsNotYetReceiced() {
        return functionsNotYetReceiced;
    }

    public void setFunctionsNotYetReceiced(final String functionsNotYetReceiced) {
        this.functionsNotYetReceiced = functionsNotYetReceiced;
    }

    public boolean isAsstFMU() {
        return asstFMU;
    }

    public void setAsstFMU(final boolean asstFMU) {
        this.asstFMU = asstFMU;
    }

    public void setBudgetDetailHelper(final BudgetDetailHelper budgetDetailHelper) {
        this.budgetDetailHelper = budgetDetailHelper;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ReportHelper getReportHelper() {
        return reportHelper;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public Long getDocNo() {
        return docNo;
    }

    public void setDocNo(final Long docNo) {
        this.docNo = docNo;
    }

    public String getAmountField() {
        return amountField;
    }

    public void setAmountField(final String amountField) {
        this.amountField = amountField;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(final String factor) {
        this.factor = factor;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(final Long detailId) {
        this.detailId = detailId;
    }

    public Long getValidId() {
        return validId;
    }

    public void setValidId(final Long validId) {
        this.validId = validId;
    }

}