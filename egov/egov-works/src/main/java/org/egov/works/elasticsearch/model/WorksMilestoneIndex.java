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

package org.egov.works.elasticsearch.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
    private Double loatotalpaidamt;

    @Field(type = FieldType.Double)
    private Double loatotalbillamt;

    @Field(type = FieldType.Double)
    private Double loaamount;

    @Field(type = FieldType.Double)
    private Double estimatevalue;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatetypeofwork;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatesubtypeofwork;

    @Field(type = FieldType.Date)
    private Date createddate;

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

    public String getLineestimatetypeofwork() {
        return lineestimatetypeofwork;
    }

    public void setLineestimatetypeofwork(final String lineestimatetypeofwork) {
        this.lineestimatetypeofwork = lineestimatetypeofwork;
    }

    public String getLineestimatesubtypeofwork() {
        return lineestimatesubtypeofwork;
    }

    public void setLineestimatesubtypeofwork(final String lineestimatesubtypeofwork) {
        this.lineestimatesubtypeofwork = lineestimatesubtypeofwork;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(final Date createddate) {
        this.createddate = createddate;
    }

}
