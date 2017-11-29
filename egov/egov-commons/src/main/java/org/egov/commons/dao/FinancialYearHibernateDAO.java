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
package org.egov.commons.dao;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Repository
public class FinancialYearHibernateDAO implements FinancialYearDAO {
    @Transactional
    public CFinancialYear update(final CFinancialYear entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public CFinancialYear create(final CFinancialYear entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(CFinancialYear entity) {
        getCurrentSession().delete(entity);
    }

    public CFinancialYear findById(Number id, boolean lock) {
        return (CFinancialYear) getCurrentSession().load(CFinancialYear.class, id);
    }

    public List<CFinancialYear> findAll() {
        return (List<CFinancialYear>) getCurrentSession().createCriteria(CFinancialYear.class).list();
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    private final Logger logger = Logger.getLogger(getClass().getName());

    public String getCurrYearFiscalId() {
        Date dt = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String currentDate = formatter.format(dt);
        String result = "";
        Query query = getCurrentSession().createQuery(
                "select cfinancialyear.id from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= '"
                        + currentDate + "' and cfinancialyear.endingDate >= '" + currentDate + "' ");
        ArrayList list = (ArrayList) query.list();
        result = list.get(0).toString();
        return result;
    }

    public String getCurrYearStartDate() {
        Date dt = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String currentDate = formatter.format(dt);
        logger.info("Obtained session");
        String result = "";
        Query query = getCurrentSession().createQuery(
                "select cfinancialyear.startingDate from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= '"
                        + currentDate + "' and cfinancialyear.endingDate >= '" + currentDate + "' ");
        ArrayList list = (ArrayList) query.list();
        if (list.size() > 0) {
            if (list.get(0) == null)
                return 0.0 + "";
            else
                result = list.get(0).toString();
        } else
            return 0.0 + "";
        return result;
    }

    public String getPrevYearFiscalId() {
        Date dt = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dt);
        int prevYear = calendar.get(Calendar.YEAR) - 1;
        calendar.set(Calendar.YEAR, prevYear);
        String previousDate = formatter.format(calendar.getTime());
        logger.info("Obtained session");
        String result = "";
        Query query = getCurrentSession().createQuery(
                "select cfinancialyear.id from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= '"
                        + previousDate + "' and cfinancialyear.endingDate >= '" + previousDate + "' ");
        ArrayList list = (ArrayList) query.list();
        if (list.size() > 0) {
            if (list.get(0) == null)
                return 0.0 + "";
            else
                result = list.get(0).toString();
        } else
            return 0.0 + "";
        return result;
    }

    @Deprecated
    public String getFinancialYearId(String estDate) {
        logger.info("Obtained session");
        String result = "";
        Query query = getCurrentSession().createQuery(
                "select cfinancialyear.id from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= to_date('"
                        + estDate + "','dd/MM/yyyy') and cfinancialyear.endingDate >= to_date('" + estDate
                        + "','dd/MM/yyyy') ");
        ArrayList list = (ArrayList) query.list();
        if (list.size() > 0)
            result = list.get(0).toString();
        return result;
    }

    public CFinancialYear getFinancialYearByFinYearRange(String finYearRange) {
        Query query = getCurrentSession().createQuery(
                "from CFinancialYear cfinancialyear where cfinancialyear.finYearRange=:finYearRange");
        query.setString("finYearRange", finYearRange);
        query.setCacheable(true);
        return (CFinancialYear) query.uniqueResult();
    }

    public List<CFinancialYear> getAllActiveFinancialYearList() {
        Query query = getCurrentSession().createQuery(
                "from CFinancialYear cfinancialyear where isActive=true order by finYearRange desc");
        return query.list();
    }

    public List<CFinancialYear> getAllActivePostingFinancialYear() {
        Query query = getCurrentSession()
                .createQuery(
                        "from CFinancialYear cfinancialyear where isActive=true and isActiveForPosting=true order by startingDate desc");
        return query.list();
    }

    public List<CFinancialYear> getAllNotClosedFinancialYears() {
        Query query = getCurrentSession()
                .createQuery(
                        "from CFinancialYear cfinancialyear where cfinancialyear.isClosed=false  order by cfinancialyear.startingDate asc");
        return query.list();
    }

    public CFinancialYear getFinancialYearById(Long id) {
        Query query = getCurrentSession().createQuery("from CFinancialYear cfinancialyear where id=:id");
        query.setLong("id", id);
        return (CFinancialYear) query.uniqueResult();
    }

    public List<CFinancialYear> getAllActivePostingAndNotClosedFinancialYears() {
        Query query = getCurrentSession()
                .createQuery(
                        "from CFinancialYear cfinancialyear where cfinancialyear.isClosed=false  and cfinancialyear.isActiveForPosting=true  order by cfinancialyear.startingDate asc");
        return query.list();
    }

    /*
     * @Override public CFinancialYear getFinancialYear(String estDate) {
     * session = getCurrentSession(); logger.info("Obtained session");
     * CFinancialYear result=null; Query query=session.createQuery(
     * "from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= '"
     * +estDate+"' and cfinancialyear.endingDate >= '"+estDate+"' "); ArrayList
     * list= (ArrayList)query.list(); if(list.size()>0) result=(CFinancialYear)
     * list.get(0); return result; }
     */
    /**
     * @param fromDate
     * @param toDate
     *            will will return false if any financialyear is not active for
     *            posting within given date range
     */
    public boolean isFinancialYearActiveForPosting(Date fromDate, Date toDate) {

        logger.info("Obtained session");
        String result = "";
        Query query = getCurrentSession()
                .createQuery(
                        ""
                                + " from CFinancialYear cfinancialyear where   cfinancialyear.isActiveForPosting=false and cfinancialyear.startingDate <=:sDate and cfinancialyear.endingDate >=:eDate  ");
        query.setDate("sDate", fromDate);
        query.setDate("eDate", toDate);
        ArrayList list = (ArrayList) query.list();
        if (list.size() > 0)
            return false;
        else
            return true;

    }

    /**
     * gives active FY only else throws
     * ApplicationRuntimeException("Financial Year is not active For Posting.")
     */

    public CFinancialYear getFinancialYearByDate(Date date) {
        CFinancialYear cFinancialYear = null;
        logger.info("Obtained session");
        String result = "";
        Query query = getCurrentSession()
                .createQuery(
                        " from CFinancialYear cfinancialyear where cfinancialyear.startingDate <=:sDate and cfinancialyear.endingDate >=:eDate  and cfinancialyear.isActiveForPosting=true");
        query.setDate("sDate", date);
        query.setDate("eDate", date);
        ArrayList list = (ArrayList) query.list();
        if (list.size() > 0)
            cFinancialYear = (CFinancialYear) list.get(0);
        if (null == cFinancialYear)
            throw new ApplicationRuntimeException("Financial Year is not active For Posting.");
        return cFinancialYear;
    }

    /**
     * gives active and not active FY
     */
    public CFinancialYear getFinYearByDate(Date date) {
        CFinancialYear cFinancialYear = null;
        logger.info("Obtained session");
        Query query = getCurrentSession()
                .createQuery(
                        " from CFinancialYear cfinancialyear where cfinancialyear.startingDate <=:sDate and cfinancialyear.endingDate >=:eDate");
        query.setDate("sDate", date);
        query.setDate("eDate", date);
        ArrayList list = (ArrayList) query.list();
        if (list.size() > 0)
            cFinancialYear = (CFinancialYear) list.get(0);
        if (null == cFinancialYear)
            throw new ApplicationRuntimeException("Financial Year Id does not exist.");
        return cFinancialYear;
    }

    public CFinancialYear getTwoPreviousYearByDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -2);
        return getFinYearByDate(cal.getTime());
    }

    public CFinancialYear getNextFinancialYearByDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, +1);
        return getFinYearByDate(cal.getTime());
    }

    public CFinancialYear getPreviousFinancialYearByDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return getFinYearByDate(cal.getTime());
    }

    /**
     * checks whether two dates fall in same financial Year
     */
    public boolean isSameFinancialYear(Date fromDate, Date toDate) {
        if (getFinYearByDate(fromDate).getId().longValue() == getFinYearByDate(toDate).getId().longValue()) {
            return true;
        } else {
            return false;
        }

    }

    public List<CFinancialYear> getAllPriorFinancialYears(Date date) {
        Query query = getCurrentSession()
                .createQuery(
                        " from CFinancialYear cfinancialyear where cfinancialyear.startingDate <:sDate and isActive=true order by finYearRange desc ");
        query.setDate("sDate", date);
        return query.list();
    }

}