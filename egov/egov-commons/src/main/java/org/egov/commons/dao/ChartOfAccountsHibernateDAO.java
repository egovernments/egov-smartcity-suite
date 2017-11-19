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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public class ChartOfAccountsHibernateDAO implements ChartOfAccountsDAO {
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Transactional
    public CChartOfAccounts update(final CChartOfAccounts entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public CChartOfAccounts create(final CChartOfAccounts entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(CChartOfAccounts entity) {
        getCurrentSession().delete(entity);
    }

    public CChartOfAccounts findById(Number id, boolean lock) {
        return (CChartOfAccounts) getCurrentSession().load(CChartOfAccounts.class, id);
    }

    @Override
    public List<CChartOfAccounts> findAll() {
        return (List<CChartOfAccounts>) getCurrentSession().createCriteria(CChartOfAccounts.class).list();
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    private final static Logger LOG = Logger.getLogger(ChartOfAccountsHibernateDAO.class);

    @Deprecated
    public Collection getAccountCodeListForDetails() {

        return getCurrentSession()
                .createQuery(
                        "select acc from CChartOfAccounts acc where acc.classification='4' and acc.isActiveForPosting=true order by acc.glcode")
                .list();

    }

    /**
     * This API will give the list of detailed active for posting chartofaccounts list
     * 
     * @return
     * @throws ApplicationException
     */
    public List<CChartOfAccounts> getDetailedAccountCodeList() {

        return getCurrentSession()
                .createQuery(
                        "select acc from CChartOfAccounts acc where acc.classification='4' and acc.isActiveForPosting=true order by acc.glcode")
                .setCacheable(true).list();

    }
    public List<CChartOfAccounts> getDetailedCodesList() {
        return getCurrentSession()
                .createQuery(
                        "from CChartOfAccounts where classification=4")
                .setCacheable(true).list();

    }

    
    public List<CChartOfAccounts> findDetailedAccountCodesByGlcodeOrNameLike(String searchString) {
        final Query qry = getCurrentSession()
                .createQuery(
                        "from CChartOfAccounts where classification='4' and isActiveForPosting=true and (glcode like :glCode or upper(name) like :name) order by glcode");
        qry.setString("glCode", searchString + "%");
        qry.setString("name", "%" + searchString.toUpperCase() + "%");
        return (List<CChartOfAccounts>) qry.list();
    }

    @Deprecated
    public CChartOfAccounts findCodeByPurposeId(final int purposeId) {
        final Query qry = getCurrentSession().createQuery(
                "select acc from CChartOfAccounts acc where acc.purposeId=:purposeId ");
        qry.setLong("purposeId", purposeId);
        return (CChartOfAccounts) qry.uniqueResult();
    }

    public CChartOfAccounts getCChartOfAccountsByGlCode(final String glCode) {
        final Query qry = getCurrentSession().createQuery("from CChartOfAccounts coa where coa.glcode =:glCode");
        qry.setString("glCode", glCode);
        return (CChartOfAccounts) qry.uniqueResult();
    }

    @Deprecated
    public List getChartOfAccountsForTds() {
        final Query qry = getCurrentSession().createQuery(
                "from CChartOfAccounts coa where purposeId = 10 order by glcode");
        return qry.list();
    }

    @Deprecated
    public int getDetailTypeId(final String glCode, final Connection connection) throws Exception {
        int detailTypeId = 0;
        ResultSet rs;
        String qryDetailType = "Select detailtypeid from chartofaccountdetail where glcodeid=(select id from chartofaccounts where glcode=?)";
        PreparedStatement st = connection.prepareStatement(qryDetailType);
        st.setString(1, glCode);
        rs = st.executeQuery();
        if (rs.next()) {
            detailTypeId = rs.getInt(1);
        }
        rs.close();
        st.close();
        return detailTypeId;
    }

    @Deprecated
    public int getDetailTypeIdByName(final String glCode, final Connection connection, final String name) {
        final SQLQuery query = persistenceService
                .getSession()
                .createSQLQuery(
                        "SELECT a.ID FROM accountdetailtype a,chartofaccountdetail coad  WHERE coad.DETAILTYPEID =a.ID  AND coad.glcodeid=(SELECT ID FROM chartofaccounts WHERE glcode=:glCode) AND a.NAME=:name");
        query.setString("glCode", glCode);
        query.setString("name", name);
        List accountDtlTypeList = query.list();
        return (accountDtlTypeList != null) && (accountDtlTypeList.size() != 0) ? Integer.valueOf(accountDtlTypeList
                .get(0).toString()) : 0;
    }

    /**
     * This API will return the accountdetailtype for an account code when the accountcode and the respective accountdetailtype
     * name is passed.
     * 
     * @param glcode - This the chartofaccount code (mandatory)
     * @param name - This is the accountdetailtype name that is associated with the account code (mandatory)
     * @return - Returns the accountdetailtype object if the account code is having the passed accountdetailtype name, else NULL
     */
    public Accountdetailtype getAccountDetailTypeIdByName(final String glCode, final String name) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(glCode)) {
            throw new ApplicationRuntimeException("Account Code or Account Detail Type Name is empty");
        }
        Query query = getCurrentSession().createQuery("from CChartOfAccounts where glcode=:glCode");
        query.setString("glCode", glCode);
        if (query.list().isEmpty()) {
            throw new ApplicationRuntimeException("GL Code not found in Chart of Accounts");
        }
        query = getCurrentSession()
                .createQuery(
                        "from Accountdetailtype where id in (select cd.detailTypeId from "
                                + "CChartOfAccountDetail  as cd,CChartOfAccounts as c where cd.glCodeId=c.id and c.glcode=:glCode) and name=:name");
        query.setString("glCode", glCode);
        query.setString("name", name);
        return (Accountdetailtype) query.uniqueResult();
    }

    public List getGlcode(final String minGlcode, final String maxGlcode, final String majGlcode) {
        Query qry = null;
        final StringBuilder qryStr = new StringBuilder("select coa.glcode from CChartOfAccounts coa where ");
        if (StringUtils.isNotBlank(minGlcode) && StringUtils.isNotBlank(maxGlcode)) {
            qryStr.append(" coa.glcode between :minGlcode and :maxGlcode ");
            qry = getCurrentSession().createQuery(qryStr.toString());
            qry.setString("minGlcode", minGlcode + "%");
            qry.setString("maxGlcode", maxGlcode + "%");
        } else if (StringUtils.isNotBlank(maxGlcode)) {
            qryStr.append(" coa.glcode like :maxGlcode ");
            qry = getCurrentSession().createQuery(qryStr.toString());
            qry.setString("maxGlcode", maxGlcode + "%");
        } else if (StringUtils.isNotBlank(majGlcode)) {
            qryStr.append(" coa.glcode =:majGlcode ");
            qry = getCurrentSession().createQuery(qryStr.toString());
            qry.setString("majGlcode", majGlcode);
        }
        return qry == null ? null : qry.list();
    }

    /**
     * This API will return the list of detailed chartofaccounts objects that are active for posting for the Type.
     * 
     * @param -Accounting type-(Asset (A), Liability (L), Income (I), Expense (E))
     * @return list of chartofaccount objects
     */
    public List<CChartOfAccounts> getActiveAccountsForType(final char type) {
        final Query query = getCurrentSession()
                .createQuery(
                        "select acc from CChartOfAccounts acc where acc.classification='4' and acc.isActiveForPosting=true and type=:type order by acc.name");

        query.setCharacter("type", type);
        return query.list();
    }

    /**
     * to get the list of chartofaccounts based on the purposeId. First query will get the detail codes for the purpose is mapped
     * to major code level. second query will get the detail codes for the purpose is mapped to minor code level. last one will
     * get the detail codes are mapped to the detail code level.
     * 
     * @param purposeId
     * @return list of COA object(s)
     */
    public List<CChartOfAccounts> getAccountCodeByPurpose(final Integer purposeId) {
        final List<CChartOfAccounts> accountCodeList = new ArrayList<CChartOfAccounts>();
        try {
            if ((purposeId == null) || (purposeId.intValue() == 0)) {
                throw new ApplicationException("Purpose Id is null or zero");
            }
            Query query = getCurrentSession().createQuery(
                    " from EgfAccountcodePurpose purpose where purpose.id=" + purposeId + "");
            if (query.list().size() == 0) {
                throw new ApplicationException("Purpose ID provided is not defined in the system");
            }
            query = persistenceService
                    .getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId))) AND classification=4 AND isActiveForPosting=true ");
            query.setLong("purposeId", purposeId);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = persistenceService
                    .getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId)) AND classification=4 AND isActiveForPosting=true ");
            query.setLong("purposeId", purposeId);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = persistenceService
                    .getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId) AND classification=4 AND isActiveForPosting=true ");
            query.setLong("purposeId", purposeId);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = getCurrentSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE purposeid=:purposeId AND classification=4 AND isActiveForPosting=true ");
            query.setLong("purposeId", purposeId);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
        } catch (final Exception e) {
            LOG.error(e);
            throw new ApplicationRuntimeException("Error occurred while getting Account Code by purpose", e);
        }
        return accountCodeList;
    }

    /**
     * This API will return the list of non control detailed chartofaccount codes that are active for posting.
     * 
     * @return list of chartofaccount objects.
     */
    public List<CChartOfAccounts> getNonControlCodeList() {
        try {
            return getCurrentSession()
                    .createQuery(
                            " from CChartOfAccounts acc where acc.classification=4 and acc.isActiveForPosting=true and acc.id not in (select cd.glCodeId from CChartOfAccountDetail cd) ")
                    .list();

        } catch (final Exception e) {
            LOG.error(e);
            throw new ApplicationRuntimeException("Error occurred while getting Non-Control Code list", e);
        }
    }

    /**
     * @description- This method returns a list of detail type object based on the glcode.
     * @param glCode - glcode supplied by the client.
     * @return List<Accountdetailtype> -list of Accountdetailtype object(s).
     * @throws ApplicationException
     */
    @SuppressWarnings("unchecked")
    public List<Accountdetailtype> getAccountdetailtypeListByGLCode(final String glCode) {
        if (StringUtils.isBlank(glCode)) {
            throw new ApplicationRuntimeException("GL Code is empty ");
        }
        // checking if the glcode is exists in ChartOfAccounts table.

        CChartOfAccounts cChartOfAccountsByGlCode = getCChartOfAccountsByGlCode(glCode);
        if (cChartOfAccountsByGlCode == null) {
            throw new ApplicationRuntimeException("GL Code not found in Chart of Accounts");
        }
        try {
            Query query = persistenceService
                    .getSession()
                    .createQuery(
                            "from Accountdetailtype where id in (select cd.detailTypeId "
                                    + "from CChartOfAccountDetail  as cd,CChartOfAccounts as c where cd.glCodeId=c.id and c.glcode=:glCode)");
            query.setString("glCode", glCode);
            query.setCacheable(true);
            return query.list().isEmpty() ? null : query.list(); // NOPMD
        } catch (final Exception e) {
            LOG.error(e);
            throw new ApplicationRuntimeException("Error occured while getting Account Detail Types for GL Code ", e);
        }
    }

    /**
     * @author manoranjan
     * @description -Get list of COA for a list of types.
     * @param type - list of types,e.g income, Assets etc.
     * @return listChartOfAcc - list of chartofaccounts based on the given list of types
     * @throws ValidationException
     */
    public List<CChartOfAccounts> getActiveAccountsForTypes(final char[] type) throws ValidationException {
        if ((null == type) || (type.length == 0)) {
            throw new ValidationException(Arrays.asList(new ValidationError("type",
                    "The supplied value for Chart of Account Type  can not be null or empty")));
        }
        final Character[] types = new Character[type.length];
        int count = 0;
        for (final char typ : type) {
            types[count++] = typ;
        }
        final Query query = getCurrentSession().createQuery(
                "from CChartOfAccounts where classification=4 " + "and isActiveForPosting=true and type in (:type)");

        query.setParameterList("type", types);
        query.setCacheable(true);
        return query.list();
    }

    /**
     * @author manoranjan
     * @description - Get list of Chartofaccount objects for a list of purpose ids
     * @param purposeId - list of purpose ids.
     * @return listChartOfAcc - list of chartofaccount objects for the given list of purpose id
     * @throws ValidationException
     */
    public List<CChartOfAccounts> getAccountCodeByListOfPurposeId(final Integer[] purposeId) throws ValidationException {
        if ((null == purposeId) || (purposeId.length == 0)) {
            throw new ValidationException(Arrays.asList(new ValidationError("purposeId",
                    "The supplied purposeId  can not be null or empty")));
        }
        final List<CChartOfAccounts> listChartOfAcc = new ArrayList<CChartOfAccounts>();
        Query query = getCurrentSession()
                .createQuery(
                        " FROM CChartOfAccounts WHERE purposeid in(:purposeId)AND classification=4 AND isActiveForPosting=true ");
        query.setParameterList("purposeId", purposeId);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());

        query = persistenceService
                .getSession()
                .createQuery(
                        " from CChartOfAccounts where parentId IN (select id  FROM CChartOfAccounts WHERE purposeid in (:purposeId) ) AND classification=4 AND isActiveForPosting=true ");
        query.setParameterList("purposeId", purposeId);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());

        query = persistenceService
                .getSession()
                .createQuery(
                        " from CChartOfAccounts where   parentId IN (select id from CChartOfAccounts where parentId IN (select id  FROM CChartOfAccounts WHERE purposeid in (:purposeId))) AND classification=4 AND isActiveForPosting=true");
        query.setParameterList("purposeId", purposeId);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());

        query = persistenceService
                .getSession()
                .createQuery(
                        " from CChartOfAccounts where   parentId IN (select id from  CChartOfAccounts where   parentId IN (select id from CChartOfAccounts where parentId IN (select id  FROM CChartOfAccounts WHERE purposeid in (:purposeId)))) AND classification=4 AND isActiveForPosting=true ");
        query.setParameterList("purposeId", purposeId);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());

        return listChartOfAcc;
    }

    /**
     * @author manoranjan
     * @description - This api will return the list of detailed chartofaccounts objects that are active for posting.
     * @param glcode - The input is the chartofaccounts code.
     */
    public List<CChartOfAccounts> getListOfDetailCode(final String glCode) throws ValidationException {
        if (StringUtils.isBlank(glCode)) {
            throw new ValidationException(Arrays.asList(new ValidationError("glcode null",
                    "the glcode value supplied can not be null or blank")));
        }
        Query query = getCurrentSession().createQuery("from CChartOfAccounts where glcode=:glCode");
        query.setString("glCode", glCode);
        query.setCacheable(true);
        if (query.list().isEmpty()) {
            throw new ValidationException(Arrays.asList(new ValidationError("glcode not exist",
                    "The GL Code value supplied does not exist in the System")));
        }
        final List<CChartOfAccounts> listChartOfAcc = new ArrayList<CChartOfAccounts>();
        query = getCurrentSession().createQuery(
                " FROM CChartOfAccounts WHERE glcode=:glCode  AND classification=4 AND isActiveForPosting=true ");
        query.setString("glCode", glCode);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());
        query = getCurrentSession()
                .createQuery(
                        " from CChartOfAccounts where parentId IN (select id  FROM CChartOfAccounts WHERE glcode=:glCode) AND classification=4 AND isActiveForPosting=true ");
        query.setString("glCode", glCode);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());
        query = getCurrentSession()
                .createQuery(
                        " from CChartOfAccounts where parentId IN (select id from CChartOfAccounts where parentId IN ( select id  FROM CChartOfAccounts WHERE glcode=:glCode)) AND classification=4 AND isActiveForPosting=true ");
        query.setString("glCode", glCode);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());
        query = getCurrentSession()
                .createQuery(
                        " from CChartOfAccounts where parentId IN (select id from  CChartOfAccounts where   parentId IN (select id from CChartOfAccounts where parentId IN ( select id  FROM CChartOfAccounts WHERE glcode=:glCode)))AND classification=4 AND isActiveForPosting=true ");

        query.setString("glCode", glCode);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());
        return listChartOfAcc;
    }

    public List<CChartOfAccounts> getBankChartofAccountCodeList() {
        return getCurrentSession().createQuery("select chartofaccounts from Bankaccount").setCacheable(true).list();
    }

    @Override
    public List<CChartOfAccounts> findByType(Character type) {

        final Query query = getCurrentSession().createQuery(
                "from CChartOfAccounts where  " + "type =:type and classification=1");
        query.setCharacter("type", type);
        // query.setCacheable(true);
        return query.list();
    }

    @Override
    public List<CChartOfAccounts> findByMajorCodeAndClassification(String majorCode, Long classification) {
        final Query query = getCurrentSession().createQuery(
                "from CChartOfAccounts where  " + "majorcode =:majorcode and classification=2");
        query.setString("majorcode", majorCode);
        // query.setCacheable(true);
        return query.list();
    }

    @Override
    public List<CChartOfAccounts> findByGlcodeLikeIgnoreCaseAndClassificationAndMajorCode(String string,
            Long classification, String majorCode) {

        return null;
    }

    @Override
    public List<CChartOfAccounts> findByGlcodeLikeIgnoreCaseAndClassification(String string, Long classification) {

        return null;
    }

    public List<CChartOfAccounts> getBySubLedgerCode(String subLedgerCode) {
        final Query query = persistenceService
                .getSession()
                .createQuery(
                        "from CChartOfAccounts where id in (select glCodeId.id from CChartOfAccountDetail where lower(detailTypeId.name) =:subLedgerCode  ) and type = 'L' and classification=4 and isActiveForPosting = true and id not in (select chartofaccounts.id from Recovery)");
        query.setString("subLedgerCode", subLedgerCode.toLowerCase());
        return query.list();
    }

    public List<CChartOfAccounts> getForRecovery() {
        final Query query = persistenceService.getSession().createQuery(
                "from CChartOfAccounts where id in  (select chartofaccounts.id from Recovery)");
        return query.list();
    }

    /**
     * to get the list of chartofaccounts based on the purposeName. First query will get the detail codes for the purpose is
     * mapped to major code level. second query will get the detail codes for the purpose is mapped to minor code level. last one
     * will get the detail codes are mapped to the detail code level.
     * 
     * @param purposeId
     * @return list of COA object(s)
     */
    public List<CChartOfAccounts> getAccountCodeByPurposeName(final String purposeName) {
        final List<CChartOfAccounts> accountCodeList = new ArrayList<CChartOfAccounts>();
        try {
            if ((purposeName == null) || purposeName.equalsIgnoreCase("")) {
                throw new ApplicationException("Purpose Name is null or empty");
            }
            Query query = getCurrentSession().createQuery(
                    " from EgfAccountcodePurpose purpose where purpose.name='" + purposeName + "'");
            if (query.list().size() == 0) {
                throw new ApplicationException("Purpose ID provided is not defined in the system");
            }
            query = persistenceService
                    .getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT coa.id FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName))) AND classification=4 AND isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = persistenceService
                    .getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT coa.id FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName)) AND classification=4 AND isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = persistenceService
                    .getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT coa.id FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName) AND classification=4 AND isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = getCurrentSession()
                    .createQuery(
                            "SELECT coa FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName AND coa.classification=4 AND coa.isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
        } catch (final Exception e) {
            LOG.error(e);
            throw new ApplicationRuntimeException("Error occurred while getting Account Code by purpose", e);
        }
        return accountCodeList;
    }

    /**
     * @description - Get list of Chartofaccount objects for a list of purpose names
     * @param purposeNames - list of purpose names.
     * @return listChartOfAcc - list of chartofaccount objects for the given list of purpose names
     * @throws ValidationException
     */
    public List<CChartOfAccounts> getAccountCodeByListOfPurposeName(final String[] purposeNames) throws ValidationException {
        if ((null == purposeNames) || (purposeNames.length == 0)) {
            throw new ValidationException(Arrays.asList(new ValidationError("purposeId",
                    "The supplied purposeId  can not be null or empty")));
        }
        final List<CChartOfAccounts> listChartOfAcc = new ArrayList<CChartOfAccounts>();
        Query query = getCurrentSession()
                .createQuery(
                        "SELECT coa  FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId = purpose.id and purpose.name in(:purposeNames)AND coa.classification=4 AND coa.isActiveForPosting=true ");
        query.setParameterList("purposeNames", purposeNames);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());

        query = persistenceService
                .getSession()
                .createQuery(
                        " from CChartOfAccounts where parentId IN (select coa.id  FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId = purpose.id and purpose.name in (:purposeNames) ) AND classification=4 AND isActiveForPosting=true ");
        query.setParameterList("purposeNames", purposeNames);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());

        query = persistenceService
                .getSession()
                .createQuery(
                        " from CChartOfAccounts where   parentId IN (select id from CChartOfAccounts where parentId IN (select coa.id  FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId = purpose.id and purpose.name in (:purposeNames) )) AND classification=4 AND isActiveForPosting=true");
        query.setParameterList("purposeNames", purposeNames);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());

        query = persistenceService
                .getSession()
                .createQuery(
                        " from CChartOfAccounts where   parentId IN (select id from  CChartOfAccounts where   parentId IN (select id from CChartOfAccounts where parentId IN (select coa.id  FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId = purpose.id and purpose.name in (:purposeNames) ))) AND classification=4 AND isActiveForPosting=true ");
        query.setParameterList("purposeNames", purposeNames);
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());

        return listChartOfAcc;
    }

    public List<CChartOfAccounts> getAccountCodesListForBankEntries() {

        return getCurrentSession()
                .createQuery(
                        "select acc from CChartOfAccounts acc where acc.isActiveForPosting=true and (acc.glcode like '1%' or acc.glcode like '2%') and acc.id not in (select cd.glCodeId from CChartOfAccountDetail cd) order by acc.glcode")
                .setCacheable(true).list();

    }
}