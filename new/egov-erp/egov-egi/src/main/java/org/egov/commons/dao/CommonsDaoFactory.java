/*
 * @(#)CommonsDaoFactory.java 3.0, 11 Jun, 2013 10:59:27 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.infstr.commonMasters.dao.EgUomHibernateDAO;
import org.egov.infstr.commonMasters.dao.EgUomcategoryHibernateDAO;
import org.egov.infstr.utils.EGovConfig;
import org.egov.lib.citizen.dao.OwnerDAO;

public abstract class CommonsDaoFactory {

	private static final CommonsDaoFactory EJB3_PERSISTENCE = null;

	private static final CommonsDaoFactory HIBERNATE = new CommonsHibernateDaoFactory();

	private static final CommonsDaoFactory retFac = resolveDAOFactory();

	public static CommonsDaoFactory getDAOFactory() {

		return retFac;
	}

	private static CommonsDaoFactory resolveDAOFactory() {

		final String method = EGovConfig.getProperty("COMMONS-FACTORY-IMPL", "HIBERNATE", "PTIS");

		if (method != null) {
			if (method.trim().equalsIgnoreCase("HIBERNATE")) {

				return HIBERNATE;
			} else {

				return EJB3_PERSISTENCE;
			}
		}
		return null;
	}

	public abstract FundSourceHibernateDAO getFundsourceDAO();

	public abstract StatusHibernateDAO getStatusDAO();

	public abstract EgUomHibernateDAO getEgUomDAO();

	public abstract EgUomcategoryHibernateDAO getEgUomcategoryDAO();

	public abstract FiscalPeriodDAO getFiscalPeriodDAO();

	public abstract WardDAO getWardDAO();

	public abstract ChartOfAccountsDAO getChartOfAccountsDAO();

	public abstract GeneralLedgerDAO getGeneralLedgerDAO();

	public abstract VoucherHeaderDAO getVoucherHeaderDAO();

	public abstract EgNumbersHibernateDAO getEgNumbersHibernateDAO();

	public abstract EgwTypeOfWorkHibernateDAO getEgwTypeOfWorkDAO();

	public abstract EgPartytypeHibernateDAO getEgPartytypeDAO();

	public abstract ChequedetailHibernateDAO getChequedetailDAO();

	public abstract EgSurrenderedChequesHibernateDAO getEgSurrenderedChequesDAO();

	public abstract VouchermisHibernateDAO getVouchermisDAO();

	public abstract FundHibernateDAO getFundDAO();

	public abstract FunctionDAO getFunctionDAO();

	public abstract FinancialYearDAO getFinancialYearDAO();

	public abstract InstallmentDao getInstallmentDao();

	public abstract OwnerDAO getOwnerDao();

	public abstract AccountdetailkeyHibernateDAO getAccountdetailkeyDAO();

	public abstract EgwStatusHibernateDAO getEgwStatusDAO();

	public abstract EgActiondetailsHibernateDAO getEgActiondetailsDAO();

	public abstract EgwSatuschangeHibernateDAO getEgwSatuschangeDAO();

	public abstract RelationHibernateDAO getRelationDAO();

	public abstract ObjectHistoryDAO getObjectHistoryDAO();

	public abstract BankaccountHibernateDAO getBankaccountDAO();

	public abstract BankBranchHibernateDAO getBankbranchDAO();

	public abstract BankHibernateDAO getBankDAO();

	public abstract FunctionaryHibernateDAO getFunctionaryDAO();

	public abstract SchemeHibernateDAO getSchemeDAO();

	public abstract SubSchemeHibernateDAO getSubSchemeDAO();

	public abstract AccountdetailtypeHibernateDAO getaccountdetailtypeHibernateDAO();

}
