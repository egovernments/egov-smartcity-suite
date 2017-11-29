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

package org.egov.works.elasticsearch.model;

import java.util.Date;

/**
 * @author venki
 */
public class WorksMilestoneIndexResponse {

    private String name;

    private String typeofwork;

    private String ulbname;

    private String ulbcode;

    private String districtname;

    private String ismilestonecreated;

    private String latestupdatedtimestamp;

    private Long totalnoofworks;

    private Double totalestimatedcostinlakhs;

    private Double totalworkordervalueinlakhs;

    private Double totalbillamountinlakhs;

    private Double totalpaidamountinlakhs;

    private Double jan01to15actual;

    private Double jan01to15target;

    private Double jan16to31actual;

    private Double jan16to31target;

    private Double feb01to15actual;

    private Double feb01to15target;

    private Double feb16to28or29actual;

    private Double feb16to28or29target;

    private Double mar01to15actual;

    private Double mar01to15target;

    private Double mar16to31actual;

    private Double mar16to31target;

    private Double apr01to15actual;

    private Double apr01to15target;

    private Double apr16to30actual;

    private Double apr16to30target;

    private Double may01to15actual;

    private Double may01to15target;

    private Double may16to31actual;

    private Double may16to31target;

    private Double jun01to15actual;

    private Double jun01to15target;

    private Double jun16to30actual;

    private Double jun16to30target;

    private Double jul01to15actual;

    private Double jul01to15target;

    private Double jul16to31actual;

    private Double jul16to31target;

    private Double aug01to15actual;

    private Double aug01to15target;

    private Double aug16to31actual;

    private Double aug16to31target;

    private Double sep01to15actual;

    private Double sep01to15target;

    private Double sep16to30actual;

    private Double sep16to30target;

    private Double oct01to15actual;

    private Double oct01to15target;

    private Double oct16to31actual;

    private Double oct16to31target;

    private Double nov01to15actual;

    private Double nov01to15target;

    private Double nov16to30actual;

    private Double nov16to30target;

    private Double dec01to15actual;

    private Double dec01to15target;

    private Double dec16to31actual;

    private Double dec16to31target;

    private String reporttype;

    private String fund;

    private String scheme;

    private String subscheme;

    private String estimatenumber;

    private String win;

    private String nameofthework;

    private String ward;

    private String agreementnumber;

    private Date agreementdate;

    private String contractornamecode;

    private String workstatus;

    private Double contractperiod;

    private Long milestonenotcreatedcount;

    private Integer lineestimatedetailid;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getTotalnoofworks() {
        return totalnoofworks;
    }

    public void setTotalnoofworks(final Long totalnoofworks) {
        this.totalnoofworks = totalnoofworks;
    }

    public Double getTotalestimatedcostinlakhs() {
        return totalestimatedcostinlakhs;
    }

    public void setTotalestimatedcostinlakhs(final Double totalestimatedcostinlakhs) {
        this.totalestimatedcostinlakhs = totalestimatedcostinlakhs;
    }

    public Double getTotalworkordervalueinlakhs() {
        return totalworkordervalueinlakhs;
    }

    public void setTotalworkordervalueinlakhs(final Double totalworkordervalueinlakhs) {
        this.totalworkordervalueinlakhs = totalworkordervalueinlakhs;
    }

    public Double getTotalbillamountinlakhs() {
        return totalbillamountinlakhs;
    }

    public void setTotalbillamountinlakhs(final Double totalbillamountinlakhs) {
        this.totalbillamountinlakhs = totalbillamountinlakhs;
    }

    public Double getTotalpaidamountinlakhs() {
        return totalpaidamountinlakhs;
    }

    public void setTotalpaidamountinlakhs(final Double totalpaidamountinlakhs) {
        this.totalpaidamountinlakhs = totalpaidamountinlakhs;
    }

    public Double getJan01to15actual() {
        return jan01to15actual;
    }

    public void setJan01to15actual(final Double jan01to15actual) {
        this.jan01to15actual = jan01to15actual;
    }

    public Double getJan01to15target() {
        return jan01to15target;
    }

    public void setJan01to15target(final Double jan01to15target) {
        this.jan01to15target = jan01to15target;
    }

    public Double getJan01to15variance() {
        if (jan01to15actual != null && !jan01to15actual.isNaN() && jan01to15target != null && !jan01to15target.isNaN())
            return jan01to15actual - jan01to15target;
        else
            return null;
    }

