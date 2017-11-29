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
@Document(indexName = "workstransaction", type = "workstransactiondata")
public class WorksTransactionIndex {

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

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatenumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatedescription;

    @Field(type = FieldType.Date)
    private Date lineestimatedate;

    @Field(type = FieldType.Integer)
    private Integer lineestimateid;

    @Field(type = FieldType.Integer)
    private Integer lineestimatedetailid;

    @Field(type = FieldType.Double)
    private Double lineestimateadminamount;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatecreatedby;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatetypeofworkname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatetypeofworkcode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatesubtypeofworkname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatesubtypeofworkcode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatenatureofwork;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimateworkcategory;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimateboundary;

    @Field(type = FieldType.Boolean)
    private Boolean lineestimatespillover;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatelocation;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatenatureofallotment;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatefund;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatebudgethead;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatefunction;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatescheme;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatesubscheme;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatedepartment;

    @Field(type = FieldType.Date)
    private Date lineestimateadminsanctiondate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimateadminsanctionby;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lineestimatestatus;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String nameofthework;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String estimatenumber;

    @Field(type = FieldType.Date)
    private Date estimatedate;

    @Field(type = FieldType.Double)
    private Double estimatevalue;

    @Field(type = FieldType.Double)
    private Double estimateworkvalue;

    @Field(type = FieldType.Date)
    private Date estimateadminsanctiondate;

    @Field(type = FieldType.Date)
    private Date estimatetechsanctiondate;

    @Field(type = FieldType.Date)
    private Date estimatecouncilresolutiondate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String estimatewin;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String estimateadminsanctionby;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String estimatetechsanctionby;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String estimatestatus;

    @Field(type = FieldType.Double)
    private Double tenderpercentage;

    @Field(type = FieldType.Date)
    private Date noticeinvitingdate;

    @Field(type = FieldType.Date)
    private Date tenderdocreleasedate;

    @Field(type = FieldType.Date)
    private Date tenderopendate;

    @Field(type = FieldType.Date)
    private Date techevalutiondate;

    @Field(type = FieldType.Date)
    private Date commercialevaluationdate;

    @Field(type = FieldType.Date)
    private Date l1finalizeddate;

    @Field(type = FieldType.Date)
    private Date filedate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String filenumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String loanumber;

    @Field(type = FieldType.Date)
    private Date agreementdate;

    @Field(type = FieldType.Double)
    private Double loaamount;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String loanameofagency;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String loacontractor;

    @Field(type = FieldType.Double)
    private Double loasecuritydeposit;

    @Field(type = FieldType.Double)
    private Double loaemd;

    @Field(type = FieldType.Double)
    private Double loacontractperiod;

    @Field(type = FieldType.Double)
    private Double loadefectliabilityperiod;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String loaengineerincharge;

    @Field(type = FieldType.Date)
    private Date acceptanceletterissuedate;

    @Field(type = FieldType.Date)
    private Date acceptanceletterackdate;

    @Field(type = FieldType.Date)
    private Date agreementsigndate;

    @Field(type = FieldType.Date)
    private Date workorderackdate;

    @Field(type = FieldType.Date)
    private Date sitehandedoverdate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String loastatus;

    @Field(type = FieldType.Double)
    private Double loatotalmbamt;

    @Field(type = FieldType.Double)
    private Double loamilestonepercent;

    @Field(type = FieldType.Double)
    private Double loatotalbillamt;

    @Field(type = FieldType.Double)
    private Double loatotalpaidamt;

    @Field(type = FieldType.Double)
    private Double loamilestoneexpectedpercent;

    @Field(type = FieldType.Double)
    private Double loamilestoneexpectednextfortnight;

    @Field(type = FieldType.Double)
    private Double loamilestoneexpectednextmonth;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String workstatus;

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

    public String getLineestimatenumber() {
        return lineestimatenumber;
    }

    public void setLineestimatenumber(final String lineestimatenumber) {
        this.lineestimatenumber = lineestimatenumber;
    }

    public String getLineestimatedescription() {
        return lineestimatedescription;
    }

    public void setLineestimatedescription(final String lineestimatedescription) {
        this.lineestimatedescription = lineestimatedescription;
    }

    public Date getLineestimatedate() {
        return lineestimatedate;
    }

    public void setLineestimatedate(final Date lineestimatedate) {
        this.lineestimatedate = lineestimatedate;
    }

    public Integer getLineestimateid() {
        return lineestimateid;
    }

    public void setLineestimateid(final Integer lineestimateid) {
        this.lineestimateid = lineestimateid;
    }

    public Integer getLineestimatedetailid() {
        return lineestimatedetailid;
    }

