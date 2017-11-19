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
import org.egov.commons.SubScheme;
import org.egov.infstr.models.BaseModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mani used for both loans and grants
 */
public class LoanGrantHeader extends BaseModel {
    private static final long serialVersionUID = 7699342818798141533L;
    final static Logger LOGGER = Logger.getLogger(LoanGrantHeader.class);
    private SubScheme subScheme;
    private String councilResNo;
    private String govtOrderNo;
    private String amendmentNo;
    private Date councilResDate;
    private Date govtOrderDate;
    private Date amendmentDate;
    private BigDecimal projectCost;
    private BigDecimal sanctionedCost;
    private BigDecimal revisedCost;
    private List<SubSchemeProject> projectList = new ArrayList<SubSchemeProject>();
    private List<LoanGrantDetail> detailList = new ArrayList<LoanGrantDetail>();
    private List<LoanGrantReceiptDetail> receiptList = new ArrayList<LoanGrantReceiptDetail>();

    public SubScheme getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final SubScheme subScheme) {
        this.subScheme = subScheme;
    }

    public String getCouncilResNo() {
        return councilResNo;
    }

    public void setCouncilResNo(final String councilResNo) {
        this.councilResNo = councilResNo;
    }

    public String getGovtOrderNo() {
        return govtOrderNo;
    }

    public void setGovtOrderNo(final String govtOrderNo) {
        this.govtOrderNo = govtOrderNo;
    }

    public String getAmendmentNo() {
        return amendmentNo;
    }

    public void setAmendmentNo(final String amendmentNo) {
        this.amendmentNo = amendmentNo;
    }

    public BigDecimal getProjectCost() {
        return projectCost;
    }

    public void setProjectCost(final BigDecimal projectCost) {
        this.projectCost = projectCost;
    }

    public BigDecimal getSanctionedCost() {
        return sanctionedCost;
    }

    public void setSanctionedCost(final BigDecimal sanctionedCost) {
        this.sanctionedCost = sanctionedCost;
    }

    public BigDecimal getRevisedCost() {
        return revisedCost;
    }

    public void setRevisedCost(final BigDecimal revisedCost) {
        this.revisedCost = revisedCost;
    }

    public List<SubSchemeProject> getProjectList() {
        return projectList;
    }

    public void setProjectList(final List<SubSchemeProject> projectList) {
        this.projectList = projectList;
    }

    public List<LoanGrantDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(final List<LoanGrantDetail> detailList) {
        this.detailList = detailList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setCouncilResDate(final Date councilResDate) {
        this.councilResDate = councilResDate;
    }

    public Date getCouncilResDate() {
        return councilResDate;
    }

    public void setGovtOrderDate(final Date govtOrderDate) {
        this.govtOrderDate = govtOrderDate;
    }

    public Date getGovtOrderDate() {
        return govtOrderDate;
    }

    public void setAmendmentDate(final Date amendmentDate) {
        this.amendmentDate = amendmentDate;
    }

    public Date getAmendmentDate() {
        return amendmentDate;
    }

    public List<LoanGrantReceiptDetail> getReceiptList() {
        return receiptList;
    }

    public void setReceiptList(final List<LoanGrantReceiptDetail> receiptList) {
        this.receiptList = receiptList;
    }

}