    public Double getJan16to31actual() {
        return jan16to31actual;
    }

    public void setJan16to31actual(final Double jan16to31actual) {
        this.jan16to31actual = jan16to31actual;
    }

    public Double getJan16to31target() {
        return jan16to31target;
    }

    public void setJan16to31target(final Double jan16to31target) {
        this.jan16to31target = jan16to31target;
    }

    public Double getJan16to31variance() {
        if (jan16to31actual != null && !jan16to31actual.isNaN() && jan16to31target != null && !jan16to31target.isNaN())
            return jan16to31actual - jan16to31target;
        else
            return null;
    }

    public Double getFeb01to15actual() {
        return feb01to15actual;
    }

    public void setFeb01to15actual(final Double feb01to15actual) {
        this.feb01to15actual = feb01to15actual;
    }

    public Double getFeb01to15target() {
        return feb01to15target;
    }

    public void setFeb01to15target(final Double feb01to15target) {
        this.feb01to15target = feb01to15target;
    }

    public Double getFeb01to15variance() {
        if (feb01to15actual != null && !feb01to15actual.isNaN() && feb01to15target != null && !feb01to15target.isNaN())
            return feb01to15actual - feb01to15target;
        else
            return null;
    }

    public Double getFeb16to28or29actual() {
        return feb16to28or29actual;
    }

    public void setFeb16to28or29actual(final Double feb16to28or29actual) {
        this.feb16to28or29actual = feb16to28or29actual;
    }

    public Double getFeb16to28or29target() {
        return feb16to28or29target;
    }

    public void setFeb16to28or29target(final Double feb16to28or29target) {
        this.feb16to28or29target = feb16to28or29target;
    }

    public Double getFeb16to28or29variance() {
        if (feb16to28or29actual != null && !feb16to28or29actual.isNaN() && feb16to28or29target != null
                && !feb16to28or29target.isNaN())
            return feb16to28or29actual - feb16to28or29target;
        else
            return null;
    }

    public Double getMar01to15actual() {
        return mar01to15actual;
    }

    public void setMar01to15actual(final Double mar01to15actual) {
        this.mar01to15actual = mar01to15actual;
    }

    public Double getMar01to15target() {
        return mar01to15target;
    }

    public void setMar01to15target(final Double mar01to15target) {
        this.mar01to15target = mar01to15target;
    }

    public Double getMar01to15variance() {
        if (mar01to15actual != null && !mar01to15actual.isNaN() && mar01to15target != null && !mar01to15target.isNaN())
            return mar01to15actual - mar01to15target;
        else
            return null;
    }

    public Double getMar16to31actual() {
        return mar16to31actual;
    }

    public void setMar16to31actual(final Double mar16to31actual) {
        this.mar16to31actual = mar16to31actual;
    }

    public Double getMar16to31target() {
        return mar16to31target;
    }

    public void setMar16to31target(final Double mar16to31target) {
        this.mar16to31target = mar16to31target;
    }

    public Double getMar16to31variance() {
        if (mar16to31actual != null && !mar16to31actual.isNaN() && mar16to31target != null && !mar16to31target.isNaN())
            return mar16to31actual - mar16to31target;
        else
            return null;
    }

    public Double getApr01to15actual() {
        return apr01to15actual;
    }

    public void setApr01to15actual(final Double apr01to15actual) {
        this.apr01to15actual = apr01to15actual;
    }

    public Double getApr01to15target() {
        return apr01to15target;
    }

    public void setApr01to15target(final Double apr01to15target) {
        this.apr01to15target = apr01to15target;
    }

    public Double getApr01to15variance() {
        if (apr01to15actual != null && !apr01to15actual.isNaN() && apr01to15target != null && !apr01to15target.isNaN())
            return apr01to15actual - apr01to15target;
        else
            return null;
    }

    public Double getApr16to30actual() {
        return apr16to30actual;
    }

    public void setApr16to30actual(final Double apr16to30actual) {
        this.apr16to30actual = apr16to30actual;
    }

    public Double getApr16to30target() {
        return apr16to30target;
    }

    public void setApr16to30target(final Double apr16to30target) {
        this.apr16to30target = apr16to30target;
    }

    public Double getApr16to30variance() {
        if (apr16to30actual != null && !apr16to30actual.isNaN() && apr16to30target != null && !apr16to30target.isNaN())
            return apr16to30actual - apr16to30target;
        else
            return null;
    }

