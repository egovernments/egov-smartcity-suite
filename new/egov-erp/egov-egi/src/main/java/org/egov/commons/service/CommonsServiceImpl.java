/*
 * @(#)CommonsServiceImpl.java 3.0, 14 Jun, 2013 12:07:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

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
import org.egov.commons.Chequedetail;
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
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.ObjectTypeDAO;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.FinancialYear;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.citizen.model.Owner;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonsServiceImpl implements CommonsService {

	private static final Logger LOG = LoggerFactory.getLogger(CommonsServiceImpl.class);

	@Override
	public Installment getInstallmentByID(final Integer installmentId) {
		return (Installment) CommonsDaoFactory.getDAOFactory().getInstallmentDao().findById(installmentId, false);
	}

	@Override
	public void createInstallment(final Installment installment) {
		try {
			CommonsDaoFactory.getDAOFactory().getInstallmentDao().create(installment);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in creating Installment.", e);
		}
	}

	@Override
	public void deleteInstallment(final Installment installment) {
		try {
			CommonsDaoFactory.getDAOFactory().getInstallmentDao().delete(installment);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in deleting Installment.", e);
		}
	}

	@Override
	public void updateInstallment(final Installment installment) {
		try {
			CommonsDaoFactory.getDAOFactory().getInstallmentDao().update(installment);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in updating Installment.", e);
		}
	}

	@Override
	public List<Installment> getInsatllmentByModule(final Module module) {
		try {
			return CommonsDaoFactory.getDAOFactory().getInstallmentDao().getInsatllmentByModule(module);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching Installment by module.", e);
		}
	}

	@Override
	public List<Installment> getInsatllmentByModule(final Module module, final Date year) {
		try {
			return CommonsDaoFactory.getDAOFactory().getInstallmentDao().getInsatllmentByModule(module, year);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching Installment by module and year.", e);
		}
	}

	@Override
	public Installment getInsatllmentByModule(final Module module, final Date year, final Integer installmentNumber) {
		try {
			return CommonsDaoFactory.getDAOFactory().getInstallmentDao().getInsatllmentByModule(module, year, installmentNumber);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching Installment by module,year and installment number.", e);
		}
	}

	@Override
	public List<Installment> getAllInstallments() {
		try {
			return CommonsDaoFactory.getDAOFactory().getInstallmentDao().findAll();
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in getting all installments.", e);
		}
	}

	@Override
	public Installment getInsatllmentByModuleForGivenDate(final Module module, final Date installmentDate) {
		try {
			return CommonsDaoFactory.getDAOFactory().getInstallmentDao().getInsatllmentByModuleForGivenDate(module, installmentDate);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching Installment by module for a given date.", e);
		}
	}

	@Override
	public Installment getInsatllmentByModuleForCurrDate(final Module module) {
		try {
			return CommonsDaoFactory.getDAOFactory().getInstallmentDao().getInsatllmentByModuleForGivenDate(module, new Date());
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching Installment by module for current date.", e);
		}
	}

	@Override
	public String getCurrentInstallmentYear() {
		try {
			final Module module = GenericDaoFactory.getDAOFactory().getModuleDao().getModuleByName(EGovConfig.getProperty("MODULE_NAME", "", "PT"));
			final Installment instCurr = CommonsDaoFactory.getDAOFactory().getInstallmentDao().getInsatllmentByModuleForGivenDate(module, DateUtils.getFinancialYear().getStartOnDate());
			final Date insYear = instCurr.getInstallmentYear();
			return new SimpleDateFormat("yyyy", Locale.ENGLISH).format(insYear);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in getting Current Installment year.", e);
		}
	}

	/**
	 * This method returns the Map Width for given boundaryId
	 * @param bndryID
	 * @return
	 */
	@Override
	public Map<Integer, Integer> getWidth(final Integer bndryID) {
		final Query query = HibernateUtil.getCurrentSession().createSQLQuery("select WIDTH from EGGIS_BNDRY_DIM where BNDRYID =:bid");
		query.setInteger("bid", bndryID);
		final Object[] widths = (Object[]) query.uniqueResult();
		final Map<Integer, Integer> retMap = new TreeMap<Integer, Integer>();
		if ((widths != null) && (widths.length != 0)) {
			retMap.put(bndryID, Integer.valueOf(widths[0].toString()));
		}
		return retMap;
	}

	// This method returns the Map Height for given boundaryId
	@Override
	public Map<Integer, Integer> getHeight(final Integer bndryID) {
		final Query query = HibernateUtil.getCurrentSession().createSQLQuery("select HEIGHT from EGGIS_BNDRY_DIM where BNDRYID =:bid");
		query.setInteger("bid", bndryID);
		final Object[] heights = (Object[]) query.uniqueResult();
		final Map<Integer, Integer> retMap = new TreeMap<Integer, Integer>();
		if ((heights != null) && (heights.length != 0)) {
			retMap.put(bndryID, Integer.valueOf(heights[0].toString()));
		}
		return retMap;
	}

	@Override
	public Owner getOwnerByID(final Integer ownerID) {
		try {
			return (Owner) CommonsDaoFactory.getDAOFactory().getOwnerDao().findById(ownerID, false);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching Owner.", e);
		}
	}

	@Override
	public Fund fundById(final Integer fundId) {
		return CommonsDaoFactory.getDAOFactory().getFundDAO().fundById(fundId);
	}

	@Override
	public Fundsource fundsourceById(final Integer fundSrcId) {
		return CommonsDaoFactory.getDAOFactory().getFundsourceDAO().fundsourceById(fundSrcId);
	}

	@Override
	public EgwStatus getEgwStatusById(final Integer statusId) {
		try {
			return (EgwStatus) CommonsDaoFactory.getDAOFactory().getEgwStatusDAO().findById(statusId, false);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching status by status id", e);
		}
	}

	@Override
	public EgwStatus getEgwStatusByCode(final String code) throws EGOVRuntimeException {
		try {
			return CommonsDaoFactory.getDAOFactory().getEgwStatusDAO().getEgwStatusByCode(code);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching status by status Code", e);
		}
	}

	@Override
	public List<Fund> getAllFunds() {
		try {
			return CommonsDaoFactory.getDAOFactory().getFundDAO().findAllActiveFunds();
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching Funds", e);
		}
	}

	@Override
	public List<Fund> getAllActiveIsLeafFunds() {
		try {
			return CommonsDaoFactory.getDAOFactory().getFundDAO().findAllActiveIsLeafFunds();
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching active and is leaf Funds", e);
		}
	}

	@Override
	public List<Fundsource> getAllFundSource() throws EGOVRuntimeException {
		try {
			return CommonsDaoFactory.getDAOFactory().getFundsourceDAO().findAll();
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching Fundsource", e);
		}
	}

	@Override
	public List<EgActiondetails> getEgActiondetailsFilterBy(final String moduleId, final ArrayList<String> actionType, final String moduleType) {
		try {
			return CommonsDaoFactory.getDAOFactory().getEgActiondetailsDAO().getEgActiondetailsFilterBy(moduleId, actionType, moduleType);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching Actiondetails", e);
		}
	}

	@Override
	public EgActiondetails getEgActiondetailsByWorksdetailId(final String moduleId, final String actionType, final String moduleType) {
		try {
			return CommonsDaoFactory.getDAOFactory().getEgActiondetailsDAO().getEgActiondetailsByWorksdetailId(moduleId, actionType, moduleType);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching ActiondetailsByWorksdetailId", e);
		}
	}

	@Override
	public void createEgActiondetails(final EgActiondetails egActiondetails) {
		try {
			CommonsDaoFactory.getDAOFactory().getEgActiondetailsDAO().create(egActiondetails);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in creating Action Details ", e);
		}
	}

	@Override
	public void updateEgActiondetails(final EgActiondetails egActiondetails) {
		try {
			CommonsDaoFactory.getDAOFactory().getEgActiondetailsDAO().update(egActiondetails);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in Updating   Action details", e);
		}
	}

	@Override
	public void createEgwSatuschange(final EgwSatuschange egwSatuschange) {
		CommonsDaoFactory.getDAOFactory().getEgwSatuschangeDAO().create(egwSatuschange);
	}

	@Override
	public Fundsource getFundSourceById(final Integer fundSourceId) {
		return (Fundsource) CommonsDaoFactory.getDAOFactory().getFundsourceDAO().findById(fundSourceId, false);
	}

	@Override
	public Fund getFundById(final Integer fundId) {
		return (Fund) CommonsDaoFactory.getDAOFactory().getFundDAO().findById(fundId, false);
	}

	@Override
	public List<EgUom> findAllUom() {
		return CommonsDaoFactory.getDAOFactory().getEgUomDAO().findAllUom();
	}

	@Override
	public Relation getRelationById(final Integer relationId) {
		return (Relation) CommonsDaoFactory.getDAOFactory().getRelationDAO().findById(relationId, false);
	}

	@Override
	public EgUom getUomById(final Integer uomId) {
		return (EgUom) CommonsDaoFactory.getDAOFactory().getEgUomDAO().findById(uomId, false);
	}

	/**
	 * @param moduleType Module type
	 * @param statusCode Status code
	 * @return EgwStatus object for given module type and status code
	 */
	@Override
	public EgwStatus getStatusByModuleAndCode(final String moduleType, final String description) {
		return CommonsDaoFactory.getDAOFactory().getEgwStatusDAO().getStatusByModuleAndCode(moduleType, description);
	}

	@Override
	public List<EgwStatus> getEgwStatusFilterByStatus(final ArrayList<Integer> statusId) {
		return CommonsDaoFactory.getDAOFactory().getEgwStatusDAO().getEgwStatusFilterByStatus(statusId);
	}

	@Override
	public List<EgActiondetails> getEgActiondetailsFilterBy(final ArrayList<String> actionType, final String moduleType) {
		return CommonsDaoFactory.getDAOFactory().getEgActiondetailsDAO().getEgActiondetailsFilterBy(actionType, moduleType);
	}

	@Override
	public List<Status> getStatusByModuleType(final String moduleType) {
		return CommonsDaoFactory.getDAOFactory().getStatusDAO().getStatusByModuleType(moduleType);
	}

	@Override
	public void createAccountdetailkey(final Accountdetailkey accountdetailkey) {
		CommonsDaoFactory.getDAOFactory().getAccountdetailkeyDAO().create(accountdetailkey);
	}

	@Override
	public Status findEgInvStatusById(final Integer statusId) {
		return (Status) CommonsDaoFactory.getDAOFactory().getStatusDAO().findById(statusId, false);
	}

	@Override
	public EgwStatus findEgwStatusById(final Integer statusId) {
		return (EgwStatus) CommonsDaoFactory.getDAOFactory().getEgwStatusDAO().findById(statusId, false);
	}

	@Override
	public List<CFinancialYear> getAllFinancialYearList() {
		return CommonsDaoFactory.getDAOFactory().getFinancialYearDAO().findAll();
	}

	@Override
	public CFinancialYear findFinancialYearById(final Long finYrId) {
		try {
			return (CFinancialYear) CommonsDaoFactory.getDAOFactory().getFinancialYearDAO().findById(finYrId, false);
		} catch (final RuntimeException e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public CFunction getCFunctionById(final Long functionId) {
		return (CFunction) CommonsDaoFactory.getDAOFactory().getFunctionDAO().findById(functionId, false);
	}

	@Override
	public List<CFunction> getAllFunction() {
		return CommonsDaoFactory.getDAOFactory().getFunctionDAO().getAllActiveFunctions();
	}

	@Override
	public CChartOfAccounts findGlCodeById(final String glcodeid) {
		return (CChartOfAccounts) CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().findById(glcodeid, false);
	}

	@Override
	public CChartOfAccounts getCChartOfAccountsById(final Long chartOfAccountsId) {
		return (CChartOfAccounts) CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().findById(chartOfAccountsId, false);
	}

	@Override
	public CFinancialYear getFinancialYearByDate(final Date date) {
		final FinancialYearDAO finYearDAO = CommonsDaoFactory.getDAOFactory().getFinancialYearDAO();
		return finYearDAO.getFinancialYearByDate(date);
	}

	@Override
	public List<CGeneralLedger> getGeneralLedgerList(final Long voucherHeaderId) {
		try {
			return CommonsDaoFactory.getDAOFactory().getGeneralLedgerDAO().findCGeneralLedgerByVoucherHeaderId(voucherHeaderId);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Error occurred while getting GL", e);
		}
	}

	@Override
	public CChartOfAccounts getCChartOfAccountsByGlCode(final String glCode) {
		return CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().getCChartOfAccountsByGlCode(glCode);
	}

	@Override
	public Collection<CFunction> getFunctionList() {
		return CommonsDaoFactory.getDAOFactory().getFunctionDAO().findAll();
	}

	@Override
	public CVoucherHeader findVoucherHeaderById(final Long vouchdrId) {
		return (CVoucherHeader) CommonsDaoFactory.getDAOFactory().getVoucherHeaderDAO().findById(vouchdrId, false);
	}

	@Override
	public CFunction findFunctionById(final Long functionId) {
		return (CFunction) CommonsDaoFactory.getDAOFactory().getFunctionDAO().findById(functionId, false);
	}

	@Override
	public Collection<FinancialYear> getFinancialYearList() {
		return CommonsDaoFactory.getDAOFactory().getFinancialYearDAO().findAll();
	}

	/**
	 * This API will return the transaction no for any type of txn. Input :Type,transaction date and connection Output :Transaction number in the format txnType+number+/+month+/+year
	 */
	@Override
	public String getTxnNumber(final String txnType, final String vDate, final Connection con) throws ParseException, SQLException {
		final String txndate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(vDate));
		final Query query = HibernateUtil.getCurrentSession().createSQLQuery("select a.FINANCIALYEAR,b.id from FINANCIALYEAR a,fiscalperiod b  where a.id=b.financialyearid AND ? between b.startingdate and b.endingdate");
		query.setString(0, txndate);
		final Object[] result = (Object[]) query.uniqueResult();
		String finYear = "";
		String fiscalPeriod = "";
		if ((result == null) || (result.length == 0)) {
			throw new EGOVRuntimeException("Year is not defined in the system");
		} else {
			finYear = result[0].toString();
			fiscalPeriod = result[1].toString();
		}
		EgNumbers egnum = CommonsDaoFactory.getDAOFactory().getEgNumbersHibernateDAO().getEgNumberByFiscalPeriodAndVouchertype(fiscalPeriod, txnType);
		String runningNumber = "";
		if (egnum == null) {
			egnum = new EgNumbers();
			runningNumber = "1";
			runningNumber = String.valueOf(Integer.parseInt(runningNumber));
			egnum.setVouchernumber(new BigDecimal(runningNumber));
			egnum.setVouchertype(txnType);
			egnum.setFiscialperiodid(new BigDecimal(fiscalPeriod));
			CommonsDaoFactory.getDAOFactory().getEgNumbersHibernateDAO().create(egnum);
		} else {
			runningNumber = String.valueOf(egnum.getVouchernumber().intValue() + 1);
			egnum.setVouchernumber(new BigDecimal(runningNumber));
			CommonsDaoFactory.getDAOFactory().getEgNumbersHibernateDAO().update(egnum);
		}
		return txnType + runningNumber + "/" + vDate.split("/")[1] + "/" + (finYear.substring(2, 4) + finYear.substring((finYear.length() - 2), finYear.length()));
	}

	/**
	 * This method returns the active and is active for posting Account records having classification as '4' , for a given type.
	 * @param type
	 * @return
	 */
	@Override
	public List<CChartOfAccounts> getActiveAccountsForType(final char type) throws EGOVException {
		return CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().getActiveAccountsForType(type);
	}

	/**
	 * This method returns all active functionary records.
	 * @return
	 */
	@Override
	public List<Functionary> getActiveFunctionaries() {
		return CommonsDaoFactory.getDAOFactory().getFunctionaryDAO().findAllActiveFunctionary();
	}

	/**
	 * This function returns the system date of the database server.
	 * @param connection
	 * @return @
	 */
	@Override
	public String getCurrentDate(final Connection connection) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultset = null;
		String currentDate = null;
		try {
			statement = connection.prepareStatement("select to_char(sysdate,'dd/MM/yyyy') as \"currentDate\" from dual");
			resultset = statement.executeQuery();
			resultset.next();
			currentDate = resultset.getString("currentDate");
		} catch (final Exception sqlex) {
			throw new SQLException(sqlex.getMessage());
		} finally {
			if (resultset != null) {
				resultset.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
		return currentDate;
	}

	@Override
	public CFinancialYear getFinancialYearByFinYearRange(final String finYearRange) {
		return CommonsDaoFactory.getDAOFactory().getFinancialYearDAO().getFinancialYearByFinYearRange(finYearRange);
	}

	@Override
	public List<CFinancialYear> getAllActiveFinancialYearList() {
		return CommonsDaoFactory.getDAOFactory().getFinancialYearDAO().getAllActiveFinancialYearList();
	}

	@Override
	public List<CFinancialYear> getAllActivePostingFinancialYear() {
		return CommonsDaoFactory.getDAOFactory().getFinancialYearDAO().getAllActivePostingFinancialYear();
	}

	@Override
	public List<EgwTypeOfWork> getAllTypeOfWork() {
		return CommonsDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO().getAllTypeOfWork();
	}

	@Override
	public List<EgwTypeOfWork> getAllParentOrderByCode() {
		return CommonsDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO().getAllParentOrderByCode();
	}

	@Override
	public EgwTypeOfWork getTypeOfWorkById(final Long workTypeId) {
		return CommonsDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO().getTypeOfWorkById(workTypeId);
	}

	@Override
	public EgwTypeOfWork findByCode(final String code) {
		return CommonsDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO().findByCode(code);
	}

	@Override
	public void createEgwTypeOfWork(final EgwTypeOfWork egwTypeOfWork) {
		CommonsDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO().create(egwTypeOfWork);
	}

	@Override
	public void updateEgwTypeOfWork(final EgwTypeOfWork egwTypeOfWork) {
		CommonsDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO().update(egwTypeOfWork);
	}

	@Override
	public List<EgwTypeOfWork> getTypeOfWorkDetailFilterBy(final String code, final String parentCode, final String description) {
		return CommonsDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO().getTypeOfWorkDetailFilterBy(code, parentCode, description);
	}

	@Override
	public List<EgwTypeOfWork> getTypeOfWorkDetailFilterByParty(final String code, final String parentCode, final String description, final String partyTypeCode) {
		return CommonsDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO().getTypeOfWorkDetailFilterByParty(code, parentCode, description, partyTypeCode);
	}

	@Override
	public List<EgPartytype> getPartyTypeDetailFilterBy(final String code, final String parentCode, final String description) {
		return CommonsDaoFactory.getDAOFactory().getEgPartytypeDAO().getPartyTypeDetailFilterBy(code, parentCode, description);
	}

	@Override
	public String getCurrYearFiscalId() {
		return CommonsDaoFactory.getDAOFactory().getFinancialYearDAO().getCurrYearFiscalId();
	}

	@Override
	public String getCurrYearStartDate() {
		return CommonsDaoFactory.getDAOFactory().getFinancialYearDAO().getCurrYearStartDate();
	}

	@Override
	public String getPrevYearFiscalId() {
		return CommonsDaoFactory.getDAOFactory().getFinancialYearDAO().getPrevYearFiscalId();
	}

	@Override
	public List<EgwStatus> getStatusByModule(final String moduleType) {
		return CommonsDaoFactory.getDAOFactory().getEgwStatusDAO().getStatusByModule(moduleType);
	}

	@Override
	public EgPartytype getPartytypeById(final Integer partyTypeId) {
		return (EgPartytype) CommonsDaoFactory.getDAOFactory().getEgPartytypeDAO().findById(partyTypeId, false);
	}

	@Override
	public void createEgPartytype(final EgPartytype egPartytype) {
		try {
			CommonsDaoFactory.getDAOFactory().getEgPartytypeDAO().create(egPartytype);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in creating party type", e);
		}
	}

	@Override
	public void updateEgPartytype(final EgPartytype egPartytype) {
		CommonsDaoFactory.getDAOFactory().getEgPartytypeDAO().update(egPartytype);
	}

	@Override
	public List<EgPartytype> findAllPartyTypeChild() {
		return CommonsDaoFactory.getDAOFactory().getEgPartytypeDAO().findAllPartyTypeChild();
	}

	@Override
	public List<EgwTypeOfWork> findAllParentPartyType() {
		return CommonsDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO().findAllParentPartyType();
	}

	@Override
	public List<EgwTypeOfWork> findAllChildPartyType() {
		return CommonsDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO().findAllChildPartyType();
	}

	@Override
	public List<CVoucherHeader> getVoucherHeadersByStatus(final Integer status) {
		try {
			return CommonsDaoFactory.getDAOFactory().getVoucherHeaderDAO().getVoucherHeadersByStatus(status);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Error occurred while getting Voucher Header by Status", e);
		}
	}

	@Override
	public List<CVoucherHeader> getVoucherHeadersByStatusAndType(final Integer status, final String type) {
		try {
			return CommonsDaoFactory.getDAOFactory().getVoucherHeaderDAO().getVoucherHeadersByStatusAndType(status, type);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Error occurred while getting Voucher Header by Status and Type", e);
		}
	}

	@Override
	public EgUom getUomByUom(final String uom) {
		EgUom egUom = null;
		if (uom != null) {
			final Query qry = HibernateUtil.getCurrentSession().createQuery("from EgUom uom where uom.uom=:uom");
			qry.setString("uom", uom);
			egUom = (EgUom) qry.uniqueResult();
		}
		return egUom;
	}

	@Override
	public List<Chequedetail> getChequedetailByVoucherheader(final CVoucherHeader voucherHeader) {
		return CommonsDaoFactory.getDAOFactory().getChequedetailDAO().getChequedetailByVoucherheader(voucherHeader);
	}

	@Override
	public Bankaccount getBankaccountById(final Integer id) {
		return (Bankaccount) CommonsDaoFactory.getDAOFactory().getBankaccountDAO().findById(id, false);
	}

	@Override
	public void createEgSurrenderedCheques(final EgSurrenderedCheques egSurrendrdChqs) {
		CommonsDaoFactory.getDAOFactory().getEgSurrenderedChequesDAO().create(egSurrendrdChqs);
	}

	@Override
	public void updateEgSurrenderedCheques(final EgSurrenderedCheques egSurrendrdChqs) {
		CommonsDaoFactory.getDAOFactory().getEgSurrenderedChequesDAO().update(egSurrendrdChqs);
	}

	@Override
	public EgPartytype getPartytypeByCode(final String code) {
		return CommonsDaoFactory.getDAOFactory().getEgPartytypeDAO().getPartytypeByCode(code);
	}

	@Override
	public String getAccountdetailtypeAttributename(final Connection connection, final String name) throws SQLException {
		final Query query = HibernateUtil.getCurrentSession().createSQLQuery("select id,attributename from accountdetailtype where name=?");
		query.setString(0, name);
		final Object[] result = (Object[]) query.uniqueResult();
		return ((result == null) || (result.length == 0)) ? "" : (result[0] + "#" + result[1]);
	}

	@Override
	public List<Bankbranch> getAllBankBranchs() {
		return CommonsDaoFactory.getDAOFactory().getBankbranchDAO().getAllBankBranchs();
	}

	@Override
	public List<Bankaccount> getAllBankAccounts() {
		return CommonsDaoFactory.getDAOFactory().getBankaccountDAO().getAllBankAccounts();
	}

	@Override
	public Bank getBankById(final Integer id) {
		return (Bank) CommonsDaoFactory.getDAOFactory().getBankDAO().findById(id, false);
	}

	@Override
	public ObjectType getObjectTypeByType(final String type) {
		return new ObjectTypeDAO().getObjectType(type);
	}

	@Override
	public ObjectType getObjectTypeById(final Integer type) {
		return new ObjectTypeDAO().getObjectType(type);
	}

	@Override
	public Bankbranch getBankbranchById(final Integer id) {
		return new BankbranchDAO().getBankbranchById(id);
	}

	@Override
	public ObjectHistory createObjectHistory(final ObjectHistory objhistory) {
		return (ObjectHistory) CommonsDaoFactory.getDAOFactory().getObjectHistoryDAO().create(objhistory);
	}

	@Override
	public String getCBillDeductionAmtByVhId(final Long voucherHeaderId) {
		try {
			return CommonsDaoFactory.getDAOFactory().getGeneralLedgerDAO().getCBillDeductionAmtByVhId(voucherHeaderId);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Exception in searching CBillDeductionAmtByVhId" + e.getMessage(), e);
		}
	}

	@Override
	public List<Chequedetail> getChequedetailFilterBy(final String vhNo, final Date vhDateFrom, final Date vhDateTo, final String chqNo, final String mode, final String pymntType, final String dept, final String functionaryId) {
		Functionary functionary = null;
		if (functionaryId != null) {
			functionary = this.getFunctionaryById(Integer.parseInt(functionaryId));
		}
		return CommonsDaoFactory.getDAOFactory().getChequedetailDAO().getChequedetailFilterBy(vhNo, vhDateFrom, vhDateTo, chqNo, mode, pymntType, dept, functionary);
	}

	@Override
	public Vouchermis getVouchermisByVhId(final Integer vhId) {
		return CommonsDaoFactory.getDAOFactory().getVouchermisDAO().getVouchermisByVhId(vhId);
	}

	@Override
	public CVoucherHeader getVoucherHeadersByCGN(final String cgn) {
		return CommonsDaoFactory.getDAOFactory().getVoucherHeaderDAO().getVoucherHeadersByCGN(cgn);
	}

	/**
	 * @param moduleType Module type
	 * @param codeList List of status codes
	 * @return List of all EgwStatus objects filtered by given module type and list of status codes
	 */
	@Override
	public List<EgwStatus> getStatusListByModuleAndCodeList(final String moduleType, final List codeList) {
		return CommonsDaoFactory.getDAOFactory().getEgwStatusDAO().getStatusListByModuleAndCodeList(moduleType, codeList);
	}

	@Override
	public Functionary getFunctionaryById(final Integer id) {
		return CommonsDaoFactory.getDAOFactory().getFunctionaryDAO().functionaryById(id);
	}

	@Override
	public CFunction getFunctionByCode(final String code) {
		return CommonsDaoFactory.getDAOFactory().getFunctionDAO().getFunctionByCode(code);
	}

	@Override
	public List<CChartOfAccounts> getAccountCodeByPurpose(final Integer purposeId) throws EGOVException {
		return CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().getAccountCodeByPurpose(purposeId);
	}

	@Override
	public List<CChartOfAccounts> getDetailedAccountCodeList() throws EGOVException {
		return CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().getDetailedAccountCodeList();
	}

	@Override
	public Accountdetailtype getAccountDetailTypeIdByName(final String glCode, final String name) {
		try {
			return CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().getAccountDetailTypeIdByName(glCode, name);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Error occurred while getting Account Detail Type ID by Name", e);
		}
	}

	@Override
	public List<Fundsource> getAllActiveIsLeafFundSources() throws EGOVException {
		return CommonsDaoFactory.getDAOFactory().getFundsourceDAO().findAllActiveIsLeafFundSources();
	}

	@Override
	public Scheme getSchemeById(final Integer id) throws EGOVException {
		return CommonsDaoFactory.getDAOFactory().getSchemeDAO().getSchemeById(id);
	}

	@Override
	public SubScheme getSubSchemeById(final Integer id) throws EGOVException {
		return CommonsDaoFactory.getDAOFactory().getSubSchemeDAO().getSubSchemeById(id);
	}

	@Override
	public CFinancialYear getFinancialYearById(final Long id) {
		return CommonsDaoFactory.getDAOFactory().getFinancialYearDAO().getFinancialYearById(id);
	}

	@Override
	public CFunction getFunctionById(final Long Id) {
		return CommonsDaoFactory.getDAOFactory().getFunctionDAO().getFunctionById(Id);
	}

	@Override
	public Integer getDetailtypeforObject(final Object master) throws EGOVException {
		return CommonsDaoFactory.getDAOFactory().getaccountdetailtypeHibernateDAO().getDetailtypeforObject(master);
	}

	@Override
	public List<Accountdetailtype> getDetailTypeListByGlCode(final String glCode) throws EGOVException {
		return CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().getAccountdetailtypeListByGLCode(glCode);
	}

	@Override
	public Fund fundByCode(final String fundCode) {
		return CommonsDaoFactory.getDAOFactory().getFundDAO().fundByCode(fundCode);
	}

	@Override
	public Scheme schemeByCode(final String code) {
		return CommonsDaoFactory.getDAOFactory().getSchemeDAO().getSchemeByCode(code);
	}

	@Override
	public Fundsource getFundSourceByCode(final String code) {
		return CommonsDaoFactory.getDAOFactory().getFundsourceDAO().getFundSourceByCode(code);
	}

	@Override
	public SubScheme getSubSchemeByCode(final String code) {
		return CommonsDaoFactory.getDAOFactory().getSubSchemeDAO().getSubSchemeByCode(code);
	}

	@Override
	public Bank getBankByCode(final String bankCode) {
		return CommonsDaoFactory.getDAOFactory().getBankDAO().getBankByCode(bankCode);
	}

	@Override
	public Bankaccount getBankAccountByAccBranchBank(final String bankAccNum, final String bankBranchCode, final String bankCode) {
		return CommonsDaoFactory.getDAOFactory().getBankaccountDAO().getBankAccountByAccBranchBank(bankAccNum, bankBranchCode, bankCode);
	}

	@Override
	public Functionary getFunctionaryByCode(final BigDecimal code) {
		return CommonsDaoFactory.getDAOFactory().getFunctionaryDAO().getFunctionaryByCode(code);
	}

	@Override
	public List<Accountdetailtype> getAccountdetailtypeListByGLCode(final String glCode) throws EGOVException {
		return CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().getAccountdetailtypeListByGLCode(glCode);
	}

	@Override
	public List<CChartOfAccounts> getActiveAccountsForTypes(final char[] type) throws ValidationException {
		return CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().getActiveAccountsForTypes(type);
	}

	@Override
	public List<CChartOfAccounts> getAccountCodeByListOfPurposeId(final Integer[] purposeId) throws ValidationException {
		return CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().getAccountCodeByListOfPurposeId(purposeId);
	}

	@Override
	public List<CChartOfAccounts> getListOfDetailCode(final String glCode) throws ValidationException {
		return CommonsDaoFactory.getDAOFactory().getChartOfAccountsDAO().getListOfDetailCode(glCode);
	}

	@Override
	public List<EgUom> getAllUomsWithinCategoryByUom(final Integer uomId) throws ValidationException {
		return CommonsDaoFactory.getDAOFactory().getEgUomDAO().getAllUomsWithinCategoryByUom(uomId);
	}

	@Override
	public BigDecimal getConversionFactorByUom(final Integer uomId) throws ValidationException {
		return CommonsDaoFactory.getDAOFactory().getEgUomDAO().getConversionFactorByUom(uomId);
	}

	@Override
	public BigDecimal getConversionFactorByFromUomToUom(final Integer fromuomId, final Integer touomId) throws ValidationException {
		return CommonsDaoFactory.getDAOFactory().getEgUomDAO().getConversionFactorByFromUomToUom(fromuomId, touomId);
	}

	@Override
	public List<EgPartytype> getSubPartyTypes(final String code) {
		return CommonsDaoFactory.getDAOFactory().getEgPartytypeDAO().getSubPartyTypesForCode(code);
	}

	@Override
	public Functionary getFunctionaryByName(final String name) {
		return CommonsDaoFactory.getDAOFactory().getFunctionaryDAO().getFunctionaryByName(name);
	}
}
