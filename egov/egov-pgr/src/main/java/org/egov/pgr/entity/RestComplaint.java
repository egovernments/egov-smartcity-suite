package org.egov.pgr.entity;

public class RestComplaint {
    String approvalUserName;
    String status;
    String approvalComment;

    public String getApprovalUserName() {
        return approvalUserName;
    }

    public void setApprovalUserName(final String approvalUserName) {
        this.approvalUserName = approvalUserName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(final String approvalComment) {
        this.approvalComment = approvalComment;
    }

}