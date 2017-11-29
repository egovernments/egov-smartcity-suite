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

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author venki
 */
@Document(indexName = "worksmilestone", type = "worksmilestonedata")
public class WorksMilestoneIndex {

    @Id
    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbcode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String distname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbgrade;

    @Field(type = FieldType.Integer)
    private Integer lineestimatedetailid;

    @Field(type = FieldType.Integer)
    private Integer estimateid;

    @Field(type = FieldType.Date)
    private Date milestonelastupdateddate;

    @Field(type = FieldType.Double)
    private Double loatotalpaidamt;

    @Field(type = FieldType.Double)
    private Double loatotalbillamt;

    @Field(type = FieldType.Double)
    private Double loaamount;

    @Field(type = FieldType.Double)
    private Double estimatevalue;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatetypeofworkname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatetypeofworkcode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatesubtypeofworkname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatesubtypeofworkcode;

    @Field(type = FieldType.Date)
    private Date createddate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String milestonestatus;

    @Field(type = FieldType.Double)
    private Double jan01to15actual;

    @Field(type = FieldType.Double)
    private Double jan01to15target;

    @Field(type = FieldType.Double)
    private Double jan16to31actual;

    @Field(type = FieldType.Double)
    private Double jan16to31target;

    @Field(type = FieldType.Double)
    private Double feb01to15actual;

    @Field(type = FieldType.Double)
    private Double feb01to15target;

    @Field(type = FieldType.Double)
    private Double feb16to28or29actual;

    @Field(type = FieldType.Double)
    private Double feb16to28or29target;

    @Field(type = FieldType.Double)
    private Double mar01to15actual;

    @Field(type = FieldType.Double)
    private Double mar01to15target;

    @Field(type = FieldType.Double)
    private Double mar16to31actual;

    @Field(type = FieldType.Double)
    private Double mar16to31target;

    @Field(type = FieldType.Double)
    private Double apr01to15actual;

    @Field(type = FieldType.Double)
    private Double apr01to15target;

    @Field(type = FieldType.Double)
    private Double apr16to30actual;

    @Field(type = FieldType.Double)
    private Double apr16to30target;

    @Field(type = FieldType.Double)
    private Double may01to15actual;

    @Field(type = FieldType.Double)
    private Double may01to15target;

    @Field(type = FieldType.Double)
    private Double may16to31actual;

    @Field(type = FieldType.Double)
    private Double may16to31target;

    @Field(type = FieldType.Double)
    private Double jun01to15actual;

    @Field(type = FieldType.Double)
    private Double jun01to15target;

    @Field(type = FieldType.Double)
    private Double jun16to30actual;

    @Field(type = FieldType.Double)
    private Double jun16to30target;

    @Field(type = FieldType.Double)
    private Double jul01to15actual;

    @Field(type = FieldType.Double)
    private Double jul01to15target;

    @Field(type = FieldType.Double)
    private Double jul16to31actual;

    @Field(type = FieldType.Double)
    private Double jul16to31target;

    @Field(type = FieldType.Double)
    private Double aug01to15actual;

    @Field(type = FieldType.Double)
    private Double aug01to15target;

    @Field(type = FieldType.Double)
    private Double aug16to31actual;

    @Field(type = FieldType.Double)
    private Double aug16to31target;

    @Field(type = FieldType.Double)
    private Double sep01to15actual;

    @Field(type = FieldType.Double)
    private Double sep01to15target;

    @Field(type = FieldType.Double)
    private Double sep16to30actual;

    @Field(type = FieldType.Double)
    private Double sep16to30target;

    @Field(type = FieldType.Double)
    private Double oct01to15actual;

    @Field(type = FieldType.Double)
    private Double oct01to15target;

    @Field(type = FieldType.Double)
    private Double oct16to31actual;

    @Field(type = FieldType.Double)
    private Double oct16to31target;

    @Field(type = FieldType.Double)
    private Double nov01to15actual;

    @Field(type = FieldType.Double)
    private Double nov01to15target;