    public Double getMay01to15actual() {
        return may01to15actual;
    }

    public void setMay01to15actual(final Double may01to15actual) {
        this.may01to15actual = may01to15actual;
    }

    public Double getMay01to15target() {
        return may01to15target;
    }

    public void setMay01to15target(final Double may01to15target) {
        this.may01to15target = may01to15target;
    }

    public Double getMay01to15variance() {
        if (may01to15actual != null && !may01to15actual.isNaN() && may01to15target != null && !may01to15target.isNaN())
            return may01to15actual - may01to15target;
        else
            return null;
    }

    public Double getMay16to31actual() {
        return may16to31actual;
    }

    public void setMay16to31actual(final Double may16to31actual) {
        this.may16to31actual = may16to31actual;
    }

    public Double getMay16to31target() {
        return may16to31target;
    }

    public void setMay16to31target(final Double may16to31target) {
        this.may16to31target = may16to31target;
    }

    public Double getMay16to31variance() {
        if (may16to31actual != null && !may16to31actual.isNaN() && may16to31target != null && !may16to31target.isNaN())
            return may16to31actual - may16to31target;
        else
            return null;
    }

    public Double getJun01to15actual() {
        return jun01to15actual;
    }

    public void setJun01to15actual(final Double jun01to15actual) {
        this.jun01to15actual = jun01to15actual;
    }

    public Double getJun01to15target() {
        return jun01to15target;
    }

    public void setJun01to15target(final Double jun01to15target) {
        this.jun01to15target = jun01to15target;
    }

    public Double getJun01to15variance() {

        if (jun01to15actual != null && !jun01to15actual.isNaN() && jun01to15target != null && !jun01to15target.isNaN())
            return jun01to15actual - jun01to15target;
        else
            return null;
    }

    public Double getJun16to30actual() {
        return jun16to30actual;
    }

    public void setJun16to30actual(final Double jun16to30actual) {
        this.jun16to30actual = jun16to30actual;
    }

    public Double getJun16to30target() {
        return jun16to30target;
    }

    public void setJun16to30target(final Double jun16to30target) {
        this.jun16to30target = jun16to30target;
    }

    public Double getJun16to30variance() {
        if (jun16to30actual != null && !jun16to30actual.isNaN() && jun16to30target != null && !jun16to30target.isNaN())
            return jun16to30actual - jun16to30target;
        else
            return null;
    }

    public Double getJul01to15actual() {
        return jul01to15actual;
    }

    public void setJul01to15actual(final Double jul01to15actual) {
        this.jul01to15actual = jul01to15actual;
    }

    public Double getJul01to15target() {
        return jul01to15target;
    }

    public void setJul01to15target(final Double jul01to15target) {
        this.jul01to15target = jul01to15target;
    }

    public Double getJul01to15variance() {
        if (jul01to15actual != null && !jul01to15actual.isNaN() && jul01to15target != null && !jul01to15target.isNaN())
            return jul01to15actual - jul01to15target;
        else
            return null;
    }

    public Double getJul16to31actual() {
        return jul16to31actual;
    }

    public void setJul16to31actual(final Double jul16to31actual) {
        this.jul16to31actual = jul16to31actual;
    }

    public Double getJul16to31target() {
        return jul16to31target;
    }

    public void setJul16to31target(final Double jul16to31target) {
        this.jul16to31target = jul16to31target;
    }

    public Double getJul16to31variance() {
        if (jul16to31actual != null && !jul16to31actual.isNaN() && jul16to31target != null && !jul16to31target.isNaN())
            return jul16to31actual - jul16to31target;
        else
            return null;
    }

    public Double getAug01to15actual() {
        return aug01to15actual;
    }

    public void setAug01to15actual(final Double aug01to15actual) {
        this.aug01to15actual = aug01to15actual;
    }

    public Double getAug01to15target() {
        return aug01to15target;
    }

    public void setAug01to15target(final Double aug01to15target) {
        this.aug01to15target = aug01to15target;
    }

    public Double getAug01to15variance() {
        if (aug01to15actual != null && !aug01to15actual.isNaN() && aug01to15target != null && !aug01to15target.isNaN())
            return aug01to15actual - aug01to15target;
        else
            return null;
    }

    public Double getAug16to31actual() {
        return aug16to31actual;
    }

    public void setAug16to31actual(final Double aug16to31actual) {
        this.aug16to31actual = aug16to31actual;
    }

