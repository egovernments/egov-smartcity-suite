/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.
    Copyright (C) <2015>  eGovernments Foundation
    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.
        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.
        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.
  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.application.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_DOCHIST_DETAIL")
@SequenceGenerator(name = DocumentHistoryDetail.SEQ_DOCUMENTHISTORY_DETAIL, sequenceName = DocumentHistoryDetail.SEQ_DOCUMENTHISTORY_DETAIL, allocationSize = 1)
public class DocumentHistoryDetail extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_DOCUMENTHISTORY_DETAIL = "SEQ_EGBPA_DOCHIST_DETAIL";
    @Id
    @GeneratedValue(generator = SEQ_DOCUMENTHISTORY_DETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "docHistory")
    private DocumentHistory docHistory;
    @Length(min = 1, max = 256)
    private String surveyNumber;
    @Length(min = 1, max = 256)
    private String vendor;
    @Length(min = 1, max = 256)
    private String purchaser;
    private BigDecimal extentInsqmt;
    private BigDecimal plotorStreetNumber;
    @Transient
    private Long srlNo;
    @Length(min = 1, max = 256)
    private String natureOfDeed;
    @Length(min = 1, max = 256)
    private String remarks;
    @Temporal(value = TemporalType.DATE)
    private Date documentDate;
    @Length(min = 1, max = 128)
    private String documentType;
    @Length(min = 1, max = 256)
    private String referenceNumber;
    @Length(min = 1, max = 256)
    private String northBoundary;
    @Length(min = 1, max = 256)
    private String southBoundary;
    @Length(min = 1, max = 256)
    private String westBoundary;
    @Length(min = 1, max = 256)
    private String eastBoundary;
    @Length(min = 1, max = 128)
    private String villeagename;
    @Length(min = 1, max = 128)
    private String previousowner;
    @Length(min = 1, max = 128)
    private String presentowner;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public DocumentHistory getDocHistory() {
        return docHistory;
    }

    public void setDocHistory(final DocumentHistory docHistory) {
        this.docHistory = docHistory;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(final String vendor) {
        this.vendor = vendor;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(final String purchaser) {
        this.purchaser = purchaser;
    }

    public BigDecimal getExtentInsqmt() {
        return extentInsqmt;
    }

    public void setExtentInsqmt(final BigDecimal extentInsqmt) {
        this.extentInsqmt = extentInsqmt;
    }

    public Long getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(final Long srlNo) {
        this.srlNo = srlNo;
    }

    public String getNatureOfDeed() {
        return natureOfDeed;
    }

    public void setNatureOfDeed(final String natureOfDeed) {
        this.natureOfDeed = natureOfDeed;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getNorthBoundary() {
        return northBoundary;
    }

    public void setNorthBoundary(final String northBoundary) {
        this.northBoundary = northBoundary;
    }

    public String getSouthBoundary() {
        return southBoundary;
    }

    public void setSouthBoundary(final String southBoundary) {
        this.southBoundary = southBoundary;
    }

    public String getWestBoundary() {
        return westBoundary;
    }

    public void setWestBoundary(final String westBoundary) {
        this.westBoundary = westBoundary;
    }

    public String getEastBoundary() {
        return eastBoundary;
    }

    public void setEastBoundary(final String eastBoundary) {
        this.eastBoundary = eastBoundary;
    }

    public String getSurveyNumber() {
        return surveyNumber;
    }

    public void setSurveyNumber(final String surveyNumber) {
        this.surveyNumber = surveyNumber;
    }

    public BigDecimal getPlotorStreetNumber() {
        return plotorStreetNumber;
    }

    public void setPlotorStreetNumber(final BigDecimal plotorStreetNumber) {
        this.plotorStreetNumber = plotorStreetNumber;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(final Date documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(final String documentType) {
        this.documentType = documentType;
    }

    public String getPreviousowner() {
        return previousowner;
    }

    public void setPreviousowner(final String previousowner) {
        this.previousowner = previousowner;
    }

    public String getPresentowner() {
        return presentowner;
    }

    public void setPresentowner(final String presentowner) {
        this.presentowner = presentowner;
    }

    public String getVilleagename() {
        return villeagename;
    }

    public void setVilleagename(final String villeagename) {
        this.villeagename = villeagename;
    }

}