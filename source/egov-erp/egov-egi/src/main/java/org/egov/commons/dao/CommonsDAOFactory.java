/*
 * @(#)CommonsHibernateDaoFactory.java 3.0, 11 Jun, 2013 2:17:00 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.CWard;
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
import org.egov.commons.Relation;
import org.egov.commons.Scheme;
import org.egov.commons.Status;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.commonMasters.EgUomcategory;
import org.egov.infstr.commonMasters.dao.EgUomHibernateDAO;
import org.egov.infstr.commonMasters.dao.EgUomcategoryHibernateDAO;
import org.egov.lib.citizen.dao.OwnerDAO;
import org.egov.lib.citizen.dao.OwnerHibernateDAO;
import org.egov.lib.citizen.model.Owner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class CommonsDAOFactory {

    private SessionFactory sessionFactory;

    public CommonsDAOFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public InstallmentDao getInstallmentDao() {
        return new InstallmentHibDao(Installment.class, getCurrentSession());
    }

    public OwnerDAO getOwnerDao() {
        return new OwnerHibernateDAO(Owner.class, getCurrentSession());
    }


    public ObjectHistoryDAO getObjectHistoryDAO() {
        return new ObjectHistoryHibernateDAO(ObjectHistory.class, getCurrentSession());
    }


    public EgwStatusHibernateDAO getEgwStatusDAO() {
        return new EgwStatusHibernateDAO(EgwStatus.class, getCurrentSession());
    }


    public EgActiondetailsHibernateDAO getEgActiondetailsDAO() {
        return new EgActiondetailsHibernateDAO(EgActiondetails.class, getCurrentSession());
    }


    public EgwSatuschangeHibernateDAO getEgwSatuschangeDAO() {
        return new EgwSatuschangeHibernateDAO(EgwSatuschange.class, getCurrentSession());
    }


    public AccountdetailkeyHibernateDAO getAccountdetailkeyDAO() {
        return new AccountdetailkeyHibernateDAO(Accountdetailkey.class, getCurrentSession());
    }


    public RelationHibernateDAO getRelationDAO() {
        return new RelationHibernateDAO(Relation.class, getCurrentSession());
    }


    public FinancialYearDAO getFinancialYearDAO() {
        return new FinancialYearHibernateDAO(CFinancialYear.class, getCurrentSession());
    }


    public BankBranchHibernateDAO getBankbranchDAO() {
        return new BankBranchHibernateDAO(Bankbranch.class, getCurrentSession());
    }


    public BankHibernateDAO getBankDAO() {
        return new BankHibernateDAO(Bank.class, getCurrentSession());
    }


    public FundHibernateDAO getFundDAO() {

        return new FundHibernateDAO(Fund.class, getCurrentSession());
    }


    public FiscalPeriodDAO getFiscalPeriodDAO() {
        return new FiscalPeriodHibernateDAO(CFiscalPeriod.class, getCurrentSession());
    }


    public FunctionDAO getFunctionDAO() {
        return new FunctionHibernateDAO(CFunction.class, getCurrentSession());
    }


    public BankaccountHibernateDAO getBankaccountDAO() {
        return new BankaccountHibernateDAO(Bankaccount.class, getCurrentSession());
    }

	/* move to org.egov.commons */

    public FundSourceHibernateDAO getFundsourceDAO() {

        return new FundSourceHibernateDAO(Fundsource.class, getCurrentSession());
    }

    /**
     * This DAO is related to -- > eg_invstatus which is part of the inventory. this table has following column --> ID,STATUS,MODULETYPE
     */

    public StatusHibernateDAO getStatusDAO() {
        return new StatusHibernateDAO(Status.class, getCurrentSession());
    }

	/*
     * public TdsHibernateDAO getTdsDAO() { return new TdsHibernateDAO(Tds.class,getCurrentSession()); }
	 */


    public EgUomHibernateDAO getEgUomDAO() {
        return new EgUomHibernateDAO(EgUom.class, getCurrentSession());
    }


    public EgUomcategoryHibernateDAO getEgUomcategoryDAO() {
        return new EgUomcategoryHibernateDAO(EgUomcategory.class, getCurrentSession());
    }


    public ChartOfAccountsDAO getChartOfAccountsDAO() {
        return new ChartOfAccountsHibernateDAO(CChartOfAccounts.class, getCurrentSession());
    }

    /**
     * this DAO is related -- > EGF_WARD following column presented in this table -->ID ,NAME
     */


    public WardDAO getWardDAO() {
        return new WardHibernateDAO(CWard.class, getCurrentSession());
    }

    public GeneralLedgerDAO getGeneralLedgerDAO() {
        return new GeneralLedgerHibernateDAO(CGeneralLedger.class, this, getCurrentSession());
    }


    public VoucherHeaderDAO getVoucherHeaderDAO() {
        return new VoucherHeaderHibernateDAO(CVoucherHeader.class, getCurrentSession());
    }


    public EgNumbersHibernateDAO getEgNumbersHibernateDAO() {
        return new EgNumbersHibernateDAO(EgNumbers.class, getCurrentSession());
    }


    public EgwTypeOfWorkHibernateDAO getEgwTypeOfWorkDAO() {
        return new EgwTypeOfWorkHibernateDAO(EgwTypeOfWork.class, getCurrentSession());
    }


    public EgPartytypeHibernateDAO getEgPartytypeDAO() {
        return new EgPartytypeHibernateDAO(EgPartytype.class, getCurrentSession());
    }

    public AccountdetailtypeHibernateDAO getaccountdetailtypeHibernateDAO() {
        return new AccountdetailtypeHibernateDAO(Accountdetailtype.class, getCurrentSession());
    }


    public ChequedetailHibernateDAO getChequedetailDAO() {
        return new ChequedetailHibernateDAO(Chequedetail.class, getCurrentSession());
    }


    public EgSurrenderedChequesHibernateDAO getEgSurrenderedChequesDAO() {
        return new EgSurrenderedChequesHibernateDAO(EgSurrenderedCheques.class, getCurrentSession());
    }


    public VouchermisHibernateDAO getVouchermisDAO() {
        return new VouchermisHibernateDAO(Vouchermis.class, getCurrentSession());
    }


    public FunctionaryHibernateDAO getFunctionaryDAO() {
        return new FunctionaryHibernateDAO(Functionary.class, getCurrentSession());
    }


    public SchemeHibernateDAO getSchemeDAO() {
        return new SchemeHibernateDAO(Scheme.class, getCurrentSession());
    }


    public SubSchemeHibernateDAO getSubSchemeDAO() {
        return new SubSchemeHibernateDAO(SubScheme.class, getCurrentSession());
    }
}
