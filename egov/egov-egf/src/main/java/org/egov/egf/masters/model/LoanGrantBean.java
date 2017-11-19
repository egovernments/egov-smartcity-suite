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
package org.egov.egf.masters.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author mani Simple helper class to get project code id,name,code etc can be used for other entities also
 */
public class LoanGrantBean {
    private Long id;
    private String code;
    private String name;
    private String voucherNumber;
    private Date voucherDate;
    private BigDecimal amount;
    private BigDecimal agencyAmount;
    private BigDecimal grantAmount;
    private BigDecimal loanAmount;
    private String subScheme;
    private String agencyName;
    private BigDecimal percentAmount;
    private String status;
    private Integer detailKey;
    private Integer detailType;
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(final Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final String subScheme) {
        this.subScheme = subScheme;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(final String agencyName) {
        this.agencyName = agencyName;
    }

    public BigDecimal getPercentAmount() {
        return percentAmount;
    }

    public void setPercentAmount(final BigDecimal percentAmount) {
        this.percentAmount = percentAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Integer getDetailKey() {
        return detailKey;
    }

    public void setDetailKey(final Integer detailKey) {
        this.detailKey = detailKey;
    }

    public Integer getDetailType() {
        return detailType;
    }

    /**
     * @param amount
     * @param agencyAmount
     * @param grantAmount
     * @param agencyName
     * @param status
     */
    public LoanGrantBean(final BigDecimal amount, final BigDecimal agencyAmount, final BigDecimal grantAmount,
            final String agencyName, final String status) {
        super();
        this.amount = amount;
        this.agencyAmount = agencyAmount;
        this.grantAmount = grantAmount;
        this.agencyName = agencyName;
        this.status = status;
    }

    public LoanGrantBean(final BigDecimal agencyAmount, final BigDecimal loanAmount, final String agencyName) {
        super();
        amount = amount;
        this.agencyAmount = agencyAmount;
        grantAmount = grantAmount;
        this.agencyName = agencyName;
        status = status;
    }

    /**
     *
     */
    public LoanGrantBean() {
        super();
    }

    public void setDetailType(final Integer detailType) {
        this.detailType = detailType;
    }

    public BigDecimal getAgencyAmount() {
        return agencyAmount;
    }

    public void setAgencyAmount(final BigDecimal agencyAmount) {
        this.agencyAmount = agencyAmount;
    }

    public BigDecimal getGrantAmount() {
        return grantAmount;
    }

    public void setGrantAmount(final BigDecimal grantAmount) {
        this.grantAmount = grantAmount;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(final BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

}
