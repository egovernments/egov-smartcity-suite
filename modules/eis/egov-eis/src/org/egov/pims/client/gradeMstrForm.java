package org.egov.pims.client;


import org.apache.struts.action.ActionForm;

public class gradeMstrForm extends ActionForm 
{
  private String gradeId;
  private String gradeVal;
  private String startDate;
  private String endDate;
  private String gradeAge;
  private int orderNo;
  

public int getOrderNo() {
	return orderNo;
}
public void setOrderNo(int orderNo) {
	this.orderNo = orderNo;
}
public String getGradeId() {
	return gradeId;
}
public void setGradeId(String gradeId) {
	this.gradeId = gradeId;
}
public String getGradeVal() {
	return gradeVal;
}
public void setGradeVal(String gradeVal) {
	this.gradeVal = gradeVal;
}
public String getStartDate() {
	return startDate;
}
public void setStartDate(String startDate) {
	this.startDate = startDate;
}
public String getEndDate() {
	return endDate;
}
public void setEndDate(String endDate) {
	this.endDate = endDate;
}
public String getGradeAge() {
	return gradeAge;
}
public void setGradeAge(String gradeAge) {
	this.gradeAge = gradeAge;
}
  
}
