/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.ptis.bean.dashboard;

import java.math.BigDecimal;
import java.util.List;

import org.egov.ptis.domain.model.ErrorDetails;

public class CollectionDetails {
    private BigDecimal todayColl = BigDecimal.ZERO;
    private BigDecimal lyTodayColl = BigDecimal.ZERO;
    private BigDecimal cytdColl = BigDecimal.ZERO;
    private BigDecimal cytdDmd = BigDecimal.ZERO;
    private BigDecimal totalDmd = BigDecimal.ZERO;
    private BigDecimal lytdColl = BigDecimal.ZERO;
    private BigDecimal performance = BigDecimal.ZERO;
    private BigDecimal lyVar = BigDecimal.ZERO;
    private BigDecimal dmdColl = BigDecimal.ZERO;
    private BigDecimal pntlyColl = BigDecimal.ZERO;
    private Long totalAssessments = 0L;
    private List<CollectionTrend> collTrends;
    private List<CollTableData> responseDetails;
    private ErrorDetails errorDetails;

    public BigDecimal getTodayColl() {
        return todayColl;
    }

    public void setTodayColl(BigDecimal todayColl) {
        this.todayColl = todayColl;
    }

    public BigDecimal getLyTodayColl() {
        return lyTodayColl;
    }

    public void setLyTodayColl(BigDecimal lyTodayColl) {
        this.lyTodayColl = lyTodayColl;
    }

    public BigDecimal getCytdColl() {
        return cytdColl;
    }

    public void setCytdColl(BigDecimal cytdColl) {
        this.cytdColl = cytdColl;
    }

    public BigDecimal getCytdDmd() {
        return cytdDmd;
    }

    public void setCytdDmd(BigDecimal cytdDmd) {
        this.cytdDmd = cytdDmd;
    }

    public BigDecimal getTotalDmd() {
        return totalDmd;
    }

    public void setTotalDmd(BigDecimal totalDmd) {
        this.totalDmd = totalDmd;
    }

    public BigDecimal getLytdColl() {
        return lytdColl;
    }

    public void setLytdColl(BigDecimal lytdColl) {
        this.lytdColl = lytdColl;
    }

    public List<CollectionTrend> getCollTrends() {
        return collTrends;
    }

    public void setCollTrends(List<CollectionTrend> collTrends) {
        this.collTrends = collTrends;
    }

    public List<CollTableData> getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(List<CollTableData> responseDetails) {
        this.responseDetails = responseDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }

    public BigDecimal getPerformance() {
        return performance;
    }

    public void setPerformance(BigDecimal performance) {
        this.performance = performance;
    }

    public BigDecimal getLyVar() {
        return lyVar;
    }

    public void setLyVar(BigDecimal lyVar) {
        this.lyVar = lyVar;
    }
    
    public BigDecimal getDmdColl() {
        return dmdColl;
    }

    public void setDmdColl(BigDecimal dmdColl) {
        this.dmdColl = dmdColl;
    }

    public BigDecimal getPntlyColl() {
        return pntlyColl;
    }

    public void setPntlyColl(BigDecimal pntlyColl) {
        this.pntlyColl = pntlyColl;
    }

    public Long getTotalAssessments() {
        return totalAssessments;
    }

    public void setTotalAssessments(Long totalAssessments) {
        this.totalAssessments = totalAssessments;
    }
}
