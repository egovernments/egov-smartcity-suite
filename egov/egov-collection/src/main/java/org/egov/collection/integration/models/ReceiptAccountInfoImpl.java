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
package org.egov.collection.integration.models;

import java.math.BigDecimal;

import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infstr.services.PersistenceService;

/**
 * Provides account information for receipts
 */

public class ReceiptAccountInfoImpl implements ReceiptAccountInfo {
    /**
     * This is used to check if an account is a revenue account.
     */
    private boolean isRevenueAccount;
        /**
     * The private instance of receipt detail. This is used by all public getters.
     */
    private ReceiptDetail receiptDetail;
    

    /**
     * Creates the receipt account info for given receipt detail.
     *
     * @param receiptDetail The receipt detail object
     */
    public ReceiptAccountInfoImpl(final ReceiptDetail receiptDetail, ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO,
            PersistenceService persistenceService) {
        this.receiptDetail = receiptDetail;
        this.isRevenueAccount = FinancialsUtil.isRevenueAccountHead(this.receiptDetail.getAccounthead(),
                chartOfAccountsHibernateDAO.getBankChartofAccountCodeList(), persistenceService);

    }

    @Override
    public String toString() {
        return receiptDetail.toString();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getGlCode ()
     */
    @Override
    public String getGlCode() {
        return receiptDetail.getAccounthead() == null ? null : receiptDetail.getAccounthead().getGlcode();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo# getAccountName()
     */
    @Override
    public String getAccountName() {
        return receiptDetail.getAccounthead() == null ? null : receiptDetail.getAccounthead().getName();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo# getFunction()
     */
    @Override
    public String getFunction() {
        return receiptDetail.getFunction() == null ? null : receiptDetail.getFunction().getCode();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo# getFunctionName()
     */
    @Override
    public String getFunctionName() {
        return receiptDetail.getFunction() == null ? null : receiptDetail.getFunction().getName();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo# getDrAmount()
     */
    @Override
    public BigDecimal getDrAmount() {
        return receiptDetail.getDramount();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo# getCrAmount()
     */
    @Override
    public BigDecimal getCrAmount() {
        return receiptDetail.getCramount();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo# getIsRevenueAccount()
     */
    @Override
    public boolean getIsRevenueAccount() {
        return isRevenueAccount;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo# getOrderNumber()
     */
    @Override
    public Long getOrderNumber() {
        return receiptDetail.getOrdernumber() == null ? null : receiptDetail.getOrdernumber();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo# getDescription()
     */
    @Override
    public String getDescription() {
        return receiptDetail.getDescription();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo# getFinancialYear()
     */
    @Override
    public String getFinancialYear() {
        return receiptDetail.getFinancialYear() == null ? null : receiptDetail.getFinancialYear().getFinYearRange();
    }

    /*
     * (non-Javadoc)
     * @seeorg.egov.infstr.collections.integration.models.IReceiptAccountInfo# getCreditAmountToBePaid()
     */
    @Override
    public BigDecimal getCreditAmountToBePaid() {
        return receiptDetail.getCramountToBePaid();
    }

    /*
     * (non-Javadoc)
     * @seeorg.egov.infstr.collections.integration.models.IReceiptAccountInfo# getPurpose()
     */
    @Override
    public String getPurpose() {
        return receiptDetail.getPurpose() == null ? "" : receiptDetail.getPurpose();
    }

 
    @Override
    public Long getGroupId() {
        return receiptDetail.getGroupId() ;
    }

  
}
