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


package org.egov.dao.recoveries;

import org.apache.log4j.Logger;
import org.egov.commons.EgPartytype;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.recoveries.Recovery;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * TODO Brief Description of the purpose of the class/interface
 *
 * @author Sathish
 * @version 1.00
 */
@Repository
@Transactional(readOnly = true)
public class TdsHibernateDAO {
    public static final SimpleDateFormat DDMMYYYYFORMAT1 = new SimpleDateFormat("dd-MMM-yyyy", new Locale("en", "IN"));
    private final Logger LOGGER = Logger.getLogger(TdsHibernateDAO.class);
    @PersistenceContext
    private EntityManager entityManager;
    private Session session;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService<Recovery, Integer> persistenceService;

    @Transactional
    public Recovery update(final Recovery entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public Recovery create(final Recovery entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(Recovery entity) {
        getCurrentSession().delete(entity);
    }

    public Recovery findById(Number id, boolean lock) {
        return (Recovery) getCurrentSession().load(Recovery.class, id);
    }

    public List<Recovery> findAll() {
        return (List<Recovery>) getCurrentSession().createQuery("from Recovery").list();
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Recovery findById(final Long id, final boolean lock) {
        session = getCurrentSession();
        Recovery recovery;
        if (lock)
            recovery = (Recovery) session.load(Recovery.class, id, LockMode.UPGRADE);
        else
            recovery = (Recovery) session.load(Recovery.class, id);
        return recovery;
    }

    public List findByEstDate(final String estimateDate) {
        session = getCurrentSession();
        final Query qry = session
                .createQuery("from Recovery tds where tds.isactive=true and tds.effectivefrom <= :estimateDate order by upper(type)");
        qry.setParameter("estimateDate", estimateDate, StringType.INSTANCE);
        return qry.list();
    }

    public Recovery getTdsByType(final String type) {
        session = getCurrentSession();
        final Query qry = session.createQuery("from Recovery tds where upper(tds.type) = :type");
        qry.setParameter("type", type.toUpperCase().trim(), StringType.INSTANCE);
        return (Recovery) qry.uniqueResult();
    }

    public List getAllTds() {
        session = getCurrentSession();
        final Query qry = session.createQuery("from Recovery order by upper(type)");
        return qry.list();
    }

    public List<Recovery> getAllActiveAutoRemitTds() {
        session = getCurrentSession();
        final Query qry = session
                .createQuery("from Recovery where isactive=true and remittanceMode ='A' order by upper(type)");
        return qry.list();
    }

    public List<Recovery> getAllActiveTds() {
        session = getCurrentSession();
        final Query qry = session
                .createQuery("from Recovery where isactive=true and isEarning is null or isEarning='0' order by upper(type)");
        return qry.list();
    }

    public List<Recovery> getActiveTdsFilterBy(final String estimateDate, final BigDecimal estCost,
                                               final EgPartytype egPartytype, final EgwTypeOfWork egwTypeOfWork, final EgwTypeOfWork egwSubTypeOfWork) {
        Query qry;
        session = getCurrentSession();
        final StringBuffer qryStr = new StringBuffer();
        List<Recovery> tdsList = null;
        qryStr.append("from Recovery tds where tds.isactive = true ");

        if (egPartytype != null) {
            qryStr.append(" and tds.egPartytype = :egPartytype");
        }

        if (estCost != null) {
            qryStr.append(" and tds.id in (select ed.recovery.id from EgDeductionDetails ed where (ed.lowlimit <= :estCost and ed.highlimit >= :estCost and ed.highlimit is not null)")
                    .append(" or (ed.lowlimit <= :estCost and ed.highlimit is null)) ");
        }

        if (estimateDate != null && !estimateDate.equals("")) {
            qryStr.append(" and tds.id in (select ed.recovery.id from EgDeductionDetails ed where (ed.datefrom <= :estimateDate and ed.dateto >= :estimateDate and ed.dateto is not null)")
                    .append(" or(ed.datefrom <= :estimateDate and ed.dateto is null))");
        }

        if (egwTypeOfWork != null) {
            qryStr.append(" and tds.id in (select ed.recovery.id from EgDeductionDetails ed where ed.workDocType = :egwTypeOfWork)");
        }
        if (egwSubTypeOfWork != null) {
            qryStr.append("  and tds.id in (select ed.recovery.id from EgDeductionDetails ed where ed.workDocSubType = :egwSubTypeOfWork)");
        }

        qryStr.append(" order by upper(type)");
        qry = session.createQuery(qryStr.toString());

        if (estimateDate != null && !estimateDate.equals(""))
            qry.setParameter("estimateDate", estimateDate, StringType.INSTANCE);
        if (estCost != null)
            qry.setParameter("estCost", estCost, BigDecimalType.INSTANCE);
        if (egPartytype != null)
            qry.setParameter("egPartytype", egPartytype);
        if (egwTypeOfWork != null)
            qry.setParameter("egwTypeOfWork", egwTypeOfWork);
        if (egwSubTypeOfWork != null)
            qry.setParameter("egwSubTypeOfWork", egwSubTypeOfWork);

        tdsList = qry.list();
        return tdsList;
    }

    public List<Recovery> getAllTdsByPartyType(final String partyType) {
        List<Recovery> tdses;
        session = getCurrentSession();
        final Query qry = session.createQuery("from Recovery tds where upper(tds.egPartytype.code) = :partyType");
        qry.setParameter("partyType", partyType.toUpperCase().trim(), StringType.INSTANCE);
        tdses = qry.list();
        return tdses;
    }

    /**
     * @param asOndate - optional ,get the active recovery Objects based on supplied
     *                 date.
     * @return listTds -List<Recovery> - list of tds objects.
     * @throws - ValidationException
     * @author manoranjan.
     * @description -This API returns the List of recovery objects for party
     * type Contractor.
     */
    public List<Recovery> recoveryForPartyContractor(final Date asOndate) throws ValidationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | recoveryForPartyContractor | Start ");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("asONDate value received : " + asOndate);
        final StringBuffer recoveryQuery = new StringBuffer(400);
        List<Recovery> listTds;
        recoveryQuery
                .append("From Recovery where egPartytype.id in ( select id from EgPartytype where code = ?1) and isactive = true");
        if (null != asOndate)
            recoveryQuery.append(" and id in (select recovery.id from EgDeductionDetails where datefrom <= ?2 AND dateto >= ?3)");
        listTds = persistenceService.findAllBy(recoveryQuery.toString(), "Contractor", DDMMYYYYFORMAT1.format(asOndate),
                DDMMYYYYFORMAT1.format(asOndate));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("The size of recovery for party type Contractor is :" + listTds.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EgovCommon | recoveryForPartyContractor | End ");
        return listTds;

    }

    public EgPartytype getPartytypeByCode(final String code) {
        session = getCurrentSession();
        final Query qry = session.createQuery("from EgPartytype where code = :code");
        qry.setParameter("code", code.trim(), StringType.INSTANCE);
        return (EgPartytype) qry.uniqueResult();
    }

    public EgwTypeOfWork getTypeOfWorkByCode(final String code) {
        session = getCurrentSession();
        final Query qry = session.createQuery("from EgwTypeOfWork where code = :code");
        qry.setParameter("code", code.trim(), StringType.INSTANCE);
        return (EgwTypeOfWork) qry.uniqueResult();
    }

    public EgPartytype getSubPartytypeByCode(final String code) {
        session = getCurrentSession();
        final Query qry = session.createQuery("from EgPartytype where code = :code and parentid is not null");
        qry.setParameter("code", code.trim(), StringType.INSTANCE);
        return (EgPartytype) qry.uniqueResult();

    }

    public Recovery getTdsByTypeAndPartyType(final String type, final EgPartytype egPartytype) {
        session = getCurrentSession();
        final Query qry = session
                .createQuery("from Recovery tds where upper(tds.type) =:type and tds.egPartytype =:egPartytype");
        qry.setParameter("type", type.toUpperCase().trim(), StringType.INSTANCE);
        qry.setParameter("egPartytype", egPartytype);
        return (Recovery) qry.uniqueResult();
    }

    public Recovery findActiveTdsByGlcodeId(Long glcodeId) {
        return (Recovery) getCurrentSession().createQuery("from Recovery tds where tds.isactive = true and chartofaccounts.id = :glcodeId")
                .setParameter("glcodeId", glcodeId, LongType.INSTANCE)
                .uniqueResult();
    }

}