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
/**
 *
 */
package org.egov.egf.masters.model;

import org.apache.log4j.Logger;
import org.egov.infstr.models.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mani Is used to store the funding patterns
 */
public class LoanGrantDetail extends BaseModel {
    private static final long serialVersionUID = -5584173572438673662L;

    final static Logger LOGGER = Logger.getLogger(LoanGrantReceiptDetail.class);
    private LoanGrantHeader header;
    private FundingAgency fundingAgency;
    private BigDecimal loanAmount;
    private BigDecimal grantAmount;
    private Double percentage;
    private String agencySchemeNo;// This field will store no and date
    private String councilResNo;// This field will store no and date
    private String loanSanctionNo;// This field will store no and date
    private Date agreementDate;
    private String commOrderNo;
    private String docId;
    private String patternType;// to type of pattern used 1.sactioned 2.unsanctioned 3.revised

    public LoanGrantDetail() {
    }

    public LoanGrantDetail(final String patternType) {
        this.patternType = patternType;
    }

    public LoanGrantHeader getHeader() {
        return header;
    }

    public void setHeader(final LoanGrantHeader header) {
        this.header = header;
    }

    public FundingAgency getFundingAgency() {
        return fundingAgency;
    }

    public void setFundingAgency(final FundingAgency fundingAgency) {
        this.fundingAgency = fundingAgency;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(final BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getGrantAmount() {
        return grantAmount;
    }

    public void setGrantAmount(final BigDecimal grantAmount) {
        this.grantAmount = grantAmount;
    }

    public String getAgencySchemeNo() {
        return agencySchemeNo;
    }

    public void setAgencySchemeNo(final String agencySchemeNo) {
        this.agencySchemeNo = agencySchemeNo;
    }

    public String getCouncilResNo() {
        return councilResNo;
    }

    public void setCouncilResNo(final String councilResNo) {
        this.councilResNo = councilResNo;
    }

    public String getLoanSanctionNo() {
        return loanSanctionNo;
    }

    public void setLoanSanctionNo(final String loanSanctionNo) {
        this.loanSanctionNo = loanSanctionNo;
    }

    public Date getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(final Date agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getCommOrderNo() {
        return commOrderNo;
    }

    public void setCommOrderNo(final String commOrderNo) {
        this.commOrderNo = commOrderNo;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(final String docId) {
        this.docId = docId;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(final Double percentage) {
        this.percentage = percentage;
    }

    public String getPatternType() {
        return patternType;
    }

    public void setPatternType(final String patternType) {
        this.patternType = patternType;
    }

}