    public void setLineestimatedetailid(final Integer lineestimatedetailid) {
        this.lineestimatedetailid = lineestimatedetailid;
    }

    public Double getLineestimateadminamount() {
        return lineestimateadminamount;
    }

    public void setLineestimateadminamount(final Double lineestimateadminamount) {
        this.lineestimateadminamount = lineestimateadminamount;
    }

    public String getLineestimatecreatedby() {
        return lineestimatecreatedby;
    }

    public void setLineestimatecreatedby(final String lineestimatecreatedby) {
        this.lineestimatecreatedby = lineestimatecreatedby;
    }

    public String getLineestimatetypeofworkname() {
        return lineestimatetypeofworkname;
    }

    public void setLineestimatetypeofworkname(final String lineestimatetypeofworkname) {
        this.lineestimatetypeofworkname = lineestimatetypeofworkname;
    }

    public String getLineestimatetypeofworkcode() {
        return lineestimatetypeofworkcode;
    }

    public void setLineestimatetypeofworkcode(final String lineestimatetypeofworkcode) {
        this.lineestimatetypeofworkcode = lineestimatetypeofworkcode;
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

    public String getLineestimatenatureofwork() {
        return lineestimatenatureofwork;
    }

    public void setLineestimatenatureofwork(final String lineestimatenatureofwork) {
        this.lineestimatenatureofwork = lineestimatenatureofwork;
    }

    public String getLineestimateworkcategory() {
        return lineestimateworkcategory;
    }

    public void setLineestimateworkcategory(final String lineestimateworkcategory) {
        this.lineestimateworkcategory = lineestimateworkcategory;
    }

    public String getLineestimateboundary() {
        return lineestimateboundary;
    }

    public void setLineestimateboundary(final String lineestimateboundary) {
        this.lineestimateboundary = lineestimateboundary;
    }

    public Boolean getLineestimatespillover() {
        return lineestimatespillover;
    }

    public void setLineestimatespillover(final Boolean lineestimatespillover) {
        this.lineestimatespillover = lineestimatespillover;
    }

    public String getLineestimatelocation() {
        return lineestimatelocation;
    }

    public void setLineestimatelocation(final String lineestimatelocation) {
        this.lineestimatelocation = lineestimatelocation;
    }

    public String getLineestimatenatureofallotment() {
        return lineestimatenatureofallotment;
    }

    public void setLineestimatenatureofallotment(final String lineestimatenatureofallotment) {
        this.lineestimatenatureofallotment = lineestimatenatureofallotment;
    }

    public String getLineestimatefund() {
        return lineestimatefund;
    }

    public void setLineestimatefund(final String lineestimatefund) {
        this.lineestimatefund = lineestimatefund;
    }

    public String getLineestimatebudgethead() {
        return lineestimatebudgethead;
    }

    public void setLineestimatebudgethead(final String lineestimatebudgethead) {
        this.lineestimatebudgethead = lineestimatebudgethead;
    }

    public String getLineestimatefunction() {
        return lineestimatefunction;
    }

    public void setLineestimatefunction(final String lineestimatefunction) {
        this.lineestimatefunction = lineestimatefunction;
    }

    public String getLineestimatescheme() {
        return lineestimatescheme;
    }

    public void setLineestimatescheme(final String lineestimatescheme) {
        this.lineestimatescheme = lineestimatescheme;
    }

    public String getLineestimatesubscheme() {
        return lineestimatesubscheme;
    }

    public void setLineestimatesubscheme(final String lineestimatesubscheme) {
        this.lineestimatesubscheme = lineestimatesubscheme;
    }

    public String getLineestimatedepartment() {
        return lineestimatedepartment;
    }

    public void setLineestimatedepartment(final String lineestimatedepartment) {
        this.lineestimatedepartment = lineestimatedepartment;
    }

    public Date getLineestimateadminsanctiondate() {
        return lineestimateadminsanctiondate;
    }

    public void setLineestimateadminsanctiondate(final Date lineestimateadminsanctiondate) {
        this.lineestimateadminsanctiondate = lineestimateadminsanctiondate;
    }

    public String getLineestimateadminsanctionby() {
        return lineestimateadminsanctionby;
    }

    public void setLineestimateadminsanctionby(final String lineestimateadminsanctionby) {
        this.lineestimateadminsanctionby = lineestimateadminsanctionby;
    }

    public String getLineestimatestatus() {
        return lineestimatestatus;
    }

    public void setLineestimatestatus(final String lineestimatestatus) {
        this.lineestimatestatus = lineestimatestatus;
    }

    public String getNameofthework() {
        return nameofthework;
    }

    public void setNameofthework(final String nameofthework) {
        this.nameofthework = nameofthework;
    }

    public String getEstimatenumber() {
        return estimatenumber;
    }

    public void setEstimatenumber(final String estimatenumber) {
        this.estimatenumber = estimatenumber;
    }

    public Date getEstimatedate() {
        return estimatedate;
    }

    public void setEstimatedate(final Date estimatedate) {
        this.estimatedate = estimatedate;
    }

    public Double getEstimatevalue() {
        return estimatevalue;
    }

    public void setEstimatevalue(final Double estimatevalue) {
        this.estimatevalue = estimatevalue;
    }

    public Double getEstimateworkvalue() {
        return estimateworkvalue;
    }

    public void setEstimateworkvalue(final Double estimateworkvalue) {
        this.estimateworkvalue = estimateworkvalue;
    }

    public Date getEstimateadminsanctiondate() {
        return estimateadminsanctiondate;
    }

    public void setEstimateadminsanctiondate(final Date estimateadminsanctiondate) {
        this.estimateadminsanctiondate = estimateadminsanctiondate;
    }

    public Date getEstimatetechsanctiondate() {
        return estimatetechsanctiondate;
    }

    public void setEstimatetechsanctiondate(final Date estimatetechsanctiondate) {
        this.estimatetechsanctiondate = estimatetechsanctiondate;
    }

    public Date getEstimatecouncilresolutiondate() {
        return estimatecouncilresolutiondate;
    }

    public void setEstimatecouncilresolutiondate(final Date estimatecouncilresolutiondate) {
        this.estimatecouncilresolutiondate = estimatecouncilresolutiondate;
    }

    public String getEstimatewin() {
        return estimatewin;
    }

    public void setEstimatewin(final String estimatewin) {
        this.estimatewin = estimatewin;
    }

    public String getEstimateadminsanctionby() {
        return estimateadminsanctionby;
    }

    public void setEstimateadminsanctionby(final String estimateadminsanctionby) {
        this.estimateadminsanctionby = estimateadminsanctionby;
    }

    public String getEstimatetechsanctionby() {
        return estimatetechsanctionby;
    }

    public void setEstimatetechsanctionby(final String estimatetechsanctionby) {
        this.estimatetechsanctionby = estimatetechsanctionby;
    }

    public String getEstimatestatus() {
        return estimatestatus;
    }

    public void setEstimatestatus(final String estimatestatus) {
        this.estimatestatus = estimatestatus;
    }

    public Double getTenderpercentage() {
        return tenderpercentage;
    }

    public void setTenderpercentage(final Double tenderpercentage) {
        this.tenderpercentage = tenderpercentage;
    }

    public Date getNoticeinvitingdate() {
        return noticeinvitingdate;
    }

    public void setNoticeinvitingdate(final Date noticeinvitingdate) {
        this.noticeinvitingdate = noticeinvitingdate;
    }

    public Date getTenderdocreleasedate() {
        return tenderdocreleasedate;
    }

    public void setTenderdocreleasedate(final Date tenderdocreleasedate) {
        this.tenderdocreleasedate = tenderdocreleasedate;
    }

    public Date getTenderopendate() {
        return tenderopendate;
    }

    public void setTenderopendate(final Date tenderopendate) {
        this.tenderopendate = tenderopendate;
    }

    public Date getTechevalutiondate() {
        return techevalutiondate;
    }

    public void setTechevalutiondate(final Date techevalutiondate) {
        this.techevalutiondate = techevalutiondate;
    }

    public Date getCommercialevaluationdate() {
        return commercialevaluationdate;
    }

    public void setCommercialevaluationdate(final Date commercialevaluationdate) {
        this.commercialevaluationdate = commercialevaluationdate;
    }

    public Date getL1finalizeddate() {
        return l1finalizeddate;
    }

    public void setL1finalizeddate(final Date l1finalizeddate) {
        this.l1finalizeddate = l1finalizeddate;
    }

    public Date getFiledate() {
        return filedate;
    }

    public void setFiledate(final Date filedate) {
        this.filedate = filedate;
    }

    public String getFilenumber() {
        return filenumber;
    }

    public void setFilenumber(final String filenumber) {
        this.filenumber = filenumber;
    }

    public String getLoanumber() {
        return loanumber;
    }

    public void setLoanumber(final String loanumber) {
        this.loanumber = loanumber;
    }

    public Date getAgreementdate() {
        return agreementdate;
    }

    public void setAgreementdate(final Date agreementdate) {
        this.agreementdate = agreementdate;
    }

    public Double getLoaamount() {
        return loaamount;
    }

    public void setLoaamount(final Double loaamount) {
        this.loaamount = loaamount;
    }

    public String getLoanameofagency() {
        return loanameofagency;
    }

    public void setLoanameofagency(final String loanameofagency) {
        this.loanameofagency = loanameofagency;
    }

    public String getLoacontractor() {
        return loacontractor;
    }

    public void setLoacontractor(final String loacontractor) {
        this.loacontractor = loacontractor;
    }

    public Double getLoasecuritydeposit() {
        return loasecuritydeposit;
    }

    public void setLoasecuritydeposit(final Double loasecuritydeposit) {
        this.loasecuritydeposit = loasecuritydeposit;
    }

    public Double getLoaemd() {
        return loaemd;
    }

    public void setLoaemd(final Double loaemd) {
        this.loaemd = loaemd;
    }

    public Double getLoacontractperiod() {
        return loacontractperiod;
    }

    public void setLoacontractperiod(final Double loacontractperiod) {
        this.loacontractperiod = loacontractperiod;
    }

    public Double getLoadefectliabilityperiod() {
        return loadefectliabilityperiod;
    }

    public void setLoadefectliabilityperiod(final Double loadefectliabilityperiod) {
        this.loadefectliabilityperiod = loadefectliabilityperiod;
    }

    public String getLoaengineerincharge() {
        return loaengineerincharge;
    }

    public void setLoaengineerincharge(final String loaengineerincharge) {
        this.loaengineerincharge = loaengineerincharge;
    }

    public Date getAcceptanceletterissuedate() {
        return acceptanceletterissuedate;
    }

    public void setAcceptanceletterissuedate(final Date acceptanceletterissuedate) {
        this.acceptanceletterissuedate = acceptanceletterissuedate;
    }

    public Date getAcceptanceletterackdate() {
        return acceptanceletterackdate;
    }

    public void setAcceptanceletterackdate(final Date acceptanceletterackdate) {
        this.acceptanceletterackdate = acceptanceletterackdate;
    }

    public Date getAgreementsigndate() {
        return agreementsigndate;
    }

    public void setAgreementsigndate(final Date agreementsigndate) {
        this.agreementsigndate = agreementsigndate;
    }

    public Date getWorkorderackdate() {
        return workorderackdate;
    }

    public void setWorkorderackdate(final Date workorderackdate) {
        this.workorderackdate = workorderackdate;
    }

    public Date getSitehandedoverdate() {
        return sitehandedoverdate;
    }

    public void setSitehandedoverdate(final Date sitehandedoverdate) {
        this.sitehandedoverdate = sitehandedoverdate;
    }

    public String getLoastatus() {
        return loastatus;
    }

    public void setLoastatus(final String loastatus) {
        this.loastatus = loastatus;
    }

    public Double getLoatotalmbamt() {
        return loatotalmbamt;
    }

    public void setLoatotalmbamt(final Double loatotalmbamt) {
        this.loatotalmbamt = loatotalmbamt;
    }

    public Double getLoamilestonepercent() {
        return loamilestonepercent;
    }

    public void setLoamilestonepercent(final Double loamilestonepercent) {
        this.loamilestonepercent = loamilestonepercent;
    }

    public Double getLoatotalbillamt() {
        return loatotalbillamt;
    }

    public void setLoatotalbillamt(final Double loatotalbillamt) {
        this.loatotalbillamt = loatotalbillamt;
    }

    public Double getLoatotalpaidamt() {
        return loatotalpaidamt;
    }

    public void setLoatotalpaidamt(final Double loatotalpaidamt) {
        this.loatotalpaidamt = loatotalpaidamt;
    }

    public Double getLoamilestoneexpectedpercent() {
        return loamilestoneexpectedpercent;
    }

    public void setLoamilestoneexpectedpercent(final Double loamilestoneexpectedpercent) {
        this.loamilestoneexpectedpercent = loamilestoneexpectedpercent;
    }

    public Double getLoamilestoneexpectednextfortnight() {
        return loamilestoneexpectednextfortnight;
    }

    public void setLoamilestoneexpectednextfortnight(final Double loamilestoneexpectednextfortnight) {
        this.loamilestoneexpectednextfortnight = loamilestoneexpectednextfortnight;
    }

    public Double getLoamilestoneexpectednextmonth() {
        return loamilestoneexpectednextmonth;
    }

    public void setLoamilestoneexpectednextmonth(final Double loamilestoneexpectednextmonth) {
        this.loamilestoneexpectednextmonth = loamilestoneexpectednextmonth;
    }

    public String getWorkstatus() {
        return workstatus;
    }

    public void setWorkstatus(final String workstatus) {
        this.workstatus = workstatus;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(final Date createddate) {
        this.createddate = createddate;
    }

}
