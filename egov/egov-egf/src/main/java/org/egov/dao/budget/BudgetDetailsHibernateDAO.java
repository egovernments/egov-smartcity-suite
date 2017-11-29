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
/*
 * Created on Jan 17, 2006 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style -
 * Code Templates
 */
package org.egov.dao.budget;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FunctionDAO;
import org.egov.egf.autonumber.BanNumberGenerator;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBillregister;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetUsage;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetGroupService;
import org.egov.services.budget.BudgetService;
import org.egov.services.budget.BudgetUsageService;
import org.egov.utils.BudgetAccountType;
import org.egov.utils.Constants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
@Transactional(readOnly = true)
public class BudgetDetailsHibernateDAO implements BudgetDetailsDAO {
    private static int count = 0;

    public BudgetDetailsHibernateDAO() {
        super();
    }

    @Transactional
    public BudgetDetail update(final BudgetDetail entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public BudgetDetail create(final BudgetDetail entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(BudgetDetail entity) {
        getCurrentSession().delete(entity);
    }

    public List<BudgetDetail> findAll() {
        return (List<BudgetDetail>) getCurrentSession().createCriteria(BudgetDetail.class).list();
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    private static final String EGF = "EGF";
    private static final String BUDGETHEADID = "budgetheadid";
    private static final String GLCODEID = "glcodeid";
    private static final String BUDGETARY_CHECK_GROUPBY_VALUES = "budgetaryCheck_groupby_values";
    private Session session;
    private static final Logger LOGGER = Logger.getLogger(BudgetDetailsHibernateDAO.class);
    private static final String EMPTY_STRING = "";
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    protected ScriptService scriptService;
    @Autowired
    protected ApplicationSequenceNumberGenerator sequenceGenerator;
    @Autowired
    @Qualifier("budgetService")
    private BudgetService budgetService;
    @Autowired
    @Qualifier("budgetDetailService")
    private BudgetDetailService budgetDetailService;
    @Autowired
    @Qualifier("budgetGroupService")
    private BudgetGroupService budgetGroupService;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    @Qualifier("financialYearDAO")
    private FinancialYearHibernateDAO financialYearHibDAO;
    @Autowired
    private FunctionDAO functionDAO;
    @Autowired
    @Qualifier("budgetUsageService")
    private BudgetUsageService budgetUsageService;
    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    private static Logger LOG = Logger.getLogger(BudgetDetailsHibernateDAO.class);
    @Autowired
    private BudgetControlTypeService budgetCheckConfigService;

    /**
     * This API is to check whether the planning budget is available or not.For
     * the amount passed if there is sufficient budget available API will return
     * TRUE. Else it will return FALSE. At any point the budgetavailable will
     * show the right picture of how much more we can plan for.
     * <p>
     * Assumptions- 1) on load of the budget there will be budgetavailable
     * amount loaded using the multiplier factor. 2) on load of supplementary
     * budget budget available is recalculated and updated.
     * <p>
     * For the sake of audit we should be updating the budgetusage object with
     * the reference object and the moduleid and amount for any budget consumed.
     * This will be used for reporting as to which object consumed how much and
     * when.
     *
     * @param financialyearid
     *            This is the id from the financial year object
     * @param moduleid
     *            This is the id of the module, say for payroll 7 and for stores
     *            8 and for PTIS it is 2
     * @param referencenumber
     *            This is the module object reference number, say purchase order
     *            number or estimate number
     * @param departmentid
     *            This is the id of the department object
     * @param functionid
     *            This is the id of the function object
     * @param functionaryid
     *            This is the id of the functionary object
     * @param schemeid
     *            This is the id of the scheme object
     * @param subschemeid
     *            This is the id of the sub scheme object
     * @param boundaryid
     *            This is the id of the boundary object
     * @param budgetheadid
     *            This is the id of budgegroup object
     * @param amount
     *            This is the amount of which budget needs to be allocated
     * @return boolean
     * @throws Exception
     */
    public boolean consumeEncumbranceBudget(final Map<String, Object> detailsMap) {
        if (detailsMap == null)
            throw new ValidationException(
                    Arrays.asList(new ValidationError("required input is null", "required input is null")));
        detailsMap.put(Constants.CONSUMEORRELEASE, true);
        final BigDecimal bd = getDetails(detailsMap);
        return bd.intValue() == 1;
    }

    @Override
    public boolean consumeEncumbranceBudget(final Long financialyearid, final Integer moduleid,
            final String referencenumber, final Integer departmentid, final Long functionid,
            final Integer functionaryid, final Integer schemeid, final Integer subschemeid, final Integer boundaryid,
            final List<Long> budgetheadid, final Integer fundid, final double amount,
            final String appropriationnumber) {

        final BigDecimal bd = getDetails(financialyearid, moduleid, referencenumber, departmentid, functionid,
                functionaryid, schemeid, subschemeid, boundaryid, budgetheadid, fundid, amount, true,
                appropriationnumber);
        return bd.intValue() == 1;
    }

    /**
     * Does the same as the above API. Except this API returns BudgetUsage
     * object if the available amount is >=0. If there is insufficient available
     * amount then null is returned. Note that the first parameter is a String,
     * unlike the previous API.
     * 
     * @param appropriationnumber
     * @param financialyearid
     * @param moduleid
     * @param referencenumber
     * @param departmentid
     * @param functionid
     * @param functionaryid
     * @param schemeid
     * @param subschemeid
     * @param boundaryid
     * @param budgetheadid
     * @param fundid
     * @param amount
     * @return BudgetUsage @
     */
    @Override
    @Deprecated
    public BudgetUsage consumeEncumbranceBudget(final String appropriationnumber, final Long financialyearid,
            final Integer moduleid, final String referencenumber, final Integer departmentid, final Long functionid,
            final Integer functionaryid, final Integer schemeid, final Integer subschemeid, final Integer boundaryid,
            final List<Long> budgetheadid, final Integer fundid, final double amount) {

        return getBudgetUsageDetails(financialyearid, moduleid, referencenumber, departmentid, functionid,
                functionaryid, schemeid, subschemeid, boundaryid, budgetheadid, fundid, amount, true,
                appropriationnumber);
    }

    /**
     * This API will be called for releasing the budget that was already
     * allocated to some estimate or purchase order. On calling this the amount
     * that was allocated to some entity will get released. Budget available
     * will be increased.
     * <p>
     * On modify of any entity they should first call the release budget and
     * then call the consume budget. The budget usage table needs to be updated
     * anytime this is invoked with the date and reference object number.
     * 
     * @param detailsMap
     *            is the map containig following fields financialyearid This is
     *            the id from the financial year object moduleid This is the id
     *            of the module, say for payroll 7 and for stores 8 and for PTIS
     *            it is 2 referencenumber This is the module object reference
     *            number, say purchase order number or estimate number
     *            departmentid This is the id of the department object
     *            functionid This is the id of the function object functionaryid
     *            This is the id of the functionary object schemeid This is the
     *            id of the scheme object subschemeid This is the id of the sub
     *            scheme object fieldid This is the id of the boundary object
     *            budgetheadid This is the id of budgegroup object amount This
     *            is the amount of which budget needs to be allocated
     * @throws Exception
     */
    public boolean releaseEncumbranceBudget(final Map<String, Object> detailsMap) {
        if (detailsMap == null)
            throw new ValidationException(
                    Arrays.asList(new ValidationError("required input is null", "required input is null")));
        detailsMap.put(Constants.CONSUMEORRELEASE, false);
        final BigDecimal bd = getDetails(detailsMap);
        return bd.intValue() == 1;
    }

    @Override
    @Deprecated
    public void releaseEncumbranceBudget(final Long financialyearid, final Integer moduleid,
            final String referencenumber, final Integer departmentid, final Long functionid,
            final Integer functionaryid, final Integer schemeid, final Integer subschemeid, final Integer boundaryid,
            final List<Long> budgetheadid, final Integer fundid, final double amount,
            final String appropriationnumber) {
        getDetails(financialyearid, moduleid, referencenumber, departmentid, functionid, functionaryid, schemeid,
                subschemeid, boundaryid, budgetheadid, fundid, amount, false, appropriationnumber);
    }

    /**
     * This does the same as the above API except this returns the BudgetUsage
     * object that is modified. Please note that first parameter is a string.
     * 
     * @param appropriationnumber
     * @param financialyearid
     * @param moduleid
     * @param referencenumber
     * @param departmentid
     * @param functionid
     * @param functionaryid
     * @param schemeid
     * @param subschemeid
     * @param boundaryid
     * @param budgetheadid
     * @param fundid
     * @param amount
     * @return BudgetUsage object @
     */

    @Override
    @Transactional
    public BudgetUsage releaseEncumbranceBudget(final String appropriationnumber, final Long financialyearid,
            final Integer moduleid, final String referencenumber, final Integer departmentid, final Long functionid,
            final Integer functionaryid, final Integer schemeid, final Integer subschemeid, final Integer boundaryid,
            final List<Long> budgetheadid, final Integer fundid, final double amount) {
        return getBudgetUsageDetails(financialyearid, moduleid, referencenumber, departmentid, functionid,
                functionaryid, schemeid, subschemeid, boundaryid, budgetheadid, fundid, amount, false,
                appropriationnumber);
    }

    private BigDecimal getDetails(final Map<String, Object> detailsMap) {
        Long financialyearid = null;
        Integer moduleid = null;
        String referencenumber = null;
        Integer departmentid = null;
        Long functionid = null;
        Integer functionaryid = null;
        Integer schemeid = null;
        Integer subschemeid = null;
        Integer boundaryid = null;
        List<Long> budgetheadid = null;
        Integer fundid = null;
        double amount = 0.0d;
        String appropriationnumber = null;
        Boolean consumeOrRelease = null;

        if (detailsMap.containsKey(Constants.FINANCIALYEARID))
            financialyearid = (Long) detailsMap.get(Constants.FINANCIALYEARID);
        if (detailsMap.containsKey(Constants.MODULEID))
            moduleid = (Integer) detailsMap.get(Constants.MODULEID);
        if (detailsMap.containsKey(Constants.REFERENCENUMBER))
            referencenumber = (String) detailsMap.get(Constants.REFERENCENUMBER);
        if (detailsMap.containsKey(Constants.DEPARTMENTID))
            departmentid = (Integer) detailsMap.get(Constants.DEPARTMENTID);
        if (detailsMap.containsKey(Constants.FUNCTIONID))
            functionid = (Long) detailsMap.get(Constants.FUNCTIONID);
        if (detailsMap.containsKey(Constants.FUNCTIONARYID))
            functionaryid = (Integer) detailsMap.get(Constants.FUNCTIONARYID);
        if (detailsMap.containsKey(Constants.SCHEMEID))
            schemeid = (Integer) detailsMap.get(Constants.SCHEMEID);
        if (detailsMap.containsKey(Constants.SUBSCHEMEID))
            subschemeid = (Integer) detailsMap.get(Constants.SUBSCHEMEID);
        if (detailsMap.containsKey(Constants.BOUNDARYID))
            boundaryid = (Integer) detailsMap.get(Constants.BOUNDARYID);
        if (detailsMap.containsKey(Constants.BUDGETHEAD))
            budgetheadid = (List<Long>) detailsMap.get(Constants.BUDGETHEAD);
        if (detailsMap.containsKey(Constants.FUNDID))
            fundid = (Integer) detailsMap.get(Constants.FUNDID);
        if (detailsMap.containsKey(Constants.AMOUNT))
            amount = (Double) detailsMap.get(Constants.AMOUNT);
        if (detailsMap.containsKey(Constants.APPROPRIATIONNUMBER))
            appropriationnumber = (String) detailsMap.get(Constants.APPROPRIATIONNUMBER);
        if (detailsMap.containsKey(Constants.CONSUMEORRELEASE))
            consumeOrRelease = (Boolean) detailsMap.get(Constants.CONSUMEORRELEASE);

        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("financialyearid==" + financialyearid + ",moduleid==" + moduleid + ",referencenumber=="
                        + referencenumber + ",departmentid==" + departmentid + ",functionid==" + functionid
                        + ",functionaryid==" + functionaryid + ",schemeid==" + schemeid + ",subschemeid==" + subschemeid
                        + ",boundaryid==" + boundaryid + ",budgetheadid==" + budgetheadid + ",amount==" + amount);

            validateMandatoryParameters(moduleid, referencenumber);
            BigDecimal amtavailable = getPlanningBudgetAvailable(financialyearid, departmentid, functionid,
                    functionaryid, schemeid, subschemeid, boundaryid, budgetheadid, fundid);

            if (consumeOrRelease)
                amtavailable = amtavailable.subtract(BigDecimal.valueOf(amount));
            else
                amtavailable = amtavailable.add(BigDecimal.valueOf(amount));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("budget available after consuming/releasing=" + amtavailable);

            if (amtavailable != null && amtavailable.compareTo(BigDecimal.ZERO) >= 0) {
                // need to update budget details
                final String query = prepareQuery(departmentid, functionid, functionaryid, schemeid, subschemeid,
                        boundaryid, fundid);
                final Query q = getCurrentSession().createQuery(
                        " from BudgetDetail bd where  bd.budget.financialYear.id=:finYearId and bd.budget.isbere=:type and bd.budgetGroup.id in (:bgId)"
                                + query);
                if (budgetService.hasApprovedReForYear(financialyearid))
                    q.setParameter("type", "RE");
                else
                    q.setParameter("type", "BE");
                q.setParameter("finYearId", financialyearid);
                q.setParameterList("bgId", budgetheadid);
                final List<BudgetDetail> bdList = q.list();
                if (bdList == null || bdList.size() == 0) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(
                                "IN consumeEncumbranceBudget()-getDetail() - No budget detail item defined for RE or BE for this combination!!");
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("financial year id - " + financialyearid.toString() + " Budget Group -  "
                                + budgetheadid.toString() + " Query - " + query);
                    throw new ValidationException(EMPTY_STRING, "Budgetary Check Failed");
                }
                final BudgetDetail bd = bdList.get(0);
                bd.setBudgetAvailable(amtavailable);
                update(bd);

                final BudgetUsage budgetUsage = new BudgetUsage();
                budgetUsage.setFinancialYearId(financialyearid.intValue());
                budgetUsage.setModuleId(moduleid);
                budgetUsage.setReferenceNumber(referencenumber);
                budgetUsage.setBudgetDetail(bd);
                budgetUsage.setAppropriationnumber(appropriationnumber);
                if (consumeOrRelease) {
                    budgetUsage.setConsumedAmount(amount);
                    budgetUsage.setReleasedAmount(0.0);
                } else {
                    budgetUsage.setConsumedAmount(0.0);
                    budgetUsage.setReleasedAmount(amount);
                }
                budgetUsage.setCreatedby(ApplicationThreadLocals.getUserId().intValue());
                budgetUsageService.create(budgetUsage);
                return BigDecimal.ONE;
            } else
                return BigDecimal.ZERO;
        } catch (final ValidationException v) {
            LOGGER.error("Exp in consumeEncumbranceBudget API()===" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exception in consumeEncumbranceBudget API()=" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
    }

    @Deprecated
    private BigDecimal getDetails(final Long financialyearid, final Integer moduleid, final String referencenumber,
            final Integer departmentid, final Long functionid, final Integer functionaryid, final Integer schemeid,
            final Integer subschemeid, final Integer boundaryid, final List<Long> budgetheadid, final Integer fundid,
            final double amount, final boolean consumeOrRelease, final String appropriationnumber) {
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("financialyearid==" + financialyearid + ",moduleid==" + moduleid + ",referencenumber=="
                        + referencenumber + ",departmentid==" + departmentid + ",functionid==" + functionid
                        + ",functionaryid==" + functionaryid + ",schemeid==" + schemeid + ",subschemeid==" + subschemeid
                        + ",boundaryid==" + boundaryid + ",budgetheadid==" + budgetheadid + ",amount==" + amount);

            validateMandatoryParameters(moduleid, referencenumber);
            BigDecimal amtavailable = getPlanningBudgetAvailable(financialyearid, departmentid, functionid,
                    functionaryid, schemeid, subschemeid, boundaryid, budgetheadid, fundid);

            if (consumeOrRelease)
                amtavailable = amtavailable.subtract(BigDecimal.valueOf(amount));
            else
                amtavailable = amtavailable.add(BigDecimal.valueOf(amount));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("budget available after consuming/releasing=" + amtavailable);

            if (amtavailable != null && amtavailable.compareTo(BigDecimal.ZERO) >= 0) {
                // need to update budget details
                final String query = prepareQuery(departmentid, functionid, functionaryid, schemeid, subschemeid,
                        boundaryid, fundid);
                final Query q = persistenceService.getSession().createQuery(
                        " from BudgetDetail bd where  bd.budget.financialYear.id=:finYearId  and  bd.budget.isbere=:type and bd.budgetGroup.id in (:bgId)"
                                + query);
                if (budgetService.hasApprovedReForYear(financialyearid))
                    q.setParameter("type", "RE");
                else
                    q.setParameter("type", "BE");
                q.setParameter("finYearId", financialyearid);
                q.setParameterList("bgId", budgetheadid);
                final List<BudgetDetail> bdList = q.list();
                if (bdList == null || bdList.size() == 0) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(
                                "IN consumeEncumbranceBudget()-getDetail() - No budget detail item defined for RE or BE for this combination!!");
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("financial year id - " + financialyearid.toString() + " Budget Group -  "
                                + budgetheadid.toString() + " Query - " + query);
                    throw new ValidationException(EMPTY_STRING, "Budgetary Check Failed");
                }
                final BudgetDetail bd = bdList.get(0);
                bd.setBudgetAvailable(amtavailable);
                update(bd);

                final BudgetUsage budgetUsage = new BudgetUsage();
                budgetUsage.setFinancialYearId(financialyearid.intValue());
                budgetUsage.setModuleId(moduleid);
                budgetUsage.setReferenceNumber(referencenumber);
                budgetUsage.setBudgetDetail(bd);
                budgetUsage.setAppropriationnumber(appropriationnumber);
                if (consumeOrRelease) {
                    budgetUsage.setConsumedAmount(amount);
                    budgetUsage.setReleasedAmount(0.0);
                } else {
                    budgetUsage.setConsumedAmount(0.0);
                    budgetUsage.setReleasedAmount(amount);
                }
                budgetUsage.setCreatedby(ApplicationThreadLocals.getUserId().intValue());
                budgetUsageService.create(budgetUsage);
                return BigDecimal.ONE;
            } else
                return BigDecimal.ZERO;
        } catch (final ValidationException v) {
            throw v;
        } catch (final Exception e) {
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
    }

    @Deprecated
    @Transactional
    private BudgetUsage getBudgetUsageDetails(final Long financialyearid, final Integer moduleid,
            final String referencenumber, final Integer departmentid, final Long functionid,
            final Integer functionaryid, final Integer schemeid, final Integer subschemeid, final Integer boundaryid,
            final List<Long> budgetheadid, final Integer fundid, final double amount, final boolean consumeOrRelease,
            final String appropriationnumber) {
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("financialyearid==" + financialyearid + ",moduleid==" + moduleid + ",referencenumber=="
                        + referencenumber + ",departmentid==" + departmentid + ",functionid==" + functionid
                        + ",functionaryid==" + functionaryid + ",schemeid==" + schemeid + ",subschemeid==" + subschemeid
                        + ",boundaryid==" + boundaryid + ",budgetheadid==" + budgetheadid + ",amount==" + amount);

            validateMandatoryParameters(moduleid, referencenumber);
            BigDecimal amtavailable = getPlanningBudgetAvailable(financialyearid, departmentid, functionid,
                    functionaryid, schemeid, subschemeid, boundaryid, budgetheadid, fundid);

            if (consumeOrRelease)
                amtavailable = amtavailable.subtract(BigDecimal.valueOf(amount));
            else
                amtavailable = amtavailable.add(BigDecimal.valueOf(amount));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("budget available after consuming/releasing=" + amtavailable);
            if (BudgetControlType.BudgetCheckOption.MANDATORY.toString()
                    .equals(budgetCheckConfigService.getConfigValue())) {
                if (amtavailable.compareTo(BigDecimal.ZERO) < 0)
                    return null;
            }

            // need to update budget details
            final String query = prepareQuery(departmentid, functionid, functionaryid, schemeid, subschemeid,
                    boundaryid, fundid);
            final Query q = persistenceService.getSession().createQuery(
                    " from BudgetDetail bd where  bd.budget.financialYear.id=:finYearId  and  bd.budget.isbere=:type and bd.budgetGroup.id in (:bgId)"
                            + query);
            if (budgetService.hasApprovedReForYear(financialyearid))
                q.setParameter("type", "RE");
            else
                q.setParameter("type", "BE");
            q.setParameter("finYearId", financialyearid);
            q.setParameterList("bgId", budgetheadid);
            final List<BudgetDetail> bdList = q.list();

            if (bdList == null || bdList.size() == 0) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(
                            "IN consumeEncumbranceBudget()-getDetail() - No budget detail item defined for RE or BE for this combination!!");
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("financial year id - " + financialyearid.toString() + " Budget Group -  "
                            + budgetheadid.toString() + " Query - " + query);
                throw new ValidationException(EMPTY_STRING, "Budgetary Check Failed");
            }
            final BudgetDetail bd = bdList.get(0);
            bd.setBudgetAvailable(amtavailable);
            budgetDetailService.update(bd);

            final BudgetUsage budgetUsage = new BudgetUsage();
            budgetUsage.setFinancialYearId(financialyearid.intValue());
            budgetUsage.setModuleId(moduleid);
            budgetUsage.setReferenceNumber(referencenumber);
            budgetUsage.setBudgetDetail(bd);
            budgetUsage.setAppropriationnumber(appropriationnumber);
            if (consumeOrRelease) {
                budgetUsage.setConsumedAmount(amount);
                budgetUsage.setReleasedAmount(0.0);
            } else {
                budgetUsage.setConsumedAmount(0.0);
                budgetUsage.setReleasedAmount(amount);
            }
            budgetUsage.setCreatedby(ApplicationThreadLocals.getUserId().intValue());
            budgetUsageService.create(budgetUsage);
            return budgetUsage;

        } catch (final ValidationException v) {
            throw v;
        } catch (final Exception e) {
            LOGGER.error("Exception in consumeEncumbranceBudget API()=" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
    }

    private void validateParameters(final Long financialyearid, final Long functionid, final List<Long> budgetheadid) {
        if (financialyearid == null)
            throw new ValidationException(EMPTY_STRING, "Financial year id is null or empty");
        if (functionid == null)
            throw new ValidationException(EMPTY_STRING, "Function id is null or empty");
        if (budgetheadid == null || budgetheadid.size() == 0)
            throw new ValidationException(EMPTY_STRING, "Budget head id is null or empty");
        session = getCurrentSession();
        // fetch mandatory parameters
        final CFinancialYear financialyear = (CFinancialYear) financialYearHibDAO.findById(financialyearid, false);
        if (financialyear == null)
            throw new ValidationException(EMPTY_STRING, "Financial year is null or empty");

        final CFunction function = (CFunction) functionDAO.findById(functionid, false);
        if (function == null)
            throw new ValidationException(EMPTY_STRING, "Function is null or empty");

        for (final Long bgId : budgetheadid) {
            final BudgetGroup budgetGroup = budgetGroupService.findById(bgId, false);
            if (budgetGroup == null)
                throw new ValidationException(EMPTY_STRING, "Budget head is null or empty");
        }
    }

    private String prepareAgregateQuery(final Integer departmentid, final Long functionid, final Integer functionaryid,
            final Integer schemeid, final Integer subschemeid, final Integer boundaryid, final Integer fundid) {
        String query = EMPTY_STRING;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside the prepareAgregateQuery... " + departmentid + " >>>" + fundid);
        if (departmentid != null && departmentid != 0)
            query = query + getQuery(Department.class, departmentid, " and bd.executingDepartment=");
        if (functionid != null && functionid != 0)
            query = query + getQuery(CFunction.class, functionid, " and bd.function=");
        if (functionaryid != null && functionaryid != 0)
            query = query + getQuery(Functionary.class, functionaryid, " and bd.functionary=");
        if (fundid != null && fundid != 0)
            query = query + getQuery(Fund.class, fundid, " and bd.fund=");
        if (schemeid != null && schemeid != 0)
            query = query + getQuery(Scheme.class, schemeid, " and bd.scheme=");
        if (subschemeid != null && subschemeid != 0)
            query = query + getQuery(SubScheme.class, subschemeid, " and bd.subScheme=");
        if (boundaryid != null && boundaryid != 0)
            query = query + getQuery(Boundary.class, boundaryid, " and bd.boundary=");

        return " and bd.budget.status.code = 'Approved' " + query;
    }

    private String prepareQuery(final Integer departmentid, final Long functionid, final Integer functionaryid,
            final Integer schemeid, final Integer subschemeid, final Integer boundaryid, final Integer fundid) {
        String query = EMPTY_STRING;

        final List<AppConfigValues> list = appConfigValuesService.getConfigValuesByModuleAndKey(EGF,
                BUDGETARY_CHECK_GROUPBY_VALUES);
        if (list.isEmpty())
            throw new ValidationException(EMPTY_STRING, "budgetaryCheck_groupby_values is not defined in AppConfig");
        else {
            final AppConfigValues appConfigValues = list.get(0);
            final String[] values = StringUtils.split(appConfigValues.getValue(), ",");
            for (final String value : values)
                if (value.equals("department")) {
                    if (departmentid == null || departmentid == 0)
                        throw new ValidationException(EMPTY_STRING, "Department is required");
                    else
                        query = query
                                + getQuery(Department.class, departmentid.longValue(), " and bd.executingDepartment=");
                } else if (value.equals("function")) {
                    if (functionid == null || functionid == 0)
                        throw new ValidationException(EMPTY_STRING, "Function is required");
                    else
                        query = query + getQuery(CFunction.class, functionid, " and bd.function=");
                } else if ("functionary".equals(value)) {
                    if (functionaryid == null || functionaryid == 0)
                        throw new ValidationException(EMPTY_STRING, "Functionary is required");
                    else
                        query = query + getQuery(Functionary.class, functionaryid, " and bd.functionary=");
                } else if (value.equals("fund")) {
                    if (fundid == null || fundid == 0)
                        throw new ValidationException(EMPTY_STRING, "Fund is required");
                    else
                        query = query + getQuery(Fund.class, fundid, " and bd.fund=");
                } else if (value.equals("scheme")) {
                    if (schemeid == null || schemeid == 0)
                        throw new ValidationException(EMPTY_STRING, "Scheme is required");
                    else
                        query = query + getQuery(Scheme.class, schemeid, " and bd.scheme=");
                } else if (value.equals("subscheme")) {
                    if (subschemeid == null || subschemeid == 0)
                        throw new ValidationException(EMPTY_STRING, "Subscheme is required");
                    else
                        query = query + getQuery(SubScheme.class, subschemeid, " and bd.subScheme=");
                } else if (value.equals("boundary")) {
                    if (boundaryid == null || boundaryid == 0)
                        throw new ValidationException(EMPTY_STRING, "Boundary is required");
                    else
                        query = query + getQuery(Boundary.class, boundaryid.longValue(), " and bd.boundary=");
                } else
                    throw new ValidationException(EMPTY_STRING,
                            "budgetaryCheck_groupby_values is not matching=" + value);
        }
        return " and bd.budget.status.description='Approved' and bd.status.description='Approved'  " + query;
    }

    private Object findById(final Class clazz, final Serializable id) {
        if (id == null)
            throw new ValidationException(EMPTY_STRING, clazz.getSimpleName() + " id is null or empty");

        final Object object = session.get(clazz, id);
        if (object == null)
            throw new ValidationException(EMPTY_STRING,
                    clazz.getSimpleName() + " is not defined for this id [ " + id.toString() + " ]");
        return object;
    }

    protected List<String> getFieldConfigValues() {
        final List<AppConfigValues> appconfigFieldlist = appConfigValuesService.getConfigValuesByModuleAndKey(EGF,
                BUDGETARY_CHECK_GROUPBY_VALUES);
        return Arrays.asList(appconfigFieldlist.get(0).getValue().split(","));
    }

    private void validateMandatoryParameters(final Integer moduleid, final String referencenumber) {
        validateMandatoryParameter(moduleid, "Module id");
        if (StringUtils.isBlank(referencenumber))
            throw new ValidationException(EMPTY_STRING, "Reference number is null or empty");
    }

    private void validateMandatoryParameter(final Object value, final String description) {
        if (value == null)
            throw new ValidationException(EMPTY_STRING, description + " is null or empty");
    }

    /**
     * To get the planning budget available amount
     * 
     * @param financialyearid
     * @param departmentid
     * @param functionid
     * @param functionaryid
     * @param schemeid
     * @param subschemeid
     * @param boundaryid
     * @param budgetheadid
     * @return @
     */
    @Override
    public BigDecimal getPlanningBudgetAvailable(final Long financialyearid, final Integer departmentid,
            final Long functionid, final Integer functionaryid, final Integer schemeid, final Integer subschemeid,
            final Integer boundaryid, final List<Long> budgetheadid, final Integer fundid) {
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("financialyearid===" + financialyearid + ",departmentid===" + departmentid
                        + ",functionid===" + functionid + ",functionaryid===" + functionaryid + ",schemeid==="
                        + schemeid + ",subschemeid===" + subschemeid + ",boundaryid===" + boundaryid
                        + ",budgetheadid===" + budgetheadid);

            validateParameters(financialyearid, functionid, budgetheadid);
            final String query = prepareQuery(departmentid, functionid, functionaryid, schemeid, subschemeid,
                    boundaryid, fundid);

            session = getCurrentSession();
            final CFinancialYear financialyear = (CFinancialYear) financialYearHibDAO.findById(financialyearid, false);

            // check any RE is available for the passed parameters.if RE is not
            // exist, take BE's available Amount
            final String finalquery = "select sum (budgetAvailable) from BudgetDetail bd where bd.budget.isbere=:type and bd.budget.financialYear.id=:financialyearid"
                    + " and bd.budgetGroup.id in (:budgetheadid) " + query;

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Final query=" + finalquery);
            final Query q = getCurrentSession().createQuery(finalquery);
            if (budgetService.hasApprovedReForYear(financialyearid))
                q.setParameter("type", "RE");
            else
                q.setParameter("type", "BE");
            q.setParameter("financialyearid", financialyearid);
            q.setParameterList("budgetheadid", budgetheadid);
            final Object obj = q.uniqueResult();
            if (obj == null)
                throw new ValidationException("no.budget.defined.for.given.parameters",
                        "No Budget is defined for the parameters for this year->" + financialyear.getFinYearRange());
            else
                return (BigDecimal) obj;
        } catch (final ValidationException v) {
            LOGGER.error("Exp in getPlanningBudgetAvailable API()===" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exception in getPlanningBudgetAvailable API()=" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
    }

    /**
     * API to load the budget consumed for the previous year/current year. it'll
     * return the transaction amount consumed for the current year based on
     * asOnDate or previous financial year and other given parameters from
     * Generalledger. Transaction amount calculation for Income and Liabilities
     * -> sum(creditamt) - sum (debitamt) Transaction amount calculation for
     * Expense and Assets -> sum(debitamt) - sum (creditamt)
     * 
     * @param functionid
     *            (optional) -id for Function object
     * @param functionaryid
     *            (optional) - id for functionary object
     * @param departmentid
     *            (optional) - id for department object
     * @param schemeid
     *            (optional) - id for scheme object
     * @param subschemeid
     *            (optional) - id for subscheme object
     * @param boundaryid
     *            (optional) - id for boundary object
     * @param budgetHead
     *            (mandatory) - budget head object, which having the major code/
     *            minor code/ detailcode/ range of minor/detail codes (based on
     *            appconfig values)
     * @param asOnDate
     *            (mandatory)-
     * @return transaction amount
     */
    // Integer departmentid, Long functionid,Integer functionaryid,Integer
    // schemeid,Integer subschemeid,Integer fieldid,Long
    // budgetheadid
    @Override
    public BigDecimal getActualBudgetUtilized(final Map<String, Object> paramMap) {
        Long deptid = null;
        Long functionid = null;
        Integer functionaryid = null;
        Integer schemeid = null;
        Integer subschemeid = null;
        Integer boundaryid = null;
        Integer fundid = null;
        Long budgetheadid = null;
        Date fromdate = null;
        Date asondate = null;

        String query = EMPTY_STRING, select = EMPTY_STRING;
        BudgetGroup budgetgroup = null;
        try {
            if (paramMap.get(Constants.DEPTID) != null)
                deptid = (Long) paramMap.get(Constants.DEPTID);
            if (paramMap.get(Constants.FUNCTIONID) != null)
                functionid = (Long) paramMap.get(Constants.FUNCTIONID);
            if (paramMap.get(Constants.FUNCTIONARYID) != null)
                functionaryid = (Integer) paramMap.get(Constants.FUNCTIONARYID);
            if (paramMap.get(Constants.SCHEMEID) != null)
                schemeid = (Integer) paramMap.get(Constants.SCHEMEID);
            if (paramMap.get(Constants.FUNDID) != null)
                fundid = (Integer) paramMap.get(Constants.FUNDID);
            if (paramMap.get(Constants.SUBSCHEMEID) != null)
                subschemeid = (Integer) paramMap.get(Constants.SUBSCHEMEID);
            if (paramMap.get(Constants.BOUNDARYID) != null)
                boundaryid = (Integer) paramMap.get(Constants.BOUNDARYID);
            if (paramMap.get(BUDGETHEADID) != null)
                budgetheadid = (Long) paramMap.get(BUDGETHEADID);
            if (paramMap.get(Constants.ASONDATE) != null)
                asondate = (java.util.Date) paramMap.get(Constants.ASONDATE);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("deptid=" + deptid + ",functionid=" + functionid + ",functionaryid=" + functionaryid
                        + ",schemeid=" + schemeid + ",subschemeid=" + subschemeid + ",boundaryid=" + boundaryid
                        + ",budgetheadid=" + budgetheadid + ",asondate=" + asondate);

            if (asondate == null)
                throw new ValidationException(EMPTY_STRING, "As On Date is null");

            final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Constants.LOCALE);

            final CFinancialYear finyear = financialYearHibDAO.getFinancialYearByDate(asondate);
            if (finyear == null)
                throw new ValidationException(EMPTY_STRING,
                        "Financial year is not fefined for this date [" + sdf.format(asondate) + "]");
            fromdate = finyear.getStartingDate();

            query = query + getQuery(CFunction.class, functionid, " and gl.functionId=");
            query = query + getQuery(Department.class, deptid, " and vmis.departmentid=");
            query = query + getQuery(Functionary.class, functionaryid, " and vmis.functionary=");
            query = query + getQuery(Scheme.class, schemeid, " and vmis.schemeid=");
            query = query + getQuery(SubScheme.class, subschemeid, " and vmis.subschemeid=");
            query = query + getQuery(Fund.class, fundid, " and vh.fundId=");
            query = query + getQuery(Boundary.class, boundaryid, " and vmis.divisionid=");

            if (budgetheadid == null || budgetheadid.equals(EMPTY_STRING))
                throw new ValidationException(EMPTY_STRING, "Budget head id is null or empty");
            budgetgroup = (BudgetGroup) budgetGroupService.findById(budgetheadid, false);
            if (budgetgroup == null || budgetgroup.getId() == null)
                throw new ValidationException(EMPTY_STRING,
                        "Budget Head is not defined for this id [ " + budgetheadid + " ]");

            final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(EGF,
                    "coa_majorcode_length");
            if (appList.isEmpty())
                throw new ValidationException(EMPTY_STRING, "coa_majorcode_length is not defined in AppConfig");
            final int majorcodelength = Integer.valueOf(appList.get(0).getValue());

            if (budgetgroup.getMinCode() != null) {
                query = query + " and substr(gl.glcode,1," + budgetgroup.getMinCode().getGlcode().length() + ")<='"
                        + budgetgroup.getMinCode().getGlcode() + "' ";
                if (budgetgroup.getMaxCode() == null)
                    query = query + " and substr(gl.glcode,1," + budgetgroup.getMinCode().getGlcode().length() + ")>='"
                            + budgetgroup.getMinCode().getGlcode() + "' ";
                else
                    query = query + " and substr(gl.glcode,1," + budgetgroup.getMinCode().getGlcode().length() + ")>='"
                            + budgetgroup.getMaxCode().getGlcode() + "' ";
            } else if (budgetgroup.getMajorCode() != null)
                query = query + " and substr(gl.glcode,1," + majorcodelength + ")='"
                        + budgetgroup.getMajorCode().getGlcode() + "'";

            if (BudgetAccountType.REVENUE_RECEIPTS.equals(budgetgroup.getAccountType())
                    || BudgetAccountType.CAPITAL_RECEIPTS.equals(budgetgroup.getAccountType()))
                select = " SELECT SUM(gl.creditAmount)-SUM(gl.debitAmount) ";
            else if (BudgetAccountType.REVENUE_EXPENDITURE.equals(budgetgroup.getAccountType())
                    || BudgetAccountType.CAPITAL_EXPENDITURE.equals(budgetgroup.getAccountType()))
                select = " SELECT SUM(gl.debitAmount)-SUM(gl.creditAmount) ";
            else
                select = " SELECT SUM(gl.debitAmount)-SUM(gl.creditAmount) ";

            final List<AppConfigValues> list = appConfigValuesService.getConfigValuesByModuleAndKey(EGF,
                    "exclude_status_forbudget_actual");
            if (list.isEmpty())
                throw new ValidationException(EMPTY_STRING,
                        "exclude_status_forbudget_actual is not defined in AppConfig");

            final String voucherstatusExclude = list.get(0).getValue();

            query = select + " FROM CGeneralLedger gl,CVoucherHeader vh,Vouchermis vmis where  "
                    + " vh.id = gl.voucherHeaderId.id AND vh.id=vmis.voucherheaderid and (vmis.budgetCheckReq is null or  vmis.budgetCheckReq=true) and vh.status not in ("
                    + voucherstatusExclude + ") and vh.voucherDate>=? and vh.voucherDate <=? " + query;

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("loadActualBudget query============" + query);
            final Object ob = persistenceService.find(query, fromdate, asondate);
            if (ob == null)
                return BigDecimal.ZERO;
            else
                return new BigDecimal(ob.toString());
        } catch (final ValidationException v) {
            LOGGER.error("Exp in getActualBudgetUtilized API()####" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exp in getActualBudgetUtilized API()===" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, "Exp in getActualBudgetUtilized API()===" + e.getMessage());
        }
    }

    /**
     * API to load the budget consumed for the previous year/current year. it'll
     * return the transaction amount consumed for the current year based on
     * asOnDate or previous financial year and other given parameters from
     * Generalledger. Transaction amount calculation for Income and Liabilities
     * -> sum(creditamt) - sum (debitamt) Transaction amount calculation for
     * Expense and Assets -> sum(debitamt) - sum (creditamt)
     * 
     * @param functionid
     *            (optional) -id for Function object
     * @param functionaryid
     *            (optional) - id for functionary object
     * @param departmentid
     *            (optional) - id for department object
     * @param schemeid
     *            (optional) - id for scheme object
     * @param subschemeid
     *            (optional) - id for subscheme object
     * @param boundaryid
     *            (optional) - id for boundary object
     * @param budgetHead
     *            (mandatory) - budget head object, which having the major code/
     *            minor code/ detailcode/ range of minor/detail codes (based on
     *            appconfig values)
     * @param asOnDate
     *            (mandatory)-
     * @return transaction amount
     */
    // Integer departmentid, Long functionid,Integer functionaryid,Integer
    // schemeid,Integer subschemeid,Integer fieldid,Long
    // budgetheadid
    @Override
    public BigDecimal getActualBudgetUtilizedForBudgetaryCheck(final Map<String, Object> paramMap) {
        Long deptid = null;
        Long functionid = null;
        Integer functionaryid = null;
        Integer schemeid = null;
        Integer subschemeid = null;
        Long boundaryid = null;
        Integer fundid = null;
        final Long budgetheadid = null;
        Date fromdate = null;
        Date asondate = null;

        String query = EMPTY_STRING, select = EMPTY_STRING;
        try {
            if (paramMap.get(Constants.DEPTID) != null)
                deptid = (Long) paramMap.get(Constants.DEPTID);
            if (paramMap.get(Constants.FUNCTIONID) != null)
                functionid = (Long) paramMap.get(Constants.FUNCTIONID);
            if (paramMap.get(Constants.FUNCTIONARYID) != null)
                functionaryid = (Integer) paramMap.get(Constants.FUNCTIONARYID);
            if (paramMap.get(Constants.SCHEMEID) != null)
                schemeid = (Integer) paramMap.get(Constants.SCHEMEID);
            if (paramMap.get(Constants.FUNDID) != null)
                fundid = (Integer) paramMap.get(Constants.FUNDID);
            if (paramMap.get(Constants.SUBSCHEMEID) != null)
                subschemeid = (Integer) paramMap.get(Constants.SUBSCHEMEID);
            if (paramMap.get(Constants.BOUNDARYID) != null)
                boundaryid = (Long) paramMap.get(Constants.BOUNDARYID);
            /*
             * if(paramMap.get(BUDGETHEADID)!=null) budgetheadid =
             * (Long)paramMap.get(BUDGETHEADID);
             */
            if (paramMap.get(Constants.ASONDATE) != null)
                asondate = (java.util.Date) paramMap.get(Constants.ASONDATE);

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("deptid=" + deptid + ",functionid=" + functionid + ",functionaryid=" + functionaryid
                        + ",schemeid=" + schemeid + ",subschemeid=" + subschemeid + ",boundaryid=" + boundaryid
                        + ",budgetheadid=" + budgetheadid + ",asondate=" + asondate);

            if (asondate == null)
                throw new ValidationException(EMPTY_STRING, "As On Date is null");

            final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Constants.LOCALE);

            final CFinancialYear finyear = financialYearHibDAO.getFinancialYearByDate(asondate);
            if (finyear == null)
                throw new ValidationException(EMPTY_STRING,
                        "Financial year is not fefined for this date [" + sdf.format(asondate) + "]");
            fromdate = finyear.getStartingDate();

            final List<AppConfigValues> budgetGrouplist = appConfigValuesService.getConfigValuesByModuleAndKey(EGF,
                    BUDGETARY_CHECK_GROUPBY_VALUES);
            if (budgetGrouplist.isEmpty())
                throw new ValidationException(EMPTY_STRING,
                        "budgetaryCheck_groupby_values is not defined in AppConfig");
            else {
                final AppConfigValues appConfigValues = budgetGrouplist.get(0);
                final String[] values = StringUtils.split(appConfigValues.getValue(), ",");
                for (final String value : values)
                    if (value.equals("department"))
                        query = query + getQuery(Department.class, deptid, " and vmis.departmentid=");
                    else if (value.equals("function"))
                        query = query + getQuery(CFunction.class, functionid, " and gl.functionId=");
                    else if (value.equals("functionary"))
                        query = query + getQuery(Functionary.class, functionaryid, " and vmis.functionary=");
                    else if (value.equals("fund"))
                        query = query + getQuery(Fund.class, fundid, " and vh.fundId=");
                    else if (value.equals("scheme"))
                        query = query + getQuery(Scheme.class, schemeid, " and vmis.schemeid=");
                    else if (value.equals("subscheme"))
                        query = query + getQuery(SubScheme.class, subschemeid, " and vmis.subschemeid=");
                    else if (value.equals("boundary"))
                        query = query + getQuery(Boundary.class, boundaryid, " and vmis.divisionid=");
                    else
                        throw new ValidationException(EMPTY_STRING,
                                "budgetaryCheck_groupby_values is not matching=" + value);
            }

            String glcode = EMPTY_STRING;
            if (paramMap.get("glcode") != null)
                glcode = paramMap.get("glcode").toString();
            if (EMPTY_STRING.equals(glcode))
                throw new ValidationException(EMPTY_STRING, "Glcode is null");

            query = query + " and gl.glcode='" + glcode + "'";

            final CChartOfAccounts coa = chartOfAccountsHibernateDAO.getCChartOfAccountsByGlCode(glcode);
            if (coa == null)
                throw new ValidationException(EMPTY_STRING, "Chartofaccounts is null for this glcode:" + glcode);

            if ("I".equalsIgnoreCase(coa.getType().toString()) || "L".equalsIgnoreCase(coa.getType().toString()))
                select = " SELECT SUM(gl.creditAmount)-SUM(gl.debitAmount) ";
            else
                select = " SELECT SUM(gl.debitAmount)-SUM(gl.creditAmount) ";

            final List<AppConfigValues> list = appConfigValuesService.getConfigValuesByModuleAndKey(EGF,
                    "exclude_status_forbudget_actual");
            if (list.isEmpty())
                throw new ValidationException(EMPTY_STRING,
                        "exclude_status_forbudget_actual is not defined in AppConfig");

            list.get(0).getValue();

            query = select + " FROM CGeneralLedger gl,CVoucherHeader vh,Vouchermis vmis where  "
                    + " vh.id = gl.voucherHeaderId.id AND vh.id=vmis.voucherheaderid and (vmis.budgetCheckReq=null or vmis.budgetCheckReq=true) and vh.status !=4 and vh.voucherDate>=? and vh.voucherDate <=? "
                    + query;

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("loadActualBudget query============" + query);
            final Object ob = persistenceService.find(query, fromdate, asondate);
            if (ob == null)
                return BigDecimal.ZERO;
            else
                return new BigDecimal(ob.toString());
        } catch (final ValidationException v) {
            LOGGER.error("Exp in getActualBudgetUtilizedForBudgetaryCheck API()===" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exp in getActualBudgetUtilizedForBudgetaryCheck API()===" + e.getMessage());
            throw new ValidationException(EMPTY_STRING,
                    "Exp in getActualBudgetUtilizedForBudgetaryCheck API()===" + e.getMessage());
        }
    }

    @Override
    public BigDecimal getPlanningBudgetUsage(final BudgetDetail bd) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Budget Detail id passed=" + bd.getId());

        // BudgetDetail bd=(BudgetDetail)findById(BudgetDetail.class,
        // budgetdtlid);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Budget Detail =" + bd.getUniqueNo() + " budget= " + bd.getBudget().getId() + " FinYear="
                    + bd.getBudget().getFinancialYear().getId());

        final List<Long> budgetDetailIds = persistenceService.findAllBy(
                "select id from BudgetDetail bd where uniqueNo=? and bd.budget.financialYear.id=? and bd.status.code='Approved' ",
                bd.getUniqueNo(), bd.getBudget().getFinancialYear().getId());

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("ids returned if be then 1 id should return else 2 ids should return =" + budgetDetailIds);
        if (budgetDetailIds == null || budgetDetailIds.size() == 0)
            return BigDecimal.ZERO;
        else {
            final Query sumQuery = getCurrentSession().createQuery(
                    "select sum(consumedAmount)-sum(releasedAmount) from BudgetUsage WHERE budgetDetail.id  in ( :IDS )");
            sumQuery.setParameterList("IDS", budgetDetailIds);
            final Double planningbudgetusage = (Double) sumQuery.list().get(0);

            if (planningbudgetusage == null) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("NO Consumed Amount");
                return BigDecimal.ZERO;
            } else {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Consumed Amount =" + BigDecimal.valueOf(planningbudgetusage));
                return BigDecimal.valueOf(planningbudgetusage);
            }
        }

    }