    public Double getAug16to31target() {
        return aug16to31target;
    }

    public void setAug16to31target(final Double aug16to31target) {
        this.aug16to31target = aug16to31target;
    }

    public Double getAug16to31variance() {
        if (aug16to31actual != null && !aug16to31actual.isNaN() && aug16to31target != null && !aug16to31target.isNaN())
            return aug16to31actual - aug16to31target;
        else
            return null;
    }

    public Double getSep01to15actual() {
        return sep01to15actual;
    }

    public void setSep01to15actual(final Double sep01to15actual) {
        this.sep01to15actual = sep01to15actual;
    }

    public Double getSep01to15target() {
        return sep01to15target;
    }

    public void setSep01to15target(final Double sep01to15target) {
        this.sep01to15target = sep01to15target;
    }

    public Double getSep01to15variance() {
        if (sep01to15actual != null && !sep01to15actual.isNaN() && sep01to15target != null && !sep01to15target.isNaN())
            return sep01to15actual - sep01to15target;
        else
            return null;
    }

    public Double getSep16to30actual() {
        return sep16to30actual;
    }

    public void setSep16to30actual(final Double sep16to30actual) {
        this.sep16to30actual = sep16to30actual;
    }

    public Double getSep16to30target() {
        return sep16to30target;
    }

    public void setSep16to30target(final Double sep16to30target) {
        this.sep16to30target = sep16to30target;
    }

    public Double getSep16to30variance() {
        if (sep16to30actual != null && !sep16to30actual.isNaN() && sep16to30target != null && !sep16to30target.isNaN())
            return sep16to30actual - sep16to30target;
        else
            return null;
    }

    public Double getOct01to15actual() {
        return oct01to15actual;
    }

    public void setOct01to15actual(final Double oct01to15actual) {
        this.oct01to15actual = oct01to15actual;
    }

    public Double getOct01to15target() {
        return oct01to15target;
    }

    public void setOct01to15target(final Double oct01to15target) {
        this.oct01to15target = oct01to15target;
    }

    public Double getOct01to15variance() {
        if (oct01to15actual != null && !oct01to15actual.isNaN() && oct01to15target != null && !oct01to15target.isNaN())
            return oct01to15actual - oct01to15target;
        else
            return null;
    }

    public Double getOct16to31actual() {
        return oct16to31actual;
    }

    public void setOct16to31actual(final Double oct16to31actual) {
        this.oct16to31actual = oct16to31actual;
    }

    public Double getOct16to31target() {
        return oct16to31target;
    }

    public void setOct16to31target(final Double oct16to31target) {
        this.oct16to31target = oct16to31target;
    }

    public Double getOct16to31variance() {
        if (oct16to31actual != null && !oct16to31actual.isNaN() && oct16to31target != null && !oct16to31target.isNaN())
            return oct16to31actual - oct16to31target;
        else
            return null;
    }

    public Double getNov01to15actual() {
        return nov01to15actual;
    }

    public void setNov01to15actual(final Double nov01to15actual) {
        this.nov01to15actual = nov01to15actual;
    }

    public Double getNov01to15target() {
        return nov01to15target;
    }

    public void setNov01to15target(final Double nov01to15target) {
        this.nov01to15target = nov01to15target;
    }

    public Double getNov01to15variance() {
        if (nov01to15actual != null && !nov01to15actual.isNaN() && nov01to15target != null && !nov01to15target.isNaN())
            return nov01to15actual - nov01to15target;
        else
            return null;
    }

    public Double getNov16to30actual() {
        return nov16to30actual;
    }

    public void setNov16to30actual(final Double nov16to30actual) {
        this.nov16to30actual = nov16to30actual;
    }

    public Double getNov16to30target() {
        return nov16to30target;
    }

    public void setNov16to30target(final Double nov16to30target) {
        this.nov16to30target = nov16to30target;
    }

    public Double getNov16to30variance() {
        if (nov16to30actual != null && !nov16to30actual.isNaN() && nov16to30target != null && !nov16to30target.isNaN())
            return nov16to30actual - nov16to30target;
        else
            return null;
    }

    public Double getDec01to15actual() {
        return dec01to15actual;
    }

    public void setDec01to15actual(final Double dec01to15actual) {
        this.dec01to15actual = dec01to15actual;
    }

    public Double getDec01to15target() {
        return dec01to15target;
    }

