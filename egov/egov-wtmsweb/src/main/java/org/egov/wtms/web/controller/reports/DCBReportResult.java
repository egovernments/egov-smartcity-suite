package org.egov.wtms.web.controller.reports;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.wtms.masters.entity.enums.ConnectionType;

public class DCBReportResult {
    
    
    private ConnectionType connectionType;
    private List<Boundary> zones;
      
    private String boundaryName; 
    private Integer id;
    private Integer boundaryId;
    private Integer propertyid; 
    private String address;
    private String hscno;
    private String username;
    private Integer zoneid;
    private Integer wardid;
    private Integer block;
    private Integer locality;
    private Integer street;
    private String connectiontype;
    private BigInteger curr_demand = BigInteger.ZERO;
    private BigInteger arr_demand = BigInteger.ZERO;
    private BigInteger total_demand = BigInteger.ZERO;
    
    private BigInteger curr_coll = BigInteger.ZERO;
    private BigInteger arr_coll = BigInteger.ZERO;
    private BigInteger total_coll = BigInteger.ZERO;
    
    private BigInteger curr_balance = BigInteger.ZERO;
    private BigInteger arr_balance = BigInteger.ZERO;
    private BigInteger total_balance = BigInteger.ZERO;
    
    
    public Integer getId() {
        return this.zoneid!=null?this.zoneid:(this.wardid!=null?this.wardid:(this.block!=null?this.block:null));
    }
    public void setId(Integer id) {
        this.id = id;
    }
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
    public Integer getPropertyid() {
        return propertyid;
    }
    public void setPropertyid(Integer propertyid) {
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
    public Integer getZoneid() {
        return zoneid;
    }
    public void setZoneid(Integer zoneid) {
        this.zoneid = zoneid;
    }
    public Integer getWardid() {
        return wardid;
    }
    public void setWardid(Integer wardid) {
        this.wardid = wardid;
    }
    public Integer getBlock() {
        return block;
    }
    public void setBlock(Integer block) {
        this.block = block;
    }
    public Integer getLocality() {
        return locality;
    }
    public void setLocality(Integer locality) {
        this.locality = locality;
    }
    public Integer getStreet() {
        return street;
    }
    public void setStreet(Integer street) {
        this.street = street;
    }
    public String getConnectiontype() {
        return connectiontype;
    }
    public void setConnectiontype(String connectiontype) {
        this.connectiontype = connectiontype;
    }
    public BigInteger getCurr_demand() {
        return curr_demand;
    }
    public void setCurr_demand(BigInteger curr_demand) {
        this.curr_demand = curr_demand;
    }
    public BigInteger getArr_demand() {
        return arr_demand;
    }
    public void setArr_demand(BigInteger arr_demand) {
        this.arr_demand = arr_demand;
    }
    public BigInteger getTotal_demand() {
        return (this.curr_demand==null?BigInteger.ZERO:this.curr_demand).add(this.arr_demand==null?BigInteger.ZERO:this.arr_demand);
    }
    public void setTotal_demand(BigInteger total_demand) {
        this.total_demand = total_demand;
    }
    public BigInteger getCurr_coll() {
        return curr_coll;
    }
    public void setCurr_coll(BigInteger curr_coll) {
        this.curr_coll = curr_coll;
    }
    public BigInteger getArr_coll() {
        return arr_coll;
    }
    public void setArr_coll(BigInteger arr_coll) {
        this.arr_coll = arr_coll;
    }
    public BigInteger getTotal_coll() {
        return (this.curr_coll==null?BigInteger.ZERO:this.curr_coll).add(this.arr_coll==null?BigInteger.ZERO:this.arr_coll);
    }
    public void setTotal_coll(BigInteger total_coll) {
        this.total_coll = total_coll;
    }
    public BigInteger getCurr_balance() {
        return (this.curr_demand==null?BigInteger.ZERO:this.curr_demand).subtract(this.curr_coll==null?BigInteger.ZERO:this.curr_coll);
    }
    public void setCurr_balance(BigInteger curr_balance) {
        this.curr_balance = curr_balance;
    }
    public BigInteger getArr_balance() {
        return (this.arr_demand==null?BigInteger.ZERO:this.arr_demand).subtract(this.arr_coll==null?BigInteger.ZERO:this.arr_coll);
    }
    public void setArr_balance(BigInteger arr_balance) {
        this.arr_balance = arr_balance;
    }
    public BigInteger getTotal_balance() {
        return (this.curr_balance==null?BigInteger.ZERO:this.curr_balance).add(this.arr_balance==null?BigInteger.ZERO:this.arr_balance);
    }
    public void setTotal_balance(BigInteger total_balance) {
        this.total_balance = total_balance;
    }

}