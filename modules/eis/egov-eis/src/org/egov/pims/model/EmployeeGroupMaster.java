package org.egov.pims.model;


@SuppressWarnings("serial")
public class EmployeeGroupMaster implements GenericMaster {
public Integer id;
public String name;
public Integer orderNumber;
public java.util.Date fromDate;
public java.util.Date toDate;

public java.util.Date getFromDate() {
	return fromDate;
}
public void setFromDate(java.util.Date fromDate) {
	this.fromDate = fromDate;
}
public java.util.Date getToDate() {
	return toDate;
}
public void setToDate(java.util.Date toDate) {
	this.toDate = toDate;
}

public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public Integer getOrderNumber() {
	return orderNumber;
}
public void setOrderNumber(Integer orderNumber) {
	this.orderNumber = orderNumber;
}

}
