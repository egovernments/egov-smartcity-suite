package org.egov.wtms.web.controller.reports;

import java.math.BigDecimal;
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.wtms.masters.entity.enums.ConnectionType;

public class DCBReportResult {
    
    
    private ConnectionType connectionType;
    private List<Boundary> zones;
      
    private String boundaryName; 
    private Integer boundaryId;
    private Long propertyid; 
    private String address;
    private String hscno;
    private String username;
    private Long zoneid;
    private Long wardid;
    private Long block;
    private Long locality;
    private Long street;
    private String connectiontype;
    private BigDecimal curr_demand = BigDecimal.ZERO;
    private BigDecimal arr_demand = BigDecimal.ZERO;
    private BigDecimal total_demand = BigDecimal.ZERO;
    
    private BigDecimal curr_coll = BigDecimal.ZERO;
    private BigDecimal arr_coll = BigDecimal.ZERO;
    private BigDecimal total_coll = BigDecimal.ZERO;
    
    private BigDecimal curr_balance = BigDecimal.ZERO;
    private BigDecimal arr_balance = BigDecimal.ZERO;
    private BigDecimal total_balance = BigDecimal.ZERO;
    
    
    public ConnectionType getConnectionType() {
        return connectionType;
    }
    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }
    public List<Boundary> getZones() {
        return zones;
    }
    public void setZones(List<Boundary> zones) {
        this.zones = zones;
    }
    public String getBoundaryName() {
        return boundaryName;
    }
    public void setBoundaryName(String boundaryName) {
        this.boundaryName = boundaryName;
    }
    public Integer getBoundaryId() {
        return boundaryId;
    }
    public void setBoundaryId(Integer boundaryId) {
        this.boundaryId = boundaryId;
    }
    public Long getPropertyid() {
        return propertyid;
    }
    public void setPropertyid(Long propertyid) {
        this.propertyid = propertyid;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getHscno() {
        return hscno;
    }
    public void setHscno(String hscno) {
        this.hscno = hscno;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Long getZoneid() {
        return zoneid;
    }
    public void setZoneid(Long zoneid) {
        this.zoneid = zoneid;
    }
    public Long getWardid() {
        return wardid;
    }
    public void setWardid(Long wardid) {
        this.wardid = wardid;
    }
    public Long getBlock() {
        return block;
    }
    public void setBlock(Long block) {
        this.block = block;
    }
    public Long getLocality() {
        return locality;
    }
    public void setLocality(Long locality) {
        this.locality = locality;
    }
    public Long getStreet() {
        return street;
    }
    public void setStreet(Long street) {
        this.street = street;
    }
    public String getConnectiontype() {
        return connectiontype;
    }
    public void setConnectiontype(String connectiontype) {
        this.connectiontype = connectiontype;
    }
    public BigDecimal getCurr_demand() {
        return curr_demand;
    }
    public void setCurr_demand(BigDecimal curr_demand) {
        this.curr_demand = curr_demand;
    }
    public BigDecimal getArr_demand() {
        return arr_demand;
    }
    public void setArr_demand(BigDecimal arr_demand) {
        this.arr_demand = arr_demand;
    }
    public BigDecimal getTotal_demand() {
        return (this.curr_demand==null?BigDecimal.ZERO:this.curr_demand).add(this.arr_demand==null?BigDecimal.ZERO:this.arr_demand);
    }
    public void setTotal_demand(BigDecimal total_demand) {
        this.total_demand = total_demand;
    }
    public BigDecimal getCurr_coll() {
        return curr_coll;
    }
    public void setCurr_coll(BigDecimal curr_coll) {
        this.curr_coll = curr_coll;
    }
    public BigDecimal getArr_coll() {
        return arr_coll;
    }
    public void setArr_coll(BigDecimal arr_coll) {
        this.arr_coll = arr_coll;
    }
    public BigDecimal getTotal_coll() {
        return (this.curr_coll==null?BigDecimal.ZERO:this.curr_coll).add(this.arr_coll==null?BigDecimal.ZERO:this.arr_coll);
    }
    public void setTotal_coll(BigDecimal total_coll) {
        this.total_coll = total_coll;
    }
    public BigDecimal getCurr_balance() {
        return (this.curr_demand==null?BigDecimal.ZERO:this.curr_demand).subtract(this.curr_coll==null?BigDecimal.ZERO:this.curr_coll);
    }
    public void setCurr_balance(BigDecimal curr_balance) {
        this.curr_balance = curr_balance;
    }
    public BigDecimal getArr_balance() {
        return (this.arr_demand==null?BigDecimal.ZERO:this.arr_demand).subtract(this.arr_coll==null?BigDecimal.ZERO:this.arr_coll);
    }
    public void setArr_balance(BigDecimal arr_balance) {
        this.arr_balance = arr_balance;
    }
    public BigDecimal getTotal_balance() {
        return (this.curr_balance==null?BigDecimal.ZERO:this.curr_balance).add(this.arr_balance==null?BigDecimal.ZERO:this.arr_balance);
    }
    public void setTotal_balance(BigDecimal total_balance) {
        this.total_balance = total_balance;
    }

}