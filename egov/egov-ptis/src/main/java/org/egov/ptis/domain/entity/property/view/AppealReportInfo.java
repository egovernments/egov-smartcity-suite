package org.egov.ptis.domain.entity.property.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AppealReportInfo implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 337555223482071890L;

    private Long slNo;
    private String electionWard;
    private String ownerName;
    private Date assessmentDate;
    private String assessmentNo;
    private Date specialNoticeDate;
    private Date appealCreatedDate;
    private String revisionPetitionNo;
    private Date revisionPetitionApprovedDate;
    private BigDecimal previousTax;
    private String groundOfAppeal;
    private BigDecimal collection;
    private Date receiptDate;
    private String appeallateComments;
    private BigDecimal currentTax;
    private Date appealApprovedDate;
    private Date disposalDate;
    private String appeallateApplicationNo;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public Date getSpecialNoticeDate() {
        return specialNoticeDate;
    }

    public void setSpecialNoticeDate(Date specialNoticeDate) {
        this.specialNoticeDate = specialNoticeDate;
    }

    public Date getAppealCreatedDate() {
        return appealCreatedDate;
    }

    public void setAppealCreatedDate(Date appealCreatedDate) {
        this.appealCreatedDate = appealCreatedDate;
    }

    public String getRevisionPetitionNo() {
        return revisionPetitionNo;
    }

    public void setRevisionPetitionNo(String revisionPetitionNo) {
        this.revisionPetitionNo = revisionPetitionNo;
    }

    public Date getRevisionPetitionApprovedDate() {
        return revisionPetitionApprovedDate;
    }

    public void setRevisionPetitionApprovedDate(Date revisionPetitionApprovedDate) {
        this.revisionPetitionApprovedDate = revisionPetitionApprovedDate;
    }

    public BigDecimal getPreviousTax() {
        return previousTax;
    }

    public void setPreviousTax(BigDecimal previousTax) {
        this.previousTax = previousTax;
    }

    public String getGroundOfAppeal() {
        return groundOfAppeal;
    }

    public void setGroundOfAppeal(String groundOfAppeal) {
        this.groundOfAppeal = groundOfAppeal;
    }

    public BigDecimal getCollection() {
        return collection;
    }

    public void setCollection(BigDecimal collection) {
        this.collection = collection;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getAppeallateComments() {
        return appeallateComments;
    }

    public void setAppeallateComments(String appeallateComments) {
        this.appeallateComments = appeallateComments;
    }

    public BigDecimal getCurrentTax() {
        return currentTax;
    }

    public void setCurrentTax(BigDecimal currentTax) {
        this.currentTax = currentTax;
    }

    public Date getAppealDate() {
        return appealApprovedDate;
    }

    public void setAppealApprovedDate(Date appealApprovedDate) {
        this.appealApprovedDate = appealApprovedDate;
    }

    public Date getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(Date disposalDate) {
        this.disposalDate = disposalDate;
    }

    public String getElectionWard() {
        return electionWard;
    }

    public void setElectionWard(String electionWard) {
        this.electionWard = electionWard;
    }

    public Long getSlNo() {
        return slNo;
    }

    public void setSlNo(Long slNo) {
        this.slNo = slNo;
    }

    public Date getAppealApprovedDate() {
        return appealApprovedDate;
    }

    public String getAppeallateApplicationNo() {
        return appeallateApplicationNo;
    }

    public void setAppeallateApplicationNo(String appeallateApplicationNo) {
        this.appeallateApplicationNo = appeallateApplicationNo;
    }
}
