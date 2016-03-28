/**
 * eGov suite of products aim to improve the internal efficiency,transparency, accountability and the service delivery of the
 * government organizations.
 * 
 * Copyright (C) <2015> eGovernments Foundation
 * 
 * The updated version of eGov suite of products as by eGovernments Foundation is available at http://www.egovernments.org
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/ or http://www.gnu.org/licenses/gpl.html .
 * 
 * In addition to the terms of the GPL license to be adhered to in using this program, the following additional terms are to be
 * complied with:
 * 
 * 1) All versions of this program, verbatim or modified must carry this Legal Notice.
 * 
 * 2) Any misrepresentation of the origin of the material is prohibited. It is required that all modified versions of this
 * material be marked in reasonable ways as different from the original version.
 * 
 * 3) This license does not grant any rights to any user of the program with regards to rights under trademark law for use of the
 * trade names or trademarks of eGovernments Foundation.
 * 
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.commons.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.hibernate.Session;

public class CommonsDAOFactory {

    @PersistenceContext
    EntityManager entityManager;

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public InstallmentDao getInstallmentDao() {
        return new InstallmentHibDao(Installment.class, getCurrentSession());
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
        return new AccountdetailkeyHibernateDAO();
    }

    public RelationHibernateDAO getRelationDAO() {
        return new RelationHibernateDAO();
    }

    public FinancialYearDAO getFinancialYearDAO() {
        return new FinancialYearHibernateDAO();
    }

    public BankBranchHibernateDAO getBankbranchDAO() {
        return new BankBranchHibernateDAO();
    }

    public BankHibernateDAO getBankDAO() {
        return new BankHibernateDAO();
    }

    public FundHibernateDAO getFundDAO() {

        return new FundHibernateDAO();
    }

    public FiscalPeriodDAO getFiscalPeriodDAO() {
        return new FiscalPeriodHibernateDAO();
    }

    public FunctionDAO getFunctionDAO() {
        return new FunctionHibernateDAO();
    }

    public BankaccountHibernateDAO getBankaccountDAO() {
        return new BankaccountHibernateDAO();
    }

    /* move to org.egov.commons */

    public FundSourceHibernateDAO getFundsourceDAO() {

        return new FundSourceHibernateDAO();
    }

    /**
     * This DAO is related to -- > eg_invstatus which is part of the inventory. this table has following column -->
     * ID,STATUS,MODULETYPE
     */

    public StatusHibernateDAO getStatusDAO() {
        return new StatusHibernateDAO(Status.class, getCurrentSession());
    }

    /*
     * public TdsHibernateDAO getTdsDAO() { return new TdsHibernateDAO(Tds.class,getCurrentSession()); }
     */

    /*
     * public EgUomHibernateDAO getEgUomDAO() { return new EgUomHibernateDAO(EgUom.class, getCurrentSession()); }
     */

    /*
     * public EgUomcategoryHibernateDAO getEgUomcategoryDAO() { return new EgUomcategoryHibernateDAO(EgUomcategory.class,
     * getCurrentSession()); }
     */

    public ChartOfAccountsDAO getChartOfAccountsDAO() {
        return new ChartOfAccountsHibernateDAO();
    }

    /**
     * this DAO is related -- > EGF_WARD following column presented in this table -->ID ,NAME
     */

    public WardDAO getWardDAO() {
        return new WardHibernateDAO(CWard.class, getCurrentSession());
    }

    public GeneralLedgerDAO getGeneralLedgerDAO() {
        return new GeneralLedgerHibernateDAO();
    }

    public VoucherHeaderDAO getVoucherHeaderDAO() {
        return new VoucherHeaderHibernateDAO();
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
        return new AccountdetailtypeHibernateDAO();
    }

    public EgSurrenderedChequesHibernateDAO getEgSurrenderedChequesDAO() {
        return new EgSurrenderedChequesHibernateDAO(EgSurrenderedCheques.class, getCurrentSession());
    }

    public VouchermisHibernateDAO getVouchermisDAO() {
        return new VouchermisHibernateDAO();
    }

    public FunctionaryHibernateDAO getFunctionaryDAO() {
        return new FunctionaryHibernateDAO();
    }

    public SchemeHibernateDAO getSchemeDAO() {
        return new SchemeHibernateDAO();
    }

    public SubSchemeHibernateDAO getSubSchemeDAO() {
        return new SubSchemeHibernateDAO();
    }
}
