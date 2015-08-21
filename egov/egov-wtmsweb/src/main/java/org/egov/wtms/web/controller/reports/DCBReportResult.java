package org.egov.wtms.web.controller.reports;

import java.math.BigInteger;
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.wtms.masters.entity.enums.ConnectionType;

public class DCBReportResult {

    private ConnectionType connectionType;
    private List<Boundary> zones;

    private String boundaryName;
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
    private BigInteger curr_coll = BigInteger.ZERO;
    private BigInteger arr_coll = BigInteger.ZERO;
    private BigInteger curr_balance = BigInteger.ZERO;
    private BigInteger arr_balance = BigInteger.ZERO;

    public Integer getId() {
        return zoneid != null ? zoneid : wardid != null ? wardid : block != null ? block : null;
    }

    public void setId(final Integer id) {
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public List<Boundary> getZones() {
        return zones;
    }

    public void setZones(final List<Boundary> zones) {
        this.zones = zones;
    }

    public String getBoundaryName() {
        return boundaryName;
    }

    public void setBoundaryName(final String boundaryName) {
        this.boundaryName = boundaryName;
    }

    public Integer getBoundaryId() {
        return boundaryId;
    }

    public void setBoundaryId(final Integer boundaryId) {
        this.boundaryId = boundaryId;
    }

    public Integer getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(final Integer propertyid) {
        this.propertyid = propertyid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getHscno() {
        return hscno;
    }

    public void setHscno(final String hscno) {
        this.hscno = hscno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Integer getZoneid() {
        return zoneid;
    }

    public void setZoneid(final Integer zoneid) {
        this.zoneid = zoneid;
    }

    public Integer getWardid() {
        return wardid;
    }

    public void setWardid(final Integer wardid) {
        this.wardid = wardid;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(final Integer block) {
        this.block = block;
    }

    public Integer getLocality() {
        return locality;
    }

    public void setLocality(final Integer locality) {
        this.locality = locality;
    }

    public Integer getStreet() {
        return street;
    }

    public void setStreet(final Integer street) {
        this.street = street;
    }

    public String getConnectiontype() {
        return connectiontype;
    }

    public void setConnectiontype(final String connectiontype) {
        this.connectiontype = connectiontype;
    }

    public BigInteger getCurr_demand() {
        return curr_demand;
    }

    public void setCurr_demand(final BigInteger curr_demand) {
        this.curr_demand = curr_demand;
    }

    public BigInteger getArr_demand() {
        return arr_demand;
    }

    public void setArr_demand(final BigInteger arr_demand) {
        this.arr_demand = arr_demand;
    }

    public BigInteger getTotal_demand() {
        return (curr_demand == null ? BigInteger.ZERO : curr_demand).add(arr_demand == null ? BigInteger.ZERO : arr_demand);
    }

    public void setTotal_demand(final BigInteger total_demand) {
    }

    public BigInteger getCurr_coll() {
        return curr_coll;
    }

    public void setCurr_coll(final BigInteger curr_coll) {
        this.curr_coll = curr_coll;
    }

    public BigInteger getArr_coll() {
        return arr_coll;
    }

    public void setArr_coll(final BigInteger arr_coll) {
        this.arr_coll = arr_coll;
    }

    public BigInteger getTotal_coll() {
        return (curr_coll == null ? BigInteger.ZERO : curr_coll).add(arr_coll == null ? BigInteger.ZERO : arr_coll);
    }

    public void setTotal_coll(final BigInteger total_coll) {
    }

    public BigInteger getCurr_balance() {
        return (curr_demand == null ? BigInteger.ZERO : curr_demand).subtract(curr_coll == null ? BigInteger.ZERO : curr_coll);
    }

    public void setCurr_balance(final BigInteger curr_balance) {
        this.curr_balance = curr_balance;
    }

    public BigInteger getArr_balance() {
        return (arr_demand == null ? BigInteger.ZERO : arr_demand).subtract(arr_coll == null ? BigInteger.ZERO : arr_coll);
    }

    public void setArr_balance(final BigInteger arr_balance) {
        this.arr_balance = arr_balance;
    }

    public BigInteger getTotal_balance() {
        return (curr_balance == null ? BigInteger.ZERO : curr_balance).add(arr_balance == null ? BigInteger.ZERO : arr_balance);
    }

    public void setTotal_balance(final BigInteger total_balance) {
    }

}