    @Field(type = FieldType.Double)
    private Double nov16to30actual;

    @Field(type = FieldType.Double)
    private Double nov16to30target;

    @Field(type = FieldType.Double)
    private Double dec01to15actual;

    @Field(type = FieldType.Double)
    private Double dec01to15target;

    @Field(type = FieldType.Double)
    private Double dec16to31actual;

    @Field(type = FieldType.Double)
    private Double dec16to31target;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUlbname() {
        return ulbname;
    }

    public void setUlbname(final String ulbname) {
        this.ulbname = ulbname;
    }

    public String getUlbcode() {
        return ulbcode;
    }

    public void setUlbcode(final String ulbcode) {
        this.ulbcode = ulbcode;
    }

    public String getDistname() {
        return distname;
    }

    public void setDistname(final String distname) {
        this.distname = distname;
    }

    public String getRegname() {
        return regname;
    }

    public void setRegname(final String regname) {
        this.regname = regname;
    }

    public String getUlbgrade() {
        return ulbgrade;
    }

    public void setUlbgrade(final String ulbgrade) {
        this.ulbgrade = ulbgrade;
    }

    public Integer getLineestimatedetailid() {
        return lineestimatedetailid;
    }

    public void setLineestimatedetailid(final Integer lineestimatedetailid) {
        this.lineestimatedetailid = lineestimatedetailid;
    }

    public Integer getEstimateid() {
        return estimateid;
    }

    public void setEstimateid(final Integer estimateid) {
        this.estimateid = estimateid;
    }

    public Date getMilestonelastupdateddate() {
        return milestonelastupdateddate;
    }

    public void setMilestonelastupdateddate(final Date milestonelastupdateddate) {
        this.milestonelastupdateddate = milestonelastupdateddate;
    }

    public Double getLoatotalpaidamt() {
        return loatotalpaidamt;
    }

    public void setLoatotalpaidamt(final Double loatotalpaidamt) {
        this.loatotalpaidamt = loatotalpaidamt;
    }

    public Double getLoatotalbillamt() {
        return loatotalbillamt;
    }

    public void setLoatotalbillamt(final Double loatotalbillamt) {
        this.loatotalbillamt = loatotalbillamt;
    }

    public Double getLoaamount() {
        return loaamount;
    }

    public void setLoaamount(final Double loaamount) {
        this.loaamount = loaamount;
    }

    public Double getEstimatevalue() {
        return estimatevalue;
    }

    public void setEstimatevalue(final Double estimatevalue) {
        this.estimatevalue = estimatevalue;
    }

    public String getLineestimatetypeofworkname() {
        return lineestimatetypeofworkname;
    }

    public void setLineestimatetypeofworkname(final String lineestimatetypeofworkname) {
        this.lineestimatetypeofworkname = lineestimatetypeofworkname;
    }

    public String getLineestimatesubtypeofworkname() {
        return lineestimatesubtypeofworkname;
    }

    public void setLineestimatesubtypeofworkname(final String lineestimatesubtypeofworkname) {
        this.lineestimatesubtypeofworkname = lineestimatesubtypeofworkname;
    }

    public String getLineestimatesubtypeofworkcode() {
        return lineestimatesubtypeofworkcode;
    }

    public void setLineestimatesubtypeofworkcode(final String lineestimatesubtypeofworkcode) {
        this.lineestimatesubtypeofworkcode = lineestimatesubtypeofworkcode;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(final Date createddate) {
        this.createddate = createddate;
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

    public String getLineestimatetypeofworkcode() {
        return lineestimatetypeofworkcode;
    }

    public void setLineestimatetypeofworkcode(final String lineestimatetypeofworkcode) {
        this.lineestimatetypeofworkcode = lineestimatetypeofworkcode;
    }

    public String getMilestonestatus() {
        return milestonestatus;
    }

    public void setMilestonestatus(final String milestonestatus) {
        this.milestonestatus = milestonestatus;
    }

}