    /**
     * This parameter HashMap contains deptid,functionid,
     * functionaryid,schemeid,
     * subschemeid,boundaryid,budgetheadid,financialyearid it'll get the
     * budgeted amount based on the parameters.
     * 
     * @param paramMap
     * @return budgeted amount @
     */
    @Override
    public BigDecimal getBudgetedAmtForYear(final Map<String, Object> paramMap) {
        Long deptid = null;
        Long functionid = null;
        Integer functionaryid = null;
        Integer schemeid = null;
        Integer subschemeid = null;
        Long boundaryid = null;
        Integer fundid = null;
        List<BudgetGroup> budgetHeadList = null;
        Long financialyearid = null;

        String query = EMPTY_STRING;
        try {
            if (paramMap.get(Constants.DEPTID) != null)
                deptid = (Long) paramMap.get(Constants.DEPTID);
            if (paramMap.get(Constants.FUNCTIONID) != null)
                functionid = (Long) paramMap.get(Constants.FUNCTIONID);
            if (paramMap.get(Constants.FUNCTIONARYID) != null)
                functionaryid = (Integer) paramMap.get(Constants.FUNCTIONARYID);
            if (paramMap.get(Constants.SCHEMEID) != null)
                schemeid = (Integer) paramMap.get(Constants.SCHEMEID);
            if (paramMap.get(Constants.SUBSCHEMEID) != null)
                subschemeid = (Integer) paramMap.get(Constants.SUBSCHEMEID);
            if (paramMap.get(Constants.FUNDID) != null)
                fundid = (Integer) paramMap.get(Constants.FUNDID);
            if (paramMap.get(Constants.BOUNDARYID) != null)
                boundaryid = (Long) paramMap.get(Constants.BOUNDARYID);
            if (paramMap.get(BUDGETHEADID) != null)
                budgetHeadList = (List) paramMap.get(BUDGETHEADID);
            if (paramMap.get("financialyearid") != null)
                financialyearid = (Long) paramMap.get("financialyearid");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("deptid " + deptid + ",functionid " + functionid + ",functionaryid " + functionaryid
                        + ",schemeid " + schemeid + ",subschemeid " + subschemeid + ",boundaryid " + boundaryid
                        + ",budgetheadids " + budgetHeadList + ",financialyearid " + financialyearid);

            query = prepareQuery(deptid.intValue(), functionid, functionaryid, schemeid, subschemeid,
                    boundaryid != null ? boundaryid.intValue() : null, fundid);

            // handle the list

            if (financialyearid == null)
                throw new ValidationException(EMPTY_STRING, "Financial Year id is null");
            query = query + getQuery(CFinancialYear.class, financialyearid, " and bd.budget.financialYear=");
            if (budgetHeadList == null || budgetHeadList.size() == 0)
                throw new ValidationException(EMPTY_STRING, "Budget head id is null or empty");
            query = query + " and bd.budgetGroup in ( :budgetHeadList )";

            // check any RE is available for the passed parameters.if RE is not
            // exist, take BE's Approved Amount
            String finalquery;
            if (budgetService.hasApprovedReForYear(financialyearid))
                finalquery = " from BudgetDetail bd where bd.budget.isbere='RE' " + query;
            else
                finalquery = " from BudgetDetail bd where bd.budget.isbere='BE' " + query;

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Final query=" + finalquery);
            // Query hibQuery =getCurrentSession().createQuery(finalquery);
            final Query hibQuery = getCurrentSession().createQuery(finalquery);

            hibQuery.setParameterList("budgetHeadList", budgetHeadList);
            final List<BudgetDetail> bdList = hibQuery.list();
            if (bdList == null || bdList.size() == 0)
                // return BigDecimal.ZERO;
                throw new ValidationException(
                        new ValidationError("Budget Check failed: Budget not defined for the given combination.",
                                "Budget Check failed: Budget not defined for the given combination."));
            else
                return getApprovedAmt(bdList);
        } catch (final ValidationException v) {
            LOGGER.error("Exp in getBudgetedAmtForYear==" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exp in getBudgetedAmtForYear==" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, "Exp in getBudgetedAmtForYear==" + e.getMessage());
        }
    }

    /**
     * This parameter HashMap contains deptid,functionid,
     * functionaryid,schemeid,
     * subschemeid,boundaryid,budgetheadid,financialyearid it'll get the
     * budgeted amount based on the parameters.
     * 
     * @param paramMap
     * @return budgeted amount @
     */
    @Override
    public BigDecimal getBudgetedAmtForYearAsOnDate(final Map<String, Object> paramMap, final Date asOnDate) {
        Integer deptid = null;
        Long functionid = null;
        Integer functionaryid = null;
        Integer schemeid = null;
        Integer subschemeid = null;
        Integer boundaryid = null;
        Integer fundid = null;
        List<BudgetGroup> budgetHeadList = null;
        Long financialyearid = null;

        String query = EMPTY_STRING;
        try {
            if (paramMap.get(Constants.DEPTID) != null)
                deptid = (Integer) paramMap.get(Constants.DEPTID);
            if (paramMap.get(Constants.FUNCTIONID) != null)
                functionid = (Long) paramMap.get(Constants.FUNCTIONID);
            if (paramMap.get(Constants.FUNCTIONARYID) != null)
                functionaryid = (Integer) paramMap.get(Constants.FUNCTIONARYID);
            if (paramMap.get(Constants.SCHEMEID) != null)
                schemeid = (Integer) paramMap.get(Constants.SCHEMEID);
            if (paramMap.get(Constants.SUBSCHEMEID) != null)
                subschemeid = (Integer) paramMap.get(Constants.SUBSCHEMEID);
            if (paramMap.get(Constants.FUNDID) != null)
                fundid = (Integer) paramMap.get(Constants.FUNDID);
            if (paramMap.get(Constants.BOUNDARYID) != null)
                boundaryid = (Integer) paramMap.get(Constants.BOUNDARYID);
            if (paramMap.get(BUDGETHEADID) != null)
                budgetHeadList = (List) paramMap.get(BUDGETHEADID);
            if (paramMap.get("financialyearid") != null)
                financialyearid = (Long) paramMap.get("financialyearid");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("deptid " + deptid + ",functionid " + functionid + ",functionaryid " + functionaryid
                        + ",schemeid " + schemeid + ",subschemeid " + subschemeid + ",boundaryid " + boundaryid
                        + ",budgetheadids " + budgetHeadList + ",financialyearid " + financialyearid);

            query = prepareQuery(deptid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);

            // handle the list

            if (financialyearid == null)
                throw new ValidationException(EMPTY_STRING, "Financial Year id is null");
            query = query + getQuery(CFinancialYear.class, financialyearid, " and bd.budget.financialYear=");
            if (budgetHeadList == null || budgetHeadList.size() == 0)
                throw new ValidationException(EMPTY_STRING, "Budget head id is null or empty");
            query = query + " and bd.budgetGroup in ( :budgetHeadList )";

            // check any RE is available for the passed parameters.if RE is not
            // exist, take BE's Approved Amount
            String finalquery;
            if (budgetService.hasApprovedReForYear(financialyearid))
                finalquery = " from BudgetDetail bd where bd.budget.isbere='RE' " + query;
            else
                finalquery = " from BudgetDetail bd where bd.budget.isbere='BE' " + query;

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Final query=" + finalquery);
            // Query hibQuery =getCurrentSession().createQuery(finalquery);
            final Query hibQuery = getCurrentSession().createQuery(finalquery);

            hibQuery.setParameterList("budgetHeadList", budgetHeadList);
            final List<BudgetDetail> bdList = hibQuery.list();
            if (bdList == null || bdList.size() == 0)
                return BigDecimal.ZERO;
            else
                return getApprovedAmtAsOnDate(bdList, asOnDate);
        } catch (final ValidationException v) {
            LOGGER.error("Exp in getBudgetedAmtForYear==" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exp in getBudgetedAmtForYear==" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, "Exp in getBudgetedAmtForYear==" + e.getMessage());
        }
    }

    @Override
    public BigDecimal getPlanningPercentForYear(final Map<String, Object> paramMap) {
        Integer deptid = null;
        Long functionid = null;
        Integer functionaryid = null;
        Integer schemeid = null;
        Integer subschemeid = null;
        Integer boundaryid = null;
        Integer fundid = null;
        List<BudgetGroup> budgetHeadList = null;
        Long financialyearid = null;

        String query = EMPTY_STRING;
        try {
            if (paramMap.get(Constants.DEPTID) != null)
                deptid = (Integer) paramMap.get(Constants.DEPTID);
            if (paramMap.get(Constants.FUNCTIONID) != null)
                functionid = (Long) paramMap.get(Constants.FUNCTIONID);
            if (paramMap.get(Constants.FUNCTIONARYID) != null)
                functionaryid = (Integer) paramMap.get(Constants.FUNCTIONARYID);
            if (paramMap.get(Constants.SCHEMEID) != null)
                schemeid = (Integer) paramMap.get(Constants.SCHEMEID);
            if (paramMap.get(Constants.SUBSCHEMEID) != null)
                subschemeid = (Integer) paramMap.get(Constants.SUBSCHEMEID);
            if (paramMap.get(Constants.FUNDID) != null)
                fundid = (Integer) paramMap.get(Constants.FUNDID);
            if (paramMap.get(Constants.BOUNDARYID) != null)
                boundaryid = (Integer) paramMap.get(Constants.BOUNDARYID);
            if (paramMap.get(BUDGETHEADID) != null)
                budgetHeadList = (List) paramMap.get(BUDGETHEADID);
            if (paramMap.get("financialyearid") != null)
                financialyearid = (Long) paramMap.get("financialyearid");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("deptid " + deptid + ",functionid " + functionid + ",functionaryid " + functionaryid
                        + ",schemeid " + schemeid + ",subschemeid " + subschemeid + ",boundaryid " + boundaryid
                        + ",budgetheadids " + budgetHeadList + ",financialyearid " + financialyearid);

            query = prepareQuery(deptid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);

            // handle the list

            if (financialyearid == null)
                throw new ValidationException(EMPTY_STRING, "Financial Year id is null");
            query = query + getQuery(CFinancialYear.class, financialyearid, " and bd.budget.financialYear=");
            if (budgetHeadList == null || budgetHeadList.size() == 0)
                throw new ValidationException(EMPTY_STRING, "Budget head id is null or empty");
            query = query + " and bd.budgetGroup in ( :budgetHeadList )";

            // check any RE is available for the passed parameters.if RE is not
            // exist, take BE's Approved Amount
            String finalquery;
            if (budgetService.hasApprovedReForYear(financialyearid))
                finalquery = " from BudgetDetail bd where bd.budget.isbere='RE' " + query;
            else
                finalquery = " from BudgetDetail bd where bd.budget.isbere='BE' " + query;

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Final query=" + finalquery);
            // Query hibQuery =getCurrentSession().createQuery(finalquery);
            final Query hibQuery = getCurrentSession().createQuery(finalquery);

            hibQuery.setParameterList("budgetHeadList", budgetHeadList);
            final List<BudgetDetail> bdList = hibQuery.list();
            if (bdList == null || bdList.size() == 0)
                return BigDecimal.ZERO;
            else if (bdList.size() > 1) {
                LOGGER.error("returned multiple rows");
                return BigDecimal.ZERO;
            } else
                return bdList.get(0).getPlanningPercent();
        } catch (final ValidationException v) {
            LOGGER.error("Exp in getBudgetedAmtForYear==" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exp in getBudgetedAmtForYear==" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, "Exp in getBudgetedAmtForYear==" + e.getMessage());
        }
    }

    /**
     * This parameter HashMap contains deptid,functionid,
     * functionaryid,schemeid,
     * subschemeid,boundaryid,budgetheadid,financialyearid it'll get the
     * budgeted amount based on the parameters. Only financial year parameter
     * will be considered mandatory here.
     * 
     * @param paramMap
     * @return budgeted amount @
     */
    @Override
    public Map<String, BigDecimal> getAggregateBudgetedAmtForYear(final Map<String, Object> paramMap) {
        Integer deptid = null;
        Long functionid = null;
        Integer functionaryid = null;
        Integer schemeid = null;
        Integer subschemeid = null;
        Integer boundaryid = null;
        Integer fundid = null;
        List<Long> budgetHeadList = null;
        Long financialyearid = null;
        final Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>();
        String query = EMPTY_STRING;
        try {
            if (paramMap.get(Constants.DEPTID) != null)
                deptid = (Integer) paramMap.get(Constants.DEPTID);
            if (paramMap.get(Constants.FUNCTIONID) != null)
                functionid = (Long) paramMap.get(Constants.FUNCTIONID);
            if (paramMap.get(Constants.FUNCTIONARYID) != null)
                functionaryid = (Integer) paramMap.get(Constants.FUNCTIONARYID);
            if (paramMap.get(Constants.SCHEMEID) != null)
                schemeid = (Integer) paramMap.get(Constants.SCHEMEID);
            if (paramMap.get(Constants.SUBSCHEMEID) != null)
                subschemeid = (Integer) paramMap.get(Constants.SUBSCHEMEID);
            if (paramMap.get(Constants.FUNDID) != null)
                fundid = (Integer) paramMap.get(Constants.FUNDID);
            if (paramMap.get(Constants.BOUNDARYID) != null)
                boundaryid = (Integer) paramMap.get(Constants.BOUNDARYID);
            if (paramMap.get(BUDGETHEADID) != null)
                budgetHeadList = (List) paramMap.get(BUDGETHEADID);
            if (paramMap.get("financialyearid") != null)
                financialyearid = (Long) paramMap.get("financialyearid");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Inside getAggregateBudgetedAmtForYear---> deptid " + deptid + ",functionid " + functionid
                        + ",functionaryid " + functionaryid + ",schemeid " + schemeid + ",subschemeid " + subschemeid
                        + ",boundaryid " + boundaryid + ",budgetheadids " + budgetHeadList + ",financialyearid "
                        + financialyearid);
            query = prepareAgregateQuery(deptid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);

            if (financialyearid == null)
                throw new ValidationException(EMPTY_STRING, "Financial Year id is null");
            query = query + getQuery(CFinancialYear.class, financialyearid, " and bd.budget.financialYear=");
            // if(budgetHeadList==null || budgetHeadList.size()==0)
            // throw new
            // ValidationException(EMPTY_STRING,"Budget head id is null or
            // empty");
            if (budgetHeadList != null && budgetHeadList.size() != 0)
                query = query + " and bd.budgetGroup.id in ( :budgetHeadList )";

            // check any RE is available for the passed parameters.if RE is not
            // exist, take BE's Approved Amount
            String finalquery;
            if (budgetService.hasApprovedReForYear(financialyearid))
                finalquery = " from BudgetDetail bd where bd.budget.isbere='RE' " + query
                        + " order by bd.executingDepartment ";
            else
                finalquery = " from BudgetDetail bd where bd.budget.isbere='BE' " + query
                        + " order by bd.executingDepartment ";

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Final query=" + finalquery);
            final Query hibQuery = getCurrentSession().createQuery(finalquery);
            if (budgetHeadList != null && budgetHeadList.size() != 0)
                hibQuery.setParameterList("budgetHeadList", budgetHeadList);
            final List<BudgetDetail> bdList = hibQuery.list();
            if (bdList == null || bdList.size() == 0)
                return retMap;
            else
                return getApprovedAmtDeptwise(bdList);
        } catch (final ValidationException v) {
            LOGGER.error("Exp in getAggregateBudgetedAmtForYear ==" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exp in getAggregateBudgetedAmtForYear ...==" + e.getMessage());
            throw new ValidationException(EMPTY_STRING,
                    "Exception in getAggregateBudgetedAmtForYear==" + e.getMessage());
        }
    }

    private BigDecimal getApprovedAmt(final List<BudgetDetail> bdList) {
        BigDecimal approvedAmt = BigDecimal.ZERO;
        for (final BudgetDetail bd : bdList) {
            if (bd.getApprovedAmount() != null)
                approvedAmt = approvedAmt.add(bd.getApprovedAmount());
            approvedAmt = approvedAmt.add(bd.getApprovedReAppropriationsTotal());
        }
        return approvedAmt;
    }

    private BigDecimal getApprovedAmtAsOnDate(final List<BudgetDetail> bdList, final Date asOnDate) {
        BigDecimal approvedAmt = BigDecimal.ZERO;
        for (final BudgetDetail bd : bdList) {
            if (bd.getApprovedAmount() != null)
                approvedAmt = approvedAmt.add(bd.getApprovedAmount());
            approvedAmt = approvedAmt.add(bd.getApprovedReAppropriationsTotalAsOnDate(asOnDate));
        }
        return approvedAmt;
    }

    private Map<String, BigDecimal> getApprovedAmtDeptwise(final List<BudgetDetail> bdList) {
        BigDecimal approvedAmt = BigDecimal.ZERO;
        String deptName = null;
        final Map<String, BigDecimal> deptBudget = new HashMap<String, BigDecimal>();

        for (final BudgetDetail bd : bdList) {
            approvedAmt = BigDecimal.ZERO;
            deptName = bd.getExecutingDepartment().getName();

            if (bd.getApprovedAmount() != null)
                approvedAmt = bd.getApprovedAmount();
            approvedAmt = approvedAmt.add(bd.getApprovedReAppropriationsTotal());
            if (null != deptBudget && deptBudget.containsKey(deptName)) {
                approvedAmt = deptBudget.get(deptName).add(approvedAmt);
                deptBudget.put(deptName, approvedAmt);
            } else
                deptBudget.put(deptName, approvedAmt);

        }
        return deptBudget;
    }

    /**
     * This API is handling the budget checking
     * 
     * @param paramMap
     *            paramMap contains 1. debitAmt (mandatory) 2. creditAmt
     *            (mandatory) 3. deptid (optional) 4. functionid (optional) 5.
     *            functionaryid (optional) 6. schemeid (optional) 7. subschemeid
     *            (optional) 8. boundaryid (optional) 9. glcode (mandatory) -
     *            based on the glcode, we can get the budgetheadid 10. asondate
     *            (manadtory) - to get the actuals, we need asondate 11.
     *            mis.budgetcheckreq-Boolean- (optional) to skip budget check if
     *            set to false.Default is true Budget checking will be enabled
     *            or disabled by these levels and in the order a. Application -
     *            uses set up "Budget Control Type" b. Voucherlevel - uses
     *            budgetcheckreq column of vouchermis table for perticular
     *            voucher c. Debit or Credit level - uses Budgetgroup.budgetting
     *            type for debit side only ,credit side or both d. Glcode level
     *            - Uses chartofaccounts.budgetcheckreq fieled to decide budget
     *            checking .
     * @return @
     */
    @Override
    public boolean budgetaryCheck(final Map<String, Object> paramMap) {
        String cashbasedbudgetType = EMPTY_STRING, txnType = EMPTY_STRING;
        BigDecimal debitAmt = null;
        BigDecimal creditAmt = null;
        BigDecimal txnAmt = null;

        try {

            String budgetCheckConfig = budgetCheckConfigService.getConfigValue();

            if (budgetCheckConfig.equals("NONE"))
                return true;

            if (paramMap.get("mis.budgetcheckreq") != null
                    && ((Boolean) paramMap.get("mis.budgetcheckreq")).equals(false))
                return true;

            if (paramMap.get("debitAmt") != null)
                debitAmt = (BigDecimal) paramMap.get("debitAmt");
            if (paramMap.get("creditAmt") != null)
                creditAmt = (BigDecimal) paramMap.get("creditAmt");

            if (debitAmt == null && creditAmt == null)
                throw new ValidationException(EMPTY_STRING, "Both Debit and Credit amount is null");

            if (debitAmt != null && debitAmt.compareTo(BigDecimal.ZERO) == 0 && creditAmt != null
                    && creditAmt.compareTo(BigDecimal.ZERO) == 0)
                throw new ValidationException(EMPTY_STRING, "Both Debit and Credit amount is zero");

            if (debitAmt != null && debitAmt.compareTo(BigDecimal.ZERO) > 0 && creditAmt != null
                    && creditAmt.compareTo(BigDecimal.ZERO) > 0)
                throw new ValidationException(EMPTY_STRING, "Both Debit and Credit amount is greater than zero");

            // get the type of budget from appconfig .
            List<AppConfigValues> list;
            list = appConfigValuesService.getConfigValuesByModuleAndKey(EGF, "budgetaryCheck_budgettype_cashbased");
            if (list.isEmpty())
                throw new ValidationException(EMPTY_STRING,
                        "budgetaryCheck_budgettype_cashbased is not defined in AppConfig");

            cashbasedbudgetType = list.get(0).getValue();
            if (cashbasedbudgetType.equalsIgnoreCase("Y")) // cash based budget
            {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("cashbasedbudgetType==" + cashbasedbudgetType);
            } else // Accural based budgeting
            {
                if (debitAmt != null && debitAmt.compareTo(BigDecimal.ZERO) > 0) {
                    txnType = "debit";
                    txnAmt = debitAmt;
                } else {
                    txnType = "credit";
                    txnAmt = creditAmt;
                }
                paramMap.put("txnAmt", txnAmt);
                paramMap.put("txnType", txnType);
                return checkCondition(paramMap);
            }
        } catch (final ValidationException v) {
            throw v;
        } catch (final Exception e) {
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
        return true;
    }

    /**
     * To check budget available for the glcode with other parameters. if txnamt
     * is less than the budget available, it would return true, otherwise false.
     * 
     * @param paramMap
     * @return @
     */
    private boolean checkCondition(final Map<String, Object> paramMap) {
        String txnType = null;
        String glCode = null;
        BigDecimal txnAmt = null;
        java.util.Date asondate = null;
        java.util.Date fromdate = null;

        try {
            if (paramMap.get("txnAmt") != null)
                txnAmt = (BigDecimal) paramMap.get("txnAmt");
            if (paramMap.get("txnType") != null)
                txnType = paramMap.get("txnType").toString();
            if (paramMap.get("glcode") != null)
                glCode = paramMap.get("glcode").toString();
            if (paramMap.get(Constants.ASONDATE) != null)
                asondate = (Date) paramMap.get(Constants.ASONDATE);

            if (glCode == null)
                throw new ValidationException(EMPTY_STRING, "glcode is null");
            if (txnAmt == null)
                throw new ValidationException(EMPTY_STRING, "txnAmt is null");
            if (txnType == null)
                throw new ValidationException(EMPTY_STRING, "txnType is null");
            if (asondate == null)
                throw new ValidationException(EMPTY_STRING, "As On Date is null");

            // check the account code needs budget checking

            final CChartOfAccounts coa = chartOfAccountsHibernateDAO.getCChartOfAccountsByGlCode(glCode);
            if (coa.getBudgetCheckReq() != null && coa.getBudgetCheckReq()) {
                // get budgethead for the glcode
                // BudgetGroup bg = getBudgetHeadByGlcode(coa,paramMap);
                final List<BudgetGroup> budgetHeadListByGlcode = getBudgetHeadByGlcode(coa);
                if (budgetHeadListByGlcode == null || budgetHeadListByGlcode.size() == 0)
                    throw new ValidationException(EMPTY_STRING,
                            "Budget Check failed: Budget not defined for the given combination.");
                // get budgettinh type for first BG object
                if (!isBudgetCheckingRequiredForType(txnType,
                        budgetHeadListByGlcode.get(0).getBudgetingType().toString())) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(
                                "No need to check budget for :" + glCode + " as the transaction type is " + txnType);
                    return true;
                }

                paramMap.put("glcodeid", coa.getId());
                // get the financialyear from asondate

                final CFinancialYear finyear = financialYearHibDAO.getFinancialYearByDate(asondate);
                if (finyear == null)
                    throw new ValidationException(EMPTY_STRING, "Financial Year is not defined for-" + asondate);
                new SimpleDateFormat("dd-MMM-yyyy", Constants.LOCALE);
                fromdate = finyear.getStartingDate();

                paramMap.put("fromdate", fromdate);
                // Here as on date is overridden by Financialyear ending date to
                // check all budget appropriation irrespective of
                // date
                paramMap.put(Constants.ASONDATE, finyear.getEndingDate());
                paramMap.put("financialyearid", Long.valueOf(finyear.getId()));

                paramMap.put(BUDGETHEADID, budgetHeadListByGlcode);

                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("************ BudgetCheck Details *********************");
                // pass the list of bg to getBudgetedAmtForYear
                final BigDecimal budgetedAmt = getBudgetedAmtForYear(paramMap); // get
                // the
                // budgetedamount
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(".............Budgeted Amount For the year............" + budgetedAmt);

                if (budgetCheckConfigService.getConfigValue()
                        .equalsIgnoreCase(BudgetControlType.BudgetCheckOption.MANDATORY.toString()))
                    if (budgetedAmt.compareTo(BigDecimal.ZERO) == 0)
                        return false;

                final BigDecimal actualAmt = getActualBudgetUtilizedForBudgetaryCheck(paramMap); // get
                // actual
                // amount
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(".............Voucher Actual amount............" + actualAmt);

                BigDecimal billAmt = getBillAmountForBudgetCheck(paramMap); // get
                // actual
                // amount
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(".............Bill Actual amount............" + billAmt);
                EgBillregister bill = null;

                if (paramMap.get("bill") != null)
                    bill = (EgBillregister) persistenceService.find("from EgBillregister where id=? ",
                            (Long) paramMap.get("bill"));
                if (bill != null && bill.getEgBillregistermis().getBudgetaryAppnumber() != null) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(
                                ".............Found BillId so subtracting txn amount......................" + txnAmt);
                    billAmt = billAmt.subtract(txnAmt);
                }
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(".......Recalculated Bill Actual amount............" + billAmt);
                final BigDecimal diff = budgetedAmt.subtract(actualAmt).subtract(billAmt); // get
                // bill
                // amt
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(".................diff amount..........................." + diff);

                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("************ BudgetCheck Details End****************");
                // BigDecimal diff = budgetedAmt.subtract(actualAmt);

                if (budgetCheckConfigService.getConfigValue()
                        .equalsIgnoreCase(BudgetControlType.BudgetCheckOption.MANDATORY.toString())) {
                    if (txnAmt.compareTo(diff) <= 0) {

                        generateBanNumber(paramMap, bill);
                        return true;
                    }

                    else
                        return false;
                }
                if (budgetCheckConfigService.getConfigValue()
                        .equalsIgnoreCase(BudgetControlType.BudgetCheckOption.ANTICIPATORY.toString())) {
                    generateBanNumber(paramMap, bill);
                    return true;
                }
            } else // no budget check for coa
                return true;
        } catch (final ValidationException v) {
            throw v;
        } catch (final Exception e) {
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
        return true;
    }

    private void generateBanNumber(final Map<String, Object> paramMap, EgBillregister bill) {
        if (bill == null || bill.getEgBillregistermis().getBudgetaryAppnumber() == null)
            if (paramMap.get("voucherHeader") != null && ((CVoucherHeader) paramMap.get("voucherHeader"))
                    .getVouchermis().getBudgetaryAppnumber() == null) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Bill level budget app no not generated so generating voucher level");
                if (bill != null) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("bill Number..........." + bill.getBillnumber());
                } else if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Bill not present");
                ((CVoucherHeader) paramMap.get("voucherHeader")).getVouchermis()
                        .setBudgetaryAppnumber(getBudgetApprNumber(paramMap));
            }
    }

    /**
     * @param paramMap
     * @return
     */
    public String getBudgetApprNumber(final Map<String, Object> paramMap) {

        BanNumberGenerator b = (BanNumberGenerator) beanResolver.getAutoNumberServiceFor(BanNumberGenerator.class);
        final String budgetApprNumber = b.getNextNumber();
        return budgetApprNumber;
    }

    /**
     * to check the budget checking is required or not
     * 
     * @param txnType
     * @param budgetingType
     * @return
     */
    private boolean isBudgetCheckingRequiredForType(final String txnType, final String budgetingType) {
        if ("debit".equalsIgnoreCase(budgetingType) && "debit".equals(txnType))
            return true;
        else if ("credit".equalsIgnoreCase(budgetingType) && "credit".equals(txnType))
            return true;
        else if ("all".equalsIgnoreCase(budgetingType))
            return true;
        else
            return false;
    }

    /**
     * To get the Budgetgroup for the glcode at detailcode level or minorcode
     * level or major code level.
     * 
     * @param coa
     * @param paramMap
     * @return @
     */

    // return list of budget group
    // try to make protected
    @Override
    public List<BudgetGroup> getBudgetHeadByGlcode(final CChartOfAccounts coa) {
        try {
            List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(EGF,
                    "coa_majorcode_length");
            if (appList.isEmpty())
                throw new ValidationException(EMPTY_STRING, "coa_majorcode_length is not defined");
            final int majorcodelength = Integer.valueOf(appList.get(0).getValue());

            appList = appConfigValuesService.getConfigValuesByModuleAndKey(EGF, "coa_minorcode_length");
            if (appList.isEmpty())
                throw new ValidationException(EMPTY_STRING, "coa_minorcode_length is not defined");
            final int minorcodelength = Integer.valueOf(appList.get(0).getValue());

            // check the budget group is defined at detailcode level or
            // detailcode within the range
            String query = " from BudgetGroup bg where bg.minCode.glcode<='" + coa.getGlcode()
                    + "' and bg.maxCode.glcode>='" + coa.getGlcode()
                    + "'  and bg in (select budgetGroup from BudgetDetail) and bg.isActive=true";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getBudgetHeadByGlcode detailcode query=====" + query);
            List bgList = persistenceService.findAllBy(query);
            if (bgList.isEmpty()) {
                query = " from BudgetGroup bg where bg.minCode.glcode<='"
                        + coa.getGlcode().substring(0, minorcodelength) + "' and bg.maxCode.glcode>='"
                        + coa.getGlcode().substring(0, minorcodelength)
                        + "' and bg in (select budgetGroup from BudgetDetail) and bg.isActive=true";
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("getBudgetHeadByGlcode minorcode query=====" + query);
                // persistenceService.setType(BudgetGroup.class);
                bgList = persistenceService.findAllBy(query);
                if (bgList.isEmpty()) {
                    query = " from BudgetGroup bg where bg.majorCode.glcode='"
                            + coa.getGlcode().substring(0, majorcodelength)
                            + "' and bg in (select budgetGroup from BudgetDetail) and bg.isActive=true ";
                    // persistenceService.setType(BudgetGroup.class);
                    bgList = persistenceService.findAllBy(query);
                    if (bgList.isEmpty())
                        throw new ValidationException(EMPTY_STRING,
                                "Budget Check failed: Budget not defined for the given combination.");
                    else
                        return bgList;
                } else
                    return bgList;
            } else
                return bgList;
        } catch (final ValidationException v) {
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", v.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            LOGGER.error("Exception in getBudgetHeadByGlcode API=" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
    }

    /**
     * To get the Budgetgroup for the glcode at detailcode level for a list of
     * COA codes
     * 
     * @param coa
     * @param paramMap
     * @return @
     */
    @Override
    public List<BudgetGroup> getBudgetHeadForGlcodeList(final List<CChartOfAccounts> coa) {
        try {
            String coaQry = "bg.minCode.glcode in(";

            if (coa.isEmpty()) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("No COA is been passed");
                throw new ValidationException(EMPTY_STRING,
                        "No Chartofaccount code is been passed for getting the budget heads");
            } else {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("COA list size passed " + coa.size());
                for (Integer i = 0; i < coa.size(); i++)
                    if (i != coa.size() - 1)
                        coaQry = coaQry + coa.get(i).getGlcode() + ",";
                    else
                        coaQry = coaQry + coa.get(i).getGlcode() + ")";
            }
            final String query = " from BudgetGroup bg where " + coaQry
                    + " and bg in (select budgetGroup from BudgetDetail) and bg.isActive=true order by bg.name";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getBudgetHeadForGlcodeList detailcode query=====" + query);
            // persistenceService.setType(BudgetGroup.class);
            final List bgList = persistenceService.findAllBy(query);
            return bgList;

        } catch (final ValidationException v) {
            LOGGER.error("Exp in getBudgetHeadForGlcodeList API()=" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exception in getBudgetHeadForGlcodeList API=" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
    }

    public String getQuery(final Class clazz, final Serializable id, final String queryString) {

        session = getCurrentSession();
        String query = EMPTY_STRING;
        if (id == null)
            return query;
        try {
            final Object o = findById(clazz, id);
            if (o != null)
                query = queryString + id;
        } catch (final ValidationException v) {
            LOGGER.error("Exp in getQuery==" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.equals("Exp in getQuery==" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
        return query;
    }

    /**
     * This API is handling the budget checking
     * 
     * @param paramMap
     *            paramMap contains 1. debitAmt (mandatory) 2. creditAmt
     *            (mandatory) 3. deptid (optional) 4. functionid (optional) 5.
     *            functionaryid (optional) 6. schemeid (optional) 7. subschemeid
     *            (optional) 8. boundaryid (optional) 9. glcode (mandatory) -
     *            based on the glcode, we can get the budgetheadid 10. asondate
     *            (manadtory) - to get the actuals, we need asondate
     *            11.mis.budgetcheckreq-Boolean-(optional) set to false if
     *            budget check not to be done for this bill default is True.
     * @return @
     */
    @Override
    public boolean budgetaryCheckForBill(final Map<String, Object> paramMap) {
        String cashbasedbudgetType = EMPTY_STRING, txnType = EMPTY_STRING;
        BigDecimal debitAmt = null;
        BigDecimal creditAmt = null;
        BigDecimal txnAmt = null;
        String glCode = "";
        Date asondate = null;
        Date fromdate = null;
        try {
            String budgetCheckConfig = budgetCheckConfigService.getConfigValue();
            if (budgetCheckConfig.equals(BudgetControlType.BudgetCheckOption.NONE.toString())) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Application Level budget check disabled skipping budget check.");
                return true;
            }
            if (paramMap.get("mis.budgetcheckreq") != null
                    && ((Boolean) paramMap.get("mis.budgetcheckreq")).equals(false)) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("voucher Level budget check disabled  so skipping budget check.");
                return true;
            }
            if (paramMap.get("debitAmt") != null)
                debitAmt = (BigDecimal) paramMap.get("debitAmt");
            if (paramMap.get("creditAmt") != null)
                creditAmt = (BigDecimal) paramMap.get("creditAmt");

            if (debitAmt == null && creditAmt == null)
                throw new ValidationException(EMPTY_STRING, "Both Debit and Credit amount is null");

            if (debitAmt != null && debitAmt.compareTo(BigDecimal.ZERO) == 0 && creditAmt != null
                    && creditAmt.compareTo(BigDecimal.ZERO) == 0)
                throw new ValidationException(EMPTY_STRING, "Both Debit and Credit amount is zero");

            if (debitAmt != null && debitAmt.compareTo(BigDecimal.ZERO) > 0 && creditAmt != null
                    && creditAmt.compareTo(BigDecimal.ZERO) > 0)
                throw new ValidationException(EMPTY_STRING, "Both Debit and Credit amount is greater than zero");

            // get the type of budget from appconfig .
            List<AppConfigValues> list = appConfigValuesService.getConfigValuesByModuleAndKey(EGF,
                    "budgetaryCheck_budgettype_cashbased");
            if (list.isEmpty())
                throw new ValidationException(EMPTY_STRING,
                        "budgetaryCheck_budgettype_cashbased is not defined in AppConfig");

            cashbasedbudgetType = list.get(0).getValue();
            if (cashbasedbudgetType.equalsIgnoreCase("Y")) // cash based budget
            {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("cashbasedbudgetType==" + cashbasedbudgetType);
            } else // Accural based budgeting
            {
                if (debitAmt != null && debitAmt.compareTo(BigDecimal.ZERO) > 0) {
                    txnType = "debit";
                    txnAmt = debitAmt;
                } else {
                    txnType = "credit";
                    txnAmt = creditAmt;
                }

                if (paramMap.get("glcode") != null)
                    glCode = paramMap.get("glcode").toString();
                if (paramMap.get(Constants.ASONDATE) != null)
                    asondate = (Date) paramMap.get(Constants.ASONDATE);

                if (glCode == null)
                    throw new ValidationException(EMPTY_STRING, "glcode is null");
                if (txnAmt == null)
                    throw new ValidationException(EMPTY_STRING, "txnAmt is null");
                if (txnType == null)
                    throw new ValidationException(EMPTY_STRING, "txnType is null");
                if (asondate == null)
                    throw new ValidationException(EMPTY_STRING, "As On Date is null");

                // check the account code needs budget checking
                final CChartOfAccounts coa = chartOfAccountsHibernateDAO.getCChartOfAccountsByGlCode(glCode);
                if (coa.getBudgetCheckReq() != null && coa.getBudgetCheckReq()) {
                    // get budgethead for the glcode
                    final List<BudgetGroup> budgetHeadListByGlcode = getBudgetHeadByGlcode(coa);

                    if (!isBudgetCheckingRequiredForType(txnType,
                            budgetHeadListByGlcode.get(0).getBudgetingType().toString())) {
                        if (LOGGER.isDebugEnabled())
                            LOGGER.debug("No need to check budget for :" + glCode + " as the transaction type is "
                                    + txnType + "so skipping budget check");
                        return true;
                    }

                    // get the financialyear from asondate
                    final CFinancialYear finyear = financialYearHibDAO.getFinancialYearByDate(asondate);
                    final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Constants.LOCALE);
                    if (finyear == null)
                        throw new ValidationException(EMPTY_STRING,
                                "Financial year is not defined for this date [" + sdf.format(asondate) + "]");
                    fromdate = finyear.getStartingDate();

                    paramMap.put("financialyearid", Long.valueOf(finyear.getId()));
                    paramMap.put(BUDGETHEADID, budgetHeadListByGlcode);
                    paramMap.put("fromdate", fromdate);
                    paramMap.put(Constants.ASONDATE, finyear.getEndingDate());

                    paramMap.put(GLCODEID, coa.getId());
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("************ BudgetCheck Details For bill *********************");
                    final BigDecimal budgetedAmt = getBudgetedAmtForYear(paramMap); // get
                    // the
                    // budgetedamount

                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(".................Budgeted amount......................" + budgetedAmt);
                    if (budgetCheckConfigService.getConfigValue()
                            .equalsIgnoreCase(BudgetControlType.BudgetCheckOption.MANDATORY.toString()))
                        if (budgetedAmt.compareTo(BigDecimal.ZERO) == 0) {
                            if (LOGGER.isDebugEnabled())
                                LOGGER.debug(
                                        "Budget check failed Because of  Budgeted not allocated for the combination");
                            return false;
                        }

                    final BigDecimal actualAmt = getActualBudgetUtilizedForBudgetaryCheck(paramMap); // get
                    // actual
                    // amount
                    // only
                    // Generalledger
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("..................Voucher Actual amount......................." + actualAmt);

                    BigDecimal billAmt = getBillAmountForBudgetCheck(paramMap); // get
                    // actual
                    // amount
                    // of
                    // Bill
                    EgBillregister bill = null;

                    if (paramMap.get("bill") != null)
                        bill = (EgBillregister) paramMap.get("bill");
                    if (bill != null && bill.getEgBillregistermis().getBudgetaryAppnumber() != null) {
                        if (LOGGER.isDebugEnabled())
                            LOGGER.debug(".............Found BillId so subtracting txn amount......................"
                                    + txnAmt);
                        billAmt = billAmt.subtract(txnAmt);
                    }

                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("........................Bill Actual amount ........................" + billAmt);

                    final BigDecimal diff = budgetedAmt.subtract(actualAmt).subtract(billAmt); // get
                    // diff
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("......................diff amount......................" + diff);
                    }
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("************ BudgetCheck Details For bill End *********************");

                    if (budgetCheckConfigService.getConfigValue()
                            .equalsIgnoreCase(BudgetControlType.BudgetCheckOption.MANDATORY.toString())) {
                        if (txnAmt.compareTo(diff) <= 0) {
                            if (paramMap.get("bill") != null)
                                getAppNumberForBill(paramMap);
                            return true;
                        } else
                            return false;
                    }
                    if (budgetCheckConfigService.getConfigValue()
                            .equalsIgnoreCase(BudgetControlType.BudgetCheckOption.ANTICIPATORY.toString())) {
                        getAppNumberForBill(paramMap);
                        return true;
                    }

                } else
                    return true;

            }
        } catch (final ValidationException v) {
            LOGGER.error("Exp in budgetary check API()=" + v.getErrors());
            throw v;
        } catch (final Exception e) {
            LOGGER.error("Exp in budgetary check API()=" + e.getMessage());
            throw e;
        }
        return true;
    }

    private void getAppNumberForBill(final Map<String, Object> paramMap) {
        if (paramMap.get("bill") != null
                && ((EgBillregister) paramMap.get("bill")).getEgBillregistermis().getBudgetaryAppnumber() == null) {
            final String budgetApprNumber = getBudgetApprNumber(paramMap);
            ((EgBillregister) paramMap.get("bill")).getEgBillregistermis().setBudgetaryAppnumber(budgetApprNumber);
        }
    }

    @Override
    public BigDecimal getBillAmountForBudgetCheck(final Map<String, Object> paramMap) {
        Long deptid = null;
        Long functionid = null;
        Integer functionaryid = null;
        Integer schemeid = null;
        Integer subschemeid = null;
        Long boundaryid = null;
        Integer fundid = null;
        Long glcodeid = null;
        Date fromdate = null;
        Date asondate = null;
        BigDecimal totalBillUtilized = new BigDecimal(0);

        String query = EMPTY_STRING;
        String query1 = EMPTY_STRING;
        try {
            if (paramMap.get(Constants.DEPTID) != null)
                deptid = (Long) paramMap.get(Constants.DEPTID);
            if (paramMap.get(Constants.FUNCTIONID) != null)
                functionid = (Long) paramMap.get(Constants.FUNCTIONID);
            if (paramMap.get(Constants.FUNCTIONARYID) != null)
                functionaryid = (Integer) paramMap.get(Constants.FUNCTIONARYID);
            if (paramMap.get(Constants.SCHEMEID) != null)
                schemeid = (Integer) paramMap.get(Constants.SCHEMEID);
            if (paramMap.get(Constants.FUNDID) != null)
                fundid = (Integer) paramMap.get(Constants.FUNDID);
            if (paramMap.get(Constants.SUBSCHEMEID) != null)
                subschemeid = (Integer) paramMap.get(Constants.SUBSCHEMEID);
            if (paramMap.get(Constants.BOUNDARYID) != null)
                boundaryid = (Long) paramMap.get(Constants.BOUNDARYID);
            if (paramMap.get(GLCODEID) != null)
                glcodeid = (Long) paramMap.get(GLCODEID);
            if (paramMap.get(Constants.ASONDATE) != null)
                asondate = (java.util.Date) paramMap.get(Constants.ASONDATE);
            if (paramMap.get("fromdate") != null)
                fromdate = (java.util.Date) paramMap.get("fromdate");

            // get the financialyear from asondate
            final CFinancialYear finyear = financialYearHibDAO.getFinancialYearByDate(asondate);
            final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Constants.LOCALE);
            if (finyear == null)
                throw new ValidationException(EMPTY_STRING,
                        "Financial year is not defined for this date [" + sdf.format(asondate) + "]");
            fromdate = finyear.getStartingDate();

            paramMap.put("financialyearid", Long.valueOf(finyear.getId()));
            paramMap.put("fromdate", fromdate);
            paramMap.put(Constants.ASONDATE, finyear.getEndingDate());

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("deptid=" + deptid + ",functionid=" + functionid + ",functionaryid=" + functionaryid
                        + ",schemeid=" + schemeid + ",subschemeid=" + subschemeid + ",boundaryid=" + boundaryid
                        + ",glcodeid=" + glcodeid + ",asondate=" + asondate);

            if (asondate == null)
                throw new ValidationException(EMPTY_STRING, "As On Date is null");

            final List<AppConfigValues> budgetGrouplist = appConfigValuesService.getConfigValuesByModuleAndKey(EGF,
                    BUDGETARY_CHECK_GROUPBY_VALUES);
            if (budgetGrouplist.isEmpty())
                throw new ValidationException(EMPTY_STRING,
                        "budgetaryCheck_groupby_values is not defined in AppConfig");
            else {
                final AppConfigValues appConfigValues = budgetGrouplist.get(0);
                final String[] values = StringUtils.split(appConfigValues.getValue(), ",");
                for (final String value : values)
                    if (value.equals("department")) {
                        if (deptid == null || deptid == 0)
                            throw new ValidationException(EMPTY_STRING, "Department is required");
                        else
                            query = query + getQuery(Department.class, deptid, " and bmis.egDepartment.id=");
                    } else if (value.equals("function")) {
                        if (functionid == null || functionid == 0)
                            throw new ValidationException(EMPTY_STRING, "Function is required");
                        else
                            query = query + getQuery(CFunction.class, functionid, " and bd.functionid=");
                    } else if (value.equals("functionary")) {
                        if (functionaryid == null || functionaryid == 0)
                            throw new ValidationException(EMPTY_STRING, "Functionary is required");
                        else
                            query = query + getQuery(Functionary.class, functionaryid, " and bmis.functionaryid.id=");
                    } else if (value.equals("fund")) {
                        if (fundid == null || fundid == 0)
                            throw new ValidationException(EMPTY_STRING, "Fund is required");
                        else
                            query = query + getQuery(Fund.class, fundid, " and bmis.fund.id=");
                    } else if (value.equals("scheme")) {
                        if (schemeid == null || schemeid == 0)
                            throw new ValidationException(EMPTY_STRING, "Scheme is required");
                        else
                            query = query + getQuery(Scheme.class, schemeid, " and bmis.scheme.id=");
                    } else if (value.equals("subscheme")) {
                        if (subschemeid == null || subschemeid == 0)
                            throw new ValidationException(EMPTY_STRING, "Sub scheme is required");
                        else
                            query = query + getQuery(SubScheme.class, subschemeid, " and bmis.subScheme.id=");
                    } else if (value.equals("boundary")) {
                        if (boundaryid == null || boundaryid == 0)
                            throw new ValidationException(EMPTY_STRING, "Boundary is required");
                        else
                            query = query + getQuery(Boundary.class, boundaryid, " and bmis.fieldid.id=");
                    } else
                        throw new ValidationException(EMPTY_STRING,
                                "budgetaryCheck_groupby_values is not matching=" + value);
            }
            if (asondate != null)
                query = query + " and br.billdate <=? ";
            if (fromdate != null)
                query = query + " and  br.billdate>=? ";

            query = query + " and bd.glcodeid='" + glcodeid + "'";
            query1 = "select sum(case when bd.debitamount is null then 0 ELSE bd.debitamount end -case when bd.creditamount is null then 0 else bd.creditamount end)  "
                    + " from EgBillregister br, EgBilldetails bd, EgBillregistermis bmis  "
                    + " where br.id=bd.egBillregister.id and br.id=bmis.egBillregister.id and (bmis.budgetCheckReq is null or bmis.budgetCheckReq=true)  and bmis.voucherHeader is null and upper(br.status.description) not in ('CANCELLED') "
                    + "    " + query;

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getBillAmountForBudgetCheck query============" + query1);
            Object ob = null;
            if (fromdate != null)
                ob = persistenceService.find(query1, asondate, fromdate);
            else
                ob = persistenceService.find(query1, asondate);

            final BigDecimal billAmountWhereCancelledVouchers = getBillAmountWhereCancelledVouchers(query, fromdate,
                    asondate);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Total Amount from all bills where vouchers are cancelled is : "
                        + billAmountWhereCancelledVouchers);
            if (ob == null)
                totalBillUtilized = billAmountWhereCancelledVouchers;
            else
                totalBillUtilized = new BigDecimal(ob.toString()).add(billAmountWhereCancelledVouchers);
            return totalBillUtilized;

        } catch (final ValidationException v) {
            LOGGER.error("Exp in getBillAmountForBudgetCheck API()===" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exp in getBillAmountForBudgetCheck API()===" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, "Exp in getBillAmountForBudgetCheck API()===" + e.getMessage());
        }
    }

    /**
     * This API is to get the total bill amount where bill vouchers are created
     * but cancelled later.
     * 
     * @param query
     * @param fromdate
     * @param asondate
     * @return @
     */

    private BigDecimal getBillAmountWhereCancelledVouchers(final String query, final Date fromdate,
            final Date asondate) {

        final String newQuery = "select sum(case when bd.debitamount is null then 0 else bd.debitamount end - case when bd.creditamount is null then 0 else bd.creditamount end )  "
                + " from EgBillregister br, EgBilldetails bd, EgBillregistermis bmis,CVoucherHeader vh  "
                + " where br.id=bd.egBillregister.id and br.id=bmis.egBillregister.id and (bmis.budgetCheckReq is null or bmis.budgetCheckReq=true)  and bmis.voucherHeader=vh.id and upper(br.status.description) not in ('CANCELLED') "
                + "  and vh.status=4  " + query;

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getBillAmountWhereCancelledVouchers query============" + newQuery);
        Object ob = null;
        if (fromdate != null)
            ob = persistenceService.find(newQuery, asondate, fromdate);
        else
            ob = persistenceService.find(newQuery, asondate);
        if (ob == null)
            return BigDecimal.ZERO;
        else
            return new BigDecimal(ob.toString());

    }

    /**
     * This parameter HashMap contains deptid,functionid,
     * functionaryid,schemeid,
     * subschemeid,boundaryid,budgetheadid,financialyearid,typeBeRe it'll get
     * the budgeted amount based on the parameters.
     * 
     * @param paramMap
     * @return budgeted amount @
     */
    @Override
    public BigDecimal getBudgetedAmtForYearRegardingBEorRE(final Map<String, Object> paramMap, final String typeBeRe) {
        Integer deptid = null;
        Long functionid = null;
        Integer functionaryid = null;
        Integer schemeid = null;
        Integer subschemeid = null;
        Integer boundaryid = null;
        Integer fundid = null;
        Long budgetheadid = null;
        Long financialyearid = null;

        String query = EMPTY_STRING;

        try {
            if (paramMap.get(Constants.DEPTID) != null)
                deptid = (Integer) paramMap.get(Constants.DEPTID);
            if (paramMap.get(Constants.FUNCTIONID) != null)
                functionid = (Long) paramMap.get(Constants.FUNCTIONID);
            if (paramMap.get(Constants.FUNCTIONARYID) != null)
                functionaryid = (Integer) paramMap.get(Constants.FUNCTIONARYID);
            if (paramMap.get(Constants.SCHEMEID) != null)
                schemeid = (Integer) paramMap.get(Constants.SCHEMEID);
            if (paramMap.get(Constants.SUBSCHEMEID) != null)
                subschemeid = (Integer) paramMap.get(Constants.SUBSCHEMEID);
            if (paramMap.get(Constants.FUNDID) != null)
                fundid = (Integer) paramMap.get(Constants.FUNDID);
            if (paramMap.get(Constants.BOUNDARYID) != null)
                boundaryid = (Integer) paramMap.get(Constants.BOUNDARYID);
            if (paramMap.get(BUDGETHEADID) != null)
                budgetheadid = (Long) paramMap.get(BUDGETHEADID);
            if (paramMap.get("financialyearid") != null)
                financialyearid = (Long) paramMap.get("financialyearid");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("deptid " + deptid + ",functionid " + functionid + ",functionaryid " + functionaryid
                        + ",schemeid " + schemeid + ",subschemeid " + subschemeid + ",boundaryid " + boundaryid
                        + ",budgetheadid " + budgetheadid + ",financialyearid " + financialyearid);

            query = prepareQuery(deptid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);

            if (budgetheadid == null)
                throw new ValidationException(EMPTY_STRING, "Budget head id is null or empty");
            query = query + getQuery(BudgetGroup.class, budgetheadid, " and bd.budgetGroup=");

            if (financialyearid == null)
                throw new ValidationException(EMPTY_STRING, "Financial Year id is null");
            query = query + getQuery(CFinancialYear.class, financialyearid, " and bd.budget.financialYear=");

            final String finalquery = " from BudgetDetail bd where bd.budget.isbere= '" + typeBeRe + "' " + query;

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("finalquery  =" + finalquery);
            final List<BudgetDetail> bdList = persistenceService.findAllBy(finalquery);

            if (bdList == null || bdList.size() == 0)
                return BigDecimal.ZERO;
            else
                return getApprovedAmt(bdList);
        } catch (final ValidationException v) {
            LOGGER.error("Exp in getBudgetedAmtForYear==" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exp in getBudgetedAmtForYear==" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, "Exp in getBudgetedAmtForYear==" + e.getMessage());
        }
    }

    /**
     * @description - returns the Sanctioned Planning Budget amount based on
     *              various parameters. For an year if there is an approved RE,
     *              then the RE sanctioned planning amount will be returned else
     *              the sanctioned BE amount will be returned. This will take
     *              care of the re-appropriations also.
     * @param parameter
     *            Map contains deptid,functionid, functionaryid,schemeid,
     *            subschemeid,boundaryid,budgetheadid,financialyearid,fundid
     *            it'll get the budgeted amount based on the parameters.
     * @return Sanctioned Planning Budget amount @
     */
    @Override
    public BigDecimal getSanctionedPlanningBudget(final Map<String, Object> paramMap) {
        final BigDecimal multiplicationFactor = new BigDecimal(
                Double.parseDouble(getAppConfigFor("EGF", "planning_budget_multiplication_factor")));
        final BigDecimal budgetedAmtForYear = zeroOrValue(getBudgetedAmtForYear(paramMap));
        return budgetedAmtForYear.multiply(multiplicationFactor);
    }

    private BigDecimal zeroOrValue(final BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String getAppConfigFor(final String module, final String key) {
        try {
            final List<AppConfigValues> list = appConfigValuesService.getConfigValuesByModuleAndKey(module, key);
            return list.get(0).getValue().toString();
        } catch (final Exception e) {
            throw new ValidationException(Arrays
                    .asList(new ValidationError(key + " not defined in appconfig", key + " not defined in appconfig")));
        }
    }

    /**
     * @description - get the list of BudgetUsage based on various parameters
     * @param queryParamMap
     *            - HashMap<String, Object> queryParamMap will have data
     *            required for the query the queryParamMap contain values :- the
     *            mis attribute values passed in the query map will be validated
     *            with the appconfig value of key=budgetaryCheck_groupby_values
     *            financialyearid - optional ExecutionDepartmentId - mandatory
     *            -if:department present in the app config value - optional
     *            -else fundId - mandatory -if:fund present in the app config
     *            value - optional -else schemeId - mandatory -if:Scheme present
     *            in the app config value - optional -else functionId -
     *            mandatory -if:function present in the app config value -
     *            optional -else subschemeId - mandatory -if:Subscheme present
     *            in the app config value - optional -else functionaryId -
     *            mandatory -if:functionary present in the app config value -
     *            optional -else boundaryId - mandatory -if:boundary present in
     *            the app config value - optional -else moduleId - optional
     *            financialYearId -optional budgetgroupId -optional fromDate
     *            -optional toDate -optional Order By - optional if passed then
     *            only Budgetary appropriation number and reference number is
     *            accepted, if not passed then default order by is date.
     * @return
     */

    @Override
    @SuppressWarnings("unchecked")
    public List<BudgetUsage> getListBudgetUsage(final Map<String, Object> queryParamMap) {

        final StringBuffer query = new StringBuffer();
        final Map<String, String> grpByVls = new HashMap<String, String>();
        List<BudgetUsage> listBudgetUsage = null;
        query.append("select bu from BudgetUsage bu,BudgetDetail bd where  bu.budgetDetail.id=bd.id");
        final List<AppConfigValues> list = appConfigValuesService.getConfigValuesByModuleAndKey(EGF,
                BUDGETARY_CHECK_GROUPBY_VALUES);
        if (list.isEmpty())
            throw new ValidationException(EMPTY_STRING, "budgetaryCheck_groupby_values is not defined in AppConfig");
        final AppConfigValues appConfigValues = list.get(0);
        if (appConfigValues.getValue().indexOf(",") != 1) { // if there are more
            // than one comma
            // separated values
            // for key =
            // budgetaryCheck_groupby_values
            final String[] values = StringUtils.split(appConfigValues.getValue(), ",");
            for (final String value : values)
                grpByVls.put(value, value);
        } else
            grpByVls.put(appConfigValues.getValue(), appConfigValues.getValue());

        if (!isNull(grpByVls.get("fund")))
            if (isNull(queryParamMap.get("fundId")))
                throw new ValidationException(EMPTY_STRING, "Fund is required");
            else
                query.append(" and bd.fund.id=").append(Integer.valueOf(queryParamMap.get("fundId").toString()));
        if (!isNull(grpByVls.get("department")))
            if (isNull(queryParamMap.get("ExecutionDepartmentId")))
                throw new ValidationException(EMPTY_STRING, "Department is required");
            else
                query.append(" and bd.executingDepartment.id=")
                        .append(Integer.valueOf(queryParamMap.get("ExecutionDepartmentId").toString()));
        if (!isNull(grpByVls.get("function")))
            if (isNull(queryParamMap.get("functionId")))
                throw new ValidationException(EMPTY_STRING, "Function is required");
            else
                query.append(" and bd.function.id=").append(Long.valueOf(queryParamMap.get("functionId").toString()));
        if (!isNull(grpByVls.get("scheme")))
            if (isNull(queryParamMap.get("schemeId")))
                throw new ValidationException(EMPTY_STRING, "Scheme is required");
            else
                query.append(" and bd.scheme.id=").append(Integer.valueOf(queryParamMap.get("schemeId").toString()));
        if (!isNull(grpByVls.get("subscheme")))
            if (isNull(queryParamMap.get("subschemeId")))
                throw new ValidationException(EMPTY_STRING, "SubScheme is required");
            else
                query.append(" and bd.subScheme.id=")
                        .append(Integer.valueOf(queryParamMap.get("subschemeId").toString()));
        if (!isNull(grpByVls.get(Constants.FUNCTIONARY)))
            if (isNull(queryParamMap.get("functionaryId")))
                throw new ValidationException(EMPTY_STRING, "Functionary is required");
            else
                query.append(" and bd.functionary.id=")
                        .append(Integer.valueOf(queryParamMap.get("functionaryId").toString()));
        if (!isNull(grpByVls.get("boundary")))
            if (isNull(queryParamMap.get("boundaryId")))
                throw new ValidationException(EMPTY_STRING, "Boundary is required");
            else
                query.append(" and bd.boundary.id=")
                        .append(Integer.valueOf(queryParamMap.get("boundaryId").toString()));
        if (!isNull(queryParamMap.get("moduleId")))
            query.append(" and bu.moduleId=").append(Integer.valueOf(queryParamMap.get("moduleId").toString()));
        if (!isNull(queryParamMap.get("financialYearId")))
            query.append(" and bu.financialYearId=")
                    .append(Integer.valueOf(queryParamMap.get("financialYearId").toString()));
        if (!isNull(queryParamMap.get("budgetgroupId")))
            query.append(" and bd.budgetGroup.id=").append(Long.valueOf(queryParamMap.get("budgetgroupId").toString()));
        if (!isNull(queryParamMap.get("fromDate")))
            query.append(" and bu.updatedTime >=:from");
        if (!isNull(queryParamMap.get("toDate")))
            query.append(" and bu.updatedTime <=:to");
        if (!isNull(queryParamMap.get("Order By"))) {
            if (queryParamMap.get("Order By").toString().indexOf("appropriationnumber") == -1
                    && queryParamMap.get("Order By").toString().indexOf("referenceNumber") == -1)
                throw new ValidationException(EMPTY_STRING,
                        "order by value can be only Budgetary appropriation number or Reference number or both");
            else
                query.append(" Order By ").append(queryParamMap.get("Order By"));
        } else
            query.append(" Order By bu.updatedTime");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Budget Usage Query >>>>>>>> " + query.toString());
        final Query query1 = getCurrentSession().createQuery(query.toString());
        if (!isNull(queryParamMap.get("fromDate")))
            query1.setTimestamp("from", (Date) queryParamMap.get("fromDate"));
        if (!isNull(queryParamMap.get("toDate"))) {
            final Date date = (Date) queryParamMap.get("toDate");
            date.setMinutes(59);
            date.setHours(23);
            date.setSeconds(59);
            query1.setTimestamp("to", date);
        }

        listBudgetUsage = query1.list();
        return listBudgetUsage;

    }

    /**
     * returns sum(approved BE/RE amount + appropriated (addition-deduction)
     * amount)* mutliplicationFactor if budget and reappropriation both doesnot
     * exist it will return zero
     * 
     * @param fundId
     * @param deptId
     * @param asOnDate
     * @return
     */
    @Override
    public BigDecimal getPlannigBudgetBy(final Integer fundId, final Integer deptId, Date asOnDate1) {
        // 0.Validated
        Date asOnDate = new Date();
        if (fundId == null || fundId == -1)
            throw new IllegalArgumentException("fundId");
        if (deptId == null || deptId == -1)
            throw new IllegalArgumentException("deptId");
        if (asOnDate1 != null)
            asOnDate = asOnDate1;

        BigDecimal amount;
        // 1. get the FinancialYear for the date
        final CFinancialYear finYear = financialYearHibDAO.getFinYearByDate(asOnDate);
        // 2. check does approved RE Existis and set budgeting type
        final boolean hasApprovedReForYear = budgetService.hasApprovedReAsonDate(finYear.getId(), asOnDate);
        String isbere = hasApprovedReForYear ? "RE" : "BE";
        // 3. get Budget approvedAmount
        amount = (BigDecimal) persistenceService
                .find("select sum(approvedAmount) from BudgetDetail bd where  bd.executingDepartment.id=? and bd.fund.id=?"
                        + " and bd.budget.financialYear=? and bd.budget.isbere=? ", deptId, fundId, finYear, isbere);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Approved " + isbere + " Amount" + amount);
        amount = amount == null ? BigDecimal.ZERO : amount;
        // 4. Reappropriated amounts
        BigDecimal reappAmount = (BigDecimal) persistenceService.find(
                "select sum(additionAmount-deductionAmount) from BudgetReAppropriation br where  br.budgetDetail.executingDepartment.id=? and br.budgetDetail.fund.id=?"
                        + " and br.budgetDetail.budget.financialYear=? and br.budgetDetail.budget.isbere=? and br.status.description='Approved' and to_date(br.modifiedDate)<=? ",
                deptId, fundId, finYear, isbere, asOnDate);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Approved Reappropriation Amount" + reappAmount);
        reappAmount = reappAmount == null ? BigDecimal.ZERO : reappAmount;
        // 5. Multiply with mutiplication factor
        final BigDecimal multiplicationFactor = BigDecimal
                .valueOf(Double.parseDouble(getAppConfigFor("EGF", "planning_budget_multiplication_factor")));
        amount = amount.add(reappAmount);
        amount = amount.multiply(multiplicationFactor);
        return amount;
    }

    /**
     * Returns a list of Functions having entry in budget detail with the given
     * fund and department .
     * 
     * @param fund,function,department
     *            and account type @
     */
    @Override
    public List<CFunction> getFunctionsByFundAndDepartment(final Integer fund, final Long department) {

        List<CFunction> functionsList = new ArrayList<CFunction>();
        try {
            final StringBuilder qryStr = new StringBuilder();
            final StringBuilder filtersQryStr = new StringBuilder();
            if (fund != null)
                filtersQryStr.append(" and bd.fund.id =:fund ");
            if (department != null)
                filtersQryStr.append(" and bd.executingDepartment.id =:department ");

            qryStr.append(" select distinct bd.function from BudgetDetail bd  where bd.id is not null  ");
            qryStr.append(filtersQryStr);
            session = getCurrentSession();
            final Query qry = session.createQuery(qryStr.toString());
            if (fund != null)
                qry.setInteger("fund", fund);
            if (department != null)
                qry.setLong("department", department);

            functionsList = qry.list();

            if (functionsList.isEmpty())
                throw new ValidationException(EMPTY_STRING, "No Functions mapped for the given fund and department  ");
        } catch (final ValidationException v) {
            throw v;
        } catch (final Exception e) {
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
        return functionsList;

    }

    private boolean isNull(final Object ob) {
        if (ob == null)
            return true;
        else
            return false;
    }

    public ApplicationSequenceNumberGenerator getSequenceGenerator() {
        return sequenceGenerator;
    }

    public void setSequenceGenerator(final ApplicationSequenceNumberGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setBudgetService(final BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public void setAppConfigValuesService(final AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

    @Override
    public BudgetDetail findById(Number id, boolean lock) {

        return null;
    }

}