package org.egov.works.reports.entity;

public class ContractorWiseAbstractReport {

    private Long financialYearId;
    private Long natureOfWork;
    private String workStatus;
    private String contractor;
    private Long electionWardId;

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(final Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public Long getNatureOfWork() {
        return natureOfWork;
    }

    public void setNatureOfWork(final Long natureOfWork) {
        this.natureOfWork = natureOfWork;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(final String workStatus) {
        this.workStatus = workStatus;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(final String contractor) {
        this.contractor = contractor;
    }

    public Long getElectionWardId() {
        return electionWardId;
    }

    public void setElectionWardId(final Long electionWardId) {
        this.electionWardId = electionWardId;
    }

}
