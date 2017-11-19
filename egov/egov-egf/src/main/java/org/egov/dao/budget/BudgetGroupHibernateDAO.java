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
 * Created on Jan 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.dao.budget;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FunctionDAO;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.budget.BudgetGroup;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class BudgetGroupHibernateDAO implements BudgetGroupDAO {
    @Transactional
    public BudgetGroup update(final BudgetGroup entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public BudgetGroup create(final BudgetGroup entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(BudgetGroup entity) {
        getCurrentSession().delete(entity);
    }

    public BudgetGroup findById(Number id, boolean lock) {
        return (BudgetGroup) getCurrentSession().load(BudgetGroup.class, id);
    }

    public List<BudgetGroup> findAll() {
        return (List<BudgetGroup>) getCurrentSession().createCriteria(BudgetGroup.class).list();
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    private static final Logger LOGGER = Logger.getLogger(BudgetGroupHibernateDAO.class);
    private Session session;
    private static final String EMPTY_STRING = "";

    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private FunctionDAO functionDAO;

    @Transactional
    public BudgetGroup createBudgetGroup(final BudgetGroup cbg) throws ValidationException {
        BudgetGroup budgetGroup = null;
        try {
            session = getCurrentSession();
            session.saveOrUpdate(cbg);
            budgetGroup = (BudgetGroup) session.get(BudgetGroup.class, cbg.getId());
            if (LOGGER.isInfoEnabled())
                LOGGER.info("budgetGroup saved id" + budgetGroup.getName());
        } catch (final ValidationException v) {
            LOGGER.error("Error in createBudgetGroup===" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error(e.getCause() + " Error in createBudgetGroup");
            throw new ValidationException(EMPTY_STRING, "egovexception in creation of budgetGroup" + e);
        }

        return budgetGroup;
    }

    @Override
    public List<BudgetGroup> getBudgetHeadByDateAndFunction(final String functionCode, final java.util.Date date)
            throws ValidationException {
        List<BudgetGroup> budgetHeadList = new ArrayList<BudgetGroup>();
        try {
            CFunction function = null;
            final StringBuffer qryStr = new StringBuffer();
            final CFinancialYear fiancialyear = financialYearDAO.getFinancialYearByDate(date);

            if (fiancialyear == null)
                throw new ValidationException(EMPTY_STRING, "Date is not defined in the Financial year master");

            qryStr.append(
                    "from BudgetGroup bg where  bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.budget.financialYear=:financialYearId   ");

            if (functionCode != null && !functionCode.equals("")) {
                function = functionDAO.getFunctionByCode(functionCode);
                if (function == null || function.getId() == null)
                    throw new ValidationException(EMPTY_STRING, "Function Code is not defined in the system");

                qryStr.append(" AND bd.function=:functionId ");
            }
            qryStr.append(" ) ");

            session = getCurrentSession();
            final Query qry = session.createQuery(qryStr.toString());
            qry.setLong("financialYearId", Long.valueOf(fiancialyear.getId()));

            if (functionCode != null && !functionCode.equals(""))
                qry.setLong("functionId", function.getId());
            budgetHeadList = qry.list();

            if (budgetHeadList.isEmpty() || budgetHeadList.size() == 0)
                throw new ValidationException(EMPTY_STRING, "No budget defined for the year "
                        + fiancialyear.getFinYearRange());
        } catch (final ValidationException v) {
            LOGGER.error("Exception in getBudgetHeadByDateAndFunction API()" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exception in getBudgetHeadByDateAndFunction API()=======" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
        return budgetHeadList;
    }

    @Override
    public BudgetGroup getBudgetHeadById(final Long id) {
        session = getCurrentSession();
        final Query qry = session.createQuery("from BudgetGroup bg where bg.id =:id");
        qry.setLong("id", id);
        return (BudgetGroup) qry.uniqueResult();
    }

    @Override
    public List<BudgetGroup> getBudgetGroupList() throws ValidationException {
        List<BudgetGroup> budgetHeadList = null;
        try {
            session = getCurrentSession();
            final Query qry = session.createQuery(" from BudgetGroup where isActive=true order by name");
            budgetHeadList = qry.list();
        } catch (final ValidationException v) {
            LOGGER.error("Exception in getBudgetGroupList API()" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exception in getBudgetGroupList API()=======" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
        return budgetHeadList;
    }

    /**
     * Returns a list of BudgetGroup having entry in budget detail with the given function code.
     * 
     * @param function code
     * @throws ValidationException
     */
    @Override
    public List<BudgetGroup> getBudgetHeadByFunction(final String functionCode) throws ValidationException {

        List<BudgetGroup> budgetHeadList = new ArrayList<BudgetGroup>();
        try {
            CFunction function = null;
            final StringBuffer qryStr = new StringBuffer();

            if (functionCode != null && !functionCode.equals("")) {
                function = functionDAO.getFunctionByCode(functionCode);
                if (function == null || function.getId() == null)
                    throw new ValidationException(EMPTY_STRING, "Function Code is not defined in the system");

                qryStr.append(
                        "from BudgetGroup bg where  bg in ( select distinct bd.budgetGroup from BudgetDetail bd  where bd.function=:functionId ) order by bg.name");
            }
            session = getCurrentSession();
            final Query qry = session.createQuery(qryStr.toString());

            if (functionCode != null && !functionCode.equals(""))
                qry.setLong("functionId", function.getId());
            budgetHeadList = qry.list();

            if (budgetHeadList.isEmpty() || budgetHeadList.size() == 0)
                throw new ValidationException(EMPTY_STRING, "No budget heads mapped for the function code - "
                        + functionCode);
        } catch (final ValidationException v) {
            LOGGER.error("Exception in getBudgetHeadByFunction API()" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exception in getBudgetHeadByFunction API()=======" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
        return budgetHeadList;

    }

    /**
     * Returns a list of BudgetGroup having entry in budget detail with the given function code or given List of ChartOfAccounts
     * or both.
     * 
     * @param function code
     * @param chartOfAccountsList
     * @throws ValidationException
     */
    @Override
    public List<BudgetGroup> getBudgetHeadByCOAandFunction(final String functionCode,
            final List<CChartOfAccounts> chartOfAccountsList) throws ValidationException {

        List<BudgetGroup> budgetHeadList = new ArrayList<BudgetGroup>();
        try {
            // If only function code is given.
            if (functionCode != null && !functionCode.equals("")
                    && (chartOfAccountsList == null || chartOfAccountsList.size() == 0))
                return getBudgetHeadByFunction(functionCode);
            else if (chartOfAccountsList != null && chartOfAccountsList.size() > 0
                    && (functionCode == null || functionCode.equals(""))) {

                final List<Long> coaIds = new ArrayList<Long>();
                for (final CChartOfAccounts coa : chartOfAccountsList)
                    coaIds.add(coa.getId());
                int size = coaIds.size();
                if (size > 999) {
                    int fromIndex = 0;
                    int toIndex = 0;
                    final int step = 1000;
                    List<BudgetGroup> newbudgetHeadList;
                    while (size - step >= 0) {
                        newbudgetHeadList = new ArrayList<BudgetGroup>();
                        toIndex += step;
                        final Query budgetHeadsQuery = getCurrentSession()
                                .createQuery(
                                        " from BudgetGroup bg where  bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 )");
                        budgetHeadsQuery.setParameterList("IDS1", coaIds.subList(fromIndex, toIndex));
                        budgetHeadsQuery.setParameterList("IDS2", coaIds.subList(fromIndex, toIndex));
                        newbudgetHeadList = budgetHeadsQuery.list();
                        fromIndex = toIndex;
                        size -= step;
                        if (newbudgetHeadList != null)
                            budgetHeadList.addAll(newbudgetHeadList);

                    }

                    if (size > 0) {
                        newbudgetHeadList = new ArrayList<BudgetGroup>();
                        fromIndex = toIndex;
                        toIndex = fromIndex + size;
                        final Query budgetHeadsQuery = getCurrentSession().createQuery(
                                " from BudgetGroup bg where bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 )");
                        budgetHeadsQuery.setParameterList("IDS1", coaIds.subList(fromIndex, toIndex));
                        budgetHeadsQuery.setParameterList("IDS2", coaIds.subList(fromIndex, toIndex));
                        newbudgetHeadList = budgetHeadsQuery.list();
                        if (newbudgetHeadList != null)
                            budgetHeadList.addAll(newbudgetHeadList);
                    }

                } else {
                    final Query budgetHeadsQuery = getCurrentSession().createQuery(
                            " from BudgetGroup bg where  bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 )");
                    budgetHeadsQuery.setParameterList("IDS1", coaIds);
                    budgetHeadsQuery.setParameterList("IDS2", coaIds);
                    budgetHeadList = budgetHeadsQuery.list();
                }
                if (budgetHeadList.isEmpty() || budgetHeadList.size() == 0)
                    throw new ValidationException(EMPTY_STRING, "No budget heads mapped for the function code - "
                            + functionCode);
                return budgetHeadList;

                // If function code and chartOfAccountsList is given.
            } else if (chartOfAccountsList != null && chartOfAccountsList.size() > 0 && functionCode != null
                    && !functionCode.equals("")) {

                final List<Long> coaIds = new ArrayList<Long>();
                CFunction function = null;

                if (functionCode != null && !functionCode.equals("")) {
                    function = functionDAO.getFunctionByCode(functionCode);
                    if (function == null || function.getId() == null)
                        throw new ValidationException(EMPTY_STRING, "Function Code is not defined in the system");

                }

                for (final CChartOfAccounts coa : chartOfAccountsList)
                    coaIds.add(coa.getId());
                int size = coaIds.size();
                if (size > 999) {
                    int fromIndex = 0;
                    int toIndex = 0;
                    final int step = 1000;
                    List<BudgetGroup> newbudgetHeadList;
                    while (size - step >= 0) {
                        newbudgetHeadList = new ArrayList<BudgetGroup>();
                        toIndex += step;
                        final Query budgetHeadsQuery = getCurrentSession()
                                .createQuery(
                                        " from BudgetGroup bg where bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 ) and bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.function=:functionId ) order by bg.name");
                        budgetHeadsQuery.setParameterList("IDS1", coaIds.subList(fromIndex, toIndex));
                        budgetHeadsQuery.setParameterList("IDS2", coaIds.subList(fromIndex, toIndex));
                        if (functionCode != null && !functionCode.equals(""))
                            budgetHeadsQuery.setLong("functionId", function.getId());
                        newbudgetHeadList = budgetHeadsQuery.list();
                        fromIndex = toIndex;
                        size -= step;
                        if (newbudgetHeadList != null)
                            budgetHeadList.addAll(newbudgetHeadList);

                    }

                    if (size > 0) {
                        newbudgetHeadList = new ArrayList<BudgetGroup>();
                        fromIndex = toIndex;
                        toIndex = fromIndex + size;
                        final Query budgetHeadsQuery = getCurrentSession()
                                .createQuery(
                                        " from BudgetGroup bg where  bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 ) and bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.function=:functionId ) order by bg.name");
                        budgetHeadsQuery.setParameterList("IDS1", coaIds.subList(fromIndex, toIndex));
                        budgetHeadsQuery.setParameterList("IDS2", coaIds.subList(fromIndex, toIndex));
                        if (functionCode != null && !functionCode.equals(""))
                            budgetHeadsQuery.setLong("functionId", function.getId());
                        newbudgetHeadList = budgetHeadsQuery.list();
                        if (newbudgetHeadList != null)
                            budgetHeadList.addAll(newbudgetHeadList);
                    }

                } else {
                    final Query budgetHeadsQuery = getCurrentSession()
                            .createQuery(
                                    " from BudgetGroup bg where  bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 ) and bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.function=:functionId ) order by bg.name ");
                    budgetHeadsQuery.setParameterList("IDS1", coaIds);
                    budgetHeadsQuery.setParameterList("IDS2", coaIds);
                    if (functionCode != null && !functionCode.equals(""))
                        budgetHeadsQuery.setLong("functionId", function.getId());
                    budgetHeadList = budgetHeadsQuery.list();
                }
                if (budgetHeadList.isEmpty() || budgetHeadList.size() == 0)
                    throw new ValidationException(EMPTY_STRING, "No budget heads mapped for the function code - "
                            + functionCode);
                return budgetHeadList;
                // If both are not given.
            } else {

                final StringBuffer qryStr = new StringBuffer();
                qryStr.append("from BudgetGroup bg order by bg.name");
                session = getCurrentSession();
                final Query qry = session.createQuery(qryStr.toString());

                budgetHeadList = qry.list();
            }

        } catch (final ValidationException v) {
            LOGGER.error("Exception in getBudgetHeadByFunction API()" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exception in getBudgetHeadByFunction API()=======" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
        return budgetHeadList;

    }

    /**
     * Returns a list of BudgetGroup having entry in budget detail with the given fund,function,department and account type.
     * 
     * @param fund,function,department and account type
     * @throws ValidationException
     */
    @Override
    public List<BudgetGroup> getBudgetGroupsByFundFunctionDeptAndAccountType(final Integer fund, final Long dept,
            final Long function, final String accountType) throws ValidationException {

        List<BudgetGroup> budgetHeadList = new ArrayList<BudgetGroup>();
        try {
            final StringBuffer qryStr = new StringBuffer();
            final StringBuffer filtersQryStr = new StringBuffer();
            final StringBuffer accountTypeQryStr = new StringBuffer();
            if (fund != null)
                filtersQryStr.append(" and bd.fund.id =:fund ");
            if (dept != null)
                filtersQryStr.append(" and bd.executingDepartment.id =:dept ");
            if (function != null)
                filtersQryStr.append(" and bd.function.id =:function ");
            if (accountType != null)
                accountTypeQryStr.append(" and bg.accountType =:accountType ");

            qryStr.append(
                    "from BudgetGroup bg where  bg in ( select distinct bd.budgetGroup from BudgetDetail bd  where bd.id is not null ");
            qryStr.append(filtersQryStr);
            qryStr.append(" ) ");
            qryStr.append(accountTypeQryStr);
            qryStr.append("order by bg.name");
            session = getCurrentSession();
            final Query qry = session.createQuery(qryStr.toString());
            if (fund != null)
                qry.setInteger("fund", fund);
            if (dept != null)
                qry.setLong("dept", dept);
            if (function != null)
                qry.setLong("function", function);
            if (accountType != null)
                qry.setString("accountType", accountType);

            budgetHeadList = qry.list();

            if (budgetHeadList.isEmpty() || budgetHeadList.size() == 0)
                throw new ValidationException(EMPTY_STRING,
                        "No budget heads mapped for the given fund,department,function and account type ");
        } catch (final ValidationException v) {
            LOGGER.error("Exception in getBudgetGroupsByFundFunctionDeptAndAccountType API()" + v.getErrors());
            throw new ValidationException(v.getErrors());
        } catch (final Exception e) {
            LOGGER.error("Exception in getBudgetGroupsByFundFunctionDeptAndAccountType API()=======" + e.getMessage());
            throw new ValidationException(EMPTY_STRING, e.getMessage());
        }
        return budgetHeadList;

    }

}