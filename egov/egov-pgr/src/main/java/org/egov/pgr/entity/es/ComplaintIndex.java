package org.egov.pgr.entity.es;

import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;
import static org.egov.infra.utils.ApplicationConstant.DEFAULT_TIMEZONE;
import static org.egov.pgr.utils.constants.PGRConstants.PGR_INDEX_NAME;
import static org.springframework.data.elasticsearch.annotations.DateFormat.date_optional_time;
import static org.springframework.data.elasticsearch.annotations.FieldIndex.not_analyzed;

import java.util.Date;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = PGR_INDEX_NAME, type = PGR_INDEX_NAME)
public class ComplaintIndex {

    @Id
    private String id;

    @Field(type = FieldType.String, index = not_analyzed)
    private String crn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, index = not_analyzed, format = date_optional_time, pattern = ES_DATE_FORMAT)
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, index = not_analyzed, format = date_optional_time, pattern = ES_DATE_FORMAT)
    private Date escalationDate;

    @Field(type = FieldType.String, index = not_analyzed)
    private String complaintStatusName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String complainantName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String complainantMobile;

    @Field(type = FieldType.String, index = not_analyzed)
    private String complainantEmail;

    @Field(type = FieldType.String, index = not_analyzed)
    private String complaintTypeName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String complaintTypeCode;

    @Field(type = FieldType.Long)
    private Long assigneeId;
    
    @Field(type = FieldType.String, index = not_analyzed)
    private String assigneeName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String details;

    @Field(type = FieldType.String, index = not_analyzed)
    private String landmarkDetails;

    @Field(type = FieldType.String, index = not_analyzed)
    private String receivingMode;

    @Field(type = FieldType.String, index = not_analyzed)
    private String departmentName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String departmentCode;

    @Field(type = FieldType.String, index = not_analyzed)
    private String wardName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String wardNo;

    @GeoPointField
    private GeoPoint wardGeo;

    @Field(type = FieldType.String, index = not_analyzed)
    private String localityName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String localityNo;

    @GeoPointField
    private GeoPoint localityGeo;

    @GeoPointField
    private GeoPoint complaintGeo;

    @Field(type = FieldType.Double)
    private double satisfactionIndex;

    @Field(type = FieldType.Long)
    private long complaintAgeingdaysFromDue;

    //ComplaintIndex additional fields
    @Field(type = FieldType.String, index = not_analyzed)
    private String cityCode;

    @Field(type = FieldType.String, index = not_analyzed)
    private String cityDistrictCode;

    @Field(type = FieldType.String, index = not_analyzed)
    private String cityDistrictName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String cityGrade;

    @Field(type = FieldType.String, index = not_analyzed)
    private String cityRegionName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String cityName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String cityDomainUrl;

    @Field(type = FieldType.Double)
    private double complaintDuration;

    @Field(type = FieldType.Boolean)
    private boolean closed;

    @Field(type = FieldType.String, index = not_analyzed)
    private String complaintIsClosed;

    @Field(type = FieldType.Integer)
    private int ifClosed;

    @Field(type = FieldType.String, index = not_analyzed)
    private String durationRange;

    @Field(type = FieldType.String, index = not_analyzed)
    private String source;

    @Field(type = FieldType.Double)
    private double complaintPeriod;

    @Field(type = FieldType.Long)
    private long complaintSLADays;

    @Field(type = FieldType.Double)
    private double complaintAgeingFromDue;

    @Field(type = FieldType.String, index = not_analyzed)
    private String isSLA;

    @Field(type = FieldType.Integer)
    private int ifSLA;

    @Field(type = FieldType.String, index = not_analyzed)
    private String currentFunctionaryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, index = not_analyzed, format = date_optional_time, pattern = ES_DATE_FORMAT)
    private Date currentFunctionaryAssigneddate;

    @Field(type = FieldType.Long)
    private long currentFunctionarySLADays;

    @Field(type = FieldType.Double)
    private double currentFunctionaryAgeingFromDue;

    @Field(type = FieldType.String, index = not_analyzed)
    private String currentFunctionaryIsSLA;

    @Field(type = FieldType.Integer)
    private int currentFunctionaryIfSLA;
    
    @Field(type = FieldType.String, index = not_analyzed)
    private String currentFunctionaryMobileNumber;

    @Field(type = FieldType.String, index = not_analyzed)
    private String closedByFunctionaryName;

    @Field(type = FieldType.String, index = not_analyzed)
    private String initialFunctionaryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, index = not_analyzed, format = date_optional_time, pattern = ES_DATE_FORMAT)
    private Date initialFunctionaryAssigneddate;

    @Field(type = FieldType.Long)
    private long initialFunctionarySLADays;

    @Field(type = FieldType.Double)
    private double initialFunctionaryAgeingFromDue;

    @Field(type = FieldType.String, index = not_analyzed)
    private String initialFunctionaryIsSLA;

    @Field(type = FieldType.Integer)
    private int initialFunctionaryIfSLA;

    @Field(type = FieldType.String, index = not_analyzed)
    private String escalation1FunctionaryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, index = not_analyzed, format = date_optional_time, pattern = ES_DATE_FORMAT)
    private Date escalation1FunctionaryAssigneddate;

    @Field(type = FieldType.Long)
    private long escalation1FunctionarySLADays;

    @Field(type = FieldType.Double)
    private double escalation1FunctionaryAgeingFromDue;

    @Field(type = FieldType.String, index = not_analyzed)
    private String escalation1FunctionaryIsSLA;

    @Field(type = FieldType.Integer)
    private int escalation1FunctionaryIfSLA;

    @Field(type = FieldType.String, index = not_analyzed)
    private String escalation2FunctionaryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, index = not_analyzed, format = date_optional_time, pattern = ES_DATE_FORMAT)
    private Date escalation2FunctionaryAssigneddate;

    @Field(type = FieldType.Long)
    private long escalation2FunctionarySLADays;

    @Field(type = FieldType.Double)
    private double escalation2FunctionaryAgeingFromDue;

    @Field(type = FieldType.String, index = not_analyzed)
    private String escalation2FunctionaryIsSLA;

    @Field(type = FieldType.Integer)
    private int escalation2FunctionaryIfSLA;

    @Field(type = FieldType.String, index = not_analyzed)
    private String escalation3FunctionaryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, index = not_analyzed, format = date_optional_time, pattern = ES_DATE_FORMAT)
    private Date escalation3FunctionaryAssigneddate;

    @Field(type = FieldType.Long)
    private long escalation3FunctionarySLADays;

    @Field(type = FieldType.Double)
    private double escalation3FunctionaryAgeingFromDue;

    @Field(type = FieldType.String, index = not_analyzed)
    private String escalation3FunctionaryIsSLA;

    @Field(type = FieldType.Integer)
    private int escalation3FunctionaryIfSLA;

    @Field(type = FieldType.Integer)
    private int escalationLevel;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, index = not_analyzed, format = date_optional_time, pattern = ES_DATE_FORMAT)
    private Date complaintReOpenedDate;

    @Field(type = FieldType.String, index = not_analyzed)
    private String reasonForRejection;

    @Field(type = FieldType.Integer)
    private int registered;

    @Field(type = FieldType.Integer)
    private int inProcess;

    @Field(type = FieldType.Integer)
    private int addressed;

    @Field(type = FieldType.Integer)
    private int rejected;

    @Field(type = FieldType.Integer)
    private int reOpened;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = ApplicationThreadLocals.getCityCode().concat("_").concat(id);
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEscalationDate() {
        return escalationDate;
    }

    public void setEscalationDate(Date escalationDate) {
        this.escalationDate = escalationDate;
    }

    public String getComplaintStatusName() {
        return complaintStatusName;
    }

    public void setComplaintStatusName(String complaintStatusName) {
        this.complaintStatusName = complaintStatusName;
    }

    public String getComplainantName() {
        return complainantName;
    }

    public void setComplainantName(String complainantName) {
        this.complainantName = complainantName;
    }

    public String getComplainantMobile() {
        return complainantMobile;
    }

    public void setComplainantMobile(String complainantMobile) {
        this.complainantMobile = complainantMobile;
    }

    public String getComplainantEmail() {
        return complainantEmail;
    }

    public void setComplainantEmail(String complainantEmail) {
        this.complainantEmail = complainantEmail;
    }

    public String getComplaintTypeName() {
        return complaintTypeName;
    }

    public void setComplaintTypeName(String complaintTypeName) {
        this.complaintTypeName = complaintTypeName;
    }

    public String getComplaintTypeCode() {
        return complaintTypeCode;
    }

    public void setComplaintTypeCode(String complaintTypeCode) {
        this.complaintTypeCode = complaintTypeCode;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLandmarkDetails() {
        return landmarkDetails;
    }

    public void setLandmarkDetails(String landmarkDetails) {
        this.landmarkDetails = landmarkDetails;
    }

    public String getReceivingMode() {
        return receivingMode;
    }

    public void setReceivingMode(String receivingMode) {
        this.receivingMode = receivingMode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public GeoPoint getWardGeo() {
        return wardGeo;
    }

    public void setWardGeo(GeoPoint wardGeo) {
        this.wardGeo = wardGeo;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getLocalityNo() {
        return localityNo;
    }

    public void setLocalityNo(String localityNo) {
        this.localityNo = localityNo;
    }

    public GeoPoint getLocalityGeo() {
        return localityGeo;
    }

    public void setLocalityGeo(GeoPoint localityGeo) {
        this.localityGeo = localityGeo;
    }

    public GeoPoint getComplaintGeo() {
        return complaintGeo;
    }

    public void setComplaintGeo(GeoPoint complaintGeo) {
        this.complaintGeo = complaintGeo;
    }

    public double getSatisfactionIndex() {
        return satisfactionIndex;
    }

    public void setSatisfactionIndex(double satisfactionIndex) {
        this.satisfactionIndex = satisfactionIndex;
    }

    public long getComplaintAgeingdaysFromDue() {
        return complaintAgeingdaysFromDue;
    }

    public void setComplaintAgeingdaysFromDue(long complaintAgeingdaysFromDue) {
        this.complaintAgeingdaysFromDue = complaintAgeingdaysFromDue;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityDistrictCode() {
        return cityDistrictCode;
    }

    public void setCityDistrictCode(String cityDistrictCode) {
        this.cityDistrictCode = cityDistrictCode;
    }

    public String getCityDistrictName() {
        return cityDistrictName;
    }

    public void setCityDistrictName(String cityDistrictName) {
        this.cityDistrictName = cityDistrictName;
    }

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(String cityGrade) {
        this.cityGrade = cityGrade;
    }

    public String getCityRegionName() {
        return cityRegionName;
    }

    public void setCityRegionName(String cityRegionName) {
        this.cityRegionName = cityRegionName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityDomainUrl() {
        return cityDomainUrl;
    }

    public void setCityDomainUrl(String cityDomainUrl) {
        this.cityDomainUrl = cityDomainUrl;
    }

    public double getComplaintDuration() {
        return complaintDuration;
    }

    public void setComplaintDuration(double complaintDuration) {
        this.complaintDuration = complaintDuration;
    }

    public boolean getClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getComplaintIsClosed() {
        return complaintIsClosed;
    }

    public void setComplaintIsClosed(String complaintIsClosed) {
        this.complaintIsClosed = complaintIsClosed;
    }

    public int getIfClosed() {
        return ifClosed;
    }

    public void setIfClosed(int ifClosed) {
        this.ifClosed = ifClosed;
    }

    public String getDurationRange() {
        return durationRange;
    }

    public void setDurationRange(String durationRange) {
        this.durationRange = durationRange;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getComplaintPeriod() {
        return complaintPeriod;
    }

    public void setComplaintPeriod(double complaintPeriod) {
        this.complaintPeriod = complaintPeriod;
    }

    public long getComplaintSLADays() {
        return complaintSLADays;
    }

    public void setComplaintSLADays(long complaintSLADays) {
        this.complaintSLADays = complaintSLADays;
    }

    public double getComplaintAgeingFromDue() {
        return complaintAgeingFromDue;
    }

    public void setComplaintAgeingFromDue(double complaintAgeingFromDue) {
        this.complaintAgeingFromDue = complaintAgeingFromDue;
    }

    public String getIsSLA() {
        return isSLA;
    }

    public void setIsSLA(String isSLA) {
        this.isSLA = isSLA;
    }

    public int getIfSLA() {
        return ifSLA;
    }

    public void setIfSLA(int ifSLA) {
        this.ifSLA = ifSLA;
    }

    public String getCurrentFunctionaryName() {
        return currentFunctionaryName;
    }

    public void setCurrentFunctionaryName(String currentFunctionaryName) {
        this.currentFunctionaryName = currentFunctionaryName;
    }

    public Date getCurrentFunctionaryAssigneddate() {
        return currentFunctionaryAssigneddate;
    }

    public void setCurrentFunctionaryAssigneddate(
            Date currentFunctionaryAssigneddate) {
        this.currentFunctionaryAssigneddate = currentFunctionaryAssigneddate;
    }

    public long getCurrentFunctionarySLADays() {
        return currentFunctionarySLADays;
    }

    public void setCurrentFunctionarySLADays(long currentFunctionarySLADays) {
        this.currentFunctionarySLADays = currentFunctionarySLADays;
    }

    public double getCurrentFunctionaryAgeingFromDue() {
        return currentFunctionaryAgeingFromDue;
    }

    public void setCurrentFunctionaryAgeingFromDue(
            double currentFunctionaryAgeingFromDue) {
        this.currentFunctionaryAgeingFromDue = currentFunctionaryAgeingFromDue;
    }

    public String getCurrentFunctionaryIsSLA() {
        return currentFunctionaryIsSLA;
    }

    public void setCurrentFunctionaryIsSLA(String currentFunctionaryIsSLA) {
        this.currentFunctionaryIsSLA = currentFunctionaryIsSLA;
    }

    public int getCurrentFunctionaryIfSLA() {
        return currentFunctionaryIfSLA;
    }

    public void setCurrentFunctionaryIfSLA(int currentFunctionaryIfSLA) {
        this.currentFunctionaryIfSLA = currentFunctionaryIfSLA;
    }

    public String getClosedByFunctionaryName() {
        return closedByFunctionaryName;
    }

    public void setClosedByFunctionaryName(String closedByFunctionaryName) {
        this.closedByFunctionaryName = closedByFunctionaryName;
    }

    public String getInitialFunctionaryName() {
        return initialFunctionaryName;
    }

    public void setInitialFunctionaryName(String initialFunctionaryName) {
        this.initialFunctionaryName = initialFunctionaryName;
    }

    public Date getInitialFunctionaryAssigneddate() {
        return initialFunctionaryAssigneddate;
    }

    public void setInitialFunctionaryAssigneddate(
            Date initialFunctionaryAssigneddate) {
        this.initialFunctionaryAssigneddate = initialFunctionaryAssigneddate;
    }

    public long getInitialFunctionarySLADays() {
        return initialFunctionarySLADays;
    }

    public void setInitialFunctionarySLADays(long initialFunctionarySLADays) {
        this.initialFunctionarySLADays = initialFunctionarySLADays;
    }

    public double getInitialFunctionaryAgeingFromDue() {
        return initialFunctionaryAgeingFromDue;
    }

    public void setInitialFunctionaryAgeingFromDue(
            double initialFunctionaryAgeingFromDue) {
        this.initialFunctionaryAgeingFromDue = initialFunctionaryAgeingFromDue;
    }

    public String getInitialFunctionaryIsSLA() {
        return initialFunctionaryIsSLA;
    }

    public void setInitialFunctionaryIsSLA(String initialFunctionaryIsSLA) {
        this.initialFunctionaryIsSLA = initialFunctionaryIsSLA;
    }

    public int getInitialFunctionaryIfSLA() {
        return initialFunctionaryIfSLA;
    }

    public void setInitialFunctionaryIfSLA(int initialFunctionaryIfSLA) {
        this.initialFunctionaryIfSLA = initialFunctionaryIfSLA;
    }

    public String getEscalation1FunctionaryName() {
        return escalation1FunctionaryName;
    }

    public void setEscalation1FunctionaryName(String escalation1FunctionaryName) {
        this.escalation1FunctionaryName = escalation1FunctionaryName;
    }

    public Date getEscalation1FunctionaryAssigneddate() {
        return escalation1FunctionaryAssigneddate;
    }

    public void setEscalation1FunctionaryAssigneddate(
            Date escalation1FunctionaryAssigneddate) {
        this.escalation1FunctionaryAssigneddate = escalation1FunctionaryAssigneddate;
    }

    public long getEscalation1FunctionarySLADays() {
        return escalation1FunctionarySLADays;
    }

    public void setEscalation1FunctionarySLADays(long escalation1FunctionarySLADays) {
        this.escalation1FunctionarySLADays = escalation1FunctionarySLADays;
    }

    public double getEscalation1FunctionaryAgeingFromDue() {
        return escalation1FunctionaryAgeingFromDue;
    }

    public void setEscalation1FunctionaryAgeingFromDue(
            double escalation1FunctionaryAgeingFromDue) {
        this.escalation1FunctionaryAgeingFromDue = escalation1FunctionaryAgeingFromDue;
    }

    public String getEscalation1FunctionaryIsSLA() {
        return escalation1FunctionaryIsSLA;
    }

    public void setEscalation1FunctionaryIsSLA(String escalation1FunctionaryIsSLA) {
        this.escalation1FunctionaryIsSLA = escalation1FunctionaryIsSLA;
    }

    public int getEscalation1FunctionaryIfSLA() {
        return escalation1FunctionaryIfSLA;
    }

    public void setEscalation1FunctionaryIfSLA(int escalation1FunctionaryIfSLA) {
        this.escalation1FunctionaryIfSLA = escalation1FunctionaryIfSLA;
    }

    public String getEscalation2FunctionaryName() {
        return escalation2FunctionaryName;
    }

    public void setEscalation2FunctionaryName(String escalation2FunctionaryName) {
        this.escalation2FunctionaryName = escalation2FunctionaryName;
    }

    public Date getEscalation2FunctionaryAssigneddate() {
        return escalation2FunctionaryAssigneddate;
    }

    public void setEscalation2FunctionaryAssigneddate(
            Date escalation2FunctionaryAssigneddate) {
        this.escalation2FunctionaryAssigneddate = escalation2FunctionaryAssigneddate;
    }

    public long getEscalation2FunctionarySLADays() {
        return escalation2FunctionarySLADays;
    }

    public void setEscalation2FunctionarySLADays(long escalation2FunctionarySLADays) {
        this.escalation2FunctionarySLADays = escalation2FunctionarySLADays;
    }

    public double getEscalation2FunctionaryAgeingFromDue() {
        return escalation2FunctionaryAgeingFromDue;
    }

    public void setEscalation2FunctionaryAgeingFromDue(
            double escalation2FunctionaryAgeingFromDue) {
        this.escalation2FunctionaryAgeingFromDue = escalation2FunctionaryAgeingFromDue;
    }

    public String getEscalation2FunctionaryIsSLA() {
        return escalation2FunctionaryIsSLA;
    }

    public void setEscalation2FunctionaryIsSLA(String escalation2FunctionaryIsSLA) {
        this.escalation2FunctionaryIsSLA = escalation2FunctionaryIsSLA;
    }

    public int getEscalation2FunctionaryIfSLA() {
        return escalation2FunctionaryIfSLA;
    }

    public void setEscalation2FunctionaryIfSLA(int escalation2FunctionaryIfSLA) {
        this.escalation2FunctionaryIfSLA = escalation2FunctionaryIfSLA;
    }

    public String getEscalation3FunctionaryName() {
        return escalation3FunctionaryName;
    }

    public void setEscalation3FunctionaryName(String escalation3FunctionaryName) {
        this.escalation3FunctionaryName = escalation3FunctionaryName;
    }

    public Date getEscalation3FunctionaryAssigneddate() {
        return escalation3FunctionaryAssigneddate;
    }

    public void setEscalation3FunctionaryAssigneddate(
            Date escalation3FunctionaryAssigneddate) {
        this.escalation3FunctionaryAssigneddate = escalation3FunctionaryAssigneddate;
    }

    public long getEscalation3FunctionarySLADays() {
        return escalation3FunctionarySLADays;
    }

    public void setEscalation3FunctionarySLADays(long escalation3FunctionarySLADays) {
        this.escalation3FunctionarySLADays = escalation3FunctionarySLADays;
    }

    public double getEscalation3FunctionaryAgeingFromDue() {
        return escalation3FunctionaryAgeingFromDue;
    }

    public void setEscalation3FunctionaryAgeingFromDue(
            double escalation3FunctionaryAgeingFromDue) {
        this.escalation3FunctionaryAgeingFromDue = escalation3FunctionaryAgeingFromDue;
    }

    public String getEscalation3FunctionaryIsSLA() {
        return escalation3FunctionaryIsSLA;
    }

    public void setEscalation3FunctionaryIsSLA(String escalation3FunctionaryIsSLA) {
        this.escalation3FunctionaryIsSLA = escalation3FunctionaryIsSLA;
    }

    public int getEscalation3FunctionaryIfSLA() {
        return escalation3FunctionaryIfSLA;
    }

    public void setEscalation3FunctionaryIfSLA(int escalation3FunctionaryIfSLA) {
        this.escalation3FunctionaryIfSLA = escalation3FunctionaryIfSLA;
    }

    public int getEscalationLevel() {
        return escalationLevel;
    }

    public void setEscalationLevel(int escalationLevel) {
        this.escalationLevel = escalationLevel;
    }

    public Date getComplaintReOpenedDate() {
        return complaintReOpenedDate;
    }

    public void setComplaintReOpenedDate(Date complaintReOpenedDate) {
        this.complaintReOpenedDate = complaintReOpenedDate;
    }

    public String getReasonForRejection() {
        return reasonForRejection;
    }

    public void setReasonForRejection(String reasonForRejection) {
        this.reasonForRejection = reasonForRejection;
    }

    public int getRegistered() {
        return registered;
    }

    public void setRegistered(int registered) {
        this.registered = registered;
    }

    public int getInProcess() {
        return inProcess;
    }

    public void setInProcess(int inProcess) {
        this.inProcess = inProcess;
    }

    public int getAddressed() {
        return addressed;
    }

    public void setAddressed(int addressed) {
        this.addressed = addressed;
    }

    public int getRejected() {
        return rejected;
    }

    public void setRejected(int rejected) {
        this.rejected = rejected;
    }

    public int getReOpened() {
        return reOpened;
    }

    public void setReOpened(int reOpened) {
        this.reOpened = reOpened;
    }

    public String getCurrentFunctionaryMobileNumber() {
        return currentFunctionaryMobileNumber;
    }

    public void setCurrentFunctionaryMobileNumber(String currentFunctionaryMobileNumber) {
        this.currentFunctionaryMobileNumber = currentFunctionaryMobileNumber;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
}
