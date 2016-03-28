/*
 * eGov suite of products aim to improve the internal efficiency,transparency, accountability and the service delivery of the
 * government organizations. Copyright (C) <2015> eGovernments Foundation The updated version of eGov suite of products as by
 * eGovernments Foundation is available at http://www.egovernments.org This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version
 * 3 of the License, or any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details. You should have received a copy of the GNU General Public License along with this program. If not,
 * see http://www.gnu.org/licenses/ or http://www.gnu.org/licenses/gpl.html . In addition to the terms of the GPL license to be
 * adhered to in using this program, the following additional terms are to be complied with: 1) All versions of this program,
 * verbatim or modified must carry this Legal Notice. 2) Any misrepresentation of the origin of the material is prohibited. It is
 * required that all modified versions of this material be marked in reasonable ways as different from the original version. 3)
 * This license does not grant any rights to any user of the program with regards to rights under trademark law for use of the
 * trade names or trademarks of eGovernments Foundation. In case of any queries, you can reach eGovernments Foundation at
 * contact@egovernments.org.
 */
package org.egov.commons.service;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgActiondetails;
import org.egov.commons.EgNumbers;
import org.egov.commons.EgPartytype;
import org.egov.commons.EgSurrenderedCheques;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Installment;
import org.egov.commons.ObjectHistory;
import org.egov.commons.ObjectType;
import org.egov.commons.Relation;
import org.egov.commons.Scheme;
import org.egov.commons.Status;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.BankbranchDAO;
import org.egov.commons.dao.CommonsDAOFactory;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.dao.ObjectTypeDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.utils.DateUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class CommonsServiceImpl implements CommonsService {

    private static final Logger LOG = LoggerFactory.getLogger(CommonsServiceImpl.class);
    private final CommonsDAOFactory commonsDAOFactory;
    private final SessionFactory sessionFactory;
    @Autowired
    public BoundaryService boundaryService;
    @Autowired
    public BoundaryTypeService boundaryTypeService;
    @Autowired
    private FundHibernateDAO fundHibernateDAO;
    
    public CommonsServiceImpl(final CommonsDAOFactory commonsDAOFactory, final SessionFactory sessionFactory) {
        this.commonsDAOFactory = commonsDAOFactory;
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Installment getInstallmentByID(final Integer installmentId) {
        return (Installment) commonsDAOFactory.getInstallmentDao().findById(installmentId, false);
    }

    @Override
    public void createInstallment(final Installment installment) {
        try {
            commonsDAOFactory.getInstallmentDao().create(installment);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in creating Installment.", e);
        }
    }

    @Override
    public void deleteInstallment(final Installment installment) {
        try {
            commonsDAOFactory.getInstallmentDao().delete(installment);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in deleting Installment.", e);
        }
    }

    @Override
    public void updateInstallment(final Installment installment) {
        try {
            commonsDAOFactory.getInstallmentDao().update(installment);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in updating Installment.", e);
        }
    }

    @Override
    public List<Installment> getInsatllmentByModule(final Module module) {
        try {
            return commonsDAOFactory.getInstallmentDao().getInsatllmentByModule(module);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching Installment by module.", e);
        }
    }

    @Override
    public List<Installment> getInsatllmentByModule(final Module module, final Date year) {
        try {
            return commonsDAOFactory.getInstallmentDao().getInsatllmentByModule(module, year);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching Installment by module and year.", e);
        }
    }

    @Override
    public Installment getInsatllmentByModule(final Module module, final Date year, final Integer installmentNumber) {
        try {
            return commonsDAOFactory.getInstallmentDao().getInsatllmentByModule(module, year, installmentNumber);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException(
                    "Exception in searching Installment by module,year and installment number.", e);
        }
    }

    @Override
    public List<Installment> getAllInstallments() {
        try {
            return commonsDAOFactory.getInstallmentDao().findAll();
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in getting all installments.", e);
        }
    }

    @Override
    public Installment getInsatllmentByModuleForGivenDate(final Module module, final Date installmentDate) {
        try {
            return commonsDAOFactory.getInstallmentDao().getInsatllmentByModuleForGivenDate(module, installmentDate);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching Installment by module for a given date.", e);
        }
    }

    @Override
    public Installment getInsatllmentByModuleForCurrDate(final Module module) {
        try {
            return commonsDAOFactory.getInstallmentDao().getInsatllmentByModuleForGivenDate(module, new Date());
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching Installment by module for current date.", e);
        }
    }

    @Override
    public String getCurrentInstallmentYear() {
        try {
            final Module module = null;// TODO migrate mdouleDao
                                       // genericHibernateDaoFactory.getModuleDao().getModuleByName(EGovConfig.getProperty("MODULE_NAME",
                                       // "", "PT"));
            final Installment instCurr = commonsDAOFactory.getInstallmentDao()
                    .getInsatllmentByModuleForGivenDate(module, DateUtils.getFinancialYear().getStartOnDate());
            final Date insYear = instCurr.getInstallmentYear();
            return new SimpleDateFormat("yyyy", Locale.ENGLISH).format(insYear);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in getting Current Installment year.", e);
        }
    }

    /**
     * This method returns the Map Width for given boundaryId
     *
     * @param bndryID
     * @return
     */
    @Override
    public Map<Integer, Integer> getWidth(final Integer bndryID) {
        final Query query = getSession().createSQLQuery("select WIDTH from EGGIS_BNDRY_DIM where BNDRYID =:bid");
        query.setInteger("bid", bndryID);
        final Object[] widths = (Object[]) query.uniqueResult();
        final Map<Integer, Integer> retMap = new TreeMap<Integer, Integer>();
        if (widths != null && widths.length != 0)
            retMap.put(bndryID, Integer.valueOf(widths[0].toString()));
        return retMap;
    }

    // This method returns the Map Height for given boundaryId
    @Override
    public Map<Integer, Integer> getHeight(final Integer bndryID) {
        final Query query = getSession().createSQLQuery("select HEIGHT from EGGIS_BNDRY_DIM where BNDRYID =:bid");
        query.setInteger("bid", bndryID);
        final Object[] heights = (Object[]) query.uniqueResult();
        final Map<Integer, Integer> retMap = new TreeMap<Integer, Integer>();
        if (heights != null && heights.length != 0)
            retMap.put(bndryID, Integer.valueOf(heights[0].toString()));
        return retMap;
    }

    @Override
    public Fund fundById(final Integer fundId) {
        return commonsDAOFactory.getFundDAO().fundById(fundId,false);
    }

    @Override
    public Fundsource fundsourceById(final Integer fundSrcId) {
        return commonsDAOFactory.getFundsourceDAO().fundsourceById(fundSrcId);
    }

    @Override
    public EgwStatus getEgwStatusById(final Integer statusId) {
        try {
            return (EgwStatus) commonsDAOFactory.getEgwStatusDAO().findById(statusId, false);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching status by status id", e);
        }
    }

    @Override
    public EgwStatus getEgwStatusByCode(final String code) throws ApplicationRuntimeException {
        try {
            return commonsDAOFactory.getEgwStatusDAO().getEgwStatusByCode(code);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching status by status Code", e);
        }
    }

    @Override
    public List<Fund> getAllFunds() {
        try {
            return commonsDAOFactory.getFundDAO().findAllActiveFunds();
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching Funds", e);
        }
    }

    @Override
    public List<Fund> getAllActiveIsLeafFunds() {
        try {
            return commonsDAOFactory.getFundDAO().findAllActiveIsLeafFunds();
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching active and is leaf Funds", e);
        }
    }

    @Override
    public List<Fundsource> getAllFundSource() throws ApplicationRuntimeException {
        try {
            return commonsDAOFactory.getFundsourceDAO().findAll();
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching Fundsource", e);
        }
    }

    @Override
    public List<EgActiondetails> getEgActiondetailsFilterBy(final String moduleId, final ArrayList<String> actionType,
            final String moduleType) {
        try {
            return commonsDAOFactory.getEgActiondetailsDAO().getEgActiondetailsFilterBy(moduleId, actionType,
                    moduleType);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching Actiondetails", e);
        }
    }

    @Override
    public EgActiondetails getEgActiondetailsByWorksdetailId(final String moduleId, final String actionType,
            final String moduleType) {
        try {
            return commonsDAOFactory.getEgActiondetailsDAO().getEgActiondetailsByWorksdetailId(moduleId, actionType,
                    moduleType);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching ActiondetailsByWorksdetailId", e);
        }
    }

    @Override
    public void createEgActiondetails(final EgActiondetails egActiondetails) {
        try {
            commonsDAOFactory.getEgActiondetailsDAO().create(egActiondetails);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in creating Action Details ", e);
        }
    }

    @Override
    public void updateEgActiondetails(final EgActiondetails egActiondetails) {
        try {
            commonsDAOFactory.getEgActiondetailsDAO().update(egActiondetails);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in Updating   Action details", e);
        }
    }

    @Override
    public void createEgwSatuschange(final EgwSatuschange egwSatuschange) {
        commonsDAOFactory.getEgwSatuschangeDAO().create(egwSatuschange);
    }

    @Override
    public Fundsource getFundSourceById(final Integer fundSourceId) {
        return (Fundsource) commonsDAOFactory.getFundsourceDAO().findById(fundSourceId, false);
    }

    @Override
    public Fund getFundById(final Integer fundId) {
        return (Fund) commonsDAOFactory.getFundDAO().fundById(fundId, false);
    }   

    /*
     * @Override public List<EgUom> findAllUom() { return commonsDAOFactory.getEgUomDAO().findAllUom(); }
     */

  
    /*
     * @Override public EgUom getUomById(final Integer uomId) { return (EgUom) commonsDAOFactory.getEgUomDAO().findById(uomId,
     * false); }
     */

    /**
     * @param moduleType Module type
     * @return EgwStatus object for given module type and status code
     */
    @Override
    public EgwStatus getStatusByModuleAndCode(final String moduleType, final String description) {
        return commonsDAOFactory.getEgwStatusDAO().getStatusByModuleAndCode(moduleType, description);
    }

    @Override
    public List<EgwStatus> getEgwStatusFilterByStatus(final ArrayList<Integer> statusId) {
        return commonsDAOFactory.getEgwStatusDAO().getEgwStatusFilterByStatus(statusId);
    }

    @Override
    public List<EgActiondetails> getEgActiondetailsFilterBy(final ArrayList<String> actionType,
            final String moduleType) {
        return commonsDAOFactory.getEgActiondetailsDAO().getEgActiondetailsFilterBy(actionType, moduleType);
    }

    @Override
    public List<Status> getStatusByModuleType(final String moduleType) {
        return commonsDAOFactory.getStatusDAO().getStatusByModuleType(moduleType);
    }

    @Override
    public void createAccountdetailkey(final Accountdetailkey accountdetailkey) {
        commonsDAOFactory.getAccountdetailkeyDAO().create(accountdetailkey);
    }

    @Override
    public Status findEgInvStatusById(final Integer statusId) {
        return (Status) commonsDAOFactory.getStatusDAO().findById(statusId, false);
    }

    @Override
    public EgwStatus findEgwStatusById(final Integer statusId) {
        return (EgwStatus) commonsDAOFactory.getEgwStatusDAO().findById(statusId, false);
    }

    @Override
    public List<CFinancialYear> getAllFinancialYearList() {
        return commonsDAOFactory.getFinancialYearDAO().findAll();
    }

    @Override
    public CFinancialYear findFinancialYearById(final Long finYrId) {
        try {
            return (CFinancialYear) commonsDAOFactory.getFinancialYearDAO().findById(finYrId, false);
        } catch (final RuntimeException e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public CFunction getCFunctionById(final Long functionId) {
        return (CFunction) commonsDAOFactory.getFunctionDAO().findById(functionId, false);
    }

    @Override
    public List<CFunction> getAllFunction() {
        return commonsDAOFactory.getFunctionDAO().getAllActiveFunctions();
    }

    @Override
    public CChartOfAccounts findGlCodeById(final String glcodeid) {
        return null;
    }

    @Override
    public CChartOfAccounts getCChartOfAccountsById(final Long chartOfAccountsId) {
        return (CChartOfAccounts) commonsDAOFactory.getChartOfAccountsDAO().findById(chartOfAccountsId, false);
    }

    @Override
    public CFinancialYear getFinancialYearByDate(final Date date) {
       final FinancialYearDAO finYearDAO = commonsDAOFactory.getFinancialYearDAO();
        return finYearDAO.getFinancialYearByDate(date);
    }

    @Override
    public List<CGeneralLedger> getGeneralLedgerList(final Long voucherHeaderId) {
        try {
            return commonsDAOFactory.getGeneralLedgerDAO().findCGeneralLedgerByVoucherHeaderId(voucherHeaderId);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Error occurred while getting GL", e);
        }
    }

    @Override
    public CChartOfAccounts getCChartOfAccountsByGlCode(final String glCode) {
        return commonsDAOFactory.getChartOfAccountsDAO().getCChartOfAccountsByGlCode(glCode);
    }

    @Override
    public Collection<CFunction> getFunctionList() {
        return commonsDAOFactory.getFunctionDAO().findAll();
    }

    @Override
    public CVoucherHeader findVoucherHeaderById(final Long vouchdrId) {
        return (CVoucherHeader) commonsDAOFactory.getVoucherHeaderDAO().findById(vouchdrId, false);
    }

    @Override
    public CFunction findFunctionById(final Long functionId) {
        return (CFunction) commonsDAOFactory.getFunctionDAO().findById(functionId, false);
    }

    @Override
    public Collection<CFinancialYear> getFinancialYearList() {
        return commonsDAOFactory.getFinancialYearDAO().findAll();
    }

    /**
     * This API will return the transaction no for any type of txn. Input :Type,transaction date and connection Output
     * :Transaction number in the format txnType+number+/+month+/+year
     */
    @Override
    public String getTxnNumber(final String txnType, final String vDate, final Connection con)
            throws ParseException, SQLException {
        final String txndate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                .format(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(vDate));
        final Query query = getSession().createSQLQuery(
                "select a.FINANCIALYEAR,b.id from FINANCIALYEAR a,fiscalperiod b  where a.id=b.financialyearid AND ? between b.startingdate and b.endingdate");
        query.setString(0, txndate);
        final Object[] result = (Object[]) query.uniqueResult();
        String finYear = "";
        String fiscalPeriod = "";
        if (result == null || result.length == 0)
            throw new ApplicationRuntimeException("Year is not defined in the system");
        else {
            finYear = result[0].toString();
            fiscalPeriod = result[1].toString();
        }
        EgNumbers egnum = commonsDAOFactory.getEgNumbersHibernateDAO()
                .getEgNumberByFiscalPeriodAndVouchertype(fiscalPeriod, txnType);
        String runningNumber = "";
        if (egnum == null) {
            egnum = new EgNumbers();
            runningNumber = "1";
            runningNumber = String.valueOf(Integer.parseInt(runningNumber));
            egnum.setVouchernumber(new BigDecimal(runningNumber));
            egnum.setVouchertype(txnType);
            egnum.setFiscialperiodid(new BigDecimal(fiscalPeriod));
            commonsDAOFactory.getEgNumbersHibernateDAO().create(egnum);
        } else {
            runningNumber = String.valueOf(egnum.getVouchernumber().intValue() + 1);
            egnum.setVouchernumber(new BigDecimal(runningNumber));
            commonsDAOFactory.getEgNumbersHibernateDAO().update(egnum);
        }
        return txnType + runningNumber + "/" + vDate.split("/")[1] + "/" + finYear.substring(2, 4)
                + finYear.substring(finYear.length() - 2, finYear.length());
    }

    /**
     * This method returns the active and is active for posting Account records having classification as '4' , for a given type.
     *
     * @param type
     * @return
     */
    @Override
    public List<CChartOfAccounts> getActiveAccountsForType(final char type) throws ApplicationException {
        return commonsDAOFactory.getChartOfAccountsDAO().getActiveAccountsForType(type);
    }

    /**
     * This method returns all active functionary records.
     *
     * @return
     */
    @Override
    public List<Functionary> getActiveFunctionaries() {
        return commonsDAOFactory.getFunctionaryDAO().findAllActiveFunctionary();
    }

    /**
     * This function returns the system date of the database server.
     *
     * @param connection
     * @return @
     */
    @Override
    public String getCurrentDate(final Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultset = null;
        String currentDate = null;
        try {
            statement = connection
                    .prepareStatement("select to_char(sysdate,'dd/MM/yyyy') as \"currentDate\" from dual");
            resultset = statement.executeQuery();
            resultset.next();
            currentDate = resultset.getString("currentDate");
        } catch (final Exception sqlex) {
            throw new SQLException(sqlex.getMessage());
        } finally {
            if (resultset != null)
                resultset.close();
            if (statement != null)
                statement.close();
        }
        return currentDate;
    }

    @Override
    public CFinancialYear getFinancialYearByFinYearRange(final String finYearRange) {
        return commonsDAOFactory.getFinancialYearDAO().getFinancialYearByFinYearRange(finYearRange);
    }

    @Override
    public List<CFinancialYear> getAllActiveFinancialYearList() {
        return commonsDAOFactory.getFinancialYearDAO().getAllActiveFinancialYearList();
    }

    @Override
    public List<CFinancialYear> getAllActivePostingFinancialYear() {
        return commonsDAOFactory.getFinancialYearDAO().getAllActivePostingFinancialYear();
    }

    @Override
    public List<EgwTypeOfWork> getAllTypeOfWork() {
        return commonsDAOFactory.getEgwTypeOfWorkDAO().getAllTypeOfWork();
    }

    @Override
    public List<EgwTypeOfWork> getAllParentOrderByCode() {
        return commonsDAOFactory.getEgwTypeOfWorkDAO().getAllParentOrderByCode();
    }

    @Override
    public EgwTypeOfWork getTypeOfWorkById(final Long workTypeId) {
        return commonsDAOFactory.getEgwTypeOfWorkDAO().getTypeOfWorkById(workTypeId);
    }

    @Override
    public EgwTypeOfWork findByCode(final String code) {
        return commonsDAOFactory.getEgwTypeOfWorkDAO().findByCode(code);
    }

    @Override
    public void createEgwTypeOfWork(final EgwTypeOfWork egwTypeOfWork) {
        commonsDAOFactory.getEgwTypeOfWorkDAO().create(egwTypeOfWork);
    }

    @Override
    public void updateEgwTypeOfWork(final EgwTypeOfWork egwTypeOfWork) {
        commonsDAOFactory.getEgwTypeOfWorkDAO().update(egwTypeOfWork);
    }

    @Override
    public List<EgwTypeOfWork> getTypeOfWorkDetailFilterBy(final String code, final String parentCode,
            final String description) {
        return commonsDAOFactory.getEgwTypeOfWorkDAO().getTypeOfWorkDetailFilterBy(code, parentCode, description);
    }

    @Override
    public List<EgwTypeOfWork> getTypeOfWorkDetailFilterByParty(final String code, final String parentCode,
            final String description, final String partyTypeCode) {
        return commonsDAOFactory.getEgwTypeOfWorkDAO().getTypeOfWorkDetailFilterByParty(code, parentCode, description,
                partyTypeCode);
    }

    @Override
    public List<EgPartytype> getPartyTypeDetailFilterBy(final String code, final String parentCode,
            final String description) {
        return commonsDAOFactory.getEgPartytypeDAO().getPartyTypeDetailFilterBy(code, parentCode, description);
    }

    @Override
    public String getCurrYearFiscalId() {
        return commonsDAOFactory.getFinancialYearDAO().getCurrYearFiscalId();
    }

    @Override
    public String getCurrYearStartDate() {
        return commonsDAOFactory.getFinancialYearDAO().getCurrYearStartDate();
    }

    @Override
    public String getPrevYearFiscalId() {
        return commonsDAOFactory.getFinancialYearDAO().getPrevYearFiscalId();
    }

    @Override
    public List<EgwStatus> getStatusByModule(final String moduleType) {
        return commonsDAOFactory.getEgwStatusDAO().getStatusByModule(moduleType);
    }

    @Override
    public EgPartytype getPartytypeById(final Integer partyTypeId) {
        return (EgPartytype) commonsDAOFactory.getEgPartytypeDAO().findById(partyTypeId, false);
    }

    @Override
    public void createEgPartytype(final EgPartytype egPartytype) {
        try {
            commonsDAOFactory.getEgPartytypeDAO().create(egPartytype);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in creating party type", e);
        }
    }

    @Override
    public void updateEgPartytype(final EgPartytype egPartytype) {
        commonsDAOFactory.getEgPartytypeDAO().update(egPartytype);
    }

    @Override
    public List<EgPartytype> findAllPartyTypeChild() {
        return commonsDAOFactory.getEgPartytypeDAO().findAllPartyTypeChild();
    }

    @Override
    public List<EgwTypeOfWork> findAllParentPartyType() {
        return commonsDAOFactory.getEgwTypeOfWorkDAO().findAllParentPartyType();
    }

    @Override
    public List<EgwTypeOfWork> findAllChildPartyType() {
        return commonsDAOFactory.getEgwTypeOfWorkDAO().findAllChildPartyType();
    }

    @Override
    public List<CVoucherHeader> getVoucherHeadersByStatus(final Integer status) {
        try {
            return commonsDAOFactory.getVoucherHeaderDAO().getVoucherHeadersByStatus(status);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Error occurred while getting Voucher Header by Status", e);
        }
    }

    @Override
    public List<CVoucherHeader> getVoucherHeadersByStatusAndType(final Integer status, final String type) {
        try {
            return commonsDAOFactory.getVoucherHeaderDAO().getVoucherHeadersByStatusAndType(status, type);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Error occurred while getting Voucher Header by Status and Type", e);
        }
    }

    /*
     * @Override public EgUom getUomByUom(final String uom) { EgUom egUom = null; if (uom != null) { final Query qry =
     * getSession().createQuery( "from EgUom uom where uom.uom=:uom"); qry.setString("uom", uom); egUom = (EgUom)
     * qry.uniqueResult(); } return egUom; }
     */

    
    public Bankaccount getBankaccountById(final Long id) {
        return null;
    }

    
    @Override
    public EgPartytype getPartytypeByCode(final String code) {
        return commonsDAOFactory.getEgPartytypeDAO().getPartytypeByCode(code);
    }

    @Override
    public String getAccountdetailtypeAttributename(final Connection connection, final String name)
            throws SQLException {
        final Query query = getSession().createSQLQuery("select id,attributename from accountdetailtype where name=?");
        query.setString(0, name);
        final Object[] result = (Object[]) query.uniqueResult();
        return result == null || result.length == 0 ? "" : result[0] + "#" + result[1];
    }

    @Override
    public List<Bankbranch> getAllBankBranchs() {
        return commonsDAOFactory.getBankbranchDAO().getAllBankBranchs();
    }

    @Override
    public List<Bankaccount> getAllBankAccounts() {
        return commonsDAOFactory.getBankaccountDAO().getAllBankAccounts();
    }

    @Override
    public Bank getBankById(final Integer id) {
        return (Bank) commonsDAOFactory.getBankDAO().findById(id, false);
    }

    @Override
    public ObjectType getObjectTypeByType(final String type) {
        return new ObjectTypeDAO(sessionFactory).getObjectType(type);
    }

    @Override
    public ObjectType getObjectTypeById(final Integer type) {
        return new ObjectTypeDAO(sessionFactory).getObjectType(type);
    }

    @Override
    public Bankbranch getBankbranchById(final Integer id) {
        return new BankbranchDAO(sessionFactory).getBankbranchById(id);
    }

    @Override
    public ObjectHistory createObjectHistory(final ObjectHistory objhistory) {
        return (ObjectHistory) commonsDAOFactory.getObjectHistoryDAO().create(objhistory);
    }

    @Override
    public String getCBillDeductionAmtByVhId(final Long voucherHeaderId) {
        try {
            return commonsDAOFactory.getGeneralLedgerDAO().getCBillDeductionAmtByVhId(voucherHeaderId);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Exception in searching CBillDeductionAmtByVhId" + e.getMessage(), e);
        }
    }

    @Override
    public Vouchermis getVouchermisByVhId(final Integer vhId) {
        return commonsDAOFactory.getVouchermisDAO().getVouchermisByVhId(vhId);
    }

    @Override
    public CVoucherHeader getVoucherHeadersByCGN(final String cgn) {
        return commonsDAOFactory.getVoucherHeaderDAO().getVoucherHeadersByCGN(cgn);
    }

    /**
     * @param moduleType Module type
     * @param codeList List of status codes
     * @return List of all EgwStatus objects filtered by given module type and list of status codes
     */
    @Override
    public List<EgwStatus> getStatusListByModuleAndCodeList(final String moduleType, final List codeList) {
        return commonsDAOFactory.getEgwStatusDAO().getStatusListByModuleAndCodeList(moduleType, codeList);
    }

    @Override
    public Functionary getFunctionaryById(final Integer id) {
        return commonsDAOFactory.getFunctionaryDAO().functionaryById(id);
    }

    @Override
    public CFunction getFunctionByCode(final String code) {
        return commonsDAOFactory.getFunctionDAO().getFunctionByCode(code);
    }

    @Override
    public List<CChartOfAccounts> getAccountCodeByPurpose(final Integer purposeId) throws ApplicationException {
        return commonsDAOFactory.getChartOfAccountsDAO().getAccountCodeByPurpose(purposeId);
    }

    @Override
    public List<CChartOfAccounts> getDetailedAccountCodeList() throws ApplicationException {
        return commonsDAOFactory.getChartOfAccountsDAO().getDetailedAccountCodeList();
    }

    @Override
    public Accountdetailtype getAccountDetailTypeIdByName(final String glCode, final String name) {
        try {
            return commonsDAOFactory.getChartOfAccountsDAO().getAccountDetailTypeIdByName(glCode, name);
        } catch (final Exception e) {
            LOG.error(e.getMessage());
            throw new ApplicationRuntimeException("Error occurred while getting Account Detail Type ID by Name", e);
        }
    }

    @Override
    public List<Fundsource> getAllActiveIsLeafFundSources() throws ApplicationException {
        return commonsDAOFactory.getFundsourceDAO().findAllActiveIsLeafFundSources();
    }

    @Override
    public Scheme getSchemeById(final Integer id) throws ApplicationException {
        return commonsDAOFactory.getSchemeDAO().getSchemeById(id);
    }

    @Override
    public SubScheme getSubSchemeById(final Integer id) throws ApplicationException {
        return commonsDAOFactory.getSubSchemeDAO().getSubSchemeById(id);
    }

    @Override
    public CFinancialYear getFinancialYearById(final Long id) {
        return commonsDAOFactory.getFinancialYearDAO().getFinancialYearById(id);
    }

    @Override
    public CFunction getFunctionById(final Long Id) {
        return commonsDAOFactory.getFunctionDAO().getFunctionById(Id);
    }

    @Override
    public Integer getDetailtypeforObject(final Object master) throws ApplicationException {
        return commonsDAOFactory.getaccountdetailtypeHibernateDAO().getDetailtypeforObject(master);
    }

    @Override
    public List<Accountdetailtype> getDetailTypeListByGlCode(final String glCode) throws ApplicationException {
        return commonsDAOFactory.getChartOfAccountsDAO().getAccountdetailtypeListByGLCode(glCode);
    }

    @Override
    public Fund fundByCode(final String fundCode) {
        return fundHibernateDAO.fundByCode(fundCode);
    }

    @Override
    public Scheme schemeByCode(final String code) {
        return commonsDAOFactory.getSchemeDAO().getSchemeByCode(code);
    }

    @Override
    public Fundsource getFundSourceByCode(final String code) {
        return commonsDAOFactory.getFundsourceDAO().getFundSourceByCode(code);
    }

    @Override
    public SubScheme getSubSchemeByCode(final String code) {
        return commonsDAOFactory.getSubSchemeDAO().getSubSchemeByCode(code);
    }

    @Override
    public Bank getBankByCode(final String bankCode) {
        return commonsDAOFactory.getBankDAO().getBankByCode(bankCode);
    }

    @Override
    public Bankaccount getBankAccountByAccBranchBank(final String bankAccNum, final String bankBranchCode,
            final String bankCode) {
        return commonsDAOFactory.getBankaccountDAO().getBankAccountByAccBranchBank(bankAccNum, bankBranchCode,
                bankCode);
    }

    @Override
    public Functionary getFunctionaryByCode(final BigDecimal code) {
        return commonsDAOFactory.getFunctionaryDAO().getFunctionaryByCode(code);
    }

    @Override
    public List<Accountdetailtype> getAccountdetailtypeListByGLCode(final String glCode) throws ApplicationException {
        return commonsDAOFactory.getChartOfAccountsDAO().getAccountdetailtypeListByGLCode(glCode);
    }

    @Override
    public List<CChartOfAccounts> getActiveAccountsForTypes(final char[] type) throws ValidationException {
        return commonsDAOFactory.getChartOfAccountsDAO().getActiveAccountsForTypes(type);
    }

    @Override
    public List<CChartOfAccounts> getAccountCodeByListOfPurposeId(final Integer[] purposeId)
            throws ValidationException {
        return commonsDAOFactory.getChartOfAccountsDAO().getAccountCodeByListOfPurposeId(purposeId);
    }

    @Override
    public List<CChartOfAccounts> getListOfDetailCode(final String glCode) throws ValidationException {
        return commonsDAOFactory.getChartOfAccountsDAO().getListOfDetailCode(glCode);
    }

    /*
     * @Override public List<EgUom> getAllUomsWithinCategoryByUom(final Integer uomId) throws ValidationException { return
     * commonsDAOFactory.getEgUomDAO().getAllUomsWithinCategoryByUom(uomId); }
     * @Override public BigDecimal getConversionFactorByUom(final Integer uomId) throws ValidationException { return
     * commonsDAOFactory.getEgUomDAO().getConversionFactorByUom(uomId); }
     * @Override public BigDecimal getConversionFactorByFromUomToUom(final Integer fromuomId, final Integer touomId) throws
     * ValidationException { return commonsDAOFactory.getEgUomDAO().getConversionFactorByFromUomToUom( fromuomId , touomId); }
     */
    @Override
    public List<EgPartytype> getSubPartyTypes(final String code) {
        return commonsDAOFactory.getEgPartytypeDAO().getSubPartyTypesForCode(code);
    }

    @Override
    public Functionary getFunctionaryByName(final String name) {
        return commonsDAOFactory.getFunctionaryDAO().getFunctionaryByName(name);
    }

    @Override
    public Long getBndryIdFromShapefile(final Double latitude, final Double longitude) {
        try {
            Long boundaryId = 0L;
            if (latitude != null && longitude != null) {
                final Map<String, URL> map = new HashMap<String, URL>();
                map.put("url", Thread.currentThread().getContextClassLoader()
                        .getResource("gis/" + EgovThreadLocals.getTenantID() + "/wards.shp"));
                final DataStore dataStore = DataStoreFinder.getDataStore(map);
                final FeatureCollection<SimpleFeatureType, SimpleFeature> collection = dataStore
                        .getFeatureSource(dataStore.getTypeNames()[0]).getFeatures();
                final Iterator<SimpleFeature> iterator = collection.iterator();
                final Point point = JTSFactoryFinder.getGeometryFactory(null)
                        .createPoint(new Coordinate(longitude, latitude));
                LOG.debug("Fetching boundary data for coordinates lng {}, lat {}", longitude, latitude);
                try {
                    while (iterator.hasNext()) {
                        final SimpleFeature feature = iterator.next();
                        final Geometry geom = (Geometry) feature.getDefaultGeometry();
                        if (geom.contains(point)) {
                            LOG.debug("Found coordinates in shape file");
                            final Long boundaryNum = (Long) feature.getAttribute("bndrynum");
                            final String bndryType = (String) feature.getAttribute("bndrytype");
                            LOG.debug("Got boundary number {} and boundary type {} from GIS", boundaryNum, bndryType);
                            if (boundaryNum != null && StringUtils.isNotBlank(bndryType)) {
                                final BoundaryType boundaryType = boundaryTypeService
                                        .getBoundaryTypeByNameAndHierarchyTypeName(bndryType, "ADMINISTRATION");
                                final Boundary boundary = boundaryService.getBoundaryByTypeAndNo(boundaryType,
                                        boundaryNum);
                                if (boundary != null && true)
                                    boundaryId = boundary.getId();
                                else {
                                    final BoundaryType cityBoundaryType = boundaryTypeService
                                            .getBoundaryTypeByNameAndHierarchyTypeName("City", "ADMINISTRATION");
                                    final Boundary cityBoundary = boundaryService
                                            .getAllBoundariesByBoundaryTypeId(cityBoundaryType.getId()).get(0);
                                    boundaryId = cityBoundary.getId();
                                }

                            }
                            break;
                        }
                    }
                } finally {
                    collection.close(iterator);
                }
            }
            LOG.debug("Found boundary data in GIS with boundary id : {}", boundaryId);
            return boundaryId;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occurred while fetching boundary from GIS data", e);
        }
    }

    @Override
    public CFinancialYear getFinYearByDate(final Date date) {
        final FinancialYearDAO finYearDAO = commonsDAOFactory.getFinancialYearDAO();
        return finYearDAO.getFinYearByDate(date);
    }

    @Override
    public Accountdetailtype getAccountDetailTypeByName(final String name) {
        return commonsDAOFactory.getaccountdetailtypeHibernateDAO().getAccountdetailtypeByName(name);
    }

    @Override
    public Relation getRelationById(Integer relationId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bankaccount getBankaccountById(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createEgSurrenderedCheques(EgSurrenderedCheques egSurrenderedCheques) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateEgSurrenderedCheques(EgSurrenderedCheques egSurrenderedCheques) {
        // TODO Auto-generated method stub
        
    }

}
