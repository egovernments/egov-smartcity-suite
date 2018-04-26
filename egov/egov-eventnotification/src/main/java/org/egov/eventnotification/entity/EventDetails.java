package org.egov.eventnotification.entity;

import java.util.Date;

import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

public class EventDetails {

    @Transient
    private String startHH;

    @Transient
    private String startMM;

    @Transient
    private String endHH;

    @Transient
    private String endMM;

    @Transient
    private double lng;

    @Transient
    private double lat;

    @Transient
    private Long crossHierarchyId;

    @Transient
    private Date startDt;

    @Transient
    private Date endDt;

    @Transient
    private MultipartFile[] file;

    public String getStartHH() {
        return startHH;
    }

    public void setStartHH(String startHH) {
        this.startHH = startHH;
    }

    public String getStartMM() {
        return startMM;
    }

    public void setStartMM(String startMM) {
        this.startMM = startMM;
    }

    public String getEndHH() {
        return endHH;
    }

    public void setEndHH(String endHH) {
        this.endHH = endHH;
    }

    public String getEndMM() {
        return endMM;
    }

    public void setEndMM(String endMM) {
        this.endMM = endMM;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Long getCrossHierarchyId() {
        return crossHierarchyId;
    }

    public void setCrossHierarchyId(Long crossHierarchyId) {
        this.crossHierarchyId = crossHierarchyId;
    }

    public Date getStartDt() {
        return startDt;
    }

    public void setStartDt(Date startDt) {
        this.startDt = startDt;
    }

    public Date getEndDt() {
        return endDt;
    }

    public void setEndDt(Date endDt) {
        this.endDt = endDt;
    }

    public MultipartFile[] getFile() {
        return file;
    }

    public void setFile(MultipartFile[] file) {
        this.file = file;
    }

}