    public void setDec01to15target(final Double dec01to15target) {
        this.dec01to15target = dec01to15target;
    }

    public Double getDec01to15variance() {
        if (dec01to15actual != null && !dec01to15actual.isNaN() && dec01to15target != null && !dec01to15target.isNaN())
            return dec01to15actual - dec01to15target;
        else
            return null;
    }

    public Double getDec16to31actual() {
        return dec16to31actual;
    }

    public void setDec16to31actual(final Double dec16to31actual) {
        this.dec16to31actual = dec16to31actual;
    }

    public Double getDec16to31target() {
        return dec16to31target;
    }

    public void setDec16to31target(final Double dec16to31target) {
        this.dec16to31target = dec16to31target;
    }

    public Double getDec16to31variance() {
        if (dec16to31actual != null && !dec16to31actual.isNaN() && dec16to31target != null && !dec16to31target.isNaN())
            return dec16to31actual - dec16to31target;
        else
            return null;
    }

    public String getReporttype() {
        return reporttype;
    }

    public void setReporttype(final String reporttype) {
        this.reporttype = reporttype;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(final String fund) {
        this.fund = fund;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(final String scheme) {
        this.scheme = scheme;
    }

    public String getSubscheme() {
        return subscheme;
    }

    public void setSubscheme(final String subscheme) {
        this.subscheme = subscheme;
    }

    public String getEstimatenumber() {
        return estimatenumber;
    }

    public void setEstimatenumber(final String estimatenumber) {
        this.estimatenumber = estimatenumber;
    }

    public String getWin() {
        return win;
    }

    public void setWin(final String win) {
        this.win = win;
    }

    public String getNameofthework() {
        return nameofthework;
    }

    public void setNameofthework(final String nameofthework) {
        this.nameofthework = nameofthework;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(final String ward) {
        this.ward = ward;
    }

    public String getAgreementnumber() {
        return agreementnumber;
    }

    public void setAgreementnumber(final String agreementnumber) {
        this.agreementnumber = agreementnumber;
    }

    public Date getAgreementdate() {
        return agreementdate;
    }

    public void setAgreementdate(final Date agreementdate) {
        this.agreementdate = agreementdate;
    }

    public String getContractornamecode() {
        return contractornamecode;
    }

    public void setContractornamecode(final String contractornamecode) {
        this.contractornamecode = contractornamecode;
    }

    public String getWorkstatus() {
        return workstatus;
    }

    public void setWorkstatus(final String workstatus) {
        this.workstatus = workstatus;
    }

    public Double getContractperiod() {
        return contractperiod;
    }

    public void setContractperiod(final Double contractperiod) {
        this.contractperiod = contractperiod;
    }

    public Long getMilestonenotcreatedcount() {
        return milestonenotcreatedcount;
    }

    public void setMilestonenotcreatedcount(final Long milestonenotcreatedcount) {
        this.milestonenotcreatedcount = milestonenotcreatedcount;
    }

    public Integer getLineestimatedetailid() {
        return lineestimatedetailid;
    }

    public void setLineestimatedetailid(final Integer lineestimatedetailid) {
        this.lineestimatedetailid = lineestimatedetailid;
    }

    public String getUlbname() {
        return ulbname;
    }

    public void setUlbname(final String ulbname) {
        this.ulbname = ulbname;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(final String districtname) {
        this.districtname = districtname;
    }

    public String getIsmilestonecreated() {
        return ismilestonecreated;
    }

    public void setIsmilestonecreated(final String ismilestonecreated) {
        this.ismilestonecreated = ismilestonecreated;
    }

    public String getLatestupdatedtimestamp() {
        return latestupdatedtimestamp;
    }

    public void setLatestupdatedtimestamp(final String latestupdatedtimestamp) {
        this.latestupdatedtimestamp = latestupdatedtimestamp;
    }

    public String getTypeofwork() {
        return typeofwork;
    }

    public void setTypeofwork(final String typeofwork) {
        this.typeofwork = typeofwork;
    }

    public String getUlbcode() {
        return ulbcode;
    }

    public void setUlbcode(final String ulbcode) {
        this.ulbcode = ulbcode;
    }

    public Double getFinancialprogress() {
        return (totalpaidamountinlakhs != null ? totalpaidamountinlakhs : (double) 0)
                / (totalworkordervalueinlakhs != null ? totalworkordervalueinlakhs > 0 ? totalworkordervalueinlakhs : 1 : 1)
                * 100;
    }

}
