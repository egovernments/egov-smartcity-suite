package org.egov.council.entity.es;

import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "councilmeeting", type = "councilmeeting")
public class CouncilMeetingIndex {

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String id;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String committeeType;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String meetingNumber;
    
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT)
    @Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date meetingDate;
    
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT)
    @Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date createdDate;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String meetingTime;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String meetingLocation;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String status;
    
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer totalNoOfCommitteMembers;
    
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer noOfCommitteMembersPresent;
    
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer noOfCommitteMembersAbsent;
    
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer totalNoOfPreamblesUsed;
    
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer noOfPreamblesApproved;
    
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer noOfPreamblesPostponed;
    
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer noOfPreamblesRejected;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbGrade;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbCode;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommitteeType() {
        return committeeType;
    }

    public void setCommitteeType(String committeeType) {
        this.committeeType = committeeType;
    }

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public Integer getTotalNoOfCommitteMembers() {
        return totalNoOfCommitteMembers;
    }

    public void setTotalNoOfCommitteMembers(Integer totalNoOfCommitteMembers) {
        this.totalNoOfCommitteMembers = totalNoOfCommitteMembers;
    }

    public Integer getNoOfCommitteMembersPresent() {
        return noOfCommitteMembersPresent;
    }

    public void setNoOfCommitteMembersPresent(Integer noOfCommitteMembersPresent) {
        this.noOfCommitteMembersPresent = noOfCommitteMembersPresent;
    }

    public Integer getNoOfCommitteMembersAbsent() {
        return noOfCommitteMembersAbsent;
    }

    public void setNoOfCommitteMembersAbsent(Integer noOfCommitteMembersAbsent) {
        this.noOfCommitteMembersAbsent = noOfCommitteMembersAbsent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalNoOfPreamblesUsed() {
        return totalNoOfPreamblesUsed;
    }

    public void setTotalNoOfPreamblesUsed(Integer totalNoOfPreamblesUsed) {
        this.totalNoOfPreamblesUsed = totalNoOfPreamblesUsed;
    }

    public Integer getNoOfPreamblesApproved() {
        return noOfPreamblesApproved;
    }

    public void setNoOfPreamblesApproved(Integer noOfPreamblesApproved) {
        this.noOfPreamblesApproved = noOfPreamblesApproved;
    }

    public Integer getNoOfPreamblesPostponed() {
        return noOfPreamblesPostponed;
    }

    public void setNoOfPreamblesPostponed(Integer noOfPreamblesPostponed) {
        this.noOfPreamblesPostponed = noOfPreamblesPostponed;
    }

    public Integer getNoOfPreamblesRejected() {
        return noOfPreamblesRejected;
    }

    public void setNoOfPreamblesRejected(Integer noOfPreamblesRejected) {
        this.noOfPreamblesRejected = noOfPreamblesRejected;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(String ulbGrade) {
        this.ulbGrade = ulbGrade;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
}
